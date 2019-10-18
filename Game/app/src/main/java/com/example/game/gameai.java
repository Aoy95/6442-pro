package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import java.util.Vector;
import android.content.Intent;

class my{
    public static int mark =0;//kill number
    public static int w,h;//weight and height
    public static float screen_rate;//poportion
    public static Vector<airplane> player_list=new Vector<airplane>();//plane list
    public static Vector<airplane> enemy_list=new Vector<airplane>();//enermy list
    public static Bitmap player,enemy,background,bullet;//picture,background picture,bullet
    public static player first_player;//my plane
    public static background b;//background
    public static int player_hp;
}

public class gameai extends View{
    private Paint p = new Paint();
    private float x,y;//propertion when press screen
    private float player_x,player_y;//plan's propertion

    public gameai(Context context,int hp) {
        super(context);
        my.player_hp = hp;
        //user plane
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent e) {
                if(e.getAction() == MotionEvent.ACTION_DOWN){
                    x = e.getX();
                    y = e.getY();
                    player_x = my.first_player.r.left;
                    player_y = my.first_player.r.top;
                }
                float xx = player_x + e.getX() - x;
                float yy = player_y + e.getY() - y;
                //plan within screen
                xx = xx<my.w-my.first_player.w/2 ? xx:my.w-my.first_player.w/2;
                xx = xx>-my.first_player.w/2 ? xx:-my.first_player.w/2;
                yy = yy<my.h-my.first_player.h/2 ? yy:my.h-my.first_player.h/2;
                yy = yy>-my.first_player.h/2 ? yy:-my.first_player.h/2;
                my.first_player.setX(xx);
                my.first_player.setY(yy);
                return true;
            }
        });

        setBackgroundColor(Color.BLACK);//background color

        my.player= BitmapFactory.decodeResource(getResources(),R.mipmap.player);//loading picture
        my.enemy=BitmapFactory.decodeResource(getResources(),R.mipmap.enemy2);
        my.bullet=BitmapFactory.decodeResource(getResources(),R.mipmap.zd);
        my.background=BitmapFactory.decodeResource(getResources(), R.mipmap.bg);

        new Thread(new re()).start();//rebuild picture
        new Thread(new loaddr()).start();//loading enermy's router
    }
    @Override
    protected void onDraw(Canvas g) {//draw everything on screen
        super.onDraw(g);
        g.drawBitmap(my.b.img,null,my.b.r,p);//draw background

        for(int i=0;i<my.player_list.size();i++){//add plane into list
            airplane h = my.player_list.get(i);
            g.drawBitmap(h.img,null,h.r,p);
        }
        g.drawText("Player Hp : " + my.player_hp,0,my.h-100,p);
        g.drawText("击杀："+my.mark,0,my.h-50,p);

    }
    @Override
    protected void onSizeChanged(int w, int h, int old_w, int old_h) {//get screen' height and weight
        super.onSizeChanged(w, h, old_w, old_h);
        my.w=w;//weight
        my.h=h;//height
        my.screen_rate= (float) (Math.sqrt(my.w * my.h)/ Math.sqrt(1920 * 1080));
        p.setTextSize(50*my.screen_rate);//kill number
        p.setColor(Color.WHITE);
        my.b = new background();//initilize background
        my.first_player = new player();//initiliaze plan
    }
    private class re implements Runnable {
        @Override
        public void run() {
            //fresh screen every 10.
            while(true){
                try { Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
                postInvalidate();//refresh picture
            }
        }
    }
    private class loaddr implements Runnable{
        @Override
        public void run() {
            while(true){
                //get enermy every 300ms
                try {Thread.sleep(300);} catch (InterruptedException e) {e.printStackTrace();}
                try {
                    new enemy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
class airplane{//store all airplane
    public RectF r = new RectF();//get position
    public int hp;//life
    public float w,h;//weight and height
    public Bitmap img;//picture

    //draw picture
    public void setX(float x){
        r.left=x;
        r.right=x+w;
    }
    public void setY(float y){
        r.top=y;
        r.bottom=y+h;
    }

    public boolean collision(airplane obj,float px) {//check hit
        px *= my.screen_rate;//
        if (r.left+px - obj.r.left <= obj.w && obj.r.left - this.r.left+px <= this.w-px-px)
            if (r.top+px - obj.r.top <= obj.h && obj.r.top - r.top+px <= h-px-px) {
                return true;
            }
        return false;

    }
}


class Bossplane extends airplane implements Runnable{
    private long sd0=(long) (Math.random()*10)+10;
    private Bitmap bitmap;
    private int x,y;
    private int frameW,frameH;
    private int speed = 4;
    private int crazySpeed =50;
    private int count;//count
    private int time = 50;//intern
    private boolean isCrazy;


    public Bossplane(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.frameW = bitmap.getWidth()/10;
        this.frameH = bitmap.getHeight();
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.save();
        canvas.clipRect(x,y,x+frameW,y+frameH);//draw boss plane
        canvas.drawBitmap(bitmap,x,y,paint);
        canvas.restore();//release
        logic();
    }

    public void logic() {
        count++;
        if (isCrazy) {
            //crazy
            y = y + crazySpeed;
            crazySpeed--;
            if (y == 0) {
                isCrazy = false;
                crazySpeed = 50;
            }

        } else {
            if (y > my.h - frameH) {
                crazySpeed = -crazySpeed;
            }

            if (count % time == 0) {
                isCrazy = true;
            }
            x = x + speed;
            if (x > my.w - frameW) {
                speed = -speed;
            }
            if (x < 0) {
                speed = -speed;
            }
        }
    }



    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getFrameW() {
        return frameW;
    }

    public int getFrameH() {
        return frameH;
    }
    public void run() {
        while(hp>0){
            try {Thread.sleep(sd0);} catch (InterruptedException e) {e.printStackTrace();}
            setY(r.top+2*my.screen_rate);
            if(r.top>=my.h)break;//if plane fly out screen,game ends
        }
        //remove from list
        my.player_list.remove(this);
        my.enemy_list.remove(this);
    }

}


class background extends airplane implements  Runnable{//background
    public background(){
        w=my.w;
        h=my.h*2;
        img=my.background;
        setX(0);
        setY(-my.h);
        new Thread(this).start();
    }
    @Override
    public void run() {
        //keep going down
        while(true){
            try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
            if(r.top+2<=0){
                setY(r.top+2);
            }else{
                setY(-my.h);
            }
        }
    }
}

class enemy extends airplane implements Runnable{//enermy plane
    private long sd0=(long) (Math.random()*10)+10;//enermy speed

    public enemy(){
        w=h=200*my.screen_rate;
        //enermy position
        setX((float)( Math.random()*(my.w-w)));
        setY(-h);//outside screen
        img=my.enemy;
        hp=12;//life
        my.player_list.add(this);
        my.enemy_list.add(this);
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(hp>0){//stop if life is less than 0
            try {Thread.sleep(sd0);} catch (InterruptedException e) {e.printStackTrace();}
            setY(r.top+2*my.screen_rate);
            if(r.top>=my.h)break;
        }
        my.player_list.remove(this);
        my.enemy_list.remove(this);
    }
}

class player extends airplane implements Runnable{//my plane

    public player(){
        w=h=200*my.screen_rate;
        setX(my.w/2-w/2);
        setY(my.h*0.7f-h/2);
        img=my.player;
        my.player_hp = 20;
        my.player_list.add(this);
        new Thread(this).start();//bullet route
    }

    @Override
    public void run() {
        while(my.player_hp > 0){
            try {Thread.sleep(90);} catch (InterruptedException e) {e.printStackTrace();}
            new bullet(this);
        }

    }
}
class bullet extends airplane implements Runnable{//bullet
    private int dps;
    private float sd0;

    public bullet(airplane airplane){
        w=h=90*my.screen_rate;
        img=my.bullet;//picture
        sd0=6*my.screen_rate;//speed
        dps=6;//damage
        setX(airplane.r.left+airplane.w/2-w/2);
        setY(airplane.r.top-h/2);
        my.player_list.add(this);
        new Thread(this).start();}

    @Override
    public void run() {
        boolean flag=false;
        while(true){
            try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
            setY(r.top-sd0);

            try {//check whether hit
                for(int i=0;i<my.enemy_list.size();i++){
                    airplane h=my.enemy_list.get(i);
                    if(collision(h,30)){
                        h.hp-=dps;
                        flag=true;
                        my.mark++;
                        break;
                    }
                }
                airplane my_h = my.first_player;
                if(collision(my_h,30)){
                    my.player_hp -= 1;
                    flag=true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            if(flag || r.top+h<=0)break;
        }
        my.player_list.remove(this);
    }
}