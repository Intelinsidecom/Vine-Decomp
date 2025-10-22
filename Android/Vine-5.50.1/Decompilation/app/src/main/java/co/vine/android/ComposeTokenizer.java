package co.vine.android;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView;
import java.util.HashSet;

/* loaded from: classes.dex */
public final class ComposeTokenizer implements MultiAutoCompleteTextView.Tokenizer {
    private static final HashSet<Character> mDelimiters;
    private final TokenListener mCallback;

    public interface TokenListener {
        void onTagTokenFound();

        void onUserTokenFound();
    }

    static {
        HashSet<Character> delimiters = new HashSet<>(32);
        delimiters.add('\t');
        delimiters.add('\n');
        delimiters.add('.');
        delimiters.add('!');
        delimiters.add('?');
        delimiters.add(',');
        delimiters.add(':');
        delimiters.add(';');
        delimiters.add('\'');
        delimiters.add('\"');
        delimiters.add('`');
        delimiters.add('/');
        delimiters.add('\\');
        delimiters.add('-');
        delimiters.add('=');
        delimiters.add('+');
        delimiters.add('~');
        delimiters.add('$');
        delimiters.add('%');
        delimiters.add('^');
        delimiters.add('&');
        delimiters.add('*');
        delimiters.add('(');
        delimiters.add(')');
        delimiters.add('[');
        delimiters.add(']');
        delimiters.add('{');
        delimiters.add('}');
        delimiters.add('<');
        delimiters.add('>');
        delimiters.add('|');
        delimiters.add('@');
        delimiters.add('#');
        mDelimiters = delimiters;
    }

    public ComposeTokenizer(TokenListener callback) {
        this.mCallback = callback;
    }

    @Override // android.widget.MultiAutoCompleteTextView.Tokenizer
    public int findTokenStart(CharSequence text, int cursor) {
        char current;
        if (cursor > 0) {
            int i = cursor;
            do {
                i--;
                current = text.charAt(i);
                if (i <= 0) {
                    break;
                }
            } while (!mDelimiters.contains(Character.valueOf(current)));
            boolean sawAt = current == '@';
            boolean sawHashtag = current == '#';
            if (sawAt || sawHashtag) {
                if (i == 0 || mDelimiters.contains(Character.valueOf(text.charAt(i - 1))) || text.charAt(i - 1) == ' ') {
                    if (sawAt) {
                        this.mCallback.onUserTokenFound();
                    } else if (sawHashtag) {
                        this.mCallback.onTagTokenFound();
                    }
                    return i;
                }
                return cursor;
            }
            return cursor;
        }
        return cursor;
    }

    @Override // android.widget.MultiAutoCompleteTextView.Tokenizer
    public int findTokenEnd(CharSequence text, int cursor) {
        int len = text.length();
        for (int i = cursor; i < len; i++) {
            if (mDelimiters.contains(Character.valueOf(text.charAt(i)))) {
                return i;
            }
        }
        return len;
    }

    @Override // android.widget.MultiAutoCompleteTextView.Tokenizer
    public CharSequence terminateToken(CharSequence text) {
        if (!(text instanceof Spanned)) {
            return ((Object) text) + " ";
        }
        SpannableString sp = new SpannableString(((Object) text) + " ");
        TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
        return sp;
    }
}
