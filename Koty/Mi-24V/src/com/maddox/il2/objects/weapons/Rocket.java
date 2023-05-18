 package com.maddox.il2.objects.weapons;
 
 import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
 
 public class Rocket extends com.maddox.il2.engine.ActorMesh implements com.maddox.il2.engine.MsgCollisionRequestListener, com.maddox.il2.engine.MsgCollisionListener
 {
   protected long noGDelay;
   
   class Interpolater extends com.maddox.il2.engine.Interpolate
   {
     public boolean tick()
     {
       if (this.timeBegin + Rocket.this.timeLife < Time.current())
       {
         Rocket.this.doExplosionAir();
         Rocket.this.postDestroy();
         Rocket.this.collide(false);
         Rocket.this.drawing(false);
         return false;
       }
       if (this.timeBegin + Rocket.this.timeFire < Time.current())
       {
         Rocket.this.endSmoke();
         Rocket.this.P = 0.0F;
       }
       else {
         Rocket.this.M -= Rocket.this.DM;
       }
       if (Rocket.this.spinFactor > 0.0F)
       {
         if (Rocket.this.interpolateStep()) Ballistics.updateSpinningRocket(this.actor, Rocket.this.M, Rocket.this.S, Rocket.this.P, this.timeBegin + Rocket.this.noGDelay < Time.current(), Rocket.this.spinFactor);
       }
       else if (Rocket.this.interpolateStep()) Ballistics.update(this.actor, Rocket.this.M, Rocket.this.S, Rocket.this.P, this.timeBegin + Rocket.this.noGDelay < Time.current());
       return true;
     }
     
 
 
     Interpolater() {}
   }
   
 
   public void msgCollisionRequest(Actor paramActor, boolean[] paramArrayOfBoolean)
   {
     if (paramActor == getOwner()) {
       paramArrayOfBoolean[0] = false;
     }
   }
   
   public void msgCollision(Actor paramActor, String paramString1, String paramString2) {
     if ((getOwner() == World.getPlayerAircraft()) && (!(paramActor instanceof com.maddox.il2.objects.ActorLand)))
     {
       World.cur().scoreCounter.rocketsHit += 1;
       if ((com.maddox.il2.game.Mission.isNet()) && ((paramActor instanceof Aircraft)) && (((Aircraft)paramActor).isNetPlayer()))
         com.maddox.il2.net.Chat.sendLogRnd(3, "gore_rocketed", (Aircraft)getOwner(), (Aircraft)paramActor);
     }
     doExplosion(paramActor, paramString2);
   }
   
   protected void doExplosion(Actor paramActor, String paramString)
   {
     this.pos.getTime(Time.current(), p);
     Class localClass = getClass();
     float f1 = Property.floatValue(localClass, "power", 1000.0F);
     int i = Property.intValue(localClass, "powerType", 0);
     float f2 = Property.floatValue(localClass, "radius", 0.0F);
     int j = Property.intValue(localClass, "newEffect", 0);
     int k = Property.intValue(localClass, "nuke", 0);
     getSpeed(this.speed);
     com.maddox.JGP.Vector3f localVector3f = new com.maddox.JGP.Vector3f(this.speed);
     if (f2 <= 0.0F)
     {
       com.maddox.il2.ai.MsgShot.send(paramActor, paramString, p, localVector3f, this.M, getOwner(), f1, 1, 0.0D);
     }
     else {
       com.maddox.il2.ai.MsgShot.send(paramActor, paramString, p, localVector3f, this.M, getOwner(), (float)(0.5F * this.M * this.speed.lengthSquared()), 0, 0.0D);
       com.maddox.il2.ai.MsgExplosion.send(paramActor, paramString, p, getOwner(), this.M, f1, i, f2, k);
     }
     com.maddox.il2.objects.effects.Explosions.generateRocket(paramActor, p, f1, i, f2, j);
     destroy();
   }
   
   protected void doExplosionAir()
   {
     this.pos.getTime(Time.current(), p);
     Class localClass = getClass();
     float f1 = Property.floatValue(localClass, "power", 1000.0F);
     int i = Property.intValue(localClass, "powerType", 0);
     float f2 = Property.floatValue(localClass, "radius", 150.0F);
     com.maddox.il2.ai.MsgExplosion.send(null, null, p, getOwner(), this.M, f1, i, f2);
     com.maddox.il2.objects.effects.Explosions.AirFlak(p, 0);
   }
   
   public boolean interpolateStep()
   {
     return true;
   }
   
   protected void endSmoke()
   {
     if (this.endedSmoke)
       return;
     this.endedSmoke = true;
     if (this.light != null)
       this.light.light.setEmit(0.0F, 1.0F);
     Eff3DActor.finish(this.smoke);
     Eff3DActor.finish(this.sprite);
     com.maddox.rts.ObjState.destroy(this.flame);
     stopSounds();
   }
   
   public void destroy()
   {
     endSmoke();
     super.destroy();
     this.smoke = null;
     this.sprite = null;
     this.flame = null;
     this.light = null;
     this.soundName = null;
   }
   
   protected void setThrust(float paramFloat)
   {
     this.P = paramFloat;
   }
   
   public double getSpeed(Vector3d paramVector3d)
   {
     if (paramVector3d != null)
       paramVector3d.set(this.speed);
     return this.speed.length();
   }
   
   public void setSpeed(Vector3d paramVector3d)
   {
     this.speed.set(paramVector3d);
   }
   
   protected void init(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6)
   {
     if ((Actor.isValid(getOwner())) && (World.getPlayerAircraft() == getOwner()))
       setName("_rocket_");
     super.getSpeed(this.speed);
     if (World.cur().diffCur.Wind_N_Turbulence)
     {
       Point3d localPoint3d = new Point3d();
       Vector3d localVector3d = new Vector3d();
       this.pos.getAbs(localPoint3d);
       World.wind().getVectorWeapon(localPoint3d, localVector3d);
       this.speed.add(-localVector3d.x, -localVector3d.y, 0.0D);
     }
     this.S = ((float)(3.141592653589793D * paramFloat1 * paramFloat1 / 4.0D));
     this.M = paramFloat2;
     if (paramFloat4 > 0.0F) {
       this.DM = ((paramFloat2 - paramFloat3) / (paramFloat4 / Time.tickConstLenFs()));
     } else
       this.DM = 0.0F;
     this.P = paramFloat5;
     this.timeFire = (long) ((paramFloat4 * 1000.0F + 0.5D));
     this.timeLife = (long) ((paramFloat6 * 1000.0F + 0.5D));
   }
   
   public void start(float paramFloat)
   {
     start(paramFloat, 0);
   }
   public void update()
   {
	   //rocketSpreadValue1 = (float) (Math.random());
	   //rocketSpreadValue2 = (float) (Math.random());
   }
   public void start(float paramFloat, int paramInt)
   {

	 //  HUD.log(AircraftHotKeys.hudLogWeaponId, "start happend");
     Class localClass = getClass();
     float f1 = Property.floatValue(localClass, "kalibr", 0.082F);
     if (paramFloat <= 0.0F)
       paramFloat = Property.floatValue(localClass, "timeLife", 45.0F);
     RangeRandom localRangeRandom = new RangeRandom(paramInt);
     rocketSpreadValue1 = (float) (Math.random());
	 rocketSpreadValue2 = (float) (Math.random());
	 // HUD.log(AircraftHotKeys.hudLogWeaponId, "start happend" + rocketSpreadValue1 + "+" + rocketSpreadValue2);
     //float f2 = -1.0F + 2.0F * localRangeRandom.nextFloat();
     float f2 = -1.0F + 2.0F * rocketSpreadValue1;
     f2 *= f2 * f2;
     //float f3 = -1.0F + 2.0F * localRangeRandom.nextFloat();
     float f3 = -1.0F + 2.0F * rocketSpreadValue2;
     f3 *= f3 * f3;
     init(f1, Property.floatValue(localClass, "massa", 6.8F), Property.floatValue(localClass, "massaEnd", 2.52F), Property.floatValue(localClass, "timeFire", 4.0F) / (1.0F + 0.1F * f2), Property.floatValue(localClass, "force", 500.0F) * (1.0F + 0.1F * f2), paramFloat + f3 * 0.1F);
     this.spinFactor = Property.floatValue(localClass, "spinningStraightFactor", 0.0F);
     setOwner(this.pos.base(), false, false, false);
     this.pos.setBase(null, null, true);
     this.pos.setAbs(this.pos.getCurrent());
     this.pos.getAbs(Aircraft.tmpOr);
     float f4 = /*0.68F* */ Property.floatValue(localClass, "maxDeltaAngle", 3.0F);
     //f2 = -1.0F + 2.0F * localRangeRandom.nextFloat();
     f2 = -0.5F + 1.0F * rocketSpreadValue1;
     //f3 = -1.0F + 2.0F * localRangeRandom.nextFloat();
     f3 = -0.5F + 1.0F * rocketSpreadValue2;
     f2 *= f2 * f2 * f4;
     f3 *= f3 * f3 * f4;
     //f2 *= f4;
     //f3 *= f4;
     Aircraft.tmpOr.increment(f2, f3, 0.0F);
     this.pos.setAbs(Aircraft.tmpOr);
     this.pos.getRelOrient().transformInv(this.speed);
     this.speed.z /= 3.0D;
     this.speed.x += 200.0D;
     this.pos.getRelOrient().transform(this.speed);
     collide(true);
     interpPut(new Interpolater(), null, Time.current(), null);
     if (getOwner() == World.getPlayerAircraft())
       World.cur().scoreCounter.rocketsFire += 1;
     if (!com.maddox.il2.engine.Config.isUSE_RENDER())
       return;
     com.maddox.il2.engine.Hook localHook = null;
     String str = Property.stringValue(localClass, "sprite", null);
     if (str != null)
     {
       if (localHook == null)
         localHook = findHook("_SMOKE");
       this.sprite = Eff3DActor.New(this, localHook, null, f1, str, -1.0F);
       if (this.sprite != null)
         this.sprite.pos.changeHookToRel();
     }
     str = Property.stringValue(localClass, "flame", null);
     if (str != null)
     {
       if (localHook == null)
         localHook = findHook("_SMOKE");
       this.flame = new com.maddox.il2.objects.ActorSimpleMesh(str);
       if (this.flame != null)
       {
         ((com.maddox.il2.objects.ActorSimpleMesh)this.flame).mesh().setScale(f1);
         this.flame.pos.setBase(this, localHook, false);
         this.flame.pos.changeHookToRel();
         this.flame.pos.resetAsBase();
       }
     }
     str = Property.stringValue(localClass, "smoke", null);
     if (str != null)
     {
       if (localHook == null)
         localHook = findHook("_SMOKE");
       this.smoke = Eff3DActor.New(this, localHook, null, 1.0F, str, -1.0F);
       if (this.smoke != null)
         this.smoke.pos.changeHookToRel();
     }
     this.light = new com.maddox.il2.engine.LightPointActor(new com.maddox.il2.engine.LightPointWorld(), new Point3d());
     this.light.light.setColor((com.maddox.JGP.Color3f)Property.value(localClass, "emitColor", new com.maddox.JGP.Color3f(1.0F, 1.0F, 0.5F)));
     this.light.light.setEmit(Property.floatValue(localClass, "emitMax", 1.0F), Property.floatValue(localClass, "emitLen", 50.0F));
     this.draw.lightMap().put("light", this.light);
     this.soundName = Property.stringValue(localClass, "sound", null);
     if (this.soundName != null)
       newSound(this.soundName, true);
     setMesh(com.maddox.il2.engine.MeshShared.get(Property.stringValue(getClass(), "meshFly", Property.stringValue(getClass(), "mesh", null))));
   }
   
   public Object getSwitchListener(com.maddox.rts.Message paramMessage) {
     return this;
   }
   
   public String getRocketName()
   {
     Class localClass = getClass();
     String str = Property.stringValue(localClass, "friendlyName", "Rocket");
     return str;
   }
   
   public Rocket()
   {
     this.noGDelay = 1000L;
     this.endedSmoke = false;
     this.speed = new Vector3d();
     setMesh(com.maddox.il2.engine.MeshShared.get(Property.stringValue(getClass(), "mesh", null)));
     this.flags |= 0xE0;
     collide(false);
     drawing(true);
   }
   

   public float rocketSpreadValue1;
   public float rocketSpreadValue2;
   private static Point3d p = new Point3d();
   private boolean endedSmoke;
   protected Eff3DActor smoke;
   protected Eff3DActor sprite;
   protected Actor flame;
   protected com.maddox.il2.engine.LightPointActor light;
   protected String soundName;
   protected long timeFire;
   protected long timeLife;
   protected Vector3d speed;
   private float S;
   protected float M;
   private float DM;
   private float P;
   private float spinFactor;
 }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\EngMod
 * Qualified Name:     com.maddox.il2.objects.weapons.Rocket
 * Java Class Version: 1.3 (47.0)
 * JD-Core Version:    0.7.0.1
 */