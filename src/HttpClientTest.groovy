import org.apache.http.HttpEntity
import org.apache.http.NoHttpResponseException
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(0).build();

HttpClients.custom().setDefaultRequestConfig(requestConfig).disableAutomaticRetries().build().withCloseable { httpclient ->
    10.times {
        // 创建http请求(get方式)
        System.out.println("--------------------------------------------------------------------------------");
        HttpGet httpget = new HttpGet("http://172.17.161.216:8080/random");
        System.out.println("executing request" + httpget.getRequestLine());
        try{
            httpclient.execute(httpget).withCloseable { response ->
                HttpEntity entity = response.getEntity();
                System.out.println(response.getStatusLine().getStatusCode());
                if (entity != null) {
//                System.out.println("Response content length: " + entity.getContentLength());
//                System.out.println(EntityUtils.toString(entity));
                    EntityUtils.consume(entity);
                }

            }

        }catch(NoHttpResponseException exception){
            exception.printStackTrace()
        }

    }

};




