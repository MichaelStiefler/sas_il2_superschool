// This file is part of the SAS IL-2 Sturmovik 1946
// Westland Whirlwind Mod.
// If you copy, modify or redistribute this package, parts
// of this package or reuse sources from this package,
// we'd be happy if you could mention the origin including
// our web address
//
// www.sas1946.com
//
// Thank you for your cooperation!
//
// Mod Creator:          tfs101
// Original file source: SAS~S3
// Modified by:          SAS - Special Aircraft Services
//                       www.sas1946.com
//
// Last Edited by:       SAS~Storebror
// Last Edited at:       2013/10/22

package com.maddox.il2.objects.air;

import java.lang.reflect.Field;

import com.maddox.il2.ai.Regiment;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.sas1946.il2.util.Reflection;

public abstract class Whirlwind extends MOSQUITO {

    public boolean       bChangedPit        = true;

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
    }

    protected void nextCUTLevel(String s, int i, Actor actor) {
        super.nextCUTLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();

        // Try to set per-gear values for Gear Period. This will work in 4.12+ game versions only.
        // This step is required for 4.12 and 4.12.1 since TD forgot to reflect changes of
        // Gear Period parameter to the per-gear specific fields.
        // We have to touch these fields through reflection since 4.11.1m and older games are missing
        // the per-gear dvGear fields in Controls class.
        try {
            Class controlsClass = this.FM.CT.getClass();
            Field dvGearField = controlsClass.getField("dvGearL");
            dvGearField.setFloat(this.FM.CT, this.FM.CT.dvGear);
            dvGearField = controlsClass.getField("dvGearR");
            dvGearField.setFloat(this.FM.CT, this.FM.CT.dvGear);
            dvGearField = controlsClass.getField("dvGearC");
            dvGearField.setFloat(this.FM.CT, this.FM.CT.dvGear);
        } catch (Exception e) { // Game is 4.11.1m or older, fall through...
        }
    }

    // ************************************************************************************************
    // Overwritten Super Classes, this is to ensure error-free log output since the base class
    // "MOSQUITO" adresses several meshes which are not part of the Whirlie (e.g. Co-Pilot etc.)
    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_D0"));
    }

    protected void moveBayDoor(float f) {
    }

    protected void moveFlap(float f) {
        this.resetYPRmodifier();
        xyz[0] = floatindex(cvt(f, 0.0F, 0.99F, 0.0F, 10.0F), flapScaleX);
        xyz[2] = floatindex(cvt(f, 0.0F, 0.99F, 0.0F, 10.0F), flapScaleZ);
        ypr[1] = -45.0F * f;
        ypr[0] = floatindex(cvt(f, 0.0F, 0.99F, 0.0F, 10.0F), flapScaley);
        this.hierMesh().chunkSetLocate("Flap02_D0", xyz, ypr);
        ypr[0] = -ypr[0];
        this.hierMesh().chunkSetLocate("Flap03_D0", xyz, ypr);
    }

    private static final float[] flapScaleX = { 0F, 0F, 0F, 0F, 0F, 0F, -0.02F, -0.04F, -0.06F, -0.08F, -0.12F };
    private static final float[] flapScaleZ = { 0F, -0.008F, -0.02F, -0.03F, -0.035F, -0.04F, -0.03F, -0.015F, 0F, 0.01F, 0.06F };
    private static final float[] flapScaley = { 0F, 0F, 0F, 0F, 0F, -0.5F, -1F, -1.5F, -2F, -2.5F, -2.8F };

    public void doMurderPilot(int i) {
        if (i == 0) {
            this.hierMesh().chunkVisible("Pilot1_D0", false);
            this.hierMesh().chunkVisible("Head1_D0", false);
            this.hierMesh().chunkVisible("HMask1_D0", false);
            this.hierMesh().chunkVisible("Pilot1_D1", true);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        if (i == 13) return false;
        return super.cutFM(i, j, actor);
    }
    // ************************************************************************************************

    public void moveCockpitDoor(float f) {
        this.resetYPRmodifier();
        xyz[1] = cvt(f, 0.01F, 0.99F, 0.0F, 0.6F);  // Move Cockpit door backwards by 0.6 units
        xyz[2] = cvt(f, 0.01F, 0.8F, 0.0F, 0.025F); // On the first 8/10th of opening, move Cockpit door upwards by 0.025 units
        ypr[2] = cvt(f, 0.01F, 0.6F, 0.0F, 3.5F);   // On the first 6/10th of opening, tilt Cockpit door backwards by 3.5 degrees
        this.hierMesh().chunkSetLocate("Blister1_D0", xyz, ypr);
        if (Config.isUSE_RENDER()) {
            if (Main3D.cur3D().cockpits != null && Main3D.cur3D().cockpits[0] != null) Main3D.cur3D().cockpits[0].onDoorMoved(f);
            this.setDoorSnd(f);
        }
    }

    // New Gear Animation Code,
    // historically accurate in terms of moving main gear before tail wheel
    // In order to do so, new Parameter "bDown" has been introduced to distinguish between
    // gear up and gear down
    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2, boolean bDown) {

        if (bDown) { // Gear Down

            // Always Set Gear direction "straight" when extending/retracting gears.
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, 0.0F);                                 // Left Wheel Direction

            // Wheel Doors of Left Main Gear move first when extending gears,
            // Left Door moves slightly before movement of Right Door starts
            // (Movement of Doors overlaps)
            hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.01F, 0.18F, 0.0F, -72F), 0.0F);     // Left Wheel Door 1
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, cvt(f, 0.05F, 0.22F, 0.0F, -68F), 0.0F);     // Left Wheel Door 2

            // When Doors of Left Main Gear are fully open,
            // Left Main Gear extends
            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f, 0.22F, 0.55F, 0.0F, -114F), 0.0F);    // Left Wheel Part 1
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f, 0.22F, 0.55F, 0.0F, -59F), 0.0F);     // Left Wheel Part 2
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f, 0.22F, 0.55F, 0.0F, -116.5F), 0.0F);  // Left Wheel Part 3

            // Right Main Gear extends slightly after Left Main Gear
            // (Movement overlaps)

            // Always Set Gear direction "straight" when extending/retracting gears.
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, 0.0F);                                 // Right Wheel Direction

            // Wheel Doors of Right Main Gear move second when extending gears,
            // Left Door moves slightly before movement of Right Door starts
            // (Movement of Doors overlaps)
            hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.1F, 0.27F, 0.0F, -72F), 0.0F);     // Right Wheel Door 1
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, cvt(f1, 0.14F, 0.31F, 0.0F, -68F), 0.0F);    // Right Wheel Door 2

            // When Doors of Right Main Gear are fully open,
            // Right Main Gear extends
            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f1, 0.31F, 0.64F, 0.0F, -114F), 0.0F);   // Right Wheel Part 1
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f1, 0.31F, 0.64F, 0.0F, -59F), 0.0F);    // Right Wheel Part 2
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f1, 0.31F, 0.64F, 0.0F, -116.5F), 0.0F); // Right Wheel Part 3

            // Tail Gear starts moving when Main Gears are fully extended

            // Always Set Gear direction "straight" when extending/retracting gears.
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);                                 // Tail Wheel Direction

            // Start Tail Gear extension with opening doors,
            // Left Door moves slightly before movement of Right Door starts
            // (Movement of Doors overlaps)
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f2, 0.64F, 0.75F, 0.0F, -70F), 0.0F);    // Tail Wheel Door 1
            hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f2, 0.68F, 0.79F, 0.0F, -70F), 0.0F);    // Tail Wheel Door 2

            // When Doors of Right Main Gear are fully open,
            // Tail Gear extends
            hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f2, 0.79F, 0.99F, 0.0F, -80F), 0.0F);    // Tail Wheel

        } else { // Gear Up

            // Retraction movement order is opposite to extension
            // apart from Main Gears move before Tail Gear

            hiermesh.chunkSetAngles("GearL2_D0", 0.0F, cvt(f1, 0.66F, 0.99F, 0.0F, -114F), 0.0F);   // Left Wheel Part 1
            hiermesh.chunkSetAngles("GearL4_D0", 0.0F, cvt(f1, 0.66F, 0.99F, 0.0F, -59F), 0.0F);    // Left Wheel Part 2
            hiermesh.chunkSetAngles("GearL5_D0", 0.0F, cvt(f1, 0.66F, 0.99F, 0.0F, -116.5F), 0.0F); // Left Wheel Part 3
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, 0.0F, 0.0F);                                 // Left Wheel Direction
            hiermesh.chunkSetAngles("GearL7_D0", 0.0F, cvt(f, 0.49F, 0.66F, 0.0F, -72F), 0.0F);     // Left Wheel Door 1
            hiermesh.chunkSetAngles("GearL8_D0", 0.0F, cvt(f, 0.45F, 0.61F, 0.0F, -68F), 0.0F);     // Left Wheel Door 2

            hiermesh.chunkSetAngles("GearR2_D0", 0.0F, cvt(f1, 0.56F, 0.89F, 0.0F, -114F), 0.0F);   // Right Wheel Part 1
            hiermesh.chunkSetAngles("GearR4_D0", 0.0F, cvt(f1, 0.56F, 0.89F, 0.0F, -59F), 0.0F);    // Right Wheel Part 2
            hiermesh.chunkSetAngles("GearR5_D0", 0.0F, cvt(f1, 0.56F, 0.89F, 0.0F, -116.5F), 0.0F); // Right Wheel Part 3
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, 0.0F, 0.0F);                                 // Right Wheel Direction
            hiermesh.chunkSetAngles("GearR7_D0", 0.0F, cvt(f1, 0.39F, 0.56F, 0.0F, -72F), 0.0F);    // Right Wheel Door 1
            hiermesh.chunkSetAngles("GearR8_D0", 0.0F, cvt(f1, 0.35F, 0.51F, 0.0F, -68F), 0.0F);    // Right Wheel Door 2

            hiermesh.chunkSetAngles("GearC2_D0", 0.0F, cvt(f2, 0.18F, 0.35F, 0.0F, -80F), 0.0F);    // Tail Wheel
            hiermesh.chunkSetAngles("GearC3_D0", 0.0F, 0.0F, 0.0F);                                 // Tail Wheel Direction
            hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f2, 0.05F, 0.18F, 0.0F, -70F), 0.0F);    // Tail Wheel Door 1
            hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f2, 0.01F, 0.14F, 0.0F, -70F), 0.0F);    // Tail Wheel Door 2
        }
    }

    public static void moveGear(HierMesh hiermesh, float f, float f1, float f2) {
        moveGear(hiermesh, f, f1, f2, true);
    }

    protected void moveGear(float f, float f1, float f2) {
        moveGear(this.hierMesh(), f, f1, f2, this.FM.CT.GearControl > 0.5F);
    }

    // ************************************************************************************************
    // Gear code for backward compatibility, older base game versions don't indepently move their gears
    public static void moveGear(HierMesh hiermesh, float f, boolean bDown) {
        moveGear(hiermesh, f, f, f, bDown); // re-route old style function calls to new code
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        moveGear(hiermesh, f, f, f, true); // re-route old style function calls to new code
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f, this.FM.CT.GearControl > 0.5F);
    }
    // ************************************************************************************************

    public void update(float f) {
        float controlRadiator, kangle1, kangle2;
        // Attention: VERY dirty fingers here!
        // We have to use reflection and break Java access modifier rules in order to get access
        // to the PRIVATE fields "kangle1" and "kangle2" of MOSQUITO abstract base class.
        // Not only do we access these fields, we even alter their values.
        // Children should do this under parental supervision only!
        try {
            controlRadiator = this.FM.EI.engines[0].getControlRadiator();
            kangle1 = Reflection.getFloat(this, "kangle1");
            if (Math.abs(kangle1 - controlRadiator) > 0.01F) {
                Reflection.setFloat(this, "kangle1", controlRadiator);
                this.hierMesh().chunkSetAngles("WaterL_D0", 0.0F, -20.0F * controlRadiator, 0.0F);
            }

            controlRadiator = this.FM.EI.engines[1].getControlRadiator();

            kangle2 = Reflection.getFloat(this, "kangle2");
            if (Math.abs(kangle2 - controlRadiator) > 0.01F) {
                Reflection.setFloat(this, "kangle2", controlRadiator);
                this.hierMesh().chunkSetAngles("WaterR_D0", 0.0F, -20.0F * controlRadiator, 0.0F);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.update(f);
    }

    public static String getSkinPrefix(String s, Regiment regiment) {
        if (regiment == null || regiment.country() == null) return "";
        if (regiment.country().equals("gb")) // Usually IL-2 uses default skins for "post invasion" and special
            // ones for "pre invasion", here we swap this so that per default
            // the Whirlie uses "pre invasion" skins in order to ensure
            // usage of "pre invasion" skin on older game versions which
            // can't handle multiple default skins according to mission date
            if (Mission.getMissionDate(true) > 19440605) // July 6th 1945 or later
                return "PostInvasion_";                  // use invasion stripes
        return "";
    }
}
