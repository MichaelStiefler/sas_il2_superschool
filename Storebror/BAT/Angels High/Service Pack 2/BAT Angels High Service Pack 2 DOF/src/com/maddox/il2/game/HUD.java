package com.maddox.il2.game;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.TTFont;
import com.maddox.il2.engine.hotkey.HookPilot;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.order.Order;
import com.maddox.il2.game.order.OrderAnyone_Help_Me;
import com.maddox.il2.game.order.OrderVector_To_Home_Base;
import com.maddox.il2.game.order.OrderVector_To_Target;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.NetUserStat;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.AircraftLH;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.VoiceBase;
import com.maddox.il2.objects.weapons.Torpedo;
import com.maddox.rts.HotKeyEnv;
import com.maddox.rts.LDRres;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.Time;
import com.maddox.rts.VK;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;

public class HUD {
    static class MsgLine {

        String msg;
        int    iActor;
        int    army;
        long   time0;
        int    len;

        MsgLine(String s, int i, int j, int k, long l) {
            this.msg = s;
            this.len = i;
            this.iActor = j;
            this.army = k;
            this.time0 = l;
        }
    }

    static class StatUser {

        NetUser  user;
        int      iNum;
        String   sNum;
        int      iPing;
        String   sPing;
        int      iScore;
        String   sScore;
        int      iArmy;
        String   sArmy;
        Aircraft aAircraft;
        String   sAircraft;
        String   sAircraftType;

        StatUser() {
        }
    }

    static class Ptr {

        public void set(float f, float f1, int i, float f2, float f3) {
            this.x = f;
            this.y = f1;
            this.color = i;
            this.alpha = f2;
            this.angle = (float) ((f3 * 180F) / Math.PI);
        }

        float x;
        float y;
        int   color;
        float alpha;
        float angle;

        public Ptr(float f, float f1, int i, float f2, float f3) {
            this.set(f, f1, i, f2, f3);
        }
    }

    public static int drawSpeed() {
        return Main3D.cur3D().hud.iDrawSpeed;
    }

    public static void setDrawSpeed(int i) {
        Main3D.cur3D().hud.iDrawSpeed = i;
    }

    public String DecStr(float Value) {
        String VRounded = Double.toString(Value + 0.05F);
        int Pos = VRounded.indexOf('.');
        return VRounded.substring(0, Pos + 2);
    }

