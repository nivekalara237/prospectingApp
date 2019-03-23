package com.niveka_team_dev.prospectingapp.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.firestore.Exclude;
import com.niveka_team_dev.prospectingapp.R;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment implements Serializable,Parcelable {
    private String path;
    private String name;
    private boolean isImage;
    private String type;
    private float length;
    @JsonIgnore
    @Exclude
    private Uri uri;

    public Attachment(String path, String name, boolean isImage) {
        this.path = path;
        this.name = name;
        this.isImage = isImage;
    }

    public Attachment() {
    }


    protected Attachment(Parcel in) {
        path = in.readString();
        name = in.readString();
        isImage = in.readByte() != 0;
        type = in.readString();
        length = in.readFloat();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Attachment> CREATOR = new Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel in) {
            return new Attachment(in);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", isImage=" + isImage +
                ", type='" + type + '\'' +

                ", length=" + length +
                '}';
    }

    public int getIconRes(){
        int res = R.drawable.ic_file_32dp;
        if (type.toLowerCase().contains("image/"))
            res = R.drawable.ic_png_32dp;
        else if(type.toLowerCase().contains("application/vnd.openxmlformats-officedocument"))
            res = R.drawable.ic_word_doc_32dp;
        else if (
                type.toLowerCase().contains("video/")||
                type.toLowerCase().contains("application/mp4")
                )
            res = R.drawable.ic_mp4_32dp;
        else if (type.toLowerCase().contains("audio/"))
            res = R.drawable.ic_mp3_32dp;

        else if (type.toLowerCase().contains("application/pdf"))
            res = R.drawable.ic_pdf_32dp;
        else if (
                type.toLowerCase().contains("application/vnd.ms-excel")||
                type.toLowerCase().contains("text/csv")
                )
            res = R.drawable.ic_excel_32dp;
        else if (
                type.toLowerCase().contains("application/x-7z-compressed")||
                type.toLowerCase().contains("application/zip")
                )
            res = R.drawable.ic_zip_32dp;
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(path);
        parcel.writeString(name);
        parcel.writeByte((byte) (isImage ? 1 : 0));
        parcel.writeString(type);
        parcel.writeFloat(length);
        parcel.writeParcelable(uri, i);
    }
}
