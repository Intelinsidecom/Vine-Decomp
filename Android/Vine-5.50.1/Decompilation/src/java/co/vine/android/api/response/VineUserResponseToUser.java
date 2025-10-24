package co.vine.android.api.response;

import co.vine.android.api.VineUser;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;

/* loaded from: classes.dex */
public final class VineUserResponseToUser extends ParsingTypeConverter<VineUser> {
    @Override // com.bluelinelabs.logansquare.typeconverters.TypeConverter
    public VineUser parse(JsonParser jsonParser) throws IOException {
        VineUserResponse response = VineUserResponse$$JsonObjectMapper._parse(jsonParser);
        return VineUser.fromVineUserResponse(response);
    }
}
