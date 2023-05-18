package com.maddox.il2.objects.air;

import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public abstract class IAR_8X extends Scheme1 {

    public IAR_8X() {
    }

    public void rareAction(float f, boolean flag) {
        super.rareAction(f, flag);
        if (this.FM.getAltitude() < 3000F) this.hierMesh().chunkVisible("HMask1_D0", false);
        else this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Head1_D0"));
    }

    public void doMurderPilot(int i) {
        switch (i) {
            case 0:
                this.hierMesh().chunkVisible("Pilot1_D0", false);
                this.hierMesh().chunkVisible("Head1_D0", false);
                this.hierMesh().chunkVisible("HMask1_D0", false);
                this.hierMesh().chunkVisible("Pilot1_D1", true);
                if (!this.FM.AS.bIsAboutToBailout) {
                    if (this.hierMesh().isChunkVisible("Blister1_D0")) this.hierMesh().chunkVisible("Gore1_D0", true);
                    this.hierMesh().chunkVisible("Gore2_D0", true);
                }
                break;
        }
    }

    public static void moveGear(HierMesh hiermesh, float f) {
        hiermesh.chunkSetAngles("GearL2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR2_D0", 0.0F, -90F * f, 0.0F);
        hiermesh.chunkSetAngles("GearL4_D0", 0.0F, 115F * f, 0.0F);
        hiermesh.chunkSetAngles("GearR4_D0", 0.0F, 115F * f, 0.0F);
    }

    protected void moveGear(float f) {
        moveGear(this.hierMesh(), f);
    }

    public void msgShot(Shot shot) {
        this.setShot(shot);
        if (shot.chunkName.startsWith("CF")) {
            if (World.Rnd().nextFloat(0.0F, 1.0F) < 0.1F) this.FM.AS.hitTank(shot.initiator, 0, 1);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 2);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
            if (World.Rnd().nextFloat() < 0.07F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
        }
        if (shot.chunkName.startsWith("Engine1") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.2F) this.FM.AS.hitEngine(shot.initiator, 0, 1);
        if (shot.chunkName.startsWith("Oil") && World.Rnd().nextFloat(0.0F, 1.0F) < 0.9F) {
            this.FM.AS.hitOil(shot.initiator, 0);
            if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.hitEngine(shot.initiator, 0, 1);
        }
        if (shot.chunkName.startsWith("Pilot")) {
            this.killPilot(shot.initiator, 0);
            if (Pd.z > 0.65D && shot.initiator == World.getPlayerAircraft() && World.cur().isArcade()) HUD.logCenter("H E A D S H O T");
            return;
        } else {
            super.msgShot(shot);
            return;
        }
    }

    protected void moveFlap(float f) {
        float f1 = -45F * f;
        this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
        this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
    }

    protected boolean cutFM(int i, int j, Actor actor) {
        switch (i) {
            case 33:
                return super.cutFM(35, j, actor);

            case 36:
                return super.cutFM(38, j, actor);
        }
        return super.cutFM(i, j, actor);
    }

    static {
        Class class1 = IAR_8X.class;
        Property.set(class1, "originCountry", PaintScheme.countryRomania);
    }
}
