// Decompiled by DJ v3.12.12.96 Copyright 2011 Atanas Neshkov  Date: 14/10/2015 10:15:50 a.m.
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   F9F6.java

package com.maddox.il2.objects.air;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.*;
import com.maddox.rts.Property;

// Referenced classes of package com.maddox.il2.objects.air:
//            F9F_Cougar, PaintSchemeFCSPar06, Aircraft, NetAircraft

public class F9F6 extends F9F_Cougar
{

    public F9F6()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(super.FM.isPlayers())
        {
            ((FlightModelMain) (super.FM)).CT.bHasCockpitDoorControl = true;
            ((FlightModelMain) (super.FM)).CT.dvCockpitDoor = 0.5F;
        }
    }

    public void update(float f)
    {
        if(((FlightModelMain) (super.FM)).CT.getWing() == 0.0F)
            if(super.FM.getSpeed() > 5F)
            {
                moveSlats(f);
                bSlatsOff = false;
            } else
            {
                slatsOff();
            }
        super.update(f);
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 70F), 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -70F), 0.0F);
    }

    protected void moveSlats(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, -0.04F);
        Aircraft.xyz[2] = Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 0.045F);
        hierMesh().chunkSetAngles("WingSlatL", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("WingSlatL", Aircraft.xyz, Aircraft.ypr);
        hierMesh().chunkSetAngles("WingSlatR", 0.0F, Aircraft.cvt(super.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        hierMesh().chunkSetLocate("WingSlatR", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff()
    {
        if(!bSlatsOff)
        {
            resetYPRmodifier();
            Aircraft.xyz[1] = -0.04F;
            Aircraft.xyz[2] = 0.045F;
            hierMesh().chunkSetAngles("WingSlatL", 0.0F, 8.5F, 0.0F);
            hierMesh().chunkSetLocate("WingSlatL", Aircraft.xyz, Aircraft.ypr);
            hierMesh().chunkSetAngles("WingSlatR", 0.0F, 8.5F, 0.0F);
            hierMesh().chunkSetLocate("WingSlatR", Aircraft.xyz, Aircraft.ypr);
            bSlatsOff = true;
        }
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

    protected boolean bSlatsOff;

    static 
    {
        Class class1 = com.maddox.il2.objects.air.F9F6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F9F6");
        Property.set(class1, "meshName", "3DO/Plane/F9F6/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar06());
        Property.set(class1, "yearService", 1946.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/F9F6.fmd:F9F");
        Property.set(class1, "cockpitClass", new Class[] {
            com.maddox.il2.objects.air.CockpitF9F_Cougar.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0, 9, 9, 9, 9, 9, 9, 
            9, 9, 3, 3, 3, 3, 3, 3, 9, 9, 
            2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 
            9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 
            3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", 
            "_ExternalDev07", "_ExternalDev08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalDev09", "_ExternalDev10", 
            "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalDev11", "_ExternalDev12", 
            "_ExternalDev13", "_ExternalDev14", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalRock12", "_ExternalRock12", 
            "_ExternalBomb07"
        });
        String as[] = new String[41];
        as[0] = "MGunHispanoMkIk 200";
        as[1] = "MGunHispanoMkIk 200";
        as[2] = "MGunHispanoMkIk 200";
        as[3] = "MGunHispanoMkIk 200";
        Aircraft.weaponsRegister(class1, "default", as);
        String as1[] = new String[41];
        as1[0] = "MGunHispanoMkIk 200";
        as1[1] = "MGunHispanoMkIk 200";
        as1[2] = "MGunHispanoMkIk 200";
        as1[3] = "MGunHispanoMkIk 200";
        as1[4] = "PylonAero65A 1";
        as1[5] = "PylonAero65A 1";
        as1[10] = "FuelTankGun_Cougar150galB 1";
        as1[11] = "FuelTankGun_Cougar150galB 1";
        Aircraft.weaponsRegister(class1, "02x154Gal_Tank", as1);
        String as2[] = new String[41];
        as2[0] = "MGunHispanoMkIk 200";
        as2[1] = "MGunHispanoMkIk 200";
        as2[2] = "MGunHispanoMkIk 200";
        as2[3] = "MGunHispanoMkIk 200";
        as2[4] = "PylonAero65A 1";
        as2[5] = "PylonAero65A 1";
        as2[12] = "BombGun75Napalm 1";
        as2[13] = "BombGun75Napalm 1";
        Aircraft.weaponsRegister(class1, "02x75Gal_Napalm", as2);
        String as3[] = new String[41];
        as3[0] = "MGunHispanoMkIk 200";
        as3[1] = "MGunHispanoMkIk 200";
        as3[2] = "MGunHispanoMkIk 200";
        as3[3] = "MGunHispanoMkIk 200";
        as3[4] = "PylonAero65A 1";
        as3[5] = "PylonAero65A 1";
        as3[12] = "BombGun250lbs 1";
        as3[13] = "BombGun250lbs 1";
        Aircraft.weaponsRegister(class1, "02x250_Bomb", as3);
        String as4[] = new String[41];
        as4[0] = "MGunHispanoMkIk 200";
        as4[1] = "MGunHispanoMkIk 200";
        as4[2] = "MGunHispanoMkIk 200";
        as4[3] = "MGunHispanoMkIk 200";
        as4[4] = "PylonAero65A 1";
        as4[5] = "PylonAero65A 1";
        as4[12] = "BombGun500lbs 1";
        as4[13] = "BombGun500lbs 1";
        Aircraft.weaponsRegister(class1, "02x500_Bomb", as4);
        String as5[] = new String[41];
        as5[0] = "MGunHispanoMkIk 200";
        as5[1] = "MGunHispanoMkIk 200";
        as5[2] = "MGunHispanoMkIk 200";
        as5[3] = "MGunHispanoMkIk 200";
        as5[4] = "PylonAero65A 1";
        as5[5] = "PylonAero65A 1";
        as5[12] = "BombGun750lbs 1";
        as5[13] = "BombGun750lbs 1";
        Aircraft.weaponsRegister(class1, "02x750_Bomb", as5);
        String as6[] = new String[41];
        as6[0] = "MGunHispanoMkIk 200";
        as6[1] = "MGunHispanoMkIk 200";
        as6[2] = "MGunHispanoMkIk 200";
        as6[3] = "MGunHispanoMkIk 200";
        as6[4] = "PylonAero65A 1";
        as6[5] = "PylonAero65A 1";
        as6[12] = "BombGun750lbsM117 1";
        as6[13] = "BombGun750lbsM117 1";
        Aircraft.weaponsRegister(class1, "02xM117_Bomb", as6);
        String as7[] = new String[41];
        as7[0] = "MGunHispanoMkIk 200";
        as7[1] = "MGunHispanoMkIk 200";
        as7[2] = "MGunHispanoMkIk 200";
        as7[3] = "MGunHispanoMkIk 200";
        as7[4] = "PylonAero65A 1";
        as7[5] = "PylonAero65A 1";
        as7[12] = "BombGun1000lbs 1";
        as7[13] = "BombGun1000lbs 1";
        Aircraft.weaponsRegister(class1, "02x1000_Bomb", as7);
        String as8[] = new String[41];
        as8[0] = "MGunHispanoMkIk 200";
        as8[1] = "MGunHispanoMkIk 200";
        as8[2] = "MGunHispanoMkIk 200";
        as8[3] = "MGunHispanoMkIk 200";
        as8[4] = "PylonAero65A 1";
        as8[5] = "PylonAero65A 1";
        as8[12] = "BombGun1000lbs_M114 1";
        as8[13] = "BombGun1000lbs_M114 1";
        Aircraft.weaponsRegister(class1, "02xM114_Bomb", as8);
        String as9[] = new String[41];
        as9[0] = "MGunHispanoMkIk 200";
        as9[1] = "MGunHispanoMkIk 200";
        as9[2] = "MGunHispanoMkIk 200";
        as9[3] = "MGunHispanoMkIk 200";
        as9[4] = "PylonAero65A 1";
        as9[5] = "PylonAero65A 1";
        as9[12] = "BombGunMk81 1";
        as9[13] = "BombGunMk81 1";
        Aircraft.weaponsRegister(class1, "02xMk81_Bomb", as9);
        String as10[] = new String[41];
        as10[0] = "MGunHispanoMkIk 200";
        as10[1] = "MGunHispanoMkIk 200";
        as10[2] = "MGunHispanoMkIk 200";
        as10[3] = "MGunHispanoMkIk 200";
        as10[4] = "PylonAero65A 1";
        as10[5] = "PylonAero65A 1";
        as10[12] = "BombGunMk82 1";
        as10[13] = "BombGunMk82 1";
        Aircraft.weaponsRegister(class1, "02xMk82_Bomb", as10);
        String as11[] = new String[41];
        as11[0] = "MGunHispanoMkIk 200";
        as11[1] = "MGunHispanoMkIk 200";
        as11[2] = "MGunHispanoMkIk 200";
        as11[3] = "MGunHispanoMkIk 200";
        as11[4] = "PylonAero65A 1";
        as11[5] = "PylonAero65A 1";
        as11[12] = "BombGunMk83 1";
        as11[13] = "BombGunMk83 1";
        Aircraft.weaponsRegister(class1, "02xMk83_Bomb", as11);
        String as12[] = new String[41];
        as12[0] = "MGunHispanoMkIk 200";
        as12[1] = "MGunHispanoMkIk 200";
        as12[2] = "MGunHispanoMkIk 200";
        as12[3] = "MGunHispanoMkIk 200";
        as12[4] = "PylonAero65A 1";
        as12[5] = "PylonAero65A 1";
        as12[18] = "Pylon_Zuni 1";
        as12[19] = "Pylon_Zuni 1";
        as12[20] = "RocketGun5inchZuni 1";
        as12[21] = "RocketGun5inchZuni 1";
        as12[22] = "RocketGun5inchZuni 1";
        as12[23] = "RocketGun5inchZuni 1";
        as12[24] = "RocketGun5inchZuni 1";
        as12[25] = "RocketGun5inchZuni 1";
        as12[26] = "RocketGun5inchZuni 1";
        as12[27] = "RocketGun5inchZuni 1";
        Aircraft.weaponsRegister(class1, "02xLAU10", as12);
        String as13[] = new String[41];
        as13[0] = "MGunHispanoMkIk 200";
        as13[1] = "MGunHispanoMkIk 200";
        as13[2] = "MGunHispanoMkIk 200";
        as13[3] = "MGunHispanoMkIk 200";
        as13[28] = "PylonF86_Sidewinder 1";
        as13[29] = "PylonF86_Sidewinder 1";
        as13[32] = "RocketGunAIM9B 1";
        as13[33] = " RocketGunNull 1";
        as13[34] = "RocketGunAIM9B 1";
        as13[35] = " RocketGunNull 1";
        Aircraft.weaponsRegister(class1, "2x_AIM9B", as13);
        Aircraft.weaponsRegister(class1, "none", new String[41]);
    }
}
