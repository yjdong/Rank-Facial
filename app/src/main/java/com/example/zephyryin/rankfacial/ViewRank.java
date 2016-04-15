package com.example.zephyryin.rankfacial;

/**
 * Created by zephyryin on 11/18/14.
 */

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *  * @author allin
 *  *
 *  
 */
public class ViewRank extends ListActivity {
    private List<Map<String, Object>> mData;
    private Database db = null;
    TreeMap<Float,Integer> rank = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = getListItems();
        MyAdapter adapter = new MyAdapter(this);
        setListAdapter(adapter);
    }

    public List<Map<String, Object>> getListItems(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        db = new Database();
        String path = getApplicationContext().getFilesDir().getAbsolutePath()+"/scores.txt";
        db.readDatabase(path);

        rank = new TreeMap<Float, Integer>(new Comparator<Float>() {
            @Override
            public int compare(Float a, Float b) {
                return -a.compareTo(b);
            }
        });
        for(int i=1;i<=db.getSize();i++){
            rank.put(db.getScore(i),i);
        }



        Intent intent=this.getIntent();
        ArrayList<String> valueList = intent.getStringArrayListExtra("imageValue");

        int rankking =1;

        for(float key : rank.keySet()){
            int imgIndex = rank.get(key);
            Map<String, Object> item = new HashMap<String, Object>();

            item.put("img", Integer.valueOf(valueList.get(imgIndex-1)));
            item.put("rank", String.valueOf(rankking));
            item.put("score", String.valueOf(key));
            item.put("index",String.valueOf(imgIndex));

            list.add(item);
            rankking ++;
        }
        return list;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        //Log.v("MyListView4-click", (String)mData.get(position).get("title"));
    }

    public final class ViewHolder{
        public ImageView img;
        public TextView rankView;
        public TextView scoreView;
        public TextView indexView;
    }
    public class MyAdapter extends BaseAdapter{

        private LayoutInflater mInflater;


        public MyAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mData.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();

                convertView = mInflater.inflate(R.layout.ranklist, null);
                holder.img = (ImageView)convertView.findViewById(R.id.img);
                holder.rankView = (TextView)convertView.findViewById(R.id.rankking);
                holder.scoreView = (TextView)convertView.findViewById(R.id.score);
                holder.indexView = (TextView)convertView.findViewById(R.id.index);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }

            holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
            holder.rankView.setText((String)mData.get(position).get("rank"));
            holder.scoreView.setText("Scores:  "+(String)mData.get(position).get("score"));
            holder.indexView.setText("Index:  "+(String)mData.get(position).get("index"));

            return convertView;
        }

    }
}
