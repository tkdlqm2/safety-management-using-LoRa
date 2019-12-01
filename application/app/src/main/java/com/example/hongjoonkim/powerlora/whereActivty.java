package com.example.hongjoonkim.powerlora;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hongjoonkim.powerlora.DTO.GpsDTO;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class whereActivty extends AppCompatActivity {

    ArrayList<GpsDTO> items;
    String Lati;
    String longti;
    ListView mListView;
    Util util;
    TextView t1,t2,t3;
    String wedo,kyungdo;
    Double wedo2,kyungdo2;
    List locationName = null;
    private GoogleApiClient googleApiClient;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            mListView = (ListView)findViewById(R.id.list);
            final GPSDTOAdapter adapter = new GPSDTOAdapter(whereActivty.this,R.layout.gps_row,items);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    GpsDTO vo = (GpsDTO)parent.getAdapter().getItem(position);
                    Intent intent = new Intent(view.getContext(),MapsActivity.class);
                    intent.putExtra("wedo",vo.getLongtitude().toString());
                    intent.putExtra("kyungdo",vo.getLatitue().toString());

                    startActivity(intent);
                    finish();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_where_activty);

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    t1 = findViewById(R.id.parentWedo);
                    t2 = findViewById(R.id.parentKyungdo);
                    items = new ArrayList<GpsDTO>();
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
                        items.add(0,dto);
                        if(i == jArray.length()-1)
                        {
                            t1.setText(row.getString("latitute"));
                            t2.setText(row.getString("longtitute"));
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


    class GPSDTOAdapter extends ArrayAdapter<GpsDTO> {
        public GPSDTOAdapter(Context context, int resource, List<GpsDTO> objects)
        {
            super(context,resource,objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;

            double d1,d2;
            if(v == null)
            {
                LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.gps_row,null);
            }
            GpsDTO dto = items.get(position);
            if(dto != null)
            {
                List<Address> addresses = null;
                TextView gps_wedo = (TextView)v.findViewById(R.id.wedo);
                TextView gps_kyungdo = (TextView)v.findViewById(R.id.kyungdo);
                TextView locationName2 = (TextView)v.findViewById(R.id.locationName);
                gps_wedo.setText(dto.getLatitue());
                gps_kyungdo.setText(dto.getLongtitude());
                wedo2 = Double.parseDouble(dto.getLatitue());
                kyungdo2 = Double.parseDouble(dto.getLongtitude());


                d1 = Double.parseDouble(dto.getLongtitude());
                d2 = Double.parseDouble(dto.getLongtitude());
//                getAddressFromLocation(d1,d2);
                Log.d("Tag","wer123123");


            }

            return v;
        }

    }
//    private void getAddressFromLocation(double latitude, double longitude) {
//
//        Geocoder geocoder = new Geocoder(this, Locale.KOREA);
//        t3 = findViewById(R.id.parentLocation);
//
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            Log.d("Tag",addresses.toString());
//            if (addresses.size() > 0) {
//                Address fetchedAddress = addresses.get(1);
//                StringBuilder strAddress = new StringBuilder();
//                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
//                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
//                }
//
//                t3.setText(strAddress.toString());
//
//            } else {
//                t3.setText("Searching Current Address");
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


}

