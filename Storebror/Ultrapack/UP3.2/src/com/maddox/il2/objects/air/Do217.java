package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public abstract class Do217 extends Scheme2 implements TypeBomber, TypeTransport {

	public Do217() {
		this.bSightAutomation = false;
		this.bSightBombDump = false;
		this.fSightCurDistance = 0.0F;
		this.fSightCurAltitude = 300F;
		this.fSightCurSpeed = 50F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightSetForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurReadyness = 0.0F;
		this.calibDistance = 0.0F;
		this.bPitUnfocused = true;
		this.wheel1 = 0.0F;
		this.wheel2 = 0.0F;
	}

	public void onAircraftLoaded() {
		super.onAircraftLoaded();
		this.FM.Gears.computePlaneLandPose(this.FM);
	}

	public boolean typeBomberToggleAutomation() {
		this.bSightAutomation = !this.bSightAutomation;
		this.bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
		return this.bSightAutomation;
	}

	public void typeBomberAdjDistanceReset() {
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus() {
		this.fSightCurForwardAngle++;
		if (this.fSightCurForwardAngle > 85F) { this.fSightCurForwardAngle = 85F; }
		this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation) { this.typeBomberToggleAutomation(); }
	}

	public void typeBomberAdjDistanceMinus() {
		this.fSightCurForwardAngle--;
		if (this.fSightCurForwardAngle < 0.0F) { this.fSightCurForwardAngle = 0.0F; }
		this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { new Integer((int) this.fSightCurForwardAngle) });
		if (this.bSightAutomation) { this.typeBomberToggleAutomation(); }
	}

	public void typeBomberAdjSideslipReset() {
		this.fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus() {
		this.fSightCurSideslip += 0.05F;
		if (this.fSightCurSideslip > 3F) { this.fSightCurSideslip = 3F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjSideslipMinus() {
		this.fSightCurSideslip -= 0.05F;
		if (this.fSightCurSideslip < -3F) { this.fSightCurSideslip = -3F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { new Float(this.fSightCurSideslip * 10F) });
	}

	public void typeBomberAdjAltitudeReset() {
		this.fSightCurAltitude = 850F;
	}

	public void typeBomberAdjAltitudePlus() {
		this.fSightCurAltitude += 10F;
		if (this.fSightCurAltitude > 10000F) { this.fSightCurAltitude = 10000F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus() {
		this.fSightCurAltitude -= 10F;
		if (this.fSightCurAltitude < 850F) { this.fSightCurAltitude = 850F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { new Integer((int) this.fSightCurAltitude) });
		this.fSightCurDistance = this.fSightCurAltitude * (float) Math.tan(Math.toRadians(this.fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset() {
		this.fSightCurSpeed = 150F;
	}

	public void typeBomberAdjSpeedPlus() {
		this.fSightCurSpeed += 10F;
		if (this.fSightCurSpeed > 600F) { this.fSightCurSpeed = 600F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus() {
		this.fSightCurSpeed -= 10F;
		if (this.fSightCurSpeed < 150F) { this.fSightCurSpeed = 150F; }
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { new Integer((int) this.fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f) {
		if (Math.abs(this.FM.Or.getKren()) > 4.5D) {
			this.fSightCurReadyness -= 0.0666666F * f;
			if (this.fSightCurReadyness < 0.0F) { this.fSightCurReadyness = 0.0F; }
		}
		if (this.fSightCurReadyness < 1.0F) {
			this.fSightCurReadyness += 0.0333333F * f;
		} else if (this.bSightAutomation) {
			this.fSightCurDistance -= this.fSightCurSpeed / 3.6F * f;
			if (this.fSightCurDistance < 0.0F) {
				this.fSightCurDistance = 0.0F;
				this.typeBomberToggleAutomation();
			}
			this.fSightCurForwardAngle = (float) Math.toDegrees(Math.atan(this.fSightCurDistance / this.fSightCurAltitude));
			this.calibDistance = this.fSightCurSpeed / 3.6F * floatindex(Aircraft.cvt(this.fSightCurAltitude, 0.0F, 7000F, 0.0F, 7F), calibrationScale);
			if (this.fSightCurDistance < this.calibDistance + this.fSightCurSpeed / 3.6F * Math.sqrt(this.fSightCurAltitude * (2F / 9.81F))) { this.bSightBombDump = true; }
			if (this.bSightBombDump) {
				if (this.FM.isTick(3, 0)) {
					if (this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1] != null && this.FM.CT.Weapons[3][this.FM.CT.Weapons[3].length - 1].haveBullets()) {
						this.FM.CT.WeaponControl[3] = true;
						HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
					}
				} else {
					this.FM.CT.WeaponControl[3] = false;
				}
			}
		}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted) throws IOException {
		netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(this.fSightCurDistance);
		netmsgguaranted.writeByte((int) this.fSightCurForwardAngle);
		netmsgguaranted.writeByte((int) ((this.fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeByte((int) (this.fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int) (this.fSightCurReadyness * 200F));
	}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput) throws IOException {
		int i = netmsginput.readUnsignedByte();
		this.bSightAutomation = (i & 1) != 0;
		this.bSightBombDump = (i & 2) != 0;
		this.fSightCurDistance = netmsginput.readFloat();
		this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
		this.fSightCurSideslip = -3F + netmsginput.readUnsignedByte() / 33.33333F;
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = netmsginput.readUnsignedByte() * 2.5F;
		this.fSightCurReadyness = netmsginput.readUnsignedByte() / 200F;
	}

	public void rareAction(float f, boolean flag) {
		super.rareAction(f, flag);
		if (flag) {
			if (this.FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.39F) { this.FM.AS.hitTank(this, 0, 1); }
			if (this.FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.39F) { this.FM.AS.hitTank(this, 1, 1); }
			if (this.FM.AS.astateEngineStates[2] > 3 && World.Rnd().nextFloat() < 0.39F) { this.FM.AS.hitTank(this, 2, 1); }
			if (this.FM.AS.astateTankStates[0] > 4 && World.Rnd().nextFloat() < 0.1F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[0] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[1] > 4 && World.Rnd().nextFloat() < 0.1F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[1] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[2] > 4 && World.Rnd().nextFloat() < 0.1F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[2] + "0", 0, this); }
			if (this.FM.AS.astateTankStates[3] > 4 && World.Rnd().nextFloat() < 0.1F) { this.nextDMGLevel(this.FM.AS.astateEffectChunks[3] + "0", 0, this); }
		}
		for (int i = 1; i <= 4; i++) {
			if (this.FM.getAltitude() < 3000F) {
				this.hierMesh().chunkVisible("hmask" + i + "_d0", false);
			} else {
				this.hierMesh().chunkVisible("hmask" + i + "_d0", this.hierMesh().isChunkVisible("Pilot" + i + "_D0"));
			}
		}

	}

	public void doWoundPilot(int i, float f) {
		switch (i) {
			case 1:
				this.FM.turret[0].setHealth(f);
				this.FM.turret[3].setHealth(f);
				this.FM.turret[4].setHealth(f);
				break;

			case 2:
				this.FM.turret[1].setHealth(f);
				break;

			case 3:
				this.FM.turret[2].setHealth(f);
				break;
		}
	}

	public void doMurderPilot(int i) {
		switch (i) {
			case 0:
				this.hierMesh().chunkVisible("Pilot1_D0", false);
				this.hierMesh().chunkVisible("hmask1_d0", false);
				this.hierMesh().chunkVisible("Head1_D0", false);
				this.hierMesh().chunkVisible("Pilot1_D1", true);
				break;

			case 1:
				this.hierMesh().chunkVisible("Pilot2_D0", false);
				this.hierMesh().chunkVisible("hmask2_d0", false);
				this.hierMesh().chunkVisible("Pilot2_D1", true);
				break;

			case 2:
				this.hierMesh().chunkVisible("Pilot3_D0", false);
				this.hierMesh().chunkVisible("hmask3_d0", false);
				this.hierMesh().chunkVisible("Pilot3_D1", true);
				break;

			case 3:
				this.hierMesh().chunkVisible("Pilot4_D0", false);
				this.hierMesh().chunkVisible("hmask4_d0", false);
				this.hierMesh().chunkVisible("Pilot4_D1", true);
				break;
		}
	}

	protected boolean cutFM(int i, int j, Actor actor) {
		switch (i) {
			default:
				break;

			case 33:
				this.hitProp(0, j, actor);
				break;

			case 36:
				this.hitProp(1, j, actor);
				break;

			case 35:
				this.FM.AS.hitEngine(this, 0, 3);
				if (World.Rnd().nextInt(0, 99) < 66) { this.FM.AS.hitEngine(this, 0, 1); }
				break;

			case 38:
				this.FM.AS.hitEngine(this, 1, 3);
				if (World.Rnd().nextInt(0, 99) < 66) { this.FM.AS.hitEngine(this, 1, 1); }
				break;

			case 11:
				this.hierMesh().chunkVisible("Wire1_D0", false);
				break;

			case 19:
				this.hierMesh().chunkVisible("Wire1_D0", false);
				this.FM.Gears.hitCentreGear();
				break;

			case 13:
				this.killPilot(this, 0);
				this.killPilot(this, 1);
				this.killPilot(this, 2);
				this.killPilot(this, 3);
				return false;
		}
		return super.cutFM(i, j, actor);
	}

	public void update(float f) {
		super.update(f);
	}

	protected void moveFlap(float f) {
		float f1 = -50F * f;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f1, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f1, 0.0F);
	}

	protected void moveRudder(float f) {
		this.hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
		this.hierMesh().chunkSetAngles("Rudder2_D0", 0.0F, -30F * f, 0.0F);
	}

	public void moveSteering(float f) {
		this.hierMesh().chunkSetAngles("GearC3_D0", 0.0F, Aircraft.cvt(f, -65F, 65F, 65F, -65F), 0.0F);
	}

	public boolean turretAngles(int i, float af[]) {
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i) {
			default:
				break;

			case 0:
				if (f1 > 45F) {
					f1 = 45F;
					flag = false;
				}
				if (f1 < -40F) {
					f1 = -40F;
					flag = false;
				}
				if (f > 50F) {
					f = 50F;
					flag = false;
				}
				if (f < -25F) {
					f = -25F;
					flag = false;
				}
				break;

			case 1:
				if (f1 > 80F) {
					f1 = 80F;
					flag = false;
				}
				if (f1 < -3F) {
					f1 = -3F;
					flag = false;
				}
				if (f > 155F || f < -155F) {
					if (f1 < 40F) {
						f1 = 40F;
						flag = false;
					}
					break;
				}
				if (f > 135F && f1 < (f - 135F) * 40F / 20F) {
					f1 = (f - 135F) * 40F / 20F;
					flag = false;
				}
				if (f < -135F && f1 < (-f - 135F) * 40F / 20F) {
					f1 = (-f - 135F) * 40F / 20F;
					flag = false;
				}
				if ((f > 110F || f < -110F) && f1 < 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				break;

			case 2:
				if (f1 > 45F) {
					f1 = 45F;
					flag = false;
				}
				if (f1 < -40F) {
					f1 = -40F;
					flag = false;
				}
				if (f > 50F) {
					f = 50F;
					flag = false;
				}
				if (f < -50F) {
					f = -50F;
					flag = false;
				}
				break;

			case 3:
				if (f1 > 35F) {
					f1 = 35F;
					flag = false;
				}
				if (f1 < 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				if (f > 65F) {
					f = 65F;
					flag = false;
				}
				if (f < -30F) {
					f = -30F;
					flag = false;
				}
				break;

			case 4:
				if (f1 > 35F) {
					f1 = 35F;
					flag = false;
				}
				if (f1 < 0.0F) {
					f1 = 0.0F;
					flag = false;
				}
				if (f > 30F) {
					f = 30F;
					flag = false;
				}
				if (f < -65F) {
					f = -65F;
					flag = false;
				}
				break;
		}
		af[0] = -f;
		af[1] = f1;
		return flag;
	}

	public void moveWheelSink() {
		this.wheel1 = 0.8F * this.wheel1 + 0.2F * this.FM.Gears.gWheelSinking[0];
		this.wheel2 = 0.8F * this.wheel2 + 0.2F * this.FM.Gears.gWheelSinking[1];
		this.resetYPRmodifier();
		Aircraft.xyz[1] = -Aircraft.cvt(this.wheel1, 0.0F, 0.3F, 0.0F, 0.3F);
		this.hierMesh().chunkSetLocate("GearL3_D0", Aircraft.xyz, Aircraft.ypr);
		Aircraft.xyz[1] = -Aircraft.cvt(this.wheel2, 0.0F, 0.3F, 0.0F, 0.3F);
		this.hierMesh().chunkSetLocate("GearR3_D0", Aircraft.xyz, Aircraft.ypr);
	}

	protected void mydebuggunnery(String s) {
		System.out.println(s);
	}

	protected void setControlDamage(Shot shot, int i) {
		if (World.Rnd().nextFloat() < 0.002F && this.getEnergyPastArmor(4F, shot) > 0.0F) { this.FM.AS.setControlsDamage(shot.initiator, i); }
	}

	protected void hitChunk(String s, Shot shot) {
		super.hitChunk(s, shot);
	}

	protected void hitBone(String s, Shot shot, Point3d point3d) {
		if (s.startsWith("xx")) {
			if (s.startsWith("xxarmor")) {
				if (s.endsWith("p1")) {
					if (Aircraft.v1.z > 0.5D) {
						this.getEnergyPastArmor(4D / Aircraft.v1.z, shot);
					} else if (Aircraft.v1.x > 0.93969261646270752D) {
						this.getEnergyPastArmor(8D / Aircraft.v1.x * World.Rnd().nextFloat(1.0F, 1.2F), shot);
					} else {
						this.getEnergyPastArmor(3F, shot);
					}
				} else if (s.endsWith("p2")) {
					this.getEnergyPastArmor(4D / Math.abs(Aircraft.v1.z), shot);
				} else if (s.endsWith("p3")) {
					this.getEnergyPastArmor(7D / Math.abs(Aircraft.v1.x) * World.Rnd().nextFloat(1.0F, 1.2F), shot);
				} else {
					this.getEnergyPastArmor(3F, shot);
				}
			}
			if (s.endsWith("p4")) {
				if (Aircraft.v1.x > 0.70710676908493042D) {
					this.getEnergyPastArmor(7D / Aircraft.v1.x * World.Rnd().nextFloat(1.0F, 1.2F), shot);
				} else if (Aircraft.v1.x > -0.70710676908493042D) {
					this.getEnergyPastArmor(5F, shot);
				} else {
					this.getEnergyPastArmor(3F, shot);
				}
			} else if (s.endsWith("a1") || s.endsWith("a2")) { this.getEnergyPastArmor(3D, shot); }
			if (s.startsWith("xxarmturr")) { this.getEnergyPastArmor(3F, shot); }
			if (s.startsWith("xxspar")) {
				this.getEnergyPastArmor(3F, shot);
				if ((s.endsWith("cf1") || s.endsWith("cf2")) && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("CF") > 1
						&& this.getEnergyPastArmor(15.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					this.msgCollision(this, "Tail1_D0", "Tail1_D0");
					this.msgCollision(this, "WingLIn_D0", "WingLIn_D0");
					this.msgCollision(this, "WingRIn_D0", "WingRIn_D0");
				}
				if ((s.endsWith("t1") || s.endsWith("t2")) && World.Rnd().nextFloat() < 0.1F && this.chunkDamageVisible("Tail1") > 1
						&& this.getEnergyPastArmor(15.9F / (float) Math.sqrt(Aircraft.v1.y * Aircraft.v1.y + Aircraft.v1.z * Aircraft.v1.z), shot) > 0.0F) {
					this.msgCollision(this, "Tail1_D0", "Tail1_D0");
				}
				if ((s.endsWith("li1") || s.endsWith("li2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLIn") > 1
						&& this.getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "WingLIn_D2", shot.initiator);
				}
				if ((s.endsWith("ri1") || s.endsWith("ri2")) && World.Rnd().nextFloat() < 1.0D - 0.92D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingRIn") > 1
						&& this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "WingRIn_D2", shot.initiator);
				}
				if ((s.endsWith("lm1") || s.endsWith("lm2")) && World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLMid") > 1
						&& this.getEnergyPastArmor(10.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "WingLMid_D2", shot.initiator);
				}
				if ((s.endsWith("rm1") || s.endsWith("rm2")) && World.Rnd().nextFloat() < 1.0D - 0.86D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingRMid") > 1
						&& this.getEnergyPastArmor(13.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "WingRMid_D2", shot.initiator);
				}
				if ((s.endsWith("lo1") || s.endsWith("lo2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingLOut") > 1
						&& this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "WingLOut_D2", shot.initiator);
				}
				if ((s.endsWith("ro1") || s.endsWith("ro2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingROut") > 1
						&& this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "WingROut_D2", shot.initiator);
				}
				if (s.endsWith("e1") && (point3d.y > 2.79D || point3d.y < 2.32D) && this.getEnergyPastArmor(17F, shot) > 0.0F) { this.nextDMGLevels(3, 2, "Engine1_D0", shot.initiator); }
				if (s.endsWith("e2") && (point3d.y < -2.79D || point3d.y > -2.32D) && this.getEnergyPastArmor(17F, shot) > 0.0F) { this.nextDMGLevels(3, 2, "Engine2_D0", shot.initiator); }
				if (s.endsWith("e3") && (point3d.y < -2.79D || point3d.y > -2.32D) && this.getEnergyPastArmor(17F, shot) > 0.0F) { this.nextDMGLevels(3, 2, "Engine3_D0", shot.initiator); }
				if ((s.endsWith("k1") || s.endsWith("k2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingROut") > 1 && this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "Keel1_D0", shot.initiator);
				}
				if ((s.endsWith("sr1") || s.endsWith("sr2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingROut") > 1
						&& this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "StabR_D0", shot.initiator);
				}
				if ((s.endsWith("sl1") || s.endsWith("sl2")) && World.Rnd().nextFloat() < 1.0D - 0.79D * Math.abs(Aircraft.v1.x) && this.chunkDamageVisible("WingROut") > 1
						&& this.getEnergyPastArmor(8.5F * World.Rnd().nextFloat(1.0F, 2.0F), shot) > 0.0F) {
					this.nextDMGLevels(1, 2, "StabL_D0", shot.initiator);
				}
			}
			if (s.startsWith("xxbomb") && World.Rnd().nextFloat() < 0.01F && this.FM.CT.Weapons[3] != null && this.FM.CT.Weapons[3][0].haveBullets()) {
				this.FM.AS.hitTank(shot.initiator, 0, 100);
				this.FM.AS.hitTank(shot.initiator, 1, 100);
				this.FM.AS.hitTank(shot.initiator, 2, 100);
				this.FM.AS.hitTank(shot.initiator, 3, 100);
				this.msgCollision(this, "CF_D0", "CF_D0");
			}
			if (s.startsWith("xxeng")) {
				int i = 0;
				if (s.startsWith("xxeng2")) { i = 1; }
				if (s.endsWith("prop")) {
					int k = i;
					if (this.getEnergyPastArmor(2.0F, shot) > 0.0F && World.Rnd().nextFloat() < 0.35F) { this.FM.AS.setEngineSpecificDamage(shot.initiator, k, 3); }
				}
				if (s.endsWith("case")) {
					if (this.getEnergyPastArmor(0.2F, shot) > 0.0F) {
						if (World.Rnd().nextFloat() < shot.power / 190000F) { this.FM.AS.setEngineStuck(shot.initiator, i); }
						if (World.Rnd().nextFloat() < shot.power / 48000F) { this.FM.AS.hitEngine(shot.initiator, i, 2); }
					}
				} else if (s.endsWith("cyls")) {
					if (this.getEnergyPastArmor(1.6F, shot) > 0.0F && World.Rnd().nextFloat() < this.FM.EI.engines[i].getCylindersRatio() * 1.4F) {
						this.FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, (int) (shot.power / 4000F)));
						if (this.FM.AS.astateEngineStates[i] < 1) {
							this.FM.AS.hitEngine(shot.initiator, i, 1);
							this.FM.AS.doSetEngineState(shot.initiator, i, 1);
						}
						if (World.Rnd().nextFloat() < shot.power / 900000F) { this.FM.AS.hitEngine(shot.initiator, i, 3); }
						this.getEnergyPastArmor(25F, shot);
					}
				} else if (s.endsWith("supc") && this.getEnergyPastArmor(0.05F, shot) > 0.0F && World.Rnd().nextFloat() < 0.89F) { this.FM.AS.setEngineSpecificDamage(shot.initiator, i, 0); }
				if (this.getEnergyPastArmor(0.42F, shot) > 0.0F && (s.endsWith("oil1") || s.endsWith("oil2") || s.endsWith("oil3"))) { this.FM.AS.hitOil(shot.initiator, i); }
			}
			if (s.startsWith("xxtank")) {
				int j = s.charAt(6) - 49;
				if (j < 4 && this.getEnergyPastArmor(1.8F, shot) > 0.0F) {
					if (shot.power < 14100F) {
						if (this.FM.AS.astateTankStates[j] < 1) { this.FM.AS.hitTank(shot.initiator, j, 1); }
						if (this.FM.AS.astateTankStates[j] < 4 && World.Rnd().nextFloat() < 0.12F) { this.FM.AS.hitTank(shot.initiator, j, 1); }
						if (shot.powerType == 3 && this.FM.AS.astateTankStates[j] > 1 && World.Rnd().nextFloat() < 0.1F) { this.FM.AS.hitTank(shot.initiator, j, 10); }
					} else {
						this.FM.AS.hitTank(shot.initiator, j, World.Rnd().nextInt(0, (int) (shot.power / 35000F)));
					}
				}
			}
			if (s.startsWith("xxlock")) {
				if (s.startsWith("xxlockr") && (s.startsWith("xxlockr1") || s.startsWith("xxlockr2")) && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.nextDMGLevels(3, 2, "Rudder1_D" + this.chunkDamageVisible("Rudder1"), shot.initiator);
				}
				if (s.startsWith("xxlockvl") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) { this.nextDMGLevels(3, 2, "VatorL_D" + this.chunkDamageVisible("VatorL"), shot.initiator); }
				if (s.startsWith("xxlockvr") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) { this.nextDMGLevels(3, 2, "VatorR_D" + this.chunkDamageVisible("VatorR"), shot.initiator); }
				if (s.startsWith("xxlockal") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) { this.nextDMGLevels(3, 2, "AroneL_D" + this.chunkDamageVisible("AroneL"), shot.initiator); }
				if (s.startsWith("xxlockar") && this.getEnergyPastArmor(5.5F * World.Rnd().nextFloat(1.0F, 1.5F), shot) > 0.0F) {
					this.debuggunnery("Lock Construction: AroneR Lock Shot Off..");
					this.nextDMGLevels(3, 2, "AroneR_D" + this.chunkDamageVisible("AroneR"), shot.initiator);
				}
			}
		}
		if (s.startsWith("xxmgun")) {
			if (s.endsWith("1")) {
				if (this.getEnergyPastArmor(5F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.setJamBullets(10, 0);
					this.getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.endsWith("2")) {
				if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.setJamBullets(11, 2);
					this.getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.endsWith("3")) {
				if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.setJamBullets(12, 1);
					this.getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.endsWith("4")) {
				if (this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
					this.FM.AS.setJamBullets(13, 4);
					this.getEnergyPastArmor(11.98F, shot);
				}
			} else if (s.endsWith("5") && this.getEnergyPastArmor(4.85F, shot) > 0.0F && World.Rnd().nextFloat() < 0.75F) {
				this.FM.AS.setJamBullets(14, 3);
				this.getEnergyPastArmor(11.98F, shot);
			}
		}
		if (s.startsWith("xcf")) {
			this.setControlDamage(shot, 0);
			this.setControlDamage(shot, 1);
			this.setControlDamage(shot, 2);
			if (this.chunkDamageVisible("CF") < 3) { this.hitChunk("CF", shot); }
			this.getEnergyPastArmor(4F, shot);
		} else if (s.startsWith("xnose")) {
			if (this.chunkDamageVisible("Nose") < 3) { this.hitChunk("Nose", shot); }
			if (shot.power > 200000F) {
				this.FM.AS.hitPilot(shot.initiator, 0, World.Rnd().nextInt(3, 192));
				this.FM.AS.hitPilot(shot.initiator, 1, World.Rnd().nextInt(3, 192));
				this.FM.AS.hitPilot(shot.initiator, 2, World.Rnd().nextInt(3, 192));
				this.FM.AS.hitPilot(shot.initiator, 3, World.Rnd().nextInt(3, 192));
			}
			if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x40); }
			if (point3d.x > 4.505000114440918D) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 1);
			} else if (point3d.y > 0.0D) {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 4);
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 8); }
			} else {
				this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10);
				if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
			}
		} else if (s.startsWith("xtail")) {
			this.setControlDamage(shot, 1);
			this.setControlDamage(shot, 2);
			if (this.chunkDamageVisible("Tail1") < 2) { this.hitChunk("Tail1", shot); }
			if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x10); }
			if (World.Rnd().nextFloat() < 0.1F) { this.FM.AS.setCockpitState(shot.initiator, this.FM.AS.astateCockpitState | 0x20); }
		} else if (s.startsWith("xkeel1")) {
			this.setControlDamage(shot, 2);
			if (this.chunkDamageVisible("Keel1") < 2) { this.hitChunk("Keel1", shot); }
		} else if (s.startsWith("xkeel2")) {
			this.setControlDamage(shot, 2);
			if (this.chunkDamageVisible("Keel2") < 2) { this.hitChunk("Keel2", shot); }
		} else if (s.startsWith("xrudder1")) {
			this.setControlDamage(shot, 2);
			this.hitChunk("Rudder1", shot);
		} else if (s.startsWith("xrudder2")) {
			this.setControlDamage(shot, 2);
			this.hitChunk("Rudder2", shot);
		} else if (s.startsWith("xstabl")) {
			this.hitChunk("StabL", shot);
		} else if (s.startsWith("xstabr")) {
			this.hitChunk("StabR", shot);
		} else if (s.startsWith("xvatorl")) {
			this.hitChunk("VatorL", shot);
		} else if (s.startsWith("xvatorr")) {
			this.hitChunk("VatorR", shot);
		} else if (s.startsWith("xwinglin")) {
			this.setControlDamage(shot, 0);
			if (this.chunkDamageVisible("WingLIn") < 2) { this.hitChunk("WingLIn", shot); }
		} else if (s.startsWith("xwingrin")) {
			this.setControlDamage(shot, 0);
			if (this.chunkDamageVisible("WingRIn") < 2) { this.hitChunk("WingRIn", shot); }
		} else if (s.startsWith("xwinglmid")) {
			this.setControlDamage(shot, 0);
			if (this.chunkDamageVisible("WingLMid") < 2) { this.hitChunk("WingLMid", shot); }
		} else if (s.startsWith("xwingrmid")) {
			this.setControlDamage(shot, 0);
			if (this.chunkDamageVisible("WingRMid") < 2) { this.hitChunk("WingRMid", shot); }
		} else if (s.startsWith("xwinglout")) {
			if (this.chunkDamageVisible("WingLOut") < 2) { this.hitChunk("WingLOut", shot); }
		} else if (s.startsWith("xwingrout")) {
			if (this.chunkDamageVisible("WingROut") < 2) { this.hitChunk("WingROut", shot); }
		} else if (s.startsWith("xaronel")) {
			this.hitChunk("AroneL", shot);
		} else if (s.startsWith("xaroner")) {
			this.hitChunk("AroneR", shot);
		} else if (s.startsWith("xflap01")) {
			this.hitChunk("Flap01", shot);
		} else if (s.startsWith("xflap02")) {
			this.hitChunk("Flap02", shot);
		} else if (s.startsWith("xengine1")) {
			if (this.chunkDamageVisible("Engine1") < 2) { this.hitChunk("Engine1", shot); }
		} else if (s.startsWith("xengine2")) {
			if (this.chunkDamageVisible("Engine2") < 2) { this.hitChunk("Engine2", shot); }
		} else if (s.startsWith("xgear")) {
			if (World.Rnd().nextFloat() < 0.1F) { this.FM.Gears.setHydroOperable(false); }
		} else if (s.startsWith("xturret")) {
			if (s.startsWith("xturret1")) { this.FM.AS.setJamBullets(10, 0); }
			if (s.startsWith("xturret2")) { this.FM.AS.setJamBullets(11, 0); }
			if (s.startsWith("xturret3")) { this.FM.AS.setJamBullets(12, 0); }
			if (s.startsWith("xturret4")) { this.FM.AS.setJamBullets(13, 0); }
			if (s.startsWith("xturret5")) { this.FM.AS.setJamBullets(14, 0); }
		} else if (s.startsWith("xpilot") || s.startsWith("xhead")) {
			byte byte0 = 0;
			int l;
			if (s.endsWith("a")) {
				byte0 = 1;
				l = s.charAt(6) - 49;
			} else if (s.endsWith("b")) {
				byte0 = 2;
				l = s.charAt(6) - 49;
			} else {
				l = s.charAt(5) - 49;
			}
			this.hitFlesh(l, shot, byte0);
		}
	}

	public static void moveGear(HierMesh hiermesh, float f) {
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, -107F), 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, -107F), 0.0F);
		hiermesh.chunkSetAngles("GearL4_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, -19F), 0.0F);
		hiermesh.chunkSetAngles("GearR4_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, -19F), 0.0F);
		hiermesh.chunkSetAngles("GearL5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, 60F), 0.0F);
		hiermesh.chunkSetAngles("GearR5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 1.0F, 0.0F, 60F), 0.0F);
		hiermesh.chunkSetAngles("GearR7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, -65.5F), 0.0F);
		hiermesh.chunkSetAngles("GearL7_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, -65.5F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, 65.5F), 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Aircraft.cvt(f, 0.01F, 0.2F, 0.0F, 65.5F), 0.0F);
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.3F, 0.0F, -60F), 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, 75F), 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, Aircraft.cvt(f, 0.1F, 0.2F, 0.0F, 75F), 0.0F);
	}

	protected void moveGear(float f) {
		moveGear(this.hierMesh(), f);
	}

	public boolean     bSightAutomation;
	private boolean    bSightBombDump;
	public float       fSightCurDistance;
	public float       fSightCurAltitude;
	public float       fSightCurSpeed;
	public float       fSightCurForwardAngle;
	public float       fSightSetForwardAngle;
	public float       fSightCurSideslip;
	public float       fSightCurReadyness;
	private float      calibDistance;
	public boolean     bPitUnfocused;
	private float      wheel1;
	private float      wheel2;
	static final float calibrationScale[] = { 0.0F, 0.2F, 0.4F, 0.66F, 0.86F, 1.05F, 1.2F, 1.6F };

	static {
		Class class1 = Do217.class;
		Property.set(class1, "originCountry", PaintScheme.countryGermany);
	}
}
