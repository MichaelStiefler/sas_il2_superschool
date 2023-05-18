// Source File Name: MiG_15SB.java
// Author:           
// Last Modified by: Storebror 2011-06-03
package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.*;
import com.maddox.sound.Sample;
import com.maddox.sound.SoundFX;

public class MiG_15SB extends Mig_15F
        implements TypeStormovik {

  public MiG_15SB() {
	  smplSirena.setInfinite(true);
  }
  
  private SoundFX fxSirena = newSound("aircraft.Sirena2", false);
  private Sample smplSirena = new Sample("sample.Sirena2.wav", 256, 65535);
  private boolean sirenaSoundPlaying = false;
  private boolean bRadarWarning;
 
 private boolean sirenaWarning()
  {
	  Point3d point3d = new Point3d();
	  pos.getAbs(point3d);
	  Vector3d vector3d = new Vector3d();
	  Aircraft aircraft = World.getPlayerAircraft();
	  double d = Main3D.cur3D().land2D.worldOfsX() + ((Actor) (aircraft)).pos.getAbsPoint().x;
	  double d1 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().y;
	  double d2 = Main3D.cur3D().land2D.worldOfsY() + ((Actor) (aircraft)).pos.getAbsPoint().z;
	  int i = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getYaw() - 90D));
	  if(i < 0)
		  i = 360 + i;
	  int j = (int)(-((double)((Actor) (aircraft)).pos.getAbsOrient().getPitch() - 90D));
	  if(j < 0)
		  j = 360 + j;
		  Actor actor = War.getNearestEnemy(this, 4000F);
		  if((actor instanceof Aircraft) && actor.getArmy() != World.getPlayerArmy() && ((actor instanceof TypeFighterAceMaker) && (actor instanceof TypeRadarGunsight)) && actor != World.getPlayerAircraft() && actor.getSpeed(vector3d) > 20D)
		  {
			  pos.getAbs(point3d);
			  double d3 = Main3D.cur3D().land2D.worldOfsX() + actor.pos.getAbsPoint().x;
			  double d4 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().y;
			  double d5 = Main3D.cur3D().land2D.worldOfsY() + actor.pos.getAbsPoint().z;
			  new String();
			  new String();
			  double d6 = (int)(Math.ceil((d2 - d5) / 10D) * 10D);
			  new String();
			  double d7 = d3 - d;
			  double d8 = d4 - d1;
			  float f = 57.32484F * (float)Math.atan2(d8, -d7);
			  int i1 = (int)(Math.floor((int)f) - 90D);
			  if(i1 < 0)
				  i1 = 360 + i1;
			  int j1 = i1 - i;
			  double d9 = d - d3;
			  double d10 = d1 - d4;
			  double d11 = Math.sqrt(d6 * d6);
			  int k1 = (int)(Math.ceil(Math.sqrt(d10 * d10 + d9 * d9) / 10D) * 10D);
			  float f1 = 57.32484F * (float)Math.atan2(k1, d11);
			  int l1 = (int)(Math.floor((int)f1) - 90D);
			  if(l1 < 0)
				  l1 = 360 + l1;
			  int i2 = l1 - j;
			  int j2 = (int)(Math.ceil(((double)k1 * 3.2808399000000001D) / 100D) * 100D);
			  if(j2 >= 5280)
			  {
				  j2 = (int)Math.floor(j2 / 5280);
			  }
			  bRadarWarning = (double)k1 <= 3000D && (double)k1 >= 50D && i2 >= 195 && i2 <= 345 && Math.sqrt(j1 * j1) >= 120D;
	          playSirenaWarning(bRadarWarning);
		  }
		  else
		  {
			  bRadarWarning = false;
			  playSirenaWarning(bRadarWarning);
		  }
	  return true;
  }

  public void playSirenaWarning(boolean isThreatened)
  {
      if (isThreatened && !sirenaSoundPlaying) {
  		fxSirena.play(smplSirena);
  		sirenaSoundPlaying = true;
  		HUD.log(AircraftHotKeys.hudLogWeaponId, "SPO-2: Enemy on Six!");
      }
	  else if(!isThreatened && sirenaSoundPlaying){
		fxSirena.cancel();
		sirenaSoundPlaying = false;
	 }
  }
  
  public void update(float f) {
	  super.update(f);
	  if((World.getPlayerRegiment().country() == "ru" || World.getPlayerRegiment().country() == "kx" || World.getPlayerRegiment().country() == "kp") && Mission.curYear() >= 1952)
		  sirenaWarning();
  }

  static {
    Class var_class = com.maddox.il2.objects.air.MiG_15SB.class;
    new NetAircraft.SPAWN(var_class);
    Property.set(var_class, "iconFar_shortClassName", "MiG-15");
    Property.set(var_class, "meshName_ru", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme_ru", new PaintSchemeFCSPar1956());
    Property.set(var_class, "meshName_sk", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme_sk", new PaintSchemeFMPar1956());
    Property.set(var_class, "meshName_ro", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme_ro", new PaintSchemeFMPar1956());
    Property.set(var_class, "meshName_hu", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme_hu", new PaintSchemeFMPar1956());
    Property.set(var_class, "meshName", "3DO/Plane/MiG-15bis(Multi1)/hier.him");
    Property.set(var_class, "PaintScheme", new PaintSchemeFMPar06());
    Property.set(var_class, "yearService", 1949.9F);
    Property.set(var_class, "yearExpired", 1960.3F);
    Property.set(var_class, "FlightModel", "FlightModels/MiG-15F.fmd");
    Property.set(var_class, "cockpitClass", new Class[]{
              com.maddox.il2.objects.air.CockpitMig_15F.class
            });
    Property.set(var_class, "LOSElevation", 0.725F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(var_class, new int[]{
              1, 0, 0, 9, 9, 9, 9, 9, 9, 9,
              9, 3, 3, 3, 3, 3, 3, 3, 3, 3,
              3, 3, 3, 2, 2, 2, 2, 2, 2, 2,
              2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
              2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
              2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
              2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
              2
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(var_class, new java.lang.String[]{
              "_CANNON01", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07",
              "_ExternalDev08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb04", "_ExternalBomb03", "_ExternalBomb05",
              "_ExternalBomb06", "_ExternalBomb06", "_ExternalBomb05", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07",
              "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17",
              "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalRock27",
              "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32", "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37",
              "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock41", "_ExternalRock42", "_ExternalRock42", "_ExternalRock43", "_ExternalRock43", "_ExternalRock44",
              "_ExternalRock44"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "default", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xdroptanks", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15 1", "PylonMiG15 1", null, null, null,
              null, "BombGunFAB100 1", null, "BombGunFAB100 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2x100+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15 1", "PylonMiG15 1", null, null, null,
              null, "BombGunFAB100 1", null, "BombGunFAB100 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15 1", "PylonMiG15 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", "BombGunFAB100 1", null, "BombGunFAB100 1", null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4x100+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15 1", "PylonMiG15 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", "BombGunFAB100 1", null, "BombGunFAB100 1", null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2x250m46", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15 1", "PylonMiG15 1", null, null, null,
              null, "BombGunFAB250m46 1", null, "BombGunFAB250m46 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2x250m46+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15 1", "PylonMiG15 1", null, null, null,
              null, "BombGunFAB250m46 1", null, "BombGunFAB250m46 1", null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2x250m46+2xdt+2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15 1", "PylonMiG15 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", "BombGunFAB250m46 1", null, "BombGunFAB250m46 1", null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2x250m46+2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15 1", "PylonMiG15 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", "BombGunFAB250m46 1", null, "BombGunFAB250m46 1", null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4x250m46", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15 1", "PylonMiG15 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", "BombGunFAB250m46 1", null, "BombGunFAB250m46 1", null, null, null, null, null, "BombGunFAB250m46 1",
              null, "BombGunFAB250m46 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4x250m46+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15 1", "PylonMiG15 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", "BombGunFAB250m46 1", null, "BombGunFAB250m46 1", null, null, null, null, null, "BombGunFAB250m46 1",
              null, "BombGunFAB250m46 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonSR55 1", "PylonSR55 1", null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonSR55 1", "PylonSR55 1", null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55+2xdt+2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonSR55 1", "PylonSR55 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55+2xdt+2x250m46", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonSR55 1", "PylonSR55 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB250m46 1",
              null, "BombGunFAB250m46 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55+2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonSR55 1", "PylonSR55 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55+2x250m46", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonSR55 1", "PylonSR55 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB250m46 1",
              null, "BombGunFAB250m46 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4xSR55", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonSR55 1", "PylonSR55 1", null, null, "PylonSR55 1",
              "PylonSR55 1", null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4xSR55+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonSR55 1", "PylonSR55 1", null, null, "PylonSR55 1",
              "PylonSR55 1", null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1",
              "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55a2a", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonSR55 1", "PylonSR55 1", null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55a2a+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonSR55 1", "PylonSR55 1", null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55a2a+2xdt+2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonSR55 1", "PylonSR55 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55a2a+2xdt+2x250m46", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonSR55 1", "PylonSR55 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB250m46 1",
              null, "BombGunFAB250m46 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55a2a+2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonSR55 1", "PylonSR55 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xSR55a2a+2x250m46", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonSR55 1", "PylonSR55 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB250m46 1",
              null, "BombGunFAB250m46 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4xSR55a2a", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonSR55 1", "PylonSR55 1", null, null, "PylonSR55 1",
              "PylonSR55 1", null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4xSR55a2a+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonSR55 1", "PylonSR55 1", null, null, "PylonSR55 1",
              "PylonSR55 1", null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1", "RocketGunLR55Salvo 1",
              "RocketGunLR55Salvo 1", "RocketGunLR55 1", "RocketGunLR55 1", null, null, null, null, null, null, null,
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xLR130", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15LR130 1", "PylonMiG15LR130 1", null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, "RocketGunLR130 1", null, "RocketGunLR130 1",
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xLR130+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15LR130 1", "PylonMiG15LR130 1", null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, "RocketGunLR130 1", null, "RocketGunLR130 1",
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xLR130+2xdt+2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15LR130 1", "PylonMiG15LR130 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, "RocketGunLR130 1", null, "RocketGunLR130 1",
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xLR130+2xdt+2x250m46", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15LR130 1", "PylonMiG15LR130 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB250m46 1",
              null, "BombGunFAB250m46 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, "RocketGunLR130 1", null, "RocketGunLR130 1",
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xLR130+2x100", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15LR130 1", "PylonMiG15LR130 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB100 1",
              null, "BombGunFAB100 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, "RocketGunLR130 1", null, "RocketGunLR130 1",
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "2xLR130+2x250m46", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15LR130 1", "PylonMiG15LR130 1", null, null, "PylonMiG15 1",
              "PylonMiG15 1", null, null, null, null, null, null, null, null, "BombGunFAB250m46 1",
              null, "BombGunFAB250m46 1", null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, "RocketGunLR130 1", null, "RocketGunLR130 1",
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4xLR130", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", null, null, "PylonMiG15LR130 1", "PylonMiG15LR130 1", null, null, "PylonMiG15LR130 1",
              "PylonMiG15LR130 1", null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR130 1", null, "RocketGunLR130 1", null, "RocketGunLR130 1", null, "RocketGunLR130 1",
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "4xLR130+2xdt", new java.lang.String[]{
              "MGunN37ki 40", "MGunNR23ki 80", "MGunNR23ki 80", "FTGunL 1", "FTGunR 1", "PylonMiG15LR130 1", "PylonMiG15LR130 1", null, null, "PylonMiG15LR130 1",
              "PylonMiG15LR130 1", null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, "RocketGunLR130 1", null, "RocketGunLR130 1", null, "RocketGunLR130 1", null, "RocketGunLR130 1",
              null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(var_class, "none", new java.lang.String[]{
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null, null, null, null, null, null, null, null, null, null,
              null
            });
  }
}