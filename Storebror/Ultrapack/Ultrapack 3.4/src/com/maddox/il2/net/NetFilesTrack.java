package com.maddox.il2.net;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import com.maddox.il2.game.Mission;
import com.maddox.rts.Finger;
import com.maddox.rts.InOutStreams;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetObj;
import com.maddox.rts.net.NetFileRequest;
import com.maddox.rts.net.NetFileServerDef;
import com.maddox.util.HashMapExt;

public class NetFilesTrack {
    static class Entry {

        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof Entry)) return false;
            else return this.idServer == ((Entry) obj).idServer && this.idOwner == ((Entry) obj).idOwner && this.ownerFileName.equals(((Entry) obj).ownerFileName);
        }

        public int hashCode() {
            return this.hash;
        }

        public int    idServer;
        public int    idOwner;
        public String ownerFileName;
        public String iosFileName;
        public long   finger;
        private int   hash;

        public Entry(int i, int j, String s) {
            this.idServer = i;
            this.idOwner = j;
            this.ownerFileName = s;
            this.hash = i + j + Finger.Int(s);
        }
    }

    public NetFilesTrack() {
        this.entryMap = new HashMapExt();
        this.iosFileNum = 0;
    }

    public static void recordFile(NetFileServerDef netfileserverdef, NetUser netuser, String s, String s1) {
        if (!NetMissionTrack.isRecording()) return;
        else {
            NetMissionTrack.netFilesTrackRecording._recordFile(netfileserverdef, netuser, s, s1);
            return;
        }
    }

    public void _recordFile(NetFileServerDef netfileserverdef, NetUser netuser, String s, String s1) {
        Entry entry = new Entry(netfileserverdef.idLocal(), netuser.idLocal(), s);
        if (this.entryMap.containsKey(entry)) return;
        if (s.equalsIgnoreCase(s1)) s1 = netfileserverdef.primaryPath() + "/" + s1;
        else s1 = netfileserverdef.alternativePath() + "/" + s1;
        String s2 = "netFile" + this.iosFileNum++;
        long l = this.record(s1, s2);
        if (l != 0L) {
            entry.iosFileName = s2;
            entry.finger = l;
            this.entryMap.put(entry, null);
        }
    }

    private long record(String s, String s1) {
        try {
            FileInputStream fileinputstream = new FileInputStream(s);
            OutputStream outputstream = NetMissionTrack.io.createStream(s1);
            byte abyte0[] = new byte[1024];
            long l = 0L;
            do {
                int i = fileinputstream.available();
                if (i == 0) break;
                if (i >= abyte0.length) i = abyte0.length;
                else abyte0 = new byte[i];
                fileinputstream.read(abyte0, 0, i);
                l = Finger.incLong(l, abyte0);
                outputstream.write(abyte0, 0, i);
            } while (true);
            fileinputstream.close();
            outputstream.close();
            return l;
        } catch (FileNotFoundException fnfe) {
            if (s.toLowerCase().startsWith("paintschemes/skins/") || s.toLowerCase().startsWith("paintschemes/pilots/")) {
                System.out.println("Skin File \"" + s + "\" not available.");
            } else printDebug(fnfe);
        } catch (Exception e) {
            printDebug(e);
        }
        return 0L;
    }

    public void startRecording() {
        Mission.cur().recordNetFiles();
        if (!NetMissionTrack.isPlaying()) ((NetUser) NetEnv.host()).recordNetFiles();
        List list = NetEnv.hosts();
        int i = list.size();
        for (int j = 0; j < i; j++) {
            NetUser netuser = (NetUser) list.get(j);
            netuser.recordNetFiles();
        }

    }

    public void stopRecording() {
        try {
            PrintStream printstream = new PrintStream(NetMissionTrack.io.createStream("NetFilesDir"));
            for (java.util.Map.Entry entry = this.entryMap.nextEntry(null); entry != null; entry = this.entryMap.nextEntry(entry)) {
                Entry entry1 = (Entry) entry.getKey();
                printstream.println(entry1.idServer);
                printstream.println(entry1.idOwner);
                printstream.println(entry1.ownerFileName);
                printstream.println(entry1.iosFileName);
                printstream.println(entry1.finger);
            }

            printstream.close();
        } catch (Exception exception) {
            printDebug(exception);
        }
        this.entryMap.clear();
    }

    public static boolean existFile(NetFileRequest netfilerequest) {
        if (!NetMissionTrack.isPlaying()) return false;
        else return NetMissionTrack.netFilesTrackPlaying._existFile(netfilerequest);
    }

    public static String getLocalFileName(NetFileServerDef netfileserverdef, NetObj netobj, String s) {
        if (!NetMissionTrack.isPlaying()) return null;
        else return NetMissionTrack.netFilesTrackPlaying._getLocalFileName(netfileserverdef, netobj, s);
    }

    private String _getLocalFileName(NetFileServerDef netfileserverdef, NetObj netobj, String s) {
        Entry entry = new Entry(netfileserverdef.idLocal(), netobj.idRemote(), s);
        entry = (Entry) this.entryMap.get(entry);
        if (entry == null) return null;
        else return entry.finger + ".bmp";
    }

    public boolean _existFile(NetFileRequest netfilerequest) {
        Entry entry = new Entry(((NetFileServerDef) netfilerequest.server()).idLocal(), netfilerequest.owner().idRemote(), netfilerequest.ownerFileName());
        entry = (Entry) this.entryMap.get(entry);
        if (entry == null) return false;
        else {
            netfilerequest.setLocalFileName(entry.finger + ".bmp");
            return true;
        }
    }

    private boolean extract(InOutStreams inoutstreams, String s, String s1) {
        try {
            InputStream inputstream = inoutstreams.openStream(s);
            FileOutputStream fileoutputstream = new FileOutputStream(s1);
            byte abyte0[] = new byte[1024];
            do {
                int i = inputstream.available();
                if (i == 0) break;
                if (i > abyte0.length) i = abyte0.length;
                inputstream.read(abyte0, 0, i);
                fileoutputstream.write(abyte0, 0, i);
            } while (true);
            inputstream.close();
            fileoutputstream.close();
            return true;
        } catch (Exception localException) {
            printDebug(localException);
        }
        return false;
    }

    public void startPlaying(InOutStreams inoutstreams) {
        try {
            InputStream inputstream = inoutstreams.openStream("NetFilesDir");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            do {
                String s = bufferedreader.readLine();
                if (s == null || s.length() == 0) break;
                int i = Integer.parseInt(s);
                int j = Integer.parseInt(bufferedreader.readLine());
                String s1 = bufferedreader.readLine();
                String s2 = bufferedreader.readLine();
                long l = Long.parseLong(bufferedreader.readLine());
                NetFileServerDef netfileserverdef = (NetFileServerDef) NetEnv.cur().objects.get(i);
                if (this.extract(inoutstreams, s2, netfileserverdef.alternativePath() + "/" + l + ".bmp")) {
                    Entry entry = new Entry(i, j, s1);
                    entry.iosFileName = s2;
                    entry.finger = l;
                    this.entryMap.put(entry, entry);
                }
            } while (true);
            inputstream.close();
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    public void stopPlaying() {
        this.entryMap.clear();
    }

    static void printDebug(Exception exception) {
        System.out.println(exception.getMessage());
        exception.printStackTrace();
    }

    private HashMapExt entryMap;
    private int        iosFileNum;
}
