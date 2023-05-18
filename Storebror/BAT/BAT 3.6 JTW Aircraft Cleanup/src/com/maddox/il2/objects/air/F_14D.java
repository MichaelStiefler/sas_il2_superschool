package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.fm.EnginesInterface;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.Pylon_AN_ALQ167_gn16;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class F_14D extends F_14 {

    public F_14D() {
        this.counter = 0;
        this.error = 0;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "F14D_";
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public void update(float f) {
        this.computeF110GE400_AB();
        super.update(f);
    }

    public void rareAction(float f, boolean flag) {
        if ((this.raretimer != Time.current()) && (this == World.getPlayerAircraft())) {
            this.counter++;
            if ((this.counter % 6) == 0) {
                this.AN_APG71();
            }
            if ((this.counter % 12) == 3) {
                this.IRST();
            }
            if ((this.counter % 12) == 9) {
                this.InertialNavigation();
            }
        }
        super.rareAction(f, flag);
        this.raretimer = Time.current();
    }

    private boolean AN_APG71() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) ((Actor) (aircraft)).pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof F_14D) {
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
            if (!flag1 || !(actor instanceof Aircraft) || (actor.getArmy() == World.getPlayerArmy()) || (actor == World.getPlayerAircraft()) || (actor.getSpeed(vector3d) <= 20D)) {
                continue;
            }
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
            float f2 = 370000F;
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
            if (((double) j1 <= (double) l2) && (j1 > 100D) && (l1 >= -20) && (l1 <= 20) && (Math.sqrt(j2 * j2) <= 60D)) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "WSO: Contact " + s3 + s + ", " + i2 + "m");
                this.freq = 1;
            } else {
                this.freq = 1;
            }
            this.setTimer(this.freq);
        }

        return true;
    }

    private boolean IRST() {
        if (this.thisWeaponsName.endsWith("IRIAF")) {
            return false;
        }
        boolean flag = false;
        if ((World.Sun().ToSun.z < 0.03F) || (((Mission.curCloudsType() > 3) || ((Mission.curCloudsType() > 1) && (World.Sun().ToSun.z < 0.1F))) && (Mission.curCloudsHeight() > this.pos.getAbsPoint().z))) {
            flag = true;
        }
        int i = F_14D.irstPlaneName.length;
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag2 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) ((Actor) (aircraft)).pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof F_14D) {
            flag2 = true;
        }
        if (d3 < 0.0D) {
            d3 = 0.0D;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (j < 0) {
            j += 360;
        }
        int k = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (k < 0) {
            k += 360;
        }
        label0:
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (flag || !flag2 || !(actor instanceof Aircraft) || (actor.getArmy() == World.getPlayerArmy()) || (actor == World.getPlayerAircraft()) || (actor.getSpeed(vector3d) <= 20D) || ((Main.cur().clouds != null) && (Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) < 1.0F)) || (((Mission.curCloudsType() > 3) || ((Mission.curCloudsType() > 1) && (World.Sun().ToSun.z < 0.1F))) && (Mission.curCloudsHeight() > actor.pos.getAbsPoint().z))) {
                continue;
            }
            this.pos.getAbs(point3d);
            double d4 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
            double d6 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
            double d8 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
            double d10 = (int) (Math.ceil((d2 - d8) / 10D) * 10D);
            String s = "level with us";
            if ((d2 - d8 - 300D) >= 0.0D) {
                s = "below us";
            }
            if (((d2 - d8) + 300D) <= 0.0D) {
                s = "above us";
            }
            if (((d2 - d8 - 300D) < 0.0D) && ((d2 - d8 - 150D) >= 0.0D)) {
                s = "slightly below";
            }
            if ((((d2 - d8) + 300D) > 0.0D) && (((d2 - d8) + 150D) < 0.0D)) {
                s = "slightly above";
            }
            double d12 = d4 - d;
            double d14 = d6 - d1;
            float f = 57.32484F * (float) Math.atan2(d14, -d12);
            int l = (int) (Math.floor((int) f) - 90D);
            if (l < 0) {
                l += 360;
            }
            int j1 = l - j;
            double d16 = d - d4;
            double d18 = d1 - d6;
            Random random = new Random();
            float f2 = ((random.nextInt(20) - 10F) / 100F) + 1.0F;
            int l1 = random.nextInt(6) - 3;
            float f4 = 100000F;
            float f6 = f4;
            if (d3 < 15D) {
                f6 = (float) (d3 * 0.8D * 3D);
            }
            int j2 = (int) (Math.ceil(Math.sqrt(((d18 * d18) + (d16 * d16)) * f2) / 10D) * 10D);
            if (j2 > f4) {
                j2 = (int) (Math.ceil(Math.sqrt((d18 * d18) + (d16 * d16)) / 10D) * 10D);
            }
            float f8 = 57.32484F * (float) Math.atan2(j2, d10);
            int l2 = (int) (Math.floor((int) f8) - 90D);
            int j3 = (l2 - (90 - k)) + l1;
            int l3 = (int) f4;
            if (j2 < f4) {
                if (j2 > 1150) {
                    l3 = (int) (Math.ceil(j2 / 900D) * 900D);
                } else {
                    l3 = (int) (Math.ceil(j2 / 500D) * 500D);
                }
            }
            int j4 = j1 + l1;
            int l4 = j4;
            if (l4 < 0) {
                l4 += 360;
            }
            float f10 = (float) (f6 + (Math.sin(Math.toRadians(Math.sqrt(j1 * j1) * 3D)) * (f6 * 0.25D)));
            int j5 = (int) (f10 * Math.cos(Math.toRadians(j3)));
            if (l4 < 5) {
            }
            if ((l4 >= 5) && (l4 <= 7.5D)) {
            }
            if ((l4 > 7.5D) && (l4 <= 12.5D)) {
            }
            if ((l4 > 12.5D) && (l4 <= 17.5D)) {
            }
            if ((l4 > 17.5D) && (l4 <= 25)) {
            }
            if ((l4 > 25) && (l4 <= 35)) {
            }
            if ((l4 > 35) && (l4 <= 45)) {
            }
            if ((l4 > 45) && (l4 <= 60)) {
            }
            if (l4 > 355) {
            }
            if ((l4 <= 355) && (l4 >= 352.5D)) {
            }
            if ((l4 < 352.5D) && (l4 >= 347.5D)) {
            }
            if ((l4 < 347.5D) && (l4 >= 342.5D)) {
            }
            if ((l4 < 342.5D) && (l4 >= 335)) {
            }
            if ((l4 < 335) && (l4 >= 325)) {
            }
            if ((l4 < 325) && (l4 >= 315)) {
            }
            if ((l4 < 345) && (l4 >= 300)) {
            }
            if (j3 < -10) {
            }
            if ((j3 >= -10) && (j3 <= -5)) {
            }
            if ((j3 > -5) && (j3 < 5)) {
            }
            if ((j3 <= 10) && (j3 >= 5)) {
            }
            if (j3 > 10) {
            }
            String s6 = "  ";
            if (l4 < 5) {
                s6 = "dead ahead, ";
            }
            if ((l4 >= 5) && (l4 <= 7.5D)) {
                s6 = "right by 5\260, ";
            }
            if ((l4 > 7.5D) && (l4 <= 12.5D)) {
                s6 = "right by 10\260, ";
            }
            if ((l4 > 12.5D) && (l4 <= 17.5D)) {
                s6 = "right by 15\260, ";
            }
            if ((l4 > 17.5D) && (l4 <= 25)) {
                s6 = "right by 20\260, ";
            }
            if ((l4 > 25) && (l4 <= 35)) {
                s6 = "right by 30\260, ";
            }
            if ((l4 > 35) && (l4 <= 45)) {
                s6 = "right by 40\260, ";
            }
            if ((l4 > 45) && (l4 <= 60)) {
                s6 = "off our right, ";
            }
            if (l4 > 355) {
                s6 = "dead ahead, ";
            }
            if ((l4 <= 355) && (l4 >= 352.5D)) {
                s6 = "left by 5\260, ";
            }
            if ((l4 < 352.5D) && (l4 >= 347.5D)) {
                s6 = "left by 10\260, ";
            }
            if ((l4 < 347.5D) && (l4 >= 342.5D)) {
                s6 = "left by 15\260, ";
            }
            if ((l4 < 342.5D) && (l4 >= 335)) {
                s6 = "left by 20\260, ";
            }
            if ((l4 < 335) && (l4 >= 325)) {
                s6 = "left by 30\260, ";
            }
            if ((l4 < 325) && (l4 >= 315)) {
                s6 = "left by 40\260, ";
            }
            if ((l4 < 345) && (l4 >= 300)) {
                s6 = "off our left, ";
            }
            String s8 = actor.getClass().getName();
            int l5 = s8.lastIndexOf('.');
            int i6 = s8.lastIndexOf('$');
            if (l5 < i6) {
                l5 = i6;
            }
            String s9 = s8.substring(l5 + 1);
            int k6 = 0;
            do {
                if (k6 >= i) {
                    continue label0;
                }
                if (((double) j2 <= (double) j5) && (j2 < F_14D.irstMaxDistance[k6]) && (j3 >= -20) && (j3 <= 20) && (Math.sqrt(j4 * j4) <= 60D) && (s9.indexOf(F_14D.irstPlaneName[k6]) != -1)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "TV Tracking! " + F_14D.irstPlaneDisplay[k6] + " " + s6 + s + ", " + l3 + "m");
                    this.freq = 1;
                    this.setTimer(this.freq);
                    return true;
                }
                k6++;
            } while (true);
        }

        for (java.util.Map.Entry entry1 = Engine.name2Actor().nextEntry(null); entry1 != null; entry1 = Engine.name2Actor().nextEntry(entry1)) {
            Actor actor1 = (Actor) entry1.getValue();
            if (!flag2 || !(actor1 instanceof Aircraft) || (actor1.getArmy() == World.getPlayerArmy()) || (actor1 == World.getPlayerAircraft()) || (actor1.getSpeed(vector3d) <= 20D) || ((Main.cur().clouds != null) && (Main.cur().clouds.getVisibility(actor1.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) < 1.0F))) {
                continue;
            }
            this.pos.getAbs(point3d);
            double d5 = Main3D.cur3D().land2D.worldOfsX() + actor1.pos.getAbsPoint().x;
            double d7 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().y;
            double d9 = Main3D.cur3D().land2D.worldOfsY() + actor1.pos.getAbsPoint().z;
            double d11 = (int) (Math.ceil((d2 - d9) / 10D) * 10D);
            String s1 = "level with us";
            if ((d2 - d9 - 300D) >= 0.0D) {
                s1 = "below us";
            }
            if (((d2 - d9) + 300D) <= 0.0D) {
                s1 = "above us";
            }
            if (((d2 - d9 - 300D) < 0.0D) && ((d2 - d9 - 150D) >= 0.0D)) {
                s1 = "slightly below";
            }
            if ((((d2 - d9) + 300D) > 0.0D) && (((d2 - d9) + 150D) < 0.0D)) {
                s1 = "slightly above";
            }
            double d13 = d5 - d;
            double d15 = d7 - d1;
            float f1 = 57.32484F * (float) Math.atan2(d15, -d13);
            int i1 = (int) (Math.floor((int) f1) - 90D);
            if (i1 < 0) {
                i1 += 360;
            }
            int k1 = i1 - j;
            double d17 = d - d5;
            double d19 = d1 - d7;
            Random random1 = new Random();
            float f3 = ((random1.nextInt(20) - 10F) / 100F) + 1.0F;
            int i2 = random1.nextInt(6) - 3;
            float f5 = 100000F;
            float f7 = f5;
            if (d3 < 15D) {
                f7 = (float) (d3 * 0.8D * 3D);
            }
            int k2 = (int) (Math.ceil(Math.sqrt(((d19 * d19) + (d17 * d17)) * f3) / 10D) * 10D);
            if (k2 > f5) {
                k2 = (int) (Math.ceil(Math.sqrt((d19 * d19) + (d17 * d17)) / 10D) * 10D);
            }
            float f9 = 57.32484F * (float) Math.atan2(k2, d11);
            int i3 = (int) (Math.floor((int) f9) - 90D);
            int k3 = (i3 - (90 - k)) + i2;
            int k4 = k1 + i2;
            int i5 = k4;
            if (i5 < 0) {
                i5 += 360;
            }
            float f11 = (float) (f7 + (Math.sin(Math.toRadians(Math.sqrt(k1 * k1) * 3D)) * (f7 * 0.25D)));
            int k5 = (int) (f11 * Math.cos(Math.toRadians(k3)));
            if (i5 < 5) {
            }
            if ((i5 >= 5) && (i5 <= 7.5D)) {
            }
            if ((i5 > 7.5D) && (i5 <= 12.5D)) {
            }
            if ((i5 > 12.5D) && (i5 <= 17.5D)) {
            }
            if ((i5 > 17.5D) && (i5 <= 25)) {
            }
            if ((i5 > 25) && (i5 <= 35)) {
            }
            if ((i5 > 35) && (i5 <= 45)) {
            }
            if ((i5 > 45) && (i5 <= 60)) {
            }
            if (i5 > 355) {
            }
            if ((i5 <= 355) && (i5 >= 352.5D)) {
            }
            if ((i5 < 352.5D) && (i5 >= 347.5D)) {
            }
            if ((i5 < 347.5D) && (i5 >= 342.5D)) {
            }
            if ((i5 < 342.5D) && (i5 >= 335)) {
            }
            if ((i5 < 335) && (i5 >= 325)) {
            }
            if ((i5 < 325) && (i5 >= 315)) {
            }
            if ((i5 < 345) && (i5 >= 300)) {
            }
            if (k3 < -10) {
            }
            if ((k3 >= -10) && (k3 <= -5)) {
            }
            if ((k3 > -5) && (k3 < 5)) {
            }
            if ((k3 <= 10) && (k3 >= 5)) {
            }
            if (k3 > 10) {
            }
            String s7 = "  ";
            if (i5 < 5) {
                s7 = "dead ahead, ";
            }
            if ((i5 >= 5) && (i5 <= 7.5D)) {
                s7 = "right by 5\260, ";
            }
            if ((i5 > 7.5D) && (i5 <= 12.5D)) {
                s7 = "right by 10\260, ";
            }
            if ((i5 > 12.5D) && (i5 <= 17.5D)) {
                s7 = "right by 15\260, ";
            }
            if ((i5 > 17.5D) && (i5 <= 25)) {
                s7 = "right by 20\260, ";
            }
            if ((i5 > 25) && (i5 <= 35)) {
                s7 = "right by 30\260, ";
            }
            if ((i5 > 35) && (i5 <= 45)) {
                s7 = "right by 40\260, ";
            }
            if ((i5 > 45) && (i5 <= 60)) {
                s7 = "off our right, ";
            }
            if (i5 > 355) {
                s7 = "dead ahead, ";
            }
            if ((i5 <= 355) && (i5 >= 352.5D)) {
                s7 = "left by 5\260, ";
            }
            if ((i5 < 352.5D) && (i5 >= 347.5D)) {
                s7 = "left by 10\260, ";
            }
            if ((i5 < 347.5D) && (i5 >= 342.5D)) {
                s7 = "left by 15\260, ";
            }
            if ((i5 < 342.5D) && (i5 >= 335)) {
                s7 = "left by 20\260, ";
            }
            if ((i5 < 335) && (i5 >= 325)) {
                s7 = "left by 30\260, ";
            }
            if ((i5 < 325) && (i5 >= 315)) {
                s7 = "left by 40\260, ";
            }
            if ((i5 < 345) && (i5 >= 300)) {
                s7 = "off our left, ";
            }
            double d20 = 0.0D;
            EnginesInterface enginesinterface = ((Aircraft) actor1).FM.EI;
            for (int j6 = 0; j6 < enginesinterface.engines.length; j6++) {
                float f12 = 0.0F;
                if ((enginesinterface.engines[j6].getType() == 2) || (enginesinterface.engines[j6].getType() == 3)) {
                    f12 = enginesinterface.engines[j6].thrustMax;
                    if (enginesinterface.engines[j6].getPowerOutput() > 1.0F) {
                        f12 *= enginesinterface.engines[j6].getPowerOutput() * 1.22F;
                    } else {
                        f12 *= (enginesinterface.engines[j6].getPowerOutput() * 0.8F) + 0.2F;
                    }
                } else {
                    f12 = Reflection.getFloat(enginesinterface.engines[j6], "horsePowers") * ((enginesinterface.engines[j6].getPowerOutput() * 0.9F) + 0.1F) * 0.36F;
                }
                d20 += f12;
            }

            if (enginesinterface.engines.length > 4) {
                d20 *= 2D / enginesinterface.engines.length;
            } else {
                d20 /= Math.sqrt(enginesinterface.engines.length);
            }
            d20 *= 1.8D;
            if (((double) k2 <= (double) k5) && (k3 >= -20) && (k3 <= 20) && (Math.sqrt(k4 * k4) <= 60D) && (k2 < d20)) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "IR Tracking! " + s7 + s1);
                this.freq = 1;
                this.setTimer(this.freq);
                return true;
            }
        }

        return false;
    }

    private boolean InertialNavigation() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if ((aircraft.getSpeed(vector3d) > 20D) && (aircraft.pos.getAbsPoint().z >= 150D) && (aircraft instanceof F_14D)) {
            this.pos.getAbs(point3d);
            if (Mission.cur() != null) {
                this.error++;
                if (this.error > 99) {
                    this.error = 1;
                }
            }
            int i = this.error;
            int j = i;
            Random random = new Random();
            int k = random.nextInt(100);
            if (k > 50) {
                i -= i * 2;
            }
            k = random.nextInt(100);
            if (k > 50) {
                j -= j * 2;
            }
            double d = Main3D.cur3D().land2D.mapSizeX() / 1000D;
            double d1 = (Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D / 10D;
            double d2 = (Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D / 10D;
            char c = (char) (int) (65D + Math.floor(((d1 / 676D) - Math.floor(d1 / 676D)) * 26D));
            char c1 = (char) (int) (65D + Math.floor(((d1 / 26D) - Math.floor(d1 / 26D)) * 26D));
            String s = "";
            if (d > 260D) {
                s = "" + c + c1;
            } else {
                s = "" + c1;
            }
            int l = (int) Math.ceil(d2);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "GPS: " + s + "-" + l);
        }
        return true;
    }

    public void missionStarting() {
        super.missionStarting();
        this.checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            if (this.FM.CT.Weapons[i] == null) {
                continue;
            }
            for (int j = 0; j < this.FM.CT.Weapons[i].length; j++) {
                if (this.FM.CT.Weapons[i][j] instanceof Pylon_AN_ALQ167_gn16) {
                    ((Pylon_AN_ALQ167_gn16) this.FM.CT.Weapons[i][j]).matGray();
                }
            }

        }

    }

    private int           counter;
    private int           error;
    private long          raretimer;
    private static String irstPlaneName[]    = { "AD", "BirdDog", "A_6", "A_7", "A_10", "A_26", "AV_8", "B_29", "C_123", "F_4", "F_5", "F_8", "F9F", "F_14", "F_15", "F_16", "F_18", "F84", "F_86", "F_100", "F_104", "F_105", "Hunter", "IL_28", "KC_10", "DC_10", "L_39", "Meteor", "Mig_15", "Mig_17", "Mig_19", "MIG_21", "MIG_23", "MIG_29", "MIRAGE", "Mi24", "MI8MT", "P_80", "Skyhawk", "Su_7", "Su_9B", "Su_11_8M", "Su_25", "Su_27", "Tucano", "TU_95", "Yak_36", "Vampire", "F_80", "T_33", "A1H", "A1J", "CAC_Sabre", "FJ_3M", "P2V", "OV_", "TU_4", "SM_12", "MIG21", "L_159", "SeaHawk", "Canberra", "F_102", "F_106", "Tu_22", "Tu_26", "T_37", "A_3", "A_5", "B_1", "B_52", "B_36", "B_45", "B_47", "B_58", "C_119", "EE_Lightning", "Tornado", "C_121", "C_130", "C_141", "C_17", "C_5", "F2H", "F3H", "F4D", "F3D", "F11F", "F_22", "F_35", "Su_50", "F_89", "F_101", "F_93", "F_111", "P_3", "S_3", "E_2", "IL_76", "An_12", "An_22", "An_124", "An_225", "747", "707", "G_222", "G_91", "MB_326", "Yak_28", "Tu_160", "Su_24", "M_4",
            "Typhoon", "Rafale", "Gripen", "MIRAGE_F1", "MIRAGE_2000", "Jaguar", "Su_17", "Su_20", "Su_22", "MiG_25", "MB_326", "G_91", "MB_339" };
    private static String irstPlaneDisplay[] = { "Skyraider", "BirdDog", "A-6", "A-7", "A-10", "A-26", "Harrier", "B-29", "C-123", "F-4", "F-5", "F-8", "F9F", "F-14", "F-15", "F-16", "F-18", "F-84", "F-86", "F-100", "F-104", "F-105", "Hunter", "IL-28", "KC-10", "DC-10", "L-39", "Meteor", "MiG-15", "MiG-17", "MiG-19", "MiG-21", "MiG-23", "MiG-29", "Mirage", "Mi-24", "Mi-8", "F-80", "Skyhawk", "Su-7", "Fishpot", "Fishpot", "Su-25", "Su-27", "Tucano", "Tupolev Bear", "Yak-36", "Vampire", "F-80", "T-33", "Skyraider", "Skyraider", "F-86", "F-86", "Neptune", "OV-1/10", "Tu-4", "MiG-19", "MiG-21", "L-39", "SeaHawk", "Canberra", "F-102", "F-106", "Tu-22", "Tu-26", "T-37", "A-3", "A-5", "B-1", "B-52", "B-36", "B-45", "B-47", "B-58", "C-119", "EE-Lightning", "Tornado", "C-121", "C-130", "C-141", "C-17", "C-5", "F2H", "F3H", "F4D", "F3D", "F11F", "F-22", "F-35", "Su-50", "F-89", "F-101", "F-93", "F-111", "P-3", "S-3", "E-2", "IL-76", "An-12", "An-22", "An-124", "An-225", "747", "707", "G-222", "G-91", "MB-326",
            "Yak-28", "Tu-160", "Su-24", "Bison", "Typhoon", "Rafale", "Gripen", "MIRAGE-F1", "MIRAGE-2000", "Jaguar", "Su-17", "Su-17", "Su-17", "MiG-25", "MB-326", "G-91", "MB-339" };
    private static double irstMaxDistance[]  = { 60000D, 40000D, 70000D, 60000D, 60000D, 80000D, 60000D, 100000D, 110000D, 75000D, 55000D, 55000D, 50000D, 75000D, 75000D, 55000D, 65000D, 50000D, 50000D, 60000D, 50000D, 80000D, 55000D, 90000D, 130000D, 130000D, 50000D, 55000D, 50000D, 50000D, 55000D, 55000D, 65000D, 70000D, 60000D, 65000D, 65000D, 50000D, 50000D, 75000D, 75000D, 75000D, 70000D, 75000D, 40000D, 110000D, 50000D, 50000D, 50000D, 50000D, 60000D, 60000D, 50000D, 50000D, 100000D, 60000D, 100000D, 55000D, 55000D, 50000D, 50000D, 60000D, 70000D, 70000D, 90000D, 90000D, 40000D, 80000D, 75000D, 90000D, 120000D, 130000D, 100000D, 100000D, 100000D, 100000D, 60000D, 65000D, 110000D, 100000D, 130000D, 130000D, 150000D, 60000D, 60000D, 60000D, 60000D, 60000D, 50000D, 50000D, 55000D, 70000D, 70000D, 60000D, 85000D, 90000D, 60000D, 90000D, 100000D, 85000D, 130000D, 150000D, 150000D, 150000D, 105000D, 90000D, 55000D, 50000D, 80000D, 110000D, 85000D, 110000D, 55000D, 55000D, 55000D, 60000D, 60000D, 60000D,
            75000D, 75000D, 75000D, 85000D, 55000D, 55000D, 50000D };

    static {
        Class class1 = F_14D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-14D");
        Property.set(class1, "meshName", "3DO/Plane/F-14/F14D.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1965F);
        Property.set(class1, "yearExpired", 1990F);
        Property.set(class1, "FlightModel", "FlightModels/F-14B.fmd:F14_FM");
        Property.set(class1, "cockpitClass", new Class[] { com.maddox.il2.objects.air.CockpitF_14D.class, com.maddox.il2.objects.air.CockpitF_14FLIR.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 7, 8 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev01", "_ExternalDev02", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev03", "_ExternalDev04", "_Flare01", "_Chaff01" });
    }
}
