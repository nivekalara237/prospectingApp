package com.niveka_team_dev.prospectingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.Utils;
import com.niveka_team_dev.prospectingapp.holders.ForumComMessageViewHolder;
import com.niveka_team_dev.prospectingapp.models.Message;
import com.niveka_team_dev.prospectingapp.models.User;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.Date;
import java.util.List;

public class ForumComMessageAdapter extends RecyclerView.Adapter<ForumComMessageViewHolder> implements FastScrollRecyclerView.SectionedAdapter{

    private Context context;
    private List<Message> messageList;
    private long me;
    private int layout = R.layout.forum_message_item;

    public ForumComMessageAdapter(Context context,List<Message> messages,long me){
        this.context = context;
        this.messageList = messages;
        this.me = me;
    }

    @NonNull
    @Override
    public ForumComMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout,parent,false);
        return new ForumComMessageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ForumComMessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        String date = Utils.convertJodaTimeToReadable(message.getTime());
        DateTimeUtils.setTimeZone("UTC");

        Log.e("ME-THIER",me+" -- "+message.getSenderId());

        if (me != message.getSenderId()){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.left_message_body.setText(message.getBody());
            holder.left_message_name.setText(message.getSenderName());
            //holder.left_message_time.setText(message.getTime());
            //if (message)
            TextDrawable drawable = TextDrawable.builder()
                    .buildRect(message.getSenderName().substring(0,1), Utils.randomColor());
            holder.left_imageProfile.setImageDrawable(drawable);


            if (DateTimeUtils.isDateTime(date)){
                if (DateTimeUtils.getDateDiff(new Date(),date, DateTimeUnits.MINUTES) < 6)
                    holder.left_message_time.setText("");
                else {
                    holder.left_message_time.setText(DateTimeUtils.formatTime(date,true));
                }
            }else if (DateTimeUtils.isYesterday(date)){
                holder.left_message_time.setText(context.getString(R.string.text0039)+DateTimeUtils.formatTime(date,true));
            }else {
                //DateTimeUtils.formatWithPattern("2017-06-13", "EEEE, MMMM dd, yyyy"); // Tuesday, June 13, 2017
                holder.left_message_time.setText(DateTimeUtils.formatWithPattern(Utils.getDateToJoda(message.getTime()), "EEEE, dd MMMM yyyy"));
            }


        }else {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.right_message_body.setText(message.getBody());
            if (DateTimeUtils.isDateTime(date)){
                if (DateTimeUtils.getDateDiff(new Date(),date, DateTimeUnits.MINUTES) < 6)
                    holder.right_message_time.setText("");
                else {
                    holder.right_message_time.setText(DateTimeUtils.formatTime(date,true));
                }
            }else if (DateTimeUtils.isYesterday(date)){
                holder.right_message_time.setText(context.getString(R.string.text0039)+DateTimeUtils.formatTime(date,true));
            }else {
                //DateTimeUtils.formatWithPattern("2017-06-13", "EEEE, MMMM dd, yyyy"); // Tuesday, June 13, 2017
                holder.right_message_time.setText(DateTimeUtils.formatWithPattern(Utils.getDateToJoda(message.getTime()), "EEEE, dd MMMM yyyy"));
            }

            if (message.isSeen()){
                holder.right_message_seen.setImageResource(R.drawable.ic_check_24dp);
            }else {
                holder.right_message_seen.setImageResource(R.drawable.ic_clock_24dp);
            }

        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public void addEntry(Message message) {
        //messageList.remove(message);
        //notifyItemRemoved(messageList.indexOf(message));
        //messageList.add(message);
        messageList.add(0,message);
        //notifyItemChanged(messageList.size());
        notifyItemInserted(0);
        //notifyDataSetChanged();
    }


    public void addEntryToTop(Message message) {
        messageList.add(messageList.size(),message);
        //notifyItemChanged(messageList.size());
        notifyItemInserted(messageList.size());
        //notifyDataSetChanged();
    }

    public void updateEntry(Message message){
        Message old = searchInListMessage(message.getKey());
        messageList.set(messageList.indexOf(old),message);
        notifyDataSetChanged();
    }

    public void removeEntry(Message message){
        messageList.remove(message);
        notifyItemRemoved(messageList.indexOf(message));
    }

    public Message searchInListMessage(String key){
        Message res = null;
        for (Message m:messageList){
            if (m.getKey() == key)
            {
                res = m;
                break;
            }
        }

        return res;
    }


    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.format("%d", position + 1);
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    private String getNameForItem(int position) {
        return String.format("Item %d", position + 1);
    }
}
