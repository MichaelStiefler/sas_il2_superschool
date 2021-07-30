package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Orient;
import com.maddox.rts.Property;

public class BombDepthCharge450lbBarracuda extends Bomb {

    public void start() {
        super.start();
        float randomFactor = 0.1F;
        for (int i = 0; i < 3; i++)
            this.randomOrient[i] = World.Rnd().nextFloat(-randomFactor, randomFactor);
    }

    public void interpolateTick() {
        super.interpolateTick();
        this.pos.getAbs(this.or);
        this.dir.set(1.0F, 0.0F, 0.0F);
        this.or2.set(0F, 0F, 0F);
        this.or2.transform(this.dir);
        float randomFactor = 0.01F;
        for (int i = 0; i < 3; i++)
            this.randomOrient[i] += World.Rnd().nextFloat(-randomFactor, randomFactor);
        this.dir.add(this.randomOrient[0], this.randomOrient[1], this.randomOrient[2]);
        this.dir.normalize();
        this.or2.setAT0(this.dir);
        this.or.add(this.or2);
        this.or.wrap();
        this.pos.setAbs(this.or);

    }

    private float[]  randomOrient = new float[3];
    private Vector3f dir          = new Vector3f();
    private Orient   or           = new Orient();
    private Orient   or2          = new Orient();

    static {
        Class class1 = BombDepthCharge450lbBarracuda.class;
        Property.set(class1, "mesh", "3DO/Arms/Bomb_Depthcharge_450lb_Barracuda/mono.sim");
        Property.set(class1, "radius", 16F);
        Property.set(class1, "power", 135F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.43F);
        Property.set(class1, "massa", 206F);
        Property.set(class1, "sound", "weapon.bomb_mid");
        try {
            Class fuze1 = Class.forName("com.maddox.il2.objects.weapons.Fuze_M112");
            Property.set(class1, "fuze", new Object[] { fuze1 });
        } catch (Exception e) {}
    }
}
