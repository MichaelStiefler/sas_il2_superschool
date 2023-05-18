package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.Property;

public class JU_88C6b extends JU_88NEW implements TypeFighter, TypeBNZFighter {

    public JU_88C6b() {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) bChangedPit = true;
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
        }
    }

    public void moveSteering(float f) {
        this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++)
            if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            else this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    protected void moveBayDoor(float f) {
        this.hierMesh().chunkSetAngles("Bay1_D0", 0.0F, 87F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay2_D0", 0.0F, -86F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay3_D0", 0.0F, 86F * f, 0.0F);
        this.hierMesh().chunkSetAngles("Bay4_D0", 0.0F, -87F * f, 0.0F);
    }

    public static boolean bChangedPit = false;
    public int            diveMechStage;
    public boolean        bNDives;
    public float          fSightCurForwardAngle;
    public float          fSightCurSideslip;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;
    public float          fSightCurReadyness;
    public float          fDiveRecoveryAlt;
    public float          fDiveVelocity;
    public float          fDiveAngle;
    protected float       fAOB;
    protected float       fShipSpeed;
    protected int         spreadAngle;

    static {
        Class class1 = JU_88C6b.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "JU-88");
        Property.set(class1, "meshName", "3DO/Plane/Ju-88C-6b(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Ju-88C-6mod.fmd");
        Property.set(class1, "LOSElevation", 1.0976F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU_88C6b.class, CockpitJU_88C6b_RGunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 0, 0, 0, 1, 1, 1, 3, 3, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN03", "_MG1701", "_MG1702", "_MG1703", "_MGFF01", "_MGFF02", "_MGFF03", "_BombSpawn01", "_BombSpawn02", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04" });
    }
}
