package com.ihandy.a2014011425;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by max on 16-9-2.
 */
public class NewsContent implements Parcelable{
    public String title;
    public String urlstr;
    @Nullable
    public String imageurl = null;
    public long newsid;
    public String category;
    public String origin;
    public static final int FAVOURITE = 1;
    public static final int NOT_FAVOURITE = 0;
    int favourite;  // =1 if favourite; =0 if not
    public NewsContent(){
        title = "一切反动派都是纸老虎";
        urlstr = "";
        favourite = NOT_FAVOURITE;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeString(title);
        parcel.writeString(urlstr);
        parcel.writeString(imageurl);
        parcel.writeLong(newsid);
        parcel.writeString(category);
        parcel.writeString(origin);
        parcel.writeInt(favourite);
    }
    public static final Parcelable.Creator<NewsContent> CREATOR = new Creator<NewsContent>() {
        @Override
        public NewsContent createFromParcel(Parcel source) {
            NewsContent res = new NewsContent();
            res.title = source.readString();
            res.urlstr = source.readString();
            res.imageurl = source.readString();
            res.newsid = source.readLong();
            res.category = source.readString();
            res.origin = source.readString();
            res.favourite = source.readInt();
            return res;
        }

        @Override
        public NewsContent[] newArray(int size) {
            return new NewsContent[size];
        }
    };
}
