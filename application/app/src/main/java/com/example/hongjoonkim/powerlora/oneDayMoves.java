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

    public class oneDayMoves extends AppCompatActivity {

        Util util;
        ArrayList<Entry> entries = new ArrayList<>();

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                LineChart lineChart = (LineChart) findViewById(R.id.chart);

                LineDataSet dataset = new LineDataSet(entries, "걸음 수");
                dataset.setColor(Color.parseColor("#FFA1B4DC"));
                dataset.setColor(Color.RED);

                LineData data = new LineData(dataset);
            /*dataset.setDrawCubic(true); //선 둥글게 만들기
            dataset.setDrawFilled(true); //그래프 밑부분 색칠*/

                lineChart.setData(data);
                lineChart.animateY(2000);

            }
        };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_one_day_moves);

            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        String page = util.REGISTER_URL2+"sumMove.jsp";
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
                        int sumMoves = 0;
                        for(int i=0; i<jArray.length(); i++)
                        {
                            JSONObject row = jArray.getJSONObject(i);
                            sumMoves = Integer.parseInt(row.getString("sumMoves"));
                            entries.add(new Entry(i+1, sumMoves));

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

