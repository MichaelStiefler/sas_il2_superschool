/*4.10.1 class*/
package com.maddox.il2.objects.ships;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Line2d;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Tuple3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Aimer;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.Front.Marker;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.StrengthProperties;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.ai.air.CellObject;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.HunterInterface;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorDraw;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorMeshDraw;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Eff3DActor;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.InterpolateAdapter;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.VisibilityLong;
import com.maddox.il2.fm.FlightModel;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.game.ZutiSupportMethods_ResourcesManagement;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.ships.Ship.RwyTransp;
import com.maddox.il2.objects.ships.Ship.RwyTranspSqr;
import com.maddox.il2.objects.ships.Ship.RwyTranspWide;
import com.maddox.il2.objects.weapons.CannonMidrangeGeneric;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.ObjIO;
import com.maddox.rts.ObjState;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class BigshipGeneric extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Predator, ActorAlign, HunterInterface,
		VisibilityLong
{
	private static final int NETSEND_MAX_NUMITEMS_DMG = 14;
	private static final int NETSEND_MIN_DELAY_MS_PARTSSTATE = 650;
    private static final int NETSEND_MAX_DELAY_MS_PARTSSTATE = 1100;
	public float						CURRSPEED;
	public boolean						isTurning;
	public boolean						isTurningBackward;
	public boolean						mustRecomputePath;
	public boolean						mustSendSpeedToNet;
	private final int					REQUEST_LOC			= 93;

	private ShipProperties				prop;
	private long						netsendPartsState_lasttimeMS;
	private boolean						netsendPartsState_needtosend;
	private static float				netsendDrown_pitch	= 0.0F;
	private static float				netsendDrown_roll	= 0.0F;
	private static float				netsendDrown_depth	= 0.0F;
	private static float				netsendDrown_timeS	= 0.0F;
	private static int					netsendDrown_nparts	= 0;
	private long						netsendFire_lasttimeMS;
	private int							netsendFire_armindex;
	private static TmpTrackOrFireInfo[]	netsendFire_tmpbuff;
	private FiringDevice[]				arms;
	private Part[]						parts;
	private int[]						shotpoints;
	int									numshotpoints;
	private long						netsendDmg_lasttimeMS;
	private int							netsendDmg_partindex;
	private ArrayList					path;
	private int							cachedSeg;
	private float						bodyDepth;
	private float						bodyPitch;
	private float						bodyRoll;
	private float						shipYaw;
	private long						tmInterpoStart;
	private long						tmInterpoEnd;
	private float						bodyDepth0;
	private float						bodyPitch0;
	private float						bodyRoll0;
	private float						bodyDepth1;
	private float						bodyPitch1;
	private float						bodyRoll1;
	private long						timeOfDeath;
	private long						sink2timeWhenStop;
	private float						sink2Depth;
	private float						sink2Pitch;
	private float						sink2Roll;
	private int							dying;
	static final int					DYING_NONE			= 0;
	static final int					DYING_SINK1			= 1;
	static final int					DYING_SINK2			= 2;
	static final int					DYING_DEAD			= 3;
	private long						respawnDelay;
	private long						wakeupTmr;
	public float						DELAY_WAKEUP;
	public int							SKILL_IDX;
	public float						SLOWFIRE_K;
	private Pipe[]						pipes;
	private Pipe[]						dsmoks;
	private Eff3DActor[]				wake;
	private Eff3DActor					noseW;
	private Eff3DActor					nose;
	private Eff3DActor					tail;
	private static ShipProperties		constr_arg1			= null;
	private static ActorSpawnArg		constr_arg2			= null;
	private static Point3d				p					= new Point3d();
	private static Point3d				p1					= new Point3d();
	private static Point3d				p2					= new Point3d();
	private Orient						o;
	private static Vector3d				tmpvd				= new Vector3d();
	private static float[]				tmpYPR				= new float[3];
	private static Loc					tmpL				= new Loc();
	private float						rollAmp;
	private int							rollPeriod;
	private double						rollWAmp;
	private float						pitchAmp;
	private int							pitchPeriod;
	private double						pitchWAmp;
	private Vector3d					W;
	private Vector3d					N;
	private Vector3d					tmpV;
	public Orient						initOr;
	public Loc							initLoc;
	private AirportCarrier				airport;
	private CellAirField				cellTO;
	private CellAirField				cellLDG;
	public Aircraft						towAircraft;
	public int							towPortNum;
	public HookNamed					towHook;
	private static Vector3d				tmpDir				= new Vector3d();

	public static class ShipPartProperties
	{
		public String				baseChunkName				= null;
		public int					baseChunkIdx				= -1;
		public Point3f				partOffs					= null;
		public float				partR						= 1.0F;
		public String[]				additCollisChunkName		= null;
		public int[]				additCollisChunkIdx			= null;
		public StrengthProperties	stre						= new StrengthProperties();
		public float				dmgDepth					= -1.0F;
		public float				dmgPitch					= 0.0F;
		public float				dmgRoll						= 0.0F;
		public float				dmgTime						= 1.0F;
		public float				BLACK_DAMAGE				= 0.0F;
		public int					gun_idx;
		public Class				gunClass					= null;
		public int					WEAPONS_MASK				= 4;
		public boolean				TRACKING_ONLY				= false;
		public float				ATTACK_MAX_DISTANCE			= 1.0F;
		public float				ATTACK_MAX_RADIUS			= 1.0F;
		public float				ATTACK_MAX_HEIGHT			= 1.0F;
		public int					ATTACK_FAST_TARGETS			= 1;
		public float				FAST_TARGETS_ANGLE_ERROR	= 0.0F;
		public AnglesRange			HEAD_YAW_RANGE				= new AnglesRange(-1.0F, 1.0F);
		public float				HEAD_STD_YAW				= 0.0F;
		public float				_HEAD_MIN_YAW				= -1.0F;
		public float				_HEAD_MAX_YAW				= -1.0F;
		public float				GUN_MIN_PITCH				= -20.0F;
		public float				GUN_STD_PITCH				= -18.0F;
		public float				GUN_MAX_PITCH				= -15.0F;
		public float				HEAD_MAX_YAW_SPEED			= 720.0F;
		public float				GUN_MAX_PITCH_SPEED			= 60.0F;
		public float				DELAY_AFTER_SHOOT			= 1.0F;
		public float				CHAINFIRE_TIME				= 0.0F;
		public String				headChunkName				= null;
		public String				gunChunkName				= null;
		public int					headChunkIdx				= -1;
		public int					gunChunkIdx					= -1;
		public Point3d				fireOffset;
		public Orient				fireOrient;
		public String				gunShellStartHookName		= null;

		public boolean isItLifeKeeper()
		{
			return dmgDepth >= 0.0F;
		}

		public boolean haveGun()
		{
			return gun_idx >= 0;
		}
	}

	public static class ShipProperties
	{
		public String				meshName			= null;
		public String				soundName			= null;
		public int					WEAPONS_MASK		= 4;
		public int					HITBY_MASK			= -2;
		public float				ATTACK_MAX_DISTANCE	= 1.0F;
		public float				SLIDER_DIST			= 1.0F;
		public float				SPEED				= 1.0F;
		public float				DELAY_RESPAWN_MIN	= 15.0F;
		public float				DELAY_RESPAWN_MAX	= 30.0F;
		public ShipPartProperties[]	propparts			= null;
		public int					nGuns;
		public AirportProperties	propAirport			= null;
	}

	public static class TmpTrackOrFireInfo
	{
		private int		gun_idx;
		private Actor	enemy;
		private double	timeWhenFireS;
		private int		shotpointIdx;
	}

	public static class FiringDevice
	{
		private int		gun_idx;
		private int		part_idx;
		private Gun		gun;
		private Aim		aime;
		private float	headYaw;
		private float	gunPitch;
		private Actor	enemy;
		private double	timeWhenFireS;
		private int		shotpointIdx;
	}

	public static class Part
	{
		private float		damage;
		private Actor		mirror_initiator;
		private Point3d		shotpointOffs		= new Point3d();
		private boolean		damageIsFromRight	= false;
		private int			state;
		ShipPartProperties	pro;

//		/* synthetic */static float access$2316(Part part, float f)
//		{
//			return part.damage += f;
//		}
	}

	private static class Segment
	{
		public Point3d	posIn;
		public Point3d	posOut;
		public float	length;
		public long		timeIn;
		public long		timeOut;
		public float	speedIn;
		public float	speedOut;

		private Segment()
		{
			/* empty */
		}
	}

	public static class Pipe
	{
		private Eff3DActor	pipe		= null;
		private int			part_idx	= -1;
	}

	static class HookNamedZ0 extends HookNamed
	{
		public void computePos(Actor actor, Loc loc, Loc loc_0_)
		{
			super.computePos(actor, loc, loc_0_);
			loc_0_.getPoint().z = 0.25;
		}

		public HookNamedZ0(ActorMesh actormesh, String string)
		{
			super(actormesh, string);
		}

		public HookNamedZ0(Mesh mesh, String string)
		{
			super(mesh, string);
		}
	}

	class Move extends Interpolate
	{
		public boolean tick()
		{
			BigshipGeneric.this.validateTowAircraft();

			// TODO: Added by |ZUTI|: second check
			// dying == 0 -> undamaged
			if (dying == 0)
			{
				long l = Time.tickNext();
				// TODO: Edited by |ZUTI|
				// if (Mission.isCoop())
				if (Mission.isCoop() || Mission.isDogfight())
					l = (NetServerParams.getServerTime());

				if (path != null)
				{
					BigshipGeneric.this.computeInterpolatedDPR(l);
					BigshipGeneric.this.setMovablePosition(l);
				}
				else if (BigshipGeneric.this.computeInterpolatedDPR(l))
					BigshipGeneric.this.setPosition();
				boolean bool = false;
				if (wakeupTmr == 0L)
				{
					for (int i = 0; i < prop.nGuns; i++)
					{
						if (parts[arms[i].part_idx].state == 0)
						{
							arms[i].aime.tick_();
							bool = true;
						}
					}
				}
				else
				{
					for (int i = 0; i < prop.nGuns; i++)
					{
						if (parts[arms[i].part_idx].state == 0)
						{
							bool = true;
							break;
						}
					}
					if (wakeupTmr > 0L)
					    BigshipGeneric.this.wakeupTmr--;
//						access$3810(BigshipGeneric.this);
					else if (++BigshipGeneric.this.wakeupTmr == 0L)
					{
						if (BigshipGeneric.this.isAnyEnemyNear())
							wakeupTmr = SecsToTicks(Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
						else
							wakeupTmr = -SecsToTicks(Rnd(4.0F, 7.0F));
					}
				}
				if (bool)
					BigshipGeneric.this.send_bufferized_FireCommand();
				if (isNetMirror())
				{
					mirror_send_bufferized_Damage();

					if (com.maddox.il2.game.Mission.isCoop() && mustSendSpeedToNet)
					{
						mirror_send_speed();
						mustSendSpeedToNet = false;
					}
				}
				else if (netsendPartsState_needtosend)
					send_bufferized_PartsState();

				// TODO: Added by |ZUTI|
				// ---------------------------------------------
				// refresh born place and stay point locations
				ZutiSupportMethods_Ships.refreshShipsBornPlacePosition(BigshipGeneric.this);
				// Refresh front marker
				ZutiSupportMethods_Ships.refreshShipsFrontMarkerPosition(BigshipGeneric.this, false);
				// ------------------------------------------

				return true;
			}
			// dying 3 -> sunk
			if (dying == 3)
			{
				// TODO: Added by |ZUTI|
				// ---------------------------------------------
				// refresh born place and stay point locations
				ZutiSupportMethods_Ships.refreshShipsBornPlacePosition(BigshipGeneric.this);
				// Refresh front marker
				ZutiSupportMethods_Ships.refreshShipsFrontMarkerPosition(BigshipGeneric.this, true);
				// disable guns when ship is sinking/sunk
                //TODO: Implement 4.10.1 Codechanges +++
				if (path != null || !Mission.isDeathmatch())
				//if (path != null)
                //TODO: Implement 4.10.1 Codechanges ---
				{
					BigshipGeneric.this.eraseGuns();
					return false;
				}
				// ---------------------------------------------

				if (BigshipGeneric.this.respawnDelay-- > 0L)
					return true;
				if (!BigshipGeneric.this.isNetMaster())
				{
					respawnDelay = 10000L;
					return true;
				}
				wakeupTmr = 0L;
				BigshipGeneric.this.makeLive();
				BigshipGeneric.this.forgetAllAiming();
				BigshipGeneric.this.setDefaultLivePose();
				BigshipGeneric.this.setDiedFlag(false);
				tmInterpoStart = tmInterpoEnd = 0L;
				bodyDepth = bodyPitch = bodyRoll = 0.0F;
				bodyDepth0 = bodyPitch0 = bodyRoll0 = 0.0F;
				bodyDepth1 = bodyPitch1 = bodyRoll1 = 0.0F;
				// TODO: Edit by |ZUTI|
				BigshipGeneric.this.setPosition();
				// BigshipGeneric.this.setMovablePosition(NetServerParams.getServerTime());
				pos.reset();
				BigshipGeneric.this.send_RespawnCommand();
				return true;
			}
			if (netsendPartsState_needtosend)
				BigshipGeneric.this.send_bufferized_PartsState();
			// TODO: Edited by |ZUTI|
			// ----------------------
			// long l = Time.tickNext();
			long l = NetServerParams.getServerTime();
			// ----------------------
			if (dying == 1)
			{
				if (l >= tmInterpoEnd)
				{
					bodyDepth0 = bodyDepth1;
					bodyPitch0 = bodyPitch1;
					bodyRoll0 = bodyRoll1;
					bodyDepth1 = sink2Depth;
					bodyPitch1 = sink2Pitch;
					bodyRoll1 = sink2Roll;
					tmInterpoStart = tmInterpoEnd;
					tmInterpoEnd = sink2timeWhenStop;
					dying = 2;
				}
			}
			else if (l >= tmInterpoEnd)
			{
				bodyDepth0 = bodyDepth1 = sink2Depth;
				bodyPitch0 = bodyPitch1 = sink2Pitch;
				bodyRoll0 = bodyRoll1 = sink2Roll;
				tmInterpoStart = tmInterpoEnd = 0L;
				dying = 3;
			}
			if ((Time.tickCounter() & 0x63) == 0 && dsmoks != null)
			{
				for (int i = 0; i < dsmoks.length; i++)
				{
					if (dsmoks[i] != null && dsmoks[i].pipe != null && dsmoks[i].pipe.pos.getAbsPoint().z < -4.891)
					{
						Eff3DActor.finish(dsmoks[i].pipe);
						dsmoks[i].pipe = null;
					}
				}
			}
			BigshipGeneric.this.computeInterpolatedDPR(l);
			if (path != null)
				BigshipGeneric.this.setMovablePosition(timeOfDeath);
			else
				BigshipGeneric.this.setPosition();

			// TODO: Added by |ZUTI|
			// --------------------------------------------------
			// refresh born place and stay point locations
			ZutiSupportMethods_Ships.refreshShipsBornPlacePosition(BigshipGeneric.this);
			// Refresh front marker
			ZutiSupportMethods_Ships.refreshShipsFrontMarkerPosition(BigshipGeneric.this, false);
			// --------------------------------------------------

			return true;
		}
	}

	class Master extends ActorNet
	{
		public boolean netInput(NetMsgInput netmsginput) throws IOException
		{
			if (netmsginput.isGuaranted())
			{
				int i = netmsginput.readUnsignedByte();
//				java.lang.System.out.println("MASTER NETINPUT: " + i);
				if (i == 93)
				{
					com.maddox.il2.net.NetUser netuser = (com.maddox.il2.net.NetUser) netmsginput.readNetObj();
					java.lang.String s = netmsginput.readUTF();
					handleLocationRequest(netuser, s);
					return true;
				}
				if (i != 86)
					return false;
				i = netmsginput.readUnsignedByte();
				float f = i;
				if (path != null && i != 127 && f < CURRSPEED)
				{
					CURRSPEED = f;
					if (com.maddox.il2.game.Mission.isCoop())
					{
						computeNewPath();
						netsendPartsState_needtosend = true;
					}
				}

				return true;
			}
			if (netmsginput.readUnsignedByte() != 80)
				return false;
			if (dying != 0)
				return true;
			int i = 2;
			if (netmsginput != null)
			{
				/* empty */
			}
			int i_1_ = i + NetMsgInput.netObjReferenceLen();
			int i_2_ = netmsginput.available();
			int i_3_ = i_2_ / i_1_;
			if (i_3_ <= 0 || i_3_ > 256 || i_2_ % i_1_ != 0)
			{
				System.out.println("*** net bigship1 len:" + i_2_);
				return true;
			}
			while (--i_3_ >= 0)
			{
				int i_4_ = netmsginput.readUnsignedByte();
				if (i_4_ < 0 || i_4_ >= parts.length)
					return true;
				int i_5_ = netmsginput.readUnsignedByte();
				com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
				Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
				if (parts[i_4_].state != 2)
				{
				    parts[i_4_].damage += (float) ((i_5_ & 0x7f) + 1) / 128.0F;
//					Part.access$2316(parts[i_4_], (float) ((i_5_ & 0x7f) + 1) / 128.0F);
					parts[i_4_].damageIsFromRight = (i_5_ & 0x80) != 0;
					BigshipGeneric.this.InjurePart(i_4_, actor, true);
				}
			}
			return true;
		}

		public Master(Actor actor)
		{
			super(actor);
		}
	}

	class Mirror extends ActorNet
	{
		NetMsgFiltered	out	= new NetMsgFiltered();

		public boolean netInput(NetMsgInput netmsginput) throws IOException
		{
			if (netmsginput.isGuaranted())
			{
				switch (netmsginput.readUnsignedByte())
				{
					case REQUEST_LOC:
						double d = netmsginput.readDouble();
						double d1 = netmsginput.readDouble();
						double d2 = netmsginput.readDouble();
						float f1 = netmsginput.readFloat();
						float f2 = netmsginput.readFloat();
						float f3 = netmsginput.readFloat();
						com.maddox.il2.engine.Loc loc = new Loc(d, d1, d2, f1, f2, f3);
						if (airport != null)
							airport.setClientLoc(loc);
						return true;
					case 73:
						if (isMirrored())
						{
							NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
							post(netmsgguaranted);
						}
						timeOfDeath = netmsginput.readLong();
						if (timeOfDeath < 0L)
						{
							if (dying == 0)
							{
								BigshipGeneric.this.makeLive();
								BigshipGeneric.this.setDefaultLivePose();
								BigshipGeneric.this.forgetAllAiming();
							}
						}
						else if (dying == 0)
							BigshipGeneric.this.Die(null, timeOfDeath, false, true);
						return true;
					case 82:
						if (isMirrored())
						{
							NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
							post(netmsgguaranted);
						}
						BigshipGeneric.this.makeLive();
						BigshipGeneric.this.setDefaultLivePose();
						BigshipGeneric.this.forgetAllAiming();
						BigshipGeneric.this.setDiedFlag(false);
						tmInterpoStart = tmInterpoEnd = 0L;
						bodyDepth = bodyPitch = bodyRoll = 0.0F;
						bodyDepth0 = bodyPitch0 = bodyRoll0 = 0.0F;
						bodyDepth1 = bodyPitch1 = bodyRoll1 = 0.0F;
						BigshipGeneric.this.setPosition();
						pos.reset();
						return true;
					case 83:
					{
						if (isMirrored())
						{
							NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
							post(netmsgguaranted);
						}
						int i = netmsginput.available();

						if (i > 0 && !com.maddox.il2.game.Mission.isDogfight())
						{
							int l2 = netmsginput.readUnsignedByte();
							float f = l2;
							if (path != null && l2 != 127 && f < CURRSPEED)
							{
								CURRSPEED = f;
								computeNewPath();
							}
							i--;
						}

						int i_6_ = (parts.length + 3) / 4;
						if (i != i_6_)
						{
							System.out.println("*** net bigship S");
							return true;
						}
						if (i_6_ <= 0)
						{
							System.out.println("*** net bigship S0");
							return true;
						}
						int i_7_ = 0;
						for (int i_8_ = 0; i_8_ < i; i_8_++)
						{
							int i_9_ = netmsginput.readUnsignedByte();
							for (int i_10_ = 0; i_10_ < 4 && i_7_ < parts.length; i_10_++)
							{
								int i_11_ = i_9_ >>> i_10_ * 2 & 0x3;
								if (i_11_ <= parts[i_7_].state)
									i_7_++;
								else
								{
									if (i_11_ == 2)
									{
										parts[i_7_].damage = 0.0F;
										parts[i_7_].mirror_initiator = null;
									}
									parts[i_7_].state = i_11_;
									BigshipGeneric.this.visualsInjurePart(i_7_, true);
									i_7_++;
								}
							}
						}
						return true;
					}
					case 100:
					{
						if (isMirrored())
						{
							NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
							post(netmsgguaranted);
						}
						int i = netmsginput.available();
						if (i != 8)
						{
							System.out.println("*** net bigship d");
							return true;
						}
						if (dying != 0)
							return true;
						BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
						bodyDepth0 = bodyDepth;
						bodyPitch0 = bodyPitch;
						bodyRoll0 = bodyRoll;
						bodyDepth1 = (float) (1000.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0));
						bodyPitch1 = (float) (90.0 * ((double) netmsginput.readShort() / 32767.0));
						bodyRoll1 = (float) (90.0 * ((double) netmsginput.readShort() / 32767.0));
						tmInterpoStart = tmInterpoEnd = NetServerParams.getServerTime();
						BigshipGeneric.this.tmInterpoEnd += (long) (1000.0 * (1200.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0)));
//						access$1014(BigshipGeneric.this, (long) (1000.0 * (1200.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0))));
						BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
						return true;
					}
					case 68:
					{
						if (isMirrored())
						{
							NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 1);
							post(netmsgguaranted);
						}
						int i = netmsginput.available();
						int i_12_ = i;
						int i_13_ = 8;
						if (netmsginput != null)
						{
							/* empty */
						}
						boolean bool;
						if (i_12_ == i_13_ + NetMsgInput.netObjReferenceLen() + 8 + 8)
							bool = false;
						else
						{
							int i_14_ = i;
							int i_15_ = 8;
							if (netmsginput != null)
							{
								/* empty */
							}
							if (i_14_ == (i_15_ + NetMsgInput.netObjReferenceLen() + 8 + 8 + 8))
								bool = true;
							else
							{
								System.out.println("*** net bigship D");
								return true;
							}
						}
						if (dying != 0)
							return true;
						timeOfDeath = netmsginput.readLong();
						if (Mission.isDeathmatch())
							timeOfDeath = NetServerParams.getServerTime();
						if (timeOfDeath < 0L)
						{
							System.out.println("*** net bigship D tm");
							return true;
						}
						com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
						Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
						BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
						bodyDepth0 = bodyDepth;
						bodyPitch0 = bodyPitch;
						bodyRoll0 = bodyRoll;
						bodyDepth1 = (float) (1000.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0));
						bodyPitch1 = (float) (90.0 * ((double) netmsginput.readShort() / 32767.0));
						bodyRoll1 = (float) (90.0 * ((double) netmsginput.readShort() / 32767.0));
						tmInterpoStart = tmInterpoEnd = NetServerParams.getServerTime();
                        BigshipGeneric.this.tmInterpoEnd += (long) (1000.0 * (1200.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0)));
//						access$1014(BigshipGeneric.this, (long) (1000.0 * (1200.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0))));
						BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
						sink2Depth = (float) (1000.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0));
						sink2Pitch = (float) (90.0 * ((double) netmsginput.readShort() / 32767.0));
						sink2Roll = (float) (90.0 * ((double) netmsginput.readShort() / 32767.0));
						sink2timeWhenStop = tmInterpoEnd;
                        BigshipGeneric.this.tmInterpoEnd += (long) (1000.0 * (1200.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0)));
