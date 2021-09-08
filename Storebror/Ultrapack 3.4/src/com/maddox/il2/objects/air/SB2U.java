package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.rts.Time;

public abstract class SB2U extends SB2Uxyz implements TypeHaveBombReleaseGear {

    public SB2U() {
        this.isReleased = false;
        this.isExtendedStart = false;
        this.isExtended = false;
        this.tickConstLenFs = 0.0F;
        this.armAngle = 0.0F;
        this.oldArmAngle = 0.0F;
        this.bombHookName = null;
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bDropsBombs = false;
    }

    public void update(float f) {
        super.update(f);
        this.moveArm();
        if ((this == World.getPlayerAircraft()) && (this.FM instanceof RealFlightModel)) {
            if (((RealFlightModel) this.FM).isRealMode()) {
                switch (this.diveMechStage) {
                    case 0:
                        if (this.bNDives && (this.FM.CT.AirBrakeControl == 1.0F) && (this.FM.CT.AirBrakeControl == 0.0F)) {
                            this.diveMechStage++;
                            this.bNDives = false;
                        } else {
                            this.bNDives = (this.FM.CT.AirBrakeControl != 1.0F) && (this.FM.CT.AirBrakeControl != 0.0F);
                        }
                        break;

                    case 1:
                        this.FM.CT.setTrimElevatorControl(-0.65F);
                        this.FM.CT.trimElevator = -0.65F;
                        if ((this.FM.CT.AirBrakeControl == 0.0F) || this.FM.CT.saveWeaponControl[5] || ((this.FM.CT.Weapons[5] != null) && (this.FM.CT.Weapons[5][this.FM.CT.Weapons[5].length - 1].countBullets() == 0) && !(this instanceof SB2U))) {
                            if (this.FM.CT.AirBrakeControl == 0.0F) {
                                this.diveMechStage++;
                            }
                            if ((this.FM.CT.Weapons[5] != null) && (this.FM.CT.Weapons[5][this.FM.CT.Weapons[5].length - 1].countBullets() == 0)) {
                                this.diveMechStage++;
                            }
                        }
                        break;

                    case 2:
                        if (this.FM.isTick(41, 0)) {
                            this.FM.CT.setTrimElevatorControl(0.85F);
                            this.FM.CT.trimElevator = 0.85F;
                        }
                        if ((this.FM.CT.AirBrakeControl == 0.0F) || (this.FM.Or.getTangage() > 0.0F)) {
                            this.diveMechStage++;
                        }
                        break;

                    case 3:
                        this.FM.CT.setTrimElevatorControl(0.0F);
                        this.FM.CT.trimElevator = 0.0F;
                        this.diveMechStage = 0;
                        break;
                }
            } else {
                this.FM.CT.setTrimElevatorControl(0.0F);
                this.FM.CT.trimElevator = 0.0F;
            }
        }
        if (this.bDropsBombs && this.FM.isTick(3, 0) && (this.FM.CT.Weapons[5] != null) && (this.FM.CT.Weapons[3][this.FM.CT.Weapons[5].length - 1] != null) && this.FM.CT.Weapons[3][this.FM.CT.Weapons[5].length - 1].haveBullets()) {
            this.FM.CT.WeaponControl[5] = true;
        }
    }

    public boolean waitRelease() {
        if ((this.bombGun == null) || !this.bombGun.isShots()) {
            return true;
        }
        this.isExtendedStart = true;
        if (this.tickConstLenFs == 0.0F) {
            this.tickConstLenFs = Time.tickConstLenFs();
        }
        return this.isReleased;
    }

    private void moveArm() {
        if (!this.isExtendedStart) {
            return;
        }
        if (!this.isExtended) {
            this.armAngle += 90F * this.tickConstLenFs;
        }
        if (this.armAngle >= 90F) {
            this.isReleased = true;
            this.isExtended = true;
        } else if (this.FM.getOverload() > 1.0F) {
            this.isReleased = true;
        }
        if (this.isReleased && this.isExtended && (this.armAngle > 0.0F)) {
            this.armAngle -= 180F * this.tickConstLenFs;
        }
        if (this.oldArmAngle != this.armAngle) {
            try {
                this.hierMesh().chunkSetAngles("Arm1_D0", 0.0F, this.armAngle, 0.0F);
                this.hierMesh().chunkSetAngles("Arm2_D0", 0.0F, -this.armAngle, 0.0F);
                this.bombGun.updateHook(this.bombHookName);
            } catch (Exception exception) {
            }
            this.oldArmAngle = this.armAngle;
        }
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Flap_D0", 0.0F, 0.0F, 45F * Math.max(f, this.FM.CT.getFlap()));
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        super.hitBone(s, shot, point3d);
        if (s.startsWith("xxmgun")) {
            if (s.endsWith("3")) {
                this.debuggunnery("Cowling Gun: Disabled..");
                this.FM.AS.setJamBullets(0, 2);
            }
            if (s.endsWith("4")) {
                this.debuggunnery("Cowling Gun: Disabled..");
                this.FM.AS.setJamBullets(0, 3);
            }
            if (s.endsWith("5")) {
                this.debuggunnery("Cowling Gun: Disabled..");
                this.FM.AS.setJamBullets(0, 4);
            }
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        try {
            this.bombHookName = "_ExternalBomb01";
            this.bombGun = (BombGun) this.getBulletEmitterByHookName(this.bombHookName);
        } catch (Exception exception) {
            this.bombGun = null;
        }
    }

    boolean         isReleased;
    boolean         isExtendedStart;
    boolean         isExtended;
    float           tickConstLenFs;
    float           armAngle;
    float           oldArmAngle;
    BombGun         bombGun;
    String          bombHookName;
    public int      diveMechStage;
    public boolean  bNDives;
    private boolean bDropsBombs;
}
