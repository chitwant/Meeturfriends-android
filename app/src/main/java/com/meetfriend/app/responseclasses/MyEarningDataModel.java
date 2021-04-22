package com.meetfriend.app.responseclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyEarningDataModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private Result result;

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

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }


    public class Result {

        @SerializedName("total_purchased_coins")
        @Expose
        private Double totalPurchasedCoins;
        @SerializedName("redeem_coins_limit")
        @Expose
        private String redeemCoinsLimit;
        @SerializedName("total_gift_recieved_coins")
        @Expose
        private Double totalGiftRecievedCoins;
        @SerializedName("total_gift_recieved_unpaid_coins")
        @Expose
        private Double totalGiftRecievedUnpaidCoins;
        @SerializedName("total_gift_send_coins")
        @Expose
        private Double totalGiftSendCoins;
        @SerializedName("total_current_coins")
        @Expose
        private Double totalCurrentCoins;
        @SerializedName("user")
        @Expose
        private User user;

        public Double getTotalPurchasedCoins() {
            return totalPurchasedCoins;
        }

        public void setTotalPurchasedCoins(Double totalPurchasedCoins) {
            this.totalPurchasedCoins = totalPurchasedCoins;
        }

        public Double getTotalGiftRecievedCoins() {
            return totalGiftRecievedCoins;
        }

        public void setTotalGiftRecievedCoins(Double totalGiftRecievedCoins) {
            this.totalGiftRecievedCoins = totalGiftRecievedCoins;
        }

        public Double getTotalGiftSendCoins() {
            return totalGiftSendCoins;
        }

        public void setTotalGiftSendCoins(Double totalGiftSendCoins) {
            this.totalGiftSendCoins = totalGiftSendCoins;
        }

        public Double getTotalCurrentCoins() {
            return totalCurrentCoins;
        }

        public void setTotalCurrentCoins(Double totalCurrentCoins) {
            this.totalCurrentCoins = totalCurrentCoins;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getRedeemCoinsLimit() {
            return redeemCoinsLimit;
        }

        public void setRedeemCoinsLimit(String redeemCoinsLimit) {
            this.redeemCoinsLimit = redeemCoinsLimit;
        }

        public Double getTotalGiftRecievedUnpaidCoins() {
            return totalGiftRecievedUnpaidCoins;
        }

        public void setTotalGiftRecievedUnpaidCoins(Double totalGiftRecievedUnpaidCoins) {
            this.totalGiftRecievedUnpaidCoins = totalGiftRecievedUnpaidCoins;
        }
    }

    public class User {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("firstName")
        @Expose
        private String firstName;
        @SerializedName("lastName")
        @Expose
        private String lastName;
        @SerializedName("profile_photo")
        @Expose
        private String profilePhoto;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

    }
}
