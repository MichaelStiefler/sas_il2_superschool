package com.maddox.il2.objects.air;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.rts.Property;
import com.maddox.rts.Time;

public abstract class Letov extends Scheme1 implements TypeScout, TypeBomber, TypeTransport, TypeStormovik {

	public Letov() {
		this.lookoutTimeLeft = 2.0F;
		this.lookoutAz = 0.0F;
		this.lookoutEl = 0.0F;
		this.lookoutPos = new float[3][128];
		this.turretPos = new float[3];
		this.turretAng = new float[3];
		this.tu = new float[3];
		this.afBk = new float[3];
		this.turretPos[0] = 0.0F;
		this.turretPos[1] = 0.0F;
		this.turretPos[2] = 0.0F;
		this.turretAng[0] = 0.0F;
		this.turretAng[1] = 0.0F;
		this.turretAng[2] = 0.0F;
		this.turretLRposW = 0.0F;
		this.btme = -1L;
		this.fGunPos = 0.0F;
		this.turretState = 6;
		this.tu[0] = 0.0F;
		this.tu[1] = 0.0F;
		this.tu[2] = 0.0F;
		this.afBk[0] = 0.0F;
		this.afBk[1] = 0.0F;
		this.afBk[2] = 0.0F;
		this.finishedTurn = false;
		this.lookout = 0.0F;
		this.bGunnerKilled = false;
		this.gunOutOverride = 0;
		for (int i = 0; i < 128; i++) {
			this.lookoutPos[0][i] = World.Rnd().nextFloat() * 180F - 90F;
			this.lookoutPos[1][i] = World.Rnd().nextFloat() * 100F - 50F;
			if (this.lookoutPos[1][i] > 0.0F) {
				this.lookoutPos[2][i] = World.Rnd().nextFloat() * 2.0F;
			} else {
				this.lookoutPos[2][i] = (World.Rnd().nextFloat() + World.Rnd().nextFloat() + World.Rnd().nextFloat() + World.Rnd().nextFloat()) * 3F;
			}
		}

	}

