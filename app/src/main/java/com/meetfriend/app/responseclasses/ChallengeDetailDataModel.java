package com.meetfriend.app.responseclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChallengeDetailDataModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("base_url")
    @Expose
    private String baseUrl;
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

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }



public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("privacy")
    @Expose
    private Integer privacy;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("file_path")
    @Expose
    private String filePath;
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("time_from")
    @Expose
    private String timeFrom;
    @SerializedName("date_from")
    @Expose
    private String dateFrom;
    @SerializedName("time_to")
    @Expose
    private String timeTo;
    @SerializedName("date_to")
    @Expose
    private String dateTo;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("by_admin")
    @Expose
    private Integer byAdmin;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("challenge_country")
    @Expose
    private List<ChallengeCountry> challengeCountry = null;
    @SerializedName("challenge_state")
    @Expose
    private List<ChallengeState> challengeState = null;
    @SerializedName("challenge_city")
    @Expose
    private List<ChallengeCity> challengeCity = null;
    @SerializedName("challenge_reactions")
    @Expose
    private ChallengeReactions challengeReactions;
    @SerializedName("challenge_posts")
    @Expose
    private List<ChallengePost> challengePosts = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrivacy() {
        return privacy;
    }

    public void setPrivacy(Integer privacy) {
        this.privacy = privacy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getByAdmin() {
        return byAdmin;
    }

    public void setByAdmin(Integer byAdmin) {
        this.byAdmin = byAdmin;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<ChallengeCountry> getChallengeCountry() {
        return challengeCountry;
    }

    public void setChallengeCountry(List<ChallengeCountry> challengeCountry) {
        this.challengeCountry = challengeCountry;
    }

    public List<ChallengeState> getChallengeState() {
        return challengeState;
    }

    public void setChallengeState(List<ChallengeState> challengeState) {
        this.challengeState = challengeState;
    }

    public List<ChallengeCity> getChallengeCity() {
        return challengeCity;
    }

    public void setChallengeCity(List<ChallengeCity> challengeCity) {
        this.challengeCity = challengeCity;
    }

    public ChallengeReactions getChallengeReactions() {
        return challengeReactions;
    }

    public void setChallengeReactions(ChallengeReactions challengeReactions) {
        this.challengeReactions = challengeReactions;
    }

    public List<ChallengePost> getChallengePosts() {
        return challengePosts;
    }

    public void setChallengePosts(List<ChallengePost> challengePosts) {
        this.challengePosts = challengePosts;
    }

}

public class StateData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("status")
    @Expose
    private Integer status;

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

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

public class User_ {

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
    public class ChallengeCity {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("challenge_id")
        @Expose
        private Integer challengeId;
        @SerializedName("city_id")
        @Expose
        private Integer cityId;
        @SerializedName("city_data")
        @Expose
        private CityData cityData;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getChallengeId() {
            return challengeId;
        }

        public void setChallengeId(Integer challengeId) {
            this.challengeId = challengeId;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }

        public CityData getCityData() {
            return cityData;
        }

        public void setCityData(CityData cityData) {
            this.cityData = cityData;
        }

    }

    public class ChallengeCountry {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("challenge_id")
        @Expose
        private Integer challengeId;
        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("country_data")
        @Expose
        private CountryData countryData;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getChallengeId() {
            return challengeId;
        }

        public void setChallengeId(Integer challengeId) {
            this.challengeId = challengeId;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public CountryData getCountryData() {
            return countryData;
        }

        public void setCountryData(CountryData countryData) {
            this.countryData = countryData;
        }

    }

    public class ChallengePost {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("challenge_id")
        @Expose
        private Integer challengeId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("file_path")
        @Expose
        private String filePath;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("no_of_likes_count")
        @Expose
        private Integer noOfLikesCount;
        @SerializedName("no_of_views_count")
        @Expose
        private Integer noOfViewsCount;
        @SerializedName("is_liked_count")
        @Expose
        private Integer isLikedCount;
        @SerializedName("user")
        @Expose
        private User_ user;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getChallengeId() {
            return challengeId;
        }

        public void setChallengeId(Integer challengeId) {
            this.challengeId = challengeId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getNoOfLikesCount() {
            return noOfLikesCount;
        }

        public void setNoOfLikesCount(Integer noOfLikesCount) {
            this.noOfLikesCount = noOfLikesCount;
        }

        public Integer getNoOfViewsCount() {
            return noOfViewsCount;
        }

        public void setNoOfViewsCount(Integer noOfViewsCount) {
            this.noOfViewsCount = noOfViewsCount;
        }

        public Integer getIsLikedCount() {
            return isLikedCount;
        }

        public void setIsLikedCount(Integer isLikedCount) {
            this.isLikedCount = isLikedCount;
        }

        public User_ getUser() {
            return user;
        }

        public void setUser(User_ user) {
            this.user = user;
        }

    }

    public class ChallengeReactions {

        @SerializedName("challenge_id")
        @Expose
        private Integer challengeId;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("status")
        @Expose
        private Integer status;

        public Integer getChallengeId() {
            return challengeId;
        }

        public void setChallengeId(Integer challengeId) {
            this.challengeId = challengeId;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }

    public class ChallengeState {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("challenge_id")
        @Expose
        private Integer challengeId;
        @SerializedName("state_id")
        @Expose
        private Integer stateId;
        @SerializedName("state_data")
        @Expose
        private StateData stateData;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getChallengeId() {
            return challengeId;
        }

        public void setChallengeId(Integer challengeId) {
            this.challengeId = challengeId;
        }

        public Integer getStateId() {
            return stateId;
        }

        public void setStateId(Integer stateId) {
            this.stateId = stateId;
        }

        public StateData getStateData() {
            return stateData;
        }

        public void setStateData(StateData stateData) {
            this.stateData = stateData;
        }

    }

    public class CityData {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("state_id")
        @Expose
        private Integer stateId;
        @SerializedName("status")
        @Expose
        private Integer status;

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

        public Integer getStateId() {
            return stateId;
        }

        public void setStateId(Integer stateId) {
            this.stateId = stateId;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }

    public class CountryData {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sortname")
        @Expose
        private String sortname;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("phonecode")
        @Expose
        private Integer phonecode;
        @SerializedName("status")
        @Expose
        private Integer status;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSortname() {
            return sortname;
        }

        public void setSortname(String sortname) {
            this.sortname = sortname;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPhonecode() {
            return phonecode;
        }

        public void setPhonecode(Integer phonecode) {
            this.phonecode = phonecode;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

    }
}
