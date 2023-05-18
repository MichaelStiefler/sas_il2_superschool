// US Marine Corps AN/ALQ-164 "DECM" ECM Pod , carried by AV-8B(+)


/*
* Base color is yellow brown.

* When you want gray color , add this code to mother Jets.

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
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_AN_ALQ164_gn16 extends Pylon
{

    public Pylon_AN_ALQ164_gn16()
    {
    }

    public void matGray()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1_gray");
        mesh.materialReplace("Ordnance1p", "Ordnance1_grayP");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_AN_ALQ164_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/AN_ALQ164_gn16/mono.sim");
        Property.set(class1, "massa", 158.5F);
        Property.set(class1, "dragCx", 0.022F);  // stock Pylons is +0.035F
    }
}