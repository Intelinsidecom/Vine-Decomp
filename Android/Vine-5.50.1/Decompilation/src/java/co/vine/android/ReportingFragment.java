package co.vine.android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import co.vine.android.api.ComplaintMenuOption;
import co.vine.android.client.AppSessionListener;
import co.vine.android.widget.Typefaces;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.edisonwang.android.slog.SLog;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import twitter4j.conf.PropertyConfiguration;

/* loaded from: classes.dex */
public class ReportingFragment extends BaseArrayListFragment {
    private View mBlockPersonContainer;
    private TextView mBlockPersonSubtext;
    private TextView mBlockPersonText;
    private View mBlockSelector;
    private boolean mBlockingUser;
    private ComplaintMenuOption.ComplaintChoice mChoice;
    private long mCommentId;
    private ComplaintMenuOption mComplaintMenuOptions;
    private String mComplaintType;
    private long mPostId;
    private ProgressBar mProgressBar;
    private TextView mPromptText;
    private Button mReportButton;
    private View mReportContainer;
    private View mReportMenu;
    private View mReportSelector;
    private View mSelectedView;
    private long mUserId;
    private String mUsernameToBlock;

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        setAppSessionListener(new ReportingAppSessionListener());
        this.mComplaintType = args.getString("menu_type");
        this.mPostId = args.getLong("postId", -1L);
        this.mUserId = args.getLong("userId", -1L);
        this.mCommentId = args.getLong("commentId");
        this.mBlockingUser = args.getBoolean("blockingUser", false);
        this.mUsernameToBlock = args.getString("username");
        setHasOptionsMenu(true);
    }

    public static ReportingFragment newFragment(Intent i) {
        Bundle args = i.getExtras();
        String commentId = "-1";
        if (i != null) {
            commentId = i.getStringExtra("commentId");
        }
        if (!TextUtils.isEmpty(commentId)) {
            args.putLong("commentId", Long.valueOf(commentId).longValue());
        } else {
            args.putLong("commentId", -1L);
        }
        ReportingFragment fragment = new ReportingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return createView(inflater, R.layout.reporting_menu, container);
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) throws Resources.NotFoundException {
        Activity activity = getActivity();
        this.mAdapter = new ComplaintChoiceAdapter(activity, R.layout.settings_check, new ComplaintMenuOption.ComplaintChoice[0]);
        this.mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        this.mReportMenu = view.findViewById(R.id.reporting_custom_menu);
        ((RefreshableListView) this.mListView).disablePTR();
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        this.mBlockPersonText = (TextView) view.findViewById(R.id.block_person_text);
        this.mBlockPersonSubtext = (TextView) view.findViewById(R.id.block_person_subtitle);
        if (!TextUtils.isEmpty(this.mUsernameToBlock)) {
            this.mBlockPersonText.setText(getString(R.string.block_username, this.mUsernameToBlock));
            this.mBlockPersonSubtext.setText(getString(R.string.block_username_explanation, this.mUsernameToBlock));
        }
        this.mBlockPersonText.setTypeface(Typefaces.get(activity).getContentTypeface(0, 4));
        TextView fileReportText = (TextView) view.findViewById(R.id.file_report_text);
        fileReportText.setTypeface(Typefaces.get(activity).getContentTypeface(0, 4));
        this.mBlockSelector = view.findViewById(R.id.block_switch);
        ComplaintChoiceAdapter.setSelected(activity, this.mBlockSelector, true, 16734567);
        this.mReportSelector = view.findViewById(R.id.report_switch);
        ComplaintChoiceAdapter.setSelected(activity, this.mReportSelector, false, -1);
        this.mReportButton = (Button) view.findViewById(R.id.report_button);
        this.mReportButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ReportingFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                ReportingFragment.this.sendReport();
            }
        });
        this.mBlockPersonContainer = view.findViewById(R.id.block_person_container);
        this.mReportContainer = view.findViewById(R.id.report_container);
        this.mBlockPersonContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ReportingFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) throws Resources.NotFoundException {
                Activity act = ReportingFragment.this.getActivity();
                if (!ReportingFragment.this.mBlockSelector.isSelected()) {
                    ComplaintChoiceAdapter.setSelected(act, ReportingFragment.this.mBlockSelector, true, 16734567);
                    ReportingFragment.this.mReportButton.setEnabled(true);
                } else {
                    ComplaintChoiceAdapter.setSelected(act, ReportingFragment.this.mBlockSelector, false, -1);
                    if (!ReportingFragment.this.mReportSelector.isSelected()) {
                        ReportingFragment.this.mReportButton.setEnabled(false);
                    }
                }
            }
        });
        this.mReportContainer.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.ReportingFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) throws Resources.NotFoundException {
                Activity act = ReportingFragment.this.getActivity();
                if (!ReportingFragment.this.mReportSelector.isSelected()) {
                    ReportingFragment.this.mReportMenu.setVisibility(0);
                    ReportingFragment.this.mReportButton.setText(R.string.send_report);
                    ComplaintChoiceAdapter.setSelected(act, ReportingFragment.this.mReportSelector, true, ReportingFragment.this.getResources().getColor(R.color.vine_green));
                    ReportingFragment.this.mReportButton.setEnabled(true);
                    return;
                }
                ReportingFragment.this.mReportMenu.setVisibility(4);
                if (ReportingFragment.this.mChoice != null) {
                    ReportingFragment.this.mChoice.selected = false;
                }
                ReportingFragment.this.mChoice = null;
                if (ReportingFragment.this.mSelectedView != null) {
                    ComplaintChoiceAdapter.setSelected(act, ReportingFragment.this.mSelectedView, false, -1);
                }
                ReportingFragment.this.mSelectedView = null;
                ComplaintChoiceAdapter.setSelected(act, ReportingFragment.this.mReportSelector, false, -1);
                ReportingFragment.this.mReportButton.setText(R.string.block_button);
                if (!ReportingFragment.this.mBlockSelector.isSelected()) {
                    ReportingFragment.this.mReportButton.setEnabled(false);
                }
            }
        });
        this.mPromptText = (TextView) view.findViewById(R.id.reporting_prompt);
        this.mAppController.fetchComplaintsMenu();
    }

    @Override // co.vine.android.BaseArrayListFragment
    public void onListItemClick(ListView l, View v, int position, long id) throws Resources.NotFoundException {
        Activity activity = getActivity();
        ComplaintMenuOption.ComplaintChoice c = (ComplaintMenuOption.ComplaintChoice) this.mAdapter.getItem(position);
        View selectionIndicator = v.findViewById(R.id.selection_indicator);
        ComplaintChoiceAdapter.setSelected(activity, selectionIndicator, true, getResources().getColor(R.color.vine_green));
        if (this.mSelectedView != null) {
            ComplaintChoiceAdapter.setSelected(activity, this.mSelectedView, false, -1);
            if (this.mChoice != null) {
                this.mChoice.selected = false;
                this.mChoice = null;
            }
        }
        this.mSelectedView = selectionIndicator;
        if (this.mSelectedView.isSelected()) {
            c.selected = true;
            this.mChoice = c;
        } else {
            this.mSelectedView = null;
        }
    }

    public void setReportingMenu(ArrayList<ComplaintMenuOption> complaintMenuOptions) {
        this.mProgressBar.setVisibility(8);
        boolean found = false;
        for (int i = 0; i < complaintMenuOptions.size(); i++) {
            ComplaintMenuOption o = complaintMenuOptions.get(i);
            if (o.complaintType.equals(this.mComplaintType)) {
                this.mComplaintMenuOptions = o;
                found = true;
            }
        }
        if (!found) {
            showErrorDialog();
        }
        if (this.mComplaintMenuOptions != null) {
            this.mPromptText.setText(this.mComplaintMenuOptions.prompt);
        }
        this.mAdapter = new ComplaintChoiceAdapter(getActivity(), R.layout.settings_check, this.mComplaintMenuOptions.complaintChoices);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
    }

    public void sendReport() {
        Activity activity = getActivity();
        if (activity != null) {
            if (this.mBlockSelector.isSelected() && !this.mBlockingUser) {
                this.mAppController.blockUser(this.mUserId, this.mUsernameToBlock);
            }
            if (this.mChoice != null && !TextUtils.isEmpty(this.mChoice.value)) {
                if (this.mComplaintType.equals("post")) {
                    this.mAppController.reportPost(this.mAppController.getActiveSession(), this.mPostId, this.mChoice.value);
                } else if (this.mComplaintType.equals(PropertyConfiguration.USER)) {
                    this.mAppController.reportPerson(this.mUserId, this.mChoice.value);
                } else if (this.mComplaintType.equals("comment")) {
                    this.mAppController.spamComment(Long.toString(this.mCommentId), Long.toString(this.mPostId), this.mChoice.value);
                }
            }
            if (this.mChoice != null && !TextUtils.isEmpty(this.mChoice.confirmation)) {
                Toast.makeText(activity, this.mChoice.confirmation, 0).show();
            }
            Intent i = new Intent();
            i.putExtra("reported", true);
            i.putExtra("postId", this.mPostId);
            i.putExtra("userId", this.mUserId);
            i.putExtra("commentId", this.mCommentId);
            if (this.mChoice != null && !TextUtils.isEmpty(this.mChoice.value)) {
                i.putExtra("event", this.mChoice.value);
            }
            i.putExtra("blocked", this.mBlockSelector.isSelected());
            i.putExtra("username", this.mUsernameToBlock);
            activity.setResult(-1, i);
            activity.finish();
        }
    }

    protected void showErrorDialog() {
        PromptDialogSupportFragment dialog = PromptDialogSupportFragment.newInstance(0).setMessage(R.string.API_ERROR_UNKNOWN_ERROR).setPositiveButton(R.string.ok);
        dialog.setListener(new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.ReportingFragment.4
            @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
            public void onDialogDone(DialogInterface dialog2, int id, int which) {
                switch (which) {
                    case -1:
                        dialog2.dismiss();
                        ReportingFragment.this.getActivity().finish();
                        break;
                }
            }
        });
        try {
            dialog.show(getActivity().getSupportFragmentManager());
        } catch (Exception e) {
            SLog.e("Exception showing error dialog", (Throwable) e);
        }
    }

    class ReportingAppSessionListener extends AppSessionListener {
        ReportingAppSessionListener() {
        }

        @Override // co.vine.android.client.AppSessionListener
        public void onComplaintMenu(String reqId, int statusCode, ArrayList<ComplaintMenuOption> complaintMenu) {
            if (statusCode < 200 || statusCode >= 300) {
                ReportingFragment.this.showErrorDialog();
                ReportingFragment.this.getActivity().finish();
            } else {
                ReportingFragment.this.setReportingMenu(complaintMenu);
            }
        }
    }
}
