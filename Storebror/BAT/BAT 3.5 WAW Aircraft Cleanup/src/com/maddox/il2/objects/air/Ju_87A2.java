package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class Ju_87A2 extends JU_87 {

    public Ju_87A2() {
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if ((i == 36) || (i == 37) || (i == 10)) {
            this.hierMesh().chunkVisible("GearR3_D0", false);
            this.hierMesh().chunkVisible("GearR3Rot_D0", false);
            this.bDynamoOperational = false;
        }
        return super.cutFM(i, j, actor);
    }

    protected void moveFan(float f) {
        if (this.bDynamoOperational) {
            this.pk = Math.abs((int) (this.FM.Vwld.length() / 14D));
            if (this.pk >= 1) {
                this.pk = 1;
            }
        }
        if (this.bDynamoRotary != (this.pk == 1)) {
            this.bDynamoRotary = this.pk == 1;
            this.hierMesh().chunkVisible("GearR3_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("GearR3Rot_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, 80F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, 80F * f, 0.0F);
    }

    public void moveCockpitDoor(float f) {
        this.hierMesh().chunkSetAngles("BlisterA1_D0", 0.0F, 100F * f, 0.0F);
        this.hierMesh().chunkSetAngles("BlisterA2_D0", 0.0F, 100F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void update(float f) {
        for (int i = 1; i < 9; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -15F * this.FM.EI.engines[0].getControlRadiator(), 0.0F);
        }

        super.update(f);
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("1xSC500")) {
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot2_D1", false);
            this.hierMesh().chunkVisible("BlisterA2_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("BlisterA3_D0", true);
            return;
        } else {
            return;
        }
    }

    private boolean bDynamoOperational;
    private float   dynamoOrient;
    private boolean bDynamoRotary;
    private int     pk;

    static {
        Class class1 = Ju_87A2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju87A");
        Property.set(class1, "meshName", "3do/Plane/Ju-87B-2/hierA2.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "yearService", 1934F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-87A-2.fmd:Ju87A_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_87B2.class, CockpitJU_87B2_Gunner.class });
        Property.set(class1, "LOSElevation", 0.742F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05" });
    }
}
