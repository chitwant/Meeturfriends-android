package com.meetfriend.app.responseclasses;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.meetfriend.app.utilclasses.UtilsClass;

import contractorssmart.app.utilsclasses.PreferenceHandler;

public class InboxPOJO {
    @SerializedName("status")
    public boolean status;
    @SerializedName("users")
    public Users users;
    @SerializedName("data")
    public Data data;

    public static class Data {
        @SerializedName("users")
        public Users users;
        @SerializedName("lastMSG")
        public String lastMSG;
        @SerializedName("node")
        public String mainNode;
        @SerializedName("isBlock")
        public String isBlock;
        @SerializedName("lastmsgTIME")
        public Long lastmsgTIME;

        public String getTime(Context mContext) {
            if (lastmsgTIME != null)
                return UtilsClass.getPassedTimeString(mContext, "" + lastmsgTIME);
            else return "";
        }
    }

    public static class Users {
        @SerializedName("proNAME")
        public String proNAME;
        @SerializedName("proImage")
        public String proImage;
        @SerializedName("proID")
        public String proID;
        @SerializedName("custNAME")
        public String custNAME;
        @SerializedName("custImage")
        public String custImage;
        @SerializedName("custID")
        public String custID;

        public String getImage(Context mContext) {
            String uid = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
            if (uid.equalsIgnoreCase(custID))
                return proImage;
            else
                return custImage;
        }

        public String getName(Context mContext) {
            String uid = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
            if (uid.equalsIgnoreCase(custID))
                return proNAME;
            else
                return custNAME;
        }

        public String getID(Context mContext) {
            String uid = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
            if (uid.equalsIgnoreCase(custID))
                return proID;
            else
                return custID;
        }  public String getuID(Context mContext) {
            String uid = PreferenceHandler.INSTANCE.readString(mContext, "USER_ID", "");
            if (!uid.equalsIgnoreCase(custID))
                return proID;
            else
                return custID;
        }
    }
}
