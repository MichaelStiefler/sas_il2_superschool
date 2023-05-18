package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.MsgAction;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Su_11_8M extends Sukhoi implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector {

    public Su_11_8M() {
        this.guidedMissileUtils = null;
        this.hasChaff = false;
        this.hasFlare = false;
        this.lastChaffDeployed = 0L;
        this.lastFlareDeployed = 0L;
        this.lastCommonThreatActive = 0L;
        this.intervalCommonThreat = 1000L;
        this.lastRadarLockThreatActive = 0L;
        this.intervalRadarLockThreat = 1000L;
        this.lastMissileLaunchThreatActive = 0L;
        this.intervalMissileLaunchThreat = 1000L;
        this.guidedMissileUtils = new GuidedMissileUtils(this);
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
        this.counter = 0;
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "SU11_";
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public long getChaffDeployed() {
        if (this.hasChaff) {
            return this.lastChaffDeployed;
        } else {
            return 0L;
        }
    }

    public long getFlareDeployed() {
        if (this.hasFlare) {
            return this.lastFlareDeployed;
        } else {
            return 0L;
        }
    }

    public void setCommonThreatActive() {
        long l = Time.current();
        if ((l - this.lastCommonThreatActive) > this.intervalCommonThreat) {
            this.lastCommonThreatActive = l;
            this.doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive() {
        long l = Time.current();
        if ((l - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
            this.lastRadarLockThreatActive = l;
            this.doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive() {
        long l = Time.current();
        if ((l - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
            this.lastMissileLaunchThreatActive = l;
            this.doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat() {
    }

    private void doDealRadarLockThreat() {
    }

    private void doDealMissileLaunchThreat() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.bHasSK1Seat = false;
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.CT.bHasRefuelControl = true;
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 1) {
            this.k14Mode = 0;
        }
        if (this.k14Mode == 0) {
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: On");
            }
        } else if ((this.k14Mode == 1) && (((Interpolate) (this.FM)).actor == World.getPlayerAircraft())) {
            HUD.log(AircraftHotKeys.hudLogWeaponId, "PKI Sight: Off");
        }
        return true;
    }

    public void update(float f) {
        super.update(f);
        this.computeAL7F1_AB();
        if (Config.isUSE_RENDER()) {
            if (!this.FM.AS.bIsAboutToBailout) {
                if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null) && Main3D.cur3D().cockpits[0].cockpitDimControl) {
                    this.hierMesh().chunkVisible("Head1_D0", false);
                    this.hierMesh().chunkVisible("Glass_Head1_D0", true);
                } else {
                    this.hierMesh().chunkVisible("Head1_D0", true);
                    this.hierMesh().chunkVisible("Glass_Head1_D0", false);
                }
            } else {
                this.hierMesh().chunkVisible("Glass_Head1_D0", false);
            }
        }
        this.guidedMissileUtils.update();
    }

    public void doEjectCatapult() {
        new MsgAction(false, this) {

            public void doAction(Object obj) {
                Aircraft aircraft = (Aircraft) obj;
                if (Actor.isValid(aircraft)) {
                    Loc loc = new Loc();
                    Loc loc1 = new Loc();
                    Vector3d vector3d = new Vector3d(0.0D, 0.0D, 40D);
                    HookNamed hooknamed = new HookNamed(aircraft, "_ExternalSeat01");
                    aircraft.pos.getAbs(loc1);
                    hooknamed.computePos(aircraft, loc1, loc);
                    loc.transform(vector3d);
                    vector3d.x += aircraft.FM.Vwld.x;
                    vector3d.y += aircraft.FM.Vwld.y;
                    vector3d.z += aircraft.FM.Vwld.z;
                    new EjectionSeat(10, loc, vector3d, aircraft);
                }
            }

        };
        this.hierMesh().chunkVisible("Seat_D0", false);
        this.FM.setTakenMortalDamage(true, null);
        this.FM.CT.WeaponControl[0] = false;
        this.FM.CT.WeaponControl[1] = false;
        this.FM.CT.bHasAileronControl = false;
        this.FM.CT.bHasRudderControl = false;
        this.FM.CT.bHasElevatorControl = false;
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 1500F, -140F);
        float f2 = Math.max(-f * 1500F, -100F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.11F, 0.6F, 0.0F, -125F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR44_D0", 0.0F, -30F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 0.0F, -f1);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 0.0F, f1);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 0.0F, -f2);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 0.0F, f2);
        hiermesh.chunkSetAngles("GearTelescopeL", 0.0F, 115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearTelescopeR", 0.0F, 115F * f, 0.0F);
    }

    protected void moveGear(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        this.hierMesh().chunkSetLocate("GearL33_D0", Aircraft.xyz, Aircraft.ypr);
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, -0.32F);
        this.hierMesh().chunkSetLocate("GearR33_D0", Aircraft.xyz, Aircraft.ypr);
        Su_11_8M.moveGear(this.hierMesh(), f);
    }

    public void rareAction(float f, boolean flag) {
        if ((this.counter++ % 5) == 0) {
            this.RP15();
        }
        super.rareAction(f, flag);
    }

    private boolean RP15() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        boolean flag1 = false;
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        double d3 = d2 - Landscape.Hmin((float) aircraft.pos.getAbsPoint().x, (float) aircraft.pos.getAbsPoint().y);
        if (aircraft instanceof Su_11_8M) {
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
                float f2 = 25000F;
                float f3 = f2;
                if (d3 < 1500D) {
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
                if (((double) j1 <= (double) l2) && (j1 > 200D) && (l1 >= -20) && (l1 <= 20) && (Math.sqrt(j2 * j2) <= 60D)) {
                    HUD.log(AircraftHotKeys.hudLogWeaponId, "RP-15: Contact " + s3 + s + ", " + i2 + "m");
                    this.freq = 1;
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

    public void computeAL7F1_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 32000D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 17F) {
                f1 = 20F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                f1 = ((0.0329824F * f3) - (0.647042F * f2)) + (2.64429F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    private GuidedMissileUtils guidedMissileUtils;
    private boolean            hasChaff;
    private boolean            hasFlare;
    private long               lastChaffDeployed;
    private long               lastFlareDeployed;
    private long               lastCommonThreatActive;
    private long               intervalCommonThreat;
    private long               lastRadarLockThreatActive;
    private long               intervalRadarLockThreat;
    private long               lastMissileLaunchThreatActive;
    private long               intervalMissileLaunchThreat;
    private int                freq;
    public float               Timer1;
    public float               Timer2;
    private int                counter;

    static {
        Class class1 = Su_11_8M.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-11");
        Property.set(class1, "meshName", "3DO/Plane/Su-9/hierSU11.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1959F);
        Property.set(class1, "yearExpired", 1970F);
        Property.set(class1, "FlightModel", "FlightModels/Su-11-8M.fmd:Sukhoi_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSu_11.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 2, 2, 9, 9, 2, 2, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 7, 7, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Flare01", "_Flare02", "_ExternalDev02", "_ExternalDev01", "_Dev03", "_Dev04", "_Dev05", "_Dev06", "_Rock01", "_Rock02", "_Rock03", "_Rock04", "_Rock05", "_Rock06", "_Rock07", "_Rock08", "_ExternalDev07", "_ExternalDev08", "_ExternalPyl09", "_ExternalPyl10", "_ExternalRock09", "_ExternalRock10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock11", "_ExternalRock22", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev13", "_ExternalDev14", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37",
                "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42", "_CANNON01", "_CANNON02", "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46" });
    }
}
