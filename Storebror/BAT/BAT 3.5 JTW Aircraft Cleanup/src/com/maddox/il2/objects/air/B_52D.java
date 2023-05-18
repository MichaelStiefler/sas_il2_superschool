package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Config;
import com.maddox.rts.Property;

public class B_52D extends B_52fuelReceiver {

    public B_52D() {
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        return "D_";
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
    }

    public void update(float f) {
        super.update(f);
        this.computeEngine();
        if (this.FM.AS.isMaster() && Config.isUSE_RENDER()) {
            for (int i = 0; i < 8; i++) {
                if ((this.FM.EI.engines[i].getPowerOutput() > 0.8F) && (this.FM.EI.engines[i].getStage() == 6)) {
                    if (this.FM.EI.engines[i].getPowerOutput() > 0.95F) {
                        this.FM.AS.setSootState(this, i, 3);
                    } else {
                        this.FM.AS.setSootState(this, i, 2);
                    }
                } else {
                    this.FM.AS.setSootState(this, i, 0);
                }
            }

        }
    }

    public void computeEngine() {
        float f = this.FM.getAltitude() / 1000F;
        float f1 = 0.0F;
        if (this.FM.getSpeedKMH() > 800F) {
            if (this.FM.EI.engines[3].getThrustOutput() <= 0.9F) {

            }
        }
        if (f > 17D) {
            f1 = 0.0F;
        } else {
            float f2 = f * f;
            float f3 = f2 * f;
            f1 = (((49F * f3) / 1980F) - ((827F * f2) / 1980F)) + ((17F * f) / 330F);
        }
        this.FM.producedAF.x -= f1 * 1000F;
    }

    static {
        Class class1 = B_52D.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "B-52");
        Property.set(class1, "meshName", "3DO/Plane/B52D(Multi1)/hier.him");
        Property.set(class1, "PaintScheme_us", new PaintSchemeBMPar02());
        Property.set(class1, "yearService", 1943F);
        Property.set(class1, "yearExpired", 1956.6F);
        Property.set(class1, "FlightModel", "FlightModels/B-52D.fmd:B52FM");
        Property.set(class1, "cockpitClass", new Class[] { CockpitB_52.class, CockpitB_52Bombardier.class, CockpitB_52DGunner.class });
        Property.set(class1, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 10, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_BombSpawn10", "_BombSpawn11", "_BombSpawn12", "_BombSpawn13", "_BombSpawn14", "_BombSpawn15", "_BombSpawn16", "_BombSpawn17", "_BombSpawn18", "_BombSpawn19", "_BombSpawn20", "_BombSpawn21", "_BombSpawn22", "_BombSpawn23", "_BombSpawn24", "_BombSpawn25", "_BombSpawn26", "_BombSpawn27", "_BombSpawn28", "_BombSpawn29", "_BombSpawn30", "_BombSpawn31", "_BombSpawn32", "_BombSpawn33", "_BombSpawn34", "_BombSpawn35", "_BombSpawn36", "_BombSpawn37", "_BombSpawn38", "_BombSpawn39", "_BombSpawn40", "_BombSpawn41", "_BombSpawn42", "_BombSpawn43", "_BombSpawn44", "_BombSpawn45", "_BombSpawn46", "_BombSpawn47", "_BombSpawn48", "_BombSpawn49", "_BombSpawn50", "_BombSpawn51", "_BombSpawn52", "_BombSpawn53", "_BombSpawn54", "_BombSpawn55", "_BombSpawn56", "_BombSpawn57", "_BombSpawn58",
                "_BombSpawn59", "_BombSpawn60", "_BombSpawn61", "_BombSpawn62", "_BombSpawn63", "_BombSpawn64", "_BombSpawn65", "_BombSpawn66", "_BombSpawn67", "_BombSpawn68", "_BombSpawn69", "_BombSpawn70", "_BombSpawn71", "_BombSpawn72", "_BombSpawn73", "_BombSpawn74", "_BombSpawn75", "_BombSpawn76", "_BombSpawn77", "_BombSpawn78", "_BombSpawn79", "_BombSpawn80", "_BombSpawn81", "_BombSpawn82", "_BombSpawn83", "_BombSpawn84", "_MGUN03", "_MGUN04", "_BombSpawn201", "_BombSpawn202", "_BombSpawn203", "_BombSpawn204", "_BombSpawn205", "_BombSpawn206", "_BombSpawn207", "_BombSpawn208", "_BombSpawn209", "_BombSpawn210", "_BombSpawn211", "_BombSpawn212", "_BombSpawn213", "_BombSpawn214", "_BombSpawn215", "_BombSpawn216", "_BombSpawn217", "_BombSpawn218", "_BombSpawn219", "_BombSpawn220", "_BombSpawn221", "_BombSpawn222", "_BombSpawn223", "_BombSpawn224", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04" });
    }
}
