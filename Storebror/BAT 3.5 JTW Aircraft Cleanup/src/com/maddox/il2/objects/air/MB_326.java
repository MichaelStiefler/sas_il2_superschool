package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.rts.Property;

public class MB_326 extends MB_326X
{

    public MB_326()
    {
    }

    public void update(float f)
    {
        super.update(f);
        if(Config.isUSE_RENDER() && this.FM.AS.isMaster())
            if(this.FM.EI.engines[0].getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6)
            {
                if(this.FM.EI.engines[0].getPowerOutput() > 0.95F)
                    this.FM.AS.setSootState(this, 0, 3);
                else
                    this.FM.AS.setSootState(this, 0, 2);
            } else
            {
                this.FM.AS.setSootState(this, 0, 0);
            }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;
        }
    }

    private Eff3DActor turbo;
    private Eff3DActor turbosmoke;
    private Eff3DActor afterburner;

    static 
    {
        Class class1 = MB_326.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MB-326");
        Property.set(class1, "meshName", "3DO/Plane/MB-326/326hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/MB-326.fmd:MB326FM");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitMB_326.class, CockpitT_33i.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 
            3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 
            2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 
            3, 3, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", 
            "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", 
            "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock06", "_ExternalRock06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", 
            "_ExternalBomb11", "_ExternalBomb12", "_ExternalDev11", "_ExternalDev12"
        });
    }
}
