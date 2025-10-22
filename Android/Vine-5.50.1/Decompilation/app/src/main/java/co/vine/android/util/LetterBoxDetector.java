package co.vine.android.util;

import android.graphics.Bitmap;
import android.graphics.Color;

/* loaded from: classes.dex */
public class LetterBoxDetector {
    private Bitmap mImage;

    public LetterBoxDetector(Bitmap image) {
        this.mImage = scale(image);
    }

    public boolean isLetterBox() {
        int midRowIndex = this.mImage.getHeight() / 2;
        int bottomRowIndex = this.mImage.getHeight() - 3;
        return isRowMonoChrome(3) && isRowMonoChrome(bottomRowIndex) && !isRowMonoChrome(midRowIndex);
    }

    private boolean isRowMonoChrome(int rowIndex) {
        int endPixel = this.mImage.getWidth() - 5;
        int currentColor = this.mImage.getPixel(5, rowIndex);
        for (int i = 5; i < endPixel; i++) {
            if (!isSimilarColor(currentColor, this.mImage.getPixel(i, rowIndex))) {
                return false;
            }
        }
        return true;
    }

    private Bitmap scale(Bitmap src) {
        double srcHeight = src.getHeight();
        double srcWidth = src.getWidth();
        double ratio = 50.0d / srcHeight;
        return Bitmap.createScaledBitmap(src, (int) (srcWidth * ratio), 50, false);
    }

    private boolean isSimilarColor(int aPixel, int bPixel) {
        int rA = Color.red(aPixel);
        int rB = Color.red(bPixel);
        int gA = Color.green(aPixel);
        int gB = Color.green(bPixel);
        int bA = Color.blue(aPixel);
        int bB = Color.blue(bPixel);
        return inRange(rA, rB) && inRange(gA, gB) && inRange(bA, bB);
    }

    private boolean inRange(int a, int b) {
        return Math.abs(a - b) < 8;
    }
}
