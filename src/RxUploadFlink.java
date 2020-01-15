import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import lombok.val;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * This example demonstrates a basic asynchronous HTTP request / response
 * exchange. Response content is buffered in memory for simplicity.
 */
public class RxUploadFlink {
    public static void main(final String[] args) {

        String host = "http://172.17.162.177:8081";
        File file = new File("/Users/jianhe/new/riil-biz-data-center/biz-data-center-job/target/biz-data-center-job_1.0-SNAPSHOT.jar");

        Observable<URI> upload = Observable.create(
                (ObservableOnSubscribe<String>) subscriber -> {

                    try (CloseableHttpClient client = HttpClients.createDefault()) {
                        HttpPost post = new HttpPost(host + "/jars/upload");
                        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
                        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                        builder.addPart("jarfile", fileBody);
                        HttpEntity entity = builder.build();
                        post.setEntity(entity);

                        try (CloseableHttpResponse response = client.execute(post)) {

                            System.out.println(response.getStatusLine());

                            entity = response.getEntity();
                            if (entity != null) {
                                System.out.println("Response content length: " + entity.getContentLength());

                                JSONObject json = JSON.parseObject(EntityUtils.toString(entity));
                                System.out.println(json);

                                subscriber.onNext(json.getString("filename"));
                                EntityUtils.consume(entity);
                            }

                        }

                    }


                })
                .map(filename -> {
                    String pattern = ".*/([\\w\\-\\.]+)";
                    Pattern r = Pattern.compile(pattern);
                    return r.matcher(filename);
                })
                .filter(m -> m.find())
                .map(m -> {
                    System.out.println("Found value: " + m.group(0));
                    System.out.println("Found value: " + m.group(1));
                    String jar = m.group(1);
                    URI url = new URIBuilder(host + "/jars/" + jar + "/run")
                            .addParameter("entry-class", "com.riil.baymax.job.NpvTask")
                            .addParameter("program-args", "--task-name riil-npv-task --npv-input-topic TOPIC_GATEWAY_NPV --dns-output-topic flink-dns-out --http-output-topic flink-http-out --tcp-output-topic flink-tcp-out --traffic-output-topic flink-traffic-out --group.id flink-npv --batch_size 20000 --npv_queue_size 32768 --npv_udp_port 10514 --dbUrl jdbc:clickhouse://172.17.162.177:8123/default  --redis-host 172.17.162.177 --bootstrap.servers 172.17.162.177:9092")
                            .build();
                    System.out.println(url);
                    return url;

                })
                .doOnNext(url -> {
                    try (CloseableHttpClient client = HttpClients.createDefault()) {
                        HttpPost post = new HttpPost(url.toString());
                        try (CloseableHttpResponse response = client.execute(post)) {
                            System.out.println(response.getStatusLine());

                            HttpEntity entity = response.getEntity();
                            if (entity != null) {
                                System.out.println("Response content length: " + entity.getContentLength());
                                System.out.println(EntityUtils.toString(entity));

                                EntityUtils.consume(entity);
                            }

                        }


                    }
                });

        Observable.create(
                (ObservableOnSubscribe<JSONObject>) subscriber -> {
                    try (CloseableHttpClient client = HttpClients.createDefault()) {
                        HttpGet get = new HttpGet(host + "/jobs/overview");

                        try (CloseableHttpResponse response = client.execute(get)) {

                            System.out.println(response.getStatusLine());

                            HttpEntity entity = response.getEntity();

                            if (entity == null) {
                                throw new RuntimeException("no result");

                            }

                            System.out.println("Response content length: " + entity.getContentLength());
                            JSONObject json = JSON.parseObject(EntityUtils.toString(entity));
                            JSONArray jsonArray = json.getJSONArray("jobs");
                            System.out.println(jsonArray);

                            EntityUtils.consume(entity);

                            for (int i = 0; i < jsonArray.size(); i++) {

                                subscriber.onNext(jsonArray.getJSONObject(i));
                            }
                        }

                    }


                    subscriber.onComplete();

                })
                .filter(json -> !json.getString("state").equals("CANCELED"))
                .map(json -> json.getString("jid"))
                .doOnNext(jid -> {
                    try (CloseableHttpClient client = HttpClients.createDefault()) {
                        HttpGet get = new HttpGet(host + "/jobs/" + jid + "/yarn-cancel");

                        try (CloseableHttpResponse response = client.execute(get)) {

                            System.out.println(response.getStatusLine());

                            HttpEntity entity = response.getEntity();

                            if (entity == null) {
                                throw new RuntimeException("no result");

                            }

                            System.out.println("Response content length: " + entity.getContentLength());

                            System.out.println(EntityUtils.toString(entity));

                            EntityUtils.consume(entity);

                        }

                    }

                })
                .doOnComplete(() -> {
                    System.out.println("clean jobs complete");
                    upload.subscribe();
                }).subscribe();


    }


}