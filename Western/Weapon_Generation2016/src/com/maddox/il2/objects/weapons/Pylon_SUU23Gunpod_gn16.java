// US Navy, Air Force 20mm gun pod, used by F-4 PhantomII or A-7 CorsairII, F-5 Freedom Fighter, etc.


/*
* Base color is High-Visible White for 1960s - 1980s.

* When you want brown camo, add this code to mother Jets.

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors()
    {
        for(int i = 0; i < ((FlightModelMain) (super.FM)).CT.Weapons.length; i++)
            if(((FlightModelMain) (super.FM)).CT.Weapons[i] != null)
            {
                for(int j = 0; j < ((FlightModelMain) (super.FM)).CT.Weapons[i].length; j++)
                {
                    if(((FlightModelMain) (super.FM)).CT.Weapons[i][j] instanceof Pylon_SUU23Gunpod_gn16)
                        ((Pylon_SUU23Gunpod_gn16)((FlightModelMain) (super.FM)).CT.Weapons[i][j]).matBrown();
                }
            }
    }
*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class Pylon_SUU23Gunpod_gn16 extends Pylon
{

    public Pylon_SUU23Gunpod_gn16()
    {
    }

    public void matBrown()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1Br");
        mesh.materialReplace("Ordnance1p", "Ordnance1Brp");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.Pylon_SUU23Gunpod_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/Pylon_SUU23_Gunpod_gn16/mono.sim");
        Property.set(class1, "massa", 495F);
        Property.set(class1, "dragCx", 0.038F);  // stock Pylons is +0.035F
    }
}