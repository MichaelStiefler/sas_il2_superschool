// 150 gal. Fueltank gun for F-5E TigerII with NO tailfin.


/*
* Base color is gray.

* When you want to apply 2x different colors from the mother Jet's custom skin, add this code to the mother Jet class.

    public void missionStarting()
    {
        super.missionStarting();
        checkChangeWeaponColors();
    }

    private void checkChangeWeaponColors()
    {
        Mat mat = hierMesh().material(hierMesh().materialFind("Gloss1D0o"));
        Mat matp = hierMesh().material(hierMesh().materialFind("Gloss1D0p"));

        for(int i = 0; i < FM.CT.Weapons.length; i++)
            if(FM.CT.Weapons[i] != null)
            {
                for(int j = 0; j < FM.CT.Weapons[i].length; j++)
                {
                    if(FM.CT.Weapons[i][j] instanceof SkinnableOrdnance)
                        ((SkinnableOrdnance)FM.CT.Weapons[i][j]).matPlane(mat, matp);
                }
            }
    }

*/

package com.maddox.il2.objects.weapons;

import com.maddox.il2.engine.Mat;
import com.maddox.rts.Property;


public class FuelTankGun_TankF5E150galNF_gn16 extends FuelTankGun
    implements SkinnableOrdnance
{

    public FuelTankGun_TankF5E150galNF_gn16()
    {
    }

    public void matPlane(Mat mat, Mat matp)
    {
        ((SkinnableOrdnance) bomb).matPlane(mat, matp);
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTankGun_TankF5E150galNF_gn16.class;
        Property.set(class1, "bulletClass", (Object) com.maddox.il2.objects.weapons.FuelTank_TankF5E150galNF_gn16.class);
        Property.set(class1, "bullets", 1);
        Property.set(class1, "shotFreq", 1.0F);
        Property.set(class1, "external", 1);
    }
}