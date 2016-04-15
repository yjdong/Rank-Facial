package com.example.zephyryin.rankfacial;

import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by zephyryin on 11/18/14.
 */
public class ImageBase {
    //private ArrayList<DrawableImage> drawableImages;
    private TreeMap<Integer,Integer> drawableImages = null;

    public void getImages(){
        //ArrayList<DrawableImage> imageArray = new ArrayList<DrawableImage>();
        //drawableImages = new ArrayList<DrawableImage>();
        drawableImages = new TreeMap<Integer, Integer>();
        Class<R.drawable> c = R.drawable.class;

        for (Field f : c.getFields()){
            try {
                if(f.getName().length()>5)
                    continue;
                //DrawableImage di = new DrawableImage();

                int index = Integer.valueOf(f.getName().substring(1));
                int value = f.getInt(f);
                drawableImages.put(index, value);
                //Log.i("imagevalue",String.valueOf(index)+"  value:"+String.valueOf(value));

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
       // Log.i("data","data");
    }

    public int getSize(){
        return drawableImages.size();
    }

    public int getImageValue(int index){
        return drawableImages.get(index);
    }
}
