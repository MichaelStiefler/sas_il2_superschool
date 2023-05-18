// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import java.util.ArrayList;

import com.maddox.JGP.Point2d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CollideEnv.ResultTrigger;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Time;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.UnicodeTo8bit;

public class Trigger extends Actor {

    protected boolean checkPeriodic() {
        this.checkMove();
        if (this.getLinkActorName() != "" && this.getLinkActorName() != null && Actor.getByName(this.getLinkActorName()) == null) return false;
        ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(this.getPosTrigger(), this.getRadius(), this.getAltitudeMin(), this.getAltitudeMax(), this.getTriggeredByArmy(), this.getTriggeredBy(), this.getNoObjectsMin());
        this.setMessageAltitude((int) result.altiSea);
        if (this.isTriggerOnExit()) {
            if (this.isEntered()) return !result.result;
            if (result.result) this.setEntered(true);
            return false;
        } else return result.result;
    }

    protected void checkMove() {
        if (this.getLinkActorName() == "" || this.getLinkActorName() == null) return;
        Actor actor = Actor.getByName(this.getLinkActorName());
        if (actor == null) return;
        int i = -1;
        if (this.getLinkActorName().indexOf("Static") < 0 && this.getLinkActorName().indexOf("Chief") < 0) {
            Actor actor2;
            do {
                i++;
                actor2 = ((Wing) actor).airc[i];
            } while (i < 3 && (actor2 == null || !actor2.isAlive()));
            if (i < 3) actor = actor2;
            else {
                this.destroy();
                return;
            }
        }
        if (actor.isAlive()) {
            Point2d tmp = new Point2d(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y);
            if (this.lastPosLink == null) this.lastPosLink = tmp;
            this.getPosTrigger().x += tmp.x - this.lastPosLink.x;
            this.getPosTrigger().y += tmp.y - this.lastPosLink.y;
            this.lastPosLink = tmp;
        } else this.destroy();
    }

    protected void execute() {
//        System.out.println("Trigger " + this.getTriggerName() + " execute()");
        this.destroy();
    }

//    public void destroy() {
//        System.out.println("Trigger " + this.getTriggerName() + " destroy()");
//        super.destroy();
//    }

    protected void doExecute() {
        this.replicateTrigger();
    }

    protected Trigger(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, int altitudeMin, int altitudeMax, int triggeredBy, boolean triggerOnExit, int noObjectsMin, int probability, String linkActorName,
            String displayMessage, float displayTime) {
//        System.out.println("Trigger(" + triggerName + ", " + triggeredByArmy + ", " + timeout + ", " + posX + ", " + posY + ", " + radius + ", " + altitudeMin + ", " + altitudeMax + ", " + triggeredBy + ", " + hasTriggerActor + ", " + noObjectsMin + ", "
//                + probability + ", " + linkActorName + ", " + displayMessage + ", " + displayTime + ")");
        this.setTriggered(false);
        this.setEntered(false);
        this.setTriggerName(triggerName);
        if (timeout >= 0) this.setTimeout(timeout * 60L * 1000L);
        else this.setTimeout(-1L);
        this.setTriggeredByArmy(triggeredByArmy);
        this.setPosTrigger(new Point2d(posX, posY));
        this.lastPosLink = null;
        this.setRadius(radius);
        this.setAltitudeMin(altitudeMin);
        this.setAltitudeMax(altitudeMax);
        this.setTriggeredBy(triggeredBy);
        this.setTriggerOnExit(triggerOnExit);
        this.setNoObjectsMin(noObjectsMin);
        this.setProbability(probability);
        this.setLinkActorName(linkActorName);
        this.setDisplayMessage(displayMessage);
        this.setDisplayTime(displayTime);
        this.setMessageAltitude(-1);
        this.setActivated(true);
        // System.out.println("Trigger " + this.name() + " created!");
        World.cur().triggersGuard.addTrigger(this);
    }

