package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;

import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

public class Hunter_F4 extends Hunter
	implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

	 private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
	  
    public Hunter_F4()
    {
        bToFire = false;
        guidedMissileUtils = new GuidedMissileUtils(this);
    }

 // <editor-fold defaultstate="collapsed" desc="Countermeasures">
    private boolean hasChaff = false;     // Aircraft is equipped with Chaffs yes/no
    private boolean hasFlare = false;     // Aircraft is equipped with Flares yes/no
    private long lastChaffDeployed = 0L;  // Last Time when Chaffs have been deployed
    private long lastFlareDeployed = 0L;  // Last Time when Flares have been deployed

    public long getChaffDeployed() {
      if (this.hasChaff) {
        return this.lastChaffDeployed;
      }
      return 0L;
    }

    public long getFlareDeployed() {
      if (this.hasFlare) {
        return this.lastFlareDeployed;
      }
      return 0L;
    }// </editor-fold>
  // <editor-fold defaultstate="collapsed" desc="Threat Detection">
    private long lastCommonThreatActive = 0L;         // Last Time when a common threat was reported
    private long intervalCommonThreat = 1000L;        // Interval (milliseconds) at which common threats should be dealt with (i.e. duration of warning sound / light)
    private long lastRadarLockThreatActive = 0L;      // Last Time when a radar lock threat was reported
    private long intervalRadarLockThreat = 1000L;     // Interval (milliseconds) at which radar lock threats should be dealt with (i.e. duration of warning sound / light)
    private long lastMissileLaunchThreatActive = 0L;  // Last Time when a missile launch threat was reported
    private long intervalMissileLaunchThreat = 1000L; // Interval (milliseconds) at which missile launch threats should be dealt with (i.e. duration of warning sound / light)

    public void setCommonThreatActive() {
      long curTime = Time.current();
      if ((curTime - this.lastCommonThreatActive) > this.intervalCommonThreat) {
        this.lastCommonThreatActive = curTime;
        this.doDealCommonThreat();
      }
    }

    public void setRadarLockThreatActive() {
      long curTime = Time.current();
      if ((curTime - this.lastRadarLockThreatActive) > this.intervalRadarLockThreat) {
        this.lastRadarLockThreatActive = curTime;
        this.doDealRadarLockThreat();
      }
    }

    public void setMissileLaunchThreatActive() {
      long curTime = Time.current();
      if ((curTime - this.lastMissileLaunchThreatActive) > this.intervalMissileLaunchThreat) {
        this.lastMissileLaunchThreatActive = curTime;
        this.doDealMissileLaunchThreat();
      }
    }

    private void doDealCommonThreat() {       // Must be filled with life for A/Cs capable of dealing with common Threats
    }

    private void doDealRadarLockThreat() {    // Must be filled with life for A/Cs capable of dealing with radar lock Threats
    }

    private void doDealMissileLaunchThreat() {// Must be filled with life for A/Cs capable of dealing with missile launch Threats
    	
    }

  // <editor-fold defaultstate="collapsed" desc="TypeGuidedMissileCarrier Implementation">

    public GuidedMissileUtils getGuidedMissileUtils() {
      return this.guidedMissileUtils;
    }

  // </editor-fold>
    
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.guidedMissileUtils.onAircraftLoaded();
      }

    public void engineSurge(float f) {
        if (((FlightModelMain) (super.FM)).AS.isMaster()) {
          if (curthrl == -1F) {
            curthrl = oldthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
          } else {
            curthrl = ((FlightModelMain) (super.FM)).EI.engines[0].getControlThrottle();
            if (curthrl < 1.05F) {
              if ((curthrl - oldthrl) / f > 10.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6 && World.Rnd().nextFloat() < 0.40F) {
                if (FM.actor == World.getPlayerAircraft()) {
                  HUD.log("Compressor Stall!");
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
              if ((curthrl - oldthrl) / f < -10.0F && (curthrl - oldthrl) / f > -100.0F && FM.EI.engines[0].getRPM() < 3200.0F && FM.EI.engines[0].getStage() == 6) {
                super.playSound("weapon.MGunMk108s", true);
                engineSurgeDamage += 1.0000000000000001E-003D * (double) (((FlightModelMain) (super.FM)).EI.engines[0].getRPM() / 1000F);
                ((FlightModelMain) (super.FM)).EI.engines[0].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[0].getReadyness() - engineSurgeDamage);
                if (World.Rnd().nextFloat() < 0.40F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode()) {
                  if (FM.actor == World.getPlayerAircraft()) {
                    HUD.log("Engine Flameout!");
                  }
                  FM.EI.engines[0].setEngineStops(this);
                } else {
                  if (FM.actor == World.getPlayerAircraft()) {
                    HUD.log("Compressor Stall!");
                  }
                }
              }
            }
            oldthrl = curthrl;
          }
        }
      }
    
    public void update(float f)
    {
        super.update(f);
        this.guidedMissileUtils.update();
    }


    private static Aircraft._WeaponSlot[] GenerateDefaultConfig(int i)
    {
        Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[i];
        try
        {
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunADEN30ki", 150);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunADEN30ki", 150);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(0, "MGunADEN30ki", 150);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(0, "MGunADEN30ki", 150);
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
        }
        catch(Exception exception) { }
        return a_lweaponslot;
    }


    public boolean bToFire;
    private float oldthrl;
    private float curthrl;
    private float engineSurgeDamage;
    
    static 
    {
        Class localClass = com.maddox.il2.objects.air.Hunter_F4.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "Hunter F4");
        Property.set(localClass, "meshName", "3DO/Plane/Hunter_F4(Multi1)/hier.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(localClass, "yearService", 1949.9F);
        Property.set(localClass, "yearExpired", 1960.3F);
        Property.set(localClass, "FlightModel", "FlightModels/HunterF4.fmd");
        Property.set(localClass, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitHunter.class
        });
        Property.set(localClass, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(localClass, new int[]{
                0, 0, 0, 0, 0, 0, 9, 9, 9, 9,
                9, 3, 3, 9, 3, 3, 9, 2, 2, 9,
                2, 2, 9, 9, 9, 9, 9, 3, 9, 3,
                9, 3, 9, 3
            });
    Aircraft.weaponHooksRegister(localClass, new String[]{
                "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
                "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08",
                "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04",
                "_ExternalDev15", "_ExternalBomb05", "_ExternalDev16", "_ExternalBomb06"
            });
    // 6  = Outer Tank Pylon Left  = _ExternalDev01
    // 7  = Outer Tank Pylon Right = _ExternalDev02
    // 8  = Outer Tank Left        = _ExternalDev03
    // 9  = Outer Tank Right       = _ExternalDev04
    // 10 = Outer Bomb Pylon Left  = _ExternalDev05
    // 11 = Outer Bomb Left        = _ExternalBomb01
    // 12 = Outer Bomb Left Dummy  = _ExternalBomb01
    // 13 = Outer Bomb Pylon Right = _ExternalDev06
    // 14 = Outer Bomb Right       = _ExternalBomb02
    // 15 = Outer Bomb Right Dummy = _ExternalBomb02
    // 16 = AIM9 Pylon Left        = _ExternalDev07
    // 17 = AIM9 Left              = _ExternalRock01
    // 18 = AIM9 Left Dummy        = _ExternalRock01
    // 19 = AIM9 Pylon Right       = _ExternalDev08
    // 20 = AIM9 Right             = _ExternalRock02
    // 21 = AIM9 Right Dummy       = _ExternalRock02

    // 22 = Inner Tank Pylon Left  = _ExternalDev09
    // 23 = Inner Tank Pylon Right = _ExternalDev10
    // 24 = Inner Tank Left        = _ExternalDev11
    // 25 = Inner Tank Right       = _ExternalDev12
    // 26 = Middle Bomb Pylon Left  = _ExternalDev13
    // 27 = Middle Bomb Left        = _ExternalBomb03
    // 28 = Middle Bomb Pylon Right = _ExternalDev14
    // 29 = Middle Bomb Right       = _ExternalBomb04
    // 30 = Inner Bomb Pylon Left  = _ExternalDev15
    // 31 = Inner Bomb Left        = _ExternalBomb05
    // 32 = Inner Bomb Pylon Right = _ExternalDev16
    // 33 = Inner Bomb Right       = _ExternalBomb06

    
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(localClass, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(localClass, "weaponsMap", hashmapint);
            byte byte0 = 40;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(byte0);
            for(int i = 6; i < byte0; i++)
                a_lweaponslot[i] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAIM9B";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 16; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[18] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            a_lweaponslot[19] = new Aircraft._WeaponSlot(9, "PylonF86_Sidewinder", 1);
            a_lweaponslot[20] = new Aircraft._WeaponSlot(2, "RocketGunAIM9B", 1);
            a_lweaponslot[21] = new Aircraft._WeaponSlot(2, "BombGunNull", 1);
            for(int j = 22; j < byte0; j++)
                a_lweaponslot[j] = null;              
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            for(int j = 0; j < byte0; j++)
                a_lweaponslot[j] = null;

            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}