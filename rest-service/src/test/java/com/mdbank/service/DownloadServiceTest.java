package com.mdbank.service;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DownloadServiceTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private DownloadService downloadService;
    private final static String expectedContent = "test content";

    @Before
    public void init() {
        HttpClient mockHttpClient = mock(HttpClient.class,
                invocation -> {
                    ResponseHandler<Path> responseHandler = invocation.getArgument(1);

                    HttpEntity httpEntity = mock(HttpEntity.class);
                    when(httpEntity.getContent()).thenReturn(new ByteArrayInputStream(expectedContent.getBytes()));

                    HttpResponse response = mock(HttpResponse.class);
                    when(response.getEntity()).thenReturn(httpEntity);

                    return responseHandler.handleResponse(response);
                });

        downloadService = new DownloadService(mockHttpClient);
    }

    @Test
    @SneakyThrows
    public void assertThatDownloadedContentIsSavedOnDisk() {
        Path destinationFilePath = folder.newFile("test.txt").toPath();

        Path downloadedFilePath = downloadService.download(destinationFilePath, "http://example.com");

        String downloadedContent = FileUtils.readFileToString(downloadedFilePath.toFile(), StandardCharsets.UTF_8);
        assertThat(downloadedContent, is(expectedContent));
    }
}