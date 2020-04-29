package com.example.alonemusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alonemusic.R;
import com.example.alonemusic.bean.Notification;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecycleHolder> {

    List<Notification> notificationList;
    LayoutInflater layoutInflater;

    public RecyclerAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.notification_item, null);
        RecycleHolder recycleHolder = new RecycleHolder(view);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleHolder holder, int position) {
        holder.textView.setText(notificationList.get(position).getName());
        holder.imageView.setImageResource(notificationList.get(position).getHeadPortrait());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"您选择了" + holder.textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"您选择了" + holder.textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public RecycleHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.notification_item_text);
            imageView = itemView.findViewById(R.id.notification_item_image);
        }
    }
}
