 package com.maddox.il2.objects.weapons;
 
 import com.maddox.rts.Property;

 public class RocketGunS13
   extends RocketGun
 {
   public RocketGunS13() {}
   
   static
   {
     Class localClass = RocketGunS13.class;
     Property.set(localClass, "bulletClass", (Object)RocketS13.class);
     Property.set(localClass, "bullets", 1);
     Property.set(localClass, "shotFreq", 8F);
     Property.set(localClass, "sound", "weapon.rocketgun_132");
     Property.set(localClass, "maxDeltaAngle", 0.4F);
   }
 }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\WeaponMod
 * Qualified Name:     com.maddox.il2.objects.weapons.RocketGunS13
 * Java Class Version: 1.3 (47.0)
 * JD-Core Version:    0.7.0.1
 */