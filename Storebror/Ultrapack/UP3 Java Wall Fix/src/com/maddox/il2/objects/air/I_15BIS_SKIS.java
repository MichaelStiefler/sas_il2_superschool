package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class I_15BIS_SKIS extends I_15xyz
{

    public I_15BIS_SKIS()
    {
        skiAngleL = 0.0F;
        skiAngleR = 0.0F;
        spring = 0.15F;
        wireRandomizer1 = 0.0F;
        wireRandomizer2 = 0.0F;
        wireRandomizer3 = 0.0F;
        wireRandomizer4 = 0.0F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.CT.bHasBrakeControl = false;
        wireRandomizer1 = (float)(Math.random() * 2D) - 1.0F;
        wireRandomizer2 = (float)(Math.random() * 2D) - 1.0F;
        wireRandomizer3 = (float)(Math.random() * 2D) - 1.0F;
        wireRandomizer4 = (float)(Math.random() * 2D) - 1.0F;
    }

    public void sfxWheels()
    {
    }

    protected void moveFan(float f)
    {
        if(Config.isUSE_RENDER())
        {
            boolean flag = false;
            float f1 = Aircraft.cvt(FM.getSpeed(), 30F, 80F, 1.0F, 0.0F);
            float f2 = Aircraft.cvt(FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
            if(FM.Gears.gWheelSinking[0] > 0.0F)
            {
                flag = true;
                skiAngleL = 0.5F * skiAngleL + 0.5F * FM.Or.getTangage();
                if(skiAngleL > 20F)
                    skiAngleL = skiAngleL - spring;
                hierMesh().chunkSetAngles("SkiL1_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2, f2) - skiAngleL, World.Rnd().nextFloat(f2, f2));
            } else
            {
                if(skiAngleL > f1 * -10F + 0.01D)
                {
                    skiAngleL = skiAngleL - spring;
                    flag = true;
                } else
                if(skiAngleL < f1 * -10F - 0.01D)
                {
                    skiAngleL = skiAngleL + spring;
                    flag = true;
                }
                hierMesh().chunkSetAngles("SkiL1_D0", 0.0F, -skiAngleL, 0.0F);
            }
            if(FM.Gears.gWheelSinking[1] > 0.0F)
            {
                flag = true;
                skiAngleR = 0.5F * skiAngleR + 0.5F * FM.Or.getTangage();
                if(skiAngleR > 20F)
                    skiAngleR = skiAngleR - spring;
                hierMesh().chunkSetAngles("SkiR1_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2, f2) - skiAngleR, World.Rnd().nextFloat(f2, f2));
                if(FM.Gears.gWheelSinking[0] == 0.0F && FM.Or.getRoll() < 365F && FM.Or.getRoll() > 355F)
                {
                    skiAngleL = skiAngleR;
                    hierMesh().chunkSetAngles("SkiL1_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2, f2) - skiAngleL, World.Rnd().nextFloat(f2, f2));
                }
            } else
            {
                if(skiAngleR > f1 * -10F + 0.01D)
                {
                    skiAngleR = skiAngleR - spring;
                    flag = true;
                } else
                if(skiAngleR < f1 * -10F - 0.01D)
                {
                    skiAngleR = skiAngleR + spring;
                    flag = true;
                }
                hierMesh().chunkSetAngles("SkiR1_D0", 0.0F, -skiAngleR, 0.0F);
            }
            if(!flag && f1 == 0.0F)
            {
                super.moveFan(f);
                return;
            }
            hierMesh().chunkSetAngles("SkiC_D0", 0.0F, (skiAngleL + skiAngleR) / 2.0F, 0.0F);
            float f3 = skiAngleL / 20F;
            if(skiAngleL > 0.0F)
            {
                hierMesh().chunkSetAngles("LSkiFrontDownWire1_d0", 0.0F, -f3 * 4F, f3 * 12.4F);
                hierMesh().chunkSetAngles("LSkiFrontDownWire2_d0", 0.0F, -f3 * 4F, f3 * 12.4F);
            } else
            {
                hierMesh().chunkSetAngles("LSkiFrontDownWire1_d0", 0.0F, -f3 * 8F, f3 * 12.4F);
                hierMesh().chunkSetAngles("LSkiFrontDownWire2_d0", 0.0F, -f3 * 8F, f3 * 12.4F);
            }
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.xyz[0] = -0.16F * f3 + suspL;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            hierMesh().chunkSetLocate("LSkiFrontUpWire_d0", Aircraft.xyz, Aircraft.ypr);
            if(skiAngleL < 0.0F)
            {
                hierMesh().chunkSetAngles("LWire1_d0", 0.0F, 0.0F, f3 * 15F);
                hierMesh().chunkSetAngles("LWire12_d0", 0.0F, 0.0F, f3 * 15F);
                hierMesh().chunkSetAngles("LWire2_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire3_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire4_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire5_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire6_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire7_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire8_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire9_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire10_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire11_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire13_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire14_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire15_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire16_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire17_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire18_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire19_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire20_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire21_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("LWire22_d0", 0.0F, 0.0F, 0.0F);
            } else
            {
                float f4 = 1.0F;
                hierMesh().chunkSetAngles("LWire1_d0", 0.0F, 6.5F * f3 + f3 * (-20F * f1) * wireRandomizer3, f3 * (60F * f4));
                hierMesh().chunkSetAngles("LWire12_d0", 0.0F, 6.5F * f3 + f3 * (20F * f1) * wireRandomizer4, f3 * (70F * f4));
                float f6 = f3 * -5F;
                float f8 = f3 * -10F;
                float f10 = f3 * -15F;
                float f12 = f3 * (5F * f1) * wireRandomizer3;
                float f14 = f3 * (10F * f1) * wireRandomizer3;
                float f16 = f3 * (-5F * f1) * wireRandomizer4;
                hierMesh().chunkSetAngles("LWire2_d0", 0.0F, f14, f6);
                hierMesh().chunkSetAngles("LWire3_d0", 0.0F, f12, f8);
                hierMesh().chunkSetAngles("LWire4_d0", 0.0F, f14, f8);
                hierMesh().chunkSetAngles("LWire5_d0", 0.0F, f12, f8);
                hierMesh().chunkSetAngles("LWire6_d0", 0.0F, f14, f10);
                hierMesh().chunkSetAngles("LWire7_d0", 0.0F, f12, f8);
                hierMesh().chunkSetAngles("LWire8_d0", 0.0F, f14, f10);
                hierMesh().chunkSetAngles("LWire9_d0", 0.0F, f12, f6);
                hierMesh().chunkSetAngles("LWire10_d0", 0.0F, f14, f6);
                hierMesh().chunkSetAngles("LWire11_d0", 0.0F, f12, f6);
                hierMesh().chunkSetAngles("LWire13_d0", 0.0F, f16, f8);
                hierMesh().chunkSetAngles("LWire14_d0", 0.0F, f16, f10);
                hierMesh().chunkSetAngles("LWire15_d0", 0.0F, f16, f8);
                hierMesh().chunkSetAngles("LWire16_d0", 0.0F, f16, f10);
                hierMesh().chunkSetAngles("LWire17_d0", 0.0F, 0.0F, f8);
                hierMesh().chunkSetAngles("LWire18_d0", 0.0F, f16, f8);
                hierMesh().chunkSetAngles("LWire19_d0", 0.0F, f16, f8);
                hierMesh().chunkSetAngles("LWire20_d0", 0.0F, f16, f8);
                hierMesh().chunkSetAngles("LWire21_d0", 0.0F, f16, f8);
                hierMesh().chunkSetAngles("LWire22_d0", 0.0F, f16, f8);
            }
            f3 = skiAngleR / 20F;
            if(skiAngleR > 0.0F)
            {
                hierMesh().chunkSetAngles("RSkiFrontDownWire1_d0", 0.0F, f3 * 4F, f3 * 12.4F);
                hierMesh().chunkSetAngles("RSkiFrontDownWire2_d0", 0.0F, f3 * 4F, f3 * 12.4F);
            } else
            {
                hierMesh().chunkSetAngles("RSkiFrontDownWire1_d0", 0.0F, f3 * 8F, f3 * 12.4F);
                hierMesh().chunkSetAngles("RSkiFrontDownWire2_d0", 0.0F, f3 * 8F, f3 * 12.4F);
            }
            Aircraft.ypr[0] = 0.0F;
            Aircraft.ypr[1] = 0.0F;
            Aircraft.ypr[2] = 0.0F;
            Aircraft.ypr[0] = 0.0F;
            Aircraft.xyz[0] = -0.16F * f3 + suspR;
            Aircraft.xyz[1] = 0.0F;
            Aircraft.xyz[2] = 0.0F;
            hierMesh().chunkSetLocate("RSkiFrontUpWire_d0", Aircraft.xyz, Aircraft.ypr);
            if(skiAngleR < 0.0F)
            {
                hierMesh().chunkSetAngles("RWire1_d0", 0.0F, 0.0F, f3 * 15F);
                hierMesh().chunkSetAngles("RWire12_d0", 0.0F, 0.0F, f3 * 15F);
                hierMesh().chunkSetAngles("RWire2_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire3_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire4_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire5_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire6_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire7_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire8_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire9_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire10_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire11_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire13_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire14_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire15_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire16_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire17_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire18_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire19_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire20_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire21_d0", 0.0F, 0.0F, 0.0F);
                hierMesh().chunkSetAngles("RWire22_d0", 0.0F, 0.0F, 0.0F);
            } else
            {
                float f5 = 1.0F;
                hierMesh().chunkSetAngles("RWire1_d0", 0.0F, -6.5F * f3 + f3 * (-20F * f1) * wireRandomizer1, f3 * (60F * f5));
                hierMesh().chunkSetAngles("RWire12_d0", 0.0F, -6.5F * f3 + f3 * (20F * f1) * wireRandomizer2, f3 * (70F * f5));
                float f7 = f3 * -5F;
                float f9 = f3 * -10F;
                float f11 = f3 * -15F;
                float f13 = f3 * (5F * f1) * wireRandomizer1;
                float f15 = f3 * (10F * f1) * wireRandomizer1;
                float f17 = f3 * (-5F * f1) * wireRandomizer2;
                hierMesh().chunkSetAngles("RWire2_d0", 0.0F, f15, f7);
                hierMesh().chunkSetAngles("RWire3_d0", 0.0F, f13, f9);
                hierMesh().chunkSetAngles("RWire4_d0", 0.0F, f15, f9);
                hierMesh().chunkSetAngles("RWire5_d0", 0.0F, f13, f11);
                hierMesh().chunkSetAngles("RWire6_d0", 0.0F, f15, f9);
                hierMesh().chunkSetAngles("RWire7_d0", 0.0F, f13, f9);
                hierMesh().chunkSetAngles("RWire8_d0", 0.0F, f15, f9);
                hierMesh().chunkSetAngles("RWire9_d0", 0.0F, f13, f7);
                hierMesh().chunkSetAngles("RWire10_d0", 0.0F, f15, f7);
                hierMesh().chunkSetAngles("RWire11_d0", 0.0F, f13, f7);
                hierMesh().chunkSetAngles("RWire13_d0", 0.0F, f17, f9);
                hierMesh().chunkSetAngles("RWire14_d0", 0.0F, f17, f9);
                hierMesh().chunkSetAngles("RWire15_d0", 0.0F, f17, f11);
                hierMesh().chunkSetAngles("RWire16_d0", 0.0F, f17, f9);
                hierMesh().chunkSetAngles("RWire17_d0", 0.0F, 0.0F, f11);
                hierMesh().chunkSetAngles("RWire18_d0", 0.0F, f17, f9);
                hierMesh().chunkSetAngles("RWire19_d0", 0.0F, f17, f9);
                hierMesh().chunkSetAngles("RWire20_d0", 0.0F, f17, f9);
                hierMesh().chunkSetAngles("RWire21_d0", 0.0F, f17, f9);
                hierMesh().chunkSetAngles("RWire22_d0", 0.0F, f17, f9);
            }
        }
        super.moveFan(f);
    }

    private float skiAngleL;
    private float skiAngleR;
    private float spring;
    private float wireRandomizer1;
    private float wireRandomizer2;
    private float wireRandomizer3;
    private float wireRandomizer4;

    static 
    {
        Class class1 = I_15BIS_SKIS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-15bis");
        Property.set(class1, "meshName", "3DO/Plane/I-15bis/hierSkis.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar08());
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/I-15bis.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_15Bis.class });
        Property.set(class1, "LOSElevation", 0.84305F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 
            2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 
            9, 9, 9, 9
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", 
            "_ExternalRock07", "_ExternalRock08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", 
            "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08"
        });
    }
}
