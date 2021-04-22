package com.meetfriend.app.ui.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.meetfriend.app.R;
import com.meetfriend.app.databinding.ActivityCoinPlanPriceBinding;
import com.meetfriend.app.databinding.CoinPriceListRowBinding;
import com.meetfriend.app.responseclasses.CoinListingDataModel;
import com.meetfriend.app.responseclasses.CommonResponseClass;
import com.meetfriend.app.responseclasses.MyEarningDataModel;
import com.meetfriend.app.utilclasses.CallProgressWheel;
import com.meetfriend.app.utilclasses.PurchaseHelper;
import com.meetfriend.app.utilclasses.UtilsClass;
import com.meetfriend.app.viewmodal.GiftViewModel;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import contractorssmart.app.utilsclasses.CommonMethods;
import contractorssmart.app.utilsclasses.PreferenceHandler;
import vas.com.currencyconverter.CurrencyConverter;

public class CoinPlanPriceActivity extends AppCompatActivity {
    private static String symbol = "";
    private static String CurrencyCode = "";
    Context mContext;
    GiftViewModel giftViewModel;
    ActivityCoinPlanPriceBinding binding;
    ArrayList<HashMap<String, String>> list;
    private Double totalCoins = 0.0;
    PurchaseHelper purchaseHelper;
    boolean isPurchaseQueryPending;
    private int index;

    public  void displayCurrencyInfoForLocale(Locale locale) {
//        System.out.println("Locale: " + locale.getDisplayName());
        Log.e("address",(PreferenceHandler.INSTANCE.readString(mContext,"countryCode","")));
//        Currency currency = Currency.getInstance(PreferenceHandler.INSTANCE.readString(mContext,"countryCode","INR"));
//        System.out.println("Currency Code: " + currency.getCurrencyCode());
//        System.out.println("Symbol: " + currency.getSymbol());
//        System.out.println("Default Fraction Digits: " + currency.getDefaultFractionDigits());
//        System.out.println();
        symbol = Currency.getInstance(new Locale("", PreferenceHandler.INSTANCE.readString(mContext, "countryCode", ""))).getSymbol();
       CurrencyCode = Currency.getInstance(new Locale("", PreferenceHandler.INSTANCE.readString(mContext, "countryCode", ""))).getCurrencyCode();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coin_plan_price);
        mContext = this;
        Locale defaultLocale = Locale.getDefault();
        displayCurrencyInfoForLocale(defaultLocale);
        initViews();
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
        purchaseHelper = new PurchaseHelper(this, getPurchaseHelperListener());

