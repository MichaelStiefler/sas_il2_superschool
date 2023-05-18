package com.maddox.il2.objects.air;

public abstract class SPITFIRE9 extends SPITFIRE
{

    public SPITFIRE9()
    {
        kangle = 0.0F;
    }

    public void update(float f)
    {
        super.update(f);
        hierMesh().chunkSetAngles("Oil1_D0", 0.0F, -20F * kangle, 0.0F);
        hierMesh().chunkSetAngles("Oil2_D0", 0.0F, -20F * kangle, 0.0F);
        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        if(kangle > 1.0F)
            kangle = 1.0F;
    }

    private float kangle;
}
