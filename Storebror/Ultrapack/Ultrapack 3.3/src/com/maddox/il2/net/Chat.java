package com.maddox.il2.net;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.LDRres;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetHost;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgOutput;
import com.maddox.rts.NetMsgSpawn;
import com.maddox.rts.NetObj;
import com.maddox.rts.NetSpawn;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Spawn;
import com.maddox.sound.AudioDevice;
import com.maddox.sound.RadioChannelSpawn;

public class Chat extends NetObj {
    static class SPAWN implements NetSpawn {

        public void netSpawn(int i, NetMsgInput netmsginput) {
            try {
                new Chat(netmsginput.channel(), i);
            } catch (Exception exception) {
                NetObj.printDebug(exception);
            }
        }

        SPAWN() {
        }
    }

    public int getMaxBuflen() {
        return this.maxBufLen;
    }

    public void setMaxBufLen(int i) {
        if (i < 1) i = 1;
        this.maxBufLen = i;
        if (this.buf.size() > this.maxBufLen) this.stampUpdate++;
        for (; this.buf.size() > this.maxBufLen; this.buf.remove(this.buf.size() - 1))
            ;
    }

    public void clear() {
        this.buf.clear();
        this.stampUpdate++;
    }

    private ChatMessage translateMsg(ChatMessage chatmessage) {
        if (chatmessage.flags == 1) {
            if (this.resOrder == null) this.resOrder = ResourceBundle.getBundle("i18n/hud_order", RTSConf.cur.locale, LDRres.loader());
            StringTokenizer stringtokenizer = new StringTokenizer(chatmessage.msg);
            StringBuffer stringbuffer = new StringBuffer();
            boolean flag = true;
            String s2;
            for (; stringtokenizer.hasMoreTokens(); stringbuffer.append(s2)) {
                if (!flag) stringbuffer.append(" > ");
                flag = false;
                String s1 = stringtokenizer.nextToken();
                s2 = null;
                String s3 = World.getPlayerLastCountry();
                if (s3 != null) try {
                    s2 = this.resOrder.getString(s1 + "_" + s3);
                } catch (Exception exception1) {}
                if (s2 != null) continue;
                try {
                    s2 = this.resOrder.getString(s1);
                } catch (Exception exception2) {
                    s2 = s1;
                }
            }

            ChatMessage chatmessage2 = new ChatMessage();
            chatmessage2.flags = chatmessage.flags;
            chatmessage2.from = chatmessage.from;
            chatmessage2.to = chatmessage.to;
            chatmessage2.msg = stringbuffer.toString();
            chatmessage = chatmessage2;
        } else if ((chatmessage.flags & 0xe) != 0) {
            if (this.resLog == null) this.resLog = ResourceBundle.getBundle("i18n/netmessages", RTSConf.cur.locale, LDRres.loader());
            ChatMessage chatmessage1 = new ChatMessage();
            chatmessage1.flags = chatmessage.flags;
            chatmessage1.from = chatmessage.from;
            chatmessage1.to = chatmessage.to;
            int i = (chatmessage.flags & 0xe) >> 1;
            switch (i) {
                case 4:
                case 5:
                default:
                    break;

                case 2:
                    if (chatmessage.param0 != null) this.params[0] = ((NetUser) chatmessage.param0).shortName();
                    break;

                case 3:
                    if (chatmessage.param0 != null) this.params[0] = ((NetUser) chatmessage.param0).shortName();
                    if (chatmessage.param1 != null) this.params[1] = ((NetUser) chatmessage.param1).shortName();
                    break;

                case 6:
                    this.params[0] = chatmessage.param0;
                    break;

                case 7:
                    this.params[0] = chatmessage.param0;
                    this.params[1] = chatmessage.param1;
                    break;
            }
            String s = null;
            try {
                s = this.resLog.getString(chatmessage.msg);
            } catch (Exception exception) {
                s = chatmessage.msg;
            }
            chatmessage1.msg = MessageFormat.format(s, this.params);
            chatmessage = chatmessage1;
            this.params[0] = this.params[1] = null;
        }
        return chatmessage;
    }

    private void addMsg(ChatMessage chatmessage) {
        chatmessage = this.translateMsg(chatmessage);
        if (chatmessage.msg != null && (chatmessage.msg.startsWith("Morse:") || chatmessage.msg.startsWith("morse:"))) try {
            if (!World.cur().blockMorseChat) World.getPlayerAircraft().playChatMsgAsMorse(chatmessage.msg);
        } catch (Exception exception) {
            System.out.println("Exception - " + exception);
        }
        else {
            this.buf.add(0, chatmessage);
            this.stampUpdate++;
            for (; this.buf.size() > this.maxBufLen; this.buf.remove(this.buf.size() - 1))
                ;
            if (chatmessage.from != null)
                // TODO: +++ Auto Login / FBDj Admin Mod +++
                this.checkServerLoginMessage(chatmessage);
            // TODO: --- Auto Login / FBDj Admin Mod ---
            else System.out.println("Chat: --- " + chatmessage.msg);
        }
    }

