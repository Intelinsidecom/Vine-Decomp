package retrofit;

/* loaded from: classes.dex */
public interface ErrorHandler {
    public static final ErrorHandler DEFAULT = new ErrorHandler() { // from class: retrofit.ErrorHandler.1
        @Override // retrofit.ErrorHandler
        public Throwable handleError(RetrofitError cause) {
            return cause;
        }
    };

    Throwable handleError(RetrofitError retrofitError);
}
