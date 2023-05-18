package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class LA_7R extends LA_X
{

    public LA_7R()
    {
        bHasEngine = true;
        flame = null;
        dust = null;
        trail = null;
        sprite = null;
    }

    public void destroy()
    {
        if(Actor.isValid(flame))
            flame.destroy();
        if(Actor.isValid(dust))
            dust.destroy();
        if(Actor.isValid(trail))
            trail.destroy();
        if(Actor.isValid(sprite))
            sprite.destroy();
        super.destroy();
    }

    public void onAircraftLoaded()
    {
        super.onAircraftLoaded();
        if(Config.isUSE_RENDER())
        {
            flame = Eff3DActor.New(this, findHook("_Engine2EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            dust = Eff3DActor.New(this, findHook("_Engine2EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100D.eff", -1F);
            trail = Eff3DActor.New(this, findHook("_Engine2EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100T.eff", -1F);
            sprite = Eff3DActor.New(this, findHook("_Engine2EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100S.eff", -1F);
            Eff3DActor.setIntesity(flame, 0.0F);
            Eff3DActor.setIntesity(dust, 0.0F);
            Eff3DActor.setIntesity(trail, 0.0F);
            Eff3DActor.setIntesity(sprite, 0.0F);
        }
    }

    protected void moveFan(float f)
    {
        if(!Config.isUSE_RENDER())
            return;
        super.moveFan(f);
        if(isNetMirror())
        {
            if(FM.EI.engines[1].getStage() == 6)
            {
                Eff3DActor.setIntesity(flame, 1.0F);
                Eff3DActor.setIntesity(dust, 1.0F);
                Eff3DActor.setIntesity(trail, 1.0F);
                Eff3DActor.setIntesity(sprite, 1.0F);
            } else
            {
                Eff3DActor.setIntesity(flame, 0.0F);
                Eff3DActor.setIntesity(dust, 0.0F);
                Eff3DActor.setIntesity(trail, 0.0F);
                Eff3DActor.setIntesity(sprite, 0.0F);
            }
        } else
        if(bHasEngine && FM.EI.engines[1].getControlThrottle() > 0.0F && FM.EI.engines[1].getStage() == 6)
        {
            Eff3DActor.setIntesity(flame, 1.0F);
            Eff3DActor.setIntesity(dust, 1.0F);
            Eff3DActor.setIntesity(trail, 1.0F);
            Eff3DActor.setIntesity(sprite, 1.0F);
        } else
        {
            Eff3DActor.setIntesity(flame, 0.0F);
            Eff3DActor.setIntesity(dust, 0.0F);
            Eff3DActor.setIntesity(trail, 0.0F);
            Eff3DActor.setIntesity(sprite, 0.0F);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor)
    {
        switch(i)
        {
        case 19:
            bHasEngine = false;
            FM.AS.setEngineDies(this, 1);
            return cut(partNames()[i]);

        case 3:
        case 4:
            return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f)
    {
        super.update(f);
        if(isNetMirror())
            return;
        bPowR = this == World.getPlayerAircraft();
        if(FM.getAltitude() - Engine.land().HQ(FM.Loc.x, FM.Loc.y) > 5D && FM.M.fuel > 0.0F)
        {
            if(FM.EI.engines[1].getControlThrottle() > (bPowR ? 0.4120879F : 0.77F) && (FM.EI.engines[1].getStage() == 0 && FM.M.nitro > 0.0F))
            {
                FM.EI.engines[1].setStage(this, 6);
                if(bPowR)
                    HUD.log("EngineI" + (FM.EI.engines[1].getStage() != 6 ? 48 : '1'));
            }
            if(FM.EI.engines[1].getControlThrottle() < (bPowR ? 0.4120879F : 0.77F) && FM.EI.engines[1].getStage() > 0)
            {
                FM.EI.engines[1].setEngineStops(this);
                if(bPowR)
                    HUD.log("EngineI" + (FM.EI.engines[1].getStage() != 6 ? 48 : '1'));
            }
        }
    }

    private static final float powR = 0.4120879F;
    private static final float powA = 0.77F;
    private boolean bHasEngine;
    private Eff3DActor flame;
    private Eff3DActor dust;
    private Eff3DActor trail;
    private Eff3DActor sprite;
    private boolean bPowR;
    private static Vector3d v = new Vector3d();

    static 
    {
        Class class1 = LA_7R.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "La");
        Property.set(class1, "meshName", "3DO/Plane/La-7R(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/La-7R.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLA_7R.class });
        Property.set(class1, "LOSElevation", 0.92F);
        weaponTriggersRegister(class1, new int[] {
            1, 1, 3, 3, 9, 9
        });
        weaponHooksRegister(class1, new String[] {
            "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02"
        });
    }
}
