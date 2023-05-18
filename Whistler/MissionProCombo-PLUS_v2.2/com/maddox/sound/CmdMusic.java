package com.maddox.sound;

import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;
import java.io.*;
import java.util.*;

public class CmdMusic extends Cmd
{
    static class WFileFilter
        implements FileFilter
    {

        public boolean accept(File file)
        {
            if(!file.isFile() || file.isHidden())
                return false;
            String s = file.getName();
            if(s == null)
                return false;
            int i = s.length();
            if(i < 5)
                return false;
            String s1 = s.substring(i - 3);
            if(s1.compareToIgnoreCase("wav") != 0 && s1.compareToIgnoreCase("mp3") != 0)
                return false;
            else
                return s.compareToIgnoreCase("empty") != 0;
        }

        WFileFilter()
        {
        }
    }

    static class PlayInfo
    {

        public String objName;
        public boolean isPath;
        public String list[];

        public PlayInfo(String s, boolean flag)
        {
            objName = s;
            isPath = flag;
            list = null;
        }

        public PlayInfo(String as[])
        {
            objName = null;
            isPath = false;
            list = as;
        }
    }


    public CmdMusic()
    {
        param.put("PATH", null);
        param.put("RAND", null);
        param.put("FILE", null);
        param.put("LIST", null);
        param.put("PLAY", null);
        param.put("STOP", null);
        param.put("BREAK", null);
        param.put("VOL", null);
        param.put("PUSH", null);
        param.put("POP", null);
        param.put("APPLY", null);
        _properties.put("NAME", "music");
        _levelAccess = 1;
        if(musFX == null)
            musFX = new SoundFX("cmdmusic");
    }

    private static void setFile(String s, boolean flag)
    {
        if(fileName != null && s != null && fileName.compareToIgnoreCase(s) == 0)
            return;
        fileName = s;
        bPlay = true;
        bPlaying = false;
        if(prevMusic != null)
        {
            prevMusic.cancel();
            prevMusic.release();
            prevMusic = null;
        }
        if(fileName == null)
        {
            if(curMusic != null)
            {
                curMusic.cancel();
                curMusic.release();
                curMusic = null;
            }
            musFX.cancel();
        } else
        {
            bPlaying = false;
            if(musFX != null)
            {
                Sample sample = null;
                if(bList)
                    sample = new Sample((String)null, fileName);
                else
                    sample = new Sample(fileName);
                if(!sample.exists())
                {
                    System.out.println("The sample " + fileName + " not found");
                    sample = null;
                    list = null;
                    return;
                }
                sample.setInfinite(!flag);
                prevMusic = curMusic;
                fader = 1.0F;
                curMusic = sample.get();
                curMusic.setVolume(1.0F);
                curMusic.play();
                musFX.add(curMusic);
                musFX.play();
                if(prevMusic != null)
                    prevMusic.stop();
                bPlaying = true;
                apply();
            }
        }
    }

    public static void tick()
    {
        if(prevMusic != null)
            if(fader > faderIncr)
            {
                fader -= faderIncr;
                prevMusic.setVolume(fader);
            } else
            {
                prevMusic.cancel();
                prevMusic.release();
                prevMusic = null;
            }
        if(list != null)
        {
            if(curMusic != null && !curMusic.isPlaying() && playState())
            {
                curMusic.cancel();
                curMusic.release();
                curMusic = null;
            }
            if(curMusic == null && playState())
            {
                index++;
                if(index >= list.length)
                    index = 0;
                fileName = null;
                setFile(list[index], true);
            }
        }
    }

    public static boolean isPathEnabled(String s)
    {
        byte byte0 = -1;
        if(s == null)
            return true;
        if(s.compareToIgnoreCase("music/takeoff") == 0)
            byte0 = 0;
        else
        if(s.compareToIgnoreCase("music/inflight") == 0)
            byte0 = 1;
        else
        if(s.compareToIgnoreCase("music/crash") == 0)
            byte0 = 2;
        else
            return true;
        SoundFlags soundflags = (SoundFlags)CfgTools.get("MusState");
        if(soundflags == null)
            return false;
        else
            return soundflags.get(byte0);
    }

    public static void setPath(String s, boolean flag)
    {
        if(s == null)
            return;
        if(flag && pathRandName != null && s.compareToIgnoreCase(pathRandName) == 0)
            return;
        File file = new File("./samples/" + s);
        File afile[] = file.listFiles(new WFileFilter());
        if(afile == null || afile.length < 1)
        {
            System.out.println("warning: no files : " + s);
            pathRandName = null;
            list = null;
            setFile(null, true);
            return;
        }
        pathRandName = s;
        if(afile.length == 1)
        {
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append(s);
            stringbuffer.append("/");
            stringbuffer.append(afile[0].getName());
            int j = stringbuffer.length();
            if(j < 5)
                System.out.println("ERROR: invalid filename : " + stringbuffer);
            else
                setFile(stringbuffer.toString(), false);
            list = null;
        } else
        {
            list = new String[afile.length];
            for(int i = 0; i < afile.length; i++)
            {
                StringBuffer stringbuffer1 = new StringBuffer();
                stringbuffer1.append(s);
                stringbuffer1.append("/");
                stringbuffer1.append(afile[i].getName());
                int k = stringbuffer1.length();
                if(k < 5)
                {
                    System.out.println("ERROR: invalid filename : " + stringbuffer1);
                    list = null;
                    return;
                }
                list[i] = stringbuffer1.toString();
            }

            index = TrueRandom.nextInt(0, list.length);
            setFile(list[index], true);
        }
    }

