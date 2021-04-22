package com.meetfriend.app.responseclasses;

import android.content.Context;

import com.meetfriend.app.utilclasses.UtilsClass;

import java.util.List;

public class ChatPOJO {

    public List<Message> messages;
    public String custID, proID, custNAME, proNAME, custImage, proImage, lastMSG, lastmsgTIME;

    public ChatPOJO(String custID, String proID, String custNAME, String proNAME, String custImage, String proImage) {
        this.custID = custID;
        this.proID = proID;
        this.custNAME = custNAME;
        this.proNAME = proNAME;
        this.custImage = custImage;
        this.proImage = proImage;
    }

    public static class Message {
        public String fromID, toID, msgTEXT, msgTYPE, file, gift_id, id, isRead, isSave, senderMsgId;
        public long msgTIME;

        public String getMsgTime(Context mContext) {
            return UtilsClass.getPassedTimeString(mContext, "" + msgTIME);
        }
    }
}