package com.maddox.il2.objects.ships;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.Aimer;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.BulletAimer;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.RangeRandom;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.StrengthProperties;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.HunterInterface;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
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
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MsgCollisionListener;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.VisibilityLong;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.ObjectsLogLevel;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.weapons.CannonMidrangeGeneric;
import com.maddox.il2.objects.weapons.Gun;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.Property;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class ShipGeneric extends ActorHMesh implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Predator, ActorAlign, HunterInterface, VisibilityLong {
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
            } else {
                return f2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1) {
            String s2 = sectfile.get(s, s1);
            if ((s2 == null) || (s2.length() <= 0)) {
                // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                    System.out.print("Ship: Value of [" + s + "]:<" + s1 + "> not found");
                    throw new RuntimeException("Can't set property");
                } else if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_SHORT) {
                    System.out.println("Ship \"" + s + "\" is not (correctly) declared in ships.ini file!");
                }
                return null;
                // ---
            } else {
                return new String(s2);
            }
        }

        /* private */ static String getS(SectFile sectfile, String s, String s1, String s2) {
            String s3 = sectfile.get(s, s1);
            if ((s3 == null) || (s3.length() <= 0)) {
                return s2;
            } else {
                return new String(s3);
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
            if (!shipproperties.stre.read("Ship", sectfile, null, s)) {
                throw new RuntimeException("Can't register Ship object");
            }
            int i;
            for (i = 0; sectfile.sectionIndex(s + ":Gun" + i) >= 0; i++) {
                ;
            }
            shipproperties.guns = new ShipGunProperties[i];
            shipproperties.WEAPONS_MASK = 0;
            shipproperties.ATTACK_MAX_DISTANCE = 1.0F;
            for (int j = 0; j < i; j++) {
                shipproperties.guns[j] = new ShipGunProperties();
                ShipGunProperties shipgunproperties = shipproperties.guns[j];
                String s1 = s + ":Gun" + j;
                String s2 = "com.maddox.il2.objects.weapons." + getS(sectfile, s1, "Gun");
                try {
                    shipgunproperties.gunClass = Class.forName(s2);
                } catch (Exception exception) {
                    System.out.println("Ship: Can't find gun class '" + s2 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register Ship object");
                    }
                    return null;
                    // ---
                }
                shipgunproperties.WEAPONS_MASK = GunGeneric.getProperties(shipgunproperties.gunClass).weaponType;
                if (shipgunproperties.WEAPONS_MASK == 0) {
                    System.out.println("Ship: Undefined weapon type in gun class '" + s2 + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register Ship object");
                    }
                    return null;
                    // ---
                }
                shipproperties.WEAPONS_MASK |= shipgunproperties.WEAPONS_MASK;
                shipgunproperties.ATTACK_MAX_DISTANCE = getF(sectfile, s1, "AttackMaxDistance", 6F, 50000F);
                shipgunproperties.ATTACK_MAX_RADIUS = getF(sectfile, s1, "AttackMaxRadius", 6F, 50000F);
                shipgunproperties.ATTACK_MAX_HEIGHT = getF(sectfile, s1, "AttackMaxHeight", 6F, 15000F);
                if (shipgunproperties.ATTACK_MAX_DISTANCE > shipproperties.ATTACK_MAX_DISTANCE) {
                    shipproperties.ATTACK_MAX_DISTANCE = shipgunproperties.ATTACK_MAX_DISTANCE;
                }
                shipgunproperties.TRACKING_ONLY = false;
                if (sectfile.exist(s1, "TrackingOnly")) {
                    shipgunproperties.TRACKING_ONLY = true;
                }
                shipgunproperties.ATTACK_FAST_TARGETS = 1;
                if (sectfile.exist(s1, "FireFastTargets")) {
                    float f = getF(sectfile, s1, "FireFastTargets", 0.0F, 2.0F);
                    shipgunproperties.ATTACK_FAST_TARGETS = (int) (f + 0.5F);
                    if (shipgunproperties.ATTACK_FAST_TARGETS > 2) {
                        shipgunproperties.ATTACK_FAST_TARGETS = 2;
                    }
                }
                shipgunproperties.FAST_TARGETS_ANGLE_ERROR = 0.0F;
                if (sectfile.exist(s1, "FastTargetsAngleError")) {
                    float f1 = getF(sectfile, s1, "FastTargetsAngleError", 0.0F, 45F);
                    shipgunproperties.FAST_TARGETS_ANGLE_ERROR = f1;
                }
                float f2 = getF(sectfile, s1, "HeadMinYaw", -360F, 360F);
                float f3 = getF(sectfile, s1, "HeadStdYaw", -360F, 360F);
                float f4 = getF(sectfile, s1, "HeadMaxYaw", -360F, 360F);
                if (f2 > f4) {
                    System.out.println("Ship: Wrong yaw angles in gun #" + j + " of '" + s + "'");
                    // TODO: +++ Modified by SAS~Storebror to avoid excessive logfile output in BAT
                    if (ObjectsLogLevel.getObjectsLogLevel() == ObjectsLogLevel.OBJECTS_LOGLEVEL_FULL) {
                        throw new RuntimeException("Can't register Ship object");
                    }
                    return null;
                    // ---
                }
                if ((f3 < f2) || (f3 > f4)) {
                    System.out.println("Ship: Wrong std yaw angle in gun #" + j + " of '" + s + "'");
                }
                shipgunproperties.HEAD_YAW_RANGE.set(f2, f4);
                shipgunproperties.HEAD_STD_YAW = f3;
                shipgunproperties.GUN_MIN_PITCH = getF(sectfile, s1, "GunMinPitch", -15F, 85F);
                shipgunproperties.GUN_STD_PITCH = getF(sectfile, s1, "GunStdPitch", -14F, 89.9F);
                shipgunproperties.GUN_MAX_PITCH = getF(sectfile, s1, "GunMaxPitch", 0.0F, 89.9F);
                shipgunproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s1, "HeadMaxYawSpeed", 0.1F, 999F);
                shipgunproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, s1, "GunMaxPitchSpeed", 0.1F, 999F);
                shipgunproperties.DELAY_AFTER_SHOOT = getF(sectfile, s1, "DelayAfterShoot", 0.0F, 999F);
                shipgunproperties.CHAINFIRE_TIME = getF(sectfile, s1, "ChainfireTime", 0.0F, 600F);
            }

            shipproperties.nGuns = i;
            shipproperties.SLIDER_DIST = getF(sectfile, s, "SliderDistance", 5F, 1000F);
            shipproperties.SPEED = ShipGeneric.KmHourToMSec(getF(sectfile, s, "Speed", 0.5F, 200F));
            shipproperties.DELAY_RESPAWN_MIN = 15F;
            shipproperties.DELAY_RESPAWN_MAX = 30F;
            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", shipproperties.meshName);
            Property.set(class1, "speed", shipproperties.SPEED);
            return shipproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg) {
            ShipGeneric shipgeneric = null;
            try {
                ShipGeneric.constr_arg1 = this.proper;
                ShipGeneric.constr_arg2 = actorspawnarg;
                shipgeneric = (ShipGeneric) this.cls.newInstance();
                ShipGeneric.constr_arg1 = null;
                ShipGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                ShipGeneric.constr_arg1 = null;
                ShipGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Ship object [class:" + this.cls.getName() + "]");
                return null;
            }
            return shipgeneric;
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
                this.proper = LoadShipProperties(Statics.getTechnicsFile(), s1, class1);

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

    class Mirror extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                switch (netmsginput.readByte()) {
                    case 73:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted);
                        }
                        ShipGeneric.this.timeOfDeath = netmsginput.readLong();
                        if (ShipGeneric.this.timeOfDeath < 0L) {
                            if (ShipGeneric.this.dying == 0) {
                                ShipGeneric.this.life = 1.0F;
                                ShipGeneric.this.setDefaultLivePose();
                                ShipGeneric.this.forgetAllAiming();
                            }
                        } else if (ShipGeneric.this.dying == 0) {
                            ShipGeneric.this.Die(null, ShipGeneric.this.timeOfDeath, false);
                        }
                        return true;

                    case 82:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted1);
                        }
                        ShipGeneric.this.life = 1.0F;
                        ShipGeneric.this.dying = 0;
                        ShipGeneric.this.setDiedFlag(false);
                        ShipGeneric.this.setMesh(ShipGeneric.this.prop.meshName);
                        ShipGeneric.this.setDefaultLivePose();
                        ShipGeneric.this.forgetAllAiming();
                        ShipGeneric.this.bodyDepth = 0.0F;
                        ShipGeneric.this.bodyPitch = ShipGeneric.this.bodyRoll = 0.0F;
                        ShipGeneric.this.setPosition();
                        ShipGeneric.this.pos.reset();
                        return true;

                    case 68:
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 1);
                            this.post(netmsgguaranted2);
                        }
                        ShipGeneric.this.timeOfDeath = netmsginput.readLong();
                        if ((ShipGeneric.this.timeOfDeath >= 0L) && (ShipGeneric.this.dying == 0)) {
                            com.maddox.rts.NetObj netobj2 = netmsginput.readNetObj();
                            Actor actor2 = netobj2 != null ? ((ActorNet) netobj2).actor() : null;
                            ShipGeneric.this.Die(actor2, ShipGeneric.this.timeOfDeath, true);
                        }
                        return true;
                }
                return false;
            }
            switch (netmsginput.readByte()) {
                default:
                    break;

                case 84:
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(false);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    byte byte0 = netmsginput.readByte();
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                    int i = netmsginput.readUnsignedByte();
                    ShipGeneric.this.Track_Mirror(byte0, actor, i);
                    break;

                case 70:
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(true);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    byte byte1 = netmsginput.readByte();
                    com.maddox.rts.NetObj netobj1 = netmsginput.readNetObj();
                    Actor actor1 = netobj1 != null ? ((ActorNet) netobj1).actor() : null;
                    float f = netmsginput.readFloat();
                    float f1 = (0.001F * (Message.currentGameTime() - Time.current())) + f;
                    int j = netmsginput.readUnsignedByte();
                    ShipGeneric.this.Fire_Mirror(byte1, actor1, j, f1);
                    break;

                case 68:
                    this.out.unLockAndSet(netmsginput, 1);
                    this.out.setIncludeTime(false);
                    this.postRealTo(Message.currentRealTime(), this.masterChannel(), this.out);
                    break;
            }
            return true;
        }

        NetMsgFiltered out;

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
            this.out = new NetMsgFiltered();
        }
    }

    class Master extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) {
                return true;
            }
            if (netmsginput.readByte() != 68) {
                return false;
            }
            if (ShipGeneric.this.dying != 0) {
                return true;
            } else {
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                ShipGeneric.this.Die(actor, -1L, true);
                return true;
            }
        }

        public Master(Actor actor) {
            super(actor);
        }
    }

    class Move extends Interpolate {

        public boolean tick() {
            if (ShipGeneric.this.dying == 0) {
                if (ShipGeneric.this.path != null) {
                    ShipGeneric.this.bodyDepth = 0.0F;
                    ShipGeneric.this.bodyPitch = ShipGeneric.this.bodyRoll = 0.0F;
                    ShipGeneric.this.setMovablePosition(NetServerParams.getServerTime());
                }
                if (ShipGeneric.this.wakeupTmr == 0L) {
                    for (int i = 0; i < ShipGeneric.this.prop.nGuns; i++) {
                        ShipGeneric.this.arms[i].aime.tick_();
                    }

                } else if (ShipGeneric.this.wakeupTmr > 0L) {
                    ShipGeneric.this.wakeupTmr--;
                } else if (++ShipGeneric.this.wakeupTmr == 0L) {
                    if (ShipGeneric.this.isAnyEnemyNear()) {
                        ShipGeneric.this.wakeupTmr = ShipGeneric.SecsToTicks(ShipGeneric.Rnd(ShipGeneric.this.DELAY_WAKEUP, ShipGeneric.this.DELAY_WAKEUP * 1.2F));
                    } else {
                        ShipGeneric.this.wakeupTmr = -ShipGeneric.SecsToTicks(ShipGeneric.Rnd(4F, 7F));
                    }
                }
                return true;
            }
            if (ShipGeneric.this.dying == 2) {
                if ((ShipGeneric.this.path != null) || !Mission.isDeathmatch()) {
                    ShipGeneric.this.eraseGuns();
                    return false;
                }
                if (ShipGeneric.this.respawnDelay-- > 0L) {
                    return true;
                }
                if (!ShipGeneric.this.isNetMaster()) {
                    ShipGeneric.this.respawnDelay = 10000L;
                    return true;
                } else {
                    ShipGeneric.this.life = 1.0F;
                    ShipGeneric.this.dying = 0;
                    ShipGeneric.this.wakeupTmr = 0L;
                    ShipGeneric.this.setDiedFlag(false);
                    ShipGeneric.this.forgetAllAiming();
                    ShipGeneric.this.setDefaultLivePose();
                    ShipGeneric.this.bodyDepth = 0.0F;
                    ShipGeneric.this.bodyPitch = ShipGeneric.this.bodyRoll = 0.0F;
                    ShipGeneric.this.setPosition();
                    ShipGeneric.this.pos.reset();
                    ShipGeneric.this.send_RespawnCommand();
                    return true;
                }
            }
            long l = NetServerParams.getServerTime() - ShipGeneric.this.timeOfDeath;
            if (l <= 0L) {
                l = 0L;
            }
            if (l >= ShipGeneric.this.timeForRotation) {
                ShipGeneric.this.bodyPitch = ShipGeneric.this.drownBodyPitch;
                ShipGeneric.this.bodyRoll = ShipGeneric.this.drownBodyRoll;
            } else {
                ShipGeneric.this.bodyPitch = ShipGeneric.this.drownBodyPitch * ((float) l / (float) ShipGeneric.this.timeForRotation);
                ShipGeneric.this.bodyRoll = ShipGeneric.this.drownBodyRoll * ((float) l / (float) ShipGeneric.this.timeForRotation);
            }
            ShipGeneric.this.bodyDepth = ShipGeneric.this.sinkingDepthSpeed * l * 0.001F * 5F;
            if (ShipGeneric.this.bodyDepth >= 5F) {
                float f = Math.abs(Geom.sinDeg(ShipGeneric.this.bodyPitch) * ShipGeneric.this.collisionR());
                f += ShipGeneric.this.bodyDepth;
                if ((f + ShipGeneric.this.bodyDepth) >= ShipGeneric.this.seaDepth) {
                    ShipGeneric.this.dying = 2;
                }
                if (ShipGeneric.this.bodyDepth > ShipGeneric.this.mesh().visibilityR()) {
                    ShipGeneric.this.dying = 2;
                }
            }
            if (ShipGeneric.this.path != null) {
                ShipGeneric.this.setMovablePosition(ShipGeneric.this.timeOfDeath);
            } else {
                ShipGeneric.this.setPosition();
            }
            return true;
        }

        Move() {
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

    private class Segment {

        public Point3d posIn;
        public Point3d posOut;
        public float   length;
        public long    timeIn;
        public long    timeOut;
        public float   speedIn;
        public float   speedOut;

        private Segment() {
        }

    }

    public class FiringDevice {

        private int   id;
        private Gun   gun;
        private Aim   aime;
        private float headYaw;
        private float gunPitch;

        public FiringDevice() {
        }
    }

    public static class ShipProperties {

        public String             meshName;
        public String             soundName;
        public StrengthProperties stre;
        public int                WEAPONS_MASK;
        public int                HITBY_MASK;
        public float              ATTACK_MAX_DISTANCE;
        public float              SLIDER_DIST;
        public float              SPEED;
        public float              DELAY_RESPAWN_MIN;
        public float              DELAY_RESPAWN_MAX;
        public ShipGunProperties  guns[];
        public int                nGuns;

        public ShipProperties() {
            this.meshName = null;
            this.soundName = null;
            this.stre = new StrengthProperties();
            this.WEAPONS_MASK = 4;
            this.HITBY_MASK = -2;
            this.ATTACK_MAX_DISTANCE = 1.0F;
            this.SLIDER_DIST = 1.0F;
            this.SPEED = 1.0F;
            this.DELAY_RESPAWN_MIN = 15F;
            this.DELAY_RESPAWN_MAX = 30F;
            this.guns = null;
        }
    }

    public static class ShipGunProperties {

        public Class       gunClass;
        public int         WEAPONS_MASK;
        public boolean     TRACKING_ONLY;
        public float       ATTACK_MAX_DISTANCE;
        public float       ATTACK_MAX_RADIUS;
        public float       ATTACK_MAX_HEIGHT;
        public int         ATTACK_FAST_TARGETS;
        public float       FAST_TARGETS_ANGLE_ERROR;
        public AnglesRange HEAD_YAW_RANGE;
        public float       HEAD_STD_YAW;
        public float       GUN_MIN_PITCH;
        public float       GUN_STD_PITCH;
        public float       GUN_MAX_PITCH;
        public float       HEAD_MAX_YAW_SPEED;
        public float       GUN_MAX_PITCH_SPEED;
        public float       DELAY_AFTER_SHOOT;
        public float       CHAINFIRE_TIME;
        public Point3d     fireOffset;
        public Orient      fireOrient;

        public ShipGunProperties() {
            this.gunClass = null;
            this.WEAPONS_MASK = 4;
            this.TRACKING_ONLY = false;
            this.ATTACK_MAX_DISTANCE = 1.0F;
            this.ATTACK_MAX_RADIUS = 1.0F;
            this.ATTACK_MAX_HEIGHT = 1.0F;
            this.ATTACK_FAST_TARGETS = 1;
            this.FAST_TARGETS_ANGLE_ERROR = 0.0F;
            this.HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            this.HEAD_STD_YAW = 0.0F;
            this.GUN_MIN_PITCH = -20F;
            this.GUN_STD_PITCH = -18F;
            this.GUN_MAX_PITCH = -15F;
            this.HEAD_MAX_YAW_SPEED = 720F;
            this.GUN_MAX_PITCH_SPEED = 60F;
            this.DELAY_AFTER_SHOOT = 1.0F;
            this.CHAINFIRE_TIME = 0.0F;
        }
    }

    public static final double Rnd(double d, double d1) {
        return World.Rnd().nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return World.Rnd().nextFloat(f, f1);
    }

    /* private */ boolean RndB(float f) {
        return World.Rnd().nextFloat(0.0F, 1.0F) < f;
    }

    public static final float KmHourToMSec(float f) {
        return f / 3.6F;
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5D + (f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    protected final boolean Head360(FiringDevice firingdevice) {
        return this.prop.guns[firingdevice.id].HEAD_YAW_RANGE.fullcircle();
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
            this.Die(null, -1L, true);
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
        float f;
        float f1;
        if (shot.powerType == 1) {
            f = this.prop.stre.EXPLHIT_MAX_TNT;
            f1 = this.prop.stre.EXPLHIT_MAX_TNT;
        } else {
            f = this.prop.stre.SHOT_MIN_ENERGY;
            f1 = this.prop.stre.SHOT_MAX_ENERGY;
        }
        float f2 = shot.power * Rnd(1.0F, 1.1F);
        if (f2 < f) {
            return;
        }
        float f3 = f2 / f1;
        this.life -= f3;
        if (this.life > 0.0F) {
            return;
        } else {
            this.Die(shot.initiator, -1L, true);
            return;
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
//        Explosion _tmp = explosion;
        if ((explosion.powerType == 2) && (explosion.chunkName != null)) {
            f *= 0.45F;
        }
        float f1;
        if (explosion.chunkName != null) {
            float f2 = f;
            f2 *= Rnd(0.5F, 1.6F);
            if (f2 < this.prop.stre.EXPLHIT_MIN_TNT) {
                return;
            }
            f1 = (f2 - this.prop.stre.EXPLHIT_MIN_TNT) / this.prop.stre.EXPLHIT_MAX_TNT;
        } else {
            float f3 = (float) this.pos.getAbsPoint().distance(p) - this.collisionR();
            float f4 = explosion.receivedTNT_1meterWater(f3, -(float) explosion.p.z);
            f4 *= Rnd(0.5F, 1.6F);
            if (f4 < this.prop.stre.EXPLNEAR_MIN_TNT) {
                return;
            }
            f1 = (f4 - this.prop.stre.EXPLHIT_MIN_TNT) / this.prop.stre.EXPLNEAR_MAX_TNT;
        }
        this.life -= f1;
        if (this.life > 0.0F) {
            return;
        } else {
            this.Die(explosion.initiator, -1L, true);
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
                    return 50F * (f / 355F);
                }
            }

        }

        return 1000F;
    }

    private void computeSinkingParams(long l) {
        RangeRandom rangerandom = new RangeRandom(l % 11073L);
        this.timeForRotation = 40000L + (long) (rangerandom.nextFloat() * 0.0F);
        this.drownBodyPitch = 50F - (rangerandom.nextFloat() * 20F);
        if (rangerandom.nextFloat() < 0.5F) {
            this.drownBodyPitch = -this.drownBodyPitch;
        }
        this.drownBodyRoll = 30F - (rangerandom.nextFloat() * 60F);
        this.sinkingDepthSpeed = 0.55F + (rangerandom.nextFloat() * 0.0F);
        this.seaDepth = this.computeSeaDepth(this.pos.getAbsPoint());
        this.seaDepth *= 1.0F + (rangerandom.nextFloat() * 0.2F);
    }

    private void showExplode() {
        if (this.mesh() instanceof HierMesh) {
            Explosions.Antiaircraft_Explode(this.pos.getAbsPoint());
        }
    }

    private void Die(Actor actor, long l, boolean flag) {
        if (this.dying != 0) {
            return;
        }
        if (l < 0L) {
            if (this.isNetMirror()) {
                this.send_DeathRequest(actor);
                return;
            }
            l = NetServerParams.getServerTime();
        }
        this.life = 0.0F;
        this.dying = 1;
        World.onActorDied(this, actor);
        this.forgetAllAiming();
        this.SetEffectsIntens(-1F);
        if (this.path != null) {
            this.bodyDepth = 0.0F;
            this.bodyPitch = this.bodyRoll = 0.0F;
            this.setMovablePosition(l);
        } else {
            this.bodyDepth = 0.0F;
            this.bodyPitch = this.bodyRoll = 0.0F;
            this.setPosition();
        }
        this.pos.reset();
        this.computeSinkingParams(l);
        if (Mission.isDeathmatch()) {
            this.timeOfDeath = Time.current();
            if (!flag) {
                this.timeOfDeath = 0L;
            }
        } else {
            this.timeOfDeath = l;
        }
        if (flag) {
            this.showExplode();
        }
        if (flag) {
            this.send_DeathCommand(actor);
        }
    }

    public void destroy() {
        if (this.isDestroyed()) {
            return;
        } else {
            this.eraseGuns();
            super.destroy();
            return;
        }
    }

    private boolean isAnyEnemyNear() {
        NearestEnemies.set(this.WeaponsMask());
        Actor actor = NearestEnemies.getAFoundEnemy(this.pos.getAbsPoint(), 2000D, this.getArmy());
        return actor != null;
    }

    private final FiringDevice GetFiringDevice(Aim aim) {
        for (int i = 0; i < this.prop.nGuns; i++) {
            if ((this.arms[i] != null) && (this.arms[i].aime == aim)) {
                return this.arms[i];
            }
        }

        System.out.println("Internal error 1: Can't find ship gun.");
        return null;
    }

    private final ShipGunProperties GetGunProperties(Aim aim) {
        for (int i = 0; i < this.prop.nGuns; i++) {
            if (this.arms[i].aime == aim) {
                return this.prop.guns[this.arms[i].id];
            }
        }

        System.out.println("Internal error 2: Can't find ship gun.");
        return null;
    }

    private void setGunAngles(FiringDevice firingdevice, float f, float f1) {
        firingdevice.headYaw = f;
        firingdevice.gunPitch = f1;
        this.hierMesh().chunkSetAngles("Head" + firingdevice.id, firingdevice.headYaw, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Gun" + firingdevice.id, -firingdevice.gunPitch, 0.0F, 0.0F);
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
                this.arms[i] = null;
            }

            this.arms = null;
        }
    }

    private void forgetAllAiming() {
        if (this.arms != null) {
            for (int i = 0; i < this.prop.nGuns; i++) {
                if ((this.arms[i] != null) && (this.arms[i].aime != null)) {
                    this.arms[i].aime.forgetAiming();
                }
            }

        }
    }

    private void CreateGuns() {
        this.arms = new FiringDevice[this.prop.nGuns];
        for (int i = 0; i < this.prop.nGuns; i++) {
            this.arms[i] = new FiringDevice();
            this.arms[i].id = i;
            this.arms[i].gun = null;
            try {
                this.arms[i].gun = (Gun) this.prop.guns[i].gunClass.newInstance();
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("Ship: Can't create gun '" + this.prop.guns[i].gunClass.getName() + "'");
            }
            this.arms[i].gun.set(this, "ShellStart" + i);
            this.arms[i].gun.loadBullets(-1);
            Loc loc = new Loc();
            this.hierMesh().setCurChunk("Head" + i);
            this.hierMesh().getChunkLocObj(loc);
            this.prop.guns[i].fireOffset = new Point3d();
            loc.get(this.prop.guns[i].fireOffset);
            this.prop.guns[i].fireOrient = new Orient();
            loc.get(this.prop.guns[i].fireOrient);
            if (this.prop.guns[i].TRACKING_ONLY) {
                this.arms[i].aime = new Aim(this, this.isNetMirror(), this.SLOWFIRE_K * this.prop.guns[i].DELAY_AFTER_SHOOT, true);
            } else {
                this.arms[i].aime = new Aim(this, this.isNetMirror(), this.SLOWFIRE_K * this.prop.guns[i].DELAY_AFTER_SHOOT);
            }
        }

    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    private void setDefaultLivePose() {
        int i = this.mesh().hookFind("Ground_Level");
        if (i != -1) {
            Matrix4d matrix4d = new Matrix4d();
            this.hierMesh().hookMatrix(i, matrix4d);
        }
        if (this.mesh() instanceof HierMesh) {
            this.hierMesh().chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        }
        for (int j = 0; j < this.prop.nGuns; j++) {
            this.setGunAngles(this.arms[j], this.prop.guns[j].HEAD_STD_YAW, this.prop.guns[j].GUN_STD_PITCH);
        }

        this.bodyDepth = 0.0F;
        this.align();
    }

    protected ShipGeneric() {
        this(constr_arg1, constr_arg2);
    }

    private ShipGeneric(ShipProperties shipproperties, ActorSpawnArg actorspawnarg) {
        super(shipproperties.meshName);
        this.prop = null;
        this.arms = null;
        this.cachedSeg = 0;
        this.life = 1.0F;
        this.dying = 0;
        this.respawnDelay = 0L;
        this.wakeupTmr = 0L;
        this.DELAY_WAKEUP = 0.0F;
        this.SKILL_IDX = 2;
        this.SLOWFIRE_K = 1.0F;
        this.pipe = null;
        this.nose = null;
        this.tail = null;
        this.outCommand = new NetMsgFiltered();
        this.prop = shipproperties;
        actorspawnarg.setStationary(this);
        this.path = null;
        this.collide(true);
        this.drawing(true);
        this.bodyDepth = 0.0F;
        this.bodyPitch = this.bodyRoll = 0.0F;
        this.bodyYaw = actorspawnarg.orient.getYaw();
        this.setPosition();
        this.pos.reset();
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.SKILL_IDX = Chief.new_SKILL_IDX;
        this.SLOWFIRE_K = Chief.new_SLOWFIRE_K;
        this.DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
        this.wakeupTmr = 0L;
        this.CreateGuns();
        this.setDefaultLivePose();
        if (!this.isNetMirror() && (this.prop.nGuns > 0) && (this.DELAY_WAKEUP > 0.0F)) {
            this.wakeupTmr = -SecsToTicks(Rnd(2.0F, 7F));
        }
        if (!this.interpEnd("move")) {
            this.interpPut(new Move(), "move", Time.current(), null);
        }
    }

    public void setMesh(String s) {
        super.setMesh(s);
        if (Config.cur.b3dgunners) {
            return;
        } else {
            this.mesh().materialReplaceToNull("Pilot1");
            return;
        }
    }

    public ShipGeneric(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
        this.prop = null;
        this.arms = null;
        this.cachedSeg = 0;
        this.life = 1.0F;
        this.dying = 0;
        this.respawnDelay = 0L;
        this.wakeupTmr = 0L;
        this.DELAY_WAKEUP = 0.0F;
        this.SKILL_IDX = 2;
        this.SLOWFIRE_K = 1.0F;
        this.pipe = null;
        this.nose = null;
        this.tail = null;
        this.outCommand = new NetMsgFiltered();
        try {
            int j = sectfile.sectionIndex(s1);
            String s3 = sectfile.var(j, 0);
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
            if (this.prop.soundName != null) {
                this.newSound(this.prop.soundName, true);
            }
            this.setName(s);
            this.setArmy(i);
            this.LoadPath(sectfile1, s2);
            this.cachedSeg = 0;
            this.bodyDepth = 0.0F;
            this.bodyPitch = this.bodyRoll = 0.0F;
            this.setMovablePosition(NetServerParams.getServerTime());
            this.pos.reset();
            this.collide(true);
            this.drawing(true);
            this.pipe = null;
            if (this.mesh().hookFind("Vapor") >= 0) {
                HookNamed hooknamed = new HookNamed(this, "Vapor");
                this.pipe = Eff3DActor.New(this, hooknamed, null, 1.0F, "Effects/Smokes/SmokePipeShip.eff", -1F);
            }
            this.wake[2] = this.wake[1] = this.wake[0] = null;
            this.tail = null;
            this.nose = null;
            boolean flag = (this.prop.SLIDER_DIST / 2.5F) < 90F;
            if (this.mesh().hookFind("_Prop") >= 0) {
                HookNamedZ0 hooknamedz0 = new HookNamedZ0(this, "_Prop");
                this.tail = Eff3DActor.New(this, hooknamedz0, null, 1.0F, flag ? "3DO/Effects/Tracers/ShipTrail/PropWakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/PropWake.eff", -1F);
            }
            if (this.mesh().hookFind("_Centre") >= 0) {
                Loc loc = new Loc();
                Loc loc1 = new Loc();
                HookNamed hooknamed1 = new HookNamed(this, "_Left");
                hooknamed1.computePos(this, new Loc(), loc);
                HookNamed hooknamed2 = new HookNamed(this, "_Right");
                hooknamed2.computePos(this, new Loc(), loc1);
                float f = (float) loc.getPoint().distance(loc1.getPoint());
                HookNamedZ0 hooknamedz0_1 = new HookNamedZ0(this, "_Centre");
                if (this.mesh().hookFind("_Prop") >= 0) {
                    HookNamedZ0 hooknamedz0_3 = new HookNamedZ0(this, "_Prop");
                    Loc loc2 = new Loc();
                    hooknamedz0_1.computePos(this, new Loc(), loc2);
                    Loc loc3 = new Loc();
                    hooknamedz0_3.computePos(this, new Loc(), loc3);
                    float f1 = (float) loc2.getPoint().distance(loc3.getPoint());
                    this.wake[0] = Eff3DActor.New(this, hooknamedz0_3, new Loc((-f1) * 0.33000000000000002D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff" : "3DO/Effects/Tracers/ShipTrail/Wake.eff", -1F);
                    this.wake[1] = Eff3DActor.New(this, hooknamedz0_1, new Loc(f1 * 0.14999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff" : "3DO/Effects/Tracers/ShipTrail/WakeS.eff", -1F);
                    this.wake[2] = Eff3DActor.New(this, hooknamedz0_1, new Loc((-f1) * 0.14999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, flag ? "3DO/Effects/Tracers/ShipTrail/WakeBoatS.eff" : "3DO/Effects/Tracers/ShipTrail/WakeS.eff", -1F);
                } else {
                    this.wake[0] = Eff3DActor.New(this, hooknamedz0_1, new Loc((-f) * 0.29999999999999999D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), f, (this.prop.SLIDER_DIST / 2.5D) >= 50D ? "3DO/Effects/Tracers/ShipTrail/Wake.eff" : "3DO/Effects/Tracers/ShipTrail/WakeBoat.eff", -1F);
                }
            }
            if (this.mesh().hookFind("_Nose") >= 0) {
                HookNamedZ0 hooknamedz0_2 = new HookNamedZ0(this, "_Nose");
                this.nose = Eff3DActor.New(this, hooknamedz0_2, new Loc(0.0D, 0.0D, 0.0D, 0.0F, 30F, 0.0F), 1.0F, "3DO/Effects/Tracers/ShipTrail/SideWave.eff", -1F);
            }
            this.SetEffectsIntens(0.0F);
            int k = Mission.cur().getUnitNetIdRemote(this);
            NetChannel netchannel = Mission.cur().getNetMasterChannel();
            if (netchannel == null) {
                this.net = new Master(this);
            } else if (k != 0) {
                this.net = new Mirror(this, netchannel, k);
            }
            this.SKILL_IDX = Chief.new_SKILL_IDX;
            this.SLOWFIRE_K = Chief.new_SLOWFIRE_K;
            this.DELAY_WAKEUP = Chief.new_DELAY_WAKEUP;
            this.wakeupTmr = 0L;
            this.CreateGuns();
            this.setDefaultLivePose();
            if (!this.isNetMirror() && (this.prop.nGuns > 0) && (this.DELAY_WAKEUP > 0.0F)) {
                this.wakeupTmr = -SecsToTicks(Rnd(2.0F, 7F));
            }
            if (!this.interpEnd("move")) {
                this.interpPut(new Move(), "move", Time.current(), null);
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
        if (this.pipe != null) {
            if (f >= 0.0F) {
                this.pipe._setIntesity(f);
            } else {
                this.pipe._finish();
                this.pipe = null;
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
                    }
                }
            }
            if (k >= (j - 1)) {
                d = 1.0D;
            }
            Segment segment7 = new Segment();
            segment7.posIn = new Point3d(f1, f2, 0.0D);
            if (Math.abs(d) < 0.10000000000000001D) {
                segment7.timeIn = 0L;
            } else {
                segment7.timeIn = (long) ((d * 60D * 1000D) + (d <= 0.0D ? -0.5D : 0.5D));
            }
            if ((f7 <= 0.0F) && ((k == 0) || (k == (j - 1)) || (segment7.timeIn == 0L))) {
                f7 = this.prop.SPEED;
            }
            segment7.speedIn = f7;
            this.path.add(segment7);
        }

        for (int l = 0; l < (this.path.size() - 1); l++) {
            Segment segment = (Segment) this.path.get(l);
            Segment segment1 = (Segment) this.path.get(l + 1);
            if ((segment.timeIn > 0L) && (segment1.timeIn > 0L)) {
                Segment segment2 = new Segment();
                segment2.posIn = new Point3d(segment.posIn);
                segment2.posIn.add(segment1.posIn);
                segment2.posIn.scale(0.5D);
                segment2.timeIn = 0L;
                segment2.speedIn = (segment.speedIn + segment1.speedIn) * 0.5F;
                this.path.add(l + 1, segment2);
            }
        }

        int i1 = 0;
        float f = ((Segment) this.path.get(i1)).length;
        int j1;
        for (; i1 < (this.path.size() - 1); i1 = j1) {
            j1 = i1 + 1;
            do {
                Segment segment3 = (Segment) this.path.get(j1);
                if (segment3.speedIn > 0.0D) {
                    break;
                }
                f += segment3.length;
                j1++;
            } while (true);
            if ((j1 - i1) <= 1) {
                continue;
            }
            float f3 = ((Segment) this.path.get(i1)).length;
            float f5 = ((Segment) this.path.get(i1)).speedIn;
            float f6 = ((Segment) this.path.get(j1)).speedIn;
            for (int i2 = i1 + 1; i2 < j1; i2++) {
                Segment segment6 = (Segment) this.path.get(i2);
                float f9 = f3 / f;
                segment6.speedIn = (f5 * (1.0F - f9)) + (f6 * f9);
                f += segment6.length;
            }

        }

        long l1 = 0L;
        for (int k1 = 0; k1 < (this.path.size() - 1); k1++) {
            Segment segment4 = (Segment) this.path.get(k1);
            Segment segment5 = (Segment) this.path.get(k1 + 1);
            if (k1 == 0) {
                l1 = segment4.timeIn;
            }
            segment4.posOut = new Point3d(segment5.posIn);
            segment5.posIn = segment4.posOut;
            segment4.length = (float) segment4.posIn.distance(segment5.posIn);
            float f8 = segment4.speedIn;
            float f10 = segment5.speedIn;
            float f11 = (f8 + f10) * 0.5F;
            if (segment4.timeIn > 0L) {
                if (segment4.timeIn > l1) {
                    segment4.timeIn -= l1;
                } else {
                    segment4.timeIn = 0L;
                }
            }
            if ((segment4.timeIn == 0L) && (segment5.timeIn > 0L)) {
                int j2 = (int) ((((2.0F * segment4.length) / f8) * 1000F) + 0.5F);
                j2 = (int) (j2 + l1);
                if (segment5.timeIn > j2) {
                    segment5.timeIn -= j2;
                } else {
                    segment5.timeIn = 0L;
                }
            }
            if (segment4.timeIn > 0L) {
                segment4.speedIn = 0.0F;
                segment4.speedOut = f10;
                float f12 = (((2.0F * segment4.length) / f10) * 1000F) + 0.5F;
                segment4.timeIn = l1 + segment4.timeIn;
                segment4.timeOut = segment4.timeIn + (int) f12;
                l1 = segment4.timeOut;
                continue;
            }
            if (segment5.timeIn > 0L) {
                segment4.speedIn = f8;
                segment4.speedOut = 0.0F;
                float f13 = (((2.0F * segment4.length) / f8) * 1000F) + 0.5F;
                segment4.timeIn = l1 + segment4.timeIn;
                segment4.timeOut = segment4.timeIn + (int) f13;
                l1 = segment4.timeOut + segment5.timeIn;
            } else {
                segment4.speedIn = f8;
                segment4.speedOut = f10;
                float f14 = ((segment4.length / f11) * 1000F) + 0.5F;
                segment4.timeIn = l1;
                segment4.timeOut = segment4.timeIn + (int) f14;
                l1 = segment4.timeOut;
            }
        }

        this.path.remove(this.path.size() - 1);
    }

    public void align() {
        this.pos.getAbs(p);
        p.z = Engine.land().HQ(p.x, p.y) - this.bodyDepth;
        this.pos.setAbs(p);
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
        int i = this.cachedSeg;
        float f6 = this.prop.SLIDER_DIST - (segment.length - f5);
        if (f6 <= 0.0F) {
            p1.interpolate(segment.posIn, segment.posOut, (f5 + this.prop.SLIDER_DIST) / segment.length);
        } else {
            do {
                if ((i + 1) >= this.path.size()) {
                    p1.interpolate(segment.posIn, segment.posOut, 1.0F + (f6 / segment.length));
                    break;
                }
                segment = (Segment) this.path.get(++i);
                if (f6 <= segment.length) {
                    p1.interpolate(segment.posIn, segment.posOut, f6 / segment.length);
                    break;
                }
                f6 -= segment.length;
            } while (true);
        }
        i = this.cachedSeg;
        segment = (Segment) this.path.get(i);
        f6 = this.prop.SLIDER_DIST - f5;
        if (f6 <= 0.0F) {
            p2.interpolate(segment.posIn, segment.posOut, (f5 - this.prop.SLIDER_DIST) / segment.length);
        } else {
            do {
                if (i <= 0) {
                    p2.interpolate(segment.posIn, segment.posOut, 0.0F - (f6 / segment.length));
                    break;
                }
                segment = (Segment) this.path.get(--i);
                if (f6 <= segment.length) {
                    p2.interpolate(segment.posIn, segment.posOut, 1.0F - (f6 / segment.length));
                    break;
                }
                f6 -= segment.length;
            } while (true);
        }
        p.interpolate(p1, p2, 0.5F);
        tmpv.sub(p1, p2);
        if (tmpv.lengthSquared() < 0.0010000000474974513D) {
            Segment segment1 = (Segment) this.path.get(this.cachedSeg);
            tmpv.sub(segment1.posOut, segment1.posIn);
        }
        float f7 = (float) (Math.atan2(tmpv.y, tmpv.x) * 57.295779513082323D);
        this.setPosition(p, f7);
    }

    private void setPosition(Point3d point3d, float f) {
        this.bodyYaw = f;
        o.setYPR(this.bodyYaw, this.bodyPitch, this.bodyRoll);
        point3d.z = -this.bodyDepth;
        this.pos.setAbs(point3d, o);
    }

    private void setPosition() {
        o.setYPR(this.bodyYaw, this.bodyPitch, this.bodyRoll);
        this.pos.setAbs(o);
        this.align();
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
        }
        return abulletproperties[0].powerType != 1 ? 0 : 1;
    }

    public int chooseShotpoint(BulletProperties bulletproperties) {
        return this.dying == 0 ? 0 : -1;
    }

    public boolean getShotpointOffset(int i, Point3d point3d) {
        if (this.dying != 0) {
            return false;
        }
        if (i != 0) {
            return false;
        }
        if (point3d != null) {
            point3d.set(0.0D, 0.0D, 0.0D);
        }
        return true;
    }

    public float AttackMaxDistance() {
        return this.prop.ATTACK_MAX_DISTANCE;
    }

    public float AttackMaxDistance(Aim aim) {
        return this.GetGunProperties(aim).ATTACK_MAX_DISTANCE;
    }

    private void send_DeathCommand(Actor actor) {
        if (!this.isNetMaster()) {
            return;
        }
        if (Mission.isDeathmatch()) {
            float f = Mission.respawnTime("Ship");
            this.respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
        } else {
            this.respawnDelay = 0L;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeLong(this.timeOfDeath);
            netmsgguaranted.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            this.net.post(netmsgguaranted);
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
    }

    private void send_FireCommand(int i, Actor actor, int j, float f) {
        if (!this.isNetMaster()) {
            return;
        }
        if (!this.net.isMirrored()) {
            return;
        }
        if (!Actor.isValid(actor) || !actor.isNet()) {
            return;
        }
        j &= 0xff;
        if (f < 0.0F) {
            try {
                this.outCommand.unLockAndClear();
                this.outCommand.writeByte(84);
                this.outCommand.writeByte(i);
                this.outCommand.writeNetObj(actor.net);
                this.outCommand.writeByte(j);
                this.outCommand.setIncludeTime(false);
                this.net.post(Time.current(), this.outCommand);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                exception.printStackTrace();
            }
        } else {
            try {
                this.outCommand.unLockAndClear();
                this.outCommand.writeByte(70);
                this.outCommand.writeByte(i);
                this.outCommand.writeFloat(f);
                this.outCommand.writeNetObj(actor.net);
                this.outCommand.writeByte(j);
                this.outCommand.setIncludeTime(true);
                this.net.post(Time.current(), this.outCommand);
            } catch (Exception exception1) {
                System.out.println(exception1.getMessage());
                exception1.printStackTrace();
            }
        }
    }

    private void send_DeathRequest(Actor actor) {
        if (!this.isNetMirror()) {
            return;
        }
        if (this.net.masterChannel() instanceof NetChannelInStream) {
            return;
        }
        try {
            NetMsgFiltered netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(68);
            netmsgfiltered.writeNetObj(actor != null ? ((com.maddox.rts.NetObj) (actor.net)) : null);
            netmsgfiltered.setIncludeTime(false);
            this.net.postTo(Time.current(), this.net.masterChannel(), netmsgfiltered);
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

    public void netFirstUpdate(NetChannel netchannel) throws IOException {
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        netmsgguaranted.writeByte(73);
        if (this.dying == 0) {
            netmsgguaranted.writeLong(-1L);
        } else {
            netmsgguaranted.writeLong(this.timeOfDeath);
        }
        this.net.postTo(netchannel, netmsgguaranted);
    }

    public float getReloadingTime(Aim aim) {
        return this.SLOWFIRE_K * this.GetGunProperties(aim).DELAY_AFTER_SHOOT;
    }

    public float chainFireTime(Aim aim) {
        float f = this.GetGunProperties(aim).CHAINFIRE_TIME;
        return f > 0.0F ? f * Rnd(0.75F, 1.25F) : 0.0F;
    }

    public float probabKeepSameEnemy(Actor actor) {
        return 0.75F;
    }

    public float minTimeRelaxAfterFight() {
        return 0.1F;
    }

    public void gunStartParking(Aim aim) {
        FiringDevice firingdevice = this.GetFiringDevice(aim);
        ShipGunProperties shipgunproperties = this.prop.guns[firingdevice.id];
        aim.setRotationForParking(firingdevice.headYaw, firingdevice.gunPitch, shipgunproperties.HEAD_STD_YAW, shipgunproperties.GUN_STD_PITCH, shipgunproperties.HEAD_YAW_RANGE, shipgunproperties.HEAD_MAX_YAW_SPEED, shipgunproperties.GUN_MAX_PITCH_SPEED);
    }

    public void gunInMove(boolean flag, Aim aim) {
        FiringDevice firingdevice = this.GetFiringDevice(aim);
        float f = aim.t();
        float f1 = aim.anglesYaw.getDeg(f);
        float f2 = aim.anglesPitch.getDeg(f);
        this.setGunAngles(firingdevice, f1, f2);
        this.pos.inValidate(false);
    }

    public Actor findEnemy(Aim aim) {
        if (this.isNetMirror()) {
            return null;
        }
        ShipGunProperties shipgunproperties = this.GetGunProperties(aim);
        Actor actor = null;
        switch (shipgunproperties.ATTACK_FAST_TARGETS) {
            case 0:
                NearestEnemies.set(shipgunproperties.WEAPONS_MASK, -9999.9F, KmHourToMSec(100F));
                break;

            case 1:
                NearestEnemies.set(shipgunproperties.WEAPONS_MASK);
                break;

            default:
                NearestEnemies.set(shipgunproperties.WEAPONS_MASK, KmHourToMSec(100F), 9999.9F);
                break;
        }
        actor = NearestEnemies.getAFoundEnemy(this.pos.getAbsPoint(), shipgunproperties.ATTACK_MAX_RADIUS, this.getArmy(), (float) shipgunproperties.fireOffset.z);
        if (actor == null) {
            return null;
        }
        if (!(actor instanceof Prey)) {
            System.out.println("ship: nearest enemies: non-Prey");
            return null;
        }
        FiringDevice firingdevice = this.GetFiringDevice(aim);
        BulletProperties bulletproperties = null;
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
        aim.shotpoint_idx = j;
        double d = this.distance(actor);
        d /= this.AttackMaxDistance(aim);
        aim.setAimingError(World.Rnd().nextFloat(-1F, 1.0F), World.Rnd().nextFloat(-1F, 1.0F), 0.0F);
        aim.scaleAimingError(23.47F * (4 - this.SKILL_IDX) * (4 - this.SKILL_IDX));
        if (actor instanceof Aircraft) {
            d *= 0.25D;
        }
        aim.scaleAimingError((float) d);
        return actor;
    }

    public boolean enterToFireMode(int i, Actor actor, float f, Aim aim) {
        if (!this.isNetMirror()) {
            FiringDevice firingdevice = this.GetFiringDevice(aim);
            this.send_FireCommand(firingdevice.id, actor, aim.shotpoint_idx, i != 0 ? f : -1F);
        }
        return true;
    }

    private void Track_Mirror(int i, Actor actor, int j) {
        if (actor == null) {
            return;
        }
        if ((this.arms == null) || (this.arms[i].aime == null)) {
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
        if ((this.arms == null) || (this.arms[i].aime == null)) {
            return;
        }
        if (f <= 0.2F) {
            f = 0.2F;
        }
        if (f >= 15F) {
            f = 15F;
        }
        this.arms[i].aime.passive_StartFiring(1, actor, j, f);
    }

    public int targetGun(Aim aim, Actor actor, float f, boolean flag) {
        if (!Actor.isValid(actor) || !actor.isAlive() || (actor.getArmy() == 0)) {
            return 0;
        }
        FiringDevice firingdevice = this.GetFiringDevice(aim);
        if (firingdevice.gun instanceof CannonMidrangeGeneric) {
            int i = ((Prey) actor).chooseBulletType(firingdevice.gun.prop.bullet);
            if (i < 0) {
                return 0;
            }
            ((CannonMidrangeGeneric) firingdevice.gun).setBulletType(i);
        }
        boolean flag1 = ((Prey) actor).getShotpointOffset(aim.shotpoint_idx, p1);
        if (!flag1) {
            return 0;
        }
        ShipGunProperties shipgunproperties = this.prop.guns[firingdevice.id];
        float f1 = f * Rnd(0.8F, 1.2F);
        if (!Aimer.aim((BulletAimer) firingdevice.gun, actor, this, f1, p1, shipgunproperties.fireOffset)) {
            return 0;
        }
        Point3d point3d = new Point3d();
        Aimer.getPredictedTargetPosition(point3d);
        point3d.add(aim.getAimingError());
        Point3d point3d1 = Aimer.getHunterFirePoint();
        float f2 = 0.05F;
        double d = point3d.distance(point3d1);
        float fd1 = (float) point3d.z;
        if (f1 > 0.001F) {
            Point3d point3d2 = new Point3d();
            actor.pos.getAbs(point3d2);
            point3d2.add(aim.getAimingError());
            tmpv.sub(point3d, point3d2);
            double d2 = tmpv.length();
            if (d2 > 0.001D) {
                float f7 = (float) d2 / f1;
                if (f7 > 200F) {
                    f7 = 200F;
                }
                float f8 = f7 * 0.01F;
                point3d2.sub(point3d1);
                double d3 = (point3d2.x * point3d2.x) + (point3d2.y * point3d2.y) + (point3d2.z * point3d2.z);
                if (d3 > 0.01D) {
                    float f9 = (float) tmpv.dot(point3d2);
                    f9 /= (float) (d2 * Math.sqrt(d3));
                    f9 = (float) Math.sqrt(1.0F - (f9 * f9));
                    f8 *= 0.4F + (0.6F * f9);
                }
                f8 *= 1.3F;
                f8 *= Aim.AngleErrorKoefForSkill[this.SKILL_IDX];
                int k = Mission.curCloudsType();
                if (k > 2) {
                    float f10 = k <= 4 ? 800F : 400F;
                    float f11 = (float) (d / f10);
                    if (f11 > 1.0F) {
                        if (f11 > 10F) {
                            return 0;
                        }
                        f11 = (f11 - 1.0F) / 9F;
                        f8 *= f11 + 1.0F;
                    }
                }
                if ((k >= 3) && (fd1 > Mission.curCloudsHeight())) {
                    f8 *= 1.25F;
                }
                f2 += f8;
            }
        }
        if (actor instanceof Aircraft) {
            aim.scaleAimingError(0.73F);
            f2 = (float) (f2 * (0.5D + (Aim.AngleErrorKoefForSkill[this.SKILL_IDX] * 0.75F)));
        } else if (aim.getAimingError().length() > 1.1100000143051147D) {
            aim.scaleAimingError(0.973F);
        }
        if (World.Sun().ToSun.z < -0.15F) {
            float f4 = (-World.Sun().ToSun.z - 0.15F) / 0.13F;
            if (f4 >= 1.0F) {
                f4 = 1.0F;
            }
            if ((actor instanceof Aircraft) && ((Time.current() - ((Aircraft) actor).tmSearchlighted) < 1000L)) {
                f4 = 0.0F;
            }
            f2 += 10F * f4;
        }
        float f5 = (float) actor.getSpeed(null) - 10F;
        if (f5 > 0.0F) {
            float f6 = 83.33334F;
            f5 = f5 < f6 ? f5 / f6 : 1.0F;
            f2 += f5 * shipgunproperties.FAST_TARGETS_ANGLE_ERROR;
        }
        Vector3d vector3d = new Vector3d();
        if (!((BulletAimer) firingdevice.gun).FireDirection(point3d1, point3d, vector3d)) {
            return 0;
        }
        float f3;
        if (flag) {
            f3 = 99999F;
            fd1 = 99999F;
        } else {
            f3 = shipgunproperties.HEAD_MAX_YAW_SPEED;
            fd1 = shipgunproperties.GUN_MAX_PITCH_SPEED;
        }
        o.add(shipgunproperties.fireOrient, this.pos.getAbs().getOrient());
        int j = aim.setRotationForTargeting(this, o, point3d1, firingdevice.headYaw, firingdevice.gunPitch, vector3d, f2, f1, shipgunproperties.HEAD_YAW_RANGE, shipgunproperties.GUN_MIN_PITCH, shipgunproperties.GUN_MAX_PITCH, f3, fd1, 0.0F);
        return j;
    }

    public void singleShot(Aim aim) {
        FiringDevice firingdevice = this.GetFiringDevice(aim);
        if (!this.prop.guns[firingdevice.id].TRACKING_ONLY) {
            firingdevice.gun.shots(1);
        }
    }

    public void startFire(Aim aim) {
        FiringDevice firingdevice = this.GetFiringDevice(aim);
        if (!this.prop.guns[firingdevice.id].TRACKING_ONLY) {
            firingdevice.gun.shots(-1);
        }
    }

    public void continueFire(Aim aim) {
    }

    public void stopFire(Aim aim) {
        FiringDevice firingdevice = this.GetFiringDevice(aim);
        if (!this.prop.guns[firingdevice.id].TRACKING_ONLY) {
            firingdevice.gun.shots(0);
        }
    }

    public boolean isVisibilityLong() {
        return true;
    }

    public int zutiGetDying() {
        return this.dying;
    }

    public boolean zutiIsStatic() {
        return (this.path == null) || (this.path.size() <= 0);
    }

    private ShipProperties        prop;
    private FiringDevice          arms[];
    private ArrayList             path;
    private int                   cachedSeg;
    private float                 bodyDepth;
    private float                 bodyYaw;
    private float                 bodyPitch;
    private float                 bodyRoll;
    private float                 seaDepth;
    private long                  timeOfDeath;
    private long                  timeForRotation;
    private float                 drownBodyPitch;
    private float                 drownBodyRoll;
    private float                 sinkingDepthSpeed;
    private float                 life;
    private int                   dying;
    static final int              DYING_NONE  = 0;
    static final int              DYING_SINK  = 1;
    static final int              DYING_DEAD  = 2;
    private long                  respawnDelay;
    private long                  wakeupTmr;
    public float                  DELAY_WAKEUP;
    public int                    SKILL_IDX;
    public float                  SLOWFIRE_K;
    private Eff3DActor            pipe;
    private Eff3DActor            wake[]      = { null, null, null };
    private Eff3DActor            nose;
    private Eff3DActor            tail;
    private static ShipProperties constr_arg1 = null;
    private static ActorSpawnArg  constr_arg2 = null;
    private static Point3d        p           = new Point3d();
    private static Point3d        p1          = new Point3d();
    private static Point3d        p2          = new Point3d();
    private static Orient         o           = new Orient();
//    private static Vector3f n = new Vector3f();
    private static Vector3d       tmpv        = new Vector3d();
    private NetMsgFiltered        outCommand;
}
