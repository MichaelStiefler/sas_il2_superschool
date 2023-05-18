package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombAB1000;
import com.maddox.il2.objects.weapons.BombAB500;
import com.maddox.il2.objects.weapons.BombGun4512;
import com.maddox.il2.objects.weapons.BombGunTorpF5Bheavy;
import com.maddox.il2.objects.weapons.BombGunTorpFiume;
import com.maddox.il2.objects.weapons.BombGunTorpLTF5Practice;
import com.maddox.il2.objects.weapons.BombGunTorpMk13;
import com.maddox.il2.objects.weapons.BombSB1000;
import com.maddox.il2.objects.weapons.BombSC1000;
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

public class FW_190_SeaDora extends Fw_190_Sea implements TypeFighter, TypeBNZFighter, TypeBomber {

    public FW_190_SeaDora() {
        this.kangle = 0.0F;
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
                if (((Maneuver) this.FM).hasRockets() || ((Maneuver) this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun)) {
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
                if (((Maneuver) this.FM).hasRockets() || ((Maneuver) this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun)) {
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
                if (((Maneuver) this.FM).hasRockets() || ((Maneuver) this.FM).hasBombs() || (aobj[i] instanceof BombSC500) || (aobj[i] instanceof BombSD500) || (aobj[i] instanceof BombAB500) || (aobj[i] instanceof BombAB1000) || (aobj[i] instanceof BombSB1000) || (aobj[i] instanceof BombSC1000) || (aobj[i] instanceof BombGunTorpFiume) || (aobj[i] instanceof BombGunTorpMk13) || (aobj[i] instanceof BombGunTorpF5Bheavy) || (aobj[i] instanceof BombGunTorpLTF5Practice) || (aobj[i] instanceof BombGun4512) || (aobj[i] instanceof RocketGunPC1000RS) || (aobj[i] instanceof RocketGunWfrGr21) || (aobj[i] instanceof FuelTankGun)) {
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
                    int j = 0;
                    do {
                        if (j >= aobj.length) {
                            break;
                        }
                        if (((Maneuver) this.FM).hasRockets() || ((Maneuver) this.FM).hasBombs() || (aobj[j] instanceof BombSC500) || (aobj[j] instanceof BombSD500) || (aobj[j] instanceof BombAB500) || (aobj[j] instanceof BombAB1000) || (aobj[j] instanceof BombSB1000) || (aobj[j] instanceof BombSC1000) || (aobj[j] instanceof BombGunTorpFiume) || (aobj[j] instanceof BombGunTorpMk13) || (aobj[j] instanceof BombGunTorpF5Bheavy) || (aobj[j] instanceof BombGunTorpLTF5Practice) || (aobj[j] instanceof BombGun4512) || (aobj[j] instanceof RocketGunPC1000RS) || (aobj[j] instanceof RocketGunWfrGr21) || (aobj[j] instanceof FuelTankGun)) {
                            this.boosterFireOutTime = Time.current() + 30000L;
                            this.doFireBoosters();
                            this.FM.AS.setGliderBoostOn();
                            break;
                        }
                        j++;
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
        for (int i = 1; i < 13; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        super.update(f);
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
        hiermesh.chunkSetAngles("GearC99_D0", 20F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f) {
        FW_190_SeaDora.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() >= 0.98F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        }
    }

    private float     kangle;
    private Bomb      booster[];
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;

    static {
        Class class1 = FW_190_SeaDora.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190D-13T/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1943.11F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190D-13N.fmd:NavalFw190D_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D11Sea.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 9, 3, 9, 9, 9, 1, 1, 9, 9, 1, 1, 1, 1, 9, 9, 1, 1, 9, 9, 2, 2, 9, 9, 1, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_CANNON03", "_CANNON04", "_ExternalDev05", "_ExternalDev06", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_ExternalDev07", "_ExternalDev08", "_CANNON09", "_CANNON10", "_ExternalDev09", "_ExternalDev10", "_ExternalRock01", "_ExternalRock02", "_ExternalDev11", "_ExternalDev12", "_CANNON11", "_CANNON12" });
    }
}
