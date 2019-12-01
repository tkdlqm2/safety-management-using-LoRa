package com.example.hongjoonkim.powerlora;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class setLocation3 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Geocoder geocoder;
    private Button button,button1,button3;
    String latitute1,longtitute1;
    String latitude2,longitude2;
    Util util;
    LatLng point;
    private EditText editText,boundary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location3);

        editText = (EditText) findViewById(R.id.editText);
        boundary = (EditText)findViewById(R.id.editText1);
        button=(Button)findViewById(R.id.button);
        button1=(Button)findViewById(R.id.button2);
        button3=(Button)findViewById(R.id.button3);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
                Intent intent;
                intent = new Intent(getApplicationContext(),MainActivity.class);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mMap.clear();
                    Double double_boundary = Double.parseDouble(boundary.getText().toString());
                    CircleOptions circle1KM = new CircleOptions().center(new LatLng(Double.parseDouble(latitude2), Double.parseDouble(longitude2))) //원점
                            .radius(double_boundary)      //반지름 단위 : m
                            .strokeWidth(0f)  //선너비 0f : 선없음
                            .fillColor(Color.parseColor("#20000000")); //배경색
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latitude2), Double.parseDouble(longitude2))).title("해당 위치"));
                    mMap.addCircle(circle1KM);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latitude2), Double.parseDouble(longitude2)), 15));
                }catch (NumberFormatException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"안전 반경을 설정하세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this);

        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                MarkerOptions mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("마커 좌표");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                latitude2 = latitude.toString();
                longitude2 = longitude.toString();
                googleMap.addMarker(mOptions);
            }
        });
        ////////////////////

        // 버튼 이벤트
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    String str = editText.getText().toString();
                    List<Address> addressList = null;
                    try {
                        // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                        addressList = geocoder.getFromLocationName(
                                str, // 주소
                                10); // 최대 검색 결과 개수
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 콤마를 기준으로 split
                    String[] splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2); // 주소

                    latitude2 = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    longitude2 = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

                    // 좌표(위도, 경도) 생성
                    point = new LatLng(Double.parseDouble(latitude2), Double.parseDouble(longitude2));
                    // 마커 생성
                    MarkerOptions mOptions2 = new MarkerOptions();
                    mOptions2.title("search result");
                    mOptions2.snippet(address);
                    mOptions2.position(point);

                    // 마커 추가
                    mMap.addMarker(mOptions2);
                    // 해당 좌표로 화면 줌
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
                }
                catch(NullPointerException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"주소를 입력하세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
        LatLng sydney = new LatLng(37.5652894, 126.8494657);

        mMap.addMarker(new MarkerOptions().position(sydney).title("seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void sendDataToServer()
    {
        StringRequest sr = new StringRequest(Request.Method.POST, Util.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(setLocation3.this, "위치 값 설정 완료", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(setLocation3.this, "값을 채워주세요." , Toast.LENGTH_SHORT).show();
            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError
            {
                HashMap<String,String> h = new HashMap<>();
                h.put("latitute",latitude2);
                h.put("longtitute",longitude2);
                h.put("boundary",boundary.getText().toString());
                return h;
            }
        };
        Volley.newRequestQueue(setLocation3.this).add(sr);
    }
}