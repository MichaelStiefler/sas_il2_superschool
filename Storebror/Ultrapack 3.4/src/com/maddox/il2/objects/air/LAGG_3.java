package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public abstract class LAGG_3 extends Scheme1 implements TypeFighter {

    public LAGG_3() {
        this.kangle = 0.0F;
    }

    public void update(float f) {
        if (!(this instanceof LAGG_3RD)) this.hierMesh().chunkSetAngles("Water_luk", 0.0F, -17.5F + 17.5F * this.kangle, 0.0F);
        this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        super.update(f);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -80F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 100F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("Engine") && World.Rnd().nextFloat(0.0F, 1.0F) < shot.mass) this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("WingLMid") && World.Rnd().nextFloat() < 0.25F && shot.power > 7650F && shot.powerType == 3) this.FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int) (shot.mass * 100F)));
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat() < 0.25F && shot.power > 7650F && shot.powerType == 3) this.FM.AS.hitTank(shot.initiator, 1, World.Rnd().nextInt(1, (int) (shot.mass * 100F)));
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat() < 0.25F && shot.power > 7650F && shot.powerType == 3) this.FM.AS.hitTank(shot.initiator, 2, World.Rnd().nextInt(1, (int) (shot.mass * 100F)));
        if (shot.chunkName.startsWith("WingRMid") && World.Rnd().nextFloat() < 0.25F && shot.power > 7650F && shot.powerType == 3) this.FM.AS.hitTank(shot.initiator, 3, World.Rnd().nextInt(1, (int) (shot.mass * 100F)));
        if (shot.chunkName.startsWith("Pilot")) {
            if (v1.x > 0.86D) {
                if (shot.power * v1.x > 19200D) this.killPilot(shot.initiator, 0);
            } else if (World.Rnd().nextFloat(0.0F, 4600F) < shot.power) {
                this.killPilot(shot.initiator, 0);
                if (Pd.z > 0.5D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
            }
            shot.chunkName = "CF_D" + this.chunkDamageVisible("CF");
        }
        super.msgShot(shot);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 33:
                this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(3, 6));
                return super.cutFM(34, j, actor);

            case 36:
                this.FM.AS.hitTank(this, 2, World.Rnd().nextInt(3, 6));
                return super.cutFM(37, j, actor);

            case 19:
                if (World.Rnd().nextFloat() < 0.25F) {
                    int k = World.Rnd().nextInt(3, 6);
                    this.FM.AS.hitTank(this, 1, k);
                    this.FM.AS.hitTank(this, 2, k);
                }
                break;
        }
        return super.cutFM(i, j, actor);
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        if (s.startsWith("xpilot") || s.startsWith("xhead")) {
            byte byte0 = 0;
            int i;
            if (s.endsWith("a")) {
                byte0 = 1;
                i = s.charAt(6) - 49;
            } else if (s.endsWith("b")) {
                byte0 = 2;
                i = s.charAt(6) - 49;
            } else i = s.charAt(5) - 49;
            this.hitFlesh(i, shot, byte0);
        }
    }

    private float kangle;

    static {
        Class class1 = LAGG_3.class;
        Property.set(class1, "originCountry", PaintScheme.countryRussia);
    }
}
