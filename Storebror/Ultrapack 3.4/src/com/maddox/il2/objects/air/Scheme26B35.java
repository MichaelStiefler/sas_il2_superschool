package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;

public abstract class Scheme26B35 extends Scheme2 {
    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 17:
                super.cutFM(11, j, actor);
                // fall through

            case 18:
                super.cutFM(12, j, actor);
                // fall through

            default:
                return super.cutFM(i, j, actor);
        }
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }
}
