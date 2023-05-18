package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Me_262_HGIV extends ME_262 implements TypeX4Carrier, TypeStormovik, TypeFighterAceMaker {

    public Me_262_HGIV() {
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.X4check = false;
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200F;
    }

    public void onAircraftLoaded() {
    }

    public void typeX4CAdjSidePlus() {
        this.deltaAzimuth = 1.0F;
        if (this.X4check) {
            this.X4check = false;
        } else if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            this.typeFighterAceMakerAdjSideslipPlus();
        }
    }

    public void typeX4CAdjSideMinus() {
        this.deltaAzimuth = -1F;
        if (this.X4check) {
            this.X4check = false;
        } else if ((this.FM instanceof RealFlightModel) && ((RealFlightModel) this.FM).isRealMode()) {
            this.typeFighterAceMakerAdjSideslipMinus();
        }
    }

    public void typeX4CAdjAttitudePlus() {
        this.deltaTangage = 1.0F;
    }

    public void typeX4CAdjAttitudeMinus() {
        this.deltaTangage = -1F;
    }

    public void typeX4CResetControls() {
        this.deltaAzimuth = this.deltaTangage = 0.0F;
        this.X4check = true;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode++;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    public void typeFighterAceMakerAdjDistancePlus() {
        this.k14Distance += 10F;
        if (this.k14Distance > 800F) {
            this.k14Distance = 800F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerInc");
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.k14Distance -= 10F;
        if (this.k14Distance < 200F) {
            this.k14Distance = 200F;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerDec");
    }

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.k14WingspanType--;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType++;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "AskaniaWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
        netmsgguaranted.writeByte(this.k14Mode);
        netmsgguaranted.writeByte(this.k14WingspanType);
        netmsgguaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput netmsginput) throws IOException {
        this.k14Mode = netmsginput.readByte();
        this.k14WingspanType = netmsginput.readByte();
        this.k14Distance = netmsginput.readFloat();
    }

    private float   deltaAzimuth;
    private float   deltaTangage;
    private boolean X4check;
    public int      k14Mode;
    public int      k14WingspanType;
    public float    k14Distance;

    static {
        Class class1 = Me_262_HGIV.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me-262");
        Property.set(class1, "meshName", "3DO/Plane/Me-262-HGIV/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/HGIV.fmd:HGIV_FM");
        Property.set(class1, "LOSElevation", 0.74615F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262HGIV.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14" });
    }
}
