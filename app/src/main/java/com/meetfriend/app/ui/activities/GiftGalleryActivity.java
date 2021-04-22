package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityGiftGalleryBinding;
import com.meetfriend.app.databinding.GiftRowBinding;
import com.meetfriend.app.responseclasses.CommonResponseClass;
import com.meetfriend.app.responseclasses.GiftListingDataModel;
import com.meetfriend.app.responseclasses.MyEarningDataModel;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.GiftViewModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;

public class GiftGalleryActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityGiftGalleryBinding binding;
    Context mContext;
    GiftViewModel giftViewModel;
    private Double totalCoins = 0.0;
    private String from = "";
    private String toId = "", postId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gift_gallery);
        mContext = this;
        initViews();
    }


    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
    }

    private void initViews() {
        from = getIntent().getStringExtra("from");
        if (from.equalsIgnoreCase("chat") || from.equalsIgnoreCase("gift")) {
            toId = getIntent().getStringExtra("to_id");
            postId = getIntent().getStringExtra("post_id");
        }
        PreferenceHandler.INSTANCE.writeString(mContext, "isSend", "0");

        giftViewModel = new ViewModelProvider(this).get(GiftViewModel.class);
        binding.headerLoginBackButton.setOnClickListener(this);
        binding.mBuyMore.setOnClickListener(this);
        binding.mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL | DividerItemDecoration.HORIZONTAL));
        initializeObservers();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myEarninginitializeObservers();
        giftSendinitializeObservers();
        getMyEarning();
        UtilsClass.updateUserStatus(mContext, true);
    }

    private void getMyEarning() {
        giftViewModel.myearning();
//        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    private void initializeObservers() {
        final Observer<GiftListingDataModel> favsObserver = new Observer<GiftListingDataModel>() {
            @Override
            public void onChanged(GiftListingDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    if (response.getResult().getData().size() > 0) {

                        List<GiftListingDataModel.Datum> list = response.getResult().getData();
                        Collections.reverse(list);
                        binding.mRecyclerView.setAdapter(new GiftListingAdapter(mContext, list, response.getMediaUrl()));
                    }
                }
            }
        };
        giftViewModel.getGiftListReponse().observe(this, favsObserver);
    }

    private void myEarninginitializeObservers() {
        final Observer<MyEarningDataModel> favsObserver = new Observer<MyEarningDataModel>() {
            @Override
            public void onChanged(MyEarningDataModel response) {
//                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    totalCoins = response.getResult().getTotalPurchasedCoins();
                    binding.mAvailableCoins.setText("Available Coins: " + String.format("%.2f", response.getResult().getTotalPurchasedCoins()));
                }
            }
        };
        giftViewModel.getMyEarningResponse().observe(this, favsObserver);
    }

    private void giftSendinitializeObservers() {
        final Observer<CommonResponseClass> favsObserver = new Observer<CommonResponseClass>() {
            @Override
            public void onChanged(CommonResponseClass response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();
                if (response.getStatus() == true) {
                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, response.getMessage());
                    finish();
                }
            }
        };
        giftViewModel.getGiftSendResponse().observe(this, favsObserver);
    }

    public void getData() {
        HashMap<String, Object> mHashMap = new HashMap<>();
        mHashMap.put("page", 1);
        mHashMap.put("per_page", 100);
        giftViewModel.getgiftList(mHashMap);
        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headerLoginBackButton:
                finish();
                break;
            case R.id.mBuyMore:
                startActivity(new Intent(mContext, CoinPlanPriceActivity.class));
                break;
        }
    }

    private class GiftListingAdapter extends RecyclerView.Adapter<GiftListingAdapter.ViewHolder> {
        Context mContext;
        List<GiftListingDataModel.Datum> data;
        String url;

        public GiftListingAdapter(Context mContext, List<GiftListingDataModel.Datum> data, String url) {
            this.data = data;
            this.mContext = mContext;
            this.url = url;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            GiftRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.gift_row, parent, false);

            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            NumberFormat nf = new DecimalFormat("##.###");
            holder.binding.mCoin.setText(nf.format(data.get(position).getCoins() )+ "");
            CommonMethods.INSTANCE.setImage(holder.binding.mImage, url + data.get(position).getFilePath());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (from.equalsIgnoreCase("chat") || from.equalsIgnoreCase("gift")) {
                        if (totalCoins >= data.get(position).getCoins()) {
                            if (from.equalsIgnoreCase("chat")) {
                                PreferenceHandler.INSTANCE.writeString(mContext, "coin", data.get(position).getCoins().toString());
                                PreferenceHandler.INSTANCE.writeString(mContext, "filePath", url + data.get(position).getFilePath().toString());
                                PreferenceHandler.INSTANCE.writeString(mContext, "isSend", "1");
                            }
                            CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
                            HashMap<String, Object> mHashMap = new HashMap<>();
                            mHashMap.put("to_id", toId);
                            mHashMap.put("post_id", postId);
                            mHashMap.put("coins", data.get(position).getCoins());
                            mHashMap.put("gift_id", data.get(position).getId());
                            giftViewModel.giftSent(mHashMap);
                        } else {
                            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "You don't have enough coins to send this gift.");
                            startActivity(new Intent(mContext, CoinPlanPriceActivity.class));
                        }
                    } else if (from.equalsIgnoreCase("request")) {
                        PreferenceHandler.INSTANCE.writeString(mContext, "coin_id", data.get(position).getId().toString());
                        PreferenceHandler.INSTANCE.writeString(mContext, "coin", data.get(position).getCoins().toString());
                        PreferenceHandler.INSTANCE.writeString(mContext, "filePath", url + data.get(position).getFilePath().toString());
                        PreferenceHandler.INSTANCE.writeString(mContext, "isSend", "1");
                        finish();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            GiftRowBinding binding;

            public ViewHolder(@NonNull GiftRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}