package com.example.game;
//import com.example.entity.Enemy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

class endActivity extends Activity {
    private long time;

    private ImageButton imagebutton;
    private TextView textview;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textview.setTextColor(Color.RED);
        textview.setTextSize(20.0f);
        imagebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
            }
        });
    }
    public boolean onKeyDown(int KeyCode,KeyEvent Event){

        if (KeyCode == KeyEvent.KEYCODE_BACK && Event.getRepeatCount() == 0){
            long t=System.currentTimeMillis();
            if(t-time<=100){
                exit();
            }else{
                time=t;
                Toast.makeText(getApplicationContext(),"Double press to exit the game",Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;

    }
    public void exit(){
        endActivity.this.finish();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
                System.exit(0);
            }
        }).start();
    }
}

