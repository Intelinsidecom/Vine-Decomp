package twitter4j.conf;

/* loaded from: classes.dex */
public final class ConfigurationContext {
    public static final String CONFIGURATION_IMPL = "twitter4j.configurationFactory";
    public static final String DEFAULT_CONFIGURATION_FACTORY = "twitter4j.conf.PropertyConfigurationFactory";
    private static final ConfigurationFactory factory;

    static {
        String CONFIG_IMPL;
        try {
            CONFIG_IMPL = System.getProperty(CONFIGURATION_IMPL, DEFAULT_CONFIGURATION_FACTORY);
        } catch (SecurityException e) {
            CONFIG_IMPL = DEFAULT_CONFIGURATION_FACTORY;
        }
        try {
            factory = (ConfigurationFactory) Class.forName(CONFIG_IMPL).newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new AssertionError(cnfe);
        } catch (IllegalAccessException iae) {
            throw new AssertionError(iae);
        } catch (InstantiationException ie) {
            throw new AssertionError(ie);
        }
    }

    public static Configuration getInstance() {
        return factory.getInstance();
    }

    public static Configuration getInstance(String configTreePath) {
        return factory.getInstance(configTreePath);
    }
}
