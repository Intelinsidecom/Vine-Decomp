package co.vine.android;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.api.FoursquareVenue;
import java.util.Map;

/* loaded from: classes.dex */
public final class FoursquareVenueAdapter extends ArrayAdapter<FoursquareVenue> {
    private final LayoutInflater mLayoutInflater;

    public FoursquareVenueAdapter(Context context) {
        super(context, R.layout.location);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        FoursquareVenueTag tag;
        if (convertView == null) {
            convertView = this.mLayoutInflater.inflate(R.layout.location, parent, false);
            tag = new FoursquareVenueTag(convertView);
            convertView.setTag(tag);
        } else {
            tag = (FoursquareVenueTag) convertView.getTag();
        }
        tag.bind(getItem(position));
        return convertView;
    }

    private static class FoursquareVenueTag {
        private static Map<String, Integer> sIcons;
        private ImageView categoryIcon;
        private TextView venueAddress;
        private TextView venueName;

        private FoursquareVenueTag(View view) {
            this.venueName = (TextView) view.findViewById(R.id.foursquare_location_name);
            this.venueAddress = (TextView) view.findViewById(R.id.foursquare_location_address);
            this.categoryIcon = (ImageView) view.findViewById(R.id.location_row_icon);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void bind(FoursquareVenue venue) {
            String address = venue.getAddress();
            String name = venue.getName();
            TextView textView = this.venueName;
            if (TextUtils.isEmpty(name)) {
                name = address;
            }
            textView.setText(name);
            this.venueAddress.setText(address);
            this.categoryIcon.setImageResource(getLocationDrawable(venue.getCategory()));
        }

        private int getLocationDrawable(FoursquareVenue.Category category) {
            if (category == null) {
                return R.drawable.ic_location_home;
            }
            Integer shortNameResId = getDrawableResourceId(category.getShortName());
            if (shortNameResId != null) {
                return shortNameResId.intValue();
            }
            Integer leafResId = getDrawableResourceId(category.getLeafName());
            if (leafResId != null) {
                return leafResId.intValue();
            }
            Integer parentResId = getDrawableResourceId(category.getParentName());
            return parentResId != null ? parentResId.intValue() : R.drawable.ic_location_home;
        }

        private Integer getDrawableResourceId(String input) {
            if (sIcons == null) {
                sIcons = new ArrayMap(13);
                sIcons.put("arts_entertainment", Integer.valueOf(R.drawable.ic_location_theater));
                sIcons.put("education", Integer.valueOf(R.drawable.ic_location_school));
                sIcons.put("event", Integer.valueOf(R.drawable.ic_location_club));
                sIcons.put("food", Integer.valueOf(R.drawable.ic_location_restaurant));
                sIcons.put("restaurant", Integer.valueOf(R.drawable.ic_location_restaurant));
                sIcons.put("home", Integer.valueOf(R.drawable.ic_location_home));
                sIcons.put("nightlife", Integer.valueOf(R.drawable.ic_location_club));
                sIcons.put("parks_outdoor", Integer.valueOf(R.drawable.ic_location_park));
                sIcons.put("building", Integer.valueOf(R.drawable.ic_location_office));
                sIcons.put("shops", Integer.valueOf(R.drawable.ic_location_retail));
                sIcons.put("shop", Integer.valueOf(R.drawable.ic_location_retail));
                sIcons.put("travel", Integer.valueOf(R.drawable.ic_location_passport));
                sIcons.put("tech startup", Integer.valueOf(R.drawable.ic_location_office));
            }
            if (TextUtils.isEmpty(input)) {
                return null;
            }
            if (input.charAt(input.length() - 1) == '_') {
                input = input.substring(0, input.length() - 1);
            }
            return sIcons.get(input.toLowerCase());
        }
    }
}
