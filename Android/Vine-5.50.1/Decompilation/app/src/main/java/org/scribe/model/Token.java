package org.scribe.model;

import java.io.Serializable;
import org.scribe.utils.Preconditions;

/* loaded from: classes.dex */
public class Token implements Serializable {
    private static final long serialVersionUID = 715000866082812683L;
    private final String rawResponse;
    private final String secret;
    private final String token;

    public Token(String token, String secret) {
        this(token, secret, null);
    }

    public Token(String token, String secret, String rawResponse) {
        Preconditions.checkNotNull(token, "Token can't be null");
        Preconditions.checkNotNull(secret, "Secret can't be null");
        this.token = token;
        this.secret = secret;
        this.rawResponse = rawResponse;
    }

    public String getToken() {
        return this.token;
    }

    public String getSecret() {
        return this.secret;
    }

    public String toString() {
        return String.format("Token[%s , %s]", this.token, this.secret);
    }

    public boolean isEmpty() {
        return "".equals(this.token) && "".equals(this.secret);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token that = (Token) o;
        return this.token.equals(that.token) && this.secret.equals(that.secret);
    }

    public int hashCode() {
        return (this.token.hashCode() * 31) + this.secret.hashCode();
    }
}
