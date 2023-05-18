package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class BlenheimS1 extends BLENHEIM
{

    public BlenheimS1()
    {
        fSightCurAltitude = 300F;
        fSightCurSpeed = 50F;
        fSightCurForwardAngle = 0.0F;
        fSightSetForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
    }

    protected void moveBayDoor(float f)
    {
        hierMesh().chunkSetAngles("BayR1_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayR2_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayL1_D0", 0.0F, 90F * f, 0.0F);
        hierMesh().chunkSetAngles("BayL2_D0", 0.0F, -90F * f, 0.0F);
        hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 0.0F, 90F * f);
        hierMesh().chunkSetAngles("Bay2_D0", 0.0F, 0.0F, -90F * f);
        hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 0.0F, -90F * f);
        hierMesh().chunkSetAngles("Bay4_D0", 0.0F, 0.0F, 90F * f);
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurForwardAngle = 0.0F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle += 0.2F;
        if(fSightCurForwardAngle > 75F)
            fSightCurForwardAngle = 75F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)(fSightCurForwardAngle * 1.0F))
        });
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle -= 0.2F;
        if(fSightCurForwardAngle < -15F)
            fSightCurForwardAngle = -15F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
            new Integer((int)(fSightCurForwardAngle * 1.0F))
        });
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip++;
        if(fSightCurSideslip > 45F)
            fSightCurSideslip = 45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 1.0F))
        });
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip--;
        if(fSightCurSideslip < -45F)
            fSightCurSideslip = -45F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
            new Integer((int)(fSightCurSideslip * 1.0F))
        });
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if(fSightCurAltitude > 10000F)
            fSightCurAltitude = 10000F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
    }

    public void typeBomberAdjAltitudeMinus()
    {
        fSightCurAltitude -= 10F;
        if(fSightCurAltitude < 300F)
            fSightCurAltitude = 300F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] {
            new Integer((int)fSightCurAltitude)
        });
    }

    public void typeBomberAdjSpeedReset()
    {
        fSightCurSpeed = 50F;
    }

    public void typeBomberAdjSpeedPlus()
    {
        fSightCurSpeed += 5F;
        if(fSightCurSpeed > 520F)
            fSightCurSpeed = 520F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberAdjSpeedMinus()
    {
        fSightCurSpeed -= 5F;
        if(fSightCurSpeed < 50F)
            fSightCurSpeed = 50F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] {
            new Integer((int)fSightCurSpeed)
        });
    }

    public void typeBomberUpdate(float f)
    {
        double d = ((double)fSightCurSpeed / 3.6D) * Math.sqrt((double)fSightCurAltitude * 2F / 9.81F);
        d -= (double)(fSightCurAltitude * fSightCurAltitude) * 1.419E-005D;
        fSightSetForwardAngle = (float)Math.toDegrees(Math.atan(d / (double)fSightCurAltitude));
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeFloat(fSightCurSpeed);
        netmsgguaranted.writeFloat(fSightCurForwardAngle);
        netmsgguaranted.writeFloat(fSightCurSideslip);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        fSightCurAltitude = netmsginput.readFloat();
        fSightCurSpeed = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readFloat();
        fSightCurSideslip = netmsginput.readFloat();
    }
    
    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        prepareWeapons(this.getClass(), this.hierMesh(), this.thisWeaponsName);
    }

    public static void prepareWeapons(Class aircraftClass, HierMesh hierMesh, String thisWeaponsName) {
        boolean bay13 = thisWeaponsName.indexOf("2x500lbs") != -1 || thisWeaponsName.indexOf("4x250lbs") != -1 || thisWeaponsName.indexOf("4x120lbs") != -1;
        boolean bay24 = thisWeaponsName.indexOf("2x250lbs") != -1 || thisWeaponsName.indexOf("2x500lbs") != -1 || thisWeaponsName.indexOf("4x250lbs") != -1 || thisWeaponsName.indexOf("4x120lbs") != -1;
        boolean bayrl12 = thisWeaponsName.indexOf("2x500lbs") == -1 && thisWeaponsName.indexOf("4x250lbs") == -1;
        boolean bayrlf12 = thisWeaponsName.indexOf("2x500lbs") != -1 || thisWeaponsName.indexOf("4x250lbs") != -1;
        boolean rackfinbl = thisWeaponsName.indexOf("2x500lbs") == -1 && thisWeaponsName.indexOf("4x250lbs") == -1 && thisWeaponsName.indexOf("4x120lbs") == -1 && thisWeaponsName.indexOf("SBC250") == -1;
        if (thisWeaponsName.equals("default") || thisWeaponsName.equals("none") ) {
            bay13 = bay24 = true;
            bayrl12 = bayrlf12 = rackfinbl = false;
        }
        
        hierMesh.chunkVisible("Bay1_D0", bay13);
        hierMesh.chunkVisible("Bay2_D0", bay24);
        hierMesh.chunkVisible("Bay3_D0", bay13);
        hierMesh.chunkVisible("Bay4_D0", bay24);
        hierMesh.chunkVisible("BayR1_D0", bayrl12);
        hierMesh.chunkVisible("BayR2_D0", bayrl12);
        hierMesh.chunkVisible("BayL1_D0", bayrl12);
        hierMesh.chunkVisible("BayL2_D0", bayrl12);
        hierMesh.chunkVisible("BayRF1_D0", bayrlf12);
        hierMesh.chunkVisible("BayRF2_D0", bayrlf12);
        hierMesh.chunkVisible("BayLF1_D0", bayrlf12);
        hierMesh.chunkVisible("BayLF2_D0", bayrlf12);
        hierMesh.chunkVisible("RackFinBL", rackfinbl);
    }
    
