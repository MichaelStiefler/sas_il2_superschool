package com.maddox.il2.objects.sounds;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.maddox.rts.HomePath;
import com.maddox.rts.Time;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;
import com.maddox.sound.SoundPreset;

public class VoiceFX {

    public VoiceFX() {
        this.time = 0L;
        this.voice = -1;
        this.armyNum = 0;
        this.country = null;
        this.phrase = null;
        this.subFX = new ArrayList();
    }

    protected void playVoice() {
        SoundFX soundfx = voices[this.voice];
        if (soundfx != null) {
            for (int i = 0; i < this.phrase.length; i++) {
                if (VoiceBase.vbStr[this.phrase[i]] == null) {
                    continue;
                }
                String s = null;
                if ((this.country != null) && countryMap.containsKey(this.country)) {
                    s = this.country;
                } else {
                    switch (this.armyNum) {
                        case 1: // '\001'
                            s = "ru";
                            break;

                        case 2: // '\002'
                            s = "de";
                            break;

                        default:
                            return;
                    }
                }
                s = "Speech/" + s + "/Actor" + (this.voice + 1) + "/" + VoiceBase.vbStr[this.phrase[i]];
                soundfx.play(new Sample(s + ".wav", 1));
            }

        }
    }

    public static void tick() {
        if (!enabled) {
            return;
        }
        long l = Time.real();
//        prevVoice = -1;
//        curDt = 100;
        if ((curSync != null) && !voices[curSync.voice].isPlaying()) {
            long l1 = 100L;
            VoiceFX voicefx3;
            for (int i1 = -1; curSync.subFX.size() > 0; i1 = voicefx3.voice) {
                voicefx3 = (VoiceFX) curSync.subFX.get(0);
                if ((i1 >= 0) && (i1 != voicefx3.voice)) {
                    l1 = minDelay + rnd.nextInt(rndDelay);
                } else {
                    l1 += 200L;
                }
                voicefx3.time = l + l1;
                async.add(voicefx3);
                curSync.subFX.remove(0);
            }

            curSync = null;
            curFX = null;
        }
        if ((curSync == null) && (queue.size() > 0)) {
//            boolean flag = false;
            curSync = (VoiceFX) queue.get(0);
            curSync.playVoice();
            queue.remove(0);
            do {
                if (queue.size() <= 0) {
                    break;
                }
                VoiceFX voicefx = (VoiceFX) queue.get(0);
                if (voicefx.voice != curSync.voice) {
                    break;
                }
                voicefx.playVoice();
                for (; voicefx.subFX.size() > 0; voicefx.subFX.remove(0)) {
                    VoiceFX voicefx1 = (VoiceFX) voicefx.subFX.get(0);
                    curSync.subFX.add(voicefx1);
                }

                queue.remove(0);
            } while (true);
        }
        if (async.size() > 0) {
            int i = 0;
            for (int j = 0; j < 9; j++) {
                if (voices[j].isPlaying()) {
                    i++;
                }
            }

            int k = 0;
            label0:
            do {
                if (k >= async.size()) {
                    break;
                }
                VoiceFX voicefx2 = (VoiceFX) async.get(k);
                if (i > 2) {
                    break;
                }
                if (voicefx2.time < l) {
                    voicefx2.playVoice();
                    async.remove(k);
                    i++;
                    do {
                        if (async.size() <= k) {
                            continue label0;
                        }
                        VoiceFX voicefx4 = (VoiceFX) async.get(k);
                        if (voicefx2.voice != voicefx4.voice) {
                            continue label0;
                        }
                        voicefx4.playVoice();
                        async.remove(k);
                    } while (true);
                }
                k++;
            } while (true);
        }
    }

    public static void play(int i, int j, int k, String s, int ai[]) {
        if ((ai == null) || (ai.length == 0)) {
            return;
        }
        VoiceFX voicefx = new VoiceFX();
        j--;
        int l;
        for (l = 0; (l < ai.length) && (ai[l] != 0); l++) {
            ;
        }
        voicefx.voice = j;
        voicefx.armyNum = k;
        voicefx.country = s;
        if (voicefx.country != null) {
            voicefx.country = voicefx.country.toLowerCase();
        }
        voicefx.phrase = new int[l];
        for (int i1 = 0; i1 < l; i1++) {
            voicefx.phrase[i1] = ai[i1];
        }

        if (i == 1) {
            i = 0;
        }
        if (i == 2) {
            async.add(voicefx);
        } else if (i == 0) {
            curFX = voicefx;
            queue.add(voicefx);
        } else if (i == 1) {
            curFX.subFX.add(voicefx);
//        prevVoice = j;
        }
    }

    public static void end() {
        for (int i = 0; i < 9; i++) {
            if (voices[i] != null) {
                voices[i].cancel();
                voices[i].clear();
            }
        }

        queue.clear();
        async.clear();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean flag) {
        if (preset == null) {
            preset = new SoundPreset("voice");
        }
        enabled = flag;
        for (int i = 0; i < 9; i++) {
            if (enabled) {
                if (voices[i] == null) {
                    voices[i] = new SoundFX(preset);
                }
                continue;
            }
            if (voices[i] != null) {
                voices[i].cancel();
                voices[i] = null;
            }
        }

    }

    public static final int      numVoices = 9;
    protected long               time;
    protected int                voice;
    protected int                armyNum;
    protected String             country;
    protected int                phrase[];
    protected ArrayList          subFX;
    protected static SoundPreset preset    = null;
    protected static SoundFX     voices[];
    protected static ArrayList   queue     = new ArrayList();
    protected static ArrayList   async     = new ArrayList();
    protected static VoiceFX     curFX     = null;
    protected static VoiceFX     curSync   = null;
    protected static boolean     enabled   = false;
    private static HashMap       countryMap;
    private static Random        rnd       = new Random();
    private static final int     rndDelay  = 3500;
    private static final int     minDelay  = 1500;
//    private static int prevVoice = -1;
//    private static int curDt = 100;

    static {
        voices = new SoundFX[9];
        countryMap = new HashMap();
        for (int i = 0; i < 9; i++) {
            voices[i] = null;
        }

        countryMap.put("ru".intern(), null);
        countryMap.put("de".intern(), null);
        File file = new File(HomePath.get(0), "samples/speech");
        if (file != null) {
            File afile[] = file.listFiles();
            if (afile != null) {
                for (int j = 0; j < afile.length; j++) {
                    if (!afile[j].isDirectory() || afile[j].isHidden()) {
                        continue;
                    }
                    String s = afile[j].getName().toLowerCase();
                    if (!countryMap.containsKey(s)) {
                        countryMap.put(s.intern(), null);
                    }
                }

            }
        }
    }
}
