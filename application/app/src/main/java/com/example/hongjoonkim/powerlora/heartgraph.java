package com.example.hongjoonkim.powerlora;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class heartgraph extends AppCompatActivity {

    Util util;
    ArrayList<Entry> entries = new ArrayList<>();

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            LineChart lineChart = (LineChart) findViewById(R.id.chart);

            LineDataSet dataset = new LineDataSet(entries, "state");
            LineData data = new LineData(dataset);
//            dataset.setDrawFilled(true); //그래프 밑부분 색칠
            dataset.setColor(Color.parseColor("#FFA1B4DC"));
//            dataset.setCircleColorHole(Color.RED);
//            dataset.setCircleColor(Color.RED);
            dataset.setColor(Color.RED);
            lineChart.setData(data);
            lineChart.animateY(2000);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heartgraph);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    String page = util.REGISTER_URL2+"sumHeart.jsp";
                    HttpClient http = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(page);

                    // 서버로 요청을 함 -> 서버에서는 제이슨을 생성하기 시작함. 제이슨을 생성하고 나서 제이슨을 보내줌.
                    // http.execute(httpPost) -> 서버에 요청
                    // 서버의 응답(json)이 response로 리턴됨.
                    HttpResponse response = http.execute(httpPost);  // response에 서버에서 생성된 제이슨 객체가 담김.
                    // httpReponse를 스트링으로 변환.
                    String body = EntityUtils.toString(response.getEntity()); // String으로 변환하는 작업.

                    // 스트링을 json 객체로 변환.
                    JSONObject jsonObj = new JSONObject(body);
                    JSONArray jArray = (JSONArray)jsonObj.get("sendData");
                    int beat = 0;
                    for(int i=0; i<jArray.length(); i++)
                    {
                        JSONObject row = jArray.getJSONObject(i);
                        beat = Integer.parseInt(row.getString("beat"));
                        entries.add(new Entry(i, beat));

//                        live_beat = (row.getString("beat"));
                    }

                    handler.sendEmptyMessage(0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
        th.start();

    }

}

