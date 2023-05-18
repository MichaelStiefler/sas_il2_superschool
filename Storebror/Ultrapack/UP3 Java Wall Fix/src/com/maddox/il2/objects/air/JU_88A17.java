package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class JU_88A17 extends JU_88NEW
    implements TypeStormovik, TypeBomber, TypeScout, TypeHasToKG
{

    public JU_88A17()
    {
        diveMechStage = 0;
        bNDives = false;
        bSightAutomation = false;
        bSightBombDump = false;
        fSightCurDistance = 0.0F;
        fSightCurForwardAngle = 0.0F;
        fSightCurSideslip = 0.0F;
        fSightCurAltitude = 850F;
        fSightCurSpeed = 150F;
        fSightCurReadyness = 0.0F;
        fDiveRecoveryAlt = 850F;
        fDiveVelocity = 150F;
        fDiveAngle = 70F;
        fAOB = 0.0F;
        fShipSpeed = 15F;
        spreadAngle = 0;
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
            break;
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("HMask1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            break;

        case 2:
            hierMesh().chunkVisible("Pilot3_D0", false);
            hierMesh().chunkVisible("Pilot3_D1", true);
            hierMesh().chunkVisible("HMask3_D0", false);
            break;
        }
    }

    public void typeBomberUpdate(float f)
    {
        if(Math.abs(FM.Or.getKren()) > 4.5D)
        {
            fSightCurReadyness -= 0.0666666F * f;
            if(fSightCurReadyness < 0.0F)
                fSightCurReadyness = 0.0F;
        }
        if(fSightCurReadyness < 1.0F)
            fSightCurReadyness += 0.0333333F * f;
        else
        if(bSightAutomation)
            return;
    }

    public boolean typeBomberToggleAutomation()
    {
        bSightAutomation = false;
        bSightBombDump = false;
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
        spreadAngle++;
        if(spreadAngle > 30)
            spreadAngle = 30;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpread", new Object[] {
            new Integer(spreadAngle)
        });
        FM.AS.setSpreadAngle(spreadAngle);
    }

    public void typeBomberAdjDistanceMinus()
    {
        spreadAngle--;
        if(spreadAngle < 0)
            spreadAngle = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpread", new Object[] {
            new Integer(spreadAngle)
        });
        FM.AS.setSpreadAngle(spreadAngle);
    }

    public void typeBomberAdjSideslipReset()
    {
    }

    public void typeBomberAdjSideslipPlus()
    {
        fAOB++;
        if(fAOB > 180F)
            fAOB = 180F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] {
            new Integer((int)fAOB)
        });
        ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
    }

    public void typeBomberAdjSideslipMinus()
    {
        fAOB--;
        if(fAOB < -180F)
            fAOB = -180F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGAOB", new Object[] {
            new Integer((int)fAOB)
        });
        ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
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
        fShipSpeed++;
        if(fShipSpeed > 35F)
            fShipSpeed = 35F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] {
            new Integer((int)fShipSpeed)
        });
        ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
    }

    public void typeBomberAdjSpeedMinus()
    {
        fShipSpeed--;
        if(fShipSpeed < 0.0F)
            fShipSpeed = 0.0F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "TOKGSpeed", new Object[] {
            new Integer((int)fShipSpeed)
        });
        ToKGUtils.setTorpedoGyroAngle(FM, fAOB, fShipSpeed);
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
        netmsgguaranted.writeFloat(fSightCurDistance);
        netmsgguaranted.writeByte((int)fSightCurForwardAngle);
        netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
        netmsgguaranted.writeFloat(fSightCurAltitude);
        netmsgguaranted.writeByte((int)(fSightCurSpeed / 2.5F));
        netmsgguaranted.writeByte((int)(fSightCurReadyness * 200F));
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        int i = netmsginput.readUnsignedByte();
        bSightAutomation = (i & 1) != 0;
        bSightBombDump = (i & 2) != 0;
        fSightCurDistance = netmsginput.readFloat();
        fSightCurForwardAngle = netmsginput.readUnsignedByte();
        fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
        fSightCurAltitude = fDiveRecoveryAlt = netmsginput.readFloat();
        fSightCurSpeed = fDiveVelocity = netmsginput.readUnsignedByte() * 2.5F;
        fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void update(float f)
    {
        super.update(f);
        if(Pitot.Indicator((float)FM.Loc.z, FM.getSpeed()) > 70F && FM.CT.getFlap() > 0.01D && FM.CT.FlapsControl != 0.0F)
        {
            FM.CT.FlapsControl = 0.0F;
            World.cur();
            if(FM.actor == World.getPlayerAircraft())
                HUD.log("FlapsRaised");
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        for(int i = 1; i < 3; i++)
            if(FM.getAltitude() < 3000F)
                hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else
                hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public boolean isSalvo()
    {
        return thisWeaponsName.indexOf("salvo") != -1;
    }

    public static boolean bChangedPit = false;
    public int diveMechStage;
    public boolean bNDives;
    private boolean bSightAutomation;
    private boolean bSightBombDump;
    private float fSightCurDistance;
    public float fSightCurForwardAngle;
    public float fSightCurSideslip;
    public float fSightCurAltitude;
    public float fSightCurSpeed;
    public float fSightCurReadyness;
    public float fDiveRecoveryAlt;
    public float fDiveVelocity;
    public float fDiveAngle;
    protected float fAOB;
    protected float fShipSpeed;
    protected int spreadAngle;

    static 
    {
        Class class1 = JU_88A17.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88A-17/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88A-17.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitJU_88A17.class, CockpitJU_88A17_Bombardier.class, CockpitJU_88A17_NGunner.class, CockpitJU_88A17_RGunner.class
        });
        Property.set(class1, "LOSElevation", 1.0976F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10, 11, 12, 3, 3, 3, 3, 1
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_CANNON01"
        });
    }
}
