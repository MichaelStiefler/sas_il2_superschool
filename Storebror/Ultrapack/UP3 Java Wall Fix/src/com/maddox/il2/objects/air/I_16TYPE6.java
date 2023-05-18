package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;

public class I_16TYPE6 extends I_16
    implements TypeTNBFighter
{

    public I_16TYPE6()
    {
        flaperonAngle = 0.0F;
        aileronsAngle = 0.0F;
        hasTubeSight = false;
        pit = null;
        sideDoorOpened = false;
        removeSpinnerHub = false;
    }

    public void moveGear(float f)
    {
        super.moveGear(f);
        if(f > 0.5F)
        {
            hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.5F, 1.0F, 14.5F, -8F), Aircraft.cvt(f, 0.5F, 1.0F, 44F, 62.5F), 0.0F);
            hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.5F, 1.0F, -14.5F, 8F), Aircraft.cvt(f, 0.5F, 1.0F, -44F, -62.5F), 0.0F);
        } else
        if(f > 0.25F)
        {
            hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.25F, 0.5F, 33F, 14.5F), Aircraft.cvt(f, 0.25F, 0.5F, 38F, 44F), 0.0F);
            hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.25F, 0.5F, -33F, -14.5F), Aircraft.cvt(f, 0.25F, 0.5F, -38F, -44F), 0.0F);
        } else
        {
            hierMesh().chunkSetAngles("GearWireR1_D0", Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, 33F), Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, 38F), 0.0F);
            hierMesh().chunkSetAngles("GearWireL1_D0", Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -33F), Aircraft.cvt(f, 0.0F, 0.25F, 0.0F, -38F), 0.0F);
        }
        if(f > 0.5F)
        {
            hierMesh().chunkVisible("GearWireR2_D0", true);
            hierMesh().chunkVisible("GearWireL2_D0", true);
        } else
        {
            hierMesh().chunkVisible("GearWireR2_D0", false);
            hierMesh().chunkVisible("GearWireL2_D0", false);
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xxtank1") && getEnergyPastArmor(0.1F, shot) > 0.0F && World.Rnd().nextFloat() < 0.3F)
        {
            if(FM.AS.astateTankStates[0] == 0)
            {
                Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
                FM.AS.hitTank(shot.initiator, 0, 2);
            }
            if(shot.powerType == 3 && World.Rnd().nextFloat() < 0.75F)
            {
                FM.AS.hitTank(shot.initiator, 0, 2);
                Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
            }
        } else
        {
            super.hitBone(s, shot, point3d);
        }
    }

    protected void moveFan(float f)
    {
        if(Config.isUSE_RENDER())
        {
            super.moveFan(f);
            float f1 = FM.CT.getAileron();
            float f2 = FM.CT.getElevator();
            hierMesh().chunkSetAngles("Stick_D0", 0.0F, 12F * f1, cvt(f2, -1F, 1.0F, -12F, 18F));
            hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - (cvt(f2, -1F, 0.0F, -36F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 32F)));
            hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f2, -1F, 0.0F, -62F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 44F));
            if(!removeSpinnerHub)
            {
                boolean flag = hierMesh().isChunkVisible("PropRot1_D0");
                hierMesh().chunkVisible("PropHubRot1_D0", flag);
                hierMesh().chunkVisible("PropHub1_D0", !flag);
            }
        }
    }

    protected void moveAileron(float f)
    {
        aileronsAngle = f;
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f - flaperonAngle, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f + flaperonAngle, 0.0F);
    }

    protected void moveFlap(float f)
    {
        flaperonAngle = f * 17F;
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * aileronsAngle - flaperonAngle, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * aileronsAngle + flaperonAngle, 0.0F);
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
            hierMesh().chunkVisible("Head1_D1", true);
            hierMesh().chunkVisible("pilotarm2_d0", false);
            hierMesh().chunkVisible("pilotarm1_d0", false);
            break;
        }
    }

    public void doRemoveBodyFromPlane(int i)
    {
        super.doRemoveBodyFromPlane(i);
        hierMesh().chunkVisible("pilotarm2_d0", false);
        hierMesh().chunkVisible("pilotarm1_d0", false);
    }

    public boolean hasTubeSight()
    {
        return hasTubeSight;
    }

    public void missionStarting()
    {
        super.missionStarting();
        customization();
        hierMesh().chunkVisible("pilotarm2_d0", true);
        hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void prepareCamouflage()
    {
        super.prepareCamouflage();
        hierMesh().chunkVisible("pilotarm2_d0", true);
        hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        hierMesh().chunkVisible("GearWireR1_D0", true);
        hierMesh().chunkVisible("GearWireL1_D0", true);
    }

    private void customization()
    {
        if(!Mission.isSingle())
            return;
        boolean flag = false;
        boolean flag1 = false;
        int i = hierMesh().chunkFindCheck("CF_D0");
        int j = hierMesh().materialFindInChunk("Gloss1D0o", i);
        Mat mat = hierMesh().material(j);
        String s = mat.Name();
        if(s.startsWith("PaintSchemes/Cache"))
        {
            try
            {
                s = s.substring(19);
                s = s.substring(0, s.indexOf("/"));
                String s1 = Main.cur().netFileServerSkin.primaryPath();
                File file = new File(HomePath.toFileSystemName(s1 + "/I-16type6/Customization.ini", 0));
                BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
                Object obj = null;
                boolean flag2 = false;
                boolean flag3 = false;
                boolean flag4 = false;
                boolean flag5 = false;
                boolean flag6 = false;
                boolean flag7 = false;
                do
                {
                    String s2;
                    if((s2 = bufferedreader.readLine()) == null)
                        break;
                    if(s2.equals("[TubeSight]"))
                    {
                        flag2 = true;
                        flag3 = false;
                        flag4 = false;
                        flag5 = false;
                        flag6 = false;
                        flag7 = false;
                    } else
                    if(s2.equals("[RadioWires]"))
                    {
                        flag2 = false;
                        flag3 = true;
                        flag4 = false;
                        flag5 = false;
                        flag6 = false;
                        flag7 = false;
                    } else
                    if(s2.equals("[FullWheelCovers]"))
                    {
                        flag2 = false;
                        flag3 = false;
                        flag4 = true;
                        flag5 = false;
                        flag6 = false;
                        flag7 = false;
                    } else
                    if(s2.equals("[RemoveSpinner]"))
                    {
                        flag2 = false;
                        flag3 = false;
                        flag4 = false;
                        flag5 = false;
                        flag6 = false;
                        flag7 = true;
                    } else
                    if(s2.equals("[KeepSpinner]"))
                    {
                        flag2 = false;
                        flag3 = false;
                        flag4 = false;
                        flag5 = false;
                        flag6 = true;
                        flag7 = false;
                    } else
                    if(s2.equals("[CanopyRails]"))
                    {
                        flag2 = false;
                        flag3 = false;
                        flag4 = false;
                        flag5 = true;
                        flag6 = false;
                        flag7 = false;
                    } else
                    if(s2.equals(s))
                    {
                        if(flag2)
                            hasTubeSight = true;
                        if(flag3)
                        {
                            hierMesh().chunkVisible("RadioWire1_d0", true);
                            hierMesh().chunkVisible("RadioWire2_d0", true);
                        }
                        if(flag4)
                        {
                            hierMesh().chunkVisible("GearR3_D0", true);
                            hierMesh().chunkVisible("GearL3_D0", true);
                        }
                        if(flag5)
                        {
                            hierMesh().chunkVisible("Rails_d0", true);
                            hierMesh().chunkVisible("Blister2Rail_D0", true);
                            hierMesh().chunkVisible("Blister2_D0", false);
                            hierMesh().chunkVisible("T6Rail_D0", false);
                        }
                        if(flag6)
                            flag1 = true;
                        if(flag7)
                            flag = true;
                    }
                } while(true);
                bufferedreader.close();
            }
            catch(Exception exception)
            {
                System.out.println(exception);
            }
        } else
        {
            if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)
                hasTubeSight = true;
            if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)
            {
                hierMesh().chunkVisible("GearR3_D0", true);
                hierMesh().chunkVisible("GearL3_D0", true);
            }
        }
        if(pit != null)
            pit.setTubeSight(hasTubeSight);
        hierMesh().chunkVisible("Sight_D0", !hasTubeSight);
        hierMesh().chunkVisible("TubeSight_D0", hasTubeSight);
        if(flag || !flag1 && (FM.CT.Weapons[2] != null || FM.CT.Weapons[3] != null))
        {
            removeSpinnerHub = true;
            hierMesh().chunkVisible("PropHubRot1_D0", false);
            hierMesh().chunkVisible("PropHub1_D0", false);
        }
    }

    public void registerPit(CockpitI_16TYPE6 cockpiti_16type6)
    {
        pit = cockpiti_16type6;
        if(cockpiti_16type6 != null)
            cockpiti_16type6.setTubeSight(hasTubeSight);
    }

    public void moveCockpitDoor(float f)
    {
        hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
    }

    public void hitDaSilk()
    {
        super.hitDaSilk();
        if(!sideDoorOpened && FM.AS.bIsAboutToBailout && !FM.AS.isPilotDead(0))
        {
            sideDoorOpened = true;
            FM.CT.bHasCockpitDoorControl = true;
            FM.CT.forceCockpitDoor(0.0F);
            FM.AS.setCockpitDoor(this, 1);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 11:
            hierMesh().chunkVisible("RadioWire1_d0", false);
            hierMesh().chunkVisible("RadioWire2_d0", false);
            break;

        case 36:
            hierMesh().chunkVisible("RadioWire2_d0", false);
            break;

        case 38:
            hierMesh().chunkVisible("RadioWire2_d0", false);
            break;

        case 19:
            FM.Gears.hitCentreGear();
            hierMesh().chunkVisible("RadioWire1_d0", false);
            hierMesh().chunkVisible("RadioWire2_d0", false);
            break;
        }
        return super.cutFM(i, j, actor);
    }

    private float flaperonAngle;
    private float aileronsAngle;
    private boolean hasTubeSight;
    private CockpitI_16TYPE6 pit;
    private boolean sideDoorOpened;
    private boolean removeSpinnerHub;

    static 
    {
        Class class1 = I_16TYPE6.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type6(multi)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar07());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type6/hier.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar07());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type6.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitI_16TYPE6.class
        });
        Property.set(class1, "LOSElevation", 0.82595F);
        weaponTriggersRegister(class1, new int[] {
            0, 0, 2, 2, 2, 2, 2, 2, 3, 3, 
            9, 9, 9, 9, 9, 9, 9, 9
        });
        weaponHooksRegister(class1, new String[] {
            "_MGUN01", "_MGUN02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb01", "_ExternalBomb02", 
            "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08"
        });
    }
}
