package com.maddox.il2.objects.weapons;

public class Bomb12000Tallboy extends com.maddox.il2.objects.weapons.Bomb
{

    public Bomb12000Tallboy()
    {
        if(com.maddox.il2.engine.Config.isUSE_RENDER() && com.maddox.il2.ai.World.Rnd().nextInt(0, 99) < 20)
        {
            ((com.maddox.il2.engine.ActorMesh)this).setMesh(com.maddox.rts.Property.stringValue(((java.lang.Object)this).getClass(), "mesh"));
            mesh.materialReplace("Ordnance1", "alhambra" + com.maddox.il2.ai.World.Rnd().nextInt(1, 1));
        }
    }

    static
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.Bomb12000Tallboy.class;
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "mesh", "3DO/Arms/Tallboy/Tallboy.sim");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "radius", 1500F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "power", 4500F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "powerType", 0);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "kalibr", 1.0F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "massa", 2500F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "sound", "weapon.bomb_big");
    }
}