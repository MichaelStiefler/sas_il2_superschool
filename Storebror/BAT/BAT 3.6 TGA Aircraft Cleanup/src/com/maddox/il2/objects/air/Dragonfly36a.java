package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class Dragonfly36a extends Dragonfly36bis implements TypeFighter {

    public Dragonfly36a() {
        this.arrestor = 0.0F;
    }

    public float getEyeLevelCorrection() {
        return 0.2F;
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 60F * f, 0.0F);
        this.arrestor = f;
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -52F, 8F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-76F * this.FM.Gears.arrestorVSink) / 60F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
                }
                if (f2 > 0.2F) {
                    f2 = 0.2F;
                }
                if (f2 > 0.0F) {
                    this.arrestor = (0.7F * this.arrestor) + (0.3F * (this.arrestor + f2));
                } else {
                    this.arrestor = (0.3F * this.arrestor) + (0.7F * (this.arrestor + f2));
                }
                if (this.arrestor < 0.0F) {
                    this.arrestor = 0.0F;
                } else if (this.arrestor > 1.0F) {
                    this.arrestor = 1.0F;
                }
                this.moveArrestorHook(this.arrestor);
            }
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -88F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 70F * f2, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 93F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -93F * f1, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Math.max(-f * 1500F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -Math.max(-f1 * 1500F, -90F), 0.0F);
        float f3 = Math.max(-f2 * 1500F, -90F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f3, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, -f3, 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        Dragonfly36a.moveGear(this.hierMesh(), f, f1, f2);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if ((shot.chunkName.startsWith("CF") || shot.chunkName.startsWith("Tail")) && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 0, 5);
        }
        if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) {
            this.FM.AS.hitTank(shot.initiator, 1, 4);
        }
        if (shot.chunkName.startsWith("WingLIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 2, 4);
        }
        if (shot.chunkName.startsWith("WingRIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 3, 4);
        }
        if (shot.chunkName.startsWith("Engine1") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 5);
        }
        super.msgShot(shot);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                this.FM.AS.hitTank(this, 2, 7);
                return super.cutFM(34, j, actor);

            case 36:
                this.FM.AS.hitTank(this, 3, 7);
                return super.cutFM(37, j, actor);

            case 19:
                this.FM.CT.bHasArrestorControl = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    private float arrestor;

    static {
        Class class1 = Dragonfly36a.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly36");
        Property.set(class1, "meshName", "3DO/Plane/Dragonfly36bis/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1936F);
        Property.set(class1, "yearExpired", 1947F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly36a.fmd:Dragonfly36bis_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly36bis.class });
        Property.set(class1, "LOSElevation", 0.896F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02" });
    }
}
