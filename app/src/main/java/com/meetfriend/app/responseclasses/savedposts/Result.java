
package com.meetfriend.app.responseclasses.savedposts;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Result {

    @SerializedName("current_page")
    private Long currentPage;
    @Expose
    private ArrayList<Datum> data;
    @SerializedName("first_page_url")
    private String firstPageUrl;
    @Expose
    private Long from;
    @SerializedName("last_page")
    private Long lastPage;
    @SerializedName("last_page_url")
    private String lastPageUrl;
    @Expose
    private ArrayList<Link> links;
    @SerializedName("next_page_url")
    private Object nextPageUrl;
    @Expose
    private String path;
    @SerializedName("per_page")
    private String perPage;
    @SerializedName("prev_page_url")
    private Object prevPageUrl;
    @Expose
    private Long to;
    @Expose
    private Long total;

    public Long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Long currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    public String getFirstPageUrl() {
        return firstPageUrl;
    }

    public void setFirstPageUrl(String firstPageUrl) {
        this.firstPageUrl = firstPageUrl;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getLastPage() {
        return lastPage;
    }

    public void setLastPage(Long lastPage) {
        this.lastPage = lastPage;
    }

    public String getLastPageUrl() {
        return lastPageUrl;
    }

    public void setLastPageUrl(String lastPageUrl) {
        this.lastPageUrl = lastPageUrl;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public Object getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(Object nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPerPage() {
        return perPage;
    }

    public void setPerPage(String perPage) {
        this.perPage = perPage;
    }

    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    public void setPrevPageUrl(Object prevPageUrl) {
        this.prevPageUrl = prevPageUrl;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

}
