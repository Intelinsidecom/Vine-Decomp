package io.realm.rx;

/* loaded from: classes.dex */
public class RealmObservableFactory implements RxObservableFactory {
    private boolean rxJavaAvailble;

    public RealmObservableFactory() throws ClassNotFoundException {
        try {
            Class.forName("rx.Observable");
            this.rxJavaAvailble = true;
        } catch (ClassNotFoundException e) {
            this.rxJavaAvailble = false;
        }
    }

    public boolean equals(Object o) {
        return o instanceof RealmObservableFactory;
    }

    public int hashCode() {
        return 37;
    }
}
