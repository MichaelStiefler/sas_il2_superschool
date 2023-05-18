
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.*;
import com.maddox.sas1946.il2.util.TrueRandom;


public class RocketAGM12E_gn16 extends RemoteControlRocket
{
    public RocketAGM12E_gn16()
    {
        super();
        setMesh(Property.stringValue(getClass(), "mesh"));
        mesh.materialReplace("Ordnance1", "Ordnance1E");
    }

    public RocketAGM12E_gn16(Actor actor, NetChannel netchannel, int i, Point3d point3d, Orient orient, float f)
    {
        super(actor, netchannel, i, point3d, orient, f);
    }

    protected void doStart(float f)
    {
        super.doStart(f);
        t1 = Time.current() + 4000L;
        if(Config.isUSE_RENDER())
        {
            fl1 = Eff3DActor.New(this, findHook("_NavLightR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
            fl2 = Eff3DActor.New(this, findHook("_NavLightG"), null, 1.0F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
//            super.flame.drawing(false);
        }
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(t1 < Time.current() && ((Actor)this).pos.getAbsPoint().z <= 200D + World.land().HQ(((Actor)this).pos.getAbsPoint().x, ((Actor)this).pos.getAbsPoint().y) + TrueRandom.nextDouble(-10D, 10D))
            doFireContaineds();
    }

    public void destroy()
    {
        if(Config.isUSE_RENDER())
        {
            Eff3DActor.finish(fl1);
            Eff3DActor.finish(fl2);
        }
        super.destroy();
    }

    private void doFireContaineds()
    {
        Explosions.AirFlak(pos.getAbsPoint(), 1);
        Actor actor = null;
        if(Actor.isValid(getOwner()))
            actor = getOwner();
        Point3d point3d = new Point3d(pos.getAbsPoint());
        Orient orient = new Orient();
        Vector3d vector3d = new Vector3d();
        for(int i = 0; i < 820; i++)
        {
            orient.set(TrueRandom.nextFloat(0.0F, 360F), TrueRandom.nextFloat(-90F, 90F), TrueRandom.nextFloat(-180F, 180F));
            getSpeed(vector3d);
            vector3d.add(TrueRandom.nextDouble(-15D, 15D), TrueRandom.nextDouble(-15D, 15D), TrueRandom.nextDouble(-15D, 15D));
            BombletBLU26 bombletblu26 = new BombletBLU26();
            ((Bomb) (bombletblu26)).pos.setUpdateEnable(true);
            ((Bomb) (bombletblu26)).pos.setAbs(point3d, orient);
            ((Bomb) (bombletblu26)).pos.reset();
            bombletblu26.start();
            bombletblu26.setOwner(actor, false, false, false);
            bombletblu26.setSpeed(vector3d);
        }

        if(Config.isUSE_RENDER())
        {
            Eff3DActor.finish(fl1);
            Eff3DActor.finish(fl2);
        }
        postDestroy();
    }

    static Class _mthclass$(String s)
    {
        Class c;
        try{
            c = Class.forName(s);
        } catch ( ClassNotFoundException e ){
            throw new NoClassDefFoundError(e.getMessage());
        }
        return c;
    }

    static Class class$com$maddox$il2$objects$weapons$RocketAGM12E_gn16;

    private Eff3DActor fl1;
    private Eff3DActor fl2;
    private long t1;

    static 
    {
   	Class class1
	    = (class$com$maddox$il2$objects$weapons$RocketAGM12E_gn16 == null
	       ? (class$com$maddox$il2$objects$weapons$RocketAGM12E_gn16
		  = _mthclass$("com.maddox.il2.objects.weapons.RocketAGM12E_gn16"))
	       : class$com$maddox$il2$objects$weapons$RocketAGM12E_gn16);
        Property.set(class1, "mesh", "3do/arms/AGM12C_gn16/mono.sim");
        Property.set(class1, "sprite", "3DO/Effects/Tracers/GuidedRocket/Black.eff");
        Property.set(class1, "flame", "3do/effects/rocket/mono.sim");
        Property.set(class1, "smoke", "3DO/Effects/Tracers/GuidedRocket/White.eff");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.5F));
        Property.set(class1, "emitLen", 50F);
        Property.set(class1, "emitMax", 1.0F);
        Property.set(class1, "sound", "weapon.rocket_132");
        Property.set(class1, "radius", 100F);
        Property.set(class1, "timeLife", 480F);
        Property.set(class1, "timeFire", 7F);
        Property.set(class1, "force", 15712F);
        Property.set(class1, "power", 10F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.46F);
        Property.set(class1, "massa", 810F);
        Property.set(class1, "massaEnd", 553F);
        Property.set(class1, "friendlyName", "AGM-12E");
        Spawn.add(class1, new SPAWN());
    }


}