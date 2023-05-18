// Source File Name: F_86F_25E.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_86F_25E extends F_86F {

  public F_86F_25E() {
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
    Class localClass = com.maddox.il2.objects.air.F_86F_25E.class;
    new NetAircraft.SPAWN(localClass);
    Property.set(localClass, "iconFar_shortClassName", "F-86");
    Property.set(localClass, "meshName", "3DO/Plane/F-86E(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(localClass, "meshName_us", "3DO/Plane/F-86E(USA)/hier.him");
    Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
    Property.set(localClass, "meshName_gb", "3DO/Plane/F-86E(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme_gb", new PaintSchemeFMPar1956());
    Property.set(localClass, "meshName_it", "3DO/Plane/F-86E(Multi1)/hier.him");
    Property.set(localClass, "PaintScheme_it", new PaintSchemeFMPar1956());
    Property.set(localClass, "yearService", 1949.9F);
    Property.set(localClass, "yearExpired", 1960.3F);
    Property.set(localClass, "FlightModel", "FlightModels/F-86F-25E.fmd");
    Property.set(localClass, "cockpitClass", new Class[]{
              com.maddox.il2.objects.air.CockpitF_86F.class
            });
    Property.set(localClass, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(localClass, new int[]{
    		0, 0, 0, 0, 0, 0, 9, 9, 9, 9,
    		9, 3, 3, 9, 3, 3, 9, 2, 2, 9,
    		2, 2, 9, 9, 9, 9, 9, 3, 3, 9,
    		3, 3, 2, 2, 2, 2, 2, 2, 2, 2,
    		2, 2, 2, 2, 2, 2, 2, 2, 3, 3
    });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(localClass, new java.lang.String[]{
    		"_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
    		"_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08",
    		"_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalBomb03", "_ExternalDev14",
    		"_ExternalBomb04", "_ExternalBomb04", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10",
    		"_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalBomb07", "_ExternalBomb08"
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "default", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500lbs", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		"PylonF86_Bombs 1", "BombGun500lbs 1", null, "PylonF86_Bombs 1", "BombGun500lbs 1", null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x750lbs", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		"PylonF86_Bombs 1", "BombGun750lbs 1", null, "PylonF86_Bombs 1", "BombGun750lbs 1", null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x750lbsM117", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		"PylonF86_Bombs 1", "BombGun750lbsM117 1", null, "PylonF86_Bombs 1", "BombGun750lbsM117 1", null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x1000lbs", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		"PylonF86_Bombs 1", "BombGun1000lbs 1", null, "PylonF86_Bombs 1", "BombGun1000lbs 1", null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x100lbsM114", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		"PylonF86_Bombs 1", "BombGun1000lbs_M114 1", null, "PylonF86_Bombs 1", "BombGun1000lbs_M114 1", null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "4x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x120dt+2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "4x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun75Napalm 1", null, "PylonF86_Outboard 1",
    		"BombGun75Napalm 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap+2x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun75Napalm 1", null, "PylonF86_Outboard 1",
    		"BombGun75Napalm 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x75nap+2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun75Napalm 1", null, "PylonF86_Outboard 1",
    		"BombGun75Napalm 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun500lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun500lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500+2x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun500lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun500lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500+2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun500lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun500lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500+2x750", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", null, null,
    		null, "BombGun500lbs 1", null, null, "BombGun500lbs 1", null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun750lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x500+2xM117", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", null, null,
    		null, "BombGun500lbs 1", null, null, "BombGun500lbs 1", null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", null, "PylonF86_Outboard 1",
    		"BombGun750lbsM117 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "4x500", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", null, null,
    		null, "BombGun500lbs 1", null, null, "BombGun500lbs 1", null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun500lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun500lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x750", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun750lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x750+2x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun750lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x750+2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun750lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", null, "PylonF86_Outboard 1",
    		"BombGun750lbsM117 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117+2x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", null, "PylonF86_Outboard 1",
    		"BombGun750lbsM117 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM117+2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun750lbsM117 1", null, "PylonF86_Outboard 1",
    		"BombGun750lbsM117 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x1000", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun1000lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x1000+2x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun1000lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2x1000+2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs 1", null, "PylonF86_Outboard 1",
    		"BombGun1000lbs 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM114", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs_M114 1", null, "PylonF86_Outboard 1",
    		"BombGun1000lbs_M114 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM114+2x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs_M114 1", null, "PylonF86_Outboard 1",
    		"BombGun1000lbs_M114 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "2xM114+2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, "PylonF86_Outboard 1", "BombGun1000lbs_M114 1", null, "PylonF86_Outboard 1",
    		"BombGun1000lbs_M114 1", null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "16xHVAR", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1",
    		"RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "8xHVAR+2x120dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_TankC120galL 1", "FuelTankGun_TankC120galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		"RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "8xHVAR+2x207dt", new java.lang.String[]{
    		"MGunBrowningM3 270", "MGunBrowningM3 270", "MGunBrowningM3b 270", "MGunBrowningM3b 270", "MGunBrowningM3c 270", "MGunBrowningM3c 270", "PylonF86_Outboard 1", "PylonF86_Outboard 1", "FuelTankGun_Tank207galL 1", "FuelTankGun_Tank207galR 1",
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		"RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", "RocketGunHVARF84 1", null, null
    });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(localClass, "none", new java.lang.String[]{
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null,
    		null, null, null, null, null, null, null, null, null, null
    });
  }
}