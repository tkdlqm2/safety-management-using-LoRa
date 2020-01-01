package com.example.hongjoonkim.powerlora;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class Util {
    public static final String REGISTER_URL = "http://52.79.109.78:8080/mobile2/list.jsp";
    public static final String REGISTER_URL2 = "http://52.79.109.78:8080/mobile2/";
    public static final String REGISTER_URL3 = "52.79.109.78:8080";

    public static void showWarning(AppCompatActivity cmp, String msg)
    {
        AlertDialog.Builder a = new AlertDialog.Builder(cmp);
        a.setTitle("Warning-Message");
        a.setMessage(msg);
        a.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        a.show();
    }
}
