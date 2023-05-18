package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Wing;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class BF_109CRP extends BF_109 {

    public BF_109CRP() {
        this.kangle = 0.0F;
        this.flapps = 0.0F;
        this.fMaxKMHSpeedForOpenCanopy = 250F;
        this.bHasBlister = true;
    }

    public void moveCockpitDoor(float f) {
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 80F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -32F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -20F * f, 0.0F);
    }

    public void update(float f) {
        if (this.FM.getSpeed() > 5F) {
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 11F, 0.0F, 1.5F), 0.0F);
        }
        if (this == World.getPlayerAircraft()) {
            World.cur().diffCur.Torque_N_Gyro_Effects = false;
        }
        if (Math.abs(this.flapps - this.kangle) > 0.01F) {
            this.flapps = this.kangle;
            this.hierMesh().chunkSetAngles("Flap01L_D0", 0.0F, -16F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap01U_D0", 0.0F, 16F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap02L_D0", 0.0F, -16F * this.kangle, 0.0F);
            this.hierMesh().chunkSetAngles("Flap02U_D0", 0.0F, 16F * this.kangle, 0.0F);
        }
        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        if (this.kangle > 1.0F) {
            this.kangle = 1.0F;
        }
        super.update(f);
        if ((this.FM.CT.getCockpitDoor() > 0.2F) && this.bHasBlister && (this.FM.getSpeedKMH() > this.fMaxKMHSpeedForOpenCanopy) && (this.hierMesh().chunkFindCheck("Blister1_D0") != -1)) {
            try {
                if (this == World.getPlayerAircraft()) {
                    ((CockpitBF_109W) Main3D.cur3D().cockpitCur).removeCanopy();
                }
            } catch (Exception exception) {
            }
            this.hierMesh().hideSubTrees("Blister1_D0");
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
            this.bHasBlister = false;
            this.FM.CT.bHasCockpitDoorControl = false;
            this.FM.setGCenter(-0.5F);
        }
    }

    public static void moveGearOld(HierMesh hiermesh, float f) {
        float f1 = 0.8F;
        float f2 = (-0.5F * (float) Math.cos((f / f1) * Math.PI)) + 0.5F;
        if ((f <= f1) || (f == 1.0F)) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -33.5F * f2, 0.0F, 0.0F);
        }
        f2 = (-0.5F * (float) Math.cos(((f - (1.0F - f1)) / f1) * Math.PI)) + 0.5F;
        if (f >= (1.0F - f1)) {
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f2, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 33.5F * f2, 0.0F, 0.0F);
        }
        hiermesh.chunkSetAngles("GearC3_D0", 70F * f, 0.0F, 0.0F);
        if (f > 0.99F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
        }
        if (f < 0.01F) {
            hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 0.0F, 0.0F);
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 0.0F, 0.0F);
        }
    }

    protected void moveGearOld(float f) {
        float f1 = 0.9F - (((Wing) this.getOwner()).aircIndex(this) * 0.1F);
        float f2 = (-0.5F * (float) Math.cos((f / f1) * Math.PI)) + 0.5F;
        if ((f <= f1) || (f == 1.0F)) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -77.5F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -33.5F * f2, 0.0F, 0.0F);
        }
        f2 = (-0.5F * (float) Math.cos(((f - (1.0F - f1)) / f1) * Math.PI)) + 0.5F;
        if (f >= (1.0F - f1)) {
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 77.5F * f2, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 33.5F * f2, 0.0F, 0.0F);
        }
        this.hierMesh().chunkSetAngles("GearC3_D0", 70F * f, 0.0F, 0.0F);
        if (f > 0.99F) {
            this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -77.5F, 0.0F);
            this.hierMesh().chunkSetAngles("GearL2_D0", -33.5F, 0.0F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, 77.5F, 0.0F);
            this.hierMesh().chunkSetAngles("GearR2_D0", 33.5F, 0.0F, 0.0F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, boolean flag) {
        if (flag) {
            hiermesh.chunkSetAngles("GearC3_D0", BF_109CRP.smoothCvt(f2, 0.725F, 0.925F, 0.0F, 70F), 0.0F, 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearC3_D0", BF_109CRP.smoothCvt(f2, 0.01F, 0.2F, 0.0F, 70F), 0.0F, 0.0F);
        }
        hiermesh.chunkSetAngles("GearL2_D0", BF_109CRP.smoothCvt(f, 0.01F, 0.6F, 0.0F, -33.5F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, BF_109CRP.smoothCvt(f, 0.01F, 0.6F, 0.0F, -77.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", BF_109CRP.smoothCvt(f1, 0.3F, 0.9F, 0.0F, 33.5F), 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, BF_109CRP.smoothCvt(f1, 0.3F, 0.9F, 0.0F, 77.5F), 0.0F);
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        BF_109CRP.moveGear(hiermesh, f, f1, f2, true);
    }

    protected void moveGear(float f, float f1, float f2) {
        BF_109CRP.moveGear(this.hierMesh(), f, f1, f2, this.FM.CT.GearControl > 0.5F);
    }

    public static void moveGear(HierMesh hiermesh, float f, boolean flag) {
        BF_109CRP.moveGear(hiermesh, f, f, f, flag);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        BF_109CRP.moveGear(hiermesh, f, f, f, true);
    }

    protected void moveGear(float f) {
        BF_109CRP.moveGear(this.hierMesh(), f, this.FM.CT.GearControl > 0.5F);
    }

    private static float smoothCvt(float f, float f1, float f2, float f3, float f4) {
        f = Math.min(Math.max(f, f1), f2);
        return f3 + ((f4 - f3) * ((-0.5F * (float) Math.cos(((f - f1) / (f2 - f1)) * Math.PI)) + 0.5F));
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() >= 0.98F) {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
        }
    }

    protected void moveFan(float f) {
        this.hierMesh().chunkFind(Aircraft.Props[1][0]);
        int i = 0;
        for (int j = 0; j < 2; j++) {
            if (this.oldProp[j] < 2) {
                i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
                if (i >= 1) {
                    i = 1;
                }
                if ((i != this.oldProp[j]) && this.hierMesh().isChunkVisible(Aircraft.Props[j][this.oldProp[j]])) {
                    this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
                    this.oldProp[j] = i;
                    this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if (i == 0) {
                this.propPos[j] = (this.propPos[j] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
            } else {
                float f1 = 57.3F * this.FM.EI.engines[0].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if (f1 <= 0.5F) {
                    f1 *= 2.0F;
                } else {
                    f1 = (f1 * 2.0F) - 2.0F;
                }
                f1 *= 1200F;
                this.propPos[j] = (this.propPos[j] + (f1 * f)) % 360F;
            }
            this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, this.propPos[j] * (j != 0 ? -1F : 1.0F), 0.0F);
        }

    }

    public void hitProp(int i, int j, Actor actor) {
        if ((i > (this.FM.EI.getNum() - 1)) || (this.oldProp[i] == 2)) {
            return;
        }
        if (this.isChunkAnyDamageVisible("Prop1") || this.isChunkAnyDamageVisible("PropRot1")) {
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
        }
        super.hitProp(i, j, actor);
    }

    private float  kangle;
    private float  flapps;
    private float  fMaxKMHSpeedForOpenCanopy;
    public boolean bHasBlister;

    static {
        Class class1 = BF_109CRP.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bf109CRP");
        Property.set(class1, "meshName", "3DO/Plane/Bf-109CRP/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Bf-109CRP.fmd:Bf109_ODDS_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBF_109CRP.class });
        Property.set(class1, "LOSElevation", 0.7498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_CANNON05", "_CANNON06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
