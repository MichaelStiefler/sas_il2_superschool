package com.maddox.il2.objects.ships;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Line2d;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Aimer;
import com.maddox.il2.ai.AirportCarrier;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MissileAimer;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.StrengthProperties;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.CellAirField;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.ShipAim;
import com.maddox.il2.ai.ground.ShipHunterInterface;
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
import com.maddox.il2.engine.GunGeneric;
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
import com.maddox.il2.game.ZutiStayPoint;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.ObjectsLogLevel;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.weapons.CannonMidrangeGeneric;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.il2.objects.weapons.RocketBomb;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class BigshipGeneric extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Predator, ActorAlign, ShipHunterInterface, VisibilityLong {
    public static class ShipPartProperties {

        public boolean isItLifeKeeper() {
            return this.dmgDepth >= 0.0F;
        }

        public boolean haveGun() {
            return this.gun_idx >= 0;
        }

        public boolean haveRadar() {
            return this.radar_idx >= 0;
        }

        public String             baseChunkName;
        public int                baseChunkIdx;
        public Point3f            partOffs;
        public float              partR;
        public String             additCollisChunkName[];
        public int                additCollisChunkIdx[];
        public StrengthProperties stre;
        public float              dmgDepth;
        public float              dmgPitch;
        public float              dmgRoll;
        public float              dmgTime;
        public float              BLACK_DAMAGE;
        public int                gun_idx;
        public int                radar_idx;
        public Class              gunClass;
        public String             missileName;
        public int                WEAPONS_MASK;
        public boolean            TRACKING_ONLY;
        public boolean            NOW_RADAR;
        public int                TypeOfTarget;
        public int                LauncherType;
        public float              ATTACK_MAX_DISTANCE;
        public float              ATTACK_MIN_DISTANCE;
        public float              ATTACK_MAX_RADIUS;
        public float              ATTACK_MAX_HEIGHT;
        public float              ATTACK_MIN_HEIGHT;
        public int                ATTACK_FAST_TARGETS;
        public float              FAST_TARGETS_ANGLE_ERROR;
        public boolean            PREFER_SLOW_TARGET;
        public AnglesRange        HEAD_YAW_RANGE;
        public AnglesRange        NOFIRE_YAW_RANGE;
        public AnglesRange        SELF_YAW_RANGE;
        public AnglesRange        NOSELF_YAW_RANGE;
        public float              HEAD_STD_YAW;
        public float              _HEAD_MIN_YAW;
        public float              _HEAD_MAX_YAW;
        public float              _NOFIRE_MIN_YAW;
        public float              _NOFIRE_MAX_YAW;
        public boolean            NOFIRE_FLAG;
        public boolean            NOHEADING_FLAG;
        public float              _SELF_YAW;
        public float              GUN_MIN_PITCH;
        public float              GUN_STD_PITCH;
        public float              GUN_MAX_PITCH;
        public float              HEAD_MAX_YAW_SPEED;
        public float              GUN_MAX_PITCH_SPEED;
        public float              DELAY_AFTER_SHOOT;
        public float              DELAY_BEFORE_FIRST_SHOOT;
        public float              CHAINFIRE_TIME;
        public String             headChunkName;
        public String             gunChunkName;
        public int                headChunkIdx;
        public int                gunChunkIdx;
        public Point3d            fireOffset;
        public Orient             fireOrient;
        public String             gunShellStartHookName;

        public ShipPartProperties() {
            this.baseChunkName = null;
            this.baseChunkIdx = -1;
            this.partOffs = null;
            this.partR = 1.0F;
            this.additCollisChunkName = null;
            this.additCollisChunkIdx = null;
            this.stre = new StrengthProperties();
            this.dmgDepth = -1F;
            this.dmgPitch = 0.0F;
            this.dmgRoll = 0.0F;
            this.dmgTime = 1.0F;
            this.BLACK_DAMAGE = 0.0F;
            this.gunClass = null;
            this.missileName = null;
            this.WEAPONS_MASK = 4;
            this.TRACKING_ONLY = false;
            this.NOW_RADAR = false;
            this.TypeOfTarget = 0;
            this.LauncherType = 0;
            this.ATTACK_MAX_DISTANCE = 1.0F;
            this.ATTACK_MIN_DISTANCE = 1.0F;
            this.ATTACK_MAX_RADIUS = 1.0F;
            this.ATTACK_MAX_HEIGHT = 1.0F;
            this.ATTACK_MIN_HEIGHT = 0.0F;
            this.ATTACK_FAST_TARGETS = 1;
            this.FAST_TARGETS_ANGLE_ERROR = 0.0F;
            this.PREFER_SLOW_TARGET = false;
            this.HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            this.NOFIRE_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            this.SELF_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            this.NOSELF_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            this.HEAD_STD_YAW = 0.0F;
            this._HEAD_MIN_YAW = -1F;
            this._HEAD_MAX_YAW = -1F;
            this._NOFIRE_MIN_YAW = -1F;
            this._NOFIRE_MAX_YAW = -1F;
            this.NOFIRE_FLAG = false;
            this.NOHEADING_FLAG = false;
            this.GUN_MIN_PITCH = -20F;
            this.GUN_STD_PITCH = -18F;
            this.GUN_MAX_PITCH = -15F;
            this.HEAD_MAX_YAW_SPEED = 720F;
            this.GUN_MAX_PITCH_SPEED = 60F;
            this.DELAY_AFTER_SHOOT = 1.0F;
            this.DELAY_BEFORE_FIRST_SHOOT = 0.0F;
            this.CHAINFIRE_TIME = 0.0F;
            this.headChunkName = null;
            this.gunChunkName = null;
            this.headChunkIdx = -1;
            this.gunChunkIdx = -1;
            this.gunShellStartHookName = null;
        }
    }

    public static class ShipProperties {

        public String             meshName;
        public String             soundName;
        public int                WEAPONS_MASK;
        public int                HITBY_MASK;
        public float              ATTACK_MAX_DISTANCE;
        public float              SLIDER_DIST;
        public float              SPEED;
        public float              DELAY_RESPAWN_MIN;
        public float              DELAY_RESPAWN_MAX;
        public float              STABILIZE_FACTOR;
        public ShipPartProperties propparts[];
        public int                nGuns;
        public int                nRadars;
        public AirportProperties  propAirport;
        public String             typicalPlaneClass;

        public ShipProperties() {
            this.meshName = null;
            this.soundName = null;
            this.WEAPONS_MASK = 4;
            this.HITBY_MASK = -2;
            this.ATTACK_MAX_DISTANCE = 1.0F;
            this.SLIDER_DIST = 1.0F;
            this.SPEED = 1.0F;
            this.DELAY_RESPAWN_MIN = 15F;
            this.DELAY_RESPAWN_MAX = 30F;
            this.STABILIZE_FACTOR = 1.0F;
            this.propparts = null;
            this.propAirport = null;
            this.typicalPlaneClass = null;
        }
    }

    public static class TmpTrackOrFireInfo {

        private int    gun_idx;
        private Actor  enemy;
        private double timeWhenFireS;
        private int    shotpointIdx;

        public TmpTrackOrFireInfo() {
        }
    }

    public static class RadarDevice {

        /* private */ int radar_idx;
        private int       part_idx;
        private float     headYaw;

        public RadarDevice() {
        }
    }

    public static class FiringDevice {

        private int       gun_idx;
        private int       part_idx;
        private Gun       gun;
        private String    missileName;
        private int       typeOfMissile;
        /* private */ int missileLauncherType;
        private ShipAim   aime;
        private float     headYaw;
        private float     gunPitch;
        private Actor     enemy;
        private double    timeWhenFireS;
        private int       shotpointIdx;

        public FiringDevice() {
        }
    }

    public static class Part {

        private float      damage;
        private Actor      mirror_initiator;
        private Point3d    shotpointOffs;
        private boolean    damageIsFromRight;
        private int        state;
        ShipPartProperties pro;

        public Part() {
            this.shotpointOffs = new Point3d();
            this.damageIsFromRight = false;
        }
    }

    private static class Segment {

        public Point3d posIn;
        public Point3d posOut;
        public float   length;
        public long    timeIn;
        public long    timeOut;
        public float   speedIn;
        public float   speedOut;
        public boolean slidersOn;

        private Segment() {
        }

    }

    public static class Pipe {

        private Eff3DActor pipe;
        private int        part_idx;

        public Pipe() {
            this.pipe = null;
            this.part_idx = -1;
        }
    }

    static class HookNamedZ0 extends HookNamed {

        public void computePos(Actor actor, Loc loc, Loc loc1) {
            super.computePos(actor, loc, loc1);
            loc1.getPoint().z = 0.25D;
        }

        public HookNamedZ0(ActorMesh actormesh, String s) {
            super(actormesh, s);
        }

        public HookNamedZ0(Mesh mesh, String s) {
            super(mesh, s);
        }
    }

    class Move extends Interpolate {

        public boolean tick() {
            BigshipGeneric.this.validateTowAircraft();
            if (BigshipGeneric.this.dying == 0) {
                long l = Time.tickNext();
                if (Mission.isCoop() || Mission.isDogfight()) {
                    l = NetServerParams.getServerTime() + Time.tickLen();
                }
                if (BigshipGeneric.this.path != null) {
                    BigshipGeneric.this.computeInterpolatedDPR(l);
                    BigshipGeneric.this.setMovablePosition(l);
                } else if (BigshipGeneric.this.computeInterpolatedDPR(l)) {
                    BigshipGeneric.this.setPosition();
                }
                boolean flag = false;
                for (int j = 0; j < BigshipGeneric.this.prop.nRadars; j++) {
                    if (BigshipGeneric.this.parts[BigshipGeneric.this.radars[j].part_idx].state == 0) {
                        float f = BigshipGeneric.this.parts[BigshipGeneric.this.radars[j].part_idx].pro.HEAD_MAX_YAW_SPEED * Time.tickLenFs();
                        BigshipGeneric.this.rotateRadar(BigshipGeneric.this.radars[j], f);
                    }
                }

                BigshipGeneric.this.radarTmr++;
                if (BigshipGeneric.this.radarTmr > BigshipGeneric.SecsToTicks(1.0F)) {
                    BigshipGeneric.this.radarTmr = 0L;
                }
                if (BigshipGeneric.this.bHasBlastDeflectorControl) {
                    if (BigshipGeneric.bLogDetailBD) {
                        System.out.println("BigShip: 1173 - bHasBlastDeflectorControl=true");
                    }
                    for (int k = 0; k < 4; k++) {
                        BigshipGeneric.this.blastDeflector[k] = BigshipGeneric.this.filter(Time.tickLenFs(), BigshipGeneric.this.BlastDeflectorControl[k], BigshipGeneric.this.blastDeflector[k], 999.9F, BigshipGeneric.this.dvBlastDeflector);
                        if (Math.abs(BigshipGeneric.this.blastDeflector_[k] - BigshipGeneric.this.blastDeflector[k]) <= 0.0005F) {
                            continue;
                        }
                        BigshipGeneric.this.moveBlastDeflector(k, BigshipGeneric.this.blastDeflector_[k] = BigshipGeneric.this.blastDeflector[k]);
                        if (BigshipGeneric.bLogDetailBD) {
                            System.out.println("BigShip: 1180 - moveBlastDeflector(iii=" + Integer.toString(k) + ", blastDeflector[iii]" + Float.toString(BigshipGeneric.this.blastDeflector[k]) + ")");
                        }
                    }

                }
                if (BigshipGeneric.this.bHasMirrorLA) {
                    if (!BigshipGeneric.this.bInitDoneMirrorLA) {
                        BigshipGeneric.this.InitMirrorLandingAid();
                    }
                    BigshipGeneric.this.MlaUpdate();
                }
                if (BigshipGeneric.this.bHasUSIflols) {
                    if (!BigshipGeneric.this.bInitDoneUSIflols) {
                        BigshipGeneric.this.InitUSIflols();
                    }
                    BigshipGeneric.this.USIflolsUpdate();
                }
                if (BigshipGeneric.this.bHasFRFlols) {
                    if (!BigshipGeneric.this.bInitDoneFRFlols) {
                        BigshipGeneric.this.InitFRFlols();
                    }
                    BigshipGeneric.this.FRFlolsUpdate();
                }
                if (BigshipGeneric.this.wakeupTmr == 0L) {
                    for (int i1 = 0; i1 < BigshipGeneric.this.prop.nGuns; i1++) {
                        if (BigshipGeneric.this.parts[BigshipGeneric.this.arms[i1].part_idx].state == 0) {
                            BigshipGeneric.this.arms[i1].aime.tick_();
                            flag = true;
                        }
                    }

                } else {
                    int j1 = 0;
                    do {
                        if (j1 >= BigshipGeneric.this.prop.nGuns) {
                            break;
                        }
                        if (BigshipGeneric.this.parts[BigshipGeneric.this.arms[j1].part_idx].state == 0) {
                            flag = true;
                            break;
                        }
                        j1++;
                    } while (true);
                    if (BigshipGeneric.this.wakeupTmr > 0L) {
                        BigshipGeneric.this.wakeupTmr--;
                    } else if (++BigshipGeneric.this.wakeupTmr == 0L) {
                        if (BigshipGeneric.this.isAnyEnemyNear()) {
                            BigshipGeneric.this.wakeupTmr = BigshipGeneric.SecsToTicks(BigshipGeneric.Rnd(BigshipGeneric.this.DELAY_WAKEUP, BigshipGeneric.this.DELAY_WAKEUP * 1.2F));
                        } else {
                            BigshipGeneric.this.wakeupTmr = -BigshipGeneric.SecsToTicks(BigshipGeneric.Rnd(4F, 7F));
                        }
                    }
                }
                if (flag) {
                    BigshipGeneric.this.send_bufferized_FireCommand();
                }
                if (BigshipGeneric.this.isNetMirror()) {
                    BigshipGeneric.this.mirror_send_bufferized_Damage();
                    if (Mission.isCoop() && BigshipGeneric.this.mustSendSpeedToNet) {
                        BigshipGeneric.this.mirror_send_speed();
                        BigshipGeneric.this.mustSendSpeedToNet = false;
                    }
                } else if (BigshipGeneric.this.netsendPartsState_needtosend) {
                    BigshipGeneric.this.send_bufferized_PartsState();
                }
                BigshipGeneric.this.zutiRefreshBornPlace();
                return true;
            }
            if (BigshipGeneric.this.dying == 3) {
                BigshipGeneric.this.zutiRefreshBornPlace();
                if ((BigshipGeneric.this.path != null) || !Mission.isDeathmatch()) {
                    BigshipGeneric.this.eraseGuns();
                    return false;
                }
                if (BigshipGeneric.this.respawnDelay-- > 0L) {
                    return true;
                }
                if (!BigshipGeneric.this.isNetMaster()) {
                    BigshipGeneric.this.respawnDelay = 10000L;
                    return true;
                } else {
                    BigshipGeneric.this.wakeupTmr = 0L;
                    BigshipGeneric.this.radarTmr = 0L;
                    BigshipGeneric.this.makeLive();
                    BigshipGeneric.this.forgetAllAiming();
                    BigshipGeneric.this.appearSailorsMats();
                    BigshipGeneric.this.setDefaultLivePose();
                    BigshipGeneric.this.setDiedFlag(false);
                    BigshipGeneric.this.tmInterpoStart = BigshipGeneric.this.tmInterpoEnd = 0L;
                    BigshipGeneric.this.bodyDepth = BigshipGeneric.this.bodyPitch = BigshipGeneric.this.bodyRoll = 0.0F;
                    BigshipGeneric.this.bodyDepth0 = BigshipGeneric.this.bodyPitch0 = BigshipGeneric.this.bodyRoll0 = 0.0F;
                    BigshipGeneric.this.bodyDepth1 = BigshipGeneric.this.bodyPitch1 = BigshipGeneric.this.bodyRoll1 = 0.0F;
                    BigshipGeneric.this.setPosition();
                    BigshipGeneric.this.pos.reset();
                    BigshipGeneric.this.send_RespawnCommand();
                    return true;
                }
            }
            if (BigshipGeneric.this.netsendPartsState_needtosend) {
                BigshipGeneric.this.send_bufferized_PartsState();
            }
            long l1 = NetServerParams.getServerTime();
            if (BigshipGeneric.this.dying == 1) {
                BigshipGeneric.this.timeOfSailorsDisappear--;
                if (!BigshipGeneric.this.bSailorsDisappear && (BigshipGeneric.this.timeOfSailorsDisappear < 0L)) {
                    BigshipGeneric.this.disappearSailorsMats();
                }
                if (BigshipGeneric.this.bHasMirrorLA) {
                    BigshipGeneric.this.MlaSetVisible(false);
                }
                if (BigshipGeneric.this.bHasUSIflols) {
                    BigshipGeneric.this.USIflolsSetVisible(false);
                }
                if (BigshipGeneric.this.bHasFRFlols) {
                    BigshipGeneric.this.FRFlolsSetVisible(false);
                }
                if (l1 >= BigshipGeneric.this.tmInterpoEnd) {
                    BigshipGeneric.this.bodyDepth0 = BigshipGeneric.this.bodyDepth1;
                    BigshipGeneric.this.bodyPitch0 = BigshipGeneric.this.bodyPitch1;
                    BigshipGeneric.this.bodyRoll0 = BigshipGeneric.this.bodyRoll1;
                    BigshipGeneric.this.bodyDepth1 = BigshipGeneric.this.sink2Depth;
                    BigshipGeneric.this.bodyPitch1 = BigshipGeneric.this.sink2Pitch;
                    BigshipGeneric.this.bodyRoll1 = BigshipGeneric.this.sink2Roll;
                    BigshipGeneric.this.tmInterpoStart = BigshipGeneric.this.tmInterpoEnd;
                    BigshipGeneric.this.tmInterpoEnd = BigshipGeneric.this.sink2timeWhenStop;
                    BigshipGeneric.this.dying = 2;
                }
            } else if (l1 >= BigshipGeneric.this.tmInterpoEnd) {
                BigshipGeneric.this.bodyDepth0 = BigshipGeneric.this.bodyDepth1 = BigshipGeneric.this.sink2Depth;
                BigshipGeneric.this.bodyPitch0 = BigshipGeneric.this.bodyPitch1 = BigshipGeneric.this.sink2Pitch;
                BigshipGeneric.this.bodyRoll0 = BigshipGeneric.this.bodyRoll1 = BigshipGeneric.this.sink2Roll;
                BigshipGeneric.this.tmInterpoStart = BigshipGeneric.this.tmInterpoEnd = 0L;
                BigshipGeneric.this.dying = 3;
            }
            if (((Time.tickCounter() & 0x63) == 0) && (BigshipGeneric.this.dsmoks != null)) {
                for (int i = 0; i < BigshipGeneric.this.dsmoks.length; i++) {
                    if ((BigshipGeneric.this.dsmoks[i] != null) && (BigshipGeneric.this.dsmoks[i].pipe != null) && (BigshipGeneric.this.dsmoks[i].pipe.pos.getAbsPoint().z < -4.891D)) {
                        Eff3DActor.finish(BigshipGeneric.this.dsmoks[i].pipe);
                        BigshipGeneric.this.dsmoks[i].pipe = null;
                    }
                }

            }
            BigshipGeneric.this.computeInterpolatedDPR(l1);
            if (BigshipGeneric.this.path != null) {
                BigshipGeneric.this.setMovablePosition(BigshipGeneric.this.timeOfDeath);
            } else {
                BigshipGeneric.this.setPosition();
            }
            BigshipGeneric.this.zutiRefreshBornPlace();
            return true;
        }

        Move() {
        }
    }

    class Master extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                int i = netmsginput.readUnsignedByte();
                if (i == 93) {
                    NetUser netuser = (NetUser) netmsginput.readNetObj();
                    String s = netmsginput.readUTF();
                    BigshipGeneric.this.handleLocationRequest(netuser, s);
                    return true;
                }
                if (i != 86) {
                    return false;
                }
                i = netmsginput.readUnsignedByte();
                float f = i;
                if ((BigshipGeneric.this.path != null) && (i != 127) && (f < BigshipGeneric.this.CURRSPEED)) {
                    BigshipGeneric.this.CURRSPEED = f;
                    if (Mission.isCoop()) {
                        BigshipGeneric.this.computeNewPath();
                        BigshipGeneric.this.netsendPartsState_needtosend = true;
                    }
                }
                return true;
            }
            if (netmsginput.readUnsignedByte() != 80) {
                return false;
            }
            if (BigshipGeneric.this.dying != 0) {
                return true;
            }
            int j = 2 + NetMsgInput.netObjReferenceLen();
            int k = netmsginput.available();
            int l = k / j;
            if ((l <= 0) || (l > 256) || ((k % j) != 0)) {
                System.out.println("*** net bigship1 len:" + k);
                return true;
            }
            do {
                if (--l < 0) {
                    break;
                }
                int i1 = netmsginput.readUnsignedByte();
                if ((i1 < 0) || (i1 >= BigshipGeneric.this.parts.length)) {
                    return true;
                }
                int j1 = netmsginput.readUnsignedByte();
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                if (BigshipGeneric.this.parts[i1].state != 2) {
                    BigshipGeneric.this.parts[i1].damage += ((j1 & 0x7f) + 1) / 128F;
                    BigshipGeneric.this.parts[i1].damageIsFromRight = (j1 & 0x80) != 0;
                    BigshipGeneric.this.InjurePart(i1, actor, true);
                }
            } while (true);
            return true;
        }

        public Master(Actor actor) {
            super(actor);
        }
    }

    class Mirror extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                int i = netmsginput.readUnsignedByte();
                switch (i) {
                    case 93:
                        double d = netmsginput.readDouble();
                        double d1 = netmsginput.readDouble();
                        double d2 = netmsginput.readDouble();
                        float f = netmsginput.readFloat();
                        float f1 = netmsginput.readFloat();
                        float f2 = netmsginput.readFloat();
                        Loc loc = new Loc(d, d1, d2, f, f1, f2);
                        if (BigshipGeneric.this.airport != null) {
                            BigshipGeneric.this.airport.setClientLoc(loc);
                        }
                        return true;

                    case 73:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted);
                        }
                        BigshipGeneric.this.timeOfDeath = netmsginput.readLong();
                        if (BigshipGeneric.this.timeOfDeath < 0L) {
                            if (BigshipGeneric.this.dying == 0) {
                                BigshipGeneric.this.makeLive();
                                BigshipGeneric.this.setDefaultLivePose();
                                BigshipGeneric.this.forgetAllAiming();
                                BigshipGeneric.this.appearSailorsMats();
                            }
                        } else if (BigshipGeneric.this.dying == 0) {
                            BigshipGeneric.this.Die(null, BigshipGeneric.this.timeOfDeath, false, true);
                        }
                        return true;

                    case 82:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted1);
                        }
                        BigshipGeneric.this.makeLive();
                        BigshipGeneric.this.setDefaultLivePose();
                        BigshipGeneric.this.forgetAllAiming();
                        BigshipGeneric.this.appearSailorsMats();
                        BigshipGeneric.this.setDiedFlag(false);
                        BigshipGeneric.this.tmInterpoStart = BigshipGeneric.this.tmInterpoEnd = 0L;
                        BigshipGeneric.this.bodyDepth = BigshipGeneric.this.bodyPitch = BigshipGeneric.this.bodyRoll = 0.0F;
                        BigshipGeneric.this.bodyDepth0 = BigshipGeneric.this.bodyPitch0 = BigshipGeneric.this.bodyRoll0 = 0.0F;
                        BigshipGeneric.this.bodyDepth1 = BigshipGeneric.this.bodyPitch1 = BigshipGeneric.this.bodyRoll1 = 0.0F;
                        BigshipGeneric.this.setPosition();
                        BigshipGeneric.this.pos.reset();
                        return true;

                    case 83:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted2);
                        }
                        int l3 = netmsginput.available();
                        if ((l3 > 0) && !Mission.isDogfight()) {
                            int i4 = netmsginput.readUnsignedByte();
                            float f3 = i4;
                            if ((BigshipGeneric.this.path != null) && (i4 != 127) && (f3 < BigshipGeneric.this.CURRSPEED)) {
                                BigshipGeneric.this.CURRSPEED = f3;
                                BigshipGeneric.this.computeNewPath();
                            }
                            l3--;
                        }
                        int j4 = (BigshipGeneric.this.parts.length + 3) / 4;
                        if (l3 != j4) {
                            System.out.println("*** net bigship S");
                            return true;
                        }
                        if (j4 <= 0) {
                            System.out.println("*** net bigship S0");
                            return true;
                        }
                        int k4 = 0;
                        for (int l4 = 0; l4 < l3; l4++) {
                            int j5 = netmsginput.readUnsignedByte();
                            for (int i6 = 0; (i6 < 4) && (k4 < BigshipGeneric.this.parts.length); i6++) {
                                int j6 = (j5 >>> (i6 * 2)) & 3;
                                if (j6 <= BigshipGeneric.this.parts[k4].state) {
                                    k4++;
                                    continue;
                                }
                                if (j6 == 2) {
                                    BigshipGeneric.this.parts[k4].damage = 0.0F;
                                    BigshipGeneric.this.parts[k4].mirror_initiator = null;
                                }
                                BigshipGeneric.this.parts[k4].state = j6;
                                BigshipGeneric.this.visualsInjurePart(k4, true);
                                k4++;
                            }

                        }

                        return true;

                    case 100:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted3 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted3);
                        }
                        int i5 = netmsginput.available();
                        if (i5 != 8) {
                            System.out.println("*** net bigship d");
                            return true;
                        }
                        if (BigshipGeneric.this.dying != 0) {
                            return true;
                        } else {
                            BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
                            BigshipGeneric.this.bodyDepth0 = BigshipGeneric.this.bodyDepth;
                            BigshipGeneric.this.bodyPitch0 = BigshipGeneric.this.bodyPitch;
                            BigshipGeneric.this.bodyRoll0 = BigshipGeneric.this.bodyRoll;
                            BigshipGeneric.this.bodyDepth1 = (float) (1000D * ((netmsginput.readUnsignedShort() & 0x7fff) / 32767D));
                            BigshipGeneric.this.bodyPitch1 = (float) (90D * (netmsginput.readShort() / 32767D));
                            BigshipGeneric.this.bodyRoll1 = (float) (90D * (netmsginput.readShort() / 32767D));
                            BigshipGeneric.this.tmInterpoStart = BigshipGeneric.this.tmInterpoEnd = NetServerParams.getServerTime();
                            BigshipGeneric.this.tmInterpoEnd += (long) (1000D * (1200D * ((netmsginput.readUnsignedShort() & 0x7fff) / 32767D)));
                            BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
                            return true;
                        }

                    case 68:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted4 = new NetMsgGuaranted(netmsginput, 1);
                            this.post(netmsgguaranted4);
                        }
                        int k5 = netmsginput.available();
                        boolean flag;
                        if (k5 == (8 + NetMsgInput.netObjReferenceLen() + 8 + 8)) {
                            flag = false;
                        } else if (k5 == (8 + NetMsgInput.netObjReferenceLen() + 8 + 8 + 8)) {
                            flag = true;
                        } else {
                            System.out.println("*** net bigship D");
                            return true;
                        }
                        if (BigshipGeneric.this.dying != 0) {
                            return true;
                        }
                        BigshipGeneric.this.timeOfDeath = netmsginput.readLong();
                        if (Mission.isDeathmatch()) {
                            BigshipGeneric.this.timeOfDeath = NetServerParams.getServerTime();
                        }
                        if (BigshipGeneric.this.timeOfDeath < 0L) {
                            System.out.println("*** net bigship D tm");
                            return true;
                        }
                        com.maddox.rts.NetObj netobj2 = netmsginput.readNetObj();
                        Actor actor2 = netobj2 == null ? null : ((ActorNet) netobj2).actor();
                        BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
                        BigshipGeneric.this.bodyDepth0 = BigshipGeneric.this.bodyDepth;
                        BigshipGeneric.this.bodyPitch0 = BigshipGeneric.this.bodyPitch;
                        BigshipGeneric.this.bodyRoll0 = BigshipGeneric.this.bodyRoll;
                        BigshipGeneric.this.bodyDepth1 = (float) (1000D * ((netmsginput.readUnsignedShort() & 0x7fff) / 32767D));
                        BigshipGeneric.this.bodyPitch1 = (float) (90D * (netmsginput.readShort() / 32767D));
                        BigshipGeneric.this.bodyRoll1 = (float) (90D * (netmsginput.readShort() / 32767D));
                        BigshipGeneric.this.tmInterpoStart = BigshipGeneric.this.tmInterpoEnd = NetServerParams.getServerTime();
                        BigshipGeneric.this.tmInterpoEnd += (long) (1000D * (1200D * ((netmsginput.readUnsignedShort() & 0x7fff) / 32767D)));
                        BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
                        BigshipGeneric.this.sink2Depth = (float) (1000D * ((netmsginput.readUnsignedShort() & 0x7fff) / 32767D));
                        BigshipGeneric.this.sink2Pitch = (float) (90D * (netmsginput.readShort() / 32767D));
                        BigshipGeneric.this.sink2Roll = (float) (90D * (netmsginput.readShort() / 32767D));
                        BigshipGeneric.this.sink2timeWhenStop = BigshipGeneric.this.tmInterpoEnd;
                        BigshipGeneric.this.sink2timeWhenStop += (long) (1000D * (1200D * ((netmsginput.readUnsignedShort() & 0x7fff) / 32767D)));
                        if (flag) {
                            long l6 = netmsginput.readLong();
                            if (l6 > 0L) {
                                BigshipGeneric.this.tmInterpoStart -= l6;
                                BigshipGeneric.this.tmInterpoEnd -= l6;
                                BigshipGeneric.this.sink2timeWhenStop -= l6;
                                BigshipGeneric.this.computeInterpolatedDPR(NetServerParams.getServerTime());
                            }
                        }
                        BigshipGeneric.this.Die(actor2, BigshipGeneric.this.timeOfDeath, true, false);
                        return true;
                }
                System.out.println("**net bigship unknown cmd " + i);
                return false;
            }
            int j = netmsginput.readUnsignedByte();
            if ((j & 0xe0) == 224) {
                int k = 1 + NetMsgInput.netObjReferenceLen() + 1;
                int i1 = 2 + NetMsgInput.netObjReferenceLen() + 1;
                int k1 = netmsginput.available();
                int i2 = j & 0x1f;
                int j2 = k1 - (i2 * k);
                int k2 = j2 / i1;
                if ((k2 < 0) || (k2 > 31) || (i2 > 31) || ((j2 % i1) != 0)) {
                    System.out.println("*** net big0 code:" + j + " szT:" + k + " szF:" + i1 + " len:" + k1 + " nT:" + i2 + " lenF:" + j2 + " nF:" + k2);
                    return true;
                }
                if (this.isMirrored()) {
                    this.out.unLockAndSet(netmsginput, i2 + k2);
                    this.out.setIncludeTime(true);
                    this.postReal(Message.currentRealTime(), this.out);
                }
                while (--i2 >= 0) {
                    int l2 = netmsginput.readUnsignedByte();
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj == null ? null : ((ActorNet) netobj).actor();
                    int k3 = netmsginput.readUnsignedByte();
                    BigshipGeneric.this.Track_Mirror(l2, actor, k3);
                }
                while (--k2 >= 0) {
                    int i3 = netmsginput.readUnsignedByte();
                    int j3 = netmsginput.readUnsignedByte();
                    com.maddox.rts.NetObj netobj1 = netmsginput.readNetObj();
                    Actor actor1 = netobj1 == null ? null : ((ActorNet) netobj1).actor();
                    double d3 = -2D + (((i3 / 255D) * 7000D) / 1000D);
                    double d4 = (0.001D * (Message.currentGameTime() - NetServerParams.getServerTime())) + d3;
                    int l5 = netmsginput.readUnsignedByte();
                    BigshipGeneric.this.Fire_Mirror(j3, actor1, l5, (float) d4);
                }
                return true;
            }
            if (j == 80) {
                int l = 2 + NetMsgInput.netObjReferenceLen();
                int j1 = netmsginput.available();
                int l1 = j1 / l;
                if ((l1 <= 0) || (l1 > 256) || ((j1 % l) != 0)) {
                    System.out.println("*** net bigship2 n:" + l1);
                    return true;
                } else {
                    this.out.unLockAndSet(netmsginput, l1);
                    this.out.setIncludeTime(false);
                    this.postRealTo(Message.currentRealTime(), this.masterChannel(), this.out);
                    return true;
                }
            } else {
                System.out.println("**net bigship unknown ng cmd " + j);
                return true;
            }
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    public static class SPAWN implements ActorSpawn {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1) {
            float f2 = sectfile.get(s, s1, -9865.345F);
            if ((f2 == -9865.345F) || (f2 < f) || (f2 > f1)) {
                if (f2 == -9865.345F) {
                    System.out.println("Ship: Value of [" + s + "]:<" + s1 + "> " + "not found");
                } else {
                    System.out.println("Ship: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
                }
                throw new RuntimeException("Can't set property");
            }
            return f2;
        }

        private static String getS(SectFile sectfile, String s, String s1) {
            String s2 = sectfile.get(s, s1);
            if ((s2 == null) || (s2.length() <= 0)) {
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                    System.out.print("Ship: Value of [" + s + "]:<" + s1 + "> not found");
                    throw new RuntimeException("Can't set property");
                } else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
                    System.out.println("Bigship \"" + s + "\" is not (correctly) declared in ships.ini file!");
                }
                return null;
                // ---
            }
            return new String(s2);
        }

        /* private */ static String getS(SectFile sectfile, String s, String s1, String s2) {
            String s3 = sectfile.get(s, s1);
            if ((s3 == null) || (s3.length() <= 0)) {
                return s2;
            } else {
                return new String(s3);
            }
        }

        private static void tryToReadRadarProperties(SectFile sectfile, String s, ShipPartProperties shippartproperties) {
            if (sectfile.exist(s, "NowRadar")) {
                if (sectfile.exist(s, "HeadMaxYawSpeed")) {
                    shippartproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed", -999F, 999F);
                }
            } else {
                System.out.println("BigShip: Can't find NowRadar definition '" + s + "'");
                throw new RuntimeException("Can't register Ship object");
            }
        }

        private static void tryToReadGunProperties(SectFile sectfile, String s, ShipPartProperties shippartproperties) {
            if (sectfile.exist(s, "Gun")) {
                String s1 = "com.maddox.il2.objects.weapons." + getS(sectfile, s, "Gun");
                try {
                    shippartproperties.gunClass = Class.forName(s1);
                } catch (Exception exception) {
                    System.out.println("BigShip: Can't find gun class '" + s1 + "'");
                    throw new RuntimeException("Can't register Ship object");
                }
            }
            if (sectfile.exist(s, "AttackMaxDistance")) {
                shippartproperties.ATTACK_MAX_DISTANCE = getF(sectfile, s, "AttackMaxDistance", 6F, 50000F);
            }
            if (sectfile.exist(s, "AttackMinDistance")) {
                shippartproperties.ATTACK_MIN_DISTANCE = getF(sectfile, s, "AttackMinDistance", 5F, 50000F);
            }
            if (sectfile.exist(s, "AttackMaxRadius")) {
                shippartproperties.ATTACK_MAX_RADIUS = getF(sectfile, s, "AttackMaxRadius", 6F, 50000F);
            }
            if (sectfile.exist(s, "AttackMaxHeight")) {
                shippartproperties.ATTACK_MAX_HEIGHT = getF(sectfile, s, "AttackMaxHeight", 6F, 15000F);
            }
            if (sectfile.exist(s, "AttackMinHeight")) {
                shippartproperties.ATTACK_MIN_HEIGHT = getF(sectfile, s, "AttackMinHeight", 0.0F, 15000F);
            }
            if (sectfile.exist(s, "TrackingOnly")) {
                shippartproperties.TRACKING_ONLY = true;
            }
            if (sectfile.exist(s, "FireFastTargets")) {
                float f = getF(sectfile, s, "FireFastTargets", 0.0F, 2.0F);
                shippartproperties.ATTACK_FAST_TARGETS = (int) (f + 0.5F);
                if (shippartproperties.ATTACK_FAST_TARGETS > 2) {
                    shippartproperties.ATTACK_FAST_TARGETS = 2;
                }
            }
            if (sectfile.exist(s, "FastTargetsAngleError")) {
                float f1 = getF(sectfile, s, "FastTargetsAngleError", 0.0F, 45F);
                shippartproperties.FAST_TARGETS_ANGLE_ERROR = f1;
            }
            if (sectfile.exist(s, "PreferSlowTarget")) {
                shippartproperties.PREFER_SLOW_TARGET = true;
            }
            if (sectfile.exist(s, "HeadMinYaw")) {
                shippartproperties._HEAD_MIN_YAW = getF(sectfile, s, "HeadMinYaw", -360F, 360F);
            }
            if (sectfile.exist(s, "HeadMaxYaw")) {
                shippartproperties._HEAD_MAX_YAW = getF(sectfile, s, "HeadMaxYaw", -360F, 360F);
            }
            if (sectfile.exist(s, "NoFireMinYaw")) {
                shippartproperties._NOFIRE_MIN_YAW = getF(sectfile, s, "NoFireMinYaw", -360F, 360F);
                shippartproperties.NOFIRE_FLAG = true;
            }
            if (sectfile.exist(s, "NoFireMaxYaw")) {
                shippartproperties._NOFIRE_MAX_YAW = getF(sectfile, s, "NoFireMaxYaw", -360F, 360F);
            }
            if (sectfile.exist(s, "GunMinPitch")) {
                shippartproperties.GUN_MIN_PITCH = getF(sectfile, s, "GunMinPitch", -15F, 85F);
            }
            if (sectfile.exist(s, "GunMaxPitch")) {
                shippartproperties.GUN_MAX_PITCH = getF(sectfile, s, "GunMaxPitch", 0.0F, 89.9F);
            }
            if (sectfile.exist(s, "HeadMaxYawSpeed")) {
                shippartproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed", 0.1F, 999F);
            }
            if (sectfile.exist(s, "GunMaxPitchSpeed")) {
                shippartproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, s, "GunMaxPitchSpeed", 0.1F, 999F);
            }
            if (sectfile.exist(s, "DelayAfterShoot")) {
                shippartproperties.DELAY_AFTER_SHOOT = getF(sectfile, s, "DelayAfterShoot", 0.0F, 999F);
            }
            if (sectfile.exist(s, "DelayBeforeFirstShoot")) {
                shippartproperties.DELAY_BEFORE_FIRST_SHOOT = getF(sectfile, s, "DelayBeforeFirstShoot", 0.0F, 999F);
            }
            if (sectfile.exist(s, "ChainfireTime")) {
                shippartproperties.CHAINFIRE_TIME = getF(sectfile, s, "ChainfireTime", 0.0F, 600F);
            }
            if (sectfile.exist(s, "GunHeadChunk")) {
                shippartproperties.headChunkName = getS(sectfile, s, "GunHeadChunk");
            }
            if (sectfile.exist(s, "GunBarrelChunk")) {
                shippartproperties.gunChunkName = getS(sectfile, s, "GunBarrelChunk");
            }
            if (sectfile.exist(s, "GunShellStartHook")) {
                shippartproperties.gunShellStartHookName = getS(sectfile, s, "GunShellStartHook");
            }
        }

        private static void tryToReadMissileProperties(SectFile sectfile, String s, ShipPartProperties shippartproperties) {
            if (BigshipGeneric.bLogDetail) {
                System.out.println("BigShip: coming in tryToReadMissileProperties '" + s + "'.");
            }
            if (sectfile.exist(s, "Missile")) {
                String s1 = "com.maddox.il2.objects.weapons." + getS(sectfile, s, "Missile");
                try {
                    Class.forName(s1);
                } catch (Exception exception) {
                    System.out.println("BigShip: Can't find missile class '" + s1 + "'");
                    throw new RuntimeException("Can't register Ship object");
                }
                shippartproperties.missileName = s1;
            }
            if (sectfile.exist(s, "AttackMaxDistance")) {
                shippartproperties.ATTACK_MAX_DISTANCE = getF(sectfile, s, "AttackMaxDistance", 6F, 50000F);
            }
            if (sectfile.exist(s, "AttackMinDistance")) {
                shippartproperties.ATTACK_MIN_DISTANCE = getF(sectfile, s, "AttackMinDistance", 5F, 50000F);
            }
            if (sectfile.exist(s, "AttackMaxRadius")) {
                shippartproperties.ATTACK_MAX_RADIUS = getF(sectfile, s, "AttackMaxRadius", 6F, 50000F);
            }
            if (sectfile.exist(s, "AttackMaxHeight")) {
                shippartproperties.ATTACK_MAX_HEIGHT = getF(sectfile, s, "AttackMaxHeight", 6F, 15000F);
            }
            if (sectfile.exist(s, "AttackMinHeight")) {
                shippartproperties.ATTACK_MIN_HEIGHT = getF(sectfile, s, "AttackMinHeight", 0.0F, 15000F);
            }
            if (sectfile.exist(s, "FireFastTargets")) {
                float f = getF(sectfile, s, "FireFastTargets", 0.0F, 2.0F);
                shippartproperties.ATTACK_FAST_TARGETS = (int) (f + 0.5F);
                if (shippartproperties.ATTACK_FAST_TARGETS > 2) {
                    shippartproperties.ATTACK_FAST_TARGETS = 2;
                }
            }
            if (sectfile.exist(s, "TypeOfTarget")) {
                String s2 = getS(sectfile, s, "TypeOfTarget");
                if (BigshipGeneric.bLogDetail) {
                    System.out.println("BigShip: reading TypeOfTarget as '" + s2 + "'.");
                }
                if (s2.equals("SAM")) {
                    shippartproperties.TypeOfTarget = 1;
                } else if (s2.equals("SSM")) {
                    shippartproperties.TypeOfTarget = 2;
                } else if (s2.equals("SUM")) {
                    shippartproperties.TypeOfTarget = 3;
                }
            }
            if (sectfile.exist(s, "LauncherType")) {
                float f1 = getF(sectfile, s, "LauncherType", 0.0F, 1.0F);
                shippartproperties.LauncherType = (int) (f1 + 1.5F);
                if (shippartproperties.LauncherType > 2) {
                    shippartproperties.LauncherType = 2;
                }
            } else {
                shippartproperties.LauncherType = 1;
            }
            if (sectfile.exist(s, "HeadMinYaw")) {
                shippartproperties._HEAD_MIN_YAW = getF(sectfile, s, "HeadMinYaw", -360F, 360F);
            }
            if (sectfile.exist(s, "HeadMaxYaw")) {
                shippartproperties._HEAD_MAX_YAW = getF(sectfile, s, "HeadMaxYaw", -360F, 360F);
            }
            if (sectfile.exist(s, "NoFireMinYaw")) {
                shippartproperties._NOFIRE_MIN_YAW = getF(sectfile, s, "NoFireMinYaw", -360F, 360F);
                shippartproperties.NOFIRE_FLAG = true;
            }
            if (sectfile.exist(s, "NoFireMaxYaw")) {
                shippartproperties._NOFIRE_MAX_YAW = getF(sectfile, s, "NoFireMaxYaw", -360F, 360F);
            }
            if (sectfile.exist(s, "SelfYaw")) {
                shippartproperties._SELF_YAW = getF(sectfile, s, "SelfYaw", 0.0F, 180F);
            }
            if (sectfile.exist(s, "GunMinPitch")) {
                shippartproperties.GUN_MIN_PITCH = getF(sectfile, s, "GunMinPitch", -15F, 85F);
            }
            if (sectfile.exist(s, "GunMaxPitch")) {
                shippartproperties.GUN_MAX_PITCH = getF(sectfile, s, "GunMaxPitch", 0.0F, 89.9F);
            }
            if (sectfile.exist(s, "HeadMaxYawSpeed")) {
                shippartproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed", 0.1F, 999F);
            }
            if (sectfile.exist(s, "GunMaxPitchSpeed")) {
                shippartproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, s, "GunMaxPitchSpeed", 0.1F, 999F);
            }
            if (sectfile.exist(s, "ChainfireTime")) {
                shippartproperties.CHAINFIRE_TIME = getF(sectfile, s, "ChainfireTime", 0.0F, 600F);
            }
            if (sectfile.exist(s, "DelayAfterShoot")) {
                shippartproperties.DELAY_AFTER_SHOOT = getF(sectfile, s, "DelayAfterShoot", 0.0F, 999F);
            }
            if (sectfile.exist(s, "DelayBeforeFirstShoot")) {
                shippartproperties.DELAY_BEFORE_FIRST_SHOOT = getF(sectfile, s, "DelayBeforeFirstShoot", 0.0F, 999F);
            }
            if (sectfile.exist(s, "GunHeadChunk")) {
                shippartproperties.headChunkName = getS(sectfile, s, "GunHeadChunk");
            }
            if (sectfile.exist(s, "GunBarrelChunk")) {
                shippartproperties.gunChunkName = getS(sectfile, s, "GunBarrelChunk");
            }
            if (sectfile.exist(s, "GunShellStartHook")) {
                shippartproperties.gunShellStartHookName = getS(sectfile, s, "GunShellStartHook");
            }
        }

        private static ShipProperties LoadShipProperties(SectFile sectfile, String s, Class class1) {
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
            String checkMesh = getS(sectfile, s, "Mesh");
            if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && ((checkMesh == null) || (checkMesh.length() == 0))) {
                return null;
            }
            // TODO: ---
            ShipProperties shipproperties = new ShipProperties();
            // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
