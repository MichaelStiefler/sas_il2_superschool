package com.maddox.il2.objects.air;

import java.util.ArrayList;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.rts.Property;
import com.maddox.util.HashMapInt;

public class I_153S extends I_153_M62 implements TypeFighter, TypeTNBFighter {

    public I_153S() {
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER() && (World.cur().camouflage == 1)) {
            this.hierMesh().chunkVisible("GearL1_D0", false);
            this.hierMesh().chunkVisible("GearL4_D0", false);
            this.hierMesh().chunkVisible("GearL6_D0", false);
            this.hierMesh().chunkVisible("Ski3L1_D0", true);
            this.hierMesh().chunkVisible("GearL6N_D0", true);
            this.hierMesh().chunkVisible("GearL7_D0", true);
            this.hierMesh().chunkVisible("GearL8_D0", true);
            this.hierMesh().chunkVisible("GearL9_D0", true);
            this.hierMesh().chunkVisible("GearR1_D0", false);
            this.hierMesh().chunkVisible("GearR4_D0", false);
            this.hierMesh().chunkVisible("GearR6_D0", false);
            this.hierMesh().chunkVisible("Ski3R1_D0", true);
            this.hierMesh().chunkVisible("GearR6N_D0", true);
            this.hierMesh().chunkVisible("GearR7_D0", true);
            this.hierMesh().chunkVisible("GearR8_D0", true);
            this.hierMesh().chunkVisible("GearR9_D0", true);
            this.FM.CT.bHasBrakeControl = false;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -95F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 95F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL3_D0", 0.0F, 15F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR3_D0", 0.0F, 15F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL5_D0", 0.0F, 170F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR5_D0", 0.0F, 170F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, -85F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL7_D0", 30F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearR7_D0", 30F * f, 0.0F, 0.0F);
        hiermesh.chunkSetAngles("GearL8_D0", 0.0F, 0.0F, 16F * f);
        hiermesh.chunkSetAngles("GearR8_D0", 0.0F, 0.0F, -16F * f);
        hiermesh.chunkSetAngles("Ski3L1_D0", 0.0F, -132F * f, 0.0F);
        hiermesh.chunkSetAngles("Ski3R1_D0", 0.0F, -132F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, -Math.max(-f * 1500F, -100F), 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, -Math.max(-f1 * 1500F, -100F), 0.0F);
    }

    protected void moveGear(float f, float f1, float f2) {
        I_153S.moveGear(this.hierMesh(), f, f1, f2);
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            I_153S.bChangedPit = true;
        }
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) {
            I_153S.bChangedPit = true;
        }
    }

    public static boolean bChangedPit = false;

    static {
        Class class1 = I_153S.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-153");
        Property.set(class1, "meshName", "3DO/Plane/I-153S/hierS.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1935F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_153.class });
        Property.set(class1, "FlightModel", "FlightModels/I-153-M62.fmd");
        Property.set(class1, "weaponsList", new ArrayList());
        Property.set(class1, "weaponsMap", new HashMapInt());
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalDev01", "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08", "_ExternalDev09", "_ExternalDev10", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev11", "_ExternalDev12" });
    }
}
