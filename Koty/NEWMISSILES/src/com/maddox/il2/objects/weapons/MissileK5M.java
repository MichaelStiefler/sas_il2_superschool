 package com.maddox.il2.objects.weapons;
 
 import com.maddox.JGP.Point3d;
 import com.maddox.il2.engine.Actor;
 import com.maddox.il2.engine.Orient;
 import com.maddox.rts.NetChannel;
 import com.maddox.rts.Property;
 
 public class MissileK5M extends Missile
 {
   public MissileK5M() {}
   
   static class SPAWN extends Missile.SPAWN
   {
     SPAWN() {}
     
     public void doSpawn(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
     {
       new MissileK5M(actor, netchannel, i, point3d, orient, f);
     }
   }
   
   public MissileK5M(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f) {
     MissileInit(actor, netchannel, i, point3d, orient, f);
   }
   
 
 
   static
   {
     Class class1 = MissileK5M.class;
     Property.set(class1, "mesh", "3do/arms/K-5M/mono.sim");
     Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
     Property.set(class1, "flame", "3do/Effects/RocketSidewinder/RocketSidewinderFlame.sim");
     Property.set(class1, "smoke", "3DO/Effects/Aircraft/TurboHWK109D.eff");
     Property.set(class1, "exhausts", 2);
     Property.set(class1, "emitColor", new com.maddox.JGP.Color3f(1.0F, 1.0F, 0.5F));
     Property.set(class1, "emitLen", 50.0F);
     Property.set(class1, "emitMax", 3F);//0.4
     Property.set(class1, "sound", "weapon.rocket_132");
     Property.set(class1, "timeLife", 12.0F);
     Property.set(class1, "timeFire", 7.0F);
     Property.set(class1, "force", 8500.0F);
     Property.set(class1, "forceT1", 1.0F);
     Property.set(class1, "forceP1", 0.0F);
     Property.set(class1, "forceT2", 0.2F);
     Property.set(class1, "forceP2", 50.0F);
     Property.set(class1, "dragCoefficient", 0.5F);
     Property.set(class1, "power", 13F);
     Property.set(class1, "powerType", 1);
     Property.set(class1, "radius", 10.0F);
     Property.set(class1, "kalibr", 0.2F);
     Property.set(class1, "massa", 74.2F);
     Property.set(class1, "massaEnd", 68.0F);
     Property.set(class1, "stepMode", 1);
     Property.set(class1, "launchType", 1);
     Property.set(class1, "detectorType", 3);
     Property.set(class1, "sunRayAngle", 0.0F);
     Property.set(class1, "multiTrackingCapable", 0);
     Property.set(class1, "canTrackSubs", 0);
     Property.set(class1, "minPkForAI", 25.0F);
     Property.set(class1, "timeForNextLaunchAI", 1000L);
     Property.set(class1, "engineDelayTime", 0L);
     Property.set(class1, "attackDecisionByAI", 1);
     Property.set(class1, "targetType", 1);
     Property.set(class1, "shotFreq", 0.01F);
     
 
 
 
     Property.set(class1, "groundTrackFactor", 10.0F);
     Property.set(class1, "flareLockTime", 1000L);
     Property.set(class1, "trackDelay", 1000L);
     Property.set(class1, "failureRate", 30.0F);
     
     Property.set(class1, "maxLockGForce", 99.9F);
     Property.set(class1, "maxFOVfrom", /*14.0*/7F);
     Property.set(class1, "maxFOVto", 180.0F);
     Property.set(class1, "PkMaxFOVfrom", 14.0F);//30
     Property.set(class1, "PkMaxFOVto", 70.0F);
     Property.set(class1, "PkDistMin", 800.0F);
     Property.set(class1, "PkDistOpt", 1500.0F);
     Property.set(class1, "PkDistMax", 4000.0F);
     Property.set(class1, "leadPercent", 0.0F);
     Property.set(class1, "maxGForce", 3.0F);
     Property.set(class1, "stepsForFullTurn", 10.0F);
     Property.set(class1, "fxLock", "weapon.K5.lock");
     Property.set(class1, "fxNoLock", "weapon.K5.nolock");
     Property.set(class1, "smplLock", "K5_lock.wav");
     Property.set(class1, "smplNoLock", "K5_no_lock.wav");
     Property.set(class1, "friendlyName", "K-5M");
     com.maddox.rts.Spawn.add(class1, new SPAWN());
   }
 }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\JetEra
 * Qualified Name:     com.maddox.il2.objects.weapons.MissileK5M
 * Java Class Version: 1.1 (45.3)
 * JD-Core Version:    0.7.0.1
 */