package co.vine.android.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import co.vine.android.views.SdkEditText;

/* loaded from: classes.dex */
public class TypingEditText extends SdkEditText {
    private Handler mHandler;
    private TypingListener mListener;
    private long mTimeout;

    public interface TypingListener {
        void onTypingTimeout(View view);
    }

    public TypingEditText(Context context) {
        super(context);
        setup();
    }

    public TypingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public TypingEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    public void setTypingListener(TypingListener listener) {
        this.mListener = listener;
    }

    public void setTimeout(long timeout) {
        this.mTimeout = timeout;
    }

    private void setup() {
        this.mTimeout = 500L;
        this.mHandler = new TypingHandler();
        addTextChangedListener(new TypingTextWatcher());
    }

    private class TypingTextWatcher implements TextWatcher {
        private TypingTextWatcher() {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TypingEditText.this.mHandler != null) {
                TypingEditText.this.mHandler.removeMessages(1);
                TypingEditText.this.mHandler.sendMessageDelayed(TypingEditText.this.mHandler.obtainMessage(1), TypingEditText.this.mTimeout);
            }
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable s) {
        }
    }

    private class TypingHandler extends Handler {
        public TypingHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (TypingEditText.this.mListener != null) {
                        TypingEditText.this.mListener.onTypingTimeout(TypingEditText.this);
                        break;
                    }
                    break;
            }
        }
    }
}
