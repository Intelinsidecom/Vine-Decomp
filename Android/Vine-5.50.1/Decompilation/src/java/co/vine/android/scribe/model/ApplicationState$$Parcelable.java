package co.vine.android.scribe.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Iterator;
import org.parceler.ParcelWrapper;

/* loaded from: classes.dex */
public class ApplicationState$$Parcelable implements Parcelable, ParcelWrapper<ApplicationState> {
    public static final ApplicationState$$Parcelable$Creator$$14 CREATOR = new ApplicationState$$Parcelable$Creator$$14();
    private ApplicationState applicationState$$6;

    public ApplicationState$$Parcelable(Parcel parcel$$202) {
        ApplicationState applicationState$$8;
        if (parcel$$202.readInt() == -1) {
            applicationState$$8 = null;
        } else {
            applicationState$$8 = readco_vine_android_scribe_model_ApplicationState(parcel$$202);
        }
        this.applicationState$$6 = applicationState$$8;
    }

    public ApplicationState$$Parcelable(ApplicationState applicationState$$10) {
        this.applicationState$$6 = applicationState$$10;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel$$203, int flags) {
        if (this.applicationState$$6 == null) {
            parcel$$203.writeInt(-1);
        } else {
            parcel$$203.writeInt(1);
            writeco_vine_android_scribe_model_ApplicationState(this.applicationState$$6, parcel$$203, flags);
        }
    }

    private ApplicationState readco_vine_android_scribe_model_ApplicationState(Parcel parcel$$204) {
        ExperimentData experimentData$$12;
        Boolean boolean$$27;
        Boolean boolean$$28;
        Double double$$32;
        Long long$$51;
        Long long$$52;
        Long long$$53;
        ApplicationState applicationState$$7 = new ApplicationState();
        if (parcel$$204.readInt() == -1) {
            experimentData$$12 = null;
        } else {
            experimentData$$12 = readco_vine_android_scribe_model_ExperimentData(parcel$$204);
        }
        applicationState$$7.activeExperiments = experimentData$$12;
        int int$$168 = parcel$$204.readInt();
        if (int$$168 < 0) {
            boolean$$27 = null;
        } else {
            boolean$$27 = Boolean.valueOf(parcel$$204.readInt() == 1);
        }
        applicationState$$7.twitterConnected = boolean$$27;
        applicationState$$7.applicationStatus = parcel$$204.readString();
        int int$$169 = parcel$$204.readInt();
        if (int$$169 < 0) {
            boolean$$28 = null;
        } else {
            boolean$$28 = Boolean.valueOf(parcel$$204.readInt() == 1);
        }
        applicationState$$7.facebookConnected = boolean$$28;
        int int$$170 = parcel$$204.readInt();
        if (int$$170 < 0) {
            double$$32 = null;
        } else {
            double$$32 = Double.valueOf(parcel$$204.readDouble());
        }
        applicationState$$7.lastLaunchTimestamp = double$$32;
        applicationState$$7.edition = parcel$$204.readString();
        applicationState$$7.abConnected = parcel$$204.readInt() == 1;
        int int$$171 = parcel$$204.readInt();
        if (int$$171 < 0) {
            long$$51 = null;
        } else {
            long$$51 = Long.valueOf(parcel$$204.readLong());
        }
        applicationState$$7.videoCacheSize = long$$51;
        int int$$172 = parcel$$204.readInt();
        if (int$$172 < 0) {
            long$$52 = null;
        } else {
            long$$52 = Long.valueOf(parcel$$204.readLong());
        }
        applicationState$$7.loggedInUserId = long$$52;
        int int$$173 = parcel$$204.readInt();
        if (int$$173 < 0) {
            long$$53 = null;
        } else {
            long$$53 = Long.valueOf(parcel$$204.readLong());
        }
        applicationState$$7.numDrafts = long$$53;
        return applicationState$$7;
    }

