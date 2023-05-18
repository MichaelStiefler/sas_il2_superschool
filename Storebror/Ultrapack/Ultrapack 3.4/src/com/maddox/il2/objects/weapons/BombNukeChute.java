package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombNukeChute extends BombNuke
{

    public BombNukeChute()
    {
        chute = null;
        bOnChute = false;
    }

    public void destroy()
    {
        if(chute != null)
            chute.destroy();
        super.destroy();
    }

    public void interpolateTick() {
        super.interpolateTick();
        if (this.bOnChute) {
            this.getSpeed(v3d);
            v3d.scale(0.99D);
            if (v3d.z < -10D) {
                v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(v3d);
        } else if (this.curTm > this.ttcurTM) {
            setMesh(Property.stringValue(this.getClass(), "meshOpen"));
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.mesh().setScale(2.0F);
            this.chute.mesh().setFastShadowVisibility(2);
            this.chute.pos.setRel(new Point3d(1.1D, 0.0D, -0.3D), new Orient(0.0F, 90F, 0.0F));
            this.DistFromCMtoStab = 5000F; // make bomb turn into moving direction when on chute, really!
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
}
