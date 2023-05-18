/* IL-2 HUD on bottom right corner mod
 * by SAS~Storebror
 * Released under the DWTFYWWI license
 *
 * Changes are marked with "TODO" tags *
 */

package com.maddox.il2.game;

import java.io.IOException;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.order.Order;
import com.maddox.il2.game.order.OrderAnyone_Help_Me;
import com.maddox.il2.game.order.OrderVector_To_Home_Base;
import com.maddox.il2.game.order.OrderVector_To_Target;
import com.maddox.il2.game.order.OrdersTree;
import com.maddox.il2.net.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;
import com.maddox.sas1946.il2.util.Reflection;

public class HUD {
    static class Ptr {

        public void set(float f, float f1, int i, float f2, float f3) {
            x = f;
            y = f1;
            color = i;
            alpha = f2;
            angle = (float) ((double) (f3 * 180F) / Math.PI);
        }

        float x;
        float y;
        int color;
        float alpha;
        float angle;

        public Ptr(float f, float f1, int i, float f2, float f3) {
            set(f, f1, i, f2, f3);
        }
    }

    static class StatUser {

        NetUser user;
        int iNum;
        String sNum;
        int iPing;
        String sPing;
        int iScore;
        String sScore;
        int iArmy;
        String sArmy;
        Aircraft aAircraft;
        String sAircraft;
        String sAircraftType;

        StatUser() {
        }
    }

    static class MsgLine {

        String msg;
        int iActor;
        int army;
        long time0;
        int len;

        MsgLine(String s, int i, int j, int k, long l) {
            msg = s;
            len = i;
            iActor = j;
            army = k;
            time0 = l;
        }
    }

    public static int drawSpeed() {
        return Main3D.cur3D().hud.iDrawSpeed;
    }

    public static void setDrawSpeed(int i) {
        Main3D.cur3D().hud.iDrawSpeed = i;
    }

    public static int drawSpeedMax() {
        return Main3D.cur3D().hud.MAX_iDrawSpeed;
    }

    public String DecStr(float Value)
    {
        String VRounded = Double.toString(Value + 0.05F);
        int Pos = VRounded.indexOf('.');
        return VRounded.substring(0, Pos + 2);
    }

    public void GetHUDUnits()
    {
        bSI = bShowSIToo;
        bIsSI = false;
        String s;
        switch(iDrawSpeed)
        {
        case 4: // '\004'
        default:
            s = ".si";
            bSI = false;
            bIsSI = true;
            break;

        case 2: // '\002'
        case 5: // '\005'
            s = ".gb";
            break;

        case 3: // '\003'
        case 6: // '\006'
            s = ".us";
            break;
        }
        sTAS = iDrawSpeed < 4 ? "" : " TAS";
        try
        {
            renderSpeedSubstrings[0][0] = resLog.getString("HDG");
            renderSpeedSubstrings[1][0] = resLog.getString("ALT");
            renderSpeedSubstrings[2][0] = resLog.getString("SPD");
            renderSpeedSubstrings[3][0] = resLog.getString("G");
            renderSpeedSubstrings[0][1] = resLog.getString("HDG" + s);
            renderSpeedSubstrings[1][1] = resLog.getString("ALT" + s);
            renderSpeedSubstrings[2][1] = resLog.getString("SPD" + s);
            renderSpeedSubstrings[3][1] = resLog.getString("Ga");
        }
        catch(Exception exception)
        {
            renderSpeedSubstrings[0][0] = "HDG";
            renderSpeedSubstrings[1][0] = "Alt";
            renderSpeedSubstrings[2][0] = "Spd";
            renderSpeedSubstrings[3][0] = "G";
            renderSpeedSubstrings[0][1] = "\260";
            renderSpeedSubstrings[1][1] = "";
            renderSpeedSubstrings[2][1] = "";
            renderSpeedSubstrings[3][1] = "";
        }
        try
        {
            renderSpeedSubstrings[4][0] = resLog.getString("Ail.Trim");
            renderSpeedSubstrings[5][0] = resLog.getString("Ele.Trim");
            renderSpeedSubstrings[6][0] = resLog.getString("Rud.Trim");
            renderSpeedSubstrings[7][0] = resLog.getString("Flaps");
            renderSpeedSubstrings[8][0] = resLog.getString("");
            renderSpeedSubstrings[9][0] = resLog.getString("TAS");
            renderSpeedSubstrings[10][0] = resLog.getString("AoA");
            renderSpeedSubstrings[11][0] = resLog.getString("VSpeed");
            renderSpeedSubstrings[12][0] = resLog.getString("RPM");
            renderSpeedSubstrings[13][0] = resLog.getString("Temp");
            renderSpeedSubstrings[14][0] = resLog.getString("HP");
            renderSpeedSubstrings[15][0] = resLog.getString("WEIGHT");
            renderSpeedSubstrings[16][0] = resLog.getString("DRAG");
            renderSpeedSubstrings[17][0] = resLog.getString("Maneuver");
            renderSpeedSubstrings[18][0] = resLog.getString("Throttle");
            renderSpeedSubstrings[4][1] = resLog.getString("Ail.Trim" + s);
            renderSpeedSubstrings[5][1] = resLog.getString("Ele.Trim" + s);
            renderSpeedSubstrings[6][1] = resLog.getString("Rud.Trim" + s);
            renderSpeedSubstrings[7][1] = resLog.getString("Flaps" + s);
            renderSpeedSubstrings[8][1] = resLog.getString("");
            renderSpeedSubstrings[9][1] = resLog.getString("TAS" + s);
            renderSpeedSubstrings[10][1] = resLog.getString("AoA" + s);
            renderSpeedSubstrings[11][1] = resLog.getString("VSpeed" + s);
            renderSpeedSubstrings[12][1] = resLog.getString("RPM" + s);
            renderSpeedSubstrings[13][1] = resLog.getString("Temp" + s);
            renderSpeedSubstrings[14][1] = resLog.getString("HP" + s);
            renderSpeedSubstrings[15][1] = resLog.getString("WEIGHT" + s);
            renderSpeedSubstrings[16][1] = resLog.getString("DRAG" + s);
            renderSpeedSubstrings[17][1] = resLog.getString("Maneuver" + s);
            renderSpeedSubstrings[18][1] = resLog.getString("Throttle" + s);
        }
        catch(Exception exception)
        {
            renderSpeedSubstrings[4][0] = "Ail:";
            renderSpeedSubstrings[5][0] = "Ele:";
            renderSpeedSubstrings[6][0] = "Rud:";
            renderSpeedSubstrings[7][0] = "Flaps:";
            renderSpeedSubstrings[8][0] = "";
            renderSpeedSubstrings[9][0] = "TAS";
            renderSpeedSubstrings[10][0] = "AoA:";
            renderSpeedSubstrings[11][0] = "VSpeed:";
            renderSpeedSubstrings[12][0] = "RPM:";
            renderSpeedSubstrings[13][0] = "Temp:";
            renderSpeedSubstrings[14][0] = "Power:";
            renderSpeedSubstrings[15][0] = "Weight:";
            renderSpeedSubstrings[16][0] = "Drag:";
            renderSpeedSubstrings[17][0] = "Maneuver:";
            renderSpeedSubstrings[18][0] = "Throttle:";
            renderSpeedSubstrings[4][1] = "%";
            renderSpeedSubstrings[5][1] = "%";
            renderSpeedSubstrings[6][1] = "%";
            renderSpeedSubstrings[7][1] = "%";
            renderSpeedSubstrings[8][1] = "";
            renderSpeedSubstrings[9][1] = "";
            renderSpeedSubstrings[10][1] = "\260";
            renderSpeedSubstrings[11][1] = bIsSI ? "m/s" : "ft/s";
            renderSpeedSubstrings[12][1] = "";
            renderSpeedSubstrings[13][1] = bIsSI ? "\260C" : "\260F";
            renderSpeedSubstrings[14][1] = "HP";
            renderSpeedSubstrings[15][1] = bIsSI ? "kg" : "lb";
            renderSpeedSubstrings[16][1] = "";
            renderSpeedSubstrings[17][1] = "";
            renderSpeedSubstrings[18][1] = "%";
        }
        lastDrawSpeed = iDrawSpeed;
    }

