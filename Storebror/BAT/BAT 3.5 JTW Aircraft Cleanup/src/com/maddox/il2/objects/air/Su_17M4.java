package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Polares;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class Su_17M4 extends Sukhoi implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector, TypeGSuit, TypeAcePlane, TypeFuelDump, TypeLaserSpotter {

    public Su_17M4() {
        this.guidedMissileUtils = null;
        this.LaserHook = new Hook[4];
        this.laserOn = false;
        this.laserLock = false;
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
        this.APmode3 = false;
        this.counter = 0;
        this.freq = 800;
        this.Timer1 = this.Timer2 = this.freq;
        this.error = 0;
    }

    public float getFlowRate() {
        return Su_17M4.FlowRate;
    }

    public float getFuelReserve() {
        return Su_17M4.FuelReserve;
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

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "SU17M4_";
    }

    public void laserUpdate() {
        if (!this.laserLock) {
            this.hierMesh().chunkSetAngles("LaserMsh_D0", -this.fSightCurForwardAngle, -this.fSightCurSideslip, 0.0F);
            this.pos.setUpdateEnable(true);
            this.pos.getRender(Actor._tmpLoc);
            this.LaserHook[1] = new HookNamed(this, "_Laser1");
            Su_17M4.LaserLoc1.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            this.LaserHook[1].computePos(this, Actor._tmpLoc, Su_17M4.LaserLoc1);
            Su_17M4.LaserLoc1.get(Su_17M4.LaserP1);
            Su_17M4.LaserLoc1.set(30000D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            this.LaserHook[1].computePos(this, Actor._tmpLoc, Su_17M4.LaserLoc1);
            Su_17M4.LaserLoc1.get(Su_17M4.LaserP2);
            Engine.land();
            if (Landscape.rayHitHQ(Su_17M4.LaserP1, Su_17M4.LaserP2, Su_17M4.LaserPL)) {
                Su_17M4.LaserPL.z -= 0.95D;
                Su_17M4.LaserP2.interpolate(Su_17M4.LaserP1, Su_17M4.LaserPL, 1.0F);
                TypeLaserSpotter.spot.set(Su_17M4.LaserP2);
                Eff3DActor.New(null, null, new Loc(((Tuple3d) (Su_17M4.LaserP2)).x, ((Tuple3d) (Su_17M4.LaserP2)).y, Su_17M4.LaserP2.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
            }
        } else if (this.laserLock) {
            Su_17M4.LaserP3.x = Su_17M4.LaserP2.x + (-(this.fSightCurForwardAngle * 6F));
            Su_17M4.LaserP3.y = Su_17M4.LaserP2.y + (this.fSightCurSideslip * 6F);
            Su_17M4.LaserP3.z = Su_17M4.LaserP2.z;
            TypeLaserSpotter.spot.set(Su_17M4.LaserP3);
            Eff3DActor.New(null, null, new Loc(((Tuple3d) (Su_17M4.LaserP3)).x, ((Tuple3d) (Su_17M4.LaserP3)).y, Su_17M4.LaserP3.z, 0.0F, 0.0F, 0.0F), 1.0F, "3DO/Effects/Fireworks/FlareWhiteWide.eff", 0.1F);
        }
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
        if (i == 24) {
            if (!this.laserOn) {
                this.laserOn = true;
                this.laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: On");
                this.fSightCurSideslip = 0.0F;
                this.fSightCurForwardAngle = 0.0F;
            } else if (this.laserOn) {
                this.laserOn = false;
                this.laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Off");
                this.fSightCurSideslip = 0.0F;
                this.fSightCurForwardAngle = 0.0F;
            }
        }
        if (i == 25) {
            if (!this.laserLock) {
                this.laserLock = true;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Locked");
                this.fSightCurSideslip = 0.0F;
                this.fSightCurForwardAngle = 0.0F;
            } else if (this.laserLock) {
                this.laserLock = false;
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Laser: Unlocked");
                this.fSightCurSideslip = 0.0F;
                this.fSightCurForwardAngle = 0.0F;
            }
        }
    }

    public void getGFactors(TypeGSuit.GFactors gfactors) {
        gfactors.setGFactors(Su_17M4.NEG_G_TOLERANCE_FACTOR, Su_17M4.NEG_G_TIME_FACTOR, Su_17M4.NEG_G_RECOVERY_FACTOR, Su_17M4.POS_G_TOLERANCE_FACTOR, Su_17M4.POS_G_TIME_FACTOR, Su_17M4.POS_G_RECOVERY_FACTOR);
    }

    public GuidedMissileUtils getGuidedMissileUtils() {
        return this.guidedMissileUtils;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
        this.FM.Skill = 3;
        if (this.thisWeaponsName.endsWith("(Fus)")) {
            this.hierMesh().chunkVisible("PylonC", true);
            this.hierMesh().chunkVisible("PylonC2", true);
            this.hierMesh().chunkVisible("PylonC3", false);
        }
    }

    public void update(float f) {
        this.guidedMissileUtils.update();
        this.computeAL21F_AB();
        this.computeVarWing();
        this.computeSpeedLimiter();
        super.update(f);
        if (this.laserOn) {
            this.laserUpdate();
        }
    }

    public void moveCockpitDoor(float f) {
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 45F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void computeAL21F_AB() {
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 26200D;
        }
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() == 6)) {
            if (f > 17F) {
                f1 = 20F;
            } else {
                float f2 = f * f;
                float f3 = f2 * f;
                float f4 = f3 * f;
                f1 = (((0.00644628F * f4) - (0.157057F * f3)) + (0.92125F * f2)) - (0.765843F * f);
            }
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    protected void moveFlap(float f) {
        float f1 = 44.5F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, -f1 * 0.6F, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, -f1 * 0.6F, 0.0F);
    }

    protected void moveVarWing(float f) {
        float f1 = -40F * f;
        this.hierMesh().chunkSetAngles("WingPivotL", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("WingPivotR", 0.0F, f1, 0.0F);
    }

    public void computeVarWing() {
        Polares polares = (Polares) Reflection.getValue(this.FM, "Wing");
        if ((this.calculateMach() > 0.9D) && (this.FM.EI.engines[0].getThrustOutput() < 1.001F)) {
            this.FM.CT.VarWingControl = 0.0F;
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
            Reflection.setFloat(this.FM, "GCenter", Reflection.getFloat(this.FM, "GCenter") - 0.7F);
            polares.CxMin_0 = 0.021F;
            polares.CyCritH_0 = 0.9F;
            polares.Cy0_0 = 0.007F;
            polares.parabCxCoeff_0 = 0.00113F;
        }
        if ((this.calculateMach() > 0.5D) && (this.FM.EI.engines[0].getThrustOutput() > 1.001F)) {
            this.FM.CT.VarWingControl = 0.0F;
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
            Reflection.setFloat(this.FM, "GCenter", Reflection.getFloat(this.FM, "GCenter") - 0.7F);
            polares.CxMin_0 = 0.021F;
            polares.CyCritH_0 = 0.9F;
            polares.Cy0_0 = 0.007F;
            polares.parabCxCoeff_0 = 0.00113F;
        }
        if ((this.calculateMach() > 0.45D) && (this.calculateMach() < 0.9D) && (this.FM.EI.engines[0].getThrustOutput() < 0.9F)) {
            this.FM.CT.VarWingControl = 0.35F;
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
            polares.CxMin_0 = 0.023F;
            polares.CyCritH_0 = 1.0F;
            polares.Cy0_0 = 0.012F;
            polares.parabCxCoeff_0 = 0.00103F;
        }
        if ((this.calculateMach() > 0.45D) && (this.calculateMach() < 0.9D) && (this.FM.getAOA() >= 10D)) {
            this.FM.CT.VarWingControl = 0.35F;
            this.FM.CT.FlapsControl = 0.0F;
            this.FM.CT.bHasFlapsControl = false;
            polares.CxMin_0 = 0.023F;
            polares.CyCritH_0 = 1.0F;
            polares.Cy0_0 = 0.012F;
            polares.parabCxCoeff_0 = 0.00103F;
        }
        if ((this.calculateMach() > 0.45D) && (this.calculateMach() < 0.8D) && (this.FM.EI.engines[0].getThrustOutput() < 0.8F)) {
            this.FM.CT.VarWingControl = 0.7F;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
        if ((this.calculateMach() < 0.65D) && (this.FM.getAOA() >= 10D)) {
            this.FM.CT.VarWingControl = 0.7F;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
        if ((this.calculateMach() < 0.45D) && (this.FM.EI.engines[0].getStage() >= 6)) {
            this.FM.CT.VarWingControl = 0.7F;
            this.FM.CT.bHasFlapsControl = true;
            polares.CxMin_0 = 0.025F;
            polares.CyCritH_0 = 1.15F;
            polares.Cy0_0 = 0.025F;
            polares.parabCxCoeff_0 = 0.0009F;
        }
    }

    public void computeSpeedLimiter() {
        Polares polares = (Polares) Reflection.getValue(this.FM, "Wing");
        if (this.calculateMach() > 1.51D) {
            polares.CxMin_0 = 0.031F;
        }
    }

    public void rareAction(float f, boolean flag) {
        if ((this.counter++ % 17) == 0) {
            this.InertialNavigation();
        }
        super.rareAction(f, flag);
    }

    private boolean InertialNavigation() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        if ((aircraft.getSpeed(vector3d) > 20D) && (aircraft.pos.getAbsPoint().z >= 150D) && (aircraft instanceof Su_17M4)) {
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
            new String();
            String s;
            if (d > 260D) {
                s = "" + c + c1;
            } else {
                s = "" + c1;
            }
            new String();
            int l = (int) Math.ceil(d2);
            HUD.log(AircraftHotKeys.hudLogWeaponId, "INS: " + s + "-" + l);
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
    public static float        FlowRate               = 10F;
    public static float        FuelReserve            = 1500F;
    public boolean             bToFire;
    private static Point3d     LaserP3                = new Point3d();
    public boolean             laserOn;
    public boolean             laserLock;
    private Hook               LaserHook[];
    private static Loc         LaserLoc1              = new Loc();
    private static Point3d     LaserP1                = new Point3d();
    private static Point3d     LaserP2                = new Point3d();
    private static Point3d     LaserPL                = new Point3d();
    public boolean             APmode3;
    public float               Timer1;
    public float               Timer2;
    private int                freq;
    private int                counter;
    private int                error;
    private static final float NEG_G_TOLERANCE_FACTOR = 1.5F;
    private static final float NEG_G_TIME_FACTOR      = 1.5F;
    private static final float NEG_G_RECOVERY_FACTOR  = 1.5F;
    private static final float POS_G_TOLERANCE_FACTOR = 4.8F;
    private static final float POS_G_TIME_FACTOR      = 2.0F;
    private static final float POS_G_RECOVERY_FACTOR  = 1.5F;

    static {
        Class class1 = Su_17M4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Su-17");
        Property.set(class1, "meshName", "3DO/Plane/Su-17M4/hierSU17M4.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1972F);
        Property.set(class1, "yearExpired", 2020F);
        Property.set(class1, "FlightModel", "FlightModels/Su-17M3.fmd:Sukhoi_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitSu_7Uc.class, CockpitSu_7Bombardier.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 3, 3, 9, 9, 2, 2, 9, 9, 9, 9, 9, 9, 3, 3, 9, 9, 9, 9, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 9, 9, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_Gun01", "_Gun02", "_ExternalDev01", "_ExternalDev02", "_ExternalTank01", "_ExternalTank02", "_ExternalBomb01", "_ExternalBomb02", "_Dev03", "_Dev04", "_Rock01", "_Rock02", "_Dev05", "_Dev06", "_ExternalDev07", "_ExternalDev08", "_ExternalTank03", "_ExternalTank04", "_ExternalBomb03", "_ExternalBomb04", "_Dev09", "_Dev10", "_Dev11", "_Dev12", "_Rock03", "_Rock04", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_Dev17", "_Dev18", "_Dev19", "_Dev20", "_Rock05", "_Rock06", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_Dev21", "_Dev22", "_Dev23", "_Dev24", "_Rock07", "_Rock08", "_Rock09", "_Rock10", "_Rock11", "_Rock12", "_Rock13", "_Rock14", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalTank05", "_ExternalTank06", "_Dev25", "_Dev26", "_Dev27", "_Dev28", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23",
                "_Rock24", "_Dev29", "_Dev30", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Dev31", "_Dev32", "_Rock29", "_Rock30", "_Rock31", "_Rock32", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_Rock33", "_Rock34" });
    }
}
