package design.kfu.sunrise.config;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.support.HttpHeaders;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author Daniyar Zakiev
 */
@Slf4j
@Configuration
@EnableElasticsearchRepositories(basePackages
        = "design.kfu.sunrise.esrepository.elastic")
public class ElasticsearchConfig  extends ElasticsearchConfiguration {


//    @Autowired
//    private RestHighLevelClient restHighLevelClient;


    private static final String CERT_PASSWORD = "topsecret";


    @Value("${spring.data.elasticsearch.properties.host}")
    private String esHost;

    @Value("${spring.data.elasticsearch.properties.port}")
    private int esPort;

    @Override
    public ClientConfiguration clientConfiguration() {
                TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                            throws CertificateException {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                            throws CertificateException {}
                }
        };

        SSLContext sc=null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier validHosts = new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };


        ClientConfiguration clientConfiguration
                = ClientConfiguration.builder()
                .connectedTo(esHost + ":" + esPort)
                .withConnectTimeout(30000)
                .withSocketTimeout(30000)
//                .usingSsl()
//                .usingSsl(sc, validHosts)
//                .withBasicAuth("elastic", "xkDo61ZhpJj_eCohJS3f")
                .withDefaultHeaders(compatibilityHeaders())    // this variant for imperative code
//                .withHeaders(() -> compatibilityHeaders())     // this variant for reactive code
//                .withDefaultHeaders(compatibilityHeaders())
//                .connectedTo("dev-elastic:9300")
                .build();
        return clientConfiguration;
    }

//    public ElasticsearchClient cl() {
//        RestClient restClient = RestClient.builder(
//                new HttpHost("localhost", 9200)).build();
//
//// Create the transport with a Jackson mapper
//// And create the API client
//        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//        ElasticsearchClient client = new ElasticsearchClient(transport);
//    }
//    @SneakyThrows
//    @Bean
//    public RestHighLevelClient client() {
//        TrustManager[] trustAllCerts = new TrustManager[] {
//                new X509TrustManager() {
//                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
//                    @Override
//                    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
//                            throws CertificateException {}
//
//                    @Override
//                    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                            throws CertificateException {}
//                }
//        };
//
//        SSLContext sc=null;
//        try {
//            sc = SSLContext.getInstance("SSL");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        try {
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//        HostnameVerifier validHosts = new HostnameVerifier() {
//            @Override
//            public boolean verify(String arg0, SSLSession arg1) {
//                return true;
//            }
//        };
//
////ToDo Elastic почему-то все равно подключается по localhost
//        ClientConfiguration clientConfiguration
//                = ClientConfiguration.builder()
//                .connectedTo(esHost + ":" + esPort)
////                .usingSsl(sc, validHosts)
////                .withBasicAuth("elastic", "xkDo61ZhpJj_eCohJS3f")
////                .withDefaultHeaders(compatibilityHeaders())    // this variant for imperative code
////                .withHeaders(() -> compatibilityHeaders())     // this variant for reactive code
////                .withDefaultHeaders(compatibilityHeaders())
////                .connectedTo("dev-elastic:9300")
//                .build();
//
//        RestHighLevelClient esClient = new RestHighLevelClientBuilder(RestClients.create(clientConfiguration).rest().getLowLevelClient())
////                .setApiCompatibilityMode(true)
//                .build();
//        return esClient;
////        return RestClients.create(clientConfiguration).rest();
//
//
////        final CredentialsProvider credentialsProvider =
////                new BasicCredentialsProvider();
////        credentialsProvider.setCredentials(AuthScope.ANY,
////                new UsernamePasswordCredentials("elastic", "xkDo61ZhpJj_eCohJS3f"));
////
////        RestClientBuilder builder = RestClient.builder(
////                        new HttpHost("localhost", 9200))
////                .setHttpClientConfigCallback(new HttpClientConfigCallback() {
////                    @Override
////                    public HttpAsyncClientBuilder customizeHttpClient(
////                            HttpAsyncClientBuilder httpClientBuilder) {
////                        httpClientBuilder.disableAuthCaching();
////                        return httpClientBuilder
////                                .setDefaultCredentialsProvider(credentialsProvider);
////                    }
////                });
//    }

    private HttpHeaders compatibilityHeaders() {
        HttpHeaders compatibilityHeaders = new HttpHeaders();
        compatibilityHeaders.add("Accept", "application/vnd.elasticsearch+json;compatible-with=7");
        compatibilityHeaders.add("Content-Type", "application/vnd.elasticsearch+json;"
                + "compatible-with=7");
        return compatibilityHeaders;
    }
/*
    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(client());
    }
*/


//    private SSLContext createSSLContext() {
//        try {
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//
//            KeyManager[] keyManagers = getKeyManagers();
//
//            sslContext.init(keyManagers, null, null);
//
//            return sslContext;
//        } catch (Exception e) {
//            log.error("cannot create SSLContext", e);
//        }
//        return null;
//    }
//
//    private KeyManager[] getKeyManagers()
//            throws KeyStoreException, NoSuchAlgorithmException, IOException, CertificateException, UnrecoverableKeyException {
//        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CERT_FILE)) {
//            KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
//            clientKeyStore.load(inputStream, CERT_PASSWORD.toCharArray());
//
//            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
//            kmf.init(clientKeyStore, CERT_PASSWORD.toCharArray());
//            return kmf.getKeyManagers();
//        }
//    }
}
