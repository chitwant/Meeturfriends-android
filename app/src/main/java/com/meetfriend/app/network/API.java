package com.meetfriend.app.network;

import com.meetfriend.app.responseclasses.CommonDataModel;
import com.meetfriend.app.responseclasses.CommonResponseClass;
import com.meetfriend.app.ui.activities.ChatActivity;
import com.meetfriend.app.utilclasses.Util;

import java.io.File;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class API {
    public static Call<CommonResponseClass> opentokSession(String friend_id,String call_type,String type) {
        return Util.requestApiDefault().opentokSession("api/chat/get-token?friend_id="+friend_id+"&call_type="+call_type+"&type="+type);
    }

    public static Call<CommonDataModel> uploadVideo(String image, ChatActivity chatActivity) {
        MultipartBody.Part fileToUpload;
        if (image.equalsIgnoreCase("")) {
            ProgressRequestBody fileBody = new ProgressRequestBody(new File(image), "multipart/form-data", chatActivity);
            fileToUpload = MultipartBody.Part.createFormData("media", "", fileBody);
        } else {
            File file = new File(image);
            ProgressRequestBody fileBody = new ProgressRequestBody(new File(image), "multipart/form-data", chatActivity);
            fileToUpload = MultipartBody.Part.createFormData("media", file.getName(), fileBody);
        }
        return Util.requestApiDefault().uploadImage("api/chat/upload-media",
                fileToUpload);
    }
    public static Call<CommonDataModel> opentokNotification(String friend_id, String sessionid, String opentoktoken, String type) {
        HashMap<String, RequestBody> map = new HashMap();
        map.put("friend_id", RequestBody.create(MultipartBody.FORM, friend_id));
        map.put("sessionid", RequestBody.create(MultipartBody.FORM, sessionid));
        map.put("opentoktoken", RequestBody.create(MultipartBody.FORM, opentoktoken));
        map.put("type", RequestBody.create(MultipartBody.FORM, type));
        return Util.requestApiDefault().sendRequest( "api/user/opentok_notification", map);
    }
    public static Call<CommonDataModel> sendNoti(String friend_id, String message, String gift_id, String type) {
        HashMap<String, RequestBody> map = new HashMap();
        map.put("friend_id", RequestBody.create(MultipartBody.FORM, friend_id));
        map.put("message", RequestBody.create(MultipartBody.FORM, message));
        map.put("gift_id", RequestBody.create(MultipartBody.FORM, gift_id));
        map.put("type", RequestBody.create(MultipartBody.FORM, type));
        return Util.requestApiDefault().sendRequest( "api/chat/send-notification", map);
    }
}