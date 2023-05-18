// 150 gal. Fueltank for F-5A Freedom Fighter with 3x tailfin.

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


public class FuelTank_TankF5A150galT3_gn16 extends FuelTank
    implements SkinnableOrdnance
{

    public FuelTank_TankF5A150galT3_gn16()
    {
    }

    public void matPlane(Mat mat, Mat matp)
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Gloss1D0o", mat);
        mesh.materialReplace("Gloss1D0p", matp);
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankF5A150galT3_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankF5A150gal_gn16/monot3.sim");
        Property.set(class1, "kalibr", 0.53F);
        Property.set(class1, "massa", 550.4F);
		Property.set(class1, "dragCoefficient", 0.42F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}