/*Modified Controls class for the SAS Engine Mod*/

/*
 * Changelog:
 * 11th/Aug./2014: doSetRocketHook method changed to make this class independent from Guided Missile Mod
 * 16th/Jun./2016: importing 4.13.2m's Bomb release mode codes and adding Select bomb function.
 * 22nd/Oct./2016: By PAL, modified incorrect static variables
 * 03rd/Jan./2017: By western, implement limit Ailerons, debug Select bomb and Unlimited Ammo
 * 14th/Jan./2017: By western, implement Rocket mode Ripple (continuous Pairs)
 * 07th/Feb./2017: By western, implement Formation lights and Anti-Collision lights
 * 18th/Nov./2017: By western, add No Jettison variant of external Fueltanks
 * 03rd/Apr./2018: By western, add AI take-off rotation and climb pitch target values
 * 16th/Oct./2019: By western, add getter methods of bombing time and class, Bomb Select treats in the same group for subtypes "groupLeader" property value written
 * 18th/Oct./2019: By western, array size and for() end values are written in static MAX int for future expanding.
 */

package com.maddox.il2.fm;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.BulletEmitter;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.GuidedMissileInterop;
import com.maddox.il2.engine.Hook;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.SU_26M2;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.FuelTank;
import com.maddox.il2.objects.weapons.FuelTankGun;
import com.maddox.il2.objects.weapons.RemoteControlRocket;
import com.maddox.il2.objects.weapons.RocketBombGun;
import com.maddox.il2.objects.weapons.RocketGun;
import com.maddox.il2.objects.weapons.RocketGunBat;
import com.maddox.il2.objects.weapons.RocketGunFritzX;
import com.maddox.il2.objects.weapons.RocketGunHS_293;
import com.maddox.il2.objects.weapons.RocketGunNull;
import com.maddox.il2.objects.weapons.RocketGunX4;
import com.maddox.rts.Property;
import com.maddox.rts.Time;
import com.maddox.sas1946.il2.util.Reflection;

public class Controls {
	// TODO: Default Parameters
	// --------------------------------------------------------
	public float Sensitivity;
	public float PowerControl;
	public boolean afterburnerControl;
	public float GearControl;
	public float wingControl;
	public float cockpitDoorControl;
	public float arrestorControl;
	public float FlapsControl;
	public float AileronControl;
	public float ElevatorControl;
	public float RudderControl;
	public float BrakeControl;
	private float StepControl;
	private boolean bStepControlAuto;
	private float MixControl;
	private int MagnetoControl;
	private int CompressorControl;
	public float BayDoorControl;
	public float AirBrakeControl;
	private float trimAileronControl;
	private float trimElevatorControl;
	private float trimRudderControl;
	public float trimAileron;
	public float trimElevator;
	public float trimRudder;
	private float RadiatorControl;
	private boolean bRadiatorControlAuto;
	public boolean StabilizerControl;
	public boolean WeaponControl[];
	public boolean saveWeaponControl[];
	public boolean bHasGearControl;
	public boolean bHasBayDoorControl;
	public boolean bHasWingControl;
	public boolean bHasCockpitDoorControl;
	public boolean bHasArrestorControl;
	public boolean bHasFlapsControl;
	public boolean bHasFlapsControlRed;
	public boolean bHasAileronControl;
	public boolean bHasElevatorControl;
	public boolean bHasRudderControl;
	public boolean bHasBrakeControl;
	public boolean bHasAirBrakeControl;
	public boolean bHasLockGearControl;
	public boolean bHasAileronTrim;
	public boolean bHasRudderTrim;
	public boolean bHasElevatorTrim;
	public BulletEmitter Weapons[][];
	public byte CTL;
	public byte WCT;
	public int TWCT;
	private float Power;
	private float Gear;
	private float gearR;
	private float gearL;
	private float gearC;
	private float wing;
	private float cockpitDoor;
	private float arrestor;
	private float Flaps;
	private float Ailerons;
	private float Elevators;
	private float Rudder;
	private float Brake;
	private float Step;
	private float radiator;
	private float airBrake;
	private float Ev;
	private int tick;
	public float AilThr;
	public float AilThr3;
	public float RudThr;
	public float RudThr2;
	public float ElevThr;
	public float ElevThr2;
	public float dvGear;
	public float dvGearR;
	public float dvGearL;
	public float dvGearC;
	public float dvWing;
	public float dvCockpitDoor;
	public float dvAirbrake;
	public int electricPropUp;
	public int electricPropDn;
	public boolean bUseElectricProp;
	public float PowerControlArr[];
	public float StepControlArr[];
	private FlightModelMain FM;
	//By PAL, static? --- Yes, static OK for temporary flashable value and to save memory and overhead.
	private static float tmpF;
	public boolean bDropWithPlayer;
	public Aircraft dropWithPlayer;
	boolean bDropWithMe;
	//By PAL, static? --- Yes, static OK for temporary flashable value and to save memory and overhead.
	private static Vector3d tmpV3d = new Vector3d();
	// --------------------------------------------------------

	// TODO: New parameters
	// --------------------------------------------------------
	public static final int MAX_ENGINE_NUM = 10;
	public boolean bHasCatLaunchBarControl;
	public float CatLaunchBarControl;
	public float dvCatLaunchBar;
	private float catLaunchBar;
	public boolean bHasAileronControlPowered;
	public boolean bHasElevatorControlPowered;
	public boolean bHasRudderControlPowered;
	public int limitModeOfAilerons;
	public float limitThresholdSpeedKMH;
	public float limitRatioAileron;
	public float limitRatioElevatorPlus;
	public float limitRatioElevatorMinus;
	public float limitRatioElevatorMinusALWAYS;
	public float limitRatioAITakeoffElevatorPlus;
	public float limitRatioRudder;
	public int DiffBrakesType;
	public float BrakeRightControl;
	public float BrakeLeftControl;
	private float BrakeRight;
	private float BrakeLeft;
	public boolean bHasDragChuteControl;
	public float DragChuteControl;
	public float dvDragchute;
	private float dragChute;
	public boolean bHasRefuelControl;
	public float RefuelControl;
	public float dvRefuel;
	private float refuel;
	public boolean bHasBayDoors = false;
	private float fSaveCockpitDoor;
	private float fSaveCockpitDoorControl;
	private float fSaveSideDoor;
	private float fSaveSideDoorControl;
	public boolean bMoveSideDoor = false;
	private int SIDE_DOOR = 2;
// These 4x static values were used by commented-out old missile codes only
//	public static final int LEFT = 1;
//	public static final int RIGHT = 2;
//	public int iToggleRocketSide = LEFT;
//	public int iToggleBombSide = LEFT;
	public long lWeaponTime = System.currentTimeMillis();
	public static final int WEAPON_FIRE_MODE_SALVO = 0;
	public static final int WEAPON_FIRE_MODE_SINGLE = 1;
	public static final int WEAPON_FIRE_MODE_PAIRS = 2;
	public static final int WEAPON_FIRE_MODE_RIPPLE = 3;
	public int weaponFireMode = WEAPON_FIRE_MODE_PAIRS;
	private int prevUserWeaponFireMode = weaponFireMode;
	public long weaponReleaseDelay = 33L;
	public int rocketHookSelected = 2;
	public String rocketNameSelected = null;
	public int nFlapStages;
	public float FlapStageMax;
	public float[] FlapStage = null;
	public float FlapTakeoffGround;
	public float FlapTakeoffCarrier;
	public int FlapsControlSwitch;
	public boolean bHasFlapsControlSwitch;
	public String[] FlapStageText = null;
	public float VarWingControl;
	private float VarWing;
	public int nVarWingStages;
	public float VarWingStageMax;
	public float[] VarWingStage = null;
	public float dvVarWing;
	public boolean bHasVarWingControl;
	public boolean bHasVarWingControlFree;
	public boolean bUseVarWingAsNozzleRot;
	public boolean bHasVarIncidence;
	public int VarWingControlSwitch;
	public boolean bHasVarWingControlSwitch;
	public String[] VarWingStageText = null;
	public boolean bHasStabilizerControl;
	public boolean bHasBlownFlaps;
	public float BlownFlapsControl;
	private float BlownFlaps;
	public float dvBlownFlaps;
	public String BlownFlapsType;
	public boolean bNoCarrierCanopyOpen;
	public boolean bHasSideDoor;
	public boolean bHasBombSelect;
	private Class[] bombClassArr;
	private Class[][] bombLeaderClassRvsIdx;
	private static final int B_CLASSES_MAX = 8;
	private static final int B_SUBCLASSES_MAX = 8;
	private int bombClassNumber;
	public int curBombSelected;
	private long timeOfLastBombReleaseRecorded;
	private Class classOfLastBombReleaseRecorded;
	public boolean bHasFormationLights;
	public boolean bFormationLights;
	public boolean bHasAntiColLights;
	public boolean bAntiColLights;
	public boolean bDroptanksDropped;
	public float targetDegreeAITakeoffRotation;
	public float targetDegreeAITakeoffClimb;
	public float targetDegreeAIApproach;

	// --------------------------------------------------------

	// Import values from 4.13.2m
	// --------------------------------------------------------
	private static final int B_RELEASE_SALVO = 0;
	private static final int B_RELEASE_SINGLE_WINGS_FIRST = 1;
	private static final int B_RELEASE_SINGLE_FC_FIRST = 2;
	private static final int B_RELEASE_PAIRS_WINGS_FIRST = 3;
	private static final int B_RELEASE_PAIRS_FC_FIRST = 4;
	private static final int B_RELEASE_TRAIN = 5;
	public int bombReleaseMode;
	public int bombTrainDelay;
	public int bombTrainAmount;
	public static final int bombTrainDelays[] = {
		50, 100, 200, 300, 400, 500, 600, 700, 800, 900,
		1000
	};
	private int bombsReleased;
	private long timeOfLastBombRelease;
//	private int bombsHangedNumber;  // for Unlimited Ammo
	private int bombsHangedNumberByClass[];  // for Unlimited Ammo and select
//	private int rocketsReleased;
//	private long timeOfLastRocketRelease;
	private int localHudLogWeaponId;

