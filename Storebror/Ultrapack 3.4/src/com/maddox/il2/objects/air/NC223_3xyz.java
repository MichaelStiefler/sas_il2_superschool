package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Time;

public abstract class NC223_3xyz extends NC223_xyz {

    public NC223_3xyz() {
        this.btme = -1L;
        this.bGunUp = false;
        this.fGunPos = 0.0F;
    }

    public void update(float f) {
        super.update(f);
        if (!this.bGunUp) {
            if (this.fGunPos > 0.0F) {
                this.fGunPos -= 0.2F * f;
                this.FM.turret[2].bIsOperable = false;
            }
        } else if (this.fGunPos < 1.0F) {
            this.fGunPos += 0.2F * f;
            if ((this.fGunPos > 0.8F) && (this.fGunPos < 0.9F)) {
                this.FM.turret[2].bIsOperable = true;
            }
        }
        if (this.fGunPos < 0.333F) {
            this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, -Aircraft.cvt(this.fGunPos, 0.0F, 0.333F, 0.0F, 41F), 0.0F);
        } else if (this.fGunPos < 0.666F) {
            this.resetYPRmodifier();
            Aircraft.xyz[1] = Aircraft.cvt(this.fGunPos, 0.333F, 0.666F, 0.0F, 0.7F);
            this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        }
        if (this.FM.turret[2].bIsAIControlled) {
            if ((this.FM.turret[2].target != null) && (this.FM.AS.astatePilotStates[2] < 90)) {
                this.bGunUp = true;
            }
            if (Time.current() > this.btme) {
                this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
                if ((this.FM.turret[2].target == null) && (this.FM.AS.astatePilotStates[2] < 90)) {
                    this.bGunUp = false;
                }
            }
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 800F, -80F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1 * NC223_xyz.kl, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -f1 * NC223_xyz.kl, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1 * NC223_xyz.kr, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f1 * NC223_xyz.kr, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -65F * f * NC223_xyz.kl);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -65F * f * NC223_xyz.kr);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 134F * f * NC223_xyz.kl);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 134F * f * NC223_xyz.kr);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, 30F * f * NC223_xyz.kl);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, 30F * f * NC223_xyz.kr);
    }

    protected void moveGear(float f) {
        NC223_3xyz.moveGear(this.hierMesh(), f);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, 91F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, -91F * f, 0.0F);
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 2:
                this.FM.turret[0].bIsOperable = false;
                break;

            case 3:
                this.FM.turret[1].bIsOperable = false;
                break;

            case 4:
                this.FM.turret[2].bIsOperable = false;
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f < -30F) {
                    f = -30F;
                    flag = false;
                }
                if (f > 30F) {
                    f = 30F;
                    flag = false;
                }
                if (f1 < -10F) {
                    f1 = -10F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                break;

            case 1:
                if (f1 < 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                if (f1 > 45F) {
                    f1 = 45F;
                    flag = false;
                }
                break;

            case 2:
                if (f1 < -45F) {
                    f1 = -45F;
                    flag = false;
                }
                if (f1 > 0.0F) {
                    f1 = 0.0F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    boolean      bGunUp;
    public long  btme;
    public float fGunPos;
}
