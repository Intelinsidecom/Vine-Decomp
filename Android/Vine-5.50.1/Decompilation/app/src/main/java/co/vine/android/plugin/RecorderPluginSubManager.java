package co.vine.android.plugin;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import co.vine.android.recorder.BasicVineRecorder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class RecorderPluginSubManager<T extends View, K extends BasicVineRecorder> extends BaseRecorderPluginManager<K> implements RecorderPlugin<T, K> {
    private WeakReference<Fragment> mFragment;
    public int mId;
    private RecorderPluginManager<K> mParent;
    private WeakReference<T> mView;
    private final String name;
    private final ArrayList<RecorderPlugin<?, K>> plugins;

    public abstract T onLayout(ViewGroup viewGroup, LayoutInflater layoutInflater, Fragment fragment);

    public RecorderPluginSubManager(String name, ArrayList<RecorderPlugin<?, K>> plugins) {
        this.name = name;
        this.plugins = plugins;
    }

    @Override // co.vine.android.plugin.BaseRecorderPluginManager, co.vine.android.plugin.RecorderPluginManager
    public K getRecorder() {
        return (K) this.mParent.getRecorder();
    }

    @Override // co.vine.android.plugin.Plugin
    public int onAdded(PluginManager manager, int id) {
        super.addPlugins(id, this.plugins);
        this.mId = this.mNextPluginId;
        this.mParent = (RecorderPluginManager) manager;
        return (this.mId - id) + 1;
    }

    @Override // co.vine.android.plugin.Plugin
    public String getName() {
        return this.name;
    }

    @Override // co.vine.android.plugin.Plugin
    public int getId() {
        return this.mId;
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void createLayout(ViewGroup parent, LayoutInflater inflater, Fragment fragment) {
        this.mFragment = new WeakReference<>(fragment);
        View viewOnLayout = onLayout(parent, inflater, fragment);
        if (viewOnLayout != null) {
            parent.addView(viewOnLayout);
            this.mView = new WeakReference<>(viewOnLayout);
        } else {
            this.mView = null;
        }
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public void onRecorderSet(BasicVineRecorder recorder) {
    }

    @Override // co.vine.android.plugin.RecorderPlugin
    public boolean onBackButtonPressed(boolean isEditing) {
        Iterator<RecorderPlugin<?, K>> it = this.plugins.iterator();
        while (it.hasNext()) {
            RecorderPlugin plugin = it.next();
            if (plugin.onBackButtonPressed(isEditing)) {
                return true;
            }
        }
        return false;
    }
}
