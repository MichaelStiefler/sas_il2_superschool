package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;

public abstract class Ba65B extends Ba65xyz {

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 93F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 93F * f);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        float f1 = Aircraft.cvt(f, 0.0F, 0.15F, 0.0F, -0.1F);
        Aircraft.xyz[1] = f1;
        hiermesh.chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveGear(float f) {
        Ba65B.moveGear(this.hierMesh(), f);
    }

    public void doKillPilot(int i) {
        if (i == 1) {
            this.FM.turret[0].bIsOperable = false;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            case 0:
                if (f < -45F) {
                    f = -45F;
                    flag = false;
                }
                if (f > 45F) {
                    f = 45F;
                    flag = false;
                }
                if (f1 < -15F) {
                    f1 = -15F;
                    flag = false;
                }
                if (f1 > 30F) {
                    f1 = 30F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

}
