package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class SM79i extends SM79
    implements TypeBomber, TypeTransport
{

    public SM79i()
    {
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.AS.wantBeaconsNet(true);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 3:
            if(f < -45F)
            {
                f = -45F;
                flag = false;
            }
            if(f > 60F)
            {
                f = 60F;
                flag = false;
            }
            if(f1 < -35F)
            {
                f1 = -35F;
                flag = false;
            }
            if(f1 > 35F)
            {
                f1 = 35F;
                flag = false;
            }
            break;

        case 0:
            if(f < -40F)
            {
                f = -40F;
                flag = false;
            }
            if(f > 40F)
            {
                f = 40F;
                flag = false;
            }
            if(f1 < -4F)
            {
                f1 = -4F;
                flag = false;
            }
            if(f1 > 70F)
            {
                f1 = 70F;
                flag = false;
            }
            break;

        case 1:
            float f2 = 10F;
            float f3 = 1.0F;
            float f4 = 15F;
            if(f < -40F)
            {
                f = -40F;
                flag = false;
            }
            if(f > 40F)
            {
                f = 40F;
                flag = false;
            }
            if(f1 < 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            if(f1 > 45F)
            {
                f1 = 45F;
                flag = false;
            }
            break;

        case 2:
            if(f < -60F)
            {
                f = -60F;
                flag = false;
            }
            if(f > 45F)
            {
                f = 45F;
                flag = false;
            }
            if(f1 < -35F)
            {
                f1 = -35F;
                flag = false;
            }
            if(f1 > 35F)
            {
                f1 = 35F;
                flag = false;
            }
            break;
        }
        af[0] = f;
        af[1] = f1;
        return flag;
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
        fSightCurForwardAngle += 0.4F;
        if(fSightCurForwardAngle > 75F)
            fSightCurForwardAngle = 75F;
    }

    public void typeBomberAdjDistanceMinus()
    {
        fSightCurForwardAngle -= 0.4F;
        if(fSightCurForwardAngle < -16F)
            fSightCurForwardAngle = -16F;
    }

    public void typeBomberAdjSideslipReset()
    {
        fSightCurSideslip = 0.0F;
    }

    public void typeBomberAdjSideslipPlus()
    {
        fSightCurSideslip += 0.5D;
        if(thisWeaponsName.startsWith("1x"))
        {
            if(fSightCurSideslip > 40F)
                fSightCurSideslip = 40F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + fSightCurSideslip);
        } else
        {
            if(fSightCurSideslip > 12F)
                fSightCurSideslip = 12F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + fSightCurSideslip);
        }
    }

    public void typeBomberAdjSideslipMinus()
    {
        fSightCurSideslip -= 0.5D;
        if(thisWeaponsName.startsWith("1x"))
        {
            if(fSightCurSideslip < -40F)
                fSightCurSideslip = -40F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Torpedo Angle  " + fSightCurSideslip);
        } else
        {
            if(fSightCurSideslip < -12F)
                fSightCurSideslip = -12F;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip " + fSightCurSideslip);
        }
    }

    public void typeBomberAdjAltitudeReset()
    {
        fSightCurAltitude = 300F;
    }

    public void typeBomberAdjAltitudePlus()
    {
        fSightCurAltitude += 10F;
        if(fSightCurAltitude > 6000F)
            fSightCurAltitude = 6000F;
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
        if(fSightCurSpeed > 650F)
            fSightCurSpeed = 650F;
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
        double d = (fSightCurSpeed / 3.6D) * Math.sqrt(fSightCurAltitude * 0.203873598D);
        d -= fSightCurAltitude * fSightCurAltitude * 1.419E-005D;
        fSightSetForwardAngle = (float)Math.atan(d / fSightCurAltitude);
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

    public float fSightCurAltitude;
    public float fSightCurSpeed;

    static 
    {
        Class class1 = SM79i.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "SM.79");
        Property.set(class1, "meshName_it", "3do/plane/SM79-I(it)/hier.him");
        Property.set(class1, "PaintScheme_it", new PaintSchemeBMPar09());
        Property.set(class1, "meshName", "3do/plane/SM79-I(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar09());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/SM79.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitSM79.class, CockpitSM79_Bombardier.class, CockpitSM79_TGunner.class, CockpitSM79_BGunner.class, CockpitSM79_LGunner.class, CockpitSM79_RGunner.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 10, 13, 12, 11, 9, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 
            3, 3
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_12,7_01", "_12,7_02", "_12,7_00", "_12,7_04", "_12,7_03", "_ExternalDev01", "_ExternalBomb01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", 
            "_BombSpawnC01", "_BombSpawnC02", "_BombSpawnC03", "_BombSpawnC04", "_BombSpawnC05", "_BombSpawnC06", "_BombSpawnC07", "_BombSpawnC08", "_BombSpawnC09", "_BombSpawnC10", 
            "_BombSpawnC11", "_BombSpawnC12"
        });
    }
}
