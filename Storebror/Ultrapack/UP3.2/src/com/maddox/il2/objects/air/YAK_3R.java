package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.HUD;
import com.maddox.rts.Property;

public class YAK_3R extends YAK {

	public YAK_3R() {
		this.bHasEngine = true;
		this.flame = null;
		this.dust = null;
		this.trail = null;
		this.sprite = null;
	}

	public void destroy() {
		if (Actor.isValid(this.flame)) { this.flame.destroy(); }
		if (Actor.isValid(this.dust)) { this.dust.destroy(); }
		if (Actor.isValid(this.trail)) { this.trail.destroy(); }
		if (Actor.isValid(this.sprite)) { this.sprite.destroy(); }
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
		if (!Config.isUSE_RENDER()) { return; }
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
		} else if (this.bHasEngine && this.FM.EI.engines[1].getControlThrottle() > 0.0F && this.FM.EI.engines[1].getStage() == 6) {
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
				return this.cut(partNames()[i]);

			case 3:
			case 4:
				return false;
		}
		return super.cutFM(i, j, actor);
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		float f1 = Math.max(-f * 1500F, -80F);
		hiermesh.chunkSetAngles("GearC3_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearC99_D0", 0.0F, 80F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 0.0F, 0.0F);
		f1 = Math.max(-f * 1500F, -60F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, f1, 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 82.5F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL3_D0", 0.0F, -85F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR3_D0", 0.0F, -85F * f, 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public void update(float f) {
		super.update(f);
		if (this.isNetMirror()) { return; }
		this.bPowR = this == World.getPlayerAircraft();
		if (this.FM.getAltitude() - Engine.land().HQ(this.FM.Loc.x, this.FM.Loc.y) > 5D && this.FM.M.fuel > 0.0F) {
			if (this.FM.EI.engines[1].getControlThrottle() > (this.bPowR ? powR : powA) && this.FM.EI.engines[1].getStage() == 0 && this.FM.M.nitro > 0.0F) {
				this.FM.EI.engines[1].setStage(this, 6);
				if (this.bPowR) { HUD.log("EngineI" + (this.FM.EI.engines[1].getStage() != 6 ? 48 : '1')); }
			}
			if (this.FM.EI.engines[1].getControlThrottle() < (this.bPowR ? powR : powA) && this.FM.EI.engines[1].getStage() > 0) {
				this.FM.EI.engines[1].setEngineStops(this);
				if (this.bPowR) { HUD.log("EngineI" + (this.FM.EI.engines[1].getStage() != 6 ? 48 : '1')); }
			}
		}
		this.hierMesh().chunkSetAngles("OilRad_D0", 0.0F, this.FM.EI.engines[0].getControlRadiator() * 25F, 0.0F);
		this.hierMesh().chunkSetAngles("Water_luk", 0.0F, this.FM.EI.engines[0].getControlRadiator() * 12F, 0.0F);
	}

	private static final float powR = 0.4120879F;
	private static final float powA = 0.77F;
	private boolean            bHasEngine;
	private Eff3DActor         flame;
	private Eff3DActor         dust;
	private Eff3DActor         trail;
	private Eff3DActor         sprite;
	private boolean            bPowR;

	static {
		Class class1 = YAK_3R.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Yak");
		Property.set(class1, "meshName", "3DO/Plane/Yak-3R(Multi1)/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar05());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1948.5F);
		Property.set(class1, "FlightModel", "FlightModels/Yak-3R.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitYAK_3R.class });
		Property.set(class1, "LOSElevation", 0.6576F);
		weaponTriggersRegister(class1, new int[] { 1 });
		weaponHooksRegister(class1, new String[] { "_CANNON01" });
	}
}
