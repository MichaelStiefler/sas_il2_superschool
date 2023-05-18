package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.rts.Property;

public class Dragonfly42a extends Dragonfly42bis implements TypeFighter {

    public Dragonfly42a() {
        this.arrestor = 0.0F;
        this.SecondProp = 0;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("droptank") && !this.thisWeaponsName.endsWith("ijn")) {
            this.hierMesh().chunkVisible("WPodL_D0", true);
            this.hierMesh().chunkVisible("WPodR_D0", true);
        }
    }

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, -30F * f, 0.0F);
        this.arrestor = f;
    }

    protected void moveFan(float f) {
        this.hierMesh().chunkFind(Aircraft.Props[1][0]);
        this.SecondProp = 1;
        int i = 0;
        for (int j = 0; j < (this.SecondProp == 1 ? 2 : 1); j++) {
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
            if (j == 0) {
                this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, this.propPos[j], 0.0F);
            } else {
                this.hierMesh().chunkSetAngles(Aircraft.Props[j][i], 0.0F, -(this.propPos[j] - 20F), 0.0F);
            }
        }

    }

    public void hitProp(int i, int j, Actor actor) {
        if ((i > (this.FM.EI.getNum() - 1)) || (this.oldProp[i] == 2)) {
            return;
        }
        if ((this.isChunkAnyDamageVisible("Prop" + (i + 1)) || this.isChunkAnyDamageVisible("PropRot" + (i + 1))) && (this.SecondProp == 1)) {
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][0], false);
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][1], false);
            this.hierMesh().chunkVisible(Aircraft.Props[i + 1][2], true);
        }
        super.hitProp(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        World.cur().diffCur.Torque_N_Gyro_Effects = false;
        if (this.FM.CT.getArrestor() > 0.2F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                float f1 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -26F, 11F, 1.0F, 0.0F);
                this.arrestor = (0.8F * this.arrestor) + (0.2F * f1);
                this.moveArrestorHook(this.arrestor);
            } else {
                float f2 = (-42F * this.FM.Gears.arrestorVSink) / 30F;
                if ((f2 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f2 > 0.0F) && (this.FM.CT.getArrestor() < 0.95F)) {
                    f2 = 0.0F;
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

    protected float arrestor;
    protected int   SecondProp;

    static {
        Class class1 = Dragonfly42a.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Dragonfly42");
        Property.set(class1, "meshName", "3DO/Plane/Dragonfly42bis/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1955F);
        Property.set(class1, "FlightModel", "FlightModels/Dragonfly42a.fmd:Dragonfly42bis_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDragonfly42bis.class });
        Property.set(class1, "LOSElevation", 0.9119F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON02", "_CANNON03", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
