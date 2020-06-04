package com.example.alonemusic.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alonemusic.R;
import com.example.alonemusic.bean.Notification;
import com.example.alonemusic.bean.User;
import com.example.alonemusic.dao.NotificationDao;
import com.example.alonemusic.dao.UserDao;
import com.example.util.TipDialog;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecycleHolder> {

    List<Notification> notificationList;
    LayoutInflater layoutInflater;
    private UserDao userDao;
    private NotificationDao notificationDao;
    private Context context;
    private User lastUser;

    public RecyclerAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.notification_item, null);
        RecycleHolder recycleHolder = new RecycleHolder(view);
        return recycleHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleHolder holder, final int position) {
        holder.title.setText(notificationList.get(position).getTitle());
        //TODO alter
        //holder.headPortrait.setImageResource(Integer.parseInt(notificationList.get(position).getHeadPortrait()));
        holder.headPortrait.setImageResource(R.drawable.ic_icons8_lol_24);
        holder.content.setText(notificationList.get(position).getContent());
        holder.musicName.setText(notificationList.get(position).getMusicName());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"您选择了" + holder.title.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.imageButton.setTag("color = deepBlue");
        userDao = new UserDao(context);
        notificationDao = new NotificationDao(context);
        if(notificationDao.hasLoveNotificationInLoveNotificationList(notificationList.get(position).getId())){
            holder.imageButton.setTag("color = red");
            holder.imageButton.setColorFilter(ContextCompat.getColor(context, R.color.colorRed));
        }
        lastUser = userDao.findLastUser();
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastUser != null){
                    if(lastUser.getState() != 0){
                        if(v.getTag().equals("color = deepBlue")){
                            ContentValues values = new ContentValues();
                            values.put("user_id", lastUser.getId());
                            values.put("notification_id", notificationList.get(position).getId());
                            values.put("state", 1);
                            notificationDao.insertLoveNotification(values, notificationList.get(position).getId());
                            v.setTag("color = red");
                            ((ImageButton)v).setColorFilter(ContextCompat.getColor(context, R.color.colorRed));
                        }else {
                            ContentValues values = new ContentValues();
                            values.put("state", 0);
                            Log.d("RecyclerAdapter", "onClick: " + notificationList.get(position).getId());
                            notificationDao.updateLoveNotificationById(values, notificationList.get(position).getId());
                            v.setTag("color = deepBlue");
                            ((ImageButton)v).setColorFilter(ContextCompat.getColor(context, R.color.colorDeepBlue));
                        }
                    }else {
                        TipDialog.showNormalDialog(context, "登录后才能收藏");
                    }
                }else {
                    TipDialog.showNormalDialog(context, "登录后才能收藏");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    class RecycleHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView headPortrait;
        TextView content;
        ImageButton imageButton;
        TextView musicName;


        public RecycleHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notification_item_title);
            headPortrait = itemView.findViewById(R.id.notification_item_image);
            content = itemView.findViewById(R.id.notification_item_content);
            imageButton = itemView.findViewById(R.id.notification_love);
            musicName = itemView.findViewById(R.id.notification_item_music);
        }
    }
}
