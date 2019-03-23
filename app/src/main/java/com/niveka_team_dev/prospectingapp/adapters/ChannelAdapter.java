package com.niveka_team_dev.prospectingapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.niveka_team_dev.prospectingapp.R;
import com.niveka_team_dev.prospectingapp.holders.ChannelViewHolder;
import com.niveka_team_dev.prospectingapp.listeners.OnItemClickListener;
import com.niveka_team_dev.prospectingapp.models.Channel;
import com.niveka_team_dev.prospectingapp.ui.FontCache;
import com.niveka_team_dev.prospectingapp.utilities.ImageUtils;
import com.niveka_team_dev.prospectingapp.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelViewHolder> {

    private List<Channel> channels = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;

    public ChannelAdapter(List<Channel> channelList,Context ctx){
        this.channels = channelList;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.channel_item,parent,false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChannelViewHolder holder, final int position) {
        Channel channel = channels.get(position);
        holder.name.setText(channel.getDesignation());
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(channel.getDesignation().substring(0,2), Utils.randomColor());

        TextDrawable drawable_ = TextDrawable.builder()
                .beginConfig()
                    .textColor(Utils.randomColor())
                    //.useFont(Typeface.DEFAULT_BOLD)
                    .fontSize(38) /* size in px */
                    .useFont(FontCache.get("fonts/futura_bold_font.ttf",context))
                    //.bold()
                    .toUpperCase()
                .endConfig()
                .buildRound(channel.getDesignation().substring(0,2), context.getResources().getColor(R.color.flatui_light_1));
        holder.image.setImageDrawable(drawable_);
        if (channel.getLastMessage()!=null){
            holder.lastMessage.setText(channel.getLastMessage().getContenu());
            holder.nbMessage.setText(String.valueOf(channel.getCountUnread()));
        }else{
            holder.lastMessage.setText(null);
            holder.dateLastMessage.setText(null);
        }

        if (channel.getCountUnread()==0){
            holder.nbMessage.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position,view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }
}