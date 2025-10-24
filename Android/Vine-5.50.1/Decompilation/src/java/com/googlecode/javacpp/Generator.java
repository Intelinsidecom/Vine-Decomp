package com.googlecode.javacpp;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacpp.Pointer;
import com.googlecode.javacpp.annotation.Adapter;
import com.googlecode.javacpp.annotation.Allocator;
import com.googlecode.javacpp.annotation.ArrayAllocator;
import com.googlecode.javacpp.annotation.ByPtr;
import com.googlecode.javacpp.annotation.ByPtrPtr;
import com.googlecode.javacpp.annotation.ByPtrRef;
import com.googlecode.javacpp.annotation.ByRef;
import com.googlecode.javacpp.annotation.ByVal;
import com.googlecode.javacpp.annotation.Cast;
import com.googlecode.javacpp.annotation.Const;
import com.googlecode.javacpp.annotation.Convention;
import com.googlecode.javacpp.annotation.Function;
import com.googlecode.javacpp.annotation.Index;
import com.googlecode.javacpp.annotation.MemberGetter;
import com.googlecode.javacpp.annotation.MemberSetter;
import com.googlecode.javacpp.annotation.Name;
import com.googlecode.javacpp.annotation.Namespace;
import com.googlecode.javacpp.annotation.NoDeallocator;
import com.googlecode.javacpp.annotation.NoException;
import com.googlecode.javacpp.annotation.Opaque;
import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;
import com.googlecode.javacpp.annotation.ValueGetter;
import com.googlecode.javacpp.annotation.ValueSetter;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: classes.dex */
public class Generator implements Closeable {
    public static final String JNI_VERSION = "JNI_VERSION_1_6";
    private LinkedListRegister<Class> arrayDeallocators;
    private LinkedListRegister<Class> deallocators;
    private LinkedListRegister<String> functionDefinitions;
    private LinkedListRegister<String> functionPointers;
    private LinkedListRegister<Class> jclasses;
    private LinkedListRegister<Class> jclassesInit;
    private boolean mayThrowExceptions;
    private HashMap<Class, LinkedList<String>> members;
    private PrintWriter out;
    private PrintWriter out2;
    private Loader.ClassProperties properties;
    private boolean usesAdapters;
    private static final Logger logger = Logger.getLogger(Generator.class.getName());
    private static final List<Class> baseClasses = Arrays.asList(Pointer.class, BytePointer.class, ShortPointer.class, IntPointer.class, LongPointer.class, FloatPointer.class, DoublePointer.class, CharPointer.class, PointerPointer.class, BoolPointer.class, CLongPointer.class, SizeTPointer.class);

    public static class AdapterInformation {
        public int argc;
        public String cast;
        public boolean constant;
        public String name;
    }

    public static class MethodInformation {
        public boolean allocator;
        public Annotation[] annotations;
        public boolean arrayAllocator;
        public boolean bufferGetter;
        public Class<?> cls;
        public boolean deallocator;
        public int dim;
        public boolean memberGetter;
        public String[] memberName;
        public boolean memberSetter;
        public Method method;
        public int modifiers;
        public String name;
        public boolean noOffset;
        public boolean noReturnGetter;
        public boolean overloaded;
        public Method pairedMethod;
        public Annotation[][] parameterAnnotations;
        public boolean[] parameterRaw;
        public Class<?>[] parameterTypes;
        public boolean returnRaw;
        public Class<?> returnType;
        public Class<?> throwsException;
        public boolean valueGetter;
        public boolean valueSetter;
        public boolean withEnv;
    }

    public Generator(Loader.ClassProperties properties) {
        this.properties = properties;
    }

    public static class LinkedListRegister<E> extends LinkedList<E> {
        public int register(E e) {
            int i = indexOf(e);
            if (i < 0) {
                add(e);
                return size() - 1;
            }
            return i;
        }
    }

