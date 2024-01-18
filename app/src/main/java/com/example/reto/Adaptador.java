package com.example.reto;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {

    private ArrayList<String> itemList;
    private Context context;

    public Adaptador(Context context, ArrayList<String> itemList) {
        this.context = context;
        if (itemList != null) {
            this.itemList = itemList;
        } else {
            this.itemList = new ArrayList<>();
        }
    }


    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(itemList.get(position));
        textView.setTextColor(ContextCompat.getColor(context, R.color.cyan));
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    // Método para agregar elementos al adaptador
    public void add(String item) {
        itemList.add(item);
    }
}
