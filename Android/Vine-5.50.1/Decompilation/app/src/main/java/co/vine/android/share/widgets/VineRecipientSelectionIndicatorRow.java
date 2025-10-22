package co.vine.android.share.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import co.vine.android.R;
import co.vine.android.api.VineRecipient;
import co.vine.android.share.providers.RecipientProvider;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.FlowLayout;
import co.vine.android.views.SimpleTextWatcher;
import co.vine.android.widget.ObservableSet;
import co.vine.android.widget.TypefacesEditText;
import co.vine.android.widget.VineIMEInputConnection;
import com.edisonwang.android.slog.SLog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class VineRecipientSelectionIndicatorRow extends LinearLayout {
    private int mCollapseRecipientsAfterX;
    private final VineRecipientOverflowItemView mOverflowItem;
    private final TypefacesEditText mQuery;
    private RecipientProvider mRecipientProvider;
    private final List<VineRecipient> mRecipients;
    private boolean mRecipientsSelectable;
    private VineRecipientView mSelectedRecipientView;
    private final FlowLayout mSelectedRecipients;
    private ObservableSet<VineRecipient> mSelectedRecipientsRepository;

    public VineRecipientSelectionIndicatorRow(Context context) {
        this(context, null);
    }

    public VineRecipientSelectionIndicatorRow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VineRecipientSelectionIndicatorRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.vm_recipient_selection_indicator, this);
        this.mSelectedRecipients = (FlowLayout) findViewById(R.id.selected_recipients);
        this.mQuery = (TypefacesEditText) findViewById(R.id.query);
        this.mOverflowItem = createVmRecipientOverflowView();
        this.mRecipients = new ArrayList();
        if (attrs != null) {
            applyStyles(context, attrs);
        }
        setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.widgets.VineRecipientSelectionIndicatorRow.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                VineRecipientSelectionIndicatorRow.this.clearSelectedRecipientIndicator();
                VineRecipientSelectionIndicatorRow.this.mQuery.requestFocus();
                CommonUtil.setSoftKeyboardVisibility(VineRecipientSelectionIndicatorRow.this.getContext(), VineRecipientSelectionIndicatorRow.this.mQuery, true);
            }
        });
        this.mQuery.setKeyboardListener(new TypefacesEditText.KeyboardListener() { // from class: co.vine.android.share.widgets.VineRecipientSelectionIndicatorRow.2
            @Override // co.vine.android.widget.TypefacesEditText.KeyboardListener
            public void onKeyboardDismissed() {
                VineRecipientSelectionIndicatorRow.this.clearSelectedRecipientIndicator();
            }

            @Override // co.vine.android.widget.TypefacesEditText.KeyboardListener
            public boolean sendKeyEvent(KeyEvent event) {
                if (event == null) {
                    return false;
                }
                if (event.getUnicodeChar() == VineIMEInputConnection.GARBAGE_CHARACTER.charAt(0)) {
                    return true;
                }
                int action = event.getAction();
                int keyCode = event.getKeyCode();
                if (CommonUtil.isNullOrWhitespace(VineRecipientSelectionIndicatorRow.this.mQuery.getText().toString()) && keyCode == 67 && action == 1) {
                    VineRecipientView recipientView = VineRecipientSelectionIndicatorRow.this.mSelectedRecipientView;
                    if (recipientView == null) {
                        if (VineRecipientSelectionIndicatorRow.this.mRecipients.isEmpty()) {
                            return false;
                        }
                        VineRecipient recipientToSelect = (VineRecipient) VineRecipientSelectionIndicatorRow.this.mRecipients.get(VineRecipientSelectionIndicatorRow.this.mRecipients.size() - 1);
                        VineRecipientSelectionIndicatorRow.this.selectRecipient(recipientToSelect);
                        return true;
                    }
                    VineRecipient recipientToRemove = recipientView.getRecipient();
                    VineRecipientSelectionIndicatorRow.this.mSelectedRecipientView = null;
                    VineRecipientSelectionIndicatorRow.this.mSelectedRecipientsRepository.remove(recipientToRemove);
                    return true;
                }
                if (keyCode != 66 && action != 6) {
                    return false;
                }
                CommonUtil.setSoftKeyboardVisibility(VineRecipientSelectionIndicatorRow.this.getContext(), VineRecipientSelectionIndicatorRow.this.mQuery, false);
                return true;
            }
        });
        this.mQuery.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.share.widgets.VineRecipientSelectionIndicatorRow.3
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void afterTextChanged(Editable text) {
                if (!CommonUtil.isNullOrWhitespace(VineRecipientSelectionIndicatorRow.this.mQuery.getText().toString())) {
                    VineRecipientSelectionIndicatorRow.this.clearSelectedRecipientIndicator();
                    String textString = text.toString();
                    if (VineRecipientSelectionIndicatorRow.this.mRecipientProvider != null) {
                        VineRecipientSelectionIndicatorRow.this.mRecipientProvider.requestUserSearch(textString);
                        VineRecipientSelectionIndicatorRow.this.mRecipientProvider.requestContacts(textString);
                    }
                }
            }
        });
        this.mSelectedRecipients.addView(this.mOverflowItem, this.mSelectedRecipients.indexOfChild(this.mQuery));
    }

    private void addSpaceBeforeCursor() {
        this.mQuery.setText(" ");
        this.mQuery.setSelection(this.mQuery.getText().length());
    }

    public void addRecipient(VineRecipient recipient) {
        clearSelectedRecipientIndicator();
        this.mRecipients.add(recipient);
        if (this.mRecipients.size() > this.mCollapseRecipientsAfterX) {
            this.mOverflowItem.addRecipient(recipient);
            return;
        }
        View view = createVmRecipientView(recipient);
        SLog.i("SelectionIndicator query view index: {}", Integer.valueOf(this.mSelectedRecipients.indexOfChild(this.mQuery)));
        this.mSelectedRecipients.addView(view, this.mSelectedRecipients.indexOfChild(this.mOverflowItem));
    }

    public void removeRecipient(VineRecipient recipient) {
        clearSelectedRecipientIndicator();
        this.mRecipients.remove(recipient);
        if (this.mOverflowItem.containsRecipient(recipient)) {
            this.mOverflowItem.removeRecipient(recipient);
            return;
        }
        VineRecipientView matchingRecipientView = findViewsForRecipient(recipient);
        if (matchingRecipientView != null) {
            this.mSelectedRecipients.removeView(matchingRecipientView);
            VineRecipient firstOverflowRecipient = this.mOverflowItem.removeFirstRecipient();
            if (firstOverflowRecipient != null) {
                View view = createVmRecipientView(firstOverflowRecipient);
                this.mSelectedRecipients.addView(view, this.mSelectedRecipients.indexOfChild(this.mOverflowItem));
            }
        }
    }

    public boolean hasRecipients() {
        return !this.mRecipients.isEmpty();
    }

    public ArrayList<VineRecipient> getRecipients() {
        return new ArrayList<>(this.mRecipients);
    }

    public int numRecipients() {
        return this.mRecipients.size();
    }

    public void clearSearchTermAndDismissKeyboard() {
        addSpaceBeforeCursor();
        if (this.mRecipientProvider != null) {
            this.mRecipientProvider.requestUserSearch("");
            this.mRecipientProvider.requestContacts("");
        }
    }

    public void setSelectedRecipientsRepository(ObservableSet<VineRecipient> selectedRecipientsRepository) {
        this.mSelectedRecipientsRepository = selectedRecipientsRepository;
    }

    public void setRecipientProvider(RecipientProvider recipientProvider) {
        this.mRecipientProvider = recipientProvider;
    }

    public void clearSelectedRecipientIndicator() {
        if (this.mSelectedRecipientView != null) {
            this.mSelectedRecipientView.setSelected(false);
            this.mSelectedRecipientView = null;
        }
    }

    private void applyStyles(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.VineRecipientSelectionIndicatorRow);
        boolean queryEnabled = attributes.getBoolean(0, false);
        this.mRecipientsSelectable = attributes.getBoolean(1, false);
        this.mCollapseRecipientsAfterX = attributes.getInteger(2, Integer.MAX_VALUE);
        attributes.recycle();
        if (queryEnabled) {
            ViewUtil.enableAndShow(this.mQuery);
        } else {
            ViewUtil.disableAndHide(this.mQuery);
        }
    }

    private VineRecipientView findViewsForRecipient(VineRecipient recipient) {
        for (int i = 0; i < this.mSelectedRecipients.getChildCount(); i++) {
            View view = this.mSelectedRecipients.getChildAt(i);
            if (view instanceof VineRecipientView) {
                VineRecipientView vineRecipientView = (VineRecipientView) view;
                VineRecipient viewRecipient = vineRecipientView.getRecipient();
                if (viewRecipient.equals(recipient)) {
                    return vineRecipientView;
                }
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectRecipient(VineRecipient recipient) {
        VineRecipientView matchingRecipientView = findViewsForRecipient(recipient);
        if (matchingRecipientView != null) {
            this.mSelectedRecipientView = matchingRecipientView;
            matchingRecipientView.setSelected(true);
        }
    }

    private View createVmRecipientView(VineRecipient recipient) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        VineRecipientView recipientView = (VineRecipientView) layoutInflater.inflate(R.layout.recipient_picker_item_standalone, (ViewGroup) this.mSelectedRecipients, false);
        recipientView.bind(recipient);
        if (this.mRecipientsSelectable) {
            recipientView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.share.widgets.VineRecipientSelectionIndicatorRow.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (view instanceof VineRecipientView) {
                        VineRecipientView clickedRecipientView = (VineRecipientView) view;
                        VineRecipientView recipientView2 = VineRecipientSelectionIndicatorRow.this.mSelectedRecipientView;
                        if (clickedRecipientView != null && clickedRecipientView == recipientView2) {
                            VineRecipient recipientToRemove = recipientView2.getRecipient();
                            VineRecipientSelectionIndicatorRow.this.mSelectedRecipientView = null;
                            VineRecipientSelectionIndicatorRow.this.mSelectedRecipientsRepository.remove(recipientToRemove);
                        } else if (clickedRecipientView != null && clickedRecipientView != VineRecipientSelectionIndicatorRow.this.mSelectedRecipientView) {
                            if (recipientView2 != null) {
                                recipientView2.setSelected(false);
                            }
                            VineRecipient recipient2 = clickedRecipientView.getRecipient();
                            if (recipient2 != null) {
                                VineRecipientSelectionIndicatorRow.this.selectRecipient(recipient2);
                            }
                        }
                    }
                }
            });
        }
        return recipientView;
    }

    private VineRecipientOverflowItemView createVmRecipientOverflowView() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        VineRecipientOverflowItemView view = (VineRecipientOverflowItemView) layoutInflater.inflate(R.layout.recipient_picker_overflow_item_standalone, (ViewGroup) this.mSelectedRecipients, false);
        return view;
    }
}
