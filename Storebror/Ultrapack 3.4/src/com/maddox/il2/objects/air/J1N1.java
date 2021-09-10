package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.bridges.BridgeSegment;

public abstract class J1N1 extends J1Nxyz implements TypeFighter, TypeBNZFighter, TypeStormovik, TypeJazzPlayer {

    public J1N1() {
        this.turretUp = false;
        this.turretMove = 0.0F;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) {
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("HMask2_D0", false);
        } else {
            this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
            this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_D0"));
        }
        Actor actor = War.GetNearestEnemy(this, 16, 7000F);
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        boolean flag1 = (this.FM.CT.Weapons[10] != null) && this.FM.CT.Weapons[10][0].haveBullets();
        if (!flag1) {
            this.FM.turret[0].bIsOperable = false;
        }
        if (flag1 && (((actor != null) && !(actor instanceof BridgeSegment)) || (aircraft != null))) {
            if (!this.turretUp) {
                this.turretUp = true;
            }
        } else if (this.turretUp) {
            this.turretUp = false;
        }
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 30F * f, 0.0F);
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -25F) {
            af[0] = -25F;
            flag = false;
        } else if (af[0] > 25F) {
            af[0] = 25F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (af[1] < -10F) {
            af[1] = -10F;
            flag = false;
        }
        if (af[1] > 45F) {
            af[1] = 45F;
            flag = false;
        }
        if (!flag) {
            return false;
        }
        float f1 = af[1];
        if ((f < 1.2F) && (f1 < 13.3F)) {
            return false;
        }
        return (f1 >= -3.1F) || (f1 <= -4.6F);
    }

    public void update(float f) {
        if (!this.gunnerDead && !this.gunnerEjected) {
            if (this.turretUp) {
                if (this.turretMove < 1.0D) {
                    this.turretMove += 0.025F;
                }
            } else if (this.turretMove > 0.0D) {
                this.turretMove -= 0.025F;
            }
            this.hierMesh().chunkSetAngles("TurretMain_D0", 0.0F, this.turretMove * 10F, 0.0F);
            this.hierMesh().chunkSetAngles("TurretTilt2_D0", this.turretMove * 10F, 0.0F, 0.0F);
        }
        super.update(f);
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        if (i == 2) {
            super.doRemoveBodyFromPlane(3);
            this.gunnerEjected = true;
        }
    }

    public boolean hasCourseWeaponBullets() {
        return (this.FM.CT.Weapons[0] != null) && (this.FM.CT.Weapons[0][0] != null) && (this.FM.CT.Weapons[0][0].countBullets() != 0);
    }

    public boolean hasSlantedWeaponBullets() {
        return ((this.FM.CT.Weapons[1] != null) && (this.FM.CT.Weapons[1][0] != null) && (this.FM.CT.Weapons[1][1] != null) && (this.FM.CT.Weapons[1][0].countBullets() != 0)) || (this.FM.CT.Weapons[1][1].countBullets() != 0);
    }

    public Vector3d getAttackVector() {
        return J1N1.ATTACK_VECTOR;
    }

    private static final Vector3d ATTACK_VECTOR = new Vector3d(-190D, 0.0D, -300D);

    private boolean               turretUp;
    private float                 turretMove;
}
