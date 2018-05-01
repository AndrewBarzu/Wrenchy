package com.example.xghos.loginregister;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.MyHolder> {

    private Context mContext;
    private List<MyDate> mDates;

    public DateAdapter( Context context, List<MyDate> dates) {
        mContext = context;
        mDates = dates;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public TextView date, name;

        public MyHolder(View view) {
            super(view);
            date = view.findViewById(R.id.day);
            name = view.findViewById(R.id.dayName);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_item, parent, false);

        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.date.setText(mDates.get(position).getDay());
        holder.name.setText(mDates.get(position).getDayName());
    }

    @Override
    public int getItemCount() {
        return mDates.size();
    }
}