//            shipproperties.meshName = getS(sectfile, s, "Mesh");
            shipproperties.meshName = checkMesh;
            // TODO: ---

            shipproperties.soundName = getS(sectfile, s, "SoundMove");
            if (shipproperties.soundName.equalsIgnoreCase("none")) {
                shipproperties.soundName = null;
            }
            shipproperties.SLIDER_DIST = getF(sectfile, s, "SliderDistance", 5F, 1000F);
            shipproperties.SPEED = BigshipGeneric.KmHourToMSec(getF(sectfile, s, "Speed", 0.5F, 200F));
            shipproperties.DELAY_RESPAWN_MIN = 15F;
            shipproperties.DELAY_RESPAWN_MAX = 30F;
            if (sectfile.exist(s, "StabilizeFactor")) {
                shipproperties.STABILIZE_FACTOR = getF(sectfile, s, "StabilizeFactor", 0.5F, 5F);
            } else {
                shipproperties.STABILIZE_FACTOR = 1.0F;
            }
            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", shipproperties.meshName);
            Property.set(class1, "speed", shipproperties.SPEED);
            int i;
            for (i = 0; sectfile.sectionIndex(s + ":Part" + i) >= 0; i++) {
                ;
            }
            if (i <= 0) {
                System.out.println("BigShip: No part sections for '" + s + "'");
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                    throw new RuntimeException("Can't register BigShip object");
                }
                return null;
                // ---
            }
            if (i >= 255) {
                System.out.println("BigShip: Too many parts in " + s + ".");
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                    throw new RuntimeException("Can't register BigShip object");
                }
                return null;
                // ---
            }
            if (BigshipGeneric.bLogDetail) {
                System.out.println("BigShip: read ships.ini '" + s + "'.");
            }
            shipproperties.propparts = new ShipPartProperties[i];
            shipproperties.nGuns = 0;
            shipproperties.nRadars = 0;
            for (int j = 0; j < i; j++) {
                String s1 = s + ":Part" + j;
                ShipPartProperties shippartproperties = new ShipPartProperties();
                shipproperties.propparts[j] = shippartproperties;
                shippartproperties.baseChunkName = getS(sectfile, s1, "BaseChunk");
                int l;
                for (l = 0; sectfile.exist(s1, "AdditionalCollisionChunk" + l); l++) {
                    ;
                }
                if (l > 4) {
                    System.out.println("BigShip: Too many addcollischunks in '" + s1 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register BigShip object");
                    }
                    return null;
                    // ---
                }
                shippartproperties.additCollisChunkName = new String[l];
                for (int i1 = 0; i1 < l; i1++) {
                    shippartproperties.additCollisChunkName[i1] = getS(sectfile, s1, "AdditionalCollisionChunk" + i1);
                }

                String s2 = null;
                if (sectfile.exist(s1, "strengthBasedOnThisSection")) {
                    s2 = getS(sectfile, s1, "strengthBasedOnThisSection");
                }
                if (!shippartproperties.stre.read("Bigship", sectfile, s2, s1)) {
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        System.out.println("BigShip: Error in part properties for '" + s + "'");
                        throw new RuntimeException("Can't register Bigship object");
                    }
                    return null;
                    // ---
                }
                if (sectfile.exist(s1, "Vital")) {
                    shippartproperties.dmgDepth = getF(sectfile, s1, "damageDepth", 0.0F, 99F);
                    shippartproperties.dmgPitch = getF(sectfile, s1, "damagePitch", -89F, 89F);
                    shippartproperties.dmgRoll = getF(sectfile, s1, "damageRoll", 0.0F, 89F);
                    shippartproperties.dmgTime = getF(sectfile, s1, "damageTime", 1.0F, 1200F);
                    shippartproperties.BLACK_DAMAGE = 0.6666667F;
                } else {
                    shippartproperties.dmgDepth = -1F;
                    shippartproperties.BLACK_DAMAGE = 1.0F;
                }
                if (!sectfile.exist(s1, "Gun") && !sectfile.exist(s1, "gunBasedOnThisSection") && !sectfile.exist(s1, "Missile") && !sectfile.exist(s1, "missileBasedOnThisSection") && !sectfile.exist(s1, "NowRadar") && !sectfile.exist(s1, "radarBasedOnThisSection")) {
                    shippartproperties.gun_idx = -1;
                    shippartproperties.radar_idx = -1;
                    continue;
                }
                if (shippartproperties.isItLifeKeeper()) {
                    System.out.println("*** ERROR: bigship: vital with gun or radar");
                    shippartproperties.gun_idx = -1;
                    shippartproperties.radar_idx = -1;
                    continue;
                }
                if (sectfile.exist(s1, "NowRadar") || sectfile.exist(s1, "radarBasedOnThisSection")) {
                    shippartproperties.gun_idx = -1;
                    shippartproperties.radar_idx = shipproperties.nRadars++;
                    if (shipproperties.nRadars > 255) {
                        System.out.println("BigShip: Too many radars in " + s + ".");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                            throw new RuntimeException("Can't register BigShip object");
                        }
                        return null;
                        // ---
                    }
                    shippartproperties.NOW_RADAR = true;
                    shippartproperties.HEAD_MAX_YAW_SPEED = -1000F;
                    if (sectfile.exist(s1, "radarBasedOnThisSection")) {
                        String s3 = getS(sectfile, s1, "radarBasedOnThisSection");
                        tryToReadRadarProperties(sectfile, s3, shippartproperties);
                    } else {
                        tryToReadRadarProperties(sectfile, s1, shippartproperties);
                    }
                    shippartproperties.headChunkName = shippartproperties.baseChunkName;
                    if ((shippartproperties.HEAD_MAX_YAW_SPEED <= -1000F) || (shippartproperties.headChunkName == null)) {
                        System.out.println("BigShip: Not enough 'radar' data  in '" + s1 + "'");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                            throw new RuntimeException("Can't register BigShip object");
                        }
                        return null;
                        // ---
                    }
                    if (BigshipGeneric.bLogDetail) {
                        System.out.println("BigShip: read NowRadar in Part:" + j + ", as Radar No." + shippartproperties.radar_idx + ".");
                    }
                    continue;
                }
                if (sectfile.exist(s1, "Missile") || sectfile.exist(s1, "missileBasedOnThisSection")) {
                    shippartproperties.gun_idx = shipproperties.nGuns++;
                    shippartproperties.radar_idx = -1;
                    if (shipproperties.nGuns > 255) {
                        System.out.println("BigShip: Too many guns in " + s + ".");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                            throw new RuntimeException("Can't register BigShip object");
                        }
                        return null;
                        // ---
                    }
                    shippartproperties.gunClass = null;
                    shippartproperties.missileName = null;
                    shippartproperties.ATTACK_MAX_DISTANCE = -1000F;
                    shippartproperties.ATTACK_MIN_DISTANCE = 5F;
                    shippartproperties.ATTACK_MAX_RADIUS = -1000F;
                    shippartproperties.ATTACK_MAX_HEIGHT = -1000F;
                    shippartproperties.ATTACK_MIN_HEIGHT = 0.0F;
                    shippartproperties.TRACKING_ONLY = false;
                    shippartproperties.NOW_RADAR = false;
                    shippartproperties.TypeOfTarget = 0;
                    shippartproperties.LauncherType = 0;
                    shippartproperties.ATTACK_FAST_TARGETS = 1;
                    shippartproperties.FAST_TARGETS_ANGLE_ERROR = 0.0F;
                    shippartproperties.PREFER_SLOW_TARGET = false;
                    shippartproperties._HEAD_MIN_YAW = -1000F;
                    shippartproperties._HEAD_MAX_YAW = -1000F;
                    shippartproperties._NOFIRE_MIN_YAW = -1000F;
                    shippartproperties._NOFIRE_MAX_YAW = -1000F;
                    shippartproperties.NOFIRE_FLAG = false;
                    shippartproperties.NOHEADING_FLAG = false;
                    shippartproperties._SELF_YAW = 0.0F;
                    shippartproperties.GUN_MIN_PITCH = -1000F;
                    shippartproperties.GUN_STD_PITCH = -1000F;
                    shippartproperties.GUN_MAX_PITCH = -1000F;
                    shippartproperties.HEAD_MAX_YAW_SPEED = -1000F;
                    shippartproperties.GUN_MAX_PITCH_SPEED = -1000F;
                    shippartproperties.DELAY_AFTER_SHOOT = -1000F;
                    shippartproperties.DELAY_BEFORE_FIRST_SHOOT = 0.0F;
                    shippartproperties.CHAINFIRE_TIME = -1000F;
                    shippartproperties.headChunkName = null;
                    shippartproperties.gunChunkName = null;
                    shippartproperties.gunShellStartHookName = null;
                    if (sectfile.exist(s1, "missileBasedOnThisSection")) {
                        String s4 = getS(sectfile, s1, "missileBasedOnThisSection");
                        tryToReadMissileProperties(sectfile, s4, shippartproperties);
                    }
                    tryToReadMissileProperties(sectfile, s1, shippartproperties);
                    if ((shippartproperties.TypeOfTarget == 0) || (shippartproperties.missileName == null) || (shippartproperties.ATTACK_MAX_DISTANCE <= -1000F) || (shippartproperties.ATTACK_MAX_RADIUS <= -1000F) || (shippartproperties.ATTACK_MAX_HEIGHT <= -1000F) || (shippartproperties._HEAD_MIN_YAW <= -1000F) || (shippartproperties._HEAD_MAX_YAW <= -1000F) || (shippartproperties.GUN_MIN_PITCH <= -1000F) || (shippartproperties.GUN_MAX_PITCH <= -1000F) || (shippartproperties.HEAD_MAX_YAW_SPEED <= -1000F) || (shippartproperties.GUN_MAX_PITCH_SPEED <= -1000F) || (shippartproperties.DELAY_AFTER_SHOOT <= -1000F) || (shippartproperties.CHAINFIRE_TIME <= -1000F) || (shippartproperties.headChunkName == null) || (shippartproperties.gunChunkName == null) || (shippartproperties.gunShellStartHookName == null)) {
                        if (BigshipGeneric.bLogDetail) {
                            System.out.println("BigShip: TypeOfTarget:" + shippartproperties.TypeOfTarget);
                            System.out.println("BigShip: missileName:" + shippartproperties.missileName);
                            System.out.println("BigShip: ATTACK_MAX_DISTANCE:" + shippartproperties.ATTACK_MAX_DISTANCE);
                            System.out.println("BigShip: ATTACK_MIN_DISTANCE:" + shippartproperties.ATTACK_MIN_DISTANCE);
                            System.out.println("BigShip: ATTACK_MAX_RADIUS:" + shippartproperties.ATTACK_MAX_RADIUS);
                            System.out.println("BigShip: ATTACK_MAX_HEIGHT:" + shippartproperties.ATTACK_MAX_HEIGHT);
                            System.out.println("BigShip: _HEAD_MIN_YAW:" + shippartproperties._HEAD_MIN_YAW);
                            System.out.println("BigShip: _HEAD_MAX_YAW:" + shippartproperties._HEAD_MAX_YAW);
                            System.out.println("BigShip: GUN_MIN_PITCH:" + shippartproperties.GUN_MIN_PITCH);
                            System.out.println("BigShip: GUN_MAX_PITCH:" + shippartproperties.GUN_MAX_PITCH);
                            System.out.println("BigShip: HEAD_MAX_YAW_SPEED:" + shippartproperties.HEAD_MAX_YAW_SPEED);
                            System.out.println("BigShip: GUN_MAX_PITCH_SPEED:" + shippartproperties.GUN_MAX_PITCH_SPEED);
                            System.out.println("BigShip: DELAY_AFTER_SHOOT:" + shippartproperties.DELAY_AFTER_SHOOT);
                            System.out.println("BigShip: CHAINFIRE_TIME:" + shippartproperties.CHAINFIRE_TIME);
                            System.out.println("BigShip: headChunkName:" + shippartproperties.headChunkName);
                            System.out.println("BigShip: gunChunkName:" + shippartproperties.gunChunkName);
                            System.out.println("BigShip: gunShellStartHookName:" + shippartproperties.gunShellStartHookName);
                        }
                        System.out.println("BigShip: Not enough 'missile' data  in '" + s1 + "'");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                            throw new RuntimeException("Can't register BigShip object");
                        }
                        return null;
                        // ---
                    }
                    shippartproperties.WEAPONS_MASK = 4;
                    if (shippartproperties.WEAPONS_MASK == 0) {
                        System.out.println("BigShip: Undefined weapon type in gun class '" + shippartproperties.gunClass.getName() + "'");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                            throw new RuntimeException("Can't register BigShip object");
                        }
                        return null;
                        // ---
                    }
                    if (shippartproperties._HEAD_MIN_YAW > shippartproperties._HEAD_MAX_YAW) {
                        System.out.println("BigShip: Wrong yaw angles in missile " + s1 + ".");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                            throw new RuntimeException("Can't register BigShip object");
                        }
                        return null;
                        // ---
                    }
                    if (shippartproperties._NOFIRE_MIN_YAW > shippartproperties._NOFIRE_MAX_YAW) {
                        System.out.println("BigShip: Wrong NoFire yaw angles in missile " + s1 + ".");
                        // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                        if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                            throw new RuntimeException("Can't register BigShip object");
                        }
                        return null;
                        // ---
                    }
                    shippartproperties.HEAD_STD_YAW = 0.0F;
                    shippartproperties.HEAD_YAW_RANGE.set(shippartproperties._HEAD_MIN_YAW, shippartproperties._HEAD_MAX_YAW);
                    float f = shippartproperties._HEAD_MIN_YAW - shippartproperties._SELF_YAW;
                    float f1 = shippartproperties._HEAD_MAX_YAW + shippartproperties._SELF_YAW;
                    if ((f1 - f) > 360F) {
                        f1 = 360F;
                        f = 0.0F;
                    }
                    if (f1 > 360F) {
                        f1 = 360F;
                    }
                    if (f < -360F) {
                        f = -360F;
                    }
                    shippartproperties.SELF_YAW_RANGE.set(f, f1);
                    if (shippartproperties.NOFIRE_FLAG) {
                        shippartproperties.NOFIRE_YAW_RANGE.set(shippartproperties._NOFIRE_MIN_YAW, shippartproperties._NOFIRE_MAX_YAW);
                        float f2 = shippartproperties._NOFIRE_MAX_YAW - shippartproperties._SELF_YAW;
                        float f3 = shippartproperties._NOFIRE_MIN_YAW + shippartproperties._SELF_YAW;
                        if (f3 >= f2) {
                            shippartproperties.NOHEADING_FLAG = false;
                        } else {
                            shippartproperties.NOSELF_YAW_RANGE.set(f3, f2);
                            shippartproperties.NOHEADING_FLAG = true;
                        }
                    }
                    if (BigshipGeneric.bLogDetail) {
                        System.out.println("BigShip: read Missile in Part:" + j + ", as Gun No." + shippartproperties.gun_idx + ".");
                    }
                    continue;
                }
                shippartproperties.gun_idx = shipproperties.nGuns++;
                shippartproperties.radar_idx = -1;
                if (shipproperties.nGuns > 255) {
                    System.out.println("BigShip: Too many guns in " + s + ".");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register BigShip object");
                    }
                    return null;
                    // ---
                }
                shippartproperties.gunClass = null;
                shippartproperties.missileName = null;
                shippartproperties.ATTACK_MAX_DISTANCE = -1000F;
                shippartproperties.ATTACK_MIN_DISTANCE = 5F;
                shippartproperties.ATTACK_MAX_RADIUS = -1000F;
                shippartproperties.ATTACK_MAX_HEIGHT = -1000F;
                shippartproperties.ATTACK_MIN_HEIGHT = 0.0F;
                shippartproperties.TRACKING_ONLY = false;
                shippartproperties.NOW_RADAR = false;
                shippartproperties.TypeOfTarget = 0;
                shippartproperties.LauncherType = 0;
                shippartproperties.ATTACK_FAST_TARGETS = 1;
                shippartproperties.FAST_TARGETS_ANGLE_ERROR = 0.0F;
                shippartproperties.PREFER_SLOW_TARGET = false;
                shippartproperties._HEAD_MIN_YAW = -1000F;
                shippartproperties._HEAD_MAX_YAW = -1000F;
                shippartproperties._NOFIRE_MIN_YAW = -1000F;
                shippartproperties._NOFIRE_MAX_YAW = -1000F;
                shippartproperties.NOFIRE_FLAG = false;
                shippartproperties.NOHEADING_FLAG = false;
                shippartproperties.GUN_MIN_PITCH = -1000F;
                shippartproperties.GUN_STD_PITCH = -1000F;
                shippartproperties.GUN_MAX_PITCH = -1000F;
                shippartproperties.HEAD_MAX_YAW_SPEED = -1000F;
                shippartproperties.GUN_MAX_PITCH_SPEED = -1000F;
                shippartproperties.DELAY_AFTER_SHOOT = -1000F;
                shippartproperties.DELAY_BEFORE_FIRST_SHOOT = 0.0F;
                shippartproperties.CHAINFIRE_TIME = -1000F;
                shippartproperties.headChunkName = null;
                shippartproperties.gunChunkName = null;
                shippartproperties.gunShellStartHookName = null;
                if (sectfile.exist(s1, "gunBasedOnThisSection")) {
                    String s5 = getS(sectfile, s1, "gunBasedOnThisSection");
                    tryToReadGunProperties(sectfile, s5, shippartproperties);
                }
                tryToReadGunProperties(sectfile, s1, shippartproperties);
                if ((shippartproperties.gunClass == null) || (shippartproperties.ATTACK_MAX_DISTANCE <= -1000F) || (shippartproperties.ATTACK_MAX_RADIUS <= -1000F) || (shippartproperties.ATTACK_MAX_HEIGHT <= -1000F) || (shippartproperties._HEAD_MIN_YAW <= -1000F) || (shippartproperties._HEAD_MAX_YAW <= -1000F) || (shippartproperties.GUN_MIN_PITCH <= -1000F) || (shippartproperties.GUN_MAX_PITCH <= -1000F) || (shippartproperties.HEAD_MAX_YAW_SPEED <= -1000F) || (shippartproperties.GUN_MAX_PITCH_SPEED <= -1000F) || (shippartproperties.DELAY_AFTER_SHOOT <= -1000F) || (shippartproperties.CHAINFIRE_TIME <= -1000F) || (shippartproperties.headChunkName == null) || (shippartproperties.gunChunkName == null) || (shippartproperties.gunShellStartHookName == null)) {
                    System.out.println("BigShip: Not enough 'gun' data  in '" + s1 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register BigShip object");
                    }
                    return null;
                    // ---
                }
                shippartproperties.WEAPONS_MASK = GunGeneric.getProperties(shippartproperties.gunClass).weaponType;
                if (shippartproperties.WEAPONS_MASK == 0) {
                    System.out.println("BigShip: Undefined weapon type in gun class '" + shippartproperties.gunClass.getName() + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register BigShip object");
                    }
                    return null;
                    // ---
                }
                if (shippartproperties._HEAD_MIN_YAW > shippartproperties._HEAD_MAX_YAW) {
                    System.out.println("BigShip: Wrong yaw angles in gun " + s1 + ".");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register BigShip object");
                    }
                    return null;
                    // ---
                }
                if (shippartproperties._NOFIRE_MIN_YAW > shippartproperties._NOFIRE_MAX_YAW) {
                    System.out.println("BigShip: Wrong NoFire yaw angles in gun " + s1 + ".");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register BigShip object");
                    }
                    return null;
                    // ---
                }
                shippartproperties.HEAD_STD_YAW = 0.0F;
                shippartproperties.HEAD_YAW_RANGE.set(shippartproperties._HEAD_MIN_YAW, shippartproperties._HEAD_MAX_YAW);
                if (shippartproperties.NOFIRE_FLAG) {
                    shippartproperties.NOFIRE_YAW_RANGE.set(shippartproperties._NOFIRE_MIN_YAW, shippartproperties._NOFIRE_MAX_YAW);
                }
                if (BigshipGeneric.bLogDetail) {
                    System.out.println("BigShip: read Gun in Part:" + j + ", as Gun No." + shippartproperties.gun_idx + ".");
                }
            }

            shipproperties.WEAPONS_MASK = 0;
            shipproperties.ATTACK_MAX_DISTANCE = 1.0F;
            for (int k = 0; k < shipproperties.propparts.length; k++) {
                if (!shipproperties.propparts[k].haveGun()) {
                    continue;
                }
                shipproperties.WEAPONS_MASK |= shipproperties.propparts[k].WEAPONS_MASK;
                if (shipproperties.ATTACK_MAX_DISTANCE < shipproperties.propparts[k].ATTACK_MAX_DISTANCE) {
                    shipproperties.ATTACK_MAX_DISTANCE = shipproperties.propparts[k].ATTACK_MAX_DISTANCE;
                }
            }

            if (sectfile.get(s, "IsAirport", false)) {
                shipproperties.propAirport = new AirportProperties(class1);
                shipproperties.typicalPlaneClass = sectfile.get(s, "TypicalPlaneClass", "");
            }
            return shipproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg) {
            BigshipGeneric bigshipgeneric = null;
            try {
                BigshipGeneric.constr_arg1 = this.proper;
                BigshipGeneric.constr_arg2 = actorspawnarg;
                bigshipgeneric = (BigshipGeneric) this.cls.newInstance();
                BigshipGeneric.constr_arg1 = null;
                BigshipGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                BigshipGeneric.constr_arg1 = null;
                BigshipGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Ship object [class:" + this.cls.getName() + "]");
                return null;
            }
            return bigshipgeneric;
        }

        public Class          cls;
        public ShipProperties proper;

        public SPAWN(Class class1) {
            try {
                String s = class1.getName();
                int i = s.lastIndexOf('.');
                int j = s.lastIndexOf('$');
                if (i < j) {
                    i = j;
                }
                String s1 = s.substring(i + 1);
                this.proper = LoadShipProperties(Statics.getShipsFile(), s1, class1);

                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if ((ObjectsLogLevel.getObjectsLogLevel() < ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) && (this.proper == null)) {
                    return;
                    // TODO: ---
                }

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Problem in spawn: " + class1.getName());
            }
            this.cls = class1;
            Spawn.add(this.cls, this);
        }
    }

    public static class AirportProperties {

        public void firstInit(BigshipGeneric bigshipgeneric) {
            if (this.bInited) {
                return;
            }
            this.bInited = true;
            HierMesh hiermesh = bigshipgeneric.hierMesh();
            this.findHook(hiermesh, "_RWY_TO", this.rwy[0]);
            this.findHook(hiermesh, "_RWY_LDG", this.rwy[1]);
            this.towString = new Mesh("3DO/Arms/ArrestorCable/mono.sim");
            ArrayList arraylist = new ArrayList();
            int i = 0;
            do {
                String s = i > 9 ? "" + i : "0" + i;
                if (!this.findHook(hiermesh, "_TOW" + s + "A", this.loc)) {
                    break;
                }
                arraylist.add(new Point3d(this.loc.getPoint()));
                this.findHook(hiermesh, "_TOW" + s + "B", this.loc);
                arraylist.add(new Point3d(this.loc.getPoint()));
                i++;
            } while (true);
            if (i > 0) {
                i *= 2;
                this.towPRel = new Point3d[i];
                for (int j = 0; j < i; j++) {
                    this.towPRel[j] = (Point3d) arraylist.get(j);
                }

            }
            this.fillParks(bigshipgeneric, hiermesh, "_Park", arraylist);
            if (arraylist.size() > 0) {
                this.cellTO = new CellAirField(new com.maddox.il2.ai.air.CellObject[1][1], arraylist, 1.0D);
            }
            this.fillParks(bigshipgeneric, hiermesh, "_LPark", arraylist);
            if (arraylist.size() > 0) {
                this.cellLDG = new CellAirField(new com.maddox.il2.ai.air.CellObject[1][1], arraylist, 1.0D);
            }
        }

        private void fillParks(BigshipGeneric bigshipgeneric, HierMesh hiermesh, String s, ArrayList arraylist) {
            arraylist.clear();
            int i = 0;
            do {
                String s1 = s + (i > 9 ? "" + i : "0" + i);
                if (this.findHook(hiermesh, s1, this.loc)) {
                    arraylist.add(new Point3d(-this.p.y, this.p.x, this.p.z));
                    i++;
                } else {
                    return;
                }
            } while (true);
        }

        private boolean findHook(HierMesh hiermesh, String s, Loc loc1) {
            int i = hiermesh.hookFind(s);
            if (i == -1) {
                return false;
            } else {
                hiermesh.hookMatrix(i, this.m1);
                this.m1.getEulers(this.tmp);
                this.o.setYPR(Geom.RAD2DEG((float) this.tmp[0]), 360F - Geom.RAD2DEG((float) this.tmp[1]), 360F - Geom.RAD2DEG((float) this.tmp[2]));
                this.p.set(this.m1.m03, this.m1.m13, this.m1.m23);
                loc1.set(this.p, this.o);
                return true;
            }
        }

        public Loc          rwy[] = { new Loc(), new Loc() };
        public Mesh         towString;
        public Point3d      towPRel[];
        public CellAirField cellTO;
        public CellAirField cellLDG;
        private boolean     bInited;
        private Loc         loc;
        private Point3d     p;
        private Orient      o;
        private Matrix4d    m1;
        private double      tmp[];

        public AirportProperties(Class class1) {
            this.loc = new Loc();
            this.p = new Point3d();
            this.o = new Orient();
            this.m1 = new Matrix4d();
            this.tmp = new double[3];
            this.bInited = false;
            Property.set(class1, "IsAirport", "true");
        }
    }

    private static class TowStringMeshDraw extends ActorMeshDraw {

        public void render(Actor actor) {
            super.render(actor);
            BigshipGeneric bigshipgeneric = (BigshipGeneric) actor;
            if (bigshipgeneric.prop.propAirport == null) {
                return;
            }
            Point3d apoint3d[] = bigshipgeneric.prop.propAirport.towPRel;
            if (apoint3d == null) {
                return;
            }
            actor.pos.getRender(this.lRender);
            int i = apoint3d.length / 2;
            for (int j = 0; j < i; j++) {
                if (j != bigshipgeneric.towPortNum) {
                    this.lRender.transform(apoint3d[j * 2], this.p0);
                    this.lRender.transform(apoint3d[(j * 2) + 1], this.p1);
                    this.renderTow(bigshipgeneric.prop.propAirport.towString);
                    continue;
                }
                if (Actor.isValid(bigshipgeneric.towAircraft)) {
                    this.lRender.transform(apoint3d[j * 2], this.p0);
                    this.l.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    bigshipgeneric.towHook.computePos(bigshipgeneric.towAircraft, bigshipgeneric.towAircraft.pos.getRender(), this.l);
                    this.p1.set(this.l.getPoint());
                    this.renderTow(bigshipgeneric.prop.propAirport.towString);
                    this.lRender.transform(apoint3d[(j * 2) + 1], this.p0);
                    this.l.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
                    bigshipgeneric.towHook.computePos(bigshipgeneric.towAircraft, bigshipgeneric.towAircraft.pos.getRender(), this.l);
                    this.p1.set(this.l.getPoint());
                    this.renderTow(bigshipgeneric.prop.propAirport.towString);
                }
            }

        }

        private void renderTow(Mesh mesh) {
            this.tmpVector.sub(this.p1, this.p0);
            mesh.setScaleXYZ((float) this.tmpVector.length(), 1.0F, 1.0F);
            this.tmpVector.normalize();
            Orient orient = this.l.getOrient();
            orient.setAT0(this.tmpVector);
            this.l.set(this.p0);
            mesh.setPos(this.l);
            mesh.render();
        }

        private Loc      lRender;
        private Loc      l;
        private Vector3d tmpVector;
        private Point3d  p0;
        private Point3d  p1;

        public TowStringMeshDraw(ActorDraw actordraw) {
            super(actordraw);
            this.lRender = new Loc();
            this.l = new Loc();
            this.tmpVector = new Vector3d();
            this.p0 = new Point3d();
            this.p1 = new Point3d();
        }
    }

    public ShipProperties getShipProp() {
        return this.prop;
    }

    public static final double Rnd(double d, double d1) {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    public static final int Rnd(int i, int j) {
        return World.Rnd().nextInt(i, j);
    }

    /* private */ boolean RndB(float f) {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    public static final float KmHourToMSec(float f) {
        return f / 3.6F;
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5D + f / Time.tickLenFs());
        return l < 1L ? 1L : l;
    }

    protected final boolean Head360(FiringDevice firingdevice) {
        return this.parts[firingdevice.part_idx].pro.HEAD_YAW_RANGE.fullcircle();
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        if (actor instanceof BridgeSegment) {
            if (this.dying != 0) {
                aflag[0] = false;
            }
            return;
        }
        if ((this.path == null) && (actor instanceof ActorMesh) && ((ActorMesh) actor).isStaticPos()) {
            aflag[0] = false;
            return;
        } else {
            return;
        }
    }

    public void msgCollision(Actor actor, String s, String s1) {
        if (this.dying != 0) {
            return;
        }
        if (this.isNetMirror()) {
            return;
        }
        if (actor instanceof WeakBody) {
            return;
        }
        if ((actor instanceof ShipGeneric) || (actor instanceof BigshipGeneric) || (actor instanceof BridgeSegment)) {
            this.Die(null, -1L, true, true);
        }
    }

    private int findNotDeadPartByShotChunk(String s) {
        if ((s == null) || s.equals("")) {
            return -2;
        }
        int i = this.hierMesh().chunkFindCheck(s);
        if (i < 0) {
            return -2;
        }
        label0:
        for (int j = 0; j < this.parts.length; j++) {
            if (this.parts[j].state == 2) {
                continue;
            }
            if (i == this.parts[j].pro.baseChunkIdx) {
                return j;
            }
            int k = 0;
            do {
                if (k >= this.parts[j].pro.additCollisChunkIdx.length) {
                    continue label0;
                }
                if (i == this.parts[j].pro.additCollisChunkIdx[k]) {
                    return j;
                }
                k++;
            } while (true);
        }

        return -1;
    }

    private void computeNewPath() {
        if ((this.path == null) || (this.dying != 0) || Mission.isDogfight()) {
            return;
        }
        Segment segment = (Segment) this.path.get(this.cachedSeg);
        long l = 0L;
        long l1 = Time.tickNext();
        if (Mission.isCoop() || Mission.isDogfight()) {
            l1 = NetServerParams.getServerTime();
        }
        if (((segment.timeIn > l1) || !this.isTurning) && ((segment.speedIn > this.CURRSPEED) || (segment.speedOut > this.CURRSPEED))) {
            if (Mission.isCoop()) {
                this.mustSendSpeedToNet = true;
            }
            float f = 0.0F;
            if (l1 >= segment.timeIn) {
                long l2 = segment.timeOut - segment.timeIn;
                long l4 = l1 - segment.timeIn;
                float f1 = segment.speedOut - segment.speedIn;
                f = segment.speedIn + (f1 * (float) ((double) l4 / (double) l2));
            }
            if (f > this.CURRSPEED) {
                segment.speedIn = this.CURRSPEED;
            } else {
                segment.speedIn = f;
            }
            if (segment.speedOut > this.CURRSPEED) {
                segment.speedOut = this.CURRSPEED;
            }
            Point3d point3d = new Point3d();
            point3d.x = this.initLoc.getX();
            point3d.y = this.initLoc.getY();
            point3d.z = this.initLoc.getZ();
            segment.posIn.set(point3d);
            if (segment.timeIn < l1) {
                segment.timeIn = l1;
            }
            double d = segment.posIn.distance(segment.posOut);
            l = segment.timeOut;
            segment.timeOut = segment.timeIn + (long) (1000D * ((2D * d) / Math.abs(segment.speedOut + segment.speedIn)));
            segment.length = (float) d;
            segment.slidersOn = false;
        } else {
            l = segment.timeOut;
        }
        if (this.isTurningBackward && ((segment.speedIn > this.CURRSPEED) || (segment.speedOut > this.CURRSPEED))) {
            this.mustRecomputePath = true;
        }
        int i = this.cachedSeg;
        for (i++; i <= (this.path.size() - 1); i++) {
            Segment segment1 = (Segment) this.path.get(i);
            long l3 = segment1.timeIn - l;
            segment1.timeIn = segment.timeOut + l3;
            segment1.posIn = segment.posOut;
            if (segment1.speedIn > this.CURRSPEED) {
                if (Mission.isCoop()) {
                    this.mustSendSpeedToNet = true;
                }
                segment1.speedIn = this.CURRSPEED;
            }
            if (segment1.speedOut > this.CURRSPEED) {
                if (Mission.isCoop()) {
                    this.mustSendSpeedToNet = true;
                }
                segment1.speedOut = this.CURRSPEED;
            }
            l = segment1.timeOut;
            segment1.timeOut = segment1.timeIn + (long) (1000D * ((2D * segment1.length) / Math.abs(segment1.speedOut + segment1.speedIn)));
            segment = segment1;
        }

    }

    public void msgShot(Shot shot) {
        shot.bodyMaterial = 2;
        if (this.dying != 0) {
            return;
        }
        if (shot.power <= 0.0F) {
            return;
        }
        if (this.isNetMirror() && shot.isMirage()) {
            return;
        }
        if (this.wakeupTmr < 0L) {
            this.wakeupTmr = SecsToTicks(Rnd(this.DELAY_WAKEUP, this.DELAY_WAKEUP * 1.2F));
        }
        int i = this.findNotDeadPartByShotChunk(shot.chunkName);
        if (i < 0) {
            return;
        }
        float f;
        float f1;
        if (shot.powerType == 1) {
            f = this.parts[i].pro.stre.EXPLHIT_MIN_TNT;
            f1 = this.parts[i].pro.stre.EXPLHIT_MAX_TNT;
        } else {
            f = this.parts[i].pro.stre.SHOT_MIN_ENERGY;
            f1 = this.parts[i].pro.stre.SHOT_MAX_ENERGY;
        }
        float f2 = shot.power * Rnd(1.0F, 1.1F);
        if (f2 < f) {
            return;
        }
        this.tmpvd.set(shot.v);
        this.pos.getAbs().transformInv(this.tmpvd);
        this.parts[i].damageIsFromRight = this.tmpvd.y > 0.0D;
        float f3 = f2 / f1;
        this.parts[i].damage += f3;
        if (this.isNetMirror() && (shot.initiator != null)) {
            this.parts[i].mirror_initiator = shot.initiator;
        }
        this.InjurePart(i, shot.initiator, true);
        if (!Mission.isDogfight() && (this.path != null) && this.parts[i].pro.isItLifeKeeper() && (this.parts[i].damage > 0.2F)) {
            this.computeSpeedReduction(this.parts[i].damage);
            this.computeNewPath();
        }
    }

    private void computeSpeedReduction(float f) {
        int i = (int) (f * 128F);
        if (--i < 0) {
            i = 0;
        } else if (i > 127) {
            i = 127;
        }
        f = i / 128F;
        float f1 = (0.4F * this.prop.SPEED) + ((1.0F - f) * 2.0F * this.prop.SPEED);
        int j = Math.round(f1);
        f1 = j;
        if (f1 < this.CURRSPEED) {
            this.CURRSPEED = f1;
        }
    }

    public void msgExplosion(Explosion explosion) {
        if (this.dying != 0) {
            return;
        }
        if (this.isNetMirror() && explosion.isMirage()) {
            return;
        }
        if (this.wakeupTmr < 0L) {
            this.wakeupTmr = SecsToTicks(Rnd(this.DELAY_WAKEUP, this.DELAY_WAKEUP * 1.2F));
        }
        float f = explosion.power;
        if ((explosion.powerType == 2) && (explosion.chunkName != null)) {
            f *= 0.45F;
        }
        int i = -1;
        if (explosion.chunkName != null) {
            int j = this.findNotDeadPartByShotChunk(explosion.chunkName);
            if (j >= 0) {
                float f1 = f;
                f1 *= Rnd(0.5F, 1.6F) * Mission.BigShipHpDiv();
                if (f1 >= this.parts[j].pro.stre.EXPLHIT_MIN_TNT) {
                    i = j;
                    this.p1.set(explosion.p);
                    this.pos.getAbs().transformInv(this.p1);
                    this.parts[j].damageIsFromRight = this.p1.y < 0.0D;
                    float f2 = (f1 - this.parts[j].pro.stre.EXPLHIT_MIN_TNT) / this.parts[j].pro.stre.EXPLHIT_MAX_TNT;
                    this.parts[j].damage += f2;
                    if (this.isNetMirror() && (explosion.initiator != null)) {
                        this.parts[j].mirror_initiator = explosion.initiator;
                    }
                    this.InjurePart(j, explosion.initiator, true);
                    if (!Mission.isDogfight() && (this.path != null) && this.parts[j].pro.isItLifeKeeper() && (this.parts[j].damage > 0.2F)) {
                        this.computeSpeedReduction(this.parts[j].damage);
                        this.computeNewPath();
                    }
                }
            }
        }
        Loc loc = this.pos.getAbs();
        this.p1.set(explosion.p);
        this.pos.getAbs().transformInv(this.p1);
        boolean flag = this.p1.y < 0.0D;
        for (int k = 0; k < this.parts.length; k++) {
            if ((k == i) || (this.parts[k].state == 2)) {
                continue;
            }
            this.p1.set(this.parts[k].pro.partOffs);
            loc.transform(this.p1);
            float f3 = this.parts[k].pro.partR;
            //float f4 = (float)(p1.distance(explosion.p) - (double)f3);
            float f5 = 0.0F;
            if ((explosion.p.z < 0.0D) && this.parts[k].pro.baseChunkName.startsWith("Hull") && (explosion.powerType == 0) && (explosion.power > 2.0F)) {
                f5 = explosion.receivedTNT_1meterWater(this.p1, f3, (float) explosion.p.z);
            } else {
                f5 = explosion.receivedTNT_1meter(this.p1, f3);
            }
            f5 *= Rnd(1.0F, 1.1F) * Mission.BigShipHpDiv();
            if (f5 < this.parts[k].pro.stre.EXPLNEAR_MIN_TNT) {
                continue;
            }
            this.parts[k].damageIsFromRight = flag;
            float f6 = f5 / this.parts[k].pro.stre.EXPLNEAR_MAX_TNT;
            this.parts[k].damage += f6;
            if (this.isNetMirror() && (explosion.initiator != null)) {
                this.parts[k].mirror_initiator = explosion.initiator;
            }
            this.InjurePart(k, explosion.initiator, true);
            if (!Mission.isDogfight() && (this.path != null) && this.parts[k].pro.isItLifeKeeper() && (this.parts[k].damage > 0.2F)) {
                this.computeSpeedReduction(this.parts[k].damage);
                this.computeNewPath();
            }
        }

    }

    private void recomputeShotpoints() {
        if ((this.shotpoints == null) || (this.shotpoints.length < (1 + this.parts.length))) {
            this.shotpoints = new int[1 + this.parts.length];
        }
        this.numshotpoints = 0;
        if (this.dying != 0) {
            return;
        }
        this.numshotpoints = 1;
        this.shotpoints[0] = 0;
        for (int i = 0; i < this.parts.length; i++) {
            if (this.parts[i].state == 2) {
                continue;
            }
            int j;
            if (this.parts[i].pro.isItLifeKeeper()) {
                j = this.parts[i].pro.baseChunkIdx;
            } else {
                if (!this.parts[i].pro.haveGun()) {
                    continue;
                }
                j = this.parts[i].pro.gunChunkIdx;
            }
            this.shotpoints[this.numshotpoints] = i + 1;
            this.hierMesh().setCurChunk(j);
            this.hierMesh().getChunkLocObj(this.tmpL);
            this.parts[i].shotpointOffs.set(this.tmpL.getPoint());
            this.numshotpoints++;
        }

    }

    private boolean visualsInjurePart(int i, boolean flag) {
        if (!flag) {
            if (this.parts[i].state == 2) {
                this.parts[i].damage = 1.0F;
                return false;
            }
            if (this.parts[i].damage < this.parts[i].pro.BLACK_DAMAGE) {
                return false;
            }
            this.netsendDrown_nparts = 0;
            this.netsendDrown_depth = 0.0F;
            this.netsendDrown_pitch = 0.0F;
            this.netsendDrown_roll = 0.0F;
            this.netsendDrown_timeS = 0.0F;
            if (this.parts[i].damage < 1.0F) {
                if (this.parts[i].state == 1) {
                    return false;
                }
                this.parts[i].state = 1;
            } else {
                this.parts[i].damage = 1.0F;
                this.parts[i].state = 2;
            }
            if (this.parts[i].pro.isItLifeKeeper()) {
                this.netsendDrown_nparts++;
                this.netsendDrown_depth += Rnd(0.8F, 1.0F) * this.parts[i].pro.dmgDepth;
                this.netsendDrown_pitch += Rnd(0.8F, 1.0F) * this.parts[i].pro.dmgPitch;
                this.netsendDrown_roll = (float) (this.netsendDrown_roll + (Rnd(0.8F, 1.0F) * this.parts[i].pro.dmgRoll * (this.parts[i].damageIsFromRight ? -1D : 1.0D)));
                this.netsendDrown_timeS += Rnd(0.7F, 1.3F) * this.parts[i].pro.dmgTime;
            }
        }
        if (this.parts[i].pro.haveGun()) {
            this.arms[this.parts[i].pro.gun_idx].aime.forgetAiming();
            this.arms[this.parts[i].pro.gun_idx].enemy = null;
            if (this.parts[i].pro.TRACKING_ONLY) {
                this.hasTracking--;
            }
        }
        int ai[] = this.hierMesh().getSubTreesSpec(this.parts[i].pro.baseChunkName);
        for (int j = 0; j < ai.length; j++) {
            this.hierMesh().setCurChunk(ai[j]);
            if (!this.hierMesh().isChunkVisible()) {
                continue;
            }
            for (int l = 0; l < this.parts.length; l++) {
                if ((l == i) || (this.parts[l].state == 2) || (ai[j] != this.parts[l].pro.baseChunkIdx)) {
                    continue;
                }
                if (!flag && (this.parts[l].state == 0) && this.parts[l].pro.isItLifeKeeper()) {
                    this.netsendDrown_nparts++;
                    this.netsendDrown_depth += Rnd(0.8F, 1.0F) * this.parts[l].pro.dmgDepth;
                    this.netsendDrown_pitch += Rnd(0.8F, 1.0F) * this.parts[l].pro.dmgPitch;
                    this.netsendDrown_roll = (float) (this.netsendDrown_roll + (Rnd(0.8F, 1.0F) * this.parts[l].pro.dmgRoll * (this.parts[l].damageIsFromRight ? -1D : 1.0D)));
                    this.netsendDrown_timeS += Rnd(0.7F, 1.3F) * this.parts[l].pro.dmgTime;
                }
                this.parts[l].damage = flag ? 0.0F : 1.0F;
                this.parts[l].mirror_initiator = null;
                this.parts[l].state = 2;
                if (this.parts[l].pro.haveGun()) {
                    this.arms[this.parts[l].pro.gun_idx].aime.forgetAiming();
                    this.arms[this.parts[l].pro.gun_idx].enemy = null;
                }
            }

            if (this.hierMesh().chunkName().endsWith("_x") || this.hierMesh().chunkName().endsWith("_X")) {
                this.hierMesh().chunkVisible(false);
                continue;
            }
            String s = this.hierMesh().chunkName() + "_dmg";
            int l1 = this.hierMesh().chunkFindCheck(s);
            if (l1 >= 0) {
                this.hierMesh().chunkVisible(false);
                this.hierMesh().chunkVisible(s, true);
            }
        }

        if (this.pipes != null) {
            boolean flag1 = false;
            for (int i1 = 0; i1 < this.pipes.length; i1++) {
                if (this.pipes[i1] == null) {
                    continue;
                }
                if (this.pipes[i1].pipe == null) {
                    this.pipes[i1] = null;
                    continue;
                }
                int i2 = this.pipes[i1].part_idx;
                if (this.parts[i2].state == 0) {
                    flag1 = true;
                } else {
                    this.pipes[i1].pipe._finish();
                    this.pipes[i1].pipe = null;
                    this.pipes[i1] = null;
                }
            }

            if (!flag1) {
                for (int j1 = 0; j1 < this.pipes.length; j1++) {
                    if (this.pipes[j1] != null) {
                        this.pipes[j1] = null;
                    }
                }

                this.pipes = null;
            }
        }
        if (this.dsmoks != null) {
            for (int k = 0; k < this.dsmoks.length; k++) {
                if ((this.dsmoks[k] == null) || (this.dsmoks[k].pipe != null)) {
                    continue;
                }
                int k1 = this.dsmoks[k].part_idx;
                if (this.parts[k1].state == 0) {
                    continue;
                }
                String s1 = this.parts[k1].pro.baseChunkName;
                Loc loc = new Loc();
                this.hierMesh().setCurChunk(s1);
                this.hierMesh().getChunkLocObj(loc);
                float f = this.parts[k1].pro.stre.EXPLNEAR_MIN_TNT;
                String s2 = "Effects/Smokes/Smoke";
                //boolean flag2 = true;
                if (this.parts[k1].pro.haveGun()) {
                    s2 = s2 + "Gun";
                    int j2;
                    if (f < 6F) {
                        s2 = s2 + "Tiny";
                        j2 = 6;
                    } else if (f < 10F) {
                        s2 = s2 + "Small";
                        j2 = 8;
                    } else if (f < 14F) {
                        s2 = s2 + "Medium";
                        j2 = 12;
                    } else if (f < 18F) {
                        s2 = s2 + "Large";
                        j2 = 16;
                    } else {
                        s2 = s2 + "Huge";
                        j2 = 24;
                    }
                    this.dsmoks[k].pipe = Eff3DActor.New(this, null, loc, 1.0F, s2 + ".eff", j2);
                    Eff3DActor.New(this, null, loc, 1.0F, s2 + "Fire.eff", j2 * 0.5F);
                    continue;
                }
                s2 = s2 + "Ship";
                if (f < 6F) {
                    s2 = s2 + "Tiny";
                } else if (f < 10F) {
                    s2 = s2 + "Small";
                } else if (f < 22F) {
                    s2 = s2 + "Medium";
                } else if (f < 30F) {
                    s2 = s2 + "Large";
                } else if (f < 50F) {
                    s2 = s2 + "Huge";
                } else if (f < 100F) {
                    s2 = s2 + "Enormous";
                } else {
                    s2 = s2 + "Invulnerable";
                }
                if (this instanceof TankerType) {
                    this.dsmoks[k].pipe = Eff3DActor.New(this, null, loc, 1.1F, "Effects/Smokes/SmokeShipTanker.eff", -1F);
                    Eff3DActor.New(this, null, loc, 1.0F, "Effects/Smokes/SmokeShipTankerFire.eff", this.parts[k1].pro.dmgTime);
                } else {
                    this.dsmoks[k].pipe = Eff3DActor.New(this, null, loc, 1.1F, s2 + ".eff", -1F);
                    Eff3DActor.New(this, null, loc, 1.0F, s2 + "Fire.eff", this.parts[k1].pro.dmgTime);
                }
            }

        }
        this.recomputeShotpoints();
        return true;
    }

    void master_sendDrown(float f, float f1, float f2, float f3) {
        if (!this.net.isMirrored()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(100);
            float f4 = f / 1000F;
            if (f4 <= 0.0F) {
                f4 = 0.0F;
            }
            if (f4 >= 1.0F) {
                f4 = 1.0F;
            }
            int i = (int) (f4 * 32767F);
            if (i > 32767) {
                i = 32767;
            }
            if (i < 0) {
                i = 0;
            }
            netmsgguaranted.writeShort(i);
            f4 = f1 / 90F;
            if (f4 <= -1F) {
                f4 = -1F;
            }
            if (f4 >= 1.0F) {
                f4 = 1.0F;
            }
            i = (int) (f4 * 32767F);
            if (i > 32767) {
                i = 32767;
            }
            if (i < -32767) {
                i = -32767;
            }
            netmsgguaranted.writeShort(i);
            f4 = f2 / 90F;
            if (f4 <= -1F) {
                f4 = -1F;
            }
            if (f4 >= 1.0F) {
                f4 = 1.0F;
            }
            i = (int) (f4 * 32767F);
            if (i > 32767) {
                i = 32767;
            }
            if (i < -32767) {
                i = -32767;
            }
            netmsgguaranted.writeShort(i);
            f4 = f3 / 1200F;
            if (f4 <= 0.0F) {
                f4 = 0.0F;
            }
            if (f4 >= 1.0F) {
                f4 = 1.0F;
            }
            i = (int) (f4 * 32767F);
            if (i > 32767) {
                i = 32767;
            }
            if (i < 0) {
                i = 0;
            }
            netmsgguaranted.writeShort(i);
            this.net.post(netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void InjurePart(int i, Actor actor, boolean flag) {
        if (this.isNetMirror()) {
            return;
        }
        this.netsendPartsState_needtosend = true;
        if (!this.visualsInjurePart(i, false)) {
            return;
        }
        if (this.dying != 0) {
            return;
        }
        boolean flag1 = false;
        int j = 0;
        do {
            if (j >= this.parts.length) {
                break;
            }
            if (this.parts[j].pro.isItLifeKeeper() && (this.parts[j].state == 2)) {
                flag1 = true;
                break;
            }
            j++;
        } while (true);
        if (this.netsendDrown_nparts > 0) {
            this.netsendDrown_depth += this.bodyDepth1;
            this.netsendDrown_pitch += this.bodyPitch1;
            this.netsendDrown_roll += this.bodyRoll1;
            this.netsendDrown_timeS /= this.netsendDrown_nparts;
            if (this.netsendDrown_timeS >= 1200F) {
                this.netsendDrown_timeS = 1200F;
            }
            this.tmInterpoStart = NetServerParams.getServerTime();
            this.tmInterpoEnd = this.tmInterpoStart + (long) (this.netsendDrown_timeS * 1000F);
            this.bodyDepth0 = this.bodyDepth;
            this.bodyPitch0 = this.bodyPitch;
            this.bodyRoll0 = this.bodyRoll;
            this.bodyDepth1 = this.netsendDrown_depth;
            this.bodyPitch1 = this.netsendDrown_pitch;
            this.bodyRoll1 = this.netsendDrown_roll;
            this.master_sendDrown(this.netsendDrown_depth, this.netsendDrown_pitch, this.netsendDrown_roll, this.netsendDrown_timeS);
        }
        if (!flag1) {
            return;
        } else {
            this.Die(actor, -1L, flag, true);
            return;
        }
    }

    private float computeSeaDepth(Point3d point3d) {
        for (float f = 5F; f <= 355F; f += 10F) {
            for (float f1 = 0.0F; f1 < 360F; f1 += 30F) {
                float f2 = f * Geom.cosDeg(f1);
                float f3 = f * Geom.sinDeg(f1);
                f2 += (float) point3d.x;
                f3 += (float) point3d.y;
                if (!World.land().isWater(f2, f3)) {
                    return 150F * (f / 355F);
                }
            }

        }

        return 1000F;
    }

    private void computeSinkingParams(long l) {
        if (this.path != null) {
            this.setMovablePosition(l);
        } else {
            this.setPosition();
        }
        this.pos.reset();
        float f = this.computeSeaDepth(this.pos.getAbsPoint()) * Rnd(1.0F, 1.25F);
        if (f >= 400F) {
            f = 400F;
        }
        float f1 = Rnd(0.2F, 0.25F);
        float f2;
        float f3;
        float f4;
        float f5;
        if (f >= 200F) {
            f2 = Rnd(90F, 110F);
            f3 = f2 * f1;
            f4 = 50F - Rnd(0.0F, 20F);
            f5 = Rnd(15F, 32F);
            f1 *= 1.6F;
        } else {
            f2 = Rnd(30F, 40F);
            f3 = f2 * f1;
            f4 = 4.5F - Rnd(0.0F, 2.5F);
            f5 = Rnd(6F, 13F);
        }
        float f6 = (f - f3) / f1;
        if (f6 < 1.0F) {
            f6 = 1.0F;
        }
        float f7 = f6 * f1;
        this.computeInterpolatedDPR(l);
        this.bodyDepth0 = this.bodyDepth;
        this.bodyPitch0 = this.bodyPitch;
        this.bodyRoll0 = this.bodyRoll;
        this.bodyDepth1 += f3;
        this.bodyPitch1 += (this.bodyPitch1 > 0.0D ? 1.0F : -1F) * f4;
        this.bodyRoll1 += (this.bodyRoll1 > 0.0D ? 1.0F : -1F) * f5;
        if (this.bodyPitch1 > 80F) {
            this.bodyPitch1 = 80F;
        }
        if (this.bodyPitch1 < -80F) {
            this.bodyPitch1 = -80F;
        }
        if (this.bodyRoll1 > 80F) {
            this.bodyRoll1 = 80F;
        }
        if (this.bodyRoll1 < -80F) {
            this.bodyRoll1 = -80F;
        }
        float f8 = Config.cur.ini.get("Mods", "SinkTimeMultipiler", 1.0F);
        if (f8 < 0.3F) {
            f8 = 0.3F;
        }
        if (f8 > 10F) {
            f8 = 10F;
        }
        f8 *= Rnd(0.8F, 1.4F);
        this.tmInterpoStart = l;
        this.tmInterpoEnd = this.tmInterpoStart + (long) (f2 * 1000F * 10F * f8);
        this.sink2Depth = this.bodyDepth1 + f7;
        this.sink2Pitch = this.bodyPitch1;
        this.sink2Roll = this.bodyRoll1;
        this.sink2timeWhenStop = this.tmInterpoEnd + (long) (f6 * 1000F);
    }

    private void showExplode() {
        Explosions.Antiaircraft_Explode(this.pos.getAbsPoint());
    }

    private void Die(Actor actor, long l, boolean flag, boolean flag1) {
        if (this.dying != 0) {
            return;
        }
        if (l < 0L) {
            if (this.isNetMirror()) {
                return;
            }
            l = NetServerParams.getServerTime();
        }
        this.dying = 1;
        World.onActorDied(this, actor);
        this.recomputeShotpoints();
        this.forgetAllAiming();
        this.SetEffectsIntens(-1F);
        if (flag1) {
            this.computeSinkingParams(l);
        }
        this.computeInterpolatedDPR(l);
        if (this.path != null) {
            this.setMovablePosition(l);
        } else {
            this.setPosition();
        }
        this.pos.reset();
        this.timeOfDeath = l;
        if (flag) {
            this.showExplode();
        }
        if (flag && this.isNetMaster()) {
            this.send_DeathCommand(actor, null);
        }
        if ((this.airport != null) && (this.zutiBornPlace != null)) {
            this.zutiBornPlace.army = -2;
        }
        this.timeOfSailorsDisappear = SecsToTicks(60F);
    }

    public void destroy() {
        if (this.isDestroyed()) {
            return;
        }
        this.eraseGuns();
        if (this.parts != null) {
            for (int i = 0; i < this.parts.length; i++) {
                this.parts[i].mirror_initiator = null;
                this.parts[i] = null;
            }

            this.parts = null;
        }
        super.destroy();
    }

    private boolean isAnyEnemyNear() {
        NearestEnemies.set(this.WeaponsMask());
        Actor actor = NearestEnemies.getAFoundEnemy(this.pos.getAbsPoint(), 2000D, this.getArmy());
        return actor != null;
    }

    private final FiringDevice GetFiringDevice(ShipAim shipaim) {
        for (int i = 0; i < this.prop.nGuns; i++) {
            if ((this.arms[i] != null) && (this.arms[i].aime == shipaim)) {
                return this.arms[i];
            }
        }

        System.out.println("Internal error 1: Can't find ship gun.");
        return null;
    }

    private final ShipPartProperties GetGunProperties(ShipAim shipaim) {
        for (int i = 0; i < this.prop.nGuns; i++) {
            if (this.arms[i].aime == shipaim) {
                return this.parts[this.arms[i].part_idx].pro;
            }
        }

        System.out.println("Internal error 2: Can't find ship gun.");
        return null;
    }

    private void setGunAngles(FiringDevice firingdevice, float f, float f1) {
        firingdevice.headYaw = f;
        firingdevice.gunPitch = f1;
        ShipPartProperties shippartproperties = this.parts[firingdevice.part_idx].pro;
        this.tmpYPR[1] = 0.0F;
        this.tmpYPR[2] = 0.0F;
        this.hierMesh().setCurChunk(shippartproperties.headChunkIdx);
        this.tmpYPR[0] = firingdevice.headYaw;
        this.hierMesh().chunkSetAngles(this.tmpYPR);
        this.hierMesh().setCurChunk(shippartproperties.gunChunkIdx);
        this.tmpYPR[0] = -(firingdevice.gunPitch - shippartproperties.GUN_STD_PITCH);
        this.hierMesh().chunkSetAngles(this.tmpYPR);
    }

    private void setRadarAngles(RadarDevice radardevice, float f) {
        radardevice.headYaw = f;
        ShipPartProperties shippartproperties = this.parts[radardevice.part_idx].pro;
        this.tmpYPR[1] = 0.0F;
        this.tmpYPR[2] = 0.0F;
        this.hierMesh().setCurChunk(shippartproperties.headChunkIdx);
        this.tmpYPR[0] = radardevice.headYaw;
        this.hierMesh().chunkSetAngles(this.tmpYPR);
    }

    private void rotateRadar(RadarDevice radardevice, float f) {
        radardevice.headYaw += f;
        if (radardevice.headYaw < -180F) {
            radardevice.headYaw += 360F;
        }
        if (radardevice.headYaw > 180F) {
            radardevice.headYaw -= 360F;
        }
        this.setRadarAngles(radardevice, radardevice.headYaw);
    }

    private void eraseGuns() {
        if (this.arms != null) {
            for (int i = 0; i < this.prop.nGuns; i++) {
                if (this.arms[i] == null) {
                    continue;
                }
                if (this.arms[i].aime != null) {
                    this.arms[i].aime.forgetAll();
                    this.arms[i].aime = null;
                }
                if (this.arms[i].gun != null) {
                    destroy(this.arms[i].gun);
                    this.arms[i].gun = null;
                }
                this.arms[i].enemy = null;
                this.arms[i] = null;
            }

            this.arms = null;
        }
    }

    /* private */ void eraseRadars() {
        if (this.radars != null) {
            for (int i = 0; i < this.prop.nRadars; i++) {
                if (this.radars[i] != null) {
                    this.radars[i] = null;
                }
            }

            this.radars = null;
        }
    }

    private void forgetAllAiming() {
        if (this.arms != null) {
            for (int i = 0; i < this.prop.nGuns; i++) {
                if ((this.arms[i] != null) && (this.arms[i].aime != null)) {
                    this.arms[i].aime.forgetAiming();
                    this.arms[i].enemy = null;
                }
            }

        }
    }

    private void CreateGuns() {
        this.arms = new FiringDevice[this.prop.nGuns];
        for (int i = 0; i < this.parts.length; i++) {
            if (!this.parts[i].pro.haveGun()) {
                continue;
            }
            ShipPartProperties shippartproperties = this.parts[i].pro;
            int j = shippartproperties.gun_idx;
            this.arms[j] = new FiringDevice();
            this.arms[j].gun_idx = j;
            this.arms[j].part_idx = i;
            this.arms[j].typeOfMissile = shippartproperties.TypeOfTarget;
            this.arms[j].missileLauncherType = shippartproperties.LauncherType;
            this.arms[j].gun = null;
            this.arms[j].missileName = null;
            if (this.arms[j].typeOfMissile == 0) {
                try {
                    this.arms[j].gun = (Gun) shippartproperties.gunClass.newInstance();
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    System.out.println("BigShip: Can't create gun '" + shippartproperties.gunClass.getName() + "'");
                }
                this.arms[j].gun.set(this, shippartproperties.gunShellStartHookName);
                this.arms[j].gun.loadBullets(-1);
            } else {
                try {
//                    Class class1 = Class.forName(shippartproperties.missileName);
//                    Rocket rocket = (Rocket)class1.newInstance();
                    Class.forName(shippartproperties.missileName).newInstance();
                } catch (Exception exception1) {
                    System.out.println(exception1.getMessage());
                    exception1.printStackTrace();
                    System.out.println("BigShip: Can't create missile '" + shippartproperties.missileName + "'");
                }
                this.arms[j].missileName = shippartproperties.missileName;
            }
            if (shippartproperties.TRACKING_ONLY) {
                this.arms[j].aime = new ShipAim(this, this.isNetMirror(), this.SLOWFIRE_K * (shippartproperties.DELAY_BEFORE_FIRST_SHOOT <= 0.0F ? 1.0F : shippartproperties.DELAY_BEFORE_FIRST_SHOOT), true);
                this.hasTracking++;
            } else {
                this.arms[j].aime = new ShipAim(this, this.isNetMirror(), this.SLOWFIRE_K * (shippartproperties.DELAY_BEFORE_FIRST_SHOOT <= 0.0F ? 1.0F : shippartproperties.DELAY_BEFORE_FIRST_SHOOT));
            }
            this.arms[j].enemy = null;
        }

    }

    private void CreateRadars() {
        this.radars = new RadarDevice[this.prop.nRadars];
        for (int i = 0; i < this.parts.length; i++) {
            if (this.parts[i].pro.haveRadar()) {
                ShipPartProperties shippartproperties = this.parts[i].pro;
                int j = shippartproperties.radar_idx;
                this.radars[j] = new RadarDevice();
                this.radars[j].radar_idx = j;
                this.radars[j].part_idx = i;
            }
        }

    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    private void initMeshBasedProperties() {
        for (int i = 0; i < this.prop.propparts.length; i++) {
            ShipPartProperties shippartproperties = this.prop.propparts[i];
            if (shippartproperties.baseChunkIdx >= 0) {
                continue;
            }
            shippartproperties.baseChunkIdx = this.hierMesh().chunkFind(shippartproperties.baseChunkName);
            this.hierMesh().setCurChunk(shippartproperties.baseChunkIdx);
            this.hierMesh().getChunkLocObj(this.tmpL);
            this.tmpL.get(this.p1);
            shippartproperties.partOffs = new Point3f();
            shippartproperties.partOffs.set(this.p1);
            shippartproperties.partR = this.hierMesh().getChunkVisibilityR();
            int j = shippartproperties.additCollisChunkName.length;
            for (int k = 0; k < shippartproperties.additCollisChunkName.length; k++) {
                if (this.hierMesh().chunkFindCheck(shippartproperties.additCollisChunkName[k] + "_dmg") >= 0) {
                    j++;
                }
            }

            if (this.hierMesh().chunkFindCheck(shippartproperties.baseChunkName + "_dmg") >= 0) {
                j++;
            }
            shippartproperties.additCollisChunkIdx = new int[j];
            j = 0;
            for (int l = 0; l < shippartproperties.additCollisChunkName.length; l++) {
                shippartproperties.additCollisChunkIdx[j++] = this.hierMesh().chunkFind(shippartproperties.additCollisChunkName[l]);
                int j1 = this.hierMesh().chunkFindCheck(shippartproperties.additCollisChunkName[l] + "_dmg");
                if (j1 >= 0) {
                    shippartproperties.additCollisChunkIdx[j++] = j1;
                }
            }

            int i1 = this.hierMesh().chunkFindCheck(shippartproperties.baseChunkName + "_dmg");
            if (i1 >= 0) {
                shippartproperties.additCollisChunkIdx[j++] = i1;
            }
            if (j != shippartproperties.additCollisChunkIdx.length) {
                System.out.println("*** bigship: collis internal error");
            }
            if (shippartproperties.haveGun()) {
                shippartproperties.headChunkIdx = this.hierMesh().chunkFind(shippartproperties.headChunkName);
                shippartproperties.gunChunkIdx = this.hierMesh().chunkFind(shippartproperties.gunChunkName);
                this.hierMesh().setCurChunk(shippartproperties.headChunkIdx);
                this.hierMesh().getChunkLocObj(this.tmpL);
                shippartproperties.fireOffset = new Point3d();
                this.tmpL.get(shippartproperties.fireOffset);
                shippartproperties.fireOrient = new Orient();
                this.tmpL.get(shippartproperties.fireOrient);
                Vector3d vector3d = new Vector3d();
                Vector3d vector3d2 = new Vector3d();
                vector3d.set(1.0D, 0.0D, 0.0D);
                vector3d2.set(1.0D, 0.0D, 0.0D);
                this.tmpL.transform(vector3d);
                this.hierMesh().setCurChunk(shippartproperties.gunChunkIdx);
                this.hierMesh().getChunkLocObj(this.tmpL);
                this.tmpL.transform(vector3d2);
                shippartproperties.GUN_STD_PITCH = Geom.RAD2DEG((float) vector3d.angle(vector3d2));
                continue;
            }
            if (shippartproperties.haveRadar()) {
                shippartproperties.headChunkIdx = this.hierMesh().chunkFind(shippartproperties.headChunkName);
                shippartproperties.gunChunkIdx = -1;
                this.hierMesh().setCurChunk(shippartproperties.headChunkIdx);
                this.hierMesh().getChunkLocObj(this.tmpL);
                shippartproperties.fireOffset = new Point3d();
                this.tmpL.get(shippartproperties.fireOffset);
                shippartproperties.fireOrient = new Orient();
                this.tmpL.get(shippartproperties.fireOrient);
                Vector3d vector3d1 = new Vector3d();
                Vector3d vector3d3 = new Vector3d();
                vector3d1.set(1.0D, 0.0D, 0.0D);
                vector3d3.set(1.0D, 0.0D, 0.0D);
                this.tmpL.transform(vector3d1);
            }
        }

        this.initMeshMats();
    }

    private void initMeshMats() {
        if (Config.cur.b3dgunners) {
            return;
        } else {
            this.hierMesh().materialReplaceToNull("Sailor");
            this.hierMesh().materialReplaceToNull("Sailor1o");
            this.hierMesh().materialReplaceToNull("Sailor2p");
            this.hierMesh().materialReplaceToNull("marina");
            this.hierMesh().materialReplaceToNull("mariM");
            this.hierMesh().materialReplaceToNull("Crew");
            return;
        }
    }

    private void disappearSailorsMats() {
        if (Config.cur.b3dgunners) {
            try {
                this.hierMesh().materialReplace("Sailor", "Sailor_dis");
                this.hierMesh().materialReplace("Sailor1o", "Sailor1o_dis");
                this.hierMesh().materialReplace("Sailor2p", "Sailor2p_dis");
                this.hierMesh().materialReplace("marina", "marina_dis");
                this.hierMesh().materialReplace("mariM", "mariM_dis");
                this.hierMesh().materialReplace("Crew", "Crew_dis");
                this.hierMesh().materialReplace("SailorUSo", "SailorUSo_dis");
            } catch (Exception exception) {
            }
        }
        this.bSailorsDisappear = true;
    }

    private void appearSailorsMats() {
        if (Config.cur.b3dgunners) {
            this.hierMesh().materialReplace("Sailor", "Sailor");
            this.hierMesh().materialReplace("Sailor1o", "Sailor1o");
            this.hierMesh().materialReplace("Sailor2p", "Sailor2p");
            this.hierMesh().materialReplace("marina", "marina");
            this.hierMesh().materialReplace("mariM", "mariM");
            this.hierMesh().materialReplace("Crew", "Crew");
            this.hierMesh().materialReplace("SailorUSo", "SailorUSo");
        }
        this.bSailorsDisappear = false;
    }

    private void makeLive() {
        this.dying = 0;
        for (int i = 0; i < this.parts.length; i++) {
            this.parts[i].damage = 0.0F;
            this.parts[i].state = 0;
            this.parts[i].pro = this.prop.propparts[i];
        }

        for (int j = 0; j < this.hierMesh().chunks(); j++) {
            this.hierMesh().setCurChunk(j);
            if (this.hierMesh().chunkName().equals("Red") || ((this instanceof TransparentTestRunway) && this.hierMesh().chunkName().equals("Hull1"))) {
                continue;
            }
            boolean flag = !this.hierMesh().chunkName().endsWith("_dmg");
            if (this.hierMesh().chunkName().startsWith("ShdwRcv")) {
                flag = false;
            }
            this.hierMesh().chunkVisible(flag);
        }

        this.recomputeShotpoints();
    }

    private void setDefaultLivePose() {
        int i = this.hierMesh().hookFind("Ground_Level");
        if (i != -1) {
            Matrix4d matrix4d = new Matrix4d();
            this.hierMesh().hookMatrix(i, matrix4d);
        }
        for (int j = 0; j < this.arms.length; j++) {
            int l = this.arms[j].part_idx;
            this.setGunAngles(this.arms[j], this.parts[l].pro.HEAD_STD_YAW, this.parts[l].pro.GUN_STD_PITCH);
        }

        for (int k = 0; k < this.radars.length; k++) {
            int i1 = this.radars[k].part_idx;
            this.setRadarAngles(this.radars[k], this.parts[i1].pro.HEAD_STD_YAW);
        }

        this.bodyDepth = 0.0F;
        this.align();
    }

    protected BigshipGeneric() {
        this(constr_arg1, constr_arg2);
    }

    private BigshipGeneric(ShipProperties shipproperties, ActorSpawnArg actorspawnarg) {
        super(shipproperties.meshName);
        this.netsendDrown_pitch = 0.0F;
        this.netsendDrown_roll = 0.0F;
        this.netsendDrown_depth = 0.0F;
        this.netsendDrown_timeS = 0.0F;
        this.netsendDrown_nparts = 0;
        this.p = new Point3d();
        this.p1 = new Point3d();
        this.p2 = new Point3d();
//        tmpvf = new Vector3f();
        this.tmpvd = new Vector3d();
        this.tmpYPR = new float[3];
//        tmpf6 = new float[6];
        this.tmpL = new Loc();
//        tmpBitsState = new byte[32];
        this.tmpDir = new Vector3d();
        this.BlastDeflectorControl = new float[4];
        this.bHasBlastDeflectorControl = false;
        this.blastDeflector = new float[4];
        this.blastDeflector_ = new float[4];
        this.effmeatball = new Eff3DActor[2];
        this.effdatum = new Eff3DActor[14];
        this.effcut = new Eff3DActor[4];
        this.effwaveoff = new Eff3DActor[4];
        this.effemgwaveoff = new Eff3DActor[2];
        this.effreserve1 = new Eff3DActor[2];
        this.effreserve2 = new Eff3DActor[2];
//        actorme = this;
        this.Meatballxyz = new float[3];
        this.Meatballypr = new float[3];
        this.meatballmoving = false;
        this.distanceVla = 1000F;
        this.meatballpos = 0;
        this.insideOfGlidepath = false;
        this.cutlighton = false;
        this.waveofflighton = false;
        this.emgwaveofflighton = false;
//        bMlaVisible = true;
        this.bHasMirrorLA = false;
        this.bInitDoneMirrorLA = false;
        this.bHasUSIflols = false;
        this.bInitDoneUSIflols = false;
        this.bHasFRFlols = false;
        this.bInitDoneFRFlols = false;
        this.CURRSPEED = 1.0F;
        this.isTurning = false;
        this.isTurningBackward = false;
        this.mustRecomputePath = false;
        this.mustSendSpeedToNet = false;
        this.hasTracking = 0;
        this.prop = null;
        this.netsendPartsState_lasttimeMS = 0L;
        this.netsendPartsState_needtosend = false;
        this.netsendFire_lasttimeMS = 0L;
        this.netsendFire_armindex = 0;
        this.arms = null;
        this.radars = null;
        this.parts = null;
        this.shotpoints = null;
        this.netsendDmg_lasttimeMS = 0L;
        this.netsendDmg_partindex = 0;
        this.cachedSeg = 0;
        this.bSailorsDisappear = false;
        this.timeOfSailorsDisappear = 0L;
        this.timeOfDeath = 0L;
        this.dying = 0;
        this.respawnDelay = 0L;
        this.wakeupTmr = 0L;
        this.radarTmr = 0L;
        this.DELAY_WAKEUP = 0.0F;
        this.SKILL_IDX = 2;
        this.SLOWFIRE_K = 1.0F;
        this.pipes = null;
        this.dsmoks = null;
        this.noseW = null;
        this.nose = null;
        this.tail = null;
        this.o = new Orient();
        this.rollAmp = (0.7F * Mission.curCloudsType()) / shipproperties.STABILIZE_FACTOR;
        this.rollPeriod = 12345;
        this.rollWAmp = (this.rollAmp * 19739.208802178713D) / (180 * this.rollPeriod);
        this.pitchAmp = (0.1F * Mission.curCloudsType()) / shipproperties.STABILIZE_FACTOR;
        this.pitchPeriod = 23456;
        this.pitchWAmp = (this.pitchAmp * 19739.208802178713D) / (180 * this.pitchPeriod);
        this.W = new Vector3d(0.0D, 0.0D, 0.0D);
        this.N = new Vector3d(0.0D, 0.0D, 1.0D);
        this.tmpV = new Vector3d();
        this.initOr = new Orient();
        this.initLoc = new Loc();
        this.airport = null;
        this.towPortNum = -1;
        this.zutiBornPlace = null;
        this.zutiIsClassBussy = false;
        for (int i = 0; i < 4; i++) {
            this.BlastDeflectorControl[i] = 0.0F;
            this.blastDeflector[i] = 0.0F;
            this.blastDeflector_[i] = 0.0F;
        }

        this.dvBlastDeflector = 0.3F;
        this.prop = shipproperties;
        if (this instanceof TransparentTestRunway) {
            this.hideTransparentRunwayRed();
        }
        this.CURRSPEED = this.prop.SPEED;
        this.initMeshBasedProperties();
        actorspawnarg.setStationary(this);
        this.path = null;
        this.collide(true);
        this.drawing(true);
        this.tmInterpoStart = this.tmInterpoEnd = 0L;
        this.bodyDepth = this.bodyPitch = this.bodyRoll = 0.0F;
        this.bodyDepth0 = this.bodyPitch0 = this.bodyRoll0 = 0.0F;
        this.bodyDepth1 = this.bodyPitch1 = this.bodyRoll1 = 0.0F;
        this.shipYaw = actorspawnarg.orient.getYaw();
        this.setPosition();
        this.pos.reset();
        this.parts = new Part[this.prop.propparts.length];
        for (int j = 0; j < this.parts.length; j++) {
            this.parts[j] = new Part();
        }

        this.makeLive();
        this.appearSailorsMats();
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.SKILL_IDX = Chief.new_SKILL_IDX;
        this.SLOWFIRE_K = Chief.new_SLOWFIRE_K;
        this.DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
        this.wakeupTmr = 0L;
        this.radarTmr = 0L;
        this.CreateGuns();
        this.CreateRadars();
        int k = 0;
        for (int l = 0; l < this.parts.length; l++) {
            if (this.parts[l].pro.isItLifeKeeper() || this.parts[l].pro.haveGun()) {
                k++;
            }
        }

        if (k <= 0) {
            this.dsmoks = null;
        } else {
            this.dsmoks = new Pipe[k];
            k = 0;
            for (int i1 = 0; i1 < this.parts.length; i1++) {
                if (this.parts[i1].pro.isItLifeKeeper() || this.parts[i1].pro.haveGun()) {
                    this.dsmoks[k] = new Pipe();
                    this.dsmoks[k].part_idx = i1;
                    this.dsmoks[k].pipe = null;
                    k++;
                }
            }

        }
        this.setDefaultLivePose();
        if (!this.isNetMirror() && (this.prop.nGuns > 0) && (this.DELAY_WAKEUP > 0.0F)) {
            this.wakeupTmr = -SecsToTicks(Rnd(2.0F, 7F));
        }
        if ((this instanceof TransparentTestRunway) && Engine.land().isWater(this.pos.getAbs().getX(), this.pos.getAbs().getY())) {
            this.hierMesh().chunkVisible("Hull1", false);
        }
        this.createAirport();
        if (!this.interpEnd("move")) {
            this.interpPut(new Move(), "move", Time.current(), null);
            InterpolateAdapter.forceListener(this);
        }
    }

    public BigshipGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
        this.netsendDrown_pitch = 0.0F;
        this.netsendDrown_roll = 0.0F;
        this.netsendDrown_depth = 0.0F;
        this.netsendDrown_timeS = 0.0F;
        this.netsendDrown_nparts = 0;
        this.p = new Point3d();
        this.p1 = new Point3d();
        this.p2 = new Point3d();
//        tmpvf = new Vector3f();
        this.tmpvd = new Vector3d();
        this.tmpYPR = new float[3];
//        tmpf6 = new float[6];
        this.tmpL = new Loc();
//        tmpBitsState = new byte[32];
        this.tmpDir = new Vector3d();
        this.BlastDeflectorControl = new float[4];
        this.bHasBlastDeflectorControl = false;
        this.blastDeflector = new float[4];
        this.blastDeflector_ = new float[4];
        this.effmeatball = new Eff3DActor[2];
        this.effdatum = new Eff3DActor[14];
        this.effcut = new Eff3DActor[4];
        this.effwaveoff = new Eff3DActor[4];
        this.effemgwaveoff = new Eff3DActor[2];
        this.effreserve1 = new Eff3DActor[2];
        this.effreserve2 = new Eff3DActor[2];
//        actorme = this;
        this.Meatballxyz = new float[3];
        this.Meatballypr = new float[3];
        this.meatballmoving = false;
        this.distanceVla = 1000F;
        this.meatballpos = 0;
        this.insideOfGlidepath = false;
        this.cutlighton = false;
        this.waveofflighton = false;
        this.emgwaveofflighton = false;
//        bMlaVisible = true;
        this.bHasMirrorLA = false;
        this.bInitDoneMirrorLA = false;
        this.bHasUSIflols = false;
        this.bInitDoneUSIflols = false;
        this.bHasFRFlols = false;
        this.bInitDoneFRFlols = false;
        this.CURRSPEED = 1.0F;
        this.isTurning = false;
        this.isTurningBackward = false;
        this.mustRecomputePath = false;
        this.mustSendSpeedToNet = false;
        this.hasTracking = 0;
        this.prop = null;
        this.netsendPartsState_lasttimeMS = 0L;
        this.netsendPartsState_needtosend = false;
        this.netsendFire_lasttimeMS = 0L;
        this.netsendFire_armindex = 0;
        this.arms = null;
        this.radars = null;
        this.parts = null;
        this.shotpoints = null;
        this.netsendDmg_lasttimeMS = 0L;
        this.netsendDmg_partindex = 0;
        this.cachedSeg = 0;
        this.bSailorsDisappear = false;
        this.timeOfSailorsDisappear = 0L;
        this.timeOfDeath = 0L;
        this.dying = 0;
        this.respawnDelay = 0L;
        this.wakeupTmr = 0L;
        this.radarTmr = 0L;
        this.DELAY_WAKEUP = 0.0F;
        this.SKILL_IDX = 2;
        this.SLOWFIRE_K = 1.0F;
        this.pipes = null;
        this.dsmoks = null;
        this.noseW = null;
        this.nose = null;
        this.tail = null;
        this.o = new Orient();
        this.W = new Vector3d(0.0D, 0.0D, 0.0D);
        this.N = new Vector3d(0.0D, 0.0D, 1.0D);
        this.tmpV = new Vector3d();
        this.initOr = new Orient();
        this.initLoc = new Loc();
        this.airport = null;
        this.towPortNum = -1;
        this.zutiBornPlace = null;
        this.zutiIsClassBussy = false;
        if (this instanceof TransparentTestRunway) {
            this.hideTransparentRunwayRed();
        }
        for (int j = 0; j < 4; j++) {
            this.BlastDeflectorControl[j] = 0.0F;
            this.blastDeflector[j] = 0.0F;
            this.blastDeflector_[j] = 0.0F;
        }

        this.dvBlastDeflector = 0.3F;
        try {
            int k = sectfile.sectionIndex(s1);
            String s3 = sectfile.var(k, 0);
            Object obj = Spawn.get(s3);
            if (obj == null) {
                throw new ActorException("Ship: Unknown class of ship (" + s3 + ")");
            }
            this.prop = ((SPAWN) obj).proper;
            try {
                this.setMesh(this.prop.meshName);
            } catch (RuntimeException runtimeexception) {
                super.destroy();
                throw runtimeexception;
            }
            this.initMeshBasedProperties();
            if (this.prop.soundName != null) {
                this.newSound(this.prop.soundName, true);
            }
            this.setName(s);
            this.setArmy(i);
            this.LoadPath(sectfile1, s2);
            this.cachedSeg = 0;
            this.tmInterpoStart = this.tmInterpoEnd = 0L;
            this.rollAmp = (0.7F * Mission.curCloudsType()) / this.prop.STABILIZE_FACTOR;
            this.rollPeriod = 12345;
            this.rollWAmp = (this.rollAmp * 19739.208802178713D) / (180 * this.rollPeriod);
            this.pitchAmp = (0.1F * Mission.curCloudsType()) / this.prop.STABILIZE_FACTOR;
            this.pitchPeriod = 23456;
            this.pitchWAmp = (this.pitchAmp * 19739.208802178713D) / (180 * this.pitchPeriod);
            this.bodyDepth = this.bodyPitch = this.bodyRoll = 0.0F;
            this.bodyDepth0 = this.bodyPitch0 = this.bodyRoll0 = 0.0F;
            this.bodyDepth1 = this.bodyPitch1 = this.bodyRoll1 = 0.0F;
            this.CURRSPEED = 2.0F * this.prop.SPEED;
            this.setMovablePosition(NetServerParams.getServerTime());
            this.pos.reset();
            this.collide(true);
            this.drawing(true);
            this.parts = new Part[this.prop.propparts.length];
            for (int l = 0; l < this.parts.length; l++) {
                this.parts[l] = new Part();
            }

            this.makeLive();
            this.appearSailorsMats();
            int i1 = 0;
            for (int j1 = 0; j1 <= 10; j1++) {
                String s4 = "Vapor";
                if (j1 > 0) {
                    s4 = s4 + (j1 - 1);
                }
                if (this.mesh().hookFind(s4) >= 0) {
                    i1++;
                }
                s4 = "VaporCoal";
                if (j1 > 0) {
                    s4 = s4 + (j1 - 1);
                }
                if (this.mesh().hookFind(s4) >= 0) {
                    i1++;
                }
                s4 = "Diesel";
                if (j1 > 0) {
                    s4 = s4 + (j1 - 1);
                }
                if (this.mesh().hookFind(s4) >= 0) {
                    i1++;
                }
            }

            if (i1 <= 0) {
                this.pipes = null;
            } else {
                this.pipes = new Pipe[i1];
                i1 = 0;
                for (int k1 = 0; k1 <= 10; k1++) {
                    String s5 = "Vapor";
                    if (k1 > 0) {
                        s5 = s5 + (k1 - 1);
                    }
                    if (this.mesh().hookFind(s5) < 0) {
                        continue;
                    }
                    this.pipes[i1] = new Pipe();
                    int k2 = this.hierMesh().hookParentChunk(s5);
                    if (k2 < 0) {
                        System.out.println(" *** Bigship: unexpected error in Vapor hook " + s5);
                        this.pipes = null;
                        break;
                    }
                    int j3;
                    for (j3 = 0; (j3 < this.parts.length) && (this.parts[j3].pro.baseChunkIdx != k2); j3++) {
                        ;
                    }
                    if (j3 >= this.parts.length) {
                        System.out.println(" *** Bigship: Vapor hook '" + s5 + "' MUST be linked to baseChunk");
                        this.pipes = null;
                        break;
                    }
                    this.pipes[i1].part_idx = j3;
                    HookNamed hooknamed1 = new HookNamed(this, s5);
                    this.pipes[i1].pipe = Eff3DActor.New(this, hooknamed1, null, 1.0F, "Effects/Smokes/SmokePipeShip.eff", -1F);
                    i1++;
                }

                for (int l1 = 0; l1 <= 10; l1++) {
                    String s6 = "VaporCoal";
                    if (l1 > 0) {
                        s6 = s6 + (l1 - 1);
                    }
                    if (this.mesh().hookFind(s6) < 0) {
                        continue;
                    }
                    this.pipes[i1] = new Pipe();
                    int l2 = this.hierMesh().hookParentChunk(s6);
                    if (l2 < 0) {
                        System.out.println(" *** Bigship: unexpected error in VaporCoal hook " + s6);
                        this.pipes = null;
                        break;
                    }
                    int k3;
                    for (k3 = 0; (k3 < this.parts.length) && (this.parts[k3].pro.baseChunkIdx != l2); k3++) {
                        ;
                    }
                    if (k3 >= this.parts.length) {
                        System.out.println(" *** Bigship: VaporCoal hook '" + s6 + "' MUST be linked to baseChunk");
                        this.pipes = null;
                        break;
                    }
                    this.pipes[i1].part_idx = k3;
                    HookNamed hooknamed2 = new HookNamed(this, s6);
                    this.pipes[i1].pipe = Eff3DActor.New(this, hooknamed2, null, 1.0F, "Effects/Smokes/SmokePipeShipCoal.eff", -1F);
                    i1++;
                }

                for (int i2 = 0; i2 <= 10; i2++) {
                    String s7 = "Diesel";
                    if (i2 > 0) {
                        s7 = s7 + (i2 - 1);
                    }
                    if (this.mesh().hookFind(s7) < 0) {
                        continue;
                    }
                    this.pipes[i1] = new Pipe();
                    int i3 = this.hierMesh().hookParentChunk(s7);
                    if (i3 < 0) {
                        System.out.println(" *** Bigship: unexpected error in Diesel hook " + s7);
                        this.pipes = null;
                        break;
                    }
                    int l3;
                    for (l3 = 0; (l3 < this.parts.length) && (this.parts[l3].pro.baseChunkIdx != i3); l3++) {
                        ;
                    }
                    if (l3 >= this.parts.length) {
                        System.out.println(" *** Bigship: Diesel hook '" + s7 + "' MUST be linked to baseChunk");
                        this.pipes = null;
                        break;
                    }
                    this.pipes[i1].part_idx = l3;
                    HookNamed hooknamed3 = new HookNamed(this, s7);
                    this.pipes[i1].pipe = Eff3DActor.New(this, hooknamed3, null, 1.0F, "Effects/Smokes/SmokePipeShipDiesel.eff", -1F);
                    i1++;
                }

            }
            this.wake[2] = this.wake[1] = this.wake[0] = null;
            this.tail = null;
            this.noseW = null;
            this.nose = null;
            boolean flag = (this.prop.SLIDER_DIST / 2.5F) < 90F;
            if (this.mesh().hookFind("_Prop") >= 0) {
                HookNamedZ0 hooknamedz0 = new HookNamedZ0(this, "_Prop");
                this.tail = Eff3DActor.New(this, hooknamedz0, null, 1.0F, flag ? "3DO/Effects/Tracers/ShipTrail/PropWakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/PropWake.eff", -1F);
            }
            if (this.mesh().hookFind("_Centre") >= 0) {
                Loc loc = new Loc();
                Loc loc1 = new Loc();
                HookNamed hooknamed = new HookNamed(this, "_Left");
                hooknamed.computePos(this, new Loc(), loc);
                HookNamed hooknamed4 = new HookNamed(this, "_Right");
                hooknamed4.computePos(this, new Loc(), loc1);
                float f = (float) loc.getPoint().distance(loc1.getPoint());
                HookNamedZ0 hooknamedz0_2 = new HookNamedZ0(this, "_Centre");
                if (this.mesh().hookFind("_Prop") >= 0) {
                    HookNamedZ0 hooknamedz0_3 = new HookNamedZ0(this, "_Prop");
                    Loc loc2 = new Loc();
                    hooknamedz0_2.computePos(this, new Loc(), loc2);
                    Loc loc3 = new Loc();
                    hooknamedz0_3.computePos(this, new Loc(), loc3);
                    float f1 = (float) loc2.getPoint().distance(loc3.getPoint());
                    this.wake[0] = Eff3DActor.New(this, hooknamedz0_3, new Loc((-f1) * 0.33000000000000002D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/Wake.eff", -1F);
                    this.wake[1] = Eff3DActor.New(this, hooknamedz0_2, new Loc(f1 * 0.14999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff" : "3DO/Effects/Tracers/ShipTrail/WakeS.eff", -1F);
                    this.wake[2] = Eff3DActor.New(this, hooknamedz0_2, new Loc((-f1) * 0.14999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff" : "3DO/Effects/Tracers/ShipTrail/WakeS.eff", -1F);
                } else {
                    this.wake[0] = Eff3DActor.New(this, hooknamedz0_2, new Loc((-f) * 0.29999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, (this.prop.SLIDER_DIST / 2.5D) < 50D ? "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/Wake.eff", -1F);
                }
            }
            if (this.mesh().hookFind("_Nose") >= 0) {
                HookNamedZ0 hooknamedz0_1 = new HookNamedZ0(this, "_Nose");
                this.noseW = Eff3DActor.New(this, hooknamedz0_1, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), 1.0F, "3DO/Effects/Tracers/ShipTrail/SideWave.eff", -1F);
                this.nose = Eff3DActor.New(this, hooknamedz0_1, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), 1.0F, flag ? "3DO/Effects/Tracers/ShipTrail/FrontPuffBoat.eff" : "3DO/Effects/Tracers/ShipTrail/FrontPuff.eff", -1F);
            }
            this.SetEffectsIntens(0.0F);
            int j2 = Mission.cur().getUnitNetIdRemote(this);
            NetChannel netchannel = Mission.cur().getNetMasterChannel();
            if (netchannel == null) {
                this.net = new Master(this);
            } else if (j2 != 0) {
                this.net = new Mirror(this, netchannel, j2);
            }
            this.SKILL_IDX = Chief.new_SKILL_IDX;
            this.SLOWFIRE_K = Chief.new_SLOWFIRE_K;
            this.DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
            this.wakeupTmr = 0L;
            this.radarTmr = 0L;
            this.CreateGuns();
            this.CreateRadars();
            j2 = 0;
            for (int i4 = 0; i4 < this.parts.length; i4++) {
                if (this.parts[i4].pro.isItLifeKeeper() || this.parts[i4].pro.haveGun()) {
                    j2++;
                }
            }

            if (j2 <= 0) {
                this.dsmoks = null;
            } else {
                this.dsmoks = new Pipe[j2];
                j2 = 0;
                for (int j4 = 0; j4 < this.parts.length; j4++) {
                    if (this.parts[j4].pro.isItLifeKeeper() || this.parts[j4].pro.haveGun()) {
                        this.dsmoks[j2] = new Pipe();
                        this.dsmoks[j2].part_idx = j4;
                        this.dsmoks[j2].pipe = null;
                        j2++;
                    }
                }

            }
            this.setDefaultLivePose();
            if (!this.isNetMirror() && (this.prop.nGuns > 0) && (this.DELAY_WAKEUP > 0.0F)) {
                this.wakeupTmr = -SecsToTicks(Rnd(2.0F, 7F));
            }
            this.createAirport();
            if ((this instanceof TransparentTestRunway) && Engine.land().isWater(this.pos.getAbs().getX(), this.pos.getAbs().getY())) {
                this.hierMesh().chunkVisible("Hull1", false);
            }
            if (!this.interpEnd("move")) {
                this.interpPut(new Move(), "move", Time.current(), null);
                InterpolateAdapter.forceListener(this);
            }
        } catch (Exception exception) {
            System.out.println("Ship creation failure:");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            throw new ActorException();
        }
    }

    private void SetEffectsIntens(float f) {
        if (this.dying != 0) {
            f = -1F;
        }
        if (this.pipes != null) {
            boolean flag = false;
            for (int j = 0; j < this.pipes.length; j++) {
                if (this.pipes[j] == null) {
                    continue;
                }
                if (this.pipes[j].pipe == null) {
                    this.pipes[j] = null;
                    continue;
                }
                if (f >= 0.0F) {
                    this.pipes[j].pipe._setIntesity(f);
                    flag = true;
                } else {
                    this.pipes[j].pipe._finish();
                    this.pipes[j].pipe = null;
                    this.pipes[j] = null;
                }
            }

            if (!flag) {
                for (int k = 0; k < this.pipes.length; k++) {
                    if (this.pipes[k] != null) {
                        this.pipes[k] = null;
                    }
                }

                this.pipes = null;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (this.wake[i] == null) {
                continue;
            }
            if (f >= 0.0F) {
                this.wake[i]._setIntesity(f);
            } else {
                this.wake[i]._finish();
                this.wake[i] = null;
            }
        }

        if (this.noseW != null) {
            if (f >= 0.0F) {
                this.noseW._setIntesity(f);
            } else {
                this.noseW._finish();
                this.noseW = null;
            }
        }
        if (this.nose != null) {
            if (f >= 0.0F) {
                this.nose._setIntesity(f);
            } else {
                this.nose._finish();
                this.nose = null;
            }
        }
        if (this.tail != null) {
            if (f >= 0.0F) {
                this.tail._setIntesity(f);
            } else {
                this.tail._finish();
                this.tail = null;
            }
        }
    }

    private void LoadPath(SectFile sectfile, String s) {
        int i = sectfile.sectionIndex(s);
        if (i < 0) {
            throw new ActorException("Ship path: Section [" + s + "] not found");
        }
        int j = sectfile.vars(i);
        if (j < 1) {
            throw new ActorException("Ship path must contain at least 2 nodes");
        }
        this.path = new ArrayList();
        for (int k = 0; k < j; k++) {
            StringTokenizer stringtokenizer = new StringTokenizer(sectfile.line(i, k));
            float f1 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
            float f2 = Float.valueOf(stringtokenizer.nextToken()).floatValue();

            // TODO: By SAS~Storebror - ATTENTION: f4 is unused, but the Token index still must be pushed forward!
//            float f4 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
            stringtokenizer.nextToken();
            // ---

            double d = 0.0D;
            float f7 = 0.0F;
            if (stringtokenizer.hasMoreTokens()) {
                d = Double.valueOf(stringtokenizer.nextToken()).doubleValue();
                if (stringtokenizer.hasMoreTokens()) {
                    Double.valueOf(stringtokenizer.nextToken()).doubleValue();
                    if (stringtokenizer.hasMoreTokens()) {
                        f7 = Float.valueOf(stringtokenizer.nextToken()).floatValue();
                        if (f7 <= 0.0F) {
                            f7 = this.prop.SPEED;
                        }
                    }
                }
            }
            if ((f7 <= 0.0F) && ((k == 0) || (k == (j - 1)))) {
                f7 = this.prop.SPEED;
            }
            if (k >= (j - 1)) {
                d = -1D;
            }
            Segment segment10 = new Segment();
            segment10.posIn = new Point3d(f1, f2, 0.0D);
            if (Math.abs(d) < 0.1D) {
                segment10.timeIn = 0L;
            } else {
                segment10.timeIn = (long) ((d * 60D * 1000D) + (d > 0.0D ? 0.5D : -0.5D));
                if ((k == 0) && (segment10.timeIn < 0L)) {
                    segment10.timeIn = -segment10.timeIn;
                }
            }
            segment10.speedIn = f7;
            segment10.slidersOn = true;
            this.path.add(segment10);
        }

        for (int l = 0; l < (this.path.size() - 1); l++) {
            Segment segment = (Segment) this.path.get(l);
            Segment segment1 = (Segment) this.path.get(l + 1);
            segment.length = (float) segment.posIn.distance(segment1.posIn);
        }

        int i1 = 0;
        float f = ((Segment) this.path.get(i1)).length;
        int j1;
        for (; i1 < (this.path.size() - 1); i1 = j1) {
            j1 = i1 + 1;
            do {
                Segment segment2 = (Segment) this.path.get(j1);
                if (segment2.speedIn > 0.0F) {
                    break;
                }
                f += segment2.length;
                j1++;
            } while (true);
            if ((j1 - i1) <= 1) {
                continue;
            }
            float f3 = ((Segment) this.path.get(i1)).length;
            float f5 = ((Segment) this.path.get(i1)).speedIn;
            float f6 = ((Segment) this.path.get(j1)).speedIn;
            for (int i2 = i1 + 1; i2 < j1; i2++) {
                Segment segment9 = (Segment) this.path.get(i2);
                float f8 = f3 / f;
                segment9.speedIn = (f5 * (1.0F - f8)) + (f6 * f8);
                f += segment9.length;
            }

        }

        for (int k1 = 0; k1 < (this.path.size() - 1); k1++) {
            Segment segment4 = (Segment) this.path.get(k1);
            Segment segment6 = (Segment) this.path.get(k1 + 1);
            if ((segment4.timeIn > 0L) && (segment6.timeIn > 0L)) {
                Segment segment8 = new Segment();
                segment8.posIn = new Point3d(segment4.posIn);
                segment8.posIn.add(segment6.posIn);
                segment8.posIn.scale(0.5D);
                segment8.timeIn = 0L;
                segment8.speedIn = (segment4.speedIn + segment6.speedIn) * 0.5F;
                this.path.add(k1 + 1, segment8);
            }
        }

        for (int l1 = 0; l1 < (this.path.size() - 1); l1++) {
            Segment segment5 = (Segment) this.path.get(l1);
            Segment segment7 = (Segment) this.path.get(l1 + 1);
            segment5.length = (float) segment5.posIn.distance(segment7.posIn);
        }

        Segment segment3 = (Segment) this.path.get(0);
        boolean flag = segment3.timeIn != 0L;
        long l2 = segment3.timeIn;
        for (int j2 = 0; j2 < (this.path.size() - 1); j2++) {
            Segment segment11 = (Segment) this.path.get(j2);
            Segment segment12 = (Segment) this.path.get(j2 + 1);
            segment11.posOut = new Point3d(segment12.posIn);
            segment12.posIn = segment11.posOut;
            float f9 = segment11.speedIn;
            float f10 = segment12.speedIn;
            float f11 = (f9 + f10) * 0.5F;
            if (flag) {
                segment11.speedIn = 0.0F;
                segment11.speedOut = f10;
                float f12 = (((2.0F * segment11.length) / f10) * 1000F) + 0.5F;
                segment11.timeIn = l2;
                segment11.timeOut = segment11.timeIn + (int) f12;
                l2 = segment11.timeOut;
                flag = false;
                continue;
            }
            if (segment12.timeIn == 0L) {
                segment11.speedIn = f9;
                segment11.speedOut = f10;
                float f13 = ((segment11.length / f11) * 1000F) + 0.5F;
                segment11.timeIn = l2;
                segment11.timeOut = segment11.timeIn + (int) f13;
                l2 = segment11.timeOut;
                flag = false;
                continue;
            }
            if (segment12.timeIn > 0L) {
                float f14 = ((segment11.length / f11) * 1000F) + 0.5F;
                long l3 = l2 + (int) f14;
                if (l3 >= segment12.timeIn) {
                    segment12.timeIn = 0L;
                } else {
                    segment11.speedIn = f9;
                    segment11.speedOut = 0.0F;
                    float f17 = (((2.0F * segment11.length) / f9) * 1000F) + 0.5F;
                    segment11.timeIn = l2;
                    segment11.timeOut = segment11.timeIn + (int) f17;
                    l2 = segment12.timeIn;
                    flag = true;
                    continue;
                }
            }
            if (segment12.timeIn == 0L) {
                segment11.speedIn = f9;
                segment11.speedOut = f10;
                float f15 = ((segment11.length / f11) * 1000F) + 0.5F;
                segment11.timeIn = l2;
                segment11.timeOut = segment11.timeIn + (int) f15;
                l2 = segment11.timeOut;
                flag = false;
            } else {
                segment11.speedIn = f9;
                segment11.speedOut = 0.0F;
                float f16 = (((2.0F * segment11.length) / f9) * 1000F) + 0.5F;
                segment11.timeIn = l2;
                segment11.timeOut = segment11.timeIn + (int) f16;
                l2 = segment11.timeOut + -segment12.timeIn;
                flag = true;
            }
        }

        this.path.remove(this.path.size() - 1);
    }

    public void align() {
        this.pos.getAbs(this.p);
        this.p.z = Engine.land().HQ(this.p.x, this.p.y) - this.bodyDepth;
        this.pos.setAbs(this.p);
    }

    private boolean computeInterpolatedDPR(long l) {
        if ((this.tmInterpoStart >= this.tmInterpoEnd) || (l >= this.tmInterpoEnd)) {
            this.bodyDepth = this.bodyDepth1;
            this.bodyPitch = this.bodyPitch1;
            this.bodyRoll = this.bodyRoll1;
            return false;
        }
        if (l <= this.tmInterpoStart) {
            this.bodyDepth = this.bodyDepth0;
            this.bodyPitch = this.bodyPitch0;
            this.bodyRoll = this.bodyRoll0;
            return true;
        } else {
            float f = (float) (l - this.tmInterpoStart) / (float) (this.tmInterpoEnd - this.tmInterpoStart);
            this.bodyDepth = this.bodyDepth0 + ((this.bodyDepth1 - this.bodyDepth0) * f);
            this.bodyPitch = this.bodyPitch0 + ((this.bodyPitch1 - this.bodyPitch0) * f);
            this.bodyRoll = this.bodyRoll0 + ((this.bodyRoll1 - this.bodyRoll0) * f);
            return true;
        }
    }

    private void setMovablePosition(long l) {
        if (this.cachedSeg < 0) {
            this.cachedSeg = 0;
        } else if (this.cachedSeg >= this.path.size()) {
            this.cachedSeg = this.path.size() - 1;
        }
        Segment segment = (Segment) this.path.get(this.cachedSeg);
        if ((segment.timeIn <= l) && (l <= segment.timeOut)) {
            this.SetEffectsIntens(1.0F);
            this.setMovablePosition((float) (l - segment.timeIn) / (float) (segment.timeOut - segment.timeIn));
            return;
        }
        if (l > segment.timeOut) {
            while ((this.cachedSeg + 1) < this.path.size()) {
                Segment segment1 = (Segment) this.path.get(++this.cachedSeg);
                if (l <= segment1.timeIn) {
                    this.SetEffectsIntens(0.0F);
                    this.setMovablePosition(0.0F);
                    return;
                }
                if (l <= segment1.timeOut) {
                    this.SetEffectsIntens(1.0F);
                    this.setMovablePosition((float) (l - segment1.timeIn) / (float) (segment1.timeOut - segment1.timeIn));
                    return;
                }
            }
            this.SetEffectsIntens(-1F);
            this.setMovablePosition(1.0F);
            return;
        }
        while (this.cachedSeg > 0) {
            Segment segment2 = (Segment) this.path.get(--this.cachedSeg);
            if (l >= segment2.timeOut) {
                this.SetEffectsIntens(0.0F);
                this.setMovablePosition(1.0F);
                return;
            }
            if (l >= segment2.timeIn) {
                this.SetEffectsIntens(1.0F);
                this.setMovablePosition((float) (l - segment2.timeIn) / (float) (segment2.timeOut - segment2.timeIn));
                return;
            }
        }
        this.SetEffectsIntens(0.0F);
        this.setMovablePosition(0.0F);
    }

    private void setMovablePosition(float f) {
        Segment segment = (Segment) this.path.get(this.cachedSeg);
        float f1 = (segment.timeOut - segment.timeIn) * 0.001F;
        float f2 = segment.speedIn;
        float f3 = segment.speedOut;
        float f4 = (f3 - f2) / f1;
        f *= f1;
        float f5 = (f2 * f) + (f4 * f * f * 0.5F);
        this.isTurning = false;
        this.isTurningBackward = false;
        int i = this.cachedSeg;
        float f6 = this.prop.SLIDER_DIST - (segment.length - f5);
        if (f6 <= 0.0F) {
            this.p1.interpolate(segment.posIn, segment.posOut, (f5 + this.prop.SLIDER_DIST) / segment.length);
        } else {
            this.isTurning = true;
            do {
                if ((i + 1) >= this.path.size()) {
                    this.p1.interpolate(segment.posIn, segment.posOut, 1.0F + (f6 / segment.length));
                    break;
                }
                segment = (Segment) this.path.get(++i);
                if (f6 <= segment.length) {
                    this.p1.interpolate(segment.posIn, segment.posOut, f6 / segment.length);
                    break;
                }
                f6 -= segment.length;
            } while (true);
        }
        i = this.cachedSeg;
        segment = (Segment) this.path.get(i);
        f6 = this.prop.SLIDER_DIST - f5;
        if ((f6 <= 0.0F) || !segment.slidersOn) {
            this.p2.interpolate(segment.posIn, segment.posOut, (f5 - this.prop.SLIDER_DIST) / segment.length);
        } else {
            this.isTurning = true;
            this.isTurningBackward = true;
            do {
                if (i <= 0) {
                    this.p2.interpolate(segment.posIn, segment.posOut, 0.0F - (f6 / segment.length));
                    break;
                }
                segment = (Segment) this.path.get(--i);
                if (f6 <= segment.length) {
                    this.p2.interpolate(segment.posIn, segment.posOut, 1.0F - (f6 / segment.length));
                    break;
                }
                f6 -= segment.length;
            } while (true);
        }
        if (!Mission.isDogfight() && !this.isTurning && this.mustRecomputePath && (f6 < (-1.5D * this.prop.SLIDER_DIST))) {
            this.computeNewPath();
            this.mustRecomputePath = false;
        }
        this.p.interpolate(this.p1, this.p2, 0.5F);
        this.tmpvd.sub(this.p1, this.p2);
        if (this.tmpvd.lengthSquared() < 0.0010000000474974513D) {
            Segment segment1 = (Segment) this.path.get(this.cachedSeg);
            this.tmpvd.sub(segment1.posOut, segment1.posIn);
        }
        float f7 = (float) (Math.atan2(this.tmpvd.y, this.tmpvd.x) * 57.295779513082323D);
        this.setPosition(this.p, f7);
    }

    public void addRockingSpeed(Vector3d vector3d, Vector3d vector3d1, Point3d point3d) {
        this.tmpV.sub(point3d, this.pos.getAbsPoint());
        this.o.transformInv(this.tmpV);
        this.tmpV.cross(this.W, this.tmpV);
        this.o.transform(this.tmpV);
        vector3d.add(this.tmpV);
        vector3d1.set(this.N);
    }

    private void setPosition(Point3d point3d, float f) {
        this.shipYaw = f;
        float f1 = (float) (NetServerParams.getServerTime() % this.rollPeriod) / (float) this.rollPeriod;
        float f2 = 0.05F * (20F - Math.abs(this.bodyPitch));
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }
        float f3 = this.rollAmp * f2 * (float) Math.sin(f1 * 2.0F * 3.1415926535897931D);
        this.W.x = -this.rollWAmp * f2 * Math.cos(f1 * 2.0F * 3.1415926535897931D);
        f1 = (float) (NetServerParams.getServerTime() % this.pitchPeriod) / (float) this.pitchPeriod;
        float f4 = this.pitchAmp * f2 * (float) Math.sin(f1 * 2.0F * 3.1415926535897931D);
        this.W.y = -this.pitchWAmp * f2 * Math.cos(f1 * 2.0F * 3.1415926535897931D);
        this.o.setYPR(this.shipYaw, this.bodyPitch + f4, this.bodyRoll + f3);
        this.N.set(0.0D, 0.0D, 1.0D);
        this.o.transform(this.N);
        this.initOr.setYPR(this.shipYaw, this.bodyPitch, this.bodyRoll);
        point3d.z = -this.bodyDepth;
        this.pos.setAbs(point3d, this.o);
        this.initLoc.set(point3d, this.initOr);
    }

    private void setPosition() {
        this.o.setYPR(this.shipYaw, this.bodyPitch, this.bodyRoll);
        this.N.set(0.0D, 0.0D, 1.0D);
        this.o.transform(this.N);
        this.pos.setAbs(this.o);
        this.align();
        this.initLoc.set(this.pos.getAbs());
    }

    public int WeaponsMask() {
        return this.prop.WEAPONS_MASK;
    }

    public int HitbyMask() {
        return this.prop.HITBY_MASK;
    }

    public int chooseBulletType(BulletProperties abulletproperties[]) {
        if (this.dying != 0) {
            return -1;
        }
        if (abulletproperties.length == 1) {
            return 0;
        }
        if (abulletproperties.length <= 0) {
            return -1;
        }
        if (abulletproperties[0].power <= 0.0F) {
            return 0;
        }
        if (abulletproperties[1].power <= 0.0F) {
            return 1;
        }
        if (abulletproperties[0].cumulativePower > 0.0F) {
            return 0;
        }
        if (abulletproperties[1].cumulativePower > 0.0F) {
            return 1;
        }
        if (abulletproperties[0].powerType == 0) {
            return 0;
        }
        if (abulletproperties[1].powerType == 0) {
            return 1;
        } else {
            return abulletproperties[0].powerType == 1 ? 1 : 0;
        }
    }

    public int chooseShotpoint(BulletProperties bulletproperties) {
        if (this.dying != 0) {
            return -1;
        }
        if (this.numshotpoints <= 0) {
            return -1;
        } else {
            return this.shotpoints[Rnd(0, this.numshotpoints - 1)];
        }
    }

    public boolean getShotpointOffset(int i, Point3d point3d) {
        if (this.dying != 0) {
            return false;
        }
        if (this.numshotpoints <= 0) {
            return false;
        }
        if (i == 0) {
            if (point3d != null) {
                point3d.set(0.0D, 0.0D, 0.0D);
            }
            return true;
        }
        int j = i - 1;
        if ((j >= this.parts.length) || (j < 0)) {
            return false;
        }
        if (this.parts[j].state == 2) {
            return false;
        }
        if (!this.parts[j].pro.isItLifeKeeper() && !this.parts[j].pro.haveGun()) {
            return false;
        }
        if (point3d != null) {
            point3d.set(this.parts[j].shotpointOffs);
        }
        return true;
    }

    public float AttackMaxDistance() {
        return this.prop.ATTACK_MAX_DISTANCE;
    }

    public float AttackMaxDistance(ShipAim shipaim) {
        return this.GetGunProperties(shipaim).ATTACK_MAX_DISTANCE;
    }

    private void send_DeathCommand(Actor actor, NetChannel netchannel) {
        if (!this.isNetMaster()) {
            return;
        }
        if (netchannel == null) {
            if (Mission.isDeathmatch()) {
                float f = Mission.respawnTime("Bigship");
                this.respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
            } else {
                this.respawnDelay = 0L;
            }
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeLong(this.timeOfDeath);
            netmsgguaranted.writeNetObj(actor == null ? null : ((com.maddox.rts.NetObj) (actor.net)));
            long l = Time.tickNext();
            long l1 = 0L;
            boolean flag = this.dying == 1;
            double d = (flag ? this.bodyDepth1 : this.bodyDepth0) / 1000D;
            if (d <= 0.0D) {
                d = 0.0D;
            }
            if (d >= 1.0D) {
                d = 1.0D;
            }
            int i = (int) (d * 32767D);
            if (i > 32767) {
                i = 32767;
            }
            if (i < 0) {
                i = 0;
            }
            netmsgguaranted.writeShort(i);
            d = (flag ? this.bodyPitch1 : this.bodyPitch0) / 90D;
            if (d <= -1D) {
                d = -1D;
            }
            if (d >= 1.0D) {
                d = 1.0D;
            }
            i = (int) (d * 32767D);
            if (i > 32767) {
                i = 32767;
            }
            if (i < -32767) {
                i = -32767;
            }
            netmsgguaranted.writeShort(i);
            d = (flag ? this.bodyRoll1 : this.bodyRoll0) / 90D;
            if (d <= -1D) {
                d = -1D;
            }
            if (d >= 1.0D) {
                d = 1.0D;
            }
            i = (int) (d * 32767D);
            if (i > 32767) {
                i = 32767;
            }
            if (i < -32767) {
                i = -32767;
            }
            netmsgguaranted.writeShort(i);
            d = (this.tmInterpoEnd - this.tmInterpoStart) / 1000D / 1200D;
            if (flag) {
                l1 = l - this.tmInterpoStart;
            } else {
                d = 0.0D;
            }
            if (d <= 0.0D) {
                d = 0.0D;
            }
            if (d >= 1.0D) {
                d = 1.0D;
            }
            i = (int) (d * 32767D);
            if (i > 32767) {
                i = 32767;
            }
            if (i < 0) {
                i = 0;
            }
            netmsgguaranted.writeShort(i);
            d = this.sink2Depth / 1000D;
            if (d <= 0.0D) {
                d = 0.0D;
            }
            if (d >= 1.0D) {
                d = 1.0D;
            }
            i = (int) (d * 32767D);
            if (i > 32767) {
                i = 32767;
            }
            if (i < 0) {
                i = 0;
            }
            netmsgguaranted.writeShort(i);
            d = this.sink2Pitch / 90D;
            if (d <= -1D) {
                d = -1D;
            }
            if (d >= 1.0D) {
                d = 1.0D;
            }
            i = (int) (d * 32767D);
            if (i > 32767) {
                i = 32767;
            }
            if (i < -32767) {
                i = -32767;
            }
            netmsgguaranted.writeShort(i);
            d = this.sink2Roll / 90D;
            if (d <= -1D) {
                d = -1D;
            }
            if (d >= 1.0D) {
                d = 1.0D;
            }
            i = (int) (d * 32767D);
            if (i > 32767) {
                i = 32767;
            }
            if (i < -32767) {
                i = -32767;
            }
            netmsgguaranted.writeShort(i);
            d = (this.sink2timeWhenStop - this.tmInterpoEnd) / 1000D / 1200D;
            if (!flag) {
                d = (this.tmInterpoEnd - this.tmInterpoStart) / 1000D / 1200D;
                l1 = l - this.tmInterpoStart;
            }
            if (d <= 0.0D) {
                d = 0.0D;
            }
            if (d >= 1.0D) {
                d = 1.0D;
            }
            i = (int) (d * 32767D);
            if (i > 32767) {
                i = 32767;
            }
            if (i < 0) {
                i = 0;
            }
            netmsgguaranted.writeShort(i);
            if (netchannel != null) {
                netmsgguaranted.writeLong(l1);
            }
            if (netchannel == null) {
                this.net.post(netmsgguaranted);
            } else {
                this.net.postTo(netchannel, netmsgguaranted);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void send_RespawnCommand() {
        if (!this.isNetMaster() || !Mission.isDeathmatch()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(82);
            this.net.post(netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        this.netsendPartsState_needtosend = false;
    }

    private void send_bufferized_FireCommand() {
        if (!this.isNetMaster()) {
            return;
        }
        long l = NetServerParams.getServerTime();
        long l1 = Rnd(40, 85);
        if (Math.abs(l - this.netsendFire_lasttimeMS) < l1) {
            return;
        }
        this.netsendFire_lasttimeMS = l;
        if (!this.net.isMirrored()) {
            for (int k = 0; k < this.arms.length; k++) {
                this.arms[k].enemy = null;
            }

            this.netsendFire_armindex = 0;
            return;
        }
        int i = 0;
        int j = 0;
        int i1;
        for (i1 = 0; i1 < this.arms.length; i1++) {
            int j1 = this.netsendFire_armindex + i1;
            if (j1 >= this.arms.length) {
                j1 -= this.arms.length;
            }
            if (this.arms[j1].enemy == null) {
                continue;
            }
            if (this.parts[this.arms[j1].part_idx].state != 0) {
                System.out.println("*** BigShip internal error #0");
                this.arms[j1].enemy = null;
                continue;
            }
            if (!Actor.isValid(this.arms[j1].enemy) || !this.arms[j1].enemy.isNet()) {
                this.arms[j1].enemy = null;
                continue;
            }
            if (i >= 15) {
                break;
            }
            netsendFire_tmpbuff[i].gun_idx = j1;
            netsendFire_tmpbuff[i].enemy = this.arms[j1].enemy;
            netsendFire_tmpbuff[i].timeWhenFireS = this.arms[j1].timeWhenFireS;
            netsendFire_tmpbuff[i].shotpointIdx = this.arms[j1].shotpointIdx;
            this.arms[j1].enemy = null;
            if (this.arms[j1].timeWhenFireS < 0.0D) {
                j++;
            }
            i++;
        }

        for (this.netsendFire_armindex += i1; this.netsendFire_armindex >= this.arms.length; this.netsendFire_armindex -= this.arms.length) {
            ;
        }
        if (i <= 0) {
            return;
        }
        NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
        try {
            netmsgfiltered.writeByte(224 + j);
        } catch (IOException ioexception) {
            System.out.println("BigShip: netmsgfiltered.writeByte(224 + k)");
            // TODO: +++ Resource Leak fixed by SAS~Storebror
            closeNetMsgFiltered(netmsgfiltered);
            // ---
            throw new RuntimeException("Can't register BigShip object");
        }
        for (int k1 = 0; k1 < i; k1++) {
            double d = netsendFire_tmpbuff[k1].timeWhenFireS;
            if (d >= 0.0D) {
                continue;
            }
            try {
                netmsgfiltered.writeByte(netsendFire_tmpbuff[k1].gun_idx);
            } catch (IOException ioexception1) {
                System.out.println("BigShip: netmsgfiltered.writeByte(netsendFire_tmpbuff[j1].gun_idx)");
                // TODO: +++ Resource Leak fixed by SAS~Storebror
                closeNetMsgFiltered(netmsgfiltered);
                // ---
                throw new RuntimeException("Can't register BigShip object");
            }
            try {
                netmsgfiltered.writeNetObj(netsendFire_tmpbuff[k1].enemy.net);
            } catch (IOException ioexception2) {
                System.out.println("BigShip: netmsgfiltered.writeNetObj(netsendFire_tmpbuff[j1].enemy.net)");
                // TODO: +++ Resource Leak fixed by SAS~Storebror
                closeNetMsgFiltered(netmsgfiltered);
                // ---
                throw new RuntimeException("Can't register BigShip object");
            }
            try {
                netmsgfiltered.writeByte(netsendFire_tmpbuff[k1].shotpointIdx);
            } catch (IOException ioexception3) {
                System.out.println("BigShip: netmsgfiltered.writeByte(netsendFire_tmpbuff[j1].shotpointIdx)");
                // TODO: +++ Resource Leak fixed by SAS~Storebror
                closeNetMsgFiltered(netmsgfiltered);
                // ---
                throw new RuntimeException("Can't register BigShip object");
            }
            j--;
        }

        if (j != 0) {
            System.out.println("*** BigShip internal error #5");
            // TODO: +++ Resource Leak fixed by SAS~Storebror
            closeNetMsgFiltered(netmsgfiltered);
            // ---
            return;
        }
        try {
            for (int i2 = 0; i2 < i; i2++) {
                double d1 = netsendFire_tmpbuff[i2].timeWhenFireS;
                if (d1 < 0.0D) {
                    continue;
                }
                double d2 = l * 0.001D;
                double d3 = (d1 - d2) * 1000D;
                if (d3 <= -2000D) {
                    d3 = -2000D;
                }
                if (d3 >= 5000D) {
                    d3 = 5000D;
                }
                d3 = (d3 - -2000D) / 7000D;
                int j2 = (int) (d3 * 255D);
                if (j2 < 0) {
                    j2 = 0;
                }
                if (j2 > 255) {
                    j2 = 255;
                }
                netmsgfiltered.writeByte(j2);
                netmsgfiltered.writeByte(netsendFire_tmpbuff[i2].gun_idx);
                netmsgfiltered.writeNetObj(netsendFire_tmpbuff[i2].enemy.net);
                netmsgfiltered.writeByte(netsendFire_tmpbuff[i2].shotpointIdx);
                netsendFire_tmpbuff[i2].enemy = null;
            }

            netmsgfiltered.setIncludeTime(true);
            this.net.post(l, netmsgfiltered);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
        // TODO: +++ Resource Leak fixed by SAS~Storebror
        finally {
            closeNetMsgFiltered(netmsgfiltered);
        }
        // ---
    }

    // TODO: +++ Resource Leak fixed by SAS~Storebror
    private static void closeNetMsgFiltered(NetMsgFiltered netmsgfiltered) {
        try {
            netmsgfiltered.close();
        } catch (IOException e) {
            // e.printStackTrace();
        }
    }
    // ---

    private void send_bufferized_PartsState() {
        if (!this.isNetMaster()) {
            return;
        }
        if (!this.netsendPartsState_needtosend) {
            return;
        }
        long l = NetServerParams.getServerTime();
        long l1 = Rnd(650, 1100);
        if (Math.abs(l - this.netsendPartsState_lasttimeMS) < l1) {
            return;
        }
        this.netsendPartsState_lasttimeMS = l;
        this.netsendPartsState_needtosend = false;
        if (!this.net.isMirrored()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(83);
            if (!Mission.isDogfight()) {
                int i = 127;
                if ((this.path != null) && (this.CURRSPEED < (2.0F * this.prop.SPEED))) {
                    i = Math.round(this.CURRSPEED);
                    if (i < 0) {
                        i = 0;
                    }
                    if (i > 126) {
                        i = 126;
                    }
                }
                netmsgguaranted.writeByte(i);
            }
            int j = (this.parts.length + 3) / 4;
            int k = 0;
            for (int i1 = 0; i1 < j; i1++) {
                int j1 = 0;
                for (int k1 = 0; k1 < 4; k1++) {
                    if (k < this.parts.length) {
                        int i2 = this.parts[k].state;
                        j1 |= i2 << (k1 * 2);
                    }
                    k++;
                }

                netmsgguaranted.writeByte(j1);
            }

            this.net.post(netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void bufferize_FireCommand(int i, Actor actor, int j, float f) {
        if (!this.isNetMaster()) {
            return;
        }
        if (!this.net.isMirrored()) {
            return;
        }
        if (!Actor.isValid(actor) || !actor.isNet()) {
            return;
        }
        if ((this.arms[i].enemy != null) && (this.arms[i].timeWhenFireS >= 0.0D)) {
            return;
        }
        j &= 0xff;
        this.arms[i].enemy = actor;
        this.arms[i].shotpointIdx = j;
        if (f < 0.0F) {
            this.arms[i].timeWhenFireS = -1D;
        } else {
            this.arms[i].timeWhenFireS = f + (NetServerParams.getServerTime() * 0.001D);
        }
    }

    private void mirror_send_speed() {
        if (!this.isNetMirror()) {
            return;
        }
        if (this.net.masterChannel() instanceof NetChannelInStream) {
            return;
        }
        if (!Mission.isCoop()) {
            return;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(86);
            int i = 127;
            if ((this.path != null) && (this.CURRSPEED < (2.0F * this.prop.SPEED))) {
                i = Math.round(this.CURRSPEED);
                if (i < 0) {
                    i = 0;
                }
                if (i > 126) {
                    i = 126;
                }
            }
            netmsgguaranted.writeByte(i);
            this.net.postTo(this.net.masterChannel(), netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void mirror_send_bufferized_Damage() {
        if (!this.isNetMirror()) {
            return;
        }
        if (this.net.masterChannel() instanceof NetChannelInStream) {
            return;
        }
        long l = NetServerParams.getServerTime();
        long l1 = Rnd(65, 115);
        if (Math.abs(l - this.netsendDmg_lasttimeMS) < l1) {
            return;
        }
        this.netsendDmg_lasttimeMS = l;
        try {
            int i = 0;
            NetMsgFiltered netmsgfiltered = null;
            int j;
            for (j = 0; j < this.parts.length; j++) {
                int k = this.netsendDmg_partindex + j;
                if (k >= this.parts.length) {
                    k -= this.parts.length;
                }
                if ((this.parts[k].state == 2) || (this.parts[k].damage < 0.0078125D)) {
                    continue;
                }
                int i1 = (int) (this.parts[k].damage * 128F);
                if (--i1 < 0) {
                    i1 = 0;
                } else if (i1 > 127) {
                    i1 = 127;
                }
                if (this.parts[k].damageIsFromRight) {
                    i1 |= 0x80;
                }
                if (i <= 0) {
                    netmsgfiltered = new NetMsgFiltered();
                    netmsgfiltered.writeByte(80);
                }
                Actor actor = this.parts[k].mirror_initiator;
                if (!Actor.isValid(actor) || !actor.isNet()) {
                    actor = null;
                }
                this.parts[k].mirror_initiator = null;
                this.parts[k].damage = 0.0F;
                netmsgfiltered.writeByte(k);
                netmsgfiltered.writeByte(i1);
                netmsgfiltered.writeNetObj(actor == null ? null : ((com.maddox.rts.NetObj) (actor.net)));
                if (++i >= 14) {
                    break;
                }
            }

            for (this.netsendDmg_partindex += j; this.netsendDmg_partindex >= this.parts.length; this.netsendDmg_partindex -= this.parts.length) {
                ;
            }
            if (i > 0) {
                netmsgfiltered.setIncludeTime(false);
                this.net.postTo(l, this.net.masterChannel(), netmsgfiltered);
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void createNetObject(NetChannel netchannel, int i) {
        if (netchannel == null) {
            this.net = new Master(this);
        } else {
            this.net = new Mirror(this, netchannel, i);
        }
    }

    public void requestLocationOnCarrierDeck(NetUser netuser, String s) {
        if (!this.isNetMirror()) {
            return;
        }
        try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(93);
            netmsgguaranted.writeNetObj(netuser);
            netmsgguaranted.writeUTF(s);
            this.net.postTo(this.net.masterChannel(), netmsgguaranted);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void handleLocationRequest(NetUser netuser, String s) {
        try {
            Class class1 = ObjIO.classForName(s);
            Object obj = class1.newInstance();
            Aircraft aircraft = (Aircraft) obj;
            String s1 = Property.stringValue(aircraft.getClass(), "FlightModel", null);
            aircraft.FM = new FlightModel(s1);
            aircraft.FM.Gears.set(aircraft.hierMesh());
            Aircraft.forceGear(aircraft.getClass(), aircraft.hierMesh(), 1.0F);
            aircraft.FM.Gears.computePlaneLandPose(aircraft.FM);
            Aircraft.forceGear(aircraft.getClass(), aircraft.hierMesh(), 0.0F);
            if (this.airport != null) {
                Loc loc = this.airport.requestCell(aircraft);
                this.postLocationToMirror(netuser, loc);
            }
            aircraft.FM = null;
            aircraft.destroy();
            aircraft = null;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void postLocationToMirror(NetUser netuser, Loc loc) {
        NetChannel netchannel = null;
        List list = NetEnv.channels();
        int i = 0;
        do {
            if (i >= list.size()) {
                break;
            }
            netchannel = (NetChannel) list.get(i);
            com.maddox.rts.NetObj netobj = netchannel.getMirror(netuser.idRemote());
            if (netuser == netobj) {
                break;
            }
            netchannel = null;
            i++;
        } while (true);
        if (netchannel == null) {
            return;
        }
        try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(93);
            netmsgguaranted.writeDouble(loc.getX());
            netmsgguaranted.writeDouble(loc.getY());
            netmsgguaranted.writeDouble(loc.getZ());
            netmsgguaranted.writeFloat(loc.getAzimut());
            netmsgguaranted.writeFloat(loc.getTangage());
            netmsgguaranted.writeFloat(loc.getKren());
            this.net.postTo(netchannel, netmsgguaranted);
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
        }
    }

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        netmsgguaranted.writeLong(-1L);
        this.net.postTo(netchannel, netmsgguaranted);
        if (this.dying == 0) {
            this.master_sendDrown(this.bodyDepth1, this.bodyPitch1, this.bodyRoll1, (this.tmInterpoEnd - NetServerParams.getServerTime()) * 1000F);
        } else {
            this.send_DeathCommand(null, netchannel);
        }
        this.netsendPartsState_needtosend = true;
    }

    public float getReloadingTime(ShipAim shipaim) {
        return this.SLOWFIRE_K * this.GetGunProperties(shipaim).DELAY_AFTER_SHOOT;
    }

    public float chainFireTime(ShipAim shipaim) {
        float f = this.GetGunProperties(shipaim).CHAINFIRE_TIME;
        return f <= 0.0F ? 0.0F : f * Rnd(0.75F, 1.25F);
    }

    public float probabKeepSameEnemy(Actor actor) {
        return 0.75F;
    }

    public float minTimeRelaxAfterFight() {
        return 0.1F;
    }

    public void gunStartParking(ShipAim shipaim) {
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        ShipPartProperties shippartproperties = this.parts[firingdevice.part_idx].pro;
        shipaim.setRotationForParking(firingdevice.headYaw, firingdevice.gunPitch, shippartproperties.HEAD_STD_YAW, shippartproperties.GUN_STD_PITCH, shippartproperties.HEAD_YAW_RANGE, shippartproperties.HEAD_MAX_YAW_SPEED, shippartproperties.GUN_MAX_PITCH_SPEED);
    }

    public void gunInMove(boolean flag, ShipAim shipaim) {
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        float f = shipaim.t();
        float f1 = shipaim.anglesYaw.getDeg(f);
        float f2 = shipaim.anglesPitch.getDeg(f);
        this.setGunAngles(firingdevice, f1, f2);
        this.pos.inValidate(false);
    }

    public Actor findEnemy(ShipAim shipaim) {
        if (this.isNetMirror()) {
            return null;
        }
        ShipPartProperties shippartproperties = this.GetGunProperties(shipaim);
        Actor actor = null;
        this.o.add(shippartproperties.fireOrient, this.pos.getAbs().getOrient());
        if (shippartproperties.PREFER_SLOW_TARGET) {
            NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, KmHourToMSec(100F), shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT);
            actor = NearestEnemies.getAFoundEnemyInShoot(this.pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, this.getArmy(), shippartproperties.partOffs.z, this.o, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG);
            if (actor == null) {
                NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, 9999.9F, shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT);
                actor = NearestEnemies.getAFoundEnemyInShoot(this.pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, this.getArmy(), shippartproperties.partOffs.z, this.o, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG);
            }
        } else {
            switch (shippartproperties.ATTACK_FAST_TARGETS) {
                case 0:
                    NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, KmHourToMSec(100F), shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT);
                    break;

                case 1:
                    NearestEnemies.set(shippartproperties.WEAPONS_MASK, -9999.9F, 9999.9F, shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT);
                    break;

                default:
                    NearestEnemies.set(shippartproperties.WEAPONS_MASK, KmHourToMSec(100F), 9999.9F, shippartproperties.ATTACK_MIN_HEIGHT, shippartproperties.ATTACK_MAX_HEIGHT);
                    break;
            }
            if (shippartproperties.TypeOfTarget == 0) {
                actor = NearestEnemies.getAFoundEnemyInShoot(this.pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, this.getArmy(), shippartproperties.partOffs.z, this.o, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG);
            } else if (shippartproperties.TypeOfTarget == 1) {
                actor = NearestEnemies.getAFoundEnemyInHeading(this.pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, this.getArmy(), shippartproperties.partOffs.z, this.o, shippartproperties.SELF_YAW_RANGE, shippartproperties.NOSELF_YAW_RANGE, shippartproperties.NOHEADING_FLAG, shippartproperties.ATTACK_MIN_DISTANCE);
                if (bLogDetail) {
                    System.out.println("BigShip: in findEnemy(aim), calling NearestEnemies.getAFoundEnemyInHeading");
                    if (actor == null) {
                        System.out.println("        getting actor == null");
                    }
                }
            } else {
                actor = NearestEnemies.getAFoundEnemyShipInHeading(this.pos.getAbsPoint(), shippartproperties.ATTACK_MAX_RADIUS, this.getArmy(), this.o, shippartproperties.SELF_YAW_RANGE, shippartproperties.NOSELF_YAW_RANGE, shippartproperties.NOHEADING_FLAG, shippartproperties.ATTACK_MIN_DISTANCE);
            }
        }
        if (actor == null) {
            return null;
        }
        if (bLogDetail) {
            if (shippartproperties.TypeOfTarget == 0) {
                System.out.println("BigShip: in findEnemy(aim), Enemy is found by Gun!");
            }
            if (shippartproperties.TypeOfTarget == 1) {
                System.out.println("BigShip: in findEnemy(aim), Enemy is found by SAM!");
            }
            if (shippartproperties.TypeOfTarget == 2) {
                System.out.println("BigShip: in findEnemy(aim), Enemy is found by SSM!");
            }
            if (shippartproperties.TypeOfTarget == 3) {
                System.out.println("BigShip: in findEnemy(aim), Enemy is found by SUM!");
            }
        }
        if (!(actor instanceof Prey) && !(actor instanceof RocketBomb)) {
            System.out.println("bigship: nearest enemies: non-Prey");
            return null;
        }
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        BulletProperties bulletproperties = null;
        if (shippartproperties.TypeOfTarget == 0) {
            if (firingdevice.gun.prop != null) {
                int i = ((Prey) actor).chooseBulletType(firingdevice.gun.prop.bullet);
                if (i < 0) {
                    return null;
                }
                bulletproperties = firingdevice.gun.prop.bullet[i];
            }
            int j = ((Prey) actor).chooseShotpoint(bulletproperties);
            if (j < 0) {
                return null;
            }
            shipaim.shotpoint_idx = j;
            double d = this.distance(actor);
            d /= this.AttackMaxDistance(shipaim);
            shipaim.setAimingError(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), 0.0F);
            shipaim.scaleAimingError(23.47F * (4 - this.SKILL_IDX) * (4 - this.SKILL_IDX));
            if (actor instanceof Aircraft) {
                d *= 0.25D;
            }
            shipaim.scaleAimingError((float) d);
            if (this.targetControl() > 0) {
                shipaim.scaleAimingError(0.5F);
            }
            return actor;
        } else {
            shipaim.shotpoint_idx = 0;
            return actor;
        }
    }

    public boolean enterToFireMode(int i, Actor actor, float f, ShipAim shipaim) {
        if (!this.isNetMirror()) {
            FiringDevice firingdevice = this.GetFiringDevice(shipaim);
            this.bufferize_FireCommand(firingdevice.gun_idx, actor, shipaim.shotpoint_idx, i == 0 ? -1F : f);
        }
        return true;
    }

    private void Track_Mirror(int i, Actor actor, int j) {
        if (actor == null) {
            return;
        }
        if ((this.arms == null) || (i < 0) || (i >= this.arms.length) || (this.arms[i].aime == null)) {
            return;
        }
        if (this.parts[this.arms[i].part_idx].state != 0) {
            return;
        } else {
            this.arms[i].aime.passive_StartFiring(0, actor, j, 0.0F);
            return;
        }
    }

    private void Fire_Mirror(int i, Actor actor, int j, float f) {
        if (actor == null) {
            return;
        }
        if ((this.arms == null) || (i < 0) || (i >= this.arms.length) || (this.arms[i].aime == null)) {
            return;
        }
        if (this.parts[this.arms[i].part_idx].state != 0) {
            return;
        }
        if (f <= 0.15F) {
            f = 0.15F;
        }
        if (f >= 7F) {
            f = 7F;
        }
        this.arms[i].aime.passive_StartFiring(1, actor, j, f);
    }

    public boolean isMissile(ShipAim shipaim) {
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        return this.parts[firingdevice.part_idx].pro.TypeOfTarget > 0;
    }

    public int targetGun(ShipAim shipaim, Actor actor, float f, boolean flag) {
        if (!Actor.isValid(actor) || !actor.isAlive() || (actor.getArmy() == 0)) {
            return 0;
        }
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        if (firingdevice.gun instanceof CannonMidrangeGeneric) {
            int i = ((Prey) actor).chooseBulletType(firingdevice.gun.prop.bullet);
            if (i < 0) {
                return 0;
            }
            ((CannonMidrangeGeneric) firingdevice.gun).setBulletType(i);
        }
        boolean flag1 = ((Prey) actor).getShotpointOffset(shipaim.shotpoint_idx, this.p1);
        if (!flag1) {
            return 0;
        }
        ShipPartProperties shippartproperties = this.parts[firingdevice.part_idx].pro;
        float f1 = f * Rnd(0.8F, 1.2F);
        if (!Aimer.aim((BulletAimer) firingdevice.gun, actor, this, f1, this.p1, shippartproperties.fireOffset)) {
            return 0;
        }
        Point3d point3d = new Point3d();
        Aimer.getPredictedTargetPosition(point3d);
        point3d.add(shipaim.getAimingError());
        Point3d point3d1 = Aimer.getHunterFirePoint();
        float f2 = 0.05F;
        double d = point3d.distance(point3d1);
        double d1 = point3d.z;
        if (f1 > 0.001F) {
            Point3d point3d2 = new Point3d();
            actor.pos.getAbs(point3d2);
            point3d2.add(shipaim.getAimingError());
            this.tmpvd.sub(point3d, point3d2);
            double d2 = this.tmpvd.length();
            if (d2 > 0.001D) {
                float f7 = (float) d2 / f1;
                if (f7 > 200F) {
                    f7 = 200F;
                }
                float f8 = f7 * 0.01F;
                point3d2.sub(point3d1);
                double d3 = (point3d2.x * point3d2.x) + (point3d2.y * point3d2.y) + (point3d2.z * point3d2.z);
                if (d3 > 0.01D) {
                    float f9 = (float) this.tmpvd.dot(point3d2);
                    f9 /= (float) (d2 * Math.sqrt(d3));
                    f9 = (float) Math.sqrt(1.0F - (f9 * f9));
                    f8 *= 0.4F + (0.6F * f9);
                }
                f8 *= 1.3F;
                f8 *= ShipAim.AngleErrorKoefForSkill[this.SKILL_IDX];
                int k = Mission.curCloudsType();
                if (k > 2) {
                    float f10 = k > 4 ? 400F : 800F;
                    float f11 = (float) (d / f10);
                    if (f11 > 1.0F) {
                        if (f11 > 10F) {
                            return 0;
                        }
                        f11 = (f11 - 1.0F) / 9F;
                        f8 *= f11 + 1.0F;
                    }
                }
                if ((k >= 3) && (d1 > Mission.curCloudsHeight())) {
                    f8 *= 1.25F;
                }
                if (this.targetControl() > 0) {
                    if (actor instanceof Aircraft) {
                        f8 = (float) (f8 * 0.75D);
                    } else {
                        f8 = (float) (f8 * 0.074999999999999997D);
                    }
                }
                f2 += f8;
            }
        }
        if (actor instanceof Aircraft) {
            shipaim.scaleAimingError(0.73F);
            f2 = (float) (f2 * (0.5D + Aim.AngleErrorKoefForSkill[this.SKILL_IDX] * 0.75F));
        } else if (shipaim.getAimingError().length() > 1.1100000143051147D) {
            shipaim.scaleAimingError(0.973F);
        }
        if (World.Sun().ToSun.z < -0.15F) {
            float f3 = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
            if (f3 >= 1.0F) {
                f3 = 1.0F;
            }
            if ((actor instanceof Aircraft) && ((NetServerParams.getServerTime() - ((Aircraft) actor).tmSearchlighted) < 1000L)) {
                f3 = 0.0F;
            }
            f2 += 10F * f3;
        }
        float f4 = (float) actor.getSpeed(null) - 10F;
        if (f4 > 0.0F) {
            float f5 = 83.33334F;
            f4 = f4 >= f5 ? 1.0F : f4 / f5;
            f2 += f4 * shippartproperties.FAST_TARGETS_ANGLE_ERROR;
        }
        Vector3d vector3d = new Vector3d();
        if (!((BulletAimer) firingdevice.gun).FireDirection(point3d1, point3d, vector3d)) {
            return 0;
        }
        float f6;
        if (flag) {
            f6 = 99999F;
            d1 = 99999D;
        } else {
            f6 = shippartproperties.HEAD_MAX_YAW_SPEED;
            d1 = shippartproperties.GUN_MAX_PITCH_SPEED;
        }
        this.o.add(shippartproperties.fireOrient, this.pos.getAbs().getOrient());
        int j = shipaim.setRotationForTargeting(this, this.o, point3d1, firingdevice.headYaw, firingdevice.gunPitch, vector3d, f2, f1, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG, shippartproperties.GUN_MIN_PITCH, shippartproperties.GUN_MAX_PITCH, f6, (float) d1, 0.0F);
        return j;
    }

    public int targetMissile(ShipAim shipaim, Actor actor, float f, boolean flag) {
        if (bLogDetail) {
            System.out.println("BigShip: Entering 'targetMissile', float f=" + f + ".");
        }
        if (!Actor.isValid(actor) || !actor.isAlive() || (actor.getArmy() == 0)) {
            return 0;
        }
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        boolean flag1 = ((Prey) actor).getShotpointOffset(shipaim.shotpoint_idx, this.p1);
        if (!flag1) {
            return 0;
        }
        ShipPartProperties shippartproperties = this.parts[firingdevice.part_idx].pro;
        float f1 = f * Rnd(0.8F, 1.2F);
        if (!MissileAimer.aim((BulletAimer) firingdevice.gun, actor, this, f1, this.p1, shippartproperties.fireOffset)) {
            return 0;
        }
        Point3d point3d = new Point3d();
        MissileAimer.getPredictedTargetPosition(point3d);
        Point3d point3d1 = MissileAimer.getHunterFirePoint();
        float f2 = 0.05F;
//        double d = point3d.distance(point3d1);
        double d1 = point3d.z;
        point3d.sub(point3d1);
        point3d.scale(Rnd(0.995D, 1.005D));
        point3d.add(point3d1);
        if (f1 > 0.001F) {
            Point3d point3d2 = new Point3d();
            actor.pos.getAbs(point3d2);
            this.tmpvd.sub(point3d, point3d2);
            double d2 = this.tmpvd.length();
            if (d2 > 0.001D) {
                float f4 = (float) d2 / f1;
                if (f4 > 200F) {
                    f4 = 200F;
                }
//                float f5 = f4 * 0.01F;
                point3d2.sub(point3d1);
                double d3 = (point3d2.x * point3d2.x) + (point3d2.y * point3d2.y) + (point3d2.z * point3d2.z);
                if (d3 > 0.01D) {
                    float f6 = (float) this.tmpvd.dot(point3d2);
                    f6 /= (float) (d2 * Math.sqrt(d3));
                    f6 = (float) Math.sqrt(1.0F - (f6 * f6));
//                    f5 *= 0.4F + 0.6F * f6;
                }
//                f5 *= 1.3F;
//                f5 *= ShipAim.AngleErrorKoefForSkill[SKILL_IDX];
            }
        }
        Vector3d vector3d = new Vector3d();
        this.FireDirection(point3d1, point3d, vector3d);
        if (shippartproperties.LauncherType == 2) {
            return 2;
        }
        float f3;
        if (flag) {
            f3 = 99999F;
            d1 = 99999D;
        } else {
            f3 = shippartproperties.HEAD_MAX_YAW_SPEED;
            d1 = shippartproperties.GUN_MAX_PITCH_SPEED;
        }
        this.o.add(shippartproperties.fireOrient, this.pos.getAbs().getOrient());
        if (bLogDetail) {
            System.out.println("BigShip: Going 'aim.setRotationForTargetingMissile'");
        }
        int i = shipaim.setRotationForTargetingMissile(this, this.o, point3d1, firingdevice.headYaw, firingdevice.gunPitch, vector3d, f2, f1, shippartproperties.HEAD_YAW_RANGE, shippartproperties.NOFIRE_YAW_RANGE, shippartproperties.NOFIRE_FLAG, shippartproperties.GUN_MIN_PITCH, shippartproperties.GUN_MAX_PITCH, f3, (float) d1, 0.0F, shippartproperties._HEAD_MIN_YAW, shippartproperties._HEAD_MAX_YAW, shippartproperties._NOFIRE_MAX_YAW, shippartproperties._NOFIRE_MAX_YAW);
        return i;
    }

    public void singleShot(ShipAim shipaim) {
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        if (!this.parts[firingdevice.part_idx].pro.TRACKING_ONLY) {
            if (firingdevice.typeOfMissile == 0) {
                firingdevice.gun.shots(1);
            } else {
                if (bLogDetail) {
                    System.out.println("BigShip: singleShot() - Fire a missile!");
                }
                String s = this.parts[firingdevice.part_idx].pro.gunShellStartHookName;
                HierMesh hiermesh = this.hierMesh();
                Loc loc = new Loc();
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                this.findHook(hiermesh, s, loc);
                if (bLogDetail) {
                    System.out.println("singleShot: startHookName==" + s);
                    System.out.println("singleShot: loc==" + loc);
                }
                point3d = loc.getPoint();
                Orient orient1 = this.pos.getAbs().getOrient();
                double d = (Math.cos(DEG2RAD(orient1.getYaw())) * point3d.x) - (Math.sin(DEG2RAD(orient1.getYaw())) * point3d.y);
                double d1 = (Math.sin(DEG2RAD(orient1.getYaw())) * point3d.x) + (Math.cos(DEG2RAD(orient1.getYaw())) * point3d.y);
                point3d.set((float) d, (float) d1, point3d.z);
                if (bLogDetail) {
                    System.out.println("singleShot: rotated point3d==" + point3d);
                }
                point3d.add(this.pos.getAbsPoint());
                orient = loc.getOrient();
                orient.add(this.pos.getAbs().getOrient());
//                Object obj = null;
                if (bLogDetail) {
                    System.out.println("singleShot: point3d==" + point3d);
                    System.out.println("singleShot: orient==" + orient);
                    System.out.println("singleShot: pos.getAbsPoint()==" + this.pos.getAbsPoint());
                    System.out.println("singleShot: pos.getAbs().getOrient()==" + this.pos.getAbs().getOrient());
                }
//                Object obj1 = null;
//                Object obj2 = null;
//                Object obj3 = null;
                Class aclass[] = new Class[6];
                aclass[0] = com.maddox.il2.engine.Actor.class;
                aclass[1] = com.maddox.rts.NetChannel.class;
                aclass[2] = Integer.TYPE;
                aclass[3] = com.maddox.JGP.Point3d.class;
                aclass[4] = com.maddox.il2.engine.Orient.class;
                aclass[5] = Float.TYPE;
                Object aobj[] = new Object[6];
                aobj[0] = this;
                aobj[1] = null;
                aobj[2] = new Integer(1);
                aobj[3] = point3d;
                aobj[4] = orient;
                aobj[5] = new Float(100F);
                try {
//                    Class class1 = Class.forName(firingdevice.missileName);
//                    Constructor constructor = class1.getConstructor(aclass);
//                    Rocket rocket = (Rocket)constructor.newInstance(aobj);
                    Class.forName(firingdevice.missileName).getConstructor(aclass).newInstance(aobj);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    System.out.println("SPAWN: Can't create missile object [class:" + firingdevice.missileName + "]");
                    return;
                }
            }
        }
    }

    public void startFire(ShipAim shipaim) {
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        if (!this.parts[firingdevice.part_idx].pro.TRACKING_ONLY) {
            if (firingdevice.typeOfMissile == 0) {
                firingdevice.gun.shots(-1);
            } else {
                if (bLogDetail) {
                    System.out.println("BigShip: startFire() - Fire a missile!");
                }
                String s = this.parts[firingdevice.part_idx].pro.gunShellStartHookName;
                HierMesh hiermesh = this.hierMesh();
                Loc loc = new Loc();
                Point3d point3d = new Point3d();
                Orient orient = new Orient();
                this.findHook(hiermesh, s, loc);
                if (bLogDetail) {
                    System.out.println("startFire: startHookName==" + s);
                    System.out.println("startFire: loc==" + loc);
                }
                point3d = loc.getPoint();
                Orient orient1 = this.pos.getAbs().getOrient();
                double d = (Math.cos(DEG2RAD(orient1.getYaw())) * point3d.x) - (Math.sin(DEG2RAD(orient1.getYaw())) * point3d.y);
                double d1 = (Math.sin(DEG2RAD(orient1.getYaw())) * point3d.x) + (Math.cos(DEG2RAD(orient1.getYaw())) * point3d.y);
                point3d.set((float) d, (float) d1, point3d.z);
                if (bLogDetail) {
                    System.out.println("startFire: rotated point3d==" + point3d);
                }
                point3d.add(this.pos.getAbsPoint());
                orient = loc.getOrient();
                orient.add(this.pos.getAbs().getOrient());
//                Object obj = null;
                if (bLogDetail) {
                    System.out.println("startFire: point3d==" + point3d);
                    System.out.println("startFire: orient==" + orient);
                    System.out.println("startFire: pos.getAbsPoint()==" + this.pos.getAbsPoint());
                    System.out.println("startFire: pos.getAbs().getOrient()==" + this.pos.getAbs().getOrient());
                }
//                Object obj1 = null;
//                Object obj2 = null;
//                Object obj3 = null;
                Class aclass[] = new Class[6];
                aclass[0] = com.maddox.il2.engine.Actor.class;
                aclass[1] = com.maddox.rts.NetChannel.class;
                aclass[2] = Integer.TYPE;
                aclass[3] = com.maddox.JGP.Point3d.class;
                aclass[4] = com.maddox.il2.engine.Orient.class;
                aclass[5] = Float.TYPE;
                Object aobj[] = new Object[6];
                aobj[0] = this;
                aobj[1] = null;
                aobj[2] = new Integer(1);
                aobj[3] = point3d;
                aobj[4] = orient;
                aobj[5] = new Float(100F);
                try {
//                    Class class1 = Class.forName(firingdevice.missileName);
//                    Constructor constructor = class1.getConstructor(aclass);
//                    Rocket rocket = (Rocket)constructor.newInstance(aobj);
                    Class.forName(firingdevice.missileName).getConstructor(aclass).newInstance(aobj);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    exception.printStackTrace();
                    System.out.println("SPAWN: Can't create missile object [class:" + firingdevice.missileName + "]");
                    return;
                }
            }
        }
    }

    public void continueFire(ShipAim shipaim) {
    }

    public void stopFire(ShipAim shipaim) {
        FiringDevice firingdevice = this.GetFiringDevice(shipaim);
        if (!this.parts[firingdevice.part_idx].pro.TRACKING_ONLY && (firingdevice.typeOfMissile == 0)) {
            firingdevice.gun.shots(0);
        }
    }

    protected static float DEG2RAD(float f) {
        return f * 0.01745329F;
    }

    private boolean findHook(HierMesh hiermesh, String s, Loc loc) {
        int i = hiermesh.hookFind(s);
        if (i == -1) {
            return false;
        } else {
            double ad[] = new double[3];
            Point3d point3d = new Point3d();
            Orient orient = new Orient();
            Matrix4d matrix4d = new Matrix4d();
            hiermesh.hookMatrix(i, matrix4d);
            matrix4d.getEulers(ad);
            orient.setYPR(Geom.RAD2DEG((float) ad[0]), 360F - Geom.RAD2DEG((float) ad[1]), 360F - Geom.RAD2DEG((float) ad[2]));
            point3d.set(matrix4d.m03, matrix4d.m13, matrix4d.m23);
            loc.set(point3d, orient);
            return true;
        }
    }

    public int targetControl() {
        return this.hasTracking;
    }

    public boolean isVisibilityLong() {
        return true;
    }

    private void createAirport() {
        if (this.prop.propAirport != null) {
            this.prop.propAirport.firstInit(this);
            this.draw = new TowStringMeshDraw(this.draw);
            if (this.prop.propAirport.cellTO != null) {
                this.cellTO = (CellAirField) this.prop.propAirport.cellTO.getClone();
            }
            if (this.prop.propAirport.cellLDG != null) {
                this.cellLDG = (CellAirField) this.prop.propAirport.cellLDG.getClone();
            }
            this.airport = new AirportCarrier(this, this.prop.propAirport.rwy);
        }
    }

    public AirportCarrier getAirport() {
        return this.airport;
    }

    public CellAirField getCellTO() {
        return this.cellTO;
    }

    public CellAirField getCellLDR() {
        return this.cellLDG;
    }

    private void validateTowAircraft() {
        if (this.towPortNum < 0) {
            return;
        }
        if (!Actor.isValid(this.towAircraft)) {
            this.requestDetowAircraft(this.towAircraft);
            return;
        }
        if (this.pos.getAbsPoint().distance(this.towAircraft.pos.getAbsPoint()) > this.hierMesh().visibilityR()) {
            this.requestDetowAircraft(this.towAircraft);
            return;
        }
        if (!this.towAircraft.FM.CT.bHasArrestorControl) {
            this.requestDetowAircraft(this.towAircraft);
            return;
        } else {
            return;
        }
    }

    public void forceTowAircraft(Aircraft aircraft, int i) {
        if (this.towPortNum >= 0) {
            return;
        } else {
            this.towPortNum = i;
            this.towAircraft = aircraft;
            this.towHook = new HookNamed(aircraft, "_ClipAGear");
            return;
        }
    }

    public void requestTowAircraft(Aircraft aircraft) {
        HookNamed hooknamed;
        Point3d apoint3d[];
        Point3d point3d;
        Point3d point3d1;
        Point3d point3d2;
        Point3d point3d3;
        Loc loc;
        Loc loc1;
        if ((this.towPortNum >= 0) || (this.prop.propAirport.towPRel == null)) {
            return;
        }
        hooknamed = new HookNamed(aircraft, "_ClipAGear");
        apoint3d = this.prop.propAirport.towPRel;
        point3d = new Point3d();
        point3d1 = new Point3d();
        point3d2 = new Point3d();
        point3d3 = new Point3d();
        loc = new Loc();
        loc1 = new Loc();
        for (int i = 0; i < (apoint3d.length / 2); i++) {
            Line2d line2d;
            Line2d line2d1;
            this.pos.getCurrent(loc);
            point3d2.set(apoint3d[i + i]);
            loc.transform(point3d2);
            point3d3.set(apoint3d[i + i + 1]);
            loc.transform(point3d3);
            aircraft.pos.getCurrent(loc1);
            loc.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(aircraft, loc1, loc);
            point3d.set(loc.getPoint());
            aircraft.pos.getPrev(loc1);
            loc.set(0.0D, 0.0D, 0.0D, 0.0F, 0.0F, 0.0F);
            hooknamed.computePos(aircraft, loc1, loc);
            point3d1.set(loc.getPoint());
            if (point3d1.z >= (point3d2.z + (0.5D * (point3d3.z - point3d2.z)) + 0.2D)) {
                continue;
            }
            line2d = new Line2d(new Point2d(point3d2.x, point3d2.y), new Point2d(point3d3.x, point3d3.y));
            line2d1 = new Line2d(new Point2d(point3d.x, point3d.y), new Point2d(point3d1.x, point3d1.y));
            try {
                Point2d point2d = line2d.crossPRE(line2d1);
                double d = Math.min(point3d2.x, point3d3.x);
                double d2 = Math.max(point3d2.x, point3d3.x);
                double d4 = Math.min(point3d2.y, point3d3.y);
                double d6 = Math.max(point3d2.y, point3d3.y);
                if ((point2d.x <= d) || (point2d.x >= d2) || (point2d.y <= d4) || (point2d.y >= d6)) {
                    continue;
                }
                double d1 = Math.min(point3d.x, point3d1.x);
                double d3 = Math.max(point3d.x, point3d1.x);
                double d5 = Math.min(point3d.y, point3d1.y);
                double d7 = Math.max(point3d.y, point3d1.y);
                if ((point2d.x > d1) && (point2d.x < d3) && (point2d.y > d5) && (point2d.y < d7)) {
                    this.towPortNum = i;
                    this.towAircraft = aircraft;
                    this.towHook = new HookNamed(aircraft, "_ClipAGear");
                    return;
                }
            } catch (Exception exception) {
            }
        }
    }

    public void requestDetowAircraft(Aircraft aircraft) {
        if (aircraft == this.towAircraft) {
            this.towAircraft = null;
            this.towPortNum = -1;
        }
    }

    public boolean isTowAircraft(Aircraft aircraft) {
        return this.towAircraft == aircraft;
    }

    public double getSpeed(Vector3d vector3d) {
        if (this.path == null) {
            return super.getSpeed(vector3d);
        }
        long l = NetServerParams.getServerTime();
        if (l > Time.tickLen() * 4) {
            return super.getSpeed(vector3d);
        }
        Segment segment = (Segment) this.path.get(0);
        this.tmpDir.sub(segment.posOut, segment.posIn);
        this.tmpDir.normalize();
        this.tmpDir.scale(segment.speedIn);
        if (vector3d != null) {
            vector3d.set(this.tmpDir);
        }
        return this.tmpDir.length();
    }

    private boolean FireDirection(Point3d point3d, Point3d point3d1, Vector3d vector3d) {
        float f = (float) point3d.distance(point3d1);
        vector3d.set(point3d1);
        vector3d.sub(point3d);
        vector3d.scale(1.0F / f);
        return true;
    }

    private void zutiRefreshBornPlace() {
        if ((this.zutiBornPlace == null) || this.zutiIsClassBussy) {
            return;
        }
        this.zutiIsClassBussy = true;
        if (this.dying == 0) {
            Point3d point3d = this.pos.getAbsPoint();
            this.zutiBornPlace.place.set(point3d.x, point3d.y);
            if (this.zutiBornPlace.zutiBpStayPoints != null) {
                for (int i = 0; i < this.zutiBornPlace.zutiBpStayPoints.size(); i++) {
                    ZutiStayPoint zutistaypoint = (ZutiStayPoint) this.zutiBornPlace.zutiBpStayPoints.get(i);
                    zutistaypoint.PsVsShipRefresh(point3d.x, point3d.y, this.initOr.getYaw());
                }

            }
        } else if (this.dying > 0) {
            ZutiSupportMethods.removeBornPlace(this.zutiBornPlace);
            this.zutiBornPlace = null;
        }
        this.zutiIsClassBussy = false;
    }

    private void zutiAssignStayPointsToBp() {
        if (this.zutiBornPlace == null) {
            return;
        }
        double d = this.pos.getAbsPoint().x;
        double d1 = this.pos.getAbsPoint().y;
        this.zutiBornPlace.zutiBpStayPoints = new ArrayList();
        double d2 = 22500D;
        Point_Stay apoint_stay[][] = World.cur().airdrome.stay;
        ArrayList arraylist = new ArrayList();
        for (int j = 0; j < apoint_stay.length; j++) {
            if (apoint_stay[j] == null) {
                continue;
            }
            Point_Stay point_stay = apoint_stay[j][apoint_stay[j].length - 1];
            double d3 = ((point_stay.x - d) * (point_stay.x - d)) + ((point_stay.y - d1) * (point_stay.y - d1));
            if (d3 <= d2) {
                arraylist.add(point_stay);
            }
        }

        int i = arraylist.size();
        if (i < 0) {
            return;
        }
        for (int k = 0; k < i;) {
            ZutiStayPoint zutistaypoint = new ZutiStayPoint();
            zutistaypoint.pointStay = (Point_Stay) arraylist.get(k);
            if (this.zutiBornPlace == null) {
                return;
            }
            try {
                this.zutiBornPlace.zutiBpStayPoints.add(zutistaypoint);
                continue;
            } catch (Exception exception) {
                System.out.println("BigshipGeneric zutiAssignStayPointsToBp error: " + exception.toString());
                exception.printStackTrace();
                k++;
                k++;
            }
        }

    }

    public void zutiAssignBornPlace(boolean flag) {
        double d = this.pos.getAbsPoint().x;
        double d1 = this.pos.getAbsPoint().y;
        double d2 = 1000000D;
        BornPlace bornplace = null;
        ArrayList arraylist = World.cur().bornPlaces;
        if (arraylist == null) {
            return;
        }
        for (int i = 0; i < arraylist.size(); i++) {
            BornPlace bornplace1 = (BornPlace) arraylist.get(i);
            if (bornplace1.zutiAlreadyAssigned) {
                continue;
            }
            double d3 = Math.sqrt(Math.pow(bornplace1.place.x - d, 2D) + Math.pow(bornplace1.place.y - d1, 2D));
            if ((d3 < d2) && (bornplace1.army == this.getArmy())) {
                d2 = d3;
                bornplace = bornplace1;
            }
        }

        if (d2 < 1000D) {
            this.zutiBornPlace = bornplace;
            bornplace.zutiAlreadyAssigned = true;
            this.airport.setCustomStayPoints();
            if (!flag) {
                this.zutiAssignStayPointsToBp();
            }
        }
    }

    public int zutiGetDying() {
        return this.dying;
    }

    public boolean zutiIsStatic() {
        return (this.path == null) || (this.path.size() <= 0);
    }

    public void showTransparentRunwayRed() {
        this.hierMesh().chunkVisible("Red", true);
    }

    public void hideTransparentRunwayRed() {
        this.hierMesh().chunkVisible("Red", false);
    }

    private float filter(float f, float f1, float f2, float f3, float f4) {
        float f5 = (float) Math.exp(-f / f3);
        float f6 = f1 + ((f2 - f1) * f5);
        if (f6 < f1) {
            f6 += f4 * f;
            if (f6 > f1) {
                f6 = f1;
            }
        } else if (f6 > f1) {
            f6 -= f4 * f;
            if (f6 < f1) {
                f6 = f1;
            }
        }
        return f6;
    }

    public void setBlastDeflector(int i, int j) {
        if (!this.bHasBlastDeflectorControl) {
            return;
        }
        if (this.BlastDeflectorControl[i] == j) {
            return;
        }
        this.doSetBlastDeflector(i, j);
        if (bLogDetailBD) {
            System.out.println("BigShip: 5223 - setBlastDeflector(int num=" + Integer.toString(i) + ", int i =" + Integer.toString(j) + ")");
        }
    }

    private void doSetBlastDeflector(int i, int j) {
        this.BlastDeflectorControl[i] = j;
    }

    public float getBlastDeflector(int i) {
        return this.blastDeflector[i];
    }

    public void forceBlastDeflector(int i, float f) {
        this.blastDeflector[i] = f;
    }

    public void moveBlastDeflector(int i, float f) {
    }

    public float getVlaGlidePath(Aircraft aircraft) {
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        HierMesh hiermesh = this.hierMesh();
        this.findHook(hiermesh, "_RWY_LDG", loc);
        loc1.set(loc);
        loc1.add(this.initLoc);
        double d = loc1.getZ() + 3D;
        double d1 = Math.abs(aircraft.pos.getAbsPoint().x - loc1.getX());
        double d2 = Math.abs(aircraft.pos.getAbsPoint().y - loc1.getY());
        float f = 10F;
        this.distanceVla = (float) Math.sqrt((d1 * d1) + (d2 * d2));
        double d3 = this.distanceVla - f;
        double d4 = aircraft.pos.getAbsPoint().z - d;
        if (d3 < d4) {
            return 90F;
        } else {
            double d5 = Math.asin(d4 / d3);
            double d6 = glideScopeInRads - d5;
            float f1 = (float) Math.toDegrees(d6);
            return f1;
        }
    }

    public float getVlaAzimuthBP(Aircraft aircraft) {
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        HierMesh hiermesh = this.hierMesh();
        this.findHook(hiermesh, "_RWY_LDG", loc);
        loc1.set(loc);
        loc1.add(this.initLoc);
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        aircraft.pos.getAbs(point3d1);
        point3d = loc1.getPoint();
        point3d.sub(point3d1);
        double d = Math.sqrt((point3d.x * point3d.x) + (point3d.y * point3d.y));
//        double d1 = (double)aircraft.FM.getAltitude() - loc1.getZ() - 2D;
//        float f = getConeOfSilence(d, d1);
        if (d > 35000D) {
            return 0.0F;
        }
//        float f1 = f;
//        f1 *= Aircraft.cvt((float)d, 0.0F, 35000F, 1.0F, 0.0F);
        float f2 = 57.32484F * (float) Math.atan2(point3d.x, point3d.y);
        f2 -= loc1.getAzimut();
        for (f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F) {
            ;
        }
        for (; f2 >= 360F; f2 -= 360F) {
            ;
        }
        float f3 = f2 - 90F;
//        boolean flag = true;
//        boolean flag1 = false;
        if (f3 > 90F) {
            f3 = -(f3 - 180F);
//            flag = false;
        }
//        float f4 = 0.0F;
//        if(!flag)
//            f4 = f3 * -18F;
//        else
//            f4 = f3 * 18F;
        return f3;
    }

    public float getVlaAzimuthPB(Aircraft aircraft) {
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        HierMesh hiermesh = this.hierMesh();
        this.findHook(hiermesh, "_RWY_LDG", loc);
        loc1.set(loc);
        loc1.add(this.initLoc);
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        aircraft.pos.getAbs(point3d1);
        point3d = loc1.getPoint();
        point3d.sub(point3d1);
        double d = Math.sqrt((point3d.x * point3d.x) + (point3d.y * point3d.y));
        double d1 = aircraft.FM.getAltitude() - loc1.getZ() - 2D;
        float f = this.getConeOfSilence(d, d1);
        if (d > 35000D) {
            return 0.0F;
        }
//        float f1 = f;
//        f1 *= Aircraft.cvt((float)d, 0.0F, 35000F, 1.0F, 0.0F);
        float f2 = 57.32484F * (float) Math.atan2(point3d.x, point3d.y);
        f2 -= loc1.getAzimut();
        for (f2 = (f2 + 180F) % 360F; f2 < 0.0F; f2 += 360F) {
            ;
        }
        for (; f2 >= 360F; f2 -= 360F) {
            ;
        }
        float f3 = f2 - 90F;
        boolean flag = true;
//        boolean flag1 = false;
        if (f3 > 90F) {
            f3 = -(f3 - 180F);
            flag = false;
        }
        float f4 = 0.0F;
        if (!flag) {
            f4 = f3 * -18F;
        } else {
            f4 = f3 * 18F;
        }
        return f4 * f;
    }

    private float getConeOfSilence(double d, double d1) {
        float f = 57.32484F * (float) Math.atan2(d, d1);
        return Aircraft.cvt(f, 20F, 40F, 0.0F, 1.0F);
    }

    public Aircraft getNearestAircraftFront() {
        List list = Engine.targets();
        int i = list.size();
        double d = 12001D;
        int j = -1;
        HierMesh hiermesh = this.hierMesh();
        Loc loc = new Loc();
        Loc loc1 = new Loc();
        this.findHook(hiermesh, "_RWY_LDG", loc);
        loc1.set(loc);
        loc1.add(this.initLoc);
        for (int k = 0; k < i; k++) {
            Actor actor = (Actor) list.get(k);
            if (!(actor instanceof Aircraft)) {
                continue;
            }
            Aircraft aircraft = (Aircraft) actor;
            Point3d point3d = aircraft.pos.getAbsPoint();
            Point3d point3d1 = loc1.getPoint();
            double d1 = Math.sqrt(((point3d1.x - point3d.x) * (point3d1.x - point3d.x)) + ((point3d1.y - point3d.y) * (point3d1.y - point3d.y)));
            if ((d1 >= 12000D) || (d1 >= d) || aircraft.FM.Gears.onGround() || aircraft.FM.isCrashedOnGround()) {
                continue;
            }
            if ((this.getVlaGlidePath(aircraft) > 89.8F) || (Math.abs(this.getVlaAzimuthBP(aircraft)) > 40F) || (Math.abs(this.getVlaAzimuthPB(aircraft)) > 90F)) {
                this.insideOfGlidepath = false;
            } else {
                this.insideOfGlidepath = true;
            }
            d = d1;
            j = k;
        }

        if (j == -1) {
            this.distanceVla = 1000F;
            return null;
        } else {
            this.distanceVla = (float) d;
            return (Aircraft) list.get(j);
        }
    }

    private void InitMirrorLandingAid() {
        this.bInitDoneMirrorLA = true;
        this.Meatballxyz[0] = this.Meatballxyz[1] = this.Meatballxyz[2] = 0.0F;
        this.Meatballypr[0] = this.Meatballypr[1] = this.Meatballypr[2] = 0.0F;
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            this.MlaDatumLightOn();
            this.MlaMeatballOn();
            this.MlaMeatballCenter();
            return;
        }
    }

    private void InitUSIflols() {
        this.bInitDoneUSIflols = true;
        this.Meatballxyz[0] = this.Meatballxyz[1] = this.Meatballxyz[2] = 0.0F;
        this.Meatballypr[0] = this.Meatballypr[1] = this.Meatballypr[2] = 0.0F;
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            this.MlaDatumLightOn();
            this.USIflolsMeatballOn();
            this.USIflolsMeatballCenter();
            return;
        }
    }

    private void InitFRFlols() {
        this.bInitDoneFRFlols = true;
        this.Meatballxyz[0] = this.Meatballxyz[1] = this.Meatballxyz[2] = 0.0F;
        this.Meatballypr[0] = this.Meatballypr[1] = this.Meatballypr[2] = 0.0F;
        if (!Config.isUSE_RENDER()) {
            return;
        } else {
            this.FRFlolsDatumLightOn();
            this.USIflolsMeatballOn();
            this.USIflolsMeatballCenter();
            return;
        }
    }

    /* private */ void MlaDatumLightOff() {
        for (int i = 0; i < 12; i++) {
            Eff3DActor.finish(this.effdatum[i]);
        }

    }

    private void MlaDatumLightOn() {
        this.effdatum[0] = Eff3DActor.New(this, this.findHook("_datum01"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[1] = Eff3DActor.New(this, this.findHook("_datum02"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[2] = Eff3DActor.New(this, this.findHook("_datum03"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[3] = Eff3DActor.New(this, this.findHook("_datum04"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[4] = Eff3DActor.New(this, this.findHook("_datum05"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[5] = Eff3DActor.New(this, this.findHook("_datum06"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[6] = Eff3DActor.New(this, this.findHook("_datum07"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[7] = Eff3DActor.New(this, this.findHook("_datum08"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[8] = Eff3DActor.New(this, this.findHook("_datum09"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[9] = Eff3DActor.New(this, this.findHook("_datum10"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[10] = Eff3DActor.New(this, this.findHook("_datum11"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[11] = Eff3DActor.New(this, this.findHook("_datum12"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
    }

    private void MlaCutLightOff() {
        if (this.cutlighton) {
            for (int i = 0; i < 4; i++) {
                Eff3DActor.finish(this.effcut[i]);
            }

        }
    }

    private void MlaCutLightOn() {
        this.effcut[0] = Eff3DActor.New(this, this.findHook("_cut01"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effcut[1] = Eff3DActor.New(this, this.findHook("_cut02"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effcut[2] = Eff3DActor.New(this, this.findHook("_cut03"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effcut[3] = Eff3DActor.New(this, this.findHook("_cut04"), null, 0.7F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
    }

    private void MlaWaveoffOff() {
        if (this.waveofflighton) {
            for (int i = 0; i < 4; i++) {
                Eff3DActor.finish(this.effwaveoff[i]);
            }

        }
    }

    private void MlaWaveoffOn() {
        this.effwaveoff[0] = Eff3DActor.New(this, this.findHook("_waveoffL01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effwaveoff[1] = Eff3DActor.New(this, this.findHook("_waveoffL02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effwaveoff[2] = Eff3DActor.New(this, this.findHook("_waveoffR01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effwaveoff[3] = Eff3DActor.New(this, this.findHook("_waveoffR02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    /* private */ void MlaEmergencyWaveoffOff() {
        for (int i = 0; i < 2; i++) {
            Eff3DActor.finish(this.effemgwaveoff[i]);
        }

    }

    /* private */ void MlaEmergencyWaveoffOn() {
        this.effemgwaveoff[0] = Eff3DActor.New(this, this.findHook("_emgwaveoffL"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effemgwaveoff[1] = Eff3DActor.New(this, this.findHook("_emgwaveoffR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    /* private */ void MlaReserve1Off() {
        for (int i = 0; i < 2; i++) {
            Eff3DActor.finish(this.effreserve1[i]);
        }

    }

    /* private */ void MlaReserve1On() {
        this.effreserve1[0] = Eff3DActor.New(this, this.findHook("_reserveL01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effreserve1[1] = Eff3DActor.New(this, this.findHook("_reserveR01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    /* private */ void MlaReserve2Off() {
        for (int i = 0; i < 2; i++) {
            Eff3DActor.finish(this.effreserve2[i]);
        }

    }

    /* private */ void MlaReserve2On() {
        this.effreserve2[0] = Eff3DActor.New(this, this.findHook("_reserveL02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effreserve2[1] = Eff3DActor.New(this, this.findHook("_reserveR02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    /* private */ void FRFlolsDatumLightOff() {
        for (int i = 0; i < 14; i++) {
            Eff3DActor.finish(this.effdatum[i]);
        }

    }

    private void FRFlolsDatumLightOn() {
        this.effdatum[0] = Eff3DActor.New(this, this.findHook("_datum01"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[1] = Eff3DActor.New(this, this.findHook("_datum02"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[2] = Eff3DActor.New(this, this.findHook("_datum03"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[3] = Eff3DActor.New(this, this.findHook("_datum04"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[4] = Eff3DActor.New(this, this.findHook("_datum05"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[5] = Eff3DActor.New(this, this.findHook("_datum06"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[6] = Eff3DActor.New(this, this.findHook("_datum07"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[7] = Eff3DActor.New(this, this.findHook("_datum08"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[8] = Eff3DActor.New(this, this.findHook("_datum09"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[9] = Eff3DActor.New(this, this.findHook("_datum10"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[10] = Eff3DActor.New(this, this.findHook("_datum11"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[11] = Eff3DActor.New(this, this.findHook("_datum12"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[12] = Eff3DActor.New(this, this.findHook("_datum13"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
        this.effdatum[13] = Eff3DActor.New(this, this.findHook("_datum14"), null, 0.4F, "3DO/Effects/Fireworks/FlareGreen.eff", -1F);
    }

    private void FRFlolsCutLightOff() {
        if (this.cutlighton) {
            for (int i = 0; i < 2; i++) {
                Eff3DActor.finish(this.effcut[i]);
            }

        }
    }

    private void FRFlolsCutLightOn() {
        this.effcut[0] = Eff3DActor.New(this, this.findHook("_cut01"), null, 0.7F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effcut[1] = Eff3DActor.New(this, this.findHook("_cut02"), null, 0.7F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    private void FRFlolsWaveoffOff() {
        if (this.waveofflighton) {
            for (int i = 0; i < 4; i++) {
                Eff3DActor.finish(this.effwaveoff[i]);
            }

        }
    }

    private void FRFlolsWaveoffOn() {
        this.effwaveoff[0] = Eff3DActor.New(this, this.findHook("_waveoffL01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effwaveoff[1] = Eff3DActor.New(this, this.findHook("_waveoffL02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effwaveoff[2] = Eff3DActor.New(this, this.findHook("_waveoffR01"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effwaveoff[3] = Eff3DActor.New(this, this.findHook("_waveoffR02"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    /* private */ void FRFlolsEmergencyWaveoffOff() {
        if (this.waveofflighton) {
            for (int i = 0; i < 2; i++) {
                Eff3DActor.finish(this.effemgwaveoff[i]);
            }

        }
    }

    /* private */ void FRFlolsEmergencyWaveoffOn() {
        this.effemgwaveoff[0] = Eff3DActor.New(this, this.findHook("_emgwaveoffL"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
        this.effemgwaveoff[1] = Eff3DActor.New(this, this.findHook("_emgwaveoffR"), null, 1.0F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
    }

    private void MlaMeatballOff() {
        this.hierMesh().chunkVisible("Meatball", false);
        Eff3DActor.finish(this.effmeatball[0]);
    }

    private void MlaMeatballOn() {
        this.hierMesh().chunkVisible("Meatball", true);
        this.effmeatball[0] = Eff3DActor.New(this, this.findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
    }

    private void MlaMeatballCenter() {
        this.Meatballxyz[0] = this.Meatballxyz[1] = this.Meatballxyz[2] = 0.0F;
        this.hierMesh().chunkSetLocate("Meatball", this.Meatballxyz, this.Meatballypr);
        Eff3DActor.finish(this.effmeatball[0]);
        this.effmeatball[0] = Eff3DActor.New(this, this.findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
    }

    private void MlaMeatballSet(float f, float f1) {
        this.Meatballxyz[0] = 0.0F;
        this.Meatballxyz[1] = f * -0.43F;
        this.Meatballxyz[2] = f1 * -0.43F;
        this.hierMesh().chunkSetLocate("Meatball", this.Meatballxyz, this.Meatballypr);
        if ((Time.current() % 100F) == 0.0F) {
            Eff3DActor.finish(this.effmeatball[0]);
            this.effmeatball[0] = Eff3DActor.New(this, this.findHook("_meatball"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
        }
    }

    private void IflolsMeatballOff() {
        this.hierMesh().chunkVisible("meatball_red", false);
        this.hierMesh().chunkVisible("meatball_yellow", false);
        Eff3DActor.finish(this.effmeatball[0]);
        Eff3DActor.finish(this.effmeatball[1]);
    }

    private void USIflolsMeatballOn() {
        switch (this.meatballpos) {
            case 0:
            case 1:
                this.hierMesh().chunkVisible("meatball_red", true);
                this.effmeatball[0] = Eff3DActor.New(this, this.findHook("_meatball_r"), null, 0.8F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                break;

            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                this.hierMesh().chunkVisible("meatball_yellow", true);
                this.effmeatball[1] = Eff3DActor.New(this, this.findHook("_meatball_y"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                break;
        }
    }

    private void FRFlolsMeatballOn() {
        switch (this.meatballpos) {
            case 0:
            case 1:
                this.hierMesh().chunkVisible("meatball_red", true);
                this.effmeatball[0] = Eff3DActor.New(this, this.findHook("_meatball_r"), null, 0.8F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                break;

            case 2:
            case 3:
            case 4:
            case 5:
                this.hierMesh().chunkVisible("meatball_yellow", true);
                this.effmeatball[1] = Eff3DActor.New(this, this.findHook("_meatball_y"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                break;
        }
    }

    private void USIflolsMeatballCenter() {
        this.USIflolsMeatballSet(0.0F);
    }

    private void FRFlolsMeatballCenter() {
        this.FRFlolsMeatballSet(0.0F);
    }

    private void USIflolsMeatballSet(float f) {
        int i = (int) Math.floor(Aircraft.cvt(-f, -1F, 1.0F, 0.0F, 11F));
        if (i == this.meatballpos) {
            return;
        }
        this.Meatballxyz[0] = 0.0F;
        this.Meatballxyz[1] = 0.0F;
        this.Meatballxyz[2] = i * 0.146624F;
        this.hierMesh().chunkSetLocate("meatball_red", this.Meatballxyz, this.Meatballypr);
        this.hierMesh().chunkSetLocate("meatball_yellow", this.Meatballxyz, this.Meatballypr);
        switch (i) {
            default:
                break;

            case 0:
            case 1:
                if (this.meatballpos > 1) {
                    Eff3DActor.finish(this.effmeatball[1]);
                    this.hierMesh().chunkVisible("meatball_yellow", false);
                    this.hierMesh().chunkVisible("meatball_red", true);
                } else {
                    Eff3DActor.finish(this.effmeatball[0]);
                }
                this.effmeatball[0] = Eff3DActor.New(this, this.findHook("_meatball_r"), null, 0.8F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                break;

            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                if (this.meatballpos > 1) {
                    Eff3DActor.finish(this.effmeatball[1]);
                } else {
                    Eff3DActor.finish(this.effmeatball[0]);
                    this.hierMesh().chunkVisible("meatball_yellow", true);
                    this.hierMesh().chunkVisible("meatball_red", false);
                }
                this.effmeatball[1] = Eff3DActor.New(this, this.findHook("_meatball_y"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                break;
        }
        this.meatballpos = i;
    }

    private void FRFlolsMeatballSet(float f) {
        int i = (int) Math.floor(Aircraft.cvt(-f, -1F, 1.0F, 0.0F, 5F));
        if (i == this.meatballpos) {
            return;
        }
        this.Meatballxyz[0] = 0.0F;
        this.Meatballxyz[1] = 0.0F;
        this.Meatballxyz[2] = i * 0.4F;
        this.hierMesh().chunkSetLocate("meatball_red", this.Meatballxyz, this.Meatballypr);
        this.hierMesh().chunkSetLocate("meatball_yellow", this.Meatballxyz, this.Meatballypr);
        switch (i) {
            default:
                break;

            case 0:
            case 1:
                if (this.meatballpos > 1) {
                    Eff3DActor.finish(this.effmeatball[1]);
                    this.hierMesh().chunkVisible("meatball_yellow", false);
                    this.hierMesh().chunkVisible("meatball_red", true);
                } else {
                    Eff3DActor.finish(this.effmeatball[0]);
                }
                this.effmeatball[0] = Eff3DActor.New(this, this.findHook("_meatball_r"), null, 0.8F, "3DO/Effects/Fireworks/FlareRed.eff", -1F);
                break;

            case 2:
            case 3:
            case 4:
            case 5:
                if (this.meatballpos > 1) {
                    Eff3DActor.finish(this.effmeatball[1]);
                } else {
                    Eff3DActor.finish(this.effmeatball[0]);
                    this.hierMesh().chunkVisible("meatball_yellow", true);
                    this.hierMesh().chunkVisible("meatball_red", false);
                }
                this.effmeatball[1] = Eff3DActor.New(this, this.findHook("_meatball_y"), null, 0.8F, "3DO/Effects/Fireworks/FlareAmber.eff", -1F);
                break;
        }
        this.meatballpos = i;
    }

    public void MlaSetVisible(boolean flag) {
        if (flag) {
            this.MlaMeatballOn();
            for (int i = 0; i < 12; i++) {
                this.effdatum[i]._setIntesity(0.4F);
            }

            for (int j = 0; j < 4; j++) {
                this.effcut[j]._setIntesity(1.0F);
            }

            for (int k = 0; k < 4; k++) {
                this.effwaveoff[k]._setIntesity(1.0F);
            }

            for (int l = 0; l < 2; l++) {
                this.effemgwaveoff[l]._setIntesity(1.0F);
            }

        } else {
            this.MlaMeatballOff();
            for (int i1 = 0; i1 < 12; i1++) {
                this.effdatum[i1]._setIntesity(0.0F);
            }

            if (this.cutlighton) {
                for (int j1 = 0; j1 < 4; j1++) {
                    this.effcut[j1]._setIntesity(0.0F);
                }

            }
            this.cutlighton = false;
            if (this.waveofflighton) {
                for (int k1 = 0; k1 < 4; k1++) {
                    this.effwaveoff[k1]._setIntesity(0.0F);
                }

            }
            this.waveofflighton = false;
            if (this.emgwaveofflighton) {
                for (int l1 = 0; l1 < 2; l1++) {
                    this.effemgwaveoff[l1]._setIntesity(0.0F);
                }

            }
            this.emgwaveofflighton = false;
        }
    }

    public void USIflolsSetVisible(boolean flag) {
        if (flag) {
            this.USIflolsMeatballOn();
            for (int i = 0; i < 12; i++) {
                this.effdatum[i]._setIntesity(0.4F);
            }

            for (int j = 0; j < 4; j++) {
                this.effcut[j]._setIntesity(1.0F);
            }

            for (int k = 0; k < 4; k++) {
                this.effwaveoff[k]._setIntesity(1.0F);
            }

            for (int l = 0; l < 2; l++) {
                this.effemgwaveoff[l]._setIntesity(1.0F);
            }

        } else {
            this.IflolsMeatballOff();
            for (int i1 = 0; i1 < 12; i1++) {
                this.effdatum[i1]._setIntesity(0.0F);
            }

            if (this.cutlighton) {
                for (int j1 = 0; j1 < 4; j1++) {
                    this.effcut[j1]._setIntesity(0.0F);
                }

            }
            this.cutlighton = false;
            if (this.waveofflighton) {
                for (int k1 = 0; k1 < 4; k1++) {
                    this.effwaveoff[k1]._setIntesity(0.0F);
                }

            }
            this.waveofflighton = false;
            if (this.emgwaveofflighton) {
                for (int l1 = 0; l1 < 2; l1++) {
                    this.effemgwaveoff[l1]._setIntesity(0.0F);
                }

            }
            this.emgwaveofflighton = false;
        }
    }

    public void FRFlolsSetVisible(boolean flag) {
        if (flag) {
            this.FRFlolsMeatballOn();
            for (int i = 0; i < 14; i++) {
                this.effdatum[i]._setIntesity(0.4F);
            }

            for (int j = 0; j < 2; j++) {
                this.effcut[j]._setIntesity(1.0F);
            }

            for (int k = 0; k < 4; k++) {
                this.effwaveoff[k]._setIntesity(1.0F);
            }

            for (int l = 0; l < 2; l++) {
                this.effemgwaveoff[l]._setIntesity(1.0F);
            }

        } else {
            this.IflolsMeatballOff();
            for (int i1 = 0; i1 < 14; i1++) {
                this.effdatum[i1]._setIntesity(0.0F);
            }

            if (this.cutlighton) {
                for (int j1 = 0; j1 < 2; j1++) {
                    this.effcut[j1]._setIntesity(0.0F);
                }

            }
            this.cutlighton = false;
            if (this.waveofflighton) {
                for (int k1 = 0; k1 < 4; k1++) {
                    this.effwaveoff[k1]._setIntesity(0.0F);
                }

            }
            this.waveofflighton = false;
            if (this.emgwaveofflighton) {
                for (int l1 = 0; l1 < 2; l1++) {
                    this.effemgwaveoff[l1]._setIntesity(0.0F);
                }

            }
            this.emgwaveofflighton = false;
        }
    }

    private boolean MlaUpdate() {
        Aircraft aircraft = this.getNearestAircraftFront();
        if ((aircraft == null) || (!this.insideOfGlidepath && (this.distanceVla > 100F))) {
            if (this.meatballmoving) {
                this.MlaMeatballCenter();
                this.MlaCutLightOff();
                this.cutlighton = false;
                this.MlaWaveoffOff();
                this.waveofflighton = false;
            }
            this.meatballmoving = false;
            return true;
        }
        float f = this.getVlaGlidePath(aircraft);
        float f1 = Aircraft.cvt(f, -0.8F, 0.8F, -1F, 1.0F);
        float f2 = this.getVlaAzimuthPB(aircraft);
        float f3 = Aircraft.cvt(f2, -92F, 92F, -1F, 1.0F);
        this.MlaMeatballSet(f3, f1);
        if (f > 0.7F) {
            if (!this.cutlighton) {
                this.MlaCutLightOn();
                this.cutlighton = true;
            }
        } else if (this.cutlighton) {
            this.MlaCutLightOff();
            this.cutlighton = false;
        }
        if ((this.distanceVla < 100F) && ((f > 0.7F) || (f < 0.7F) || (f2 < -80F) || (f2 > 80F))) {
            if (!this.waveofflighton) {
                this.MlaWaveoffOn();
                this.waveofflighton = true;
            }
        } else if (this.waveofflighton) {
            this.MlaWaveoffOff();
            this.waveofflighton = false;
        }
        this.meatballmoving = true;
        return true;
    }

    private boolean USIflolsUpdate() {
        Aircraft aircraft = this.getNearestAircraftFront();
        if ((aircraft == null) || (!this.insideOfGlidepath && (this.distanceVla > 100F))) {
            if (this.meatballmoving) {
                this.USIflolsMeatballCenter();
                this.MlaCutLightOff();
                this.cutlighton = false;
                this.MlaWaveoffOff();
                this.waveofflighton = false;
            }
            this.meatballmoving = false;
            return true;
        }
        float f = this.getVlaGlidePath(aircraft);
        float f1 = Aircraft.cvt(f, -0.8F, 0.8F, -1F, 1.0F);
        float f2 = this.getVlaAzimuthPB(aircraft);
        this.USIflolsMeatballSet(f1);
        if (f > 0.7F) {
            if (!this.cutlighton) {
                this.MlaCutLightOn();
                this.cutlighton = true;
            }
        } else if (this.cutlighton) {
            this.MlaCutLightOff();
            this.cutlighton = false;
        }
        if ((this.distanceVla < 100F) && ((f > 0.7F) || (f < 0.7F) || (f2 < -80F) || (f2 > 80F))) {
            if (!this.waveofflighton) {
                this.MlaWaveoffOn();
                this.waveofflighton = true;
            }
        } else if (this.waveofflighton) {
            this.MlaWaveoffOff();
            this.waveofflighton = false;
        }
        this.meatballmoving = true;
        return true;
    }

    private boolean FRFlolsUpdate() {
        Aircraft aircraft = this.getNearestAircraftFront();
        if ((aircraft == null) || (!this.insideOfGlidepath && (this.distanceVla > 100F))) {
            if (this.meatballmoving) {
                this.FRFlolsMeatballCenter();
                this.FRFlolsCutLightOff();
                this.cutlighton = false;
                this.FRFlolsWaveoffOff();
                this.waveofflighton = false;
            }
            this.meatballmoving = false;
            return true;
        }
        float f = this.getVlaGlidePath(aircraft);
        float f1 = Aircraft.cvt(f, -0.8F, 0.8F, -1F, 1.0F);
        float f2 = this.getVlaAzimuthPB(aircraft);
        this.FRFlolsMeatballSet(f1);
        if (f > 0.7F) {
            if (!this.cutlighton) {
                this.FRFlolsCutLightOn();
                this.cutlighton = true;
            }
        } else if (this.cutlighton) {
            this.FRFlolsCutLightOff();
            this.cutlighton = false;
        }
        if ((this.distanceVla < 100F) && ((f > 0.7F) || (f < 0.7F) || (f2 < -80F) || (f2 > 80F))) {
            if (!this.waveofflighton) {
                this.FRFlolsWaveoffOn();
                this.waveofflighton = true;
            }
        } else if (this.waveofflighton) {
            this.FRFlolsWaveoffOff();
            this.waveofflighton = false;
        }
        this.meatballmoving = true;
        return true;
    }

    /* private */ static final int    MAX_PARTS                              = 255;
    /* private */ static final int    MAX_GUNS                               = 255;
    /* private */ static final int    MAX_USER_ADDITIONAL_COLLISION_CHUNKS   = 4;
    public float                      CURRSPEED;
    public boolean                    isTurning;
    public boolean                    isTurningBackward;
    public boolean                    mustRecomputePath;
    public boolean                    mustSendSpeedToNet;
    /* private */ final int           REQUEST_LOC                            = 93;
    int                               hasTracking;
    private ShipProperties            prop;
    /* private */ static final int    NETSEND_MIN_DELAY_MS_PARTSSTATE        = 650;
    /* private */ static final int    NETSEND_MAX_DELAY_MS_PARTSSTATE        = 1100;
    private long                      netsendPartsState_lasttimeMS;
    private boolean                   netsendPartsState_needtosend;
    private float                     netsendDrown_pitch;
    private float                     netsendDrown_roll;
    private float                     netsendDrown_depth;
    private float                     netsendDrown_timeS;
    private int                       netsendDrown_nparts;
    /* private */ static final int    NETSEND_MIN_DELAY_MS_FIRE              = 40;
    /* private */ static final int    NETSEND_MAX_DELAY_MS_FIRE              = 85;
    /* private */ static final long   NETSEND_MIN_BYTECODEDDELTATIME_MS_FIRE = -2000L;
    /* private */ static final long   NETSEND_MAX_BYTECODEDDELTATIME_MS_FIRE = 5000L;
    /* private */ static final int    NETSEND_ABSLIMIT_NUMITEMS_FIRE         = 31;
    /* private */ static final int    NETSEND_MAX_NUMITEMS_FIRE              = 15;
    private long                      netsendFire_lasttimeMS;
    private int                       netsendFire_armindex;
    private static TmpTrackOrFireInfo netsendFire_tmpbuff[];
    private FiringDevice              arms[];
    private RadarDevice               radars[];
    /* private */ static final int    STPART_LIVE                            = 0;
    /* private */ static final int    STPART_BLACK                           = 1;
    /* private */ static final int    STPART_DEAD                            = 2;
    private Part                      parts[];
    private int                       shotpoints[];
    int                               numshotpoints;
    /* private */ static final int    NETSEND_MIN_DELAY_MS_DMG               = 65;
    /* private */ static final int    NETSEND_MAX_DELAY_MS_DMG               = 115;
    /* private */ static final int    NETSEND_ABSLIMIT_NUMITEMS_DMG          = 256;
    /* private */ static final int    NETSEND_MAX_NUMITEMS_DMG               = 14;
    private long                      netsendDmg_lasttimeMS;
    private int                       netsendDmg_partindex;
    private ArrayList                 path;
    private int                       cachedSeg;
    private float                     bodyDepth;
    private float                     bodyPitch;
    private float                     bodyRoll;
    private float                     shipYaw;
    private long                      tmInterpoStart;
    private long                      tmInterpoEnd;
    private float                     bodyDepth0;
    private float                     bodyPitch0;
    private float                     bodyRoll0;
    private float                     bodyDepth1;
    private float                     bodyPitch1;
    private float                     bodyRoll1;
    private boolean                   bSailorsDisappear;
    private long                      timeOfSailorsDisappear;
    private long                      timeOfDeath;
    private long                      sink2timeWhenStop;
    private float                     sink2Depth;
    private float                     sink2Pitch;
    private float                     sink2Roll;
    public int                        dying;
    static final int                  DYING_NONE                             = 0;
    static final int                  DYING_SINK1                            = 1;
    static final int                  DYING_SINK2                            = 2;
    static final int                  DYING_DEAD                             = 3;
    private long                      respawnDelay;
    private long                      wakeupTmr;
    private long                      radarTmr;
    public float                      DELAY_WAKEUP;
    public int                        SKILL_IDX;
    public float                      SLOWFIRE_K;
    private Pipe                      pipes[];
    private Pipe                      dsmoks[];
    private Eff3DActor                wake[]                                 = { null, null, null };
    private Eff3DActor                noseW;
    private Eff3DActor                nose;
    private Eff3DActor                tail;
    private static ShipProperties     constr_arg1                            = null;
    private static ActorSpawnArg      constr_arg2                            = null;
    private Point3d                   p;
    private Point3d                   p1;
    private Point3d                   p2;
    private Orient                    o;
//    private Vector3f tmpvf;
    private Vector3d                  tmpvd;
    private float                     tmpYPR[];
//    private float tmpf6[];
    private Loc                       tmpL;
//    private byte tmpBitsState[];
    private float                     rollAmp;
    private int                       rollPeriod;
    private double                    rollWAmp;
    private float                     pitchAmp;
    private int                       pitchPeriod;
    private double                    pitchWAmp;
    private Vector3d                  W;
    private Vector3d                  N;
    private Vector3d                  tmpV;
    public Orient                     initOr;
    public Loc                        initLoc;
    private AirportCarrier            airport;
    private CellAirField              cellTO;
    private CellAirField              cellLDG;
    public Aircraft                   towAircraft;
    public int                        towPortNum;
    public HookNamed                  towHook;
    private Vector3d                  tmpDir;
    public static String              ZUTI_RADAR_SHIPS[]                     = { "CV", "Marat", "Kirov", "BB", "Niobe", "Illmarinen", "Vainamoinen", "Tirpitz", "Aurora", "Carrier0", "Carrier1" };
    public static String              ZUTI_RADAR_SHIPS_SMALL[]               = { "Destroyer", "DD", "USSMcKean", "Italia0", "Italia1" };
    public BornPlace                  zutiBornPlace;
    private boolean                   zutiIsClassBussy;
    private static boolean            bLogDetail                             = false;
    private static boolean            bLogDetailBD                           = false;
    private float                     BlastDeflectorControl[];
    public boolean                    bHasBlastDeflectorControl;
    private float                     blastDeflector[];
    private float                     blastDeflector_[];
    private float                     dvBlastDeflector;
    private static final double       glideScopeInRads                       = Math.toRadians(3D);
    private Eff3DActor                effmeatball[];
    private Eff3DActor                effdatum[];
    private Eff3DActor                effcut[];
    private Eff3DActor                effwaveoff[];
    private Eff3DActor                effemgwaveoff[];
    private Eff3DActor                effreserve1[];
    private Eff3DActor                effreserve2[];
//    private Actor actorme;
    private float                     Meatballxyz[];
    private float                     Meatballypr[];
    private boolean                   meatballmoving;
    private float                     distanceVla;
    private int                       meatballpos;
    private boolean                   insideOfGlidepath;
    private boolean                   cutlighton;
    private boolean                   waveofflighton;
    private boolean                   emgwaveofflighton;
//    private boolean bMlaVisible;
    public boolean                    bHasMirrorLA;
    public boolean                    bInitDoneMirrorLA;
    public boolean                    bHasUSIflols;
    public boolean                    bInitDoneUSIflols;
    public boolean                    bHasFRFlols;
    public boolean                    bInitDoneFRFlols;

    static {
        netsendFire_tmpbuff = new TmpTrackOrFireInfo[31];
        for (int i = 0; i < netsendFire_tmpbuff.length; i++) {
            netsendFire_tmpbuff[i] = new TmpTrackOrFireInfo();
        }

    }

}
