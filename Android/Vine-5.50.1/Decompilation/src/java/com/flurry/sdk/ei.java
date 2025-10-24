package com.flurry.sdk;

import com.flurry.sdk.ek;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes.dex */
public class ei<RequestObjectType, ResponseObjectType> extends ek {
    private a<RequestObjectType, ResponseObjectType> a;
    private RequestObjectType b;
    private ResponseObjectType c;
    private eu<RequestObjectType> d;
    private eu<ResponseObjectType> e;

    public interface a<RequestObjectType, ResponseObjectType> {
        void a(ei<RequestObjectType, ResponseObjectType> eiVar, ResponseObjectType responseobjecttype);
    }

    public void a(RequestObjectType requestobjecttype) {
        this.b = requestobjecttype;
    }

    public void a(eu<RequestObjectType> euVar) {
        this.d = euVar;
    }

    public void b(eu<ResponseObjectType> euVar) {
        this.e = euVar;
    }

    public void a(a<RequestObjectType, ResponseObjectType> aVar) {
        this.a = aVar;
    }

    @Override // com.flurry.sdk.ek, com.flurry.sdk.fc
    public void a() {
        m();
        super.a();
    }

    private void m() {
        a(new ek.c() { // from class: com.flurry.sdk.ei.1
            @Override // com.flurry.sdk.ek.c
            public void a(ek ekVar, OutputStream outputStream) throws Exception {
                if (ei.this.b != null && ei.this.d != null) {
                    ei.this.d.a(outputStream, ei.this.b);
                }
            }

            @Override // com.flurry.sdk.ek.c
            public void a(ek ekVar, InputStream inputStream) throws Exception {
                if (ekVar.d() && ei.this.e != null) {
                    ei.this.c = ei.this.e.a(inputStream);
                }
            }

            @Override // com.flurry.sdk.ek.c
            public void a(ek ekVar) {
                ei.this.n();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n() {
        if (this.a != null && !b()) {
            this.a.a(this, this.c);
        }
    }
}
