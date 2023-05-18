/*Modified Maneuver class for the SAS Engine Mod*/
package com.maddox.il2.ai.air;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.maddox.JGP.Point2f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector2d;
import com.maddox.JGP.Vector2f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.Front;
import com.maddox.il2.ai.VisCheck;
import com.maddox.il2.ai.War;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.GunGeneric;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.TextScr;
import com.maddox.il2.fm.AIFlightModel;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.fm.FlightModelMain;
import com.maddox.il2.fm.Pitot;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.objects.ActorCrater;
import com.maddox.il2.objects.air.AR_234;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.BI_1;
import com.maddox.il2.objects.air.BI_6;
import com.maddox.il2.objects.air.G4M2E;
import com.maddox.il2.objects.air.HE_LERCHE3;
import com.maddox.il2.objects.air.JU_88NEW;
import com.maddox.il2.objects.air.ME_163B1A;
import com.maddox.il2.objects.air.MXY_7;
import com.maddox.il2.objects.air.NetAircraft;
import com.maddox.il2.objects.air.SM79;
import com.maddox.il2.objects.air.Scheme4;
import com.maddox.il2.objects.air.Swordfish;
import com.maddox.il2.objects.air.TA_152C;
import com.maddox.il2.objects.air.TA_183;
import com.maddox.il2.objects.air.TypeAcePlane;
import com.maddox.il2.objects.air.TypeBomber;
import com.maddox.il2.objects.air.TypeDiveBomber;
import com.maddox.il2.objects.air.TypeDockable;
import com.maddox.il2.objects.air.TypeFastJet;
import com.maddox.il2.objects.air.TypeFighter;
import com.maddox.il2.objects.air.TypeGlider;
import com.maddox.il2.objects.air.TypeGuidedBombCarrier;
import com.maddox.il2.objects.air.TypeHasToKG;
import com.maddox.il2.objects.air.TypeJazzPlayer;
import com.maddox.il2.objects.air.TypeSeaPlane;
import com.maddox.il2.objects.air.TypeStormovik;
import com.maddox.il2.objects.air.TypeSupersonic;
import com.maddox.il2.objects.air.TypeTransport;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.ships.TestRunway;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.BombGun;
import com.maddox.il2.objects.weapons.BombGunNull;
import com.maddox.il2.objects.weapons.BombGunPara;
import com.maddox.il2.objects.weapons.BombGunTorp45_36AV_A;
import com.maddox.il2.objects.weapons.ParaTorpedoGun;
import com.maddox.il2.objects.weapons.ToKGUtils;
import com.maddox.il2.objects.weapons.TorpedoGun;
import com.maddox.rts.MsgDestroy;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Time;

public class Maneuver extends AIFlightModel {
	// TODO: Default Parameters
	// -------------------------------------------
	private int task;
	private int maneuver;
	private float mn_time;
	private int program[];
	private boolean bBusy;
	public boolean wasBusy;
	public boolean dontSwitch;
	public boolean aggressiveWingman;
	public boolean kamikaze;
	public boolean silence;
	public boolean bombsOut;
	private int bombsOutCounter;
	public float direction;
	public Loc rwLoc;
	private boolean first;
	private int rocketsDelay;
	private int bulletDelay;
	private int submanDelay;
	private static final int volleyL = 120;
	private float maxG;
	private float maxAOA;
	public float Alt;
	private float oldAOA;
	private float corrCoeff;
	private float corrElev;
	private float corrAile;
	private boolean checkGround;
	private boolean checkStrike;
	private boolean frequentControl;
	private boolean stallable;
	public FlightModel airClient;
	public FlightModel target;
	private FlightModel oldTarget;
	public FlightModel danger;
	private float dangerAggressiveness;
	private float oldDanCoeff;
	private int shotAtFriend;
	private float distToFriend;
	public Actor target_ground;
	public AirGroup Group;
	protected boolean TaxiMode;
	protected boolean finished;
	private boolean canTakeoff;
	public Point_Any wayCurPos;
	protected Point_Any wayPrevPos;
	protected Point_Any airdromeWay[];
	private Vector2f V_taxiLeg;
	protected List taxiToTakeOffWay;
	private int taxiToTakeOffWayLength;
	protected int curAirdromePoi;
	public long actionTimerStart;
	public long actionTimerStop;
	protected int gattackCounter;
	private int beNearOffsPhase;
	private int submaneuver;
	private boolean dont_change_subm;
	private int tmpi;
	private int sub_Man_Count;
	private float dA;
	private float dist;
	private float man1Dist;
	private float bullTime;
	private static final int STAY_ON_THE_TAIL = 1;
	private static final int NOT_TOO_FAST = 2;
	private static final int FROM_WAYPOINT = 3;
	private static final int CONST_SPEED = 4;
	private static final int MIN_SPEED = 5;
	private static final int MAX_SPEED = 6;
	private static final int CONST_POWER = 7;
	protected static final int ZERO_POWER = 8;
	private static final int BOOST_ON = 9;
	private static final int FOLLOW_WITHOUT_FLAPS = 10;
	private static final int BOOST_FULL = 11;
	private int speedMode;
	private float smConstSpeed;
	protected float smConstPower;
	private FlightModel tailForStaying;
	private Vector3d tailOffset;
	protected int Speak5minutes;
	protected int Speak1minute;
	protected int SpeakBeginGattack;
	protected boolean WeWereInGAttack;
	protected boolean WeWereInAttack;
	private boolean SpeakMissionAccomplished;
	public static final int ROOKIE = 0;
	public static final int NORMAL = 1;
	public static final int VETERAN = 2;
	public static final int ACE = 3;
	public static final int NO_TASK = 0;
	public static final int WAIT = 1;
	public static final int STAY_FORMATION = 2;
	public static final int FLY_WAYPOINT = 3;
	public static final int DEFENCE = 4;
	public static final int DEFENDING = 5;
	public static final int ATTACK_AIR = 6;
	public static final int ATTACK_GROUND = 7;
	public static final int NONE = 0;
	public static final int HOLD = 1;
	public static final int PULL_UP = 2;
	private static final int LEVEL_PLANE = 3;
	private static final int ROLL = 4;
	private static final int ROLL_90 = 5;
	private static final int ROLL_180 = 6;
	public static final int SPIRAL_BRAKE = 7;
	public static final int SPIRAL_UP = 8;
	private static final int SPIRAL_DOWN = 9;
	private static final int CLIMB = 10;
	private static final int DIVING_0_RPM = 11;
	public static final int DIVING_30_DEG = 12;
	private static final int DIVING_45_DEG = 13;
	private static final int TURN = 14;
	private static final int MIL_TURN = 15;
	private static final int LOOP = 16;
	private static final int LOOP_DOWN = 17;
	private static final int HALF_LOOP_UP = 18;
	private static final int HALF_LOOP_DOWN = 19;
	public static final int STALL = 20;
	public static final int WAYPOINT = 21;
	public static final int SPEEDUP = 22;
	private static final int BELL = 23;
	public static final int FOLLOW = 24;
	public static final int LANDING = 25;
	public static final int TAKEOFF = 26;
	public static final int ATTACK = 27;
	private static final int WAVEOUT = 28;
	private static final int SINUS = 29;
	private static final int ZIGZAG_UP = 30;
	private static final int ZIGZAG_DOWN = 31;
	private static final int ZIGZAG_SPIT = 32;
	private static final int HALF_LOOP_DOWN_135 = 33;
	private static final int HARTMANN_REDOUT = 34;
	public static final int ROLL_360 = 35;
	private static final int STALL_POKRYSHKIN = 36;
	private static final int BARREL_POKRYSHKIN = 37;
	private static final int SLIDE_LEVEL = 38;
	private static final int SLIDE_DESCENT = 39;
	private static final int RANVERSMAN = 40;
	private static final int CUBAN = 41;
	private static final int CUBAN_INVERT = 42;
	public static final int GATTACK = 43;
	public static final int PILOT_DEAD = 44;
	public static final int HANG_ON = 45;
	public static final int DELAY = 48;
	public static final int EMERGENCY_LANDING = 49;
	public static final int GATTACK_DIVE = 50;
	public static final int GATTACK_TORPEDO = 51;
	public static final int GATTACK_CASSETTE = 52;
	public static final int GATTACK_KAMIKAZE = 46;
	public static final int GATTACK_TINYTIM = 47;
	public static final int FAR_FOLLOW = 53;
	public static final int SPIRAL_DOWN_SLOW = 54;
	private static final int FOLLOW_SPIRAL_UP = 55;
	private static final int SINUS_SHALLOW = 56;
	public static final int GAIN = 57;
	private static final int SEPARATE = 58;
	public static final int BE_NEAR = 59;
	public static final int EVADE_UP = 60;
	public static final int EVADE_DN = 61;
	public static final int ENERGY_ATTACK = 62;
	public static final int ATTACK_BOMBER = 63;
	public static final int PARKED_STARTUP = 64;
	public static final int COVER = 65;
	public static final int TAXI = 66;
	private static final int RUN_AWAY = 67;
	private static final int FAR_COVER = 68;
	private static final int TAKEOFF_VTOL_A = 69;
	private static final int LANDING_VTOL_A = 70;
	public static final int GATTACK_HS293 = 71;
	public static final int GATTACK_FRITZX = 72;
	public static final int GATTACK_TORPEDO_TOKG = 73;
	public static final int COVER_DRAG_AND_BAG = 74;
	private static final int ATTACK_FROM_PLAYER = 75;
	public static final int COVER_AGRESSIVE = 76;
	private static final int TURN_HARD = 77;
	private static final int CLOUDS = 78;
	private static final int EVADE_ATTACK = 79;
	private static final int BRACKET_ATTACK = 80;
	public static final int DOUBLE_ATTACK = 81;
	public static final int BE_NEAR_LOW = 82;
	private static final int BARREL_ROLL = 83;
	public static final int PULL_UP_EMERGENCY = 84;
	private static final int DIVING_90_DEG = 85;
	private static final int SMOOTH_LEVEL = 86;
	private static final int IVAN = 87;
	public static final int FISHTAIL_LEFT = 88;
	public static final int FISHTAIL_RIGHT = 89;
	public static final int LOOKDOWN_LEFT = 90;
	public static final int LOOKDOWN_RIGHT = 91;
	public static final int LINE_ATTACK = 92;
	public static final int BOX_ATTACK = 93;
	private static final int BANG_BANG = 94;
	private static final int PANIC_MANIC = 95;
	private static final int PANIC_FREEZE = 96;
	private static final int COMBAT_CLIMB = 97;
	private static final int HIT_AND_RUN = 98;
	private static final int SEEK_AND_DESTROY = 99;
	private static final int BREAK_AWAY = 100;
	private static final int ATTACK_HARD = 101;
	public static final int TAXI_TO_TO = 102;
	private static final int MIL_TURN_LEFT = 103;
	private static final int MIL_TURN_RIGHT = 104;
	private static final int STRAIGHT_AND_LEVEL = 105;
	public static final int ART_SPOT = 106;
	public static final int WVSF_RESET = 0;
	public static final int WVSF_BOOM_ZOOM = 1;
	public static final int WVSF_FROM_AHEAD = 2;
	public static final int WVSF_FROM_BELOW = 3;
	public static final int WVSF_AS_IT_IS = 4;
	public static final int WVSF_FROM_TAIL = 5;
	public static final int WVSF_SHALLOW_DIVE = 6;
	public static final int WVSF_FROM_BOTTOM = 7;
	public static final int WVSF_FROM_LEFT = 8;
	public static final int WVSF_FROM_RIGHT = 9;
	public static final int LIVE = 0;
	public static final int RETURN = 1;
	public static final int TASK = 2;
	public static final int PROTECT_LEADER = 3;
	public static final int PROTECT_WINGMAN = 4;
	public static final int PROTECT_FRIENDS = 5;
	public static final int DESTROY_ENEMIES = 6;
	public static final int KEEP_ORDER = 7;
	public float takeIntoAccount[];
	public float AccountCoeff[];
	public static final int FVSB_BOOM_ZOOM = 0;
	public static final int FVSB_BOOM_ZOOM_TO_ENGINE = 1;
	public static final int FVSB_SHALLOW_DIVE_TO_ENGINE = 2;
	public static final int FVSB_FROM_AHEAD = 3;
	public static final int FVSB_FROM_BELOW = 4;
	public static final int FVSB_AS_IT_IS = 5;
	public static final int FVSB_FROM_SIDE = 6;
	public static final int FVSB_FROM_TAIL_TO_ENGINE = 7;
	public static final int FVSB_FROM_TAIL = 8;
	public static final int FVSB_SHALLOW_DIVE = 9;
	public static final int FVSB_FROM_BOTTOM = 10;
	private Vector3d ApproachV;
	private Vector3d TargV;
	private Vector3d TargDevV;
	private Vector3d TargDevVnew;
	private Vector3d scaledApproachV;
	private float ApproachR;
	private float TargY;
	private float TargZ;
	private float TargYS;
	private float TargZS;
	private float RandomVal;
	public Vector3d followOffset;
	private Vector3d followTargShift;
	private Vector3d followCurShift;
	private float raAilShift;
	private float raElevShift;
	private float raRudShift;
	private float sinKren;
	private boolean strikeEmer;
	private FlightModel strikeTarg;
	private Vector3d tempV;
	private boolean targetLost;
	private Point3d lastKnownTargetLoc;
	private long timeToSearchTarget;
	private Point3d tempPoint;
	private Vector3d losVector;
	private Vector3d corrVector;
	private Vector3d wanderVector;
	private Vector3d wanderVectorNew;
	private float shootingDeviation;
	private float sp;
	private int followType;
	Vector3d tmpV3dToDanger;
	Vector3d spreadV3d;
	long lookAroundTime;
	private static AnglesFork steerAngleFork = new AnglesFork();
	private float Wo;
	public boolean bKeepOrdnance;
	public int wAttType;
	public int bracketSide;
	float cloudHeight;
	public boolean bGentsStartYourEngines;
	float desiredAlt;
	float oldError;
	float errorAlt;
	float cCoeff;
	float koeff;
	public boolean bSlowDown;
	private boolean direc;
	float Kmax;
	float rmin;
	float rmax;
	double phase;
	double radius;
	int pointQuality;
	int curPointQuality;
	private static Vector3d vTemp = new Vector3d();
	private Loc tLoc;
	private static Point3d tPoint = new Point3d();
	private static Vector3d tmpV3d = new Vector3d();
	private static Vector3d tmpV3f = new Vector3d();
	public static Orient tmpOr = new Orient();
	public Orient saveOr;
	private static Point3d Po = new Point3d();
	private static Point3d Pc = new Point3d();
	private static Point3d Pd = new Point3d();
	private static Vector3d Ve = new Vector3d();
	private Vector3d oldVe;
	private Vector3d Vtarg;
	private Vector3d constVtarg;
	private Vector3d constVtarg1;
	private static Vector3d Vf = new Vector3d();
	private Vector3d Vxy;
	private static Vector3d Vpl = new Vector3d();
	private AnglesFork AFo;
	private static Point3d P = new Point3d();
	private static Point2f Pcur = new Point2f();
	private static Vector2d Vcur = new Vector2d();
	private static Vector2f V_to = new Vector2f();
	private static Vector2d Vdiff = new Vector2d();
	private static Loc elLoc = new Loc();
	public static boolean showFM = false;
	private float pilotHeadT;
	private float pilotHeadY;
	Vector3d windV;
	private static Point3d tmpLoc = new Point3d();
	private static HashMap collisionMap = new HashMap();
	private Actor ignoredActor;
	private Actor collisionDangerActor;
	private float distToTaxiPoint;
	// -------------------------------------------

	// TODO: New Parameters
	// -------------------------------------------
	private boolean bStage3;
	private boolean bStage4;
	private boolean bStage6;
	private boolean bStage7;
	private boolean bAlreadyCheckedStage7;
	private float fNearestDistance;
	private boolean bCatapultAI;
	private boolean bFastLaunchAI;
	private boolean bNoNavLightsAI;
	private int passCounter = 0;
//	private float oldTangage = 0.0F;
	private float of52 = 0.0F;
	private boolean bTouchingDown = false;
	private long brakingtimer = 0L;
//	private boolean bLogDetail = true;

	// --------------------------------

	public void set_task(int i) {
		task = i;
	}

	public int get_task() {
		return task;
	}

	public int get_maneuver() {
		return maneuver;
	}

	public void set_maneuver(int i) {
		if (maneuver == 84) return;
		if ((i == 21 || i == 24) && (maneuver == 88 || maneuver == 89 || maneuver == 90 || maneuver == 91)) return;
		if (CT.dropWithPlayer != null && hasBombs() && CT.dropWithPlayer.isAlive()) if (i == 43) i = 24;
		else if (i == 0 && maneuver == 24) return;
		if (maneuver == 44) return;
		if (i != 44 && (maneuver == 26 || maneuver == 69 || maneuver == 66 || maneuver == 46 || maneuver == 95)) return;
		int j = maneuver;
		maneuver = i;
		if (j != maneuver) set_flags();
	}

	public void pop() {
		if (maneuver == 44) return;
		if (program[0] != 44 && (maneuver == 26 || maneuver == 69 || maneuver == 66 || maneuver == 46)) return;
		int i = maneuver;
		maneuver = program[0];
		for (int j = 0; j < program.length - 1; j++)
			program[j] = program[j + 1];

		program[program.length - 1] = 0;
		if (i != maneuver) set_flags();
	}

	public void unblock() {
		maneuver = 0;
	}

	public void safe_set_maneuver(int i) {
		dont_change_subm = true;
		set_maneuver(i);
		dont_change_subm = true;
	}

	public void safe_pop() {
		dont_change_subm = true;
		pop();
		dont_change_subm = true;
	}

	public void clear_stack() {
		for (int i = 0; i < program.length; i++)
			program[i] = 0;

	}

	public void push(int i) {
		for (int j = program.length - 2; j >= 0; j--)
			program[j + 1] = program[j];

		program[0] = i;
	}

	public void push() {
		push(maneuver);
	}

	public void accurate_set_task_maneuver(int i, int j) {
		if (maneuver == 44 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 102) return;
		set_task(i);
		if (maneuver != j) {
			clear_stack();
			set_maneuver(j);
		}
	}

	public void accurate_set_FOLLOW() {
		if (maneuver == 44 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 102) return;
		set_task(2);
		if (maneuver != 24 && maneuver != 53) {
			clear_stack();
			set_maneuver(24);
		}
	}

	public void setBusy(boolean flag) {
		bBusy = flag;
	}

	public boolean isBusy() {
		return bBusy;
	}

	public void setSpeedMode(int i) {
		speedMode = i;
	}

	private boolean isStallable() {
		return !(actor instanceof TypeStormovik) && !(actor instanceof TypeTransport);
	}

	private void resetControls() {
		// TODO: Added in blown flaps control
		CT.AileronControl = CT.BrakeControl = CT.ElevatorControl = CT.FlapsControl = CT.BlownFlapsControl = CT.RudderControl = 0.0F;
		CT.AirBrakeControl = 0.0F;
	}

	private void set_flags() {
		if (World.cur().isDebugFM()) printDebugFM();
		AP.setStabAll(false);
		mn_time = 0.0F;
		minElevCoeff = 4F;
		shootingDeviation = 6F;
		sp = nShootingPoint(Skill);
		shootingPoint = sp;
		wanderVector.set(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F));
		if (!dont_change_subm) {
			setSpeedMode(3);
			first = true;
			submaneuver = 0;
			sub_Man_Count = 0;
		}
		dont_change_subm = false;
		if (maneuver != 48 && maneuver != 0 && maneuver != 26 && maneuver != 64 && maneuver != 102 && maneuver != 44) resetControls();
		setCheckGround(maneuver != 20 && maneuver != 25 && maneuver != 102 && maneuver != 1 && maneuver != 26 && maneuver != 69 && maneuver != 44 && maneuver != 49 && maneuver != 43 && maneuver != 50 && maneuver != 51 && maneuver != 73 && maneuver != 46
				&& maneuver != 84 && maneuver != 64 && maneuver != 95 && maneuver != 100);
		frequentControl = maneuver == 24
				|| maneuver == 53
				|| maneuver == 68
				|| (maneuver == 59)
				| (maneuver == 82)
				|| (maneuver == 8 || maneuver == 55 || maneuver == 27 || maneuver == 62 || maneuver == 63 || maneuver == 25 || maneuver == 102 || maneuver == 43 || maneuver == 50 || maneuver == 65 || maneuver == 44 || maneuver == 21 || maneuver == 64
						|| maneuver == 69 || maneuver == 76 || maneuver == 74 || maneuver == 75 || maneuver == 80 || maneuver == 87 || maneuver == 77 || maneuver == 99 || maneuver == 83 || maneuver == 100 || maneuver == 101 || maneuver == 98);
		turnOnChristmasTree(maneuver == 25 || maneuver == 26 || maneuver == 69 || maneuver == 70);
		turnOnCloudShine(maneuver == 25);
		checkStrike = maneuver != 60 && maneuver != 61 && maneuver != 102 && maneuver != 1 && maneuver != 24 && maneuver != 26 && maneuver != 69 && maneuver != 64 && maneuver != 44;
		stallable = maneuver != 44 && maneuver != 1 && maneuver != 48 && maneuver != 0 && maneuver != 26 && maneuver != 69 && maneuver != 64 && maneuver != 43 && maneuver != 50 && maneuver != 51 && maneuver != 52 && maneuver != 47 && maneuver != 71
				&& maneuver != 72 && maneuver != 102;
		if (maneuver == 44 || maneuver == 1 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 2 || maneuver == 84 || maneuver == 57 || maneuver == 60 || maneuver == 61 || maneuver == 43 || maneuver == 50 || maneuver == 51
				|| maneuver == 52 || maneuver == 47 || maneuver == 29 || maneuver == 71 || maneuver == 72 || maneuver == 88 || maneuver == 89 || maneuver == 90 || maneuver == 91 || maneuver == 86 || maneuver == 102) setBusy(true);
	}

	public void setCheckStrike(boolean flag) {
		checkStrike = flag;
	}

	private void setCheckGround(boolean flag) {
		checkGround = flag;
	}

	public Maneuver(String s) {
		super(s);
		maneuver = 26;
		program = new int[8];
		bBusy = false;
		wasBusy = false;
		dontSwitch = false;
		aggressiveWingman = false;
		kamikaze = false;
		silence = true;
		bombsOutCounter = 0;
		first = true;
		rocketsDelay = 0;
		bulletDelay = 0;
		submanDelay = 0;
		Alt = 0.0F;
		corrCoeff = 0.0F;
		corrElev = 0.0F;
		corrAile = 0.0F;
		checkGround = false;
		checkStrike = false;
		frequentControl = false;
		stallable = false;
		airClient = null;
		target = null;
		oldTarget = null;
		danger = null;
		dangerAggressiveness = 0.0F;
		oldDanCoeff = 0.0F;
		shotAtFriend = 0;
		distToFriend = 0.0F;
		target_ground = null;
		TaxiMode = false;
		finished = false;
		canTakeoff = false;
		V_taxiLeg = new Vector2f();
		taxiToTakeOffWay = null;
		taxiToTakeOffWayLength = 0;
		curAirdromePoi = 0;
		actionTimerStart = 0L;
		actionTimerStop = 0L;
		gattackCounter = 0;
		beNearOffsPhase = 0;
		submaneuver = 0;
		dont_change_subm = false;
		tmpi = 0;
		sub_Man_Count = 0;
		dist = 0.0F;
		man1Dist = 50F;
		bullTime = 0.0015F;
		speedMode = 3;
		smConstSpeed = 90F;
		smConstPower = 0.7F;
		tailForStaying = null;
		tailOffset = new Vector3d();
		WeWereInGAttack = false;
		WeWereInAttack = false;
		SpeakMissionAccomplished = false;
		takeIntoAccount = new float[8];
		AccountCoeff = new float[8];
		ApproachV = new Vector3d();
		TargV = new Vector3d();
		TargDevV = new Vector3d(0.0D, 0.0D, 0.0D);
		TargDevVnew = new Vector3d(0.0D, 0.0D, 0.0D);
		scaledApproachV = new Vector3d();
		followOffset = new Vector3d();
		followTargShift = new Vector3d(0.0D, 0.0D, 0.0D);
		followCurShift = new Vector3d(0.0D, 0.0D, 0.0D);
		raAilShift = 0.0F;
		raElevShift = 0.0F;
		raRudShift = 0.0F;
		sinKren = 0.0F;
		strikeEmer = false;
		strikeTarg = null;
		tempV = new Vector3d();
		// targetLost = false;
		lastKnownTargetLoc = new Point3d();
		timeToSearchTarget = 0L;
		tempPoint = new Point3d();
		losVector = new Vector3d();
		corrVector = new Vector3d();
		wanderVector = new Vector3d(0.0D, 0.0D, 0.0D);
		wanderVectorNew = new Vector3d(0.0D, 0.0D, 0.0D);
		tmpV3dToDanger = new Vector3d();
		spreadV3d = new Vector3d(0.0D, 0.0D, 0.0D);
		lookAroundTime = -1L;
		bKeepOrdnance = false;
		wAttType = -1;
		bGentsStartYourEngines = false;
		bSlowDown = false;
		direc = false;
		Kmax = 10F;
		phase = 0.0D;
		radius = 50D;
		pointQuality = -1;
		curPointQuality = 50;
		settLoc(new Loc());
		saveOr = new Orient();
		oldVe = new Vector3d();
		Vtarg = new Vector3d();
		constVtarg = new Vector3d();
		constVtarg1 = new Vector3d();
		Vxy = new Vector3d();
		AFo = new AnglesFork();
		pilotHeadT = 0.0F;
		pilotHeadY = 0.0F;
		windV = new Vector3d();
		ignoredActor = null;
		collisionDangerActor = null;
		distToTaxiPoint = -1F;
		AP = new AutopilotAI(this);
		shootingDeviation = 6F;
		cCoeff = 0.01F * (float) courage + World.Rnd().nextFloat(0.93F, 0.98F);
		if (cCoeff > 1.01F) cCoeff = 1.01F;
		lookAroundTime = Time.current() + (long) World.Rnd().nextInt(30000, 0x30d40 - Skill * 40000);
		wanderVector.set(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F));
		// TODO: +++ CTO Mod 4.12 +++
		bStage3 = false;
		bStage4 = false;
		bStage6 = false;
		bStage7 = false;
		bAlreadyCheckedStage7 = false;
		bFastLaunchAI = false;
		if (Config.cur.ini.get("Mods", "NoNavLightsAI", 0) == 1) bNoNavLightsAI = true;
		if (Mission.cur().sectFile().get("Mods", "NoNavLightsAI", 0) == 1) bNoNavLightsAI = true;
		if (Config.cur.ini.get("Mods", "FastLaunchAI", 0) == 1) bFastLaunchAI = true;
		if (Mission.cur().sectFile().get("Mods", "FastLaunchAI", 0) == 1) bFastLaunchAI = true;
		// TODO: --- CTO Mod 4.12 ---

	}

	public void decDangerAggressiveness() {
		dangerAggressiveness -= 0.01F;
		if (dangerAggressiveness < 0.0F) dangerAggressiveness = 0.0F;
		oldDanCoeff -= 0.005F;
		if (oldDanCoeff < 0.0F) oldDanCoeff = 0.0F;
	}

	public void incDangerAggressiveness(int i, float f, float f1, FlightModel flightmodel) {
		double d = (double) (flightmodel.Energy - Energy) * 0.1019D;
		float f2 = nDanCoeff(d, f, f1);
		if (danger == null || f2 > oldDanCoeff) {
			if (danger != flightmodel && (flightmodel.actor instanceof TypeFighter)) {
				boolean flag = false;
				if (Group != null) {
					for (int j = 0; j < Group.nOfAirc; j++)
						if (Group.airc[j] != null && Group.airc[j].FM != null && ((Maneuver) Group.airc[j].FM).airClient == this) flag = true;

				}
				if (!flag && VisCheck.checkDefense((Aircraft) actor, (Aircraft) flightmodel.actor)) return;
			}
			danger = flightmodel;
		}
		oldDanCoeff = f2;
		dangerAggressiveness += f2 * (float) i;
		if (dangerAggressiveness > 1.0F) dangerAggressiveness = 1.0F;
	}

	public float getDangerAggressiveness() {
		return dangerAggressiveness;
	}

	public float getMaxHeightSpeed(float f) {
		if (f < HofVmax) return Vmax + (VmaxH - Vmax) * (f / HofVmax);
		float f1 = (f - HofVmax) / HofVmax;
		f1 = 1.0F - 1.5F * f1;
		if (f1 < 0.0F) f1 = 0.0F;
		return VmaxH * f1;
	}

	public float getMinHeightTurn(float f) {
		return Wing.T_turn;
	}

	public void setShotAtFriend(float f) {
		distToFriend = f;
		shotAtFriend = 30;
	}

	public boolean hasCourseWeaponBullets() {
		if (actor instanceof TypeJazzPlayer) return ((TypeJazzPlayer) actor).hasCourseWeaponBullets();
		if (actor instanceof AR_234) return false;
		else return CT.Weapons[0] != null && CT.Weapons[0][0] != null && CT.Weapons[0][0].countBullets() != 0 || CT.Weapons[1] != null && CT.Weapons[1][0] != null && CT.Weapons[1][0].countBullets() != 0;
	}

	public boolean hasBombs() {
		if (CT.Weapons[3] != null) {
			for (int i = 0; i < CT.Weapons[3].length; i++)
				if (CT.Weapons[3][i] != null && CT.Weapons[3][i].countBullets() != 0) return true;

		}
		return false;
	}

	public boolean hasRockets() {
		if (CT.Weapons[2] != null) {
			for (int i = 0; i < CT.Weapons[2].length; i++)
				if (CT.Weapons[2][i] != null && CT.Weapons[2][i].countBullets() != 0) return true;

		}
		return false;
	}

	public boolean canAttack() {
		return (!Group.isWingman(Group.numInGroup((Aircraft) actor)) || aggressiveWingman) && isOk() && hasCourseWeaponBullets();
	}

	public void getDifferentTarget() {
		int i = 0;
		do {
			i++;
			target = Group.setAAttackObject(((Aircraft) actor).aircIndex());
		} while (target == oldTarget && i < 5);
		if (target == null) target = oldTarget;
	}

	public void update(float f) {
		if (Config.isUSE_RENDER()) headTurn(f);
		if (showFM) OutCT(20);
		super.update(f);
		callSuperUpdate = true;
		decDangerAggressiveness();
		if (Loc.z < -20D) ((Aircraft) actor).postEndAction(0.0D, actor, 4, null);
		float f1 = (float) Engine.land().HQ_Air(Loc.x, Loc.y);
		Po.set(Vwld);
		Po.scale(3D);
		Po.add(Loc);
		f1 = (float) Math.max(f1, Engine.land().HQ_Air(Po.x, Po.y));
		Alt = (float) Loc.z - f1;
		indSpeed = getSpeed() * (float) Math.sqrt(Density / 1.225F);
		if (!Gears.onGround() && isOk() && Alt > 8F) {
			if (AOA > AOA_Crit - 2.0F) Or.increment(0.0F, 0.05F * (AOA_Crit - 2.0F - AOA), 0.0F);
			if (AOA < -5F) Or.increment(0.0F, 0.05F * (-5F - AOA), 0.0F);
		}
		if (!frequentControl && (Time.tickCounter() + actor.hashCode()) % 4 != 0) return;
		turnOffTheWeapon();
		maxG = 3.5F + AS.getPilotHealth(0) * (float) Skill * 0.5F;
		Or.wrap();
		if (mn_time > 10F && AOA > AOA_Crit + 5F && isStallable() && stallable && Alt > 5F) safe_set_maneuver(20);
		if (getSpeed() > VmaxAllowed * 0.85F) {
			setSpeedMode(7);
			smConstPower = 0.78F;
			if (getSpeed() > VmaxAllowed * cCoeff) {
				setSpeedMode(8);
				if (maneuver != 86) {
					push();
					set_maneuver(86);
				}
			}
		}
		if ((maneuver == 27 || maneuver == 6 || maneuver == 63 || maneuver == 62) && isTick(32, 0) && (VisCheck.isVisibilityBlockedByClouds(this, target, false) || VisCheck.isVisibilityBlockedByDarkness(this, target))) {
			push();
			set_maneuver(99);
		}
		switch (maneuver) {
		default:
			break;

		case 0: // '\0'
			target_ground = null;
			if (mn_time > 2.0F) set_maneuver(21);
			break;

		case 1: // '\001'
			dryFriction = 8F;
			CT.FlapsControl = 0.0F;
			CT.BrakeControl = 1.0F;
			break;

		case 48: // '0'
			if (mn_time >= World.Rnd().nextFloat(1.0F, 1.0F + 2.0F / (float) (1 + subSkill))) pop();
			break;

		case 44: // ','
			if (Gears.onGround() && Vwld.length() < 0.30000001192092896D && World.Rnd().nextInt(0, 99) < 4) {
				if (Group != null) Group.delAircraft((Aircraft) actor);
				if ((actor instanceof TypeGlider) || (actor instanceof TypeSeaPlane)) break;
				World.cur();
				if (actor != World.getPlayerAircraft()) if (Airport.distToNearestAirport(Loc) > 900D) ((Aircraft) actor).postEndAction(60D, actor, 3, null);
				else MsgDestroy.Post(Time.current() + 30000L, actor);
			}
			if (first) {
				AP.setStabAll(false);
				CT.ElevatorControl = World.Rnd().nextFloat(-0.07F, 0.4F);
				CT.AileronControl = World.Rnd().nextFloat(-0.15F, 0.15F);
			}
			break;

		case 7: // '\007'
			wingman(false);
			setSpeedMode(9);
			if (getW().x <= 0.0D) {
				CT.AileronControl = -1F;
				CT.RudderControl = 1.0F;
			} else {
				CT.AileronControl = 1.0F;
				CT.RudderControl = -1F;
			}
			float f2 = Or.getKren();
			if (f2 > -90F && f2 < 90F) {
				float f5 = 0.01111F * (90F - Math.abs(f2));
				CT.ElevatorControl = -0.08F * f5 * (Or.getTangage() - 3F);
			} else {
				float f6 = 0.01111F * (Math.abs(f2) - 90F);
				CT.ElevatorControl = 0.08F * f6 * (Or.getTangage() - 3F);
			}
			if (getSpeed() < (1.0F + 0.7F / (float) (1 + flying)) * Vmin) pop();
			if (mn_time > 2.2F + (float) courage / 2.0F) pop();
			if ((danger instanceof Pilot) && ((Maneuver) danger).target == this && Loc.distance(danger.Loc) > (double) (400F + World.Rnd().nextFloat(0.0F, 250F))) pop();
			break;

		case 8: // '\b'
			if (first && !isCapableOfACM()) {
				if (Skill > 0) pop();
				if (Skill > 1) setReadyToReturn(true);
			}
			setSpeedMode(6);
			tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
			if (Or.getKren() > 0.0F) Ve.set(100D, -8D, 10D);
			else Ve.set(100D, 8D, 10D);
			tmpOr.transform(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			farTurnToDirection();
			if (Alt > 250F && mn_time > 8F || mn_time > 120F) pop();
			break;

		case 55: // '7'
			if (first && !isCapableOfACM()) {
				if (Skill > 0) pop();
				if (Skill > 1) setReadyToReturn(true);
			}
			setSpeedMode(6);
			tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
			if (Leader != null && Actor.isAlive(Leader.actor) && mn_time < 2.5F) {
				if (Leader.Or.getKren() > 0.0F) Ve.set(100D, -8D, 10D);
				else Ve.set(100D, 8D, 10D);
			} else if (Or.getKren() > 0.0F) Ve.set(100D, -8D, 10D);
			else Ve.set(100D, 8D, 10D);
			if (Group.getAaaNum() > 4F) if (Leader != null && Actor.isAlive(Leader.actor) && mn_time < 2.5F) {
				if (Leader.Or.getKren() > 0.0F) {
					if (Group.numInGroup((Aircraft) actor) >= 2 && subSkill > 7) Ve.set(100D, 8D, 10D);
					else Ve.set(100D, -8D, 10D);
				} else if (Group.numInGroup((Aircraft) actor) >= 2 && subSkill > 7) Ve.set(100D, -8D, 10D);
				else Ve.set(100D, 8D, 10D);
			} else if (Or.getKren() > 0.0F) Ve.set(100D, -8D, 10D);
			else Ve.set(100D, 8D, 10D);
			tmpOr.transform(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			farTurnToDirection();
			if (Alt > 250F && mn_time > 8F || mn_time > 120F) pop();
			break;

		case 45: // '-'
			setSpeedMode(7);
			smConstPower = 0.8F;
			dA = Or.getKren();
			if (dA > 0.0F) dA -= 25F;
			else dA -= 335F;
			if (dA < -180F) dA += 360F;
			dA = -0.01F * dA;
			CT.AileronControl = dA;
			CT.ElevatorControl = -0.04F * (Or.getTangage() - 1.0F) + 0.002F * ((AP.way.curr().z() - (float) Loc.z) + 250F);
			if (mn_time > 60F) {
				mn_time = 0.0F;
				pop();
			}
			break;

		case 106: // 'j'
			setSpeedMode(7);
			smConstPower = 0.8F;
			switch (submaneuver) {
			case 0: // '\0'
				tmpLoc.set(AP.way.curr().getP());
				Ve.sub(tmpLoc, Loc);
				dist = (float) Ve.length();
				Or.transformInv(Ve);
				Ve.normalize();
				turnToDirection(10F);
				desiredAlt = (float) tmpLoc.z;
				submaneuver++;
				break;

			case 1: // '\001'
				Ve.sub(tmpLoc, Loc);
				dist = (float) Ve.length();
				Or.transformInv(Ve);
				if (dist < 2000F) {
					actionTimerStop += Time.current();
					submaneuver++;
				}
				Ve.normalize();
				attackTurnToDirection(dist, f, 10F);
				break;

			case 2: // '\002'
				Ve.sub(tmpLoc, Loc);
				dist = (float) Ve.length();
				Or.transformInv(Ve);
				Ve.normalize();
				attackTurnToDirection(dist, f, 10F);
				if (dist < 300F) submaneuver++;
				break;

			case 3: // '\003'
				dA = Or.getKren();
				if (dA > 0.0F) dA -= 45F;
				else dA -= 315F;
				if (dA < -180F) dA += 360F;
				dA = -0.01F * dA;
				CT.AileronControl = dA;
				dA = 0.002F * ((desiredAlt - (float) Loc.z) + 250F);
				if (dA > 0.66F) dA = 0.66F;
				CT.ElevatorControl = -0.04F * (Or.getTangage() - 1.0F) + dA;
				if (actionTimerStop < Time.current()) {
					((Aircraft) actor).bSpotter = false;
					target_ground = null;
					AP.way.curr().waypointType = 0;
					Group.setGroupTask(3);
					pop();
				}
				break;
			}
			break;

		case 54: // '6'
			if (Vwld.length() > (double) (VminFLAPS * 2.0F)) Vwld.scale(0.99500000476837158D);
			// fall through

		case 9: // '\t'
			if (first && !isCapableOfACM()) {
				if (Skill > 0) pop();
				if (Skill > 1) setReadyToReturn(true);
			}
			setSpeedMode(4);
			smConstSpeed = 100F;
			dA = Or.getKren();
			if (dA > 0.0F) dA -= 50F;
			else dA -= 310F;
			if (dA < -180F) dA += 360F;
			dA = -0.01F * dA;
			CT.AileronControl = dA;
			dA = (-10F - Or.getTangage()) * 0.05F;
			CT.ElevatorControl = dA;
			if ((double) getOverload() < 1.0D / Math.abs(Math.cos(DEG2RAD(dA)))) CT.ElevatorControl += 1.0F * f;
			else CT.ElevatorControl -= 1.0F * f;
			if (Alt < 100F) {
				push(3);
				pop();
			}
			if (mn_time > 5F) pop();
			break;

		case 14: // '\016'
			setSpeedMode(6);
			if (first) AP.setStabAltitude(true);
			dA = Or.getKren();
			if ((double) getOverload() < 1.0D / Math.abs(Math.cos(DEG2RAD(dA)))) CT.ElevatorControl += 1.0F * f;
			else CT.ElevatorControl -= 1.0F * f;
			if (dA > 0.0F) dA -= 50F;
			else dA -= 310F;
			if (dA < -180F) dA += 360F;
			dA = -0.01F * dA;
			CT.AileronControl = dA;
			if (mn_time > 5F) pop();
			break;

		case 77: // 'M'
			minElevCoeff = 20F;
			if (first) AP.setStabAltitude(true);
			dA = Or.getKren();
			switch (submaneuver) {
			default:
				break;

			case 0: // '\0'
				desiredAlt = getAltitude();
				if (World.Rnd().nextInt(0, 100) > 50) bankAngle = 72F;
				else bankAngle = 288F;
				submaneuver++;
				break;

			case 1: // '\001'
				int i = World.Rnd().nextInt(0, 100);
				if (i < 30) {
					float f11 = World.Rnd().nextFloat(0.4F, 0.8F);
					setSpeedMode(4);
					smConstSpeed = Vmax * f11;
				} else {
					setSpeedMode(11);
				}
				submaneuver++;
				break;

			case 2: // '\002'
				float f9 = getAltitude() - desiredAlt;
				errorAlt = f9 - oldError;
				oldError = f9;
				if ((double) getOverload() < 2.5D + 1.0D / Math.abs(Math.cos(DEG2RAD(dA))) && AOA < AOA_Crit * 0.95F) {
					CT.ElevatorControl = 1.0F;
					if (CT.bHasElevatorTrim) CT.setTrimElevatorControl(0.75F);
				} else {
					CT.ElevatorControl -= 0.1F * f;
					if (CT.bHasElevatorTrim) CT.setTrimElevatorControl(0.0F);
				}
				if (f9 > 0.0F) if (errorAlt > 0.0F) {
					if (bankAngle > 180F) bankAngle -= 0.5F;
					else bankAngle += 0.5F;
				} else if (errorAlt < 0.0F) if (bankAngle > 180F) bankAngle += 0.4F;
				else bankAngle -= 0.4F;
				if (f9 < 0.0F) if (errorAlt < 0.0F) {
					if (bankAngle > 180F) bankAngle += 0.5F;
					else bankAngle -= 0.5F;
				} else if (errorAlt > 0.0F) if (bankAngle > 180F) bankAngle -= 0.4F;
				else bankAngle += 0.4F;
				dA -= bankAngle;
				if (dA < -180F) dA += 360F;
				dA = -0.01F * dA;
				CT.AileronControl = dA;
				CT.RudderControl = -0.1F * getAOS();
				break;
			}
			nShakeMe(flying, Skill);
			if (danger != null && danger.CT.isShooting()) nFullPants(dangerAggressiveness, subSkill);
			if (AOA > AOA_Crit) {
				CT.setTrimElevatorControl(0.0F);
				pop();
			}
			if (mn_time > 20F) {
				CT.setTrimElevatorControl(0.0F);
				mn_time = 0.0F;
				if (dangerAggressiveness < 0.8F) push(101);
				pop();
			}
			break;

		case 101: // 'e'
			minElevCoeff = 20F;
			if (target != null) {
				Ve.sub(target.Loc, Loc);
				Vtarg.set(Ve);
			} else {
				clear_stack();
				pop();
				break;
			}
			float f10 = (target.Energy - Energy) * 0.1019F;
			dist = (float) Ve.length();
			if (target != null && dist < 700F && VisCheck.checkLeadShotBlock((Aircraft) actor, (Aircraft) target.actor)) {
				Vtarg.z += 0.14499999999999999D * (double) flying * (double) dist * (1.0D - Ve.x);
				Vtarg.x -= 0.22500000000000001D * (double) flying * (double) dist * (1.0D - Ve.x);
				Ve.set(Vtarg);
			}
			Or.transformInv(Ve);
			Ve.normalize();
			if (Ve.x > 0.99900001287460327D) {
				fighterVsFighter(f);
				set_maneuver(27);
			} else {
				turnBaby(dist, f, 2 * subSkill + 1);
				if (AOA > AOA_Crit * 0.85F && Ve.x > 0.97000002861022949D || dist < 300F) {
					if (CT.FlapsControl < 0.18F) CT.FlapsControl += 0.02F;
				} else {
					CT.FlapsControl = 0.0F;
				}
				if (Ve.x > 0.98999999999999999D && dist <= convAI) {
					setSpeedMode(2);
					tailForStaying = target;
					tailOffset.set(-convAI * 0.6F, 0.0D, 0.0D);
				} else {
					setSpeedMode(11);
				}
				if (mn_time > 30F && Ve.x < -0.5D || mn_time > 60F || f10 > 150F) {
					CT.setTrimElevatorControl(0.0F);
					mn_time = 0.0F;
					pop();
				}
			}
			break;

		case 87: // 'W'
			minElevCoeff = 20F;
			switch (submaneuver) {
			case 0: // '\0'
				if (danger != null && sub_Man_Count == 0) {
					tmpV3d.set(danger.getW());
					danger.Or.transform(tmpV3d);
					if (tmpV3d.z > 0.0D) sinKren = -World.Rnd().nextFloat(75F, 90F);
					else sinKren = World.Rnd().nextFloat(75F, 90F);
				}
				setSpeedMode(8);
				CT.AileronControl = -0.2F * (Or.getKren() + sinKren);
				sub_Man_Count++;
				if (sub_Man_Count > 16 + World.Rnd().nextInt(0, 14)) {
					sub_Man_Count = 0;
					submaneuver++;
				}
				break;

			case 1: // '\001'
				sub_Man_Count++;
				CT.AileronControl = 0.0F;
				CT.FlapsControl = 0.2F;
				if (AOA < AOA_Crit * 0.8F) {
					CT.ElevatorControl = 1.0F;
					if (CT.bHasElevatorTrim) CT.setTrimElevatorControl(0.75F);
				} else {
					CT.ElevatorControl -= 0.02F * f;
					if (CT.bHasElevatorTrim) CT.setTrimElevatorControl(0.0F);
				}
				if (sub_Man_Count > 32) setSpeedMode(11);
				if (sub_Man_Count > 75 + World.Rnd().nextInt(0, 30)) {
					sinKren = World.Rnd().nextFloat(-270F, 90F);
					CT.FlapsControl = 0.0F;
					sub_Man_Count = 0;
					submaneuver++;
				}
				break;

			case 2: // '\002'
				sub_Man_Count++;
				CT.AileronControl = -0.2F * (Or.getKren() + sinKren);
				setSpeedMode(8);
				CT.RudderControl = CT.AileronControl * -1F;
				CT.ElevatorControl = -1F;
				if (CT.bHasElevatorTrim) CT.setTrimElevatorControl(-0.75F);
				if (sub_Man_Count > 18 + World.Rnd().nextInt(0, 18)) {
					sub_Man_Count = 0;
					submaneuver = 0;
					pop();
				}
				break;
			}
			break;

		case 58: // ':'
			setSpeedMode(11);
			CT.setPowerControl(1.1F);
			if (first) {
				submaneuver = World.Rnd().nextInt(0, 1);
				direction = World.Rnd().nextFloat(-5F, -1F);
			}
			CT.AileronControl = -0.08F * (Or.getKren() - direction);
			switch (submaneuver) {
			case 0: // '\0'
				direction = World.Rnd().nextFloat(-5F, -1F);
				break;

			case 1: // '\001'
				direction = World.Rnd().nextFloat(1.0F, 5F);
				break;
			}
			if (sub_Man_Count > 150) {
				submaneuver = World.Rnd().nextInt(0, 1);
				sub_Man_Count = 0;
			}
			if (Alt > 50F) {
				dA = -((float) Vwld.z / (Math.abs(getSpeed()) + 1.0F) + 0.37F);
				if (dA < -0.075F) dA = -0.075F;
				CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F + 0.25F * (float) getW().y;
			} else {
				CT.ElevatorControl = -0.04F * (Or.getTangage() + 5F);
			}
			CT.RudderControl = 0.0F;
			sub_Man_Count++;
			setBusy(true);
			if (mn_time > 20F) pop();
			break;

		case 4: // '\004'
			CT.AileronControl = getW().x <= 0.0D ? -1F : 1.0F;
			CT.ElevatorControl = 0.1F * (float) Math.cos(DEG2RAD(Or.getKren()));
			CT.RudderControl = 0.0F;
			if (getSpeedKMH() < 220F) {
				push(3);
				pop();
			}
			if (mn_time > 7F) pop();
			break;

		case 2: // '\002'
			minElevCoeff = 20F;
			if (first) {
				wingman(false);
				AP.setStabAll(false);
				CT.RudderControl = 0.0F;
				if (World.Rnd().nextInt(0, 99) < 10 && Alt < 80F) Voice.speakPullUp((Aircraft) actor);
			}
			if (getSpeed() < Vmax * 0.7F || Or.getTangage() > 8F) setSpeedMode(11);
			else setSpeedMode(8);
			dA = Or.getKren();
			CT.BayDoorControl = 0.0F;
			CT.AirBrakeControl = 0.0F;
			CT.AileronControl = -0.04F * dA;
			CT.ElevatorControl = 1.0F + 0.3F * (float) getW().y;
			if (CT.ElevatorControl < 0.0F) CT.ElevatorControl = 0.0F;
			if (AOA > 15F) Or.increment(0.0F, (15F - AOA) * 0.5F * f, 0.0F);
			if (Alt < 10F && Vwld.z < 0.0D) Vwld.z *= 0.89999997615814209D;
			if (Vwld.z > 1.0D) {
				if (actor instanceof TypeGlider) push(49);
				else if (getSpeed() > Vmin * 1.3F || Alt > 50F) push(57);
				else push(2);
				pop();
			}
			if (mn_time > 25F) {
				push(49);
				pop();
			}
			break;

		case 84: // 'T'
			if (sub_Man_Count == 0) {
				tmpi = World.Rnd().nextBoolean() ? -1 : 1;
				koeff = M.getFullMass() / (Sq.squareWing * Wing.CyCritH_0);
			}
			sub_Man_Count++;
			AP.setStabAll(false);
			if (getSpeed() < Vmax * 0.7F || Or.getPitch() > 360F) setSpeedMode(11);
			else setSpeedMode(8);
			CT.BayDoorControl = 0.0F;
			CT.AirBrakeControl = 0.0F;
			float f12 = Math.min(Or.getPitch(), 375F);
			int j = 35;
			boolean flag = true;
			float f13 = 0.0003F * M.getFullMass();
			float f14 = 2.0F;
			if (target != null || danger != null) f14 = 1.7F - 0.1F * (float) Skill * (float) courage;
			float f15 = (float) Math.sqrt(Vrel.x * Vrel.x + Vrel.y * Vrel.y);
			float f16 = f15 * 0.12F * f13 * f14 * koeff;
			if (f16 > 3000F) f16 = 3000F;
			if (f16 < koeff * 5F) f16 = koeff * 5F;
			float af[] = { 1.0F, 1.0F, 1.0F };
			Po.set(Loc);
			Vpl.set(f16, 0.0D, -100D);
			tmpOr.setYPR(Or.getYaw(), f12, 0.0F);
			tmpOr.transform(Vpl);
			Po.add(Vpl);
			if (Landscape.rayHitHQ(actor.pos.getAbsPoint(), Po, tempPoint)) {
				flag = false;
				af[0] = (float) Loc.distance(tempPoint) / f16;
			}
			Po.set(Loc);
			f16 = Math.max(koeff * 5F, f16);
			Vpl.set(f16, 0.0D, 0.0D);
			float f17 = Or.getYaw() + (float) j;
			if (f17 < -180F) f17 = 360F - f17;
			tmpOr.setYPR(f17, f12, 0.0F);
			tmpOr.transform(Vpl);
			Po.add(Vpl);
			if (Landscape.rayHitHQ(actor.pos.getAbsPoint(), Po, tempPoint)) {
				flag = false;
				af[1] = (float) Loc.distance(tempPoint) / f16;
			}
			Po.set(Loc);
			Vpl.set(f16, 0.0D, 0.0D);
			f17 = Or.getYaw() - (float) j;
			if (f17 > 180F) f17 -= 360F;
			tmpOr.setYPR(f17, f12, 0.0F);
			tmpOr.transform(Vpl);
			Po.add(Vpl);
			if (Landscape.rayHitHQ(actor.pos.getAbsPoint(), Po, tempPoint)) {
				flag = false;
				af[2] = (float) Loc.distance(tempPoint) / f16;
			}
			bankAngle = 0.0F;
			dA = Or.getKren();
			nLandAvoidance(af[0], af[1], af[2], dA, Vmin, tmpi);
			if (!flag) {
				if ((task == 3 || task == 2) && ((Aircraft) actor).getWing().bOnlyAI && Group != null && Group.formationType != 11) {
					Group.setFormationAndScale((byte) 11, 2.0F, true);
					Group.formationType = 11;
				}
				CT.ElevatorControl += bankAngle;
				mn_time = 0.0F;
			} else {
				dA = CT.ElevatorControl;
				if (Or.getTangage() > 20F) dA -= (Or.getTangage() - 20F) * 0.1F * f;
				else dA = (((float) Vwld.length() / VminFLAPS) * 140F - 50F - Or.getTangage() * 20F) * 0.004F;
				dA += 0.5D * getW().y;
				CT.ElevatorControl = dA;
				if (Vwld.z < 0.0D || CT.ElevatorControl <= 0.05F || mn_time > 0.75F) {
					if (mn_time > 3F) CT.AileronControl = -0.04F * Or.getKren();
					if (target != null || danger != null) {
						if (target != null) {
							if (!Landscape.rayHitHQ(Loc, target.Loc, tempPoint) || mn_time > 3F) pop();
							else target = null;
						} else if (mn_time > 2.0F) pop();
					} else if (task == 3 || task == 2) {
						Vpl.sub(AP.way.curr().getP(), actor.pos.getAbsPoint());
						Vpl.normalize();
						float f18 = Math.min(AP.getWayPointDistance(), 4000F);
						Vpl.scale(f18);
						Po.set(Loc);
						Po.add(Vpl);
						Pd.set(Po);
						boolean flag1 = !Landscape.rayHitHQ(actor.pos.getAbsPoint(), Pd, tempPoint);
						if (flag1 || mn_time > 25F) pop();
					} else {
						pop();
					}
				}
			}
			CT.RudderControl = -0.1F * getAOS();
			if (getSpeed() < Vmin * 1.5F) CT.FlapsControl = 0.15F;
			else CT.FlapsControl = 0.0F;
			if (mn_time > 27F) {
				push(49);
				pop();
			}
			break;

		case 60: // '<'
			setSpeedMode(11);
			CT.RudderControl = 0.0F;
			CT.ElevatorControl = 1.0F;
			if (mn_time > 0.15F) pop();
			break;

		case 61: // '='
			CT.RudderControl = 0.0F;
			CT.ElevatorControl = -0.4F;
			if (mn_time > 0.2F) pop();
			break;

		case 3: // '\003'
			if (first && program[0] == 49) pop();
			setSpeedMode(6);
			CT.AileronControl = -0.04F * Or.getKren();
			dA = (getSpeedKMH() - 180F - Or.getTangage() * 10F - getVertSpeed() * 5F) * 0.004F;
			CT.ElevatorControl = dA;
			if (getSpeed() > Vmin * 1.2F && getVertSpeed() > 0.0F) {
				setSpeedMode(7);
				smConstPower = 0.7F;
				pop();
			}
			break;

		case 86: // 'V'
			setSpeedMode(8);
			CT.AileronControl = -0.04F * Or.getKren();
			dA = -((float) Vwld.z / (Math.abs(getSpeed()) + 1.0F) + 0.07F);
			if (dA < -0.0075F) dA = -0.0075F;
			CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F + 0.25F * (float) getW().y;
			if (getSpeed() < VmaxAllowed * 0.93F) {
				setSpeedMode(7);
				smConstPower = 0.87F;
				pop();
			}
			break;

		case 10: // '\n'
			AP.setStabAll(false);
			setSpeedMode(6);
			CT.AileronControl = -0.04F * Or.getKren();
			dA = CT.ElevatorControl;
			if (Or.getTangage() > 15F) {
				dA -= (Or.getTangage() - 15F) * 0.1F * f;
			} else {
				dA = (((float) Vwld.length() / VminFLAPS) * 140F - 50F - Or.getTangage() * 20F) * 0.004F;
				dA += 0.5D * getW().y;
			}
			// TODO: Review please. The following line is not part of 4.12.2 base code and has hence been commented out!
			// dA += 0.5D * getW().y;
			CT.ElevatorControl = dA;
			if (Alt > 250F && mn_time > 6F || mn_time > 20F) pop();
			break;

		case 97: // 'a'
			AP.setStabAll(false);
			setSpeedMode(11);
			CT.AileronControl = -0.04F * Or.getKren();
			dA = CT.ElevatorControl;
			if (Or.getTangage() > 15F) dA -= (Or.getTangage() - 15F) * 0.1F * f;
			else dA = (((float) Vwld.length() / VminFLAPS) * 140F - 50F - Or.getTangage() * 20F) * 0.004F;
			dA += 0.5D * getW().y;
			CT.ElevatorControl = dA;
			if (mn_time > 20F || dangerAggressiveness > 0.65F) pop();
			break;

		case 96: // '`'
			CT.AileronControl = -0.04F * Or.getKren();
			CT.ElevatorControl = -0.04F * Or.getTangage();
			CT.RudderControl = 0.0F;
			if (mn_time > (10F - (float) subSkill) * 0.5F) {
				if (World.Rnd().nextInt(0, 1000) < 20 - 4 * subSkill) {
					((Aircraft) actor).hitDaSilk();
					if (Group != null) Group.delAircraft((Aircraft) actor);
					set_task(0);
				}
				pop();
			}
			break;

		case 95: // '_'
			setSpeedMode(7);
			smConstPower = 0.98F;
			dA = Or.getKren();
			if ((double) getOverload() < 1.0D / Math.abs(Math.cos(DEG2RAD(dA)))) CT.ElevatorControl += 1.0F * f;
			if (dA > 0.0F) dA -= 70F;
			else dA -= 290F;
			if (dA < -180F) dA += 360F;
			dA = -0.015F * dA;
			CT.AileronControl = dA;
			CT.RudderControl = -0.1F * getAOS();
			nSmackMe((float) W.z, Sq.squareWing, (float) Vflow.length(), subSkill, Skill);
			if (mn_time > (16F - (float) subSkill) * 0.125F) if (subSkill > 7 || World.Rnd().nextInt(0, 1000) < 50 - 3 * subSkill) pop();
			else if (World.Rnd().nextInt(0, 1000) < 30 - 4 * subSkill || World.Rnd().nextInt(0, 100) < 20 && Alt < 200F) {
				((Aircraft) actor).hitDaSilk();
				if (Group != null) Group.delAircraft((Aircraft) actor);
				set_task(0);
			} else if (World.Rnd().nextInt(0, 1000) < 50 - 3 * subSkill) {
				push(96);
				pop();
			} else {
				pop();
			}
			break;

		case 57: // '9'
			AP.setStabAll(false);
			minElevCoeff = 20F;
			setSpeedMode(9);
			CT.setPowerControl(1.1F);
			CT.AileronControl = -0.04F * Or.getKren();
			dA = CT.ElevatorControl;
			if (Or.getTangage() > 25F) dA -= (Or.getTangage() - 25F) * 0.1F * f;
			else dA = (((float) Vwld.length() / VminFLAPS) * 140F - 50F - Or.getTangage() * 15F) * 0.004F;
			dA += 0.5D * getW().y;
			CT.ElevatorControl = dA;
			if (Alt > 150F || Alt > 100F && mn_time > 2.0F || mn_time > 3F) pop();
			break;

		case 78: // 'N'
			switch (submaneuver) {
			case 0: // '\0'
				cloudHeight = Mission.curCloudsHeight();
				if (Loc.z > (double) (cloudHeight + 500F)) submaneuver = 1;
				else if (Loc.z < (double) (cloudHeight - 500F)) submaneuver = 2;
				else submaneuver = 3;
				break;

			case 1: // '\001'
				setSpeedMode(11);
				CT.AileronControl = -0.04F * Or.getKren();
				dA = -((float) Vwld.z / (Math.abs(getSpeed()) + 1.0F) + 0.37F);
				if (dA < -0.075F) dA = -0.075F;
				CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F + 0.25F * (float) getW().y;
				if (Loc.z <= (double) (cloudHeight + 400F)) {
					push();
					push(86);
					pop();
				}
				break;

			case 2: // '\002'
				AP.setStabAll(false);
				setSpeedMode(9);
				CT.AileronControl = -0.04F * Or.getKren();
				dA = CT.ElevatorControl;
				if (Or.getTangage() > 15F) dA -= (Or.getTangage() - 15F) * 0.1F * f;
				else dA = (((float) Vwld.length() / VminFLAPS) * 140F - 50F - Or.getTangage() * 20F) * 0.004F;
				dA += 0.5D * getW().y;
				CT.ElevatorControl = dA;
				if (Loc.z >= (double) (cloudHeight + 300F)) {
					push();
					push(22);
					push(22);
					pop();
				}
				break;

			case 3: // '\003'
				scaledApproachV.set(World.Rnd().nextInt(3500, 4500), World.Rnd().nextInt(-2500, 2500), World.Rnd().nextInt(-250, 250));
				Or.transformInv(scaledApproachV);
				lastKnownTargetLoc.set(scaledApproachV);
				submaneuver++;
				break;

			case 4: // '\004'
				if (mn_time % 5F + (float) World.Rnd().nextInt(0, 7) > 7F) submaneuver--;
				Ve.sub(lastKnownTargetLoc, Loc);
				Or.transformInv(Ve);
				Ve.normalize();
				farTurnToDirection();
				break;
			}
			if (Alt < 120F || mn_time > 50F) pop();
			break;

		case 11: // '\013'
			setSpeedMode(8);
			if (Math.abs(Or.getKren()) < 90F) {
				CT.AileronControl = -0.04F * Or.getKren();
				if (Vwld.z > 0.0D || getSpeedKMH() < 270F) dA = -0.04F;
				else dA = 0.04F;
				CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F;
			} else {
				CT.AileronControl = 0.04F * (180F - Math.abs(Or.getKren()));
				if (Or.getTangage() > -25F) dA = 0.33F;
				else if (Vwld.z > 0.0D || getSpeedKMH() < 270F) dA = 0.04F;
				else dA = -0.04F;
				CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F;
			}
			if (Alt < 120F || mn_time > 4F) pop();
			break;

		case 12: // '\f'
			setSpeedMode(4);
			smConstSpeed = 80F;
			CT.AileronControl = -0.04F * Or.getKren();
			if (Vwld.length() > (double) (VminFLAPS * 2.0F)) Vwld.scale(0.99500000476837158D);
			dA = -((float) Vwld.z / (Math.abs(getSpeed()) + 1.0F) + 0.5F);
			if (dA < -0.1F) dA = -0.1F;
			CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F + 0.3F * (float) getW().y;
			if (mn_time > 5F || Alt < 200F) pop();
			break;

		case 13: // '\r'
			if (first) {
				AP.setStabAll(false);
				submaneuver = (actor instanceof TypeFighter) ? 0 : 2;
			}
			switch (submaneuver) {
			case 0: // '\0'
				dA = Or.getKren() - 180F;
				if (dA < -180F) dA += 360F;
				dA = -0.04F * dA;
				CT.AileronControl = dA;
				if (mn_time > 3F || Math.abs(Or.getKren()) > 175F - 5F * (float) Skill) submaneuver++;
				break;

			case 1: // '\001'
				dA = Or.getKren() - 180F;
				if (dA < -180F) dA += 360F;
				dA = -0.04F * dA;
				CT.AileronControl = dA;
				CT.RudderControl = -0.1F * getAOS();
				setSpeedMode(8);
				if (Or.getTangage() > -45F && getOverload() < maxG) CT.ElevatorControl += 1.5F * f;
				else CT.ElevatorControl -= 0.5F * f;
				if (Or.getTangage() < -44F) submaneuver++;
				if ((double) Alt < -5D * Vwld.z || mn_time > 5F) pop();
				break;

			case 2: // '\002'
				setSpeedMode(8);
				CT.AileronControl = -0.04F * Or.getKren();
				dA = -((float) Vwld.z / (Math.abs(getSpeed()) + 1.0F) + 0.707F);
				if (dA < -0.75F) dA = -0.75F;
				CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F + 0.5F * (float) getW().y;
				if ((double) Alt < -5D * Vwld.z || mn_time > 5F) pop();
				break;
			}
			if (Alt < 200F) pop();
			break;

		case 85: // 'U'
			if (first) {
				AP.setStabAll(false);
				submaneuver = (actor instanceof TypeFighter) ? 0 : 2;
			}
			switch (submaneuver) {
			case 0: // '\0'
				dA = Or.getKren() - 180F;
				if (dA < -180F) dA += 360F;
				dA = -0.04F * dA;
				CT.AileronControl = dA;
				if (mn_time > 3F || Math.abs(Or.getKren()) > 175F - 5F * (float) Skill) submaneuver++;
				break;

			case 1: // '\001'
				dA = Or.getKren() - 180F;
				if (dA < -180F) dA += 360F;
				dA = -0.04F * dA;
				CT.AileronControl = dA;
				CT.RudderControl = -0.1F * getAOS();
				setSpeedMode(11);
				if (Or.getTangage() > -88F && getOverload() < maxG) CT.ElevatorControl += 1.5F * f;
				else CT.ElevatorControl -= 0.5F * f;
				if (Or.getTangage() < -87F) submaneuver++;
				if ((double) Alt < -5D * Vwld.z || mn_time > 5F) pop();
				break;

			case 2: // '\002'
				setSpeedMode(11);
				CT.AileronControl = -0.04F * Or.getKren();
				dA = -((float) Vwld.z / (Math.abs(getSpeed()) + 1.0F) + 0.707F);
				if (dA < -0.75F) dA = -0.75F;
				CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F + 0.5F * (float) getW().y;
				if ((double) Alt < -5D * Vwld.z || mn_time > 5F) pop();
				break;
			}
			if (Alt < 200F) pop();
			break;

		case 5: // '\005'
			dA = Or.getKren();
			if (dA < 0.0F) dA -= 270F;
			else dA -= 90F;
			if (dA < -180F) dA += 360F;
			dA = -0.02F * dA;
			CT.AileronControl = dA;
			if (mn_time > 5F || Math.abs(Math.abs(Or.getKren()) - 90F) < 1.0F) pop();
			break;

		case 6: // '\006'
			dA = Or.getKren() - 180F;
			if (dA < -180F) dA += 360F;
			CT.AileronControl = (float) ((double) (-0.04F * dA) - 0.5D * getW().x);
			if (mn_time > 4F || Math.abs(Or.getKren()) > 178F) {
				W.x = 0.0D;
				pop();
			}
			break;

		case 35: // '#'
			if (first) {
				AP.setStabAll(false);
				direction = Or.getKren();
				submaneuver = Or.getKren() > 0.0F ? 1 : -1;
				tmpi = 0;
				setSpeedMode(9);
			}
			CT.AileronControl = 1.0F * (float) submaneuver;
			CT.RudderControl = 0.0F * (float) submaneuver;
			float f3 = Or.getKren();
			if (f3 > -90F && f3 < 90F) {
				float f7 = 0.01111F * (90F - Math.abs(f3));
				CT.ElevatorControl = -0.08F * f7 * (Or.getTangage() - 3F);
			} else {
				float f8 = 0.01111F * (90F - Math.abs(f3));
				CT.ElevatorControl = 0.08F * f8 * (Or.getTangage() - 3F);
			}
			if (Or.getKren() * direction < 0.0F) tmpi = 1;
			if (tmpi == 1 && (submaneuver > 0 ? Or.getKren() > direction : Or.getKren() < direction) || mn_time > 17.5F) pop();
			break;

		case 83: // 'S'
			minElevCoeff = 20F;
			if (first || sub_Man_Count == 0) {
				sub_Man_Count++;
				AP.setStabAll(false);
				submaneuver = Or.getKren() > 0.0F ? 1 : -1;
				direction = World.Rnd().nextFloat(0.25F, 4.7F);
				if (danger != null) {
					dA = (float) danger.Loc.distance(Loc);
					if (dA > 150F) {
						setSpeedMode(8);
						CT.setPowerControl(0.0F);
					} else {
						setSpeedMode(9);
					}
				} else {
					setSpeedMode(8);
					CT.setPowerControl(0.0F);
				}
				dA = Or.getKren();
				if (dA < 0.0F) dA = -1F;
			}
			CT.RudderControl = -1F * (float) submaneuver;
			if (AOA < AOA_Crit * 0.8F) {
				CT.ElevatorControl = 1.0F;
				if (CT.bHasElevatorTrim) CT.setTrimElevatorControl(0.75F);
			} else {
				CT.ElevatorControl -= 0.1F * f;
				if (CT.bHasElevatorTrim) CT.setTrimElevatorControl(0.0F);
			}
			CT.AileronControl = 0.5F + dA / (2.0F + mn_time);
			if (mn_time > direction || Alt < 100F) pop();
			break;

		case 22: // '\026'
			setSpeedMode(11);
			CT.AileronControl = -0.04F * Or.getKren();
			CT.ElevatorControl = -0.04F * (Or.getTangage() + 5F);
			CT.RudderControl = 0.0F;
			if (getSpeed() > Vmax || mn_time > 30F) pop();
			break;

		case 79: // 'O'
			setSpeedMode(11);
			CT.setPowerControl(1.1F);
			minElevCoeff = 18F;
			if (first) {
				sub_Man_Count = 0;
				setSpeedMode(11);
				CT.setPowerControl(1.1F);
			}
			if (sub_Man_Count == 0) {
				scaledApproachV.set(World.Rnd().nextInt(8500, 10500), 6000 - World.Rnd().nextInt(0, 1) * 12000 - World.Rnd().nextInt(-2000, 2000), 0.0D);
				Or.transformInv(scaledApproachV);
				scaledApproachV.z = 0.0D;
				scaledApproachV.add(Loc);
				sub_Man_Count++;
			}
			Ve.sub(scaledApproachV, Loc);
			Or.transformInv(Ve);
			Ve.normalize();
			farTurnToDirection();
			if (danger != null && danger.Loc.distance(Loc) < 850D || mn_time > 50F) pop();
			break;

		case 67: // 'C'
			minElevCoeff = 18F;
			if (first) {
				sub_Man_Count = 0;
				setSpeedMode(11);
				CT.setPowerControl(1.1F);
			}
			if (danger != null) {
				float f19 = 700F - (float) danger.Loc.distance(Loc);
				if (f19 < 0.0F) f19 = 0.0F;
				f19 *= 0.00143F;
				float f4 = Or.getKren();
				if (sub_Man_Count == 0 || first) {
					if (raAilShift < 0.0F) raAilShift = f19 * World.Rnd().nextFloat(0.6F, 1.0F);
					else raAilShift = f19 * World.Rnd().nextFloat(-1F, -0.6F);
					raRudShift = f19 * World.Rnd().nextFloat(-0.5F, 0.5F);
					raElevShift = f19 * World.Rnd().nextFloat(-0.8F, 0.8F);
				}
				CT.AileronControl = 0.9F * CT.AileronControl + 0.1F * raAilShift;
				CT.RudderControl = 0.95F * CT.RudderControl + 0.05F * raRudShift;
				if (f4 > -90F && f4 < 90F) CT.ElevatorControl = -0.04F * (Or.getTangage() + 5F);
				else CT.ElevatorControl = 0.05F * (Or.getTangage() + 5F);
				CT.ElevatorControl += 0.1F * raElevShift;
				sub_Man_Count++;
				if ((float) sub_Man_Count >= 80F * (1.5F - f19) && f4 > -70F && f4 < 70F) sub_Man_Count = 0;
				if (mn_time > 30F) pop();
			} else {
				pop();
			}
			break;

		case 16: // '\020'
			if (first) {
				if (!isCapableOfACM()) pop();
				AP.setStabAll(false);
				setSpeedMode(6);
				if (getSpeed() < 0.75F * Vmax) {
					push();
					push(22);
					pop();
					break;
				}
				submaneuver = 0;
			}
			maxAOA = Vwld.z > 0.0D ? 7F : 12F;
			switch (submaneuver) {
			case 0: // '\0'
				CT.ElevatorControl = 0.05F;
				CT.AileronControl = -0.04F * Or.getKren();
				CT.RudderControl = -0.1F * getAOS();
				if (Math.abs(Or.getKren()) < 2.0F) submaneuver++;
				break;

			case 1: // '\001'
				CT.AileronControl = -0.04F * Or.getKren();
				CT.RudderControl = -0.1F * getAOS();
				dA = 0.5F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.4F * f;
				else CT.ElevatorControl += 0.4F * f;
				if (Or.getTangage() > 80F) submaneuver++;
				if (getSpeed() < Vmin * 1.5F) pop();
				break;

			case 2: // '\002'
				CT.RudderControl = -0.1F * getAOS() * (getSpeed() > 300F ? 1.0F : 0.0F);
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.4F * f;
				else CT.ElevatorControl += 0.4F * f;
				if (Or.getTangage() < 0.0F) submaneuver++;
				break;

			case 3: // '\003'
				CT.RudderControl = -0.1F * getAOS() * (getSpeed() > 300F ? 1.0F : 0.0F);
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.2F * f;
				else CT.ElevatorControl += 0.2F * f;
				if (Or.getTangage() < -60F) submaneuver++;
				break;

			case 4: // '\004'
				if (Or.getTangage() > -45F) {
					CT.AileronControl = -0.04F * Or.getKren();
					maxAOA = 3.5F;
				}
				CT.RudderControl = -0.1F * getAOS();
				dA = 0.5F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 1.0F * f;
				else CT.ElevatorControl += 0.4F * f;
				if (Or.getTangage() > 3F || Vwld.z > 0.0D) pop();
				break;
			}
			break;

		case 17: // '\021'
			if (first) {
				if (Alt < 1000F) pop();
				else if (getSpeed() < Vmin * 1.2F) {
					push();
					push(22);
					pop();
				} else {
					push(18);
					push(19);
					pop();
				}
			} else {
				pop();
			}
			break;

		case 19: // '\023'
			if (first) {
				if (Alt < 1000F) {
					pop();
					break;
				}
				AP.setStabAll(false);
				submaneuver = 0;
			}
			maxAOA = Vwld.z > 0.0D ? 7F : 12F;
			switch (submaneuver) {
			case 0: // '\0'
				CT.ElevatorControl = 0.05F;
				CT.AileronControl = 0.04F * (Or.getKren() > 0.0F ? 180F - Or.getKren() : -180F + Or.getKren());
				CT.RudderControl = -0.1F * getAOS();
				if (Math.abs(Or.getKren()) > 178F) submaneuver++;
				break;

			case 1: // '\001'
				setSpeedMode(7);
				smConstPower = 0.5F;
				CT.AileronControl = 0.0F;
				CT.RudderControl = -0.1F * getAOS();
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.2F * f;
				else CT.ElevatorControl += 1.2F * f;
				if (Or.getTangage() < -60F) submaneuver++;
				break;

			case 2: // '\002'
				if (Or.getTangage() > -45F) {
					CT.AileronControl = -0.04F * Or.getKren();
					setSpeedMode(11);
					maxAOA = 7F;
				}
				CT.RudderControl = -0.1F * getAOS();
				dA = 0.5F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.8F * f;
				else CT.ElevatorControl += 0.4F * f;
				if (Or.getTangage() > AOA - 1.0F || Vwld.z > 0.0D) pop();
				break;
			}
			break;

		case 18: // '\022'
			if (first) {
				if (!isCapableOfACM()) pop();
				if (getSpeed() < Vmax * 0.75F) {
					push();
					push(22);
					pop();
					break;
				}
				AP.setStabAll(false);
				submaneuver = 0;
				setSpeedMode(6);
			}
			maxAOA = Vwld.z > 0.0D ? 7F : 12F;
			switch (submaneuver) {
			case 0: // '\0'
				CT.AileronControl = -0.04F * Or.getKren();
				CT.RudderControl = -0.1F * getAOS();
				if (Math.abs(Or.getKren()) < 2.0F) submaneuver++;
				break;

			case 1: // '\001'
				CT.AileronControl = -0.04F * Or.getKren();
				CT.RudderControl = -0.1F * getAOS();
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.4F * f;
				else CT.ElevatorControl += 0.8F * f;
				if (Or.getTangage() > 80F) submaneuver++;
				if (getSpeed() < Vmin * 1.5F) pop();
				break;

			case 2: // '\002'
				CT.RudderControl = -0.1F * getAOS() * (getSpeed() > 300F ? 1.0F : 0.0F);
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.4F * f;
				else CT.ElevatorControl += 0.4F * f;
				if (Or.getTangage() < 12F) submaneuver++;
				break;

			case 3: // '\003'
				if (Math.abs(Or.getKren()) < 60F) CT.ElevatorControl = 0.05F;
				CT.AileronControl = -0.04F * Or.getKren();
				CT.RudderControl = -0.1F * getAOS();
				if (Math.abs(Or.getKren()) < 30F) submaneuver++;
				break;

			case 4: // '\004'
				pop();
				break;
			}
			break;

		case 15: // '\017'
			if (first && getSpeed() < 0.35F * (Vmin + Vmax)) {
				pop();
			} else {
				if (Or.getKren() > 0.0F) setTurn(1000F, -80F, 200F);
				else setTurn(1000F, 80F, 200F);
				if (Alt > 500F && mn_time > 12F || dangerAggressiveness > 0.7F) pop();
			}
			break;

		case 103: // 'g'
			setTurn(1000F, 130F, 200F);
			if (mn_time > 4F || dangerAggressiveness > 0.7F) {
				mn_time = 0.0F;
				pop();
			}
			break;

		case 104: // 'h'
			setTurn(1000F, -130F, 200F);
			if (mn_time > 4F || dangerAggressiveness > 0.7F) {
				mn_time = 0.0F;
				pop();
			}
			break;

		case 20: // '\024'
			if (first) {
				wingman(false);
				setSpeedMode(6);
			}
			if (!isCapableOfBMP()) {
				setReadyToDie(true);
				pop();
			}
			if (getW().z > 0.0D) CT.RudderControl = 1.0F;
			else CT.RudderControl = -1F;
			if (AOA > AOA_Crit - 4F) Or.increment(0.0F, 0.01F * (AOA_Crit - 4F - AOA), 0.0F);
			if (AOA < -5F) Or.increment(0.0F, 0.01F * (-5F - AOA), 0.0F);
			if (AOA < AOA_Crit - 1.0F) pop();
			if (mn_time > 14F - (float) Skill * 4F || Alt < 50F) pop();
			break;

		case 21: // '\025'
			AP.setWayPoint(true);
			// TODO: DBW AI Mod Edits
			if (getAltitude() < super.AP.way.curr().z() - 100F && (super.actor instanceof TypeSupersonic)) super.CT.ElevatorControl += 0.025F;
			if (mn_time > 300F) pop();
			if (isTick(256, 0) && !actor.isTaskComplete() && (AP.way.isLast() && AP.getWayPointDistance() < 1500F || AP.way.isLanding())) World.onTaskComplete(actor);
			if (((Aircraft) actor).aircIndex() == 0 && !isReadyToReturn()) {
				World.cur();
				if (World.getPlayerAircraft() != null) {
					World.cur();
					if (((Aircraft) actor).getRegiment() == World.getPlayerAircraft().getRegiment()) {
						float f20 = 1E+012F;
						if (AP.way.curr().Action == 3) {
							f20 = AP.getWayPointDistance();
						} else {
							int k = AP.way.Cur();
							AP.way.next();
							if (AP.way.curr().Action == 3) f20 = AP.getWayPointDistance();
							AP.way.setCur(k);
						}
						if (Speak5minutes == 0 && 22000F < f20 && f20 < 30000F) {
							Voice.speak5minutes((Aircraft) actor);
							Speak5minutes = 1;
						}
						if (Speak1minute == 0 && f20 < 10000F) {
							Voice.speak1minute((Aircraft) actor);
							Speak1minute = 1;
							Speak5minutes = 1;
						}
						if ((WeWereInGAttack || WeWereInAttack) && mn_time > 5F) {
							if (!SpeakMissionAccomplished) {
								boolean flag2 = false;
								int l = AP.way.Cur();
								if (AP.way.curr().Action == 3 || AP.way.curr().getTarget() != null) break;
								do {
									if (AP.way.Cur() >= AP.way.size() - 1) break;
									AP.way.next();
									if (AP.way.curr().Action == 3 || AP.way.curr().getTarget() != null) flag2 = true;
								} while (true);
								AP.way.setCur(l);
								if (!flag2) {
									Voice.speakMissionAccomplished((Aircraft) actor);
									SpeakMissionAccomplished = true;
								}
							}
							if (!SpeakMissionAccomplished) {
								Speak5minutes = 0;
								Speak1minute = 0;
								SpeakBeginGattack = 0;
							}
							WeWereInGAttack = false;
							WeWereInAttack = false;
						}
					}
				}
			}
			if (((actor instanceof TypeBomber) || (actor instanceof TypeTransport)) && AP.way.curr() != null && AP.way.curr().Action == 3 && (AP.way.curr().getTarget() == null || (actor instanceof Scheme4))) {
				double d = Loc.z - World.land().HQ(Loc.x, Loc.y);
				if (d < 0.0D) d = 0.0D;
				if ((double) AP.getWayPointDistance() < (double) getSpeed() * Math.sqrt(d * 0.20386999845504761D) && !bombsOut) {
					if (CT.Weapons[3] != null && CT.Weapons[3][0] != null && CT.Weapons[3][0].countBullets() != 0 && !(CT.Weapons[3][0] instanceof BombGunPara)) Voice.airSpeaks((Aircraft) actor, 85, 1);
					bombsOut = true;
					AP.way.curr().Action = 0;
					if (Group != null) Group.dropBombs();
				}
			}
			setSpeedMode(3);
			if (AP.way.isLandingOnShip() && AP.way.isLanding()) {
				AP.way.landingAirport.rebuildLandWay(this);
				// TODO: DBW AI Mod Edits
				if (CT.bHasCockpitDoorControl && !CT.bNoCarrierCanopyOpen) AS.setCockpitDoor(actor, 1);
			}
			if (isTick(256, 0)) checkBlindSpots();
			break;

		case 23: // '\027'
			if (first) {
				wingman(false);
				if (getSpeedKMH() < Vmin * 1.25F) {
					push();
					push(22);
					pop();
					break;
				}
			}
			setSpeedMode(6);
			CT.AileronControl = -0.04F * Or.getKren();
			CT.RudderControl = -0.1F * getAOS();
			if (Or.getTangage() < 70F && getOverload() < maxG && AOA < 14F) CT.ElevatorControl += 0.5F * f;
			else CT.ElevatorControl -= 0.5F * f;
			if (Vwld.z < 1.0D) pop();
			break;

		case 24: // '\030'
			if (Leader == null || !Actor.isAlive(Leader.actor) || !Leader.isOk() || ((Maneuver) Leader).isBusy() && (!(Leader instanceof RealFlightModel) || !((RealFlightModel) Leader).isRealMode())) {
				if (Group.grTask == 7) {
					push();
					push(45);
					pop();
				} else {
					set_maneuver(0);
				}
				break;
			}
			if (actor instanceof TypeGlider) {
				if (Leader.AP.way.curr().Action != 0 && Leader.AP.way.curr().Action != 1) set_maneuver(49);
			} else if (Leader.getSpeed() < 30F || Leader.Loc.z - Engine.land().HQ_Air(Leader.Loc.x, Leader.Loc.y) < 51.5D || Leader.Loc.z - Engine.land().HQ_Air(Leader.Loc.x, Leader.Loc.y) < 51.5D && Leader.getSpeed() < Vmin * 1.3F) {
				airClient = Leader;
				if (Leader.getSpeed() < 15F && Leader.Loc.z - Engine.land().HQ_Air(Leader.Loc.x, Leader.Loc.y) < 2D) {
					Group.setGroupTask(7);
					push();
					push(45);
					pop();
				} else {
					push();
					push(82);
					pop();
				}
				break;
			}
			if (Leader.AP.way.isLanding()) {
				if (Leader.Wingman != this) {
					push(8);
					push(8);
					push(World.Rnd().nextBoolean() ? 8 : 48);
					push(World.Rnd().nextBoolean() ? 8 : 48);
					pop();
				}
				Leader = null;
				pop();
			} else {
				AP.way.setCur(Leader.AP.way.Cur());
				if (Leader.Wingman != this) {
					if (!bombsOut && ((Maneuver) Leader).bombsOut && Leader.isCapableOfACM() && !Leader.isReadyToDie() && !Leader.isReadyToReturn()) {
						bombsOut = true;
						for (Maneuver maneuver1 = this; maneuver1.Wingman != null;) {
							maneuver1 = (Maneuver) maneuver1.Wingman;
							maneuver1.bombsOut = true;
						}

					}
					if (CT.BayDoorControl != Leader.CT.BayDoorControl) {
						CT.BayDoorControl = Leader.CT.BayDoorControl;
						for (Pilot pilot = (Pilot) this; pilot.Wingman != null;) {
							pilot = (Pilot) pilot.Wingman;
							pilot.CT.BayDoorControl = CT.BayDoorControl;
						}

					}
				}
				airClient = Leader;
				tmpOr.setAT0(airClient.Vwld);
				tmpOr.increment(0.0F, airClient.getAOA(), 0.0F);
				Ve.set(followOffset);
				Ve.x -= 300D;
				tmpV3f.sub(followTargShift, followCurShift);
				if (tmpV3f.lengthSquared() < 0.5D) followTargShift.set(World.cur().rnd.nextFloat(-0F, 10F), World.cur().rnd.nextFloat(-5F, 5F), World.cur().rnd.nextFloat(-3.5F, 3.5F));
				tmpV3f.normalize();
				tmpV3f.scale(2.0F * f);
				followCurShift.add(tmpV3f);
				Ve.add(followCurShift);
				tmpOr.transform(Ve, Po);
				Po.scale(-1D);
				Po.add(airClient.Loc);
				Ve.sub(Po, Loc);
				Or.transformInv(Ve);
				dist = (float) Ve.length();
				if (followOffset.x > 600D) {
					Ve.set(followOffset);
					Ve.x -= 0.5D * followOffset.x;
					tmpOr.transform(Ve, Po);
					Po.scale(-1D);
					Po.add(airClient.Loc);
					Ve.sub(Po, Loc);
					Or.transformInv(Ve);
				}
				Ve.normalize();
				if ((double) dist > 600D + Ve.x * 400D) {
					push();
					push(53);
					pop();
				} else {
					if ((actor instanceof TypeTransport) && (double) Vmax < 70D) farTurnToDirection(6.2F);
					else attackTurnToDirection(dist, f, 10F);
					setSpeedMode(10);
					tailForStaying = Leader;
					tailOffset.set(followOffset);
					tailOffset.scale(-1D);
					if (isTick(256, 0)) checkBlindSpots();
				}
			}
			break;

		case 65: // 'A'
			if ((!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) && !bKeepOrdnance) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			if (airClient == null || !Actor.isAlive(airClient.actor) || !isOk()) {
				set_maneuver(0);
			} else {
				Maneuver maneuver2 = (Maneuver) airClient;
				Maneuver maneuver7 = (Maneuver) ((Maneuver) airClient).danger;
				if (maneuver2.getDangerAggressiveness() >= 0.8F - (float) Skill * 0.2F && maneuver7 != null && hasCourseWeaponBullets()) {
					Voice.speakCheckYour6((Aircraft) maneuver2.actor, (Aircraft) maneuver7.actor);
					Voice.speakHelpFromAir((Aircraft) actor, 1);
					set_task(6);
					target = maneuver7;
					set_maneuver(27);
				}
				Ve.sub(airClient.Loc, Loc);
				Or.transformInv(Ve);
				dist = (float) Ve.length();
				Ve.normalize();
				attackTurnToDirection(dist, f, 10F + (float) Skill * 8F);
				if (Alt > 50F) setSpeedMode(1);
				else setSpeedMode(6);
				tailForStaying = airClient;
				tailOffset.set(followOffset);
				tailOffset.scale(-1D);
				if ((double) dist > 600D + Ve.x * 400D && get_maneuver() != 27) {
					push();
					push(53);
					pop();
				} else if (((Aircraft) actor).aircIndex() == 2 && mn_time > 100F) pop();
			}
			break;

		case 76: // 'L'
			if ((!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) && !bKeepOrdnance) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			if (airClient == null || !Actor.isAlive(airClient.actor) || !isOk()) {
				set_maneuver(0);
			} else {
				Maneuver maneuver3 = (Maneuver) airClient;
				Maneuver maneuver8 = (Maneuver) ((Maneuver) airClient).danger;
				if (maneuver3.getDangerAggressiveness() >= 0.8F - (float) Skill * 0.25F && maneuver8 != null && hasCourseWeaponBullets()) {
					Voice.speakCheckYour6((Aircraft) maneuver3.actor, (Aircraft) maneuver8.actor);
					Voice.speakHelpFromAir((Aircraft) actor, 1);
					set_task(6);
					target = maneuver8;
					set_maneuver(27);
				}
				Ve.sub(airClient.Loc, Loc);
				Ve.add(spreadV3d);
				Or.transformInv(Ve);
				dist = (float) Ve.length();
				Ve.normalize();
				attackTurnToDirection(dist, f, 10F + (float) Skill * 8F);
				if (Alt > 20F) setSpeedMode(1);
				else setSpeedMode(6);
				tailForStaying = airClient;
				tailOffset.set(followOffset);
				tailOffset.scale(-1D);
				if ((double) dist > 600D + Ve.x * 500D && get_maneuver() != 27) {
					push();
					push(53);
					pop();
				} else if (((Aircraft) actor).aircIndex() == 2 && mn_time > 100F) pop();
			}
			break;

		case 53: // '5'
			if (airClient == null || !Actor.isAlive(airClient.actor) || !isOk()) {
				airClient = null;
				set_maneuver(0);
			} else {
				maxAOA = 5F;
				Ve.set(followOffset);
				Ve.x -= 300D;
				tmpOr.setAT0(airClient.Vwld);
				tmpOr.increment(0.0F, 4F, 0.0F);
				tmpOr.transform(Ve, Po);
				Po.scale(-1D);
				Po.add(airClient.Loc);
				Ve.sub(Po, Loc);
				Or.transformInv(Ve);
				dist = (float) Ve.length();
				Ve.normalize();
				if (Vmax < 83F) farTurnToDirection(8.5F);
				else farTurnToDirection(7F);
				float f21 = (Energy - airClient.Energy) * 0.1019F;
				if ((double) f21 < -50D + followOffset.z) setSpeedMode(9);
				else setSpeedMode(10);
				tailForStaying = airClient;
				tailOffset.set(followOffset);
				tailOffset.scale(-1D);
				if ((double) dist < 500D + Ve.x * 200D) {
					pop();
				} else {
					if (AOA > 12F && Alt > 15F) Or.increment(0.0F, 12F - AOA, 0.0F);
					if (mn_time > 30F && (Ve.x < 0.0D || dist > 10000F)) pop();
				}
			}
			break;

		case 68: // 'D'
			if (airClient == null || !Actor.isAlive(airClient.actor) || !isOk()) {
				set_maneuver(0);
			} else {
				Maneuver maneuver4 = (Maneuver) airClient;
				Maneuver maneuver9 = (Maneuver) ((Maneuver) airClient).danger;
				if (maneuver4.getDangerAggressiveness() >= 1.0F - (float) Skill * 0.3F && maneuver9 != null && hasCourseWeaponBullets()) {
					tmpV3d.sub(maneuver4.Vwld, maneuver9.Vwld);
					if (tmpV3d.length() < 170D) {
						set_task(6);
						target = maneuver9;
						set_maneuver(27);
					}
				}
				maxAOA = 5F;
				Ve.set(followOffset);
				Ve.x -= 300D;
				tmpOr.setAT0(airClient.Vwld);
				tmpOr.increment(0.0F, 4F, 0.0F);
				tmpOr.transform(Ve, Po);
				Po.scale(-1D);
				Po.add(airClient.Loc);
				Ve.sub(Po, Loc);
				Or.transformInv(Ve);
				dist = (float) Ve.length();
				Ve.normalize();
				if (Vmax < 83F) farTurnToDirection(8.5F);
				else farTurnToDirection(7F);
				setSpeedMode(10);
				tailForStaying = airClient;
				tailOffset.set(followOffset);
				tailOffset.scale(-1D);
				if ((double) dist < 500D + Ve.x * 200D) {
					pop();
				} else {
					if (AOA > 12F && Alt > 15F) Or.increment(0.0F, 12F - AOA, 0.0F);
					if (mn_time > 10F || Ve.x < 0.0D || dist > 3000F) pop();
				}
			}
			break;

		case 74: // 'J'
			if (airClient == null || !Actor.isAlive(airClient.actor) || !isOk()) {
				set_maneuver(0);
			} else {
				Maneuver maneuver5 = (Maneuver) airClient;
				Maneuver maneuver10 = (Maneuver) ((Maneuver) airClient).danger;
				if (maneuver10 != null && hasCourseWeaponBullets() && !maneuver10.isTakenMortalDamage()) {
					tmpV3dToDanger.sub(maneuver10.Loc, Loc);
					dist = (float) tmpV3dToDanger.length();
					Or.transformInv(tmpV3dToDanger);
					tmpV3dToDanger.normalize();
					if (maneuver5.getDangerAggressiveness() >= 0.5F || dist < 1000F && tmpV3dToDanger.x > -0.33000000000000002D || dist < 500F) {
						set_task(6);
						target = maneuver10;
						push();
						set_maneuver(27);
					}
				}
				Ve.sub(airClient.Loc, Loc);
				Ve.add(spreadV3d);
				Or.transformInv(Ve);
				dist = (float) Ve.length();
				Ve.normalize();
				attackTurnToDirection(dist, f, 10F + (float) Skill * 8F);
				if (Alt > 50F) setSpeedMode(1);
				else setSpeedMode(6);
				tailForStaying = airClient;
				tailOffset.set(followOffset);
				tailOffset.scale(-1D);
				if (dist > 4500F && get_maneuver() != 27) {
					push();
					push(53);
					pop();
				} else if (((Aircraft) actor).aircIndex() == 2 && mn_time > 120F) pop();
			}
			break;

		case 75: // 'K'
			if ((!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) && !bKeepOrdnance) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			if (airClient == null || !Actor.isAlive(airClient.actor) || !isOk()) {
				set_maneuver(0);
			} else {
				Maneuver maneuver11 = (Maneuver) ((Maneuver) airClient).danger;
				if (maneuver11 != null && hasCourseWeaponBullets() && !maneuver11.isTakenMortalDamage()) {
					tmpV3dToDanger.sub(maneuver11.Loc, Loc);
					dist = (float) tmpV3dToDanger.length();
					Or.transformInv(tmpV3dToDanger);
					tmpV3dToDanger.normalize();
					if (dist < 700F && tmpV3dToDanger.x > -0.20000000000000001D) {
						set_task(6);
						target = maneuver11;
						set_maneuver(27);
					}
				}
				if (target == null) target = ((Maneuver) airClient).target;
				if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets()) {
					target = null;
					clear_stack();
					set_maneuver(0);
				} else {
					wingmanVsFighter(f);
					setSpeedMode(9);
					if (AOA > AOA_Crit - 1.0F && Alt > 15F) Or.increment(0.0F, 0.01F * (AOA_Crit - 1.0F - AOA), 0.0F);
					if (mn_time > 60F) pop();
				}
			}
			break;

		case 82: // 'R'
			if (airClient == null || !Actor.isValid(airClient.actor) || !isOk()) {
				airClient = null;
				set_maneuver(0);
			} else {
				tmpOr.setAT0(airClient.Vwld);
				tmpOr.increment(0.0F, 4F, 0.0F);
				Ve.set(followOffset);
				Ve.x -= 300D;
				tmpOr.transform(Ve, Po);
				Po.scale(-1D);
				Po.add(airClient.Loc);
				Po.z -= followOffset.z;
				Ve.sub(Po, Loc);
				Or.transformInv(Ve);
				dist = (float) Ve.length();
				Ve.normalize();
				farTurnToDirection();
				if (airClient != null && (airClient.getSpeed() < Vmin * 1.5F || dist > 200F)) setSpeedMode(10);
				else setSpeedMode(1);
				tailForStaying = airClient;
				tailOffset.set(followOffset);
				tailOffset.scale(-1D);
				if (airClient != null && airClient.getSpeed() < Vmin * 1.35F && Vwld.z < 0.0D) {
					clear_stack();
					push(10);
					set_maneuver(2);
				} else {
					if (AOA > 12F && Alt > 15F) Or.increment(0.0F, 12F - AOA, 0.0F);
					if (mn_time > 5F && (Ve.x < 0.0D || dist > 3000F)) pop();
					if (mn_time > 15F) pop();
				}
			}
			break;

		case 59: // ';'
			if (airClient == null || !Actor.isValid(airClient.actor) || !isOk()) {
				airClient = null;
				set_maneuver(0);
			} else {
				maxAOA = 5F;
				if (first) followOffset.set(1000F * (float) Math.sin((float) beNearOffsPhase * 0.7854F), 1000F * (float) Math.cos((float) beNearOffsPhase * 0.7854F), -400D);
				Ve.set(followOffset);
				Ve.x -= 300D;
				tmpOr.setAT0(airClient.Vwld);
				tmpOr.increment(0.0F, 4F, 0.0F);
				tmpOr.transform(Ve, Po);
				Po.scale(-1D);
				Po.add(airClient.Loc);
				Ve.sub(Po, Loc);
				Or.transformInv(Ve);
				dist = (float) Ve.length();
				Ve.normalize();
				farTurnToDirection();
				setSpeedMode(2);
				tailForStaying = airClient;
				tailOffset.set(followOffset);
				tailOffset.scale(-1D);
				if (dist < 300F) {
					beNearOffsPhase++;
					if (beNearOffsPhase > 3) beNearOffsPhase = 0;
					float f24 = (float) Math.sqrt(followOffset.x * followOffset.x + followOffset.y * followOffset.y);
					followOffset.set(f24 * (float) Math.sin((float) beNearOffsPhase * 1.5708F), f24 * (float) Math.cos((float) beNearOffsPhase * 1.5708F), followOffset.z);
				}
				if (AOA > 12F && Alt > 15F) Or.increment(0.0F, 12F - AOA, 0.0F);
				if (mn_time > 15F && (Ve.x < 0.0D || dist > 3000F)) pop();
				if (mn_time > 30F) pop();
			}
			break;

		case 63: // '?'
			if ((!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) && !bKeepOrdnance) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || !hasCourseWeaponBullets()) {
				target = null;
				clear_stack();
				set_maneuver(3);
			} else if (target.getSpeedKMH() < 45F && target.Loc.z - Engine.land().HQ_Air(target.Loc.x, target.Loc.y) < 50D && target.actor.getArmy() != actor.getArmy()) {
				target_ground = target.actor;
				set_task(7);
				clear_stack();
				set_maneuver(43);
			} else {
				if ((actor instanceof HE_LERCHE3) && ((HE_LERCHE3) actor).bToFire) {
					CT.WeaponControl[2] = true;
					((HE_LERCHE3) actor).bToFire = false;
				}
				if ((actor instanceof TA_183) && ((TA_183) actor).bToFire) {
					CT.WeaponControl[2] = true;
					((TA_183) actor).bToFire = false;
				}
				if ((actor instanceof TA_152C) && ((TA_152C) actor).bToFire) {
					CT.WeaponControl[2] = true;
					((TA_152C) actor).bToFire = false;
				}
				if (TargV.z < -100D) fighterUnderBomber(f);
				else fighterVsBomber(f);
				if (AOA > AOA_Crit - 2.0F && Alt > 15F) Or.increment(0.0F, 0.01F * (AOA_Crit - 2.0F - AOA), 0.0F);
			}
			break;

		case 27: // '\033'
			if ((!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) && !bKeepOrdnance) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets()) {
				target = null;
				clear_stack();
				set_maneuver(0);
			} else if (isTick(77, 0) && !target.isOk() && World.Rnd().nextInt(0, 1000) < 40 * (Skill + 1)) {
				target = null;
				clear_stack();
				set_maneuver(0);
			} else if (target.getSpeedKMH() < 45F && target.Loc.z - Engine.land().HQ_Air(target.Loc.x, target.Loc.y) < 50D && target.actor.getArmy() != actor.getArmy()) {
				target_ground = target.actor;
				set_task(7);
				clear_stack();
				set_maneuver(43);
			} else {
				if (first && (actor instanceof TypeAcePlane)) {
					if (target != null && target.actor != null && target.actor.getArmy() != actor.getArmy()) target.Skill = 0;
					if (danger != null && danger.actor != null && danger.actor.getArmy() != actor.getArmy()) danger.Skill = 0;
				}
				if (target.actor.getArmy() != actor.getArmy()) ((Maneuver) target).danger = this;
				if (isTick(64, 0)) {
					float f22 = (target.Energy - Energy) * 0.1019F;
					Ve.sub(target.Loc, Loc);
					Or.transformInv(Ve);
					Ve.normalize();
					float f25 = (470F + (float) Ve.x * 120F) - f22;
					if (f25 < 0.0F) {
						clear_stack();
						set_maneuver(62);
					}
				}
				minElevCoeff = 20F;
				fighterVsFighter(f);
				if (AOA > AOA_Crit - 1.0F && Alt > 15F) Or.increment(0.0F, 0.01F * (AOA_Crit - 1.0F - AOA), 0.0F);
				if (mn_time > 100F) {
					target = null;
					pop();
				}
			}
			break;

		case 62: // '>'
			if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets()) {
				if (((Aircraft) actor).aircIndex() == 0) Group.chooseTargetGroup();
				target = null;
				clear_stack();
				set_maneuver(0);
			} else if (target.getSpeedKMH() < 45F && target.Loc.z - Engine.land().HQ_Air(target.Loc.x, target.Loc.y) < 50D && target.actor.getArmy() != actor.getArmy()) {
				target_ground = target.actor;
				set_task(7);
				clear_stack();
				set_maneuver(43);
			} else {
				if (first && (actor instanceof TypeAcePlane)) {
					if (target != null && target.actor != null && target.actor.getArmy() != actor.getArmy()) target.Skill = 0;
					if (danger != null && danger.actor != null && danger.actor.getArmy() != actor.getArmy()) danger.Skill = 0;
				}
				if (isTick(37, 0) && !target.isOk() && World.Rnd().nextInt(0, 100) < 20 * (Skill + 1)) {
					target = null;
					clear_stack();
					set_maneuver(0);
				} else if (isTick(164, 0) && !Group.inCorridor(Loc)) {
					target = null;
					clear_stack();
					set_maneuver(0);
				} else {
					if (target.actor.getArmy() != actor.getArmy()) ((Maneuver) target).danger = this;
					goodFighterVsFighter(f);
				}
			}
			break;

		case 80: // 'P'
			if ((!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) && !bKeepOrdnance) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets()) {
				target = null;
				clear_stack();
				set_maneuver(0);
			} else if (target.getSpeedKMH() < 45F && target.Loc.z - Engine.land().HQ_Air(target.Loc.x, target.Loc.y) < 50D && target.actor.getArmy() != actor.getArmy()) {
				target_ground = target.actor;
				set_task(7);
				clear_stack();
				set_maneuver(43);
			} else {
				if (first && (actor instanceof TypeAcePlane)) {
					if (target != null && target.actor != null && target.actor.getArmy() != actor.getArmy()) target.Skill = 0;
					if (danger != null && danger.actor != null && danger.actor.getArmy() != actor.getArmy()) danger.Skill = 0;
				}
				if (target.actor.getArmy() != actor.getArmy()) ((Maneuver) target).danger = this;
				switch (submaneuver) {
				case 0: // '\0'
					tmpLoc.set(target.Loc);
					bracketSide = World.Rnd().nextInt(0, 1);
					scaledApproachV.set(World.Rnd().nextFloat(800F, 1500F), (float) (1000 - bracketSide * 2000) + World.Rnd().nextFloat(-500F, 250F), World.Rnd().nextFloat(-100F, 1250F));
					target.Or.transformInv(scaledApproachV);
					tmpLoc.add(scaledApproachV);
					Ve.sub(tmpLoc, Loc);
					submaneuver++;
					break;

				case 1: // '\001'
					target.Loc.add(scaledApproachV);
					Ve.sub(target.Loc, Loc);
					break;
				}
				dist = (float) Ve.length();
				Or.transformInv(Ve);
				Ve.normalize();
				attackTurnToDirection(dist, f, 4F + (float) Skill * 4F);
				setSpeedMode(9);
				if (AOA > AOA_Crit - 1.0F && Alt > 15F) Or.increment(0.0F, 0.01F * (AOA_Crit - 1.0F - AOA), 0.0F);
				if (mn_time > 20F || dist < 1000F) {
					submaneuver = 0;
					if (Group.nOfAirc > 2 && Group.airc[2] != null) {
						Maneuver maneuver12 = (Maneuver) Group.airc[2].FM;
						maneuver12.clear_stack();
						maneuver12.set_maneuver(27);
					}
					push(27);
					pop();
				}
			}
			break;

		case 98: // 'b'
			if (submaneuver == 1 && (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets())) {
				target = null;
				clear_stack();
				set_maneuver(0);
			} else {
				switch (submaneuver) {
				case 0: // '\0'
					target = Group.setAAttackObject(((Aircraft) actor).aircIndex());
					gattackCounter = 0;
					submaneuver++;
					break;

				case 1: // '\001'
					boomAttack(f);
					setSpeedMode(11);
					break;
				}
				if (gattackCounter > 51 || mn_time > 120F) {
					submaneuver = 0;
					gattackCounter = 0;
					push(97);
					push(58);
					pop();
				}
			}
			break;

		case 81: // 'Q'
			wingman(false);
			target = Group.setAAttackObject(((Aircraft) actor).aircIndex());
			clear_stack();
			set_maneuver(27);
			break;

		case 92: // '\\'
			if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets()) {
				target = null;
				clear_stack();
				set_maneuver(0);
				break;
			}
			switch (submaneuver) {
			default:
				break;

			case 0: // '\0'
				CT.AileronControl = -0.04F * Or.getKren();
				CT.RudderControl = -0.1F * getAOS();
				dA = (getSpeedKMH() - 180F - Or.getTangage() * 10F - getVertSpeed() * 5F) * 0.004F;
				CT.ElevatorControl = dA;
				sub_Man_Count++;
				if (sub_Man_Count > ((Aircraft) actor).aircIndex() * World.Rnd().nextInt(40, 75)) submaneuver++;
				break;

			case 1: // '\001'
				tmpLoc.set(target.Loc);
				scaledApproachV.set(World.Rnd().nextFloat(50F, 250F), (float) (10 - bracketSide * 20) + World.Rnd().nextFloat(-50F, 50F), World.Rnd().nextFloat(50F, 120F));
				target.Or.transformInv(scaledApproachV);
				tmpLoc.add(scaledApproachV);
				submaneuver++;
				break;
			}
			// fall through

		case 93: // ']'
			if (maneuver == 93) {
				if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets()) {
					target = null;
					clear_stack();
					set_maneuver(0);
					break;
				}
				switch (submaneuver) {
				case 0: // '\0'
					tmpLoc.set(target.Loc);
					switch (((Aircraft) actor).aircIndex()) {
					case 0: // '\0'
						scaledApproachV.set(World.Rnd().nextFloat(800F, 1200F), World.Rnd().nextFloat(-200F, 200F), World.Rnd().nextFloat(800F, 1200F));
						break;

					case 1: // '\001'
						scaledApproachV.set(World.Rnd().nextFloat(-100F, 100F), World.Rnd().nextFloat(-800F, -1200F), World.Rnd().nextFloat(800F, 1200F));
						break;

					case 2: // '\002'
						scaledApproachV.set(World.Rnd().nextFloat(-100F, 100F), World.Rnd().nextFloat(800F, 1200F), World.Rnd().nextFloat(800F, 1200F));
						break;

					case 3: // '\003'
						scaledApproachV.set(World.Rnd().nextFloat(-800F, -1200F), World.Rnd().nextFloat(-200F, 200F), World.Rnd().nextFloat(800F, 1200F));
						break;

					default:
						scaledApproachV.set(World.Rnd().nextFloat(-800F, -1200F), World.Rnd().nextFloat(-100F, 100F), World.Rnd().nextFloat(900F, 1100F));
						break;
					}
					target.Or.transformInv(scaledApproachV);
					tmpLoc.add(scaledApproachV);
					submaneuver += 2;
					break;
				}
			}
			// fall through

		case 94: // '^'
			if ((!(this instanceof RealFlightModel) || !((RealFlightModel) this).isRealMode()) && !bKeepOrdnance) {
				bombsOut = true;
				CT.dropFuelTanks();
			}
			if (!isOk()) {
				set_maneuver(0);
			} else {
				switch (submaneuver) {
				case 2: // '\002'
					Ve.sub(tmpLoc, Loc);
					if (dist < 500F) submaneuver++;
					break;

				case 3: // '\003'
					Ve.sub(target.Loc, Loc);
					Ve.add(scaledApproachV);
					break;
				}
				if (submaneuver > 1) {
					dist = (float) Ve.length();
					Or.transformInv(Ve);
					Ve.normalize();
					attackTurnToDirection(dist, f, 10F + (float) Skill * 8F);
				}
				setSpeedMode(9);
				if (mn_time > 20F || submaneuver == 3 && dist < 1000F) {
					sub_Man_Count = 0;
					submaneuver = 0;
					push(27);
					pop();
				}
			}
			break;

		case 105: // 'i'
			CT.setPowerControl(1.1F);
			setTurn(500F, 0.0F, 0.0F);
			if (mn_time > 715F / Vmax) pop();
			break;

		case 99: // 'c'
			if (target == null || !Actor.isValid(target.actor) || target.isTakenMortalDamage() || target.actor.getArmy() == actor.getArmy() || !hasCourseWeaponBullets()) {
				target = null;
				clear_stack();
				set_maneuver(0);
			} else {
				switch (submaneuver) {
				default:
					break;

				case 0: // '\0'
					oldTarget = target;
					cloudHeight = Mission.curCloudsHeight();
					Group.setEnemyConvPoint(target, actor);
					submaneuver++;
					break;

				case 1: // '\001'
					lastKnownTargetLoc.set(target.Loc);
					scaledApproachV.set(target.Vwld);
					if (Group != null && Group.outNum < 0) {
						switch (Group.outNum) {
						case -1:
							if (Group.nOfAirc < 3) timeToSearchTarget = 50 + World.Rnd().nextInt(0, 100);
							else timeToSearchTarget = 5 + World.Rnd().nextInt(0, 20);
							desiredAlt = cloudHeight + 1000F + (float) World.Rnd().nextInt(-200, 500);
							scaledApproachV.add(World.Rnd().nextInt(-50, 50), World.Rnd().nextInt(-50, 50), World.Rnd().nextInt(-25, 25));
							break;

						case -2:
							switch (((Aircraft) actor).aircIndex()) {
							case 0: // '\0'
								scaledApproachV.add(World.Rnd().nextInt(-50, 50), World.Rnd().nextInt(-50, 50), World.Rnd().nextInt(10, 50));
								desiredAlt = cloudHeight + 1500F + (float) World.Rnd().nextInt(-200, 500);
								break;

							case 1: // '\001'
								scaledApproachV.add(World.Rnd().nextInt(-50, -120), World.Rnd().nextInt(-50, -120), World.Rnd().nextInt(0, 50));
								desiredAlt = cloudHeight - (float) World.Rnd().nextInt(200, 500);
								break;

							case 2: // '\002'
								scaledApproachV.add(World.Rnd().nextInt(20, 50), World.Rnd().nextInt(20, 50), World.Rnd().nextInt(-20, 50));
								desiredAlt = cloudHeight + 1500F + (float) World.Rnd().nextInt(-200, 500);
								break;

							case 3: // '\003'
								scaledApproachV.add(World.Rnd().nextInt(50, 120), World.Rnd().nextInt(50, 120), World.Rnd().nextInt(-50, 30));
								desiredAlt = cloudHeight - (float) World.Rnd().nextInt(200, 500);
								break;
							}
							timeToSearchTarget = 50 + World.Rnd().nextInt(0, -100 * Group.outNum);
							break;

						case -3:
							switch (((Aircraft) actor).aircIndex()) {
							case 0: // '\0'
								scaledApproachV.add(World.Rnd().nextInt(20, 50), World.Rnd().nextInt(20, 50), World.Rnd().nextInt(10, 50));
								desiredAlt = cloudHeight + 1500F + (float) World.Rnd().nextInt(-200, 500);
								break;

							case 1: // '\001'
								scaledApproachV.add(World.Rnd().nextInt(-50, -120), World.Rnd().nextInt(-50, -120), World.Rnd().nextInt(0, 50));
								desiredAlt = cloudHeight + 1500F + (float) World.Rnd().nextInt(-200, 500);
								break;

							case 2: // '\002'
								scaledApproachV.add(World.Rnd().nextInt(20, 50), World.Rnd().nextInt(20, 50), World.Rnd().nextInt(-20, 50));
								desiredAlt = cloudHeight - (float) World.Rnd().nextInt(200, 500);
								break;

							case 3: // '\003'
								scaledApproachV.add(World.Rnd().nextInt(50, 120), World.Rnd().nextInt(50, 120), World.Rnd().nextInt(-50, 30));
								desiredAlt = cloudHeight - (float) World.Rnd().nextInt(200, 500);
								break;
							}
							timeToSearchTarget = 100 + World.Rnd().nextInt(0, 200);
							break;

						default:
							timeToSearchTarget = 100 + World.Rnd().nextInt(0, 200);
							desiredAlt = cloudHeight + 1500F + (float) World.Rnd().nextInt(-200, 500);
							break;
						}
					} else {
						if (Group != null && Group.outNum > 0) {
							getDifferentTarget();
							pop();
							break;
						}
						timeToSearchTarget = 100 + World.Rnd().nextInt(0, 200 / Group.nOfAirc);
						desiredAlt = cloudHeight + 1500F + (float) World.Rnd().nextInt(-200, 500);
					}
					Ve.sub(target.Loc, Loc);
					dist = (float) Ve.length();
					lastKnownTargetLoc.x += scaledApproachV.x * (double) Time.tickConstLenFs();
					lastKnownTargetLoc.y += scaledApproachV.y * (double) Time.tickConstLenFs();
					lastKnownTargetLoc.z += scaledApproachV.z * (double) Time.tickConstLenFs();
					submanDelay = (int) (dist * 0.1F);
					if (submanDelay > 5000) submanDelay = 500;
					submaneuver++;
					break;

				case 2: // '\002'
					lastKnownTargetLoc.x += scaledApproachV.x * (double) Time.tickConstLenFs();
					lastKnownTargetLoc.y += scaledApproachV.y * (double) Time.tickConstLenFs();
					lastKnownTargetLoc.z += scaledApproachV.z * (double) Time.tickConstLenFs();
					Ve.sub(lastKnownTargetLoc, Loc);
					dist = (float) Ve.length();
					if (dist < 200F || sub_Man_Count > 100) {
						sub_Man_Count = 0;
						if (!VisCheck.seekCheck((Aircraft) actor, (Aircraft) target.actor)) {
							float f26 = Math.abs(target.getOverload());
							float f28 = dist * 0.05F;
							if (World.Rnd().nextFloat(0.0F, 70F + (float) Skill * 10F) > 10F + f26 * 5F + f28) submaneuver--;
							else submaneuver++;
						} else {
							if (target != null) Group.setEnemyConvPoint(target, actor);
							pop();
						}
					}
					Or.transformInv(Ve);
					Ve.normalize();
					attackTurnToDirection(dist, f, 2.0F + (float) Skill * 2.0F);
					break;

				case 3: // '\003'
					if (sub_Man_Count > 100) {
						sub_Man_Count = 0;
						if (!VisCheck.seekCheck((Aircraft) actor, (Aircraft) target.actor)) {
							submaneuver--;
							Point3d point3d = Group.getEnemyConvPoint(target, actor);
							if (point3d != null) {
								dist = (float) Loc.distance(point3d);
								if (dist > 700F) {
									point3d.z += World.Rnd().nextInt(-200, 200);
									point3d.y += World.Rnd().nextFloat(-dist / 10F, dist / 10F);
									point3d.x += World.Rnd().nextFloat(-dist / 10F, -dist / 10F);
								}
								lastKnownTargetLoc.set(point3d);
								scaledApproachV.set(target.Vwld);
							}
						} else {
							pop();
						}
					}
					if ((float) timeToSearchTarget < mn_time) {
						getDifferentTarget();
						if (target != oldTarget) pop();
						else if (((Aircraft) actor).aircIndex() == 0) {
							AirGroupList.delAirGroup(Group.enemies, 0, ((Maneuver) target).Group);
							Group.setEnemyFighters();
							clear_stack();
							set_maneuver(0);
						} else {
							clear_stack();
							set_maneuver(0);
						}
					}
					sub_Man_Count++;
					break;
				}
				if (submaneuver == 3) {
					setSpeedMode(7);
					smConstPower = 0.9F;
					dA = Or.getKren();
					if (dA > 0.0F) dA -= 35F;
					else dA -= 325F;
					if (dA < -180F) dA += 360F;
					dA = -0.01F * dA;
					CT.AileronControl = dA;
					dA = 0.002F * ((desiredAlt - (float) Loc.z) + 250F);
					if (dA > 0.66F) dA = 0.66F;
					CT.ElevatorControl = -0.04F * (Or.getTangage() - 1.0F) + dA;
				}
			}
			break;

		case 70: // 'F'
			checkGround = false;
			checkStrike = false;
			frequentControl = true;
			stallable = false;
			if (actor instanceof HE_LERCHE3) switch (submaneuver) {
			case 0: // '\0'
				AP.setStabAll(false);
				submaneuver++;
				sub_Man_Count = 0;
				// fall through

			case 1: // '\001'
				if (sub_Man_Count == 0) CT.AileronControl = World.cur().rnd.nextFloat(-0.15F, 0.15F);
				CT.AirBrakeControl = 1.0F;
				CT.setPowerControl(1.0F);
				CT.ElevatorControl = Aircraft.cvt(Or.getTangage(), 0.0F, 60F, 1.0F, 0.0F);
				if (Or.getTangage() > 30F) {
					submaneuver++;
					sub_Man_Count = 0;
				}
				sub_Man_Count++;
				break;

			case 2: // '\002'
				CT.AileronControl = 0.0F;
				CT.ElevatorControl = 0.0F;
				CT.setPowerControl(0.1F);
				Or.increment(0.0F, (float) ((double) f * 0.10000000000000001D * (double) sub_Man_Count * (90D - (double) Or.getTangage())), 0.0F);
				if (Or.getTangage() > 89F) {
					saveOr.set(Or);
					submaneuver++;
				}
				sub_Man_Count++;
				break;

			case 3: // '\003'
				CT.ElevatorControl = 0.0F;
				if (Alt > 10F) CT.setPowerControl(0.33F);
				else CT.setPowerControl(0.0F);
				if (Alt < 20F) {
					if (Vwld.z < -4D) Vwld.z *= 0.94999999999999996D;
					if (Vwld.lengthSquared() < 1.0D) EI.setEngineStops();
				}
				Or.set(saveOr);
				if (mn_time > 100F) {
					Vwld.set(0.0D, 0.0D, 0.0D);
					MsgDestroy.Post(Time.current() + 12000L, actor);
				}
				break;
			}
			break;

		case 25: // '\031'
			wingman(false);
			if (AP.way.isLanding()) {
				if (AP.way.isLandingOnShip()) {
					AP.way.landingAirport.rebuildLandWay(this);
					if (CT.GearControl == 1.0F && CT.arrestorControl < 1.0F && !Gears.onGround()) AS.setArrestor(actor, 1);
				}
				if (first) {
					AP.setWayPoint(true);
					doDumpBombsPassively();
					submaneuver = 0;
					if (super.actor instanceof TypeFastJet) CT.FlapsControl = 1.0F;
				}
				if ((actor instanceof HE_LERCHE3) && Alt < 50F) maneuver = 70;
				AP.way.curr().getP(Po);
				int i1 = AP.way.Cur();
				float f29 = (float) Loc.z - AP.way.last().z();
				AP.way.setCur(i1);
				Alt = Math.min(Alt, f29);
				Po.add(0.0D, 0.0D, -3D);
				Ve.sub(Po, Loc);
				float f30 = (float) Ve.length();
				boolean flag4 = Alt > (((EI.engines[0].getType() == 2 || EI.engines[0].getType() == 3) ? 10.0F : 4.5F) + Gears.H) && AP.way.Cur() < 8;
				if (AP.way.isLandingOnShip()) flag4 = Alt > (((EI.engines[0].getType() == 2 || EI.engines[0].getType() == 3) ? 10.0F : 4.5F) + Gears.H) && AP.way.Cur() < 8;
				if (!flag4) bTouchingDown = true;
				if (!bTouchingDown) {
					AP.way.prev();
					AP.way.curr().getP(Pc);
					AP.way.next();
					tmpV3f.sub(Po, Pc);
					tmpV3f.normalize();
					if (tmpV3f.dot(Ve) < 0.0D && f30 > 1000F && !TaxiMode) {
						AP.way.first();
						push(10);
						pop();
						CT.GearControl = 0.0F;
					}
					float f33 = (float) tmpV3f.dot(Ve);
					tmpV3f.scale(-f33);
					tmpV3f.add(Po, tmpV3f);
					tmpV3f.sub(Loc);
					float f37 = 0.0005F * (3000F - f30);
					if (f37 > 1.0F) f37 = 1.0F;
					if (f37 < 0.1F) f37 = 0.1F;
					float f41 = (float) tmpV3f.length();
					if (f41 > 40F * f37) {
						f41 = 40F * f37;
						tmpV3f.normalize();
						tmpV3f.scale(f41);
					}
					float f44 = VminFLAPS;
					if (AP.way.Cur() >= 6) {
						if (AP.way.isLandingOnShip()) {
							if (Actor.isAlive(AP.way.landingAirport) && (AP.way.landingAirport instanceof AirportCarrier)) {
								float f47 = (float) ((AirportCarrier) AP.way.landingAirport).speedLen();
								if (VminFLAPS < f47 + 10F) f44 = f47 + 10F;
							}
						} else {
							f44 = VminFLAPS * 1.2F;
						}
						if (f44 < 14F) f44 = 14F;
					} else {
						f44 = VminFLAPS * 2.0F;
					}
					double d3 = Vwld.length();
					double d5 = (double) f44 - d3;
					float f50 = 2.0F * f;
					if (d5 > (double) f50) d5 = f50;
					if (d5 < (double) (-f50)) d5 = -f50;
					Ve.normalize();
					Ve.scale(d3);
					Ve.add(tmpV3f);
					Ve.sub(Vwld);
					float f51 = (50F * f37 - f41) * f;
					if (Ve.length() > (double) f51) {
						Ve.normalize();
						Ve.scale(f51);
					}
					Vwld.add(Ve);
					Vwld.normalize();
					Vwld.scale(d3 + d5);
					Loc.x += Vwld.x * (double) f;
					Loc.y += Vwld.y * (double) f;
					Loc.z += Vwld.z * (double) f;
					Or.transformInv(tmpV3f);
					tmpOr.setAT0(Vwld);
					float f52 = 0.0F;
					if (AP.way.isLandingOnShip()) f52 = 0.9F * (45F - Alt);
					else f52 = 0.7F * (20F - Alt);
					if (f52 < 0.0F) f52 = 0.0F;
					float gp = Gears.Pitch;
					if (gp > 180F) gp -= 360F;
					if (Or.getTangage() > gp + 4.0F) f52 = of52 * 0.5F;
					else of52 = f52;
					tmpOr.increment(0.0F, 4F + f52, (float) (tmpV3f.y * 0.5D));
					Or.interpolate(tmpOr, 0.5F * f);
					callSuperUpdate = false;
					W.set(0.0D, 0.0D, 0.0D);
					float newelev = 0.05F + 0.3F * f52;
					gp = Gears.Pitch;
					if (gp > 180F) gp -= 360F;
					if (gp > 7.0F) gp -= 2.0F;
					else gp += 3.0F;
					if (Or.getTangage() < -1.0F) {
						if (newelev > 1.0F) newelev = 1.0F;
						if (newelev < -1.0F) newelev = -1.0F;
						newelev = newelev * 0.4F - Or.getTangage() * 0.3F;
					}
					if (Or.getTangage() - gp > 1.0F) {
						if (newelev > 1.0F) newelev = 1.0F;
						if (newelev < -0.2F) newelev = -0.2F;
						newelev = newelev * 0.9F - (Or.getTangage() - gp - 1F) * (Or.getTangage() - gp - 1F) * 0.05F;
					}
					if (newelev > 1.0F) newelev = 1.0F;
					if (newelev < -0.2F) newelev = -0.2F;
					CT.ElevatorControl = newelev;
					CT.RudderControl = (float) (-tmpV3f.y * 0.02D);
					direction = Or.getAzimut();
				} else {
					AP.setStabDirection(true);
				}
			} else {
				AP.setStabDirection(true);
			}
			dA = CT.ElevatorControl;
			AP.update(f);
			setSpeedControl(f);
			CT.ElevatorControl = dA;
			if (maneuver != 25) return;
			if (Alt > 60F) {
				if (Alt < 160F) {
					if (!(super.actor instanceof TypeFastJet)) CT.FlapsControl = 0.32F;
					// TODO: Blown Flaps
					if (CT.bHasBlownFlaps) CT.BlownFlapsControl = 1.0F;
				} else if (!(super.actor instanceof TypeFastJet)) CT.FlapsControl = 0.0F;
				setSpeedMode(7);
				smConstPower = 0.2F;
				if (super.actor instanceof TypeFastJet) dA = Math.min(210F + Alt, 350F);
				else dA = Math.min(130F + Alt, 270F);
				if (Vwld.z > 0.0D || getSpeedKMH() < dA) dA = -1.2F * f;
				else dA = 1.2F * f;
				CT.ElevatorControl = CT.ElevatorControl * 0.9F + dA * 0.1F;
			} else {
				minElevCoeff = 15F;
				if (AP.way.Cur() >= 6 || AP.way.Cur() == 0) {
					if (Or.getTangage() < -5F) Or.increment(0.0F, -Or.getTangage() - 5F, 0.0F);
					if (Or.getTangage() > Gears.Pitch + 10F) Or.increment(0.0F, -(Or.getTangage() - (Gears.Pitch + 10F)), 0.0F);
				}
				// TODO: Blown Flaps
				if (CT.bHasBlownFlaps) CT.BlownFlapsControl = 1.0F;
				CT.FlapsControl = 1.0F;
				if (Vrel.length() < 1.0D) {
					// TODO: Blown Flaps
					CT.FlapsControl = CT.BlownFlapsControl = CT.BrakeControl = 0.0F;
					if (!TaxiMode) {
						setSpeedMode(8);
						if (AP.way.isLandingOnShip()) {
							if (CT.getFlap() < 0.001F) AS.setWingFold(actor, 1);
							if (CT.bHasCockpitDoorControl && CT.bNoCarrierCanopyOpen) AS.setCockpitDoor(actor, 1);
							CT.BrakeControl = 1.0F;
							if (CT.arrestorControl == 1.0F && Gears.onGround()) AS.setArrestor(actor, 0);
							MsgDestroy.Post(Time.current() + 25000L, actor);
						} else {
							EI.setEngineStops();
							if (EI.engines[0].getPropw() < 1.0F) {
								World.cur();
								if (actor != World.getPlayerAircraft()) MsgDestroy.Post(Time.current() + 12000L, actor);
							}
						}
					}
				}
				if (getSpeed() < VmaxFLAPS * 0.21F) CT.FlapsControl = 0.0F;
				if (Vrel.length() < (double) ((super.actor instanceof TypeFastJet) ? (Math.min(35F, VminFLAPS * 0.50F)) : (VmaxFLAPS * 0.25F)) && wayCurPos == null && !AP.way.isLandingOnShip() && isEnableToTaxi()) {
					TaxiMode = true;
					AP.way.setCur(0);
					return;
				}
				if (getSpeed() > VminFLAPS * 0.6F && Alt < 10F) {
					setSpeedMode(8);
					if (AP.way.isLandingOnShip() && CT.bHasArrestorControl) {
						if (Vwld.z < -5.5D) Vwld.z *= 0.94999998807907104D;
						if (Vwld.z > 0.0D) Vwld.z *= 0.9100000262260437D;
					} else {
						if (Vwld.z < -0.60000002384185791D) Vwld.z *= 0.93999999761581421D;
						if (Vwld.z < -2.5D) Vwld.z = -2.5D;
						if (Vwld.z > -0.6D) Vwld.z -= 0.1D;
						if (Vwld.z > -1.2D) Vwld.z -= 0.08D;
						if (Vwld.z > 0.0D) Vwld.z *= 0.9100000262260437D;
					}
				}
				float f27 = Gears.Pitch;
				float gp = Gears.Pitch;
				if (f27 > 180F) f27 -= 360F;
				if (gp > 180F) gp -= 360F;
				if (f27 > 7.0F) f27 -= 2.0F;
				else f27 += 3.0F;
				if (f27 < 3F) f27 = 3F;
				if ((Alt < 7F && Or.getTangage() < f27 || AP.way.isLandingOnShip() && Or.getTangage() < f27) && !Gears.onGround()) {
					CT.ElevatorControl += (f27 - Or.getTangage()) * 0.333F * f;
					if (CT.ElevatorControl > 1.0F) CT.ElevatorControl = 1.0F;
					if (CT.ElevatorControl < -0.2F && (super.actor instanceof TypeFastJet)) CT.ElevatorControl = -0.2F;
					else if (CT.ElevatorControl < 0.04F) CT.ElevatorControl = 0.04F;
				}
				if (((Alt < 7F || AP.way.isLandingOnShip()) && Or.getTangage() > f27 + 4F && !Gears.onGround() && gp < 5.0F)) {
					CT.ElevatorControl += (f27 + 2.0F - Or.getTangage()) * 0.02F * f;
				}
				// CT.ElevatorControl += 0.05000000074505806D * getW().y;
				if (Gears.onGround()) {
					f27 = Gears.Pitch;
					if (f27 > 180F) f27 -= 360F;
					if (Gears.nOfGearsOnGr < 3) if (f27 > 5F) {
						if (Or.getTangage() < f27) CT.ElevatorControl += 1.5F * f;
						if (!AP.way.isLandingOnShip()) CT.ElevatorControl += 0.30000001192092896D * getW().y;
						if (CT.ElevatorControl > 1.0F) CT.ElevatorControl = 1.0F;
						if (CT.ElevatorControl < -1.0F) CT.ElevatorControl = -1.0F;
					} else {
						CT.ElevatorControl -= 0.005F;
						if (CT.ElevatorControl > 1.0F) CT.ElevatorControl = 1.0F;
						if (CT.ElevatorControl < 0.08F) CT.ElevatorControl = 0.08F;
					}
					if (Gears.nOfGearsOnGr == 3 && !AP.way.isLandingOnShip()) {
						if (brakingtimer == 0L) brakingtimer = Time.current();
						CT.ElevatorControl = 0.0F;
						if (getSpeedKMH() > 85F && Time.current() > brakingtimer + 5000L && (super.actor instanceof TypeFastJet)) CT.BrakeControl = 0.2F;
						else CT.BrakeControl = 0.0F;
					}
					if (!TaxiMode) {
						AFo.setDeg(Or.getAzimut(), direction);
						CT.RudderControl = 8F * AFo.getDiffRad() + 0.5F * (float) getW().z;
					} else {
						CT.RudderControl = 0.0F;
					}
				}
			}
			AP.way.curr().getP(Po);
			return;

		case 66: // 'B'
			return;

		case 49: // '1'
			emergencyLanding(f);
			break;

		case 64: // '@'
			if (actor instanceof TypeGlider) {
				pop();
				break;
			}
			if (actor instanceof HE_LERCHE3) {
				boolean flag3 = Actor.isAlive(AP.way.takeoffAirport) && (AP.way.takeoffAirport instanceof AirportCarrier);
				if (!flag3) callSuperUpdate = false;
			}
			FlightModel flightmodel = Group.airc[0].FM;
			if ((AP.way.first().waypointType == 4 || AP.way.first().waypointType == 5) && flightmodel.isCapableOfTaxiing() && (flightmodel instanceof RealFlightModel) && ((RealFlightModel) flightmodel).isRealMode() && !bGentsStartYourEngines) break;
			CT.BrakeControl = 1.0F;
			CT.ElevatorControl = 0.5F;
			CT.setPowerControl(0.0F);
			EI.setCurControlAll(false);
			setSpeedMode(0);
			if (World.Rnd().nextFloat() < 0.05F) {
				EI.setCurControl(submaneuver, true);
				if (EI.engines[submaneuver].getStage() == 0) {
					setSpeedMode(0);
					CT.setPowerControl(0.05F);
					EI.engines[submaneuver].setControlThrottle(0.2F);
					EI.toggle();
					if ((actor instanceof BI_1) || (actor instanceof BI_6)) {
						pop();
						break;
					}
				}
			}
			if (EI.engines[submaneuver].getStage() == 6) {
				setSpeedMode(0);
				EI.engines[submaneuver].setControlThrottle(0.0F);
				submaneuver++;
				CT.setPowerControl(0.0F);
				if (submaneuver > EI.getNum() - 1) {
					EI.setCurControlAll(true);
					pop();
				}
			}
			break;

		case 102: // 'f'
			if (Group != null && actor != null) {
				int j1 = 5 + Group.numInGroup((Aircraft) actor) * 8;
				if (mn_time >= (float) j1) {
					P.x = Loc.x;
					P.y = Loc.y;
					P.z = Loc.z;
					Vcur.x = (float) Vwld.x;
					Vcur.y = (float) Vwld.y;
					if (curAirdromePoi == 0 && ((Aircraft) actor).aircIndex() > 0 && Leader != null) {
						Ve.sub(Leader.Loc, Loc);
						double d1 = Ve.length();
						Ve.normalize();
						Or.transformInv(Ve);
						if (Ve.x < 0.69999998807907104D && d1 < 80D) return;
						if (Leader.Vwld.lengthSquared() < 25D) return;
					}
					if (wayCurPos == null) {
						if (CT.bHasCockpitDoorControl) AS.setCockpitDoor(actor, 1);
						int k1 = 0;
						WayPoint waypoint = AP.way.look_at_point(k1);
						if (waypoint.waypointType == 4) k1 = 1;
						Or.transform(Vwld);
						Vcur.x = (float) Vwld.x;
						Vcur.y = (float) Vwld.y;
						taxiToTakeOffWay = new ArrayList();
						Vpl.set(actor.collisionR() * 3F, 0.0D, 0.0D);
						Or.transform(Vpl);
						P.add(Vpl);
						taxiToTakeOffWay.add(new Point_Any(P.x, P.y));
						WayPoint waypoint1 = null;
						do {
							WayPoint waypoint2 = AP.way.look_at_point(k1);
							if (waypoint1 == waypoint2 || waypoint2.waypointType != 4 && waypoint2.waypointType != 5) break;
							waypoint1 = waypoint2;
							float f42 = 0.0F;
							float f45 = 0.0F;
							WayPoint waypoint4 = AP.way.look_at_point(k1 + 1);
							if (waypoint4.waypointType == 4 || waypoint4.waypointType == 5) {
								f42 = World.Rnd().nextFloat(-1F, 1.0F);
								f45 = World.Rnd().nextFloat(-1F, 1.0F);
							}
							taxiToTakeOffWay.add(new Point_Any(waypoint2.x() + f42, waypoint2.y() + f45));
							k1++;
						} while (true);
						taxiToTakeOffWayLength = taxiToTakeOffWay.size();
						wayPrevPos = wayCurPos = (Point_Any) taxiToTakeOffWay.get(0);
						P.x = Loc.x;
						P.y = Loc.y;
						P.z = Loc.z;
						CT.setPowerControl(0.0F);
						EI.setCurControlAll(true);
						CT.BrakeControl = 0.0F;
						setSpeedMode(8);
					}
					if (wayCurPos != null) {
						Point_Any point_any = wayCurPos;
						Pcur.set((float) P.x, (float) P.y);
						distToTaxiPoint = Pcur.distance(point_any);
						V_to.sub(point_any, Pcur);
						V_to.normalize();
						float f32 = 3F + 0.1F * (distToTaxiPoint - actor.collisionR() * 2.0F);
						if (f32 > 7F && (Leader == null || !Leader.isCapableOfTaxiing())) f32 = 7F;
						else if (Leader != null && f32 > 9F && Leader.isCapableOfTaxiing()) {
							f32 = 9F;
							Ve.sub(Leader.Loc, Loc);
							float f34 = (float) Ve.length();
							if (f34 < 80F) f32 -= -0.1F * f34 + 8F;
						}
						if (f32 < 2.0F) f32 = 2.0F;
						if (distToTaxiPoint < 5F + actor.collisionR() * 0.75F + Gears.mgx * 2.0F) {
							f32 = 0.01F;
							ignoredActor = null;
							Point_Any point_any1 = getNextTaxiToTakeOffPoint();
							if (point_any1 == null) {
								if (taxiToTakeOffWayLength == AP.way.size()) {
									maneuver = 0;
									clear_stack();
									set_maneuver(25);
									TaxiMode = true;
									wayPrevPos = wayCurPos = new Point_Null((float) Loc.x, (float) Loc.y);
									airdromeWay = new Point_Any[1];
									airdromeWay[0] = wayCurPos;
									curAirdromePoi = 1;
									return;
								}
								Point3d point3d1 = null;
								WayPoint waypoint3 = AP.way.look_at_point(0);
								if (waypoint3.waypointType == 4) point3d1 = AP.way.get(taxiToTakeOffWayLength).getP();
								else point3d1 = AP.way.get(taxiToTakeOffWayLength - 1).getP();
								point_any.set((float) point3d1.x, (float) point3d1.y);
								V_to.sub(point_any, Pcur);
								direction = 360F - RAD2DEG(V_to.direction());
								CT.setPowerControl(0.0F);
								EI.setCurControlAll(true);
								CT.BrakeControl = 1.0F;
								setSpeedMode(8);
								if (CT.bHasCockpitDoorControl) AS.setCockpitDoor(actor, 0);
								pop();
								cleanCollisionMap(actor);
								Wo = 0.0F;
								W.set(0.0D, 0.0D, 0.0D);
								return;
							}
							wayPrevPos = wayCurPos;
							wayCurPos = point_any1;
							V_taxiLeg.set(wayCurPos);
							V_taxiLeg.sub(wayPrevPos);
							if (V_taxiLeg.lengthSquared() > 0.1F) {
								V_taxiLeg.normalize();
								V_taxiLeg.scale(0.5F);
							}
						} else {
							boolean flag6 = isTaxingCollisionDanger();
							if (flag6) f32 = 0.01F;
						}
						V_to.sub(V_taxiLeg);
						float f38 = RAD2DEG((float) Math.atan2(V_to.y, V_to.x));
						for (f38 += Or.azimut(); f38 > 180F; f38 -= 360F)
							;
						for (; f38 < -180F; f38 += 360F)
							;
						Wo += (2.0F * f38 * f) / actor.collisionR();
						if ((double) f32 < 0.10000000000000001D) Wo *= 0.95F;
						f38 = (f38 > 20F ? 20F : f38) < -20F ? -20F : f38;
						CT.RudderControl = -f38 * 0.05F;
						if (f38 > 0.0F) {
							if (Wo > f38) {
								CT.RudderControl = f38 * 0.05F;
								Wo = f38;
							}
						} else if (Wo < f38) {
							CT.RudderControl = f38 * 0.05F;
							Wo = f38;
						}
						W.z = Wo * 0.01F;
						Or.setRoll(0.0F);
						if (f32 > 2.0F && (f38 > 15F || f38 < -15F)) f32 = 2.0F;
						Or.transformInv(Vwld);
						if (Vwld.x < Math.abs(W.z * (double) Gears.mgy)) Vwld.x = Math.abs(W.z * (double) Gears.mgy);
						Vwld.y = (double) Gears.mgh * Math.cos((double) Gears.mgalpha + W.z) - (double) Gears.mgy;
						Vwld.x += 2.0F * f * (float) ((double) f32 > Vwld.x ? 1 : -1);
						Vwld.y += W.z * 0.5D * Vwld.x;
						if (Vwld.x > 0.019999999552965164D) Gears.setSteeringAngle((float) Math.toDegrees(Math.atan2(W.z * (double) (Gears.sgx + Gears.mgx), Vwld.x)));
						Or.transform(Vwld);
						P.x += Vwld.x * (double) f;
						P.y += Vwld.y * (double) f;
						Loc.set(P);
					}
				}
			}
			break;

		case 26: // '\032'
			Po.set(Loc);
			if ((long) AP.way.first().delayTimer * 60000L > Time.current()) return;
			int l1 = ((Aircraft) actor).aircIndex();
			if (l1 == -1) l1 = 0;
			float f35 = Alt;
			float f39 = 0.4F;
			if (Actor.isAlive(AP.way.takeoffAirport) && (AP.way.takeoffAirport instanceof AirportCarrier)) {
				AirportCarrier airportcarrier = (AirportCarrier) AP.way.takeoffAirport;
				if (!(airportcarrier.ship() instanceof TestRunway)) {
					f35 -= ((AirportCarrier) AP.way.takeoffAirport).height();
					if (Alt < 9F && Vwld.z < 0.0D) Vwld.z *= 0.84999999999999998D;
					f39 = 0.97F;
					if (CT.bHasCockpitDoorControl && !bStage6) AS.setCockpitDoor(actor, 1);
				}
			}
			if (first) {
				setCheckGround(false);
				CT.BrakeControl = 1.0F;
				CT.GearControl = 1.0F;
				CT.setPowerControl(0.0F);
				if (CT.bHasArrestorControl) AS.setArrestor(actor, 0);
				setSpeedMode(8);
				if (f35 > 7.21F && AP.way.Cur() == 0) {
					EI.setEngineRunning();
					Aircraft.debugprintln(actor, "in the air - engines running!.");
				}
				if (!Actor.isAlive(AP.way.takeoffAirport)) direction = actor.pos.getAbsOrient().getAzimut();
				if (AP.way.first().waypointType == 2 || AP.way.first().waypointType == 3 || ((Aircraft) actor).stationarySpawnLocSet && AP.way.first().waypointType != 5 && AP.way.first().waypointType != 4)
					direction = actor.pos.getAbsOrient().getAzimut();
				if (actor instanceof HE_LERCHE3) {
					maneuver = 69;
					break;
				}
			}
			if (Gears.onGround()) {
				if (EI.engines[0].getStage() == 0) {
					CT.setPowerControl(0.0F);
					setSpeedMode(8);
					float f31 = World.Rnd().nextFloat(7F + (float) l1 * 0.25F, 10F);
					if (World.Rnd().nextFloat() < 0.05F && mn_time > 1.0F || mn_time > f31) {
						push();
						if (AP.way.first().waypointType == 4 || AP.way.first().waypointType == 5) push(102);
						push(64);
						submaneuver = 0;
						maneuver = 0;
						safe_pop();
						break;
					}
				} else {
					Po.set(Loc);
					// TODO: +++ CTO Mod 4.12 +++
					if (!bAlreadyCheckedStage7) {
						if (this.AP.way.takeoffAirport instanceof AirportCarrier) {
							AirportCarrier airportcarrier = (AirportCarrier) this.AP.way.takeoffAirport;
							BigshipGeneric bigshipgeneric = airportcarrier.ship();
							// TODO: Edited to make use of new Catapults.ini
							super.Gears.setCatapultOffset(bigshipgeneric, new SectFile("com/maddox/il2/objects/Catapults.ini"));
							bCatapultAI = this.Gears.getCatapultAI();
						} else {
							bStage7 = true;
						}
						bAlreadyCheckedStage7 = true;
					}
					if (!bCatapultAI) {
						Po.set(this.Loc);
						Vpl.set(60D, 0.0D, 0.0D);
						fNearestDistance = 70F;
						this.Or.transform(Vpl);
						Po.add(Vpl);
						Pd.set(Po);
					} else {
						Po.set(this.Loc);
						Vpl.set(200D, 0.0D, 0.0D);
						fNearestDistance = 210F;
						this.Or.transform(Vpl);
						Po.add(Vpl);
						Pd.set(Po);
					}
					if (canTakeoff) {
						if (!bStage7) {
							if (bStage6) {
								if (bFastLaunchAI || !CT.bHasWingControl || CT.bHasWingControl && CT.getWing() < 0.5F) bStage7 = true;
							} else if (bStage4) {
								if (CT.bHasWingControl && CT.getWing() > 0.001F) {
									if (bFastLaunchAI) CT.forceWing(0.0F);
									AS.setWingFold(actor, 0);
								}
								if (CT.bHasCockpitDoorControl && CT.bNoCarrierCanopyOpen) {
									if (bFastLaunchAI) CT.forceCockpitDoor(0.0F);
									AS.setCockpitDoor(actor, 0);
								}
								bStage6 = true;
							} else if (bStage3) {
								Loc loc = new Loc();
								BigshipGeneric bigshipgeneric1 = (BigshipGeneric) brakeShoeLastCarrier;
								Aircraft aircraft = (Aircraft) actor;
								CellAirField cellairfield = bigshipgeneric1.getCellTO();
								double d3;
								double d6;
								if (Gears.getCatapultAlreadySetNum() == -1) {
									if (bCatapultAI) {
										d3 = -((Tuple3d) (cellairfield.leftUpperCorner())).x - Gears.getCatapultOffsetX();
										d6 = ((Tuple3d) (cellairfield.leftUpperCorner())).y - Gears.getCatapultOffsetY();
									} else {
										d3 = -((Tuple3d) (cellairfield.leftUpperCorner())).x - (double) (cellairfield.getWidth() / 2 - 3);
										d6 = brakeShoeLoc.getX() + (double) aircraft.getCellAirPlane().getHeight() + 4D;
									}
									loc.set(d6, d3, brakeShoeLoc.getZ(), brakeShoeLoc.getAzimut(), brakeShoeLoc.getTangage(), brakeShoeLoc.getKren());
									loc.add(brakeShoeLastCarrier.pos.getAbs());
									actor.pos.setAbs(loc.getPoint());
									brakeShoeLoc.set(actor.pos.getAbs());
									brakeShoeLoc.sub(brakeShoeLastCarrier.pos.getAbs());
								}
								bStage4 = true;
							} else {
								CT.setPowerControl(1.0F);
								bStage3 = true;
							}
						} else {
							// TODO: --- CTO Mod 4.12 ---
							CT.setPowerControl(1.1F);
							setSpeedMode(11);
						}
					} else {
						setSpeedMode(8);
						CT.BrakeControl = 1.0F;
						boolean flag7 = true;
						boolean flag9 = true;
						if (AP.way.first().waypointType == 2 || AP.way.first().waypointType == 3 || AP.way.first().waypointType == 4 || AP.way.first().waypointType == 5 || ((Aircraft) actor).stationarySpawnLocSet) flag9 = false;
						if (mn_time < 8F && flag9) {
							flag7 = false;
						}
						// TODO: +++ CTO Mod 4.12 +++
						// if (actor != War.getNearestFriendAtPoint(Pd, (Aircraft) actor, 70F))
						if (actor != War.getNearestFriendAtPoint(Pd, (Aircraft) actor, fNearestDistance)) {
							// TODO: --- CTO Mod 4.12 ---
							if (actor instanceof G4M2E) {
								// TODO: +++ CTO Mod 4.12 +++
								// if (War.getNearestFriendAtPoint(Pd, (Aircraft) actor, 70F) != ((G4M2E) actor).typeDockableGetDrone()) {
								if (War.getNearestFriendAtPoint(Pd, (Aircraft) actor, fNearestDistance) != ((G4M2E) actor).typeDockableGetDrone()) {
									// TODO: --- CTO Mod 4.12 ---
									flag7 = false;
								}
							} else {
								flag7 = false;
							}
						}
						if (Actor.isAlive(AP.way.takeoffAirport) && flag9 && AP.way.takeoffAirport.takeoffRequest > 0) {
							flag7 = false;
						}
						if (flag7) {
							canTakeoff = true;
							if (Actor.isAlive(AP.way.takeoffAirport)) AP.way.takeoffAirport.takeoffRequest = 300;
						}
					}
				}
				// TODO: Added takeoffairport condition
				if (EI.engines[0].getStage() == 6 && CT.bHasWingControl && CT.getWing() > 0.001F && !(super.AP.way.takeoffAirport instanceof AirportCarrier)) AS.setWingFold(actor, 0);
			} else if (canTakeoff) {
				CT.setPowerControl(1.1F);
				setSpeedMode(11);
			}
			if (CT.FlapsControl == 0.0F && CT.getWing() < 0.001F) CT.FlapsControl = 0.4F;
			if (EI.engines[0].getStage() == 6 && CT.getPower() > f39) {
				CT.BrakeControl = 0.0F;
				brakeShoe = false;
				float f43 = (float) ((double) ((Vmin + VminFLAPS) * 0.5F) * Math.sqrt(M.getFullMass() / M.referenceWeight));
				float f46 = (AOA_Crit - 2.0F) * (getSpeed() / f43);
				if (Gears.bIsSail) f46 *= 2.0F;
				if (Gears.bFrontWheel) f46 = Gears.Pitch + 4F;
				if (f46 < 1.0F) f46 = 1.0F;
				if (f46 > AOA_Crit - 2.0F) f46 = AOA_Crit - 2.0F;
				float f48 = 1.5F;
				if (Actor.isAlive(AP.way.takeoffAirport) && (AP.way.takeoffAirport instanceof AirportCarrier)) {
					AirportCarrier airportcarrier1 = (AirportCarrier) AP.way.takeoffAirport;
					if (!(airportcarrier1.ship() instanceof TestRunway) && !Gears.isUnderDeck()) {
						CT.GearControl = 0.0F;
						if (f35 < 0.0F) {
							f46 = 18F;
							f48 = 0.05F;
						} else {
							f46 = 14F;
							f48 = 0.3F;
						}
					}
				}
				if (Or.getTangage() < f46) dA = -0.7F * (Or.getTangage() - f46) + f48 * (float) getW().y + 0.5F * (float) getAW().y;
				else dA = -0.1F * (Or.getTangage() - f46) + f48 * (float) getW().y + 0.5F * (float) getAW().y;
				CT.ElevatorControl += (dA - CT.ElevatorControl) * 3F * f;
			} else {
				CT.ElevatorControl = 1.0F;
			}
			boolean flag8 = true;
			if (AP.way.first().waypointType == 2 || AP.way.first().waypointType == 3 || AP.way.first().waypointType == 5 || AP.way.first().waypointType == 4 || ((Aircraft) actor).stationarySpawnLocSet) flag8 = false;
			AFo.setDeg(Or.getAzimut(), direction);
			double d2 = AFo.getDiffRad();
			if (EI.engines[0].getStage() == 6) {
				CT.RudderControl = 8F * (float) d2;
				if (d2 > -1D && d2 < 1.0D) {
					if (flag8 && Actor.isAlive(AP.way.takeoffAirport) && CT.getPower() > 0.3F) {
						double d4 = AP.way.takeoffAirport.shiftFromLine(this);
						double d6 = 3D - 3D * Math.abs(d2);
						if (d6 > 1.0D) d6 = 1.0D;
						double d7 = 0.25D * d4 * d6;
						if (d7 > 1.5D) d7 = 1.5D;
						if (d7 < -1.5D) d7 = -1.5D;
						CT.RudderControl += (float) d7;
					}
				} else {
					CT.BrakeControl = 1.0F;
				}
			}
			CT.AileronControl = -0.05F * Or.getKren() + 0.3F * (float) getW().y;
			if (f35 > 5F && !Gears.isUnderDeck()) CT.GearControl = 0.0F;
			float f49 = 1.0F;
			if (hasBombs() || !flag8) f49 *= 1.7F;
			if (f35 > 120F * f49 || getSpeed() > Vmin * 1.8F * f49 || f35 > 80F * f49 && getSpeed() > Vmin * 1.6F * f49 || f35 > 40F * f49 && getSpeed() > Vmin * 1.3F * f49 && mn_time > 60F + (float) ((Aircraft) actor).aircIndex() * 3F) {
				CT.FlapsControl = 0.0F;
				CT.GearControl = 0.0F;
				rwLoc = null;
				if (actor instanceof TypeGlider) push(24);
				maneuver = 0;
				brakeShoe = false;
				turnOffCollisions = false;
				if (CT.bHasCockpitDoorControl) AS.setCockpitDoor(actor, 0);
				pop();
				if (Group.numInGroup((Aircraft) actor) == 0 && Group.grTask == 7) Group.setGroupTask(1);
				if (AP.way.first().waypointType == 4 || AP.way.prev().waypointType == 5) {
					AP.way.setCur(curAirdromePoi);
					curAirdromePoi = 0;
					wayCurPos = null;
				}
			}
			if (actor instanceof TypeGlider) {
				CT.BrakeControl = 0.0F;
				CT.ElevatorControl = 0.05F;
				canTakeoff = true;
			}
			break;

		case 69: // 'E'
			float f36 = Alt;
			float f40 = 0.4F;
			CT.BrakeControl = 1.0F;
			W.scale(0.0D);
			boolean flag10 = Actor.isAlive(AP.way.takeoffAirport) && (AP.way.takeoffAirport instanceof AirportCarrier);
			if (flag10) {
				f36 -= ((AirportCarrier) AP.way.takeoffAirport).height();
				f40 = 0.8F;
				if (Alt < 9F && Vwld.z < 0.0D) Vwld.z *= 0.84999999999999998D;
				if (CT.bHasCockpitDoorControl) AS.setCockpitDoor(actor, 1);
			}
			if (Loc.z != 0.0D && ((HE_LERCHE3) actor).suka.getPoint().z == 0.0D) ((HE_LERCHE3) actor).suka.set(Loc, Or);
			if (Loc.z != 0.0D) if (EI.getPowerOutput() < f40 && !flag10) {
				callSuperUpdate = false;
				Loc.set(((HE_LERCHE3) actor).suka.getPoint());
				Or.set(((HE_LERCHE3) actor).suka.getOrient());
			} else if (f36 < 100F) Or.set(((HE_LERCHE3) actor).suka.getOrient());
			if (Gears.onGround() && EI.engines[0].getStage() == 0) {
				CT.setPowerControl(0.0F);
				setSpeedMode(8);
				if (World.Rnd().nextFloat() < 0.05F && mn_time > 1.0F || mn_time > 8F) {
					push();
					push(64);
					submaneuver = 0;
					maneuver = 0;
					safe_pop();
					break;
				}
			}
			if (EI.engines[0].getStage() == 6) {
				CT.BrakeControl = 0.0F;
				CT.AirBrakeControl = 1.0F;
				brakeShoe = false;
				CT.setPowerControl(1.1F);
				setSpeedMode(11);
			}
			if (f36 > 200F) {
				CT.GearControl = 0.0F;
				rwLoc = null;
				CT.ElevatorControl = -1F;
				CT.AirBrakeControl = 0.0F;
				if (Or.getTangage() < 25F) maneuver = 0;
				brakeShoe = false;
				turnOffCollisions = false;
				if (CT.bHasCockpitDoorControl) AS.setCockpitDoor(actor, 0);
				pop();
			}
			break;

		case 28: // '\034'
			if (first) {
				direction = World.Rnd().nextFloat(-25F, 25F);
				AP.setStabAll(false);
				setSpeedMode(6);
				CT.RudderControl = World.Rnd().nextFloat(-0.22F, 0.22F);
				submaneuver = 0;
				if (getSpeed() < Vmin * 1.5F) pop();
			}
			CT.AileronControl = -0.04F * (Or.getKren() - direction);
			switch (submaneuver) {
			case 0: // '\0'
				dA = 1.0F;
				maxAOA = 12F + 1.0F * (float) Skill;
				if (AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.6F * f;
				else CT.ElevatorControl += 3.3F * f;
				if (Or.getTangage() > 40F + 5.1F * (float) Skill) submaneuver++;
				break;

			case 1: // '\001'
				direction = World.Rnd().nextFloat(-25F, 25F);
				CT.RudderControl = World.Rnd().nextFloat(-0.75F, 0.75F);
				submaneuver++;
				// fall through

			case 2: // '\002'
				dA = -1F;
				maxAOA = 12F + 5F * (float) Skill;
				if (AOA < -maxAOA || CT.ElevatorControl < dA) CT.ElevatorControl += 0.6F * f;
				else CT.ElevatorControl -= 3.3F * f;
				if (Or.getTangage() < -45F) pop();
				break;
			}
			if (mn_time > 3F) pop();
			break;

		case 29: // '\035'
			minElevCoeff = 17F;
			if (first) {
				AP.setStabAll(false);
				setSpeedMode(9);
				sub_Man_Count = 0;
			}
			if (danger != null) {
				if (sub_Man_Count == 0) {
					tmpV3d.set(danger.getW());
					danger.Or.transform(tmpV3d);
					if (tmpV3d.z > 0.0D) sinKren = -World.Rnd().nextFloat(60F, 90F);
					else sinKren = World.Rnd().nextFloat(60F, 90F);
				}
				float f23 = (Energy - danger.Energy) * 0.1019F;
				if (Loc.distanceSquared(danger.Loc) < 5000D || f23 < -200F) {
					setSpeedMode(8);
					CT.setPowerControl(0.0F);
				} else {
					setSpeedMode(9);
				}
			} else {
				pop();
			}
			CT.FlapsControl = 0.2F;
			// TODO: Blown Flaps
			if ((double) getSpeed() < 120D) {
				CT.FlapsControl = 0.33F;
				if (CT.bHasBlownFlaps) CT.BlownFlapsControl = 1.0F;
			}
			if ((double) getSpeed() < 80D) CT.FlapsControl = 1.0F;
			CT.AileronControl = -0.08F * (Or.getKren() + sinKren);
			CT.ElevatorControl = 0.9F;
			CT.RudderControl = 0.0F;
			nShakeMe(flying, Skill);
			sub_Man_Count++;
			if (sub_Man_Count > 15) sub_Man_Count = 0;
			if (mn_time > 10F) pop();
			break;

		case 56: // '8'
			if (first) {
				submaneuver = World.Rnd().nextInt(0, 1);
				direction = World.Rnd().nextFloat(-20F, -10F);
			}
			CT.AileronControl = -0.08F * (Or.getKren() - direction);
			switch (submaneuver) {
			case 0: // '\0'
				dA = 1.0F;
				maxAOA = 10F + 2.0F * (float) Skill;
				if ((double) getOverload() > 1.0D / Math.abs(Math.cos(DEG2RAD(Or.getKren()))) || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.2F * f;
				else CT.ElevatorControl += 0.4F * f;
				CT.RudderControl = -1F * (float) Math.sin(DEG2RAD(Or.getKren() + 55F));
				if (mn_time > 1.5F) submaneuver++;
				break;

			case 1: // '\001'
				direction = World.Rnd().nextFloat(10F, 20F);
				submaneuver++;
				// fall through

			case 2: // '\002'
				dA = 1.0F;
				maxAOA = 10F + 2.0F * (float) Skill;
				if ((double) getOverload() > 1.0D / Math.abs(Math.cos(DEG2RAD(Or.getKren()))) || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.2F * f;
				else CT.ElevatorControl += 0.4F * f;
				CT.RudderControl = 1.0F * (float) Math.sin(DEG2RAD(Or.getKren() - 55F));
				if (mn_time > 4.5F) submaneuver++;
				break;

			case 3: // '\003'
				direction = World.Rnd().nextFloat(-20F, -10F);
				submaneuver++;
				// fall through

			case 4: // '\004'
				dA = 1.0F;
				maxAOA = 10F + 2.0F * (float) Skill;
				if ((double) getOverload() > 1.0D / Math.abs(Math.cos(DEG2RAD(Or.getKren()))) || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.2F * f;
				else CT.ElevatorControl += 0.4F * f;
				CT.RudderControl = -1F * (float) Math.sin(DEG2RAD(Or.getKren() + 55F));
				if (mn_time > 6F) pop();
				break;
			}
			break;

		case 30: // '\036'
			push(14);
			push(18);
			pop();
			break;

		case 31: // '\037'
			push(14);
			push(19);
			pop();
			break;

		case 32: // ' '
			if (!isCapableOfACM() && World.Rnd().nextInt(-2, 9) < Skill) ((Aircraft) actor).hitDaSilk();
			if (first) {
				AP.setStabAll(false);
				setSpeedMode(6);
				submaneuver = 0;
				direction = 178F - World.Rnd().nextFloat(0.0F, 8F) * (float) Skill;
			}
			maxAOA = Vwld.z > 0.0D ? 14F : 24F;
			if (danger != null) {
				Ve.sub(danger.Loc, Loc);
				dist = (float) Ve.length();
				Or.transformInv(Ve);
				Vpl.set(danger.Vwld);
				Or.transformInv(Vpl);
				Vpl.normalize();
			}
			switch (submaneuver) {
			case 0: // '\0'
				dA = Or.getKren() - 180F;
				if (dA < -180F) dA += 360F;
				dA = -0.08F * dA;
				CT.AileronControl = dA;
				CT.RudderControl = dA > 0.0F ? 1.0F : -1F;
				CT.ElevatorControl = 0.01111111F * Math.abs(Or.getKren());
				if (mn_time > 2.0F || Math.abs(Or.getKren()) > direction) {
					submaneuver++;
					CT.RudderControl = World.Rnd().nextFloat(-0.5F, 0.5F);
					direction = World.Rnd().nextFloat(-60F, -30F);
					mn_time = 0.5F;
				}
				break;

			case 1: // '\001'
				dA = Or.getKren() - 180F;
				if (dA < -180F) dA += 360F;
				dA = -0.04F * dA;
				CT.AileronControl = dA;
				if (Or.getTangage() > direction + 5F && getOverload() < maxG && AOA < maxAOA) {
					if (CT.ElevatorControl < 0.0F) CT.ElevatorControl = 0.0F;
					CT.ElevatorControl += 1.0F * f;
				} else {
					if (CT.ElevatorControl > 0.0F) CT.ElevatorControl = 0.0F;
					CT.ElevatorControl -= 1.0F * f;
				}
				if (mn_time > 2.0F && Or.getTangage() < direction + 20F) submaneuver++;
				if (danger != null) {
					if (Skill >= 2 && Or.getTangage() < -30F && Vpl.x > 0.99862951040267944D) submaneuver++;
					if (Skill >= 3 && Math.abs(Or.getTangage()) > 145F && Math.abs(danger.Or.getTangage()) > 135F && dist < 400F) submaneuver++;
				}
				break;

			case 2: // '\002'
				direction = World.Rnd().nextFloat(-60F, 60F);
				setSpeedMode(6);
				submaneuver++;
				// fall through

			case 3: // '\003'
				dA = Or.getKren() - direction;
				CT.AileronControl = -0.04F * dA;
				CT.RudderControl = dA > 0.0F ? 1.0F : -1F;
				CT.ElevatorControl = 0.5F;
				if (Math.abs(dA) < 4F + 3F * (float) Skill) submaneuver++;
				break;

			case 4: // '\004'
				direction *= World.Rnd().nextFloat(0.5F, 1.0F);
				setSpeedMode(6);
				submaneuver++;
				// fall through

			case 5: // '\005'
				dA = Or.getKren() - direction;
				CT.AileronControl = -0.04F * dA;
				CT.RudderControl = -0.1F * getAOS();
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA || Or.getTangage() > 40F) CT.ElevatorControl -= 0.8F * f;
				else CT.ElevatorControl += 1.6F * f;
				if (Or.getTangage() > 80F || mn_time > 4F || getSpeed() < Vmin * 1.5F) pop();
				if (danger != null && (Ve.z < -1D || dist > 600F || Vpl.x < 0.78801000118255615D)) pop();
				break;
			}
			if ((double) Alt < -7D * Vwld.z) pop();
			break;

		case 33: // '!'
			if (first) {
				if (Alt < 1000F) {
					pop();
					break;
				}
				AP.setStabAll(false);
				submaneuver = 0;
				direction = (World.Rnd().nextBoolean() ? 1.0F : -1F) * World.Rnd().nextFloat(107F, 160F);
			}
			maxAOA = Vwld.z > 0.0D ? 14F : 24F;
			switch (submaneuver) {
			case 0: // '\0'
				if (Math.abs(Or.getKren()) < 45F) CT.ElevatorControl = 0.005555556F * Math.abs(Or.getKren());
				else if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > 1.0F) CT.ElevatorControl -= 0.2F * f;
				else CT.ElevatorControl += 1.2F * f;
				dA = Or.getKren() - direction;
				CT.AileronControl = -0.04F * dA;
				CT.RudderControl = -0.1F * getAOS();
				if (Math.abs(dA) < 4F + 1.0F * (float) Skill) submaneuver++;
				break;

			case 1: // '\001'
				setSpeedMode(7);
				smConstPower = 0.5F;
				CT.AileronControl = 0.0F;
				CT.RudderControl = -0.1F * getAOS();
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.2F * f;
				else CT.ElevatorControl += 1.2F * f;
				if (Or.getTangage() < -60F) submaneuver++;
				break;

			case 2: // '\002'
				if (Or.getTangage() > -45F) {
					CT.AileronControl = -0.04F * Or.getKren();
					setSpeedMode(9);
					maxAOA = 7F;
				}
				CT.RudderControl = -0.1F * getAOS();
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.8F * f;
				else CT.ElevatorControl += 0.4F * f;
				if (Or.getTangage() > AOA - 10F || Vwld.z > -1D) pop();
				break;
			}
			if ((double) Alt < -7D * Vwld.z) pop();
			break;

		case 34: // '"'
			if (first) {
				if (Alt < 500F) {
					pop();
					break;
				}
				direction = Or.getTangage();
				setSpeedMode(9);
			}
			dA = Or.getKren() - (Or.getKren() > 0.0F ? 35F : -35F);
			CT.AileronControl = -0.04F * dA;
			CT.RudderControl = Or.getKren() > 0.0F ? 1.0F : -1F;
			CT.ElevatorControl = -1F;
			if (direction > Or.getTangage() + 45F || Or.getTangage() < -60F || mn_time > 4F) pop();
			break;

		case 36: // '$'
		case 37: // '%'
			if (first) {
				if (!isCapableOfACM()) pop();
				if (getSpeed() < Vmax * 0.5F) {
					pop();
					break;
				}
				AP.setStabAll(false);
				submaneuver = 0;
				direction = World.Rnd().nextFloat(-30F, 80F);
				setSpeedMode(9);
			}
			maxAOA = Vwld.z > 0.0D ? 14F : 24F;
			switch (submaneuver) {
			case 0: // '\0'
				CT.AileronControl = -0.04F * Or.getKren();
				CT.RudderControl = -0.1F * getAOS();
				if (Math.abs(Or.getKren()) < 45F) submaneuver++;
				break;

			case 1: // '\001'
				CT.AileronControl = 0.0F;
				dA = 1.0F;
				if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > dA) CT.ElevatorControl -= 0.4F * f;
				else CT.ElevatorControl += 0.8F * f;
				if (Or.getTangage() > direction) submaneuver++;
				if (getSpeed() < Vmin * 1.25F) pop();
				break;

			case 2: // '\002'
				push(maneuver == 36 ? 7 : 35);
				pop();
				break;
			}
			break;

		case 38: // '&'
			if (first) CT.RudderControl = Or.getKren() > 0.0F ? 1.0F : -1F;
			CT.AileronControl += -0.02F * Or.getKren();
			if (CT.AileronControl > 0.1F) CT.AileronControl = 0.1F;
			if (CT.AileronControl < -0.1F) CT.AileronControl = -0.1F;
			dA = (getSpeedKMH() - 180F - Or.getTangage() * 10F - getVertSpeed() * 5F) * 0.004F;
			CT.ElevatorControl = dA;
			if (mn_time > 3.5F) pop();
			break;

		case 39: // '\''
			setSpeedMode(6);
			CT.AileronControl = -0.04F * Or.getKren();
			CT.ElevatorControl = -0.04F * (Or.getTangage() + 10F);
			if (CT.RudderControl > 0.1F) CT.RudderControl = 0.8F;
			else if (CT.RudderControl < -0.1F) CT.RudderControl = -0.8F;
			else CT.RudderControl = Or.getKren() > 0.0F ? 1.0F : -1F;
			if (getSpeed() > Vmax || mn_time > 7F) pop();
			break;

		case 89: // 'Y'
			CT.AileronControl = -0.04F * Or.getKren();
			if (Alt > 50F) {
				dA = (getSpeedKMH() - 180F - Or.getTangage() * 10F - getVertSpeed() * 5F) * 0.004F;
				CT.ElevatorControl = dA;
			}
			CT.RudderControl += 0.1F;
			if (CT.RudderControl > 0.9F) CT.RudderControl = 0.9F;
			if (mn_time > 0.8F) pop();
			if (Leader != null) Vwld.z = Leader.Vwld.z;
			break;

		case 88: // 'X'
			CT.AileronControl = -0.04F * Or.getKren();
			if (Alt > 50F) {
				dA = (getSpeedKMH() - 180F - Or.getTangage() * 10F - getVertSpeed() * 5F) * 0.004F;
				CT.ElevatorControl = dA;
			}
			CT.RudderControl -= 0.1F;
			if (CT.RudderControl < -0.9F) CT.RudderControl = -0.9F;
			if (mn_time > 0.8F) pop();
			if (Leader != null) Vwld.z = Leader.Vwld.z;
			break;

		case 90: // 'Z'
			setSpeedMode(6);
			CT.RudderControl = 1.0F;
			if (Math.abs(Or.getRoll() - 360F) > 90F) CT.AileronControl = 0.0F;
			else if (Math.abs(Or.getRoll() - 360F) > 60F) {
				CT.AileronControl *= 0.8F;
			} else {
				CT.AileronControl -= 0.03F;
				if (CT.AileronControl < -1F) CT.AileronControl = -1F;
			}
			CT.ElevatorControl = Math.abs(Or.getRoll() - 360F) * -0.002F;
			if (mn_time > 0.8F) pop();
			if (Leader != null) {
				Vwld.z = Leader.Vwld.z;
				Vwld.y = Leader.Vwld.y;
			}
			break;

		case 91: // '['
			setSpeedMode(6);
			CT.RudderControl = -1F;
			if (Math.abs(Or.getRoll() - 360F) > 90F) CT.AileronControl = 0.0F;
			else if (Math.abs(Or.getRoll() - 360F) > 60F) {
				CT.AileronControl *= 0.8F;
			} else {
				CT.AileronControl += 0.03F;
				if (CT.AileronControl > 1.0F) CT.AileronControl = 1.0F;
			}
			CT.ElevatorControl = Math.abs(Or.getRoll() - 360F) * -0.002F;
			if (mn_time > 0.8F) pop();
			if (Leader != null) {
				Vwld.z = Leader.Vwld.z;
				Vwld.y = Leader.Vwld.y;
			}
			break;

		case 40: // '('
			push(39);
			push(57);
			pop();
			break;

		case 41: // ')'
			push(13);
			push(18);
			pop();
			break;

		case 42: // '*'
			push(19);
			push(57);
			pop();
			break;

		case 100: // 'd'
			setSpeedMode(6);
			if (getSpeed() < Vmax * 0.7F || Or.getPitch() > 360F) setSpeedMode(11);
			else setSpeedMode(8);
			minElevCoeff = 20F;
			dA = Math.abs(Or.getKren()) * (float) Group.bracketSideGr;
			if (Math.abs(dA) > 75F) set_maneuver(2);
			if (dA > 0.0F) dA -= 30F;
			else dA -= 330F;
			if (dA < -180F) dA += 360F;
			if (Math.abs(dA) > 60F) set_maneuver(2);
			dA = 0.03F * dA;
			CT.AileronControl = dA;
			CT.ElevatorControl = 1.0F;
			if (mn_time > 1.5F + World.Rnd().nextFloat(0.0F, 2.0F)) pop();
			break;

		case 46: // '.'
			if (target_ground == null || target_ground.pos == null) {
				if (Group != null) {
					dont_change_subm = true;
					boolean flag11 = isBusy();
					int j2 = Group.grTask;
					Group.grTask = 4;
					setBusy(false);
					Group.setTaskAndManeuver(Group.numInGroup((Aircraft) actor));
					setBusy(flag11);
					Group.grTask = j2;
				}
				if (target_ground == null || target_ground.pos == null) {
					AP.way.first();
					Airport airport = Airport.nearest(AP.way.curr().getP(), actor.getArmy(), 7);
					WayPoint waypoint5;
					if (airport != null) waypoint5 = new WayPoint(airport.pos.getAbsPoint());
					else waypoint5 = new WayPoint(AP.way.first().getP());
					waypoint5.set(0.6F * Vmax);
					waypoint5.Action = 2;
					AP.way.add(waypoint5);
					AP.way.last();
					set_task(3);
					clear_stack();
					maneuver = 21;
					set_maneuver(21);
					break;
				}
			}
			groundAttackKamikaze(target_ground, f);
			break;

		case 43: // '+'
		case 47: // '/'
		case 50: // '2'
		case 51: // '3'
		case 52: // '4'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
			if (first && !isCapableOfACM()) {
				bombsOut = true;
				setReadyToReturn(true);
				if (Group != null) {
					Group.waitGroup(Group.numInGroup((Aircraft) actor));
				} else {
					AP.way.next();
					set_task(3);
					clear_stack();
					set_maneuver(21);
				}
				break;
			}
			if (CT.dropWithPlayer != null && CT.dropWithPlayer.isAlive() && hasBombs() && maneuver == 43) {
				set_task(2);
				clear_stack();
				set_maneuver(24);
			}
			if (target_ground == null || target_ground.pos == null || !Actor.isAlive(target_ground)) {
				int i2 = maneuver;
				if (Group != null && Group.grTask == 4) {
					dont_change_subm = true;
					boolean flag12 = isBusy();
					setBusy(false);
					Group.setTaskAndManeuver(Group.numInGroup((Aircraft) actor));
					setBusy(flag12);
				}
				if (target_ground == null || target_ground.pos == null || !Actor.isAlive(target_ground)) {
					if (i2 == 50) bombsOut = true;
					if (Group != null) {
						Group.waitGroup(Group.numInGroup((Aircraft) actor));
					} else {
						AP.way.next();
						set_task(3);
						clear_stack();
						set_maneuver(21);
					}
					push(2);
					pop();
					break;
				}
			}
			switch (maneuver) {
			case 44: // ','
			case 45: // '-'
			case 48: // '0'
			case 49: // '1'
			case 53: // '5'
			case 54: // '6'
			case 55: // '7'
			case 56: // '8'
			case 57: // '9'
			case 58: // ':'
			case 59: // ';'
			case 60: // '<'
			case 61: // '='
			case 62: // '>'
			case 63: // '?'
			case 64: // '@'
			case 65: // 'A'
			case 66: // 'B'
			case 67: // 'C'
			case 68: // 'D'
			case 69: // 'E'
			case 70: // 'F'
			default:
				break;

			case 43: // '+'
				groundAttack(target_ground, f);
				if (mn_time > 120F) set_maneuver(0);
				break;

			case 50: // '2'
				groundAttackDiveBomber(target_ground, f);
				if (mn_time > 120F) {
					setSpeedMode(6);
					CT.BayDoorControl = 0.0F;
					CT.AirBrakeControl = 0.0F;
					CT.FlapsControl = 0.0F;
					pop();
					sub_Man_Count = 0;
				}
				break;

			case 51: // '3'
				groundAttackTorpedo(target_ground, f);
				break;

			case 73: // 'I'
				groundAttackTorpedoToKG(target_ground, f);
				break;

			case 52: // '4'
				groundAttackCassette(target_ground, f);
				break;

			case 46: // '.'
				groundAttackKamikaze(target_ground, f);
				break;

			case 47: // '/'
				groundAttackTinyTim(target_ground, f);
				break;

			case 71: // 'G'
				groundAttackHS293(target_ground, f);
				break;

			case 72: // 'H'
				groundAttackFritzX(target_ground, f);
				break;
			}
			break;
		}
		if (checkGround) doCheckGround(f);
		if (checkStrike && strikeEmer) doCheckStrike();
		strikeEmer = false;
		setSpeedControl(f);
		first = false;
		mn_time += f;
		if (frequentControl) AP.update(f);
		else AP.update(f * 4F);
		wasBusy = bBusy;
		if (shotAtFriend > 0) shotAtFriend--;
	}

	void OutCT(int i) {
		if (actor != Main3D.cur3D().viewActor()) return;
		TextScr.output(i + 5, 45, "Alt(MSL)  " + (int) Loc.z + "    " + (CT.BrakeControl <= 0.0F ? "" : "BRAKE"));
		TextScr.output(i + 5, 65, "Alt(AGL)  " + (int) (Loc.z - Engine.land().HQ_Air(Loc.x, Loc.y)));
		int j = 0;
		TextScr.output(i + 225, 140, "---ENGINES (" + EI.getNum() + ")---" + EI.engines[j].getStage());
		TextScr.output(i + 245, 120, "THTL " + (int) (100F * EI.engines[j].getControlThrottle()) + "%" + (EI.engines[j].getControlAfterburner() ? " (NITROS)" : ""));
		TextScr.output(i + 245, 100, "PROP " + (int) (100F * EI.engines[j].getControlProp()) + "%" + (CT.getStepControlAuto() ? " (AUTO)" : ""));
		TextScr.output(i + 245, 80, "MIX " + (int) (100F * EI.engines[j].getControlMix()) + "%");
		TextScr.output(i + 245, 60, "RAD " + (int) (100F * EI.engines[j].getControlRadiator()) + "%" + (CT.getRadiatorControlAuto() ? " (AUTO)" : ""));
		TextScr.output(i + 245, 40, "SUPC " + EI.engines[j].getControlCompressor() + "x");
		TextScr.output(245, 20, "PropAoA :" + (int) Math.toDegrees(EI.engines[j].getPropAoA()));
		TextScr.output(i + 455, 120, "Cyls/Cams " + EI.engines[j].getCylindersOperable() + "/" + EI.engines[0].getCylinders());
		TextScr.output(i + 455, 100, "Readyness " + (int) (100F * EI.engines[j].getReadyness()) + "%");
		TextScr.output(i + 455, 80, "PRM " + (int) ((float) (int) (EI.engines[j].getRPM() * 0.02F) * 50F) + " rpm");
		TextScr.output(i + 455, 60, "Thrust " + (int) EI.engines[j].getEngineForce().x + " N");
		TextScr.output(i + 455, 40, "Fuel " + (int) ((100F * M.fuel) / M.maxFuel) + "% Nitro " + (int) ((100F * M.nitro) / M.maxNitro) + "%");
		TextScr.output(i + 455, 20, "MPrs " + (int) (1000F * EI.engines[j].getManifoldPressure()) + " mBar");
		TextScr.output(i + 640, 140, "---Controls---");
		TextScr.output(i + 640, 120, "A/C: " + (CT.bHasAileronControl ? "" : "AIL ") + (CT.bHasElevatorControl ? "" : "ELEV ") + (CT.bHasRudderControl ? "" : "RUD ") + (Gears.bIsHydroOperable ? "" : "GEAR "));
		TextScr.output(i + 640, 100, "ENG: " + (EI.engines[j].isHasControlThrottle() ? "" : "THTL ") + (EI.engines[j].isHasControlProp() ? "" : "PROP ") + (EI.engines[j].isHasControlMix() ? "" : "MIX ")
				+ (EI.engines[j].isHasControlCompressor() ? "" : "SUPC ") + (EI.engines[j].isPropAngleDeviceOperational() ? "" : "GVRNR "));
		TextScr.output(i + 640, 80, "PIL: (" + (int) (AS.getPilotHealth(0) * 100F) + "%)");
		TextScr.output(i + 5, 105, "V   " + (int) getSpeedKMH());
		TextScr.output(i + 5, 125, "AOA " + (float) (int) (getAOA() * 1000F) / 1000F);
		TextScr.output(i + 5, 145, "AOS " + (float) (int) (getAOS() * 1000F) / 1000F);
		TextScr.output(i + 5, 165, "Kr  " + (int) Or.getKren());
		TextScr.output(i + 5, 185, "Ta  " + (int) Or.getTangage());
		TextScr.output(i + 250, 185, "way.speed  " + AP.way.curr().getV() * 3.6F + "way.z " + AP.way.curr().z() + "  mn_time = " + mn_time);
		TextScr.output(i + 5, 205, "<" + actor.name() + ">: " + ManString.tname(task) + ":" + ManString.name(maneuver) + " , WP=" + AP.way.Cur() + "(" + (AP.way.size() - 1) + ")-" + ManString.wpname(AP.way.curr().Action));
		TextScr.output(i + 7, 225, "======= " + ManString.name(program[0]) + "  Sub = " + submaneuver + " follOffs.x = " + followOffset.x + "  " + (((AutopilotAI) AP).bWayPoint ? "Stab WPoint " : "")
				+ (((AutopilotAI) AP).bStabAltitude ? "Stab ALT " : "") + (((AutopilotAI) AP).bStabDirection ? "Stab DIR " : "") + (((AutopilotAI) AP).bStabSpeed ? "Stab SPD " : "   ") + (((Pilot) ((Aircraft) actor).FM).isDumb() ? "DUMB " : " ")
				+ (((Pilot) ((Aircraft) actor).FM).Gears.lgear ? "L " : " ") + (((Pilot) ((Aircraft) actor).FM).Gears.rgear ? "R " : " ") + (((Pilot) ((Aircraft) actor).FM).TaxiMode ? "TaxiMode" : ""));
		TextScr.output(i + 7, 245, " ====== " + ManString.name(program[1]));
		TextScr.output(i + 7, 265, "  ===== " + ManString.name(program[2]));
		TextScr.output(i + 7, 285, "   ==== " + ManString.name(program[3]));
		TextScr.output(i + 7, 305, "    === " + ManString.name(program[4]));
		TextScr.output(i + 7, 325, "     == " + ManString.name(program[5]));
		TextScr.output(i + 7, 345, "      = " + ManString.name(program[6]) + "  " + (target != null ? "TARGET  " : "") + (target_ground != null ? "GROUND  " : "") + (danger != null ? "DANGER  " : ""));
		if (target != null && Actor.isValid(target.actor)) TextScr.output(i + 1, 365, " AT: (" + target.actor.name() + ") " + ((target.actor instanceof Aircraft) ? "" : target.actor.getClass().getName()));
		if (target_ground != null && Actor.isValid(target_ground)) TextScr.output(i + 1, 385, " GT: (" + target_ground.name() + ") ..." + target_ground.getClass().getName());
		TextScr.output(400, 500, "+");
		TextScr.output(400, 400, "|");
		TextScr.output((int) (400F + 200F * CT.AileronControl), (int) (500F - 200F * CT.ElevatorControl), "+");
		TextScr.output((int) (400F + 200F * CT.RudderControl), 400, "|");
		TextScr.output(250, 750, "followOffset  " + followOffset.x + "  " + followOffset.y + "  " + followOffset.z + "  ");
		if (Group != null && Group.enemies != null) TextScr.output(250, 760, "Enemies   " + AirGroupList.length(Group.enemies[0]));
		TextScr.output(700, 720, "speedMode   " + speedMode);
		if (Group != null) TextScr.output(690, 760, "Group task  " + Group.grTaskName());
		if (AP.way.isLandingOnShip()) TextScr.output(5, 460, "Landing On Carrier");
		TextScr.output(700, 660, "gattackCounter  " + gattackCounter);
		TextScr.output(5, 360, "silence = " + silence);
	}

	private static void rotCoord(double d, Vector3d vector3d) {
		double d1 = vector3d.x * (double) (float) Math.cos(d) - vector3d.y * (double) (float) Math.sin(d);
		double d2 = vector3d.x * (double) (float) Math.sin(d) + vector3d.y * (double) (float) Math.cos(d);
		vector3d.x = d1;
		vector3d.y = d2;
	}

	private void groundAttackGuns(Actor actor, float f) {
		if (submaneuver == 0 && sub_Man_Count == 0 && CT.Weapons[1] != null) {
			float f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			if (f1 > 0.01F) bullTime = 1.0F / (f1 + getSpeed());
		}
		maxAOA = 15F;
		minElevCoeff = 20F;
		switch (submaneuver) {
		case 0: // '\0'
			setCheckGround(true);
			rocketsDelay = 0;
			if (sub_Man_Count == 0) {
				Vtarg.set(actor.pos.getAbsPoint());
				koeff = ((float) (12 - flying) * M.getFullMass()) / (Sq.squareWing * Wing.CyCritH_0);
				float f7 = 0.2666667F;
				if (koeff < 800F) koeff = 800F;
				Ve.sub(Vtarg, Loc);
				float f2 = (float) Ve.length();
				float f6 = f2 - koeff * 1.75F;
				if (f2 > 3000F) f2 = 3000F;
				actor.getSpeed(tmpV3d);
				tmpV3f.x = tmpV3d.x;
				tmpV3f.y = tmpV3d.y;
				tmpV3f.z = 0.0D;
				double d = tmpV3f.length();
				double d1 = 0.0D;
				if (d < 9.9999997473787516E-005D || Group.getAaaNum() > 8.5F && !(actor instanceof TgtShip)) {
					tmpV3f.sub(Vtarg, Loc);
					tmpV3f.z = 0.0D;
					tmpV3f.normalize();
					d1 = Math.toDegrees(Math.atan2(tmpV3f.y, tmpV3f.x));
					d1 = (double) Or.getYaw() - d1;
					if (d1 > 180D) d1 -= 360D;
					else if (d1 < -180D) d1 += 360D;
					d1 = DEG2RAD(d1);
					d1 *= -0.33000000000000002D;
					if (f6 < 0.0F) f6 = koeff * 0.7F;
					else f6 = 0.0F;
					rotCoord(d1, tmpV3f);
				}
				tmpV3f.normalize();
				Vtarg.x -= tmpV3f.x * (double) koeff;
				Vtarg.y -= tmpV3f.y * (double) koeff;
				Vtarg.z += koeff * f7;
				constVtarg.set(Vtarg);
				if (d < 9.9999997473787516E-005D) {
					rotCoord(-0.69999999999999996D * d1, tmpV3f);
					tmpV3f.normalize();
					d1 = Math.abs(d1);
					Vtarg.x -= tmpV3f.x * (double) koeff * d1 + (double) f6;
					Vtarg.y -= tmpV3f.y * (double) koeff * d1 + (double) f6;
				} else {
					Ve.sub(constVtarg, Loc);
					Ve.normalize();
					Vxy.cross(Ve, tmpV3f);
					Ve.sub(tmpV3f);
					if (Vxy.z > 0.0D) {
						Vtarg.x += Ve.y * 0.75D * (double) koeff;
						Vtarg.y -= Ve.x * 0.75D * (double) koeff;
					} else {
						Vtarg.x -= Ve.y * 0.75D * (double) koeff;
						Vtarg.y += Ve.x * 0.75D * (double) koeff;
					}
				}
				Vtarg.z += koeff * f7 * 0.2F;
				constVtarg1.set(Vtarg);
				Vtarg.sub(constVtarg1, constVtarg);
				if (Vtarg.length() < 500D) {
					constVtarg1.add(constVtarg);
					constVtarg1.scale(0.5D);
					constVtarg.set(constVtarg1);
				}
			}
			Ve.set(constVtarg1);
			Ve.sub(Loc);
			float f3 = (float) Ve.length();
			Or.transformInv(Ve);
			if (Ve.x < 0.0D && f3 < koeff) {
				push(0);
				push(8);
				pop();
				dontSwitch = true;
			} else {
				Ve.normalize();
				if (Loc.z < constVtarg1.z) {
					setSpeedMode(6);
				} else {
					setSpeedMode(4);
					smConstSpeed = 100F;
				}
				if (f3 > 1000F) farTurnToDirection(8F);
				else attackTurnToDirection(f3, f, 4F + (float) Skill * 2.0F);
				sub_Man_Count++;
				if (f3 < 300F) {
					submaneuver++;
					gattackCounter++;
					sub_Man_Count = 0;
				}
			}
			break;

		case 1: // '\001'
			Ve.set(constVtarg);
			Ve.sub(Loc);
			float f4 = (float) Ve.length();
			Or.transformInv(Ve);
			Ve.normalize();
			if (Loc.z < constVtarg.z) {
				setSpeedMode(6);
			} else {
				setSpeedMode(4);
				smConstSpeed = 100F;
			}
			if (f4 > 1000F) farTurnToDirection(8F);
			else attackTurnToDirection(f4, f, 4F + (float) Skill * 2.0F);
			sub_Man_Count++;
			if (f4 < 300F) {
				submaneuver++;
				gattackCounter++;
				sub_Man_Count = 0;
			}
			break;

		case 2: // '\002'
			if (rocketsDelay > 0) rocketsDelay--;
			if (sub_Man_Count > 100) setCheckGround(false);
			P.set(actor.pos.getAbsPoint());
			P.z += 4D;
			Engine.land();
			if (Landscape.rayHitHQ(Loc, P, P)) {
				push(0);
				push(38);
				pop();
				gattackCounter--;
				if (gattackCounter < 0) gattackCounter = 0;
			}
			P.set(actor.pos.getAbsPoint());
			Ve.sub(P, Loc);
			float f5 = (float) Ve.length();
			setRSDgrd(shootingDeviation, f5 * bullTime);
			Ve.add(wanderVector);
			actor.getSpeed(tmpV3d);
			tmpV3f.x = (float) tmpV3d.x;
			tmpV3f.y = (float) tmpV3d.y;
			tmpV3f.z = (float) tmpV3d.z;
			tmpV3f.sub(Vwld);
			tmpV3f.scale(f5 * bullTime * 0.3333F * (float) Skill);
			Ve.add(tmpV3f);
			float f8 = 0.3F * (f5 - 1000F);
			if (f8 < 0.0F) f8 = 0.0F;
			if (f8 > 300F) f8 = 300F;
			f8 += f5 * getAOA() * 0.005F;
			Ve.z += f8 + Group.getAaaNum() * 0.5F;
			Or.transformInv(Ve);
			if (f5 < 800F && (shotAtFriend <= 0 || distToFriend > f5)) {
				shootingDeviation -= f;
				if (shootingDeviation < (float) (6 - gunnery)) shootingDeviation = 6 - gunnery;
				if (Math.abs(Ve.y) < 15D && Math.abs(Ve.z) < 10D) {
					if (f5 < 800F && Group.getAaaNum() > 10F || f5 < 650F) CT.WeaponControl[0] = true;
					if (f5 < 700F && Group.getAaaNum() > 10F || f5 < 550F) CT.WeaponControl[1] = true;
					if (CT.Weapons[2] != null && CT.Weapons[2][0] != null && CT.Weapons[2][CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1 && f5 < 570F) {
						CT.WeaponControl[2] = true;
						Voice.speakAttackByRockets((Aircraft) this.actor);
						if (Group.getAaaNum() > 2.0F) rocketsDelay += 30F - Group.getAaaNum();
						else rocketsDelay += 30;
					}
				}
			}
			if (sub_Man_Count > 200 && Ve.x < (double) (getSpeed() * 2.4F) || (this.actor instanceof TypeStormovik) && Alt < 80F || Alt < 40F) {
				if (Leader == null || !Actor.isAlive(Leader.actor)) Voice.speakEndGattack((Aircraft) this.actor);
				rocketsDelay = 0;
				push(0);
				push(55);
				push(10);
				if (Group.getAaaNum() > 3F) push(105);
				push(100);
				pop();
				dontSwitch = true;
				return;
			}
			Ve.normalize();
			attackTurnToDirection(f5, f, 10F + (float) Skill * 2.0F);
			setSpeedMode(4);
			smConstSpeed = 100F;
			break;

		default:
			submaneuver = 0;
			sub_Man_Count = 0;
			break;
		}
	}

	// TODO: All ground attack methods have been edited for the DBW AI Mod
	private void groundAttack(Actor actor, float f) {
		// TODO: DBW AI Mod Edits
		if (!(super.actor instanceof TypeSupersonic) && !(super.actor instanceof TypeFastJet) && !(super.actor instanceof TypeDiveBomber)) bombsOutCounter = 129;
		else bombsOutCounter = 0;
		bombsOut = false;
		super.CT.WeaponControl[3] = false;
		// TODO: DBW AI Mod Edits
		if ((super.actor instanceof TypeSupersonic) && (super.actor instanceof TypeFastJet)) {
			setSpeedMode(7);
		} else {
			setSpeedMode(4);
			smConstSpeed = 120F;
		}
		float f1 = ((Tuple3d) (super.Vwld)).z <= 0.0D ? 4F : 3F;
		boolean flag = false;
		boolean flag1 = false;
		if (hasBombs()) {
			flag = true;
			if (super.CT.Weapons[3][0] instanceof ParaTorpedoGun) flag1 = true;
		} else if (!(super.actor instanceof TypeStormovik) && !(super.actor instanceof TypeFighter) && !(super.actor instanceof TypeDiveBomber)) {
			set_maneuver(0);
			return;
		}
		Ve.set(actor.pos.getAbsPoint());
		if (flag1) if (super.CT.Weapons[3][0] instanceof BombGunTorp45_36AV_A) Ve.z = (((Tuple3d) (super.Loc)).z - 100D) + World.Rnd().nextDouble() * 50D;
		else Ve.z = (((Tuple3d) (super.Loc)).z - 200D) + World.Rnd().nextDouble() * 50D;
		float f2 = (float) ((Tuple3d) (super.Loc)).z - (float) ((Tuple3d) (Ve)).z;
		if (f2 < 0.0F) f2 = 0.0F;
		float f3 = (float) Math.sqrt(2.0F * f2 * 0.1019F) + 0.0017F * f2;
		actor.getSpeed(tmpV3d);
		if ((actor instanceof Aircraft) && tmpV3d.length() > 20D) {
			target = ((SndAircraft) ((Aircraft) actor)).FM;
			set_task(6);
			clear_stack();
			set_maneuver(27);
			dontSwitch = true;
		} else {
			float f4 = 0.5F;
			if (flag) f4 = (f3 + 5F) * 0.33333F;
			Vtarg.x = (float) ((Tuple3d) (tmpV3d)).x * f4 * (float) super.Skill;
			Vtarg.y = (float) ((Tuple3d) (tmpV3d)).y * f4 * (float) super.Skill;
			Vtarg.z = (float) ((Tuple3d) (tmpV3d)).z * f4 * (float) super.Skill;
			Ve.add(Vtarg);
			Ve.sub(super.Loc);
			if (flag) addWindCorrection();
			Ve.add(0.0D, 0.0D, -0.5F + World.Rnd().nextFloat(-2F, 0.8F));
			Vf.set(Ve);
			float f5 = (float) Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y);
			if (flag) {
				float f6 = getSpeed() * f3 + 100F;
				if (f5 > f6) Ve.z += 200D;
				else Ve.z = 0.0D;
			}
			Vtarg.set(Ve);
			Vtarg.normalize();
			super.Or.transformInv(Ve);
			if (!hasBombs()) groundAttackGuns(actor, f);
			else if ((super.actor instanceof TypeFighter) || (super.actor instanceof TypeStormovik)) {
				// TODO: DBW AI Mod Edits
				if (!(super.actor instanceof TypeSupersonic) && !(super.actor instanceof TypeFastJet) && !(super.actor instanceof TypeDiveBomber)) bombsOutCounter = 129;
				else bombsOutCounter = 0;
				passCounter = 0;
				groundAttackShallowDive(actor, f);
			} else {
				Ve.normalize();
				Vpl.set(super.Vwld);
				Vpl.normalize();
				super.CT.BayDoorControl = 1.0F;
				if (f5 + 4F * f3 < getSpeed() * f3 && ((Tuple3d) (Ve)).x > 0.0D && Vpl.dot(Vtarg) > 0.98000001907348633D) {
					if (!bombsOut) {
						bombsOut = true;
						if (super.CT.Weapons[3] != null && super.CT.Weapons[3][0] != null && super.CT.Weapons[3][0].countBullets() != 0 && !(super.CT.Weapons[3][0] instanceof BombGunPara)) Voice.speakAttackByBombs((Aircraft) super.actor);
					}
					push(0);
					push(55);
					push(48);
					pop();
					Voice.speakEndGattack((Aircraft) super.actor);
					super.CT.BayDoorControl = 0.0F;
				}
				if (((Tuple3d) (Ve)).x < 0.0D) {
					push(0);
					push(55);
					push(10);
					pop();
				}
				if (Math.abs(((Tuple3d) (Ve)).y) > 0.10000000149011612D) super.CT.AileronControl = -(float) Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).z) - 0.016F * super.Or.getKren();
				else super.CT.AileronControl = -(float) Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) - 0.016F * super.Or.getKren();
				if (Math.abs(((Tuple3d) (Ve)).y) > 0.0010000000474974513D) super.CT.RudderControl = -98F * (float) Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x);
				else super.CT.RudderControl = 0.0F;
				if ((double) super.CT.RudderControl * ((Tuple3d) (super.W)).z > 0.0D) super.W.z = 0.0D;
				else super.W.z *= 1.0399999618530273D;
				float f7 = (float) Math.atan2(((Tuple3d) (Ve)).z, ((Tuple3d) (Ve)).x);
				if (Math.abs(((Tuple3d) (Ve)).y) < 0.0020000000949949026D && Math.abs(((Tuple3d) (Ve)).z) < 0.0020000000949949026D) f7 = 0.0F;
				if (((Tuple3d) (Ve)).x < 0.0D) {
					f7 = 1.0F;
				} else {
					if ((double) f7 * ((Tuple3d) (super.W)).y > 0.0D) super.W.y = 0.0D;
					if (f7 < 0.0F) {
						if (getOverload() < 0.1F) f7 = 0.0F;
						if (super.CT.ElevatorControl > 0.0F) super.CT.ElevatorControl = 0.0F;
					} else if (super.CT.ElevatorControl < 0.0F) super.CT.ElevatorControl = 0.0F;
				}
				if (getOverload() > maxG || super.AOA > f1 || super.CT.ElevatorControl > f7) super.CT.ElevatorControl -= 0.2F * f;
				else super.CT.ElevatorControl += 0.2F * f;
			}
		}
	}

	private void groundAttackKamikaze(Actor actor, float f) {
		if (submaneuver == 0 && sub_Man_Count == 0 && CT.Weapons[1] != null) {
			float f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			if (f1 > 0.01F) bullTime = 1.0F / f1;
		}
		maxAOA = 15F;
		minElevCoeff = 20F;
		switch (submaneuver) {
		case 0: // '\0'
			setCheckGround(true);
			rocketsDelay = 0;
			if (sub_Man_Count == 0) {
				Vtarg.set(actor.pos.getAbsPoint());
				tmpV3f.set(Vwld);
				tmpV3f.x += World.Rnd().nextFloat(-100F, 100F);
				tmpV3f.y += World.Rnd().nextFloat(-100F, 100F);
				tmpV3f.z = 0.0D;
				if (tmpV3f.length() < 9.9999997473787516E-005D) {
					tmpV3f.sub(Vtarg, Loc);
					tmpV3f.z = 0.0D;
				}
				tmpV3f.normalize();
				Vtarg.x -= tmpV3f.x * 1500D;
				Vtarg.y -= tmpV3f.y * 1500D;
				Vtarg.z += 400D;
				constVtarg.set(Vtarg);
				Ve.sub(constVtarg, Loc);
				Ve.normalize();
				Vxy.cross(Ve, tmpV3f);
				Ve.sub(tmpV3f);
				Vtarg.z += 100D;
				if (Vxy.z > 0.0D) {
					Vtarg.x += Ve.y * 1000D;
					Vtarg.y -= Ve.x * 1000D;
				} else {
					Vtarg.x -= Ve.y * 1000D;
					Vtarg.y += Ve.x * 1000D;
				}
				constVtarg1.set(Vtarg);
			}
			Ve.set(constVtarg1);
			Ve.sub(Loc);
			float f2 = (float) Ve.length();
			Or.transformInv(Ve);
			if (Ve.x < 0.0D) {
				push(0);
				push(8);
				pop();
				dontSwitch = true;
			} else {
				Ve.normalize();
				setSpeedMode(6);
				farTurnToDirection();
				sub_Man_Count++;
				if (f2 < 300F) {
					submaneuver++;
					gattackCounter++;
					sub_Man_Count = 0;
				}
				if (sub_Man_Count > 1000) sub_Man_Count = 0;
			}
			break;

		case 1: // '\001'
			setCheckGround(true);
			Ve.set(constVtarg);
			Ve.sub(Loc);
			float f3 = (float) Ve.length();
			Or.transformInv(Ve);
			Ve.normalize();
			setSpeedMode(6);
			farTurnToDirection();
			sub_Man_Count++;
			if (f3 < 300F) {
				submaneuver++;
				gattackCounter++;
				sub_Man_Count = 0;
			}
			if (sub_Man_Count > 700) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 2: // '\002'
			setCheckGround(false);
			if (rocketsDelay > 0) rocketsDelay--;
			if (sub_Man_Count > 100) setCheckGround(false);
			Ve.set(actor.pos.getAbsPoint());
			Ve.sub(Loc);
			Vtarg.set(Ve);
			float f4 = (float) Ve.length();
			if (this.actor instanceof MXY_7) {
				Ve.z += 0.01D * (double) f4;
				Vtarg.z += 0.01D * (double) f4;
			}
			actor.getSpeed(tmpV3d);
			tmpV3f.x = (float) tmpV3d.x;
			tmpV3f.y = (float) tmpV3d.y;
			tmpV3f.z = (float) tmpV3d.z;
			tmpV3f.sub(Vwld);
			tmpV3f.scale(f4 * bullTime * 0.3333F * (float) Skill);
			Ve.add(tmpV3f);
			float f5 = 0.3F * (f4 - 1000F);
			if (f5 < 0.0F) f5 = 0.0F;
			if (f5 > 300F) f5 = 300F;
			Ve.z += f5 + World.cur().rnd.nextFloat(-3F, 3F) * (float) (3 - Skill);
			Or.transformInv(Ve);
			if (f4 < 50F && Math.abs(Ve.y) < 40D && Math.abs(Ve.z) < 30D) {
				CT.WeaponControl[0] = true;
				CT.WeaponControl[1] = true;
				CT.WeaponControl[2] = true;
				CT.WeaponControl[3] = true;
			}
			if (Ve.x < -50D) {
				rocketsDelay = 0;
				push(0);
				push(55);
				push(10);
				pop();
				dontSwitch = true;
				return;
			}
			Ve.normalize();
			attackTurnToDirection(f4, f, 4F + (float) Skill * 2.0F);
			setSpeedMode(4);
			smConstSpeed = 130F;
			break;

		default:
			submaneuver = 0;
			sub_Man_Count = 0;
			break;
		}
	}

	private void groundAttackTinyTim(Actor actor, float f) {
		maxG = 5F;
		maxAOA = 8F;
		setSpeedMode(4);
		smConstSpeed = 120F;
		minElevCoeff = 20F;
		switch (submaneuver) {
		case 0: // '\0'
			if (sub_Man_Count == 0) {
				Vtarg.set(actor.pos.getAbsPoint());
				actor.getSpeed(tmpV3d);
				if (tmpV3d.length() < 0.5D) tmpV3d.sub(Vtarg, Loc);
				tmpV3d.normalize();
				Vtarg.x -= tmpV3d.x * 3000D;
				Vtarg.y -= tmpV3d.y * 3000D;
				Vtarg.z += 500D;
			}
			Ve.sub(Vtarg, Loc);
			double d = Ve.length();
			Or.transformInv(Ve);
			sub_Man_Count++;
			if (d < 1000D) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			Ve.normalize();
			farTurnToDirection();
			break;

		case 1: // '\001'
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.z += 80D;
			Ve.sub(Vtarg, Loc);
			double d1 = Ve.length();
			Or.transformInv(Ve);
			sub_Man_Count++;
			if (d1 < 1500D) {
				if (Math.abs(Ve.y) < 40D && Math.abs(Ve.z) < 30D) CT.WeaponControl[2] = true;
				push(0);
				push(10);
				push(48);
				pop();
				dontSwitch = true;
			}
			if (d1 < 500D && Ve.x < 0.0D) {
				push(0);
				push(10);
				pop();
			}
			Ve.normalize();
			if (Ve.x < 0.80000001192092896D) turnToDirection(f);
			else attackTurnToDirection((float) d1, f, 2.0F + (float) Skill * 1.5F);
			break;

		default:
			submaneuver = 0;
			sub_Man_Count = 0;
			break;
		}
	}

	private void groundAttackHS293(Actor actor, float f) {
		maxG = 5F;
		maxAOA = 8F;
		setSpeedMode(4);
		smConstSpeed = 120F;
		minElevCoeff = 20F;
		switch (submaneuver) {
		case 0: // '\0'
			if (sub_Man_Count == 0) {
				Vtarg.set(actor.pos.getAbsPoint());
				actor.getSpeed(tmpV3d);
				if (tmpV3d.length() < 0.5D) tmpV3d.sub(Vtarg, Loc);
				tmpV3d.normalize();
				Vtarg.x -= tmpV3d.x * 3000D;
				Vtarg.y -= tmpV3d.y * 3000D;
				Vtarg.z += 500D;
			}
			Ve.sub(Vtarg, Loc);
			double d = Ve.length();
			Or.transformInv(Ve);
			sub_Man_Count++;
			if (d < 10000D) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			Ve.normalize();
			farTurnToDirection();
			break;

		case 1: // '\001'
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.z += 2000D;
			Ve.sub(Vtarg, Loc);
			double d1 = Ve.angle(Vwld);
			Ve.z = 0.0D;
			double d2 = Ve.length();
			Or.transformInv(Ve);
			sub_Man_Count++;
			TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier) this.actor;
			if (d2 > 2000D && d2 < 6500D && d1 < 0.90000000000000002D && !typeguidedbombcarrier.typeGuidedBombCgetIsGuiding()) {
				CT.WeaponControl[3] = true;
				push(0);
				push(10);
				pop();
				dontSwitch = true;
			}
			if (d2 < 500D && Ve.x < 0.0D) {
				push(0);
				push(10);
				pop();
			}
			Ve.normalize();
			if (Ve.x < 99999.800000011921D) turnToDirection(f);
			else attackTurnToDirection((float) d2, f, 2.0F + (float) Skill * 1.5F);
			break;

		default:
			submaneuver = 0;
			sub_Man_Count = 0;
			break;
		}
	}

	private void groundAttackFritzX(Actor actor, float f) {
		maxG = 5F;
		maxAOA = 8F;
		setSpeedMode(4);
		smConstSpeed = 140F;
		minElevCoeff = 20F;
		switch (submaneuver) {
		case 0: // '\0'
			if (sub_Man_Count == 0) {
				Vtarg.set(actor.pos.getAbsPoint());
				actor.getSpeed(tmpV3d);
				if (tmpV3d.length() < 0.5D) tmpV3d.sub(Vtarg, Loc);
				tmpV3d.normalize();
				Vtarg.x -= tmpV3d.x * 3000D;
				Vtarg.y -= tmpV3d.y * 3000D;
				Vtarg.z += 500D;
			}
			Ve.sub(Vtarg, Loc);
			double d = Ve.length();
			Or.transformInv(Ve);
			sub_Man_Count++;
			if (d < 15000D) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			Ve.normalize();
			farTurnToDirection();
			break;

		case 1: // '\001'
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.z += 2000D;
			Ve.sub(Vtarg, Loc);
			double d1 = Ve.angle(Vwld);
			Ve.z = 0.0D;
			double d2 = Ve.length();
			Or.transformInv(Ve);
			sub_Man_Count++;
			TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier) this.actor;
			if (d2 < 4000D && d1 < 0.90000000000000002D && (d2 < 2000D || d1 > 0.40000000000000002D) && !typeguidedbombcarrier.typeGuidedBombCgetIsGuiding()) {
				CT.WeaponControl[3] = true;
				setSpeedMode(5);
				push(0);
				push(10);
				pop();
				dontSwitch = true;
			}
			if (d2 < 500D && Ve.x < 0.0D) {
				push(0);
				push(10);
				pop();
			}
			Ve.normalize();
			if (Ve.x < 99999.800000011921D) turnToDirection(f);
			else attackTurnToDirection((float) d2, f, 2.0F + (float) Skill * 1.5F);
			break;

		default:
			submaneuver = 0;
			sub_Man_Count = 0;
			break;
		}
	}

	private void groundAttackShallowDive(Actor actor, float f) {
		maxAOA = 8F;
		if (!hasBombs()) {
			set_maneuver(0);
			wingman(true);
		} else {
			if (first) RandomVal = 50F - World.cur().rnd.nextFloat(0.0F, 100F);
			// TODO: DBW AI Mod Edits
			if ((super.actor instanceof TypeSupersonic) || (super.actor instanceof TypeFastJet)) {
				setSpeedMode(7);
			} else {
				setSpeedMode(4);
				smConstSpeed = 120F;
			}
			Ve.set(actor.pos.getAbsPoint());
			Ve.sub(super.Loc);
			addWindCorrection();
			float f1 = (float) (-((Tuple3d) (Ve)).z);
			if (f1 < 0.0F) f1 = 0.0F;
			Ve.z += 250D;
			float f2 = (float) Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y) + RandomVal * (float) (2.75D - (double) super.Skill);
			if (((Tuple3d) (Ve)).z < (double) (-0.1F * f2)) Ve.z = -0.1F * f2;
			if ((double) Alt + ((Tuple3d) (Ve)).z < 250D) Ve.z = 250F - Alt;
			if (Alt < 50F) {
				push(10);
				pop();
			}
			Vf.set(Ve);
			super.CT.BayDoorControl = 1.0F;
			float f3 = (float) ((Tuple3d) (super.Vwld)).z * 0.1019F;
			f3 += (float) Math.sqrt(f3 * f3 + 2.0F * f1 * 0.1019F);
			float f4 = (float) Math.sqrt(((Tuple3d) (super.Vwld)).x * ((Tuple3d) (super.Vwld)).x + ((Tuple3d) (super.Vwld)).y * ((Tuple3d) (super.Vwld)).y);
			float f5 = f4 * f3 + 10F;
			actor.getSpeed(tmpV3d);
			tmpV3d.scale((double) f3 * 0.45000000000000001D * (double) super.Skill);
			Ve.x += (float) ((Tuple3d) (tmpV3d)).x;
			Ve.y += (float) ((Tuple3d) (tmpV3d)).y;
			Ve.z += (float) ((Tuple3d) (tmpV3d)).z;
			if ((CT.Weapons[3][0].getClass().getName().endsWith("SnakeEye") ? f5 >= (f2 * 1.175) : f5 >= f2) && passCounter == 0) {
				bombsOut = true;
				// TODO: DBW AI Mod Edits
				if (!(super.actor instanceof TypeSupersonic) && !(super.actor instanceof TypeFastJet) && !(super.actor instanceof TypeDiveBomber)) bombsOutCounter = 129;
				else bombsOutCounter = 0;
				Voice.speakAttackByBombs((Aircraft) super.actor);
				setSpeedMode(6);
				super.CT.BayDoorControl = 0.0F;
				pop();
				sub_Man_Count = 0;
				passCounter++;
			} else if (passCounter >= 5)
			;
			passCounter = 0;
			push(55);
			push(48);
			super.Or.transformInv(Ve);
			Ve.normalize();
			turnToDirection(f);
		}
	}

	private void groundAttackDiveBomber(Actor actor, float f) {
		maxG = 5F;
		maxAOA = 10F;
		setSpeedMode(6);
		maxAOA = 4F;
		minElevCoeff = 20F;
		if (CT.Weapons[3] == null || CT.getWeaponCount(3) == 0) {
			if (AP.way.curr().Action == 3) AP.way.next();
			set_maneuver(0);
			wingman(true);
			return;
		}
		if (Alt < 350F) {
			bombsOut = true;
			Voice.speakAttackByBombs((Aircraft) this.actor);
			setSpeedMode(6);
			CT.BayDoorControl = 0.0F;
			CT.AirBrakeControl = 0.0F;
			AP.way.next();
			push(0);
			push(8);
			push(2);
			pop();
			sub_Man_Count = 0;
		}
		Ve.set(actor.pos.getAbsPoint());
		Ve.sub(Loc);
		float f4 = (float) (-Ve.z);
		dist = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
		if (f1 > 1000F || submaneuver == 3 && sub_Man_Count > 100) {
			Vtarg.set(actor.pos.getAbsPoint());
			actor.getSpeed(tmpV3d);
			float f5 = 0.0F;
			f5 = Alt / 120F;
			f5 *= 0.33333F;
			Vtarg.x += (float) tmpV3d.x * f5 * (float) Skill;
			Vtarg.y += (float) tmpV3d.y * f5 * (float) Skill;
			Vtarg.z += (float) tmpV3d.z * f5 * (float) Skill;
		}
		Ve.set(Vtarg);
		Ve.sub(Loc);
		float f3 = (float) (-Ve.z);
		if (f3 < 0.0F) f3 = 0.0F;
		Ve.add(Vxy);
		f4 = (float) (-Ve.z);
		dist = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
		f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
		if (submaneuver < 2) Ve.z = 0.0D;
		Vf.set(Ve);
		Ve.normalize();
		Vpl.set(Vwld);
		Vpl.normalize();
		switch (submaneuver) {
		default:
			break;

		case 0: // '\0'
			push();
			pop();
			if (f4 < 1200F) man1Dist = 400F;
			else if (f4 > 4500F) man1Dist = 50F;
			else man1Dist = 50F + (350F * (4500F - f4)) / 3300F;
			float f6 = 0.01F * f4;
			if (f6 < 10F) f6 = 10F;
			Vxy.set(World.Rnd().nextFloat(-10F, 10F), World.Rnd().nextFloat(-10F, 10F), World.Rnd().nextFloat(5F, f6));
			Vxy.scale(4F - (float) Skill);
			float f7 = 2E-005F * f4 * f4;
			if (f7 < 60F) f7 = 60F;
			if (f7 > 350F) f7 = 350F;
			Vxy.z += f7;
			submaneuver++;
			break;

		case 1: // '\001'
			setSpeedMode(4);
			smConstSpeed = 110F;
			if (f1 >= man1Dist) break;
			CT.AirBrakeControl = 1.0F;
			if (this.actor instanceof TypeFighter) CT.FlapsControl = 1.0F;
			push();
			push(6);
			safe_pop();
			submaneuver++;
			break;

		case 2: // '\002'
			setSpeedMode(4);
			smConstSpeed = 110F;
			sub_Man_Count++;
			CT.AirBrakeControl = 1.0F;
			if (this.actor instanceof TypeFighter) CT.FlapsControl = 1.0F;
			float f2 = Or.getKren();
			if (Or.getTangage() > -90F) {
				f2 -= 180F;
				if (f2 < -180F) f2 += 360F;
			}
			CT.AileronControl = (float) ((double) (-0.04F * f2) - 0.5D * getW().x);
			if (getOverload() < 4F) CT.ElevatorControl += 0.3F * f;
			else CT.ElevatorControl -= 0.3F * f;
			if (sub_Man_Count > 30 && Or.getTangage() < -90F || sub_Man_Count > 150) {
				sub_Man_Count = 0;
				submaneuver++;
			}
			break;

		case 3: // '\003'
			CT.AirBrakeControl = 1.0F;
			if (this.actor instanceof TypeFighter) CT.FlapsControl = 1.0F;
			CT.BayDoorControl = 1.0F;
			setSpeedMode(4);
			smConstSpeed = 110F;
			sub_Man_Count++;
			if (sub_Man_Count > 80) {
				float f8 = (float) Vwld.z * 0.1019F;
				f8 += (float) Math.sqrt(f8 * f8 + 2.0F * f3 * 0.1019F) + 0.00035F * f3;
				float f9 = (float) Math.sqrt(Vwld.x * Vwld.x + Vwld.y * Vwld.y);
				float f10 = f9 * f8;
				float f11 = 0.2F * (f1 - f10);
				Vxy.z += f11;
				if (Vxy.z > (double) (0.7F * f3)) Vxy.z = 0.7F * f3;
			}
			if (sub_Man_Count > 100 && Alt < 1000F && Vpl.dot(Ve) > 0.99000000953674316D || Alt < 600F) {
				bombsOut = true;
				Voice.speakAttackByBombs((Aircraft) this.actor);
				CT.BayDoorControl = 0.0F;
				CT.AirBrakeControl = 0.0F;
				AP.way.next();
				push(0);
				push(8);
				push(2);
				pop();
			}
			break;
		}
		Or.transformInv(Ve);
		Ve.normalize();
		if (submaneuver == 3) attackTurnToDirection(1000F, f, 30F);
		else if (submaneuver != 2) turnToDirection(f);
	}

	private void groundAttackTorpedo(Actor actor, float f) {
		float f4 = 50F;
		maxG = 5F;
		maxAOA = 8F;
		float f5 = 0.0F;
		setSpeedMode(4);
		Class class1 = null;
		if (CT.Weapons[3][0] instanceof TorpedoGun) {
			TorpedoGun torpedogun = (TorpedoGun) CT.Weapons[3][0];
			class1 = (Class) Property.value(torpedogun.getClass(), "bulletClass", null);
		}
		smConstSpeed = 100F;
		if (class1 != null) {
			smConstSpeed = Property.floatValue(class1, "dropSpeed", 100F) / 3.7F;
			f4 = Property.floatValue(class1, "dropAltitude", 50F) + 10F;
		}
		if (this.actor instanceof Swordfish) {
			setSpeedMode(11);
			f4 = 20F;
		}
		float f6 = 0.0F;
		minElevCoeff = 20F;
		Ve.set(actor.pos.getAbsPoint());
		Ve.sub(Loc);
		if (first) {
			Vtarg.set(actor.pos.getAbsPoint());
			submaneuver = 0;
		}
		if (submaneuver == 1 && sub_Man_Count == 0) {
			tmpV3f.set(actor.pos.getAbsPoint());
			tmpV3f.sub(Vtarg);
			actor.getSpeed(tmpV3d);
			if (tmpV3f.length() > 9.9999997473787516E-005D) tmpV3f.normalize();
			Vxy.set(tmpV3f.y * 3000D, -tmpV3f.x * 3000D, 0.0D);
			direc = Vxy.dot(Ve) > 0.0D;
			if (direc) Vxy.scale(-1D);
			Vtarg.add(Vxy);
			Vtarg.x += tmpV3d.x * 80D;
			Vtarg.y += tmpV3d.y * 80D;
			Vtarg.z = 80D;
			TargDevV.set((double) World.cur().rnd.nextFloat(-16F, 16F) * (3.5D - (double) Skill), (double) World.cur().rnd.nextFloat(-16F, 16F) * (3.5D - (double) Skill), 0.0D);
		}
		if (submaneuver == 2) {
			Vtarg.set(actor.pos.getAbsPoint());
			actor.getSpeed(tmpV3d);
			float f7 = 20F;
			if (class1 != null) f7 = Property.floatValue(class1, "velocity", 1.0F);
			float f9 = actor.collisionR();
			if (f9 > 80F) f5 = 50F;
			if (f9 > 130F) f5 = 100F;
			if (f9 < 25F) f5 = -50F;
			float f12 = 950F;
			if (f4 > 110F) {
				f6 = f4;
				f12 += f4 * 0.4F;
			}
			if (this.actor instanceof JU_88NEW) f12 += 90F;
			double d1 = Math.sqrt(0.20399999999999999D * Loc.z);
			double d2 = 1.0D * d1 * (double) getSpeed();
			double d3 = ((double) (f12 + f5) - d2) / (double) f7;
			Vtarg.x += (float) (tmpV3d.x * d3);
			Vtarg.y += (float) (tmpV3d.y * d3);
			Vtarg.z = f4;
			if (Loc.z < 30D) Vtarg.z += 3D * (30D - Loc.z);
			Vtarg.add(TargDevV);
		}
		Ve.set(Vtarg);
		Ve.sub(Loc);
		float f2 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
		Vf.set(Ve);
		Vpl.set(Vwld);
		Vpl.normalize();
		if (Alt < f4 - 5F) {
			if (Vwld.z < 0.0D) Vwld.z += (f4 - Alt) * 0.25F;
			if (Alt < 8F) set_maneuver(2);
			if (Alt < 20F && f2 < 75F) set_maneuver(2);
		} else if (Alt > f4 + 5F && submaneuver == 1 && Vwld.z > 0.0D) Vwld.z--;
		switch (submaneuver) {
		default:
			break;

		case 0: // '\0'
			sub_Man_Count++;
			if (sub_Man_Count > 60) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			addWindCorrection();
			break;

		case 1: // '\001'
			setSpeedMode(4);
			if (this.actor instanceof Swordfish) setSpeedMode(9);
			sub_Man_Count++;
			if (f2 < 1200F || f2 < 2000F && ZutiSupportMethods.isStaticActor(actor)) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			addWindCorrection();
			break;

		case 2: // '\002'
			setSpeedMode(4);
			if (f2 < 800F + f5 + f6) {
				if ((this.actor instanceof TypeHasToKG) && ((TypeHasToKG) this.actor).isSalvo()) {
					int i = 0;
					float f10 = actor.collisionR() * 1.8F;
					i = (int) Math.toDegrees(Math.atan(f10 / 800F));
					i += World.Rnd().nextInt(-2, 2);
					if (i < 3) i = 3;
					AS.setSpreadAngle(i);
				}
				CT.WeaponControl[3] = true;
				setSpeedMode(6);
				AP.way.next();
				submaneuver++;
				sub_Man_Count = 0;
				break;
			}
			if (!ZutiSupportMethods.isStaticActor(actor)) break;
			float f8 = Property.floatValue(class1, "velocity", 20F);
			float f11 = Property.floatValue(class1, "traveltime", 100F);
			float f13 = f8 * f11 - 150F;
			if (f2 < f13 && World.land().isWater(this.actor.pos.getAbsPoint().x, this.actor.pos.getAbsPoint().y)) {
				CT.WeaponControl[3] = true;
				setSpeedMode(6);
				AP.way.next();
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 3: // '\003'
			setSpeedMode(9);
			sub_Man_Count++;
			if (sub_Man_Count > 150) {
				task = 3;
				push(0);
				push(8);
				pop();
				submaneuver = 0;
				sub_Man_Count = 0;
			}
			break;
		}
		Or.transformInv(Ve);
		if (submaneuver == 3) {
			if (sub_Man_Count < 20) Ve.set(1.0D, 0.090000003576278687D, 0.029999999329447746D);
			else Ve.set(1.0D, 0.090000003576278687D, 0.0099999997764825821D);
			if (!direc) Ve.y *= -1D;
		}
		Ve.normalize();
		turnToDirection(f);
	}

	private void groundAttackTorpedoToKG(Actor actor, float f) {
		float f2 = 50F;
		maxG = 5F;
		maxAOA = 8F;
		setSpeedMode(4);
		Class class1 = null;
		if (CT.Weapons[3][0] instanceof TorpedoGun) {
			TorpedoGun torpedogun = (TorpedoGun) CT.Weapons[3][0];
			class1 = (Class) Property.value(torpedogun.getClass(), "bulletClass", null);
		}
		smConstSpeed = 100F;
		if (class1 != null) {
			smConstSpeed = Property.floatValue(class1, "dropSpeed", 100F) / 3.7F;
			f2 = Property.floatValue(class1, "dropAltitude", 50F) + 10F;
		}
		minElevCoeff = 20F;
		Ve.set(actor.pos.getAbsPoint());
		Ve.sub(Loc);
		if (first) {
			Vtarg.set(actor.pos.getAbsPoint());
			submaneuver = 0;
		}
		if (submaneuver == 1 && sub_Man_Count == 0) {
			tmpV3f.set(actor.pos.getAbsPoint());
			tmpV3f.sub(Vtarg);
			if (tmpV3f.length() > 9.9999997473787516E-005D) tmpV3f.normalize();
			Vxy.set(tmpV3f.y * 3000D, -tmpV3f.x * 3000D, 0.0D);
			direc = Vxy.dot(Ve) > 0.0D;
			if (direc) Vxy.scale(-1D);
			Vtarg.add(Vxy);
			Vtarg.z = 80D;
		}
		if (submaneuver == 2) {
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.z = f2;
			if (Loc.z < 30D) Vtarg.z += 3D * (30D - Loc.z);
		}
		Ve.set(Vtarg);
		Ve.sub(Loc);
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
		Vf.set(Ve);
		Vpl.set(Vwld);
		Vpl.normalize();
		if (Alt < f2 - 5F) {
			if (Vwld.z < 0.0D) Vwld.z += (f2 - Alt) * 0.25F;
			if (Alt < 8F) set_maneuver(2);
			if (Alt < 20F && f1 < 75F) set_maneuver(2);
		} else if (Alt > f2 + 5F && submaneuver == 1 && Vwld.z > 0.0D) Vwld.z--;
		switch (submaneuver) {
		default:
			break;

		case 0: // '\0'
			sub_Man_Count++;
			if (sub_Man_Count > 60) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			addWindCorrection();
			break;

		case 1: // '\001'
			setSpeedMode(4);
			sub_Man_Count++;
			if (f1 < 4000F) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			addWindCorrection();
			break;

		case 2: // '\002'
			setSpeedMode(4);
			double d = Ve.angle(Vwld);
			float f3 = 180F - (Or.getYaw() - actor.pos.getAbsOrient().getYaw());
			if (f3 < -180F) f3 += 360F;
			else if (f3 > 180F) f3 -= 360F;
			float f4 = Property.floatValue(class1, "velocity", 20F);
			float f5 = Property.floatValue(class1, "traveltime", 100F);
			float f6 = f4 * f5 - 10F;
			if (f6 > 2700F) f6 = 2700F;
			double d1 = (Math.abs(f3) - 90F) * 8F;
			if (d1 < 0.0D) d1 = 0.0D;
			if (Skill == 2) d1 += 100D;
			if ((double) f1 < (double) f6 - d1 && f1 > 1800F && d < 0.20000000000000001D) {
				actor.getSpeed(tmpV3d);
				float f7 = 1.0F;
				if (Skill == 2) f7 = World.Rnd().nextFloat(0.8F, 1.2F);
				else if (Skill == 3) f7 = World.Rnd().nextFloat(0.9F, 1.1F);
				f7 = (float) ((double) f7 + 0.10000000000000001D);
				ToKGUtils.setTorpedoGyroAngle(this, f3, (float) (1.95D * tmpV3d.length()) * f7);
				if (((TypeHasToKG) this.actor).isSalvo()) {
					int i = 0;
					float f8 = actor.collisionR() * 1.8F;
					i = (int) Math.toDegrees(Math.atan((double) f8 / ((double) f6 - d1)));
					i += World.Rnd().nextInt(-2, 2);
					if (i < 1) i = 1;
					AS.setSpreadAngle(i);
				}
				CT.WeaponControl[3] = true;
				setSpeedMode(6);
				AP.way.next();
				push(8);
				submaneuver++;
				sub_Man_Count = 0;
				break;
			}
			if (f1 < 1500F) {
				ToKGUtils.setTorpedoGyroAngle(this, 0.0F, 0.0F);
				set_maneuver(51);
			}
			break;

		case 3: // '\003'
			setSpeedMode(9);
			push(8);
			pop();
			task = 61;
			sub_Man_Count++;
			boolean flag = false;
			for (int j = 0; j < CT.Weapons[3].length; j++)
				if (CT.Weapons[3][j] != null && !(CT.Weapons[3][j] instanceof BombGunNull) && CT.Weapons[3][j].countBullets() != 0) flag = true;

			if (sub_Man_Count > 800 || !flag) {
				task = 3;
				push(21);
				push(8);
				pop();
				submaneuver = 0;
				sub_Man_Count = 0;
			}
			break;
		}
		Or.transformInv(Ve);
		if (submaneuver == 3) {
			if (sub_Man_Count < 20) Ve.set(1.0D, 0.090000003576278687D, 0.029999999329447746D);
			else Ve.set(1.0D, 0.090000003576278687D, 0.0099999997764825821D);
			if (!direc) Ve.y *= -1D;
		}
		Ve.normalize();
		turnToDirection(f);
	}

	private void groundAttackCassette(Actor actor, float f) {
		maxG = 5F;
		maxAOA = 8F;
		setSpeedMode(4);
		smConstSpeed = 120F;
		minElevCoeff = 20F;
		Ve.set(actor.pos.getAbsPoint());
		Ve.sub(Loc);
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
		if (submaneuver == 3 && sub_Man_Count > 0 && sub_Man_Count < 45 && f1 > 200F) {
			tmpV3f.set(Vxy);
			float f4 = (float) tmpV3f.dot(Ve);
			tmpV3f.scale(-f4);
			tmpV3f.add(Ve);
			float f7 = (float) tmpV3f.length();
			float f6;
			if (f7 > 150F) f6 = 7.5F / f7;
			else f6 = 0.05F;
			tmpV3f.scale(f6);
			tmpV3f.z = 0.0D;
			Vwld.add(tmpV3f);
		}
		if (f1 <= 200F) sub_Man_Count = 50;
		if (first) {
			Vtarg.set(actor.pos.getAbsPoint());
			submaneuver = 0;
		}
		if (submaneuver == 1 && sub_Man_Count == 0) {
			tmpV3f.set(actor.pos.getAbsPoint());
			actor.getSpeed(tmpV3d);
			if (tmpV3d.length() < 0.5D) tmpV3d.set(Ve);
			tmpV3d.normalize();
			Vxy.set((float) tmpV3d.x, (float) tmpV3d.y, (float) tmpV3d.z);
			Vtarg.x -= tmpV3d.x * 3000D;
			Vtarg.y -= tmpV3d.y * 3000D;
			Vtarg.z += 250D;
		}
		if (submaneuver == 2 && sub_Man_Count == 0) {
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.x -= Vxy.x * 1000D;
			Vtarg.y -= Vxy.y * 1000D;
			Vtarg.z += 50D;
		}
		if (submaneuver == 3 && sub_Man_Count == 0) {
			checkGround = false;
			Vtarg.set(actor.pos.getAbsPoint());
			Vtarg.x += Vxy.x * 1000D;
			Vtarg.y += Vxy.y * 1000D;
			Vtarg.z += 50D;
		}
		Ve.set(Vtarg);
		Ve.sub(Loc);
		addWindCorrection();
		float f2 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y + Ve.z * Ve.z);
		Vf.set(Ve);
		Or.transformInv(Ve);
		Ve.normalize();
		Vpl.set(Vwld);
		Vpl.normalize();
		if (Alt < 10F) {
			push(0);
			push(2);
			pop();
		}
		switch (submaneuver) {
		case 0: // '\0'
			setSpeedMode(9);
			sub_Man_Count++;
			if (sub_Man_Count > 60) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 1: // '\001'
			setSpeedMode(9);
			sub_Man_Count++;
			if (f2 < 1000F) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 2: // '\002'
			setSpeedMode(6);
			sub_Man_Count++;
			if (f2 < 155F) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			if (sub_Man_Count > 320) {
				push(0);
				push(10);
				pop();
			}
			break;

		case 3: // '\003'
			setSpeedMode(6);
			sub_Man_Count++;
			Vwld.z *= 0.80000001192092896D;
			Or.transformInv(Vwld);
			Vwld.y *= 0.89999997615814209D;
			Or.transform(Vwld);
			float f5 = sub_Man_Count;
			if (f5 < 100F) f5 = 100F;
			if (Alt > 45F) Vwld.z -= 0.002F * (Alt - 45F) * f5;
			else Vwld.z -= 0.005F * (Alt - 45F) * f5;
			if (Alt < 0.0F) Alt = 0.0F;
			if (f2 < 1080F + getSpeed() * (float) Math.sqrt((2.0F * Alt) / 9.81F)) bombsOut = true;
			if (Ve.x < 0.0D || f2 < 350F || sub_Man_Count > 160) {
				push(0);
				push(10);
				push(10);
				pop();
			}
			break;
		}
		if (submaneuver == 0) Ve.set(1.0D, 0.0D, 0.0D);
		turnToDirection(f);
	}

	public void goodFighterVsFighter(float f) {
		Ve.sub(target.Loc, Loc);
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
		Or.transformInv(Ve);
		float f2 = (float) Ve.length();
		float f4 = 1.0F / f2;
		Vtarg.set(Ve);
		Vtarg.scale(f4);
		float f5 = (Energy - target.Energy) * 0.1019F;
		tmpV3f.sub(target.Vwld, Vwld);
		if (sub_Man_Count == 0) {
			float f6 = 0.0F;
			if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0) f6 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			else if (CT.Weapons[0] != null) f6 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
			if (f6 > 0.01F) bullTime = 1.0F / f6;
			submanDelay = 0;
		}
		if (f1 < 1500F) {
			float f7 = 0.0F;
			float f9 = 0.0F;
			if (Vtarg.x > -0.20000000298023224D) {
				f7 = 3F * ((float) Vtarg.x + 0.2F);
				Vxy.set(tmpV3f);
				Vxy.scale(1.0D);
				Or.transformInv(Vxy);
				Vxy.add(Ve);
				Vxy.normalize();
				f9 = 10F * (float) (Vxy.x - Vtarg.x);
				if (f9 < -1F) f9 = -1F;
				if (f9 > 1.0F) f9 = 1.0F;
			} else {
				f7 = 3F * ((float) Vtarg.x + 0.2F);
			}
			if (submaneuver == 4 && Vtarg.x < 0.93000000715255737D && (double) f2 < 300D) {
				if (f5 > 500F) submaneuver = 5;
				else submaneuver = 1;
				submanDelay = 30;
			}
			if (submaneuver != 4 && (double) f5 > 300D && Vtarg.x > 0.75D) {
				submaneuver = 4;
				submanDelay = 240;
			}
			float f11 = 0.0015F * f5 + 0.0006F * f1 + f7 + 0.5F * f9;
			if (f11 > 0.9F && submanDelay == 0) {
				if (Vtarg.x > 0.5D || (double) f1 * 2D < (double) f2) {
					submaneuver = 4;
					submanDelay = 240;
				} else {
					submaneuver = 3;
					submanDelay = 120;
				}
			} else if (f5 > 800F && submaneuver == 0 || f5 > 1000F) {
				Ve.set(0.0D, 0.0D, 800D);
				if (submanDelay == 0) {
					submaneuver = 0;
					submanDelay = 30;
				}
			} else if (f2 > 450F && submaneuver == 2 || f2 > 600F) {
				if (submanDelay == 0) {
					submaneuver = 2;
					submanDelay = 60;
				}
			} else if (submanDelay == 0) {
				submaneuver = 1;
				submanDelay = 30;
			}
		} else if (f1 < 3000F) {
			if (Vtarg.x < 0.5D) {
				if (submanDelay == 0) {
					submaneuver = 3;
					submanDelay = 120;
				}
			} else if (f5 > 600F && submaneuver == 0 || f5 > 800F) {
				Ve.set(0.0D, 0.0D, 800D);
				if (submanDelay == 0) {
					submaneuver = 0;
					submanDelay = 120;
				}
			} else if (f5 > -200F && submaneuver >= 4 || f5 > -100F) {
				if (submanDelay == 0) {
					submaneuver = 4;
					submanDelay = 120;
				}
			} else {
				Ve.set(0.0D, 0.0D, Loc.z);
				if (submanDelay == 0) {
					submaneuver = 0;
					submanDelay = 120;
				}
			}
		} else {
			Ve.set(0.0D, 0.0D, 1000D);
			if (submanDelay == 0) {
				submaneuver = 0;
				submanDelay = 180;
			}
		}
		switch (submaneuver) {
		case 0: // '\0'
			target.Or.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			tmpV3f.set(Ve);
			tmpV3f.z = 0.0D;
			float f3 = (float) tmpV3f.length();
			tmpV3f.normalize();
			Vtarg.set(target.Vwld);
			Vtarg.z = 0.0D;
			float f8 = (float) tmpV3f.dot(Vtarg);
			float f10 = getSpeed() - f8;
			if (f10 < 10F) f10 = 10F;
			float f12 = f3 / f10;
			if (f12 < 0.0F) f12 = 0.0F;
			tmpV3f.scale(Vtarg, f12);
			Ve.add(tmpV3f);
			Or.transformInv(Ve);
			Ve.normalize();
			setSpeedMode(9);
			farTurnToDirection();
			sub_Man_Count++;
			break;

		case 1: // '\001'
			setSpeedMode(11);
			CT.AileronControl = -0.04F * Or.getKren();
			CT.ElevatorControl = -0.04F * (Or.getTangage() + 5F);
			CT.RudderControl = 0.0F;
			break;

		case 2: // '\002'
			setSpeedMode(9);
			tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
			float f13 = 6F;
			if (Or.getKren() > 0.0F) Ve.set(100D, -f13, 10D);
			else Ve.set(100D, f13, 10D);
			tmpOr.transform(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			farTurnToDirection();
			break;

		case 3: // '\003'
			minElevCoeff = 20F;
			setSpeedMode(9);
			tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
			Ve.sub(target.Loc, Loc);
			Ve.z = 0.0D;
			Ve.normalize();
			Ve.z = 0.40000000000000002D;
			Or.transformInv(Ve);
			Ve.normalize();
			attackTurnToDirection(1000F, f, 15F);
			break;

		case 4: // '\004'
			minElevCoeff = 20F;
			boomAttack(f);
			setSpeedMode(11);
			break;

		case 5: // '\005'
			minElevCoeff = 20F;
			setSpeedMode(11);
			tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
			Ve.sub(target.Loc, Loc);
			Ve.z = 0.0D;
			Ve.normalize();
			Ve.z = 0.94999999999999996D;
			Or.transformInv(Ve);
			Ve.normalize();
			attackTurnToDirection(1000F, f, 15F);
			break;

		default:
			minElevCoeff = 20F;
			fighterVsFighter(f);
			break;
		}
		if (submanDelay > 0) submanDelay--;
	}

	public void bNZFighterVsFighter(float f) {
		Ve.sub(target.Loc, Loc);
		Or.transformInv(Ve);
		dist = (float) Ve.length();
		float f1 = (float) Math.sqrt(Ve.x * Ve.x + Ve.y * Ve.y);
		float f2 = 1.0F / dist;
		Vtarg.set(Ve);
		Vtarg.scale(f2);
		float f3 = (Energy - target.Energy) * 0.1019F;
		tmpV3f.sub(target.Vwld, Vwld);
		if (sub_Man_Count == 0) {
			float f4 = 0.0F;
			if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0) f4 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			else if (CT.Weapons[0] != null) f4 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
			if (f4 > 0.01F) bullTime = 1.0F / f4;
			submanDelay = 0;
		}
		if (f1 < 1500F) {
			float f5 = 0.0F;
			float f7 = 0.0F;
			if (Vtarg.x > -0.20000000298023224D) {
				f5 = 3F * ((float) Vtarg.x + 0.2F);
				Vxy.set(tmpV3f);
				Vxy.scale(1.0D);
				Or.transformInv(Vxy);
				Vxy.add(Ve);
				Vxy.normalize();
				f7 = 20F * (float) (Vxy.x - Vtarg.x);
				if (f7 < -1F) f7 = -1F;
				if (f7 > 1.0F) f7 = 1.0F;
			}
			float f9 = f3 * 0.0015F + f1 * 0.0006F + f5 + f7;
			if (f9 > 0.8F && submaneuver >= 3 || f9 > 1.2F) {
				if (tmpV3f.length() < 100D) {
					if (submanDelay == 0) {
						submaneuver = 4;
						submanDelay = 120;
					}
				} else if (submanDelay == 0) {
					submaneuver = 3;
					submanDelay = 120;
				}
			} else if (f3 > 800F && submaneuver == 0 || f3 > 1000F) {
				Ve.set(0.0D, 0.0D, 800D);
				if (submanDelay == 0) {
					submaneuver = 0;
					submanDelay = 30;
				}
			} else if (dist > 450F && submaneuver == 2 || dist > 600F) {
				if (submanDelay == 0) {
					submaneuver = 2;
					submanDelay = 60;
				}
			} else if (submanDelay == 0) {
				submaneuver = 1;
				submanDelay = 30;
			}
		} else if (f1 < 3000F) {
			if (f3 > 600F && submaneuver == 0 || f3 > 800F) {
				Ve.set(0.0D, 0.0D, 800D);
				if (submanDelay == 0) {
					submaneuver = 0;
					submanDelay = 120;
				}
			} else if (f3 > -200F && submaneuver >= 3 || f3 > -100F) {
				if (submanDelay == 0) {
					submaneuver = 3;
					submanDelay = 120;
				}
			} else {
				Ve.set(0.0D, 0.0D, Loc.z);
				if (submanDelay == 0) {
					submaneuver = 0;
					submanDelay = 120;
				}
			}
		} else {
			Ve.set(0.0D, 0.0D, 1000D);
			if (submanDelay == 0) {
				submaneuver = 0;
				submanDelay = 180;
			}
		}
		switch (submaneuver) {
		case 0: // '\0'
			target.Or.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			tmpV3f.set(Ve);
			tmpV3f.z = 0.0D;
			dist = (float) tmpV3f.length();
			tmpV3f.normalize();
			Vtarg.set(target.Vwld);
			Vtarg.z = 0.0D;
			float f6 = (float) tmpV3f.dot(Vtarg);
			float f8 = getSpeed() - f6;
			if (f8 < 10F) f8 = 10F;
			float f10 = dist / f8;
			if (f10 < 0.0F) f10 = 0.0F;
			tmpV3f.scale(Vtarg, f10);
			Ve.add(tmpV3f);
			Or.transformInv(Ve);
			Ve.normalize();
			setSpeedMode(9);
			farTurnToDirection();
			sub_Man_Count++;
			break;

		case 1: // '\001'
			setSpeedMode(11);
			CT.AileronControl = -0.04F * Or.getKren();
			CT.ElevatorControl = -0.04F * (Or.getTangage() + 5F);
			CT.RudderControl = 0.0F;
			break;

		case 2: // '\002'
			setSpeedMode(9);
			tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
			if (Or.getKren() > 0.0F) Ve.set(100D, -6D, 10D);
			else Ve.set(100D, 6D, 10D);
			tmpOr.transform(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			farTurnToDirection();
			break;

		case 3: // '\003'
			minElevCoeff = 20F;
			fighterVsFighter(f);
			setSpeedMode(6);
			break;

		default:
			minElevCoeff = 20F;
			fighterVsFighter(f);
			break;
		}
		if (submanDelay > 0) submanDelay--;
	}

	public void setBomberAttackType(int i) {
		float f;
		if (target != null && Actor.isValid(target.actor)) f = target.actor.collisionR();
		else f = 15F;
		setRandomTargDeviation(0.8F);
		switch (i) {
		case 0: // '\0'
			ApproachV.set(-300F + World.Rnd().nextFloat(-100F, 100F), 0.0D, 600F + World.Rnd().nextFloat(-100F, 100F));
			TargV.set(0.0D, 0.0D, 0.0D);
			ApproachR = 150F;
			TargY = 2.1F * f;
			TargZ = 3.9F * f;
			TargYS = 0.43F * f;
			TargZS = 0.43F * f;
			break;

		case 1: // '\001'
			ApproachV.set(-300F + World.Rnd().nextFloat(-100F, 100F), 0.0D, 600F + World.Rnd().nextFloat(-100F, 100F));
			TargV.set(target.EI.engines[World.Rnd().nextInt(0, target.EI.getNum() - 1)].getEnginePos());
			TargV.x--;
			ApproachR = 150F;
			TargY = 2.1F * f;
			TargZ = 3.9F * f;
			TargYS = 0.43F * f;
			TargZS = 0.43F * f;
			break;

		case 2: // '\002'
			if (actor instanceof ME_163B1A) ApproachV.set(-1200F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
			else ApproachV.set(-600F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
			TargV.set(target.EI.engines[World.Rnd().nextInt(0, target.EI.getNum() - 1)].getEnginePos());
			TargV.x--;
			ApproachR = 300F;
			TargY = 2.1F * f;
			TargZ = 3.9F * f;
			TargYS = 0.43F * f;
			TargZS = 0.43F * f;
			break;

		case 3: // '\003'
			ApproachV.set(2600D, 0.0D, 300F + World.Rnd().nextFloat(-100F, 100F));
			TargV.set(0.0D, 0.0D, 0.0D);
			ApproachR = 800F;
			TargY = 25F;
			TargZ = 15F;
			TargYS = 3F;
			TargZS = 3F;
			break;

		case 4: // '\004'
			ApproachV.set(-400F + World.Rnd().nextFloat(-100F, 100F), 0.0D, -200F + World.Rnd().nextFloat(-50F, 50F));
			TargV.set(0.0D, 0.0D, 0.0D);
			ApproachR = 300F;
			TargY = 2.1F * f;
			TargZ = 1.3F * f;
			TargYS = 0.26F * f;
			TargZS = 0.26F * f;
			break;

		case 5: // '\005'
			ApproachV.set(0.0D, 0.0D, 0.0D);
			TargV.set(0.0D, 0.0D, 0.0D);
			ApproachR = 600F;
			TargY = 25F;
			TargZ = 12F;
			TargYS = 0.26F * f;
			TargZS = 0.26F * f;
			break;

		case 6: // '\006'
			ApproachV.set(600D, (float) (600 - World.Rnd().nextInt(0, 1) * 1200) + World.Rnd().nextFloat(-100F, 100F), 300D);
			TargV.set(0.0D, 0.0D, 0.0D);
			ApproachR = 300F;
			TargY = 2.1F * f;
			TargZ = 1.2F * f;
			TargYS = 0.26F * f;
			TargZS = 0.26F * f;
			break;

		case 7: // '\007'
			if (actor instanceof ME_163B1A) ApproachV.set(-1500F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
			else ApproachV.set(-1000F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
			TargV.set(target.EI.engines[World.Rnd().nextInt(0, target.EI.getNum() - 1)].getEnginePos());
			ApproachR = 200F;
			TargY = 10F;
			TargZ = 10F;
			TargYS = 2.0F;
			TargZS = 2.0F;
			break;

		case 8: // '\b'
			ApproachV.set(-1000F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
			TargV.set(0.0D, 0.0D, 0.0D);
			ApproachR = 200F;
			TargY = 2.1F * f;
			TargZ = 3.9F * f;
			TargYS = 0.2F * f;
			TargZS = 0.2F * f;
			break;

		case 9: // '\t'
			ApproachV.set(-600F + World.Rnd().nextFloat(-100F, 100F), 0.0D, 600F + World.Rnd().nextFloat(-100F, 100F));
			TargV.set(0.0D, 0.0D, 0.0D);
			ApproachR = 300F;
			TargY = 2.1F * f;
			TargZ = 3.9F * f;
			TargYS = 0.2F * f;
			TargZS = 0.2F * f;
			break;

		case 10: // '\n'
			ApproachV.set(-600F + World.Rnd().nextFloat(-100F, 100F), 0.0D, -300F + World.Rnd().nextFloat(-100F, 100F));
			TargV.set(((TypeJazzPlayer) actor).getAttackVector());
			ApproachR = 300F;
			TargY = 2.1F * f;
			TargZ = 3.9F * f;
			TargYS = 0.43F * f;
			TargZS = 0.43F * f;
			break;

		default:
			ApproachV.set(-1000F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
			TargV.set(target.EI.engines[World.Rnd().nextInt(0, target.EI.getNum() - 1)].getEnginePos());
			ApproachR = 200F;
			TargY = 10F;
			TargZ = 10F;
			TargYS = 2.0F;
			TargZS = 2.0F;
			break;
		}
		float f1 = 0.0F;
		if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0) f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
		else if (CT.Weapons[0] != null) f1 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
		if (f1 > 0.01F) bullTime = 1.0F / f1;
	}

	public void fighterVsBomber(float f) {
		maxAOA = 15F;
		tmpOr.setAT0(target.Vwld);
		switch (submaneuver) {
		case 0: // '\0'
			minElevCoeff = 4F;
			rocketsDelay = 0;
			bulletDelay = 0;
			double d = 0.0001D * target.Loc.z;
			Ve.sub(target.Loc, Loc);
			tmpOr.transformInv(Ve);
			scaledApproachV.set(ApproachV);
			scaledApproachV.x -= 700D * d;
			scaledApproachV.z += 500D * d;
			Ve.add(scaledApproachV);
			Ve.normalize();
			tmpV3f.scale(scaledApproachV, -1D);
			tmpV3f.normalize();
			double d1 = Ve.dot(tmpV3f);
			Ve.set(Vwld);
			Ve.normalize();
			tmpV3f.set(target.Vwld);
			tmpV3f.normalize();
			double d2 = Ve.dot(tmpV3f);
			Ve.set(scaledApproachV);
			Ve.x += 60D * (2D * (1.0D - d1) + 4D * (1.0D - d2));
			tmpOr.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			double d3 = Ve.z;
			tmpV3f.set(Ve);
			tmpV3f.z = 0.0D;
			float f1 = (float) tmpV3f.length();
			float f4 = 0.55F + 0.15F * (float) Skill;
			tmpV3f.normalize();
			Vtarg.set(target.Vwld);
			Vtarg.z = 0.0D;
			float f7 = (float) tmpV3f.dot(Vtarg);
			float f8 = getSpeed() - f7;
			if (f8 < 10F) f8 = 10F;
			float f9 = f1 / f8;
			if (f9 < 0.0F) f9 = 0.0F;
			tmpV3f.scale(Vtarg, f9 * f4);
			Ve.add(tmpV3f);
			Or.transformInv(Ve);
			Ve.normalize();
			if (d3 > -200D) {
				setSpeedMode(9);
			} else {
				setSpeedMode(4);
				smConstSpeed = 180F;
			}
			attackTurnToDirection(f1, f, 10F * (float) (1.0D + d));
			sub_Man_Count++;
			if ((double) f1 < (double) ApproachR * (1.0D + d) && d3 < 200D) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 1: // '\001'
			minElevCoeff = 20F;
			Ve.set(TargV);
			target.Or.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			float f2 = (float) Ve.length();
			float f5 = 0.55F + 0.15F * (float) Skill;
			tmpV3f.sub(target.Vwld, Vwld);
			float f10 = f2 * bullTime * 0.0025F;
			if (f10 > 0.05F) f10 = 0.05F;
			tmpV3f.scale(f2 * f10 * f5);
			Ve.add(tmpV3f);
			Vtarg.set(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
			if (f2 > 3200F || Vtarg.z > 1500D) {
				submaneuver = 0;
				sub_Man_Count = 0;
				set_maneuver(0);
				break;
			}
			if (Ve.x < 0.30000001192092896D) {
				Vtarg.z += (double) (0.012F * (float) Skill * (800F + f2)) * (0.30000001192092896D - Ve.x);
				Ve.set(Vtarg);
				Or.transformInv(Ve);
				Ve.normalize();
			}
			attackTurn(f2, f, 10F + (float) Skill * 8F);
			setSpeedMode(4);
			smConstSpeed = 180F;
			if (f2 < 400F) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 2: // '\002'
			minElevCoeff = 20F;
			if (rocketsDelay > 0) rocketsDelay--;
			if (bulletDelay > 0) bulletDelay--;
			if (bulletDelay == 120) bulletDelay = 0;
			setRandomTargDeviation(0.8F);
			Ve.set(TargV);
			Ve.add(TargDevV);
			target.Or.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			Vtarg.set(Ve);
			float f3 = (float) Ve.length();
			float f6 = 0.55F + 0.15F * (float) Skill;
			float f11 = 0.0002F * (float) Skill;
			tmpV3f.sub(target.Vwld, Vwld);
			Vpl.set(tmpV3f);
			tmpV3f.scale(f3 * (bullTime + f11) * f6);
			Ve.add(tmpV3f);
			tmpV3f.set(Vpl);
			tmpV3f.scale(f3 * bullTime * f6);
			Vtarg.add(tmpV3f);
			if (f3 > 4000F || Vtarg.z > 2000D) {
				submaneuver = 0;
				sub_Man_Count = 0;
				set_maneuver(0);
				break;
			}
			Or.transformInv(Vtarg);
			if (Vtarg.x > 0.0D && f3 < 500F && (shotAtFriend <= 0 || distToFriend > f3) && Math.abs(Vtarg.y) < (double) (TargY - TargYS * (float) Skill) && Math.abs(Vtarg.z) < (double) (TargZ - TargZS * (float) Skill) && bulletDelay < 120) {
				if (!(actor instanceof TypeJazzPlayer)) CT.WeaponControl[1] = true;
				CT.WeaponControl[0] = true;
				bulletDelay += 2;
				if (bulletDelay >= 118) {
					int i = (int) (target.getW().length() * 150D);
					if (i > 60) i = 60;
					bulletDelay += World.Rnd().nextInt(i * Skill, 2 * i * Skill);
				}
				if (CT.Weapons[2] != null && CT.Weapons[2][0] != null && CT.Weapons[2][CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1) {
					tmpV3f.sub(target.Vwld, Vwld);
					Or.transformInv(tmpV3f);
					if (Math.abs(tmpV3f.y) < (double) (TargY - TargYS * (float) Skill) && Math.abs(tmpV3f.z) < (double) (TargZ - TargZS * (float) Skill)) CT.WeaponControl[2] = true;
					Voice.speakAttackByRockets((Aircraft) actor);
					rocketsDelay += 120;
				}
				((Maneuver) target).incDangerAggressiveness(1, 0.8F, f3, this);
				((Pilot) target).setAsDanger(actor);
			}
			Vtarg.set(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f3, this);
			if (Ve.x < 0.30000001192092896D) {
				Vtarg.z += (double) (0.01F * (float) Skill * (800F + f3)) * (0.30000001192092896D - Ve.x);
				Ve.set(Vtarg);
				Or.transformInv(Ve);
				Ve.normalize();
			}
			attackTurn(f3, f, 10F + (float) Skill * 8F);
			if (target.getSpeed() > 70F) {
				setSpeedMode(2);
				tailForStaying = target;
				tailOffset.set(-20D, 0.0D, 0.0D);
			} else {
				setSpeedMode(4);
				smConstSpeed = 70F;
			}
			if (strikeEmer) {
				Vpl.sub(strikeTarg.Loc, Loc);
				tmpV3f.set(Vpl);
				Or.transformInv(tmpV3f);
				if (tmpV3f.x < 0.0D) return;
				tmpV3f.sub(strikeTarg.Vwld, Vwld);
				tmpV3f.scale(0.69999998807907104D);
				Vpl.add(tmpV3f);
				Or.transformInv(Vpl);
				push();
				if (Vpl.z < 0.0D) {
					push(0);
					push(8);
					push(60);
				} else {
					push(0);
					push(8);
					push(61);
				}
				pop();
				strikeEmer = false;
				submaneuver = 0;
				sub_Man_Count = 0;
			}
			if (sub_Man_Count > 600) {
				push(8);
				pop();
			}
			break;

		default:
			submaneuver = 0;
			sub_Man_Count = 0;
			set_maneuver(0);
			break;
		}
	}

	public void fighterUnderBomber(float f) {
		maxAOA = 15F;
		switch (submaneuver) {
		case 0: // '\0'
			rocketsDelay = 0;
			bulletDelay = 0;
			Ve.set(ApproachV);
			target.Or.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			tmpV3f.set(Ve);
			tmpV3f.z = 0.0D;
			float f1 = (float) tmpV3f.length();
			float f4 = 0.55F + 0.15F * (float) Skill;
			tmpV3f.normalize();
			Vtarg.set(target.Vwld);
			Vtarg.z = 0.0D;
			float f7 = (float) tmpV3f.dot(Vtarg);
			float f8 = getSpeed() - f7;
			if (f8 < 10F) f8 = 10F;
			float f9 = f1 / f8;
			if (f9 < 0.0F) f9 = 0.0F;
			tmpV3f.scale(Vtarg, f9 * f4);
			Ve.add(tmpV3f);
			Or.transformInv(Ve);
			Ve.normalize();
			setSpeedMode(4);
			smConstSpeed = 140F;
			farTurnToDirection();
			sub_Man_Count++;
			if (f1 < ApproachR) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 1: // '\001'
			Ve.set(TargV);
			target.Or.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			float f2 = (float) Ve.length();
			float f5 = 0.55F + 0.15F * (float) Skill;
			tmpV3f.sub(target.Vwld, Vwld);
			float f10 = f2 * bullTime * 0.0025F;
			if (f10 > 0.02F) f10 = 0.02F;
			tmpV3f.scale(f2 * f10 * f5);
			Ve.add(tmpV3f);
			Vtarg.set(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
			if (f2 > 3200F || Vtarg.z > 1500D) {
				submaneuver = 0;
				sub_Man_Count = 0;
				break;
			}
			if (Ve.x < 0.30000001192092896D) {
				Vtarg.z += (double) (0.01F * (float) Skill * (800F + f2)) * (0.30000001192092896D - Ve.x);
				Ve.set(Vtarg);
				Or.transformInv(Ve);
				Ve.normalize();
			}
			attackTurn(f2, f, 10F);
			if (target.getSpeed() > 120F) {
				setSpeedMode(2);
				tailForStaying = target;
			} else {
				setSpeedMode(4);
				smConstSpeed = 120F;
			}
			if (f2 < 600F) {
				tailForStaying = target;
				setSpeedMode(1);
				tailOffset.set(-190D, 0.0D, 0.0D);
			}
			if (f2 < 400F) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 2: // '\002'
			setCheckStrike(false);
			if (rocketsDelay > 0) rocketsDelay--;
			if (bulletDelay > 0) bulletDelay--;
			if (bulletDelay == 120) bulletDelay = 0;
			setRandomTargDeviation(0.8F);
			Ve.set(TargV);
			Ve.add(TargDevV);
			target.Or.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			float f3 = (float) Ve.length();
			float f6 = 0.55F + 0.15F * (float) Skill;
			tmpV3f.sub(target.Vwld, Vwld);
			tmpV3f.scale(f3 * bullTime * f6);
			Ve.add(tmpV3f);
			Vtarg.set(Ve);
			Or.transformInv(Ve);
			if (f3 > 4000F || Vtarg.z > 2000D) {
				submaneuver = 0;
				sub_Man_Count = 0;
				break;
			}
			if (f3 < 330F && Math.abs(Or.getKren()) < 3F) if (actor instanceof TypeJazzPlayer) {
				if (((TypeJazzPlayer) actor).hasSlantedWeaponBullets()) CT.WeaponControl[1] = true;
			} else {
				CT.WeaponControl[0] = true;
				CT.WeaponControl[1] = true;
			}
			Ve.normalize();
			if (Ve.x < 0.30000001192092896D) {
				Vtarg.z += (double) (0.01F * (float) Skill * (800F + f3)) * (0.30000001192092896D - Ve.x);
				Ve.set(Vtarg);
				Or.transformInv(Ve);
				Ve.normalize();
			}
			attackTurn(f3, f, 6F + (float) Skill * 3F);
			setSpeedMode(1);
			tailForStaying = target;
			tailOffset.set(-190D, 0.0D, 0.0D);
			if (sub_Man_Count > 10000 || f3 < 240F) {
				push(9);
				pop();
			}
			break;

		default:
			submaneuver = 0;
			sub_Man_Count = 0;
			break;
		}
	}

	public void setWingmanAttackType(int i) {
		wAttType = i;
		TargV.set(0.0D, 0.0D, 0.0D);
		setRandomTargDeviation(8F);
		ApproachR = 500F;
		switch (i) {
		case 1: // '\001'
			ApproachV.set(-300F + World.Rnd().nextFloat(-100F, 100F), 0.0D, 600F + World.Rnd().nextFloat(-100F, 100F));
			break;

		case 2: // '\002'
			ApproachV.set(1000D, 0.0D, 300F + World.Rnd().nextFloat(-100F, 100F));
			break;

		case 3: // '\003'
			ApproachV.set(-400F + World.Rnd().nextFloat(-100F, 100F), 0.0D, -200F + World.Rnd().nextFloat(-50F, 50F));
			break;

		case 4: // '\004'
			ApproachV.set(0.0D, 0.0D, 0.0D);
			break;

		case 5: // '\005'
			ApproachV.set(-1000F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
			break;

		case 6: // '\006'
			ApproachV.set(-600F + World.Rnd().nextFloat(-100F, 100F), 0.0D, 200F + World.Rnd().nextFloat(-100F, 100F));
			break;

		case 7: // '\007'
			ApproachV.set(-400F + World.Rnd().nextFloat(-100F, 100F), 0.0D, -300F + World.Rnd().nextFloat(-100F, 100F));
			if (actor instanceof TypeJazzPlayer) TargV.set(((TypeJazzPlayer) actor).getAttackVector());
			else TargV.set(100D, 0.0D, -400D);
			break;

		case 8: // '\b'
			ApproachV.set(900D, World.Rnd().nextFloat(500F, 700F), -300D);
			break;

		case 9: // '\t'
			ApproachV.set(900D, World.Rnd().nextFloat(-500F, -700F), -300D);
			break;

		default:
			ApproachV.set(-1000F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
			break;
		}
		float f = 0.0F;
		if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0) f = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
		else if (CT.Weapons[0] != null) f = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
		if (f > 0.01F) bullTime = 1.0F / f;
	}

	public void wingmanVsFighter(float f) {
		maxAOA = 20F;
		tmpOr.setAT0(target.Vwld);
		switch (submaneuver) {
		case 0: // '\0'
			minElevCoeff = 4F;
			rocketsDelay = 0;
			bulletDelay = 0;
			double d = 0.0001D * target.Loc.z;
			Ve.sub(target.Loc, Loc);
			tmpOr.transformInv(Ve);
			scaledApproachV.set(ApproachV);
			scaledApproachV.x -= 700D * d;
			scaledApproachV.z += 500D * d;
			Ve.add(scaledApproachV);
			Ve.normalize();
			tmpV3f.scale(scaledApproachV, -1D);
			tmpV3f.normalize();
			double d1 = Ve.dot(tmpV3f);
			Ve.set(Vwld);
			Ve.normalize();
			tmpV3f.set(target.Vwld);
			tmpV3f.normalize();
			double d2 = Ve.dot(tmpV3f);
			Ve.set(scaledApproachV);
			Ve.x += 60D * (2D * (1.0D - d1) + 4D * (1.0D - d2));
			tmpOr.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			double d3 = Ve.z;
			tmpV3f.set(Ve);
			tmpV3f.z = 0.0D;
			float f1 = (float) tmpV3f.length();
			float f3 = 0.55F + 0.15F * (float) Skill;
			tmpV3f.normalize();
			Vtarg.set(target.Vwld);
			Vtarg.z = 0.0D;
			float f5 = (float) tmpV3f.dot(Vtarg);
			float f6 = getSpeed() - f5;
			if (f6 < 10F) f6 = 10F;
			float f7 = f1 / f6;
			if (f7 < 0.0F) f7 = 0.0F;
			tmpV3f.scale(Vtarg, f7 * f3);
			Ve.add(tmpV3f);
			Or.transformInv(Ve);
			Ve.normalize();
			setSpeedMode(9);
			attackTurnToDirection(f1, f, 10F * (float) (1.0D + d));
			sub_Man_Count++;
			if ((double) f1 < (double) ApproachR * (1.0D + d) && d3 < 200D) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 1: // '\001'
			minElevCoeff = 20F;
			Ve.set(TargV);
			target.Or.transform(Ve);
			Ve.add(target.Loc);
			Ve.sub(Loc);
			float f2 = (float) Ve.length();
			float f4 = 0.55F + 0.15F * (float) Skill;
			tmpV3f.sub(target.Vwld, Vwld);
			float f8 = f2 * bullTime * 0.0025F;
			if (f8 > 0.05F) f8 = 0.05F;
			tmpV3f.scale(f2 * f8 * f4);
			Ve.add(tmpV3f);
			Vtarg.set(Ve);
			Or.transformInv(Ve);
			Ve.normalize();
			((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f2, this);
			if (f2 > 3200F || Vtarg.z > 1500D) {
				submaneuver = 0;
				sub_Man_Count = 0;
				set_maneuver(0);
				break;
			}
			if (Ve.x < 0.30000001192092896D) {
				Vtarg.z += (double) (0.012F * (float) Skill * (800F + f2)) * (0.30000001192092896D - Ve.x);
				Ve.set(Vtarg);
				Or.transformInv(Ve);
				Ve.normalize();
			}
			attackTurnToDirection(f2, f, 10F + (float) Skill * 8F);
			setSpeedMode(9);
			if (f2 < 800F) {
				submaneuver++;
				sub_Man_Count = 0;
			}
			break;

		case 2: // '\002'
			set_maneuver(27);
			break;

		default:
			submaneuver = 0;
			sub_Man_Count = 0;
			set_maneuver(0);
			break;
		}
	}

	public boolean friendCheck(float f, Point3d point3d) {
		Aircraft aircraft = War.getNearestFriendAtPoint(point3d, (Aircraft) actor, f - 10F);
		if (aircraft != null && (((Aircraft) aircraft).FM instanceof RealFlightModel)) {
			tmpV3d.sub(target.Loc, Loc);
			Or.transformInv(tmpV3d);
			tmpV3d.normalize();
			if (tmpV3d.x > 0.80000001192092896D) {
				push();
				set_maneuver(14);
				return false;
			}
		}
		return true;
	}

	public void fighterVsFighter(float f) {
		if (sub_Man_Count == 0) {
			float f1 = 0.0F;
			if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0) f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			else if (CT.Weapons[0] != null) f1 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
			if (f1 > 0.01F) bullTime = 1.0F / (f1 + getSpeed());
			followType = World.cur().rnd.nextInt(0, 2);
		}
		maxAOA = 15F;
		if (rocketsDelay > 0) rocketsDelay--;
		if (bulletDelay > 0) bulletDelay--;
		if (bulletDelay == 122) bulletDelay = 0;
		losVector.set(1.0D, 0.0D, 0.0D);
		Or.transform(losVector);
		if (dist < 700F && VisCheck.checkLeadShotBlock((Aircraft) actor, (Aircraft) target.actor)) {
			if (lastKnownTargetLoc.x == -1D) {
				lastKnownTargetLoc.set(target.Loc);
				scaledApproachV.set(target.Vwld);
			}
			Ve.sub(lastKnownTargetLoc, Loc);
			float f2 = (float) (3 - Skill) * 0.2F;
			float f4 = World.Rnd().nextFloat(-f2, f2);
			lastKnownTargetLoc.x += (scaledApproachV.x + (double) f4) * (double) Time.tickConstLenFs();
			lastKnownTargetLoc.y += (scaledApproachV.y + (double) f4) * (double) Time.tickConstLenFs();
			lastKnownTargetLoc.z += (scaledApproachV.z + (double) f4) * (double) Time.tickConstLenFs();
		} else {
			lastKnownTargetLoc.x = -1D;
			Ve.sub(target.Loc, Loc);
		}
		float f3 = (float) Ve.length();
		float f5 = 1.0F + 0.15F * sp;
		float f6 = 0.00053F * (1.0F + AOA * 0.007F);
		tmpV3f.sub(target.Vwld, Vwld);
		Vpl.set(tmpV3f);
		tmpV3f.scale(f3 * (bullTime + f6) * f5);
		Ve.add(tmpV3f);
		losVector.scale(Ve.length());
		corrVector.sub(Ve, losVector);
		setRSD(10F - (float) (Skill * Skill) * 0.33F);
		corrVector.add(wanderVector);
		Ve.add(corrVector);
		Or.transformInv(Vpl);
		float f7 = (float) (-Vpl.x);
		if (f7 < 0.001F) f7 = 0.001F;
		Vpl.normalize();
		if (Vpl.x < -0.93999999761581421D && f3 / f7 < 1.5F * (float) (3 - Skill)) {
			push();
			set_maneuver(14);
			return;
		}
		Or.transformInv(Vtarg);
		if (Vtarg.x > 0.0D && f3 < 600F && (shotAtFriend <= 0 || distToFriend > f3) && friendCheck(f3, target.Loc)) {
			if (Math.abs(Vtarg.y) < 13D && Math.abs(Vtarg.z) < 13D) ((Maneuver) target).incDangerAggressiveness(1, 0.95F, f3, this);
			if (Math.abs(Vtarg.y) < (double) (5.5F + 2.5F * (float) gunnery) && Math.abs(Vtarg.z) < 5.5D + 2.5D * (double) gunnery && bulletDelay < 120) {
				if (f3 < convAI + (float) ((4 - Skill) * 200) / getOverload()) {
					CT.WeaponControl[0] = true;
					if (subSkill > 6) if (sp >= 0.0F) sp -= f * 0.15F;
					else sp += f * 0.15F;
				}
				bulletDelay += 2;
				if (bulletDelay >= 118) {
					int i = (int) (target.getW().length() * 130D);
					if (i > 45) i = 45;
					bulletDelay += World.Rnd().nextInt(i * (Skill + 1), 2 * i * (Skill + 1));
				}
				if (f3 < convAI + (float) ((4 - Skill) * 75)) {
					if (!(actor instanceof TypeJazzPlayer)) CT.WeaponControl[1] = true;
					if (f3 < 100F && CT.Weapons[2] != null && CT.Weapons[2][0] != null && CT.Weapons[2][CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1) {
						tmpV3f.sub(target.Vwld, Vwld);
						Or.transformInv(tmpV3f);
						if (Math.abs(tmpV3f.y) < 4D && Math.abs(tmpV3f.z) < 4D) CT.WeaponControl[2] = true;
						Voice.speakAttackByRockets((Aircraft) actor);
						rocketsDelay += 120;
					}
				}
				((Pilot) target).setAsDanger(actor);
			}
		}
		Vtarg.set(Ve);
		Or.transformInv(Ve);
		Ve.normalize();
		((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f3, this);
		if (Ve.x < 0.30000001192092896D) {
			Vtarg.z += 0.044999999999999998D * (double) flying * (double) (800F + f3) * (0.30000001192092896D - Ve.x);
			Ve.set(Vtarg);
			Or.transformInv(Ve);
			Ve.normalize();
		}
		attackTurn(f3, f, 10F + (float) Skill * 8F);
		if (Alt > 5F + (float) ((4 - Skill) * 5) && Ve.x > 0.97499999999999998D && f3 < convAI * 1.25F) {
			if (followType == 0) setSpeedMode(2);
			else if (followType == 1) setSpeedMode(1);
			else setSpeedMode(9);
			tailForStaying = target;
			tailOffset.set(-convAI * 0.75F, 0.0D, 0.0D);
		} else {
			setSpeedMode(9);
		}
		if (sp > 0.0F && shootingPoint > sp) sp += f * 0.05F;
		else if (shootingPoint < sp) sp -= f * 0.05F;
	}

	public void boomAttack(float f) {
		if (sub_Man_Count == 0) {
			float f1 = 0.0F;
			if (CT.Weapons[1] != null && CT.Weapons[1][0].countBullets() > 0) f1 = ((GunGeneric) CT.Weapons[1][0]).bulletSpeed();
			else if (CT.Weapons[0] != null) f1 = ((GunGeneric) CT.Weapons[0][0]).bulletSpeed();
			if (f1 > 0.01F) bullTime = 1.0F / f1;
		}
		maxAOA = 15F;
		if (rocketsDelay > 0) rocketsDelay--;
		if (bulletDelay > 0) bulletDelay--;
		if (bulletDelay == 122) bulletDelay = 0;
		if (dist < 700F && VisCheck.checkLeadShotBlock((Aircraft) actor, (Aircraft) target.actor)) {
			if (lastKnownTargetLoc.x == -1D) {
				lastKnownTargetLoc.set(target.Loc);
				scaledApproachV.set(target.Vwld);
			}
			Ve.sub(lastKnownTargetLoc, Loc);
			float f2 = (float) (3 - Skill) * 0.2F;
			float f4 = World.Rnd().nextFloat(-f2, f2);
			lastKnownTargetLoc.x += (scaledApproachV.x + (double) f4) * (double) Time.tickConstLenFs();
			lastKnownTargetLoc.y += (scaledApproachV.y + (double) f4) * (double) Time.tickConstLenFs();
			lastKnownTargetLoc.z += (scaledApproachV.z + (double) f4) * (double) Time.tickConstLenFs();
		} else {
			lastKnownTargetLoc.x = -1D;
			Ve.sub(target.Loc, Loc);
		}
		float f3 = (float) Ve.length();
		float f5 = 1.0F + 0.15F * sp;
		tmpV3f.sub(target.Vwld, Vwld);
		Vpl.set(tmpV3f);
		tmpV3f.scale(f3 * bullTime * f5);
		Ve.add(tmpV3f);
		tmpV3f.set(Vpl);
		Or.transformInv(Vpl);
		float f6 = (float) (-Vpl.x);
		if (f6 < 0.001F) f6 = 0.001F;
		Vpl.normalize();
		if (Vpl.x < -0.93999999761581421D && f3 / f6 < 1.5F * (float) (3 - Skill)) {
			push();
			set_maneuver(14);
			return;
		}
		losVector.set(1.0D, 0.0D, 0.0D);
		Or.transform(losVector);
		losVector.set(1.0D, 0.0D, 0.0D);
		Or.transform(losVector);
		losVector.scale(Ve.length());
		corrVector.sub(Ve, losVector);
		setRSD(11F - (float) (Skill * Skill) * 0.33F);
		corrVector.add(wanderVector);
		Ve.add(corrVector);
		if (Vtarg.x > 0.0D && f3 < 700F && (shotAtFriend <= 0 || distToFriend > f3) && friendCheck(f3, target.Loc) && Math.abs(Vtarg.y) < (double) (5.5F + 2.5F * (float) gunnery) && Math.abs(Vtarg.z) < 5.5D + 2.5D * (double) gunnery && bulletDelay < 120) {
			((Maneuver) target).incDangerAggressiveness(1, 0.8F, f3, this);
			CT.WeaponControl[0] = true;
			if (subSkill > 8) if (sp >= 0.0F) sp -= f * 0.2F;
			else sp += f * 0.2F;
			bulletDelay += 2;
			if (bulletDelay >= 118) {
				int i = (int) (target.getW().length() * 130D);
				if (i > 45) i = 45;
				bulletDelay += World.Rnd().nextInt(i * (Skill + 1), 2 * i * (Skill + 1));
			}
			if (maneuver == 98) gattackCounter++;
			if (f3 < convAI + (float) ((4 - Skill) * 50)) {
				if (!(actor instanceof TypeJazzPlayer)) CT.WeaponControl[1] = true;
				if (f3 < 100F && CT.Weapons[2] != null && CT.Weapons[2][0] != null && CT.Weapons[2][CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1) {
					tmpV3f.sub(target.Vwld, Vwld);
					Or.transformInv(tmpV3f);
					if (Math.abs(tmpV3f.y) < 4D && Math.abs(tmpV3f.z) < 4D) CT.WeaponControl[2] = true;
					Voice.speakAttackByRockets((Aircraft) actor);
					rocketsDelay += 120;
				}
			}
			((Pilot) target).setAsDanger(actor);
		}
		Vtarg.set(Ve);
		Or.transformInv(Ve);
		Ve.normalize();
		((Maneuver) target).incDangerAggressiveness(1, (float) Ve.x, f3, this);
		if (Ve.x < 0.89999997615814209D) {
			Vtarg.z += (double) (0.03F * (float) Skill * (800F + f3)) * (0.89999997615814209D - Ve.x);
			Ve.set(Vtarg);
			Or.transformInv(Ve);
			Ve.normalize();
		}
		attackTurn(f3, f, 10F + (float) Skill * 8F);
		if (sp > 0.0F && shootingPoint > sp) sp += f * 0.05F;
		else if (shootingPoint < sp) sp -= f * 0.05F;
	}

	private void turnToDirection(float f) {
		if (Math.abs(Ve.y) > 0.10000000149011612D) CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.z) - 0.016F * Or.getKren();
		else CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.x) - 0.016F * Or.getKren();
		CT.RudderControl = -10F * (float) Math.atan2(Ve.y, Ve.x);
		if ((double) CT.RudderControl * W.z > 0.0D) W.z = 0.0D;
		else W.z *= 1.0399999618530273D;
		float f1 = (float) Math.atan2(Ve.z, Ve.x);
		if (Math.abs(Ve.y) < 0.0020000000949949026D && Math.abs(Ve.z) < 0.0020000000949949026D) f1 = 0.0F;
		if (Ve.x < 0.0D) {
			f1 = 1.0F;
		} else {
			if ((double) f1 * W.y > 0.0D) W.y = 0.0D;
			if (f1 < 0.0F) {
				if (getOverload() < 0.1F) f1 = 0.0F;
				if (CT.ElevatorControl > 0.0F) CT.ElevatorControl = 0.0F;
			} else if (CT.ElevatorControl < 0.0F) CT.ElevatorControl = 0.0F;
		}
		if (getOverload() > maxG || AOA > maxAOA || CT.ElevatorControl > f1) CT.ElevatorControl -= 0.3F * f;
		else CT.ElevatorControl += 0.3F * f;
	}

	private void farTurnToDirection() {
		farTurnToDirection(4F);
	}

	private void farTurnToDirection(float f) {
		Vpl.set(1.0D, 0.0D, 0.0D);
		tmpV3f.cross(Vpl, Ve);
		Or.transform(tmpV3f);
		CT.RudderControl = -10F * (float) Math.atan2(Ve.y, Ve.x) + 1.0F * (float) W.y;
		float f7 = (getSpeed() / Vmax) * 45F;
		if (f7 > 85F) f7 = 85F;
		float f8 = (float) Ve.x;
		if (f8 < 0.0F) f8 = 0.0F;
		float f1;
		if (tmpV3f.z >= 0.0D) f1 = (-0.02F * (f7 + Or.getKren()) * (1.0F - f8) - 0.05F * Or.getTangage()) + 1.0F * (float) W.x;
		else f1 = -0.02F * (-f7 + Or.getKren()) * (1.0F - f8) + 0.05F * Or.getTangage() + 1.0F * (float) W.x;
		float f2 = (-(float) Math.atan2(Ve.y, Ve.x) - 0.016F * Or.getKren()) + 1.0F * (float) W.x;
		float f4 = -0.1F * (getAOA() - 10F) + 0.5F * (float) getW().y;
		float f5;
		if (Ve.z > 0.0D) f5 = -0.1F * (getAOA() - f) + 0.5F * (float) getW().y;
		else f5 = 1.0F * (float) Ve.z + 0.5F * (float) getW().y;
		if (Math.abs(Ve.y) < 0.0020000000949949026D) {
			f2 = 0.0F;
			CT.RudderControl = 0.0F;
		}
		tmpV3f.set(Ve);
		Or.transform(tmpV3f);
		float f9 = ((float) Math.atan2(tmpV3f.y, tmpV3f.x) * 180F) / 3.1415F;
		float f10 = 1.0F - Math.abs(f9 - Or.getYaw()) * 0.01F;
		if (f10 < 0.0F || Ve.x < 0.0D) f10 = 0.0F;
		float f3 = f10 * f2 + (1.0F - f10) * f1;
		float f6 = f10 * f5 + (1.0F - f10) * f4;
		CT.AileronControl = f3;
		CT.ElevatorControl = f6;
	}

	private void turnBaby(float f, float f1, float f2) {
		if (Ve.x < 0.0099999997764825821D) Ve.x = 0.0099999997764825821D;
		if (sub_Man_Count == 0) oldVe.set(Ve);
		minElevCoeff = 20F;
		CT.RudderControl = (float) (-3D * Math.atan2(Ve.y, Ve.x) + 0.050000000000000003D * (Ve.y - oldVe.y));
		float f3 = (float) (10D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z));
		CT.AileronControl = (-3F * (float) Math.atan2(Ve.y, Ve.x) - 0.006F * Or.getKren()) + 0.3F * (float) W.x;
		if (Math.abs(CT.ElevatorControl - f3) < f2 * f1) CT.ElevatorControl = f3;
		else if (CT.ElevatorControl < f3) CT.ElevatorControl += f2 * f1;
		else CT.ElevatorControl -= 0.2F * f2 * f1;
		if (AOA < AOA_Crit * 0.75F && AOA > 0.0F) nShakeMe(flying, Skill);
		oldVe.set(Ve);
	}

	private void attackTurnToDirection(float f, float f1, float f2) {
		if (Ve.x < 0.0099999997764825821D) Ve.x = 0.0099999997764825821D;
		if (sub_Man_Count == 0) oldVe.set(Ve);
		if (Ve.x > 0.94999998807907104D) {
			CT.RudderControl = (float) (-30D * Math.atan2(Ve.y, Ve.x) + 1.5D * (Ve.y - oldVe.y));
			float f3;
			if (Ve.z > 0.0D || CT.RudderControl > 0.9F) {
				f3 = (float) (10D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z));
				CT.AileronControl = (-30F * (float) Math.atan2(Ve.y, Ve.x) - 0.02F * Or.getKren()) + 5F * (float) W.x;
			} else {
				f3 = (float) (5D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z));
				CT.AileronControl = (-5F * (float) Math.atan2(Ve.y, Ve.x) - 0.02F * Or.getKren()) + 5F * (float) W.x;
			}
			if (Ve.x > (double) (1.0F - 0.005F * (float) Skill)) {
				tmpOr.set(Or);
				tmpOr.increment((float) Math.toDegrees(Math.atan2(Ve.y, Ve.x)), (float) Math.toDegrees(Math.atan2(Ve.z, Ve.x)), 0.0F);
				Or.interpolate(tmpOr, 0.1F);
			}
			if (Math.abs(CT.ElevatorControl - f3) < f2 * f1) CT.ElevatorControl = f3;
			else if (CT.ElevatorControl < f3) CT.ElevatorControl += f2 * f1;
			else CT.ElevatorControl -= f2 * f1;
		} else {
			if (Skill >= 2 && Ve.z > 0.5D && f < 600F) CT.FlapsControl = 0.1F;
			else CT.FlapsControl = 0.0F;
			float f5 = 0.6F - (float) Ve.z;
			if (f5 < 0.0F) f5 = 0.0F;
			CT.RudderControl = (float) (-30D * Math.atan2(Ve.y, Ve.x) * (double) f5 + 1.0D * (Ve.y - oldVe.y) * Ve.x + 0.5D * W.z);
			float f4;
			if (Ve.z > 0.0D) {
				f4 = (float) (10D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z) + 0.5D * (double) (float) W.y);
				if (f4 < 0.0F) f4 = 0.0F;
				CT.AileronControl = (float) ((-20D * Math.atan2(Ve.y, Ve.z) - 0.050000000000000003D * (double) Or.getKren()) + 5D * W.x);
			} else {
				f4 = (float) (-5D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z) + 0.5D * (double) (float) W.y);
				CT.AileronControl = (float) ((-20D * Math.atan2(Ve.y, Ve.z) - 0.050000000000000003D * (double) Or.getKren()) + 5D * W.x);
			}
			if (f4 < 0.0F) f4 = 0.0F;
			if (Math.abs(CT.ElevatorControl - f4) < f2 * f1) CT.ElevatorControl = f4;
			else if (CT.ElevatorControl < f4) CT.ElevatorControl += 0.3F * f1;
			else CT.ElevatorControl -= 0.3F * f1;
		}
		float f6 = 0.054F * (600F - f);
		if (f6 < 4F) f6 = 4F;
		if (f6 > AOA_Crit) f6 = AOA_Crit;
		if (AOA > f6 - 1.5F) Or.increment(0.0F, 0.16F * (f6 - 1.5F - AOA), 0.0F);
		if (AOA < -5F) Or.increment(0.0F, 0.12F * (-5F - AOA), 0.0F);
		oldVe.set(Ve);
		setOldAOA(AOA);
		sub_Man_Count++;
		W.x *= 0.94999999999999996D;
	}

	private void attackTurn(float f, float f1, float f2) {
		if (Ve.x < 0.0099999997764825821D && subSkill > 6 && maneuver == 27 && f < 800F && getMinHeightTurn(100F) < ((Maneuver) target).getMinHeightTurn(50F) + (float) Skill * 0.33F) set_maneuver(101);
		else if (Ve.x < 0.0099999997764825821D) Ve.x = 0.0099999997764825821D;
		if (sub_Man_Count == 0) oldVe.set(Ve);
		if (Ve.x > 0.94999998807907104D) {
			CT.RudderControl = (float) (-10D * Math.atan2(Ve.y, Ve.x) + 1.5D * (Ve.y - oldVe.y));
			float f3;
			float f5;
			if (Ve.z > 0.0D || CT.RudderControl > 0.9F) {
				f3 = (float) (10D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z));
				f5 = -10F * (float) Math.atan2(Ve.y, Ve.x);
			} else {
				f3 = (float) (5D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z));
				f5 = (-5F * (float) Math.atan2(Ve.y, Ve.x) - 0.02F * Or.getKren()) + 5F * (float) W.x;
			}
			CT.AileronControl = f5;
			if (Ve.x > (double) (1.0F - 0.005F * (float) Skill)) {
				tmpOr.set(Or);
				tmpOr.increment((float) Math.toDegrees(Math.atan2(Ve.y, Ve.x)), (float) Math.toDegrees(Math.atan2(Ve.z, Ve.x)), 0.0F);
				Or.interpolate(tmpOr, 0.1F);
			}
			if (Math.abs(CT.ElevatorControl - f3) < f2 * f1) CT.ElevatorControl = f3;
			else if (CT.ElevatorControl < f3) CT.ElevatorControl += f2 * f1;
			else CT.ElevatorControl -= f2 * f1;
		} else {
			if (Skill >= 2 && Ve.z > 0.5D && f < 600F) CT.FlapsControl = 0.1F;
			else CT.FlapsControl = 0.0F;
			float f6 = 0.6F - (float) Ve.z;
			if (f6 < 0.0F) f6 = 0.0F;
			CT.RudderControl = (float) (-30D * Math.atan2(Ve.y, Ve.x) * (double) f6 + 1.0D * (Ve.y - oldVe.y) * Ve.x + 0.5D * W.z);
			float f4;
			if (Ve.z > 0.0D) {
				f4 = (float) (10D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z) + 0.5D * (double) (float) W.y);
				if (f4 < 0.0F) f4 = 0.0F;
				CT.AileronControl = (float) ((-20D * Math.atan2(Ve.y, Ve.z) - 0.050000000000000003D * (double) Or.getKren()) + 5D * W.x);
			} else {
				f4 = (float) (-5D * Math.atan2(Ve.z, Ve.x) + 6D * (Ve.z - oldVe.z) + 0.5D * (double) (float) W.y);
				CT.AileronControl = (float) ((-20D * Math.atan2(Ve.y, Ve.z) - 0.050000000000000003D * (double) Or.getKren()) + 5D * W.x);
			}
			if (f4 < 0.0F) f4 = 0.0F;
			if (Math.abs(CT.ElevatorControl - f4) < f2 * f1) CT.ElevatorControl = f4;
			else if (CT.ElevatorControl < f4) CT.ElevatorControl += 0.3F * f1;
			else CT.ElevatorControl -= 0.3F * f1;
		}
		float f7 = 0.054F * (600F - f);
		if (f7 < 4F) f7 = 4F;
		if (f7 > AOA_Crit) f7 = AOA_Crit;
		if (AOA > f7 - 1.5F) Or.increment(0.0F, 0.16F * (f7 - 1.5F - AOA), 0.0F);
		if (AOA < -5F) Or.increment(0.0F, 0.12F * (-5F - AOA), 0.0F);
		oldVe.set(Ve);
		setOldAOA(AOA);
		sub_Man_Count++;
		W.x *= 0.94999999999999996D;
	}

	private void doCheckStrike() {
		if (M.massEmpty > 5000F && !AP.way.isLanding()) return;
		if (maneuver == 24 && strikeTarg == Leader) return;
		if ((actor instanceof TypeDockable) && ((TypeDockable) actor).typeDockableIsDocked()) return;
		Vpl.sub(strikeTarg.Loc, Loc);
		tmpV3f.set(Vpl);
		Or.transformInv(tmpV3f);
		if (tmpV3f.x < 0.0D) return;
		tmpV3f.sub(strikeTarg.Vwld, Vwld);
		tmpV3f.scale(0.69999998807907104D);
		Vpl.add(tmpV3f);
		Or.transformInv(Vpl);
		push();
		if (Vpl.z > 0.0D && Alt > 50F) push(61);
		else push(60);
		safe_pop();
	}

	public void setStrikeEmer(FlightModel flightmodel) {
		strikeEmer = true;
		strikeTarg = flightmodel;
	}

	float bombDist(float f) {
		float f1 = positiveSquareEquation(-0.5F * World.g(), (float) Vwld.z, f);
		if (f1 < 0.0F) return -1000000F;
		else return f1 * (float) Math.sqrt(Vwld.x * Vwld.x + Vwld.y * Vwld.y);
	}

	protected void wingman(boolean flag) {
		if (Wingman == null) {
			return;
		} else {
			Wingman.Leader = flag ? ((FlightModel) (this)) : null;
			return;
		}
	}

	public float getMnTime() {
		return mn_time;
	}

	public void doDumpBombsPassively() {
		for (int i = 0; i < CT.Weapons.length; i++) {
			if (CT.Weapons[i] == null || CT.Weapons[i].length <= 0) continue;
			for (int j = 0; j < CT.Weapons[i].length; j++) {
				if (!(CT.Weapons[i][j] instanceof BombGun)) continue;
				if ((CT.Weapons[i][j] instanceof BombGunPara) || (actor instanceof SM79) && (CT.Weapons[i][j] instanceof TorpedoGun)) {
					continue;
				}
				CT.dropExternalStores(true);
				break;
			}

		}

	}

	protected void printDebugFM() {
		String s = ((Aircraft) actor).typedName();
		System.out.print("<" + actor.name() + "> (" + s + ") " + ManString.tname(task) + ":" + ManString.name(maneuver) + (target == null ? " t" : " T") + (danger == null ? "d" : "D") + (airClient == null ? "c" : "C")
				+ (target_ground == null ? "g" : "G") + (targetLost ? "L " : "l "));
		HUD.training("<" + actor.name() + "> " + s + " " + ManString.tname(task) + ":" + ManString.name(maneuver) + (target == null ? " t" : " T") + (danger == null ? "d" : "D") + (airClient == null ? "c" : "C") + (target_ground == null ? "g" : "G")
				+ (targetLost ? "L " : "l "));
		switch (maneuver) {
		case 21: // '\025'
			System.out.println(": WP=" + AP.way.Cur() + "(" + (AP.way.size() - 1) + ")-" + ManString.wpname(AP.way.curr().Action));
			if (target_ground != null) System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
			break;

		case 27: // '\033'
			if (target == null || !Actor.isValid(target.actor)) System.out.println(" T=null");
			else System.out.println(" T=" + target.actor.getClass().getName() + "(" + target.actor.name() + ")");
			break;

		case 43: // '+'
		case 50: // '2'
		case 51: // '3'
		case 52: // '4'
			if (target_ground == null || !Actor.isValid(target_ground)) System.out.println(" GT=null");
			else System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
			break;

		default:
			System.out.println("");
			if (target == null || !Actor.isValid(target.actor)) {
				System.out.println(" T=null");
				break;
			}
			System.out.println(" T=" + target.actor.getClass().getName() + "(" + target.actor.name() + ")");
			if (target_ground == null || !Actor.isValid(target_ground)) {
				System.out.println(" GT=null");
				break;
			}
			System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
			if (airClient == null || !Actor.isValid(airClient.actor)) System.out.println(" Client=null");
			else System.out.println(" Client=" + airClient.getClass().getName() + "(" + airClient.actor.name() + ")");
			break;
		}
	}

	protected void headTurn(float f) {
		if (actor == Main3D.cur3D().viewActor() && AS.astatePilotStates[0] < 90) {
			boolean flag = false;
			switch (get_task()) {
			case 2: // '\002'
				if (Leader != null) {
					Ve.set(Leader.Loc);
					flag = true;
				}
				break;

			case 6: // '\006'
				if (target != null) {
					Ve.set(target.Loc);
					flag = true;
				}
				break;

			case 5: // '\005'
				if (airClient != null) {
					Ve.set(airClient.Loc);
					flag = true;
				}
				break;

			case 4: // '\004'
				if (danger != null) {
					Ve.set(danger.Loc);
					flag = true;
				}
				break;

			case 7: // '\007'
				if (target_ground != null) {
					Ve.set(target_ground.pos.getAbsPoint());
					flag = true;
				}
				break;
			}
			float f1;
			float f2;
			if (flag) {
				Ve.sub(Loc);
				Or.transformInv(Ve);
				tmpOr.setAT0(Ve);
				f1 = tmpOr.getTangage();
				f2 = tmpOr.getYaw();
				if (f2 > 107F) f2 = 107F;
				if (f2 < -107F) f2 = -107F;
				if (f1 < -22F) f1 = -22F;
				if (f1 > 57F) f1 = 57F;
			} else if (maneuver == 88) {
				f1 = 0.0F;
				f2 = 107F;
			} else if (maneuver == 89) {
				f1 = 0.0F;
				f2 = -107F;
			} else if (maneuver == 90) {
				f1 = 0.0F;
				f2 = 90F;
			} else if (maneuver == 91) {
				f1 = 0.0F;
				f2 = -90F;
			} else if (get_maneuver() == 44) {
				f2 = -15F;
				f1 = -15F;
			} else {
				f1 = 0.0F;
				f2 = 0.0F;
			}
			if (Math.abs(pilotHeadT - f1) > 3F) pilotHeadT += 90F * (pilotHeadT <= f1 ? 1.0F : -1F) * f;
			else pilotHeadT = f1;
			if (Math.abs(pilotHeadY - f2) > 2.0F) pilotHeadY += 60F * (pilotHeadY <= f2 ? 1.0F : -1F) * f;
			else pilotHeadY = f2;
			((NetAircraft) actor).setHeadAngles(pilotHeadY, pilotHeadT);
		}
	}

	protected void turnOffTheWeapon() {
		CT.WeaponControl[0] = false;
		CT.WeaponControl[1] = false;
		CT.WeaponControl[2] = false;
		CT.WeaponControl[3] = false;
		if (bombsOut) {
			bombsOutCounter++;
			if (bombsOutCounter > 128) {
				bombsOutCounter = 0;
				bombsOut = false;
			}
			if (CT.Weapons[3] != null) CT.WeaponControl[3] = true;
			else bombsOut = false;
			if (CT.Weapons[3] != null && CT.Weapons[3][0] != null) {
				int i = 0;
				for (int j = 0; j < CT.Weapons[3].length; j++)
					i += CT.Weapons[3][j].countBullets();

				if (i == 0) {
					bombsOut = false;
					for (int k = 0; k < CT.Weapons[3].length; k++)
						CT.Weapons[3][k].loadBullets(0);

				}
			}
		}
	}

	protected void turnOnChristmasTree(boolean flag) {
		if (flag) {
			if (World.Sun().ToSun.z < -0.22F || bNoNavLightsAI) AS.setNavLightsState(true);
		} else {
			AS.setNavLightsState(false);
		}
	}

	protected void turnOnCloudShine(boolean flag) {
		if (flag) {
			if (World.Sun().ToSun.z < -0.22F) AS.setLandingLightState(true);
		} else {
			AS.setLandingLightState(false);
		}
	}

	private boolean doGroundCheckFront(float f) {
		if (maneuver == 2 && (Or.getPitch() < 360F || Vrel.z < 0.0D)) return false;
		float f1 = (float) Math.sqrt(Vrel.x * Vrel.x + Vrel.y * Vrel.y);
		float f2 = (float) (getW().z * 2000D);
		if (f2 < -500F) f2 = -500F;
		if (f2 > 500F) f2 = 500F;
		float f3 = 1.0F;
		if (task == 24) f3 = 0.5F;
		else if (task == 3) f3 = 1.5F;
		float f4 = (f1 * 20F * f - Math.abs(f2)) * f3;
		if (f4 < 800F) f4 = 800F;
		if (maneuver == 21) {
			float f5 = AP.getWayPointDistance();
			f4 = Math.min(f4, f5);
		}
		if (f4 > 2500F) f4 = 2500F;
		Po.set(Loc);
		float f6 = -10F;
		if (Vrel.z > 0.0D) f6 = (float) ((double) f6 - Vrel.z);
		if (f6 < -30F) f6 = -30F;
		if (target != null) {
			tempV.sub(Loc, target.Loc);
			Or.transformInv(tempV);
			tempV.normalize();
			if (tempV.x < -0.96999999999999997D) {
				f4 = 50F;
				f6 = 0.0F;
			}
		}
		Vpl.set(f4, f2, f6);
		float f7 = Or.getPitch();
		if (f7 < 360F) f7 = 360F;
		tmpOr.setYPR(Or.getYaw(), f7, 0.0F);
		tmpOr.transform(Vpl);
		Po.add(Vpl);
		Pd.set(Po);
		if (Landscape.rayHitHQ(actor.pos.getAbsPoint(), Pd, tempPoint)) {
			push();
			push(84);
			pop();
			return true;
		} else {
			return false;
		}
	}

	protected void doCheckGround(float f) {
		if (Gears.getWheelsOnGround() || Gears.isUnderDeck()) {
			setCheckGround(false);
			pop();
			return;
		}
		if (CT.AileronControl > 1.0F) CT.AileronControl = 1.0F;
		if (CT.AileronControl < -1F) CT.AileronControl = -1F;
		if (CT.ElevatorControl > 1.0F) CT.ElevatorControl = 1.0F;
		if (CT.ElevatorControl < -1F) CT.ElevatorControl = -1F;
		float f4 = 0.0003F * M.getFullMass();
		float f5 = 10F;
		float f6 = 80F;
		if (maneuver == 24) {
			f5 = 15F;
			f6 = 120F;
		}
		if (Alt < 2000F && isTick(16, 0) && doGroundCheckFront(f4)) return;
		if (maneuver == 57 || maneuver == 2) return;
		float f7 = (float) (-Vwld.z) * f5 * f4;
		float f8 = 1.0F + 7F * ((indSpeed - VmaxAllowed) / VmaxAllowed);
		if (f8 > 1.0F) f8 = 1.0F;
		if (f8 < 0.0F) f8 = 0.0F;
		float f1;
		float f2;
		float f3;
		if (f7 > Alt || Alt < f6 || f8 > 0.0F) {
			if (Alt < 0.001F) Alt = 0.001F;
			f1 = (f7 - Alt) / Alt;
			Math.max(0.016670000000000001D * (double) (f6 - Alt), f1);
			if (f1 > 1.0F) f1 = 1.0F;
			if (f1 < 0.0F) f1 = 0.0F;
			if (f1 < f8) f1 = f8;
			f2 = -0.12F * (Or.getTangage() - 5F) + 3F * (float) W.y;
			f3 = -0.07F * Or.getKren() + 3F * (float) W.x;
			if (f3 > 2.0F) f3 = 2.0F;
			if (f3 < -2F) f3 = -2F;
			if (f2 > 2.0F) f2 = 2.0F;
			if (f2 < -2F) f2 = -2F;
		} else {
			f1 = 0.0F;
			f2 = 0.0F;
			f3 = 0.0F;
		}
		float f9 = 0.2F;
		if (corrCoeff < f1) corrCoeff = f1;
		if (corrCoeff > f1) corrCoeff *= 1.0D - (double) (f9 * f);
		if (f2 < 0.0F) {
			if (corrElev > f2) corrElev = f2;
			if (corrElev < f2) corrElev *= 1.0D - (double) (f9 * f);
		} else {
			if (corrElev < f2) corrElev = f2;
			if (corrElev > f2) corrElev *= 1.0D - (double) (f9 * f);
		}
		if (f3 < 0.0F) {
			if (corrAile > f3) corrAile = f3;
			if (corrAile < f3) corrAile *= 1.0D - (double) (f9 * f);
		} else {
			if (corrAile < f3) corrAile = f3;
			if (corrAile > f3) corrAile *= 1.0D - (double) (f9 * f);
		}
		CT.AileronControl = corrCoeff * corrAile + (1.0F - corrCoeff) * CT.AileronControl;
		CT.ElevatorControl = corrCoeff * corrElev + (1.0F - corrCoeff) * CT.ElevatorControl;
		if (Alt < 15F && Vwld.z < 0.0D) Vwld.z *= 0.85000002384185791D;
		if (-Vwld.z * 2.5D > (double) Alt || Alt < 20F / (float) (Skill + 1)) {
			if (maneuver == 27 || maneuver == 43 || maneuver == 21 || maneuver == 24 || maneuver == 68 || maneuver == 53 || maneuver == 65 || maneuver == 76 || maneuver == 74 || maneuver == 75) push();
			push(2);
			pop();
			submaneuver = 0;
			sub_Man_Count = 0;
		}
		W.x *= 0.94999999999999996D;
	}

	protected void setSpeedControl(float f) {
		float f1 = 0.8F;
		float f4 = 0.02F;
		float f5 = 1.5F;
		float f14 = -0.05F;
		boolean flag = false;
		CT.setAfterburnerControl(false);
		switch (speedMode) {
		case 1: // '\001'
			if (tailForStaying == null) {
				f1 = 1.0F;
			} else {
				Ve.sub(tailForStaying.Loc, Loc);
				tailForStaying.Or.transform(tailOffset, tmpV3f);
				Ve.add(tmpV3f);
				float f15 = (float) Ve.z;
				float f6 = 0.005F * (200F - (float) Ve.length());
				Or.transformInv(Ve);
				float f10 = (float) Ve.x;
				Ve.normalize();
				f6 = Math.max(f6, (float) Ve.x);
				if (f6 < 0.0F) f6 = 0.0F;
				Ve.set(Vwld);
				Ve.normalize();
				tmpV3f.set(tailForStaying.Vwld);
				tmpV3f.normalize();
				float f8 = (float) tmpV3f.dot(Ve);
				if (f8 < 0.0F) f8 = 0.0F;
				f6 *= f8;
				if (f6 > 0.0F && f10 < 1000F) {
					if (f10 > 300F) f10 = 300F;
					float f18 = 0.6F;
					if (tailForStaying.VmaxH == VmaxH) f18 = tailForStaying.CT.getPower();
					if (Vmax < 83F) {
						float f21 = f15;
						if (f21 > 0.0F) {
							if (f21 > 20F) f21 = 20F;
							Vwld.z += 0.01F * f21;
						}
					}
					float f12;
					if (f10 > 0.0F) f12 = 0.007F * f10 + 0.005F * f15;
					else f12 = 0.03F * f10 + 0.001F * f15;
					float f22 = getSpeed() - tailForStaying.getSpeed();
					float f24 = -0.3F * f22;
					float f29 = -3F * (getForwAccel() - tailForStaying.getForwAccel());
					if (f22 > 27F) {
						CT.FlapsControl = 1.0F;
						f1 = 0.0F;
					} else {
						CT.FlapsControl = 0.02F * f22 + 0.02F * -f10;
						f1 = f18 + f12 + f24 + f29;
					}
				} else {
					f1 = 1.1F;
				}
			}
			break;

		case 2: // '\002'
			f1 = (float) (1.0D - 8.0000000000000007E-005D * (0.5D * Vwld.lengthSquared() - 9.8000000000000007D * Ve.z - 0.5D * tailForStaying.Vwld.lengthSquared()));
			break;

		case 3: // '\003'
			f14 = -0.1F;
			f1 = CT.PowerControl;
			if (AP.way.curr().Speed < 10F) AP.way.curr().set(1.7F * Vmin);
			float f19 = AP.way.curr().Speed / VmaxH;
			f1 = 0.2F + 0.8F * (float) Math.pow(f19, 1.5D);
			f1 += 0.1F * (AP.way.curr().Speed - Pitot.Indicator((float) Loc.z, getSpeed())) - 3F * getForwAccel();
			if (bSlowDown && f1 > 0.7F) f1 = 0.7F;
			if (getAltitude() < AP.way.curr().z() - 70F) f1 += AP.way.curr().z() - 70F - getAltitude();
			if (f1 > 0.9F) f1 = 0.9F;
			if (f1 > 0.5F && AP.way.isLanding() && AP.way.Cur() > 1 && (super.actor instanceof TypeFastJet)) f1 = 0.5F;
			if (f1 < 0.35F && !AP.way.isLanding()) f1 = 0.35F;
			break;

		case 4: // '\004'
			f1 = CT.PowerControl;
			f1 = (float) ((double) f1 + ((double) (f4 * (smConstSpeed - Pitot.Indicator((float) Loc.z, getSpeed()))) - ((double) f5 * getLocalAccel().x) / 9.8100004196166992D) * (double) f);
			if (f1 > 1.0F) f1 = 1.0F;
			break;

		case 5: // '\005'
			f1 = CT.PowerControl;
			CT.FlapsControl = 1.0F;
			f1 += (f4 * (1.3F * VminFLAPS - Pitot.Indicator((float) Loc.z, getSpeed())) - f5 * getForwAccel()) * f;
			break;

		case 8: // '\b'
			f1 = 0.0F;
			break;

		case 6: // '\006'
			f1 = 1.0F;
			f14 = 0.0F;
			break;

		case 9: // '\t'
			f1 = 1.1F;
			CT.setAfterburnerControl(true);
			f14 = 0.01F;
			break;

		case 11: // '\013'
			f1 = 1.1F;
			CT.setAfterburnerControl(true);
			f14 = 0.04F;
			flag = true;
			break;

		case 7: // '\007'
			f1 = smConstPower;
			break;

		case 10: // '\n'
			if (tailForStaying == null) {
				f1 = 1.0F;
			} else {
				Ve.sub(tailForStaying.Loc, Loc);
				tailForStaying.Or.transform(tailOffset, tmpV3f);
				Ve.add(tmpV3f);
				float f16 = (float) Ve.z;
				float f7 = 0.005F * (200F - (float) Ve.length());
				Or.transformInv(Ve);
				float f11 = (float) Ve.x;
				Ve.normalize();
				f7 = Math.max(f7, (float) Ve.x);
				if (f7 < 0.0F) f7 = 0.0F;
				Ve.set(Vwld);
				Ve.normalize();
				tmpV3f.set(tailForStaying.Vwld);
				tmpV3f.normalize();
				float f9 = (float) tmpV3f.dot(Ve);
				if (f9 < 0.0F) f9 = 0.0F;
				f7 *= f9;
				if (f7 > 0.0F && f11 < 1000F) {
					if (f11 > 300F) f11 = 300F;
					float f23 = 0.6F;
					if (tailForStaying.VmaxH == VmaxH) f23 = tailForStaying.CT.getPower();
					if (Vmax < 83F) {
						float f25 = f16;
						if (f25 > 0.0F) {
							if (f25 > 20F) f25 = 20F;
							Vwld.z += 0.01F * f25;
						}
					}
					float f13;
					if (f11 > 0.0F) f13 = 0.01F * f11 + 0.07F * f16;
					else f13 = 0.03F * f11 + 0.001F * f16;
					float f26 = getSpeed() - tailForStaying.getSpeed();
					float f30 = -0.3F * f26;
					float f31 = -3F * (getForwAccel() - tailForStaying.getForwAccel());
					if (f26 > 27F) {
						Vwld.scale(0.98000001907348633D);
						f1 = 0.0F;
					} else {
						float f32 = 1.0F - 0.02F * (0.02F * f26 + 0.02F * -f11);
						if (f32 < 0.98F) f32 = 0.98F;
						if (f32 > 1.0F) f32 = 1.0F;
						Vwld.scale(f32);
						f1 = f23 + f13 + f30 + f31;
					}
				} else {
					f1 = 1.1F;
				}
			}
			break;

		default:
			return;
		}
		if (AS.isEnginesOnFire()) f1 = (float) ((double) f1 * 0.59999999999999998D);
		if (f1 > 1.1F) f1 = 1.1F;
		else if (f1 < 0.0F) f1 = 0.0F;
		float f17 = EI.getAIOverheatFactor();
		float f20 = f17 * 15F;
		char c = '\u0800';
		if (Skill >= 2) c = '\u0400';
		if (f17 > f14) {
			if (f17 > 0.0F && speedMode != 10) f1 *= 1.0F - f20;
			if (!EI.isSelectionAllowsAutoRadiator()) {
				float f27 = 0.0F;
				if (f17 > 0.0F) f27 = 0.1F + f20 * 5F;
				else f27 = 0.1F;
				if (f27 > CT.getRadiatorControl()) c = '\020';
				if (isTick(c, 0)) CT.setRadiatorControl(f27);
			}
			if (f1 > 1.1F) f1 = 1.1F;
			else if (f1 < 0.0F) f1 = 0.0F;
		} else if (!EI.isSelectionAllowsAutoRadiator() && CT.getRadiatorControl() > 0.0F && (flag || isTick(c, 0))) CT.setRadiatorControl(0.0F);
		if ((double) Math.abs(CT.PowerControl - f1) < 0.5D * (double) f) CT.setPowerControl(f1);
		else if (CT.PowerControl < f1) CT.setPowerControl(CT.getPowerControl() + 0.5F * f);
		else CT.setPowerControl(CT.getPowerControl() - 0.5F * f);
		float f28 = EI.engines[0].getCriticalW();
		if (EI.engines[0].getw() > 0.9F * f28) {
			float f2 = (10F * (f28 - EI.engines[0].getw())) / f28;
			if (f2 < CT.PowerControl) CT.setPowerControl(f2);
		}
		if (indSpeed > 0.8F * VmaxAllowed) {
			float f3 = (1.0F * (VmaxAllowed - indSpeed)) / VmaxAllowed;
			if (f3 < CT.PowerControl) CT.setPowerControl(f3);
		}
	}

	private void setRandomTargDeviation(float f) {
		if (isTick(16, 0)) {
			float f1 = f * (8F - 1.5F * (float) Skill);
			TargDevVnew.set(World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1));
		}
		TargDevV.interpolate(TargDevVnew, 0.01F);
	}

	private void setRSD(float f) {
		if (isTick(16, 0)) {
			float f1 = f * (6F - (float) (gunnery + flying) * 0.5F);
			wanderVectorNew.set(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F));
			wanderVectorNew.normalize();
			wanderVectorNew.scale(f1);
		}
		wanderVector.interpolate(wanderVectorNew, wanderRate * 0.75F);
	}

	private void setRSDgrd(float f, float f1) {
		if (isTick(16, 0)) {
			float f2 = 0.65F * f * (10F - (float) (gunnery + flying) * 0.5F);
			wanderVectorNew.set(0.0D, World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F));
			wanderVectorNew.normalize();
			wanderVectorNew.scale(f2 * f1);
		}
		wanderVector.interpolate(wanderVectorNew, wanderRate * 1.2F);
	}

	private Point_Any getNextTaxiToTakeOffPoint() {
		if (taxiToTakeOffWay == null) return null;
		if (taxiToTakeOffWay.size() == 0) return null;
		if (curAirdromePoi >= taxiToTakeOffWay.size()) return null;
		else return (Point_Any) taxiToTakeOffWay.get(curAirdromePoi++);
	}

	private void findPointForEmLanding(float f) {
		Point3d point3d = elLoc.getPoint();
		if (radius > 2D * (double) rmax) if (submaneuver != 69) initTargPoint(f);
		else return;
		for (int i = 0; i < 32; i++) {
			Po.set(Vtarg.x + radius * Math.sin(phase), Vtarg.y + radius * Math.cos(phase), Loc.z);
			radius += 0.01D * (double) rmax;
			phase += 0.29999999999999999D;
			Ve.sub(Po, Loc);
			double d = Ve.length();
			Or.transformInv(Ve);
			Ve.normalize();
			float f1 = 0.9F - 0.005F * Alt;
			if (f1 < -1F) f1 = -1F;
			if (f1 > 0.8F) f1 = 0.8F;
			float f2 = 1.5F - 0.0005F * Alt;
			if (f2 < 1.0F) f2 = 1.0F;
			if (f2 > 1.5F) f2 = 1.5F;
			if ((double) rmax > d && d > (double) (rmin * f2) && Ve.x > (double) f1 && isConvenientPoint()) {
				submaneuver = 69;
				point3d.set(Po);
				pointQuality = curPointQuality;
				return;
			}
		}

	}

	private boolean isConvenientPoint() {
		Po.z = Engine.cur.land.HQ_Air(Po.x, Po.y);
		curPointQuality = 50;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (Engine.land().isWater(Po.x + 500D * (double) i, Po.y + 500D * (double) j)) {
					if (!(actor instanceof TypeSeaPlane)) curPointQuality--;
				} else if (actor instanceof TypeSeaPlane) curPointQuality--;
				if (Engine.cur.land.HQ_ForestHeightHere(Po.x + 500D * (double) i, Po.y + 500D * (double) j) > 1.0D) curPointQuality -= 2;
				if (Engine.cur.land.EQN(Po.x + 500D * (double) i, Po.y + 500D * (double) j, Ve) > 1110.949951171875D) curPointQuality -= 2;
			}

		}

		if (curPointQuality < 0) curPointQuality = 0;
		return curPointQuality > pointQuality;
	}

	private void emergencyTurnToDirection(float f) {
		if (Math.abs(Ve.y) > 0.10000000149011612D) CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.z) - 0.016F * Or.getKren();
		else CT.AileronControl = -(float) Math.atan2(Ve.y, Ve.x) - 0.016F * Or.getKren();
		CT.RudderControl = -10F * (float) Math.atan2(Ve.y, Ve.x);
		if ((double) CT.RudderControl * W.z > 0.0D) W.z = 0.0D;
		else W.z *= 1.0399999618530273D;
	}

	private void initTargPoint(float f) {
		int i = AP.way.Cur();
		Vtarg.set(AP.way.last().x(), AP.way.last().y(), 0.0D);
		AP.way.setCur(i);
		Vtarg.sub(Loc);
		Vtarg.z = 0.0D;
		if (Vtarg.length() > (double) rmax) {
			Vtarg.normalize();
			Vtarg.scale(rmax);
		}
		Vtarg.add(Loc);
		phase = 0.0D;
		radius = 50D;
		pointQuality = -1;
	}

	private void emergencyLanding(float f) {
		if (first) {
			Kmax = Wing.new_Cya(5F) / Wing.new_Cxa(5F);
			if (Kmax > 14F) Kmax = 14F;
			Kmax *= 0.8F;
			rmax = 1.2F * Kmax * Alt;
			rmin = 0.6F * Kmax * Alt;
			initTargPoint(f);
			setReadyToDieSoftly(true);
			AP.setStabAll(false);
			if (TaxiMode) {
				setSpeedMode(8);
				smConstPower = 0.0F;
				push(44);
				pop();
			}
			dist = Alt;
			if (actor instanceof TypeSeaPlane) EI.setEngineStops();
		}
		setSpeedMode(4);
		smConstSpeed = VminFLAPS * 1.25F;
		if (Alt < 500F && ((actor instanceof TypeGlider) || (actor instanceof TypeSeaPlane))) CT.GearControl = 1.0F;
		if (Alt < 10F) {
			CT.AileronControl = -0.04F * Or.getKren();
			setSpeedMode(4);
			smConstSpeed = VminFLAPS * 1.1F;
			if (Alt < 5F) setSpeedMode(8);
			CT.BrakeControl = 0.2F;
			if (Vwld.length() < 0.30000001192092896D && World.Rnd().nextInt(0, 99) < 4) {
				setStationedOnGround(true);
				World.cur();
				if (actor != World.getPlayerAircraft()) {
					push(44);
					safe_pop();
					if (actor instanceof TypeSeaPlane) {
						EI.setCurControlAll(true);
						EI.setEngineStops();
						if (Engine.land().isWater(Loc.x, Loc.y)) return;
					}
					((Aircraft) actor).hitDaSilk();
				}
				if (Group != null) Group.delAircraft((Aircraft) actor);
				if ((actor instanceof TypeGlider) || (actor instanceof TypeSeaPlane)) return;
				World.cur();
				if (actor != World.getPlayerAircraft()) if (Airport.distToNearestAirport(Loc) > 900D) ((Aircraft) actor).postEndAction(60D, actor, 4, null);
				else MsgDestroy.Post(Time.current() + 30000L, actor);
			}
		}
		dA = 0.2F * (getSpeed() - Vmin * 1.3F) - 0.8F * (getAOA() - 5F);
		if (Alt < 40F) {
			CT.AileronControl = -0.04F * Or.getKren();
			CT.RudderControl = 0.0F;
			if ((actor instanceof BI_1) || (actor instanceof ME_163B1A)) CT.GearControl = 1.0F;
			float f1;
			if (Gears.Pitch < 10F) f1 = (40F * (getSpeed() - VminFLAPS * 1.15F) - 60F * (Or.getTangage() + 3F) - 240F * (getVertSpeed() + 1.0F) - 120F * ((float) getAccel().z - 1.0F)) * 0.004F;
			else f1 = (40F * (getSpeed() - VminFLAPS * 1.15F) - 60F * ((Or.getTangage() - Gears.Pitch) + 10F) - 240F * (getVertSpeed() + 1.0F) - 120F * ((float) getAccel().z - 1.0F)) * 0.004F;
			if (Alt > 0.0F) {
				float f2 = 0.01666F * Alt;
				dA = dA * f2 + f1 * (1.0F - f2);
			} else {
				dA = f1;
			}
			CT.FlapsControl = 0.33F;
			if (Alt < 9F && Math.abs(Or.getKren()) < 5F && getVertSpeed() < -0.7F) Vwld.z *= 0.87000000476837158D;
		} else {
			rmax = 1.2F * Kmax * Alt;
			rmin = 0.6F * Kmax * Alt;
			if ((actor instanceof TypeGlider) && Alt > 200F) CT.RudderControl = 0.0F;
			else if (pointQuality < 50 && mn_time > 0.5F) findPointForEmLanding(f);
			if (submaneuver == 69) {
				Ve.sub(elLoc.getPoint(), Loc);
				double d = Ve.length();
				Or.transformInv(Ve);
				Ve.normalize();
				float f3 = 0.9F - 0.005F * Alt;
				if (f3 < -1F) f3 = -1F;
				if (f3 > 0.8F) f3 = 0.8F;
				if ((double) (rmax * 2.0F) < d || d < (double) rmin || Ve.x < (double) f3) {
					submaneuver = 0;
					initTargPoint(f);
				}
				if (d > 88D) {
					emergencyTurnToDirection(f);
					if ((double) Alt > d) submaneuver = 0;
				} else {
					CT.AileronControl = -0.04F * Or.getKren();
				}
			} else {
				CT.AileronControl = -0.04F * Or.getKren();
			}
			if (Or.getTangage() > -1F) dA -= 0.1F * (Or.getTangage() + 1.0F);
			if (Or.getTangage() < -10F) dA -= 0.1F * (Or.getTangage() + 10F);
		}
		if (Alt < 2.0F || Gears.onGround()) dA = -2F * (Or.getTangage() - Gears.Pitch);
		double d1 = Vwld.length() / (double) Vmin;
		if (d1 > 1.0D) d1 = 1.0D;
		CT.ElevatorControl += (double) ((dA - CT.ElevatorControl) * 3.33F * f) + 1.5D * getW().y * d1 + 0.5D * getAW().y * d1;
	}

	public boolean canITakeoff() {
		Po.set(Loc);
		Vpl.set(69D, 0.0D, 0.0D);
		Or.transform(Vpl);
		Po.add(Vpl);
		Pd.set(Po);
		if (actor == War.getNearestFriendAtPoint(Pd, (Aircraft) actor, 70F)) {
			if (canTakeoff) return true;
			if (Actor.isAlive(AP.way.takeoffAirport)) {
				if (AP.way.takeoffAirport.takeoffRequest <= 0) {
					AP.way.takeoffAirport.takeoffRequest = 2000;
					canTakeoff = true;
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public void set_maneuver_imm(int i) {
		int j = maneuver;
		maneuver = i;
		if (j != maneuver) set_flags();
	}

	private void addWindCorrection() {
		if (!World.cur().diffCur.Wind_N_Turbulence) return;
		if (World.wind().noWind) return;

		double d = Ve.length();
		World.cur();
		World.wind().getVectorAI(actor.pos.getAbsPoint(), windV);
		windV.scale(-1D);
		Ve.normalize();
		Ve.scale(getSpeed());
		Ve.add(windV);
		Ve.normalize();
		Ve.scale(d);
	}

	public boolean canHideInClouds() {
		if (Mission.curCloudsType() >= 4) {
			float f = 1.0F;
			if (actor instanceof TypeFighter) f = 2.0F;
			float f1 = (float) Loc.z - Mission.curCloudsHeight();
			if (f1 >= 0.0F && f1 < 2000F * f) return true;
			if (f1 < 0.0F && f1 > -1000F * f) return true;
		}
		return false;
	}

	private void checkBlindSpots() {
		if ((maneuver == 21 && Group.nOfAirc == 1 || maneuver == 24 && Group.airc[Group.nOfAirc - 1] == actor) && (actor instanceof TypeFighter) && !isBusy() && crew == 1) {
			float f = AP.getWayPointDistance();
			if (f < 1000F) return;
			if ((double) AS.getPilotHealth(0) < 0.20000000000000001D || !isCapableOfACM()) return;
			if (first) lookAroundTime = Time.current() + (long) World.Rnd().nextInt(5000, 0x186a0 - Skill * 20000);
			if (lookAroundTime < Time.current()) {
				if (Math.abs(Or.getRoll() - 360F) < 10F && Math.abs(Or.getPitch() - 360F) < 20F) {
					byte byte0 = 1;
					byte byte1 = 7;
					if (Alt < 700F) byte0 = 4;
					if (byte0 < byte1) switch (World.Rnd().nextInt(byte0, byte1)) {
					case 2: // '\002'
						push();
						push(90);
						pop();
						break;

					case 3: // '\003'
						push();
						push(91);
						pop();
						break;

					case 4: // '\004'
					case 5: // '\005'
						push();
						push(88);
						pop();
						break;

					case 6: // '\006'
					case 7: // '\007'
						push();
						push(89);
						pop();
						break;
					}
				}
				boolean flag = Front.isCaptured(actor);
				int i = 60000 - Skill * 15000;
				int j = 0x30d40 - Skill * 55000;
				if (!flag && Front.markers().size() > 0) j *= 2;
				lookAroundTime = Time.current() + (long) World.Rnd().nextInt(i, j);
			}
		}
	}

	private void planAlternativeTaxiRoute(Point3d point3d, float f, Actor actor) {
		if (curAirdromePoi >= taxiToTakeOffWay.size()) return;
		Point_Any point_any = null;
		Point_Any point_any1 = null;
		Point_Any point_any2 = wayCurPos;
		int i = 0;
		float f1 = 2.0F;
		int j = 0;
		boolean flag = false;
		Iterator iterator = taxiToTakeOffWay.iterator();
		do {
			if (!iterator.hasNext()) break;
			Point_Any point_any3 = (Point_Any) iterator.next();
			if (++i >= curAirdromePoi) {
				tmpLoc.set(point_any3.x, point_any3.y, point3d.z);
				float f3 = (float) point3d.distance(tmpLoc);
				if (f3 < f) {
					if (!flag) j = i;
					flag = true;
					iterator.remove();
				} else if (point_any1 == null && flag) point_any1 = point_any3;
			}
		} while (true);
		if (j != 0) {
			Iterator iterator1 = taxiToTakeOffWay.iterator();
			do {
				if (!iterator1.hasNext()) break;
				i++;
				iterator1.next();
				if (i > curAirdromePoi && i < j) iterator1.remove();
			} while (true);
		}
		point_any = new Point_Any(this.actor.pos.getAbs().getX(), this.actor.pos.getAbs().getY());
		if (point_any1 == null) point_any1 = point_any2;
		tmpLoc.set(point_any1.x, point_any1.y, point3d.z);
		float f2 = (float) point3d.distance(tmpLoc);
		tmpLoc.set(point_any.x, point_any.y, point3d.z);
		float f4 = (float) tmpLoc.distance(point3d);
		if (f2 < f * f1) {
			Point_Any point_any5 = getEvadePoint(new Point2f((float) point3d.x, (float) point3d.y), f, actor);
			taxiToTakeOffWay.add(curAirdromePoi - 1, point_any5);
			wayCurPos = point_any5;
		} else {
			Vector2f vector2f = new Vector2f();
			vector2f.sub(point_any1, point_any);
			vector2f.normalize();
			vector2f.scale(f4 + f * f1);
			Point_Any point_any6 = new Point_Any(point_any.x, point_any.y);
			point_any6.add(vector2f);
			Point_Any point_any7 = getEvadePoint(new Point2f((float) point3d.x, (float) point3d.y), f, actor);
			taxiToTakeOffWay.add(curAirdromePoi - 1, point_any7);
			taxiToTakeOffWay.add(curAirdromePoi, point_any6);
			wayCurPos = point_any7;
		}
	}

	private Point_Any getEvadePoint(Point2f point2f, float f, Actor actor) {
		Vector2f vector2f = new Vector2f();
		vector2f.sub(point2f, Pcur);
		float f1 = vector2f.length();
		float f2 = f + Wingspan * 0.5F;
		float f3 = (float) Math.atan(f2 / f1);
		if (evadeFromRightSide(point2f)) f3 *= -1F;
		Point_Any point_any = getPoint(vector2f, f3);
		Pd.set(point_any.x, point_any.y, Loc.z);
		Actor actor1 = War.getNearestAnyFriendAtPoint(Pd, (Aircraft) this.actor, Wingspan * 1.5F);
		if (actor1 != null && this.actor != actor1 && actor1 != actor) {
			if ((actor1 instanceof Aircraft) && ((Maneuver) ((Aircraft) actor1).FM).Group == Group) return point_any;
			else return getPoint(vector2f, f3 * -1F);
		} else {
			return point_any;
		}
	}

	private Point_Any getPoint(Vector2f vector2f, float f) {
		float f1 = RAD2DEG(f);
		Orient orient = new Orient();
		orient.setYPR(f1, 0.0F, 0.0F);
		tmpV3f.set(vector2f.x, vector2f.y, 0.0D);
		orient.transform(tmpV3f);
		float f2 = (float) ((double) vector2f.length() / Math.cos(f));
		tmpV3f.normalize();
		tmpV3f.scale(f2);
		tmpLoc.set(Pcur.x, Pcur.y, 0.0D);
		tmpLoc.add(tmpV3f);
		return new Point_Any(tmpLoc.x, tmpLoc.y);
	}

	private static boolean evadeFromRightSide(Point2f point2f) {
		float f = 360F - (float) RAD2DEG(Vcur.direction());
		Vector2f vector2f = new Vector2f();
		vector2f.sub(point2f, Pcur);
		float f1 = 360F - vector2f.direction();
		if (f > f1) return true;
		else return f + 180F < f1;
	}

	private boolean isStopOnStalemate(Aircraft aircraft) {
		if (!aircraft.isAlive() || aircraft.isDestroyed() || !aircraft.FM.isCapableOfTaxiing() || aircraft.FM.isCrashedOnGround()) return false;
		if (collisionDangerActor != null && taxiToTakeOffWay.size() - curAirdromePoi > 1 && collisionMap.containsKey(collisionDangerActor)) {
			Object obj = collisionMap.get(collisionDangerActor);
			Object aobj[] = (Object[]) (Object[]) obj;
			Point3d point3d = (Point3d) aobj[0];
			if (point3d.x == 0.0D && point3d.y == 0.0D && point3d.z == 0.0D) return false;
		}
		if (taxiToTakeOffWay.size() - curAirdromePoi > 2) {
			if (collisionDangerActor == aircraft && ((Maneuver) aircraft.FM).collisionDangerActor == null) return true;
			if (collisionDangerActor == null && ((Maneuver) aircraft.FM).collisionDangerActor == actor) return false;
		}
		if (Group == ((Maneuver) aircraft.FM).Group) {
			int i = Group.numInGroup((Aircraft) actor);
			int j = Group.numInGroup(aircraft);
			return i > j;
		}
		if (((Maneuver) aircraft.FM).taxiToTakeOffWay == null) return true;
		if (taxiToTakeOffWay.size() - curAirdromePoi < ((Maneuver) aircraft.FM).taxiToTakeOffWay.size() - ((Maneuver) aircraft.FM).curAirdromePoi) return false;
		if (taxiToTakeOffWay.size() - curAirdromePoi == ((Maneuver) aircraft.FM).taxiToTakeOffWay.size() - ((Maneuver) aircraft.FM).curAirdromePoi) {
			if (Wingspan < aircraft.FM.Wingspan && taxiToTakeOffWay.size() - curAirdromePoi > 0) return false;
			if (Wingspan == aircraft.FM.Wingspan || taxiToTakeOffWay.size() - curAirdromePoi == 0) {
				if (distToTaxiPoint < ((Maneuver) aircraft.FM).distToTaxiPoint) return false;
				if (distToTaxiPoint == ((Maneuver) aircraft.FM).distToTaxiPoint) {
					if (aircraft.pos.getAbs().getX() > actor.pos.getAbs().getX()) return true;
					return aircraft.pos.getAbs().getX() == actor.pos.getAbs().getX() && aircraft.pos.getAbs().getY() > actor.pos.getAbs().getY();
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	private boolean isTaxingCollisionDanger() {
		if (isTick(64, 0)) cleanCollisionMap();
		if (collisionMap.containsKey(this.actor)) {
			Object obj = collisionMap.get(this.actor);
			Object aobj[] = (Object[]) (Object[]) obj;
			Point3d point3d = (Point3d) aobj[0];
			if (point3d.x == 0.0D && point3d.y == 0.0D && point3d.z == 0.0D) {
				Long long1 = (Long) aobj[1];
				if (Time.current() - long1.longValue() < 5000L) return true;
			}
		}
		collisionDangerActor = null;
		float f = cvt(Length, 8F, 20F, 30F, 45F);
		float f1 = CT.RudderControl * 0.1F;
		float f2 = f1 * -(f * 5F);
		float f3 = Math.abs(f1) * (f * 7F);
		float f4 = f - f3;
		Point3d point3d1 = updateCollisionMap(f4, f2);
		Set set = collisionMap.keySet();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			Object obj1 = iterator.next();
			if (obj1 != this.actor) {
				Object obj2 = collisionMap.get(obj1);
				Object aobj1[] = (Object[]) (Object[]) obj2;
				Point3d point3d2 = (Point3d) aobj1[0];
				float f6 = (float) point3d1.distance(point3d2);
				if (f6 < f) {
					Aircraft aircraft1 = (Aircraft) obj1;
					if (isStopOnStalemate(aircraft1)) {
						point3d1.set(0.0D, 0.0D, 0.0D);
						collisionDangerActor = aircraft1;
						return true;
					}
				}
			}
		}

		byte byte0 = 3;
		for (int i = 1; i <= byte0; i++) {
			Po.set(Loc);
			float f5 = Math.min(f4 * (float) i * 0.666F, distToTaxiPoint + Wingspan * 0.5F);
			Vpl.set(f5, f2, 0.0D);
			Or.transform(Vpl);
			Po.add(Vpl);
			Po.z = Loc.z;
			Pd.set(Po);
			Actor actor = War.getNearestAnyFriendAtPoint(Pd, (Aircraft) this.actor, Math.min(Wingspan * 1.2F, f5));
			if (actor != null && this.actor != actor && actor != ignoredActor) {
				collisionDangerActor = actor;
				if (actor instanceof Aircraft) {
					Aircraft aircraft = (Aircraft) actor;
					if (!aircraft.isAlive() || aircraft.isDestroyed() || !aircraft.FM.isCapableOfTaxiing() || aircraft.FM.isCrashedOnGround()) {
						planAlternativeTaxiRoute(aircraft.pos.getAbsPoint(), aircraft.FM.Wingspan, actor);
						ignoredActor = actor;
						return false;
					}
					if (((Maneuver) aircraft.FM).collisionDangerActor == this.actor) return isStopOnStalemate(aircraft);
					if (((Maneuver) aircraft.FM).distToTaxiPoint == -1F) continue;
				} else if (!actor.isAlive() || actor.isDestroyed() || actor.getSpeed(Vpl) < 0.10000000149011612D) {
					planAlternativeTaxiRoute(actor.pos.getAbsPoint(), actor.collisionR() * 5F, actor);
					ignoredActor = actor;
					return false;
				}
				return true;
			}
			if (i != byte0 || !isTick(2, 0)) continue;
			List list = ActorCrater.getCraters();
			for (int j = 0; j < list.size(); j++) {
				Actor actor1 = (Actor) list.get(j);
				if (actor1 == ignoredActor) continue;
				double d = Pd.distance(actor1.pos.getAbsPoint());
				if (d < (double) (f4 * 0.5F)) {
					planAlternativeTaxiRoute(actor1.pos.getAbsPoint(), Wingspan * 0.7F, actor1);
					ignoredActor = actor1;
					return false;
				}
			}

		}

		return false;
	}

	public static void updateCollisionMap(FlightModelMain flightmodelmain) {
		float f = cvt(flightmodelmain.Length, 8F, 20F, 25F, 40F);
		float f1 = (float) flightmodelmain.getW().z * -(f * 5F);
		float f2 = Math.abs((float) flightmodelmain.getW().z) * (f * 7F);
		float f3 = f - f2;
		if (f3 < 1.0F) f3 = 1.0F;
		Po.set(flightmodelmain.Loc);
		Vpl.set(f3 * 1.25F, f1, 0.0D);
		flightmodelmain.Or.transform(Vpl);
		Po.add(Vpl);
		Po.z = flightmodelmain.Loc.z;
		Point3d point3d = new Point3d();
		point3d.set(Po);
		Object aobj[] = new Object[2];
		aobj[0] = point3d;
		aobj[1] = new Long(Time.current());
		collisionMap.put(flightmodelmain.actor, ((Object) (aobj)));
	}

	private Point3d updateCollisionMap(float f, float f1) {
		Po.set(Loc);
		Vpl.set(f * 1.25F, f1, 0.0D);
		Or.transform(Vpl);
		Po.add(Vpl);
		Po.z = Loc.z;
		Point3d point3d = new Point3d();
		point3d.set(Po);
		Object aobj[] = new Object[2];
		aobj[0] = point3d;
		aobj[1] = new Long(Time.current());
		collisionMap.put(actor, ((Object) (aobj)));
		return point3d;
	}

	private void cleanCollisionMap() {
		Set set = collisionMap.keySet();
		Iterator iterator = set.iterator();
		do {
			if (!iterator.hasNext()) break;
			Object obj = iterator.next();
			Object obj1 = collisionMap.get(obj);
			Object aobj[] = (Object[]) (Object[]) obj1;
			long l = ((Long) aobj[1]).longValue();
			if (Time.current() - l > 30000L) iterator.remove();
		} while (true);
	}

	private void cleanCollisionMap(Actor actor) {
		Set set = collisionMap.keySet();
		Iterator iterator = set.iterator();
		do {
			if (!iterator.hasNext()) break;
			Object obj = iterator.next();
			if (obj == actor) iterator.remove();
		} while (true);
	}

	public static void clear() {
		collisionMap.clear();
	}

	private void setTurn(float f, float f1, float f2) {
		CT.setPowerControl(1.1F);
		setSpeedMode(11);
		tmpOr.setYPR(Or.getYaw(), 0.0F, 0.0F);
		Ve.set(f, f1, f2);
		tmpOr.transform(Ve);
		Or.transformInv(Ve);
		Ve.normalize();
		farTurnToDirection(10F);
	}

	private boolean isEnableToTaxi() {
		if (CT.bHasGearControl) return CT.getGearL() == 1.0F && CT.getGearR() == 1.0F;
		else return true;
	}

	// TODO: +++ CTO Mod 4.12 +++
	public static Vector3d getvTemp() {
		return vTemp;
	}

	public static void setvTemp(Vector3d vTemp) {
		Maneuver.vTemp = vTemp;
	}

	public Loc gettLoc() {
		return tLoc;
	}

	public void settLoc(Loc tLoc) {
		this.tLoc = tLoc;
	}

	public static Point3d gettPoint() {
		return tPoint;
	}

	public static void settPoint(Point3d tPoint) {
		Maneuver.tPoint = tPoint;
	}

	public static Vector2d getVdiff() {
		return Vdiff;
	}

	public static void setVdiff(Vector2d vdiff) {
		Vdiff = vdiff;
	}

	public static AnglesFork getSteerAngleFork() {
		return steerAngleFork;
	}

	public static void setSteerAngleFork(AnglesFork steerAngleFork) {
		Maneuver.steerAngleFork = steerAngleFork;
	}

	public static int getVolleyl() {
		return volleyL;
	}

	public float getOldAOA() {
		return oldAOA;
	}

	public void setOldAOA(float oldAOA) {
		this.oldAOA = oldAOA;
	}

	public static int getStayOnTheTail() {
		return STAY_ON_THE_TAIL;
	}

	public static int getNotTooFast() {
		return NOT_TOO_FAST;
	}

	public static int getFromWaypoint() {
		return FROM_WAYPOINT;
	}

	public static int getConstSpeed() {
		return CONST_SPEED;
	}

	public static int getMaxSpeed() {
		return MAX_SPEED;
	}

	public static int getMinSpeed() {
		return MIN_SPEED;
	}

	public static int getConstPower() {
		return CONST_POWER;
	}

	public static int getBoostOn() {
		return BOOST_ON;
	}

	public static int getFollowWithoutFlaps() {
		return FOLLOW_WITHOUT_FLAPS;
	}

	public static int getBoostFull() {
		return BOOST_FULL;
	}

	public static int getLevelPlane() {
		return LEVEL_PLANE;
	}

	public static int getRoll() {
		return ROLL;
	}

	public static int getRoll180() {
		return ROLL_180;
	}

	public static int getSpiralBrake() {
		return SPIRAL_BRAKE;
	}

	public static int getRoll90() {
		return ROLL_90;
	}

	public static int getSpiralDown() {
		return SPIRAL_DOWN;
	}

	public static int getClimb() {
		return CLIMB;
	}

	public static int getDiving0Rpm() {
		return DIVING_0_RPM;
	}

	public static int getDiving45Deg() {
		return DIVING_45_DEG;
	}

	public static int getTurn() {
		return TURN;
	}

	public static int getMilTurn() {
		return MIL_TURN;
	}

	public static int getLoop() {
		return LOOP;
	}

	public static int getLoopDown() {
		return LOOP_DOWN;
	}

	public static int getHalfLoopUp() {
		return HALF_LOOP_UP;
	}

	public static int getHalfLoopDown() {
		return HALF_LOOP_DOWN;
	}

	public static int getBell() {
		return BELL;
	}

	public static int getWaveout() {
		return WAVEOUT;
	}

	public static int getSinus() {
		return SINUS;
	}

	public static int getZigzagUp() {
		return ZIGZAG_UP;
	}

	public static int getZigzagDown() {
		return ZIGZAG_DOWN;
	}

	public static int getZigzagSpit() {
		return ZIGZAG_SPIT;
	}

	public static int getHalfLoopDown135() {
		return HALF_LOOP_DOWN_135;
	}

	public static int getHartmannRedout() {
		return HARTMANN_REDOUT;
	}

	public static int getStallPokryshkin() {
		return STALL_POKRYSHKIN;
	}

	public static int getBarrelPokryshkin() {
		return BARREL_POKRYSHKIN;
	}

	public static int getSlideLevel() {
		return SLIDE_LEVEL;
	}

	public static int getSlideDescent() {
		return SLIDE_DESCENT;
	}

	public static int getRanversman() {
		return RANVERSMAN;
	}

	public static int getCuban() {
		return CUBAN;
	}

	public static int getCubanInvert() {
		return CUBAN_INVERT;
	}

	public static int getFollowSpiralUp() {
		return FOLLOW_SPIRAL_UP;
	}

	public static int getSinusShallow() {
		return SINUS_SHALLOW;
	}

	public static int getSeparate() {
		return SEPARATE;
	}

	public static int getTaxi() {
		return TAXI;
	}

	public static int getRunAway() {
		return RUN_AWAY;
	}

	public static int getFarCover() {
		return FAR_COVER;
	}

	public static int getTakeoffVtolA() {
		return TAKEOFF_VTOL_A;
	}

	public static int getLandingVtolA() {
		return LANDING_VTOL_A;
	}

	public static int getAttackFromPlayer() {
		return ATTACK_FROM_PLAYER;
	}

	public static int getTurnHard() {
		return TURN_HARD;
	}

	public static int getClouds() {
		return CLOUDS;
	}

	public static int getEvadeAttack() {
		return EVADE_ATTACK;
	}

	public static int getBracketAttack() {
		return BRACKET_ATTACK;
	}

	public static int getBarrelRoll() {
		return BARREL_ROLL;
	}

	public static int getDiving90Deg() {
		return DIVING_90_DEG;
	}

	public static int getSmoothLevel() {
		return SMOOTH_LEVEL;
	}

	public static int getIvan() {
		return IVAN;
	}

	public static int getBangBang() {
		return BANG_BANG;
	}

	public static int getPanicManic() {
		return PANIC_MANIC;
	}

	public static int getPanicFreeze() {
		return PANIC_FREEZE;
	}

	public static int getCombatClimb() {
		return COMBAT_CLIMB;
	}

	public static int getHitAndRun() {
		return HIT_AND_RUN;
	}

	public static int getSeekAndDestroy() {
		return SEEK_AND_DESTROY;
	}

	public static int getBreakAway() {
		return BREAK_AWAY;
	}

	public static int getAttackHard() {
		return ATTACK_HARD;
	}

	public static int getMilTurnLeft() {
		return MIL_TURN_LEFT;
	}

	public static int getMilTurnRight() {
		return MIL_TURN_RIGHT;
	}

	public static int getStraightAndLevel() {
		return STRAIGHT_AND_LEVEL;
	}

	public int getmaneuver() {
		return maneuver;
	}

	public int getsubmaneuver() {
		return submaneuver;
	}

	public int getspeedMode() {
		return speedMode;
	}

	public float getsmConstSpeed() {
		return smConstSpeed;
	}

	public float getsmConstPower() {
		return smConstPower;
	}

	public boolean getTaxiMode() {
		return TaxiMode;
	}

	public long getACx() {
		return (long) Loc.x;
	}

	public long getACy() {
		return (long) Loc.y;
	}

	public long getWAYPOINTx() {
		return (long) AP.way.curr().x();
	}

	public long getWAYPOINTy() {
		return (long) AP.way.curr().y();
	}

	public long getWAYPOINTz() {
		return (long) AP.way.curr().z();
	}

	public float getWAYPOINTspeed() {
		return (long) AP.way.curr().getV();
	}

	public long getWAYPOINTdistance() {
		return (long) Math.sqrt(((getWAYPOINTx() - getACx()) * (getWAYPOINTx() - getACx())) + ((getWAYPOINTy() - getACy()) * (getWAYPOINTy() - getACy())));
	}

	public int getWAYPOINTcurrentnum() {
		return AP.way.Cur();
	}
}