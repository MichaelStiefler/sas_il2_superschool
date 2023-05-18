// USSR night illumination flare bomb SAB-250-200 with a parachute, 34 cm diameter --- 250 kg weight.


package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class BombSAB250_200_gn16 extends Bomb
{

    public BombSAB250_200_gn16()
    {
        chute = null;
        bOnChute = false;
        bCollideLand = false;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void start()
    {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.5F, 1.75F);
        ttexpTM = World.Rnd().nextFloat(11.2F, 17.75F);
    }

    public void destroy()
    {
        if(chute != null)
            chute.destroy();
        super.destroy();
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(bOnChute)
        {
            getSpeed(v3d);
            if(!bCollideLand)
            {
                v3d.scale(0.96999999999999997D);
                if(v3d.z < -2D)
                    v3d.z += 1.1F * Time.tickConstLenFs();
            }
            else
                v3d.set(0.0D, 0.0D, 0.0D);
            setSpeed(v3d);
        } else
        if(curTm > ttcurTM)
        {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
            chute.mesh().setScale(0.5F);
            chute.pos.setRel(new Point3d(0.5D, 0.0D, -0.06D), new Orient(0.0F, 90F, 0.0F));
        }

    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(actor instanceof ActorLand)
        {
            bCollideLand = true;
            if(chute != null)
                chute.landing();
        }
        super.msgCollision(actor, s, s1);
    }

    private Chute chute;
    private boolean bOnChute;
    private boolean bCollideLand;
    private static Vector3d v3d = new Vector3d();
    private float ttcurTM;
    private float ttexpTM;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombSAB250_200_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/SAB250_200_gn16/mono.sim");
        Property.set(class1, "emitColor", new Color3f(1.0F, 1.0F, 0.95F));
        Property.set(class1, "emitLen", 250F);
        Property.set(class1, "emitMax", 10F);
        Property.set(class1, "timeLife", 600F);
        Property.set(class1, "timeFire", 580F);
        Property.set(class1, "radius", 0.01F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.34F);
        Property.set(class1, "massa", 244.6F);
        Property.set(class1, "sound", "weapon.bomb_phball");
        Property.set(class1, "dragCoefficient", 1.05F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}
