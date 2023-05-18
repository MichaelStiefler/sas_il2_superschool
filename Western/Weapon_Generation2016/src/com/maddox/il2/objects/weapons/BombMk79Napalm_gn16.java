// 1000lbs. Mk79 Napalm fire bomb of US Navy and Marine Corps for carrier based atacker A-1, A-4, A-6, F-4

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
                    if(FM.CT.Weapons[i][j] instanceof BombGunMk79Napalm_gn16)
                        ((BombGunMk79Napalm_gn16)FM.CT.Weapons[i][j]).matRed();
                }
            }
    }

*/


package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class BombMk79Napalm_gn16 extends BombNapalmGeneric_gn16
{

    public BombMk79Napalm_gn16()
    {
        napalmFilledKg = 310F;
    }

    public void matRed()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance79", "Ordnance79red");
        mesh.materialReplace("Ordnance79P", "Ordnance79redP");
        mesh.materialReplace("Ordnance79Q", "Ordnance79redQ");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk79Napalm_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Mk79_US_napalm_gn16/mono.sim");
        Property.set(class1, "radius", 114.6805F);
        Property.set(class1, "power", 317.5F);
        Property.set(class1, "powerType", 2);
        Property.set(class1, "kalibr", 0.50F);
        Property.set(class1, "massa", 453.6F);
        Property.set(class1, "sound", "weapon.bomb_std");
 		Property.set(class1, "dragCoefficient", 0.31F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