	// --------------------------------------------------------
	public Controls(FlightModelMain flightmodelmain) {
		Sensitivity = 1.0F;
		PowerControl = 0.0F;
		GearControl = 0.0F;
		wingControl = 0.0F;
		cockpitDoorControl = 0.0F;
		arrestorControl = 0.0F;
		FlapsControl = 0.0F;
		AileronControl = 0.0F;
		ElevatorControl = 0.0F;
		RudderControl = 0.0F;
		BrakeControl = 0.0F;
		StepControl = 1.0F;
		bStepControlAuto = true;
		MixControl = 1.0F;
		MagnetoControl = 3;
		CompressorControl = 0;
		BayDoorControl = 0.0F;
		AirBrakeControl = 0.0F;
		trimAileronControl = 0.0F;
		trimElevatorControl = 0.0F;
		trimRudderControl = 0.0F;
		trimAileron = 0.0F;
		trimElevator = 0.0F;
		trimRudder = 0.0F;
		RadiatorControl = 0.0F;
		bRadiatorControlAuto = true;
		StabilizerControl = false;
		WeaponControl = new boolean[21];
		saveWeaponControl = new boolean[4];
		bHasGearControl = true;
		bHasBayDoorControl = false;
		bHasWingControl = false;
		bHasCockpitDoorControl = false;
		bHasArrestorControl = false;
		bHasFlapsControl = true;
		bHasFlapsControlRed = false;
		bHasAileronControl = true;
		bHasElevatorControl = true;
		bHasRudderControl = true;
		bHasBrakeControl = true;
		bHasAirBrakeControl = true;
		bHasLockGearControl = true;
		bHasAileronTrim = true;
		bHasRudderTrim = true;
		bHasElevatorTrim = true;
		Weapons = new BulletEmitter[21][];
		Step = 1.0F;
		AilThr = 100F;
		AilThr3 = 1000000F;
		RudThr = 100F;
		RudThr2 = 10000F;
		ElevThr = 112F;
		ElevThr2 = 12544F;
		dvGear = 0.2F;
		dvGearR = 0.19F;
		dvGearL = 0.21F;
		dvGearC = 0.24F;
		dvWing = 0.1F;
		dvCockpitDoor = 0.1F;
		dvAirbrake = 0.5F;
		electricPropDn = 0;
		//  PowerControlArr = new float[6];   --- stock code comment-out by western
		//  StepControlArr = new float[6];   --- stock code comment-out by western
		bDropWithPlayer = false;
		dropWithPlayer = null;
		bDropWithMe = false;
		FM = flightmodelmain;
		// TODO: Expanded to allow for 10 engines, use MAX_ENGINE_NUM static int value for future more expanding
		// --------------------------------------------------------
		PowerControlArr = new float[MAX_ENGINE_NUM];
		StepControlArr = new float[MAX_ENGINE_NUM];
		for (int i = 0; i < MAX_ENGINE_NUM; i++)
			PowerControlArr[i] = 0.0F;
		for (int j = 0; j < MAX_ENGINE_NUM; j++)
			StepControlArr[j] = 1.0F;
		// --------------------------------------------------------

		// TODO: New parameters
		// --------------------------------------------------------
		bHasCatLaunchBarControl = false;
		CatLaunchBarControl = 0.0F;
		dvCatLaunchBar = 0.5F;
		catLaunchBar = 0.0F;
		bHasAileronControlPowered = false;
		bHasElevatorControlPowered = false;
		bHasRudderControlPowered = false;
		limitModeOfAilerons = 0;
		limitThresholdSpeedKMH = 440F;
		limitRatioAileron = 1.0F;
		limitRatioElevatorPlus = 1.0F;
		limitRatioElevatorMinus = 1.0F;
		limitRatioElevatorMinusALWAYS = 1.0F;
		limitRatioAITakeoffElevatorPlus = 1.0F;
		limitRatioRudder = 1.0F;
		bHasDragChuteControl = false;
		bHasRefuelControl = false;
		DiffBrakesType = 0;
		BrakeRightControl = 0.0F;
		BrakeLeftControl = 0.0F;
		nFlapStages = -1;
		FlapStageMax = -1.0F;
		FlapStage = null;
		FlapTakeoffGround = -1.0F;
		FlapTakeoffCarrier = -1.0F;
		FlapsControlSwitch = 0;
		bHasFlapsControlSwitch = false;
		FlapStageText = null;
		bHasVarWingControl = false;
		bHasVarWingControlFree = false;
		bUseVarWingAsNozzleRot = false;
		bHasVarIncidence = false;
		VarWingControl = 0.0F;
		nVarWingStages = -1;
		VarWingStageMax = -1.0F;
		VarWingStage = null;
		VarWingControlSwitch = 0;
		bHasVarWingControlSwitch = false;
		VarWingStageText = null;
		bHasStabilizerControl = false;
		DragChuteControl = 0.0F;
		RefuelControl = 0.0F;
		dvDragchute = 0.5F;
		dvRefuel = 0.5F;
		dvVarWing = 0.5F;
		bHasBlownFlaps = false;
		BlownFlapsControl = 0.0F;
		dvBlownFlaps = 0.5F;
		bNoCarrierCanopyOpen = false;
		bHasSideDoor = false;
		bHasBombSelect = false;
		bombClassArr = new Class[B_CLASSES_MAX];
		bombLeaderClassRvsIdx = new Class[B_CLASSES_MAX][B_SUBCLASSES_MAX];
		bombClassNumber = 0;
		curBombSelected = -1;
//		bombsHangedNumber = 0;
		bombsHangedNumberByClass = new int[8];
		timeOfLastBombReleaseRecorded = -1L;
		classOfLastBombReleaseRecorded = null;
		bDroptanksDropped = false;
		targetDegreeAITakeoffRotation = -1.0F;
		targetDegreeAITakeoffClimb = -1.0F;
		targetDegreeAIApproach = -1.0F;
		// --------------------------------------------------------

		// Import values from 4.13.2m
		// --------------------------------------------------------
		bombReleaseMode = B_RELEASE_PAIRS_WINGS_FIRST;
		bombTrainDelay = 0;
		bombTrainAmount = 1;
		bombsReleased = 0;
		timeOfLastBombRelease = -1L;
//		rocketsReleased = 0;
//		timeOfLastRocketRelease = -1L;
		// --------------------------------------------------------
	}

	public void set(Controls controls) {
		PowerControl = controls.PowerControl;
		GearControl = controls.GearControl;
		arrestorControl = controls.arrestorControl;
		FlapsControl = controls.FlapsControl;
		AileronControl = controls.AileronControl;
		ElevatorControl = controls.ElevatorControl;
		RudderControl = controls.RudderControl;
		BrakeControl = controls.BrakeControl;
		BayDoorControl = controls.BayDoorControl;
		AirBrakeControl = controls.AirBrakeControl;
		dvGear = controls.dvGear;
		dvWing = controls.dvWing;
		dvCockpitDoor = controls.dvCockpitDoor;
		dvAirbrake = controls.dvAirbrake;
		// TODO: New parameters
		DiffBrakesType = controls.DiffBrakesType;
		BrakeRightControl = controls.BrakeRightControl;
		BrakeLeftControl = controls.BrakeLeftControl;
		dvCatLaunchBar = controls.dvCatLaunchBar;
		CatLaunchBarControl = controls.CatLaunchBarControl;
		dvDragchute = controls.dvDragchute;
		DragChuteControl = controls.DragChuteControl;
		dvRefuel = controls.dvRefuel;
		RefuelControl = controls.RefuelControl;
		dvVarWing = controls.dvVarWing;
		VarWingControl = controls.VarWingControl;
		VarWingControlSwitch = controls.VarWingControlSwitch;
		dvBlownFlaps = controls.dvBlownFlaps;
		BlownFlapsControl = controls.BlownFlapsControl;
		FlapsControlSwitch = controls.FlapsControlSwitch;
	}

	public void CalcTresholds() {
		AilThr3 = AilThr * AilThr * AilThr;
		RudThr2 = RudThr * RudThr;
		ElevThr2 = ElevThr * ElevThr;
	}

	public void setLanded() {
		if (bHasGearControl)
			GearControl = Gear = gearC = gearR = gearL = 1.0F;
		else
			Gear = gearC = gearR = gearL = 1.0F;
		FlapsControl = Flaps = 0.0F;
		StepControl = 1.0F;
		bStepControlAuto = true;
		bRadiatorControlAuto = true;
		BayDoorControl = 0.0F;
		AirBrakeControl = 0.0F;
		// TODO: New parameter
		CatLaunchBarControl = 0.0F;
		DragChuteControl = 0.0F;
		RefuelControl = 0.0F;
		VarWingControl = VarWing = 0.0F;
		VarWingControlSwitch = 0;
		BlownFlapsControl = BlownFlaps = 0.0F;
		FlapsControlSwitch = 0;
	}

	public void setFixedGear(boolean flag) {
		if (flag) {
			Gear = 1.0F;
			gearC = gearL = gearR = 1.0F;
			GearControl = 0.0F;
		}
	}

	public void setGearAirborne() {
		if (bHasGearControl)
			GearControl = Gear = gearC = gearL = gearR = 0.0F;
	}

	public void setGear(float f) {
		Gear = gearR = gearL = gearC = f;
	}

	public void setGearBraking() {
		Brake = 1.0F;
	}

	public void forceFlaps(float f) {
		Flaps = f;
	}

	// TODO
	public void forceVarWing(float f) {
		VarWing = f;
	}

	public void forceGear(float f) {
		if (bHasGearControl)
			Gear = GearControl = gearC = gearR = gearL = f;
		else
			setFixedGear(true);
	}

	public void forceWing(float f) {
		wing = f;
		FM.doRequestFMSFX(1, (int)(100F * f));
		((Aircraft)FM.actor).moveWingFold(f);
	}

	public void forceArrestor(float f) {
		arrestor = f;
		((Aircraft)FM.actor).moveArrestorHook(f);
	}

