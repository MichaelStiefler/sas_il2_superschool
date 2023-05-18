package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class PE_2SERIES110 extends PE_2
    implements TypeBomber, TypeDiveBomber, TypeTransport
{

    public PE_2SERIES110()
    {
        tme = 0L;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(FM.turret.length != 0)
        {
            FM.turret[1].bIsOperable = false;
            FM.turret[2].bIsOperable = false;
            FM.turret[3].bIsOperable = false;
        }
        gun3 = getGunByHookName("_MGUN03");
        gun4 = getGunByHookName("_MGUN04");
    }

    public void update(float f)
    {
        if(Time.current() > tme)
        {
            tme = Time.current() + World.Rnd().nextLong(5000L, 20000L);
            if(FM.turret.length != 0)
            {
                gun3.loadBullets(Math.min(gun3.countBullets(), gun4.countBullets()));
                gun4.loadBullets(gun3.countBullets());
                Actor actor = null;
                for(int i = 1; i < 4; i++)
                    if(FM.turret[i].bIsOperable)
                        actor = FM.turret[i].target;

                for(int j = 1; j < 4; j++)
                    FM.turret[j].target = actor;

                if(actor == null)
                    setRadist(0);
                else
                if(Actor.isValid(actor))
                {
                    pos.getAbs(tmpLoc2);
                    actor.pos.getAbs(tmpLoc3);
                    tmpLoc2.transformInv(tmpLoc3.getPoint());
                    if(tmpLoc3.getPoint().x < -Math.abs(tmpLoc3.getPoint().y))
                        setRadist(1);
                    else
                    if(tmpLoc3.getPoint().y < 0.0D)
                        setRadist(2);
                    else
                        setRadist(3);
                }
            }
        }
        super.update(f);
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch(i)
        {
        default:
            break;

        case 0:
            if(f < -110F)
            {
                f = -110F;
                flag = false;
            }
            if(f > 88F)
            {
                f = 88F;
                flag = false;
            }
            if(f1 < -1F)
            {
                f1 = -1F;
                flag = false;
            }
            if(f1 > 55F)
            {
                f1 = 55F;
                flag = false;
            }
            break;

        case 1:
            if(f < -30F)
            {
                f = -30F;
                flag = false;
            }
            if(f > 30F)
            {
                f = 30F;
                flag = false;
            }
            if(f1 < -45F)
            {
                f1 = -45F;
                flag = false;
            }
            if(f1 > 0.0F)
            {
                f1 = 0.0F;
                flag = false;
            }
            break;

        case 2:
            if(f < -30F)
            {
                f = -30F;
                flag = false;
            }
            if(f > 30F)
            {
                f = 30F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            break;

        case 3:
            if(f < -30F)
            {
                f = -30F;
                flag = false;
            }
            if(f > 30F)
            {
                f = 30F;
                flag = false;
            }
            if(f1 < -30F)
            {
                f1 = -30F;
                flag = false;
            }
            if(f1 > 30F)
            {
                f1 = 30F;
                flag = false;
            }
            break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    private void setRadist(int i)
    {
        hierMesh().chunkVisible("Pilot3_D0", false);
        hierMesh().chunkVisible("Pilot3a_D0", false);
        hierMesh().chunkVisible("Pilot3b_D0", false);
        hierMesh().chunkVisible("Pilot3c_D0", false);
        FM.turret[1].bIsOperable = false;
        FM.turret[2].bIsOperable = false;
        FM.turret[3].bIsOperable = false;
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot3_D0", true);
            FM.turret[1].bIsOperable = true;
            break;

        case 1:
            hierMesh().chunkVisible("Pilot3a_D0", true);
            FM.turret[1].bIsOperable = true;
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3b_D0", true);
            FM.turret[2].bIsOperable = true;
            hierMesh().chunkVisible("Turret3B_D0", true);
            hierMesh().chunkVisible("Turret4B_D0", false);
            break;

        case 3:
            hierMesh().chunkVisible("Pilot3c_D0", true);
            FM.turret[3].bIsOperable = true;
            hierMesh().chunkVisible("Turret3B_D0", false);
            hierMesh().chunkVisible("Turret4B_D0", true);
            break;
        }
    }

    public void doWoundPilot(int i, float f)
    {
        switch(i)
        {
        case 1:
            FM.turret[0].setHealth(f);
            break;

        case 2:
            FM.turret[1].setHealth(f);
            FM.turret[2].setHealth(f);
            FM.turret[3].setHealth(f);
            break;
        }
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
        fSightSetForwardAngle = (float)Math.toDegrees(Math.atan(d / fSightCurAltitude));
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

    private long tme;
    private Gun gun3;
    private Gun gun4;

    static 
    {
        Class class1 = PE_2SERIES110.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Pe-2");
        Property.set(class1, "meshName", "3DO/Plane/Pe-2series110/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "yearService", 1942.7F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Pe-2series110.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitPE2_110.class, CockpitPE2_Bombardier.class, CockpitPE2_110_TGunner.class
        });
        Property.set(class1, "LOSElevation", 0.76315F);
        weaponTriggersRegister(class1, new int[] {
            0, 1, 10, 11, 12, 13, 3, 3, 3, 3, 
            3, 3, 3, 3, 3, 3
        });
        weaponHooksRegister(class1, new String[] {
            "_CANNON02", "_CANNON01", "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", 
            "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06"
        });
    }
}
