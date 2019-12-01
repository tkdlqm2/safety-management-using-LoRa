package com.example.hongjoonkim.powerlora;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class infoActivity extends AppCompatActivity {

    TextView t1,t2,t3;
    TextView t4,t5;
    int count;
    Button btn,btn1,btn2;
    Dialog dialog;
    EditText wedo,kyungdo;
    Util util;


    private static final int PROGRESS = 0x01;
    private ProgressBar mProgress;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    t1 = findViewById(R.id.name);
                    t2 = findViewById(R.id.age);
                    t3 = findViewById(R.id.PhoneNumber);
                    btn = (Button)findViewById(R.id.change02);
                    btn1 = (Button)findViewById(R.id.call);

//                    btn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(getApplicationContext(),.class);
//                            startActivity(intent);
//                        }
//                    });

                    String page = util.REGISTER_URL2+"person.jsp";
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

                    for(int i=0; i<jArray.length(); i++)
                    {
                        JSONObject row = jArray.getJSONObject(i);
                        t1.setText(row.getString("name"));
                        t2.setText(row.getString("age"));
                        t3.setText(row.getString("phone"));
                        count += Integer.parseInt(row.getString("count"));
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
