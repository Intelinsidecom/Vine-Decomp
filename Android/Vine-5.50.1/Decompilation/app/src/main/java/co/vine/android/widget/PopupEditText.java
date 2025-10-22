package co.vine.android.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupWindow;
import co.vine.android.R;
import co.vine.android.api.VineEntity;
import co.vine.android.api.VineTypeAhead;
import co.vine.android.feedadapter.ArrayListScrollListener;
import co.vine.android.util.CrashUtil;
import co.vine.android.util.Util;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.SdkEditText;
import co.vine.android.views.SdkListView;
import com.edisonwang.android.slog.SLog;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/* loaded from: classes.dex */
public class PopupEditText extends SdkEditText implements Filter.FilterListener {
    private int mAttributeDropDownHorizontalOffset;
    private int mAttributeDropDownVerticalOffset;
    private int mAttributeThreshold;
    private final DropDownListView mDropDownListView;
    private TreeMap<Integer, VineEntity.Range> mEntityRanges;
    private FilterHandler mHandler;
    private OnBackPressedListener mOnBackPressedListener;
    private View.OnClickListener mOnClickListener;
    private final PopupWindow mPopupWindow;
    private final ScrollListener mScrollListener;
    private VineEntity mSelectedEntity;
    private boolean mShowRequested;
    private BaseAdapter mSimpleAdapter;
    private long mThrottleDelay;
    private MultiAutoCompleteTextView.Tokenizer mTokenizer;

    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    public interface PopupEditTextListener {
        void onFiltering(CharSequence charSequence);

        void onItemsExhausted(CharSequence charSequence);
    }

    public PopupEditText(Context context) {
        this(context, null);
    }

