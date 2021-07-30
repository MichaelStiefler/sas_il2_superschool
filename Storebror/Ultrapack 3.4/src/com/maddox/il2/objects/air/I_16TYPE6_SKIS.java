package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;

public class I_16TYPE6_SKIS extends I_16FixedSkis implements TypeTNBFighter {

    public I_16TYPE6_SKIS() {
        this.flaperonAngle = 0.0F;
        this.aileronsAngle = 0.0F;
        this.hasTubeSight = false;
        this.pit = null;
        this.sideDoorOpened = false;
        this.removeSpinnerHub = false;
    }

    public void moveCockpitDoor(float f) {
        this.hierMesh().chunkSetAngles("Blister2_D0", 0.0F, 160F * f, 0.0F);
    }

    public void hitDaSilk() {
        super.hitDaSilk();
        if (!this.sideDoorOpened && this.FM.AS.bIsAboutToBailout && !this.FM.AS.isPilotDead(0)) {
            this.sideDoorOpened = true;
            this.FM.CT.bHasCockpitDoorControl = true;
            this.FM.CT.forceCockpitDoor(0.0F);
            this.FM.AS.setCockpitDoor(this, 1);
        }
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER() && !this.removeSpinnerHub) {
            boolean flag = this.hierMesh().isChunkVisible("PropRot1_D0");
            this.hierMesh().chunkVisible("PropHubRot1_D0", flag);
            this.hierMesh().chunkVisible("PropHub1_D0", !flag);
        }
        super.moveFan(f);
    }

    public void moveWheelSink() {
    }

    public void moveGear(float f) {
    }

    protected void moveAileron(float f) {
        this.aileronsAngle = f;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * f - this.flaperonAngle, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * f + this.flaperonAngle, 0.0F);
    }

    protected void moveFlap(float f) {
        this.flaperonAngle = f * 17F;
        this.hierMesh().chunkSetAngles("AroneL_D0", 0.0F, -30F * this.aileronsAngle - this.flaperonAngle, 0.0F);
        this.hierMesh().chunkSetAngles("AroneR_D0", 0.0F, -30F * this.aileronsAngle + this.flaperonAngle, 0.0F);
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                this.hierMesh().chunkVisible("Head1_D1", true);
                this.hierMesh().chunkVisible("pilotarm2_d0", false);
                this.hierMesh().chunkVisible("pilotarm1_d0", false);
                break;
        }
    }

    public void doRemoveBodyFromPlane(int i) {
        super.doRemoveBodyFromPlane(i);
        this.hierMesh().chunkVisible("pilotarm2_d0", false);
        this.hierMesh().chunkVisible("pilotarm1_d0", false);
    }

    public boolean hasTubeSight() {
        return this.hasTubeSight;
    }

    public void missionStarting() {
        super.missionStarting();
        this.customization();
        this.hierMesh().chunkVisible("pilotarm2_d0", true);
        this.hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    public void prepareCamouflage() {
        super.prepareCamouflage();
        this.hierMesh().chunkVisible("pilotarm2_d0", true);
        this.hierMesh().chunkVisible("pilotarm1_d0", true);
    }

    private void customization() {
        if (!Mission.isSingle()) return;
        boolean flag = false;
        boolean flag1 = false;
        int i = this.hierMesh().chunkFindCheck("CF_D0");
        int j = this.hierMesh().materialFindInChunk("Gloss1D0o", i);
        Mat mat = this.hierMesh().material(j);
        String s = mat.Name();
        if (s.startsWith("PaintSchemes/Cache")) try {
            s = s.substring(19);
            s = s.substring(0, s.indexOf("/"));
            String s1 = Main.cur().netFileServerSkin.primaryPath();
            File file = new File(HomePath.toFileSystemName(s1 + "/I-16type6_Skis/Customization.ini", 0));
            BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
            boolean flag2 = false;
            boolean flag3 = false;
            boolean flag4 = false;
            boolean flag5 = false;
            boolean flag6 = false;
            boolean flag7 = false;
            boolean flag8 = false;
            do {
                String s2;
                if ((s2 = bufferedreader.readLine()) == null) break;
                if (s2.equals("[TubeSight]")) {
                    flag2 = true;
                    flag3 = false;
                    flag4 = false;
                    flag5 = false;
                    flag6 = false;
                    flag7 = false;
                    flag8 = false;
                } else if (s2.equals("[OldStyleSkis]")) {
                    flag2 = false;
                    flag3 = true;
                    flag4 = false;
                    flag5 = false;
                    flag6 = false;
                    flag7 = false;
                    flag8 = false;
                } else if (s2.equals("[RadioWires]")) {
                    flag2 = false;
                    flag3 = false;
                    flag4 = true;
                    flag5 = false;
                    flag6 = false;
                    flag7 = false;
                    flag8 = false;
                } else if (s2.equals("[FullWheelCovers]")) {
                    flag2 = false;
                    flag3 = false;
                    flag4 = false;
                    flag5 = true;
                    flag6 = false;
                    flag7 = false;
                    flag8 = false;
                } else if (s2.equals("[RemoveSpinner]")) {
                    flag2 = false;
                    flag3 = false;
                    flag4 = false;
                    flag5 = false;
                    flag6 = false;
                    flag7 = false;
                    flag8 = true;
                } else if (s2.equals("[KeepSpinner]")) {
                    flag2 = false;
                    flag3 = false;
                    flag4 = false;
                    flag5 = false;
                    flag6 = false;
                    flag7 = true;
                    flag8 = false;
                } else if (s2.equals("[CanopyRails]")) {
                    flag2 = false;
                    flag3 = false;
                    flag4 = false;
                    flag5 = false;
                    flag6 = true;
                    flag7 = false;
                    flag8 = false;
                } else if (s2.equals(s)) {
                    if (flag2) this.hasTubeSight = true;
                    if (flag3) {
                        this.hierMesh().chunkVisible("SkiL1_D0", false);
                        this.hierMesh().chunkVisible("SkiR1_D0", false);
                        this.hierMesh().chunkVisible("OldSkiL1_D0", true);
                        this.hierMesh().chunkVisible("OldSkiR1_D0", true);
                    }
                    if (flag4) {
                        this.hierMesh().chunkVisible("RadioWire1_d0", true);
                        this.hierMesh().chunkVisible("RadioWire2_d0", true);
                    }
                    if (flag5) {
                        this.hierMesh().chunkVisible("GearCoverR_D0", true);
                        this.hierMesh().chunkVisible("GearCoverL_D0", true);
                    }
                    if (flag6) {
                        this.hierMesh().chunkVisible("Rails_d0", true);
                        this.hierMesh().chunkVisible("Blister2Rail_D0", true);
                        this.hierMesh().chunkVisible("Blister2_D0", false);
                        this.hierMesh().chunkVisible("T6Rail_D0", false);
                    }
                    if (flag7) flag1 = true;
                    if (flag8) flag = true;
                }
            } while (true);
            bufferedreader.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        else {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.hasTubeSight = true;
            if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) {
                this.hierMesh().chunkVisible("GearCoverR_D0", true);
                this.hierMesh().chunkVisible("GearCoverL_D0", true);
            }
        }
        if (this.pit != null) this.pit.setTubeSight(this.hasTubeSight);
        this.hierMesh().chunkVisible("Sight_D0", !this.hasTubeSight);
        this.hierMesh().chunkVisible("TubeSight_D0", this.hasTubeSight);
        if (flag || !flag1 && (this.FM.CT.Weapons[2] != null || this.FM.CT.Weapons[3] != null)) {
            this.removeSpinnerHub = true;
            this.hierMesh().chunkVisible("PropHubRot1_D0", false);
            this.hierMesh().chunkVisible("PropHub1_D0", false);
        }
    }

    public void registerPit(CockpitI_16TYPE6 cockpiti_16type6) {
        this.pit = cockpiti_16type6;
        if (cockpiti_16type6 != null) cockpiti_16type6.setTubeSight(this.hasTubeSight);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 11:
                this.hierMesh().chunkVisible("RadioWire1_d0", false);
                this.hierMesh().chunkVisible("RadioWire2_d0", false);
                break;

            case 36:
                this.hierMesh().chunkVisible("RadioWire2_d0", false);
                break;

            case 38:
                this.hierMesh().chunkVisible("RadioWire2_d0", false);
                break;

            case 19:
                this.FM.Gears.hitCentreGear();
                this.hierMesh().chunkVisible("RadioWire1_d0", false);
                this.hierMesh().chunkVisible("RadioWire2_d0", false);
                break;
        }
        return super.cutFM(i, j, actor);
    }

    private float            flaperonAngle;
    private float            aileronsAngle;
    private boolean          hasTubeSight;
    private CockpitI_16TYPE6 pit;
    private boolean          sideDoorOpened;
    private boolean          removeSpinnerHub;

    static {
        Class class1 = I_16TYPE6_SKIS.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "I-16");
        Property.set(class1, "meshName", "3DO/Plane/I-16type6(multi)/hier_skis.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFCSPar07());
        Property.set(class1, "meshName_ru", "3DO/Plane/I-16type6/hier_skis.him");
        Property.set(class1, "PaintScheme_ru", new PaintSchemeFCSPar07());
        Property.set(class1, "yearService", 1937F);
        Property.set(class1, "yearExpired", 1942F);
        Property.set(class1, "FlightModel", "FlightModels/I-16type6Skis.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitI_16TYPE6.class });
        Property.set(class1, "LOSElevation", 0.82595F);
        weaponTriggersRegister(class1, new int[] { 0, 0, 2, 2, 2, 2, 2, 2, 3, 3, 9, 9, 9, 9, 9, 9, 9, 9 });
        weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01",
                "_ExternalDev02", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06", "_ExternalDev07", "_ExternalDev08" });
    }
}
