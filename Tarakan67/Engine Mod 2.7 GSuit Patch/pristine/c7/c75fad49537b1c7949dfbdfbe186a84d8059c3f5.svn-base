// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 17.01.2015 13:02:32
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BF_110G4NJ.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.FlightModel;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.maddox.il2.objects.air:
//            BF_110, PaintSchemeBMPar03, TypeBomber, Aircraft, 
//            NetAircraft

public class BF_110G4NJ extends BF_110
    implements TypeBomber
{

    public BF_110G4NJ()
    {
        radarContacts = new ArrayList();
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor)
    {
        super.nextCUTLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        case 0: // '\0'
            if(f1 < -19F)
            {
                f1 = -19F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            float f2;
            if(f1 < 0.0F)
                f2 = Aircraft.cvt(f1, -19F, 0.0F, 20F, 30F);
            else
            if(f1 < 12F)
                f2 = Aircraft.cvt(f1, 0.0F, 12F, 30F, 35F);
            else
                f2 = Aircraft.cvt(f1, 12F, 30F, 35F, 40F);
            if(f < 0.0F)
            {
                if(f < -f2)
                {
                    f = -f2;
                    flag = false;
                }
            } else
            if(f > f2)
            {
                f = f2;
                flag = false;
            }
            if(Math.abs(f) > 17.8F && Math.abs(f) < 25F && f1 < -12F)
                flag = false;
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

 

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberUpdate(float f)
    {
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

    public static boolean bChangedPit = false;
    private List radarContacts;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.BF_110G4NJ.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf-110");
        Property.set(class1, "meshName", "3DO/Plane/Bf-110G-4R3/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1940F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-110G-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitBF_110G4NJ.class, com.maddox.il2.objects.air.CockpitBF_110G4NJ_RadarOp.class, com.maddox.il2.objects.air.CockpitBF_110G4_Gunner.class
        });
        Property.set(class1, "LOSElevation", 0.66895F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            1, 1, 1, 1, 0, 0, 10, 10, 9, 1, 
            1, 9, 9, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_MGUN05", "_MGUN06", "_ExternalDev03", "_CANNON06", 
            "_CANNON07", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08"
        });
        try
        {
            ArrayList arraylist = new ArrayList();
            Property.set(class1, "weaponsList", arraylist);
            HashMapInt hashmapint = new HashMapInt();
            Property.set(class1, "weaponsMap", hashmapint);
            byte byte0 = 15;
            Aircraft._WeaponSlot a_lweaponslot[] = new Aircraft._WeaponSlot[byte0];
            String s = "default (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cmJazz (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cmJazz_Tanks (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cmJazz_Tanks (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cmJazz_Nag (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cmJazz_Nag (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cmJazz_Tanks_Nag (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cmJazz_Tanks_Nag (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cm_Full (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cm_Full (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cm_Full_Tanks (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFnt", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cm_Full_Tanks (NT)";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120nt", 200);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "default";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cmJazz";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cmJazz_Tanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cmJazz_Tanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = null;
            a_lweaponslot[9] = null;
            a_lweaponslot[10] = null;
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cmJazz_Nag";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cmJazz_Nag";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cmJazz_Tanks_Nag";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cmJazz_Tanks_Nag";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cm_Full";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cm_Full";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[11] = null;
            a_lweaponslot[12] = null;
            a_lweaponslot[13] = null;
            a_lweaponslot[14] = null;
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "2cm_Full_Tanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMGFFk", 200);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "3cm_Full_Tanks";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108ki", 137);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[3] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG2", 250);
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[5] = new Aircraft._WeaponSlot(0, "MGunMK108ki", 110);
            a_lweaponslot[6] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[7] = new Aircraft._WeaponSlot(10, "MGunMG15t", 1200);
            a_lweaponslot[8] = new Aircraft._WeaponSlot(9, "PylonBF110R3", 1);
            a_lweaponslot[9] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[10] = new Aircraft._WeaponSlot(1, "MGunMG15120NAG", 200);
            a_lweaponslot[11] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[12] = new Aircraft._WeaponSlot(9, "PylonETC250", 1);
            a_lweaponslot[13] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            a_lweaponslot[14] = new Aircraft._WeaponSlot(9, "FuelTankGun_Type_D", 1);
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "Stand_in_110H4";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 135);
            a_lweaponslot[1] = new Aircraft._WeaponSlot(1, "MGunMK108nt", 135);
            a_lweaponslot[2] = new Aircraft._WeaponSlot(1, "MGunMK103nt", 140);
            a_lweaponslot[3] = null;
            a_lweaponslot[4] = new Aircraft._WeaponSlot(0, "MGunMK108nt", 110);
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
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
            s = "none";
            a_lweaponslot = new Aircraft._WeaponSlot[byte0];
            a_lweaponslot[0] = null;
            a_lweaponslot[1] = null;
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
            arraylist.add(s);
            hashmapint.put(Finger.Int(s), a_lweaponslot);
        }
        catch(Exception exception) { }
    }
}
