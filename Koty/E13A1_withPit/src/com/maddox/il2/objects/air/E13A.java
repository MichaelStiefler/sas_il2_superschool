package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.AircraftState;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.Gear;
import com.maddox.il2.fm.Motor;
import com.maddox.il2.fm.Turret;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class E13A extends Scheme1BomberType90Mk1
  implements TypeSeaPlane, TypeScout, TypeStormovik
{
  public boolean bChangedPit = false;
  boolean bGunUp = false;
  public long btme = -1L;
  public float fGunPos = 0.0F;
  protected float flapps = 0.0F;
  
  protected boolean cutFM(int paramInt1, int paramInt2, Actor paramActor)
  {
    switch (paramInt1) {
    case 9:
    case 33:
      this.FM.Gears.bIsSail = false;
      break;
    case 10:
    case 36:
      this.FM.Gears.bIsSail = false;
    }

    return super.cutFM(paramInt1, paramInt2, paramActor);
  }

  public boolean canOpenBombBay()
  {
    if (this.FM.CT.Weapons[3] != null)
    {
      for (int i = 0; i < this.FM.CT.Weapons[3].length; i++) {
        if ((this.FM.CT.Weapons[3][i].haveBullets()) && 
          (this.FM.CT.Weapons[3][i].getHookName().startsWith("_ExternalBomb"))) {
          return false;
        }
      }
    }

    return true;
  }

  public void moveCockpitDoor(float paramFloat)
  {
    resetYPRmodifier();
    Aircraft.xyz[1] = Aircraft.cvt(paramFloat, 0.01F, 0.99F, 0.0F, -0.65F);
    hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
  }

  protected void moveFlap(float paramFloat)
  {
    hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45.0F * paramFloat, 0.0F);
    hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45.0F * paramFloat, 0.0F);
  }

  protected void moveElevator(float paramFloat)
  {
    hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30.0F * paramFloat, 0.0F);
    hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30.0F * paramFloat, 0.0F);
  }

  public static void moveGear(HierMesh paramHierMesh, float paramFloat1, float paramFloat2, float paramFloat3)
  {
  }

  protected void moveGear(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    moveGear(hierMesh(), paramFloat1, paramFloat2, paramFloat3);
  }

  public void moveSteering(float paramFloat)
  {
  }

  public void moveWheelSink() {
  }

  protected void moveRudder(float paramFloat) {
    hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30.0F * paramFloat, 0.0F);
    hierMesh().chunkSetAngles("GearR12_D0", 0.0F, -30.0F * paramFloat, 0.0F);
    hierMesh().chunkSetAngles("GearL12_D0", 0.0F, -30.0F * paramFloat, 0.0F);
  }

  public void rareAction(float paramFloat, boolean paramBoolean)
  {
    super.rareAction(paramFloat, paramBoolean);

    for (int i = 1; i < 5; i++)
      if (this.FM.getAltitude() < 3000.0F)
        hierMesh().chunkVisible("HMask" + i + "_D0", false);
      else
        hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));
  }

  public boolean turretAngles(int paramInt, float[] paramArrayOfFloat)
  {
    boolean bool = super.turretAngles(paramInt, paramArrayOfFloat);

    float f1 = -paramArrayOfFloat[0]; float f2 = paramArrayOfFloat[1];

    float f3 = -30.0F;
    if (f1 < 0.0F)
    {
      if (f1 < -20.0F)
        f3 = cvt(f1, -41.0F, -20.0F, -30.0F, -15.0F);
      else {
        f3 = cvt(f1, -20.0F, -10.0F, -15.0F, -8.0F);
      }

    }
    else if (f1 > 20.0F)
      f3 = cvt(f1, 20.0F, 41.0F, -15.0F, -30.0F);
    else {
      f3 = cvt(f1, 10.0F, 20.0F, -8.0F, -15.0F);
    }
    switch (paramInt) {
    case 0:
      if (f1 < -54.0F) { f1 = -54.0F; bool = false; }
      if (f1 > 54.0F) { f1 = 54.0F; bool = false; }
      if (f2 < f3) { f2 = f3; bool = false; }
      if (f2 > 55.0F) { f2 = 55.0F; bool = false;
      }
      if ((f1 > -0.9F) && (f1 < 0.9F) && (f2 < 15.5F)) bool = false;
      if ((f1 > -32.0F) && (f1 < 32.0F) && (f2 < -8.0F) && (f2 > -15.0F)) bool = false;

      break;
    }

    paramArrayOfFloat[0] = (-f1); paramArrayOfFloat[1] = f2;
    return bool;
  }

  public void doWoundPilot(int paramInt, float paramFloat)
  {
    switch (paramInt) {
    case 0:
    case 1:
      break;
    case 2:
      this.FM.turret[0].setHealth(paramFloat);
    }
  }

  public void doMurderPilot(int paramInt) {
    switch (paramInt) {
    case 0:
      hierMesh().chunkVisible("Pilot1_D0", false);
      hierMesh().chunkVisible("Pilot1_D1", true);
      hierMesh().chunkVisible("Head1_D0", false);
      hierMesh().chunkVisible("HMask1_D0", false);
      break;
    case 1:
      hierMesh().chunkVisible("Pilot2_D0", false);
      hierMesh().chunkVisible("Pilot2_D1", true);
      hierMesh().chunkVisible("HMask2_D0", false);
      break;
    case 2:
    case 3:
      if (hierMesh().isChunkVisible("Pilot3_D0"))
      {
        hierMesh().chunkVisible("Pilot3_D0", false);
        hierMesh().chunkVisible("Pilot3_D1", true);
        hierMesh().chunkVisible("HMask3_D0", false);
      }
      else
      {
        hierMesh().chunkVisible("Pilot4_D0", false);
        hierMesh().chunkVisible("Pilot4_D1", true);
        hierMesh().chunkVisible("HMask4_D0", false);
      }
      break;
    }
    if (this.FM.isPlayers())
        this.bChangedPit = true;
  }

  protected void moveWingFold(HierMesh paramHierMesh, float paramFloat) {
    paramHierMesh.chunkSetAngles("WingLFold_D0", 0.0F, -156.0F * paramFloat, 0.0F);
    paramHierMesh.chunkSetAngles("WingRFold_D0", 0.0F, -156.0F * paramFloat, 0.0F);
  }

  public void moveWingFold(float paramFloat) {
    moveWingFold(hierMesh(), paramFloat);
  }

  protected void moveBayDoor(float paramFloat) {
    hierMesh().chunkSetAngles("BDoor1_D0", 0.0F, -86.0F * paramFloat, 0.0F);
    hierMesh().chunkSetAngles("BDoor2_D0", 0.0F, -86.0F * paramFloat, 0.0F);
  }

  public void update(float paramFloat)
  {
    super.update(paramFloat);

    float f = this.FM.EI.engines[0].getControlRadiator();
    if (Math.abs(this.flapps - f) > 0.01F) {
      this.flapps = f;

      for (int i = 1; i < 14; i++) {
        String str = "Cowflap" + i + "_D0";
        hierMesh().chunkSetAngles(str, 0.0F, -30.0F * f, 0.0F);
      }
    }

    if (!this.bGunUp) {
      if (this.fGunPos > 0.0F) {
        this.fGunPos -= 0.2F * paramFloat;
        this.FM.turret[0].bIsOperable = false;
        hierMesh().chunkVisible("Turret1A_D0", false);
        hierMesh().chunkVisible("Turret1B_D0", false);
        hierMesh().chunkVisible("Pilot3_D0", true);
        hierMesh().chunkVisible("Pilot4_D0", false);
      }
    }
    else if (this.fGunPos < 1.0F) {
      this.fGunPos += 0.2F * paramFloat;
      if ((this.fGunPos > 0.8F) && (this.fGunPos < 0.9F)) {
        this.FM.turret[0].bIsOperable = true;
        hierMesh().chunkVisible("Turret1A_D0", true);
        hierMesh().chunkVisible("Turret1B_D0", true);
        hierMesh().chunkVisible("Pilot3_D0", false);
        hierMesh().chunkVisible("Pilot4_D0", true);
      }

    }

    if (this.fGunPos < 0.333F) {
      hierMesh().chunkSetAngles("Blister4_D0", 0.0F, -Aircraft.cvt(this.fGunPos, 0.0F, 0.333F, 0.0F, 41.0F), 0.0F);
    } else if (this.fGunPos < 0.666F)
    {
      resetYPRmodifier();
      Aircraft.xyz[1] = Aircraft.cvt(this.fGunPos, 0.333F, 0.666F, 0.0F, -0.4F);
      hierMesh().chunkSetLocate("Blister3_D0", Aircraft.xyz, Aircraft.ypr);
    }
    else {
      hierMesh().chunkSetAngles("Blister4_D0", 0.0F, -Aircraft.cvt(this.fGunPos, 0.666F, 1.0F, 41.0F, 71.0F), 0.0F);
    }
    if (this.FM.turret[0].bIsAIControlled) {
      if ((this.FM.turret[0].target != null) && (this.FM.AS.astatePilotStates[2] < 90)) {
        this.bGunUp = true;
      }
      if (Time.current() > this.btme) {
        this.btme = (Time.current() + World.Rnd().nextLong(5000L, 12000L));
        if ((this.FM.turret[0].target == null) && (this.FM.AS.astatePilotStates[2] < 90))
          this.bGunUp = false;
      }
    }
  }

  protected void hitBone(String paramString, Shot paramShot, Point3d paramPoint3d)
  {
    int i;
    if (paramString.startsWith("xx"))
    {
      if (paramString.startsWith("xxcontrols")) {
        i = paramString.charAt(10) - '0';
        switch (i) {
        case 1:
          if (getEnergyPastArmor(2.2F, paramShot) > 0.0F)
            this.FM.AS.setControlsDamage(paramShot.initiator, 0); break;
        case 3:
          if (getEnergyPastArmor(0.5F, paramShot) > 0.0F) {
            this.FM.AS.setControlsDamage(paramShot.initiator, 2);
          }
          break;
        }
        return;
      }
      if (paramString.startsWith("xxspar")) {
        if (((paramString.endsWith("t1")) || (paramString.endsWith("t2")) || (paramString.endsWith("t3")) || (paramString.endsWith("t4"))) && 
          (World.Rnd().nextFloat() < 0.1F) && (chunkDamageVisible("Tail1") > 2) && (getEnergyPastArmor(23.0F / (float)Math.sqrt(v1.y * v1.y + v1.z * v1.z), paramShot) > 0.0F)) {
          debuggunnery("*** Tail1 Spars Broken in Half..");
          msgCollision(this, "Tail1_D0", "Tail1_D0");
        }

        if (((paramString.endsWith("li1")) || (paramString.endsWith("li2")) || (paramString.endsWith("li3")) || (paramString.endsWith("li4"))) && 
          (chunkDamageVisible("WingLIn") > 2) && (getEnergyPastArmor(23.0F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)) {
          debuggunnery("*** WingLIn Spars Damaged..");
          nextDMGLevels(1, 2, "WingLIn_D3", paramShot.initiator);
        }

        if (((paramString.endsWith("ri1")) || (paramString.endsWith("ri2")) || (paramString.endsWith("ri3")) || (paramString.endsWith("ri4"))) && 
          (chunkDamageVisible("WingRIn") > 2) && (getEnergyPastArmor(23.0F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)) {
          debuggunnery("*** WingRIn Spars Damaged..");
          nextDMGLevels(1, 2, "WingRIn_D3", paramShot.initiator);
        }

        if (((paramString.endsWith("lm1")) || (paramString.endsWith("lm2")) || (paramString.endsWith("lm3")) || (paramString.endsWith("lm4"))) && 
          (chunkDamageVisible("WingLMid") > 2) && (getEnergyPastArmor(23.0F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)) {
          debuggunnery("*** WingLMid Spars Damaged..");
          nextDMGLevels(1, 2, "WingLMid_D3", paramShot.initiator);
        }

        if (((paramString.endsWith("rm1")) || (paramString.endsWith("rm2")) || (paramString.endsWith("rm3")) || (paramString.endsWith("rm4"))) && 
          (chunkDamageVisible("WingRMid") > 2) && (getEnergyPastArmor(23.0F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)) {
          debuggunnery("*** WingRMid Spars Damaged..");
          nextDMGLevels(1, 2, "WingRMid_D3", paramShot.initiator);
        }

        if (((paramString.endsWith("lo1")) || (paramString.endsWith("lo2")) || (paramString.endsWith("lo3")) || (paramString.endsWith("lo4"))) && 
          (chunkDamageVisible("WingLOut") > 2) && (getEnergyPastArmor(23.0F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)) {
          debuggunnery("*** WingLOut Spars Damaged..");
          nextDMGLevels(1, 2, "WingLOut_D3", paramShot.initiator);
        }

        if (((paramString.endsWith("ro1")) || (paramString.endsWith("ro2")) || (paramString.endsWith("ro3")) || (paramString.endsWith("ro4"))) && 
          (chunkDamageVisible("WingROut") > 2) && (getEnergyPastArmor(23.0F * World.Rnd().nextFloat(1.0F, 2.0F), paramShot) > 0.0F)) {
          debuggunnery("*** WingROut Spars Damaged..");
          nextDMGLevels(1, 2, "WingROut_D3", paramShot.initiator);
        }

        return;
      }
      if (paramString.startsWith("xxeng1")) {
        if (paramString.endsWith("case")) {
          if (getEnergyPastArmor(0.2F, paramShot) > 0.0F) {
            if (World.Rnd().nextFloat() < paramShot.power / 140000.0F) {
              this.FM.AS.setEngineStuck(paramShot.initiator, 0);
            }
            if (World.Rnd().nextFloat() < paramShot.power / 50000.0F) {
              this.FM.AS.hitEngine(paramShot.initiator, 0, 2);
            }
          }
          else if (World.Rnd().nextFloat() < 0.04F) {
            this.FM.EI.engines[0].setCyliderKnockOut(paramShot.initiator, 1);
          } else {
            this.FM.EI.engines[0].setReadyness(paramShot.initiator, this.FM.EI.engines[0].getReadyness() - 0.02F);
          }

          getEnergyPastArmor(12.0F, paramShot);
        }
        if (paramString.endsWith("cyls")) {
          if ((getEnergyPastArmor(0.85F, paramShot) > 0.0F) && (World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.75F)) {
            this.FM.EI.engines[0].setCyliderKnockOut(paramShot.initiator, World.Rnd().nextInt(1, (int)(paramShot.power / 19000.0F)));
            if (World.Rnd().nextFloat() < paramShot.power / 48000.0F) {
              this.FM.AS.hitEngine(paramShot.initiator, 0, 2);
            }
          }
          getEnergyPastArmor(25.0F, paramShot);
        }
        if (paramString.startsWith("xxeng1mag")) {
          i = paramString.charAt(9) - '1';
          this.FM.EI.engines[0].setMagnetoKnockOut(paramShot.initiator, i);
        }
        if (paramString.endsWith("oil1")) {
          this.FM.AS.hitOil(paramShot.initiator, 0);
        }
        return;
      }

      if (paramString.startsWith("xxoil")) {
        this.FM.AS.hitOil(paramShot.initiator, 0);
        getEnergyPastArmor(0.22F, paramShot);
      }

      if (paramString.startsWith("xxtank")) {
        i = paramString.charAt(6) - '1';
        if ((getEnergyPastArmor(0.1F, paramShot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
          if (this.FM.AS.astateTankStates[i] == 0) {
            this.FM.AS.hitTank(paramShot.initiator, i, 1);
            this.FM.AS.doSetTankState(paramShot.initiator, i, 1);
          }
          if ((paramShot.powerType == 3) && (World.Rnd().nextFloat() < 0.5F)) {
            this.FM.AS.hitTank(paramShot.initiator, i, 2);
          }
        }
      }
      return;
    }

    if (paramString.startsWith("xcf")) {
      if (chunkDamageVisible("CF") < 3)
        hitChunk("CF", paramShot);
    }
    else if (paramString.startsWith("xtail")) {
      if (chunkDamageVisible("Tail1") < 3)
        hitChunk("Tail1", paramShot);
    }
    else if (paramString.startsWith("xkeel")) {
      if (chunkDamageVisible("Keel1") < 2)
        hitChunk("Keel1", paramShot);
    }
    else if (paramString.startsWith("xrudder")) {
      hitChunk("Rudder1", paramShot);
    } else if (paramString.startsWith("xstabl")) {
      hitChunk("StabL", paramShot);
    } else if (paramString.startsWith("xstabr")) {
      hitChunk("StabR", paramShot);
    } else if (paramString.startsWith("xvatorl")) {
      hitChunk("VatorL", paramShot);
    } else if (paramString.startsWith("xwinglin")) {
      if (chunkDamageVisible("WingLIn") < 3)
        hitChunk("WingLIn", paramShot);
    }
    else if (paramString.startsWith("xwingrin")) {
      if (chunkDamageVisible("WingRIn") < 3)
        hitChunk("WingRIn", paramShot);
    }
    else if (paramString.startsWith("xwinglmid")) {
      if (chunkDamageVisible("WingLMid") < 3)
        hitChunk("WingLMid", paramShot);
    }
    else if (paramString.startsWith("xwingrmid")) {
      if (chunkDamageVisible("WingRMid") < 3)
        hitChunk("WingRMid", paramShot);
    }
    else if (paramString.startsWith("xwinglout")) {
      if (chunkDamageVisible("WingLOut") < 3)
        hitChunk("WingLOut", paramShot);
    }
    else if (paramString.startsWith("xwingrout")) {
      if (chunkDamageVisible("WingROut") < 3)
        hitChunk("WingROut", paramShot);
    }
    else if (paramString.startsWith("xaronel")) {
      hitChunk("AroneL", paramShot);
    } else if (paramString.startsWith("xaroner")) {
      hitChunk("AroneR", paramShot);
    } else if (paramString.startsWith("xengine")) {
      if (chunkDamageVisible("Engine1") < 2)
        hitChunk("Engine1", paramShot);
    }
    else if (paramString.startsWith("xturret1a")) {
      this.FM.AS.setJamBullets(10, 0);
      getEnergyPastArmor(World.Rnd().nextFloat(3.3F, 24.6F), paramShot);
    } else if (paramString.startsWith("xgearl")) {
      hitChunk("GearL2", paramShot);
    } else if (paramString.startsWith("xgearr")) {
      hitChunk("GearR2", paramShot);
    } else if ((paramString.startsWith("xpilot")) || (paramString.startsWith("xhead"))) {
      i = 0;
      int j;
      if (paramString.endsWith("a")) {
        i = 1;
        j = paramString.charAt(6) - '1';
      } else if (paramString.endsWith("b")) {
        i = 2;
        j = paramString.charAt(6) - '1';
      } else {
        j = paramString.charAt(5) - '1';
      }
      hitFlesh(j, paramShot, i);
    }
  }

  static
  {
    Class localClass = E13A.class;
    Property.set(localClass, "originCountry", PaintScheme.countryJapan);
  }
}