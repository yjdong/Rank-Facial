package com.example.zephyryin.rankfacial;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.Map;
import java.util.Random;

public class   MainActivity extends Activity {
    private Button exitButton = null;
    private ImageView imageView1 = null;
    private ImageView imageView2 = null;
    private Database db = null;
    private String path = null;
    private Bitmap bitmap1 = null;       // display on screen
    private Bitmap bitmap2 = null;
    private ImageBase imageBase = null;
    private Map<Integer,Integer> index = null;        // store two random index
    private int cnt;   // to record how many clicks has been made

    public  final static String DB_KEY = "com.tutor.objecttran.ser";
    public  final static String IMG_KEY = "com.RankFacial.img";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cnt =0;

        //drawableImages = getImages();
        imageBase = new ImageBase();
        imageBase.getImages();

        exitButton = (Button)findViewById(R.id.button_exit);
        imageView1 = (ImageView)findViewById(R.id.imageView_1);
        imageView2 = (ImageView)findViewById(R.id.imageView_2);
        index = new LinkedHashMap<Integer, Integer>();

        Random r=new Random();
        int indexOne,indexTwo;

        path = getApplicationContext().getFilesDir().getAbsolutePath()+"/scores.txt";
        Log.i("path",path);
        db = new Database();
        db.readDatabase(path);

        int range = db.scores.size();
        indexOne = r.nextInt(range)+1;
        do{
            indexTwo = r.nextInt(range)+1;
        }while(indexTwo == indexOne);

        index.put(1,indexOne);
        index.put(2,indexTwo);
        Log.i("first",String.valueOf(indexOne)+" "+String.valueOf(indexTwo));

        bitmap1 = BitmapFactory.decodeResource(getResources(), imageBase.getImageValue(indexOne));
        bitmap2 = BitmapFactory.decodeResource(getResources(), imageBase.getImageValue(indexTwo));
        imageView1.setImageBitmap(bitmap1);
        imageView2.setImageBitmap(bitmap2);




        Log.i("size",String.valueOf(db.scores.size()));

        exitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cnt>0)
                    db.writeDatabase(path);
                Intent intent= new Intent(MainActivity.this, ViewRank.class);
                ArrayList valuelist = new ArrayList();
                for(int i=1;i<=imageBase.getSize();i++){
                    //Log.i("getvalue",String.valueOf(i));
                    valuelist.add(String.valueOf(imageBase.getImageValue(i)));
                }

                intent.putStringArrayListExtra("imageValue",valuelist);

                MainActivity.this.startActivityForResult(intent, 3);//3是标示请求码，随便写

                //db.writeDatabase(path);
                //MainActivity.this.finish();
                //Toast.makeText(MainActivity.this, " write to database ", Toast.LENGTH_SHORT).show();
            }
        });

        imageView1.setOnClickListener(updateListener);
        imageView2.setOnClickListener(updateListener);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK ){
            db.writeDatabase(path);
            this.finish();
        }
        return false;
    }

    public OnClickListener updateListener = new OnClickListener(){

        @Override
        public void onClick(View view) {
            int indexOne,indexTwo;
            float exOne,exTwo,scoreOne,scoreTwo;
            Random r=new Random();

            int[] result = new int[2];
            int K = 10;
            indexOne = index.get(1);
            indexTwo = index.get(2);

            Log.i("second",String.valueOf(indexOne)+" "+String.valueOf(indexTwo));
            scoreOne = db.scores.get(indexOne);
            scoreTwo = db.scores.get(indexTwo);

            switch (view.getId()) {
                case R.id.imageView_1:
                    result[0] = 1;
                    result[1] = 0;
                    cnt++;
                    //Toast.makeText(MainActivity.this, " u like 1 ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.imageView_2:
                    result[0] = 0;
                    result[1] = 1;
                    cnt++;
                    //Toast.makeText(MainActivity.this, " u like 2 ", Toast.LENGTH_SHORT).show();
                    break;
            }

            if(cnt == 50){
                db.writeDatabase(path);  // if click 50 times, write to database
                cnt = 0;
            }

            if(scoreOne == scoreTwo){       // compute expect possibility
                exOne = 0.5f;
                exTwo = 0.5f;
            }else{
                exOne = (float)(1/(1+Math.pow(10.0,(scoreOne - scoreTwo)/400.0)));
                exTwo = (float)(1/(1+Math.pow(10.0,(scoreTwo - scoreOne)/400.0)));
            }

            scoreOne += K*(result[0]-exOne);            // compute new score
            scoreTwo += K*(result[1]-exTwo);

            db.reviseScore(indexOne,scoreOne);
            db.reviseScore(indexTwo,scoreTwo);
            db.writeDatabase(path);

            int range = db.scores.size();
            indexOne = r.nextInt(range)+1;    // 0->range-1
            do{
                indexTwo = r.nextInt(range)+1;    // 0->range-1
            }while(indexTwo == indexOne);

            index.remove(1);
            index.put(1,indexOne);
            index.remove(2);
            index.put(2,indexTwo);

            bitmap1 = BitmapFactory.decodeResource(getResources(), imageBase.getImageValue(indexOne));
            bitmap2 = BitmapFactory.decodeResource(getResources(), imageBase.getImageValue(indexTwo));
            imageView1.setImageBitmap(bitmap1);
            imageView2.setImageBitmap(bitmap2);
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
