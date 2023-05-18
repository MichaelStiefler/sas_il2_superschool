package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.rts.Property;

public class HurricaneMkIaFAC extends Hurricane implements TypeFighter {

    public void update(float f) {
        super.update(f);
        if (this == World.getPlayerAircraft() && this.FM.turret.length > 0 && this.FM.AS.astatePilotStates[1] < 90 && this.FM.turret[0].bIsAIControlled && (this.FM.getOverload() > 7F || this.FM.getOverload() < -0.7F)) Voice.speakRearGunShake();
    }

    public boolean turretAngles(int i, float af[]) {
        boolean flag = super.turretAngles(i, af);
        if (af[0] < -60F) {
            af[0] = -60F;
            flag = false;
        } else if (af[0] > 60F) {
            af[0] = 60F;
            flag = false;
        }
        float f = Math.abs(af[0]);
        if (f < 20F) {
            if (af[1] < -10F) {
                af[1] = -10F;
                flag = false;
            }
        } else if (af[1] < -15F) {
            af[1] = -15F;
            flag = false;
        }
        if (af[1] > 45F) {
            af[1] = 45F;
            flag = false;
        }
        if (!flag) return false;
        float f1 = af[1];
        if (f < 2.0F && f1 < 17F) return false;
        if (f1 > -5F) return true;
        if (f1 > -12F) {
            f1 += 12F;
            return f > 12F + f1 * 2.571429F;
        }
        f1 = -f1;
        return f > f1;
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                if (this.FM.turret.length == 0) return;
                this.FM.turret[0].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", this.hierMesh().isChunkVisible("Blister1_D0"));
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                break;
        }
        if (i == 1) {
            if (this.FM.turret == null) return;
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot2_D1", true);
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret1A_D1", true);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        for (int i = 1; i < 3; i++)
            if (this.FM.getAltitude() < 3000F) {
                if (this.hierMesh().chunkFindCheck("HMask" + i + "_D0") != -1) this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else if (this.hierMesh().chunkFindCheck("HMask" + i + "_D0") != -1) this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));

    }

    static {
        Class class1 = HurricaneMkIaFAC.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hurri");
        Property.set(class1, "meshName", "3DO/Plane/HurricaneMkIFAC(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar03());
        Property.set(class1, "yearService", 1938F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/HurricaneMkIaFAC.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHURRIFAC.class, CockpitHurricane_Gunner.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 10, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_ExternalBomb01" });
    }
}