    // TODO: +++ Auto Login / FBDj Admin Mod +++
    public void checkServerLoginMessage(ChatMessage chatmessage) {
        System.out.println("Chat: " + chatmessage.from.shortName() + ": \t" + chatmessage.msg);
        if (chatmessage.from.shortName().equals("Server")) if (chatmessage.msg.endsWith("using admin name. Must login or will be kicked!")) {
            List receiverList = new ArrayList();
            receiverList.add(chatmessage.from);
            if (Config.cur.bUseAutoAdminLogin && Config.cur.sAutoAdminPassword.length() > 0) this.send(NetEnv.host(), "!login " + Config.cur.sAutoAdminPassword, receiverList);
            else if (Config.cur.bUseAutoUserLogin && Config.cur.sAutoUserPassword.length() > 0) this.send(NetEnv.host(), ">login " + Config.cur.sAutoUserPassword, receiverList);
        } else if (chatmessage.msg.endsWith("Logged in as Admin")) HUD.setAdminMode(true);
    }
    // TODO: --- Auto Login / FBDj Admin Mod ---

    public static void sendLogRnd(int i, String s, Aircraft aircraft, Aircraft aircraft1) {
        sendLog(i, s + (int) (Math.random() * 2D + 1.3999999999999999D), aircraft, aircraft1);
    }

    public static void sendLog(int i, String s, Aircraft aircraft, Aircraft aircraft1) {
        if (Main.cur().chat == null) return;
        if (Main.cur().netServerParams == null) return;
        if (i > Main.cur().netServerParams.autoLogDetail()) return;
        NetUser netuser = null;
        if (aircraft != null && aircraft.isNetPlayer()) netuser = aircraft.netUser();
        NetUser netuser1 = null;
        if (aircraft1 != null && aircraft1.isNetPlayer()) netuser1 = aircraft1.netUser();
        sendLog(i, s, netuser, netuser1);
    }

    public static void sendLog(int i, String s, NetUser netuser, NetUser netuser1) {
        if (Main.cur().chat == null) return;
        if (Main.cur().netServerParams == null) return;
        if (i > Main.cur().netServerParams.autoLogDetail()) return;
        int j = 2;
        if (netuser != null) j = 4;
        if (netuser1 != null) j = 6;
        Main.cur().chat.send(null, s, null, (byte) j, netuser, netuser1, true);
    }

    public static void sendLog(int i, String s, String s1, String s2) {
        if (Main.cur().chat == null) return;
        if (Main.cur().netServerParams == null) return;
        if (i > Main.cur().netServerParams.autoLogDetail()) return;
        int j = 2;
        if (s1 != null) j = 12;
        if (s2 != null) j = 14;
        Main.cur().chat.send(null, s, null, (byte) j, s1, s2, true);
    }

    public void send(NetHost nethost, String s, List list) {
        this.send(nethost, s, list, (byte) 0);
    }

    public void send(NetHost nethost, String s, List list, byte byte0) {
        this.send(nethost, s, list, byte0, true);
    }

    public void send(NetHost nethost, String s, List list, byte byte0, boolean flag) {
        this.send(nethost, s, list, byte0, null, null, flag);
    }

