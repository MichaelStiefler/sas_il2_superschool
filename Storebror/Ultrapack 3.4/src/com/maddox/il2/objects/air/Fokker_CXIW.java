package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Fokker_CXIW extends Biplanexyz implements TypeSeaPlane, TypeStormovikArmored {

    public Fokker_CXIW() {
        this.tmpp = new Point3d();
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D0", false);
                this.hierMesh().chunkVisible("Blister2_D0", false);
            } else {
                this.hierMesh().chunkVisible("Blister1_D0", true);
                this.hierMesh().chunkVisible("Blister2_D0", true);
            }
        }
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Blister1_D1", false);
            }
            this.hierMesh().chunkVisible("Blister2_D1", false);
            this.hierMesh().chunkVisible("Blister1_D2", false);
            this.hierMesh().chunkVisible("Blister2_D3", false);
        }
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 40F * f, 0.0F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, -40F * f, 0.0F);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("FSteerL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("FSteerR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update1(float f) {
        this.update(f);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (this.FM.Gears.clpGearEff[i][j] != null) {
                    this.tmpp.set(this.FM.Gears.clpGearEff[i][j].pos.getAbsPoint());
                    this.tmpp.z = 0.01D;
                    this.FM.Gears.clpGearEff[i][j].pos.setAbs(this.tmpp);
                    this.FM.Gears.clpGearEff[i][j].pos.reset();
                }
            }

        }

    }

    private Point3d tmpp;

    static {
        Class class1 = Fokker_CXIW.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Fokker_CXIW");
        Property.set(class1, "meshName", "3DO/Plane/Fokker_CXIW/hierS.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1931F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/Fokker_CXIW.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFokker_CXIW.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03" });
    }
}
