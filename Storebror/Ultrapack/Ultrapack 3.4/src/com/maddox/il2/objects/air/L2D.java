package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class L2D extends Scheme2 implements TypeTransport {

    public L2D() {
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -45F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 20F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 20F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, -120F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -120F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLOut") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(Aircraft.Pd.y) < 6D) this.FM.AS.hitTank(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("WingROut") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(Aircraft.Pd.y) < 6D) this.FM.AS.hitTank(shot.initiator, 3, 1);
        if (shot.chunkName.startsWith("WingLIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(Aircraft.Pd.y) < 1.94D) this.FM.AS.hitTank(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("WingRIn") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F && Math.abs(Aircraft.Pd.y) < 1.94D) this.FM.AS.hitTank(shot.initiator, 2, 1);
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("Engine2") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitEngine(shot.initiator, 1, 1);
        if (shot.chunkName.startsWith("Nose") && Aircraft.Pd.x > 4.9D && Aircraft.Pd.z > -0.09D && World.Rnd().nextFloat() < 0.1F) if (Aircraft.Pd.y > 0.0D) {
            this.killPilot(shot.initiator, 0);
            this.FM.setCapableOfBMP(false, shot.initiator);
        } else this.killPilot(shot.initiator, 1);
        if (this.FM.AS.astateEngineStates[0] > 2 && this.FM.AS.astateEngineStates[1] > 2 && World.Rnd().nextInt(0, 99) < 33) this.FM.setCapableOfBMP(false, shot.initiator);
        super.msgShot(shot);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            default:
                break;

            case 13:
                this.killPilot(this, 0);
                this.killPilot(this, 1);
                break;

            case 35:
                if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitTank(this, 1, World.Rnd().nextInt(2, 6));
                break;

            case 38:
                if (World.Rnd().nextFloat() < 0.25F) this.FM.AS.hitTank(this, 2, World.Rnd().nextInt(2, 6));
                break;
        }
        return super.cutFM(i, j, actor);
    }

    static {
        Class class1 = L2D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "L2D");
        Property.set(class1, "meshName", "3DO/Plane/L2D(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(class1, "meshName_ja", "3DO/Plane/L2D(ja)/hier.him");
        Property.set(class1, "PaintScheme_ja", new PaintSchemeBCSPar01());
        Property.set(class1, "originCountry", PaintScheme.countryJapan);
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 2999.9F);
        Property.set(class1, "FlightModel", "FlightModels/DC-3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitC47.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
    }
}
