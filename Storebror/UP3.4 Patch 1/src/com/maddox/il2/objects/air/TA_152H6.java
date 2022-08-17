package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.*;
import com.maddox.util.HashMapInt;
import java.io.IOException;
import java.util.ArrayList;

public class TA_152H6 extends TA_152NEW // TA_152H_Paulus
    implements TypeFighter, TypeFighterAceMaker
{

    public TA_152H6()
    {
        k14Mode = 0;
        k14WingspanType = 0;
        k14Distance = 200F;
        kangle = 0.0F;
        SecondProp = 0;
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -77F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -102F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 20F * f, 0.0F, 0.0F);
        float f1 = Math.max(-f * 1500F, -94F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, f1, 0.0F);
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = 0.0F;
        Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.0F, 1.0F, 0.0F, 0.1F);
        hiermesh.chunkSetLocate("poleL_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetLocate("poleR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        if(this.FM.CT.getGear() < 0.98F)
        {
            return;
        } else
        {
            hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.44F, 0.0F, 0.44F);
        hierMesh().chunkSetLocate("GearL2a_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.44F, 0.0F, 0.44F);
        hierMesh().chunkSetLocate("GearR2a_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveFan(float f)
    {
        hierMesh().chunkFind(Aircraft.Props[1][0]);
        SecondProp = 1;
        int i = 0;
        for(int j = 0; j < (SecondProp == 1 ? 2 : 1); j++)
        {
            if(oldProp[j] < 2)
            {
                i = Math.abs((int)(FM.EI.engines[0].getw() * 0.06F));
                if(i >= 1)
                    i = 1;
                if(i != oldProp[j] && hierMesh().isChunkVisible(Aircraft.Props[j][oldProp[j]]))
                {
                    hierMesh().chunkVisible(Aircraft.Props[j][oldProp[j]], false);
                    oldProp[j] = i;
                    hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if(i == 0)
            {
                propPos[j] = (propPos[j] + 57.3F * FM.EI.engines[0].getw() * f) % 360F;
            } else
            {
                float f1 = 57.3F * FM.EI.engines[0].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if(f1 <= 0.5F)
                    f1 *= 2.0F;
                else
                    f1 = f1 * 2.0F - 2.0F;
                f1 *= 1200F;
                propPos[j] = (propPos[j] + f1 * f) % 360F;
            }
            if(j == 0)
                hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, propPos[j], 0.0F);
            else
                hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, propPos[j] - 20F, 0.0F);
        }

    }

    public void hitProp(int i, int j, Actor actor)
    {
        if(i > FM.EI.getNum() - 1 || oldProp[i] == 2)
            return;
        if((isChunkAnyDamageVisible("Prop" + (i + 1)) || isChunkAnyDamageVisible("PropRot" + (i + 1))) && SecondProp == 1)
        {
            hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
            hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
            hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
        }
        super.hitProp(i, j, actor);
    }

    public void update(float f)
    {
        for(int i = 1; i < 15; i++)
            hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * kangle, 0.0F);

        kangle = 0.95F * kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        if(this.FM.Loc.z > 9000D)
        {
            if(!this.FM.EI.engines[0].getControlAfterburner())
                this.FM.EI.engines[0].setAfterburnerType(2);
        } else
        if(!this.FM.EI.engines[0].getControlAfterburner())
            this.FM.EI.engines[0].setAfterburnerType(1);
        World.cur().diffCur.Torque_N_Gyro_Effects = false;
        super.update(f);
    }

    public boolean typeFighterAceMakerToggleAutomation()
    {
        k14Mode++;
        if(k14Mode > 2)
            k14Mode = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset()
    {
    }

    public void typeFighterAceMakerAdjDistancePlus()
    {
        k14Distance += 10F;
        if(k14Distance > 800F)
            k14Distance = 800F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus()
    {
        k14Distance -= 10F;
        if(k14Distance < 200F)
            k14Distance = 200F;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset()
    {
    }

    public void typeFighterAceMakerAdjSideslipPlus()
    {
        k14WingspanType--;
        if(k14WingspanType < 0)
            k14WingspanType = 0;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus()
    {
        k14WingspanType++;
        if(k14WingspanType > 9)
            k14WingspanType = 9;
        HUD.log(AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
        netmsgguaranted.writeByte(k14Mode);
        netmsgguaranted.writeByte(k14WingspanType);
        netmsgguaranted.writeFloat(k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
        k14Mode = netmsginput.readByte();
        k14WingspanType = netmsginput.readByte();
        k14Distance = netmsginput.readFloat();
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        this.FM.AS.wantBeaconsNet(true);
    }

    public int k14Mode;
    public int k14WingspanType;
    public float k14Distance;
    private float kangle;
    protected int SecondProp;

    static 
    {
        Class class1 = TA_152H6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ta.152");
        Property.set(class1, "meshName", "3DO/Plane/Ta-152H-5/hierCRP.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1945.5F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Ta-152H-6.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitTA_152H6.class
        });
        Property.set(class1, "LOSElevation", 0.764106F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 1, 1, 1, 1, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01", "_ExternalDev02"
        });
    }
}
