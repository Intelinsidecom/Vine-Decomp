package com.twitter.sdk.android.core.internal.scribe;

import android.content.Context;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.events.EventTransform;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import io.fabric.sdk.android.services.events.QueueFileEventStorage;
import java.io.IOException;
import java.util.UUID;

/* loaded from: classes.dex */
class ScribeFilesManager extends EventsFilesManager<ScribeEvent> {
    public ScribeFilesManager(Context context, EventTransform<ScribeEvent> transform, CurrentTimeProvider currentTimeProvider, QueueFileEventStorage eventsStorage, int defaultMaxFilesToKeep) throws IOException {
        super(context, transform, currentTimeProvider, eventsStorage, defaultMaxFilesToKeep);
    }

    @Override // io.fabric.sdk.android.services.events.EventsFilesManager
    protected String generateUniqueRollOverFileName() {
        UUID targetUUIDComponent = UUID.randomUUID();
        return "se_" + targetUUIDComponent.toString() + "_" + this.currentTimeProvider.getCurrentTimeMillis() + ".tap";
    }
}
