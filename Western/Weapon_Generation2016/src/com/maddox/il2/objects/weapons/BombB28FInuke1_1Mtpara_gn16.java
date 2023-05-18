
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


public class BombB28FInuke1_1Mtpara_gn16 extends Bomb
{

    public BombB28FInuke1_1Mtpara_gn16()
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
        ttcurTM = World.Rnd().nextFloat(0.9F, 1.6F);
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
            if(!bCollideLand)
            {
                super.pos.getAbs(p, or);
                v3d.scale(0.990D);
                if(v3d.z < -2D)
                    v3d.z += 0.95F * Time.tickConstLenFs();
                float pitch = or.getPitch();
                for(;pitch > 180.0F; pitch -= 360.0F) ;
                for(;pitch < -180.0F; pitch += 360.0F) ;
                if(v3d.x > 0.0D && pitch > -89.0F && pitch > Math.toDegrees(Math.atan(v3d.z / v3d.x)))
                {
                    or.increment(0.0F, ((float)Math.toDegrees(Math.atan(v3d.z / v3d.x)) - pitch) * 0.5F, 0.0F);
                    super.pos.setAbs(p, or);
                }
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
            chute.pos.setRel(new Point3d(0.5D, 0.0D, -0.04D), new Orient(0.0F, 90F, 0.0F));
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
    private Orient or = new Orient();
    private Point3d p = new Point3d();

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombB28FInuke1_1Mtpara_gn16.class;
        Property.set(class1, "mesh", "3DO/Arms/USnukeB28_gn16/monoFI.sim");
        Property.set(class1, "radius", 31000F);
        Property.set(class1, "power", 1100000000F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.58F);
        Property.set(class1, "massa", 1053.0F);
        Property.set(class1, "sound", "weapon.bomb_big");
        Property.set(class1, "nuke", 1);
        Property.set(class1, "fuze", ((Object) (new Object[] {
            com.maddox.il2.objects.weapons.Fuze_USnukeDelay.class, com.maddox.il2.objects.weapons.Fuze_USnukeLowAlt.class, com.maddox.il2.objects.weapons.Fuze_AN_M100.class, com.maddox.il2.objects.weapons.Fuze_USnukeAir.class
        })));
		Property.set(class1, "dragCoefficient", 0.60F); // Aerodynamic Drag Coefficient, Stock WWII bombs=1.0F
    }
}