package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class F51D extends P_51 implements TypeFighterAceMaker {

    public F51D() {
        this.k14Mode = 2;
        this.k14WingspanType = 1;
        this.k14Distance = 250F;
        this.timeCounter = 0.0F;
        this.timeCounterneg = 0.0F;
        this.temps = World.Rnd().nextFloat(1260F, 1740F);
        this.tempsneg = World.Rnd().nextFloat(12F, 16F);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][0] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) || ((this.FM.CT.Weapons[2] != null) && (this.FM.CT.Weapons[2][0] != null) && this.FM.CT.Weapons[2][this.FM.CT.Weapons[2].length - 1].haveBullets())) {
            this.hierMesh().chunkVisible("Pylon1_D0", true);
            this.hierMesh().chunkVisible("Pylon2_D0", true);
            this.hierMesh().chunkVisible("Pylon3_D0", true);
            this.hierMesh().chunkVisible("Pylon4_D0", true);
            this.hierMesh().chunkVisible("Pylon5_D0", true);
            this.hierMesh().chunkVisible("Pylon6_D0", true);
            this.hierMesh().chunkVisible("Pylon7_D0", true);
            this.hierMesh().chunkVisible("Pylon8_D0", true);
            this.hierMesh().chunkVisible("Pylon9_D0", true);
            this.hierMesh().chunkVisible("Pylon10_D0", true);
            this.hierMesh().chunkVisible("Pylon11_D0", true);
            this.hierMesh().chunkVisible("Pylon12_D0", true);
        } else {
            this.hierMesh().chunkVisible("Pylon1_D0", false);
            this.hierMesh().chunkVisible("Pylon2_D0", false);
            this.hierMesh().chunkVisible("Pylon3_D0", false);
            this.hierMesh().chunkVisible("Pylon4_D0", false);
            this.hierMesh().chunkVisible("Pylon5_D0", false);
            this.hierMesh().chunkVisible("Pylon6_D0", false);
            this.hierMesh().chunkVisible("Pylon7_D0", false);
            this.hierMesh().chunkVisible("Pylon8_D0", false);
            this.hierMesh().chunkVisible("Pylon9_D0", false);
            this.hierMesh().chunkVisible("Pylon10_D0", false);
            this.hierMesh().chunkVisible("Pylon11_D0", false);
            this.hierMesh().chunkVisible("Pylon12_D0", false);
        }
        this.FM.CT.bHasLockGearControl = false;
        this.FM.SensPitch = 0.496F;
        this.FM.SensRoll = 0.267F;
        this.FM.SensYaw = 0.65F;
        this.FM.EI.engines[0].tOilOutMaxRPM = 106F;
        this.FM.setGCenter(0.0F);
        this.FM.EI.engines[0].setPropReductorValue(0.591F);
        this.FM.AS.setLandingLightState(false);
        this.FM.Gears.bTailwheelLocked = true;
    }

    public void update(float f) {
        float k = World.Rnd().nextFloat(0.87F, 1.04F);
        if (this.FM.isPlayers() && (this.FM.CT.cockpitDoorControl > 0.9F) && (this.FM.getSpeed() > (70F * k)) && (this.FM.AS.aircraft.hierMesh().chunkFindCheck("Blister1_D0") != -1) && (this.FM.AS.getPilotHealth(0) > 0.0F)) {
            this.playSound("aircraft.arrach", true);
            this.FM.AS.aircraft.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage((ActorHMesh) this.FM.AS.actor, this.FM.AS.aircraft.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.AS.aircraft.FM.Vwld);
            wreckage.setSpeed(vector3d);
            this.FM.CT.cockpitDoorControl = 0.9F;
            this.FM.CT.bHasCockpitDoorControl = false;
            this.FM.VmaxAllowed = 161F;
            this.FM.Sq.dragEngineCx[0] *= 6.2F;
        }
        if (!this.FM.isPlayers()) {
            if (this.FM.EI.engines[0].getRPM() < 500F) {
                this.FM.CT.cockpitDoorControl = 1.0F;
            } else {
                this.FM.CT.cockpitDoorControl = 0.0F;
            }
        }
        if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.getSpeed() > 18F)) {
            this.FM.VmaxAllowed = this.FM.getSpeed() + 1.0F;
        }
        if ((this.FM.CT.GearControl < 0.5F) && this.FM.AS.bLandingLightOn) {
            this.FM.AS.setLandingLightState(false);
        }
        if (!this.FM.Gears.onGround()) {
            this.FM.SensYaw = 0.48F;
            this.petrole();
            this.FM.EI.engines[0].addVside *= 1.01953D;
            this.FM.EI.engines[0].addVflow *= 1.00765D;
        }
        if (!this.FM.Gears.onGround() && (this.FM.getSpeed() < 48F)) {
            this.FM.setGCenter(-0.09F);
        }
        if (this.FM.getSpeed() > (40F * k)) {
            this.FM.CT.bHasCockpitDoorControl = false;
        } else {
            this.FM.CT.bHasCockpitDoorControl = true;
        }
        if ((this.FM.EI.engines[0].getRPM() > 2920F) || (this.FM.EI.engines[0].getManifoldPressure() > 2.304F) || (this.timeCounter > 1400F)) {
            this.timeCounter += f;
            if (this.timeCounter > this.temps) {
                this.FM.EI.engines[0].tOilOutMaxRPM = 119.8F;
                this.FM.Sq.dragEngineCx[0] *= 5.4F;
            }
        } else {
            this.timeCounter = 0.0F;
        }
        if (this.FM.getOverload() < 0.0F) {
            this.timeCounterneg += f;
            if (this.timeCounterneg > this.tempsneg) {
                this.timeCounterneg = 0.0F;
                this.FM.EI.engines[0].tOilOutMaxRPM = 119.4F;
                this.FM.Sq.dragEngineCx[0] *= 5.7F;
                if (World.Rnd().nextFloat(0.0F, 1.0F) > 0.54F) {
                    this.FM.EI.engines[0].doSetCyliderKnockOut(1);
                }
            }
        } else {
            this.timeCounterneg = 0.0F;
        }
        super.update(f);
        this.calcg();
        this.compression();
        this.roulette();
        this.trims();
    }

    private void trims() {
        if (this.FM.CT.getTrimAileronControl() > 0.35F) {
            this.FM.CT.setTrimAileronControl(0.35F);
        }
        if (this.FM.CT.getTrimAileronControl() < -0.35F) {
            this.FM.CT.setTrimAileronControl(-0.35F);
        }
        if (this.FM.CT.getTrimElevatorControl() > 0.241F) {
            this.FM.CT.setTrimElevatorControl(0.241F);
        }
        if (this.FM.CT.getTrimElevatorControl() < -0.241F) {
            this.FM.CT.setTrimElevatorControl(-0.241F);
        }
    }

    private void roulette() {
        if (this.FM.Gears.onGround() && (this.FM.CT.ElevatorControl >= 0.0F)) {
            if (this.FM.EI.engines[0].getManifoldPressure() <= 1.417F) {
                this.FM.Gears.bTailwheelLocked = true;
                this.FM.EI.engines[0].addVside *= 1.01253D;
                this.FM.EI.engines[0].addVflow *= 1.00765D;
            } else if (this.FM.EI.engines[0].getManifoldPressure() > 1.417F) {
                this.FM.Gears.bTailwheelLocked = true;
                this.FM.SensYaw = 0.41F;
                this.FM.getSpeedKMH();
                this.FM.EI.engines[0].addVside *= 1.01953D;
                this.FM.EI.engines[0].addVflow *= 1.00765D;
            }
        } else if (this.FM.Gears.onGround() && (this.FM.CT.ElevatorControl < -0.58F) && (this.FM.EI.engines[0].getManifoldPressure() < 1.417F)) {
            this.FM.Gears.bTailwheelLocked = false;
            this.FM.SensYaw = 0.91F;
            this.petrole();
            this.FM.EI.engines[0].addVside *= 0.9152D;
        } else if (this.FM.Gears.onGround() && (this.FM.CT.ElevatorControl < -0.42F) && (this.FM.EI.engines[0].getManifoldPressure() > 1.417F)) {
            this.FM.Gears.bTailwheelLocked = false;
            this.FM.SensYaw = 0.91F;
            this.petrole();
            double couple2 = this.FM.getSpeedKMH();
            if (couple2 > 80D) {
                couple2 = 80D;
            }
            this.FM.EI.engines[0].addVside *= 1.01153D + (couple2 / 10000D);
            this.FM.EI.engines[0].addVflow *= 1.00765D;
        }
    }

    private void petrole() {
        float petrole = this.FM.M.fuel;
        float cg = (petrole - 593F) / 591.28F;
        if (cg < 0.0F) {
            cg = 0.0F;
        }
        float reverse = this.FM.getAOA();
        reverse = (cg * reverse) / 11.9F;
        if (reverse <= 0.0F) {
            reverse = 1E-005F;
        }
        this.FM.SensPitch += reverse;
        if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][0] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
            this.FM.setGCenter(0.007F - cg - reverse);
        } else {
            this.FM.setGCenter(0.015F - cg - reverse);
        }
        for (int l = 0; l < this.FM.CT.Weapons.length; l++) {
            if (this.FM.CT.Weapons[l] != null) {
                for (int j1 = 0; j1 < this.FM.CT.Weapons[l].length; j1 += 2) {
                    if ((this.FM.CT.Weapons[l][j1] instanceof FuelTankGun) && this.FM.CT.Weapons[l][j1].haveBullets()) {
                        this.FM.setGCenter(0.007F - cg - reverse);
                    } else {
                        this.FM.setGCenter(0.015F - cg - reverse);
                    }
                }

            }
        }

    }

    private void calcg() {
        this.G = this.FM.getOverload();
        this.limite = 36800F / this.FM.M.mass;
        if (this.limite < this.G) {
            this.FM.SensPitch = this.FM.SensPitch - (this.G / 91F);
        }
        if (this.G > (this.limite * 1.1040000000000001D)) {
            this.FM.VmaxAllowed = this.FM.getSpeed() - 30F;
            this.FM.SensPitch = this.FM.SensPitch + (this.G / 91F);
        } else if (this.FM.CT.cockpitDoorControl != 0.9F) {
            this.FM.SensPitch = 0.496F - (this.G / 91F);
        } else {
            this.FM.SensPitch = 0.496F - (this.G / 91F);
        }
    }

    private void compression() {
        if (this.FM.CT.cockpitDoorControl != 0.9F) {
            this.coeff = World.Rnd().nextFloat(-0.15F, 0.012F) * (this.FM.getSpeed() / 100F);
            if (this.FM.getSpeed() > 264F) {
                this.FM.VmaxAllowed = this.FM.getSpeed() + 2.0F;
                if (this.FM.VmaxAllowed > 310F) {
                    this.FM.VmaxAllowed = 310F;
                }
                this.FM.SensPitch = this.FM.SensPitch + (this.coeff * 1.71F);
                this.FM.SensRoll = this.FM.SensRoll - (this.coeff * 0.34F);
            } else {
                this.FM.SensPitch = 0.496F - (this.G / 67F);
                this.FM.SensRoll = 0.267F;
                this.petrole();
            }
        }
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        if (this.FM.CT.GearControl > 0.5F) {
            this.hierMesh().chunkSetAngles("GearC1_D0", 0.0F, -6F * f, 0.0F);
        }
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 50F;
        if (this.k14Distance > 800F) {
            this.k14Distance = 800F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 50F;
        if (this.k14Distance < 150F) {
            this.k14Distance = 150F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.k14Mode);
        netmsgguaranted.writeByte(this.k14WingspanType);
        netmsgguaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.k14Mode = netmsginput.readByte();
        this.k14WingspanType = netmsginput.readByte();
        this.k14Distance = netmsginput.readFloat();
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.675F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    private float G;
    private float limite;
    private float coeff;
    private float timeCounter;
    private float timeCounterneg;
    private float temps;
    private float tempsneg;
    public int    k14Mode;
    public int    k14WingspanType;
    public float  k14Distance;

    static {
        Class class1 = F51D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-51");
        Property.set(class1, "meshNameDemo", "3DO/Plane/F-51(USA)/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/F-51(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "meshName_us", "3DO/Plane/F-51(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "noseart", 1);
        Property.set(class1, "yearService", 1947F);
        Property.set(class1, "yearExpired", 1957.5F);
        Property.set(class1, "FlightModel", "FlightModels/F51D.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF51.class });
        Property.set(class1, "LOSElevation", 1.06935F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDEV01", "_ExternalDEV02", "_ExternalDEV03", "_ExternalDEV04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06" });
    }
}
