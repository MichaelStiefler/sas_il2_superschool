package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class U_2TM extends Scheme1
    implements TypeScout, TypeTransport
{

    public U_2TM()
    {
    }

    protected void moveFlap(float f)
    {
        float f1 = -60F * f;
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
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
            if(shot.chunkName.startsWith("Turret"))
                FM.turret[0].bIsOperable = false;
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

    public void doKillPilot(int i)
    {
        if(i == 1)
            FM.turret[0].bIsOperable = false;
    }

    public void doMurderPilot(int i)
    {
        switch(i)
        {
        default:
            break;

        case 0:
            hierMesh().chunkVisible("Pilot1_D0", false);
            hierMesh().chunkVisible("Head1_D0", false);
            hierMesh().chunkVisible("Pilot1_D1", true);
            if(!FM.AS.bIsAboutToBailout)
                hierMesh().chunkVisible("Gore1_D0", true);
            break;

        case 1:
            hierMesh().chunkVisible("Pilot2_D0", false);
            hierMesh().chunkVisible("Head2_D0", false);
            hierMesh().chunkVisible("Pilot2_D1", true);
            if(!FM.AS.bIsAboutToBailout)
                hierMesh().chunkVisible("Gore2_D0", true);
            break;
        }
    }

    public boolean turretAngles(int i, float af[])
    {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        if(f < -45F)
        {
            f = -45F;
            flag = false;
        }
        if(f > 45F)
        {
            f = 45F;
            flag = false;
        }
        if(f1 < -45F)
        {
            f1 = -45F;
            flag = false;
        }
        if(f1 > 20F)
        {
            f1 = 20F;
            flag = false;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    private static final float gearL2[] = {
        0.0F, 1.0F, 2.0F, 2.9F, 3.2F, 3.35F
    };
    private static final float gearL4[] = {
        0.0F, 7.5F, 15F, 22F, 29F, 35.5F
    };
    private static final float gearL5[] = {
        0.0F, 1.5F, 4F, 7.5F, 10F, 11.5F
    };

    static
    {
        Class class1 = U_2TM.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "U-2TM");
        Property.set(class1, "meshName", "3do/plane/U-2TM/PlaneU2TM.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1956F);
        Property.set(class1, "FlightModel", "FlightModels/U-2TM.fmd");
        Property.set(class1, "cockpitClass", new Class[] {
            CockpitU2TM.class
        });
        Property.set(class1, "LOSElevation", 1.01885F);
        Aircraft.weaponTriggersRegister(class1, new int[] {
            10
        });
        Aircraft.weaponHooksRegister(class1, new String[] {
            "_MGUN01"
        });
    }
}
