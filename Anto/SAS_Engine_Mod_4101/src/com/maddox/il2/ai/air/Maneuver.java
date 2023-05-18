/*Modified Maneuver class for the SAS Engine Mod*/

package com.maddox.il2.ai.air;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.fm.*;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.objects.ships.BigshipGeneric;
import com.maddox.il2.objects.sounds.SndAircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.weapons.*;
import com.maddox.rts.*;

public class Maneuver extends AIFlightModel
{
	private int					task;
	private int					maneuver					= 26;
	protected float				mn_time;
	private int[]				program						= new int[8];
	private boolean				bBusy						= true;
	public boolean				wasBusy						= true;
	public boolean				dontSwitch					= false;
	public boolean				aggressiveWingman			= false;
	public boolean				kamikaze					= false;
	public boolean				silence						= true;
	public boolean				bombsOut;
	private int					bombsOutCounter				= 0;
	public float				direction;
	public Loc					rwLoc;
	private boolean				first						= true;
	private int					rocketsDelay				= 0;
	private int					bulletDelay					= 0;
	private int					submanDelay					= 0;
	private float				maxG;
	private float				maxAOA;
	private float				LandHQ;
	public float				Alt							= 0.0F;
	private float				corrCoeff					= 0.0F;
	private float				corrElev					= 0.0F;
	private float				corrAile					= 0.0F;
	private boolean				checkGround					= false;
	private boolean				checkStrike					= false;
	private boolean				frequentControl				= false;
	private boolean				stallable					= false;
	public FlightModel			airClient					= null;
	public FlightModel			target						= null;
	public FlightModel			danger						= null;
	private float				dangerAggressiveness		= 0.0F;
	private float				oldDanCoeff					= 0.0F;
	private int					shotAtFriend				= 0;
	private float				distToFriend				= 0.0F;
	public Actor				target_ground				= null;
	public AirGroup				Group;
	protected boolean			TaxiMode					= false;
	protected boolean			finished					= false;
	protected boolean			canTakeoff					= false;
	public Point_Any			wayCurPos;
	protected Point_Any			wayPrevPos;
	protected Point_Any[]		airdromeWay;
	protected int				curAirdromePoi				= 0;
	public long					actionTimerStart			= 0L;
	public long					actionTimerStop				= 0L;
	protected int				gattackCounter				= 0;
	private int					beNearOffsPhase				= 0;
	public int					submaneuver					= 0;
	private boolean				dont_change_subm			= false;
	private int					tmpi						= 0;
	private int					sub_Man_Count				= 0;
	private float				dA;
	private float				dist						= 0.0F;
	private float				man1Dist					= 50.0F;
	private float				bullTime					= 0.0015F;
	protected static final int	STAY_ON_THE_TAIL			= 1;
	protected static final int	NOT_TOO_FAST				= 2;
	protected static final int	FROM_WAYPOINT				= 3;
	protected static final int	CONST_SPEED					= 4;
	protected static final int	MIN_SPEED					= 5;
	protected static final int	MAX_SPEED					= 6;
	protected static final int	CONST_POWER					= 7;
	protected static final int	ZERO_POWER					= 8;
	protected static final int	BOOST_ON					= 9;
	protected static final int	FOLLOW_WITHOUT_FLAPS		= 10;
	protected int				speedMode					= 3;
	protected float				smConstSpeed				= 90.0F;
	protected float				smConstPower				= 0.7F;
	protected FlightModel		tailForStaying				= null;
	public Vector3d				tailOffset					= new Vector3d();
	protected int				Speak5minutes;
	protected int				Speak1minute;
	protected int				SpeakBeginGattack;
	protected boolean			WeWereInGAttack				= false;
	protected boolean			WeWereInAttack				= false;
	protected boolean			SpeakMissionAccomplished	= false;
	public static final int		ROOKIE						= 0;
	public static final int		NORMAL						= 1;
	public static final int		VETERAN						= 2;
	public static final int		ACE							= 3;
	public static final int		NO_TASK						= 0;
	public static final int		WAIT						= 1;
	public static final int		STAY_FORMATION				= 2;
	public static final int		FLY_WAYPOINT				= 3;
	public static final int		DEFENCE						= 4;
	public static final int		DEFENDING					= 5;
	public static final int		ATTACK_AIR					= 6;
	public static final int		ATTACK_GROUND				= 7;
	public static final int		NONE						= 0;
	public static final int		HOLD						= 1;
	public static final int		PULL_UP						= 2;
	public static final int		LEVEL_PLANE					= 3;
	public static final int		ROLL						= 4;
	public static final int		ROLL_90						= 5;
	public static final int		ROLL_180					= 6;
	public static final int		SPIRAL_BRAKE				= 7;
	public static final int		SPIRAL_UP					= 8;
	public static final int		SPIRAL_DOWN					= 9;
	public static final int		CLIMB						= 10;
	public static final int		DIVING_0_RPM				= 11;
	public static final int		DIVING_30_DEG				= 12;
	public static final int		DIVING_45_DEG				= 13;
	public static final int		TURN						= 14;
	public static final int		MIL_TURN					= 15;
	public static final int		LOOP						= 16;
	public static final int		LOOP_DOWN					= 17;
	public static final int		HALF_LOOP_UP				= 18;
	public static final int		HALF_LOOP_DOWN				= 19;
	public static final int		STALL						= 20;
	public static final int		WAYPOINT					= 21;
	public static final int		SPEEDUP						= 22;
	public static final int		BELL						= 23;
	public static final int		FOLLOW						= 24;
	public static final int		LANDING						= 25;
	public static final int		TAKEOFF						= 26;
	public static final int		ATTACK						= 27;
	public static final int		WAVEOUT						= 28;
	public static final int		SINUS						= 29;
	public static final int		ZIGZAG_UP					= 30;
	public static final int		ZIGZAG_DOWN					= 31;
	public static final int		ZIGZAG_SPIT					= 32;
	public static final int		HALF_LOOP_DOWN_135			= 33;
	public static final int		HARTMANN_REDOUT				= 34;
	public static final int		ROLL_360					= 35;
	public static final int		STALL_POKRYSHKIN			= 36;
	public static final int		BARREL_POKRYSHKIN			= 37;
	public static final int		SLIDE_LEVEL					= 38;
	public static final int		SLIDE_DESCENT				= 39;
	public static final int		RANVERSMAN					= 40;
	public static final int		CUBAN						= 41;
	public static final int		CUBAN_INVERT				= 42;
	public static final int		GATTACK						= 43;
	public static final int		PILOT_DEAD					= 44;
	public static final int		HANG_ON						= 45;
	public static final int		DELAY						= 48;
	public static final int		EMERGENCY_LANDING			= 49;
	public static final int		GATTACK_DIVE				= 50;
	public static final int		GATTACK_TORPEDO				= 51;
	public static final int		GATTACK_CASSETTE			= 52;
	public static final int		GATTACK_KAMIKAZE			= 46;
	public static final int		GATTACK_TINYTIM				= 47;
	public static final int		GATTACK_HS293				= 79;
	public static final int		GATTACK_FRITZX				= 80;
	public static final int		GATTACK_TORPEDO_TOKG		= 81;
	public static final int		FAR_FOLLOW					= 53;
	public static final int		SPIRAL_DOWN_SLOW			= 54;
	public static final int		FOLLOW_SPIRAL_UP			= 55;
	public static final int		SINUS_SHALLOW				= 56;
	public static final int		GAIN						= 57;
	public static final int		SEPARATE					= 58;
	public static final int		BE_NEAR						= 59;
	public static final int		EVADE_UP					= 60;
	public static final int		EVADE_DN					= 61;
	public static final int		ENERGY_ATTACK				= 62;
	public static final int		ATTACK_BOMBER				= 63;
	public static final int		PARKED_STARTUP				= 64;
	public static final int		COVER						= 65;
	public static final int		TAXI						= 66;
	public static final int		RUN_AWAY					= 67;
	public static final int		FAR_COVER					= 68;
	public static final int		TAKEOFF_VTOL_A				= 69;
	public static final int		LANDING_VTOL_A				= 70;
    public static final int 	CLIMBFOR_ADVANTAGE 			= 71;
    public static final int		MOD_ATTACK 					= 72;
	private static final int	SUB_MAN0					= 0;
	private static final int	SUB_MAN1					= 1;
	private static final int	SUB_MAN2					= 2;
	private static final int	SUB_MAN3					= 3;
	private static final int	SUB_MAN4					= 4;
	public static final int		LIVE						= 0;
	public static final int		RETURN						= 1;
	public static final int		TASK						= 2;
	public static final int		PROTECT_LEADER				= 3;
	public static final int		PROTECT_WINGMAN				= 4;
	public static final int		PROTECT_FRIENDS				= 5;
	public static final int		DESTROY_ENEMIES				= 6;
	public static final int		KEEP_ORDER					= 7;
	public float[]				takeIntoAccount				= new float[8];
	public float[]				AccountCoeff				= new float[8];
	public static final int		FVSB_BOOM_ZOOM				= 0;
	public static final int		FVSB_BOOM_ZOOM_TO_ENGINE	= 1;
	public static final int		FVSB_SHALLOW_DIVE_TO_ENGINE	= 2;
	public static final int		FVSB_FROM_AHEAD				= 3;
	public static final int		FVSB_FROM_BELOW				= 4;
	public static final int		FVSB_AS_IT_IS				= 5;
	public static final int		FVSB_FROM_SIDE				= 6;
	public static final int		FVSB_FROM_TAIL_TO_ENGINE	= 7;
	public static final int		FVSB_FROM_TAIL				= 8;
	public static final int		FVSB_SHALLOW_DIVE			= 9;
	public static final int		FVSB_FROM_BOTTOM			= 10;
	private Vector3d			ApproachV					= new Vector3d();
	private Vector3d			TargV						= new Vector3d();
	private Vector3d			TargDevV					= new Vector3d(0.0, 0.0, 0.0);
	private Vector3d			TargDevVnew					= new Vector3d(0.0, 0.0, 0.0);
	private Vector3d			scaledApproachV				= new Vector3d();
	private float				ApproachR;
	private float				TargY;
	private float				TargZ;
	private float				TargYS;
	private float				TargZS;
	private float				RandomVal;
	public Vector3d				followOffset				= new Vector3d();
	private Vector3d			followTargShift				= new Vector3d(0.0, 0.0, 0.0);
	private Vector3d			followCurShift				= new Vector3d(0.0, 0.0, 0.0);
	private float				raAilShift					= 0.0F;
	private float				raElevShift					= 0.0F;
	private float				raRudShift					= 0.0F;
	private float				sinKren						= 0.0F;
	private boolean				strikeEmer					= false;
	private FlightModel			strikeTarg					= null;
	private boolean				direc						= false;
	float						Kmax						= 10.0F;
	float						rmin;
	float						rmax;
	double						phase						= 0.0;
	double						radius						= 50.0;
	int							pointQuality				= -1;
	int							curPointQuality				= 50;
	private static Vector3d		tmpV3d						= new Vector3d();
	private static Vector3d		tmpV3f						= new Vector3d();
	public static Orient		tmpOr						= new Orient();
	public Orient				saveOr						= new Orient();
	private static Point3d		Po							= new Point3d();
	private static Point3d		Pc							= new Point3d();
	private static Point3d		Pd							= new Point3d();
	private static Vector3d		Ve							= new Vector3d();
	private Vector3d			oldVe						= new Vector3d();
	private Vector3d			Vtarg						= new Vector3d();
	private Vector3d			constVtarg					= new Vector3d();
	private Vector3d			constVtarg1					= new Vector3d();
	private static Vector3d		Vf							= new Vector3d();
	private Vector3d			Vxy							= new Vector3d();
	private static Vector3d		Vpl							= new Vector3d();
	private AnglesFork			AFo							= new AnglesFork();
	private float[]				headPos						= new float[3];
	private float[]				headOr						= new float[3];
	private static Point3d		P							= new Point3d();
	private static Point2f		Pcur						= new Point2f();
	private static Vector2d		Vcur						= new Vector2d();
	private static Vector2f		V_to						= new Vector2f();
	private static Vector2d		Vdiff						= new Vector2d();
	private static Loc			elLoc						= new Loc();
	public static boolean		showFM;
	private float				pilotHeadT					= 0.0F;
	private float				pilotHeadY					= 0.0F;
	Vector3d					windV						= new Vector3d();
    private static Vector3f 	LandingOffset = new Vector3f(220F, 4F, 0.0F);
    private Vector3d 			strikeV;
    private static final int 	demultiplier = 4;
    private float 				oldAOA;
    private static final int 	volleyL = 120;
    private int 				curman;
    
	// TODO: CTO Mod
	// -------------------------------
    private boolean 			bStage1;
    private boolean 			bStage2;
    private boolean 			bStage3;
    private boolean 			bStage4;
    private boolean 			bStage5;
    private boolean 			bStage6;
    private boolean 			bStage7;
    private long 				lStageStartTime;
    private long 				lStage6Delay;
    private boolean 			bAlreadyCheckedStage7;
    private boolean 			bHasCatapult;
	private float				fNearestDistance;
	private boolean				bCatapultAI;
	private boolean				bNoNavLightsAI;
	private boolean				bFastLaunchAI;
	// -------------------------------

	// TODO: GATTACK
	//---------------------------------
	private int					passCounter					= 0;
	//--------------------------------

    public void set_task(int i)
    {
        task = i;
    }

    public int get_task()
    {
        return task;
    }

    public int get_maneuver()
    {
        return maneuver;
    }

    public void set_maneuver(int i)
    {
        if(maneuver != 44 && (i == 44 || maneuver != 26 && maneuver != 69 && maneuver != 66 && maneuver != 46))
        {
            int j = maneuver;
            maneuver = i;
            if(j != maneuver)
                set_flags();
        }
    }

    public void pop()
    {
        if(maneuver != 44 && (program[0] == 44 || maneuver != 26 && maneuver != 69 && maneuver != 66 && maneuver != 46))
        {
            int i = maneuver;
            maneuver = program[0];
            for(int j = 0; j < program.length - 1; j++)
                program[j] = program[j + 1];

            program[program.length - 1] = 0;
            if(i != maneuver)
                set_flags();
        }
    }

    public void unblock()
    {
        maneuver = 0;
    }

    public void safe_set_maneuver(int i)
    {
        dont_change_subm = true;
        set_maneuver(i);
        dont_change_subm = true;
    }

    public void safe_pop()
    {
        dont_change_subm = true;
        pop();
        dont_change_subm = true;
    }

    public void clear_stack()
    {
        for(int i = 0; i < program.length; i++)
            program[i] = 0;

    }

    public void push(int i)
    {
        for(int j = program.length - 2; j >= 0; j--)
            program[j + 1] = program[j];

        program[0] = i;
    }

    public void push()
    {
        push(maneuver);
    }

    public void accurate_set_task_maneuver(int i, int j)
    {
        if(maneuver != 44 && maneuver != 26 && maneuver != 69 && maneuver != 64)
        {
            set_task(i);
            if(maneuver != j)
            {
                clear_stack();
                set_maneuver(j);
            }
        }
    }

    public void accurate_set_FOLLOW()
    {
        if(maneuver != 44 && maneuver != 26 && maneuver != 69 && maneuver != 64)
        {
            set_task(2);
            if(maneuver != 24 && maneuver != 53)
            {
                clear_stack();
                set_maneuver(24);
            }
        }
    }

    public void setBusy(boolean flag)
    {
        bBusy = flag;
    }

    public boolean isBusy()
    {
        return bBusy;
    }

    public void setSpeedMode(int i)
    {
        speedMode = i;
    }

    private boolean isStallable()
    {
        if(super.actor instanceof TypeStormovik)
            return false;
        else
            return !(super.actor instanceof TypeTransport);
    }

    private void resetControls()
    {
    	//TODO: Added in blown flaps control
        CT.AileronControl = CT.BrakeControl = CT.ElevatorControl = CT.FlapsControl = CT.BlownFlapsControl = CT.RudderControl = 0.0F;
        CT.AirBrakeControl = 0.0F;
    }

    private void set_flags()
    {
        if(World.cur().isDebugFM())
            printDebugFM();
        super.AP.setStabAll(false);
        mn_time = 0.0F;
        super.minElevCoeff = 4F;
        if(!dont_change_subm)
        {
            setSpeedMode(3);
            first = true;
            submaneuver = 0;
            sub_Man_Count = 0;
        }
        dont_change_subm = false;
        if(maneuver != 48 && maneuver != 0 && maneuver != 26 && maneuver != 64 && maneuver != 44)
            resetControls();
        if(maneuver == 20 || maneuver == 25 || maneuver == 66 || maneuver == 1 || maneuver == 26 || maneuver == 69 || maneuver == 44 || maneuver == 49 || maneuver == 43 || maneuver == 50 || maneuver == 51 || maneuver == 46 || maneuver == 2 || maneuver == 10 || maneuver == 57 || maneuver == 64)
            setCheckGround(false);
        else
            setCheckGround(true);
        if(maneuver == 24 || maneuver == 53 || maneuver == 68 || maneuver == 59 || maneuver == 8 || maneuver == 55 || maneuver == 27 || maneuver == 62 || maneuver == 63 || maneuver == 25 || maneuver == 66 || maneuver == 43 || maneuver == 50 || maneuver == 65 || maneuver == 44 || maneuver == 21 || maneuver == 64 || maneuver == 69)
            frequentControl = true;
        else
            frequentControl = false;
        if(((Tuple3f) (World.Sun().ToSun)).z < -0.22F && super.AP.way.isLanding())
            turnOnChristmasTree(true);
        else
            turnOnChristmasTree(false);
        if(maneuver == 25 && ((Tuple3f) (World.Sun().ToSun)).z < -0.22F && Alt < 50F)
            turnOnCloudShine(true);
        else
            turnOnCloudShine(false);
        if(maneuver == 60 || maneuver == 61 || maneuver == 66 || maneuver == 1 || maneuver == 24 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 44)
            checkStrike = false;
        else
            checkStrike = true;
        if(maneuver == 44 || maneuver == 1 || maneuver == 48 || maneuver == 0 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 43 || maneuver == 50 || maneuver == 51 || maneuver == 52 || maneuver == 47)
            stallable = false;
        else
            stallable = true;
        if(maneuver == 44 || maneuver == 1 || maneuver == 26 || maneuver == 69 || maneuver == 64 || maneuver == 2 || maneuver == 57 || maneuver == 60 || maneuver == 61 || maneuver == 43 || maneuver == 50 || maneuver == 51 || maneuver == 52 || maneuver == 47 || maneuver == 29)
            setBusy(true);
    }

    public void setCheckStrike(boolean flag)
    {
        checkStrike = flag;
    }

    private void setCheckGround(boolean flag)
    {
        checkGround = flag;
    }

    public Maneuver(String s)
    {
        super(s);
        maneuver = 26;
        program = new int[8];
        bBusy = true;
        wasBusy = true;
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
        danger = null;
        dangerAggressiveness = 0.0F;
        oldDanCoeff = 0.0F;
        shotAtFriend = 0;
        distToFriend = 0.0F;
        target_ground = null;
        TaxiMode = false;
        finished = false;
        canTakeoff = false;
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
        direc = false;
        Kmax = 10F;
        phase = 0.0D;
        radius = 50D;
        pointQuality = -1;
        curPointQuality = 50;
        saveOr = new Orient();
        oldVe = new Vector3d();
        Vtarg = new Vector3d();
        constVtarg = new Vector3d();
        constVtarg1 = new Vector3d();
        Vxy = new Vector3d();
        AFo = new AnglesFork();
        headPos = new float[3];
        headOr = new float[3];
        pilotHeadT = 0.0F;
        pilotHeadY = 0.0F;
        windV = new Vector3d();
        passCounter = 0;
        bStage1 = false;
        bStage2 = false;
        bStage3 = false;
        bStage4 = false;
        bStage5 = false;
        bStage6 = false;
        bStage7 = false;
        bAlreadyCheckedStage7 = false;
        bHasCatapult = false;
        bNoNavLightsAI = false;
        bFastLaunchAI = false;
        maneuver = 26;
        program = new int[8];
        bBusy = true;
        wasBusy = true;
        dontSwitch = false;
        aggressiveWingman = false;
        kamikaze = false;
        silence = true;
        bombsOutCounter = 0;
        passCounter = 0;
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
        danger = null;
        dangerAggressiveness = 0.0F;
        oldDanCoeff = 0.0F;
        shotAtFriend = 0;
        distToFriend = 0.0F;
        target_ground = null;
        TaxiMode = false;
        finished = false;
        canTakeoff = false;
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
        strikeV = new Vector3d();
        direc = false;
        Kmax = 10F;
        phase = 0.0D;
        radius = 50D;
        pointQuality = -1;
        curPointQuality = 50;
        saveOr = new Orient();
        oldVe = new Vector3d();
        Vtarg = new Vector3d();
        constVtarg = new Vector3d();
        constVtarg1 = new Vector3d();
        Vxy = new Vector3d();
        AFo = new AnglesFork();
        headPos = new float[3];
        headOr = new float[3];
        pilotHeadT = 0.0F;
        pilotHeadY = 0.0F;
        super.AP = new AutopilotAI(this);
    }

    public void decDangerAggressiveness()
    {
        dangerAggressiveness -= 0.01F;
        if(dangerAggressiveness < 0.0F)
            dangerAggressiveness = 0.0F;
        oldDanCoeff -= 0.005F;
        if(oldDanCoeff < 0.0F)
            oldDanCoeff = 0.0F;
    }

    public void incDangerAggressiveness(int i, float f, float f1, FlightModel flightmodel)
    {
        f -= 0.7F;
        if(f < 0.0F)
            f = 0.0F;
        f1 = 1000F - f1;
        if(f1 < 0.0F)
            f1 = 0.0F;
        double d = (double)(((FlightModelMain) (flightmodel)).Energy - super.Energy) * 0.1019D;
        double d1 = 1.0D + d * 0.00125D;
        if(d1 > 1.2D)
            d1 = 1.2D;
        if(d1 < 0.80000000000000004D)
            d1 = 0.80000000000000004D;
        float f2 = (float)d1 * (7E-005F * f * f1);
        if(danger == null || f2 > oldDanCoeff)
            danger = flightmodel;
        oldDanCoeff = f2;
        dangerAggressiveness += f2 * (float)i;
        if(dangerAggressiveness > 1.0F)
            dangerAggressiveness = 1.0F;
    }

    public float getDangerAggressiveness()
    {
        return dangerAggressiveness;
    }

    public float getMaxHeightSpeed(float f)
    {
        if(f < super.HofVmax)
            return super.Vmax + (super.VmaxH - super.Vmax) * (f / super.HofVmax);
        float f1 = (f - super.HofVmax) / super.HofVmax;
        f1 = 1.0F - 1.5F * f1;
        if(f1 < 0.0F)
            f1 = 0.0F;
        return super.VmaxH * f1;
    }

    public float getMinHeightTurn(float f)
    {
        return super.Wing.T_turn;
    }

    public void setShotAtFriend(float f)
    {
        distToFriend = f;
        shotAtFriend = 30;
    }

    public boolean hasCourseWeaponBullets()
    {
    	//TODO: DBW AI Mod edits
        if(super.actor instanceof KI_46_OTSUHEI)
            return super.CT.Weapons[1] != null && super.CT.Weapons[1][0] != null && super.CT.Weapons[1][0].countBullets() != 0;
        if(super.actor instanceof TypeJazzPlayer)
            return super.CT.Weapons[1] != null && super.CT.Weapons[1][0] != null && super.CT.Weapons[1][0].countBullets() != 0;
        if(super.actor instanceof AR_234)
            return false;
        return super.CT.Weapons[0] != null && super.CT.Weapons[0][0] != null && super.CT.Weapons[0][0].countBullets() != 0 || super.CT.Weapons[1] != null && super.CT.Weapons[1][0] != null && super.CT.Weapons[1][0].countBullets() != 0;
    }

    public boolean hasBombs()
    {
        if(super.CT.Weapons[3] != null)
        {
            for(int i = 0; i < super.CT.Weapons[3].length; i++)
                if(super.CT.Weapons[3][i] != null && super.CT.Weapons[3][i].countBullets() != 0)
                    return true;

        }
        return false;
    }

    public boolean hasRockets()
    {
        if(super.CT.Weapons[2] != null)
        {
            for(int i = 0; i < super.CT.Weapons[2].length; i++)
                if(super.CT.Weapons[2][i] != null && super.CT.Weapons[2][i].countBullets() != 0)
                    return true;

        }
        return false;
    }

    public boolean canAttack()
    {
        return (!Group.isWingman(Group.numInGroup((Aircraft)super.actor)) || aggressiveWingman) && isOk() && hasCourseWeaponBullets();
    }