//    public void onAircraftLoaded()
//    {
//        super.onAircraftLoaded();
//        if(thisWeaponsName.equals("2x500lbs"))
//        {
//            hierMesh().chunkVisible("Bay1_D0", true); // default false
//            hierMesh().chunkVisible("Bay2_D0", true); // default false
//            hierMesh().chunkVisible("Bay3_D0", true); // default false
//            hierMesh().chunkVisible("Bay4_D0", true); // default false
//            hierMesh().chunkVisible("BayR1_D0", false); // default true
//            hierMesh().chunkVisible("BayR2_D0", false); // default true
//            hierMesh().chunkVisible("BayL1_D0", false); // default true
//            hierMesh().chunkVisible("BayL2_D0", false); // default true
//            hierMesh().chunkVisible("BayRF1_D0", true); // default false
//            hierMesh().chunkVisible("BayRF2_D0", true); // default false
//            hierMesh().chunkVisible("BayLF1_D0", true); // default false
//            hierMesh().chunkVisible("BayLF2_D0", true); // default false
//            hierMesh().chunkVisible("RackFinBL", false); // default true
//            return;
//        }
//        if(thisWeaponsName.equals("4x250lbs"))
//        {
//            hierMesh().chunkVisible("Bay1_D0", true);
//            hierMesh().chunkVisible("Bay2_D0", true);
//            hierMesh().chunkVisible("Bay3_D0", true);
//            hierMesh().chunkVisible("Bay4_D0", true);
//            hierMesh().chunkVisible("BayR1_D0", false);
//            hierMesh().chunkVisible("BayR2_D0", false);
//            hierMesh().chunkVisible("BayL1_D0", false);
//            hierMesh().chunkVisible("BayL2_D0", false);
//            hierMesh().chunkVisible("BayRF1_D0", true);
//            hierMesh().chunkVisible("BayRF2_D0", true);
//            hierMesh().chunkVisible("BayLF1_D0", true);
//            hierMesh().chunkVisible("BayLF2_D0", true);
//            hierMesh().chunkVisible("RackFinBL", false);
//            return;
//        }
//        if(thisWeaponsName.equals("4x250lbs+8x40lbsF"))
//        {
//            hierMesh().chunkVisible("Bay1_D0", true);
//            hierMesh().chunkVisible("Bay2_D0", true);
//            hierMesh().chunkVisible("Bay3_D0", true);
//            hierMesh().chunkVisible("Bay4_D0", true);
//            hierMesh().chunkVisible("RackFinBL", false);
//            return;
//        }
//        if(thisWeaponsName.equals("4x120lbs+8x40lbsPara"))
//        {
//            hierMesh().chunkVisible("Bay1_D0", true);
//            hierMesh().chunkVisible("Bay2_D0", true);
//            hierMesh().chunkVisible("Bay3_D0", true);
//            hierMesh().chunkVisible("Bay4_D0", true);
//            hierMesh().chunkVisible("RackFinBL", false);
//            return;
//        }
//        if(thisWeaponsName.equals("4x250lbs+8x30lbsInc"))
//        {
//            hierMesh().chunkVisible("Bay1_D0", true);
//            hierMesh().chunkVisible("Bay2_D0", true);
//            hierMesh().chunkVisible("Bay3_D0", true);
//            hierMesh().chunkVisible("Bay4_D0", true);
//            hierMesh().chunkVisible("RackFinBL", false);
//            return;
//        }
//        if(thisWeaponsName.equals("2xSBC250_20lbsPara+2x250lbs+8x30lbsInc"))
//        {
//            hierMesh().chunkVisible("Bay1_D0", false);
//            hierMesh().chunkVisible("Bay2_D0", true);
//            hierMesh().chunkVisible("Bay3_D0", false);
//            hierMesh().chunkVisible("Bay4_D0", true);
//            hierMesh().chunkVisible("RackFinBL", false);
//            return;
//        }
//        if(thisWeaponsName.equals("4xSBC250_20lbs+8x30lbsInc"))
//        {
//            hierMesh().chunkVisible("Bay1_D0", false);
//            hierMesh().chunkVisible("Bay2_D0", false);
//            hierMesh().chunkVisible("Bay3_D0", false);
//            hierMesh().chunkVisible("Bay4_D0", false);
//            hierMesh().chunkVisible("RackFinBL", false);
//            return;
//        } else
//        {
//            return;
//        }
//    }

    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurForwardAngle;
    public float fSightSetForwardAngle;
    public float fSightCurSideslip;

    static 
    {
        Class class1 = BlenheimS1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Blenheim");
        Property.set(class1, "meshName", "3DO/Plane/BlenheimMkI(Multi1)/hierF.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Blenheim_MkI.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitBLENHEIM1.class, CockpitBlenheimS1_Bombardier.class, CockpitBLENHEIM1_TGunner.class
        });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 9, 9, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 9, 9, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", 
            "_BombSpawn09", "_BombSpawn10", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", 
            "_ExternalBomb09", "_ExternalBomb10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb15", "_ExternalBomb16", 
            "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", 
            "_ExternalBomb27", "_ExternalBomb28", "_ExternalBomb29", "_ExternalBomb30", "_ExternalBomb31", "_ExternalBomb32", "_ExternalBomb33", "_ExternalBomb34", "_ExternalBomb35", "_ExternalBomb36", 
            "_ExternalBomb37", "_ExternalBomb38", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb39", "_ExternalBomb40", "_ExternalBomb41", "_ExternalBomb42", "_ExternalBomb43", "_ExternalBomb44", 
            "_ExternalBomb45", "_ExternalBomb46", "_ExternalBomb47", "_ExternalBomb48", "_ExternalBomb49", "_ExternalBomb50", "_ExternalBomb51", "_ExternalBomb52", "_ExternalBomb53", "_ExternalBomb54", 
            "_ExternalBomb55", "_ExternalBomb56", "_ExternalBomb57", "_ExternalBomb58", "_ExternalBomb59", "_ExternalBomb60", "_ExternalBomb61", "_ExternalBomb62"
        });
    }
}
