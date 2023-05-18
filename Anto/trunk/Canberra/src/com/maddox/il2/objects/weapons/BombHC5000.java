package com.maddox.il2.objects.weapons;

public class BombHC5000 extends com.maddox.il2.objects.weapons.Bomb
{

    public BombHC5000()
    {
        if(com.maddox.il2.engine.Config.isUSE_RENDER() && com.maddox.il2.ai.World.Rnd().nextInt(0, 99) < 20)
        {
            setMesh(com.maddox.rts.Property.stringValue(((java.lang.Object)this).getClass(), "mesh"));
            mesh.materialReplace("Ordnance1", "alhambra" + com.maddox.il2.ai.World.Rnd().nextInt(1, 1));
        }
    }

    static
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.BombHC5000.class;
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "mesh", "3DO/Arms/hc5000/hc5000.sim");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "radius", 1300F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "power", 3200F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "powerType", 0);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "kalibr", 1.0F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "massa", 2267F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "sound", "weapon.bomb_big");
        /*
        com.maddox.rts.Property.set(class1, "fuze", ((Object) (new Object[] {
                com.maddox.il2.objects.weapons.Fuze_PistolNo44.class, com.maddox.il2.objects.weapons.Fuze_PistolNo54.class, com.maddox.il2.objects.weapons.Fuze_PistolNo28.class
            })));
            */
    }
}