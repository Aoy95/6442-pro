package com.example.game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1=(Button)findViewById(R.id.btnTextView1);

        //给btn1绑定监听事件
        btn1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // 给bnt1添加点击响应事件
                Intent intent =new Intent(MainActivity.this,StartActivity.class);
                //启动
                startActivity(intent);
            }
        });

    }
}