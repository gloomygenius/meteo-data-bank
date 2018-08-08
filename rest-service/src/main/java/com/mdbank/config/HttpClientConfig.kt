package com.mdbank.config

import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.client.LaxRedirectStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClientConfig {
    @Bean
    fun closableHttpClient(nasaConfig: NasaConfig): HttpClient {
        val credentials = UsernamePasswordCredentials(nasaConfig.login, nasaConfig.password)
        val credentialsProvider = BasicCredentialsProvider().also { it.setCredentials(AuthScope.ANY, credentials) }

        return HttpClients.custom()
                .setRedirectStrategy(LaxRedirectStrategy()) // adds HTTP REDIRECT support to GET and POST methods
                .setDefaultCredentialsProvider(credentialsProvider)
                .build()
    }
}