package co.vine.android.widgets;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.os.Bundle;
import co.vine.android.util.CrashUtil;

@TargetApi(11)
/* loaded from: classes.dex */
public class PromptDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private OnDialogDoneListener mListener;

    public interface OnDialogDoneListener {
        void onDialogDone(DialogInterface dialogInterface, int i, int i2);
    }

    public static PromptDialogFragment newInstance(int id) {
        PromptDialogFragment f = new PromptDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        f.setArguments(bundle);
        return f;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mListener == null) {
            ComponentCallbacks2 targetFragment = getTargetFragment();
            if (targetFragment instanceof OnDialogDoneListener) {
                this.mListener = (OnDialogDoneListener) targetFragment;
            } else if (activity instanceof OnDialogDoneListener) {
                this.mListener = (OnDialogDoneListener) activity;
            }
        }
    }

    @Override // android.app.DialogFragment
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Bundle args = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), 2);
        if (args.containsKey("icon")) {
            builder.setIcon(args.getInt("icon"));
        }
        if (args.containsKey("title")) {
            builder.setTitle(args.getInt("title"));
        }
        if (args.containsKey("message")) {
            builder.setMessage(args.getInt("message"));
        }
        if (args.containsKey("positive_button")) {
            builder.setPositiveButton(args.getInt("positive_button"), this);
        }
        if (args.containsKey("neutral_button")) {
            builder.setNeutralButton(args.getInt("neutral_button"), this);
        }
        if (args.containsKey("negative_button")) {
            builder.setNegativeButton(args.getInt("negative_button"), this);
        }
        if (args.containsKey("items")) {
            builder.setItems(getResources().getTextArray(args.getInt("items")), this);
        }
        if (args.containsKey("items_string")) {
            builder.setItems(args.getStringArray("items_string"), this);
        }
        return builder.create();
    }

    @Override // android.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (this.mListener != null) {
            this.mListener.onDialogDone(dialog, getArguments().getInt("id"), Integer.MIN_VALUE);
        }
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        if (this.mListener != null) {
            this.mListener.onDialogDone(dialog, getArguments().getInt("id"), which);
        }
    }

    public void setListener(OnDialogDoneListener listener) {
        this.mListener = listener;
    }

    public PromptDialogFragment setMessage(int message) {
        getArguments().putInt("message", message);
        return this;
    }

    public PromptDialogFragment setItems(String[] items) {
        getArguments().putStringArray("items_string", items);
        return this;
    }

    public PromptDialogFragment setPositiveButton(int positiveButton) {
        getArguments().putInt("positive_button", positiveButton);
        return this;
    }

    public PromptDialogFragment setNegativeButton(int negativeButton) {
        getArguments().putInt("negative_button", negativeButton);
        return this;
    }

    public void show(FragmentManager fragmentManager) {
        try {
            show(fragmentManager, (String) null);
        } catch (Exception e) {
            CrashUtil.logException(e);
        }
    }
}
