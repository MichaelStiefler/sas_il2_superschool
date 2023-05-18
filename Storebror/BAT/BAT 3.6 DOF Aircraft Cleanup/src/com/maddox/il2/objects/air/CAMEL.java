package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CAMEL extends SOPWITH {

    public CAMEL() {
        this.oldProp = new int[6];
        this.tmpPoint = new Point3d();
        this.tmpVector = new Vector3d();
        this.dynamoOrient = 0.0F;
        this.bDynamoRotary = false;
        this.clearedTakeoff = false;
        this.timerTakeoff = -1L;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.startsWith("rockets")) {
            this.hierMesh().chunkVisible("RocketPylonL", true);
            this.hierMesh().chunkVisible("RocketPylonR", true);
        }
    }

    protected void moveFan(float f) {
        this.dynamoOrient = this.bDynamoRotary ? (this.dynamoOrient - 17.987F) % 360F : (float) (this.dynamoOrient - (this.FM.Vwld.length() * 1.5444015264511108D)) % 360F;
        this.hierMesh().chunkSetAngles("SpedometerProp", 0.0F, 0.0F, -this.dynamoOrient);
        int i = 0;
        int j = 0;
        int k = 1;
        if (this.oldProp[j] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[j]) && this.hierMesh().isChunkVisible(CAMEL.Props[j][this.oldProp[j]])) {
                this.hierMesh().chunkVisible(CAMEL.Props[0][this.oldProp[j]], false);
                this.oldProp[j] = i;
                this.hierMesh().chunkVisible(CAMEL.Props[j][i], true);
            }
        }
        if (i == 0) {
            this.propPos[j] = (this.propPos[j] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
        } else {
            float f1 = 57.3F * this.FM.EI.engines[0].getw();
            f1 %= 2880F;
            f1 /= 2880F;
            if (f1 <= 0.5F) {
                f1 *= 2.0F;
            } else {
                f1 = (f1 * 2.0F) - 2.0F;
            }
            f1 *= 1200F;
            this.propPos[j] = (this.propPos[j] + (f1 * f)) % 360F;
        }
        this.hierMesh().chunkSetAngles(CAMEL.Props[j][0], 0.0F, 0.0F, this.propPos[j]);
        if (this.oldProp[k] < 2) {
            i = Math.abs((int) (this.FM.EI.engines[0].getw() * 0.06F));
            if (i >= 1) {
                i = 1;
            }
            if ((i != this.oldProp[k]) && this.hierMesh().isChunkVisible(CAMEL.Props[k][this.oldProp[k]])) {
                this.hierMesh().chunkVisible(CAMEL.Props[k][this.oldProp[k]], false);
                this.oldProp[k] = i;
                this.hierMesh().chunkVisible(CAMEL.Props[k][i], true);
            }
        }
        if (i == 0) {
            this.propPos[k] = (this.propPos[k] + (57.3F * this.FM.EI.engines[0].getw() * f)) % 360F;
        } else {
            float f2 = 57.3F * this.FM.EI.engines[0].getw();
            f2 %= 2880F;
            f2 /= 2880F;
            if (f2 <= 0.5F) {
                f2 *= 2.0F;
            } else {
                f2 = (f2 * 2.0F) - 2.0F;
            }
            f2 *= 1200F;
            this.propPos[k] = (this.propPos[k] + (f2 * f)) % 360F;
        }
        this.hierMesh().chunkSetAngles(CAMEL.Props[k][0], 0.0F, 0.0F, this.propPos[k]);
    }

    public void doSetSootState(int i, int j) {
        for (int k = 0; k < 2; k++) {
            if (this.FM.AS.astateSootEffects[i][k] != null) {
                Eff3DActor.finish(this.FM.AS.astateSootEffects[i][k]);
            }
            this.FM.AS.astateSootEffects[i][k] = null;
        }

        switch (j) {
            case 1:
            case 2:
            case 3:
                this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("HolyGrail01"), null, 1.0F, "3DO/Effects/Aircraft/EngineBlackSmall.eff", -1F);
                break;

            case 4:
            case 5:
            default:
                return;
        }
    }

    public void update(float f) {
        super.update(f);
        float f1 = Atmosphere.temperature((float) this.FM.Loc.z) - 273.15F;
        float f2 = Pitot.Indicator((float) this.FM.Loc.z, this.FM.getSpeedKMH());
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f3 = (((this.FM.EI.engines[0].getControlRadiator() * f * f2) / (f2 + 50F)) * (this.FM.EI.engines[0].tWaterOut - f1)) / 256F;
        this.FM.EI.engines[0].tWaterOut -= f3;
        if ((this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[0].getControlThrottle() > 0.2F)) {
            this.FM.AS.setSootState(this, 0, 3);
        } else {
            this.FM.AS.setSootState(this, 0, 0);
        }
        if ((this.FM.EI.engines[0].getStage() == 6) && ((this.FM.CT.getMagnetoControl() == 2.0F) || (this.FM.CT.getMagnetoControl() == 1.0F))) {
            this.FM.EI.engines[0].setControlThrottle(0.33F);
        } else if ((this.FM.EI.engines[0].getStage() == 6) && (this.FM.CT.getMagnetoControl() == 3F)) {
            this.FM.EI.engines[0].setControlThrottle(1.0F);
        }
        this.gunSync();
        if ((!this.FM.isPlayers() || !(this.FM instanceof RealFlightModel) || !((RealFlightModel) this.FM).isRealMode()) && (this.FM instanceof Maneuver)) {
            if (!this.clearedTakeoff && this.FM.Gears.onGround() && (this.FM.getSpeedKMH() < 5F)) {
                this.FM.brakeShoe = true;
            } else if (this.clearedTakeoff || !this.FM.Gears.onGround()) {
                this.FM.brakeShoe = false;
            }
            if ((this.FM.EI.engines[0].getStage() == 6) && (this.FM.CT.getMagnetoControl() == 3F)) {
                if (!this.FM.AP.way.isLanding()) {
                    this.FM.CT.PowerControl = 1.0F;
                } else {
                    this.FM.CT.setMixControl(0.2F);
                }
                if (!this.clearedTakeoff && (this.timerTakeoff == -1L)) {
                    this.timerTakeoff = Time.current() + 60000L;
                }
                this.tmpPoint.set(this.pos.getAbsPoint());
                this.tmpVector.set(60D, 0.0D, 0.0D);
                this.pos.getAbsOrient().transform(this.tmpVector);
                this.tmpPoint.add(this.tmpVector);
                Aircraft.Pd.set(this.tmpPoint);
                if ((Time.current() > this.timerTakeoff) && (War.getNearestFriendAtPoint(Aircraft.Pd, this, 50F) == null)) {
                    this.clearedTakeoff = true;
                }
            }
        }
    }

    private void gunSync() {
        if (this.syncMechDmg && this.FM.CT.WeaponControl[0] && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.CT.Weapons[0][0].countBullets() != 0) && (World.Rnd().nextFloat() < 0.005F)) {
            Aircraft.debugprintln(this, "*** Prop hit");
            this.hierMesh().chunkVisible("PropRot1_D0", false);
            this.hierMesh().chunkVisible("Prop1_D1", true);
            this.FM.EI.engines[0].setEngineDies(this);
            this.FM.AS.setJamBullets(0, 0);
            this.FM.AS.setJamBullets(0, 1);
            this.FM.AS.setJamBullets(0, 2);
        }
    }

    protected int                 oldProp[];
    protected static final String Props[][] = { { "Prop1_D0", "PropRot1_D0", "Prop1_D1" }, { "Prop2_D0", "PropRot2_D0", "Prop2_D1" }, { "Prop3_D0", "PropRot3_D0", "Prop3_D1" }, { "Prop4_D0", "PropRot4_D0", "Prop4_D1" }, { "Prop5_D0", "PropRot5_D0", "Prop5_D1" }, { "Prop6_D0", "PropRot6_D0", "Prop6_D1" } };
    private float                 dynamoOrient;
    private boolean               bDynamoRotary;
    private boolean               clearedTakeoff;
    private long                  timerTakeoff;
    private Point3d               tmpPoint;
    private Vector3d              tmpVector;

    static {
        Class class1 = CAMEL.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Camel");
        Property.set(class1, "meshName", "3DO/Plane/Camel/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMParWW1());
        Property.set(class1, "yearService", 1916F);
        Property.set(class1, "yearExpired", 1918F);
        Property.set(class1, "FlightModel", "FlightModels/Camel.fmd:SOPWITH");
        Property.set(class1, "originCountry", PaintScheme.countryBritain);
        Property.set(class1, "cockpitClass", new Class[] { CockpitCAMEL.class });
        Property.set(class1, "LOSElevation", 0.66F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock08" });
    }
}
