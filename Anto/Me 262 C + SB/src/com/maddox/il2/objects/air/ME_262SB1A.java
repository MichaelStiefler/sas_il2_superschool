package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class ME_262SB1A extends ME_262 implements TypeStormovik {

    public ME_262SB1A() {
        this.booster = new Bomb[1];
        this.bHasBoosters = true;
        this.boosterFireOutTime = -1L;
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
    }

    public void doCutBoosters() {
        if (this.booster[0] != null) {
            this.booster[0].start();
            this.booster[0] = null;
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        try {
            this.booster[0] = new BombStarthilfe109500();
            ((Actor) (this.booster[0])).pos.setBase(this, this.findHook("_BoosterH1"), false);
            ((Actor) (this.booster[0])).pos.resetAsBase();
            this.booster[0].drawing(true);
        } catch (Exception exception) {
            this.debugprintln("Structure corrupt - can't hang Starthilferakete..");
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
            if ((this.FM.getAltitude() > 300F) && (this.boosterFireOutTime == -1L) && (this.FM.Loc.z != 0.0D) && (World.Rnd().nextFloat() < 0.05F)) {
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

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 103F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC21_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearC6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.11F, 0.0F, -90F), 0.0F);
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 73F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 73F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 88F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 88F * f, 0.0F);
        float f1 = Math.max(-f * 1500F, -90F);
        hiermesh.chunkSetAngles("GearL6_D0", 0.0F, f1, 0.0F);
        hiermesh.chunkSetAngles("GearR6_D0", 0.0F, f1, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
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

    private Bomb      booster[];
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;

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
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalDev03", "_ExternalDev04", "_ExternalDev05", "_ExternalDev06" });
    }
}
