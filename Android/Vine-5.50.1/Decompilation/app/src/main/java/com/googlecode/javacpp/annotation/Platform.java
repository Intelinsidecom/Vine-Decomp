package com.googlecode.javacpp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: classes.dex */
public @interface Platform {
    String[] cinclude() default {};

    String[] define() default {};

    String[] framework() default {};

    String[] include() default {};

    String[] includepath() default {};

    String library() default "";

    String[] link() default {};

    String[] linkpath() default {};

    String[] not() default {};

    String[] options() default {};

    String[] preload() default {};

    String[] preloadpath() default {};

    String[] value() default {};
}
