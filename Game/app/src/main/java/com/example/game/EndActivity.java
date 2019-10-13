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

class endActivity extends Activity {

    private ImageButton imagebutton;
    private TextView textview;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.end_activity);
      //  imagebutton=(ImageButton)findViewById(R.id.button1);
      //  textview=(TextView)findViewById(R.id.textView2);
        textview.setTextColor(Color.RED);
        textview.setTextSize(20.0f);
        //textview.setText("分数：\n"+Enemy.grade);
        imagebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                finish();
            }
        });
    }
    public boolean onKeyDown(int KeyCode,KeyEvent Event){

        if(KeyCode==KeyEvent.KEYCODE_BACK)
        {

            finish();
        }
        return true;
    }
}

