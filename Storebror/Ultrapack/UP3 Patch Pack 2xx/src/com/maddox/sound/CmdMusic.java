package com.maddox.sound;

import java.io.File;
import java.io.FileFilter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Stack;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.rts.CfgTools;
import com.maddox.rts.Cmd;
import com.maddox.rts.CmdEnv;

public class CmdMusic extends Cmd {
// private static final boolean _debug = false;
    protected static SoundFX   musFX;
    protected static String    fileName;
    protected static String    pathRandName;
    protected static float     vol;
    protected static Random    rnd;
    protected static Stack     stack;
    protected static boolean   bPlay;
    protected static boolean   bPlaying;
    protected static boolean   bList;
    public static final String PATH  = "PATH";
    public static final String RAND  = "RAND";
    public static final String FILE  = "FILE";
    public static final String LIST  = "LIST";
    public static final String PLAY  = "PLAY";
    public static final String STOP  = "STOP";
    public static final String BREAK = "BREAK";
    public static final String VOL   = "VOL";
    public static final String PUSH  = "PUSH";
    public static final String POP   = "POP";
    public static final String APPLY = "APPLY";
    private static AudioStream prevMusic;
    private static AudioStream curMusic;
    private static float       faderIncr;
    private static float       fader;
    private static String[]    list;
    private static int         index;

    public CmdMusic() {
        this.param.put("PATH", null);
        this.param.put("RAND", null);
        this.param.put("FILE", null);
        this.param.put("LIST", null);
        this.param.put("PLAY", null);
        this.param.put("STOP", null);
        this.param.put("BREAK", null);
        this.param.put("VOL", null);
        this.param.put("PUSH", null);
        this.param.put("POP", null);
        this.param.put("APPLY", null);
        this._properties.put("NAME", "music");
        this._levelAccess = 1;
        if (CmdMusic.musFX == null) {
            CmdMusic.musFX = new SoundFX("cmdmusic");
        }
    }

    private static void setFile(final String fileName, final boolean b) {
        if ((CmdMusic.fileName != null) && (fileName != null) && (CmdMusic.fileName.compareToIgnoreCase(fileName) == 0)) {
            return;
        }
        CmdMusic.fileName = fileName;
        CmdMusic.bPlay = true;
        CmdMusic.bPlaying = false;
        if (CmdMusic.prevMusic != null) {
            CmdMusic.prevMusic.cancel();
            CmdMusic.prevMusic.release();
            CmdMusic.prevMusic = null;
        }
        if (CmdMusic.fileName == null) {
            if (CmdMusic.curMusic != null) {
                CmdMusic.curMusic.cancel();
                CmdMusic.curMusic.release();
                CmdMusic.curMusic = null;
            }
            CmdMusic.musFX.cancel();
        } else {
            CmdMusic.bPlaying = false;
            if (CmdMusic.musFX != null) {
                Sample sample;
                if (CmdMusic.bList) {
                    sample = new Sample((String) null, CmdMusic.fileName);
                } else {
                    sample = new Sample(CmdMusic.fileName);
                }
                if (!sample.exists()) {
                    System.out.println("The sample " + CmdMusic.fileName + " not found");
                    CmdMusic.list = null;
                    return;
                }
                sample.setInfinite(!b);
                CmdMusic.prevMusic = CmdMusic.curMusic;
                CmdMusic.fader = 1.0f;
                (CmdMusic.curMusic = sample.get()).setVolume(1.0f);
                CmdMusic.curMusic.play();
                CmdMusic.musFX.add(CmdMusic.curMusic);
                CmdMusic.musFX.play();
                if (CmdMusic.prevMusic != null) {
                    CmdMusic.prevMusic.stop();
                }
                CmdMusic.bPlaying = true;
                apply();
            }
        }
    }

    public static void tick() {
        if (CmdMusic.prevMusic != null) {
            if (CmdMusic.fader > CmdMusic.faderIncr) {
                CmdMusic.fader -= CmdMusic.faderIncr;
                CmdMusic.prevMusic.setVolume(CmdMusic.fader);
            } else {
                CmdMusic.prevMusic.cancel();
                CmdMusic.prevMusic.release();
                CmdMusic.prevMusic = null;
            }
        }
        if (CmdMusic.list != null) {
            if ((CmdMusic.curMusic != null) && !CmdMusic.curMusic.isPlaying() && playState()) {
                CmdMusic.curMusic.cancel();
                CmdMusic.curMusic.release();
                CmdMusic.curMusic = null;
            }
            if ((CmdMusic.curMusic == null) && playState()) {
                ++CmdMusic.index;
                if (CmdMusic.index >= CmdMusic.list.length) {
                    CmdMusic.index = 0;
                }
                CmdMusic.fileName = null;
                setFile(CmdMusic.list[CmdMusic.index], true);
            }
        }
    }

