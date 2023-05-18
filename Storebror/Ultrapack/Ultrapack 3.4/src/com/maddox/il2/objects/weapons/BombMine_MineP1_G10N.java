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

public class BombMine_MineP1_G10N extends AerialMine2 implements BombParaInfluenceMine2 {

    public BombMine_MineP1_G10N() {
        this.chute = null;
        this.bOnChute = false;
    }

    public void start() {
        super.start();
        this.ttcurTM = 1.5F;
        this.openHeight = 400F;
        this.minHeight = 100F;
    }

    public void destroy() {
        if (this.chute != null) {
            this.chute.destroy();
        }
        super.destroy();
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if ((actor instanceof ActorLand) && (this.chute != null)) {
            this.bOnChute = false;
        }
        this.ttcurTM = 100000F;
        if (this.chute != null) {
            this.chute.landing();
        }
        super.msgCollision(actor, s, s1);
    }

    public void interpolateTick() {
        super.curTm += Time.tickLenFs();
        super.interpolateTick();
        if (this.bOnChute) {
            this.getSpeed(BombMine_MineP1_G10N.v3d);
            BombMine_MineP1_G10N.v3d.scale(0.99D);
            if (BombMine_MineP1_G10N.v3d.z < -10D) {
                BombMine_MineP1_G10N.v3d.z += 1.1F * Time.tickConstLenFs();
            }
            this.setSpeed(BombMine_MineP1_G10N.v3d);
            super.pos.getAbs(Bomb.P, Bomb.Or);
        } else if ((super.curTm > this.ttcurTM) && (Bomb.P.z <= this.openHeight) && (Bomb.P.z >= this.minHeight)) {
            this.bOnChute = true;
            this.chute = new Chute(this);
            this.chute.collide(false);
            this.chute.mesh().setScale(2.5F);
            this.chute.mesh().setFastShadowVisibility(2);
            this.chute.pos.setRel(new Point3d(1.0D, 0.0D, 0.0D), new Orient(0.0F, 90F, 0.0F));
        } else if ((super.curTm > this.ttcurTM) && (Bomb.P.z <= this.minHeight) && (Bomb.P.z > (0.05D + World.land().HQ(Bomb.P.x, Bomb.P.y)))) {
            this.bOnChute = false;
        }
        super.pos.getRel(Bomb.P, Bomb.Or);
        if ((this.chute == null) && (Bomb.P.z <= (0.05D + World.land().HQ(Bomb.P.x, Bomb.P.y)))) {
            this.drawing(false);
//          Explosions.torpedoEnter_Water(AerialMine.P, 4F, 1.0F);
            Explosions.Explode10Kg_Water(AerialMine2.P, 4F, 1.0F);
            this.destroy();
            HUD.log(AircraftHotKeys.hudLogWeaponId, "Mine Did Not Arm! ");
        }
        if ((this.chute != null) && (Bomb.P.z <= (0.05D + World.land().HQ(Bomb.P.x, Bomb.P.y)))) {
            this.drawing(false);
        }
    }

    private Chute           chute;
    private boolean         bOnChute;
    private static Vector3d v3d = new Vector3d();
    private float           ttcurTM;
    private float           openHeight;
    private float           minHeight;

    static {
        Class class1 = BombMine_MineP1_G10N.class;
        Property.set(class1, "mesh", "3DO/Arms/MineP1_G10N/mono.sim");
        Property.set(class1, "radius", 38.5F);
        Property.set(class1, "power", 400F);
        Property.set(class1, "powerType", 0);
        Property.set(class1, "kalibr", 0.66F);
        Property.set(class1, "massa", 848F);
        Property.set(class1, "sound", "weapon.torpedo");
        Property.set(class1, "velocity", 0.0F);
        Property.set(class1, "traveltime", 36000F);
        Property.set(class1, "startingspeed", 0.0F);
        Property.set(class1, "impactAngleMin", 10F);
        Property.set(class1, "impactAngleMax", 80F);
        Property.set(class1, "impactSpeed", 88F);
        Property.set(class1, "armingTime", 0.0F);
        Property.set(class1, "dropMaxAltitude", 950F);
        Property.set(class1, "dropMinAltitude", 60F);
        Property.set(class1, "dropSpeed", 296F);
    }
}
