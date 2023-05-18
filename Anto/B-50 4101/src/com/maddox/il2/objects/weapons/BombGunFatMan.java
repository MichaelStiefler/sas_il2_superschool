package com.maddox.il2.objects.weapons;

public class BombGunFatMan extends com.maddox.il2.objects.weapons.BombGun
{

    public BombGunFatMan()
    {
    }

    static
    {
        java.lang.Class class1 = com.maddox.il2.objects.weapons.BombGunFatMan.class;
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "bulletClass", ((java.lang.Object) (((java.lang.Object) (com.maddox.il2.objects.weapons.BombFatMan.class)))));
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "bullets", 1);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "shotFreq", 0.05F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "external", 1);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "sound", "weapon.bombgun");
    }
}