    public static boolean isPathEnabled(final String s) {
        if (s == null) {
            return true;
        }
        int n;
        if (s.compareToIgnoreCase("music/takeoff") == 0) {
            n = 0;
        } else if (s.compareToIgnoreCase("music/inflight") == 0) {
            n = 1;
        } else {
            if (s.compareToIgnoreCase("music/crash") != 0) {
                return true;
            }
            n = 2;
        }
        final SoundFlags soundFlags = (SoundFlags) CfgTools.get("MusState");
        return (soundFlags != null) && soundFlags.get(n);
    }

    public static void setPath(final String pathRandName, final boolean b) {
        if (pathRandName == null) {
            return;
        }
        if (b && (CmdMusic.pathRandName != null) && (pathRandName.compareToIgnoreCase(CmdMusic.pathRandName) == 0)) {
            return;
        }
        final File[] listFiles = new File("./samples/" + pathRandName).listFiles(new WFileFilter());
        if ((listFiles == null) || (listFiles.length < 1)) {
            System.out.println("warning: no files : " + pathRandName);
            CmdMusic.pathRandName = null;
            CmdMusic.list = null;
            setFile(null, true);
            return;
        }
        CmdMusic.pathRandName = pathRandName;
        if (listFiles.length == 1) {
            final StringBuffer sb = new StringBuffer();
            sb.append(pathRandName);
            sb.append("/");
            sb.append(listFiles[0].getName());
            if (sb.length() < 5) {
                System.out.println("ERROR: invalid filename : " + sb);
            } else {
                setFile(sb.toString(), false);
            }
            CmdMusic.list = null;
        }
        // TODO skylla: Randomize Radio input.
        else {
            CmdMusic.list = new String[listFiles.length];
            ArrayList num = new ArrayList();
            for (int i = 0; i < listFiles.length; i++) {
                final StringBuffer sb2 = new StringBuffer();
                sb2.append(pathRandName);
                sb2.append("/");
                // old:
                // sb2.append(listFiles[i].getName());
                // new
                sb2.append(listFiles[getRandomInt(num, 0, listFiles.length)].getName());
                if (sb2.length() < 5) {
                    System.out.println("ERROR: invalid filename : " + sb2);
                    CmdMusic.list = null;
                    return;
                }
                CmdMusic.list[i] = sb2.toString();
            }
            CmdMusic.index = 0;
            setFile(CmdMusic.list[0], true);
            // System.out.println("SKYLLA RANDOM RADIO DEBUG:"
            // + "\nTotal number of files in folder: " + listFiles.length
            // + "\nRandomly listed as: " + num);
        }
    }

    private static int getRandomInt(ArrayList list, int neglim, int poslim) {
        if (neglim >= poslim) {
            throw new IllegalArgumentException("positive limit must be at least 1 greater than negative limit!");
        }
        SecureRandom secRandom = new SecureRandom();
        secRandom.setSeed(System.currentTimeMillis());
        RangeRandom rr = new RangeRandom(secRandom.nextLong());
        int ran = Math.round((rr.nextFloat(0.0F, 1.0F) * (poslim - 1 - neglim)) + neglim);
        for (int i = 0; i < list.size(); i++) {
            if (ran == Integer.parseInt((String) (list.get(i)))) {
                return getRandomInt(list, neglim, poslim);
            }
        }
        list.add(ran + "");
        return ran;
    }

    public static void setList(final Map map) {
        if (Cmd.nargs(map, "LIST") == 0) {
            return;
        }
        CmdMusic.list = new String[Cmd.nargs(map, "LIST")];
        for (int i = 0; i < CmdMusic.list.length; ++i) {
            CmdMusic.list[i] = Cmd.arg(map, "LIST", i);
        }
        CmdMusic.index = 0;
        setFile(CmdMusic.list[0], true);
    }

    public static void setVolume(final float vol) {
        CmdMusic.vol = vol;
    }

    public static void setCurrentVolume(final float volume) {
        CmdMusic.musFX.setVolume(volume);
    }

    public static boolean playState() {
        if (!isPathEnabled(CmdMusic.pathRandName)) {
            return false;
        }
        final SoundFlags soundFlags = (SoundFlags) CfgTools.get("MusFlags");
        return (soundFlags != null) && soundFlags.get(0);
    }

    public static void play() {
        CmdMusic.bPlay = true;
        apply();
    }

    public static void stop() {
        CmdMusic.bPlay = false;
        apply();
    }

    public static void apply() {
        if (playState() && CmdMusic.bPlay) {
            if (!CmdMusic.bPlaying) {
                if (CmdMusic.musFX != null) {
                    CmdMusic.musFX.play();
                }
                CmdMusic.bPlaying = true;
            }
        } else if (CmdMusic.bPlaying) {
            if (CmdMusic.musFX != null) {
                CmdMusic.musFX.cancel();
            }
            CmdMusic.bPlaying = false;
        }
    }

    public static void cancel() {
        if (CmdMusic.musFX != null) {
            CmdMusic.musFX.cancel();
        }
    }

    public static void push() {
        PlayInfo playInfo;
        if (CmdMusic.bList) {
            playInfo = new PlayInfo(CmdMusic.list);
        } else {
            playInfo = new PlayInfo((CmdMusic.pathRandName == null) ? CmdMusic.fileName : CmdMusic.pathRandName, CmdMusic.pathRandName != null);
        }
        CmdMusic.stack.push(playInfo);
    }

