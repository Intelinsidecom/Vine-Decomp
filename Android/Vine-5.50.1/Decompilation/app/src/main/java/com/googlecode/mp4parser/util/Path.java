package com.googlecode.mp4parser.util;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.AbstractContainerBox;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class Path {
    static final /* synthetic */ boolean $assertionsDisabled;
    static Pattern component;

    static {
        $assertionsDisabled = !Path.class.desiredAssertionStatus();
        component = Pattern.compile("(....|\\.\\.)(\\[(.*)\\])?");
    }

    private Path() {
    }

    public static String createPath(Box box) {
        return createPath(box, "");
    }

    private static String createPath(Box box, String path) {
        Container parent = box.getParent();
        int index = 0;
        List<Box> siblings = parent.getBoxes();
        for (Box sibling : siblings) {
            if (sibling.getType().equals(box.getType())) {
                if (sibling == box) {
                    break;
                }
                index++;
            }
        }
        String path2 = String.valueOf(String.format("/%s[%d]", box.getType(), Integer.valueOf(index))) + path;
        if (parent instanceof Box) {
            return createPath((Box) parent, path2);
        }
        return path2;
    }

    public static <T extends Box> T getPath(AbstractContainerBox abstractContainerBox, String str) {
        List paths = getPaths(abstractContainerBox, str, true);
        if (paths.isEmpty()) {
            return null;
        }
        return (T) ((Box) paths.get(0));
    }

    public static <T extends Box> List<T> getPaths(Box box, String path) {
        return getPaths(box, path, false);
    }

    public static <T extends Box> List<T> getPaths(Container container, String path) {
        return getPaths(container, path, false);
    }

    private static <T extends Box> List<T> getPaths(AbstractContainerBox container, String path, boolean singleResult) {
        return getPaths((Object) container, path, singleResult);
    }

    private static <T extends Box> List<T> getPaths(Container container, String path, boolean singleResult) {
        return getPaths((Object) container, path, singleResult);
    }

    private static <T extends Box> List<T> getPaths(Box box, String path, boolean singleResult) {
        return getPaths((Object) box, path, singleResult);
    }

    private static <T extends Box> List<T> getPaths(Object thing, String path, boolean singleResult) throws NumberFormatException {
        Object obj;
        String now;
        String later;
        if (path.startsWith("/")) {
            path = path.substring(1);
            while (thing instanceof Box) {
                thing = ((Box) thing).getParent();
            }
            obj = thing;
        } else {
            obj = thing;
        }
        if (path.length() == 0) {
            if (obj instanceof Box) {
                return Collections.singletonList((Box) obj);
            }
            throw new RuntimeException("Result of path expression seems to be the root container. This is not allowed!");
        }
        if (path.contains("/")) {
            later = path.substring(path.indexOf(47) + 1);
            now = path.substring(0, path.indexOf(47));
        } else {
            now = path;
            later = "";
        }
        Matcher m = component.matcher(now);
        if (m.matches()) {
            String type = m.group(1);
            if ("..".equals(type)) {
                if (obj instanceof Box) {
                    return getPaths(((Box) obj).getParent(), later, singleResult);
                }
                return Collections.emptyList();
            }
            if (obj instanceof Container) {
                int index = -1;
                if (m.group(2) != null) {
                    String indexString = m.group(3);
                    index = Integer.parseInt(indexString);
                }
                List<T> children = new LinkedList<>();
                int currentIndex = 0;
                for (Box box1 : ((Container) obj).getBoxes()) {
                    if (box1.getType().matches(type)) {
                        if (index == -1 || index == currentIndex) {
                            children.addAll(getPaths(box1, later, singleResult));
                        }
                        currentIndex++;
                    }
                    if (singleResult || index >= 0) {
                        if (!children.isEmpty()) {
                            return children;
                        }
                    }
                }
                return children;
            }
            return Collections.emptyList();
        }
        throw new RuntimeException(String.valueOf(now) + " is invalid path.");
    }
}
