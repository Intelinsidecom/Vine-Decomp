package com.googlecode.mp4parser.boxes.mp4.objectdescriptors;

import com.coremedia.iso.Hex;
import com.googlecode.javacv.cpp.avcodec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Logger;
import twitter4j.internal.http.HttpResponseCode;

@Descriptor(tags = {19, 106, 107, 108, 109, 110, 111, 112, avcodec.AV_CODEC_ID_INDEO5, avcodec.AV_CODEC_ID_MIMIC, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, avcodec.AV_CODEC_ID_BINKVIDEO, avcodec.AV_CODEC_ID_IFF_ILBM, avcodec.AV_CODEC_ID_IFF_BYTERUN1, avcodec.AV_CODEC_ID_KGV1, avcodec.AV_CODEC_ID_YOP, avcodec.AV_CODEC_ID_VP8, avcodec.AV_CODEC_ID_PICTOR, avcodec.AV_CODEC_ID_ANSI, 144, avcodec.AV_CODEC_ID_A64_MULTI5, avcodec.AV_CODEC_ID_R10K, avcodec.AV_CODEC_ID_MXPEG, avcodec.AV_CODEC_ID_LAGARITH, avcodec.AV_CODEC_ID_PRORES, avcodec.AV_CODEC_ID_JV, avcodec.AV_CODEC_ID_DFA, avcodec.AV_CODEC_ID_WMV3IMAGE, avcodec.AV_CODEC_ID_VC1IMAGE, avcodec.AV_CODEC_ID_UTVIDEO, avcodec.AV_CODEC_ID_BMV_VIDEO, avcodec.AV_CODEC_ID_VBLE, avcodec.AV_CODEC_ID_DXTORY, avcodec.AV_CODEC_ID_V410, avcodec.AV_CODEC_ID_XWD, avcodec.AV_CODEC_ID_CDXL, avcodec.AV_CODEC_ID_XBM, avcodec.AV_CODEC_ID_ZEROCODEC, avcodec.AV_CODEC_ID_MSS1, avcodec.AV_CODEC_ID_MSA1, avcodec.AV_CODEC_ID_TSCC2, avcodec.AV_CODEC_ID_MTS2, avcodec.AV_CODEC_ID_CLLC, avcodec.AV_CODEC_ID_MSS2, avcodec.AV_CODEC_ID_VP9, avcodec.AV_CODEC_ID_AIC, avcodec.AV_CODEC_ID_ESCAPE130_DEPRECATED, avcodec.AV_CODEC_ID_G2M_DEPRECATED, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, HttpResponseCode.OK, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, avcodec.AVCodecContext.FF_PROFILE_H264_HIGH_444_PREDICTIVE, 245, 246, 247, 248, 249, 250, 251, 252, 253})
/* loaded from: classes.dex */
public class ExtensionDescriptor extends BaseDescriptor {
    private static Logger log = Logger.getLogger(ExtensionDescriptor.class.getName());
    byte[] bytes;

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public void parseDetail(ByteBuffer bb) throws IOException {
        if (getSize() > 0) {
            this.bytes = new byte[this.sizeOfInstance];
            bb.get(this.bytes);
        }
    }

    @Override // com.googlecode.mp4parser.boxes.mp4.objectdescriptors.BaseDescriptor
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ExtensionDescriptor");
        sb.append("{bytes=").append(this.bytes == null ? "null" : Hex.encodeHex(this.bytes));
        sb.append('}');
        return sb.toString();
    }
}
