package com.mdbank.service.globaldata

import com.mdbank.model.FetchDataTask
import com.mdbank.repository.FetchDataTaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.LinkedBlockingQueue
import javax.annotation.PostConstruct

@Component
class TaskQueue @Autowired constructor(val fetchDataTaskRepository: FetchDataTaskRepository) {
    private val queue = LinkedBlockingQueue<FetchDataTask>()

    @Suppress("unused")
    @PostConstruct
    fun init() {
        fetchDataTaskRepository.findAll().forEach { queue.put(it) }
    }

    fun take(): FetchDataTask {
        return queue.take()
    }

    fun putExistingTask(task: FetchDataTask) {
        queue.put(task)
    }

    fun putNew(task: FetchDataTask): FetchDataTask {
        return fetchDataTaskRepository.save(task)
    }

    fun getStatus(): String {
        return queue.toString()
    }
}