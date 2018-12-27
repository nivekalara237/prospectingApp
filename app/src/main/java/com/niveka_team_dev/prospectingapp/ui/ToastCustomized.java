package com.niveka_team_dev.prospectingapp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.niveka_team_dev.prospectingapp.R;

public class ToastCustomized {
    public Context context;
    private Toast toast;
    private View layout;
    private TextView text;
    private ImageView icon;

    public ToastCustomized build(CharSequence txt){

        /*LayoutInflater inflater = LayoutInflater.from(context);
        View root = ((Activity)context).getWindow().getDecorView().getRootView();
        layout = inflater.inflate(R.layout.custom_toast_view, (ViewGroup)root ,false);
        text = (TextView) layout.findViewById(R.id.text);*/

        text.setText(txt);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        toast.setGravity(Gravity.TOP|Gravity.START, 20, 150);
        toast.setDuration(Toast.LENGTH_LONG);
        layout.setLayoutParams(params);
        toast.setView(layout);

        return this;
    }
    public ToastCustomized build(){

        /*LayoutInflater inflater = LayoutInflater.from(context);
        View root = ((Activity)context).getWindow().getDecorView().getRootView();
        layout = inflater.inflate(R.layout.custom_toast_view, (ViewGroup)root ,false);
        text = (TextView) layout.findViewById(R.id.text);*/

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        toast.setGravity(Gravity.TOP|Gravity.START, 20, 150);
        toast.setDuration(Toast.LENGTH_LONG);
        layout.setLayoutParams(params);
        toast.setView(layout);

        return this;
    }

    public ToastCustomized _default(CharSequence txt){
        text.setText(txt);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
        toast.setGravity(Gravity.TOP|Gravity.START, 20, 150);
        toast.setDuration(Toast.LENGTH_LONG);
        layout.setLayoutParams(params);
        toast.setView(layout);
        toast.show();
        return this;
    }

    public void show(){
        if (toast!=null)
            toast.show();
    }

    public static ToastCustomized init(Context context){
        return new ToastCustomized(context);
    }

    public void reset(){
        if(toast!=null)
            toast.cancel();
    }

    public ToastCustomized gravity(int gravity,int xOffset,int yOffset){
        if(toast!=null){
            toast.setGravity(gravity, xOffset, yOffset);
        }

        return this;
    }

    public ToastCustomized time(int duration){
        if(toast!=null)
            toast.setDuration(duration);

        return this;
    }
    public void dismiss(){
        if(toast!=null)
            toast.cancel();
    }
    public ToastCustomized color(int intRes){
        if(toast!=null){
            layout.setBackgroundColor(context.getResources().getColor(intRes));
        }

        return this;
    }

    public ToastCustomized color(String colorString){
        if(toast!=null){
            layout.setBackgroundColor(Color.parseColor(colorString));
        }

        return this;
    }
    public ToastCustomized text(String txt){
        if(toast!=null){
            text.setText(txt);
        }

        return this;
    }
    public ToastCustomized icon(int intRes){
        if(toast!=null){
            icon.setImageResource(intRes);
            icon.setVisibility(View.VISIBLE);
        }

        return this;
    }
    public ToastCustomized icon(Drawable drawable){
        if(toast!=null){
            icon.setImageDrawable(drawable);
            icon.setVisibility(View.VISIBLE);
        }

        return this;
    }

    public ToastCustomized(Context context){
        this.context = context;
        this.toast = new Toast(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View root = ((Activity)context).getWindow().getDecorView().getRootView();
        layout = inflater.inflate(R.layout.custom_toast_view, (ViewGroup)root ,false);
        text = (TextView) layout.findViewById(R.id.text);
        icon = (ImageView)layout.findViewById(R.id.icon);
        icon.setVisibility(View.GONE);
    }

}
