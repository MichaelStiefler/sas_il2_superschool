package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class Shavrov_2 extends Shavrov_X implements TypeSeaPlane, TypeFighter {

    public Shavrov_2() {
        Shavrov_2.bChangedPit = true;
        this.bDynamoOperational = true;
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Shavrov_2.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            Shavrov_2.bChangedPit = true;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
    }

    protected void moveFlap(float f1) {
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLMid") && (World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass)) {
            this.FM.AS.hitTank(shot.initiator, 0, (int) (1.0F + (shot.mass * 18.95F * 2.0F)));
        }
        if (shot.chunkName.startsWith("WingRMid") && (World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass)) {
            this.FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + (shot.mass * 18.95F * 2.0F)));
        }
        if (shot.chunkName.startsWith("Engine") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
            this.FM.setCapableOfBMP(false, shot.initiator);
        } else {
            if ((this.FM.AS.astateEngineStates[0] == 4) && (World.Rnd().nextInt(0, 99) < 33)) {
                this.FM.setCapableOfBMP(false, shot.initiator);
            }
            super.msgShot(shot);
        }
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
            this.hierMesh().chunkVisible("Gener_D0", !this.bDynamoRotary);
            this.hierMesh().chunkVisible("Generrot_D0", this.bDynamoRotary);
        }
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("Gener_D0", 0.0F, this.dynamoOrient, 0.0F);
        super.moveFan(f);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -29F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -35F * f, 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;
        }
    }

    public static boolean bChangedPit = false;
    private boolean       bDynamoOperational;
    private float         dynamoOrient;
    private boolean       bDynamoRotary;
    private int           pk;

    static {
        Class class1 = Shavrov_2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Shav");
        Property.set(class1, "meshName", "3DO/Plane/Shavrov_2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1917F);
        Property.set(class1, "yearExpired", 1962F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitShavrov_2.class });
        Property.set(class1, "FlightModel", "FlightModels/LodkaMR.fmd:LodkaMR");
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN00", "_MGUN01" });
    }
}
