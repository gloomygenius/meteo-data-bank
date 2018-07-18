package com.mdbank.service;

import org.apache.commons.io.FileUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

/**
 * Сервис для скачивания nc4 файлов с серверов NASA
 */
@Service
public class DownloadService {

    private final BasicCredentialsProvider basicCredentialsProvider;

    public DownloadService(@Value("${nasa.login}") String login,
                               @Value("${nasa.password}") String password) {

        if (login == null || password == null) {
            throw new IllegalArgumentException("login or password is null");
        }

        basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(login, password));
    }

    public Path download(Path path, String link) {

        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods
                .setDefaultCredentialsProvider(basicCredentialsProvider)
                .build()) {

            URL url = new URL(link);
            HttpGet get = new HttpGet(url.toURI()); // we're using GET but it could be via POST as well

            return httpclient.execute(get, response -> {
                File dstFile = path.toFile();
                InputStream source = response.getEntity().getContent();
                FileUtils.copyInputStreamToFile(source, dstFile);
                return dstFile;
            }).toPath();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
