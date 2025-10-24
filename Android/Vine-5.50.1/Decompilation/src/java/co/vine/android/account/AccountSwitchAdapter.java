package co.vine.android.account;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import co.vine.android.R;
import co.vine.android.client.AppController;
import co.vine.android.client.Session;
import co.vine.android.client.SessionManager;
import co.vine.android.util.ResourceLoader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: classes.dex */
public class AccountSwitchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final AccountSwitchCallback mCallback;
    private final Context mContext;
    private final ResourceLoader mResourceLoader;
    private final SessionManager mSessionManager = SessionManager.getSharedInstance();
    private final List<Session> mSessions = this.mSessionManager.getSessions();

    public interface AccountSwitchCallback {
        void onSwitchAccounts();
    }

    public AccountSwitchAdapter(Context context, AppController appController, AccountSwitchCallback callback) {
        this.mContext = context;
        this.mCallback = callback;
        this.mResourceLoader = new ResourceLoader(this.mContext, appController);
        Collections.sort(this.mSessions, new Comparator<Session>() { // from class: co.vine.android.account.AccountSwitchAdapter.1
            @Override // java.util.Comparator
            public int compare(Session lhs, Session rhs) {
                String lhScreenName = lhs.getScreenName();
                String rhScreenName = rhs.getScreenName();
                if (lhScreenName != null && rhScreenName != null) {
                    return lhScreenName.compareTo(rhScreenName);
                }
                if (lhScreenName != null) {
                    return -1;
                }
                if (rhScreenName != null) {
                    return 1;
                }
                return 0;
            }
        });
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        switch (viewType) {
            case 1:
                View v = inflater.inflate(R.layout.account_switch_add_new, parent, false);
                return new AddAccountViewHolder(v);
            default:
                View v2 = inflater.inflate(R.layout.account_switch_menu_item, parent, false);
                return new SessionViewHolder(v2);
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0:
                final SessionViewHolder sessionHolder = (SessionViewHolder) holder;
                final Session session = this.mSessions.get(position);
                this.mResourceLoader.setImageWhenLoaded(new ResourceLoader.ImageViewImageSetter(sessionHolder.avatar), session.getAvatarUrl(), true);
                sessionHolder.screenName.setText(session.getScreenName());
                if (session.equals(this.mSessionManager.getCurrentSession())) {
                    sessionHolder.check.setVisibility(0);
                } else {
                    sessionHolder.check.setVisibility(8);
                }
                sessionHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.account.AccountSwitchAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        if (!session.equals(AccountSwitchAdapter.this.mSessionManager.getCurrentSession())) {
                            AccountSwitchAdapter.this.mSessionManager.setCurrentSession((Session) AccountSwitchAdapter.this.mSessions.get(sessionHolder.getAdapterPosition()));
                            AccountSwitchAdapter.this.mCallback.onSwitchAccounts();
                        }
                    }
                });
                break;
            case 1:
                ((AddAccountViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() { // from class: co.vine.android.account.AccountSwitchAdapter.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        AccountSwitchAdapter.this.mSessionManager.onAddSession(AccountSwitchAdapter.this.mContext);
                    }
                });
                break;
        }
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mSessions.size() + 1;
    }

    @Override // android.support.v7.widget.RecyclerView.Adapter
    public int getItemViewType(int position) {
        return position == this.mSessions.size() ? 1 : 0;
    }

    public class SessionViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatar;
        public View check;
        public TextView screenName;

        public SessionViewHolder(View v) {
            super(v);
            this.avatar = (ImageView) v.findViewById(R.id.avatar);
            this.screenName = (TextView) v.findViewById(R.id.screen_name);
            this.check = v.findViewById(R.id.check_icon);
        }
    }

    public class AddAccountViewHolder extends RecyclerView.ViewHolder {
        public AddAccountViewHolder(View v) {
            super(v);
        }
    }
}
