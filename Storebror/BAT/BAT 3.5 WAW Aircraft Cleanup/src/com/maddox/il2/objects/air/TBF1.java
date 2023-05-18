package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;

public class TBF1 extends TBF {

    public TBF1() {
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLMid_D0", 0.0F, -100F * f, 0.0F);
        hiermesh.chunkSetAngles("WingRMid_D0", 0.0F, 100F * f, 0.0F);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TBF1.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TBF1.bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0:
                if (f1 > 89F) {
                    f1 = 89F;
                    flag = false;
                }
                if (f1 < -30F) {
                    f1 = -30F;
                    flag = false;
                }
                float f2 = Math.abs(f);
                if (f1 < Aircraft.cvt(f2, 137F, 180F, -1F, 46F)) {
                    f1 = Aircraft.cvt(f2, 137F, 180F, -1F, 46F);
                }
                break;

            case 1:
                if (f < -23F) {
                    f = -23F;
                    flag = false;
                }
                if (f > 39F) {
                    f = 39F;
                    flag = false;
                }
                if (f1 < -60F) {
                    f1 = -60F;
                    flag = false;
                }
                if (f1 > 31F) {
                    f1 = 31F;
                    flag = false;
                }
                break;
        }
        af[0] = -f;
        af[1] = f1;
        return flag;
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 2:
                this.FM.turret[0].setHealth(f);
                this.FM.turret[1].setHealth(f);
                break;
        }
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

            case 2:
                this.hierMesh().chunkVisible("HMask3_D0", false);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", this.hierMesh().isChunkVisible("Pilot3_D0"));
                this.hierMesh().chunkVisible("Pilot4_D1", this.hierMesh().isChunkVisible("Pilot4_D0"));
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                break;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasBayDoorControl = true;
        this.FM.CT.setTrimAileronControl(0.058F);
        this.FM.CT.setTrimElevatorControl(-0.1F);
        this.FM.CT.setTrimRudderControl(0.1F);
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = TBF1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "TBF");
        Property.set(class1, "meshName", "3DO/Plane/TBF-1(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/TBF-1(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar01());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/TBF-1C.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTBX1.class, CockpitTBXF1_Bombardier.class, CockpitTBX1_TGunner.class, CockpitTBX1_BGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 10, 11, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07" });
    }
}
