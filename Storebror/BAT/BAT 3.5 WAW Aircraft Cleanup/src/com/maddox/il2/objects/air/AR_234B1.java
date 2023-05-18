package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class AR_234B1 extends AR_234B2 implements TypeScout {

    public AR_234B1() {
        this.tankStates = new int[2];
        this.nextTankToggleTime = 0L;
        this.nextEngineFireActionTime = new long[2];
        this.nextEngineFireAction = new int[2];
        this.engineStates = new int[2];
        this.throttleLocked = false;
        this.lastThrustChange = 0L;
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

    public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
    }

    public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
    }

    public void update(float f) {
        super.update(f);
        for (int i = 0; i <= 1; i++) {
            if ((Math.abs(this.FM.EI.engines[i].getControlThrottle() - this.lastLockedThrust[i]) > 0.03F) || ((this.FM.EI.engines[i].getStage() > 0) && (this.FM.EI.engines[i].getStage() < 6))) {
                this.lastLockedThrust[i] = this.FM.EI.engines[i].getControlThrottle();
                this.lastThrustChange = Time.current();
            }
            this.throttleLocked = (Time.current() - this.lastThrustChange) > 5000L;
        }

    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this != World.getPlayerAircraft()) {
            return;
        } else {
            this.checkEngineStates();
            this.checkTankState(0, 1);
            this.checkTankState(1, 0);
            return;
        }
    }

    private void checkEngineStates() {
        for (int i = 0; i <= 1; i++) {
            if (!this.engineIsUp(i)) {
                this.tankSelectors[i] = 2;
            }
            if (this.FM.AS.astateEngineStates[i] >= 4) {
                if (this.nextEngineFireActionTime[i] == 0L) {
                    System.out.println("ENGINE " + (i + 1) + " caught fire !!!");
                    this.nextEngineFireActionTime[i] = Time.current() + 5000L;
                } else if (Time.current() > this.nextEngineFireActionTime[i]) {
                    switch (this.nextEngineFireAction[i]) {
                        default:
                            break;

                        case 0:
                            this.FM.AS.setEngineDies(this, i);
                            this.tankSelectors[i] = 2;
                            this.nextEngineFireActionTime[i] = Time.current() + 5000L;
                            this.nextEngineFireAction[i] = 1;
                            break;

                        case 1:
                            this.FM.AS.setEngineState(this, i, 2);
                            Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Aircraft/EngineExtinguisher1.eff", 3F);
                            this.FM.EI.engines[i].tOilIn -= 20F;
                            this.FM.EI.engines[i].tOilOut -= 20F;
                            this.FM.EI.engines[i].tWaterOut -= 40F;
                            LightPointActor astateEngineBurnLights[] = (LightPointActor[]) Reflection.getValue(this.FM.AS, "astateEngineBurnLights");
                            for (int j = 0; j < astateEngineBurnLights.length; j++) {
                                if (astateEngineBurnLights[i] != null) {
                                    this.draw.lightMap().remove("_EngineBurnLight" + i);
                                    astateEngineBurnLights[i].destroy();
                                    astateEngineBurnLights[i] = null;
                                }
                            }

                            this.nextEngineFireActionTime[i] = 0L;
                            break;
                    }
                }
            }
        }

    }

    private boolean engineIsUp(int engineIndex) {
        return (this.FM.EI.engines[engineIndex].getStage() > 0) && (this.FM.EI.engines[engineIndex].getStage() < 7);
    }

    private void checkTankState(int theTank, int otherTank) {
        this.tankStates[theTank] = this.FM.AS.astateTankStates[theTank];
        if (((this.tankStates[theTank] != 0) && (this.tankStates[otherTank] == 0)) || ((this.tankStates[theTank] == 6) && (this.tankStates[otherTank] < 6))) {
            for (int i = 0; i <= 1; i++) {
                if (this.engineIsUp(i)) {
                    this.tankSelectors[i] = otherTank;
                }
            }

        } else if (((this.tankStates[theTank] != 0) && (this.tankStates[otherTank] != 0)) || (this.tankStates[theTank] == this.tankStates[otherTank])) {
            if (!this.engineIsUp(0) && !this.engineIsUp(1)) {
                return;
            }
            if (this.engineIsUp(0) && this.engineIsUp(1)) {
                this.tankSelectors[0] = 0;
                this.tankSelectors[1] = 1;
                return;
            }
            if (Time.current() < this.nextTankToggleTime) {
                return;
            }
            int toggleTankIndex = this.engineIsUp(0) ? 0 : 1;
            if (this.tankSelectors[toggleTankIndex] == 0) {
                this.tankSelectors[toggleTankIndex] = 1;
            } else {
                this.tankSelectors[toggleTankIndex] = 0;
            }
            this.nextTankToggleTime = Time.current() + 30000L;
        }
    }

    protected int              tankSelectors[]    = { 0, 1 };
    protected int              tankStates[];
    private long               nextTankToggleTime;
    protected long             nextEngineFireActionTime[];
    protected int              nextEngineFireAction[];
    protected int              engineStates[];
    protected static final int LEFT_ENGINE        = 0;
    protected static final int RIGHT_ENGINE       = 1;
    protected boolean          throttleLocked;
    private float              lastLockedThrust[] = { 0.0F, 0.0F };
    private long               lastThrustChange;
    static {
        Class class1 = AR_234B1.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Ar 234");
        Property.set(class1, "meshName", "3DO/Plane/Ar-234B-1/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.8F);
        Property.set(class1, "FlightModel", "FlightModels/Ar-234B-1.fmd:AR234B1_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitAR_234B1.class });
        Property.set(class1, "LOSElevation", 1.14075F);
        Aircraft.weaponTriggersRegister(class1, new int[0]);
        Aircraft.weaponHooksRegister(class1, new String[0]);
    }
}
