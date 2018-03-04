package home.safe.com.myguarder;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kch on 2018-03-04.
 */

public class NetworkTask extends AsyncTask<Void, Void, String>{

    HttpResultListener listener;
    Context context;
    String strUrl;
    String params;
    URL url;
    String strCookie;
    String result;

    public NetworkTask(Context context,HttpResultListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d("onPreExecute","()");
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            url = new URL(strUrl+ params);  //url화 함
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();  //url을 연결한 객체 생성
            Log.d("doInBackground","HttpURLConnection");
            conn.setRequestMethod("GET");   // get방식 통신
            conn.setDoOutput(true);         // 쓰기모드 지정
            conn.setDoInput(true);          // 읽기모드 지정
            conn.setUseCaches(false);       // 캐싱데이터를 받을지 안받을지
            conn.setDefaultUseCaches(false);// 캐싱데이터 디폴트 값 설정

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                Log.d("HttpURLConnection","200");
            }
            else
            {
                Log.d("HttpURLConnection","400");
            }

            strCookie = conn.getHeaderField("Set-Cookie");  //쿠키데이터 보관

            InputStream is = conn.getInputStream(); // input 스트림 개방
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); //문자열 세팅

            StringBuilder builder = new StringBuilder();    //문자열 담을 객체 생성
            String line;

            while((line = reader.readLine())!=null) {
                builder.append(line + "/n");
            }

            result = builder.toString();

            reader.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("onPreExecute","()"+ s);
        System.out.println(result);
        listener.onPost(result);
    }
}
