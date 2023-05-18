package com.maddox.il2.objects.air;

public abstract class SPITFIRE9 extends SPITFIRE {

    public SPITFIRE9() {
        this.kangle = 0.0F;
    }

    public void update(float f) {
        super.update(f);
        this.hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * this.kangle, 0.0F);
        this.hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * this.kangle, 0.0F);
        this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        if (this.kangle > 1.0F) this.kangle = 1.0F;
    }

    private float kangle;
}
