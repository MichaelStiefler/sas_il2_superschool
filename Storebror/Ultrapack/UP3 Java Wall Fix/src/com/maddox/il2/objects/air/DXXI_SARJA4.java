package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;

public class DXXI_SARJA4 extends DXXI
    implements TypeScout
{

    public DXXI_SARJA4()
    {
        skiAngleL = 0.0F;
        skiAngleR = 0.0F;
        spring = 0.15F;
    }

    public void missionStarting()
    {
        super.missionStarting();
        customization();
    }

    private void customization()
    {
        if(!Mission.isSingle())
            return;
        int i = hierMesh().chunkFindCheck("cf_D0");
        int j = hierMesh().materialFindInChunk("Gloss1D0o", i);
        Mat mat = hierMesh().material(j);
        String s = mat.Name();
        if(s.startsWith("PaintSchemes/Cache"))
            try
            {
                s = s.substring(19);
                s = s.substring(0, s.indexOf("/"));
                String s1 = Main.cur().netFileServerSkin.primaryPath();
                File file = new File(HomePath.toFileSystemName(s1 + "/DXXI_SARJA4/Customization.ini", 0));
                BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
                Object obj = null;
                boolean flag = false;
                boolean flag1 = false;
                do
                {
                    String s2;
                    if((s2 = bufferedreader.readLine()) == null)
                        break;
                    if(s2.equals("[NoWheelSpats]"))
                    {
                        flag = true;
                        flag1 = false;
                    } else
                    if(s2.equals("[NoWingSlots]"))
                    {
                        flag = false;
                        flag1 = true;
                    } else
                    if(s2.equals(s))
                    {
                        if(flag && World.cur().camouflage != 1)
                            removeWheelSpats();
                        if(flag1)
                        {
                            hierMesh().chunkVisible("SlotCoverLMid", true);
                            hierMesh().chunkVisible("SlotCoverRMid", true);
                            hierMesh().chunkVisible("SlotCoverLOut", true);
                            hierMesh().chunkVisible("SlotCoverROut", true);
                        }
                    }
                } while(true);
                bufferedreader.close();
            }
            catch(Exception exception)
            {
                System.out.println(exception);
            }
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        hasSelfSealingTank = true;
        canopyMaxAngle = 0.45F;
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
        } else
        if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.01F)
            removeWheelSpats();
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
            super.moveFan(-f);
            float f1 = FM.CT.getAileron();
            float f2 = FM.CT.getElevator();
            hierMesh().chunkSetAngles("Stick_D0", 0.0F, 9F * f1, cvt(f2, -1F, 1.0F, -8F, 9.5F));
            hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - cvt(f2, -1F, 1.0F, -37F, 35F));
            hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f2, -1F, 0.0F, -61F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 43F));
            float f3 = FM.CT.getRadiator() * 30F;
            hierMesh().chunkSetAngles("cowlf1_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf2_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf3_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf4_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf5_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf6_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf7_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf8_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf9_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf10_d0", 0.0F, f3, 0.0F);
            hierMesh().chunkSetAngles("cowlf0_d0", 0.0F, f3, 0.0F);
            if(World.cur().camouflage == 1)
            {
                float f4 = Aircraft.cvt(FM.getSpeed(), 30F, 100F, 1.0F, 0.0F);
                float f5 = Aircraft.cvt(FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
                if(FM.Gears.gWheelSinking[0] > 0.0F)
                {
                    skiAngleL = 0.5F * skiAngleL + 0.5F * FM.Or.getTangage();
                    if(skiAngleL > 20F)
                        skiAngleL = skiAngleL - spring;
                    hierMesh().chunkSetAngles("GearL21_D0", World.Rnd().nextFloat(-f5, f5), World.Rnd().nextFloat(-f5 * 2.0F, f5 * 2.0F) + skiAngleL, World.Rnd().nextFloat(f5, f5));
                    if(FM.Gears.gWheelSinking[1] == 0.0F && FM.Or.getRoll() < 365F && FM.Or.getRoll() > 355F)
                    {
                        skiAngleR = skiAngleL;
                        hierMesh().chunkSetAngles("GearR21_D0", World.Rnd().nextFloat(-f5, f5), World.Rnd().nextFloat(-f5 * 2.0F, f5 * 2.0F) + skiAngleR, World.Rnd().nextFloat(f5, f5));
                    }
                } else
                {
                    if(skiAngleL > f4 * -10F + 0.01D)
                        skiAngleL = skiAngleL - spring;
                    else
                    if(skiAngleL < f4 * -10F - 0.01D)
                        skiAngleL = skiAngleL + spring;
                    hierMesh().chunkSetAngles("GearL21_D0", 0.0F, skiAngleL, 0.0F);
                }
                if(FM.Gears.gWheelSinking[1] > 0.0F)
                {
                    skiAngleR = 0.5F * skiAngleR + 0.5F * FM.Or.getTangage();
                    if(skiAngleR > 20F)
                        skiAngleR = skiAngleR - spring;
                    hierMesh().chunkSetAngles("GearR21_D0", World.Rnd().nextFloat(-f5, f5), World.Rnd().nextFloat(-f5 * 2.0F, f5 * 2.0F) + skiAngleR, World.Rnd().nextFloat(f5, f5));
                } else
                {
                    if(skiAngleR > f4 * -10F + 0.01D)
                        skiAngleR = skiAngleR - spring;
                    else
                    if(skiAngleR < f4 * -10F - 0.01D)
                        skiAngleR = skiAngleR + spring;
                    hierMesh().chunkSetAngles("GearR21_D0", 0.0F, skiAngleR, 0.0F);
                }
                hierMesh().chunkSetAngles("GearC11_D0", 0.0F, (skiAngleL + skiAngleR) / 2.0F, 0.0F);
            }
        }
    }

    private void removeWheelSpats()
    {
        hierMesh().chunkVisible("GearR22_D0", false);
        hierMesh().chunkVisible("GearL22_D0", false);
        hierMesh().chunkVisible("GearR22_D2", true);
        hierMesh().chunkVisible("GearL22_D2", true);
        hierMesh().chunkVisible("gearl31_d0", true);
        hierMesh().chunkVisible("gearl32_d0", true);
        hierMesh().chunkVisible("gearr31_d0", true);
        hierMesh().chunkVisible("gearr32_d0", true);
    }

    protected void nextDMGLevel(String s, int i, Actor actor)
    {
        super.nextDMGLevel(s, i, actor);
        if(FM.isPlayers())
            bChangedPit = true;
        if(World.cur().camouflage != 1)
        {
            if(hierMesh().isChunkVisible("GearR22_D2") && !hierMesh().isChunkVisible("gearr31_d0"))
            {
                hierMesh().chunkVisible("gearr31_d0", true);
                hierMesh().chunkVisible("gearr32_d0", true);
                Wreckage wreckage = new Wreckage(this, hierMesh().chunkFind("GearR22_D1"));
                wreckage.collide(true);
                Vector3d vector3d = new Vector3d();
                vector3d.set(FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
            if(hierMesh().isChunkVisible("GearL22_D2") && !hierMesh().isChunkVisible("gearl31_d0"))
            {
                hierMesh().chunkVisible("gearl31_d0", true);
                hierMesh().chunkVisible("gearl32_d0", true);
                Wreckage wreckage1 = new Wreckage(this, hierMesh().chunkFind("GearL22_D1"));
                wreckage1.collide(true);
                Vector3d vector3d1 = new Vector3d();
                vector3d1.set(FM.Vwld);
                wreckage1.setSpeed(vector3d1);
            }
        } else
        {
            if((hierMesh().isChunkVisible("GearR11_D1") || hierMesh().isChunkVisible("GearR21_D2")) && !hierMesh().isChunkVisible("gearr31_d0"))
            {
                hierMesh().chunkVisible("GearR11_D1", true);
                hierMesh().chunkVisible("GearR11_D0", false);
                hierMesh().chunkVisible("gearr31_d0", true);
                hierMesh().chunkVisible("gearr32_d0", true);
                Wreckage wreckage2 = new Wreckage(this, hierMesh().chunkFind("GearR11_D0"));
                wreckage2.collide(true);
                Vector3d vector3d2 = new Vector3d();
                vector3d2.set(FM.Vwld);
                wreckage2.setSpeed(vector3d2);
            }
            if((hierMesh().isChunkVisible("GearL11_D1") || hierMesh().isChunkVisible("GearL21_D2")) && !hierMesh().isChunkVisible("gearl31_d0"))
            {
                hierMesh().chunkVisible("GearL11_D1", true);
                hierMesh().chunkVisible("GearL11_D0", false);
                hierMesh().chunkVisible("gearl31_d0", true);
                hierMesh().chunkVisible("gearl32_d0", true);
                Wreckage wreckage3 = new Wreckage(this, hierMesh().chunkFind("GearL11_D0"));
                wreckage3.collide(true);
                Vector3d vector3d3 = new Vector3d();
                vector3d3.set(FM.Vwld);
                wreckage3.setSpeed(vector3d3);
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
        Class class1 = DXXI_SARJA4.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D.XXI");
        Property.set(class1, "meshName_fi", "3DO/Plane/DXXI_SARJA4/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/DXXI_SARJA4/hierMulti.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFMPar00DXXI());
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00DXXI());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/FokkerS4.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitDXXI_SARJA4.class
        });
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
