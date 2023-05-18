package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class BrandenburgC1s26Mod extends BrandenburgM {

    public BrandenburgC1s26Mod() {
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                this.FM.turret[1].setHealth(f);
                break;
        }
    }

    public void doMurderPilot(int i) {
        switch (i) {
            default:
                break;

            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore1_D0", true);
                }
                break;

            case 1:
                this.hierMesh().chunkVisible("Pilot2_D0", false);
                this.hierMesh().chunkVisible("Pilot2_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;

            case 2:
                this.hierMesh().chunkVisible("Pilot3_D0", false);
                this.hierMesh().chunkVisible("Pilot3_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore3_D0", true);
                }
                break;
        }
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberUpdate(float f) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            BrandenburgC1s26Mod.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            BrandenburgC1s26Mod.bChangedPit = true;
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                break;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("1Schwarzlose0712+4xWollersdorf18")) {
            this.hierMesh().chunkVisible("Tgun0712Slotted", false);
            this.hierMesh().chunkVisible("Tgun0712", true);
            this.hierMesh().chunkVisible("Tgun0716", false);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            return;
        }
        if (this.thisWeaponsName.equals("1Schwarzlose0712Slotted+4xSkoda15")) {
            this.hierMesh().chunkVisible("Tgun0712Slotted", true);
            this.hierMesh().chunkVisible("Tgun0712", false);
            this.hierMesh().chunkVisible("Tgun0716", false);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            return;
        }
        if (this.thisWeaponsName.equals("1Schwarzlose0716+4xSkoda15")) {
            this.hierMesh().chunkVisible("Tgun0712Slotted", false);
            this.hierMesh().chunkVisible("Tgun0712", false);
            this.hierMesh().chunkVisible("Tgun0716", true);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            return;
        } else {
            return;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = BrandenburgC1s26Mod.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "HansaC1");
        Property.set(class1, "meshName", "3DO/Plane/BrandenburgC1(P)/hier26Mod.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1915F);
        Property.set(class1, "yearExpired", 1923F);
        Property.set(class1, "FlightModel", "FlightModels/BrandenburgC1s26Mod.fmd:BrandenburgC1");
        Property.set(class1, "cockpitClass", new Class[] { CockpitBrandenburg.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_ExternalDev01", "_ExternalDev02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09" });
    }
}
