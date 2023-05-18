
package com.maddox.il2.objects.weapons;

import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            FuelTank

public class FuelTank_TankF100_335gal_gn16 extends FuelTank
{

    public FuelTank_TankF100_335gal_gn16()
    {
        if(Config.isUSE_RENDER() && (World.cur().camouflage == 1 || World.cur().camouflage == 4 || World.cur().camouflage == 5))
        {
            setMesh(Property.stringValue(getClass(), "mesh"));
            mesh.materialReplace("335Gal", "335GalSilver");
            mesh.materialReplace("335GalP", "335GalSilverP");
        } else
        if(Config.isUSE_RENDER() && World.Rnd().nextInt(0, 10) < 5)
        {
            setMesh(Property.stringValue(getClass(), "mesh"));
            mesh.materialReplace("335Gal", "335GalAlt");
            mesh.materialReplace("335GalP", "335GalAltP");
        }
    }

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.FuelTank_TankF100_335gal_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/TankF100_335gal_gn16/mono.sim");
        Property.set(class1, "kalibr", 0.9F);
        Property.set(class1, "massa", 1000F);
		Property.set(class1, "dragCoefficient", 0.35F); // Aerodynamic Drag Coefficient, Stock WWII droptanks=1.0F
    }
}
