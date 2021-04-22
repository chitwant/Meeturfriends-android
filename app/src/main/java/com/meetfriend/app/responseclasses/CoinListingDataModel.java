package com.meetfriend.app.responseclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinListingDataModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }


    public class Result {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("identifier")
        @Expose
        private String identifier;
        @SerializedName("plan_name")
        @Expose
        private String planName;
        @SerializedName("coins")
        @Expose
        private String coins;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("discount")
        @Expose
        private Double discount;
        @SerializedName("discount_coins")
        @Expose
        private Double discountCoins;
        @SerializedName("coins_with_discount")
        @Expose
        private Double coinsWithDiscount;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getCoins() {
            return coins;
        }

        public void setCoins(String coins) {
            this.coins = coins;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Double getDiscount() {
            return discount;
        }

        public void setDiscount(Double discount) {
            this.discount = discount;
        }

        public Double getDiscountCoins() {
            return discountCoins;
        }

        public void setDiscountCoins(Double discountCoins) {
            this.discountCoins = discountCoins;
        }

        public Double getCoinsWithDiscount() {
            return coinsWithDiscount;
        }

        public void setCoinsWithDiscount(Double coinsWithDiscount) {
            this.coinsWithDiscount = coinsWithDiscount;
        }

    }
}
