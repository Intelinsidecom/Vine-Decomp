package co.vine.android.service.components.recommendations;

import android.os.Bundle;
import co.vine.android.client.AppController;
import co.vine.android.service.VineServiceActionMapProvider;
import co.vine.android.service.components.NotifiableComponent;

/* loaded from: classes.dex */
public class RecommendationsComponent extends NotifiableComponent<Actions, RecommendationsListener> {

    public enum Actions {
        POST_FEEDBACK,
        USER_FEEDBACK
    }

    @Override // co.vine.android.service.components.VineServiceComponent
    public void registerActions(VineServiceActionMapProvider.Builder builder) {
        registerAsActionCode(builder, Actions.POST_FEEDBACK, new PostFeedbackAction(), new PostFeedbackActionNotifier(this.mListeners));
        registerAsActionCode(builder, Actions.USER_FEEDBACK, new UserFeedbackAction(), new UserFeedbackActionNotifier(this.mListeners));
    }

    public String userFeedback(AppController appController, long userId, String recBoost) {
        Bundle b = appController.createServiceBundle();
        b.putLong("user_id", userId);
        b.putString("recommendationBoost", recBoost);
        return executeServiceAction(appController, Actions.USER_FEEDBACK, b);
    }
}
