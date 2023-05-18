package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.rts.Property;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class SeaHornet_NF21 extends DH103 implements TypeX4Carrier {

    public SeaHornet_NF21() {
        this.arrestor = 0.0F;
        this.obsLookTime = 0;
        this.obsLookAzimuth = 0.0F;
        this.obsLookElevation = 0.0F;
        this.obsAzimuth = 0.0F;
        this.obsElevation = 0.0F;
        this.obsAzimuthOld = 0.0F;
        this.obsElevationOld = 0.0F;
        this.obsMove = 0.0F;
        this.obsMoveTot = 0.0F;
        this.RSOKilled = false;
        this.scopemode = 1;
        this.bToFire = false;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.fSightCurDistance = 0.0F;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 3000F;
        this.fSightCurSpeed = 200F;
        this.fSightCurReadyness = 0.0F;
        this.hasKAB = false;
        this.fxKAB = this.newSound("weapon.K5.lock", false);
        this.smplKAB = new Sample("K5_lock.wav", 256, 65535);
        this.smplKAB.setInfinite(true);
        this.KABSoundPlaying = false;
        this.KABEngaged = false;
        this.KAB = 0;
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 0.002F;
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -0.002F;
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 0.002F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -0.002F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    private boolean KABscan() {
        Point3d point3d = new Point3d();
        this.pos.getAbs(point3d);
        Vector3d vector3d = new Vector3d();
        Aircraft aircraft = World.getPlayerAircraft();
        double d = Main3D.cur3D().land2D.worldOfsX() + aircraft.pos.getAbsPoint().x;
        double d1 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().y;
        double d2 = Main3D.cur3D().land2D.worldOfsY() + aircraft.pos.getAbsPoint().z;
        int i = (int) (-(aircraft.pos.getAbsOrient().getYaw() - 90D));
        if (i < 0) {
            i = 360 + i;
        }
        int j = (int) (-(aircraft.pos.getAbsOrient().getPitch() - 90D));
        if (j < 0) {
            j = 360 + j;
        }
        for (java.util.Map.Entry entry = Engine.name2Actor().nextEntry(null); entry != null; entry = Engine.name2Actor().nextEntry(entry)) {
            Actor actor = (Actor) entry.getValue();
            if (((actor instanceof BigshipGeneric) || (actor instanceof ShipGeneric)) && (actor.getArmy() != World.getPlayerArmy()) && actor.isAlive() && (actor != World.getPlayerAircraft())) {
                this.pos.getAbs(point3d);
                double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
                double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
                double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
                new String();
                new String();
                Math.floor(actor.pos.getAbsPoint().z * 0.1D);
                Math.floor((actor.getSpeed(vector3d) * 60D * 60D) / 10000D);
                double d6 = (int) (Math.ceil((d2 - d5) / 10D) * 10D);
                new String();
                double d7 = d3 - d;
                double d8 = d4 - d1;
                float f = 57.32484F * (float) Math.atan2(d8, -d7);
                int i1 = (int) (Math.floor((int) f) - 90D);
                if (i1 < 0) {
                    i1 = 360 + i1;
                }
                int j1 = i1 - i;
                double d9 = d - d3;
                double d10 = d1 - d4;
                double d11 = Math.sqrt(d6 * d6);
                int k1 = (int) Math.ceil(Math.sqrt((d10 * d10) + (d9 * d9)));
                float f1 = 57.32484F * (float) Math.atan2(k1, d11);
                int l1 = (int) (Math.floor((int) f1) - 90D);
                if (l1 < 0) {
                    l1 = 360 + l1;
                }
                k1 = (int) (k1 / 1000D);
                int j2 = (int) Math.ceil(k1);
                byte byte0 = 9;
                if (actor instanceof ShipGeneric) {
                    byte0 = 40;
                }
                if (actor instanceof BigshipGeneric) {
                    byte0 = 60;
                }
                if (j1 < 0) {
                    j1 = 360 + j1;
                }
                double d12 = this.FM.getAltitude() / j2;
                if ((k1 <= byte0) && (j2 <= 15D)) {
                    if ((j1 <= 20D) || (j1 >= 340D)) {
                        if (d12 >= 325D) {
                            this.KABEngaged = true;
                            this.playKAB(this.KABEngaged);
                        } else {
                            this.KABEngaged = false;
                            this.playKAB(this.KABEngaged);
                        }
                    } else {
                        this.KABEngaged = false;
                        this.playKAB(this.KABEngaged);
                    }
                } else {
                    this.KABEngaged = false;
                    this.playKAB(this.KABEngaged);
                }
            }
        }

        return true;
    }

    public void playKAB(boolean flag) {
        if (flag && !this.KABSoundPlaying) {
            this.KABSoundPlaying = true;
            this.fxKAB.play(this.smplKAB);
            this.KAB = this.KAB + 1;
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M engaged");
            }
        } else if (!flag && this.KABSoundPlaying) {
            this.KABSoundPlaying = false;
            this.fxKAB.cancel();
            if ((this.KAB > 1) && (((Interpolate) (this.FM)).actor == World.getPlayerAircraft())) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M disengaged");
            }
        }
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, 150F * f, 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", 0.0F, -150F * f, 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(true);
        }
        this.moveWingFold(this.hierMesh(), f);
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("TailHook_D0", 0.0F, 35F * f, 0.0F);
        this.arrestor = f;
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.radarmode = 5;
                this.FM.AS.bIsAboutToBailout = true;
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Head2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.RSOKilled = true;
                break;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            default:
                return super.cutFM(i, j, actor);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("01")) {
            this.hierMesh().chunkVisible("S1ASR", true);
            this.hierMesh().chunkVisible("S7ASR", true);
        }
        if (this.thisWeaponsName.startsWith("02")) {
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
        }
        if (this.thisWeaponsName.startsWith("03")) {
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
        }
        if (this.thisWeaponsName.startsWith("04")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S6PTB", true);
        }
        if (this.thisWeaponsName.startsWith("05")) {
            this.hierMesh().chunkVisible("S1ASR", true);
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
            this.hierMesh().chunkVisible("S7ASR", true);
        }
        if (this.thisWeaponsName.startsWith("06")) {
            this.hierMesh().chunkVisible("S1ASR", true);
            this.hierMesh().chunkVisible("S2FAB", true);
            this.hierMesh().chunkVisible("S6FAB", true);
            this.hierMesh().chunkVisible("S7ASR", true);
        }
        if (this.thisWeaponsName.startsWith("07")) {
            this.hierMesh().chunkVisible("S1ASR", true);
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S6PTB", true);
            this.hierMesh().chunkVisible("S7ASR", true);
        }
        if (this.thisWeaponsName.startsWith("08")) {
            this.hierMesh().chunkVisible("S2ASM", true);
            this.hierMesh().chunkVisible("S4ASM", true);
            this.hierMesh().chunkVisible("S6ASM", true);
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("09")) {
            this.hierMesh().chunkVisible("S2ASM", true);
            this.hierMesh().chunkVisible("S3FAB", true);
            this.hierMesh().chunkVisible("S4ASM", true);
            this.hierMesh().chunkVisible("S5FAB", true);
            this.hierMesh().chunkVisible("S6ASM", true);
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("10")) {
            this.hierMesh().chunkVisible("S4KAB", true);
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("11")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S4KAB", true);
            this.hierMesh().chunkVisible("S6PTB", true);
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("12")) {
            this.hierMesh().chunkVisible("S2ASM", true);
            this.hierMesh().chunkVisible("S4KAB", true);
            this.hierMesh().chunkVisible("S6ASM", true);
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("13")) {
            this.hierMesh().chunkVisible("S4KAB", true);
            this.hasKAB = true;
            this.scopemode = 2;
        }
        if (this.thisWeaponsName.startsWith("14")) {
            this.hierMesh().chunkVisible("S2PTB", true);
            this.hierMesh().chunkVisible("S4KAB", true);
            this.hierMesh().chunkVisible("S6PTB", true);
            this.hasKAB = true;
        }
        if (this.thisWeaponsName.startsWith("15")) {
            this.hierMesh().chunkVisible("S2ASM", true);
            this.hierMesh().chunkVisible("S4KAB", true);
            this.hierMesh().chunkVisible("S6ASM", true);
            this.hasKAB = true;
            this.scopemode = 2;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.hasKAB) {
            this.KABscan();
        }
        if ((this.FM.Gears.nearGround() || this.FM.Gears.onGround()) && (this.FM.CT.getCockpitDoor() == 1.0F)) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
        if (!this.RSOKilled) {
            if (this.obsLookTime == 0) {
                this.obsLookTime = 2 + World.Rnd().nextInt(1, 3);
                this.obsMoveTot = 1.0F + (World.Rnd().nextFloat() * 1.5F);
                this.obsMove = 0.0F;
                this.obsAzimuthOld = this.obsAzimuth;
                this.obsElevationOld = this.obsElevation;
                if (World.Rnd().nextFloat() > 0.8D) {
                    this.obsAzimuth = 0.0F;
                    this.obsElevation = 0.0F;
                } else {
                    this.obsAzimuth = (World.Rnd().nextFloat() * 120F) - 70F;
                    this.obsElevation = (World.Rnd().nextFloat() * 40F) - 20F;
                }
            } else {
                this.obsLookTime--;
            }
        }
    }

    public void update(float f) {
        super.update(f);
        if (!(this.FM instanceof Pilot)) {
            return;
        }
        if (this.hasKAB && !this.FM.CT.Weapons[3][0].haveBullets()) {
            this.hasKAB = false;
            this.fxKAB.cancel();
            if (((Interpolate) (this.FM)).actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "KAB-500M launched");
            }
        }
        if ((this.obsMove < this.obsMoveTot) && !this.RSOKilled && !this.FM.AS.isPilotParatrooper(1)) {
            if ((this.obsMove < 0.2F) || (this.obsMove > (this.obsMoveTot - 0.2F))) {
                this.obsMove += 0.3D * f;
            } else if ((this.obsMove < 0.1F) || (this.obsMove > (this.obsMoveTot - 0.1F))) {
                this.obsMove += 0.15F;
            } else {
                this.obsMove += 1.2D * f;
            }
            this.obsLookAzimuth = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsAzimuthOld, this.obsAzimuth);
            this.obsLookElevation = Aircraft.cvt(this.obsMove, 0.0F, this.obsMoveTot, this.obsElevationOld, this.obsElevation);
            this.hierMesh().chunkSetAngles("Head2_D0", 0.0F, this.obsLookAzimuth, this.obsLookElevation);
        }
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -50F, 7F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-33F * this.FM.Gears.arrestorVSink) / 57F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.2F) {
                    f2 = 0.2F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
    }

    private float   arrestor;
    private int     obsLookTime;
    private float   obsLookAzimuth;
    private float   obsLookElevation;
    private float   obsAzimuth;
    private float   obsElevation;
    private float   obsAzimuthOld;
    private float   obsElevationOld;
    private float   obsMove;
    private float   obsMoveTot;
    private float   deltaAzimuth;
    private float   deltaTangage;
    public float    fSightCurDistance;
    public float    fSightCurForwardAngle;
    public float    fSightCurSideslip;
    public float    fSightCurAltitude;
    public float    fSightCurSpeed;
    public float    fSightCurReadyness;
    private boolean hasKAB;
    private SoundFX fxKAB;
    private Sample  smplKAB;
    private boolean KABSoundPlaying;
    private boolean KABEngaged;
    private int     KAB;

    static {
        Class class1 = SeaHornet_NF21.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SeaHornet NF.21");
        Property.set(class1, "meshName", "3DO/Plane/SeaHornetNF21(Multi1)/Hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1946.8F);
        Property.set(class1, "yearExpired", 1957.7F);
        Property.set(class1, "FlightModel", "FlightModels/DH103-NF21.fmd:DH103-NF21_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDH103.class, CockpitDH_ASH.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 9, 9, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalBomb03", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06" });
    }
}
