package co.vine.android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import co.vine.android.api.FoursquareVenue;
import co.vine.android.client.AppSessionListener;
import co.vine.android.share.widgets.FakeActionBar;
import co.vine.android.util.CommonUtil;
import co.vine.android.util.ViewUtil;
import co.vine.android.views.SimpleTextWatcher;
import co.vine.android.widget.TypefacesEditText;
import co.vine.android.widget.TypefacesTextView;
import co.vine.android.widgets.PromptDialogSupportFragment;
import com.edisonwang.android.slog.SLog;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twitter.android.widget.RefreshableListView;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class LocationSearchFragment extends BaseArrayListFragment {
    private PromptDialogSupportFragment.OnDialogDoneListener mDialogDoneListener;
    private View mEmptyView;
    private ProgressBar mEmptyViewProgressBar;
    private FakeActionBar mFakeActionBar;
    private ImageView mFakeActionBarBackButton;
    private ImageView mFakeActionBarCancelView;
    private TypefacesEditText mFakeActionBarQuery;
    private ImageView mFakeActionBarSearchView;
    private TypefacesTextView mFakeActionBarTitle;
    private Location mLocation;
    private LocationHelper mLocationHelper;
    private GoogleMap mMap;
    private boolean mMapEnabled = true;
    private MapView mMapView;
    private Handler mTypeaheadHandler;

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDialogDoneListener = createDialogDoneListener();
        setAppSessionListener(createAppSessionListener());
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = createView(inflater, R.layout.location_search, container);
        this.mFakeActionBar = (FakeActionBar) view.findViewById(R.id.fake_action_bar);
        this.mFakeActionBarBackButton = (ImageButton) this.mFakeActionBar.inflateBackView(R.layout.fake_action_bar_back_arrow);
        this.mEmptyView = view.findViewById(R.id.list_view_empty);
        this.mEmptyViewProgressBar = (ProgressBar) this.mEmptyView.findViewById(R.id.progress_bar);
        this.mMapView = (MapView) view.findViewById(R.id.map_view);
        return view;
    }

    @Override // co.vine.android.BaseArrayListFragment, android.support.v4.app.Fragment
    public void onViewCreated(View view, Bundle savedInstanceState) throws PackageManager.NameNotFoundException {
        Activity activity = getActivity();
        initializeFakeActionBar();
        initializeMapView(activity, savedInstanceState);
        initializeListView(activity);
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, co.vine.android.BaseFragment, android.support.v4.app.Fragment
    public void onResume() {
        super.onResume();
        this.mMapView.onResume();
        this.mTypeaheadHandler = new Handler(new TypeaheadHandler());
        this.mLocationHelper = new LocationHelper(getActivity(), new Handler(new LocationHandler()));
        if (!this.mLocationHelper.isLocationEnabled()) {
            PromptDialogSupportFragment dialog = createEnableLocationProviderDialog(this.mDialogDoneListener);
            dialog.show(getFragmentManager());
        }
        this.mLocation = this.mLocationHelper.getLocation();
        if (this.mLocation != null) {
            updateMap();
            doSearch();
        }
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, co.vine.android.BaseControllerFragment, android.support.v4.app.Fragment
    public void onPause() {
        super.onPause();
        this.mMapView.onPause();
        if (this.mLocationHelper != null) {
            this.mLocationHelper.disable();
            this.mLocationHelper = null;
        }
    }

    @Override // android.support.v4.app.Fragment
    public void onDestroy() {
        this.mMapView.onDestroy();
        super.onDestroy();
    }

    @Override // android.support.v4.app.Fragment, android.content.ComponentCallbacks
    public void onLowMemory() {
        this.mMapView.onLowMemory();
        super.onLowMemory();
    }

    @Override // co.vine.android.BaseArrayListFragment, co.vine.android.BaseAdapterFragment, android.support.v4.app.Fragment
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mMapView.onSaveInstanceState(outState);
    }

    @Override // co.vine.android.BaseArrayListFragment
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Activity activity = getActivity();
        if (activity != null) {
            Intent result = new Intent();
            FoursquareVenue venue = (FoursquareVenue) listView.getItemAtPosition(position);
            result.putExtra("foursquare_venue", venue.getName());
            result.putExtra("foursquare_venue_id", venue.getVenueId());
            activity.setResult(-1, result);
            this.mLocationHelper.disable();
            this.mLocationHelper = null;
            activity.finish();
        }
    }

    private void initializeFakeActionBar() {
        ViewGroup customContent = (ViewGroup) this.mFakeActionBar.findViewById(R.id.custom_content);
        this.mFakeActionBarTitle = (TypefacesTextView) customContent.findViewById(R.id.title);
        this.mFakeActionBarQuery = (TypefacesEditText) customContent.findViewById(R.id.query);
        ViewGroup customAction = (ViewGroup) this.mFakeActionBar.findViewById(R.id.custom_action);
        this.mFakeActionBarSearchView = (ImageView) customAction.findViewById(R.id.search);
        this.mFakeActionBarCancelView = (ImageView) customAction.findViewById(R.id.cancel);
        this.mFakeActionBarBackButton.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LocationSearchFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LocationSearchFragment.this.getActivity().finish();
            }
        });
        this.mFakeActionBar.setBackView(this.mFakeActionBarBackButton);
        this.mFakeActionBarTitle.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LocationSearchFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ViewUtil.disableAndHide(LocationSearchFragment.this.mFakeActionBarTitle, LocationSearchFragment.this.mFakeActionBarSearchView);
                ViewUtil.enableAndShow(LocationSearchFragment.this.mFakeActionBarQuery, LocationSearchFragment.this.mFakeActionBarCancelView);
                LocationSearchFragment.this.mFakeActionBarQuery.requestFocus();
                CommonUtil.setSoftKeyboardVisibility(LocationSearchFragment.this.getActivity(), LocationSearchFragment.this.mFakeActionBarQuery, true);
            }
        });
        this.mFakeActionBarQuery.addTextChangedListener(new SimpleTextWatcher() { // from class: co.vine.android.LocationSearchFragment.3
            @Override // co.vine.android.views.SimpleTextWatcher, android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String searchText = LocationSearchFragment.this.mFakeActionBarQuery.getText().toString().trim();
                if (CommonUtil.isNullOrWhitespace(searchText)) {
                    if (LocationSearchFragment.this.mMapEnabled) {
                        ViewUtil.enableAndShow(LocationSearchFragment.this.mMapView);
                    }
                } else {
                    ViewUtil.disableAndHide(LocationSearchFragment.this.mMapView);
                }
                LocationSearchFragment.this.mTypeaheadHandler.removeMessages(0);
                Message message = Message.obtain(LocationSearchFragment.this.mTypeaheadHandler, 0);
                LocationSearchFragment.this.mTypeaheadHandler.sendMessageDelayed(message, 150L);
            }
        });
        this.mFakeActionBarQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: co.vine.android.LocationSearchFragment.4
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != 3) {
                    return false;
                }
                CommonUtil.setSoftKeyboardVisibility(LocationSearchFragment.this.getActivity(), LocationSearchFragment.this.mFakeActionBarQuery, false);
                return true;
            }
        });
        this.mFakeActionBarSearchView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LocationSearchFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ViewUtil.disableAndHide(LocationSearchFragment.this.mFakeActionBarTitle, LocationSearchFragment.this.mFakeActionBarSearchView);
                ViewUtil.enableAndShow(LocationSearchFragment.this.mFakeActionBarQuery, LocationSearchFragment.this.mFakeActionBarCancelView);
                LocationSearchFragment.this.mFakeActionBarQuery.requestFocus();
                CommonUtil.setSoftKeyboardVisibility(LocationSearchFragment.this.getActivity(), LocationSearchFragment.this.mFakeActionBarQuery, true);
            }
        });
        this.mFakeActionBarCancelView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.LocationSearchFragment.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                LocationSearchFragment.this.mFakeActionBarQuery.setText((CharSequence) null);
                ViewUtil.disableAndHide(LocationSearchFragment.this.mFakeActionBarQuery, LocationSearchFragment.this.mFakeActionBarCancelView);
                ViewUtil.enableAndShow(LocationSearchFragment.this.mFakeActionBarTitle, LocationSearchFragment.this.mFakeActionBarSearchView);
            }
        });
    }

    private void initializeMapView(Activity activity, Bundle savedInstanceState) throws PackageManager.NameNotFoundException {
        this.mMapView.onCreate(savedInstanceState);
        this.mMap = this.mMapView.getMap();
        if (this.mMap == null) {
            this.mMapEnabled = false;
            ViewUtil.disableAndHide(this.mMapView);
        } else {
            this.mMap.getUiSettings().setMyLocationButtonEnabled(false);
            this.mMap.setMyLocationEnabled(true);
            this.mMap.getUiSettings().setZoomControlsEnabled(false);
            this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() { // from class: co.vine.android.LocationSearchFragment.7
                @Override // com.google.android.gms.maps.GoogleMap.OnMapClickListener
                public void onMapClick(LatLng coordinates) {
                    LocationSearchFragment.this.mMap.clear();
                    LocationSearchFragment.this.updateMap(coordinates.latitude, coordinates.longitude, false);
                    LocationSearchFragment.this.doSearch(coordinates.latitude, coordinates.longitude);
                }
            });
        }
        try {
            MapsInitializer.initialize(activity);
        } catch (Exception e) {
            SLog.e("Exception while trying to acquire map", (Throwable) e);
        }
    }

    private void initializeListView(Activity activity) {
        ImageView emptyViewFoursquareLogo = (ImageView) this.mEmptyView.findViewById(R.id.empty_view_foursquare_logo);
        emptyViewFoursquareLogo.setColorFilter(getResources().getColor(R.color.white_twenty_percent), PorterDuff.Mode.SRC_IN);
        if (this.mRefreshable && (this.mListView instanceof RefreshableListView)) {
            RefreshableListView refreshableListView = (RefreshableListView) this.mListView;
            refreshableListView.disablePTR();
        }
        this.mAdapter = new FoursquareVenueAdapter(activity);
        this.mListView.setAdapter((ListAdapter) this.mAdapter);
        this.mListView.setEmptyView(this.mEmptyView);
        this.mListView.addFooterView(createFooterView(activity), null, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSearch() {
        if (this.mLocation != null) {
            doSearch(this.mLocation.getLatitude(), this.mLocation.getLongitude());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doSearch(double latitude, double longitude) {
        this.mAppController.fetchNearbyLocations(latitude, longitude, this.mFakeActionBarQuery.getText().toString().trim());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMap() {
        if (this.mLocation != null) {
            updateMap(this.mLocation.getLatitude(), this.mLocation.getLongitude(), true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMap(double latitude, double longitude, boolean animate) {
        if (this.mMap != null) {
            LatLng coordinates = new LatLng(latitude, longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(coordinates);
            this.mMap.addMarker(markerOptions);
            if (animate) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinates, 15.0f);
                this.mMap.animateCamera(cameraUpdate);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ArrayList<FoursquareVenue> filterLocations(ArrayList<FoursquareVenue> locations) {
        ArrayList<FoursquareVenue> venues = new ArrayList<>();
        Iterator<FoursquareVenue> it = locations.iterator();
        while (it.hasNext()) {
            FoursquareVenue venue = it.next();
            if (!TextUtils.isEmpty(venue.getName())) {
                venues.add(venue);
            }
        }
        return venues;
    }

    private AppSessionListener createAppSessionListener() {
        return new AppSessionListener() { // from class: co.vine.android.LocationSearchFragment.8
            @Override // co.vine.android.client.AppSessionListener
            public void onFoursquareLocationFetchComplete(int responseCode, ArrayList<FoursquareVenue> response) {
                Activity activity = LocationSearchFragment.this.getActivity();
                if (activity != null) {
                    LocationSearchFragment.this.mEmptyViewProgressBar.setVisibility(8);
                    if (responseCode != 200) {
                        PromptDialogSupportFragment dialog = LocationSearchFragment.this.createUnknownErrorDialog(LocationSearchFragment.this.mDialogDoneListener);
                        dialog.show(LocationSearchFragment.this.getFragmentManager());
                    } else {
                        ArrayList<FoursquareVenue> filteredVenues = LocationSearchFragment.this.filterLocations(response);
                        ((FoursquareVenueAdapter) LocationSearchFragment.this.mAdapter).clear();
                        ((FoursquareVenueAdapter) LocationSearchFragment.this.mAdapter).addAll(filteredVenues);
                        LocationSearchFragment.this.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
    }

    private PromptDialogSupportFragment.OnDialogDoneListener createDialogDoneListener() {
        return new PromptDialogSupportFragment.OnDialogDoneListener() { // from class: co.vine.android.LocationSearchFragment.9
            @Override // co.vine.android.widgets.PromptDialogFragment.OnDialogDoneListener
            public void onDialogDone(DialogInterface dialog, int id, int which) {
                Activity activity = LocationSearchFragment.this.getActivity();
                if (activity != null) {
                    switch (id) {
                        case 0:
                            switch (which) {
                                case -2:
                                    activity.finish();
                                    break;
                                case -1:
                                    LocationSearchFragment.this.startActivity(new Intent("android.settings.SETTINGS"));
                                    activity.finish();
                                    break;
                            }
                        case 1:
                            activity.finish();
                            break;
                    }
                }
            }
        };
    }

    private View createFooterView(Context context) throws Resources.NotFoundException {
        int foursquareFooterPadding = context.getResources().getDimensionPixelSize(R.dimen.foursquare_footer_padding);
        ImageView view = new ImageView(context);
        view.setImageResource(R.drawable.ic_foursquare);
        view.setColorFilter(getResources().getColor(R.color.white_twenty_percent), PorterDuff.Mode.SRC_IN);
        view.setBackground(new ColorDrawable(ViewCompat.MEASURED_STATE_MASK));
        view.setPadding(0, foursquareFooterPadding, 0, foursquareFooterPadding);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PromptDialogSupportFragment createUnknownErrorDialog(PromptDialogSupportFragment.OnDialogDoneListener listener) {
        PromptDialogSupportFragment dialog = PromptDialogSupportFragment.newInstance(1);
        dialog.setListener(listener);
        dialog.setMessage(R.string.API_ERROR_UNKNOWN_ERROR);
        dialog.setPositiveButton(R.string.ok);
        return dialog;
    }

    public PromptDialogSupportFragment createEnableLocationProviderDialog(PromptDialogSupportFragment.OnDialogDoneListener listener) {
        PromptDialogSupportFragment dialog = PromptDialogSupportFragment.newInstance(0);
        dialog.setListener(listener);
        dialog.setMessage(R.string.gps_enable);
        dialog.setPositiveButton(R.string.yes);
        dialog.setNegativeButton(R.string.no);
        dialog.setCancelebleOnOutisde(false);
        return dialog;
    }

    private class TypeaheadHandler implements Handler.Callback {
        private TypeaheadHandler() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what == 0) {
                LocationSearchFragment.this.doSearch();
                return true;
            }
            return true;
        }
    }

    private class LocationHandler implements Handler.Callback {
        private LocationHandler() {
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what == 0) {
                String searchText = LocationSearchFragment.this.mFakeActionBarQuery.getText().toString().trim();
                if (!CommonUtil.isNullOrWhitespace(searchText)) {
                    LocationSearchFragment.this.updateMap();
                    LocationSearchFragment.this.doSearch();
                    return true;
                }
                return true;
            }
            return true;
        }
    }
}
