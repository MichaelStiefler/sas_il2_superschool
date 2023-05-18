package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.air.Maneuver;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.Wreckage;
import com.maddox.rts.MsgAction;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;

public class Canberra extends Scheme2
implements TypeBomber
{
	private boolean starterTriggered[] = {false, false};

	public Canberra()
	{
		bSightAutomation = false;
		bSightBombDump = false;
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
		fSightCurSideslip = 0.0F;
		fSightCurAltitude = 3000F;
		fSightCurSpeed = 200F;
		fSightCurReadyness = 0.0F;
	}

	private static final float toMeters(float f)
	{
		return 0.3048F * f;
	}

	private static final float toMetersPerSecond(float f)
	{
		return 0.4470401F * f;
	}

	public boolean typeBomberToggleAutomation()
	{
		bSightAutomation = !bSightAutomation;
		bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (bSightAutomation ? "ON" : "OFF"));
		return bSightAutomation;
	}

	public void typeBomberAdjDistanceReset()
	{
		fSightCurDistance = 0.0F;
		fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus()
	{
		fSightCurForwardAngle++;
		if(fSightCurForwardAngle > 85F)
			fSightCurForwardAngle = 85F;
		fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
				new Integer((int)fSightCurForwardAngle)
		});
		if(bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus()
	{
		fSightCurForwardAngle--;
		if(fSightCurForwardAngle < 0.0F)
			fSightCurForwardAngle = 0.0F;
		fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] {
				new Integer((int)fSightCurForwardAngle)
		});
		if(bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipReset()
	{
		fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus()
	{
		fSightCurSideslip += 0.1F;
		if(fSightCurSideslip > 3F)
			fSightCurSideslip = 3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
				new Integer((int)(fSightCurSideslip * 10F))
		});
	}

	public void typeBomberAdjSideslipMinus()
	{
		fSightCurSideslip -= 0.1F;
		if(fSightCurSideslip < -3F)
			fSightCurSideslip = -3F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] {
				new Integer((int)(fSightCurSideslip * 10F))
		});
	}

	public void typeBomberAdjAltitudeReset()
	{
		fSightCurAltitude = 3000F;
	}

	public void typeBomberAdjAltitudePlus()
	{
		fSightCurAltitude += 50F;
		if(fSightCurAltitude > 50000F)
			fSightCurAltitude = 50000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] {
				new Integer((int)fSightCurAltitude)
		});
		fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjAltitudeMinus()
	{
		fSightCurAltitude -= 50F;
		if(fSightCurAltitude < 1000F)
			fSightCurAltitude = 1000F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitudeft", new Object[] {
				new Integer((int)fSightCurAltitude)
		});
		fSightCurDistance = toMeters(fSightCurAltitude) * (float)Math.tan(Math.toRadians(fSightCurForwardAngle));
	}

	public void typeBomberAdjSpeedReset()
	{
		fSightCurSpeed = 200F;
	}

	public void typeBomberAdjSpeedPlus()
	{
		fSightCurSpeed += 10F;
		if(fSightCurSpeed > 450F)
			fSightCurSpeed = 450F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] {
				new Integer((int)fSightCurSpeed)
		});
	}

	public void typeBomberAdjSpeedMinus()
	{
		fSightCurSpeed -= 10F;
		if(fSightCurSpeed < 100F)
			fSightCurSpeed = 100F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeedMPH", new Object[] {
				new Integer((int)fSightCurSpeed)
		});
	}

	public void typeBomberUpdate(float f)
	{
		if((double)Math.abs(FM.Or.getKren()) > 4.5D)
		{
			fSightCurReadyness -= 0.0666666F * f;
			if(fSightCurReadyness < 0.0F)
				fSightCurReadyness = 0.0F;
		}
		if(fSightCurReadyness < 1.0F)
			fSightCurReadyness += 0.0333333F * f;
		else
			if(bSightAutomation)
			{
				fSightCurDistance -= toMetersPerSecond(fSightCurSpeed) * f;
				if(fSightCurDistance < 0.0F)
				{
					fSightCurDistance = 0.0F;
					typeBomberToggleAutomation();
				}
				fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(fSightCurDistance / toMeters(fSightCurAltitude)));
				if((double)fSightCurDistance < (double)toMetersPerSecond(fSightCurSpeed) * Math.sqrt(toMeters(fSightCurAltitude) * 0.2038736F))
					bSightBombDump = true;
				if(bSightBombDump)
					if(FM.isTick(3, 0))
					{
						if(FM.CT.Weapons[3] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1] != null && FM.CT.Weapons[3][FM.CT.Weapons[3].length - 1].haveBullets())
						{
							FM.CT.WeaponControl[3] = true;
							HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
						}
					} else
					{
						FM.CT.WeaponControl[3] = false;
					}
			}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
			throws IOException
			{
		netmsgguaranted.writeByte((bSightAutomation ? 1 : 0) | (bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(fSightCurDistance);
		netmsgguaranted.writeByte((int)fSightCurForwardAngle);
		netmsgguaranted.writeByte((int)((fSightCurSideslip + 3F) * 33.33333F));
		netmsgguaranted.writeFloat(fSightCurAltitude);
		netmsgguaranted.writeByte((int)(fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int)(fSightCurReadyness * 200F));
			}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
			throws IOException
			{
		int i = netmsginput.readUnsignedByte();
		bSightAutomation = (i & 1) != 0;
		bSightBombDump = (i & 2) != 0;
		fSightCurDistance = netmsginput.readFloat();
		fSightCurForwardAngle = netmsginput.readUnsignedByte();
		fSightCurSideslip = -3F + (float)netmsginput.readUnsignedByte() / 33.33333F;
		fSightCurAltitude = netmsginput.readFloat();
		fSightCurSpeed = (float)netmsginput.readUnsignedByte() * 2.5F;
		fSightCurReadyness = (float)netmsginput.readUnsignedByte() / 200F;
			}

	public float getEyeLevelCorrection()
	{
		return -0.125F;
	}

	public void doMurderPilot(int i)
	{
		switch(i)
		{
		case 0: // '\0'
		hierMesh().chunkVisible("Pilot1_D0", false);
		hierMesh().chunkVisible("Head1_D0", false);
		hierMesh().chunkVisible("HMask1_D0", false);
		hierMesh().chunkVisible("Pilot1_D1", true);
		if(!FM.AS.bIsAboutToBailout)
		{
			if(hierMesh().isChunkVisible("Blister1_D0"))
				hierMesh().chunkVisible("Gore1_D0", true);
			hierMesh().chunkVisible("Gore2_D0", true);
		}
		break;
		case 1: // '\001'
			hierMesh().chunkVisible("Pilot2_D0", false);
			hierMesh().chunkVisible("Head2_D0", false);
			hierMesh().chunkVisible("HMask2_d0", false);
			hierMesh().chunkVisible("Pilot2_D1", true);
			break;
		}
	}

	protected void moveBayDoor(float f)
	{
		resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(f, 0.2F, 0.99F, 0.0F, 0.5F);
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 0.99F, 0.0F, -0.1F);
		Aircraft.ypr[2] = Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -60F);
		//hierMesh().chunkSetAngles("BombDoorLeft",  0.0F, 0.0F, Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, -60F));
		hierMesh().chunkSetLocate("BombDoorLeft", Aircraft.xyz, Aircraft.ypr);
		resetYPRmodifier();
		Aircraft.xyz[2] = Aircraft.cvt(f, 0.2F, 0.99F, 0.0F, 0.5F);
		Aircraft.xyz[1] = Aircraft.cvt(f, 0.2F, 0.99F, 0.0F, 0.1F);
		Aircraft.ypr[2] = Aircraft.cvt(f, 0.02F, 0.98F, 0.0F, 60F);
		hierMesh().chunkSetLocate("BombDoorRight", Aircraft.xyz, Aircraft.ypr);
	}

	public static void moveGear(HierMesh hiermesh, float f, float f1, float f2)
	{
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 90F * f2, 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f2, 0.01F, 0.21F, 0.0F, 110F), 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f2, 0.01F, 0.21F, 0.0F, 110F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f1, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Math.max(-f * 1500F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Math.max(-f1 * 1500F, -90F), 0.0F);
	}

	protected void moveGear(float f, float f1, float f2)
	{
		moveGear(hierMesh(), f, f1, f2);
	}

	public static void moveGear(HierMesh hiermesh, float f)
	{
		hiermesh.chunkSetAngles("GearC2_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearC5_D0", 0.0F, cvt(f, 0.01F, 0.21F, 0.0F, 110F), 0.0F);
		hiermesh.chunkSetAngles("GearC4_D0", 0.0F, cvt(f, 0.01F, 0.21F, 0.0F, 110F), 0.0F);
		hiermesh.chunkSetAngles("GearL2_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearR2_D0", 0.0F, 90F * f, 0.0F);
		hiermesh.chunkSetAngles("GearL6_D0", 0.0F, Math.max(-f * 1500F, -90F), 0.0F);
		hiermesh.chunkSetAngles("GearR6_D0", 0.0F, Math.max(-f * 1500F, -90F), 0.0F);
	}

	protected void moveGear(float f)
	{
		moveGear(hierMesh(), f);
	}

	protected void moveRudder(float f)
	{
		hierMesh().chunkSetAngles("Rudder1_D0", 0.0F, -30F * f, 0.0F);
	}

	public void moveSteering(float f)
	{
		if(FM.CT.getGear() < 0.99)
			hierMesh().chunkSetAngles("GearC1_D0", 0F * f, 0.0F, 0.0F);
		else
			hierMesh().chunkSetAngles("GearC1_D0", 15F * f, 0.0F, 0.0F);
	}

	protected void moveFlap(float paramFloat) {
		float f = -55.0F * paramFloat;
		this.hierMesh().chunkSetAngles("Flap01_D0", 0.0F, f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap02_D0", 0.0F, f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap03_D0", 0.0F, f, 0.0F);
		this.hierMesh().chunkSetAngles("Flap04_D0", 0.0F, f, 0.0F);
	}

	protected void moveFan(float f)
	{
	}

	protected void hitBone(String s, Shot shot, Point3d point3d)
	{
		if(s.startsWith("xcf"))
		{
			if(chunkDamageVisible("CF") < 3)
				hitChunk("CF", shot);
			if(point3d.x > 1.7D)
			{
				if(World.Rnd().nextFloat() < 0.07F)
					FM.AS.setJamBullets(0, 0);
				if(World.Rnd().nextFloat() < 0.07F)
					FM.AS.setJamBullets(0, 1);
				if(World.Rnd().nextFloat() < 0.12F)
					FM.AS.setJamBullets(1, 0);
				if(World.Rnd().nextFloat() < 0.12F)
					FM.AS.setJamBullets(1, 1);
			}
			if(point3d.x > -0.999D && point3d.x < 0.53500000000000003D && point3d.z > -0.224D)
			{
				if(World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x10);
				if(World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x20);
				if(World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 4);
				if(World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 8);
				if(World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 0x40);
				if(World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 1);
				if(World.Rnd().nextFloat() < 0.1F)
					FM.AS.setCockpitState(shot.initiator, FM.AS.astateCockpitState | 2);
			}
			if(point3d.x > 0.80000000000000004D && point3d.x < 1.5800000000000001D && World.Rnd().nextFloat() < 0.25F && (shot.powerType == 3 && getEnergyPastArmor(0.4F, shot) > 0.0F || shot.powerType == 0))
				FM.AS.hitTank(shot.initiator, 0, World.Rnd().nextInt(1, (int)(shot.power / 4000F)));
			if(point3d.x > -2.4849999999999999D && point3d.x < -1.6000000000000001D && World.Rnd().nextFloat() < 0.25F && (shot.powerType == 3 && getEnergyPastArmor(0.4F, shot) > 0.0F || shot.powerType == 0))
				FM.AS.hitTank(shot.initiator, 1, World.Rnd().nextInt(1, (int)(shot.power / 4000F)));
		} else
			if(s.startsWith("xtail"))
			{
				if(chunkDamageVisible("Tail1") < 3)
					hitChunk("Tail1", shot);
			} else
				if(s.startsWith("xkeel"))
					hitChunk("Keel1", shot);
				else
					if(s.startsWith("xstabl"))
						hitChunk("StabL", shot);
					else
						if(s.startsWith("xstabr"))
							hitChunk("StabR", shot);
						else
							if(s.startsWith("xwing"))
							{
								if(s.endsWith("lin") && chunkDamageVisible("WingLIn") < 3)
									hitChunk("WingLIn", shot);
								if(s.endsWith("rin") && chunkDamageVisible("WingRIn") < 3)
									hitChunk("WingRIn", shot);
								if(s.endsWith("lmid") && chunkDamageVisible("WingLMid") < 3)
									hitChunk("WingLMid", shot);
								if(s.endsWith("rmid") && chunkDamageVisible("WingRMid") < 3)
									hitChunk("WingRMid", shot);
								if(s.endsWith("lout") && chunkDamageVisible("WingLOut") < 3)
									hitChunk("WingLOut", shot);
								if(s.endsWith("rout") && chunkDamageVisible("WingROut") < 3)
									hitChunk("WingROut", shot);
							} else
								if(s.startsWith("xengine"))
								{
									int i = s.charAt(7) - 49;
									if(point3d.x > 0.0D && point3d.x < 0.69699999999999995D)
										FM.EI.engines[i].setCyliderKnockOut(shot.initiator, World.Rnd().nextInt(1, 6));
									if(World.Rnd().nextFloat(0.009F, 0.1357F) < shot.mass)
										FM.AS.hitEngine(shot.initiator, i, 5);
								} else
									if(s.startsWith("xpilot") || s.startsWith("xhead"))
									{
										byte byte0 = 0;
										int j;
										if(s.endsWith("a"))
										{
											byte0 = 1;
											j = s.charAt(6) - 49;
										} else
											if(s.endsWith("b"))
											{
												byte0 = 2;
												j = s.charAt(6) - 49;
											} else
											{
												j = s.charAt(5) - 49;
											}
										hitFlesh(j, shot, byte0);
									}
	}

	protected boolean cutFM(int i, int j, Actor actor)
	{
		switch(i)
		{
		case 33: // '!'
			return super.cutFM(34, j, actor);

		case 36: // '$'
			return super.cutFM(37, j, actor);

		case 11: // '\013'
			cutFM(17, j, actor);
			FM.cut(17, j, actor);
			cutFM(18, j, actor);
			FM.cut(18, j, actor);
			return super.cutFM(i, j, actor);
		}
		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag)
	{
		super.rareAction(f, flag);
		if(flag && World.Rnd().nextFloat() < 0.2F)
		{
			if(FM.AS.astateEngineStates[0] > 3 && World.Rnd().nextFloat() < 0.12F)
			{
				FM.AS.explodeEngine(this, 0);
				msgCollision(this, "WingLIn_D0", "WingLIn_D0");
				if(World.Rnd().nextBoolean())
					FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
				else
					FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
			}
			if(FM.AS.astateEngineStates[1] > 3 && World.Rnd().nextFloat() < 0.12F)
			{
				FM.AS.explodeEngine(this, 1);
				msgCollision(this, "WingRIn_D0", "WingRIn_D0");
				if(World.Rnd().nextBoolean())
					FM.AS.hitTank(this, 0, World.Rnd().nextInt(1, 8));
				else
					FM.AS.hitTank(this, 1, World.Rnd().nextInt(1, 8));
			}
		}
		if ((FM.Gears.nearGround() || FM.Gears.onGround()) && FM.CT.getCockpitDoor() == 1.0F) {
			hierMesh().chunkVisible("HMask1_D0", false);
		}
		else
		{
			hierMesh().chunkVisible("HMask1_D0", hierMesh().isChunkVisible("Head1_D0"));
		}
	}

	public void engineSurge(float f){
		for(int i = 0; i < 2; i++)	
			if(curthrl[i] == -1F)
			{
				curthrl[i] = oldthrl[i] = FM.EI.engines[i].getControlThrottle();
			} else
			{
				curthrl[i] = ((FlightModelMain) (super.FM)).EI.engines[i].getControlThrottle();
				if ((curthrl[i] - oldthrl[i]) / f > 20.0F && FM.EI.engines[i].getRPM() < 3200.0F && FM.EI.engines[i].getStage() == 6 && World.Rnd().nextFloat() < 0.40F)
				{
					if(FM.actor == World.getPlayerAircraft())
						HUD.log("Compressor Stall!");
					super.playSound("weapon.MGunMk108s", true);
					engineSurgeDamage[i] += 1.0000000000000001E-002D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
					((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage[i]);
					if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode())
					{
						FM.AS.hitEngine(this, i, 100);
					}
					if (World.Rnd().nextFloat() < 0.05F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode())
					{
						FM.EI.engines[i].setEngineDies(this);
					}
				}
				if ((curthrl[i] - oldthrl[i]) / f < -20.0F && (curthrl[i] - oldthrl[i]) / f > -100.0F && FM.EI.engines[i].getRPM() < 3200.0F && FM.EI.engines[i].getStage() == 6) 
				{
					super.playSound("weapon.MGunMk108s", true);
					engineSurgeDamage[i] += 1.0000000000000001E-003D * (double)(((FlightModelMain) (super.FM)).EI.engines[i].getRPM() / 1000F);
					((FlightModelMain) (super.FM)).EI.engines[i].doSetReadyness(((FlightModelMain) (super.FM)).EI.engines[i].getReadyness() - engineSurgeDamage[i]);
					if (World.Rnd().nextFloat() < 0.40F && FM instanceof RealFlightModel && ((RealFlightModel) FM).isRealMode())
					{
						if(FM.actor == World.getPlayerAircraft())
							HUD.log("Engine Flameout!");
						FM.EI.engines[i].setEngineStops(this);
					}
					else
					{
						if(FM.actor == World.getPlayerAircraft())
							HUD.log("Compressor Stall!");
						FM.EI.engines[i].setKillCompressor(this);
					}
				}
				oldthrl[i] = curthrl[i];
			}
	}

	public void doEjectCatapult() {
		new MsgAction(false, this) {

			public void doAction(Object paramObject) {
				Aircraft localAircraft = (Aircraft) paramObject;
				if (Actor.isValid(localAircraft)) {
					Loc localLoc1 = new Loc();
					Loc localLoc2 = new Loc();
					Vector3d localVector3d = new Vector3d(0.0, 0.0, 10.0);
					HookNamed localHookNamed = new HookNamed(localAircraft,
							"_ExternalSeat01");
					localAircraft.pos.getAbs(localLoc2);
					localHookNamed.computePos(localAircraft, localLoc2,
							localLoc1);
					localLoc1.transform(localVector3d);
					localVector3d.x += localAircraft.FM.Vwld.x;
					localVector3d.y += localAircraft.FM.Vwld.y;
					localVector3d.z += localAircraft.FM.Vwld.z;
					new EjectionSeat(6, localLoc1, localVector3d, localAircraft);
				}
			}
		};
		this.hierMesh().chunkVisible("Seat_D0", false);
		FM.setTakenMortalDamage(true, null);
		FM.CT.WeaponControl[0] = false;
		FM.CT.WeaponControl[1] = false;
		FM.CT.bHasAileronControl = false;
		FM.CT.bHasRudderControl = false;
		FM.CT.bHasElevatorControl = false;
	}

	private void bailout() {
		if (overrideBailout) {
			if (FM.AS.astateBailoutStep >= 0 && FM.AS.astateBailoutStep < 2) {
				if (FM.CT.cockpitDoorControl > 0.5F
						&& FM.CT.getCockpitDoor() > 0.5F) {
					FM.AS.astateBailoutStep = (byte) 11;
					doRemoveBlisters();
				} else {
					FM.AS.astateBailoutStep = (byte) 2;
				}
			} else if (FM.AS.astateBailoutStep >= 2
					&& FM.AS.astateBailoutStep <= 3) {
				switch (FM.AS.astateBailoutStep) {
				case 2:
					if (FM.CT.cockpitDoorControl < 0.5F) {
						doRemoveBlister1();
					}
					break;
				case 3:
					doRemoveBlisters();
					break;
				}
				if (FM.AS.isMaster()) {
					FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
				}
				FM.AS.astateBailoutStep = (byte) (FM.AS.astateBailoutStep + 1);
				if (FM.AS.astateBailoutStep == 4) {
					FM.AS.astateBailoutStep = (byte) 11;
				}
			} else if (FM.AS.astateBailoutStep >= 11
					&& FM.AS.astateBailoutStep <= 19) {
				int i = FM.AS.astateBailoutStep;
				if (FM.AS.isMaster()) {
					FM.AS.netToMirrors(20, FM.AS.astateBailoutStep, 1, null);
				}
				FM.AS.astateBailoutStep = (byte) (FM.AS.astateBailoutStep + 1);
				if (i == 11) {
					FM.setTakenMortalDamage(true, null);
					if (FM instanceof Maneuver
							&& ((Maneuver) FM).get_maneuver() != 44) {
						World.cur();
						if (FM.AS.actor != World.getPlayerAircraft()) {
							((Maneuver) FM).set_maneuver(44);
						}
					}
				}
				if (FM.AS.astatePilotStates[i - 11] < 99) {
					this.doRemoveBodyFromPlane(i - 10);
					if (i == 11) {
						doEjectCatapult();
						FM.setTakenMortalDamage(true, null);
						FM.CT.WeaponControl[0] = false;
						FM.CT.WeaponControl[1] = false;
						FM.AS.astateBailoutStep = (byte) -1;
						overrideBailout = false;
						FM.AS.bIsAboutToBailout = true;
						ejectComplete = true;
						if (i > 10 && i <= 19) {
							EventLog.onBailedOut(this, i - 11);
						}
					}
				}
			}
		}
	}

	private final void doRemoveBlister1() {
		if (this.hierMesh().chunkFindCheck("Blister1_D0") != -1
				&& FM.AS.getPilotHealth(0) > 0.0F) {
			this.hierMesh().hideSubTrees("Blister1_D0");
			Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister1_D0"));
			localWreckage.collide(false);
			Vector3d localVector3d = new Vector3d();
			localVector3d.set(FM.Vwld);
			localWreckage.setSpeed(localVector3d);
		}
	}

	private final void doRemoveBlisters() {
		for (int i = 2; i < 10; i++) {
			if (this.hierMesh().chunkFindCheck("Blister" + i + "_D0") != -1
					&& FM.AS.getPilotHealth(i - 1) > 0.0F) {
				this.hierMesh().hideSubTrees("Blister" + i + "_D0");
				Wreckage localWreckage = new Wreckage(this, this.hierMesh().chunkFind("Blister" + i + "_D0"));
				localWreckage.collide(false);
				Vector3d localVector3d = new Vector3d();
				localVector3d.set(FM.Vwld);
				localWreckage.setSpeed(localVector3d);
			}
		}
	}

	public void update(float f)
	{
		if ((FM.AS.bIsAboutToBailout || overrideBailout) && !ejectComplete
				&& FM.getSpeedKMH() > 15.0F) {
			overrideBailout = true;
			FM.AS.bIsAboutToBailout = false;
			bailout();
		}
		if(FM.AS.isMaster())
		{
			engineSurge(f);
			if(Config.isUSE_RENDER())
			{
				for(int i = 0; i <2; i++){
					if (FM.EI.engines[i].getStage() == 2 && !starterTriggered[i]){
						Eff3DActor.New(this, findHook("_Engine" + (i + 1) + "Smoke"), null, 1.0F, "3DO/Effects/Aircraft/CanberraStarter.eff", 1F);
						starterTriggered[i] = true;
					}
					else if(starterTriggered[i])
					{
						starterTriggered[i] = false;
					}
					if (FM.EI.engines[i].getThrustOutput() > 0.50F && FM.EI.engines[i].getStage() == 6) {
						FM.AS.setSootState(this, i, 3);
					} else {
						FM.AS.setSootState(this, i, 0);
					}
				}
			}
		}
		super.update(f);
	}

	private boolean overrideBailout;
	private boolean ejectComplete;
	private boolean bSightAutomation;
	private boolean bSightBombDump;
	private float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;
	private float oldthrl[] = {
			-1F, -1F
	};
	private float curthrl[] = {
			-1F, -1F
	};
	private float engineSurgeDamage[] = {
			0F, 0F
	};

	static 
	{
		Class class1 = com.maddox.il2.objects.air.Canberra.class;
		Property.set(class1, "originCountry", PaintScheme.countryBritain);
	}
}