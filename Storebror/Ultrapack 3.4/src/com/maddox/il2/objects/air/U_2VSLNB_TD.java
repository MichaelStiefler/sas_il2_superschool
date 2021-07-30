package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.weapons.BombGunPara1;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

public abstract class U_2VSLNB_TD extends U_2xyz
{

    public U_2VSLNB_TD()
    {
        this.fSightCurForwardAngle = 9F;
        Spare1 = true;
        Spare2 = true;
        Spare3 = true;
        Spare4 = true;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasBrakeControl = false;
//        if(FM.CT.Weapons[10] != null)
//            FM.CT.Weapons[10][0].setReloadParams(5, 2.0F);
        if(FM.CT.Weapons[3] != null && (FM.CT.Weapons[3][0] instanceof BombGunPara1))
        {
            FM.crew = 1;
            FM.AS.astatePilotFunctions[0] = 1;
        }
        this.moveGunner();
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        if(i == 2)
        {
            super.doRemoveBodyFromPlane(3);
            gunnerEjected = true;
        }
    }

    public static String getSkinPrefix(String s, Regiment regiment)
    {
        if(regiment == null || regiment.country() == null)
            return "";
        if(regiment.country().equals(PaintScheme.countryRussia))
        {
            int i = Mission.getMissionDate(true);
            if(i > 0)
                if(i > 0x1287d91)
                    return "43_";
                else
                    return "";
        }
        return "";
    }

    public void update(float f)
    {
        super.update(f);
        gunnerAiming();
        gunnerTarget();
        if(FM.CT.Weapons[3] != null && (FM.CT.Weapons[3][0] instanceof BombGunPara1) && !FM.CT.Weapons[3][0].haveBullets())
        {
            if(FM.AS.astateBailoutStep == 12)
                FM.AS.astateBailoutStep = 14;
            this.doRemoveBodyFromPlane(2);
            this.doRemoveBodyFromPlane(3);
            gunnerEjected = true;
        }
    }

    public void doWoundPilot(int i, float f)
    {
        super.doWoundPilot(i, f);
        switch(i)
        {
        case 1:
            FM.turret[0].setHealth(f);
            if(f <= 0.0F)
                gunnerDead = true;
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        super.doMurderPilot(i);
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D1", hierMesh().isChunkVisible("Pilot2_D0"));
            hierMesh().chunkVisible("Pilot3_D1", hierMesh().isChunkVisible("Pilot3_D0"));
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot3_D0", false);
            gunnerDead = true;
            break;
        }
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
        fSightCurForwardAngle = 9F;
    }

    public void typeBomberAdjDistancePlus()
    {
        fSightCurForwardAngle++;
        if(fSightCurForwardAngle > 70F)
            fSightCurForwardAngle = 70F;
        if(fSightCurForwardAngle <= 33F)
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
                new Float(fSightCurForwardAngle * 1.0F)
            });
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle--;
        if(fSightCurForwardAngle < 9F)
            fSightCurForwardAngle = 9F;
        if(fSightCurForwardAngle <= 33F)
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
                new Float(fSightCurForwardAngle * 1.0F)
            });
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
    }

    public void typeBomberAdjSideslipMinus()
    {
    }

    public void typeBomberAdjAltitudeReset()
    {
    }

    public void typeBomberAdjAltitudePlus()
    {
    }

    public void typeBomberAdjAltitudeMinus()
    {
    }

    public void typeBomberAdjSpeedReset()
    {
    }

    public void typeBomberAdjSpeedPlus()
    {
    }

    public void typeBomberAdjSpeedMinus()
    {
    }

    public void typeBomberUpdate(float f)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeFloat(fSightCurForwardAngle);
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        fSightCurForwardAngle = netmsginput.readFloat();
    }

}
