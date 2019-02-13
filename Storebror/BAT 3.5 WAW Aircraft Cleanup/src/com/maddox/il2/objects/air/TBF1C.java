package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class TBF1C extends TBF {

    public TBF1C() {
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
            bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            bChangedPit = true;
        }
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        float f = -af[0];
        float f1 = af[1];
        switch (i) {
            default:
                break;

            case 0: // '\0'
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

            case 1: // '\001'
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
            case 2: // '\002'
                this.FM.turret[0].setHealth(f);
                this.FM.turret[1].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0: // '\0'
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;

            case 1: // '\001'
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;

            case 2: // '\002'
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
        Class class1 = TBF1C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "TBF");
        Property.set(class1, "meshName", "3DO/Plane/TBF-1C(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar01());
        Property.set(class1, "meshName_us", "3DO/Plane/TBF-1C(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar01());
        Property.set(class1, "meshName_gb", "3DO/Plane/TBF-1C(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeBCSPar01());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1946.5F);
        Property.set(class1, "FlightModel", "FlightModels/TBF-1C.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitTBX1.class, CockpitTBXF1_Bombardier.class, CockpitTBX1_TGunner.class, CockpitTBX1_BGunner.class });
        weaponTriggersRegister(class1, new int[] { 0, 0, 10, 11, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07" });
        weaponsRegister(class1, "default", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
        weaponsRegister(class1, "2x20FragClusters", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, null, null, null, null, "BombGunM26A2 1", "BombGunM26A2 1" });
        weaponsRegister(class1, "4x20FragClusters", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, null, null, "BombGunM26A2 1", "BombGunM26A2 1", "BombGunM26A2 1", "BombGunM26A2 1" });
        weaponsRegister(class1, "8xhvargp", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", null, null, null, null, null, null, null });
        weaponsRegister(class1, "8xhvarap", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", null, null, null, null, null, null, null });
        weaponsRegister(class1, "4x100", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, null, null, "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1" });
        weaponsRegister(class1, "4x1008xhvargp", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", null, null, null, "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1" });
        weaponsRegister(class1, "4x1008xhvarap", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", null, null, null, "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1", "BombGun100lbs 1" });
        weaponsRegister(class1, "2x250", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null });
        weaponsRegister(class1, "2x2508xhvargp", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null });
        weaponsRegister(class1, "2x2508xhvarap", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", null, "BombGun250lbs 1", "BombGun250lbs 1", null, null, null, null });
        weaponsRegister(class1, "4x250", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, null, null, "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1", "BombGun250lbs 1" });
        weaponsRegister(class1, "2x500", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null });
        weaponsRegister(class1, "2x5008xhvargp", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", "RocketGunHVAR5", null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null });
        weaponsRegister(class1, "2x5008xhvarap", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", "RocketGunHVAR5AP", null, "BombGun500lbs 1", "BombGun500lbs 1", null, null, null, null });
        weaponsRegister(class1, "4x500", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, null, null, "BombGun500lbs 1", "BombGun500lbs 1", "BombGun500lbs 1", "BombGun500lbs 1" });
        weaponsRegister(class1, "2x1000", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, null, "BombGun1000lbs 1", "BombGun1000lbs 1", null, null, null, null });
        weaponsRegister(class1, "1x1600", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, "BombGun1600lbs", null, null, null, null, null, null });
        weaponsRegister(class1, "1x2000", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, "BombGun2000lbs", null, null, null, null, null, null });
        weaponsRegister(class1, "1xmk13", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, "BombGunTorpMk13a", null, null, null, null, null, null });
        weaponsRegister(class1, "1xmk13_late", new String[] { "MGunBrowning50kWF 600", "MGunBrowning50kWF 600", "MGunBrowning50t 400", "MGunBrowning303t 500", null, null, null, null, null, null, null, null, "BombGunTorpMk13a_late", null, null, null, null, null, null });
        weaponsRegister(class1, "none", new String[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null });
    }
}
