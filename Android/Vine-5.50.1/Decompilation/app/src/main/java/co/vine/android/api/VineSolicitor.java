package co.vine.android.api;

/* loaded from: classes.dex */
public class VineSolicitor implements TimelineItem {
    public String buttonText;
    public boolean closeable;
    public String completeButton;
    public String completeDescription;
    public String completeExplanation;
    public String completeTitle;
    public String description;
    public String dismissText;
    public String originUrl;
    public String reference;
    public String title;
    public String type;

    public VineSolicitor() {
    }

    public VineSolicitor(VineSolicitorBuilder builder) {
        this.type = builder.type;
        this.title = builder.title;
        this.description = builder.description;
        this.buttonText = builder.buttonText;
        this.dismissText = builder.dismissText;
        this.completeTitle = builder.completeTitle;
        this.completeDescription = builder.completeDescription;
        this.completeExplanation = builder.completeExplanation;
        this.completeButton = builder.completeButton;
        this.closeable = builder.closeable;
        this.reference = builder.reference;
    }

    @Override // co.vine.android.api.TimelineItem
    public long getId() {
        long resultId = 0 + (this.type != null ? this.type.hashCode() : 0L) + (this.title != null ? this.title.hashCode() : 0L) + (this.description != null ? this.description.hashCode() : 0L) + (this.buttonText != null ? this.buttonText.hashCode() : 0L) + (this.dismissText != null ? this.dismissText.hashCode() : 0L) + (this.completeTitle != null ? this.completeTitle.hashCode() : 0L) + (this.completeDescription != null ? this.completeDescription.hashCode() : 0L) + (this.completeExplanation != null ? this.completeExplanation.hashCode() : 0L) + (this.completeButton != null ? this.completeButton.hashCode() : 0L) + (this.reference != null ? this.reference.hashCode() : 0L);
        return resultId < 0 ? resultId : resultId * (-1);
    }

    @Override // co.vine.android.api.TimelineItem
    public TimelineItemType getType() {
        return TimelineItemType.SOLICITOR;
    }

    public static class VineSolicitorBuilder {
        public String buttonText;
        public boolean closeable;
        public String completeButton;
        public String completeDescription;
        public String completeExplanation;
        public String completeTitle;
        public String description;
        public String dismissText;
        public String reference;
        public String title;
        public String type;

        public VineSolicitorBuilder type(String type) {
            this.type = type;
            return this;
        }

        public VineSolicitorBuilder title(String title) {
            this.title = title;
            return this;
        }

        public VineSolicitorBuilder description(String description) {
            this.description = description;
            return this;
        }

        public VineSolicitorBuilder buttonText(String buttonText) {
            this.buttonText = buttonText;
            return this;
        }

        public VineSolicitorBuilder dismissText(String dismissText) {
            this.dismissText = dismissText;
            return this;
        }

        public VineSolicitorBuilder completeTitle(String completeTitle) {
            this.completeTitle = completeTitle;
            return this;
        }

        public VineSolicitorBuilder completeDescription(String completeDescription) {
            this.completeDescription = completeDescription;
            return this;
        }

        public VineSolicitorBuilder completeExplanation(String completeExplanation) {
            this.completeExplanation = completeExplanation;
            return this;
        }

        public VineSolicitorBuilder completeButton(String completeButton) {
            this.completeButton = completeButton;
            return this;
        }

        public VineSolicitorBuilder closeable(boolean closeable) {
            this.closeable = closeable;
            return this;
        }

        public VineSolicitorBuilder reference(String reference) {
            this.reference = reference;
            return this;
        }
    }
}
