package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;

public abstract class Ba65A extends Ba65xyz {

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 95F * f);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 95F * f);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 15F * f);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 15F * f);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 0.0F, -170F * f);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 0.0F, -170F * f);
    }

    protected void moveGear(float f) {
        Ba65A.moveGear(this.hierMesh(), f);
    }

}
