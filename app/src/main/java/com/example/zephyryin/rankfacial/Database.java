package com.example.zephyryin.rankfacial;

import android.os.Parcelable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by zephyryin on 11/18/14.
 */
public class Database{
    Map<Integer,Float> scores =  new LinkedHashMap<Integer, Float>();

    public void reviseScore(int i, float value){
        scores.remove(i);
        scores.put(i,value);
//        for(int key : scores.keySet()){
//            Log.i("score", String.valueOf(key) + " " + String.valueOf(scores.get(key)));
//        }
//        Log.i("fuck","fuck");
    }

    public int getSize(){
        return scores.size();
    }

    public float getScore(int index){
        return scores.get(index);
    }

    public void readDatabase(String path){
        FileInputStream fis = null;
        BufferedReader reader = null;

        try {
            fis = new FileInputStream(path);
            reader = new BufferedReader(new InputStreamReader(fis));

            String line = reader.readLine();
            int cnt=1;           // start from 1
            while(line != null){
                float s = Float.valueOf(line);
                scores.put(cnt++,s);          // write to ;
                line = reader.readLine();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void writeDatabase(String path){
        FileOutputStream fis = null;
        BufferedWriter writer = null;

        try {
            fis = new FileOutputStream(path);
            writer = new BufferedWriter(new OutputStreamWriter(fis));
            for(int i=1; i<=scores.size();i++){
                writer.write(String.valueOf(scores.get(i)));
                writer.newLine();
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
                fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
