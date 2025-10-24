package co.vine.android.digits;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import co.vine.android.R;
import co.vine.android.util.ClientFlagsHelper;

/* loaded from: classes.dex */
public class ClientFlagsAdapter extends ArrayAdapter<String> {
    private final Context mContext;
    private final String[] mValues;

    public ClientFlagsAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.mContext = context;
        this.mValues = values;
    }

    static class ViewHolder {
        public Switch mSwitch;

        ViewHolder() {
        }
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(this.mContext).inflate(R.layout.row_flag_switch, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.mSwitch = (Switch) rowView.findViewById(R.id.flag_switch);
            rowView.setTag(viewHolder);
        }
        ViewHolder viewHolder2 = (ViewHolder) rowView.getTag();
        viewHolder2.mSwitch.setText(this.mValues[position]);
        viewHolder2.mSwitch.setOnCheckedChangeListener(null);
        viewHolder2.mSwitch.setChecked(ClientFlagsHelper.getOverrideClientFlagValue(this.mContext, this.mValues[position]));
        viewHolder2.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: co.vine.android.digits.ClientFlagsAdapter.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ClientFlagsHelper.updateOverrideClientFlagValue(ClientFlagsAdapter.this.mContext, buttonView.getText().toString(), buttonView.isChecked());
            }
        });
        return rowView;
    }
}
