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

        if(KeyCode==KeyEvent.KEYCODE_BACK)
        {

            finish();
        }
        return true;
    }
}

