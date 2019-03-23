package com.niveka_team_dev.prospectingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.holders.AttachmentViewHolder;
import com.niveka_team_dev.prospectingapp.listeners.OnRemoveItemAttachmentListener;
import com.niveka_team_dev.prospectingapp.models.Attachment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentViewHolder> {

    private List<Attachment> attachments = new ArrayList<>();
    private Context context;
    private OnRemoveItemAttachmentListener listener;

    public AttachmentAdapter(List<Attachment> attachments, Context context) {
        this.attachments = attachments;
        this.context = context;
        listener = (OnRemoveItemAttachmentListener)context;
    }

    @NonNull
    @Override
    public AttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.attachment_item,viewGroup,false);
        ButterKnife.bind(this,v);
        return new AttachmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentViewHolder holder, int i) {

        final Attachment attachment = attachments.get(i);
        holder.icon.setImageResource(attachment.getIconRes());
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
        this.attachments.clear();
        this.attachments.addAll(attachs);
        notifyDataSetChanged();
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
