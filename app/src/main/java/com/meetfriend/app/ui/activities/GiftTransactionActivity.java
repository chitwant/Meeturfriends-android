package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityGiftTransactionBinding;
import com.meetfriend.app.databinding.GiftTransactionRowBinding;
import com.meetfriend.app.responseclasses.GiftTransactionDataModel;
import com.meetfriend.app.responseclasses.MyEarningDataModel;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.GiftViewModel;

import java.util.List;

public class GiftTransactionActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityGiftTransactionBinding binding;
    Context mContext;
    GiftViewModel giftViewModel;
    private Double totalReceived, totalSent;
    private String from = "receive";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift_transaction);
        mContext = this;
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UtilsClass.updateUserStatus(mContext, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
    }
    private void sentinitializeObservers() {
        final Observer<GiftTransactionDataModel> favsObserver = new Observer<GiftTransactionDataModel>() {
            @Override
            public void onChanged(GiftTransactionDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();
                if (response.getStatus() == true) {
                    if (response.getResult().size() > 0) {
                        binding.mRecyclerView.setVisibility(View.VISIBLE);
                        binding.mNoGift.setVisibility(View.GONE);
                        binding.mRecyclerView.setAdapter(new TransactionAapter(mContext, response.getResult(), response.getMediaUrl()));
                    } else {
                        binding.mRecyclerView.setVisibility(View.GONE);
                        binding.mNoGift.setVisibility(View.VISIBLE);
                        if (from.equalsIgnoreCase("receive"))
                            binding.mNoGift.setText("No Gift Received");
                        else
                            binding.mNoGift.setText("No Gift Sent");
                    }
                }
            }
        };
        giftViewModel.getGiftsentResponse().observe(this, favsObserver);
    }

    private void receivedinitializeObservers() {
        final Observer<GiftTransactionDataModel> favsObserver = new Observer<GiftTransactionDataModel>() {
            @Override
            public void onChanged(GiftTransactionDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    if (response.getResult().size() > 0) {
                        binding.mRecyclerView.setVisibility(View.VISIBLE);
                        binding.mNoGift.setVisibility(View.GONE);
                        binding.mRecyclerView.setAdapter(new TransactionAapter(mContext, response.getResult(), response.getMediaUrl()));
                    } else {
                        binding.mRecyclerView.setVisibility(View.GONE);
                        binding.mNoGift.setVisibility(View.VISIBLE);
                        if (from.equalsIgnoreCase("receive"))
                            binding.mNoGift.setText("No Gift Received");
                        else
                            binding.mNoGift.setText("No Gift Sent");
                    }
                }
            }
        };
        giftViewModel.getGiftReceivedResponse().observe(this, favsObserver);
    }

    private void initViews() {
        giftViewModel = new ViewModelProvider(this).get(GiftViewModel.class);
        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
        giftViewModel.giftReceived();
        binding.headerLoginBackButton.setOnClickListener(this);
        binding.mTabs.addTab(binding.mTabs.newTab().setText("Received History"));
        binding.mTabs.addTab(binding.mTabs.newTab().setText("Sent History"));
        binding.mTabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition() == 0) {
                    from = "receive";
                    CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
                    giftViewModel.giftReceived();
                    binding.mCoins.setText("Total Received: " + String.format("%.2f", totalReceived) + " Coins");
                    binding.mRecyclerView.setVisibility(View.GONE);
                    binding.mNoGift.setVisibility(View.VISIBLE);
                    binding.mNoGift.setText("No Gift Received");
                } else {
                    from = "semt";
                    CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
                    giftViewModel.giftSent();
                    binding.mCoins.setText("Total Sent: " + String.format("%.2f", totalSent) + " Coins");
                    binding.mRecyclerView.setVisibility(View.GONE);
                    binding.mNoGift.setVisibility(View.VISIBLE);
                    binding.mNoGift.setText("No Gift Sent");
                }
                getMyEarning();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    from = "receive";
                    CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
                    giftViewModel.giftReceived();
                    binding.mCoins.setText("Total Received: " + String.format("%.2f", totalReceived) + " Coins");
                    binding.mRecyclerView.setVisibility(View.GONE);
                    binding.mNoGift.setVisibility(View.VISIBLE);
                    binding.mNoGift.setText("No Gift Received");
                } else {
                    from = "sent";
                    CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
                    giftViewModel.giftSent();
                    binding.mCoins.setText("Total Sent: " + String.format("%.2f", totalSent) + " Coins");
                    binding.mRecyclerView.setVisibility(View.GONE);
                    binding.mNoGift.setVisibility(View.VISIBLE);
                    binding.mNoGift.setText("No Gift Sent");
                }
                getMyEarning();

            }
        });
        myEarninginitializeObservers();
        sentinitializeObservers();
        receivedinitializeObservers();
        getMyEarning();
    }

    private void getMyEarning() {
        giftViewModel.myearning();
//        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    private void myEarninginitializeObservers() {
        final Observer<MyEarningDataModel> favsObserver = new Observer<MyEarningDataModel>() {
            @Override
            public void onChanged(MyEarningDataModel response) {
//                CallProgressWheel.INSTANCE.dismissLoadingDialog();
                if (response.getStatus() == true) {
                    totalReceived = response.getResult().getTotalGiftRecievedCoins();
                    totalSent =response.getResult().getTotalGiftSendCoins();
                    if (from.equalsIgnoreCase("receive")) {
                        binding.mCoins.setText("Total Received: " + String.format("%.2f", totalReceived) + " Coins");
                    } else
                        binding.mCoins.setText("Total Sent: " + String.format("%.2f", totalSent) + " Coins");

                }
            }
        };
        giftViewModel.getMyEarningResponse().observe(this, favsObserver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLoginBackButton:
                finish();
                break;

        }
    }

    private class TransactionAapter extends RecyclerView.Adapter<TransactionAapter.ViewHolder> {
        Context mContext;
        List<GiftTransactionDataModel.Result> result;
        String mediaUrl;

        public TransactionAapter(Context mContext, List<GiftTransactionDataModel.Result> result, String mediaUrl) {
            this.mContext = mContext;
            this.result = result;
            this.mediaUrl = mediaUrl;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GiftTransactionRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.gift_transaction_row, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (result.get(position).getFromUser()!=null)
            holder.binding.mName.setText(result.get(position).getFromUser().getFirstName() + " " + result.get(position).getFromUser().getLastName());
            holder.binding.mCoin.setText(result.get(position).getCoins() + " Coins");
            holder.binding.mDate.setText(result.get(position).getCreatedAt());
            Glide.with(mContext).load("https://meeturfriends.s3.amazonaws.com" + result.get(position).getGiftGallery().getFilePath()).into(holder.binding.mImage);
            if (from.equalsIgnoreCase("receive")) {
                holder.binding.mStatus.setVisibility(View.VISIBLE);
                if (result.get(position).getPaymentStatus()==1){
                    holder.binding.mStatus.setVisibility(View.GONE);
                }else   if (result.get(position).getPaymentStatus()==2){
                    holder.binding.mStatus.setText("Paid");
                    holder.binding.mStatus.setTextColor(getColor(R.color.textColor_Green));
                }
            } else {
                holder.binding.mStatus.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return result.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            GiftTransactionRowBinding binding;

            public ViewHolder(@NonNull GiftTransactionRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}