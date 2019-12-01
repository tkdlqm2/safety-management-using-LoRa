package com.example.hongjoonkim.powerlora;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.example.hongjoonkim.powerlora.DTO.GpsDTO;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String wedo;
    String kyungdo;
    double wedo2;
    double kyungdo2;
    Util util;
    double set_lati,set_longti;
    WhereAlaram w1; // 설정 범위 값
    int boundary;
    double distance = 0;
    Button btnn,power222;
    private FusedLocationProviderClient fusedLocationClient; // 위치 공급자 클라이언트
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            btnn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRestart();
                }
            });

            try {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this);
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        try{
            w1 = new WhereAlaram();
            w1.getResponse(w1.page);
            set_lati = Double.parseDouble(w1.latitute);
            set_longti = Double.parseDouble(w1.longtitute);
            boundary = Integer.parseInt(w1.boundary);
        }catch(Exception e){

        }

        btnn = findViewById(R.id.btnn);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    URL url = new URL(util.REGISTER_URL2+"gps.jsp");
                    String page = util.REGISTER_URL2+"gps.jsp";
                    HttpClient http = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(page);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("1","1");

                    // 서버로 요청을 함 -> 서버에서는 제이슨을 생성하기 시작함. 제이슨을 생성하고 나서 제이슨을 보내줌.
                    // http.execute(httpPost) -> 서버에 요청
                    // 서버의 응답(json)이 response로 리턴됨.
                    HttpResponse response = http.execute(httpPost);  // response에 서버에서 생성된 제이슨 객체가 담김.
                    // httpReponse를 스트링으로 변환.
                    String body = EntityUtils.toString(response.getEntity()); // String으로 변환하는 작업.

                    // 스트링을 json 객체로 변환.
                    JSONObject jsonObj = new JSONObject(body);
                    JSONArray jArray = (JSONArray)jsonObj.get("sendData");

                    for(int i=0; i<jArray.length(); i++)
                    {
                        JSONObject row = jArray.getJSONObject(i);
                        GpsDTO dto = new GpsDTO();
                        dto.setLatitue((row.getString("latitute")));
                        dto.setLongtitude(row.getString("longtitute"));
                        if(i == jArray.length()-1)
                        {
                            wedo = row.getString("latitute");
                            kyungdo = row.getString("longtitute");
                            wedo2 = Double.parseDouble(wedo);
                            kyungdo2 = Double.parseDouble(kyungdo);
                        }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng position = new LatLng(wedo2, kyungdo2);
        LatLng position2 = new LatLng(set_lati, set_longti);

        CircleOptions circle1KM = new CircleOptions().center(position2) //원점
                .radius(boundary)      //반지름 단위 : m
                .strokeWidth(0f)  //선너비 0f : 선없음
                .fillColor(Color.parseColor("#20000000")); //배경색
        mMap.addMarker(new MarkerOptions().position(position).title("해당위치"));
        mMap.addCircle(circle1KM);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position,18));
    }

    @Override
    protected void onRestart() {

        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(MapsActivity.this, MapsActivity.class);  //your class
        startActivity(i);
        finish();

    }
}
