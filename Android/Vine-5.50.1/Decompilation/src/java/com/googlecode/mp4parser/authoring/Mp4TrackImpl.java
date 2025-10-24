package com.googlecode.mp4parser.authoring;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.CompositionTimeToSample;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.EditListBox;
import com.coremedia.iso.boxes.MediaHeaderBox;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.coremedia.iso.boxes.SampleDependencyTypeBox;
import com.coremedia.iso.boxes.SampleDescriptionBox;
import com.coremedia.iso.boxes.SampleTableBox;
import com.coremedia.iso.boxes.SubSampleInformationBox;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.TrackHeaderBox;
import com.coremedia.iso.boxes.fragment.MovieExtendsBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.SampleFlags;
import com.coremedia.iso.boxes.fragment.TrackExtendsBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.coremedia.iso.boxes.mdat.SampleList;
import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.GroupEntry;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleGroupDescriptionBox;
import com.googlecode.mp4parser.boxes.mp4.samplegrouping.SampleToGroupBox;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Path;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class Mp4TrackImpl extends AbstractTrack {
    private List<CompositionTimeToSample.Entry> compositionTimeEntries;
    private long[] decodingTimes;
    IsoFile[] fragments;
    private String handler;
    private List<SampleDependencyTypeBox.Entry> sampleDependencies;
    private SampleDescriptionBox sampleDescriptionBox;
    private List<Sample> samples;
    private SubSampleInformationBox subSampleInformationBox;
    private long[] syncSamples;
    TrackBox trackBox;
    private TrackMetaData trackMetaData;

    public Mp4TrackImpl(String name, TrackBox trackBox, IsoFile... fragments) {
        SampleFlags sampleFlags;
        super(name);
        this.syncSamples = new long[0];
        this.trackMetaData = new TrackMetaData();
        this.subSampleInformationBox = null;
        long trackId = trackBox.getTrackHeaderBox().getTrackId();
        this.samples = new SampleList(trackBox, fragments);
        SampleTableBox stbl = trackBox.getMediaBox().getMediaInformationBox().getSampleTableBox();
        this.handler = trackBox.getMediaBox().getHandlerBox().getHandlerType();
        List<TimeToSampleBox.Entry> decodingTimeEntries = new ArrayList<>();
        this.compositionTimeEntries = new ArrayList();
        this.sampleDependencies = new ArrayList();
        decodingTimeEntries.addAll(stbl.getTimeToSampleBox().getEntries());
        if (stbl.getCompositionTimeToSample() != null) {
            this.compositionTimeEntries.addAll(stbl.getCompositionTimeToSample().getEntries());
        }
        if (stbl.getSampleDependencyTypeBox() != null) {
            this.sampleDependencies.addAll(stbl.getSampleDependencyTypeBox().getEntries());
        }
        if (stbl.getSyncSampleBox() != null) {
            this.syncSamples = stbl.getSyncSampleBox().getSampleNumber();
        }
        this.subSampleInformationBox = (SubSampleInformationBox) Path.getPath(stbl, SubSampleInformationBox.TYPE);
        List<MovieFragmentBox> movieFragmentBoxes = new ArrayList<>();
        movieFragmentBoxes.addAll(((Box) trackBox.getParent()).getParent().getBoxes(MovieFragmentBox.class));
        for (IsoFile fragment : fragments) {
            movieFragmentBoxes.addAll(fragment.getBoxes(MovieFragmentBox.class));
        }
        this.sampleDescriptionBox = stbl.getSampleDescriptionBox();
        List<MovieExtendsBox> movieExtendsBoxes = trackBox.getParent().getBoxes(MovieExtendsBox.class);
        if (movieExtendsBoxes.size() > 0) {
            for (MovieExtendsBox mvex : movieExtendsBoxes) {
                List<TrackExtendsBox> trackExtendsBoxes = mvex.getBoxes(TrackExtendsBox.class);
                for (TrackExtendsBox trex : trackExtendsBoxes) {
                    if (trex.getTrackId() == trackId) {
                        List<SubSampleInformationBox> subss = Path.getPaths(((Box) trackBox.getParent()).getParent(), "/moof/traf/subs");
                        if (subss.size() > 0) {
                            this.subSampleInformationBox = new SubSampleInformationBox();
                        }
                        List<Long> syncSampleList = new LinkedList<>();
                        long sampleNumber = 1;
                        for (MovieFragmentBox movieFragmentBox : movieFragmentBoxes) {
                            List<TrackFragmentBox> trafs = movieFragmentBox.getBoxes(TrackFragmentBox.class);
                            for (TrackFragmentBox traf : trafs) {
                                if (traf.getTrackFragmentHeaderBox().getTrackId() == trackId) {
                                    SubSampleInformationBox subs = (SubSampleInformationBox) Path.getPath(traf, SubSampleInformationBox.TYPE);
                                    if (subs != null) {
                                        long difFromLastFragment = (sampleNumber - 0) - 1;
                                        for (SubSampleInformationBox.SubSampleEntry subSampleEntry : subs.getEntries()) {
                                            SubSampleInformationBox.SubSampleEntry se = new SubSampleInformationBox.SubSampleEntry();
                                            se.getSubsampleEntries().addAll(subSampleEntry.getSubsampleEntries());
                                            if (difFromLastFragment != 0) {
                                                se.setSampleDelta(subSampleEntry.getSampleDelta() + difFromLastFragment);
                                                difFromLastFragment = 0;
                                            } else {
                                                se.setSampleDelta(subSampleEntry.getSampleDelta());
                                            }
                                            this.subSampleInformationBox.getEntries().add(se);
                                        }
                                    }
                                    List<TrackRunBox> truns = traf.getBoxes(TrackRunBox.class);
                                    for (TrackRunBox trun : truns) {
                                        TrackFragmentHeaderBox tfhd = ((TrackFragmentBox) trun.getParent()).getTrackFragmentHeaderBox();
                                        boolean first = true;
                                        for (TrackRunBox.Entry entry : trun.getEntries()) {
                                            if (trun.isSampleDurationPresent()) {
                                                if (decodingTimeEntries.size() == 0 || decodingTimeEntries.get(decodingTimeEntries.size() - 1).getDelta() != entry.getSampleDuration()) {
                                                    decodingTimeEntries.add(new TimeToSampleBox.Entry(1L, entry.getSampleDuration()));
                                                } else {
                                                    TimeToSampleBox.Entry e = decodingTimeEntries.get(decodingTimeEntries.size() - 1);
                                                    e.setCount(e.getCount() + 1);
                                                }
                                            } else if (tfhd.hasDefaultSampleDuration()) {
                                                decodingTimeEntries.add(new TimeToSampleBox.Entry(1L, tfhd.getDefaultSampleDuration()));
                                            } else {
                                                decodingTimeEntries.add(new TimeToSampleBox.Entry(1L, trex.getDefaultSampleDuration()));
                                            }
                                            if (trun.isSampleCompositionTimeOffsetPresent()) {
                                                if (this.compositionTimeEntries.size() == 0 || this.compositionTimeEntries.get(this.compositionTimeEntries.size() - 1).getOffset() != entry.getSampleCompositionTimeOffset()) {
                                                    this.compositionTimeEntries.add(new CompositionTimeToSample.Entry(1, CastUtils.l2i(entry.getSampleCompositionTimeOffset())));
                                                } else {
                                                    CompositionTimeToSample.Entry e2 = this.compositionTimeEntries.get(this.compositionTimeEntries.size() - 1);
                                                    e2.setCount(e2.getCount() + 1);
                                                }
                                            }
                                            if (trun.isSampleFlagsPresent()) {
                                                sampleFlags = entry.getSampleFlags();
                                            } else if (first && trun.isFirstSampleFlagsPresent()) {
                                                sampleFlags = trun.getFirstSampleFlags();
                                            } else if (tfhd.hasDefaultSampleFlags()) {
                                                sampleFlags = tfhd.getDefaultSampleFlags();
                                            } else {
                                                sampleFlags = trex.getDefaultSampleFlags();
                                            }
                                            if (sampleFlags != null && !sampleFlags.isSampleIsDifferenceSample()) {
                                                syncSampleList.add(Long.valueOf(sampleNumber));
                                            }
                                            sampleNumber++;
                                            first = false;
                                        }
                                    }
                                }
                            }
                        }
                        long[] oldSS = this.syncSamples;
                        this.syncSamples = new long[this.syncSamples.length + syncSampleList.size()];
                        System.arraycopy(oldSS, 0, this.syncSamples, 0, oldSS.length);
                        int i = oldSS.length;
                        for (Long syncSampleNumber : syncSampleList) {
                            this.syncSamples[i] = syncSampleNumber.longValue();
                            i++;
                        }
                    }
                }
            }
            new ArrayList();
            new ArrayList();
            for (MovieFragmentBox movieFragmentBox2 : movieFragmentBoxes) {
                for (TrackFragmentBox traf2 : movieFragmentBox2.getBoxes(TrackFragmentBox.class)) {
                    if (traf2.getTrackFragmentHeaderBox().getTrackId() == trackId) {
                        this.sampleGroups = getSampleGroups(Path.getPaths((Container) traf2, SampleGroupDescriptionBox.TYPE), Path.getPaths((Container) traf2, SampleToGroupBox.TYPE), this.sampleGroups);
                    }
                }
            }
        } else {
            this.sampleGroups = getSampleGroups(stbl.getBoxes(SampleGroupDescriptionBox.class), stbl.getBoxes(SampleToGroupBox.class), this.sampleGroups);
        }
        this.decodingTimes = TimeToSampleBox.blowupTimeToSamples(decodingTimeEntries);
        MediaHeaderBox mdhd = trackBox.getMediaBox().getMediaHeaderBox();
        TrackHeaderBox tkhd = trackBox.getTrackHeaderBox();
        this.trackMetaData.setTrackId(tkhd.getTrackId());
        this.trackMetaData.setCreationTime(mdhd.getCreationTime());
        this.trackMetaData.setLanguage(mdhd.getLanguage());
        this.trackMetaData.setModificationTime(mdhd.getModificationTime());
        this.trackMetaData.setTimescale(mdhd.getTimescale());
        this.trackMetaData.setHeight(tkhd.getHeight());
        this.trackMetaData.setWidth(tkhd.getWidth());
        this.trackMetaData.setLayer(tkhd.getLayer());
        this.trackMetaData.setMatrix(tkhd.getMatrix());
        EditListBox elst = (EditListBox) Path.getPath(trackBox, "edts/elst");
        MovieHeaderBox mvhd = (MovieHeaderBox) Path.getPath(trackBox, "../mvhd");
        if (elst != null) {
            for (EditListBox.Entry e3 : elst.getEntries()) {
                this.edits.add(new Edit(e3.getMediaTime(), mdhd.getTimescale(), e3.getMediaRate(), e3.getSegmentDuration() / mvhd.getTimescale()));
            }
        }
    }

    private Map<GroupEntry, long[]> getSampleGroups(List<SampleGroupDescriptionBox> sgdbs, List<SampleToGroupBox> sbgps, Map<GroupEntry, long[]> sampleGroups) {
        for (SampleGroupDescriptionBox sgdb : sgdbs) {
            boolean found = false;
            for (SampleToGroupBox sbgp : sbgps) {
                if (sbgp.getGroupingType().equals(sgdb.getGroupEntries().get(0).getType())) {
                    found = true;
                    int sampleNum = 0;
                    for (SampleToGroupBox.Entry entry : sbgp.getEntries()) {
                        if (entry.getGroupDescriptionIndex() > 0) {
                            GroupEntry groupEntry = sgdb.getGroupEntries().get(entry.getGroupDescriptionIndex() - 1);
                            long[] samples = sampleGroups.get(groupEntry);
                            if (samples == null) {
                                samples = new long[0];
                            }
                            long[] nuSamples = new long[CastUtils.l2i(entry.getSampleCount()) + samples.length];
                            System.arraycopy(samples, 0, nuSamples, 0, samples.length);
                            for (int i = 0; i < entry.getSampleCount(); i++) {
                                nuSamples[samples.length + i] = sampleNum + i;
                            }
                            sampleGroups.put(groupEntry, nuSamples);
                        }
                        sampleNum = (int) (sampleNum + entry.getSampleCount());
                    }
                }
            }
            if (!found) {
                throw new RuntimeException("Could not find SampleToGroupBox for " + sgdb.getGroupEntries().get(0).getType() + ".");
            }
        }
        return sampleGroups;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Container c = this.trackBox.getParent();
        if (c instanceof BasicContainer) {
            ((BasicContainer) c).close();
        }
        for (IsoFile fragment : this.fragments) {
            fragment.close();
        }
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public List<Sample> getSamples() {
        return this.samples;
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public synchronized long[] getSampleDurations() {
        return this.decodingTimes;
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public SampleDescriptionBox getSampleDescriptionBox() {
        return this.sampleDescriptionBox;
    }

    @Override // com.googlecode.mp4parser.authoring.AbstractTrack, com.googlecode.mp4parser.authoring.Track
    public List<CompositionTimeToSample.Entry> getCompositionTimeEntries() {
        return this.compositionTimeEntries;
    }

    @Override // com.googlecode.mp4parser.authoring.AbstractTrack, com.googlecode.mp4parser.authoring.Track
    public long[] getSyncSamples() {
        if (this.syncSamples.length == this.samples.size()) {
            return null;
        }
        return this.syncSamples;
    }

    @Override // com.googlecode.mp4parser.authoring.AbstractTrack, com.googlecode.mp4parser.authoring.Track
    public List<SampleDependencyTypeBox.Entry> getSampleDependencies() {
        return this.sampleDependencies;
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public TrackMetaData getTrackMetaData() {
        return this.trackMetaData;
    }

    @Override // com.googlecode.mp4parser.authoring.Track
    public String getHandler() {
        return this.handler;
    }

    @Override // com.googlecode.mp4parser.authoring.AbstractTrack, com.googlecode.mp4parser.authoring.Track
    public SubSampleInformationBox getSubsampleInformationBox() {
        return this.subSampleInformationBox;
    }
}
