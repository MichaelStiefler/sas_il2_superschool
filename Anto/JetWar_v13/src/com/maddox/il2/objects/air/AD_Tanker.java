// Source File Name: AD_Tanker.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public abstract class AD_Tanker extends Scheme1
        implements TypeTransport, TypeTankerDrogue, TypeZBReceiver {

  public AD_Tanker() {
    prevGear = 0.0F;
    prevGear2 = 0.0F;
    prevWing = 1.0F;
    cGearPos = 0.0F;
    cGear = 0.0F;
    bNeedSetup = true;
    bDrogueExtended = true;
  }

  protected void moveAileron(float f) {
    hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
    hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
  }

  public void onAircraftLoaded() {
    super.onAircraftLoaded();
  }

  public static void moveGear(HierMesh hiermesh, float f, FlightModel flightmodel) {
    float f1 = 10F * f;
    if (flightmodel != null) {
      f = 10F * Math.max(f, flightmodel.CT.getAirBrake());
    } else {
      f = f1;
    }
    hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
    if (bGearExtending) {
      if (flightmodel == null) {
        float f2 = Aircraft.cvt(f1, 0.0F, 1.0F, 0.0F, 0.7071068F);
        f2 = 2.0F * f2 * f2;
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 41F * f2, 0.0F);
        f2 = Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 1.0F);
      }
      float f3;
      float f6;
      if (f < 4F) {
        f3 = f6 = Aircraft.cvt(f, 3F, 4F, 0.0F, 0.4F);
      } else {
        f3 = Aircraft.cvt(f, 4F, 8F, 0.75F, 2.0F);
        f3 = (float) Math.sqrt(f3);
        f3 = Aircraft.cvt(f3, (float) Math.sqrt(0.75D), (float) Math.sqrt(2D), 0.4F, 1.0F);
        f6 = Aircraft.cvt(f, 4F, 8.5F, 0.75F, 2.0F);
        f6 = (float) Math.sqrt(f6);
        f6 = Aircraft.cvt(f6, (float) Math.sqrt(0.75D), (float) Math.sqrt(2D), 0.4F, 1.0F);
      }
      hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 81F * f3, 0.0F);
      hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 84F * f3, 0.0F);
      hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 83F * f3, 0.0F);
      hiermesh.chunkSetAngles("GearL33_D0", 0.0F, -104F * f3, 0.0F);
      hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 40F * f3, 0.0F);
      hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -168F * f3, 0.0F);
      hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -90F * f3, 0.0F);
      hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 81F * f6, 0.0F);
      hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 84F * f6, 0.0F);
      hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 83F * f6, 0.0F);
      hiermesh.chunkSetAngles("GearR33_D0", 0.0F, -104F * f6, 0.0F);
      hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 40F * f6, 0.0F);
      hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -168F * f6, 0.0F);
      hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -90F * f6, 0.0F);
    } else {
      if (flightmodel == null) {
        float f4 = Aircraft.cvt(f1, 8.5F, 10F, 0.0F, 1.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 41F * f4, 0.0F);
        f4 = Aircraft.cvt(f1, 8.5F, 8.75F, 0.0F, 1.0F);
      }
      float f5;
      if (f > 7.5F) {
        f5 = Aircraft.cvt(f, 7.5F, 8.5F, 0.9F, 1.0F);
      } else {
        f5 = Aircraft.cvt(f, 3F, 7.5F, 0.0F, 0.9F);
      }
      hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 81F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 70F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 83F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearL33_D0", 0.0F, -104F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 40F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -168F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -90F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 81F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 84F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 83F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearR33_D0", 0.0F, -104F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 40F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -168F * f5, 0.0F);
      hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -90F * f5, 0.0F);
    }
  }

  public static void moveGear(HierMesh hiermesh, float f) {
    moveGear(hiermesh, f, null);
  }

  protected void moveGear(float f) {
    if (prevGear > f) {
      bGearExtending = false;
    } else {
      bGearExtending = true;
    }
    prevGear = f;
    moveGear(hierMesh(), f, FM);
    f *= 10F;
    if (bGearExtending) {
      float f1 = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.7071068F);
      cGearPos = 2.0F * f1 * f1;
    } else {
      cGearPos = Aircraft.cvt(f, 8.5F, 10F, 0.0F, 1.0F);
    }
  }

  public void moveSteering(float f) {
    hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
  }

  public void moveArrestorHook(float f) {
    hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -37F * f, 0.0F);
    arrestor = f;
  }

  public void moveWheelSink() {
    resetYPRmodifier();
    Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.231F, 0.0F, 0.231F);
    hierMesh().chunkSetLocate("GearL7_D0", Aircraft.xyz, Aircraft.ypr);
    Aircraft.xyz[1] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.231F, 0.0F, -0.231F);
    hierMesh().chunkSetLocate("GearR7_D0", Aircraft.xyz, Aircraft.ypr);
  }

  protected void moveWingFold(HierMesh hiermesh, float f) {
    f *= 18F;
    if (bGearExtending) {
      if (f < 1.5F) {
        hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.5F, 0.0F, 2.6F), 0.0F);
        hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, Aircraft.cvt(f, 0.0F, 1.5F, 0.0F, 2.6F), 0.0F);
      } else if (f < 2.5F) {
        hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, Aircraft.cvt(f, 1.5F, 2.5F, 2.6F, 5.1F), 0.0F);
        hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, Aircraft.cvt(f, 1.5F, 2.5F, 2.6F, 5.1F), 0.0F);
      } else {
        hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, Aircraft.cvt(f, 2.5F, 17.9F, 5.1F, 105F), 0.0F);
        hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, Aircraft.cvt(f, 2.5F, 16F, 5.1F, 105F), 0.0F);
      }
    } else if (f < 9F) {
      if (f < 6.8F) {
        hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, Aircraft.cvt(f, 0.01F, 6.8F, 0.0F, 45F), 0.0F);
      } else {
        hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, Aircraft.cvt(f, 6.8F, 9F, 45F, 50F), 0.0F);
      }
      if (f < 7.5F) {
        hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, Aircraft.cvt(f, 0.75F, 7.5F, 0.0F, 45F), 0.0F);
      } else {
        hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, Aircraft.cvt(f, 7.5F, 9F, 45F, 50F), 0.0F);
      }
    } else if (f < 11F) {
      hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, Aircraft.cvt(f, 9F, 11F, 50F, 60F), 0.0F);
      hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, Aircraft.cvt(f, 9F, 11F, 50F, 60F), 0.0F);
    } else {
      hiermesh.chunkSetAngles("WingLFold_D0", 0.0F, Aircraft.cvt(f, 11F, 15.75F, 60F, 105F), 0.0F);
      hiermesh.chunkSetAngles("WingRFold_D0", 0.0F, Aircraft.cvt(f, 11F, 15.75F, 60F, 105F), 0.0F);
    }
  }

  public void moveWingFold(float f) {
    if (prevWing > f) {
      bGearExtending = false;
    } else {
      bGearExtending = true;
    }
    prevWing = f;
    if (f < 0.001F) {
      setGunPodsOn(true);
      hideWingWeapons(false);
    } else {
      setGunPodsOn(false);
      FM.CT.WeaponControl[0] = false;
      hideWingWeapons(true);
    }
    moveWingFold(hierMesh(), f);
  }

  protected void moveAirBrake(float f) {
    resetYPRmodifier();
    hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -70F * f, 0.0F);
    hierMesh().chunkSetAngles("BrakeB01_D0", 0.0F, -30F * f, 0.0F);
    hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -70F * f, 0.0F);
    hierMesh().chunkSetAngles("BrakeB02_D0", 0.0F, 30F * f, 0.0F);
    if ((double) f < 0.20000000000000001D) {
      Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.18F, 0.0F, -0.05F);
    } else {
      Aircraft.xyz[2] = Aircraft.cvt(f, 0.22F, 0.99F, -0.05F, -0.22F);
    }
    hierMesh().chunkSetLocate("BrakeB01e_D0", Aircraft.xyz, Aircraft.ypr);
    hierMesh().chunkSetLocate("BrakeB02e_D0", Aircraft.xyz, Aircraft.ypr);
  }

  public void moveCockpitDoor(float f) {
    resetYPRmodifier();
    Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.625F);
    Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.06845F);
    Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 1.0F);
    hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
    resetYPRmodifier();
    Aircraft.xyz[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.13F);
    Aircraft.ypr[2] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -8F);
    hierMesh().chunkSetLocate("Pilot1_D0", Aircraft.xyz, Aircraft.ypr);
    if (Config.isUSE_RENDER()) {
      if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) {
        Main3D.cur3D().cockpits[0].onDoorMoved(f);
      }
      setDoorSnd(f);
    }
  }

  protected void moveFlap(float f) {
    float f1 = 55F * f;
    hierMesh().chunkSetAngles("Flap1_D0", 0.0F, 0.0F, f1);
    hierMesh().chunkSetAngles("Flap2_D0", 0.0F, 0.0F, f1);
  }

  public void update(float f) {
    super.update(f);
    if (bNeedSetup) {
      cGear = FM.CT.GearControl;
      bNeedSetup = false;
    }
    cGear = filter(f, FM.CT.GearControl, cGear, 999.9F, FM.CT.dvGear);
    if (prevGear2 > cGear) {
      bGearExtending2 = false;
    } else {
      bGearExtending2 = true;
    }
    prevGear2 = cGear;
    float f1 = 10F * cGear;
    if (bGearExtending2) {
      float f2 = Aircraft.cvt(f1, 0.0F, 1.0F, 0.0F, 0.7071068F);
      cGearPos = 2.0F * f2 * f2;
      hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 41F * cGearPos, 0.0F);
      f2 = Aircraft.cvt(f1, 0.0F, 0.25F, 0.0F, 1.0F);
      hierMesh().chunkSetAngles("GearC4_D0", 0.0F, 140F * f2, 0.0F);
      hierMesh().chunkSetAngles("GearC5_D0", 0.0F, 140F * f2, 0.0F);
    } else {
      cGearPos = Aircraft.cvt(f1, 8.5F, 10F, 0.0F, 1.0F);
      hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 41F * cGearPos, 0.0F);
    }
    if (FM.getAltitude() < 1000 || FM.CT.getGear() > 0.0 || FM.CT.getArrestor() > 0.0) {
      hierMesh().chunkVisible("FuelLine1_D0", false);
      hierMesh().chunkVisible("Drogue1_D0", false);
      bDrogueExtended = false;
    } else {
      hierMesh().chunkVisible("FuelLine1_D0", true);
      hierMesh().chunkVisible("Drogue1_D0", true);
      bDrogueExtended = true;
    }
  }

  public boolean isDrogueExtended()
  {
    return bDrogueExtended;
  }

  public void doMurderPilot(int i) {
    switch (i) {
      case 0: // '\0'
        hierMesh().chunkVisible("Pilot1_D0", false);
        hierMesh().chunkVisible("Head1_D0", false);
        hierMesh().chunkVisible("Pilot1_D1", true);
        hierMesh().chunkVisible("HMask1_D0", false);
        break;
    }
  }

  protected void hitBone(String s, Shot shot, Point3d point3d) {
    if (s.startsWith("xx")) {
      if (s.startsWith("xxarmor")) {
        if (s.endsWith("p1")) {
          getEnergyPastArmor(6.78F, shot);
        } else if (s.endsWith("g1")) {
          getEnergyPastArmor(9.96F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
        } else if (s.endsWith("g2")) {
          getEnergyPastArmor(World.Rnd().nextFloat(30F, 50F) / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot);
        }
      } else if (s.startsWith("xxcontrols")) {
        if (s.endsWith("1")) {
          if (World.Rnd().nextFloat() < 0.12F) {
            FM.AS.setControlsDamage(shot.initiator, 1);
            Aircraft.debugprintln(this, "*** Evelator Controls Out..");
          }
          if (World.Rnd().nextFloat() < 0.12F) {
            FM.AS.setControlsDamage(shot.initiator, 2);
            Aircraft.debugprintln(this, "*** Rudder Controls Out..");
          }
          if (World.Rnd().nextFloat() < 0.12F) {
            FM.AS.setControlsDamage(shot.initiator, 0);
            Aircraft.debugprintln(this, "*** Arone Controls Out..");
          }
        } else if (s.endsWith("2")) {
          if (World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F) {
            FM.AS.setControlsDamage(shot.initiator, 1);
            Aircraft.debugprintln(this, "*** Evelator Controls Out..");
          }
        } else if (s.endsWith("3") && World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F) {
          FM.AS.setControlsDamage(shot.initiator, 2);
          Aircraft.debugprintln(this, "*** Rudder Controls Out..");
        }
      } else if (s.startsWith("xxspar")) {
        if ((s.endsWith("t1") || s.endsWith("t2") || s.endsWith("t3") || s.endsWith("t4")) && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(3.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
          nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
        }
        if ((s.endsWith("li1") || s.endsWith("li2") || s.endsWith("li3") || s.endsWith("li4")) && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
          nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
        }
        if ((s.endsWith("ri1") || s.endsWith("ri2") || s.endsWith("ri3") || s.endsWith("ri4")) && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
          nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
        }
        if ((s.endsWith("lm1") || s.endsWith("lm2") || s.endsWith("lm3") || s.endsWith("lm4")) && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
          nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
        }
        if ((s.endsWith("rm1") || s.endsWith("rm2") || s.endsWith("rm3") || s.endsWith("rm4")) && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
          nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
        }
        if ((s.endsWith("lo1") || s.endsWith("lo2")) && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
          nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
        }
        if ((s.endsWith("ro1") || s.endsWith("ro2")) && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
          nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
        }
        if ((s.endsWith("sl1") || s.endsWith("sl2") || s.endsWith("sl3") || s.endsWith("sl4") || s.endsWith("sl5")) && chunkDamageVisible("StabL") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** StabL Spars Damaged..");
          nextDMGLevels(1, 2, "StabL_D3", shot.initiator);
        }
        if ((s.endsWith("sr1") || s.endsWith("sr2") || s.endsWith("sr3") || s.endsWith("sr4") || s.endsWith("sr5")) && chunkDamageVisible("StabR") > 2 && getEnergyPastArmor(3.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
          Aircraft.debugprintln(this, "*** StabR Spars Damaged..");
          nextDMGLevels(1, 2, "StabR_D3", shot.initiator);
        }
        if (s.endsWith("e1")) {
          getEnergyPastArmor(6F, shot);
        }
      } else if (s.startsWith("xxeng1")) {
        if (s.endsWith("prp") && getEnergyPastArmor(0.1F, shot) > 0.0F) {
          FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
        }
        if (s.endsWith("cas") && getEnergyPastArmor(0.1F, shot) > 0.0F) {
          if (World.Rnd().nextFloat() < shot.power / 200000F) {
            FM.AS.setEngineStuck(shot.initiator, 0);
            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Stucks..");
          }
          if (World.Rnd().nextFloat() < shot.power / 50000F) {
            FM.AS.hitEngine(shot.initiator, 0, 2);
            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Engine Damaged..");
          }
          if (World.Rnd().nextFloat() < shot.power / 28000F) {
            FM.EI.engines[0].setCyliderKnockOut(shot.initiator, 1);
            Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Cylinder Feed Out, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
          }
          FM.EI.engines[0].setReadyness(shot.initiator, FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
          Aircraft.debugprintln(this, "*** Engine Crank Case Hit - Readyness Reduced to " + FM.EI.engines[0].getReadyness() + "..");
        }
        if (s.endsWith("cyl") && getEnergyPastArmor(0.45F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 1.75F) {
          FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
          Aircraft.debugprintln(this, "*** Engine Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
          if (FM.AS.astateEngineStates[0] < 1) {
            FM.AS.hitEngine(shot.initiator, 0, 1);
          }
          if (World.Rnd().nextFloat() < shot.power / 24000F) {
            FM.AS.hitEngine(shot.initiator, 0, 3);
            Aircraft.debugprintln(this, "*** Engine Cylinders Hit - Engine Fires..");
          }
          getEnergyPastArmor(25F, shot);
        }
        if (s.endsWith("sup") && getEnergyPastArmor(0.05F, shot) > 0.0F) {
          FM.EI.engines[0].setKillCompressor(shot.initiator);
        }
      } else if (s.startsWith("xxtank")) {
        int i = s.charAt(6) - 49;
        if (getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F) {
          FM.AS.hitTank(shot.initiator, i, 1);
          if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.11F) {
            FM.AS.hitTank(shot.initiator, i, 2);
          }
        }
      } else {
        if (s.startsWith("xxmgunl1") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
          FM.AS.setJamBullets(0, 0);
        }
        if (s.startsWith("xxmgunr1") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
          FM.AS.setJamBullets(0, 1);
        }
        if (s.startsWith("xxmgunl2") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
          FM.AS.setJamBullets(0, 2);
        }
        if (s.startsWith("xxmgunr2") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
          FM.AS.setJamBullets(0, 3);
        }
        if (s.startsWith("xxhispa1") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
          FM.AS.setJamBullets(1, 0);
        }
        if (s.startsWith("xxhispa2") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
          FM.AS.setJamBullets(1, 1);
        }
        if (s.startsWith("xxhispa3") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
          FM.AS.setJamBullets(1, 2);
        }
        if (s.startsWith("xxhispa4") && getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
          FM.AS.setJamBullets(1, 3);
        }
      }
    } else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
      hitChunk("CF", shot);
      if (point3d.x > -2.2000000000000002D) {
        if (World.Rnd().nextFloat() < 0.1F) {
          FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
        }
        if (point3d.x < -1D && point3d.z > 0.55000000000000004D) {
          FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
        }
        if (point3d.z > 0.65000000000000002D) {
          FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
        }
        if (Math.abs(Aircraft.v1.x) < 0.80000001192092896D) {
          if (point3d.y > 0.0D) {
            if (World.Rnd().nextFloat() < 0.1F) {
              FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            }
            if (World.Rnd().nextFloat() < 0.1F) {
              FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
            }
          } else {
            if (World.Rnd().nextFloat() < 0.1F) {
              FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            }
            if (World.Rnd().nextFloat() < 0.1F) {
              FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            }
          }
        }
      }
    } else if (s.startsWith("xeng")) {
      if (chunkDamageVisible("Engine1") < 3) {
        hitChunk("Engine1", shot);
      }
    } else if (s.startsWith("xtail")) {
      if (chunkDamageVisible("Tail1") < 3) {
        hitChunk("Tail1", shot);
      }
    } else if (s.startsWith("xkeel")) {
      hitChunk("Keel1", shot);
    } else if (s.startsWith("xrudder")) {
      hitChunk("Rudder1", shot);
    } else if (s.startsWith("xstab")) {
      if (s.startsWith("xstabl") && chunkDamageVisible("StabL") < 3) {
        hitChunk("StabL", shot);
      }
      if (s.startsWith("xstabr") && chunkDamageVisible("StabR") < 3) {
        hitChunk("StabR", shot);
      }
    } else if (s.startsWith("xvator")) {
      if (s.startsWith("xvatorl")) {
        hitChunk("VatorL", shot);
      }
      if (s.startsWith("xvatorr")) {
        hitChunk("VatorR", shot);
      }
    } else if (s.startsWith("xwing")) {
      if (s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3) {
        hitChunk("WingLIn", shot);
      }
      if (s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3) {
        hitChunk("WingRIn", shot);
      }
      if (s.startsWith("xwinglmid")) {
        if (chunkDamageVisible("WingLMid") < 3) {
          hitChunk("WingLMid", shot);
        }
        if (World.Rnd().nextFloat() < shot.mass + 0.02F) {
          FM.AS.hitOil(shot.initiator, 0);
        }
      }
      if (s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3) {
        hitChunk("WingRMid", shot);
      }
      if (s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3) {
        hitChunk("WingLOut", shot);
      }
      if (s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3) {
        hitChunk("WingROut", shot);
      }
    } else if (s.startsWith("xarone")) {
      if (s.startsWith("xaronel")) {
        hitChunk("AroneL", shot);
      }
      if (s.startsWith("xaroner")) {
        hitChunk("AroneR", shot);
      }
    } else if (s.startsWith("xoil")) {
      if (getEnergyPastArmor(0.5F, shot) > 0.0F) {
        debuggunnery("Engine Module: Oil Radiator Hit, Oil Radiator Pierced..");
        FM.AS.hitOil(shot.initiator, 0);
      }
    } else if (s.startsWith("xwater")) {
      if (FM.AS.astateEngineStates[0] == 0) {
        debuggunnery("Engine Module: Water Radiator Pierced..");
        FM.AS.hitEngine(shot.initiator, 0, 1);
        FM.AS.doSetEngineState(shot.initiator, 0, 1);
      } else if (FM.AS.astateEngineStates[0] == 1) {
        debuggunnery("Engine Module: Water Radiator Pierced..");
        FM.AS.hitEngine(shot.initiator, 0, 1);
        FM.AS.doSetEngineState(shot.initiator, 0, 2);
      }
    } else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
      byte byte0 = 0;
      int j;
      if (s.endsWith("a")) {
        byte0 = 1;
        j = s.charAt(6) - 49;
      } else if (s.endsWith("b")) {
        byte0 = 2;
        j = s.charAt(6) - 49;
      } else {
        j = s.charAt(5) - 49;
      }
      hitFlesh(j, shot, byte0);
    }
  }

  public void rareAction(float f, boolean flag) {
    super.rareAction(f, flag);
    if (FM.getAltitude() < 3000F) {
      hierMesh().chunkVisible("HMask1_D0", false);
    } else {
      hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
    }
  }

  private static final float filter(float f, float f1, float f2, float f3, float f4) {
    float f5 = (float) Math.exp(-f / f3);
    float f6 = f1 + (f2 - f1) * f5;
    if (f6 < f1) {
      f6 += f4 * f;
      if (f6 > f1) {
        f6 = f1;
      }
    } else if (f6 > f1) {
      f6 -= f4 * f;
      if (f6 < f1) {
        f6 = f1;
      }
    }
    return f6;
  }
  private float prevGear;
  private float prevGear2;
  private static boolean bGearExtending = false;
  private static boolean bGearExtending2 = false;
  private float prevWing;
  private float cGearPos;
  private float cGear;
  private boolean bNeedSetup;
  protected float arrestor;
  private boolean bDrogueExtended;

  static {
    Class class1 = com.maddox.il2.objects.air.AD_Tanker.class;
    Property.set(class1, "originCountry", PaintScheme.countryUSA);
  }
}