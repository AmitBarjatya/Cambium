package com.amit.cambium.models;

import com.amit.cambium.utils.DateUtils;

import org.json.JSONObject;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Model class for a Project element fetched from server
 * Extends RealmObject for saving in local realm database
 * <p>
 * JSON representation for this object:
 * {
 * s.no: 6,
 * amt.pledged: 577844,
 * blurb: "Polygons is the origami-like measuring ...",
 * by: "Polygons Design",
 * country: "US",
 * currency: "usd",
 * end.time: "2016-11-20T11:57:34-05:00",
 * location: "Wilmington, DE",
 * percentage.funded: 5778,
 * num.backers: "74405",
 * state: "DE",
 * title: "Polygons | The Flat 4-in-1 Measuring Spoon",
 * type: "Town",
 * url: "/projects/stillalive/polygons-the-flat-4-in-1-measuring-spoon?ref=discovery"
 * }
 * <p>
 * Created by Amit Barjatya on 10/29/17.
 */

public class Project extends RealmObject {

    @PrimaryKey
    private long serialNumber;
    private long amtPledged;
    private String blurb;
    private String by;
    private String country;
    private String currency;
    private Date endTime;
    private String location;
    private long percentageFunded;  // 5778 ==> 57.78%
    private long numBackers;
    private String state;
    private String title;
    private String type;
    private String url;

    /**
     * Create a new Project object from its json representation
     *
     * @param object the json representation
     */
    public static Project fromJSONObject(JSONObject object) {
        Project project = new Project();
        project.setSerialNumber(object.optLong("s.no"));
        project.setAmtPledged(object.optLong("amt.pledged"));
        project.setBlurb(object.optString("blurb"));
        project.setBy(object.optString("by"));
        project.setCountry(object.optString("country"));
        project.setCurrency(object.optString("currency"));
        project.setEndTime(DateUtils.from(object.optString("end.time")));
        project.setLocation(object.optString("location"));
        project.setPercentageFunded(object.optLong("percentage.funded"));
        project.setNumBackers(object.optLong("num.backers"));
        project.setState(object.optString("state"));
        project.setTitle(object.optString("title"));
        project.setType(object.optString("type"));
        project.setUrl(object.optString("url"));

        return project;
    }


    public long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public long getAmtPledged() {
        return amtPledged;
    }

    public void setAmtPledged(long amtPledged) {
        this.amtPledged = amtPledged;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getPercentageFunded() {
        return percentageFunded;
    }

    public void setPercentageFunded(long percentageFunded) {
        this.percentageFunded = percentageFunded;
    }

    public long getNumBackers() {
        return numBackers;
    }

    public void setNumBackers(long numBackers) {
        this.numBackers = numBackers;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
