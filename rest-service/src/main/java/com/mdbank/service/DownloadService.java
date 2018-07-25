package com.mdbank.service;

import com.mdbank.exception.DownloadException;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

/**
 * Сервис для скачивания файлов по HTTP#GET
 */
@Service
public class DownloadService {

    private final HttpClient httpClient;

    @Autowired

    public DownloadService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

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
    public Path download(Path destinationPath, String link) throws DownloadException {
        try {
            URL url = new URL(link);
            HttpGet get = new HttpGet(url.toURI());
            return httpClient.execute(get, response -> saveStreamToFile(response.getEntity().getContent(), destinationPath));
        } catch (Exception e) {
            throw new DownloadException(e);
        }
    }

    @SneakyThrows
    private Path saveStreamToFile(InputStream source, Path destination) {
        File dstFile = destination.toFile();
        FileUtils.copyInputStreamToFile(source, dstFile);
        return destination;
    }
}
