package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.weapons.GuidedMissileUtils;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import java.io.IOException;


public class TU_4 extends B_29X
implements TypeBomber, TypeGuidedMissileCarrier, TypeX4Carrier
{
	public static boolean bChangedPit = false;
	private boolean bSightAutomation;
	private boolean bSightBombDump;
	private float fSightCurDistance;
	public float fSightCurForwardAngle;
	public float fSightCurSideslip;
	public float fSightCurAltitude;
	public float fSightCurSpeed;
	public float fSightCurReadyness;
	private GuidedMissileUtils guidedMissileUtils = new GuidedMissileUtils(this);
	private float deltaAzimuth;
	private float deltaTangage;

	public TU_4()
	{
		this.bSightAutomation = false;
		this.bSightBombDump = false;
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
		this.fSightCurSideslip = 0.0F;
		this.fSightCurAltitude = 850.0F;
		this.fSightCurSpeed = 200.0F;
		this.fSightCurReadyness = 0.0F;
		this.deltaAzimuth = 0.0F;
		this.deltaTangage = 0.0F;
	}

	protected boolean cutFM(int i, int j, Actor actor)
	{
		switch (i)
		{
		case 19:
			killPilot(this, 4);
		}

		return super.cutFM(i, j, actor);
	}

	public void rareAction(float f, boolean flag)
	{
		super.rareAction(f, flag);
		for (int i = 1; i < 7; i++)
			if (this.FM.getAltitude() < 3000.0F)
				hierMesh().chunkVisible("HMask" + i + "_D0", false);
			else
				hierMesh().chunkVisible("HMask" + i + "_D0", hierMesh().isChunkVisible("Pilot" + i + "_D0"));
	}

	protected void nextDMGLevel(String s, int i, Actor actor)
	{
		super.nextDMGLevel(s, i, actor);
		if (this.FM.isPlayers())
			bChangedPit = true;
	}

	protected void nextCUTLevel(String s, int i, Actor actor)
	{
		super.nextCUTLevel(s, i, actor);
		if (this.FM.isPlayers())
			bChangedPit = true;
	}

	public boolean turretAngles(int i, float[] af)
	{
		boolean flag = super.turretAngles(i, af);
		float f = -af[0];
		float f1 = af[1];
		switch (i)
		{
		default:
			break;
		case 0:
			if (f < -23.0F)
			{
				f = -23.0F;
				flag = false;
			}
			if (f > 23.0F)
			{
				f = 23.0F;
				flag = false;
			}
			if (f1 < -25.0F)
			{
				f1 = -25.0F;
				flag = false;
			}
			if (f1 <= 15.0F)
				break;
			f1 = 15.0F;
			flag = false;

			break;
		case 1:
			if (f1 < 0.0F)
			{
				f1 = 0.0F;
				flag = false;
			}
			if (f1 <= 73.0F)
				break;
			f1 = 73.0F;
			flag = false;

			break;
		case 2:
			if (f < -38.0F)
			{
				f = -38.0F;
				flag = false;
			}
			if (f > 38.0F)
			{
				f = 38.0F;
				flag = false;
			}
			if (f1 < -41.0F)
			{
				f1 = -41.0F;
				flag = false;
			}
			if (f1 <= 43.0F)
				break;
			f1 = 43.0F;
			flag = false;

			break;
		case 3:
			if (f < -85.0F)
			{
				f = -85.0F;
				flag = false;
			}
			if (f > 22.0F)
			{
				f = 22.0F;
				flag = false;
			}
			if (f1 < -40.0F)
			{
				f1 = -40.0F;
				flag = false;
			}
			if (f1 <= 32.0F)
				break;
			f1 = 32.0F;
			flag = false;

			break;
		case 4:
			if (f < -34.0F)
			{
				f = -34.0F;
				flag = false;
			}
			if (f > 30.0F)
			{
				f = 30.0F;
				flag = false;
			}
			if (f1 < -30.0F)
			{
				f1 = -30.0F;
				flag = false;
			}
			if (f1 <= 32.0F)
				break;
			f1 = 32.0F;
			flag = false;
		}

		af[0] = (-f);
		af[1] = f1;
		return flag;
	}

	public void doKillPilot(int i)
	{
		switch (i)
		{
		case 2:
			this.FM.turret[0].bIsOperable = false;
			break;
		case 3:
			this.FM.turret[1].bIsOperable = false;
			break;
		case 4:
			this.FM.turret[2].bIsOperable = false;
			break;
		case 5:
			this.FM.turret[3].bIsOperable = false;
			this.FM.turret[4].bIsOperable = false;
		}
	}

	public boolean typeBomberToggleAutomation()
	{
		this.bSightAutomation = (!this.bSightAutomation);
		this.bSightBombDump = false;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAutomation" + (this.bSightAutomation ? "ON" : "OFF"));
		return this.bSightAutomation;
	}

	public void typeBomberAdjDistanceReset()
	{
		this.fSightCurDistance = 0.0F;
		this.fSightCurForwardAngle = 0.0F;
	}

	public void typeBomberAdjDistancePlus()
	{
		this.fSightCurForwardAngle += 1.0F;
		if (this.fSightCurForwardAngle > 85.0F)
			this.fSightCurForwardAngle = 85.0F;
		this.fSightCurDistance = (this.fSightCurAltitude * (float)Math.tan(Math.toRadians(this.fSightCurForwardAngle)));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { 
				new Integer((int)this.fSightCurForwardAngle) });

		if (this.bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjDistanceMinus()
	{
		this.fSightCurForwardAngle -= 1.0F;
		if (this.fSightCurForwardAngle < -15.0F)
			this.fSightCurForwardAngle = -15.0F;
		this.fSightCurDistance = (this.fSightCurAltitude * (float)Math.tan(Math.toRadians(this.fSightCurForwardAngle)));
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightElevation", new Object[] { 
				new Integer((int)this.fSightCurForwardAngle) });

		if (this.bSightAutomation)
			typeBomberToggleAutomation();
	}

	public void typeBomberAdjSideslipReset()
	{
		this.fSightCurSideslip = 0.0F;
	}

	public void typeBomberAdjSideslipPlus()
	{
		this.fSightCurSideslip += 0.1F;
		if (this.fSightCurSideslip > 3.0F)
			this.fSightCurSideslip = 3.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { 
				new Integer((int)(this.fSightCurSideslip * 10.0F)) });
	}

	public void typeBomberAdjSideslipMinus()
	{
		this.fSightCurSideslip -= 0.1F;
		if (this.fSightCurSideslip < -3.0F)
			this.fSightCurSideslip = -3.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSlip", new Object[] { 
				new Integer((int)(this.fSightCurSideslip * 10.0F)) });
	}

	public void typeBomberAdjAltitudeReset()
	{
		this.fSightCurAltitude = 850.0F;
	}

	public void typeBomberAdjAltitudePlus()
	{
		this.fSightCurAltitude += 10.0F;
		if (this.fSightCurAltitude > 10000.0F)
			this.fSightCurAltitude = 10000.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { 
				new Integer((int)this.fSightCurAltitude) });

		this.fSightCurDistance = (this.fSightCurAltitude * (float)Math.tan(Math.toRadians(this.fSightCurForwardAngle)));
	}

	public void typeBomberAdjAltitudeMinus()
	{
		this.fSightCurAltitude -= 10.0F;
		if (this.fSightCurAltitude < 850.0F)
			this.fSightCurAltitude = 850.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightAltitude", new Object[] { 
				new Integer((int)this.fSightCurAltitude) });

		this.fSightCurDistance = (this.fSightCurAltitude * (float)Math.tan(Math.toRadians(this.fSightCurForwardAngle)));
	}

	public void typeBomberAdjSpeedReset()
	{
		this.fSightCurSpeed = 150.0F;
	}

	public void typeBomberAdjSpeedPlus()
	{
		this.fSightCurSpeed += 10.0F;
		if (this.fSightCurSpeed > 800.0F)
			this.fSightCurSpeed = 800.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { 
				new Integer((int)this.fSightCurSpeed) });
	}

	public void typeBomberAdjSpeedMinus()
	{
		this.fSightCurSpeed -= 10.0F;
		if (this.fSightCurSpeed < 150.0F)
			this.fSightCurSpeed = 150.0F;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightSpeed", new Object[] { 
				new Integer((int)this.fSightCurSpeed) });
	}

	public void typeBomberUpdate(float f)
	{
		if (Math.abs(this.FM.Or.getKren()) > 4.5D)
		{
			this.fSightCurReadyness -= 0.0666666F * f;
			if (this.fSightCurReadyness < 0.0F)
				this.fSightCurReadyness = 0.0F;
		}
		if (this.fSightCurReadyness < 1.0F) {
			this.fSightCurReadyness += 0.0333333F * f;
		}
		else if (this.bSightAutomation)
		{
			this.fSightCurDistance -= this.fSightCurSpeed / 3.6F * f;
			if (this.fSightCurDistance < 0.0F)
			{
				this.fSightCurDistance = 0.0F;
				typeBomberToggleAutomation();
			}
			this.fSightCurForwardAngle = (float)Math.toDegrees(Math.atan(this.fSightCurDistance / this.fSightCurAltitude));
			if (this.fSightCurDistance < this.fSightCurSpeed / 3.6F * Math.sqrt(this.fSightCurAltitude * 0.203874F))
				this.bSightBombDump = true;
			if (this.bSightBombDump)
				if (this.FM.isTick(3, 0))
				{
					if ((this.FM.CT.Weapons[3] != null) && (this.FM.CT.Weapons[3][(this.FM.CT.Weapons[3].length - 1)] != null) && (this.FM.CT.Weapons[3][(this.FM.CT.Weapons[3].length - 1)].haveBullets()))
					{
						this.FM.CT.WeaponControl[3] = true;
						HUD.log(AircraftHotKeys.hudLogWeaponId, "BombsightBombdrop");
					}
				}
				else
					this.FM.CT.WeaponControl[3] = false;
		}
	}

	public void typeBomberReplicateToNet(NetMsgGuaranted netmsgguaranted)
			throws IOException
			{
		netmsgguaranted.writeByte((this.bSightAutomation ? 1 : 0) | (this.bSightBombDump ? 2 : 0));
		netmsgguaranted.writeFloat(this.fSightCurDistance);
		netmsgguaranted.writeByte((int)this.fSightCurForwardAngle);
		netmsgguaranted.writeByte((int)((this.fSightCurSideslip + 3.0F) * 33.333328F));
		netmsgguaranted.writeFloat(this.fSightCurAltitude);
		netmsgguaranted.writeByte((int)(this.fSightCurSpeed / 2.5F));
		netmsgguaranted.writeByte((int)(this.fSightCurReadyness * 200.0F));
			}

	public void typeBomberReplicateFromNet(NetMsgInput netmsginput)
			throws IOException
			{
		int i = netmsginput.readUnsignedByte();
		this.bSightAutomation = ((i & 0x1) != 0);
		this.bSightBombDump = ((i & 0x2) != 0);
		this.fSightCurDistance = netmsginput.readFloat();
		this.fSightCurForwardAngle = netmsginput.readUnsignedByte();
		this.fSightCurSideslip = (-3.0F + netmsginput.readUnsignedByte() / 33.333328F);
		this.fSightCurAltitude = netmsginput.readFloat();
		this.fSightCurSpeed = (netmsginput.readUnsignedByte() * 2.5F);
		this.fSightCurReadyness = (netmsginput.readUnsignedByte() / 200.0F);
			}

	public GuidedMissileUtils getGuidedMissileUtils() {
		return this.guidedMissileUtils;
	}

	public void onAircraftLoaded()
	{
		super.onAircraftLoaded();
		this.guidedMissileUtils.onAircraftLoaded();
		FM.CT.bHasBayDoors = true;
	}

	public void update(float f1) {
		this.guidedMissileUtils.update();
		super.update(f1);
	}

	public void typeX4CAdjSidePlus()
	{
		this.deltaAzimuth = 1.0F;
	}

	public void typeX4CAdjSideMinus()
	{
		this.deltaAzimuth = -1.0F;
	}

	public void typeX4CAdjAttitudePlus()
	{
		this.deltaTangage = 1.0F;
	}

	public void typeX4CAdjAttitudeMinus()
	{
		this.deltaTangage = -1.0F;
	}

	public void typeX4CResetControls()
	{
		this.deltaAzimuth = (this.deltaTangage = 0.0F);
	}

	public float typeX4CgetdeltaAzimuth()
	{
		return this.deltaAzimuth;
	}

	public float typeX4CgetdeltaTangage()
	{
		return this.deltaTangage;
	}

	static
	{
		Class class1 = TU_4.class;
		new NetAircraft.SPAWN(class1);
		Property.set(class1, "iconFar_shortClassName", "TU-4");
		Property.set(class1, "meshName", "3DO/Plane/Tu-4/hier.him");
		Property.set(class1, "PaintScheme", new PaintSchemeBMPar05());
		Property.set(class1, "yearService", 1947.0F);
		Property.set(class1, "yearExpired", 1960.0F);
		Property.set(class1, "cockpitClass", new Class[] { 
				CockpitTU4.class, CockpitTU4_Bombardier.class, CockpitTU4_TGunner.class, CockpitTU4_T2Gunner.class, CockpitTU4_FGunner.class, CockpitTU4_AGunner.class, CockpitTU4_RGunner.class });

		Property.set(class1, "FlightModel", "FlightModels/B-29TU4.fmd");
		Aircraft.weaponTriggersRegister(class1, new int[] { 
				10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 
				3, 3, 3, 3, 2, 2, 2, 2, 9, 9 });

		Aircraft.weaponHooksRegister(class1, new String[] { 
				"_MGUN01", "_MGUN02", "_MGUN05", "_MGUN06", "_MGUN07", "_MGUN08", "_MGUN09", "_MGUN10", "_MGUN11", "_MGUN12", 
				"_BombSpawn01", "_BombSpawn02", "_BombSpawn03", "_BombSpawn04", "_ExternalRock01", "_ExternalRock01", "_ExternalRock02", "_ExternalRock02", "_ExternalDev01", "_ExternalDev02" });

		Aircraft.weaponsRegister(class1, "default", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				null, null, null, null, null, null, null, null, null, null });

		Aircraft.weaponsRegister(class1, "48XFAB-100", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				"BombGunFAB100 12", "BombGunFAB100 12", "BombGunFAB100 12", "BombGunFAB100 12", null, null, null, null, null, null });

		Aircraft.weaponsRegister(class1, "48XFAB-50", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				"BombGunFAB50 12", "BombGunFAB50 12", "BombGunFAB50 12", "BombGunFAB50 12", null, null, null, null, null, null });

		Aircraft.weaponsRegister(class1, "24XFAB-250M-46", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				"BombGunFAB250m46 6", "BombGunFAB250m46 6", "BombGunFAB250m46 6", "BombGunFAB250m46 6", null, null, null, null, null, null });

		Aircraft.weaponsRegister(class1, "24XFAB-250", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				"BombGunFAB250int 6", "BombGunFAB250int 6", "BombGunFAB250int 6", "BombGunFAB250int 6", null, null, null, null, null, null });

		Aircraft.weaponsRegister(class1, "14XFAB-500", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				"BombGunFAB500int 4", "BombGunFAB500int 4", "BombGunFAB500int 3", "BombGunFAB500int 3", null, null, null, null, null, null });

		Aircraft.weaponsRegister(class1, "8XFAB-1000", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				"BombGunFAB1000int 2", "BombGunFAB1000int 2", "BombGunFAB1000int 2", "BombGunFAB1000int 2", null, null, null, null, null, null });

		Aircraft.weaponsRegister(class1, "1xRDS-4T", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				"BombGunRDS4T 1", null, null, null, null, null, null, null, null, null });

		Aircraft.weaponsRegister(class1, "2xKS-1", new String[] { 
				"MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", "MGunVYakNS23 315", 
				null, null, null, null, "RocketGunKS1 1", "RocketGunNull 1", "RocketGunKS1 1", "RocketGunNull 1", "PylonKS1", "PylonKS1" });

		Aircraft.weaponsRegister(class1, "none", new String[]{ 
				null, null, null, null, null, null, null, null, null, null, 
				null, null, null, null, null, null, null, null, null, null });
	}
}