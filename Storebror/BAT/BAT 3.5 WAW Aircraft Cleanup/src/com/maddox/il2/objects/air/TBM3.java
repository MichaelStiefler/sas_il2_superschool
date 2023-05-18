package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class TBM3 extends TBF {

    public TBM3() {
        this.flapps = 0.0F;
    }

    protected void moveFlap(float f) {
        float f1 = -38F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f1, 0.0F);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TBM3.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            TBM3.bChangedPit = true;
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

    public void update(float f) {
        super.update(f);
        float f1 = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f1) > 0.01F) {
            this.flapps = f1;
            for (int i = 1; i < 9; i++) {
                this.hierMesh().chunkSetAngles("Cowflap" + i + "_D0", 0.0F, 22.2F * f1, 0.0F);
            }

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
    private float         flapps;

    static {
        Class class1 = TBM3.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "TBF");
        Property.set(class1, "meshName", "3DO/Plane/TBM-3(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/TBM-3(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/TBM-3.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTBX1.class, CockpitTBXF1_Bombardier.class, CockpitTBX1_TGunner.class, CockpitTBX1_BGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 11, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07" });
    }
}
