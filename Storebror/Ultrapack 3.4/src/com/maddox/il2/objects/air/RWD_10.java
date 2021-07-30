package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class RWD_10 extends Scheme1 implements TypeScout {

    public RWD_10() {
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("CF") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("Pilot")) this.FM.AS.hitPilot(shot.initiator, 0, 101);
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = Aircraft.cvt(this.FM.Gears.gWheelSinking[0], 0.0F, 0.25F, 0.0F, 0.35F);
        this.hierMesh().chunkSetAngles("GearL2_D0", 0.0F, 20F * f, 0.0F);
        Aircraft.xyz[2] = -0.21F * f;
        f = Aircraft.cvt(this.FM.Gears.gWheelSinking[1], 0.0F, 0.25F, 0.0F, 0.35F);
        this.hierMesh().chunkSetAngles("GearR2_D0", 0.0F, -20F * f, 0.0F);
        Aircraft.xyz[2] = -0.21F * f;
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -25F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1RodR_D0", -25F * f, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Rudder1RodL_D0", 0.0F, 25F * f, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorLRodV_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorLRodN_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorLRodR_D0", 0.0F, -20F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorRRodV_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorRRodN_D0", 0.0F, 30F * f, 0.0F);
        this.hierMesh().chunkSetAngles("VatorRRodR_D0", 0.0F, -20F * f, 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = RWD_10.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "RWD");
        Property.set(class1, "meshName", "3DO/Plane/RWD-10/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00());
        Property.set(class1, "originCountry", PaintScheme.countryPoland);
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1939.5F);
        Property.set(class1, "FlightModel", "FlightModels/RWD-10.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitRWD_10.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { null });
    }
}
