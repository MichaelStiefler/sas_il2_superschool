package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Dewoitine520 extends D_520 implements TypeBNZFighter {

    public Dewoitine520() {
        this.kangle = 0.0F;
    }

    protected void moveFan(float f) {
        int i = 0;
        for (int j = 0; j < this.FM.EI.getNum(); j++) {
            if (this.oldProp[j] < 2) {
                i = Math.abs((int) (this.FM.EI.engines[j].getw() * 0.06F));
                if (i >= 1) i = 1;
                if (i != this.oldProp[j] && this.hierMesh().isChunkVisible(Aircraft.Props[j][this.oldProp[j]])) {
                    this.hierMesh().chunkVisible(Aircraft.Props[j][this.oldProp[j]], false);
                    this.oldProp[j] = i;
                    this.hierMesh().chunkVisible(Aircraft.Props[j][i], true);
                }
            }
            if (i == 0) this.propPos[j] = (this.propPos[j] + 57.3F * this.FM.EI.engines[j].getw() * f) % 360F;
            else {
                float f1 = 57.3F * this.FM.EI.engines[j].getw();
                f1 %= 2880F;
                f1 /= 2880F;
                if (f1 <= 0.5F) f1 *= 2.0F;
                else f1 = f1 * 2.0F - 2.0F;
                f1 *= 1200F;
                this.propPos[j] = (this.propPos[j] + f1 * f) % 360F;
            }
            this.hierMesh().chunkSetAngles(Aircraft.Props[j][0], 0.0F, this.propPos[j], 0.0F);
        }

    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("Radiatorb", 0.0F, -27F * this.kangle, 0.0F);
        this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        if (this.kangle > 1.0F) this.kangle = 1.0F;
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, -84.5F), 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, -3.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 84.5F), 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 3.5F), 0.0F);
        hiermesh.chunkSetAngles("Antenna2", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, 99F), 0.0F);
        hiermesh.chunkSetAngles("LandingLight", 0.0F, Aircraft.cvt(f, 0.0F, 0.99F, 0.0F, -90F), 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) return;
        else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, 0.514F);
        this.hierMesh().chunkSetLocate("Blister1_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetLocate("Blister1_D1", Aircraft.xyz, Aircraft.ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    private float kangle;

    static {
        Class class1 = Dewoitine520.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D520");
        Property.set(class1, "meshName", "3DO/Plane/Dewoitine520/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/D520.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitD_520.class });
        Property.set(class1, "LOSElevation", 0.1498F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01" });
    }
}
