package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.GunProperties;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Property;

public class DXXI_SARJA3_SARVANTO extends DXXI implements TypeAcePlane {

    public DXXI_SARJA3_SARVANTO() {
        this.skiAngleL = 0.0F;
        this.skiAngleR = 0.0F;
        this.spring = 0.15F;
    }

    public float getWheelWidth(int i) {
        return i > 1 ? 0.2F : 0.5F;
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        this.FM.Skill = 3;
        this.JormaSarvanto();
        if (Config.isUSE_RENDER() && (World.cur().camouflage == 1)) {
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
        }
    }

    private void JormaSarvanto() {
        for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
            com.maddox.il2.ai.BulletEmitter abulletemitter[] = this.FM.CT.Weapons[i];
            if (abulletemitter != null) {
                for (int j = 0; j < abulletemitter.length; j++) {
                    com.maddox.il2.ai.BulletEmitter bulletemitter = abulletemitter[j];
                    if (bulletemitter instanceof Gun) {
                        GunProperties gunproperties = ((GunGeneric) ((Gun) bulletemitter)).prop;
                        BulletProperties abulletproperties[] = gunproperties.bullet;
                        if (abulletproperties != null) {
                            for (int k = 0; k < abulletproperties.length; k++) {
                                abulletproperties[k].powerType = 3;
                                abulletproperties[k].massa = 0.02F;
                                abulletproperties[k].kalibr = 4.442131E-005F;
                                abulletproperties[k].speed = 835F;
                                if (abulletproperties[k].power != 0.0F) {
                                    abulletproperties[k].power = 0.002F;
                                }
                            }

                        }
                    }
                }

            }
        }

    }

    public static void moveGear(HierMesh hiermesh, float f) {
        if ((World.cur().camouflage == 1) && (World.Rnd().nextFloat() > 0.1F)) {
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
            this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 9F * f1, Aircraft.cvt(f2, -1F, 1.0F, -8F, 9.5F));
            this.hierMesh().chunkSetAngles("pilotarm2_d0", Aircraft.cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, Aircraft.cvt(f1, -1F, 1.0F, 6F, -8F) - Aircraft.cvt(f2, -1F, 1.0F, -37F, 35F));
            this.hierMesh().chunkSetAngles("pilotarm1_d0", 0.0F, 0.0F, Aircraft.cvt(f1, -1F, 1.0F, -16F, 14F) + Aircraft.cvt(f2, -1F, 0.0F, -61F, 0.0F) + Aircraft.cvt(f2, 0.0F, 1.0F, 0.0F, 43F));
            if (World.cur().camouflage == 1) {
                float f3 = Aircraft.cvt(this.FM.getSpeed(), 30F, 100F, 1.0F, 0.0F);
                float f4 = Aircraft.cvt(this.FM.getSpeed(), 0.0F, 30F, 0.0F, 0.5F);
                if (this.FM.Gears.gWheelSinking[0] > 0.0F) {
                    this.skiAngleL = (0.5F * this.skiAngleL) + (0.5F * this.FM.Or.getTangage());
                    if (this.skiAngleL > 20F) {
                        this.skiAngleL -= this.spring;
                    }
                    this.hierMesh().chunkSetAngles("GearL21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + this.skiAngleL, World.Rnd().nextFloat(f4, f4));
                    if ((this.FM.Gears.gWheelSinking[1] == 0.0F) && (this.FM.Or.getRoll() < 365F) && (this.FM.Or.getRoll() > 355F)) {
                        this.skiAngleR = this.skiAngleL;
                        this.hierMesh().chunkSetAngles("GearR21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + this.skiAngleR, World.Rnd().nextFloat(f4, f4));
                    }
                } else {
                    if (this.skiAngleL > ((f3 * -10F) + 0.01D)) {
                        this.skiAngleL -= this.spring;
                    } else if (this.skiAngleL < ((f3 * -10F) - 0.01D)) {
                        this.skiAngleL += this.spring;
                    }
                    this.hierMesh().chunkSetAngles("GearL21_D0", 0.0F, this.skiAngleL, 0.0F);
                }
                if (this.FM.Gears.gWheelSinking[1] > 0.0F) {
                    this.skiAngleR = (0.5F * this.skiAngleR) + (0.5F * this.FM.Or.getTangage());
                    if (this.skiAngleR > 20F) {
                        this.skiAngleR -= this.spring;
                    }
                    this.hierMesh().chunkSetAngles("GearR21_D0", World.Rnd().nextFloat(-f4, f4), World.Rnd().nextFloat(-f4 * 2.0F, f4 * 2.0F) + this.skiAngleR, World.Rnd().nextFloat(f4, f4));
                } else {
                    if (this.skiAngleR > ((f3 * -10F) + 0.01D)) {
                        this.skiAngleR -= this.spring;
                    } else if (this.skiAngleR < ((f3 * -10F) - 0.01D)) {
                        this.skiAngleR += this.spring;
                    }
                    this.hierMesh().chunkSetAngles("GearR21_D0", 0.0F, this.skiAngleR, 0.0F);
                }
                this.hierMesh().chunkSetAngles("GearC11_D0", 0.0F, (this.skiAngleL + this.skiAngleR) / 2.0F, 0.0F);
            }
        }
    }

    public void sfxWheels() {
        if (!this.hasSkis) {
            super.sfxWheels();
        }
    }

    private float skiAngleL;
    private float skiAngleR;
    private float spring;

    static {
        Class class1 = DXXI_SARJA3_SARVANTO.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "D.XXI");
        Property.set(class1, "meshName", "3DO/Plane/DXXI_SARJA3_EARLY(Sarvanto)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar00DXXI());
        Property.set(class1, "yearService", 1939F);
        Property.set(class1, "yearExpired", 1940F);
        Property.set(class1, "FlightModel", "FlightModels/FokkerS3Early.fmd");
        Property.set(class1, "LOSElevation", 0.8472F);
        Property.set(class1, "originCountry", PaintScheme.countryFinland);
        Property.set(class1, "cockpitClass", new Class[] { CockpitDXXI_SARJA3_SARVANTO.class });
        Aircraft.weaponTriggersRegister(class1, new int[4]);
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04" });
    }
}
