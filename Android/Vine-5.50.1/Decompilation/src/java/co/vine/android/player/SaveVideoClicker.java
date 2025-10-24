package co.vine.android.player;

import android.widget.Toast;
import co.vine.android.embed.player.VideoViewInterface;
import co.vine.android.util.CrossConstants;
import co.vine.android.util.MediaUtil;
import com.edisonwang.android.slog.SLog;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/* loaded from: classes.dex */
public class SaveVideoClicker {
    private static File LONG_PRESS_SAVE_DIR = null;
    private int mCount = 0;
    private Toast mToast;

    public static void setLongPressSaveDir(File dir) {
        LONG_PRESS_SAVE_DIR = dir;
    }

    public void onClick(VideoViewInterface view) throws Throwable {
        if (LONG_PRESS_SAVE_DIR != null) {
            this.mCount++;
            if (this.mCount >= 20) {
                String path = view.getPath();
                try {
                    String name = new File(path).getName();
                    if (!name.endsWith(".mp4")) {
                        name = name + ".mp4";
                    }
                    File dest = new File(LONG_PRESS_SAVE_DIR, name);
                    FileUtils.copyFile(new File(path), dest);
                    if (this.mToast == null) {
                        this.mToast = Toast.makeText(view.getContext().getApplicationContext(), "", 0);
                    }
                    this.mToast.setText("File Saved: " + dest);
                    this.mToast.show();
                    MediaUtil.scanFile(view.getContext(), dest, null);
                } catch (IOException e) {
                    SLog.e("Failed to save video", (Throwable) e);
                }
                this.mCount = 0;
            }
            if (this.mCount > 3) {
                Object cacheKey = view.getTag(CrossConstants.RES_CACHE_TAG_KEY);
                if (this.mToast == null) {
                    this.mToast = Toast.makeText(view.getContext().getApplicationContext(), "", 0);
                }
                this.mToast.setText(cacheKey.toString());
                this.mToast.show();
            }
        }
    }
}
