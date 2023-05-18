package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.il2.objects.weapons.Pylon;
import com.maddox.rts.Property;

public class FW190D9r5 extends FW_190Dr5 implements TypeFighter, TypeBNZFighter {

    public FW190D9r5() {
        this.kangle = 0.0F;
        this.patate = World.Rnd().nextFloat(1.07F, 1.092F);
        this.prout = World.Rnd().nextFloat(2350F, 2710F);
        this.pouet = World.Rnd().nextFloat(840F, 1060F);
        this.prout1 = 0.0F;
        this.pouet1 = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FW190D9r5.bChangedPit = true;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
        this.FM.CT.bHasLockGearControl = false;
        this.FM.SensPitch = 0.603F;
        this.FM.SensRoll = 0.34F;
        this.FM.SensYaw = 0.49F;
        this.FM.EI.engines[0].tOilOutMaxRPM = World.Rnd().nextFloat(106.4F, 111.3F);
        this.FM.setGCenter(0.122F);
        this.FM.Gears.bTailwheelLocked = false;
        this.FM.EI.engines[0].setPropReductorValue(World.Rnd().nextFloat(0.55221F, 0.55367F));
        this.FM.EI.engines[0].setControlPropAuto(false);
        if (this.FM.isPlayers()) {
            this.FM.CT.setMagnetoControl(0);
        }
        if (this.getGunByHookName("_MGUN01") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("7mmC_D0", false);
            this.hierMesh().chunkVisible("7mmCowl_D0", true);
        }
        if (this.getGunByHookName("_CANNON03") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmL1_D0", false);
        }
        if (this.getGunByHookName("_CANNON04") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("20mmR1_D0", false);
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FW190D9r5.bChangedPit = true;
        }
    }

