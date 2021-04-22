package com.meetfriend.app.network;

import android.app.Dialog;
import android.content.Context;

import com.meetfriend.app.R;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.Util;

import contractorssmart.app.utilsclasses.CommonMethods;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class ServerRequest<T> {

    private Context mContext;
    private Call<T> call;
    private Dialog dialog;


    public ServerRequest(final Context mContext, Call<T> call, boolean showProgress) {
        this.mContext = mContext;
        this.call = call;

        // final Dialog pd = new Dialog(mContext);
        if (showProgress) {
            CallProgressWheel.INSTANCE.showLoadingDialog(mContext);

        }

        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                try {
//                        if (pd.isShowing()) {
//                            pd.dismiss();
                    CallProgressWheel.INSTANCE.dismissLoadingDialog();

                    //   }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (response.isSuccessful())
                    afterResponse(call, response);
                else {
//                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, Util.getServerFailureMsg(mContext, response));
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                try {

                    ////}
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Util.showLog("Exp : " + t.getMessage());
                Util.showMessageDialog(mContext,"MeetUrFriends", "Server not responding. Please try after sometime");
            }
        });

    }


    private void afterResponse(Call<T> call, Response<T> response) {
        onCompletion(call, response);
    }

    public abstract void onCompletion(Call<T> call, Response<T> response);

    public void cancelRequest() {
        if (call.isExecuted()) {
            call.cancel();
        }
    }
}
