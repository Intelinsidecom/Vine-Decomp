package co.vine.android;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import co.vine.android.api.ComplaintMenuOption;
import co.vine.android.util.ViewUtil;

/* loaded from: classes.dex */
public class ComplaintChoiceAdapter extends ArrayAdapter<ComplaintMenuOption.ComplaintChoice> {
    private ComplaintMenuOption.ComplaintChoice[] mChoices;
    private Context mContext;
    private int mLayoutId;

    public ComplaintChoiceAdapter(Context context, int layoutResourceId, ComplaintMenuOption.ComplaintChoice[] data) {
        super(context, layoutResourceId, data);
        this.mLayoutId = layoutResourceId;
        this.mContext = context;
        this.mChoices = data;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) throws Resources.NotFoundException {
        ComplaintMenuOption.ComplaintChoice complaintChoice = this.mChoices[position];
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) this.mContext).getLayoutInflater();
            convertView = inflater.inflate(this.mLayoutId, parent, false);
        }
        TextView title = (TextView) convertView.findViewById(R.id.title);
        View selectionIndicator = convertView.findViewById(R.id.selection_indicator);
        if (complaintChoice.selected) {
            setSelected(this.mContext, selectionIndicator, true, this.mContext.getResources().getColor(R.color.vine_green));
        } else {
            setSelected(this.mContext, selectionIndicator, false, -1);
        }
        title.setText(complaintChoice.title);
        return convertView;
    }

    public static void setSelected(Context context, View selectionIndicator, boolean selected, int color) throws Resources.NotFoundException {
        selectionIndicator.setSelected(selected);
        Resources res = context.getResources();
        if (selected) {
            Drawable bg = res.getDrawable(R.drawable.ic_circle_selected).mutate();
            bg.setColorFilter(new PorterDuffColorFilter((-16777216) | color, PorterDuff.Mode.SRC_IN));
            ViewUtil.setBackground(selectionIndicator, bg);
        } else {
            Drawable bg2 = res.getDrawable(R.drawable.ic_circle_default);
            bg2.setColorFilter(new PorterDuffColorFilter(res.getColor(R.color.btn_circle_stroke), PorterDuff.Mode.SRC_IN));
            ViewUtil.setBackground(selectionIndicator, bg2);
        }
    }
}
