package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Color3f;
import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.air.Chute;
import com.maddox.rts.Property;

public class BombSAB3m extends FlareBomb {

    public void start() {
//        fuzeAlt = 2000F;
        super.start();
        this.ttcurTM = World.Rnd().nextFloat(1.5F, 1.75F);
    }

    protected void setChute() {
        this.chute = new Chute(this);
        this.chute.collide(false);
        this.chute.mesh().setScale(0.3F);
        this.chute.mesh().setFastShadowVisibility(2);
        this.chute.pos.setRel(new Point3d(0.5D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
    }

    static {
        Class class1 = BombSAB3m.class;
        Property.set(class1, "mesh", "3DO/Arms/SAB3m/mono.sim");
        Property.set(class1, "flareColor", new Color3f(1.0F, 0.9F, 0.5F));
        Property.set(class1, "flareSmoke", "3DO/Effects/Fireworks/ParaFlareSmoke.eff");
        Property.set(class1, "flareLen", 1000F);
        Property.set(class1, "flareBurnTime", 140F);
        Property.set(class1, "decentRate", 4.5F);
        Property.set(class1, "flareMax", 10F);
        Property.set(class1, "radius", 0.01F);
        Property.set(class1, "power", 0.0F);
        Property.set(class1, "powerType", 1);
        Property.set(class1, "kalibr", 0.096F);
        Property.set(class1, "massa", 5.1F);
        Property.set(class1, "sound", "weapon.bomb_phball");
//        Property.set(class1, "fuze", ((Object) (new Object[] {
//            com.maddox.il2.objects.weapons.Fuze_SABBarometric.class
//        })));
    }
}
