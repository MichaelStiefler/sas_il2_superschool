package com.maddox.il2.objects.air;

import com.maddox.rts.Property;

public class F_86F_25E extends F_86F {

    public F_86F_25E() {
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.getSpeed() > 5F) {
            this.moveSlats(f);
            this.bSlatsOff = false;
        } else this.slatsOff();
    }

    protected void moveSlats(float paramFloat) {
        this.resetYPRmodifier();
        Aircraft.xyz[0] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.15F);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 0.1F);
        Aircraft.xyz[2] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.065F);
        this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        this.hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
        Aircraft.xyz[1] = Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, -0.1F);
        this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, Aircraft.cvt(this.FM.getAOA(), 6.8F, 15F, 0.0F, 8.5F), 0.0F);
        this.hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void slatsOff() {
        if (this.bSlatsOff) return;
        else {
            this.resetYPRmodifier();
            Aircraft.xyz[0] = -0.15F;
            Aircraft.xyz[1] = 0.1F;
            Aircraft.xyz[2] = -0.065F;
            this.hierMesh().chunkSetAngles("SlatL_D0", 0.0F, 8.5F, 0.0F);
            this.hierMesh().chunkSetLocate("SlatL_D0", Aircraft.xyz, Aircraft.ypr);
            Aircraft.xyz[1] = -0.1F;
            this.hierMesh().chunkSetAngles("SlatR_D0", 0.0F, 8.5F, 0.0F);
            this.hierMesh().chunkSetLocate("SlatR_D0", Aircraft.xyz, Aircraft.ypr);
            this.bSlatsOff = true;
            return;
        }
    }

    static {
        Class localClass = F_86F_25E.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "F-86");
        Property.set(localClass, "meshName", "3DO/Plane/F-86E(Multi1)/hier.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(localClass, "meshName_us", "3DO/Plane/F-86E(USA)/hier.him");
        Property.set(localClass, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(localClass, "meshName_gb", "3DO/Plane/F-86E(Multi1)/hier.him");
        Property.set(localClass, "PaintScheme_gb", new PaintSchemeFMPar1956());
        Property.set(localClass, "meshName_it", "3DO/Plane/F-86E(Multi1)/hier.him");
        Property.set(localClass, "PaintScheme_it", new PaintSchemeFMPar1956());
        Property.set(localClass, "yearService", 1949.9F);
        Property.set(localClass, "yearExpired", 1960.3F);
        Property.set(localClass, "FlightModel", "FlightModels/F-86F-25E.fmd");
        Property.set(localClass, "cockpitClass", new Class[] { CockpitF_86F.class });
        Property.set(localClass, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(localClass,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06",
                        "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06",
                        "_ExternalRock07", "_ExternalRock08" });
    }
}