	public boolean cut(String s) {
		boolean flag = super.cut(s);
		if (s.equalsIgnoreCase("WingLIn")) {
			this.hierMesh().chunkVisible("WingLMid_CAP", true);
		} else if (s.equalsIgnoreCase("WingRIn")) { this.hierMesh().chunkVisible("WingRMid_CAP", true); }
		return flag;
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			default:
				break;

			case 19:
				this.FM.Gears.hitCentreGear();
				break;

			case 3:
				if (World.Rnd().nextInt(0, 99) < 1) {
					this.FM.AS.hitEngine(this, 0, 4);
					this.hitProp(0, j, actor);
					this.FM.EI.engines[0].setEngineStuck(actor);
					return this.cut("engine1");
				} else {
					this.FM.AS.setEngineDies(this, 0);
					return false;
				}
		}
		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
	}

	protected void moveFlap(float f) {
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -29F * f, 0.0F);
	}

	protected void moveElevator(float f) {
		this.hierMesh().chunkSetAngles("VatorL_D0", 0.0F, -35F * f, 0.0F);
		this.hierMesh().chunkSetAngles("VatorR_D0", 0.0F, -35F * f, 0.0F);
		float f1 = this.FM.CT.getAileron();
		this.hierMesh().chunkSetAngles("pilot1_arm2_d0", cvt(f1, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f1, -1F, 1.0F, 6F, -8F) - cvt(f, -1F, 1.0F, -37F, 35F));
		this.hierMesh().chunkSetAngles("pilot1_arm1_d0", 0.0F, 0.0F, cvt(f1, -1F, 1.0F, -16F, 14F) + cvt(f, -1F, 0.0F, -61F, 0.0F) + cvt(f, 0.0F, 1.0F, 0.0F, 43F));
		if (f < 0.0F) { f /= 2.0F; }
		this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 15F * f1, cvt(f, -1F, 1.0F, -16F, 20F));
	}

	protected void moveAileron(float f) {
		this.hierMesh().chunkSetAngles("AroneL1_D0", 0.0F, -35F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneL2_D0", 0.0F, -35F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneL3_D0", 0.0F, -35F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneR1_D0", 0.0F, -35F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneR2_D0", 0.0F, -35F * f, 0.0F);
		this.hierMesh().chunkSetAngles("AroneR3_D0", 0.0F, -35F * f, 0.0F);
		float f1 = this.FM.CT.getElevator();
		this.hierMesh().chunkSetAngles("pilot1_arm2_d0", cvt(f, -1F, 1.0F, 14F, -16F), 0.0F, cvt(f, -1F, 1.0F, 6F, -8F) - cvt(f1, -1F, 1.0F, -37F, 35F));
		this.hierMesh().chunkSetAngles("pilot1_arm1_d0", 0.0F, 0.0F, cvt(f, -1F, 1.0F, -16F, 14F) + cvt(f1, -1F, 0.0F, -61F, 0.0F) + cvt(f1, 0.0F, 1.0F, 0.0F, 43F));
		if (f1 < 0.0F) { f1 /= 2.0F; }
		this.hierMesh().chunkSetAngles("Stick_D0", 0.0F, 15F * f, cvt(f1, -1F, 1.0F, -16F, 20F));
	}

	public void doRemoveBodyFromPlane(int i) {
		super.doRemoveBodyFromPlane(i);
		this.hierMesh().chunkVisible("pilot1_arm2_d0", false);
		this.hierMesh().chunkVisible("pilot1_arm1_d0", false);
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				this.hierMesh().chunkVisible("pilot1_arm2_d0", false);
				this.hierMesh().chunkVisible("pilot1_arm1_d0", false);
				break;

			case 1:
				this.bGunnerKilled = true;
				this.FM.turret[0].bIsOperable = false;
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("Head2_D0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				break;
		}
	}

	private void moveAndTurnTurret(float af[]) {
		float f = Math.abs(af[0]);
		if (!this.bGunnerKilled) {
			this.turretAng[1] = 0.0F;
			this.turretAng[2] = 0.0F;
			this.turretPos[0] = 0.0F;
			this.turretPos[2] = 0.0F;
			if (this.turretFront > 0.5F) {
				if (af[0] < 0.0F) {
					this.turretAng[0] = (720F - af[0]) / 5F + this.turretLRpos * 25F;
				} else {
					this.turretAng[0] = (-720F - af[0]) / 5F + this.turretLRpos * 25F;
				}
				this.turretPos[1] = -0.6F + 0.2F * (float) Math.sin(Math.toRadians(f));
				this.turretAng[2] = 20F;
			} else {
				this.turretAng[0] = -af[0] / 5F - this.turretLRpos * 25F;
				this.turretPos[1] = -0.4F * (float) Math.sin(Math.toRadians(f));
			}
			this.hierMesh().chunkSetLocate("Pilot2_D0", this.turretPos, this.turretAng);
		}
		this.turretAng[0] = 0.0F;
		this.turretAng[1] = 0.0F;
		this.turretAng[2] = 0.0F;
		this.turretPos[2] = 0.0F;
		this.turretPos[1] = -0.6F * this.turretFront;
		this.turretPos[0] = 0.0F;
		this.hierMesh().chunkSetLocate("tyc", this.turretPos, this.turretAng);
		this.turretPos[1] = 0.0F;
		this.turretPos[0] = 0.35F * this.turretLRpos;
		this.hierMesh().chunkSetLocate("buben", this.turretPos, this.turretAng);
		this.hierMesh().chunkSetAngles("Turret1A_D0X", -af[0], 0.0F, 0.0F);
		this.hierMesh().chunkSetAngles("Turret1B_D0X", 0.0F, af[1], 0.0F);
	}

	private boolean turretMoves(float af[]) {
		boolean flag = true;
		float f = Math.abs(af[0]);
		if (af[0] == 0.0F && af[1] == 0.0F) {
			this.turretLRposW = 0.0F;
		} else {
			this.turretLRposW = (float) Math.cos(Math.toRadians(f));
			this.turretLRposW *= this.turretLRposW;
			this.turretLRposW *= this.turretLRposW;
			this.turretLRposW *= this.turretLRposW;
			if (af[0] < 0.0F) {
				this.turretLRposW = this.turretLRposW - 1.0F;
			} else {
				this.turretLRposW = 1.0F - this.turretLRposW;
			}
			if (this.turretState < 5 && (af[1] > -25F && f < 20F || af[1] > -5F)) {
				if (af[0] < 0.0F) {
					this.turretLRposW = -1F;
				} else {
					this.turretLRposW = 1.0F;
				}
			}
		}
		if (this.turretLRposW < -1F) { this.turretLRposW = -1F; }
		if (this.turretLRposW > 1.0F) { this.turretLRposW = 1.0F; }
		this.turretLRpos = 0.95F * this.turretLRpos + 0.05F * this.turretLRposW;
		float f1 = 0.0F;
		if (f > 100F - 20F * this.turretFront) { f1 = 1.0F; }
		if (this.turretFront < 0.0F) {
			this.turretFront = 0.0F;
		} else if (this.turretFront > 1.0F) { this.turretFront = 1.0F; }
		if (f1 - this.turretFront > 0.01D || f1 - this.turretFront < -0.01D) {
			if (f1 > this.turretFront) {
				this.turretFront += 0.15F * (0.3F - (0.5F - this.turretFront) * (0.5F - this.turretFront));
			} else {
				this.turretFront -= 0.15F * (0.3F - (0.5F - this.turretFront) * (0.5F - this.turretFront));
			}
			if (f1 - this.turretFront > 0.1D || f1 - this.turretFront < -0.1D) { flag = false; }
		}
		return flag;
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		for (; af[0] > 180F; af[0] -= 360F) {
			;
		}
		for (; af[0] < -180F; af[0] += 360F) {
			;
		}
		float f = Math.abs(af[0]);
		if (af[1] < -50F) {
			af[1] = -50F;
			flag = false;
		}
		float f1;
		if (f < 7F) {
			if (f < 5F) {
				f1 = -2F;
			} else {
				f1 = f * 7.5F - 39.5F;
			}
		} else if (f < 55F) {
			f1 = f + 6F;
		} else if (f < 95F) {
			f1 = 50F + f / 5F;
		} else if (f < 155F) {
			f1 = 206.7F - 1.46F * f;
		} else {
			f1 = -20F;
		}
		if (af[1] > f1) {
			af[1] = f1;
			flag = false;
		}
		if (f < 34F && af[1] < 3F && af[1] > -3F) { flag = false; }
		if (f > 100F) {
			if (af[1] > -20F) { flag = false; }
		} else if (f > 90F && af[1] > 12F) { flag = false; }
		this.afBk[0] = af[0];
		this.afBk[1] = af[1];
		if (this.turretState != 4) {
			return false;
		} else {
			flag &= this.turretMoves(af);
			this.moveAndTurnTurret(af);
			return flag;
		}
	}

	private boolean anglesMoveTo(float af[], float f) {
		float f2 = f * MAX_DANGLE;
		this.finishedTurn = true;
		float f1;
		for (f1 = af[0] - this.tu[0]; f1 < -180F; f1 += 360F) {
			;
		}
		for (; f1 > 180F; f1 -= 360F) {
			;
		}
		if (f1 > f2) {
			f1 = f2;
			this.finishedTurn = false;
		}
		if (f1 < -f2) {
			f1 = -f2;
			this.finishedTurn = false;
		}
		this.tu[0] += f1;
		for (f1 = af[1] - this.tu[1]; f1 < -180F; f1 += 360F) {
			;
		}
		for (; f1 > 180F; f1 -= 360F) {
			;
		}
		if (f1 > f2) {
			f1 = f2;
			this.finishedTurn = false;
		}
		if (f1 < -f2) {
			f1 = -f2;
			this.finishedTurn = false;
		}
		this.tu[1] += f1;
		return this.finishedTurn;
	}

	private void gunStoreMoveAnim() {
		float f = this.fGunPos * this.fGunPos * (3F - 2.0F * this.fGunPos);
		this.hierMesh().chunkSetAngles("buben", 0.0F, 0.0F, 70F * (1.0F - f));
		if (this.fGunPos > 0.8F) {
			this.hierMesh().chunkSetAngles("Turret1B_D0X", 0.0F, 0.0F, 0.0F);
		} else {
			float f1 = 1.0F - this.fGunPos * 1.25F;
			this.hierMesh().chunkSetAngles("Turret1B_D0X", 0.0F, 70F * f1 * f1 * (2.0F * f1 - 3F), 0.0F);
		}
	}

	private void setGunnerSitting(boolean flag) {
		if (flag) {
			this.turretAng[0] = 180F;
			this.turretAng[1] = 0.0F;
			this.turretAng[2] = 15F;
			this.turretPos[0] = 0.0F;
			this.turretPos[1] = -0.3F;
			this.turretPos[2] = -0.15F;
			this.hierMesh().chunkSetLocate("Pilot2_D0", this.turretPos, this.turretAng);
			this.lookoutAz = 0.0F;
			this.lookoutEl = 0.0F;
			this.turnHead(this.lookoutAz, this.lookoutEl);
			this.lookoutAnim = -1F;
			this.lookoutTimeLeft = -1F;
		}
	}

	private boolean gunStoreTurnAnim(float af[], float f) {
		boolean flag;
		if (this.anglesMoveTo(af, f)) {
			flag = true;
		} else {
			flag = false;
		}
		this.turretMoves(this.tu);
		this.moveAndTurnTurret(this.tu);
		return flag;
	}

	private void turnHead(float f, float f1) {
		this.turretAng[0] = 0.0F;
		this.turretAng[1] = f;
		this.turretAng[2] = 0.0F;
		this.turretPos[0] = 0.3F * (1.0F - (float) Math.cos(Math.toRadians(f)));
		this.turretPos[1] = 0.0F;
		this.turretPos[2] = -0.28F * (float) Math.sin(Math.toRadians(f));
		this.hierMesh().chunkSetLocate("Head2_D0", this.turretPos, this.turretAng);
	}

	void gunnerLookout(float f) {
		this.lookoutTimeLeft -= f;
		if (this.lookoutTimeLeft > 0.0F) { return; }
		if (this.lookoutAnim < 0.0F) {
			this.lookoutIndex++;
			if (this.lookoutIndex > 127) { this.lookoutIndex = 0; }
			this.lookoutTimeLeft = this.lookoutPos[2][this.lookoutIndex];
			float f1 = this.lookoutPos[0][this.lookoutIndex] - this.lookoutAz;
			float f3 = this.lookoutPos[1][this.lookoutIndex] - this.lookoutEl;
			this.lookoutAnim = 0.001F;
			this.lookout = 0.0F;
			if (f1 * f1 > f3 * f3) {
				this.lookoutMax = Math.abs(f1);
			} else {
				this.lookoutMax = Math.abs(f3);
			}
			if (this.lookoutMax == 0.0F) { this.lookoutMax = 1E-005F; }
			this.lookoutAzSpd = f1 / this.lookoutMax;
			this.lookoutElSpd = f3 / this.lookoutMax;
			return;
		}
		if (2.0F * this.lookout > this.lookoutMax) {
			this.lookoutAnim -= f;
		} else {
			this.lookoutAnim += f;
		}
		float f2 = 1.0F - 5F / (this.lookoutAnim + 5F);
		f2 = f * f2 * 800F;
		this.lookoutAz += f2 * this.lookoutAzSpd;
		this.lookoutEl += f2 * this.lookoutElSpd;
		this.lookout += f2;
		this.turnHead(this.lookoutAz, this.lookoutEl);
	}

	private void gunnerTurnInit(boolean flag) {
		if (flag) {
			this.turnPos = 1.0F;
		} else {
			this.lookoutAz = 0.0F;
			this.lookoutEl = 0.0F;
			this.turnPos = 0.0F;
		}
	}

	private void gunnerTurn(float f) {
		float f1 = f * f * (3F - 2.0F * f);
		this.turnHead(f1 * this.lookoutAz, f1 * this.lookoutEl);
		if (this.lookoutAz > 0.0F) {
			this.turretAng[0] = f1 * 180F;
		} else {
			this.turretAng[0] = -f1 * 180F;
		}
		this.turretAng[1] = 0.0F;
		this.turretAng[2] = f1 * 15F;
		this.turretPos[0] = 0.0F;
		this.turretPos[1] = -f1 * 0.3F;
		this.turretPos[2] = -f1 * 0.15F;
		this.hierMesh().chunkSetLocate("Pilot2_D0", this.turretPos, this.turretAng);
	}

	public void update(float f) {
		this.kangle = 0.95F * this.kangle + 0.05F * this.FM.EI.engines[0].getControlRadiator();
		if (this.kangle > 1.0F) { this.kangle = 1.0F; }
		this.hierMesh().chunkSetAngles("radiator1_D0", 0.0F, -55F * this.kangle, 0.0F);
		this.hierMesh().chunkSetAngles("radiator2_D0", 0.0F, -40F * this.kangle, 0.0F);
		if (!this.bGunnerKilled && !this.FM.AS.isPilotParatrooper(this.FM.AS.astatePilotStates[1])) {
			if (this.FM.AS.isMaster() || NetMissionTrack.isPlaying()) {
				if (this.turretState == 0 && (this.gunOutOverride == 1 || this.FM.turret[0].target != null)) {
					this.turretState = 1;
					this.setGunnerSitting(false);
					this.gunnerTurnInit(true);
				}
				if (Time.current() > this.btme) {
					this.btme = Time.current() + World.Rnd().nextLong(5000L, 12000L);
					if (this.FM.turret[0].target == null && this.turretState == 4 && this.gunOutOverride == 0) {
						this.turretState = 5;
						this.tu[0] = this.afBk[0];
						this.tu[1] = this.afBk[1];
						this.afBk[0] = 0.0F;
						this.afBk[1] = 0.0F;
					}
				}
			}
			switch (this.turretState) {
				case ACTIVE:
				default:
					break;

				case SLEEPING:
					this.gunnerLookout(f);
					break;

				case GOING_OUT_M:
					this.turnPos -= f;
					if (this.turnPos < 0.0F) {
						this.turretState = 2;
						this.turnPos = 0.0F;
					}
					this.gunnerTurn(this.turnPos);
					break;

				case GOING_OUT_PULL:
					this.fGunPos += 1.0F * f;
					this.gunStoreMoveAnim();
					if (this.fGunPos > 0.999D) { this.turretState = 3; }
					break;

				case GOING_OUT_T:
					if (this.gunStoreTurnAnim(this.afBk, f)) { this.turretState = 4; }
					break;

				case GOING_IN_T:
					if (this.gunStoreTurnAnim(this.afBk, f)) { this.turretState = 6; }
					break;

				case GOING_IN_PULL:
					if (this.turretLRpos < 0.1F && this.turretLRpos > -0.1F) {
						this.fGunPos -= 1.0F * f;
						this.gunStoreMoveAnim();
					} else {
						this.gunStoreTurnAnim(this.afBk, f);
					}
					if (this.fGunPos < 0.001D) {
						this.turretState = 7;
						this.setGunnerSitting(true);
						this.gunnerTurnInit(false);
					}
					break;

				case GOING_IN_M:
					this.turnPos += f;
					if (this.turnPos > 1.0F) {
						this.turretState = 0;
						this.turnPos = 1.0F;
					}
					this.gunnerTurn(this.turnPos);
					break;
			}
		}
		super.update(f);
	}

	public void missionStarting() {
		super.missionStarting();
		this.hierMesh().chunkVisible("pilot1_arm2_d0", true);
		this.hierMesh().chunkVisible("pilot1_arm1_d0", true);
	}

	public void prepareCamouflage() {
		super.prepareCamouflage();
		this.hierMesh().chunkVisible("pilot1_arm2_d0", true);
		this.hierMesh().chunkVisible("pilot1_arm1_d0", true);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) { this.getEnergyPastArmor(9.96F / (1E-005F + (float) Math.abs(Aircraft.v1.x)), shot); }
				return;
			}
			if (s.startsWith("xxcontrols")) {
				if (s.endsWith("7")) {
					if (World.Rnd().nextFloat() < 0.2F) {
						this.FM.AS.setControlsDamage(shot.initiator, 2);
						Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#7)");
					}
				} else if (s.endsWith("8")) {
					if (World.Rnd().nextFloat() < 0.2F) {
						this.FM.AS.setControlsDamage(shot.initiator, 1);
						Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#8)");
					}
					if (World.Rnd().nextFloat() < 0.2F) {
						this.FM.AS.setControlsDamage(shot.initiator, 0);
						Aircraft.debugprintln(this, "*** Arone Controls Out.. (#8)");
					}
				} else if (s.endsWith("5")) {
					if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.AS.setControlsDamage(shot.initiator, 1);
						Aircraft.debugprintln(this, "*** Evelator Controls Out.. (#5)");
					}
				} else if (s.endsWith("6")) {
					if (World.Rnd().nextFloat() < 0.5F) {
						this.FM.AS.setControlsDamage(shot.initiator, 2);
						Aircraft.debugprintln(this, "*** Rudder Controls Out.. (#6)");
					}
				} else if ((s.endsWith("2") || s.endsWith("4")) && World.Rnd().nextFloat() < 0.5F) {
					this.FM.AS.setControlsDamage(shot.initiator, 2);
					Aircraft.debugprintln(this, "*** Arone Controls Out.. (#2/#4)");
				}
				return;
			}
			if (s.startsWith("xxeng") || s.startsWith("xxEng")) {
				Aircraft.debugprintln(this, "*** Engine Module: Hit..");
				if (s.endsWith("prop")) {
					Aircraft.debugprintln(this, "*** Prop hit");
				} else if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(2.1F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 175000F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Crank Ball Bearing..");
						}
						if (World.Rnd().nextFloat() < shot.power / 50000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 2);
							Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
						}
						this.FM.EI.engines[0].setReadyness(shot.initiator, this.FM.EI.engines[0].getReadyness() - World.Rnd().nextFloat(0.0F, shot.power / 48000F));
						Aircraft.debugprintln(this, "*** Engine Module: Crank Case Hit, Readyness Reduced to " + this.FM.EI.engines[0].getReadyness() + "..");
					}
					this.getEnergyPastArmor(12.7F, shot);
				} else if (s.startsWith("xxeng1cyls")) {
					if (this.getEnergyPastArmor(World.Rnd().nextFloat(0.2F, 4.4F), shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[0].getCylindersRatio() * 1.12F) {
						this.FM.EI.engines[0].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4800F)));
						Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, " + this.FM.EI.engines[0].getCylindersOperable() + "/" + this.FM.EI.engines[0].getCylinders() + " Left..");
						if (World.Rnd().nextFloat() < shot.power / 48000F) {
							this.FM.AS.hitEngine(shot.initiator, 0, 3);
							Aircraft.debugprintln(this, "*** Engine Module: Cylinders Hit, Engine Fires..");
						}
						if (World.Rnd().nextFloat() < 0.005F) {
							this.FM.AS.setEngineStuck(shot.initiator, 0);
							Aircraft.debugprintln(this, "*** Engine Module: Bullet Jams Piston Head..");
						}
						this.getEnergyPastArmor(22.5F, shot);
					}
				} else if (s.endsWith("Oil1")) {
					this.FM.AS.hitOil(shot.initiator, 0);
					Aircraft.debugprintln(this, "*** Engine Module: Oil Radiator Hit..");
				}
				return;
			}
			if (s.startsWith("xxoil")) {
				this.FM.AS.hitOil(shot.initiator, 0);
				this.getEnergyPastArmor(0.22F, shot);
				Aircraft.debugprintln(this, "*** Engine Module: Oil Tank Pierced..");
			} else if (s.startsWith("xxtank")) {
				int i = s.charAt(6) - 49;
				if (this.getEnergyPastArmor(0.4F, shot) > 0.0F && World.Rnd().nextFloat() < 0.99F) {
					if (this.FM.AS.astateTankStates[i] == 0) {
						Aircraft.debugprintln(this, "*** Fuel Tank: Pierced..");
						this.FM.AS.hitTank(shot.initiator, i, 2);
					}
					if (shot.powerType == 3 && World.Rnd().nextFloat() < 0.25F) {
						this.FM.AS.hitTank(shot.initiator, i, 2);
						Aircraft.debugprintln(this, "*** Fuel Tank: Hit..");
					}
				}
			} else if (s.startsWith("xxlock")) {
				Aircraft.debugprintln(this, "*** Lock Construction: Hit..");
				if (s.startsWith("xxlockr") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Rudder Lock Shot Off..");
					this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
				} else if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** VatorL Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator);
				} else if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(1.5F * World.Rnd().nextFloat(1.0F, 1.2F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** VatorR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator);
				}
			} else if (s.startsWith("xxmgun")) {
				if (s.endsWith("01")) {
					Aircraft.debugprintln(this, "*** Fixed Gun #1: Disabled..");
					this.FM.AS.setJamBullets(0, 0);
				}
				if (s.endsWith("02")) {
					Aircraft.debugprintln(this, "*** Fixed Gun #2: Disabled..");
					this.FM.AS.setJamBullets(0, 1);
				}
				this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
			} else if (s.startsWith("xxspar")) {
				Aircraft.debugprintln(this, "*** Spar Construction: Hit..");
				if (s.startsWith("xxspart") && this.chunkDamageVisible("Tail1") > 2 && this.getEnergyPastArmor(9.5F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** Tail1 Spars Broken in Half..");
					this.nextDMGLevels(1, 2, "Tail1_D3", shot.initiator);
				} else if (s.startsWith("xxsparli") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLIn_D" + this.chunkDamageVisible("WingLIn"), shot.initiator);
				} else if (s.startsWith("xxspar2i") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRIn Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRIn_D" + this.chunkDamageVisible("WingRIn"), shot.initiator);
				} else if (s.startsWith("xxsparlm") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLMid_D" + this.chunkDamageVisible("WingLMid"), shot.initiator);
				} else if (s.startsWith("xxsparrm") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingRMid Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingRMid_D" + this.chunkDamageVisible("WingRMid"), shot.initiator);
				} else if (s.startsWith("xxsparlo") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingLOut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingLOut_D" + this.chunkDamageVisible("WingLOut"), shot.initiator);
				} else if (s.startsWith("xxsparro") && World.Rnd().nextFloat() < 0.25F && this.getEnergyPastArmor(9.5F * World.Rnd().nextFloat(1.0F, 3F), shot) > 0.0F) {
					Aircraft.debugprintln(this, "*** WingROut Spars Damaged..");
					this.nextDMGLevels(1, 2, "WingROut_D" + this.chunkDamageVisible("WingROut"), shot.initiator);
				}
			}
			return;
		}
		if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int j;
			if (s.endsWith("a")) {
				byte0 = 1;
				j = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				j = s.charAt(6) - 49;
			} else {
				j = s.charAt(5) - 49;
			}
			Aircraft.debugprintln(this, "*** hitFlesh..");
			this.hitFlesh(j, shot, byte0);
		} else if (s.startsWith("xcf") || s.startsWith("xcockpit")) {
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
		} else if (s.startsWith("xturret1b")) {
			Aircraft.debugprintln(this, "*** Turret Gun: Disabled.. (xturret1b)");
			this.FM.AS.setJamBullets(10, 0);
			this.getEnergyPastArmor(World.Rnd().nextFloat(0.0F, 28.33F), shot);
		} else if (s.startsWith("xeng")) {
			if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
		} else if (s.startsWith("xtail")) {
			if (this.chunkDamageVisible("Tail1") < 3) { this.hitChunk("Tail1", shot); }
		} else if (s.startsWith("xkeel")) {
			if (this.chunkDamageVisible("Keel1") < 2) { this.hitChunk("Keel1", shot); }
		} else if (s.startsWith("xrudder")) {
			if (this.chunkDamageVisible("Rudder1") < 1) { this.hitChunk("Rudder1", shot); }
		} else if (s.startsWith("xstab")) {
			if (s.startsWith("xstabl") && this.chunkDamageVisible("StabL") < 2) { this.hitChunk("StabL", shot); }
			if (s.startsWith("xstabr") && this.chunkDamageVisible("StabR") < 2) { this.hitChunk("StabR", shot); }
		} else if (s.startsWith("xvator")) {
			if (s.startsWith("xvatorl") && this.chunkDamageVisible("VatorL") < 1) { this.hitChunk("VatorL", shot); }
			if (s.startsWith("xvatorr") && this.chunkDamageVisible("VatorR") < 1) { this.hitChunk("VatorR", shot); }
		} else if (s.startsWith("xWing")) {
			Aircraft.debugprintln(this, "*** xWing: " + s);
			if (s.startsWith("xWingLIn") && this.chunkDamageVisible("WingLIn") < 3) { this.hitChunk("WingLIn", shot); }
			if (s.startsWith("xWingRIn") && this.chunkDamageVisible("WingRIn") < 3) { this.hitChunk("WingRIn", shot); }
			if (s.startsWith("xWingLmid") && this.chunkDamageVisible("WingLMid") < 3) { this.hitChunk("WingLMid", shot); }
			if (s.startsWith("xWingRmid") && this.chunkDamageVisible("WingRMid") < 3) { this.hitChunk("WingRMid", shot); }
		} else if (s.startsWith("xwing")) {
			Aircraft.debugprintln(this, "*** xwing: " + s);
			if (s.startsWith("xwinglout") && this.chunkDamageVisible("WingLOut") < 3) { this.hitChunk("WingLOut", shot); }
			if (s.startsWith("xwingrout") && this.chunkDamageVisible("WingROut") < 3) { this.hitChunk("WingROut", shot); }
		} else if (s.startsWith("xarone")) {
			if (s.startsWith("xaronel") && this.chunkDamageVisible("AroneL") < 1) {
				this.hitChunk("AroneL1", shot);
				this.hitChunk("AroneL2", shot);
			}
			if (s.startsWith("xaroner") && this.chunkDamageVisible("AroneR") < 1) {
				this.hitChunk("AroneR1", shot);
				this.hitChunk("AroneR2", shot);
			}
		}
	}

	float                    lookoutTimeLeft;
	float                    lookoutAz;
	float                    lookoutEl;
	float                    lookoutAnim;
	float                    lookoutMax;
	float                    lookoutAzSpd;
	float                    lookoutElSpd;
	int                      lookoutIndex;
	float                    lookoutPos[][];
	private float            turnPos;
	private float            lookout;
	float                    turretLRpos;
	float                    turretFront;
	float                    turretLRposW;
	float                    turretPos[];
	float                    turretAng[];
	float                    tu[];
	float                    afBk[];
	protected float          kangle;
	private int              turretState;
	private static final int SLEEPING       = 0;
	private static final int GOING_OUT_M    = 1;
	private static final int GOING_OUT_PULL = 2;
	private static final int GOING_OUT_T    = 3;
	private static final int ACTIVE         = 4;
	private static final int GOING_IN_T     = 5;
	private static final int GOING_IN_PULL  = 6;
	private static final int GOING_IN_M     = 7;
	private float            fGunPos;
	private boolean          finishedTurn;
	private long             btme;
	private static float     MAX_DANGLE     = 100F;
	protected int            gunOutOverride;
	boolean                  bGunnerKilled;

	static {
		Class class1 = Letov.class;
		Property.set(class1, "originCountry", PaintScheme.countrySlovakia);
	}
}
