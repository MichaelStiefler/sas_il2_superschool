package com.maddox.il2.objects.air;

import java.security.SecureRandom;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class ME_262SB1A extends ME_262 implements TypeStormovik, TypeX4Carrier, TypeRadarLiSN2Carrier {

    public ME_262SB1A() {
        this.deltaAzimuth = 0.0F;
        this.deltaTangage = 0.0F;
        this.radarGain = 50;
        this.radarMode = 0;
        this.booster = null;
        this.bHasBoosters = false;
        this.boosterFireOutTime = -1L;
        SecureRandom secRandom = new SecureRandom();
        secRandom.setSeed(System.currentTimeMillis());
        RangeRandom rr = new RangeRandom(secRandom.nextLong());
        for (int i = 0; i < this.rndgear.length; i++) {
            this.rndgear[i] = rr.nextFloat(0.0F, 0.15F);
        }

    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
    }

    public void doCutBoosters() {
        if (this.booster != null) {
            this.booster.start();
            this.booster = null;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.thisWeaponsName.equalsIgnoreCase("default") || this.thisWeaponsName.equalsIgnoreCase("24r4m")) {
            this.hierMesh().chunkVisible("PylonL_D0", false);
            this.hierMesh().chunkVisible("PylonR_D0", false);
        } else if (this.thisWeaponsName.toLowerCase().startsWith("1x")) {
            this.hierMesh().chunkVisible("PylonR_D0", false);
        }
        if ((this.thisWeaponsName.toLowerCase().indexOf("wfrgr") < 0) && (this.thisWeaponsName.toLowerCase().indexOf("r4m") < 0)) {
            this.hierMesh().hideSubTrees("Antenna_L_D0");
            this.hierMesh().hideSubTrees("Antenna_R_D0");
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
                break;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        if (!(this.FM instanceof Pilot)) {
            return;
        }
        if (this.bHasBoosters) {
            if (((this.FM.getAltitude() - World.land().HQ_Air(this.FM.Loc.x, this.FM.Loc.y)) > 300D) && (this.boosterFireOutTime == -1L) && (this.FM.Loc.z != 0.0D) && (World.Rnd().nextFloat() < 0.05F)) {
                this.doCutBoosters();
                this.FM.AS.setGliderBoostOff();
                this.bHasBoosters = false;
            }
            if (this.bHasBoosters && (this.boosterFireOutTime == -1L) && this.FM.Gears.onGround() && (this.FM.EI.getPowerOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[1].getStage() == 6) && (this.FM.getSpeedKMH() > 20F)) {
                this.boosterFireOutTime = Time.current() + 30000L;
                this.doFireBoosters();
                this.FM.AS.setGliderBoostOn();
            }
            if (this.bHasBoosters && (this.boosterFireOutTime > 0L)) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 15000D;
                }
                if (Time.current() > (this.boosterFireOutTime + 10000L)) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
            }
        }
    }

    public void setOnGround(Point3d point3d, Orient orient, Vector3d vector3d) {
        super.setOnGround(point3d, orient, vector3d);
        try {
            this.booster = new BombStarthilfe109500();
            ((Actor) (this.booster)).pos.setBase(this, this.findHook("_BoosterH1"), false);
            ((Actor) (this.booster)).pos.resetAsBase();
            this.booster.drawing(true);
            this.bHasBoosters = true;
        } catch (Exception exception) {
            this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
        }
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float frontWheelPos, boolean bDown, float rnd[]) {
        ME_262SB1A.myResetYPRmodifier();
        Aircraft.xyz[2] = ME_262SB1A.smoothCvt(frontWheelPos, 0.01F + rnd[2], 0.11F + rnd[2], 0.0F, 0.1F);
        Aircraft.ypr[1] = ME_262SB1A.smoothCvt(frontWheelPos, 0.01F + rnd[2], 0.11F + rnd[2], 0.0F, -90F);
        hiermesh.chunkSetLocate("GearC5_D0", Aircraft.xyz, Aircraft.ypr);
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, ME_262SB1A.smoothCvt(frontWheelPos, 0.11F + rnd[2], 0.84F + rnd[2], 0.0F, 103F), 0.0F);
        hiermesh.chunkSetAngles("GearC21_D0", 0.0F, ME_262SB1A.smoothCvt(frontWheelPos, 0.15F + rnd[2], 0.35F + rnd[2], 0.0F, -90F), 0.0F);
        if (frontWheelPos < 0.2F) {
            hiermesh.chunkSetAngles("GearC6_D0", 0.0F, ME_262SB1A.smoothCvt(frontWheelPos, 0.01F + rnd[2], 0.11F + rnd[2], 0.0F, -90F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearC6_D0", 0.0F, ME_262SB1A.smoothCvt(frontWheelPos, 0.51F + rnd[2], 0.65F + rnd[2], -90F, 0.0F), 0.0F);
        }
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, ME_262SB1A.smoothCvt(leftGearPos, 0.15F + rnd[0], 0.74F + rnd[0], 0.0F, 73F), 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, ME_262SB1A.smoothCvt(rightGearPos, 0.25F + rnd[1], 0.84F + rnd[1], 0.0F, 73F), 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, ME_262SB1A.smoothCvt(leftGearPos, 0.15F + rnd[0], 0.74F + rnd[0], 0.0F, 82F), 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, ME_262SB1A.smoothCvt(rightGearPos, 0.25F + rnd[1], 0.84F + rnd[1], 0.0F, 82F), 0.0F);
        if (leftGearPos < 0.2F) {
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, ME_262SB1A.smoothCvt(leftGearPos, 0.05F + rnd[0], 0.15F + rnd[0], 0.0F, -90F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearL6_D0", 0.0F, ME_262SB1A.smoothCvt(leftGearPos, 0.45F + rnd[0], 0.55F + rnd[0], -90F, 0.0F), 0.0F);
        }
        if (rightGearPos < 0.3F) {
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, ME_262SB1A.smoothCvt(rightGearPos, 0.15F + rnd[1], 0.25F + rnd[1], 0.0F, -90F), 0.0F);
        } else {
            hiermesh.chunkSetAngles("GearR6_D0", 0.0F, ME_262SB1A.smoothCvt(rightGearPos, 0.55F + rnd[1], 0.65F + rnd[1], -90F, 0.0F), 0.0F);
        }
    }

    public static void moveGear(HierMesh hiermesh, float leftGearPos, float rightGearPos, float frontWheelPos) {
        ME_262SB1A.moveGear(hiermesh, leftGearPos, rightGearPos, frontWheelPos, true, ME_262SB1A.rndgearnull);
    }

    protected void moveGear(float leftGearPos, float rightGearPos, float frontWheelPos) {
        ME_262SB1A.moveGear(this.hierMesh(), leftGearPos, rightGearPos, frontWheelPos, this.FM.CT.GearControl > 0.5F, this.rndgear);
    }

    public static void moveGear(HierMesh hiermesh, float gearPos, boolean bDown) {
        ME_262SB1A.moveGear(hiermesh, gearPos, gearPos, gearPos, bDown, ME_262SB1A.rndgearnull);
    }

    public static void moveGear(HierMesh hiermesh, float gearPos) {
        ME_262SB1A.moveGear(hiermesh, gearPos, gearPos, gearPos, true, ME_262SB1A.rndgearnull);
    }

    protected void moveGear(float gearPos) {
        ME_262SB1A.moveGear(this.hierMesh(), gearPos, gearPos, gearPos, this.FM.CT.GearControl > 0.5F, this.rndgear);
    }

    static void myResetYPRmodifier() {
        Aircraft.xyz[0] = Aircraft.xyz[1] = Aircraft.xyz[2] = Aircraft.ypr[0] = Aircraft.ypr[1] = Aircraft.ypr[2] = 0.0F;
    }

    static float smoothCvt(float inputValue, float inMin, float inMax, float outMin, float outMax) {
        inputValue = Math.min(Math.max(inputValue, inMin), inMax);
        return outMin + ((outMax - outMin) * ((-0.5F * (float) Math.cos(((inputValue - inMin) / (inMax - inMin)) * Math.PI)) + 0.5F));
    }

    public void moveWheelSink() {
        this.resetYPRmodifier();
        float f = this.FM.Gears.gWheelSinking[2];
        Aircraft.xyz[1] = Aircraft.cvt(f, 0.0F, 0.19F, 0.0F, 0.19F);
        this.hierMesh().chunkSetLocate("GearC22_D0", Aircraft.xyz, Aircraft.ypr);
    }

    protected void moveRudder(float f) {
        this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
        if (this.FM.CT.getGear() > 0.75F) {
            this.hierMesh().chunkSetAngles("GearC21_D0", 0.0F, -90F + (40F * f), 0.0F);
        }
    }

    public void typeX4CAdjSidePlus() {
        this.radarMode++;
        if (this.radarMode > 1) {
            this.radarMode = 0;
        }
    }

    public void typeX4CAdjSideMinus() {
        this.radarMode--;
        if (this.radarMode < 0) {
            this.radarMode = 1;
        }
    }

    public void typeX4CAdjAttitudePlus() {
        this.radarGain += 10;
        if (this.radarGain > 100) {
            this.radarGain = 100;
        }
    }

    public void typeX4CAdjAttitudeMinus() {
        this.radarGain -= 10;
        if (this.radarGain < 0) {
            this.radarGain = 0;
        }
    }

    public void typeX4CResetControls() {
        this.radarGain = 50;
    }

    public float typeX4CgetdeltaAzimuth() {
        return this.deltaAzimuth;
    }

    public float typeX4CgetdeltaTangage() {
        return this.deltaTangage;
    }

    public void setCurPilot(int theCurPilot) {
        System.out.println("### Attempt to set Pilot Index on single crew plane!!! ###");
    }

    public int getCurPilot() {
        return 1;
    }

    public int getRadarGain() {
        return this.radarGain;
    }

    public int getRadarMode() {
        return this.radarMode;
    }

    private float     deltaAzimuth;
    private float     deltaTangage;
    private int       radarGain;
    private int       radarMode;
    private Bomb      booster;
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;
    float             rndgear[]     = { 0.0F, 0.0F, 0.0F };
    static float      rndgearnull[] = { 0.0F, 0.0F, 0.0F };

    static {
        Class class1 = ME_262SB1A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me 262");
        Property.set(class1, "meshName", "3DO/Plane/Me-262SB-1a/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
        Property.set(class1, "yearService", 1944.1F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262SB1a.fmd:Me-262SB");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262Sb.class });
        Property.set(class1, "LOSElevation", 0.74615F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalBomb09", "_ExternalBomb10", "_ExternalBomb11", "_ExternalBomb12", "_ExternalBomb13", "_ExternalBomb14", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06" });
    }
}
