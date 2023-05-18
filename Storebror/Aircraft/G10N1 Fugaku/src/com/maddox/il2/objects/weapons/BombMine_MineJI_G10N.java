package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class BombMine_MineJI_G10N extends AerialMine2 implements BombParaAerialMine2
{

    public BombMine_MineJI_G10N()
    {
        chute = null;
        bOnChute = false;
    }

    public void start()
    {
        super.start();
        ttcurTM = 1.5F;
        openHeight = 400F;
        minHeight = 100F;
    }

    public void destroy()
    {
        if(chute != null)
            chute.destroy();
        super.destroy();
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if((actor instanceof ActorLand) && chute != null)
            bOnChute = false;
        ttcurTM = 100000F;
        if(chute != null)
            chute.landing();
        super.msgCollision(actor, s, s1);
    }

    public void interpolateTick()
    {
        super.curTm += Time.tickLenFs();
        super.interpolateTick();
        this.pos.getAbs(P, Or);
        if(bOnChute)
        {
            getSpeed(v3d);
            v3d.scale(0.99D);
            if(v3d.z < -10D)
                v3d.z += 1.1F * Time.tickConstLenFs();
            setSpeed(v3d);
//            super.pos.getAbs(P, Or);
        } else
        if(super.curTm > ttcurTM && P.z <= (double)openHeight && P.z >= (double)minHeight)
        {
            bOnChute = true;
            chute = new Chute(this);
            chute.collide(false);
            chute.mesh().setScale(2.5F);
            chute.mesh().setFastShadowVisibility(2);
            chute.pos.setRel(new Point3d(1.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        } else
        if(super.curTm > ttcurTM && P.z <= (double)minHeight && P.z > 0.05D + World.land().HQ(P.x, P.y))
            bOnChute = false;
        super.pos.getRel(P, Or);
        if(chute == null && P.z <= 0.05D + World.land().HQ(P.x, P.y))
        {
            drawing(false);
//            Explosions.torpedoEnter_Water(AerialMine.P, 4F, 1.0F);
            Explosions.Explode10Kg_Water(AerialMine2.P, 4F, 1.0F);
            destroy();
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Did Not Arm! ");
        }
        if(chute != null && P.z <= 0.05D + World.land().HQ(P.x, P.y))
        {
            setMesh("3DO/Arms/MineJI_G10N/Sea/mono.sim");
            drawing(true);
        }
    }

    private Chute chute;
    private boolean bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float ttcurTM;
    private float openHeight;
    private float minHeight;
    static Orient Or = new Orient();
    static Point3d P = new Point3d();

    static 
    {
        Class class1 = BombMine_MineJI_G10N.class;
        Property.set(class1, "mesh", "3DO/Arms/MineJI_G10N/mono.sim");
        Property.set(class1, "radius", 8F);
        Property.set(class1, "power", 55F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.36F);
        Property.set(class1, "massa", 135F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 0.0F);
        Property.set(class1, "traveltime", 86400F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 10F);
        Property.set(class1, "impactAngleMax", 80F);
        Property.set(class1, "impactSpeed", 88F);
        Property.set(class1, "armingTime", 0.0F);
        Property.set(class1, "dropMaxAltitude", 400F);
        Property.set(class1, "dropMinAltitude", 50F);
        Property.set(class1, "dropSpeed", 160F);
    }
}
