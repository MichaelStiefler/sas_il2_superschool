package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;

public abstract class NC223_4xyz extends NC223_xyz {

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = Math.max(-f * 800F, -80F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -f1 * NC223_xyz.kl, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1 * NC223_xyz.kl, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1 * NC223_xyz.kr, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1 * NC223_xyz.kr, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, -70F * f * NC223_xyz.kl);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, -70F * f * NC223_xyz.kr);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 130F * f * NC223_xyz.kl);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 130F * f * NC223_xyz.kr);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, -110F * f * NC223_xyz.kl);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, -110F * f * NC223_xyz.kr);
    }

    protected void moveGear(float f) {
        NC223_4xyz.moveGear(this.hierMesh(), f);
    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("BayL_D0", 0.0F, 92F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BayR_D0", 0.0F, -92F * f, 0.0F);
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 4:
                this.FM.turret[1].bIsOperable = false;
                break;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f1 < -3F) {
                    f1 = -3F;
                    flag = false;
                }
                if (f1 > 50F) {
                    f1 = 50F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

}