	public void setPowerControl(float f) {
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.1F)
			f = 1.1F;
		PowerControl = f;
		// TODO: Increased to 10 to allow aircraft with up to 10 engines to work
		//       Use MAX_ENGINE_NUM static int value for future more expanding
		// --------------------------------------------------------
		for (int i = 0; i < MAX_ENGINE_NUM; i++)
			if (i < FM.EI.getNum() && FM.EI.bCurControl[i])
				PowerControlArr[i] = f;
		// --------------------------------------------------------

	}

	public void setPowerControl(float f, int i) {
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.1F)
			f = 1.1F;
		PowerControlArr[i] = f;
		if (i == 0)
			PowerControl = f;
	}

	public float getPowerControl() {
		return PowerControl;
	}

	public float getPowerControl(int i) {
		return PowerControlArr[i];
	}

	public void setStepControl(float f) {
		if (!bUseElectricProp) {
			if (f > 1.0F)
				f = 1.0F;
			if (f < 0.0F)
				f = 0.0F;
			StepControl = f;
			// TODO: Expanded to allow for 10 engines
			//       Use MAX_ENGINE_NUM static int value for future more expanding
			// --------------------------------------------------------
			for (int i = 0; i < MAX_ENGINE_NUM; i++)
				if (i < FM.EI.getNum() && FM.EI.bCurControl[i])
					StepControlArr[i] = f;
			// --------------------------------------------------------
			HUD.log(AircraftHotKeys.hudLogPowerId, "PropPitch",
					new Object[] { new Integer(Math.round(getStepControl() * 100F)) });
		}
	}

	public void setStepControl(float f, int i) {
		if (!bUseElectricProp) {
			if (f > 1.0F)
				f = 1.0F;
			if (f < 0.0F)
				f = 0.0F;
			StepControlArr[i] = f;
			if (!getStepControlAuto(i))
				HUD.log(AircraftHotKeys.hudLogPowerId, "PropPitch",
						new Object[] { new Integer(Math.round(getStepControl(i) * 100F)) });
		}
	}

	public void setStepControlAuto(boolean flag) {
		bStepControlAuto = flag;
	}

	public float getStepControl() {
		return StepControl;
	}

	public boolean getStepControlAuto() {
		return bStepControlAuto;
	}

	public boolean getStepControlAuto(int i) {
		if (i < FM.EI.getNum())
			return !FM.EI.engines[i].isHasControlProp() || FM.EI.engines[i].getControlPropAuto();
		else
			return true;
	}

	public float getStepControl(int i) {
		return StepControlArr[i];
	}

	public void setElectricPropUp(boolean flag) {
		if (bUseElectricProp)
			if (flag)
				electricPropUp = 1;
			else
				electricPropUp = 0;
	}

	public void setElectricPropDn(boolean flag) {
		if (bUseElectricProp)
			if (flag)
				electricPropDn = 1;
			else
				electricPropDn = 0;
	}

	public void setRadiatorControl(float f) {
		if (f > 1.0F)
			f = 1.0F;
		if (f < 0.0F)
			f = 0.0F;
		RadiatorControl = f;
	}

	public void setRadiatorControlAuto(boolean flag, EnginesInterface enginesinterface) {
		bRadiatorControlAuto = flag;
		if (enginesinterface.getFirstSelected() != null)
			radiator = enginesinterface.getFirstSelected().getControlRadiator();
	}

	public float getRadiatorControl() {
		return RadiatorControl;
	}

	public boolean getRadiatorControlAuto() {
		return bRadiatorControlAuto;
	}

	public float getRadiator() {
		return radiator;
	}

	public void setMixControl(float f) {
		if (f < 0.0F)
			f = 0.0F;
		if (f > 1.2F)
			f = 1.2F;
		MixControl = f;
	}

	public float getMixControl() {
		return MixControl;
	}

	public void setAfterburnerControl(boolean flag) {
		afterburnerControl = flag;
	}

	public boolean getAfterburnerControl() {
		return afterburnerControl;
	}

	public void setMagnetoControl(int i) {
		if (i < 0)
			i = 0;
		if (i > 3)
			i = 3;
		MagnetoControl = i;
	}

	public int getMagnetoControl() {
		return MagnetoControl;
	}

	public void setCompressorControl(int i) {
		if (i < 0)
			i = 0;
		if (i > FM.EI.engines[0].compressorMaxStep)
			i = FM.EI.engines[0].compressorMaxStep;
		CompressorControl = i;
	}

	public int getCompressorControl() {
		return CompressorControl;
	}

	public void setTrimAileronControl(float f) {
		trimAileronControl = f;
	}

	public float getTrimAileronControl() {
		return trimAileronControl;
	}

	public void setTrimElevatorControl(float f) {
		trimElevatorControl = f;
	}

	public float getTrimElevatorControl() {
		return trimElevatorControl;
	}

	public void setTrimRudderControl(float f) {
		trimRudderControl = f;
	}

	public float getTrimRudderControl() {
		return trimRudderControl;
	}

	public void interpolate(Controls controls, float f) {
		PowerControl = FMMath.interpolate(PowerControl, controls.PowerControl, f);
		FlapsControl = FMMath.interpolate(FlapsControl, controls.FlapsControl, f);
		AileronControl = FMMath.interpolate(AileronControl, controls.AileronControl, f);
		ElevatorControl = FMMath.interpolate(ElevatorControl, controls.ElevatorControl, f);
		RudderControl = FMMath.interpolate(RudderControl, controls.RudderControl, f);
		BrakeControl = FMMath.interpolate(BrakeControl, controls.BrakeControl, f);
		// TODO: Engine mod
		BrakeRightControl = FMMath.interpolate(BrakeRightControl, controls.BrakeRightControl, f);
		BrakeLeftControl = FMMath.interpolate(BrakeLeftControl, controls.BrakeLeftControl, f);
		VarWingControl = FMMath.interpolate(VarWingControl, controls.VarWingControl, f);
		// ---- Engine mod
	}

	public float getGearL() {
		return getGears(0, gearL);
	}

	public float getGearR() {
		return getGears(1, gearR);
	}

	public float getGearC() {
		return getGears(2, gearC);
	}

	private float getGears(int i, float f) {
		float f1 = FM.AS.gearStates[i];
		if (f1 == 0.0F || f1 == -1F || f1 == 1.0F)
			return f;
		if (FM.AS.isMaster() && f1 < 0.0F && FM.AS.gearDamRecoveryStates[i] >= 3 && Gear > Math.abs(f1))
			FM.AS.hitGear(FM.actor, i, 11);
		return f;
	}

	public float getGear() {
		return Gear;
	}

	public float getWing() {
		return wing;
	}

	public float getCockpitDoor() {
		return cockpitDoor;
	}

	public void forceCockpitDoor(float f) {
		cockpitDoor = f;
	}

	public float getArrestor() {
		return arrestor;
	}

	public float getFlap() {
		return Flaps;
	}

	public float getAileron() {
		return Ailerons;
	}

	public float getElevator() {
		return Elevators;
	}

	public float getRudder() {
		return Rudder;
	}

	public float getBrake() {
		return Brake;
	}

	public float getPower() {
		return Power;
	}

	public float getStep() {
		return Step;
	}

	public float getBayDoor() {
		return BayDoorControl;
	}

	public float getAirBrake() {
		return airBrake;
	}

	// TODO: New method for drag chute and refuelling equipment control
	// --------------------------------------------------------
	public float getDragChute() {
		return dragChute;
	}

	public float getRefuel() {
		return refuel;
	}

	public float getVarWing() {
		return VarWing;
	}

	public float getBlownFlaps() {
		return BlownFlaps;
	}

	public float getCatLaunchBar() {
		return catLaunchBar;
	}

	public long getLastBombReleaseTime() {
		return timeOfLastBombReleaseRecorded;
	}

	public Class getLastBombReleaseClass() {
		return classOfLastBombReleaseRecorded;
	}

	// --------------------------------------------------------

	private float filter(float f, float f1, float f2, float f3, float f4) {
		float f5 = (float)Math.exp(-f / f3);
		float f6 = f1 + (f2 - f1) * f5;
		if (f6 < f1) {
			f6 += f4 * f;
			if (f6 > f1)
				f6 = f1;
		} else if (f6 > f1) {
			f6 -= f4 * f;
			if (f6 < f1)
				f6 = f1;
		}
		return f6;
	}

	private float clamp01(float f) {
		if (f < 0.0F)
			f = 0.0F;
		else if (f > 1.0F)
			f = 1.0F;
		return f;
	}

	private float clamp0115(float f) {
		if (f < 0.0F)
			f = 0.0F;
		else if (f > 1.1F)
			f = 1.1F;
		return f;
	}

	private float clamp11(float f) {
		if (f < -1F)
			f = -1F;
		else if (f > 1.0F)
			f = 1.0F;
		return f;
	}

	private float clampA(float f, float f1) {
		if (f < -f1)
			f = -f1;
		else if (f > f1)
			f = f1;
		return f;
	}

	// TODO: Method for selecting side door
	// --------------------------------------------------------
	public void setActiveDoor(int i) {
		if (i != SIDE_DOOR && bMoveSideDoor) {
			fSaveSideDoor = cockpitDoor;
			fSaveSideDoorControl = cockpitDoorControl;
			cockpitDoor = fSaveCockpitDoor;
			cockpitDoorControl = fSaveCockpitDoorControl;
			bMoveSideDoor = false;
		} else if (i == SIDE_DOOR && !bMoveSideDoor) {
			fSaveCockpitDoor = cockpitDoor;
			fSaveCockpitDoorControl = cockpitDoorControl;
			cockpitDoor = fSaveSideDoor;
			cockpitDoorControl = fSaveSideDoorControl;
			bMoveSideDoor = true;
		}
	}

	// --------------------------------------------------------

	public void update(float f, float f1, EnginesInterface enginesinterface, boolean flag) {
		update(f, f1, enginesinterface, flag, false);
	}

	private float getDamMovSpd(int i, int j) {
		switch (i) {
		case 1: // '\001'
			return j != 0 ? dvGearR : dvGearL;

		case 5: // '\005'
			return j != 0 ? dvGearR * 15F : dvGearL * 15F;

		case 6: // '\006'
			return j != 0 ? dvGearR * 5F : dvGearL * 5F;

		case 8: // '\b'
			return j != 0 ? dvGearR : dvGearL;

		case 4: // '\004'
			if (j == 0)
				return dvGear * 0.4F;
			else
				return dvGear * 0.6F;

		case 7: // '\007'
			return j != 0 ? dvGearR : dvGearL;

		case 3: // '\003'
			return j != 0 ? dvGearR : dvGearL;

		case 2: // '\002'
			return j != 0 ? dvGearR : dvGearL;

		case 10: // '\n'
			return j != 0 ? dvGearR * 0.1F : dvGearL * 0.2F;

		case 9: // '\t'
		default:
			return dvGear;
		}
	}

	public void update(float f, float fVflowx, EnginesInterface enginesinterface, boolean flag, boolean flag1) {
		if (bHasBayDoors)
			bHasBayDoorControl = true;
		float fAilLimit = 1.0F;
		float fRudLimit = 1.0F;
		float fElevLimit = 1.0F;
		float fVflowx2 = fVflowx * fVflowx;
		if (fVflowx > AilThr && !bHasAileronControlPowered)
			fAilLimit = Math.max(0.2F, AilThr3 / (fVflowx2 * fVflowx));
		if (fVflowx2 > RudThr2 && !bHasRudderControlPowered)
			fRudLimit = Math.max(0.2F, RudThr2 / fVflowx2);
		if (fVflowx2 > ElevThr2 && !bHasElevatorControlPowered)
			fElevLimit = Math.max(0.4F, ElevThr2 / fVflowx2);
		fAilLimit *= Sensitivity;
		fRudLimit *= Sensitivity;
		fElevLimit *= Sensitivity;
		if (!flag1)
			if (FM instanceof RealFlightModel) {
				float f6 = 0.0F;
				for (int j1 = 0; j1 < enginesinterface.getNum(); j1++) {
					PowerControlArr[j1] = clamp0115(PowerControlArr[j1]);
					enginesinterface.engines[j1].setControlThrottle(PowerControlArr[j1]);
					if (PowerControlArr[j1] > f6)
						f6 = PowerControlArr[j1];
				}

				if (flag) {
					Power = f6;
				} else {
					Power = filter(f, f6, Power, 5F, 0.01F * f);
					enginesinterface.setThrottle(Power);
				}
			} else {
				PowerControl = clamp0115(PowerControl);
				if (flag)
					Power = PowerControl;
				else
					Power = filter(f, PowerControl, Power, 5F, 0.01F * f);
				enginesinterface.setThrottle(Power);
			}
		if (!flag1)
			enginesinterface.setAfterburnerControl(afterburnerControl);
		if (!flag1) {
			StepControl = clamp01(StepControl);
			if (bUseElectricProp && (FM instanceof RealFlightModel)) {
				enginesinterface.setPropAuto(bStepControlAuto);
				int i = electricPropUp - electricPropDn;
				if (i < 0)
					HUD.log(AircraftHotKeys.hudLogPowerId, "elPropDn");
				else if (i > 0)
					HUD.log(AircraftHotKeys.hudLogPowerId, "elPropUp");
				enginesinterface.setPropDelta(i);
			}
			if (enginesinterface.getFirstSelected() != null)
				if (bStepControlAuto) {
					if (enginesinterface.isSelectionAllowsAutoProp()) {
						enginesinterface.setPropAuto(true);
					} else {
						enginesinterface.setPropAuto(false);
						bStepControlAuto = false;
					}
				} else if (FM instanceof RealFlightModel) {
					if (!bUseElectricProp) {
						for (int j = 0; j < enginesinterface.getNum(); j++) {
							StepControlArr[j] = clamp01(StepControlArr[j]);
							enginesinterface.engines[j].setControlPropAuto(false);
							enginesinterface.engines[j].setControlProp(StepControlArr[j]);
						}

					}
				} else {
					Step = filter(f, StepControl, Step, 0.2F, 0.02F);
					enginesinterface.setPropAuto(false);
					enginesinterface.setProp(Step);
				}
		}
		RadiatorControl = clamp01(RadiatorControl);
		radiator = filter(f, RadiatorControl, radiator, 999.9F, 0.2F);
		if (bRadiatorControlAuto && enginesinterface.getFirstSelected() != null) {
			if (enginesinterface.isSelectionAllowsAutoRadiator()) {
				enginesinterface.updateRadiator(f);
			} else {
				enginesinterface.setRadiator(radiator);
				bRadiatorControlAuto = false;
			}
		} else {
			enginesinterface.setRadiator(radiator);
		}
		if (!flag1)
			enginesinterface.setMagnetos(MagnetoControl);
		if (!flag1 && flag && World.cur().diffCur.ComplexEManagement)
			enginesinterface.setCompressorStep(CompressorControl);
		if (!flag1)
			enginesinterface.setMix(MixControl);
		if (bHasGearControl || flag1) {
			GearControl = clamp01(GearControl);
			Gear = filter(f, GearControl, Gear, 999.9F, dvGear);
			float fGearStatL = FM.AS.gearStates[0];
			float fGearStatR = FM.AS.gearStates[1];
			float fGearStatC = FM.AS.gearStates[2];
			if (fGearStatL > 0.0F)
				gearL = filter(f, fGearStatL, gearL, 999.9F, getDamMovSpd(FM.AS.gearDamType[0], 0));
			else if (fGearStatL < 0.0F)
				gearL = filter(f, Math.min(GearControl, Math.abs(fGearStatL)), gearL, 999.9F, getDamMovSpd(FM.AS.gearDamType[0], 0));
			else
				gearL = filter(f, GearControl, gearL, 999.9F, dvGearL);
			if (fGearStatR > 0.0F)
				gearR = filter(f, fGearStatR, gearR, 999.9F, getDamMovSpd(FM.AS.gearDamType[1], 1));
			else if (fGearStatR < 0.0F)
				gearR = filter(f, Math.min(GearControl, Math.abs(fGearStatR)), gearR, 999.9F, getDamMovSpd(FM.AS.gearDamType[1], 1));
			else
				gearR = filter(f, GearControl, gearR, 999.9F, dvGearR);
			if (fGearStatC > 0.0F)
				gearC = filter(f, fGearStatC, gearC, 999.9F, getDamMovSpd(FM.AS.gearDamType[2], 2));
			else if (fGearStatC < 0.0F)
				gearC = filter(f, Math.min(GearControl, Math.abs(fGearStatC)), gearC, 999.9F, getDamMovSpd(FM.AS.gearDamType[2], 2));
			else
				gearC = filter(f, GearControl, gearC, 999.9F, dvGearC);
		}
		if (bHasAirBrakeControl || flag1)
			airBrake = filter(f, AirBrakeControl, airBrake, 999.9F, dvAirbrake);
		// TODO: For drag chute, refuelling, variable geometry wings and blown
		// flaps
		// --------------------------------------------------------
		if (bHasCatLaunchBarControl || flag1)
			catLaunchBar = filter(f, CatLaunchBarControl, catLaunchBar, 999.9F, dvCatLaunchBar);
		if (bHasDragChuteControl || flag1)
			dragChute = filter(f, DragChuteControl, dragChute, 999.9F, dvDragchute);
		if (bHasRefuelControl || flag1)
			refuel = filter(f, RefuelControl, refuel, 999.9F, dvRefuel);
		if (bHasVarWingControl || flag1) {
			VarWingControl = clamp01(VarWingControl);
			VarWing = filter(f, VarWingControl, VarWing, 999.9F, dvVarWing);
		}
		if (bHasBlownFlaps || flag1) {
			BlownFlapsControl = clamp01(BlownFlapsControl);
			BlownFlaps = filter(f, BlownFlapsControl, BlownFlaps, 999.9F, dvBlownFlaps);
		}
		// --------------------------------------------------------
		if (bHasWingControl) {
			wing = filter(f, wingControl, wing, 999.9F, dvWing);
			if (wing > 0.01F && wing < 0.99F)
				FM.doRequestFMSFX(1, (int)(100F * wing));
		}
		if (bHasCockpitDoorControl)
			cockpitDoor = filter(f, cockpitDoorControl, cockpitDoor, 999.9F, dvCockpitDoor);
		if ((bHasArrestorControl || flag1) && (arrestorControl == 0.0F || arrestorControl == 1.0F))
			arrestor = filter(f, arrestorControl, arrestor, 999.9F, 0.2F);
		if (bHasFlapsControl || flag1) {
			FlapsControl = clamp01(FlapsControl);
			if (Flaps > FlapsControl) {
				if ((Aircraft)FM.actor instanceof TypeFastJet)
					Flaps = filter(f, FlapsControl, Flaps, 999F, 0.25F);
				else
					Flaps = filter(f, FlapsControl, Flaps, 999F, Aircraft.cvt(FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.25F));
			}
			else
			{
				if ((Aircraft)FM.actor instanceof TypeFastJet)
					Flaps = filter(f, FlapsControl, Flaps, 999F, Aircraft.cvt(FM.getSpeedKMH(), 250F, 400F, 0.16F, 0.11F));
				else
					Flaps = filter(f, FlapsControl, Flaps, 999F, Aircraft.cvt(FM.getSpeedKMH(), 150F, 280F, 0.15F, 0.02F));
			}
		}
		if (StabilizerControl) {
			AileronControl = -0.2F * FM.Or.getKren() - 2.0F * (float)FM.getW().x;
			tmpV3d.set(FM.Vwld);
			tmpV3d.normalize();
			float f8 = (float)(-500D * (tmpV3d.z - 0.001D));
			if (f8 > 0.8F)
				f8 = 0.8F;
			if (f8 < -0.8F)
				f8 = -0.8F;
			ElevatorControl = (f8 - 0.2F * FM.Or.getTangage() - 0.05F * FM.AOA) + 25F * (float)FM.getW().y;
			RudderControl = -0.2F * FM.AOS + 20F * (float)FM.getW().z;
			// System.out.print("ElevC=" + ElevatorControl);
			// System.out.print("RuddC=" + RudderControl);
			// System.out.print(" f8=" + f8);
			// System.out.print(" getTangage=" + FM.Or.getTangage());
			// System.out.print(" AOA=" + FM.AOA);
			// System.out.print(" getW().y=" + FM.getW().y);
			// System.out.print(" AOS=" + FM.AOS);
			// System.out.println(" getW().z=" + FM.getW().z);
		}
		// TODO: +++ by western, limit Ailerons in high speed flight, simulating A-6 or F-5, etc.
		boolean bLimitEnabled = false;
		switch(limitModeOfAilerons) {
		case 1:
			if(FM.getSpeedKMH() > limitThresholdSpeedKMH) bLimitEnabled = true;
			break;
		case 2:
			if(getFlap() < 0.01F) bLimitEnabled = true;
			break;
		case 3:
			if(FM.getSpeedKMH() > limitThresholdSpeedKMH && getFlap() < 0.01F) bLimitEnabled = true;
			break;
		case 4:
			if(getGear() < 0.01F) bLimitEnabled = true;
			break;
		case 5:
			if(FM.getSpeedKMH() > limitThresholdSpeedKMH && getGear() < 0.01F) bLimitEnabled = true;
			break;
		case 6:
			if(getFlap() < 0.01F && getGear() < 0.01F) bLimitEnabled = true;
			break;
		case 7:
			if(FM.getSpeedKMH() > limitThresholdSpeedKMH && getFlap() < 0.01F && getGear() < 0.01F)
				bLimitEnabled = true;
			break;
		default:
			break;
		}
		// TODO: --- end of limit Ailerons
		if (bHasAileronControl || flag1) {
			trimAileron = filter(f, trimAileronControl, trimAileron, 999.9F, 0.25F);
			AileronControl = clamp11(AileronControl);
			tmpF = clampA(AileronControl, fAilLimit);
			Ailerons = filter(f, (1.0F + (trimAileron * tmpF <= 0.0F ? 1.0F : -1F) * Math.abs(trimAileron)) * tmpF
					+ trimAileron, Ailerons, 0.2F * (1.0F + 0.3F * Math.abs(AileronControl)), 0.025F);
			// TODO: by western
			if(bLimitEnabled) Ailerons = clampA(Ailerons, limitRatioAileron);
		}
		if (bHasElevatorControl || flag1) {
			trimElevator = filter(f, trimElevatorControl, trimElevator, 999.9F, 0.25F);
			ElevatorControl = clamp11(ElevatorControl);
			tmpF = clampA(ElevatorControl, fElevLimit);
			Ev = filter(f, (1.0F + (trimElevator * tmpF <= 0.0F ? 1.0F : -1F) * Math.abs(trimElevator)) * tmpF + trimElevator,
					Ev, 0.3F * (1.0F + 0.3F * Math.abs(ElevatorControl)), 0.022F);
			if (FM.actor instanceof SU_26M2)
				Elevators = clamp11(Ev);
			else
				Elevators = clamp11(Ev - 0.25F * (1.0F - fElevLimit));
			// TODO: by western
			// Elevator can have different limit plus / minus
			if(bLimitEnabled) {
				if(Elevators < -limitRatioElevatorMinus) Elevators = -limitRatioElevatorMinus;
				if(Elevators > limitRatioElevatorPlus) Elevators = limitRatioElevatorPlus;
			} else {
				// Elevator can have always minus limit to simulate A-6 etc.
				if(Elevators < -limitRatioElevatorMinusALWAYS) Elevators = -limitRatioElevatorMinusALWAYS;
			}
		}
		if (bHasRudderControl || flag1) {
			trimRudder = filter(f, trimRudderControl, trimRudder, 999.9F, 0.25F);
			RudderControl = clamp11(RudderControl);
			tmpF = clampA(RudderControl, fRudLimit);
			Rudder = filter(f, (1.0F + (trimRudder * tmpF <= 0.0F ? 1.0F : -1F) * Math.abs(trimRudder)) * tmpF + trimRudder,
					Rudder, 0.35F * (1.0F + 0.3F * Math.abs(RudderControl)), 0.025F);
			// TODO: by western
			// Rudder limitter becomes canceled when gears on the ground.
			if(bLimitEnabled && FM.Gears.nOfGearsOnGr < 2) Rudder = clampA(Rudder, limitRatioRudder);
		}
		BrakeControl = clamp01(BrakeControl);
		BrakeLeftControl = clamp01(BrakeLeftControl);
		BrakeRightControl = clamp01(BrakeRightControl);
		if (bHasBrakeControl || flag1) {
			if (BrakeControl > Brake)
				Brake = Brake + 0.3F * f;
			else
				Brake = BrakeControl;
		} else {
			Brake = 0.0F;
		}
		// TODO: Edits for differential braking
		// --------------------------------------------------------
		if (bHasBrakeControl || flag1) {
			if (BrakeRightControl > BrakeRight)
				BrakeRight = BrakeRight + 0.3F * f;
			else
				BrakeRight = BrakeRightControl;
		} else {
			BrakeRight = 0.0F;
		}
		if (bHasBrakeControl || flag1) {
			if (BrakeLeftControl > BrakeLeft)
				BrakeLeft = BrakeLeft + 0.3F * f;
			else
				BrakeLeft = BrakeLeftControl;
		} else {
			BrakeLeft = 0.0F;
		}
		// --------------------------------------------------------
		if (tick == Time.tickCounter())
			return;
		tick = Time.tickCounter();
		CTL = (byte)((GearControl <= 0.5F ? 0 : 1)
				| (FlapsControl >= 0.25F ? ((byte)(FlapsControl >= 0.66F ? 6 : 4)) : FlapsControl <= 0.1F ? 0 : 2)
				| (BrakeControl <= 0.2F ? 0 : 8) | (RadiatorControl <= 0.5F ? 0 : 0x10) | (BayDoorControl <= 0.5F ? 0 : 0x20) | (AirBrakeControl <= 0.5F ? 0
				: 0x40));
		WCT &= 0xfc;
		TWCT &= 0xfc;
		int k = 0;
		for (int j2 = 1; k < WeaponControl.length && j2 < 256; j2 <<= 1) {
			if (WeaponControl[k]) {
				WCT |= j2;
				TWCT |= j2;
			}
			k++;
		}

		for (int l = 0; l < 4; l++)
			saveWeaponControl[l] = WeaponControl[l];
		// TODO: Start of weapon control & release mod
		// --------------------------------------------------------
		long delay = weaponReleaseDelay;
		if (Time.speed() == 0.5D) {
			delay *= 2L;
		} else if (Time.speed() == 0.25D) {
			delay *= 4L;
		} else
			delay /= (long)Time.speed();
		for (int weaponTrigger = 0; weaponTrigger < Weapons.length; weaponTrigger++) {
			if (Weapons[weaponTrigger] != null) {
				switch (weaponTrigger) {
				case 3: // default for bombs, not valid for missiles with
						// GuidedMissileUtils support
					// +++ Import from 4.13.2m
					boolean flag3 = WeaponControl[weaponTrigger];
					if (!flag3)
						break;
					if (bDropWithPlayer) {
						bDropWithMe = true;
						bDropWithPlayer = false;
					}
					doBombRelease();
					break;
					// --- Import from 4.13.2m
				case 2: // default for missiles
					// TODO: ++ Added/changed Code Multiple Missile Type
					// Selection ++
				case 4: // Triggers 4, 5 and 6 hold missiles and/or bombs, too.
				case 5:
				case 6:
				// TODO: -- Added/changed Code Multiple Missile Type Selection
				// --
				{
					if (WeaponControl[weaponTrigger]) {
						// HUD.log("Control " + weaponTrigger + " triggered!");
						if (bDropWithPlayer) {
							bDropWithMe = true;
							bDropWithPlayer = false;
						}
						if ((System.currentTimeMillis() > (lWeaponTime + delay))) {
							switch (this.weaponFireMode) {
							case WEAPON_FIRE_MODE_SALVO:
								for (int i = 0; i < Weapons[weaponTrigger].length; i++) {
									this.fireWeapon(weaponTrigger, i, true);
								}
								break;
							case WEAPON_FIRE_MODE_SINGLE:
							default:
								for (int i = 0; i < Weapons[weaponTrigger].length; i++) {
									if (this.fireWeapon(weaponTrigger, i, true))
										break;
								}
								WeaponControl[weaponTrigger] = false;
								break;
		// +++ add Ripple mode by western
							case WEAPON_FIRE_MODE_PAIRS:
							case WEAPON_FIRE_MODE_RIPPLE:
								for (int i = 0; i < Weapons[weaponTrigger].length; i += 2) {
									if (this.fireWeapon(weaponTrigger, i, false))
										break;
								}
								for (int i = 1; i < Weapons[weaponTrigger].length; i += 2) {
									if (this.fireWeapon(weaponTrigger, i, false))
										break;
								}
								if(this.weaponFireMode == WEAPON_FIRE_MODE_PAIRS)
									WeaponControl[weaponTrigger] = false;
								break;
		// --- add Ripple mode by western
							}

							//
							//
							//
							// int dropBullet = -1;
							// for (int evenBullet = 0; evenBullet <
							// Weapons[weaponTrigger].length; evenBullet += 2) {
							// if (!(Weapons[weaponTrigger][evenBullet]
							// instanceof FuelTankGun) &&
							// Weapons[weaponTrigger][evenBullet].haveBullets())
							// {
							// if
							// (Weapons[weaponTrigger][evenBullet].getHookName().startsWith("_BombSpawn")
							// && ((Aircraft) FM.actor).needsOpenBombBay()) {
							// if (bHasBayDoorControl && isPlayers((Aircraft)
							// FM.actor)) {
							// if (BayDoorControl < 0.9F) break;
							// } else {
							// BayDoorControl = 1.0F;
							// }
							// Weapons[weaponTrigger][evenBullet].shots(1);
							// } else {
							// if
							// (Weapons[weaponTrigger][evenBullet].getHookName().startsWith("_InternalRock")
							// && BayDoorControl == 0.0F) break;
							// else if (weaponFireMode == 2 ||
							// (Weapons[weaponTrigger][evenBullet] instanceof
							// RocketGun4andHalfInch) ||
							// ((Weapons[weaponTrigger][evenBullet] instanceof
							// RocketGun) && (iToggleRocketSide == LEFT))
							// || ((Weapons[weaponTrigger][evenBullet]
							// instanceof RocketBombGun) && (iToggleRocketSide
							// == LEFT)) || ((Weapons[weaponTrigger][evenBullet]
							// instanceof BombGun) && (iToggleBombSide ==
							// LEFT))) {
							// Weapons[weaponTrigger][evenBullet].shots(1);
							// }
							// if
							// (Weapons[weaponTrigger][evenBullet].getHookName().startsWith("_BombSpawn")
							// && ((Aircraft) FM.actor).needsOpenBombBay()) if
							// (bHasBayDoorControl && isPlayers((Aircraft)
							// FM.actor)) {
							// if (BayDoorControl < 0.9F) break;
							// } else {
							// BayDoorControl = 1.0F;
							// }
							// }
							// if (((Weapons[weaponTrigger][evenBullet]
							// instanceof BombGun) && weaponFireMode == 0) ||
							// ((Weapons[weaponTrigger][evenBullet] instanceof
							// RocketGun) &&
							// !(Weapons[weaponTrigger][evenBullet] instanceof
							// MissileGun) &&
							// weaponFireMode == 0)) {
							// Weapons[weaponTrigger][evenBullet].shots(1);
							// } else if (((Weapons[weaponTrigger][evenBullet]
							// instanceof BombGun) && !((BombGun)
							// Weapons[weaponTrigger][evenBullet]).isCassette()
							// && weaponFireMode != 0)
							// || ((Weapons[weaponTrigger][evenBullet]
							// instanceof RocketGun) && !((RocketGun)
							// Weapons[weaponTrigger][evenBullet]).isCassette()
							// && weaponFireMode != 0)) {
							// dropBullet = evenBullet;
							// lWeaponTime = System.currentTimeMillis();
							// break;
							// }
							// }
							// }
							// for (int oddBullet = 1; oddBullet <
							// Weapons[weaponTrigger].length; oddBullet += 2) {
							// if (!(Weapons[weaponTrigger][oddBullet]
							// instanceof FuelTankGun) &&
							// Weapons[weaponTrigger][oddBullet].haveBullets())
							// {
							// if
							// (Weapons[weaponTrigger][oddBullet].getHookName().startsWith("_BombSpawn")
							// && ((Aircraft) FM.actor).needsOpenBombBay()) {
							// if (bHasBayDoorControl && isPlayers((Aircraft)
							// FM.actor)) {
							// if (BayDoorControl < 0.9F) break;
							// } else {
							// BayDoorControl = 1.0F;
							//
							// }
							// Weapons[weaponTrigger][oddBullet].shots(1);
							// } else {
							// if
							// (Weapons[weaponTrigger][oddBullet].getHookName().startsWith("_InternalRock")
							// && BayDoorControl == 0.0F) break;
							// else if (weaponFireMode == 2 ||
							// (Weapons[weaponTrigger][oddBullet] instanceof
							// RocketGun4andHalfInch) ||
							// ((Weapons[weaponTrigger][oddBullet] instanceof
							// RocketGun) && (iToggleRocketSide == RIGHT))
							// || ((Weapons[weaponTrigger][oddBullet] instanceof
							// RocketBombGun) && (iToggleRocketSide == LEFT)) ||
							// ((Weapons[weaponTrigger][oddBullet] instanceof
							// BombGun) && (iToggleBombSide == RIGHT))) {
							// Weapons[weaponTrigger][oddBullet].shots(1);
							// }
							// }
							// if (((Weapons[weaponTrigger][oddBullet]
							// instanceof BombGun) && weaponFireMode == 0) ||
							// ((Weapons[weaponTrigger][oddBullet] instanceof
							// RocketGun) && !(Weapons[weaponTrigger][oddBullet]
							// instanceof MissileGun) && weaponFireMode
							// == 0)) {
							// Weapons[weaponTrigger][oddBullet].shots(1);
							// } else if (((Weapons[weaponTrigger][oddBullet]
							// instanceof BombGun) && !((BombGun)
							// Weapons[weaponTrigger][oddBullet]).isCassette()
							// && weaponFireMode != 0)
							// || ((Weapons[weaponTrigger][oddBullet] instanceof
							// RocketGun) && !((RocketGun)
							// Weapons[weaponTrigger][oddBullet]).isCassette()
							// && weaponFireMode != 0)
							// || ((Weapons[weaponTrigger][oddBullet] instanceof
							// RocketBombGun) && !((RocketBombGun)
							// Weapons[weaponTrigger][oddBullet]).isCassette()
							// && weaponFireMode != 0)) {
							// dropBullet = oddBullet;
							// lWeaponTime = System.currentTimeMillis();
							// break;
							// }
							// }
							// }
							// if (dropBullet != -1) {
							// if (Weapons[weaponTrigger][dropBullet] instanceof
							// BombGun) {
							// if (iToggleBombSide == LEFT) iToggleBombSide =
							// RIGHT;
							// else iToggleBombSide = LEFT;
							// } else if (!(Weapons[weaponTrigger][dropBullet]
							// instanceof RocketGun4andHalfInch)) {
							// if (iToggleRocketSide == LEFT) iToggleRocketSide
							// = RIGHT;
							// else iToggleRocketSide = LEFT;
							// }
							// }
							// if (weaponFireMode != 0)
							// WeaponControl[weaponTrigger] = false;
						}
					}
					break;
				}
				default:
					boolean flag2 = false;
					for (int i2 = 0; i2 < Weapons[weaponTrigger].length; i2++) {
						Weapons[weaponTrigger][i2].shots(WeaponControl[weaponTrigger] ? -1 : 0);
						flag2 = flag2 || Weapons[weaponTrigger][i2].haveBullets();
					}

					if (WeaponControl[weaponTrigger] && !flag2 && FM.isPlayers())
						com.maddox.il2.objects.effects.ForceFeedback.fxTriggerShake(weaponTrigger, false);
					break;
				}
			}
		}
		// --------------------------------------------------------
	}

	private boolean fireWeapon(int triggerNum, int hookNum, boolean dropFollowingNulls) {
		BulletEmitter theEmitter = Weapons[triggerNum][hookNum];
		if (theEmitter instanceof FuelTankGun)
			return false;
		if (!theEmitter.haveBullets())
			return false;
		if ((theEmitter.getHookName().startsWith("_BombSpawn") || theEmitter.getHookName().startsWith("_InternalRock")) && ((Aircraft)FM.actor).needsOpenBombBay()) {
			if (bHasBayDoorControl && isPlayers((Aircraft)FM.actor)) {
				if (BayDoorControl < 0.9F)
					return true;
			} else {
				BayDoorControl = 1.0F;
			}
		}
		// if (theEmitter.getHookName().startsWith("_InternalRock") && BayDoorControl == 0.0F)
		//	return true;			 // merged to "_BombSpawn" for Tu-95MS's rotally launcher
		if (!(theEmitter instanceof BombGun) && !(theEmitter instanceof RocketGun) && !(theEmitter instanceof RocketBombGun))
			return false;

		lWeaponTime = System.currentTimeMillis();
		theEmitter.shots(1);
		if (dropFollowingNulls) {
			for (int nullHooks = hookNum + 1; nullHooks < Weapons[triggerNum].length; nullHooks++) {
				theEmitter = Weapons[triggerNum][nullHooks];
				if (theEmitter == null)
					continue;
				if (theEmitter instanceof FuelTankGun)
					continue;
				if (!(theEmitter instanceof BombGunNull) && !(theEmitter instanceof RocketGunNull))
					break;
				if (theEmitter.haveBullets())
					theEmitter.shots(theEmitter.countBullets());
			}
		}
		return true;
	}

	private boolean isPlayers(Aircraft aircraft) {
		if (aircraft.FM.isPlayers() && (aircraft.FM instanceof RealFlightModel) && ((RealFlightModel)aircraft.FM).isRealMode())
			return true;
		return aircraft.isNetPlayer();
	}

	public float getWeaponMass() {
		int i = Weapons.length;
		float f = 0.0F;
		for (int k = 0; k < i; k++) {
			if (Weapons[k] == null)
				continue;
			int l = Weapons[k].length;
			for (int j = 0; j < l; j++) {
				BulletEmitter bulletemitter = Weapons[k][j];
				if (bulletemitter == null || (bulletemitter instanceof FuelTankGun))
					continue;
				int i1 = bulletemitter.countBullets();
				if (i1 < 0) {
					i1 = 1;
					if ((bulletemitter instanceof BombGun) && ((BombGun)bulletemitter).isCassette())
						i1 = 10;
				}
				f += bulletemitter.bulletMassa() * (float)i1;
			}

		}

		return f;
	}

	public int getWeaponCount(int i) {
		if (i >= Weapons.length || Weapons[i] == null)
			return 0;
		int l = Weapons[i].length;
		int k = 0;
		int j = 0;
		for (; k < l; k++) {
			BulletEmitter bulletemitter = Weapons[i][k];
			if (bulletemitter != null && !(bulletemitter instanceof FuelTankGun))
				j += bulletemitter.countBullets();
		}
		if (j < 0) j = -1;	// TODO: by western, debug Unlimited-Ammo

		return j;
	}

			// TODO: Weapon counter with trigger number and its classname
			// --------------------------------------------------------
	public int getWeaponCount(int i, Class class1) {
		if (i >= Weapons.length || Weapons[i] == null)
			return 0;
		int l = Weapons[i].length;
		int k;
		int j = k = 0;
		for (; k < l; k++) {
			BulletEmitter bulletemitter = Weapons[i][k];
			if (bulletemitter == null || !(bulletemitter instanceof BombGun)) continue;
			if (((BombGun)bulletemitter).bulletClass() == class1)
				j += bulletemitter.countBullets();
			else if (Property.stringValue(((BombGun)bulletemitter).bulletClass(), "groupLeader", null) != null) {
				boolean mBreak = false;
				for (int m = 0; m < B_CLASSES_MAX && !mBreak; m++) {
					if (bombClassArr[m] == class1) {
						for (int n = 0; n < B_SUBCLASSES_MAX; n++) {
							if (bombLeaderClassRvsIdx[m][n] == ((BombGun)bulletemitter).bulletClass()) {
								j += bulletemitter.countBullets();
								mBreak = true;
								break;
							}
						}
					}
				}
			}
		}
		if (j < 0) j = -1;	// TODO: by western, debug Unlimited-Ammo

		return j;
	}
			// --------------------------------------------------------

			// TODO: Bomb class exist in the array?
			// --------------------------------------------------------
	private boolean getBombClassExist(Class class1) {
		if (bombClassNumber == 0)
			return false;
		String leaderName = Property.stringValue(class1, "groupLeader", null);
		if (leaderName == null) {
			for (int i = 0; i < bombClassNumber; i++) {
				if (bombClassArr[i] == class1)
					return true;
			}
		} else {
			Class classLeader = null;
			try {
				classLeader = Class.forName("com.maddox.il2.objects.weapons." + leaderName);
			} catch(ClassNotFoundException classnotfoundexception) {
				System.out.println("Error: " + class1.getName().substring(31) + "'s groupLeader value '" + leaderName + "' is invalid.");
				throw new NoClassDefFoundError(classnotfoundexception.getMessage());
			}
			for (int i = 0; i < bombClassNumber; i++) {
				if (bombClassArr[i] == classLeader)
					return true;
			}
		}

		return false;
	}

	private int getBombClassIndex(Class class1) {
		if (bombClassNumber == 0)
			return -1;
		String leaderName = Property.stringValue(class1, "groupLeader", null);
		if (leaderName == null) {
			for (int i = 0; i < bombClassNumber; i++) {
				if (bombClassArr[i] == class1)
					return i;
			}
		} else {
			for (int i = 0; i < bombClassNumber; i++) {
				if (bombClassArr[i].getName().substring(31) == leaderName)
					return i;
			}
		}

		return -1;
	}

	private boolean isMatchBombSelectedNum(int selectedNum, Class class1) {
		if (bombClassArr[selectedNum] == null)
			return false;
		if (bombClassArr[selectedNum] == class1)
			return true;

		if (Property.stringValue(class1, "groupLeader", null) != null) {
			for (int n = 0; n < B_SUBCLASSES_MAX; n++) {
				if (bombLeaderClassRvsIdx[selectedNum][n] == null)
					break;
				if (bombLeaderClassRvsIdx[selectedNum][n] == class1)
					return true;
			}
		}

		return false;
	}

			// TODO: Add a new Bomb class to the array
			// --------------------------------------------------------
	private boolean addBombClass(Class class1) {
		if (bombClassNumber == bombClassArr.length)
			return false;
		if (getBombClassExist(class1))
			return false;
		String leaderName = Property.stringValue(class1, "groupLeader", null);
		if (leaderName == null) {
			bombClassArr[bombClassNumber] = class1;
		} else {
			Class classLeader = null;
			try {
				classLeader = Class.forName("com.maddox.il2.objects.weapons." + leaderName);
			} catch(ClassNotFoundException classnotfoundexception) {
				System.out.println("Error: " + class1.getName().substring(31) + "'s groupLeader value '" + leaderName + "' is invalid.");
				throw new NoClassDefFoundError(classnotfoundexception.getMessage());
			}
			bombClassArr[bombClassNumber] = classLeader;
			for (int i = 0; i < B_CLASSES_MAX; i++) {
				if (bombLeaderClassRvsIdx[bombClassNumber][i] == null) {
					bombLeaderClassRvsIdx[bombClassNumber][i] = class1;
					break;
				} else if (bombLeaderClassRvsIdx[bombClassNumber][i] == class1)
					break;
			}
		}
		bombClassNumber++;

		return true;
	}

			// TODO: initialize register of the Bombs class array
			// --------------------------------------------------------
	public boolean registerBombs() {
		for (int i = 0; i < bombClassArr.length; i++)
			bombClassArr[i] = null;
		for (int k = 0; k < B_CLASSES_MAX; k++)
			for (int j = 0; j < B_SUBCLASSES_MAX; j++)
				bombLeaderClassRvsIdx[k][j] = null;
		bombClassNumber = 0;

		if (Weapons[3] == null || Weapons[3].length == 0)
			return false;

		for (int i = 0; i < Weapons[3].length; i++) {
			if (Weapons[3][i] == null || Weapons[3][i] instanceof FuelTankGun || Weapons[3][i] instanceof BombGunNull || !(Weapons[3][i] instanceof BombGun))
				continue;

			addBombClass(((BombGun)Weapons[3][i]).bulletClass());
			int index = getBombClassIndex(((BombGun)Weapons[3][i]).bulletClass());
			if (index != -1) bombsHangedNumberByClass[index]++;
		}
		if (bombClassNumber > 0) {
			if (bombClassNumber == 1) curBombSelected = 0;
			else curBombSelected = -1;
			bombReleaseMode = 0;
			setBombModeDefaults();
			return true;
		}

		return false;
	}
			// --------------------------------------------------------

			// TODO: number of the valid Bombs class array
			// --------------------------------------------------------
	public int getNumberOfValidBombClass() {
		int count = 0;
		for (int i = 0; i < bombClassNumber; i++)
			if (getWeaponCount(3, bombClassArr[i]) > 0 || (getWeaponCount(3, bombClassArr[i]) == -1 && !World.cur().diffCur.Limited_Ammo)) count++;

		return count;
	}
			// --------------------------------------------------------

	// By western0221: NoJettison flag tanks are ignored to drop. For F-80's wingtip tanks etc.
	public boolean dropFuelTanks() {
		if (bDroptanksDropped) return false;

		boolean flag = false;
		for (int i = 0; i < Weapons.length; i++) {
			if (Weapons[i] == null)
				continue;
			for (int j = 0; j < Weapons[i].length; j++) {
				if ((Weapons[i][j] instanceof FuelTankGun) && Weapons[i][j].haveBullets()) {
					if (Weapons[i][j].getHookName().startsWith("_StaticTank")
						|| ((FuelTankGun) Weapons[i][j]).isNoJettison())
						continue;

					Weapons[i][j].shots(1);
					flag = true;
				}
			}

		}

		if (flag) {
			bDroptanksDropped = true;
			((Aircraft)FM.actor).replicateDropFuelTanks();
			FM.M.onFuelTanksChanged();
		}
		return flag;
	}

	public boolean dropExternalStores(boolean flag) {
		boolean flag1 = ((Aircraft)FM.actor).dropExternalStores(flag);
		if (flag1) {
			FM.AS.externalStoresDropped = true;
			((Aircraft)FM.actor).replicateDropExternalStores();
		}
		return flag1;
	}

	public FuelTank[] getFuelTanks() {
		int i1 = 0;
		for (int i = 0; i < Weapons.length; i++) {
			if (Weapons[i] == null)
				continue;
			for (int k = 0; k < Weapons[i].length; k++) {
				if ((Weapons[i][k] instanceof FuelTankGun) && Weapons[i][k].haveBullets()) {
					if (!bDroptanksDropped
						|| Weapons[i][k].getHookName().startsWith("_StaticTank")
						|| ((FuelTankGun) Weapons[i][k]).isNoJettison()) {
						i1++;
					}
				}
			}

		}

		FuelTank afueltank[] = new FuelTank[i1];
		int j1;
		for (int j = j1 = 0; j < Weapons.length; j++) {
			if (Weapons[j] == null)
				continue;
			for (int l = 0; l < Weapons[j].length; l++)
				if ((Weapons[j][l] instanceof FuelTankGun) && Weapons[j][l].haveBullets()) {
					if (!bDroptanksDropped
						|| Weapons[j][l].getHookName().startsWith("_StaticTank")
						|| ((FuelTankGun) Weapons[j][l]).isNoJettison()) {
						afueltank[j1++] = ((FuelTankGun)Weapons[j][l]).getFuelTank();
					}
				}

		}

		return afueltank;
	}

	public void resetControl(int i) {
		switch (i) {
		case 0: // '\0'
			AileronControl = 0.0F;
			Ailerons = 0.0F;
			trimAileron = 0.0F;
			break;

		case 1: // '\001'
			ElevatorControl = 0.0F;
			Elevators = 0.0F;
			trimElevator = 0.0F;
			break;

		case 2: // '\002'
			RudderControl = 0.0F;
			Rudder = 0.0F;
			trimRudder = 0.0F;
			break;
		}
	}

	public boolean isShooting() {
		for (int i = 0; i < WeaponControl.length; i++)
			if (i != 3 && WeaponControl[i])
				return true;

		return false;
	}

	// TODO: Differential breaking methods
	// ------------------------------------------------------------
	public float getBrakeRight() {
		return BrakeRight;
	}

	public float getBrakeLeft() {
		return BrakeLeft;
	}

	// ------------------------------------------------------------

	// TODO: ++ Added Code for Net Replication ++
	// functions to apply settings off- and online follow here
	public boolean doSetRocketHook(int theRocketHook) {
		rocketHookSelected = theRocketHook;
		if (Weapons[rocketHookSelected] == null)
			return false;

		// TODO: ++ Changed Code to make Engine Mod independent of Guided Missiles Mod ++
		if (!GuidedMissileInterop.getGuidedMissileModExists())
			return false;
		if (GuidedMissileInterop.getMissileGunClass().isInstance(Weapons[rocketHookSelected][0])) {
			try {
				Object theGuidedMissileUtils = Reflection.invokeMethod(FM.actor, "getGuidedMissileUtils");
				Class theMissileClass = ((RocketGun)Weapons[rocketHookSelected][0]).bulletClass();
				Reflection.invokeMethod(theGuidedMissileUtils, "changeMissileClass", new Class[] { Class.class }, new Class[] { theMissileClass });
				rocketNameSelected = (String)Reflection.invokeMethod(theGuidedMissileUtils, "getMissileName");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else
			// TODO: -- Added/changed Code Multiple Missile Type Selection --
		{
			Class theRocketClass = ((RocketGun)Weapons[rocketHookSelected][0]).bulletClass();
			rocketNameSelected = Property.stringValue(theRocketClass, "friendlyName", "Rocket");
		}

		// +++ Import from 4.13.2m
		setWeaponFireModeDefaults();
		// --- Import from 4.13.2m

		// TODO: -- Changed Code to make Engine Mod independent of Guided Missiles Mod --

		// // TODO: ++ Added/changed Code Multiple Missile Type Selection ++
		// if (Weapons[rocketHookSelected][0] instanceof MissileGun) {
		// GuidedMissileUtils theGuidedMissileUtils =
		// ((TypeGuidedMissileCarrier) FM.actor).getGuidedMissileUtils();
		// Class theMissileClass = ((RocketGun)
		// Weapons[rocketHookSelected][0]).bulletClass();
		// theGuidedMissileUtils.changeMissileClass(theMissileClass);
		// rocketNameSelected = theGuidedMissileUtils.getMissileName();
		// } else
		// // TODO: -- Added/changed Code Multiple Missile Type Selection --
		// rocketNameSelected = "Rocket";
		return true;
	}

	public boolean toggleRocketHook() {
		if (rocketHookSelected == 2) {
			if (Weapons[4] != null)
				return doSetRocketHook(4);
			if (Weapons[5] != null)
				return doSetRocketHook(5);
			if (Weapons[6] != null)
				return doSetRocketHook(6);
		} else if (rocketHookSelected == 4) {
			if (Weapons[5] != null)
				return doSetRocketHook(5);
			if (Weapons[6] != null)
				return doSetRocketHook(6);
			if (Weapons[2] != null)
				return doSetRocketHook(2);
		} else if (rocketHookSelected == 5) {
			if (Weapons[6] != null)
				return doSetRocketHook(6);
			if (Weapons[2] != null)
				return doSetRocketHook(2);
			if (Weapons[4] != null)
				return doSetRocketHook(4);
		} else if (rocketHookSelected == 6) {
			if (Weapons[2] != null)
				return doSetRocketHook(2);
			if (Weapons[4] != null)
				return doSetRocketHook(4);
			if (Weapons[5] != null)
				return doSetRocketHook(5);
		}
		return false;
	}

	public void doSetWeaponFireMode(int theWeaponFireMode) {
		this.weaponFireMode = theWeaponFireMode;
	}

	public void toggleWeaponFireMode(int hudLogWeaponId) {
		boolean canToggleWeaponFireMode = false;
		localHudLogWeaponId = hudLogWeaponId;
		for (int i = 2; i < 7; i++) {
			if (i == 3) continue;		// excluding Bombs with importing 4.13.2m's bombs release mode codes
			if (Weapons[i] != null) {
				canToggleWeaponFireMode = true;
				break;
			}
		}
		// if (Weapons[2] == null && Weapons[3] == null) return;
		if (!canToggleWeaponFireMode)
			return;
		switch (weaponFireMode) {
		case WEAPON_FIRE_MODE_SALVO:
			doSetWeaponFireMode(WEAPON_FIRE_MODE_SINGLE);
			HUD.log(hudLogWeaponId, "RocketReleaseSingle");
			prevUserWeaponFireMode = WEAPON_FIRE_MODE_SINGLE;
			break;
		case WEAPON_FIRE_MODE_SINGLE:
		default:
			doSetWeaponFireMode(WEAPON_FIRE_MODE_PAIRS);
			HUD.log(hudLogWeaponId, "RocketReleasePairs");
			prevUserWeaponFireMode = WEAPON_FIRE_MODE_PAIRS;
			break;
		// +++ add Ripple mode by western
		case WEAPON_FIRE_MODE_PAIRS:
			doSetWeaponFireMode(WEAPON_FIRE_MODE_RIPPLE);
			HUD.log(hudLogWeaponId, "RocketReleaseRipple");
			prevUserWeaponFireMode = WEAPON_FIRE_MODE_RIPPLE;
			break;
		case WEAPON_FIRE_MODE_RIPPLE:
			doSetWeaponFireMode(WEAPON_FIRE_MODE_SALVO);
			HUD.log(hudLogWeaponId, "RocketReleaseSalvo");
			prevUserWeaponFireMode = WEAPON_FIRE_MODE_SALVO;
			break;
		// --- add Ripple mode by western
		}
	}

		// +++ Import from 4.13.2m , PLUS , Select bomb mod by western
	public void setWeaponFireModeDefaults() {
		BulletEmitter abulletemitter[][] = Weapons;
		if (abulletemitter[rocketHookSelected] == null)
			return;
		for (int i = 0; i < abulletemitter[rocketHookSelected].length; i++)
			if (abulletemitter[rocketHookSelected][i].haveBullets() && ((abulletemitter[rocketHookSelected][i] instanceof RocketGunX4) || (abulletemitter[rocketHookSelected][i] instanceof RemoteControlRocket))) {
				doSetWeaponFireMode(WEAPON_FIRE_MODE_SINGLE);
				HUD.log(localHudLogWeaponId, "RocketReleaseSingle");
				return;
			}
			else if (prevUserWeaponFireMode != weaponFireMode && abulletemitter[rocketHookSelected][i].haveBullets()) {
				doSetWeaponFireMode(prevUserWeaponFireMode);
				switch (prevUserWeaponFireMode) {
				case WEAPON_FIRE_MODE_SINGLE:
					HUD.log(localHudLogWeaponId, "RocketReleaseSingle");
					break;
				case WEAPON_FIRE_MODE_PAIRS:
					HUD.log(localHudLogWeaponId, "RocketReleasePairs");
					break;
		// +++ add Ripple mode by western
				case WEAPON_FIRE_MODE_RIPPLE:
					HUD.log(localHudLogWeaponId, "RocketReleaseRipple");
					break;
		// --- add Ripple mode by western
				case WEAPON_FIRE_MODE_SALVO:
					HUD.log(localHudLogWeaponId, "RocketReleaseSalvo");
					break;
				}
			}
	}

	public void toggleBombReleaseMode() {
		if (isTrainStillDropping() || (getWeaponCount(3) < 1 && World.cur().diffCur.Limited_Ammo))
			return;
		if (bHasBombSelect && getNumberOfValidBombClass() == 1) {
			for (curBombSelected = 0; curBombSelected < bombClassNumber; curBombSelected++)
				if (getWeaponCount(3, bombClassArr[curBombSelected]) != 0) break;
		}
		boolean bHasWingB = hasWingBombs();
		boolean bHasFuselB = hasFuselageBombs();
		int i = bombReleaseMode + 1;
		if (i == B_RELEASE_SINGLE_WINGS_FIRST && bHasBombSelect && curBombSelected == -1)
			i = B_RELEASE_TRAIN;		// When selected ALL bombs, Single/Pair modes are ignored.
		if (bHasBombSelect && curBombSelected > -1)
			if (getWeaponCount(3, bombClassArr[curBombSelected]) == 1)
				i = 6;		// When rest amount of the bomb selected is only 1, Single/Pair/Train modes are ignored and go to next bomb class.
		if ((i == B_RELEASE_SINGLE_FC_FIRST || i == B_RELEASE_PAIRS_FC_FIRST) && (bHasFuselB != bHasWingB || bHasBombSelect))
			i++;
		if (i > B_RELEASE_TRAIN || i == B_RELEASE_TRAIN && (!((Aircraft)FM.actor).hasIntervalometer() || !World.cur().diffCur.Limited_Ammo)) {
			if (bHasBombSelect && getNumberOfValidBombClass() > 1) {
				curBombSelected++;
				for (; curBombSelected < bombClassNumber; curBombSelected++)
					if (getWeaponCount(3, bombClassArr[curBombSelected]) != 0) break;

				if (curBombSelected == bombClassNumber) curBombSelected = -1;
				if (curBombSelected > -1 && bombTrainAmount > getWeaponCount(3, bombClassArr[curBombSelected])) bombTrainAmount = getWeaponCount(3, bombClassArr[curBombSelected]);
				if (bombTrainAmount < 0) bombTrainAmount = 1;
			}

			i = B_RELEASE_SALVO;
		}
		if (i == B_RELEASE_TRAIN) {
			bombsReleased = 0;
			timeOfLastBombRelease = -1L;
		}
		bombReleaseMode = i;
		FM.AS.replicateBombModeStatesToNet();
		String classname = null;
		if (bHasBombSelect && curBombSelected > -1) {
			String classnametemp = bombClassArr[curBombSelected].getName().substring(35);
			int index = classnametemp.indexOf("_");
			classname = (index > 0) ? classnametemp.substring(0, index) : classnametemp;
		}
		switch(bombReleaseMode) {
		case 0: // B_RELEASE_SALVO
			if (bHasBombSelect) {
				if (curBombSelected == -1)
					HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSalvoAll");
				else
					HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSalvoName" , new Object[] { new String(classname) });
			} else
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSalvo");
			return;

		case 3: // B_RELEASE_PAIRS_WINGS_FIRST
			if (bHasBombSelect)
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairsName" , new Object[] { new String(classname) });
			else if (bHasWingB && bHasFuselB)
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairsWF");
			else
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairs");
			return;

		case 4: // B_RELEASE_PAIRS_FC_FIRST
			if (bHasWingB && bHasFuselB)
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairsFF");
			else
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleasePairs");
			return;

		case 1: // B_RELEASE_SINGLE_WINGS_FIRST
			if (bHasBombSelect)
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingleName" , new Object[] { new String(classname) });
			else if (bHasWingB && bHasFuselB)
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingleWF");
			else
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingle");
			return;

		case 2: // B_RELEASE_SINGLE_FC_FIRST
			if (bHasWingB && bHasFuselB)
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingleFF");
			else
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseSingle");
			return;

		case 5: // B_RELEASE_TRAIN
			if (bHasBombSelect) {
				if (curBombSelected == -1)
					HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrainAll");
				else
					HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrainName" , new Object[] { new String(classname) });
			} else
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrain");
			return;
		}
	}

	private void autoSwitchToTrain() {
		if (bombReleaseMode != B_RELEASE_TRAIN) {
			bombReleaseMode = B_RELEASE_TRAIN;
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrain");
		}
	}

	public void toggleBombTrainAmount() {
		if (isTrainStillDropping() || (getWeaponCount(3) <= 1 && World.cur().diffCur.Limited_Ammo))
			return;
		autoSwitchToTrain();
		int i;
		if (bHasBombSelect && curBombSelected > -1)
			i = getWeaponCount(3, bombClassArr[curBombSelected]);
		else
			i = getWeaponCount(3);
		bombTrainAmount++;
		if (bombTrainAmount > i || bombTrainAmount > ((Aircraft)FM.actor).getBombTrainMaxAmount())
			bombTrainAmount = 1;
		if (bombTrainAmount == i)
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrainAmountAll", new Object[] {
				new Integer(bombTrainAmount)
			});
		else
			HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrainAmount", new Object[] {
				new Integer(bombTrainAmount)
			});
	}

	public void toggleBombTrainDelay() {
		if (isTrainStillDropping() || (getWeaponCount(3) <= 1 && World.cur().diffCur.Limited_Ammo))
			return;
		autoSwitchToTrain();
		bombTrainDelay++;
		if (bombTrainDelay > ((Aircraft)FM.actor).getBombTrainDelayArray().length - 1)
			bombTrainDelay = 0;
		HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseTrainDelay", new Object[] {
			new Float(getBombTrainDelay())
		});
	}

	public float getBombTrainDelay() {
		return (float)((Aircraft)FM.actor).getBombTrainDelayArray()[bombTrainDelay];
	}

	public boolean isTrainStillDropping() {
		return bombReleaseMode == B_RELEASE_TRAIN && bombsReleased > 0;
	}

	private void doBombRelease() {
		byte byte0 = 3;
		if (isTrainStillDropping() && Time.current() < timeOfLastBombRelease + (long)getBombTrainDelay())
			return;
		int i = bombsReleased;
		if (bombReleaseMode == B_RELEASE_PAIRS_FC_FIRST || bombReleaseMode == B_RELEASE_PAIRS_WINGS_FIRST) {
			boolean flag = hasWingBombs();
			boolean flag2 = hasFuselageBombs();
			weaponReleaseCycle(byte0, bombReleaseMode, 0, 2, flag, flag2);
			weaponReleaseCycle(byte0, bombReleaseMode, 1, 2, flag, flag2);
		} else if (bombReleaseMode == B_RELEASE_SINGLE_FC_FIRST || bombReleaseMode == B_RELEASE_SINGLE_WINGS_FIRST) {
			boolean flag1 = hasWingBombs();
			boolean flag3 = hasFuselageBombs();
			weaponReleaseCycle(byte0, bombReleaseMode, 0, 1, flag1, flag3);
		} else {
			weaponReleaseCycle(byte0, bombReleaseMode, 0, 1, false, false);
		}
		if (bombReleaseMode != B_RELEASE_TRAIN) {
			WeaponControl[byte0] = false;
			bombsReleased = 0;
			timeOfLastBombRelease = -1L;
		} else if (bombsReleased >= bombTrainAmount || i == bombsReleased) {
			WeaponControl[byte0] = false;
			bombsReleased = 0;
			timeOfLastBombRelease = -1L;
		}
	}

	private void weaponReleaseCycle(int i, int relMode, int start, int unit, boolean bHasWingB, boolean bHasFuselB) {
		if (i == 3 && bHasBombSelect && curBombSelected > -1 && getWeaponCount(3, bombClassArr[curBombSelected]) == 0 && getWeaponCount(3) > 0)
		{
			String classnametemp = bombClassArr[curBombSelected].getName().substring(35);
			int index = classnametemp.indexOf("_");
			if(index > 0)
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseEmptyName" , new Object[] { new String(classnametemp.substring(0, index)) });
			else
				HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseEmptyName" , new Object[] { classnametemp });
		}

		for (int i1 = start; i1 < Weapons[i].length; i1 += unit) {
			if (Weapons[i][i1] instanceof FuelTankGun)
				continue;
			if ((Weapons[i][i1] instanceof BombGunNull) && i == 3 && relMode != B_RELEASE_PAIRS_FC_FIRST && relMode != B_RELEASE_PAIRS_WINGS_FIRST) {
				Weapons[i][i1].shots(1);
				continue;
			}
			if (!Weapons[i][i1].haveBullets())
				continue;
			if (i == 3 && bHasBombSelect && curBombSelected > -1 && !isMatchBombSelectedNum(curBombSelected, ((BombGun)Weapons[i][i1]).bulletClass()))
				continue;
			if (Weapons[i][i1].getHookName().startsWith("_BombSpawn") && ((Aircraft)FM.actor).needsOpenBombBay())
				if (bHasBayDoorControl && isPlayers((Aircraft)FM.actor)) {
					if (BayDoorControl < 0.9F)
						continue;
				} else {
					BayDoorControl = 1.0F;
				}
			if (relMode == B_RELEASE_SALVO) {
				if (World.cur().diffCur.Limited_Ammo) Weapons[i][i1].shots(Weapons[i][i1].countBullets());
				else Weapons[i][i1].shots(1);
				if (i == 2) {
//					rocketsReleased++;
//					timeOfLastRocketRelease = Time.current();
				}
				else if (i == 3) {
					timeOfLastBombReleaseRecorded = Time.current();
					if (bHasBombSelect && curBombSelected > -1)
						classOfLastBombReleaseRecorded = bombClassArr[curBombSelected];
				}
			} else {
				if (i == 3 && relMode != B_RELEASE_SALVO && relMode != B_RELEASE_TRAIN && !bHasBombSelect &&
				(bHasFuselB && (relMode == B_RELEASE_PAIRS_FC_FIRST || relMode == B_RELEASE_SINGLE_FC_FIRST) && isWingBomb(Weapons[i][i1])
				 || bHasWingB && (relMode == B_RELEASE_PAIRS_WINGS_FIRST || relMode == B_RELEASE_SINGLE_WINGS_FIRST) && isFuselageBomb(Weapons[i][i1])))
					continue;
				Weapons[i][i1].shots(1);
				if (i == 3) {
					if (i1 < Weapons[i].length - 1 && Weapons[i][i1].getHookName().startsWith("_BombSpawn")) {
						int j1 = i1 + 1;
						if (j1 < Weapons[i].length && Weapons[i][j1].getHookName().endsWith("a"))
							Weapons[i][j1].shots(1);
					}
					bombsReleased++;
					timeOfLastBombRelease = Time.current();
					timeOfLastBombReleaseRecorded = Time.current();
					if (bHasBombSelect && curBombSelected > -1)
						classOfLastBombReleaseRecorded = bombClassArr[curBombSelected];
				}
			}

			if (i == 3 && bHasBombSelect && curBombSelected > -1 && getWeaponCount(3, bombClassArr[curBombSelected]) == 0 && getWeaponCount(3) > 0)
			{
				String classnametemp = bombClassArr[curBombSelected].getName().substring(35);
				int index = classnametemp.indexOf("_");
				if(index > 0)
					HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseEmptyName" , new Object[] { new String(classnametemp.substring(0, index)) });
				else
					HUD.log(AircraftHotKeys.hudLogWeaponId, "BombReleaseEmptyName" , new Object[] { classnametemp });
			}

			if ((Weapons[i][i1] instanceof BombGun) && !((BombGun)Weapons[i][i1]).isCassette() && relMode != B_RELEASE_SALVO)
				return;
			if ((Weapons[i][i1] instanceof RocketGun) && !((RocketGun)Weapons[i][i1]).isCassette())
				return;
			if ((Weapons[i][i1] instanceof RocketBombGun) && !((RocketBombGun)Weapons[i][i1]).isCassette())
				return;
		}

	}

	public void setBombModeDefaults() {
		BulletEmitter abulletemitter[][] = Weapons;
//		bombsHangedNumber = 0;
		if (abulletemitter[3] != null) {
			for (int i = 0; i < abulletemitter[3].length; i++)
				if (abulletemitter[3][i].haveBullets()) {
					if ((abulletemitter[3][i] instanceof FuelTankGun) || (abulletemitter[3][i] instanceof BombGunNull) || !(abulletemitter[3][i] instanceof BombGun))
						continue;
//					bombsHangedNumber++;
				}
			for (int i = 0; i < abulletemitter[3].length; i++)
				if (abulletemitter[3][i].haveBullets()) {
					if ((abulletemitter[3][i] instanceof RocketGunHS_293) || (abulletemitter[3][i] instanceof RocketGunFritzX) || (abulletemitter[3][i] instanceof RocketGunBat)) {
						bombReleaseMode = B_RELEASE_SINGLE_WINGS_FIRST;
						return;
					}
			   //  ---   No FlareBombGun.class of 4.13.2m in 4.12.2m   ---
			   //	 if (abulletemitter[3][i] instanceof FlareBombGun)
			   //	 {
			   //		 bombReleaseMode = B_RELEASE_SINGLE_WINGS_FIRST;
			   //		 return;
			   //	 }
				}

		}
		if ((FM.actor instanceof TypeBomber) && ((Aircraft)FM.actor).hasIntervalometer() && World.cur().diffCur.Limited_Ammo)
			bombReleaseMode = B_RELEASE_TRAIN;
		int j = 0;
		for (int k = 0; k < FM.AP.way.size(); k++) {
			WayPoint waypoint = FM.AP.way.look_at_point(k);
			if (waypoint.Action == 3)
				j++;
		}

		bombTrainAmount = getWeaponCount(3) / Math.max(1, j);
		if (bombTrainAmount == 0)
			bombTrainAmount = 1;
		int l = ((Aircraft)FM.actor).getBombTrainDelayArray().length;
		for (int i1 = 0; i1 < l; i1++) {
			if (((Aircraft)FM.actor).getBombTrainDelayArray()[i1] < 200)
				continue;
			bombTrainDelay = i1;
			break;
		}

	}

	private boolean isWingBomb(BulletEmitter bulletemitter) {
		String s = bulletemitter.getHookName();
		Hook hook = FM.actor.findHook(s);
		String s1 = hook.chunkName();
		return s1.toLowerCase().startsWith("wing");
	}

	private boolean isFuselageBomb(BulletEmitter bulletemitter) {
		String s = bulletemitter.getHookName();
		Hook hook = FM.actor.findHook(s);
		String s1 = hook.chunkName();
		return s1.toLowerCase().startsWith("cf");
	}

	private boolean hasWingBombs() {
		for (int i = 0; i < Weapons[3].length; i++)
			if (!(Weapons[3][i] instanceof FuelTankGun) && !(Weapons[3][i] instanceof BombGunNull) && Weapons[3][i].haveBullets() && isWingBomb(Weapons[3][i]))
				return true;

		return false;
	}

	private boolean hasFuselageBombs() {
		for (int i = 0; i < Weapons[3].length; i++)
			if (!(Weapons[3][i] instanceof FuelTankGun) && !(Weapons[3][i] instanceof BombGunNull) && Weapons[3][i].haveBullets() && isFuselageBomb(Weapons[3][i]))
				return true;

		return false;
	}

	public void doSetCurrentBombSelected(int theCurrentBombSelected) {
		this.curBombSelected = theCurrentBombSelected;
	}

		// --- Import from 4.13.2m , PLUS , Select bomb mod by western

	public void doSetWeaponReleaseDelay(long theWeaponReleaseDelay) {
		this.weaponReleaseDelay = theWeaponReleaseDelay;
	}

	public void doChangeWeaponReleaseDelay(long theWeaponReleaseDelayOffset) {
		this.weaponReleaseDelay += theWeaponReleaseDelayOffset;
	}

	public void toggleWeaponReleaseDelay(int hudLogWeaponId) {
		if (Weapons[2] == null && Weapons[3] == null)
			return;
		if (weaponReleaseDelay == 33L)
			this.doChangeWeaponReleaseDelay(92L);
		else if (weaponReleaseDelay >= 125L) {
			if (weaponReleaseDelay >= 250L)
				this.doChangeWeaponReleaseDelay(250L);
			else
				this.doChangeWeaponReleaseDelay(125L);
		}
		if (weaponReleaseDelay > 1000L)
			this.doSetWeaponReleaseDelay(33L);
		HUD.log(hudLogWeaponId, "RocketReleaseDelay", new Object[] {
			new Float(weaponReleaseDelay)
		});
	}
}
