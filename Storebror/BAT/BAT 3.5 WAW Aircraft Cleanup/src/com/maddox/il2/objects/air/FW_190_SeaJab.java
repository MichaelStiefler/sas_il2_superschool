package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombAB1000;
import com.maddox.il2.objects.weapons.BombAB500;
import com.maddox.il2.objects.weapons.BombGun4512;
import com.maddox.il2.objects.weapons.BombGunTorpF5Bheavy;
import com.maddox.il2.objects.weapons.BombGunTorpFiume;
import com.maddox.il2.objects.weapons.BombGunTorpLTF5Practice;
import com.maddox.il2.objects.weapons.BombGunTorpMk13;
import com.maddox.il2.objects.weapons.BombPC1600;
import com.maddox.il2.objects.weapons.BombSB1000;
import com.maddox.il2.objects.weapons.BombSC1000;
import com.maddox.il2.objects.weapons.BombSC1800;
import com.maddox.il2.objects.weapons.BombSC2000;
import com.maddox.il2.objects.weapons.BombSC500;
import com.maddox.il2.objects.weapons.BombSD500;
import com.maddox.il2.objects.weapons.BombStarthilfeSolfuelL;
import com.maddox.il2.objects.weapons.BombStarthilfeSolfuelR;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.RocketGunPC1000RS;
import com.maddox.il2.objects.weapons.RocketGunWfrGr21;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class FW_190_SeaJab extends Fw_190_Sea implements TypeX4Carrier, TypeBomber {

    public FW_190_SeaJab() {
        this.bToFire = false;
        this.tX4Prev = 0L;
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.booster = new Bomb[2];
        this.bHasBoosters = true;
        this.boosterFireOutTime = -1L;
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) {
                    break;
                }
                if (((Maneuver) this.FM).hasRockets() || ((Maneuver) this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun)) {
                    if ((this.booster[0] != null) && (this.booster[1] != null)) {
                        Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
                    }
                    Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
                    break;
                }
                i++;
            } while (true);
        }
    }

    public void doCutBoosters() {
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) {
                    break;
                }
                if (((Maneuver) this.FM).hasRockets() || ((Maneuver) this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun)) {
                    for (int j = 0; j < 2; j++) {
                        if (this.booster[j] != null) {
                            this.booster[j].start();
                            this.booster[j] = null;
                        }
                    }

                    break;
                }
                i++;
            } while (true);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            int i = 0;
            do {
                if (i >= aobj.length) {
                    break;
                }
                if (((Maneuver) this.FM).hasRockets() || ((Maneuver) this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun)) {
                    try {
                        this.booster[0] = new BombStarthilfeSolfuelL();
                        ((Actor) (this.booster[0])).pos.setBase(this, this.findHook("_BoosterH1"), false);
                        ((Actor) (this.booster[0])).pos.resetAsBase();
                        this.booster[0].drawing(true);
                    } catch (Exception exception) {
                        this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
                    }
                    try {
                        this.booster[1] = new BombStarthilfeSolfuelR();
                        ((Actor) (this.booster[1])).pos.setBase(this, this.findHook("_BoosterH2"), false);
                        ((Actor) (this.booster[1])).pos.resetAsBase();
                        this.booster[1].drawing(true);
                    } catch (Exception exception1) {
                        this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
                    }
                    break;
                }
                i++;
            } while (true);
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
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        if ((this.FM instanceof Pilot) && this.bHasBoosters) {
            if ((this.FM.getAltitude() > 300F) && (this.boosterFireOutTime == -1L) && (this.FM.Loc.z != 0.0D) && (World.Rnd().nextFloat() < 0.05F)) {
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
            }
            if (this.bHasBoosters && (this.boosterFireOutTime == -1L) && this.FM.Gears.onGround() && (this.FM.EI.getPowerOutput() > 0.9F) && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.getSpeedKMH() > 20F)) {
                Object aobj[] = this.pos.getBaseAttached();
                if (aobj != null) {
                    int i = 0;
                    do {
                        if (i >= aobj.length) {
                            break;
                        }
                        if (((Maneuver) this.FM).hasRockets() || ((Maneuver) this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombPC1600) || (aobj[i] instanceof BombSC1800) || (aobj[i] instanceof BombSC2000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun)) {
                            this.boosterFireOutTime = Time.current() + 30000L;
                            this.doFireBoosters();
                            this.FM.AS.setGliderBoostOn();
                            break;
                        }
                        i++;
                    } while (true);
                } else {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
            }
            if (this.bHasBoosters && (this.boosterFireOutTime > 0L)) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 20000D;
                }
                if (Time.current() > (this.boosterFireOutTime + 10000L)) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
            }
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 40F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f) {
        FW_190_SeaJab.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() >= 0.98F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if ((!(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && flag && (this.FM instanceof Pilot)) {
            Pilot pilot = (Pilot) this.FM;
            if ((pilot.get_maneuver() == 63) && (((Maneuver) (pilot)).target != null)) {
                Point3d point3d = new Point3d(((FlightModelMain) (((Maneuver) (pilot)).target)).Loc);
                point3d.sub(this.FM.Loc);
                this.FM.Or.transformInv(point3d);
                if ((((point3d.x > 4000D) && (point3d.x < 5500D)) || ((point3d.x > 100D) && (point3d.x < 5000D) && (World.Rnd().nextFloat() < 0.33F))) && (Time.current() > (this.tX4Prev + 10000L))) {
                    this.bToFire = true;
                    this.tX4Prev = Time.current();
                }
            }
        }
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 1.0F;
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -1F;
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -1F;
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

    public float getFullMass() {
        return this.mass;
    }

    public boolean    bToFire;
    private long      tX4Prev;
    private float     deltaAzimuth;
    private float     deltaTangage;
    public float      mass;
    protected boolean mgffEnable;
    protected boolean frontbox;
    protected boolean boostersEnable;
    private Bomb      booster[];
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;

    static {
        Class class1 = FW_190_SeaJab.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190F-9T/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190F-9N.fmd:NavalFw190F_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190F8T.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 2, 2, 1, 1, 1, 1, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 3, 9, 1, 3, 3, 3, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb13", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalRock25", "_ExternalRock26", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev10", "_ExternalDev11",
                "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock31", "_ExternalRock32", "_ExternalRock32", "_ExternalDev12", "_ExternalDev13", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev14", "_ExternalDev15", "_CANNON09", "_CANNON10", "_ExternalDev16", "_ExternalDev17", "_ExternalBomb10", "_ExternalDev18", "_CANNON11", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb05", "_ExternalDev19" });
    }
}
