package com.googlecode.javacpp;

import com.googlecode.javacpp.Loader;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeSet;

/* loaded from: classes.dex */
public class Parser {
    InfoMap infoMap;
    Properties properties;
    Token[] tokenArray = null;
    int tokenIndex = 0;

    public interface InfoMapper {
        void map(InfoMap infoMap);
    }

    public static class Exception extends java.lang.Exception {
        public Exception(String message) {
            super(message);
        }

        public Exception(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class Info implements Cloneable {
        String[] annotations;
        boolean cast;
        boolean complex;
        String[] cppNames;
        boolean define;
        boolean forwardDeclared;
        String[] genericTypes;
        String[] javaNames;
        String[] pointerTypes;
        String[] primitiveTypes;
        String textAfter;
        String textBefore;

        public Info() {
            this.cppNames = null;
            this.javaNames = null;
            this.annotations = null;
            this.primitiveTypes = null;
            this.pointerTypes = null;
            this.genericTypes = null;
            this.cast = false;
            this.define = false;
            this.complex = false;
            this.forwardDeclared = false;
            this.textBefore = null;
            this.textAfter = null;
        }

        public Info(String... cppNames) {
            this.cppNames = null;
            this.javaNames = null;
            this.annotations = null;
            this.primitiveTypes = null;
            this.pointerTypes = null;
            this.genericTypes = null;
            this.cast = false;
            this.define = false;
            this.complex = false;
            this.forwardDeclared = false;
            this.textBefore = null;
            this.textAfter = null;
            this.cppNames = cppNames;
        }

        public Info cppNames(String... cppNames) {
            this.cppNames = cppNames;
            return this;
        }

        public Info javaNames(String... javaNames) {
            this.javaNames = javaNames;
            return this;
        }

        public Info annotations(String... annotations) {
            this.annotations = annotations;
            return this;
        }

        public Info primitiveTypes(String... primitiveTypes) {
            this.primitiveTypes = primitiveTypes;
            return this;
        }

        public Info pointerTypes(String... pointerTypes) {
            this.pointerTypes = pointerTypes;
            return this;
        }

        public Info genericTypes(String... genericTypes) {
            this.genericTypes = genericTypes;
            return this;
        }

        public Info cast(boolean cast) {
            this.cast = cast;
            return this;
        }

        public Info define(boolean define) {
            this.define = define;
            return this;
        }

        public Info complex(boolean complex) {
            this.complex = complex;
            return this;
        }

        public Info forwardDeclared(boolean forwardDeclared) {
            this.forwardDeclared = forwardDeclared;
            return this;
        }

        public Info textBefore(String textBefore) {
            this.textBefore = textBefore;
            return this;
        }

        public Info textAfter(String textAfter) {
            this.textAfter = textAfter;
            return this;
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Info m7clone() {
            Info i = new Info();
            i.cppNames = this.cppNames != null ? (String[]) this.cppNames.clone() : null;
            i.javaNames = this.javaNames != null ? (String[]) this.javaNames.clone() : null;
            i.annotations = this.annotations != null ? (String[]) this.annotations.clone() : null;
            i.primitiveTypes = this.primitiveTypes != null ? (String[]) this.primitiveTypes.clone() : null;
            i.pointerTypes = this.pointerTypes != null ? (String[]) this.pointerTypes.clone() : null;
            i.genericTypes = this.genericTypes != null ? (String[]) this.genericTypes.clone() : null;
            i.cast = this.cast;
            i.define = this.define;
            i.forwardDeclared = this.forwardDeclared;
            i.textBefore = this.textBefore;
            i.textAfter = this.textAfter;
            return i;
        }
    }

    public static class InfoMap extends HashMap<String, LinkedList<Info>> {
        static final InfoMap defaults = new InfoMap(null).put(new Info("void").primitiveTypes("void").pointerTypes("Pointer")).put(new Info("FILE").pointerTypes("Pointer").cast(true)).put(new Info("va_list").pointerTypes("Pointer").cast(true)).put(new Info("int8_t", "jbyte", "signed char").primitiveTypes("byte").pointerTypes("BytePointer", "ByteBuffer", "byte[]")).put(new Info("uint8_t", "char", "unsigned char").primitiveTypes("byte").pointerTypes("BytePointer", "ByteBuffer", "byte[]").cast(true)).put(new Info("int16_t", "jshort", "short", "signed short", "short int", "signed short int").primitiveTypes("short").pointerTypes("ShortPointer", "ShortBuffer", "short[]")).put(new Info("uint16_t", "unsigned short", "unsigned short int").primitiveTypes("short").pointerTypes("ShortPointer", "ShortBuffer", "short[]").cast(true)).put(new Info("int32_t", "jint", "int", "signed int", "signed").primitiveTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]")).put(new Info("uint32_t", "unsigned int", "unsigned").primitiveTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]").cast(true)).put(new Info("int64_t", "__int64", "jlong", "long long", "signed long long", "long long int", "signed long long int").primitiveTypes("long").pointerTypes("LongPointer", "LongBuffer", "long[]")).put(new Info("uint64_t", "__uint64", "unsigned long long", "unsigned long long int").primitiveTypes("long").pointerTypes("LongPointer", "LongBuffer", "long[]").cast(true)).put(new Info("long", "signed long", "long int", "signed long int").primitiveTypes("long").pointerTypes("CLongPointer")).put(new Info("unsigned long", "unsigned long int").primitiveTypes("long").pointerTypes("CLongPointer").cast(true)).put(new Info("size_t").primitiveTypes("long").pointerTypes("SizeTPointer")).put(new Info("float", "jfloat").primitiveTypes("float").pointerTypes("FloatPointer", "FloatBuffer", "float[]")).put(new Info("double", "jdouble").primitiveTypes("double").pointerTypes("DoublePointer", "DoubleBuffer", "double[]")).put(new Info("bool", "jboolean").primitiveTypes("boolean").pointerTypes("BoolPointer").cast(true)).put(new Info("const char").primitiveTypes("byte").pointerTypes("@Cast(\"const char*\") BytePointer", "String")).put(new Info("position").javaNames("_position")).put(new Info("limit").javaNames("_limit")).put(new Info("capacity").javaNames("_capacity"));
        InfoMap parent;

        public InfoMap() {
            this.parent = null;
            this.parent = defaults;
        }

        public InfoMap(InfoMap parent) {
            this.parent = null;
            this.parent = parent;
        }

        static String sort(String name) {
            return sort(name, false);
        }

        static String sort(String name, boolean unconst) {
            if (name == null) {
                return null;
            }
            TreeSet<String> set = new TreeSet<>();
            try {
                Tokenizer tokenizer = new Tokenizer(new StringReader(name));
                while (true) {
                    Token token = tokenizer.nextToken();
                    if (token.isEmpty()) {
                        break;
                    }
                    set.add(token.value);
                }
                boolean foundConst = false;
                String name2 = "";
                Iterator i$ = set.iterator();
                while (i$.hasNext()) {
                    String s = i$.next();
                    if ("const".equals(s)) {
                        foundConst = true;
                    } else {
                        name2 = name2 + s + " ";
                    }
                }
                if (!unconst && foundConst) {
                    return "const " + name2.trim();
                }
                return name2.trim();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        public LinkedList<Info> get(String cppName) {
            String key = sort(cppName, false);
            LinkedList<Info> infoList = (LinkedList) super.get((Object) key);
            if (infoList == null) {
                String key2 = sort(cppName, true);
                infoList = (LinkedList) super.get((Object) key2);
            }
            if (infoList == null && this.parent != null) {
                infoList = this.parent.get(cppName);
            }
            if (infoList == null) {
                return new LinkedList<>();
            }
            return infoList;
        }

        public InfoMap put(Info info) {
            String[] arr$ = info.cppNames != null ? info.cppNames : new String[]{null};
            for (String cppName : arr$) {
                String key = sort(cppName, false);
                LinkedList<Info> infoList = (LinkedList) super.get((Object) key);
                if (infoList == null) {
                    infoList = new LinkedList<>();
                }
                if (!infoList.contains(info)) {
                    infoList.add(info);
                }
                super.put(key, infoList);
            }
            return this;
        }
    }

    static class Token implements Cloneable {
        static final int COMMENT = 4;
        static final int FLOAT = 2;
        static final int IDENTIFIER = 5;
        static final int INTEGER = 1;
        static final int STRING = 3;
        File file;
        int lineNumber;
        String spacing;
        int type;
        String value;
        static final Token EOF = new Token();
        static final Token CONST = new Token(5, "const");
        static final Token DEFINE = new Token(5, "define");
        static final Token IF = new Token(5, "if");
        static final Token IFDEF = new Token(5, "ifdef");
        static final Token IFNDEF = new Token(5, "ifndef");
        static final Token ELIF = new Token(5, "elif");
        static final Token ELSE = new Token(5, "else");
        static final Token ENDIF = new Token(5, "endif");
        static final Token ENUM = new Token(5, "enum");
        static final Token EXTERN = new Token(5, "extern");
        static final Token INLINE = new Token(5, "inline");
        static final Token STATIC = new Token(5, "static");
        static final Token CLASS = new Token(5, "class");
        static final Token STRUCT = new Token(5, "struct");
        static final Token UNION = new Token(5, "union");
        static final Token TEMPLATE = new Token(5, "template");
        static final Token TYPEDEF = new Token(5, "typedef");
        static final Token TYPENAME = new Token(5, "typename");

        Token() {
            this.file = null;
            this.lineNumber = 0;
            this.type = -1;
            this.spacing = "";
            this.value = "";
        }

        Token(int type, String value) {
            this.file = null;
            this.lineNumber = 0;
            this.type = -1;
            this.spacing = "";
            this.value = "";
            this.type = type;
            this.value = value;
        }

        boolean match(Object... tokens) {
            boolean found = false;
            for (Object t : tokens) {
                found = found || equals(t);
            }
            return found;
        }

        Token expect(Object... tokens) throws Exception {
            if (!match(tokens)) {
                throw new Exception(this.file + ":" + this.lineNumber + ": Unexpected token '" + toString() + "'");
            }
            return this;
        }

        boolean isEmpty() {
            return this.type == -1 && this.spacing.isEmpty();
        }

        /* renamed from: clone, reason: merged with bridge method [inline-methods] */
        public Token m8clone() {
            Token t = new Token();
            t.file = this.file;
            t.lineNumber = this.lineNumber;
            t.type = this.type;
            t.spacing = this.spacing;
            t.value = this.value;
            return t;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (obj.getClass() == Integer.class) {
                return this.type == ((Integer) obj).intValue();
            }
            if (obj.getClass() == Character.class) {
                return this.type == ((Character) obj).charValue();
            }
            if (obj.getClass() == String.class) {
                return obj.equals(this.value);
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            Token other = (Token) obj;
            if (this.type == other.type) {
                if (this.value == null && other.value == null) {
                    return true;
                }
                if (this.value != null && this.value.equals(other.value)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return this.type;
        }

        public String toString() {
            return (this.value == null || this.value.length() <= 0) ? String.valueOf((char) this.type) : this.value;
        }
    }

    static class Tokenizer implements Closeable {
        StringBuilder buffer;
        File file;
        int lastChar;
        int lineNumber;
        String lineSeparator;
        Reader reader;

        Tokenizer(Reader reader) {
            this.file = null;
            this.reader = null;
            this.lineSeparator = null;
            this.lastChar = -1;
            this.lineNumber = 1;
            this.buffer = new StringBuilder();
            this.reader = reader;
        }

        Tokenizer(File file) throws FileNotFoundException {
            this.file = null;
            this.reader = null;
            this.lineSeparator = null;
            this.lastChar = -1;
            this.lineNumber = 1;
            this.buffer = new StringBuilder();
            this.file = file;
            this.reader = new BufferedReader(new FileReader(file));
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.reader.close();
        }

        int readChar() throws IOException {
            String str;
            if (this.lastChar != -1) {
                int c = this.lastChar;
                this.lastChar = -1;
                return c;
            }
            int c2 = this.reader.read();
            if (c2 == 13 || c2 == 10) {
                this.lineNumber++;
                int c22 = c2 == 13 ? this.reader.read() : -1;
                if (this.lineSeparator == null) {
                    if (c2 == 13 && c22 == 10) {
                        str = "\r\n";
                    } else {
                        str = c2 == 13 ? "\r" : "\n";
                    }
                    this.lineSeparator = str;
                }
                if (c22 != 10) {
                    this.lastChar = c22;
                }
                c2 = 10;
            }
            return c2;
        }

        public Token nextToken() throws IOException {
            int c;
            int c2;
            int c3;
            Token token = new Token();
            int c4 = readChar();
            this.buffer.setLength(0);
            if (Character.isWhitespace(c4)) {
                this.buffer.append((char) c4);
                while (true) {
                    c4 = readChar();
                    if (c4 == -1 || !Character.isWhitespace(c4)) {
                        break;
                    }
                    this.buffer.append((char) c4);
                }
            }
            token.file = this.file;
            token.lineNumber = this.lineNumber;
            token.spacing = this.buffer.toString();
            this.buffer.setLength(0);
            if (Character.isLetter(c4) || c4 == 95) {
                token.type = 5;
                this.buffer.append((char) c4);
                while (true) {
                    c = readChar();
                    if (c == -1 || !(Character.isDigit(c) || Character.isLetter(c) || c == 95)) {
                        break;
                    }
                    this.buffer.append((char) c);
                }
                token.value = this.buffer.toString();
                this.lastChar = c;
            } else if (Character.isDigit(c4) || c4 == 46 || c4 == 45 || c4 == 43) {
                token.type = c4 == 46 ? 2 : 1;
                this.buffer.append((char) c4);
                int prevc = 0;
                while (true) {
                    c2 = readChar();
                    if (c2 == -1 || !(Character.isDigit(c2) || c2 == 46 || c2 == 45 || c2 == 43 || ((c2 >= 97 && c2 <= 102) || c2 == 108 || c2 == 117 || c2 == 120 || ((c2 >= 65 && c2 <= 70) || c2 == 76 || c2 == 85 || c2 == 88)))) {
                        break;
                    }
                    if (c2 == 46) {
                        token.type = 2;
                    }
                    if (c2 != 117 && c2 != 85) {
                        this.buffer.append((char) c2);
                    }
                    prevc = c2;
                }
                if (prevc == 102 || prevc == 70) {
                    token.type = 2;
                }
                token.value = this.buffer.toString();
                if (token.type == 1 && token.value.endsWith("LL")) {
                    token.value = token.value.substring(0, token.value.length() - 1);
                }
                this.lastChar = c2;
            } else if (c4 == 34) {
                token.type = 3;
                this.buffer.append('\"');
                int prevc2 = 0;
                while (true) {
                    int c5 = readChar();
                    if (c5 == -1 || (prevc2 != 92 && c5 == 34)) {
                        break;
                    }
                    this.buffer.append((char) c5);
                    prevc2 = c5;
                }
                this.buffer.append('\"');
                token.value = this.buffer.toString();
            } else if (c4 == 47) {
                int c6 = readChar();
                if (c6 == 47) {
                    token.type = 4;
                    this.buffer.append('/').append('/');
                    int prevc3 = 0;
                    while (true) {
                        c3 = readChar();
                        if (c3 == -1 || (prevc3 != 92 && c3 == 10)) {
                            break;
                        }
                        this.buffer.append((char) c3);
                        prevc3 = c3;
                    }
                    token.value = this.buffer.toString();
                    this.lastChar = c3;
                } else if (c6 == 42) {
                    token.type = 4;
                    this.buffer.append('/').append('*');
                    int prevc4 = 0;
                    while (true) {
                        int c7 = readChar();
                        if (c7 == -1 || (prevc4 == 42 && c7 == 47)) {
                            break;
                        }
                        this.buffer.append((char) c7);
                        prevc4 = c7;
                    }
                    this.buffer.append('/');
                    token.value = this.buffer.toString();
                } else {
                    this.lastChar = c6;
                    token.type = 47;
                }
            } else {
                if (c4 == 92) {
                    int c22 = readChar();
                    if (c22 == 10) {
                        String s = token.spacing;
                        Token token2 = nextToken();
                        token2.spacing = s;
                        return token2;
                    }
                    this.lastChar = c22;
                }
                token.type = c4;
            }
            return token;
        }
    }

    public Parser(Properties properties, InfoMap infoMap) {
        this.properties = null;
        this.infoMap = null;
        this.properties = properties;
        this.infoMap = infoMap;
    }

    Token getToken() {
        return getToken(0);
    }

    Token getToken(int i) {
        return this.tokenIndex + i < this.tokenArray.length ? this.tokenArray[this.tokenIndex + i] : Token.EOF;
    }

    Token nextToken() {
        return nextToken(true);
    }

    Token nextToken(boolean skipComment) {
        this.tokenIndex++;
        while (skipComment && getToken().match(4)) {
            this.tokenIndex++;
        }
        return getToken();
    }

    static class TemplateMap extends LinkedHashMap<String, String> {
        LinkedHashMap<String, String> defaults;

        TemplateMap(TemplateMap defaults) {
            this.defaults = null;
            this.defaults = defaults;
        }

        String get(String key) {
            String value = (String) super.get((Object) key);
            return (value != null || this.defaults == null) ? value : this.defaults.get(key);
        }
    }

    TemplateMap template(TemplateMap defaults) throws Exception {
        if (getToken().match(Token.TEMPLATE)) {
            TemplateMap map = new TemplateMap(defaults);
            nextToken().expect('<');
            Token token = nextToken();
            while (true) {
                if (token.match(Token.EOF)) {
                    break;
                }
                if (token.match(Token.CLASS, Token.TYPENAME)) {
                    map.put(nextToken().expect(5).value, null);
                }
                if (!nextToken().expect(',', '>').match('>')) {
                    token = nextToken();
                } else {
                    nextToken();
                    break;
                }
            }
            return map;
        }
        return defaults;
    }

    static class Declarator {
        int infoNumber = 0;
        int indices = 0;
        boolean constValue = false;
        boolean constPointer = false;
        String annotations = "";
        String cppType = "";
        String javaType = "";
        String objectName = "";
        String convention = "";
        String definitions = "";
        String parameters = "";

        Declarator() {
        }
    }

    Declarator declarator(TemplateMap typeMap, String defaultName, int infoNumber, int varNumber, boolean arrayAsPointer, boolean pointerAsArray) throws Exception {
        String str;
        boolean isTypedef = getToken().match(Token.TYPEDEF);
        Declarator decl = new Declarator();
        String cppName = "";
        boolean simpleType = false;
        Token token = getToken();
        while (!token.match(Token.EOF) && token.match(5)) {
            if (token.match(Token.CONST)) {
                decl.constValue = true;
            } else if (token.match(Token.TYPEDEF, Token.ENUM, Token.CLASS, Token.STRUCT, Token.UNION)) {
                continue;
            } else if (token.match("signed", "unsigned", "char", "short", "int", "long", "bool", "float", "double")) {
                if (!simpleType) {
                    cppName = token.value + " ";
                } else {
                    cppName = cppName + token.value + " ";
                }
                simpleType = true;
            } else {
                if (cppName.length() > 0 && !getToken(1).match('*', '&', 5, Token.CONST)) {
                    break;
                }
                LinkedList<Info> infoList = this.infoMap.get(token.value);
                if (infoList.size() > 0 && infoList.getFirst().annotations != null) {
                    String[] arr$ = infoList.getFirst().annotations;
                    for (String s : arr$) {
                        decl.annotations += s + " ";
                    }
                } else {
                    cppName = token.value;
                }
            }
            token = nextToken();
        }
        String cppName2 = cppName.trim();
        if (typeMap != null && typeMap.containsKey(cppName2)) {
            cppName2 = typeMap.get(cppName2);
        }
        decl.javaType = cppName2;
        decl.cppType = cppName2;
        if ("...".equals(getToken().value)) {
            nextToken();
            return null;
        }
        int count = 0;
        Token token2 = getToken();
        while (varNumber > 0 && !token2.match(Token.EOF)) {
            if (token2.match('(')) {
                count++;
            } else if (token2.match(')')) {
                count--;
            } else if (count != 0) {
                continue;
            } else if (token2.match(',')) {
                varNumber--;
            } else if (token2.match(';')) {
                nextToken();
                return null;
            }
            token2 = nextToken();
        }
        String cast = cppName2;
        int indirections = 0;
        boolean reference = false;
        Token token3 = getToken();
        while (!token3.match(Token.EOF)) {
            if (token3.match('*')) {
                indirections++;
            } else if (token3.match('&')) {
                reference = true;
            } else {
                if (!token3.match(Token.CONST)) {
                    break;
                }
                decl.constPointer = true;
            }
            cast = cast + token3.toString();
            token3 = nextToken();
        }
        int indirections2 = 0;
        decl.objectName = defaultName;
        if (getToken().match('(')) {
            while (getToken().match('(')) {
                nextToken();
            }
            Token token4 = getToken();
            while (true) {
                if (token4.match(Token.EOF)) {
                    break;
                }
                if (!token4.match(5)) {
                    if (!token4.match('*')) {
                        if (!token4.match('[')) {
                            if (token4.match(')')) {
                                nextToken();
                                break;
                            }
                        } else {
                            decl.indices++;
                        }
                    } else {
                        indirections2++;
                        decl.convention = decl.objectName;
                        decl.objectName = defaultName;
                    }
                } else {
                    decl.objectName = token4.value;
                }
                token4 = nextToken();
            }
            while (getToken().match(')')) {
                nextToken();
            }
        } else if (getToken().match(5)) {
            decl.objectName = getToken().value;
            nextToken();
        }
        boolean bracket = false;
        Token token5 = getToken();
        while (!token5.match(Token.EOF)) {
            if (!bracket && token5.match('[')) {
                bracket = true;
                decl.indices++;
            } else {
                if (!bracket) {
                    break;
                }
                if (bracket && token5.match(']')) {
                    bracket = false;
                }
            }
            token5 = nextToken();
        }
        while (decl.indices > 0 && indirections2 > 0) {
            decl.indices++;
            indirections2--;
        }
        if (arrayAsPointer && decl.indices > 0) {
            indirections++;
            cast = cast + "*";
        }
        if (pointerAsArray && indirections > 1) {
            decl.indices++;
            indirections--;
            cast = cast.substring(0, cast.length() - 1);
        }
        if (getToken().match(':')) {
            decl.annotations += "@NoOffset ";
            nextToken().expect(1);
            nextToken().expect(',', ';');
        }
        int infoLength = 1;
        boolean primitive = false;
        boolean needCast = false;
        boolean implicitConst = false;
        String key = (!decl.constValue || indirections >= 2 || reference) ? decl.cppType : "const " + decl.cppType;
        LinkedList<Info> infoList2 = this.infoMap.get(key);
        if (infoList2.size() > 0) {
            Info info = infoList2.getFirst();
            primitive = (info.primitiveTypes == null || indirections != 0 || reference) ? false : true;
            needCast = info.cast;
            implicitConst = info.cppNames[0].startsWith("const ");
            if (primitive) {
                infoLength = info.primitiveTypes.length;
            } else {
                infoLength = info.pointerTypes != null ? info.pointerTypes.length : 1;
            }
            decl.infoNumber = infoNumber < 0 ? 0 : infoNumber % infoLength;
            if (primitive) {
                str = info.primitiveTypes[decl.infoNumber];
            } else {
                str = info.pointerTypes != null ? info.pointerTypes[decl.infoNumber] : decl.cppType;
            }
            decl.javaType = str;
        }
        if (!primitive) {
            if (indirections == 0 && !reference) {
                decl.annotations += "@ByVal ";
            } else if (indirections == 0 && reference) {
                decl.annotations += "@ByRef ";
            } else if (indirections == 1 && reference) {
                decl.annotations += "@ByPtrRef ";
            } else if (indirections == 2 && !reference && infoNumber >= 0) {
                decl.annotations += "@ByPtrPtr ";
                if (decl.cppType.equals("void")) {
                    needCast = true;
                }
            } else if (indirections >= 2) {
                decl.infoNumber += infoLength;
                needCast = true;
                decl.javaType = "PointerPointer";
                if (reference) {
                    decl.annotations += "@ByRef ";
                }
            }
            if (!needCast && decl.constValue && !implicitConst) {
                decl.annotations = "@Const " + decl.annotations;
            }
        }
        if (needCast || (arrayAsPointer && decl.indices > 1)) {
            if (decl.constValue) {
                cast = "const " + cast;
            }
            if (primitive || indirections != 0 || reference) {
                decl.annotations = "@Cast(\"" + cast + "\") " + decl.annotations;
            } else {
                decl.annotations += "@Cast(\"" + cast + "*\") ";
            }
        }
        Parameters params = parameters(typeMap, infoNumber);
        if (params != null) {
            decl.infoNumber = Math.max(decl.infoNumber, params.infoNumber);
            if (params.definitions.length() > 0) {
                decl.definitions += params.definitions + "\n";
            }
            if (indirections2 == 0) {
                decl.parameters = params.list;
            } else {
                String functionType = isTypedef ? decl.objectName : Character.toUpperCase(decl.objectName.charAt(0)) + decl.objectName.substring(1) + params.signature;
                if (infoNumber <= params.infoNumber) {
                    decl.definitions += "public static class " + functionType + " extends FunctionPointer {\n    static { Loader.load(); }\n    public    " + functionType + "(Pointer p) { super(p); }\n    protected " + functionType + "() { allocate(); }\n    private native void allocate();\n    public native " + decl.annotations + decl.javaType + " call" + params.list + ";\n}\n";
                }
                decl.annotations = "";
                decl.javaType = functionType;
                decl.parameters = "";
            }
        }
        LinkedList<Info> infoList3 = this.infoMap.get(decl.objectName);
        if (infoList3.size() > 0) {
            Info info2 = infoList3.getFirst();
            if (info2.javaNames != null && info2.javaNames.length > 0) {
                decl.annotations += "@Name(\"" + decl.objectName + "\") ";
                decl.objectName = info2.javaNames[0];
                return decl;
            }
            return decl;
        }
        return decl;
    }

    String commentBefore() throws Exception {
        String comment = "";
        while (this.tokenIndex > 0 && getToken(-1).match(4)) {
            this.tokenIndex--;
        }
        Token token = getToken();
        while (token.match(4)) {
            if (token.value.length() <= 3 || token.value.charAt(3) != '<') {
                comment = comment + token.spacing + token.value;
            }
            token = nextToken(false);
        }
        return comment;
    }

    String commentAfter() throws Exception {
        String comment = "";
        while (this.tokenIndex > 0 && getToken(-1).match(4)) {
            this.tokenIndex--;
        }
        Token token = getToken();
        while (token.match(4)) {
            if (token.value.length() > 3 && token.value.charAt(3) == '<') {
                String value = token.value;
                comment = comment + (comment.length() > 0 ? " * " : "/**") + value.substring(4);
            }
            token = nextToken(false);
        }
        if (comment.length() > 0 && !comment.endsWith("*/")) {
            return comment + " */";
        }
        return comment;
    }

    boolean attribute() throws Exception {
        if (!getToken().match(5)) {
            return false;
        }
        if (!nextToken().match('(')) {
            return true;
        }
        int count = 1;
        Token token = nextToken();
        while (!token.match(Token.EOF) && count > 0) {
            if (token.match('(')) {
                count++;
            } else if (token.match(')')) {
                count--;
            }
            token = nextToken(false);
        }
        return true;
    }

    boolean body() throws Exception {
        if (!getToken().match('{')) {
            return false;
        }
        int count = 1;
        Token token = nextToken();
        while (!token.match(Token.EOF) && count > 0) {
            if (token.match('{')) {
                count++;
            } else if (token.match('}')) {
                count--;
            }
            token = nextToken(false);
        }
        return true;
    }

    static class Parameters {
        int infoNumber = 0;
        String list = "";
        String definitions = "";
        String signature = "";

        Parameters() {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x01a8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.googlecode.javacpp.Parser.Parameters parameters(com.googlecode.javacpp.Parser.TemplateMap r19, int r20) throws com.googlecode.javacpp.Parser.Exception {
        /*
            Method dump skipped, instructions count: 426
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.googlecode.javacpp.Parser.parameters(com.googlecode.javacpp.Parser$TemplateMap, int):com.googlecode.javacpp.Parser$Parameters");
    }

    String function(String group, TemplateMap templateMap) throws Exception {
        int backIndex = this.tokenIndex;
        String spacing = getToken().spacing;
        String access = (group == null || group.length() == 0) ? "public static native " : "public native ";
        Token token = getToken();
        while (!token.match(Token.EOF)) {
            if (!token.match(Token.STATIC)) {
                if (!token.match(Token.INLINE)) {
                    break;
                }
            } else {
                access = "public static native ";
            }
            token = nextToken();
        }
        int startIndex = this.tokenIndex;
        Declarator decl = declarator(templateMap, null, 0, 0, false, false);
        String name = decl.objectName;
        if (name == null || decl.parameters.length() == 0) {
            this.tokenIndex = backIndex;
            return null;
        }
        String text = "";
        String statements = "";
        String definitions = "";
        LinkedList<Info> infoList = this.infoMap.get(name);
        if (infoList.size() == 0) {
            infoList.add(null);
        }
        Iterator<Info> it = infoList.iterator();
        while (it.hasNext()) {
            Info info = it.next();
            if (info != null) {
                if (info.genericTypes != null && templateMap != null) {
                    int count = 0;
                    for (String key : templateMap.keySet()) {
                        if (count < info.genericTypes.length) {
                            templateMap.put(key, info.genericTypes[count]);
                            count++;
                        }
                    }
                }
                if (info.javaNames == null) {
                    name = info.cppNames[0];
                } else {
                    name = info.javaNames.length == 0 ? "" : info.javaNames[0];
                }
                if (!name.equals(info.cppNames[0]) && name.length() > 0) {
                    name = "@Name(\"" + info.cppNames[0] + "\") " + name;
                }
            }
            LinkedList<Declarator> prevDecl = new LinkedList<>();
            for (int n = -1; n < Integer.MAX_VALUE; n++) {
                this.tokenIndex = startIndex;
                Declarator decl2 = declarator(templateMap, null, n, 0, false, false);
                boolean found = false;
                Iterator i$ = prevDecl.iterator();
                while (i$.hasNext()) {
                    Declarator d = i$.next();
                    found |= decl2.parameters.equals(d.parameters);
                }
                if (found && n > 0) {
                    break;
                }
                if (name.length() > 0 && !found) {
                    statements = statements + access + decl2.annotations + decl2.javaType + " " + name + decl2.parameters + ";\n";
                    definitions = definitions + decl2.definitions;
                }
                prevDecl.add(decl2);
            }
            while (attribute()) {
            }
            if (getToken().match('{')) {
                body();
            } else {
                nextToken();
            }
        }
        String comment = commentAfter();
        if (comment.length() > 0) {
            statements = comment + "\n" + statements;
        }
        Scanner scanner = new Scanner(definitions);
        while (scanner.hasNextLine()) {
            text = text + spacing + scanner.nextLine();
            int newline = spacing.lastIndexOf(10);
            if (newline > 0) {
                spacing = spacing.substring(newline);
            }
        }
        Scanner scanner2 = new Scanner(statements);
        while (scanner2.hasNextLine()) {
            text = text + spacing + scanner2.nextLine();
            int newline2 = spacing.lastIndexOf(10);
            if (newline2 > 0) {
                spacing = spacing.substring(newline2);
            }
        }
        return text;
    }

    String variable(String group) throws Exception {
        int backIndex = this.tokenIndex;
        String spacing = getToken().spacing;
        if (declarator(null, null, 0, 0, false, true).objectName == null || !getToken().match('[', '=', ',', ':', ';')) {
            this.tokenIndex = backIndex;
            return null;
        }
        String text = "";
        String statements = "";
        String definitions = "";
        for (int n = 0; n < Integer.MAX_VALUE; n++) {
            this.tokenIndex = backIndex;
            Declarator decl = declarator(null, null, -1, n, false, true);
            if (decl == null) {
                break;
            }
            String access = (group == null || group.length() == 0) ? "public static native " : "public native ";
            String setterType = (group == null || group.length() == 0) ? "void " : group + " ";
            String name = decl.objectName;
            String indices = "";
            for (int i = 0; i < decl.indices; i++) {
                if (i > 0) {
                    indices = indices + ", ";
                }
                indices = indices + "int " + ((char) (i + 105));
            }
            if (decl.constValue) {
                statements = statements + "@MemberGetter ";
            }
            String statements2 = statements + access + decl.annotations + decl.javaType + " " + name + "(" + indices + ");";
            if (!decl.constValue) {
                if (decl.indices > 0) {
                    indices = indices + ", ";
                }
                statements2 = statements2 + " " + access + setterType + name + "(" + indices + decl.javaType + " " + name + ");";
            }
            statements = statements2 + "\n";
            definitions = definitions + decl.definitions;
            if (decl.indices > 0) {
                this.tokenIndex = backIndex;
                Declarator decl2 = declarator(null, null, -1, n, true, false);
                statements = statements + "@MemberGetter " + access + decl2.annotations + decl2.javaType + " " + name + "();\n";
            }
        }
        String comment = commentAfter();
        if (comment.length() > 0) {
            statements = comment + "\n" + statements;
        }
        Scanner scanner = new Scanner(definitions);
        while (scanner.hasNextLine()) {
            text = text + spacing + scanner.nextLine();
            int newline = spacing.lastIndexOf(10);
            if (newline > 0) {
                spacing = spacing.substring(newline);
            }
        }
        Scanner scanner2 = new Scanner(statements);
        while (scanner2.hasNextLine()) {
            text = text + spacing + scanner2.nextLine();
            int newline2 = spacing.lastIndexOf(10);
            if (newline2 > 0) {
                spacing = spacing.substring(newline2);
            }
        }
        return text;
    }

    String macro() throws Exception {
        if (!getToken().match('#')) {
            return null;
        }
        String spacing = getToken().spacing;
        Token keyword = nextToken();
        nextToken();
        int beginIndex = this.tokenIndex;
        Token token = getToken();
        while (!token.match(Token.EOF) && !token.match(4) && token.spacing.indexOf(10) < 0) {
            token = nextToken(false);
        }
        int endIndex = this.tokenIndex;
        String text = "";
        if (!keyword.match(Token.DEFINE) || beginIndex + 1 >= endIndex) {
            if (keyword.match(Token.IF, Token.IFDEF, Token.IFNDEF, Token.ELIF) && beginIndex < endIndex) {
                this.tokenIndex = beginIndex;
                String value = "";
                Token token2 = getToken();
                while (this.tokenIndex < endIndex) {
                    value = value + token2.spacing + token2;
                    token2 = nextToken();
                }
                String text2 = spacing + "// #" + keyword + value;
                LinkedList<Info> infoList = this.infoMap.get(value);
                boolean define = true;
                if (infoList.size() > 0) {
                    Info info = infoList.getFirst();
                    if (keyword.match(Token.IFNDEF)) {
                        define = !info.define;
                    } else {
                        define = info.define;
                    }
                }
                if (!define) {
                    int count = 1;
                    Token prevToken = new Token();
                    Token token3 = getToken();
                    while (!token3.match(Token.EOF) && count > 0) {
                        if (prevToken.match('#') && token3.match(Token.IF, Token.IFDEF, Token.IFNDEF)) {
                            count++;
                        } else if (prevToken.match('#') && token3.match(Token.ELIF, Token.ELSE, Token.ENDIF)) {
                            count--;
                        }
                        prevToken = token3;
                        token3 = nextToken(false);
                    }
                    this.tokenIndex -= 2;
                    return text2;
                }
                return text2;
            }
            this.tokenIndex = beginIndex;
            String text3 = spacing + "// #" + keyword;
            Token token4 = getToken();
            while (this.tokenIndex < endIndex) {
                text3 = text3 + token4.spacing + token4;
                token4 = nextToken();
            }
            return text3;
        }
        this.tokenIndex = beginIndex;
        String name = getToken().value;
        Token first = nextToken();
        String statements = "";
        LinkedList<Info> infoList2 = this.infoMap.get(name);
        if (first.spacing.length() == 0 && first.match('(')) {
            Iterator i$ = infoList2.iterator();
            while (i$.hasNext()) {
                Info info2 = i$.next();
                if (info2.genericTypes != null && info2.genericTypes.length != 0) {
                    int count2 = 1;
                    this.tokenIndex = beginIndex + 2;
                    String params = "(";
                    Token token5 = getToken();
                    while (this.tokenIndex < endIndex && count2 < info2.genericTypes.length) {
                        if (token5.match(5)) {
                            String type = info2.genericTypes[count2];
                            String name2 = token5.value;
                            if (name2.equals("...")) {
                                name2 = "arg" + count2;
                            }
                            params = params + type + " " + name2;
                            count2++;
                            if (count2 < info2.genericTypes.length) {
                                params = params + ", ";
                            }
                        } else if (token5.match(')')) {
                            break;
                        }
                        token5 = nextToken();
                    }
                    while (count2 < info2.genericTypes.length) {
                        String type2 = info2.genericTypes[count2];
                        params = params + type2 + " " + ("arg" + count2);
                        count2++;
                        if (count2 < info2.genericTypes.length) {
                            params = params + ", ";
                        }
                    }
                    String params2 = params + ")";
                    String type3 = info2.genericTypes[0];
                    String name3 = info2.javaNames == null ? info2.cppNames[0] : info2.javaNames[0];
                    if (!name3.equals(info2.cppNames[0])) {
                        name3 = "@Name(\"" + info2.cppNames[0] + "\") " + name3;
                    }
                    statements = statements + "public static native " + type3 + " " + name3 + params2 + ";\n";
                }
            }
        } else if (infoList2.size() == 0 || infoList2.getFirst().genericTypes == null || infoList2.getFirst().genericTypes.length > 0) {
            String value2 = "";
            String type4 = "int";
            String cat = "";
            this.tokenIndex = beginIndex + 1;
            Token prevToken2 = new Token();
            boolean complex = false;
            Token token6 = getToken();
            while (true) {
                if (this.tokenIndex < endIndex) {
                    if (!token6.match(3)) {
                        if (!token6.match(2)) {
                            if (token6.match(1) && token6.value.endsWith("L")) {
                                type4 = "long";
                                cat = "";
                                break;
                            }
                            if ((prevToken2.match(5) && token6.match('(')) || token6.match('{', '}')) {
                                complex = true;
                            }
                            prevToken2 = token6;
                            token6 = nextToken();
                        } else {
                            type4 = "double";
                            cat = "";
                            break;
                        }
                    } else {
                        type4 = "String";
                        cat = " + ";
                        break;
                    }
                } else {
                    break;
                }
            }
            if (infoList2.size() > 0) {
                Info info3 = infoList2.getFirst();
                if (info3.genericTypes != null) {
                    type4 = info3.genericTypes[0];
                }
                name = info3.cppNames[0];
                if (info3.javaNames != null) {
                    name = info3.javaNames[0];
                }
                if (info3.complex) {
                    complex = true;
                }
            }
            this.tokenIndex = beginIndex + 1;
            if (complex) {
                statements = "public static native @MemberGetter " + type4 + " " + name + "();\n";
                value2 = " " + name + "()";
            } else {
                Token token7 = getToken();
                while (this.tokenIndex < endIndex) {
                    value2 = value2 + token7.spacing + token7 + (this.tokenIndex < endIndex ? cat : "");
                    token7 = nextToken();
                }
            }
            int i = type4.lastIndexOf(32);
            if (i > 0) {
                type4 = type4.substring(i + 1);
            }
            statements = statements + "public static final " + type4 + " " + name + " =" + value2 + ";\n";
        }
        this.tokenIndex = endIndex;
        String comment = commentAfter();
        if (comment.length() > 0) {
            statements = comment + "\n" + statements;
        }
        Scanner scanner = new Scanner(statements);
        while (scanner.hasNextLine()) {
            text = text + spacing + scanner.nextLine();
            int newline = spacing.lastIndexOf(10);
            if (newline > 0) {
                spacing = spacing.substring(newline);
            }
        }
        return text;
    }

    String typedef() throws Exception {
        if (!getToken().match(Token.TYPEDEF)) {
            return null;
        }
        String spacing = getToken().spacing;
        Declarator decl = declarator(null, null, 0, 0, false, false);
        nextToken();
        LinkedList<Info> infoList = this.infoMap.get(decl.cppType);
        Info info = infoList.size() > 0 ? infoList.getFirst().m7clone() : new Info();
        this.infoMap.put(info.cppNames(decl.objectName).cast(true));
        String comment = commentAfter();
        String text = comment.length() > 0 ? comment + "\n" : "";
        Scanner scanner = new Scanner(decl.definitions);
        while (scanner.hasNextLine()) {
            text = text + spacing + scanner.nextLine();
            int newline = spacing.lastIndexOf(10);
            if (newline > 0) {
                spacing = spacing.substring(newline);
            }
        }
        return text;
    }

    String group(TemplateMap templateMap) throws Exception {
        int backIndex = this.tokenIndex;
        String spacing = getToken().spacing;
        boolean isTypedef = getToken().match(Token.TYPEDEF);
        boolean foundGroup = false;
        boolean doOutput = true;
        Token token = getToken();
        while (true) {
            if (!token.match(Token.EOF)) {
                if (!token.match(Token.CLASS, Token.STRUCT, Token.UNION)) {
                    if (!token.match(Token.EXTERN) || !nextToken().match(3)) {
                        if (!token.match(5)) {
                            break;
                        }
                        token = nextToken();
                    } else {
                        foundGroup = true;
                        doOutput = false;
                        break;
                    }
                } else {
                    foundGroup = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (!foundGroup) {
            this.tokenIndex = backIndex;
            return null;
        }
        if (isTypedef && !getToken(1).match('{') && getToken(2).match(5)) {
            nextToken();
        }
        String text = "";
        String name = nextToken().expect(3, 5, '{').value;
        if (!getToken(0).match('{', ';') && !getToken(1).match('{', ';')) {
            this.tokenIndex = backIndex;
            return null;
        }
        if (doOutput) {
            LinkedList<Info> infoList = this.infoMap.get(name);
            if (getToken().match(5) && nextToken().match(';')) {
                nextToken();
                if (infoList.size() == 0 || !infoList.getFirst().forwardDeclared) {
                    this.infoMap.put(new Info(name).forwardDeclared(true));
                    String text2 = spacing + "@Opaque public static class " + name + " extends Pointer {\n    public " + name + "() { }\n    public " + name + "(Pointer p) { super(p); }\n}";
                    return text2;
                }
                return "";
            }
            int index = this.tokenIndex;
            if (body() && isTypedef && getToken().match(5)) {
                name = getToken().value;
                this.infoMap.get(name);
            }
            if (name.length() == 0) {
                Token token2 = nextToken();
                while (true) {
                    if (token2.match(Token.EOF)) {
                        break;
                    }
                    if (!token2.match(';')) {
                        token2 = nextToken();
                    } else {
                        nextToken();
                        break;
                    }
                }
                return "";
            }
            this.tokenIndex = index;
            text = "" + spacing + "public static class " + name + " extends Pointer {\n    static { Loader.load(); }\n    public " + name + "() { allocate(); }\n    public " + name + "(int size) { allocateArray(size); }\n    public " + name + "(Pointer p) { super(p); }\n    private native void allocate();\n    private native void allocateArray(int size);\n    @Override public " + name + " position(int position) {\n        return (" + name + ")super.position(position);\n    }\n";
        }
        if (getToken().match('{')) {
            nextToken();
        }
        while (!getToken().match(Token.EOF, '}')) {
            text = text + declaration(name, templateMap);
        }
        if (doOutput) {
            String text3 = text + getToken().spacing + '}';
            Token token3 = nextToken();
            if (token3.match(5)) {
                token3 = nextToken();
            }
            text = text3 + token3.expect(';').spacing;
        }
        nextToken();
        return text;
    }

    String enumeration() throws Exception, NumberFormatException {
        int backIndex = this.tokenIndex;
        String enumSpacing = getToken().spacing;
        boolean isTypedef = getToken().match(Token.TYPEDEF);
        boolean foundEnum = false;
        Token token = getToken();
        while (true) {
            if (!token.match(Token.EOF)) {
                if (!token.match(Token.ENUM)) {
                    if (!token.match(5)) {
                        break;
                    }
                    token = nextToken();
                } else {
                    foundEnum = true;
                    break;
                }
            } else {
                break;
            }
        }
        if (!foundEnum) {
            this.tokenIndex = backIndex;
            return null;
        }
        if (isTypedef && !getToken(1).match('{') && getToken(2).match(5)) {
            nextToken();
        }
        boolean first = true;
        int count = 0;
        String countPrefix = " ";
        String enumerators = "";
        String macroText = "";
        String name = nextToken().expect(5, '{').value;
        if (!getToken().match('{') && !nextToken().match('{')) {
            this.tokenIndex = backIndex;
            return null;
        }
        Token token2 = nextToken();
        while (!token2.match(Token.EOF, '}')) {
            String s = macro();
            if (s != null) {
                macroText = macroText + s;
            } else {
                String comment = commentBefore();
                Token enumerator = getToken();
                String spacing2 = " ";
                String separator = first ? "" : ",";
                if (nextToken().match('=')) {
                    spacing2 = getToken().spacing;
                    countPrefix = " ";
                    int count2 = 0;
                    Token prevToken = new Token();
                    boolean complex = false;
                    Token token3 = nextToken();
                    while (true) {
                        if (!token3.match(Token.EOF, ',', '}') || count2 > 0) {
                            countPrefix = countPrefix + token3.spacing + token3;
                            if (token3.match('(')) {
                                count2++;
                            } else if (token3.match(')')) {
                                count2--;
                            }
                            if (prevToken.match(5) && token3.match('(')) {
                                complex = true;
                            }
                            prevToken = token3;
                            token3 = nextToken();
                        } else {
                            try {
                                break;
                            } catch (NumberFormatException e) {
                                count = 0;
                                if (complex) {
                                    if (!first) {
                                        separator = ";\n";
                                    }
                                    separator = separator + "public static native @MemberGetter int " + enumerator.value + "();\npublic static final int";
                                    countPrefix = " " + enumerator.value + "()";
                                }
                            }
                        }
                    }
                    count = Integer.parseInt(countPrefix.trim());
                    countPrefix = " ";
                }
                first = false;
                String enumerators2 = enumerators + separator + macroText + comment;
                macroText = "";
                String comment2 = commentAfter();
                if (comment2.length() == 0 && getToken().match(',')) {
                    nextToken();
                    comment2 = commentAfter();
                }
                String spacing = enumerator.spacing;
                if (comment2.length() > 0) {
                    enumerators2 = enumerators2 + spacing + comment2;
                    int newline = spacing.lastIndexOf(10);
                    if (newline > 0) {
                        spacing = spacing.substring(newline);
                    }
                }
                enumerators = enumerators2 + spacing + enumerator.value + spacing2 + "=" + countPrefix;
                if (countPrefix.trim().length() > 0) {
                    if (count > 0) {
                        enumerators = enumerators + " + " + count;
                    }
                } else {
                    enumerators = enumerators + count;
                }
                count++;
            }
            token2 = getToken();
        }
        String comment3 = commentBefore();
        Token token4 = nextToken();
        if (token4.match(5)) {
            name = token4.value;
            token4 = nextToken();
        }
        String text = "" + enumSpacing + "/** enum " + name + " */\n";
        int newline2 = enumSpacing.lastIndexOf(10);
        if (newline2 >= 0) {
            enumSpacing = enumSpacing.substring(newline2 + 1);
        }
        String text2 = text + enumSpacing + "public static final int" + enumerators + token4.expect(';').spacing + ";";
        this.infoMap.put(new Info(name).primitiveTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]").cast(true));
        nextToken();
        return text2 + macroText + comment3;
    }

    String declaration(String group, TemplateMap templateMap) throws Exception, NumberFormatException {
        String comment = commentBefore();
        Token token = getToken();
        String spacing = token.spacing;
        TemplateMap map = template(templateMap);
        if (map != templateMap) {
            comment = comment + spacing.substring(0, spacing.lastIndexOf(10));
        }
        String text = macro();
        if (text != null) {
            return comment + text;
        }
        String text2 = enumeration();
        if (text2 != null) {
            return comment + text2;
        }
        String text3 = group(map);
        if (text3 != null) {
            return comment + text3;
        }
        String text4 = typedef();
        if (text4 != null) {
            return comment + text4;
        }
        String text5 = function(group, map);
        if (text5 != null) {
            return comment + text5;
        }
        String text6 = variable(group);
        if (text6 != null) {
            return comment + text6;
        }
        if (attribute()) {
            return comment + spacing;
        }
        throw new Exception(token.file + ":" + token.lineNumber + ": Could not parse declaration at '" + token + "'");
    }

    public void parse(String outputFilename, String... inputFilenames) throws Exception, IOException {
        File[] files = new File[inputFilenames.length];
        for (int i = 0; i < files.length; i++) {
            files[i] = new File(inputFilenames[i]);
        }
        parse(new File(outputFilename), files);
    }

    public void parse(File outputFile, File... inputFiles) throws Exception, IOException {
        ArrayList<Token> tokens = new ArrayList<>();
        String lineSeparator = "\n";
        for (File file : inputFiles) {
            System.out.println("Parsing header file: " + file);
            Token token = new Token();
            token.type = 4;
            token.value = "\n/* Wrapper for header file " + file + " */\n\n";
            tokens.add(token);
            Tokenizer tokenizer = new Tokenizer(file);
            while (true) {
                Token token2 = tokenizer.nextToken();
                if (token2.isEmpty()) {
                    break;
                }
                if (token2.type == -1) {
                    token2.type = 4;
                }
                tokens.add(token2);
            }
            if (lineSeparator == null) {
                lineSeparator = tokenizer.lineSeparator;
            }
            tokenizer.close();
            Token token3 = new Token();
            token3.type = 4;
            token3.spacing = "\n";
            tokens.add(token3);
        }
        this.tokenArray = (Token[]) tokens.toArray(new Token[tokens.size()]);
        this.tokenIndex = 0;
        Writer out = outputFile != null ? new FileWriter(outputFile) : new Writer() { // from class: com.googlecode.javacpp.Parser.1
            @Override // java.io.Writer
            public void write(char[] cbuf, int off, int len) {
            }

            @Override // java.io.Writer, java.io.Flushable
            public void flush() {
            }

            @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
            public void close() {
            }
        };
        LinkedList<Info> infoList = this.infoMap.get((String) null);
        Iterator i$ = infoList.iterator();
        while (i$.hasNext()) {
            Info info = i$.next();
            if (info.textBefore != null) {
                out.append((CharSequence) info.textBefore.replaceAll("\n", lineSeparator));
            }
        }
        out.append((CharSequence) ("{" + lineSeparator));
        out.append((CharSequence) ("    static { Loader.load(); }" + lineSeparator));
        Iterator i$2 = infoList.iterator();
        while (i$2.hasNext()) {
            Info info2 = i$2.next();
            if (info2.textAfter != null) {
                out.append((CharSequence) info2.textAfter.replaceAll("\n", lineSeparator));
            }
        }
        while (!getToken().match(Token.EOF)) {
            out.append((CharSequence) declaration(null, null).replaceAll("\n", lineSeparator));
        }
        String comment = commentBefore();
        if (comment != null) {
            out.append((CharSequence) comment.replaceAll("\n", lineSeparator));
        }
        out.append((CharSequence) (lineSeparator + "}" + lineSeparator));
        out.close();
    }

    public File parse(String outputDirectory, Class cls) throws Exception, IOException {
        return parse(new File(outputDirectory), cls);
    }

    public File parse(File outputDirectory, Class cls) throws Exception, IOException {
        Loader.ClassProperties allProperties = Loader.loadProperties(cls, this.properties, true);
        Loader.ClassProperties clsProperties = Loader.loadProperties(cls, this.properties, false);
        LinkedList<File> allFiles = allProperties.getHeaderFiles();
        LinkedList<File> clsFiles = clsProperties.getHeaderFiles();
        LinkedList<String> allTargets = allProperties.get("parser.target");
        LinkedList<String> clsTargets = clsProperties.get("parser.target");
        String target = clsTargets.getFirst();
        int n = target.lastIndexOf(46);
        String text = n > 0 ? "/* DO NOT EDIT THIS FILE - IT IS MACHINE GENERATED */\n\npackage " + target.substring(0, n) + ";\n\n" : "/* DO NOT EDIT THIS FILE - IT IS MACHINE GENERATED */\n\n";
        String text2 = text + "import com.googlecode.javacpp.*;\nimport com.googlecode.javacpp.annotation.*;\nimport java.nio.*;\n\n";
        Iterator i$ = allTargets.iterator();
        while (i$.hasNext()) {
            String s = i$.next();
            if (!target.equals(s)) {
                text2 = text2 + "import static " + s + ".*;\n";
            }
        }
        if (allTargets.size() > 1) {
            text2 = text2 + "\n";
        }
        this.infoMap.put(new Info().textBefore(text2 + "public class " + target.substring(n + 1) + " extends " + cls.getCanonicalName() + " "));
        File targetFile = new File(outputDirectory, target.replace('.', '/') + ".java");
        System.out.println("Targeting file: " + targetFile);
        Iterator i$2 = allFiles.iterator();
        while (i$2.hasNext()) {
            File f = i$2.next();
            if (!clsFiles.contains(f)) {
                parse((File) null, f);
            }
        }
        parse(targetFile, (File[]) clsFiles.toArray(new File[clsFiles.size()]));
        return targetFile;
    }
}
