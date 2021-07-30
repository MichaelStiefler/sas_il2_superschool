package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.MsgExplosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class Fuze_Barometric extends Fuze {

    public Fuze_Barometric() {
        this.fuzeAlt = -1F;
    }

    public void rareAction(ActorPos paramActorPos, Vector3d paramVector3d) {
        if (this.getOwnerBomb() != null) {
            this.getOwnerBomb().pos.getRel(Bomb.P, Bomb.Or);
            this.fuzeAlt = this.getDetonationDelay();
            if (super.isFuzeArmed(paramActorPos, paramVector3d, null)) {
                double d1 = World.land().HQ(Bomb.P.x, Bomb.P.y);
                if (this.getOwnerBomb().pos.getAbsPoint().z <= (this.fuzeAlt - d1)) {
                    this.doExplosionAir();
                }
            }
        }
    }

    protected void doExplosionAir() {
        this.getOwnerBomb().pos.getTime(Time.current(), Fuze_Barometric.p);
        String k4 = String.valueOf(this.getOwnerBomb().pos.getRelPoint().z);
        double d = this.getDetonationDelay();
        double d1 = World.land().HQ(Bomb.P.x, Bomb.P.y);
        String k5 = String.valueOf(d - d1);
        String k6 = String.valueOf(d1);
        HUD.log(AircraftHotKeys.hudLogWeaponId, "bomb position  " + k4 + " " + k5 + " " + k6);
        Class localClass = this.getClass();
        float f1 = Property.floatValue(localClass, "power", 1000F);
        int i = Property.intValue(localClass, "powerType", 0);
        float f2 = Property.floatValue(localClass, "radius", 150F);
        MsgExplosion.send(null, null, Fuze_Barometric.p, this.getOwnerBomb().getOwner(), this.getOwnerBomb().M, f1, i, f2);
        Explosions.AirFlak(Fuze_Barometric.p, 0);
        this.getOwnerBomb().destroy();
    }

    protected float        fuzeAlt;
    private static Point3d p = new Point3d();

    static {
        Class localClass = Fuze_Barometric.class;
        Property.set(localClass, "type", 8);
        Property.set(localClass, "airTravelToArm", 0.0F);
        Property.set(localClass, "fixedDelay", new float[] { 100F, 300F, 500F, 600F, 700F, 800F, 900F, 1000F, 1100F, 1200F, 1300F, 1400F, 1500F, 1600F, 1700F, 1800F, 1900F, 2000F, 2100F, 2200F, 2300F, 2400F, 2500F, 2600F, 2700F, 2800F, 2900F, 3000F, 3200F, 3400F, 3600F, 3800F, 4000F, 4200F, 4400F, 4600F, 4800F, 5000F, 5500F, 6000F, 7000F, 8000F, 9000F, 10000F });
    }
}
