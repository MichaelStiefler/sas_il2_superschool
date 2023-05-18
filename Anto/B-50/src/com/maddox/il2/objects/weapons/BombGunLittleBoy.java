package com.maddox.il2.objects.weapons;

public class BombGunLittleBoy extends com.maddox.il2.objects.weapons.BombGun
{

    public BombGunLittleBoy()
    {
    }

    static
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.BombGunLittleBoy.class;
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "bulletClass", ((java.lang.Object) (((java.lang.Object) (com.maddox.il2.objects.weapons.BombLittleBoy.class)))));
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "bullets", 1);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "shotFreq", 0.05F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "external", 1);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "sound", "weapon.bombgun");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "newEffect", 1);
    }
}