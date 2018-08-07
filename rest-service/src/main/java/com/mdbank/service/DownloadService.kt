package com.mdbank.service

import com.mdbank.exception.DownloadException
import lombok.SneakyThrows
import org.apache.commons.io.FileUtils
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream
import java.net.URL
import java.nio.file.Path

@Service
class DownloadService @Autowired constructor(private val httpClient: HttpClient) {

    /**
     * Метод загружает контент по http ссылке и сохраненяет в файл по указанному пути.
     * Файл не обязательно должен существать на момент вызова метода.
     * Если файл уже существует, то он будет перезаисан.
     *
     * @param destinationPath - путь к файлу в котором будет сохранён контент
     * @param link - URI ресурса, к которому будет выполен GET запрос
     * @return путь к сохранённому файлу. Эквивалентен destinationPath.
     * @throws DownloadException в случае ошибки соединения, неверной ссылки или проблем с сохранением контента в файл
     */
    @Throws(DownloadException::class)
    fun download(destinationPath: Path, link: String): Path {
        try {
            val url = URL(link)
            val get = HttpGet(url.toURI())
            return httpClient.execute(get) { response -> saveStreamToFile(response.entity.content, destinationPath) }
        } catch (e: Exception) {
            throw DownloadException(e)
        }
    }

    @SneakyThrows
    private fun saveStreamToFile(source: InputStream, destination: Path): Path {
        val dstFile = destination.toFile()
        FileUtils.copyInputStreamToFile(source, dstFile)
        return destination
    }
}