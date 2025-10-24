package com.mobileapptracker;

import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class MATEventItem {
    public String attribute_sub1;
    public String attribute_sub2;
    public String attribute_sub3;
    public String attribute_sub4;
    public String attribute_sub5;
    public String itemname;
    public int quantity;
    public double revenue;
    public double unitPrice;

    public MATEventItem(String itemname) {
        this.itemname = itemname;
    }

    public MATEventItem withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public MATEventItem withUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public MATEventItem withRevenue(double revenue) {
        this.revenue = revenue;
        return this;
    }

    public MATEventItem withAttribute1(String attribute) {
        this.attribute_sub1 = attribute;
        return this;
    }

    public MATEventItem withAttribute2(String attribute) {
        this.attribute_sub2 = attribute;
        return this;
    }

    public MATEventItem withAttribute3(String attribute) {
        this.attribute_sub3 = attribute;
        return this;
    }

    public MATEventItem withAttribute4(String attribute) {
        this.attribute_sub4 = attribute;
        return this;
    }

    public MATEventItem withAttribute5(String attribute) {
        this.attribute_sub5 = attribute;
        return this;
    }

    public JSONObject toJSON() {
        HashMap<String, String> mapValues = new HashMap<>();
        if (this.itemname != null) {
            mapValues.put("item", this.itemname);
        }
        mapValues.put("quantity", Integer.toString(this.quantity));
        mapValues.put("unit_price", Double.toString(this.unitPrice));
        if (this.revenue != 0.0d) {
            mapValues.put("revenue", Double.toString(this.revenue));
        }
        if (this.attribute_sub1 != null) {
            mapValues.put("attribute_sub1", this.attribute_sub1);
        }
        if (this.attribute_sub2 != null) {
            mapValues.put("attribute_sub2", this.attribute_sub2);
        }
        if (this.attribute_sub3 != null) {
            mapValues.put("attribute_sub3", this.attribute_sub3);
        }
        if (this.attribute_sub4 != null) {
            mapValues.put("attribute_sub4", this.attribute_sub4);
        }
        if (this.attribute_sub5 != null) {
            mapValues.put("attribute_sub5", this.attribute_sub5);
        }
        return new JSONObject(mapValues);
    }

    public String getAttrStringByName(String name) {
        if (name.equals("itemname")) {
            return this.itemname;
        }
        if (name.equals("quantity")) {
            return Integer.toString(this.quantity);
        }
        if (name.equals("unitPrice")) {
            return Double.toString(this.unitPrice);
        }
        if (name.equals("revenue")) {
            return Double.toString(this.revenue);
        }
        if (name.equals("attribute_sub1")) {
            return this.attribute_sub1;
        }
        if (name.equals("attribute_sub2")) {
            return this.attribute_sub2;
        }
        if (name.equals("attribute_sub3")) {
            return this.attribute_sub3;
        }
        if (name.equals("attribute_sub4")) {
            return this.attribute_sub4;
        }
        if (name.equals("attribute_sub5")) {
            return this.attribute_sub5;
        }
        return null;
    }
}
