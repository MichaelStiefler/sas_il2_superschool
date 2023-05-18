package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class YAK_15 extends YAK
{

    public YAK_15()
    {
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearC99_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, cvt(f, 0.1F, 0.6F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.1F, 0.6F, 0.0F, -83.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, cvt(f, 0.4F, 0.9F, 0.0F, -85F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f, 0.4F, 0.9F, 0.0F, -83.5F), 0.0F);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxCannon01"))
            {
                if(getEnergyPastArmor(9.8F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon (0) Disabled..");
                    FM.AS.setJamBullets(1, 0);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if(s.startsWith("xxCannon02"))
            {
                if(getEnergyPastArmor(9.8F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Cannon (0) Disabled..");
                    FM.AS.setJamBullets(1, 1);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
        }
        super.hitBone(s, shot, point3d);
    }

    public void update(float f)
    {
        if(Config.isUSE_RENDER() && FM.AS.isMaster())
            if(FM.EI.engines[0].getPowerOutput() > 0.8F && FM.EI.engines[0].getStage() == 6)
            {
                if(FM.EI.engines[0].getPowerOutput() > 0.95F)
                    FM.AS.setSootState(this, 0, 3);
                else
                    FM.AS.setSootState(this, 0, 2);
            } else
            {
                FM.AS.setSootState(this, 0, 0);
            }
        super.update(f);
    }

    static 
    {
        Class class1 = YAK_15.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Yak");
        Property.set(class1, "meshName", "3DO/Plane/Yak-15(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_ru", "3DO/Plane/Yak-15(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1946F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/Yak-15.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_15.class });
        Property.set(class1, "LOSElevation", 1.0989F);
        weaponTriggersRegister(class1, new int[] {
            1, 1
        });
        weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02"
        });
    }
}
