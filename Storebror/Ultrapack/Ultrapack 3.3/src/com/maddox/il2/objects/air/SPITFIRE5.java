package com.maddox.il2.objects.air;

import com.maddox.il2.fm.RealFlightModel;

public class SPITFIRE5 extends SPITFIRE {

    public SPITFIRE5() {
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.isPlayers()) {
            RealFlightModel realflightmodel = (RealFlightModel) this.FM;
            if (realflightmodel.RealMode) this.FM.producedAM.z -= 25F * this.FM.EI.engines[0].getControlRadiator() * realflightmodel.indSpeed;
        }
    }
}
