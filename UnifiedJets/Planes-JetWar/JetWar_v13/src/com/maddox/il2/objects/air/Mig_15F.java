// Source File Name: Mig_15F.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Mig_15F extends Scheme1 implements TypeSupersonic, TypeFighter, TypeBNZFighter, TypeFighterAceMaker {

  protected boolean curst;
  private boolean ts;
  private float oldctl;
  private float curctl;
  private float oldthrl;
  private float curthrl;
  private boolean overrideBailout;
  private boolean ejectComplete;
  public static boolean bChangedPit = false;
  private float mn = 0.0F;
  private static float uteb = 1.25F;
  private static float lteb = 0.9F;
  private static float mteb = 1.0F;
  private boolean ictl;
  private float actl;
  private float rctl;
  private float ectl;
  private float SonicBoom = 0.0F;
  private Eff3DActor shockwave;
  private boolean isSonic;
  private final $0 $1;
  protected boolean isTrainer;
  public int k14Mode;
  public int k14WingspanType;
  public float k14Distance;
  private float engineSurgeDamage;
  private static float kl = 1.0F;
  private static float kr = 1.0F;
  private static float kc = 1.0F;

  private class $0 {

    private float lal;
    private float tal;
    private float bef;
    private float tef;
    private float bhef;
    private float thef;
    private float phef;
    private float mef;
    private float wef;
    private float lef;
    private float ftl;

    private $0(float f1, float f2, float f3, float f4, float f5, float f6,
            float f7, float f8, float f9, float f10, float f11) {
      lal = f1;
      tal = f2;
      bef = f3;
      tef = f4;
      bhef = f5;
      thef = f6;
      phef = f7;
      mef = f8;
      wef = f9;
      lef = f10;
      ftl = f11;
    }

    public void rs(int ii) {
      if (ii == 0 || ii == 1) {
        access$234(Mig_15F.this, 0.68);
      }
      if (ii == 31 || ii == 32) {
        access$334(Mig_15F.this, 0.68);
      }
      if (ii == 15 || ii == 16) {
        access$434(Mig_15F.this, 0.68);
      }
    }

    private void $1() {
      if (ts) {
        float f1 = Aircraft.cvt(FM.getAltitude(), lal, tal, bef, tef);
        float f2 = Aircraft.cvt(mn,
                (mn < Mig_15F.mteb ? Mig_15F.lteb
                : Mig_15F.uteb),
                (mn < Mig_15F.mteb ? Mig_15F.uteb
                : Mig_15F.lteb),
                mn < Mig_15F.mteb ? bhef : thef,
                mn < Mig_15F.mteb ? thef : phef);
        float f3 = Aircraft.cvt(mn,
                (mn < Mig_15F.mteb ? Mig_15F.lteb
                : Mig_15F.uteb),
                (mn < Mig_15F.mteb ? Mig_15F.uteb
                : Mig_15F.lteb),
                mn < Mig_15F.mteb ? mef : wef / f1,
                mn < Mig_15F.mteb ? wef / f1 : lef / f1);
        ((RealFlightModel) FM).producedShakeLevel += 0.1125F * f2;
        if (World.Rnd().nextFloat() > 0.76F) {
          FM.SensPitch = ectl * f3 * f3;
        } else {
          FM.SensPitch = ectl * f3;
        }
        FM.SensRoll = actl * f3 * f3;
        FM.SensYaw = rctl * f3;
        if (f2 > 0.6F) {
          ictl = true;
        } else {
          ictl = false;
        }
        if (ftl > 0.0F) {
          if (World.Rnd().nextFloat() > 0.6F) {
            if (FM.CT.RudderControl > 0.0F) {
              FM.CT.RudderControl -= ftl * f2;
            } else if (FM.CT.RudderControl < 0.0F) {
              FM.CT.RudderControl += ftl * f2;
            } else {
              com.maddox.il2.fm.Controls controls = FM.CT;
              controls.RudderControl = (controls.RudderControl
                      + (World.Rnd().nextFloat() > 0.5F ? ftl * f2
                      : -ftl * f2));
            }
          }
          if (FM.CT.RudderControl > 1.0F) {
            FM.CT.RudderControl = 1.0F;
          }
          if (FM.CT.RudderControl < -1.0F) {
            FM.CT.RudderControl = -1.0F;
          }
        }
      } else {
        FM.SensPitch = ectl;
        FM.SensRoll = actl;
        FM.SensYaw = rctl;
      }
    }
  }

  public Mig_15F() {
    ts = false;
    curst = false;
    oldctl = -1.0F;
    curctl = -1.0F;
    oldthrl = -1.0F;
    curthrl = -1.0F;
    ictl = false;
    overrideBailout = false;
    ejectComplete = false;
    $1 = new $0(0.0F, 14000.0F, 0.65F, 1.0F, 0.05F, 1.0F, 0.4F, 1.0F,
            0.46F, 0.55F, 0.65F);
    k14Mode = 0;
    k14WingspanType = 0;
    k14Distance = 200F;
    engineSurgeDamage = 0.0F;
    isTrainer = false;
  }

  public boolean typeFighterAceMakerToggleAutomation() {
    k14Mode++;
    if (k14Mode > 2) {
      k14Mode = 0;
    }
    HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
    return true;
  }

  public void typeFighterAceMakerAdjDistanceReset() {
  }

  public void typeFighterAceMakerAdjDistancePlus() {
    k14Distance += 10F;
    if (k14Distance > 800F) {
      k14Distance = 800F;
    }
    HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
  }

  public void typeFighterAceMakerAdjDistanceMinus() {
    k14Distance -= 10F;
    if (k14Distance < 200F) {
      k14Distance = 200F;
    }
    HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
  }

  public void typeFighterAceMakerAdjSideslipReset() {
  }

  public void typeFighterAceMakerAdjSideslipPlus() {
    k14WingspanType--;
    if (k14WingspanType < 0) {
      k14WingspanType = 0;
    }
    HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
  }

  public void typeFighterAceMakerAdjSideslipMinus() {
    k14WingspanType++;
    if (k14WingspanType > 9) {
      k14WingspanType = 9;
    }
    HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + k14WingspanType);
  }

  public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
          throws IOException {
    netmsgguaranted.writeByte(k14Mode);
    netmsgguaranted.writeByte(k14WingspanType);
    netmsgguaranted.writeFloat(k14Distance);
  }

  public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
          throws IOException {
    k14Mode = netmsginput.readByte();
    k14WingspanType = netmsginput.readByte();
    k14Distance = netmsginput.readFloat();
  }

  public void onAircraftLoaded() {
    super.onAircraftLoaded();
    FM.AS.wantBeaconsNet(true);
    actl = FM.SensRoll;
    ectl = FM.SensPitch;
    rctl = FM.SensYaw;
  }

  public void rareAction(float paramFloat, boolean paramBoolean) {
    super.rareAction(paramFloat, paramBoolean);
    if ((FM.Gears.nearGround() || FM.Gears.onGround())
            && FM.CT.getCockpitDoor() == 1.0F) {
      this.hierMesh().chunkVisible("HMask1_D0", false);
    } else {
      this.hierMesh().chunkVisible("HMask1_D0",
              this.hierMesh().isChunkVisible("Pilot1_D0"));
    }
    if ((!FM.isPlayers() || !(FM instanceof RealFlightModel) || !((RealFlightModel) FM).isRealMode()) && (FM instanceof Maneuver)) {
      if (FM.AP.way.isLanding()
              && (FM.getSpeed() > FM.VmaxFLAPS)
              && (FM.getSpeed() > FM.AP.way.curr().getV() * 1.4F)) {
        if (FM.CT.AirBrakeControl != 1.0F) {
          FM.CT.AirBrakeControl = 1.0F;
        }
      } else if ((((Maneuver) FM).get_maneuver() == Maneuver.LANDING)
              && FM.AP.way.isLanding()
              && (FM.getSpeed() < FM.VmaxFLAPS * 1.16F)) {
        if ((FM.getSpeed() > FM.VminFLAPS * 0.5F) && (FM.Gears.nearGround() || FM.Gears.onGround())) {
          if (FM.CT.AirBrakeControl != 1.0F) {
            FM.CT.AirBrakeControl = 1.0F;
          }
        } else {
          if (FM.CT.AirBrakeControl != 0.0F) {
            FM.CT.AirBrakeControl = 0.0F;
          }
        }
      } else if (((Maneuver) FM).get_maneuver() == Maneuver.TAXI) {
        if (FM.CT.AirBrakeControl != 0.0F) {
          FM.CT.AirBrakeControl = 0.0F;
        }
      } else if (((Maneuver) FM).get_maneuver() == Maneuver.SPIRAL_BRAKE) {
        if (FM.CT.AirBrakeControl != 1.0F) {
          FM.CT.AirBrakeControl = 1.0F;
        }
      } else {
        if (FM.CT.AirBrakeControl != 0.0F) {
          FM.CT.AirBrakeControl = 0.0F;
        }
      }
    }
  }

  public void doEjectCatapult() {
    new MsgAction(false, this) {

      public void doAction(Object paramObject) {
        Aircraft localAircraft = (Aircraft) paramObject;
        if (Actor.isValid(localAircraft)) {
          Loc localLoc1 = new Loc();
          Loc localLoc2 = new Loc();
          Vector3d localVector3d = new Vector3d(0.0, 0.0, 10.0);
          HookNamed localHookNamed = new HookNamed(localAircraft, "_ExternalSeat01");
          localAircraft.pos.getAbs(localLoc2);
          localHookNamed.computePos(localAircraft, localLoc2,
                  localLoc1);
          localLoc1.transform(localVector3d);
          localVector3d.x += localAircraft.FM.Vwld.x;
          localVector3d.y += localAircraft.FM.Vwld.y;
          localVector3d.z += localAircraft.FM.Vwld.z;
          new EjectionSeat(7, localLoc1, localVector3d,
                  localAircraft);
        }
      }
    };
    this.hierMesh().chunkVisible("Seat_D0", false);
    FM.setTakenMortalDamage(true, null);
    FM.CT.WeaponControl[0] = false;
    FM.CT.WeaponControl[1] = false;
    FM.CT.bHasAileronControl = false;
    FM.CT.bHasRudderControl = false;
    FM.CT.bHasElevatorControl = false;
  }

  public void doMurderPilot(int paramInt) {
    switch (paramInt) {
      case 0:
        this.hierMesh().chunkVisible("Pilot1_D0", false);
        this.hierMesh().chunkVisible("Head1_D0", false);
        this.hierMesh().chunkVisible("HMask1_D0", false);
        this.hierMesh().chunkVisible("Pilot1_D1", true);
    }
  }

  protected void nextDMGLevel(String paramString, int paramInt,
          Actor paramActor) {
    super.nextDMGLevel(paramString, paramInt, paramActor);
    if (FM.isPlayers()) {
      bChangedPit = true;
    }
  }

  protected void nextCUTLevel(String paramString, int paramInt,
          Actor paramActor) {
    super.nextCUTLevel(paramString, paramInt, paramActor);
    if (FM.isPlayers()) {
      bChangedPit = true;
    }
  }

  public void moveCockpitDoor(float paramFloat) {
    this.resetYPRmodifier();
    if (paramFloat < 0.1F) {
      Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.08F, 0.0F, 0.1F);
    } else {
      Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.17F, 0.99F, 0.1F, 0.4F);
    }
    this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz,
            Aircraft.ypr);
    if (Config.isUSE_RENDER()) {
      if (Main3D.cur3D().cockpits != null
              && Main3D.cur3D().cockpits[0] != null) {
        Main3D.cur3D().cockpits[0].onDoorMoved(paramFloat);
      }
      this.setDoorSnd(paramFloat);
    }
  }

  public static void moveGear(HierMesh paramHierMesh, float paramFloat) {
	    float f = Math.max(-paramFloat * 1500.0F, -90.0F);
	    paramHierMesh.chunkSetAngles("GearC2_D0", 0.0F, -127.0F * paramFloat * kc,
	            0.0F);
	    paramHierMesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(paramFloat * kc, 0.0F, 0.15F,
	            0.0F, -90.0F), 0.0F);
	    paramHierMesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(paramFloat * kc, 0.0F, 0.15F,
	            0.0F, -95.0F), 0.0F);
	    paramHierMesh.chunkSetAngles("GearL2_D0", -90.0F * paramFloat * kl,
	            -38.0F * paramFloat * kl, 90.0F * paramFloat * kl);
	    paramHierMesh.chunkSetAngles("GearR2_D0", 90.0F * paramFloat * kr,
	            -38.0F * paramFloat * kr, -90.0F * paramFloat * kr);
	    paramHierMesh.chunkSetAngles("GearL4_D0", 0.0F, -115.0F * paramFloat * kl,
	            0.0F);
	    paramHierMesh.chunkSetAngles("GearR4_D0", 0.0F, -115.0F * paramFloat * kr,
	            0.0F);
	    paramHierMesh.chunkSetAngles("GearL6_D0", 0.0F, f, 0.0F);
	    paramHierMesh.chunkSetAngles("GearR6_D0", 0.0F, f, 0.0F);
	  }

	  protected void moveGear(float paramFloat) {
	      moveGear(hierMesh(), paramFloat);
	      if(((FlightModelMain) (super.FM)).CT.getGear() >= 0.9985F)
	      {
	          kl = 1.0F;
	          kr = 1.0F;
	          kc = 1.0F;
	      }
	  }

	  private void gearDamageFX(String s)
	  {
	      if(s.startsWith("xgearl") || s.startsWith("GearL"))
	      {
	          if(super.FM.isPlayers())
	              HUD.log("Left Gear:  Hydraulic system Failed");
	          kl = World.Rnd().nextFloat();
	          kr = World.Rnd().nextFloat() * kl;
	          kc = 0.1F;
	          cutGearCovers("L");
	      } else
	      if(s.startsWith("xgearr") || s.startsWith("GearR"))
	      {
	          if(super.FM.isPlayers())
	              HUD.log("Right Gear:  Hydraulic system Failed");
	          kr = World.Rnd().nextFloat();
	          kl = World.Rnd().nextFloat() * kr;
	          kc = 0.1F;
	          cutGearCovers("R");
	      } else
	      {
	          if(super.FM.isPlayers())
	              HUD.log("Center Gear:  Hydraulic system Failed");
	          kc = World.Rnd().nextFloat();
	          kl = World.Rnd().nextFloat() * kc;
	          kr = World.Rnd().nextFloat() * kc;
	          cutGearCovers("C");
	      }
	      ((FlightModelMain) (super.FM)).CT.GearControl = 1.0F;
	      ((FlightModelMain) (super.FM)).Gears.setHydroOperable(false);
	  }

	  private void cutGearCovers(String s)
	  {
	      Vector3d vector3d = new Vector3d();
	      if(World.Rnd().nextFloat() < 0.3F)
	      {
	          Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("Gear" + s + 5 + "_D0"));
	          wreckage.collide(true);
	          vector3d.set(((FlightModelMain) (super.FM)).Vwld);
	          wreckage.setSpeed(vector3d);
	          hierMesh().chunkVisible("Gear" + s + 5 + "_D0", false);
	          Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("Gear" + s + 6 + "_D0"));
	          wreckage1.collide(true);
	          vector3d.set(((FlightModelMain) (super.FM)).Vwld);
	          wreckage1.setSpeed(vector3d);
	          hierMesh().chunkVisible("Gear" + s + 6 + "_D0", false);
	      } else
	      if(World.Rnd().nextFloat() < 0.3F)
	      {
	          int i = World.Rnd().nextInt(2) + 5;
	          Wreckage wreckage2 = new Wreckage(this, hierMesh().chunkFind("Gear" + s + i + "_D0"));
	          wreckage2.collide(true);
	          vector3d.set(((FlightModelMain) (super.FM)).Vwld);
	          wreckage2.setSpeed(vector3d);
	          hierMesh().chunkVisible("Gear" + s + i + "_D0", false);
	      }
	  }

  public void moveWheelSink() {
    float f = Aircraft.cvt(FM.Gears.gWheelSinking[2], 0.0F, 0.2F, 0.0F, 1.0F);
    this.resetYPRmodifier();
    Aircraft.xyz[2] = -0.2F * f;
    this.hierMesh().chunkSetLocate("GearC6_D0", Aircraft.xyz,
            Aircraft.ypr);
    this.hierMesh().chunkSetAngles("GearC8_D0", 0.0F, -15.0F * f, 0.0F);
  }

  protected void moveRudder(float paramFloat) {
	  this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30.0F * paramFloat,
			  0.0F);
	  if (FM.CT.GearControl > 0.5F) {
		  try{
		  if(FM.CT.DiffBrakesType == 0)    
			  this.hierMesh().chunkSetAngles("GearC7_D0", 0.0F,
					  40.0F * paramFloat, 0.0F);
		  }
		  catch(Exception localException){
		  }
	  }
  }

  public void moveSteering(float f)
  {
		if(FM.CT.getGear() < 0.99)
			hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 0.0F*f, 0.0F);
		else
			hierMesh().chunkSetAngles("GearC7_D0", 0.0F, 15F*f, 0.0F);
  }  

  protected void moveFlap(float paramFloat) {
    float f = -55.0F * paramFloat;
    this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
    this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
  }

  protected void moveFan(float paramFloat) {
    /* empty */
  }

  protected void moveAirBrake(float paramFloat) {
    this.resetYPRmodifier();
    this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -70.0F * paramFloat,
            0.0F);
    this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -70.0F * paramFloat,
            0.0F);
  }

  protected void hitBone(String paramString, Shot paramShot,
          Point3d paramPoint3d) {
    int ii = this.part(paramString);
    $1.rs(ii);
    if (paramString.startsWith("xx")) {
      if (paramString.startsWith("xxammo")) {
        if (World.Rnd().nextFloat(0.0F, 20000.0F) < paramShot.power
                && World.Rnd().nextFloat() < 0.25F) {
          FM.AS.setJamBullets(0, World.Rnd().nextInt(0, 2));
        }
        this.getEnergyPastArmor(11.4F, paramShot);
      } else if (paramString.startsWith("xxarmor")) {
        this.debuggunnery("Armor: Hit..");
        if (paramString.endsWith("p1")) {
          this.getEnergyPastArmor((13.350000381469727
                  / (Math.abs(Aircraft.v1.x)
                  + 9.999999747378752E-5)),
                  paramShot);
          if (paramShot.power <= 0.0F) {
            this.doRicochetBack(paramShot);
          }
        } else if (paramString.endsWith("p2")) {
          this.getEnergyPastArmor(8.770001F, paramShot);
        } else if (paramString.endsWith("P3")) {
          this.getEnergyPastArmor(8.770001F, paramShot);
        } else if (paramString.endsWith("g1")) {
          this.getEnergyPastArmor(((double) World.Rnd().nextFloat(40.0F, 60.0F)
                  / (Math.abs(Aircraft.v1.x) + 9.999999747378752E-5)),
                  paramShot);
          FM.AS.setCockpitState(paramShot.initiator,
                  FM.AS.astateCockpitState | 0x2);
          if (paramShot.power <= 0.0F) {
            this.doRicochetBack(paramShot);
          }
        }
      } else if (paramString.startsWith("xxcontrols")) {
        this.debuggunnery("Controls: Hit..");
        int i = paramString.charAt(10) - 48;
        switch (i) {
          case 1:
          case 2:
            if (World.Rnd().nextFloat() < 0.5F
                    && this.getEnergyPastArmor(1.1F, paramShot) > 0.0F) {
              this.debuggunnery("Controls: Ailerones Controls: Out..");
              FM.AS.setControlsDamage(paramShot.initiator, 0);
            }
            break;
          case 3:
          case 4:
            if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F,
                    2.93F),
                    paramShot) > 0.0F
                    && World.Rnd().nextFloat() < 0.25F) {
              this.debuggunnery("Controls: Elevator Controls: Disabled / Strings Broken..");
              FM.AS.setControlsDamage(paramShot.initiator, 1);
            }
            if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F,
                    2.93F),
                    paramShot) > 0.0F
                    && World.Rnd().nextFloat() < 0.25F) {
              this.debuggunnery("Controls: Rudder Controls: Disabled / Strings Broken..");
              FM.AS.setControlsDamage(paramShot.initiator, 2);
            }
            break;
        }
      } else if (paramString.startsWith("xxeng1")) {
        this.debuggunnery("Engine Module: Hit..");
        if (paramString.endsWith("bloc")) {
          this.getEnergyPastArmor(((double) World.Rnd().nextFloat(0.0F, 60.0F)
                  / (Math.abs(Aircraft.v1.x) + 9.999999747378752E-5)),
                  paramShot);
        }
        if (paramString.endsWith("cams")
                && this.getEnergyPastArmor(0.45F, paramShot) > 0.0F
                && (World.Rnd().nextFloat()
                < FM.EI.engines[0].getCylindersRatio() * 20.0F)) {
          FM.EI.engines[0].setCyliderKnockOut(paramShot.initiator,
                  World.Rnd().nextInt(1, (int) (paramShot.power
                  / 4800.0F)));
          this.debuggunnery("Engine Module: Engine Cams Hit, "
                  + FM.EI.engines[0].getCylindersOperable()
                  + "/" + FM.EI.engines[0].getCylinders()
                  + " Left..");
          if (World.Rnd().nextFloat() < paramShot.power / 24000.0F) {
            FM.AS.hitEngine(paramShot.initiator, 0, 2);
            this.debuggunnery("Engine Module: Engine Cams Hit - Engine Fires..");
          }
          if (paramShot.powerType == 3
                  && World.Rnd().nextFloat() < 0.75F) {
            FM.AS.hitEngine(paramShot.initiator, 0, 1);
            this.debuggunnery("Engine Module: Engine Cams Hit (2) - Engine Fires..");
          }
        }
        if (paramString.endsWith("eqpt")
                && World.Rnd().nextFloat() < paramShot.power / 24000.0F) {
          FM.AS.hitEngine(paramShot.initiator, 0, 3);
          this.debuggunnery("Engine Module: Hit - Engine Fires..");
        }
        if (!paramString.endsWith("exht")) {
          /* empty */
        }
      } else if (paramString.startsWith("xxcannon0")) {
        int i = paramString.charAt(9) - 49;
        if (this.getEnergyPastArmor(1.5F, paramShot) > 0.0F) {
          this.debuggunnery("Armament: Cannon (" + i
                  + ") Disabled..");
          FM.AS.setJamBullets(0, i);
          this.getEnergyPastArmor(World.Rnd().nextFloat(0.5F,
                  23.325F),
                  paramShot);
        }
      } else if (paramString.startsWith("xxtank")) {
        int i = paramString.charAt(6) - 49;
        if (this.getEnergyPastArmor(0.1F, paramShot) > 0.0F
                && World.Rnd().nextFloat() < 0.25F) {
          if (FM.AS.astateTankStates[i] == 0) {
            this.debuggunnery("Fuel Tank (" + i + "): Pierced..");
            FM.AS.hitTank(paramShot.initiator, i, 1);
            FM.AS.doSetTankState(paramShot.initiator, i, 1);
          }
          if (paramShot.powerType == 3
                  && World.Rnd().nextFloat() < 0.075F) {
            FM.AS.hitTank(paramShot.initiator, i, 2);
            this.debuggunnery("Fuel Tank (" + i + "): Hit..");
          }
        }
      } else if (paramString.startsWith("xxspar")) {
        this.debuggunnery("Spar Construction: Hit..");
        if (paramString.startsWith("xxsparlm")
                && this.chunkDamageVisible("WingLMid") > 2
                && this.getEnergyPastArmor((16.5F
                * World.Rnd().nextFloat(1.0F,
                1.5F)),
                paramShot) > 0.0F) {
          this.debuggunnery("Spar Construction: WingLMid Spars Damaged..");
          this.nextDMGLevels(1, 2, "WingLMid_D3",
                  paramShot.initiator);
        }
        if (paramString.startsWith("xxsparrm")
                && this.chunkDamageVisible("WingRMid") > 2
                && this.getEnergyPastArmor((16.5F
                * World.Rnd().nextFloat(1.0F,
                1.5F)),
                paramShot) > 0.0F) {
          this.debuggunnery("Spar Construction: WingRMid Spars Damaged..");
          this.nextDMGLevels(1, 2, "WingRMid_D3",
                  paramShot.initiator);
        }
        if (paramString.startsWith("xxsparlo")
                && this.chunkDamageVisible("WingLOut") > 2
                && this.getEnergyPastArmor((16.5F
                * World.Rnd().nextFloat(1.0F,
                1.5F)),
                paramShot) > 0.0F) {
          this.debuggunnery("Spar Construction: WingLOut Spars Damaged..");
          this.nextDMGLevels(1, 2, "WingLOut_D3",
                  paramShot.initiator);
        }
        if (paramString.startsWith("xxsparro")
                && this.chunkDamageVisible("WingROut") > 2
                && this.getEnergyPastArmor((16.5F
                * World.Rnd().nextFloat(1.0F,
                1.5F)),
                paramShot) > 0.0F) {
          this.debuggunnery("Spar Construction: WingROut Spars Damaged..");
          this.nextDMGLevels(1, 2, "WingROut_D3",
                  paramShot.initiator);
        }
      } else if (paramString.startsWith("xxhyd")) {
        FM.AS.setInternalDamage(paramShot.initiator, 3);
      } else if (paramString.startsWith("xxpnm")) {
        FM.AS.setInternalDamage(paramShot.initiator, 1);
      }
    } else {
      if (paramString.startsWith("xblister")) {
        FM.AS.setCockpitState(paramShot.initiator,
                FM.AS.astateCockpitState | 0x1);
        this.getEnergyPastArmor(0.05F, paramShot);
      }
      if (paramString.startsWith("xcf")) {
        this.hitChunk("CF", paramShot);
      } else if (paramString.startsWith("xnose")) {
        this.hitChunk("Nose", paramShot);
      } else if (paramString.startsWith("xTail")) {
        if (this.chunkDamageVisible("Tail1") < 3) {
          this.hitChunk("Tail1", paramShot);
        }
      } else if (paramString.startsWith("xkeel")) {
        if (this.chunkDamageVisible("Keel1") < 2) {
          this.hitChunk("Keel1", paramShot);
        }
      } else if (paramString.startsWith("xrudder")) {
        this.hitChunk("Rudder1", paramShot);
      } else if (paramString.startsWith("xstab")) {
        if (paramString.startsWith("xstabl")
                && this.chunkDamageVisible("StabL") < 2) {
          this.hitChunk("StabL", paramShot);
        }
        if (paramString.startsWith("xstabr")
                && this.chunkDamageVisible("StabR") < 1) {
          this.hitChunk("StabR", paramShot);
        }
      } else if (paramString.startsWith("xvator")) {
        if (paramString.startsWith("xvatorl")) {
          this.hitChunk("VatorL", paramShot);
        }
        if (paramString.startsWith("xvatorr")) {
          this.hitChunk("VatorR", paramShot);
        }
      } else if (paramString.startsWith("xwing")) {
        if (paramString.startsWith("xwinglin")
                && this.chunkDamageVisible("WingLIn") < 3) {
          this.hitChunk("WingLIn", paramShot);
        }
        if (paramString.startsWith("xwingrin")
                && this.chunkDamageVisible("WingRIn") < 3) {
          this.hitChunk("WingRIn", paramShot);
        }
        if (paramString.startsWith("xwinglmid")
                && this.chunkDamageVisible("WingLMid") < 3) {
          this.hitChunk("WingLMid", paramShot);
        }
        if (paramString.startsWith("xwingrmid")
                && this.chunkDamageVisible("WingRMid") < 3) {
          this.hitChunk("WingRMid", paramShot);
        }
        if (paramString.startsWith("xwinglout")
                && this.chunkDamageVisible("WingLOut") < 3) {
          this.hitChunk("WingLOut", paramShot);
        }
        if (paramString.startsWith("xwingrout")
                && this.chunkDamageVisible("WingROut") < 3) {
          this.hitChunk("WingROut", paramShot);
        }
      } else if (paramString.startsWith("xarone")) {
        if (paramString.startsWith("xaronel")) {
          this.hitChunk("AroneL", paramShot);
        }
        if (paramString.startsWith("xaroner")) {
          this.hitChunk("AroneR", paramShot);
        }
      } else if (paramString.startsWith("xgear")) {
          if(World.Rnd().nextFloat() < 0.05F && ((FlightModelMain) (super.FM)).Gears.isHydroOperable())
          {
              debuggunnery("Hydro System: Disabled..");
              ((FlightModelMain) (super.FM)).AS.setInternalDamage(paramShot.initiator, 0);
              ((FlightModelMain) (super.FM)).Gears.setHydroOperable(false);
              gearDamageFX(paramString);
          }
      } else if (paramString.startsWith("xpilot")
              || paramString.startsWith("xhead")) {
        int i = 0;
        int j;
        if (paramString.endsWith("a")) {
          i = 1;
          j = paramString.charAt(6) - 49;
        } else if (paramString.endsWith("b")) {
          i = 2;
          j = paramString.charAt(6) - 49;
        } else {
          j = paramString.charAt(5) - 49;
        }
        this.hitFlesh(j, paramShot, i);
      }
    }
  }

  protected boolean cutFM(int paramInt1, int paramInt2, Actor paramActor) {
    switch (paramInt1) {
      case 13: {
        FM.Gears.cgear = false;
        float t = World.Rnd().nextFloat();
        if (t < 0.1F) {
          FM.AS.hitEngine(this, 0, 100);
          if ((double) World.Rnd().nextFloat() < 0.49) {
            FM.EI.engines[0].setEngineDies(paramActor);
          }
        } else if ((double) t > 0.55) {
          FM.EI.engines[0].setEngineDies(paramActor);
        }
        break;
      }
      case 34:
        FM.Gears.lgear = false;
        break;
      case 37:
        FM.Gears.rgear = false;
        break;
      case 19:
        FM.CT.bHasAirBrakeControl = false;
        FM.EI.engines[0].setEngineDies(paramActor);
        break;
      case 11:
        FM.CT.bHasElevatorControl = false;
        FM.CT.bHasRudderControl = false;
        FM.CT.bHasRudderTrim = false;
        FM.CT.bHasElevatorTrim = false;
        break;
    }
    return super.cutFM(paramInt1, paramInt2, paramActor);
  }

  public float getAirPressure(float theAltitude) {
    float fBase = 1F - (L * theAltitude / T0);
    float fExponent = (G_CONST * M) / (R * L);
    return p0 * (float) Math.pow(fBase, fExponent);
  }

  public float getAirPressureFactor(float theAltitude) {
    return getAirPressure(theAltitude) / p0;
  }

  public float getAirDensity(float theAltitude) {
    return (getAirPressure(theAltitude) * M) / (R * (T0 - (L * theAltitude)));
  }

  public float getAirDensityFactor(float theAltitude) {
    return getAirDensity(theAltitude) / Rho0;
  }

  public float getMachForAlt(float theAltValue) {
    theAltValue /= 1000F; // get altitude in km
    int i = 0;
    for (i = 0; i < fMachAltX.length; i++) {
      if (fMachAltX[i] > theAltValue) {
        break;
      }
    }
    if (i == 0) {
      return fMachAltY[0];
    }
    float baseMach = fMachAltY[i - 1];
    float spanMach = fMachAltY[i] - baseMach;
    float baseAlt = fMachAltX[i - 1];
    float spanAlt = fMachAltX[i] - baseAlt;
    float spanMult = (theAltValue - baseAlt) / spanAlt;
    return baseMach + (spanMach * spanMult);
  }

  public float calculateMach() {
    return (FM.getSpeedKMH() / getMachForAlt(FM.getAltitude()));

  }

  public void soundbarier() {

    float f = getMachForAlt(FM.getAltitude()) - FM.getSpeedKMH();
    if (f < 0.5F) {
      f = 0.5F;
    }
    float f_0_ = FM.getSpeedKMH() - getMachForAlt(FM.getAltitude());
    if (f_0_ < 0.5F) {
      f_0_ = 0.5F;
    }
    if (calculateMach() <= 1.0) {
      FM.VmaxAllowed = FM.getSpeedKMH() + f;
      SonicBoom = 0.0F;
      isSonic = false;
    }
    if (calculateMach() >= 1.0) {
      FM.VmaxAllowed = FM.getSpeedKMH() + f_0_;
      isSonic = true;
    }
    if (FM.VmaxAllowed > 1300.0F) {
      FM.VmaxAllowed = 1300.0F;
    }

    if (isSonic && SonicBoom < 1) {
      super.playSound("aircraft.SonicBoom", true);
      super.playSound("aircraft.SonicBoomInternal", true);
      if (FM.actor == World.getPlayerAircraft()) {
        HUD.log(AircraftHotKeys.hudLogPowerId, "Mach 1 Exceeded!");
      }
      if (Config.isUSE_RENDER() && World.Rnd().nextFloat() < getAirDensityFactor(FM.getAltitude())) {
        shockwave = Eff3DActor.New(this, findHook("_Shockwave"), null, 1.0F, "3DO/Effects/Aircraft/Condensation.eff", -1F);
      }
      SonicBoom = 1;
    }
    if (calculateMach() > 1.01 || calculateMach() < 1.0) {
      Eff3DActor.finish(shockwave);
    }
  }

  public void engineSurge(float f) {
    if (((FlightModelMain) (super.FM)).AS.isMaster()) {
      if (curthrl == -1F) {
        curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
      } else {
        curthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
        if (curthrl < 1.05F) {
          if ((curthrl - oldthrl) / f > 20.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.40F) {
            if (FM.actor == World.getPlayerAircraft()) {
              HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
            }
            super.playSound("weapon.MGunMk108s", true);
            engineSurgeDamage += 1.0000000000000001E-002D * (double) (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
            ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
            if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
              FM.AS.hitEngine(this, 0, 100);
            }
            if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
              FM.EI.engines[0].setEngineDies(this);
            }
          }
          if ((curthrl - oldthrl) / f < -20.0F && (curthrl - oldthrl) / f > -100.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6) {
            super.playSound("weapon.MGunMk108s", true);
            engineSurgeDamage += 1.0000000000000001E-003D * (double) (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
            ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
            if (World.Rnd().nextFloat() < 0.40F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
              if (FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Engine Flameout!");
              }
              FM.EI.engines[0].setEngineStops(this);
            } else {
              if (FM.actor == World.getPlayerAircraft()) {
                HUD.log(AircraftHotKeys.hudLogWeaponId, "Compressor Stall!");
              }
            }
          }
        }
        oldthrl = curthrl;
      }
    }
  }

  public void update(float paramFloat) {
    if (FM.AS.isMaster() && Config.isUSE_RENDER()) {
      if (FM.EI.engines[0].getThrustOutput() > 0.50F
              && FM.EI.engines[0].getStage() == 6) {
        if (FM.EI.engines[0].getThrustOutput() > 0.50F) {
          FM.AS.setSootState(this, 0, 3);
        }
      } else {
        FM.AS.setSootState(this, 0, 0);
      }
      setExhaustFlame(Math.round(Aircraft.cvt(FM.EI.engines[0].getThrustOutput(),
              0.7F, 0.87F, 0.0F, 12.0F)),
              0);
      if (FM instanceof RealFlightModel) {
        umnr();
        $1.$1();
      }
      if (curctl == -1.0F) {
        curctl = oldctl = FM.CT.getAirBrake();
      } else {
        curctl = FM.CT.getAirBrake();
        if (curctl > 0.05F) {
          if (curctl - oldctl > 0.0F) {
            curst = true;
          } else if (curctl - oldctl == 0.0F && oldctl == 1.0F) {
            curst = true;
          } else {
            curst = false;
          }
        }
      }
      oldctl = curctl;
    }
    if(!isTrainer)
    	if ((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete
            && FM.getSpeedKMH() > 15.0F) {
      overrideBailout = true;
      FM.AS.bIsAboutToBailout = false;
      bailout();
    }
    soundbarier();
    engineSurge(paramFloat);
    super.update(paramFloat);
  }

  public void setExhaustFlame(int stage, int i) {
    if (i == 0) {
      switch (stage) {
        case 0:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 1:
          this.hierMesh().chunkVisible("Exhaust1", true);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 2:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", true);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 3:
          this.hierMesh().chunkVisible("Exhaust1", true);
          this.hierMesh().chunkVisible("Exhaust2", true);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", false);
        /* fall through */
        case 4:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", true);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 5:
          this.hierMesh().chunkVisible("Exhaust1", true);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", true);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 6:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", true);
          this.hierMesh().chunkVisible("Exhaust3", true);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 7:
          this.hierMesh().chunkVisible("Exhaust1", true);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", true);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 8:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", true);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", true);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 9:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", true);
          this.hierMesh().chunkVisible("Exhaust4", true);
          this.hierMesh().chunkVisible("Exhaust5", false);
          break;
        case 10:
          this.hierMesh().chunkVisible("Exhaust1", true);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", true);
          break;
        case 11:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", true);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", true);
          break;
        case 12:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", true);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", true);
          break;
        default:
          this.hierMesh().chunkVisible("Exhaust1", false);
          this.hierMesh().chunkVisible("Exhaust2", false);
          this.hierMesh().chunkVisible("Exhaust3", false);
          this.hierMesh().chunkVisible("Exhaust4", false);
          this.hierMesh().chunkVisible("Exhaust5", false);
      }
    }
  }

  private void bailout() {
    if (overrideBailout) {
      if (FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2) {
        if (FM.CT.cockpitDoorControl > 0.5F
                && FM.CT.getCockpitDoor() > 0.5F) {
          FM.AS.astateBailoutStep = (byte) 11;
          doRemoveBlisters();
        } else {
          FM.AS.astateBailoutStep = (byte) 2;
        }
      } else if (FM.AS.astateBailoutStep >= 2
              && FM.AS.astateBailoutStep <= 3) {
        switch (FM.AS.astateBailoutStep) {
          case 2:
            if (FM.CT.cockpitDoorControl < 0.5F) {
              doRemoveBlister1();
            }
            break;
          case 3:
            doRemoveBlisters();
            break;
        }
        if (FM.AS.isMaster()) {
          FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
        }
        AircraftState tmp178_177 = FM.AS;
        tmp178_177.astateBailoutStep = (byte) (tmp178_177.astateBailoutStep + 1);
        if (FM.AS.astateBailoutStep == 4) {
          FM.AS.astateBailoutStep = (byte) 11;
        }
      } else if (FM.AS.astateBailoutStep >= 11
              && FM.AS.astateBailoutStep <= 19) {
        int i = FM.AS.astateBailoutStep;
        if (FM.AS.isMaster()) {
          FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
        }
        AircraftState tmp383_382 = FM.AS;
        tmp383_382.astateBailoutStep = (byte) (tmp383_382.astateBailoutStep + 1);
        if (i == 11) {
          FM.setTakenMortalDamage(true, null);
          if (FM instanceof Maneuver
                  && ((Maneuver) FM).get_maneuver() != 44) {
            World.cur();
            if (FM.AS.actor != World.getPlayerAircraft()) {
              ((Maneuver) FM).set_maneuver(44);
            }
          }
        }
        if (FM.AS.astatePilotStates[i - 11] < 99) {
          this.doRemoveBodyFromPlane(i - 10);
          if (i == 11) {
            doEjectCatapult();
            FM.setTakenMortalDamage(true, null);
            FM.CT.WeaponControl[0] = false;
            FM.CT.WeaponControl[1] = false;
            FM.AS.astateBailoutStep = (byte) -1;
            overrideBailout = false;
            FM.AS.bIsAboutToBailout = true;
            ejectComplete = true;
          }
        }
      }
    }
  }

  private final void doRemoveBlister1() {
    if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1
            && FM.AS.getPilotHealth(0) > 0.0F) {
      this.hierMesh().hideSubTrees("Blister1_D0");
      Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
      localWreckage.collide(false);
      Vector3d localVector3d = new Vector3d();
      localVector3d.set(FM.Vwld);
      localWreckage.setSpeed(localVector3d);
    }
  }

  private final void doRemoveBlisters() {
    for (int i = 2; i < 10; i++) {
      if (this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1
              && FM.AS.getPilotHealth(i - 1) > 0.0F) {
        this.hierMesh().hideSubTrees("Blister" + i + "_D0");
        Wreckage localWreckage = new Wreckage(this,
                this.hierMesh().chunkFind("Blister" + i + "_D0"));
        localWreckage.collide(false);
        Vector3d localVector3d = new Vector3d();
        localVector3d.set(FM.Vwld);
        localWreckage.setSpeed(localVector3d);
      }
    }
  }

  public boolean ist() {
    return ts;
  }

  public float gmnr() {
    return mn;
  }

  public boolean inr() {
    return ictl;
  }

  private final void umnr() {
    Vector3d vf1 = FM.getVflow();
    mn = (float) vf1.lengthSquared();
    mn = (float) Math.sqrt((double) mn);
    Mig_15F Mig_15F = this;
    float f = mn;
    if (World.cur().Atm != null) {
      /* empty */
    }
    Mig_15F.mn = f / Atmosphere.sonicSpeed((float) FM.Loc.z);
    if (mn >= lteb) {
      ts = true;
    } else {
      ts = false;
    }
  }

  public void doSetSootState(int paramInt1, int paramInt2) {
    for (int i = 0; i < 2; i++) {
      if (FM.AS.astateSootEffects[paramInt1][i] != null) {
        Eff3DActor.finish(FM.AS.astateSootEffects[paramInt1][i]);
      }
      FM.AS.astateSootEffects[paramInt1][i] = null;
    }
    switch (paramInt2) {
      case 0:
        break;
      case 2:
        break;
      case 3:
        FM.AS.astateSootEffects[paramInt1][0] = Eff3DActor.New(this, this.findHook("_Engine1EF_01"), null, 0.75F, "3DO/Effects/Aircraft/TurboZippo.eff", -1.0F);
        break;
    }
  }

  /*synthetic*/ static float access$234(Mig_15F x0, double x1) {
    return x0.actl *= x1;
  }

  /*synthetic*/ static float access$334(Mig_15F x0, double x1) {
    return x0.ectl *= x1;
  }

  /*synthetic*/ static float access$434(Mig_15F x0, double x1) {
    return x0.rctl *= x1;
  }

  static {
    Class localClass = com.maddox.il2.objects.air.Mig_15F.class;
    Property.set(localClass, "originCountry", PaintScheme.countryRussia);
  }

}
