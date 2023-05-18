package com.maddox.il2.objects.air;

import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Bulldog_II extends ParasolX {

    public Bulldog_II() {
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
    }

    protected void moveFlap(float f) {
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, -45F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, -45F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f, 0.0F);
    }

    public void update(float f) {
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        Aircraft.xyz[0] = 0.0F;
        Aircraft.xyz[1] = 0.0F;
        Aircraft.xyz[2] = f1 * -0.45F;
        Aircraft.ypr[0] = 0.0F;
        Aircraft.ypr[1] = 0.0F;
        Aircraft.ypr[2] = 0.0F;
        this.hierMesh().chunkSetLocate("Water_D0", Aircraft.xyz, Aircraft.ypr);
        super.update(f);
        this.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            if (!Main3D.cur3D().isViewOutside()) {
                this.hierMesh().chunkVisible("Goertz_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Blister_D0", false);
            } else {
                this.hierMesh().chunkVisible("Goertz_D0", true);
                this.hierMesh().chunkVisible("Head1_D0", true);
                this.hierMesh().chunkVisible("Pilot1_D0", true);
                this.hierMesh().chunkVisible("Blister_D0", true);
            }
        }
        if (this.FM.isPlayers() && !Main3D.cur3D().isViewOutside()) {
            this.hierMesh().chunkVisible("Goertz_D0", false);
            this.hierMesh().chunkVisible("Head1_D0", false);
            this.hierMesh().chunkVisible("Pilot1_D0", false);
            this.hierMesh().chunkVisible("Pilot1_D1", false);
            this.hierMesh().chunkVisible("Blister_D0", false);
        }
    }

    static {
        Class class1 = Bulldog_II.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Bulldog_II");
        Property.set(class1, "meshName", "3DO/Plane/Bulldog_II/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "meshName_it", "3DO/Plane/Bulldog_II/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1925F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/Bulldog_II.fmd:Parasol2_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBulldog_II.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb6", "_ExternalBomb7", "_ExternalBomb8", "_ExternalBomb9", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12" });
    }
}
