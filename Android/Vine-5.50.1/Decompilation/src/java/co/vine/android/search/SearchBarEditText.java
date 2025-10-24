package co.vine.android.search;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import co.vine.android.util.BuildUtil;
import co.vine.android.util.Util;
import co.vine.android.views.SimpleTextWatcher;

/* loaded from: classes.dex */
public final class SearchBarEditText extends EditText {
    private boolean mFilter;
    private TextWatcher mQueryTextWatcher;
    private SearchBarTextChangeHandler mTextChangeHandler;

    public SearchBarEditText(Context context) {
        super(context);
        this.mQueryTextWatcher = new SimpleTextWatcher() { // from class: co.vine.android.search.SearchBarEditText.3
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (SearchBarEditText.this.mFilter) {
                    if (SearchBarEditText.this.mTextChangeHandler == null) {
                        if (BuildUtil.isLogsOn()) {
                            throw new RuntimeException("Specify a SearchTextChangeHandler to receive callbacks for typing events.");
                        }
                        return;
                    }
                    Message typingStarted = Message.obtain(null, 1, s);
                    SearchBarEditText.this.mTextChangeHandler.sendMessage(typingStarted);
                    int length = SearchBarEditText.this.getTrimmedLength();
                    if (length > 1) {
                        SearchBarEditText.this.mTextChangeHandler.removeMessages(2);
                        Message typingStopped = Message.obtain(null, 2, s);
                        SearchBarEditText.this.mTextChangeHandler.sendMessageDelayed(typingStopped, 150L);
                    } else if (length == 0) {
                        SearchBarEditText.this.mTextChangeHandler.sendMessage(Message.obtain((Handler) null, 3));
                    }
                }
            }
        };
        init();
    }

    public SearchBarEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mQueryTextWatcher = new SimpleTextWatcher() { // from class: co.vine.android.search.SearchBarEditText.3
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (SearchBarEditText.this.mFilter) {
                    if (SearchBarEditText.this.mTextChangeHandler == null) {
                        if (BuildUtil.isLogsOn()) {
                            throw new RuntimeException("Specify a SearchTextChangeHandler to receive callbacks for typing events.");
                        }
                        return;
                    }
                    Message typingStarted = Message.obtain(null, 1, s);
                    SearchBarEditText.this.mTextChangeHandler.sendMessage(typingStarted);
                    int length = SearchBarEditText.this.getTrimmedLength();
                    if (length > 1) {
                        SearchBarEditText.this.mTextChangeHandler.removeMessages(2);
                        Message typingStopped = Message.obtain(null, 2, s);
                        SearchBarEditText.this.mTextChangeHandler.sendMessageDelayed(typingStopped, 150L);
                    } else if (length == 0) {
                        SearchBarEditText.this.mTextChangeHandler.sendMessage(Message.obtain((Handler) null, 3));
                    }
                }
            }
        };
        init();
    }

    public SearchBarEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mQueryTextWatcher = new SimpleTextWatcher() { // from class: co.vine.android.search.SearchBarEditText.3
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (SearchBarEditText.this.mFilter) {
                    if (SearchBarEditText.this.mTextChangeHandler == null) {
                        if (BuildUtil.isLogsOn()) {
                            throw new RuntimeException("Specify a SearchTextChangeHandler to receive callbacks for typing events.");
                        }
                        return;
                    }
                    Message typingStarted = Message.obtain(null, 1, s);
                    SearchBarEditText.this.mTextChangeHandler.sendMessage(typingStarted);
                    int length = SearchBarEditText.this.getTrimmedLength();
                    if (length > 1) {
                        SearchBarEditText.this.mTextChangeHandler.removeMessages(2);
                        Message typingStopped = Message.obtain(null, 2, s);
                        SearchBarEditText.this.mTextChangeHandler.sendMessageDelayed(typingStopped, 150L);
                    } else if (length == 0) {
                        SearchBarEditText.this.mTextChangeHandler.sendMessage(Message.obtain((Handler) null, 3));
                    }
                }
            }
        };
        init();
    }

    @TargetApi(21)
    public SearchBarEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mQueryTextWatcher = new SimpleTextWatcher() { // from class: co.vine.android.search.SearchBarEditText.3
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (SearchBarEditText.this.mFilter) {
                    if (SearchBarEditText.this.mTextChangeHandler == null) {
                        if (BuildUtil.isLogsOn()) {
                            throw new RuntimeException("Specify a SearchTextChangeHandler to receive callbacks for typing events.");
                        }
                        return;
                    }
                    Message typingStarted = Message.obtain(null, 1, s);
                    SearchBarEditText.this.mTextChangeHandler.sendMessage(typingStarted);
                    int length = SearchBarEditText.this.getTrimmedLength();
                    if (length > 1) {
                        SearchBarEditText.this.mTextChangeHandler.removeMessages(2);
                        Message typingStopped = Message.obtain(null, 2, s);
                        SearchBarEditText.this.mTextChangeHandler.sendMessageDelayed(typingStopped, 150L);
                    } else if (length == 0) {
                        SearchBarEditText.this.mTextChangeHandler.sendMessage(Message.obtain((Handler) null, 3));
                    }
                }
            }
        };
        init();
    }

    private void init() {
        this.mFilter = true;
        addTextChangedListener(this.mQueryTextWatcher);
    }

    public void setTextChangeHandler(SearchBarTextChangeHandler handler) {
        this.mTextChangeHandler = handler;
    }

    public String getTrimmedText() {
        CharSequence s = getText();
        return s != null ? s.toString().trim() : "";
    }

    public int getTrimmedLength() {
        CharSequence s = getText();
        if (s != null) {
            return s.toString().trim().length();
        }
        return -1;
    }

    public void setText(CharSequence text, boolean filter) {
        if (filter) {
            setText(text);
            return;
        }
        this.mFilter = false;
        setText(text);
        setSelection(length());
        this.mFilter = true;
    }

    public void requestFocusAndShowKeyboardAfterDelay(long delayMillis) {
        postDelayed(new Runnable() { // from class: co.vine.android.search.SearchBarEditText.1
            @Override // java.lang.Runnable
            public void run() {
                SearchBarEditText.this.requestFocus();
                Util.setSoftKeyboardVisibility(SearchBarEditText.this.getContext(), SearchBarEditText.this, true);
            }
        }, delayMillis);
    }

    public void clearFocusAndHideKeyboardAfterDelay(long delayMillis) {
        postDelayed(new Runnable() { // from class: co.vine.android.search.SearchBarEditText.2
            @Override // java.lang.Runnable
            public void run() {
                SearchBarEditText.this.clearFocus();
                Util.setSoftKeyboardVisibility(SearchBarEditText.this.getContext(), SearchBarEditText.this, false);
            }
        }, delayMillis);
    }
}