    private final void renderSpeed() {
       // changed by western   //       if(!Actor.isValid(World.getPlayerAircraft()))
        if(!((main3d.viewActor() instanceof Aircraft) || (main3d.viewActor() instanceof Bomb) || (main3d.viewActor() instanceof Rocket) || (main3d.viewActor() instanceof RocketBomb)))
            return;
        if(!Actor.isValid(main3d.viewActor()) && !Actor.isValid(World.getPlayerAircraft()))
            return;
        if (iDrawSpeed == 0) return;
        if (!bDrawAllMessages) return;
        if (Main.cur().netServerParams != null && !Main.cur().netServerParams.isShowSpeedBar()) return;
        TTFont ttfont = null;
        if (bDrawDashBoard) ttfont = TTFont.font[4];
        else ttfont = TTFont.font[1];
        int i = ttfont.height();
        int col = COLOR_SPD_REDo;
        int colpl = COLOR_SPD_RED;
        int k;
        boolean flag = false;
        if(bUseColor)
        {
            if(World.getPlayerAircraft() != null)
                colpl = World.getPlayerAircraft().getArmy() != 1 ? COLOR_SPD_BLUE : COLOR_SPD_RED;
        }
        if(main3d.viewActor() instanceof Aircraft)
        {
            float f = ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getLoadDiff();
            if(f <= ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getLimitLoad() * 0.25F && f > ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getLimitLoad() * 0.1F)
            {
                flag = true;
                cnt = 0;
                timeLoadLimit = 0L;
            } else
            if(f <= ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getLimitLoad() * 0.1F && com.maddox.rts.Time.current() < timeLoadLimit)
                flag = false;
            else
            if(f <= ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getLimitLoad() * 0.1F && com.maddox.rts.Time.current() >= timeLoadLimit)
            {
                flag = true;
                cnt++;
                if(cnt == 22)
                {
                    timeLoadLimit = 125L + com.maddox.rts.Time.current();
                    cnt = 0;
                }
            } else
            {
                cnt = 0;
                timeLoadLimit = 0L;
            }
        }
        String s;
        double d;
        boolean bPlayerLive = Actor.isValid(World.getPlayerAircraft());
        int plhdg = 0;
        int plalt = 0;
        int plspd = 0;
        float fs;
        float fgs;
        float fa;
        Vector3d vtemp = new Vector3d();

        if(bPlayerLive)
        {
            plhdg = (int)(World.getPlayerFM().Or.getYaw() + 0.5F);
            plhdg = plhdg > 90 ? 450 - plhdg : 90 - plhdg;
        }

        if(main3d.viewActor() instanceof Bomb)
        {
            fa = (float) main3d.viewActor().pos.getAbsPoint().z;
            fs = (float) ((Bomb) (main3d.viewActor())).getSpeed((Vector3d) null);
            main3d.viewActor().pos.speed(vtemp);
            fgs = (float) vtemp.length();
            d = main3d.viewActor().pos.getAbsPoint().z;
        } else
        if(main3d.viewActor() instanceof RocketBomb)
        {
            fa = (float) main3d.viewActor().pos.getAbsPoint().z;
            fs = (float) ((RocketBomb) (main3d.viewActor())).getSpeed((Vector3d) null);
            main3d.viewActor().pos.speed(vtemp);
            fgs = (float) vtemp.length();
            d = main3d.viewActor().pos.getAbsPoint().z;
        } else
        if(main3d.viewActor() instanceof Rocket)
        {
            fa = (float) main3d.viewActor().pos.getAbsPoint().z;
            fs = (float) ((Rocket) (main3d.viewActor())).getSpeed((Vector3d) null);
            main3d.viewActor().pos.speed(vtemp);
            fgs = (float) vtemp.length();
            d = main3d.viewActor().pos.getAbsPoint().z;
        } else
        if(!bMixedMessage)
        {
            fa = ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getAltitude();
            fs = ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getSpeed();
            main3d.viewActor().pos.speed(vtemp);
            fgs = (float) vtemp.length();
            d = ((Tuple3d) (((SndAircraft) ((Aircraft)main3d.viewActor())).FM).Loc).z;
        } else
        if(main3d.viewActor() instanceof Aircraft)
        {
            fa = ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getAltitude();
            fs = ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getSpeed();
            main3d.viewActor().pos.speed(vtemp);
            fgs = (float) vtemp.length();
            d = ((Tuple3d) (((FlightModelMain) (((SndAircraft) ((Aircraft)main3d.viewActor())).FM)).Loc)).z;
        } else
        {
            fa = 0.0F;
            fs = 0.0F;
            fgs = 0.0F;
            d = 0.0D;
        }
        int l0;
        int i10;
        int iIAS;
        int iTAS;
        int iGS;
        String speedunit;
        switch(iDrawSpeed)
        {
        default:
            if(bPlayerLive)
            {
                plalt = (int)(World.getPlayerFM().getAltitude() * speedbarMultiplierMeters * (float)speedbarAltMeters * 0.1F) * 10;
                plspd = (int)((3.6F * Pitot.Indicator((float)World.getPlayerFM().Loc.z, World.getPlayerFM().getSpeed()) * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F) * 0.1F) * 10;
            }
            iTAS = (int)(3.6F * fs * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            iIAS = (int)(3.6F * Pitot.Indicator((float)d, fs) * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            iGS = (int)(3.6F * fgs * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            l0 = 0;
            i10 = 0;
            speedunit = "km/h";
            break;

        case 2: // '\002'
            if(bPlayerLive)
            {
                plalt = (int)((3.28084F * World.getPlayerFM().getAltitude() * speedbarMultiplierFeet * (float)speedbarAltFeet + 0.5F) * 0.1F) * 10;
                plspd = (int)((1.943845F * Pitot.Indicator((float)World.getPlayerFM().Loc.z, World.getPlayerFM().getSpeed()) * speedbarMultiplierKnots * (float)speedbarSpdKnots + 0.5F) * 0.1F) * 10;
            }
            iTAS = (int)(1.943845F * fs * speedbarMultiplierKnots * (float)speedbarSpdKnots + 0.5F);
            iIAS = (int)(1.943845F * Pitot.Indicator((float)d, fs) * speedbarMultiplierKnots * (float)speedbarSpdKnots + 0.5F);
            iGS = (int)(1.943845F * fgs * speedbarMultiplierKnots * (float)speedbarSpdKnots + 0.5F);
            l0 = (int)(fa * speedbarMultiplierMeters * (float)speedbarAltMeters + 0.5F);
            i10 = (int)(3.6F * Pitot.Indicator((float)d, fs) * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            speedunit = "knots";
            break;

        case 3: // '\003'
            if(bPlayerLive)
            {
                plalt = (int)((3.28084F * World.getPlayerFM().getAltitude() * speedbarMultiplierFeet * (float)speedbarAltFeet + 0.5F) * 0.1F) * 10;
                plspd = (int)((2.236936F * Pitot.Indicator((float)World.getPlayerFM().Loc.z, World.getPlayerFM().getSpeed()) * speedbarMultiplierMPH * (float)speedbarSpdMPH + 0.5F) * 0.1F) * 10;
            }
            iTAS = (int)(2.236936F * fs * speedbarMultiplierMPH * (float)speedbarSpdMPH + 0.5F);
            iIAS = (int)(2.236936F * Pitot.Indicator((float)d, fs) * speedbarMultiplierMPH * (float)speedbarSpdMPH + 0.5F);
            iGS = (int)(2.236936F * fgs * speedbarMultiplierMPH * (float)speedbarSpdMPH + 0.5F);
            l0 = (int)(fa * speedbarMultiplierMeters * (float)speedbarAltMeters + 0.5F);
            i10 = (int)(3.6F * Pitot.Indicator((float)d, fs) * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            speedunit = "MPH";
            break;

        case 4: // '\004'
            if(bPlayerLive)
            {
                plalt = (int)((World.getPlayerFM().getAltitude() * speedbarMultiplierMeters * (float)speedbarAltMeters + 0.5F) * 0.1F) * 10;
                plspd = (int)((3.6F * World.getPlayerFM().getSpeed() * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F) * 0.1F) * 10;
            }
            iTAS = (int)(3.6F * fs * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            iIAS = (int)(3.6F * Pitot.Indicator((float)d, fs) * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            iGS = (int)(3.6F * fgs * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            l0 = 0;
            i10 = 0;
            speedunit = "km/h";
            break;

        case 5: // '\005'
            if(bPlayerLive)
            {
                plalt = (int)((3.28084F * World.getPlayerFM().getAltitude() * speedbarMultiplierFeet * (float)speedbarAltFeet + 0.5F) * 0.1F) * 10;
                plspd = (int)((1.943845F * World.getPlayerFM().getSpeed() * speedbarMultiplierKnots * (float)speedbarSpdKnots + 0.5F) * 0.1F) * 10;
            }
            iTAS = (int)(1.943845F * fs * speedbarMultiplierKnots * (float)speedbarSpdKnots + 0.5F);
            iIAS = (int)(1.943845F * Pitot.Indicator((float)d, fs) * speedbarMultiplierKnots * (float)speedbarSpdKnots + 0.5F);
            iGS = (int)(1.943845F * fgs * speedbarMultiplierKnots * (float)speedbarSpdKnots + 0.5F);
            l0 = (int)(fa * speedbarMultiplierMeters * (float)speedbarAltMeters + 0.5F);
            i10 = (int)(3.6F * fs * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            speedunit = "Knots";
            break;

        case 6: // '\006'
            if(bPlayerLive)
            {
                plalt = (int)((3.28084F * World.getPlayerFM().getAltitude() * speedbarMultiplierFeet * (float)speedbarAltFeet + 0.5F) * 0.1F) * 10;
                plspd = (int)((2.236936F * World.getPlayerFM().getSpeed() * speedbarMultiplierMPH * (float)speedbarSpdMPH + 0.5F) * 0.1F) * 10;
            }
            iTAS = (int)(2.236936F * fs * speedbarMultiplierMPH * (float)speedbarSpdMPH + 0.5F);
            iIAS = (int)(2.236936F * Pitot.Indicator((float)d, fs) * speedbarMultiplierMPH * (float)speedbarSpdMPH + 0.5F);
            iGS = (int)(2.236936F * fgs * speedbarMultiplierMPH * (float)speedbarSpdMPH + 0.5F);
            l0 = (int)(fa * speedbarMultiplierMeters * (float)speedbarAltMeters + 0.5F);
            i10 = (int)(3.6F * fs * speedbarMultiplierKMH * (float)speedbarSpdKMH + 0.5F);
            speedunit = "MPH";
            break;


        }
        float machIAS = (float)(Math.floor((Pitot.Indicator((float)d, fs) / Atmosphere.sonicSpeed((float)d))*100F)) / 100F;
        float machTAS = (float)(Math.floor((fs / Atmosphere.sonicSpeed((float)d))*100F)) / 100F;
        if(iDrawSpeed != lastDrawSpeed)
            GetHUDUnits();
        if((main3d.viewActor() instanceof Bomb) || (main3d.viewActor() instanceof Rocket) || (main3d.viewActor() instanceof RocketBomb))
        {
            Orientation orient = new Orientation();
            Point3d location = new Point3d();

            main3d.viewActor().pos.getAbs(location, orient);
            int yaw = (int)orient.getYaw();
            int pitch = (int)orient.getPitch();
            for(;pitch > 180; pitch -= 360) ;
            for(;pitch < -180; pitch += 360) ;
            int roll = (int)orient.getRoll();
            int locx = (int)location.x;
            int locy = (int)location.y;
            int locz = (int)fa;
            if(sbLocWay >= 1.0F)
            {
                boolean bMissile = (main3d.viewActor() instanceof Missile);
                boolean bGuidedBomb = false;

                if(bMissile)
                {
                    Missile misact = (Missile)main3d.viewActor();
                    int theaddiff = 0;
                    int distance = 0;

                    if(misact.getMissileTarget() != null)
                    {
                        Orientation vorient = new Orientation();
                        Point3d vlocation = new Point3d();
                        Actor vc = misact.getMissileTarget();
                        vc.pos.getAbs(vlocation, vorient);
                        int tgtx = (int)vlocation.x;
                        int tgty = (int)vlocation.y;
                        int tgtz = (int)vlocation.z;
                        int tgtspeed = (int) (vc.getSpeed((Vector3d) null) * 3.6F);
                        int tgthead = (int)vorient.getYaw();
                        tgthead = ((tgthead > 90)? 450 - tgthead : 90 - tgthead);
                        Vector3d tvec = new Vector3d((double)(tgtx - locx), (double)(tgty - locy) , 0.0D);
                        tvec.normalize();
                        Orient oTemp = new Orient();
                        oTemp.setAT0(tvec);
                        int thead = (int)(oTemp.getAzimut() + 90F);
                        for(; thead > 360; thead -= 360) ;
                        for(; thead < -360; thead += 360) ;
                        theaddiff = thead - ((yaw > 90)? 450 - yaw : 90 - yaw);
                        if( theaddiff > 180 ) theaddiff -= 360;
                        if( theaddiff < -180 ) theaddiff += 360;
                        distance = (int) location.distance(vlocation);
                        String classnameFull = vc.getClass().getName();
                        int idot = classnameFull.lastIndexOf('.');
                        int idol = classnameFull.lastIndexOf('$');
                        if (idot < idol) idot = idol;
                        String classnameSection = classnameFull.substring(idot + 1);
                        ttfont.output(COLOR_SPD_REDo, 5F, 5 + (3 + (int)sbLocWay) * i, 0.0F, "Target(" + tgtx + " , " + tgty + " , " + tgtz + ") , Head:" + tgthead + " , Speed:" + tgtspeed + "km/h , Class:" + classnameSection);
                    }
                    else
                    if(misact.getMissileTargetPoint3dAbs() != null && misact.getDetectorType() == 9)
                    {                   // because of "Missile.DETECTOR_TYPE_LASER = 9" is protected
                        int tgtx = (int) misact.getMissileTargetPoint3dAbs().x;
                        int tgty = (int) misact.getMissileTargetPoint3dAbs().y;
                        int tgtz = (int) misact.getMissileTargetPoint3dAbs().z;
                        Vector3d tvec = new Vector3d((double)(tgtx - locx), (double)(tgty - locy) , 0.0D);
                        tvec.normalize();
                        Orient oTemp = new Orient();
                        oTemp.setAT0(tvec);
                        int thead = (int)(oTemp.getAzimut() + 90F);
                        for(; thead > 360; thead -= 360) ;
                        for(; thead < -360; thead += 360) ;
                        theaddiff = thead - ((yaw > 90)? 450 - yaw : 90 - yaw);
                        if( theaddiff > 180 ) theaddiff -= 360;
                        if( theaddiff < -180 ) theaddiff += 360;
                        distance = (int) location.distance(misact.getMissileTargetPoint3dAbs());
                        ttfont.output(COLOR_SPD_REDo, 5F, 5 + (3 + (int)sbLocWay) * i, 0.0F, "Target(" + tgtx + " , " + tgty + " , " + tgtz + ")");
                    }
                    else
                    {
                        ttfont.output(COLOR_SPD_REDo, 5F, 5 + (3 + (int)sbLocWay) * i, 0.0F, "Target: None");
                    }
                    ttfont.output(COLOR_SPD_REDo, 5F, 5 + (4 + (int)sbLocWay) * i, 0.0F, "Locate(" + locx + " , " + locy + " , " + locz + ") - Alt:" + (locz - (int)Engine.land().HQ_Air(locx, locy)) + "m - Distance:" + distance + "m , HeadDiff:" + theaddiff);
                }
                else
                {
                    ttfont.output(COLOR_SPD_REDo, 5F, 5 + (4 + (int)sbLocWay) * i, 0.0F, "Locate(" + locx + " , " + locy + " , " + locz + ") - Alt:" + (locz - (int)Engine.land().HQ_Air(locx, locy)) + "m");
                }
            }
            if(sbOrient >= 1.0F)
                ttfont.output(COLOR_SPD_REDo, 5F, 5 + (3 + (int)sbOrient) * i, 0.0F, "Yaw:" +  ((yaw > 90)? 450 - yaw : 90 - yaw) + ", Pitch:" + pitch + ", Roll:" + (360 - roll));
            if(sbIASTAS >= 1.0F)
                ttfont.output(COLOR_SPD_REDo, 5F, 5 + (3 + (int)sbIASTAS) * i, 0.0F, "TAS " + iTAS + speedunit + " (Mach " + Float.toString(machTAS) + ") ,  IAS " + iIAS + speedunit + " ,  GS " + iGS + speedunit);
        } else
        if(!bMixedMessage)
      //  if(!bMixedMessage || main3d.viewActor() == World.getPlayerAircraft())
        {
            if(bUseColor)
            {
                if(main3d.viewActor() == World.getPlayerAircraft())
                    col = colpl;
                else
                    col = main3d.viewActor().getArmy() != 1 ? COLOR_SPD_BLUEo : COLOR_SPD_REDo;
            }
            if(!World.cur().diffCur.NoSpeedBar || bMessageFull)
            {
              //  if(main3d.viewActor() == World.getPlayerAircraft())
                if(true)
                {
                    switch(sbGMode)
                    {
                    case 0: // '\0'
                        break;

                    case 2: // '\002'
                        ttfont.output(flag ? 0xc00000a0 : 0xc010a010, 5F, 5 + 3 * i, 0.0F, DecStr(((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getOverload()) + " " + renderSpeedSubstrings[3][0]);
                        break;

                    case 3: // '\003'
                        ttfont.output(flag ? 0xc00000a0 : 0xc010a010, 5F, 5 + 3 * i, 0.0F, DecStr(((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getOverload()) + " " + renderSpeedSubstrings[3][0] + " (" + DecStr(((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getOverload() / ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getLimitLoad()) + ")");
                        break;

                    case 1: // '\001'
                    default:
                        if(flag)
                            ttfont.output(0xc00000a0, 5F, 5 + 3 * i, 0.0F, renderSpeedSubstrings[3][0]);
                        break;
                    }
                    boolean bSA = false;
                  //  if(!main3d.isViewOutside() && !main3d.cockpitCur.isNullShow())
                    if(true)
                    {
                        if(BombSightAssist > 0 && (World.getPlayerAircraft() instanceof TypeBomber) && !main3d.isViewOutside() && !main3d.cockpitCur.isNullShow())
                        {
                            int pf = ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.AS.astatePilotFunctions[Main3D.cur3D().cockpitCurIndx()];
                            if(BombSightAssist == 3 || BombSightAssist == 2 && (pf == 2 || pf == 1) || BombSightAssist == 1 && pf == 2)
                            {
                                bSA = true;
                                Point3d point3d = ((Actor) (World.getPlayerAircraft())).pos.getAbsPoint();
                                float Alt = (float)(((Tuple3d) (point3d)).z - World.land().HQ(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y));
                                float Time = (float)Math.sqrt(0.20394F * Alt);
                                float Dist = ((SndAircraft) ((Aircraft)main3d.viewActor())).FM.getSpeed() * Time;
                                float Angle = Alt <= 0.0F ? 90F : (float)Math.toDegrees(Math.atan(Dist / Alt));
                                String Distance;
                                if(bIsSI)
                                    Distance = (int)(Dist + 0.5F) + " " + "m";
                                else
                                    Distance = (int)(Dist * 0.9144F + 0.5F) + " " + "yd";
                                if(BombSightAssistConf == 3)
                                    ttfont.output(0xc010fefe, 5F, 5 + 6 * i, 0.0F, "Distance: " + Distance);
                                if(BombSightAssistConf > 1)
                                    ttfont.output(0xc010fefe, 5F, 5 + 5 * i, 0.0F, "Time to Ground: " + DecStr(Time) + " " + "s");
                                HookPilot hookpilot = HookPilot.current;
                                ttfont.output(0xc010fefe, 5F, 5 + 4 * i, 0.0F, (hookpilot.isAim() ? "[Aim] " : "") + "Bombsight Angle: " + DecStr(Angle) + " " + "\260");
                            }
                        }
                        if(!bSA)
                            try
                            {
                                FlightModel fm = ((SndAircraft) ((Aircraft)main3d.viewActor())).FM;
                                float trAil = (int)(fm.CT.getTrimAileronControl() * 10000F) / 100F;
                                float trEl = (int)(fm.CT.getTrimElevatorControl() * 10000F) / 100F;
                                float trRud = (int)(fm.CT.getTrimRudderControl() * 10000F) / 100F;
                                int flap = (int)(fm.CT.getFlap() * 100F);
                                int flapSw = fm.CT.FlapsControlSwitch;
                                int varwing = (int)(fm.CT.getVarWing() * 100F);
                                int varwingcon = (int)(fm.CT.VarWingControl * 100F);
                                int varwingSw = fm.CT.VarWingControlSwitch;
                                float fric0 = (float)(Math.floor(fm.Gears.fric_pub[0] / 2D) * 2D);
                                float fricF0 = (float)(Math.floor(fm.Gears.fricF_pub[0] / 2D) * 2D);
                                float fricR0 = (float)(Math.floor(fm.Gears.fricR_pub[0] / 2D) * 2D);
                                float fric1 = (float)(Math.floor(fm.Gears.fric_pub[1] / 2D) * 2D);
                                float fricF1 = (float)(Math.floor(fm.Gears.fricF_pub[1] / 2D) * 2D);
                                float fricR1 = (float)(Math.floor(fm.Gears.fricR_pub[1] / 2D) * 2D);
                                float fric2 = (float)(Math.floor(fm.Gears.fric_pub[2] / 2D) * 2D);
                                float fricF2 = (float)(Math.floor(fm.Gears.fricF_pub[2] / 2D) * 2D);
                                float fricR2 = (float)(Math.floor(fm.Gears.fricR_pub[2] / 2D) * 2D);
                                float vs = (int)(fm.getVertSpeed() * 100F) / 100F;
                                float aoa = (int)fm.getAOA();
                                float hp = 0.0F;
                                String rpm = "";
                                String temp = "";
                                String temp2 = "";
                                String throttle = "";
                                String proppitch = " , Pitch:";
                                String enginestage = " , Stage:";
                                String thrust = " , Thrust:";
                                String readyness = " , Ready:";
                                for(int num = 0; num < fm.EI.getNum(); num++)
                                {
                                    hp += fm.EI.engines[num].getw();
                                    if(num > 0)
                                    {
                                        rpm = rpm + ":";
                                        temp = temp + ":";
                                        temp2 = temp2 + ":";
                                        throttle = throttle + "%:";
                                        proppitch = proppitch + ":";
                                        enginestage = enginestage + ":";
                                        thrust = thrust + ":";
                                        readyness = readyness + ":";
                                    }
                                    rpm = rpm + (int)(fm.EI.engines[num].getRPM() + 0.5F);
                                    temp = temp + (bIsSI ? (int)(fm.EI.engines[num].tWaterOut + 0.5F) : (int)(fm.EI.engines[num].tWaterOut * 1.8F + 32.5F));
                                    temp2 = temp2 + (bIsSI ? (int)(fm.EI.engines[num].tOilOut + 0.5F) : (int)(fm.EI.engines[num].tOilOut * 1.8F + 32.5F));
                                    throttle = throttle + (int)(fm.EI.engines[num].getControlThrottle()*100);
                                    proppitch = proppitch + (int)(fm.EI.engines[num].getControlProp() * 100F);
                                    enginestage = enginestage + fm.EI.engines[num].getStage();
                                    thrust = thrust + (int)(fm.EI.engines[num].getThrustOutput() * 100F);
                                    readyness = readyness + (int)(fm.EI.engines[num].getReadyness() * 100F);
                                }

                                FuelTank fuelTanks[];
                                fuelTanks = fm.CT.getFuelTanks();
                                String droptank = "";
                                if(fuelTanks.length == 0 || fuelTanks[0] == null || fm.M.bFuelTanksDropped)
                                    droptank = "No Droptanks";
                                else
                                {
                                    droptank = "Droptank: ";
                                    for(int num = 0; num < fuelTanks.length; num++)
                                    {
                                        if(num > 0)
                                        {
                                            droptank +=  " , ";
                                        }
                                        droptank += (float)(Math.floor(fuelTanks[num].checkFuel() * 10F)) / 10F + "kg / " + (float)(Math.floor((fuelTanks[num].checkFuel() + fuelTanks[num].checkFreeTankSpace()) * 10F)) / 10F;
                                    }
                                }
                                Polares polares = (Polares)Reflection.getValue(fm, "Wing");
                                String squredata = "CxMin=" + Math.floor(Reflection.getFloat(polares, "CxMin")  * 1000F) / 1000F + " (CxMin_0=" + Math.floor(polares.CxMin_0 * 1000F) / 1000F + " , CxMin_1=" + Math.floor(polares.CxMin_1 * 1000F) / 1000F + ") ; Cy0=" + Math.floor(Reflection.getFloat(polares, "Cy0")  * 1000F) / 1000F + " (Cy0_0=" + Math.floor(polares.Cy0_0 * 1000F) / 1000F + " , Cy0_1=" + Math.floor(polares.Cy0_1 * 1000F) / 1000F + ")";

//                                fm.Sq.getClass();
                                float dragsta = (float)(Math.floor((fm.Sq.dragAirbrakeCx + 0.12F + fm.Sq.dragFuselageCx + fm.Sq.dragParasiteCx + fm.Sq.dragProducedCx + 0.06F) * 1000F ) / 1000F);
                                float dragdyn = (float)(Math.floor((fm.GearCX * fm.CT.getGearR() + fm.GearCX * fm.CT.getGearL() + 2.0F * fm.Sq.dragAirbrakeCx * fm.CT.getAirBrake() + fm.radiatorCX * (fm.EI.getRadiatorPos() + fm.CT.getCockpitDoor() + fm.CT.BayDoorControl) + fm.Sq.dragChuteCx * fm.CT.getDragChute() + 0.12F + fm.Sq.dragFuselageCx + fm.Sq.dragParasiteCx + fm.Sq.dragProducedCx + 0.06F) * 1000F ) / 1000F);
                                int airbrakecon = (int)(fm.CT.AirBrakeControl * 100F);
                                int airbrake = (int)(fm.CT.getAirBrake() * 100F);
                                int brakecon = (int)(fm.CT.BrakeControl * 100F);
                                int brake = (int)(fm.CT.getBrake() * 100F);
                                int brakeLeftcon = (int)(fm.CT.BrakeLeftControl * 100F);
                                int brakeLeft = (int)(fm.CT.getBrakeLeft() * 100F);
                                int brakeRightcon = (int)(fm.CT.BrakeRightControl * 100F);
                                int brakeRight = (int)(fm.CT.getBrakeRight() * 100F);
                                boolean brakeShoe = fm.brakeShoe;
                                int weight = (int)((bIsSI ? fm.M.mass : (double)fm.M.mass * 2.2046000000000001D) + 0.5D);
                                int fuel = (int)(fm.M.fuel + 0.5D);
                                int maxfuel = (int)(fm.M.maxFuel + 0.5D);
                                int nitro = (int)(fm.M.nitro + 0.5D);
                                int maxnitro = (int)(fm.M.maxNitro + 0.5D);
                                int maneuver = (int)((Maneuver)fm).getmaneuver();
                                int submaneuver = (int)((Maneuver)fm).getsubmaneuver();
                                int speedMode = (int)((Maneuver)fm).getspeedMode();
                                int smConstSpeed = (int)((Maneuver)fm).getsmConstSpeed();
                                int smConstPower = (int)(((Maneuver)fm).getsmConstPower() * 100F);
                                boolean TaxiMode = (boolean)((Maneuver)fm).getTaxiMode();
                                int yaw = (int)((FlightModel) ((Aircraft)main3d.viewActor()).FM).Or.getYaw();
                                int pitch = (int)((FlightModel) ((Aircraft)main3d.viewActor()).FM).Or.getPitch();
                                int roll = (int)((FlightModel) ((Aircraft)main3d.viewActor()).FM).Or.getRoll();
                                int locx = (int)((Maneuver) ((Aircraft)main3d.viewActor()).FM).getACx();
                                int locy = (int)((Maneuver) ((Aircraft)main3d.viewActor()).FM).getACy();
                                int locz = (int)fa;
                                int wayx = (int)((Maneuver) ((Aircraft)main3d.viewActor()).FM).getWAYPOINTx();
                                int wayy = (int)((Maneuver) ((Aircraft)main3d.viewActor()).FM).getWAYPOINTy();
                                int wayz = (int)((Maneuver) ((Aircraft)main3d.viewActor()).FM).getWAYPOINTz();
                                int wayspeed = (int)(Math.floor(((Maneuver) ((Aircraft)main3d.viewActor()).FM).getWAYPOINTspeed() * 3.6F));
                                int waycur = ((Maneuver) ((Aircraft)main3d.viewActor()).FM).getWAYPOINTcurrentnum();
                                int wayaction = ((Maneuver) ((Aircraft)main3d.viewActor()).FM).AP.way.curr().Action;
                                int distance = (int)((Maneuver) ((Aircraft)main3d.viewActor()).FM).getWAYPOINTdistance();
                                Vector3d tvec = new Vector3d((double)(wayx - locx), (double)(wayy - locy) , 0.0D);
                                tvec.normalize();
                                Orient oTemp = new Orient();
                                oTemp.setAT0(tvec);
                                int thead = (int)(oTemp.getAzimut() + 90F);
                                for(; thead > 360; thead -= 360) ;
                                for(; thead < -360; thead += 360) ;
                                int theaddiff = thead - ((yaw > 90)? 450 - yaw : 90 - yaw);
                                if( theaddiff > 180 ) theaddiff -= 360;
                                if( theaddiff < -180 ) theaddiff += 360;
                                int rudder = (int)(((Maneuver) ((Aircraft)main3d.viewActor()).FM).CT.RudderControl * 100F);
                                int rudder2 = (int)(((Maneuver) ((Aircraft)main3d.viewActor()).FM).CT.getRudder() * 100F);
                                int elevator = (int)(((Maneuver) ((Aircraft)main3d.viewActor()).FM).CT.ElevatorControl * 100F);
                                int elevator2 = (int)(((Maneuver) ((Aircraft)main3d.viewActor()).FM).CT.getElevator() * 100F);
                                int aileron = (int)(((Maneuver) ((Aircraft)main3d.viewActor()).FM).CT.AileronControl * 100F);
                                int aileron2 = (int)(((Maneuver) ((Aircraft)main3d.viewActor()).FM).CT.getAileron() * 100F);
                                if(sbLocWay >= 1.0F)
                                {
                                    ttfont.output(col, 5F, 5 + (4 + (int)sbLocWay) * i, 0.0F, "Locate(" + locx + " , " + locy + " , " + locz + ") - Alt:" + (locz - (int)Engine.land().HQ_Air(locx, locy)) + "m - Distance:" + distance + "m , Head:" + thead + " , HeadDiff:" + theaddiff);
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbLocWay) * i, 0.0F, "Waypoint(" + wayx + " , " + wayy + " , " + wayz + ") , Speed:" + wayspeed + "km/h , Cur:" + waycur + " , Action:" + wayaction);
                                }
                                if(sbAIManeuver >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbAIManeuver) * i, 0.0F, renderSpeedSubstrings[17][0] + " " + maneuver + " , sub:" + submaneuver + " , speedMode:" + speedMode + " , spd:" + smConstSpeed + "m/s , pwr:" + smConstPower + " " + ((TaxiMode)?", TaxiMode":" ") + ", GrTask " + ((Maneuver) fm).Group.grTask + ", gTgtPref " + ((Maneuver) fm).Group.gTargetPreference + renderSpeedSubstrings[17][1]);
                                if(sbThrottle >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbThrottle) * i, 0.0F, "Rudder:" + rudder2 + "/" + rudder + ", Elevator:" + elevator2 + "/" + elevator + ", Aileron:" + aileron2 + "/" + aileron + ", " + renderSpeedSubstrings[18][0] + throttle + renderSpeedSubstrings[18][1]);
                                if(sbOrient >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbOrient) * i, 0.0F, "Yaw:" +  ((yaw > 90)? 450 - yaw : 90 - yaw) + ", Pitch:" + ((pitch > 180)? pitch - 360 : pitch) + ", Roll:" + (360 - roll));
                                if(sbHPSet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbHPSet) * i, 0.0F, renderSpeedSubstrings[14][0] + " " + (int)(hp * 10F) + " " + renderSpeedSubstrings[14][1] + thrust + readyness);
                                if(sbWeightSet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbWeightSet) * i, 0.0F, renderSpeedSubstrings[15][0] + " " + weight + " " + renderSpeedSubstrings[15][1] + " (Fuel " + fuel + "kg / " + maxfuel + ", Nitro " + nitro + "kg / " + maxnitro + ")" );
                                if(sbDragSet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbDragSet) * i, 0.0F, renderSpeedSubstrings[16][0] + " " + Float.toString(dragsta) + "Sta , " + Float.toString(dragdyn) + "Dyn " + renderSpeedSubstrings[16][1] + " , AirBrake:" + airbrake + "/" + airbrakecon + " , WheelBrake:" + brake + "/" + brakecon + " (Left:" + brakeLeft + "/" + brakeLeftcon + ", Right:" + brakeRight + "/" + brakeRightcon + ")" + (brakeShoe ? " CHOCK" : ""));
                                if(sbFlapsSet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbFlapsSet) * i, 0.0F, renderSpeedSubstrings[7][0] + " " + flap + " " + renderSpeedSubstrings[7][1] + ", FlapSw=" + flapSw + ", VarWing=" + varwing + "/" + varwingcon + ", VarWingSw=" + varwingSw);
                                if(sbVSpeedSet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbVSpeedSet) * i, 0.0F, renderSpeedSubstrings[11][0] + " " + DecStr(vs * (bIsSI ? 1.0F : 3.28084F)) + " " + renderSpeedSubstrings[11][1] + ", " + DecStr((int)(vs * 3.28084F * 60F)) + "ft/min");
                                if(sbAoASet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbAoASet) * i, 0.0F, renderSpeedSubstrings[10][0] + " " + DecStr(aoa) + " " + renderSpeedSubstrings[10][1] + " , fric0=" + fric0 + ",fricF0=" + fricF0 + ",fricR0=" + fricR0 + " , fric1=" + fric1 + ",fricF1=" + fricF1 + ",fricR1=" + fricR1 + " , fric2=" + fric2 + ",fricF2=" + fricF2 + ",fricR2=" + fricR2);
                                if(sbTrimSet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbTrimSet) * i, 0.0F, "Trim (" + renderSpeedSubstrings[4][0] + " " + trAil + renderSpeedSubstrings[4][1] + ", " + renderSpeedSubstrings[5][0] + " " + trEl + renderSpeedSubstrings[5][1] + ", " + renderSpeedSubstrings[6][0] + " " + trRud + renderSpeedSubstrings[6][1] + ")");
                                if(sbRPMSet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbRPMSet) * i, 0.0F, renderSpeedSubstrings[12][0] + " " + rpm + " " + renderSpeedSubstrings[12][1] + proppitch + enginestage);
                                if(sbTempSet >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbTempSet) * i, 0.0F, renderSpeedSubstrings[13][0] + " " + temp + " " + renderSpeedSubstrings[13][1] + ", " + temp2 + " " + renderSpeedSubstrings[13][1]);
                                if(sbIASTAS >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbIASTAS) * i, 0.0F, "TAS " + iTAS + speedunit + " (Mach " + Float.toString(machTAS) + ") ,  IAS " + iIAS + speedunit + " ,  GS " + iGS + speedunit);
                                if(sbDroptank >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbDroptank) * i, 0.0F, droptank);
                                if(sbSqure >= 1.0F)
                                    ttfont.output(col, 5F, 5 + (3 + (int)sbSqure) * i, 0.0F, squredata);
                            }
                            catch(Exception exception1)
                            {
                              //   exception1.printStackTrace();
                            }
                    }
                }
            }
        } else
        if((main3d.viewActor() instanceof Aircraft) && !Mission.isNet())
        {
            if(bUseColor)
            {
                if(main3d.viewActor() == World.getPlayerAircraft())
                    col = colpl;
                else
                    col = main3d.viewActor().getArmy() != 1 ? COLOR_SPD_BLUEo : COLOR_SPD_REDo;
            } else
            {
                col = COLOR_SPD_REDo;
            }
        }

