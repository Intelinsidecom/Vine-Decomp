package twitter4j.internal.json;

import twitter4j.conf.Configuration;

/* loaded from: classes.dex */
public class z_T4JInternalJSONImplFactory implements z_T4JInternalFactory {
    private static final long serialVersionUID = 5217622295050444866L;
    private Configuration conf;

    public z_T4JInternalJSONImplFactory(Configuration conf) {
        this.conf = conf;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof z_T4JInternalJSONImplFactory)) {
            return false;
        }
        z_T4JInternalJSONImplFactory that = (z_T4JInternalJSONImplFactory) o;
        if (this.conf != null) {
            if (this.conf.equals(that.conf)) {
                return true;
            }
        } else if (that.conf == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.conf != null) {
            return this.conf.hashCode();
        }
        return 0;
    }

    public String toString() {
        return "JSONImplFactory{conf=" + this.conf + '}';
    }
}
