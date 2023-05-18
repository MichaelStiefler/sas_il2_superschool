package com.maddox.il2.objects.air;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.Property;

public class JU_88C2 extends JU_88Axx implements TypeFighter, TypeBNZFighter, TypeStormovik {

    public JU_88C2() {
        this.diveMechStage = 0;
        this.bNDives = false;
        this.bDropsBombs = false;
        this.needsToOpenBombays = false;
        this.fSightCurForwardAngle = 0.0F;
        this.fSightCurSideslip = 0.0F;
        this.fSightCurAltitude = 850F;
        this.fSightCurSpeed = 150F;
        this.fSightCurReadyness = 0.0F;
        this.fDiveRecoveryAlt = 850F;
        this.fDiveVelocity = 150F;
        this.fDiveAngle = 70F;
        this.iRust = 1;
        this.blisterRemoved = false;
        this.topBlisterRemoved = false;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        float f = 0.0F;
        float f1 = 0.0F;
        this.needsToOpenBombays = false;
        if (this.thisWeaponsName.endsWith("RustA")) {
            this.iRust = 1;
            if (this.thisWeaponsName.endsWith("2xTank900L_RustA")) f1 += 45F;
            if (this.thisWeaponsName.startsWith("28x")) this.needsToOpenBombays = true;
        } else if (this.thisWeaponsName.endsWith("RustB")) {
            this.iRust = 2;
            f1 += 20F;
            f += 900F;
            if (this.thisWeaponsName.endsWith("1xTank900L_RustB")) f1 += 45F;
            else if (this.thisWeaponsName.endsWith("2xTank900L_RustB")) f1 += 110F;
            if (this.thisWeaponsName.startsWith("10x")) this.needsToOpenBombays = true;
        } else if (this.thisWeaponsName.endsWith("RustC")) {
            this.iRust = 3;
            this.FM.CT.bHasBayDoors = false;
            f1 += 170F;
            if (this.thisWeaponsName.startsWith("1xSC1800")) {
                f += 1300F;
                f1 += 45F;
            } else if (this.thisWeaponsName.endsWith("1xTank900L_RustC")) {
                f1 += 110F;
                f += 1400F;
            } else {
                f1 += 45F;
                f += 1400F;
            }
        }
        float f2 = this.FM.M.fuel / this.FM.M.maxFuel;
        this.FM.M.fuel += f2 * f;
        this.FM.M.maxFuel += f;
        this.FM.M.massEmpty += f1;
    }

