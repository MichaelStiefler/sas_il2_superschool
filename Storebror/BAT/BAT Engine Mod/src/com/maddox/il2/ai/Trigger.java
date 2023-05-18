package com.maddox.il2.ai;

import com.maddox.JGP.Point2d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.CollideEnv;
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
        checkMove();
        if (sLink != "" && sLink != null && Actor.getByName(sLink) == null)
            return false;
        CollideEnv.ResultTrigger result = Engine.collideEnv().getEnemiesInCyl(posTigger, rayon, altiMin, altiMax, army, iaHumans, avionMin);
        altiMsg = (int) result.altiSea;
        if (bTSortie) {
            if (bIsEnter)
                return !result.result;
            if (result.result)
                bIsEnter = true;
            return false;
        } else {
            return result.result;
        }
    }

    protected void checkMove() {
        if (sLink == "" || sLink == null)
            return;
        Actor actor = Actor.getByName(sLink);
        if (actor == null)
            return;
        int i = -1;
        if (sLink.indexOf("Static") < 0 && sLink.indexOf("Chief") < 0) {
            Actor actor2;
            do {
                i++;
                actor2 = ((Wing) actor).airc[i];
            } while (i < 3 && (actor2 == null || !actor2.isAlive()));
            if (i < 3) {
                actor = actor2;
            } else {
                destroy();
                return;
            }
        }
        if (actor.isAlive()) {
            Point2d tmp = new Point2d(actor.pos.getAbsPoint().x, actor.pos.getAbsPoint().y);
            if (lastPosLink == null)
                lastPosLink = tmp;
            posTigger.x += tmp.x - lastPosLink.x;
            posTigger.y += tmp.y - lastPosLink.y;
            lastPosLink = tmp;
        } else {
            destroy();
        }
    }

    protected void execute() {
        destroy();
    }

    protected Trigger(String zname, int i, int j, int posx, int posy, int r, int zmin, int zmax, int ziaHumans, boolean bSortie, int zAvionMin, int zProba, String zslink, String sTextDisplay, int zTextDuree) {
        declanche = false;
        bIsEnter = false;
        name = zname;
        if (j >= 0)
            timeout = (long) j * 60L * 1000L;
        else
            timeout = -1L;
        army = i;
        posTigger = new Point2d(posx, posy);
        lastPosLink = null;
        rayon = r;
        altiMin = zmin;
        altiMax = zmax;
        iaHumans = ziaHumans;
        bTSortie = bSortie;
        avionMin = zAvionMin;
        proba = zProba;
        sLink = zslink;
        textDisplay = sTextDisplay;
        textDuree = zTextDuree;
        altiMsg = -1;
        World.cur().triggersGuard.addTrigger(this);
    }

    public static final void create(String s) {
        if (s == null || s.length() == 0)
            return;
        NumberTokenizer numbertokenizer = new NumberTokenizer(s);
        String name = numbertokenizer.next(null);
        int type = 0;
        if (name.toLowerCase().indexOf("_trigger") > 0)
            type = numbertokenizer.next(0);
        else
            type = Integer.parseInt(name);
        int army = numbertokenizer.next(1, 1, 2);
        boolean bTimeOut = numbertokenizer.next(0) == 1;
        int intTimeOut = numbertokenizer.next(0, 0, 720);
        if (!bTimeOut)
            intTimeOut = -1;
        int posx = numbertokenizer.next(0);
        int posy = numbertokenizer.next(0);
        int r = numbertokenizer.next(1000, 1, 0x186a0);
        int zmin = numbertokenizer.next(0, 0, 20000);
        int zmax = numbertokenizer.next(10000, 0, 20000);
        String s1 = null;
        if (type != 3)
            s1 = numbertokenizer.next(null);
        int zAppliesFor = numbertokenizer.next(0, 0, 11);
        boolean bSortie = numbertokenizer.next(0) == 1;
        int zAvionMin = numbertokenizer.next(1, 1, 1000);
        int zProba = numbertokenizer.next(100, 0, 100);
        int zAltiDiff = 0;
        if (type == 2)
            zAltiDiff = numbertokenizer.next(0, -10000, 10000);
        boolean bLink = numbertokenizer.next(0) == 1;
        String slink = null;
        if (bLink)
            slink = numbertokenizer.next(null);
        int zTextDuree = numbertokenizer.next(5, 1, 60);
        String sTextDisplay = "";
        while (numbertokenizer.hasMoreElements()) {
            sTextDisplay = sTextDisplay + numbertokenizer.next(null);
            if (numbertokenizer.hasMoreElements())
                sTextDisplay = sTextDisplay + " ";
        }
        switch (type) {
            default:
                break;

            case 0:
                new TriNewAircraftAir(name, army, intTimeOut, posx, posy, r, s1, zmin, zmax, zAppliesFor, bSortie, zAvionMin, zProba, slink, sTextDisplay, zTextDuree);
                break;

            case 1:
                new TriNewAircraftSol(name, army, intTimeOut, posx, posy, r, s1, zmin, zmax, zAppliesFor, bSortie, zAvionMin, zProba, slink, sTextDisplay, zTextDuree);
                break;

            case 2:
                if (zAppliesFor == 0)
                    zAppliesFor = 4;
                new TriNewAircraftAirLevel(name, army, intTimeOut, posx, posy, r, s1, zmin, zmax, zAppliesFor, bSortie, zAvionMin, zProba, zAltiDiff, slink, sTextDisplay, zTextDuree);
                break;

            case 3:
                new TriNewMessage(name, army, intTimeOut, posx, posy, r, zmin, zmax, zAppliesFor, bSortie, zAvionMin, zProba, slink, sTextDisplay, zTextDuree);
                break;
        }
    }

    public String getTarget() {
        return null;
    }

    public double getAlti() {
        return -1D;
    }

    private static void subString(StringBuffer stringbuffer, String s, int i, int j) {
        while (i < j)
            stringbuffer.append(s.charAt(i++));
    }

    protected String prepareTextArmy(int i) {
        String s = (Army.name(i) + ">").toUpperCase();
        String s1 = "NONE>";
        int k = textDisplay.length();
        StringBuffer stringbuffer = new StringBuffer();
        if (k == 0)
            return "";
        int l = textDisplay.indexOf("<ARMY ");
        int i1 = 0;
        if (l >= 0)
            do {
                i1 = textDisplay.indexOf("</ARMY>", l);
                if (i1 == -1)
                    i1 = k;
                l += 6;
                if (textDisplay.startsWith(s, l)) {
                    l += s.length();
                    subString(stringbuffer, textDisplay, l, i1);
                } else if (textDisplay.startsWith(s1, l)) {
                    l += s1.length();
                    subString(stringbuffer, textDisplay, l, i1);
                }
            } while ((l = textDisplay.indexOf("<ARMY ", l)) >= 0);
        else
            return UnicodeTo8bit.load(textDisplay);
        return UnicodeTo8bit.load(stringbuffer.toString());
    }

    public static String prepareTextKeyWords(String s, Point2d pos, int alti) {
        int k = s.length();
        if (k == 0)
            return "";
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
                            stringbuffer.append(((alti + 50) / 100) * 100 + " m");
                            break;

                        case 2:
                            stringbuffer.append((int) (((double) alti * 3.28084D + 250D) / 500D) * 500 + " ft");
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
        StringBuffer s = new StringBuffer();
        if (d > 260D)
            s.append(c).append(c1);
        else
            s.append(c1);
        return s.append("-").append((int) Math.ceil(d2)).toString();
    }

    protected Point2d getPosLink() {
        Actor actorlink = Actor.getByName(sLink);
        if (actorlink == null)
            return null;
        int i = -1;
        if (actorlink instanceof Wing) {
            Actor actor2;
            do {
                i++;
                actor2 = ((Wing) actorlink).airc[i];
            } while (i < 3 && (actor2 == null || !actor2.isAlive()));
            if (i < 3)
                actorlink = actor2;
            else
                return null;
        }
        return new Point2d(actorlink.pos.getAbsPoint().x, actorlink.pos.getAbsPoint().y);
    }

    protected void doSendMsg(boolean bLink) {
        if (textDisplay != "" && textDisplay != null) {
            Point2d point2d = new Point2d(bLink ? getPosLink() : posTigger);
            if (Main.cur().netServerParams.isMaster())
                ((NetUser) NetEnv.host()).replicateTriggerMsg(prepareTextArmy(1), prepareTextArmy(2), (float) point2d.x, (float) point2d.y, altiMsg, textDuree);
            if (Config.isUSE_RENDER())
                HUD.addMsgToWaitingList(prepareTextKeyWords(prepareTextArmy(World.getPlayerArmy()), point2d, altiMsg), textDuree * 1000);
        }
    }

    public static final int VERSION = 12;
    protected String        name;
    protected long          timeout;
    protected int           army;
    protected Point2d       posTigger;
    protected int           rayon;
    protected int           altiMin;
    protected int           altiMax;
    public boolean          declanche;
    protected int           iaHumans;
    protected boolean       bTSortie;
    protected boolean       bIsEnter;
    protected int           avionMin;
    protected int           proba;
    protected String        sLink;
    private Point2d         lastPosLink;
    protected String        textDisplay;
    protected int           textDuree;
    protected int           altiMsg;
}
