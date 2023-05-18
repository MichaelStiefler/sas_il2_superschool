 package com.maddox.il2.objects.weapons;
 
 import com.maddox.rts.Property;
 
 
 
 
 
 public class RocketGunS24
   extends RocketGun
 {
   public void setRocketTimeLife(float f) { this.timeLife = -1.0F; }
   
   public RocketGunS24() {}
   
   static { Class class1 = RocketGunS24.class;
     Property.set(class1, "bulletClass", (Object)RocketS24.class);
     Property.set(class1, "bullets", 1);
     Property.set(class1, "shotFreq", 4F);
     Property.set(class1, "sound", "weapon.rocketgun_132");
     Property.set(class1, "maxDeltaAngle", 0.4F);
   }
 }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\WeaponMod
 * Qualified Name:     com.maddox.il2.objects.weapons.RocketGunS24
 * Java Class Version: 1.1 (45.3)
 * JD-Core Version:    0.7.0.1
 */