//						access$3014(BigshipGeneric.this, (long) (1000.0 * (1200.0 * ((double) (netmsginput.readUnsignedShort() & 0x7fff) / 32767.0))));
						if (bool)
						{
							long l = netmsginput.readLong();
							if (l > 0L)
							{
							    BigshipGeneric.this.tmInterpoStart -= l;
                                BigshipGeneric.this.tmInterpoEnd -= l;
                                BigshipGeneric.this.sink2timeWhenStop -= l;
//								access$922(BigshipGeneric.this, l);
//								access$1022(BigshipGeneric.this, l);
//								access$3022(BigshipGeneric.this, l);
								BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
							}
						}
						BigshipGeneric.this.Die(actor, timeOfDeath, true, false);
						return true;
					}
					default:
						System.out.println("**net bigship unknown cmd");
						return false;
				}
			}
			int i = netmsginput.readUnsignedByte();
			if ((i & 0xe0) == 224)
			{
				int i_16_ = 1;
				if (netmsginput != null)
				{
					/* empty */
				}
				int i_17_ = i_16_ + NetMsgInput.netObjReferenceLen() + 1;
				int i_18_ = 2;
				if (netmsginput != null)
				{
					/* empty */
				}
				int i_19_ = i_18_ + NetMsgInput.netObjReferenceLen() + 1;
				int i_20_ = netmsginput.available();
				int i_21_ = i & 0x1f;
				int i_22_ = i_20_ - i_21_ * i_17_;
				int i_23_ = i_22_ / i_19_;
				if (i_23_ < 0 || i_23_ > 31 || i_21_ > 31 || i_22_ % i_19_ != 0)
				{
					System.out.println("*** net big0 code:" + i + " szT:" + i_17_ + " szF:" + i_19_ + " len:" + i_20_ + " nT:" + i_21_ + " lenF:" + i_22_ + " nF:" + i_23_);
					return true;
				}
				if (isMirrored())
				{
					out.unLockAndSet(netmsginput, i_21_ + i_23_);
					out.setIncludeTime(true);
					postReal(Message.currentRealTime(), out);
				}
				while (--i_21_ >= 0)
				{
					int i_24_ = netmsginput.readUnsignedByte();
					com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
					Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
					int i_25_ = netmsginput.readUnsignedByte();
					BigshipGeneric.this.Track_Mirror(i_24_, actor, i_25_);
				}
				while (--i_23_ >= 0)
				{
					int i_26_ = netmsginput.readUnsignedByte();
					int i_27_ = netmsginput.readUnsignedByte();
					com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
					Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
					double d = -2.0 + (double) i_26_ / 255.0 * 7000.0 / 1000.0;
					double d_28_ = ((0.0010 * (double) (Message.currentGameTime() - NetServerParams.getServerTime())) + d);
					int i_29_ = netmsginput.readUnsignedByte();
					BigshipGeneric.this.Fire_Mirror(i_27_, actor, i_29_, (float) d_28_);
				}
				return true;
			}
			if (i == 80)
			{
				int i_30_ = 2;
				if (netmsginput != null)
				{
					/* empty */
				}
				int i_31_ = i_30_ + NetMsgInput.netObjReferenceLen();
				int i_32_ = netmsginput.available();
				int i_33_ = i_32_ / i_31_;
				if (i_33_ <= 0 || i_33_ > 256 || i_32_ % i_31_ != 0)
				{
					System.out.println("*** net bigship2 n:" + i_33_);
					return true;
				}
				out.unLockAndSet(netmsginput, i_33_);
				out.setIncludeTime(false);
				postRealTo(Message.currentRealTime(), masterChannel(), out);
				return true;
			}
			System.out.println("**net bigship unknown ng cmd");
			return true;
		}

		public Mirror(Actor actor, NetChannel netchannel, int i)
		{
			super(actor, netchannel, i);
		}
	}

	public static class SPAWN implements ActorSpawn
	{
		public Class			cls;
		public ShipProperties	proper;

		private static float getF(SectFile sectfile, String string, String string_34_, float f, float f_35_)
		{
			float f_36_ = sectfile.get(string, string_34_, -9865.345F);
			if (f_36_ == -9865.345F || f_36_ < f || f_36_ > f_35_)
			{
				if (f_36_ == -9865.345F)
					System.out.println("Ship: Value of [" + string + "]:<" + string_34_ + "> " + "not found");
				else
					System.out.println("Ship: Value of [" + string + "]:<" + string_34_ + "> (" + f_36_ + ")" + " is out of range (" + f + ";" + f_35_ + ")");
				throw new RuntimeException("Can't set property");
			}
			return f_36_;
		}

		private static String getS(SectFile sectfile, String string, String string_37_)
		{
			String string_38_ = sectfile.get(string, string_37_);
			if (string_38_ == null || string_38_.length() <= 0)
			{
				System.out.print("Ship: Value of [" + string + "]:<" + string_37_ + "> not found");
				throw new RuntimeException("Can't set property");
			}
			return new String(string_38_);
		}

		private static void tryToReadGunProperties(SectFile sectfile, String string, ShipPartProperties shippartproperties)
		{
			if (sectfile.exist(string, "Gun"))
			{
				String string_42_ = ("com.maddox.il2.objects.weapons." + getS(sectfile, string, "Gun"));
				try
				{
					shippartproperties.gunClass = Class.forName(string_42_);
				}
				catch (Exception exception)
				{
					System.out.println("BigShip: Can't find gun class '" + string_42_ + "'");
					throw new RuntimeException("Can't register Ship object");
				}
			}
			if (sectfile.exist(string, "AttackMaxDistance"))
				shippartproperties.ATTACK_MAX_DISTANCE = getF(sectfile, string, "AttackMaxDistance", 6.0F, 50000.0F);
			if (sectfile.exist(string, "AttackMaxRadius"))
				shippartproperties.ATTACK_MAX_RADIUS = getF(sectfile, string, "AttackMaxRadius", 6.0F, 50000.0F);
			if (sectfile.exist(string, "AttackMaxHeight"))
				shippartproperties.ATTACK_MAX_HEIGHT = getF(sectfile, string, "AttackMaxHeight", 6.0F, 15000.0F);
			if (sectfile.exist(string, "TrackingOnly"))
				shippartproperties.TRACKING_ONLY = true;
			if (sectfile.exist(string, "FireFastTargets"))
			{
				float f = getF(sectfile, string, "FireFastTargets", 0.0F, 2.0F);
				shippartproperties.ATTACK_FAST_TARGETS = (int) (f + 0.5F);
				if (shippartproperties.ATTACK_FAST_TARGETS > 2)
					shippartproperties.ATTACK_FAST_TARGETS = 2;
			}
			if (sectfile.exist(string, "FastTargetsAngleError"))
			{
				float f = getF(sectfile, string, "FastTargetsAngleError", 0.0F, 45.0F);
				shippartproperties.FAST_TARGETS_ANGLE_ERROR = f;
			}
			if (sectfile.exist(string, "HeadMinYaw"))
				shippartproperties._HEAD_MIN_YAW = getF(sectfile, string, "HeadMinYaw", -360.0F, 360.0F);
			if (sectfile.exist(string, "HeadMaxYaw"))
				shippartproperties._HEAD_MAX_YAW = getF(sectfile, string, "HeadMaxYaw", -360.0F, 360.0F);
			if (sectfile.exist(string, "GunMinPitch"))
				shippartproperties.GUN_MIN_PITCH = getF(sectfile, string, "GunMinPitch", -15.0F, 85.0F);
			if (sectfile.exist(string, "GunMaxPitch"))
				shippartproperties.GUN_MAX_PITCH = getF(sectfile, string, "GunMaxPitch", 0.0F, 89.9F);
			if (sectfile.exist(string, "HeadMaxYawSpeed"))
				shippartproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, string, "HeadMaxYawSpeed", 0.1F, 999.0F);
			if (sectfile.exist(string, "GunMaxPitchSpeed"))
				shippartproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, string, "GunMaxPitchSpeed", 0.1F, 999.0F);
			if (sectfile.exist(string, "DelayAfterShoot"))
				shippartproperties.DELAY_AFTER_SHOOT = getF(sectfile, string, "DelayAfterShoot", 0.0F, 999.0F);
			if (sectfile.exist(string, "ChainfireTime"))
				shippartproperties.CHAINFIRE_TIME = getF(sectfile, string, "ChainfireTime", 0.0F, 600.0F);
			if (sectfile.exist(string, "GunHeadChunk"))
				shippartproperties.headChunkName = getS(sectfile, string, "GunHeadChunk");
			if (sectfile.exist(string, "GunBarrelChunk"))
				shippartproperties.gunChunkName = getS(sectfile, string, "GunBarrelChunk");
			if (sectfile.exist(string, "GunShellStartHook"))
				shippartproperties.gunShellStartHookName = getS(sectfile, string, "GunShellStartHook");
		}

		private static ShipProperties LoadShipProperties(SectFile sectfile, String string, Class var_class)
		{
			ShipProperties shipproperties = new ShipProperties();
			shipproperties.meshName = getS(sectfile, string, "Mesh");
			shipproperties.soundName = getS(sectfile, string, "SoundMove");
			if (shipproperties.soundName.equalsIgnoreCase("none"))
				shipproperties.soundName = null;
			shipproperties.SLIDER_DIST = getF(sectfile, string, "SliderDistance", 5.0F, 1000.0F);
			shipproperties.SPEED = KmHourToMSec(getF(sectfile, string, "Speed", 0.5F, 200.0F));
			shipproperties.DELAY_RESPAWN_MIN = 15.0F;
			shipproperties.DELAY_RESPAWN_MAX = 30.0F;
			Property.set(var_class, "iconName", "icons/" + getS(sectfile, string, "Icon") + ".mat");
			Property.set(var_class, "meshName", shipproperties.meshName);
			Property.set(var_class, "speed", shipproperties.SPEED);
			int i;
			for (i = 0; sectfile.sectionIndex(string + ":Part" + i) >= 0; i++)
			{
				/* empty */
			}
			if (i <= 0)
			{
				System.out.println("BigShip: No part sections for '" + string + "'");
				throw new RuntimeException("Can't register BigShip object");
			}
			if (i >= 255)
			{
				System.out.println("BigShip: Too many parts in " + string + ".");
				throw new RuntimeException("Can't register BigShip object");
			}
			shipproperties.propparts = new ShipPartProperties[i];
			shipproperties.nGuns = 0;
			for (int i_43_ = 0; i_43_ < i; i_43_++)
			{
				String string_44_ = string + ":Part" + i_43_;
				ShipPartProperties shippartproperties = new ShipPartProperties();
				shipproperties.propparts[i_43_] = shippartproperties;
				shippartproperties.baseChunkName = getS(sectfile, string_44_, "BaseChunk");
				int i_45_;
				for (i_45_ = 0; sectfile.exist(string_44_, "AdditionalCollisionChunk" + i_45_); i_45_++)
				{
					/* empty */
				}
				if (i_45_ > 4)
				{
					System.out.println("BigShip: Too many addcollischunks in '" + string_44_ + "'");
					throw new RuntimeException("Can't register BigShip object");
				}
				shippartproperties.additCollisChunkName = new String[i_45_];
				for (int i_46_ = 0; i_46_ < i_45_; i_46_++)
					shippartproperties.additCollisChunkName[i_46_] = getS(sectfile, string_44_, "AdditionalCollisionChunk" + i_46_);
				String string_47_ = null;
				if (sectfile.exist(string_44_, "strengthBasedOnThisSection"))
					string_47_ = getS(sectfile, string_44_, "strengthBasedOnThisSection");
				if (!shippartproperties.stre.read("Bigship", sectfile, string_47_, string_44_))
					throw new RuntimeException("Can't register Bigship object");
				if (sectfile.exist(string_44_, "Vital"))
				{
					shippartproperties.dmgDepth = getF(sectfile, string_44_, "damageDepth", 0.0F, 99.0F);
					shippartproperties.dmgPitch = getF(sectfile, string_44_, "damagePitch", -89.0F, 89.0F);
					shippartproperties.dmgRoll = getF(sectfile, string_44_, "damageRoll", 0.0F, 89.0F);
					shippartproperties.dmgTime = getF(sectfile, string_44_, "damageTime", 1.0F, 1200.0F);
					shippartproperties.BLACK_DAMAGE = 0.6666667F;
				}
				else
				{
					shippartproperties.dmgDepth = -1.0F;
					shippartproperties.BLACK_DAMAGE = 1.0F;
				}
				if (!sectfile.exist(string_44_, "Gun") && !sectfile.exist(string_44_, "gunBasedOnThisSection"))
					shippartproperties.gun_idx = -1;
				else if (shippartproperties.isItLifeKeeper())
				{
					System.out.println("*** ERROR: bigship: vital with gun");
					shippartproperties.gun_idx = -1;
				}
				else
				{
					shippartproperties.gun_idx = shipproperties.nGuns++;
					if (shipproperties.nGuns > 256)
					{
						System.out.println("BigShip: Too many guns in " + string + ".");
						throw new RuntimeException("Can't register BigShip object");
					}
					shippartproperties.gunClass = null;
					shippartproperties.ATTACK_MAX_DISTANCE = -1000.0F;
					shippartproperties.ATTACK_MAX_RADIUS = -1000.0F;
					shippartproperties.ATTACK_MAX_HEIGHT = -1000.0F;
					shippartproperties.TRACKING_ONLY = false;
					shippartproperties.ATTACK_FAST_TARGETS = 1;
					shippartproperties.FAST_TARGETS_ANGLE_ERROR = 0.0F;
					shippartproperties._HEAD_MIN_YAW = -1000.0F;
					shippartproperties._HEAD_MAX_YAW = -1000.0F;
					shippartproperties.GUN_MIN_PITCH = -1000.0F;
					shippartproperties.GUN_STD_PITCH = -1000.0F;
					shippartproperties.GUN_MAX_PITCH = -1000.0F;
					shippartproperties.HEAD_MAX_YAW_SPEED = -1000.0F;
					shippartproperties.GUN_MAX_PITCH_SPEED = -1000.0F;
					shippartproperties.DELAY_AFTER_SHOOT = -1000.0F;
					shippartproperties.CHAINFIRE_TIME = -1000.0F;
					shippartproperties.headChunkName = null;
					shippartproperties.gunChunkName = null;
					shippartproperties.gunShellStartHookName = null;
					if (sectfile.exist(string_44_, "gunBasedOnThisSection"))
					{
						String string_48_ = getS(sectfile, string_44_, "gunBasedOnThisSection");
						tryToReadGunProperties(sectfile, string_48_, shippartproperties);
					}
					tryToReadGunProperties(sectfile, string_44_, shippartproperties);
					if (shippartproperties.gunClass == null || shippartproperties.ATTACK_MAX_DISTANCE <= -1000.0F || shippartproperties.ATTACK_MAX_RADIUS <= -1000.0F
							|| shippartproperties.ATTACK_MAX_HEIGHT <= -1000.0F || shippartproperties._HEAD_MIN_YAW <= -1000.0F || shippartproperties._HEAD_MAX_YAW <= -1000.0F
							|| shippartproperties.GUN_MIN_PITCH <= -1000.0F || shippartproperties.GUN_MAX_PITCH <= -1000.0F || shippartproperties.HEAD_MAX_YAW_SPEED <= -1000.0F
							|| shippartproperties.GUN_MAX_PITCH_SPEED <= -1000.0F || shippartproperties.DELAY_AFTER_SHOOT <= -1000.0F || shippartproperties.CHAINFIRE_TIME <= -1000.0F
							|| shippartproperties.headChunkName == null || shippartproperties.gunChunkName == null || shippartproperties.gunShellStartHookName == null)
					{
						System.out.println("BigShip: Not enough 'gun' data  in '" + string_44_ + "'");
						throw new RuntimeException("Can't register BigShip object");
					}
					shippartproperties.WEAPONS_MASK = (Gun.getProperties(shippartproperties.gunClass).weaponType);
					if (shippartproperties.WEAPONS_MASK == 0)
					{
						System.out.println("BigShip: Undefined weapon type in gun class '" + shippartproperties.gunClass.getName() + "'");
						throw new RuntimeException("Can't register BigShip object");
					}
					if (shippartproperties._HEAD_MIN_YAW > shippartproperties._HEAD_MAX_YAW)
					{
						System.out.println("BigShip: Wrong yaw angles in gun " + string_44_ + ".");
						throw new RuntimeException("Can't register BigShip object");
					}
					shippartproperties.HEAD_STD_YAW = 0.0F;
					shippartproperties.HEAD_YAW_RANGE.set((shippartproperties._HEAD_MIN_YAW), (shippartproperties._HEAD_MAX_YAW));
				}
			}
			shipproperties.WEAPONS_MASK = 0;
			shipproperties.ATTACK_MAX_DISTANCE = 1.0F;
			for (int i_49_ = 0; i_49_ < shipproperties.propparts.length; i_49_++)
			{
				if (shipproperties.propparts[i_49_].haveGun())
				{
					shipproperties.WEAPONS_MASK |= shipproperties.propparts[i_49_].WEAPONS_MASK;
					if (shipproperties.ATTACK_MAX_DISTANCE < shipproperties.propparts[i_49_].ATTACK_MAX_DISTANCE)
						shipproperties.ATTACK_MAX_DISTANCE = (shipproperties.propparts[i_49_].ATTACK_MAX_DISTANCE);
				}
			}
			if (sectfile.get(string, "IsAirport", false))
				shipproperties.propAirport = new AirportProperties(var_class);
			return shipproperties;
		}

		public Actor actorSpawn(ActorSpawnArg actorspawnarg)
		{
			BigshipGeneric bigshipgeneric;
			try
			{
				BigshipGeneric.constr_arg1 = proper;
				BigshipGeneric.constr_arg2 = actorspawnarg;
				bigshipgeneric = (BigshipGeneric) cls.newInstance();
				BigshipGeneric.constr_arg1 = null;
				BigshipGeneric.constr_arg2 = null;
			}
			catch (Exception exception)
			{
				BigshipGeneric.constr_arg1 = null;
				BigshipGeneric.constr_arg2 = null;
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("SPAWN: Can't create Ship object [class:" + cls.getName() + "]");
				return null;
			}
			return bigshipgeneric;
		}

		public SPAWN(Class var_class)
		{
			try
			{
				String string = var_class.getName();
				int i = string.lastIndexOf('.');
				int i_50_ = string.lastIndexOf('$');
				if (i < i_50_)
					i = i_50_;
				String string_51_ = string.substring(i + 1);
				proper = LoadShipProperties(Statics.getShipsFile(), string_51_, var_class);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
				System.out.println("Problem in spawn: " + var_class.getName());
			}
			cls = var_class;
			Spawn.add(cls, this);
		}
	}

	public static class AirportProperties
	{
		public Loc[]			rwy		= { new Loc(), new Loc() };
		public Mesh				towString;
		public Point3d[]		towPRel;
		public CellAirField		cellTO;
		public CellAirField		cellLDG;
		private boolean			bInited	= false;
		private static Loc		loc		= new Loc();
		private static Point3d	p		= new Point3d();
		private static Orient	o		= new Orient();
		private static Matrix4d	m1		= new Matrix4d();
		private static double[]	tmp		= new double[3];

		public void firstInit(BigshipGeneric bigshipgeneric)
		{
			if (!bInited)
			{
				bInited = true;
				HierMesh hiermesh = bigshipgeneric.hierMesh();
				findHook(hiermesh, "_RWY_TO", rwy[0]);
				findHook(hiermesh, "_RWY_LDG", rwy[1]);
				towString = new Mesh("3DO/Arms/ArrestorCable/mono.sim");
				ArrayList arraylist = new ArrayList();
				int i = 0;
				for (;;)
				{
					String string = i <= 9 ? "0" + i : "" + i;
					if (!findHook(hiermesh, "_TOW" + string + "A", loc))
						break;
					arraylist.add(new Point3d(loc.getPoint()));
					findHook(hiermesh, "_TOW" + string + "B", loc);
					arraylist.add(new Point3d(loc.getPoint()));
					i++;
				}
				if (i > 0)
				{
					i *= 2;
					towPRel = new Point3d[i];
					for (int i_52_ = 0; i_52_ < i; i_52_++)
						towPRel[i_52_] = (Point3d) arraylist.get(i_52_);
				}
				fillParks(bigshipgeneric, hiermesh, "_Park", arraylist);
				if (arraylist.size() > 0)
					cellTO = new CellAirField(new CellObject[1][1], arraylist, 1.0);
				fillParks(bigshipgeneric, hiermesh, "_LPark", arraylist);
				if (arraylist.size() > 0)
					cellLDG = new CellAirField(new CellObject[1][1], arraylist, 1.0);
			}
		}

		private void fillParks(BigshipGeneric bigshipgeneric, HierMesh hiermesh, String string, ArrayList arraylist)
		{
			arraylist.clear();
			int i = 0;
			for (;;)
			{
				String string_53_ = string + (i <= 9 ? "0" + i : "" + i);
				if (findHook(hiermesh, string_53_, loc))
				{
					arraylist.add(new Point3d(-p.y, p.x, p.z));
					i++;
				}
				else
					break;
			}
		}

		private boolean findHook(HierMesh hiermesh, String string, Loc loc)
		{
			int i = hiermesh.hookFind(string);
			if (i == -1)
				return false;
			hiermesh.hookMatrix(i, m1);
			m1.getEulers(tmp);
			o.setYPR(Geom.RAD2DEG((float) tmp[0]), 360.0F - Geom.RAD2DEG((float) tmp[1]), 360.0F - Geom.RAD2DEG((float) tmp[2]));
			p.set(m1.m03, m1.m13, m1.m23);
			loc.set(p, o);
			return true;
		}

		public AirportProperties(Class var_class)
		{
			Property.set(var_class, "IsAirport", "true");
		}
	}

	private static class TowStringMeshDraw extends ActorMeshDraw
	{
		private static Loc		lRender		= new Loc();
		private static Loc		l			= new Loc();
		private static Vector3d	tmpVector	= new Vector3d();
		private static Point3d	p0			= new Point3d();
		private static Point3d	p1			= new Point3d();

		public void render(Actor actor)
		{
			super.render(actor);
			BigshipGeneric bigshipgeneric = (BigshipGeneric) actor;
			if (bigshipgeneric.prop.propAirport != null)
			{
				Point3d[] point3ds = bigshipgeneric.prop.propAirport.towPRel;
				if (point3ds != null)
				{
					actor.pos.getRender(lRender);
					int i = point3ds.length / 2;
					for (int i_54_ = 0; i_54_ < i; i_54_++)
					{
						if (i_54_ != bigshipgeneric.towPortNum)
						{
							lRender.transform(point3ds[i_54_ * 2], p0);
							lRender.transform(point3ds[i_54_ * 2 + 1], p1);
							renderTow(bigshipgeneric.prop.propAirport.towString);
						}
						else if (Actor.isValid(bigshipgeneric.towAircraft))
						{
							lRender.transform(point3ds[i_54_ * 2], p0);
							l.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
							bigshipgeneric.towHook.computePos(bigshipgeneric.towAircraft, bigshipgeneric.towAircraft.pos.getRender(), l);
							p1.set(l.getPoint());
							renderTow(bigshipgeneric.prop.propAirport.towString);
							lRender.transform(point3ds[i_54_ * 2 + 1], p0);
							l.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
							bigshipgeneric.towHook.computePos(bigshipgeneric.towAircraft, bigshipgeneric.towAircraft.pos.getRender(), l);
							p1.set(l.getPoint());
							renderTow(bigshipgeneric.prop.propAirport.towString);
						}
					}
				}
			}
		}

		private void renderTow(Mesh mesh)
		{
			tmpVector.sub(p1, p0);
			mesh.setScaleXYZ((float) tmpVector.length(), 1.0F, 1.0F);
			tmpVector.normalize();
			Orient orient = l.getOrient();
			orient.setAT0(tmpVector);
			l.set(p0);
			mesh.setPos(l);
			mesh.render();
		}

		public TowStringMeshDraw(ActorDraw actordraw)
		{
			super(actordraw);
		}
	}

	public ShipProperties getShipProp()
	{
		return prop;
	}

	public static final double Rnd(double d, double d_55_)
	{
		return World.Rnd().nextDouble(d, d_55_);
	}

	public static final float Rnd(float f, float f_56_)
	{
		return World.Rnd().nextFloat(f, f_56_);
	}

	public static final int Rnd(int i, int i_57_)
	{
		return World.Rnd().nextInt(i, i_57_);
	}

	public static final float KmHourToMSec(float f)
	{
		return f / 3.6F;
	}

	private static final long SecsToTicks(float f)
	{
		long l = (long) (0.5 + (double) (f / Time.tickLenFs()));
		return l >= 1L ? l : 1L;
	}

	protected final boolean Head360(FiringDevice firingdevice)
	{
		return parts[firingdevice.part_idx].pro.HEAD_YAW_RANGE.fullcircle();
	}

	public void msgCollisionRequest(Actor actor, boolean[] bools)
	{
		if (actor instanceof BridgeSegment)
		{
			if (dying != 0)
				bools[0] = false;
		}
		else if (path == null && actor instanceof ActorMesh && ((ActorMesh) actor).isStaticPos())
			bools[0] = false;
	}

	public void msgCollision(Actor actor, String string, String string_58_)
	{
		if (dying == 0 && !isNetMirror() && !(actor instanceof WeakBody))
		{
			if (actor instanceof ShipGeneric || actor instanceof BigshipGeneric || actor instanceof BridgeSegment)
				Die(null, -1L, true, true);
		}
	}

	private int findNotDeadPartByShotChunk(String string)
	{
		if (string == null || string == "")
			return -2;
		int i = hierMesh().chunkFindCheck(string);
		if (i < 0)
			return -2;
		for (int i_59_ = 0; i_59_ < parts.length; i_59_++)
		{
			if (parts[i_59_].state != 2)
			{
				if (i == parts[i_59_].pro.baseChunkIdx)
					return i_59_;
				for (int i_60_ = 0; i_60_ < parts[i_59_].pro.additCollisChunkIdx.length; i_60_++)
				{
					if (i == parts[i_59_].pro.additCollisChunkIdx[i_60_])
						return i_59_;
				}
			}
		}
		return -1;
	}

	public void msgShot(Shot shot)
	{
		shot.bodyMaterial = 2;
		if (dying == 0 && !(shot.power <= 0.0F) && (!isNetMirror() || !shot.isMirage()))
		{
			if (wakeupTmr < 0L)
				wakeupTmr = SecsToTicks(Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
			int i = findNotDeadPartByShotChunk(shot.chunkName);
			if (i >= 0)
			{
				float f;
				float f_61_;
				if (shot.powerType == 1)
				{
					f = parts[i].pro.stre.EXPLHIT_MAX_TNT;
					f_61_ = parts[i].pro.stre.EXPLHIT_MAX_TNT;
				}
				else
				{
					f = parts[i].pro.stre.SHOT_MIN_ENERGY;
					f_61_ = parts[i].pro.stre.SHOT_MAX_ENERGY;
				}
				float f_62_ = shot.power * Rnd(1.0F, 1.1F);
				if (!(f_62_ < f))
				{
					tmpvd.set(shot.v);
					pos.getAbs().transformInv(tmpvd);
					parts[i].damageIsFromRight = tmpvd.y > 0.0;
					float f_63_ = f_62_ / f_61_;
					parts[i].damage += f_63_;
//					Part.access$2316(parts[i], f_63_);
					if (isNetMirror() && shot.initiator != null)
						parts[i].mirror_initiator = shot.initiator;
					InjurePart(i, shot.initiator, true);

					// TODO: Added by |ZUTI|: advanced damage logging
					// ---------------------------------------------------------------------------------------------------------------------------------------------------
					if (shot.power > 3000.0F)
					{
						EventLog.type(21, World.getTimeofDay(), EventLog.name(this), ZutiSupportMethods.getAircraftCompleteName(shot.initiator), 0, (float) this.pos.getAbsPoint().x, (float) this.pos
								.getAbsPoint().y, true);
					}
					// ---------------------------------------------------------------------------------------------------------------------------------------------------

					if (!com.maddox.il2.game.Mission.isDogfight() && path != null && parts[i].pro.isItLifeKeeper() && parts[i].damage > 0.2F)
					{
						computeSpeedReduction(parts[i].damage);
						computeNewPath();
					}
				}
			}
		}
	}

	private void computeSpeedReduction(float f)
	{
		int i = (int) (f * 128F);
		if (--i < 0)
			i = 0;
		else if (i > 127)
			i = 127;
		f = (float) i / 128F;
		float f1 = 0.4F * prop.SPEED + (1.0F - f) * 2.0F * prop.SPEED;
		int j = java.lang.Math.round(f1);
		f1 = j;
		if (f1 < CURRSPEED)
			CURRSPEED = f1;
	}

	public void msgExplosion(Explosion explosion)
	{
		if (dying == 0 && (!isNetMirror() || !explosion.isMirage()))
		{
			if (wakeupTmr < 0L)
				wakeupTmr = SecsToTicks(Rnd(DELAY_WAKEUP, DELAY_WAKEUP * 1.2F));
			float f = explosion.power;
			if (explosion.powerType == 2 && explosion.chunkName != null)
				f *= 0.45F;
			int i = -1;
			if (explosion.chunkName != null)
			{
				int j = findNotDeadPartByShotChunk(explosion.chunkName);
				if (j >= 0)
				{
					float f1 = f;
					f1 *= Rnd(1.0F, 1.1F);
					if (f1 >= parts[j].pro.stre.EXPLHIT_MIN_TNT)
					{
						i = j;
						p1.set(explosion.p);
						super.pos.getAbs().transformInv(p1);
						parts[j].damageIsFromRight = ((Tuple3d) (p1)).y < 0.0D;
						float f2 = f1 / parts[j].pro.stre.EXPLHIT_MAX_TNT;
						parts[j].damage = parts[j].damage + f2;
						if (isNetMirror() && explosion.initiator != null)
							parts[j].mirror_initiator = explosion.initiator;
						InjurePart(j, explosion.initiator, true);
						if (explosion.power > 10F)
						{
							// TODO: Added by |ZUTI|: advanced damage logging
							// ---------------------------------------------------------------------------------------------------------------------------------------------------
							EventLog.type(21, World.getTimeofDay(), EventLog.name(this), ZutiSupportMethods.getAircraftCompleteName(explosion.initiator), 0, (float) this.pos.getAbsPoint().x,
									(float) this.pos.getAbsPoint().y, true);
							// ---------------------------------------------------------------------------------------------------------------------------------------------------
						}

						if (!com.maddox.il2.game.Mission.isDogfight() && path != null && parts[j].pro.isItLifeKeeper() && parts[j].damage > 0.2F)
						{
							computeSpeedReduction(parts[j].damage);
							computeNewPath();
						}
					}
				}
			}
			Loc loc = super.pos.getAbs();
			p1.set(explosion.p);
			super.pos.getAbs().transformInv(p1);
			boolean flag = ((Tuple3d) (p1)).y < 0.0D;
			for (int k = 0; k < parts.length; k++)
				if (k != i && parts[k].state != 2)
				{
					p1.set(parts[k].pro.partOffs);
					loc.transform(p1);
					float f3 = parts[k].pro.partR;
					float f4 = explosion.receivedTNT_1meter(p1, f3);
					f4 *= Rnd(1.0F, 1.1F);
					if (f4 >= parts[k].pro.stre.EXPLNEAR_MIN_TNT)
					{
						parts[k].damageIsFromRight = flag;
						float f5 = f4 / parts[k].pro.stre.EXPLNEAR_MAX_TNT;
						parts[k].damage = parts[k].damage + f5;
						if (isNetMirror() && explosion.initiator != null)
							parts[k].mirror_initiator = explosion.initiator;
						InjurePart(k, explosion.initiator, true);

						if (!com.maddox.il2.game.Mission.isDogfight() && path != null && parts[k].pro.isItLifeKeeper() && parts[k].damage > 0.2F)
						{
							computeSpeedReduction(parts[k].damage);
							computeNewPath();
						}
					}
				}
		}
		// ---------------------------WildWillie------------------------
		// TODO: This mod gives a player points for damaging a ship and also records the event
		if (dying == 0 && explosion.power > 10F)
		{
			if (ZutiSupportMethods.isDamagerCurrentUser(explosion.initiator))
			{
				if (explosion.initiator.getArmy() != this.getArmy())
				{
					World.cur().scoreCounter.enemyDamaged(this, explosion.power);
				}
				else
				{
					World.cur().scoreCounter.friendDamaged(this, explosion.power);
				}

				//Added by |ZUTI|: sync this with your crew!
				//--------------------------------------------------
				ZutiSupportMethods_NetSend.syncCrewScore_ToServer(explosion.initiator.name(), this.name(), true, explosion.power);
				//--------------------------------------------------
			}
			// Send "Damaged By" event.
			EventLog.type(21, World.getTimeofDay(), EventLog.name(this), ZutiSupportMethods.getAircraftCompleteName(explosion.initiator), 0, (float) this.pos.getAbsPoint().x, (float) this.pos
					.getAbsPoint().y, true);
		}
		// -------------------------------------------------------------
	}

	private void recomputeShotpoints()
	{
		if (shotpoints == null || shotpoints.length < 1 + parts.length)
			shotpoints = new int[1 + parts.length];
		numshotpoints = 0;
		if (dying == 0)
		{
			numshotpoints = 1;
			shotpoints[0] = 0;
			for (int i = 0; i < parts.length; i++)
			{
				if (parts[i].state != 2)
				{
					int i_72_;
					if (parts[i].pro.isItLifeKeeper())
						i_72_ = parts[i].pro.baseChunkIdx;
					else
					{
						if (!parts[i].pro.haveGun())
							continue;
						i_72_ = parts[i].pro.gunChunkIdx;
					}
					shotpoints[numshotpoints] = i + 1;
					hierMesh().setCurChunk(i_72_);
					hierMesh().getChunkLocObj(tmpL);
					parts[i].shotpointOffs.set(tmpL.getPoint());
					numshotpoints++;
				}
			}
		}
	}

	private boolean visualsInjurePart(int i, boolean bool)
	{
		if (!bool)
		{
			if (parts[i].state == 2)
			{
				parts[i].damage = 1.0F;
				return false;
			}
			if (parts[i].damage < parts[i].pro.BLACK_DAMAGE)
				return false;
			netsendDrown_nparts = 0;
			netsendDrown_depth = 0.0F;
			netsendDrown_pitch = 0.0F;
			netsendDrown_roll = 0.0F;
			netsendDrown_timeS = 0.0F;
			if (parts[i].damage < 1.0F)
			{
				if (parts[i].state == 1)
					return false;
				parts[i].state = 1;
			}
			else
			{
				parts[i].damage = 1.0F;
				parts[i].state = 2;
			}
			if (parts[i].pro.isItLifeKeeper())
			{
				netsendDrown_nparts++;
				netsendDrown_depth += Rnd(0.8F, 1.0F) * parts[i].pro.dmgDepth;
				netsendDrown_pitch += Rnd(0.8F, 1.0F) * parts[i].pro.dmgPitch;
				netsendDrown_roll = (float) ((double) netsendDrown_roll + ((double) (Rnd(0.8F, 1.0F) * parts[i].pro.dmgRoll) * (parts[i].damageIsFromRight ? -1.0 : 1.0)));
				netsendDrown_timeS += Rnd(0.7F, 1.3F) * parts[i].pro.dmgTime;
			}
		}
		if (parts[i].pro.haveGun())
		{
			arms[parts[i].pro.gun_idx].aime.forgetAiming();
			arms[parts[i].pro.gun_idx].enemy = null;
		}
		int[] is = hierMesh().getSubTreesSpec(parts[i].pro.baseChunkName);
		for (int i_73_ = 0; i_73_ < is.length; i_73_++)
		{
			hierMesh().setCurChunk(is[i_73_]);
			if (hierMesh().isChunkVisible())
			{
				for (int i_74_ = 0; i_74_ < parts.length; i_74_++)
				{
					if (i_74_ != i && parts[i_74_].state != 2 && is[i_73_] == parts[i_74_].pro.baseChunkIdx)
					{
						if (!bool && parts[i_74_].state == 0 && parts[i_74_].pro.isItLifeKeeper())
						{
							netsendDrown_nparts++;
							netsendDrown_depth += Rnd(0.8F, 1.0F) * parts[i_74_].pro.dmgDepth;
							netsendDrown_pitch += Rnd(0.8F, 1.0F) * parts[i_74_].pro.dmgPitch;
							netsendDrown_roll = (float) ((double) netsendDrown_roll + ((double) (Rnd(0.8F, 1.0F) * (parts[i_74_].pro.dmgRoll)) * (parts[i_74_].damageIsFromRight ? -1.0 : 1.0)));
							netsendDrown_timeS += Rnd(0.7F, 1.3F) * parts[i_74_].pro.dmgTime;
						}
						parts[i_74_].damage = bool ? 0.0F : 1.0F;
						parts[i_74_].mirror_initiator = null;
						parts[i_74_].state = 2;
						if (parts[i_74_].pro.haveGun())
						{
							arms[parts[i_74_].pro.gun_idx].aime.forgetAiming();
							arms[parts[i_74_].pro.gun_idx].enemy = null;
						}
					}
				}
				if (hierMesh().chunkName().endsWith("_x") || hierMesh().chunkName().endsWith("_X"))
					hierMesh().chunkVisible(false);
				else
				{
					String string = hierMesh().chunkName() + "_dmg";
					int i_75_ = hierMesh().chunkFindCheck(string);
					if (i_75_ >= 0)
					{
						hierMesh().chunkVisible(false);
						hierMesh().chunkVisible(string, true);
					}
				}
			}
		}
		if (pipes != null)
		{
			boolean bool_76_ = false;
			for (int i_77_ = 0; i_77_ < pipes.length; i_77_++)
			{
				if (pipes[i_77_] != null)
				{
					if (pipes[i_77_].pipe == null)
						pipes[i_77_] = null;
					else
					{
						int i_78_ = pipes[i_77_].part_idx;
						if (parts[i_78_].state == 0)
							bool_76_ = true;
						else
						{
							pipes[i_77_].pipe._finish();
							pipes[i_77_].pipe = null;
							pipes[i_77_] = null;
						}
					}
				}
			}
			if (!bool_76_)
			{
				for (int i_79_ = 0; i_79_ < pipes.length; i_79_++)
				{
					if (pipes[i_79_] != null)
						pipes[i_79_] = null;
				}
				pipes = null;
			}
		}
		if (dsmoks != null)
		{
			for (int i_80_ = 0; i_80_ < dsmoks.length; i_80_++)
			{
				if (dsmoks[i_80_] != null && dsmoks[i_80_].pipe == null)
				{
					int i_81_ = dsmoks[i_80_].part_idx;
					if (parts[i_81_].state != 0)
					{
						String string = parts[i_81_].pro.baseChunkName;
						Loc loc = new Loc();
						hierMesh().setCurChunk(string);
						hierMesh().getChunkLocObj(loc);
						float f = parts[i_81_].pro.stre.EXPLNEAR_MIN_TNT;
						String string_82_ = "Effects/Smokes/Smoke";
						if (parts[i_81_].pro.haveGun())
						{
							string_82_ += "Gun";
							if (f < 4.0F)
								string_82_ += "Tiny";
							else if (f < 24.0F)
								string_82_ += "Small";
							else if (f < 32.0F)
								string_82_ += "Medium";
							else if (f < 45.0F)
								string_82_ += "Large";
							else
								string_82_ += "Huge";
							dsmoks[i_80_].pipe = Eff3DActor.New(this, null, loc, 1.0F, string_82_ + ".eff", 600.0F);
							Eff3DActor.New(this, null, loc, 1.0F, string_82_ + "Fire.eff", 120.0F);
						}
						else
						{
							string_82_ += "Ship";
							if (f < 24.0F)
								string_82_ += "Tiny";
							else if (f < 49.0F)
								string_82_ += "Small";
							else if (f < 70.0F)
								string_82_ += "Medium";
							else if (f == 70.0F)
								string_82_ += "Large";
							else if (f < 130.0F)
								string_82_ += "Huge";
							else if (f < 3260.0F)
								string_82_ += "Enormous";
							else
								string_82_ += "Invulnerable";
							dsmoks[i_80_].pipe = Eff3DActor.New(this, null, loc, 1.1F, string_82_ + ".eff", -1.0F);
						}
					}
				}
			}
		}
		recomputeShotpoints();
		return true;
	}

	void master_sendDrown(float f, float f_83_, float f_84_, float f_85_)
	{
		if (net.isMirrored())
		{
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			try
			{
				netmsgguaranted.writeByte(100);
				float f_86_ = f / 1000.0F;
				if (f_86_ <= 0.0F)
					f_86_ = 0.0F;
				if (f_86_ >= 1.0F)
					f_86_ = 1.0F;
				int i = (int) (f_86_ * 32767.0F);
				if (i > 32767)
					i = 32767;
				if (i < 0)
					i = 0;
				netmsgguaranted.writeShort(i);
				f_86_ = f_83_ / 90.0F;
				if (f_86_ <= -1.0F)
					f_86_ = -1.0F;
				if (f_86_ >= 1.0F)
					f_86_ = 1.0F;
				i = (int) (f_86_ * 32767.0F);
				if (i > 32767)
					i = 32767;
				if (i < -32767)
					i = -32767;
				netmsgguaranted.writeShort(i);
				f_86_ = f_84_ / 90.0F;
				if (f_86_ <= -1.0F)
					f_86_ = -1.0F;
				if (f_86_ >= 1.0F)
					f_86_ = 1.0F;
				i = (int) (f_86_ * 32767.0F);
				if (i > 32767)
					i = 32767;
				if (i < -32767)
					i = -32767;
				netmsgguaranted.writeShort(i);
				f_86_ = f_85_ / 1200.0F;
				if (f_86_ <= 0.0F)
					f_86_ = 0.0F;
				if (f_86_ >= 1.0F)
					f_86_ = 1.0F;
				i = (int) (f_86_ * 32767.0F);
				if (i > 32767)
					i = 32767;
				if (i < 0)
					i = 0;
				netmsgguaranted.writeShort(i);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void InjurePart(int i, Actor actor, boolean bool)
	{
		if (!isNetMirror() && visualsInjurePart(i, false) && dying == 0)
		{
			boolean bool_87_ = false;
			for (int i_88_ = 0; i_88_ < parts.length; i_88_++)
			{
				if (parts[i_88_].pro.isItLifeKeeper() && parts[i_88_].state == 2)
				{
					bool_87_ = true;
					break;
				}
			}
			netsendPartsState_needtosend = true;
			if (netsendDrown_nparts > 0)
			{
				netsendDrown_depth += bodyDepth1;
				netsendDrown_pitch += bodyPitch1;
				netsendDrown_roll += bodyRoll1;
				netsendDrown_timeS /= (float) netsendDrown_nparts;
				if (netsendDrown_timeS >= 1200.0F)
					netsendDrown_timeS = 1200.0F;
				tmInterpoStart = NetServerParams.getServerTime();
				tmInterpoEnd = tmInterpoStart + (long) (netsendDrown_timeS * 1000.0F);
				bodyDepth0 = bodyDepth;
				bodyPitch0 = bodyPitch;
				bodyRoll0 = bodyRoll;
				bodyDepth1 = netsendDrown_depth;
				bodyPitch1 = netsendDrown_pitch;
				bodyRoll1 = netsendDrown_roll;
				master_sendDrown(netsendDrown_depth, netsendDrown_pitch, netsendDrown_roll, netsendDrown_timeS);
			}
			if (bool_87_)
				Die(actor, -1L, bool, true);
		}
	}

	private float computeSeaDepth(Point3d point3d)
	{
		for (float f = 5.0F; f <= 355.0F; f += 10.0F)
		{
			for (float f_89_ = 0.0F; f_89_ < 360.0F; f_89_ += 30.0F)
			{
				float f_90_ = f * Geom.cosDeg(f_89_);
				float f_91_ = f * Geom.sinDeg(f_89_);
				f_90_ += (float) point3d.x;
				f_91_ += (float) point3d.y;
				if (!World.land().isWater((double) f_90_, (double) f_91_))
					return 150.0F * (f / 355.0F);
			}
		}
		return 1000.0F;
	}

	private void computeSinkingParams(long l)
	{
		if (path != null)
			setMovablePosition(l);
		else
			setPosition();
		pos.reset();
		float f = computeSeaDepth(pos.getAbsPoint()) * Rnd(1.0F, 1.25F);
		if (f >= 400.0F)
			f = 400.0F;
		float f_92_ = Rnd(0.2F, 0.25F);
		float f_93_;
		float f_94_;
		float f_95_;
		float f_96_;
		if (f >= 200.0F)
		{
			f_93_ = Rnd(90.0F, 110.0F);
			f_94_ = f_93_ * f_92_;
			f_95_ = 50.0F - Rnd(0.0F, 20.0F);
			f_96_ = Rnd(15.0F, 32.0F);
			f_92_ *= 1.6F;
		}
		else
		{
			f_93_ = Rnd(30.0F, 40.0F);
			f_94_ = f_93_ * f_92_;
			f_95_ = 4.5F - Rnd(0.0F, 2.5F);
			f_96_ = Rnd(6.0F, 13.0F);
		}
		float f_97_ = (f - f_94_) / f_92_;
		if (f_97_ < 1.0F)
			f_97_ = 1.0F;
		float f_98_ = f_97_ * f_92_;
		computeInterpolatedDPR(l);
		bodyDepth0 = bodyDepth;
		bodyPitch0 = bodyPitch;
		bodyRoll0 = bodyRoll;
		bodyDepth1 += f_94_;
		BigshipGeneric bigshipgeneric_99_ = this;
		bigshipgeneric_99_.bodyPitch1 = (bigshipgeneric_99_.bodyPitch1 + ((double) bodyPitch1 <= 0.0 ? -1.0F : 1.0F) * f_95_);
		BigshipGeneric bigshipgeneric_100_ = this;
		bigshipgeneric_100_.bodyRoll1 = (bigshipgeneric_100_.bodyRoll1 + ((double) bodyRoll1 <= 0.0 ? -1.0F : 1.0F) * f_96_);
		if (bodyPitch1 > 80.0F)
			bodyPitch1 = 80.0F;
		if (bodyPitch1 < -80.0F)
			bodyPitch1 = -80.0F;
		if (bodyRoll1 > 80.0F)
			bodyRoll1 = 80.0F;
		if (bodyRoll1 < -80.0F)
			bodyRoll1 = -80.0F;
		tmInterpoStart = l;
		float f_101_ = Rnd(2.0F, 50.0F);
		tmInterpoEnd = tmInterpoStart + (long) (f_93_ * f_101_ * 1000.0F);
		tmInterpoEnd = tmInterpoStart + (long) (f_93_ * 1000.0F);
		sink2Depth = bodyDepth1 + f_98_;
		sink2Pitch = bodyPitch1;
		sink2Roll = bodyRoll1;
		sink2timeWhenStop = tmInterpoEnd + (long) (f_97_ * 1000.0F);
	}

	private void showExplode()
	{
		Explosions.Antiaircraft_Explode(pos.getAbsPoint());
	}

	private void Die(Actor actor, long l, boolean bool, boolean bool_102_)
	{
		if (dying == 0)
		{
			if (l < 0L)
			{
				if (isNetMirror())
				{
					System.out.println("** bigship InternalError: mirror death");
					return;
				}
				l = NetServerParams.getServerTime();
			}
			dying = 1;
			World.onActorDied(this, actor);
			recomputeShotpoints();
			forgetAllAiming();
			SetEffectsIntens(-1.0F);
			if (bool_102_)
				computeSinkingParams(l);
			computeInterpolatedDPR(l);
			if (path != null)
				setMovablePosition(l);
			else
				setPosition();
			pos.reset();
			timeOfDeath = l;
			if (bool)
				showExplode();
			if (bool && isNetMaster())
				send_DeathCommand(actor, null);
		}
	}

	public void destroy()
	{
		if (!isDestroyed())
		{
			eraseGuns();
			if (parts != null)
			{
				for (int i = 0; i < parts.length; i++)
				{
					parts[i].mirror_initiator = null;
					parts[i] = null;
				}
				parts = null;
			}
			super.destroy();
		}
	}

	private boolean isAnyEnemyNear()
	{
		NearestEnemies.set(WeaponsMask());
		Actor actor = NearestEnemies.getAFoundEnemy(pos.getAbsPoint(), 2000.0, getArmy());
		return actor != null;
	}

	private final FiringDevice GetFiringDevice(Aim aim)
	{
		for (int i = 0; i < prop.nGuns; i++)
		{
			if (arms[i] != null && arms[i].aime == aim)
				return arms[i];
		}
		System.out.println("Internal error 1: Can't find ship gun.");
		return null;
	}

	private final ShipPartProperties GetGunProperties(Aim aim)
	{
		for (int i = 0; i < prop.nGuns; i++)
		{
			if (arms[i].aime == aim)
				return parts[arms[i].part_idx].pro;
		}
		System.out.println("Internal error 2: Can't find ship gun.");
		return null;
	}

	private void setGunAngles(FiringDevice firingdevice, float f, float f_103_)
	{
		firingdevice.headYaw = f;
		firingdevice.gunPitch = f_103_;
		ShipPartProperties shippartproperties = parts[firingdevice.part_idx].pro;
		tmpYPR[1] = 0.0F;
		tmpYPR[2] = 0.0F;
		hierMesh().setCurChunk(shippartproperties.headChunkIdx);
		tmpYPR[0] = firingdevice.headYaw;
		hierMesh().chunkSetAngles(tmpYPR);
		hierMesh().setCurChunk(shippartproperties.gunChunkIdx);
		tmpYPR[0] = -(firingdevice.gunPitch - shippartproperties.GUN_STD_PITCH);
		hierMesh().chunkSetAngles(tmpYPR);
	}

	private void eraseGuns()
	{
		if (arms != null)
		{
			for (int i = 0; i < prop.nGuns; i++)
			{
				if (arms[i] != null)
				{
					if (arms[i].aime != null)
					{
						arms[i].aime.forgetAll();
						arms[i].aime = null;
					}
					if (arms[i].gun != null)
					{
						ObjState.destroy(arms[i].gun);
						arms[i].gun = null;
					}
					arms[i].enemy = null;
					arms[i] = null;
				}
			}
			arms = null;
		}
	}

	private void forgetAllAiming()
	{
		if (arms != null)
		{
			for (int i = 0; i < prop.nGuns; i++)
			{
				if (arms[i] != null && arms[i].aime != null)
				{
					arms[i].aime.forgetAiming();
					arms[i].enemy = null;
				}
			}
		}
	}

	private void CreateGuns()
	{
		arms = new FiringDevice[prop.nGuns];
		for (int i = 0; i < parts.length; i++)
		{
			if (parts[i].pro.haveGun())
			{
				ShipPartProperties shippartproperties = parts[i].pro;
				int i_104_ = shippartproperties.gun_idx;
				arms[i_104_] = new FiringDevice();
				arms[i_104_].gun_idx = i_104_;
				arms[i_104_].part_idx = i;
				arms[i_104_].gun = null;
				try
				{
					arms[i_104_].gun = (Gun) shippartproperties.gunClass.newInstance();
				}
				catch (Exception exception)
				{
					System.out.println(exception.getMessage());
					exception.printStackTrace();
					System.out.println("BigShip: Can't create gun '" + shippartproperties.gunClass.getName() + "'");
				}
				arms[i_104_].gun.set(this, shippartproperties.gunShellStartHookName);
				arms[i_104_].gun.loadBullets(-1);
				arms[i_104_].aime = new Aim(this, isNetMirror(), SLOWFIRE_K * (shippartproperties.DELAY_AFTER_SHOOT));
				arms[i_104_].enemy = null;
			}
		}
	}

	public Object getSwitchListener(Message message)
	{
		return this;
	}

	private void initMeshBasedProperties()
	{
		for (int i = 0; i < prop.propparts.length; i++)
		{
			ShipPartProperties shippartproperties = prop.propparts[i];
			if (shippartproperties.baseChunkIdx < 0)
			{
				shippartproperties.baseChunkIdx = hierMesh().chunkFind(shippartproperties.baseChunkName);
				hierMesh().setCurChunk(shippartproperties.baseChunkIdx);
				hierMesh().getChunkLocObj(tmpL);
				tmpL.get(p1);
				shippartproperties.partOffs = new Point3f();
				shippartproperties.partOffs.set(p1);
				shippartproperties.partR = hierMesh().getChunkVisibilityR();
				int i_105_ = shippartproperties.additCollisChunkName.length;
				for (int i_106_ = 0; i_106_ < shippartproperties.additCollisChunkName.length; i_106_++)
				{
					if (hierMesh().chunkFindCheck((shippartproperties.additCollisChunkName[i_106_]) + "_dmg") >= 0)
						i_105_++;
				}
				if (hierMesh().chunkFindCheck(shippartproperties.baseChunkName + "_dmg") >= 0)
					i_105_++;
				shippartproperties.additCollisChunkIdx = new int[i_105_];
				i_105_ = 0;
				for (int i_107_ = 0; i_107_ < shippartproperties.additCollisChunkName.length; i_107_++)
				{
					shippartproperties.additCollisChunkIdx[i_105_++] = hierMesh().chunkFind(shippartproperties.additCollisChunkName[i_107_]);
					int i_108_ = hierMesh().chunkFindCheck((shippartproperties.additCollisChunkName[i_107_]) + "_dmg");
					if (i_108_ >= 0)
						shippartproperties.additCollisChunkIdx[i_105_++] = i_108_;
				}
				int i_109_ = hierMesh().chunkFindCheck((shippartproperties.baseChunkName) + "_dmg");
				if (i_109_ >= 0)
					shippartproperties.additCollisChunkIdx[i_105_++] = i_109_;
				if (i_105_ != shippartproperties.additCollisChunkIdx.length)
					System.out.println("*** bigship: collis internal error");
				if (shippartproperties.haveGun())
				{
					shippartproperties.headChunkIdx = hierMesh().chunkFind(shippartproperties.headChunkName);
					shippartproperties.gunChunkIdx = hierMesh().chunkFind(shippartproperties.gunChunkName);
					hierMesh().setCurChunk(shippartproperties.headChunkIdx);
					hierMesh().getChunkLocObj(tmpL);
					shippartproperties.fireOffset = new Point3d();
					tmpL.get(shippartproperties.fireOffset);
					shippartproperties.fireOrient = new Orient();
					tmpL.get(shippartproperties.fireOrient);
					Vector3d vector3d = new Vector3d();
					Vector3d vector3d_110_ = new Vector3d();
					vector3d.set(1.0, 0.0, 0.0);
					vector3d_110_.set(1.0, 0.0, 0.0);
					tmpL.transform(vector3d);
					hierMesh().setCurChunk(shippartproperties.gunChunkIdx);
					hierMesh().getChunkLocObj(tmpL);
					tmpL.transform(vector3d_110_);
					shippartproperties.GUN_STD_PITCH = Geom.RAD2DEG((float) vector3d.angle(vector3d_110_));
				}
			}
		}
		initMeshMats();
	}

	private void initMeshMats()
	{
		if (!Config.cur.b3dgunners)
		{
			hierMesh().materialReplaceToNull("Sailor");
			hierMesh().materialReplaceToNull("Sailor1o");
			hierMesh().materialReplaceToNull("Sailor2p");
		}
	}

	private void makeLive()
	{
		dying = 0;
		for (int i = 0; i < parts.length; i++)
		{
			parts[i].damage = 0.0F;
			parts[i].state = 0;
			parts[i].pro = prop.propparts[i];
		}
		for (int i = 0; i < hierMesh().chunks(); i++)
		{
			hierMesh().setCurChunk(i);

			if (hierMesh().chunkName().equals("Red"))
				continue;

			boolean bool = !hierMesh().chunkName().endsWith("_dmg");
			if (hierMesh().chunkName().startsWith("ShdwRcv"))
				bool = false;
			hierMesh().chunkVisible(bool);
		}
		recomputeShotpoints();
	}

	private void setDefaultLivePose()
	{
		int i = hierMesh().hookFind("Ground_Level");
		if (i != -1)
		{
			Matrix4d matrix4d = new Matrix4d();
			hierMesh().hookMatrix(i, matrix4d);
		}
		for (int i_111_ = 0; i_111_ < arms.length; i_111_++)
		{
			int i_112_ = arms[i_111_].part_idx;
			setGunAngles(arms[i_111_], parts[i_112_].pro.HEAD_STD_YAW, parts[i_112_].pro.GUN_STD_PITCH);
		}
		bodyDepth = 0.0F;
		align();
	}

	protected BigshipGeneric()
	{
		this(constr_arg1, constr_arg2);
	}

	private BigshipGeneric(ShipProperties shipproperties, ActorSpawnArg actorspawnarg)
	{
		super(shipproperties.meshName);

		CURRSPEED = 1.0F;
		isTurning = false;
		isTurningBackward = false;
		mustRecomputePath = false;
		mustSendSpeedToNet = false;

		wake = new Eff3DActor[] { null, null, null };
		prop = null;
		netsendPartsState_lasttimeMS = 0L;
		netsendPartsState_needtosend = false;
		netsendFire_lasttimeMS = 0L;
		netsendFire_armindex = 0;
		arms = null;
		parts = null;
		shotpoints = null;
		netsendDmg_lasttimeMS = 0L;
		netsendDmg_partindex = 0;
		cachedSeg = 0;
		timeOfDeath = 0L;
		dying = 0;
		respawnDelay = 0L;
		wakeupTmr = 0L;
		DELAY_WAKEUP = 0.0F;
		SKILL_IDX = 2;
		SLOWFIRE_K = 1.0F;
		pipes = null;
		dsmoks = null;
		noseW = null;
		nose = null;
		tail = null;
		o = new Orient();
		rollAmp = 0.7F * (float) Mission.curCloudsType();
		rollPeriod = 12345;
		rollWAmp = (double) rollAmp * 19739.208802178713 / (double) (180 * rollPeriod);
		pitchAmp = 0.1F * (float) Mission.curCloudsType();
		pitchPeriod = 23456;
		pitchWAmp = ((double) pitchAmp * 19739.208802178713 / (double) (180 * pitchPeriod));
		W = new Vector3d(0.0, 0.0, 0.0);
		N = new Vector3d(0.0, 0.0, 1.0);
		tmpV = new Vector3d();
		initOr = new Orient();
		initLoc = new Loc();
		airport = null;
		towPortNum = -1;
		prop = shipproperties;

		if ((this instanceof RwyTransp) || (this instanceof RwyTranspWide) || (this instanceof RwyTranspSqr))
			hideTransparentRunwayRed();

		initMeshBasedProperties();
		actorspawnarg.setStationary(this);
		path = null;
		collide(true);
		drawing(true);
		tmInterpoStart = tmInterpoEnd = 0L;
		bodyDepth = bodyPitch = bodyRoll = 0.0F;
		bodyDepth0 = bodyPitch0 = bodyRoll0 = 0.0F;
		bodyDepth1 = bodyPitch1 = bodyRoll1 = 0.0F;
		shipYaw = actorspawnarg.orient.getYaw();
		setPosition();
		pos.reset();
		parts = new Part[prop.propparts.length];
		for (int i = 0; i < parts.length; i++)
			parts[i] = new Part();
		makeLive();
		createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
		SKILL_IDX = Chief.new_SKILL_IDX;
		SLOWFIRE_K = Chief.new_SLOWFIRE_K;
		DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
		wakeupTmr = 0L;
		CreateGuns();
		int i = 0;
		for (int i_113_ = 0; i_113_ < parts.length; i_113_++)
		{
			if (parts[i_113_].pro.isItLifeKeeper() || parts[i_113_].pro.haveGun())
				i++;
		}
		if (i <= 0)
			dsmoks = null;
		else
		{
			dsmoks = new Pipe[i];
			i = 0;
			for (int i_114_ = 0; i_114_ < parts.length; i_114_++)
			{
				if (parts[i_114_].pro.isItLifeKeeper() || parts[i_114_].pro.haveGun())
				{
					dsmoks[i] = new Pipe();
					dsmoks[i].part_idx = i_114_;
					dsmoks[i].pipe = null;
					i++;
				}
			}
		}
		setDefaultLivePose();
		if (!isNetMirror() && prop.nGuns > 0 && DELAY_WAKEUP > 0.0F)
			wakeupTmr = -SecsToTicks(Rnd(2.0F, 7.0F));

		if (((this instanceof RwyTransp) || (this instanceof RwyTranspWide) || (this instanceof RwyTranspSqr)) && Engine.land().isWater(pos.getAbs().getX(), pos.getAbs().getY()))
			hierMesh().chunkVisible("Hull1", false);

		createAirport();
		if (!interpEnd("move"))
		{
			interpPut(new Move(), "move", Time.current(), null);
			InterpolateAdapter.forceListener(this);
		}

		CURRSPEED = prop.SPEED;
	}

	public BigshipGeneric(String string, int i, SectFile sectfile, String string_115_, SectFile sectfile_116_, String string_117_)
	{
		CURRSPEED = 1.0F;
		isTurning = false;
		isTurningBackward = false;
		mustRecomputePath = false;
		mustSendSpeedToNet = false;

		wake = new Eff3DActor[] { null, null, null };
		prop = null;
		netsendPartsState_lasttimeMS = 0L;
		netsendPartsState_needtosend = false;
		netsendFire_lasttimeMS = 0L;
		netsendFire_armindex = 0;
		arms = null;
		parts = null;
		shotpoints = null;
		netsendDmg_lasttimeMS = 0L;
		netsendDmg_partindex = 0;
		cachedSeg = 0;
		timeOfDeath = 0L;
		dying = 0;
		respawnDelay = 0L;
		wakeupTmr = 0L;
		DELAY_WAKEUP = 0.0F;
		SKILL_IDX = 2;
		SLOWFIRE_K = 1.0F;
		pipes = null;
		dsmoks = null;
		noseW = null;
		nose = null;
		tail = null;
		o = new Orient();
		rollAmp = 0.7F * (float) Mission.curCloudsType();
		rollPeriod = 12345;
		rollWAmp = (double) rollAmp * 19739.208802178713 / (double) (180 * rollPeriod);
		pitchAmp = 0.1F * (float) Mission.curCloudsType();
		pitchPeriod = 23456;
		pitchWAmp = ((double) pitchAmp * 19739.208802178713 / (double) (180 * pitchPeriod));
		W = new Vector3d(0.0, 0.0, 0.0);
		N = new Vector3d(0.0, 0.0, 1.0);
		tmpV = new Vector3d();
		initOr = new Orient();
		initLoc = new Loc();
		airport = null;
		towPortNum = -1;

		if ((this instanceof RwyTransp) || (this instanceof RwyTranspWide) || (this instanceof RwyTranspSqr))
			hideTransparentRunwayRed();

		try
		{
			int i_118_ = sectfile.sectionIndex(string_115_);
			String string_119_ = sectfile.var(i_118_, 0);
			Object object = Spawn.get(string_119_);
			if (object == null)
				throw new ActorException("Ship: Unknown class of ship (" + string_119_ + ")");
			prop = ((SPAWN) object).proper;
			try
			{
				setMesh(prop.meshName);
			}
			catch (RuntimeException runtimeexception)
			{
				super.destroy();
				throw runtimeexception;
			}
			initMeshBasedProperties();
			if (prop.soundName != null)
				newSound(prop.soundName, true);
			setName(string);
			setArmy(i);
			LoadPath(sectfile_116_, string_117_);
			cachedSeg = 0;
			tmInterpoStart = tmInterpoEnd = 0L;
			bodyDepth = bodyPitch = bodyRoll = 0.0F;
			bodyDepth0 = bodyPitch0 = bodyRoll0 = 0.0F;
			bodyDepth1 = bodyPitch1 = bodyRoll1 = 0.0F;
			setMovablePosition(NetServerParams.getServerTime());
			pos.reset();
			collide(true);
			drawing(true);
			parts = new Part[prop.propparts.length];
			for (int i_120_ = 0; i_120_ < parts.length; i_120_++)
				parts[i_120_] = new Part();
			makeLive();
			int i_121_ = 0;
			for (int i_122_ = 0; i_122_ <= 10; i_122_++)
			{
				String string_123_ = "Vapor";
				if (i_122_ > 0)
					string_123_ += i_122_ - 1;
				if (mesh().hookFind(string_123_) >= 0)
					i_121_++;
			}
			if (i_121_ <= 0)
				pipes = null;
			else
			{
				pipes = new Pipe[i_121_];
				i_121_ = 0;
				for (int i_124_ = 0; i_124_ <= 10; i_124_++)
				{
					String string_125_ = "Vapor";
					if (i_124_ > 0)
						string_125_ += i_124_ - 1;
					if (mesh().hookFind(string_125_) >= 0)
					{
						pipes[i_121_] = new Pipe();
						int i_126_ = hierMesh().hookParentChunk(string_125_);
						if (i_126_ < 0)
						{
							System.out.println(" *** Bigship: unexpected error in vapor hook " + string_125_);
							pipes = null;
							break;
						}
						int i_127_;
						for (i_127_ = 0; (i_127_ < parts.length && parts[i_127_].pro.baseChunkIdx != i_126_); i_127_++)
						{
							/* empty */
						}
						if (i_127_ >= parts.length)
						{
							System.out.println(" *** Bigship: vapor hook '" + string_125_ + "' MUST be linked to baseChunk");
							pipes = null;
							break;
						}
						pipes[i_121_].part_idx = i_127_;
						HookNamed hooknamed = new HookNamed(this, string_125_);
						pipes[i_121_].pipe = (Eff3DActor.New(this, hooknamed, null, 1.0F, "Effects/Smokes/SmokePipeShip.eff", -1.0F));
						i_121_++;
					}
				}
			}
			wake[2] = wake[1] = wake[0] = null;
			tail = null;
			noseW = null;
			nose = null;
			boolean bool = prop.SLIDER_DIST / 2.5F < 90.0F;
			if (mesh().hookFind("_Prop") >= 0)
			{
				HookNamedZ0 hooknamedz0 = new HookNamedZ0(this, "_Prop");
				tail = (Eff3DActor.New(this, hooknamedz0, null, 1.0F, (bool ? "3DO/Effects/Tracers/ShipTrail/PropWakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/PropWake.eff"), -1.0F));
			}
			if (mesh().hookFind("_Centre") >= 0)
			{
				Loc loc = new Loc();
				Loc loc_128_ = new Loc();
				HookNamed hooknamed = new HookNamed(this, "_Left");
				hooknamed.computePos(this, new Loc(), loc);
				HookNamed hooknamed_129_ = new HookNamed(this, "_Right");
				hooknamed_129_.computePos(this, new Loc(), loc_128_);
				float f = (float) loc.getPoint().distance(loc_128_.getPoint());
				HookNamedZ0 hooknamedz0 = new HookNamedZ0(this, "_Centre");
				if (mesh().hookFind("_Prop") >= 0)
				{
					HookNamedZ0 hooknamedz0_130_ = new HookNamedZ0(this, "_Prop");
					Loc loc_131_ = new Loc();
					hooknamedz0.computePos(this, new Loc(), loc_131_);
					Loc loc_132_ = new Loc();
					hooknamedz0_130_.computePos(this, new Loc(), loc_132_);
					float f_133_ = (float) loc_131_.getPoint().distance(loc_132_.getPoint());
					wake[0] = (Eff3DActor.New(this, hooknamedz0_130_, new Loc((double) -f_133_ * 0.33, 0.0, 0.0, 0.0F, 30.0F, 0.0F), f, (bool ? "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff"
							: "3DO/Effects/Tracers/ShipTrail/Wake.eff"), -1.0F));
					wake[1] = (Eff3DActor.New(this, hooknamedz0, new Loc((double) f_133_ * 0.15, 0.0, 0.0, 0.0F, 30.0F, 0.0F), f, (bool ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff"
							: "3DO/Effects/Tracers/ShipTrail/WakeS.eff"), -1.0F));
					wake[2] = (Eff3DActor.New(this, hooknamedz0, new Loc((double) -f_133_ * 0.15, 0.0, 0.0, 0.0F, 30.0F, 0.0F), f, (bool ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff"
							: "3DO/Effects/Tracers/ShipTrail/WakeS.eff"), -1.0F));
				}
				else
					wake[0] = (Eff3DActor.New(this, hooknamedz0, new Loc((double) -f * 0.3, 0.0, 0.0, 0.0F, 30.0F, 0.0F), f,
							((double) prop.SLIDER_DIST / 2.5 >= 50.0 ? "3DO/Effects/Tracers/ShipTrail/Wake.eff" : "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff"), -1.0F));
			}
			if (mesh().hookFind("_Nose") >= 0)
			{
				HookNamedZ0 hooknamedz0 = new HookNamedZ0(this, "_Nose");
				noseW = (Eff3DActor.New(this, hooknamedz0, new Loc(0.0, 0.0, 0.0, 0.0F, 30.0F, 0.0F), 1.0F, "3DO/Effects/Tracers/ShipTrail/SideWave.eff", -1.0F));
				nose = (Eff3DActor.New(this, hooknamedz0, new Loc(0.0, 0.0, 0.0, 0.0F, 30.0F, 0.0F), 1.0F, (bool ? "3DO/Effects/Tracers/ShipTrail/FrontPuffBoat.eff"
						: "3DO/Effects/Tracers/ShipTrail/FrontPuff.eff"), -1.0F));
			}
			SetEffectsIntens(0.0F);
			int i_134_ = Mission.cur().getUnitNetIdRemote(this);
			NetChannel netchannel = Mission.cur().getNetMasterChannel();
			if (netchannel == null)
				net = new Master(this);
			else if (i_134_ != 0)
				net = new Mirror(this, netchannel, i_134_);
			SKILL_IDX = Chief.new_SKILL_IDX;
			SLOWFIRE_K = Chief.new_SLOWFIRE_K;
			DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
			wakeupTmr = 0L;
			CreateGuns();
			i_134_ = 0;
			for (int i_135_ = 0; i_135_ < parts.length; i_135_++)
			{
				if (parts[i_135_].pro.isItLifeKeeper() || parts[i_135_].pro.haveGun())
					i_134_++;
			}
			if (i_134_ <= 0)
				dsmoks = null;
			else
			{
				dsmoks = new Pipe[i_134_];
				i_134_ = 0;
				for (int i_136_ = 0; i_136_ < parts.length; i_136_++)
				{
					if (parts[i_136_].pro.isItLifeKeeper() || parts[i_136_].pro.haveGun())
					{
						dsmoks[i_134_] = new Pipe();
						dsmoks[i_134_].part_idx = i_136_;
						dsmoks[i_134_].pipe = null;
						i_134_++;
					}
				}
			}
			setDefaultLivePose();
			if (!isNetMirror() && prop.nGuns > 0 && DELAY_WAKEUP > 0.0F)
				wakeupTmr = -SecsToTicks(Rnd(2.0F, 7.0F));
			createAirport();

			if (((this instanceof RwyTransp) || (this instanceof RwyTranspWide) || (this instanceof RwyTranspSqr)) && Engine.land().isWater(pos.getAbs().getX(), pos.getAbs().getY()))
				hierMesh().chunkVisible("Hull1", false);

			if (!interpEnd("move"))
			{
				interpPut(new Move(), "move", Time.current(), null);
				InterpolateAdapter.forceListener(this);
			}
		}
		catch (Exception exception)
		{
			System.out.println("Ship creation failure:");
			System.out.println(exception.getMessage());
			exception.printStackTrace();
			throw new ActorException();
		}

		// TODO: Added by |ZUTI|
		// ----------------------
		zutiIsShipChief = true;
		zutiShipName = string_115_.substring(string_115_.indexOf(".") + 1, string_115_.length());
		zutiReportFinalDestination = ZutiSupportMethods.isMovingRRRObject(zutiShipName, this);
		Segment lastSection = (Segment) path.get(path.size() - 1);
		zutiLastPointX = (int) lastSection.posOut.x;
		zutiLastPointY = (int) lastSection.posOut.y;
		// System.out.println("Bigship name: " + zutiShipName);
		// ----------------------

		CURRSPEED = 2.0F * prop.SPEED;
	}

	private void SetEffectsIntens(float f)
	{
		if (dying != 0)
			f = -1.0F;
		if (pipes != null)
		{
			boolean bool = false;
			for (int i = 0; i < pipes.length; i++)
			{
				if (pipes[i] != null)
				{
					if (pipes[i].pipe == null)
						pipes[i] = null;
					else if (f >= 0.0F)
					{
						pipes[i].pipe._setIntesity(f);
						bool = true;
					}
					else
					{
						pipes[i].pipe._finish();
						pipes[i].pipe = null;
						pipes[i] = null;
					}
				}
			}
			if (!bool)
			{
				for (int i = 0; i < pipes.length; i++)
				{
					if (pipes[i] != null)
						pipes[i] = null;
				}
				pipes = null;
			}
		}

		// TODO: Added by |ZUTI|: if wake effects are disabled, set f to -1
		// -----------------------------------------
		if (!zutiDrawWakeEffects)
		{
			f = 0.0F;
			// System.out.println(" NOT DRAWING!");
			// System.out.println("=====================================");
		}
		// -----------------------------------------

		for (int i = 0; i < 3; i++)
		{
			if (wake[i] != null)
			{
				if (f >= 0.0F)
					wake[i]._setIntesity(f);
				else
				{
					wake[i]._finish();
					wake[i] = null;
				}
			}
		}
		if (noseW != null)
		{
			if (f >= 0.0F)
				noseW._setIntesity(f);
			else
			{
				noseW._finish();
				noseW = null;
			}
		}
		if (nose != null)
		{
			if (f >= 0.0F)
				nose._setIntesity(f);
			else
			{
				nose._finish();
				nose = null;
			}
		}
		if (tail != null)
		{
			if (f >= 0.0F)
				tail._setIntesity(f);
			else
			{
				// TODO: Added by |ZUTI|
				// -----------------------------------------
				if (zutiReportResources && zutiReportFinalDestination && cachedSeg == path.size() - 1)
				{
					zutiHasShipReachedFinalDestination();
				}
				// -----------------------------------------
				
				tail._finish();
				tail = null;
			}
		}
	}

	private void LoadPath(SectFile sectfile, String string)
	{
		int i = sectfile.sectionIndex(string);
		if (i < 0)
			throw new ActorException("Ship path: Section [" + string + "] not found");
		int i_137_ = sectfile.vars(i);
		if (i_137_ < 1)
			throw new ActorException("Ship path must contain at least 2 nodes");
		path = new ArrayList();
		for (int i_138_ = 0; i_138_ < i_137_; i_138_++)
		{
			StringTokenizer stringtokenizer = new StringTokenizer(sectfile.line(i, i_138_));
			float f = Float.valueOf(stringtokenizer.nextToken()).floatValue();
			float f_139_ = Float.valueOf(stringtokenizer.nextToken()).floatValue();
			Float.valueOf(stringtokenizer.nextToken()).floatValue();
			double d = 0.0;
			float f_141_ = 0.0F;
			if (stringtokenizer.hasMoreTokens())
			{
				d = Double.valueOf(stringtokenizer.nextToken()).doubleValue();
				if (stringtokenizer.hasMoreTokens())
				{
					Double.valueOf(stringtokenizer.nextToken()).doubleValue();
					if (stringtokenizer.hasMoreTokens())
					{
						f_141_ = Float.valueOf(stringtokenizer.nextToken()).floatValue();
						if (f_141_ <= 0.0F)
							f_141_ = prop.SPEED;
					}
				}
			}
			if (f_141_ <= 0.0F && (i_138_ == 0 || i_138_ == i_137_ - 1))
				f_141_ = prop.SPEED;
			if (i_138_ >= i_137_ - 1)
				d = -1.0;
			Segment segment = new Segment();
			segment.posIn = new Point3d((double) f, (double) f_139_, 0.0);
			if (Math.abs(d) < 0.1)
				segment.timeIn = 0L;
			else
			{
				segment.timeIn = (long) (d * 60.0 * 1000.0 + (d <= 0.0 ? -0.5 : 0.5));
				if (i_138_ == 0 && segment.timeIn < 0L)
					segment.timeIn = -segment.timeIn;
			}
			segment.speedIn = f_141_;
			path.add(segment);
		}
		for (int i_142_ = 0; i_142_ < path.size() - 1; i_142_++)
		{
			Segment segment = (Segment) path.get(i_142_);
			Segment segment_143_ = (Segment) path.get(i_142_ + 1);
			segment.length = (float) segment.posIn.distance(segment_143_.posIn);
		}
		int i_144_ = 0;
		float f = ((Segment) path.get(i_144_)).length;
		int i_145_;
		for (/**/; i_144_ < path.size() - 1; i_144_ = i_145_)
		{
			i_145_ = i_144_ + 1;
			for (;;)
			{
				Segment segment = (Segment) path.get(i_145_);
				if (segment.speedIn > 0.0F)
					break;
				f += segment.length;
				i_145_++;
			}
			if (i_145_ - i_144_ > 1)
			{
				float f_146_ = ((Segment) path.get(i_144_)).length;
				float f_147_ = ((Segment) path.get(i_144_)).speedIn;
				float f_148_ = ((Segment) path.get(i_145_)).speedIn;
				for (int i_149_ = i_144_ + 1; i_149_ < i_145_; i_149_++)
				{
					Segment segment = (Segment) path.get(i_149_);
					float f_150_ = f_146_ / f;
					segment.speedIn = f_147_ * (1.0F - f_150_) + f_148_ * f_150_;
					f += segment.length;
				}
			}
		}
		for (int i_151_ = 0; i_151_ < path.size() - 1; i_151_++)
		{
			Segment segment = (Segment) path.get(i_151_);
			Segment segment_152_ = (Segment) path.get(i_151_ + 1);
			if (segment.timeIn > 0L && segment_152_.timeIn > 0L)
			{
				Segment segment_153_ = new Segment();
				segment_153_.posIn = new Point3d(segment.posIn);
				segment_153_.posIn.add(segment_152_.posIn);
				segment_153_.posIn.scale(0.5);
				segment_153_.timeIn = 0L;
				segment_153_.speedIn = (segment.speedIn + segment_152_.speedIn) * 0.5F;
				path.add(i_151_ + 1, segment_153_);
			}
		}
		for (int i_154_ = 0; i_154_ < path.size() - 1; i_154_++)
		{
			Segment segment = (Segment) path.get(i_154_);
			Segment segment_155_ = (Segment) path.get(i_154_ + 1);
			segment.length = (float) segment.posIn.distance(segment_155_.posIn);
		}
		Segment segment = (Segment) path.get(0);
		boolean bool = segment.timeIn != 0L;
		long l = segment.timeIn;
		for (int i_156_ = 0; i_156_ < path.size() - 1; i_156_++)
		{
			Segment segment_157_ = (Segment) path.get(i_156_);
			Segment segment_158_ = (Segment) path.get(i_156_ + 1);
			segment_157_.posOut = new Point3d(segment_158_.posIn);
			segment_158_.posIn = segment_157_.posOut;
			float f_159_ = segment_157_.speedIn;
			float f_160_ = segment_158_.speedIn;
			float f_161_ = (f_159_ + f_160_) * 0.5F;
			if (bool)
			{
				segment_157_.speedIn = 0.0F;
				segment_157_.speedOut = f_160_;
				float f_162_ = 2.0F * segment_157_.length / f_160_ * 1000.0F + 0.5F;
				segment_157_.timeIn = l;
				segment_157_.timeOut = segment_157_.timeIn + (long) (int) f_162_;
				l = segment_157_.timeOut;
				bool = false;
			}
			else if (segment_158_.timeIn == 0L)
			{
				segment_157_.speedIn = f_159_;
				segment_157_.speedOut = f_160_;
				float f_163_ = segment_157_.length / f_161_ * 1000.0F + 0.5F;
				segment_157_.timeIn = l;
				segment_157_.timeOut = segment_157_.timeIn + (long) (int) f_163_;
				l = segment_157_.timeOut;
				bool = false;
			}
			else
			{
				if (segment_158_.timeIn > 0L)
				{
					float f_164_ = segment_157_.length / f_161_ * 1000.0F + 0.5F;
					long l_165_ = l + (long) (int) f_164_;
					if (l_165_ >= segment_158_.timeIn)
						segment_158_.timeIn = 0L;
					else
					{
						segment_157_.speedIn = f_159_;
						segment_157_.speedOut = 0.0F;
						float f_166_ = (2.0F * segment_157_.length / f_159_ * 1000.0F + 0.5F);
						segment_157_.timeIn = l;
						segment_157_.timeOut = segment_157_.timeIn + (long) (int) f_166_;
						l = segment_158_.timeIn;
						bool = true;
						continue;
					}
				}
				if (segment_158_.timeIn == 0L)
				{
					segment_157_.speedIn = f_159_;
					segment_157_.speedOut = f_160_;
					float f_167_ = segment_157_.length / f_161_ * 1000.0F + 0.5F;
					segment_157_.timeIn = l;
					segment_157_.timeOut = segment_157_.timeIn + (long) (int) f_167_;
					l = segment_157_.timeOut;
					bool = false;
				}
				else
				{
					segment_157_.speedIn = f_159_;
					segment_157_.speedOut = 0.0F;
					float f_168_ = 2.0F * segment_157_.length / f_159_ * 1000.0F + 0.5F;
					segment_157_.timeIn = l;
					segment_157_.timeOut = segment_157_.timeIn + (long) (int) f_168_;
					l = segment_157_.timeOut + -segment_158_.timeIn;
					bool = true;
				}
			}
		}
		path.remove(path.size() - 1);
	}

	public void printPath(String string)
	{
		System.out.println("------------ Path: " + string + "  #:" + path.size());
		for (int i = 0; i < path.size(); i++)
		{
			Segment segment = (Segment) path.get(i);
			System.out.println(" " + i + ":  len=" + segment.length + " spdIn=" + segment.speedIn + " spdOut=" + segment.speedOut + " tmIn=" + segment.timeIn + " tmOut=" + segment.timeOut);
			java.lang.System.out.println("posIn=" + segment.posIn + " posOut=" + segment.posOut);
		}
		System.out.println("------------");
	}

	public void align()
	{
		pos.getAbs(p);
		p.z = Engine.land().HQ(p.x, p.y) - (double) bodyDepth;
		pos.setAbs(p);
	}

	private boolean computeInterpolatedDPR(long l)
	{
		if (tmInterpoStart >= tmInterpoEnd || l >= tmInterpoEnd)
		{
			bodyDepth = bodyDepth1;
			bodyPitch = bodyPitch1;
			bodyRoll = bodyRoll1;
			return false;
		}
		if (l <= tmInterpoStart)
		{
			bodyDepth = bodyDepth0;
			bodyPitch = bodyPitch0;
			bodyRoll = bodyRoll0;
			return true;
		}
		float f = ((float) (l - tmInterpoStart) / (float) (tmInterpoEnd - tmInterpoStart));
		bodyDepth = bodyDepth0 + (bodyDepth1 - bodyDepth0) * f;
		bodyPitch = bodyPitch0 + (bodyPitch1 - bodyPitch0) * f;
		bodyRoll = bodyRoll0 + (bodyRoll1 - bodyRoll0) * f;
		return true;
	}

	private void setMovablePosition(long l)
	{
		// TODO: Added by |ZUTI|: if new reported time is smaller than old one, set effects intensity to 0
		// because if they are set to 1, long long white lines are drawn when ship is positioned backwards.
		// ---------------------------------------------------------
		if (l < zutiLastKnownServerTime)
		{
			zutiDrawWakeEffects = false;
			// System.out.println("  FALSE!");
		}
		else
		{
			zutiDrawWakeEffects = true;
			// System.out.println("  TRUE!");
		}

		zutiLastKnownServerTime = l;
		// System.out.println("  " + zutiLastKnownServerTime);
		// System.out.println("=====================================");
		// ---------------------------------------------------------

		if (cachedSeg < 0)
			cachedSeg = 0;
		else if (cachedSeg >= path.size())
			cachedSeg = path.size() - 1;
		Segment segment = (Segment) path.get(cachedSeg);
		if (segment.timeIn <= l && l <= segment.timeOut)
		{
			SetEffectsIntens(1.0F);
			setMovablePosition((float) (l - segment.timeIn) / (float) (segment.timeOut - segment.timeIn));
		}
		else if (l > segment.timeOut)
		{
			while (cachedSeg + 1 < path.size())
			{
				Segment segment_169_ = (Segment) path.get(++cachedSeg);
				if (l <= segment_169_.timeIn)
				{
					SetEffectsIntens(0.0F);
					setMovablePosition(0.0F);
					return;
				}
				if (l <= segment_169_.timeOut)
				{
					SetEffectsIntens(1.0F);
					setMovablePosition((float) (l - segment_169_.timeIn) / (float) (segment_169_.timeOut - segment_169_.timeIn));
					return;
				}
			}
			SetEffectsIntens(-1.0F);
			setMovablePosition(1.0F);
		}
		else
		{
			while (cachedSeg > 0)
			{
				Segment segment_170_ = (Segment) path.get(--cachedSeg);
				if (l >= segment_170_.timeOut)
				{
					SetEffectsIntens(0.0F);
					setMovablePosition(1.0F);
					return;
				}
				if (l >= segment_170_.timeIn)
				{
					SetEffectsIntens(1.0F);
					setMovablePosition((float) (l - segment_170_.timeIn) / (float) (segment_170_.timeOut - segment_170_.timeIn));
					return;
				}
			}
			SetEffectsIntens(0.0F);
			setMovablePosition(0.0F);
		}

	}

	private void setMovablePosition(float f)
	{
		isTurning = false;
		isTurningBackward = false;

		Segment segment = (Segment) path.get(cachedSeg);
		float f_171_ = (float) (segment.timeOut - segment.timeIn) * 0.0010F;
		float f_172_ = segment.speedIn;
		float f_173_ = segment.speedOut;
		float f_174_ = (f_173_ - f_172_) / f_171_;
		f *= f_171_;
		float f_175_ = f_172_ * f + f_174_ * f * f * 0.5F;
		int i = cachedSeg;
		float f_176_ = prop.SLIDER_DIST - (segment.length - f_175_);
		if (f_176_ <= 0.0F)
			p1.interpolate(segment.posIn, segment.posOut, (f_175_ + prop.SLIDER_DIST) / segment.length);
		else
		{
			isTurning = true;
			for (;;)
			{
				if (i + 1 >= path.size())
				{
					p1.interpolate(segment.posIn, segment.posOut, 1.0F + f_176_ / segment.length);
					break;
				}
				segment = (Segment) path.get(++i);
				if (f_176_ <= segment.length)
				{
					p1.interpolate(segment.posIn, segment.posOut, f_176_ / segment.length);
					break;
				}
				f_176_ -= segment.length;
			}
		}
		i = cachedSeg;
		segment = (Segment) path.get(i);
		f_176_ = prop.SLIDER_DIST - f_175_;
		if (f_176_ <= 0.0F)
			p2.interpolate(segment.posIn, segment.posOut, (f_175_ - prop.SLIDER_DIST) / segment.length);
		else
		{
			isTurning = true;
			isTurningBackward = true;

			for (;;)
			{
				if (i <= 0)
				{
					p2.interpolate(segment.posIn, segment.posOut, 0.0F - f_176_ / segment.length);
					break;
				}
				segment = (Segment) path.get(--i);
				if (f_176_ <= segment.length)
				{
					p2.interpolate(segment.posIn, segment.posOut, 1.0F - f_176_ / segment.length);
					break;
				}
				f_176_ -= segment.length;
			}
		}

		if (!com.maddox.il2.game.Mission.isDogfight() && !isTurning && mustRecomputePath && (double) f_176_ < -1.5D * (double) prop.SLIDER_DIST)
		{
			computeNewPath();
			mustRecomputePath = false;
		}

		p.interpolate(p1, p2, 0.5F);
		tmpvd.sub(p1, p2);
		if (tmpvd.lengthSquared() < 0.0010000000474974513)
		{
			Segment segment_177_ = (Segment) path.get(cachedSeg);
			tmpvd.sub(segment_177_.posOut, segment_177_.posIn);
		}
		float f_178_ = (float) (Math.atan2(tmpvd.y, tmpvd.x) * 57.29577951308232);
		setPosition(p, f_178_);
	}

	public void addRockingSpeed(Vector3d vector3d, Vector3d vector3d_179_, Point3d point3d)
	{
		tmpV.sub(point3d, pos.getAbsPoint());
		o.transformInv(tmpV);
		tmpV.cross(W, tmpV);
		o.transform(tmpV);
		vector3d.add(tmpV);
		vector3d_179_.set(N);
	}

	private void setPosition(Point3d point3d, float f)
	{
		shipYaw = f;
		float f_180_ = ((float) (NetServerParams.getServerTime() % (long) rollPeriod) / (float) rollPeriod);
		float f_181_ = 0.05F * (20.0F - Math.abs(bodyPitch));
		if (f_181_ < 0.0F)
			f_181_ = 0.0F;
		float f_182_ = rollAmp * f_181_ * (float) Math.sin((double) (f_180_ * 2.0F) * 3.141592653589793);
		W.x = (-rollWAmp * (double) f_181_ * Math.cos((double) (f_180_ * 2.0F) * 3.141592653589793));
		f_180_ = (float) (NetServerParams.getServerTime() % (long) pitchPeriod) / (float) pitchPeriod;
		float f_183_ = pitchAmp * f_181_ * (float) Math.sin((double) (f_180_ * 2.0F) * 3.141592653589793);
		W.y = (-pitchWAmp * (double) f_181_ * Math.cos((double) (f_180_ * 2.0F) * 3.141592653589793));
		o.setYPR(shipYaw, bodyPitch + f_183_, bodyRoll + f_182_);
		N.set(0.0, 0.0, 1.0);
		o.transform(N);
		initOr.setYPR(shipYaw, bodyPitch, bodyRoll);
		point3d.z = (double) -bodyDepth;
		pos.setAbs(point3d, o);
		initLoc.set(point3d, initOr);
	}

	private void setPosition()
	{
		o.setYPR(shipYaw, bodyPitch, bodyRoll);
		N.set(0.0, 0.0, 1.0);
		o.transform(N);
		pos.setAbs(o);
		align();
		initLoc.set(pos.getAbs());
	}

	public int WeaponsMask()
	{
		return prop.WEAPONS_MASK;
	}

	public int HitbyMask()
	{
		return prop.HITBY_MASK;
	}

	public int chooseBulletType(BulletProperties[] bulletpropertieses)
	{
		if (dying != 0)
			return -1;
		if (bulletpropertieses.length == 1)
			return 0;
		if (bulletpropertieses.length <= 0)
			return -1;
		if (bulletpropertieses[0].power <= 0.0F)
			return 0;
		if (bulletpropertieses[1].power <= 0.0F)
			return 1;
		if (bulletpropertieses[0].cumulativePower > 0.0F)
			return 0;
		if (bulletpropertieses[1].cumulativePower > 0.0F)
			return 1;
		if (bulletpropertieses[0].powerType == 0)
			return 0;
		if (bulletpropertieses[1].powerType == 0)
			return 1;
		return bulletpropertieses[0].powerType != 1 ? 0 : 1;
	}

	public int chooseShotpoint(BulletProperties bulletproperties)
	{
		if (dying != 0)
			return -1;
		if (numshotpoints <= 0)
			return -1;
		return shotpoints[Rnd(0, numshotpoints - 1)];
	}

	public boolean getShotpointOffset(int i, Point3d point3d)
	{
		if (dying != 0)
			return false;
		if (numshotpoints <= 0)
			return false;
		if (i == 0)
		{
			if (point3d != null)
				point3d.set(0.0, 0.0, 0.0);
			return true;
		}
		int i_184_ = i - 1;
		if (i_184_ >= parts.length || i_184_ < 0)
			return false;
		if (parts[i_184_].state == 2)
			return false;
		if (!parts[i_184_].pro.isItLifeKeeper() && !parts[i_184_].pro.haveGun())
			return false;
		if (point3d != null)
			point3d.set(parts[i_184_].shotpointOffs);
		return true;
	}

	public float AttackMaxDistance()
	{
		return prop.ATTACK_MAX_DISTANCE;
	}

	private void send_DeathCommand(Actor actor, NetChannel netchannel)
	{
		if (isNetMaster())
		{
			if (netchannel == null)
			{
				if (Mission.isDeathmatch())
				{
					float f = Mission.respawnTime("Bigship");
					respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
				}
				else
					respawnDelay = 0L;
			}
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			try
			{
				netmsgguaranted.writeByte(68);
				netmsgguaranted.writeLong(timeOfDeath);
				netmsgguaranted.writeNetObj(actor != null ? actor.net : null);
				long l = Time.tickNext();
				long l_185_ = 0L;
				boolean bool = dying == 1;
				double d = (double) (bool ? bodyDepth1 : bodyDepth0) / 1000.0;
				if (d <= 0.0)
					d = 0.0;
				if (d >= 1.0)
					d = 1.0;
				int i = (int) (d * 32767.0);
				if (i > 32767)
					i = 32767;
				if (i < 0)
					i = 0;
				netmsgguaranted.writeShort(i);
				d = (double) (bool ? bodyPitch1 : bodyPitch0) / 90.0;
				if (d <= -1.0)
					d = -1.0;
				if (d >= 1.0)
					d = 1.0;
				i = (int) (d * 32767.0);
				if (i > 32767)
					i = 32767;
				if (i < -32767)
					i = -32767;
				netmsgguaranted.writeShort(i);
				d = (double) (bool ? bodyRoll1 : bodyRoll0) / 90.0;
				if (d <= -1.0)
					d = -1.0;
				if (d >= 1.0)
					d = 1.0;
				i = (int) (d * 32767.0);
				if (i > 32767)
					i = 32767;
				if (i < -32767)
					i = -32767;
				netmsgguaranted.writeShort(i);
				d = (double) (tmInterpoEnd - tmInterpoStart) / 1000.0 / 1200.0;
				if (bool)
					l_185_ = l - tmInterpoStart;
				else
					d = 0.0;
				if (d <= 0.0)
					d = 0.0;
				if (d >= 1.0)
					d = 1.0;
				i = (int) (d * 32767.0);
				if (i > 32767)
					i = 32767;
				if (i < 0)
					i = 0;
				netmsgguaranted.writeShort(i);
				d = (double) sink2Depth / 1000.0;
				if (d <= 0.0)
					d = 0.0;
				if (d >= 1.0)
					d = 1.0;
				i = (int) (d * 32767.0);
				if (i > 32767)
					i = 32767;
				if (i < 0)
					i = 0;
				netmsgguaranted.writeShort(i);
				d = (double) sink2Pitch / 90.0;
				if (d <= -1.0)
					d = -1.0;
				if (d >= 1.0)
					d = 1.0;
				i = (int) (d * 32767.0);
				if (i > 32767)
					i = 32767;
				if (i < -32767)
					i = -32767;
				netmsgguaranted.writeShort(i);
				d = (double) sink2Roll / 90.0;
				if (d <= -1.0)
					d = -1.0;
				if (d >= 1.0)
					d = 1.0;
				i = (int) (d * 32767.0);
				if (i > 32767)
					i = 32767;
				if (i < -32767)
					i = -32767;
				netmsgguaranted.writeShort(i);
				d = ((double) (sink2timeWhenStop - tmInterpoEnd) / 1000.0 / 1200.0);
				if (!bool)
				{
					d = ((double) (tmInterpoEnd - tmInterpoStart) / 1000.0 / 1200.0);
					l_185_ = l - tmInterpoStart;
				}
				if (d <= 0.0)
					d = 0.0;
				if (d >= 1.0)
					d = 1.0;
				i = (int) (d * 32767.0);
				if (i > 32767)
					i = 32767;
				if (i < 0)
					i = 0;
				netmsgguaranted.writeShort(i);
				if (netchannel != null)
					netmsgguaranted.writeLong(l_185_);
				if (netchannel == null)
					net.post(netmsgguaranted);
				else
					net.postTo(netchannel, netmsgguaranted);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
		}
	}

	private void send_RespawnCommand()
	{
		if (isNetMaster() && Mission.isDeathmatch())
		{
			NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			try
			{
				netmsgguaranted.writeByte(82);
				net.post(netmsgguaranted);
			}
			catch (Exception exception)
			{
				System.out.println(exception.getMessage());
				exception.printStackTrace();
			}
			netsendPartsState_needtosend = false;
		}
	}

	private void send_bufferized_FireCommand()
	{
		if (isNetMaster())
		{
			long l = NetServerParams.getServerTime();
			long l_186_ = (long) Rnd(40, 85);
			if (Math.abs(l - netsendFire_lasttimeMS) >= l_186_)
			{
				netsendFire_lasttimeMS = l;
				if (!net.isMirrored())
				{
					for (int i = 0; i < arms.length; i++)
						arms[i].enemy = null;
					netsendFire_armindex = 0;
				}
				else
				{
					int i = 0;
					int i_187_ = 0;
					int i_188_;
					for (i_188_ = 0; i_188_ < arms.length; i_188_++)
					{
						int i_189_ = netsendFire_armindex + i_188_;
						if (i_189_ >= arms.length)
							i_189_ -= arms.length;
						if (arms[i_189_].enemy != null)
						{
							if (parts[arms[i_189_].part_idx].state != 0)
							{
								System.out.println("*** BigShip internal error #0");
								arms[i_189_].enemy = null;
							}
							else if (!Actor.isValid(arms[i_189_].enemy) || !arms[i_189_].enemy.isNet())
								arms[i_189_].enemy = null;
							else
							{
								if (i >= 15)
									break;
								netsendFire_tmpbuff[i].gun_idx = i_189_;
								netsendFire_tmpbuff[i].enemy = arms[i_189_].enemy;
								netsendFire_tmpbuff[i].timeWhenFireS = arms[i_189_].timeWhenFireS;
								netsendFire_tmpbuff[i].shotpointIdx = arms[i_189_].shotpointIdx;
								arms[i_189_].enemy = null;
								if (arms[i_189_].timeWhenFireS < 0.0)
									i_187_++;
								i++;
							}
						}
					}
					for (netsendFire_armindex += i_188_; netsendFire_armindex >= arms.length; netsendFire_armindex -= arms.length)
					{
						/* empty */
					}
					if (i > 0)
					{
						NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
						try
						{
							netmsgfiltered.writeByte(224 + i_187_);
							for (int i_190_ = 0; i_190_ < i; i_190_++)
							{
								double d = (netsendFire_tmpbuff[i_190_].timeWhenFireS);
								if (d < 0.0)
								{
									netmsgfiltered.writeByte(netsendFire_tmpbuff[i_190_].gun_idx);
									netmsgfiltered.writeNetObj(netsendFire_tmpbuff[i_190_].enemy.net);
									netmsgfiltered.writeByte(netsendFire_tmpbuff[i_190_].shotpointIdx);
									i_187_--;
								}
							}
						}
						catch (IOException ioexception)
						{
							/* empty */
						}
						if (i_187_ != 0)
							System.out.println("*** BigShip internal error #5");
						else
						{
							try
							{
								for (int i_191_ = 0; i_191_ < i; i_191_++)
								{
									double d = (netsendFire_tmpbuff[i_191_].timeWhenFireS);
									if (d >= 0.0)
									{
										double d_192_ = (double) l * 0.0010;
										double d_193_ = (d - d_192_) * 1000.0;
										if (d_193_ <= -2000.0)
											d_193_ = -2000.0;
										if (d_193_ >= 5000.0)
											d_193_ = 5000.0;
										d_193_ = (d_193_ - -2000.0) / 7000.0;
										int i_194_ = (int) (d_193_ * 255.0);
										if (i_194_ < 0)
											i_194_ = 0;
										if (i_194_ > 255)
											i_194_ = 255;
										netmsgfiltered.writeByte(i_194_);
										netmsgfiltered.writeByte(netsendFire_tmpbuff[i_191_].gun_idx);
										netmsgfiltered.writeNetObj(netsendFire_tmpbuff[i_191_].enemy.net);
										netmsgfiltered.writeByte(netsendFire_tmpbuff[i_191_].shotpointIdx);
										netsendFire_tmpbuff[i_191_].enemy = null;
									}
								}
								netmsgfiltered.setIncludeTime(true);
								net.post(l, netmsgfiltered);
							}
							catch (Exception exception)
							{
								System.out.println(exception.getMessage());
								exception.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	private void computeNewPath()
	{
		if (path == null || dying != 0 || com.maddox.il2.game.Mission.isDogfight())
			return;
		Segment segment = (Segment) path.get(cachedSeg);
		long l = 0L;
		long l1 = com.maddox.rts.Time.tickNext();
		if (com.maddox.il2.game.Mission.isCoop() || com.maddox.il2.game.Mission.isDogfight())
			l1 = com.maddox.il2.net.NetServerParams.getServerTime();
		if ((segment.timeIn > l1 || !isTurning) && (segment.speedIn > CURRSPEED || segment.speedOut > CURRSPEED))
		{
			if (com.maddox.il2.game.Mission.isCoop())
				mustSendSpeedToNet = true;
			float f = 0.0F;
			if (l1 >= segment.timeIn)
			{
				long l2 = segment.timeOut - segment.timeIn;
				long l4 = l1 - segment.timeIn;
				float f1 = segment.speedOut - segment.speedIn;
				f = segment.speedIn + f1 * (float) ((double) l4 / (double) l2);
			}
			if (f > CURRSPEED)
				segment.speedIn = CURRSPEED;
			else
				segment.speedIn = f;
			if (segment.speedOut > CURRSPEED)
				segment.speedOut = CURRSPEED;
			com.maddox.JGP.Point3d point3d = new Point3d();
			point3d.x = initLoc.getX();
			point3d.y = initLoc.getY();
			point3d.z = initLoc.getZ();
			segment.posIn.set(point3d);
			if (segment.timeIn < l1)
				segment.timeIn = l1;
			double d = segment.posIn.distance(segment.posOut);
			l = segment.timeOut;
			segment.timeOut = segment.timeIn + (long) (1000D * ((2D * d) / (double) java.lang.Math.abs(segment.speedOut + segment.speedIn)));
			segment.length = (float) d;
		}
		else
		{
			l = segment.timeOut;
		}
		if (isTurningBackward && (segment.speedIn > CURRSPEED || segment.speedOut > CURRSPEED))
			mustRecomputePath = true;
		int i = cachedSeg;
		for (i++; i <= path.size() - 1; i++)
		{
			Segment segment1 = (Segment) path.get(i);
			long l3 = segment1.timeIn - l;
			segment1.timeIn = segment.timeOut + l3;
			segment1.posIn = segment.posOut;
			if (segment1.speedIn > CURRSPEED)
			{
				if (com.maddox.il2.game.Mission.isCoop())
					mustSendSpeedToNet = true;
				segment1.speedIn = CURRSPEED;
			}
			if (segment1.speedOut > CURRSPEED)
			{
				if (com.maddox.il2.game.Mission.isCoop())
					mustSendSpeedToNet = true;
				segment1.speedOut = CURRSPEED;
			}
			l = segment1.timeOut;
			segment1.timeOut = segment1.timeIn + (long) (1000D * ((2D * (double) segment1.length) / (double) java.lang.Math.abs(segment1.speedOut + segment1.speedIn)));
			segment = segment1;
		}
	}

	private void send_bufferized_PartsState()
	{
		if (!isNetMaster())
			return;
		if (!netsendPartsState_needtosend)
			return;
		long l = com.maddox.il2.net.NetServerParams.getServerTime();
		long l1 = com.maddox.il2.objects.ships.BigshipGeneric.Rnd(NETSEND_MIN_DELAY_MS_PARTSSTATE, NETSEND_MAX_DELAY_MS_PARTSSTATE);
		if (java.lang.Math.abs(l - netsendPartsState_lasttimeMS) < l1)
			return;
		netsendPartsState_lasttimeMS = l;
		netsendPartsState_needtosend = false;
		if (!net.isMirrored())
			return;
		com.maddox.rts.NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		try
		{
			netmsgguaranted.writeByte(83);

			if (!com.maddox.il2.game.Mission.isDogfight())
			{

				int i = 127;
				if (path != null && CURRSPEED < prop.SPEED)
				{

					i = java.lang.Math.round(CURRSPEED);
					if (i < 0)
						i = 0;
					if (i > 126)
						i = 126;
				}
				netmsgguaranted.writeByte(i);
			}
			int j = (parts.length + 3) / 4;
			int k = 0;
			for (int i1 = 0; i1 < j; i1++)
			{
				int j1 = 0;
				for (int k1 = 0; k1 < 4; k1++)
				{
					if (k < parts.length)
					{
						int i2 = parts[k].state;

						j1 |= i2 << k1 * 2;
					}

					k++;
				}

				netmsgguaranted.writeByte(j1);
			}

			net.post(netmsgguaranted);
		}
		catch (java.lang.Exception exception)
		{
			java.lang.System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	private void mirror_send_speed()
	{
		if (!isNetMirror())
			return;
		if (net.masterChannel() instanceof com.maddox.rts.NetChannelInStream)
			return;
		if (!com.maddox.il2.game.Mission.isCoop())
			return;
		com.maddox.rts.NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		try
		{
			netmsgguaranted.writeByte(86);
			int i = 127;
			if (path != null && CURRSPEED < prop.SPEED)
			{
				i = java.lang.Math.round(CURRSPEED);
				if (i < 0)
					i = 0;
				if (i > 126)
					i = 126;
			}
			netmsgguaranted.writeByte(i);
			net.postTo(net.masterChannel(), netmsgguaranted);
		}
		catch (java.lang.Exception exception)
		{
			java.lang.System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	private void bufferize_FireCommand(int i, Actor actor, int i_201_, float f)
	{
		if (isNetMaster() && net.isMirrored() && (Actor.isValid(actor) && actor.isNet()) && (arms[i].enemy == null || !(arms[i].timeWhenFireS >= 0.0)))
		{
			i_201_ &= 0xff;
			arms[i].enemy = actor;
			arms[i].shotpointIdx = i_201_;
			if (f < 0.0F)
				arms[i].timeWhenFireS = -1.0;
			else
				arms[i].timeWhenFireS = ((double) f + (double) NetServerParams.getServerTime() * 0.0010);
		}
	}

	private void mirror_send_bufferized_Damage()
	{
		if (isNetMirror() && !(net.masterChannel() instanceof NetChannelInStream))
		{
			long l = NetServerParams.getServerTime();
			long l_202_ = (long) Rnd(65, 115);
			if (Math.abs(l - netsendDmg_lasttimeMS) >= l_202_)
			{
				netsendDmg_lasttimeMS = l;
				try
				{
					int i = 0;
					NetMsgFiltered netmsgfiltered = null;
					int i_203_;
					for (i_203_ = 0; i_203_ < parts.length; i_203_++)
					{
						int i_204_ = netsendDmg_partindex + i_203_;
						if (i_204_ >= parts.length)
							i_204_ -= parts.length;
						if (parts[i_204_].state != 2 && !((double) parts[i_204_].damage < 0.0078125))
						{
							int i_205_ = (int) (parts[i_204_].damage * 128.0F);
							if (--i_205_ < 0)
								i_205_ = 0;
							else if (i_205_ > 127)
								i_205_ = 127;
							if (parts[i_204_].damageIsFromRight)
								i_205_ |= 0x80;
							if (i <= 0)
							{
								netmsgfiltered = new NetMsgFiltered();
								netmsgfiltered.writeByte(80);
							}
							Actor actor = parts[i_204_].mirror_initiator;
							if (!Actor.isValid(actor) || !actor.isNet())
								actor = null;
							parts[i_204_].mirror_initiator = null;
							parts[i_204_].damage = 0.0F;
							netmsgfiltered.writeByte(i_204_);
							netmsgfiltered.writeByte(i_205_);
							netmsgfiltered.writeNetObj(actor != null ? actor.net : null);
							if (++i >= NETSEND_MAX_NUMITEMS_DMG)
								break;
						}
					}
					for (netsendDmg_partindex += i_203_; netsendDmg_partindex >= parts.length; netsendDmg_partindex -= parts.length);
					if (i > 0)
					{
						netmsgfiltered.setIncludeTime(false);
						net.postTo(l, net.masterChannel(), netmsgfiltered);
					}
				}
				catch (Exception exception)
				{
					System.out.println(exception.getMessage());
					exception.printStackTrace();
				}
			}
		}
	}

	public void requestLocationOnCarrierDeck(com.maddox.il2.net.NetUser netuser, java.lang.String s)
	{
		if (!isNetMirror())
			return;
		try
		{
			com.maddox.rts.NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(93);
			netmsgguaranted.writeNetObj(netuser);
			netmsgguaranted.writeUTF(s);
			net.postTo(net.masterChannel(), netmsgguaranted);
		}
		catch (java.lang.Exception exception)
		{
			java.lang.System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}

	private void handleLocationRequest(com.maddox.il2.net.NetUser netuser, java.lang.String s)
	{
		try
		{
			Class class1 = ObjIO.classForName(s);
			Object obj = class1.newInstance();
			Aircraft aircraft = (Aircraft) obj;

			String s1 = Property.stringValue(aircraft.getClass(), "FlightModel", null);
			aircraft.FM = new FlightModel(s1);
			aircraft.FM.Gears.set(aircraft.hierMesh());
			Aircraft.forceGear(aircraft.getClass(), aircraft.hierMesh(), 1.0F);
			aircraft.FM.Gears.computePlaneLandPose(aircraft.FM);
			Aircraft.forceGear(aircraft.getClass(), aircraft.hierMesh(), 0.0F);
			if (airport != null)
			{
				Loc loc = airport.requestCell(aircraft);
				postLocationToMirror(netuser, loc);
			}
			aircraft.FM = null;
			aircraft.destroy();
			aircraft = null;
		}
		catch (java.lang.Exception exception)
		{
			exception.printStackTrace();
		}
	}

	private void postLocationToMirror(com.maddox.il2.net.NetUser netuser, com.maddox.il2.engine.Loc loc)
	{
		com.maddox.rts.NetChannel netchannel;
		netchannel = null;
		java.util.List list = com.maddox.rts.NetEnv.channels();
		int i = 0;
		do
		{
			if (i >= list.size())
				break;
			netchannel = (com.maddox.rts.NetChannel) list.get(i);
			com.maddox.rts.NetObj netobj = netchannel.getMirror(netuser.idRemote());
			if (netuser == netobj)
				break;
			netchannel = null;
			i++;
		}
		while (true);
		if (netchannel == null)
			return;
		try
		{
			com.maddox.rts.NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
			netmsgguaranted.writeByte(93);
			netmsgguaranted.writeDouble(loc.getX());
			netmsgguaranted.writeDouble(loc.getY());
			netmsgguaranted.writeDouble(loc.getZ());
			netmsgguaranted.writeFloat(loc.getAzimut());
			netmsgguaranted.writeFloat(loc.getTangage());
			netmsgguaranted.writeFloat(loc.getKren());
			net.postTo(netchannel, netmsgguaranted);
		}
		catch (java.io.IOException ioexception)
		{
			ioexception.printStackTrace();
		}
		return;
	}

	public void createNetObject(NetChannel netchannel, int i)
	{
		if (netchannel == null)
			net = new Master(this);
		else
			net = new Mirror(this, netchannel, i);
	}

	public void netFirstUpdate(NetChannel netchannel) throws IOException
	{
		NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
		netmsgguaranted.writeByte(73);
		netmsgguaranted.writeLong(-1L);
		net.postTo(netchannel, netmsgguaranted);
		if (dying == 0)
			master_sendDrown(bodyDepth1, bodyPitch1, bodyRoll1, ((float) (tmInterpoEnd - NetServerParams.getServerTime()) * 1000.0F));
		else
			send_DeathCommand(null, netchannel);
		netsendPartsState_needtosend = true;
	}

	public float getReloadingTime(Aim aim)
	{
		return SLOWFIRE_K * GetGunProperties(aim).DELAY_AFTER_SHOOT;
	}

	public float chainFireTime(Aim aim)
	{
		float f = GetGunProperties(aim).CHAINFIRE_TIME;
		return f > 0.0F ? f * Rnd(0.75F, 1.25F) : 0.0F;
	}

	public float probabKeepSameEnemy(Actor actor)
	{
		return 0.75F;
	}

	public float minTimeRelaxAfterFight()
	{
		return 0.1F;
	}

	public void gunStartParking(Aim aim)
	{
		FiringDevice firingdevice = GetFiringDevice(aim);
		ShipPartProperties shippartproperties = parts[firingdevice.part_idx].pro;
		aim.setRotationForParking(firingdevice.headYaw, firingdevice.gunPitch, shippartproperties.HEAD_STD_YAW, shippartproperties.GUN_STD_PITCH, shippartproperties.HEAD_YAW_RANGE,
				shippartproperties.HEAD_MAX_YAW_SPEED, shippartproperties.GUN_MAX_PITCH_SPEED);
	}

	public void gunInMove(boolean bool, Aim aim)
	{
		FiringDevice firingdevice = GetFiringDevice(aim);
		float f = aim.t();
		float f_206_ = aim.anglesYaw.getDeg(f);
		float f_207_ = aim.anglesPitch.getDeg(f);
		setGunAngles(firingdevice, f_206_, f_207_);
		pos.inValidate(false);
	}

	public Actor findEnemy(Aim aim)
	{
		if (isNetMirror())
			return null;
		ShipPartProperties shippartproperties = GetGunProperties(aim);
		switch (shippartproperties.ATTACK_FAST_TARGETS)
		{
			case 0:
				NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, KmHourToMSec(100.0F));
			break;
			case 1:
				NearestEnemies.set(shippartproperties.WEAPONS_MASK);
			break;
			default:
				NearestEnemies.set(shippartproperties.WEAPONS_MASK, KmHourToMSec(100.0F), 9999.9F);
		}
		Actor actor = NearestEnemies.getAFoundEnemy(pos.getAbsPoint(), (double) (shippartproperties.ATTACK_MAX_RADIUS), getArmy());
		if (actor == null)
			return null;
		if (!(actor instanceof Prey))
		{
			System.out.println("bigship: nearest enemies: non-Prey");
			return null;
		}
		FiringDevice firingdevice = GetFiringDevice(aim);
		BulletProperties bulletproperties = null;
		if (firingdevice.gun.prop != null)
		{
			int i = ((Prey) actor).chooseBulletType(firingdevice.gun.prop.bullet);
			if (i < 0)
				return null;
			bulletproperties = firingdevice.gun.prop.bullet[i];
		}
		int i = ((Prey) actor).chooseShotpoint(bulletproperties);
		if (i < 0)
			return null;
		aim.shotpoint_idx = i;
		return actor;
	}

	public boolean enterToFireMode(int i, Actor actor, float f, Aim aim)
	{
		if (!isNetMirror())
		{
			FiringDevice firingdevice = GetFiringDevice(aim);
			bufferize_FireCommand(firingdevice.gun_idx, actor, aim.shotpoint_idx, i != 0 ? f : -1.0F);
		}
		return true;
	}

	private void Track_Mirror(int i, Actor actor, int i_208_)
	{
		if (actor != null && (arms != null && i >= 0 && i < arms.length && arms[i].aime != null) && parts[arms[i].part_idx].state == 0)
			arms[i].aime.passive_StartFiring(0, actor, i_208_, 0.0F);
	}

	private void Fire_Mirror(int i, Actor actor, int i_209_, float f)
	{
		if (actor != null && (arms != null && i >= 0 && i < arms.length && arms[i].aime != null) && parts[arms[i].part_idx].state == 0)
		{
			if (f <= 0.15F)
				f = 0.15F;
			if (f >= 7.0F)
				f = 7.0F;
			arms[i].aime.passive_StartFiring(1, actor, i_209_, f);
		}
	}

	public int targetGun(Aim aim, Actor actor, float f, boolean bool)
	{
		if (!Actor.isValid(actor) || !actor.isAlive() || actor.getArmy() == 0)
			return 0;
		FiringDevice firingdevice = GetFiringDevice(aim);
		if (firingdevice.gun instanceof CannonMidrangeGeneric)
		{
			int i = ((Prey) actor).chooseBulletType(firingdevice.gun.prop.bullet);
			if (i < 0)
				return 0;
			((CannonMidrangeGeneric) firingdevice.gun).setBulletType(i);
		}
		boolean bool_210_ = ((Prey) actor).getShotpointOffset(aim.shotpoint_idx, p1);
		if (!bool_210_)
			return 0;
		ShipPartProperties shippartproperties = parts[firingdevice.part_idx].pro;
		float f_211_ = f * Rnd(0.8F, 1.2F);
		if (!Aimer.Aim((BulletAimer) firingdevice.gun, actor, this, f_211_, p1, shippartproperties.fireOffset))
			return 0;
		Point3d point3d = new Point3d();
		Aimer.GetPredictedTargetPosition(point3d);
		Point3d point3d_212_ = Aimer.GetHunterFirePoint();
		float f_213_ = 0.05F;
		double d = point3d.distance(point3d_212_);
		double d_214_ = point3d.z;
		point3d.sub(point3d_212_);
		point3d.scale(Rnd(0.995, 1.005));
		point3d.add(point3d_212_);
		if (f_211_ > 0.0010F)
		{
			Point3d point3d_215_ = new Point3d();
			actor.pos.getAbs(point3d_215_);
			tmpvd.sub(point3d, point3d_215_);
			double d_216_ = tmpvd.length();
			if (d_216_ > 0.0010)
			{
				float f_217_ = (float) d_216_ / f_211_;
				if (f_217_ > 200.0F)
					f_217_ = 200.0F;
				float f_218_ = f_217_ * 0.01F;
				point3d_215_.sub(point3d_212_);
				double d_219_ = (point3d_215_.x * point3d_215_.x + point3d_215_.y * point3d_215_.y + point3d_215_.z * point3d_215_.z);
				if (d_219_ > 0.01)
				{
					float f_220_ = (float) tmpvd.dot(point3d_215_);
					f_220_ /= (float) (d_216_ * Math.sqrt(d_219_));
					f_220_ = (float) Math.sqrt((double) (1.0F - f_220_ * f_220_));
					f_218_ *= 0.4F + 0.6F * f_220_;
				}
				f_218_ *= 1.3F;
				f_218_ *= Aim.AngleErrorKoefForSkill[SKILL_IDX];
				int i = Mission.curCloudsType();
				if (i > 2)
				{
					float f_221_ = i <= 4 ? 800.0F : 400.0F;
					float f_222_ = (float) (d / (double) f_221_);
					if (f_222_ > 1.0F)
					{
						if (f_222_ > 10.0F)
							return 0;
						f_222_ = (f_222_ - 1.0F) / 9.0F;
						f_218_ *= f_222_ + 1.0F;
					}
				}
				if (i >= 3 && d_214_ > (double) Mission.curCloudsHeight())
					f_218_ *= 1.25F;
				f_213_ += f_218_;
			}
		}
		if (World.Sun().ToSun.z < -0.15F)
		{
			float f_223_ = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
			if (f_223_ >= 1.0F)
				f_223_ = 1.0F;
			if (actor instanceof Aircraft && (NetServerParams.getServerTime() - ((Aircraft) actor).tmSearchlighted) < 1000L)
				f_223_ = 0.0F;
			f_213_ += 10.0F * f_223_;
		}
		float f_224_ = (float) actor.getSpeed(null) - 10.0F;
		if (f_224_ > 0.0F)
		{
			float f_225_ = 83.33334F;
			f_224_ = f_224_ < f_225_ ? f_224_ / f_225_ : 1.0F;
			f_213_ += f_224_ * shippartproperties.FAST_TARGETS_ANGLE_ERROR;
		}
		Vector3d vector3d = new Vector3d();
		if (!((BulletAimer) firingdevice.gun).FireDirection(point3d_212_, point3d, vector3d))
			return 0;
		float f_226_;
		if (bool)
		{
			f_226_ = 99999.0F;
			d_214_ = 99999.0;
		}
		else
		{
			f_226_ = shippartproperties.HEAD_MAX_YAW_SPEED;
			d_214_ = (double) shippartproperties.GUN_MAX_PITCH_SPEED;
		}
		o.add(shippartproperties.fireOrient, pos.getAbs().getOrient());
		int i = aim.setRotationForTargeting(this, o, point3d_212_, firingdevice.headYaw, firingdevice.gunPitch, vector3d, f_213_, f_211_, shippartproperties.HEAD_YAW_RANGE,
				shippartproperties.GUN_MIN_PITCH, shippartproperties.GUN_MAX_PITCH, f_226_, (float) d_214_, 0.0F);
		return i;
	}

	public void singleShot(Aim aim)
	{
		FiringDevice firingdevice = GetFiringDevice(aim);
		if (!parts[firingdevice.part_idx].pro.TRACKING_ONLY)
			firingdevice.gun.shots(1);
	}

	public void startFire(Aim aim)
	{
		FiringDevice firingdevice = GetFiringDevice(aim);
		if (!parts[firingdevice.part_idx].pro.TRACKING_ONLY)
			firingdevice.gun.shots(-1);
	}

	public void continueFire(Aim aim)
	{
		/* empty */
	}

	public void stopFire(Aim aim)
	{
		FiringDevice firingdevice = GetFiringDevice(aim);
		if (!parts[firingdevice.part_idx].pro.TRACKING_ONLY)
			firingdevice.gun.shots(0);
	}

	public boolean isVisibilityLong()
	{
		return true;
	}

	private void createAirport()
	{
		if (prop.propAirport != null)
		{
			prop.propAirport.firstInit(this);
			draw = new TowStringMeshDraw(draw);
			if (prop.propAirport.cellTO != null)
				cellTO = (CellAirField) prop.propAirport.cellTO.getClone();
			if (prop.propAirport.cellLDG != null)
				cellLDG = (CellAirField) prop.propAirport.cellLDG.getClone();
			airport = new AirportCarrier(this, prop.propAirport.rwy);
		}
	}

	public AirportCarrier getAirport()
	{
		return airport;
	}

	public CellAirField getCellTO()
	{
		return cellTO;
	}

	public CellAirField getCellLDR()
	{
		return cellLDG;
	}

	private void validateTowAircraft()
	{
		if (towPortNum >= 0)
		{
			if (!Actor.isValid(towAircraft))
				requestDetowAircraft(towAircraft);
			else if (pos.getAbsPoint().distance(towAircraft.pos.getAbsPoint()) > (double) hierMesh().visibilityR())
				requestDetowAircraft(towAircraft);
			else if (!towAircraft.FM.CT.bHasArrestorControl)
				requestDetowAircraft(towAircraft);
		}
	}

	public void forceTowAircraft(Aircraft aircraft, int i)
	{
		if (towPortNum < 0)
		{
			towPortNum = i;
			towAircraft = aircraft;
			towHook = new HookNamed(aircraft, "_ClipAGear");
		}
	}

	public void requestTowAircraft(Aircraft aircraft)
	{
		if (towPortNum < 0 && prop.propAirport.towPRel != null)
		{
			HookNamed hooknamed = new HookNamed(aircraft, "_ClipAGear");
			Point3d[] point3ds = prop.propAirport.towPRel;
			Point3d point3d_1 = new Point3d();
			Point3d point3d_2 = new Point3d();
			Point3d point3d_3 = new Point3d();
			Point3d point3d_4 = new Point3d();
			Loc loc = new Loc();
			Loc loc_230_ = new Loc();
			for (int i = 0; i < point3ds.length / 2; i++)
			{
				pos.getCurrent(loc);
				point3d_3.set(point3ds[i + i]);
				loc.transform(point3d_3);
				point3d_4.set(point3ds[i + i + 1]);
				loc.transform(point3d_4);
				aircraft.pos.getCurrent(loc_230_);
				loc.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
				hooknamed.computePos(aircraft, loc_230_, loc);
				point3d_1.set(loc.getPoint());
				aircraft.pos.getPrev(loc_230_);
				loc.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
				hooknamed.computePos(aircraft, loc_230_, loc);
				point3d_2.set(loc.getPoint());
				if (point3d_2.z < (point3d_3.z + 0.5 * (point3d_4.z - point3d_3.z) + 0.2))
				{
					Line2d line2d = new Line2d(new Point2d(point3d_3.x, point3d_3.y), new Point2d(point3d_4.x, point3d_4.y));
					Line2d line2d_231_ = new Line2d(new Point2d(point3d_1.x, point3d_1.y), new Point2d(point3d_2.x, point3d_2.y));
					do
					{
						try
						{
							Point2d point2d = line2d.crossPRE(line2d_231_);
							double d_1 = Math.min(point3d_3.x, point3d_4.x);
							double d_2 = Math.max(point3d_3.x, point3d_4.x);
							double d_3 = Math.min(point3d_3.y, point3d_4.y);
							double d_4 = Math.max(point3d_3.y, point3d_4.y);
							if (!(point2d.x >= d_1) || !(point2d.x <= d_2) || !(point2d.y >= d_3) || !(point2d.y <= d_4))
								break;
							double d_5 = Math.min(point3d_1.x, point3d_2.x);
							double d_6 = Math.max(point3d_1.x, point3d_2.x);
							double d_7 = Math.min(point3d_1.y, point3d_2.y);
							double d_8 = Math.max(point3d_1.y, point3d_2.y);
							if (!(point2d.x >= d_5) || !(point2d.x <= d_6) || !(point2d.y >= d_7) || !(point2d.y <= d_8))
								break;
							towPortNum = i;
							towAircraft = aircraft;
							towHook = new HookNamed(aircraft, "_ClipAGear");
						}
						catch (Exception exception)
						{
					        // TODO: +++ Arrestor Hook Debugging
						    exception.printStackTrace();
						    // ---
							break;
						}
						return;
					}
					while (false);
				}
			}
		}
	}

	public void requestDetowAircraft(Aircraft aircraft)
	{
		if (aircraft == towAircraft)
		{
			towAircraft = null;
			towPortNum = -1;
		}
	}

	public boolean isTowAircraft(Aircraft aircraft)
	{
		return towAircraft == aircraft;
	}

	public double getSpeed(Vector3d vector3d)
	{
		if (path == null)
			return super.getSpeed(vector3d);
		long l = NetServerParams.getServerTime();
		if (l > (long) (Time.tickLen() * 4))
			return super.getSpeed(vector3d);
		Segment segment = (Segment) path.get(0);
		tmpDir.sub(segment.posOut, segment.posIn);
		tmpDir.normalize();
		tmpDir.scale((double) segment.speedIn);
		if (vector3d != null)
			vector3d.set(tmpDir);
		return tmpDir.length();
	}

	public void hideTransparentRunwayRed()
	{
		hierMesh().chunkVisible("Red", false);
	}

	public void showTransparentRunwayRed()
    {
        hierMesh().chunkVisible("Red", true);
    }
	
	static
	{
		netsendFire_tmpbuff = new TmpTrackOrFireInfo[31];
		for (int i = 0; i < netsendFire_tmpbuff.length; i++)
			netsendFire_tmpbuff[i] = new TmpTrackOrFireInfo();
	}

	// TODO: |ZUTI| methods and variables
	// ----------------------------------------------------------------
	public BornPlace	zutiBornPlace				= null;
	protected ArrayList	zutiFrontMarkers			= null;
	protected boolean	zutiIsClassBussy			= false;
	// I need this one because ALL ships are NOT recognized as chiefs, even though some are. Very strange.
	protected boolean	zutiIsShipChief				= false;
	private boolean		zutiReportResources			= true;
	private String		zutiShipName				= null;
	private boolean		zutiReportFinalDestination	= false;
	private int			zutiLastPointX				= -1;
	private int			zutiLastPointY				= -1;
	private boolean		zutiDrawWakeEffects			= true;
	private long		zutiLastKnownServerTime		= 0;

	/**
	 * True if ship is front mark carrier.
	 * 
	 * @return
	 */
	public boolean zutiHasFrontMarkerAssigned()
	{
		if (zutiFrontMarkers == null || zutiFrontMarkers.size() == 0)
			return false;

		return true;
	}

	/**
	 * Add front marker to ship.
	 * 
	 * @param marker
	 */
	public void zutiAddFrontMarker(Marker marker)
	{
		if (zutiFrontMarkers == null)
			zutiFrontMarkers = new ArrayList(1);

		zutiFrontMarkers.add(marker);
	}

	/**
	 * True if ship is moving or stationary.
	 * 
	 * @return
	 */
	public boolean zutiIsShipChief()
	{
		return zutiIsShipChief;
	}

	/**
	 * Method returns value of dying variable.
	 */
	public int zutiGetDying()
	{
		return dying;
	}

	/**
	 * This method checks object path variable. If it is null or it's size is 0
	 * then true is returned otherwise object is moving one.
	 * 
	 * @return
	 */
	public boolean zutiIsStatic()
	{
		if (path != null && path.size() > 0)
			return false;

		return true;
	}

	private void zutiHasShipReachedFinalDestination()
	{
		Point3d p = pos.getAbsPoint();
		//System.out.println("(" + (int)p.x + "," + (int)p.y + ") vs (" + (int)zutiLastPointX + "," + (int)zutiLastPointY + ")" );
		int resultX = (int)Math.abs(p.x - zutiLastPointX);
		int resultY = (int)Math.abs(p.y - zutiLastPointY);
		if (resultX < 40 && resultY < 40)
		{
			zutiReportResources = false;
			// System.out.println(zutiShipName + " has reached it's final destination.");

			ZutiSupportMethods_ResourcesManagement.addResourcesFromMovingRRRObjects(zutiShipName, p, this.getArmy(), 1.0F, true);
		}
	}
	
	public void zutiResetFrontMarkers()
	{
		if( zutiFrontMarkers != null )
			zutiFrontMarkers.clear();
	}
	// ----------------------------------------------------
	
	
    // Patch Pack 107, additional Debugging Settings
    private static int debugLevel = Integer.MIN_VALUE;
    private static final int DEBUG_NONE = 0;
    static final int DEBUG_NORMAL = 1;
    static final int DEBUG_DETAILED = 2;
    private static final int DEBUG_EXTREME = 3;
    private static final int DEBUG_DEFAULT = DEBUG_NONE;
    
    private static int curDebugLevel() {
        if (debugLevel == Integer.MIN_VALUE) debugLevel = Config.cur.ini.get("Mods", "DEBUG_BSG", DEBUG_DEFAULT);
        return debugLevel;
    }
    
    public static void printDebugMessage(int minLogLevel, String theMessage) {
        if (curDebugLevel() < minLogLevel) return;
        if (curDebugLevel() == DEBUG_EXTREME) {
            printDebug(theMessage);
            return;
        }
        System.out.println(theMessage);
    }

    public static void printDebug(String theMessage) {
        if (curDebugLevel() == 0) return;
        System.out.println(theMessage);
        Exception e = new Exception("BigshipGeneric Debugger Stacktrace");
        System.out.println("Stacktrace follows:");
        e.printStackTrace();
    }
    // ---

}