package com.example.game;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    private long time;
    public static int player_hp = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(new gameai(this,player_hp));
        if(player_hp <=0){
            exit();
        }
    }



    public boolean onKeyDown(int keyCode,KeyEvent event) { //kack key
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            long t=System.currentTimeMillis();//get system time
            if(t-time<=500){
                exit(); //exit game if press twice with 500
            }else{
                time=t;
                Toast.makeText(getApplicationContext(),"Press to exit game",Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return false;

    }
    public void exit(){
        StartActivity.this.finish();
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {Thread.sleep(500);} catch (InterruptedException e) {e.printStackTrace();}
                System.exit(0);
            }
        }).start();
    }
}