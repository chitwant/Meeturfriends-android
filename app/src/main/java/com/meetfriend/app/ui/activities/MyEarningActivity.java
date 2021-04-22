package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityMyEarningBinding;
import com.meetfriend.app.responseclasses.CommonResponseClass;
import com.meetfriend.app.responseclasses.MyEarningDataModel;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.GiftViewModel;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;

public class MyEarningActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityMyEarningBinding binding;
    Context mContext;
    GiftViewModel giftViewModel;
    private Double receivedCoins = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_earning);
        mContext = this;
        initViews();
        claimCoininitializeObservers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myEarninginitializeObservers();
        getMyEarning();
        UtilsClass.updateUserStatus(mContext,true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
    }

    private void getMyEarning() {
        giftViewModel.myearning();
        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    private void myEarninginitializeObservers() {
        final Observer<MyEarningDataModel> favsObserver = new Observer<MyEarningDataModel>() {
            @Override
            public void onChanged(MyEarningDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    receivedCoins =response.getResult().getTotalGiftRecievedUnpaidCoins();
                    binding.tvUserName.setText(response.getResult().getUser().getFirstName() + " " + response.getResult().getUser().getLastName());
                    CommonMethods.INSTANCE.setUserImage(binding.imageView, "https://meeturfriends.s3.amazonaws.com" + response.getResult().getUser().getProfilePhoto());
                    binding.mReceivedCoin.setText(String.format("%.2f", response.getResult().getTotalGiftRecievedUnpaidCoins()) + " Coins");
                    binding.mSentCoin.setText(String.format("%.2f", response.getResult().getTotalGiftSendCoins()) + " Coins");
                    binding.mPurchasedCoin.setText(String.format("%.2f", Double.parseDouble(response.getResult().getTotalPurchasedCoins() + "")) + " Coins");
//                    binding.mUnpaidCoin.setText(String.format("%.2f", Double.parseDouble(response.getResult().getTotalGiftRecievedUnpaidCoins() + "")) + " Coins");
                  binding.mLimit.setText("(when you have "+response.getResult().getRedeemCoinsLimit()+" coins you will be able to redeem your coins)");
                    if (receivedCoins < Double.parseDouble(response.getResult().getRedeemCoinsLimit())) {
                        binding.mRedeem.setEnabled(false);
                        binding.mRedeem.setBackgroundColor(getColor(R.color.light_grey));
                    } else {
                        binding.mRedeem.setEnabled(true);
                        binding.mRedeem.setBackgroundColor(getColor(R.color.app_blue));
                    }
                }
            }
        };
        giftViewModel.getMyEarningResponse().observe(this, favsObserver);
    }

    private void claimCoininitializeObservers() {
        final Observer<CommonResponseClass> favsObserver = new Observer<CommonResponseClass>() {
            @Override
            public void onChanged(CommonResponseClass response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, response.getMessage());
                    getMyEarning();
                }
            }
        };
        giftViewModel.getClaimCoinResponse().observe(this, favsObserver);
    }

    private void initViews() {
        giftViewModel = new ViewModelProvider(this).get(GiftViewModel.class);

        binding.mTransaction.setOnClickListener(this);
        binding.headerLoginBackButton.setOnClickListener(this);
        binding.mRedeem.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLoginBackButton:
                finish();
                break;
            case R.id.mRedeem:
                if (PreferenceHandler.INSTANCE.readInteger(mContext, "bankUpdated", 0) == 0)
                    startActivity(new Intent(mContext, BankCountryActivity.class));
                else {
                    showDialogBox();
                }
                break;
            case R.id.mTransaction:
                startActivity(new Intent(mContext, TransactionHistortActivity.class));
                break;
        }
    }

    private void showDialogBox() {
        UtilsClass.showDialog(mContext, "Are you sure you want to redeem these coins?", "", "Yes",
                (dialog, which) -> requestCoin(), null, "No", "", null, false);
    }

    private void requestCoin() {
        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
        giftViewModel.clainCoin();
    }
}