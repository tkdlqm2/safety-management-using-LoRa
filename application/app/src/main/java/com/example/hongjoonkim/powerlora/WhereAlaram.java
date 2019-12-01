package com.example.hongjoonkim.powerlora;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WhereAlaram {

    String boundary,latitute,longtitute;
    Util util;
    String page = util.REGISTER_URL;

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
                            //boundary = row.getString("boundary");
                            latitute = row.getString("latitute");
                            longtitute = row.getString("longtitute");
                            boundary = row.getString("boundary");
                        }


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
