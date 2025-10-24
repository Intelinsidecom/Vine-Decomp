package co.vine.android.widgets;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentCallbacks;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import co.vine.android.util.CrashUtil;
import co.vine.android.widgets.PromptDialogFragment;

/* loaded from: classes.dex */
public class PromptDialogSupportFragment extends DialogFragment implements DialogInterface.OnClickListener {
    private OnDialogDoneListener mListener;

    public interface OnDialogDoneListener extends PromptDialogFragment.OnDialogDoneListener {
    }

    public static PromptDialogSupportFragment newInstance(int id, int theme) {
        PromptDialogSupportFragment f = new PromptDialogSupportFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("theme", theme);
        f.setArguments(bundle);
        return f;
    }

    public static PromptDialogSupportFragment newInstance(int id) {
        return newInstance(id, 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.support.v4.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mListener == null) {
            ComponentCallbacks targetFragment = getTargetFragment();
            if (targetFragment instanceof OnDialogDoneListener) {
                this.mListener = (OnDialogDoneListener) targetFragment;
            } else if (activity instanceof OnDialogDoneListener) {
                this.mListener = (OnDialogDoneListener) activity;
            }
        }
    }

    @Override // android.support.v4.app.DialogFragment
    @TargetApi(11)
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Bundle args = getArguments();
        int theme = args.getInt("theme", 2);
        AlertDialog.Builder builder = Build.VERSION.SDK_INT >= 11 ? new AlertDialog.Builder(getActivity(), theme) : new AlertDialog.Builder(getActivity());
        if (args.containsKey("icon")) {
            builder.setIcon(args.getInt("icon"));
        }
        if (args.containsKey("title")) {
            builder.setTitle(args.getInt("title"));
        }
        if (args.containsKey("message")) {
            builder.setMessage(args.getInt("message"));
        }
        if (args.containsKey("message_string")) {
            builder.setMessage(args.getString("message_string"));
        }
        Integer positiveButtonId = null;
        Integer negativeButtonId = null;
        Integer neutralButtonId = null;
        if (args.containsKey("positive_button")) {
            positiveButtonId = Integer.valueOf(args.getInt("positive_button"));
        }
        if (args.containsKey("neutral_button")) {
            neutralButtonId = Integer.valueOf(args.getInt("neutral_button"));
        }
        if (args.containsKey("negative_button")) {
            negativeButtonId = Integer.valueOf(args.getInt("negative_button"));
        }
        if (args.containsKey("vertical") && args.getBoolean("vertical")) {
            int options = 3;
            if (args.containsKey("option_num")) {
                options = args.getInt("option_num");
            }
            createVerticalButtons(builder, positiveButtonId, negativeButtonId, neutralButtonId, options);
        } else {
            createHorizontalButtons(builder, positiveButtonId, negativeButtonId, neutralButtonId);
        }
        if (args.containsKey("items")) {
            builder.setItems(getResources().getTextArray(args.getInt("items")), this);
        }
        if (args.containsKey("items_string")) {
            builder.setItems(args.getStringArray("items_string"), this);
        }
        Dialog dialog = builder.create();
        if (args.containsKey("cancel_outside")) {
            dialog.setCanceledOnTouchOutside(args.getBoolean("cancel_outside"));
        }
        return dialog;
    }

    private void createVerticalButtons(AlertDialog.Builder builder, Integer positiveButtonId, Integer negativeButtonId, Integer neutralButtonId, int length) {
        String[] items = new String[length];
        final int[] itemMap = new int[length];
        int count = 0;
        if (positiveButtonId != null) {
            itemMap[0] = -1;
            items[0] = getResources().getString(positiveButtonId.intValue());
            count = 0 + 1;
        }
        if (neutralButtonId != null) {
            itemMap[count] = -3;
            items[count] = getResources().getString(neutralButtonId.intValue());
            count++;
        }
        if (negativeButtonId != null) {
            itemMap[count] = -2;
            items[count] = getResources().getString(negativeButtonId.intValue());
        }
        builder.setItems(items, new DialogInterface.OnClickListener() { // from class: co.vine.android.widgets.PromptDialogSupportFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                PromptDialogSupportFragment.this.onClick(dialog, itemMap[which]);
            }
        });
    }

    private void createHorizontalButtons(AlertDialog.Builder builder, Integer positiveButtonId, Integer negativeButtonId, Integer neutralButtonId) {
        if (positiveButtonId != null) {
            builder.setPositiveButton(positiveButtonId.intValue(), this);
        }
        if (neutralButtonId != null) {
            builder.setNeutralButton(neutralButtonId.intValue(), this);
        }
        if (negativeButtonId != null) {
            builder.setNegativeButton(negativeButtonId.intValue(), this);
        }
    }

    @Override // android.support.v4.app.DialogFragment, android.content.DialogInterface.OnCancelListener
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

    public PromptDialogSupportFragment setTitle(int title) {
        getArguments().putInt("title", title);
        return this;
    }

    public PromptDialogSupportFragment setMessage(int message) {
        getArguments().putInt("message", message);
        return this;
    }

    public PromptDialogSupportFragment setMessage(String message) {
        getArguments().putString("message_string", message);
        return this;
    }

    public PromptDialogSupportFragment setItems(String[] items) {
        getArguments().putStringArray("items_string", items);
        return this;
    }

    public PromptDialogSupportFragment setPositiveButton(int positiveButton) {
        getArguments().putInt("positive_button", positiveButton);
        return this;
    }

    public PromptDialogSupportFragment setNeutralButton(int neutralButton) {
        getArguments().putInt("neutral_button", neutralButton);
        return this;
    }

    public PromptDialogSupportFragment setNegativeButton(int negativeButton) {
        getArguments().putInt("negative_button", negativeButton);
        return this;
    }

    public PromptDialogSupportFragment setCancelebleOnOutisde(boolean cancelebleOnOutisde) {
        getArguments().putBoolean("cancel_outside", cancelebleOnOutisde);
        return this;
    }

    public PromptDialogSupportFragment setOptionNumber(int optionNumber) {
        getArguments().putInt("option_num", optionNumber);
        return this;
    }

    public PromptDialogSupportFragment setButtonPlacementVertical(boolean vertical) {
        getArguments().putBoolean("vertical", vertical);
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
