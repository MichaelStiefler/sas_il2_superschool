package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class ANT_20MG extends ANT20 {

    public ANT_20MG() {
        this.fSightCurAltitude = 300F;
        this.fSightCurSpeed = 50F;
    }

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        this.hierMesh().chunkSetAngles("Blister1_D0", 0.0F, 70F * f, 0.0F);
        if (Config.isUSE_RENDER()) {
            if ((Main3D.cur3D().cockpits != null) && (Main3D.cur3D().cockpits[0] != null)) {
                Main3D.cur3D().cockpits[0].onDoorMoved(f);
            }
            this.setDoorSnd(f);
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.CT.bHasCockpitDoorControl = true;
        this.FM.CT.dvCockpitDoor = 0.5F;
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", f, 0.0F, 0.0F);
            return;
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            ANT_20MG.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            ANT_20MG.bChangedPit = true;
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag) {
            for (int i = 0; i < 6; i++) {
                if ((this.FM.AS.astateEngineStates[i] > 3) && (this.FM.EI.engines[i].getReadyness() < 0.1F)) {
                    this.FM.AS.repairEngine(i);
                }
            }

            for (int j = 0; j < 4; j++) {
                if ((this.FM.AS.astateTankStates[j] > 3) && (this.FM.AS.astatePilotStates[4] < 50F) && (this.FM.AS.astatePilotStates[7] < 50F) && (World.Rnd().nextFloat() < 0.1F)) {
                    this.FM.AS.repairTank(j);
                }
            }

        }
    }

    public void update(float f) {
        super.update(f);
        this.hierMesh().chunkSetAngles("GearL3_D0", 0.0F, -this.FM.Gears.gWheelAngles[0], 0.0F);
        this.hierMesh().chunkSetAngles("GearR3_D0", 0.0F, -this.FM.Gears.gWheelAngles[1], 0.0F);
    }

    public void msgExplosion(Explosion explosion) {
        this.setExplosion(explosion);
        if ((explosion.chunkName != null) && (explosion.chunkName.startsWith("Wing") || explosion.chunkName.startsWith("Tail")) && explosion.chunkName.endsWith("D3") && (explosion.power < 0.014F)) {
            return;
        } else {
            super.msgExplosion(explosion);
            return;
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

    public static boolean bChangedPit = false;
    public float          fSightCurAltitude;
    public float          fSightCurSpeed;

    static {
        Class class1 = ANT_20MG.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "ANT20");
        Property.set(class1, "meshName", "3Do/Plane/ANT-20MG/hierANT20MG.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar00());
        Property.set(class1, "yearService", 1932F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/ANT-20MG.fmd:ANT20");
        Property.set(class1, "cockpitClass", new Class[] { CockpitANT20MG.class, CockpitANT20MG_Bombardier.class });
        Aircraft.weaponTriggersRegister(class1, new int[0]);
        Aircraft.weaponHooksRegister(class1, new String[0]);
    }
}
