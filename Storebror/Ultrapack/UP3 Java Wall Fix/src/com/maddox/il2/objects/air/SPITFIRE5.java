package com.maddox.il2.objects.air;

import com.maddox.il2.fm.RealFlightModel;

public class SPITFIRE5 extends SPITFIRE
{

    public SPITFIRE5()
    {
    }

    public void update(float f)
    {
        super.update(f);
        if(FM.isPlayers())
        {
            RealFlightModel realflightmodel = (RealFlightModel)FM;
            if(realflightmodel.RealMode)
                FM.producedAM.z -= 25F * FM.EI.engines[0].getControlRadiator() * realflightmodel.indSpeed;
        }
    }
}
