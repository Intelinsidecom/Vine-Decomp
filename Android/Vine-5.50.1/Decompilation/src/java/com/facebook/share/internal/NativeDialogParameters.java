package com.facebook.share.internal;

import android.os.Bundle;
import com.facebook.FacebookException;
import com.facebook.internal.Utility;
import com.facebook.internal.Validate;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideoContent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class NativeDialogParameters {
    public static Bundle create(UUID callId, ShareContent shareContent, boolean shouldFailOnDataError) {
        Validate.notNull(shareContent, "shareContent");
        Validate.notNull(callId, "callId");
        if (shareContent instanceof ShareLinkContent) {
            ShareLinkContent linkContent = (ShareLinkContent) shareContent;
            Bundle nativeParams = create(linkContent, shouldFailOnDataError);
            return nativeParams;
        }
        if (shareContent instanceof SharePhotoContent) {
            SharePhotoContent photoContent = (SharePhotoContent) shareContent;
            List<String> photoUrls = ShareInternalUtility.getPhotoUrls(photoContent, callId);
            Bundle nativeParams2 = create(photoContent, photoUrls, shouldFailOnDataError);
            return nativeParams2;
        }
        if (shareContent instanceof ShareVideoContent) {
            ShareVideoContent videoContent = (ShareVideoContent) shareContent;
            String videoUrl = ShareInternalUtility.getVideoUrl(videoContent, callId);
            Bundle nativeParams3 = create(videoContent, videoUrl, shouldFailOnDataError);
            return nativeParams3;
        }
        if (!(shareContent instanceof ShareOpenGraphContent)) {
            return null;
        }
        ShareOpenGraphContent openGraphContent = (ShareOpenGraphContent) shareContent;
        try {
            JSONObject openGraphActionJSON = ShareInternalUtility.toJSONObjectForCall(callId, openGraphContent);
            Bundle nativeParams4 = create(openGraphContent, ShareInternalUtility.removeNamespacesFromOGJsonObject(openGraphActionJSON, false), shouldFailOnDataError);
            return nativeParams4;
        } catch (JSONException e) {
            throw new FacebookException("Unable to create a JSON Object from the provided ShareOpenGraphContent: " + e.getMessage());
        }
    }

    private static Bundle create(ShareLinkContent linkContent, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(linkContent, dataErrorsFatal);
        Utility.putNonEmptyString(params, "TITLE", linkContent.getContentTitle());
        Utility.putNonEmptyString(params, "DESCRIPTION", linkContent.getContentDescription());
        Utility.putUri(params, "IMAGE", linkContent.getImageUrl());
        return params;
    }

    private static Bundle create(SharePhotoContent photoContent, List<String> imageUrls, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(photoContent, dataErrorsFatal);
        params.putStringArrayList("PHOTOS", new ArrayList<>(imageUrls));
        return params;
    }

    private static Bundle create(ShareVideoContent videoContent, String videoUrl, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(videoContent, dataErrorsFatal);
        Utility.putNonEmptyString(params, "TITLE", videoContent.getContentTitle());
        Utility.putNonEmptyString(params, "DESCRIPTION", videoContent.getContentDescription());
        Utility.putNonEmptyString(params, "VIDEO", videoUrl);
        return params;
    }

    private static Bundle create(ShareOpenGraphContent openGraphContent, JSONObject openGraphActionJSON, boolean dataErrorsFatal) {
        Bundle params = createBaseParameters(openGraphContent, dataErrorsFatal);
        String previewProperty = (String) ShareInternalUtility.getFieldNameAndNamespaceFromFullName(openGraphContent.getPreviewPropertyName()).second;
        Utility.putNonEmptyString(params, "PREVIEW_PROPERTY_NAME", previewProperty);
        Utility.putNonEmptyString(params, "ACTION_TYPE", openGraphContent.getAction().getActionType());
        Utility.putNonEmptyString(params, "ACTION", openGraphActionJSON.toString());
        return params;
    }

    private static Bundle createBaseParameters(ShareContent content, boolean dataErrorsFatal) {
        Bundle params = new Bundle();
        Utility.putUri(params, "LINK", content.getContentUrl());
        Utility.putNonEmptyString(params, "PLACE", content.getPlaceId());
        Utility.putNonEmptyString(params, "REF", content.getRef());
        params.putBoolean("DATA_FAILURES_FATAL", dataErrorsFatal);
        List<String> peopleIds = content.getPeopleIds();
        if (!Utility.isNullOrEmpty(peopleIds)) {
            params.putStringArrayList("FRIENDS", new ArrayList<>(peopleIds));
        }
        return params;
    }
}