    public boolean generate(String sourceFilename, String headerFilename, String classPath, Class<?>... classes) throws FileNotFoundException {
        this.out = new PrintWriter(new Writer() { // from class: com.googlecode.javacpp.Generator.1
            @Override // java.io.Writer
            public void write(char[] cbuf, int off, int len) {
            }

            @Override // java.io.Writer, java.io.Flushable
            public void flush() {
            }

            @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }
        });
        this.out2 = null;
        this.functionDefinitions = new LinkedListRegister<>();
        this.functionPointers = new LinkedListRegister<>();
        this.deallocators = new LinkedListRegister<>();
        this.arrayDeallocators = new LinkedListRegister<>();
        this.jclasses = new LinkedListRegister<>();
        this.jclassesInit = new LinkedListRegister<>();
        this.members = new HashMap<>();
        this.mayThrowExceptions = false;
        this.usesAdapters = false;
        if (!doClasses(true, true, classPath, classes)) {
            return false;
        }
        this.out = new PrintWriter(sourceFilename);
        if (headerFilename != null) {
            this.out2 = new PrintWriter(headerFilename);
        }
        return doClasses(this.mayThrowExceptions, this.usesAdapters, classPath, classes);
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.out != null) {
            this.out.close();
        }
        if (this.out2 != null) {
            this.out2.close();
        }
    }

    private boolean doClasses(boolean handleExceptions, boolean defineAdapters, String classPath, Class<?>... classes) throws SecurityException {
        this.out.println("/* DO NOT EDIT THIS FILE - IT IS MACHINE GENERATED */");
        this.out.println();
        if (this.out2 != null) {
            this.out2.println("/* DO NOT EDIT THIS FILE - IT IS MACHINE GENERATED */");
            this.out2.println();
        }
        Iterator i$ = this.properties.get("generator.define").iterator();
        while (i$.hasNext()) {
            String s = i$.next();
            this.out.println("#define " + s);
        }
        this.out.println();
        this.out.println("#ifdef __APPLE__");
        this.out.println("    #define _JAVASOFT_JNI_MD_H_");
        this.out.println();
        this.out.println("    #define JNIEXPORT __attribute__((visibility(\"default\")))");
        this.out.println("    #define JNIIMPORT");
        this.out.println("    #define JNICALL");
        this.out.println();
        this.out.println("    typedef int jint;");
        this.out.println("    typedef long long jlong;");
        this.out.println("    typedef signed char jbyte;");
        this.out.println("#endif");
        this.out.println("#ifdef _WIN32");
        this.out.println("    #define _JAVASOFT_JNI_MD_H_");
        this.out.println();
        this.out.println("    #define JNIEXPORT __declspec(dllexport)");
        this.out.println("    #define JNIIMPORT __declspec(dllimport)");
        this.out.println("    #define JNICALL __stdcall");
        this.out.println();
        this.out.println("    typedef int jint;");
        this.out.println("    typedef long long jlong;");
        this.out.println("    typedef signed char jbyte;");
        this.out.println("#endif");
        this.out.println("#include <jni.h>");
        if (this.out2 != null) {
            this.out2.println("#include <jni.h>");
        }
        this.out.println("#ifdef ANDROID");
        this.out.println("    #include <android/log.h>");
        this.out.println("    #define NewWeakGlobalRef(obj) NewGlobalRef(obj)");
        this.out.println("    #define DeleteWeakGlobalRef(obj) DeleteGlobalRef(obj)");
        this.out.println("#endif");
        this.out.println();
        this.out.println("#include <stddef.h>");
        this.out.println("#ifndef _WIN32");
        this.out.println("    #include <stdint.h>");
        this.out.println("#endif");
        this.out.println("#include <stdio.h>");
        this.out.println("#include <stdlib.h>");
        this.out.println("#include <string.h>");
        this.out.println("#include <exception>");
        this.out.println("#include <new>");
        this.out.println();
        this.out.println("#define jlong_to_ptr(a) ((void*)(uintptr_t)(a))");
        this.out.println("#define ptr_to_jlong(a) ((jlong)(uintptr_t)(a))");
        this.out.println();
        this.out.println("#if defined(_MSC_VER)");
        this.out.println("    #define JavaCPP_noinline __declspec(noinline)");
        this.out.println("    #define JavaCPP_hidden /* hidden by default */");
        this.out.println("#elif defined(__GNUC__)");
        this.out.println("    #define JavaCPP_noinline __attribute__((noinline))");
        this.out.println("    #define JavaCPP_hidden   __attribute__((visibility(\"hidden\")))");
        this.out.println("#else");
        this.out.println("    #define JavaCPP_noinline");
        this.out.println("    #define JavaCPP_hidden");
        this.out.println("#endif");
        this.out.println();
        List[] include = {this.properties.get("generator.include"), this.properties.get("generator.cinclude")};
        for (int i = 0; i < include.length; i++) {
            if (include[i] != null && include[i].size() > 0) {
                if (i == 1) {
                    this.out.println("extern \"C\" {");
                    if (this.out2 != null) {
                        this.out2.println("#ifdef __cplusplus");
                        this.out2.println("extern \"C\" {");
                        this.out2.println("#endif");
                    }
                }
                for (String s2 : include[i]) {
                    String line = "#include ";
                    if (!s2.startsWith("<") && !s2.startsWith("\"")) {
                        line = "#include \"";
                    }
                    String line2 = line + s2;
                    if (!s2.endsWith(">") && !s2.endsWith("\"")) {
                        line2 = line2 + '\"';
                    }
                    this.out.println(line2);
                    if (this.out2 != null) {
                        this.out2.println(line2);
                    }
                }
                if (i == 1) {
                    this.out.println("}");
                    if (this.out2 != null) {
                        this.out2.println("#ifdef __cplusplus");
                        this.out2.println("}");
                        this.out2.println("#endif");
                    }
                }
                this.out.println();
            }
        }
        this.out.println("static JavaVM* JavaCPP_vm = NULL;");
        this.out.println("static const char* JavaCPP_classNames[" + this.jclasses.size() + "] = {");
        Iterator<Class> classIterator = this.jclasses.iterator();
        int maxMemberSize = 0;
        while (classIterator.hasNext()) {
            Class c = classIterator.next();
            this.out.print("        \"" + c.getName().replace('.', '/') + "\"");
            if (classIterator.hasNext()) {
                this.out.println(",");
            }
            LinkedList<String> m = this.members.get(c);
            if (m != null && m.size() > maxMemberSize) {
                maxMemberSize = m.size();
            }
        }
        this.out.println(" };");
        this.out.println("static jclass JavaCPP_classes[" + this.jclasses.size() + "] = { NULL };");
        this.out.println("static jmethodID JavaCPP_initMID = NULL;");
        this.out.println("static jfieldID JavaCPP_addressFID = NULL;");
        this.out.println("static jfieldID JavaCPP_positionFID = NULL;");
        this.out.println("static jfieldID JavaCPP_limitFID = NULL;");
        this.out.println("static jfieldID JavaCPP_capacityFID = NULL;");
        this.out.println();
        this.out.println("static inline void JavaCPP_log(const char* fmt, ...) {");
        this.out.println("    va_list ap;");
        this.out.println("    va_start(ap, fmt);");
        this.out.println("#ifdef ANDROID");
        this.out.println("    __android_log_vprint(ANDROID_LOG_ERROR, \"javacpp\", fmt, ap);");
        this.out.println("#else");
        this.out.println("    vfprintf(stderr, fmt, ap);");
        this.out.println("    fprintf(stderr, \"\\n\");");
        this.out.println("#endif");
        this.out.println("    va_end(ap);");
        this.out.println("}");
        this.out.println();
        this.out.println("static JavaCPP_noinline jclass JavaCPP_getClass(JNIEnv* env, int i) {");
        this.out.println("    if (JavaCPP_classes[i] == NULL && env->PushLocalFrame(1) == 0) {");
        this.out.println("        jclass cls = env->FindClass(JavaCPP_classNames[i]);");
        this.out.println("        if (cls == NULL || env->ExceptionCheck()) {");
        this.out.println("            JavaCPP_log(\"Error loading class %s.\", JavaCPP_classNames[i]);");
        this.out.println("            return NULL;");
        this.out.println("        }");
        this.out.println("        JavaCPP_classes[i] = (jclass)env->NewWeakGlobalRef(cls);");
        this.out.println("        if (JavaCPP_classes[i] == NULL || env->ExceptionCheck()) {");
        this.out.println("            JavaCPP_log(\"Error creating global reference of class %s.\", JavaCPP_classNames[i]);");
        this.out.println("            return NULL;");
        this.out.println("        }");
        this.out.println("        env->PopLocalFrame(NULL);");
        this.out.println("    }");
        this.out.println("    return JavaCPP_classes[i];");
        this.out.println("}");
        this.out.println();
        this.out.println("class JavaCPP_hidden JavaCPP_exception : public std::exception {");
        this.out.println("public:");
        this.out.println("    JavaCPP_exception(const char* str) throw() {");
        this.out.println("        if (str == NULL) {");
        this.out.println("            strcpy(msg, \"Unknown exception.\");");
        this.out.println("        } else {");
        this.out.println("            strncpy(msg, str, sizeof(msg));");
        this.out.println("            msg[sizeof(msg) - 1] = 0;");
        this.out.println("        }");
        this.out.println("    }");
        this.out.println("    virtual const char* what() const throw() { return msg; }");
        this.out.println("    char msg[1024];");
        this.out.println("};");
        this.out.println();
        if (handleExceptions) {
            this.out.println("static JavaCPP_noinline jthrowable JavaCPP_handleException(JNIEnv* env, int i) {");
            this.out.println("    jstring str = NULL;");
            this.out.println("    try {");
            this.out.println("        throw;");
            this.out.println("    } catch (std::exception& e) {");
            this.out.println("        str = env->NewStringUTF(e.what());");
            this.out.println("    } catch (...) {");
            this.out.println("        str = env->NewStringUTF(\"Unknown exception.\");");
            this.out.println("    }");
            this.out.println("    jclass cls = JavaCPP_getClass(env, i);");
            this.out.println("    jmethodID mid = env->GetMethodID(cls, \"<init>\", \"(Ljava/lang/String;)V\");");
            this.out.println("    if (mid == NULL || env->ExceptionCheck()) {");
            this.out.println("        JavaCPP_log(\"Error getting constructor ID of %s.\", JavaCPP_classNames[i]);");
            this.out.println("        return NULL;");
            this.out.println("    } else {");
            this.out.println("        return (jthrowable)env->NewObject(cls, mid, str);");
            this.out.println("    }");
            this.out.println("}");
            this.out.println();
        }
        if (defineAdapters) {
            this.out.println("#include <vector>");
            this.out.println("template<typename P, typename T = P> class JavaCPP_hidden VectorAdapter {");
            this.out.println("public:");
            this.out.println("    VectorAdapter(const P* ptr, typename std::vector<T>::size_type size) : ptr((P*)ptr), size(size),");
            this.out.println("        vec2(ptr ? std::vector<T>((P*)ptr, (P*)ptr + size) : std::vector<T>()), vec(vec2) { }");
            this.out.println("    VectorAdapter(const std::vector<T>& vec) : ptr(0), size(0), vec2(vec), vec(vec2) { }");
            this.out.println("    VectorAdapter(      std::vector<T>& vec) : ptr(0), size(0), vec(vec) { }");
            this.out.println("    void assign(P* ptr, typename std::vector<T>::size_type size) {");
            this.out.println("        this->ptr = ptr;");
            this.out.println("        this->size = size;");
            this.out.println("        vec.assign(ptr, ptr + size);");
            this.out.println("    }");
            this.out.println("    static void deallocate(P* ptr) { delete[] ptr; }");
            this.out.println("    operator P*() {");
            this.out.println("        if (vec.size() > size) {");
            this.out.println("            ptr = new (std::nothrow) P[vec.size()];");
            this.out.println("        }");
            this.out.println("        if (ptr) {");
            this.out.println("            std::copy(vec.begin(), vec.end(), ptr);");
            this.out.println("        }");
            this.out.println("        size = vec.size();");
            this.out.println("        return ptr;");
            this.out.println("    }");
            this.out.println("    operator const P*()        { return &vec[0]; }");
            this.out.println("    operator std::vector<T>&() { return vec; }");
            this.out.println("    operator std::vector<T>*() { return ptr ? &vec : 0; }");
            this.out.println("    P* ptr;");
            this.out.println("    typename std::vector<T>::size_type size;");
            this.out.println("    std::vector<T> vec2;");
            this.out.println("    std::vector<T>& vec;");
            this.out.println("};");
            this.out.println();
            this.out.println("#include <string>");
            this.out.println("class JavaCPP_hidden StringAdapter {");
            this.out.println("public:");
            this.out.println("    StringAdapter(const          char* ptr, size_t size) : ptr((char*)ptr), size(size),");
            this.out.println("        str2(ptr ? (char*)ptr : \"\"), str(str2) { }");
            this.out.println("    StringAdapter(const signed   char* ptr, size_t size) : ptr((char*)ptr), size(size),");
            this.out.println("        str2(ptr ? (char*)ptr : \"\"), str(str2) { }");
            this.out.println("    StringAdapter(const unsigned char* ptr, size_t size) : ptr((char*)ptr), size(size),");
            this.out.println("        str2(ptr ? (char*)ptr : \"\"), str(str2) { }");
            this.out.println("    StringAdapter(const std::string& str) : ptr(0), size(0), str2(str), str(str2) { }");
            this.out.println("    StringAdapter(      std::string& str) : ptr(0), size(0), str(str) { }");
            this.out.println("    void assign(char* ptr, size_t size) {");
            this.out.println("        this->ptr = ptr;");
            this.out.println("        this->size = size;");
            this.out.println("        str.assign(ptr ? ptr : \"\");");
            this.out.println("    }");
            this.out.println("    static void deallocate(char* ptr) { free(ptr); }");
            this.out.println("    operator char*() {");
            this.out.println("        const char* c_str = str.c_str();");
            this.out.println("        if (ptr == NULL || strcmp(c_str, ptr) != 0) {");
            this.out.println("            ptr = strdup(c_str);");
            this.out.println("        }");
            this.out.println("        size = strlen(c_str) + 1;");
            this.out.println("        return ptr;");
            this.out.println("    }");
            this.out.println("    operator       signed   char*() { return (signed   char*)(operator char*)(); }");
            this.out.println("    operator       unsigned char*() { return (unsigned char*)(operator char*)(); }");
            this.out.println("    operator const          char*() { return                 str.c_str(); }");
            this.out.println("    operator const signed   char*() { return (signed   char*)str.c_str(); }");
            this.out.println("    operator const unsigned char*() { return (unsigned char*)str.c_str(); }");
            this.out.println("    operator         std::string&() { return str; }");
            this.out.println("    operator         std::string*() { return ptr ? &str : 0; }");
            this.out.println("    char* ptr;");
            this.out.println("    size_t size;");
            this.out.println("    std::string str2;");
            this.out.println("    std::string& str;");
            this.out.println("};");
            this.out.println();
        }
        if (!this.functionDefinitions.isEmpty()) {
            this.out.println("static JavaCPP_noinline void JavaCPP_detach(int detach) {");
            this.out.println("    if (detach > 0 && JavaCPP_vm->DetachCurrentThread() != 0) {");
            this.out.println("        JavaCPP_log(\"Could not detach the JavaVM from the current thread.\");");
            this.out.println("    }");
            this.out.println("}");
            this.out.println();
            this.out.println("static JavaCPP_noinline int JavaCPP_getEnv(JNIEnv** env) {");
            this.out.println("    int attached = 0;");
            this.out.println("    struct {");
            this.out.println("        JNIEnv **env;");
            this.out.println("        operator JNIEnv**() { return env; } // Android JNI");
            this.out.println("        operator void**() { return (void**)env; } // standard JNI");
            this.out.println("    } env2 = { env };");
            this.out.println("    JavaVM *vm = JavaCPP_vm;");
            this.out.println("    if (vm == NULL) {");
            if (this.out2 != null) {
                this.out.println("#ifndef ANDROID");
                this.out.println("        int size = 1;");
                this.out.println("        if (JNI_GetCreatedJavaVMs(&vm, 1, &size) != 0 || size == 0) {");
                this.out.println("#endif");
            }
            this.out.println("            JavaCPP_log(\"Could not get any created JavaVM.\");");
            this.out.println("            return -1;");
            if (this.out2 != null) {
                this.out.println("#ifndef ANDROID");
                this.out.println("        }");
                this.out.println("#endif");
            }
            this.out.println("    }");
            this.out.println("    if (vm->GetEnv((void**)env, JNI_VERSION_1_6) != JNI_OK) {");
            this.out.println("        if (vm->AttachCurrentThread(env2, NULL) != 0) {");
            this.out.println("            JavaCPP_log(\"Could not attach the JavaVM to the current thread.\");");
            this.out.println("            return -1;");
            this.out.println("        }");
            this.out.println("        attached = 1;");
            this.out.println("    }");
            this.out.println("    if (JavaCPP_vm == NULL) {");
            this.out.println("        if (JNI_OnLoad(vm, NULL) < 0) {");
            this.out.println("            JavaCPP_detach(attached);");
            this.out.println("            return -1;");
            this.out.println("        }");
            this.out.println("    }");
            this.out.println("    return attached;");
            this.out.println("}");
            this.out.println();
        }
        Iterator i$2 = this.functionDefinitions.iterator();
        while (i$2.hasNext()) {
            String s3 = (String) i$2.next();
            this.out.println(s3);
        }
        this.out.println();
        Iterator i$3 = this.functionPointers.iterator();
        while (i$3.hasNext()) {
            String s4 = (String) i$3.next();
            this.out.println(s4);
        }
        this.out.println();
        Iterator i$4 = this.deallocators.iterator();
        while (i$4.hasNext()) {
            Class c2 = (Class) i$4.next();
            String name = "JavaCPP_" + mangle(c2.getName());
            this.out.print("static void " + name + "_deallocate(");
            if (FunctionPointer.class.isAssignableFrom(c2)) {
                this.out.println(getFunctionClassName(c2) + "* p) { JNIEnv *e; int a = JavaCPP_getEnv(&e); if (a >= 0) e->DeleteWeakGlobalRef(p->obj); delete p; JavaCPP_detach(a); }");
            } else {
                String[] typeName = getCPPTypeName(c2);
                this.out.println(typeName[0] + " p" + typeName[1] + ") { delete p; }");
            }
        }
        Iterator i$5 = this.arrayDeallocators.iterator();
        while (i$5.hasNext()) {
            Class c3 = (Class) i$5.next();
            String name2 = "JavaCPP_" + mangle(c3.getName());
            String[] typeName2 = getCPPTypeName(c3);
            this.out.println("static void " + name2 + "_deallocateArray(" + typeName2[0] + " p" + typeName2[1] + ") { delete[] p; }");
        }
        this.out.println();
        this.out.println("extern \"C\" {");
        if (this.out2 != null) {
            this.out2.println();
            this.out2.println("#ifdef __cplusplus");
            this.out2.println("extern \"C\" {");
            this.out2.println("#endif");
            this.out2.println("JNIIMPORT int JavaCPP_init(int argc, const char *argv[]);");
            this.out.println();
            this.out.println("JNIEXPORT int JavaCPP_init(int argc, const char *argv[]) {");
            this.out.println("#ifdef ANDROID");
            this.out.println("    return JNI_OK;");
            this.out.println("#else");
            this.out.println("    JavaVM *vm;");
            this.out.println("    JNIEnv *env;");
            this.out.println("    int nOptions = 1 + (argc > 255 ? 255 : argc);");
            this.out.println("    JavaVMOption options[256] = { { NULL } };");
            this.out.println("    options[0].optionString = (char*)\"-Djava.class.path=" + classPath.replace('\\', '/') + "\";");
            this.out.println("    for (int i = 1; i < nOptions && argv != NULL; i++) {");
            this.out.println("        options[i].optionString = (char*)argv[i - 1];");
            this.out.println("    }");
            this.out.println("    JavaVMInitArgs vm_args = { JNI_VERSION_1_6, nOptions, options };");
            this.out.println("    return JNI_CreateJavaVM(&vm, (void **)&env, &vm_args);");
            this.out.println("#endif");
            this.out.println("}");
        }
        this.out.println();
        this.out.println("JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved) {");
        this.out.println("    JNIEnv* env;");
        this.out.println("    if (vm->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {");
        this.out.println("        JavaCPP_log(\"Could not get JNIEnv for JNI_VERSION_1_6 inside JNI_OnLoad().\");");
        this.out.println("        return JNI_ERR;");
        this.out.println("    }");
        this.out.println("    if (JavaCPP_vm == vm) {");
        this.out.println("        return env->GetVersion();");
        this.out.println("    }");
        this.out.println("    JavaCPP_vm = vm;");
        this.out.println("    const char* members[" + this.jclasses.size() + "][" + maxMemberSize + "] = {");
        Iterator<Class> classIterator2 = this.jclasses.iterator();
        while (classIterator2.hasNext()) {
            this.out.print("            { ");
            LinkedList<String> m2 = this.members.get(classIterator2.next());
            Iterator<String> memberIterator = m2 == null ? null : m2.iterator();
            while (memberIterator != null && memberIterator.hasNext()) {
                this.out.print("\"" + memberIterator.next() + "\"");
                if (memberIterator.hasNext()) {
                    this.out.print(", ");
                }
            }
            this.out.print(" }");
            if (classIterator2.hasNext()) {
                this.out.println(",");
            }
        }
        this.out.println(" };");
        this.out.println("    int offsets[" + this.jclasses.size() + "][" + maxMemberSize + "] = {");
        Iterator<Class> classIterator3 = this.jclasses.iterator();
        while (classIterator3.hasNext()) {
            this.out.print("            { ");
            Class c4 = classIterator3.next();
            LinkedList<String> m3 = this.members.get(c4);
            Iterator<String> memberIterator2 = m3 == null ? null : m3.iterator();
            while (memberIterator2 != null && memberIterator2.hasNext()) {
                String valueTypeName = getValueTypeName(getCPPTypeName(c4));
                String memberName = memberIterator2.next();
                if ("sizeof".equals(memberName)) {
                    if ("void".equals(valueTypeName)) {
                        valueTypeName = "void*";
                    }
                    this.out.print("sizeof(" + valueTypeName + ")");
                } else {
                    this.out.print("offsetof(" + valueTypeName + "," + memberName + ")");
                }
                if (memberIterator2.hasNext()) {
                    this.out.print(", ");
                }
            }
            this.out.print(" }");
            if (classIterator3.hasNext()) {
                this.out.println(",");
            }
        }
        this.out.println(" };");
        this.out.print("    int memberOffsetSizes[" + this.jclasses.size() + "] = { ");
        Iterator<Class> classIterator4 = this.jclasses.iterator();
        while (classIterator4.hasNext()) {
            LinkedList<String> m4 = this.members.get(classIterator4.next());
            this.out.print(m4 == null ? 0 : m4.size());
            if (classIterator4.hasNext()) {
                this.out.print(", ");
            }
        }
        this.out.println(" };");
        this.out.println("    jmethodID putMemberOffsetMID = env->GetStaticMethodID(JavaCPP_getClass(env, " + this.jclasses.register(Loader.class) + "), \"putMemberOffset\", \"(Ljava/lang/String;Ljava/lang/String;I)V\");");
        this.out.println("    if (putMemberOffsetMID == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting method ID of Loader.putMemberOffset().\");");
        this.out.println("        return JNI_ERR;");
        this.out.println("    }");
        this.out.println("    for (int i = 0; i < " + this.jclasses.size() + " && !env->ExceptionCheck(); i++) {");
        this.out.println("        for (int j = 0; j < memberOffsetSizes[i] && !env->ExceptionCheck(); j++) {");
        this.out.println("            if (env->PushLocalFrame(2) == 0) {");
        this.out.println("                jvalue args[3];");
        this.out.println("                args[0].l = env->NewStringUTF(JavaCPP_classNames[i]);");
        this.out.println("                args[1].l = env->NewStringUTF(members[i][j]);");
        this.out.println("                args[2].i = offsets[i][j];");
        this.out.println("                env->CallStaticVoidMethodA(JavaCPP_getClass(env, " + this.jclasses.register(Loader.class) + "), putMemberOffsetMID, args);");
        this.out.println("                env->PopLocalFrame(NULL);");
        this.out.println("            }");
        this.out.println("        }");
        this.out.println("    }");
        this.out.println("    JavaCPP_initMID = env->GetMethodID(JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), \"init\", \"(JIJ)V\");");
        this.out.println("    if (JavaCPP_initMID == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting method ID of Pointer.init().\");");
        this.out.println("        return JNI_ERR;");
        this.out.println("    }");
        this.out.println("    JavaCPP_addressFID = env->GetFieldID(JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), \"address\", \"J\");");
        this.out.println("    if (JavaCPP_addressFID == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting field ID of Pointer.address.\");");
        this.out.println("        return JNI_ERR;");
        this.out.println("    }");
        this.out.println("    JavaCPP_positionFID = env->GetFieldID(JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), \"position\", \"I\");");
        this.out.println("    if (JavaCPP_positionFID == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting field ID of Pointer.position.\");");
        this.out.println("        return JNI_ERR;");
        this.out.println("    }");
        this.out.println("    JavaCPP_limitFID = env->GetFieldID(JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), \"limit\", \"I\");");
        this.out.println("    if (JavaCPP_limitFID == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting field ID of Pointer.limit.\");");
        this.out.println("        return JNI_ERR;");
        this.out.println("    }");
        this.out.println("    JavaCPP_capacityFID = env->GetFieldID(JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), \"capacity\", \"I\");");
        this.out.println("    if (JavaCPP_capacityFID == NULL || env->ExceptionCheck()) {");
        this.out.println("        JavaCPP_log(\"Error getting field ID of Pointer.capacity.\");");
        this.out.println("        return JNI_ERR;");
        this.out.println("    }");
        Iterator<Class> classIterator5 = this.jclassesInit.iterator();
        while (classIterator5.hasNext()) {
            Class c5 = classIterator5.next();
            if (c5 != Pointer.class) {
                this.out.println("    if (JavaCPP_getClass(env, " + this.jclasses.indexOf(c5) + ") == NULL) {");
                this.out.println("        return JNI_ERR;");
                this.out.println("    }");
            }
        }
        this.out.println("    return env->GetVersion();");
        this.out.println("}");
        this.out.println();
        if (this.out2 != null) {
            this.out2.println("JNIIMPORT int JavaCPP_uninit();");
            this.out2.println();
            this.out.println("JNIEXPORT int JavaCPP_uninit() {");
            this.out.println("#ifdef ANDROID");
            this.out.println("    return JNI_OK;");
            this.out.println("#else");
            this.out.println("    JavaVM *vm = JavaCPP_vm;");
            this.out.println("    JNI_OnUnload(JavaCPP_vm, NULL);");
            this.out.println("    return vm->DestroyJavaVM();");
            this.out.println("#endif");
            this.out.println("}");
        }
        this.out.println();
        this.out.println("JNIEXPORT void JNICALL JNI_OnUnload(JavaVM* vm, void* reserved) {");
        this.out.println("    JNIEnv* env;");
        this.out.println("    if (vm->GetEnv((void**)&env, JNI_VERSION_1_6) != JNI_OK) {");
        this.out.println("        JavaCPP_log(\"Could not get JNIEnv for JNI_VERSION_1_6 inside JNI_OnUnLoad().\");");
        this.out.println("        return;");
        this.out.println("    }");
        this.out.println("    for (int i = 0; i < " + this.jclasses.size() + "; i++) {");
        this.out.println("        env->DeleteWeakGlobalRef(JavaCPP_classes[i]);");
        this.out.println("        JavaCPP_classes[i] = NULL;");
        this.out.println("    }");
        this.out.println("    JavaCPP_vm = NULL;");
        this.out.println("}");
        this.out.println();
        Iterator i$6 = baseClasses.iterator();
        while (i$6.hasNext()) {
            doMethods(i$6.next());
        }
        boolean didSomethingUseful = false;
        for (Class<?> cls : classes) {
            try {
                didSomethingUseful |= doMethods(cls);
            } catch (NoClassDefFoundError e) {
                logger.log(Level.WARNING, "Could not generate code for class " + cls.getCanonicalName() + ": " + e);
            }
        }
        this.out.println("}");
        this.out.println();
        if (this.out2 != null) {
            this.out2.println("#ifdef __cplusplus");
            this.out2.println("}");
            this.out2.println("#endif");
        }
        return didSomethingUseful;
    }

    private boolean doMethods(Class<?> cls) throws SecurityException {
        if (!checkPlatform(cls)) {
            return false;
        }
        LinkedList<String> memberList = this.members.get(cls);
        if (!cls.isAnnotationPresent(Opaque.class) && !FunctionPointer.class.isAssignableFrom(cls)) {
            if (memberList == null) {
                HashMap<Class, LinkedList<String>> map = this.members;
                memberList = new LinkedList<>();
                map.put(cls, memberList);
            }
            if (!memberList.contains("sizeof")) {
                memberList.add("sizeof");
            }
        }
        boolean didSomething = false;
        Class[] classes = cls.getDeclaredClasses();
        for (int i = 0; i < classes.length; i++) {
            if (Pointer.class.isAssignableFrom(classes[i]) || Pointer.Deallocator.class.isAssignableFrom(classes[i])) {
                didSomething |= doMethods(classes[i]);
            }
        }
        Method[] methods = cls.getDeclaredMethods();
        boolean[] callbackAllocators = new boolean[methods.length];
        Method functionMethod = getFunctionMethod(cls, callbackAllocators);
        if (functionMethod != null) {
            String[] typeName = getCPPTypeName(cls);
            String[] returnConvention = typeName[0].split("\\(");
            returnConvention[1] = getValueTypeName(returnConvention[1]);
            String parameterDeclaration = typeName[1].substring(1);
            String instanceTypeName = getFunctionClassName(cls);
            this.functionDefinitions.register("struct JavaCPP_hidden " + instanceTypeName + " {\n    " + instanceTypeName + "() : ptr(NULL), obj(NULL) { }\n    " + returnConvention[0] + "operator()" + parameterDeclaration + ";\n    " + typeName[0] + "ptr" + typeName[1] + ";\n    jobject obj; static jmethodID mid;\n};\njmethodID " + instanceTypeName + "::mid = NULL;");
        }
        boolean firstCallback = true;
        for (int i2 = 0; i2 < methods.length; i2++) {
            String nativeName = mangle(cls.getName()) + "_" + mangle(methods[i2].getName());
            if (checkPlatform((Platform) methods[i2].getAnnotation(Platform.class))) {
                MethodInformation methodInfo = getMethodInformation(methods[i2]);
                String callbackName = "JavaCPP_" + nativeName + "_callback";
                if (callbackAllocators[i2] && functionMethod == null) {
                    logger.log(Level.WARNING, "No callback method call() or apply() has been not declared in \"" + cls.getCanonicalName() + "\". No code will be generated for callback allocator.");
                } else {
                    if (callbackAllocators[i2] || (methods[i2].equals(functionMethod) && methodInfo == null)) {
                        Name name = (Name) methods[i2].getAnnotation(Name.class);
                        if (name != null && name.value().length > 0 && name.value()[0].length() > 0) {
                            callbackName = name.value()[0];
                        }
                        doCallback(cls, functionMethod, callbackName, firstCallback);
                        firstCallback = false;
                        didSomething = true;
                    }
                    if (methodInfo != null) {
                        if ((methodInfo.memberGetter || methodInfo.memberSetter) && !methodInfo.noOffset && memberList != null && !Modifier.isStatic(methodInfo.modifiers) && !memberList.contains(methodInfo.memberName[0])) {
                            memberList.add(methodInfo.memberName[0]);
                        }
                        didSomething = true;
                        this.out.print("JNIEXPORT " + getJNITypeName(methodInfo.returnType) + " JNICALL Java_" + nativeName);
                        if (methodInfo.overloaded) {
                            this.out.print("__" + mangle(getSignature(methodInfo.parameterTypes)));
                        }
                        if (Modifier.isStatic(methodInfo.modifiers)) {
                            this.out.print("(JNIEnv* env, jclass cls");
                        } else {
                            this.out.print("(JNIEnv* env, jobject obj");
                        }
                        for (int j = 0; j < methodInfo.parameterTypes.length; j++) {
                            this.out.print(", " + getJNITypeName(methodInfo.parameterTypes[j]) + " arg" + j);
                        }
                        this.out.println(") {");
                        if (callbackAllocators[i2]) {
                            doCallbackAllocator(cls, callbackName);
                        } else {
                            if (!Modifier.isStatic(methodInfo.modifiers) && !methodInfo.allocator && !methodInfo.arrayAllocator && !methodInfo.deallocator) {
                                String[] typeName2 = getCPPTypeName(cls);
                                if ("void*".equals(typeName2[0])) {
                                    typeName2[0] = "char*";
                                } else if (FunctionPointer.class.isAssignableFrom(cls)) {
                                    typeName2[0] = getFunctionClassName(cls) + "*";
                                    typeName2[1] = "";
                                }
                                this.out.println("    " + typeName2[0] + " ptr" + typeName2[1] + " = (" + typeName2[0] + typeName2[1] + ")jlong_to_ptr(env->GetLongField(obj, JavaCPP_addressFID));");
                                this.out.println("    if (ptr == NULL) {");
                                this.out.println("        env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.register(NullPointerException.class) + "), \"This pointer address is NULL.\");");
                                this.out.println("        return" + (methodInfo.returnType == Void.TYPE ? ";" : " 0;"));
                                this.out.println("    }");
                                if (FunctionPointer.class.isAssignableFrom(cls)) {
                                    this.out.println("    if (ptr->ptr == NULL) {");
                                    this.out.println("        env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.register(NullPointerException.class) + "), \"This function pointer address is NULL.\");");
                                    this.out.println("        return" + (methodInfo.returnType == Void.TYPE ? ";" : " 0;"));
                                    this.out.println("    }");
                                }
                                if (!cls.isAnnotationPresent(Opaque.class)) {
                                    this.out.println("    jint position = env->GetIntField(obj, JavaCPP_positionFID);");
                                    this.out.println("    ptr += position;");
                                    if (methodInfo.bufferGetter) {
                                        this.out.println("    jint size = env->GetIntField(obj, JavaCPP_limitFID);");
                                        this.out.println("    size -= position;");
                                    }
                                }
                            }
                            doParametersBefore(methodInfo);
                            String returnPrefix = doReturnBefore(methodInfo);
                            doCall(methodInfo, returnPrefix);
                            doReturnAfter(methodInfo);
                            doParametersAfter(methodInfo);
                            if (methodInfo.throwsException != null) {
                                this.out.println("    if (exc != NULL) {");
                                this.out.println("        env->Throw(exc);");
                                this.out.println("    }");
                            }
                            if (methodInfo.returnType != Void.TYPE) {
                                this.out.println("    return rarg;");
                            }
                            this.out.println("}");
                        }
                    }
                }
            }
        }
        this.out.println();
        return didSomething;
    }

    private void doParametersBefore(MethodInformation methodInfo) throws SecurityException {
        String adapterLine = "";
        AdapterInformation prevAdapterInfo = null;
        for (int j = 0; j < methodInfo.parameterTypes.length; j++) {
            if (!methodInfo.parameterTypes[j].isPrimitive()) {
                Annotation passBy = getParameterBy(methodInfo, j);
                String cast = getParameterCast(methodInfo, j);
                String[] typeName = getCPPTypeName(methodInfo.parameterTypes[j]);
                AdapterInformation adapterInfo = getParameterAdapterInformation(false, methodInfo, j);
                if (FunctionPointer.class.isAssignableFrom(methodInfo.parameterTypes[j])) {
                    if (methodInfo.parameterTypes[j] == FunctionPointer.class) {
                        logger.log(Level.WARNING, "Method \"" + methodInfo.method + "\" has an abstract FunctionPointer parameter, but a concrete subclass is required. Compilation will most likely fail.");
                    }
                    typeName[0] = getFunctionClassName(methodInfo.parameterTypes[j]) + "*";
                    typeName[1] = "";
                }
                if (typeName[0].length() == 0 || methodInfo.parameterRaw[j]) {
                    methodInfo.parameterRaw[j] = true;
                    typeName[0] = getJNITypeName(methodInfo.parameterTypes[j]);
                    this.out.println("    " + typeName[0] + " ptr" + j + " = arg" + j + ";");
                } else {
                    if ("void*".equals(typeName[0])) {
                        typeName[0] = "char*";
                    }
                    this.out.print("    " + typeName[0] + " ptr" + j + typeName[1] + " = ");
                    if (Pointer.class.isAssignableFrom(methodInfo.parameterTypes[j])) {
                        this.out.println("arg" + j + " == NULL ? NULL : (" + typeName[0] + typeName[1] + ")jlong_to_ptr(env->GetLongField(arg" + j + ", JavaCPP_addressFID));");
                        if ((j == 0 && FunctionPointer.class.isAssignableFrom(methodInfo.cls) && methodInfo.cls.isAnnotationPresent(Namespace.class)) || (passBy instanceof ByVal) || (passBy instanceof ByRef)) {
                            this.out.println("    if (ptr" + j + " == NULL) {");
                            this.out.println("        env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.register(NullPointerException.class) + "), \"Pointer address of argument " + j + " is NULL.\");");
                            this.out.println("        return" + (methodInfo.returnType == Void.TYPE ? ";" : " 0;"));
                            this.out.println("    }");
                        }
                        if (adapterInfo != null || prevAdapterInfo != null) {
                            this.out.println("    jint size" + j + " = arg" + j + " == NULL ? 0 : env->GetIntField(arg" + j + ", JavaCPP_limitFID);");
                        }
                        if (!methodInfo.parameterTypes[j].isAnnotationPresent(Opaque.class)) {
                            this.out.println("    jint position" + j + " = arg" + j + " == NULL ? 0 : env->GetIntField(arg" + j + ", JavaCPP_positionFID);");
                            this.out.println("    ptr" + j + " += position" + j + ";");
                            if (adapterInfo != null || prevAdapterInfo != null) {
                                this.out.println("    size" + j + " -= position" + j + ";");
                            }
                        }
                    } else if (methodInfo.parameterTypes[j] == String.class) {
                        this.out.println("arg" + j + " == NULL ? NULL : env->GetStringUTFChars(arg" + j + ", NULL);");
                        if (adapterInfo != null || prevAdapterInfo != null) {
                            this.out.println("    jint size" + j + " = 0;");
                        }
                    } else if (methodInfo.parameterTypes[j].isArray() && methodInfo.parameterTypes[j].getComponentType().isPrimitive()) {
                        this.out.print("arg" + j + " == NULL ? NULL : ");
                        String s = methodInfo.parameterTypes[j].getComponentType().getName();
                        if (methodInfo.valueGetter || methodInfo.valueSetter || methodInfo.memberGetter || methodInfo.memberSetter) {
                            this.out.println("(j" + s + "*)env->GetPrimitiveArrayCritical(arg" + j + ", NULL);");
                        } else {
                            this.out.println("env->Get" + (Character.toUpperCase(s.charAt(0)) + s.substring(1)) + "ArrayElements(arg" + j + ", NULL);");
                        }
                        if (adapterInfo != null || prevAdapterInfo != null) {
                            this.out.println("    jint size" + j + " = arg" + j + " == NULL ? 0 : env->GetArrayLength(arg" + j + ");");
                        }
                    } else if (Buffer.class.isAssignableFrom(methodInfo.parameterTypes[j])) {
                        this.out.println("arg" + j + " == NULL ? NULL : (" + typeName[0] + typeName[1] + ")env->GetDirectBufferAddress(arg" + j + ");");
                        if (adapterInfo != null || prevAdapterInfo != null) {
                            this.out.println("    jint size" + j + " = arg" + j + " == NULL ? 0 : env->GetDirectBufferCapacity(arg" + j + ");");
                        }
                    } else {
                        this.out.println("arg" + j + ";");
                        logger.log(Level.WARNING, "Method \"" + methodInfo.method + "\" has an unsupported parameter of type \"" + methodInfo.parameterTypes[j].getCanonicalName() + "\". Compilation will most likely fail.");
                    }
                    if (adapterInfo != null) {
                        this.usesAdapters = true;
                        adapterLine = "    " + adapterInfo.name + " adapter" + j + "(";
                        prevAdapterInfo = adapterInfo;
                    }
                    if (prevAdapterInfo != null) {
                        if (!FunctionPointer.class.isAssignableFrom(methodInfo.cls)) {
                            adapterLine = adapterLine + cast;
                        }
                        adapterLine = adapterLine + "ptr" + j + ", size" + j;
                        int i = prevAdapterInfo.argc - 1;
                        prevAdapterInfo.argc = i;
                        if (i > 0) {
                            adapterLine = adapterLine + ", ";
                        }
                    }
                    if (prevAdapterInfo != null && prevAdapterInfo.argc <= 0) {
                        this.out.println(adapterLine + ");");
                        prevAdapterInfo = null;
                    }
                }
            }
        }
    }

    private String doReturnBefore(MethodInformation methodInfo) throws SecurityException {
        String returnPrefix = "";
        if (methodInfo.returnType == Void.TYPE) {
            if (methodInfo.allocator || methodInfo.arrayAllocator) {
                if (methodInfo.cls != Pointer.class) {
                    this.out.println("    if (!env->IsSameObject(env->GetObjectClass(obj), JavaCPP_getClass(env, " + this.jclasses.register(methodInfo.cls) + "))) {");
                    this.out.println("        return;");
                    this.out.println("    }");
                }
                String[] typeName = getCPPTypeName(methodInfo.cls);
                returnPrefix = typeName[0] + " rptr" + typeName[1] + " = ";
            }
        } else {
            String cast = getCast(methodInfo.annotations, methodInfo.returnType);
            String[] typeName2 = getCastedCPPTypeName(methodInfo.annotations, methodInfo.returnType);
            if (methodInfo.valueSetter || methodInfo.memberSetter || methodInfo.noReturnGetter) {
                this.out.println("    jobject rarg = obj;");
            } else if (methodInfo.returnType.isPrimitive()) {
                this.out.println("    " + getJNITypeName(methodInfo.returnType) + " rarg = 0;");
                returnPrefix = typeName2[0] + " rvalue" + typeName2[1] + " = " + cast;
            } else {
                Annotation returnBy = getBy(methodInfo.annotations);
                String valueTypeName = getValueTypeName(typeName2);
                returnPrefix = "rptr = " + cast;
                if (typeName2[0].length() == 0 || methodInfo.returnRaw) {
                    methodInfo.returnRaw = true;
                    typeName2[0] = getJNITypeName(methodInfo.returnType);
                    this.out.println("    " + typeName2[0] + " rarg = NULL;");
                    this.out.println("    " + typeName2[0] + " rptr;");
                } else if (Pointer.class.isAssignableFrom(methodInfo.returnType) || Buffer.class.isAssignableFrom(methodInfo.returnType) || (methodInfo.returnType.isArray() && methodInfo.returnType.getComponentType().isPrimitive())) {
                    if (FunctionPointer.class.isAssignableFrom(methodInfo.returnType)) {
                        typeName2[0] = getFunctionClassName(methodInfo.returnType) + "*";
                        typeName2[1] = "";
                        valueTypeName = getValueTypeName(typeName2);
                        returnPrefix = "if (rptr != NULL) rptr->ptr = ";
                    }
                    if (returnBy instanceof ByVal) {
                        returnPrefix = returnPrefix + (getNoException(methodInfo.returnType, methodInfo.method) ? "new (std::nothrow) " : "new ") + valueTypeName + typeName2[1] + "(";
                    } else if (returnBy instanceof ByRef) {
                        returnPrefix = returnPrefix + "&";
                    } else if (returnBy instanceof ByPtrPtr) {
                        if (cast.length() > 0) {
                            typeName2[0] = typeName2[0].substring(0, typeName2[0].length() - 1);
                        }
                        returnPrefix = "rptr = NULL; " + typeName2[0] + "* rptrptr" + typeName2[1] + " = " + cast;
                    }
                    if (methodInfo.bufferGetter) {
                        this.out.println("    jobject rarg = NULL;");
                        this.out.println("    char* rptr;");
                    } else {
                        this.out.println("    " + getJNITypeName(methodInfo.returnType) + " rarg = NULL;");
                        this.out.println("    " + typeName2[0] + " rptr" + typeName2[1] + ";");
                    }
                    if (FunctionPointer.class.isAssignableFrom(methodInfo.returnType)) {
                        this.out.println("    rptr = new (std::nothrow) " + valueTypeName + ";");
                    }
                } else if (methodInfo.returnType == String.class) {
                    this.out.println("    jstring rarg = NULL;");
                    this.out.println("    const char* rptr;");
                    returnPrefix = returnBy instanceof ByRef ? "std::string rstr(" : returnPrefix + "(const char*)";
                } else {
                    logger.log(Level.WARNING, "Method \"" + methodInfo.method + "\" has unsupported return type \"" + methodInfo.returnType.getCanonicalName() + "\". Compilation will most likely fail.");
                }
                AdapterInformation adapterInfo = getAdapterInformation(false, valueTypeName, methodInfo.annotations);
                if (adapterInfo != null) {
                    this.usesAdapters = true;
                    returnPrefix = adapterInfo.name + " radapter(";
                }
            }
        }
        if (methodInfo.throwsException != null) {
            this.out.println("    jthrowable exc = NULL;");
            this.out.println("    try {");
        }
        return returnPrefix;
    }

    private void doCall(MethodInformation methodInfo, String returnPrefix) throws SecurityException {
        String indent = methodInfo.throwsException != null ? "        " : "    ";
        String prefix = "(";
        String suffix = ")";
        int skipParameters = 0;
        boolean index = methodInfo.method.isAnnotationPresent(Index.class) || (methodInfo.pairedMethod != null && methodInfo.pairedMethod.isAnnotationPresent(Index.class));
        if (methodInfo.deallocator) {
            this.out.println(indent + "void* allocatedAddress = jlong_to_ptr(arg0);");
            this.out.println(indent + "void (*deallocatorAddress)(void*) = (void(*)(void*))jlong_to_ptr(arg1);");
            this.out.println(indent + "if (deallocatorAddress != NULL && allocatedAddress != NULL) {");
            this.out.println(indent + "    (*deallocatorAddress)(allocatedAddress);");
            this.out.println(indent + "}");
            return;
        }
        if (methodInfo.valueGetter || methodInfo.valueSetter || methodInfo.memberGetter || methodInfo.memberSetter) {
            boolean wantsPointer = false;
            int k = methodInfo.parameterTypes.length - 1;
            if ((methodInfo.valueSetter || methodInfo.memberSetter) && !(getParameterBy(methodInfo, k) instanceof ByRef) && getParameterAdapterInformation(false, methodInfo, k) == null && methodInfo.parameterTypes[k] == String.class) {
                this.out.print(indent + "strcpy((char*)");
                wantsPointer = true;
                prefix = ", ";
            } else if (k >= 1 && methodInfo.parameterTypes[0].isArray() && methodInfo.parameterTypes[0].getComponentType().isPrimitive() && (methodInfo.parameterTypes[1] == Integer.TYPE || methodInfo.parameterTypes[1] == Long.TYPE)) {
                this.out.print(indent + "memcpy(");
                wantsPointer = true;
                prefix = ", ";
                if (!methodInfo.memberGetter && !methodInfo.valueGetter) {
                    prefix = ", ptr0 + arg1, ";
                } else {
                    this.out.print("ptr0 + arg1, ");
                }
                skipParameters = 2;
                suffix = " * sizeof(*ptr0))";
            } else {
                this.out.print(indent + returnPrefix);
                prefix = (methodInfo.valueGetter || methodInfo.memberGetter) ? "" : " = ";
                suffix = "";
            }
            if (Modifier.isStatic(methodInfo.modifiers)) {
                this.out.print(getCPPScopeName(methodInfo));
            } else if (methodInfo.memberGetter || methodInfo.memberSetter) {
                if (index) {
                    this.out.print("(*ptr)");
                    prefix = "." + methodInfo.memberName[0] + prefix;
                } else {
                    this.out.print("ptr->" + methodInfo.memberName[0]);
                }
            } else {
                this.out.print(index ? "(*ptr)" : (methodInfo.dim > 0 || wantsPointer) ? "ptr" : "*ptr");
            }
        } else if (methodInfo.bufferGetter) {
            this.out.print(indent + returnPrefix + "ptr");
            prefix = "";
            suffix = "";
        } else {
            this.out.print(indent + returnPrefix);
            if (FunctionPointer.class.isAssignableFrom(methodInfo.cls)) {
                if (methodInfo.cls.isAnnotationPresent(Namespace.class)) {
                    this.out.print("(ptr0->*(ptr->ptr))");
                    skipParameters = 1;
                } else {
                    this.out.print("(*ptr->ptr)");
                }
            } else if (methodInfo.allocator) {
                String[] typeName = getCPPTypeName(methodInfo.cls);
                String valueTypeName = getValueTypeName(typeName);
                if (methodInfo.cls == Pointer.class) {
                    prefix = "";
                    suffix = "";
                } else {
                    this.out.print((getNoException(methodInfo.cls, methodInfo.method) ? "new (std::nothrow) " : "new ") + valueTypeName + typeName[1]);
                    if (methodInfo.arrayAllocator) {
                        prefix = "[";
                        suffix = "]";
                    }
                }
            } else if (Modifier.isStatic(methodInfo.modifiers)) {
                this.out.print(getCPPScopeName(methodInfo));
            } else if (index) {
                this.out.print("(*ptr)");
                prefix = "." + methodInfo.memberName[0] + "(";
            } else {
                this.out.print("ptr->" + methodInfo.memberName[0]);
            }
        }
        for (int j = skipParameters; j < methodInfo.dim; j++) {
            this.out.print("[" + getParameterCast(methodInfo, j) + (methodInfo.parameterTypes[j].isPrimitive() ? "arg" : "ptr") + j + "]");
        }
        if (methodInfo.memberName.length > 1) {
            this.out.print(methodInfo.memberName[1]);
        }
        this.out.print(prefix);
        if (methodInfo.withEnv) {
            this.out.print(Modifier.isStatic(methodInfo.modifiers) ? "env, cls" : "env, obj");
            if ((methodInfo.parameterTypes.length - skipParameters) - methodInfo.dim > 0) {
                this.out.print(", ");
            }
        }
        int j2 = skipParameters + methodInfo.dim;
        while (j2 < methodInfo.parameterTypes.length) {
            Annotation passBy = getParameterBy(methodInfo, j2);
            String cast = getParameterCast(methodInfo, j2);
            AdapterInformation adapterInfo = getParameterAdapterInformation(false, methodInfo, j2);
            if (("(void*)".equals(cast) || "(void *)".equals(cast)) && methodInfo.parameterTypes[j2] == Long.TYPE) {
                this.out.print("jlong_to_ptr(arg" + j2 + ")");
            } else if (methodInfo.parameterTypes[j2].isPrimitive()) {
                this.out.print(cast + "arg" + j2);
            } else if (adapterInfo != null) {
                String cast2 = adapterInfo.cast.trim();
                if (cast2.length() > 0 && !cast2.startsWith("(") && !cast2.endsWith(")")) {
                    cast2 = "(" + cast2 + ")";
                }
                this.out.print(cast2 + "adapter" + j2);
                j2 += adapterInfo.argc - 1;
            } else if (FunctionPointer.class.isAssignableFrom(methodInfo.parameterTypes[j2]) && passBy == null) {
                this.out.print(cast + "(ptr" + j2 + " == NULL ? NULL : ptr" + j2 + "->ptr)");
            } else if ((passBy instanceof ByVal) || ((passBy instanceof ByRef) && methodInfo.parameterTypes[j2] != String.class)) {
                this.out.print("*" + cast + "ptr" + j2);
            } else if (passBy instanceof ByPtrPtr) {
                this.out.print(cast + "(arg" + j2 + " == NULL ? NULL : &ptr" + j2 + ")");
            } else {
                this.out.print(cast + "ptr" + j2);
            }
            if (j2 < methodInfo.parameterTypes.length - 1) {
                this.out.print(", ");
            }
            j2++;
        }
        this.out.print(suffix);
        if (methodInfo.memberName.length > 2) {
            this.out.print(methodInfo.memberName[2]);
        }
        if ((getBy(methodInfo.annotations) instanceof ByRef) && methodInfo.returnType == String.class) {
            this.out.print(");\n" + indent + "rptr = rstr.c_str()");
        }
    }

    private void doReturnAfter(MethodInformation methodInfo) throws SecurityException {
        String indent = methodInfo.throwsException != null ? "        " : "    ";
        String[] typeName = getCastedCPPTypeName(methodInfo.annotations, methodInfo.returnType);
        Annotation returnBy = getBy(methodInfo.annotations);
        String valueTypeName = getValueTypeName(typeName);
        AdapterInformation adapterInfo = getAdapterInformation(false, valueTypeName, methodInfo.annotations);
        String suffix = methodInfo.deallocator ? "" : ";";
        if (!methodInfo.returnType.isPrimitive() && adapterInfo != null) {
            suffix = ")" + suffix;
        }
        if (Pointer.class.isAssignableFrom(methodInfo.returnType) || (methodInfo.returnType.isArray() && methodInfo.returnType.getComponentType().isPrimitive())) {
            if (returnBy instanceof ByVal) {
                suffix = ")" + suffix;
            } else if (returnBy instanceof ByPtrPtr) {
                this.out.println(suffix);
                suffix = "";
                this.out.println(indent + "if (rptrptr == NULL) {");
                this.out.println(indent + "    env->ThrowNew(JavaCPP_getClass(env, " + this.jclasses.register(NullPointerException.class) + "), \"Return pointer address is NULL.\");");
                this.out.println(indent + "} else {");
                this.out.println(indent + "    rptr = *rptrptr;");
                this.out.println(indent + "}");
            }
        }
        this.out.println(suffix);
        if (methodInfo.returnType == Void.TYPE) {
            if (methodInfo.allocator || methodInfo.arrayAllocator) {
                this.out.println(indent + "jint rcapacity = " + (methodInfo.arrayAllocator ? "arg0;" : "1;"));
                boolean noDeallocator = methodInfo.cls == Pointer.class || methodInfo.cls.isAnnotationPresent(NoDeallocator.class);
                Annotation[] arr$ = methodInfo.annotations;
                int len$ = arr$.length;
                int i$ = 0;
                while (true) {
                    if (i$ >= len$) {
                        break;
                    }
                    Annotation a = arr$[i$];
                    if (!(a instanceof NoDeallocator)) {
                        i$++;
                    } else {
                        noDeallocator = true;
                        break;
                    }
                }
                if (!noDeallocator) {
                    this.out.println(indent + "jvalue args[3];");
                    this.out.println(indent + "args[0].j = ptr_to_jlong(rptr);");
                    this.out.println(indent + "args[1].i = rcapacity;");
                    this.out.print(indent + "args[2].j = ptr_to_jlong(&JavaCPP_" + mangle(methodInfo.cls.getName()));
                    if (methodInfo.arrayAllocator) {
                        this.out.println("_deallocateArray);");
                        this.arrayDeallocators.register(methodInfo.cls);
                    } else {
                        this.out.println("_deallocate);");
                        this.deallocators.register(methodInfo.cls);
                    }
                    this.out.println(indent + "env->CallNonvirtualVoidMethodA(obj, JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), JavaCPP_initMID, args);");
                    return;
                }
                this.out.println(indent + "env->SetLongField(obj, JavaCPP_addressFID, ptr_to_jlong(rptr));");
                this.out.println(indent + "env->SetIntField(obj, JavaCPP_limitFID, rcapacity);");
                this.out.println(indent + "env->SetIntField(obj, JavaCPP_capacityFID, rcapacity);");
                return;
            }
            return;
        }
        if (!methodInfo.valueSetter && !methodInfo.memberSetter && !methodInfo.noReturnGetter) {
            if (methodInfo.returnType.isPrimitive()) {
                this.out.println(indent + "rarg = (" + getJNITypeName(methodInfo.returnType) + ")rvalue;");
                return;
            }
            if (methodInfo.returnRaw) {
                this.out.println(indent + "rarg = rptr;");
                return;
            }
            boolean needInit = false;
            if (adapterInfo != null) {
                this.out.println(indent + "rptr = radapter;");
                if (methodInfo.returnType != String.class) {
                    this.out.println(indent + "jint rcapacity = (jint)radapter.size;");
                    this.out.println(indent + "jlong deallocator = " + (adapterInfo.constant ? "0;" : "ptr_to_jlong(&(" + adapterInfo.name + "::deallocate));"));
                }
                needInit = true;
            } else if ((returnBy instanceof ByVal) || FunctionPointer.class.isAssignableFrom(methodInfo.returnType)) {
                this.out.println(indent + "jint rcapacity = 1;");
                this.out.println(indent + "jlong deallocator = ptr_to_jlong(&JavaCPP_" + mangle(methodInfo.returnType.getName()) + "_deallocate);");
                this.deallocators.register(methodInfo.returnType);
                needInit = true;
            }
            if (Pointer.class.isAssignableFrom(methodInfo.returnType)) {
                this.out.print(indent);
                if (!(returnBy instanceof ByVal)) {
                    if (Modifier.isStatic(methodInfo.modifiers) && methodInfo.parameterTypes.length > 0) {
                        for (int i = 0; i < methodInfo.parameterTypes.length; i++) {
                            String cast = getParameterCast(methodInfo, i);
                            if (methodInfo.parameterTypes[i] == methodInfo.returnType) {
                                this.out.println("if (rptr == " + cast + "ptr" + i + ") {");
                                this.out.println(indent + "    rarg = arg" + i + ";");
                                this.out.print(indent + "} else ");
                            }
                        }
                    } else if (!Modifier.isStatic(methodInfo.modifiers) && methodInfo.cls == methodInfo.returnType) {
                        this.out.println("if (rptr == ptr) {");
                        this.out.println(indent + "    rarg = obj;");
                        this.out.print(indent + "} else ");
                    }
                }
                this.out.println("if (rptr != NULL) {");
                this.out.println(indent + "    rarg = env->AllocObject(JavaCPP_getClass(env, " + this.jclasses.register(methodInfo.returnType) + "));");
                if (needInit) {
                    this.out.println(indent + "    if (deallocator != 0) {");
                    this.out.println(indent + "        jvalue args[3];");
                    this.out.println(indent + "        args[0].j = ptr_to_jlong(rptr);");
                    this.out.println(indent + "        args[1].i = rcapacity;");
                    this.out.println(indent + "        args[2].j = deallocator;");
                    this.out.println(indent + "        env->CallNonvirtualVoidMethodA(rarg, JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), JavaCPP_initMID, args);");
                    this.out.println(indent + "    } else {");
                    this.out.println(indent + "        env->SetLongField(rarg, JavaCPP_addressFID, ptr_to_jlong(rptr));");
                    this.out.println(indent + "        env->SetIntField(rarg, JavaCPP_limitFID, rcapacity);");
                    this.out.println(indent + "        env->SetIntField(rarg, JavaCPP_capacityFID, rcapacity);");
                    this.out.println(indent + "    }");
                } else {
                    this.out.println(indent + "    env->SetLongField(rarg, JavaCPP_addressFID, ptr_to_jlong(rptr));");
                }
                this.out.println(indent + "}");
                return;
            }
            if (methodInfo.returnType == String.class) {
                this.out.println(indent + "if (rptr != NULL) {");
                this.out.println(indent + "    rarg = env->NewStringUTF(rptr);");
                this.out.println(indent + "}");
                return;
            }
            if (methodInfo.returnType.isArray() && methodInfo.returnType.getComponentType().isPrimitive()) {
                if (adapterInfo == null) {
                    this.out.println(indent + "jint rcapacity = rptr != NULL ? 1 : 0;");
                }
                String s = methodInfo.returnType.getComponentType().getName();
                String S = Character.toUpperCase(s.charAt(0)) + s.substring(1);
                this.out.println(indent + "if (rptr != NULL) {");
                this.out.println(indent + "    rarg = env->New" + S + "Array(rcapacity);");
                this.out.println(indent + "    env->Set" + S + "ArrayRegion(rarg, 0, rcapacity, (j" + s + "*)rptr);");
                this.out.println(indent + "}");
                if (adapterInfo != null) {
                    this.out.println(indent + "if (deallocator != 0 && rptr != NULL) {");
                    this.out.println(indent + "    (*(void(*)(void*))jlong_to_ptr(deallocator))((void*)rptr);");
                    this.out.println(indent + "}");
                    return;
                }
                return;
            }
            if (Buffer.class.isAssignableFrom(methodInfo.returnType)) {
                if (methodInfo.bufferGetter) {
                    this.out.println(indent + "jint rcapacity = size;");
                } else if (adapterInfo == null) {
                    this.out.println(indent + "jint rcapacity = rptr != NULL ? 1 : 0;");
                }
                this.out.println(indent + "if (rptr != NULL) {");
                this.out.println(indent + "    rarg = env->NewDirectByteBuffer(rptr, rcapacity);");
                this.out.println(indent + "}");
            }
        }
    }

    private void doParametersAfter(MethodInformation methodInfo) throws SecurityException {
        if (methodInfo.throwsException != null) {
            this.mayThrowExceptions = true;
            this.out.println("    } catch (...) {");
            this.out.println("        exc = JavaCPP_handleException(env, " + this.jclasses.register(methodInfo.throwsException) + ");");
            this.out.println("    }");
            this.out.println();
        }
        for (int j = 0; j < methodInfo.parameterTypes.length; j++) {
            if (!methodInfo.parameterRaw[j]) {
                Annotation passBy = getParameterBy(methodInfo, j);
                String cast = getParameterCast(methodInfo, j);
                String[] typeName = getCastedCPPTypeName(methodInfo.parameterAnnotations[j], methodInfo.parameterTypes[j]);
                AdapterInformation adapterInfo = getParameterAdapterInformation(true, methodInfo, j);
                if ("void*".equals(typeName[0])) {
                    typeName[0] = "char*";
                }
                if (Pointer.class.isAssignableFrom(methodInfo.parameterTypes[j])) {
                    if (adapterInfo != null) {
                        int k = 0;
                        while (k < adapterInfo.argc) {
                            this.out.println("    " + typeName[0] + " rptr" + (j + k) + typeName[1] + " = " + cast + "adapter" + j + ";");
                            this.out.println("    jint rsize" + (j + k) + " = (jint)adapter" + j + ".size" + (k > 0 ? (k + 1) + ";" : ";"));
                            this.out.println("    if (rptr" + (j + k) + " != " + cast + "ptr" + (j + k) + ") {");
                            this.out.println("        jvalue args[3];");
                            this.out.println("        args[0].j = ptr_to_jlong(rptr" + (j + k) + ");");
                            this.out.println("        args[1].i = rsize" + (j + k) + ";");
                            this.out.println("        args[2].j = ptr_to_jlong(&(" + adapterInfo.name + "::deallocate));");
                            this.out.println("        env->CallNonvirtualVoidMethodA(arg" + j + ", JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), JavaCPP_initMID, args);");
                            this.out.println("    } else {");
                            this.out.println("        env->SetIntField(arg" + j + ", JavaCPP_limitFID, rsize" + (j + k) + " + position" + (j + k) + ");");
                            this.out.println("    }");
                            k++;
                        }
                    } else if (((passBy instanceof ByPtrPtr) || (passBy instanceof ByPtrRef)) && !methodInfo.valueSetter && !methodInfo.memberSetter) {
                        if (!methodInfo.parameterTypes[j].isAnnotationPresent(Opaque.class)) {
                            this.out.println("    ptr" + j + " -= position" + j + ";");
                        }
                        this.out.println("    if (arg" + j + " != NULL) env->SetLongField(arg" + j + ", JavaCPP_addressFID, ptr_to_jlong(ptr" + j + "));");
                    }
                } else if (methodInfo.parameterTypes[j] == String.class) {
                    this.out.println("    if (arg" + j + " != NULL) env->ReleaseStringUTFChars(arg" + j + ", ptr" + j + ");");
                } else if (methodInfo.parameterTypes[j].isArray() && methodInfo.parameterTypes[j].getComponentType().isPrimitive()) {
                    this.out.print("    if (arg" + j + " != NULL) ");
                    if (methodInfo.valueGetter || methodInfo.valueSetter || methodInfo.memberGetter || methodInfo.memberSetter) {
                        this.out.println("env->ReleasePrimitiveArrayCritical(arg" + j + ", ptr" + j + ", 0);");
                    } else {
                        String s = methodInfo.parameterTypes[j].getComponentType().getName();
                        String S = Character.toUpperCase(s.charAt(0)) + s.substring(1);
                        this.out.println("env->Release" + S + "ArrayElements(arg" + j + ", (j" + s + "*)ptr" + j + ", 0);");
                    }
                }
            }
        }
    }

    private void doCallback(Class<?> cls, Method callbackMethod, String callbackName, boolean needFunctor) throws SecurityException {
        Class<?> callbackReturnType = callbackMethod.getReturnType();
        Class<?>[] callbackParameterTypes = callbackMethod.getParameterTypes();
        Annotation[] callbackAnnotations = callbackMethod.getAnnotations();
        Annotation[][] callbackParameterAnnotations = callbackMethod.getParameterAnnotations();
        String instanceTypeName = getFunctionClassName(cls);
        String[] callbackTypeName = getCPPTypeName(cls);
        String[] returnConvention = callbackTypeName[0].split("\\(");
        returnConvention[1] = getValueTypeName(returnConvention[1]);
        String parameterDeclaration = callbackTypeName[1].substring(1);
        this.functionPointers.register("static " + instanceTypeName + " " + callbackName + "_instance;");
        this.jclassesInit.register(cls);
        if (this.out2 != null) {
            this.out2.println("JNIIMPORT " + returnConvention[0] + (returnConvention.length > 1 ? returnConvention[1] : "") + callbackName + parameterDeclaration + ";");
        }
        this.out.println("JNIEXPORT " + returnConvention[0] + (returnConvention.length > 1 ? returnConvention[1] : "") + callbackName + parameterDeclaration + " {");
        this.out.print((callbackReturnType != Void.TYPE ? "    return " : "    ") + callbackName + "_instance(");
        for (int j = 0; j < callbackParameterTypes.length; j++) {
            this.out.print("arg" + j);
            if (j < callbackParameterTypes.length - 1) {
                this.out.print(", ");
            }
        }
        this.out.println(");");
        this.out.println("}");
        if (needFunctor) {
            this.out.println(returnConvention[0] + instanceTypeName + "::operator()" + parameterDeclaration + " {");
            String returnPrefix = "";
            if (callbackReturnType != Void.TYPE) {
                this.out.println("    " + getJNITypeName(callbackReturnType) + " rarg = 0;");
                returnPrefix = "rarg = ";
                if (callbackReturnType == String.class) {
                    returnPrefix = "rarg = (jstring)";
                }
            }
            String callbackReturnCast = getCast(callbackAnnotations, callbackReturnType);
            Annotation returnBy = getBy(callbackAnnotations);
            String[] returnTypeName = getCPPTypeName(callbackReturnType);
            String returnValueTypeName = getValueTypeName(returnTypeName);
            AdapterInformation returnAdapterInfo = getAdapterInformation(false, returnValueTypeName, callbackAnnotations);
            this.out.println("    jthrowable exc = NULL;");
            this.out.println("    JNIEnv* env;");
            this.out.println("    int attached = JavaCPP_getEnv(&env);");
            this.out.println("    if (attached < 0) {");
            this.out.println("        goto end;");
            this.out.println("    }");
            this.out.println("{");
            if (callbackParameterTypes.length > 0) {
                this.out.println("    jvalue args[" + callbackParameterTypes.length + "];");
                for (int j2 = 0; j2 < callbackParameterTypes.length; j2++) {
                    if (callbackParameterTypes[j2].isPrimitive()) {
                        this.out.println("    args[" + j2 + "]." + getSignature(callbackParameterTypes[j2]).toLowerCase() + " = (" + getJNITypeName(callbackParameterTypes[j2]) + ")arg" + j2 + ";");
                    } else {
                        Annotation passBy = getBy(callbackParameterAnnotations[j2]);
                        String[] typeName = getCPPTypeName(callbackParameterTypes[j2]);
                        String valueTypeName = getValueTypeName(typeName);
                        AdapterInformation adapterInfo = getAdapterInformation(false, valueTypeName, callbackParameterAnnotations[j2]);
                        boolean needInit = false;
                        if (adapterInfo != null) {
                            this.usesAdapters = true;
                            this.out.println("    " + adapterInfo.name + " adapter" + j2 + "(arg" + j2 + ");");
                            if (callbackParameterTypes[j2] != String.class) {
                                this.out.println("    jint size" + j2 + " = (jint)adapter" + j2 + ".size;");
                                this.out.println("    jlong deallocator" + j2 + " = ptr_to_jlong(&(" + adapterInfo.name + "::deallocate));");
                            }
                            needInit = true;
                        } else if (((passBy instanceof ByVal) && callbackParameterTypes[j2] != Pointer.class) || FunctionPointer.class.isAssignableFrom(callbackParameterTypes[j2])) {
                            this.out.println("    jint size" + j2 + " = 1;");
                            this.out.println("    jlong deallocator" + j2 + " = ptr_to_jlong(&JavaCPP_" + mangle(callbackParameterTypes[j2].getName()) + "_deallocate);");
                            this.deallocators.register(callbackParameterTypes[j2]);
                            needInit = true;
                        }
                        if (Pointer.class.isAssignableFrom(callbackParameterTypes[j2]) || Buffer.class.isAssignableFrom(callbackParameterTypes[j2]) || (callbackParameterTypes[j2].isArray() && callbackParameterTypes[j2].getComponentType().isPrimitive())) {
                            if (FunctionPointer.class.isAssignableFrom(callbackParameterTypes[j2])) {
                                typeName[0] = getFunctionClassName(callbackParameterTypes[j2]) + "*";
                                typeName[1] = "";
                                valueTypeName = getValueTypeName(typeName);
                            }
                            this.out.println("    " + getJNITypeName(callbackParameterTypes[j2]) + " obj" + j2 + " = NULL;");
                            this.out.println("    " + typeName[0] + " ptr" + j2 + typeName[1] + " = NULL;");
                            if (FunctionPointer.class.isAssignableFrom(callbackParameterTypes[j2])) {
                                this.out.println("    ptr" + j2 + " = new (std::nothrow) " + valueTypeName + ";");
                                this.out.println("    if (ptr" + j2 + " != NULL) {");
                                this.out.println("        ptr" + j2 + "->ptr = arg" + j2 + ";");
                                this.out.println("    }");
                            } else if (adapterInfo != null) {
                                this.out.println("    ptr" + j2 + " = adapter" + j2 + ";");
                            } else if ((passBy instanceof ByVal) && callbackParameterTypes[j2] != Pointer.class) {
                                this.out.println("    ptr" + j2 + (getNoException(callbackParameterTypes[j2], callbackMethod) ? " = new (std::nothrow) " : " = new ") + valueTypeName + typeName[1] + "(*(" + typeName[0] + typeName[1] + ")&arg" + j2 + ");");
                            } else if ((passBy instanceof ByVal) || (passBy instanceof ByRef)) {
                                this.out.println("    ptr" + j2 + " = (" + typeName[0] + typeName[1] + ")&arg" + j2 + ";");
                            } else if (passBy instanceof ByPtrPtr) {
                                this.out.println("    if (arg" + j2 + " == NULL) {");
                                this.out.println("        JavaCPP_log(\"Pointer address of argument " + j2 + " is NULL in callback for " + cls.getCanonicalName() + ".\");");
                                this.out.println("    } else {");
                                this.out.println("        ptr" + j2 + " = (" + typeName[0] + typeName[1] + ")*arg" + j2 + ";");
                                this.out.println("    }");
                            } else {
                                this.out.println("    ptr" + j2 + " = (" + typeName[0] + typeName[1] + ")arg" + j2 + ";");
                            }
                        }
                        if (Pointer.class.isAssignableFrom(callbackParameterTypes[j2])) {
                            String s = "    obj" + j2 + " = env->AllocObject(JavaCPP_getClass(env, " + this.jclasses.register(callbackParameterTypes[j2]) + "));";
                            this.jclassesInit.register(callbackParameterTypes[j2]);
                            if (getAdapterInformation(true, valueTypeName, callbackParameterAnnotations[j2]) != null || (passBy instanceof ByPtrPtr) || (passBy instanceof ByPtrRef)) {
                                this.out.println(s);
                            } else {
                                this.out.println("    if (ptr" + j2 + " != NULL) { ");
                                this.out.println("    " + s);
                                this.out.println("    }");
                            }
                            this.out.println("    if (obj" + j2 + " != NULL) { ");
                            if (needInit) {
                                this.out.println("        if (deallocator" + j2 + " != 0) {");
                                this.out.println("            jvalue args[3];");
                                this.out.println("            args[0].j = ptr_to_jlong(ptr" + j2 + ");");
                                this.out.println("            args[1].i = size" + j2 + ";");
                                this.out.println("            args[2].j = deallocator" + j2 + ";");
                                this.out.println("            env->CallNonvirtualVoidMethodA(obj" + j2 + ", JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), JavaCPP_initMID, args);");
                                this.out.println("        } else {");
                                this.out.println("            env->SetLongField(obj" + j2 + ", JavaCPP_addressFID, ptr_to_jlong(ptr" + j2 + "));");
                                this.out.println("            env->SetIntField(obj" + j2 + ", JavaCPP_limitFID, size" + j2 + ");");
                                this.out.println("            env->SetIntField(obj" + j2 + ", JavaCPP_capacityFID, size" + j2 + ");");
                                this.out.println("        }");
                            } else {
                                this.out.println("        env->SetLongField(obj" + j2 + ", JavaCPP_addressFID, ptr_to_jlong(ptr" + j2 + "));");
                            }
                            this.out.println("    }");
                            this.out.println("    args[" + j2 + "].l = obj" + j2 + ";");
                        } else if (callbackParameterTypes[j2] == String.class) {
                            this.out.println("    jstring obj" + j2 + " = (const char*)" + (adapterInfo != null ? "adapter" : "arg") + j2 + " == NULL ? NULL : env->NewStringUTF((const char*)" + (adapterInfo != null ? "adapter" : "arg") + j2 + ");");
                            this.out.println("    args[" + j2 + "].l = obj" + j2 + ";");
                        } else if (callbackParameterTypes[j2].isArray() && callbackParameterTypes[j2].getComponentType().isPrimitive()) {
                            if (adapterInfo == null) {
                                this.out.println("    jint size" + j2 + " = ptr" + j2 + " != NULL ? 1 : 0;");
                            }
                            String s2 = callbackParameterTypes[j2].getComponentType().getName();
                            String S = Character.toUpperCase(s2.charAt(0)) + s2.substring(1);
                            this.out.println("    if (ptr" + j2 + " != NULL) {");
                            this.out.println("        obj" + j2 + " = env->New" + S + "Array(size" + j2 + ");");
                            this.out.println("        env->Set" + S + "ArrayRegion(obj" + j2 + ", 0, size" + j2 + ", (j" + s2 + "*)ptr" + j2 + ");");
                            this.out.println("    }");
                            if (adapterInfo != null) {
                                this.out.println("    if (deallocator" + j2 + " != 0 && ptr" + j2 + " != NULL) {");
                                this.out.println("        (*(void(*)(void*))jlong_to_ptr(deallocator" + j2 + "))((void*)ptr" + j2 + ");");
                                this.out.println("    }");
                            }
                        } else if (Buffer.class.isAssignableFrom(callbackParameterTypes[j2])) {
                            if (adapterInfo == null) {
                                this.out.println("    jint size" + j2 + " = ptr" + j2 + " != NULL ? 1 : 0;");
                            }
                            this.out.println("    if (ptr" + j2 + " != NULL) {");
                            this.out.println("        obj" + j2 + " = env->NewDirectByteBuffer(ptr" + j2 + ", size" + j2 + ");");
                            this.out.println("    }");
                        } else {
                            logger.log(Level.WARNING, "Callback \"" + callbackMethod + "\" has unsupported parameter type \"" + callbackParameterTypes[j2].getCanonicalName() + "\". Compilation will most likely fail.");
                        }
                    }
                }
            }
            this.out.println("    if (obj == NULL) {");
            this.out.println("        obj = env->NewGlobalRef(env->AllocObject(JavaCPP_getClass(env, " + this.jclasses.register(cls) + ")));");
            this.out.println("        if (obj == NULL) {");
            this.out.println("            JavaCPP_log(\"Error creating global reference of " + cls.getCanonicalName() + " instance for callback.\");");
            this.out.println("        } else {");
            this.out.println("            env->SetLongField(obj, JavaCPP_addressFID, ptr_to_jlong(this));");
            this.out.println("        }");
            this.out.println("        ptr = &" + callbackName + ";");
            this.out.println("    }");
            this.out.println("    if (mid == NULL) {");
            this.out.println("        mid = env->GetMethodID(JavaCPP_getClass(env, " + this.jclasses.register(cls) + "), \"" + callbackMethod.getName() + "\", \"(" + getSignature(callbackMethod.getParameterTypes()) + ")" + getSignature(callbackMethod.getReturnType()) + "\");");
            this.out.println("    }");
            this.out.println("    if (env->IsSameObject(obj, NULL)) {");
            this.out.println("        JavaCPP_log(\"Function pointer object is NULL in callback for " + cls.getCanonicalName() + ".\");");
            this.out.println("    } else if (mid == NULL) {");
            this.out.println("        JavaCPP_log(\"Error getting method ID of function caller \\\"" + callbackMethod + "\\\" for callback.\");");
            this.out.println("    } else {");
            String s3 = "Object";
            if (callbackReturnType.isPrimitive()) {
                String s4 = callbackReturnType.getName();
                s3 = Character.toUpperCase(s4.charAt(0)) + s4.substring(1);
            }
            this.out.println("        " + returnPrefix + "env->Call" + s3 + "MethodA(obj, mid, " + (callbackParameterTypes.length == 0 ? "NULL);" : "args);"));
            this.out.println("        if ((exc = env->ExceptionOccurred()) != NULL) {");
            this.out.println("            env->ExceptionClear();");
            this.out.println("        }");
            this.out.println("    }");
            for (int j3 = 0; j3 < callbackParameterTypes.length; j3++) {
                if (Pointer.class.isAssignableFrom(callbackParameterTypes[j3])) {
                    String[] typeName2 = getCPPTypeName(callbackParameterTypes[j3]);
                    Annotation passBy2 = getBy(callbackParameterAnnotations[j3]);
                    String cast = getCast(callbackParameterAnnotations[j3], callbackParameterTypes[j3]);
                    AdapterInformation adapterInfo2 = getAdapterInformation(true, getValueTypeName(typeName2), callbackParameterAnnotations[j3]);
                    if ("void*".equals(typeName2[0])) {
                        typeName2[0] = "char*";
                    }
                    if (adapterInfo2 != null || (passBy2 instanceof ByPtrPtr) || (passBy2 instanceof ByPtrRef)) {
                        this.out.println("    " + typeName2[0] + " rptr" + j3 + typeName2[1] + " = (" + typeName2[0] + typeName2[1] + ")jlong_to_ptr(env->GetLongField(obj" + j3 + ", JavaCPP_addressFID));");
                        if (adapterInfo2 != null) {
                            this.out.println("    jint rsize" + j3 + " = env->GetIntField(obj" + j3 + ", JavaCPP_limitFID);");
                        }
                        if (!callbackParameterTypes[j3].isAnnotationPresent(Opaque.class)) {
                            this.out.println("    jint rposition" + j3 + " = env->GetIntField(obj" + j3 + ", JavaCPP_positionFID);");
                            this.out.println("    rptr" + j3 + " += rposition" + j3 + ";");
                            if (adapterInfo2 != null) {
                                this.out.println("    rsize" + j3 + " -= rposition" + j3 + ";");
                            }
                        }
                        if (adapterInfo2 != null) {
                            this.out.println("    adapter" + j3 + ".assign(rptr" + j3 + ", rsize" + j3 + ");");
                        } else if (passBy2 instanceof ByPtrPtr) {
                            this.out.println("    if (arg" + j3 + " != NULL) {");
                            this.out.println("        *arg" + j3 + " = *" + cast + "&rptr" + j3 + ";");
                            this.out.println("    }");
                        } else if (passBy2 instanceof ByPtrRef) {
                            this.out.println("    arg" + j3 + " = " + cast + "rptr" + j3 + ";");
                        }
                    }
                }
                if (!callbackParameterTypes[j3].isPrimitive()) {
                    this.out.println("    env->DeleteLocalRef(obj" + j3 + ");");
                }
            }
            this.out.println("}");
            this.out.println("end:");
            if (callbackReturnType != Void.TYPE) {
                if ("void*".equals(returnTypeName[0])) {
                    returnTypeName[0] = "char*";
                }
                if (Pointer.class.isAssignableFrom(callbackReturnType)) {
                    this.out.println("    " + returnTypeName[0] + " rptr" + returnTypeName[1] + " = rarg == NULL ? NULL : (" + returnTypeName[0] + returnTypeName[1] + ")jlong_to_ptr(env->GetLongField(rarg, JavaCPP_addressFID));");
                    if (returnAdapterInfo != null) {
                        this.out.println("    jint rsize = rarg == NULL ? 0 : env->GetIntField(rarg, JavaCPP_limitFID);");
                    }
                    if (!callbackReturnType.isAnnotationPresent(Opaque.class)) {
                        this.out.println("    jint rposition = rarg == NULL ? 0 : env->GetIntField(rarg, JavaCPP_positionFID);");
                        this.out.println("    rptr += rposition;");
                        if (returnAdapterInfo != null) {
                            this.out.println("    rsize -= rposition;");
                        }
                    }
                } else if (callbackReturnType == String.class) {
                    this.out.println("    " + returnTypeName[0] + " rptr" + returnTypeName[1] + " = rarg == NULL ? NULL : env->GetStringUTFChars(rarg, NULL);");
                    if (returnAdapterInfo != null) {
                        this.out.println("    jint rsize = 0;");
                    }
                } else if (Buffer.class.isAssignableFrom(callbackReturnType)) {
                    this.out.println("    " + returnTypeName[0] + " rptr" + returnTypeName[1] + " = rarg == NULL ? NULL : env->GetDirectBufferAddress(rarg);");
                    if (returnAdapterInfo != null) {
                        this.out.println("    jint rsize = rarg == NULL ? 0 : env->GetDirectBufferCapacity(rarg);");
                    }
                } else if (!callbackReturnType.isPrimitive()) {
                    logger.log(Level.WARNING, "Callback \"" + callbackMethod + "\" has unsupported return type \"" + callbackReturnType.getCanonicalName() + "\". Compilation will most likely fail.");
                }
            }
            this.out.println("    if (exc != NULL) {");
            this.out.println("        jclass cls = env->GetObjectClass(exc);");
            this.out.println("        jmethodID mid = env->GetMethodID(cls, \"toString\", \"()Ljava/lang/String;\");");
            this.out.println("        env->DeleteLocalRef(cls);");
            this.out.println("        jstring str = (jstring)env->CallObjectMethod(exc, mid);");
            this.out.println("        env->DeleteLocalRef(exc);");
            this.out.println("        const char *msg = env->GetStringUTFChars(str, NULL);");
            this.out.println("        JavaCPP_exception e(msg);");
            this.out.println("        env->ReleaseStringUTFChars(str, msg);");
            this.out.println("        env->DeleteLocalRef(str);");
            this.out.println("        JavaCPP_detach(attached);");
            this.out.println("        throw e;");
            this.out.println("    } else {");
            this.out.println("        JavaCPP_detach(attached);");
            this.out.println("    }");
            if (callbackReturnType != Void.TYPE) {
                if (callbackReturnType.isPrimitive()) {
                    this.out.println("    return " + callbackReturnCast + "rarg;");
                } else if (returnAdapterInfo != null) {
                    this.usesAdapters = true;
                    this.out.println("    return " + returnAdapterInfo.name + "(" + callbackReturnCast + "rptr, rsize);");
                } else if (FunctionPointer.class.isAssignableFrom(callbackReturnType)) {
                    this.out.println("    return " + callbackReturnCast + "(rptr == NULL ? NULL : rptr->ptr);");
                } else if ((returnBy instanceof ByVal) || (returnBy instanceof ByRef)) {
                    this.out.println("    if (rptr == NULL) {");
                    this.out.println("        JavaCPP_log(\"Return pointer address is NULL in callback for " + cls.getCanonicalName() + ".\");");
                    this.out.println("        static " + returnValueTypeName + " empty" + returnTypeName[1] + ";");
                    this.out.println("        return empty;");
                    this.out.println("    } else {");
                    this.out.println("        return *" + callbackReturnCast + "rptr;");
                    this.out.println("    }");
                } else if (returnBy instanceof ByPtrPtr) {
                    this.out.println("    return " + callbackReturnCast + "&rptr;");
                } else {
                    this.out.println("    return " + callbackReturnCast + "rptr;");
                }
            }
            this.out.println("}");
        }
    }

    private void doCallbackAllocator(Class cls, String callbackName) {
        String instanceTypeName = getFunctionClassName(cls);
        this.out.println("    obj = env->NewWeakGlobalRef(obj);");
        this.out.println("    if (obj == NULL) {");
        this.out.println("        JavaCPP_log(\"Error creating global reference of " + cls.getCanonicalName() + " instance for callback.\");");
        this.out.println("        return;");
        this.out.println("    }");
        this.out.println("    " + instanceTypeName + "* rptr = new (std::nothrow) " + instanceTypeName + ";");
        this.out.println("    if (rptr != NULL) {");
        this.out.println("        rptr->ptr = &" + callbackName + ";");
        this.out.println("        rptr->obj = obj;");
        this.out.println("        jvalue args[3];");
        this.out.println("        args[0].j = ptr_to_jlong(rptr);");
        this.out.println("        args[1].i = 1;");
        this.out.println("        args[2].j = ptr_to_jlong(&JavaCPP_" + mangle(cls.getName()) + "_deallocate);");
        this.deallocators.register(cls);
        this.out.println("        env->CallNonvirtualVoidMethodA(obj, JavaCPP_getClass(env, " + this.jclasses.register(Pointer.class) + "), JavaCPP_initMID, args);");
        this.out.println("        " + callbackName + "_instance = *rptr;");
        this.out.println("    }");
        this.out.println("}");
    }

    public boolean checkPlatform(Class<?> cls) {
        Properties classProperties = (Properties) cls.getAnnotation(Properties.class);
        if (classProperties != null) {
            Class[] classes = classProperties.inherit();
            if (classes != null) {
                for (Class c : classes) {
                    if (checkPlatform((Class<?>) c)) {
                        return true;
                    }
                }
            }
            Platform[] platforms = classProperties.value();
            if (platforms != null) {
                for (Platform p : platforms) {
                    if (checkPlatform(p)) {
                        return true;
                    }
                }
            }
        } else if (checkPlatform((Platform) cls.getAnnotation(Platform.class))) {
            return true;
        }
        return false;
    }

    public boolean checkPlatform(Platform platform) {
        if (platform == null) {
            return true;
        }
        String platformName = this.properties.getProperty("platform.name");
        String[][] names = {platform.value(), platform.not()};
        boolean[] matches = {false, false};
        for (int i = 0; i < names.length; i++) {
            String[] arr$ = names[i];
            int len$ = arr$.length;
            int i$ = 0;
            while (true) {
                if (i$ < len$) {
                    String s = arr$[i$];
                    if (!platformName.startsWith(s)) {
                        i$++;
                    } else {
                        matches[i] = true;
                        break;
                    }
                }
            }
        }
        return (names[0].length == 0 || matches[0]) && (names[1].length == 0 || !matches[1]);
    }

    private String getFunctionClassName(Class<?> cls) {
        Name name = (Name) cls.getAnnotation(Name.class);
        return name != null ? name.value()[0] : "JavaCPP_" + mangle(cls.getName());
    }

    private static Method getFunctionMethod(Class<?> cls, boolean[] callbackAllocators) throws SecurityException {
        if (!FunctionPointer.class.isAssignableFrom(cls)) {
            return null;
        }
        Method[] methods = cls.getDeclaredMethods();
        Method functionMethod = null;
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            int modifiers = methods[i].getModifiers();
            Class[] parameterTypes = methods[i].getParameterTypes();
            Class returnType = methods[i].getReturnType();
            if (!Modifier.isStatic(modifiers)) {
                if (callbackAllocators != null && methodName.startsWith("allocate") && Modifier.isNative(modifiers) && returnType == Void.TYPE && parameterTypes.length == 0) {
                    callbackAllocators[i] = true;
                } else if (methodName.startsWith("call") || methodName.startsWith("apply")) {
                    functionMethod = methods[i];
                }
            }
        }
        return functionMethod;
    }

    /* JADX WARN: Removed duplicated region for block: B:135:0x034e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.googlecode.javacpp.Generator.MethodInformation getMethodInformation(java.lang.reflect.Method r34) throws java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 1655
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.javacpp.Generator.getMethodInformation(java.lang.reflect.Method):com.googlecode.javacpp.Generator$MethodInformation");
    }

    public static boolean getNoException(Class<?> cls, Method method) {
        boolean noException = baseClasses.contains(cls) || method.isAnnotationPresent(NoException.class);
        while (!noException && cls != null) {
            noException = cls.isAnnotationPresent(NoException.class);
            if (noException) {
                break;
            }
            cls = cls.getDeclaringClass();
        }
        return noException;
    }

    public static AdapterInformation getParameterAdapterInformation(boolean out, MethodInformation methodInfo, int j) throws SecurityException {
        if (out && (methodInfo.parameterTypes[j] == String.class || methodInfo.valueSetter || methodInfo.memberSetter)) {
            return null;
        }
        String typeName = getParameterCast(methodInfo, j);
        if (typeName != null && typeName.startsWith("(") && typeName.endsWith(")")) {
            typeName = typeName.substring(1, typeName.length() - 1);
        }
        if (typeName == null || typeName.length() == 0) {
            typeName = getCastedCPPTypeName(methodInfo.parameterAnnotations[j], methodInfo.parameterTypes[j])[0];
        }
        String valueTypeName = getValueTypeName(typeName);
        AdapterInformation adapter = getAdapterInformation(out, valueTypeName, methodInfo.parameterAnnotations[j]);
        if (adapter != null || methodInfo.pairedMethod == null) {
            return adapter;
        }
        if (methodInfo.valueSetter || methodInfo.memberSetter) {
            return getAdapterInformation(out, valueTypeName, methodInfo.pairedMethod.getAnnotations());
        }
        return adapter;
    }

    public static AdapterInformation getAdapterInformation(boolean out, String valueTypeName, Annotation... annotations) {
        AdapterInformation adapterInfo = null;
        boolean constant = false;
        String cast = "";
        for (Annotation a : annotations) {
            Adapter adapter = a instanceof Adapter ? (Adapter) a : (Adapter) a.annotationType().getAnnotation(Adapter.class);
            if (adapter != null) {
                adapterInfo = new AdapterInformation();
                adapterInfo.name = adapter.value();
                adapterInfo.argc = adapter.argc();
                if (a != adapter) {
                    try {
                        Class cls = a.annotationType();
                        if (cls.isAnnotationPresent(Const.class)) {
                            constant = true;
                        }
                        try {
                            String value = cls.getDeclaredMethod("value", new Class[0]).invoke(a, new Object[0]).toString();
                            if (value != null && value.length() > 0) {
                                valueTypeName = value;
                            }
                        } catch (NoSuchMethodException e) {
                            valueTypeName = null;
                        }
                        Cast c = (Cast) cls.getAnnotation(Cast.class);
                        if (c != null && cast.length() == 0) {
                            cast = c.value()[0];
                            if (valueTypeName != null) {
                                cast = cast + "< " + valueTypeName + " >";
                            }
                            if (c.value().length > 1) {
                                cast = cast + c.value()[1];
                            }
                        }
                    } catch (Exception ex) {
                        logger.log(Level.WARNING, "Could not invoke the value() method on annotation \"" + a + "\".", (Throwable) ex);
                    }
                    if (valueTypeName != null && valueTypeName.length() > 0) {
                        adapterInfo.name += "< " + valueTypeName + " >";
                    }
                }
            } else if (a instanceof Const) {
                constant = true;
            } else if (a instanceof Cast) {
                Cast c2 = (Cast) a;
                if (c2.value().length > 1) {
                    cast = c2.value()[1];
                }
            }
        }
        if (adapterInfo != null) {
            adapterInfo.cast = cast;
            adapterInfo.constant = constant;
        }
        if (out && constant) {
            return null;
        }
        return adapterInfo;
    }

    public static String getParameterCast(MethodInformation methodInfo, int j) throws SecurityException {
        String cast = getCast(methodInfo.parameterAnnotations[j], methodInfo.parameterTypes[j]);
        if ((cast != null && cast.length() != 0) || j != methodInfo.parameterTypes.length - 1) {
            return cast;
        }
        if ((methodInfo.valueSetter || methodInfo.memberSetter) && methodInfo.pairedMethod != null) {
            return getCast(methodInfo.pairedMethod.getAnnotations(), methodInfo.pairedMethod.getReturnType());
        }
        return cast;
    }

    public static String getCast(Annotation[] annotations, Class<?> type) throws SecurityException {
        String[] typeName = null;
        for (Annotation a : annotations) {
            if (((a instanceof Cast) && ((Cast) a).value()[0].length() > 0) || (a instanceof Const)) {
                typeName = getCastedCPPTypeName(annotations, type);
                break;
            }
        }
        return (typeName == null || typeName.length <= 0) ? "" : "(" + typeName[0] + typeName[1] + ")";
    }

    public static Annotation getParameterBy(MethodInformation methodInfo, int j) {
        Annotation passBy = getBy(methodInfo.parameterAnnotations[j]);
        if (passBy != null || methodInfo.pairedMethod == null) {
            return passBy;
        }
        if (methodInfo.valueSetter || methodInfo.memberSetter) {
            return getBy(methodInfo.pairedMethod.getAnnotations());
        }
        return passBy;
    }

    public static Annotation getBy(Annotation... annotations) {
        Annotation byAnnotation = null;
        for (Annotation a : annotations) {
            if ((a instanceof ByPtr) || (a instanceof ByPtrPtr) || (a instanceof ByPtrRef) || (a instanceof ByRef) || (a instanceof ByVal)) {
                if (byAnnotation != null) {
                    logger.log(Level.WARNING, "\"By\" annotation \"" + byAnnotation + "\" already found. Ignoring superfluous annotation \"" + a + "\".");
                } else {
                    byAnnotation = a;
                }
            }
        }
        return byAnnotation;
    }

    public static Annotation getBehavior(Annotation... annotations) {
        Annotation behaviorAnnotation = null;
        for (Annotation a : annotations) {
            if ((a instanceof Function) || (a instanceof Allocator) || (a instanceof ArrayAllocator) || (a instanceof ValueSetter) || (a instanceof ValueGetter) || (a instanceof MemberGetter) || (a instanceof MemberSetter)) {
                if (behaviorAnnotation != null) {
                    logger.log(Level.WARNING, "Behavior annotation \"" + behaviorAnnotation + "\" already found. Ignoring superfluous annotation \"" + a + "\".");
                } else {
                    behaviorAnnotation = a;
                }
            }
        }
        return behaviorAnnotation;
    }

    public static String getValueTypeName(String... typeName) {
        String type = typeName[0];
        if (type.startsWith("const ")) {
            return type.substring(6, type.length() - 1);
        }
        if (type.length() != 0) {
            return type.substring(0, type.length() - 1);
        }
        return type;
    }

    public static String[] getAnnotatedCPPTypeName(Annotation[] annotations, Class<?> type) {
        String[] typeName = getCastedCPPTypeName(annotations, type);
        String prefix = typeName[0];
        String suffix = typeName[1];
        boolean casted = false;
        for (Annotation a : annotations) {
            if (((a instanceof Cast) && ((Cast) a).value()[0].length() > 0) || (a instanceof Const)) {
                casted = true;
                break;
            }
        }
        Annotation by = getBy(annotations);
        if (by instanceof ByVal) {
            prefix = getValueTypeName(typeName);
        } else if (by instanceof ByRef) {
            prefix = getValueTypeName(typeName) + "&";
        } else if ((by instanceof ByPtrPtr) && !casted) {
            prefix = prefix + "*";
        } else if (by instanceof ByPtrRef) {
            prefix = prefix + "&";
        }
        typeName[0] = prefix;
        typeName[1] = suffix;
        return typeName;
    }

    public static String[] getCastedCPPTypeName(Annotation[] annotations, Class<?> type) throws SecurityException {
        String[] typeName = null;
        boolean warning = false;
        boolean adapter = false;
        for (Annotation a : annotations) {
            if (a instanceof Cast) {
                warning = typeName != null;
                String prefix = ((Cast) a).value()[0];
                String suffix = "";
                int parenthesis = prefix.indexOf(41);
                if (parenthesis > 0) {
                    suffix = prefix.substring(parenthesis).trim();
                    prefix = prefix.substring(0, parenthesis).trim();
                }
                typeName = prefix.length() > 0 ? new String[]{prefix, suffix} : null;
            } else if (a instanceof Const) {
                warning = typeName != null;
                if (!warning) {
                    typeName = getCPPTypeName(type);
                    if (((Const) a).value()) {
                        typeName[0] = getValueTypeName(typeName) + " const *";
                    } else {
                        typeName[0] = "const " + typeName[0];
                    }
                    Annotation by = getBy(annotations);
                    if (by instanceof ByPtrPtr) {
                        typeName[0] = typeName[0] + "*";
                    }
                }
            } else if ((a instanceof Adapter) || a.annotationType().isAnnotationPresent(Adapter.class)) {
                adapter = true;
            }
        }
        if (warning && !adapter) {
            logger.log(Level.WARNING, "Without \"Adapter\", \"Cast\" and \"Const\" annotations are mutually exclusive.");
        }
        if (typeName == null) {
            String[] typeName2 = getCPPTypeName(type);
            return typeName2;
        }
        return typeName;
    }

    public static String[] getCPPTypeName(Class<?> type) throws SecurityException {
        String prefix;
        String prefix2 = "";
        String suffix = "";
        if (type == Buffer.class || type == Pointer.class) {
            prefix2 = "void*";
        } else if (type == byte[].class || type == ByteBuffer.class || type == BytePointer.class) {
            prefix2 = "signed char*";
        } else if (type == short[].class || type == ShortBuffer.class || type == ShortPointer.class) {
            prefix2 = "short*";
        } else if (type == int[].class || type == IntBuffer.class || type == IntPointer.class) {
            prefix2 = "int*";
        } else if (type == long[].class || type == LongBuffer.class || type == LongPointer.class) {
            prefix2 = "jlong*";
        } else if (type == float[].class || type == FloatBuffer.class || type == FloatPointer.class) {
            prefix2 = "float*";
        } else if (type == double[].class || type == DoubleBuffer.class || type == DoublePointer.class) {
            prefix2 = "double*";
        } else if (type == char[].class || type == CharBuffer.class || type == CharPointer.class) {
            prefix2 = "unsigned short*";
        } else if (type == boolean[].class) {
            prefix2 = "unsigned char*";
        } else if (type == PointerPointer.class) {
            prefix2 = "void**";
        } else if (type == String.class) {
            prefix2 = "const char*";
        } else if (type == Byte.TYPE) {
            prefix2 = "signed char";
        } else if (type == Long.TYPE) {
            prefix2 = "jlong";
        } else if (type == Character.TYPE) {
            prefix2 = "unsigned short";
        } else if (type == Boolean.TYPE) {
            prefix2 = "unsigned char";
        } else if (type.isPrimitive()) {
            prefix2 = type.getName();
        } else if (FunctionPointer.class.isAssignableFrom(type)) {
            Method functionMethod = getFunctionMethod(type, null);
            if (functionMethod != null) {
                Convention convention = (Convention) type.getAnnotation(Convention.class);
                String callingConvention = convention == null ? "" : convention.value() + " ";
                Namespace namespace = (Namespace) type.getAnnotation(Namespace.class);
                String spaceName = namespace == null ? "" : namespace.value();
                if (spaceName.length() > 0 && !spaceName.endsWith("::")) {
                    spaceName = spaceName + "::";
                }
                Class returnType = functionMethod.getReturnType();
                Class[] parameterTypes = functionMethod.getParameterTypes();
                Annotation[] annotations = functionMethod.getAnnotations();
                Annotation[][] parameterAnnotations = functionMethod.getParameterAnnotations();
                String[] returnTypeName = getAnnotatedCPPTypeName(annotations, returnType);
                AdapterInformation returnAdapterInfo = getAdapterInformation(false, getValueTypeName(returnTypeName), annotations);
                if (returnAdapterInfo != null && returnAdapterInfo.cast.length() > 0) {
                    prefix = returnAdapterInfo.cast;
                } else {
                    prefix = returnTypeName[0] + returnTypeName[1];
                }
                prefix2 = prefix + " (" + callingConvention + spaceName + "*";
                String suffix2 = ")(";
                if (namespace != null && !Pointer.class.isAssignableFrom(parameterTypes[0])) {
                    logger.log(Level.WARNING, "First parameter of caller method call() or apply() for member function pointer " + type.getCanonicalName() + " is not a Pointer. Compilation will most likely fail.");
                }
                for (int j = namespace == null ? 0 : 1; j < parameterTypes.length; j++) {
                    String[] paramTypeName = getAnnotatedCPPTypeName(parameterAnnotations[j], parameterTypes[j]);
                    AdapterInformation paramAdapterInfo = getAdapterInformation(false, getValueTypeName(paramTypeName), parameterAnnotations[j]);
                    if (paramAdapterInfo != null && paramAdapterInfo.cast.length() > 0) {
                        suffix2 = suffix2 + paramAdapterInfo.cast + " arg" + j;
                    } else {
                        suffix2 = suffix2 + paramTypeName[0] + " arg" + j + paramTypeName[1];
                    }
                    if (j < parameterTypes.length - 1) {
                        suffix2 = suffix2 + ", ";
                    }
                }
                suffix = suffix2 + ")";
                if (type.isAnnotationPresent(Const.class)) {
                    suffix = suffix + " const";
                }
            }
        } else {
            String scopedType = getCPPScopeName(type);
            if (scopedType.length() > 0) {
                prefix2 = scopedType + "*";
            } else {
                logger.log(Level.WARNING, "The class " + type.getCanonicalName() + " does not map to any C++ type. Compilation will most likely fail.");
            }
        }
        return new String[]{prefix2, suffix};
    }

    public static String getCPPScopeName(MethodInformation methodInfo) {
        String scopeName = getCPPScopeName(methodInfo.cls);
        Namespace namespace = (Namespace) methodInfo.method.getAnnotation(Namespace.class);
        String spaceName = namespace == null ? "" : namespace.value();
        if ((namespace != null && namespace.value().length() == 0) || spaceName.startsWith("::")) {
            scopeName = "";
        }
        if (scopeName.length() > 0 && !scopeName.endsWith("::")) {
            scopeName = scopeName + "::";
        }
        String scopeName2 = scopeName + spaceName;
        if (spaceName.length() > 0 && !spaceName.endsWith("::")) {
            scopeName2 = scopeName2 + "::";
        }
        return scopeName2 + methodInfo.memberName[0];
    }

    public static String getCPPScopeName(Class<?> type) {
        String s;
        String scopeName = "";
        while (type != null) {
            Namespace namespace = (Namespace) type.getAnnotation(Namespace.class);
            String spaceName = namespace == null ? "" : namespace.value();
            if (Pointer.class.isAssignableFrom(type) && type != Pointer.class) {
                Name name = (Name) type.getAnnotation(Name.class);
                if (name == null) {
                    String s2 = type.getName();
                    int i = s2.lastIndexOf("$");
                    if (i < 0) {
                        i = s2.lastIndexOf(".");
                    }
                    s = s2.substring(i + 1);
                } else {
                    s = name.value()[0];
                }
                if (spaceName.length() > 0 && !spaceName.endsWith("::")) {
                    spaceName = spaceName + "::";
                }
                spaceName = spaceName + s;
            }
            if (scopeName.length() > 0 && !spaceName.endsWith("::")) {
                spaceName = spaceName + "::";
            }
            scopeName = spaceName + scopeName;
            if ((namespace != null && namespace.value().length() == 0) || spaceName.startsWith("::")) {
                break;
            }
            type = type.getDeclaringClass();
        }
        return scopeName;
    }

    public static String getJNITypeName(Class type) {
        if (type == Byte.TYPE) {
            return "jbyte";
        }
        if (type == Short.TYPE) {
            return "jshort";
        }
        if (type == Integer.TYPE) {
            return "jint";
        }
        if (type == Long.TYPE) {
            return "jlong";
        }
        if (type == Float.TYPE) {
            return "jfloat";
        }
        if (type == Double.TYPE) {
            return "jdouble";
        }
        if (type == Character.TYPE) {
            return "jchar";
        }
        if (type == Boolean.TYPE) {
            return "jboolean";
        }
        if (type == byte[].class) {
            return "jbyteArray";
        }
        if (type == short[].class) {
            return "jshortArray";
        }
        if (type == int[].class) {
            return "jintArray";
        }
        if (type == long[].class) {
            return "jlongArray";
        }
        if (type == float[].class) {
            return "jfloatArray";
        }
        if (type == double[].class) {
            return "jdoubleArray";
        }
        if (type == char[].class) {
            return "jcharArray";
        }
        if (type == boolean[].class) {
            return "jbooleanArray";
        }
        if (type.isArray()) {
            return "jobjectArray";
        }
        if (type == String.class) {
            return "jstring";
        }
        if (type == Class.class) {
            return "jclass";
        }
        if (type == Void.TYPE) {
            return "void";
        }
        return "jobject";
    }

    public static String getSignature(Class... types) {
        StringBuilder signature = new StringBuilder(types.length * 2);
        for (Class cls : types) {
            signature.append(getSignature(cls));
        }
        return signature.toString();
    }

    public static String getSignature(Class type) {
        if (type == Byte.TYPE) {
            return "B";
        }
        if (type == Short.TYPE) {
            return "S";
        }
        if (type == Integer.TYPE) {
            return "I";
        }
        if (type == Long.TYPE) {
            return "J";
        }
        if (type == Float.TYPE) {
            return "F";
        }
        if (type == Double.TYPE) {
            return "D";
        }
        if (type == Boolean.TYPE) {
            return "Z";
        }
        if (type == Character.TYPE) {
            return "C";
        }
        if (type == Void.TYPE) {
            return "V";
        }
        if (type.isArray()) {
            return type.getName().replace(".", "/");
        }
        return "L" + type.getName().replace(".", "/") + ";";
    }

    public static String mangle(String name) {
        StringBuilder mangledName = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((c >= '0' && c <= '9') || ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                mangledName.append(c);
            } else if (c == '_') {
                mangledName.append("_1");
            } else if (c == ';') {
                mangledName.append("_2");
            } else if (c == '[') {
                mangledName.append("_3");
            } else if (c == '.' || c == '/') {
                mangledName.append("_");
            } else {
                String code = Integer.toHexString(c);
                mangledName.append("_0");
                switch (code.length()) {
                    case 1:
                        mangledName.append("0");
                    case 2:
                        mangledName.append("0");
                    case 3:
                        mangledName.append("0");
                        break;
                }
                mangledName.append(code);
            }
        }
        return mangledName.toString();
    }
}
