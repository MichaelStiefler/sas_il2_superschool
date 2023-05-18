
package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;


public class BombB57nuke20ktpara_gn16 extends Bomb
{

    public BombB57nuke20ktpara_gn16()
    {
        chute = null;
        bOnChute = false;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void start()
    {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.7F, 1.95F);
        ttexpTM = World.Rnd().nextFloat(11.5F, 18.05F);
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
            v3d.scale(0.97999999999999997D);
            if(v3d.z < -2D)
                v3d.z += 0.8F * Time.tickConstLenFs();
            setSpeed(v3d);
        } else
        if(curTm > ttcurTM)
        {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
            chute.mesh().setScale(0.5F);
            chute.pos.setRel(new Point3d(0.5D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        }
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if((actor instanceof ActorLand) && chute != null)
            chute.landing();
        super.msgCollision(actor, s, s1);
    }

    private Chute chute;
    private boolean bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float ttcurTM;
    private float ttexpTM;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombB57nuke20ktpara_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/USnukeB57_gn16/mono.sim");
        Property.set(class1, "radius", 9000F);
        Property.set(class1, "power", 20000000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.375F);
        Property.set(class1, "massa", 242.0F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_USnukeDelay.class, com.maddox.il2.objects.weapons.Fuze_USnukeLowAlt.class, com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_USnukeAir.class
        })));
    }
}