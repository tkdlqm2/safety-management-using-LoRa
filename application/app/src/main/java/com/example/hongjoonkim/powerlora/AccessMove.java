package com.example.hongjoonkim.powerlora;

import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AccessMove {

    int realCount;
    String a;
    TextView t1,t2;
    int count;
    int divde = 0;
    int rest = 0;
    String po,po1;
    Util util;
    String page = util.REGISTER_URL2+"move.jsp";

    public HttpResponse getResponse(String url)
    {
        try {
            AsyncTask<String, Void, HttpResponse> asyncTask = new AsyncTask<String, Void, HttpResponse>() {
                @Override
                protected HttpResponse doInBackground(String... url) {
                    HttpGet request = new HttpGet(url[0]);
                    HttpResponse response = null;
                    try {
                        response = new DefaultHttpClient().execute(request);
                        String body = EntityUtils.toString(response.getEntity()); // String으로 변환하는 작업.
                        JSONObject jsonObj = new JSONObject(body);
                        JSONArray jArray = (JSONArray)jsonObj.get("sendData");
                        for(int i=0; i<jArray.length(); i++)
                        {
                            JSONObject row = jArray.getJSONObject(i);
                            count += Integer.parseInt(row.getString("count"));
                        }
                        divde = count/100;
                        po = Integer.toString(count);
                        rest = 10000 - count;
                        po1 = Integer.toString(rest);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    return response;
                }
            };

            HttpResponse response = asyncTask.execute(url).get();
            return response;
        } catch (Exception e) {
            // No error
        }
        return null;
    }

}

