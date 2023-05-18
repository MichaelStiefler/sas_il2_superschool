
package com.maddox.il2.objects.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;
import com.maddox.sound.*;
import com.maddox.util.HashMapExt;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;


public class Mi24V extends Mi24xyz
{

    public Mi24V()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public void update(float f)
    {
        super.update(f);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        if(af[0] < -60F)
        {
            af[0] = -60F;
            flag = false;
        } else
        if(af[0] > 60F)
        {
            af[0] = 60F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if(af[1] < -80F)
        {
            af[1] = -80F;
            flag = false;
        }
        if(af[1] > 20F)
        {
            af[1] = 20F;
            flag = false;
        }
        if(!flag)
            return false;
        float f1 = af[1];
        if(f < 1.2F && f1 < 13.3F)
            return false;
        else
            return f1 >= -3.1F || f1 <= -4.6F;
    }

    static Class _mthclass$(String x0)
    {
        try
        {
            return Class.forName(x0);
        }
        catch(ClassNotFoundException x1)
        {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static
    {
        Class class1 = com.maddox.il2.objects.air.Mi24V.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mi-24V");
        Property.set(class1, "meshName", "3DO/Plane/Mi-24V/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1976F);
        Property.set(class1, "yearExpired", 2010F);
        Property.set(class1, "FlightModel", "FlightModels/Mi-24V.fmd:HIND");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitMi24.class, com.maddox.il2.objects.air.CockpitMi24_TGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 3, 7, 7, 1, 1, 9, 9, 9, 9,
             9, 9, 9, 9, 3, 3, 3, 3, 3, 3,
             2, 2, 2, 2, 5, 5, 5, 5, 5, 5,
             5, 5, 4, 4, 4, 4, 4, 4, 4, 4,
             4, 4, 4, 4, 4, 4, 4, 4, 2, 2,
             2, 2, 3, 3, 3, 3, 3, 3, 3, 3,
             1, 1, 1, 1, 0, 0, 0, 0, 0, 0,
             0, 0
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01",          "_BombSpawn01",     "_Flare01",         "_Flare02",         "_CANNON01",        "_CANNON02",        "_ExternalDev01",   "_ExternalDev02",   "_ExternalDev03",   "_ExternalDev04",
            "_ExternalDev05",   "_ExternalDev06",   "_ExternalDev07",   "_ExternalDev08",   "_ExternalBomb01",  "_ExternalBomb02",  "_ExternalBomb03",  "_ExternalBomb04",  "_Bomblet01",       "_Bomblet02",
            "_Rock01",          "_Rock02",          "_Rock03",          "_Rock04",          "_ExternalRock05",  "_ExternalRock05",  "_ExternalRock06",  "_ExternalRock06",  "_ExternalRock07",  "_ExternalRock07",
            "_ExternalRock08",  "_ExternalRock08",  "_Sturm01",         "_Sturm01",         "_Sturm02",         "_Sturm02",         "_Sturm03",         "_Sturm03",         "_Sturm04",         "_Sturm04",
            "_Sturm05",         "_Sturm05",         "_Sturm06",         "_Sturm06",         "_Sturm07",         "_Sturm07",         "_Sturm08",         "_Sturm08",         "_ExternalRock09",  "_ExternalRock10",
            "_ExternalRock11",  "_ExternalRock12",  "_ExternalBomb05",  "_ExternalBomb06",  "_ExternalBomb07",  "_ExternalBomb08",  "_ExternalBomb09",  "_ExternalBomb10",  "_ExternalBomb11",  "_ExternalBomb12",
            "_CANNON03",        "_CANNON04",        "_CANNON05",        "_CANNON06",        "_MGUN11",          "_MGUN12",          "_MGUN13",          "_MGUN14",          "_MGUN15",          "_MGUN16",
            "_MGUN17",          "_MGUN18"
        });
    }
}