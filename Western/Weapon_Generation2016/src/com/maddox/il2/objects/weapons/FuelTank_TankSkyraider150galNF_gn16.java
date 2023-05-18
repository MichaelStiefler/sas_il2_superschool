// 150 gal. Fueltank for A-1 Skyraider and OV-10 Bronco with NO tailfin

/*
* Base color is high visibility white gray for carrier base units.

* When you want SEA camouflage dark green + gray, add this code to mother Jets.

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
                    if(FM.CT.Weapons[i][j] instanceof FuelTankGun_TankSkyhawk_gn16)
                        ((FuelTankGun_TankSkyhawk_gn16)FM.CT.Weapons[i][j]).matSEAcamo();
                }
            }
    }

*/


package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class FuelTank_TankSkyraider150galNF_gn16 extends FuelTank
{

    public FuelTank_TankSkyraider150galNF_gn16()
    {
    }

    public void matSEAcamo()
    {
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Tank_GlossHV", "Tank_GlossSEA");
        mesh.materialReplace("Tank_GlossHVP", "Tank_GlossSEAP");
        mesh.materialReplace("Tank_GlossHVQ", "Tank_GlossSEAQ");
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankSkyraider150galNF_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankSkyraider150gal_gn16/mononf.sim");
        Property.set(class1, "kalibr", 0.56F);
        Property.set(class1, "massa", 570F);
		Property.set(class1, "dragCoefficient", 0.28F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}