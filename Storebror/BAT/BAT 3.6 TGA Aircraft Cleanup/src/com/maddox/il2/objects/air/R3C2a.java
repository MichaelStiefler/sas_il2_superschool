package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class R3C2a extends U_SYP implements TypeAmphibiousPlane, TypeStormovik {

    public R3C2a() {
        R3C2a.bChangedPit = true;
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            R3C2a.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            R3C2a.bChangedPit = true;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
    }

    protected void moveFlap(float f) {
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

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -29F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1RodR_D0", -29F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1RodL_D0", 0.0F, 29F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorLRodV_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorLRodN_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorLRodR_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorRRodV_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorRRodN_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorRRodR_D0", 0.0F, -35F * f, 0.0F);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLn_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneLrod_D0", 0.0F, -35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRn_D0", 0.0F, 35F * f, 0.0F);
        this.hierMesh().chunkSetAngles("AroneRrod_D0", 0.0F, -35F * f, 0.0F);
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

    static {
        Class class1 = R3C2a.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "R3-C2a");
        Property.set(class1, "meshName", "3DO/Plane/R3C2a(Multi)/hierG5.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1917F);
        Property.set(class1, "yearExpired", 1927F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitG50.class });
        Property.set(class1, "FlightModel", "FlightModels/R3C2a.fmd:R3C2a_FM");
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN00", "_MGUN01", "_BombSpawn01" });
    }
}