    public static void setList(Map map)
    {
        if(Cmd.nargs(map, "LIST") == 0)
            return;
        list = new String[Cmd.nargs(map, "LIST")];
        for(int i = 0; i < list.length; i++)
            list[i] = Cmd.arg(map, "LIST", i);

        index = 0;
        setFile(list[0], true);
    }

    public static void setVolume(float f)
    {
        vol = f;
    }

    public static void setCurrentVolume(float f)
    {
        musFX.setVolume(f);
    }

    public static boolean playState()
    {
        if(!isPathEnabled(pathRandName))
            return false;
        if(pathRandName != null && pathRandName.toLowerCase().startsWith("music/radio"))
            return true;
        SoundFlags soundflags = (SoundFlags)CfgTools.get("MusFlags");
        if(soundflags == null)
            return false;
        else
            return soundflags.get(0);
    }

    public static void play()
    {
        bPlay = true;
        apply();
    }

    public static void stop()
    {
        bPlay = false;
        apply();
    }

    public static void apply()
    {
        if(playState() && bPlay)
        {
            if(!bPlaying)
            {
                if(musFX != null)
                    musFX.play();
                bPlaying = true;
            }
        } else
        if(bPlaying)
        {
            if(musFX != null)
                musFX.cancel();
            bPlaying = false;
        }
    }

    public static void cancel()
    {
        if(musFX != null)
            musFX.cancel();
    }

    public static void push()
    {
        PlayInfo playinfo = null;
        if(bList)
            playinfo = new PlayInfo(list);
        else
            playinfo = new PlayInfo(pathRandName == null ? fileName : pathRandName, pathRandName != null);
        stack.push(playinfo);
    }

    public static void pop()
    {
        PlayInfo playinfo = null;
        if(stack.empty())
            System.out.println("ERROR: stack is empty.");
        else
            playinfo = (PlayInfo)stack.pop();
        if(playinfo != null)
            if(playinfo.list != null)
            {
                bList = true;
                list = playinfo.list;
                index = 0;
                setFile(list[0], true);
            } else
            if(playinfo.isPath)
            {
                bList = false;
                list = null;
                setPath(playinfo.objName, true);
            } else
            {
                bList = false;
                pathRandName = null;
                list = null;
                setFile(playinfo.objName, false);
            }
    }

    public Object exec(CmdEnv cmdenv, Map map)
    {
        boolean flag = false;
        if(map.isEmpty())
        {
            System.out.println("  music  : " + fileName);
            System.out.println("  volume : " + vol);
            System.out.println("  state  : " + (bPlaying ? "PLAYING" : "STOPPED"));
        } else
        if(map.containsKey("_$$"))
        {
            System.out.println("Unknown command :" + Cmd.arg(map, "_$$", 0));
        } else
        {
            if(map.containsKey("PLAY"))
                play();
            if(map.containsKey("STOP"))
                stop();
            if(map.containsKey("BREAK"))
                cancel();
            if(map.containsKey("PUSH"))
                push();
            if(map.containsKey("POP"))
                pop();
            if(map.containsKey("APPLY"))
                apply();
            if(map.containsKey("PATH"))
            {
                if(Cmd.arg(map, "PATH", 0) == null)
                {
                    System.out.println("ERROR: path name expected");
                } else
                {
                    bList = false;
                    setPath(Cmd.arg(map, "PATH", 0), false);
                }
            } else
            if(map.containsKey("RAND"))
            {
                if(Cmd.arg(map, "RAND", 0) == null)
                {
                    System.out.println("ERROR: path name expected");
                } else
                {
                    bList = false;
                    setPath(Cmd.arg(map, "RAND", 0), true);
                }
            } else
            if(map.containsKey("FILE"))
            {
                if(Cmd.arg(map, "FILE", 0) == null)
                {
                    System.out.println("ERROR: file name expected");
                } else
                {
                    pathRandName = null;
                    bList = false;
                    setFile(Cmd.arg(map, "FILE", 0) + ".wav", false);
                }
            } else
            if(map.containsKey("LIST"))
                if(Cmd.arg(map, "LIST", 0) == null)
                {
                    System.out.println("ERROR: list names expected");
                } else
                {
                    bList = true;
                    setList(map);
                }
            if(map.containsKey("VOL"))
            {
                String s = Cmd.arg(map, "PLAY", 0);
                if(s == null)
                {
                    System.out.println("ERROR: volume gain (0..1) expected");
                } else
                {
                    float f = Float.parseFloat(s);
                    if(f < 0.0F || f > 1.0F)
                        System.out.println("ERROR: value must be between 0.0 - 1.0");
                    else
                        setVolume(f);
                }
            }
        }
        return CmdEnv.RETURN_OK;
    }

    private static final boolean _debug = false;
    protected static SoundFX musFX = null;
    protected static String fileName = null;
    protected static String pathRandName = null;
    protected static float vol = 1.0F;
    protected static Random rnd = new Random();
    protected static Stack stack = new Stack();
    protected static boolean bPlay = false;
    protected static boolean bPlaying = false;
    protected static boolean bList = false;
    public static final String PATH = "PATH";
    public static final String RAND = "RAND";
    public static final String FILE = "FILE";
    public static final String LIST = "LIST";
    public static final String PLAY = "PLAY";
    public static final String STOP = "STOP";
    public static final String BREAK = "BREAK";
    public static final String VOL = "VOL";
    public static final String PUSH = "PUSH";
    public static final String POP = "POP";
    public static final String APPLY = "APPLY";
    private static AudioStream prevMusic = null;
    private static AudioStream curMusic = null;
    private static float faderIncr = 0.02F;
    private static float fader = 1.0F;
    private static String list[] = null;
    private static int index = 0;

}