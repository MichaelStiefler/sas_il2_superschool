package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.rts.Property;

public class A_26K extends A_26 implements TypeStormovik, TypeStormovikArmored {

    public A_26K() {
    }

    public void rareAction(float paramFloat, boolean paramBoolean) {
        super.rareAction(paramFloat, paramBoolean);
        if (paramBoolean) {
            if ((this.FM.AS.astateEngineStates[0] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 0, 1);
            }
            if ((this.FM.AS.astateEngineStates[1] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 1, 1);
            }
            if ((this.FM.AS.astateEngineStates[2] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 2, 1);
            }
            if ((this.FM.AS.astateEngineStates[3] > 3) && (World.Rnd().nextFloat() < 0.0023F)) {
                this.FM.AS.hitTank(this, 3, 1);
            }
        }
        for (int i = 1; i < 4; i++) {
            if (this.FM.getAltitude() < 3000F) {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", false);
            } else {
                this.hierMesh().chunkVisible("HMask" + i + "_D0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
            }
        }

    }

    public static boolean bChangedPit = false;

    static {
        Class localClass = A_26K.class;
        new NetAircraft.SPAWN(localClass);
        Property.set(localClass, "iconFar_shortClassName", "A-26K");
        Property.set(localClass, "meshName", "3DO/Plane/A26-Invader(multi1)/hierk.him");
        Property.set(localClass, "PaintScheme", new PaintSchemeBMPar03());
        Property.set(localClass, "noseart", 1);
        Property.set(localClass, "yearService", 1945F);
        Property.set(localClass, "yearExpired", 1969.6F);
        Property.set(localClass, "FlightModel", "FlightModels/A-26K.fmd:A26K_FM");
        Property.set(localClass, "cockpitClass", new Class[] { CockpitA_26B.class });
        Property.set(localClass, "LOSElevation", 0.73425F);
        Aircraft.weaponTriggersRegister(localClass, new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 11, 11, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(localClass,
                new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", "_MGUN13", "_MGUN14", "_MGUN15", "_MGUN16", "_MGUN17", "_MGUN18", "_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_BombSpawn05", "_BombSpawn06", "_BombSpawn07", "_BombSpawn08", "_BombSpawn09", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalDev11", "_ExternalDev12", "_ExternalDev13", "_ExternalDev14", "_ExternalDev15", "_ExternalDev16", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05",
                        "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalBomb15", "_ExternalBomb16", "_ExternalBomb17", "_ExternalBomb18", "_ExternalBomb19", "_ExternalBomb20", "_ExternalBomb21", "_ExternalBomb22", "_ExternalBomb23", "_ExternalBomb24", "_ExternalBomb25", "_ExternalBomb26", "_ExternalDev17", "_ExternalDev18", "_ExternalR01", "_ExternalR02", "_ExternalR03", "_ExternalR04", "_ExternalR05", "_ExternalR06", "_ExternalR07", "_ExternalR08", "_ExternalR09", "_ExternalR10", "_ExternalR11", "_ExternalR12", "_ExternalR13", "_ExternalR14", "_ExternalR15", "_ExternalR16", "_ExternalR17", "_ExternalR18", "_ExternalR19", "_ExternalR20", "_ExternalR21", "_ExternalR22", "_ExternalR23", "_ExternalR24", "_ExternalR25", "_ExternalR26", "_ExternalR27", "_ExternalR28", "_ExternalR29", "_ExternalR30", "_ExternalR31", "_ExternalR32", "_ExternalR33", "_ExternalR34", "_ExternalR35",
                        "_ExternalR36", "_ExternalR37", "_ExternalR38" });
    }
}
