package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class F4U extends Scheme1
    implements TypeFighter, TypeDiveBomber
{

    public F4U()
    {
        arrestorPos = 0.0F;
        arrestorVel = 0.0F;
        prevGear = 0.0F;
        prevGear2 = 0.0F;
        prevWing = 1.0F;
        cGearPos = 0.0F;
        cGear = 0.0F;
        bNeedSetup = true;
        flapps = 0.0F;
    }

    public boolean typeDiveBomberToggleAutomation()
    {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset()
    {
    }

    public void typeDiveBomberAdjAltitudePlus()
    {
    }

    public void typeDiveBomberAdjAltitudeMinus()
    {
    }

    public void typeDiveBomberAdjVelocityReset()
    {
    }

    public void typeDiveBomberAdjVelocityPlus()
    {
    }

    public void typeDiveBomberAdjVelocityMinus()
    {
    }

    public void typeDiveBomberAdjDiveAngleReset()
    {
    }

    public void typeDiveBomberAdjDiveAnglePlus()
    {
    }

    public void typeDiveBomberAdjDiveAngleMinus()
    {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
        throws IOException
    {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput)
        throws IOException
    {
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 30F * f, 0.0F);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 30F * f, 0.0F);
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
    }

    public static void moveGear(HierMesh hiermesh, float f, FlightModel flightmodel)
    {
        float f6 = 10F * f;
        if(flightmodel != null)
            f = 10F * Math.max(f, flightmodel.CT.getAirBrake());
        else
            f = f6;
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);
        if(bGearExtending)
        {
            hiermesh.chunkSetAngles("GearL9_D0", 0.0F, cvt(f, 0.0F, 3F, 0.0F, 95F), 0.0F);
            hiermesh.chunkSetAngles("GearL10_D0", 0.0F, cvt(f, 0.0F, 3F, 0.0F, 60F), 0.0F);
            hiermesh.chunkSetAngles("GearR9_D0", 0.0F, cvt(f, 0.0F, 3F, 0.0F, 95F), 0.0F);
            hiermesh.chunkSetAngles("GearR10_D0", 0.0F, cvt(f, 0.0F, 3F, 0.0F, 60F), 0.0F);
            if(flightmodel == null)
            {
                float f1 = cvt(f6, 0.0F, 1.0F, 0.0F, 0.7071068F);
                f1 = 2.0F * f1 * f1;
                hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 41F * f1, 0.0F);
                hiermesh.chunkSetAngles("Hook1_D0", 0.0F, cvt(f1, 0.0F, 1.0F, 0.0F, -64.5F), 0.0F);
                f1 = cvt(f6, 0.0F, 0.25F, 0.0F, 1.0F);
                hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 140F * f1, 0.0F);
                hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 140F * f1, 0.0F);
            }
            float f2;
            float f5;
            if(f < 4F)
            {
                f2 = f5 = cvt(f, 3F, 4F, 0.0F, 0.4F);
            } else
            {
                f2 = cvt(f, 4F, 8F, 0.75F, 2.0F);
                f2 = (float)Math.sqrt(f2);
                f2 = cvt(f2, (float)Math.sqrt(0.75D), (float)Math.sqrt(2D), 0.4F, 1.0F);
                f5 = cvt(f, 4F, 8.5F, 0.75F, 2.0F);
                f5 = (float)Math.sqrt(f5);
                f5 = cvt(f5, (float)Math.sqrt(0.75D), (float)Math.sqrt(2D), 0.4F, 1.0F);
            }
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 81F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 84F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 83F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL33_D0", 0.0F, -104F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 40F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -168F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -90F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 81F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 84F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 83F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR33_D0", 0.0F, -104F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 40F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -168F * f5, 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -90F * f5, 0.0F);
        } else
        {
            if(flightmodel == null)
            {
                float f3 = cvt(f6, 8.5F, 10F, 0.0F, 1.0F);
                hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 41F * f3, 0.0F);
                f3 = cvt(f6, 8.5F, 8.75F, 0.0F, 1.0F);
                hiermesh.chunkSetAngles("GearC4_D0", 0.0F, 140F * f3, 0.0F);
                hiermesh.chunkSetAngles("GearC5_D0", 0.0F, 140F * f3, 0.0F);
                hiermesh.chunkSetAngles("Hook1_D0", 0.0F, cvt(f6, 8.5F, 10F, 0.0F, -64.5F), 0.0F);
            }
            float f4;
            if(f > 7.5F)
                f4 = cvt(f, 7.5F, 8.5F, 0.9F, 1.0F);
            else
                f4 = cvt(f, 3F, 7.5F, 0.0F, 0.9F);
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 81F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 84F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 83F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL33_D0", 0.0F, -104F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 40F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -168F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, -90F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 81F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 84F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 83F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR33_D0", 0.0F, -104F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 40F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -168F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, -90F * f4, 0.0F);
            hiermesh.chunkSetAngles("GearL9_D0", 0.0F, cvt(f, 1.5F, 3.7F, 0.0F, 95F), 0.0F);
            hiermesh.chunkSetAngles("GearL10_D0", 0.0F, cvt(f, 1.5F, 3.7F, 0.0F, 60F), 0.0F);
            hiermesh.chunkSetAngles("GearR9_D0", 0.0F, cvt(f, 0.01F, 3.7F, 0.0F, 95F), 0.0F);
            hiermesh.chunkSetAngles("GearR10_D0", 0.0F, cvt(f, 0.01F, 3.7F, 0.0F, 60F), 0.0F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        moveGear(hiermesh, f, null);
    }

    protected void moveGear(float f)
    {
        if(prevGear > f)
            bGearExtending = false;
        else
            bGearExtending = true;
        prevGear = f;
        moveGear(hierMesh(), f, FM);
        f *= 10F;
        if(bGearExtending)
        {
            float f1 = cvt(f, 0.0F, 1.0F, 0.0F, 0.7071068F);
            cGearPos = 2.0F * f1 * f1;
        } else
        {
            cGearPos = cvt(f, 8.5F, 10F, 0.0F, 1.0F);
        }
    }

    public void moveSteering(float f)
    {
        hierMesh().chunkSetAngles("GearC3_D0", 0.0F, -f, 0.0F);
    }

    public void moveArrestorHook(float f)
    {
        hierMesh().chunkSetAngles("Hook1_D0", 0.0F, f, 0.0F);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        xyz[1] = cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.231F, 0.0F, 0.231F);
        hierMesh().chunkSetLocate("GearL7_D0", xyz, ypr);
        xyz[1] = cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.231F, 0.0F, -0.231F);
        hierMesh().chunkSetLocate("GearR7_D0", xyz, ypr);
    }

    protected void moveAirBrake(float f)
    {
        moveGear(FM.CT.getGear());
        moveArrestorHook(arrestorPos);
    }

    protected void moveWingFold(HierMesh hiermesh, float f)
    {
        f *= 18F;
        if(bGearExtending)
        {
            if(f < 1.5F)
            {
                hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 0.0F, 1.5F, 0.0F, 2.6F), 0.0F);
                hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 0.0F, 1.5F, 0.0F, 2.6F), 0.0F);
            } else
            if(f < 2.5F)
            {
                hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 1.5F, 2.5F, 2.6F, 5.1F), 0.0F);
                hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 1.5F, 2.5F, 2.6F, 5.1F), 0.0F);
            } else
            {
                hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 2.5F, 17.9F, 5.1F, 105F), 0.0F);
                hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 2.5F, 16F, 5.1F, 105F), 0.0F);
            }
        } else
        if(f < 9F)
        {
            if(f < 6.8F)
                hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 0.01F, 6.8F, 0.0F, 45F), 0.0F);
            else
                hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 6.8F, 9F, 45F, 50F), 0.0F);
            if(f < 7.5F)
                hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 0.75F, 7.5F, 0.0F, 45F), 0.0F);
            else
                hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 7.5F, 9F, 45F, 50F), 0.0F);
        } else
        if(f < 11F)
        {
            hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 9F, 11F, 50F, 60F), 0.0F);
            hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 9F, 11F, 50F, 60F), 0.0F);
        } else
        {
            hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, cvt(f, 11F, 15.75F, 60F, 105F), 0.0F);
            hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, cvt(f, 11F, 15.75F, 60F, 105F), 0.0F);
        }
    }

    public void moveWingFold(float f)
    {
        if(prevWing > f)
            bGearExtending = false;
        else
            bGearExtending = true;
        prevWing = f;
        if(f < 0.001F)
        {
            setGunPodsOn(true);
            hideWingWeapons(false);
        } else
        {
            setGunPodsOn(false);
            FM.CT.WeaponControl[0] = false;
            hideWingWeapons(true);
        }
        moveWingFold(hierMesh(), f);
    }

    public void moveCockpitDoor(float f)
    {
        resetYPRmodifier();
        xyz[1] = cvt(f, 0.01F, 0.99F, 0.0F, 0.625F);
        xyz[2] = cvt(f, 0.01F, 0.99F, 0.0F, 0.06845F);
        ypr[2] = cvt(f, 0.01F, 0.99F, 0.0F, 1.0F);
        hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
        resetYPRmodifier();
        xyz[2] = cvt(f, 0.01F, 0.99F, 0.0F, 0.13F);
        ypr[2] = cvt(f, 0.01F, 0.99F, 0.0F, -8F);
        hierMesh().chunkSetLocate("Pilot1_D0", xyz, ypr);
        if(Config.isUSE_RENDER())
        {
            if(Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null)
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            setDoorSnd(f);
        }
    }

    protected void moveFlap(float f)
    {
        float f1 = 50F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap05_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap06_D0", 0.0F, f1, 0.0F);
    }

    public void update(float f)
    {
        super.update(f);
        if(bNeedSetup)
        {
            cGear = FM.CT.GearControl;
            bNeedSetup = false;
        }
        cGear = filter(f, FM.CT.GearControl, cGear, 999.9F, FM.CT.dvGear);
        if(prevGear2 > cGear)
            bGearExtending2 = false;
        else
            bGearExtending2 = true;
        prevGear2 = cGear;
        float f6 = 10F * cGear;
        if(bGearExtending2)
        {
            float f1 = cvt(f6, 0.0F, 1.0F, 0.0F, 0.7071068F);
            cGearPos = 2.0F * f1 * f1;
            hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 41F * cGearPos, 0.0F);
            f1 = cvt(f6, 0.0F, 0.25F, 0.0F, 1.0F);
            hierMesh().chunkSetAngles("GearC4_D0", 0.0F, 140F * f1, 0.0F);
            hierMesh().chunkSetAngles("GearC5_D0", 0.0F, 140F * f1, 0.0F);
        } else
        {
            cGearPos = cvt(f6, 8.5F, 10F, 0.0F, 1.0F);
            hierMesh().chunkSetAngles("GearC2_D0", 0.0F, 41F * cGearPos, 0.0F);
            float f2 = cvt(f6, 8.5F, 8.75F, 0.0F, 1.0F);
            hierMesh().chunkSetAngles("GearC4_D0", 0.0F, 140F * f2, 0.0F);
            hierMesh().chunkSetAngles("GearC5_D0", 0.0F, 140F * f2, 0.0F);
        }
        if(FM.Gears.arrestorVAngle != 0.0F)
        {
            float f3 = cvt(FM.Gears.arrestorVAngle, -43F, 21.5F, 0.0F, -64.5F);
            if(f3 < -64.5F * cGearPos)
                f3 = -64.5F * cGearPos;
            arrestorPos = 0.5F * arrestorPos + 0.5F * f3;
            arrestorVel = 0.0F;
        } else
        {
            float f4;
            if(arrestorVel >= -0.1F)
            {
                if(Engine.cur.land.isWater(FM.Loc.x, FM.Loc.y))
                    f4 = 0.0F;
                else
                    f4 = cvt(FM.getSpeedKMH(), 0.0F, 250F, 0.0F, 0.75F);
                f4 = -47.5F * World.Rnd().nextFloat(1.0F - f4, 1.0F + f4 + f4) * FM.Gears.arrestorVSink;
            } else
            {
                f4 = 0.0F;
            }
            if(f4 < 0.0F && FM.getSpeedKMH() > 60F)
                Eff3DActor.New(this, FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
            if(f4 < -47.2F)
                f4 = -47.2F;
            if(f4 < 0.0F)
                arrestorVel += f4;
            else
                arrestorVel += 0.5F;
            arrestorPos += arrestorVel;
            if(arrestorPos < -64.5F * cGearPos)
            {
                arrestorPos = -64.5F * cGearPos;
                arrestorVel = 0.0F;
            }
            if(arrestorPos > -64.5F * cGearPos * (1.0F - FM.CT.getArrestor()))
            {
                arrestorPos = -64.5F * cGearPos * (1.0F - FM.CT.getArrestor());
                arrestorVel = 0.0F;
            }
        }
        moveArrestorHook(arrestorPos);
        float f5 = Math.min(0.98F, FM.CT.getAirBrake());
        f5 = Math.max(f5, FM.CT.getGear());
        FM.CT.setGear(f5);
        f5 = FM.EI.engines[0].getControlRadiator();
        if(Math.abs(flapps - f5) > 0.01F)
        {
            flapps = f5;
            for(int i = 1; i < 22; i++)
                hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -23.7F * f5, 0.0F);

            hierMesh().chunkSetAngles("Water19_D0", 0.0F, -16F * f5, 0.0F);
        }
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            hierMesh().chunkVisible("HMask1_D0", false);
            break;
        }
    }

    protected void hitBone(String s, Shot shot, Point3d point3d)
    {
        if(s.startsWith("xx"))
        {
            if(s.startsWith("xxarmor"))
            {
                debuggunnery("Armor: Hit..");
                if(s.endsWith("f1"))
                    getEnergyPastArmor(World.Rnd().nextFloat(8F, 12F) / (Math.abs(v1.z) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p1"))
                {
                    getEnergyPastArmor(World.Rnd().nextFloat(16F, 36F) / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
                    if(shot.power <= 0.0F)
                        doRicochetBack(shot);
                } else
                if(s.endsWith("p2"))
                    getEnergyPastArmor(11D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                else
                if(s.endsWith("p3"))
                    getEnergyPastArmor(11.5D / (Math.abs(v1.x) + 9.9999997473787516E-005D), shot);
                return;
            }
            if(s.startsWith("xxcmglammo") && World.Rnd().nextFloat(0.0F, 20000F) < shot.power)
            {
                int i = 0 + 2 * World.Rnd().nextInt(0, 2);
                FM.AS.setJamBullets(0, i);
            }
            if(s.startsWith("xxcmgrammo") && World.Rnd().nextFloat(0.0F, 20000F) < shot.power)
            {
                int j = 1 + 2 * World.Rnd().nextInt(0, 2);
                FM.AS.setJamBullets(0, j);
            }
            if(s.startsWith("xxcontrols"))
            {
                debuggunnery("Controls: Hit..");
                int k = s.charAt(10) - 48;
                switch(k)
                {
                default:
                    break;

                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Ailerones Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 0);
                    }
                    break;

                case 7:
                case 8:
                    if(World.Rnd().nextFloat() < 0.08F && getEnergyPastArmor(0.1F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Elevator Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 1);
                    }
                    break;

                case 9:
                    if(World.Rnd().nextFloat() < 0.95F && getEnergyPastArmor(1.27F, shot) > 0.0F)
                    {
                        debuggunnery("Controls: Rudder Controls: Disabled..");
                        FM.AS.setControlsDamage(shot.initiator, 2);
                    }
                    break;
                }
                return;
            }
            if(s.startsWith("xxeng1"))
            {
                debuggunnery("Engine Module: Hit..");
                if(s.endsWith("case"))
                {
                    if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 0.55F), shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat() < shot.power / 280000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Stucks..");
                            FM.AS.setEngineStuck(shot.initiator, 0);
                        }
                        if(World.Rnd().nextFloat() < shot.power / 100000F)
                        {
                            debuggunnery("Engine Module: Engine Crank Case Hit - Engine Damaged..");
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                        }
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 24F), shot);
                }
                if(s.endsWith("cyls"))
                {
                    if(getEnergyPastArmor(0.85F, shot) > 0.0F && World.Rnd().nextFloat() < FM.EI.engines[0].getCylindersRatio() * 0.66F)
                    {
                        FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int)(shot.power / 32200F)));
                        debuggunnery("Engine Module: Cylinders Hit, " + FM.EI.engines[0].getCylindersOperable() + "/" + FM.EI.engines[0].getCylinders() + " Left..");
                        if(World.Rnd().nextFloat() < shot.power / 1000000F)
                        {
                            FM.AS.hitEngine(shot.initiator, 0, 2);
                            debuggunnery("Engine Module: Cylinders Hit - Engine Fires..");
                        }
                    }
                    getEnergyPastArmor(25F, shot);
                }
                if(s.endsWith("eqpt"))
                {
                    if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                    {
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 4);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 6);
                        if(World.Rnd().nextFloat(0.0F, 26700F) < shot.power)
                            FM.AS.setEngineSpecificDamage(shot.initiator, 0, 1);
                    }
                    getEnergyPastArmor(2.0F, shot);
                }
                if(s.endsWith("gear"))
                {
                    if(getEnergyPastArmor(4.6F, shot) > 0.0F && World.Rnd().nextFloat() < 0.5F)
                    {
                        debuggunnery("Engine Module: Bullet Jams Reductor Gear..");
                        FM.EI.engines[0].setEngineStuck(shot.initiator);
                    }
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 12.44565F), shot);
                }
                if(s.startsWith("xxeng1mag"))
                {
                    int l = s.charAt(9) - 49;
                    debuggunnery("Engine Module: Magneto " + l + " Destroyed..");
                    FM.EI.engines[0].setMagnetoKnockOut(shot.initiator, l);
                }
                if(s.endsWith("oil1"))
                {
                    if(World.Rnd().nextFloat() < 0.5F && getEnergyPastArmor(0.25F, shot) > 0.0F)
                        debuggunnery("Engine Module: Oil Radiator Hit..");
                    FM.AS.hitOil(shot.initiator, 0);
                }
                if(s.endsWith("prop") && getEnergyPastArmor(0.42F, shot) > 0.0F)
                    FM.EI.engines[0].setKillPropAngleDevice(shot.initiator);
                if(s.startsWith("xxeng1typ") && getEnergyPastArmor(0.42F, shot) > 0.0F)
                    FM.EI.engines[0].setKillPropAngleDeviceSpeeds(shot.initiator);
                return;
            }
            if(s.startsWith("xxhyd"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.25F, 12.39F), shot) > 0.0F)
                {
                    debuggunnery("Hydro System: Disabled..");
                    FM.AS.setInternalDamage(shot.initiator, 0);
                }
                return;
            }
            if(s.startsWith("xxlock"))
            {
                debuggunnery("Lock Construction: Hit..");
                if(s.startsWith("xxlockr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: Rudder1 Lock Shot Off..");
                    nextDMGLevels(3, 2, "Rudder1_D" + chunkDamageVisible("Rudder1"), shot.initiator);
                }
                if(s.startsWith("xxlockvl") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorL Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorL_D" + chunkDamageVisible("VatorL"), shot.initiator);
                }
                if(s.startsWith("xxlockvr") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: VatorR Lock Shot Off..");
                    nextDMGLevels(3, 2, "VatorR_D" + chunkDamageVisible("VatorR"), shot.initiator);
                }
                if(s.startsWith("xxlockal") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneL Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneL_D" + chunkDamageVisible("AroneL"), shot.initiator);
                }
                if(s.startsWith("xxlockar") && getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debuggunnery("Lock Construction: AroneR Lock Shot Off..");
                    nextDMGLevels(3, 2, "AroneR_D" + chunkDamageVisible("AroneR"), shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxmgun0"))
            {
                int i1 = s.charAt(7) - 49;
                if(getEnergyPastArmor(0.5F, shot) > 0.0F)
                {
                    debuggunnery("Armament: Machine Gun (" + i1 + ") Disabled..");
                    FM.AS.setJamBullets(0, i1);
                    getEnergyPastArmor(World.Rnd().nextFloat(0.5F, 23.325F), shot);
                }
                return;
            }
            if(s.startsWith("xxoil"))
            {
                if(getEnergyPastArmor(0.25F, shot) > 0.0F && World.Rnd().nextFloat() < 0.25F)
                {
                    FM.AS.hitOil(shot.initiator, 0);
                    getEnergyPastArmor(0.22F, shot);
                    debuggunnery("Engine Module: Oil Tank Pierced..");
                }
                return;
            }
            if(s.startsWith("xxradio"))
            {
                getEnergyPastArmor(25.532F, shot);
                return;
            }
            if(s.startsWith("xxspar"))
            {
                debugprintln(this, "*** Spar Construction: Hit..");
                if(s.startsWith("xxsparli") && chunkDamageVisible("WingLIn") > 2 && getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparri") && chunkDamageVisible("WingRIn") > 2 && getEnergyPastArmor(12.7F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRIn Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRIn_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlm") && chunkDamageVisible("WingLMid") > 2 && getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparrm") && chunkDamageVisible("WingRMid") > 2 && getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingRMid Spars Damaged..");
                    nextDMGLevels(1, 2, "WingRMid_D3", shot.initiator);
                }
                if(s.startsWith("xxsparlo") && chunkDamageVisible("WingLOut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingLOut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingLOut_D3", shot.initiator);
                }
                if(s.startsWith("xxsparro") && chunkDamageVisible("WingROut") > 2 && getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 1.8F), shot) > 0.0F)
                {
                    debugprintln(this, "*** WingROut Spars Damaged..");
                    nextDMGLevels(1, 2, "WingROut_D3", shot.initiator);
                }
                if(s.startsWith("xxspart") && chunkDamageVisible("Tail1") > 2 && getEnergyPastArmor(13.8F * World.Rnd().nextFloat(0.99F, 1.8F), shot) > 0.0F)
                {
                    debugprintln(this, "*** Tail1 Spars Damaged..");
                    nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
                }
                return;
            }
            if(s.startsWith("xxsupc"))
            {
                if(getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 12F), shot) > 0.0F)
                {
                    debuggunnery("Engine Module: Turbine Disabled..");
                    FM.AS.setEngineSpecificDamage(shot.initiator, 0, 0);
                }
                return;
            }
            if(s.startsWith("xxtank"))
            {
                int j1 = s.charAt(6) - 49;
                if(getEnergyPastArmor(1.0F, shot) > 0.0F)
                {
                    if(FM.AS.astateTankStates[j1] == 0)
                    {
                        debuggunnery("Fuel Tank (" + j1 + "): Pierced..");
                        FM.AS.hitTank(shot.initiator, j1, 1);
                        FM.AS.doSetTankState(shot.initiator, j1, 1);
                    }
                    if(World.Rnd().nextFloat() < 0.07F || shot.powerType == 3 && World.Rnd().nextFloat() < 0.8F)
                    {
                        FM.AS.hitTank(shot.initiator, j1, 2);
                        debuggunnery("Fuel Tank (" + j1 + "): Hit..");
                    }
                }
                return;
            } else
            {
                return;
            }
        }
        if(s.startsWith("xcf") || s.startsWith("xblister"))
        {
            hitChunk("CF", shot);
            if(s.startsWith("xcf2"))
            {
                if(point3d.x > -2.313D && point3d.x < -1.455D && point3d.z > 0.669D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
                if(point3d.z > 1.125D)
                    FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            } else
            if(point3d.x > -1.489D && point3d.x < -1.2D && point3d.z > 0.34D)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(World.Rnd().nextFloat() < 0.054F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            if(World.Rnd().nextFloat() < 0.054F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            return;
        }
        if(s.startsWith("xeng"))
        {
            if(chunkDamageVisible("Engine1") < 2)
                hitChunk("Engine1", shot);
        } else
        if(s.startsWith("xtail"))
        {
            if(chunkDamageVisible("Tail1") < 3)
                hitChunk("Tail1", shot);
        } else
        if(s.startsWith("xkeel"))
        {
            if(chunkDamageVisible("Keel1") < 2)
                hitChunk("Keel1", shot);
        } else
        if(s.startsWith("xstab"))
        {
            if(s.startsWith("xstabl") && chunkDamageVisible("StabL") < 2)
                hitChunk("StabL", shot);
            if(s.startsWith("xstabr") && chunkDamageVisible("StabR") < 1)
                hitChunk("StabR", shot);
        } else
        if(s.startsWith("xwing"))
        {
            if(s.startsWith("xwinglin") && chunkDamageVisible("WingLIn") < 3)
                hitChunk("WingLIn", shot);
            if(s.startsWith("xwingrin") && chunkDamageVisible("WingRIn") < 3)
                hitChunk("WingRIn", shot);
            if(s.startsWith("xwinglmid") && chunkDamageVisible("WingLMid") < 3)
                hitChunk("WingLMid", shot);
            if(s.startsWith("xwingrmid") && chunkDamageVisible("WingRMid") < 3)
                hitChunk("WingRMid", shot);
            if(s.startsWith("xwinglout") && chunkDamageVisible("WingLOut") < 3)
                hitChunk("WingLOut", shot);
            if(s.startsWith("xwingrout") && chunkDamageVisible("WingROut") < 3)
                hitChunk("WingROut", shot);
        } else
        if(s.startsWith("xgear"))
        {
            if(s.endsWith("1") && World.Rnd().nextFloat() < 0.05F)
            {
                debuggunnery("Hydro System: Disabled..");
                FM.AS.setInternalDamage(shot.initiator, 0);
            }
        } else
        if(s.startsWith("xpilot") || s.startsWith("xhead"))
        {
            byte byte0 = 0;
            int k1;
            if(s.endsWith("a"))
            {
                byte0 = 1;
                k1 = s.charAt(6) - 49;
            } else
            if(s.endsWith("b"))
            {
                byte0 = 2;
                k1 = s.charAt(6) - 49;
            } else
            {
                k1 = s.charAt(5) - 49;
            }
            hitFlesh(k1, shot, byte0);
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
    }

    private static final float filter(float f, float f1, float f2, float f3, float f4)
    {
        float f5 = (float)Math.exp(-f / f3);
        float f6 = f1 + (f2 - f1) * f5;
        if(f6 < f1)
        {
            f6 += f4 * f;
            if(f6 > f1)
                f6 = f1;
        } else
        if(f6 > f1)
        {
            f6 -= f4 * f;
            if(f6 < f1)
                f6 = f1;
        }
        return f6;
    }

    private float arrestorPos;
    private float arrestorVel;
    private float prevGear;
    private float prevGear2;
    private static boolean bGearExtending = false;
    private static boolean bGearExtending2 = false;
    private float prevWing;
    private float cGearPos;
    private float cGear;
    private boolean bNeedSetup;
    private float flapps;

    static 
    {
        Class class1 = F4U.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
