package org.techtown.flashlight;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RequestHttpURLConnection {
    public String request(String _url, json json1) {


        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");

        Calendar time = Calendar.getInstance();

        String format_time1 = format.format(time.getTime());

        // HttpURLConnection 참조 변수.
        HttpURLConnection urlConn = null;
        try {
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            String json = "";


            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("flashId", json1.getflashid());
            jsonObject.accumulate("uuid", json1.getuuid());
          //  jsonObject.accumulate("uuid", "50ebdb374a1a4de4bb42f44a91dafe37");
         //   jsonObject.accumulate("time", format_time1);


            // convert JSONObject to JSON to String

            json = jsonObject.toString();


            // ** Alternative way to convert Person object to JSON string usin Jackson Lib

            // ObjectMapper mapper = new ObjectMapper();

            // json = mapper.writeValueAsString(person);


            // Set some headers to inform server about the type of the content

            urlConn.setRequestProperty("Accept-Charset", "UTF-8");;

            urlConn.setRequestProperty("Content-type", "application/json");


            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.

            urlConn.setDoOutput(true);

            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.

            urlConn.setDoInput(true);


            OutputStream os = urlConn.getOutputStream();

            os.write(json.getBytes("euc-kr"));

            os.flush();

            // receive response as inputStream

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null) {
                page += line;
            }
            return page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        return null;

    }
}