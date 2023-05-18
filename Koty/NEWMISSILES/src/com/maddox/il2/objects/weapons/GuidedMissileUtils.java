/*      */ package com.maddox.il2.objects.weapons;
/*      */ 
/*      */ import com.maddox.JGP.Geom;
/*      */ import com.maddox.JGP.Point3d;
/*      */ import com.maddox.JGP.Point3f;
import com.maddox.JGP.Tuple3d;
/*      */ import com.maddox.JGP.Vector3d;
/*      */ import com.maddox.JGP.Vector3f;
/*      */ import com.maddox.il2.ai.EventLog;
/*      */ import com.maddox.il2.ai.Way;
/*      */ import com.maddox.il2.ai.World;
/*      */ import com.maddox.il2.ai.air.AutopilotAI;
/*      */ import com.maddox.il2.ai.air.Pilot;
/*      */ import com.maddox.il2.engine.Actor;
/*      */ import com.maddox.il2.engine.ActorPos;
/*      */ import com.maddox.il2.engine.EffClouds;
import com.maddox.il2.engine.Engine;
/*      */ import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
/*      */ import com.maddox.il2.fm.Autopilotage;
/*      */ import com.maddox.il2.fm.Controls;
/*      */ import com.maddox.il2.fm.FlightModel;
/*      */ import com.maddox.il2.fm.Motor;
/*      */ import com.maddox.il2.fm.RealFlightModel;
/*      */ import com.maddox.il2.game.Main;
/*      */ import com.maddox.il2.game.Mission;
/*      */ import com.maddox.il2.objects.air.Aircraft;
/*      */ import com.maddox.il2.objects.air.TypeGroundRadar;
import com.maddox.il2.objects.air.TypeHARM_carrier;
/*      */ import com.maddox.il2.objects.air.TypeLaserDesignator;
import com.maddox.il2.objects.air.TypeMyotka;
/*      */ import com.maddox.il2.objects.air.TypeSemiRadar;
/*      */ import com.maddox.il2.objects.bridges.LongBridge;
/*      */ import com.maddox.il2.objects.sounds.SndAircraft;
/*      */ import com.maddox.il2.objects.vehicles.artillery.RocketryRocket;
/*      */ import com.maddox.rts.Property;
/*      */ import com.maddox.rts.Time;
/*      */ import com.maddox.sound.Sample;
/*      */ import com.maddox.sound.SoundFX;

