// US Navy, Marine Corps Mk4 HIPEG 20mm gun pod, used by A-4 Skyhawk or A-7 CorsairII.


/*
* Base color is Low-Visible Gray color for 1990s or later.

* When you want High-Visible White for 1980s or earlier, add this code to mother Jets.

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
                    if(FM.CT.Weapons[i][j] instanceof Pylon_Mk4HIPEGpod_gn16)
                        ((Pylon_Mk4HIPEGpod_gn16)FM.CT.Weapons[i][j]).matHighvis();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_Mk4HIPEGpod_gn16 extends Pylon
{

    public Pylon_Mk4HIPEGpod_gn16()
    {
    }

    public void matHighvis()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1HV");
        mesh.materialReplace("Ordnance1p", "Ordnance1HVp");
        mesh.materialReplace("Ordnance1q", "Ordnance1HVq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_Mk4HIPEGpod_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_Mk4HIPEGpod_gn16/mono.sim");
        Property.set(class1, "massa", 359F);
        Property.set(class1, "dragCx", 0.039F);  // stock Pylons is +0.035F
    }
}