        if(bPlayerLive)
        {
            ttfont.output(colpl, 5F, 5F, 0.0F, renderSpeedSubstrings[0][0] + " " + plhdg + " " + renderSpeedSubstrings[0][1]);
            if(!World.cur().diffCur.NoSpeedBar || bMessageFull)
            {
                ttfont.output(colpl, 5F, 5 + i, 0.0F, renderSpeedSubstrings[1][0] + " " + plalt + " " + renderSpeedSubstrings[1][1]);
                ttfont.output(colpl, 5F, 5 + i + i, 0.0F, renderSpeedSubstrings[2][0] + " " + plspd + " " + renderSpeedSubstrings[2][1] + sTAS);
            }
        }
    }

    public void clearSpeed() {
        iDrawSpeed = speedbarUnits;
    }

    private void initSpeed() {
        iDrawSpeed = speedbarUnits;
    }

    public static void order(Order aorder[]) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            Main3D.cur3D().hud._order(aorder);
            return;
        }
    }

    public void _order(Order aorder[]) {
        if (aorder == null) {
            order = null;
            return;
        }
        order = new Order[aorder.length];
        orderStr = new String[aorder.length];
        int i = World.getPlayerArmy();
        for (int j = 0; j < order.length; j++) {
            order[j] = aorder[j];
            if (aorder[j] == null || order[j].name(i) == null) continue;
            String s = order[j].name(i);
            String s1 = null;
            String s2 = World.getPlayerLastCountry();
            if (s2 != null) try {
                s1 = resOrder.getString(s + "_" + s2);
            } catch (Exception exception) {
            }
            if (s1 == null) try {
                s1 = resOrder.getString(s);
            } catch (Exception exception1) {
                s1 = s;
            }
            orderStr[j] = s1;
        }

    }

    private void renderOrder() {
        if (order == null) return;
        if (!bDrawAllMessages) return;
        TTFont ttfont = TTFont.font[1];
        int i = (int) ((double) viewDX * 0.05D);
        int j = ttfont.height();
        int k = viewDY - 2 * ttfont.height();
        String s = null;
        int l = k;
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;
        boolean flag1 = false;
        for (int k1 = 0; k1 < order.length; k1++) {
            if (orderStr[k1] != null) {
                if (order[k1] instanceof OrderAnyone_Help_Me) flag = true;
                if (Main3D.cur3D().ordersTree.frequency() == null) flag1 = true;
                if (s != null) drawOrder(s, i, l, j1 != 0 ? j1 : -1, i1, flag1);
                j1 = k1;
                s = orderStr[k1];
                l = k;
                i1 = order[k1].attrib();
                if (((order[k1] instanceof OrderVector_To_Home_Base) || (order[k1] instanceof OrderVector_To_Target)) && Main.cur().mission.zutiRadar_DisableVectoring) flag1 = true;
                else flag1 = false;
            }
            k -= j;
        }

        if (Main3D.cur3D().ordersTree.frequency() == null) flag1 = true;
        if (s != null) drawOrder(s, i, l, 0, i1, flag1);
        if (flag) {
            String as[] = Main3D.cur3D().ordersTree.getShipIDs();
            for (int l1 = 0; l1 < as.length; l1++) {
                if (l1 == 0 && as[l1] != null) {
                    k -= j;
                    k -= j;
                    drawShipIDs(resOrder.getString("ShipIDs"), i, k);
                    k -= j;
                }
                if (as[l1] != null) {
                    drawShipIDs(as[l1], i, k);
                    k -= j;
                }
            }

            drawWeaponsInfo();
        }
    }

    private void drawWeaponsInfo() {
        int i = 0;
        TTFont ttfont = TTFont.font[1];
        int j = 0xff0000ff;
        int k = (int) ((double) viewDX * 0.6D);
        int l = ttfont.height();
        int i1 = viewDY - 2 * ttfont.height();
        if (World.cur().diffCur.BombFuzes) {
            Map map = AircraftLH.getInfoList();
            Object aobj[] = map.keySet().toArray();
            for (int j1 = 0; j1 < aobj.length; j1++) {
                String s = (String) aobj[j1];
                String as[] = (String[]) (String[]) map.get(s);
                for (int l1 = 0; l1 < as.length - 1; l1++) {
                    String s2 = as[l1];
                    if (l1 == 1 && (drawSpeed() == 2 || drawSpeed() == 3) && as[4] != null) s2 = as[4];
                    if (s2 != null) {
                        ttfont.output(j, k, i1 - i * l, 0.0F, s2);
                        i++;
                    }
                }

                i++;
            }

            if (aobj.length > 0) i++;
        }
        if (World.cur().diffCur.FragileTorps) {
            Map map1 = Torpedo.getInfoList();
            Object aobj1[] = map1.keySet().toArray();
            for (int k1 = 0; k1 < aobj1.length; k1++) {
                String s1 = (String) aobj1[k1];
                String as1[] = (String[]) (String[]) map1.get(s1);
                for (int i2 = 0; i2 < as1.length - 3; i2++) {
                    String s3 = as1[i2];
                    if (i2 == 1 && (drawSpeed() == 2 || drawSpeed() == 3)) s3 = as1[3];
                    else if (i2 == 2 && drawSpeed() == 2) s3 = as1[4];
                    else if (i2 == 2 && drawSpeed() == 3) s3 = as1[5];
                    if (s3 != null) {
                        ttfont.output(j, k, i1 - i * l, 0.0F, s3);
                        i++;
                    }
                }

                i++;
            }

        }
    }

    private void drawShipIDs(String s, int i, int j) {
        int k = 0xff0000ff;
        TTFont ttfont = TTFont.font[1];
        ttfont.output(k, i, j, 0.0F, s);
    }

    private void drawOrder(String s, int i, int j, int k, int l, boolean flag) {
        int i1 = 0xff0000ff;
        if ((l & 1) != 0) i1 = 0xff00007f;
        else if ((l & 2) != 0) i1 = 0xff007fff;
        TTFont ttfont = TTFont.font[1];
        if (flag) i1 = 0x7f7f7f7f;
        HotKeyEnv hotkeyenv = HotKeyEnv.env("orders");
        HashMapInt hashmapint = hotkeyenv.all();
        HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null);
        int j1 = 0;
        do {
            if (hashmapintentry == null) break;
            String s1 = (String) (String) hashmapintentry.getValue();
            if (s1.equals("order" + k)) {
                j1 = hashmapintentry.getKey();
                break;
            }
            hashmapintentry = hashmapint.nextEntry(hashmapintentry);
        } while (true);
        String s2 = "";
        if ((j1 & 0xffff0000) == 0) s2 = resName(VK.getKeyText(j1));
        else s2 = resName(VK.getKeyText(j1 >> 16 & 0xffff)) + " " + resName(VK.getKeyText(j1 & 0xffff));
        if (k >= 0) ttfont.output(i1, i, j, 0.0F, "" + s2 + ". " + s);
        else ttfont.output(i1, i, j, 0.0F, s);
    }

    private String resName(String s) {
        if (resControl == null) return s;
        try {
            return resControl.getString(s);
        } catch (Exception exception) {
        }
        return s;
    }

    public void clearOrder() {
        order = null;
    }

    private void initOrder() {
        clearOrder();
        resOrder = ResourceBundle.getBundle("i18n/hud_order", RTSConf.cur.locale, LDRres.loader());
        resControl = ResourceBundle.getBundle("i18n/controls", RTSConf.cur.locale, LDRres.loader());
    }

    public static void message(int ai[], int i, int j, boolean flag) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            Main3D.cur3D().hud._message(ai, i, j, flag);
            return;
        }
    }

    public void _message(int ai[], int i, int j, boolean flag) {
        if (!bDrawVoiceMessages) return;
        if (Config.cur.bNoSubTitles) return;
        if (i < 1) return;
        if (i > 9) return;
        if (j < 1) return;
        if (j > 2) return;
        if (ai == null) return;
        TTFont ttfont = TTFont.font[4];
        for (int k = 0; k < ai.length && ai[k] != 0; k++) {
            String s = Voice.vbStr[ai[k]];
            try {
                String s1 = resMsg.getString(s);
                if (s1 != null) s = s1;
            } catch (Exception exception) {
            }
            for (StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens(); flag = false) {
                String s2 = stringtokenizer.nextToken();
                int l = (int) ttfont.width(s2);
                if (msgLines.size() == 0) {
                    msgLines.add(new MsgLine(s2, l, i, j, Time.current()));
                    continue;
                }
                MsgLine msgline1 = (MsgLine) msgLines.get(msgLines.size() - 1);
                if (msgline1.iActor == i && msgline1.army == j && !flag) {
                    int i1 = msgline1.len + msgSpaceLen + l;
                    if (i1 < msgDX) {
                        msgline1.msg = msgline1.msg + " " + s2;
                        msgline1.len = i1;
                    } else {
                        msgLines.add(new MsgLine(s2, l, i, j, 0L));
                    }
                } else {
                    msgLines.add(new MsgLine(s2, l, i, j, 0L));
                }
            }

        }

        while (msgLines.size() > msgSIZE) {
            msgLines.remove(0);
            MsgLine msgline = (MsgLine) msgLines.get(0);
            msgline.time0 = Time.current();
        }
    }

    private int msgColor(int i, int j) {
        return msgColor[j - 1][i - 1];
    }

    private void renderMsg() {
        if (!bDrawVoiceMessages) return;
        if (!bDrawAllMessages) return;
        int i = msgLines.size();
        if (i == 0) return;
        MsgLine msgline = (MsgLine) msgLines.get(0);
        long l = msgline.time0 + (long) (msgline.msg.length() * 250);
        if (l < Time.current()) {
            msgLines.remove(0);
            if (--i == 0) return;
            MsgLine msgline1 = (MsgLine) msgLines.get(0);
            msgline1.time0 = Time.current();
        }
        TTFont ttfont = TTFont.font[4];
        int j = msgX0;
        int k = msgY0 + msgDY;
        for (int i1 = 0; i1 < i; i1++) {
            MsgLine msgline2 = (MsgLine) msgLines.get(i1);
            ttfont.output(msgColor(msgline2.iActor, msgline2.army), j, k, 0.0F, msgline2.msg);
            k -= ttfont.height();
        }

    }

    public void clearMsg() {
        msgLines.clear();
    }

    public void resetMsgSizes() {
        clearMsg();
        TTFont ttfont = TTFont.font[1];
        msgX0 = (int) ((double) viewDX * 0.3D);
        msgDX = (int) ((double) viewDX * 0.6D);
        msgDY = ttfont.height() * subTitlesLines;
        if (msgDY > (int) ((double) viewDY * 0.9D)) msgDY = (int) ((double) viewDY * 0.9D);
        int i = msgDY / ttfont.height();
        if (i == 0) i = 1;
        msgDY = ttfont.height() * i;
        msgSIZE = i;
        msgY0 = (int) ((double) viewDY * 0.95D) - msgDY;
        msgSpaceLen = Math.round(ttfont.width(" "));
    }

    private void initMsg() {
        resetMsgSizes();
        resMsg = ResourceBundle.getBundle("i18n/hud_msg", RTSConf.cur.locale, LDRres.loader());
    }

    public static void training(String s) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            Main3D.cur3D().hud._training(s);
            return;
        }
    }

    public void _training(String s) {
        trainingLines.clear();
        if (s == null) return;
        TTFont ttfont = TTFont.font[2];
        StringTokenizer stringtokenizer = new StringTokenizer(s);
        do {
            if (!stringtokenizer.hasMoreTokens()) break;
            String s1 = stringtokenizer.nextToken();
            int i = (int) ttfont.width(s1);
            if (trainingLines.size() == 0) {
                trainingLines.add(s1);
                continue;
            }
            String s2 = (String) trainingLines.get(trainingLines.size() - 1);
            int j = (int) ttfont.width(s2);
            int k = j + trainingSpaceLen + i;
            if (k < trainingDX) {
                trainingLines.set(trainingLines.size() - 1, s2 + " " + s1);
                continue;
            }
            if (trainingLines.size() >= trainingSIZE) break;
            trainingLines.add(s1);
        } while (true);
    }

    private void renderTraining() {
        int i = trainingLines.size();
        if (i == 0) return;
        TTFont ttfont = TTFont.font[2];
        int j = trainingX0;
        int k = trainingY0 + trainingDY;
        for (int l = 0; l < i; l++) {
            String s = (String) trainingLines.get(l);
            ttfont.output(0xff0000ff, j, k, 0.0F, s);
            k -= ttfont.height();
        }

    }

    public void clearTraining() {
        trainingLines.clear();
    }

    public void resetTrainingSizes() {
        clearTraining();
        TTFont ttfont = TTFont.font[2];
        trainingX0 = (int) ((double) viewDX * 0.3D);
        trainingDX = (int) ((double) viewDX * 0.5D);
        trainingY0 = (int) ((double) viewDY * 0.5D);
        trainingDY = (int) ((double) viewDY * 0.45D);
        int i = trainingDY / ttfont.height();
        if (i == 0) i = 1;
        trainingDY = ttfont.height() * i;
        trainingSIZE = i;
        trainingSpaceLen = Math.round(ttfont.width(" "));
    }

    private void initTraining() {
        resetTrainingSizes();
    }

    public static void intro(String s) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            Main3D.cur3D().hud._intro(s);
            return;
        }
    }

    public void _intro(String s) {
        if (s == null) {
            logIntro = null;
            return;
        } else {
            logIntro = s;
            return;
        }
    }

    public static void introESC(String s) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            Main3D.cur3D().hud._introESC(s);
            return;
        }
    }

    public void _introESC(String s) {
        if (s == null) {
            logIntroESC = null;
            return;
        } else {
            logIntroESC = s;
            return;
        }
    }

    public static int makeIdLog() {
        return idLog++;
    }

    public static void log(String s, Object aobj[]) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            log(0, s, aobj);
            return;
        }
    }

    public static void log(int i, String s, Object aobj[]) {
        if (!Config.isUSE_RENDER()) return;
        if (Main3D.cur3D().gameTrackPlay() != null) return;
        if (Main3D.cur3D().gameTrackRecord() != null && aobj != null && aobj.length == 1 && (aobj[0] instanceof Integer)) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(0);
            netmsgguaranted.writeInt(i);
            netmsgguaranted.write255(s);
            netmsgguaranted.writeInt(((Integer) aobj[0]).intValue());
            Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
        } catch (Exception exception) {
        }
        Main3D.cur3D().hud._log(i, s, aobj);
    }

    private boolean showAlways(String s) {
        return s.startsWith("Beacon") || s.startsWith("Bombsight") || s.startsWith("EL_AZ") || s.startsWith("TOKG");
    }

    public void _log(int i, String s, Object aobj[]) {
        if (Config.cur.bNoHudLog && !showAlways(s)) return;
        int j = __log(i, s);
        String s1 = null;
        try {
            s1 = resLog.getString(s);
        } catch (Exception exception) {
            s1 = s;
        }
        logBufStr[j] = MessageFormat.format(s1, aobj);
    }

    public static void log(String s) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            log(0, s);
            return;
        }
    }

    public static void log(int i, String s) {
        log(i, s, true);
    }

    public static void log(int i, String s, boolean flag) {
        if (!Config.isUSE_RENDER()) return;
        if (flag) {
            if (Main3D.cur3D().gameTrackPlay() != null) return;
            if (Main3D.cur3D().gameTrackRecord() != null) try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(1);
                netmsgguaranted.writeInt(i);
                netmsgguaranted.write255(s);
                Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
            } catch (Exception exception) {
            }
        }
        Main3D.cur3D().hud._log(i, s);
    }

    public void _log(int i, String s) {
        if (Config.cur.bNoHudLog && !showAlways(s)) return;
        int j = __log(i, s);
        try {
            logBufStr[j] = resLog.getString(s);
        } catch (Exception exception) {
            logBufStr[j] = s;
        }
    }

    public static void logRightBottom(String s) {
        if (!Config.isUSE_RENDER()) return;
        if (Main3D.cur3D().gameTrackPlay() != null) return;
        if (Main3D.cur3D().gameTrackRecord() != null) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(2);
            netmsgguaranted.write255(s != null ? s : "");
            Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
        } catch (Exception exception) {
        }
        Main3D.cur3D().hud._logRightBottom(s);
    }

    public void _logRightBottom(String s) {
        if (Config.cur.bNoHudLog) return;
        if (s == null) {
            logRightBottom = null;
            return;
        }
        try {
            logRightBottom = resLog.getString(s);
        } catch (Exception exception) {
            logRightBottom = s;
        }
        logRightBottomTime = Time.current();
    }

    public static void logCenter(String s) {
        if (!Config.isUSE_RENDER()) return;
        if (Main3D.cur3D().gameTrackPlay() != null) return;
        if (Main3D.cur3D().gameTrackRecord() != null) try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(3);
            netmsgguaranted.write255(s != null ? s : "");
            Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
        } catch (Exception exception) {
        }
        Main3D.cur3D().hud._logCenter(s);
    }

    public void _logCenter(String s) {
        if (s == null) {
            logCenter = null;
            return;
        }
        try {
            logCenter = resLog.getString(s);
        } catch (Exception exception) {
            logCenter = s;
        }
        logCenterTime = Time.current();
    }

    public static void logCoopTimeStart(long l) {
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            Main3D.cur3D().hud._logCoopTimeStart(l);
            return;
        }
    }

    public void _logCoopTimeStart(long l) {
        bCoopTimeStart = true;
        coopTimeStart = l;
    }

    public boolean netInputLog(int i, NetMsgInput netmsginput) throws IOException {
        switch (i) {
        default:
            break;

        case 0: // '\0'
            int j = netmsginput.readInt();
            String s2 = netmsginput.read255();
            Object aobj[] = new Object[1];
            aobj[0] = new Integer(netmsginput.readInt());
            _log(j, s2, aobj);
            break;

        case 1: // '\001'
            int k = netmsginput.readInt();
            String s3 = netmsginput.read255();
            _log(k, s3);
            break;

        case 2: // '\002'
            String s = netmsginput.read255();
            if ("".equals(s)) s = null;
            _logRightBottom(s);
            break;

        case 3: // '\003'
            String s1 = netmsginput.read255();
            if ("".equals(s1)) s1 = null;
            _logCenter(s1);
            break;
        }
        return true;
    }

    public void clearLog() {
        logRightBottom = null;
        logPtr = 0;
        logLen = 0;
        logCenter = null;
        logIntro = null;
        logIntroESC = null;
        bCoopTimeStart = false;
    }

    private void initLog() {
        clearLog();
        resLog = ResourceBundle.getBundle("i18n/hud_log", RTSConf.cur.locale, LDRres.loader());
        fntCenter = TTFont.font[2];
    }

    private void renderLog() {
        long l = Time.current();
        if (bCoopTimeStart) {
            int i = (int) (coopTimeStart - Time.currentReal());
            if (i < 0) bCoopTimeStart = false;
            else if (bDrawAllMessages) {
                TTFont ttfont5 = fntCenter;
                String s = "" + (i + 500) / 1000;
                float f3 = ttfont5.width(s);
                ttfont5.output(0xff00ffff, ((float) viewDX - f3) / 2.0F, (float) viewDY * 0.75F, 0.0F, s);
            }
        } else if (logIntro != null) {
            TTFont ttfont = fntCenter;
            float f = ttfont.width(logIntro);
            int i1 = 0xff000000;
            ttfont.output(i1, ((float) viewDX - f) / 2.0F, (float) viewDY * 0.75F, 0.0F, logIntro);

            // TODO: Replace the final "5000" by the static field "logCenterTimeLife"
        } else if (logCenter != null) if (l > logCenterTime + logCenterTimeLife) logCenter = null;
        // ---

            else if (bDrawAllMessages) {
                TTFont ttfont1 = fntCenter;
                float f1 = ttfont1.width(logCenter);
                int j1 = 0xff0000ff;

                // TODO: Replace the final "5000" by the static field "logCenterTimeLife"
                int i2 = 255 - (int) (((double) (l - logCenterTime) / (double) logCenterTimeLife) * 255D);
                // ---

                j1 |= i2 << 8;
                ttfont1.output(j1, ((float) viewDX - f1) / 2.0F, (float) viewDY * 0.75F, 0.0F, logCenter);
            }
        if (logIntroESC != null) {
            TTFont ttfont2 = TTFont.font[0];
            float f2 = ttfont2.width(logIntroESC);
            byte byte0 = -1;
            ttfont2.output(byte0, ((float) viewDX - f2) / 2.0F, (float) viewDY * 0.05F, 0.0F, logIntroESC);
        }
        if (!Main3D.cur3D().aircraftHotKeys.isAfterburner()) logRightBottom = null;
        if (logRightBottom != null && bDrawAllMessages) {
            TTFont ttfont3 = TTFont.font[1];

            // TODO: Let Log start 2 pixels off the right side of the screen.
            // Original code line commented out for reference
            // int j = (int) ((double) viewDX * 0.95D);
            int j = viewDX - 2;
            // ---

            int k1 = (int) ttfont3.width(logRightBottom);

            // TODO: Let Log start 3 pixels off the bottom of the screen.
            // Original code line commented out for reference
            // int j2 = (int) ((double) viewDY * 0.45D - (double) (lenLogBuf * ttfont3.height()));
            int j2 = 3;
            // ---

            // TODO: During NTRK playback, leave bottom line gap for recorded time display
            if (NetMissionTrack.isPlaying()) j2 += ttfont3.height();
            // ---

            // TODO: New Bottom Log Line Color fading code
            float fadePercent = 2.0F * (float) ((Time.current() - logRightBottomTime) % logTimeFire) / (float) logTimeFire;
            ttfont3.output(crossFadeColor(colorLogBottom1, colorLogBottom2, fadePercent), j - k1, j2, 0.0F, logRightBottom);
            // ---

        }
        if (logLen == 0) return;

        // TODO: Replace final values (10000, 3) by the static fields "logTimeLife" and "lenLogBuf"
        for (; logLen > 0 && l >= logTime[logPtr] + logTimeLife; logLen--)
            logPtr = (logPtr + 1) % lenLogBuf;
        // ---

        if (logLen == 0) return;
        TTFont ttfont4 = TTFont.font[1];

        // TODO: Let Log start 2 pixels off the right side of the screen.
        // Original code line commented out for reference
        // int k = (int) ((double) viewDX * 0.95D);
        int k = viewDX - 2;
        // ---

        int l1 = ttfont4.height();

        // TODO: Let Log start 3 pixels off the bottom of the screen.
        // Original code line commented out for reference
        // int k2 = (int) ((double) viewDY * 0.45D) - (lenLogBuf - logLen) * l1;
        int k2 = 3 + (logLen - 1) * l1;
        // ---

        // TODO: If WEP message is active, leave 1 line gap at the bottom
        if (logRightBottom != null && bDrawAllMessages) k2 += l1;
        // ---

        // TODO: During NTRK playback, leave bottom line gap for recorded time display
        if (NetMissionTrack.isPlaying()) k2 += l1;
        // ---

        for (int i3 = 0; i3 < logLen; i3++) {
            int k3 = (logPtr + i3) % lenLogBuf;

            // TODO: New Log Color Transition / Fading Code with user selectable colors
            int l3;
            if (l < logTime[k3] + logTimeFire) {
                float fadePercent = (float) ((logTime[k3] + logTimeFire) - l) / (float) logTimeFire;
                l3 = crossFadeColor(colorLog2, colorLog1, fadePercent);
            } else {
                float fadePercent = (float) ((logTime[k3] + logTimeLife) - l) / (float) (logTimeLife - logTimeFire);
                l3 = crossFadeColor(colorLog2 & 0x00ffffff, colorLog2, fadePercent);
            }
            // ---

            float f4 = ttfont4.width(logBufStr[k3]);
            if (bDrawAllMessages) ttfont4.output(l3, (float) k - f4, k2, 0.0F, logBufStr[k3]);
            k2 -= l1;
        }

    }

    // TODO: Calculation Algorithm for cross fading between two RGB color values with alpha.
    // "fadePercent" is the percentage of color2 to apply, in the range of 0.0F - 1.0F (0-100%)
    // Negative values for "fadePercent" will be inverted
    // Values from 1.0F to 2.0F will be "mirrored" at 1.0F, e.g. 1.25F becomes 0.75F
    // Values for "fadePercent" are modulo 2.0F
    private static int crossFadeColor(int color1, int color2, float fadePercent) {
        fadePercent %= 2.0F;
        if (fadePercent < 0.0F) fadePercent = -fadePercent;
        if (fadePercent > 1.0F) fadePercent = 2.0F - fadePercent;
        int fadedColor = 0;
        for (int i = 0; i < 4; i++) {
            int shift = i * 8;
            int c1 = (color1 & (0xff << shift)) >>> shift;
            int c2 = (color2 & (0xff << shift)) >>> shift;
            int c3 = c1 + (int) ((float) (c2 - c1) * fadePercent);
            fadedColor |= c3 << shift;
        }
        return fadedColor;
    }

    // ---

    // TODO: Read Hex Values from conf.ini
    private static int getHexFromIni(String section, String value, int defaultValue) {
        int retVal = 0;
        String defaultString = Integer.toHexString(defaultValue);
        String retValString = Config.cur.ini.get(section, value, defaultString);
        try {
            retVal = new BigInteger(retValString, 16).intValue();
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return retVal;
    }
    // ---

    private int __log(int i, String s) {
        if (logLen > 0 && i != 0) {

            // TODO: Replace the final "3" by the static field "lenLogBuf"
            int j = ((logPtr + logLen) - 1) % lenLogBuf;
            // ---

            if (logBufId[j] == i) {
                logTime[j] = Time.current();
                logBuf[j] = s;
                return j;
            }
        }

        // TODO: Replace the final "3" by the static field "lenLogBuf"
        if (logLen >= lenLogBuf) {
            logPtr = (logPtr + 1) % lenLogBuf;
            // ---

            // TODO: Replace the final "2" by the static field "lenLogBuf" - 1
            logLen = lenLogBuf - 1;
            // ---
        }

        // TODO: Replace the final "3" by the static field "lenLogBuf"
        int k = (logPtr + logLen) % lenLogBuf;
        // ---

        logBuf[k] = s;
        logBufId[k] = i;
        logTime[k] = Time.current();
        logLen++;
        return k;
    }

    private void syncStatUser(int i, NetUser netuser, boolean flag) {
        if (i == statUsers.size()) statUsers.add(new StatUser());
        StatUser statuser = (StatUser) statUsers.get(i);
        statuser.user = netuser;
        if (statuser.iNum != i + 1 || statuser.sNum == null) {
            statuser.iNum = i + 1;
            statuser.sNum = statuser.iNum + ".";
        }
        if (statuser.iPing != netuser.ping || statuser.sPing == null) {
            statuser.iPing = netuser.ping;
            statuser.sPing = "(" + statuser.iPing + ")";
        }
        int j = (int) netuser.stat().score;
        if (statuser.iScore != j || statuser.sScore == null) {
            statuser.iScore = j;
            statuser.sScore = "" + statuser.iScore;
        }
        if (statuser.iArmy != netuser.getArmy() || statuser.sArmy == null) {
            statuser.iArmy = netuser.getArmy();
            statuser.sArmy = "(" + statuser.iArmy + ")" + I18N.army(Army.name(statuser.iArmy));
        }
        if (!Actor.isAlive(statuser.aAircraft) || statuser.aAircraft.netUser() != netuser || statuser.sAircraft == null) {
            Aircraft aircraft = netuser.findAircraft();
            statuser.aAircraft = aircraft;
            if (aircraft == null) {
                statuser.sAircraft = "";
                statuser.sAircraftType = "";
            } else {
                statuser.sAircraft = aircraft.typedName();
                statuser.sAircraftType = I18N.plane(Property.stringValue(aircraft.getClass(), "keyName"));
            }
        }
        if (flag) {
            netuser.stat();
            armyScores = NetUserStat.armyScores;
        }
    }

    private void syncNetStat() {
        syncStatUser(0, (NetUser) NetEnv.host(), true);
        for (int i = 0; i < NetEnv.hosts().size(); i++)
            syncStatUser(i + 1, (NetUser) NetEnv.hosts().get(i), false);

        for (; statUsers.size() > NetEnv.hosts().size() + 1; statUsers.remove(statUsers.size() - 1))
            ;
    }

    private int x1024(float f) {
        return (int) (((float) viewDX / 1024F) * f);
    }

    private int y1024(float f) {
        return (int) (((float) viewDY / 768F) * f);
    }

    public void startNetStat() {
        if (bDrawNetStat) return;
        if (!Mission.isPlaying()) return;
        if (Mission.isSingle()) {
            return;
        } else {
            syncNetStat();
            TTFont ttfont = TTFont.font[1];
            int i = ttfont.height() - ttfont.descender();
            int j = y1024(740F);
            int k = 2 * i;
            pageSizeNetStat = (j - k) / i;
            bDrawNetStat = true;
            return;
        }
    }

    public void stopNetStat() {
        if (!bDrawNetStat) {
            return;
        } else {
            statUsers.clear();
            bDrawNetStat = false;
            pageNetStat = 0;
            return;
        }
    }

    public boolean isDrawNetStat() {
        return bDrawNetStat;
    }

    public void pageNetStat() {
        if (!bDrawNetStat) return;
        pageNetStat++;
        if (pageSizeNetStat * pageNetStat > statUsers.size()) pageNetStat = 0;
    }

    public void renderNetStat() {
        if (!bDrawNetStat) return;
        if (!Mission.isPlaying()) return;
        if (Mission.isSingle()) return;
        TTFont ttfont = TTFont.font[1];
        TTFont ttfont1 = TTFont.font[3];
        if (Main.cur().netServerParams.netStat_DisableStatistics) return;
        int i = ttfont.height() - ttfont.descender();
        int j = y1024(740F);
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        for (int i2 = pageSizeNetStat * pageNetStat; i2 < pageSizeNetStat * (pageNetStat + 1) && i2 < statUsers.size(); i2++) {
            StatUser statuser = (StatUser) statUsers.get(i2);
            int l2 = 0;
            if (Main.cur().netServerParams.netStat_ShowPilotNumber) {
                l2 = (int) ttfont.width(statuser.sNum);
                if (k < l2) k = l2;
            }
            if (Main.cur().netServerParams.netStat_ShowPilotPing) {
                l2 = (int) ttfont1.width(statuser.sPing);
                if (l < l2) l = l2;
            }
            if (Main.cur().netServerParams.netStat_ShowPilotName) {
                l2 = (int) ttfont.width(statuser.user.uniqueName());
                if (i1 < l2) i1 = l2;
            }
            if (Main.cur().netServerParams.netStat_ShowPilotScore) {
                l2 = (int) ttfont.width(statuser.sScore);
                if (j1 < l2) j1 = l2;
            }
            if (Main.cur().netServerParams.netStat_ShowPilotArmy) {
                l2 = (int) ttfont.width(statuser.sArmy);
                if (k1 < l2) k1 = l2;
            }
            if (!Main.cur().netServerParams.netStat_ShowPilotACDesignation) continue;
            l2 = (int) ttfont.width(statuser.sAircraft);
            if (l1 < l2) l1 = l2;
        }

        int j2 = x1024(40F) + k;
        int k2 = j2 + l + x1024(16F);
        int i3 = k2 + i1 + x1024(16F);
        int j3 = i3 + j1 + x1024(16F);
        if (Mission.isCoop()) j3 = i3;
        int k3 = j3 + k1 + x1024(16F);
        int l3 = k3 + l1 + x1024(16F);
        int i4 = j;
        for (int k4 = pageSizeNetStat * pageNetStat; k4 < pageSizeNetStat * (pageNetStat + 1) && k4 < statUsers.size(); k4++) {
            StatUser statuser1 = (StatUser) statUsers.get(k4);
            i4 -= i;
            int j5 = Army.color(statuser1.iArmy);
            if (!Main.cur().netServerParams.netStat_ShowPilotArmy) j5 = -1;
            if (Main.cur().netServerParams.netStat_ShowPilotNumber) ttfont.output(j5, (float) j2 - ttfont.width(statuser1.sNum), i4, 0.0F, statuser1.sNum);
            if (Main.cur().netServerParams.netStat_ShowPilotPing) ttfont1.output(-1, (float) k2 - ttfont1.width(statuser1.sPing) - (float) x1024(4F), i4, 0.0F, statuser1.sPing);
            if (Main.cur().netServerParams.netStat_ShowPilotName) ttfont.output(j5, k2, i4, 0.0F, statuser1.user.uniqueName());
            if (!Mission.isCoop() && Main.cur().netServerParams.netStat_ShowPilotScore) ttfont.output(j5, i3, i4, 0.0F, statuser1.sScore);
            if (Main.cur().netServerParams.netStat_ShowPilotArmy) ttfont.output(j5, j3, i4, 0.0F, statuser1.sArmy);
            if (Main.cur().netServerParams.netStat_ShowPilotACDesignation) ttfont.output(j5, k3, i4, 0.0F, statuser1.sAircraft);
            if (Main.cur().netServerParams.netStat_ShowPilotACType) ttfont.output(j5, l3, i4, 0.0F, statuser1.sAircraftType);
        }

        if (!Mission.isCoop() && Main.cur().netServerParams.netStat_ShowTeamScore) {
            int j4 = j;
            j4 -= i;
            ttfont.output(Army.color(0), x1024(800F), j4, 0.0F, resGUI.getString("netStat.TeamScore"));
            j4 -= i;
            for (int l4 = 1; l4 < armyScores.length; l4++) {
                for (int i5 = 0; i5 < statUsers.size(); i5++) {
                    StatUser statuser2 = (StatUser) statUsers.get(i5);
                    if (statuser2.iArmy == l4) {
                        int k5 = Army.color(l4);
                        j4 -= i;
                        ttfont.output(k5, x1024(800F), j4, 0.0F, resGUI.getString(Army.name(l4)) + ": " + armyScores[l4]);
                    }
                }

            }

        }
    }

    public static void addPointer(float f, float f1, int i, float f2, float f3) {
        Main3D.cur3D().hud._addPointer(f, f1, i, f2, f3);
    }

    private void _addPointer(float f, float f1, int i, float f2, float f3) {
        if (nPointers == pointers.size()) {
            pointers.add(new Ptr(f, f1, i, f2, f3));
        } else {
            Ptr ptr = (Ptr) pointers.get(nPointers);
            ptr.set(f, f1, i, f2, f3);
        }
        nPointers++;
    }

    private void renderPointers() {
        if (nPointers == 0) return;
        float f = (float) viewDX / 1024F;
        float f1 = (float) viewDX / (float) viewDY;
        float f2 = 1.333333F / f1;
        f *= f2;
        int i = IconDraw.scrSizeX();
        int j = IconDraw.scrSizeY();
        for (int k = 0; k < nPointers; k++) {
            Ptr ptr = (Ptr) pointers.get(k);
            int l = (int) (64F * f * ptr.alpha);
            IconDraw.setScrSize(l, l);
            IconDraw.setColor(ptr.color & 0xffffff | (int) (ptr.alpha * 255F) << 24);
            IconDraw.render(spritePointer, ptr.x, ptr.y, 90F - ptr.angle);
        }

        IconDraw.setScrSize(i, j);
        nPointers = 0;
    }

    public void clearPointers() {
        nPointers = 0;
    }

    private void initPointers() {
        spritePointer = Mat.New("gui/game/hud/pointer.mat");
    }

    private void preRenderDashBoard() {
    //    if (!Actor.isValid(World.getPlayerAircraft())) return;
        if (!Actor.isValid(main3d.cockpitCur)) return;
        if (main3d.isViewOutside()) {
        //    if (main3d.viewActor() != World.getPlayerAircraft()) return;
            if (!main3d.cockpitCur.isNullShow()) return;
        } else if (main3d.isViewInsideShow()) return;
        if (!bDrawDashBoard) {
            return;
        } else {
            spriteLeft.preRender();
            spriteRight.preRender();
            meshNeedle1.preRender();
            meshNeedle2.preRender();
            meshNeedle3.preRender();
            meshNeedle4.preRender();
            meshNeedle5.preRender();
            meshNeedle6.preRender();
            meshNeedleMask.preRender();
            return;
        }
    }

    private void renderDashBoard() {
    //    if (!Actor.isValid(World.getPlayerAircraft())) return;
        if (!Actor.isValid(main3d.cockpitCur)) return;
        if (main3d.isViewOutside()) {
        //    if (main3d.viewActor() != World.getPlayerAircraft()) return;
            if (!main3d.cockpitCur.isNullShow()) return;
        } else if (main3d.isViewInsideShow()) return;
        if (!bDrawDashBoard) return;
        float f = viewDX;
        float f1 = viewDY;
        float f2 = f / 1024F;
        float f3 = 1.333333F / (f / f1);
        float f4 = f1 / 768F;
        if (f / f1 < 1.0F) {
            float f5 = 1.333333F;
            f2 = f / (768F * f5);
            f3 = 0.75F / (f / f1);
            f4 = f1 / (1024F * f5);
        }
        float f6 = ((1024F - 256F * f3) * f2 - 768F * f2) * 4F;
        Render.drawTile(0.0F, 0.0F, 256F * f2 * f3, 256F * f4, 0.0F, spriteLeft, -1, 0.0F, 1.0F, 1.0F, -1F);
        Render.drawTile((1024F - 256F * f3) * f2, 0.0F, 256F * f2 * f3, 256F * f4, 0.0F, spriteRight, -1, 0.0F, 1.0F, 1.0F, -1F);
        Point3d point3d = World.getPlayerAircraft().pos.getAbsPoint();
        Orient orient = World.getPlayerAircraft().pos.getAbsOrient();
        float f7 = (float) (point3d.z - World.land().HQ(point3d.x, point3d.y));
        _p.x = 172F * f2 * f3;
        _p.y = 84F * f4;
        _o.set(cvt(f7, 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        meshNeedle2.setPos(_p, _o);
        meshNeedle2.render();
        _o.set(cvt(f7, 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        meshNeedle1.setPos(_p, _o);
        meshNeedle1.render();
        String s = "" + (int) ((double) f7 + 0.5D);
        float f9 = fntLcd.width(s);
        fntLcd.output(-1, 208F * f2 * f3 - f9, 70F * f4, 0.0F, s);
        if (f7 > 90F) meshNeedle5.setScale(90F * f2);
        else meshNeedle5.setScaleXYZ(90F * f2, 90F * f2, cvt(f7, 0.0F, 90F, 13F, 90F) * f2);
        f7 = (float) World.getPlayerAircraft().getSpeed(null);
        f7 *= 3.6F;
        _o.set(cvt(f7, 0.0F, 900F, 0.0F, 270F) + 180F, 0.0F, 0.0F);
        _p.x = 83F * f2 * f3;
        _p.y = 167F * f4;
        meshNeedle2.setPos(_p, _o);
        meshNeedle2.render();
        s = "" + (int) ((double) f7 + 0.5D);
        f9 = fntLcd.width(s);
        fntLcd.output(-1, 104F * f2 * f3 - f9, 135F * f4, 0.0F, s);
        for (f7 = orient.azimut() + 90F; f7 < 0.0F; f7 += 360F)
            ;
        f7 %= 360F;
        _o.set(f7, 0.0F, 0.0F);
        _p.x = 939F * f2 * f3 + f6;
        _p.y = 167F * f4;
        meshNeedle3.setPos(_p, _o);
        meshNeedle3.render();
        s = "" + (int) ((double) f7 + 0.5D);
        f9 = fntLcd.width(s);
        fntLcd.output(-1, (960F * f2 * f3 - f9) + f6, 216F * f4, 0.0F, s);
        Orient orient1 = main3d.camera3D.pos.getAbsOrient();
        _p.x = 511F * f2;
        _p.y = 96F * f4;
        if (orient1.tangage() < 0.0F) {
            _o1.set(orient1);
            _o1.increment(0.0F, 0.0F, 90F);
            _o1.increment(0.0F, 90F, 0.0F);
            _o.sub(_oNull, _o1);
            meshNeedle5.setPos(_p, _o);
            meshNeedle5.render();
        }
        _o1.set(orient1);
        _o1.increment(0.0F, 0.0F, 90F);
        _o1.increment(0.0F, 90F, 0.0F);
        _o.sub(orient, _o1);
        meshNeedle4.setPos(_p, _o);
        meshNeedle4.render();
        if (orient1.tangage() >= 0.0F) {
            _o1.set(orient1);
            _o1.increment(0.0F, 0.0F, 90F);
            _o1.increment(0.0F, 90F, 0.0F);
            _o.sub(_oNull, _o1);
            meshNeedle5.setPos(_p, _o);
            meshNeedle5.render();
        }
        _p.x = 851F * f2 * f3 + f6;
        _p.y = 84F * f4;
        _o1.set(orient);
        _o1.set(0.0F, -_o1.tangage(), _o1.kren());
        _o1.increment(0.0F, 0.0F, 90F);
        _o1.increment(0.0F, 90F, 0.0F);
        _o.sub(_oNull, _o1);
        meshNeedle6.setPos(_p, _o);
        meshNeedle6.render();
        _o.set(0.0F, 0.0F, 0.0F);
        meshNeedleMask.setPos(_p, _o);
        meshNeedleMask.render();
        int i = (int) (World.getPlayerFM().getOverload() * 10F);
        float f8 = (float) i / 10F;
        String s1 = "" + f8;
        float f10 = fntLcd.width(s1);
        if (World.getPlayerFM().getLoadDiff() < World.getPlayerFM().getLimitLoad() * 0.25F) fntLcd.output(0xff0000ff, 215F * f2 * f3 - f10, 182F * f4, 0.0F, s1);
        else if (i < 0) fntLcd.output(0xff000000, 215F * f2 * f3 - f10, 182F * f4, 0.0F, s1);
        else fntLcd.output(-1, 215F * f2 * f3 - f10, 182F * f4, 0.0F, s1);
    }

    private float cvt(float f, float f1, float f2, float f3, float f4) {
        f = Math.min(Math.max(f, f1), f2);
        return f3 + ((f4 - f3) * (f - f1)) / (f2 - f1);
    }

    private void initDashBoard() {
        spriteLeft = Mat.New("gui/game/hud/hudleft.mat");
        spriteRight = Mat.New("gui/game/hud/hudright.mat");
        meshNeedle1 = new Mesh("gui/game/hud/needle1/mono.sim");
        meshNeedle2 = new Mesh("gui/game/hud/needle2/mono.sim");
        meshNeedle3 = new Mesh("gui/game/hud/needle3/mono.sim");
        meshNeedle4 = new Mesh("gui/game/hud/needle4/mono.sim");
        meshNeedle5 = new Mesh("gui/game/hud/needle5/mono.sim");
        meshNeedle6 = new Mesh("gui/game/hud/needle6/mono.sim");
        meshNeedleMask = new Mesh("gui/game/hud/needlemask/mono.sim");
        fntLcd = TTFont.get("lcdnova");
        setScales();
    }

    private void setScales() {
        float f = viewDX;
        float f1 = f / 1024F;
        float f2 = 1.333333F / (f / (float) viewDY);
        if (viewDX / viewDY < 1) {
            f1 *= 0.75F;
            f2 *= 0.75F;
        }
        meshNeedle1.setScale(140F * f1 * f2);
        meshNeedle2.setScale(140F * f1 * f2);
        meshNeedle3.setScale(75F * f1 * f2);
        meshNeedle4.setScale(100F * f1 * f2);
        meshNeedle5.setScale(90F * f1 * f2);
        meshNeedle6.setScale(150F * f1 * f2);
        meshNeedleMask.setScale(150F * f1 * f2);
    }

    public void render() {
        renderSpeed();
        renderOrder();
        renderMsg();
        renderTraining();
        renderLog();
        renderDashBoard();
        renderPointers();
        renderNetStat();
        renderRec();
    }

    public void preRender() {
        preRenderDashBoard();
    }

    public void resetGame() {
        setScales();
        clearSpeed();
        clearOrder();
        clearMsg();
        clearTraining();
        clearLog();
        clearPointers();
        stopNetStat();
    }

    public void contextResize(int i, int j) {
        viewDX = main3d.renderHUD.getViewPortWidth();
        viewDY = main3d.renderHUD.getViewPortHeight();
        setScales();
        resetMsgSizes();
        resetTrainingSizes();
    }

    private void renderRec() {
        if (!NetMissionTrack.isRecording() || !Config.cur.bRec) return;
        float f = viewDX;
        float f1 = viewDY;
        float f2 = f / 1024F;
        float f3 = f1 / 768F;
        float f4 = 1.333333F / (f / f1);
        if (NetMissionTrack.isPlaying()) {
            // TTFont ttfont = TTFont.font[1];
            // int i = ttfont.height();
            Render.drawTile(828F * f2, 2.0F * f3, 16F * f2 * f4, 16F * f3, 0.0F, spriteRec, -1, 0.0F, 1.0F, 1.0F, -1F);
        } else {
            Render.drawTile(896F * f2, 2.0F * f3, 16F * f2 * f4, 16F * f3, 0.0F, spriteRec, -1, 0.0F, 1.0F, 1.0F, -1F);
        }
    }

    private void initRec() {
        spriteRec = Mat.New("gui/game/recIndicator.mat");
    }

    public HUD() {
        bDrawAllMessages = true;
        bDrawVoiceMessages = true;
        subTitlesLines = 11;
        timeLoadLimit = 0L;
        iDrawSpeed = 1;
        lastDrawSpeed = -1;
        msgLines = new ArrayList();
        trainingLines = new ArrayList();
        bCoopTimeStart = false;

        // TODO: Read max. Number of Log Lines from conf.ini and initialize log buffers accordingly
        lenLogBuf = Config.cur.ini.get("Mods", "HUDLogLines", lenLogBuf);
        logBuf = new String[lenLogBuf];
        logBufStr = new String[lenLogBuf];
        logBufId = new int[lenLogBuf];
        logTime = new long[lenLogBuf];
        // ---

        // TODO: Read custom color values from conf.ini
        colorLog1 = getHexFromIni("Mods", "HUDLogColor1", COLOR_LOG1_DEFAULT);
        colorLog2 = getHexFromIni("Mods", "HUDLogColor2", COLOR_LOG2_DEFAULT);
        colorLogBottom1 = getHexFromIni("Mods", "HUDLogBottomColor1", COLOR_LOG_BOTTOM1_DEFAULT);
        colorLogBottom2 = getHexFromIni("Mods", "HUDLogBottomColor2", COLOR_LOG_BOTTOM2_DEFAULT);
        // ---

        logPtr = 0;
        logLen = 0;
        bDrawNetStat = false;
        pageNetStat = 0;
        pageSizeNetStat = 0;
        statUsers = new ArrayList();
        pointers = new ArrayList();
        nPointers = 0;
        bDrawDashBoard = false;
        _p = new Point3d();
        _o = new Orient();
        _o1 = new Orient();
        _oNull = new Orient(0.0F, 0.0F, 0.0F);
        main3d = Main3D.cur3D();
        viewDX = main3d.renderHUD.getViewPortWidth();
        viewDY = main3d.renderHUD.getViewPortHeight();
        speedbarMultiplierKMH = 0.2F;
        speedbarMultiplierMPH = 0.2F;
        speedbarMultiplierKnots = 0.2F;
        speedbarMultiplierMeters = 0.2F;
        speedbarMultiplierFeet = 0.1F;
        bMixedMessage = Config.cur.ini.get("Mods", "MixedSpeedbar", false);
        bMessageFull = Config.cur.ini.get("Mods", "FullSpeedbar", false);
        bShowSIToo = Config.cur.ini.get("Mods", "ShowSIToo", true);
        bMphHUD = false;
        bMphKmhHUD = false;
        HUDSpeedMultiplier1 = 3.6F;
        HUDSpeedMultiplier2 = 1.0F;
        HUDAltitudeDigitalMultiplier = 1.0F;
        HUDAltitudeAnalogMultiplier = 1.0F;
        bUseColor = Config.cur.ini.get("Mods", "PALMODsColor", true);
        BombSightAssist = Config.cur.ini.get("Mods", "BombSightAssist", 1);
        BombSightAssistConf = Config.cur.ini.get("Mods", "BombSightAssistConf", 1);
        speedbarUnits = Config.cur.ini.get("Mods", "SpeedbarUnits", 1);
        speedbarSpdKMH = Config.cur.ini.get("Mods", "SpeedbarSpdKMH", 5);
        speedbarSpdMPH = Config.cur.ini.get("Mods", "SpeedbarSpdMPH", 5);
        speedbarSpdKnots = Config.cur.ini.get("Mods", "SpeedbarSpdKnots", 5);
        speedbarAltMeters = Config.cur.ini.get("Mods", "SpeedbarAltMeters", 5);
        speedbarAltFeet = Config.cur.ini.get("Mods", "SpeedbarAltFeet", 10);
        if(speedbarUnits < 0 || speedbarUnits > 6)
            speedbarUnits = 1;
        if(speedbarSpdKMH >= 1 && speedbarSpdKMH <= 10)
            speedbarMultiplierKMH = 1.0F / (float)speedbarSpdKMH;
        else
            speedbarSpdKMH = 5;
        if(speedbarSpdMPH >= 1 && speedbarSpdMPH <= 10)
            speedbarMultiplierMPH = 1.0F / (float)speedbarSpdMPH;
        else
            speedbarSpdMPH = 5;
        if(speedbarSpdKnots >= 1 && speedbarSpdKnots <= 10)
            speedbarMultiplierKnots = 1.0F / (float)speedbarSpdKnots;
        else
            speedbarSpdKnots = 5;
        if(speedbarAltMeters >= 1 && speedbarAltMeters <= 10)
            speedbarMultiplierMeters = 1.0F / (float)speedbarAltMeters;
        else
            speedbarAltMeters = 5;
        if(speedbarAltFeet >= 1 && speedbarAltFeet <= 50)
            speedbarMultiplierFeet = 1.0F / (float)speedbarAltFeet;
        else
            speedbarAltFeet = 10;
        int i = Config.cur.ini.get("Mods", "HUDGauges", 1);
        if(i == 3)
        {
            bMphKmhHUD = true;
            HUDSpeedMultiplier1 = 2.236936F;
            HUDSpeedMultiplier2 = 1.609344F;
            HUDAltitudeDigitalMultiplier = 3.28084F;
            HUDAltitudeAnalogMultiplier = 1.0F;
        } else
        if(i == 2)
        {
            bMphHUD = true;
            HUDSpeedMultiplier1 = 2.236936F;
            HUDSpeedMultiplier2 = 1.0F;
            HUDAltitudeDigitalMultiplier = 3.28084F;
            HUDAltitudeAnalogMultiplier = 3.28084F;
        }
        sbGMode = Config.cur.ini.get("Mods", "SpeedbarGMode", 1);
        sbTrimSet = Config.cur.ini.get("Mods", "SpeedbarTrimSet", 0.0F);
        sbFlapsSet = Config.cur.ini.get("Mods", "SpeedbarFlapsSet", 0.0F);
        sbAoASet = Config.cur.ini.get("Mods", "SpeedbarAoASet", 0.0F);
        sbVSpeedSet = Config.cur.ini.get("Mods", "SpeedbarVSpeedSet", 0.0F);
        sbRPMSet = Config.cur.ini.get("Mods", "SpeedbarRPMSet", 0.0F);
        sbTempSet = Config.cur.ini.get("Mods", "SpeedbarTempSet", 0.0F);
        sbHPSet = Config.cur.ini.get("Mods", "SpeedbarHPSet", 0.0F);
        sbWeightSet = Config.cur.ini.get("Mods", "SpeedbarWeightSet", 0.0F);
        sbDragSet = Config.cur.ini.get("Mods", "SpeedbarDragSet", 0.0F);
        initSpeed();
        initOrder();
        initMsg();
        initTraining();
        initLog();
        initDashBoard();
        initPointers();
        initRec();
        bRec = Config.cur.ini.get("game", "RecordingIndicator", bRec);
        bNoSubTitles = Config.cur.ini.get("game", "NoSubTitles", bNoSubTitles);
        subTitlesLines = Config.cur.ini.get("game", "SubTitlesLines", subTitlesLines);
        if (subTitlesLines < 1) subTitlesLines = 1;
        resGUI = ResourceBundle.getBundle("i18n/gui", RTSConf.cur.locale, LDRres.loader());
        bNoHudLog = Config.cur.ini.get("game", "NoHudLog", bNoHudLog);
        sbAIManeuver = Config.cur.ini.get("Mods", "SpeedbarAIManeuver", 0.0F);
        sbThrottle = Config.cur.ini.get("Mods", "SpeedbarThrottle", 0.0F);
        sbOrient = Config.cur.ini.get("Mods", "SpeedbarOrient", 0.0F);
        sbLocWay = Config.cur.ini.get("Mods", "SpeedbarLocateWaypoint", 0.0F);
        sbIASTAS = Config.cur.ini.get("Mods", "SpeedbarIASTAS", 0.0F);
        sbDroptank = Config.cur.ini.get("Mods", "SpeedbarDroptank", 0.0F);
        sbSqure = Config.cur.ini.get("Mods", "SpeedbarSqure", 0.0F);
    }

    public boolean bDrawAllMessages;
    public boolean bDrawVoiceMessages;
    private boolean bNoSubTitles;
    private int subTitlesLines;
    private boolean bNoHudLog;
    private Main3D main3d;
    private int viewDX;
    private int viewDY;
    long timeLoadLimit;
    int cnt;
    public boolean bRec;
    private String renderSpeedSubstrings[][] = {
        {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }, {
            null, null, null
        }
    };
    private int iDrawSpeed;
    private int lastDrawSpeed;
    private Order order[];
    private String orderStr[];
    public ResourceBundle resOrder;
    public ResourceBundle resControl;
    public ResourceBundle resGUI;
    public ResourceBundle resMsg;
    private int msgX0;
    private int msgY0;
    private int msgDX;
    private int msgDY;
    private int msgSIZE;
    private int msgSpaceLen;
    private ArrayList msgLines;
    private int msgColor[][] = { { 0xcf0000ff, 0xcf0000ff, 0xcf0000ff, 0xcf005f9f, 0xcf003fdf, 0xcf0000ff, 0xcf0000ff, 0xcf0000ff, 0xcf0000ff },
            { 0xcfff0000, 0xcfff0000, 0xcfff0000, 0xcf9f5f00, 0xcfdf3f00, 0xcfff0000, 0xcfff0000, 0xcfff0000, 0xcfff0000 } };
    private int trainingX0;
    private int trainingY0;
    private int trainingDX;
    private int trainingDY;
    private int trainingSIZE;
    private int trainingSpaceLen;
    private ArrayList trainingLines;
    private static int idLog = 1;
    public ResourceBundle resLog;
    private String logRightBottom;
    private long logRightBottomTime;
    private static final long logCenterTimeLife = 5000L;
    private String logCenter;
    private long logCenterTime;
    private String logIntro;
    private String logIntroESC;
    private TTFont fntCenter;
    private boolean bCoopTimeStart;
    private long coopTimeStart;

    // TODO: Remove "final" modifier for lenLogBuf
    private static int lenLogBuf = 3;
    // ---

    private static final long logTimeLife = 10000L;
    private static final long logTimeFire = 5000L;

    // TODO: Custom color values for cross fading message colors
    // Both "normal" and "bottom" (WEP) log messages
    private static int colorLog1;
    private static int colorLog2;
    private static int colorLogBottom1;
    private static int colorLogBottom2;

    private static final int COLOR_LOG1_DEFAULT = 0xffffffff;
    private static final int COLOR_LOG2_DEFAULT = 0xffff0000;
    private static final int COLOR_LOG_BOTTOM1_DEFAULT = 0xff0000ff;
    private static final int COLOR_LOG_BOTTOM2_DEFAULT = 0xff00ffff;

    private static final int COLOR_SPD_RED = 0xc00000ff;
    private static final int COLOR_SPD_REDo = 0xc01133ff;
    private static final int COLOR_SPD_BLUE = 0xc0ff2222;
    private static final int COLOR_SPD_BLUEo = 0xc0ff4422;
    private static final int MAX_iDrawSpeed = 6;
    // ---

    private String logBuf[];
    private String logBufStr[];
    private int logBufId[];
    private long logTime[];
    private int logPtr;
    private int logLen;
    private boolean bDrawNetStat;
    private int pageNetStat;
    private int pageSizeNetStat;
    private int armyScores[];
    ArrayList statUsers;
    private ArrayList pointers;
    private int nPointers;
    private Mat spritePointer;
    public boolean bDrawDashBoard;
    private Point3d _p;
    private Orient _o;
    private Orient _o1;
    private Orient _oNull;
    private Mat spriteLeft;
    private Mat spriteRight;
    private Mesh meshNeedle1;
    private Mesh meshNeedle2;
    private Mesh meshNeedle3;
    private Mesh meshNeedle4;
    private Mesh meshNeedle5;
    private Mesh meshNeedle6;
    private Mesh meshNeedleMask;
    private TTFont fntLcd;
    private Mat spriteRec;

    private int speedbarSpdKMH;
    private int speedbarSpdMPH;
    private int speedbarSpdKnots;
    private int speedbarAltMeters;
    private int speedbarAltFeet;
    private float speedbarMultiplierKMH;
    private float speedbarMultiplierMPH;
    private float speedbarMultiplierKnots;
    private float speedbarMultiplierMeters;
    private float speedbarMultiplierFeet;
    private int speedbarUnits;
    private boolean bUseColor;
    private int BombSightAssist;
    private int BombSightAssistConf;
    private boolean bMixedMessage;
    private boolean bMessageFull;
    private boolean bShowSIToo;
    private boolean bMphHUD;
    private boolean bMphKmhHUD;
    private float HUDSpeedMultiplier1;
    private float HUDSpeedMultiplier2;
    private float HUDAltitudeDigitalMultiplier;
    private float HUDAltitudeAnalogMultiplier;
    private String sTAS;
    private boolean bSI;
    private boolean bIsSI;
    private int sbGMode;
    private float sbTrimSet;
    private float sbFlapsSet;
    private float sbAoASet;
    private float sbVSpeedSet;
    private float sbRPMSet;
    private float sbTempSet;
    private float sbHPSet;
    private float sbWeightSet;
    private float sbDragSet;
    private float sbAIManeuver;
    private float sbThrottle;
    private float sbOrient;
    private float sbLocWay;
    private float sbIASTAS;
    private float sbDroptank;
    private float sbSqure;
}
