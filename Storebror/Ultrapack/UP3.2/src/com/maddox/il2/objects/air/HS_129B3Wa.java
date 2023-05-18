package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.objects.Wreckage;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.GunEmpty;
import com.maddox.il2.objects.weapons.MGunBK75;
import com.maddox.il2.objects.weapons.Pylon;
import com.maddox.il2.objects.weapons.PylonHS129BK75;
import com.maddox.rts.Property;

public class HS_129B3Wa extends HS_129 {

	public HS_129B3Wa() {
		this.phase = 0;
		this.disp = 0.0F;
		this.oldbullets = -1;
		this.g1 = null;
		this.BK75dropped = false;
		this.BK75stabilizingMultiplier = 2.0F;
	}

	public void auxPressed(int i) {
		if (i == 1) { this.FM.CT.dropExternalStores(true); }
		super.auxPressed(i);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (!this.BK75dropped && !this.FM.isPlayers() && (this.isNetMaster() || !this.isNet()) && !this.FM.AS.isPilotDead(0) && !this.isNetPlayer() && this.FM.AS.astateBailoutStep == 0 && (this.FM.AS.astateEngineStates[0] > 2
				|| this.FM.AS.astateEngineStates[1] > 2 || this.FM.EI.engines[0].getReadyness() < 0.5F || this.FM.EI.engines[1].getReadyness() < 0.5F || ((Maneuver) this.FM).get_maneuver() == 49 || this.FM.isReadyToReturn())) {
			this.FM.CT.dropExternalStores(true);
		}
	}

	public boolean dropExternalStores(boolean flag) {
		if (!this.BK75dropped) {
			if (flag && (this.FM.getSpeedKMH() > 342F || this.FM.getSpeedKMH() < 95F)) { return false; }
			this.sfxHit(1.0F, new Point3d(0.0D, 0.0D, -1D));
			this.BK75dropped = true;
			((Gun) this.g1).destroy();
			this.FM.Sq.liftKeel /= this.BK75stabilizingMultiplier;
			if (World.getPlayerAircraft() == this) { World.cur().scoreCounter.playerDroppedExternalStores(50); }
			for (int i = 0; i < this.FM.CT.Weapons.length; i++) {
				BulletEmitter abulletemitter[] = this.FM.CT.Weapons[i];
				if (abulletemitter == null) { continue; }
				for (int j = 0; j < abulletemitter.length; j++) {
					Object obj = abulletemitter[j];
					if (obj instanceof MGunBK75) {
						this.FM.CT.Weapons[i][j] = GunEmpty.get();
						obj = GunEmpty.get();
					}
					if (obj instanceof MGunBK75 || obj instanceof PylonHS129BK75) {
						((Pylon) obj).destroy();
						this.FM.CT.Weapons[i][j] = GunEmpty.get();
						obj = GunEmpty.get();
					}
				}

			}

			Vector3d vector3d = new Vector3d();
			vector3d.set(this.FM.Vwld);
			vector3d.z = vector3d.z - 8D;
			this.hierMesh().chunkVisible("Hole_d0", true);
			Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("BK75Wreck_D0"));
			wreckage.setSpeed(vector3d);
			wreckage.collide(true);
			this.hierMesh().chunkSetAngles("BK75Pod_D0", 0.0F, 180F, 0.0F);
			this.hierMesh().chunkSetAngles("Barrel_D0", 0.0F, 145F, 0.0F);
			this.hierMesh().chunkVisible("BK75Pod_D0", false);
			this.hierMesh().chunkVisible("Barrel_D0", false);
			this.hierMesh().chunkVisible("sled_d0", false);
			this.hierMesh().chunkVisible("shell_d0", false);
			return true;
		} else {
			return false;
		}
	}

	public void onAircraftLoaded() {
		if (this.FM.CT.Weapons[1] != null) { this.g1 = this.FM.CT.Weapons[1][0]; }
		this.FM.Sq.liftKeel *= this.BK75stabilizingMultiplier;
	}

	public void update(float f) {
		if (this.g1 != null) {
			switch (this.phase) {
				default:
					break;

				case 0:
					if (this.g1.isShots() && this.oldbullets != this.g1.countBullets()) {
						this.oldbullets = this.g1.countBullets();
						this.phase++;
						this.hierMesh().chunkVisible("Shell_D0", true);
						this.disp = 0.0F;
					}
					break;

				case 1:
					this.disp += 6F * f;
					this.resetYPRmodifier();
					Aircraft.xyz[0] = this.disp;
					this.hierMesh().chunkSetLocate("Barrel_D0", Aircraft.xyz, Aircraft.ypr);
					Aircraft.xyz[0] = this.disp * 1.5F;
					this.hierMesh().chunkSetLocate("Sled_D0", Aircraft.xyz, Aircraft.ypr);
					if (this.disp >= 0.75F) { this.phase++; }
					break;

				case 2:
					Wreckage wreckage = new Wreckage(this, this.hierMesh().chunkFind("Shell_D0"));
					Eff3DActor.New(wreckage, null, null, 1.0F, Wreckage.SMOKE, 3F);
					Vector3d vector3d = new Vector3d();
					vector3d.set(this.FM.Vwld);
					vector3d.z = vector3d.z - 8D;
					wreckage.setSpeed(vector3d);
					this.hierMesh().chunkVisible("Shell_D0", false);
					this.phase++;
					break;

				case 3:
					this.disp -= 0.8F * f;
					this.resetYPRmodifier();
					Aircraft.xyz[0] = this.disp;
					this.hierMesh().chunkSetLocate("Barrel_D0", Aircraft.xyz, Aircraft.ypr);
					Aircraft.xyz[0] = this.disp * 1.5F;
					this.hierMesh().chunkSetLocate("Sled_D0", Aircraft.xyz, Aircraft.ypr);
					if (this.disp <= 0.0F) {
						this.disp = 0.0F;
						Aircraft.xyz[0] = this.disp;
						this.hierMesh().chunkSetLocate("Barrel_D0", Aircraft.xyz, Aircraft.ypr);
						this.hierMesh().chunkSetLocate("Sled_D0", Aircraft.xyz, Aircraft.ypr);
						this.phase++;
					}
					break;

				case 4:
					this.phase = 0;
					break;
			}
		}
		super.update(f);
	}

	private int           phase;
	private float         disp;
	private int           oldbullets;
	private BulletEmitter g1;
	private boolean       BK75dropped;
	private float         BK75stabilizingMultiplier;

	static {
		Class class1 = HS_129B3Wa.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "Hs-129");
		Property.set(class1, "meshName", "3do/plane/Hs-129B-3Wa/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
		Property.set(class1, "yearService", 1943.9F);
		Property.set(class1, "yearExpired", 1945.5F);
		Property.set(class1, "FlightModel", "FlightModels/Hs-129B-3.fmd");
		Property.set(class1, "cockpitClass", new Class[] { CockpitHS_129B3.class });
		weaponTriggersRegister(class1, new int[] { 0, 0, 1, 9 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_HEAVYCANNON01", "_ExternalDev01" });
	}
}
