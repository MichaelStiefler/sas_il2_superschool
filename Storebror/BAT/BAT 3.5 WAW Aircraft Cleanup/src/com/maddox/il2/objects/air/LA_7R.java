package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class LA_7R extends LA_X {

    public LA_7R() {
        this.bHasEngine = true;
        this.flame = null;
        this.dust = null;
        this.trail = null;
        this.sprite = null;
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
        super.destroy();
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        if (Config.isUSE_RENDER()) {
            this.flame = Eff3DActor.New(this, this.findHook("_Engine2EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100F.eff", -1F);
            this.dust = Eff3DActor.New(this, this.findHook("_Engine2EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100D.eff", -1F);
            this.trail = Eff3DActor.New(this, this.findHook("_Engine2EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100T.eff", -1F);
            this.sprite = Eff3DActor.New(this, this.findHook("_Engine2EF_01"), null, 1.0F, "3DO/Effects/Aircraft/TurboJRD1100S.eff", -1F);
            Eff3DActor.setIntesity(this.flame, 0.0F);
            Eff3DActor.setIntesity(this.dust, 0.0F);
            Eff3DActor.setIntesity(this.trail, 0.0F);
            Eff3DActor.setIntesity(this.sprite, 0.0F);
        }
    }

    protected void moveFan(float f) {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        super.moveFan(f);
        if (this.isNetMirror()) {
            if (this.FM.EI.engines[1].getStage() == 6) {
                Eff3DActor.setIntesity(this.flame, 1.0F);
                Eff3DActor.setIntesity(this.dust, 1.0F);
                Eff3DActor.setIntesity(this.trail, 1.0F);
                Eff3DActor.setIntesity(this.sprite, 1.0F);
            } else {
                Eff3DActor.setIntesity(this.flame, 0.0F);
                Eff3DActor.setIntesity(this.dust, 0.0F);
                Eff3DActor.setIntesity(this.trail, 0.0F);
                Eff3DActor.setIntesity(this.sprite, 0.0F);
            }
        } else if (this.bHasEngine && (this.FM.EI.engines[1].getControlThrottle() > 0.0F) && (this.FM.EI.engines[1].getStage() == 6)) {
            Eff3DActor.setIntesity(this.flame, 1.0F);
            Eff3DActor.setIntesity(this.dust, 1.0F);
            Eff3DActor.setIntesity(this.trail, 1.0F);
            Eff3DActor.setIntesity(this.sprite, 1.0F);
        } else {
            Eff3DActor.setIntesity(this.flame, 0.0F);
            Eff3DActor.setIntesity(this.dust, 0.0F);
            Eff3DActor.setIntesity(this.trail, 0.0F);
            Eff3DActor.setIntesity(this.sprite, 0.0F);
        }
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 19:
                this.bHasEngine = false;
                this.FM.AS.setEngineDies(this, 1);
                return this.cut(Aircraft.partNames()[i]);

            case 3:
            case 4:
                return false;
        }
        return super.cutFM(i, j, actor);
    }

    public void update(float f) {
        super.update(f);
        if (this.isNetMirror()) {
            return;
        }
        this.bPowR = this == World.getPlayerAircraft();
        if (((this.FM.getAltitude() - Engine.land().HQ(this.FM.Loc.x, this.FM.Loc.y)) > 5D) && (this.FM.M.fuel > 0.0F)) {
            if ((this.FM.EI.engines[1].getControlThrottle() > (this.bPowR ? 0.4120879F : 0.77F)) && ((this.FM.EI.engines[1].getStage() == 0) && (this.FM.M.nitro > 0.0F))) {
                this.FM.EI.engines[1].setStage(this, 6);
                if (this.bPowR) {
                    HUD.log("EngineI" + (this.FM.EI.engines[1].getStage() != 6 ? 48 : '1'));
                }
            }
            if ((this.FM.EI.engines[1].getControlThrottle() < (this.bPowR ? 0.4120879F : 0.77F)) && (this.FM.EI.engines[1].getStage() > 0)) {
                this.FM.EI.engines[1].setEngineStops(this);
                if (this.bPowR) {
                    HUD.log("EngineI" + (this.FM.EI.engines[1].getStage() != 6 ? 48 : '1'));
                }
            }
        }
    }

    private boolean    bHasEngine;
    private Eff3DActor flame;
    private Eff3DActor dust;
    private Eff3DActor trail;
    private Eff3DActor sprite;
    private boolean    bPowR;

    static {
        Class class1 = LA_7R.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "La");
        Property.set(class1, "meshName", "3DO/Plane/La-7R(Multi1)/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
        Property.set(class1, "yearService", 1944F);
        Property.set(class1, "yearExpired", 1948.5F);
        Property.set(class1, "FlightModel", "FlightModels/La-7R.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitLA_7R.class });
        Property.set(class1, "LOSElevation", 0.92F);
        Aircraft.weaponTriggersRegister(class1, new int[] { 1, 1, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_CANNON01", "_CANNON02", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb01", "_ExternalBomb02" });
    }
}
