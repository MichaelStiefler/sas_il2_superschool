// US LAU-131 , launcher for 7x 2.75inch rocket -- Mk4 FFAR and HYDRA70
// same to LAU-32 , LAU-68

/*
* Base color is gray for Navy Jets and Air Force's Intercepter.

* When you want green color for Air Force's bomber or brown color for FAC, add these codes.

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
                  // Green case
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_gn16)
                        ((Pylon_LAU131_gn16)FM.CT.Weapons[i][j]).matGreen();
                  // Brown case
                    if(FM.CT.Weapons[i][j] instanceof Pylon_LAU131_gn16)
                        ((Pylon_LAU131_gn16)FM.CT.Weapons[i][j]).matBrown();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_LAU131_gn16 extends Pylon
{

    public Pylon_LAU131_gn16()
    {
    }

    public void matGreen()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance2", "Ordnance2green");
        mesh.materialReplace("Ordnance2p", "Ordnance2greenp");
        mesh.materialReplace("Ordnance2q", "Ordnance2greenq");
    }

    public void matBrown()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance2", "Ordnance2brown");
        mesh.materialReplace("Ordnance2p", "Ordnance2brownp");
        mesh.materialReplace("Ordnance2q", "Ordnance2brownq");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_LAU131_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_LAU131_gn16/mono.sim");
        Property.set(class1, "massa", 29.5F);
        Property.set(class1, "dragCx", 0.025F);  // stock Pylons is +0.035F
    }
}