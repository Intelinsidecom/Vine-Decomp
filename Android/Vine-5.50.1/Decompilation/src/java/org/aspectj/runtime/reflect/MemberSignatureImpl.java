package org.aspectj.runtime.reflect;

import org.aspectj.lang.reflect.MemberSignature;

/* loaded from: classes2.dex */
abstract class MemberSignatureImpl extends SignatureImpl implements MemberSignature {
    MemberSignatureImpl(int modifiers, String name, Class declaringType) {
        super(modifiers, name, declaringType);
    }
}
