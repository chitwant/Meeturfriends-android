package com.meetfriend.app.responseclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftReceiveDataModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("result")
    @Expose
    private List<GiftTransactionDataModel.Result> result = null;

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

    public List<GiftTransactionDataModel.Result> getResult() {
        return result;
    }

    public void setResult(List<GiftTransactionDataModel.Result> result) {
        this.result = result;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }


    public class FromUser {

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
        @SerializedName("gender")
        @Expose
        private String gender;

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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

    }

    public class GiftGallery {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("coins")
        @Expose
        private Double coins;
        @SerializedName("file_path")
        @Expose
        private String filePath;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getCoins() {
            return coins;
        }

        public void setCoins(Double coins) {
            this.coins = coins;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

    }

    public class Result {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("from_id")
        @Expose
        private Integer fromId;
        @SerializedName("to_id")
        @Expose
        private Integer toId;
        @SerializedName("coins")
        @Expose
        private Double coins;
        @SerializedName("coins_type")
        @Expose
        private String coinsType;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("gift_id")
        @Expose
        private Integer giftId;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("gift_gallery")
        @Expose
        private GiftTransactionDataModel.GiftGallery giftGallery;
        @SerializedName("from_user")
        @Expose
        private GiftTransactionDataModel.FromUser fromUser;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getFromId() {
            return fromId;
        }

        public void setFromId(Integer fromId) {
            this.fromId = fromId;
        }

        public Integer getToId() {
            return toId;
        }

        public void setToId(Integer toId) {
            this.toId = toId;
        }

        public Double getCoins() {
            return coins;
        }

        public void setCoins(Double coins) {
            this.coins = coins;
        }

        public String getCoinsType() {
            return coinsType;
        }

        public void setCoinsType(String coinsType) {
            this.coinsType = coinsType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getGiftId() {
            return giftId;
        }

        public void setGiftId(Integer giftId) {
            this.giftId = giftId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public GiftTransactionDataModel.GiftGallery getGiftGallery() {
            return giftGallery;
        }

        public void setGiftGallery(GiftTransactionDataModel.GiftGallery giftGallery) {
            this.giftGallery = giftGallery;
        }

        public GiftTransactionDataModel.FromUser getFromUser() {
            return fromUser;
        }

        public void setFromUser(GiftTransactionDataModel.FromUser fromUser) {
            this.fromUser = fromUser;
        }

    }
}
