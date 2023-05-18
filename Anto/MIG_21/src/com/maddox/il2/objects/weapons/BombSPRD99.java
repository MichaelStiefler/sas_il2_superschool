package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.air.Chute;

public class BombSPRD99 extends com.maddox.il2.objects.weapons.Bomb
{

    public BombSPRD99()
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
        ttcurTM = com.maddox.il2.ai.World.Rnd().nextFloat(0.5F, 3.5F);
    }

/*    public void interpolateTick()
    {
        super.interpolateTick();
        if(bOnChute)
        {
            pos.getAbs(or);
            or.interpolate(or_, 0.4F);
            pos.setAbs(or);
            getSpeed(v3d);
            v3d.scale(0.997D);
            if(v3d.z < -5D)
                v3d.z += 1.1F * com.maddox.rts.Time.tickConstLenFs();
            setSpeed(v3d);
        } else
        if(curTm > ttcurTM)
        {
            bOnChute = true;
            chute = new Chute(((com.maddox.il2.engine.Actor) (this)));
            chute.collide(false);
            setMesh("3DO/Arms/SPRD-99/Chuted_mono.sim");
        }
    }
    */

    public void msgCollision(com.maddox.il2.engine.Actor actor, java.lang.String s, java.lang.String s1)
    {
        if(actor instanceof com.maddox.il2.objects.ActorLand)
        {
            if(chute != null)
                chute.landing();
            postDestroy();
        } else
        {
            super.msgCollision(actor, s, s1);
        }
    }

    private com.maddox.il2.objects.air.Chute chute;
    private boolean bOnChute;
    private static com.maddox.il2.engine.Orient or = new Orient();
    private static com.maddox.il2.engine.Orient or_ = new Orient(0.0F, 0.0F, 0.0F);
    private static com.maddox.JGP.Vector3d v3d = new Vector3d();
    private float ttcurTM;

    static 
    {
        java.lang.Class class1 = com.maddox.rts.CLASS.THIS();
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "mesh", "3DO/Arms/SPRD-99/mono.sim");
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "radius", 0.1F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "power", 0.0F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "powerType", 0);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "kalibr", 0.7F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "massa", 0.9F);
        com.maddox.rts.Property.set(((java.lang.Object) (class1)), "sound", "weapon.bomb_phball");
    }
}