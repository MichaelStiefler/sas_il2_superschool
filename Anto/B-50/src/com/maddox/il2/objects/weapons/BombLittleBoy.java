package com.maddox.il2.objects.weapons;

public class BombLittleBoy extends com.maddox.il2.objects.weapons.Bomb
{

    public BombLittleBoy()
    {
        if(com.maddox.il2.engine.Config.isUSE_RENDER() && com.maddox.il2.ai.World.Rnd().nextInt(0, 99) < 20)
        {
            setMesh(com.maddox.rts.Property.stringValue(((java.lang.Object)this).getClass(), "mesh"));
            mesh.materialReplace("Ordnance1", "alhambra" + com.maddox.il2.ai.World.Rnd().nextInt(1, 1));
        }
    }

    static
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.BombLittleBoy.class;
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "mesh", "3DO/Arms/LittleBoy/mono.sim");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "radius", 3200F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "power", 8000000F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "powerType", 0);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "kalibr", 1.0F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "massa", 4000F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "sound", "weapon.bomb_big");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "newEffect", 1);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "nuke", 1);
    }
}