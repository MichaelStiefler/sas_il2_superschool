
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
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
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
            if (flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D)) {
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
                }
            }
        }

        return true;
    }

    private boolean IRST() {
        if (this.thisWeaponsName.endsWith("IRIAF")) {
            return false;
        }

        boolean noTV = false;
        // F-14D has both IR sensor and Visible Ray TV camera.
        // At Daytime, TV camera works as determining aircraft classes.
        // At Night or bad weather, TV camera doesn't work and only IR sensor shows the direction.
        if ((World.Sun().ToSun.z < 0.03F) || (((Mission.curCloudsType() > 3) || ((Mission.curCloudsType() > 1) && (World.Sun().ToSun.z < 0.10F))) && (Mission.curCloudsHeight() > ((Actor) this).pos.getAbsPoint().z))) {
            noTV = true;
        }

        int irstListSize = F_14D.irstPlaneName.length;

        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
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
            if (!noTV && flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D) && !((Main.cur().clouds != null) && (Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) < 1.0F)) && !(((Mission.curCloudsType() > 3) || ((Mission.curCloudsType() > 1) && (World.Sun().ToSun.z < 0.10F))) && (Mission.curCloudsHeight() > actor.pos.getAbsPoint().z))) {
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
                float f2 = 100000F;
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
                String classnameFull = actor.getClass().getName();
                int idot = classnameFull.lastIndexOf('.');
                int idol = classnameFull.lastIndexOf('$');
                if (idot < idol) {
                    idot = idol;
                }
                String classnameSection = classnameFull.substring(idot + 1);
                for (int num = 0; num < irstListSize; num++) {
                    if (((double) j1 <= (double) l2) && (j1 < F_14D.irstMaxDistance[num]) && (l1 >= -20) && (l1 <= 20) && (Math.sqrt(j2 * j2) <= 60D) && ((classnameSection.indexOf(F_14D.irstPlaneName[num])) != -1)) {
                        HUD.log(AircraftHotKeys.hudLogWeaponId, "TV Tracking! " + F_14D.irstPlaneDisplay[num] + " " + s3 + s + ", " + i2 + "m");
                        return true;
                    }
                }
            }
        }

        // IR sensor part, when TV camera has no result.
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (flag1 && (actor instanceof Aircraft) && (actor.getArmy() != World.getPlayerArmy()) && (actor != World.getPlayerAircraft()) && (actor.getSpeed(vector3d) > 20D) && !((Main.cur().clouds != null) && (Main.cur().clouds.getVisibility(actor.pos.getAbsPoint(), aircraft.pos.getAbsPoint()) < 1.0F))) {
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
                float f2 = 100000F;
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
                double IRstrength = 0D;
                EnginesInterface ei = ((Aircraft) actor).FM.EI;
                for (int en = 0; en < ei.engines.length; en++) {
                    float add = 0;
                    if ((ei.engines[en].getType() == 2) || (ei.engines[en].getType() == 3)) {   // get IR strength from Jet exhaust from thrust and throttle values
                        add = ei.engines[en].thrustMax;
                        if (ei.engines[en].getPowerOutput() > 1.0F) {
                            add *= (ei.engines[en].getPowerOutput() * 1.22F);
                        } else {
                            add *= ((ei.engines[en].getPowerOutput() * 0.8F) + 0.2F);
                        }
                    } else {
                        add = Reflection.getFloat(ei.engines[en], "horsePowers") * ((ei.engines[en].getPowerOutput() * 0.9F) + 0.1F) * 0.36F;
                    }

                    IRstrength += add;
                }
                if (ei.engines.length > 4) {
                    IRstrength *= (2.0D / ei.engines.length);
                } else {
                    IRstrength /= Math.sqrt(ei.engines.length);
                }
                IRstrength *= 1.8D;   // approximation distance in meters

                if (((double) j1 <= (double) l2) && (l1 >= -20) && (l1 <= 20) && (Math.sqrt(j2 * j2) <= 60D) && (j1 < IRstrength)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "IR Tracking! " + s3 + s);
                    return true;
                }
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
            double d1 = ((Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x) / 1000D) / 10D;
            double d2 = ((Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y) / 1000D) / 10D;
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
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_14D.class, CockpitF_14FLIR.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 7, 8 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalDev01", "_ExternalDev02", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev03", "_ExternalDev04", "_Flare01", "_Chaff01"

        });
    }
}
