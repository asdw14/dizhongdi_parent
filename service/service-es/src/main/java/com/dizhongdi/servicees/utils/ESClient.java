package com.dizhongdi.servicees.utils;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * ClassName:ESClient
 * Package:com.dizhongdi.es8
 * Description:
 *
 * @Date: 2022/7/29 22:27
 * @Author:dizhongdi
 */
public class ESClient {
    public static ElasticsearchTransport transport;
    public static ElasticsearchAsyncClient asyncClient;
    public static ElasticsearchClient client;

    public static ElasticsearchClient initESConnection() throws Exception {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "gkgdHELd2*Zb-85lEgcw"));
//        Path caCertificatePath = Paths.get("java-ca.crt");
        ClassPathResource resource = new ClassPathResource("certs/java-ca.crt");
        InputStream inputStream = resource.getInputStream();
        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");
        Certificate trustedCa;

        trustedCa = factory.generateCertificate(inputStream);

//        try (InputStream is = Files.newInputStream(caCertificatePath)) {
//            trustedCa = factory.generateCertificate(is);
//        }

        KeyStore trustStore = KeyStore.getInstance("pkcs12");
        trustStore.load(null, null);
        trustStore.setCertificateEntry("ca", trustedCa);
        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                .loadTrustMaterial(trustStore, null);
        final SSLContext sslContext = sslContextBuilder.build();
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("120.76.118.137", 9200, "https"))
                .setHttpClientConfigCallback(new
                                                     RestClientBuilder.HttpClientConfigCallback() {
                                                         @Override
                                                         public HttpAsyncClientBuilder customizeHttpClient(
                                                                 HttpAsyncClientBuilder httpClientBuilder) {
                                                             return httpClientBuilder.setSSLContext(sslContext)
                                                                     .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                                                     .setDefaultCredentialsProvider(credentialsProvider);
                                                         }
                                                     });
        RestClient restClient = builder.build();
        transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        //同步
        return client = new ElasticsearchClient(transport);
        //异步
//        asyncClient = new ElasticsearchAsyncClient(transport);
    }
    public static void close() throws IOException {
        transport.close();
    }
}
