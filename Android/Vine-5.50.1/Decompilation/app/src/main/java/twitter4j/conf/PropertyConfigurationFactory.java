package twitter4j.conf;

/* loaded from: classes.dex */
class PropertyConfigurationFactory implements ConfigurationFactory {
    private static final PropertyConfiguration ROOT_CONFIGURATION = new PropertyConfiguration();

    PropertyConfigurationFactory() {
    }

    @Override // twitter4j.conf.ConfigurationFactory
    public Configuration getInstance() {
        return ROOT_CONFIGURATION;
    }

    @Override // twitter4j.conf.ConfigurationFactory
    public Configuration getInstance(String configTreePath) {
        PropertyConfiguration conf = new PropertyConfiguration(configTreePath);
        return conf;
    }

    @Override // twitter4j.conf.ConfigurationFactory
    public void dispose() {
    }
}
