package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.rts.Property;

public class ME_262C1A extends ME_262 {

    public ME_262C1A() {
        this.flame = null;
        this.dust = null;
        this.trail = null;
        this.sprite = null;
        this.turboexhaust = null;
        this.bCockpitNVentilated = false;
    }

    protected void hitBone(String s, Shot shot, Point3d point3d) {
        this.hitBone(s, shot, point3d);
        if (s.startsWith("xxtank")) {
            if (s.endsWith("front")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), shot) > 0.0F) {
                    this.debuggunnery("T-Stoff Tank: Pierced..");
                }
                this.FM.AS.hitTank(shot.initiator, 2, World.Rnd().nextInt(1, 4));
                this.bCockpitNVentilated = true;
            }
            if (s.endsWith("rear")) {
                if (this.getEnergyPastArmor(World.Rnd().nextFloat(1.0F, 7.9F), shot) > 0.0F) {
                    this.debuggunnery("C-Stoff Tank: Pierced..");
                }
                this.FM.AS.hitTank(shot.initiator, 3, World.Rnd().nextInt(1, 4));
                if (World.Rnd().nextFloat() < 0.25F) {
                    this.FM.AS.hitEngine(shot.initiator, 2, 100);
                }
            }
        }
        if (s.startsWith("xxengine3") && (this.getEnergyPastArmor(4.96F, shot) > 0.0F) && (World.Rnd().nextFloat() < 0.25F)) {
            this.FM.AS.hitEngine(shot.initiator, 2, 100);
        }
    }

    public void destroy() {
        if (Actor.isValid(this.flame)) {
            this.flame.destroy();
        }
        if (Actor.isValid(this.dust)) {
            this.dust.destroy();
        }
        if (Actor.isValid(this.trail)) {
            this.trail.destroy();
        }
        if (Actor.isValid(this.sprite)) {
            this.sprite.destroy();
        }
        if (Actor.isValid(this.turboexhaust)) {
            this.turboexhaust.destroy();
        }
        super.destroy();
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if ((this.getBulletEmitterByHookName("_ExternalBomb01") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalDev03") instanceof GunEmpty) && (this.getBulletEmitterByHookName("_ExternalDev05") instanceof GunEmpty)) {
            this.hierMesh().chunkVisible("Pylon_D0", false);
        }
        if (this.getBulletEmitterByHookName("_CANNON07") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("MK103", false);
        } else {
            this.hierMesh().chunkVisible("MK103", true);
        }
        if (this.getBulletEmitterByHookName("_CANNON09") instanceof GunEmpty) {
            this.hierMesh().chunkVisible("MK108", false);
        } else {
            this.hierMesh().chunkVisible("MK108", true);
        }
        if (Config.isUSE_RENDER()) {
            this.flame = Eff3DActor.New(this, this.findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109F.eff", -1F);
            this.dust = Eff3DActor.New(this, this.findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109D.eff", -1F);
            this.trail = Eff3DActor.New(this, this.findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109T.eff", -1F);
            this.sprite = Eff3DActor.New(this, this.findHook("_Engine3EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboHWK109S.eff", -1F);
            this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
            Eff3DActor.setIntesity(this.flame, 0.0F);
            Eff3DActor.setIntesity(this.dust, 0.0F);
            Eff3DActor.setIntesity(this.trail, 0.0F);
            Eff3DActor.setIntesity(this.sprite, 0.0F);
            Eff3DActor.setIntesity(this.turboexhaust, 1.0F);
        }
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (flag && this.bCockpitNVentilated) {
            this.FM.AS.hitPilot(this, 0, 1);
        }
        if (Config.isUSE_RENDER()) {
            if ((this.oldVwld < 20F) && (this.FM.getSpeed() > 20F)) {
                Eff3DActor.finish(this.turboexhaust);
                this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallTSPD.eff", -1F);
            }
            if ((this.oldVwld > 20F) && (this.FM.getSpeed() < 20F)) {
                Eff3DActor.finish(this.turboexhaust);
                this.turboexhaust = Eff3DActor.New(this, this.findHook("_Engine3ES_02"), null, 1.0F, "3DO/Effects/Aircraft/WhiteOxySmallGND.eff", -1F);
            }
            this.oldVwld = this.FM.getSpeed();
        }
    }

    public void update(float f) {
        super.update(f);
        if (this.FM.AS.isMaster()) {
            if (this.prevThtl < 0.25F) {
                this.FM.EI.engines[2].setControlThrottle(0.1176471F);
            } else if (this.prevThtl < 0.5F) {
                this.FM.EI.engines[2].setControlThrottle(0.4117647F);
            } else if (this.prevThtl < 0.75F) {
                this.FM.EI.engines[2].setControlThrottle(0.7058824F);
            } else {
                this.FM.EI.engines[2].setControlThrottle(1.0F);
            }
            if (this.prevThtl != this.FM.CT.getPowerControl(2)) {
                this.prevThtl = this.FM.CT.getPowerControl(2);
            }
            if ((this.FM.EI.engines[2].getStage() > 0) && (this.FM.EI.engines[2].getStage() < 7)) {
                if (!this.FM.M.requestNitro((1774F * this.FM.EI.engines[2].getControlThrottle() * f) / 180F) && (World.cur().diffCur.Limited_Fuel || !this.FM.isPlayers())) {
                    this.FM.EI.engines[2].setControlThrottle(0.0F);
                    this.FM.EI.engines[2].setEngineStops(this);
                    this.FM.EI.engines[2].setStage(this, 0);
                }
                if (Config.isUSE_RENDER()) {
                    if (this.FM.EI.engines[2].getControlThrottle() > 0.0F) {
                        this.doSetSootState(2, 8);
                    } else {
                        this.doSetSootState(2, 7);
                    }
                }
            }
        }
    }

    public void doSetSootState(int i, int j) {
        for (int k = 0; k < 2; k++) {
            if (this.FM.AS.astateSootEffects[i][k] != null) {
                Eff3DActor.finish(this.FM.AS.astateSootEffects[i][k]);
            }
            this.FM.AS.astateSootEffects[i][k] = null;
        }

        try {
            switch (j) {
                case 1:
                    this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                    this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/BlackSmallTSPD.eff", -1F);
                    break;

                case 3:
                    this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                    // fall through

                case 2:
                    this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboZippo.eff", -1F);
                    break;

                case 5:
                    this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 3F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
                    // fall through

                case 4:
                    this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "EF_01"), null, 1.0F, "3DO/Effects/Aircraft/BlackMediumTSPD.eff", -1F);
                    break;

                case 6:
                    this.FM.AS.astateSootEffects[i][0] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_01"), null, 1.0F, "3DO/Effects/Aircraft/Full_throttle.eff", -1F);
                    this.FM.AS.astateSootEffects[i][1] = Eff3DActor.New(this, this.findHook("_Engine" + (i + 1) + "ES_02"), null, 1.0F, "3DO/Effects/Aircraft/Full_throttle.eff", -1F);
                    break;

                case 7:
                    Eff3DActor.setIntesity(this.flame, 0.0F);
                    Eff3DActor.setIntesity(this.dust, 0.0F);
                    Eff3DActor.setIntesity(this.trail, 0.0F);
                    Eff3DActor.setIntesity(this.sprite, 0.0F);
                    break;

                case 8:
                    Eff3DActor.setIntesity(this.flame, 1.0F);
                    Eff3DActor.setIntesity(this.dust, 1.0F);
                    Eff3DActor.setIntesity(this.trail, 1.0F);
                    Eff3DActor.setIntesity(this.sprite, 1.0F);
                    break;
            }
        } catch (Exception exception) {
        }
    }

    private Eff3DActor flame;
    private Eff3DActor dust;
    private Eff3DActor trail;
    private Eff3DActor sprite;
    private Eff3DActor turboexhaust;
    private float      prevThtl;
    private float      oldVwld;
    private boolean    bCockpitNVentilated;
    static {
        Class class1 = ME_262C1A.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Me 262");
        Property.set(class1, "meshName", "3DO/Plane/Me-262C-1a/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1945.5F);
        Property.set(class1, "FlightModel", "FlightModels/Me-262C-1a.fmd:Me-262C");
        Property.set(class1, "cockpitClass", new Class[] { CockpitME_262.class });
        Property.set(class1, "LOSElevation", 0.74615F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 1, 1, 9, 9, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 9, 9, 3, 3, 0, 0, 1, 1, 1, 1, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev01", "_ExternalDev02", "_ExternalRock01", "_ExternalRock02", "_ExternalRock03", "_ExternalRock04", "_ExternalRock05", "_ExternalRock06", "_ExternalRock07", "_ExternalRock08", "_ExternalRock09", "_ExternalRock10", "_ExternalRock11", "_ExternalRock12", "_ExternalRock13", "_ExternalRock14", "_ExternalRock15", "_ExternalRock16", "_ExternalRock17", "_ExternalRock18", "_ExternalRock19", "_ExternalRock20", "_ExternalRock21", "_ExternalRock22", "_ExternalRock23", "_ExternalRock24", "_ExternalRock25", "_ExternalRock26", "_ExternalDev03", "_ExternalDev04", "_ExternalBomb01", "_ExternalBomb02", "_CANNON05", "_CANNON06", "_CANNON07", "_CANNON08", "_CANNON09", "_CANNON10", "_ExternalDev05", "_ExternalDev06" });
    }
}
