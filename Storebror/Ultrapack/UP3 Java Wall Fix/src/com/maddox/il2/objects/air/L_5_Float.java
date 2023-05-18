package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class L_5_Float extends Scheme1
    implements TypeSailPlane, TypeScout, TypeTransport, TypeStormovik
{

    public L_5_Float()
    {
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(Config.isUSE_RENDER() && World.cur().camouflage == 1)
        {
            hierMesh().chunkVisible("GearL1_D0", false);
            hierMesh().chunkVisible("GearL11_D0", true);
            hierMesh().chunkVisible("GearR1_D0", false);
            hierMesh().chunkVisible("GearR11_D0", true);
            FM.CT.bHasBrakeControl = false;
        }
    }

    protected void moveFan(float f)
    {
        if(Config.isUSE_RENDER())
        {
            super.moveFan(f);
            float f1 = Aircraft.cvt(FM.Or.getTangage(), -30F, 30F, -30F, 30F);
            if(FM.Gears.onGround() && FM.CT.getGear() > 0.9F && FM.getSpeed() > 5F)
            {
                if(FM.Gears.gWheelSinking[0] > 0.0F)
                    hierMesh().chunkSetAngles("GearL11_D0", World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F) - f1);
                else
                    hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, -f1);
                if(FM.Gears.gWheelSinking[1] > 0.0F)
                    hierMesh().chunkSetAngles("GearR11_D0", World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F) - f1);
                else
                    hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, -f1);
            } else
            {
                hierMesh().chunkSetAngles("GearL11_D0", 0.0F, 0.0F, -f1);
                hierMesh().chunkSetAngles("GearR11_D0", 0.0F, 0.0F, -f1);
            }
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

    public static void moveGear(HierMesh hiermesh, float f)
    {
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    public void moveWheelSink()
    {
        resetYPRmodifier();
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 0.5F);
        float f = Aircraft.cvt(FM.Gears.gWheelSinking[0], 0.0F, 0.5F, 0.0F, 5F);
        hierMesh().chunkSetAngles("GearL2_D0", 0.0F, floatindex(f, gearL2), 0.0F);
        Aircraft.xyz[2] = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 0.5F);
        f = Aircraft.cvt(FM.Gears.gWheelSinking[1], 0.0F, 0.5F, 0.0F, 5F);
        hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -floatindex(f, gearL2), 0.0F);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f)
    {
        hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * f);
        hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveAileron(float f)
    {
        hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
        hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveFlap(float f)
    {
        float f1 = -60F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, -f1);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, -f1);
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("WingLMid") && World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass)
            FM.AS.hitTank(shot.initiator, 0, (int)(1.0F + shot.mass * 18.95F * 2.0F));
        if(shot.chunkName.startsWith("WingRMid") && World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass)
            FM.AS.hitTank(shot.initiator, 1, (int)(1.0F + shot.mass * 18.95F * 2.0F));
        if(shot.chunkName.startsWith("Engine"))
        {
            if(World.Rnd().nextFloat(0.0F, 1.0F) < shot.mass)
                FM.AS.hitEngine(shot.initiator, 0, 1);
            if(Aircraft.v1.z > 0.0D && World.Rnd().nextFloat() < 0.12F)
            {
                FM.AS.setEngineDies(shot.initiator, 0);
                if(shot.mass > 0.1F)
                    FM.AS.hitEngine(shot.initiator, 0, 5);
            }
            if(Aircraft.v1.x < 0.1D && World.Rnd().nextFloat() < 0.57F)
                FM.AS.hitOil(shot.initiator, 0);
        }
        if(shot.chunkName.startsWith("Pilot1"))
        {
            killPilot(shot.initiator, 0);
            FM.setCapableOfBMP(false, shot.initiator);
            if(Aircraft.Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
                HUD.logCenter("H E A D S H O T");
        } else
        if(shot.chunkName.startsWith("Pilot2"))
        {
            killPilot(shot.initiator, 1);
            if(Aircraft.Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
                HUD.logCenter("H E A D S H O T");
        } else
        {
            if(FM.AS.astateEngineStates[0] == 4 && World.Rnd().nextInt(0, 99) < 33)
                FM.setCapableOfBMP(false, shot.initiator);
            super.msgShot(shot);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 34:
            return super.cutFM(35, j, actor);

        case 37:
            return super.cutFM(38, j, actor);
        }
        return super.cutFM(i, j, actor);
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

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            hierMesh().chunkVisible("HMask2_D0", false);
            break;
        }
    }

    private static final float gearL2[] = {
        0.0F, 1.0F, 2.0F, 2.9F, 3.2F, 3.35F
    };
    public static boolean bChangedPit = false;

    static
    {
        Class class1 = L_5_Float.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "L-5_float");
        Property.set(class1, "meshName", "3DO/Plane/Sentinelfloat/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "meshName_us", "3DO/Plane/Sentinelfloat(US)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar04());
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
        Property.set(class1, "yearService", 1940.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/L5Sentinel_Ex.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitL_5.class
        });
        Aircraft.weaponTriggersRegister(class1, new int[] {
            9, 9, 9, 9, 2, 2, 2, 2
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04"
        });
    }
}
