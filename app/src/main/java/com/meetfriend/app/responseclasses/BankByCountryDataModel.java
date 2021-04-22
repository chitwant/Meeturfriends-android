package com.meetfriend.app.responseclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BankByCountryDataModel {
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

    public class Detail {

        @SerializedName("values")
        @Expose
        private String values;
        @SerializedName("code_param")
        @Expose
        private String codeParam;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("title_example")
        @Expose
        private String titleExample;
        @SerializedName("title_limit_from")
        @Expose
        private String titleLimitFrom;
        @SerializedName("title_limit_to")
        @Expose
        private String titleLimitTo;

        public String getCodeParam() {
            return codeParam;
        }

        public void setCodeParam(String codeParam) {
            this.codeParam = codeParam;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleExample() {
            return titleExample;
        }

        public void setTitleExample(String titleExample) {
            this.titleExample = titleExample;
        }

        public String getTitleLimitFrom() {
            return titleLimitFrom;
        }

        public void setTitleLimitFrom(String titleLimitFrom) {
            this.titleLimitFrom = titleLimitFrom;
        }

        public String getTitleLimitTo() {
            return titleLimitTo;
        }

        public void setTitleLimitTo(String titleLimitTo) {
            this.titleLimitTo = titleLimitTo;
        }

        public String getValues() {
            return values;
        }

        public void setValues(String values) {
            this.values = values;
        }
    }

public class Result {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("country_code")
    @Expose
    private String countryCode;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("currency")
    @Expose
    private List<String> currency = null;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getCurrency() {
        return currency;
    }

    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

}
}
