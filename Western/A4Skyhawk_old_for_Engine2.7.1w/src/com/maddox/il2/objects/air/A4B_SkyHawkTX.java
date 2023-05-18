
package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.util.ArrayList;


public class A4B_SkyHawkTX extends A4TXreceiver
    implements TypeGuidedMissileCarrier, TypeCountermeasure, TypeThreatDetector
{

    public A4B_SkyHawkTX()
    {
        guidedMissileUtils = null;
        hasChaff = false;
        hasFlare = false;
        lastChaffDeployed = 0L;
        lastFlareDeployed = 0L;
        lastCommonThreatActive = 0L;
        intervalCommonThreat = 1000L;
        lastRadarLockThreatActive = 0L;
        intervalRadarLockThreat = 1000L;
        lastMissileLaunchThreatActive = 0L;
        intervalMissileLaunchThreat = 1000L;
        guidedMissileUtils = new GuidedMissileUtils(this);
    }

    public long getChaffDeployed()
    {
        if(hasChaff)
            return lastChaffDeployed;
        else
            return 0L;
    }

    public long getFlareDeployed()
    {
        if(hasFlare)
            return lastFlareDeployed;
        else
            return 0L;
    }

    public void setCommonThreatActive()
    {
        long l = Time.current();
        if(l - lastCommonThreatActive > intervalCommonThreat)
        {
            lastCommonThreatActive = l;
            doDealCommonThreat();
        }
    }

    public void setRadarLockThreatActive()
    {
        long l = Time.current();
        if(l - lastRadarLockThreatActive > intervalRadarLockThreat)
        {
            lastRadarLockThreatActive = l;
            doDealRadarLockThreat();
        }
    }

    public void setMissileLaunchThreatActive()
    {
        long l = Time.current();
        if(l - lastMissileLaunchThreatActive > intervalMissileLaunchThreat)
        {
            lastMissileLaunchThreatActive = l;
            doDealMissileLaunchThreat();
        }
    }

    private void doDealCommonThreat()
    {
    }

    private void doDealRadarLockThreat()
    {
    }

    private void doDealMissileLaunchThreat()
    {
    }

    public GuidedMissileUtils getGuidedMissileUtils()
    {
        return guidedMissileUtils;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        guidedMissileUtils.onAircraftLoaded();
    }

    public void update(float f)
    {
        guidedMissileUtils.update();
        super.update(f);
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    private GuidedMissileUtils guidedMissileUtils;
    private boolean hasChaff;
    private boolean hasFlare;
    private long lastChaffDeployed;
    private long lastFlareDeployed;
    private long lastCommonThreatActive;
    private long intervalCommonThreat;
    private long lastRadarLockThreatActive;
    private long intervalRadarLockThreat;
    private long lastMissileLaunchThreatActive;
    private long intervalMissileLaunchThreat;

    static 
    {
        Class class1 = CLASS.THIS();
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SkyHawk");
        Property.set(class1, "meshName", "3DO/Plane/A4BSkyHawkTX(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1971F);
        Property.set(class1, "yearExpired", 1999F);
        Property.set(class1, "FlightModel", "FlightModels/A4B.fmd:Skyhawks_FM");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitSkyhawkTX.class
        });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 9, 9, 0, 0, 9, 9, 
            3, 3, 3, 3, 2, 2, 2, 2, 9, 2, 
            2, 2, 2, 9, 2, 2, 2, 2, 9, 9, 
            9, 3, 3, 3, 3, 3, 3, 9, 9, 9, 
            9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 2, 2, 9, 9, 9, 9, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", 
            "_ExternalBomb07", "_ExternalBomb08", "_Bomb09", "_ExternalBomb10", "_Rock01", "_Rock02", "_MGUN01", "_MGUN02", "_ExternalRock03", "_ExternalRock04", 
            "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", 
            "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalDev01", "_ExternalDev02", 
            "_Dev03", "_ExternalBomb11", "_Bomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_Dev04", "_Dev05", "_Dev06", 
            "_Dev07", "_Bomb17", "_Bomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_Bomb23", "_ExternalBomb24", "_Bomb25", 
            "_Bomb26", "_Bomb27", "_Bomb28", "_ExternalRock23", "_ExternalRock24", "_Dev08", "_Dev09", "_Dev10", "_Dev11", "_ExternalRock25"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 70;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[18] = null;
            a_lweaponslot[19] = null;
            a_lweaponslot[20] = null;
            a_lweaponslot[21] = null;
            a_lweaponslot[22] = null;
            a_lweaponslot[23] = null;
            a_lweaponslot[24] = null;
            a_lweaponslot[25] = null;
            a_lweaponslot[26] = null;
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[29] = null;
            a_lweaponslot[30] = null;
            a_lweaponslot[31] = null;
            a_lweaponslot[32] = null;
            a_lweaponslot[33] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[36] = null;
            a_lweaponslot[37] = null;
            a_lweaponslot[38] = null;
            a_lweaponslot[39] = null;
            a_lweaponslot[40] = null;
            a_lweaponslot[41] = null;
            a_lweaponslot[42] = null;
            a_lweaponslot[43] = null;
            a_lweaponslot[44] = null;
            a_lweaponslot[45] = null;
            a_lweaponslot[46] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk81";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "8xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82_2xMk83";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "18xMk81";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = null;
            a_lweaponslot[5] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "PylonF4TER", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82_12xMk81";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "12xMk81_1xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[16] = null;
            a_lweaponslot[17] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[47] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[48] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[49] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[50] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[51] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[52] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[53] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[54] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[55] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[56] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[57] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[58] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[59] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[60] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[61] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[62] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xMk83";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[39] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk83_1xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[39] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xMk82";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[39] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMk82_1xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[39] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk81_2xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82_2xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xMk83_2xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk83", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82_2xAGM12";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xAGM12_1xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3xAGM12";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[63] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            a_lweaponslot[64] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            a_lweaponslot[65] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[66] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[67] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[68] = new Aircraft._WeaponSlot(9, "PylonP51PLN2", 1);
            a_lweaponslot[69] = new Aircraft._WeaponSlot(2, "RocketGunAGM12", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2xMinigun_1xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "PylonSUU11", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "PylonSUU11", 1);
            a_lweaponslot[14] = null;
            a_lweaponslot[15] = null;
            a_lweaponslot[16] = new Aircraft._WeaponSlot(1, "MGunMiniGun", 1500);
            a_lweaponslot[17] = new Aircraft._WeaponSlot(1, "MGunMiniGun", 1500);
            a_lweaponslot[27] = null;
            a_lweaponslot[28] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[39] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk81_2xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk81", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "6xMk82_2xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[34] = null;
            a_lweaponslot[35] = null;
            a_lweaponslot[39] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[40] = new Aircraft._WeaponSlot(9, "PylonTERoc", 1);
            a_lweaponslot[41] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[42] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[43] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[44] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[45] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            a_lweaponslot[46] = new Aircraft._WeaponSlot(3, "BombGunMk82", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "1xNuke_2xDpt";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(0, "MGunHispanoMkIki", 200);
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(9, "FuelTankGun_TankSkyhawk", 1);
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[38] = new Aircraft._WeaponSlot(3, "BombGunMk7", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "empty";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = null;
            a_lweaponslot[3] = null;
            a_lweaponslot[6] = null;
            a_lweaponslot[7] = null;
            a_lweaponslot[39] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}