    public void send(NetHost nethost, String s, List list, byte byte0, Object obj, Object obj1, boolean flag) {
        if (NetMissionTrack.isPlaying()) return;
        ChatMessage chatmessage = new ChatMessage();
        chatmessage.flags = byte0;
        chatmessage.from = nethost;
        chatmessage.to = list;
        if (s.length() > 80) s = s.substring(0, 80);
        int i = NetMsgOutput.len255(s);
        if (list != null) i += list.size() * NetMsgOutput.netObjReferenceLen();
        if (i > 250) {
            i -= 250;
            s = s.substring(0, s.length() - i);
        }
        chatmessage.msg = s;
        chatmessage.param0 = obj;
        chatmessage.param1 = obj1;
        if (flag) this.addMsg(chatmessage);
        if (this.isMirror() || this.isMirrored()) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeNetObj(chatmessage.from);
            netmsgguaranted.writeByte(chatmessage.flags);
            netmsgguaranted.write255(chatmessage.msg);
            if ((chatmessage.flags & 0xe) != 0) {
                int j = (chatmessage.flags & 0xe) >> 1;
                switch (j) {
                    case 2:
                        netmsgguaranted.writeNetObj((NetObj) obj);
                        break;

                    case 3:
                        netmsgguaranted.writeNetObj((NetObj) obj);
                        netmsgguaranted.writeNetObj((NetObj) obj1);
                        break;

                    case 6:
                        netmsgguaranted.write255((String) obj);
                        break;

                    case 7:
                        netmsgguaranted.write255((String) obj);
                        netmsgguaranted.write255((String) obj1);
                        break;
                }
            }
            if (chatmessage.to != null) for (int k = 0; k < chatmessage.to.size(); k++)
                netmsgguaranted.writeNetObj((NetObj) chatmessage.to.get(k));
            this.postExclude(null, netmsgguaranted);
        } catch (Exception exception) {
            printDebug(exception);
        }
    }

    public boolean netInput(NetMsgInput netmsginput) throws IOException {
        netmsginput.reset();
        ChatMessage chatmessage = new ChatMessage();
        chatmessage.from = (NetHost) netmsginput.readNetObj();
        chatmessage.flags = (byte) netmsginput.readUnsignedByte();
        chatmessage.msg = netmsginput.read255();
        byte byte0 = 0;
        if ((chatmessage.flags & 0xe) != 0) {
            int i = (chatmessage.flags & 0xe) >> 1;
            switch (i) {
                case 2:
                    chatmessage.param0 = netmsginput.readNetObj();
                    byte0 = 1;
                    break;

                case 3:
                    chatmessage.param0 = netmsginput.readNetObj();
                    chatmessage.param1 = netmsginput.readNetObj();
                    byte0 = 2;
                    break;

                case 6:
                    chatmessage.param0 = netmsginput.read255();
                    break;

                case 7:
                    chatmessage.param0 = netmsginput.read255();
                    chatmessage.param1 = netmsginput.read255();
                    break;
            }
        }
        boolean flag = false;
        int j = netmsginput.available() / NetMsgInput.netObjReferenceLen();
        if (j == 0) flag = true;
        else {
            chatmessage.to = new ArrayList(j);
            for (int k = 0; k < j; k++)
                chatmessage.to.add(netmsginput.readNetObj());

            if (netmsginput.channel() instanceof NetChannelInStream) {
                NetUser netuser = NetUser.findTrackWriter();
                flag = netuser == chatmessage.from || chatmessage.to.indexOf(netuser) >= 0;
            } else flag = chatmessage.to.indexOf(NetEnv.host()) >= 0;
        }
        if (flag) this.addMsg(chatmessage);
        int l = 0;
        if (this.isMirror() && netmsginput.channel() != this.masterChannel()) l = 1;
        if (this.isMirrored()) {
            l += this.countMirrors();
            if (netmsginput.channel() != this.masterChannel()) l--;
        }
        if (l > 0) this.postExclude(netmsginput.channel(), new NetMsgGuaranted(netmsginput, j + byte0 + 1));
        return true;
    }

    public void destroy() {
        if (USE_NET_PHONE) {
            AudioDevice.endNetPhone();
            radioSpawn.killMasterChannels();
        }
        super.destroy();
        Main.cur().chat = null;
    }

    public Chat() {
        super(null);
        this.buf = new ArrayList();
        this.stampUpdate = 0;
        this.maxBufLen = 80;
        this.params = new Object[2];
        Main.cur().chat = this;
        if (USE_NET_PHONE) {
            AudioDevice.beginNetPhone();
            String s = ((NetUser) NetEnv.host()).radio();
            int i = ((NetUser) NetEnv.host()).curCodec();
            ((NetUser) NetEnv.host()).setRadio(null, 0);
            ((NetUser) NetEnv.host()).setRadio(s, i);
        }
    }

    public Chat(NetChannel netchannel, int i) {
        super(null, netchannel, i);
        this.buf = new ArrayList();
        this.stampUpdate = 0;
        this.maxBufLen = 80;
        this.params = new Object[2];
        Main.cur().chat = this;
        if (USE_NET_PHONE && !NetMissionTrack.isPlaying()) AudioDevice.beginNetPhone();
    }

    public NetMsgSpawn netReplicate(NetChannel netchannel) throws IOException {
        return new NetMsgSpawn(this);
    }

    public static boolean           USE_NET_PHONE = true;
    public static RadioChannelSpawn radioSpawn    = new RadioChannelSpawn();
    public ResourceBundle           resOrder;
    public ResourceBundle           resLog;
    public List                     buf;
    public int                      stampUpdate;
    private int                     maxBufLen;
    private Object                  params[];

    static {
        Spawn.add(com.maddox.il2.net.Chat.class, new SPAWN());
    }

}
