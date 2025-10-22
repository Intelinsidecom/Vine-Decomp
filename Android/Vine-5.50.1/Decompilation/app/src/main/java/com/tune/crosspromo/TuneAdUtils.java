package com.tune.crosspromo;

import android.app.Service;
import android.content.Context;
import android.util.Log;
import com.mobileapptracker.MATParameters;
import com.mobileapptracker.MobileAppTracker;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
class TuneAdUtils {
    private static TuneAdUtils INSTANCE;
    protected boolean isInitialized = false;
    private Context mAdContext;
    private ExecutorService mAdThreadExecutor;
    private Context mContext;
    private ExecutorService mLogThreadExecutor;
    private MATParameters mParams;
    private HashMap<String, TuneAdViewSet> mPlacementMap;

    TuneAdUtils() {
    }

    public static TuneAdUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new TuneAdUtils();
        }
        return INSTANCE;
    }

    protected void init(Context context, String advertiserId, String conversionKey) {
        if (context instanceof Service) {
            throw new RuntimeException("Context cannot be a Service");
        }
        if (!this.isInitialized) {
            if (advertiserId != null && conversionKey != null) {
                MobileAppTracker.init(context, advertiserId, conversionKey);
            }
            this.mParams = MATParameters.getInstance();
            if (this.mParams == null) {
                Log.e("TUNE", "Tune was not initialized before ads were called");
                throw new NullPointerException();
            }
            this.mContext = context.getApplicationContext();
            this.mAdThreadExecutor = Executors.newSingleThreadExecutor();
            this.mLogThreadExecutor = Executors.newCachedThreadPool();
            this.mPlacementMap = new HashMap<>();
            TuneAdClient.init(this.mParams.getAdvertiserId());
            this.isInitialized = true;
        }
    }

    protected void setAdContext(Context context) {
        this.mAdContext = context;
    }

    protected Context getContext() {
        return this.mContext;
    }

    protected ExecutorService getLogThread() {
        return this.mLogThreadExecutor;
    }

    protected TuneAdViewSet getViewSet(String placement) {
        return this.mPlacementMap.get(placement);
    }

    protected TuneAdView getPreviousView(String placement) {
        return getViewSet(placement).getPreviousView();
    }
}
