package com.maddox.il2.objects.air;

import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.objects.weapons.RocketGunAIM9L_gn16;
import com.maddox.rts.Property;

public class F_18C extends F_18 {

    public F_18C() {
        this.bulletEmitters = null;
        this.wingFoldValue = 0.0F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.bulletEmitters = new BulletEmitter[F_18C.weaponHookArray.length];
        for (int i = 0; i < F_18C.weaponHookArray.length; i++) {
            this.bulletEmitters[i] = this.getBulletEmitterByHookName(F_18C.weaponHookArray[i]);
        }

    }

    public void update(float f) {
        this.computeF404_GE402_AB();
        super.update(f);
    }

    private void computeF404_GE402_AB() {
        float f = Aircraft.cvt(this.calculateMach(), 1.0F, 1.75F, 1.0F, 0.4F);
        float f1 = Aircraft.cvt(this.FM.getAltitude(), 16500F, 18000F, 1.0F, 0.4F);
        if ((this.FM.EI.engines[0].getThrustOutput() > 1.001F) && (this.FM.EI.engines[0].getStage() > 5)) {
            this.FM.producedAF.x += 25000D * f * f1;
        }
        if ((this.FM.EI.engines[1].getThrustOutput() > 1.001F) && (this.FM.EI.engines[1].getStage() > 5)) {
            this.FM.producedAF.x += 25000D * f * f1;
        }
    }

    public void missionStarting() {
        super.missionStarting();
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
    }

    public void updateHook() {
        for (int i = 0; i < F_18C.weaponHookArray.length; i++) {
            try {
                if (this.bulletEmitters[i] instanceof RocketGunAIM9L_gn16) {
                    ((RocketGunAIM9L_gn16) this.bulletEmitters[i]).updateHook(F_18C.weaponHookArray[i]);
                }
            } catch (Exception exception) {
            }
        }

    }

    public void moveWingFold(float f) {
        this.moveWingFold(this.hierMesh(), f);
        super.moveWingFold(f);
        if (this.wingFoldValue != f) {
            this.wingFoldValue = f;
            this.needUpdateHook = true;
        }
    }

    private static String weaponHookArray[] = { "_CANNON01", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalDev17", "_ExternalDev18", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExMis01", "_ExMis01", "_ExMis02", "_ExMis02", "_ExMis03", "_ExFLIR", "_ExMis04", "_ExLASER", "_ExternalMis05", "_ExternalMis05", "_ExternalMis06", "_ExternalMis06", "_ExternalMis07", "_ExternalMis07", "_ExternalMis08", "_ExternalMis08", "_ExternalMis09", "_ExternalMis09", "_ExternalMis10", "_ExternalMis10", "_ExternalMis11", "_ExternalMis11", "_ExternalMis12", "_ExternalMis12",
            "_ExternalMis13", "_ExternalMis13", "_ExternalMis14", "_ExternalMis14", "_ExternalMis15", "_ExternalMis15", "_ExternalMis16", "_ExternalMis16", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalRock03", "_ExternalRock03", "_ExternalRock04", "_ExternalRock04", "_ExternalRock05", "_ExternalRock05", "_ExternalRock06", "_ExternalRock06", "_ExternalRock07", "_ExternalRock07", "_ExternalRock08", "_ExternalRock08", "_ExternalRock09", "_ExternalRock09", "_ExternalRock10", "_ExternalRock10", "_ExternalRock11", "_ExternalRock11", "_ExternalRock12", "_ExternalRock12", "_Rock13", "_Rock14", "_Rock15", "_Rock16", "_Rock17", "_Rock18", "_Rock19", "_Rock20", "_Rock21", "_Rock22", "_Rock23", "_Rock24", "_Rock25", "_Rock26", "_Rock27", "_Rock28", "_Flare01", "_Flare02", "_Chaff01", "_Chaff02" };
    private BulletEmitter bulletEmitters[];
    private float         wingFoldValue;

    static {
        Class class1 = F_18C.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "F-18C");
        Property.set(class1, "meshName", "3DO/Plane/F-18C/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1989F);
        Property.set(class1, "yearExpired", 2050F);
        Property.set(class1, "FlightModel", "FlightModels/F-18C.fmd:F18_FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitF_18C.class, CockpitF18FLIR.class });
        Property.set(class1, "LOSElevation", 0.965F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 7, 7, 8, 8 });
        Aircraft.weaponHooksRegister(class1, F_18C.weaponHookArray);
    }
}
