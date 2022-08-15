package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class FW_190D9early extends FW_190DNEW implements TypeFighter, TypeBNZFighter {

    public FW_190D9early() {
        this.kangle = 0.0F;
        this.patate = World.Rnd().nextFloat(1.07F, 1.096F);
        this.prout = World.Rnd().nextFloat(2350F, 2710F);
        this.prout1 = 0.0F;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            FW_190D9early.bChangedPit = true;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasLockGearControl = false;
        this.FM.SensPitch = 0.603F;
        this.FM.SensRoll = 0.34F;
        this.FM.SensYaw = 0.49F;
        this.FM.EI.engines[0].tOilOutMaxRPM = World.Rnd().nextFloat(106.4F, 111.3F);
        this.FM.setGCenter(0.122F);
        this.FM.Gears.bTailwheelLocked = false;
        this.FM.EI.engines[0].setPropReductorValue(World.Rnd().nextFloat(0.54952F, 0.5523F));
        this.FM.EI.engines[0].setControlPropAuto(false);
        if (this.FM.isPlayers()) {
            this.FM.CT.setMagnetoControl(0);
        }
        this.FM.CT.bHasAirBrakeControl = true;
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
            FW_190D9early.bChangedPit = true;
        }
    }

    protected void moveGear(float f) {
        FW_190DNEW.moveGear(this.hierMesh(), f);
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
            this.FM.EI.engines[0].addVside *= 1.01717D;
            this.FM.EI.engines[0].addVflow *= 1.00765D;
        }
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            if (this.FM.CT.PowerControl > this.patate) {
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
            this.FM.EI.engines[0].addVside *= 1.00741D;
            this.FM.EI.engines[0].addVflow *= 1.00765D;
            this.FM.SensYaw = 0.63F;
        } else {
            if (this.FM.Gears.onGround() && (this.FM.CT.ElevatorControl <= 0.06F)) {
                this.FM.Gears.bTailwheelLocked = false;
                this.FM.SensYaw = 0.86F;
                this.FM.EI.engines[0].addVside *= 1.01717D;
                this.FM.EI.engines[0].addVflow *= 1.00765D;
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
            if ((this.FM.CT.cockpitDoorControl > 0.5F) && (this.FM.getSpeed() > 18F)) {
                this.FM.VmaxAllowed = this.FM.getSpeed() + 1.0F;
            }
            if (this.FM.EI.engines[0].getControlThrottle() > 1.0F) {
                if (this.FM.CT.getElevator() < 0.0F) {
                    this.FM.SensPitch = 0.534F;
                } else {
                    this.FM.SensPitch = 0.603F;
                }
            }
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
            this.calcg();
            this.calcgneg();
            this.calccg();
        }
    }

    private void calcg() {
        this.G = this.FM.getOverload();
        this.limite = 35400F / this.FM.M.mass;
        if (this.G > (this.limite * 1.104D)) {
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
    private float         prout1;

    static {
        Class class1 = FW_190D9early.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190D-9early/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190D9early.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190D9early.class });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 3, 9, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02", "_ExternalBomb02" });
    }
}
