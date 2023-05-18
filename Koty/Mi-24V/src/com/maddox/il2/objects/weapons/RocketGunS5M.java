 package com.maddox.il2.objects.weapons;
 
 import com.maddox.rts.Property;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class RocketGunS5M
   extends RocketGun
 {
   public void setConvDistance(float f, float f1) { super.setConvDistance(f, f1 + 2.81F); }
   
   public RocketGunS5M() {}
   
   static {
     Class class1 = RocketGunS5M.class;
     Property.set(class1, "bulletClass", (Object)RocketS5M.class);
     Property.set(class1, "bullets", 1);
     Property.set(class1, "shotFreq", 8F);
     Property.set(class1, "sound", "weapon.rocketgun_132");
     Property.set(class1, "maxDeltaAngle", 0.40F);
   }
 }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\WeaponMod
 * Qualified Name:     com.maddox.il2.objects.weapons.RocketGunS5M
 * Java Class Version: 1.1 (45.3)
 * JD-Core Version:    0.7.0.1
 */