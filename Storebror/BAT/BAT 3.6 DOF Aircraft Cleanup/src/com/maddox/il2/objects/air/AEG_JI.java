package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.engine.Actor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class AEG_JI extends GermanBi {

    public AEG_JI() {
    }

    public void doWoundPilot(int i, float f) {
        switch (i) {
            case 1:
                this.FM.turret[0].setHealth(f);
                this.FM.turret[1].setHealth(f);
                this.FM.turret[2].setHealth(f);
                this.FM.turret[3].setHealth(f);
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

            case 3:
                this.hierMesh().chunkVisible("Pilot4_D0", false);
                this.hierMesh().chunkVisible("Pilot4_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore4_D0", true);
                }
                break;

            case 4:
                this.hierMesh().chunkVisible("Pilot5_D0", false);
                this.hierMesh().chunkVisible("Pilot5_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    this.hierMesh().chunkVisible("Gore5_D0", true);
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
            AEG_JI.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            AEG_JI.bChangedPit = true;
        }
    }

    public void doKillPilot(int i) {
        switch (i) {
            case 1:
                this.FM.turret[0].bIsOperable = false;
                this.FM.turret[1].bIsOperable = false;
                this.FM.turret[2].bIsOperable = false;
                this.FM.turret[3].bIsOperable = false;
                break;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equals("2Spandau+1MG14+4xPuW12_5")) {
            this.hierMesh().chunkVisible("Turret1A_D0", true);
            this.hierMesh().chunkVisible("Turret1B_D0", true);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", true);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", true);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", true);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1MG14+4xPuW50")) {
            this.hierMesh().chunkVisible("Turret1A_D0", true);
            this.hierMesh().chunkVisible("Turret1B_D0", true);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", true);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", true);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", true);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1MG14+4xPuW12_5Inc")) {
            this.hierMesh().chunkVisible("Turret1A_D0", true);
            this.hierMesh().chunkVisible("Turret1B_D0", true);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", true);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", true);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", true);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1MG14+4xPuW12_5Sturm")) {
            this.hierMesh().chunkVisible("Turret1A_D0", true);
            this.hierMesh().chunkVisible("Turret1B_D0", true);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", true);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", true);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", true);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", true);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Gast1918+4xPuW12_5")) {
            this.hierMesh().chunkVisible("Turret2A_D0", true);
            this.hierMesh().chunkVisible("Turret2B_D0", true);
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", true);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", true);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", true);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", true);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Gast1918+4xPuW50")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", true);
            this.hierMesh().chunkVisible("Turret2B_D0", true);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", true);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", true);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", true);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", true);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Gast1918+4xPuW12_5Inc")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", true);
            this.hierMesh().chunkVisible("Turret2B_D0", true);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", true);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", true);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", true);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", true);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Gast1918+4xPuW12_5Sturm")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", true);
            this.hierMesh().chunkVisible("Turret2B_D0", true);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", true);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", true);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", true);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", true);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Becker20mm+4xPuW12_5")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", true);
            this.hierMesh().chunkVisible("Turret3B_D0", true);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", true);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", true);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", true);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Becker20mm+4xPuW50")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", true);
            this.hierMesh().chunkVisible("Turret3B_D0", true);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", true);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", true);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", true);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Becker20mm+4xPuW12_5Inc")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", true);
            this.hierMesh().chunkVisible("Turret3B_D0", true);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", true);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", true);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", true);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Becker20mm+4xPuW12_5Sturm")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", true);
            this.hierMesh().chunkVisible("Turret3B_D0", true);
            this.hierMesh().chunkVisible("Turret4A_D0", false);
            this.hierMesh().chunkVisible("Turret4B_D0", false);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", true);
            this.hierMesh().chunkVisible("TMountBergman", false);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", true);
            this.hierMesh().chunkVisible("Pilot5_D0", false);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", true);
            this.hierMesh().chunkVisible("TgunBergman", false);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Bergman(Drum200)+4xPuW12_5St+Stgr")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", true);
            this.hierMesh().chunkVisible("Turret4B_D0", true);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", true);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", true);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", true);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", false);
            this.hierMesh().chunkVisible("TDrumBergman", true);
            return;
        }
        if (this.thisWeaponsName.equals("2Spandau+1Bergman(Box50)+4xPuW12_5Inc+Stgr")) {
            this.hierMesh().chunkVisible("Turret1A_D0", false);
            this.hierMesh().chunkVisible("Turret1B_D0", false);
            this.hierMesh().chunkVisible("Turret2A_D0", false);
            this.hierMesh().chunkVisible("Turret2B_D0", false);
            this.hierMesh().chunkVisible("Turret3A_D0", false);
            this.hierMesh().chunkVisible("Turret3B_D0", false);
            this.hierMesh().chunkVisible("Turret4A_D0", true);
            this.hierMesh().chunkVisible("Turret4B_D0", true);
            this.hierMesh().chunkVisible("TMountMG14", false);
            this.hierMesh().chunkVisible("TMountGast", false);
            this.hierMesh().chunkVisible("TMountBecker", false);
            this.hierMesh().chunkVisible("TMountBergman", true);
            this.hierMesh().chunkVisible("Pilot2_D0", false);
            this.hierMesh().chunkVisible("Pilot3_D0", false);
            this.hierMesh().chunkVisible("Pilot4_D0", false);
            this.hierMesh().chunkVisible("Pilot5_D0", true);
            this.hierMesh().chunkVisible("TgunMG14Late", false);
            this.hierMesh().chunkVisible("TgunGast", false);
            this.hierMesh().chunkVisible("TgunBecker", false);
            this.hierMesh().chunkVisible("TgunBergman", true);
            this.hierMesh().chunkVisible("TDrum200MG14", false);
            this.hierMesh().chunkVisible("TDrumGast", false);
            this.hierMesh().chunkVisible("TBoxBergman", true);
            this.hierMesh().chunkVisible("TDrumBergman", false);
            return;
        } else {
            return;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = AEG_JI.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "AEGJI");
        Property.set(class1, "meshName", "3do/plane/AEG-J/hierJI.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1925F);
        Property.set(class1, "FlightModel", "FlightModels/AEGJI.fmd:AEGJ");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAEG_CIV.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 10, 11, 11, 12, 13, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25" });
    }
}