    public static void pop() {
        PlayInfo playInfo = null;
        if (CmdMusic.stack.empty()) {
            System.out.println("ERROR: stack is empty.");
        } else {
            playInfo = (PlayInfo) CmdMusic.stack.pop();
        }
        if (playInfo != null) {
            if (playInfo.list != null) {
                CmdMusic.bList = true;
                CmdMusic.list = playInfo.list;
                CmdMusic.index = 0;
                setFile(CmdMusic.list[0], true);
            } else if (playInfo.isPath) {
                CmdMusic.bList = false;
                CmdMusic.list = null;
                setPath(playInfo.objName, true);
            } else {
                CmdMusic.bList = false;
                CmdMusic.pathRandName = null;
                CmdMusic.list = null;
                setFile(playInfo.objName, false);
            }
        }
    }

    public Object exec(final CmdEnv cmdEnv, final Map list) {
        if (list.isEmpty()) {
            System.out.println("  music  : " + CmdMusic.fileName);
            System.out.println("  volume : " + CmdMusic.vol);
            System.out.println("  state  : " + (CmdMusic.bPlaying ? "PLAYING" : "STOPPED"));
        } else if (list.containsKey("_$$")) {
            System.out.println("Unknown command :" + Cmd.arg(list, "_$$", 0));
        } else {
            if (list.containsKey("PLAY")) {
                play();
            }
            if (list.containsKey("STOP")) {
                stop();
            }
            if (list.containsKey("BREAK")) {
                cancel();
            }
            if (list.containsKey("PUSH")) {
                push();
            }
            if (list.containsKey("POP")) {
                pop();
            }
            if (list.containsKey("APPLY")) {
                apply();
            }
            if (list.containsKey("PATH")) {
                if (Cmd.arg(list, "PATH", 0) == null) {
                    System.out.println("ERROR: path name expected");
                } else {
                    CmdMusic.bList = false;
                    setPath(Cmd.arg(list, "PATH", 0), false);
                }
            } else if (list.containsKey("RAND")) {
                if (Cmd.arg(list, "RAND", 0) == null) {
                    System.out.println("ERROR: path name expected");
                } else {
                    CmdMusic.bList = false;
                    setPath(Cmd.arg(list, "RAND", 0), true);
                }
            } else if (list.containsKey("FILE")) {
                if (Cmd.arg(list, "FILE", 0) == null) {
                    System.out.println("ERROR: file name expected");
                } else {
                    CmdMusic.pathRandName = null;
                    CmdMusic.bList = false;
                    setFile(Cmd.arg(list, "FILE", 0) + ".wav", false);
                }
            } else if (list.containsKey("LIST")) {
                if (Cmd.arg(list, "LIST", 0) == null) {
                    System.out.println("ERROR: list names expected");
                } else {
                    CmdMusic.bList = true;
                    setList(list);
                }
            }
            if (list.containsKey("VOL")) {
                final String arg = Cmd.arg(list, "PLAY", 0);
                if (arg == null) {
                    System.out.println("ERROR: volume gain (0..1) expected");
                } else {
                    final float float1 = Float.parseFloat(arg);
                    if ((float1 < 0.0f) || (float1 > 1.0f)) {
                        System.out.println("ERROR: value must be between 0.0 - 1.0");
                    } else {
                        setVolume(float1);
                    }
                }
            }
        }
        return CmdEnv.RETURN_OK;
    }

    static {
        CmdMusic.musFX = null;
        CmdMusic.fileName = null;
        CmdMusic.pathRandName = null;
        CmdMusic.vol = 1.0f;
        CmdMusic.rnd = new Random();
        CmdMusic.stack = new Stack();
        CmdMusic.bPlay = false;
        CmdMusic.bPlaying = false;
        CmdMusic.bList = false;
        CmdMusic.prevMusic = null;
        CmdMusic.curMusic = null;
        CmdMusic.faderIncr = 0.02f;
        CmdMusic.fader = 1.0f;
        CmdMusic.list = null;
        CmdMusic.index = 0;
    }

    static class WFileFilter implements FileFilter {
        public boolean accept(final File file) {
            if (!file.isFile() || file.isHidden()) {
                return false;
            }
            final String name = file.getName();
            if (name == null) {
                return false;
            }
            final int length = name.length();
            if (length < 5) {
                return false;
            }
            final String substring = name.substring(length - 3);
            return ((substring.compareToIgnoreCase("wav") == 0) || (substring.compareToIgnoreCase("mp3") == 0)) && (name.compareToIgnoreCase("empty") != 0);
        }
    }

    static class PlayInfo {
        public String   objName;
        public boolean  isPath;
        public String[] list;

        public PlayInfo(final String objName, final boolean isPath) {
            this.objName = objName;
            this.isPath = isPath;
            this.list = null;
        }

        public PlayInfo(final String[] list) {
            this.objName = null;
            this.isPath = false;
            this.list = list;
        }
    }
}
