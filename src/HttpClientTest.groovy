import org.apache.http.HttpEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

HttpClients.createDefault().withCloseable { httpclient ->
    10.times {
        // 创建http请求(get方式)
        HttpGet httpget = new HttpGet("http://www.apache.org");
        System.out.println("executing request" + httpget.getRequestLine());
        httpclient.execute(httpget).withCloseable { response ->
            HttpEntity entity = response.getEntity();
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
//                System.out.println("Response content length: " + entity.getContentLength());
//                System.out.println(EntityUtils.toString(entity));
                EntityUtils.consume(entity);
            }

        }

    }

};




