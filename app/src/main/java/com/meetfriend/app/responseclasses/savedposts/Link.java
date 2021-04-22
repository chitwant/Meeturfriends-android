
package com.meetfriend.app.responseclasses.savedposts;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Link {

    @Expose
    private Boolean active;
    @Expose
    private String label;
    @Expose
    private Object url;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

}
