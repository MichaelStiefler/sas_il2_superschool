package com.maddox.il2.objects.air;

import com.maddox.il2.engine.*;
import com.maddox.il2.fm.Controls;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

public class FW_190SeaFg extends FW_190Sea
    implements TypeFighter, TypeBNZFighter, TypeBomber
{

    public FW_190SeaFg()
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

    public void typeBomberUpdate(float f1)
    {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1)
        throws IOException
    {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput1)
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
        float f_0_ = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -f_0_, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -f_0_, 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        if(this.FM.CT.getGear() >= 0.98F)
            hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    protected void nextDMGLevel(String string, int i, Actor actor)
    {
        super.nextDMGLevel(string, i, actor);
        if(this.FM.isPlayers())
            bChangedPit = true;
    }

    protected void nextCUTLevel(String string, int i, Actor actor)
    {
        super.nextCUTLevel(string, i, actor);
        if(this.FM.isPlayers())
            bChangedPit = true;
    }

    public void msgCollision(Actor actor, String string, String string_0_)
    {
        if((!isNet() || !isNetMirror()) && !string.startsWith("Hook"))
            super.msgCollision(actor, string, string_0_);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(getGunByHookName("_MGUN01") instanceof GunEmpty)
        {
            hierMesh().chunkVisible("7mmC_D0", false);
            hierMesh().chunkVisible("7mmCowl_D0", true);
        }
        if(getGunByHookName("_CANNON03") instanceof GunEmpty)
            hierMesh().chunkVisible("20mmL_D0", false);
        if(getGunByHookName("_CANNON04") instanceof GunEmpty)
            hierMesh().chunkVisible("20mmR_D0", false);
        if(!(getGunByHookName("_ExternalDev05") instanceof GunEmpty))
        {
            hierMesh().chunkVisible("Flap01_D0", false);
            hierMesh().chunkVisible("Flap01Holed_D0", true);
        }
        if(!(getGunByHookName("_ExternalDev06") instanceof GunEmpty))
        {
            hierMesh().chunkVisible("Flap04_D0", false);
            hierMesh().chunkVisible("Flap04Holed_D0", true);
        }
    }

    private float kangle;
    public static boolean bChangedPit = false;

    static 
    {
        Class var_class = FW_190SeaFg.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "FW190");
        Property.set(var_class, "meshName", "3DO/Plane/Fw-190A-4T/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar01());
        Property.set(var_class, "yearService", 1942.6F);
        Property.set(var_class, "yearExpired", 1948F);
        Property.set(var_class, "FlightModel", "FlightModels/Fw-190A-4N.fmd");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitFW_190A4T.class });
        Property.set(var_class, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(var_class, new int[] {
            0, 0, 1, 1, 1, 1, 9, 9, 9, 9, 
            9, 9, 2, 2, 9, 9, 3
        });
        Aircraft.weaponHooksRegister(var_class, new String[] {
            "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", 
            "_ExternalDev07", "_ExternalDev08", "_ExternalRock01", "_ExternalRock02", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01"
        });
    }
}
