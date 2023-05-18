package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class CASA_352L extends JU_52 implements TypeBomber {

    public CASA_352L() {
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("WingRIn") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitTank(shot.initiator, 1, 1);
        }
        if (shot.chunkName.startsWith("Engine1") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Engine2") && (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F)) {
            this.FM.AS.hitEngine(shot.initiator, 1, 1);
        }
        if ((this.FM.AS.astateEngineStates[0] > 2) && (this.FM.AS.astateEngineStates[1] > 2)) {
            this.FM.setCapableOfBMP(false, shot.initiator);
        }
        super.msgShot(shot);
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

    public void typeBomberUpdate(float f1) {
    }

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    static {
        Class class1 = CASA_352L.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "FlightModel", "FlightModels/Ju-52_3mg4e.fmd");
        Property.set(class1, "meshName", "3do/plane/CASA-352L/hier.him");
        Property.set(class1, "iconFar_shortClassName", "Ju-52");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1970);
        Property.set(class1, "cockpitClass", new Class[] { CockpitJU524E.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 3 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_BombSpawn01" });
    }
}
