// US Air Force LAU-88 pylon -- launcher rail for triple AGM-65 Marverick missiles. Used by A-10, F-16, F-15E
// US Navy or Marine Corps Aircrafts never use this launcher.
// Even USAF stops to use this launcher later, because of low reliability problem and high drag.

/*
* Base color is high visibility light cream for early 1980s.

* When you want low visibility dark gray for later, add this code to mother Jets.

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
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU88_gn16)
                        ((Pylon_LAU88_gn16)FM.CT.Weapons[i][j]).matLowvis();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU88_gn16 extends Pylon
{

    public Pylon_LAU88_gn16()
    {
    }

    public void matLowvis()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance88", "Ordnance88LV");
        mesh.materialReplace("Ordnance88p", "Ordnance88LVp");
        mesh.materialReplace("Ordnance88q", "Ordnance88LVq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU88_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU88_gn16/mono.sim");
        Property.set(class1, "massa", 212.8F);
        Property.set(class1, "dragCx", 0.036F);  // stock Pylons is +0.035F
    }
}