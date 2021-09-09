package com.maddox.il2.objects.air;

import com.maddox.il2.ai.War;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.objects.bridges.BridgeSegment;

public abstract class Vulteexyz extends VulteeAxyz {

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
        if (f < 10F) {
            if (af[1] < -5F) {
                af[1] = -5F;
                flag = false;
            }
        } else if (af[1] < -15F) {
            af[1] = -15F;
            flag = false;
        }
        if (af[1] > 35F) {
            af[1] = 35F;
            flag = false;
        }
        if (!flag) {
            return false;
        }
        float f1 = af[1];
        if ((f < 2.0F) && (f1 < 17F)) {
            return false;
        }
        if (f1 > -5F) {
            return true;
        }
        if (f1 > -12F) {
            f1 += 12F;
            return f > (12F + (f1 * 2.571429F));
        } else {
            f1 = -f1;
            return f > f1;
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.gunnerAiming) {
            if (this.gunnerAnimation < 1.0D) {
                this.gunnerAnimation += 0.025F;
                this.moveGunner();
            }
        } else if (this.gunnerAnimation > 0.0D) {
            this.gunnerAnimation -= 0.025F;
            this.moveGunner();
        }
    }

    private void moveGunner() {
        if (this.gunnerDead || this.gunnerEjected) {
            return;
        }
        if (this.gunnerAnimation > 0.5D) {
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkSetAngles("Pilot2_D0", ((this.gunnerAnimation - 0.5F) * 360F) - 180F, 0.0F, 0.0F);
        } else if (this.gunnerAnimation > 0.25D) {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = (this.gunnerAnimation - 0.5F) * 0.5F;
            Aircraft.ypr[0] = 180F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            this.hierMesh().chunkSetLocate("Pilot2_D0", Aircraft.xyz, Aircraft.ypr);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
        } else {
            Aircraft.xyz[0] = 0.0F;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = this.gunnerAnimation * 0.5F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = this.gunnerAnimation * -110F;
            this.hierMesh().chunkSetLocate("Pilot3_D0", Aircraft.xyz, Aircraft.ypr);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", true);
        }
    }

    public void rareAction(float f, boolean flag) {
        Actor actor = War.GetNearestEnemy(this, 16, 7000F);
        Aircraft aircraft = War.getNearestEnemy(this, 6000F);
        boolean flag1 = ((this.FM.CT.Weapons[10] != null) && this.FM.CT.Weapons[10][0].haveBullets()) || ((this.FM.CT.Weapons[10] != null) && this.FM.CT.Weapons[10][1].haveBullets());
        if (!flag1 || this.gunnerDead) {
            this.FM.turret[0].bIsOperable = false;
        }
        if (flag1 && (((actor != null) && !(actor instanceof BridgeSegment)) || (aircraft != null))) {
            if (!this.gunnerAiming) {
                this.gunnerAiming = true;
            }
        } else if (this.gunnerAiming) {
            this.gunnerAiming = false;
        }
        super.rareAction(f, flag);
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
            if (this.pk >= 1) {
                this.pk = 1;
            }
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("Prop2_d0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("PropRot2_d0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("Prop2_d0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        if (i == 2) {
            super.doRemoveBodyFromPlane(3);
            this.gunnerEjected = true;
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                if (f <= 0.0F) {
                    this.gunnerDead = true;
                }
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D1", this.hierMesh().isChunkVisible("Pilot2_D0"));
                this.hierMesh().chunkVisible("Pilot3_D1", this.hierMesh().isChunkVisible("Pilot3_D0"));
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.gunnerDead = true;
                break;
        }
    }
}
