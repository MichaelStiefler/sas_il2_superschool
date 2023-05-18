// US Navy / Marine Corps AN/ALQ-167 ECM Pod , carried by A-6E, EA-6A, EA-6B, F-14, F/A-18A,B,C,D
// This class is single-side (only Forward) antenna version.
// In some old times , early A-6E and F-14 used it.
// For more modern Jets , dual-side (Forward and Backward) antenna version as Pylon_AN_ALQ167_gn16 is used.

/*
* Base color is sky blue for 1990s and later.

* When you want gray color for 1980s gray, add this code to mother Jets.

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
                    if(FM.CT.Weapons[i][j] instanceof Pylon_AN_ALQ167_single_gn16)
                        ((Pylon_AN_ALQ167_single_gn16)FM.CT.Weapons[i][j]).matGray();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_AN_ALQ167_single_gn16 extends Pylon
{

    public Pylon_AN_ALQ167_single_gn16()
    {
    }

    public void matGray()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1_gray");
        mesh.materialReplace("Ordnance1p", "Ordnance1_grayP");
        mesh.materialReplace("Ordnance1q", "Ordnance1_grayQ");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_AN_ALQ167_single_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/AN_ALQ167_gn16/mono_single.sim");
        Property.set(class1, "massa", 175F);
        Property.set(class1, "dragCx", 0.017F);  // stock Pylons is +0.035F
    }
}