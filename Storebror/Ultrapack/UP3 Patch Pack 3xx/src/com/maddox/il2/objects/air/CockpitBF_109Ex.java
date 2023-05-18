/*
 * Bf 109 Cockpit Bugfix by SAS~Storebror
 * 
 * In original UP3 RC4, when you perform the following steps:
 * 1.) Open up canopy in flight at high speed
 * 2.) Lose canopy
 * 3.) bailout
 * You will first lose sight (black screen) and your game will
 * lock up on impact with lots of nullpointer exceptions in
 * the log.
 */

package com.maddox.il2.objects.air;

import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Tuple3f;
import com.maddox.JGP.Vector3f;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.InterpolateRef;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.fm.Pitot;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class CockpitBF_109Ex extends CockpitPilot {
	class Interpolater extends InterpolateRef {

		public boolean tick() {
			if (CockpitBF_109Ex.this.bNeedSetUp) {
				CockpitBF_109Ex.this.reflectPlaneMats();
				CockpitBF_109Ex.this.bNeedSetUp = false;
			}
			CockpitBF_109Ex.this.setTmp = CockpitBF_109Ex.this.setOld;
			CockpitBF_109Ex.this.setOld = CockpitBF_109Ex.this.setNew;
			CockpitBF_109Ex.this.setNew = CockpitBF_109Ex.this.setTmp;
			CockpitBF_109Ex.this.setNew.altimeter = CockpitBF_109Ex.this.fm.getAltitude();
			if (CockpitBF_109Ex.this.cockpitDimControl) {
				if (CockpitBF_109Ex.this.setNew.dimPosition > 0.0F)
					CockpitBF_109Ex.this.setNew.dimPosition = CockpitBF_109Ex.this.setOld.dimPosition - 0.05F;
			} else if (CockpitBF_109Ex.this.setNew.dimPosition < 1.0F)
				CockpitBF_109Ex.this.setNew.dimPosition = CockpitBF_109Ex.this.setOld.dimPosition + 0.05F;
			CockpitBF_109Ex.this.setNew.throttle = (10F * CockpitBF_109Ex.this.setOld.throttle
					+ CockpitBF_109Ex.this.fm.CT.PowerControl) / 11F;
			CockpitBF_109Ex.this.setNew.mix = (8F * CockpitBF_109Ex.this.setOld.mix
					+ CockpitBF_109Ex.this.fm.EI.engines[0].getControlMix()) / 9F;
			float f = CockpitBF_109Ex.this.waypointAzimuth();
			if (CockpitBF_109Ex.this.useRealisticNavigationInstruments()) {
				CockpitBF_109Ex.this.setNew.waypointAzimuth.setDeg(f - 90F);
				CockpitBF_109Ex.this.setOld.waypointAzimuth.setDeg(f - 90F);
			} else {
				CockpitBF_109Ex.this.setNew.waypointAzimuth.setDeg(
						CockpitBF_109Ex.this.setOld.waypointAzimuth.getDeg(0.1F),
						f - CockpitBF_109Ex.this.setOld.azimuth.getDeg(1.0F));
			}
			CockpitBF_109Ex.this.setNew.azimuth.setDeg(CockpitBF_109Ex.this.setOld.azimuth.getDeg(1.0F),
					CockpitBF_109Ex.this.fm.Or.azimut());
			CockpitBF_109Ex.this.w.set(CockpitBF_109Ex.this.fm.getW());
			CockpitBF_109Ex.this.fm.Or.transform(CockpitBF_109Ex.this.w);
			CockpitBF_109Ex.this.setNew.turn = (12F * CockpitBF_109Ex.this.setOld.turn
					+ ((Tuple3f) (CockpitBF_109Ex.this.w)).z) / 13F;
			CockpitBF_109Ex.this.buzzerFX(
					CockpitBF_109Ex.this.fm.CT.getGear() < 0.999999F && CockpitBF_109Ex.this.fm.CT.getFlap() > 0.0F);
			return true;
		}

		Interpolater() {
		}
	}

	private class Variables {

		float altimeter;
		float throttle;
		float dimPosition;
		AnglesFork azimuth;
		AnglesFork waypointAzimuth;
		float turn;
		float mix;

		private Variables() {
			this.azimuth = new AnglesFork();
			this.waypointAzimuth = new AnglesFork();
		}

		Variables(Variables variables) {
			this();
		}
	}

	protected float waypointAzimuth() {
		return this.waypointAzimuthInvertMinus(30F);
	}

	public CockpitBF_109Ex() {
		super("3DO/Cockpit/Bf-109E-7/hier.him", "bf109");
		this.hasCanopy = true;
		this.setOld = new Variables(null);
		this.setNew = new Variables(null);
		this.pictAiler = 0.0F;
		this.pictElev = 0.0F;
		this.pictManifold = 0.0F;
		this.bNeedSetUp = true;
		this.w = new Vector3f();
		this.setNew.dimPosition = 1.0F;
		HookNamed hooknamed = new HookNamed(this.mesh, "LAMPHOOK1");
		Loc loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		this.light1 = new LightPointActor(new LightPoint(), loc.getPoint());
		this.light1.light.setColor(227F, 65F, 33F);
		this.light1.light.setEmit(0.0F, 0.0F);
		this.pos.base().draw.lightMap().put("LAMPHOOK1", this.light1);
		hooknamed = new HookNamed(this.mesh, "LAMPHOOK2");
		loc = new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
		hooknamed.computePos(this, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F), loc);
		this.light2 = new LightPointActor(new LightPoint(), loc.getPoint());
		this.light2.light.setColor(227F, 65F, 33F);
		this.light2.light.setEmit(0.0F, 0.0F);
		this.pos.base().draw.lightMap().put("LAMPHOOK2", this.light2);
		this.cockpitNightMats = (new String[] { "ZClocks1", "ZClocks1DMG", "ZClocks2", "ZClocks3", "FW190A4Compass",
				"oxigen" });
		this.setNightMats(false);
		this.interpPut(new Interpolater(), null, Time.current(), null);
		this.loadBuzzerFX();
		AircraftLH.printCompassHeading = true;
	}

	public void removeCanopy() {
		this.hasCanopy = false;
		if (this.mesh.chunkFindCheck("Top") >= 0)
			this.mesh.chunkVisible("TopOpen", false);
		this.mesh.chunkVisible("TopE1", false);
		this.mesh.chunkVisible("Z_Holes1_D1", false);
		this.mesh.chunkVisible("Z_Holes2_D1", false);
		if (this.mesh.chunkFindCheck("Top2") >= 0)
			this.mesh.chunkVisible("Top2", false);
	}

	public void reflectWorldToInstruments(float f) {
		if (this.bNeedSetUp) {
			this.reflectPlaneMats();
			this.bNeedSetUp = false;
		}
		this.mesh.chunkSetAngles("Top", 0.0F, 100F * this.fm.CT.getCockpitDoor(), 0.0F);
		this.mesh.chunkSetAngles("Z_Altimeter1",
				this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 3600F), 0.0F,
				0.0F);
		this.mesh.chunkSetAngles("Z_Altimeter2",
				this.cvt(this.interp(this.setNew.altimeter, this.setOld.altimeter, f), 0.0F, 10000F, 0.0F, 180F), 0.0F,
				0.0F);
		this.mesh.chunkSetAngles("Z_ReviTinter",
				this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, -30F),
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_ReviTint",
				this.cvt(this.interp(this.setNew.dimPosition, this.setOld.dimPosition, f), 0.0F, 1.0F, 0.0F, 40F), 0.0F,
				0.0F);
		this.mesh
				.chunkSetAngles("Z_ATA1",
						15.5F + this.cvt(this.pictManifold = 0.75F * this.pictManifold
								+ 0.25F * this.fm.EI.engines[0].getManifoldPressure(), 0.6F, 1.8F, 0.0F, 336F),
						0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Speedometer1",
				this.floatindex(this.cvt(Pitot.Indicator((float) ((Tuple3d) (this.fm.Loc)).z, this.fm.getSpeedKMH()),
						0.0F, 800F, 0.0F, 16F), speedometerScale),
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_RPM1",
				this.floatindex(this.cvt(this.fm.EI.engines[0].getRPM(), 0.0F, 4000F, 0.0F, 8F), rpmScale), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_FuelQuantity1",
				-44.5F + this.floatindex(this.cvt(this.fm.M.fuel / 0.72F, 0.0F, 400F, 0.0F, 8F), fuelScale), 0.0F,
				0.0F);
		this.mesh.chunkSetAngles("Z_Iengtemprad1", this.cvt(this.fm.EI.engines[0].tOilOut, 0.0F, 160F, 0.0F, 58.5F),
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_EngTemp1", this.cvt(this.fm.EI.engines[0].tWaterOut, 0.0F, 120F, 0.0F, 58.5F), 0.0F,
				0.0F);
		this.mesh.chunkSetAngles("Z_FuelPress1", this.cvt(this.fm.M.fuel > 1.0F ? 0.26F : 0.0F, 0.0F, 3F, 0.0F, 135F),
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_OilPress1",
				this.cvt(1.0F + 0.05F * this.fm.EI.engines[0].tOilOut, 0.0F, 15F, 0.0F, 135F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurnBank1", this.cvt(this.setNew.turn, -0.23562F, 0.23562F, 30F, -30F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_TurnBank2", this.cvt(this.getBall(6D), -6F, 6F, -7F, 7F), 0.0F, 0.0F);
		if (this.useRealisticNavigationInstruments()) {
			this.mesh.chunkSetAngles("Z_Azimuth1",
					this.setNew.azimuth.getDeg(f) - this.setNew.waypointAzimuth.getDeg(f), 0.0F, 0.0F);
			this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.waypointAzimuth.getDeg(f), 0.0F);
		} else {
			this.mesh.chunkSetAngles("Z_Compass1", 0.0F, -this.setNew.azimuth.getDeg(f), 0.0F);
			this.mesh.chunkSetAngles("Z_Azimuth1", this.setNew.waypointAzimuth.getDeg(f * 0.1F), 0.0F, 0.0F);
		}
		this.mesh.chunkSetAngles("Z_PropPitch1",
				270F - (float) Math
						.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 60F,
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_PropPitch2",
				105F - (float) Math
						.toDegrees(this.fm.EI.engines[0].getPropPhi() - this.fm.EI.engines[0].getPropPhiMin()) * 5F,
				0.0F, 0.0F);
		this.mesh.chunkVisible("Z_FuelRed1", this.fm.M.fuel < 36F);
		this.mesh.chunkVisible("Z_GearLRed1", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkVisible("Z_GearRRed1", this.fm.CT.getGear() == 0.0F);
		this.mesh.chunkVisible("Z_GearLGreen1", this.fm.CT.getGear() == 1.0F);
		this.mesh.chunkVisible("Z_GearRGreen1", this.fm.CT.getGear() == 1.0F);
		this.mesh.chunkSetAngles("Z_Hour1", this.cvt(World.getTimeofDay(), 0.0F, 24F, 0.0F, 720F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Minute1", this.cvt(World.getTimeofDay() % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F,
				0.0F);
		this.mesh.chunkSetAngles("Z_Second1",
				this.cvt(((World.getTimeofDay() % 1.0F) * 60F) % 1.0F, 0.0F, 1.0F, 0.0F, 360F), 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Column",
				(this.pictAiler = 0.85F * this.pictAiler + 0.15F * this.fm.CT.AileronControl) * 15F, 0.0F,
				(this.pictElev = 0.85F * this.pictElev + 0.15F * this.fm.CT.ElevatorControl) * 10F);
		this.mesh.chunkSetAngles("Z_PedalStrut", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_PedalStrut2", this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_LeftPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_RightPedal", -this.fm.CT.getRudder() * 15F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 57.72727F,
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle_D1", this.interp(this.setNew.throttle, this.setOld.throttle, f) * 27F,
				0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Throttle_tube",
				-this.interp(this.setNew.throttle, this.setOld.throttle, f) * 57.72727F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Mix", this.interp(this.setNew.mix, this.setOld.mix, f) * 70F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_Mix_tros", -this.interp(this.setNew.mix, this.setOld.mix, f) * 70F, 0.0F, 0.0F);
		this.mesh.chunkSetAngles("Z_MagnetoSwitch", -45F + 28.333F * this.fm.EI.engines[0].getControlMagnetos(), 0.0F,
				0.0F);
	}

	public void toggleDim() {
		this.cockpitDimControl = !this.cockpitDimControl;
	}

	public void toggleLight() {
		this.cockpitLightControl = !this.cockpitLightControl;
		if (this.cockpitLightControl) {
			this.light1.light.setEmit(0.005F, 0.5F);
			this.light2.light.setEmit(0.005F, 0.5F);
			this.setNightMats(true);
		} else {
			this.light1.light.setEmit(0.0F, 0.0F);
			this.light2.light.setEmit(0.0F, 0.0F);
			this.setNightMats(false);
		}
	}

	public void reflectCockpitState() {
		if ((this.fm.AS.astateCockpitState & 2) != 0) {
			this.mesh.chunkVisible("Z_Armor_D1", true);
			this.mesh.chunkVisible("Z_HullDamage1", true);
		}
		if ((this.fm.AS.astateCockpitState & 1) != 0)
			if (this.type == 0 || this.type == 1)
				this.mesh.chunkVisible("Z_Holes1E4_D1", true);
			else
				this.mesh.chunkVisible("Z_Holes1_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x40) != 0) {
			this.mesh.chunkVisible("Z_HullDamage1", true);
			this.mesh.chunkVisible("Z_HullDamage2", true);
		}
		if ((this.fm.AS.astateCockpitState & 4) != 0)
			this.mesh.chunkVisible("Z_HullDamage4", true);
		if ((this.fm.AS.astateCockpitState & 8) != 0) {
			this.mesh.chunkVisible("Z_Throttle", false);
			this.mesh.chunkVisible("Z_Throttle_tube", false);
			this.mesh.chunkVisible("Z_Throttle_D1", true);
			this.mesh.chunkVisible("Z_Throttle_TD1", true);
		}
		if ((this.fm.AS.astateCockpitState & 0x80) != 0)
			if (this.type == 0 || this.type == 1)
				this.mesh.chunkVisible("Z_OilSplatE4_D1", true);
			else
				this.mesh.chunkVisible("Z_OilSplats_D1", true);
		if ((this.fm.AS.astateCockpitState & 0x10) != 0)
			this.mesh.chunkVisible("Z_HullDamage3", true);
		if ((this.fm.AS.astateCockpitState & 0x20) != 0) {
			this.mesh.chunkVisible("Z_HullDamage2", true);
			if (this.type == 0 || this.type == 1)
				this.mesh.chunkVisible("Z_Holes2E4_D1", true);
			else
				this.mesh.chunkVisible("Z_Holes2_D1", true);
		}
	}

	protected void reflectPlaneMats() {
		if (Actor.isValid(this.fm.actor)) {
			if (this.fm.actor instanceof BF_109E4)
				this.type = 0;
			else if (this.fm.actor instanceof BF_109E4N)
				this.type = 1;
			else if ((this.fm.actor instanceof BF_109E7N) || (this.fm.actor instanceof BF_109E7))
				this.type = 2;
			else if (this.fm.actor instanceof BF_109E1)
				this.type = 3;
			else if (this.fm.actor instanceof BF_109E3)
				this.type = 4;
			else if (this.fm.actor instanceof BF_109E4B)
				this.type = 5;
			else if (this.fm.actor instanceof BF_109E7NZ)
				this.type = 6;
			switch (this.type) {
			case 0: // '\0'
				this.mesh.chunkVisible("Body", false);
				this.mesh.chunkVisible("BodyE4", true);
				this.mesh.chunkVisible("PanelE4_D0", true);
				this.mesh.chunkVisible("PanelE4B_D0", false);
				this.mesh.chunkVisible("PanelE7_D0", false);
				this.mesh.chunkVisible("oxigen-7", true);
				this.mesh.chunkVisible("oxigen-7z", false);
				break;

			case 1: // '\001'
				this.mesh.chunkVisible("Body", false);
				this.mesh.chunkVisible("BodyE4", true);
				this.mesh.chunkVisible("PanelE4_D0", true);
				this.mesh.chunkVisible("PanelE4B_D0", false);
				this.mesh.chunkVisible("PanelE7_D0", false);
				this.mesh.chunkVisible("oxigen-7", true);
				this.mesh.chunkVisible("oxigen-7z", false);
				break;

			case 2: // '\002'
				this.mesh.chunkVisible("Body", true);
				this.mesh.chunkVisible("BodyE4", false);
				this.mesh.chunkVisible("PanelE4_D0", false);
				this.mesh.chunkVisible("PanelE4B_D0", true);
				this.mesh.chunkVisible("PanelE7_D0", false);
				this.mesh.chunkVisible("oxigen-7", true);
				this.mesh.chunkVisible("oxigen-7z", false);
				break;

			case 3: // '\003'
				this.mesh.chunkVisible("Body", false);
				this.mesh.chunkVisible("BodyE4", true);
				this.mesh.chunkVisible("TopOpen", false);
				this.mesh.chunkVisible("TopE1", true);
				this.mesh.chunkVisible("PanelE4_D0", true);
				this.mesh.chunkVisible("PanelE4B_D0", false);
				this.mesh.chunkVisible("PanelE7_D0", false);
				this.mesh.chunkVisible("oxigen-7", true);
				this.mesh.chunkVisible("oxigen-7z", false);
				break;

			case 4: // '\004'
				this.mesh.chunkVisible("Body", false);
				this.mesh.chunkVisible("BodyE4", true);
				this.mesh.chunkVisible("TopOpen", true);
				this.mesh.chunkVisible("TopE1", false);
				this.mesh.chunkVisible("PanelE4_D0", true);
				this.mesh.chunkVisible("PanelE4B_D0", false);
				this.mesh.chunkVisible("PanelE7_D0", false);
				this.mesh.chunkVisible("oxigen-7", true);
				this.mesh.chunkVisible("oxigen-7z", false);
				break;

			case 5: // '\005'
				this.mesh.chunkVisible("Body", false);
				this.mesh.chunkVisible("BodyE4", true);
				this.mesh.chunkVisible("PanelE4_D0", false);
				this.mesh.chunkVisible("PanelE4B_D0", true);
				this.mesh.chunkVisible("PanelE7_D0", false);
				this.mesh.chunkVisible("oxigen-7", true);
				this.mesh.chunkVisible("oxigen-7z", false);
				break;

			case 6: // '\006'
				this.mesh.chunkVisible("Body", true);
				this.mesh.chunkVisible("BodyE4", false);
				this.mesh.chunkVisible("PanelE4_D0", false);
				this.mesh.chunkVisible("PanelE4B_D0", true);
				this.mesh.chunkVisible("PanelE7_D0", false);
				this.mesh.chunkVisible("oxigen-7", false);
				this.mesh.chunkVisible("oxigen-7z", true);
				break;
			}
			// TODO: Storebror, Bugfix required to reflect correct cockpit type in case of
			// repairing Cockpit Damage
			// ------------------------------------------
			ZutiSupportMethods_Air.backupCockpit(this);
			// ------------------------------------------
		}
	}

	protected boolean doFocusEnter() {
		if (super.doFocusEnter()) {
			this.aircraft().hierMesh().chunkVisible("Blister1_D0", false);
			return true;
		} else {
			return false;
		}
	}

	protected void doFocusLeave() {
		if (!this.isFocused())
			return;
		if (this.hasCanopy)
			this.aircraft().hierMesh().chunkVisible("Blister1_D0", true);
		super.doFocusLeave();
	}

	boolean hasCanopy;
	private Variables setOld;
	private Variables setNew;
	private Variables setTmp;
	private LightPointActor light1;
	private LightPointActor light2;
	private float pictAiler;
	private float pictElev;
	private float pictManifold;
	private boolean bNeedSetUp;
	private int type;
	public Vector3f w;
	private static final float speedometerScale[] = { 0.0F, -12.33333F, 18.5F, 37F, 62.5F, 90F, 110.5F, 134F, 158.5F,
			186F, 212.5F, 238.5F, 265F, 289.5F, 315F, 339.5F, 346F, 346F };
	private static final float rpmScale[] = { 0.0F, 11.25F, 54F, 111F, 171.5F, 229.5F, 282.5F, 334F, 342.5F, 342.5F };
	private static final float fuelScale[] = { 0.0F, 9F, 21F, 29.5F, 37F, 48F, 61.5F, 75.5F, 92F, 92F };

	static {
		Property.set(CockpitBF_109Ex.class, "normZN", 0.8F);
		Property.set(CockpitBF_109Ex.class, "gsZN", 0.85F);
	}

}
