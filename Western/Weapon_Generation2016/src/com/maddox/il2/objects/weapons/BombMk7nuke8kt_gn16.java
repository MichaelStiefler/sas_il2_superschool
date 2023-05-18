
package com.maddox.il2.objects.weapons;

import com.maddox.rts.Property;


public class BombMk7nuke8kt_gn16 extends Bomb
{

    public BombMk7nuke8kt_gn16()
    {
    }

    public void start()
    {
        super.start();
        extendTailfin(true);
    }

    public void extendTailfin(boolean bExtend)
    {
        if(bTailfinExtended == bExtend)
            return;

        if(bExtend)
            setMesh("3DO/Arms/USnukeMk7_gn16/mono_open.sim");
        else
            setMesh("3DO/Arms/USnukeMk7_gn16/mono.sim");

        bTailfinExtended = bExtend;
    }

    public boolean getTailfin()
    {
        return bTailfinExtended;
    }

    private boolean bTailfinExtended = false;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMk7nuke8kt_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/USnukeMk7_gn16/mono.sim");
        Property.set(class1, "radius",6000F);
        Property.set(class1, "power", 8000000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.76F);
        Property.set(class1, "massa", 762.0F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_USnukeDelay.class, com.maddox.il2.objects.weapons.Fuze_USnukeLowAlt.class, com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_USnukeAir.class
        })));
 		Property.set(class1, "dragCoefficient", 0.7F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}