package com.meetfriend.app.network;

import com.meetfriend.app.responseclasses.CommonDataModel;
import com.meetfriend.app.responseclasses.CommonResponseClass;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface ApiInterface {

    @Multipart
    @POST
    Call<CommonDataModel> uploadImage(@Url String url,
                                      @Part MultipartBody.Part image);
    @GET
    Call<CommonResponseClass> opentokSession(@Url String url);
    @Multipart
    @POST
    Call<CommonDataModel> sendRequest(@Url String url, @PartMap HashMap<String, RequestBody> map);

}