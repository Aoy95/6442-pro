package com.example.myapplication;

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


class basicSetting {
    //the number of killed plane
    public static int killedNumber = 0;
    //height and wieght of screen
    public static int height, weight;
    //proportion which can be used in different screen
    public static float proportion;
    // list of our plane
    public static Vector<Father> list = new Vector<Father>();
    //list of enemy's plane
    public static Vector<Father> enemyList = new Vector<Father>();
    //picture
    public static Bitmap userPlane, enemyPlane, background, bullet;
    public static userPlane user;
    public static background backGround;
}

public class BasicSetting extends View {
    //pen
    private Paint paint = new Paint();
    //coordinates when we press the screen
    private float roatX, roatY;
    // user's coordinates when user press the screen
    private float userX, userY;

    public BasicSetting(Context context) {
        super(context);
        // add fuction to control user's plane
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    roatX = event.getX();
                roatY = event.getY();
                userX = basicSetting.user.rectF.left;
                userY = basicSetting.user.rectF.top;

                float x = userX + event.getX() - roatX;
                float y = userY + event.getY() - roatY;
                // make sure plane is in screen
                x = x < basicSetting.weight - basicSetting.user.weight / 2 ? x : basicSetting.weight - basicSetting.
                        user.weight / 2;
                x = x > -basicSetting.user.weight / 2 ? x : -basicSetting.user.weight / 2;
                y = y < basicSetting.height - basicSetting.user.height / 2 ? y : basicSetting.height - basicSetting.
                        user.height / 2;
                y = y > -basicSetting.user.height / 2 ? y : -basicSetting.user.height / 2;
                basicSetting.user.setX(x);
                basicSetting.user.setY(y);
                return true;
            }
        });
        //set background color
        setBackgroundColor(Color.BLUE);
        // set picture
        basicSetting.userPlane = BitmapFactory.decodeResource(getResources(), R.mipmap.hj);
        basicSetting.enemyPlane = BitmapFactory.decodeResource(getResources(), R.mipmap.dr);
        basicSetting.background = BitmapFactory.decodeResource(getResources(), R.mipmap.bj);
        basicSetting.bullet = BitmapFactory.decodeResource(getResources(), R.mipmap.zd);
        // build a fuction to restart the picture
        new Thread(new restart()).start();
        ;
        // bbuild a new picture for enemy plane
        new Thread(new enemyLoading()).start();
    }

    @Override
    //function to draw anything
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw background;
        canvas.drawBitmap(basicSetting.backGround.image, null, basicSetting.backGround.rectF, paint);

        for (int i = 0; i < basicSetting.list.size(); i++) {
            Father father = basicSetting.list.get(i);
            canvas.drawBitmap(father.image, null, father.rectF, paint);
        }
        canvas.drawText("Kill：" + basicSetting.killedNumber, 0, basicSetting.height - 50, paint);

    }

    @Override
    protected void onSizeChanged(int weight, int height, int oldWeight, int oldHeight) {
        //get screen's height and weight
        super.onSizeChanged(weight, height, oldHeight, oldWeight);
        basicSetting.weight = weight;
        basicSetting.height = height;
        //get phone's property
        basicSetting.proportion = (float) (Math.sqrt(basicSetting.weight * basicSetting.height) /
                Math.sqrt(1920 * 1080));
        //set character
        paint.setTextSize(50 * basicSetting.proportion);
        //set color
        paint.setColor(Color.WHITE);
        // inilize game
        basicSetting.backGround = new background();
        //inilize plane
        basicSetting.user = new userPlane();
    }

    private class restart implements Runnable {
        @Override
        public void run() {
            //fresh screen every 10s
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                postInvalidate();
            }
        }
    }

    private class enemyLoading implements Runnable {
        @Override
        public void run() {
            while (true) {
                //fresh enemy 300ms
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    new enemyPlane();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


// store all father
class Father{
    //find the roate
    public RectF rectF=new RectF();
    //life
    public int life;
    //hight and weight
    public float height,weight;
    //picture
    public Bitmap image;


    // make coordinates
    public void setX(float x){
        rectF.left=x;
        rectF.right=x+weight;
    }

    public void setY(float y){
        rectF.top=y;
        rectF.bottom=y+height;
    }
    //chechk whether it's hit
    public boolean Hit(Father object,float px){
        px*=basicSetting.proportion;

        if (rectF.left+px-object.rectF.left<=object.weight&&object.rectF.left-this.rectF
        .left+px<this.weight-px-px)
            if (rectF.top+px-object.rectF.top<=object.height&&object.rectF.top-rectF.top
            +px<=height-px-px)
                return true;
            return false;
    }
}

// background picture
class background extends Father implements Runnable{
    public background(){
        height=basicSetting.height*2;
        weight=basicSetting.weight;
        image=basicSetting.background;
        setX(0);
        setY(-basicSetting.height);
        new Thread(this).start();
    }
    @Override
    public void run(){
        // let picture keep doing down
        while(true){
            try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
            if(rectF.top+2<=0){
                setY(rectF.top+2);
            }else{
                setY(-basicSetting.height);}
        }
    }
}

// enemy plane
class enemyPlane extends Father implements Runnable{
    //speed
    private long speed=(long)(Math.random()*10)+10;

    public enemyPlane(){
        weight=height=200*basicSetting.proportion;
        setX((float)( Math.random()*(basicSetting.weight-weight)));
        //outside the sccreen
        setY(-height);
        image=basicSetting.enemyPlane;
        //life is 12
        life=12;
        basicSetting.list.add(this);
        basicSetting.enemyList.add(this);
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(life>0){
            //if life less than 0, stop
            try {Thread.sleep(speed);} catch (InterruptedException e) {e.printStackTrace();}
            setY(rectF.top+2*basicSetting.proportion);
            // if plane fly outside screen, stop
            if(rectF.top>=basicSetting.height)break;
        }
        //remove from list
        basicSetting.list.remove(this);
        basicSetting.enemyList.remove(this);
    }
}

// user plane
class userPlane extends Father implements Runnable{

    public userPlane (){
        weight=height=200*basicSetting.proportion;
        setX(basicSetting.weight/2-weight/2);
        setY(basicSetting.height*0.7f-height/2);
        image=basicSetting.userPlane;
        basicSetting.list.add(this);
        //bullet route
        new Thread(this).start();
    }

    @Override
    public void run() {
        while(true){
            //every 90ms for one bullet
            try {Thread.sleep(90);} catch (InterruptedException e) {e.printStackTrace();}
            new bullet(this);
        }
    }
}

//bullet
class bullet extends Father implements Runnable{
    private int dps;
    private float speed;

    public bullet(Father father){
        weight=height=90*basicSetting.proportion;
        image=basicSetting.bullet;
        //speed
        speed=6*basicSetting.proportion;
        //damage
        dps=6;
        setX(father.rectF.left+father.weight/2-weight/2);
        setY(father.rectF.top-height/2);
        basicSetting.list.add(this);
        new Thread(this).start();
    }

    @Override
    public void run() {
        boolean flag=false;
        while(true){
            try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
            setY(rectF.top-speed);
            try {
                //check whether hitting happen
                for(int i=0;i<basicSetting.enemyList.size();i++){
                    Father faTher=basicSetting.enemyList.get(i);
                    if(Hit(faTher,30)){
                        faTher.life-=dps;
                        flag=true;
                        basicSetting.killedNumber++;
                        break;
                    }
                }
            } catch (Exception expection) {
                expection.printStackTrace();
                break;
            }
            if(flag || rectF.top+height<=0)break;
        }
        basicSetting.list.remove(this);
    }
}
