/*    */ package com.maddox.il2.objects.weapons;
/*    */ 
/*    */ import com.maddox.rts.Property;
/*    */ 
/*    */ public class BombFAB100 extends Bomb
/*    */ {
/*    */   public BombFAB100() {}
/*    */   
/*    */   static {
/* 10 */     Class class1 = com.maddox.il2.objects.weapons.BombFAB100.class;
Property.set(class1, "mesh", "3do/arms/fab-250/mono.sim");
Property.set(class1, "power", 1200F);
Property.set(class1, "powerType", 0);
Property.set(class1, "kalibr", 0.408F);
Property.set(class1, "massa", 250F);
Property.set(class1, "sound", "weapon.bomb_std");
Property.set(class1, "fuze", ((Object) (new Object[] {
    com.maddox.il2.objects.weapons.Fuze_APUV.class, com.maddox.il2.objects.weapons.Fuze_APUV_M.class, com.maddox.il2.objects.weapons.Fuze_APUV_1.class, com.maddox.il2.objects.weapons.Fuze_AV_1du.class, com.maddox.il2.objects.weapons.Fuze_AV_1.class, com.maddox.il2.objects.weapons.Fuze_AV_87.class
})));
/*    */   }
/*    */ }

/* Location:           C:\Users\Виктор1\Downloads\ClassDump_4.12.2.jar
 * Qualified Name:     com.maddox.il2.objects.weapons.BombFAB100
 * Java Class Version: 1.3 (47.0)
 * JD-Core Version:    0.7.0.1
 */