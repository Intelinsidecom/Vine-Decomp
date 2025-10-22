package co.vine.android.api;

/* loaded from: classes.dex */
public class VineUrlAction implements TimelineItem {
    public String actionIconUrl;
    public String actionLink;
    public String actionTitle;
    public String backgroundImageUrl;
    public String backgroundVideoUrl;
    public boolean closeable;
    public String description;
    public String originUrl;
    public String reference;
    public String title;
    public String type;

    public VineUrlAction() {
    }

    public VineUrlAction(VineUrlActionBuilder builder) {
        this.title = builder.title;
        this.description = builder.description;
        this.backgroundImageUrl = builder.backgroundImageUrl;
        this.backgroundVideoUrl = builder.backgroundVideoUrl == null ? "" : builder.backgroundVideoUrl;
        this.actionTitle = builder.actionTitle;
        this.actionIconUrl = builder.actionIconUrl;
        this.actionLink = builder.actionLink;
        this.closeable = builder.closeable;
        this.type = builder.type;
        this.reference = builder.reference;
    }

    @Override // co.vine.android.api.TimelineItem
    public long getId() {
        long resultId = 0 + (this.title != null ? this.title.hashCode() : 0L) + (this.description != null ? this.description.hashCode() : 0L) + (this.backgroundImageUrl != null ? this.backgroundImageUrl.hashCode() : 0L) + (this.backgroundVideoUrl != null ? this.backgroundVideoUrl.hashCode() : 0L) + (this.actionTitle != null ? this.actionTitle.hashCode() : 0L) + (this.actionIconUrl != null ? this.actionIconUrl.hashCode() : 0L) + (this.actionLink != null ? this.actionLink.hashCode() : 0L) + (this.type != null ? this.type.hashCode() : 0L) + (this.reference != null ? this.reference.hashCode() : 0L);
        return resultId < 0 ? resultId : resultId * (-1);
    }

    @Override // co.vine.android.api.TimelineItem
    public TimelineItemType getType() {
        return TimelineItemType.URL_ACTION;
    }

    public static class VineUrlActionBuilder {
        public String actionIconUrl;
        public String actionLink;
        public String actionTitle;
        public String backgroundImageUrl;
        public String backgroundVideoUrl;
        public boolean closeable;
        public String description;
        public String reference;
        public String title;
        public String type;

        public VineUrlActionBuilder title(String title) {
            this.title = title;
            return this;
        }

        public VineUrlActionBuilder description(String description) {
            this.description = description;
            return this;
        }

        public VineUrlActionBuilder backgroundImageUrl(String backgroundImageUrl) {
            this.backgroundImageUrl = backgroundImageUrl;
            return this;
        }

        public VineUrlActionBuilder backgroundVideoUrl(String backgroundVideoUrl) {
            this.backgroundVideoUrl = backgroundVideoUrl;
            return this;
        }

        public VineUrlActionBuilder actionTitle(String actionTitle) {
            this.actionTitle = actionTitle;
            return this;
        }

        public VineUrlActionBuilder actionIconUrl(String actionIconUrl) {
            this.actionIconUrl = actionIconUrl;
            return this;
        }

        public VineUrlActionBuilder actionLink(String actionLink) {
            this.actionLink = actionLink;
            return this;
        }

        public VineUrlActionBuilder type(String type) {
            this.type = type;
            return this;
        }

        public VineUrlActionBuilder reference(String reference) {
            this.reference = reference;
            return this;
        }

        public VineUrlActionBuilder closeable(boolean closeable) {
            this.closeable = closeable;
            return this;
        }
    }
}
