package com.android.flipmapapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by techjini on 7/1/17.
 */

public class MyAdapter extends BaseAdapter {

    private ArrayList<DataProvider> list;
    private Context context;


    MyAdapter(Context c, ArrayList<DataProvider> listArray) {
        this.list=listArray;
        this.context = c;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        if (row == null) {
            row = LayoutInflater.from(context).inflate(R.layout.row_item, viewGroup, false);
        }

        TextView name = (TextView) row.findViewById(R.id.name);
        TextView mobnum = (TextView) row.findViewById(R.id.mobnum);
        TextView fixnum = (TextView) row.findViewById(R.id.fixnum);
        ImageView image = (ImageView) row.findViewById(R.id.gmap) ;
        DataProvider temp = list.get(i);
       // Log.d("gsk","Name :"+temp.getName());
        name.setText(temp.getName());
        mobnum.setText(temp.getMobNumber());
        fixnum.setText(temp.getFixNumber());
        if(((!temp.getFixNumber().equals("null")))&&(!temp.getMobNumber().equals("null"))){
            image.setVisibility(View.VISIBLE);
        }
        else
        image.setVisibility(View.INVISIBLE);
        return row;
    }
}