    public void update(float f)
    {
        super.actor = super.actor;
        if(Config.isUSE_RENDER())
            headTurn(f);
        if(showFM)
            OutCT(20);
        super.update(f);
        super.callSuperUpdate = true;
        decDangerAggressiveness();
        if(((Tuple3d) (super.Loc)).z < -20D)
            ((Aircraft)super.actor).postEndAction(0.0D, super.actor, 4, null);
        LandHQ = (float)Engine.land().HQ_Air(((Tuple3d) (super.Loc)).x, ((Tuple3d) (super.Loc)).y);
        Po.set(super.Vwld);
        Po.scale(3D);
        Po.add(super.Loc);
        LandHQ = (float)Math.max(LandHQ, Engine.land().HQ_Air(((Tuple3d) (Po)).x, ((Tuple3d) (Po)).y));
        Alt = (float)((Tuple3d) (super.Loc)).z - LandHQ;
        super.indSpeed = getSpeed() * (float)Math.sqrt(super.Density / 1.225F);
        if(!super.Gears.onGround() && isOk() && Alt > 8F)
        {
            if(super.AOA > super.AOA_Crit - 2.0F)
                super.Or.increment(0.0F, 0.05F * (super.AOA_Crit - 2.0F - super.AOA), 0.0F);
            if(super.AOA < -5F)
                super.Or.increment(0.0F, 0.05F * (-5F - super.AOA), 0.0F);
        }
        if(frequentControl || (Time.tickCounter() + super.actor.hashCode()) % 4 == 0)
        {
            turnOffTheWeapon();
            maxG = 3.5F + (float)super.Skill * 0.5F;
            super.Or.wrap();
            if(mn_time > 10F && super.AOA > super.AOA_Crit + 5F && isStallable() && stallable)
                safe_set_maneuver(20);
            switch(maneuver)
            {
            case 58: // ':'
            case 73: // 'I'
            case 74: // 'J'
            case 75: // 'K'
            case 76: // 'L'
            case 77: // 'M'
            case 78: // 'N'
            default:
                break;

            case 0: // '\0'
                target_ground = null;
                break;

            case 1: // '\001'
                super.dryFriction = 8F;
                super.CT.FlapsControl = 0.0F;
                super.CT.BrakeControl = 1.0F;
                break;

            case 48: // '0'
                if(mn_time >= 1.0F)
                    pop();
                break;

            case 44: // ','
                if(super.Gears.onGround() && super.Vwld.length() < 0.30000001192092896D && World.Rnd().nextInt(0, 99) < 4)
                {
                    if(Group != null)
                        Group.delAircraft((Aircraft)super.actor);
                    if((super.actor instanceof TypeGlider) || (super.actor instanceof TypeSailPlane))
                        break;
                    World.cur();
                    if(super.actor != World.getPlayerAircraft())
                        if(Airport.distToNearestAirport(super.Loc) > 900D)
                            ((Aircraft)super.actor).postEndAction(60D, super.actor, 3, null);
                        else
                            MsgDestroy.Post(Time.current() + 30000L, super.actor);
                }
                if(first)
                {
                    super.AP.setStabAll(false);
                    super.CT.ElevatorControl = World.Rnd().nextFloat(-0.07F, 0.4F);
                    super.CT.AileronControl = World.Rnd().nextFloat(-0.15F, 0.15F);
                }
                break;

            case 7: // '\007'
                wingman(false);
                setSpeedMode(9);
                if(((Tuple3d) (getW())).x <= 0.0D)
                {
                    super.CT.AileronControl = -1F;
                    super.CT.RudderControl = 1.0F;
                } else
                {
                    super.CT.AileronControl = 1.0F;
                    super.CT.RudderControl = -1F;
                }
                float f1 = super.Or.getKren();
                if(f1 > -90F && f1 < 90F)
                {
                    float f10 = 0.01111F * (90F - Math.abs(f1));
                    super.CT.ElevatorControl = -0.08F * f10 * (super.Or.getTangage() - 3F);
                } else
                {
                    float f11 = 0.01111F * (Math.abs(f1) - 90F);
                    super.CT.ElevatorControl = 0.08F * f11 * (super.Or.getTangage() - 3F);
                }
                if(getSpeed() < 1.7F * super.Vmin)
                    pop();
                if(mn_time > 2.2F)
                    pop();
                if(danger != null && (danger instanceof Pilot) && ((Maneuver)danger).target == this && super.Loc.distance(((FlightModelMain) (danger)).Loc) > 400D)
                    pop();
                break;

            case 8: // '\b'
                if(first && !isCapableOfACM())
                {
                    if(super.Skill > 0)
                        pop();
                    if(super.Skill > 1)
                        setReadyToReturn(true);
                }
                setSpeedMode(6);
                tmpOr.setYPR(super.Or.getYaw(), 0.0F, 0.0F);
                if(super.Or.getKren() > 0.0F)
                    Ve.set(100D, -6D, 10D);
                else
                    Ve.set(100D, 6D, 10D);
                tmpOr.transform(Ve);
                super.Or.transformInv(Ve);
                Ve.normalize();
                farTurnToDirection();
                if(Alt > 250F && mn_time > 8F || mn_time > 120F)
                    pop();
                break;

            case 55: // '7'
                if(first && !isCapableOfACM())
                {
                    if(super.Skill > 0)
                        pop();
                    if(super.Skill > 1)
                        setReadyToReturn(true);
                }
                setSpeedMode(6);
                tmpOr.setYPR(super.Or.getYaw(), 0.0F, 0.0F);
                if(super.Leader != null && Actor.isAlive(((Interpolate) (super.Leader)).actor) && mn_time < 2.5F)
                {
                    if(((FlightModelMain) (super.Leader)).Or.getKren() > 0.0F)
                        Ve.set(100D, -6D, 10D);
                    else
                        Ve.set(100D, 6D, 10D);
                } else
                if(super.Or.getKren() > 0.0F)
                    Ve.set(100D, -6D, 10D);
                else
                    Ve.set(100D, 6D, 10D);
                tmpOr.transform(Ve);
                super.Or.transformInv(Ve);
                Ve.normalize();
                farTurnToDirection();
                if(Alt > 250F && mn_time > 8F || mn_time > 120F)
                    pop();
                break;

            case 45: // '-'
                setSpeedMode(7);
                smConstPower = 0.8F;
                dA = super.Or.getKren();
                if(dA > 0.0F)
                    dA -= 25F;
                else
                    dA -= 335F;
                if(dA < -180F)
                    dA += 360F;
                dA *= -0.01F;
                super.CT.AileronControl = dA;
                super.CT.ElevatorControl = -0.04F * (super.Or.getTangage() - 1.0F) + 0.002F * ((super.AP.way.curr().z() - (float)((Tuple3d) (super.Loc)).z) + 250F);
                if(mn_time > 60F)
                {
                    mn_time = 0.0F;
                    pop();
                }
                break;

            case 54: // '6'
                if(super.Vwld.length() > (double)(super.VminFLAPS * 2.0F))
                    super.Vwld.scale(0.99500000476837158D);
                // fall through

            case 9: // '\t'
                if(first && !isCapableOfACM())
                {
                    if(super.Skill > 0)
                        pop();
                    if(super.Skill > 1)
                        setReadyToReturn(true);
                }
                setSpeedMode(4);
                smConstSpeed = 100F;
                dA = super.Or.getKren();
                if(dA > 0.0F)
                    dA -= 50F;
                else
                    dA -= 310F;
                if(dA < -180F)
                    dA += 360F;
                dA *= -0.01F;
                super.CT.AileronControl = dA;
                dA = (-10F - super.Or.getTangage()) * 0.05F;
                super.CT.ElevatorControl = dA;
                if((double)getOverload() < 1.0D / Math.abs(Math.cos(dA)))
                    super.CT.ElevatorControl += 1.0F * f;
                else
                    super.CT.ElevatorControl -= 1.0F * f;
                if(Alt < 100F)
                {
                    push(3);
                    pop();
                }
                if(mn_time > 5F)
                    pop();
                break;

            case 14: // '\016'
                setSpeedMode(6);
                if(first)
                    super.AP.setStabAltitude(true);
                dA = super.Or.getKren();
                if((double)getOverload() < 1.0D / Math.abs(Math.cos(dA)))
                    super.CT.ElevatorControl += 1.0F * f;
                else
                    super.CT.ElevatorControl -= 1.0F * f;
                if(dA > 0.0F)
                    dA -= 50F;
                else
                    dA -= 310F;
                if(dA < -180F)
                    dA += 360F;
                dA *= -0.01F;
                super.CT.AileronControl = dA;
                if(mn_time > 5F)
                    pop();
                break;

            case 4: // '\004'
                super.CT.AileronControl = ((Tuple3d) (getW())).x > 0.0D ? 1.0F : -1F;
                super.CT.ElevatorControl = 0.1F * (float)Math.cos(FMMath.DEG2RAD(super.Or.getKren()));
                super.CT.RudderControl = 0.0F;
                if(getSpeedKMH() < 220F)
                {
                    push(3);
                    pop();
                }
                if(mn_time > 7F)
                    pop();
                break;

            case 2: // '\002'
                super.minElevCoeff = 20F;
                if(first)
                {
                    wingman(false);
                    super.AP.setStabAll(false);
                    super.CT.RudderControl = 0.0F;
                    if(World.Rnd().nextInt(0, 99) < 10 && Alt < 80F)
                        Voice.speakPullUp((Aircraft)super.actor);
                }
                setSpeedMode(9);
                super.CT.BayDoorControl = 0.0F;
                super.CT.AirBrakeControl = 0.0F;
                super.CT.AileronControl = -0.04F * (dA = super.Or.getKren());
                super.CT.ElevatorControl = 1.0F + 0.3F * (float)((Tuple3d) (getW())).y;
                if(super.CT.ElevatorControl < 0.0F)
                    super.CT.ElevatorControl = 0.0F;
                if(super.AOA > 15F)
                    super.Or.increment(0.0F, (15F - super.AOA) * 0.5F * f, 0.0F);
                if(Alt < 10F && ((Tuple3d) (super.Vwld)).z < 0.0D)
                    super.Vwld.z *= 0.89999997615814209D;
                if(((Tuple3d) (super.Vwld)).z > 1.0D)
                {
                    if(super.actor instanceof TypeGlider)
                        push(49);
                    else
                        push(57);
                    pop();
                }
                if(mn_time > 25F)
                {
                    push(49);
                    pop();
                }
                break;

            case 60: // '<'
                setSpeedMode(9);
                super.CT.RudderControl = 0.0F;
                super.CT.ElevatorControl = 1.0F;
                if(mn_time > 0.15F)
                    pop();
                break;

            case 61: // '='
                super.CT.RudderControl = 0.0F;
                super.CT.ElevatorControl = -0.4F;
                if(mn_time > 0.2F)
                    pop();
                break;

            case 3: // '\003'
                if(first && program[0] == 49)
                    pop();
                setSpeedMode(6);
                super.CT.AileronControl = -0.04F * super.Or.getKren();
                dA = (getSpeedKMH() - 180F - super.Or.getTangage() * 10F - getVertSpeed() * 5F) * 0.004F;
                super.CT.ElevatorControl = dA;
                if(getSpeed() > super.Vmin * 1.2F && getVertSpeed() > 0.0F)
                {
                    setSpeedMode(7);
                    smConstPower = 0.7F;
                    pop();
                }
                break;

            case 10: // '\n'
                super.AP.setStabAll(false);
                setSpeedMode(6);
                super.CT.AileronControl = -0.04F * super.Or.getKren();
                dA = super.CT.ElevatorControl;
                if(super.Or.getTangage() > 15F)
                    dA -= (super.Or.getTangage() - 15F) * 0.1F * f;
                else
                    dA = (((float)super.Vwld.length() / super.VminFLAPS) * 140F - 50F - super.Or.getTangage() * 20F) * 0.004F;
                dA += 0.5D * ((Tuple3d) (getW())).y;
                super.CT.ElevatorControl = dA;
                if(Alt > 250F && mn_time > 6F || mn_time > 20F)
                    pop();
                break;

            case 71: // 'G'
                super.AP.setStabAll(false);
                setSpeedMode(6);
                super.CT.AileronControl = -0.04F * super.Or.getKren();
                dA = super.CT.ElevatorControl;
                if(super.Or.getTangage() > 15F)
                    dA -= (super.Or.getTangage() - 15F) * 0.1F * f;
                else
                    dA = (((float)super.Vwld.length() / super.VminFLAPS) * 140F - 50F - super.Or.getTangage() * 20F) * 0.004F;
                dA += 0.5D * ((Tuple3d) (getW())).y;
                super.CT.ElevatorControl = dA;
                if(mn_time > 30F)
                    pop();
                break;

            case 57: // '9'
                super.AP.setStabAll(false);
                setSpeedMode(9);
                super.CT.AileronControl = -0.04F * super.Or.getKren();
                dA = super.CT.ElevatorControl;
                if(super.Or.getTangage() > 25F)
                    dA -= (super.Or.getTangage() - 25F) * 0.1F * f;
                else
                    dA = (((float)super.Vwld.length() / super.VminFLAPS) * 140F - 50F - super.Or.getTangage() * 20F) * 0.004F;
                dA += 0.5D * ((Tuple3d) (getW())).y;
                super.CT.ElevatorControl = dA;
                if(Alt > 150F || Alt > 100F && mn_time > 2.0F || mn_time > 3F)
                    pop();
                break;

            case 11: // '\013'
                setSpeedMode(8);
                if(Math.abs(super.Or.getKren()) < 90F)
                {
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                    if(((Tuple3d) (super.Vwld)).z > 0.0D || getSpeedKMH() < 270F)
                        dA = -0.04F;
                    else
                        dA = 0.04F;
                    super.CT.ElevatorControl = super.CT.ElevatorControl * 0.9F + dA * 0.1F;
                } else
                {
                    super.CT.AileronControl = 0.04F * (180F - Math.abs(super.Or.getKren()));
                    if(super.Or.getTangage() > -25F)
                        dA = 0.33F;
                    else
                    if(((Tuple3d) (super.Vwld)).z > 0.0D || getSpeedKMH() < 270F)
                        dA = 0.04F;
                    else
                        dA = -0.04F;
                    super.CT.ElevatorControl = super.CT.ElevatorControl * 0.9F + dA * 0.1F;
                }
                if(Alt < 120F || mn_time > 4F)
                    pop();
                break;

            case 12: // '\f'
                setSpeedMode(4);
                smConstSpeed = 80F;
                super.CT.AileronControl = -0.04F * super.Or.getKren();
                if(super.Vwld.length() > (double)(super.VminFLAPS * 2.0F))
                    super.Vwld.scale(0.99500000476837158D);
                dA = -((float)((Tuple3d) (super.Vwld)).z / (Math.abs(getSpeed()) + 1.0F) + 0.5F);
                if(dA < -0.1F)
                    dA = -0.1F;
                super.CT.ElevatorControl = super.CT.ElevatorControl * 0.9F + dA * 0.1F + 0.3F * (float)((Tuple3d) (getW())).y;
                if(mn_time > 5F || Alt < 200F)
                    pop();
                break;

            case 13: // '\r'
                if(first)
                {
                    super.AP.setStabAll(false);
                    submaneuver = (super.actor instanceof TypeFighter) ? 0 : 2;
                }
                switch(submaneuver)
                {
                case 0: // '\0'
                    dA = super.Or.getKren() - 180F;
                    if(dA < -180F)
                        dA += 360F;
                    dA *= -0.04F;
                    super.CT.AileronControl = dA;
                    if(mn_time > 3F || Math.abs(super.Or.getKren()) > 175F - 5F * (float)super.Skill)
                        submaneuver++;
                    break;

                case 1: // '\001'
                    dA = super.Or.getKren() - 180F;
                    if(dA < -180F)
                        dA += 360F;
                    dA *= -0.04F;
                    super.CT.AileronControl = dA;
                    super.CT.RudderControl = -0.1F * getAOS();
                    setSpeedMode(8);
                    if(super.Or.getTangage() > -45F && getOverload() < maxG)
                        super.CT.ElevatorControl += 1.5F * f;
                    else
                        super.CT.ElevatorControl -= 0.5F * f;
                    if(super.Or.getTangage() < -44F)
                        submaneuver++;
                    if((double)Alt < -5D * ((Tuple3d) (super.Vwld)).z || mn_time > 5F)
                        pop();
                    break;

                case 2: // '\002'
                    setSpeedMode(8);
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                    dA = -((float)((Tuple3d) (super.Vwld)).z / (Math.abs(getSpeed()) + 1.0F) + 0.707F);
                    if(dA < -0.75F)
                        dA = -0.75F;
                    super.CT.ElevatorControl = super.CT.ElevatorControl * 0.9F + dA * 0.1F + 0.5F * (float)((Tuple3d) (getW())).y;
                    if((double)Alt < -5D * ((Tuple3d) (super.Vwld)).z || mn_time > 5F)
                        pop();
                    break;
                }
                if(Alt < 200F)
                    pop();
                break;

            case 5: // '\005'
                dA = super.Or.getKren();
                if(dA < 0.0F)
                    dA -= 270F;
                else
                    dA -= 90F;
                if(dA < -180F)
                    dA += 360F;
                dA *= -0.02F;
                super.CT.AileronControl = dA;
                if(mn_time > 5F || Math.abs(Math.abs(super.Or.getKren()) - 90F) < 1.0F)
                    pop();
                break;

            case 6: // '\006'
                dA = super.Or.getKren() - 180F;
                if(dA < -180F)
                    dA += 360F;
                super.CT.AileronControl = (float)((double)(-0.04F * dA) - 0.5D * ((Tuple3d) (getW())).x);
                if(mn_time > 4F || Math.abs(super.Or.getKren()) > 178F)
                {
                    super.W.x = 0.0D;
                    pop();
                }
                break;

            case 35: // '#'
                if(first)
                {
                    super.AP.setStabAll(false);
                    direction = super.Or.getKren();
                    submaneuver = super.Or.getKren() <= 0.0F ? -1 : 1;
                    tmpi = 0;
                    setSpeedMode(9);
                }
                super.CT.AileronControl = 1.0F * (float)submaneuver;
                super.CT.RudderControl = 0.0F * (float)submaneuver;
                float f2 = super.Or.getKren();
                if(f2 > -90F && f2 < 90F)
                {
                    float f12 = 0.01111F * (90F - Math.abs(f2));
                    super.CT.ElevatorControl = -0.08F * f12 * (super.Or.getTangage() - 3F);
                } else
                {
                    float f13 = 0.01111F * (90F - Math.abs(f2));
                    super.CT.ElevatorControl = 0.08F * f13 * (super.Or.getTangage() - 3F);
                }
                if(super.Or.getKren() * direction < 0.0F)
                    tmpi = 1;
                if(tmpi == 1 && (submaneuver <= 0 ? super.Or.getKren() < direction : super.Or.getKren() > direction) || mn_time > 17.5F)
                    pop();
                break;

            case 22: // '\026'
                setSpeedMode(9);
                super.CT.AileronControl = -0.04F * super.Or.getKren();
                super.CT.ElevatorControl = -0.04F * (super.Or.getTangage() + 5F);
                super.CT.RudderControl = 0.0F;
                if(getSpeed() > super.Vmax || mn_time > 30F)
                    pop();
                break;

            case 67: // 'C'
                super.minElevCoeff = 18F;
                if(first)
                {
                    sub_Man_Count = 0;
                    setSpeedMode(9);
                    super.CT.setPowerControl(1.1F);
                }
                if(danger != null)
                {
                    float f3 = 700F - (float)((FlightModelMain) (danger)).Loc.distance(super.Loc);
                    if(f3 < 0.0F)
                        f3 = 0.0F;
                    f3 *= 0.00143F;
                    float f14 = super.Or.getKren();
                    if(sub_Man_Count == 0 || first)
                    {
                        if(raAilShift < 0.0F)
                            raAilShift = f3 * World.Rnd().nextFloat(0.6F, 1.0F);
                        else
                            raAilShift = f3 * World.Rnd().nextFloat(-1F, -0.6F);
                        raRudShift = f3 * World.Rnd().nextFloat(-0.5F, 0.5F);
                        raElevShift = f3 * World.Rnd().nextFloat(-0.8F, 0.8F);
                    }
                    super.CT.AileronControl = 0.9F * super.CT.AileronControl + 0.1F * raAilShift;
                    super.CT.RudderControl = 0.95F * super.CT.RudderControl + 0.05F * raRudShift;
                    if(f14 > -90F && f14 < 90F)
                        super.CT.ElevatorControl = -0.04F * (super.Or.getTangage() + 5F);
                    else
                        super.CT.ElevatorControl = 0.05F * (super.Or.getTangage() + 5F);
                    super.CT.ElevatorControl += 0.1F * raElevShift;
                    sub_Man_Count++;
                    if((float)sub_Man_Count >= 80F * (1.5F - f3) && f14 > -70F && f14 < 70F)
                        sub_Man_Count = 0;
                    if(mn_time > 30F)
                        pop();
                } else
                {
                    pop();
                }
                break;

            case 16: // '\020'
                if(first)
                {
                    if(!isCapableOfACM())
                        pop();
                    super.AP.setStabAll(false);
                    setSpeedMode(6);
                    if(getSpeed() < 0.75F * super.Vmax)
                    {
                        push();
                        push(22);
                        pop();
                        break;
                    }
                    submaneuver = 0;
                }
                maxAOA = ((Tuple3d) (super.Vwld)).z <= 0.0D ? 12F : 7F;
                switch(submaneuver)
                {
                case 0: // '\0'
                    super.CT.ElevatorControl = 0.05F;
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                    super.CT.RudderControl = -0.1F * getAOS();
                    if(Math.abs(super.Or.getKren()) < 2.0F)
                        submaneuver++;
                    break;

                case 1: // '\001'
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                    super.CT.RudderControl = -0.1F * getAOS();
                    dA = 0.5F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.4F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    if(super.Or.getTangage() > 80F)
                        submaneuver++;
                    if(getSpeed() < super.Vmin * 1.5F)
                        pop();
                    break;

                case 2: // '\002'
                    super.CT.RudderControl = -0.1F * getAOS() * (getSpeed() <= 300F ? 0.0F : 1.0F);
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.4F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    if(super.Or.getTangage() < 0.0F)
                        submaneuver++;
                    break;

                case 3: // '\003'
                    super.CT.RudderControl = -0.1F * getAOS() * (getSpeed() <= 300F ? 0.0F : 1.0F);
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.2F * f;
                    else
                        super.CT.ElevatorControl += 0.2F * f;
                    if(super.Or.getTangage() < -60F)
                        submaneuver++;
                    break;

                case 4: // '\004'
                    if(super.Or.getTangage() > -45F)
                    {
                        super.CT.AileronControl = -0.04F * super.Or.getKren();
                        maxAOA = 3.5F;
                    }
                    super.CT.RudderControl = -0.1F * getAOS();
                    dA = 0.5F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 1.0F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    if(super.Or.getTangage() > 3F || ((Tuple3d) (super.Vwld)).z > 0.0D)
                        pop();
                    break;
                }
                break;

            case 17: // '\021'
                if(first)
                {
                    if(Alt < 1000F)
                        pop();
                    else
                    if(getSpeed() < super.Vmin * 1.2F)
                    {
                        push();
                        push(22);
                        pop();
                    } else
                    {
                        push(18);
                        push(19);
                        pop();
                    }
                } else
                {
                    pop();
                }
                break;

            case 19: // '\023'
                if(first)
                {
                    if(Alt < 1000F)
                    {
                        pop();
                        break;
                    }
                    super.AP.setStabAll(false);
                    submaneuver = 0;
                }
                maxAOA = ((Tuple3d) (super.Vwld)).z <= 0.0D ? 12F : 7F;
                switch(submaneuver)
                {
                case 0: // '\0'
                    super.CT.ElevatorControl = 0.05F;
                    super.CT.AileronControl = 0.04F * (super.Or.getKren() <= 0.0F ? -180F + super.Or.getKren() : 180F - super.Or.getKren());
                    super.CT.RudderControl = -0.1F * getAOS();
                    if(Math.abs(super.Or.getKren()) > 178F)
                        submaneuver++;
                    break;

                case 1: // '\001'
                    setSpeedMode(7);
                    smConstPower = 0.5F;
                    super.CT.AileronControl = 0.0F;
                    super.CT.RudderControl = -0.1F * getAOS();
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.2F * f;
                    else
                        super.CT.ElevatorControl += 1.2F * f;
                    if(super.Or.getTangage() < -60F)
                        submaneuver++;
                    break;

                case 2: // '\002'
                    if(super.Or.getTangage() > -45F)
                    {
                        super.CT.AileronControl = -0.04F * super.Or.getKren();
                        setSpeedMode(9);
                        maxAOA = 7F;
                    }
                    super.CT.RudderControl = -0.1F * getAOS();
                    dA = 0.5F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.8F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    if(super.Or.getTangage() > super.AOA - 1.0F || ((Tuple3d) (super.Vwld)).z > 0.0D)
                        pop();
                    break;
                }
                break;

            case 18: // '\022'
                if(first)
                {
                    if(!isCapableOfACM())
                        pop();
                    if(getSpeed() < super.Vmax * 0.75F)
                    {
                        push();
                        push(22);
                        pop();
                        break;
                    }
                    super.AP.setStabAll(false);
                    submaneuver = 0;
                    setSpeedMode(6);
                }
                maxAOA = ((Tuple3d) (super.Vwld)).z <= 0.0D ? 12F : 7F;
                switch(submaneuver)
                {
                case 0: // '\0'
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                    super.CT.RudderControl = -0.1F * getAOS();
                    if(Math.abs(super.Or.getKren()) < 2.0F)
                        submaneuver++;
                    break;

                case 1: // '\001'
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                    super.CT.RudderControl = -0.1F * getAOS();
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.4F * f;
                    else
                        super.CT.ElevatorControl += 0.8F * f;
                    if(super.Or.getTangage() > 80F)
                        submaneuver++;
                    if(getSpeed() < super.Vmin * 1.5F)
                        pop();
                    break;

                case 2: // '\002'
                    super.CT.RudderControl = -0.1F * getAOS() * (getSpeed() <= 300F ? 0.0F : 1.0F);
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.4F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    if(super.Or.getTangage() < 12F)
                        submaneuver++;
                    break;

                case 3: // '\003'
                    if(Math.abs(super.Or.getKren()) < 60F)
                        super.CT.ElevatorControl = 0.05F;
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                    super.CT.RudderControl = -0.1F * getAOS();
                    if(Math.abs(super.Or.getKren()) < 30F)
                        submaneuver++;
                    break;

                case 4: // '\004'
                    pop();
                    break;
                }
                break;

            case 15: // '\017'
                if(first && getSpeed() < 0.35F * (super.Vmin + super.Vmax))
                {
                    pop();
                } else
                {
                    push(8);
                    pop();
                    super.CT.RudderControl = -0.1F * getAOS();
                    if(super.Or.getKren() < 0.0F)
                        super.CT.AileronControl = -0.04F * (super.Or.getKren() + 30F);
                    else
                        super.CT.AileronControl = -0.04F * (super.Or.getKren() - 30F);
                    if(mn_time > 7.5F)
                        pop();
                }
                break;

            case 20: // '\024'
                if(first)
                {
                    wingman(false);
                    setSpeedMode(6);
                }
                if(!isCapableOfBMP())
                {
                    setReadyToDie(true);
                    pop();
                }
                if(((Tuple3d) (getW())).z > 0.0D)
                    super.CT.RudderControl = 1.0F;
                else
                    super.CT.RudderControl = -1F;
                if(super.AOA > super.AOA_Crit - 4F)
                    super.Or.increment(0.0F, 0.01F * (super.AOA_Crit - 4F - super.AOA), 0.0F);
                if(super.AOA < -5F)
                    super.Or.increment(0.0F, 0.01F * (-5F - super.AOA), 0.0F);
                if(super.AOA < super.AOA_Crit - 1.0F)
                    pop();
                if(mn_time > 14F - (float)super.Skill * 4F || Alt < 50F)
                    pop();
                break;

            case 21: // '\025'
                super.AP.setWayPoint(true);
                //TODO: DBW AI Mod Edits
                if(getAltitude() < super.AP.way.curr().z() - 100F && (super.actor instanceof TypeSupersonic))
                    super.CT.ElevatorControl += 0.025F;
                if(mn_time > 300F)
                    pop();
                if(isTick(256, 0) && !super.actor.isTaskComplete() && (super.AP.way.isLast() && super.AP.getWayPointDistance() < 1500F || super.AP.way.isLanding()))
                    World.onTaskComplete(super.actor);
                if(((Aircraft)super.actor).aircNumber() == 0 && !isReadyToReturn())
                {
                    com.maddox.il2.ai.Regiment regiment = ((Aircraft)super.actor).getRegiment();
                    World.cur();
                    if(regiment == World.getPlayerAircraft().getRegiment())
                    {
                        float f15 = 1E+012F;
                        if(super.AP.way.curr().Action == 3)
                        {
                            f15 = super.AP.getWayPointDistance();
                        } else
                        {
                            int i1 = super.AP.way.Cur();
                            super.AP.way.next();
                            if(super.AP.way.curr().Action == 3)
                                f15 = super.AP.getWayPointDistance();
                            super.AP.way.setCur(i1);
                        }
                        if(Speak5minutes == 0 && 22000F < f15 && f15 < 30000F)
                        {
                            Voice.speak5minutes((Aircraft)super.actor);
                            Speak5minutes = 1;
                        }
                        if(Speak1minute == 0 && f15 < 10000F)
                        {
                            Voice.speak1minute((Aircraft)super.actor);
                            Speak1minute = 1;
                            Speak5minutes = 1;
                        }
                        if((WeWereInGAttack || WeWereInAttack) && mn_time > 5F)
                        {
                            if(!SpeakMissionAccomplished)
                            {
                                boolean flag2 = false;
                                int j1 = super.AP.way.Cur();
                                if(super.AP.way.curr().Action == 3 || super.AP.way.curr().getTarget() != null)
                                    break;
                                do
                                {
                                    if(super.AP.way.Cur() >= super.AP.way.size() - 1)
                                        break;
                                    super.AP.way.next();
                                    if(super.AP.way.curr().Action == 3 || super.AP.way.curr().getTarget() != null)
                                        flag2 = true;
                                } while(true);
                                super.AP.way.setCur(j1);
                                if(!flag2)
                                {
                                    Voice.speakMissionAccomplished((Aircraft)super.actor);
                                    SpeakMissionAccomplished = true;
                                }
                            }
                            if(!SpeakMissionAccomplished)
                            {
                                Speak5minutes = 0;
                                Speak1minute = 0;
                                SpeakBeginGattack = 0;
                            }
                            WeWereInGAttack = false;
                            WeWereInAttack = false;
                        }
                    }
                }
                if(((super.actor instanceof TypeBomber) || (super.actor instanceof TypeTransport)) && super.AP.way.curr() != null && super.AP.way.curr().Action == 3 && (super.AP.way.curr().getTarget() == null || (super.actor instanceof Scheme4)))
                {
                    double d = ((Tuple3d) (super.Loc)).z - World.land().HQ(((Tuple3d) (super.Loc)).x, ((Tuple3d) (super.Loc)).y);
                    if(d < 0.0D)
                        d = 0.0D;
                    if((double)super.AP.getWayPointDistance() < (double)getSpeed() * Math.sqrt(d * 0.20386999845504761D) && !bombsOut)
                    {
                        if(super.CT.Weapons[3] != null && super.CT.Weapons[3][0] != null && super.CT.Weapons[3][0].countBullets() != 0 && !(super.CT.Weapons[3][0] instanceof BombGunPara))
                            Voice.airSpeaks((Aircraft)super.actor, 85, 1);
                        bombsOut = true;
                        super.AP.way.curr().Action = 0;
                        if(Group != null)
                            Group.dropBombs();
                    }
                }
                setSpeedMode(3);
                if(super.AP.way.isLandingOnShip() && super.AP.way.isLanding())
                {
                    super.AP.way.landingAirport.rebuildLandWay(this);
                    //TODO: DBW AI Mod Edits
                    if(super.CT.bHasCockpitDoorControl && !super.CT.bNoCarrierCanopyOpen)
                        super.AS.setCockpitDoor(super.actor, 1);
                }
                break;

            case 23: // '\027'
                if(first)
                {
                    wingman(false);
                    if(getSpeedKMH() < super.Vmin * 1.25F)
                    {
                        push();
                        push(22);
                        pop();
                        break;
                    }
                }
                setSpeedMode(6);
                super.CT.AileronControl = -0.04F * super.Or.getKren();
                super.CT.RudderControl = -0.1F * getAOS();
                if(super.Or.getTangage() < 70F && getOverload() < maxG && super.AOA < 14F)
                    super.CT.ElevatorControl += 0.5F * f;
                else
                    super.CT.ElevatorControl -= 0.5F * f;
                if(((Tuple3d) (super.Vwld)).z < 1.0D)
                    pop();
                break;

            case 24: // '\030'
                if(super.Leader == null || !Actor.isAlive(((Interpolate) (super.Leader)).actor) || !((Maneuver)super.Leader).isOk() || ((Maneuver)super.Leader).isBusy() && (!(super.Leader instanceof RealFlightModel) || !((RealFlightModel)super.Leader).isRealMode()))
                {
                    set_maneuver(0);
                    break;
                }
                if(super.actor instanceof TypeGlider)
                {
                    if(((FlightModelMain) (super.Leader)).AP.way.curr().Action != 0 && ((FlightModelMain) (super.Leader)).AP.way.curr().Action != 1)
                        set_maneuver(49);
                } else
                if(super.Leader.getSpeed() < 30F || ((Tuple3d) (((FlightModelMain) (super.Leader)).Loc)).z - Engine.land().HQ_Air(((Tuple3d) (((FlightModelMain) (super.Leader)).Loc)).x, ((Tuple3d) (((FlightModelMain) (super.Leader)).Loc)).y) < 75D)
                {
                    airClient = super.Leader;
                    push(71);
                    push(71);
                    pop();
                    break;
                }
                if(!((FlightModelMain) (super.Leader)).AP.way.isLanding())
                {
                    super.AP.way.setCur(((FlightModelMain) (super.Leader)).AP.way.Cur());
                    if(((FlightModelMain) (super.Leader)).Wingman != this)
                    {
                        if(!bombsOut && ((Maneuver)super.Leader).bombsOut)
                        {
                            bombsOut = true;
                            for(Maneuver maneuver1 = this; ((FlightModelMain) (maneuver1)).Wingman != null;)
                            {
                                maneuver1 = (Maneuver)((FlightModelMain) (maneuver1)).Wingman;
                                maneuver1.bombsOut = true;
                            }

                        }
                        if(super.CT.BayDoorControl != ((FlightModelMain) (super.Leader)).CT.BayDoorControl)
                        {
                            super.CT.BayDoorControl = ((FlightModelMain) (super.Leader)).CT.BayDoorControl;
                            for(Pilot pilot = (Pilot)this; ((FlightModelMain) (pilot)).Wingman != null;)
                            {
                                pilot = (Pilot)((FlightModelMain) (pilot)).Wingman;
                                ((FlightModelMain) (pilot)).CT.BayDoorControl = super.CT.BayDoorControl;
                            }

                        }
                    }
                } else
                {
                    if(((FlightModelMain) (super.Leader)).Wingman != this)
                    {
                        push(8);
                        push(8);
                        push(World.Rnd().nextBoolean() ? 8 : 48);
                        push(World.Rnd().nextBoolean() ? 8 : 48);
                        pop();
                    }
                    super.Leader = null;
                    pop();
                    break;
                }
                airClient = super.Leader;
                tmpOr.setAT0(((FlightModelMain) (airClient)).Vwld);
                tmpOr.increment(0.0F, airClient.getAOA(), 0.0F);
                Ve.set(followOffset);
                Ve.x -= 300D;
                tmpV3f.sub(followTargShift, followCurShift);
                if(tmpV3f.lengthSquared() < 0.5D)
                    followTargShift.set(World.cur().rnd.nextFloat(-0F, 10F), World.cur().rnd.nextFloat(-5F, 5F), World.cur().rnd.nextFloat(-3.5F, 3.5F));
                tmpV3f.normalize();
                tmpV3f.scale(2.0F * f);
                followCurShift.add(tmpV3f);
                Ve.add(followCurShift);
                tmpOr.transform(Ve, Po);
                Po.scale(-1D);
                Po.add(((FlightModelMain) (airClient)).Loc);
                Ve.sub(Po, super.Loc);
                super.Or.transformInv(Ve);
                dist = (float)Ve.length();
                if(((Tuple3d) (followOffset)).x > 600D)
                {
                    Ve.set(followOffset);
                    Ve.x -= 0.5D * ((Tuple3d) (followOffset)).x;
                    tmpOr.transform(Ve, Po);
                    Po.scale(-1D);
                    Po.add(((FlightModelMain) (airClient)).Loc);
                    Ve.sub(Po, super.Loc);
                    super.Or.transformInv(Ve);
                }
                Ve.normalize();
                if((double)dist > 600D + ((Tuple3d) (Ve)).x * 400D)
                {
                    push();
                    push(53);
                    pop();
                } else
                {
                    if((super.actor instanceof TypeTransport) && (double)super.Vmax < 70D)
                        farTurnToDirection(6.2F);
                    else
                        attackTurnToDirection(dist, f, 10F);
                    setSpeedMode(10);
                    tailForStaying = super.Leader;
                    tailOffset.set(followOffset);
                    tailOffset.scale(-1D);
                }
                break;

            case 65: // 'A'
                if(!(this instanceof RealFlightModel) || !((RealFlightModel)this).isRealMode())
                {
                    bombsOut = true;
                    super.CT.dropFuelTanks();
                }
                if(airClient == null || !Actor.isAlive(((Interpolate) (airClient)).actor) || !isOk())
                {
                    set_maneuver(0);
                } else
                {
                    Maneuver maneuver2 = (Maneuver)airClient;
                    Maneuver maneuver4 = (Maneuver)((Maneuver)airClient).danger;
                    if(maneuver2.getDangerAggressiveness() >= 1.0F - (float)super.Skill * 0.35F && maneuver4 != null && hasCourseWeaponBullets())
                    {
                        Voice.speakCheckYour6((Aircraft)((Interpolate) (maneuver2)).actor, (Aircraft)((Interpolate) (maneuver4)).actor);
                        Voice.speakHelpFromAir((Aircraft)super.actor, 1);
                        set_task(6);
                        target = maneuver4;
                        set_maneuver(27);
                    }
                    Ve.sub(((FlightModelMain) (airClient)).Loc, super.Loc);
                    super.Or.transformInv(Ve);
                    dist = (float)Ve.length();
                    Ve.normalize();
                    attackTurnToDirection(dist, f, 10F + (float)super.Skill * 8F);
                    if(Alt > 50F)
                        setSpeedMode(1);
                    else
                        setSpeedMode(6);
                    tailForStaying = airClient;
                    tailOffset.set(followOffset);
                    tailOffset.scale(-1D);
                    if((double)dist > 600D + ((Tuple3d) (Ve)).x * 400D && get_maneuver() != 27)
                    {
                        push();
                        push(53);
                        pop();
                    }
                }
                break;

            case 53: // '5'
                if(airClient == null || !Actor.isAlive(((Interpolate) (airClient)).actor) || !isOk())
                {
                    airClient = null;
                    set_maneuver(0);
                } else
                {
                    maxAOA = 5F;
                    Ve.set(followOffset);
                    Ve.x -= 300D;
                    tmpOr.setAT0(((FlightModelMain) (airClient)).Vwld);
                    tmpOr.increment(0.0F, 4F, 0.0F);
                    tmpOr.transform(Ve, Po);
                    Po.scale(-1D);
                    Po.add(((FlightModelMain) (airClient)).Loc);
                    Ve.sub(Po, super.Loc);
                    super.Or.transformInv(Ve);
                    dist = (float)Ve.length();
                    Ve.normalize();
                    if(super.Vmax < 83F)
                        farTurnToDirection(8.5F);
                    else
                        farTurnToDirection(7F);
                    float f4 = (super.Energy - ((FlightModelMain) (airClient)).Energy) * 0.1019F;
                    if((double)f4 < -50D + ((Tuple3d) (followOffset)).z)
                        setSpeedMode(9);
                    else
                        setSpeedMode(10);
                    tailForStaying = airClient;
                    tailOffset.set(followOffset);
                    tailOffset.scale(-1D);
                    if((double)dist < 500D + ((Tuple3d) (Ve)).x * 200D)
                    {
                        pop();
                    } else
                    {
                        if(super.AOA > 12F && Alt > 15F)
                            super.Or.increment(0.0F, 12F - super.AOA, 0.0F);
                        if(mn_time > 30F && (((Tuple3d) (Ve)).x < 0.0D || dist > 10000F))
                            pop();
                    }
                }
                break;

            case 68: // 'D'
                if(airClient == null || !Actor.isAlive(((Interpolate) (airClient)).actor) || !isOk())
                {
                    set_maneuver(0);
                } else
                {
                    Maneuver maneuver3 = (Maneuver)airClient;
                    Maneuver maneuver5 = (Maneuver)((Maneuver)airClient).danger;
                    if(maneuver3.getDangerAggressiveness() >= 1.0F - (float)super.Skill * 0.3F && maneuver5 != null && hasCourseWeaponBullets())
                    {
                        tmpV3d.sub(((FlightModelMain) (maneuver3)).Vwld, ((FlightModelMain) (maneuver5)).Vwld);
                        if(tmpV3d.length() < 170D)
                        {
                            set_task(6);
                            target = maneuver5;
                            set_maneuver(27);
                        }
                    }
                    maxAOA = 5F;
                    Ve.set(followOffset);
                    Ve.x -= 300D;
                    tmpOr.setAT0(((FlightModelMain) (airClient)).Vwld);
                    tmpOr.increment(0.0F, 4F, 0.0F);
                    tmpOr.transform(Ve, Po);
                    Po.scale(-1D);
                    Po.add(((FlightModelMain) (airClient)).Loc);
                    Ve.sub(Po, super.Loc);
                    super.Or.transformInv(Ve);
                    dist = (float)Ve.length();
                    Ve.normalize();
                    if(super.Vmax < 83F)
                        farTurnToDirection(8.5F);
                    else
                        farTurnToDirection(7F);
                    setSpeedMode(10);
                    tailForStaying = airClient;
                    tailOffset.set(followOffset);
                    tailOffset.scale(-1D);
                    if((double)dist < 500D + ((Tuple3d) (Ve)).x * 200D)
                    {
                        pop();
                    } else
                    {
                        if(super.AOA > 12F && Alt > 15F)
                            super.Or.increment(0.0F, 12F - super.AOA, 0.0F);
                        if(mn_time > 30F && (((Tuple3d) (Ve)).x < 0.0D || dist > 10000F))
                            pop();
                    }
                }
                break;

            case 59: // ';'
                if(airClient == null || !Actor.isValid(((Interpolate) (airClient)).actor) || !isOk())
                {
                    airClient = null;
                    set_maneuver(0);
                } else
                {
                    maxAOA = 5F;
                    if(first)
                        followOffset.set(1000F * (float)Math.sin((float)beNearOffsPhase * 0.7854F), 1000F * (float)Math.cos((float)beNearOffsPhase * 0.7854F), -400D);
                    Ve.set(followOffset);
                    Ve.x -= 300D;
                    tmpOr.setAT0(((FlightModelMain) (airClient)).Vwld);
                    tmpOr.increment(0.0F, 4F, 0.0F);
                    tmpOr.transform(Ve, Po);
                    Po.scale(-1D);
                    Po.add(((FlightModelMain) (airClient)).Loc);
                    Ve.sub(Po, super.Loc);
                    super.Or.transformInv(Ve);
                    dist = (float)Ve.length();
                    Ve.normalize();
                    farTurnToDirection();
                    setSpeedMode(2);
                    tailForStaying = airClient;
                    tailOffset.set(followOffset);
                    tailOffset.scale(-1D);
                    if(dist < 300F)
                    {
                        beNearOffsPhase++;
                        if(beNearOffsPhase > 3)
                            beNearOffsPhase = 0;
                        float f5 = (float)Math.sqrt(((Tuple3d) (followOffset)).x * ((Tuple3d) (followOffset)).x + ((Tuple3d) (followOffset)).y * ((Tuple3d) (followOffset)).y);
                        followOffset.set(f5 * (float)Math.sin((float)beNearOffsPhase * 1.5708F), f5 * (float)Math.cos((float)beNearOffsPhase * 1.5708F), ((Tuple3d) (followOffset)).z);
                    }
                    if(super.AOA > 12F && Alt > 15F)
                        super.Or.increment(0.0F, 12F - super.AOA, 0.0F);
                    if(mn_time > 15F && (((Tuple3d) (Ve)).x < 0.0D || dist > 3000F))
                        pop();
                    if(mn_time > 30F)
                        pop();
                }
                break;

            case 63: // '?'
                if(!(this instanceof RealFlightModel) || !((RealFlightModel)this).isRealMode())
                {
                    bombsOut = true;
                    super.CT.dropFuelTanks();
                }
                if(target == null || !Actor.isValid(((Interpolate) (target)).actor) || target.isTakenMortalDamage() || !hasCourseWeaponBullets())
                {
                    target = null;
                    clear_stack();
                    set_maneuver(3);
                } else
                if(target.getSpeedKMH() < 45F && ((Tuple3d) (((FlightModelMain) (target)).Loc)).z - Engine.land().HQ_Air(((Tuple3d) (((FlightModelMain) (target)).Loc)).x, ((Tuple3d) (((FlightModelMain) (target)).Loc)).y) < 50D && ((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                {
                    target_ground = ((Interpolate) (target)).actor;
                    set_task(7);
                    clear_stack();
                    set_maneuver(43);
                } else
                {
                    if((super.actor instanceof HE_LERCHE3) && ((HE_LERCHE3)super.actor).bToFire)
                    {
                        super.CT.WeaponControl[2] = true;
                        ((HE_LERCHE3)super.actor).bToFire = false;
                    }
                    if((super.actor instanceof TA_183) && ((TA_183)super.actor).bToFire)
                    {
                        super.CT.WeaponControl[2] = true;
                        ((TA_183)super.actor).bToFire = false;
                    }
                    if((super.actor instanceof TA_152C) && ((TA_152C)super.actor).bToFire)
                    {
                        super.CT.WeaponControl[2] = true;
                        ((TA_152C)super.actor).bToFire = false;
                    }
                    if(((Tuple3d) (TargV)).z == -400D)
                        fighterUnderBomber(f);
                    else
                        fighterVsBomber(f);
                    if(super.AOA > super.AOA_Crit - 2.0F && Alt > 15F)
                        super.Or.increment(0.0F, 0.01F * (super.AOA_Crit - 2.0F - super.AOA), 0.0F);
                }
                break;

            case 27: // '\033'
                if(!(this instanceof RealFlightModel) || !((RealFlightModel)this).isRealMode())
                {
                    bombsOut = true;
                    super.CT.dropFuelTanks();
                }
                if(target == null || !Actor.isValid(((Interpolate) (target)).actor) || target.isTakenMortalDamage() || ((Interpolate) (target)).actor.getArmy() == super.actor.getArmy() || !hasCourseWeaponBullets())
                {
                    target = null;
                    clear_stack();
                    set_maneuver(0);
                } else
                if(target.getSpeedKMH() < 45F && ((Tuple3d) (((FlightModelMain) (target)).Loc)).z - Engine.land().HQ_Air(((Tuple3d) (((FlightModelMain) (target)).Loc)).x, ((Tuple3d) (((FlightModelMain) (target)).Loc)).y) < 50D && ((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                {
                    target_ground = ((Interpolate) (target)).actor;
                    set_task(7);
                    clear_stack();
                    set_maneuver(43);
                } else
                {
                    if(first && (super.actor instanceof TypeAcePlane))
                    {
                        if(target != null && ((Interpolate) (target)).actor != null && ((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                            target.Skill = 0;
                        if(danger != null && ((Interpolate) (danger)).actor != null && ((Interpolate) (danger)).actor.getArmy() != super.actor.getArmy())
                            danger.Skill = 0;
                    }
                    if(((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                        ((Maneuver)target).danger = this;
                    if(isTick(64, 0))
                    {
                        float f6 = (((FlightModelMain) (target)).Energy - super.Energy) * 0.1019F;
                        Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
                        super.Or.transformInv(Ve);
                        Ve.normalize();
                        float f16 = (470F + (float)((Tuple3d) (Ve)).x * 120F) - f6;
                        if(f16 < 0.0F)
                        {
                            clear_stack();
                            set_maneuver(62);
                        }
                    }
                    fighterVsFighter(f);
                    setSpeedMode(9);
                    if(super.AOA > super.AOA_Crit - 1.0F && Alt > 15F)
                        super.Or.increment(0.0F, 0.01F * (super.AOA_Crit - 1.0F - super.AOA), 0.0F);
                    if(mn_time > 100F)
                    {
                        target = null;
                        pop();
                    }
                }
                break;

            case 62: // '>'
                if(target == null || !Actor.isValid(((Interpolate) (target)).actor) || target.isTakenMortalDamage() || ((Interpolate) (target)).actor.getArmy() == super.actor.getArmy() || !hasCourseWeaponBullets())
                {
                    target = null;
                    clear_stack();
                    set_maneuver(0);
                } else
                if(target.getSpeedKMH() < 45F && ((Tuple3d) (((FlightModelMain) (target)).Loc)).z - Engine.land().HQ_Air(((Tuple3d) (((FlightModelMain) (target)).Loc)).x, ((Tuple3d) (((FlightModelMain) (target)).Loc)).y) < 50D && ((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                {
                    target_ground = ((Interpolate) (target)).actor;
                    set_task(7);
                    clear_stack();
                    set_maneuver(43);
                } else
                {
                    if(first && (super.actor instanceof TypeAcePlane))
                    {
                        if(target != null && ((Interpolate) (target)).actor != null && ((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                            target.Skill = 0;
                        if(danger != null && ((Interpolate) (danger)).actor != null && ((Interpolate) (danger)).actor.getArmy() != super.actor.getArmy())
                            danger.Skill = 0;
                    }
                    if(((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                        ((Maneuver)target).danger = this;
                    goodFighterVsFighter(f);
                }
                break;

            case 72: // 'H'
                if(target == null || !Actor.isValid(((Interpolate) (target)).actor) || target.isTakenMortalDamage() || ((Interpolate) (target)).actor.getArmy() == super.actor.getArmy() || !hasCourseWeaponBullets())
                {
                    target = null;
                    clear_stack();
                    set_maneuver(0);
                } else
                if(target.getSpeedKMH() < 45F && ((Tuple3d) (((FlightModelMain) (target)).Loc)).z - Engine.land().HQ_Air(((Tuple3d) (((FlightModelMain) (target)).Loc)).x, ((Tuple3d) (((FlightModelMain) (target)).Loc)).y) < 50D && ((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                {
                    target_ground = ((Interpolate) (target)).actor;
                    set_task(7);
                    clear_stack();
                    set_maneuver(43);
                } else
                {
                    if(first && (super.actor instanceof TypeAcePlane))
                    {
                        if(target != null && ((Interpolate) (target)).actor != null && ((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                            target.Skill = 0;
                        if(danger != null && ((Interpolate) (danger)).actor != null && ((Interpolate) (danger)).actor.getArmy() != super.actor.getArmy())
                            danger.Skill = 0;
                    }
                    if(((Interpolate) (target)).actor.getArmy() != super.actor.getArmy())
                        ((Maneuver)target).danger = this;
                    modFighterVsFighter(f);
                }
                break;

            case 70: // 'F'
                checkGround = false;
                checkStrike = false;
                frequentControl = true;
                stallable = false;
                if(super.actor instanceof HE_LERCHE3)
                    switch(submaneuver)
                    {
                    case 0: // '\0'
                        super.AP.setStabAll(false);
                        submaneuver++;
                        sub_Man_Count = 0;
                        // fall through

                    case 1: // '\001'
                        if(sub_Man_Count == 0)
                            super.CT.AileronControl = World.cur().rnd.nextFloat(-0.15F, 0.15F);
                        super.CT.AirBrakeControl = 1.0F;
                        super.CT.setPowerControl(1.0F);
                        super.CT.ElevatorControl = Aircraft.cvt(super.Or.getTangage(), 0.0F, 60F, 1.0F, 0.0F);
                        if(super.Or.getTangage() > 30F)
                        {
                            submaneuver++;
                            sub_Man_Count = 0;
                        }
                        sub_Man_Count++;
                        break;

                    case 2: // '\002'
                        super.CT.AileronControl = 0.0F;
                        super.CT.ElevatorControl = 0.0F;
                        super.CT.setPowerControl(0.1F);
                        super.Or.increment(0.0F, (float)((double)f * 0.10000000000000001D * (double)sub_Man_Count * (90D - (double)super.Or.getTangage())), 0.0F);
                        if(super.Or.getTangage() > 89F)
                        {
                            saveOr.set(super.Or);
                            submaneuver++;
                        }
                        sub_Man_Count++;
                        break;

                    case 3: // '\003'
                        super.CT.ElevatorControl = 0.0F;
                        if(Alt > 10F)
                            super.CT.setPowerControl(0.33F);
                        else
                            super.CT.setPowerControl(0.0F);
                        if(Alt < 20F)
                        {
                            if(((Tuple3d) (super.Vwld)).z < -4D)
                                super.Vwld.z *= 0.94999999999999996D;
                            if(super.Vwld.lengthSquared() < 1.0D)
                                super.EI.setEngineStops();
                        }
                        super.Or.set(saveOr);
                        if(mn_time > 100F)
                        {
                            super.Vwld.set(0.0D, 0.0D, 0.0D);
                            MsgDestroy.Post(Time.current() + 12000L, super.actor);
                        }
                        break;
                    }
                break;

            case 25: // '\031'
                wingman(false);
                if(super.AP.way.isLanding())
                {
                    if(super.AP.way.isLandingOnShip())
                    {
                        super.AP.way.landingAirport.rebuildLandWay(this);
                        if(super.CT.GearControl == 1.0F && super.CT.arrestorControl < 1.0F && !super.Gears.onGround())
                            super.AS.setArrestor(super.actor, 1);
                    }
                    if(first)
                    {
                        super.AP.setWayPoint(true);
                        doDumpBombsPassively();
                        submaneuver = 0;
                    }
                    if((super.actor instanceof HE_LERCHE3) && Alt < 50F)
                        maneuver = 70;
                    if(Alt < 50F)
                        super.AS.setNavLightsState(false);
                    if(Alt < 50F)
                        super.AS.setLandingLightState(true);
                    super.AP.way.curr().getP(Po);
                    int i = super.AP.way.Cur();
                    float f17 = (float)((Tuple3d) (super.Loc)).z - super.AP.way.last().z();
                    super.AP.way.setCur(i);
                    Alt = Math.min(Alt, f17);
                    Po.add(0.0D, 0.0D, -3D);
                    Ve.sub(Po, super.Loc);
                    float f20 = (float)Ve.length();
                    boolean flag6 = Alt > 4.5F + super.Gears.H && super.AP.way.Cur() < 8;
                    if(super.AP.way.isLandingOnShip())
                        flag6 = Alt > 4.5F + super.Gears.H && super.AP.way.Cur() < 8;
                    if(flag6)
                    {
                        super.AP.way.prev();
                        super.AP.way.curr().getP(Pc);
                        super.AP.way.next();
                        tmpV3f.sub(Po, Pc);
                        tmpV3f.normalize();
                        if(tmpV3f.dot(Ve) < 0.0D && f20 > 1000F && !TaxiMode)
                        {
                            super.AP.way.first();
                            push(10);
                            pop();
                            super.CT.GearControl = 0.0F;
                        }
                        float f25 = (float)tmpV3f.dot(Ve);
                        tmpV3f.scale(-f25);
                        tmpV3f.add(Po, tmpV3f);
                        tmpV3f.sub(super.Loc);
                        float f29 = 0.0005F * (3000F - f20);
                        if(f29 > 1.0F)
                            f29 = 1.0F;
                        if(f29 < 0.1F)
                            f29 = 0.1F;
                        float f31 = (float)tmpV3f.length();
                        if(f31 > 40F * f29)
                        {
                            f31 = 40F * f29;
                            tmpV3f.normalize();
                            tmpV3f.scale(f31);
                        }
                        float f33 = super.VminFLAPS;
                        if(super.AP.way.Cur() >= 6)
                        {
                            if(super.AP.way.isLandingOnShip())
                            {
                                if(Actor.isAlive(super.AP.way.landingAirport) && (super.AP.way.landingAirport instanceof AirportCarrier))
                                {
                                    float f34 = (float)((AirportCarrier)super.AP.way.landingAirport).speedLen();
                                    if(super.VminFLAPS < f34 + 10F)
                                        f33 = f34 + 10F;
                                }
                            } else
                            {
                                f33 = super.VminFLAPS * 1.2F;
                            }
                            if(f33 < 14F)
                                f33 = 14F;
                        } else
                        {
                            f33 = super.VminFLAPS * 2.0F;
                        }
                        double d4 = super.Vwld.length();
                        double d6 = (double)f33 - d4;
                        float f35 = 2.0F * f;
                        if(d6 > (double)f35)
                            d6 = f35;
                        if(d6 < (double)(-f35))
                            d6 = -f35;
                        Ve.normalize();
                        Ve.scale(d4);
                        Ve.add(tmpV3f);
                        Ve.sub(super.Vwld);
                        float f36 = (50F * f29 - f31) * f;
                        if(Ve.length() > (double)f36)
                        {
                            Ve.normalize();
                            Ve.scale(f36);
                        }
                        super.Vwld.add(Ve);
                        super.Vwld.normalize();
                        super.Vwld.scale(d4 + d6);
                        super.Loc.x += ((Tuple3d) (super.Vwld)).x * (double)f;
                        super.Loc.y += ((Tuple3d) (super.Vwld)).y * (double)f;
                        super.Loc.z += ((Tuple3d) (super.Vwld)).z * (double)f;
                        super.Or.transformInv(tmpV3f);
                        tmpOr.setAT0(super.Vwld);
                        float f37 = 0.0F;
                        if(super.AP.way.isLandingOnShip())
                            f37 = 0.9F * (45F - Alt);
                        else
                            f37 = 0.7F * (20F - Alt);
                        if(f37 < 0.0F)
                            f37 = 0.0F;
                        tmpOr.increment(0.0F, 4F + f37, (float)(((Tuple3d) (tmpV3f)).y * 0.5D));
                        super.Or.interpolate(tmpOr, 0.5F * f);
                        super.callSuperUpdate = false;
                        super.W.set(0.0D, 0.0D, 0.0D);
                        super.CT.ElevatorControl = 0.05F + 0.3F * f37;
                        super.CT.RudderControl = (float)(-((Tuple3d) (tmpV3f)).y * 0.02D);
                        direction = super.Or.getAzimut();
                    } else
                    {
                        super.AP.setStabDirection(true);
                    }
                } else
                {
                    super.AP.setStabDirection(true);
                }
                dA = super.CT.ElevatorControl;
                super.AP.update(f);
                setSpeedControl(f);
                super.CT.ElevatorControl = dA;
                if(maneuver == 25)
                {
                    if(Alt > 60F)
                    {
                        if(Alt < 160F){
                            CT.FlapsControl = 0.32F;
                            //TODO: Blown Flaps
                            if(CT.bHasBlownFlaps)
                            	CT.BlownFlapsControl = 1.0F;
                        }
                        else
                            super.CT.FlapsControl = 0.0F;
                        setSpeedMode(7);
                        smConstPower = 0.2F;
                        dA = Math.min(130F + Alt, 270F);
                        if(((Tuple3d) (super.Vwld)).z > 0.0D || getSpeedKMH() < dA)
                            dA = -1.2F * f;
                        else
                            dA = 1.2F * f;
                        super.CT.ElevatorControl = super.CT.ElevatorControl * 0.9F + dA * 0.1F;
                    } else
                    {
                        super.minElevCoeff = 15F;
                        if(super.AP.way.Cur() >= 6 || super.AP.way.Cur() == 0)
                        {
                            if(super.Or.getTangage() < -5F)
                                super.Or.increment(0.0F, -super.Or.getTangage() - 5F, 0.0F);
                            if(super.Or.getTangage() > super.Gears.Pitch + 10F)
                                super.Or.increment(0.0F, -(super.Or.getTangage() - (super.Gears.Pitch + 10F)), 0.0F);
                        }
                        super.CT.FlapsControl = 1.0F;
                        //TODO: Blown Flaps
                        if(CT.bHasBlownFlaps)
                        	CT.BlownFlapsControl = 1.0F;
                        if(super.Vrel.length() < 1.0D)
                        {
                        	//TODO: Blown Flaps
                            CT.FlapsControl = CT.BlownFlapsControl = CT.BrakeControl = 0.0F;
                            if(!TaxiMode)
                            {
                                setSpeedMode(8);
                                if(super.AP.way.isLandingOnShip())
                                {
                                    if(super.CT.getFlap() < 0.001F)
                                        super.AS.setWingFold(super.actor, 1);
                                    super.CT.BrakeControl = 1.0F;
                                    if(super.CT.arrestorControl == 1.0F && super.Gears.onGround())
                                        super.AS.setArrestor(super.actor, 0);
                                    MsgDestroy.Post(Time.current() + 25000L, super.actor);
                                } else
                                {
                                    super.EI.setEngineStops();
                                    if(super.EI.engines[0].getPropw() < 1.0F)
                                    {
                                        super.actor = super.actor;
                                        World.cur();
                                        if(super.actor != World.getPlayerAircraft())
                                            MsgDestroy.Post(Time.current() + 12000L, super.actor);
                                    }
                                }
                            }
                        }
                        if(getSpeed() < super.VmaxFLAPS * 0.21F)
                            super.CT.FlapsControl = 0.0F;
                        if(super.Vrel.length() < (double)(super.VmaxFLAPS * 0.25F) && wayCurPos == null && !super.AP.way.isLandingOnShip())
                        {
                            TaxiMode = true;
                            super.AP.way.setCur(0);
                            return;
                        }
                        if(getSpeed() > super.VminFLAPS * 0.6F && Alt < 10F)
                        {
                            setSpeedMode(8);
                            if(super.AP.way.isLandingOnShip() && super.CT.bHasArrestorControl)
                            {
                                if(((Tuple3d) (super.Vwld)).z < -5.5D)
                                    super.Vwld.z *= 0.94999998807907104D;
                                if(((Tuple3d) (super.Vwld)).z > 0.0D)
                                    super.Vwld.z *= 0.9100000262260437D;
                            } else
                            {
                                if(((Tuple3d) (super.Vwld)).z < -0.60000002384185791D)
                                    super.Vwld.z *= 0.93999999761581421D;
                                if(((Tuple3d) (super.Vwld)).z < -2.5D)
                                    super.Vwld.z = -2.5D;
                                if(((Tuple3d) (super.Vwld)).z > 0.0D)
                                    super.Vwld.z *= 0.9100000262260437D;
                            }
                        }
                        float f7 = super.Gears.Pitch - 2.0F;
                        if(f7 < 5F)
                            f7 = 5F;
                        if(Alt < 7F && super.Or.getTangage() < f7 || super.AP.way.isLandingOnShip())
                            super.CT.ElevatorControl += 1.5F * f;
                        super.CT.ElevatorControl += 0.05000000074505806D * ((Tuple3d) (getW())).y;
                        if(super.Gears.onGround())
                        {
                            if(super.Gears.Pitch > 5F)
                            {
                                if(super.Or.getTangage() < super.Gears.Pitch)
                                    super.CT.ElevatorControl += 1.5F * f;
                                if(!super.AP.way.isLandingOnShip())
                                    super.CT.ElevatorControl += 0.30000001192092896D * ((Tuple3d) (getW())).y;
                            } else
                            {
                                super.CT.ElevatorControl = 0.0F;
                            }
                            if(!TaxiMode)
                            {
                                AFo.setDeg(super.Or.getAzimut(), direction);
                                super.CT.RudderControl = 8F * AFo.getDiffRad() + 0.5F * (float)((Tuple3d) (getW())).z;
                            } else
                            {
                                super.CT.RudderControl = 0.0F;
                            }
                        }
                    }
                    super.AP.way.curr().getP(Po);
                    return;
                } else
                {
                    return;
                }

            case 66: // 'B'
                if(!isCapableOfTaxiing() || super.EI.getThrustOutput() < 0.01F)
                {
                    set_task(3);
                    maneuver = 0;
                    set_maneuver(49);
                    super.AP.setStabAll(false);
                } else
                {
                    if(super.AS.isPilotDead(0))
                    {
                        set_task(3);
                        maneuver = 0;
                        set_maneuver(44);
                        setSpeedMode(8);
                        smConstPower = 0.0F;
                        if(Airport.distToNearestAirport(super.Loc) > 900D)
                            ((Aircraft)super.actor).postEndAction(6000D, super.actor, 3, null);
                        else
                            MsgDestroy.Post(Time.current() + 0x493e0L, super.actor);
                    } else
                    {
                        P.x = ((Tuple3d) (super.Loc)).x;
                        P.y = ((Tuple3d) (super.Loc)).y;
                        P.z = ((Tuple3d) (super.Loc)).z;
                        Vcur.set(super.Vwld);
                        if(wayCurPos == null)
                        {
                            World.cur().airdrome.findTheWay((Pilot)this);
                            wayPrevPos = wayCurPos = getNextAirdromeWayPoint();
                        }
                        if(wayCurPos != null)
                        {
                            Point_Any point_any = wayCurPos;
                            Point_Any point_any1 = wayPrevPos;
                            Pcur.set((float)((Tuple3d) (P)).x, (float)((Tuple3d) (P)).y);
                            float f21 = Pcur.distance(point_any);
                            float f23 = Pcur.distance(point_any1);
                            V_to.sub(point_any, Pcur);
                            V_to.normalize();
                            float f26 = 5F + 0.1F * f21;
                            if(f26 > 12F)
                                f26 = 12F;
                            if(f26 > 0.9F * super.VminFLAPS)
                                f26 = 0.9F * super.VminFLAPS;
                            if(curAirdromePoi < airdromeWay.length && f21 < 15F || f21 < 4F)
                            {
                                f26 = 0.0F;
                                Point_Any point_any2 = getNextAirdromeWayPoint();
                                if(point_any2 == null)
                                {
                                    super.CT.setPowerControl(0.0F);
                                    super.Loc.set(P);
                                    super.Loc.set(super.Loc);
                                    if(!finished)
                                    {
                                        finished = true;
                                        int k1 = 1000;
                                        if(wayCurPos != null)
                                            k1 = 0x249f00;
                                        super.actor.collide(true);
                                        super.Vwld.set(0.0D, 0.0D, 0.0D);
                                        super.CT.setPowerControl(0.0F);
                                        super.EI.setCurControlAll(true);
                                        super.EI.setEngineStops();
                                        super.actor = super.actor;
                                        World.cur();
                                        if(super.actor != World.getPlayerAircraft())
                                        {
                                            MsgDestroy.Post(Time.current() + (long)k1, super.actor);
                                            setStationedOnGround(true);
                                            maneuver = 0;
                                            set_maneuver(44);
                                        }
                                        return;
                                    } else
                                    {
                                        return;
                                    }
                                }
                                wayPrevPos = wayCurPos;
                                wayCurPos = point_any2;
                            }
                            V_to.scale(f26);
                            float f30 = 2.0F * f;
                            Vdiff.set(V_to);
                            Vdiff.sub(Vcur);
                            float f32 = (float)Vdiff.length();
                            if(f32 > f30)
                            {
                                Vdiff.normalize();
                                Vdiff.scale(f30);
                            }
                            Vcur.add(Vdiff);
                            tmpOr.setYPR(FMMath.RAD2DEG((float)Vcur.direction()), super.Or.getPitch(), 0.0F);
                            super.Or.interpolate(tmpOr, 0.2F);
                            super.Vwld.x = ((Tuple2d) (Vcur)).x;
                            super.Vwld.y = ((Tuple2d) (Vcur)).y;
                            P.x += ((Tuple2d) (Vcur)).x * (double)f;
                            P.y += ((Tuple2d) (Vcur)).y * (double)f;
                        } else
                        {
                            wayPrevPos = wayCurPos = new Point_Null((float)((Tuple3d) (super.Loc)).x, (float)((Tuple3d) (super.Loc)).y);
                            super.actor = super.actor;
                            World.cur();
                            if(super.actor != World.getPlayerAircraft())
                            {
                                MsgDestroy.Post(Time.current() + 30000L, super.actor);
                                setStationedOnGround(true);
                            }
                        }
                        super.Loc.set(P);
                        super.Loc.set(super.Loc);
                        return;
                    }
                    return;
                }
                break;

            case 49: // '1'
                emergencyLanding(f);
                break;

            case 64: // '@'
                if(super.actor instanceof TypeGlider)
                {
                    pop();
                    break;
                }
                if(super.actor instanceof HE_LERCHE3)
                {
                    boolean flag = Actor.isAlive(super.AP.way.takeoffAirport) && (super.AP.way.takeoffAirport instanceof AirportCarrier);
                    if(!flag)
                        super.callSuperUpdate = false;
                }
                super.CT.BrakeControl = 1.0F;
                super.CT.ElevatorControl = 0.5F;
                super.CT.setPowerControl(0.0F);
                super.EI.setCurControlAll(false);
                setSpeedMode(0);
                if(World.Rnd().nextFloat() < 0.05F)
                {
                    super.EI.setCurControl(submaneuver, true);
                    if(super.EI.engines[submaneuver].getStage() == 0)
                    {
                        setSpeedMode(0);
                        super.CT.setPowerControl(0.05F);
                        super.EI.engines[submaneuver].setControlThrottle(0.2F);
                        super.EI.toggle();
                        if((super.actor instanceof BI_1) || (super.actor instanceof BI_6))
                        {
                            pop();
                            break;
                        }
                    }
                }
                if(super.EI.engines[submaneuver].getStage() == 6)
                {
                    setSpeedMode(0);
                    super.EI.engines[submaneuver].setControlThrottle(0.0F);
                    submaneuver++;
                    super.CT.setPowerControl(0.0F);
                    if(submaneuver > super.EI.getNum() - 1)
                    {
                        super.EI.setCurControlAll(true);
                        pop();
                    }
                }
                break;

            case 26: // '\032'
                float f8 = Alt;
                float f18 = 0.4F;
                if(Actor.isAlive(super.AP.way.takeoffAirport) && (super.AP.way.takeoffAirport instanceof AirportCarrier))
                {
                    f8 -= ((AirportCarrier)super.AP.way.takeoffAirport).height();
                    f18 = 0.95F;
                    if(Alt < 9F && ((Tuple3d) (super.Vwld)).z < 0.0D)
                        super.Vwld.z *= 0.84999999999999998D;
                    if(super.CT.bHasCockpitDoorControl && !bStage6)
                        super.AS.setCockpitDoor(super.actor, 1);
                }
                if(first)
                {
                    setCheckGround(false);
                    super.CT.BrakeControl = 1.0F;
                    super.CT.GearControl = 1.0F;
                    super.CT.setPowerControl(0.0F);
                    if(super.CT.bHasArrestorControl)
                        super.AS.setArrestor(super.actor, 0);
                    setSpeedMode(8);
                    if(f8 > 7.21F && super.AP.way.Cur() == 0)
                    {
                        super.EI.setEngineRunning();
                        Aircraft.debugprintln(super.actor, "in the air - engines running!.");
                    }
                    if(!Actor.isAlive(super.AP.way.takeoffAirport))
                        direction = super.actor.pos.getAbsOrient().getAzimut();
                    if(super.actor instanceof HE_LERCHE3)
                    {
                        maneuver = 69;
                        break;
                    }
                }
                if(super.Gears.onGround())
                {
                    if(super.EI.engines[0].getStage() == 0)
                    {
                        super.CT.setPowerControl(0.0F);
                        setSpeedMode(8);
                        if(World.Rnd().nextFloat() < 0.05F && mn_time > 1.0F || mn_time > 8F)
                        {
                            push();
                            push(64);
                            submaneuver = 0;
                            maneuver = 0;
                            safe_pop();
                            break;
                        }
                    } else
                    {
                        if(!bAlreadyCheckedStage7)
                        {
                            if(super.AP.way.takeoffAirport instanceof AirportCarrier)
                            {
                                AirportCarrier airportcarrier = (AirportCarrier)super.AP.way.takeoffAirport;
                                BigshipGeneric bigshipgeneric = airportcarrier.ship();
                                //TODO: Edited to make use of new Catapults.ini
                                bHasCatapult = super.Gears.setCatapultOffset(bigshipgeneric, new SectFile("com/maddox/il2/objects/Catapults.ini"));
                                bCatapultAI = super.Gears.getCatapultAI();
                            } else
                            {
                                bStage7 = true;
                            }
                            bAlreadyCheckedStage7 = true;
                        }
                        if(!bCatapultAI)
                        {
                            Po.set(super.Loc);
                            Vpl.set(60D, 0.0D, 0.0D);
                            fNearestDistance = 70F;
                            super.Or.transform(Vpl);
                            Po.add(Vpl);
                            Pd.set(Po);
                        } else
                        {
                            Po.set(super.Loc);
                            Vpl.set(200D, 0.0D, 0.0D);
                            fNearestDistance = 210F;
                            super.Or.transform(Vpl);
                            Po.add(Vpl);
                            Pd.set(Po);
                        }
                        if(canTakeoff)
                        {
                            if(!bStage7)
                            {
                                if(bStage6)
                                {
                                    if(bFastLaunchAI || !super.CT.bHasWingControl || super.CT.bHasWingControl && super.CT.getWing() < 0.5F)
                                        bStage7 = true;
                                } else
                                if(bStage4)
                                {
                                    if(super.CT.bHasWingControl && super.CT.getWing() > 0.001F)
                                    {
                                        if(bFastLaunchAI)
                                            super.CT.forceWing(0.0F);
                                        super.AS.setWingFold(super.actor, 0);
                                    }
                                    //TODO: DBW AI Mod Edits
                                    if(super.CT.bHasCockpitDoorControl && super.CT.bNoCarrierCanopyOpen)
                                    {
                                        if(bFastLaunchAI)
                                            super.CT.forceCockpitDoor(0.0F);
                                        super.AS.setCockpitDoor(super.actor, 0);
                                    }
                                    bStage6 = true;
                                } else
                                if(bStage3)
                                {
                                    Loc loc = new Loc();
                                    BigshipGeneric bigshipgeneric1 = (BigshipGeneric)super.brakeShoeLastCarrier;
                                    Aircraft aircraft = (Aircraft)super.actor;
                                    CellAirField cellairfield = bigshipgeneric1.getCellTO();
                                    double d3;
                                    double d6;
                                    if(bCatapultAI)
                                    {
                                        d3 = -((Tuple3d) (cellairfield.leftUpperCorner())).x - super.Gears.getCatapultOffsetX();
                                        d6 = ((Tuple3d) (cellairfield.leftUpperCorner())).y - super.Gears.getCatapultOffsetY();
                                    } else
                                    {
                                        d3 = -((Tuple3d) (cellairfield.leftUpperCorner())).x - (double)(cellairfield.getWidth() / 2 - 3);
                                        d6 = super.brakeShoeLoc.getX() + (double)aircraft.getCellAirPlane().getHeight() + 4D;
                                    }
                                    loc.set(d6, d3, super.brakeShoeLoc.getZ(), super.brakeShoeLoc.getAzimut(), super.brakeShoeLoc.getTangage(), super.brakeShoeLoc.getKren());
                                    loc.add(super.brakeShoeLastCarrier.pos.getAbs());
                                    super.actor.pos.setAbs(loc.getPoint());
                                    super.brakeShoeLoc.set(super.actor.pos.getAbs());
                                    super.brakeShoeLoc.sub(super.brakeShoeLastCarrier.pos.getAbs());
                                    bStage4 = true;
                                } else
                                {
                                    super.CT.setPowerControl(1.0F);
                                    bStage3 = true;
                                }
                            } else
                            {
                                super.CT.setPowerControl(1.1F);
                                setSpeedMode(9);
                            }
                        } else
                        {
                            setSpeedMode(8);
                            super.CT.BrakeControl = 1.0F;
                            boolean flag2 = true;
                            if(mn_time < 8F)
                                flag2 = false;
                            if(super.actor != War.getNearestFriendAtPoint(Pd, (Aircraft)super.actor, fNearestDistance))
                                if(super.actor instanceof G4M2E)
                                {
                                    if(War.getNearestFriendAtPoint(Pd, (Aircraft)super.actor, fNearestDistance) != ((G4M2E)super.actor).typeDockableGetDrone())
                                        flag2 = false;
                                } else
                                {
                                    flag2 = false;
                                }
                            if(Actor.isAlive(super.AP.way.takeoffAirport) && super.AP.way.takeoffAirport.takeoffRequest > 0)
                                flag2 = false;
                            if(flag2)
                            {
                                canTakeoff = true;
                                if(Actor.isAlive(super.AP.way.takeoffAirport))
                                    super.AP.way.takeoffAirport.takeoffRequest = 300;
                            }
                        }
                    }
                    if(super.EI.engines[0].getStage() == 6 && super.CT.bHasWingControl && super.CT.getWing() > 0.001F && !(super.AP.way.takeoffAirport instanceof AirportCarrier))
                        super.AS.setWingFold(super.actor, 0);
                } else
                if(canTakeoff)
                {
                    super.CT.setPowerControl(1.1F);
                    setSpeedMode(9);
                }
                //TODO: Blown Flaps
                if(CT.FlapsControl == 0.0F && CT.getWing() < 0.001F){
                    CT.FlapsControl = 0.4F;
                    if(CT.bHasBlownFlaps)
                    	CT.BlownFlapsControl = 1.0F;
                }
                if(super.EI.engines[0].getStage() == 6 && super.CT.getPower() > f18)
                {
                    super.CT.BrakeControl = 0.0F;
                    super.brakeShoe = false;
                    float f22 = (super.Vmin * super.M.getFullMass()) / super.M.massEmpty;
                    float f24 = 12F * (getSpeed() / f22 - 0.2F);
                    if(super.Gears.bIsSail)
                        f24 *= 2.0F;
                    if(super.Gears.bFrontWheel)
                        f24 = super.Gears.Pitch + 4F;
                    if(f24 < 1.0F)
                        f24 = 1.0F;
                    if(f24 > 10F)
                        f24 = 10F;
                    float f27 = 1.5F;
                    if(Actor.isAlive(super.AP.way.takeoffAirport) && (super.AP.way.takeoffAirport instanceof AirportCarrier) && !super.Gears.isUnderDeck())
                    {
                        super.CT.GearControl = 0.0F;
                        if(f8 < 0.0F)
                        {
                            f24 = 18F;
                            f27 = 0.05F;
                        } else
                        {
                            f24 = 14F;
                            f27 = 0.3F;
                        }
                    }
                    if(super.Or.getTangage() < f24)
                        dA = -0.7F * (super.Or.getTangage() - f24) + f27 * (float)((Tuple3d) (getW())).y + 0.5F * (float)((Tuple3d) (getAW())).y;
                    else
                        dA = -0.1F * (super.Or.getTangage() - f24) + f27 * (float)((Tuple3d) (getW())).y + 0.5F * (float)((Tuple3d) (getAW())).y;
                    super.CT.ElevatorControl += (dA - super.CT.ElevatorControl) * 3F * f;
                } else
                {
                    super.CT.ElevatorControl = 1.0F;
                }
                AFo.setDeg(super.Or.getAzimut(), direction);
                double d1 = AFo.getDiffRad();
                if(super.EI.engines[0].getStage() == 6)
                {
                    super.CT.RudderControl = 8F * (float)d1;
                    if(d1 > -1D && d1 < 1.0D)
                    {
                        if(Actor.isAlive(super.AP.way.takeoffAirport) && super.CT.getPower() > 0.3F)
                        {
                            double d2 = super.AP.way.takeoffAirport.ShiftFromLine(this);
                            double d3 = 3D - 3D * Math.abs(d1);
                            if(d3 > 1.0D)
                                d3 = 1.0D;
                            double d5 = 0.25D * d2 * d3;
                            if(d5 > 1.5D)
                                d5 = 1.5D;
                            if(d5 < -1.5D)
                                d5 = -1.5D;
                            super.CT.RudderControl += (float)d5;
                        }
                    } else
                    {
                        super.CT.BrakeControl = 1.0F;
                    }
                }
                super.CT.AileronControl = -0.05F * super.Or.getKren() + 0.3F * (float)((Tuple3d) (getW())).y;
                if(f8 > 5F && !super.Gears.isUnderDeck())
                    super.CT.GearControl = 0.0F;
                float f28 = 1.0F;
                if(hasBombs())
                    f28 *= 1.7F;
                if(f8 > 120F * f28 || getSpeed() > super.Vmin * 1.8F * f28 || f8 > 80F * f28 && getSpeed() > super.Vmin * 1.6F * f28 || f8 > 40F * f28 && getSpeed() > super.Vmin * 1.3F * f28 && mn_time > 60F + (float)((Aircraft)super.actor).aircIndex() * 3F)
                {
                    super.CT.FlapsControl = 0.0F;
                    super.CT.GearControl = 0.0F;
                    rwLoc = null;
                    if(super.actor instanceof TypeGlider)
                        push(24);
                    maneuver = 0;
                    super.brakeShoe = false;
                    super.turnOffCollisions = false;
                    if(super.CT.bHasCockpitDoorControl)
                        super.AS.setCockpitDoor(super.actor, 0);
                    pop();
                }
                if(super.actor instanceof TypeGlider)
                {
                    super.CT.BrakeControl = 0.0F;
                    super.CT.ElevatorControl = 0.05F;
                    canTakeoff = true;
                }
                break;

            case 69: // 'E'
                float f9 = Alt;
                float f19 = 0.4F;
                super.CT.BrakeControl = 1.0F;
                super.W.scale(0.0D);
                boolean flag4 = Actor.isAlive(super.AP.way.takeoffAirport) && (super.AP.way.takeoffAirport instanceof AirportCarrier);
                if(flag4)
                {
                    f9 -= ((AirportCarrier)super.AP.way.takeoffAirport).height();
                    f19 = 0.8F;
                    if(Alt < 9F && ((Tuple3d) (super.Vwld)).z < 0.0D)
                        super.Vwld.z *= 0.84999999999999998D;
                    if(super.CT.bHasCockpitDoorControl)
                        super.AS.setCockpitDoor(super.actor, 1);
                }
                if(((Tuple3d) (super.Loc)).z != 0.0D && ((Tuple3d) (((HE_LERCHE3)super.actor).suka.getPoint())).z == 0.0D)
                    ((HE_LERCHE3)super.actor).suka.set(super.Loc, super.Or);
                if(((Tuple3d) (super.Loc)).z != 0.0D)
                    if(super.EI.getPowerOutput() < f19 && !flag4)
                    {
                        super.callSuperUpdate = false;
                        super.Loc.set(((HE_LERCHE3)super.actor).suka.getPoint());
                        super.Or.set(((HE_LERCHE3)super.actor).suka.getOrient());
                    } else
                    if(f9 < 100F)
                        super.Or.set(((HE_LERCHE3)super.actor).suka.getOrient());
                if(super.Gears.onGround() && super.EI.engines[0].getStage() == 0)
                {
                    super.CT.setPowerControl(0.0F);
                    setSpeedMode(8);
                    if(World.Rnd().nextFloat() < 0.05F && mn_time > 1.0F || mn_time > 8F)
                    {
                        push();
                        push(64);
                        submaneuver = 0;
                        maneuver = 0;
                        safe_pop();
                        break;
                    }
                }
                if(super.EI.engines[0].getStage() == 6)
                {
                    super.CT.BrakeControl = 0.0F;
                    super.CT.AirBrakeControl = 1.0F;
                    super.brakeShoe = false;
                    super.CT.setPowerControl(1.1F);
                    setSpeedMode(11);
                }
                if(f9 > 200F)
                {
                    super.CT.GearControl = 0.0F;
                    rwLoc = null;
                    super.CT.ElevatorControl = -1F;
                    super.CT.AirBrakeControl = 0.0F;
                    if(super.Or.getTangage() < 25F)
                        maneuver = 0;
                    super.brakeShoe = false;
                    super.turnOffCollisions = false;
                    if(super.CT.bHasCockpitDoorControl)
                        super.AS.setCockpitDoor(super.actor, 0);
                    pop();
                }
                break;

            case 28: // '\034'
                if(first)
                {
                    direction = World.Rnd().nextFloat(-25F, 25F);
                    super.AP.setStabAll(false);
                    setSpeedMode(6);
                    super.CT.RudderControl = World.Rnd().nextFloat(-0.22F, 0.22F);
                    submaneuver = 0;
                    if(getSpeed() < super.Vmin * 1.5F)
                        pop();
                }
                super.CT.AileronControl = -0.04F * (super.Or.getKren() - direction);
                switch(submaneuver)
                {
                case 0: // '\0'
                    dA = 1.0F;
                    maxAOA = 12F + 1.0F * (float)super.Skill;
                    if(super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.6F * f;
                    else
                        super.CT.ElevatorControl += 3.3F * f;
                    if(super.Or.getTangage() > 40F + 5.1F * (float)super.Skill)
                        submaneuver++;
                    break;

                case 1: // '\001'
                    direction = World.Rnd().nextFloat(-25F, 25F);
                    super.CT.RudderControl = World.Rnd().nextFloat(-0.75F, 0.75F);
                    submaneuver++;
                    // fall through

                case 2: // '\002'
                    dA = -1F;
                    maxAOA = 12F + 5F * (float)super.Skill;
                    if(super.AOA < -maxAOA || super.CT.ElevatorControl < dA)
                        super.CT.ElevatorControl += 0.6F * f;
                    else
                        super.CT.ElevatorControl -= 3.3F * f;
                    if(super.Or.getTangage() < -45F)
                        pop();
                    break;
                }
                if(mn_time > 3F)
                    pop();
                break;

            case 29: // '\035'
                super.minElevCoeff = 17F;
                if(first)
                {
                    super.AP.setStabAll(false);
                    setSpeedMode(9);
                    sub_Man_Count = 0;
                }
                if(danger != null)
                {
                    if(sub_Man_Count == 0)
                    {
                        tmpV3d.set(danger.getW());
                        ((FlightModelMain) (danger)).Or.transform(tmpV3d);
                        if(((Tuple3d) (tmpV3d)).z > 0.0D)
                            sinKren = -World.Rnd().nextFloat(60F, 90F);
                        else
                            sinKren = World.Rnd().nextFloat(60F, 90F);
                    }
                    if(super.Loc.distanceSquared(((FlightModelMain) (danger)).Loc) < 5000D)
                    {
                        setSpeedMode(8);
                        super.CT.setPowerControl(0.0F);
                    } else
                    {
                        setSpeedMode(9);
                    }
                } else
                {
                    pop();
                }
                super.CT.FlapsControl = 0.2F;
                if((double)getSpeed() < 120D){
                    CT.FlapsControl = 0.33F;
                    if(CT.bHasBlownFlaps)
                    	CT.BlownFlapsControl = 1.0F;
                }
                if((double)getSpeed() < 80D){
                    CT.FlapsControl = 1.0F;
                    if(CT.bHasBlownFlaps)
                    	CT.BlownFlapsControl = 1.0F;
                }
                super.CT.AileronControl = -0.08F * (super.Or.getKren() + sinKren);
                super.CT.ElevatorControl = 0.9F;
                super.CT.RudderControl = 0.0F;
                sub_Man_Count++;
                if(sub_Man_Count > 15)
                    sub_Man_Count = 0;
                if(mn_time > 10F)
                    pop();
                break;

            case 56: // '8'
                if(first)
                {
                    submaneuver = World.Rnd().nextInt(0, 1);
                    direction = World.Rnd().nextFloat(-20F, -10F);
                }
                super.CT.AileronControl = -0.08F * (super.Or.getKren() - direction);
                switch(submaneuver)
                {
                case 0: // '\0'
                    dA = 1.0F;
                    maxAOA = 10F + 2.0F * (float)super.Skill;
                    if((double)getOverload() > 1.0D / Math.abs(Math.cos(Math.toRadians(super.Or.getKren()))) || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.2F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    super.CT.RudderControl = -1F * (float)Math.sin(Math.toRadians(super.Or.getKren() + 55F));
                    if(mn_time > 1.5F)
                        submaneuver++;
                    break;

                case 1: // '\001'
                    direction = World.Rnd().nextFloat(10F, 20F);
                    submaneuver++;
                    // fall through

                case 2: // '\002'
                    dA = 1.0F;
                    maxAOA = 10F + 2.0F * (float)super.Skill;
                    if((double)getOverload() > 1.0D / Math.abs(Math.cos(Math.toRadians(super.Or.getKren()))) || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.2F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    super.CT.RudderControl = 1.0F * (float)Math.sin(Math.toRadians(super.Or.getKren() - 55F));
                    if(mn_time > 4.5F)
                        submaneuver++;
                    break;

                case 3: // '\003'
                    direction = World.Rnd().nextFloat(-20F, -10F);
                    submaneuver++;
                    // fall through

                case 4: // '\004'
                    dA = 1.0F;
                    maxAOA = 10F + 2.0F * (float)super.Skill;
                    if((double)getOverload() > 1.0D / Math.abs(Math.cos(Math.toRadians(super.Or.getKren()))) || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.2F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    super.CT.RudderControl = -1F * (float)Math.sin(Math.toRadians(super.Or.getKren() + 55F));
                    if(mn_time > 6F)
                        pop();
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
                if(!isCapableOfACM() && World.Rnd().nextInt(-2, 9) < super.Skill)
                    ((Aircraft)super.actor).hitDaSilk();
                if(first)
                {
                    super.AP.setStabAll(false);
                    setSpeedMode(6);
                    submaneuver = 0;
                    direction = 178F - World.Rnd().nextFloat(0.0F, 8F) * (float)super.Skill;
                }
                maxAOA = ((Tuple3d) (super.Vwld)).z <= 0.0D ? 24F : 14F;
                if(danger != null)
                {
                    Ve.sub(((FlightModelMain) (danger)).Loc, super.Loc);
                    dist = (float)Ve.length();
                    super.Or.transformInv(Ve);
                    Vpl.set(((FlightModelMain) (danger)).Vwld);
                    super.Or.transformInv(Vpl);
                    Vpl.normalize();
                }
                switch(submaneuver)
                {
                case 0: // '\0'
                    dA = super.Or.getKren() - 180F;
                    if(dA < -180F)
                        dA += 360F;
                    dA *= -0.08F;
                    super.CT.AileronControl = dA;
                    super.CT.RudderControl = dA <= 0.0F ? -1F : 1.0F;
                    super.CT.ElevatorControl = 0.01111111F * Math.abs(super.Or.getKren());
                    if(mn_time > 2.0F || Math.abs(super.Or.getKren()) > direction)
                    {
                        submaneuver++;
                        super.CT.RudderControl = World.Rnd().nextFloat(-0.5F, 0.5F);
                        direction = World.Rnd().nextFloat(-60F, -30F);
                        mn_time = 0.5F;
                    }
                    break;

                case 1: // '\001'
                    dA = super.Or.getKren() - 180F;
                    if(dA < -180F)
                        dA += 360F;
                    dA *= -0.04F;
                    super.CT.AileronControl = dA;
                    if(super.Or.getTangage() > direction + 5F && getOverload() < maxG && super.AOA < maxAOA)
                    {
                        if(super.CT.ElevatorControl < 0.0F)
                            super.CT.ElevatorControl = 0.0F;
                        super.CT.ElevatorControl += 1.0F * f;
                    } else
                    {
                        if(super.CT.ElevatorControl > 0.0F)
                            super.CT.ElevatorControl = 0.0F;
                        super.CT.ElevatorControl -= 1.0F * f;
                    }
                    if(mn_time > 2.0F && super.Or.getTangage() < direction + 20F)
                        submaneuver++;
                    if(danger != null)
                    {
                        if(super.Skill >= 2 && super.Or.getTangage() < -30F && ((Tuple3d) (Vpl)).x > 0.99862951040267944D)
                            submaneuver++;
                        if(super.Skill >= 3 && Math.abs(super.Or.getTangage()) > 145F && Math.abs(((FlightModelMain) (danger)).Or.getTangage()) > 135F && dist < 400F)
                            submaneuver++;
                    }
                    break;

                case 2: // '\002'
                    direction = World.Rnd().nextFloat(-60F, 60F);
                    setSpeedMode(6);
                    submaneuver++;
                    // fall through

                case 3: // '\003'
                    dA = super.Or.getKren() - direction;
                    super.CT.AileronControl = -0.04F * dA;
                    super.CT.RudderControl = dA <= 0.0F ? -1F : 1.0F;
                    super.CT.ElevatorControl = 0.5F;
                    if(Math.abs(dA) < 4F + 3F * (float)super.Skill)
                        submaneuver++;
                    break;

                case 4: // '\004'
                    direction *= World.Rnd().nextFloat(0.5F, 1.0F);
                    setSpeedMode(6);
                    submaneuver++;
                    // fall through

                case 5: // '\005'
                    dA = super.Or.getKren() - direction;
                    super.CT.AileronControl = -0.04F * dA;
                    super.CT.RudderControl = -0.1F * getAOS();
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA || super.Or.getTangage() > 40F)
                        super.CT.ElevatorControl -= 0.8F * f;
                    else
                        super.CT.ElevatorControl += 1.6F * f;
                    if(super.Or.getTangage() > 80F || mn_time > 4F || getSpeed() < super.Vmin * 1.5F)
                        pop();
                    if(danger != null && (((Tuple3d) (Ve)).z < -1D || dist > 600F || ((Tuple3d) (Vpl)).x < 0.78801000118255615D))
                        pop();
                    break;
                }
                if((double)Alt < -7D * ((Tuple3d) (super.Vwld)).z)
                    pop();
                break;

            case 33: // '!'
                if(first)
                {
                    if(Alt < 1000F)
                    {
                        pop();
                        break;
                    }
                    super.AP.setStabAll(false);
                    submaneuver = 0;
                    direction = (World.Rnd().nextBoolean() ? 1.0F : -1F) * World.Rnd().nextFloat(107F, 160F);
                }
                maxAOA = ((Tuple3d) (super.Vwld)).z <= 0.0D ? 24F : 14F;
                switch(submaneuver)
                {
                case 0: // '\0'
                    if(Math.abs(super.Or.getKren()) < 45F)
                        super.CT.ElevatorControl = 0.005555556F * Math.abs(super.Or.getKren());
                    else
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > 1.0F)
                        super.CT.ElevatorControl -= 0.2F * f;
                    else
                        super.CT.ElevatorControl += 1.2F * f;
                    dA = super.Or.getKren() - direction;
                    super.CT.AileronControl = -0.04F * dA;
                    super.CT.RudderControl = -0.1F * getAOS();
                    if(Math.abs(dA) < 4F + 1.0F * (float)super.Skill)
                        submaneuver++;
                    break;

                case 1: // '\001'
                    setSpeedMode(7);
                    smConstPower = 0.5F;
                    super.CT.AileronControl = 0.0F;
                    super.CT.RudderControl = -0.1F * getAOS();
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.2F * f;
                    else
                        super.CT.ElevatorControl += 1.2F * f;
                    if(super.Or.getTangage() < -60F)
                        submaneuver++;
                    break;

                case 2: // '\002'
                    if(super.Or.getTangage() > -45F)
                    {
                        super.CT.AileronControl = -0.04F * super.Or.getKren();
                        setSpeedMode(9);
                        maxAOA = 7F;
                    }
                    super.CT.RudderControl = -0.1F * getAOS();
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.8F * f;
                    else
                        super.CT.ElevatorControl += 0.4F * f;
                    if(super.Or.getTangage() > super.AOA - 10F || ((Tuple3d) (super.Vwld)).z > -1D)
                        pop();
                    break;
                }
                if((double)Alt < -7D * ((Tuple3d) (super.Vwld)).z)
                    pop();
                break;

            case 34: // '"'
                if(first)
                {
                    if(Alt < 500F)
                    {
                        pop();
                        break;
                    }
                    direction = super.Or.getTangage();
                    setSpeedMode(9);
                }
                dA = super.Or.getKren() - (super.Or.getKren() <= 0.0F ? -35F : 35F);
                super.CT.AileronControl = -0.04F * dA;
                super.CT.RudderControl = super.Or.getKren() <= 0.0F ? -1F : 1.0F;
                super.CT.ElevatorControl = -1F;
                if(direction > super.Or.getTangage() + 45F || super.Or.getTangage() < -60F || mn_time > 4F)
                    pop();
                break;

            case 36: // '$'
            case 37: // '%'
                if(first)
                {
                    if(!isCapableOfACM())
                        pop();
                    if(getSpeed() < super.Vmax * 0.5F)
                    {
                        pop();
                        break;
                    }
                    super.AP.setStabAll(false);
                    submaneuver = 0;
                    direction = World.Rnd().nextFloat(-30F, 80F);
                    setSpeedMode(9);
                }
                maxAOA = ((Tuple3d) (super.Vwld)).z <= 0.0D ? 24F : 14F;
                switch(submaneuver)
                {
                case 0: // '\0'
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                    super.CT.RudderControl = -0.1F * getAOS();
                    if(Math.abs(super.Or.getKren()) < 45F)
                        submaneuver++;
                    break;

                case 1: // '\001'
                    super.CT.AileronControl = 0.0F;
                    dA = 1.0F;
                    if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > dA)
                        super.CT.ElevatorControl -= 0.4F * f;
                    else
                        super.CT.ElevatorControl += 0.8F * f;
                    if(super.Or.getTangage() > direction)
                        submaneuver++;
                    if(getSpeed() < super.Vmin * 1.25F)
                        pop();
                    break;

                case 2: // '\002'
                    push(maneuver != 36 ? 35 : 7);
                    pop();
                    break;
                }
                break;

            case 38: // '&'
                if(first)
                    super.CT.RudderControl = super.Or.getKren() <= 0.0F ? -1F : 1.0F;
                super.CT.AileronControl += -0.02F * super.Or.getKren();
                if(super.CT.AileronControl > 0.1F)
                    super.CT.AileronControl = 0.1F;
                if(super.CT.AileronControl < -0.1F)
                    super.CT.AileronControl = -0.1F;
                dA = (getSpeedKMH() - 180F - super.Or.getTangage() * 10F - getVertSpeed() * 5F) * 0.004F;
                super.CT.ElevatorControl = dA;
                if(mn_time > 3.5F)
                    pop();
                break;

            case 39: // '\''
                setSpeedMode(6);
                super.CT.AileronControl = -0.04F * super.Or.getKren();
                super.CT.ElevatorControl = -0.04F * (super.Or.getTangage() + 10F);
                if(super.CT.RudderControl > 0.1F)
                    super.CT.RudderControl = 0.8F;
                else
                if(super.CT.RudderControl < -0.1F)
                    super.CT.RudderControl = -0.8F;
                else
                    super.CT.RudderControl = super.Or.getKren() <= 0.0F ? -1F : 1.0F;
                if(getSpeed() > super.Vmax || mn_time > 7F)
                    pop();
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

            case 46: // '.'
                if(target_ground == null || target_ground.pos == null)
                {
                    if(Group != null)
                    {
                        dont_change_subm = true;
                        boolean flag1 = isBusy();
                        int k = Group.grTask;
                        AirGroup airgroup = Group;
                        if(Group == null);
                        airgroup.grTask = 4;
                        setBusy(false);
                        Group.setTaskAndManeuver(Group.numInGroup((Aircraft)super.actor));
                        setBusy(flag1);
                        Group.grTask = k;
                    }
                    if(target_ground == null || target_ground.pos == null)
                    {
                        super.AP.way.first();
                        Airport airport = Airport.nearest(super.AP.way.curr().getP(), super.actor.getArmy(), 7);
                        WayPoint waypoint;
                        if(airport != null)
                            waypoint = new WayPoint(((Actor) (airport)).pos.getAbsPoint());
                        else
                            waypoint = new WayPoint(super.AP.way.first().getP());
                        waypoint.set(0.6F * super.Vmax);
                        waypoint.Action = 2;
                        super.AP.way.add(waypoint);
                        super.AP.way.last();
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
            case 79: // 'O'
            case 80: // 'P'
            case 81: // 'Q'
                if(first && !isCapableOfACM())
                {
                    bombsOut = true;
                    setReadyToReturn(true);
                    if(Group != null)
                    {
                        Group.waitGroup(Group.numInGroup((Aircraft)super.actor));
                    } else
                    {
                        super.AP.way.next();
                        set_task(3);
                        clear_stack();
                        set_maneuver(21);
                    }
                    break;
                }
                if(target_ground == null || target_ground.pos == null || !Actor.isAlive(target_ground))
                {
                    int i1 = maneuver;
                    if(Group != null)
                    {
                        AirGroup _tmp1 = Group;
                        if(Group.grTask == 4)
                        {
                            dont_change_subm = true;
                            boolean flag6 = isBusy();
                            setBusy(false);
                            Group.setTaskAndManeuver(Group.numInGroup((Aircraft)super.actor));
                            setBusy(flag6);
                        }
                    }
                    if(target_ground == null || target_ground.pos == null || !Actor.isAlive(target_ground))
                    {
                        if(i1 == 50)
                            bombsOut = true;
                        if(Group != null)
                        {
                            Group.waitGroup(Group.numInGroup((Aircraft)super.actor));
                        } else
                        {
                            super.AP.way.next();
                            set_maneuver(21);
                        }
                        push(2);
                        pop();
                        break;
                    }
                }
                switch(maneuver)
                {
                default:
                    break;

                case 43: // '+'
                    groundAttack(target_ground, f);
                    if(mn_time > 120F)
                        set_maneuver(0);
                    break;

                case 50: // '2'
                    groundAttackDiveBomber(target_ground, f);
                    if(mn_time > 120F)
                    {
                        setSpeedMode(6);
                        super.CT.BayDoorControl = 0.0F;
                        super.CT.AirBrakeControl = 0.0F;
                        super.CT.FlapsControl = 0.0F;
                        pop();
                        sub_Man_Count = 0;
                    }
                    break;

                case 51: // '3'
                    groundAttackTorpedo(target_ground, f);
                    break;

                case 81: // 'Q'
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

                case 79: // 'O'
                    groundAttackHS293(target_ground, f);
                    break;

                case 80: // 'P'
                    groundAttackFritzX(target_ground, f);
                    break;
                }
                break;
            }
            if(checkGround)
                doCheckGround(f);
            if(checkStrike && strikeEmer)
                doCheckStrike();
            strikeEmer = false;
            setSpeedControl(f);
            first = false;
            mn_time += f;
            if(frequentControl)
                super.AP.update(f);
            else
                super.AP.update(f * 4F);
            if(bBusy)
                wasBusy = true;
            else
                wasBusy = false;
            if(shotAtFriend > 0)
                shotAtFriend--;
        }
    }

    void OutCT(int i)
    {
        if(super.actor == Main3D.cur3D().viewActor())
        {
            TextScr.output(i + 5, 45, "Alt(MSL)  " + (int)((Tuple3d) (super.Loc)).z + "    " + (super.CT.BrakeControl <= 0.0F ? "" : "BRAKE"));
            TextScr.output(i + 5, 65, "Alt(AGL)  " + (int)(((Tuple3d) (super.Loc)).z - Engine.land().HQ_Air(((Tuple3d) (super.Loc)).x, ((Tuple3d) (super.Loc)).y)));
            int j = 0;
            TextScr.output(i + 225, 140, "---ENGINES (" + super.EI.getNum() + ")---" + super.EI.engines[j].getStage());
            TextScr.output(i + 245, 120, "THTL " + (int)(100F * super.EI.engines[j].getControlThrottle()) + "%" + (super.EI.engines[j].getControlAfterburner() ? " (NITROS)" : ""));
            TextScr.output(i + 245, 100, "PROP " + (int)(100F * super.EI.engines[j].getControlProp()) + "%" + (super.CT.getStepControlAuto() ? " (AUTO)" : ""));
            TextScr.output(i + 245, 80, "MIX " + (int)(100F * super.EI.engines[j].getControlMix()) + "%");
            TextScr.output(i + 245, 60, "RAD " + (int)(100F * super.EI.engines[j].getControlRadiator()) + "%" + (super.CT.getRadiatorControlAuto() ? " (AUTO)" : ""));
            TextScr.output(i + 245, 40, "SUPC " + super.EI.engines[j].getControlCompressor() + "x");
            TextScr.output(245, 20, "PropAoA :" + (int)Math.toDegrees(super.EI.engines[j].getPropAoA()));
            TextScr.output(i + 455, 120, "Cyls/Cams " + super.EI.engines[j].getCylindersOperable() + "/" + super.EI.engines[0].getCylinders());
            TextScr.output(i + 455, 100, "Readyness " + (int)(100F * super.EI.engines[j].getReadyness()) + "%");
            TextScr.output(i + 455, 80, "PRM " + (int)((float)(int)(super.EI.engines[j].getRPM() * 0.02F) * 50F) + " rpm");
            TextScr.output(i + 455, 60, "Thrust " + (int)((Tuple3f) (super.EI.engines[j].getEngineForce())).x + " N");
            TextScr.output(i + 455, 40, "Fuel " + (int)((100F * super.M.fuel) / super.M.maxFuel) + "% Nitro " + (int)((100F * super.M.nitro) / super.M.maxNitro) + "%");
            TextScr.output(i + 455, 20, "MPrs " + (int)(1000F * super.EI.engines[j].getManifoldPressure()) + " mBar");
            TextScr.output(i + 640, 140, "---Controls---");
            TextScr.output(i + 640, 120, "A/C: " + (super.CT.bHasAileronControl ? "" : "AIL ") + (super.CT.bHasElevatorControl ? "" : "ELEV ") + (super.CT.bHasRudderControl ? "" : "RUD ") + (super.Gears.bIsHydroOperable ? "" : "GEAR "));
            TextScr.output(i + 640, 100, "ENG: " + (super.EI.engines[j].isHasControlThrottle() ? "" : "THTL ") + (super.EI.engines[j].isHasControlProp() ? "" : "PROP ") + (super.EI.engines[j].isHasControlMix() ? "" : "MIX ") + (super.EI.engines[j].isHasControlCompressor() ? "" : "SUPC ") + (super.EI.engines[j].isPropAngleDeviceOperational() ? "" : "GVRNR "));
            TextScr.output(i + 640, 80, "PIL: (" + (int)(super.AS.getPilotHealth(0) * 100F) + "%)");
            TextScr.output(i + 5, 105, "V   " + (int)getSpeedKMH());
            TextScr.output(i + 5, 125, "AOA " + (float)(int)(getAOA() * 1000F) / 1000F);
            TextScr.output(i + 5, 145, "AOS " + (float)(int)(getAOS() * 1000F) / 1000F);
            TextScr.output(i + 5, 165, "Kr  " + (int)super.Or.getKren());
            TextScr.output(i + 5, 185, "Ta  " + (int)super.Or.getTangage());
            TextScr.output(i + 250, 185, "way.speed  " + super.AP.way.curr().getV() * 3.6F + "way.z " + super.AP.way.curr().z() + "  mn_time = " + mn_time);
            TextScr.output(i + 5, 205, "<" + super.actor.name() + ">: " + ManString.tname(task) + ":" + ManString.name(maneuver) + " , WP=" + super.AP.way.Cur() + "(" + (super.AP.way.size() - 1) + ")-" + ManString.wpname(super.AP.way.curr().Action));
            TextScr.output(i + 7, 225, "======= " + ManString.name(program[0]) + "  Sub = " + submaneuver + " follOffs.x = " + ((Tuple3d) (followOffset)).x + "  " + (((AutopilotAI)super.AP).bWayPoint ? "Stab WPoint " : "") + (((AutopilotAI)super.AP).bStabAltitude ? "Stab ALT " : "") + (((AutopilotAI)super.AP).bStabDirection ? "Stab DIR " : "") + (((AutopilotAI)super.AP).bStabSpeed ? "Stab SPD " : "   ") + (((Pilot)((SndAircraft) ((Aircraft)super.actor)).FM).isDumb() ? "DUMB " : " ") + (((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)super.actor)).FM)).Gears.lgear ? "L " : " ") + (((FlightModelMain) ((Pilot)((SndAircraft) ((Aircraft)super.actor)).FM)).Gears.rgear ? "R " : " ") + (((Maneuver) ((Pilot)((SndAircraft) ((Aircraft)super.actor)).FM)).TaxiMode ? "TaxiMode" : ""));
            TextScr.output(i + 7, 245, " ====== " + ManString.name(program[1]));
            TextScr.output(i + 7, 265, "  ===== " + ManString.name(program[2]));
            TextScr.output(i + 7, 285, "   ==== " + ManString.name(program[3]));
            TextScr.output(i + 7, 305, "    === " + ManString.name(program[4]));
            TextScr.output(i + 7, 325, "     == " + ManString.name(program[5]));
            TextScr.output(i + 7, 345, "      = " + ManString.name(program[6]) + "  " + (target != null ? "TARGET  " : "") + (target_ground != null ? "GROUND  " : "") + (danger != null ? "DANGER  " : ""));
            if(target != null && Actor.isValid(((Interpolate) (target)).actor))
                TextScr.output(i + 1, 365, " AT: (" + ((Interpolate) (target)).actor.name() + ") " + ((((Interpolate) (target)).actor instanceof Aircraft) ? "" : ((Interpolate) (target)).actor.getClass().getName()));
            if(target_ground != null && Actor.isValid(target_ground))
                TextScr.output(i + 1, 385, " GT: (" + target_ground.name() + ") ..." + target_ground.getClass().getName());
            TextScr.output(400, 500, "+");
            TextScr.output(400, 400, "|");
            TextScr.output((int)(400F + 200F * super.CT.AileronControl), (int)(500F - 200F * super.CT.ElevatorControl), "+");
            TextScr.output((int)(400F + 200F * super.CT.RudderControl), 400, "|");
            TextScr.output(250, 780, "followOffset  " + ((Tuple3d) (followOffset)).x + "  " + ((Tuple3d) (followOffset)).y + "  " + ((Tuple3d) (followOffset)).z + "  ");
            if(Group != null && Group.enemies != null)
                TextScr.output(250, 760, "Enemies   " + AirGroupList.length(Group.enemies[0]));
            TextScr.output(700, 780, "speedMode   " + speedMode);
            if(Group != null)
                TextScr.output(700, 760, "Group task  " + Group.grTaskName());
            if(super.AP.way.isLandingOnShip())
                TextScr.output(5, 460, "Landing On Carrier");
            TextScr.output(700, 740, "gattackCounter  " + gattackCounter);
            TextScr.output(5, 360, "silence = " + silence);
        }
    }

    private void groundAttackGuns(Actor actor, float f)
    {
        if(submaneuver == 0 && sub_Man_Count == 0 && super.CT.Weapons[1] != null)
        {
            float f1 = ((GunGeneric)super.CT.Weapons[1][0]).bulletSpeed();
            if(f1 > 0.01F)
                bullTime = 1.0F / f1;
        }
        maxAOA = 15F;
        super.minElevCoeff = 20F;
        switch(submaneuver)
        {
        case 0: // '\0'
            setCheckGround(true);
            rocketsDelay = 0;
            if(sub_Man_Count == 0)
            {
                Vtarg.set(actor.pos.getAbsPoint());
                actor.getSpeed(tmpV3d);
                tmpV3f.x = (float)((Tuple3d) (tmpV3d)).x;
                tmpV3f.y = (float)((Tuple3d) (tmpV3d)).y;
                tmpV3f.z = (float)((Tuple3d) (tmpV3d)).z;
                tmpV3f.z = 0.0D;
                if(tmpV3f.length() < 9.9999997473787516E-005D)
                {
                    tmpV3f.sub(Vtarg, super.Loc);
                    tmpV3f.z = 0.0D;
                }
                tmpV3f.normalize();
                Vtarg.x -= ((Tuple3d) (tmpV3f)).x * 1500D;
                Vtarg.y -= ((Tuple3d) (tmpV3f)).y * 1500D;
                Vtarg.z += 400D;
                constVtarg.set(Vtarg);
                Ve.sub(constVtarg, super.Loc);
                Ve.normalize();
                Vxy.cross(Ve, tmpV3f);
                Ve.sub(tmpV3f);
                Vtarg.z += 100D;
                if(((Tuple3d) (Vxy)).z > 0.0D)
                {
                    Vtarg.x += ((Tuple3d) (Ve)).y * 1000D;
                    Vtarg.y -= ((Tuple3d) (Ve)).x * 1000D;
                } else
                {
                    Vtarg.x -= ((Tuple3d) (Ve)).y * 1000D;
                    Vtarg.y += ((Tuple3d) (Ve)).x * 1000D;
                }
                constVtarg1.set(Vtarg);
            }
            Ve.set(constVtarg1);
            Ve.sub(super.Loc);
            float f2 = (float)Ve.length();
            super.Or.transformInv(Ve);
            if(((Tuple3d) (Ve)).x < 0.0D)
            {
                push(0);
                push(8);
                pop();
                dontSwitch = true;
            } else
            {
                Ve.normalize();
                setSpeedMode(4);
                smConstSpeed = 100F;
                farTurnToDirection();
                sub_Man_Count++;
                if(f2 < 300F)
                {
                    submaneuver++;
                    gattackCounter++;
                    sub_Man_Count = 0;
                }
            }
            break;

        case 1: // '\001'
            Ve.set(constVtarg);
            Ve.sub(super.Loc);
            float f3 = (float)Ve.length();
            super.Or.transformInv(Ve);
            Ve.normalize();
            setSpeedMode(4);
            smConstSpeed = 100F;
            farTurnToDirection();
            sub_Man_Count++;
            if(f3 < 300F)
            {
                submaneuver++;
                gattackCounter++;
                sub_Man_Count = 0;
            }
            break;

        case 2: // '\002'
            if(rocketsDelay > 0)
                rocketsDelay--;
            if(sub_Man_Count > 100)
                setCheckGround(false);
            P.set(actor.pos.getAbsPoint());
            P.z += 4D;
            Engine.land();
            if(Landscape.rayHitHQ(super.Loc, P, P))
            {
                push(0);
                push(38);
                pop();
                gattackCounter--;
                if(gattackCounter < 0)
                    gattackCounter = 0;
            }
            Ve.sub(actor.pos.getAbsPoint(), super.Loc);
            Vtarg.set(Ve);
            float f4 = (float)Ve.length();
            actor.getSpeed(tmpV3d);
            tmpV3f.x = (float)((Tuple3d) (tmpV3d)).x;
            tmpV3f.y = (float)((Tuple3d) (tmpV3d)).y;
            tmpV3f.z = (float)((Tuple3d) (tmpV3d)).z;
            tmpV3f.sub(super.Vwld);
            tmpV3f.scale(f4 * bullTime * 0.3333F * (float)super.Skill);
            Ve.add(tmpV3f);
            float f5 = 0.3F * (f4 - 1000F);
            if(f5 < 0.0F)
                f5 = 0.0F;
            if(f5 > 300F)
                f5 = 300F;
            Ve.z += f5 + World.cur().rnd.nextFloat(-3F, 3F) * (float)(3 - super.Skill);
            super.Or.transformInv(Ve);
            if(f4 < 800F && (shotAtFriend <= 0 || distToFriend > f4) && Math.abs(((Tuple3d) (Ve)).y) < 15D && Math.abs(((Tuple3d) (Ve)).z) < 10D)
            {
                if(f4 < 550F)
                    super.CT.WeaponControl[0] = true;
                if(f4 < 450F)
                    super.CT.WeaponControl[1] = true;
                if(super.CT.Weapons[2] != null && super.CT.Weapons[2][0] != null && super.CT.Weapons[2][super.CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1 && f4 < 500F)
                {
                    super.CT.WeaponControl[2] = true;
                    Voice.speakAttackByRockets((Aircraft)super.actor);
                    rocketsDelay += 30;
                }
            }
            if(sub_Man_Count > 200 && ((Tuple3d) (Ve)).x < 200D || Alt < 40F)
            {
                if(super.Leader == null || !Actor.isAlive(((Interpolate) (super.Leader)).actor))
                    Voice.speakEndGattack((Aircraft)super.actor);
                rocketsDelay = 0;
                push(0);
                push(55);
                push(10);
                pop();
                dontSwitch = true;
            } else
            {
                Ve.normalize();
                attackTurnToDirection(f4, f, 4F + (float)super.Skill * 2.0F);
                setSpeedMode(4);
                smConstSpeed = 100F;
            }
            break;

        default:
            submaneuver = 0;
            sub_Man_Count = 0;
            break;
        }
    }

    //TODO: All ground attack methods have been edited for the DBW AI Mod
    private void groundAttack(Actor actor, float f)
    {
    	//TODO: DBW AI Mod Edits
        if(!(super.actor instanceof TypeSupersonic) && !(super.actor instanceof TypeFastJet) && !(super.actor instanceof TypeDiveBomber))
            bombsOutCounter = 129;
        else
            bombsOutCounter = 0;
        bombsOut = false;
        super.CT.WeaponControl[3] = false;
        //TODO: DBW AI Mod Edits
        if((super.actor instanceof TypeSupersonic) && (super.actor instanceof TypeFastJet))
        {
            setSpeedMode(7);
        } else
        {
            setSpeedMode(4);
            smConstSpeed = 120F;
        }
        float f1 = ((Tuple3d) (super.Vwld)).z <= 0.0D ? 4F : 3F;
        boolean flag = false;
        boolean flag1 = false;
        if(hasBombs())
        {
            flag = true;
            if(super.CT.Weapons[3][0] instanceof ParaTorpedoGun)
                flag1 = true;
        } else
        if(!(super.actor instanceof TypeStormovik) && !(super.actor instanceof TypeFighter) && !(super.actor instanceof TypeDiveBomber))
        {
            set_maneuver(0);
            return;
        }
        Ve.set(actor.pos.getAbsPoint());
        if(flag1)
            if(super.CT.Weapons[3][0] instanceof BombGunTorp45_36AV_A)
                Ve.z = (((Tuple3d) (super.Loc)).z - 100D) + World.Rnd().nextDouble() * 50D;
            else
                Ve.z = (((Tuple3d) (super.Loc)).z - 200D) + World.Rnd().nextDouble() * 50D;
        float f2 = (float)((Tuple3d) (super.Loc)).z - (float)((Tuple3d) (Ve)).z;
        if(f2 < 0.0F)
            f2 = 0.0F;
        float f3 = (float)Math.sqrt(2.0F * f2 * 0.1019F) + 0.0017F * f2;
        actor.getSpeed(tmpV3d);
        if((actor instanceof Aircraft) && tmpV3d.length() > 20D)
        {
            target = ((SndAircraft) ((Aircraft)actor)).FM;
            set_task(6);
            clear_stack();
            set_maneuver(27);
            dontSwitch = true;
        } else
        {
            float f4 = 0.5F;
            if(flag)
                f4 = (f3 + 5F) * 0.33333F;
            Vtarg.x = (float)((Tuple3d) (tmpV3d)).x * f4 * (float)super.Skill;
            Vtarg.y = (float)((Tuple3d) (tmpV3d)).y * f4 * (float)super.Skill;
            Vtarg.z = (float)((Tuple3d) (tmpV3d)).z * f4 * (float)super.Skill;
            Ve.add(Vtarg);
            Ve.sub(super.Loc);
            if(flag)
                addWindCorrection();
            Ve.add(0.0D, 0.0D, -0.5F + World.Rnd().nextFloat(-2F, 0.8F));
            Vf.set(Ve);
            float f5 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y);
            if(flag)
            {
                float f6 = getSpeed() * f3 + 100F;
                if(f5 > f6)
                    Ve.z += 200D;
                else
                    Ve.z = 0.0D;
            }
            Vtarg.set(Ve);
            Vtarg.normalize();
            super.Or.transformInv(Ve);
            if(!hasBombs())
                groundAttackGuns(actor, f);
            else
            if((super.actor instanceof TypeFighter) || (super.actor instanceof TypeStormovik))
            {
            	//TODO: DBW AI Mod Edits
                if(!(super.actor instanceof TypeSupersonic) && !(super.actor instanceof TypeFastJet) && !(super.actor instanceof TypeDiveBomber))
                    bombsOutCounter = 129;
                else
                    bombsOutCounter = 0;
                passCounter = 0;
                groundAttackShallowDive(actor, f);
            } else
            {
                Ve.normalize();
                Vpl.set(super.Vwld);
                Vpl.normalize();
                super.CT.BayDoorControl = 1.0F;
                if(f5 + 4F * f3 < getSpeed() * f3 && ((Tuple3d) (Ve)).x > 0.0D && Vpl.dot(Vtarg) > 0.98000001907348633D)
                {
                    if(!bombsOut)
                    {
                        bombsOut = true;
                        if(super.CT.Weapons[3] != null && super.CT.Weapons[3][0] != null && super.CT.Weapons[3][0].countBullets() != 0 && !(super.CT.Weapons[3][0] instanceof BombGunPara))
                            Voice.speakAttackByBombs((Aircraft)super.actor);
                    }
                    push(0);
                    push(55);
                    push(48);
                    pop();
                    Voice.speakEndGattack((Aircraft)super.actor);
                    super.CT.BayDoorControl = 0.0F;
                }
                if(((Tuple3d) (Ve)).x < 0.0D)
                {
                    push(0);
                    push(55);
                    push(10);
                    pop();
                }
                if(Math.abs(((Tuple3d) (Ve)).y) > 0.10000000149011612D)
                    super.CT.AileronControl = -(float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).z) - 0.016F * super.Or.getKren();
                else
                    super.CT.AileronControl = -(float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) - 0.016F * super.Or.getKren();
                if(Math.abs(((Tuple3d) (Ve)).y) > 0.0010000000474974513D)
                    super.CT.RudderControl = -98F * (float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x);
                else
                    super.CT.RudderControl = 0.0F;
                if((double)super.CT.RudderControl * ((Tuple3d) (super.W)).z > 0.0D)
                    super.W.z = 0.0D;
                else
                    super.W.z *= 1.0399999618530273D;
                float f7 = (float)Math.atan2(((Tuple3d) (Ve)).z, ((Tuple3d) (Ve)).x);
                if(Math.abs(((Tuple3d) (Ve)).y) < 0.0020000000949949026D && Math.abs(((Tuple3d) (Ve)).z) < 0.0020000000949949026D)
                    f7 = 0.0F;
                if(((Tuple3d) (Ve)).x < 0.0D)
                {
                    f7 = 1.0F;
                } else
                {
                    if((double)f7 * ((Tuple3d) (super.W)).y > 0.0D)
                        super.W.y = 0.0D;
                    if(f7 < 0.0F)
                    {
                        if(getOverload() < 0.1F)
                            f7 = 0.0F;
                        if(super.CT.ElevatorControl > 0.0F)
                            super.CT.ElevatorControl = 0.0F;
                    } else
                    if(super.CT.ElevatorControl < 0.0F)
                        super.CT.ElevatorControl = 0.0F;
                }
                if(getOverload() > maxG || super.AOA > f1 || super.CT.ElevatorControl > f7)
                    super.CT.ElevatorControl -= 0.2F * f;
                else
                    super.CT.ElevatorControl += 0.2F * f;
            }
        }
    }

    private void groundAttackKamikaze(Actor actor, float f)
    {
        if(submaneuver == 0 && sub_Man_Count == 0 && super.CT.Weapons[1] != null)
        {
            float f1 = ((GunGeneric)super.CT.Weapons[1][0]).bulletSpeed();
            if(f1 > 0.01F)
                bullTime = 1.0F / f1;
        }
        maxAOA = 15F;
        super.minElevCoeff = 20F;
        switch(submaneuver)
        {
        case 0: // '\0'
            setCheckGround(true);
            rocketsDelay = 0;
            if(sub_Man_Count == 0)
            {
                Vtarg.set(actor.pos.getAbsPoint());
                tmpV3f.set(super.Vwld);
                tmpV3f.x += World.Rnd().nextFloat(-100F, 100F);
                tmpV3f.y += World.Rnd().nextFloat(-100F, 100F);
                tmpV3f.z = 0.0D;
                if(tmpV3f.length() < 9.9999997473787516E-005D)
                {
                    tmpV3f.sub(Vtarg, super.Loc);
                    tmpV3f.z = 0.0D;
                }
                tmpV3f.normalize();
                Vtarg.x -= ((Tuple3d) (tmpV3f)).x * 1500D;
                Vtarg.y -= ((Tuple3d) (tmpV3f)).y * 1500D;
                Vtarg.z += 400D;
                constVtarg.set(Vtarg);
                Ve.sub(constVtarg, super.Loc);
                Ve.normalize();
                Vxy.cross(Ve, tmpV3f);
                Ve.sub(tmpV3f);
                Vtarg.z += 100D;
                if(((Tuple3d) (Vxy)).z > 0.0D)
                {
                    Vtarg.x += ((Tuple3d) (Ve)).y * 1000D;
                    Vtarg.y -= ((Tuple3d) (Ve)).x * 1000D;
                } else
                {
                    Vtarg.x -= ((Tuple3d) (Ve)).y * 1000D;
                    Vtarg.y += ((Tuple3d) (Ve)).x * 1000D;
                }
                constVtarg1.set(Vtarg);
            }
            Ve.set(constVtarg1);
            Ve.sub(super.Loc);
            float f2 = (float)Ve.length();
            super.Or.transformInv(Ve);
            if(((Tuple3d) (Ve)).x < 0.0D)
            {
                push(0);
                push(8);
                pop();
                dontSwitch = true;
            } else
            {
                Ve.normalize();
                setSpeedMode(6);
                farTurnToDirection();
                sub_Man_Count++;
                if(f2 < 300F)
                {
                    submaneuver++;
                    gattackCounter++;
                    sub_Man_Count = 0;
                }
                if(sub_Man_Count > 1000)
                    sub_Man_Count = 0;
            }
            break;

        case 1: // '\001'
            setCheckGround(true);
            Ve.set(constVtarg);
            Ve.sub(super.Loc);
            float f3 = (float)Ve.length();
            super.Or.transformInv(Ve);
            Ve.normalize();
            setSpeedMode(6);
            farTurnToDirection();
            sub_Man_Count++;
            if(f3 < 300F)
            {
                submaneuver++;
                gattackCounter++;
                sub_Man_Count = 0;
            }
            if(sub_Man_Count > 700)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            break;

        case 2: // '\002'
            setCheckGround(false);
            if(rocketsDelay > 0)
                rocketsDelay--;
            if(sub_Man_Count > 100)
                setCheckGround(false);
            Ve.set(actor.pos.getAbsPoint());
            Ve.sub(super.Loc);
            Vtarg.set(Ve);
            float f4 = (float)Ve.length();
            if(super.actor instanceof MXY_7)
            {
                Ve.z += 0.01D * (double)f4;
                Vtarg.z += 0.01D * (double)f4;
            }
            actor.getSpeed(tmpV3d);
            tmpV3f.x = (float)((Tuple3d) (tmpV3d)).x;
            tmpV3f.y = (float)((Tuple3d) (tmpV3d)).y;
            tmpV3f.z = (float)((Tuple3d) (tmpV3d)).z;
            tmpV3f.sub(super.Vwld);
            tmpV3f.scale(f4 * bullTime * 0.3333F * (float)super.Skill);
            Ve.add(tmpV3f);
            float f5 = 0.3F * (f4 - 1000F);
            if(f5 < 0.0F)
                f5 = 0.0F;
            if(f5 > 300F)
                f5 = 300F;
            Ve.z += f5 + World.cur().rnd.nextFloat(-3F, 3F) * (float)(3 - super.Skill);
            super.Or.transformInv(Ve);
            if(f4 < 50F && Math.abs(((Tuple3d) (Ve)).y) < 40D && Math.abs(((Tuple3d) (Ve)).z) < 30D)
            {
                super.CT.WeaponControl[0] = true;
                super.CT.WeaponControl[1] = true;
                super.CT.WeaponControl[2] = true;
                super.CT.WeaponControl[3] = true;
            }
            if(((Tuple3d) (Ve)).x < -50D)
            {
                rocketsDelay = 0;
                push(0);
                push(55);
                push(10);
                pop();
                dontSwitch = true;
            } else
            {
                Ve.normalize();
                attackTurnToDirection(f4, f, 4F + (float)super.Skill * 2.0F);
                setSpeedMode(4);
                smConstSpeed = 130F;
            }
            break;

        default:
            submaneuver = 0;
            sub_Man_Count = 0;
            break;
        }
    }

    private void groundAttackTinyTim(Actor actor, float f)
    {
        maxG = 5F;
        maxAOA = 8F;
        setSpeedMode(4);
        smConstSpeed = 120F;
        super.minElevCoeff = 20F;
        switch(submaneuver)
        {
        case 0: // '\0'
            if(sub_Man_Count == 0)
            {
                Vtarg.set(actor.pos.getAbsPoint());
                actor.getSpeed(tmpV3d);
                if(tmpV3d.length() < 0.5D)
                    tmpV3d.sub(Vtarg, super.Loc);
                tmpV3d.normalize();
                Vtarg.x -= ((Tuple3d) (tmpV3d)).x * 3000D;
                Vtarg.y -= ((Tuple3d) (tmpV3d)).y * 3000D;
                Vtarg.z += 500D;
            }
            Ve.sub(Vtarg, super.Loc);
            double d = Ve.length();
            super.Or.transformInv(Ve);
            sub_Man_Count++;
            if(d < 1000D)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            Ve.normalize();
            farTurnToDirection();
            break;

        case 1: // '\001'
            Vtarg.set(actor.pos.getAbsPoint());
            Vtarg.z += 80D;
            Ve.sub(Vtarg, super.Loc);
            double d1 = Ve.length();
            super.Or.transformInv(Ve);
            sub_Man_Count++;
            if(d1 < 1500D)
            {
                if(Math.abs(((Tuple3d) (Ve)).y) < 40D && Math.abs(((Tuple3d) (Ve)).z) < 30D)
                    super.CT.WeaponControl[2] = true;
                push(0);
                push(10);
                push(48);
                pop();
                dontSwitch = true;
            }
            if(d1 < 500D && ((Tuple3d) (Ve)).x < 0.0D)
            {
                push(0);
                push(10);
                pop();
            }
            Ve.normalize();
            if(((Tuple3d) (Ve)).x < 0.80000001192092896D)
                turnToDirection(f);
            else
                attackTurnToDirection((float)d1, f, 2.0F + (float)super.Skill * 1.5F);
            break;

        default:
            submaneuver = 0;
            sub_Man_Count = 0;
            break;
        }
    }

    private void groundAttackHS293(Actor actor, float f)
    {
        maxG = 5F;
        maxAOA = 8F;
        setSpeedMode(4);
        smConstSpeed = 120F;
        super.minElevCoeff = 20F;
        switch(submaneuver)
        {
        case 0: // '\0'
            if(sub_Man_Count == 0)
            {
                Vtarg.set(actor.pos.getAbsPoint());
                actor.getSpeed(tmpV3d);
                if(tmpV3d.length() < 0.5D)
                    tmpV3d.sub(Vtarg, super.Loc);
                tmpV3d.normalize();
                Vtarg.x -= ((Tuple3d) (tmpV3d)).x * 3000D;
                Vtarg.y -= ((Tuple3d) (tmpV3d)).y * 3000D;
                Vtarg.z += 500D;
            }
            Ve.sub(Vtarg, super.Loc);
            double d = Ve.length();
            super.Or.transformInv(Ve);
            sub_Man_Count++;
            if(d < 10000D)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            Ve.normalize();
            farTurnToDirection();
            break;

        case 1: // '\001'
            Vtarg.set(actor.pos.getAbsPoint());
            Vtarg.z += 2000D;
            Ve.sub(Vtarg, super.Loc);
            double d1 = Ve.angle(super.Vwld);
            Ve.z = 0.0D;
            double d2 = Ve.length();
            super.Or.transformInv(Ve);
            sub_Man_Count++;
            TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier)super.actor;
            if(d2 > 2000D && d2 < 6500D && d1 < 0.90000000000000002D && !typeguidedbombcarrier.typeGuidedBombCgetIsGuiding())
            {
                super.CT.WeaponControl[3] = true;
                push(0);
                push(10);
                pop();
                dontSwitch = true;
            }
            if(d2 < 500D && ((Tuple3d) (Ve)).x < 0.0D)
            {
                push(0);
                push(10);
                pop();
            }
            Ve.normalize();
            if(((Tuple3d) (Ve)).x < 99999.800000011921D)
                turnToDirection(f);
            else
                attackTurnToDirection((float)d2, f, 2.0F + (float)super.Skill * 1.5F);
            break;

        default:
            submaneuver = 0;
            sub_Man_Count = 0;
            break;
        }
    }

    private void groundAttackFritzX(Actor actor, float f)
    {
        maxG = 5F;
        maxAOA = 8F;
        setSpeedMode(4);
        smConstSpeed = 140F;
        super.minElevCoeff = 20F;
        switch(submaneuver)
        {
        case 0: // '\0'
            if(sub_Man_Count == 0)
            {
                Vtarg.set(actor.pos.getAbsPoint());
                actor.getSpeed(tmpV3d);
                if(tmpV3d.length() < 0.5D)
                    tmpV3d.sub(Vtarg, super.Loc);
                tmpV3d.normalize();
                Vtarg.x -= ((Tuple3d) (tmpV3d)).x * 3000D;
                Vtarg.y -= ((Tuple3d) (tmpV3d)).y * 3000D;
                Vtarg.z += 500D;
            }
            Ve.sub(Vtarg, super.Loc);
            double d = Ve.length();
            super.Or.transformInv(Ve);
            sub_Man_Count++;
            if(d < 15000D)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            Ve.normalize();
            farTurnToDirection();
            break;

        case 1: // '\001'
            Vtarg.set(actor.pos.getAbsPoint());
            Vtarg.z += 2000D;
            Ve.sub(Vtarg, super.Loc);
            double d1 = Ve.angle(super.Vwld);
            Ve.z = 0.0D;
            double d2 = Ve.length();
            super.Or.transformInv(Ve);
            sub_Man_Count++;
            TypeGuidedBombCarrier typeguidedbombcarrier = (TypeGuidedBombCarrier)super.actor;
            if(d2 < 4000D && d1 < 0.90000000000000002D && (d2 < 2000D || d1 > 0.40000000000000002D) && !typeguidedbombcarrier.typeGuidedBombCgetIsGuiding())
            {
                super.CT.WeaponControl[3] = true;
                setSpeedMode(5);
                push(0);
                push(10);
                pop();
                dontSwitch = true;
            }
            if(d2 < 500D && ((Tuple3d) (Ve)).x < 0.0D)
            {
                push(0);
                push(10);
                pop();
            }
            Ve.normalize();
            if(((Tuple3d) (Ve)).x < 99999.800000011921D)
                turnToDirection(f);
            else
                attackTurnToDirection((float)d2, f, 2.0F + (float)super.Skill * 1.5F);
            break;

        default:
            submaneuver = 0;
            sub_Man_Count = 0;
            break;
        }
    }

    private void groundAttackShallowDive(Actor actor, float f)
    {
        maxAOA = 8F;
        if(!hasBombs())
        {
            set_maneuver(0);
            wingman(true);
        } else
        {
            if(first)
                RandomVal = 50F - World.cur().rnd.nextFloat(0.0F, 100F);
            //TODO: DBW AI Mod Edits
            if((super.actor instanceof TypeSupersonic) && (super.actor instanceof TypeFastJet))
            {
                setSpeedMode(7);
            } else
            {
                setSpeedMode(4);
                smConstSpeed = 120F;
            }
            Ve.set(actor.pos.getAbsPoint());
            Ve.sub(super.Loc);
            addWindCorrection();
            float f1 = (float)(-((Tuple3d) (Ve)).z);
            if(f1 < 0.0F)
                f1 = 0.0F;
            Ve.z += 250D;
            float f2 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y) + RandomVal * (float)(2.75D - (double)super.Skill);
            if(((Tuple3d) (Ve)).z < (double)(-0.1F * f2))
                Ve.z = -0.1F * f2;
            if((double)Alt + ((Tuple3d) (Ve)).z < 250D)
                Ve.z = 250F - Alt;
            if(Alt < 50F)
            {
                push(10);
                pop();
            }
            Vf.set(Ve);
            super.CT.BayDoorControl = 1.0F;
            float f3 = (float)((Tuple3d) (super.Vwld)).z * 0.1019F;
            f3 += (float)Math.sqrt(f3 * f3 + 2.0F * f1 * 0.1019F);
            float f4 = (float)Math.sqrt(((Tuple3d) (super.Vwld)).x * ((Tuple3d) (super.Vwld)).x + ((Tuple3d) (super.Vwld)).y * ((Tuple3d) (super.Vwld)).y);
            float f5 = f4 * f3 + 10F;
            actor.getSpeed(tmpV3d);
            tmpV3d.scale((double)f3 * 0.45000000000000001D * (double)super.Skill);
            Ve.x += (float)((Tuple3d) (tmpV3d)).x;
            Ve.y += (float)((Tuple3d) (tmpV3d)).y;
            Ve.z += (float)((Tuple3d) (tmpV3d)).z;
            if((super.CT.Weapons[3][0].getClass().getName().endsWith("SnakeEye") ? (double)f5 >= (double)f2 * 1.175D : f5 >= f2) && passCounter == 0)
            {
                bombsOut = true;
                //TODO: DBW AI Mod Edits
                if(!(super.actor instanceof TypeSupersonic) && !(super.actor instanceof TypeFastJet) && !(super.actor instanceof TypeDiveBomber))
                    bombsOutCounter = 129;
                else
                    bombsOutCounter = 0;
                Voice.speakAttackByBombs((Aircraft)super.actor);
                setSpeedMode(6);
                super.CT.BayDoorControl = 0.0F;
                pop();
                sub_Man_Count = 0;
                passCounter++;
            } else
            if(passCounter < 5);
            passCounter = 0;
            push(55);
            push(48);
            super.Or.transformInv(Ve);
            Ve.normalize();
            turnToDirection(f);
        }
    }

    private void groundAttackDiveBomber(Actor actor, float f)
    {
        maxG = 5F;
        maxAOA = 10F;
        setSpeedMode(6);
        maxAOA = 4F;
        super.minElevCoeff = 20F;
        if(super.CT.Weapons[3] == null || super.CT.getWeaponCount(3) == 0)
        {
            if(super.AP.way.curr().Action == 3)
                super.AP.way.next();
            set_maneuver(0);
            wingman(true);
        } else
        {
            if(Alt < 350F)
            {
                bombsOut = true;
                Voice.speakAttackByBombs((Aircraft)super.actor);
                setSpeedMode(6);
                super.CT.BayDoorControl = 0.0F;
                super.CT.AirBrakeControl = 0.0F;
                super.AP.way.next();
                push(0);
                push(8);
                push(2);
                pop();
                sub_Man_Count = 0;
            }
            Ve.set(actor.pos.getAbsPoint());
            Ve.sub(super.Loc);
            float f1 = (float)(-((Tuple3d) (Ve)).z);
            float f2 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y + ((Tuple3d) (Ve)).z * ((Tuple3d) (Ve)).z);
            float f3 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y);
            if(f3 > 1000F || submaneuver == 3 && sub_Man_Count > 100)
            {
                Vtarg.set(actor.pos.getAbsPoint());
                actor.getSpeed(tmpV3d);
                float f4 = 0.0F;
                switch(submaneuver)
                {
                case 0: // '\0'
                    f4 = f3 / 40F + 4F + Alt / 48F;
                    // fall through

                case 1: // '\001'
                    f4 = (f3 - man1Dist) / (float)super.Vwld.length() + 4F + Alt / 48F;
                    // fall through

                case 2: // '\002'
                    f4 = Alt / 60F;
                    // fall through

                case 3: // '\003'
                    f4 = Alt / 120F;
                    // fall through

                default:
                    f4 *= 0.33333F;
                    break;
                }
                Vtarg.x += (float)((Tuple3d) (tmpV3d)).x * f4 * (float)super.Skill;
                Vtarg.y += (float)((Tuple3d) (tmpV3d)).y * f4 * (float)super.Skill;
                Vtarg.z += (float)((Tuple3d) (tmpV3d)).z * f4 * (float)super.Skill;
            }
            Ve.set(Vtarg);
            Ve.sub(super.Loc);
            float f5 = (float)(-((Tuple3d) (Ve)).z);
            if(f5 < 0.0F)
                f5 = 0.0F;
            Ve.add(Vxy);
            f1 = (float)(-((Tuple3d) (Ve)).z);
            f2 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y + ((Tuple3d) (Ve)).z * ((Tuple3d) (Ve)).z);
            f3 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y);
            if(submaneuver < 2)
                Ve.z = 0.0D;
            Vf.set(Ve);
            Ve.normalize();
            Vpl.set(super.Vwld);
            Vpl.normalize();
            switch(submaneuver)
            {
            default:
                break;

            case 0: // '\0'
                push();
                pop();
                if(f1 < 1200F)
                    man1Dist = 400F;
                else
                if(f1 > 4500F)
                    man1Dist = 50F;
                else
                    man1Dist = 50F + (350F * (4500F - f1)) / 3300F;
                float f6 = 0.01F * f1;
                if(f6 < 10F)
                    f6 = 10F;
                Vxy.set(World.Rnd().nextFloat(-10F, 10F), World.Rnd().nextFloat(-10F, 10F), World.Rnd().nextFloat(5F, f6));
                Vxy.scale(4F - (float)super.Skill);
                float f9 = 2E-005F * f1 * f1;
                if(f9 < 60F)
                    f9 = 60F;
                if(f9 > 350F)
                    f9 = 350F;
                Vxy.z += f9;
                submaneuver++;
                break;

            case 1: // '\001'
                setSpeedMode(4);
                smConstSpeed = 110F;
                if(f3 >= man1Dist)
                    break;
                super.CT.AirBrakeControl = 1.0F;
                if(super.actor instanceof TypeFighter)
                    super.CT.FlapsControl = 1.0F;
                push();
                push(6);
                safe_pop();
                submaneuver++;
                break;

            case 2: // '\002'
                setSpeedMode(4);
                smConstSpeed = 110F;
                sub_Man_Count++;
                super.CT.AirBrakeControl = 1.0F;
                if(super.actor instanceof TypeFighter)
                    super.CT.FlapsControl = 1.0F;
                float f7 = super.Or.getKren();
                if(super.Or.getTangage() > -90F)
                {
                    f7 -= 180F;
                    if(f7 < -180F)
                        f7 += 360F;
                }
                super.CT.AileronControl = (float)((double)(-0.04F * f7) - 0.5D * ((Tuple3d) (getW())).x);
                if(getOverload() < 4F)
                    super.CT.ElevatorControl += 0.3F * f;
                else
                    super.CT.ElevatorControl -= 0.3F * f;
                if(sub_Man_Count > 30 && super.Or.getTangage() < -90F || sub_Man_Count > 150)
                {
                    sub_Man_Count = 0;
                    submaneuver++;
                }
                break;

            case 3: // '\003'
                super.CT.AirBrakeControl = 1.0F;
                if(super.actor instanceof TypeFighter)
                    super.CT.FlapsControl = 1.0F;
                super.CT.BayDoorControl = 1.0F;
                setSpeedMode(4);
                smConstSpeed = 110F;
                sub_Man_Count++;
                if(sub_Man_Count > 80)
                {
                    float f8 = (float)((Tuple3d) (super.Vwld)).z * 0.1019F;
                    f8 = f8 + (float)Math.sqrt(f8 * f8 + 2.0F * f5 * 0.1019F) + 0.00035F * f5;
                    float f10 = (float)Math.sqrt(((Tuple3d) (super.Vwld)).x * ((Tuple3d) (super.Vwld)).x + ((Tuple3d) (super.Vwld)).y * ((Tuple3d) (super.Vwld)).y);
                    float f11 = f10 * f8;
                    float f12 = 0.2F * (f3 - f11);
                    Vxy.z += f12;
                    if(((Tuple3d) (Vxy)).z > (double)(0.7F * f5))
                        Vxy.z = 0.7F * f5;
                }
                if(sub_Man_Count > 100 && Alt < 1000F && Vpl.dot(Ve) > 0.99000000953674316D || Alt < 600F)
                {
                    bombsOut = true;
                    Voice.speakAttackByBombs((Aircraft)super.actor);
                    super.CT.BayDoorControl = 0.0F;
                    super.CT.AirBrakeControl = 0.0F;
                    super.AP.way.next();
                    push(0);
                    push(8);
                    push(2);
                    pop();
                }
                break;
            }
            super.Or.transformInv(Ve);
            Ve.normalize();
            if(submaneuver == 3)
                attackTurnToDirection(1000F, f, 30F);
            else
            if(submaneuver != 2)
                turnToDirection(f);
        }
    }

    private void groundAttackTorpedo(Actor actor, float f)
    {
        float f4 = 50F;
        maxG = 5F;
        maxAOA = 8F;
        float f5 = 0.0F;
        setSpeedMode(4);
        Class class1 = null;
        if(super.CT.Weapons[3][0] instanceof TorpedoGun)
        {
            TorpedoGun torpedogun = (TorpedoGun)super.CT.Weapons[3][0];
            class1 = (Class)Property.value(torpedogun.getClass(), "bulletClass", null);
        }
        smConstSpeed = 100F;
        if(class1 != null)
        {
            smConstSpeed = Property.floatValue(class1, "dropSpeed", 100F) / 3.7F;
            f4 = Property.floatValue(class1, "dropAltitude", 50F) + 10F;
        }
        if(super.actor instanceof Swordfish)
        {
            setSpeedMode(9);
            f4 *= 0.75F;
        }
        super.minElevCoeff = 20F;
        Ve.set(actor.pos.getAbsPoint());
        Ve.sub(super.Loc);
        if(first)
        {
            Vtarg.set(actor.pos.getAbsPoint());
            submaneuver = 0;
        }
        if(submaneuver == 1 && sub_Man_Count == 0)
        {
            tmpV3f.set(actor.pos.getAbsPoint());
            tmpV3f.sub(Vtarg);
            actor.getSpeed(tmpV3d);
            if(tmpV3f.length() > 9.9999997473787516E-005D)
                tmpV3f.normalize();
            Vxy.set(((Tuple3d) (tmpV3f)).y * 3000D, -((Tuple3d) (tmpV3f)).x * 3000D, 0.0D);
            direc = Vxy.dot(Ve) > 0.0D;
            if(direc)
                Vxy.scale(-1D);
            Vtarg.add(Vxy);
            Vtarg.x += ((Tuple3d) (tmpV3d)).x * 80D;
            Vtarg.y += ((Tuple3d) (tmpV3d)).y * 80D;
            Vtarg.z = 80D;
            TargDevV.set((double)World.cur().rnd.nextFloat(-16F, 16F) * (3.5D - (double)super.Skill), (double)World.cur().rnd.nextFloat(-16F, 16F) * (3.5D - (double)super.Skill), 0.0D);
        }
        if(submaneuver == 2)
        {
            Vtarg.set(actor.pos.getAbsPoint());
            actor.getSpeed(tmpV3d);
            float f6 = 20F;
            if(class1 != null)
                f6 = Property.floatValue(class1, "velocity", 1.0F);
            float f8 = actor.collisionR();
            if(f8 > 80F)
                f5 = 50F;
            if(f8 > 130F)
                f5 = 100F;
            if(f8 < 25F)
                f5 = -50F;
            float f11 = 950F;
            if(super.actor instanceof JU_88NEW)
                f11 += 90F;
            double d1 = Math.sqrt(0.20399999999999999D * ((Tuple3d) (super.Loc)).z);
            double d2 = 1.0D * d1 * (double)getSpeed();
            double d3 = ((double)(f11 + f5) - d2) / (double)f6;
            Vtarg.x += (float)(((Tuple3d) (tmpV3d)).x * d3);
            Vtarg.y += (float)(((Tuple3d) (tmpV3d)).y * d3);
            Vtarg.z = f4;
            if(((Tuple3d) (super.Loc)).z < 30D)
                Vtarg.z += 3D * (30D - ((Tuple3d) (super.Loc)).z);
            Vtarg.add(TargDevV);
        }
        Ve.set(Vtarg);
        Ve.sub(super.Loc);
        float f2 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y + ((Tuple3d) (Ve)).z * ((Tuple3d) (Ve)).z);
        Vf.set(Ve);
        Vpl.set(super.Vwld);
        Vpl.normalize();
        if(Alt < f4 - 5F)
        {
            if(((Tuple3d) (super.Vwld)).z < 0.0D)
                super.Vwld.z += (f4 - Alt) * 0.25F;
            if(Alt < 8F)
                set_maneuver(2);
            if(Alt < 20F && f2 < 75F)
                set_maneuver(2);
        } else
        if(Alt > f4 + 5F && submaneuver == 1 && ((Tuple3d) (super.Vwld)).z > 0.0D)
            super.Vwld.z--;
        switch(submaneuver)
        {
        default:
            break;

        case 0: // '\0'
            sub_Man_Count++;
            if(sub_Man_Count > 60)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            addWindCorrection();
            break;

        case 1: // '\001'
            setSpeedMode(4);
            if(super.actor instanceof Swordfish)
                setSpeedMode(9);
            sub_Man_Count++;
            if(f2 < 1200F || f2 < 2000F)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            addWindCorrection();
            break;

        case 2: // '\002'
            setSpeedMode(4);
            if(f2 < 800F + f5)
            {
                if((super.actor instanceof TypeHasToKG) && ((TypeHasToKG)super.actor).isSalvo())
                {
                    int i = 0;
                    float f9 = actor.collisionR() * 1.8F;
                    i = (int)Math.toDegrees(Math.atan(f9 / 800F));
                    i += World.Rnd().nextInt(-2, 2);
                    if(i < 3)
                        i = 3;
                    super.AS.setSpreadAngle(i);
                }
                super.CT.WeaponControl[3] = true;
                setSpeedMode(6);
                super.AP.way.next();
                submaneuver++;
                sub_Man_Count = 0;
                break;
            }
            if(!ZutiSupportMethods.isStaticActor(actor))
                break;
            float f7 = Property.floatValue(class1, "velocity", 20F);
            float f10 = Property.floatValue(class1, "traveltime", 100F);
            float f12 = f7 * f10 - 150F;
            if(f2 < f12 && World.land().isWater(((Tuple3d) (super.actor.pos.getAbsPoint())).x, ((Tuple3d) (super.actor.pos.getAbsPoint())).y))
            {
                super.CT.WeaponControl[3] = true;
                setSpeedMode(6);
                super.AP.way.next();
                submaneuver++;
                sub_Man_Count = 0;
            }
            break;

        case 3: // '\003'
            setSpeedMode(9);
            sub_Man_Count++;
            if(sub_Man_Count > 150)
            {
                task = 3;
                push(0);
                push(8);
                pop();
                submaneuver = 0;
                sub_Man_Count = 0;
            }
            break;
        }
        super.Or.transformInv(Ve);
        if(submaneuver == 3)
        {
            if(sub_Man_Count < 20)
                Ve.set(1.0D, 0.090000003576278687D, 0.029999999329447746D);
            else
                Ve.set(1.0D, 0.090000003576278687D, 0.0099999997764825821D);
            if(!direc)
                Ve.y *= -1D;
        }
        Ve.normalize();
        turnToDirection(f);
    }

    private void groundAttackTorpedoToKG(Actor actor, float f)
    {
        float f2 = 50F;
        maxG = 5F;
        maxAOA = 8F;
        setSpeedMode(4);
        Class class1 = null;
        if(super.CT.Weapons[3][0] instanceof TorpedoGun)
        {
            TorpedoGun torpedogun = (TorpedoGun)super.CT.Weapons[3][0];
            class1 = (Class)Property.value(torpedogun.getClass(), "bulletClass", null);
        }
        smConstSpeed = 100F;
        if(class1 != null)
        {
            smConstSpeed = Property.floatValue(class1, "dropSpeed", 100F) / 3.7F;
            f2 = Property.floatValue(class1, "dropAltitude", 50F) + 10F;
        }
        super.minElevCoeff = 20F;
        Ve.set(actor.pos.getAbsPoint());
        Ve.sub(super.Loc);
        if(first)
        {
            Vtarg.set(actor.pos.getAbsPoint());
            submaneuver = 0;
        }
        if(submaneuver == 1 && sub_Man_Count == 0)
        {
            tmpV3f.set(actor.pos.getAbsPoint());
            tmpV3f.sub(Vtarg);
            if(tmpV3f.length() > 9.9999997473787516E-005D)
                tmpV3f.normalize();
            Vxy.set(((Tuple3d) (tmpV3f)).y * 3000D, -((Tuple3d) (tmpV3f)).x * 3000D, 0.0D);
            direc = Vxy.dot(Ve) > 0.0D;
            if(direc)
                Vxy.scale(-1D);
            Vtarg.add(Vxy);
            Vtarg.z = 80D;
        }
        if(submaneuver == 2)
        {
            Vtarg.set(actor.pos.getAbsPoint());
            Vtarg.z = f2;
            if(((Tuple3d) (super.Loc)).z < 30D)
                Vtarg.z += 3D * (30D - ((Tuple3d) (super.Loc)).z);
        }
        Ve.set(Vtarg);
        Ve.sub(super.Loc);
        float f1 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y + ((Tuple3d) (Ve)).z * ((Tuple3d) (Ve)).z);
        Vf.set(Ve);
        Vpl.set(super.Vwld);
        Vpl.normalize();
        if(Alt < f2 - 5F)
        {
            if(((Tuple3d) (super.Vwld)).z < 0.0D)
                super.Vwld.z += (f2 - Alt) * 0.25F;
            if(Alt < 8F)
                set_maneuver(2);
            if(Alt < 20F && f1 < 75F)
                set_maneuver(2);
        } else
        if(Alt > f2 + 5F && submaneuver == 1 && ((Tuple3d) (super.Vwld)).z > 0.0D)
            super.Vwld.z--;
        switch(submaneuver)
        {
        default:
            break;

        case 0: // '\0'
            sub_Man_Count++;
            if(sub_Man_Count > 60)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            addWindCorrection();
            break;

        case 1: // '\001'
            setSpeedMode(4);
            sub_Man_Count++;
            if(f1 < 4000F)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            addWindCorrection();
            break;

        case 2: // '\002'
            setSpeedMode(4);
            double d = Ve.angle(super.Vwld);
            float f3 = 180F - (super.Or.getYaw() - actor.pos.getAbsOrient().getYaw());
            if(f3 < -180F)
                f3 += 360F;
            else
            if(f3 > 180F)
                f3 -= 360F;
            float f4 = Property.floatValue(class1, "velocity", 20F);
            float f5 = Property.floatValue(class1, "traveltime", 100F);
            float f6 = f4 * f5 - 10F;
            if(f6 > 2700F)
                f6 = 2700F;
            double d1 = (Math.abs(f3) - 90F) * 8F;
            if(d1 < 0.0D)
                d1 = 0.0D;
            if(super.Skill == 2)
                d1 += 100D;
            if((double)f1 < (double)f6 - d1 && f1 > 1800F && d < 0.20000000000000001D)
            {
                actor.getSpeed(tmpV3d);
                float f7 = 1.0F;
                if(super.Skill == 2)
                    f7 = World.Rnd().nextFloat(0.8F, 1.2F);
                else
                if(super.Skill == 3)
                    f7 = World.Rnd().nextFloat(0.9F, 1.1F);
                f7 = (float)((double)f7 + 0.10000000000000001D);
                ToKGUtils.setTorpedoGyroAngle(this, f3, (float)(1.95D * tmpV3d.length()) * f7);
                if(((TypeHasToKG)super.actor).isSalvo())
                {
                    int i = 0;
                    float f8 = actor.collisionR() * 1.8F;
                    i = (int)Math.toDegrees(Math.atan((double)f8 / ((double)f6 - d1)));
                    i += World.Rnd().nextInt(-2, 2);
                    if(i < 1)
                        i = 1;
                    super.AS.setSpreadAngle(i);
                }
                super.CT.WeaponControl[3] = true;
                setSpeedMode(6);
                super.AP.way.next();
                push(15);
                submaneuver++;
                sub_Man_Count = 0;
                break;
            }
            if(f1 < 1500F)
            {
                ToKGUtils.setTorpedoGyroAngle(this, 0.0F, 0.0F);
                set_maneuver(51);
            }
            break;

        case 3: // '\003'
            setSpeedMode(9);
            push(15);
            pop();
            task = 61;
            sub_Man_Count++;
            boolean flag = false;
            for(int j = 0; j < super.CT.Weapons[3].length; j++)
                if(super.CT.Weapons[3][j] != null && !(super.CT.Weapons[3][j] instanceof BombGunNull) && super.CT.Weapons[3][j].countBullets() != 0)
                    flag = true;

            if(sub_Man_Count > 800 || !flag)
            {
                task = 3;
                push(21);
                push(8);
                pop();
                submaneuver = 0;
                sub_Man_Count = 0;
            }
            break;
        }
        super.Or.transformInv(Ve);
        if(submaneuver == 3)
        {
            if(sub_Man_Count < 20)
                Ve.set(1.0D, 0.090000003576278687D, 0.029999999329447746D);
            else
                Ve.set(1.0D, 0.090000003576278687D, 0.0099999997764825821D);
            if(!direc)
                Ve.y *= -1D;
        }
        Ve.normalize();
        turnToDirection(f);
    }

    private void groundAttackCassette(Actor actor, float f)
    {
        maxG = 5F;
        maxAOA = 8F;
        setSpeedMode(4);
        smConstSpeed = 120F;
        super.minElevCoeff = 20F;
        Ve.set(actor.pos.getAbsPoint());
        Ve.sub(super.Loc);
        float f1 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y);
        if(submaneuver == 3 && sub_Man_Count > 0 && sub_Man_Count < 45 && f1 > 200F)
        {
            tmpV3f.set(Vxy);
            float f2 = (float)tmpV3f.dot(Ve);
            tmpV3f.scale(-f2);
            tmpV3f.add(Ve);
            float f4 = (float)tmpV3f.length();
            float f6;
            if(f4 > 150F)
                f6 = 7.5F / f4;
            else
                f6 = 0.05F;
            tmpV3f.scale(f6);
            tmpV3f.z = 0.0D;
            super.Vwld.add(tmpV3f);
        }
        if(f1 <= 200F)
            sub_Man_Count = 50;
        if(first)
        {
            Vtarg.set(actor.pos.getAbsPoint());
            submaneuver = 0;
        }
        if(submaneuver == 1 && sub_Man_Count == 0)
        {
            tmpV3f.set(actor.pos.getAbsPoint());
            actor.getSpeed(tmpV3d);
            if(tmpV3d.length() < 0.5D)
                tmpV3d.set(Ve);
            tmpV3d.normalize();
            Vxy.set((float)((Tuple3d) (tmpV3d)).x, (float)((Tuple3d) (tmpV3d)).y, (float)((Tuple3d) (tmpV3d)).z);
            Vtarg.x -= ((Tuple3d) (tmpV3d)).x * 3000D;
            Vtarg.y -= ((Tuple3d) (tmpV3d)).y * 3000D;
            Vtarg.z += 250D;
        }
        if(submaneuver == 2 && sub_Man_Count == 0)
        {
            Vtarg.set(actor.pos.getAbsPoint());
            Vtarg.x -= ((Tuple3d) (Vxy)).x * 1000D;
            Vtarg.y -= ((Tuple3d) (Vxy)).y * 1000D;
            Vtarg.z += 50D;
        }
        if(submaneuver == 3 && sub_Man_Count == 0)
        {
            checkGround = false;
            Vtarg.set(actor.pos.getAbsPoint());
            Vtarg.x += ((Tuple3d) (Vxy)).x * 1000D;
            Vtarg.y += ((Tuple3d) (Vxy)).y * 1000D;
            Vtarg.z += 50D;
        }
        Ve.set(Vtarg);
        Ve.sub(super.Loc);
        addWindCorrection();
        float f3 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y + ((Tuple3d) (Ve)).z * ((Tuple3d) (Ve)).z);
        Vf.set(Ve);
        float f5 = (float)(-((Tuple3d) (Ve)).z);
        super.Or.transformInv(Ve);
        Ve.normalize();
        Vpl.set(super.Vwld);
        Vpl.normalize();
        if(Alt < 10F)
        {
            push(0);
            push(2);
            pop();
        }
        switch(submaneuver)
        {
        case 0: // '\0'
            setSpeedMode(9);
            sub_Man_Count++;
            if(sub_Man_Count > 60)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            break;

        case 1: // '\001'
            setSpeedMode(9);
            sub_Man_Count++;
            if(f3 < 1000F)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            break;

        case 2: // '\002'
            setSpeedMode(6);
            sub_Man_Count++;
            if(f3 < 155F)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            if(sub_Man_Count > 320)
            {
                push(0);
                push(10);
                pop();
            }
            break;

        case 3: // '\003'
            setSpeedMode(6);
            sub_Man_Count++;
            super.Vwld.z *= 0.80000001192092896D;
            super.Or.transformInv(super.Vwld);
            super.Vwld.y *= 0.89999997615814209D;
            super.Or.transform(super.Vwld);
            float f7 = sub_Man_Count;
            if(f7 < 100F)
                f7 = 100F;
            if(Alt > 45F)
                super.Vwld.z -= 0.002F * (Alt - 45F) * f7;
            else
                super.Vwld.z -= 0.005F * (Alt - 45F) * f7;
            if(Alt < 0.0F)
                Alt = 0.0F;
            if(f3 < 1080F + getSpeed() * (float)Math.sqrt((2.0F * Alt) / 9.81F))
                bombsOut = true;
            if(((Tuple3d) (Ve)).x < 0.0D || f3 < 350F || sub_Man_Count > 160)
            {
                push(0);
                push(10);
                push(10);
                pop();
            }
            break;
        }
        if(submaneuver == 0)
            Ve.set(1.0D, 0.0D, 0.0D);
        turnToDirection(f);
    }

    public void modFighterVsFighter(float f)
    {
        Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
        float f1 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y);
        super.Or.transformInv(Ve);
        float f2 = (float)Ve.length();
        float f4 = 1.0F / f2;
        Vtarg.set(Ve);
        Vtarg.scale(f4);
        float f5 = (super.Energy - ((FlightModelMain) (target)).Energy) * 0.1019F;
        tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
        if(sub_Man_Count == 0)
        {
            float f6 = 0.0F;
            if(super.CT.Weapons[1] != null && super.CT.Weapons[1][0].countBullets() > 0)
                f6 = ((GunGeneric)super.CT.Weapons[1][0]).bulletSpeed();
            else
            if(super.CT.Weapons[0] != null)
                f6 = ((GunGeneric)super.CT.Weapons[0][0]).bulletSpeed();
            if(f6 > 0.01F)
                bullTime = 1.0F / f6;
            submanDelay = 0;
        }
        if(f1 < 1500F)
        {
            float f7 = 0.0F;
            float f10 = 0.0F;
            if(((Tuple3d) (Vtarg)).x > -0.20000000298023224D)
            {
                f7 = 3F * ((float)((Tuple3d) (Vtarg)).x + 0.2F);
                Vxy.set(tmpV3f);
                Vxy.scale(1.0D);
                super.Or.transformInv(Vxy);
                Vxy.add(Ve);
                Vxy.normalize();
                f10 = 10F * (float)(((Tuple3d) (Vxy)).x - ((Tuple3d) (Vtarg)).x);
                if(f10 < -1F)
                    f10 = -1F;
                if(f10 > 1.0F)
                    f10 = 1.0F;
            } else
            {
                f7 = 3F * ((float)((Tuple3d) (Vtarg)).x + 0.2F);
            }
            if(submaneuver == 4 && ((Tuple3d) (Vtarg)).x < 0.60000002384185791D && (double)f2 < 300D)
            {
                submaneuver = 1;
                submanDelay = 30;
            }
            if(submaneuver != 4 && (double)f5 > 300D && ((Tuple3d) (Vtarg)).x > 0.75D)
            {
                submaneuver = 4;
                submanDelay = 240;
            }
            float f12 = 0.0015F * f5 + 0.0006F * f1 + f7 + 0.5F * f10;
            if(f12 > 0.9F && submanDelay == 0)
            {
                if(((Tuple3d) (Vtarg)).x > 0.5D || (double)f1 * 2D < (double)f2)
                {
                    submaneuver = 4;
                    submanDelay = 240;
                } else
                {
                    submaneuver = 3;
                    submanDelay = 120;
                }
            } else
            if(f5 > 800F && submaneuver == 0 || f5 > 1000F)
            {
                Ve.set(0.0D, 0.0D, 800D);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 30;
                }
            } else
            if(f2 > 450F && submaneuver == 2 || f2 > 600F)
            {
                if(submanDelay == 0)
                {
                    submaneuver = 2;
                    submanDelay = 60;
                }
            } else
            if(submanDelay == 0)
            {
                submaneuver = 1;
                submanDelay = 30;
            }
        } else
        if(f1 < 3000F)
        {
            if(((Tuple3d) (Vtarg)).x < 0.5D)
            {
                if(submanDelay == 0)
                {
                    submaneuver = 3;
                    submanDelay = 120;
                }
            } else
            if(f5 > 600F && submaneuver == 0 || f5 > 800F)
            {
                Ve.set(0.0D, 0.0D, 800D);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 120;
                }
            } else
            if(f5 > -200F && submaneuver >= 4 || f5 > -100F)
            {
                if(submanDelay == 0)
                {
                    submaneuver = 4;
                    submanDelay = 120;
                }
            } else
            {
                Ve.set(0.0D, 0.0D, ((Tuple3d) (super.Loc)).z);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 120;
                }
            }
        } else
        {
            Ve.set(0.0D, 0.0D, 1000D);
            if(submanDelay == 0)
            {
                submaneuver = 0;
                submanDelay = 180;
            }
        }
        switch(submaneuver)
        {
        case 0: // '\0'
            ((FlightModelMain) (target)).Or.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            tmpV3f.set(Ve);
            tmpV3f.z = 0.0D;
            float f3 = (float)tmpV3f.length();
            tmpV3f.normalize();
            Vtarg.set(((FlightModelMain) (target)).Vwld);
            Vtarg.z = 0.0D;
            float f8 = (float)tmpV3f.dot(Vtarg);
            float f11 = getSpeed() - f8;
            if(f11 < 10F)
                f11 = 10F;
            float f13 = f3 / f11;
            if(f13 < 0.0F)
                f13 = 0.0F;
            tmpV3f.scale(Vtarg, f13);
            Ve.add(tmpV3f);
            super.Or.transformInv(Ve);
            Ve.normalize();
            setSpeedMode(9);
            farTurnToDirection();
            sub_Man_Count++;
            break;

        case 1: // '\001'
            setSpeedMode(9);
            super.CT.AileronControl = -0.04F * super.Or.getKren();
            super.CT.ElevatorControl = -0.04F * (super.Or.getTangage() + 5F);
            super.CT.RudderControl = 0.0F;
            break;

        case 2: // '\002'
            setSpeedMode(9);
            tmpOr.setYPR(super.Or.getYaw(), 0.0F, 0.0F);
            float f9 = 6F;
            if(super.Or.getKren() > 0.0F)
                Ve.set(100D, -f9, 10D);
            else
                Ve.set(100D, f9, 10D);
            tmpOr.transform(Ve);
            super.Or.transformInv(Ve);
            Ve.normalize();
            farTurnToDirection();
            break;

        case 3: // '\003'
            super.minElevCoeff = 20F;
            setSpeedMode(9);
            tmpOr.setYPR(super.Or.getYaw(), 0.0F, 0.0F);
            Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
            Ve.z = 0.0D;
            Ve.normalize();
            Ve.z = 0.40000000000000002D;
            super.Or.transformInv(Ve);
            Ve.normalize();
            attackTurnToDirection(1000F, f, 15F);
            break;

        case 4: // '\004'
            super.minElevCoeff = 20F;
            boomAttack(f);
            setSpeedMode(9);
            break;

        default:
            super.minElevCoeff = 20F;
            fighterVsFighter(f);
            break;
        }
        if(submanDelay > 0)
            submanDelay--;
    }

    public void goodFighterVsFighter(float f)
    {
        Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
        float f1 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y);
        super.Or.transformInv(Ve);
        float f2 = (float)Ve.length();
        float f4 = 1.0F / f2;
        Vtarg.set(Ve);
        Vtarg.scale(f4);
        float f5 = (super.Energy - ((FlightModelMain) (target)).Energy) * 0.1019F;
        tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
        if(sub_Man_Count == 0)
        {
            float f6 = 0.0F;
            if(super.CT.Weapons[1] != null && super.CT.Weapons[1][0].countBullets() > 0)
                f6 = ((GunGeneric)super.CT.Weapons[1][0]).bulletSpeed();
            else
            if(super.CT.Weapons[0] != null)
                f6 = ((GunGeneric)super.CT.Weapons[0][0]).bulletSpeed();
            if(f6 > 0.01F)
                bullTime = 1.0F / f6;
            submanDelay = 0;
        }
        if(f1 < 1500F)
        {
            float f7 = 0.0F;
            float f10 = 0.0F;
            if(((Tuple3d) (Vtarg)).x > -0.20000000298023224D)
            {
                f7 = 3F * ((float)((Tuple3d) (Vtarg)).x + 0.2F);
                Vxy.set(tmpV3f);
                Vxy.scale(1.0D);
                super.Or.transformInv(Vxy);
                Vxy.add(Ve);
                Vxy.normalize();
                f10 = 10F * (float)(((Tuple3d) (Vxy)).x - ((Tuple3d) (Vtarg)).x);
                if(f10 < -1F)
                    f10 = -1F;
                if(f10 > 1.0F)
                    f10 = 1.0F;
            } else
            {
                f7 = 3F * ((float)((Tuple3d) (Vtarg)).x + 0.2F);
            }
            if(submaneuver == 4 && ((Tuple3d) (Vtarg)).x < 0.60000002384185791D && (double)f2 < 300D)
            {
                submaneuver = 1;
                submanDelay = 30;
            }
            if(submaneuver != 4 && (double)f5 > 300D && ((Tuple3d) (Vtarg)).x > 0.75D)
            {
                submaneuver = 4;
                submanDelay = 240;
            }
            float f12 = 0.0015F * f5 + 0.0006F * f1 + f7 + 0.5F * f10;
            if(f12 > 0.9F && submanDelay == 0)
            {
                if(((Tuple3d) (Vtarg)).x > 0.5D || (double)f1 * 2D < (double)f2)
                {
                    submaneuver = 4;
                    submanDelay = 240;
                } else
                {
                    submaneuver = 3;
                    submanDelay = 120;
                }
            } else
            if(f5 > 800F && submaneuver == 0 || f5 > 1000F)
            {
                Ve.set(0.0D, 0.0D, 800D);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 30;
                }
            } else
            if(f2 > 450F && submaneuver == 2 || f2 > 600F)
            {
                if(submanDelay == 0)
                {
                    submaneuver = 2;
                    submanDelay = 60;
                }
            } else
            if(submanDelay == 0)
            {
                submaneuver = 1;
                submanDelay = 30;
            }
        } else
        if(f1 < 3000F)
        {
            if(((Tuple3d) (Vtarg)).x < 0.5D)
            {
                if(submanDelay == 0)
                {
                    submaneuver = 3;
                    submanDelay = 120;
                }
            } else
            if(f5 > 600F && submaneuver == 0 || f5 > 800F)
            {
                Ve.set(0.0D, 0.0D, 800D);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 120;
                }
            } else
            if(f5 > -200F && submaneuver >= 4 || f5 > -100F)
            {
                if(submanDelay == 0)
                {
                    submaneuver = 4;
                    submanDelay = 120;
                }
            } else
            {
                Ve.set(0.0D, 0.0D, ((Tuple3d) (super.Loc)).z);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 120;
                }
            }
        } else
        {
            Ve.set(0.0D, 0.0D, 1000D);
            if(submanDelay == 0)
            {
                submaneuver = 0;
                submanDelay = 180;
            }
        }
        switch(submaneuver)
        {
        case 0: // '\0'
            ((FlightModelMain) (target)).Or.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            tmpV3f.set(Ve);
            tmpV3f.z = 0.0D;
            float f3 = (float)tmpV3f.length();
            tmpV3f.normalize();
            Vtarg.set(((FlightModelMain) (target)).Vwld);
            Vtarg.z = 0.0D;
            float f8 = (float)tmpV3f.dot(Vtarg);
            float f11 = getSpeed() - f8;
            if(f11 < 10F)
                f11 = 10F;
            float f13 = f3 / f11;
            if(f13 < 0.0F)
                f13 = 0.0F;
            tmpV3f.scale(Vtarg, f13);
            Ve.add(tmpV3f);
            super.Or.transformInv(Ve);
            Ve.normalize();
            setSpeedMode(9);
            farTurnToDirection();
            sub_Man_Count++;
            break;

        case 1: // '\001'
            setSpeedMode(9);
            super.CT.AileronControl = -0.04F * super.Or.getKren();
            super.CT.ElevatorControl = -0.04F * (super.Or.getTangage() + 5F);
            super.CT.RudderControl = 0.0F;
            break;

        case 2: // '\002'
            setSpeedMode(9);
            tmpOr.setYPR(super.Or.getYaw(), 0.0F, 0.0F);
            float f9 = 6F;
            if(super.Or.getKren() > 0.0F)
                Ve.set(100D, -f9, 10D);
            else
                Ve.set(100D, f9, 10D);
            tmpOr.transform(Ve);
            super.Or.transformInv(Ve);
            Ve.normalize();
            farTurnToDirection();
            break;

        case 3: // '\003'
            super.minElevCoeff = 20F;
            setSpeedMode(9);
            tmpOr.setYPR(super.Or.getYaw(), 0.0F, 0.0F);
            Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
            Ve.z = 0.0D;
            Ve.normalize();
            Ve.z = 0.40000000000000002D;
            super.Or.transformInv(Ve);
            Ve.normalize();
            attackTurnToDirection(1000F, f, 15F);
            break;

        case 4: // '\004'
            super.minElevCoeff = 20F;
            boomAttack(f);
            setSpeedMode(9);
            break;

        default:
            super.minElevCoeff = 20F;
            fighterVsFighter(f);
            break;
        }
        if(submanDelay > 0)
            submanDelay--;
    }

    public void bNZFighterVsFighter(float f)
    {
        Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
        super.Or.transformInv(Ve);
        dist = (float)Ve.length();
        float f1 = (float)Math.sqrt(((Tuple3d) (Ve)).x * ((Tuple3d) (Ve)).x + ((Tuple3d) (Ve)).y * ((Tuple3d) (Ve)).y);
        float f2 = 1.0F / dist;
        Vtarg.set(Ve);
        Vtarg.scale(f2);
        float f3 = (super.Energy - ((FlightModelMain) (target)).Energy) * 0.1019F;
        tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
        if(sub_Man_Count == 0)
        {
            float f4 = 0.0F;
            if(super.CT.Weapons[1] != null && super.CT.Weapons[1][0].countBullets() > 0)
                f4 = ((GunGeneric)super.CT.Weapons[1][0]).bulletSpeed();
            else
            if(super.CT.Weapons[0] != null)
                f4 = ((GunGeneric)super.CT.Weapons[0][0]).bulletSpeed();
            if(f4 > 0.01F)
                bullTime = 1.0F / f4;
            submanDelay = 0;
        }
        if(f1 < 1500F)
        {
            float f5 = 0.0F;
            float f7 = 0.0F;
            if(((Tuple3d) (Vtarg)).x > -0.20000000298023224D)
            {
                f5 = 3F * ((float)((Tuple3d) (Vtarg)).x + 0.2F);
                Vxy.set(tmpV3f);
                Vxy.scale(1.0D);
                super.Or.transformInv(Vxy);
                Vxy.add(Ve);
                Vxy.normalize();
                f7 = 20F * (float)(((Tuple3d) (Vxy)).x - ((Tuple3d) (Vtarg)).x);
                if(f7 < -1F)
                    f7 = -1F;
                if(f7 > 1.0F)
                    f7 = 1.0F;
            }
            float f9 = f3 * 0.0015F + f1 * 0.0006F + f5 + f7;
            if(f9 > 0.8F && submaneuver >= 3 || f9 > 1.2F)
            {
                if(tmpV3f.length() < 100D)
                {
                    if(submanDelay == 0)
                    {
                        submaneuver = 4;
                        submanDelay = 120;
                    }
                } else
                if(submanDelay == 0)
                {
                    submaneuver = 3;
                    submanDelay = 120;
                }
            } else
            if(f3 > 800F && submaneuver == 0 || f3 > 1000F)
            {
                Ve.set(0.0D, 0.0D, 800D);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 30;
                }
            } else
            if(dist > 450F && submaneuver == 2 || dist > 600F)
            {
                if(submanDelay == 0)
                {
                    submaneuver = 2;
                    submanDelay = 60;
                }
            } else
            if(submanDelay == 0)
            {
                submaneuver = 1;
                submanDelay = 30;
            }
        } else
        if(f1 < 3000F)
        {
            if(f3 > 600F && submaneuver == 0 || f3 > 800F)
            {
                Ve.set(0.0D, 0.0D, 800D);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 120;
                }
            } else
            if(f3 > -200F && submaneuver >= 3 || f3 > -100F)
            {
                if(submanDelay == 0)
                {
                    submaneuver = 3;
                    submanDelay = 120;
                }
            } else
            {
                Ve.set(0.0D, 0.0D, ((Tuple3d) (super.Loc)).z);
                if(submanDelay == 0)
                {
                    submaneuver = 0;
                    submanDelay = 120;
                }
            }
        } else
        {
            Ve.set(0.0D, 0.0D, 1000D);
            if(submanDelay == 0)
            {
                submaneuver = 0;
                submanDelay = 180;
            }
        }
        switch(submaneuver)
        {
        case 0: // '\0'
            ((FlightModelMain) (target)).Or.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            tmpV3f.set(Ve);
            tmpV3f.z = 0.0D;
            dist = (float)tmpV3f.length();
            tmpV3f.normalize();
            Vtarg.set(((FlightModelMain) (target)).Vwld);
            Vtarg.z = 0.0D;
            float f6 = (float)tmpV3f.dot(Vtarg);
            float f8 = getSpeed() - f6;
            if(f8 < 10F)
                f8 = 10F;
            float f10 = dist / f8;
            if(f10 < 0.0F)
                f10 = 0.0F;
            tmpV3f.scale(Vtarg, f10);
            Ve.add(tmpV3f);
            super.Or.transformInv(Ve);
            Ve.normalize();
            setSpeedMode(9);
            farTurnToDirection();
            sub_Man_Count++;
            break;

        case 1: // '\001'
            setSpeedMode(9);
            super.CT.AileronControl = -0.04F * super.Or.getKren();
            super.CT.ElevatorControl = -0.04F * (super.Or.getTangage() + 5F);
            super.CT.RudderControl = 0.0F;
            break;

        case 2: // '\002'
            setSpeedMode(9);
            tmpOr.setYPR(super.Or.getYaw(), 0.0F, 0.0F);
            if(super.Or.getKren() > 0.0F)
                Ve.set(100D, -6D, 10D);
            else
                Ve.set(100D, 6D, 10D);
            tmpOr.transform(Ve);
            super.Or.transformInv(Ve);
            Ve.normalize();
            farTurnToDirection();
            break;

        case 3: // '\003'
            super.minElevCoeff = 20F;
            fighterVsFighter(f);
            setSpeedMode(6);
            break;

        default:
            super.minElevCoeff = 20F;
            fighterVsFighter(f);
            break;
        }
        if(submanDelay > 0)
            submanDelay--;
    }

    public void setBomberAttackType(int i)
    {
        float f;
        if(Actor.isValid(((Interpolate) (target)).actor))
            f = ((Interpolate) (target)).actor.collisionR();
        else
            f = 15F;
        setRandomTargDeviation(1.5F);
        switch(i)
        {
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
            TargV.set(((FlightModelMain) (target)).EI.engines[World.Rnd().nextInt(0, ((FlightModelMain) (target)).EI.getNum() - 1)].getEnginePos());
            TargV.x--;
            ApproachR = 150F;
            TargY = 2.1F * f;
            TargZ = 3.9F * f;
            TargYS = 0.43F * f;
            TargZS = 0.43F * f;
            break;

        case 2: // '\002'
            ApproachV.set(-600F + World.Rnd().nextFloat(-100F, 100F), 0.0D, 600F + World.Rnd().nextFloat(-100F, 100F));
            TargV.set(((FlightModelMain) (target)).EI.engines[World.Rnd().nextInt(0, ((FlightModelMain) (target)).EI.getNum() - 1)].getEnginePos());
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
            ApproachV.set(600D, (float)(600 - World.Rnd().nextInt(0, 1) * 1200) + World.Rnd().nextFloat(-100F, 100F), 300D);
            TargV.set(0.0D, 0.0D, 0.0D);
            ApproachR = 300F;
            TargY = 2.1F * f;
            TargZ = 1.2F * f;
            TargYS = 0.26F * f;
            TargZS = 0.26F * f;
            break;

        case 7: // '\007'
            ApproachV.set(-1000F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
            TargV.set(((FlightModelMain) (target)).EI.engines[World.Rnd().nextInt(0, ((FlightModelMain) (target)).EI.getNum() - 1)].getEnginePos());
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
            ApproachV.set(-600F + World.Rnd().nextFloat(-100F, 100F), 0.0D, -100F + World.Rnd().nextFloat(-50F, 50F));
            TargV.set(100D, 0.0D, -400D);
            ApproachR = 300F;
            TargY = 2.1F * f;
            TargZ = 3.9F * f;
            TargYS = 0.43F * f;
            TargZS = 0.43F * f;
            break;

        default:
            ApproachV.set(-1000F + World.Rnd().nextFloat(-200F, 200F), 0.0D, 100F + World.Rnd().nextFloat(-50F, 50F));
            TargV.set(((FlightModelMain) (target)).EI.engines[World.Rnd().nextInt(0, ((FlightModelMain) (target)).EI.getNum() - 1)].getEnginePos());
            ApproachR = 200F;
            TargY = 10F;
            TargZ = 10F;
            TargYS = 2.0F;
            TargZS = 2.0F;
            break;
        }
        float f1 = 0.0F;
        if(super.CT.Weapons[1] != null && super.CT.Weapons[1][0].countBullets() > 0)
            f1 = ((GunGeneric)super.CT.Weapons[1][0]).bulletSpeed();
        else
        if(super.CT.Weapons[0] != null)
            f1 = ((GunGeneric)super.CT.Weapons[0][0]).bulletSpeed();
        if(f1 > 0.01F)
            bullTime = 1.0F / f1;
    }

    public void fighterVsBomber(float f)
    {
        maxAOA = 15F;
        tmpOr.setAT0(((FlightModelMain) (target)).Vwld);
        switch(submaneuver)
        {
        case 0: // '\0'
            super.minElevCoeff = 4F;
            rocketsDelay = 0;
            bulletDelay = 0;
            double d = 0.0001D * ((Tuple3d) (((FlightModelMain) (target)).Loc)).z;
            Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
            tmpOr.transformInv(Ve);
            scaledApproachV.set(ApproachV);
            scaledApproachV.x -= 700D * d;
            scaledApproachV.z += 500D * d;
            Ve.add(scaledApproachV);
            Ve.normalize();
            tmpV3f.scale(scaledApproachV, -1D);
            tmpV3f.normalize();
            double d1 = Ve.dot(tmpV3f);
            Ve.set(super.Vwld);
            Ve.normalize();
            tmpV3f.set(((FlightModelMain) (target)).Vwld);
            tmpV3f.normalize();
            double d2 = Ve.dot(tmpV3f);
            Ve.set(scaledApproachV);
            Ve.x += 60D * (2D * (1.0D - d1) + 4D * (1.0D - d2));
            tmpOr.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            double d3 = ((Tuple3d) (Ve)).z;
            tmpV3f.set(Ve);
            tmpV3f.z = 0.0D;
            float f7 = (float)tmpV3f.length();
            float f8 = 0.55F + 0.15F * (float)super.Skill;
            tmpV3f.normalize();
            Vtarg.set(((FlightModelMain) (target)).Vwld);
            Vtarg.z = 0.0D;
            float f9 = (float)tmpV3f.dot(Vtarg);
            float f10 = getSpeed() - f9;
            if(f10 < 10F)
                f10 = 10F;
            float f11 = f7 / f10;
            if(f11 < 0.0F)
                f11 = 0.0F;
            tmpV3f.scale(Vtarg, f11 * f8);
            Ve.add(tmpV3f);
            super.Or.transformInv(Ve);
            Ve.normalize();
            if(d3 > -200D)
            {
                setSpeedMode(9);
            } else
            {
                setSpeedMode(4);
                smConstSpeed = 180F;
            }
            attackTurnToDirection(f7, f, 10F * (float)(1.0D + d));
            sub_Man_Count++;
            if((double)f7 < (double)ApproachR * (1.0D + d) && d3 < 200D)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            break;

        case 1: // '\001'
            super.minElevCoeff = 20F;
            Ve.set(TargV);
            ((FlightModelMain) (target)).Or.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            float f1 = (float)Ve.length();
            float f3 = 0.55F + 0.15F * (float)super.Skill;
            tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
            float f5 = f1 * bullTime * 0.0025F;
            if(f5 > 0.05F)
                f5 = 0.05F;
            tmpV3f.scale(f1 * f5 * f3);
            Ve.add(tmpV3f);
            Vtarg.set(Ve);
            super.Or.transformInv(Ve);
            Ve.normalize();
            ((Maneuver)target).incDangerAggressiveness(1, (float)((Tuple3d) (Ve)).x, f1, this);
            if(f1 > 3200F || ((Tuple3d) (Vtarg)).z > 1500D)
            {
                submaneuver = 0;
                sub_Man_Count = 0;
                set_maneuver(0);
                break;
            }
            if(((Tuple3d) (Ve)).x < 0.30000001192092896D)
            {
                Vtarg.z += (double)(0.012F * (float)super.Skill * (800F + f1)) * (0.30000001192092896D - ((Tuple3d) (Ve)).x);
                Ve.set(Vtarg);
                super.Or.transformInv(Ve);
                Ve.normalize();
            }
            attackTurnToDirection(f1, f, 10F + (float)super.Skill * 8F);
            setSpeedMode(4);
            smConstSpeed = 180F;
            if(f1 < 400F)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            break;

        case 2: // '\002'
            super.minElevCoeff = 20F;
            if(rocketsDelay > 0)
                rocketsDelay--;
            if(bulletDelay > 0)
                bulletDelay--;
            if(bulletDelay == 120)
                bulletDelay = 0;
            setRandomTargDeviation(0.8F);
            Ve.set(TargV);
            Ve.add(TargDevV);
            ((FlightModelMain) (target)).Or.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            Vtarg.set(Ve);
            float f2 = (float)Ve.length();
            float f4 = 0.55F + 0.15F * (float)super.Skill;
            float f6 = 0.0002F * (float)super.Skill;
            tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
            Vpl.set(tmpV3f);
            tmpV3f.scale(f2 * (bullTime + f6) * f4);
            Ve.add(tmpV3f);
            tmpV3f.set(Vpl);
            tmpV3f.scale(f2 * bullTime * f4);
            Vtarg.add(tmpV3f);
            if(f2 > 4000F || ((Tuple3d) (Vtarg)).z > 2000D)
            {
                submaneuver = 0;
                sub_Man_Count = 0;
                set_maneuver(0);
                break;
            }
            super.Or.transformInv(Vtarg);
            if(((Tuple3d) (Vtarg)).x > 0.0D && f2 < 500F && (shotAtFriend <= 0 || distToFriend > f2) && Math.abs(((Tuple3d) (Vtarg)).y) < (double)(TargY - TargYS * (float)super.Skill) && Math.abs(((Tuple3d) (Vtarg)).z) < (double)(TargZ - TargZS * (float)super.Skill) && bulletDelay < 120)
            {
            	//TODO: DBW AI Mod Edits
                if((super.actor instanceof KI_46_OTSUHEI) || (super.actor instanceof TypeJazzPlayer))
                {
                    super.CT.WeaponControl[0] = false;
                    super.CT.WeaponControl[1] = true;
                } else
                {
                    super.CT.WeaponControl[0] = true;
                    super.CT.WeaponControl[1] = true;
                }
                bulletDelay += 2;
                if(bulletDelay >= 118)
                {
                    int i = (int)(target.getW().length() * 150D);
                    if(i > 60)
                        i = 60;
                    bulletDelay += World.Rnd().nextInt(i * super.Skill, 2 * i * super.Skill);
                }
                if(super.CT.Weapons[2] != null && super.CT.Weapons[2][0] != null && super.CT.Weapons[2][super.CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1)
                {
                    tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
                    super.Or.transformInv(tmpV3f);
                    if(Math.abs(((Tuple3d) (tmpV3f)).y) < (double)(TargY - TargYS * (float)super.Skill) && Math.abs(((Tuple3d) (tmpV3f)).z) < (double)(TargZ - TargZS * (float)super.Skill))
                        super.CT.WeaponControl[2] = true;
                    Voice.speakAttackByRockets((Aircraft)super.actor);
                    rocketsDelay += 120;
                }
                ((Maneuver)target).incDangerAggressiveness(1, 0.8F, f2, this);
                ((Pilot)target).setAsDanger(super.actor);
            }
            Vtarg.set(Ve);
            super.Or.transformInv(Ve);
            Ve.normalize();
            ((Maneuver)target).incDangerAggressiveness(1, (float)((Tuple3d) (Ve)).x, f2, this);
            if(((Tuple3d) (Ve)).x < 0.30000001192092896D)
            {
                Vtarg.z += (double)(0.01F * (float)super.Skill * (800F + f2)) * (0.30000001192092896D - ((Tuple3d) (Ve)).x);
                Ve.set(Vtarg);
                super.Or.transformInv(Ve);
                Ve.normalize();
            }
            attackTurnToDirection(f2, f, 10F + (float)super.Skill * 8F);
            if(target.getSpeed() > 70F)
            {
                setSpeedMode(2);
                tailForStaying = target;
                tailOffset.set(-20D, 0.0D, 0.0D);
            } else
            {
                setSpeedMode(4);
                smConstSpeed = 70F;
            }
            if(strikeEmer)
            {
                Vpl.sub(((FlightModelMain) (strikeTarg)).Loc, super.Loc);
                tmpV3f.set(Vpl);
                super.Or.transformInv(tmpV3f);
                if(((Tuple3d) (tmpV3f)).x < 0.0D)
                    break;
                tmpV3f.sub(((FlightModelMain) (strikeTarg)).Vwld, super.Vwld);
                tmpV3f.scale(0.69999998807907104D);
                Vpl.add(tmpV3f);
                super.Or.transformInv(Vpl);
                push();
                if(((Tuple3d) (Vpl)).z < 0.0D)
                {
                    push(0);
                    push(8);
                    push(60);
                } else
                {
                    push(0);
                    push(8);
                    push(61);
                }
                pop();
                strikeEmer = false;
                submaneuver = 0;
                sub_Man_Count = 0;
            }
            if(sub_Man_Count > 600)
            {
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

    public void fighterUnderBomber(float f)
    {
        maxAOA = 15F;
        switch(submaneuver)
        {
        case 0: // '\0'
            rocketsDelay = 0;
            bulletDelay = 0;
            Ve.set(ApproachV);
            ((FlightModelMain) (target)).Or.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            tmpV3f.set(Ve);
            tmpV3f.z = 0.0D;
            float f1 = (float)tmpV3f.length();
            float f4 = 0.55F + 0.15F * (float)super.Skill;
            tmpV3f.normalize();
            Vtarg.set(((FlightModelMain) (target)).Vwld);
            Vtarg.z = 0.0D;
            float f7 = (float)tmpV3f.dot(Vtarg);
            float f9 = getSpeed() - f7;
            if(f9 < 10F)
                f9 = 10F;
            float f10 = f1 / f9;
            if(f10 < 0.0F)
                f10 = 0.0F;
            tmpV3f.scale(Vtarg, f10 * f4);
            Ve.add(tmpV3f);
            super.Or.transformInv(Ve);
            Ve.normalize();
            setSpeedMode(4);
            smConstSpeed = 140F;
            farTurnToDirection();
            sub_Man_Count++;
            if(f1 < ApproachR)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            break;

        case 1: // '\001'
            Ve.set(TargV);
            ((FlightModelMain) (target)).Or.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            float f2 = (float)Ve.length();
            float f5 = 0.55F + 0.15F * (float)super.Skill;
            tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
            float f8 = f2 * bullTime * 0.0025F;
            if(f8 > 0.02F)
                f8 = 0.02F;
            tmpV3f.scale(f2 * f8 * f5);
            Ve.add(tmpV3f);
            Vtarg.set(Ve);
            super.Or.transformInv(Ve);
            Ve.normalize();
            ((Maneuver)target).incDangerAggressiveness(1, (float)((Tuple3d) (Ve)).x, f2, this);
            if(f2 > 3200F || ((Tuple3d) (Vtarg)).z > 1500D)
            {
                submaneuver = 0;
                sub_Man_Count = 0;
                break;
            }
            if(((Tuple3d) (Ve)).x < 0.30000001192092896D)
            {
                Vtarg.z += (double)(0.01F * (float)super.Skill * (800F + f2)) * (0.30000001192092896D - ((Tuple3d) (Ve)).x);
                Ve.set(Vtarg);
                super.Or.transformInv(Ve);
                Ve.normalize();
            }
            attackTurnToDirection(f2, f, 10F);
            if(target.getSpeed() > 120F)
            {
                setSpeedMode(2);
                tailForStaying = target;
            } else
            {
                setSpeedMode(4);
                smConstSpeed = 120F;
            }
            if(f2 < 400F)
            {
                submaneuver++;
                sub_Man_Count = 0;
            }
            break;

        case 2: // '\002'
            setCheckStrike(false);
            if(rocketsDelay > 0)
                rocketsDelay--;
            if(bulletDelay > 0)
                bulletDelay--;
            if(bulletDelay == 120)
                bulletDelay = 0;
            setRandomTargDeviation(0.8F);
            Ve.set(TargV);
            Ve.add(TargDevV);
            ((FlightModelMain) (target)).Or.transform(Ve);
            Ve.add(((FlightModelMain) (target)).Loc);
            Ve.sub(super.Loc);
            float f3 = (float)Ve.length();
            float f6 = 0.55F + 0.15F * (float)super.Skill;
            tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
            tmpV3f.scale(f3 * bullTime * f6);
            Ve.add(tmpV3f);
            Vtarg.set(Ve);
            super.Or.transformInv(Ve);
            if(f3 > 4000F || ((Tuple3d) (Vtarg)).z > 2000D)
            {
                submaneuver = 0;
                sub_Man_Count = 0;
                break;
            }
            if(super.AS.astatePilotStates[1] < 100 && f3 < 330F && Math.abs(super.Or.getKren()) < 3F)
                super.CT.WeaponControl[0] = true;
            Ve.normalize();
            if(((Tuple3d) (Ve)).x < 0.30000001192092896D)
            {
                Vtarg.z += (double)(0.01F * (float)super.Skill * (800F + f3)) * (0.30000001192092896D - ((Tuple3d) (Ve)).x);
                Ve.set(Vtarg);
                super.Or.transformInv(Ve);
                Ve.normalize();
            }
            attackTurnToDirection(f3, f, 6F + (float)super.Skill * 3F);
            setSpeedMode(1);
            tailForStaying = target;
            tailOffset.set(-190D, 0.0D, 0.0D);
            if(sub_Man_Count > 10000 || f3 < 240F)
            {
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

    public void fighterVsFighter(float f)
    {
        if(sub_Man_Count == 0)
        {
            float f1 = 0.0F;
            if(super.CT.Weapons[1] != null && super.CT.Weapons[1][0].countBullets() > 0)
                f1 = ((GunGeneric)super.CT.Weapons[1][0]).bulletSpeed();
            else
            if(super.CT.Weapons[0] != null)
                f1 = ((GunGeneric)super.CT.Weapons[0][0]).bulletSpeed();
            if(f1 > 0.01F)
                bullTime = 1.0F / f1;
        }
        maxAOA = 15F;
        if(rocketsDelay > 0)
            rocketsDelay--;
        if(bulletDelay > 0)
            bulletDelay--;
        if(bulletDelay == 122)
            bulletDelay = 0;
        Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
        setRandomTargDeviation(0.3F);
        Ve.add(TargDevV);
        Vtarg.set(Ve);
        float f2 = (float)Ve.length();
        float f3 = 0.55F + 0.15F * (float)super.Skill;
        float f4 = 0.0002F * (float)super.Skill;
        tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
        Vpl.set(tmpV3f);
        tmpV3f.scale(f2 * (bullTime + f4) * f3);
        Ve.add(tmpV3f);
        tmpV3f.set(Vpl);
        tmpV3f.scale(f2 * bullTime * f3);
        Vtarg.add(tmpV3f);
        super.Or.transformInv(Vpl);
        float f5 = (float)(-((Tuple3d) (Vpl)).x);
        if(f5 < 0.001F)
            f5 = 0.001F;
        Vpl.normalize();
        if(((Tuple3d) (Vpl)).x < -0.93999999761581421D && f2 / f5 < 1.5F * (float)(3 - super.Skill))
        {
            push();
            set_maneuver(14);
        } else
        {
            super.Or.transformInv(Vtarg);
            if(((Tuple3d) (Vtarg)).x > 0.0D && f2 < 500F && (shotAtFriend <= 0 || distToFriend > f2))
            {
                if(Math.abs(((Tuple3d) (Vtarg)).y) < 13D && Math.abs(((Tuple3d) (Vtarg)).z) < 13D)
                    ((Maneuver)target).incDangerAggressiveness(1, 0.95F, f2, this);
                if(Math.abs(((Tuple3d) (Vtarg)).y) < (double)(12.5F - 3.5F * (float)super.Skill) && Math.abs(((Tuple3d) (Vtarg)).z) < 12.5D - 3.5D * (double)super.Skill && bulletDelay < 120)
                {
                	//TODO: DBW AI Mod Edits
                    if((super.actor instanceof KI_46_OTSUHEI) || (super.actor instanceof TypeJazzPlayer))
                    {
                        super.CT.WeaponControl[0] = false;
                        super.CT.WeaponControl[1] = true;
                    } else
                    {
                        super.CT.WeaponControl[0] = true;
                    }
                    bulletDelay += 2;
                    if(bulletDelay >= 118)
                    {
                        int i = (int)(target.getW().length() * 150D);
                        if(i > 60)
                            i = 60;
                        bulletDelay += World.Rnd().nextInt(i * super.Skill, 2 * i * super.Skill);
                    }
                    if(f2 < 400F)
                    {
                        super.CT.WeaponControl[1] = true;
                        if(f2 < 100F && super.CT.Weapons[2] != null && super.CT.Weapons[2][0] != null && super.CT.Weapons[2][super.CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1)
                        {
                            tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
                            super.Or.transformInv(tmpV3f);
                            if(Math.abs(((Tuple3d) (tmpV3f)).y) < 4D && Math.abs(((Tuple3d) (tmpV3f)).z) < 4D)
                                super.CT.WeaponControl[2] = true;
                            Voice.speakAttackByRockets((Aircraft)super.actor);
                            rocketsDelay += 120;
                        }
                    }
                    ((Pilot)target).setAsDanger(super.actor);
                }
            }
            Vtarg.set(Ve);
            super.Or.transformInv(Ve);
            Ve.normalize();
            ((Maneuver)target).incDangerAggressiveness(1, (float)((Tuple3d) (Ve)).x, f2, this);
            if(((Tuple3d) (Ve)).x < 0.30000001192092896D)
            {
                Vtarg.z += (double)(0.01F * (float)super.Skill * (800F + f2)) * (0.30000001192092896D - ((Tuple3d) (Ve)).x);
                Ve.set(Vtarg);
                super.Or.transformInv(Ve);
                Ve.normalize();
            }
            attackTurnToDirection(f2, f, 10F + (float)super.Skill * 8F);
            if(Alt > 150F && ((Tuple3d) (Ve)).x > 0.59999999999999998D && (double)f2 < 70D)
            {
                setSpeedMode(1);
                tailForStaying = target;
                tailOffset.set(-20D, 0.0D, 0.0D);
            } else
            {
                setSpeedMode(9);
            }
        }
    }

    public void boomAttack(float f)
    {
        if(sub_Man_Count == 0)
        {
            float f1 = 0.0F;
            if(super.CT.Weapons[1] != null && super.CT.Weapons[1][0].countBullets() > 0)
                f1 = ((GunGeneric)super.CT.Weapons[1][0]).bulletSpeed();
            else
            if(super.CT.Weapons[0] != null)
                f1 = ((GunGeneric)super.CT.Weapons[0][0]).bulletSpeed();
            if(f1 > 0.01F)
                bullTime = 1.0F / f1;
        }
        maxAOA = 15F;
        if(rocketsDelay > 0)
            rocketsDelay--;
        if(bulletDelay > 0)
            bulletDelay--;
        if(bulletDelay == 122)
            bulletDelay = 0;
        Ve.sub(((FlightModelMain) (target)).Loc, super.Loc);
        setRandomTargDeviation(0.3F);
        Ve.add(TargDevV);
        Vtarg.set(Ve);
        float f2 = (float)Ve.length();
        float f3 = 0.55F + 0.15F * (float)super.Skill;
        float f4 = 0.000333F * (float)super.Skill;
        tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
        Vpl.set(tmpV3f);
        tmpV3f.scale(f2 * (bullTime + f4) * f3);
        Ve.add(tmpV3f);
        tmpV3f.set(Vpl);
        tmpV3f.scale(f2 * bullTime * f3);
        Vtarg.add(tmpV3f);
        super.Or.transformInv(Vpl);
        float f5 = (float)(-((Tuple3d) (Vpl)).x);
        if(f5 < 0.001F)
            f5 = 0.001F;
        Vpl.normalize();
        if(((Tuple3d) (Vpl)).x < -0.93999999761581421D && f2 / f5 < 1.5F * (float)(3 - super.Skill))
        {
            push();
            set_maneuver(14);
        } else
        {
            super.Or.transformInv(Vtarg);
            if(((Tuple3d) (Vtarg)).x > 0.0D && f2 < 700F && (shotAtFriend <= 0 || distToFriend > f2) && Math.abs(((Tuple3d) (Vtarg)).y) < (double)(15.5F - 3.5F * (float)super.Skill) && Math.abs(((Tuple3d) (Vtarg)).z) < 15.5D - 3.5D * (double)super.Skill && bulletDelay < 120)
            {
                ((Maneuver)target).incDangerAggressiveness(1, 0.8F, f2, this);
                //TODO: DBW AI Mod Edits
                if((super.actor instanceof KI_46_OTSUHEI) || (super.actor instanceof TypeJazzPlayer))
                {
                    super.CT.WeaponControl[0] = false;
                    super.CT.WeaponControl[1] = true;
                } else
                {
                    super.CT.WeaponControl[0] = true;
                }
                bulletDelay += 2;
                if(bulletDelay >= 118)
                {
                    int i = (int)(target.getW().length() * 150D);
                    if(i > 60)
                        i = 60;
                    bulletDelay += World.Rnd().nextInt(i * super.Skill, 2 * i * super.Skill);
                }
                if(f2 < 500F)
                {
                    super.CT.WeaponControl[1] = true;
                    if(f2 < 100F && super.CT.Weapons[2] != null && super.CT.Weapons[2][0] != null && super.CT.Weapons[2][super.CT.Weapons[2].length - 1].countBullets() != 0 && rocketsDelay < 1)
                    {
                        tmpV3f.sub(((FlightModelMain) (target)).Vwld, super.Vwld);
                        super.Or.transformInv(tmpV3f);
                        if(Math.abs(((Tuple3d) (tmpV3f)).y) < 4D && Math.abs(((Tuple3d) (tmpV3f)).z) < 4D)
                            super.CT.WeaponControl[2] = true;
                        Voice.speakAttackByRockets((Aircraft)super.actor);
                        rocketsDelay += 120;
                    }
                }
                ((Pilot)target).setAsDanger(super.actor);
            }
            Vtarg.set(Ve);
            super.Or.transformInv(Ve);
            Ve.normalize();
            ((Maneuver)target).incDangerAggressiveness(1, (float)((Tuple3d) (Ve)).x, f2, this);
            if(((Tuple3d) (Ve)).x < 0.89999997615814209D)
            {
                Vtarg.z += (double)(0.03F * (float)super.Skill * (800F + f2)) * (0.89999997615814209D - ((Tuple3d) (Ve)).x);
                Ve.set(Vtarg);
                super.Or.transformInv(Ve);
                Ve.normalize();
            }
            attackTurnToDirection(f2, f, 10F + (float)super.Skill * 8F);
        }
    }

    private void turnToDirection(float f)
    {
        if(Math.abs(((Tuple3d) (Ve)).y) > 0.10000000149011612D)
            super.CT.AileronControl = -(float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).z) - 0.016F * super.Or.getKren();
        else
            super.CT.AileronControl = -(float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) - 0.016F * super.Or.getKren();
        super.CT.RudderControl = -10F * (float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x);
        if((double)super.CT.RudderControl * ((Tuple3d) (super.W)).z > 0.0D)
            super.W.z = 0.0D;
        else
            super.W.z *= 1.0399999618530273D;
        float f1 = (float)Math.atan2(((Tuple3d) (Ve)).z, ((Tuple3d) (Ve)).x);
        if(Math.abs(((Tuple3d) (Ve)).y) < 0.0020000000949949026D && Math.abs(((Tuple3d) (Ve)).z) < 0.0020000000949949026D)
            f1 = 0.0F;
        if(((Tuple3d) (Ve)).x < 0.0D)
        {
            f1 = 1.0F;
        } else
        {
            if((double)f1 * ((Tuple3d) (super.W)).y > 0.0D)
                super.W.y = 0.0D;
            if(f1 < 0.0F)
            {
                if(getOverload() < 0.1F)
                    f1 = 0.0F;
                if(super.CT.ElevatorControl > 0.0F)
                    super.CT.ElevatorControl = 0.0F;
            } else
            if(super.CT.ElevatorControl < 0.0F)
                super.CT.ElevatorControl = 0.0F;
        }
        if(getOverload() > maxG || super.AOA > maxAOA || super.CT.ElevatorControl > f1)
            super.CT.ElevatorControl -= 0.3F * f;
        else
            super.CT.ElevatorControl += 0.3F * f;
    }

    private void farTurnToDirection()
    {
        farTurnToDirection(4F);
    }

    private void farTurnToDirection(float f)
    {
        Vpl.set(1.0D, 0.0D, 0.0D);
        tmpV3f.cross(Vpl, Ve);
        super.Or.transform(tmpV3f);
        super.CT.RudderControl = -10F * (float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) + 1.0F * (float)((Tuple3d) (super.W)).y;
        float f1 = (getSpeed() / super.Vmax) * 45F;
        if(f1 > 85F)
            f1 = 85F;
        float f2 = (float)((Tuple3d) (Ve)).x;
        if(f2 < 0.0F)
            f2 = 0.0F;
        float f3;
        if(((Tuple3d) (tmpV3f)).z >= 0.0D)
            f3 = (-0.02F * (f1 + super.Or.getKren()) * (1.0F - f2) - 0.05F * super.Or.getTangage()) + 1.0F * (float)((Tuple3d) (super.W)).x;
        else
            f3 = -0.02F * (-f1 + super.Or.getKren()) * (1.0F - f2) + 0.05F * super.Or.getTangage() + 1.0F * (float)((Tuple3d) (super.W)).x;
        float f4 = (-(float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) - 0.016F * super.Or.getKren()) + 1.0F * (float)((Tuple3d) (super.W)).x;
        float f5 = -0.1F * (getAOA() - 10F) + 0.5F * (float)((Tuple3d) (getW())).y;
        float f6;
        if(((Tuple3d) (Ve)).z > 0.0D)
            f6 = -0.1F * (getAOA() - f) + 0.5F * (float)((Tuple3d) (getW())).y;
        else
            f6 = 1.0F * (float)((Tuple3d) (Ve)).z + 0.5F * (float)((Tuple3d) (getW())).y;
        if(Math.abs(((Tuple3d) (Ve)).y) < 0.0020000000949949026D)
        {
            f4 = 0.0F;
            super.CT.RudderControl = 0.0F;
        }
        float f7 = 1.0F - Math.abs((float)((Tuple3d) (Ve)).y) * 2.0F;
        if(f7 < 0.0F || ((Tuple3d) (Ve)).x < 0.0D)
            f7 = 0.0F;
        float f8 = f7 * f4 + (1.0F - f7) * f3;
        float f9 = f7 * f6 + (1.0F - f7) * f5;
        super.CT.AileronControl = f8;
        super.CT.ElevatorControl = f9;
    }

    private void attackTurnToDirection(float f, float f1, float f2)
    {
        if(((Tuple3d) (Ve)).x < 0.0099999997764825821D)
            Ve.x = 0.0099999997764825821D;
        if(sub_Man_Count == 0)
            oldVe.set(Ve);
        if(((Tuple3d) (Ve)).x > 0.94999998807907104D)
        {
            super.CT.RudderControl = (float)(-30D * Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) + 1.5D * (((Tuple3d) (Ve)).y - ((Tuple3d) (oldVe)).y));
            float f3;
            if(((Tuple3d) (Ve)).z > 0.0D || super.CT.RudderControl > 0.9F)
            {
                f3 = (float)(10D * Math.atan2(((Tuple3d) (Ve)).z, ((Tuple3d) (Ve)).x) + 6D * (((Tuple3d) (Ve)).z - ((Tuple3d) (oldVe)).z));
                super.CT.AileronControl = (-30F * (float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) - 0.02F * super.Or.getKren()) + 5F * (float)((Tuple3d) (super.W)).x;
            } else
            {
                f3 = (float)(5D * Math.atan2(((Tuple3d) (Ve)).z, ((Tuple3d) (Ve)).x) + 6D * (((Tuple3d) (Ve)).z - ((Tuple3d) (oldVe)).z));
                super.CT.AileronControl = (-5F * (float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) - 0.02F * super.Or.getKren()) + 5F * (float)((Tuple3d) (super.W)).x;
            }
            if(((Tuple3d) (Ve)).x > (double)(1.0F - 0.005F * (float)super.Skill))
            {
                tmpOr.set(super.Or);
                tmpOr.increment((float)Math.toDegrees(Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x)), (float)Math.toDegrees(Math.atan2(((Tuple3d) (Ve)).z, ((Tuple3d) (Ve)).x)), 0.0F);
                super.Or.interpolate(tmpOr, 0.1F);
            }
            if(Math.abs(super.CT.ElevatorControl - f3) < f2 * f1)
                super.CT.ElevatorControl = f3;
            else
            if(super.CT.ElevatorControl < f3)
                super.CT.ElevatorControl += f2 * f1;
            else
                super.CT.ElevatorControl -= f2 * f1;
        } else
        {
            if(super.Skill >= 2 && ((Tuple3d) (Ve)).z > 0.5D && f < 600F)
                super.CT.FlapsControl = 0.1F;
            else
                super.CT.FlapsControl = 0.0F;
            float f4 = 0.6F - (float)((Tuple3d) (Ve)).z;
            if(f4 < 0.0F)
                f4 = 0.0F;
            super.CT.RudderControl = (float)(-30D * Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) * (double)f4 + 1.0D * (((Tuple3d) (Ve)).y - ((Tuple3d) (oldVe)).y) * ((Tuple3d) (Ve)).x + 0.5D * ((Tuple3d) (super.W)).z);
            float f6;
            if(((Tuple3d) (Ve)).z > 0.0D)
            {
                f6 = (float)(10D * Math.atan2(((Tuple3d) (Ve)).z, ((Tuple3d) (Ve)).x) + 6D * (((Tuple3d) (Ve)).z - ((Tuple3d) (oldVe)).z) + 0.5D * (double)(float)((Tuple3d) (super.W)).y);
                if(f6 < 0.0F)
                    f6 = 0.0F;
                super.CT.AileronControl = (float)((-20D * Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).z) - 0.050000000000000003D * (double)super.Or.getKren()) + 5D * ((Tuple3d) (super.W)).x);
            } else
            {
                f6 = (float)(-5D * Math.atan2(((Tuple3d) (Ve)).z, ((Tuple3d) (Ve)).x) + 6D * (((Tuple3d) (Ve)).z - ((Tuple3d) (oldVe)).z) + 0.5D * (double)(float)((Tuple3d) (super.W)).y);
                super.CT.AileronControl = (float)((-20D * Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).z) - 0.050000000000000003D * (double)super.Or.getKren()) + 5D * ((Tuple3d) (super.W)).x);
            }
            if(f6 < 0.0F)
                f6 = 0.0F;
            if(Math.abs(super.CT.ElevatorControl - f6) < f2 * f1)
                super.CT.ElevatorControl = f6;
            else
            if(super.CT.ElevatorControl < f6)
                super.CT.ElevatorControl += 0.3F * f1;
            else
                super.CT.ElevatorControl -= 0.3F * f1;
        }
        float f5 = 0.054F * (600F - f);
        if(f5 < 4F)
            f5 = 4F;
        if(f5 > super.AOA_Crit)
            f5 = super.AOA_Crit;
        if(super.AOA > f5 - 1.5F)
            super.Or.increment(0.0F, 0.16F * (f5 - 1.5F - super.AOA), 0.0F);
        if(super.AOA < -5F)
            super.Or.increment(0.0F, 0.12F * (-5F - super.AOA), 0.0F);
        oldVe.set(Ve);
        oldAOA = super.AOA;
        sub_Man_Count++;
        super.W.x *= 0.94999999999999996D;
    }

    private void doCheckStrike()
    {
        if((super.M.massEmpty <= 5000F || super.AP.way.isLanding()) && (maneuver != 24 || strikeTarg != super.Leader) && (!(super.actor instanceof TypeDockable) || !((TypeDockable)super.actor).typeDockableIsDocked()))
        {
            Vpl.sub(((FlightModelMain) (strikeTarg)).Loc, super.Loc);
            tmpV3f.set(Vpl);
            super.Or.transformInv(tmpV3f);
            if(((Tuple3d) (tmpV3f)).x >= 0.0D)
            {
                tmpV3f.sub(((FlightModelMain) (strikeTarg)).Vwld, super.Vwld);
                tmpV3f.scale(0.69999998807907104D);
                Vpl.add(tmpV3f);
                super.Or.transformInv(Vpl);
                push();
                if(((Tuple3d) (Vpl)).z > 0.0D)
                    push(61);
                else
                    push(60);
                safe_pop();
            }
        }
    }

    public void setStrikeEmer(FlightModel flightmodel)
    {
        strikeEmer = true;
        strikeTarg = flightmodel;
    }

    private float bombDist(float f)
    {
        float f1 = FMMath.positiveSquareEquation(-0.5F * World.g(), (float)((Tuple3d) (super.Vwld)).z, f);
        if(f1 < 0.0F)
            return -1000000F;
        else
            return f1 * (float)Math.sqrt(((Tuple3d) (super.Vwld)).x * ((Tuple3d) (super.Vwld)).x + ((Tuple3d) (super.Vwld)).y * ((Tuple3d) (super.Vwld)).y);
    }

    protected void wingman(boolean flag)
    {
        if(super.Wingman != null)
            super.Wingman.Leader = flag ? ((FlightModel) (this)) : null;
    }

    public float getMnTime()
    {
        return mn_time;
    }

    public void doDumpBombsPassively()
    {
        boolean flag = false;
        for(int i = 0; i < super.CT.Weapons.length; i++)
            if(super.CT.Weapons[i] != null && super.CT.Weapons[i].length > 0)
            {
                for(int j = 0; j < super.CT.Weapons[i].length; j++)
                    if(super.CT.Weapons[i][j] instanceof BombGun)
                        if(super.CT.Weapons[i][j] instanceof BombGunPara)
                        {
                            flag = true;
                        } else
                        {
                            ((BombGun)super.CT.Weapons[i][j]).setBombDelay(3.3E+007F);
                            if(super.actor == World.getPlayerAircraft() && !World.cur().diffCur.Limited_Ammo)
                                super.CT.Weapons[i][j].loadBullets(0);
                        }

            }

        if(!flag)
            bombsOut = true;
    }

    protected void printDebugFM()
    {
        System.out.print("<" + super.actor.name() + "> " + ManString.tname(task) + ":" + ManString.name(maneuver) + (target == null ? "t" : "T") + (danger == null ? "d" : "D") + (target_ground == null ? "g " : "G "));
        switch(maneuver)
        {
        case 21: // '\025'
            System.out.println(": WP=" + super.AP.way.Cur() + "(" + (super.AP.way.size() - 1) + ")-" + ManString.wpname(super.AP.way.curr().Action));
            if(target_ground != null)
                System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
            break;

        case 27: // '\033'
            if(target == null || !Actor.isValid(((Interpolate) (target)).actor))
                System.out.println(" T=null");
            else
                System.out.println(" T=" + ((Interpolate) (target)).actor.getClass().getName() + "(" + ((Interpolate) (target)).actor.name() + ")");
            break;

        case 43: // '+'
        case 50: // '2'
        case 51: // '3'
        case 52: // '4'
            if(target_ground == null || !Actor.isValid(target_ground))
                System.out.println(" GT=null");
            else
                System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
            break;

        default:
            System.out.println("");
            if(target == null || !Actor.isValid(((Interpolate) (target)).actor))
            {
                System.out.println(" T=null");
                break;
            }
            System.out.println(" T=" + ((Interpolate) (target)).actor.getClass().getName() + "(" + ((Interpolate) (target)).actor.name() + ")");
            if(target_ground == null || !Actor.isValid(target_ground))
                System.out.println(" GT=null");
            else
                System.out.println(" GT=" + target_ground.getClass().getName() + "(" + target_ground.name() + ")");
            break;
        }
    }

    protected void headTurn(float f)
    {
        if(super.actor == Main3D.cur3D().viewActor() && super.AS.astatePilotStates[0] < 90)
        {
            boolean flag = false;
            switch(get_task())
            {
            case 2: // '\002'
                if(super.Leader != null)
                {
                    Ve.set(((FlightModelMain) (super.Leader)).Loc);
                    flag = true;
                }
                break;

            case 6: // '\006'
                if(target != null)
                {
                    Ve.set(((FlightModelMain) (target)).Loc);
                    flag = true;
                }
                break;

            case 5: // '\005'
                if(airClient != null)
                {
                    Ve.set(((FlightModelMain) (airClient)).Loc);
                    flag = true;
                }
                break;

            case 4: // '\004'
                if(danger != null)
                {
                    Ve.set(((FlightModelMain) (danger)).Loc);
                    flag = true;
                }
                break;

            case 7: // '\007'
                if(target_ground != null)
                {
                    Ve.set(target_ground.pos.getAbsPoint());
                    flag = true;
                }
                break;
            }
            float f1;
            float f2;
            if(flag)
            {
                Ve.sub(super.Loc);
                super.Or.transformInv(Ve);
                tmpOr.setAT0(Ve);
                f1 = tmpOr.getTangage();
                f2 = tmpOr.getYaw();
                if(f2 > 75F)
                    f2 = 75F;
                if(f2 < -75F)
                    f2 = -75F;
                if(f1 < -15F)
                    f1 = -15F;
                if(f1 > 40F)
                    f1 = 40F;
            } else
            if(get_maneuver() != 44)
            {
                f1 = 0.0F;
                f2 = 0.0F;
            } else
            {
                f2 = -15F;
                f1 = -15F;
            }
            if(Math.abs(pilotHeadT - f1) > 3F)
            {
                Maneuver maneuver1 = this;
                maneuver1.pilotHeadT = maneuver1.pilotHeadT + 90F * (pilotHeadT <= f1 ? 1.0F : -1F) * f;
            } else
            {
                pilotHeadT = f1;
            }
            if(Math.abs(pilotHeadY - f2) > 2.0F)
            {
                Maneuver maneuver2 = this;
                maneuver2.pilotHeadY = maneuver2.pilotHeadY + 60F * (pilotHeadY <= f2 ? 1.0F : -1F) * f;
            } else
            {
                pilotHeadY = f2;
            }
            tmpOr.setYPR(0.0F, 0.0F, 0.0F);
            tmpOr.increment(0.0F, pilotHeadY, 0.0F);
            tmpOr.increment(pilotHeadT, 0.0F, 0.0F);
            tmpOr.increment(0.0F, 0.0F, -0.2F * pilotHeadT + 0.05F * pilotHeadY);
            headOr[0] = tmpOr.getYaw();
            headOr[1] = tmpOr.getPitch();
            headOr[2] = tmpOr.getRoll();
            headPos[0] = 0.0005F * Math.abs(pilotHeadY);
            headPos[1] = -0.0001F * Math.abs(pilotHeadY);
            headPos[2] = 0.0F;
            ((ActorHMesh)super.actor).hierMesh().chunkSetLocate("Head1_D0", headPos, headOr);
        }
    }

    protected void turnOffTheWeapon()
    {
        super.CT.WeaponControl[0] = false;
        super.CT.WeaponControl[1] = false;
        super.CT.WeaponControl[2] = false;
        super.CT.WeaponControl[3] = false;
        if(bombsOut)
        {
            bombsOutCounter++;
            if(bombsOutCounter > 128)
            {
                bombsOutCounter = 0;
                bombsOut = false;
            }
            if(super.CT.Weapons[3] != null)
                super.CT.WeaponControl[3] = true;
            else
                bombsOut = false;
            if(super.CT.Weapons[3] != null && super.CT.Weapons[3][0] != null)
            {
                int i = 0;
                for(int j = 0; j < super.CT.Weapons[3].length; j++)
                    i += super.CT.Weapons[3][j].countBullets();

                if(i == 0)
                {
                    bombsOut = false;
                    for(int k = 0; k < super.CT.Weapons[3].length; k++)
                        super.CT.Weapons[3][k].loadBullets(0);

                }
            }
        }
    }

    protected void turnOnChristmasTree(boolean flag)
    {
        if(flag)
            super.AS.setNavLightsState(true);
        else
            super.AS.setNavLightsState(false);
    }

    protected void turnOnCloudShine(boolean flag)
    {
        if(flag)
            super.AS.setLandingLightState(true);
        else
            super.AS.setLandingLightState(false);
    }

    protected void doCheckGround(float f)
    {
        if(super.CT.AileronControl > 1.0F)
            super.CT.AileronControl = 1.0F;
        if(super.CT.AileronControl < -1F)
            super.CT.AileronControl = -1F;
        if(super.CT.ElevatorControl > 1.0F)
            super.CT.ElevatorControl = 1.0F;
        if(super.CT.ElevatorControl < -1F)
            super.CT.ElevatorControl = -1F;
        float f1 = 0.0003F * super.M.massEmpty;
        float f2 = 10F;
        float f3 = 80F;
        if(maneuver == 24)
        {
            f2 = 15F;
            f3 = 120F;
        }
        float f4 = (float)(-((Tuple3d) (super.Vwld)).z) * f2 * f1;
        float f5 = 1.0F + 7F * ((super.indSpeed - super.VmaxAllowed) / super.VmaxAllowed);
        if(f5 > 1.0F)
            f5 = 1.0F;
        if(f5 < 0.0F)
            f5 = 0.0F;
        float f6;
        float f7;
        float f8;
        if(f4 > Alt || Alt < f3 || f5 > 0.0F)
        {
            if(Alt < 0.001F)
                Alt = 0.001F;
            f6 = (f4 - Alt) / Alt;
            Math.max(0.016670000000000001D * (double)(f3 - Alt), f6);
            if(f6 > 1.0F)
                f6 = 1.0F;
            if(f6 < 0.0F)
                f6 = 0.0F;
            if(f6 < f5)
                f6 = f5;
            f7 = -0.12F * (super.Or.getTangage() - 5F) + 3F * (float)((Tuple3d) (super.W)).y;
            f8 = -0.07F * super.Or.getKren() + 3F * (float)((Tuple3d) (super.W)).x;
            if(f8 > 2.0F)
                f8 = 2.0F;
            if(f8 < -2F)
                f8 = -2F;
            if(f7 > 2.0F)
                f7 = 2.0F;
            if(f7 < -2F)
                f7 = -2F;
        } else
        {
            f6 = 0.0F;
            f7 = 0.0F;
            f8 = 0.0F;
        }
        float f9 = 0.2F;
        if(corrCoeff < f6)
            corrCoeff = f6;
        if(corrCoeff > f6)
            corrCoeff *= 1.0D - (double)(f9 * f);
        if(f7 < 0.0F)
        {
            if(corrElev > f7)
                corrElev = f7;
            if(corrElev < f7)
                corrElev *= 1.0D - (double)(f9 * f);
        } else
        {
            if(corrElev < f7)
                corrElev = f7;
            if(corrElev > f7)
                corrElev *= 1.0D - (double)(f9 * f);
        }
        if(f8 < 0.0F)
        {
            if(corrAile > f8)
                corrAile = f8;
            if(corrAile < f8)
                corrAile *= 1.0D - (double)(f9 * f);
        } else
        {
            if(corrAile < f8)
                corrAile = f8;
            if(corrAile > f8)
                corrAile *= 1.0D - (double)(f9 * f);
        }
        super.CT.AileronControl = corrCoeff * corrAile + (1.0F - corrCoeff) * super.CT.AileronControl;
        super.CT.ElevatorControl = corrCoeff * corrElev + (1.0F - corrCoeff) * super.CT.ElevatorControl;
        if(Alt < 15F && ((Tuple3d) (super.Vwld)).z < 0.0D)
            super.Vwld.z *= 0.85000002384185791D;
        if(-((Tuple3d) (super.Vwld)).z * 1.5D > (double)Alt || Alt < 10F)
        {
            if(maneuver == 27 || maneuver == 43 || maneuver == 21 || maneuver == 24 || maneuver == 68 || maneuver == 53)
                push();
            push(2);
            pop();
            submaneuver = 0;
            sub_Man_Count = 0;
        }
        super.W.x *= 0.94999999999999996D;
    }

    protected void setSpeedControl(float f)
    {
        float f1 = 0.8F;
        float f4 = 0.02F;
        float f5 = 1.5F;
        super.CT.setAfterburnerControl(false);
        switch(speedMode)
        {
        case 1: // '\001'
            if(tailForStaying == null)
            {
                f1 = 1.0F;
            } else
            {
                Ve.sub(((FlightModelMain) (tailForStaying)).Loc, super.Loc);
                ((FlightModelMain) (tailForStaying)).Or.transform(tailOffset, tmpV3f);
                Ve.add(tmpV3f);
                float f6 = (float)((Tuple3d) (Ve)).z;
                float f10 = 0.005F * (200F - (float)Ve.length());
                super.Or.transformInv(Ve);
                float f12 = (float)((Tuple3d) (Ve)).x;
                Ve.normalize();
                f10 = Math.max(f10, (float)((Tuple3d) (Ve)).x);
                if(f10 < 0.0F)
                    f10 = 0.0F;
                Ve.set(super.Vwld);
                Ve.normalize();
                tmpV3f.set(((FlightModelMain) (tailForStaying)).Vwld);
                tmpV3f.normalize();
                float f14 = (float)tmpV3f.dot(Ve);
                if(f14 < 0.0F)
                    f14 = 0.0F;
                f10 *= f14;
                if(f10 > 0.0F && f12 < 1000F)
                {
                    if(f12 > 300F)
                        f12 = 300F;
                    float f16 = 0.6F;
                    if(((FlightModelMain) (tailForStaying)).VmaxH == super.VmaxH)
                        f16 = ((FlightModelMain) (tailForStaying)).CT.getPower();
                    if(super.Vmax < 83F)
                    {
                        float f18 = f6;
                        if(f18 > 0.0F)
                        {
                            if(f18 > 20F)
                                f18 = 20F;
                            super.Vwld.z += 0.01F * f18;
                        }
                    }
                    float f19;
                    if(f12 > 0.0F)
                        f19 = 0.007F * f12 + 0.005F * f6;
                    else
                        f19 = 0.03F * f12 + 0.001F * f6;
                    float f22 = getSpeed() - tailForStaying.getSpeed();
                    float f24 = -0.3F * f22;
                    float f26 = -3F * (getForwAccel() - tailForStaying.getForwAccel());
                    if(f22 > 27F)
                    {
                        super.CT.FlapsControl = 1.0F;
                        f1 = 0.0F;
                    } else
                    {
                        super.CT.FlapsControl = 0.02F * f22 + 0.02F * -f12;
                        f1 = f16 + f19 + f24 + f26;
                    }
                } else
                {
                    f1 = 1.1F;
                }
            }
            break;

        case 2: // '\002'
            f1 = (float)(1.0D - 8.0000000000000007E-005D * (0.5D * super.Vwld.lengthSquared() - 9.8000000000000007D * ((Tuple3d) (Ve)).z - 0.5D * ((FlightModelMain) (tailForStaying)).Vwld.lengthSquared()));
            break;

        case 3: // '\003'
            f1 = super.CT.PowerControl;
            if(super.AP.way.curr().Speed < 10F)
                super.AP.way.curr().set(1.7F * super.Vmin);
            float f7 = super.AP.way.curr().Speed / super.VmaxH;
            f1 = 0.2F + 0.8F * (float)Math.pow(f7, 1.5D);
            f1 += 0.1F * (super.AP.way.curr().Speed - Pitot.Indicator((float)((Tuple3d) (super.Loc)).z, getSpeed())) - 3F * getForwAccel();
            if(getAltitude() < super.AP.way.curr().z() - 70F)
                f1 += super.AP.way.curr().z() - 70F - getAltitude();
            if(f1 > 0.93F)
                f1 = 0.93F;
            if(f1 < 0.35F && !super.AP.way.isLanding())
                f1 = 0.35F;
            break;

        case 4: // '\004'
            f1 = super.CT.PowerControl;
            f1 += (f4 * (smConstSpeed - Pitot.Indicator((float)((Tuple3d) (super.Loc)).z, getSpeed())) - f5 * getForwAccel()) * f;
            if(f1 > 1.0F)
                f1 = 1.0F;
            break;

        case 5: // '\005'
            f1 = super.CT.PowerControl;
            super.CT.FlapsControl = 1.0F;
            f1 += (f4 * (1.3F * super.VminFLAPS - Pitot.Indicator((float)((Tuple3d) (super.Loc)).z, getSpeed())) - f5 * getForwAccel()) * f;
            break;

        case 8: // '\b'
            f1 = 0.0F;
            break;

        case 6: // '\006'
            if(super.Skill == 0)
                f1 = 0.95F;
            else
            if(super.Skill == 1)
                f1 = 0.97F;
            else
            if(super.Skill == 2)
                f1 = 0.99F;
            else
                f1 = 1.0F;
            break;

        case 9: // '\t'
            if(super.Skill == 0)
                f1 = 1.05F;
            else
            if(super.Skill == 1)
                f1 = 1.07F;
            else
            if(super.Skill == 2)
                f1 = 1.09F;
            else
                f1 = 1.1F;
            super.CT.setAfterburnerControl(true);
            break;

        case 7: // '\007'
            f1 = smConstPower;
            break;

        case 11: // '\013'
            f1 = 1.1F;
            super.CT.setAfterburnerControl(true);
            break;

        case 10: // '\n'
            if(tailForStaying == null)
            {
                f1 = 1.0F;
            } else
            {
                Ve.sub(((FlightModelMain) (tailForStaying)).Loc, super.Loc);
                ((FlightModelMain) (tailForStaying)).Or.transform(tailOffset, tmpV3f);
                Ve.add(tmpV3f);
                float f8 = (float)((Tuple3d) (Ve)).z;
                float f11 = 0.005F * (200F - (float)Ve.length());
                super.Or.transformInv(Ve);
                float f13 = (float)((Tuple3d) (Ve)).x;
                Ve.normalize();
                f11 = Math.max(f11, (float)((Tuple3d) (Ve)).x);
                if(f11 < 0.0F)
                    f11 = 0.0F;
                Ve.set(super.Vwld);
                Ve.normalize();
                tmpV3f.set(((FlightModelMain) (tailForStaying)).Vwld);
                tmpV3f.normalize();
                float f15 = (float)tmpV3f.dot(Ve);
                if(f15 < 0.0F)
                    f15 = 0.0F;
                f11 *= f15;
                if(f11 > 0.0F && f13 < 1000F)
                {
                    if(f13 > 300F)
                        f13 = 300F;
                    float f17 = 0.6F;
                    if(((FlightModelMain) (tailForStaying)).VmaxH == super.VmaxH)
                        f17 = ((FlightModelMain) (tailForStaying)).CT.getPower();
                    if(super.Vmax < 83F)
                    {
                        float f20 = f8;
                        if(f20 > 0.0F)
                        {
                            if(f20 > 20F)
                                f20 = 20F;
                            super.Vwld.z += 0.01F * f20;
                        }
                    }
                    float f21;
                    if(f13 > 0.0F)
                        f21 = 0.007F * f13 + 0.005F * f8;
                    else
                        f21 = 0.03F * f13 + 0.001F * f8;
                    float f23 = getSpeed() - tailForStaying.getSpeed();
                    float f25 = -0.3F * f23;
                    float f27 = -3F * (getForwAccel() - tailForStaying.getForwAccel());
                    if(f23 > 27F)
                    {
                        super.Vwld.scale(0.98000001907348633D);
                        f1 = 0.0F;
                    } else
                    {
                        float f28 = 1.0F - 0.02F * (0.02F * f23 + 0.02F * -f13);
                        if(f28 < 0.98F)
                            f28 = 0.98F;
                        if(f28 > 1.0F)
                            f28 = 1.0F;
                        super.Vwld.scale(f28);
                        f1 = f17 + f21 + f25 + f27;
                    }
                } else
                {
                    f1 = 1.1F;
                }
            }
            break;

        default:
            return;
        }
        if(f1 > 1.1F)
            f1 = 1.1F;
        else
        if(f1 < 0.0F)
            f1 = 0.0F;
        if((double)Math.abs(super.CT.PowerControl - f1) < 0.5D * (double)f)
            super.CT.setPowerControl(f1);
        else
        if(super.CT.PowerControl < f1)
            super.CT.setPowerControl(super.CT.getPowerControl() + 0.5F * f);
        else
            super.CT.setPowerControl(super.CT.getPowerControl() - 0.5F * f);
        float f16 = super.EI.engines[0].getCriticalW();
        if(super.EI.engines[0].getw() > 0.9F * f16)
        {
            float f2 = (10F * (f16 - super.EI.engines[0].getw())) / f16;
            if(f2 < super.CT.PowerControl)
                super.CT.setPowerControl(f2);
        }
        if(super.indSpeed > 0.8F * super.VmaxAllowed)
        {
            float f3 = (1.0F * (super.VmaxAllowed - super.indSpeed)) / super.VmaxAllowed;
            if(f3 < super.CT.PowerControl)
                super.CT.setPowerControl(f3);
        }
    }

    private void setRandomTargDeviation(float f)
    {
        if(isTick(16, 0))
        {
            float f1 = f * (8F - 1.5F * (float)super.Skill);
            TargDevVnew.set(World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1));
        }
        TargDevV.interpolate(TargDevVnew, 0.01F);
    }

    private Point_Any getNextAirdromeWayPoint()
    {
        if(airdromeWay == null)
            return null;
        if(airdromeWay.length == 0)
            return null;
        if(curAirdromePoi >= airdromeWay.length)
            return null;
        else
            return airdromeWay[curAirdromePoi++];
    }

    private void findPointForEmLanding(float f)
    {
        Point3d point3d = elLoc.getPoint();
        if(radius > 2D * (double)rmax)
            if(submaneuver != 69)
                initTargPoint(f);
            else
                return;
        int i = 0;
        do
        {
            if(i >= 32)
                break;
            Po.set(((Tuple3d) (Vtarg)).x + radius * Math.sin(phase), ((Tuple3d) (Vtarg)).y + radius * Math.cos(phase), ((Tuple3d) (super.Loc)).z);
            radius += 0.01D * (double)rmax;
            phase += 0.29999999999999999D;
            Ve.sub(Po, super.Loc);
            double d = Ve.length();
            super.Or.transformInv(Ve);
            Ve.normalize();
            float f1 = 0.9F - 0.005F * Alt;
            if(f1 < -1F)
                f1 = -1F;
            if(f1 > 0.8F)
                f1 = 0.8F;
            float f2 = 1.5F - 0.0005F * Alt;
            if(f2 < 1.0F)
                f2 = 1.0F;
            if(f2 > 1.5F)
                f2 = 1.5F;
            if((double)rmax > d && d > (double)(rmin * f2) && ((Tuple3d) (Ve)).x > (double)f1 && isConvenientPoint())
            {
                submaneuver = 69;
                point3d.set(Po);
                pointQuality = curPointQuality;
                break;
            }
            i++;
        } while(true);
    }

    private boolean isConvenientPoint()
    {
        Po.z = Engine.cur.land.HQ_Air(((Tuple3d) (Po)).x, ((Tuple3d) (Po)).y);
        curPointQuality = 50;
        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                if(Engine.cur == null);
                if(Engine.land().isWater(((Tuple3d) (Po)).x + 500D * (double)i, ((Tuple3d) (Po)).y + 500D * (double)j))
                {
                    if(!(super.actor instanceof TypeSailPlane))
                        curPointQuality--;
                } else
                if(super.actor instanceof TypeSailPlane)
                    curPointQuality--;
                if(Engine.cur.land.HQ_ForestHeightHere(((Tuple3d) (Po)).x + 500D * (double)i, ((Tuple3d) (Po)).y + 500D * (double)j) > 1.0D)
                    curPointQuality -= 2;
                if(Engine.cur.land.EQN(((Tuple3d) (Po)).x + 500D * (double)i, ((Tuple3d) (Po)).y + 500D * (double)j, Ve) > 1110.949951171875D)
                    curPointQuality -= 2;
            }

        }

        if(curPointQuality < 0)
            curPointQuality = 0;
        return curPointQuality > pointQuality;
    }

    private void emergencyTurnToDirection(float f)
    {
        if(Math.abs(((Tuple3d) (Ve)).y) > 0.10000000149011612D)
            super.CT.AileronControl = -(float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).z) - 0.016F * super.Or.getKren();
        else
            super.CT.AileronControl = -(float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x) - 0.016F * super.Or.getKren();
        super.CT.RudderControl = -10F * (float)Math.atan2(((Tuple3d) (Ve)).y, ((Tuple3d) (Ve)).x);
        if((double)super.CT.RudderControl * ((Tuple3d) (super.W)).z > 0.0D)
            super.W.z = 0.0D;
        else
            super.W.z *= 1.0399999618530273D;
    }

    private void initTargPoint(float f)
    {
        int i = super.AP.way.Cur();
        Vtarg.set(super.AP.way.last().x(), super.AP.way.last().y(), 0.0D);
        super.AP.way.setCur(i);
        Vtarg.sub(super.Loc);
        Vtarg.z = 0.0D;
        if(Vtarg.length() > (double)rmax)
        {
            Vtarg.normalize();
            Vtarg.scale(rmax);
        }
        Vtarg.add(super.Loc);
        phase = 0.0D;
        radius = 50D;
        pointQuality = -1;
    }

    private void emergencyLanding(float f)
    {
        if(first)
        {
            Kmax = super.Wing.new_Cya(5F) / super.Wing.new_Cxa(5F);
            if(Kmax > 14F)
                Kmax = 14F;
            Kmax *= 0.8F;
            rmax = 1.2F * Kmax * Alt;
            rmin = 0.6F * Kmax * Alt;
            initTargPoint(f);
            setReadyToDieSoftly(true);
            super.AP.setStabAll(false);
            if(TaxiMode)
            {
                setSpeedMode(8);
                smConstPower = 0.0F;
                push(44);
                pop();
            }
            dist = Alt;
            if(super.actor instanceof TypeSailPlane)
                super.EI.setEngineStops();
        }
        setSpeedMode(4);
        smConstSpeed = super.VminFLAPS * 1.25F;
        if(Alt < 500F && ((super.actor instanceof TypeGlider) || (super.actor instanceof TypeSailPlane)))
            super.CT.GearControl = 1.0F;
        if(Alt < 10F)
        {
            super.CT.AileronControl = -0.04F * super.Or.getKren();
            setSpeedMode(4);
            smConstSpeed = super.VminFLAPS * 1.1F;
            if(Alt < 5F)
                setSpeedMode(8);
            super.CT.BrakeControl = 0.2F;
            if(super.Vwld.length() < 0.30000001192092896D && World.Rnd().nextInt(0, 99) < 4)
            {
                setStationedOnGround(true);
                super.actor = super.actor;
                World.cur();
                if(super.actor != World.getPlayerAircraft())
                {
                    push(44);
                    safe_pop();
                    if(super.actor instanceof TypeSailPlane)
                    {
                        super.EI.setCurControlAll(true);
                        super.EI.setEngineStops();
                        if(Engine.land().isWater(((Tuple3d) (super.Loc)).x, ((Tuple3d) (super.Loc)).y))
                            return;
                    }
                    ((Aircraft)super.actor).hitDaSilk();
                }
                if(Group != null)
                    Group.delAircraft((Aircraft)super.actor);
                if((super.actor instanceof TypeGlider) || (super.actor instanceof TypeSailPlane))
                    return;
                Actor actor = super.actor;
                World.cur();
                if(actor != World.getPlayerAircraft())
                    if(Airport.distToNearestAirport(super.Loc) > 900D)
                        ((Aircraft)super.actor).postEndAction(60D, super.actor, 4, null);
                    else
                        MsgDestroy.Post(Time.current() + 30000L, super.actor);
            }
        }
        dA = 0.2F * (getSpeed() - super.Vmin * 1.3F) - 0.8F * (getAOA() - 5F);
        if(Alt < 40F)
        {
            super.CT.AileronControl = -0.04F * super.Or.getKren();
            super.CT.RudderControl = 0.0F;
            if((super.actor instanceof BI_1) || (super.actor instanceof ME_163B1A))
                super.CT.GearControl = 1.0F;
            float f1;
            if(super.Gears.Pitch < 10F)
                f1 = (40F * (getSpeed() - super.VminFLAPS * 1.15F) - 60F * (super.Or.getTangage() + 3F) - 240F * (getVertSpeed() + 1.0F) - 120F * ((float)((Tuple3d) (getAccel())).z - 1.0F)) * 0.004F;
            else
                f1 = (40F * (getSpeed() - super.VminFLAPS * 1.15F) - 60F * ((super.Or.getTangage() - super.Gears.Pitch) + 10F) - 240F * (getVertSpeed() + 1.0F) - 120F * ((float)((Tuple3d) (getAccel())).z - 1.0F)) * 0.004F;
            if(Alt > 0.0F)
            {
                float f2 = 0.01666F * Alt;
                dA = dA * f2 + f1 * (1.0F - f2);
            } else
            {
                dA = f1;
            }
            super.CT.FlapsControl = 0.33F;
            if(Alt < 9F && Math.abs(super.Or.getKren()) < 5F && getVertSpeed() < -0.7F)
                super.Vwld.z *= 0.87000000476837158D;
        } else
        {
            rmax = 1.2F * Kmax * Alt;
            rmin = 0.6F * Kmax * Alt;
            if((super.actor instanceof TypeGlider) && Alt > 200F)
                super.CT.RudderControl = 0.0F;
            else
            if(pointQuality < 50 && mn_time > 0.5F)
                findPointForEmLanding(f);
            if(submaneuver == 69)
            {
                Ve.sub(elLoc.getPoint(), super.Loc);
                double d = Ve.length();
                super.Or.transformInv(Ve);
                Ve.normalize();
                float f3 = 0.9F - 0.005F * Alt;
                if(f3 < -1F)
                    f3 = -1F;
                if(f3 > 0.8F)
                    f3 = 0.8F;
                if((double)(rmax * 2.0F) < d || d < (double)rmin || ((Tuple3d) (Ve)).x < (double)f3)
                {
                    submaneuver = 0;
                    initTargPoint(f);
                }
                if(d > 88D)
                {
                    emergencyTurnToDirection(f);
                    if((double)Alt > d)
                        submaneuver = 0;
                } else
                {
                    super.CT.AileronControl = -0.04F * super.Or.getKren();
                }
            } else
            {
                super.CT.AileronControl = -0.04F * super.Or.getKren();
            }
            if(super.Or.getTangage() > -1F)
                dA -= 0.1F * (super.Or.getTangage() + 1.0F);
            if(super.Or.getTangage() < -10F)
                dA -= 0.1F * (super.Or.getTangage() + 10F);
        }
        if(Alt < 2.0F || super.Gears.onGround())
            dA = -2F * (super.Or.getTangage() - super.Gears.Pitch);
        double d1 = super.Vwld.length() / (double)super.Vmin;
        if(d1 > 1.0D)
            d1 = 1.0D;
        super.CT.ElevatorControl += (double)((dA - super.CT.ElevatorControl) * 3.33F * f) + 1.5D * ((Tuple3d) (getW())).y * d1 + 0.5D * ((Tuple3d) (getAW())).y * d1;
    }

    public boolean canITakeoff()
    {
        Po.set(super.Loc);
        Vpl.set(69D, 0.0D, 0.0D);
        super.Or.transform(Vpl);
        Po.add(Vpl);
        Pd.set(Po);
        if(super.actor != War.getNearestFriendAtPoint(Pd, (Aircraft)super.actor, 70F))
            return false;
        if(canTakeoff)
            return true;
        if(Actor.isAlive(super.AP.way.takeoffAirport))
        {
            if(super.AP.way.takeoffAirport.takeoffRequest <= 0)
            {
                super.AP.way.takeoffAirport.takeoffRequest = 2000;
                canTakeoff = true;
                return true;
            } else
            {
                return false;
            }
        } else
        {
            return true;
        }
    }

    public void set_maneuver_imm(int i)
    {
        int j = maneuver;
        maneuver = i;
        if(j != maneuver)
            set_flags();
    }

    private void addWindCorrection()
    {
label0:
        {
            if(World.cur().diffCur.Wind_N_Turbulence)
            {
                World.cur();
                if(!World.wind().noWind)
                    break label0;
            }
            return;
        }
        double d = Ve.length();
        World.cur();
        World.wind().getVectorAI(super.actor.pos.getAbsPoint(), windV);
        windV.scale(-1D);
        Ve.normalize();
        Ve.scale(getSpeed());
        Ve.add(windV);
        Ve.normalize();
        Ve.scale(d);
    }
}