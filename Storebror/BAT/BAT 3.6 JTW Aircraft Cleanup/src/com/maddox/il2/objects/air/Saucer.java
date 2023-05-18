package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Saucer extends SaucerX implements TypeStormovik, TypeDiveBomber {

    public Saucer() {
        this.flapps = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.FM.isPlayers()) {
            this.FM.CT.bHasWingControl = false;
        }
    }

    protected void moveGear(float f) {
        Aircraft.xyz[2] = Aircraft.cvt(f, 0.1F, 0.99F, 0.0F, -1.2F);
        this.hierMesh().chunkSetLocate("GearC2_D0", Aircraft.xyz, Aircraft.ypr);
        this.hierMesh().chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.99F, 0.0F, -117F), 0.0F);
        this.hierMesh().chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.21F, 0.99F, 0.0F, -117F), 0.0F);
    }

    public boolean turretAngles(int i, float fs[]) {
        boolean bool = super.turretAngles(i, fs);
        float f = -fs[0];
        float f_0_ = fs[1];
        switch (i) {
            case 0:
                if (f < -135F) {
                    f = -135F;
                }
                if (f > 135F) {
                    f = 135F;
                }
                if (f_0_ < -69F) {
                    f_0_ = -69F;
                    bool = false;
                }
                if (f_0_ > 45F) {
                    f_0_ = 45F;
                    bool = false;
                }
                float f_1_;
                for (f_1_ = Math.abs(f); f_1_ > 180F; f_1_ -= 180F) {

                }
                if (f_0_ < -AircraftLH.floatindex(Aircraft.cvt(f_1_, 0.0F, 180F, 0.0F, 36F), fs)) {
                    f_0_ = -AircraftLH.floatindex(Aircraft.cvt(f_1_, 0.0F, 180F, 0.0F, 36F), fs);
                }
                break;
        }
        fs[0] = -f;
        fs[1] = f_0_;
        return bool;
    }

    public boolean typeDiveBomberToggleAutomation() {
        return false;
    }

    public void typeDiveBomberAdjAltitudeReset() {
    }

    public void typeDiveBomberAdjAltitudePlus() {
    }

    public void typeDiveBomberAdjAltitudeMinus() {
    }

    public void typeDiveBomberAdjVelocityReset() {
    }

    public void typeDiveBomberAdjVelocityPlus() {
    }

    public void typeDiveBomberAdjVelocityMinus() {
    }

    public void typeDiveBomberAdjDiveAngleReset() {
    }

    public void typeDiveBomberAdjDiveAnglePlus() {
    }

    public void typeDiveBomberAdjDiveAngleMinus() {
    }

    public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
    }

    public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
    }

    public void update(float f) {
        super.update(f);
        float f_2_ = this.FM.EI.engines[0].getControlRadiator();
        if (Math.abs(this.flapps - f_2_) > 0.01F) {
            this.flapps = f_2_;
            this.hierMesh().chunkSetAngles("Oil_D0", 0.0F, -22F * f_2_, 0.0F);
            for (int i = 1; i < 3; i++) {
                this.hierMesh().chunkSetAngles("Water" + i + "_D0", 0.0F, -22F * f_2_, 0.0F);
            }

        }
    }

    private float flapps;

    static {
        Class var_class = Saucer.class;
        new NetAircraft.SPAWN(var_class);
        Property.set(var_class, "iconFar_shortClassName", "Saucer");
        Property.set(var_class, "meshName", "3DO/Plane/Saucer/hier.him");
        Property.set(var_class, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(var_class, "yearService", 1943F);
        Property.set(var_class, "yearExpired", 1946.5F);
        Property.set(var_class, "FlightModel", "FlightModels/Saucer.fmd:Saucer_FM");
        Property.set(var_class, "cockpitClass", new Class[] { CockpitHE_LIIIB.class });
        Property.set(var_class, "LOSElevation", 1.1058F);
        Aircraft.weaponTriggersRegister(var_class, new int[] { 0, 0, 10, 10, 3, 3, 3 });
        Aircraft.weaponHooksRegister(var_class, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb01" });
    }
}
