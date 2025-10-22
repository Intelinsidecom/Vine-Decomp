package com.digits.sdk.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.digits.sdk.android.DigitsScribeConstants;
import io.fabric.sdk.android.services.common.CommonUtils;
import twitter4j.internal.http.HttpResponseCode;

/* loaded from: classes.dex */
class FailureActivityDelegateImpl {
    final Activity activity;
    final FailureController controller;
    final DigitsScribeService scribeService;

    public FailureActivityDelegateImpl(Activity activity) {
        this(activity, new FailureControllerImpl(), new FailureScribeService(Digits.getInstance().getScribeClient()));
    }

    public FailureActivityDelegateImpl(Activity activity, FailureController controller, DigitsScribeService scribeService) {
        this.activity = activity;
        this.controller = controller;
        this.scribeService = scribeService;
    }

    public void init() {
        this.scribeService.impression();
        Bundle bundle = this.activity.getIntent().getExtras();
        if (isBundleValid(bundle)) {
            setContentView();
            setUpViews();
            return;
        }
        throw new IllegalAccessError("This activity can only be started from Digits");
    }

    protected boolean isBundleValid(Bundle bundle) {
        return BundleManager.assertContains(bundle, "receiver");
    }

    protected void setContentView() {
        this.activity.setContentView(R.layout.dgts__activity_failure);
    }

    protected void setUpViews() {
        Button dismissButton = (Button) this.activity.findViewById(R.id.dgts__dismiss_button);
        TextView tryAnotherNumberButton = (TextView) this.activity.findViewById(R.id.dgts__try_another_phone);
        setUpDismissButton(dismissButton);
        setUpTryAnotherPhoneButton(tryAnotherNumberButton);
    }

    protected void setUpDismissButton(Button button) {
        button.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.FailureActivityDelegateImpl.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FailureActivityDelegateImpl.this.scribeService.click(DigitsScribeConstants.Element.DISMISS);
                CommonUtils.finishAffinity(FailureActivityDelegateImpl.this.activity, HttpResponseCode.OK);
                FailureActivityDelegateImpl.this.controller.sendFailure(FailureActivityDelegateImpl.this.getBundleResultReceiver(), FailureActivityDelegateImpl.this.getBundleException());
            }
        });
    }

    protected void setUpTryAnotherPhoneButton(TextView textView) {
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.digits.sdk.android.FailureActivityDelegateImpl.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                FailureActivityDelegateImpl.this.scribeService.click(DigitsScribeConstants.Element.RETRY);
                FailureActivityDelegateImpl.this.controller.tryAnotherNumber(FailureActivityDelegateImpl.this.activity, FailureActivityDelegateImpl.this.getBundleResultReceiver());
                FailureActivityDelegateImpl.this.activity.finish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ResultReceiver getBundleResultReceiver() {
        Bundle bundle = this.activity.getIntent().getExtras();
        return (ResultReceiver) bundle.getParcelable("receiver");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DigitsException getBundleException() {
        Bundle bundle = this.activity.getIntent().getExtras();
        return (DigitsException) bundle.getSerializable("fallback_reason");
    }
}
