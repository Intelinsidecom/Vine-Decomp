package twitter4j;

/* loaded from: classes.dex */
public final class Version {
    private static final String TITLE = "Twitter4J";
    private static final String VERSION = "3.0.6-SNAPSHOT";

    private Version() {
        throw new AssertionError();
    }

    public static String getVersion() {
        return VERSION;
    }

    public static void main(String[] args) {
        System.out.println("Twitter4J 3.0.6-SNAPSHOT");
    }
}
