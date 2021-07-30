package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class Hs126A_1 extends Hs126 {

    public Hs126A_1() {
        this.skiAngleL = 0.0F;
        this.skiAngleR = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && World.cur().camouflage == 1) {
            this.hierMesh().chunkVisible("GearLSki_D0", true);
            this.hierMesh().chunkVisible("GearRSki_D0", true);
            this.hierMesh().chunkVisible("CableLSki_D0", true);
            this.hierMesh().chunkVisible("CableRSki_D0", true);
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        float f1 = 13F;
        hiermesh.chunkSetAngles("GearLSki_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearRSki_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("CableLSki_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("CableRSki_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            boolean flag = false;
            float f1 = Aircraft.cvt(this.FM.getSpeed(), 30F, 80F, 1.0F, 0.0F);
            float f2 = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
            if (this.FM.Gears.gWheelSinking[0] > 0.0F) {
                flag = true;
                this.skiAngleL = 0.5F * this.skiAngleL + 0.5F * this.FM.Or.getTangage();
                this.skiAngleR = 0.5F * this.skiAngleR + 0.5F * this.FM.Or.getTangage();
                if (this.skiAngleL > 20F) this.skiAngleL -= this.spring;
                this.skiAngleR -= this.spring;
                this.hierMesh().chunkSetAngles("GearLSki_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleL, World.Rnd().nextFloat(f2, f2));
                this.hierMesh().chunkSetAngles("GearRSki_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleL, World.Rnd().nextFloat(f2, f2));
                this.hierMesh().chunkSetAngles("CableLSki_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleL, World.Rnd().nextFloat(f2, f2));
                this.hierMesh().chunkSetAngles("CableRSki_D0", World.Rnd().nextFloat(-f2, f2), World.Rnd().nextFloat(-f2 * 2.0F, f2 * 2.0F) - this.skiAngleL, World.Rnd().nextFloat(f2, f2));
            } else {
                if (this.skiAngleL > f1 * -10F + 0.01D) {
                    this.skiAngleL -= this.spring;
                    this.skiAngleR -= this.spring;
                    flag = true;
                } else if (this.skiAngleL < f1 * -10F - 0.01D) {
                    this.skiAngleL += this.spring;
                    this.skiAngleR += this.spring;
                    flag = true;
                }
                this.hierMesh().chunkSetAngles("GearLSki_D0", 0.0F, -this.skiAngleL, 0.0F);
                this.hierMesh().chunkSetAngles("GearRSki_D0", 0.0F, -this.skiAngleL, 0.0F);
                this.hierMesh().chunkSetAngles("CableLSki_D0", 0.0F, -this.skiAngleL, 0.0F);
                this.hierMesh().chunkSetAngles("CableRSki_D0", 0.0F, -this.skiAngleL, 0.0F);
            }
            if (!flag && f1 == 0.0F) {
                super.moveFan(f);
                return;
            }
        }
        super.moveFan(f);
    }

    private float skiAngleL;
    private float skiAngleR;
    private float spring;

    static {
        Class localClass = Hs126A_1.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "Hs126A_1");
        Property.set(localClass, "meshName", "3DO/Plane/Hs126A_1/hierC2.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(localClass, "meshName_de", "3DO/Plane/Hs126A_1/hierC2.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(localClass, "yearService", 1939F);
        Property.set(localClass, "yearExpired", 1943F);
        Property.set(localClass, "FlightModel", "FlightModels/Hs126.fmd");
        Property.set(localClass, "cockpitClass", new Class[] { CockpitHS126.class });
        Property.set(localClass, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(localClass,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10",
                        "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06",
                        "_ExternalDev07", "_ExternalDev08" });
    }
}
