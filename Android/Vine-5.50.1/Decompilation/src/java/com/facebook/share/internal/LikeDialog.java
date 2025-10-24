package com.facebook.share.internal;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.facebook.FacebookCallback;
import com.facebook.internal.AppCall;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.internal.DialogFeature;
import com.facebook.internal.DialogPresenter;
import com.facebook.internal.FacebookDialogBase;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class LikeDialog extends FacebookDialogBase<LikeContent, Result> {
    private static final int DEFAULT_REQUEST_CODE = CallbackManagerImpl.RequestCodeOffset.Like.toRequestCode();

    public static final class Result {
        private final Bundle bundle;

        public Result(Bundle bundle) {
            this.bundle = bundle;
        }
    }

    public static boolean canShowNativeDialog() {
        return DialogPresenter.canPresentNativeDialogWithFeature(getFeature());
    }

    public static boolean canShowWebFallback() {
        return DialogPresenter.canPresentWebFallbackDialogWithFeature(getFeature());
    }

    public LikeDialog(Activity activity) {
        super(activity, DEFAULT_REQUEST_CODE);
    }

    public LikeDialog(Fragment fragment) {
        super(fragment, DEFAULT_REQUEST_CODE);
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected AppCall createBaseAppCall() {
        return new AppCall(getRequestCode());
    }

    @Override // com.facebook.internal.FacebookDialogBase
    protected List<FacebookDialogBase<LikeContent, Result>.ModeHandler> getOrderedModeHandlers() {
        AnonymousClass1 anonymousClass1 = null;
        ArrayList<FacebookDialogBase<LikeContent, Result>.ModeHandler> handlers = new ArrayList<>();
        handlers.add(new NativeHandler(this, anonymousClass1));
        handlers.add(new WebFallbackHandler(this, anonymousClass1));
        return handlers;
    }

    /* renamed from: com.facebook.share.internal.LikeDialog$1, reason: invalid class name */
    class AnonymousClass1 extends ResultProcessor {
        final /* synthetic */ FacebookCallback val$callback;

        @Override // com.facebook.share.internal.ResultProcessor
        public void onSuccess(AppCall appCall, Bundle results) {
            this.val$callback.onSuccess(new Result(results));
        }
    }

    private class NativeHandler extends FacebookDialogBase<LikeContent, Result>.ModeHandler {
        private NativeHandler() {
            super();
        }

        /* synthetic */ NativeHandler(LikeDialog x0, AnonymousClass1 x1) {
            this();
        }

        public boolean canShow(LikeContent content) {
            return content != null && LikeDialog.canShowNativeDialog();
        }

        public AppCall createAppCall(final LikeContent content) {
            AppCall appCall = LikeDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForNativeDialog(appCall, new DialogPresenter.ParameterProvider() { // from class: com.facebook.share.internal.LikeDialog.NativeHandler.1
                @Override // com.facebook.internal.DialogPresenter.ParameterProvider
                public Bundle getParameters() {
                    return LikeDialog.createParameters(content);
                }

                @Override // com.facebook.internal.DialogPresenter.ParameterProvider
                public Bundle getLegacyParameters() {
                    Log.e("LikeDialog", "Attempting to present the Like Dialog with an outdated Facebook app on the device");
                    return new Bundle();
                }
            }, LikeDialog.getFeature());
            return appCall;
        }
    }

    private class WebFallbackHandler extends FacebookDialogBase<LikeContent, Result>.ModeHandler {
        private WebFallbackHandler() {
            super();
        }

        /* synthetic */ WebFallbackHandler(LikeDialog x0, AnonymousClass1 x1) {
            this();
        }

        public boolean canShow(LikeContent content) {
            return content != null && LikeDialog.canShowWebFallback();
        }

        public AppCall createAppCall(LikeContent content) throws PackageManager.NameNotFoundException, NoSuchAlgorithmException {
            AppCall appCall = LikeDialog.this.createBaseAppCall();
            DialogPresenter.setupAppCallForWebFallbackDialog(appCall, LikeDialog.createParameters(content), LikeDialog.getFeature());
            return appCall;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DialogFeature getFeature() {
        return LikeDialogFeature.LIKE_DIALOG;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Bundle createParameters(LikeContent likeContent) {
        Bundle params = new Bundle();
        params.putString("object_id", likeContent.getObjectId());
        params.putString("object_type", likeContent.getObjectType());
        return params;
    }
}
