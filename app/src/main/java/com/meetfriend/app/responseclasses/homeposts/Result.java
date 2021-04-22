
package com.meetfriend.app.responseclasses.homeposts;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Result {

    @SerializedName("current_page")
    private Long mCurrentPage;
    @SerializedName("data")
    private ArrayList<Datum> mData;
    @SerializedName("first_page_url")
    private String mFirstPageUrl;
    @SerializedName("from")
    private Long mFrom;
    @SerializedName("last_page")
    private Long mLastPage;
    @SerializedName("last_page_url")
    private String mLastPageUrl;
    @SerializedName("links")
    private ArrayList<Link> mLinks;
    @SerializedName("next_page_url")
    private Object mNextPageUrl;
    @SerializedName("path")
    private String mPath;
    @SerializedName("per_page")
    private String mPerPage;
    @SerializedName("prev_page_url")
    private Object mPrevPageUrl;
    @SerializedName("to")
    private Long mTo;
    @SerializedName("total")
    private Long mTotal;

    public Long getCurrentPage() {
        return mCurrentPage;
    }

    public void setCurrentPage(Long currentPage) {
        mCurrentPage = currentPage;
    }

    public ArrayList<Datum> getData() {
        return mData;
    }

    public void setData(ArrayList<Datum> data) {
        mData = data;
    }

    public String getFirstPageUrl() {
        return mFirstPageUrl;
    }

    public void setFirstPageUrl(String firstPageUrl) {
        mFirstPageUrl = firstPageUrl;
    }

    public Long getFrom() {
        return mFrom;
    }

    public void setFrom(Long from) {
        mFrom = from;
    }

    public Long getLastPage() {
        return mLastPage;
    }

    public void setLastPage(Long lastPage) {
        mLastPage = lastPage;
    }

    public String getLastPageUrl() {
        return mLastPageUrl;
    }

    public void setLastPageUrl(String lastPageUrl) {
        mLastPageUrl = lastPageUrl;
    }

    public ArrayList<Link> getLinks() {
        return mLinks;
    }

    public void setLinks(ArrayList<Link> links) {
        mLinks = links;
    }

    public Object getNextPageUrl() {
        return mNextPageUrl;
    }

    public void setNextPageUrl(Object nextPageUrl) {
        mNextPageUrl = nextPageUrl;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getPerPage() {
        return mPerPage;
    }

    public void setPerPage(String perPage) {
        mPerPage = perPage;
    }

    public Object getPrevPageUrl() {
        return mPrevPageUrl;
    }

    public void setPrevPageUrl(Object prevPageUrl) {
        mPrevPageUrl = prevPageUrl;
    }

    public Long getTo() {
        return mTo;
    }

    public void setTo(Long to) {
        mTo = to;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
