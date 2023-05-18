package com.maddox.il2.objects.air;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.fm.Atmosphere;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.CLASS;
import com.maddox.rts.Property;

public class CockpitSB2M100A_NGunner extends CockpitGunner {

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
		this.mesh.chunkSetAngles("Z_arrow_hour", -this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_arrow_min", -this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F,
				0.0F);
		this.mesh.chunkSetAngles("Z_arrow_sec_1",
				-this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_arrow_temp",
				this.cvt(Atmosphere.temperature((float) this.fm.Loc.z) - 273.15F, -70F, 70F, 52F, -52F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_arrow_speed",
				this.floatindex(
						this.cvt(Pitot.Indicator((float) this.fm.Loc.z, this.fm.getSpeedKMH()), 0.0F, 600F, 0.0F, 15F),
						speedometerScale),
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_arrow_alt2", -this.cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 3600F), 0.0F,
				0.0F);
		this.mesh.chunkSetAngles("Z_arrow_alt1", -this.cvt(this.fm.getAltitude(), 0.0F, 10000F, 0.0F, 360F), 0.0F,
				0.0F);
		if (this.bGunFire && (this.patronMat != null)) {
			this.patron -= 0.5F * f;
			this.patronMat.setLayer(0);
			this.patronMat.set((byte) 11, this.patron);
		}
		if (this.emitter != null) {
			if (this.emitter.countBullets() < 44) {
				this.mesh.chunkVisible("ammoL1", false);
				this.mesh.chunkVisible("ammoR1", false);
			}
			if (this.emitter.countBullets() < 40) {
				this.mesh.chunkVisible("ammoL2", false);
				this.mesh.chunkVisible("ammoR2", false);
			}
			if (this.emitter.countBullets() < 34) {
				this.mesh.chunkVisible("ammoL3", false);
				this.mesh.chunkVisible("ammoR3", false);
			}
			if (this.emitter.countBullets() < 30) {
				this.mesh.chunkVisible("ammoL4", false);
				this.mesh.chunkVisible("ammoR4", false);
			}
			if (this.emitter.countBullets() < 25) {
				this.mesh.chunkVisible("ammoL5", false);
				this.mesh.chunkVisible("ammoR5", false);
			}
			if (this.emitter.countBullets() < 21) {
				this.mesh.chunkVisible("ammoL6", false);
				this.mesh.chunkVisible("ammoR6", false);
			}
			if (this.emitter.countBullets() < 16) {
				this.mesh.chunkVisible("ammoL7", false);
				this.mesh.chunkVisible("ammoR7", false);
			}
			if (this.emitter.countBullets() < 12) {
				this.mesh.chunkVisible("ammoL8", false);
				this.mesh.chunkVisible("ammoR8", false);
			}
			if (this.emitter.countBullets() < 8) {
				this.mesh.chunkVisible("ammoL9", false);
				this.mesh.chunkVisible("ammoR9", false);
			}
			if (this.emitter.countBullets() < 1) {
				this.mesh.chunkVisible("ammoL10", false);
				this.mesh.chunkVisible("ammoR10", false);
			}
		}
	}

	protected void reflectPlaneMats() {
		final HierMesh hiermesh = this.aircraft().hierMesh();
		final Mat mat = hiermesh.material(hiermesh.materialFind("Gloss1D0o"));
		this.mesh.materialReplace("Gloss1D0o", mat);
	}

