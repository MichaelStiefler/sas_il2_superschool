// TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
// This class is completely new to Triggers
// Rewritten and refactored by SAS~Storebror

package com.maddox.il2.ai;

import com.maddox.JGP.Point2d;
import com.maddox.il2.builder.ActorTrigger;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CollideEnv.ResultTrigger;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.NetEnv;
import com.maddox.util.NumberTokenizer;
import com.maddox.util.UnicodeTo8bit;

public class Trigger extends Actor {

    protected boolean checkPeriodic() {
        this.checkMove();
        if (this.getLinkActorName() != "" && this.getLinkActorName() != null && Actor.getByName(this.getLinkActorName()) == null) return false;
        ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(this.getPosTigger(), this.getRadius(), this.getAltitudeMin(), this.getAltitudeMax(), this.getTriggeredByArmy(), this.getTriggeredBy(), this.getNoObjectsMin());
        this.setMessageAltitude((int) result.altiSea);
        if (this.isHasTriggerActor()) {
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
            this.getPosTigger().x += tmp.x - this.lastPosLink.x;
            this.getPosTigger().y += tmp.y - this.lastPosLink.y;
            this.lastPosLink = tmp;
        } else this.destroy();
    }

    protected void execute() {
        this.destroy();
    }

    protected Trigger(String triggerName, int triggeredByArmy, int timeout, int posX, int posY, int radius, int altitudeMin, int altitudeMax, int triggeredBy, boolean hasTriggerActor, int noObjectsMin, int probability, String linkActorName,
            String displayMessage, int displayTime) {
        this.setTriggered(false);
        this.setEntered(false);
        this.setTriggerName(triggerName);
        if (timeout >= 0) this.setTimeout(timeout * 60L * 1000L);
        else this.setTimeout(-1L);
        this.setTriggeredByArmy(triggeredByArmy);
        this.setPosTigger(new Point2d(posX, posY));
        this.lastPosLink = null;
        this.setRadius(radius);
        this.setAltitudeMin(altitudeMin);
        this.setAltitudeMax(altitudeMax);
        this.setTriggeredBy(triggeredBy);
        this.setHasTriggerActor(hasTriggerActor);
        this.setNoObjectsMin(noObjectsMin);
        this.setProbability(probability);
        this.setLinkActorName(linkActorName);
        this.setDisplayMessage(displayMessage);
        this.setDisplayTime(displayTime);
        this.setMessageAltitude(-1);
        World.cur().triggersGuard.addTrigger(this);
    }

    public static final void create(String s) {
        if (s == null || s.length() == 0) return;
        NumberTokenizer numbertokenizer = new NumberTokenizer(s);
        String name = numbertokenizer.next(null);
        int triggerType = ActorTrigger.TRIGGER_TYPE_SPAWN;
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
        if (triggerType != ActorTrigger.TRIGGER_TYPE_MESSAGE) triggerActorName = numbertokenizer.next(null);
        int triggeredBy = numbertokenizer.next(0, 0, 11);
        boolean hasTriggerActor = numbertokenizer.next(0) == 1;
        int noObjectsMin = numbertokenizer.next(1, 1, 1000);
        int probability = numbertokenizer.next(100, 0, 100);
        int deltaAltitude = 0;
        if (triggerType == ActorTrigger.TRIGGER_TYPE_AIRSPAWN_RELATIVE_ALTITUDE) deltaAltitude = numbertokenizer.next(0, -10000, 10000);
        boolean hasTriggerLink = numbertokenizer.next(0) == 1;
        String linkActorName = null;
        if (hasTriggerLink) linkActorName = numbertokenizer.next(null);
        int displayTime = numbertokenizer.next(5, 1, 60);
        String displayMessage = "";
        while (numbertokenizer.hasMoreElements()) {
            displayMessage = displayMessage + numbertokenizer.next(null);
            if (numbertokenizer.hasMoreElements()) displayMessage = displayMessage + " ";
        }
        switch (triggerType) {
            default:
                break;

            case TYPE_APPARITION:
                new TriNewAircraftAir(name, triggeredByArmy, intTimeout, posX, posY, radius, triggerActorName, altitudeMin, altitudeMax, triggeredBy, hasTriggerActor, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
                break;

            case TYPE_STARTUP:
                new TriNewAircraftSol(name, triggeredByArmy, intTimeout, posX, posY, radius, triggerActorName, altitudeMin, altitudeMax, triggeredBy, hasTriggerActor, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
                break;

            case TYPE_APPARITION_ALTITUDE_RELATED:
                if (triggeredBy == 0) triggeredBy = 4;
                new TriNewAircraftAirLevel(name, triggeredByArmy, intTimeout, posX, posY, radius, triggerActorName, altitudeMin, altitudeMax, triggeredBy, hasTriggerActor, noObjectsMin, probability, deltaAltitude, linkActorName, displayMessage,
                        displayTime);
                break;

            case TYPE_MESSAGE:
                new TriNewMessage(name, triggeredByArmy, intTimeout, posX, posY, radius, altitudeMin, altitudeMax, triggeredBy, hasTriggerActor, noObjectsMin, probability, linkActorName, displayMessage, displayTime);
                break;
        }
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
            Point2d point2d = new Point2d(bLink ? this.getPosLink() : this.getPosTigger());
            if (Main.cur().netServerParams.isMaster()) ((NetUser) NetEnv.host()).replicateTriggerMsg(this.prepareTextArmy(1), this.prepareTextArmy(2), (float) point2d.x, (float) point2d.y, this.getMessageAltitude(), this.getDisplayTime());
            if (Config.isUSE_RENDER()) HUD.addMsgToWaitingList(prepareTextKeyWords(this.prepareTextArmy(World.getPlayerArmy()), point2d, this.getMessageAltitude()), this.getDisplayTime() * 1000);
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

    public Point2d getPosTigger() {
        return this.posTigger;
    }

    public void setPosTigger(Point2d posTigger) {
        this.posTigger = posTigger;
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

    public boolean isHasTriggerActor() {
        return this.hasTriggerActor;
    }

    public void setHasTriggerActor(boolean hasTriggerActor) {
        this.hasTriggerActor = hasTriggerActor;
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

    public int getDisplayTime() {
        return this.displayTime;
    }

    public void setDisplayTime(int displayTime) {
        this.displayTime = displayTime;
    }

    public int getMessageAltitude() {
        return this.messageAltitude;
    }

    public void setMessageAltitude(int messageAltitude) {
        this.messageAltitude = messageAltitude;
    }

    public static final int VERSION                          = 12;
    public static final int TYPE_APPARITION                  = 0;
    public static final int TYPE_STARTUP                     = 1;
    public static final int TYPE_APPARITION_ALTITUDE_RELATED = 2;
    public static final int TYPE_MESSAGE                     = 3;
    private String          triggerName;
    private long            timeout;
    private int             triggeredByArmy;
    private Point2d         posTigger;
    private int             radius;
    private int             altitudeMin;
    private int             altitudeMax;
    private boolean         isTriggered;
    private int             triggeredBy;
    private boolean         hasTriggerActor;
    private boolean         isEntered;
    private int             noObjectsMin;
    private int             probability;
    private String          linkActorName;
    private Point2d         lastPosLink;
    private String          displayMessage;
    private int             displayTime;
    private int             messageAltitude;
}
