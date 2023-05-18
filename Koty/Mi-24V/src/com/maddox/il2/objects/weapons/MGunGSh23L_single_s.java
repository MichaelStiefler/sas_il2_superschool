/*    */ package com.maddox.il2.objects.weapons;
/*    */ 
/*    */ import com.maddox.JGP.Color3f;
/*    */ import com.maddox.il2.engine.BulletProperties;
/*    */ import com.maddox.il2.engine.GunProperties;
/*    */ 
/*    */ 
/*    */ public class MGunGSh23L_single_s
/*    */   extends MGunVYas
/*    */ {
/*    */   public MGunGSh23L_single_s() {}
/*    */   
/*    */   public GunProperties createProperties()
/*    */   {
/* 15 */     GunProperties gunproperties = super.createProperties();
/* 16 */     gunproperties.bCannon = false;
/* 17 */     gunproperties.bUseHookAsRel = true;
/* 18 */     gunproperties.fireMesh = null;
/* 19 */     gunproperties.fire = "3DO/Effects/GunFire/30mm/GunFire.eff";
/* 20 */     gunproperties.sprite = "3DO/Effects/GunFire/30mm/GunFlare.eff";
/* 21 */     gunproperties.smoke = "effects/smokes/MachineGun.eff";
/* 22 */     gunproperties.shells = "3DO/Effects/GunShells/CannonShells.eff";
/* 23 */     gunproperties.sound = "weapon.GSH23";
/* 24 */     gunproperties.emitColor = new Color3f(1.0F, 1.0F, 0.0F);
/* 25 */     gunproperties.emitI = 2.5F;
/* 26 */     gunproperties.emitR = 1.5F;
/* 27 */     gunproperties.emitTime = 0.03F;
/* 28 */     gunproperties.aimMinDist = 10.0F;
/* 29 */     gunproperties.aimMaxDist = 1000.0F;
/* 30 */     gunproperties.weaponType = 3;
/* 31 */     gunproperties.maxDeltaAngle = 1.5F; //0.28F;
/* 32 */     gunproperties.shotFreqDeviation = 3.3F; //0.03F;
/* 33 */     gunproperties.shotFreq = 53.3F;
/* 34 */     gunproperties.traceFreq = 3;
/* 35 */     gunproperties.bEnablePause = true;
/* 36 */     gunproperties.bullets = 120;
/* 37 */     gunproperties.bulletsCluster = 1;
/* 38 */     gunproperties.bullet = new BulletProperties[] {
/* 39 */       new BulletProperties(), new BulletProperties(), new BulletProperties(), new BulletProperties()};
/*    */     
/* 41 */     gunproperties.bullet[0].massa = 0.190F;
/* 42 */     gunproperties.bullet[0].kalibr = 0.000320F;
/* 43 */     gunproperties.bullet[0].speed = 690.0F;
/* 44 */     gunproperties.bullet[0].power = 0.007F;
/* 45 */     gunproperties.bullet[0].powerType = 0;
/* 46 */     gunproperties.bullet[0].powerRadius = 0.2F;
/* 47 */     gunproperties.bullet[0].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
/* 48 */     gunproperties.bullet[0].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
/* 49 */     gunproperties.bullet[0].traceColor = -654299393;
/* 50 */     gunproperties.bullet[0].timeLife = 8.0F;

             gunproperties.bullet[1].massa = 0.190F;
/* 42 */     gunproperties.bullet[1].kalibr = 0.000320F;
/* 43 */     gunproperties.bullet[1].speed = 690.0F;
/* 44 */     gunproperties.bullet[1].power = 0.007F;
/* 45 */     gunproperties.bullet[1].powerType = 0;
/* 46 */     gunproperties.bullet[1].powerRadius = 0.2F;
/* 47 */     gunproperties.bullet[1].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
/* 48 */     gunproperties.bullet[1].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
/* 49 */     gunproperties.bullet[1].traceColor = -654299393;
/* 50 */     gunproperties.bullet[1].timeLife = 8.0F;

/* 51 */     gunproperties.bullet[2].massa = 0.176F;
/* 52 */     gunproperties.bullet[2].kalibr = 0.000320F;
/* 53 */     gunproperties.bullet[2].speed = 710.0F;
/* 54 */     gunproperties.bullet[2].power = 0.0115F;
/* 55 */     gunproperties.bullet[2].powerType = 1;
/* 56 */     gunproperties.bullet[2].powerRadius = 1.0F;
/* 57 */     gunproperties.bullet[2].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
/* 58 */     gunproperties.bullet[2].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
/* 59 */     gunproperties.bullet[2].traceColor = -654299393;
/* 60 */     gunproperties.bullet[2].timeLife = 8.0F;

             gunproperties.bullet[3].massa = 0.176F;
/* 52 */     gunproperties.bullet[3].kalibr = 0.000320F;
/* 53 */     gunproperties.bullet[3].speed = 710.0F;
/* 54 */     gunproperties.bullet[3].power = 0.0115F;
/* 55 */     gunproperties.bullet[3].powerType = 1;
/* 56 */     gunproperties.bullet[3].powerRadius = 1.0F;
/* 57 */     gunproperties.bullet[3].traceMesh = "3do/effects/tracers/20mmRed/mono.sim";
/* 58 */     gunproperties.bullet[3].traceTrail = "effects/Smokes/SmokeBlack_BuletteTrail.eff";
/* 59 */     gunproperties.bullet[3].traceColor = -654299393;
/* 60 */     gunproperties.bullet[3].timeLife = 8.0F;
	 /*     
	 gunproperties.bullet[2].massa = 0.175F;
     gunproperties.bullet[2].kalibr = 6.245E-4F;
     gunproperties.bullet[2].speed = 690.0F;
     gunproperties.bullet[2].power = 0.007F;
     gunproperties.bullet[2].powerType = 0;
     gunproperties.bullet[2].powerRadius = 0.0F;
     gunproperties.bullet[2].traceMesh = null;
     gunproperties.bullet[2].traceTrail = null;
     gunproperties.bullet[2].traceColor = 0;
     gunproperties.bullet[2].timeLife = 4.0F;
     */
     return gunproperties;
/*    */   }
/*    */ }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\WeaponMod
 * Qualified Name:     com.maddox.il2.objects.weapons.MGunGSh23Ls
 * Java Class Version: 1.1 (45.3)
 * JD-Core Version:    0.7.0.1
 */