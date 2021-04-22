package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityBankCountryBinding;
import com.meetfriend.app.databinding.BankCountryRowBinding;
import com.meetfriend.app.responseclasses.BankCountryDataModel;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.GiftViewModel;

import java.util.ArrayList;
import java.util.List;

public class BankCountryActivity extends AppCompatActivity {
    Context mContext;
    GiftViewModel giftViewModel;
    ActivityBankCountryBinding binding;
    private List<BankCountryDataModel.Result> countryList = new ArrayList<>();
    private CountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bank_country);
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

    private void initViews() {
        giftViewModel = new ViewModelProvider(this).get(GiftViewModel.class);
        binding.headerLoginBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        countryinitializeObservers();
        getCountry();
        binding.mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() > 0) {
                    if (countryList != null) {
                        if (countryList.size() > 0) {
                            adapter.getFilter().filter(newText);
                        }
                    }
                } else {
                    adapter = new CountryAdapter(mContext, countryList);
                    binding.mRecyclerView.setAdapter(adapter);
                }
                return false;
            }
        });
    }

    private void getCountry() {
        giftViewModel.getBankCountry();

        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    private void countryinitializeObservers() {
        final Observer<BankCountryDataModel> favsObserver = new Observer<BankCountryDataModel>() {
            @Override
            public void onChanged(BankCountryDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    if (response.getResult().size() > 0) {
                        countryList = response.getResult();
                        adapter = new CountryAdapter(mContext, countryList);
                        binding.mRecyclerView.setAdapter(adapter);
                    }
                }
            }
        };
        giftViewModel.getBankCountryResponse().observe(this, favsObserver);
    }

    private class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> implements Filterable {
        Context mContext;
        List<BankCountryDataModel.Result> countryList;
        List<BankCountryDataModel.Result> filterList;
        List<BankCountryDataModel.Result> temp;

        public CountryAdapter(Context mContext, List<BankCountryDataModel.Result> countryList) {
            this.mContext = mContext;
            this.countryList = countryList;
            this.temp = countryList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            BankCountryRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.bank_country_row, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.binding.mCheckBox.setText(countryList.get(position).getCountry());
            holder.binding.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(mContext, AddBankDetailActivity.class).putExtra("from", "add")
                            .putExtra("country_code", countryList.get(position).getCountryCode())
                            .putExtra("id", countryList.get(position).getId() + ""));
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return countryList.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence dd) {
                    countryList = temp;
                    String fd = dd.toString();
                    if (fd.isEmpty()) {
                        filterList = countryList;
                    } else {
                        List<BankCountryDataModel.Result> filteredList1 = new ArrayList<>();
                        for (BankCountryDataModel.Result row : countryList) {
                            if (row.getCountry().toLowerCase().contains(dd.toString().toLowerCase())) {
                                filteredList1.add(row);
                            }
                        }

                        filterList = filteredList1;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filterList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    countryList = (List<BankCountryDataModel.Result>) results.values;
                    notifyDataSetChanged();

                }
            };
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            BankCountryRowBinding binding;

            public ViewHolder(@NonNull BankCountryRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}