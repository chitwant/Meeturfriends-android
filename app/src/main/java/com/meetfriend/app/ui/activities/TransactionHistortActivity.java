package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityTransactionHistortBinding;
import com.meetfriend.app.databinding.TransactionHistoryRowBinding;
import com.meetfriend.app.responseclasses.TransactionHistoryDataModel;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.GiftViewModel;

import java.util.HashMap;
import java.util.List;

public class TransactionHistortActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    GiftViewModel giftViewModel;
    ActivityTransactionHistortBinding binding;
    int days = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_histort);
        mContext = this;
        initViews();
    }


    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
    }
    private void initViews() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        giftViewModel = new ViewModelProvider(this).get(GiftViewModel.class);
        binding.headerLoginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myEarninginitializeObservers();
        binding.mFilter.setOnClickListener(this);
        binding.mCancel.setOnClickListener(this);
        binding.mFirst.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.mFirst.setChecked(true);
                    binding.mSecond.setChecked(false);
                    binding.mThird.setChecked(false);
                    binding.mFourth.setChecked(false);
                    binding.mFifth.setChecked(false);
                    days = 10;
                    getMyEarning(days);
                }
            }
        });
        binding.mSecond.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.mFirst.setChecked(false);
                    binding.mSecond.setChecked(true);
                    binding.mThird.setChecked(false);
                    binding.mFourth.setChecked(false);
                    binding.mFifth.setChecked(false);
                    days = 30;
                    getMyEarning(days);
                }
            }
        });
        binding.mThird.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.mFirst.setChecked(false);
                    binding.mSecond.setChecked(false);
                    binding.mThird.setChecked(true);
                    binding.mFourth.setChecked(false);
                    binding.mFifth.setChecked(false);
                    days = 60;
                    getMyEarning(days);
                }
            }
        });
        binding.mFourth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.mFirst.setChecked(false);
                    binding.mSecond.setChecked(false);
                    binding.mThird.setChecked(false);
                    binding.mFourth.setChecked(true);
                    binding.mFifth.setChecked(false);
                    days = 90;
                    getMyEarning(days);
                }
            }
        });
        binding.mFifth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.mFirst.setChecked(false);
                    binding.mSecond.setChecked(false);
                    binding.mThird.setChecked(false);
                    binding.mFourth.setChecked(false);
                    binding.mFifth.setChecked(true);
                    days = 180;
                    getMyEarning(days);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyEarning(days);
        UtilsClass.updateUserStatus(mContext,true);
    }

    private void getMyEarning(int days) {
        HashMap<String, Object> mHashMap = new HashMap<>();
        mHashMap.put("last_no_of_days", days);
        giftViewModel.getTransactionHistory(mHashMap);
        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }


    private void myEarninginitializeObservers() {
        final Observer<TransactionHistoryDataModel> favsObserver = new Observer<TransactionHistoryDataModel>() {
            @Override
            public void onChanged(TransactionHistoryDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    binding.mCoin.setText(String.format("%.2f", response.getTotalCurrentCoins()) + " Coins");
                    if (response.getResult().size() > 0) {
                        binding.mNoFound.setVisibility(View.GONE);
                        binding.mRecyclerView.setVisibility(View.VISIBLE);
                        binding.mRecyclerView.setAdapter(new TransactionAdapter(mContext, response.getResult()));
                    } else {
                        binding.mNoFound.setVisibility(View.VISIBLE);
                        binding.mRecyclerView.setVisibility(View.GONE);
                    }
                }
            }
        };
        giftViewModel.getTransactioHistoryResponse().observe(this, favsObserver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mFilter:
                binding.mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.mCancel:
                binding.mDrawerLayout.closeDrawer(Gravity.RIGHT);
                break;
        }
    }

    private class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
        Context mContext;
        List<TransactionHistoryDataModel.Result> result;

        public TransactionAdapter(Context mContext, List<TransactionHistoryDataModel.Result> result) {
            this.mContext = mContext;
            this.result = result;
        }

        @NonNull
        @Override
        public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TransactionHistoryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.transaction_history_row, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {
            if (result.get(position).getCoinsType().toString().equalsIgnoreCase("gift")) {
                holder.binding.mName.setText(result.get(position).getGiftGallery().getName());
                if (result.get(position).getType().toString().equalsIgnoreCase("minus")) {
                    holder.binding.mCoin.setText("-" + result.get(position).getCoins());
                    holder.binding.mCoin.setTextColor(getColor(R.color.red_google_button));
                } else {
                    holder.binding.mCoin.setText("+" + result.get(position).getCoins());
                }
            } else {
                holder.binding.mName.setText("Purchased");
                holder.binding.mCoin.setText("+" + result.get(position).getCoins());
            }
            if (result.get(position).getPaymentStatus()==3){
                holder.binding.mStatus.setVisibility(View.VISIBLE);
            }else   if (result.get(position).getPaymentStatus()==2){
                holder.binding.mStatus.setText("Paid");
                holder.binding.mStatus.setVisibility(View.VISIBLE);
                holder.binding.mStatus.setTextColor(getColor(R.color.textColor_Green));
            }
            holder.binding.mTime.setText(result.get(position).getCreatedAt());
        }

        @Override
        public long getItemId(int position) {
            return  position;
        }

        @Override
        public int getItemViewType(int position) {
            return  position;
        }

        @Override
        public int getItemCount() {
            return result.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TransactionHistoryRowBinding binding;

            public ViewHolder(@NonNull TransactionHistoryRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}