package co.vine.android.service.components.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import co.vine.android.R;
import co.vine.android.api.PostInfo;
import co.vine.android.api.VineError;
import co.vine.android.api.VineKnownErrors;
import co.vine.android.api.VineParserReader;
import co.vine.android.api.VinePrivateMessage;
import co.vine.android.api.VinePrivateMessagePostResponseWithUsers;
import co.vine.android.api.VinePrivateMessageResponse;
import co.vine.android.api.VineRecipient;
import co.vine.android.api.VineUpload;
import co.vine.android.api.VineUser;
import co.vine.android.client.AppController;
import co.vine.android.client.VineAPI;
import co.vine.android.network.NetworkOperation;
import co.vine.android.network.NetworkOperationReader;
import co.vine.android.provider.VineDatabaseHelper;
import co.vine.android.service.VineServiceAction;
import co.vine.android.service.VineServiceActionResult;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.SMSUtil;
import co.vine.android.util.Util;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class VMShareAction extends VineServiceAction {
    @Override // co.vine.android.service.VineServiceAction
    public VineServiceActionResult doAction(VineServiceAction.Request request) throws Throwable {
        Bundle bundle = preMergeMessage(request);
        VineUpload upload = (VineUpload) bundle.getParcelable("upload");
        JSONObject postBody = new JSONObject();
        PostInfo info = upload.getPostInfo();
        StringBuilder url = VineAPI.buildUponUrl(request.api.getBaseUrl(), "conversations");
        VineDatabaseHelper dbHelper = VineDatabaseHelper.getDatabaseHelper(request.context);
        long conversationId = dbHelper.getConversationRemoteId(upload.conversationRowId);
        if (conversationId > 0) {
            url = VineAPI.buildUponUrl(url.toString(), Long.valueOf(conversationId));
        } else if (info.recipients == null && upload.conversationRowId > 0) {
            info.recipients = dbHelper.getConversationRecipientsFromConversationRowId(upload.conversationRowId);
        }
        VineParserReader vp = VineParserReader.createParserReader(24);
        if (info != null) {
            try {
                if (!TextUtils.isEmpty(info.message)) {
                    postBody.put("message", info.message);
                }
                if (info.postId > 0) {
                    postBody.put("postId", info.postId);
                }
                postBody.put("created", info.created > 0 ? info.created : System.currentTimeMillis());
                if (info.recipients != null && !info.recipients.isEmpty()) {
                    postBody.put("to", PostInfo.recipientsToJsonArray(info.recipients));
                } else if (conversationId <= 0) {
                    SLog.d("VM post failed: The recipients array is empty and no conversation id");
                }
            } catch (JSONException ex) {
                CrashUtil.logOrThrowInDebug(ex);
            }
        }
        postBody.put("locale", Util.getCountryCode());
        NetworkOperation op = request.networkFactory.createBasicAuthJsonPostRequest(request.context, url, (StringBuilder) request.api, postBody, (NetworkOperationReader) vp).execute();
        if (op.isOK()) {
            handleVinePrivateMessageResponses(request.context, upload, info, vp, bundle);
        } else {
            VineError error = (VineError) vp.getParsedObject();
            if (error != null) {
                SLog.e("ERROR POSTING: {} {}", Integer.valueOf(op.statusCode), error.getMessage());
                if (error.getErrorCode() == VineKnownErrors.CAPTCHA.code) {
                    upload.captchaUrl = error.getData();
                    if (upload.isPrivate) {
                        VineDatabaseHelper.getDatabaseHelper(request.context).setMessageError(upload.mergedMessageId, error.getErrorCode(), request.context.getString(R.string.message_failed_tap_to_retry));
                    }
                }
                VineDatabaseHelper.getDatabaseHelper(request.context).setMessageError(upload.mergedMessageId, error.getErrorCode(), request.context.getString(R.string.message_failed_tap_to_retry));
            } else {
                VineDatabaseHelper.getDatabaseHelper(request.context).setMessageError(upload.mergedMessageId, -1, request.context.getString(R.string.message_failed_tap_to_retry));
                SLog.e("Post failed: Unknown error while  sharing post with postId {}", Long.valueOf(info.postId));
            }
        }
        return new VineServiceActionResult(vp, op);
    }

    private Bundle preMergeMessage(VineServiceAction.Request request) throws Throwable {
        Bundle b = request.b;
        VineDatabaseHelper dbHelper = VineDatabaseHelper.getDatabaseHelper(request.context);
        int networkType = b.getInt("network", 1);
        long sessionOwnerId = b.getLong("s_owner_id", 0L);
        VineUpload upload = (VineUpload) b.getParcelable("upload");
        PostInfo info = upload.getPostInfo();
        ArrayList<VineRecipient> recipients = info.recipients;
        long conversationRowId = upload.conversationRowId;
        if (recipients == null && conversationRowId > 0) {
            dbHelper.getConversationRecipientsFromConversationRowId(conversationRowId);
        }
        if (recipients != null) {
            Iterator<VineRecipient> it = recipients.iterator();
            while (it.hasNext()) {
                VineRecipient recipient = it.next();
                if (recipient.isFromUser()) {
                    if (recipient.recipientId <= 0) {
                        recipient.recipientId = dbHelper.getUserRowIdForUserRemoteId(recipient.userId);
                    } else if (recipient.userId < 0) {
                        recipient.userId = dbHelper.getUserRemoteIdForUserRowId(recipient.recipientId);
                    }
                }
            }
        }
        long myUserId = dbHelper.getUserRowIdForUserRemoteId(sessionOwnerId);
        if (conversationRowId <= 0) {
            conversationRowId = dbHelper.determineOrCreateBestConversationRowIdForRecipients(recipients, networkType);
        }
        upload.conversationRowId = conversationRowId;
        long created = System.currentTimeMillis();
        String videoPath = null;
        String thumbPath = null;
        long messageId = dbHelper.getNewMessageId();
        if (info.postId > 0) {
            videoPath = info.sharedPostVideoUrl;
            thumbPath = info.sharedPostThumbUrl;
        }
        VinePrivateMessage vpm = new VinePrivateMessage(-1L, conversationRowId, messageId, myUserId, created, info.message, videoPath, thumbPath, 1, false, info.postId, 0, null, upload.path);
        long messageRowId = dbHelper.mergeMessage(conversationRowId, vpm);
        dbHelper.updateConversationWithLastMessage(conversationRowId, messageRowId, created);
        upload.postInfo = info.toString();
        upload.mergedMessageId = messageRowId;
        b.putParcelable("upload", upload);
        b.putLong("premerged_message_id", messageRowId);
        return b;
    }

    private void handleVinePrivateMessageResponses(Context context, VineUpload upload, PostInfo info, VineParserReader vp, Bundle bundle) {
        String text;
        VinePrivateMessagePostResponseWithUsers serverResponse = (VinePrivateMessagePostResponseWithUsers) vp.getParsedObject();
        ArrayList<VinePrivateMessageResponse> messageResponses = serverResponse.responses;
        ArrayList<VineUser> users = serverResponse.users;
        ArrayList<VineRecipient> recipients = serverResponse.recipients;
        VineDatabaseHelper dbHelper = VineDatabaseHelper.getDatabaseHelper(context);
        VinePrivateMessage premergedMessage = dbHelper.getMessageFromMessageRow(upload.mergedMessageId);
        if (upload.mergedMessageId <= 0 || premergedMessage == null) {
            CrashUtil.log("No local message to merge with.");
            return;
        }
        if (users != null) {
            dbHelper.mergeRecipientsWithUsersAndRemoveUnusedRecipients(premergedMessage.conversationRowId, recipients, users);
        }
        long messageIdToMerge = -1;
        if (messageResponses == null || messageResponses.size() < 1) {
            Object[] objArr = new Object[1];
            objArr[0] = Boolean.valueOf(messageResponses == null);
            CrashUtil.log("No vine private message responses, deleting the conversation and bailing. Response is null: {}", objArr);
            dbHelper.deleteConversation(-1L, premergedMessage.conversationRowId);
            return;
        }
        if (messageResponses.size() == 1) {
            if (premergedMessage.conversationId <= 0) {
                premergedMessage.conversationRowId = dbHelper.mergeConversationWithLocalId(premergedMessage.conversationRowId, messageResponses.get(0).conversationId, premergedMessage.networkType);
            }
            messageIdToMerge = upload.mergedMessageId;
        } else {
            dbHelper.deleteConversation(-1L, premergedMessage.conversationRowId);
        }
        String smsFormatter = null;
        String smsFooter = null;
        String videoUrl = null;
        String thumbnailUrl = null;
        Iterator<VinePrivateMessageResponse> it = messageResponses.iterator();
        while (it.hasNext()) {
            VinePrivateMessageResponse vpmr = it.next();
            if (vpmr != null) {
                if (vpmr.recipient != null && vpmr.recipient.key.equals("phoneNumber") && vpmr.shareUrl != null) {
                    if (smsFormatter == null) {
                        smsFormatter = context.getString(R.string.sms_text);
                    }
                    if (smsFooter == null) {
                        smsFooter = context.getString(R.string.sms_footer);
                    }
                    if (TextUtils.isEmpty(info.message)) {
                        text = String.format(smsFormatter, vpmr.shareUrl);
                    } else {
                        text = info.message + ": " + vpmr.shareUrl;
                    }
                    String text2 = text + smsFooter;
                    SLog.d("Send SMS to {} with text {}", vpmr.recipient.value, text2);
                    SMSUtil.sendSMS(vpmr.recipient.value, text2);
                }
                if (vpmr.videoUrl != null) {
                    videoUrl = vpmr.videoUrl;
                }
                if (vpmr.thumbnailUrl != null) {
                    thumbnailUrl = vpmr.thumbnailUrl;
                }
                handleSingleVinePrivateMessageResponse(context, dbHelper, vpmr, info, messageIdToMerge, premergedMessage.conversationRowId);
                if (vpmr.error != null) {
                    bundle.putBoolean("should_delete_upload", true);
                }
            }
        }
        bundle.putString("post_url", videoUrl);
        bundle.putString("thumbnail_url", thumbnailUrl);
    }

    private void handleSingleVinePrivateMessageResponse(Context context, VineDatabaseHelper dbHelper, VinePrivateMessageResponse vpmr, PostInfo info, long messageIdToMerge, long conversationRowIdToMerge) {
        int errorCode;
        String errorReason;
        long conversationRowId;
        long userRowId = dbHelper.getUserRowIdForUserRemoteId(AppController.getInstance(context).getActiveSessionReadOnly().getUserId());
        if (vpmr.error != null) {
            errorCode = vpmr.error.getErrorCode();
            errorReason = vpmr.error.getMessage();
        } else {
            errorCode = 0;
            errorReason = null;
        }
        VinePrivateMessage vpm = new VinePrivateMessage(-1L, vpmr.conversationId, vpmr.messageId, userRowId, info.created, info.message, vpmr.videoUrl, vpmr.thumbnailUrl, 1, false, info.postId, errorCode, errorReason);
        if (messageIdToMerge > 0) {
            long finalMessageRowId = dbHelper.mergeMessageWithMessageRow(messageIdToMerge, conversationRowIdToMerge, vpm);
            dbHelper.updateConversationWithLastMessage(conversationRowIdToMerge, finalMessageRowId, vpm.created);
        } else {
            HashSet<Long> userRowIds = new HashSet<>();
            userRowIds.add(Long.valueOf(vpmr.recipient.recipientId));
            if (vpmr.conversationId > 0) {
                conversationRowId = dbHelper.getConversationRowId(vpmr.conversationId);
            } else {
                conversationRowId = dbHelper.determineBestConversationRowIdForUserRowId(vpmr.recipient.recipientId);
            }
            if (conversationRowId <= 0) {
                conversationRowId = dbHelper.createConversationRowId(userRowIds, 1);
                dbHelper.mergeConversationWithLocalId(conversationRowId, vpmr.conversationId, 1);
            }
            long messageId = dbHelper.mergeMessage(conversationRowId, vpm);
            dbHelper.updateConversationWithLastMessage(conversationRowId, messageId, vpm.created);
        }
        SLog.d("VM post successful, sending merge broadcast now");
        Intent intent = new Intent("co.vine.android.service.mergeSelfNewMessage");
        intent.putExtra("conversation_id", vpm.conversationId);
        intent.putExtra("message_id", vpm.messageId);
        context.sendBroadcast(intent, CrossConstants.BROADCAST_PERMISSION);
    }
}
