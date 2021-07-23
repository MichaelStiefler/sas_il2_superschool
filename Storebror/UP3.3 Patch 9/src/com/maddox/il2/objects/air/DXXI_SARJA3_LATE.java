package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;

public class DXXI_SARJA3_LATE extends DXXI implements TypeScout {

    public DXXI_SARJA3_LATE() {
        this.hasRevi = false;
        this.pit = null;
        this.skiAngleL = 0.0F;
        this.skiAngleR = 0.0F;
        this.spring = 0.15F;
        this.gyroDelta = 0.0F;
    }

    public void missionStarting() {
        super.missionStarting();
        this.customization();
        if (this.FM.isStationedOnGround()) this.gyroDelta += (float) Math.random() * 360F;
    }

    public void registerPit(CockpitDXXI_SARJA3_LATE cockpitdxxi_sarja3_late) {
        this.pit = cockpitdxxi_sarja3_late;
    }

    public boolean hasRevi() {
        return this.hasRevi;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.hasSelfSealingTank = true;
        if (Config.isUSE_RENDER() && World.cur().camouflage == 1) {
            this.hasSkis = true;
            this.hierMesh().chunkVisible("GearL1_D0", false);
            this.hierMesh().chunkVisible("GearL22_D0", false);
            this.hierMesh().chunkVisible("GearR1_D0", false);
            this.hierMesh().chunkVisible("GearR22_D0", false);
            this.hierMesh().chunkVisible("GearC1_D0", false);
            this.hierMesh().chunkVisible("GearL31_D0", false);
            this.hierMesh().chunkVisible("GearL32_D0", false);
            this.hierMesh().chunkVisible("GearR31_D0", false);
            this.hierMesh().chunkVisible("GearR32_D0", false);
            this.hierMesh().chunkVisible("GearC11_D0", true);
            this.hierMesh().chunkVisible("GearL11_D0", true);
            this.hierMesh().chunkVisible("GearL21_D0", true);
            this.hierMesh().chunkVisible("GearR11_D0", true);
            this.hierMesh().chunkVisible("GearR21_D0", true);
            this.FM.CT.bHasBrakeControl = false;
        } else if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.01F) this.removeWheelSpats();
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.Or.getKren() < -10F || this.FM.Or.getKren() > 10F) this.gyroDelta -= 0.01D;
    }

    private void removeWheelSpats() {
        this.hierMesh().chunkVisible("GearR22_D0", false);
        this.hierMesh().chunkVisible("GearL22_D0", false);
        this.hierMesh().chunkVisible("GearR22_D2", true);
        this.hierMesh().chunkVisible("GearL22_D2", true);
        this.hierMesh().chunkVisible("gearl31_d0", true);
        this.hierMesh().chunkVisible("gearl32_d0", true);
        this.hierMesh().chunkVisible("gearr31_d0", true);
        this.hierMesh().chunkVisible("gearr32_d0", true);
    }

    private void customization() {
        if (!Mission.isSingle()) return;
        int i = this.hierMesh().chunkFindCheck("cf_D0");
        int j = this.hierMesh().materialFindInChunk("Gloss1D0o", i);
        Mat mat = this.hierMesh().material(j);
        String s = mat.Name();
        if (s.startsWith("PaintSchemes/Cache")) try {
            s = s.substring(19);
            s = s.substring(0, s.indexOf("/"));
            String s1 = Main.cur().netFileServerSkin.primaryPath();
            File file = new File(HomePath.toFileSystemName(s1 + "/DXXI_SARJA3_LATE/Customization.ini", 0));
            BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            do {
                String s2;
                if ((s2 = bufferedreader.readLine()) == null) break;
                if (s2.equals("[ReflectorSight]")) {
                    flag = true;
                    flag1 = false;
                    flag2 = false;
                } else if (s2.equals("[NoWheelSpats]")) {
                    flag = false;
                    flag1 = true;
                    flag2 = false;
                } else if (s2.equals("[WingSlots]")) {
                    flag = false;
                    flag1 = false;
                    flag2 = true;
                } else if (s2.equals(s)) {
                    if (flag) {
                        this.hierMesh().chunkVisible("Revi_D0", true);
                        this.hierMesh().chunkVisible("Goertz_D0", false);
                        this.hasRevi = true;
                    }
                    if (flag1 && World.cur().camouflage != 1) this.removeWheelSpats();
                    if (flag2) {
                        this.hierMesh().chunkVisible("SlotCoverLMid", false);
                        this.hierMesh().chunkVisible("SlotCoverRMid", false);
                        this.hierMesh().chunkVisible("SlotCoverLOut", false);
                        this.hierMesh().chunkVisible("SlotCoverROut", false);
                    }
                }
            } while (true);
            bufferedreader.close();
        } catch (Exception exception) {
            System.out.println(exception);
        }
        else if (World.Rnd().nextFloat(0.0F, 1.0F) > 0.6F) {
            this.hierMesh().chunkVisible("Revi_D0", true);
            this.hierMesh().chunkVisible("Goertz_D0", false);
            this.hasRevi = true;
        }
        if (this.hasRevi && this.pit != null) this.pit.setRevi();
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        if (World.cur().camouflage == 1 && World.Rnd().nextFloat() > 0.1F) {
            hiermesh.chunkVisible("GearL1_D0", false);
            hiermesh.chunkVisible("GearL22_D0", false);
            hiermesh.chunkVisible("GearR1_D0", false);
            hiermesh.chunkVisible("GearR22_D0", false);
            hiermesh.chunkVisible("GearC1_D0", false);
            hiermesh.chunkVisible("GearL31_D0", false);
            hiermesh.chunkVisible("GearL32_D0", false);
            hiermesh.chunkVisible("GearR31_D0", false);
            hiermesh.chunkVisible("GearR32_D0", false);
            hiermesh.chunkVisible("GearC11_D0", true);
            hiermesh.chunkVisible("GearL11_D0", true);
            hiermesh.chunkVisible("GearL21_D0", true);
            hiermesh.chunkVisible("GearR11_D0", true);
            hiermesh.chunkVisible("GearR21_D0", true);
            hiermesh.chunkSetAngles("GearL21_D0", 0.0F, 12F, 0.0F);
            hiermesh.chunkSetAngles("GearR21_D0", 0.0F, 12F, 0.0F);
            hiermesh.chunkSetAngles("GearC11_D0", 0.0F, 12F, 0.0F);
        }
    }

    protected void moveFan(float f) {
        if (Config.isUSE_RENDER()) {
            super.moveFan(f);
            float f1 = this.FM.CT.getAileron();
            float f2 = this.FM.CT.getElevator();
            this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 9F * f1, cvt(f2, -1F, 1.0F, -8F, 9.5F));
            this.hierMesh().chunkSetAngles("pilotarm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - cvt(f2, -1F, 1.0F, -37F, 35F));
            this.hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f2, -1F, 0.0F, -61F, 0.0F) + cvt(f2, 0.0F, 1.0F, 0.0F, 43F));
            if (World.cur().camouflage == 1) {
                float f3 = Aircraft.cvt(this.FM.getSpeed(), 30F, 100F, 1.0F, 0.0F);
                float f4 = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
                if (this.FM.Gears.gWheelSinking[0] > 0.0F) {
                    this.skiAngleL = 0.5F * this.skiAngleL + 0.5F * this.FM.Or.getTangage();
                    if (this.skiAngleL > 20F) this.skiAngleL = this.skiAngleL - this.spring;
                    this.hierMesh().chunkSetAngles("GearL21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + this.skiAngleL, World.Rnd().nextFloat(f4, f4));
                    if (this.FM.Gears.gWheelSinking[1] == 0.0F && this.FM.Or.getRoll() < 365F && this.FM.Or.getRoll() > 355F) {
                        this.skiAngleR = this.skiAngleL;
                        this.hierMesh().chunkSetAngles("GearR21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + this.skiAngleR, World.Rnd().nextFloat(f4, f4));
                    }
                } else {
                    if (this.skiAngleL > f3 * -10F + 0.01D) this.skiAngleL = this.skiAngleL - this.spring;
                    else if (this.skiAngleL < f3 * -10F - 0.01D) this.skiAngleL = this.skiAngleL + this.spring;
                    this.hierMesh().chunkSetAngles("GearL21_D0", 0.0F, this.skiAngleL, 0.0F);
                }
                if (this.FM.Gears.gWheelSinking[1] > 0.0F) {
                    this.skiAngleR = 0.5F * this.skiAngleR + 0.5F * this.FM.Or.getTangage();
                    if (this.skiAngleR > 20F) this.skiAngleR = this.skiAngleR - this.spring;
                    this.hierMesh().chunkSetAngles("GearR21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + this.skiAngleR, World.Rnd().nextFloat(f4, f4));
                } else {
                    if (this.skiAngleR > f3 * -10F + 0.01D) this.skiAngleR = this.skiAngleR - this.spring;
                    else if (this.skiAngleR < f3 * -10F - 0.01D) this.skiAngleR = this.skiAngleR + this.spring;
                    this.hierMesh().chunkSetAngles("GearR21_D0", 0.0F, this.skiAngleR, 0.0F);
                }
                this.hierMesh().chunkSetAngles("GearC11_D0", 0.0F, (this.skiAngleL + this.skiAngleR) / 2.0F, 0.0F);
            }
        }
    }

    protected void nextDMGLevel(String s, int i, Actor actor) {
        super.nextDMGLevel(s, i, actor);
        if (this.FM.isPlayers()) this.bChangedPit = true;
        if (World.cur().camouflage != 1) {
            if (this.hierMesh().isChunkVisible("GearR22_D2") && !this.hierMesh().isChunkVisible("gearr31_d0")) {
                this.hierMesh().chunkVisible("gearr31_d0", true);
                this.hierMesh().chunkVisible("gearr32_d0", true);
                Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("GearR22_D1"));
                wreckage.collide(true);
                Vector3d vector3d = new Vector3d();
                vector3d.set(this.FM.Vwld);
                wreckage.setSpeed(vector3d);
            }
            if (this.hierMesh().isChunkVisible("GearL22_D2") && !this.hierMesh().isChunkVisible("gearl31_d0")) {
                this.hierMesh().chunkVisible("gearl31_d0", true);
                this.hierMesh().chunkVisible("gearl32_d0", true);
                Wreckage wreckage1 = new Wreckage(this, this.hierMesh().chunkFind("GearL22_D1"));
                wreckage1.collide(true);
                Vector3d vector3d1 = new Vector3d();
                vector3d1.set(this.FM.Vwld);
                wreckage1.setSpeed(vector3d1);
            }
        } else {
            if ((this.hierMesh().isChunkVisible("GearR11_D1") || this.hierMesh().isChunkVisible("GearR21_D2")) && !this.hierMesh().isChunkVisible("gearr31_d0")) {
                this.hierMesh().chunkVisible("GearR11_D1", true);
                this.hierMesh().chunkVisible("GearR11_D0", false);
                this.hierMesh().chunkVisible("gearr31_d0", true);
                this.hierMesh().chunkVisible("gearr32_d0", true);
                Wreckage wreckage2 = new Wreckage(this, this.hierMesh().chunkFind("GearR11_D0"));
                wreckage2.collide(true);
                Vector3d vector3d2 = new Vector3d();
                vector3d2.set(this.FM.Vwld);
                wreckage2.setSpeed(vector3d2);
            }
            if ((this.hierMesh().isChunkVisible("GearL11_D1") || this.hierMesh().isChunkVisible("GearL21_D2")) && !this.hierMesh().isChunkVisible("gearl31_d0")) {
                this.hierMesh().chunkVisible("GearL11_D1", true);
                this.hierMesh().chunkVisible("GearL11_D0", false);
                this.hierMesh().chunkVisible("gearl31_d0", true);
                this.hierMesh().chunkVisible("gearl32_d0", true);
                Wreckage wreckage3 = new Wreckage(this, this.hierMesh().chunkFind("GearL11_D0"));
                wreckage3.collide(true);
                Vector3d vector3d3 = new Vector3d();
                vector3d3.set(this.FM.Vwld);
                wreckage3.setSpeed(vector3d3);
            }
        }
    }

    public void sfxWheels() {
        if (!this.hasSkis) super.sfxWheels();
    }

    public void auxPlus(int i) {
        switch (i) {
            case 1:
                this.gyroDelta++;
                break;
        }
    }

    public void auxMinus(int i) {
        switch (i) {
            case 1:
                this.gyroDelta--;
                break;
        }
    }

    private boolean                 hasRevi;
    private CockpitDXXI_SARJA3_LATE pit;
    private float                   skiAngleL;
    private float                   skiAngleR;
    private float                   spring;
//    public float                    gyroDelta;

    static {
        Class class1 = DXXI_SARJA3_LATE.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D.XXI");
        Property.set(class1, "meshName_fi", "3DO/Plane/DXXI_SARJA3_LATE/hier.him");
        Property.set(class1, "meshName", "3DO/Plane/DXXI_SARJA3_LATE/hierMulti.him");
        Property.set(class1, "PaintScheme_fi", new PaintSchemeFMPar00DXXI());
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00DXXI());
        Property.set(class1, "yearService", 1941F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/FokkerS3LATE.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitDXXI_SARJA3_LATE.class });
        Property.set(class1, "LOSElevation", 0.8472F);
        Property.set(class1, "originCountry", PaintScheme.countryFinland);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