/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ 
/*      */ public class GuidedMissileUtils
/*      */ {
/*      */   private class MissileDataForPk
/*      */   {
/*      */     private int triggerNum;
/*      */     private float maxLaunchLoad;
/*      */     private float maxAngleToTarget;
/*      */     private float maxAngleFromTargetAft;
/*      */     private float minDist;
/*      */     private float optDist;
/*      */     private float maxDist;
/*      */     
/*      */     MissileDataForPk(GuidedMissileUtils param1)
/*      */     {
/*   54 */       this();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public int getTriggerNum()
/*      */     {
/*   64 */       return this.triggerNum;
/*      */     }
/*      */     
/*   67 */     public void setTriggerNum(int paramInt) { this.triggerNum = paramInt; }
/*      */     
/*      */     public float getMaxLaunchLoad() {
/*   70 */       return this.maxLaunchLoad;
/*      */     }
/*      */     
/*   73 */     public void setMaxLaunchLoad(float paramFloat) { this.maxLaunchLoad = paramFloat; }
/*      */     
/*      */     public float getMaxAngleToTarget() {
/*   76 */       return this.maxAngleToTarget;
/*      */     }
/*      */     
/*   79 */     public void setMaxAngleToTarget(float paramFloat) { this.maxAngleToTarget = paramFloat; }
/*      */     
/*      */     public float getMaxAngleFromTargetAft() {
/*   82 */       return this.maxAngleFromTargetAft;
/*      */     }
/*      */     
/*   85 */     public void setMaxAngleFromTargetAft(float paramFloat) { this.maxAngleFromTargetAft = paramFloat; }
/*      */     
/*      */     public float getMinDist() {
/*   88 */       return this.minDist;
/*      */     }
/*      */     
/*   91 */     public void setMinDist(float paramFloat) { this.minDist = paramFloat; }
/*      */     
/*      */     public float getOptDist() {
/*   94 */       return this.optDist;
/*      */     }
/*      */     
/*   97 */     public void setOptDist(float paramFloat) { this.optDist = paramFloat; }
/*      */     
/*      */     public float getMaxDist() {
/*  100 */       return this.maxDist;
/*      */     }
/*      */     
/*  103 */     public void setMaxDist(float paramFloat) { this.maxDist = paramFloat; }
/*      */     
/*      */     private MissileDataForPk() {
/*  106 */       this.triggerNum = 0;
/*  107 */       this.maxLaunchLoad = 99.9F;
/*  108 */       this.maxAngleToTarget = 99.9F;
/*  109 */       this.maxAngleFromTargetAft = 99.9F;
/*  110 */       this.minDist = 99.9F;
/*  111 */       this.optDist = 99.9F;
/*  112 */       this.maxDist = 99.9F;
/*      */     }
/*      */   }
/*      */   
/*      */   public static float angleActorBetween(Actor paramActor1, Actor paramActor2) {
/*  117 */     float f = 180.1F;
/*  118 */     double d1 = 0.0D;
/*  119 */     Loc localLoc = new Loc();
/*  120 */     Point3d localPoint3d1 = new Point3d();
/*  121 */     Point3d localPoint3d2 = new Point3d();
/*  122 */     Vector3d localVector3d1 = new Vector3d();
/*  123 */     Vector3d localVector3d2 = new Vector3d();
/*  124 */     paramActor1.pos.getAbs(localLoc);
/*  125 */     localLoc.get(localPoint3d1);
/*  126 */     paramActor2.pos.getAbs(localPoint3d2);
/*  127 */     localVector3d1.sub(localPoint3d2, localPoint3d1);
/*  128 */     d1 = localVector3d1.length();
/*  129 */     localVector3d1.scale(1.0D / d1);
/*  130 */     localVector3d2.set(1.0D, 0.0D, 0.0D);
/*  131 */     localLoc.transform(localVector3d2);
/*  132 */     d1 = localVector3d2.dot(localVector3d1);
/*  133 */     f = Geom.RAD2DEG((float)Math.acos(d1));
/*  134 */     return f;
/*      */   }
/*      */   
/*      */   public static float angleBetween(Actor paramActor, Point3d paramPoint3d) {
/*  138 */     float f = 180.1F;
/*  139 */     if ((paramActor == null) || (!Actor.isValid(paramActor)) || (paramPoint3d == null)) {
/*  140 */       return f;
/*      */     }
/*  142 */     double d1 = 0.0D;
/*  143 */     Loc localLoc = new Loc();
/*  144 */     Point3d localPoint3d = new Point3d();
/*  145 */     Vector3d localVector3d1 = new Vector3d();
/*  146 */     Vector3d localVector3d2 = new Vector3d();
/*  147 */     paramActor.pos.getAbs(localLoc);
/*  148 */     localLoc.get(localPoint3d);
/*  149 */     localVector3d1.sub(paramPoint3d, localPoint3d);
/*  150 */     d1 = localVector3d1.length();
/*  151 */     localVector3d1.scale(1.0D / d1);
/*  152 */     localVector3d2.set(1.0D, 0.0D, 0.0D);
/*  153 */     localLoc.transform(localVector3d2);
/*  154 */     d1 = localVector3d2.dot(localVector3d1);
/*  155 */     f = Geom.RAD2DEG((float)Math.acos(d1));
/*  156 */     return f;
/*      */   }
/*      */   
/*      */   public static float angleBetween(Point3d paramPoint3d, Actor paramActor) {
/*  160 */     float f = 180.1F;
/*      */     
/*      */ 
/*      */ 
/*  164 */     double d1 = 0.0D;
/*  165 */     Loc localLoc = new Loc();
/*  166 */     Point3d localPoint3d1 = new Point3d();
/*  167 */     Point3d localPoint3d2 = new Point3d();
/*  168 */     Vector3d localVector3d1 = new Vector3d();
/*  169 */     Vector3d localVector3d2 = new Vector3d();
/*  170 */     localLoc.set(paramPoint3d);
/*  171 */     localLoc.get(localPoint3d1);
/*  172 */     paramActor.pos.getAbs(localPoint3d2);
/*  173 */     localVector3d1.sub(localPoint3d2, localPoint3d1);
/*  174 */     d1 = localVector3d1.length();
/*  175 */     localVector3d1.scale(1.0D / d1);
/*  176 */     localVector3d2.set(1.0D, 0.0D, 0.0D);
/*  177 */     localLoc.transform(localVector3d2);
/*  178 */     d1 = localVector3d2.dot(localVector3d1);
/*  179 */     f = Geom.RAD2DEG((float)Math.acos(d1));
/*  180 */     return f;
/*      */   }
/*      */   
/*      */   public static float angleBetween(Actor paramActor1, Actor paramActor2) {
/*  184 */     float f = 180.1F;
/*      */     
/*      */ 
/*      */ 
/*  188 */     double d1 = 0.0D;
/*  189 */     Loc localLoc = new Loc();
/*  190 */     Point3d localPoint3d1 = new Point3d();
/*  191 */     Point3d localPoint3d2 = new Point3d();
/*  192 */     Vector3d localVector3d1 = new Vector3d();
/*  193 */     Vector3d localVector3d2 = new Vector3d();
/*  194 */     paramActor1.pos.getAbs(localLoc);
/*  195 */     localLoc.get(localPoint3d1);
/*  196 */     paramActor2.pos.getAbs(localPoint3d2);
/*  197 */     localVector3d1.sub(localPoint3d2, localPoint3d1);
/*  198 */     d1 = localVector3d1.length();
/*  199 */     localVector3d1.scale(1.0D / d1);
/*  200 */     localVector3d2.set(1.0D, 0.0D, 0.0D);
/*  201 */     localLoc.transform(localVector3d2);
/*  202 */     d1 = localVector3d2.dot(localVector3d1);
/*  203 */     f = Geom.RAD2DEG((float)Math.acos(d1));
/*  204 */     return f;
/*      */   }
/*      */   
/*      */   public static float angleBetween(Actor paramActor, Vector3d paramVector3d) {
/*  208 */     Vector3d localVector3d1 = new Vector3d();
/*  209 */     localVector3d1.set(paramVector3d);
/*  210 */     double d1 = 0.0D;
/*  211 */     Loc localLoc = new Loc();
/*  212 */     Point3d localPoint3d = new Point3d();
/*  213 */     Vector3d localVector3d2 = new Vector3d();
/*  214 */     paramActor.pos.getAbs(localLoc);
/*  215 */     localLoc.get(localPoint3d);
/*  216 */     d1 = localVector3d1.length();
/*  217 */     localVector3d1.scale(1.0D / d1);
/*  218 */     localVector3d2.set(1.0D, 0.0D, 0.0D);
/*  219 */     localLoc.transform(localVector3d2);
/*  220 */     d1 = localVector3d2.dot(localVector3d1);
/*  221 */     return Geom.RAD2DEG((float)Math.acos(d1));
/*      */   }
/*      */   
/*      */   public static float angleBetween(Actor paramActor, Vector3f paramVector3f) {
/*  225 */     return angleBetween(paramActor, new Vector3d(paramVector3f));
/*      */   }
/*      */   
/*      */   public static float pitchBetween(Actor paramActor1, Actor paramActor2) {
/*  229 */     float f = 180.1F;
/*  230 */     double d1 = 0.0D;
/*  231 */     Loc localLoc = new Loc();
/*  232 */     Point3d localPoint3d1 = new Point3d();
/*  233 */     Point3d localPoint3d2 = new Point3d();
/*  234 */     Vector3d localVector3d1 = new Vector3d();
/*  235 */     Vector3d localVector3d2 = new Vector3d();
/*  236 */     paramActor1.pos.getAbs(localLoc);
/*  237 */     localLoc.get(localPoint3d1);
/*  238 */     paramActor2.pos.getAbs(localPoint3d2);
/*  239 */     localVector3d1.sub(localPoint3d2, localPoint3d1);
/*  240 */     d1 = localVector3d1.length();
/*  241 */     localVector3d1.scale(1.0D / d1);
/*  242 */     localVector3d2.set(1.0D, 0.0D, 0.0D);
/*  243 */     localLoc.transform(localVector3d2);
/*  244 */     d1 = localVector3d1.y;
/*  245 */     localVector3d1.y = localVector3d1.z;
/*  246 */     localVector3d1.z = d1;
/*  247 */     d1 = localVector3d2.dot(localVector3d1);
/*  248 */     f = Geom.RAD2DEG((float)Math.acos(d1));
/*  249 */     return f;
/*      */   }
/*      */   
/*      */   public static double distanceBetween(Actor paramActor1, Actor paramActor2) {
/*  253 */     double d1 = 99999.999D;
/*  254 */     if ((!Actor.isValid(paramActor1)) || (!Actor.isValid(paramActor2))) return d1;
/*  255 */     Loc localLoc = new Loc();
/*  256 */     Point3d localPoint3d1 = new Point3d();
/*  257 */     Point3d localPoint3d2 = new Point3d();
/*  258 */     paramActor1.pos.getAbs(localLoc);
/*  259 */     localLoc.get(localPoint3d1);
/*  260 */     paramActor2.pos.getAbs(localPoint3d2);
/*  261 */     d1 = localPoint3d1.distance(localPoint3d2);
/*  262 */     return d1;
/*      */   }
/*      */   
/*      */   public static void LocalLog(Actor paramActor, int paramInt, String paramString) {
/*  266 */     if ((paramActor == World.getPlayerAircraft()) && (!paramActor.isNetMirror())) {
/*  267 */       com.maddox.il2.game.HUD.log(paramInt, paramString);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void LocalLog(Actor paramActor, String paramString) {
/*  272 */     if ((paramActor == World.getPlayerAircraft()) && (!paramActor.isNetMirror())) {
/*  273 */       com.maddox.il2.game.HUD.log(paramString);
/*      */     }
/*      */   }
/*      */   
/*      */   public GuidedMissileUtils(Actor paramActor) {
/*  278 */     initParams(paramActor);
/*      */   }
/*      */   
/*      */ 
/*      */   public GuidedMissileUtils(Actor paramActor, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*  284 */     initParams(paramActor, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramFloat7, paramFloat8, paramFloat9, paramFloat10, paramFloat11, paramFloat12, paramFloat13, paramFloat14, paramLong1, paramLong2, paramBoolean1, paramBoolean2, paramBoolean3, paramString1, paramString2, paramString3, paramString4);
/*      */   }
/*      */   
/*      */   private boolean actorIsAI(Actor paramActor)
/*      */   {
/*  289 */     if (!(paramActor instanceof Aircraft)) return true;
/*  290 */     if (((Aircraft)paramActor).FM == null) return true;
/*  291 */     if (((paramActor != World.getPlayerAircraft()) || (!((RealFlightModel)((Aircraft)paramActor).FM).isRealMode())) && ((((Aircraft)paramActor).FM instanceof Pilot))) return true;
/*  292 */     return false;
/*      */   }
/*      */   
/*      */   public void cancelMissileGrowl() {
/*  296 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/*  297 */     if (this.fxMissileToneLock != null) {
/*  298 */       this.fxMissileToneLock.setPlay(false);
/*  299 */       this.fxMissileToneLock.cancel();
/*      */     }
/*  301 */     if (this.fxMissileToneNoLock != null) {
/*  302 */       this.fxMissileToneNoLock.setPlay(false);
/*  303 */       this.fxMissileToneNoLock.cancel();
/*      */     }
/*      */   }
/*      */   
/*      */   public void changeMissileClass(Class paramClass) {
/*  308 */     if ((this.iDebugLogLevel & 0x1) == 1)
/*  309 */       System.out.println("changeMissileClass to " + paramClass.getName());
/*  310 */     cancelMissileGrowl();
/*  311 */     this.rocketsList.clear();
/*  312 */     this.myMissileClass = paramClass;
/*  313 */     this.lockTonesInitialized = false;
/*  314 */     createMissileList(this.rocketsList, paramClass);
/*  315 */     setMissileGrowl(0);
/*  316 */     this.iMissileLockState = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void changeMissileGrowl(int paramInt)
/*      */   {
/*  323 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/*  324 */     setMissileGrowl(paramInt);
/*  325 */     switch (paramInt) {
/*      */     case 1: 
/*  327 */       playMissileGrowlLock(false);
/*  328 */       break;
/*      */     
/*      */     case 2: 
/*  331 */       playMissileGrowlLock(true);
/*  332 */       break;
/*      */     
/*      */     default: 
/*  335 */       cancelMissileGrowl();
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */   private void checkAIlaunchMissile()
/*      */   {
/*  342 */     if (!(this.missileOwner instanceof Aircraft)) return;
/*  343 */     if ((!this.attackDecisionByAI) && (!this.attackDecisionByWaypoint)) return;
/*  344 */     Autopilotage localAutopilotage = ((Aircraft)this.missileOwner).FM.AP;
/*  345 */     if ((this.attackDecisionByWaypoint) && ((localAutopilotage.way.curr().Action != 3) || (localAutopilotage.way.curr().getTarget() == null))) return;
/*  346 */     Aircraft localAircraft = (Aircraft)this.missileOwner;
/*  347 */     if ((((localAircraft.FM instanceof RealFlightModel)) && (((RealFlightModel)localAircraft.FM).isRealMode())) || (!(localAircraft.FM instanceof Pilot))) return;
/*  348 */     if (this.rocketsList.isEmpty()) { return;
/*      */     }
/*      */     
/*  351 */     Pilot localPilot = (Pilot)localAircraft.FM;
/*      */     
/*      */ 
/*  354 */     if (localPilot.target != null) {
/*  355 */       this.trgtAI = localPilot.target.actor;
/*  356 */       if ((this.iDebugLogLevel & 0x3) == 3) {
/*  357 */         System.out.println("AI is targetting victim " + this.trgtAI.hashCode());
/*      */       }
/*  359 */       checkAllActiveMissilesValidity();
/*  360 */       for (int i = 0; i < getActiveMissilesSize(); i++) {
/*  361 */         ActiveMissile localActiveMissile = getActiveMissile(i);
/*  362 */         if ((this.iDebugLogLevel & 0x3) == 3) {
/*  363 */           System.out.println("AI isAI=" + localActiveMissile.isAI() + " owner army=" + localAircraft.FM.actor.getArmy() + " missily army=" + localActiveMissile.getOwnerArmy() + " victim=" + localActiveMissile.getVictim().hashCode() + " theTarget1=" + this.trgtAI.hashCode());
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  368 */         if ((localActiveMissile.isAI()) && 
/*  369 */           (localAircraft.getArmy() == localActiveMissile.getOwnerArmy()) && 
/*  370 */           (localActiveMissile.getVictim() == this.trgtAI))
/*      */         {
/*  372 */           this.trgtAI = null;
/*  373 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  379 */       this.trgtAI = null;
/*      */     }
/*      */     
/*  382 */     if (this.trgtAI == null) {
/*  383 */       this.trgtAI = getMissileTarget();
/*  384 */       if ((this.iDebugLogLevel & 0x3) == 3)
/*  385 */         System.out.println("AI getMissileTarget victim=" + (getMissileTarget() == null ? 0 : getMissileTarget().hashCode()));
/*      */     }
/*  387 */     if ((this.trgtAI != null) && 
/*  388 */       (localAircraft.getArmy() == this.trgtAI.getArmy())) {
/*  389 */       this.trgtAI = null;
/*  390 */       this.trgtPk = 0.0F;
/*  391 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  397 */     if ((this.targetType & 1L) != 0L) {
/*  398 */       if ((Actor.isValid(this.trgtAI)) && (((this.trgtAI instanceof Aircraft)) || (((this.trgtAI instanceof RocketryRocket)) && (!((RocketryRocket)this.trgtAI).isOnRamp())) || (((this.trgtAI instanceof MissileInterceptable)) && (((MissileInterceptable)this.trgtAI).isReleased())))) {
/*  399 */         setMissileTarget(this.trgtAI);
/*  400 */         this.trgtPk = getMissilePk();
/*      */       } else {
/*  402 */         this.trgtPk = 0.0F;
/*      */       }
/*      */       
/*      */     }
/*  406 */     else if (((this.targetType & 0x10) != 0L) || ((this.targetType & 0x100) != 0L)) {
/*  407 */       if (Actor.isValid(this.trgtAI)) {
/*  408 */         setMissileTarget(this.trgtAI);
/*  409 */         this.trgtPk = getMissilePk();
/*      */       } else {
/*  411 */         this.trgtPk = 0.0F;
/*      */       }
/*      */       
/*      */     }
/*  415 */     else if ((this.targetType & 0x20) != 0L) {
/*  416 */       setMissileTarget(this.trgtAI);
/*  417 */       this.trgtPk = getMissilePk();
/*      */     }
/*      */     
/*  420 */     if ((localAircraft.FM.AP instanceof AutopilotAI)) {
/*  421 */       ((AutopilotAI)localAircraft.FM.AP).setOverrideMissileControl(localAircraft.FM.CT, false);
/*      */     }
/*      */     
/*      */ 
/*  425 */     if ((this.trgtPk > getMinPkForAttack()) && ((this.iDetectorMode == 2) || (this.iDetectorMode == 3) || (this.iDetectorMode == 4)) && (Actor.isValid(getMissileTarget())) && ((getMissileTarget() instanceof com.maddox.il2.objects.air.TypeRadarWarningReceiver)) && (getMissileTarget().getArmy() != localAircraft.FM.actor.getArmy()))
/*      */     {
/*  427 */       ((com.maddox.il2.objects.air.TypeRadarWarningReceiver)getMissileTarget()).myRadarLockYou(localAircraft.FM.actor, Property.stringValue(this.myMissileClass, "soundRadarPW", null));
/*      */     }
/*  429 */     if ((this.trgtPk > getMinPkForAttack()) && ((this.iDetectorMode == 9) || (this.iDetectorMode == 10)) && (getMissileTargetPos() != null) && (Time.current() > this.tMissilePrev + getMillisecondsBetweenMissileLaunchAI()) && (noLaunchSince(1000L, localAircraft.FM.actor.getArmy())) && (missilesLeft(localAircraft.FM.CT.Weapons[localAircraft.FM.CT.rocketHookSelected])))
/*      */     {
/*      */ 
/*  432 */       this.tMissilePrev = Time.current();
/*      */       
/*  434 */       localAircraft.FM.CT.WeaponControl[localAircraft.FM.CT.rocketHookSelected] = true;
/*  435 */       if ((localAircraft.FM.AP instanceof AutopilotAI)) {
/*  436 */         ((AutopilotAI)localAircraft.FM.AP).setOverrideMissileControl(localAircraft.FM.CT, true);
/*      */       }
/*  438 */       if ((this.iDebugLogLevel & 0x3) == 3) {
/*  439 */         System.out.println("Owner " + localAircraft.hashCode() + " missile launch against victim=" + getMissileTarget().hashCode() + " (" + getMissileTarget().getClass().getName() + ")");
/*      */       }
/*      */     }
/*  442 */     else if ((this.trgtPk > getMinPkForAttack()) && (Actor.isValid(getMissileTarget())) && (getMissileTarget().getArmy() != localAircraft.FM.actor.getArmy()) && (Time.current() > this.tMissilePrev + getMillisecondsBetweenMissileLaunchAI()) && (noLaunchSince(1000L, localAircraft.FM.actor.getArmy())) && (missilesLeft(localAircraft.FM.CT.Weapons[localAircraft.FM.CT.rocketHookSelected])))
/*      */     {
/*      */ 
/*  445 */       if (isTargetHandledByAi(localAircraft.FM.actor.getArmy(), getMissileTarget())) return;
/*  446 */       addTargetHandledByAi(localAircraft.FM.actor.getArmy(), getMissileTarget());
/*  447 */       this.tMissilePrev = Time.current();
/*      */       
/*  449 */       localAircraft.FM.CT.WeaponControl[localAircraft.FM.CT.rocketHookSelected] = true;
/*  450 */       if ((localAircraft.FM.AP instanceof AutopilotAI)) {
/*  451 */         ((AutopilotAI)localAircraft.FM.AP).setOverrideMissileControl(localAircraft.FM.CT, true);
/*      */       }
/*  453 */       if ((this.iDebugLogLevel & 0x3) == 3) {
/*  454 */         System.out.println("Owner " + localAircraft.hashCode() + " missile launch against victim=" + getMissileTarget().hashCode() + " (" + getMissileTarget().getClass().getName() + ")");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void checkLockStatus() {
/*  460 */     int i = this.iMissileLockState;
/*  461 */     int j = 0;
/*  462 */     int k = 0;
/*  463 */     int m = 0;
/*      */     try {
/*  465 */       if (((Aircraft)this.missileOwner).FM.CT.BrakeControl == 1.0F) {
/*  466 */         if (!this.oldBreakControl) {
/*  467 */           this.oldBreakControl = true;
/*  468 */           if (!((Aircraft)this.missileOwner).FM.Gears.onGround()) {
/*  469 */             this.engageMode -= 1;
/*  470 */             getClass(); if (this.engageMode < -1) {
/*  471 */               getClass();this.engageMode = 1;
/*      */             }
/*  473 */             switch (this.engageMode) {
/*      */             case -1: 
/*  475 */               if (this.missileName != null)
/*      */               {
/*  477 */                 LocalLog(this.missileOwner, this.missileName + " Engagement OFF");
/*      */               }
/*  479 */               break;
/*      */             case 0: 
/*  481 */               if (this.missileName != null)
/*      */               {
/*  483 */                 LocalLog(this.missileOwner, this.missileName + " Engagement AUTO");
/*      */               }
/*  485 */               break;
/*      */             case 1: 
/*  487 */               if (this.missileName != null)
/*      */               {
/*  489 */                 LocalLog(this.missileOwner, this.missileName + " Engagement ON");
/*      */               }
/*      */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */       } else {
/*  496 */         this.oldBreakControl = false;
/*      */       }
/*      */     }
/*      */     catch (Exception localException1) {}
/*      */     try {
/*  501 */       if (this.missileOwner != World.getPlayerAircraft()) {
/*  502 */         if (this.iMissileLockState != 0) {
/*  503 */           changeMissileGrowl(0);
/*  504 */           this.iMissileLockState = 0;
/*      */         }
/*  506 */         return;
/*      */       }
/*      */       
/*  509 */       if (!hasMissiles()) {
/*  510 */         if (this.iMissileLockState != 0) {
/*  511 */           changeMissileGrowl(0);
/*  512 */           LocalLog(this.missileOwner, this.missileName + " missiles depleted");
/*  513 */           this.iMissileLockState = 0;
/*      */         }
/*  515 */         return;
/*      */       }
/*  517 */       getClass(); if (this.engageMode == -1) {
/*  518 */         if (this.iMissileLockState != 0) {
/*  519 */           changeMissileGrowl(0);
/*  520 */           LocalLog(this.missileOwner, this.missileName + " disengaged");
/*  521 */           this.iMissileLockState = 0;
/*      */         }
/*  523 */         return;
/*      */       }
/*      */       
/*  526 */       if (((this.iDetectorMode == 9) || (this.iDetectorMode == 10)) && (this.trgtPosMissile != null)) {
/*  527 */         k = 1;
/*  528 */         j = 1;
/*      */       }
/*  530 */       else if (Actor.isValid(this.trgtMissile)) {
/*  531 */         k = 1;
/*  532 */         if (this.trgtMissile.getArmy() != World.getPlayerAircraft().getArmy()) {
/*  533 */           j = 1;
/*      */         }
/*      */       }
/*      */       
/*  537 */       if ((Actor.isValid(com.maddox.il2.game.Main3D.cur3D().viewActor())) && 
/*  538 */         (com.maddox.il2.game.Main3D.cur3D().viewActor() == this.missileOwner)) {
/*  539 */         Actor localActor = com.maddox.il2.game.Selector.look(true, false, com.maddox.il2.game.Main3D.cur3D()._camera3D[com.maddox.il2.game.Main3D.cur3D().getRenderIndx()], World.getPlayerAircraft().getArmy(), -1, World.getPlayerAircraft(), false);
/*  540 */         if (Actor.isValid(localActor)) {
/*  541 */           j = 1;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  546 */       if (j != 0) {
/*  547 */         this.tLastSeenEnemy = Time.current();
/*      */       }
/*  549 */       else if (Time.current() - this.tLastSeenEnemy > 10000L) {
/*  550 */         m = 1;
/*      */       }
/*      */       
/*      */ 
/*  554 */       if (k != 0) {
/*  555 */         if (j != 0) {
/*  556 */           this.iMissileLockState = 2;
/*      */         } else {
/*  558 */           getClass(); if (this.engageMode == 1) {
/*  559 */             this.iMissileLockState = 2;
/*      */           }
/*  561 */           else if (m != 0) {
/*  562 */             this.iMissileLockState = 0;
/*      */           } else {
/*  564 */             this.iMissileLockState = 2;
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*  569 */       else if (m != 0) {
/*  570 */         this.iMissileLockState = 0;
/*      */       } else {
/*  572 */         this.iMissileLockState = 1;
/*      */       }
/*      */       
/*      */ 
/*  576 */       getClass(); if ((this.engageMode == 1) && (this.iMissileLockState == 0)) {
/*  577 */         this.iMissileLockState = 1;
/*      */       }
/*  579 */       if ((((Aircraft)this.missileOwner).FM.getOverload() > this.fPkMaxG) && (this.iMissileLockState == 2)) {
/*  580 */         this.iMissileLockState = 1;
/*      */       }
/*      */       
/*  583 */       switch (this.iMissileLockState) {
/*      */       case 1: 
/*  585 */         if (i != 1) {
/*  586 */           changeMissileGrowl(1);
/*      */         }
/*  588 */         if (i == 0) {
/*  589 */           LocalLog(this.missileOwner, this.missileName + " engaged");
/*      */         }
/*      */         
/*      */         break;
/*      */       case 2: 
/*  594 */         if (i != 2) {
/*  595 */           changeMissileGrowl(2);
/*      */         }
/*  597 */         if (i == 0) {
/*  598 */           LocalLog(this.missileOwner, this.missileName + " engaged");
/*      */         }
/*      */         
/*      */         break;
/*      */       case 0: 
/*  603 */         if (i != 0) {
/*  604 */           changeMissileGrowl(0);
/*  605 */           LocalLog(this.missileOwner, this.missileName + " disengaged");
/*      */         }
/*      */         
/*      */         break;
/*      */       default: 
/*  610 */         if (i != 0) {
/*  611 */           changeMissileGrowl(0);
/*  612 */           LocalLog(this.missileOwner, this.missileName + " disengaged");
/*      */         }
/*      */         break;
/*      */       }
/*      */     }
/*      */     catch (Exception localException2) {}
/*      */   }
/*      */   
/*      */   private void checkPendingMissiles()
/*      */   {
/*  622 */     if (this.rocketsList.isEmpty()) {
/*  623 */       if (((this.iDebugLogLevel & 0x3) == 3) && 
/*  624 */         (this.missileOwner == World.getPlayerAircraft())) System.out.println("checkPendingMissiles this.rocketsList.isEmpty()=true");
/*  625 */       return;
/*      */     }
/*  627 */     if ((this.rocketsList.get(0) instanceof RocketGunWithDelay)) {
/*  628 */       if (((this.iDebugLogLevel & 0x3) == 3) && 
/*  629 */         (this.missileOwner == World.getPlayerAircraft())) System.out.println("checkPendingMissiles this.rocketsList.get(0) instanceof RocketGunWithDelay=true, hash=" + ((RocketGunWithDelay)this.rocketsList.get(0)).hashCode());
/*  630 */       ((RocketGunWithDelay)this.rocketsList.get(0)).checkPendingWeaponRelease();
/*      */     }
/*  632 */     else if (((this.iDebugLogLevel & 0x3) == 3) && 
/*  633 */       (this.missileOwner == World.getPlayerAircraft())) { System.out.println("checkPendingMissiles this.rocketsList.get(0) instanceof RocketGunWithDelay=false, hash=" + this.rocketsList.get(0).hashCode());
/*      */     }
/*      */   }
/*      */   
/*      */   public void createMissileList(ArrayList paramArrayList) {
/*  638 */     createMissileList(paramArrayList, null);
/*      */   }
/*      */   
/*      */   public void createMissileList(ArrayList paramArrayList, Class paramClass)
/*      */   {
/*  643 */     Aircraft localAircraft = (Aircraft)this.missileOwner;
/*      */     try {
/*  645 */       for (int i = 0; i < localAircraft.FM.CT.Weapons.length; i++) {
/*  646 */         if (localAircraft.FM.CT.Weapons[i] != null) {
/*  647 */           for (int j = 0; j < localAircraft.FM.CT.Weapons[i].length; j++) {
/*  648 */             if ((localAircraft.FM.CT.Weapons[i][j] != null) && 
/*  649 */               ((localAircraft.FM.CT.Weapons[i][j] instanceof RocketGun))) {
/*  650 */               RocketGun localRocketGun = (RocketGun)localAircraft.FM.CT.Weapons[i][j];
/*  651 */               if (localRocketGun.haveBullets()) {
/*  652 */                 Class localClass = localRocketGun.bulletClass();
/*  653 */                 if ((paramClass == null) || 
/*  654 */                   (localClass.getName().equals(paramClass.getName())))
/*      */                 {
/*      */ 
/*      */ 
/*  658 */                   if (Missile.class.isAssignableFrom(localClass)) {
/*  659 */                     if (paramClass == null) {
/*  660 */                       paramClass = localClass;
/*      */                     }
/*  662 */                     paramArrayList.add(localAircraft.FM.CT.Weapons[i][j]);
/*      */                   }
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (Exception localException) {
/*  671 */       EventLog.type("Exception in initParams: " + localException.getMessage());
/*      */     }
/*  673 */     if (paramClass == null) return;
/*  674 */     getMissileProperties(paramClass);
/*      */   }
/*      */   
/*      */   private int engineHeatSpreadType(Actor paramActor) {
/*  678 */     if ((paramActor instanceof RocketryRocket)) return HEAT_SPREAD_AFT;
/*  679 */     if ((paramActor instanceof MissileInterceptable)) return HEAT_SPREAD_AFT;
/*  680 */     if (!(paramActor instanceof Aircraft)) return HEAT_SPREAD_360;
/*  681 */     com.maddox.il2.fm.EnginesInterface localEnginesInterface = ((SndAircraft)paramActor).FM.EI;
/*  682 */     int i = HEAT_SPREAD_NONE;
/*  683 */     for (int j = 0; j < localEnginesInterface.getNum(); j++) {
/*  684 */       int k = localEnginesInterface.engines[j].getType();
/*  685 */       if ((k == 2) || (k == 3) || (k == 4) || (k == 6)) {
/*  686 */         i |= HEAT_SPREAD_AFT;
/*      */       }
/*  688 */       if ((k == 0) || (k == 1) || (k == 7) || (k == 8)) {
/*  689 */         i |= HEAT_SPREAD_360;
/*      */       }
/*      */     }
/*  692 */     return i;
/*      */   }
/*      */   
/*      */   private int engineHeatSpreadType(Motor paramMotor) {
/*  696 */     int i = HEAT_SPREAD_NONE;
/*  697 */     int j = paramMotor.getType();
/*  698 */     if ((j == 2) || (j == 3) || (j == 4) || (j == 6)) {
/*  699 */       i |= HEAT_SPREAD_AFT;
/*      */     }
/*  701 */     if ((j == 0) || (j == 1) || (j == 7) || (j == 8)) {
/*  702 */       i |= HEAT_SPREAD_360;
/*      */     }
/*  704 */     return i;
/*      */   }
/*      */   
/*      */   public boolean getAttackDecisionByAI() {
/*  708 */     return this.attackDecisionByAI;
/*      */   }
/*      */   
/*      */   public boolean getAttackDecisionByWaypoint() {
/*  712 */     return this.attackDecisionByWaypoint;
/*      */   }
/*      */   
/*      */   public int getDetectorType()
/*      */   {
/*  717 */     return this.iDetectorMode;
/*      */   }
/*      */   
/*      */   public boolean getCanTrackSubs() {
/*  721 */     return this.canTrackSubs;
/*      */   }
/*      */   
/*      */   public SoundFX getFxMissileToneLock() {
/*  725 */     return this.fxMissileToneLock;
/*      */   }
/*      */   
/*      */   public SoundFX getFxMissileToneNoLock() {
/*  729 */     return this.fxMissileToneNoLock;
/*      */   }
/*      */   
/*      */   public float getLeadPercent() {
/*  733 */     return this.fLeadPercent;
/*      */   }
/*      */   
/*      */   public float getMaxDistance() {
/*  737 */     return this.fMaxDistance;
/*      */   }
/*      */   
/*      */   public float getMaxG() {
/*  741 */     return this.fMaxG;
/*      */   }
/*      */   
/*      */   public float getMaxPOVfrom() {
/*  745 */     return this.fMaxPOVfrom;
/*      */   }
/*      */   
/*      */   public float getMaxPOVto() {
/*  749 */     return this.fMaxPOVto;
/*      */   }
/*      */   
/*      */   public long getMillisecondsBetweenMissileLaunchAI() {
/*  753 */     return this.millisecondsBetweenMissileLaunchAI;
/*      */   }
/*      */   
/*      */   public float getMinPkForAttack() {
/*  757 */     return this.minPkForAttack;
/*      */   }
/*      */   
/*      */   public Missile getMissileFromRocketGun(RocketGun paramRocketGun) {
/*  761 */     return (Missile)paramRocketGun.rocket;
/*      */   }
/*      */   
/*      */   public int getMissileGrowl() {
/*  765 */     return this.iMissileTone;
/*      */   }
/*      */   
/*      */   public int getMissileLockState() {
/*  769 */     return this.iMissileLockState;
/*      */   }
/*      */   
/*      */   public float getMissileMaxSpeedKmh() {
/*  773 */     return this.fMissileMaxSpeedKmh;
/*      */   }
/*      */   
/*      */   public String getMissileName() {
/*  777 */     return this.missileName;
/*      */   }
/*      */   
/*      */   public Actor getMissileOwner() {
/*  781 */     return this.missileOwner;
/*      */   }
/*      */   
/*      */   private float getMissilePk() {
/*  785 */     float f = 0.0F;
/*  786 */     if ((this.iDetectorMode == 9) || (this.iDetectorMode == 10)) {
/*  787 */       if (getMissileTargetPos() != null) {
/*  788 */         f = Pk(this.missileOwner, getMissileTargetPos());
/*      */       }
/*  790 */     } else if (Actor.isValid(getMissileTarget())) {
/*  791 */       f = Pk(this.missileOwner, getMissileTarget());
/*      */     }
/*      */     
/*  794 */     return f;
/*      */   }
/*      */   
/*      */   public void getMissileProperties(Class paramClass) {
			   this.MyotkaFOV = Property.floatValue(paramClass, "typeMyotka",0);
/*  798 */     this.fPkMaxG = Property.floatValue(paramClass, "maxLockGForce", 99.9F);
/*  799 */     this.fMaxPOVfrom = Property.floatValue(paramClass, "maxFOVfrom", 99.9F);
/*  800 */     this.fMaxPOVto = Property.floatValue(paramClass, "maxFOVto", 99.9F);
/*  801 */     this.fPkMaxAngle = Property.floatValue(paramClass, "PkMaxFOVfrom", 99.9F);
/*  802 */     this.fPkMaxAngleAft = Property.floatValue(paramClass, "PkMaxFOVto", 99.9F);
/*  803 */     this.fPkMinDist = Property.floatValue(paramClass, "PkDistMin", 99.9F);
/*  804 */     this.fPkOptDist = Property.floatValue(paramClass, "PkDistOpt", 99.9F);
/*  805 */     this.fPkMaxDist = Property.floatValue(paramClass, "PkDistMax", 99.9F);
/*  806 */     this.fMissileMaxSpeedKmh = Property.floatValue(paramClass, "maxSpeed", 99.9F);
/*  807 */     this.fLeadPercent = Property.floatValue(paramClass, "leadPercent", 99.9F);
/*  808 */     this.fMaxG = Property.floatValue(paramClass, "maxGForce", 99.9F);
/*  809 */     this.iDetectorMode = Property.intValue(paramClass, "detectorType", 0);
/*  810 */     this.attackDecisionByAI = (Property.intValue(paramClass, "attackDecisionByAI", 0) == 1);
/*  811 */     this.attackDecisionByWaypoint = (Property.intValue(paramClass, "attackDecisionByAI", 0) == 2);
/*  812 */     this.canTrackSubs = (Property.intValue(paramClass, "canTrackSubs", 0) != 0);
/*  813 */     this.multiTrackingCapable = (Property.intValue(paramClass, "multiTrackingCapable", 0) != 0);
/*  814 */     this.minPkForAttack = Property.floatValue(paramClass, "minPkForAI", 25.0F);
/*  815 */     this.millisecondsBetweenMissileLaunchAI = Property.longValue(paramClass, "timeForNextLaunchAI", 10000L);
/*  816 */     this.targetType = Property.longValue(paramClass, "targetType", 1L);
/*  817 */     this.fSunBrightThreshold = Property.floatValue(paramClass, "sunBrightThreshold", 0.03F);
/*  818 */     this.missileName = Property.stringValue(paramClass, "friendlyName", "Missile");
/*  819 */     this.myMissileClass = paramClass;
/*  820 */     initLockTones();
/*  821 */     this.iDebugLogLevel = com.maddox.il2.engine.Config.cur.ini.get("Mods", "GuidedMissileDebugLog", 0);
/*  822 */     this.bRealisticRadarSelect = (com.maddox.il2.engine.Config.cur.ini.get("Mods", "RealisticRadarSelect", 0) != 0);
/*      */   }
/*      */   
/*      */   public Actor getMissileTarget() {
/*  826 */     return this.trgtMissile;
/*      */   }
/*      */   
/*      */   public Point3d getMissileTargetPos() {
/*  830 */     return this.trgtPosMissile;
/*      */   }
/*      */   
/*      */   public Actor getMissileTargetPosOwner() {
/*  834 */     return this.trgtPosOwner;
/*      */   }
/*      */   
/*      */   public Point3f getMissileTargetOffset() {
/*  838 */     return getSelectedActorOffset();
/*      */   }
/*      */   
/*      */   public boolean getMultiTrackingCapable() {
/*  842 */     return this.multiTrackingCapable;
/*      */   }
/*      */   
/*      */   public float getPkMaxAngle() {
/*  846 */     return this.fPkMaxAngle;
/*      */   }
/*      */   
/*      */   public float getPkMaxAngleAft() {
/*  850 */     return this.fPkMaxAngleAft;
/*      */   }
/*      */   
/*      */   public float getPkMaxDist() {
/*  854 */     return this.fPkMaxDist;
/*      */   }
/*      */   
/*      */   public float getPkMaxG() {
/*  858 */     return this.fPkMaxG;
/*      */   }
/*      */   
/*      */   public float getPkMinDist() {
/*  862 */     return this.fPkMinDist;
/*      */   }
/*      */   
/*      */   public float getPkOptDist() {
/*  866 */     return this.fPkOptDist;
/*      */   }
/*      */   
/*      */   public Point3f getSelectedActorOffset() {
/*  870 */     return this.selectedActorOffset;
/*      */   }
/*      */   
/*      */   public Sample getSmplMissileLock() {
/*  874 */     return this.smplMissileLock;
/*      */   }
/*      */   
/*      */   public Sample getSmplMissileNoLock() {
/*  878 */     return this.smplMissileNoLock;
/*      */   }
/*      */   
/*      */   public long getStartLastMissile() {
/*  882 */     return this.tStartLastMissile;
/*      */   }
/*      */   
/*      */   public float getStepsForFullTurn() {
/*  886 */     return this.fStepsForFullTurn;
/*      */   }
/*      */   
/*      */   public long getTargetType() {
/*  890 */     return this.targetType;
/*      */   }
/*      */   
/*      */   public boolean hasMissiles() {
/*  894 */     return !this.rocketsList.isEmpty();
/*      */   }
/*      */   
/*      */   private void initCommon() {
/*  898 */     this.selectedActorOffset = new Point3f();
/*  899 */     getClass();this.engageMode = 0;
/*  900 */     this.iMissileLockState = 0;
/*  901 */     this.iMissileTone = 0;
/*  902 */     this.tLastSeenEnemy = (Time.current() - 20000L);
/*  903 */     this.oldBreakControl = true;
/*  904 */     this.rocketsList = new ArrayList();
/*  905 */     this.tMissilePrev = 0L;
/*  906 */     this.attackDecisionByAI = false;
/*  907 */     this.attackDecisionByWaypoint = false;
/*  908 */     this.minPkForAttack = 25.0F;
/*  909 */     this.millisecondsBetweenMissileLaunchAI = 10000L;
/*  910 */     this.trgtPosMissile = new Point3d();
/*      */   }
/*      */   
/*      */   private void initLockTones() {
/*  914 */     if (this.lockTonesInitialized) return;
/*  915 */     if (this.myMissileClass == null) return;
/*  916 */     if (this.missileOwner == World.getPlayerAircraft()) {
/*  917 */       setFxMissileToneLock(Property.stringValue(this.myMissileClass, "fxLock", null), Property.floatValue(this.myMissileClass, "fxLockVolume", 1.0F));
/*  918 */       setFxMissileToneNoLock(Property.stringValue(this.myMissileClass, "fxNoLock", null), Property.floatValue(this.myMissileClass, "fxNoLockVolume", 1.0F));
/*  919 */       setSmplMissileLock(Property.stringValue(this.myMissileClass, "smplLock", null));
/*  920 */       setSmplMissileNoLock(Property.stringValue(this.myMissileClass, "smplNoLock", null));
/*      */     }
/*  922 */     this.lockTonesInitialized = true;
/*  923 */     if ((this.iDebugLogLevel & 0x3) == 3)
/*  924 */       System.out.println("initLockTones finished");
/*      */   }
/*      */   
/*      */   private void initParams(Actor paramActor) {
/*  928 */     if ((paramActor instanceof Aircraft)) {
/*  929 */       this.missileOwner = paramActor;
/*      */     }
/*  931 */     initCommon();
/*      */   }
/*      */   
/*      */ 
/*      */   private void initParams(Actor paramActor, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, float paramFloat10, float paramFloat11, float paramFloat12, float paramFloat13, float paramFloat14, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString1, String paramString2, String paramString3, String paramString4)
/*      */   {
/*  937 */     initCommon();
/*  938 */     this.missileOwner = paramActor;
/*  939 */     this.fMissileMaxSpeedKmh = paramFloat1;
/*  940 */     this.fLeadPercent = paramFloat2;
/*  941 */     this.fMaxG = paramFloat3;
/*  942 */     this.fStepsForFullTurn = paramFloat4;
/*  943 */     this.fPkMaxAngle = paramFloat5;
/*  944 */     this.fPkMaxAngleAft = paramFloat6;
/*  945 */     this.fPkMinDist = paramFloat7;
/*  946 */     this.fPkOptDist = paramFloat8;
/*  947 */     this.fPkMaxDist = paramFloat9;
/*  948 */     this.fPkMaxG = paramFloat10;
/*  949 */     this.fMaxPOVfrom = paramFloat11;
/*  950 */     this.fMaxPOVto = paramFloat12;
/*  951 */     this.fMaxDistance = paramFloat13;
/*  952 */     this.attackDecisionByAI = paramBoolean1;
/*  953 */     this.minPkForAttack = paramFloat14;
/*  954 */     this.millisecondsBetweenMissileLaunchAI = paramLong1;
/*  955 */     this.targetType = paramLong2;
/*  956 */     this.canTrackSubs = paramBoolean3;
/*  957 */     this.multiTrackingCapable = paramBoolean2;
/*  958 */     if (paramString1 == null) {
/*  959 */       this.fxMissileToneLock = null;
/*      */     } else {
/*  961 */       this.fxMissileToneLock = this.missileOwner.newSound(paramString1, false);
/*      */     }
/*  963 */     if (paramString2 == null) {
/*  964 */       this.fxMissileToneNoLock = null;
/*      */     } else {
/*  966 */       this.fxMissileToneNoLock = this.missileOwner.newSound(paramString2, false);
/*      */     }
/*  968 */     if (paramString3 == null) {
/*  969 */       this.smplMissileLock = null;
/*      */     } else {
/*  971 */       this.smplMissileLock = new Sample(paramString3, 256, 65535);
/*  972 */       this.smplMissileLock.setInfinite(true);
/*      */     }
/*  974 */     if (paramString4 == null) {
/*  975 */       this.smplMissileNoLock = null;
/*      */     } else {
/*  977 */       this.smplMissileNoLock = new Sample(paramString4, 256, 65535);
/*  978 */       this.smplMissileNoLock.setInfinite(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public Actor lookForGuidedMissileTarget(Actor paramActor, float paramFloat1, float paramFloat2, double paramDouble) {
/*  983 */     return lookForGuidedMissileTargetAircraft(paramActor, paramFloat1, paramFloat2, paramDouble);
/*      */   }
/*      */   
/*      */   public Actor lookForGuidedMissileTarget(Actor paramActor, float paramFloat1, float paramFloat2, double paramDouble, long paramLong) {
/*  987 */     Actor localObject = null;
/*  988 */     if ((paramLong & 1L) != 0L) {
/*  989 */       localObject = lookForGuidedMissileTargetAircraft(paramActor, paramFloat1, paramFloat2, paramDouble);
/*  990 */     } else if (((paramLong & 0x10) != 0L) && ((paramLong & 0x100) != 0L)) {
/*  991 */       Actor localActor1 = lookForGuidedMissileTargetGround(paramActor, paramFloat1, paramFloat2, paramDouble);
/*  992 */       Actor localActor2 = lookForGuidedMissileTargetShip(paramActor, paramFloat1, paramFloat2, paramDouble);
/*  993 */       if ((localActor1 != null) && (localActor2 == null)) localObject = localActor1;
/*  994 */       if ((localActor1 == null) && (localActor2 != null)) localObject = localActor2;
/*  995 */       if ((localActor1 != null) && (localActor2 != null)) {
/*  996 */         double d1 = distanceBetween(paramActor, localActor1);
/*  997 */         double d2 = distanceBetween(paramActor, localActor2);
/*  998 */         if (d1 < d2) localObject = localActor1; else
/*  999 */           localObject = localActor2;
/*      */       }
/* 1001 */     } else if ((paramLong & 0x10) != 0L) {
/* 1002 */       localObject = lookForGuidedMissileTargetGround(paramActor, paramFloat1, paramFloat2, paramDouble);
/* 1003 */     } else if ((paramLong & 0x100) != 0L) {
/* 1004 */       localObject = lookForGuidedMissileTargetShip(paramActor, paramFloat1, paramFloat2, paramDouble);
/* 1005 */     } else if ((paramLong & 0x20) != 0L) {
/* 1006 */       localObject = lookForGuidedMissileTargetLocate(paramActor, paramFloat1, paramFloat2, paramDouble);
/*      */     }



				




/* 1008 */     return localObject;
/*      */   }
/*      */   
/*      */   public Actor lookForGuidedMissileTargetAircraft(Actor paramActor, float paramFloat1, float paramFloat2, double paramDouble) {
/* 1012 */     double d1 = 0.0D;
/* 1013 */     float f1 = 0.0F;
/* 1014 */     float f2 = 0.0F;
/* 1015 */     float f3 = 0.0F;
/* 1016 */     float f4 = 0.0F;
/* 1017 */     Actor localObject1 = null;
/* 1018 */     Point3f localPoint3f = new Point3f(0.0F, 0.0F, 0.0F);
/*      */     
/* 1020 */     if (this.iDetectorMode == 0) { return localObject1;
/*      */     }
/* 1022 */     FlightModel localFlightModel = ((Aircraft)paramActor).FM;
/* 1023 */     Actor localObject2;
               if (/*(this.bRealisticRadarSelect) && */((paramActor instanceof TypeSemiRadar)) && ((this.iDetectorMode == 2) || (this.iDetectorMode == 4)) && ((localFlightModel instanceof RealFlightModel)) && (((RealFlightModel)localFlightModel).isRealMode()) && ((localFlightModel instanceof Pilot)))
/*      */     {
/*      */ 
/* 1026 */       if ((!this.multiTrackingCapable&&(!((TypeSemiRadar)paramActor).getSemiActiveRadarOn())) || (((TypeSemiRadar)paramActor).getSemiActiveRadarLockedActor() == null)) {
/* 1027 */         if ((this.iDebugLogLevel & 0x3) == 3)
/* 1028 */           System.out.println("Semi-Active Radar is OFF. Missile target is made null.");
/* 1029 */         return null;
/*      */       }
/*      */       
/* 1032 */       localObject2 = ((TypeSemiRadar)paramActor).getSemiActiveRadarLockedActor();
/* 1033 */       if (((localObject2 instanceof Aircraft)) || (((localObject2 instanceof RocketryRocket)) && (!((RocketryRocket)localObject2).isOnRamp())) || (((localObject2 instanceof MissileInterceptable)) && (((MissileInterceptable)localObject2).isReleased()))) {
/* 1034 */         d1 = distanceBetween(paramActor, (Actor)localObject2);
/* 1035 */         if (d1 > paramDouble) {
/* 1036 */           return null;
/*      */         }
/* 1038 */         f1 = angleBetween(paramActor, (Actor)localObject2);
/* 1039 */         if (f1 > paramFloat1) {
/* 1040 */           return null;
/*      */         }
/*      */         
/* 1043 */         float f5 = 0.0F;
/* 1044 */         if ((localObject2 instanceof Aircraft)) {
/* 1045 */           com.maddox.il2.fm.Mass localMass1 = ((SndAircraft)localObject2).FM.M;
/* 1046 */           f5 = localMass1.getFullMass();
/* 1047 */         } else if (((localObject2 instanceof RocketryRocket)) || ((localObject2 instanceof MissileInterceptable))) {
/* 1048 */           f5 = Property.floatValue(localObject2.getClass(), "massa", 1000.0F);
/*      */         }
/* 1050 */         f3 = f5 / f1 / (float)(d1 * d1);
/* 1051 */         if (!((Actor)localObject2).isAlive()) {
/* 1052 */           f3 /= 10.0F;
/*      */         }
/* 1054 */         if (f3 > f4) {
/* 1055 */           f4 = f3;
/* 1056 */           localObject1 = localObject2;
/* 1057 */           if ((localObject2 instanceof Aircraft)) {
/* 1058 */             localPoint3f.set(0.0F, 0.0F, 0.0F);
/*      */           }
/* 1060 */           return localObject1;
/*      */         }
/*      */         
/* 1063 */         return null;
/*      */       }
/*      */     }
               int i;
/*      */     /**TODO Type Myotka*/
               
               try
               {
            	   if((this.iDetectorMode == 6 || this.iDetectorMode == 5)&& paramActor instanceof TypeHARM_carrier)
            	   {
            		   return ((TypeHARM_carrier)paramActor).getHarmVictim();
            		   
            	   }
               }
               catch(Exception localException) {
             	  EventLog.type("Exception in selectedActor");
             	  EventLog.type(localException.toString());
             	  EventLog.type(localException.getMessage());
             	}
               
               
               
               try
               {
            	   if(this.iDetectorMode == 1  && paramActor instanceof TypeMyotka && this.MyotkaFOV > 0 && ((localFlightModel instanceof RealFlightModel)) && (((RealFlightModel)localFlightModel).isRealMode()) && ((localFlightModel instanceof Pilot)))
            	   {
            		   
            		   if ( ((TypeMyotka)paramActor).getMyotkaMode() <= 0 )
            		   {
            			   return null;
            		   }
            		   Point3d point3d = ((Actor) (paramActor)).pos.getAbsPoint();
                       Orient orient = ((Actor) (paramActor)).pos.getAbsOrient();
            		   List list = Engine.targets();
            		          i = ((List)list).size();
            		          for (int j = 0; j < i; j++) {
            		            Actor localActor = (Actor)((List)list).get(j);
            		            if (((localActor instanceof Aircraft)) || (((localActor instanceof RocketryRocket)) && (!((RocketryRocket)localActor).isOnRamp())) || (((localActor instanceof MissileInterceptable)) && (((MissileInterceptable)localActor).isReleased())))
            		            {
            		            	Vector3d vector3d = new Vector3d();
            	                    vector3d.set(point3d);
            	                    Point3d point3d1 = new Point3d();
            	                    point3d1.set(localActor.pos.getAbsPoint());
            	                    point3d1.sub(point3d);
            	                    orient.transformInv(point3d1);
            	                    
            	                    
            	                    d1 = distanceBetween(paramActor, localActor);
            	                              if (d1 <= paramDouble)
            	                              {
            	                    
            	                                f1 = angleBetween(paramActor, localActor);
            	                                if (f1 <= paramFloat1)
            	                                {
            	                    
            	                                	float X = ((TypeMyotka)paramActor).getMyotkaAnglesHorizontal();
            	                                	float Y = ((TypeMyotka)paramActor).getMyotkaAnglesVertical();
            	                                	float targetx=0;
            	                                	float targety=0;
            	                                	if(((Tuple3d) (point3d1)).x > 0)
            	                                	{
            	                                		targety = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).z/((Tuple3d) (point3d1)).x));
            	                                		targetx = (float) Math.toDegrees(Math.atan(((Tuple3d) (point3d1)).y/((Tuple3d) (point3d1)).x));
            	                                	}
            	                                	else
            	                                	{
            	                                		if(((Tuple3d) (point3d1)).y != 0)
            	                                		{
            	                                			targety = (float) Math.toDegrees(Math.atan((Math.abs(((Tuple3d) (point3d1)).x))/((Tuple3d) (point3d1)).y));
            	                                			
            	                                			if (targety > 0) targety+=90f; else targety -=90f;
            	                                			
            	                                		}
            	                                		else return null;
            	                                		if(((Tuple3d) (point3d1)).z != 0)
            	                                		{
            	                                			
            	                                			targetx = (float) Math.toDegrees(Math.atan((Math.abs(((Tuple3d) (point3d1)).x))/((Tuple3d) (point3d1)).z));
            	                                			
            	                                			if (targetx > 0) targetx+=90f; else targetx -=90f;
            	                                		}
            	                                		else return null;
            	                                		
            	                                	}
            	                                	
            	                                	targety -= Y;
            	                                	targetx += X;
            	                                	
            	                                	float angle = (float) Math.sqrt(targety*targety + targetx*targetx);
            	                                	if(this.MyotkaFOV < angle) return null;
            	                                	
            	                                	
            	                                	
            	                                  f2 = 180.0F - angleBetween(localActor, paramActor);
            	                                  if ((f2 <= paramFloat2) || 
            	                                    (this.iDetectorMode != 1) || 
            	                                    ((engineHeatSpreadType(localActor) & HEAT_SPREAD_360) != 0))
            	                                  {
            	                                    float f6;
            	                                    

                                                    f6 = 0.0F;
                                                    int k = 0;
                                                    com.maddox.il2.fm.EnginesInterface localEnginesInterface = null;
                                                    if ((localActor instanceof Aircraft)) {
                                                      localEnginesInterface = ((SndAircraft)localActor).FM.EI;
                                                      int m = localEnginesInterface.getNum();
                                                      for (int n = 0; n < m; n++) {
                                                        Motor localMotor = localEnginesInterface.engines[n];
                                                        float f7 = localMotor.getEngineForce().length();
                                                        if (engineHeatSpreadType(localMotor) == HEAT_SPREAD_NONE) {
                                                          f7 = 0.0F;
                                                        }
                                                        if (engineHeatSpreadType(localMotor) == HEAT_SPREAD_360) {
                                                          f7 /= 10.0F;
                                                        }
                                                        if (f7 > f6) {
                                                          f6 = f7;
                                                          k = n;
                                                        }
                                                      }
                                                    } else if (((localActor instanceof RocketryRocket)) || ((localActor instanceof MissileInterceptable))) {
                                                      f6 = Property.floatValue(localActor.getClass(), "force", 1000.0F);
                                                    }
                                                    f3 = f6 / f1 / (float)(d1 * d1);
                                                    if (!localActor.isAlive()) {
                                                      f3 /= 10.0F;
                                                    }
                                                    
                                  
                                  
                                  
                                  
                                  
                                  
                                  
                                  
                                  
                                  
                                  
                                                    if (f3 > f4) {
                                                      f4 = f3;
                                                      localObject1 = localActor;
                                                      if ((localActor instanceof Aircraft)) {
                                                        localPoint3f = localEnginesInterface.engines[k].getEnginePos();
                                                      }
                                                    }
            	                                    
            	                                    
            	                    }
            	                    }
            	                    }
            	                    
            		            }
            		            
            		            
            		            
            		          }
            	   }
               }
               catch(Exception localException) {
            	  EventLog.type("Exception in selectedActor");
            	  EventLog.type(localException.toString());
            	  EventLog.type(localException.getMessage());
            	}
               
               
               
/*      */   
/*      */     try
/*      */     {
/* 1070 */       List list = Engine.targets();
/* 1071 */       i = ((List)list).size();
/* 1072 */       for (int j = 0; j < i; j++) {
/* 1073 */         Actor localActor = (Actor)((List)list).get(j);
/* 1074 */         if (((localActor instanceof Aircraft)) || (((localActor instanceof RocketryRocket)) && (!((RocketryRocket)localActor).isOnRamp())) || (((localActor instanceof MissileInterceptable)) && (((MissileInterceptable)localActor).isReleased()))) {
/* 1075 */           d1 = distanceBetween(paramActor, localActor);
/* 1076 */           if (d1 <= paramDouble)
/*      */           {
/*      */ 
/* 1079 */             f1 = angleBetween(paramActor, localActor);
/* 1080 */             if (f1 <= paramFloat1)
/*      */             {
/*      */ 
/* 1083 */               f2 = 180.0F - angleBetween(localActor, paramActor);
/* 1084 */               if ((f2 <= paramFloat2) || 
/* 1085 */                 (this.iDetectorMode != 1) || 
/* 1086 */                 ((engineHeatSpreadType(localActor) & HEAT_SPREAD_360) != 0))
/*      */               {
/*      */                 float f6;
/*      */                 
/*      */ 
/*      */ 
/* 1092 */                 switch (this.iDetectorMode) {
/*      */                 case 1: 
/* 1094 */                   f6 = 0.0F;
/* 1095 */                   int k = 0;
/* 1096 */                   com.maddox.il2.fm.EnginesInterface localEnginesInterface = null;
/* 1097 */                   if ((localActor instanceof Aircraft)) {
/* 1098 */                     localEnginesInterface = ((SndAircraft)localActor).FM.EI;
/* 1099 */                     int m = localEnginesInterface.getNum();
/* 1100 */                     for (int n = 0; n < m; n++) {
/* 1101 */                       Motor localMotor = localEnginesInterface.engines[n];
/* 1102 */                       float f7 = localMotor.getEngineForce().length();
/* 1103 */                       if (engineHeatSpreadType(localMotor) == HEAT_SPREAD_NONE) {
/* 1104 */                         f7 = 0.0F;
/*      */                       }
/* 1106 */                       if (engineHeatSpreadType(localMotor) == HEAT_SPREAD_360) {
/* 1107 */                         f7 /= 10.0F;
/*      */                       }
/* 1109 */                       if (f7 > f6) {
/* 1110 */                         f6 = f7;
/* 1111 */                         k = n;
/*      */                       }
/*      */                     }
/* 1114 */                   } else if (((localActor instanceof RocketryRocket)) || ((localActor instanceof MissileInterceptable))) {
/* 1115 */                     f6 = Property.floatValue(localActor.getClass(), "force", 1000.0F);
/*      */                   }
/* 1117 */                   f3 = f6 / f1 / (float)(d1 * d1);
/* 1118 */                   if (!localActor.isAlive()) {
/* 1119 */                     f3 /= 10.0F;
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1133 */                   if (f3 > f4) {
/* 1134 */                     f4 = f3;
/* 1135 */                     localObject1 = localActor;
/* 1136 */                     if ((localActor instanceof Aircraft)) {
/* 1137 */                       localPoint3f = localEnginesInterface.engines[k].getEnginePos();
/*      */                     }
/*      */                   }
/*      */                   
/*      */                   break;
/*      */                 case 2: 
/*      */                 case 3: 
/*      */                 case 4: 
/* 1145 */                   f6 = 0.0F;
/* 1146 */                   if ((localActor instanceof Aircraft)) {
/* 1147 */                     com.maddox.il2.fm.Mass localMass2 = ((SndAircraft)localActor).FM.M;
/* 1148 */                     f6 = localMass2.getFullMass();
/* 1149 */                   } else if (((localActor instanceof RocketryRocket)) || ((localActor instanceof MissileInterceptable))) {
/* 1150 */                     f6 = Property.floatValue(localActor.getClass(), "massa", 1000.0F);
/*      */                   }
/* 1152 */                   f3 = f6 / f1 / (float)(d1 * d1);
/* 1153 */                   if (!localActor.isAlive()) {
/* 1154 */                     f3 /= 10.0F;
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1178 */                   if (f3 > f4) {
/* 1179 */                     f4 = f3;
/* 1180 */                     localObject1 = localActor;
/* 1181 */                     if ((localActor instanceof Aircraft))
/*      */                     {
/*      */ 
/*      */ 
/*      */ 
/* 1186 */                       localPoint3f.set(0.0F, 0.0F, 0.0F);
/*      */                     }
/*      */                   }
/*      */                   break;
/*      */                 }
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException) {
/* 1198 */       EventLog.type("Exception in selectedActor");
/* 1199 */       EventLog.type(localException.toString());
/* 1200 */       EventLog.type(localException.getMessage());
/*      */     }
/* 1202 */     if (((this.iDebugLogLevel & 0x3) == 3) && 
/* 1203 */       (localObject1 != null)) { System.out.println("Owner " + paramActor.hashCode() + " selected target " + ((Actor)localObject1).hashCode() + "(" + localObject1.getClass().getName() + ")");
/*      */     }
/*      */     
/* 1206 */     if (((paramActor instanceof Aircraft)) && (localObject1 != null)) {
/* 1207 */       Aircraft localAircraft = (Aircraft)paramActor;
/* 1208 */       if ((!localAircraft.FM.isPlayers()) || (!(localAircraft.FM instanceof RealFlightModel)) || (!((RealFlightModel)localAircraft.FM).isRealMode())) {
/* 1209 */         if ((this.iDebugLogLevel & 0x3) == 3)
/* 1210 */           System.out.println("Active Missiles before check: " + getActiveMissilesSize());
/* 1211 */         checkAllActiveMissilesValidity();
/* 1212 */         if ((this.iDebugLogLevel & 0x3) == 3)
/* 1213 */           System.out.println("Active Missiles after check: " + getActiveMissilesSize());
/* 1214 */         for (i = 0; i < getActiveMissilesSize(); i++) {
/* 1215 */           ActiveMissile localActiveMissile = getActiveMissile(i);
/* 1216 */           if ((this.iDebugLogLevel & 0x3) == 3) {
/* 1217 */             System.out.println("isAI=" + localActiveMissile.isAI() + " owner army=" + localAircraft.FM.actor.getArmy() + " missily army=" + localActiveMissile.getOwnerArmy() + " victim=" + localActiveMissile.getVictim().hashCode() + " selectedActor=" + ((Actor)localObject1).hashCode());
/*      */           }
/*      */           
/*      */ 
/*      */ 
/* 1222 */           if ((localActiveMissile.isAI()) && 
/* 1223 */             (localAircraft.FM.actor.getArmy() == localActiveMissile.getOwnerArmy()) && 
/* 1224 */             (localActiveMissile.getVictim() == localObject1)) {
/* 1225 */             if ((this.iDebugLogLevel & 0x3) == 3)
/* 1226 */               System.out.println("Skipping target " + ((Actor)localObject1).hashCode() + "(" + localObject1.getClass().getName() + ")");
/* 1227 */             localObject1 = null;
/* 1228 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1236 */     this.selectedActorOffset.set(localPoint3f);
/* 1237 */     return localObject1;
/*      */   }
/*      */   
/*      */   public Actor lookForGuidedMissileTargetGround(Actor paramActor, float paramFloat1, float paramFloat2, double paramDouble) {
/* 1241 */     double d1 = 0.0D;
/* 1242 */     float f1 = 0.0F;
/* 1243 */     float f2 = 0.0F;
/* 1244 */     float f3 = 0.0F;
/* 1245 */     Actor localObject1 = null;
/* 1246 */     Point3f localPoint3f = new Point3f(0.0F, 0.0F, 0.0F);
/*      */     
/* 1248 */     if ((!(paramActor instanceof Aircraft)) || (this.iDetectorMode == 0)) { return localObject1;
/*      */     }
/*      */     			   if(this.iDetectorMode == 6|| this.iDetectorMode == 5)    
{
    if(paramActor instanceof TypeHARM_carrier)
	   {
 	   localPoint3f = new Point3f(0.0F, 0.0F, 2.0F);
 	   this.selectedActorOffset.set(localPoint3f);
 	   Actor victim = ((TypeHARM_carrier)paramActor).getHarmVictim();
 	   d1 = distanceBetween(paramActor, victim);
 	               if (d1 <= paramDouble) {return null;}
		   return victim;
	   }}
/* 1251 */     if ((this.iDetectorMode == 7) && ((World.Sun().ToSun.z < this.fSunBrightThreshold) || (((Mission.curCloudsType() > 3) || ((Mission.curCloudsType() > 1) && (World.Sun().ToSun.z < this.fSunBrightThreshold + 0.05F))) && (Mission.curCloudsHeight() > paramActor.pos.getAbsPoint().z))))
/*      */     {
/*      */ 
/*      */ 
/* 1255 */       return localObject1;
/*      */     }
/*      */     
/* 1258 */     if ((this.iDetectorMode == 8) && (Mission.curCloudsType() < 2) && (World.getTimeofDay() > 11.0F) && (World.getTimeofDay() < 13.5F) && (com.maddox.il2.fm.Atmosphere.temperature((float)com.maddox.il2.engine.Engine.land().HQ_Air(paramActor.pos.getAbsPoint().x, paramActor.pos.getAbsPoint().y)) > 308.15F))
/*      */     {
/*      */ 
/*      */ 
/* 1262 */       return localObject1;
/*      */     }
/* 1264 */     FlightModel localFlightModel = ((Aircraft)paramActor).FM;
/* 1265 */     Actor localObject2;
               if (/*(this.bRealisticRadarSelect) && */((paramActor instanceof TypeGroundRadar)) && ((this.iDetectorMode == 7) || (this.iDetectorMode == 8)) && ((localFlightModel instanceof RealFlightModel)) && (((RealFlightModel)localFlightModel).isRealMode()) && ((localFlightModel instanceof Pilot)))
/*      */     {
/*      */ 
/* 1268 */       if ((((TypeGroundRadar)paramActor).getGroundRadarOn()) && (((TypeGroundRadar)paramActor).getGroundRadarLockedActor() != null)) {
/* 1269 */         localObject2 = ((TypeGroundRadar)paramActor).getGroundRadarLockedActor();
/* 1270 */         if ((((localObject2 instanceof com.maddox.il2.ai.ground.TgtFlak)) || ((localObject2 instanceof com.maddox.il2.ai.ground.TgtTank)) || ((localObject2 instanceof com.maddox.il2.ai.ground.TgtTrain)) || ((localObject2 instanceof com.maddox.il2.ai.ground.TgtVehicle))) && ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(((Actor)localObject2).pos.getAbsPoint(), paramActor.pos.getAbsPoint()) >= 1.0F)))
/*      */         {
/* 1272 */           d1 = distanceBetween(paramActor, (Actor)localObject2);
/* 1273 */           f1 = angleBetween(paramActor, (Actor)localObject2);
/* 1274 */           f2 = 1.0F / f1 / (float)(d1 * d1);
/* 1275 */           if (!((Actor)localObject2).isAlive()) {
/* 1276 */             f2 /= 10.0F;
/*      */           }
/* 1278 */           if ((d1 <= paramDouble) && (f1 <= paramFloat1) && (f2 <= f3)) {
/* 1279 */             f3 = f2;
/* 1280 */             localObject1 = localObject2;
/* 1281 */             this.selectedActorOffset.set(localPoint3f);
/* 1282 */             return localObject1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 1290 */       ArrayList list = World.cur().statics.bridges;
/* 1291 */       int i = ((ArrayList)list).size();
/* 1292 */       Object localObject3 = null;
/* 1293 */       com.maddox.il2.objects.bridges.BridgeSegment localBridgeSegment = null;
/* 1294 */       double d2 = paramDouble;
/* 1295 */       for (int j = 0; j < i; j++) {
/* 1296 */         LongBridge localLongBridge = (LongBridge)((ArrayList)list).get(j);
/* 1297 */         if (localLongBridge.isAlive())
/*      */         {
/* 1299 */           if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(localLongBridge.pos.getAbsPoint(), paramActor.pos.getAbsPoint()) >= 1.0F))
/*      */           {
/* 1301 */             d1 = distanceBetween(paramActor, localLongBridge);
/* 1302 */             if (d1 <= paramDouble)
/*      */             {
/*      */ 
/* 1305 */               f1 = angleBetween(paramActor, localLongBridge);
/* 1306 */               if (f1 <= paramFloat1)
/*      */               {
/*      */ 
/* 1309 */                 if (d1 < d2) {
/* 1310 */                   localObject3 = localLongBridge;
/* 1311 */                   d2 = d1;
/*      */                 } }
/*      */             }
/*      */           } } }
/* 1315 */       if (localObject3 != null) {
/* 1316 */         int j = ((LongBridge) localObject3).NumStateBits() / 2;
/* 1317 */         localBridgeSegment = com.maddox.il2.objects.bridges.BridgeSegment.getByIdx(((LongBridge) localObject3).bridgeIdx(), World.Rnd().nextInt(j));
/*      */       }
/*      */       
/* 1320 */       List localList = com.maddox.il2.engine.Engine.targets();
/* 1321 */       int k = localList.size();
/* 1322 */       for (int m = 0; m < k; m++) {
/* 1323 */         Actor localActor = (Actor)localList.get(m);
/* 1324 */         if (((localActor instanceof com.maddox.il2.ai.ground.TgtFlak)) || ((localActor instanceof com.maddox.il2.ai.ground.TgtTank)) || ((localActor instanceof com.maddox.il2.ai.ground.TgtTrain)) || ((localActor instanceof com.maddox.il2.ai.ground.TgtVehicle)))
/*      */         {
/*      */ 
/* 1327 */           if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(localActor.pos.getAbsPoint(), paramActor.pos.getAbsPoint()) >= 1.0F))
/*      */           {
/* 1329 */             d1 = distanceBetween(paramActor, localActor);
/*      */             
/* 1331 */             if (d1 <= paramDouble)
/*      */             {
/*      */ 
/* 1334 */               f1 = angleBetween(paramActor, localActor);
/*      */               
/* 1336 */               if (f1 <= paramFloat1)
/*      */               {
/*      */ 
/*      */ 
/* 1340 */                 f2 = 1.0F / f1 / (float)(d1 * d1);
/* 1341 */                 if (!localActor.isAlive()) {
/* 1342 */                   f2 /= 10.0F;
/*      */                 }
/* 1344 */                 checkAllActiveMissilesValidity();
/* 1345 */                 for (int n = 0; n < getActiveMissilesSize(); n++) {
/* 1346 */                   ActiveMissile localActiveMissile = getActiveMissile(n);
/* 1347 */                   if ((actorIsAI(paramActor)) && 
/* 1348 */                     (paramActor.getArmy() == localActiveMissile.getOwnerArmy()) && 
/* 1349 */                     (localActiveMissile.getVictim() == localActor)) {
/* 1350 */                     f2 = 0.0F;
/* 1351 */                     break;
/*      */                   }
/*      */                   
/*      */ 
/* 1355 */                   if ((this.iDetectorMode != 1) && 
/* 1356 */                     (!this.multiTrackingCapable) && 
/* 1357 */                     (localActiveMissile.getOwner() == paramActor) && 
/* 1358 */                     (localActiveMissile.getVictim() != null)) {
/* 1359 */                     if (actorIsAI(paramActor)) return null;
/* 1360 */                     return localActiveMissile.getVictim();
/*      */                   }
/*      */                 }
/*      */                 
/*      */ 
/*      */ 
/*      */ 
/* 1367 */                 if (f2 > f3)
/*      */                 {
/*      */ 
/* 1370 */                   f3 = f2;
/* 1371 */                   localObject1 = localActor;
/* 1372 */                   localPoint3f.set(0.0F, 0.0F, 0.0F);
/*      */                 }
/*      */               }
/*      */             }
/*      */           } }
/*      */       }
/* 1378 */       if ((localBridgeSegment != null) && 
/* 1379 */         (d2 < distanceBetween(paramActor, (Actor)localObject1))) {
/* 1380 */         localObject1 = localBridgeSegment;
/* 1381 */         localPoint3f.set(0.0F, 0.0F, 3.0F);
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/* 1386 */       EventLog.type("Exception in selectedActor");
/* 1387 */       EventLog.type(localException.toString());
/* 1388 */       EventLog.type(localException.getMessage());
/*      */     }
/* 1390 */     this.selectedActorOffset.set(localPoint3f);
/* 1391 */     if ((localObject1 != null) && 
/* 1392 */       ((this.iDebugLogLevel & 0x3) == 3)) {
/* 1393 */       System.out.print("Target=" + localObject1.getClass().getName());
/* 1394 */       System.out.print(" Army=" + ((Actor)localObject1).getArmy());
/* 1395 */       System.out.print(" Distance=" + distanceBetween(paramActor, (Actor)localObject1));
/* 1396 */       System.out.println(" Angle=" + angleBetween(paramActor, (Actor)localObject1));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1401 */     return localObject1;
/*      */   }
/*      */   
/*      */   public Actor lookForGuidedMissileTargetLocate(Actor paramActor, float paramFloat1, float paramFloat2, double paramDouble) {
/* 1405 */     double d1 = 0.0D;
/* 1406 */     float f1 = 0.0F;
/* 1407 */     float f2 = 0.0F;
/* 1408 */     float f3 = 0.0F;
/* 1409 */     Actor localObject = null;
/* 1410 */     Point3f localPoint3f = new Point3f(0.0F, 0.0F, 0.0F);
/*      */     
/* 1412 */     if ((!(paramActor instanceof Aircraft)) || (this.iDetectorMode == 0)) return localObject;



//TODO:HARM interface
			   if(this.iDetectorMode == 6|| this.iDetectorMode == 5)    
			   {
               if(paramActor instanceof TypeHARM_carrier)
			   {
            	   localPoint3f = new Point3f(0.0F, 0.0F, 2.0F);
            	   this.selectedActorOffset.set(localPoint3f);
            	   Actor victim = ((TypeHARM_carrier)paramActor).getHarmVictim();
            	   d1 = distanceBetween(paramActor, victim);
            	               if (d1 <= paramDouble) {return null;}
				   return victim;
			   }}
			   
			   
/*      */     try {
/* 1414 */       Autopilotage localAutopilotage = ((Aircraft)paramActor).FM.AP;
/*      */       
/* 1416 */       int i = localAutopilotage.way.Cur();
/* 1417 */       int j = 0;
/* 1418 */       Actor localActor = null;
/*      */       for (;;) {
/* 1420 */         if ((localAutopilotage.way.curr().Action == 3) || (localAutopilotage.way.curr().getTarget() != null)) {
/* 1421 */           localActor = localAutopilotage.way.curr().getTarget();
/* 1422 */           if (localActor.getSpeed(null) < 1.0D) {
/* 1423 */             d1 = distanceBetween(paramActor, localActor);
/* 1424 */             if (d1 <= paramDouble) {
/* 1425 */               j = 1;
/* 1426 */               break;
/*      */             }
/*      */           }
/*      */         }
/* 1430 */         localActor = null;
/* 1431 */         if (localAutopilotage.way.isLast())
/*      */           break;
/* 1433 */         localAutopilotage.way.next();
/*      */       }
/* 1435 */       localAutopilotage.way.setCur(i);
/*      */       
/* 1437 */       if (j != 0)
/* 1438 */         localObject = localActor;
/* 1439 */       if ((localObject instanceof com.maddox.il2.objects.bridges.Bridge)) {
/* 1440 */         localPoint3f = new Point3f(0.0F, 0.0F, 3.0F);
/*      */       }
/*      */     } catch (Exception localException) {
/* 1443 */       EventLog.type("Exception in selectedActor");
/* 1444 */       EventLog.type(localException.toString());
/* 1445 */       EventLog.type(localException.getMessage());
/*      */     }
/* 1447 */     this.selectedActorOffset.set(localPoint3f);
/* 1448 */     return localObject;
/*      */   }
/*      */   
/*      */   public Actor lookForGuidedMissileTargetShip(Actor paramActor, float paramFloat1, float paramFloat2, double paramDouble) {
/* 1452 */     double d1 = 0.0D;
/* 1453 */     float f1 = 0.0F;
/* 1454 */     float f2 = 0.0F;
/* 1455 */     float f3 = 0.0F;
/* 1456 */     Actor localObject = null;
/* 1457 */     this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 0.0F));
/*      */     
/* 1459 */     if ((!(paramActor instanceof Aircraft)) || (this.iDetectorMode == 0)) { return localObject;
/*      */     }
/*      */     
/* 1462 */     if ((this.iDetectorMode == 7) && ((World.Sun().ToSun.z < this.fSunBrightThreshold) || (((Mission.curCloudsType() > 3) || ((Mission.curCloudsType() > 1) && (World.Sun().ToSun.z < this.fSunBrightThreshold + 0.05F))) && (Mission.curCloudsHeight() > paramActor.pos.getAbsPoint().z))))
/*      */     {
/*      */ 
/*      */ 
/* 1466 */       return localObject;
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1472 */       List localList = com.maddox.il2.engine.Engine.targets();
/* 1473 */       int i = localList.size();
/* 1474 */       for (int j = 0; j < i; j++) {
/* 1475 */         Actor localActor = (Actor)localList.get(j);
/* 1476 */         if ((localActor instanceof com.maddox.il2.ai.ground.TgtShip))
/*      */         {
/*      */ 
/* 1479 */           if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility(localActor.pos.getAbsPoint(), paramActor.pos.getAbsPoint()) >= 1.0F))
/*      */           {
/* 1481 */             d1 = distanceBetween(paramActor, localActor);
/*      */             
/* 1483 */             if (d1 <= paramDouble)
/*      */             {
/*      */ 
/* 1486 */               f1 = angleBetween(paramActor, localActor);
/*      */               
/* 1488 */               if (f1 <= paramFloat1)
/*      */               {
/*      */ 
/* 1491 */                 if ((localActor.pos.getAbsPoint().z >= 0.0D) || 
/* 1492 */                   (this.canTrackSubs))
/*      */                 {
/*      */ 
/*      */ 
/*      */ 
/* 1497 */                   f2 = 1.0F / f1 / (float)(d1 * d1);
/* 1498 */                   if (!localActor.isAlive()) {
/* 1499 */                     f2 /= 10.0F;
/*      */                   }
/* 1501 */                   checkAllActiveMissilesValidity();
/* 1502 */                   for (int k = 0; k < getActiveMissilesSize(); k++) {
/* 1503 */                     ActiveMissile localActiveMissile = getActiveMissile(k);
/* 1504 */                     if ((actorIsAI(paramActor)) && 
/* 1505 */                       (paramActor.getArmy() == localActiveMissile.getOwnerArmy()) && 
/* 1506 */                       (localActiveMissile.getVictim() == localActor))
/*      */                     {
/* 1508 */                       f2 = 0.0F;
/* 1509 */                       break;
/*      */                     }
/*      */                     
/*      */ 
/* 1513 */                     if ((this.iDetectorMode != 1) && 
/* 1514 */                       (!this.multiTrackingCapable) && 
/* 1515 */                       (localActiveMissile.getOwner() == paramActor) && 
/* 1516 */                       (localActiveMissile.getVictim() != null)) {
/* 1517 */                       if (actorIsAI(paramActor)) { return null;
/*      */                       }
/* 1519 */                       if ((localActiveMissile.getVictim().pos.getAbsPoint().z < 5.0D) && 
/* 1520 */                         (!this.canTrackSubs))
/*      */                       {
/* 1522 */                         this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 5.0F));
/*      */                       }
/*      */                       
/* 1525 */                       return localActiveMissile.getVictim();
/*      */                     }
/*      */                   }
/*      */                   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1533 */                   if (f2 > f3)
/*      */                   {
/*      */ 
/* 1536 */                     f3 = f2;
/* 1537 */                     localObject = localActor;
/*      */                   }
/*      */                 } }
/*      */             }
/*      */           } }
/*      */       }
/* 1543 */     } catch (Exception localException) { EventLog.type("Exception in selectedActor");
/* 1544 */       EventLog.type(localException.toString());
/* 1545 */       EventLog.type(localException.getMessage());
/*      */     }
/* 1547 */     if ((localObject != null) && 
/* 1548 */       (((Actor)localObject).pos.getAbsPoint().z < 5.0D) && 
/* 1549 */       (!this.canTrackSubs))
/*      */     {
/* 1551 */       this.selectedActorOffset.set(new Point3f(0.0F, 0.0F, 5.0F));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1559 */     return localObject;
/*      */   }
/*      */   
/*      */   public Point3d lookForGuidedMissileTargetPos(Actor paramActor, float paramFloat1, float paramFloat2, double paramDouble, long paramLong) {
/* 1563 */     if (((paramLong & 0x10) != 0L) || ((paramLong & 0x100) != 0L) || ((paramLong & 0x20) != 0L)) {
/* 1564 */       return lookForGuidedMissileTargetGroundPos(paramActor, paramFloat1, paramFloat2, paramDouble);
/*      */     }
/* 1566 */     return null;
/*      */   }
/*      */   
/*      */   public Point3d lookForGuidedMissileTargetGroundPos(Actor paramActor, float paramFloat1, float paramFloat2, double paramDouble) {
/* 1570 */     double d1 = 0.0D;
/* 1571 */     float f1 = 0.0F;
/* 1572 */     float f2 = 0.0F;
/* 1573 */     float f3 = 0.0F;
/* 1574 */     Point3d localObject1 = new Point3d();
/* 1575 */     Point3f localPoint3f = new Point3f(0.0F, 0.0F, 0.0F);
/* 1576 */     int i = 0;
/*      */     
/* 1578 */     if ((!(paramActor instanceof Aircraft)) || ((this.iDetectorMode != 9) && (this.iDetectorMode != 10))) return null;
/*      */     Point3d localObject2;
/* 1580 */     if (this.iDetectorMode == 9)
/*      */     {
/* 1582 */       if (((paramActor instanceof TypeLaserDesignator)) && (((TypeLaserDesignator)paramActor).getLaserOn())) {
/* 1583 */         localObject2 = new Point3d();
/* 1584 */         localObject2 = ((TypeLaserDesignator)paramActor).getLaserSpot();
/* 1585 */         if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility((Point3d)localObject2, paramActor.pos.getAbsPoint()) >= 1.0F))
/*      */         {
/* 1587 */           d1 = paramActor.pos.getAbsPoint().distance((Point3d)localObject2);
/* 1588 */           if (d1 <= paramDouble)
/*      */           {
/* 1590 */             f1 = angleBetween(paramActor, (Point3d)localObject2);
/* 1591 */             if (f1 <= paramFloat1)
/*      */             {
/*      */ 
/* 1594 */               localObject1 = localObject2;
/* 1595 */               i = 1;
/* 1596 */               this.trgtPosOwner = paramActor;
/*      */             }
/*      */           }
/*      */         } }
/* 1600 */       if (i == 0) {
/* 1601 */         List list = Engine.targets();
/* 1602 */         int j = ((List)list).size();
/* 1603 */         for (int k = 0; k < j; k++) {
/* 1604 */           Actor localActor = (Actor)((List)list).get(k);
/* 1605 */           if (((localActor instanceof TypeLaserDesignator)) && (((TypeLaserDesignator)localActor).getLaserOn()) && (localActor.getArmy() == paramActor.getArmy())) {
/* 1606 */             Point3d localPoint3d = new Point3d();
/* 1607 */             localPoint3d = ((TypeLaserDesignator)localActor).getLaserSpot();
/*      */             
/* 1609 */             if ((Main.cur().clouds == null) || ((Main.cur().clouds.getVisibility(localPoint3d, paramActor.pos.getAbsPoint()) >= 1.0F) && (Main.cur().clouds.getVisibility(localPoint3d, localActor.pos.getAbsPoint()) >= 1.0F)))
/*      */             {
/*      */ 
/*      */ 
/* 1613 */               d1 = paramActor.pos.getAbsPoint().distance(localPoint3d);
/* 1614 */               if (d1 <= paramDouble)
/*      */               {
/* 1616 */                 f1 = angleBetween(paramActor, localPoint3d);
/* 1617 */                 if (f1 <= paramFloat1)
/*      */                 {
/*      */ 
/* 1620 */                   f2 = 1.0F / f1 / (float)(d1 * d1);
/* 1621 */                   checkAllActiveMissilesValidity();
/* 1622 */                   for (int m = 0; m < getActiveMissilesSize(); m++) {
/* 1623 */                     ActiveMissile localActiveMissile = getActiveMissile(m);
/*      */                   }
/*      */                   
/* 1626 */                   if (f2 > f3)
/*      */                   {
/*      */ 
/* 1629 */                     f3 = f2;
/* 1630 */                     localObject1 = localPoint3d;
/* 1631 */                     i = 1;
/* 1632 */                     this.trgtPosOwner = localActor;
/*      */                   }
/*      */                 }
/*      */               } } } } }
/* 1636 */       this.selectedActorOffset.set(localPoint3f);
/* 1637 */       if ((this.iDebugLogLevel & 0x3) == 3) {
/* 1638 */         if (i != 0) {
/* 1639 */           System.out.print("TargetPos=" + localObject1);
/* 1640 */           System.out.print(" Distance=" + paramActor.pos.getAbsPoint().distance((Point3d)localObject1));
/* 1641 */           System.out.println(" Angle=" + angleBetween(paramActor, (Point3d)localObject1));
/*      */         }
/*      */         else {
/* 1644 */           System.out.println("TargetPos=null");
/*      */         }
/*      */       }
/*      */     }
/* 1648 */     if ((this.iDetectorMode == 10) && 
/* 1649 */       ((paramActor instanceof com.maddox.il2.objects.air.TypeSACLOS)) && (((com.maddox.il2.objects.air.TypeSACLOS)paramActor).getSACLOSenabled())) {
/* 1650 */       localObject2 = new Point3d();
/* 1651 */       localObject2 = ((com.maddox.il2.objects.air.TypeSACLOS)paramActor).getSACLOStarget();
/* 1652 */       if ((Main.cur().clouds == null) || (Main.cur().clouds.getVisibility((Point3d)localObject2, paramActor.pos.getAbsPoint()) >= 1.0F))
/*      */       {
/* 1654 */         d1 = paramActor.pos.getAbsPoint().distance((Point3d)localObject2);
/* 1655 */         if (d1 <= paramDouble)
/*      */         {
/* 1657 */           f1 = angleBetween(paramActor, (Point3d)localObject2);
/* 1658 */           if (f1 <= paramFloat1)
/*      */           {
/*      */ 
/* 1661 */             localObject1 = localObject2;
/* 1662 */             localObject1.z += 1.0D;
/* 1663 */             i = 1;
/* 1664 */             this.trgtPosOwner = paramActor;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1672 */     if (i != 0) {
/* 1673 */       return (Point3d) localObject1;
/*      */     }
/* 1675 */     this.trgtPosOwner = null;
/* 1676 */     return null;
/*      */   }
/*      */   
/*      */   public void onAircraftLoaded()
/*      */   {
/* 1681 */     this.rocketsList.clear();
/* 1682 */     createMissileList(this.rocketsList);
/* 1683 */     setGunNullOwner();
/*      */   }
/*      */   
/*      */   private void generateMissileDataForPk() {
/* 1687 */     if (!(this.missileOwner instanceof Aircraft)) {
/* 1688 */       this.missileDataForPk = null;
/* 1689 */       return;
/*      */     }
/* 1691 */     if (this.missileDataForPk == null) {
/* 1692 */       this.missileDataForPk = new ArrayList();
/*      */     } else {
/* 1694 */       this.missileDataForPk.clear();
/*      */     }
/* 1696 */     Aircraft localAircraft = (Aircraft)this.missileOwner;
/* 1697 */     com.maddox.il2.ai.BulletEmitter[][] arrayOfBulletEmitter = localAircraft.FM.CT.Weapons;
/* 1698 */     for (int i = 0; i < arrayOfBulletEmitter.length; i++) {
/* 1699 */       if ((i >= 2) && 
/* 1700 */         (i <= 7) && 
/* 1701 */         (arrayOfBulletEmitter[i] != null) && (
/* 1702 */         (!localAircraft.FM.isPlayers()) || (!(localAircraft.FM instanceof RealFlightModel)) || (!((RealFlightModel)localAircraft.FM).isRealMode()) || 
/* 1703 */         (i == localAircraft.FM.CT.rocketHookSelected)))
/*      */       {
/* 1705 */         for (int j = 0; j < arrayOfBulletEmitter[i].length; j++)
/* 1706 */           if ((arrayOfBulletEmitter[i][j] != null) && 
/* 1707 */             (arrayOfBulletEmitter[i][j].haveBullets()) && 
/* 1708 */             ((arrayOfBulletEmitter[i][j] instanceof MissileGun))) {
/* 1709 */             MissileGun localMissileGun = (MissileGun)arrayOfBulletEmitter[i][j];
/* 1710 */             Class localClass = localMissileGun.bulletClass();
/* 1711 */             MissileDataForPk localMissileDataForPk = new MissileDataForPk(null);
/* 1712 */             localMissileDataForPk.setTriggerNum(i);
/* 1713 */             localMissileDataForPk.setMaxLaunchLoad(Property.floatValue(localClass, "maxLockGForce", 99.9F));
/* 1714 */             localMissileDataForPk.setMaxAngleToTarget(Property.floatValue(localClass, "PkMaxFOVfrom", 99.9F));
/* 1715 */             localMissileDataForPk.setMaxAngleFromTargetAft(Property.floatValue(localClass, "PkMaxFOVto", 99.9F));
/* 1716 */             localMissileDataForPk.setMinDist(Property.floatValue(localClass, "PkDistMin", 99.9F));
/* 1717 */             localMissileDataForPk.setOptDist(Property.floatValue(localClass, "PkDistOpt", 99.9F));
/* 1718 */             localMissileDataForPk.setMaxDist(Property.floatValue(localClass, "PkDistMax", 99.9F));
/* 1719 */             this.missileDataForPk.add(localMissileDataForPk);
/* 1720 */             break;
/*      */           } }
/*      */     }
/*      */   }
/*      */   
/*      */   public float Pk(Actor paramActor1, Actor paramActor2) {
/* 1726 */     float f1 = 0.0F;
/* 1727 */     if (!(this.missileOwner instanceof Aircraft)) {
/* 1728 */       return f1;
/*      */     }
/* 1730 */     generateMissileDataForPk();
/* 1731 */     float f2 = angleBetween(paramActor1, paramActor2);
/* 1732 */     float f3 = 180.0F - angleBetween(paramActor2, paramActor1);
/* 1733 */     float f4 = (float)distanceBetween(paramActor1, paramActor2);
/* 1734 */     float f5 = ((Aircraft)paramActor1).FM.getOverload();
/* 1735 */     if (((paramActor1 instanceof Aircraft)) && (
/* 1736 */       (!(((Aircraft)paramActor1).FM instanceof RealFlightModel)) || (!((RealFlightModel)((Aircraft)paramActor1).FM).isRealMode()))) {
/* 1737 */       this.fMaxG *= 2.0F;
/*      */     }
/*      */     
/*      */ 
/* 1741 */     Aircraft localAircraft = (Aircraft)this.missileOwner;
/* 1742 */     int i = localAircraft.FM.CT.rocketHookSelected;
/*      */     
/* 1744 */     for (int j = 0; j < this.missileDataForPk.size(); j++) {
/* 1745 */       float f6 = 100.0F;
/* 1746 */       float f7 = 0.0F;
/* 1747 */       MissileDataForPk localMissileDataForPk = (MissileDataForPk)this.missileDataForPk.get(j);
/* 1748 */       if ((this.iDebugLogLevel & 0x3) == 3) {
/* 1749 */         System.out.println("Checking Missile Data: Trigger=" + localMissileDataForPk.getTriggerNum() + ", maxG=" + localMissileDataForPk.getMaxLaunchLoad() + ", Angle=" + localMissileDataForPk.getMaxAngleToTarget() + ", AngleAft=" + localMissileDataForPk.getMaxAngleFromTargetAft() + ", minDist=" + localMissileDataForPk.getMinDist() + ", optDist=" + localMissileDataForPk.getOptDist() + ", maxDist=" + localMissileDataForPk.getMaxDist());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1756 */       if ((f4 > localMissileDataForPk.getMaxDist()) || (f4 < localMissileDataForPk.getMinDist()) || (f2 > localMissileDataForPk.getMaxAngleToTarget()) || (f3 > localMissileDataForPk.getMaxAngleFromTargetAft()) || (f5 > localMissileDataForPk.getMaxLaunchLoad())) break;
/* 1757 */       if (f4 > localMissileDataForPk.getOptDist()) {
/* 1758 */         f7 = f4 - localMissileDataForPk.getOptDist();
/* 1759 */         f7 /= (localMissileDataForPk.getMaxDist() - localMissileDataForPk.getOptDist());
/* 1760 */         f6 -= f7 * f7 * 20.0F;
/*      */       } else {
/* 1762 */         f7 = localMissileDataForPk.getOptDist() - f4;
/* 1763 */         f7 /= (localMissileDataForPk.getOptDist() - localMissileDataForPk.getMinDist());
/* 1764 */         f6 -= f7 * f7 * 60.0F;
/*      */       }
/* 1766 */       f7 = f2 / localMissileDataForPk.getMaxAngleToTarget();
/* 1767 */       f6 -= f7 * f7 * 30.0F;
/* 1768 */       f7 = f3 / localMissileDataForPk.getMaxAngleFromTargetAft();
/* 1769 */       f6 -= f7 * f7 * 50.0F;
/* 1770 */       f7 = f5 / localMissileDataForPk.getMaxLaunchLoad();
/* 1771 */       f6 -= f7 * f7 * 30.0F;
/* 1772 */       if (f6 < 0.0F) {
/* 1773 */         f6 = 0.0F;
/*      */       }
/* 1775 */       if (f6 > f1) {
/* 1776 */         i = localMissileDataForPk.getTriggerNum();
/* 1777 */         f1 = f6;
/*      */       }
/*      */     }
/*      */     
/* 1781 */     if (i != localAircraft.FM.CT.rocketHookSelected) {
/* 1782 */       localAircraft.FM.CT.doSetRocketHook(i);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1787 */     return f1;
/*      */   }
/*      */   
/*      */   public float Pk(Actor paramActor, Point3d paramPoint3d) {
/* 1791 */     float f1 = 0.0F;
/* 1792 */     if ((!(this.missileOwner instanceof Aircraft)) || (paramPoint3d == null)) {
/* 1793 */       return f1;
/*      */     }
/* 1795 */     generateMissileDataForPk();
/* 1796 */     float f2 = angleBetween(paramActor, paramPoint3d);
/* 1797 */     float f3 = 180.0F - angleBetween(paramPoint3d, paramActor);
/* 1798 */     float f4 = (float)paramActor.pos.getAbsPoint().distance(paramPoint3d);
/* 1799 */     float f5 = ((Aircraft)paramActor).FM.getOverload();
/* 1800 */     if (((paramActor instanceof Aircraft)) && (
/* 1801 */       (!(((Aircraft)paramActor).FM instanceof RealFlightModel)) || (!((RealFlightModel)((Aircraft)paramActor).FM).isRealMode()))) {
/* 1802 */       this.fMaxG *= 2.0F;
/*      */     }
/*      */     
/*      */ 
/* 1806 */     Aircraft localAircraft = (Aircraft)this.missileOwner;
/* 1807 */     int i = localAircraft.FM.CT.rocketHookSelected;
/*      */     
/* 1809 */     for (int j = 0; j < this.missileDataForPk.size(); j++) {
/* 1810 */       float f6 = 100.0F;
/* 1811 */       float f7 = 0.0F;
/* 1812 */       MissileDataForPk localMissileDataForPk = (MissileDataForPk)this.missileDataForPk.get(j);
/* 1813 */       if ((this.iDebugLogLevel & 0x3) == 3) {
/* 1814 */         System.out.println("Checking Missile Data: Trigger=" + localMissileDataForPk.getTriggerNum() + ", maxG=" + localMissileDataForPk.getMaxLaunchLoad() + ", Angle=" + localMissileDataForPk.getMaxAngleToTarget() + ", AngleAft=" + localMissileDataForPk.getMaxAngleFromTargetAft() + ", minDist=" + localMissileDataForPk.getMinDist() + ", optDist=" + localMissileDataForPk.getOptDist() + ", maxDist=" + localMissileDataForPk.getMaxDist());
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1821 */       if ((f4 > localMissileDataForPk.getMaxDist()) || (f4 < localMissileDataForPk.getMinDist()) || (f2 > localMissileDataForPk.getMaxAngleToTarget()) || (f3 > localMissileDataForPk.getMaxAngleFromTargetAft()) || (f5 > localMissileDataForPk.getMaxLaunchLoad())) break;
/* 1822 */       if (f4 > localMissileDataForPk.getOptDist()) {
/* 1823 */         f7 = f4 - localMissileDataForPk.getOptDist();
/* 1824 */         f7 /= (localMissileDataForPk.getMaxDist() - localMissileDataForPk.getOptDist());
/* 1825 */         f6 -= f7 * f7 * 20.0F;
/*      */       } else {
/* 1827 */         f7 = localMissileDataForPk.getOptDist() - f4;
/* 1828 */         f7 /= (localMissileDataForPk.getOptDist() - localMissileDataForPk.getMinDist());
/* 1829 */         f6 -= f7 * f7 * 60.0F;
/*      */       }
/* 1831 */       f7 = f2 / localMissileDataForPk.getMaxAngleToTarget();
/* 1832 */       f6 -= f7 * f7 * 30.0F;
/* 1833 */       f7 = f3 / localMissileDataForPk.getMaxAngleFromTargetAft();
/* 1834 */       f6 -= f7 * f7 * 50.0F;
/* 1835 */       f7 = f5 / localMissileDataForPk.getMaxLaunchLoad();
/* 1836 */       f6 -= f7 * f7 * 30.0F;
/* 1837 */       if (f6 < 0.0F) {
/* 1838 */         f6 = 0.0F;
/*      */       }
/* 1840 */       if (f6 > f1) {
/* 1841 */         i = localMissileDataForPk.getTriggerNum();
/* 1842 */         f1 = f6;
/*      */       }
/*      */     }
/*      */     
/* 1846 */     if (i != localAircraft.FM.CT.rocketHookSelected) {
/* 1847 */       localAircraft.FM.CT.doSetRocketHook(i);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1852 */     return f1;
/*      */   }
/*      */   
/*      */   public float PkOld(Actor paramActor1, Actor paramActor2)
/*      */   {
/* 1857 */     float f1 = 0.0F;
/* 1858 */     float f2 = 0.0F;
/*      */     
/*      */ 
/*      */ 
/* 1862 */     float f3 = angleBetween(paramActor1, paramActor2);
/* 1863 */     float f4 = 180.0F - angleBetween(paramActor2, paramActor1);
/* 1864 */     float f5 = (float)distanceBetween(paramActor1, paramActor2);
/* 1865 */     float f6 = ((Aircraft)paramActor1).FM.getOverload();
/*      */     
/* 1867 */     if (((paramActor1 instanceof Aircraft)) && (
/* 1868 */       (!(((Aircraft)paramActor1).FM instanceof RealFlightModel)) || (!((RealFlightModel)((Aircraft)paramActor1).FM).isRealMode()))) {
/* 1869 */       this.fMaxG *= 2.0F;
/*      */     }
/*      */     
/* 1872 */     f1 = 100.0F;
/* 1873 */     if ((f5 > this.fPkMaxDist) || (f5 < this.fPkMinDist) || (f3 > this.fPkMaxAngle) || (f4 > this.fPkMaxAngleAft) || (f6 > this.fPkMaxG)) return 0.0F;
/* 1874 */     if (f5 > this.fPkOptDist) {
/* 1875 */       f2 = f5 - this.fPkOptDist;
/* 1876 */       f2 /= (this.fPkMaxDist - this.fPkOptDist);
/* 1877 */       f1 -= f2 * f2 * 20.0F;
/*      */     } else {
/* 1879 */       f2 = this.fPkOptDist - f5;
/* 1880 */       f2 /= (this.fPkOptDist - this.fPkMinDist);
/* 1881 */       f1 -= f2 * f2 * 60.0F;
/*      */     }
/* 1883 */     f2 = f3 / this.fPkMaxAngle;
/* 1884 */     f1 -= f2 * f2 * 30.0F;
/* 1885 */     f2 = f4 / this.fPkMaxAngleAft;
/* 1886 */     f1 -= f2 * f2 * 50.0F;
/* 1887 */     f2 = f6 / this.fPkMaxG;
/* 1888 */     f1 -= f2 * f2 * 30.0F;
/* 1889 */     if (f1 < 0.0F) {
/* 1890 */       f1 = 0.0F;
/*      */     }
/* 1892 */     return f1;
/*      */   }
/*      */   
/*      */   public void playMissileGrowlLock(boolean paramBoolean) {
/* 1896 */     if (paramBoolean) {
/* 1897 */       if (this.fxMissileToneNoLock != null) {
/* 1898 */         this.fxMissileToneNoLock.setPlay(false);
/*      */       }
/* 1900 */       if ((this.fxMissileToneLock != null) && (this.smplMissileLock != null)) {
/* 1901 */         this.fxMissileToneLock.play(this.smplMissileLock);
/*      */       }
/* 1903 */       this.fxMissileToneLock.setPlay(true);
/*      */     } else {
/* 1905 */       if (this.fxMissileToneLock != null) {
/* 1906 */         this.fxMissileToneLock.setPlay(false);
/*      */       }
/* 1908 */       if ((this.fxMissileToneNoLock != null) && (this.smplMissileNoLock != null)) {
/* 1909 */         this.fxMissileToneNoLock.play(this.smplMissileNoLock);
/*      */       }
/* 1911 */       this.fxMissileToneNoLock.setPlay(true);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setAttackDecisionByAI(boolean paramBoolean)
/*      */   {
/* 1917 */     this.attackDecisionByAI = paramBoolean;
/*      */   }
/*      */   
/*      */   public void setAttackDecisionByWaypoint(boolean paramBoolean) {
/* 1921 */     this.attackDecisionByWaypoint = paramBoolean;
/*      */   }
/*      */   
/*      */   public void setCanTrackSubs(boolean paramBoolean) {
/* 1925 */     this.canTrackSubs = paramBoolean;
/*      */   }
/*      */   
/*      */   public void setFxMissileToneLock(String paramString) {
/* 1929 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 1930 */     if (paramString == null) {
/* 1931 */       this.fxMissileToneLock = null;
/*      */     }
/*      */     
/* 1934 */     this.fxMissileToneLock = this.missileOwner.newSound(paramString, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setFxMissileToneLock(String paramString, float paramFloat)
/*      */   {
/* 1941 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 1942 */     setFxMissileToneLock(paramString);
/* 1943 */     setFxMissileToneLockVolume(paramFloat);
/*      */   }
/*      */   
/*      */   public void setFxMissileToneLockVolume(float paramFloat) {
/* 1947 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 1948 */     if (this.fxMissileToneLock == null) return;
/* 1949 */     this.fxMissileToneLock.setVolume(paramFloat);
/*      */   }
/*      */   
/*      */ 
/*      */   public void setFxMissileToneNoLock(String paramString)
/*      */   {
/* 1955 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 1956 */     if (paramString == null) {
/* 1957 */       this.fxMissileToneNoLock = null;
/*      */     }
/*      */     
/* 1960 */     this.fxMissileToneNoLock = this.missileOwner.newSound(paramString, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setFxMissileToneNoLock(String paramString, float paramFloat)
/*      */   {
/* 1967 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 1968 */     setFxMissileToneNoLock(paramString);
/* 1969 */     setFxMissileToneNoLockVolume(paramFloat);
/*      */   }
/*      */   
/*      */   public void setFxMissileToneNoLockVolume(float paramFloat) {
/* 1973 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 1974 */     if (this.fxMissileToneNoLock == null) return;
/* 1975 */     this.fxMissileToneNoLock.setVolume(paramFloat);
/*      */   }
/*      */   
/*      */   public void setGunNullOwner() {
/* 1979 */     if (!(this.missileOwner instanceof Aircraft)) return;
/* 1980 */     Aircraft localAircraft = (Aircraft)this.missileOwner;
/*      */     try {
/* 1982 */       for (int i = 0; i < localAircraft.FM.CT.Weapons.length; i++) {
/* 1983 */         if (localAircraft.FM.CT.Weapons[i] != null) {
/* 1984 */           for (int j = 0; j < localAircraft.FM.CT.Weapons[i].length; j++) {
/* 1985 */             if ((localAircraft.FM.CT.Weapons[i][j] != null) && ((localAircraft.FM.CT.Weapons[i][j] instanceof GunNull)))
/*      */             {
/*      */ 
/* 1988 */               ((GunNull)localAircraft.FM.CT.Weapons[i][j]).setOwner(localAircraft); }
/*      */           }
/*      */         }
/*      */       }
/*      */     } catch (Exception localException) {}
/*      */   }
/*      */   
/*      */   public void setLeadPercent(float paramFloat) {
/* 1996 */     this.fLeadPercent = paramFloat;
/*      */   }
/*      */   
/*      */   public void setLockTone(String paramString1, String paramString2, String paramString3, String paramString4) {
/* 2000 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 2001 */     setFxMissileToneLock(paramString1);
/* 2002 */     setFxMissileToneNoLock(paramString2);
/*      */     
/* 2004 */     setSmplMissileLock(paramString3);
/* 2005 */     setSmplMissileNoLock(paramString4);
/*      */   }
/*      */   
/*      */   public void setMaxDistance(float paramFloat)
/*      */   {
/* 2010 */     this.fMaxDistance = paramFloat;
/*      */   }
/*      */   
/*      */   public void setMaxG(float paramFloat) {
/* 2014 */     this.fMaxG = paramFloat;
/*      */   }
/*      */   
/*      */   public void setMaxPOVfrom(float paramFloat) {
/* 2018 */     this.fMaxPOVfrom = paramFloat;
/*      */   }
/*      */   
/*      */   public void setMaxPOVto(float paramFloat) {
/* 2022 */     this.fMaxPOVto = paramFloat;
/*      */   }
/*      */   
/*      */   public void setMillisecondsBetweenMissileLaunchAI(long paramLong) {
/* 2026 */     this.millisecondsBetweenMissileLaunchAI = paramLong;
/*      */   }
/*      */   
/*      */   public void setMinPkForAttack(float paramFloat) {
/* 2030 */     this.minPkForAttack = paramFloat;
/*      */   }
/*      */   
/*      */   public void setMissileGrowl(int paramInt) {
/* 2034 */     this.iMissileTone = paramInt;
/*      */   }
/*      */   
/*      */   public void setMissileMaxSpeedKmh(float paramFloat) {
/* 2038 */     this.fMissileMaxSpeedKmh = paramFloat;
/*      */   }
/*      */   
/*      */   public void setMissileName(String paramString) {
/* 2042 */     this.missileName = paramString;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMissileOwner(Actor paramActor)
/*      */   {
/* 2067 */     this.missileOwner = paramActor;
/* 2068 */     initLockTones();
/*      */   }
/*      */   
/*      */   public void setMissileTarget(Actor paramActor) {
/* 2072 */     this.trgtMissile = paramActor;
/*      */   }
/*      */   
/*      */   public void setMissileTargetPos(Point3d paramPoint3d) {
/* 2076 */     this.trgtPosMissile = paramPoint3d;
/*      */   }
/*      */   
/*      */   public void setMissileTargetPosOwner(Actor paramActor) {
/* 2080 */     this.trgtPosOwner = paramActor;
/*      */   }
/*      */   
/*      */   public void setMultiTrackingCapable(boolean paramBoolean) {
/* 2084 */     this.multiTrackingCapable = paramBoolean;
/*      */   }
/*      */   
/*      */   public void setPkMaxAngle(float paramFloat) {
/* 2088 */     this.fPkMaxAngle = paramFloat;
/*      */   }
/*      */   
/*      */   public void setPkMaxAngleAft(float paramFloat) {
/* 2092 */     this.fPkMaxAngleAft = paramFloat;
/*      */   }
/*      */   
/*      */   public void setPkMaxDist(float paramFloat) {
/* 2096 */     this.fPkMaxDist = paramFloat;
/*      */   }
/*      */   
/*      */   public void setPkMaxG(float paramFloat) {
/* 2100 */     this.fPkMaxG = paramFloat;
/*      */   }
/*      */   
/*      */   public void setPkMinDist(float paramFloat) {
/* 2104 */     this.fPkMinDist = paramFloat;
/*      */   }
/*      */   
/*      */   public void setPkOptDist(float paramFloat) {
/* 2108 */     this.fPkOptDist = paramFloat;
/*      */   }
/*      */   
/*      */   public void setSmplMissileLock(String paramString) {
/* 2112 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 2113 */     if (paramString == null) {
/* 2114 */       this.smplMissileLock = null;
/* 2115 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2119 */     this.smplMissileLock = new Sample(paramString, 256, 65535);
/*      */     
/* 2121 */     this.smplMissileLock.setInfinite(true);
/*      */   }
/*      */   
/*      */   public void setSmplMissileNoLock(String paramString) {
/* 2125 */     if (this.missileOwner != World.getPlayerAircraft()) return;
/* 2126 */     if (paramString == null) {
/* 2127 */       this.smplMissileNoLock = null;
/* 2128 */       return;
/*      */     }
/*      */     
/*      */ 
/* 2132 */     this.smplMissileNoLock = new Sample(paramString, 256, 65535);
/*      */     
/* 2134 */     this.smplMissileNoLock.setInfinite(true);
/*      */   }
/*      */   
/*      */   public void setStartLastMissile(long paramLong) {
/* 2138 */     this.tStartLastMissile = paramLong;
/*      */   }
/*      */   
/*      */   public void setStepsForFullTurn(float paramFloat) {
/* 2142 */     this.fStepsForFullTurn = paramFloat;
/*      */   }
/*      */   
/*      */   public void setTargetType(long paramLong) {
/* 2146 */     this.targetType = paramLong;
/*      */   }
/*      */   
/*      */   public void shootRocket() {
/* 2150 */     if (this.rocketsList.isEmpty()) { return;
/*      */     }
/* 2152 */     ((RocketGun)this.rocketsList.get(0)).shots(1);
/*      */   }
/*      */   
/*      */   public void shotMissile()
/*      */   {
/* 2157 */     if (!(this.missileOwner instanceof Aircraft)) { return;
/*      */     }
/*      */     
/* 2160 */     Aircraft localAircraft = (Aircraft)this.missileOwner;
/* 2161 */     if (hasMissiles()) {
/* 2162 */       if ((com.maddox.il2.net.NetMissionTrack.isPlaying()) || (Mission.isNet())) {
/* 2163 */         if ((!(localAircraft.FM instanceof RealFlightModel)) || (!((RealFlightModel)localAircraft.FM).isRealMode())) {
/* 2164 */           ((RocketGun)this.rocketsList.get(0)).loadBullets(((RocketGun)this.rocketsList.get(0)).bullets() - 1);
/* 2165 */         } else if ((World.cur().diffCur.Limited_Ammo) && (this.missileOwner.isNetMirror())) {
/* 2166 */           ((RocketGun)this.rocketsList.get(0)).loadBullets(((RocketGun)this.rocketsList.get(0)).bullets() - 1);
/*      */         }
/*      */       }
/*      */       
/* 2170 */       if (((World.cur().diffCur.Limited_Ammo) || (localAircraft != World.getPlayerAircraft())) && 
/* 2171 */         (((RocketGun)this.rocketsList.get(0)).bullets() == 1)) {
/* 2172 */         this.rocketsList.remove(0);
/*      */       }
/*      */       
/* 2175 */       if (localAircraft != World.getPlayerAircraft()) {
/* 2176 */         com.maddox.il2.objects.sounds.Voice.speakAttackByRockets(localAircraft);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void update() {
/* 2182 */     Aircraft localAircraft = (Aircraft)this.missileOwner;
/* 2183 */     if ((localAircraft != null) && 
/* 2184 */       (localAircraft.FM.CT.Weapons[localAircraft.FM.CT.rocketHookSelected] == null)) { localAircraft.FM.CT.toggleRocketHook();
/*      */     }
/*      */     
/* 2187 */     if ((this.iDetectorMode == 9) || (this.iDetectorMode == 10)) {
/* 2188 */       setMissileTargetPos(lookForGuidedMissileTargetPos(this.missileOwner, getMaxPOVfrom(), getMaxPOVto(), getPkMaxDist(), this.targetType));
/*      */     } else
/* 2190 */       setMissileTarget(lookForGuidedMissileTarget(this.missileOwner, getMaxPOVfrom(), getMaxPOVto(), getPkMaxDist(), this.targetType));
/* 2191 */     this.trgtPk = getMissilePk();
/* 2192 */     checkAIlaunchMissile();
/* 2193 */     checkPendingMissiles();
/* 2194 */     checkLockStatus();
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean checkActiveMissileInit()
/*      */   {
/* 2200 */     if (activeMissiles == null) {
/* 2201 */       curMission = Mission.cur();
/* 2202 */       activeMissiles = new ArrayList();
/* 2203 */       return true;
/*      */     }
/* 2205 */     if (Mission.cur() != curMission) {
/* 2206 */       curMission = Mission.cur();
/* 2207 */       if (activeMissiles != null) activeMissiles.clear();
/* 2208 */       activeMissiles = new ArrayList();
/* 2209 */       return true;
/*      */     }
/* 2211 */     return false;
/*      */   }
/*      */   
/*      */   public static ArrayList getActiveMissiles() {
/* 2215 */     checkActiveMissileInit();
/* 2216 */     return activeMissiles;
/*      */   }
/*      */   
/*      */   public static void setActiveMissiles(ArrayList paramArrayList) {
/* 2220 */     activeMissiles = paramArrayList;
/*      */   }
/*      */   
/*      */   public static void addActiveMissile(ActiveMissile paramActiveMissile) {
/* 2224 */     checkActiveMissileInit();
/* 2225 */     addTargetHandledByAi(paramActiveMissile.getOwnerArmy(), paramActiveMissile.getVictim());
/* 2226 */     activeMissiles.add(paramActiveMissile);
/*      */   }
/*      */   
/*      */   public static boolean removeActiveMissile(ActiveMissile paramActiveMissile) {
/* 2230 */     if (checkActiveMissileInit()) return true;
/* 2231 */     if (!activeMissiles.contains(paramActiveMissile)) return false;
/* 2232 */     addTargetHandledByAi(paramActiveMissile.getOwnerArmy(), paramActiveMissile.getVictim());
/* 2233 */     return activeMissiles.remove(paramActiveMissile);
/*      */   }
/*      */   
/*      */   public static int getActiveMissilesSize() {
/* 2237 */     if (checkActiveMissileInit()) return 0;
/* 2238 */     return activeMissiles.size();
/*      */   }
/*      */   
/*      */   public static ActiveMissile getActiveMissile(int paramInt) {
/* 2242 */     if (checkActiveMissileInit()) return null;
/* 2243 */     if (paramInt >= activeMissiles.size()) return null;
/* 2244 */     return (ActiveMissile)activeMissiles.get(paramInt);
/*      */   }
/*      */   
/*      */   public static boolean checkAllActiveMissilesValidity() {
/* 2248 */     if (activeMissiles == null) {
/* 2249 */       curMission = Mission.cur();
/* 2250 */       activeMissiles = new ArrayList();
/* 2251 */       return true;
/*      */     }
/* 2253 */     boolean bool = true;
/* 2254 */     for (int i = 0; i < activeMissiles.size(); i++) {
/* 2255 */       ActiveMissile localActiveMissile = (ActiveMissile)activeMissiles.get(i);
/* 2256 */       if (!localActiveMissile.isValidMissile()) {
/* 2257 */         activeMissiles.remove(i);
/* 2258 */         bool = false;
/*      */       }
/*      */     }
/* 2261 */     return bool;
/*      */   }
/*      */   
/*      */   public static boolean noLaunchSince(long paramLong, int paramInt) {
/* 2265 */     if (checkActiveMissileInit()) return true;
/* 2266 */     checkAllActiveMissilesValidity();
/* 2267 */     for (int i = 0; i < activeMissiles.size(); i++) {
/* 2268 */       ActiveMissile localActiveMissile = (ActiveMissile)activeMissiles.get(i);
/* 2269 */       if ((localActiveMissile.getOwnerArmy() == paramInt) && 
/* 2270 */         (Time.current() - localActiveMissile.getLaunchTime() < paramLong)) {
/* 2271 */         return false;
/*      */       }
/*      */     }
/*      */     
/* 2275 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean missilesLeft(com.maddox.il2.ai.BulletEmitter[] paramArrayOfBulletEmitter) {
/* 2279 */     for (int i = 0; i < paramArrayOfBulletEmitter.length; i++) {
/* 2280 */       if ((paramArrayOfBulletEmitter[i] != null) && 
/* 2281 */         ((paramArrayOfBulletEmitter[i] instanceof MissileGun)) && 
/* 2282 */         (paramArrayOfBulletEmitter[i].haveBullets())) { return true;
/*      */       }
/*      */     }
/*      */     
/* 2286 */     return false;
/*      */   }
/*      */   
/*      */   private static boolean checkTargetsHandledByAi() {
/* 2290 */     if (targetsHandledByAiAlready == null) {
/* 2291 */       targetsHandledByAiAlready = new HashMap();
/* 2292 */       return true;
/*      */     }
/* 2294 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isTargetHandledByAi(int paramInt, Actor paramActor) {
/* 2298 */     if (paramActor == null) return true;
/* 2299 */     checkTargetsHandledByAi();
/* 2300 */     int i = paramActor.hashCode();
/* 2301 */     Long localLong = new Long(com.maddox.sas1946.il2.util.Conversion.longFromTwoInts(paramInt, i));
/* 2302 */     long l1 = Time.current();
/* 2303 */     if (targetsHandledByAiAlready.containsKey(localLong)) {
/* 2304 */       long l2 = ((Long)targetsHandledByAiAlready.get(localLong)).longValue();
/* 2305 */       if ((l1 < l2) || (l1 > l2 + 1000L))
/*      */       {
/* 2307 */         targetsHandledByAiAlready.remove(localLong);
/* 2308 */         return false;
/*      */       }
/* 2310 */       return true;
/*      */     }
/* 2312 */     return false;
/*      */   }
/*      */   
/*      */   public static long addTargetHandledByAi(int paramInt, Actor paramActor) {
/* 2316 */     if (paramActor == null) return 0L;
/* 2317 */     checkTargetsHandledByAi();
/* 2318 */     int i = paramActor.hashCode();
/* 2319 */     Long localLong1 = new Long(com.maddox.sas1946.il2.util.Conversion.longFromTwoInts(paramInt, i));
/* 2320 */     Long localLong2 = new Long(Time.current());
/* 2321 */     Long localLong3 = (Long)targetsHandledByAiAlready.put(localLong1, localLong2);
/* 2322 */     if (localLong3 == null) return 0L;
/* 2323 */     return localLong3.longValue();
/*      */   }
/*      */   


			 private float MyotkaFOV = 0;
			 private boolean SoundON = true;
/* 2326 */   private static Mission curMission = null;
/* 2327 */   private static ArrayList activeMissiles = null;
/* 2328 */   private static HashMap targetsHandledByAiAlready = null;
/*      */   
/* 2330 */   public static int HEAT_SPREAD_360 = 2;
/* 2331 */   public static int HEAT_SPREAD_AFT = 1;
/* 2332 */   public static int HEAT_SPREAD_NONE = 0;
/*      */   
/* 2334 */   private boolean attackDecisionByAI = false;
/* 2335 */   private boolean attackDecisionByWaypoint = false;
/* 2336 */   private boolean canTrackSubs = false;
/* 2337 */   protected double d = 0.0D;
/* 2338 */   protected float deltaAzimuth = 0.0F;
/* 2339 */   protected float deltaTangage = 0.0F;
/* 2340 */   private final int ENGAGE_AUTO = 0;
/* 2341 */   private final int ENGAGE_OFF = -1;
/* 2342 */   private final int ENGAGE_ON = 1;
/* 2343 */   private int engageMode = 0;
/* 2344 */   protected com.maddox.il2.engine.Eff3DActor fl1 = null;
/* 2345 */   protected com.maddox.il2.engine.Eff3DActor fl2 = null;
/* 2346 */   private float fLeadPercent = 0.0F;
/* 2347 */   protected FlightModel fm = null;
/* 2348 */   private float fMaxDistance = 4500.0F;
/* 2349 */   private float fMaxG = 12.0F;
/* 2350 */   private float fMaxPOVfrom = 25.0F;
/* 2351 */   private float fMaxPOVto = 60.0F;
/* 2352 */   protected float fMissileBaseSpeedKmh = 0.0F;
/* 2353 */   protected float fMissileMaxSpeedKmh = 2000.0F;
/* 2354 */   private float fPkMaxAngle = 30.0F;
/* 2355 */   private float fPkMaxAngleAft = 70.0F;
/* 2356 */   private float fPkMaxDist = 4500.0F;
/* 2357 */   private float fPkMaxG = 2.0F;
/* 2358 */   private float fPkMinDist = 400.0F;
/* 2359 */   private float fPkOptDist = 1500.0F;
/* 2360 */   private float fStepsForFullTurn = 10.0F;
/* 2361 */   private float fSunBrightThreshold = 0.03F;
/* 2362 */   private SoundFX fxMissileToneLock = null;
/* 2363 */   private SoundFX fxMissileToneNoLock = null;
/* 2364 */   private int iDetectorMode = 0;
/* 2365 */   private int iMissileLockState = 0;
/* 2366 */   private int iMissileTone = 0;
/* 2367 */   protected double launchKren = 0.0D;
/* 2368 */   protected double launchPitch = 0.0D;
/* 2369 */   protected double launchYaw = 0.0D;
/* 2370 */   private boolean lockTonesInitialized = false;
/* 2371 */   private long millisecondsBetweenMissileLaunchAI = 10000L;
/* 2372 */   private float minPkForAttack = 25.0F;
/* 2373 */   private String missileName = null;
/* 2374 */   private Actor missileOwner = null;
/* 2375 */   private boolean multiTrackingCapable = true;
/* 2376 */   private Class myMissileClass = null;
/* 2377 */   private boolean oldBreakControl = false;
/* 2378 */   protected float oldDeltaAzimuth = 0.0F;
/* 2379 */   protected float oldDeltaTangage = 0.0F;
/* 2380 */   protected com.maddox.il2.engine.Orient or = new com.maddox.il2.engine.Orient();
/* 2381 */   protected com.maddox.il2.engine.Orient orVictimOffset = null;
/* 2382 */   protected Point3d p = new Point3d();
/* 2383 */   protected float prevd = 0.0F;
/* 2384 */   protected Point3d pT = null;
/* 2385 */   protected Point3f pVictimOffset = null;
/* 2386 */   public int rocketSelected = 2;
/* 2387 */   private ArrayList rocketsList = null;
/* 2388 */   private Point3f selectedActorOffset = null;
/* 2389 */   private Sample smplMissileLock = null;
/* 2390 */   private Sample smplMissileNoLock = null;
/* 2391 */   private long targetType = 1L;
/* 2392 */   private long tLastSeenEnemy = 0L;
/* 2393 */   private long tMissilePrev = 0L;
/*      */   
/* 2395 */   private Actor trgtAI = null;
/*      */   
/* 2397 */   private Actor trgtMissile = null;
/* 2398 */   private Point3d trgtPosMissile = null;
/* 2399 */   private Actor trgtPosOwner = null;
/*      */   
/* 2401 */   private float trgtPk = 0.0F;
/*      */   
/* 2403 */   protected long tStartLastMissile = 0L;
/*      */   
/* 2405 */   protected Vector3d v = null;
/*      */   
/* 2407 */   protected Actor victim = null;
/*      */   
/* 2409 */   protected Vector3d victimSpeed = null;
/*      */   
/* 2411 */   private ArrayList missileDataForPk = null;
/*      */   
/*      */ 
/*      */   private static final long minTimeBetweenAIMissileLaunch = 1000L;
/*      */   
/*      */ 
/* 2417 */   private int iDebugLogLevel = 0;
/* 2418 */   private boolean bRealisticRadarSelect = false;
/*      */ }

/* Location:           C:\Users\Koty\OLD_PC\Downloads\Projects\RESOLVED\EngMod_2 - kopie
 * Qualified Name:     com.maddox.il2.objects.weapons.GuidedMissileUtils
 * Java Class Version: 1.3 (47.0)
 * JD-Core Version:    0.7.0.1
 */