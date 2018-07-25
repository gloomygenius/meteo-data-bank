package com.mdbank.config;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
    @Bean
    public HttpClient closableHttpClient(NasaConfig nasaConfig) {
        CredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();

        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(
                nasaConfig.getLogin(),
                nasaConfig.getPassword());

        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setRedirectStrategy(new LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods
                .setDefaultCredentialsProvider(basicCredentialsProvider)
                .build();

        return closeableHttpClient;
    }
}
