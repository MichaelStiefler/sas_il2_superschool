/*    */ package com.maddox.il2.objects.weapons;
/*    */ 
/*    */ import com.maddox.JGP.Point3d;
/*    */ import com.maddox.il2.ai.World;
/*    */ import com.maddox.il2.engine.Actor;
/*    */ import com.maddox.il2.engine.ActorPos;
/*    */ import com.maddox.il2.engine.Landscape;
/*    */ import com.maddox.il2.objects.effects.Explosions;
/*    */ import com.maddox.rts.MsgDestroy;
/*    */ import com.maddox.rts.Property;
/*    */ import com.maddox.rts.Time;
/*    */ 
/*    */ public class FuelTank extends Bomb
/*    */ {
/*    */   public float Fuel;
/* 29 */   private static Point3d p = new Point3d();
/*    */ 
/*    */   public FuelTank()
/*    */   {
/* 18 */     Class localClass = super.getClass();
/* 19 */     fill(Property.floatValue(localClass, "massa", 0.0F));
/*    */   }
/*    */ 
/*    */   protected void doExplosion(Actor paramActor, String paramString) {
/* 23 */     MsgDestroy.Post(Time.current(), this);
/* 24 */     this.pos.getTime(Time.current(), p);
/* 25 */     if (World.land().isWater(p.x, p.y))
/* 26 */       Explosions.WreckageDrop_Water(p);
/*    */   }
/*    */ 
/*    */   public void fill(float paramFloat)
/*    */   {
/* 33 */     setName("_fueltank_");
/* 34 */     this.M = paramFloat;
/* 35 */     this.Fuel = (paramFloat * 0.9F);
/*    */   }
/*    */ 
/*    */   public float getFuel(float paramFloat) {
/* 39 */     if (paramFloat > this.Fuel) paramFloat = this.Fuel;
/* 40 */     this.Fuel -= paramFloat;
/* 41 */     this.M -= paramFloat;
/* 42 */     return paramFloat;
/*    */   }
/*    */ }