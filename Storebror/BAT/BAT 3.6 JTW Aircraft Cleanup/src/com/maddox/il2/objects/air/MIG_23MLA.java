package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class MIG_23MLA extends MIG_23 implements TypeLaserSpotter {

    public MIG_23MLA() {
        this.APmode3 = false;
        this.counter = 0;
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
        this.fxSPO10 = this.newSound("aircraft.Sirena2", false);
        this.smplSPO10 = new Sample("sample.Sirena2.wav", 256, 65535);
        this.smplSPO10.setInfinite(true);
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 7) {
            this.k14Mode = 0;
        }
        if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
            switch (this.k14Mode) {
                case 0:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed");
                    break;

                case 1:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + Gsh-23 AA");
                    break;

                case 2:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Fixed + GSh-23 AG");
                    break;

                case 3:
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "ASP-PFD: Off");
                    break;
            }
        }
        return true;
    }

    public void rareAction(float f, boolean flag) {
        if ((this.raretimer != Time.current()) && (this == World.getPlayerAircraft())) {
            if ((this.counter++ % 5) == 0) {
                this.SAPFIR23();
            }
            if ((this.counter++ % 12) == 3) {
                this.IRST();
            }
        }
        super.rareAction(f, flag);
        this.raretimer = Time.current();
    }

    private boolean SAPFIR23() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof MIG_23MLA) {
            flag1 = true;
        }
        if (d3 < 0.0D) {
            d3 = 0.0D;
        }
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D)) {
                this.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                Engine.land();
                if ((d2 - d6 - 300D) >= 0.0D) {
                }
                if (((d2 - d6) + 300D) <= 0.0D) {
                }
                if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                }
                if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) {
                }
                new String();
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float) Math.atan2(d9, -d8);
                int k = (int) (Math.floor((int) f) - 90D);
                if (k < 0) {
                    k += 360;
                }
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((random.nextInt(20) - 10F) / 100F) + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 57000F;
                float f3 = f2;
                if (d3 < 1200D) {
                    f3 = (float) (d3 * 0.8D * 3D);
                }
                int j1 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f1) / 10D) * 10D);
                if (j1 > f2) {
                    j1 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                }
                float f4 = 57.32484F * (float) Math.atan2(j1, d7);
                int k1 = (int) (Math.floor((int) f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int i2 = (int) f2;
                if (j1 < f2) {
                    if (j1 > 1150) {
                        i2 = (int) (Math.ceil(j1 / 900D) * 900D);
                    } else {
                        i2 = (int) (Math.ceil(j1 / 500D) * 500D);
                    }
                }
                int j2 = l + i1;
                int k2 = j2;
                if (k2 < 0) {
                    k2 += 360;
                }
                float f5 = (float) (f3 + (Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * (f3 * 0.25D)));
                int l2 = (int) (f5 * Math.cos(Math.toRadians(l1)));
                String s1 = "  ";
                if (k2 < 5) {
                    s1 = "Dead ahead, ";
                }
                if ((k2 >= 5) && (k2 <= 7.5D)) {
                    s1 = "Right 5, ";
                }
                if ((k2 > 7.5D) && (k2 <= 12.5D)) {
                    s1 = "Right 10, ";
                }
                if ((k2 > 12.5D) && (k2 <= 17.5D)) {
                    s1 = "Right 15, ";
                }
                if ((k2 > 17.5D) && (k2 <= 25)) {
                    s1 = "Right 20, ";
                }
                if ((k2 > 25) && (k2 <= 35)) {
                    s1 = "Right 30, ";
                }
                if ((k2 > 35) && (k2 <= 45)) {
                    s1 = "Right 40, ";
                }
                if ((k2 > 45) && (k2 <= 60)) {
                    s1 = "Turn right, ";
                }
                if (k2 > 355) {
                    s1 = "Dead ahead, ";
                }
                if ((k2 <= 355) && (k2 >= 352.5D)) {
                    s1 = "Left 5, ";
                }
                if ((k2 < 352.5D) && (k2 >= 347.5D)) {
                    s1 = "Left 10, ";
                }
                if ((k2 < 347.5D) && (k2 >= 342.5D)) {
                    s1 = "Left 15, ";
                }
                if ((k2 < 342.5D) && (k2 >= 335)) {
                    s1 = "Left 20, ";
                }
                if ((k2 < 335) && (k2 >= 325)) {
                    s1 = "Left 30, ";
                }
                if ((k2 < 325) && (k2 >= 315)) {
                    s1 = "Left 40, ";
                }
                if ((k2 < 345) && (k2 >= 300)) {
                    s1 = "Turn left, ";
                }
                String s2 = "  ";
                if (l1 < -10) {
                    s2 = "nose down";
                }
                if ((l1 >= -10) && (l1 <= -5)) {
                    s2 = "down a bit";
                }
                if ((l1 > -5) && (l1 < 5)) {
                    s2 = "level";
                }
                if ((l1 <= 10) && (l1 >= 5)) {
                    s2 = "up a bit";
                }
                if (l1 > 10) {
                    s2 = "pull up";
                }
                if (k2 < 5) {
                }
                if ((k2 >= 5) && (k2 <= 7.5D)) {
                }
                if ((k2 > 7.5D) && (k2 <= 12.5D)) {
                }
                if ((k2 > 12.5D) && (k2 <= 17.5D)) {
                }
                if ((k2 > 17.5D) && (k2 <= 25)) {
                }
                if ((k2 > 25) && (k2 <= 35)) {
                }
                if ((k2 > 35) && (k2 <= 45)) {
                }
                if ((k2 > 45) && (k2 <= 60)) {
                }
                if (k2 > 355) {
                }
                if ((k2 <= 355) && (k2 >= 352.5D)) {
                }
                if ((k2 < 352.5D) && (k2 >= 347.5D)) {
                }
                if ((k2 < 347.5D) && (k2 >= 342.5D)) {
                }
                if ((k2 < 342.5D) && (k2 >= 335)) {
                }
                if ((k2 < 335) && (k2 >= 325)) {
                }
                if ((k2 < 325) && (k2 >= 315)) {
                }
                if ((k2 < 345) && (k2 >= 300)) {
                }
                if (((double) j1 <= (double) l2) && (j1 >= 100D) && (j1 >= 700D) && (l1 >= -20) && (l1 <= 20) && (Math.sqrt(j2 * j2) <= 40D)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "Contact " + s1 + s2 + ", " + i2 + "m");
                    this.freq = 3;
                } else {
                    this.freq = 7;
                }
                this.setTimer(this.freq);
            }
        }

        return true;
    }

    public void setTimer(int i) {
        Random random = new Random();
        this.Timer1 = (float) (random.nextInt(i) * 0.1D);
        this.Timer2 = (float) (random.nextInt(i) * 0.1D);
    }

    public void resetTimer(float f) {
        this.Timer1 = f;
        this.Timer2 = f;
    }

    private boolean IRST() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof MIG_23MLA) {
            flag1 = true;
        }
        if (d3 < 0.0D) {
            d3 = 0.0D;
        }
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i += 360;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j += 360;
        }
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D) && ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) >= 1.0F))) {
                this.pos.getAbs(point3d);
                double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                double d7 = (int) (Math.ceil((d2 - d6) / 10D) * 10D);
                String s = "level with us";
                if ((d2 - d6 - 300D) >= 0.0D) {
                    s = "below us";
                }
                if (((d2 - d6) + 300D) <= 0.0D) {
                    s = "above us";
                }
                if (((d2 - d6 - 300D) < 0.0D) && ((d2 - d6 - 150D) >= 0.0D)) {
                    s = "slightly below";
                }
                if ((((d2 - d6) + 300D) > 0.0D) && (((d2 - d6) + 150D) < 0.0D)) {
                    s = "slightly above";
                }
                double d8 = d4 - d;
                double d9 = d5 - d1;
                float f = 57.32484F * (float) Math.atan2(d9, -d8);
                int k = (int) (Math.floor((int) f) - 90D);
                if (k < 0) {
                    k += 360;
                }
                int l = k - i;
                double d10 = d - d4;
                double d11 = d1 - d5;
                Random random = new Random();
                float f1 = ((random.nextInt(20) - 10F) / 100F) + 1.0F;
                int i1 = random.nextInt(6) - 3;
                float f2 = 35000F;
                float f3 = f2;
                if (d3 < 15D) {
                    f3 = (float) (d3 * 0.8D * 3D);
                }
                int j1 = (int) (Math.ceil(Math.sqrt(((d11 * d11) + (d10 * d10)) * f1) / 10D) * 10D);
                if (j1 > f2) {
                    j1 = (int) (Math.ceil(Math.sqrt((d11 * d11) + (d10 * d10)) / 10D) * 10D);
                }
                float f4 = 57.32484F * (float) Math.atan2(j1, d7);
                int k1 = (int) (Math.floor((int) f4) - 90D);
                int l1 = (k1 - (90 - j)) + i1;
                int j2 = l + i1;
                int k2 = j2;
                if (k2 < 0) {
                    k2 += 360;
                }
                float f5 = (float) (f3 + (Math.sin(Math.toRadians(Math.sqrt(l * l) * 3D)) * (f3 * 0.25D)));
                int l2 = (int) (f5 * Math.cos(Math.toRadians(l1)));
                if (k2 < 5) {
                }
                if ((k2 >= 5) && (k2 <= 7.5D)) {
                }
                if ((k2 > 7.5D) && (k2 <= 12.5D)) {
                }
                if ((k2 > 12.5D) && (k2 <= 17.5D)) {
                }
                if ((k2 > 17.5D) && (k2 <= 25)) {
                }
                if ((k2 > 25) && (k2 <= 35)) {
                }
                if ((k2 > 35) && (k2 <= 45)) {
                }
                if ((k2 > 45) && (k2 <= 60)) {
                }
                if (k2 > 355) {
                }
                if ((k2 <= 355) && (k2 >= 352.5D)) {
                }
                if ((k2 < 352.5D) && (k2 >= 347.5D)) {
                }
                if ((k2 < 347.5D) && (k2 >= 342.5D)) {
                }
                if ((k2 < 342.5D) && (k2 >= 335)) {
                }
                if ((k2 < 335) && (k2 >= 325)) {
                }
                if ((k2 < 325) && (k2 >= 315)) {
                }
                if ((k2 < 345) && (k2 >= 300)) {
                }
                if (l1 < -10) {
                }
                if ((l1 >= -10) && (l1 <= -5)) {
                }
                if ((l1 > -5) && (l1 < 5)) {
                }
                if ((l1 <= 10) && (l1 >= 5)) {
                }
                if (l1 > 10) {
                }
                String s3 = "  ";
                if (k2 < 5) {
                    s3 = "dead ahead, ";
                }
                if ((k2 >= 5) && (k2 <= 7.5D)) {
                    s3 = "right by 5\260, ";
                }
                if ((k2 > 7.5D) && (k2 <= 12.5D)) {
                    s3 = "right by 10\260, ";
                }
                if ((k2 > 12.5D) && (k2 <= 17.5D)) {
                    s3 = "right by 15\260, ";
                }
                if ((k2 > 17.5D) && (k2 <= 25)) {
                    s3 = "right by 20\260, ";
                }
                if ((k2 > 25) && (k2 <= 35)) {
                    s3 = "right by 30\260, ";
                }
                if ((k2 > 35) && (k2 <= 45)) {
                    s3 = "right by 40\260, ";
                }
                if ((k2 > 45) && (k2 <= 60)) {
                    s3 = "off our right, ";
                }
                if (k2 > 355) {
                    s3 = "dead ahead, ";
                }
                if ((k2 <= 355) && (k2 >= 352.5D)) {
                    s3 = "left by 5\260, ";
                }
                if ((k2 < 352.5D) && (k2 >= 347.5D)) {
                    s3 = "left by 10\260, ";
                }
                if ((k2 < 347.5D) && (k2 >= 342.5D)) {
                    s3 = "left by 15\260, ";
                }
                if ((k2 < 342.5D) && (k2 >= 335)) {
                    s3 = "left by 20\260, ";
                }
                if ((k2 < 335) && (k2 >= 325)) {
                    s3 = "left by 30\260, ";
                }
                if ((k2 < 325) && (k2 >= 315)) {
                    s3 = "left by 40\260, ";
                }
                if ((k2 < 345) && (k2 >= 300)) {
                    s3 = "off our left, ";
                }
                double d12 = 0.0D;
                EnginesInterface enginesinterface = ((Aircraft) actor).FM.EI;
                for (int i3 = 0; i3 < enginesinterface.engines.length; i3++) {
                    float f6 = 0.0F;
                    if ((enginesinterface.engines[i3].getType() == 2) || (enginesinterface.engines[i3].getType() == 3)) {
                        f6 = enginesinterface.engines[i3].thrustMax;
                        if (enginesinterface.engines[i3].getPowerOutput() > 1.0F) {
                            f6 *= enginesinterface.engines[i3].getPowerOutput() * 1.22F;
                        } else {
                            f6 *= (enginesinterface.engines[i3].getPowerOutput() * 0.8F) + 0.2F;
                        }
                    } else {
                        f6 = Reflection.getFloat(enginesinterface.engines[i3], "horsePowers") * ((enginesinterface.engines[i3].getPowerOutput() * 0.9F) + 0.1F) * 0.36F;
                    }
                    d12 += f6;
                }

                if (enginesinterface.engines.length > 4) {
                    d12 *= 2D / enginesinterface.engines.length;
                } else {
                    d12 /= Math.sqrt(enginesinterface.engines.length);
                }
                d12 *= 1.5D;
                if (((double) j1 <= (double) l2) && (l1 >= -20) && (l1 <= 20) && (Math.sqrt(j2 * j2) <= 60D) && (j1 < d12)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "IR Tracking! " + s3 + s);
                    this.freq = 1;
                    this.setTimer(this.freq);
                    return true;
                }
            }
        }

        return true;
    }

    public void playSirenaWarning(boolean flag) {
        if (this != World.getPlayerAircraft()) {
            return;
        }
        if (flag && !this.sirenaSoundPlaying) {
            this.fxSPO10.play(this.smplSirena);
            this.sirenaSoundPlaying = true;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO: Enemy on Six!");
        } else if (!flag && this.sirenaSoundPlaying) {
            this.fxSPO10.cancel();
            this.sirenaSoundPlaying = false;
        }
    }

    public void update(float f) {
        super.update(f);
        this.computeR35F_300_AB();
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "MiG23MLA_";
    }

    public void auxPressed(int i) {
        super.auxPressed(i);
        if (i == 22) {
            if (!this.APmode3) {
                this.APmode3 = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route ON");
                this.FM.AP.setWayPoint(true);
            } else if (this.APmode3) {
                this.APmode3 = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Autopilot Mode: Route OFF");
                this.FM.AP.setWayPoint(false);
                this.FM.CT.AileronControl = 0.0F;
                this.FM.CT.ElevatorControl = 0.0F;
                this.FM.CT.RudderControl = 0.0F;
            }
        }
    }

    public void computeR35F_300_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 36550D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 19.5D) {
                f1 = 16F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (((0.000650144F * f4) - (0.013923F * f3)) + (0.0804962F * f2)) - (0.275674F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    public boolean  APmode3;
    private long    raretimer;
    public float    Timer1;
    public float    Timer2;
    private int     freq;
    private int     counter;
    private Sample  smplSirena;
    private boolean sirenaSoundPlaying;
    private SoundFX fxSPO10;
    private Sample  smplSPO10;
    static {
        Class class1 = MIG_23MLA.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG-23");
        Property.set(class1, "meshName", "3DO/Plane/MiG-23/hierMiG23ML.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1972F);
        Property.set(class1, "yearExpired", 2020F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-23ML.fmd:MIG23FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_23MLD.class, CockpitMIG_23Bombardier.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 9, 9, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 7, 8 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Gun01", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalDev01", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Dev02", "_Dev03", "_Dev04", "_Dev05", "_Dev06", "_Dev07", "_Dev08", "_Dev09", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_Dev10", "_Dev11", "_Gun02", "_Gun03", "_Gun04", "_Gun05", "_Dev12", "_Dev13", "_Dev14", "_Dev15", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_Dev16", "_Dev17", "_Dev18", "_Dev19", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalDev20", "_ExternalDev21", "_Rock17",
                "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22" });
    }
}
