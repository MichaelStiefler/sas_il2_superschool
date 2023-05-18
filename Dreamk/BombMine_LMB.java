// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/22/2014 7:02:31 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   BombMine_BM250.java

package com.maddox.il2.objects.weapons;

import com.maddox.JGP.*;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.ActorLand;
import com.maddox.il2.objects.air.Chute;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

// Referenced classes of package com.maddox.il2.objects.weapons:
//            BombParaInfluenceMine, AerialMine, Bomb

public class BombMine_LMB extends BombParaInfluenceMine
{

    public BombMine_LMB()
    {
        chute = null;
        bOnChute = false;
    }

    public void start()
    {
        super.start();
        ttcurTM = 1.5F;
        ttexpTM = World.Rnd().nextFloat(11.2F, 17.75F);
        openHeight = 400F;
        minHeight = 70.0F;
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
     //   if(super.curTm > ttcurTM && super.curTm <= ttcurTM + 0.05F)
       // {
         //   Explosions.airbustABcontainer(super.pos.getAbsPoint(), 0);
           // setMesh("3DO/Arms/LMB/Open/mono.sim");
       // }
        if(bOnChute)
        {
            setMesh("3DO/Arms/LMB/Open/mono.sim");
            getSpeed(v3d);
            v3d.scale(0.98999999999999999D);
            if(((Tuple3d) (v3d)).z < -10D)
                v3d.z += 1.1F * Time.tickConstLenFs();
            setSpeed(v3d);
            super.pos.getAbs(Bomb.P, Bomb.Or);
        } else
        if(super.curTm > ttcurTM && ((Tuple3d) (Bomb.P)).z <= (double)openHeight && ((Tuple3d) (Bomb.P)).z >= (double)minHeight)
        {
            bOnChute = true;
            setMesh("3DO/Arms/LMB/Open/mono.sim");
            chute = new Chute(this);
            chute.collide(false);
            chute.mesh().setScale(2.5F);
            ((Actor) (chute)).pos.setRel(new Point3d(1.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        } else
        if(super.curTm > ttcurTM && ((Tuple3d) (Bomb.P)).z <= (double)minHeight && ((Tuple3d) (Bomb.P)).z > 0.050000000000000003D + World.land().HQ(((Tuple3d) (Bomb.P)).x, ((Tuple3d) (Bomb.P)).y))
        {
            bOnChute = false;
            setMesh("3DO/Arms/LMB/mono.sim");
        }
        super.pos.getRel(Bomb.P, Bomb.Or);
        if(chute == null && ((Tuple3d) (Bomb.P)).z <= 0.050000000000000003D + World.land().HQ(((Tuple3d) (Bomb.P)).x, ((Tuple3d) (Bomb.P)).y))
        {
            drawing(false);
            Explosions.torpedoEnter_Water(AerialMine.P, 4F, 1.0F);
            destroy();
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Did Not Arm! ");
        }
        if(chute != null && ((Tuple3d) (Bomb.P)).z <= 0.050000000000000003D + World.land().HQ(((Tuple3d) (Bomb.P)).x, ((Tuple3d) (Bomb.P)).y))
        {
           // setMesh("3DO/Arms/LMB/Open/mono.sim");
            drawing(false);
        }
    }

    private Chute chute;
    private boolean bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float ttcurTM;
    private float ttexpTM;
    private float openHeight;
    private float minHeight;

    static 
    {
        Class class1 = com.maddox.il2.objects.weapons.BombMine_LMB.class;
        Property.set(class1, "mesh", "3DO/Arms/LMB/mono.sim");
        Property.set(class1, "radius", 60F);
        Property.set(class1, "power", 700F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.66F);
        Property.set(class1, "massa", 965F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 0.0F);
        Property.set(class1, "traveltime", 86400F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 10F);
        Property.set(class1, "impactAngleMax", 80F);
        Property.set(class1, "impactSpeed", 88F);
        Property.set(class1, "armingTime", 0.0F);
        Property.set(class1, "dropMaxAltitude", 400F);
        Property.set(class1, "dropMinAltitude", 70F);        
        Property.set(class1, "dropSpeed", 354F);
        Property.set(class1, "fuze", new Object[] { Fuze_generic_Delay.class });
        
    }
}