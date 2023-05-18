// 150 gal. Fueltank gun for A-1 Skyraider and OV-10 Bronco with NO tailfin

/*
* Base color is high visibility white gray for carrier base units.

* When you want SEA camouflage dark green + gray, add this code to mother Jets.

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16)
                        ((FuelTankGun_TankSkyhawk_gn16)FM.CT.Weapons[i][j]).matSEAcamo();
                }
            }
    }

*/

package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTankGun_TankSkyraider150galNF_gn16 extends FuelTankGun
{

    public FuelTankGun_TankSkyraider150galNF_gn16()
    {
    }

    public void matSEAcamo()
    {
        ((FuelTank_TankSkyraider150galNF_gn16) bomb).matSEAcamo();
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_TankSkyraider150galNF_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_TankSkyraider150galNF_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}