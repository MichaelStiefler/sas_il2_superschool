// US Air Force AN/ALQ-131 ECM Pod , carried by F-4, F-15, F-16, A-7, A-10


/*
* Base color is dark green for ground attacking on 1990s and later.

* When you want gray color for air battle fighters, add this code to mother Jets.

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
                    if(FM.CT.Weapons[i][j] instanceof Pylon_AN_ALQ131_gn16)
                        ((Pylon_AN_ALQ131_gn16)FM.CT.Weapons[i][j]).matGray();
                }
            }
    }


* And when you want white high visibility color for early time, add this code to mother Jets.

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
                    if(FM.CT.Weapons[i][j] instanceof Pylon_AN_ALQ131_gn16)
                        ((Pylon_AN_ALQ131_gn16)FM.CT.Weapons[i][j]).matHivis();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_AN_ALQ131_gn16 extends Pylon
{

    public Pylon_AN_ALQ131_gn16()
    {
    }

    public void matGray()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1_gray");
        mesh.materialReplace("Ordnance1p", "Ordnance1_grayP");
    }

    public void matHivis()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1_HV");
        mesh.materialReplace("Ordnance1p", "Ordnance1_HVP");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_AN_ALQ131_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/AN_ALQ131_gn16/mono.sim");
        Property.set(class1, "massa", 299.5F);
        Property.set(class1, "dragCx", 0.026F);  // stock Pylons is +0.035F
    }
}