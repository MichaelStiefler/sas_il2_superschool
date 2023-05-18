// US LAU-7 pylon -- launcher rail for AIM-9 Sidewinder missiles.

/*
* Base color is low visibility dark gray for 1980s and later.

* When you want high visibility cream for 1960-1970s, add this code to mother Jets.

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
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU7_gn16)
                        ((Pylon_LAU7_gn16)FM.CT.Weapons[i][j]).matHighvis();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU7_gn16 extends Pylon
{

    public Pylon_LAU7_gn16()
    {
    }

    public void matHighvis()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1HV");
        mesh.materialReplace("Ordnance1p", "Ordnance1HVp");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU7_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU7_gn16/mono.sim");
        Property.set(class1, "massa", 40.8F);
        Property.set(class1, "dragCx", 0.003F);  // stock Pylons is +0.035F
    }
}