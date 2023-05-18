// Source File Name: F_86A5.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import com.maddox.il2.ai.War;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class F_86A5 extends F_86F {

  public F_86A5() {
  }

  public void checkHydraulicStatus() {
	    if (FM.EI.engines[0].getStage() < 6 && FM.Gears.nOfGearsOnGr > 0) {
	      gearTargetAngle = 90F;
	      hasHydraulicPressure = false;
	      FM.CT.bHasAileronControl = false;
	      FM.CT.AirBrakeControl = 1.0F;
	    } else if (FM.EI.engines[0].getStage() < 6) {
	      hasHydraulicPressure = false;
	      FM.CT.bHasAileronControl = false;
	      FM.CT.bHasAirBrakeControl = false;
	    }
	    if (!hasHydraulicPressure) {
	      gearTargetAngle = 0.0F;
	      hasHydraulicPressure = true;
	      FM.CT.bHasAileronControl = true;
	      FM.CT.bHasAirBrakeControl = true;
	    }

	  }
	  
  public void typeFighterAceMakerRangeFinder() {
	    if (k14Mode == 2) {
	      return;
	    }
	    if (!Config.isUSE_RENDER()) {
	      return;
	    }
	    hunted = Main3D.cur3D().getViewPadlockEnemy();
	    if (hunted == null) {
	      k14Distance = 200F;
	      hunted = War.GetNearestEnemyAircraft(FM.actor, 2000F, 9);
	    }
	    if (hunted != null) {
	      k14Distance = (float) FM.actor.pos.getAbsPoint().distance(
	              hunted.pos.getAbsPoint());
	      if (k14Distance > 800F) {
	        k14Distance = 800F;
	      } else if (k14Distance < 200F) {
	        k14Distance = 200F;
	      }
	    }
	  }
  
  public void update(float f) {
	    super.update(f);
	    if (FM.getSpeed() > 5.0F) {
	      moveSlats(f);
	      bSlatsOff = false;
	    } else {
	      slatsOff();
	    }
	  }

	  protected void moveSlats(float paramFloat) {
	    resetYPRmodifier();
	    Aircraft.xyz[0] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.15F);
	    Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 0.1F);
	    Aircraft.xyz[2] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.065F);
	    hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
	    hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
	    Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.1F);
	    hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
	    hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
	  }

	  protected void slatsOff() {
	    if (super.bSlatsOff) {
	      return;
	    } else {
	      resetYPRmodifier();
	      Aircraft.xyz[0] = -0.15F;
	      Aircraft.xyz[1] = 0.1F;
	      Aircraft.xyz[2] = -0.065F;
	      hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 8.5F, 0.0F);
	      hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
	      Aircraft.xyz[1] = -0.1F;
	      hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 8.5F, 0.0F);
	      hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
	      super.bSlatsOff = true;
	      return;
	    }
	  }
	  
  static {
    Class localClass = com.maddox.il2.objects.air.F_86A5.class;
    new NetAircraft.SPAWN(localClass);
    Property.set(localClass, "iconFar_shortClassName", "F-86");
    Property.set(localClass, "meshName", "3DO/Plane/F-86A(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(localClass, "meshName_us", "3DO/Plane/F-86A(USA)/hier.him");
    Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
    Property.set(localClass, "yearService", 1949.9F);
    Property.set(localClass, "yearExpired", 1960.3F);
    Property.set(localClass, "FlightModel", "FlightModels/F-86A.fmd");
    Property.set(localClass, "cockpitClass", new Class[]{
              com.maddox.il2.objects.air.CockpitF_86A.class
            });
    Property.set(localClass, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(localClass, new int[]{
              0, 0, 0, 0, 0, 0, 9, 9, 9, 9,
              3, 3, 9, 9, 9, 9, 9, 9, 9, 9,
              2, 2, 2, 2, 2, 2, 2, 2
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(localClass, new java.lang.String[]{
              "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
              "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12",
              "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "default", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x207gal", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120gal", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500lbs", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              "BombGun500lbs 1", "BombGun500lbs 1", "PylonF86_Bombs 1", "PylonF86_Bombs 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x750lbs", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              "BombGun750lbs 1", "BombGun750lbs 1", "PylonF86_Bombs 1", "PylonF86_Bombs 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              "BombGun750lbsM117 1", "BombGun750lbsM117 1", "PylonF86_Bombs 1", "PylonF86_Bombs 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x1000lbs", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              "BombGun1000lbs 1", "BombGun1000lbs 1", "PylonF86_Bombs 1", "PylonF86_Bombs 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM114", new java.lang.String[]{
              "MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
              "BombGun1000lbs_M114 1", "BombGun1000lbs_M114 1", "PylonF86_Bombs 1", "PylonF86_Bombs 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "none", new java.lang.String[]{
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null
            });
  }
}