package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Pilot;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.objects.weapons.Bomb;
import com.maddox.il2.objects.weapons.BombStarthilfe109500;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public class HE_111H22 extends HE_111xyz implements TypeBomber, TypeTransport, TypeDockable {

	public HE_111H22() {
		this.drones = new Actor[1];
		this.booster = new Bomb[2];
		this.kangle1 = 0.0F;
		this.kangle2 = 0.0F;
		this.slider = false;
		this.sliderX = 0.0F;
		this.sliderZ = 0.0F;
		this.pilot2kill = false;
		this.pilot4kill = false;
		this.pilot5kill = false;
		this.bHasBoosters = true;
		this.boosterFireOutTime = -1L;
		this.FZG76 = false;
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
		for (int i = 0; i < 2; i++)
			if (this.booster[i] != null) {
				this.booster[i].start();
				this.booster[i] = null;
			}

	}

	public void FZG76() {
		if (this.FM.AS.isMaster()) {
			if (Actor.isValid(this.drones[0])) this.FZG76 = true;
			else this.FZG76 = false;
		} else this.FZG76 = false;
	}

	public boolean typeDockableIsDocked() {
		return true;
	}

	public Actor typeDockableGetDrone() {
		return this.drones[0];
	}

	public void typeDockableAttemptAttach() {
	}

	public void typeDockableAttemptDetach() {
		if (this.FM.AS.isMaster()) for (int i = 0; i < this.drones.length; i++)
			if (Actor.isValid(this.drones[i])) this.typeDockableRequestDetach(this.drones[i], i, true);
	}

	public void typeDockableRequestAttach(Actor actor) {
		if (actor instanceof Aircraft) {
			Aircraft aircraft = (Aircraft) actor;
			if (aircraft.FM.AS.isMaster() && aircraft.FM.Gears.onGround() && aircraft.FM.getSpeedKMH() < 10F && this.FM.getSpeedKMH() < 10F) for (int i = 0; i < this.drones.length; i++) {
				if (Actor.isValid(this.drones[i])) continue;
				HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
				Loc loc = new Loc();
				Loc loc1 = new Loc();
				this.pos.getAbs(loc1);
				hooknamed.computePos(this, loc1, loc);
				actor.pos.getAbs(loc1);
				if (loc.getPoint().distance(loc1.getPoint()) >= 5D) continue;
				if (this.FM.AS.isMaster()) this.typeDockableRequestAttach(actor, i, true);
				else this.FM.AS.netToMaster(32, i, 0, actor);
				break;
			}
		}
	}

	public void typeDockableRequestDetach(Actor actor) {
		for (int i = 0; i < this.drones.length; i++)
			if (actor == this.drones[i]) {
				Aircraft aircraft = (Aircraft) actor;
				if (aircraft.FM.AS.isMaster()) if (this.FM.AS.isMaster()) this.typeDockableRequestDetach(actor, i, true);
				else this.FM.AS.netToMaster(33, i, 1, actor);
			}

	}

	public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
		if (i >= 0 && i <= 1) if (flag) {
			if (this.FM.AS.isMaster()) {
				this.FM.AS.netToMirrors(34, i, 1, actor);
				this.typeDockableDoAttachToDrone(actor, i);
			} else this.FM.AS.netToMaster(34, i, 1, actor);
		} else if (this.FM.AS.isMaster()) {
			if (!Actor.isValid(this.drones[i])) {
				this.FM.AS.netToMirrors(34, i, 1, actor);
				this.typeDockableDoAttachToDrone(actor, i);
			}
		} else this.FM.AS.netToMaster(34, i, 0, actor);
	}

	public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
		if (flag) if (this.FM.AS.isMaster()) {
			this.FM.AS.netToMirrors(35, i, 1, actor);
			this.typeDockableDoDetachFromDrone(i);
		} else this.FM.AS.netToMaster(35, i, 1, actor);
	}

	public void typeDockableDoAttachToDrone(Actor actor, int i) {
		if (!Actor.isValid(this.drones[i])) {
			HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
			Loc loc = new Loc();
			Loc loc1 = new Loc();
			this.pos.getAbs(loc1);
			hooknamed.computePos(this, loc1, loc);
			actor.pos.setAbs(loc);
			actor.pos.setBase(this, null, true);
			actor.pos.resetAsBase();
			this.drones[i] = actor;
			((TypeDockable) this.drones[i]).typeDockableDoAttachToQueen(this, i);
		}
	}

	public void typeDockableDoDetachFromDrone(int i) {
		if (Actor.isValid(this.drones[i])) {
			this.drones[i].pos.setBase(null, null, true);
			((TypeDockable) this.drones[i]).typeDockableDoDetachFromQueen(i);
			this.drones[i] = null;
		}
	}

	public void typeDockableDoAttachToQueen(Actor actor1, int j) {
	}

	public void typeDockableDoDetachFromQueen(int j) {
	}

	public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		for (int i = 0; i < this.drones.length; i++)
			if (Actor.isValid(this.drones[i])) {
				netmsgguaranted.writeByte(1);
				com.maddox.il2.engine.ActorNet actornet = this.drones[i].net;
				if (actornet.countNoMirrors() == 0) netmsgguaranted.writeNetObj(actornet);
				else netmsgguaranted.writeNetObj(null);
			} else netmsgguaranted.writeByte(0);

	}

	public void typeDockableReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		for (int i = 0; i < this.drones.length; i++)
			if (netmsginput.readByte() == 1) {
				NetObj netobj = netmsginput.readNetObj();
				if (netobj != null) this.typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
			}

	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xnose")) {
			if (this.chunkDamageVisible("Nose") < 2) this.hitChunk("Nose", shot);
			if (shot.power > 200000F) {
				this.FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(3, 192));
				this.FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(3, 192));
			}
			if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40);
			if (point3d.x > 4.505000114440918D) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
			else if (point3d.y > 0.0D) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
				if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8);
			} else {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				if (World.Rnd().nextFloat() < 0.1F) this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20);
			}
		} else if (s.equals("xxarmorg1")) this.getEnergyPastArmor(5F, shot);
		else if (s.equals("xxarmoro1")) this.getEnergyPastArmor(8F, shot);
		else if (s.equals("xxarmoro2")) this.getEnergyPastArmor(8F, shot);
		else if (s.equals("xoil1") && shot.power > 0.0F) {
			this.FM.AS.hitOil(shot.initiator, 0);
			if (this.chunkDamageVisible("Engine1") < 2) this.hitChunk("Engine1", shot);
		} else if (s.equals("xoil2") && shot.power > 0.0F) {
			this.FM.AS.hitOil(shot.initiator, 1);
			if (this.chunkDamageVisible("Engine2") < 2) this.hitChunk("Engine2", shot);
		} else if (s.startsWith("xxoil")) {
			int i = 0;
			if (s.endsWith("2")) i = 1;
			if (this.getEnergyPastArmor(0.21F, shot) > 0.0F && World.Rnd().nextFloat() < 0.49F) {
				this.FM.AS.hitOil(shot.initiator, i);
				this.getEnergyPastArmor(0.42F, shot);
			}
		} else super.hitBone(s, shot, point3d);
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
		switch (i) {
			case 1:
				this.hierMesh().chunkVisible("Pilot1_FAK", false);
				this.hierMesh().chunkVisible("Head1_FAK", false);
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot2_FAK", false);
				break;

			case 4:
				this.hierMesh().chunkVisible("Pilot4_FAK", false);
				break;
		}
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.crew = 5;
		this.FM.AS.astatePilotFunctions[0] = 1;
		this.FM.AS.astatePilotFunctions[1] = 9;
		this.FM.AS.astatePilotFunctions[2] = 4;
		this.FM.AS.astatePilotFunctions[3] = 6;
		this.FM.AS.astatePilotFunctions[4] = 5;
		this.FM.CT.bHasCockpitDoorControl = false;
		for (int i = 0; i < 2; i++)
			try {
				this.booster[i] = new BombStarthilfe109500();
				this.booster[i].pos.setBase(this, this.findHook("_BoosterH" + (i + 1)), false);
				this.booster[i].pos.resetAsBase();
				this.booster[i].drawing(true);
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
				this.typeDockableRequestDetach(this.drones[0], 0, true);
				break;

			case 3:
			case 4:
			case 15:
			case 17:
			case 18:
			case 31:
			case 32:
				this.typeDockableRequestDetach(this.drones[0], 0, true);
				break;
		}
		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag) {
		if (flag) {
			if (this.FM.AS.astateEngineStates[0] > 3) {
				if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.hitTank(this, 0, 1);
				if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.hitTank(this, 1, 1);
			}
			if (this.FM.AS.astateEngineStates[1] > 3) {
				if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.hitTank(this, 2, 1);
				if (World.Rnd().nextFloat() < 0.05F) this.FM.AS.hitTank(this, 3, 1);
			}
			if (this.FM.AS.astateTankStates[0] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this);
			if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this);
			if (this.FM.AS.astateTankStates[1] > 5 && World.Rnd().nextFloat() < 0.125F) this.FM.AS.hitTank(this, 2, 1);
			if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.125F) this.FM.AS.hitTank(this, 1, 1);
			if (this.FM.AS.astateTankStates[2] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this);
			if (this.FM.AS.astateTankStates[3] > 5 && World.Rnd().nextFloat() < 0.02F) this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this);
		}
		if (this.FM.getAltitude() < 3000F) {
			this.hierMesh().chunkVisible("HMask1_D0", false);
			this.hierMesh().chunkVisible("HMask2_D0", false);
			this.hierMesh().chunkVisible("HMask3_D0", false);
			this.hierMesh().chunkVisible("HMask4_D0", false);
			this.hierMesh().chunkVisible("HMask5_D0", false);
		} else {
			this.hierMesh().chunkVisible("HMask1_D0", this.hierMesh().isChunkVisible("Pilot1_FAK"));
			this.hierMesh().chunkVisible("HMask2_D0", this.hierMesh().isChunkVisible("Pilot2_FAK"));
			this.hierMesh().chunkVisible("HMask3_D0", this.hierMesh().isChunkVisible("Pilot3_D0"));
			this.hierMesh().chunkVisible("HMask4_D0", this.hierMesh().isChunkVisible("Pilot4_FAK"));
			this.hierMesh().chunkVisible("HMask5_D0", this.hierMesh().isChunkVisible("Pilot5_D0"));
		}
	}

	public void update(float f) {
		this.FZG76();
		this.hierMesh().chunkSetAngles("WaterLukL_D0", 0.0F, 0.0F, 30F * this.kangle1);
		this.hierMesh().chunkSetAngles("WaterLukR_D0", 0.0F, 0.0F, 30F * this.kangle2);
		this.kangle1 = 0.95F * this.kangle1 + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		this.kangle2 = 0.95F * this.kangle2 + 0.05F * this.FM.EI.engines[1].getControlRadiator();
		if (this.slider) {
			if (this.sliderX < 0.9F) this.sliderX += 0.01F;
			if (this.sliderZ < 0.05F) this.sliderZ += 0.005F;
			Aircraft.xyz[0] = 0.0F;
			Aircraft.xyz[1] = this.sliderX;
			Aircraft.xyz[2] = this.sliderZ;
			this.hierMesh().chunkSetLocate("Window_D0", Aircraft.xyz, Aircraft.ypr);
		}
		if (this.FM.AS.astatePlayerIndex == 1) {
			if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[1]) this.SturmanBusy(1);
			else if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[2]) this.SturmanBusy(2);
		} else if (Time.current() > this.tme3) {
			this.tme3 = Time.current() + 100L;
			if (this.FM.turret.length != 0 && !this.pilot2kill) this.FM.turret[0].bIsOperable = true;
		}
		if (this.FM.AS.astatePlayerIndex == 4) {
			if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[6]) this.SturmanBusy(6);
			else if (Main3D.cur3D().cockpitCur == Main3D.cur3D().cockpits[5]) this.SturmanBusy(5);
		} else if (Time.current() > this.tme) {
			this.tme = Time.current() + 1500L;
			if (this.FM.turret.length != 0) {
				Actor actor = null;
				actor = this.FM.turret[3].target;
				if (actor == null) actor = this.FM.turret[4].target;
				if (actor != null) if (Actor.isValid(actor)) {
					this.pos.getAbs(Aircraft.tmpLoc2);
					actor.pos.getAbs(Aircraft.tmpLoc3);
					Aircraft.tmpLoc2.transformInv(Aircraft.tmpLoc3.getPoint());
					if (Aircraft.tmpLoc3.getPoint().y < 0.0D) this.SturmanBusy(6);
					else this.SturmanBusy(5);
				} else if (!this.pilot5kill) {
					this.FM.turret[3].bIsOperable = true;
					this.FM.turret[4].bIsOperable = true;
				}
			}
		}
		super.update(f);
		if (!(this.FM instanceof Pilot)) return;
		if (this.bHasBoosters) {
			if (this.FM.getAltitude() > 300F && this.boosterFireOutTime == -1L && this.FM.Loc.z != 0.0D && World.Rnd().nextFloat() < 0.05F) {
				this.doCutBoosters();
				this.FM.AS.setGliderBoostOff();
				this.bHasBoosters = false;
			}
			if (this.bHasBoosters && this.boosterFireOutTime == -1L && this.FM.Gears.onGround() && this.FM.EI.getPowerOutput() > 0.8F && this.FM.EI.engines[0].getStage() == 6 && this.FM.EI.engines[1].getStage() == 6 && this.FM.getSpeedKMH() > 20F) {
				this.boosterFireOutTime = Time.current() + 30000L;
				this.doFireBoosters();
				this.FM.AS.setGliderBoostOn();
			}
			if (this.bHasBoosters && this.boosterFireOutTime > 0L) {
				if (Time.current() < this.boosterFireOutTime) this.FM.producedAF.x += 20000D;
				if (Time.current() > this.boosterFireOutTime + 10000L) {
					this.doCutBoosters();
					this.FM.AS.setGliderBoostOff();
					this.bHasBoosters = false;
				}
			}
		}
	}

	public void typeDiveBomberReplicateToNet(NetMsgGuaranted netmsgguaranted1) throws IOException {
	}

	public void typeDiveBomberReplicateFromNet(NetMsgInput netmsginput1) throws IOException {
	}

	private void SturmanBusy(int i) {
		switch (i) {
			case 3:
			default:
				break;

			case 1:
				if (!this.pilot2kill) this.FM.turret[0].bIsOperable = false;
				break;

			case 2:
				if (!this.pilot2kill) this.FM.turret[0].bIsOperable = true;
				break;

			case 4:
				if (!this.pilot4kill) this.FM.turret[2].bIsOperable = true;
				break;

			case 5:
				if (!this.pilot5kill) {
					this.FM.turret[3].bIsOperable = true;
					this.FM.turret[4].bIsOperable = false;
					this.hierMesh().chunkSetAngles("Pilot5_D0", -180F, 0.0F, 0.0F);
				}
				break;

			case 6:
				if (!this.pilot5kill) {
					this.FM.turret[3].bIsOperable = false;
					this.FM.turret[4].bIsOperable = true;
					this.hierMesh().chunkSetAngles("Pilot5_D0", 0.0F, 0.0F, 0.0F);
				}
				break;
		}
	}

	public void hitDaSilk() {
		if (this.FM.AS.astatePilotStates[0] < 95 && !this.slider) this.slider = true;
		super.hitDaSilk();
	}

	public boolean turretAngles(int i, float af[]) {
		for (int j = 0; j < 2; j++) {
			af[j] = (af[j] + 3600F) % 360F;
			if (af[j] > 180F) af[j] -= 360F;
		}

		af[2] = 0.0F;
		boolean flag = true;
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f < -25F) {
					f = -25F;
					flag = false;
				}
				if (f > 15F) {
					f = 15F;
					flag = false;
				}
				if (f1 < -40F) {
					f1 = -40F;
					flag = false;
				}
				if (f1 > 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				break;

			case 1:
				if (f < -40F) {
					f = -40F;
					flag = false;
				}
				if (f > 40F) {
					f = 40F;
					flag = false;
				}
				if (f1 < -5F) {
					f1 = -5F;
					flag = false;
				}
				if (f1 > 60F) {
					f1 = 60F;
					flag = false;
				}
				break;

			case 2:
				if (this.FZG76) {
					if (f < -7F) f = -7F;
					flag = false;
				} else {
					if (f < -45F) f = -45F;
					flag = false;
				}
				if (f > 45F) {
					f = 45F;
					flag = false;
				}
				if (f1 < -30F) {
					f1 = -30F;
					flag = false;
				}
				if (f1 > 46F) {
					f1 = 46F;
					flag = false;
				}
				break;

			case 3:
				if (f < -40F) {
					f = -40F;
					flag = false;
				}
				if (f > 30F) {
					f = 30F;
					flag = false;
				}
				if (f1 < -40F) {
					f1 = -40F;
					flag = false;
				}
				if (f1 > 30F) {
					f1 = 30F;
					flag = false;
				}
				break;

			case 4:
				if (f < -30F) {
					f = -30F;
					flag = false;
				}
				if (f > 40F) {
					f = 40F;
					flag = false;
				}
				if (this.FZG76) {
					if (f1 < -23F) f1 = -23F;
					flag = false;
				} else {
					if (f1 < -40F) f1 = -40F;
					flag = false;
				}
				if (f1 > 30F) {
					f1 = 30F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void doMurderPilot(int i) {
		switch (i) {
			default:
				break;

			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("HMask1_D0", false);
				if (this.bPitUnfocused && !this.FM.AS.bIsAboutToBailout) {
					this.hierMesh().chunkVisible("Pilot1_FAK", false);
					this.hierMesh().chunkVisible("Pilot1_FAL", true);
					this.hierMesh().chunkVisible("Head1_FAK", false);
				}
				if (this.FM.AS.bIsAboutToBailout) {
					this.hierMesh().chunkVisible("Pilot1_FAK", false);
					this.hierMesh().chunkVisible("Pilot1_FAL", false);
					this.hierMesh().chunkVisible("Head1_FAK", false);
				}
				if (this.hierMesh().isChunkVisible("Nose_D0") || this.hierMesh().isChunkVisible("Nose_D1") || this.hierMesh().isChunkVisible("Nose_D2")) this.hierMesh().chunkVisible("Gore1_D0", true);
				break;

			case 1:
				this.pilot2kill = true;
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				if (this.bPitUnfocused && !this.FM.AS.bIsAboutToBailout) {
					this.hierMesh().chunkVisible("Pilot2_FAK", false);
					this.hierMesh().chunkVisible("Pilot2_FAL", true);
					this.hierMesh().chunkVisible("HMask2_D0", false);
				}
				if (this.FM.AS.bIsAboutToBailout) {
					this.hierMesh().chunkVisible("Pilot2_FAK", false);
					this.hierMesh().chunkVisible("Pilot2_FAL", false);
					this.hierMesh().chunkVisible("HMask2_D0", false);
				}
				if (this.hierMesh().isChunkVisible("Nose_D0") || this.hierMesh().isChunkVisible("Nose_D1") || this.hierMesh().isChunkVisible("Nose_D2")) this.hierMesh().chunkVisible("Gore2_D0", true);
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", true);
				this.hierMesh().chunkVisible("HMask3_D0", false);
				this.hierMesh().chunkVisible("Gore3_D0", true);
				break;

			case 3:
				this.pilot4kill = true;
				this.hierMesh().chunkVisible("Pilot4_D0", false);
				this.hierMesh().chunkVisible("Pilot4_D1", true);
				this.hierMesh().chunkVisible("HMask4_D0", false);
				if (this.bPitUnfocused && !this.FM.AS.bIsAboutToBailout) {
					this.hierMesh().chunkVisible("Pilot4_FAK", false);
					this.hierMesh().chunkVisible("Pilot4_FAL", true);
					this.hierMesh().chunkVisible("HMask4_D0", false);
				}
				if (this.FM.AS.bIsAboutToBailout) {
					this.hierMesh().chunkVisible("Pilot4_FAK", false);
					this.hierMesh().chunkVisible("Pilot4_FAL", false);
					this.hierMesh().chunkVisible("HMask4_D0", false);
				}
				break;

			case 4:
				this.pilot5kill = true;
				this.hierMesh().chunkVisible("Pilot5_D0", false);
				this.hierMesh().chunkVisible("Pilot5_D1", true);
				this.hierMesh().chunkVisible("HMask5_D0", false);
				break;
		}
	}

	protected void moveBayDoor(float f1) {
	}

	private Actor     drones[];
	protected boolean FZG76;
	private float     kangle1;
	private float     kangle2;
	private boolean   slider;
	private float     sliderX;
	private float     sliderZ;
	private long      tme;
	private long      tme3;
	private boolean   pilot2kill;
	private boolean   pilot4kill;
	private boolean   pilot5kill;
	private Bomb      booster[];
	protected boolean bHasBoosters;
	protected long    boosterFireOutTime;

	static {
		Class class1 = HE_111H22.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "He-111");
		Property.set(class1, "meshName", "3do/plane/He-111H-22/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar02());
		Property.set(class1, "yearService", 1944F);
		Property.set(class1, "yearExpired", 1948F);
		Property.set(class1, "FlightModel", "FlightModels/He-111H-22.fmd");
		Property.set(class1, "cockpitClass",
				new Class[] { CockpitHE_111H11.class, CockpitHE_111H11_Bombardier.class, CockpitHE_111H11_NGunner.class, CockpitHE_111H16_TGunner.class, CockpitHE_111H22_BGunner.class, CockpitHE_111H16_LGunner.class, CockpitHE_111H22_RGunner.class });
		Aircraft.weaponTriggersRegister(class1, new int[] { 10, 11, 12, 13, 14, 12, 13, 14 });
		Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08" });
	}
}
