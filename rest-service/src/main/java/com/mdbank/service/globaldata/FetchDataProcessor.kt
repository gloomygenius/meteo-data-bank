package com.mdbank.service.globaldata

import com.mdbank.model.TaskStatus
import com.mdbank.model.metadata.DataMetaInfo
import com.mdbank.repository.DataMetaInfoRepository
import com.mdbank.service.DownloadService
import com.mdbank.service.LocalDataService
import com.mdbank.service.PositionService
import com.mdbank.util.NetCdfProcessor
import org.aspectj.weaver.tools.cache.SimpleCacheFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.Executors
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Component
class FetchDataProcessor @Autowired constructor(val taskQueue: TaskQueue,
                                                val downloadService: DownloadService,
                                                val dataMetaInfoRepository: DataMetaInfoRepository,
                                                val netCdfProcessor: NetCdfProcessor,
                                                val positionService: PositionService,
                                                val localDataService: LocalDataService) {

    private val executorService = Executors.newFixedThreadPool(1)

    @PreDestroy
    fun destroy() {
        if (!executorService.isShutdown) {
            executorService.shutdown()
        }
    }

    @PostConstruct
    fun init() {
        executorService.execute { scanQueueAndFetchData() }
    }

    fun scanQueueAndFetchData() {
        while (true) {
            val task = taskQueue.take()
            try {
                val link = task.generateLink()
                val downloadedFile = downloadService.download(Paths.get(SimpleCacheFactory.path), link)
                val dataMetaInfoListRelatedToThisSource = dataMetaInfoRepository.findBySourceInfo(task.source)
                dataMetaInfoListRelatedToThisSource.forEach { metaInfo -> readDataFromNc4FileAndSaveToLocalData(downloadedFile, metaInfo) }
                task.status = TaskStatus.PROCESSED
            } catch (e: Exception) {
                taskQueue.putExistingTask(task)
            }
        }
    }

    fun readDataFromNc4FileAndSaveToLocalData(file: Path, metaInfo: DataMetaInfo) {
        val globalData = netCdfProcessor.readFromNc4ToGlobalData(file, metaInfo)
        positionService.forEachPosition { position ->
            globalData.toLocalData(position).let { localData -> localDataService.save(localData) }
        }
    }
}