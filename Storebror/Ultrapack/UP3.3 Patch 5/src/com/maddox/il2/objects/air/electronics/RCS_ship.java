package com.maddox.il2.objects.air.electronics;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.ShipGeneric;
import com.maddox.sas1946.il2.util.CommonTools;

public class RCS_ship {

    public RCS_ship() {
        this.R_2_lambda = 10F;
        this.SMALL_LOBES_SIZE = 1E-12F;
        this.SIDE_LOBE_SIZE = 10F;
        this.FRONT_LOBE_SIZE = 15F;
        this.REAR_LOBE_SIZE = 1E-12F;
        this.TOP_LOBE_SIZE = 1E-12F;
        this.RCS_DIVISION_FACTOR = 100000F;
    }

    protected float func(float f, float f1) {
        double d = Math.sin(Math.toRadians(f));
        d *= d;
        double d1 = Math.sin(Math.toRadians(f1)) * Math.cos(Math.toRadians(f));
        d1 *= d1;
        return ((float) Math.cos(Math.PI * this.R_2_lambda * (float) Math.sqrt(d + d1)) * f) / BEAM_SEPARATION;
    }

    protected float mult(float f, float f1) {
        float f2;
        if (Math.abs(f - BEAM_SEPARATION) < BEAM_WIDTH / 2F) {
            f2 = 1F - BEAM_WIDTH_FACTOR * (BEAM_SEPARATION - f) * (BEAM_SEPARATION - f);
            f2 = this.SMALL_LOBES_SIZE + (f2 * this.SIDE_LOBE_SIZE);
        } else if (f < BEAM_WIDTH / 2F) {
            f2 = 1F - BEAM_WIDTH_FACTOR * f * f;
            f2 = this.SMALL_LOBES_SIZE + (f2 * this.FRONT_LOBE_SIZE);
        } else if (f > 180F - BEAM_WIDTH / 2F) {
            f2 = 1F - BEAM_WIDTH_FACTOR * (180F - f) * (180F - f);
            f2 = this.SMALL_LOBES_SIZE + (f2 * this.REAR_LOBE_SIZE);
        } else {
            f2 = this.SMALL_LOBES_SIZE;
        }
        float f3 = 1.0F;
        if (f1 > 90F - BEAM_WIDTH / 2F) {
            f3 = 1F - BEAM_WIDTH_FACTOR * (90F - f) * (90F - f);
            f3 = this.SMALL_LOBES_SIZE + (f3 * this.TOP_LOBE_SIZE);
        } else {
            f3 = this.SMALL_LOBES_SIZE;
        }
        return Math.max(f2, f3);
    }

    protected float baseRCS(Orient orient) {
        float f1 = orient.getAzimut();
//        float f1 = orient.getYaw();
        float f = Math.abs(orient.getTangage());
        return this.baseRCS(f1, f);
    }

    protected float baseRCS(float f, float f1) {
        while (f < 0.0F) f += 360F;
        while (f > 360F) f -= 360F;
        if (f > 180F) f = 360F - f;
        float f3 = Math.abs(f - BEAM_SEPARATION);
        float f2 = Math.abs(this.func(f3, f1) + this.func(BEAM_SEPARATION - f3, f1));
        f2 *= this.mult(f, f1);
        return f2;
    }

    float getRCS(Actor actor, Orient orient) {
        float f = this.baseRCS(orient);
        f -= 7F;
        if (Actor.isValid(actor)) {
            float f1 = 0F;
            if (actor instanceof BigshipGeneric)
                f1=(float) Math.pow(((BigshipGeneric) actor).collisionR(), 2F) / this.RCS_DIVISION_FACTOR;
            else if (actor instanceof ShipGeneric)
                f1=(float) Math.pow(((ShipGeneric) actor).collisionR(), 2F) / this.RCS_DIVISION_FACTOR;
            float oa = orient.getAzimut();
            while (oa < 0.0F) oa += 360F;
            while (oa > 360F) oa -= 360F;
            if (oa > 180F) oa = 360F - oa;
            float oaa = Math.abs(oa);
            if (oaa > BEAM_SEPARATION) f1 = CommonTools.smoothCvt(oaa, BEAM_SEPARATION, BEAM_SEPARATION + BEAM_WIDTH / 2F, f1, 0F);
            f += (-3.053F / f1) + (f1 * 3.152F);
        }
        f += World.Rnd().nextFloat(-2.5F, 2.5F);
        return f;
    }
    
    private static final float BEAM_WIDTH = 30F;
    private static final float BEAM_SEPARATION = 20F;
    private static final float BEAM_WIDTH_FACTOR = 1F / (BEAM_WIDTH * BEAM_WIDTH);
    protected float R_2_lambda;
    protected float SMALL_LOBES_SIZE;
    protected float SIDE_LOBE_SIZE;
    protected float FRONT_LOBE_SIZE;
    protected float REAR_LOBE_SIZE;
    protected float TOP_LOBE_SIZE;
    protected float RCS_DIVISION_FACTOR;
}
