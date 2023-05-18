package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class FW_190D13 extends FW_190
    implements TypeBomber
{

    public FW_190D13()
    {
        kangle = 0.0F;
    }

    public boolean typeBomberToggleAutomation()
    {
        return false;
    }

    public void typeBomberAdjDistanceReset()
    {
    }

    public void typeBomberAdjDistancePlus()
    {
    }

    public void typeBomberAdjDistanceMinus()
    {
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
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 157F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC99_D0", 20F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        if(FM.CT.getGear() >= 0.98F)
            hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(getGunByHookName("_CANNON03") instanceof GunEmpty)
        {
            hierMesh().chunkVisible("30mmL_D0", false);
            hierMesh().chunkVisible("30mmR_D0", false);
        }
        if(getGunByHookName("_MGUN01") instanceof GunEmpty)
            hierMesh().chunkVisible("30mmL_D0", false);
        if(getGunByHookName("_MGUN02") instanceof GunEmpty)
            hierMesh().chunkVisible("30mmR_D0", false);
    }

    public void update(float f)
    {
        for(int i = 1; i < 13; i++)
            hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -10F * kangle, 0.0F);

        kangle = 0.95F * kangle + 0.05F * FM.EI.engines[0].getControlRadiator();
        float f1 = World.Rnd().nextFloat(0.87F, 1.04F);
        if(FM.isPlayers() && FM.CT.cockpitDoorControl > 0.9F && FM.getSpeedKMH() > 180F * f1 && FM.AS.aircraft.hierMesh().chunkFindCheck("Blister1_D0") != -1 && FM.AS.getPilotHealth(0) > 0.0F && FM.isPlayers() && FM.CT.cockpitDoorControl > 0.9F && FM.getSpeedKMH() > 180F * f1 && FM.AS.aircraft.hierMesh().chunkFindCheck("Wire_D0") != -1 && FM.AS.getPilotHealth(0) > 0.0F)
        {
            this.playSound("aircraft.arrach", true);
            if(this == World.getPlayerAircraft())
                ((CockpitFW_190D11)Main3D.cur3D().cockpitCur).blowoffcanopyforCirXdl();
            FM.AS.aircraft.hierMesh().hideSubTrees("Wire_D0");
            FM.AS.aircraft.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage((ActorHMesh)FM.AS.actor, FM.AS.aircraft.hierMesh().chunkFind("Blister1_D0"));
            Wreckage wreckage1 = new Wreckage((ActorHMesh)FM.AS.actor, FM.AS.aircraft.hierMesh().chunkFind("Wire_D0"));
            wreckage.collide(true);
            wreckage1.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(FM.AS.aircraft.FM.Vwld);
            wreckage.setSpeed(vector3d);
            wreckage1.setSpeed(vector3d);
            FM.CT.cockpitDoorControl = 0.9F;
            FM.CT.bHasCockpitDoorControl = false;
            FM.VmaxAllowed = 161F;
            FM.Sq.dragEngineCx[0] *= 6.2F;
        }
        super.update(f);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -0.53F);
        hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    private float kangle;

    static 
    {
        Class class1 = FW_190D13.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190D-13(Beta)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1943.11F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190D-13.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitFW_190D11.class
        });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 1, 1, 9, 3, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalBomb01", "_ExternalDev02"
        });
    }
}
