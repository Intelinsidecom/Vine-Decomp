package co.vine.android.feedadapter.viewholder;

import android.view.View;
import co.vine.android.R;
import co.vine.android.feedadapter.v2.ViewType;
import co.vine.android.widget.CounterView;

/* loaded from: classes.dex */
public final class LoopCounterViewHolder implements ViewHolder {
    public CounterView counter;
    public View onFire;
    public View plus;

    public LoopCounterViewHolder(View view) {
        this.onFire = view.findViewById(R.id.fire);
        this.counter = (CounterView) view.findViewById(R.id.loopCount);
        this.plus = view.findViewById(R.id.loops_plus);
    }

    @Override // co.vine.android.feedadapter.viewholder.ViewHolder
    public ViewType getType() {
        return ViewType.LOOP_COUNTER;
    }
}
