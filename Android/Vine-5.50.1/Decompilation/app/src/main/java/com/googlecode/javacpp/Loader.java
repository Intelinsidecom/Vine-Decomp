package com.googlecode.javacpp;

import com.googlecode.javacpp.annotation.Platform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.WeakHashMap;

/* loaded from: classes.dex */
public class Loader {
    static boolean loadLibraries;
    static Map<String, String> loadedLibraries;
    static WeakHashMap<Class<? extends Pointer>, HashMap<String, Integer>> memberOffsets;
    private static final String platformName;
    private static Properties platformProperties = null;
    static File tempDir;

    static {
        String jvmName = System.getProperty("java.vm.name").toLowerCase();
        String osName = System.getProperty("os.name").toLowerCase();
        String osArch = System.getProperty("os.arch").toLowerCase();
        if (jvmName.startsWith("dalvik") && osName.startsWith("linux")) {
            osName = "android";
        } else if (osName.startsWith("mac os x")) {
            osName = "macosx";
        } else {
            int spaceIndex = osName.indexOf(32);
            if (spaceIndex > 0) {
                osName = osName.substring(0, spaceIndex);
            }
        }
        if (osArch.equals("i386") || osArch.equals("i486") || osArch.equals("i586") || osArch.equals("i686")) {
            osArch = "x86";
        } else if (osArch.equals("amd64") || osArch.equals("x86-64") || osArch.equals("x64")) {
            osArch = "x86_64";
        } else if (osArch.startsWith("arm")) {
            osArch = "arm";
        }
        platformName = osName + "-" + osArch;
        tempDir = null;
        loadLibraries = true;
        loadedLibraries = Collections.synchronizedMap(new HashMap());
        if (getPlatformName().startsWith("windows")) {
            Runtime.getRuntime().addShutdownHook(new Thread() { // from class: com.googlecode.javacpp.Loader.2
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() throws IOException {
                    if (Loader.tempDir != null) {
                        try {
                            LinkedList<String> command = new LinkedList<>();
                            command.add(System.getProperty("java.home") + "/bin/java");
                            command.add("-classpath");
                            command.add(System.getProperty("java.class.path"));
                            command.add(Loader.class.getName());
                            command.add(Loader.tempDir.getAbsolutePath());
                            new ProcessBuilder(command).start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
        memberOffsets = new WeakHashMap<>();
    }

    public static String getPlatformName() {
        return System.getProperty("com.googlecode.javacpp.platform.name", platformName);
    }

    public static Properties loadProperties() throws IOException {
        String name = getPlatformName();
        if (platformProperties != null && name.equals(platformProperties.getProperty("platform.name"))) {
            return platformProperties;
        }
        Properties propertiesLoadProperties = loadProperties(name);
        platformProperties = propertiesLoadProperties;
        return propertiesLoadProperties;
    }

    public static Properties loadProperties(String name) throws IOException {
        Properties p = new Properties();
        p.put("platform.name", name);
        InputStream is = Loader.class.getResourceAsStream("properties/" + name + ".properties");
        try {
            try {
                p.load(new InputStreamReader(is));
            } catch (Exception e) {
                InputStream is2 = Loader.class.getResourceAsStream("properties/generic.properties");
                try {
                    try {
                        p.load(new InputStreamReader(is2));
                    } catch (NoSuchMethodError e2) {
                        p.load(is2);
                    }
                } catch (Exception e22) {
                    throw new MissingResourceException("Could not even get generic properties: " + e22.getMessage(), Loader.class.getName(), "properties/generic.properties");
                }
            }
        } catch (NoSuchMethodError e3) {
            p.load(is);
        }
        return p;
    }

    public static Class getEnclosingClass(Class cls) {
        Class declaringClass = cls;
        while (declaringClass.getDeclaringClass() != null && !declaringClass.isAnnotationPresent(com.googlecode.javacpp.annotation.Properties.class)) {
            if (declaringClass.isAnnotationPresent(Platform.class)) {
                Platform p = (Platform) declaringClass.getAnnotation(Platform.class);
                if (p.define().length > 0 || p.include().length > 0 || p.cinclude().length > 0 || p.includepath().length > 0 || p.options().length > 0 || p.linkpath().length > 0 || p.link().length > 0 || p.framework().length > 0 || p.preloadpath().length > 0 || p.preload().length > 0 || p.library().length() > 0) {
                    break;
                }
            }
            declaringClass = declaringClass.getDeclaringClass();
        }
        return declaringClass;
    }

    public static class ClassProperties extends HashMap<String, LinkedList<String>> {
        String pathSeparator;
        String platformName;
        String platformRoot;

        public ClassProperties() {
        }

        public ClassProperties(Properties properties) {
            this.platformName = properties.getProperty("platform.name");
            this.platformRoot = properties.getProperty("platform.root");
            this.pathSeparator = properties.getProperty("path.separator");
            if (this.platformRoot == null || this.platformRoot.length() == 0) {
                this.platformRoot = ".";
            }
            if (!this.platformRoot.endsWith(File.separator)) {
                this.platformRoot += File.separator;
            }
            for (Map.Entry e : properties.entrySet()) {
                String k = (String) e.getKey();
                String v = (String) e.getValue();
                if (v != null && v.length() != 0) {
                    if (k.equals("compiler.includepath") || k.equals("compiler.include") || k.equals("compiler.linkpath") || k.equals("compiler.link") || k.equals("compiler.framework")) {
                        addAll(k, v.split(this.pathSeparator));
                    } else {
                        setProperty(k, v);
                    }
                }
            }
        }

        public LinkedList<String> get(String key) {
            LinkedList<String> list = (LinkedList) super.get((Object) key);
            if (list == null) {
                LinkedList<String> list2 = new LinkedList<>();
                put(key, list2);
                return list2;
            }
            return list;
        }

        public void addAll(String key, String... values) {
            if (values != null) {
                addAll(key, Arrays.asList(values));
            }
        }

        public void addAll(String key, Collection<String> values) {
            if (values != null) {
                String root = null;
                if (key.equals("compiler.path") || key.equals("compiler.sysroot") || key.equals("compiler.includepath") || key.equals("compiler.linkpath")) {
                    root = this.platformRoot;
                }
                LinkedList<String> values2 = get(key);
                for (String value : values) {
                    if (value != null && !values2.contains(value)) {
                        if (root != null && !new File(value).isAbsolute() && new File(root + value).exists()) {
                            value = root + value;
                        }
                        values2.add(value);
                    }
                }
            }
        }

        public String getProperty(String key) {
            return getProperty(key, null);
        }

        public String getProperty(String key, String defaultValue) {
            LinkedList<String> values = get(key);
            if (values.isEmpty()) {
                return defaultValue;
            }
            String defaultValue2 = values.get(0);
            return defaultValue2;
        }

        public String setProperty(String key, String value) {
            LinkedList<String> values = get(key);
            String oldValue = values.isEmpty() ? null : values.get(0);
            values.clear();
            addAll(key, value);
            return oldValue;
        }

        public void load(Class cls, boolean inherit) {
            Platform[] platforms;
            Class<?> c = Loader.getEnclosingClass(cls);
            while (!c.isAnnotationPresent(com.googlecode.javacpp.annotation.Properties.class) && !c.isAnnotationPresent(Platform.class) && c.getSuperclass() != null) {
                c = c.getSuperclass();
            }
            com.googlecode.javacpp.annotation.Properties classProperties = (com.googlecode.javacpp.annotation.Properties) c.getAnnotation(com.googlecode.javacpp.annotation.Properties.class);
            if (classProperties == null) {
                Platform platform = (Platform) c.getAnnotation(Platform.class);
                if (platform != null) {
                    platforms = new Platform[]{platform};
                } else {
                    return;
                }
            } else {
                Class[] classes = classProperties.inherit();
                if (inherit && classes != null) {
                    for (Class c2 : classes) {
                        load(c2, inherit);
                    }
                }
                String target = classProperties.target();
                if (target.length() > 0) {
                    addAll("parser.target", target);
                }
                platforms = classProperties.value();
                if (platforms == null) {
                    return;
                }
            }
            String[] define = new String[0];
            String[] include = new String[0];
            String[] cinclude = new String[0];
            String[] includepath = new String[0];
            String[] options = new String[0];
            String[] linkpath = new String[0];
            String[] link = new String[0];
            String[] framework = new String[0];
            String[] preloadpath = new String[0];
            String[] preload = new String[0];
            String library = "jni" + c.getSimpleName();
            Platform[] arr$ = platforms;
            int len$ = arr$.length;
            int i$ = 0;
            while (true) {
                int i$2 = i$;
                if (i$2 < len$) {
                    Platform p = arr$[i$2];
                    String[][] names = {p.value(), p.not()};
                    boolean[] matches = {false, false};
                    for (int i = 0; i < names.length; i++) {
                        String[] arr$2 = names[i];
                        int len$2 = arr$2.length;
                        int i$3 = 0;
                        while (true) {
                            if (i$3 < len$2) {
                                String s = arr$2[i$3];
                                if (!this.platformName.startsWith(s)) {
                                    i$3++;
                                } else {
                                    matches[i] = true;
                                    break;
                                }
                            }
                        }
                    }
                    if ((names[0].length == 0 || matches[0]) && (names[1].length == 0 || !matches[1])) {
                        if (p.define().length > 0) {
                            define = p.define();
                        }
                        if (p.include().length > 0) {
                            include = p.include();
                        }
                        if (p.cinclude().length > 0) {
                            cinclude = p.cinclude();
                        }
                        if (p.includepath().length > 0) {
                            includepath = p.includepath();
                        }
                        if (p.options().length > 0) {
                            options = p.options();
                        }
                        if (p.linkpath().length > 0) {
                            linkpath = p.linkpath();
                        }
                        if (p.link().length > 0) {
                            link = p.link();
                        }
                        if (p.framework().length > 0) {
                            framework = p.framework();
                        }
                        if (p.preloadpath().length > 0) {
                            preloadpath = p.preloadpath();
                        }
                        if (p.preload().length > 0) {
                            preload = p.preload();
                        }
                        if (p.library().length() > 0) {
                            library = p.library();
                        }
                    }
                    i$ = i$2 + 1;
                } else {
                    addAll("generator.define", define);
                    addAll("generator.include", include);
                    addAll("generator.cinclude", cinclude);
                    addAll("compiler.includepath", includepath);
                    addAll("compiler.options", options);
                    addAll("compiler.linkpath", linkpath);
                    addAll("compiler.link", link);
                    addAll("compiler.framework", framework);
                    addAll("loader.preloadpath", preloadpath);
                    addAll("loader.preload", preload);
                    setProperty("loader.library", library);
                    return;
                }
            }
        }

        LinkedList<File> getHeaderFiles() {
            LinkedList<String> paths = get("compiler.includepath");
            LinkedList<String> includes = new LinkedList<>();
            includes.addAll(get("generator.include"));
            includes.addAll(get("generator.cinclude"));
            LinkedList<File> files = new LinkedList<>();
            Iterator<String> it = includes.iterator();
            while (it.hasNext()) {
                String include = it.next();
                if (include.startsWith("<") && include.endsWith(">")) {
                    include = include.substring(1, include.length() - 1);
                } else {
                    File f = new File(include);
                    if (f.exists()) {
                        files.add(f);
                    }
                }
                Iterator i$ = paths.iterator();
                while (true) {
                    if (i$.hasNext()) {
                        String path = i$.next();
                        File f2 = new File(path, include);
                        if (f2.exists()) {
                            files.add(f2);
                            break;
                        }
                    }
                }
            }
            return files;
        }
    }

    public static ClassProperties loadProperties(Class[] cls, Properties properties, boolean inherit) {
        ClassProperties cp = new ClassProperties(properties);
        for (Class c : cls) {
            cp.load(c, inherit);
        }
        return cp;
    }

    public static ClassProperties loadProperties(Class cls, Properties properties, boolean inherit) {
        ClassProperties cp = new ClassProperties(properties);
        cp.load(cls, inherit);
        return cp;
    }

    public static Class getCallerClass(int i) {
        Class[] classContext = new SecurityManager() { // from class: com.googlecode.javacpp.Loader.1
            @Override // java.lang.SecurityManager
            public Class[] getClassContext() {
                return super.getClassContext();
            }
        }.getClassContext();
        if (classContext != null) {
            for (int j = 0; j < classContext.length; j++) {
                if (classContext[j] == Loader.class) {
                    return classContext[i + j];
                }
            }
        } else {
            try {
                StackTraceElement[] classNames = Thread.currentThread().getStackTrace();
                for (int j2 = 0; j2 < classNames.length; j2++) {
                    if (Class.forName(classNames[j2].getClassName()) == Loader.class) {
                        return Class.forName(classNames[i + j2].getClassName());
                    }
                }
            } catch (ClassNotFoundException e) {
            }
        }
        return null;
    }

    public static File extractResource(String name, File directory, String prefix, String suffix) throws IOException {
        Class cls = getCallerClass(2);
        return extractResource(cls, name, directory, prefix, suffix);
    }

    public static File extractResource(Class cls, String name, File directory, String prefix, String suffix) throws IOException {
        return extractResource(cls.getResource(name), directory, prefix, suffix);
    }

    public static File extractResource(URL resourceURL, File directory, String prefix, String suffix) throws IOException {
        File file = null;
        InputStream is = resourceURL != null ? resourceURL.openStream() : null;
        if (is != null) {
            file = null;
            boolean fileExisted = false;
            try {
                if (prefix == null && suffix == null) {
                    if (directory == null) {
                        directory = new File(System.getProperty("java.io.tmpdir"));
                    }
                    File file2 = new File(directory, new File(resourceURL.getPath()).getName());
                    try {
                        fileExisted = file2.exists();
                        file = file2;
                    } catch (IOException e) {
                        e = e;
                        file = file2;
                        if (file != null && !fileExisted) {
                            file.delete();
                        }
                        throw e;
                    }
                } else {
                    file = File.createTempFile(prefix, suffix, directory);
                }
                FileOutputStream os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = is.read(buffer);
                    if (length == -1) {
                        break;
                    }
                    os.write(buffer, 0, length);
                }
                is.close();
                os.close();
            } catch (IOException e2) {
                e = e2;
            }
        }
        return file;
    }

    public static File getTempDir() {
        if (tempDir == null) {
            File tmpdir = new File(System.getProperty("java.io.tmpdir"));
            int i = 0;
            while (true) {
                if (i >= 1000) {
                    break;
                }
                File f = new File(tmpdir, "javacpp" + System.nanoTime());
                if (!f.mkdir()) {
                    i++;
                } else {
                    tempDir = f;
                    tempDir.deleteOnExit();
                    break;
                }
            }
        }
        return tempDir;
    }

    public static boolean isLoadLibraries() {
        return loadLibraries;
    }

    public static String load() {
        Class cls = getCallerClass(2);
        return load(cls);
    }

    public static String load(Class cls) throws ClassNotFoundException {
        if (!loadLibraries || cls == null) {
            return null;
        }
        Class cls2 = getEnclosingClass(cls);
        try {
            Class cls3 = Class.forName(cls2.getName(), true, cls2.getClassLoader());
            ClassProperties p = loadProperties(cls3, loadProperties(), true);
            LinkedList<String> preloads = new LinkedList<>();
            preloads.addAll(p.get("loader.preload"));
            preloads.addAll(p.get("compiler.link"));
            UnsatisfiedLinkError preloadError = null;
            Iterator i$ = preloads.iterator();
            while (i$.hasNext()) {
                String preload = i$.next();
                try {
                    URL[] urls = findLibrary(cls3, p, preload);
                    loadLibrary(urls, preload);
                } catch (UnsatisfiedLinkError e) {
                    preloadError = e;
                }
            }
            try {
                String library = p.getProperty("loader.library");
                URL[] urls2 = findLibrary(cls3, p, library);
                return loadLibrary(urls2, library);
            } catch (UnsatisfiedLinkError e2) {
                if (preloadError != null) {
                    e2.initCause(preloadError);
                }
                throw e2;
            }
        } catch (ClassNotFoundException ex) {
            Error e3 = new NoClassDefFoundError(ex.toString());
            e3.initCause(ex);
            throw e3;
        }
    }

    public static URL[] findLibrary(Class cls, ClassProperties properties, String libnameversion) {
        int k;
        String[] s = libnameversion.split("@");
        String libname = s[0];
        String version = s.length > 1 ? s[s.length - 1] : "";
        String filename = loadedLibraries.get(libnameversion);
        if (filename != null) {
            try {
                return new URL[]{new File(filename).toURI().toURL()};
            } catch (IOException e) {
                return new URL[0];
            }
        }
        String subdir = properties.getProperty("platform.name") + '/';
        String prefix = properties.getProperty("library.prefix", "") + libname;
        String suffix = properties.getProperty("library.suffix", "");
        String[] styles = {prefix + suffix + version, prefix + version + suffix, prefix + suffix};
        LinkedList<String> paths = new LinkedList<>();
        paths.addAll(properties.get("loader.preloadpath"));
        paths.addAll(properties.get("compiler.linkpath"));
        URL[] urls = new URL[styles.length * (paths.size() + 1)];
        int i = 0;
        int k2 = 0;
        while (cls != null && i < styles.length) {
            URL u = cls.getResource(subdir + styles[i]);
            if (u != null) {
                k = k2 + 1;
                urls[k2] = u;
            } else {
                k = k2;
            }
            i++;
            k2 = k;
        }
        int k3 = k2;
        for (int i2 = 0; paths.size() > 0 && i2 < styles.length; i2++) {
            Iterator i$ = paths.iterator();
            while (i$.hasNext()) {
                String path = i$.next();
                File file = new File(path, styles[i2]);
                if (file.exists()) {
                    int k4 = k3 + 1;
                    try {
                        urls[k3] = file.toURI().toURL();
                        k3 = k4;
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
        return (URL[]) Arrays.copyOf(urls, k3);
    }

    public static String loadLibrary(URL[] urls, String libnameversion) {
        File file;
        if (!loadLibraries) {
            return null;
        }
        String filename = loadedLibraries.get(libnameversion);
        if (filename != null) {
            return filename;
        }
        File tempFile = null;
        UnsatisfiedLinkError loadError = null;
        try {
            try {
                try {
                    for (URL url : urls) {
                        try {
                            file = new File(url.toURI());
                        } catch (Exception e) {
                            if (tempFile != null && tempFile.exists()) {
                                tempFile.deleteOnExit();
                            }
                            tempFile = extractResource(url, getTempDir(), (String) null, (String) null);
                            file = tempFile;
                        }
                        if (file != null && file.exists()) {
                            String filename2 = file.getAbsolutePath();
                            try {
                                loadedLibraries.put(libnameversion, filename2);
                                System.load(filename2);
                            } catch (UnsatisfiedLinkError e2) {
                                loadError = e2;
                                loadedLibraries.remove(libnameversion);
                            }
                        }
                    }
                    String libname = libnameversion.split("@")[0];
                    loadedLibraries.put(libnameversion, libname);
                    System.loadLibrary(libname);
                    if (tempFile != null && tempFile.exists()) {
                        tempFile.deleteOnExit();
                    }
                    return libname;
                } catch (IOException ex) {
                    loadedLibraries.remove(libnameversion);
                    if (loadError != null) {
                        ex.initCause(loadError);
                    }
                    Error e3 = new UnsatisfiedLinkError(ex.toString());
                    e3.initCause(ex);
                    throw e3;
                }
            } catch (UnsatisfiedLinkError e4) {
                loadedLibraries.remove(libnameversion);
                if (0 != 0) {
                    e4.initCause(null);
                }
                throw e4;
            }
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.deleteOnExit();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        File tmpdir = new File(System.getProperty("java.io.tmpdir"));
        File tempDir2 = new File(args[0]);
        if (tmpdir.equals(tempDir2.getParentFile()) && tempDir2.getName().startsWith("javacpp")) {
            File[] arr$ = tempDir2.listFiles();
            for (File file : arr$) {
                while (file.exists() && !file.delete()) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                    }
                }
            }
            tempDir2.delete();
        }
    }

    static void putMemberOffset(String typeName, String member, int offset) throws ClassNotFoundException {
        Class<?> c = Class.forName(typeName.replace('/', '.'), false, Loader.class.getClassLoader());
        putMemberOffset((Class<? extends Pointer>) c.asSubclass(Pointer.class), member, offset);
    }

    static synchronized void putMemberOffset(Class<? extends Pointer> type, String member, int offset) {
        HashMap<String, Integer> offsets = memberOffsets.get(type);
        if (offsets == null) {
            WeakHashMap<Class<? extends Pointer>, HashMap<String, Integer>> weakHashMap = memberOffsets;
            offsets = new HashMap<>();
            weakHashMap.put(type, offsets);
        }
        offsets.put(member, Integer.valueOf(offset));
    }

    public static int offsetof(Class<? extends Pointer> type, String member) {
        return memberOffsets.get(type).get(member).intValue();
    }

    public static int sizeof(Class<? extends Pointer> type) {
        return memberOffsets.get(type).get("sizeof").intValue();
    }
}
