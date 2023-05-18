package com.maddox.il2.objects.air;

import com.maddox.il2.ai.War;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.game.Main3D;
import com.maddox.rts.Property;

public class F_86A5 extends F_86F {

    public F_86A5() {
    }

    public void checkHydraulicStatus() {
        if ((this.FM.EI.engines[0].getStage() < 6) && (this.FM.Gears.nOfGearsOnGr > 0)) {
            this.gearTargetAngle = 90F;
            this.hasHydraulicPressure = false;
            this.FM.CT.bHasAileronControl = false;
            this.FM.CT.AirBrakeControl = 1.0F;
        } else if (this.FM.EI.engines[0].getStage() < 6) {
            this.hasHydraulicPressure = false;
            this.FM.CT.bHasAileronControl = false;
            this.FM.CT.bHasAirBrakeControl = false;
        }
        if (!this.hasHydraulicPressure) {
            this.gearTargetAngle = 0.0F;
            this.hasHydraulicPressure = true;
            this.FM.CT.bHasAileronControl = true;
            this.FM.CT.bHasAirBrakeControl = true;
        }
    }

    public void typeFighterAceMakerRangeFinder() {
        if (this.k14Mode == 2) {
            return;
        }
        if (!Config.isUSE_RENDER()) {
            return;
        }
        F_86F.hunted = Main3D.cur3D().getViewPadlockEnemy();
        if (F_86F.hunted == null) {
            this.k14Distance = 200F;
            F_86F.hunted = War.GetNearestEnemyAircraft(((Interpolate) (this.FM)).actor, 2000F, 9);
        }
        if (F_86F.hunted != null) {
            this.k14Distance = (float) ((Interpolate) (this.FM)).actor.pos.getAbsPoint().distance(F_86F.hunted.pos.getAbsPoint());
            if (this.k14Distance > 800F) {
                this.k14Distance = 800F;
            } else if (this.k14Distance < 200F) {
                this.k14Distance = 200F;
            }
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.getSpeed() > 5F) {
            this.moveSlats(f);
            this.bSlatsOff = false;
        } else {
            this.slatsOff();
        }
    }

    protected void moveSlats(float f) {
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
        if (this.bSlatsOff) {
            return;
        } else {
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
        Class class1 = F_86A5.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-86");
        Property.set(class1, "meshName", "3DO/Plane/F-86A(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
        Property.set(class1, "meshName_us", "3DO/Plane/F-86A(USA)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1949.9F);
        Property.set(class1, "yearExpired", 1960.3F);
        Property.set(class1, "FlightModel", "FlightModels/F-86A.fmd:JETERA");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_86A.class });
        Property.set(class1, "LOSElevation", 0.725F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 0, 0, 9, 9, 9, 9, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
