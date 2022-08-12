package com.dizhongdi.servicees;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.*;
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

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;

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

    public static void main(String[] args) throws Exception {
        //初始化链接
        initESConnection();
        //操作索引
        //operationIndex();
        //Lambda
        initESConnectionLambda();


        //操作文档
//        operationDocument();
//        operationDocumentLambda();

        //查询文档
//        queryDocument();
//        queryDocumentLambda();

        //异步操作
//        asyncClientOperation();

    }

    public static void asyncClientOperation() throws Exception {
        asyncClient.indices().create(req -> req.index("newindex3")).
                thenApply(
                        resp -> resp.acknowledged()
                ).
                whenComplete(
                        (resp,error) ->{
                            System.out.println("回调方法");
                            if (resp!=null){
                                System.out.println(resp);
                            }
                            try {
                                transport.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
        System.out.println("主线程..");
    }



    public static void queryDocumentLambda() throws Exception {
        client.search(
                req -> {
                    req.query(
                            q ->
                                    q.match(
                                            m -> m.field("age").query("18")
                                    )
                    );
                    return req;
                }
                , Object.class
        ).hits();
        transport.close();
    }

    public static void queryDocument() throws Exception {
        Query query = new Query.Builder().match(new MatchQuery.Builder().field("age").query(18).build()).build();
        SearchResponse response = client.search(new SearchRequest.Builder().index("dizhongdi").query(query).build(),Object.class);
        System.out.println(response);
        transport.close();
    }

        //文档操作
    public static void operationDocumentLambda() throws Exception {
//        CreateResponse response = client.create(req -> req.index("dizhongdi").
//                document(new User(1002,"zhangsan",18)).id("1002").build());
//        System.out.println(response);
    }

    public static void operationDocument() throws Exception {
        CreateRequest<User> request = new CreateRequest.Builder<User>().
                index("dizhongdi")
                .document(new User(1001,"zhangsan",18))
                .id("1001")
                .build();

        //文档插入
        CreateResponse response = client.create(request);
        System.out.println(response.result());

        //批量插入
        ArrayList<BulkOperation> arrayList = new ArrayList<>();
        for (Integer i = 3; i < 9; i++) {
            CreateOperation<User> build = new CreateOperation.Builder<User>()
                    .index("dizhongdi").
                            document(new User(i, "asd", i)).
                            id("100"+i.toString())
                    .build();
            arrayList.add(new BulkOperation.Builder().create(build).build());
        }
        BulkResponse bulkResponse = client.bulk(new BulkRequest.Builder().operations(arrayList).build());
        System.out.println("数据操作成功：" + bulkResponse);

        //删除文档
        DeleteRequest deleteRequest = new DeleteRequest.Builder().index("dizhongdi").id("1001").build();
        System.out.println(deleteRequest);

        transport.close();
    }

    public static void initESConnectionLambda() throws Exception {
        //获取索引客户端对象
        final ElasticsearchIndicesClient indices = client.indices();

        boolean flag = indices.exists(req -> req.index("dizhongdi")).value();
        if (flag){
            System.out.println("索引已经存在");
        }else {
            CreateIndexResponse response = indices.create(request -> request.index("dizhongdi"));
            System.out.println("创建索引成功: "+response);
        }

        //查询索引
        GetIndexResponse getIndexResponse = indices.get(getIndexRequest -> getIndexRequest.index("dizhongdi"));
        System.out.println("索引查询成功：" +getIndexResponse.result());

        // 删除索引
        DeleteIndexResponse deleteIndexResponse = indices.delete(deleteIndexRequest -> deleteIndexRequest.index("dizhongdi"));
        System.out.println("删除索引成功：" + deleteIndexResponse.acknowledged());
        transport.close();
    }



        public static void operationIndex() throws Exception {
        //获取索引客户端对象
        final ElasticsearchIndicesClient indices = client.indices();
        ExistsRequest existsRequest = new ExistsRequest.Builder().index("dizhongdi").build();

        boolean flag = indices.exists(existsRequest).value();
        if (flag){
            System.out.println("索引已经存在");
        }else {
            CreateIndexRequest request = new CreateIndexRequest.Builder().index("dizhongdi").build();
            CreateIndexResponse response = indices.create(request);
            System.out.println("创建索引成功: "+response);
        }

        //查询索引
        GetIndexRequest getIndexRequest = new GetIndexRequest.Builder().index("dizhongdi").build();
        GetIndexResponse getIndexResponse = indices.get(getIndexRequest);
        System.out.println("索引查询成功：" +getIndexResponse.result());

        // 删除索引
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index("dizhongdi").build();
        DeleteIndexResponse deleteIndexResponse = indices.delete(deleteIndexRequest);
        System.out.println("删除索引成功：" + deleteIndexResponse.acknowledged());
        transport.close();
    }


    public static void initESConnection() throws Exception {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "gkgdHELd2*Zb-85lEgcw"));
        Path caCertificatePath = Paths.get("service/service-es/target/classes/certs/java-ca.crt");
        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");
        Certificate trustedCa;
        try (InputStream is = Files.newInputStream(caCertificatePath)) {
            trustedCa = factory.generateCertificate(is);
        }
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
        client = new ElasticsearchClient(transport);
        //异步
        asyncClient = new ElasticsearchAsyncClient(transport);
//        transport.close();
    }

}
