package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;
import com.maddox.sas1946.il2.util.Reflection;

public class FW_190V18 extends FW_190V implements MsgCollisionRequestListener, MsgCollisionListener {

    public FW_190V18() {
        this.compressorOK = true;
        this.radiatorControlOK = true;
        this.kangle = 0.0F;
        this.IdleTime = 0.0F;
        this.IdleTime1 = 0.0F;
    }

    protected void moveGear(float f) {
        FW_190V.moveGear(this.hierMesh(), f);
    }

    public void moveSteering(float f) {
        if (this.FM.CT.getGear() < 0.98F) {
            return;
        } else {
            this.hierMesh().chunkSetAngles("GearC2_D0", 0.0F, -f, 0.0F);
            return;
        }
    }

    private float randomLimit(float f, float f1) {
        return f / f1;
    }

    private void randomEngineFailures(float f) {
        if (this.FM.EI.engines[0].getStage() < 6) {
            return;
        }
        if (this.compressorOK && (World.Rnd().nextFloat() < this.randomLimit(f, FW_190V18.randomCompressorKillTime))) {
            this.FM.EI.engines[0].setKillCompressor(this);
            this.compressorOK = false;
            if (this == World.getPlayerAircraft()) {
                HUD.log("FailedCompressor");
            }
        }
        if ((this.FM.EI.engines[0].getCylindersOperable() > 0) && (World.Rnd().nextFloat() < this.randomLimit(f, FW_190V18.randomCylinderKnockoutTime))) {
            this.FM.EI.engines[0].setCyliderKnockOut(this, 1);
            HUD.log("Cylinder Knockout!");
        }
        if (World.Rnd().nextFloat() < this.randomLimit(f, FW_190V18.randomEngineStopTime)) {
            this.FM.EI.engines[0].setEngineStops(this);
        }
        if ((this.magnetosOK[0] || this.magnetosOK[1]) && (World.Rnd().nextFloat() < this.randomLimit(f, FW_190V18.randomMagnetoKnockoutTime))) {
            int i = World.Rnd().nextFloat() >= 0.5F ? 1 : 0;
            if (this.magnetosOK[i]) {
                this.FM.EI.engines[0].setMagnetoKnockOut(this, i);
                this.magnetosOK[i] = false;
                if (this == World.getPlayerAircraft()) {
                    HUD.log("Magneto Knockout !");
                }
            }
        }
        if (this.radiatorControlOK && (World.Rnd().nextFloat() < this.randomLimit(f, FW_190V18.randomRadiatorControlDamagedTime))) {
            Reflection.setBoolean(this.FM.EI.engines[0], "bHasRadiatorControl", false);
            this.radiatorControlOK = false;
            if (this == World.getPlayerAircraft()) {
                HUD.log("FailedRadiator");
            }
        }
    }

    public void update(float f) {
        if (this.FM.AS.isMaster() && World.cur().diffCur.Reliability) {
            this.randomEngineFailures(f);
        }
        for (int i = 1; i < 17; i++) {
            this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -20F * this.kangle, 0.0F);
        }

        this.kangle = (0.95F * this.kangle) + (0.05F * this.FM.EI.engines[0].getControlRadiator());
        super.update(f);
    }

    private static float randomCompressorKillTime         = 3600F;
    private static float randomCylinderKnockoutTime       = 1800F;
    private static float randomEngineStopTime             = 2700F;
    private static float randomMagnetoKnockoutTime        = 3600F;
    private static float randomRadiatorControlDamagedTime = 3600F;
    private boolean      compressorOK;
    private boolean      radiatorControlOK;
    private boolean      magnetosOK[]                     = { true, true };
    private float        kangle;
    public float         IdleTime;
    public float         IdleTime1;

    static {
        Class class1 = FW_190V18.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "FW190V18");
        Property.set(class1, "meshName", "3DO/Plane/Fw-190V18/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1944.6F);
        Property.set(class1, "yearExpired", 1948F);
        Property.set(class1, "FlightModel", "FlightModels/Fw-190V18.fmd:FW190V13V18_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitFW_190V18.class });
        Property.set(class1, "LOSElevation", 0.755F);
        Aircraft.weaponTriggersRegister(class1, new int[2]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02" });
    }
}
