package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public abstract class P_39 extends Scheme1
    implements TypeFighter
{

    public P_39()
    {
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
            break;
        }
    }

    public void rareAction(float f, boolean flag)
    {
        super.rareAction(f, flag);
        if(FM.getAltitude() < 3000F)
            hierMesh().chunkVisible("HMask1_D0", false);
        else
            hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Pilot1_D0"));
    }

    public static void moveGear(HierMesh hiermesh, float f)
    {
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 87.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -87.5F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 15.5F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 15.5F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC9_D0", 0.0F, 90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 100F * f, 0.0F);
        float f1 = Math.max(-f * 1500F, -90F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("LanLight_D0", 0.0F, 0.0F, 92.5F * f);
    }

    protected void moveGear(float f)
    {
        moveGear(hierMesh(), f);
    }

    protected void moveRudder(float f)
    {
        hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -31F * f, 0.0F);
        if(FM.CT.getGear() == 1.0F)
            hierMesh().chunkSetAngles("GearC2_D0", -40F * f, 0.0F, 0.0F);
    }

    protected void moveFlap(float f)
    {
        hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    public void msgShot(Shot shot)
    {
        setShot(shot);
        if(shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)
            FM.AS.hitTank(shot.initiator, 0, 1);
        if(shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)
            FM.AS.hitTank(shot.initiator, 1, 1);
        if(shot.chunkName.startsWith("CF"))
        {
            if(World.Rnd().nextFloat(0.0F, 1.0F) < 0.3F)
                FM.AS.hitEngine(shot.initiator, 0, 1);
            if(World.Rnd().nextFloat() < 0.03F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
            if(World.Rnd().nextFloat() < 0.03F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
            if(World.Rnd().nextFloat() < 0.03F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
            if(World.Rnd().nextFloat() < 0.03F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
            if(World.Rnd().nextFloat() < 0.03F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
            if(World.Rnd().nextFloat() < 0.03F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
            if(World.Rnd().nextFloat() < 0.03F)
                FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
        }
        if(shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.06F && Pd.z > 0.47999998927116394D)
        {
            FM.AS.setJamBullets(0, 0);
            FM.AS.setJamBullets(0, 1);
        }
        if(shot.chunkName.startsWith("Oil") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.5F)
            FM.AS.hitOil(shot.initiator, 0);
        if(shot.chunkName.startsWith("Pilot"))
        {
            if(shot.power * Math.abs(v1.x) > 12070D)
                FM.AS.hitPilot(shot.initiator, 0, (int)(shot.mass * 1000F * 0.5F));
            if(Pd.z > 1.0117499828338623D)
            {
                killPilot(shot.initiator, 0);
                if(shot.initiator == World.getPlayerAircraft() && World.cur().isArcade())
                    HUD.logCenter("H E A D S H O T");
            }
            return;
        } else
        {
            super.msgShot(shot);
            return;
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 33:
            return super.cutFM(34, j, actor);

        case 36:
            return super.cutFM(37, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    static 
    {
        Class class1 = P_39.class;
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
    }
}