    public void GetHUDUnits() {
        this.bSI = this.bShowSIToo;
        this.bIsSI = false;
        String s;
        switch (this.iDrawSpeed) {
            case 4: // '\004'
            default:
                s = ".si";
                this.bSI = false;
                this.bIsSI = true;
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
        this.sTAS = this.iDrawSpeed < 4 ? "" : " TAS";
        try {
            this.renderSpeedSubstrings[0][0] = this.resLog.getString("HDG");
            this.renderSpeedSubstrings[1][0] = this.resLog.getString("ALT");
            this.renderSpeedSubstrings[2][0] = this.resLog.getString("SPD");
            this.renderSpeedSubstrings[3][0] = this.resLog.getString("G");
            this.renderSpeedSubstrings[0][1] = this.resLog.getString("HDG" + s);
            this.renderSpeedSubstrings[1][1] = this.resLog.getString("ALT" + s);
            this.renderSpeedSubstrings[2][1] = this.resLog.getString("SPD" + s);
            this.renderSpeedSubstrings[3][1] = this.resLog.getString("Ga");
        } catch (Exception exception) {
            this.renderSpeedSubstrings[0][0] = "HDG";
            this.renderSpeedSubstrings[1][0] = "Alt";
            this.renderSpeedSubstrings[2][0] = "Spd";
            this.renderSpeedSubstrings[3][0] = "G";
            this.renderSpeedSubstrings[0][1] = "\260";
            this.renderSpeedSubstrings[1][1] = "";
            this.renderSpeedSubstrings[2][1] = "";
            this.renderSpeedSubstrings[3][1] = "";
        }
        try {
            this.renderSpeedSubstrings[4][0] = this.resLog.getString("Ail.Trim");
            this.renderSpeedSubstrings[5][0] = this.resLog.getString("Ele.Trim");
            this.renderSpeedSubstrings[6][0] = this.resLog.getString("Rud.Trim");
            this.renderSpeedSubstrings[7][0] = this.resLog.getString("Flaps");
            this.renderSpeedSubstrings[8][0] = this.resLog.getString("");
            this.renderSpeedSubstrings[9][0] = this.resLog.getString("TAS");
            this.renderSpeedSubstrings[10][0] = this.resLog.getString("AoA");
            this.renderSpeedSubstrings[11][0] = this.resLog.getString("VSpeed");
            this.renderSpeedSubstrings[12][0] = this.resLog.getString("RPM");
            this.renderSpeedSubstrings[13][0] = this.resLog.getString("Temp");
            this.renderSpeedSubstrings[14][0] = this.resLog.getString("HP");
            this.renderSpeedSubstrings[15][0] = this.resLog.getString("WEIGHT");
            this.renderSpeedSubstrings[16][0] = this.resLog.getString("DRAG");
            this.renderSpeedSubstrings[4][1] = this.resLog.getString("Ail.Trim" + s);
            this.renderSpeedSubstrings[5][1] = this.resLog.getString("Ele.Trim" + s);
            this.renderSpeedSubstrings[6][1] = this.resLog.getString("Rud.Trim" + s);
            this.renderSpeedSubstrings[7][1] = this.resLog.getString("Flaps" + s);
            this.renderSpeedSubstrings[8][1] = this.resLog.getString("");
            this.renderSpeedSubstrings[9][1] = this.resLog.getString("TAS" + s);
            this.renderSpeedSubstrings[10][1] = this.resLog.getString("AoA" + s);
            this.renderSpeedSubstrings[11][1] = this.resLog.getString("VSpeed" + s);
            this.renderSpeedSubstrings[12][1] = this.resLog.getString("RPM" + s);
            this.renderSpeedSubstrings[13][1] = this.resLog.getString("Temp" + s);
            this.renderSpeedSubstrings[14][1] = this.resLog.getString("HP" + s);
            this.renderSpeedSubstrings[15][1] = this.resLog.getString("WEIGHT" + s);
            this.renderSpeedSubstrings[16][1] = this.resLog.getString("DRAG" + s);
        } catch (Exception exception) {
            this.renderSpeedSubstrings[4][0] = "Ail:";
            this.renderSpeedSubstrings[5][0] = "Ele:";
            this.renderSpeedSubstrings[6][0] = "Rud:";
            this.renderSpeedSubstrings[7][0] = "Flaps:";
            this.renderSpeedSubstrings[8][0] = "";
            this.renderSpeedSubstrings[9][0] = "TAS";
            this.renderSpeedSubstrings[10][0] = "AoA:";
            this.renderSpeedSubstrings[11][0] = "VSpeed:";
            this.renderSpeedSubstrings[12][0] = "RPM:";
            this.renderSpeedSubstrings[13][0] = "Temp:";
            this.renderSpeedSubstrings[14][0] = "Power:";
            this.renderSpeedSubstrings[15][0] = "Weight:";
            this.renderSpeedSubstrings[16][0] = "Drag:";
            this.renderSpeedSubstrings[4][1] = "%";
            this.renderSpeedSubstrings[5][1] = "%";
            this.renderSpeedSubstrings[6][1] = "%";
            this.renderSpeedSubstrings[7][1] = "%";
            this.renderSpeedSubstrings[8][1] = "";
            this.renderSpeedSubstrings[9][1] = "";
            this.renderSpeedSubstrings[10][1] = "\260";
            this.renderSpeedSubstrings[11][1] = this.bIsSI ? "m/s" : "ft/s";
            this.renderSpeedSubstrings[12][1] = "";
            this.renderSpeedSubstrings[13][1] = this.bIsSI ? "\260C" : "\260F";
            this.renderSpeedSubstrings[14][1] = "% HP";
            this.renderSpeedSubstrings[15][1] = this.bIsSI ? "kg" : "lb";
            this.renderSpeedSubstrings[16][1] = "";
        }
        this.lastDrawSpeed = this.iDrawSpeed;
    }

    private final void renderSpeed() {
        if (!Actor.isValid(World.getPlayerAircraft())) {
            return;
        }
        if (this.iDrawSpeed == 0) {
            return;
        }
        if (!this.bDrawAllMessages) {
            return;
        }
        if ((Main.cur().netServerParams != null) && !Main.cur().netServerParams.isShowSpeedBar()) {
            return;
        }
        TTFont ttfont = null;
        if (this.bDrawDashBoard) {
            ttfont = TTFont.font[4];
        } else {
            ttfont = TTFont.font[1];
        }
        int i = ttfont.height();
        int j = 0xc00000ff;
        boolean flag = false;
        float f = World.getPlayerFM().getLoadDiff();
        if ((f <= (World.getPlayerFM().getLimitLoad() * 0.25F)) && (f > (World.getPlayerFM().getLimitLoad() * 0.1F))) {
            flag = true;
            this.cnt = 0;
            this.timeLoadLimit = 0L;
        } else if ((f <= (World.getPlayerFM().getLimitLoad() * 0.1F)) && (com.maddox.rts.Time.current() < this.timeLoadLimit)) {
            flag = false;
        } else if ((f <= (World.getPlayerFM().getLimitLoad() * 0.1F)) && (com.maddox.rts.Time.current() >= this.timeLoadLimit)) {
            flag = true;
            this.cnt++;
            if (this.cnt == 22) {
                this.timeLoadLimit = 125L + com.maddox.rts.Time.current();
                this.cnt = 0;
            }
        } else {
            this.cnt = 0;
            this.timeLoadLimit = 0L;
        }
        double d;
        int k;
        float fs;
        float fa;
        if (!this.bMixedMessage || (this.main3d.viewActor() == World.getPlayerAircraft())) {
            fa = World.getPlayerFM().getAltitude();
            fs = World.getPlayerFM().getSpeed();
            d = ((Tuple3d) (((FlightModelMain) (World.getPlayerFM())).Loc)).z;
            k = (int) (((FlightModelMain) (World.getPlayerFM())).Or.getYaw() + 0.5F);
            k = k > 90 ? 450 - k : 90 - k;
        } else if (this.main3d.viewActor() instanceof Aircraft) {
            fa = ((SndAircraft) ((Aircraft) this.main3d.viewActor())).FM.getAltitude();
            fs = ((SndAircraft) ((Aircraft) this.main3d.viewActor())).FM.getSpeed();
            d = ((Tuple3d) (((FlightModelMain) (((SndAircraft) ((Aircraft) this.main3d.viewActor())).FM)).Loc)).z;
            k = (int) (((FlightModelMain) (((SndAircraft) ((Aircraft) this.main3d.viewActor())).FM)).Or.getYaw() + 0.5F);
            k = k > 90 ? 450 - k : 90 - k;
        } else {
            fa = 0.0F;
            fs = 0.0F;
            d = 0.0D;
            k = 0;
        }
        int l;
        int i1;
        int l0;
        int i10;
        switch (this.iDrawSpeed) {
            default:
                l = (int) ((fa * this.speedbarMultiplierMeters * this.speedbarAltMeters) + 0.5F);
                i1 = (int) ((3.6F * Pitot.Indicator((float) d, fs) * this.speedbarMultiplierKMH * this.speedbarSpdKMH) + 0.5F);
                l0 = 0;
                i10 = 0;
                break;

            case 2: // '\002'
                l = (int) ((3.28084F * fa * this.speedbarMultiplierFeet * this.speedbarAltFeet) + 0.5F);
                i1 = (int) ((1.943845F * Pitot.Indicator((float) d, fs) * this.speedbarMultiplierKnots * this.speedbarSpdKnots) + 0.5F);
                l0 = (int) ((f * this.speedbarMultiplierMeters * this.speedbarAltMeters) + 0.5F);
                i10 = (int) ((3.6F * Pitot.Indicator((float) d, fs) * this.speedbarMultiplierKMH * this.speedbarSpdKMH) + 0.5F);
                break;

            case 3: // '\003'
                l = (int) ((3.28084F * fa * this.speedbarMultiplierFeet * this.speedbarAltFeet) + 0.5F);
                i1 = (int) ((2.236936F * Pitot.Indicator((float) d, fs) * this.speedbarMultiplierMPH * this.speedbarSpdMPH) + 0.5F);
                l0 = (int) ((fa * this.speedbarMultiplierMeters * this.speedbarAltMeters) + 0.5F);
                i10 = (int) ((3.6F * Pitot.Indicator((float) d, fs) * this.speedbarMultiplierKMH * this.speedbarSpdKMH) + 0.5F);
                break;

            case 4: // '\004'
                l = (int) ((fa * this.speedbarMultiplierMeters * this.speedbarAltMeters) + 0.5F);
                i1 = (int) ((3.6F * fs * this.speedbarMultiplierKMH * this.speedbarSpdKMH) + 0.5F);
                l0 = 0;
                i10 = 0;
                break;

            case 5: // '\005'
                l = (int) ((3.28084F * fa * this.speedbarMultiplierFeet * this.speedbarAltFeet) + 0.5F);
                i1 = (int) ((1.943845F * fs * this.speedbarMultiplierKnots * this.speedbarSpdKnots) + 0.5F);
                l0 = (int) ((fa * this.speedbarMultiplierMeters * this.speedbarAltMeters) + 0.5F);
                i10 = (int) ((3.6F * fs * this.speedbarMultiplierKMH * this.speedbarSpdKMH) + 0.5F);
                break;

            case 6: // '\006'
                l = (int) ((3.28084F * fa * this.speedbarMultiplierFeet * this.speedbarAltFeet) + 0.5F);
                i1 = (int) ((2.236936F * fs * this.speedbarMultiplierMPH * this.speedbarSpdMPH) + 0.5F);
                l0 = (int) ((fa * this.speedbarMultiplierMeters * this.speedbarAltMeters) + 0.5F);
                i10 = (int) ((3.6F * fs * this.speedbarMultiplierKMH * this.speedbarSpdKMH) + 0.5F);
                break;
        }
        if (this.iDrawSpeed != this.lastDrawSpeed) {
            this.GetHUDUnits();
        }
        if (!this.bMixedMessage || (this.main3d.viewActor() == World.getPlayerAircraft())) {
            if (this.bUseColor) {
                j = World.getPlayerAircraft().getArmy() != 1 ? 0xc0ff0000 : 0xc00000ff;
            }
            ttfont.output(j, 5F, 5F, 0.0F, this.renderSpeedSubstrings[0][0] + " " + k + " " + this.renderSpeedSubstrings[0][1]);
            if (!World.cur().diffCur.NoSpeedBar || this.bMessageFull) {
                ttfont.output(j, 5F, 5 + (1 * i), 0.0F, this.renderSpeedSubstrings[1][0] + " " + l + " " + this.renderSpeedSubstrings[1][1] + (this.bSI ? " (" + l0 + " " + this.resLog.getString("ALT.si") + ")" : ""));
                ttfont.output(j, 5F, 5 + (2 * i), 0.0F, this.renderSpeedSubstrings[2][0] + " " + i1 + " " + this.renderSpeedSubstrings[2][1] + this.sTAS + (this.bSI ? " (" + i10 + " " + this.resLog.getString("SPD.si") + ")" : ""));
                if (this.main3d.viewActor() == World.getPlayerAircraft()) {
                    switch (this.sbGMode) {
                        case 0: // '\0'
                            break;

                        case 2: // '\002'
                            ttfont.output(flag ? 0xc00000a0 : 0xc010a010, 5F, 5 + (3 * i), 0.0F, this.DecStr(World.getPlayerFM().getOverload()) + " " + this.renderSpeedSubstrings[3][0]);
                            break;

                        case 3: // '\003'
                            ttfont.output(flag ? 0xc00000a0 : 0xc010a010, 5F, 5 + (3 * i), 0.0F, this.DecStr(World.getPlayerFM().getOverload()) + " " + this.renderSpeedSubstrings[3][0] + " (" + this.DecStr(World.getPlayerFM().getOverload() / World.getPlayerFM().getLimitLoad()) + ")");
                            break;

                        case 1: // '\001'
                        default:
                            if (flag) {
                                ttfont.output(0xc00000a0, 5F, 5 + (3 * i), 0.0F, this.renderSpeedSubstrings[3][0]);
                            }
                            break;
                    }
                    boolean bSA = false;
                    if (!this.main3d.isViewOutside() && !this.main3d.cockpitCur.isNullShow()) {
                        if ((this.BombSightAssist > 0) && (World.getPlayerAircraft() instanceof TypeBomber)) {
                            int pf = World.getPlayerFM().AS.astatePilotFunctions[Main3D.cur3D().cockpitCurIndx()];
                            if ((this.BombSightAssist == 3) || ((this.BombSightAssist == 2) && ((pf == 2) || (pf == 1))) || ((this.BombSightAssist == 1) && (pf == 2))) {
                                bSA = true;
                                Point3d point3d = ((Actor) (World.getPlayerAircraft())).pos.getAbsPoint();
                                float Alt = (float) (((Tuple3d) (point3d)).z - World.land().HQ(((Tuple3d) (point3d)).x, ((Tuple3d) (point3d)).y));
                                float Time = (float) Math.sqrt(0.20394F * Alt);
                                float Dist = World.getPlayerFM().getSpeed() * Time;
                                float Angle = Alt <= 0.0F ? 90F : (float) Math.toDegrees(Math.atan(Dist / Alt));
                                String Distance;
                                if (this.bIsSI) {
                                    Distance = (int) (Dist + 0.5F) + " " + "m";
                                } else {
                                    Distance = (int) ((Dist * 0.9144F) + 0.5F) + " " + "yd";
                                }
                                if (this.BombSightAssistConf == 3) {
                                    ttfont.output(0xc010fefe, 5F, 5 + (6 * i), 0.0F, "Distance: " + Distance);
                                }
                                if (this.BombSightAssistConf > 1) {
                                    ttfont.output(0xc010fefe, 5F, 5 + (5 * i), 0.0F, "Time to Ground: " + this.DecStr(Time) + " " + "s");
                                }
                                HookPilot hookpilot = HookPilot.current;
                                ttfont.output(0xc010fefe, 5F, 5 + (4 * i), 0.0F, (hookpilot.isAim() ? "[Aim] " : "") + "Bombsight Angle: " + this.DecStr(Angle) + " " + "\260");
                            }
                        }
                        if (!bSA) {
                            try {
                                int trAil = (int) (((FlightModelMain) (World.getPlayerFM())).CT.getTrimAileronControl() * 100F);
                                int trEl = (int) (((FlightModelMain) (World.getPlayerFM())).CT.getTrimElevatorControl() * 100F);
                                int trRud = (int) (((FlightModelMain) (World.getPlayerFM())).CT.getTrimRudderControl() * 100F);
                                int flap = (int) (((FlightModelMain) (World.getPlayerFM())).CT.getFlap() * 100F);
                                float vs = (int) World.getPlayerFM().getVertSpeed();
                                float aoa = (int) World.getPlayerFM().getAOA();
                                float hp = 0.0F;
                                String rpm = "";
                                String temp = "";
                                int nEng = World.getPlayerFM().EI.getNum();
                                for (int num = 0; num < nEng; num++) {
                                    if (num > 0) {
                                        rpm = rpm + ":";
                                        temp = temp + ":";
                                    }
                                    float N = World.getPlayerFM().EI.engines[num].getPowerOutput();
                                    if (N >= 0.0F) {
                                        hp += N / nEng;
                                    }
                                    rpm = rpm + (int) (World.getPlayerFM().EI.engines[num].getRPM() + 0.5F);
                                    temp = temp + (this.bIsSI ? (int) (World.getPlayerFM().EI.engines[num].tWaterOut + 0.5F) : (int) ((World.getPlayerFM().EI.engines[num].tWaterOut * 1.8F) + 32.5F));
                                }

                                World.getPlayerFM().Sq.getClass();
                                World.getPlayerFM().Sq.getClass();
                                float drag = World.getPlayerFM().Sq.dragAirbrakeCx + 0.12F + World.getPlayerFM().Sq.dragFuselageCx + World.getPlayerFM().Sq.dragParasiteCx + World.getPlayerFM().Sq.dragProducedCx + 0.06F;
                                int weight = (int) ((this.bIsSI ? World.getPlayerFM().M.mass : World.getPlayerFM().M.mass * 2.2046D) + 0.5D);
                                if (this.sbHPSet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbHPSet) * i), 0.0F, this.renderSpeedSubstrings[14][0] + " " + (int) (hp * 100F) + " " + this.renderSpeedSubstrings[14][1]);
                                }
                                if (this.sbWeightSet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbWeightSet) * i), 0.0F, this.renderSpeedSubstrings[15][0] + " " + weight + " " + this.renderSpeedSubstrings[15][1]);
                                }
                                if (this.sbDragSet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbDragSet) * i), 0.0F, this.renderSpeedSubstrings[16][0] + " " + this.DecStr(drag) + " " + this.renderSpeedSubstrings[16][1]);
                                }
                                if (this.sbFlapsSet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbFlapsSet) * i), 0.0F, this.renderSpeedSubstrings[7][0] + " " + flap + " " + this.renderSpeedSubstrings[7][1]);
                                }
                                if (this.sbVSpeedSet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbVSpeedSet) * i), 0.0F, this.renderSpeedSubstrings[11][0] + " " + this.DecStr(vs) + " " + this.renderSpeedSubstrings[11][1]);
                                }
                                if (this.sbAoASet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbAoASet) * i), 0.0F, this.renderSpeedSubstrings[10][0] + " " + this.DecStr(aoa) + " " + this.renderSpeedSubstrings[10][1]);
                                }
                                if (this.sbTrimSet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbTrimSet) * i), 0.0F, "Trim (" + this.renderSpeedSubstrings[4][0] + " " + trAil + this.renderSpeedSubstrings[4][1] + ", " + this.renderSpeedSubstrings[5][0] + " " + trEl + this.renderSpeedSubstrings[5][1] + ", " + this.renderSpeedSubstrings[6][0] + " " + trRud + this.renderSpeedSubstrings[6][1] + ")");
                                }
                                if (this.sbRPMSet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbRPMSet) * i), 0.0F, this.renderSpeedSubstrings[12][0] + " " + rpm + " " + this.renderSpeedSubstrings[12][1]);
                                }
                                if (this.sbTempSet >= 1.0F) {
                                    ttfont.output(j, 5F, 5 + ((3 + (int) this.sbTempSet) * i), 0.0F, this.renderSpeedSubstrings[13][0] + " " + temp + " " + this.renderSpeedSubstrings[13][1]);
                                }
                            } catch (Exception exception1) {
                            }
                        }
                    }
                }
            }
        } else if ((this.main3d.viewActor() instanceof Aircraft) && !Mission.isNet()) {
            if (this.bUseColor) {
                if (this.main3d.viewActor().getArmy() == World.getPlayerAircraft().getArmy()) {
                    j = this.main3d.viewActor().getArmy() != 1 ? 0xc0900000 : 0xc0000090;
                } else {
                    j = this.main3d.viewActor().getArmy() != 1 ? 0xc0703030 : 0xc0303070;
                }
            } else {
                j = 0xc0000090;
            }
            ttfont.output(j, 5F, 5F, 0.0F, this.renderSpeedSubstrings[0][0] + " " + k + " " + this.renderSpeedSubstrings[0][1]);
            if (!World.cur().diffCur.NoSpeedBar || this.bMessageFull) {
                ttfont.output(j, 5F, 5 + i, 0.0F, this.renderSpeedSubstrings[1][0] + " " + l + " " + this.renderSpeedSubstrings[1][1]);
                ttfont.output(j, 5F, 5 + i + i, 0.0F, this.renderSpeedSubstrings[2][0] + " " + i1 + " " + this.renderSpeedSubstrings[2][1] + this.sTAS);
            }
        }
    }

    public void clearSpeed() {
        this.iDrawSpeed = this.speedbarUnits;
    }

    private void initSpeed() {
        this.iDrawSpeed = this.speedbarUnits;
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
            this.order = null;
            return;
        }
        this.order = new Order[aorder.length];
        this.orderStr = new String[aorder.length];
        int i = World.getPlayerArmy();
        for (int j = 0; j < this.order.length; j++) {
            this.order[j] = aorder[j];
            if ((aorder[j] == null) || (this.order[j].name(i) == null)) {
                continue;
            }
            String s = this.order[j].name(i);
            String s1 = null;
            String s2 = World.getPlayerLastCountry();
            if (s2 != null) {
                try {
                    s1 = this.resOrder.getString(s + "_" + s2);
                } catch (Exception exception) {
                }
            }
            if (s1 == null) {
                try {
                    s1 = this.resOrder.getString(s);
                } catch (Exception exception1) {
                    s1 = s;
                }
            }
            this.orderStr[j] = s1;
        }

    }

    private void renderOrder() {
        if (this.order == null) {
            return;
        }
        if (!this.bDrawAllMessages) {
            return;
        }
        TTFont ttfont = TTFont.font[1];
        int i = (int) (this.viewDX * 0.05D);
        int j = ttfont.height();
        int k = this.viewDY - (2 * ttfont.height());
        String s = null;
        int l = k;
        int i1 = 0;
        int j1 = 0;
        boolean flag = false;
        boolean flag1 = false;
        for (int k1 = 0; k1 < this.order.length; k1++) {
            if (this.orderStr[k1] != null) {
                if (this.order[k1] instanceof OrderAnyone_Help_Me) {
                    flag = true;
                }
                if (Main3D.cur3D().ordersTree.frequency() == null) {
                    flag1 = true;
                }
                if (s != null) {
                    this.drawOrder(s, i, l, j1 == 0 ? -1 : j1, i1, flag1);
                }
                j1 = k1;
                s = this.orderStr[k1];
                l = k;
                i1 = this.order[k1].attrib();
                if (((this.order[k1] instanceof OrderVector_To_Home_Base) || (this.order[k1] instanceof OrderVector_To_Target)) && Main.cur().mission.zutiRadar_DisableVectoring) {
                    flag1 = true;
                } else {
                    flag1 = false;
                }
            }
            k -= j;
        }

        if (Main3D.cur3D().ordersTree.frequency() == null) {
            flag1 = true;
        }
        if (s != null) {
            this.drawOrder(s, i, l, 0, i1, flag1);
        }
        if (flag) {
            String as[] = Main3D.cur3D().ordersTree.getShipIDs();
            for (int l1 = 0; l1 < as.length; l1++) {
                if ((l1 == 0) && (as[l1] != null)) {
                    k -= j;
                    k -= j;
                    this.drawShipIDs(this.resOrder.getString("ShipIDs"), i, k);
                    k -= j;
                }
                if (as[l1] != null) {
                    this.drawShipIDs(as[l1], i, k);
                    k -= j;
                }
            }

            this.drawWeaponsInfo();
        }
    }

    private void drawWeaponsInfo() {
        int i = 0;
        TTFont ttfont = TTFont.font[1];
        int j = 0xff0000ff;
        int k = (int) (this.viewDX * 0.6D);
        int l = ttfont.height();
        int i1 = this.viewDY - (2 * ttfont.height());
        if (World.cur().diffCur.BombFuzes) {
            Map map = AircraftLH.getInfoList();
            Object aobj[] = map.keySet().toArray();
            for (int j1 = 0; j1 < aobj.length; j1++) {
                String s = (String) aobj[j1];
                String as[] = (String[]) map.get(s);
                for (int l1 = 0; l1 < (as.length - 1); l1++) {
                    String s2 = as[l1];
                    if ((l1 == 1) && ((drawSpeed() == 2) || (drawSpeed() == 3)) && (as[4] != null)) {
                        s2 = as[4];
                    }
                    if (s2 != null) {
                        ttfont.output(j, k, i1 - (i * l), 0.0F, s2);
                        i++;
                    }
                }

                i++;
            }

            if (aobj.length > 0) {
                i++;
            }
        }
        if (World.cur().diffCur.FragileTorps) {
            Map map1 = Torpedo.getInfoList();
            Object aobj1[] = map1.keySet().toArray();
            for (int k1 = 0; k1 < aobj1.length; k1++) {
                String s1 = (String) aobj1[k1];
                String as1[] = (String[]) map1.get(s1);
                for (int i2 = 0; i2 < (as1.length - 3); i2++) {
                    String s3 = as1[i2];
                    if ((i2 == 1) && ((drawSpeed() == 2) || (drawSpeed() == 3))) {
                        s3 = as1[3];
                    } else if ((i2 == 2) && (drawSpeed() == 2)) {
                        s3 = as1[4];
                    } else if ((i2 == 2) && (drawSpeed() == 3)) {
                        s3 = as1[5];
                    }
                    if (s3 != null) {
                        ttfont.output(j, k, i1 - (i * l), 0.0F, s3);
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
        if ((l & 1) != 0) {
            i1 = 0xff00007f;
        } else if ((l & 2) != 0) {
            i1 = 0xff007fff;
        }
        TTFont ttfont = TTFont.font[1];
        if (flag) {
            i1 = 0x7f7f7f7f;
        }
        HotKeyEnv hotkeyenv = HotKeyEnv.env("orders");
        HashMapInt hashmapint = hotkeyenv.all();
        HashMapIntEntry hashmapintentry = hashmapint.nextEntry(null);
        int j1 = 0;
        do {
            if (hashmapintentry == null) {
                break;
            }
            String s1 = (String) hashmapintentry.getValue();
            if (s1.equals("order" + k)) {
                j1 = hashmapintentry.getKey();
                break;
            }
            hashmapintentry = hashmapint.nextEntry(hashmapintentry);
        } while (true);
        String s2 = "";
        if ((j1 & 0xffff0000) == 0) {
            s2 = this.resName(VK.getKeyText(j1));
        } else {
            s2 = this.resName(VK.getKeyText((j1 >> 16) & 0xffff)) + " " + this.resName(VK.getKeyText(j1 & 0xffff));
        }
        if (k >= 0) {
            ttfont.output(i1, i, j, 0.0F, "" + s2 + ". " + s);
        } else {
            ttfont.output(i1, i, j, 0.0F, s);
        }
    }

    private String resName(String s) {
        if (this.resControl == null) {
            return s;
        }
        try {
            return this.resControl.getString(s);
        } catch (Exception e) {
        }
        return s;
    }

    public void clearOrder() {
        this.order = null;
    }

    private void initOrder() {
        this.clearOrder();
        this.resOrder = ResourceBundle.getBundle("i18n/hud_order", RTSConf.cur.locale, LDRres.loader());
        this.resControl = ResourceBundle.getBundle("i18n/controls", RTSConf.cur.locale, LDRres.loader());
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
        if (!this.bDrawVoiceMessages) {
            return;
        }
        if (Config.cur.bNoSubTitles) {
            return;
        }
        if (i < 1) {
            return;
        }
        if (i > 9) {
            return;
        }
        if (j < 1) {
            return;
        }
        if (j > 2) {
            return;
        }
        if (ai == null) {
            return;
        }
        TTFont ttfont = TTFont.font[4];
        for (int k = 0; (k < ai.length) && (ai[k] != 0); k++) {
            String s = VoiceBase.vbStr[ai[k]];
            try {
                String s1 = this.resMsg.getString(s);
                if (s1 != null) {
                    s = s1;
                }
            } catch (Exception exception) {
            }
            if ((s == null) || (s.length() == 0)) {
                continue; // TODO: Fixed by SAS~Storebror: Game locks up from missing voice commands!
            }
            for (StringTokenizer stringtokenizer = new StringTokenizer(s); stringtokenizer.hasMoreTokens(); flag = false) {
                String s2 = stringtokenizer.nextToken();
                int l = (int) ttfont.width(s2);
                if (this.msgLines.size() == 0) {
                    this.msgLines.add(new MsgLine(s2, l, i, j, Time.current()));
                    continue;
                }
                MsgLine msgline1 = (MsgLine) this.msgLines.get(this.msgLines.size() - 1);
                if ((msgline1.iActor == i) && (msgline1.army == j) && !flag) {
                    int i1 = msgline1.len + this.msgSpaceLen + l;
                    if (i1 < this.msgDX) {
                        msgline1.msg = msgline1.msg + " " + s2;
                        msgline1.len = i1;
                    } else {
                        this.msgLines.add(new MsgLine(s2, l, i, j, 0L));
                    }
                } else {
                    this.msgLines.add(new MsgLine(s2, l, i, j, 0L));
                }
            }

        }

        while (this.msgLines.size() > this.msgSIZE) {
            this.msgLines.remove(0);
            MsgLine msgline = (MsgLine) this.msgLines.get(0);
            msgline.time0 = Time.current();
        }
    }

    private int msgColor(int i, int j) {
        return this.msgColor[j - 1][i - 1];
    }

    private void renderMsg() {
        if (!this.bDrawVoiceMessages) {
            return;
        }
        if (!this.bDrawAllMessages) {
            return;
        }
        int i = this.msgLines.size();
        if (i == 0) {
            return;
        }
        MsgLine msgline = (MsgLine) this.msgLines.get(0);
        long l = msgline.time0 + (msgline.msg.length() * 250);
        if (l < Time.current()) {
            this.msgLines.remove(0);
            if (--i == 0) {
                return;
            }
            MsgLine msgline1 = (MsgLine) this.msgLines.get(0);
            msgline1.time0 = Time.current();
        }
        TTFont ttfont = TTFont.font[4];
        int j = this.msgX0;
        int k = this.msgY0 + this.msgDY;
        for (int i1 = 0; i1 < i; i1++) {
            MsgLine msgline2 = (MsgLine) this.msgLines.get(i1);
            ttfont.output(this.msgColor(msgline2.iActor, msgline2.army), j, k, 0.0F, msgline2.msg);
            k -= ttfont.height();
        }

    }

    public void clearMsg() {
        this.msgLines.clear();
    }

    public void resetMsgSizes() {
        this.clearMsg();
        TTFont ttfont = TTFont.font[1];
        this.msgX0 = (int) (this.viewDX * 0.3D);
        this.msgDX = (int) (this.viewDX * 0.6D);
        this.msgDY = ttfont.height() * this.subTitlesLines;
        if (this.msgDY > (int) (this.viewDY * 0.9D)) {
            this.msgDY = (int) (this.viewDY * 0.9D);
        }
        int i = this.msgDY / ttfont.height();
        if (i == 0) {
            i = 1;
        }
        this.msgDY = ttfont.height() * i;
        this.msgSIZE = i;
        this.msgY0 = (int) (this.viewDY * 0.95D) - this.msgDY;
        this.msgSpaceLen = Math.round(ttfont.width(" "));
    }

    private void initMsg() {
        this.resetMsgSizes();
        this.resMsg = ResourceBundle.getBundle("i18n/hud_msg", RTSConf.cur.locale, LDRres.loader());
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
        this.trainingLines.clear();
        if (s == null) {
            return;
        }
        TTFont ttfont = TTFont.font[2];
        StringTokenizer stringtokenizer = new StringTokenizer(s);
        do {
            if (!stringtokenizer.hasMoreTokens()) {
                break;
            }
            String s1 = stringtokenizer.nextToken();
            int i = (int) ttfont.width(s1);
            if (this.trainingLines.size() == 0) {
                this.trainingLines.add(s1);
                continue;
            }
            String s2 = (String) this.trainingLines.get(this.trainingLines.size() - 1);
            int j = (int) ttfont.width(s2);
            int k = j + this.trainingSpaceLen + i;
            if (k < this.trainingDX) {
                this.trainingLines.set(this.trainingLines.size() - 1, s2 + " " + s1);
                continue;
            }
            if (this.trainingLines.size() >= this.trainingSIZE) {
                break;
            }
            this.trainingLines.add(s1);
        } while (true);
    }

    private void renderTraining() {
        int i = this.trainingLines.size();
        if (i == 0) {
            return;
        }
        TTFont ttfont = TTFont.font[2];
        int j = this.trainingX0;
        int k = this.trainingY0 + this.trainingDY;
        for (int l = 0; l < i; l++) {
            String s = (String) this.trainingLines.get(l);
            ttfont.output(0xff0000ff, j, k, 0.0F, s);
            k -= ttfont.height();
        }

    }

    public void clearTraining() {
        this.trainingLines.clear();
    }

    public void resetTrainingSizes() {
        this.clearTraining();
        TTFont ttfont = TTFont.font[2];
        this.trainingX0 = (int) (this.viewDX * 0.3D);
        this.trainingDX = (int) (this.viewDX * 0.5D);
        this.trainingY0 = (int) (this.viewDY * 0.5D);
        this.trainingDY = (int) (this.viewDY * 0.45D);
        int i = this.trainingDY / ttfont.height();
        if (i == 0) {
            i = 1;
        }
        this.trainingDY = ttfont.height() * i;
        this.trainingSIZE = i;
        this.trainingSpaceLen = Math.round(ttfont.width(" "));
    }

    private void initTraining() {
        this.resetTrainingSizes();
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
            this.logIntro = null;
            return;
        } else {
            this.logIntro = s;
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
            this.logIntroESC = null;
            return;
        } else {
            this.logIntroESC = s;
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
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (Main3D.cur3D().gameTrackPlay() != null) {
            return;
        }
        if ((Main3D.cur3D().gameTrackRecord() != null) && (aobj != null) && (aobj.length == 1) && (aobj[0] instanceof Integer)) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(0);
                netmsgguaranted.writeInt(i);
                netmsgguaranted.write255(s);
                netmsgguaranted.writeInt(((Integer) aobj[0]).intValue());
                Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
            } catch (Exception exception) {
            }
        }
        Main3D.cur3D().hud._log(i, s, aobj);
    }

    private boolean showAlways(String s) {
        return s.startsWith("Beacon") || s.startsWith("Bombsight") || s.startsWith("EL_AZ") || s.startsWith("TOKG");
    }

    public void _log(int i, String s, Object aobj[]) {
        if (Config.cur.bNoHudLog && !this.showAlways(s)) {
            return;
        }
        int j = this.__log(i, s);
        String s1 = null;
        try {
            s1 = this.resLog.getString(s);
        } catch (Exception exception) {
            s1 = s;
        }
        this.logBufStr[j] = MessageFormat.format(s1, aobj);
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
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (flag) {
            if (Main3D.cur3D().gameTrackPlay() != null) {
                return;
            }
            if (Main3D.cur3D().gameTrackRecord() != null) {
                try {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(1);
                    netmsgguaranted.writeInt(i);
                    netmsgguaranted.write255(s);
                    Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
                } catch (Exception exception) {
                }
            }
        }
        Main3D.cur3D().hud._log(i, s);
    }

    public void _log(int i, String s) {
        if (Config.cur.bNoHudLog && !this.showAlways(s)) {
            return;
        }
        int j = this.__log(i, s);
        try {
            this.logBufStr[j] = this.resLog.getString(s);
        } catch (Exception exception) {
            this.logBufStr[j] = s;
        }
    }

    public static void logRightBottom(String s) {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (Main3D.cur3D().gameTrackPlay() != null) {
            return;
        }
        if (Main3D.cur3D().gameTrackRecord() != null) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(2);
                netmsgguaranted.write255(s == null ? "" : s);
                Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
            } catch (Exception exception) {
            }
        }
        Main3D.cur3D().hud._logRightBottom(s);
    }

    public void _logRightBottom(String s) {
        if (Config.cur.bNoHudLog) {
            return;
        }
        if (s == null) {
            this.logRightBottom = null;
            return;
        }
        try {
            this.logRightBottom = this.resLog.getString(s);
        } catch (Exception exception) {
            this.logRightBottom = s;
        }
        this.logRightBottomTime = Time.current();
    }

    public static void logCenter(String s) {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (Main3D.cur3D().gameTrackPlay() != null) {
            return;
        }
        if (Main3D.cur3D().gameTrackRecord() != null) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(3);
                netmsgguaranted.write255(s == null ? "" : s);
                Main3D.cur3D().gameTrackRecord().postTo(Main3D.cur3D().gameTrackRecord().channel(), netmsgguaranted);
            } catch (Exception exception) {
            }
        }
        Main3D.cur3D().hud._logCenter(s);
    }

    public void _logCenter(String s) {
        if (s == null) {
            this.logCenter = null;
            return;
        }
        try {
            this.logCenter = this.resLog.getString(s);
        } catch (Exception exception) {
            this.logCenter = s;
        }
        this.logCenterTime = Time.current();
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
        this.bCoopTimeStart = true;
        this.coopTimeStart = l;
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
                this._log(j, s2, aobj);
                break;

            case 1: // '\001'
                int k = netmsginput.readInt();
                String s3 = netmsginput.read255();
                this._log(k, s3);
                break;

            case 2: // '\002'
                String s = netmsginput.read255();
                if ("".equals(s)) {
                    s = null;
                }
                this._logRightBottom(s);
                break;

            case 3: // '\003'
                String s1 = netmsginput.read255();
                if ("".equals(s1)) {
                    s1 = null;
                }
                this._logCenter(s1);
                break;
        }
        return true;
    }

    public void clearLog() {
        this.logRightBottom = null;
        this.logPtr = 0;
        this.logLen = 0;
        this.logCenter = null;
        this.logIntro = null;
        this.logIntroESC = null;
        this.bCoopTimeStart = false;
    }

    private void initLog() {
        this.clearLog();
        this.resLog = ResourceBundle.getBundle("i18n/hud_log", RTSConf.cur.locale, LDRres.loader());
        this.fntCenter = TTFont.font[2];
    }

    private void renderLog() {
        long l = Time.current();
        if (this.bCoopTimeStart) {
            int i = (int) (this.coopTimeStart - Time.currentReal());
            if (i < 0) {
                this.bCoopTimeStart = false;
            } else if (this.bDrawAllMessages) {
                TTFont ttfont5 = this.fntCenter;
                String s = "" + ((i + 500) / 1000);
                float f3 = ttfont5.width(s);
                ttfont5.output(0xff00ffff, (this.viewDX - f3) / 2.0F, this.viewDY * 0.75F, 0.0F, s);
            }
        } else if (this.logIntro != null) {
            TTFont ttfont = this.fntCenter;
            float f = ttfont.width(this.logIntro);
            int i1 = 0xff000000;
            ttfont.output(i1, (this.viewDX - f) / 2.0F, this.viewDY * 0.75F, 0.0F, this.logIntro);
        } else if (this.logCenter != null) {
            if (l > (this.logCenterTime + logCenterTimeLife)) {
                this.logCenter = null;
            } else if (this.bDrawAllMessages) {
                TTFont ttfont1 = this.fntCenter;
                float f1 = ttfont1.width(this.logCenter);
                int j1 = 0xff0000ff;
                int i2 = 255 - (int) (((l - this.logCenterTime) / 5000D) * 255D);
                j1 |= i2 << 8;
                ttfont1.output(j1, (this.viewDX - f1) / 2.0F, this.viewDY * 0.75F, 0.0F, this.logCenter);
            }
        }
        if (this.logIntroESC != null) {
            TTFont ttfont2 = TTFont.font[0];
            float f2 = ttfont2.width(this.logIntroESC);
            byte byte0 = -1;
            ttfont2.output(byte0, (this.viewDX - f2) / 2.0F, this.viewDY * 0.05F, 0.0F, this.logIntroESC);
        }
        if (!Main3D.cur3D().aircraftHotKeys.isAfterburner()) {
            this.logRightBottom = null;
        }
        if ((this.logRightBottom != null) && this.bDrawAllMessages) {
            TTFont ttfont3 = TTFont.font[1];
            int j = (int) (this.viewDX * 0.95D);
            int k1 = (int) ttfont3.width(this.logRightBottom);
            int j2 = (int) ((this.viewDY * 0.45D) - (3 * ttfont3.height()));
            int l2 = 0xff0000ff;
            int j3 = (int) ((510F * ((Time.current() - this.logRightBottomTime) % logTimeFire)) / 5000F);
            if ((j3 -= 255) < 0) {
                j3 = -j3;
            }
            ttfont3.output(l2 | (j3 << 8), j - k1, j2, 0.0F, this.logRightBottom);
        }
        if (this.logLen == 0) {
            return;
        }
        for (; (this.logLen > 0) && (l >= (this.logTime[this.logPtr] + logTimeLife)); this.logLen--) {
            this.logPtr = (this.logPtr + 1) % 3;
        }

        if (this.logLen == 0) {
            return;
        }
        TTFont ttfont4 = TTFont.font[1];
        int k = (int) (this.viewDX * 0.95D);
        int l1 = ttfont4.height();
        int k2 = (int) (this.viewDY * 0.45D) - ((3 - this.logLen) * l1);
        for (int i3 = 0; i3 < this.logLen; i3++) {
            int k3 = (this.logPtr + i3) % 3;
            int l3 = 0xffff0000;
            if (l < (this.logTime[k3] + logTimeFire)) {
                int i4 = (int) ((((this.logTime[k3] + logTimeFire) - l) / 5000D) * 255D);
                l3 |= i4 | (i4 << 8);
            }
            float f4 = ttfont4.width(this.logBufStr[k3]);
            if (this.bDrawAllMessages) {
                ttfont4.output(l3, k - f4, k2, 0.0F, this.logBufStr[k3]);
            }
            k2 -= l1;
        }

    }

    private int __log(int i, String s) {
        if ((this.logLen > 0) && (i != 0)) {
            int j = ((this.logPtr + this.logLen) - 1) % 3;
            if (this.logBufId[j] == i) {
                this.logTime[j] = Time.current();
                this.logBuf[j] = s;
                return j;
            }
        }
        if (this.logLen >= 3) {
            this.logPtr = (this.logPtr + 1) % 3;
            this.logLen = 2;
        }
        int k = (this.logPtr + this.logLen) % 3;
        this.logBuf[k] = s;
        this.logBufId[k] = i;
        this.logTime[k] = Time.current();
        this.logLen++;
        return k;
    }

    private void syncStatUser(int i, NetUser netuser, boolean flag) {
        if (i == this.statUsers.size()) {
            this.statUsers.add(new StatUser());
        }
        StatUser statuser = (StatUser) this.statUsers.get(i);
        statuser.user = netuser;
        if ((statuser.iNum != (i + 1)) || (statuser.sNum == null)) {
            statuser.iNum = i + 1;
            statuser.sNum = statuser.iNum + ".";
        }
        if ((statuser.iPing != netuser.ping) || (statuser.sPing == null)) {
            statuser.iPing = netuser.ping;
            statuser.sPing = "(" + statuser.iPing + ")";
        }
        int j = (int) netuser.stat().score;
        if ((statuser.iScore != j) || (statuser.sScore == null)) {
            statuser.iScore = j;
            statuser.sScore = "" + statuser.iScore;
        }
        if ((statuser.iArmy != netuser.getArmy()) || (statuser.sArmy == null)) {
            statuser.iArmy = netuser.getArmy();
            statuser.sArmy = "(" + statuser.iArmy + ")" + I18N.army(Army.name(statuser.iArmy));
        }
        if (!Actor.isAlive(statuser.aAircraft) || (statuser.aAircraft.netUser() != netuser) || (statuser.sAircraft == null)) {
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
            this.armyScores = NetUserStat.armyScores;
        }
    }

    private void syncNetStat() {
        this.syncStatUser(0, (NetUser) NetEnv.host(), true);
        for (int i = 0; i < NetEnv.hosts().size(); i++) {
            this.syncStatUser(i + 1, (NetUser) NetEnv.hosts().get(i), false);
        }

        for (; this.statUsers.size() > (NetEnv.hosts().size() + 1); this.statUsers.remove(this.statUsers.size() - 1)) {
            ;
        }
    }

    private int x1024(float f) {
        return (int) ((this.viewDX / 1024F) * f);
    }

    private int y1024(float f) {
        return (int) ((this.viewDY / 768F) * f);
    }

    public void startNetStat() {
        if (this.bDrawNetStat) {
            return;
        }
        if (!Mission.isPlaying()) {
            return;
        }
        if (Mission.isSingle()) {
            return;
        } else {
            this.syncNetStat();
            TTFont ttfont = TTFont.font[1];
            int i = ttfont.height() - ttfont.descender();
            int j = this.y1024(740F);
            int k = 2 * i;
            this.pageSizeNetStat = (j - k) / i;
            this.bDrawNetStat = true;
            return;
        }
    }

    public void stopNetStat() {
        if (!this.bDrawNetStat) {
            return;
        } else {
            this.statUsers.clear();
            this.bDrawNetStat = false;
            this.pageNetStat = 0;
            return;
        }
    }

    public boolean isDrawNetStat() {
        return this.bDrawNetStat;
    }

    public void pageNetStat() {
        if (!this.bDrawNetStat) {
            return;
        }
        this.pageNetStat++;
        if ((this.pageSizeNetStat * this.pageNetStat) > this.statUsers.size()) {
            this.pageNetStat = 0;
        }
    }

    public void renderNetStat() {
        if (!this.bDrawNetStat) {
            return;
        }
        if (!Mission.isPlaying()) {
            return;
        }
        if (Mission.isSingle()) {
            return;
        }
        TTFont ttfont = TTFont.font[1];
        TTFont ttfont1 = TTFont.font[3];
        if (Main.cur().netServerParams.netStat_DisableStatistics) {
            return;
        }
        int i = ttfont.height() - ttfont.descender();
        int j = this.y1024(740F);
        int k = 0;
        int l = 0;
        int i1 = 0;
        int j1 = 0;
        int k1 = 0;
        int l1 = 0;
        for (int i2 = this.pageSizeNetStat * this.pageNetStat; (i2 < (this.pageSizeNetStat * (this.pageNetStat + 1))) && (i2 < this.statUsers.size()); i2++) {
            StatUser statuser = (StatUser) this.statUsers.get(i2);
            int l2 = 0;
            if (Main.cur().netServerParams.netStat_ShowPilotNumber) {
                l2 = (int) ttfont.width(statuser.sNum);
                if (k < l2) {
                    k = l2;
                }
            }
            if (Main.cur().netServerParams.netStat_ShowPilotPing) {
                l2 = (int) ttfont1.width(statuser.sPing);
                if (l < l2) {
                    l = l2;
                }
            }
            if (Main.cur().netServerParams.netStat_ShowPilotName) {
                l2 = (int) ttfont.width(statuser.user.uniqueName());
                if (i1 < l2) {
                    i1 = l2;
                }
            }
            if (Main.cur().netServerParams.netStat_ShowPilotScore) {
                l2 = (int) ttfont.width(statuser.sScore);
                if (j1 < l2) {
                    j1 = l2;
                }
            }
            if (Main.cur().netServerParams.netStat_ShowPilotArmy) {
                l2 = (int) ttfont.width(statuser.sArmy);
                if (k1 < l2) {
                    k1 = l2;
                }
            }
            if (!Main.cur().netServerParams.netStat_ShowPilotACDesignation) {
                continue;
            }
            l2 = (int) ttfont.width(statuser.sAircraft);
            if (l1 < l2) {
                l1 = l2;
            }
        }

        int j2 = this.x1024(40F) + k;
        int k2 = j2 + l + this.x1024(16F);
        int i3 = k2 + i1 + this.x1024(16F);
        int j3 = i3 + j1 + this.x1024(16F);
        if (Mission.isCoop()) {
            j3 = i3;
        }
        int k3 = j3 + k1 + this.x1024(16F);
        int l3 = k3 + l1 + this.x1024(16F);
        int i4 = j;
        for (int k4 = this.pageSizeNetStat * this.pageNetStat; (k4 < (this.pageSizeNetStat * (this.pageNetStat + 1))) && (k4 < this.statUsers.size()); k4++) {
            StatUser statuser1 = (StatUser) this.statUsers.get(k4);
            i4 -= i;
            int j5 = Army.color(statuser1.iArmy);
            if (!Main.cur().netServerParams.netStat_ShowPilotArmy) {
                j5 = -1;
            }
            if (Main.cur().netServerParams.netStat_ShowPilotNumber) {
                ttfont.output(j5, j2 - ttfont.width(statuser1.sNum), i4, 0.0F, statuser1.sNum);
            }
            if (Main.cur().netServerParams.netStat_ShowPilotPing) {
                ttfont1.output(-1, k2 - ttfont1.width(statuser1.sPing) - this.x1024(4F), i4, 0.0F, statuser1.sPing);
            }
            if (Main.cur().netServerParams.netStat_ShowPilotName) {
                ttfont.output(j5, k2, i4, 0.0F, statuser1.user.uniqueName());
            }
            if (!Mission.isCoop() && Main.cur().netServerParams.netStat_ShowPilotScore) {
                ttfont.output(j5, i3, i4, 0.0F, statuser1.sScore);
            }
            if (Main.cur().netServerParams.netStat_ShowPilotArmy) {
                ttfont.output(j5, j3, i4, 0.0F, statuser1.sArmy);
            }
            if (Main.cur().netServerParams.netStat_ShowPilotACDesignation) {
                ttfont.output(j5, k3, i4, 0.0F, statuser1.sAircraft);
            }
            if (Main.cur().netServerParams.netStat_ShowPilotACType) {
                ttfont.output(j5, l3, i4, 0.0F, statuser1.sAircraftType);
            }
        }

        if (!Mission.isCoop() && Main.cur().netServerParams.netStat_ShowTeamScore) {
            int j4 = j;
            j4 -= i;
            ttfont.output(Army.color(0), this.x1024(800F), j4, 0.0F, this.resGUI.getString("netStat.TeamScore"));
            j4 -= i;
            for (int l4 = 1; l4 < this.armyScores.length; l4++) {
                for (int i5 = 0; i5 < this.statUsers.size(); i5++) {
                    StatUser statuser2 = (StatUser) this.statUsers.get(i5);
                    if (statuser2.iArmy == l4) {
                        int k5 = Army.color(l4);
                        j4 -= i;
                        ttfont.output(k5, this.x1024(800F), j4, 0.0F, this.resGUI.getString(Army.name(l4)) + ": " + this.armyScores[l4]);
                    }
                }

            }

        }
    }

    public static void addPointer(float f, float f1, int i, float f2, float f3) {
        Main3D.cur3D().hud._addPointer(f, f1, i, f2, f3);
    }

    private void _addPointer(float f, float f1, int i, float f2, float f3) {
        if (this.nPointers == this.pointers.size()) {
            this.pointers.add(new Ptr(f, f1, i, f2, f3));
        } else {
            Ptr ptr = (Ptr) this.pointers.get(this.nPointers);
            ptr.set(f, f1, i, f2, f3);
        }
        this.nPointers++;
    }

    private void renderPointers() {
        if (this.nPointers == 0) {
            return;
        }
        float f = this.viewDX / 1024F;
        float f1 = (float) this.viewDX / (float) this.viewDY;
        float f2 = 1.333333F / f1;
        f *= f2;
        int i = IconDraw.scrSizeX();
        int j = IconDraw.scrSizeY();
        for (int k = 0; k < this.nPointers; k++) {
            Ptr ptr = (Ptr) this.pointers.get(k);
            int l = (int) (64F * f * ptr.alpha);
            IconDraw.setScrSize(l, l);
            IconDraw.setColor((ptr.color & 0xffffff) | ((int) (ptr.alpha * 255F) << 24));
            IconDraw.render(this.spritePointer, ptr.x, ptr.y, 90F - ptr.angle);
        }

        IconDraw.setScrSize(i, j);
        this.nPointers = 0;
    }

    public void clearPointers() {
        this.nPointers = 0;
    }

    private void initPointers() {
        this.spritePointer = Mat.New("gui/game/hud/pointer.mat");
    }

    private void preRenderDashBoard() {
        if (!Actor.isValid(World.getPlayerAircraft())) {
            return;
        }
        if (!Actor.isValid(this.main3d.cockpitCur)) {
            return;
        }
        if (this.main3d.isViewOutside()) {
            if (this.main3d.viewActor() != World.getPlayerAircraft()) {
                return;
            }
            if (!this.main3d.cockpitCur.isNullShow()) {
                return;
            }
        } else if (this.main3d.isViewInsideShow()) {
            return;
        }
        if (!this.bDrawDashBoard) {
            return;
        } else {
            this.spriteLeft.preRender();
            this.spriteRight.preRender();
            this.meshNeedle1.preRender();
            this.meshNeedle2.preRender();
            this.meshNeedle3.preRender();
            this.meshNeedle4.preRender();
            this.meshNeedle5.preRender();
            this.meshNeedle6.preRender();
            this.meshNeedleMask.preRender();
            return;
        }
    }

    private void renderDashBoard() {
        if (!Actor.isValid(World.getPlayerAircraft())) {
            return;
        }
        if (!Actor.isValid(this.main3d.cockpitCur)) {
            return;
        }
        if (this.main3d.isViewOutside()) {
            if (this.main3d.viewActor() != World.getPlayerAircraft()) {
                return;
            }
            if (!this.main3d.cockpitCur.isNullShow()) {
                return;
            }
        } else if (this.main3d.isViewInsideShow()) {
            return;
        }
        if (!this.bDrawDashBoard) {
            return;
        }
        float f1 = this.viewDX;
        float f2 = this.viewDY;
        float f3 = f1 / 1024F;
        float f4 = f2 / 768F;
        Render.drawTile(0.0F, 0.0F, 256F * f3, 256F * f4, 0.0F, this.spriteLeft, -1, 0.0F, 1.0F, 1.0F, -1F);
        Render.drawTile(768F * f3, 0.0F, 256F * f3, 256F * f4, 0.0F, this.spriteRight, -1, 0.0F, 1.0F, 1.0F, -1F);
        Point3d localPoint3d = World.getPlayerAircraft().pos.getAbsPoint();
        Orient localOrient1 = World.getPlayerAircraft().pos.getAbsOrient();
        float f5 = (float) (localPoint3d.z - World.land().HQ(localPoint3d.x, localPoint3d.y));
        this._p.x = 172F * f3;
        this._p.y = 84F * f4;
        this._o.set(this.cvt(f5, 0.0F, 10000F, 0.0F, 3600F), 0.0F, 0.0F);
        this.meshNeedle2.setPos(this._p, this._o);
        this.meshNeedle2.render();
        this._o.set(this.cvt(f5, 0.0F, 10000F, 0.0F, 360F), 0.0F, 0.0F);
        this.meshNeedle1.setPos(this._p, this._o);
        this.meshNeedle1.render();
        String str1 = "" + (int) (f5 + 0.5D);
        float f7 = this.fntLcd.width(str1);
        this.fntLcd.output(-1, (208F * f3) - f7, 70F * f4, 0.0F, str1);
        if (f5 > 90F) {
            this.meshNeedle5.setScale(90F * f3);
        } else {
            this.meshNeedle5.setScaleXYZ(90F * f3, 90F * f3, this.cvt(f5, 0.0F, 90F, 13F, 90F) * f3);
        }
        f5 = (float) World.getPlayerAircraft().getSpeed(null);
        f5 *= 3.6F;
        this._o.set(this.cvt(f5, 0.0F, 900F, 0.0F, 270F) + 180F, 0.0F, 0.0F);
        this._p.x = 83F * f3;
        this._p.y = 167F * f4;
        this.meshNeedle2.setPos(this._p, this._o);
        this.meshNeedle2.render();
        str1 = "" + (int) (f5 + 0.5D);
        f7 = this.fntLcd.width(str1);
        this.fntLcd.output(-1, (104F * f3) - f7, 135F * f4, 0.0F, str1);
        for (f5 = localOrient1.azimut() + 90F; f5 < 0.0F; f5 += 360F) {
            ;
        }
        f5 %= 360F;
        this._o.set(f5, 0.0F, 0.0F);
        this._p.x = 939F * f3;
        this._p.y = 167F * f4;
        this.meshNeedle3.setPos(this._p, this._o);
        this.meshNeedle3.render();
        str1 = "" + (int) (f5 + 0.5D);
        f7 = this.fntLcd.width(str1);
        this.fntLcd.output(-1, (960F * f3) - f7, 216F * f4, 0.0F, str1);
        Orient localOrient2 = this.main3d.camera3D.pos.getAbsOrient();
        this._p.x = 511F * f3;
        this._p.y = 96F * f4;
        if (localOrient2.tangage() < 0.0F) {
            this._o1.set(localOrient2);
            this._o1.increment(0.0F, 0.0F, 90F);
            this._o1.increment(0.0F, 90F, 0.0F);
            this._o.sub(this._oNull, this._o1);
            this.meshNeedle5.setPos(this._p, this._o);
            this.meshNeedle5.render();
        }
        this._o1.set(localOrient2);
        this._o1.increment(0.0F, 0.0F, 90F);
        this._o1.increment(0.0F, 90F, 0.0F);
        this._o.sub(localOrient1, this._o1);
        this.meshNeedle4.setPos(this._p, this._o);
        this.meshNeedle4.render();
        if (localOrient2.tangage() >= 0.0F) {
            this._o1.set(localOrient2);
            this._o1.increment(0.0F, 0.0F, 90F);
            this._o1.increment(0.0F, 90F, 0.0F);
            this._o.sub(this._oNull, this._o1);
            this.meshNeedle5.setPos(this._p, this._o);
            this.meshNeedle5.render();
        }
        this._p.x = 851F * f3;
        this._p.y = 84F * f4;
        this._o1.set(localOrient1);
        this._o1.set(0.0F, -this._o1.tangage(), this._o1.kren());
        this._o1.increment(0.0F, 0.0F, 90F);
        this._o1.increment(0.0F, 90F, 0.0F);
        this._o.sub(this._oNull, this._o1);
        this.meshNeedle6.setPos(this._p, this._o);
        this.meshNeedle6.render();
        this._o.set(0.0F, 0.0F, 0.0F);
        this.meshNeedleMask.setPos(this._p, this._o);
        this.meshNeedleMask.render();
        int i = (int) (World.getPlayerFM().getOverload() * 10F);
        float f6 = i / 10F;
        String str2 = "" + f6;
        float f8 = this.fntLcd.width(str2);
        if (World.getPlayerFM().getLoadDiff() < (World.getPlayerFM().getLimitLoad() * 0.25F)) {
            this.fntLcd.output(0xff0000ff, (215F * f3) - f8, 182F * f4, 0.0F, str2);
        } else if (i < 0) {
            this.fntLcd.output(0xff000000, (215F * f3) - f8, 182F * f4, 0.0F, str2);
        } else {
            this.fntLcd.output(-1, (215F * f3) - f8, 182F * f4, 0.0F, str2);
        }
    }

    private float cvt(float f, float f1, float f2, float f3, float f4) {
        f = Math.min(Math.max(f, f1), f2);
        return f3 + (((f4 - f3) * (f - f1)) / (f2 - f1));
    }

    private void initDashBoard() {
        if (this.bMphKmhHUD) {
            this.spriteLeft = Mat.New("gui/game/hud/hudleftMphKmh.mat");
        } else if (this.bMphHUD) {
            this.spriteLeft = Mat.New("gui/game/hud/hudleftMph.mat");
        } else {
            this.spriteLeft = Mat.New("gui/game/hud/hudleft.mat");
        }
        this.spriteLeft = Mat.New("gui/game/hud/hudleft.mat");
        this.spriteRight = Mat.New("gui/game/hud/hudright.mat");
        this.meshNeedle1 = new Mesh("gui/game/hud/needle1/mono.sim");
        this.meshNeedle2 = new Mesh("gui/game/hud/needle2/mono.sim");
        this.meshNeedle3 = new Mesh("gui/game/hud/needle3/mono.sim");
        this.meshNeedle4 = new Mesh("gui/game/hud/needle4/mono.sim");
        this.meshNeedle5 = new Mesh("gui/game/hud/needle5/mono.sim");
        this.meshNeedle6 = new Mesh("gui/game/hud/needle6/mono.sim");
        this.meshNeedleMask = new Mesh("gui/game/hud/needlemask/mono.sim");
        this.fntLcd = TTFont.get("lcdnova");
        this.setScales();
    }

    private void setScales() {
        float f = this.viewDX;
        float f1 = f / 1024F;
        float f2 = 1.333333F / (f / this.viewDY);
        if ((this.viewDX / this.viewDY) < 1) {
            f1 *= 0.75F;
            f2 *= 0.75F;
        }
        this.meshNeedle1.setScale(140F * f1 * f2);
        this.meshNeedle2.setScale(140F * f1 * f2);
        this.meshNeedle3.setScale(75F * f1 * f2);
        this.meshNeedle4.setScale(100F * f1 * f2);
        this.meshNeedle5.setScale(90F * f1 * f2);
        this.meshNeedle6.setScale(150F * f1 * f2);
        this.meshNeedleMask.setScale(150F * f1 * f2);
    }

    public void render() {
        this.renderSpeed();
        this.renderOrder();
        this.renderMsg();
        this.renderTraining();
        this.renderLog();
        this.renderDashBoard();
        this.renderPointers();
        this.renderNetStat();
        this.renderRec();
    }

    public void preRender() {
        this.preRenderDashBoard();
    }

    public void resetGame() {
        this.setScales();
        this.clearSpeed();
        this.clearOrder();
        this.clearMsg();
        this.clearTraining();
        this.clearLog();
        this.clearPointers();
        this.stopNetStat();
    }

    public void contextResize(int i, int j) {
        this.viewDX = this.main3d.renderHUD.getViewPortWidth();
        this.viewDY = this.main3d.renderHUD.getViewPortHeight();
        this.setScales();
        this.resetMsgSizes();
        this.resetTrainingSizes();
    }

    private void renderRec() {
        if (!NetMissionTrack.isRecording() || !this.bRec) {
            return;
        }
        float f = this.viewDX;
        float f1 = this.viewDY;
        float f2 = f / 1024F;
        float f3 = f1 / 768F;
        float f4 = 1.333333F / (f / f1);
        if (NetMissionTrack.isPlaying()) {
//            TTFont ttfont = TTFont.font[1];
//            int i = ttfont.height();
            Render.drawTile(828F * f2, 2.0F * f3, 16F * f2 * f4, 16F * f3, 0.0F, this.spriteRec, -1, 0.0F, 1.0F, 1.0F, -1F);
        } else {
            Render.drawTile(896F * f2, 2.0F * f3, 16F * f2 * f4, 16F * f3, 0.0F, this.spriteRec, -1, 0.0F, 1.0F, 1.0F, -1F);
        }
    }

    private void initRec() {
        this.spriteRec = Mat.New("gui/game/recIndicator.mat");
    }

    public HUD() {
        this.bDrawAllMessages = true;
        this.bDrawVoiceMessages = true;
        this.bNoSubTitles = false;
        this.subTitlesLines = 11;
        this.bNoHudLog = false;
        this.timeLoadLimit = 0L;
        this.bRec = false;
        this.iDrawSpeed = 1;
        this.lastDrawSpeed = -1;
        this.msgLines = new ArrayList();
        this.trainingLines = new ArrayList();
        this.bCoopTimeStart = false;
        this.logBuf = new String[lenLogBuf];
        this.logBufStr = new String[lenLogBuf];
        this.logBufId = new int[lenLogBuf];
        this.logTime = new long[lenLogBuf];
        this.logPtr = 0;
        this.logLen = 0;
        this.bDrawNetStat = false;
        this.pageNetStat = 0;
        this.pageSizeNetStat = 0;
        this.statUsers = new ArrayList();
        this.pointers = new ArrayList();
        this.nPointers = 0;
        this.bDrawDashBoard = false;
        this._p = new Point3d();
        this._o = new Orient();
        this._o1 = new Orient();
        this._oNull = new Orient(0.0F, 0.0F, 0.0F);
        this.main3d = Main3D.cur3D();
        this.viewDX = this.main3d.renderHUD.getViewPortWidth();
        this.viewDY = this.main3d.renderHUD.getViewPortHeight();
        this.speedbarMultiplierKMH = 0.2F;
        this.speedbarMultiplierMPH = 0.2F;
        this.speedbarMultiplierKnots = 0.2F;
        this.speedbarMultiplierMeters = 0.2F;
        this.speedbarMultiplierFeet = 0.1F;
        this.bMixedMessage = Config.cur.ini.get("Mods", "MixedSpeedbar", false);
        this.bMessageFull = Config.cur.ini.get("Mods", "FullSpeedbar", false);
        this.bShowSIToo = Config.cur.ini.get("Mods", "ShowSIToo", true);
        this.bMphHUD = false;
        this.bMphKmhHUD = false;
//        HUDSpeedMultiplier1 = 3.6F;
//        HUDSpeedMultiplier2 = 1.0F;
//        HUDAltitudeDigitalMultiplier = 1.0F;
//        HUDAltitudeAnalogMultiplier = 1.0F;
        this.bUseColor = Config.cur.ini.get("Mods", "PALMODsColor", true);
        this.BombSightAssist = Config.cur.ini.get("Mods", "BombSightAssist", 1);
        this.BombSightAssistConf = Config.cur.ini.get("Mods", "BombSightAssistConf", 1);
        this.speedbarUnits = Config.cur.ini.get("Mods", "SpeedbarUnits", 1);
        this.speedbarSpdKMH = Config.cur.ini.get("Mods", "SpeedbarSpdKMH", 5);
        this.speedbarSpdMPH = Config.cur.ini.get("Mods", "SpeedbarSpdMPH", 5);
        this.speedbarSpdKnots = Config.cur.ini.get("Mods", "SpeedbarSpdKnots", 5);
        this.speedbarAltMeters = Config.cur.ini.get("Mods", "SpeedbarAltMeters", 5);
        this.speedbarAltFeet = Config.cur.ini.get("Mods", "SpeedbarAltFeet", 10);
        if ((this.speedbarUnits < 0) || (this.speedbarUnits > 6)) {
            this.speedbarUnits = 1;
        }
        if ((this.speedbarSpdKMH >= 1) && (this.speedbarSpdKMH <= 10)) {
            this.speedbarMultiplierKMH = 1.0F / this.speedbarSpdKMH;
        } else {
            this.speedbarSpdKMH = 5;
        }
        if ((this.speedbarSpdMPH >= 1) && (this.speedbarSpdMPH <= 10)) {
            this.speedbarMultiplierMPH = 1.0F / this.speedbarSpdMPH;
        } else {
            this.speedbarSpdMPH = 5;
        }
        if ((this.speedbarSpdKnots >= 1) && (this.speedbarSpdKnots <= 10)) {
            this.speedbarMultiplierKnots = 1.0F / this.speedbarSpdKnots;
        } else {
            this.speedbarSpdKnots = 5;
        }
        if ((this.speedbarAltMeters >= 1) && (this.speedbarAltMeters <= 10)) {
            this.speedbarMultiplierMeters = 1.0F / this.speedbarAltMeters;
        } else {
            this.speedbarAltMeters = 5;
        }
        if ((this.speedbarAltFeet >= 1) && (this.speedbarAltFeet <= 50)) {
            this.speedbarMultiplierFeet = 1.0F / this.speedbarAltFeet;
        } else {
            this.speedbarAltFeet = 10;
        }
        int i = Config.cur.ini.get("Mods", "HUDGauges", 1);
        if (i == 3) {
            this.bMphKmhHUD = true;
//            HUDSpeedMultiplier1 = 2.236936F;
//            HUDSpeedMultiplier2 = 1.609344F;
//            HUDAltitudeDigitalMultiplier = 3.28084F;
//            HUDAltitudeAnalogMultiplier = 1.0F;
        } else if (i == 2) {
            this.bMphHUD = true;
//            HUDSpeedMultiplier1 = 2.236936F;
//            HUDSpeedMultiplier2 = 1.0F;
//            HUDAltitudeDigitalMultiplier = 3.28084F;
//            HUDAltitudeAnalogMultiplier = 3.28084F;
        }
        this.sbGMode = Config.cur.ini.get("Mods", "SpeedbarGMode", 1);
        this.sbTrimSet = Config.cur.ini.get("Mods", "SpeedbarTrimSet", 0.0F);
        this.sbFlapsSet = Config.cur.ini.get("Mods", "SpeedbarFlapsSet", 0.0F);
        this.sbAoASet = Config.cur.ini.get("Mods", "SpeedbarAoASet", 0.0F);
        this.sbVSpeedSet = Config.cur.ini.get("Mods", "SpeedbarVSpeedSet", 0.0F);
        this.sbRPMSet = Config.cur.ini.get("Mods", "SpeedbarRPMSet", 0.0F);
        this.sbTempSet = Config.cur.ini.get("Mods", "SpeedbarTempSet", 0.0F);
        this.sbHPSet = Config.cur.ini.get("Mods", "SpeedbarHPSet", 0.0F);
        this.sbWeightSet = Config.cur.ini.get("Mods", "SpeedbarWeightSet", 0.0F);
        this.sbDragSet = Config.cur.ini.get("Mods", "SpeedbarDragSet", 0.0F);
        this.initSpeed();
        this.initOrder();
        this.initMsg();
        this.initTraining();
        this.initLog();
        this.initDashBoard();
        this.initPointers();
        this.initRec();
        this.bRec = Config.cur.ini.get("game", "RecordingIndicator", this.bRec);
        this.bNoSubTitles = Config.cur.ini.get("game", "NoSubTitles", this.bNoSubTitles);
        this.subTitlesLines = Config.cur.ini.get("game", "SubTitlesLines", this.subTitlesLines);
        if (this.subTitlesLines < 1) {
            this.subTitlesLines = 1;
        }
        this.resGUI = ResourceBundle.getBundle("i18n/gui", RTSConf.cur.locale, LDRres.loader());
        this.bNoHudLog = Config.cur.ini.get("game", "NoHudLog", this.bNoHudLog);
    }

    public boolean            bDrawAllMessages;
    public boolean            bDrawVoiceMessages;
    private boolean           bNoSubTitles;
    private int               subTitlesLines;
    private boolean           bNoHudLog;
    private Main3D            main3d;
    private int               viewDX;
    private int               viewDY;
    long                      timeLoadLimit;
    int                       cnt;
    public boolean            bRec;
    private String            renderSpeedSubstrings[][] = { { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null }, { null, null, null } };
    private int               iDrawSpeed;
    private int               lastDrawSpeed;
    private Order             order[];
    private String            orderStr[];
    public ResourceBundle     resOrder;
    public ResourceBundle     resControl;
    public ResourceBundle     resGUI;
    public ResourceBundle     resMsg;
    private int               msgX0;
    private int               msgY0;
    private int               msgDX;
    private int               msgDY;
    private int               msgSIZE;
    private int               msgSpaceLen;
    private ArrayList         msgLines;
    private int               msgColor[][]              = { { 0xcf0000ff, 0xcf0000ff, 0xcf0000ff, 0xcf005f9f, 0xcf003fdf, 0xcf0000ff, 0xcf0000ff, 0xcf0000ff, 0xcf0000ff }, { 0xcfff0000, 0xcfff0000, 0xcfff0000, 0xcf9f5f00, 0xcfdf3f00, 0xcfff0000, 0xcfff0000, 0xcfff0000, 0xcfff0000 } };
    private int               trainingX0;
    private int               trainingY0;
    private int               trainingDX;
    private int               trainingDY;
    private int               trainingSIZE;
    private int               trainingSpaceLen;
    private ArrayList         trainingLines;
    private static int        idLog                     = 1;
    public ResourceBundle     resLog;
    private String            logRightBottom;
    private long              logRightBottomTime;
    private static final long logCenterTimeLife         = 5000L;
    private String            logCenter;
    private long              logCenterTime;
    private String            logIntro;
    private String            logIntroESC;
    private TTFont            fntCenter;
    private boolean           bCoopTimeStart;
    private long              coopTimeStart;
    private static final int  lenLogBuf                 = 3;
    private static final long logTimeLife               = 10000L;
    private static final long logTimeFire               = 5000L;
    private String            logBuf[];
    private String            logBufStr[];
    private int               logBufId[];
    private long              logTime[];
    private int               logPtr;
    private int               logLen;
    private boolean           bDrawNetStat;
    private int               pageNetStat;
    private int               armyScores[];
    private int               pageSizeNetStat;
    ArrayList                 statUsers;
    private ArrayList         pointers;
    private int               nPointers;
    private Mat               spritePointer;
    public boolean            bDrawDashBoard;
    private Point3d           _p;
    private Orient            _o;
    private Orient            _o1;
    private Orient            _oNull;
    private Mat               spriteLeft;
    private Mat               spriteRight;
    private Mesh              meshNeedle1;
    private Mesh              meshNeedle2;
    private Mesh              meshNeedle3;
    private Mesh              meshNeedle4;
    private Mesh              meshNeedle5;
    private Mesh              meshNeedle6;
    private Mesh              meshNeedleMask;
    private TTFont            fntLcd;
    private Mat               spriteRec;
    private int               speedbarSpdKMH;
    private int               speedbarSpdMPH;
    private int               speedbarSpdKnots;
    private int               speedbarAltMeters;
    private int               speedbarAltFeet;
    private float             speedbarMultiplierKMH;
    private float             speedbarMultiplierMPH;
    private float             speedbarMultiplierKnots;
    private float             speedbarMultiplierMeters;
    private float             speedbarMultiplierFeet;
    private int               speedbarUnits;
    private boolean           bUseColor;
    private int               BombSightAssist;
    private int               BombSightAssistConf;
    private boolean           bMixedMessage;
    private boolean           bMessageFull;
    private boolean           bShowSIToo;
    private boolean           bMphHUD;
    private boolean           bMphKmhHUD;
//    private float HUDSpeedMultiplier1;
//    private float HUDSpeedMultiplier2;
//    private float HUDAltitudeDigitalMultiplier;
//    private float HUDAltitudeAnalogMultiplier;
    private String            sTAS;
    private boolean           bSI;
    private boolean           bIsSI;
    private int               sbGMode;
    private float             sbTrimSet;
    private float             sbFlapsSet;
    private float             sbAoASet;
    private float             sbVSpeedSet;
    private float             sbRPMSet;
    private float             sbTempSet;
    private float             sbHPSet;
    private float             sbWeightSet;
    private float             sbDragSet;

}
