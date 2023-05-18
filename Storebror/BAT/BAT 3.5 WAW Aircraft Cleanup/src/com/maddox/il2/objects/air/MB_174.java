package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Vector3d;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class MB_174 extends MB_170 implements TypeBomber, TypeStormovik {

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.75F;
    }

    public MB_174() {
    }

    public boolean hasCourseWeaponBullets() {
        return (this.FM.CT.Weapons[0] != null) && (this.FM.CT.Weapons[0][0] != null) && (this.FM.CT.Weapons[0][0].countBullets() != 0);
    }

    public boolean hasSlantedWeaponBullets() {
        if (this.hasJazzMusic) {
            return ((this.FM.CT.Weapons[1] != null) && (this.FM.CT.Weapons[1][0] != null) && (this.FM.CT.Weapons[1][1] != null) && (this.FM.CT.Weapons[1][0].countBullets() != 0)) || (this.FM.CT.Weapons[1][1].countBullets() != 0);
        } else {
            return false;
        }
    }

    public Vector3d getAttackVector() {
        return MB_174.ATTACK_VECTOR;
    }

    public void typeBomberAdjAltitudeMinus() {
    }

    public void typeBomberAdjAltitudePlus() {
    }

    public void typeBomberAdjAltitudeReset() {
    }

    public void typeBomberAdjDistanceMinus() {
    }

    public void typeBomberAdjDistancePlus() {
    }

    public void typeBomberAdjDistanceReset() {
    }

    public void typeBomberAdjSideslipMinus() {
    }

    public void typeBomberAdjSideslipPlus() {
    }

    public void typeBomberAdjSideslipReset() {
    }

    public void typeBomberAdjSpeedMinus() {
    }

    public void typeBomberAdjSpeedPlus() {
    }

    public void typeBomberAdjSpeedReset() {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public boolean typeBomberToggleAutomation() {
        return false;
    }

    public void typeBomberUpdate(float f1) {
    }

    public float                  fSightCurForwardAngle;
    public float                  fSightCurSideslip;
    public float                  fSightCurReadyness;
    private static final Vector3d ATTACK_VECTOR = new Vector3d(-190D, 0.0D, -300D);

    static {
        Class class1 = MB_174.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "MB174");
        Property.set(class1, "meshName", "3do/plane/MB-174(Multi1)/KAIKO_hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_fr", "3do/plane/MB-174(fr)/KAIKO_hier.him");
        Property.set(class1, "PaintScheme_fr", new PaintSchemeFCSPar05());
        Property.set(class1, "meshName_vi", "3do/plane/MB-174(vi)/KAIKO_hier.him");
        Property.set(class1, "PaintScheme_vi", new PaintSchemeFCSPar05());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/MB_174.fmd:MB_174_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMB_174.class, CockpitMB_174_Bombardier.class, CockpitMB_174_Gunner.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 0, 0, 0, 0, 3, 3, 9, 9, 9, 9, 10 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN06", "_MGUN09", "_MGUN12", "_BombSpawn02", "_BombSpawn03", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_MGUN01" });
    }
}
