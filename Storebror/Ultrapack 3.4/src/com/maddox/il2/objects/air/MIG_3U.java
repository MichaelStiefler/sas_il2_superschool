package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class MIG_3U extends MIG_3 {

    public MIG_3U() {
        this.kangle = 0.0F;
    }

    public void update(float f) {
        this.hierMesh().chunkSetAngles("FlapWater_D0", 0.0F, 0.0F, 40.5F * this.kangle);
        this.hierMesh().chunkSetAngles("FlapOil_D0", 0.0F, 0.0F, 12.5F * this.kangle);
        this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
        super.update(f);
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 70F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 93F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -93F * f, 0.0F);
        float f1 = Math.max(-f * 1500F, -90F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -f1, 0.0F);
        hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearC4_D0", 0.0F, -f1, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if ((shot.chunkName.startsWith("CF") || shot.chunkName.startsWith("Tail")) && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 0, 2);
        if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 2, 1);
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 3, 1);
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F) this.FM.AS.hitEngine(shot.initiator, 0, 2);
        super.msgShot(shot);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                this.FM.AS.hitTank(this, 2, 7);
                return super.cutFM(34, j, actor);

            case 36:
                this.FM.AS.hitTank(this, 3, 7);
                return super.cutFM(37, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    private float kangle;

    static {
        Class class1 = MIG_3U.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MiG");
        Property.set(class1, "meshName", "3DO/Plane/MIG-3U(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1943F);
        Property.set(class1, "FlightModel", "FlightModels/MiG-3U.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMIG_3U.class });
        Property.set(class1, "LOSElevation", 0.906F);
        weaponTriggersRegister(class1, new int[] { 0, 0 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
