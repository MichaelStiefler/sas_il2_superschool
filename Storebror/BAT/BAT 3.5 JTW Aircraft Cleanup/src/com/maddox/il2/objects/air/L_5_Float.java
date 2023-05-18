package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class L_5_Float extends Scheme1 implements TypeSailPlane, TypeScout, TypeTransport, TypeStormovik {

    public L_5_Float() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && (World.cur().camouflage == 1)) {
            this.FM.CT.bHasBrakeControl = false;
        }
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            Aircraft.cvt(this.FM.Or.getTangage(), -30F, 30F, -30F, 30F);
        }
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 30F * f, 0.0F, 0.0F);
    }

    protected void moveElevator(float f) {
        this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, 0.0F, -30F * f);
        this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveAileron(float f) {
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, 0.0F, 30F * f);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, 0.0F, -30F * f);
    }

    protected void moveFlap(float f) {
        float f1 = -60F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, 0.0F, -f1);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, 0.0F, -f1);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("WingLMid") && (World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass)) {
            this.FM.AS.hitTank(shot.initiator, 0, (int) (1.0F + (shot.mass * 18.95F * 2.0F)));
        }
        if (shot.chunkName.startsWith("WingRMid") && (World.Rnd().nextFloat(0.0F, 0.121F) < shot.mass)) {
            this.FM.AS.hitTank(shot.initiator, 1, (int) (1.0F + (shot.mass * 18.95F * 2.0F)));
        }
        if (shot.chunkName.startsWith("Engine")) {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < shot.mass) {
                this.FM.AS.hitEngine(shot.initiator, 0, 1);
            }
            if ((Aircraft.v1.z > 0.0D) && (World.Rnd().nextFloat() < 0.12F)) {
                this.FM.AS.setEngineDies(shot.initiator, 0);
                if (shot.mass > 0.1F) {
                    this.FM.AS.hitEngine(shot.initiator, 0, 5);
                }
            }
            if ((Aircraft.v1.x < 0.1D) && (World.Rnd().nextFloat() < 0.57F)) {
                this.FM.AS.hitOil(shot.initiator, 0);
            }
        }
        if (shot.chunkName.startsWith("Pilot1")) {
            this.killPilot(shot.initiator, 0);
            this.FM.setCapableOfBMP(false, shot.initiator);
            if ((Aircraft.Pd.z > 0.5D) && (shot.initiator == World.getPlayerAircraft()) && World.cur().isArcade()) {
                HUD.logCenter("H E A D S H O T");
            }
        } else {
            if ((this.FM.AS.astateEngineStates[0] == 4) && (World.Rnd().nextInt(0, 99) < 33)) {
                this.FM.setCapableOfBMP(false, shot.initiator);
            }
            super.msgShot(shot);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 34:
                return super.cutFM(35, j, actor);

            case 37:
                return super.cutFM(38, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                break;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = L_5_Float.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "L-5_float");
        Property.set(class1, "meshName", "3DO/Plane/Sentinelfloat/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
        Property.set(class1, "meshName_us", "3DO/Plane/Sentinelfloat(US)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar04());
        Property.set(class1, "originCountry", PaintScheme.countryUSA);
        Property.set(class1, "yearService", 1940.9F);
        Property.set(class1, "yearExpired", 1955.3F);
        Property.set(class1, "FlightModel", "FlightModels/L5Sentinel_Ex.fmd:L5_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitL_5.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 9, 9, 9, 9, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04" });
    }
}
