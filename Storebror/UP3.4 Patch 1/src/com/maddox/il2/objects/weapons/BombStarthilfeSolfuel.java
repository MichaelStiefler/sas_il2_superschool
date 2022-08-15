package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.*;

public class BombStarthilfeSolfuel extends Bomb
{

    public BombStarthilfeSolfuel()
    {
        chute = null;
        bOnChute = false;
    }

    protected boolean haveSound()
    {
        return false;
    }

    public void startSound() {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (this.sound == null) {
            this.sound = this.newSound("weapon.rato", true);
        }
        this.sound.setVolume(1.0F);
    }

    public void stopSound() {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        if (this.sound != null) {
            this.sound.cancel();
        }
    }

    public void start()
    {
        super.start();
        ttcurTM = World.Rnd().nextFloat(0.5F, 3.5F);
    }

    public void interpolateTick()
    {
        super.interpolateTick();
        if(bOnChute)
        {
            this.pos.getAbs(or);
            or.interpolate(or_, 0.4F);
            this.pos.setAbs(or);
            getSpeed(v3d);
            v3d.scale(0.997D);
            if(v3d.z < -5D)
                v3d.z += 1.1F * Time.tickConstLenFs();
            setSpeed(v3d);
        } else
        if(this.curTm > ttcurTM)
        {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
        }
    }

    public void msgCollision(Actor actor, String s, String s_0_)
    {
        if(actor instanceof ActorLand)
        {
            if(chute != null)
                chute.landing();
            postDestroy();
        } else
        {
            super.msgCollision(actor, s, s_0_);
        }
    }

    private Chute chute;
    boolean bOnChute;
    private static Orient or = new Orient();
    private static Orient or_ = new Orient(0.0F, 0.0F, 0.0F);
    private static Vector3d v3d = new Vector3d();
    float ttcurTM;
}
