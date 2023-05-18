// Source File Name: Mig_15bis.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import java.util.Random;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;

public class Mig_15bis extends Mig_15F {

	protected Random random = new Random();
	protected int valkhenza_mode = random.nextInt(3);
    private static Vector3d v = new Vector3d();
    
  public Mig_15bis() {
  }
  
  public void update(float f){
	  super.update(f);
      if(valkhenza_mode != 0 && FM.isPlayers() && calculateMach() >= 0.85)
      {
    	  if(valkhenza_mode == 1)
    		  //Left bias
    		  v.x = cvt(calculateMach(), 0.85F, 1.0F, 0.0F, -800000F);
    	  if(valkhenza_mode == 2)
    		  //Right bias
    		  v.x = cvt(calculateMach(), 0.85F, 1.0F, 0.0F, 800000F);
          v.y = cvt(calculateMach(), 0.85F, 1.0F, 0.0F, -600000F);
          v.z = 0.0D;
          ((RealFlightModel)FM).gunMomentum(v, false);
      }
  }

  static {
    Class class1 = com.maddox.il2.objects.air.Mig_15bis.class;
    new NetAircraft.SPAWN(class1);
    Property.set(class1, "iconFar_shortClassName", "MiG-15");
    Property.set(class1, "meshName_ru", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(class1, "PaintScheme_ru", new PaintSchemeFMPar1956());
    Property.set(class1, "meshName_sk", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(class1, "PaintScheme_sk", new PaintSchemeFMPar1956());
    Property.set(class1, "meshName_ro", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar1956());
    Property.set(class1, "meshName_hu", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(class1, "PaintScheme_hu", new PaintSchemeFMPar1956());
    Property.set(class1, "meshName", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(class1, "yearService", 1949.9F);
    Property.set(class1, "yearExpired", 1960.3F);
    Property.set(class1, "FlightModel", "FlightModels/MiG-15F.fmd");
    Property.set(class1, "cockpitClass", new Class[]{
              com.maddox.il2.objects.air.CockpitMig_15F.class
            });
    Property.set(class1, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[]{
              1, 0, 0, 9, 9
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[]{
              "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "default", new java.lang.String[]{
            "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null
          });
  com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "2xDroptanks", new java.lang.String[]{
            "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1"
          });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "none", new java.lang.String[]{
              null, null, null, null, null
            });
  }
}