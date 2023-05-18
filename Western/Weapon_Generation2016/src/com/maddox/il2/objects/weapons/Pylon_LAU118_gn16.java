// US LAU-118 pylon -- launcher rail for AGM-45 and AGM-88 missiles.

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
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU118_gn16)
                        ((Pylon_LAU118_gn16)FM.CT.Weapons[i][j]).matHighvis();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU118_gn16 extends Pylon
{

    public Pylon_LAU118_gn16()
    {
    }

    public void matHighvis()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance2", "Ordnance2HV");
        mesh.materialReplace("Ordnance2p", "Ordnance2HVp");
        mesh.materialReplace("Ordnance2q", "Ordnance2HVq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU118_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU118_gn16/mono.sim");
        Property.set(class1, "massa", 54.4F);
        Property.set(class1, "dragCx", 0.008F);  // stock Pylons is +0.035F
    }
}