    private void doWreck(String s) {
        if (this.hierMesh().chunkFindCheck(s) != -1) {
            this.hierMesh().hideSubTrees(s);
            Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind(s));
            wreckage.collide(true);
            Vector3d vector3d = new Vector3d();
            vector3d.set(this.FM.Vwld);
            wreckage.setSpeed(vector3d);
        }
    }

    public void blisterRemoved(int i) {
        if (i < 4) {
            if (!this.topBlisterRemoved) {
                this.doWreck("BlisterTop_D0");
                this.hierMesh().chunkVisible("Turret2B_D0", false);
                this.hierMesh().chunkVisible("Turret3B_D0", false);
                this.topBlisterRemoved = true;
            }
        } else if (!this.blisterRemoved && i == 4) {
            this.doWreck("BlisterDown_D0");
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.blisterRemoved = true;
        }
    }

    protected void moveBayDoor(float f) {
        if (!this.needsToOpenBombays && !this.FM.isPlayers()) return;
        if (this.iRust != 3) {
            this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 87F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -86F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 86F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -87F * f, 0.0F);
        }
        if (this.iRust == 1) {
            this.hierMesh().chunkSetAngles("Bay5_D0", 0.0F, 85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay6_D0", 0.0F, -85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay7_D0", 0.0F, 85F * f, 0.0F);
            this.hierMesh().chunkSetAngles("Bay8_D0", 0.0F, -85F * f, 0.0F);
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 4; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    public void update(float f) {
        this.updateJU87D5(f);
        this.updateJU87(f);
        super.update(f);
        if (Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeed()) > 70F && this.FM.CT.getFlap() > 0.01D && this.FM.CT.FlapsControl != 0.0F) {
            this.FM.CT.FlapsControl = 0.0F;
            World.cur();
            if (this.FM.actor == World.getPlayerAircraft()) HUD.log("FlapsRaised");
        }
    }

    public void updateJU87(float f) {
        if (this == World.getPlayerAircraft() && this.FM instanceof RealFlightModel) if (((RealFlightModel) this.FM).isRealMode()) switch (this.diveMechStage) {
            case 0:
                if (this.bNDives && this.FM.CT.AirBrakeControl == 1.0F && this.FM.Loc.z > this.fDiveRecoveryAlt) {
                    this.diveMechStage++;
                    this.bNDives = false;
                } else this.bNDives = this.FM.CT.AirBrakeControl != 1.0F;
                break;

            case 1:
                this.FM.CT.setTrimElevatorControl(-0.25F);
                this.FM.CT.trimElevator = -0.25F;
                if (this.FM.CT.AirBrakeControl == 0.0F || this.FM.CT.saveWeaponControl[3] || this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0) {
                    if (this.FM.CT.AirBrakeControl == 0.0F) this.diveMechStage++;
                    if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].countBullets() == 0) this.diveMechStage++;
                }
                break;

            case 2:
                this.FM.CT.setTrimElevatorControl(0.45F);
                this.FM.CT.trimElevator = 0.45F;
                if (this.FM.CT.AirBrakeControl == 0.0F || this.FM.Or.getTangage() > 0.0F) this.diveMechStage++;
                break;

            case 3:
                this.FM.CT.setTrimElevatorControl(0.0F);
                this.FM.CT.trimElevator = 0.0F;
                this.diveMechStage = 0;
                break;
        }
        else {
            this.FM.CT.setTrimElevatorControl(0.0F);
            this.FM.CT.trimElevator = 0.0F;
        }
        if (this.bDropsBombs && this.FM.isTick(3, 0) && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets())
            this.FM.CT.WeaponControl[3] = true;
    }

    public void updateJU87D5(float f) {
        this.fDiveAngle = -this.FM.Or.getTangage();
        if (this.fDiveAngle > 89F) this.fDiveAngle = 89F;
        if (this.fDiveAngle < 10F) this.fDiveAngle = 10F;
    }

    protected void moveAirBrake(float f) {
        this.hierMesh().chunkSetAngles("Brake01_D0", 0.0F, -90F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Brake02_D0", 0.0F, -90F * f, 0.0F);
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
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                this.hierMesh().chunkVisible("HMask2_D0", false);
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                this.hierMesh().chunkVisible("HMask3_D0", false);
                break;

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                this.hierMesh().chunkVisible("HMask4_D0", false);
                break;
        }
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
    }

    public static boolean bChangedPit = false;
    private int           iRust;
    public boolean        blisterRemoved;
    public boolean        topBlisterRemoved;
    public int            diveMechStage;
    public boolean        bNDives;
    private boolean       bDropsBombs;
    private boolean       needsToOpenBombays;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;
    public float          fDiveRecoveryAlt;
    public float          fDiveVelocity;
    public float          fDiveAngle;

    static {
        Class class1 = JU_88C2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ju-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88C-2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88C-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88C2.class, CockpitJU_88C2_NGunner.class, CockpitJU_88C2_LGunner.class, CockpitJU_88C2_RGunner.class, CockpitJU_88C2_BGunner.class });
        Property.set(class1, "LOSElevation", 1.0976F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 13, 0, 0, 0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MG1701", "_MG1702", "_MG1703", "_MGFF01", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06",
                "_BombSpawn07", "_BombSpawn08", "_BombSpawn08a", "_BombSpawn09", "_BombSpawn09a", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn15a", "_BombSpawn16", "_BombSpawn16a" });
    }
}
