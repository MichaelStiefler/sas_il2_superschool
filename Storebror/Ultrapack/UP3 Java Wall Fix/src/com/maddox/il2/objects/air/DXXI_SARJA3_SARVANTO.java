package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;

public class DXXI_SARJA3_SARVANTO extends DXXI
    implements TypeAcePlane
{

    public DXXI_SARJA3_SARVANTO()
    {
        skiAngleL = 0.0F;
        skiAngleR = 0.0F;
        spring = 0.15F;
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        FM.Skill = 3;
        JormaSarvanto();
        if(Config.isUSE_RENDER() && World.cur().camouflage == 1)
        {
            hasSkis = true;
            hierMesh().chunkVisible("GearL1_D0", false);
            hierMesh().chunkVisible("GearL22_D0", false);
            hierMesh().chunkVisible("GearR1_D0", false);
            hierMesh().chunkVisible("GearR22_D0", false);
            hierMesh().chunkVisible("GearC1_D0", false);
            hierMesh().chunkVisible("GearL31_D0", false);
            hierMesh().chunkVisible("GearL32_D0", false);
            hierMesh().chunkVisible("GearR31_D0", false);
            hierMesh().chunkVisible("GearR32_D0", false);
            hierMesh().chunkVisible("GearC11_D0", true);
            hierMesh().chunkVisible("GearL11_D0", true);
            hierMesh().chunkVisible("GearL21_D0", true);
            hierMesh().chunkVisible("GearR11_D0", true);
            hierMesh().chunkVisible("GearR21_D0", true);
            FM.CT.bHasBrakeControl = false;
        }
    }

    private void JormaSarvanto()
    {
        for(int i = 0; i < FM.CT.Weapons.length; i++)
        {
            com.maddox.il2.ai.BulletEmitter abulletemitter[] = FM.CT.Weapons[i];
            if(abulletemitter == null)
                continue;
            for(int j = 0; j < abulletemitter.length; j++)
            {
                com.maddox.il2.ai.BulletEmitter bulletemitter = abulletemitter[j];
                if(!(bulletemitter instanceof Gun))
                    continue;
                GunProperties gunproperties = ((Gun)bulletemitter).prop;
                BulletProperties abulletproperties[] = gunproperties.bullet;
                if(abulletproperties == null)
                    continue;
                for(int k = 0; k < abulletproperties.length; k++)
                {
                    abulletproperties[k].powerType = 3;
                    abulletproperties[k].massa = 0.02F;
                    abulletproperties[k].kalibr = 4.442131E-005F;
                    abulletproperties[k].speed = 835F;
                    if(abulletproperties[k].power != 0.0F)
                        abulletproperties[k].power = 0.002F;
                }

            }

        }

    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        if(World.cur().camouflage == 1 && World.Rnd().nextFloat() > 0.1F)
        {
            hiermesh.chunkVisible("GearL1_D0", false);
            hiermesh.chunkVisible("GearL22_D0", false);
            hiermesh.chunkVisible("GearR1_D0", false);
            hiermesh.chunkVisible("GearR22_D0", false);
            hiermesh.chunkVisible("GearC1_D0", false);
            hiermesh.chunkVisible("GearL31_D0", false);
            hiermesh.chunkVisible("GearL32_D0", false);
            hiermesh.chunkVisible("GearR31_D0", false);
            hiermesh.chunkVisible("GearR32_D0", false);
            hiermesh.chunkVisible("GearC11_D0", true);
            hiermesh.chunkVisible("GearL11_D0", true);
            hiermesh.chunkVisible("GearL21_D0", true);
            hiermesh.chunkVisible("GearR11_D0", true);
            hiermesh.chunkVisible("GearR21_D0", true);
            hiermesh.chunkSetAngles("GearL21_D0", 0.0F, 12F, 0.0F);
            hiermesh.chunkSetAngles("GearR21_D0", 0.0F, 12F, 0.0F);
            hiermesh.chunkSetAngles("GearC11_D0", 0.0F, 12F, 0.0F);
        }
    }

    protected void moveFan(float f)
    {
        if(Config.isUSE_RENDER())
        {
            super.moveFan(f);
            float f1 = FM.CT.getAileron();
            float f2 = FM.CT.getElevator();
            hierMesh().chunkSetAngles("Stick_D0", 0.0F, 9F * f1, cvt(f2, -1F, 1.0F, -8F, 9.5F));
            hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - cvt(f2, -1F, 1.0F, -37F, 35F));
            hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f2, -1F, 0.0F, -61F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 43F));
            if(World.cur().camouflage == 1)
            {
                float f3 = Aircraft.cvt(FM.getSpeed(), 30F, 100F, 1.0F, 0.0F);
                float f4 = Aircraft.cvt(FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
                if(FM.Gears.gWheelSinking[0] > 0.0F)
                {
                    skiAngleL = 0.5F * skiAngleL + 0.5F * FM.Or.getTangage();
                    if(skiAngleL > 20F)
                        skiAngleL = skiAngleL - spring;
                    hierMesh().chunkSetAngles("GearL21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + skiAngleL, World.Rnd().nextFloat(f4, f4));
                    if(FM.Gears.gWheelSinking[1] == 0.0F && FM.Or.getRoll() < 365F && FM.Or.getRoll() > 355F)
                    {
                        skiAngleR = skiAngleL;
                        hierMesh().chunkSetAngles("GearR21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + skiAngleR, World.Rnd().nextFloat(f4, f4));
                    }
                } else
                {
                    if(skiAngleL > f3 * -10F + 0.01D)
                        skiAngleL = skiAngleL - spring;
                    else
                    if(skiAngleL < f3 * -10F - 0.01D)
                        skiAngleL = skiAngleL + spring;
                    hierMesh().chunkSetAngles("GearL21_D0", 0.0F, skiAngleL, 0.0F);
                }
                if(FM.Gears.gWheelSinking[1] > 0.0F)
                {
                    skiAngleR = 0.5F * skiAngleR + 0.5F * FM.Or.getTangage();
                    if(skiAngleR > 20F)
                        skiAngleR = skiAngleR - spring;
                    hierMesh().chunkSetAngles("GearR21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + skiAngleR, World.Rnd().nextFloat(f4, f4));
                } else
                {
                    if(skiAngleR > f3 * -10F + 0.01D)
                        skiAngleR = skiAngleR - spring;
                    else
                    if(skiAngleR < f3 * -10F - 0.01D)
                        skiAngleR = skiAngleR + spring;
                    hierMesh().chunkSetAngles("GearR21_D0", 0.0F, skiAngleR, 0.0F);
                }
                hierMesh().chunkSetAngles("GearC11_D0", 0.0F, (skiAngleL + skiAngleR) / 2.0F, 0.0F);
            }
        }
    }

    public void sfxWheels()
    {
        if(!hasSkis)
            super.sfxWheels();
    }

    private float skiAngleL;
    private float skiAngleR;
    private float spring;

    static 
    {
        Class class1 = DXXI_SARJA3_SARVANTO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D.XXI");
        Property.set(class1, "meshName", "3DO/Plane/DXXI_SARJA3_EARLY(Sarvanto)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00DXXI());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1940F);
        Property.set(class1, "FlightModel", "FlightModels/FokkerS3Early.fmd");
        Property.set(class1, "LOSElevation", 0.8472F);
        Property.set(class1, "originCountry", PaintScheme.countryFinland);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            0, 0, 0, 0
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04"
        });
    }
}
