package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityAddBankDetailBinding;
import com.meetfriend.app.databinding.BankRowBinding;
import com.meetfriend.app.responseclasses.BankByCountryDataModel;
import com.meetfriend.app.responseclasses.BankDetailDataModel;
import com.meetfriend.app.responseclasses.CommonResponseClass;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.GiftViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;

public class AddBankDetailActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityAddBankDetailBinding binding;
    Context mContext;
    String from = "", country_code = "", id = "";
    GiftViewModel giftViewModel;
    private List<BankByCountryDataModel.Detail> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_bank_detail);
        initViews();
    }


    @Override
    protected void onPause() {
        super.onPause();
        UtilsClass.updateUserStatus(mContext, false);
    }

    private void initViews() {
        mContext = this;
        giftViewModel = new ViewModelProvider(this).get(GiftViewModel.class);

        from = getIntent().getStringExtra("from");
        if (from.equalsIgnoreCase("add")) {
            country_code = getIntent().getStringExtra("country_code");
            id = getIntent().getStringExtra("id");
            binding.mCommonView.setVisibility(View.VISIBLE);
            binding.mSubmit.setVisibility(View.VISIBLE);
            binding.mEdit.setVisibility(View.GONE);
            BankinitializeObservers();
            BankUpdateinitializeObservers();
            getBankByCountry();
        } else {

//            PreferenceHandler.INSTANCE.writeInteger(mContext, "bankUpdated", 1);
            binding.mCommonView.setVisibility(View.GONE);
            binding.mSubmit.setVisibility(View.GONE);
            binding.mEdit.setVisibility(View.VISIBLE);
        }
        binding.ivBackIcon.setOnClickListener(this);
        binding.mSubmit.setOnClickListener(this);
        binding.mEdit.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (from.equalsIgnoreCase("update")) {
            if (PreferenceHandler.INSTANCE.readInteger(mContext, "bankUpdated", 0) == 0) {
                startActivity(new Intent(mContext, BankCountryActivity.class));
            } else {
                countryinitializeObservers();
                getCountry();
            }
        }
        UtilsClass.updateUserStatus(mContext, true);
    }

    private void getBankByCountry() {
        HashMap<String, Object> mHashMap = new HashMap<>();
        mHashMap.put("country_id", id);
        giftViewModel.getBankByCountry(mHashMap);

        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    private void BankinitializeObservers() {
        final Observer<BankByCountryDataModel> favsObserver = new Observer<BankByCountryDataModel>() {
            @Override
            public void onChanged(BankByCountryDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {

                    if (response.getResult().getDetails().size() > 0) {
                        list = response.getResult().getDetails();
                        binding.mRecyclerView.setAdapter(new BankCountryRowAdapter(mContext, response.getResult().getDetails()));

                    }
                }
            }
        };
        giftViewModel.getBankByCountryResponse().observe(this, favsObserver);
    }

    private void BankUpdateinitializeObservers() {
        final Observer<CommonResponseClass> favsObserver = new Observer<CommonResponseClass>() {
            @Override
            public void onChanged(CommonResponseClass response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();
                if (response.getStatus() == true) {
                    PreferenceHandler.INSTANCE.writeInteger(mContext, "bankUpdated", 1);
                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, response.getMessage());
                    finish();
                }
            }
        };
        giftViewModel.getUpdateBankResponse().observe(this, favsObserver);
    }

    private void getCountry() {
        giftViewModel.getBankDetail();

        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    private void countryinitializeObservers() {
        final Observer<BankDetailDataModel> favsObserver = new Observer<BankDetailDataModel>() {
            @Override
            public void onChanged(BankDetailDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    Map<String, String> map = response.getResult();
                    if (map != null) {
                        Log.e("map_response", map.toString());
                        List value = new ArrayList<>();
                        List keys = new ArrayList<>();
                        Set mapSet = map.entrySet();
                        Iterator mapIterator = mapSet.iterator();
                        Map.Entry mapEntry;
                        while (mapIterator.hasNext()) {
                            mapEntry = (Map.Entry) mapIterator.next();
                            String key = (String) mapEntry.getKey();
                            keys.add(key);
                            value.add((String) map.get(key));
                        }
                        if (keys.size() > 0) {
                            binding.mRecyclerView.setAdapter(new BankRowAdapter(mContext, keys, value));

                        }
                    } else {
//                        startActivity(new Intent(mContext, BankCountryActivity.class));
                    }

                }
            }
        };
        giftViewModel.getBankDetailResponse().observe(this, favsObserver);
    }

    @Override
    public void onBackPressed() {
//        if (from.equalsIgnoreCase("add")) {
//            startActivity(new Intent(mContext, BankCountryActivity.class));
//        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackIcon:
//                if (from.equalsIgnoreCase("add")) {
//                    startActivity(new Intent(mContext, BankCountryActivity.class));
//                }
                finish();

                break;
            case R.id.mSubmit:
                if (checkConditions()) {
                    CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
                    HashMap<String, Object> mHashMap = new HashMap<>();
                    mHashMap.put("email", binding.mEmail.getText().toString());
                    mHashMap.put("country_code", binding.mCountryCode.getText().toString());
                    mHashMap.put("phone", binding.mPhoneNumber.getText().toString());
                    mHashMap.put("address_country", country_code);
                    mHashMap.put("bank_name", binding.mBankName.getText().toString());
                    mHashMap.put("account_holder_name", binding.mUserName.getText().toString());
                    for (int i = 0; i < list.size(); i++) {
                        mHashMap.put(list.get(i).getCodeParam(), list.get(i).getValues());
                    }
                    giftViewModel.updateBank(mHashMap);
                }
                break;
            case R.id.mEdit:
                startActivity(new Intent(mContext, BankCountryActivity.class));
                break;
        }
    }

    private boolean checkConditions() {
        if (binding.mEmail.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Email is Empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.mEmail.getText().toString()).matches()) {
            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Email is not valid");
            return false;
        } else if (binding.mCountryCode.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Country Code is Empty");
            return false;
        } else if (binding.mPhoneNumber.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Phone Number is Empty");
            return false;
        } else if (binding.mPhoneNumber.getText().toString().length() < 8) {
            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Phone Number is not valid");
            return false;
        } else if (binding.mBankName.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Bank Name is Empty");
            return false;
        } else if (binding.mUserName.getText().toString().equalsIgnoreCase("")) {
            CommonMethods.INSTANCE.showToastMessageAtTop(mContext, "Account holder name is Empty");
            return false;
        } else {
            boolean val = true;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getValues() == null) {
                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, list.get(i).getTitle() + " is Empty");
                    val = false;
                    return false;
                } else if (list.get(i).getValues().equalsIgnoreCase("")) {
                    CommonMethods.INSTANCE.showToastMessageAtTop(mContext, list.get(i).getTitle() + " is Empty");
                    val = false;
                    return false;
                }
            }
            return val;
        }
    }

    private class BankRowAdapter extends RecyclerView.Adapter<BankRowAdapter.ViewHolder> {
        Context mContext;
        List keys;
        List value;

        public BankRowAdapter(Context mContext, List keys, List value) {
            this.mContext = mContext;
            this.keys = keys;
            this.value = value;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            BankRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.bank_row, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.mAccountNumber.setText(value.get(position).toString());
            holder.binding.mTitle.setText(keys.get(position).toString());
            if (from.equalsIgnoreCase("add")) {
                holder.binding.mAccountNumber.setEnabled(true);
            } else
                holder.binding.mAccountNumber.setEnabled(false);
        }

        @Override
        public int getItemCount() {
            return keys.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            BankRowBinding binding;

            public ViewHolder(@NonNull BankRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }

    private class BankCountryRowAdapter extends RecyclerView.Adapter<BankCountryRowAdapter.ViewHolder> {
        Context mContext;
        List<BankByCountryDataModel.Detail> details;

        public BankCountryRowAdapter(Context mContext, List<BankByCountryDataModel.Detail> details) {
            this.mContext = mContext;
            this.details = details;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            BankRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.bank_row, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.mTitle.setText(details.get(position).getTitle());
            holder.binding.mAccountNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    list.get(position).setValues(holder.binding.mAccountNumber.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return details.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            BankRowBinding binding;

            public ViewHolder(@NonNull BankRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}