        myEarninginitializeObservers();
        coinPurchaseinitializeObservers();
        coinListinginitializeObservers();
        if (purchaseHelper != null && purchaseHelper.isServiceConnected())
            purchaseHelper.getPurchasedItems(BillingClient.SkuType.INAPP);
        else
            isPurchaseQueryPending = true;
    }

    public PurchaseHelper.PurchaseHelperListener getPurchaseHelperListener() {
        return new PurchaseHelper.PurchaseHelperListener() {
            @Override
            public void onServiceConnected(int resultCode) {

                if (isPurchaseQueryPending) {
                    purchaseHelper.getPurchasedItems(BillingClient.SkuType.INAPP);
                    isPurchaseQueryPending = false;
                }

            }

            @Override
            public void onSkuQueryResponse(List<SkuDetails> skuDetails) {
            }

            @Override
            public void onPurchasehistoryResponse(List<Purchase> purchasedItems) {

            }

            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

//                Log.d(TAG, "onPurchasesUpdated: " + responseCode);
                if (responseCode == BillingClient.BillingResponse.OK && purchases != null)
                {

                    HashMap<String, Object> mHashMap = new HashMap<>();
                    mHashMap.put("coins",(Double.parseDouble( list.get(index).get("coin"))+Double.parseDouble(list.get(index).get("discount_coin")))+"");
                    mHashMap.put("transaction_id",purchases.get(0).getOrderId() );
                    mHashMap.put("plan_name", list.get(index).get("id"));
                    mHashMap.put("price", list.get(index).get("price"));
                    giftViewModel.coinPurchase(mHashMap);
                    CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
//                    int itemIndex = getCityItemPosition(purchases.get(0).getSku(),placeData);

//                    if (itemIndex >= 0)
//                    {
//                        placeData.get(itemIndex).setIs_purchase(true);
//                        cityListingAdapter.placeData = placeData;
//                        cityListingAdapter.notifyDataSetChanged();
//                    }

                }


            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyEarning();
        UtilsClass.updateUserStatus(mContext, true);
    }

    private void coinPurchaseinitializeObservers() {
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
        giftViewModel.getCoinPurchaseResponse().observe(this, favsObserver);
    }

    private void coinListinginitializeObservers() {
        final Observer<CoinListingDataModel> favsObserver = new Observer<CoinListingDataModel>() {
            @Override
            public void onChanged(CoinListingDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    list = new ArrayList<>();
                    HashMap<String, String> map;
                    if ( response.getResult().get(0).getPlanName().equalsIgnoreCase("new user")){
                        for (int i = 0; i < response.getResult().size(); i++) {
                            map = new HashMap<>();
                            map.put("id", response.getResult().get(i).getId().toString());
                            map.put("coin", response.getResult().get(i).getCoins().toString());
                            map.put("discount_coin", response.getResult().get(i).getDiscountCoins().toString());
                            map.put("discount", response.getResult().get(i).getDiscount().toString());
                            if (i == 1) {
                                map.put("color", "#6244B6");
                                map.put("price", "4.99");
                                map.put("id","basic_02");
                            } else if (i == 2) {
                                map.put("color", "#DD5859");
                                map.put("price", "6.99");
                                map.put("id","silver_03");
                            } else if (i == 3) {
                                map.put("color", "#3F80F8");
                                map.put("price", "14.99");
                                map.put("id","gold_04");
                            } else if (i == 4) {
                                map.put("color", "#4F8A54");
                                map.put("price", "24.99");
                                map.put("id","platinum_05");
                            } else if (i == 5) {
                                map.put("color", "#82433C");
                                map.put("price", "49.99");
                                map.put("id","best_value_06");
                            } else if (i == 6) {
                                map.put("color", "#6244B6");
                                map.put("price", "99.99");
                                map.put("id","best_offer_07");
                            } else if (i == 0) {
                                map.put("color", "#DD5859");
                                map.put("price", "1.99");
                                map.put("id","new_01");
                            }
                            list.add(map);
                        }
                    }else {
                        for (int i = 0; i < response.getResult().size(); i++) {
                            map = new HashMap<>();
                            map.put("coin", response.getResult().get(i).getCoins().toString());
                            map.put("discount_coin", response.getResult().get(i).getDiscountCoins().toString());
                            map.put("discount", response.getResult().get(i).getDiscount().toString());

                            if (i == 0) {
                                map.put("color", "#6244B6");
                                map.put("price", "4.99");
                                map.put("id","basic_02");
                            } else if (i == 1) {
                                map.put("color", "#DD5859");
                                map.put("price", "6.99");
                                map.put("id","silver_03");
                            } else if (i == 2) {
                                map.put("color", "#3F80F8");
                                map.put("price", "14.99");
                                map.put("id","gold_04");
                            } else if (i == 3) {
                                map.put("color", "#4F8A54");
                                map.put("price", "24.99");
                                map.put("id","platinum_05");
                            } else if (i == 4) {
                                map.put("color", "#82433C");
                                map.put("price", "49.99");
                                map.put("id","best_value_06");
                            } else if (i == 5) {
                                map.put("color", "#6244B6");
                                map.put("price", "99.99");
                                map.put("id","best_offer_07");
                            }
                            list.add(map);
                        }
                    }
                    binding.mRecyclerView.setAdapter(new CoinPriceListAdapter(mContext, list));
                } else {
                }
            }
        };
        giftViewModel.getCoinListResponse().observe(this, favsObserver);
    }

    private void getMyEarning() {
        giftViewModel.myearning();
        giftViewModel.getCoinList();

        CallProgressWheel.INSTANCE.showLoadingDialog(mContext);
    }

    private void myEarninginitializeObservers() {
        final Observer<MyEarningDataModel> favsObserver = new Observer<MyEarningDataModel>() {
            @Override
            public void onChanged(MyEarningDataModel response) {
                CallProgressWheel.INSTANCE.dismissLoadingDialog();

                if (response.getStatus() == true) {
                    totalCoins = response.getResult().getTotalPurchasedCoins();
                    binding.mAvailableCoins.setText("My Coins: " + String.format("%.2f", response.getResult().getTotalPurchasedCoins()));
                }
            }
        };
        giftViewModel.getMyEarningResponse().observe(this, favsObserver);
    }

    private class CoinPriceListAdapter extends RecyclerView.Adapter<CoinPriceListAdapter.ViewHolder> {
        Context mContext;
        ArrayList<HashMap<String, String>> list;

        public CoinPriceListAdapter(Context mContext, ArrayList<HashMap<String, String>> list) {
            this.mContext = mContext;
            this.list = list;
        }

        @NonNull
        @Override
        public CoinPriceListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            CoinPriceListRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.coin_price_list_row, parent, false);
            return new ViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull CoinPriceListAdapter.ViewHolder holder, int position) {
            holder.binding.mCoin.setText(list.get(position).get("coin"));
            if (list.get(position).get("discount").equalsIgnoreCase("0") || list.get(position).get("discount").equalsIgnoreCase("")) {
                holder.binding.mDiscount.setVisibility(View.GONE);
                holder.binding.mDiscountCount.setVisibility(View.GONE);
            } else {
                holder.binding.mDiscount.setText(list.get(position).get("discount") + "% OFF");
                holder.binding.mDiscountCount.setText("+" + list.get(position).get("discount_coin"));
            }
            CurrencyConverter.calculate( Double.parseDouble(list.get(position).get("price")), "USD", CurrencyCode, new CurrencyConverter.Callback() {
                @Override
                public void onValueCalculated(Double value, Exception e) {
                    if (e != null) {
                        CurrencyConverter.calculate( Double.parseDouble(list.get(position).get("price")), "USD", CurrencyCode, new CurrencyConverter.Callback() {
                            @Override
                            public void onValueCalculated(Double value, Exception e) {
                                if (e != null) {
//                        Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                                    holder.binding.mPrice.setText(symbol+Double.parseDouble(list.get(position).get("price")));
                                }else{
                                    holder.binding.mPrice.setText(CurrencyConverter.formatCurrencyValue(CurrencyCode, value));
                                }
                            }
                        });
//                        Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                        holder.binding.mPrice.setText(symbol+Double.parseDouble(list.get(position).get("price")));
                    }else{
                        holder.binding.mPrice.setText(CurrencyConverter.formatCurrencyValue(CurrencyCode, value));
                    }
                }
            });
            holder.binding.mRow.setBackgroundColor(Color.parseColor(list.get(position).get("color")));
            holder.binding.mRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   index=position;
                    purchaseHelper.launchBillingFLow(BillingClient.SkuType.INAPP, list.get(position).get("id"));
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CoinPriceListRowBinding binding;

            public ViewHolder(@NonNull CoinPriceListRowBinding itemView) {
                super(itemView.getRoot());
                binding = itemView;
            }
        }
    }
}