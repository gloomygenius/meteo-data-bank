package com.mdbank.service

import com.mdbank.exception.repository.EntityNotFoundException
import com.mdbank.model.FetchDataTask
import com.mdbank.repository.DataMetaInfoRepository
import com.mdbank.repository.DataSourceInfoRepository
import com.mdbank.repository.FetchDataTaskRepository
import com.mdbank.util.NetCdfProcessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Paths
import java.time.LocalDate
import java.util.concurrent.BlockingQueue
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service
class GlobalDataService @Autowired constructor(val dataSourceInfoRepository: DataSourceInfoRepository,
                                               val fetchDataTaskRepository: FetchDataTaskRepository) {
    private val executorService = Executors.newFixedThreadPool(1)
    private val taskQueue: BlockingQueue<FetchDataTask> = LinkedBlockingQueue<FetchDataTask>()

    @Suppress("unused")
    @PostConstruct
    fun init(@Autowired downloadService: DownloadService,
             @Autowired netCdfProcessor: NetCdfProcessor,
             @Autowired dataMetaInfoRepository: DataMetaInfoRepository,
             @Autowired positionService: PositionService,
             @Autowired localDataService: LocalDataService,
             @Value("downloadservice.path") path: String) {

        fetchDataTaskRepository.findAll().forEach { taskQueue.put(it) }
        executorService.execute {
            while (true) {
                val task = taskQueue.poll()
                task.generateLinks().forEach {
                    val downloadedFile = downloadService.download(Paths.get(path), it)
                    val dataMetaInfoList = dataMetaInfoRepository.findByDataSourceInfo(task.source)
                    dataMetaInfoList.forEach { metaInfo ->
                        val globalData = netCdfProcessor.readFromNc4ToGlobalData(downloadedFile, metaInfo)
                        positionService.forEachPosition { position ->
                            val localData = globalData.toLocalData(position)
                            localDataService.save(localData)
                        }
                    }
                    task.processedFiles++
                    fetchDataTaskRepository.save(task)
                }
            }
        }
    }

    @PreDestroy
    fun destroy() {
        if (!executorService.isShutdown) {
            executorService.shutdown()
        }
    }

    /**
     * Добавление задания на обновление данных для диапазона дат [startDate..endDate)
     *
     * @param sourceId - идентификатор сущности источника данных
     */
    @Transactional
    fun fetchFromSource(startDate: LocalDate, endDate: LocalDate, sourceId: Long) {
        val sourceInfo = dataSourceInfoRepository.findById(sourceId)
                .orElseThrow { EntityNotFoundException("DataMetaSource with id $sourceId doesn't exist") }
        FetchDataTask(startDate, endDate, sourceInfo)
                .let { fetchDataTaskRepository.save(it) }
                .also { taskQueue.put(it) }
    }
}