    public static final void create(String s) {
//        System.out.println("Trigger create(" + s + ")");
        if (s == null || s.length() == 0) return;
        NumberTokenizer numbertokenizer = new NumberTokenizer(s);
        String name = numbertokenizer.next(null);
        int triggerType = TYPE_SPAWN;
        if (name.toLowerCase().indexOf("_trigger") > 0) triggerType = numbertokenizer.next(0);
        else triggerType = Integer.parseInt(name);
        int triggeredByArmy = numbertokenizer.next(1, 1, 2);
        boolean hasTimeout = numbertokenizer.next(0) == 1;
        int intTimeout = numbertokenizer.next(0, 0, 720);
        if (!hasTimeout) intTimeout = -1;
        int posX = numbertokenizer.next(0);
        int posY = numbertokenizer.next(0);
        int radius = numbertokenizer.next(1000, 1, 100000);
        int altitudeMin = numbertokenizer.next(0, 0, 20000);
        int altitudeMax = numbertokenizer.next(10000, 0, 20000);
        String triggerActorName = null;
        if (triggerType != TYPE_MESSAGE) triggerActorName = numbertokenizer.next(null);
        int triggeredBy = numbertokenizer.next(0, 0, 11);
        boolean triggerOnExit = numbertokenizer.next(0) == 1;
        int noObjectsMin = numbertokenizer.next(1, 1, 1000);
        int probability = numbertokenizer.next(100, 0, 100);
        int deltaAltitude = 0;
        if (triggerType == TYPE_SPAWN_AIRCRAFT_RELATIVE_ALTITUDE) deltaAltitude = numbertokenizer.next(0, -10000, 10000);
        boolean hasTriggerLink = numbertokenizer.next(0) == 1;
        String linkActorName = null;
        if (hasTriggerLink) linkActorName = numbertokenizer.next(null);
        float displayTime = numbertokenizer.next(5F, Time.tickConstLenFs(), Float.MAX_VALUE);
        String displayMessage = "";
        while (numbertokenizer.hasMoreElements()) {
            displayMessage = displayMessage + numbertokenizer.next(null);
            if (numbertokenizer.hasMoreElements()) displayMessage = displayMessage + " ";
        }
        switch (triggerType) {
            default:
                break;

            case TYPE_SPAWN:
                new TriggerSpawn(name, triggeredByArmy, intTimeout, posX, posY, radius, triggerActorName, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
                break;

            case TYPE_ACTIVATE:
                new TriggerActivate(name, triggeredByArmy, intTimeout, posX, posY, radius, triggerActorName, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
                break;

            case TYPE_SPAWN_AIRCRAFT_RELATIVE_ALTITUDE:
                if (triggeredBy == 0) triggeredBy = 4;
                new TriggerSpawnAircraftRelativeAltitude(name, triggeredByArmy, intTimeout, posX, posY, radius, triggerActorName, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, deltaAltitude, linkActorName,
                        displayMessage, displayTime);
                break;

            case TYPE_MESSAGE:
                new TriggerMessage(name, triggeredByArmy, intTimeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, triggerOnExit, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
                break;
        }
    }
    
    public static final void checkLinkedTriggerActivation() {
        for (int triggerIndex=0; triggerIndex < World.cur().triggersGuard.getTriggers().size(); triggerIndex++) {
            Trigger trigger = (Trigger)World.cur().triggersGuard.getTriggers().get(triggerIndex);
            // System.out.println("Checking Trigger " + trigger.name());
//            if (trigger.getTriggerClass() == Trigger.TYPE_ACTIVATE
//                    && trigger.getTargetActorName() != null
//                    && trigger.getTargetActorName().length() > 0
//                    && trigger.getTargetActorName().indexOf("Trigger") > 0) {
//                System.out.println("TargetActorName is " + trigger.getTargetActorName());
//                Actor triggerTargetActor = Actor.getByName(trigger.getTargetActorName());
//                System.out.println("triggerTargetActor is " + (triggerTargetActor==null?"null":triggerTargetActor.getClass().getName()));
//                
//                if (triggerTargetActor == null) {
//                    triggerTargetActor = World.cur().triggersGuard.getTrigger(trigger.getTargetActorName());
//                    System.out.println("triggerTargetActor is now " + (triggerTargetActor==null?"null":triggerTargetActor.getClass().getName()));
//                }
//                
//                if (triggerTargetActor instanceof Trigger) {
//                    ((Trigger)triggerTargetActor).setActivated(false);
//                    System.out.println("Trigger " + ((Trigger)triggerTargetActor).name() + " deactivated!");
//                }
//            }
            if (trigger.getTriggerClass() == Trigger.TYPE_ACTIVATE
                    && trigger.getTargetActorNames() != null
                    && trigger.getTargetActorNames().size() > 0) {
                for (int targetActorNamesIndex = 0; targetActorNamesIndex < trigger.getTargetActorNames().size(); targetActorNamesIndex++) {
                    String targetActorName = (String)trigger.getTargetActorNames().get(targetActorNamesIndex);
                    if (targetActorName.indexOf("Trigger") < 0) continue;
                    // System.out.println("TargetActorName is " + targetActorName);
                    Actor triggerTargetActor = World.cur().triggersGuard.getTrigger(targetActorName);
                    // System.out.println("triggerTargetActor is " + (triggerTargetActor==null?"null":triggerTargetActor.getClass().getName()));
                    if (triggerTargetActor instanceof Trigger) {
                        ((Trigger)triggerTargetActor).setActivated(false);
                        // System.out.println("Trigger " + ((Trigger)triggerTargetActor).name() + " deactivated!");
                    }
                }
                
            }
        }
    }

    public ArrayList getTargetActorNames() {
        return null;
    }

    public String getTargetActorName() {
        return null;
    }

    public double getAltitude() {
        return -1D;
    }

    private static void subString(StringBuffer stringbuffer, String s, int i, int j) {
        while (i < j)
            stringbuffer.append(s.charAt(i++));
    }

    protected String prepareTextArmy(int i) {
        String s = (Army.name(i) + ">").toUpperCase();
        String s1 = "NONE>";
        int k = this.getDisplayMessage().length();
        StringBuffer stringbuffer = new StringBuffer();
        if (k == 0) return "";
        int l = this.getDisplayMessage().indexOf("<ARMY ");
        int i1 = 0;
        if (l >= 0) do {
            i1 = this.getDisplayMessage().indexOf("</ARMY>", l);
            if (i1 == -1) i1 = k;
            l += 6;
            if (this.getDisplayMessage().startsWith(s, l)) {
                l += s.length();
                subString(stringbuffer, this.getDisplayMessage(), l, i1);
            } else if (this.getDisplayMessage().startsWith(s1, l)) {
                l += s1.length();
                subString(stringbuffer, this.getDisplayMessage(), l, i1);
            }
        } while ((l = this.getDisplayMessage().indexOf("<ARMY ", l)) >= 0);
        else return UnicodeTo8bit.load(this.getDisplayMessage());
        return UnicodeTo8bit.load(stringbuffer.toString());
    }

    public static String prepareTextKeyWords(String s, Point2d pos, int alti) {
        int k = s.length();
        if (k == 0) return "";
        String listWords[] = { "%GRID%", "%ALTM%", "%ALTF%" };
        for (int i = 0; i < listWords.length; i++) {
            int posWord = s.indexOf(listWords[i]);
            if (posWord >= 0) {
                StringBuffer stringbuffer = new StringBuffer();
                int posPrev = 0;
                do {
                    subString(stringbuffer, s, posPrev, posWord);
                    switch (i) {
                        case 0:
                            stringbuffer.append(getGrid(pos));
                            break;

                        case 1:
                            stringbuffer.append((alti + 50) / 100 * 100 + " m");
                            break;

                        case 2:
                            stringbuffer.append((int) ((alti * 3.28084D + 250D) / 500D) * 500 + " ft");
                            break;
                    }
                    posPrev = posWord + listWords[i].length();
                } while ((posWord = s.indexOf(listWords[i], posPrev)) >= 0);
                subString(stringbuffer, s, posPrev, k);
                s = stringbuffer.toString();
                k = s.length();
            }
        }

        return s;
    }

    protected static String getGrid(Point2d pos) {
        double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
        double d1 = (Main3D.cur3D().land2D.worldOfsX() + pos.x) / 10000D;
        double d2 = (Main3D.cur3D().land2D.worldOfsX() + pos.y) / 10000D;
        char c = (char) (int) (65D + Math.floor((d1 / 676D - Math.floor(d1 / 676D)) * 26D));
        char c1 = (char) (int) (65D + Math.floor((d1 / 26D - Math.floor(d1 / 26D)) * 26D));
        String s;
        if (d > 260D) s = "" + c + c1;
        else s = "" + c1;
        int j1 = (int) Math.ceil(d2);
        return s + "-" + j1;
    }

    protected Point2d getPosLink() {
        Actor actorlink = Actor.getByName(this.getLinkActorName());
        if (actorlink == null) return null;
        int i = -1;
        if (actorlink instanceof Wing) {
            Actor actor2;
            do {
                i++;
                actor2 = ((Wing) actorlink).airc[i];
            } while (i < 3 && (actor2 == null || !actor2.isAlive()));
            if (i < 3) actorlink = actor2;
            else return null;
        }
        return new Point2d(actorlink.pos.getAbsPoint().x, actorlink.pos.getAbsPoint().y);
    }

    protected void doSendMsg(boolean bLink) {
        if (this.getDisplayMessage() != "" && this.getDisplayMessage() != null) {
            Point2d p2dTemp = bLink ? this.getPosLink() : this.getPosTrigger();
            if (p2dTemp == null) p2dTemp = this.getPosTrigger();
            if (p2dTemp == null) p2dTemp = new Point2d();
            Point2d point2d = new Point2d(p2dTemp);
//            Point2d point2d = new Point2d(bLink ? this.getPosLink() : this.getPosTrigger());
//            if (Main.cur().netServerParams.isMaster()) ((NetUser) NetEnv.host()).replicateTriggerMsg(this.prepareTextArmy(1), this.prepareTextArmy(2), (float) point2d.x, (float) point2d.y, this.getMessageAltitude(), this.getDisplayTime());
            if (Config.isUSE_RENDER()) {
//                HUD.clearWaitingList();
                HUD.addMsgToWaitingList(prepareTextKeyWords(this.prepareTextArmy(World.getPlayerArmy()), point2d, this.getMessageAltitude()), this.getDisplayTime());
            }
        }
    }

    public String getTriggerName() {
        return this.triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public int getTriggeredByArmy() {
        return this.triggeredByArmy;
    }

    public void setTriggeredByArmy(int triggeredByArmy) {
        this.triggeredByArmy = triggeredByArmy;
    }

    public Point2d getPosTrigger() {
        return this.posTrigger;
    }

    public void setPosTrigger(Point2d posTigger) {
        this.posTrigger = posTigger;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getAltitudeMin() {
        return this.altitudeMin;
    }

    public void setAltitudeMin(int altitudeMin) {
        this.altitudeMin = altitudeMin;
    }

    public int getAltitudeMax() {
        return this.altitudeMax;
    }

    public void setAltitudeMax(int altitudeMax) {
        this.altitudeMax = altitudeMax;
    }

    public boolean isTriggered() {
        return this.isTriggered;
    }

    public void setTriggered(boolean isTriggered) {
        this.isTriggered = isTriggered;
    }

    public int getTriggeredBy() {
        return this.triggeredBy;
    }

    public void setTriggeredBy(int triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public boolean isTriggerOnExit() {
        return this.triggerOnExit;
    }

    public void setTriggerOnExit(boolean triggerOnExit) {
        this.triggerOnExit = triggerOnExit;
    }

    public boolean isEntered() {
        return this.isEntered;
    }

    public void setEntered(boolean isEntered) {
        this.isEntered = isEntered;
    }

    public int getNoObjectsMin() {
        return this.noObjectsMin;
    }

    public void setNoObjectsMin(int noObjectsMin) {
        this.noObjectsMin = noObjectsMin;
    }

    public int getProbability() {
        return this.probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public String getLinkActorName() {
        return this.linkActorName;
    }

    public void setLinkActorName(String linkActorName) {
        this.linkActorName = linkActorName;
    }

    public String getDisplayMessage() {
        return this.displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public float getDisplayTime() {
        return this.displayTime;
    }

    public void setDisplayTime(float displayTime) {
        this.displayTime = displayTime;
    }

    public int getMessageAltitude() {
        return this.messageAltitude;
    }

    public void setMessageAltitude(int messageAltitude) {
        this.messageAltitude = messageAltitude;
    }

    public int getTriggerClass() {
        return triggerClass;
    }

    public void setTriggerClass(int triggerClass) {
        this.triggerClass = triggerClass;
    }
    
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    
    private static boolean netServerParamsIsNull;
    private static boolean missionIsSingle;
    private static boolean missionIsNet;
    private static boolean hostIsNetUser;
    private static boolean netUserIsMirrored;
    private static boolean trackIsRecording;
    private static boolean trackIsQuickRecording;
    private static boolean trackIsPlaying;
    

    public static void getReplicatedTrigger(NetMsgInput netmsginput) {
//        Exception test = new Exception("getReplicatedTrigger");
//        test.printStackTrace();

        try {
            int triggerClass = netmsginput.readByte();
            String triggerName = netmsginput.read255();
            Trigger trigger = World.cur().triggersGuard.getTrigger(triggerName);
            // System.out.println(("Trigger " + triggerName + " = " + trigger);
            if (trigger != null && !trigger.isDestroyed()) {
                switch(triggerClass) {
                    case TYPE_SPAWN:
                    case TYPE_ACTIVATE:
                    case TYPE_SPAWN_AIRCRAFT_RELATIVE_ALTITUDE:
                    case TYPE_MESSAGE:
                        trigger.setMessageAltitude(netmsginput.readInt());
                        trigger.setDisplayTime(netmsginput.readFloat());
                        trigger.setPosTrigger(new Point2d(netmsginput.readDouble(), netmsginput.readDouble()));
//                        if (trigger.getLinkActorName() != null && trigger.getLinkActorName().length() > 0) {
//                            trigger.setLinkActorName(netmsginput.read255());
//                        } else {
//                            trigger.setPosTrigger(new Point2d(netmsginput.readDouble(), netmsginput.readDouble()));
//                        }
                        break;
                    default:
                        break;
                }
                trigger.doExecute();
                trigger.replicateTrigger();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void replicateTrigger() {
        boolean newNetServerParamsIsNull = Main.cur().netServerParams == null;
        boolean newMissionIsSingle= Mission.isSingle();
        boolean newMissionIsNet = Mission.isNet();
        boolean newHostIsNetUser = NetEnv.host() instanceof NetUser;
        boolean newNetUserIsMirrored = newHostIsNetUser?((NetUser) NetEnv.host()).isMirrored():false;
        boolean newTrackIsRecording = NetMissionTrack.isRecording();
        boolean newTrackIsQuickRecording = NetMissionTrack.isQuickRecording();
        boolean newTrackIsPlaying = NetMissionTrack.isPlaying();
        
        if (netServerParamsIsNull != newNetServerParamsIsNull
                || missionIsSingle != newMissionIsSingle
                || missionIsNet != newMissionIsNet
                || hostIsNetUser != newHostIsNetUser
                || netUserIsMirrored != newNetUserIsMirrored
                || trackIsRecording != newTrackIsRecording
                || trackIsQuickRecording != newTrackIsQuickRecording
                || trackIsPlaying != newTrackIsPlaying) {
//             System.out.println(("netServerParamsIsNull = " + newNetServerParamsIsNull +
//                    ", missionIsSingle = " + newMissionIsSingle +
//                    ", missionIsNet = " + newMissionIsNet +
//                    ", hostIsNetUser = " + newHostIsNetUser +
//                    ", netUserIsMirrored = " + newNetUserIsMirrored +
//                    ", trackIsRecording = " + newTrackIsRecording +
//                    ", trackIsQuickRecording = " + newTrackIsQuickRecording +
//                    ", trackIsPlaying = " + newTrackIsPlaying);
            netServerParamsIsNull = newNetServerParamsIsNull;
            missionIsSingle = newMissionIsSingle;
            missionIsNet = newMissionIsNet;
            hostIsNetUser = newHostIsNetUser;
            netUserIsMirrored = newNetUserIsMirrored;
            trackIsRecording = newTrackIsRecording;
            trackIsQuickRecording = newTrackIsQuickRecording;
            trackIsPlaying = newTrackIsPlaying;
        }
        
//        Exception test = new Exception("replicateTrigger");
//        test.printStackTrace();
        if (Main.cur().netServerParams == null || (Mission.isSingle() && !NetMissionTrack.isRecording())) return;
//        System.out.println("replicateTrigger 1");
        if (!(NetEnv.host() instanceof NetUser)) return;
//        System.out.println("replicateTrigger 2");
        NetUser netUser = ((NetUser) NetEnv.host());
        if (!netUser.isMirrored()) return;
//        System.out.println("replicateTrigger 3");
        try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(NetUser.MSG_TRIGGER);
            netmsgguaranted.writeByte(this.getTriggerClass());
            netmsgguaranted.write255(this.getTriggerName());
            switch(this.getTriggerClass()) {
                case TYPE_SPAWN:
                case TYPE_ACTIVATE:
                case TYPE_SPAWN_AIRCRAFT_RELATIVE_ALTITUDE:
                case TYPE_MESSAGE:
                    netmsgguaranted.writeInt(this.getMessageAltitude());
                    netmsgguaranted.writeFloat(this.getDisplayTime());
                    if (this.getLinkActorName() != null && this.getLinkActorName().length() > 0) {
//                        netmsgguaranted.write255(this.getLinkActorName());
                        Point2d p2dTemp = this.getPosLink();
                        if (p2dTemp == null) p2dTemp = this.getPosTrigger();
                        netmsgguaranted.writeDouble(p2dTemp.x);
                        netmsgguaranted.writeDouble(p2dTemp.y);
                    } else {
                        netmsgguaranted.writeDouble(this.getPosTrigger().x);
                        netmsgguaranted.writeDouble(this.getPosTrigger().y);
                    }
                    break;
                default:
                    break;
            }
            
            
            netUser.post(netmsgguaranted);
//            System.out.println("Replicated Trigger " + this.getTriggerName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    bLink ? this.getPosLink() : this.getPosTrigger()
//    prepareTextKeyWords(this.prepareTextArmy(World.getPlayerArmy()), point2d, this.getMessageAltitude()), this.getDisplayTime() * 1000


    public static final int VERSION                               = 12;
    public static final int TYPE_SPAWN                            = 0;
    public static final int TYPE_ACTIVATE                         = 1;
    public static final int TYPE_SPAWN_AIRCRAFT_RELATIVE_ALTITUDE = 2;
    public static final int TYPE_MESSAGE                          = 3;

    private String          triggerName;
    private long            timeout;
    private int             triggeredByArmy;
    private Point2d         posTrigger;
    private int             radius;
    private int             altitudeMin;
    private int             altitudeMax;
    private boolean         isTriggered;
    private int             triggeredBy;
    private boolean         triggerOnExit;
    private boolean         isEntered;
    private int             noObjectsMin;
    private int             probability;
    private String          linkActorName;
    private Point2d         lastPosLink;
    private String          displayMessage;
    private float           displayTime;
    private int             messageAltitude;
    private int             triggerClass;
    private boolean         activated;
}
