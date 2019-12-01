package com.example.hongjoonkim.powerlora;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class moveActivity extends AppCompatActivity {

    TextView t1,t2,t3;
    int count;
    double count1;
    String finalMeter;
    int divde = 0;
    int rest = 0;
    String po,po1;
    ImageView image;
    Button power2;
    double caloriesNumber;
    String caloriesText;
    TextView calories;
    private static final int PROGRESS = 0x01;
    private ProgressBar mProgress;
    Util util;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            finalMeter = Double.toString(Math.round(count1*100)/100.0);
            t1.setText(po);
            t2.setText(po1);
            t3.setText(finalMeter);
            calories.setText(caloriesText);
            mProgress = (ProgressBar)findViewById(R.id.progress);
            mProgress.setProgress(divde);
            power2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), oneDayMoves.class);
                    startActivity(intent);
                    finish();
                }
            });

            if(rest == 0)
            {
                show();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    divde = 0;
                    t1 = findViewById(R.id.result01);
                    t2 = findViewById(R.id.result02);
                    t3 = findViewById(R.id.result03);
                    power2 = findViewById(R.id.power2);
                    calories = findViewById(R.id.calories);

                    String page = util.REGISTER_URL2+"move.jsp";
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
                        count += Integer.parseInt(row.getString("move"));
                    }
                    divde = count/100;
                    count1 = count * 0.15;
                    caloriesNumber = count * 0.033;
                    caloriesNumber = caloriesNumber/5;
                    caloriesText = Double.toString(Math.round(caloriesNumber*100)/100.0);
                    po = Integer.toString(count);
                    rest = 10000 - count;
                    po1 = Integer.toString(rest);

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
    public void show()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"default");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("알림 제목");
        builder.setContentText("알림 세부 텍스트");

        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        builder.setLargeIcon(largeIcon);
        builder.setColor(Color.RED);
        Uri ringtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this,RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(ringtoneUri);

        long[] vibrate = {0,100,200,300};
        builder.setVibrate(vibrate);
        builder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel("default","기본 채널",NotificationManager.IMPORTANCE_DEFAULT));
        }
        manager.notify(1,builder.build());
    }
    public void removeNotification(View view)
    {
        hide();
    }
    public void hide()
    {
        NotificationManagerCompat.from(this).cancel(1);
    }
}