    public PopupEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.popupEditTextStyle);
    }

    public PopupEditText(Context context, AttributeSet attrs, int defStyleAttr) throws Resources.NotFoundException {
        super(context, attrs, defStyleAttr);
        this.mShowRequested = false;
        readStyles(context, attrs, defStyleAttr);
        Resources resources = getResources();
        ViewGroup contentContainer = createContentContainer(context);
        int fakeActionBarPlaceholderHeight = resources.getDimensionPixelSize(R.dimen.fake_action_bar_height) + ViewUtil.getStatusBarHeightPx(resources);
        int listViewTopPaddingHeight = resources.getDimensionPixelSize(R.dimen.list_view_top_padding);
        this.mDropDownListView = createDropDownListView(context);
        this.mScrollListener = new ScrollListener();
        this.mDropDownListView.setOnScrollListener(this.mScrollListener);
        contentContainer.addView(createView(context, fakeActionBarPlaceholderHeight, 0));
        contentContainer.addView(createView(context, listViewTopPaddingHeight, -1));
        contentContainer.addView(this.mDropDownListView);
        this.mPopupWindow = createPopupWindow(context, attrs);
        this.mPopupWindow.setContentView(contentContainer);
        initialize();
    }

    public void setTokenizer(MultiAutoCompleteTextView.Tokenizer tokenizer, Filterable filter, long throttleDelay) {
        if (tokenizer == null) {
            throw new IllegalArgumentException("tokenizer cannot be null.");
        }
        if (this.mSimpleAdapter == null) {
            throw new IllegalStateException("setAdapter must be called first with a non-null adapter");
        }
        this.mTokenizer = tokenizer;
        this.mThrottleDelay = throttleDelay;
        this.mHandler.setFilterable(filter);
    }

    @Override // android.widget.TextView
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        atomizeEntity(selStart, selEnd);
    }

    @Override // android.widget.TextView, android.view.View, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.mSelectedEntity == null || event.getAction() != 0 || event.getKeyCode() != 67) {
            return super.onKeyDown(keyCode, event);
        }
        clearSelectedEntity(this.mSelectedEntity.start, true);
        return true;
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        NavigableMap<Integer, VineEntity.Range> ranges = this.mEntityRanges;
        if (ranges != null && !ranges.isEmpty()) {
            Map.Entry<Integer, VineEntity.Range> entry = ranges.ceilingEntry(Integer.valueOf(start));
            while (entry != null) {
                VineEntity.Range range = entry.getValue();
                Editable editable = getText();
                int newStart = editable.getSpanStart(range.span);
                int newEnd = range.entity.end;
                range.entity.start = newStart;
                range.entity.end = newEnd;
                VineEntity.Range newRange = new VineEntity.Range(newEnd, range.entity, range.span);
                ranges.remove(entry.getKey());
                ranges.put(Integer.valueOf(newStart), newRange);
                entry = ranges.ceilingEntry(Integer.valueOf(range.entity.end));
            }
        }
        if (this.mSelectedEntity != null && this.mSelectedEntity.start == start) {
            clearSelectedEntity(this.mSelectedEntity.start, false);
        } else if (start + lengthAfter > 0 && !Character.isWhitespace(text.charAt((start + lengthAfter) - 1))) {
            performFilter(true);
        } else if (start + lengthAfter > 0) {
            dismissDropDown();
        }
        if (Build.VERSION.SDK_INT < 16) {
            atomizeEntity(getSelectionStart(), getSelectionEnd());
        }
    }

    @Override // android.widget.TextView
    public void setTypeface(Typeface tf, int style) {
        Context context = getContext();
        super.setTypeface(Typefaces.get(context).getContentTypeface(style, 2));
    }

    public void performFilter(boolean triggerCallback) {
        int end;
        if (this.mTokenizer != null && (end = getSelectionEnd()) >= 0) {
            CharSequence text = getText();
            int tokenStart = this.mTokenizer.findTokenStart(text, end);
            Handler handler = this.mHandler;
            handler.removeMessages(0);
            CharSequence token = text.subSequence(tokenStart, end);
            this.mScrollListener.setCurrentToken(token);
            handler.sendMessageDelayed(handler.obtainMessage(0, triggerCallback ? 1 : 0, 0, token), this.mThrottleDelay);
        }
    }

    @Override // android.widget.Filter.FilterListener
    public void onFilterComplete(int count) {
        int end = getSelectionEnd();
        int tokenStart = this.mTokenizer.findTokenStart(getText(), end);
        if (end - tokenStart >= this.mAttributeThreshold) {
            showDropDown();
        } else {
            dismissDropDown();
        }
    }

    public void onItemsFetched() {
        int end = getSelectionEnd();
        int tokenStart = this.mTokenizer.findTokenStart(getText(), end);
        if (end - tokenStart >= this.mAttributeThreshold) {
            this.mSimpleAdapter.notifyDataSetChanged();
            showDropDown();
        } else {
            dismissDropDown();
        }
    }

    @Override // android.widget.TextView
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);
        if (isPopupShowing()) {
            showDropDown();
        }
        return result;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!focused) {
            dismissDropDown();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            dismissDropDown();
        }
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissDropDown();
    }

    public void showDropDown() {
        if (getWindowVisibility() != 8) {
            int count = 0;
            if (this.mSimpleAdapter != null) {
                count = this.mSimpleAdapter.getCount();
            }
            if (count == 0) {
                dismissDropDown();
                this.mShowRequested = true;
                return;
            }
            PopupWindow popupWindow = this.mPopupWindow;
            if (!popupWindow.isShowing()) {
                int maxAvailableHeight = popupWindow.getMaxAvailableHeight(this);
                SLog.i("Popup max available height: {}", Integer.valueOf(maxAvailableHeight));
                popupWindow.setHeight(-1);
                popupWindow.setWidth(-1);
                setPopupWindowHeightToFitRemainingSpace();
                popupWindow.showAsDropDown(this, this.mAttributeDropDownHorizontalOffset, this.mAttributeDropDownVerticalOffset);
                boolean isAbove = popupWindow.isAboveAnchor();
                SLog.i("Popup is showing above: {}", Boolean.valueOf(isAbove));
                SLog.i("Popup width={}, height={}", Integer.valueOf(popupWindow.getWidth()), Integer.valueOf(popupWindow.getHeight()));
                this.mDropDownListView.setSelectionAfterHeaderView();
            }
        }
    }

    public void dismissDropDown() {
        this.mPopupWindow.dismiss();
        this.mShowRequested = false;
        this.mHandler.removeMessages(0);
    }

    public boolean clearSelectedEntity(int start, boolean usingBackspace) {
        VineEntity selectedEntity = this.mSelectedEntity;
        if (selectedEntity == null) {
            return false;
        }
        this.mSelectedEntity = null;
        Editable editable = getText();
        if (!TextUtils.isEmpty(editable)) {
            VineEntity.Range range = this.mEntityRanges.remove(Integer.valueOf(start));
            int spanStart = editable.getSpanStart(range.span);
            int spanEnd = editable.getSpanEnd(range.span);
            editable.removeSpan(range.span);
            if (usingBackspace) {
                editable.delete(spanStart, spanEnd);
            }
        }
        return true;
    }

    public ArrayList<VineEntity> getEntities() {
        Editable text = getText();
        if (text != null && text.length() > 0) {
            while (Character.isWhitespace(text.charAt(0))) {
                try {
                    text.replace(0, 1, "");
                } catch (IndexOutOfBoundsException e) {
                }
            }
        }
        ArrayList<VineEntity> entities = new ArrayList<>();
        TreeMap<Integer, VineEntity.Range> ranges = this.mEntityRanges;
        int adjustedRanges = 0;
        if (text != null) {
            for (int i = 0; i < text.length(); i++) {
                if (Character.isHighSurrogate(text.charAt(i)) || text.charAt(i) == 8206 || text.charAt(i) == 8207) {
                    Map.Entry<Integer, VineEntity.Range> entry = ranges.ceilingEntry(Integer.valueOf(i));
                    adjustedRanges++;
                    while (entry != null) {
                        VineEntity.Range range = entry.getValue();
                        int newStart = text.getSpanStart(range.span) - adjustedRanges;
                        int newEnd = text.getSpanEnd(range.span) - adjustedRanges;
                        range.entity.start = newStart;
                        range.entity.end = newEnd;
                        VineEntity.Range newRange = new VineEntity.Range(newEnd, range.entity, range.span);
                        ranges.remove(entry.getKey());
                        ranges.put(Integer.valueOf(newStart), newRange);
                        entry = ranges.ceilingEntry(Integer.valueOf(range.entity.end));
                    }
                }
            }
        }
        Iterator<VineEntity.Range> it = ranges.values().iterator();
        while (it.hasNext()) {
            entities.add(it.next().entity);
        }
        ranges.clear();
        return entities;
    }

    public void setText(String text, List<VineEntity> entities) {
        this.mEntityRanges.clear();
        setText(text);
        if (entities != null) {
            for (VineEntity entity : entities) {
                putEntity(entity.start, entity.end, entity);
            }
        }
    }

    public void setTextWithEntities(String text, List<VineEntity> entities) {
        this.mEntityRanges.clear();
        SpannableStringBuilder textSb = new SpannableStringBuilder(text);
        if (entities != null) {
            try {
                Util.adjustEntities(entities, textSb, 0, false);
                for (VineEntity entity : entities) {
                    if (entity.link != null) {
                        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.vine_green));
                        Util.safeSetSpan(textSb, span, entity.start, entity.end, 33);
                        this.mEntityRanges.put(Integer.valueOf(entity.start), new VineEntity.Range(entity.end, entity, span));
                    }
                }
            } catch (IndexOutOfBoundsException e) {
                CrashUtil.logException(e);
            }
        }
        textSb.append((CharSequence) " ");
        Editable editable = getText();
        editable.replace(0, editable.length(), textSb);
    }

    public void putEntity(int start, int end, VineEntity entity) {
        Editable editable = getText();
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.vine_green));
        SpannableStringBuilder ssb = new SpannableStringBuilder(entity.title);
        Util.safeSetSpan(ssb, span, 0, entity.end - entity.start, 0);
        editable.replace(start, end, ssb);
        this.mEntityRanges.put(Integer.valueOf(entity.start), new VineEntity.Range(entity.end, entity, span));
    }

    public void setAdapter(BaseAdapter adapter) {
        this.mDropDownListView.setAdapter((ListAdapter) adapter);
        this.mSimpleAdapter = adapter;
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == 4 && isPopupShowing()) {
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                getKeyDispatcherState().startTracking(event, this);
                return true;
            }
            if (event.getAction() == 1) {
                getKeyDispatcherState().handleUpEvent(event);
                if (event.isTracking() && !event.isCanceled()) {
                    dismissDropDown();
                    return true;
                }
            }
        } else if (keyCode == 4 && event != null && event.getAction() == 1 && this.mOnBackPressedListener != null && this.mOnBackPressedListener.onBackPressed()) {
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new PopupInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    @Override // android.widget.EditText, android.widget.TextView
    public boolean onTextContextMenuItem(int id) {
        if (id == 16908320 && this.mSelectedEntity != null) {
            clearSelectedEntity(this.mSelectedEntity.start, true);
        }
        return super.onTextContextMenuItem(id);
    }

    public void setPopupEditTextListener(PopupEditTextListener listener) {
        this.mHandler.setOnFilterListener(listener);
        this.mScrollListener.setOnFilterListener(listener);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener listener) {
        this.mOnClickListener = listener;
    }

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.mOnBackPressedListener = listener;
    }

    private void initialize() {
        this.mHandler = new FilterHandler(Looper.getMainLooper(), this);
        this.mEntityRanges = new TreeMap<>();
        setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.widget.PopupEditText.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (PopupEditText.this.isPopupShowing()) {
                    PopupEditText.this.mPopupWindow.setInputMethodMode(1);
                    PopupEditText.this.showDropDown();
                }
                if (PopupEditText.this.mOnClickListener != null) {
                    PopupEditText.this.mOnClickListener.onClick(view);
                }
            }
        });
        setCustomSelectionActionModeCallback(new ActionMode.Callback() { // from class: co.vine.android.widget.PopupEditText.2
            @Override // android.view.ActionMode.Callback
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override // android.view.ActionMode.Callback
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override // android.view.ActionMode.Callback
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override // android.view.ActionMode.Callback
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }

    private void atomizeEntity(int selStart, int selEnd) {
        VineEntity.Range range = lookupRange(selStart, selEnd);
        if (range != null && range.entity != null) {
            this.mSelectedEntity = range.entity;
            Editable editable = getText();
            int spanStart = editable.getSpanStart(range.span);
            int spanEnd = editable.getSpanEnd(range.span);
            if (spanEnd - spanStart > 0) {
                setSelection(spanStart, spanEnd);
                return;
            }
            return;
        }
        this.mSelectedEntity = null;
    }

    private VineEntity.Range lookupRange(int selStart, int selEnd) {
        TreeMap<Integer, VineEntity.Range> ranges = this.mEntityRanges;
        if (ranges != null && !ranges.isEmpty()) {
            Map.Entry<Integer, VineEntity.Range> entry = ranges.floorEntry(Integer.valueOf(selStart));
            if (entry == null) {
                entry = ranges.floorEntry(Integer.valueOf(selEnd));
            }
            if (entry != null && (selStart <= entry.getValue().upper || selEnd <= entry.getValue().upper)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void setPopupWindowHeightToFitRemainingSpace() {
        int[] location = new int[2];
        getLocationInWindow(location);
        this.mPopupWindow.setHeight(location[1]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPopupShowing() {
        return this.mPopupWindow.isShowing();
    }

    private void readStyles(Context context, AttributeSet attrs, int defStyle) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PopupEditText, defStyle, 0);
        this.mAttributeDropDownVerticalOffset = attributes.getDimensionPixelOffset(0, 0);
        this.mAttributeDropDownHorizontalOffset = attributes.getDimensionPixelOffset(1, 0);
        this.mAttributeThreshold = attributes.getInteger(2, 0);
        attributes.recycle();
    }

    private PopupWindow createPopupWindow(Context context, AttributeSet attrs) {
        PopupWindow popupWindow = new PopupWindow(context, attrs, R.attr.popupEditTextStyle);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setWidth(-1);
        popupWindow.setHeight(0);
        popupWindow.setInputMethodMode(1);
        popupWindow.setSoftInputMode(16);
        return popupWindow;
    }

    private ViewGroup createContentContainer(Context context) {
        LinearLayout contentContainer = new LinearLayout(context);
        contentContainer.setOrientation(1);
        contentContainer.setBackgroundColor(0);
        return contentContainer;
    }

    private View createView(Context context, int height, int color) {
        View topBorder = new View(context);
        topBorder.setLayoutParams(new LinearLayout.LayoutParams(-1, height));
        topBorder.setBackgroundColor(color);
        return topBorder;
    }

    private DropDownListView createDropDownListView(Context context) throws Resources.NotFoundException {
        int dividerHeightPx = getResources().getDimensionPixelSize(R.dimen.divider_thickness);
        DropDownListView listView = new DropDownListView(context, null, R.attr.popupEditListStyle);
        listView.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, 1.0f));
        listView.setBackgroundColor(-1);
        listView.setOverScrollMode(2);
        listView.setDividerHeight(dividerHeightPx);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: co.vine.android.widget.PopupEditText.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PopupEditText.this.clearComposingText();
                if (adapterView.getItemAtPosition(position) instanceof VineTypeAhead) {
                    VineTypeAhead item = (VineTypeAhead) adapterView.getItemAtPosition(position);
                    Editable editable = PopupEditText.this.getText();
                    int end = PopupEditText.this.getSelectionEnd();
                    int tokenStart = PopupEditText.this.mTokenizer.findTokenStart(editable, end);
                    int tokenEnd = tokenStart + item.token.length();
                    VineEntity entity = new VineEntity(item.type, item.token, null, tokenStart, tokenEnd, item.id);
                    SpannableStringBuilder ssb = new SpannableStringBuilder(item.token);
                    ForegroundColorSpan span = new ForegroundColorSpan(PopupEditText.this.getResources().getColor(R.color.vine_green));
                    Util.safeSetSpan(ssb, span, 0, item.token.length(), 0);
                    editable.replace(tokenStart, end, ssb);
                    PopupEditText.this.mEntityRanges.put(Integer.valueOf(tokenStart), new VineEntity.Range(tokenEnd, entity, span));
                    PopupEditText.this.append(" ");
                    PopupEditText.this.dismissDropDown();
                }
            }
        });
        return listView;
    }

    private class PopupInputConnection extends InputConnectionWrapper {
        public PopupInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
        public boolean sendKeyEvent(KeyEvent event) {
            if (PopupEditText.this.mSelectedEntity == null || event.getAction() != 0 || event.getKeyCode() != 67) {
                return super.sendKeyEvent(event);
            }
            PopupEditText.this.clearSelectedEntity(PopupEditText.this.mSelectedEntity.start, true);
            return true;
        }

        @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            return (PopupEditText.this.mSelectedEntity == null || beforeLength <= 0 || afterLength != 0) ? super.deleteSurroundingText(beforeLength, afterLength) : sendKeyEvent(new KeyEvent(0, 67));
        }
    }

    private static class DropDownListView extends SdkListView {
        public DropDownListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override // android.view.View
        public boolean hasWindowFocus() {
            return true;
        }

        @Override // android.view.View
        public boolean isFocused() {
            return true;
        }

        @Override // android.view.ViewGroup, android.view.View
        public boolean hasFocus() {
            return true;
        }
    }

    private static final class ScrollListener extends ArrayListScrollListener {
        private CharSequence mCurrentToken;
        private PopupEditTextListener mListener;

        private ScrollListener() {
        }

        void setOnFilterListener(PopupEditTextListener listener) {
            this.mListener = listener;
        }

        public void setCurrentToken(CharSequence currentToken) {
            this.mCurrentToken = currentToken;
        }

        @Override // co.vine.android.feedadapter.ArrayListScrollListener
        protected void onScrollLastItem(int totalItemCount) {
            super.onScrollLastItem(totalItemCount);
            if (this.mListener != null) {
                this.mListener.onItemsExhausted(this.mCurrentToken);
            }
        }
    }

    private static class FilterHandler extends Handler {
        private Filterable mFilterable;
        private final WeakReference<Filter.FilterListener> mListenerRef;
        private PopupEditTextListener mOnFilterListener;

        public FilterHandler(Looper looper, Filter.FilterListener listener) {
            super(looper);
            this.mListenerRef = new WeakReference<>(listener);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Filter.FilterListener listener = this.mListenerRef.get();
            if (listener != null) {
                CharSequence token = (CharSequence) msg.obj;
                if (this.mFilterable != null) {
                    this.mFilterable.getFilter().filter(token, listener);
                }
                if (msg.arg1 == 1 && this.mOnFilterListener != null) {
                    this.mOnFilterListener.onFiltering(token);
                }
            }
        }

        public void setFilterable(Filterable filterable) {
            this.mFilterable = filterable;
        }

        public void setOnFilterListener(PopupEditTextListener listener) {
            this.mOnFilterListener = listener;
        }
    }
}
