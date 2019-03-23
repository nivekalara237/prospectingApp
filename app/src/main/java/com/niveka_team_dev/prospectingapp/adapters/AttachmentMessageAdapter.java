package com.niveka_team_dev.prospectingapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.holders.AttachmentMessageViewHolder;
import com.niveka_team_dev.prospectingapp.listeners.OnRemoveItemAttachmentListener;
import com.niveka_team_dev.prospectingapp.models.Attachment;
import com.niveka_team_dev.prospectingapp.utilities.Utils;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;

public class AttachmentMessageAdapter extends RecyclerView.Adapter<AttachmentMessageViewHolder> {

    private List<Attachment> attachments;
    private Context context;
    private OnRemoveItemAttachmentListener listener;

    public AttachmentMessageAdapter(List<Attachment> attachments, Context context) {
        this.attachments = attachments;
        this.context = context;
        listener = (OnRemoveItemAttachmentListener)context;
    }

    @NonNull
    @Override
    public AttachmentMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.attachment_item,viewGroup,false);
        //ButterKnife.bind(this,v);
        return new AttachmentMessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentMessageViewHolder holder, int i) {
        final Attachment attachment = attachments.get(i);
        if (!attachment.isImage())
            holder.icon.setImageResource(R.drawable.ic_file_grey_128dp);
        else {
            //Log.e("IMAGE",attachment.getPath());
            File f = new File(attachment.getPath());
            Bitmap d = new BitmapDrawable(context.getResources(), f.getAbsolutePath()).getBitmap();
            //Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(512, com.fxn.utility.Utility.getExifCorrectedBitmap(f));
            Bitmap scaled = com.fxn.utility.Utility.getScaledBitmap(512, d);
            holder.icon.setImageBitmap(scaled);
        }

        holder.title.setText(attachment.getName());
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.removeAttachment(attachment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    public void addAttachments(List<Attachment> attachs){
        if (!attachs.isEmpty()){
            this.attachments.clear();
            this.attachments.addAll(attachs);
            notifyDataSetChanged();
        }

        //notifyItemRangeChanged(0,att);
    }
    public void addItemAttachment(Attachment attach){
        this.attachments.add(attach);
        notifyItemInserted(this.attachments.indexOf(attach));
        //notifyItemRangeChanged(0,att);
    }
    public void removeAttachments(List<Attachment> attachs){
        //this.attachments.clear();
        this.attachments.removeAll(attachs);
        notifyDataSetChanged();
        //notifyAll();
        //notifyItemRangeChanged(0,att);
    }

    public void rempoveAttach(Attachment attachment){
        int position = this.attachments.indexOf(attachment);
        this.attachments.remove(attachment);
        notifyItemRemoved(position);
        //notifyDataSetChanged();
    }
}
