// 1000lbs. Mk79 Napalm fire bomb ('s Bomgun) of US Navy and Marine Corps for carrier based atacker A-1, A-4, A-6, F-4

/*
* Base color is gray.

* When you want red color for A-1 Skyraider, add this code to mother Jets.

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
                    if(FM.CT.Weapons[i][j] instanceof BombGunMk79Napalm)
                        ((BombGunMk79Napalm_gn16)FM.CT.Weapons[i][j]).matRed();
                }
            }
    }

*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombGunMk79Napalm_gn16 extends BombGun
{

    public BombGunMk79Napalm_gn16()
    {
    }

    public void matRed()
    {
        ((BombMk79Napalm_gn16) bomb).matRed();
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombGunMk79Napalm_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.BombMk79Napalm_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
        Property.set(class1, "sound", "weapon.bombgun");
    }
}