    protected void moveGear(float f) {
        FW_190Dr5.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void update(float f) {
        World.Rnd().nextFloat(1.0F, 1.08F);
        for (int i = 1; i < 13; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        if (!this.FM.Gears.onGround()) {
            this.FM.EI.engines[0].addVside *= 1.01817D;
            this.FM.EI.engines[0].addVflow *= 1.0076499999999999D;
        }
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if ((this.FM.CT.PowerControl > this.patate) || this.FM.EI.engines[0].getControlAfterburner()) {
                this.FM.AS.setSootState(this, 0, 1);
            } else {
                this.FM.AS.setSootState(this, 0, 0);
            }
        }
        if (this.FM.CT.getTrimElevatorControl() > 0.361F) {
            this.FM.CT.setTrimElevatorControl(0.361F);
        }
        if (this.FM.CT.getTrimElevatorControl() < -0.261F) {
            this.FM.CT.setTrimElevatorControl(-0.261F);
        }
        super.update(f);
        if (this.FM.Gears.onGround() && (this.FM.EI.engines[0].getRPM() < 2600F)) {
            this.FM.SensYaw = 0.86F;
        } else {
            this.FM.SensYaw = 0.49F;
        }
        if (this.FM.Gears.onGround() && (this.FM.CT.ElevatorControl >= 0.06F)) {
            this.FM.Gears.bTailwheelLocked = true;
            this.FM.EI.engines[0].addVside *= 1.0074099999999999D;
            this.FM.EI.engines[0].addVflow *= 1.0076499999999999D;
            this.FM.SensYaw = 0.51F;
        } else {
            if (this.FM.Gears.onGround() && (this.FM.CT.ElevatorControl <= 0.06F)) {
                this.FM.Gears.bTailwheelLocked = false;
                this.FM.SensYaw = 0.86F;
                this.FM.EI.engines[0].addVside *= 1.01817D;
                this.FM.EI.engines[0].addVflow *= 1.0076499999999999D;
            }
            if (this.FM.isPlayers() && this.FM.Gears.onGround()) {
                this.FM.VmaxAllowed = this.FM.getSpeed();
            }
            if (this.FM.VmaxAllowed > 251.3F) {
                this.FM.VmaxAllowed = 251.3F;
            }
            if (!this.FM.isPlayers() && this.FM.Gears.onGround()) {
                if (this.FM.EI.engines[0].getRPM() < 100F) {
                    this.FM.CT.cockpitDoorControl = 1.0F;
                } else {
                    this.FM.CT.cockpitDoorControl = 0.0F;
                }
            }
            if ((this.FM.CT.cockpitDoorControl > 0.9F) && (this.FM.getSpeed() > 18F)) {
                this.FM.VmaxAllowed = this.FM.getSpeed() + 1.0F;
            }
            if (this.FM.EI.engines[0].getRPM() < 3000F) {
                this.FM.EI.engines[0].setManifoldPressure(this.FM.EI.engines[0].getRPM() / 2113F);
            }
            if ((this.FM.EI.engines[0].getControlThrottle() <= 1.0F) && (this.FM.EI.engines[0].getManifoldPressure() > 1.42F)) {
                this.FM.EI.engines[0].setManifoldPressure(1.42F);
            }
            if (this.FM.CT.getElevator() < 0.0F) {
                this.FM.SensPitch = 0.514F;
            } else {
                this.FM.SensPitch = 0.603F;
            }
            for (int l = 0; l < this.FM.CT.Weapons.length; l++) {
                if (this.FM.CT.Weapons[l] != null) {
                    for (int j1 = 0; j1 < this.FM.CT.Weapons[l].length; j1 += 2) {
                        if (((this.FM.CT.Weapons[l][j1] instanceof FuelTankGun) || (this.FM.CT.Weapons[l][j1] instanceof Pylon)) && this.FM.CT.Weapons[l][j1].haveBullets()) {
                            this.FM.Sq.dragParasiteCx *= 0.904F;
                        }
                        if ((this.FM.CT.Weapons[l][j1] instanceof BombGun) && this.FM.CT.Weapons[l][j1].haveBullets()) {
                            this.FM.Sq.dragParasiteCx *= 0.91F;
                        }
                    }

                } else if ((this.FM.EI.engines[0].getControlThrottle() > 1.0F) && !this.FM.EI.engines[0].getControlAfterburner()) {
                    if (this.FM.EI.engines[0].getControlThrottle() > 1.05F) {
                        this.prout1 += f;
                        if (this.prout1 > this.prout) {
                            this.FM.EI.engines[0].tOilOutMaxRPM = 119.8F;
                            this.FM.Sq.dragEngineCx[0] *= 5.4F;
                            if (World.Rnd().nextFloat(0.0F, 1.0F) > 0.97F) {
                                this.FM.EI.engines[0].doSetCyliderKnockOut(1);
                            }
                        }
                    } else {
                        this.prout1 = 0.0F;
                    }
                }
            }

            if ((this.FM.EI.engines[0].getControlThrottle() > 1.05F) && this.FM.EI.engines[0].getControlAfterburner()) {
                this.pouet1 += f;
                if (this.pouet1 > this.pouet) {
                    this.FM.EI.engines[0].tOilOutMaxRPM = 123.8F;
                    this.FM.Sq.dragEngineCx[0] *= 7.2F;
                }
            } else {
                this.pouet1 = 0.0F;
            }
            this.calcg();
            this.calcgneg();
            this.calccg();
        }
    }

    private void calcg() {
        this.G = this.FM.getOverload();
        this.limite = 35400F / this.FM.M.mass;
        if (this.G > (this.limite * 1.1040000000000001D)) {
            this.FM.VmaxAllowed = this.FM.getSpeed() - 30F;
        } else if ((this.FM.EI.engines[0].getRPM() > 2800F) && (this.FM.EI.engines[0].getRPM() <= 3000F)) {
            this.FM.VmaxAllowed = this.FM.getSpeed() + 14.5F;
        } else if (this.FM.EI.engines[0].getRPM() > 3000F) {
            this.FM.VmaxAllowed = this.FM.getSpeed() + 11F;
        } else {
            this.FM.VmaxAllowed = 251.3F;
        }
    }

    private void calcgneg() {
        this.Gneg = this.FM.getOverload();
        this.limiteneg = -17700F / this.FM.M.mass;
        if (this.Gneg < this.limiteneg) {
            this.FM.VmaxAllowed = this.FM.getSpeed() - 30F;
        }
    }

    private void calccg() {
        if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][0] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
            this.FM.setGCenter(0.157F);
            this.FM.Sq.dragParasiteCx *= 0.97F;
        } else {
            for (int l = 0; l < this.FM.CT.Weapons.length; l++) {
                if (this.FM.CT.Weapons[l] != null) {
                    for (int j1 = 0; j1 < this.FM.CT.Weapons[l].length; j1++) {
                        if ((this.FM.CT.Weapons[l][j1] instanceof FuelTankGun) && this.FM.CT.Weapons[l][j1].haveBullets()) {
                            this.FM.setGCenter(0.154F);
                        }
                    }

                } else {
                    this.FM.setGCenter(0.122F);
                }
            }

        }
    }

    public static boolean bChangedPit = false;
    private float         kangle;
    private float         patate;
    private float         G;
    private float         limite;
    private float         Gneg;
    private float         limiteneg;
    private float         prout;
    private float         pouet;
    private float         prout1;
    private float         pouet1;

    static {
        Class class1 = FW190D9r5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw190D_Latetail/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190D9r5.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D9r5.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 3, 9, 3, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12" });
    }
}
