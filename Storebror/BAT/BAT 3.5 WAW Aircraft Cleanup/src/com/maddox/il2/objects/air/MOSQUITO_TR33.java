package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class MOSQUITO_TR33 extends MOSQUITO implements TypeFighter, TypeStormovik {

    public MOSQUITO_TR33() {
        this.arrestor2 = 0.0F;
        this.bHasBoosters = true;
        this.boosterFireOutTime = -1L;
    }

    public void destroy() {
        this.doCutBoosters();
        super.destroy();
    }

    public void doFireBoosters() {
        Eff3DActor.New(this, this.findHook("_Booster1"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
        Eff3DActor.New(this, this.findHook("_Booster2"), null, 1.0F, "3DO/Effects/Tracers/HydrogenRocket/rocket.eff", 30F);
    }

    public void doCutBoosters() {
        for (int i = 0; i < 2; i++) {
            if (this.booster[i] != null) {
                this.booster[i].start();
                this.booster[i] = null;
            }
        }

    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (this.getBulletEmitterByHookName("_ExternalBomb03") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("TorpPylon_D0", false);
        } else {
            this.hierMesh().chunkVisible("TorpPylon_D0", true);
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

    public void moveArrestorHook(float f) {
        this.hierMesh().chunkSetAngles("Hook1_D0", 0.0F, 25F * f, 0.0F);
    }

    protected void moveWingFold(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("WingLOut_D0", 0.0F, 158F * f, 0.0F);
        hiermesh.chunkSetAngles("WingROut_D0", -0F, -158F * f, 0.0F);
    }

    public void moveWingFold(float f) {
        if (f < 0.001F) {
            this.setGunPodsOn(true);
            this.hideWingWeapons(false);
        } else {
            this.setGunPodsOn(false);
            this.FM.CT.WeaponControl[0] = false;
            this.hideWingWeapons(true);
        }
        this.moveWingFold(this.hierMesh(), f);
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
            if (this.bHasBoosters && (this.boosterFireOutTime == -1L) && this.FM.Gears.onGround() && (this.FM.EI.getPowerOutput() > 0.8F) && (this.FM.EI.engines[0].getStage() == 6) && (this.FM.EI.engines[1].getStage() == 6) && (this.FM.getSpeedKMH() > 5F)) {
                this.boosterFireOutTime = Time.current() + 30000L;
                this.doFireBoosters();
                this.FM.AS.setGliderBoostOn();
            }
            if (this.bHasBoosters && (this.boosterFireOutTime > 0L)) {
                if (Time.current() < this.boosterFireOutTime) {
                    this.FM.producedAF.x += 35000D;
                }
                if (Time.current() > (this.boosterFireOutTime + 10000L)) {
                    this.doCutBoosters();
                    this.FM.AS.setGliderBoostOff();
                    this.bHasBoosters = false;
                }
            }
        }
        if (this.FM.CT.getArrestor() > 0.9F) {
            if (this.FM.Gears.arrestorVAngle != 0.0F) {
                this.arrestor2 = Aircraft.cvt(this.FM.Gears.arrestorVAngle, -65F, 3F, 45F, -23F);
                this.hierMesh().chunkSetAngles("Hook_D0", 0.0F, this.arrestor2, 0.0F);
                if (this.FM.Gears.arrestorVAngle >= -35F) {

                }
            } else {
                float f1 = -41F * this.FM.Gears.arrestorVSink;
                if ((f1 < 0.0F) && (this.FM.getSpeedKMH() > 60F)) {
                    Eff3DActor.New(this, this.FM.Gears.arrestorHook, null, 1.0F, "3DO/Effects/Fireworks/04_Sparks.eff", 0.1F);
                }
                if ((f1 > 0.0F) && (this.FM.CT.getArrestor() < 0.9F)) {
                    f1 = 0.0F;
                }
                if (f1 > 6.2F) {
                    f1 = 6.2F;
                }
                this.arrestor2 += f1;
                if (this.arrestor2 < -23F) {
                    this.arrestor2 = -23F;
                } else if (this.arrestor2 > 45F) {
                    this.arrestor2 = 45F;
                }
                this.hierMesh().chunkSetAngles("Hook_D0", 0.0F, this.arrestor2, 0.0F);
            }
        }
    }

    private Bomb      booster[] = { null, null };
    protected boolean bHasBoosters;
    protected long    boosterFireOutTime;
    private float     arrestor2;

    static {
        Class class1 = MOSQUITO_TR33.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Mosquito");
        Property.set(class1, "meshName", "3DO/Plane/Mosquito_TR_Mk33(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar04());
        Property.set(class1, "meshName_gb", "3DO/Plane/Mosquito_TR_Mk33(GB)/hier.him");
        Property.set(class1, "PaintScheme_gb", new PaintSchemeFMPar06());
        Property.set(class1, "yearService", 1946.8F);
        Property.set(class1, "yearExpired", 1947.7F);
        Property.set(class1, "FlightModel", "FlightModels/Mosquito-TR33.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitMosquito6.class });
        Property.set(class1, "LOSElevation", 0.6731F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb01", "_ExternalBomb02", "_BombSpawn01", "_BombSpawn02", "_ExternalDev05", "_ExternalDev06", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08" });
    }
}
