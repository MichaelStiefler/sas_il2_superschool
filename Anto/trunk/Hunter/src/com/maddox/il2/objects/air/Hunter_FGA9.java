package com.maddox.il2.objects.air;

import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;

public class Hunter_FGA9 extends Hunter
implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{
	private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
	  
    public Hunter_FGA9()
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
    
    static 
    {
        Class localClass = com.maddox.il2.objects.air.Hunter_FGA9.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "Hunter FGA9");
        Property.set(localClass, "meshName", "3DO/Plane/Hunter_FGA9(Multi1)/hier.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(localClass, "yearService", 1949.9F);
        Property.set(localClass, "yearExpired", 1960.3F);
        Property.set(localClass, "FlightModel", "FlightModels/HunterFGA9.fmd");
        Property.set(localClass, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitHunter.class
        });
        Property.set(localClass, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(localClass, new int[]{
                0, 0, 0, 0, 0, 0, 9, 9, 9, 9,
                9, 3, 3, 9, 3, 3, 9, 2, 2, 9,
                2, 2, 9, 9, 9, 9, 9, 3, 9, 3,
                9, 3, 9, 3, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 9, 9,
                9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
                9, 9, 9, 9, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
                2, 2, 2, 2, 2, 2, 2, 2, 9, 9,
                2, 2, 2, 2, 2, 2, 2, 2
            });
    Aircraft.weaponHooksRegister(localClass, new String[]{
                "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04",
                "_ExternalDev05", "_ExternalBomb01", "_ExternalBomb01", "_ExternalDev06", "_ExternalBomb02", "_ExternalBomb02", "_ExternalDev07", "_ExternalRock01", "_ExternalRock01", "_ExternalDev08",
                "_ExternalRock02", "_ExternalRock02", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalBomb03", "_ExternalDev14", "_ExternalBomb04",
                "_ExternalDev15", "_ExternalBomb05", "_ExternalDev16", "_ExternalBomb06", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", 
                "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18",
                "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26","_ExternalDev17", "_ExternalDev18", 
                "_ExternalDev19", "_ExternalDev20", "_ExternalDev21", "_ExternalDev22", "_ExternalDev23", "_ExternalDev24", "_ExternalDev25", "_ExternalDev26", "_ExternalDev27", "_ExternalDev28", 
                "_ExternalDev29", "_ExternalDev30", "_ExternalDev31", "_ExternalDev32", "_ExternalRock27", "_ExternalRock28", "_ExternalRock29", "_ExternalRock30", "_ExternalRock31", "_ExternalRock32",
                "_ExternalRock33", "_ExternalRock34", "_ExternalRock35", "_ExternalRock36", "_ExternalRock37", "_ExternalRock38", "_ExternalRock39", "_ExternalRock40", "_ExternalRock41", "_ExternalRock42",
                "_ExternalRock43", "_ExternalRock44", "_ExternalRock45", "_ExternalRock46", "_ExternalRock47", "_ExternalRock48", "_ExternalRock49", "_ExternalRock50", "_ExternalDev33", "_ExternalDev34",
                "_ExternalRock51", "_ExternalRock52", "_ExternalRock53", "_ExternalRock54", "_ExternalRock55", "_ExternalRock56", "_ExternalRock57", "_ExternalRock58"
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
    
    // 34-57 = RP3 rockets = _ExternalRock03 to _ExternalRock18
    // 57-66 = Zero-point launchers = _ExternalDev17 to _ExternalDev24
    // 66-73 = SURA D Rails = _ExternalDev25 to _ExternalDev32
    // 73-97 = SURA D Rockets = _ExternalRock19 to _ExternalRock50
    // 98-99 = SNEB 100mm Pods = _ExternalDev33 to 34
    // 100-107 = SNEB 100mm Rockets = _ExternalRock51 to _ExternalRock58
    
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(localClass, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(localClass, "weaponsMap", hashmapint);
            byte byte0 = 120;
            String s = "default";
            Aircraft._WeaponSlot a_lweaponslot[] = GenerateDefaultConfig(byte0);
            for(int i = 6; i < byte0; i++)
                a_lweaponslot[i] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x1000lbs";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 26; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun1000lbsMC", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGun1000lbsMC", 1);
            for(int j = 30; j < byte0; j++)
                a_lweaponslot[j] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x500lbs";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 30; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGun500lbsMC", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGun500lbsMC", 1);
            for(int j = 34; j < byte0; j++)
                a_lweaponslot[j] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2x1000lbs_2x500lbs";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 26; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun1000lbsMC", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGun1000lbsMC", 1);
            a_lweaponslot[30] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[31] = new Aircraft._WeaponSlot(3, "BombGun500lbsMC", 1);
            a_lweaponslot[32] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[33] = new Aircraft._WeaponSlot(3, "BombGun500lbsMC", 1);
            for(int j = 34; j < byte0; j++)
                a_lweaponslot[j] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "02x75Gal_Napalm";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 26; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[26] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[27] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            a_lweaponslot[28] = new Aircraft._WeaponSlot(9, "PylonF86_Bombs", 1);
            a_lweaponslot[29] = new Aircraft._WeaponSlot(3, "BombGun75Napalm", 1);
            for(int j = 30; j < byte0; j++)
                a_lweaponslot[j] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xRP3s_25";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 34; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_25", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            for(int j = 66; j < byte0; j++)
                a_lweaponslot[j] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xRP3s_60";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 34; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[34] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[35] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[36] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[37] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(2, "RocketGunRP3_Mk5_60", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(9, "PylonSpitROCK", 1);
            for(int j = 66; j < byte0; j++)
                a_lweaponslot[j] = null;
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xSURA_D_HE";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 58; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunSURA_HE", 1);
            for(int j = 98; j < byte0; j++)
                a_lweaponslot[j] = null;
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "24xSURA_D_AP";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 58; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[70] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[71] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[72] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[73] = new Aircraft._WeaponSlot(9, "PylonSURA_Launcher", 1);
            a_lweaponslot[74] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[75] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[76] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[77] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[78] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[79] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[80] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[81] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[82] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[83] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[84] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[85] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[86] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[87] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[88] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[89] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[90] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[91] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[92] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[93] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[94] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[95] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[96] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            a_lweaponslot[97] = new Aircraft._WeaponSlot(2, "RocketGunSURA_AP", 1);
            for(int j = 98; j < byte0; j++)
                a_lweaponslot[j] = null;
              
            
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xSNEB-100mm";
            a_lweaponslot = GenerateDefaultConfig(byte0);
            for(int j = 6; j < 100; j++)
                a_lweaponslot[j] = null;
            a_lweaponslot[98] = new Aircraft._WeaponSlot(9, "PylonSNEB_6xLauncher", 1);
            a_lweaponslot[99] = new Aircraft._WeaponSlot(9, "PylonSNEB_6xLauncher", 1);
            a_lweaponslot[100] = new Aircraft._WeaponSlot(2, "RocketGunSNEB100", 1);
            a_lweaponslot[101] = new Aircraft._WeaponSlot(2, "RocketGunSNEB100", 1);
            a_lweaponslot[102] = new Aircraft._WeaponSlot(2, "RocketGunSNEB100", 1);
            a_lweaponslot[103] = new Aircraft._WeaponSlot(2, "RocketGunSNEB100", 1);
            a_lweaponslot[104] = new Aircraft._WeaponSlot(2, "RocketGunSNEB100", 1);
            a_lweaponslot[105] = new Aircraft._WeaponSlot(2, "RocketGunSNEB100", 1);
            a_lweaponslot[106] = new Aircraft._WeaponSlot(2, "RocketGunSNEB100", 1);
            a_lweaponslot[107] = new Aircraft._WeaponSlot(2, "RocketGunSNEB100", 1);
            for(int j = 108; j < byte0; j++)
                a_lweaponslot[j] = null;
            
            
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