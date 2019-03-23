package com.niveka_team_dev.prospectingapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.activities.ForumCommerciauxActivity;
import com.niveka_team_dev.prospectingapp.listeners.OnFileDownloadButtomClickedListener;
import com.niveka_team_dev.prospectingapp.utilities.ImageUtils;
import com.niveka_team_dev.prospectingapp.utilities.Utils;
import com.niveka_team_dev.prospectingapp.holders.ForumComMessageViewHolder;
import com.niveka_team_dev.prospectingapp.models.Message;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ForumComMessageAdapter extends RecyclerView.Adapter<ForumComMessageViewHolder> implements FastScrollRecyclerView.SectionedAdapter{

    private Context context;
    private List<Message> messageList;
    private String me;
    private int layout = R.layout.forum_message_item;
    private OnFileDownloadButtomClickedListener fileDownloadButtomClickedListener;

    public ForumComMessageAdapter(Context context,List<Message> messages,String me){
        this.context = context;
        this.messageList = messages;
        this.me = me;
        if (context instanceof OnFileDownloadButtomClickedListener){
            fileDownloadButtomClickedListener = (OnFileDownloadButtomClickedListener)context;
        }
    }

    @NonNull
    @Override
    public ForumComMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout,parent,false);
        return new ForumComMessageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ForumComMessageViewHolder holder, final int position) {
        final Message message = messageList.get(position);
        String date = Utils.convertJodaTimeToReadable(message.getTime());
        DateTimeUtils.setTimeZone("UTC");

        if (!me.equals(message.getUser().getId())){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);

            if (message.getAttachment()!=null){
                holder.left_message_body.setVisibility(View.GONE);
                holder.imageLeftLayout.setVisibility(View.VISIBLE);
                holder.leftErrorLayout.setVisibility(View.VISIBLE);
                holder.left_attachement.setImageResource(R.drawable.ic_list_128dp);

                File extStore = Environment.getExternalStorageDirectory();
                File myFile = new File(extStore.getAbsolutePath()+"/"+ context.getString(R.string.firedapp_directory)
                +"/"+message.getAttachment().getName());

                if (myFile.exists()){
                    Bitmap b = ImageUtils.fileToBitmap(context,myFile);
                    holder.left_attachement.setImageBitmap(b);
                    holder.leftErrorLayout.setVisibility(View.GONE);
                    holder.left_progress_value.setVisibility(View.GONE);
                    holder.left_file_progress_bar.setVisibility(View.GONE);
                }else{
                    holder.leftErrorLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (fileDownloadButtomClickedListener!=null){
                                fileDownloadButtomClickedListener.onFileDownloadClicked(
                                        message,
                                        holder.left_attachement,
                                        holder.left_progress_value,
                                        holder.left_file_progress_bar,
                                        holder.leftErrorLayout
                                );
                            }
                        }
                    });
                }

            }else{
                holder.left_message_body.setVisibility(View.VISIBLE);
                holder.imageLeftLayout.setVisibility(View.GONE);
                holder.left_message_body.setText(message.getContenu());
                holder.left_message_name.setText(message.getUser().getLogin());
                if (DateTimeUtils.isToday(date)){
                    if (DateTimeUtils.getDateDiff(new Date(),date, DateTimeUnits.MINUTES) < 6)
                        holder.left_message_time.setText("");
                    else {
                        holder.left_message_time.setText(DateTimeUtils.formatTime(date,true));
                    }
                }else if (DateTimeUtils.isYesterday(date)){
                    holder.left_message_time.setText(context.getString(R.string.text0039)+DateTimeUtils.formatTime(date,true));
                }else {
                    //DateTimeUtils.formatWithPattern("2017-06-13", "EEEE, MMMM dd, yyyy"); // Tuesday, June 13, 2017
                    holder.left_message_time.setText(DateTimeUtils.formatWithPattern(date, "EEEE, dd MMMM yyyy"));
                }
            }

        }else {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);

            if (message.getAttachment()!=null){
                holder.right_message_body.setVisibility(View.GONE);
                holder.imageRightLayout.setVisibility(View.VISIBLE);
                holder.rightErrorLayout.setVisibility(View.VISIBLE);
                holder.right_attachement.setImageResource(R.drawable.ic_list_128dp);


                File extStore = Environment.getExternalStorageDirectory();
                File myFile = new File(extStore.getAbsolutePath()+"/"+ context.getString(R.string.firedapp_directory)
                        +"/"+message.getAttachment().getName());

                if (myFile.exists()){
                    Bitmap b = ImageUtils.fileToBitmap(context,myFile);
                    holder.right_attachement.setImageBitmap(b);
                    holder.rightErrorLayout.setVisibility(View.GONE);
                    holder.rigth_progress_value.setVisibility(View.GONE);
                    holder.right_file_progress_bar.setVisibility(View.GONE);
                }else{
                    holder.rightErrorLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (fileDownloadButtomClickedListener!=null){
                                fileDownloadButtomClickedListener.onFileDownloadClicked(
                                        message,
                                        holder.right_attachement,
                                        holder.rigth_progress_value,
                                        holder.right_file_progress_bar,
                                        holder.rightErrorLayout
                                );
                            }
                        }
                    });
                }

            }else{
                holder.right_message_body.setVisibility(View.VISIBLE);
                holder.imageRightLayout.setVisibility(View.GONE);
                holder.right_message_body.setText(message.getContenu());
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

                if (message.isVu()){
                    holder.right_message_seen.setImageResource(R.drawable.ic_check_24dp);
                }else {
                    holder.right_message_seen.setImageResource(R.drawable.ic_clock_24dp);
                }
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
        messageList.add(0,message);
        notifyItemInserted(0);
    }


    public void addEntryToTop(Message message) {
        messageList.add(messageList.size(),message);
        notifyItemInserted(messageList.size());
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