    private ExperimentData readco_vine_android_scribe_model_ExperimentData(Parcel parcel$$205) {
        ArrayList<ExperimentValue> list$$18;
        ExperimentValue experimentValue$$18;
        ExperimentData experimentData$$11 = new ExperimentData();
        int int$$166 = parcel$$205.readInt();
        if (int$$166 < 0) {
            list$$18 = null;
        } else {
            list$$18 = new ArrayList<>();
            for (int int$$167 = 0; int$$167 < int$$166; int$$167++) {
                if (parcel$$205.readInt() == -1) {
                    experimentValue$$18 = null;
                } else {
                    experimentValue$$18 = readco_vine_android_scribe_model_ExperimentValue(parcel$$205);
                }
                list$$18.add(experimentValue$$18);
            }
        }
        experimentData$$11.experimentValues = list$$18;
        return experimentData$$11;
    }

    private ExperimentValue readco_vine_android_scribe_model_ExperimentValue(Parcel parcel$$206) {
        ExperimentValue experimentValue$$17 = new ExperimentValue();
        experimentValue$$17.value = parcel$$206.readString();
        experimentValue$$17.key = parcel$$206.readString();
        return experimentValue$$17;
    }

    private void writeco_vine_android_scribe_model_ApplicationState(ApplicationState applicationState$$9, Parcel parcel$$207, int flags$$80) {
        if (applicationState$$9.activeExperiments == null) {
            parcel$$207.writeInt(-1);
        } else {
            parcel$$207.writeInt(1);
            writeco_vine_android_scribe_model_ExperimentData(applicationState$$9.activeExperiments, parcel$$207, flags$$80);
        }
        if (applicationState$$9.twitterConnected == null) {
            parcel$$207.writeInt(-1);
        } else {
            parcel$$207.writeInt(1);
            parcel$$207.writeInt(applicationState$$9.twitterConnected.booleanValue() ? 1 : 0);
        }
        parcel$$207.writeString(applicationState$$9.applicationStatus);
        if (applicationState$$9.facebookConnected == null) {
            parcel$$207.writeInt(-1);
        } else {
            parcel$$207.writeInt(1);
            parcel$$207.writeInt(applicationState$$9.facebookConnected.booleanValue() ? 1 : 0);
        }
        if (applicationState$$9.lastLaunchTimestamp == null) {
            parcel$$207.writeInt(-1);
        } else {
            parcel$$207.writeInt(1);
            parcel$$207.writeDouble(applicationState$$9.lastLaunchTimestamp.doubleValue());
        }
        parcel$$207.writeString(applicationState$$9.edition);
        parcel$$207.writeInt(applicationState$$9.abConnected ? 1 : 0);
        if (applicationState$$9.videoCacheSize == null) {
            parcel$$207.writeInt(-1);
        } else {
            parcel$$207.writeInt(1);
            parcel$$207.writeLong(applicationState$$9.videoCacheSize.longValue());
        }
        if (applicationState$$9.loggedInUserId == null) {
            parcel$$207.writeInt(-1);
        } else {
            parcel$$207.writeInt(1);
            parcel$$207.writeLong(applicationState$$9.loggedInUserId.longValue());
        }
        if (applicationState$$9.numDrafts == null) {
            parcel$$207.writeInt(-1);
        } else {
            parcel$$207.writeInt(1);
            parcel$$207.writeLong(applicationState$$9.numDrafts.longValue());
        }
    }

    private void writeco_vine_android_scribe_model_ExperimentData(ExperimentData experimentData$$13, Parcel parcel$$208, int flags$$81) {
        if (experimentData$$13.experimentValues == null) {
            parcel$$208.writeInt(-1);
            return;
        }
        parcel$$208.writeInt(experimentData$$13.experimentValues.size());
        Iterator<ExperimentValue> it = experimentData$$13.experimentValues.iterator();
        while (it.hasNext()) {
            ExperimentValue experimentValue$$19 = it.next();
            if (experimentValue$$19 == null) {
                parcel$$208.writeInt(-1);
            } else {
                parcel$$208.writeInt(1);
                writeco_vine_android_scribe_model_ExperimentValue(experimentValue$$19, parcel$$208, flags$$81);
            }
        }
    }

    private void writeco_vine_android_scribe_model_ExperimentValue(ExperimentValue experimentValue$$20, Parcel parcel$$209, int flags$$82) {
        parcel$$209.writeString(experimentValue$$20.value);
        parcel$$209.writeString(experimentValue$$20.key);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.parceler.ParcelWrapper
    public ApplicationState getParcel() {
        return this.applicationState$$6;
    }
}
