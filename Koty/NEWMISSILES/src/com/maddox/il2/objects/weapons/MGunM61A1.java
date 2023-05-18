/*    */ package com.maddox.il2.objects.weapons;
/*    */ 
/*    */ import com.maddox.JGP.Color3f;
/*    */ import com.maddox.il2.engine.BulletProperties;
/*    */ import com.maddox.il2.engine.GunProperties;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MGunM61A1
/*    */   extends MGunAircraftGeneric
/*    */ {
/*    */   public MGunM61A1() {}
/*    */   
/*    */   public GunProperties createProperties()
/*    */   {
/* 24 */     GunProperties localGunProperties = super.createProperties();
/* 25 */     localGunProperties.bCannon = false;
/* 26 */     localGunProperties.bUseHookAsRel = true;
/* 27 */     localGunProperties.fireMesh = "3DO/Effects/GunFire/20mm/mono.sim";
/* 28 */     localGunProperties.fire = null;
/* 29 */     localGunProperties.sprite = "3DO/Effects/GunFire/20mm/GunFlare.eff";
/* 30 */     localGunProperties.smoke = "effects/smokes/MachineGun.eff";
/* 31 */     localGunProperties.shells = null;
/* 32 */     localGunProperties.sound = "weapon.Gau4";
/* 33 */     localGunProperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
/* 34 */     localGunProperties.emitI = 10.0F;
/* 35 */     localGunProperties.emitR = 3.0F;
/* 36 */     localGunProperties.emitTime = 0.03F;
/* 37 */     localGunProperties.aimMinDist = 5.0F;
/* 38 */     localGunProperties.aimMaxDist = 1500.0F;
/* 39 */     localGunProperties.weaponType = 3;
/* 40 */     localGunProperties.maxDeltaAngle = 0.3F;
/* 41 */     localGunProperties.shotFreq = 100.0F;
/* 42 */     localGunProperties.traceFreq = 1;
/* 43 */     localGunProperties.bullets = 250;
/* 44 */     localGunProperties.bulletsCluster = 1;
/* 45 */     localGunProperties.bullet = new BulletProperties[] { new BulletProperties(), new BulletProperties(),  new BulletProperties(), new BulletProperties()  };
/*    */     
/*    */ 
/* 48 */     localGunProperties.bullet[0].massa = 0.102F;
/* 49 */     localGunProperties.bullet[0].kalibr = 3.2E-4F;
/* 50 */     localGunProperties.bullet[0].speed = 1054.0F;
/* 51 */     localGunProperties.bullet[0].power = 0.0208F;
/* 52 */     localGunProperties.bullet[0].powerType = 2;
/* 53 */     localGunProperties.bullet[0].powerRadius = 0.34F;
/* 54 */     localGunProperties.bullet[0].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
/* 55 */     localGunProperties.bullet[0].traceColor = -117440257;
/* 56 */     localGunProperties.bullet[0].timeLife = 6.0F;


/* 57 */     localGunProperties.bullet[1].massa = 0.102F;
/* 58 */     localGunProperties.bullet[1].kalibr = 3.2E-4F;
/* 59 */     localGunProperties.bullet[1].speed = 1030.0F;
/* 60 */     localGunProperties.bullet[1].power = 0.0208F;
/* 61 */     localGunProperties.bullet[1].powerType = 1;
/* 62 */     localGunProperties.bullet[1].powerRadius = 0.34F;
/* 54 */     localGunProperties.bullet[1].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
/* 64 */     localGunProperties.bullet[1].traceTrail = null;
/* 65 */     localGunProperties.bullet[1].traceColor = -117440257;
/* 66 */     localGunProperties.bullet[1].timeLife = 8.0F;


/* 57 */     localGunProperties.bullet[2].massa = 0.102F;
/* 58 */     localGunProperties.bullet[2].kalibr = 3.2E-4F;
/* 59 */     localGunProperties.bullet[2].speed = 1030.0F;
/* 60 */     localGunProperties.bullet[2].power = 0.0208F;
/* 61 */     localGunProperties.bullet[2].powerType = 1;
/* 62 */     localGunProperties.bullet[2].powerRadius = 0.34F;
/* 54 */     localGunProperties.bullet[2].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
/* 64 */     localGunProperties.bullet[2].traceTrail = null;
/* 65 */     localGunProperties.bullet[2].traceColor = -117440257;
/* 66 */     localGunProperties.bullet[2].timeLife = 8.0F;


/* 57 */     localGunProperties.bullet[3].massa = 0.102F;
/* 58 */     localGunProperties.bullet[3].kalibr = 3.2E-4F;
/* 59 */     localGunProperties.bullet[3].speed = 1030.0F;
/* 60 */     localGunProperties.bullet[3].power = 0.0208F;
/* 61 */     localGunProperties.bullet[3].powerType = 1;
/* 62 */     localGunProperties.bullet[3].powerRadius = 0.34F;
/* 54 */     localGunProperties.bullet[3].traceMesh = "3do/effects/tracers/7mmYellow/mono.sim";
/* 64 */     localGunProperties.bullet[3].traceTrail = null;
/* 65 */     localGunProperties.bullet[3].traceColor = -117440257;
/* 66 */     localGunProperties.bullet[3].timeLife = 8.0F;
/* 67 */     return localGunProperties;
/*    */   }
/*    */ }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\WeaponMod
 * Qualified Name:     com.maddox.il2.objects.weapons.MGunM61
 * Java Class Version: 1.3 (47.0)
 * JD-Core Version:    0.7.0.1
 */