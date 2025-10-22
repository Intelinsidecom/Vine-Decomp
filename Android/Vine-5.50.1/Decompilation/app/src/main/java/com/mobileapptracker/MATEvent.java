package com.mobileapptracker;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/* loaded from: classes.dex */
public class MATEvent implements Serializable {
    public static final String ACHIEVEMENT_UNLOCKED = "achievement_unlocked";
    public static final String ADDED_PAYMENT_INFO = "added_payment_info";
    public static final String ADD_TO_CART = "add_to_cart";
    public static final String ADD_TO_WISHLIST = "add_to_wishlist";
    public static final String CHECKOUT_INITIATED = "checkout_initiated";
    public static final String CONTENT_VIEW = "content_view";
    public static final String DEVICE_FORM_WEARABLE = "wearable";
    public static final String INVITE = "invite";
    public static final String LEVEL_ACHIEVED = "level_achieved";
    public static final String LOGIN = "login";
    public static final String PURCHASE = "purchase";
    public static final String RATED = "rated";
    public static final String REGISTRATION = "registration";
    public static final String RESERVATION = "reservation";
    public static final String SEARCH = "search";
    public static final String SHARE = "share";
    public static final String SPENT_CREDITS = "spent_credits";
    public static final String TUTORIAL_COMPLETE = "tutorial_complete";
    private static final long serialVersionUID = -7616393848331704848L;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String contentId;
    private String contentType;
    private String currencyCode;
    private Date date1;
    private Date date2;
    private String deviceForm;
    private int eventId;
    private List<MATEventItem> eventItems;
    private String eventName;
    private int level;
    private int quantity;
    private double rating;
    private String receiptData;
    private String receiptSignature;
    private String refId;
    private double revenue;
    private String searchString;

    public MATEvent(String eventName) {
        this.eventName = eventName;
    }

    public MATEvent(int eventId) {
        this.eventId = eventId;
    }

    public MATEvent withRevenue(double revenue) {
        this.revenue = revenue;
        return this;
    }

    public MATEvent withCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public MATEvent withAdvertiserRefId(String refId) {
        this.refId = refId;
        return this;
    }

    public MATEvent withEventItems(List<MATEventItem> items) {
        this.eventItems = items;
        return this;
    }

    public MATEvent withReceipt(String receiptData, String receiptSignature) {
        this.receiptData = receiptData;
        this.receiptSignature = receiptSignature;
        return this;
    }

    public MATEvent withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public MATEvent withContentId(String contentId) {
        this.contentId = contentId;
        return this;
    }

    public MATEvent withLevel(int level) {
        this.level = level;
        return this;
    }

    public MATEvent withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public MATEvent withSearchString(String searchString) {
        this.searchString = searchString;
        return this;
    }

    public MATEvent withRating(double rating) {
        this.rating = rating;
        return this;
    }

    public MATEvent withDate1(Date date1) {
        this.date1 = date1;
        return this;
    }

    public MATEvent withDate2(Date date2) {
        this.date2 = date2;
        return this;
    }

    public MATEvent withAttribute1(String attribute1) {
        this.attribute1 = attribute1;
        return this;
    }

    public MATEvent withAttribute2(String attribute2) {
        this.attribute2 = attribute2;
        return this;
    }

    public MATEvent withAttribute3(String attribute3) {
        this.attribute3 = attribute3;
        return this;
    }

    public MATEvent withAttribute4(String attribute4) {
        this.attribute4 = attribute4;
        return this;
    }

    public MATEvent withAttribute5(String attribute5) {
        this.attribute5 = attribute5;
        return this;
    }

    public MATEvent withDeviceForm(String deviceForm) {
        this.deviceForm = deviceForm;
        return this;
    }

    public String getEventName() {
        return this.eventName;
    }

    public int getEventId() {
        return this.eventId;
    }

    public double getRevenue() {
        return this.revenue;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getRefId() {
        return this.refId;
    }

    public List<MATEventItem> getEventItems() {
        return this.eventItems;
    }

    public String getReceiptData() {
        return this.receiptData;
    }

    public String getReceiptSignature() {
        return this.receiptSignature;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getContentId() {
        return this.contentId;
    }

    public int getLevel() {
        return this.level;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getSearchString() {
        return this.searchString;
    }

    public double getRating() {
        return this.rating;
    }

    public Date getDate1() {
        return this.date1;
    }

    public Date getDate2() {
        return this.date2;
    }

    public String getAttribute1() {
        return this.attribute1;
    }

    public String getAttribute2() {
        return this.attribute2;
    }

    public String getAttribute3() {
        return this.attribute3;
    }

    public String getAttribute4() {
        return this.attribute4;
    }

    public String getAttribute5() {
        return this.attribute5;
    }

    public String getDeviceForm() {
        return this.deviceForm;
    }
}
