package co.vine.android;

import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import co.vine.android.digits.ClientFlagsAdapter;
import co.vine.android.util.ClientFlagsHelper;

/* loaded from: classes.dex */
public class ClientFlagsOverrideActivity extends BaseControllerActionBarActivity {
    ListView mListView;

    @Override // co.vine.android.BaseActionBarActivity, android.support.v7.app.AppCompatActivity, android.support.v4.app.FragmentActivity, android.support.v4.app.BaseFragmentActivityDonut, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_view, true);
        this.mListView = (ListView) findViewById(R.id.list);
        ClientFlagsAdapter adapter = new ClientFlagsAdapter(this, ClientFlagsHelper.overrideFlags);
        this.mListView.setAdapter((ListAdapter) adapter);
    }
}
