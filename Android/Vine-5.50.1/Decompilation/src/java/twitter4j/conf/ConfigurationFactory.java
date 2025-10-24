package twitter4j.conf;

/* loaded from: classes.dex */
public interface ConfigurationFactory {
    void dispose();

    Configuration getInstance();

    Configuration getInstance(String str);
}