	public void moveGun(Orient orient) {
		this.gunOrient.set(orient.getTangage(), orient.getYaw(), 0F);
		super.moveGun(this.gunOrient);
		final float f = -orient.getYaw();
		final float f1 = orient.getTangage();
		this.mesh.chunkSetAngles("Z_TUR_1", f1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("CameraTan", f1, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_SHKAS_R", f, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_SHKAS_L", f, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("CameraYaw", f, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TUR_3", -f, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TUR_center", -f, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("CameraTan", 0.0F, 0.0F, f1);
		this.resetYPRmodifier();
		Cockpit.xyz[0] = f * 0.0003F;
		this.mesh.chunkSetLocate("Z_TUR_bag", Cockpit.xyz, Cockpit.ypr);
		if (f1 > 0.0F) {
			this.mesh.chunkSetAngles("Extract4", 0.0F, f * 0.4F, f1 * 0.3F);
			this.resetYPRmodifier();
			Cockpit.xyz[2] = f1 * 0.00102F;
			this.mesh.chunkSetLocate("Extract3", Cockpit.xyz, Cockpit.ypr);
			this.mesh.chunkSetLocate("Extract2", Cockpit.xyz, Cockpit.ypr);
			this.mesh.chunkSetLocate("Extract1", Cockpit.xyz, Cockpit.ypr);
		} else {
			this.mesh.chunkSetAngles("Extract4", 0.0F, f * 0.4F, f1 * -0.02F);
			this.resetYPRmodifier();
			Cockpit.xyz[2] = f1 * 0.0012F;
			this.mesh.chunkSetLocate("Extract3", Cockpit.xyz, Cockpit.ypr);
			this.mesh.chunkSetLocate("Extract2", Cockpit.xyz, Cockpit.ypr);
			this.mesh.chunkSetLocate("Extract1", Cockpit.xyz, Cockpit.ypr);
		}
		final float f2 = f1 * 0.12F;
		this.mesh.chunkSetAngles("BeltL1", -f2, 0.0F, -f * 0.36F);
		this.mesh.chunkSetAngles("BeltL2", -f2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltL3", -f2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltL4", -f2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltL5", -f2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR1", f2, 0.0F, f * 0.36F);
		this.mesh.chunkSetAngles("BeltR2", f2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR3", f2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR4", f2, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR5", f2, 0.0F, 0.0F);
		final float f3 = f * 0.32F;
		final float f4 = 5F + (f1 * 0.038F);
		this.mesh.chunkSetAngles("BeltL6", (f3 * 0.5F) + f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltL7", f3 + f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltL8", f3 + f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltL9", -f3 - f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltL10", (-f3 * 1.5F) - f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR6", (f3 * 0.5F) - f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR7", f3 - f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR8", f3 - f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR9", -f3 + f4, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("BeltR10", -(f3 * 1.5F) + f4, 0.0F, 0.0F);
	}

	public void clipAnglesGun(Orient orient) {
		if (!this.isRealMode()) {
			return;
		}
		if (!this.aiTurret().bIsOperable) {
			orient.setYPR(0.0F, 0.0F, 0.0F);
			return;
		}
		float f = orient.getYaw();
		float f1 = orient.getTangage();
		if (f < -20F) {
			f = -20F;
		}
		if (f > 20F) {
			f = 20F;
		}
		if (f1 < -50F) {
			f1 = -50F;
		}
		if (f1 > 45F) {
			f1 = 45F;
		}
		orient.setYPR(f, f1, 0.0F);
		orient.wrap();
	}

	protected void interpTick() {
		if (!this.isRealMode()) {
			return;
		}
		if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
			this.bGunFire = false;
		}
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
		if (this.bGunFire) {
			if (this.hook1 == null) {
				this.hook1 = new HookNamed(this.aircraft(), "_MGUN01");
			}
			this.doHitMasterAircraft(this.aircraft(), this.hook1, "_MGUN01");
			if (this.hook2 == null) {
				this.hook2 = new HookNamed(this.aircraft(), "_MGUN02");
			}
			this.doHitMasterAircraft(this.aircraft(), this.hook2, "_MGUN02");
		}
	}

	public void doGunFire(boolean flag) {
		if (!this.isRealMode()) {
			return;
		}
		if ((this.emitter == null) || !this.emitter.haveBullets() || !this.aiTurret().bIsOperable) {
			this.bGunFire = false;
		} else {
			this.bGunFire = flag;
		}
		this.fm.CT.WeaponControl[this.weaponControlNum()] = this.bGunFire;
	}

	public CockpitSB2M100A_NGunner() {
		super("3DO/Cockpit/SB_100-NGunner/hier.him", "he111_gunner");
		this.patron = 1.0F;
		this.patronMat = null;
		this.bNeedSetUp = true;
		this.hook1 = null;
		this.hook2 = null;
		this.gunOrient = new Orient();
		this.cockpitNightMats = (new String[] { "ACHO_arrow", "Dprib_one", "Dprib_six", "equip_AN4_sh", "prib_one",
				"Prib_six" });
		this.setNightMats(false);
		HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK03");
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		this.light3 = new LightPointActor(new LightPoint(), loc.getPoint());
		this.light3.light.setColor(126F, 232F, 245F);
		this.light3.light.setEmit(0.0F, 0.0F);
		this.pos.base().draw.lightMap().put("LAMPHOOK03", this.light3);
		hooknamed = new HookNamed(this.mesh, "LAMPHOOK04");
		loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		this.light4 = new LightPointActor(new LightPoint(), loc.getPoint());
		this.light4.light.setColor(126F, 232F, 245F);
		this.light4.light.setEmit(0.0F, 0.0F);
		this.pos.base().draw.lightMap().put("LAMPHOOK04", this.light4);
		int i = -1;
		i = this.mesh.materialFind("patron");
		if (i != -1) {
			this.patronMat = this.mesh.material(i);
			this.patronMat.setLayer(0);
		}
		if (this.emitter == null) {
			for (int j = 1; j <= 10; j++) {
				this.mesh.chunkVisible("ammoL" + j, false);
				this.mesh.chunkVisible("ammoR" + j, false);
			}

		}
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) {
			this.light3.light.setEmit(0.003F, 0.6F);
			this.light4.light.setEmit(0.003F, 0.6F);
			this.setNightMats(true);
		} else {
			this.light3.light.setEmit(0.0F, 0.0F);
			this.light4.light.setEmit(0.0F, 0.0F);
			this.setNightMats(false);
		}
	}

	private final LightPointActor light3;
	private final LightPointActor light4;
	private float patron;
	private Mat patronMat;
	private boolean bNeedSetUp;
	private static final float speedometerScale[] = { 0.0F, -10F, -19.5F, -32F, -46F, -66.5F, -89F, -114F, -141F,
			-170.5F, -200.5F, -232.5F, -264F, -295.5F, -328F, -360F };
	private Hook hook1;
	private Hook hook2;
	private final Orient gunOrient;

	static {
		Property.set(CockpitSB2M100A_NGunner.class, "aiTuretNum", 0);
		Property.set(CockpitSB2M100A_NGunner.class, "weaponControlNum", 10);
		Property.set(CockpitSB2M100A_NGunner.class, "astatePilotIndx", 1);
		Property.set(CLASS.THIS(), "normZN", 1.1F);
		Property.set(CLASS.THIS(), "gsZN", 1.1F);
	}
}
