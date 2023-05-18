package com.maddox.il2.objects.vehicles.lights;

import java.io.IOException;
import java.util.List;

import com.maddox.JGP.Geom;
import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Point3f;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.AnglesFork;
import com.maddox.il2.ai.AnglesRange;
import com.maddox.il2.ai.Explosion;
import com.maddox.il2.ai.MsgExplosionListener;
import com.maddox.il2.ai.MsgShotListener;
import com.maddox.il2.ai.Shot;
import com.maddox.il2.ai.TableFunctions;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.Aim;
import com.maddox.il2.ai.ground.HunterInterface;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.Obstacle;
import com.maddox.il2.ai.ground.Predator;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorMeshDraw;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.BulletProperties;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.engine.LightPoint;
import com.maddox.il2.engine.LightPointActor;
import com.maddox.il2.engine.LightPointWorld;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgCollisionRequestListener;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.effects.LightsGlare;
import com.maddox.il2.objects.vehicles.aeronautics.Balloon;
import com.maddox.il2.objects.vehicles.tanks.TankGeneric;
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
import com.maddox.sas1946.il2.util.TrueRandom;
import com.maddox.util.HashMapExt;
import com.maddox.util.TableFunction2;

public abstract class SearchlightGeneric extends ActorHMesh implements MsgCollisionRequestListener, MsgExplosionListener, MsgShotListener, Predator, Obstacle, ActorAlign, HunterInterface {
    public static class SPAWN implements ActorSpawn {

        private static float getF(SectFile sectfile, String s, String s1, float f, float f1) {
            float f2 = sectfile.get(s, s1, -9865.345F);
            if ((f2 == -9865.345F) || (f2 < f) || (f2 > f1)) {
                if (f2 == -9865.345F) {
                    System.out.println("Searchlight: Parameter [" + s + "]:<" + s1 + "> " + "not found");
                } else {
                    System.out.println("Searchlight: Value of [" + s + "]:<" + s1 + "> (" + f2 + ")" + " is out of range (" + f + ";" + f1 + ")");
                }
                throw new RuntimeException("Can't set property");
            } else {
                return f2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1) {
            String s2 = sectfile.get(s, s1);
            if ((s2 == null) || (s2.length() <= 0)) {
                System.out.print("Searchlight: Parameter [" + s + "]:<" + s1 + "> ");
                System.out.println(s2 != null ? "is empty" : "not found");
                throw new RuntimeException("Can't set property");
            } else {
                return s2;
            }
        }

        private static String getS(SectFile sectfile, String s, String s1, String s2) {
            String s3 = sectfile.get(s, s1);
            if ((s3 == null) || (s3.length() <= 0)) {
                return s2;
            } else {
                return s3;
            }
        }

        private static SearchlightProperties LoadSearchlightProperties(SectFile sectfile, String s, Class class1) {
            SearchlightProperties searchlightproperties = new SearchlightProperties();
            String s1 = getS(sectfile, s, "PanzerType", null);
            if (s1 == null) {
                s1 = "Car";
            }
            searchlightproperties.fnShotPanzer = TableFunctions.GetFunc2(s1 + "ShotPanzer");
            searchlightproperties.fnExplodePanzer = TableFunctions.GetFunc2(s1 + "ExplodePanzer");
            searchlightproperties.PANZER_TNT_TYPE = getF(sectfile, s, "PanzerSubtype", 0.0F, 100F);
            searchlightproperties.meshSummer = getS(sectfile, s, "MeshSummer");
            searchlightproperties.meshDesert = getS(sectfile, s, "MeshDesert", searchlightproperties.meshSummer);
            searchlightproperties.meshWinter = getS(sectfile, s, "MeshWinter", searchlightproperties.meshSummer);
            searchlightproperties.PANZER = getF(sectfile, s, "PanzerBody", 0.001F, 9.999F);
            searchlightproperties.HITBY_MASK = searchlightproperties.PANZER <= 0.015F ? -1 : -2;
            searchlightproperties.explodeName = getS(sectfile, s, "Explode", "Searchlight");
            searchlightproperties.Hclear = getF(sectfile, s, "MaxDistance", 6F, 12000F);
            float f = getF(sectfile, s, "HeadYawHalfRange", 0.0F, 180F);
            searchlightproperties.HEAD_YAW_RANGE.set(-f, f);
            searchlightproperties.GUN_MIN_PITCH = getF(sectfile, s, "GunMinPitch", -15F, 85F);
            searchlightproperties.GUN_STD_PITCH = getF(sectfile, s, "GunStdPitch", -15F, 90F);
            searchlightproperties.GUN_MAX_PITCH = getF(sectfile, s, "GunMaxPitch", 0.0F, 90F);
            searchlightproperties.HEAD_MAX_YAW_SPEED = getF(sectfile, s, "HeadMaxYawSpeed", 0.1F, 999F);
            searchlightproperties.GUN_MAX_PITCH_SPEED = getF(sectfile, s, "GunMaxPitchSpeed", 0.1F, 999F);
            searchlightproperties.SEARCH_MAX_CONE_ANGLE = getF(sectfile, s, "SearchMaxConeAngle", 0.01F, 60F);
            searchlightproperties.FOUND_MAX_CONE_ANGLE = getF(sectfile, s, "FoundMaxConeAngle", 0.0F, 20F);
            searchlightproperties.LIGHT_COLOR.x = getF(sectfile, s, "ColorR", 0.0F, 1.0F);
            searchlightproperties.LIGHT_COLOR.y = getF(sectfile, s, "ColorG", 0.0F, 1.0F);
            searchlightproperties.LIGHT_COLOR.z = getF(sectfile, s, "ColorB", 0.0F, 1.0F);
            searchlightproperties.LIGHT_LAND_I = getF(sectfile, s, "LandIntensity", 0.1F, 100F);
            searchlightproperties.LIGHT_LAND_R = getF(sectfile, s, "LandRadius", 1.0F, 300F);
            Property.set(class1, "iconName", "icons/" + getS(sectfile, s, "Icon") + ".mat");
            Property.set(class1, "meshName", searchlightproperties.meshSummer);
            return searchlightproperties;
        }

        public Actor actorSpawn(ActorSpawnArg actorspawnarg) {
            switch (World.cur().camouflage) {
                case 1: // '\001'
                    this.proper.meshName = this.proper.meshWinter;
                    break;

                case 2: // '\002'
                    this.proper.meshName = this.proper.meshDesert;
                    break;

                default:
                    this.proper.meshName = this.proper.meshSummer;
                    break;
            }
            SearchlightGeneric searchlightgeneric = null;
            try {
                SearchlightGeneric.constr_arg1 = this.proper;
                SearchlightGeneric.constr_arg2 = actorspawnarg;
                searchlightgeneric = (SearchlightGeneric) this.cls.newInstance();
                SearchlightGeneric.constr_arg1 = null;
                SearchlightGeneric.constr_arg2 = null;
            } catch (Exception exception) {
                SearchlightGeneric.constr_arg1 = null;
                SearchlightGeneric.constr_arg2 = null;
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                System.out.println("SPAWN: Can't create Searchlight object [class:" + this.cls.getName() + "]");
                return null;
            }
            return searchlightgeneric;
        }

        public Class                 cls;
        public SearchlightProperties proper;

        public SPAWN(Class class1) {
            try {
                String s = class1.getName();
                int i = s.lastIndexOf('.');
                int j = s.lastIndexOf('$');
                if (i < j) {
                    i = j;
                }
                String s1 = s.substring(i + 1);
                this.proper = LoadSearchlightProperties(Statics.getTechnicsFile(), s1, class1);
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
                    case 73: // 'I'
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted);
                        }
                        short word0 = netmsginput.readShort();
                        float f = netmsginput.readFloat();
                        float f2 = netmsginput.readFloat();
                        if (word0 <= 0) {
                            if (SearchlightGeneric.this.dying != 1) {
                                SearchlightGeneric.this.aime.forgetAiming();
                                SearchlightGeneric.this.setGunAngles(f, f2);
                            }
                        } else if (SearchlightGeneric.this.dying != 1) {
                            SearchlightGeneric.this.setGunAngles(f, f2);
                            SearchlightGeneric.this.Die(null, word0, false);
                        }
                        return true;

                    case 82: // 'R'
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted1 = new NetMsgGuaranted(netmsginput, 0);
                            this.post(netmsgguaranted1);
                        }
                        SearchlightGeneric.this.dying = 0;
                        SearchlightGeneric.this.setDiedFlag(false);
                        SearchlightGeneric.this.aime.forgetAiming();
                        SearchlightGeneric.this.activateMesh(true, false);
                        SearchlightGeneric.this.setDefaultLivePose();
                        SearchlightGeneric.this.lastTimeWhenFound = 0L;
                        return true;

                    case 68: // 'D'
                        if (this.isMirrored()) {
                            NetMsgGuaranted netmsgguaranted2 = new NetMsgGuaranted(netmsginput, 1);
                            this.post(netmsgguaranted2);
                        }
                        short word1 = netmsginput.readShort();
                        float f1 = netmsginput.readFloat();
                        float f3 = netmsginput.readFloat();
                        if ((word1 > 0) && (SearchlightGeneric.this.dying != 1)) {
                            SearchlightGeneric.this.setGunAngles(f1, f3);
                            com.maddox.rts.NetObj netobj2 = netmsginput.readNetObj();
                            Actor actor2 = netobj2 != null ? ((ActorNet) netobj2).actor() : null;
                            SearchlightGeneric.this.Die(actor2, word1, true);
                        }
                        return true;
                }
                return false;
            }
            switch (netmsginput.readByte()) {
                default:
                    break;

                case 84: // 'T'
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(false);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                    Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                    SearchlightGeneric.this.Track_Mirror(actor);
                    break;

                case 70: // 'F'
                    if (this.isMirrored()) {
                        this.out.unLockAndSet(netmsginput, 1);
                        this.out.setIncludeTime(true);
                        this.postReal(Message.currentRealTime(), this.out);
                    }
                    com.maddox.rts.NetObj netobj1 = netmsginput.readNetObj();
                    Actor actor1 = netobj1 != null ? ((ActorNet) netobj1).actor() : null;
                    float f4 = netmsginput.readFloat();
                    float f5 = (0.001F * (Message.currentGameTime() - Time.current())) + f4;
                    SearchlightGeneric.this.Fire_Mirror(actor1, f5);
                    break;

                case 68: // 'D'
                    this.out.unLockAndSet(netmsginput, 1);
                    this.out.setIncludeTime(false);
                    this.postRealTo(Message.currentRealTime(), this.masterChannel(), this.out);
                    return true;
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
            if (SearchlightGeneric.this.dying == 1) {
                return true;
            } else {
                com.maddox.rts.NetObj netobj = netmsginput.readNetObj();
                Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                SearchlightGeneric.this.Die(actor, (short) 0, true);
                return true;
            }
        }

        public Master(Actor actor) {
            super(actor);
        }
    }

    class TickGame extends Interpolate {

        public boolean tick() {
            if (SearchlightGeneric.this.dying == 1) {
                if (SearchlightGeneric.this.respawnDelay-- <= 0L) {
                    if (!Mission.isDeathmatch()) {
                        if (SearchlightGeneric.this.aime != null) {
                            SearchlightGeneric.this.aime.forgetAll();
                            SearchlightGeneric.this.aime = null;
                        }
                        return false;
                    }
                    if (!SearchlightGeneric.this.isNetMaster()) {
                        SearchlightGeneric.this.respawnDelay = 10000L;
                        return true;
                    } else {
                        SearchlightGeneric.this.dying = 0;
                        SearchlightGeneric.this.setDiedFlag(false);
                        SearchlightGeneric.this.aime.forgetAiming();
                        SearchlightGeneric.this.activateMesh(true, false);
                        SearchlightGeneric.this.setDefaultLivePose();
                        SearchlightGeneric.this.send_RespawnCommand();
                        SearchlightGeneric.this.lastTimeWhenFound = 0L;
                        return true;
                    }
                } else {
                    return true;
                }
            }
            SearchlightGeneric.this.aime.tick_();
            SearchlightGeneric.this.nightTime = World.Sun().ToSun.z <= SUNZ_MIN_TO_WORK;
            if (SearchlightGeneric.this.lightWantedState) {
                if (SearchlightGeneric.this.nightTime && !SearchlightGeneric.this.lightIsOn) {
                    SearchlightGeneric.this.activateMesh(true, true);
                }
            } else if (SearchlightGeneric.this.lightIsOn) {
                SearchlightGeneric.this.activateMesh(true, false);
            }
            return true;
        }

        TickGame() {
        }
    }

    class TickBuilder extends Interpolate {

        public boolean tick() {
            SearchlightGeneric.this.nightTime = World.Sun().ToSun.z <= SUNZ_MIN_TO_WORK;
            if (SearchlightGeneric.this.lightIsOn) {
                if (!SearchlightGeneric.this.nightTime) {
                    SearchlightGeneric.this.activateMesh(true, false);
                }
            } else if (SearchlightGeneric.this.nightTime) {
                SearchlightGeneric.this.activateMesh(true, true);
            }
            return true;
        }

        TickBuilder() {
        }
    }

    class MyDrawer extends ActorMeshDraw {

        public void killLightMap() {
            if (this.lightMap != null) {
                this.lightMap.clear();
                this.lightMap = null;
            }
        }

        public void pushAngles(float f, float f1) {
            long l = Time.tickNext();
            if (this.interpolateAngles || (this.t1 <= l)) {
                if (this.t1 == l) {
                    this.fa.setDstDeg(f);
                    this.fb.setDstDeg(f1);
                    this.t1 = l;
                } else {
                    this.fa.setDeg(this.fa.getDstDeg(), f);
                    this.fb.setDeg(this.fb.getDstDeg(), f1);
                    this.t0 = this.t1;
                    this.t1 = l;
                }
                this.interpolateAngles = true;
            } else {
                this.t0 = this.t1 = l;
                this.fa.setDeg(f, f);
                this.fb.setDeg(f1, f1);
                this.interpolateAngles = true;
            }
        }

        public int preRender(Actor actor) {
            if (this.interpolateAngles) {
                long l = Time.current();
                float f;
                float f1;
                if ((this.t1 <= this.t0) || (l >= this.t1)) {
                    f = this.fa.getDstDeg();
                    f1 = this.fb.getDstDeg();
                } else if (l <= this.t0) {
                    f = this.fa.getSrcDeg();
                    f1 = this.fb.getSrcDeg();
                } else {
                    float f2 = (float) (l - this.t0) / (float) (this.t1 - this.t0);
                    f = this.fa.getDeg(f2);
                    f1 = this.fb.getDeg(f2);
                }
                ((ActorHMesh) actor).hierMesh().chunkSetAngles("Head", f, 0.0F, 0.0F);
                ((ActorHMesh) actor).hierMesh().chunkSetAngles("Gun", f1, 0.0F, 0.0F);
            }
            if (SearchlightGeneric.this.lightIsOn) {
                ((ActorHMesh) actor).hierMesh().setCurChunk("Ray_ON");
                ((ActorHMesh) actor).hierMesh().getChunkLTM(SearchlightGeneric.locLmatr);
                SearchlightGeneric.locLpos.set(10D, 0.0D, 0.0D);
                SearchlightGeneric.locLmatr.transform(SearchlightGeneric.locLpos);
                SearchlightGeneric.this.landLight.relPos.set(SearchlightGeneric.locLpos);
            }
            return super.preRender(actor);
        }

        long           t0;
        long           t1;
        AnglesFork     fa;
        AnglesFork     fb;
        public boolean interpolateAngles;

        MyDrawer() {
            this.t0 = 0L;
            this.t1 = 0L;
            this.fa = new AnglesFork();
            this.fb = new AnglesFork();
            this.interpolateAngles = false;
        }
    }

    public static class SearchlightProperties {

        public String         meshName;
        public String         meshDeadName;
        public String         meshSummer;
        public String         meshDesert;
        public String         meshWinter;
        public String         meshDeadSummer;
        public String         meshDeadDesert;
        public String         meshDeadWinter;
        public TableFunction2 fnShotPanzer;
        public TableFunction2 fnExplodePanzer;
        public float          PANZER;
        public float          PANZER_TNT_TYPE;
        public String         explodeName;
        public int            HITBY_MASK;
        public AnglesRange    HEAD_YAW_RANGE;
        public float          GUN_MIN_PITCH;
        public float          GUN_STD_PITCH;
        public float          GUN_MAX_PITCH;
        public float          HEAD_MAX_YAW_SPEED;
        public float          GUN_MAX_PITCH_SPEED;
        public float          SEARCH_MAX_CONE_ANGLE;
        public float          FOUND_MAX_CONE_ANGLE;
        public Point3f        LIGHT_COLOR;
        public float          LIGHT_LAND_I;
        public float          LIGHT_LAND_R;
        public double         Hclear;
        public double         H;
        public double         R0;
        public double         R1;
        public double         TANGA;

        public SearchlightProperties() {
            this.meshName = null;
            this.meshDeadName = null;
            this.meshSummer = null;
            this.meshDesert = null;
            this.meshWinter = null;
            this.meshDeadSummer = null;
            this.meshDeadDesert = null;
            this.meshDeadWinter = null;
            this.fnShotPanzer = null;
            this.fnExplodePanzer = null;
            this.PANZER = 0.001F;
            this.PANZER_TNT_TYPE = 1.0F;
            this.explodeName = null;
            this.HITBY_MASK = -1;
            this.HEAD_YAW_RANGE = new AnglesRange(-1F, 1.0F);
            this.GUN_MIN_PITCH = 30F;
            this.GUN_STD_PITCH = 90F;
            this.GUN_MAX_PITCH = 90F;
            this.HEAD_MAX_YAW_SPEED = 720F;
            this.GUN_MAX_PITCH_SPEED = 60F;
            this.SEARCH_MAX_CONE_ANGLE = 30F;
            this.FOUND_MAX_CONE_ANGLE = 0.2F;
            this.LIGHT_COLOR = new Point3f(1.0F, 1.0F, 1.0F);
            this.LIGHT_LAND_I = 3F;
            this.LIGHT_LAND_R = 3F;
            this.Hclear = 0.0D;
            this.H = 0.0D;
            this.R0 = 1.0D;
            this.R1 = 2D;
            this.TANGA = 1.0D;
        }
    }

    public static void resetGame() {
        hashmap_ON.clear();
        hashmap_ALL.clear();
        hashmap_ALL_is_changed = true;
        someObjectWasLightedInPreviousCall = true;
        next_entry_get = null;
    }

    private static void register_ONOFF(SearchlightGeneric searchlightgeneric, boolean flag) {
        if (flag) {
            hashmap_ON.put(searchlightgeneric, searchlightgeneric);
        } else {
            hashmap_ON.remove(searchlightgeneric);
        }
        if (!hashmap_ALL.containsKey(searchlightgeneric)) {
            hashmap_ALL.put(searchlightgeneric, searchlightgeneric);
            hashmap_ALL_is_changed = true;
        }
    }

    public static int possibleGlare() {
        if (!hashmap_ALL.isEmpty() && (World.Sun().ToSun.z <= SUNZ_MIN_TO_WORK)) {
            if (hashmap_ALL_is_changed) {
                hashmap_ALL_is_changed = false;
                return 2;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    public static int numlightsGlare() {
        return hashmap_ALL.size();
    }

    private static float computeAngleFromCam(Point3d point3d) {
        targRayDir.sub(point3d, nosePos);
        double d = targRayDir.length();
        if (d < 0.001D) {
            return -1F;
        }
        targRayDir.scale(1.0D / d);
        double d1 = noseDir.dot(targRayDir);
        if (d1 < 0.01D) {
            return -1F;
        } else {
            return Geom.RAD2DEG((float) Math.acos(d1));
        }
    }

    public static boolean computeGlare(LightsGlare lightsglare, Point3d point3d) {
        Main3D.cur3D()._camera3D[Main3D.cur3D().getRenderIndx()].pos.getRender(Cam2WorldLoc);
        Cam2WorldLoc.get(nosePos);
        noseDir.set(1.0D, 0.0D, 0.0D);
        Cam2WorldLoc.transform(noseDir);
        java.util.Map.Entry entry = hashmap_ALL.nextEntry(null);
//        LightsGlare _tmp = lightsglare;
        int i = hashmap_ALL.size() * 3;
        for (int j = 0; j < i; j += 3) {
            SearchlightGeneric searchlightgeneric = (SearchlightGeneric) entry.getKey();
            entry = hashmap_ALL.nextEntry(entry);
            if (!searchlightgeneric.lightIsOn) {
                float f = computeAngleFromCam(searchlightgeneric.B);
                lightsglare.glareData[j] = f < 0.0F ? -1F : 0.0F;
                lightsglare.glareData[j + 1] = f;
            } else {
                double d = searchlightgeneric.L.dot(nosePos) - searchlightgeneric.L.dot(searchlightgeneric.B);
                if ((d <= 0.0D) || (d >= searchlightgeneric.prop.H)) {
                    float f1 = computeAngleFromCam(searchlightgeneric.B);
                    lightsglare.glareData[j] = f1 < 0.0F ? -1F : 0.0F;
                    lightsglare.glareData[j + 1] = f1;
                } else {
                    locLpos.scaleAdd(d, searchlightgeneric.L, searchlightgeneric.B);
                    d /= searchlightgeneric.prop.H;
                    double d1 = searchlightgeneric.prop.R0 + ((searchlightgeneric.prop.R1 - searchlightgeneric.prop.R0) * d);
                    tmpv.sub(nosePos, locLpos);
                    if (tmpv.lengthSquared() >= (d1 * d1)) {
                        float f2 = computeAngleFromCam(searchlightgeneric.B);
                        lightsglare.glareData[j] = f2 < 0.0F ? -1F : 0.0F;
                        lightsglare.glareData[j + 1] = f2;
                    } else {
                        double d2 = (1.0D - d) * searchlightgeneric.prop.LIGHT_LAND_I;
                        if (d2 <= 0.0D) {
                            float f3 = computeAngleFromCam(searchlightgeneric.B);
                            lightsglare.glareData[j] = f3 < 0.0F ? -1F : 0.0F;
                            lightsglare.glareData[j + 1] = f3;
                        } else {
                            float f4 = lightsglare.computeFlash(searchlightgeneric, searchlightgeneric.B, accumColor);
                            if (f4 > 0.0F) {
                                f4 = (float) (f4 * d2);
                            }
                            lightsglare.glareData[j] = f4;
                            lightsglare.glareData[j + 1] = accumColor.x;
                        }
                    }
                }
            }
//            LightsGlare _tmp1 = lightsglare;
        }

        return true;
    }

    public static void getnextposandcolorGlare(Point3d point3d, Point3f point3f) {
        if (point3d == null) {
            next_entry_get = hashmap_ALL.nextEntry(null);
            return;
        } else {
            SearchlightGeneric searchlightgeneric = (SearchlightGeneric) next_entry_get.getKey();
            next_entry_get = hashmap_ALL.nextEntry(next_entry_get);
            point3d.set(searchlightgeneric.B);
            point3f.set(searchlightgeneric.prop.LIGHT_COLOR);
            return;
        }
    }

    private static void clearSearchlightSourcesInObjects() {
        List list = Engine.targets();
        int i = list.size();
        for (int j = 0; j < i; j++) {
            Actor actor = (Actor) list.get(j);
            if ((!(actor instanceof Aircraft) && !(actor instanceof Balloon)) || (actor.draw == null)) {
                continue;
            }
            HashMapExt hashmapext = actor.draw.lightMap();
            if (hashmapext == null) {
                continue;
            }
            LightPointActor lightpointactor = (LightPointActor) hashmapext.remove("SL");
            if (lightpointactor != null) {
                lightpointactor.destroy();
            }
        }

    }

    public static void lightPlanesBySearchlights() {
        if (hashmap_ON.isEmpty()) {
            if (someObjectWasLightedInPreviousCall) {
                clearSearchlightSourcesInObjects();
                someObjectWasLightedInPreviousCall = false;
            }
            return;
        }
        List list = Engine.targets();
        int i = list.size();
        for (int j = 0; j < i; j++) {
            Actor actor = (Actor) list.get(j);
            if (!(actor instanceof Aircraft) && !(actor instanceof Balloon)) {
                continue;
            }
            actor.pos.getRender(planeP);
            double d = 0.0D;
            if ((planeP.z - World.land().HQ(planeP.x, planeP.y)) > 10D) {
//                boolean flag = false;
                accumColor.set(0.0F, 0.0F, 0.0F);
                accumDir.set(0.0D, 0.0D, 0.0D);
                for (java.util.Map.Entry entry = hashmap_ON.nextEntry(null); entry != null;) {
                    SearchlightGeneric searchlightgeneric = (SearchlightGeneric) entry.getKey();
                    double d1 = searchlightgeneric.L.dot(planeP) - searchlightgeneric.L.dot(searchlightgeneric.B);
                    if ((d1 <= 0.0D) || (d1 >= searchlightgeneric.prop.H)) {
                        entry = hashmap_ON.nextEntry(entry);
                    } else {
                        locLpos.scaleAdd(d1, searchlightgeneric.L, searchlightgeneric.B);
                        d1 /= searchlightgeneric.prop.H;
                        double d2 = searchlightgeneric.prop.R0 + ((searchlightgeneric.prop.R1 - searchlightgeneric.prop.R0) * d1);
                        double d3 = d2 + (actor.collisionR() * 0.75D);
                        tmpv.sub(planeP, locLpos);
                        double d4 = tmpv.lengthSquared();
                        if (d4 >= (d3 * d3)) {
                            entry = hashmap_ON.nextEntry(entry);
                        } else if ((d3 - d2) <= 0.0D) {
                            entry = hashmap_ON.nextEntry(entry);
                        } else {
                            d4 = Math.sqrt(d4);
                            double d5 = 1.0D - ((d4 - d2) / (d3 - d2));
                            if (d5 >= 1.0D) {
                                d5 = 1.0D;
                            }
                            double d6 = (1.0D - d1) * searchlightgeneric.prop.LIGHT_LAND_I;
                            d6 *= d5;
                            if (d6 <= 0.0D) {
                                entry = hashmap_ON.nextEntry(entry);
                            } else {
                                d += d6;
                                accumColor.scaleAdd((float) d6, searchlightgeneric.prop.LIGHT_COLOR);
                                accumDir.scaleAdd(d6, searchlightgeneric.L);
                                entry = hashmap_ON.nextEntry(entry);
                            }
                        }
                    }
                }

            }
            if (actor.draw == null) {
                continue;
            }
            HashMapExt hashmapext = actor.draw.lightMap();
            if (hashmapext == null) {
                continue;
            }
            LightPointActor lightpointactor = (LightPointActor) hashmapext.get("SL");
            if (d <= 0.0D) {
                if (lightpointactor != null) {
                    lightpointactor.light.setEmit(0.0F, 0.0F);
                }
                continue;
            }
            someObjectWasLightedInPreviousCall = true;
            if (lightpointactor == null) {
                lightpointactor = new LightPointActor(new LightPoint());
                hashmapext.put("SL", lightpointactor);
            }
            accumDir.normalize();
            accumDir.negate();
            float f = 100123F;
            accumDir.scale(1000D);
            actor.pos.getRender(locLloc);
            locLloc.transformInv(accumDir);
            float f1 = Math.max(accumColor.x, Math.max(accumColor.y, accumColor.z));
            accumColor.scale(1.0F / f1);
            if (d >= 1.3500000238418579D) {
                d = 1.3500000238418579D;
            }
            lightpointactor.light.setEmit((float) d, f);
            lightpointactor.light.setColor(accumColor.x, accumColor.y, accumColor.z);
            lightpointactor.relPos.set(accumDir);
        }

    }

    public static void lightCloudsBySearchlights() {
        if (hashmap_ON.isEmpty()) {
            return;
        }
        int i = Mission.curCloudsType();
        if (i <= 0) {
            return;
        }
        double d = Mission.curCloudsHeight();
        java.util.Map.Entry entry = hashmap_ON.nextEntry(null);
        do {
            if (entry == null) {
                break;
            }
            SearchlightGeneric searchlightgeneric = (SearchlightGeneric) entry.getKey();
            entry = hashmap_ON.nextEntry(entry);
            if (searchlightgeneric.L.z > 0.01D) {
                double d1 = (d - searchlightgeneric.B.z) / searchlightgeneric.L.z;
                if ((d1 >= 0.0D) && (d1 < searchlightgeneric.prop.H)) {
                    p3d.scaleAdd(d1, searchlightgeneric.L, searchlightgeneric.B);
                    d1 /= searchlightgeneric.prop.H;
                    double d2 = searchlightgeneric.prop.R0 + ((searchlightgeneric.prop.R1 - searchlightgeneric.prop.R0) * d1);
                    d2 *= 5D;
                    if (d2 <= 100D) {
                        d2 = 100D;
                    }
                    if (d2 >= 400D) {
                        d2 = 400D;
                    }
                    double d3 = 3F * searchlightgeneric.prop.LIGHT_LAND_I;
                    d3 *= 1.0D - d1;
                    if (d3 > 0.0D) {
                        searchlightgeneric.cloudLight.setPos(p3d);
                        searchlightgeneric.cloudLight.setEmit((float) d3, (float) d2);
                        searchlightgeneric.cloudLight.addToRender();
                    }
                }
            }
        } while (true);
    }

    public static final double Rnd(double d, double d1) {
        return TrueRandom.nextDouble(d, d1);
    }

    public static final float Rnd(float f, float f1) {
        return TrueRandom.nextFloat(f, f1);
    }

    private boolean RndB(float f) {
        return TrueRandom.nextFloat(0.0F, 1.0F) < f;
    }

    private static final long SecsToTicks(float f) {
        long l = (long) (0.5D + (f / Time.tickLenFs()));
        return l >= 1L ? l : 1L;
    }

    protected final boolean Head360() {
        return this.prop.HEAD_YAW_RANGE.fullcircle();
    }

    private void activateMesh(boolean flag, boolean flag1) {
        if (!flag) {
            this.hierMesh().chunkVisible("Body", false);
            this.hierMesh().chunkVisible("Head", false);
            this.hierMesh().chunkVisible("Gun", false);
            this.hierMesh().chunkVisible("Ray_ON", false);
            this.hierMesh().chunkVisible("Ray_OFF", false);
            this.hierMesh().chunkVisible("Dead", true);
            this.lightIsOn = false;
            this.lightWantedState = false;
            if (this.landLight != null) {
                this.landLight.destroy();
                this.landLight = null;
            }
            ((MyDrawer) this.draw).killLightMap();
            register_ONOFF(this, false);
            return;
        }
        this.hierMesh().chunkVisible("Body", true);
        this.hierMesh().chunkVisible("Head", true);
        this.hierMesh().chunkVisible("Gun", true);
        this.hierMesh().chunkVisible("Ray_ON", flag1);
        this.hierMesh().chunkVisible("Ray_OFF", !flag1);
        this.hierMesh().chunkVisible("Dead", false);
        this.lightIsOn = flag1;
        this.lightWantedState = flag1;
        if (this.landLight != null) {
            this.landLight.destroy();
            this.landLight = null;
        }
        ((MyDrawer) this.draw).killLightMap();
        if (flag1) {
            this.landLight = new LightPointActor(new LightPointWorld(), new Point3d(0.0D, 0.0D, 0.0D));
            this.landLight.light.setColor(this.prop.LIGHT_COLOR.x, this.prop.LIGHT_COLOR.y, this.prop.LIGHT_COLOR.z);
            this.landLight.light.setEmit(this.prop.LIGHT_LAND_I, this.prop.LIGHT_LAND_R);
            this.draw.lightMap().put("light", this.landLight);
            this.cloudLight.setColor(this.prop.LIGHT_COLOR.x, this.prop.LIGHT_COLOR.y, this.prop.LIGHT_COLOR.z);
        }
        register_ONOFF(this, flag1);
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[]) {
        if ((actor instanceof ActorMesh) && ((ActorMesh) actor).isStaticPos()) {
            aflag[0] = false;
            return;
        } else {
            return;
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
        if (shot.powerType == 1) {
            if (this.RndB(0.15F)) {
                return;
            } else {
                this.Die(shot.initiator, (short) 0, true);
                return;
            }
        }
        float f = this.prop.PANZER;
        f *= Rnd(0.93F, 1.07F);
        float f1 = this.prop.fnShotPanzer.Value(shot.power, f);
        if ((f1 < 1000F) && ((f1 <= 1.0F) || this.RndB(1.0F / f1))) {
            this.Die(shot.initiator, (short) 0, true);
        }
    }

    public void msgExplosion(Explosion explosion) {
        if (this.dying != 0) {
            return;
        }
        if (this.isNetMirror() && explosion.isMirage()) {
            return;
        }
        if (explosion.power <= 0.0F) {
            return;
        }
//        Explosion _tmp = explosion;
        if (explosion.powerType == 1) {
            if (TankGeneric.splintersKill(explosion, this.prop.fnShotPanzer, 0.5F, 0.5F, this, 0.7F, 0.25F, this.prop.PANZER, this.prop.PANZER, this.prop.PANZER, this.prop.PANZER, this.prop.PANZER, this.prop.PANZER)) {
                this.Die(explosion.initiator, (short) 0, true);
            }
            return;
        }
//        Explosion _tmp1 = explosion;
        if ((explosion.powerType == 2) && (explosion.chunkName != null)) {
            this.Die(explosion.initiator, (short) 0, true);
            return;
        }
        float f;
        if (explosion.chunkName != null) {
            f = 0.5F * explosion.power;
        } else {
            f = explosion.receivedTNTpower(this);
        }
        f *= Rnd(0.95F, 1.05F);
        float f1 = this.prop.fnExplodePanzer.Value(f, this.prop.PANZER_TNT_TYPE);
        if ((f1 < 1000F) && ((f1 <= 1.0F) || this.RndB(1.0F / f1))) {
            this.Die(explosion.initiator, (short) 0, true);
        }
    }

    private void ShowExplode(float f) {
        if (f > 0.0F) {
            f = Rnd(f, f * 1.6F);
        }
        Explosions.runByName(this.prop.explodeName, this, "", "", f);
    }

    private float[] computeDeathPose(short word0) {
//        RangeRandom rangerandom = new RangeRandom(word0); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        float af[] = new float[10];
        af[0] = this.headYaw + TrueRandom.nextFloat(-15F, 15F);
        af[1] = (TrueRandom.nextInt(2) == 1 ? 1.0F : -1F) * TrueRandom.nextFloat(4F, 9F); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        af[2] = (TrueRandom.nextInt(2) == 1 ? 1.0F : -1F) * TrueRandom.nextFloat(4F, 9F); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        af[3] = -this.gunPitch + TrueRandom.nextFloat(-15F, 15F);
        af[4] = (TrueRandom.nextInt(2) == 1 ? 1.0F : -1F) * TrueRandom.nextFloat(2.0F, 5F); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        af[5] = (TrueRandom.nextInt(2) == 1 ? 1.0F : -1F) * TrueRandom.nextFloat(5F, 9F); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        af[6] = 0.0F;
        af[7] = (TrueRandom.nextInt(2) == 1 ? 1.0F : -1F) * TrueRandom.nextFloat(4F, 8F); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        af[8] = (TrueRandom.nextInt(2) == 1 ? 1.0F : -1F) * TrueRandom.nextFloat(7F, 12F); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        af[9] = this.heightAboveLandSurface - TrueRandom.nextFloat(0.0F, 0.25F); // TODO: Fixed by SAS~Storebror: Don't create new Random Classes all the time!!!
        return af;
    }

    private void Die(Actor actor, short word0, boolean flag) {
        if (this.dying != 0) {
            return;
        }
        if (word0 <= 0) {
            if (this.isNetMirror()) {
                this.send_DeathRequest(actor);
                return;
            }
            word0 = (short) (int) Rnd(1.0F, 30000F);
        }
        this.deathSeed = word0;
        this.dying = 1;
        World.onActorDied(this, actor);
        if (this.aime != null) {
            this.aime.forgetAiming();
        }
        float af[] = this.computeDeathPose(word0);
        this.hierMesh().chunkSetAngles("Head", af[0], af[1], af[2]);
        this.hierMesh().chunkSetAngles("Gun", af[3], af[4], af[5]);
        this.hierMesh().chunkSetAngles("Body", af[6], af[7], af[8]);
        this.heightAboveLandSurface = af[9];
        ((MyDrawer) this.draw).interpolateAngles = false;
        this.Align();
        this.activateMesh(false, false);
        if (flag) {
            this.ShowExplode(14F);
        }
        if (flag) {
            this.send_DeathCommand(actor);
        }
    }

    private void setGunAngles(float f, float f1) {
        this.headYaw = f;
        this.gunPitch = f1;
        this.hierMesh().chunkSetAngles("Head", this.headYaw, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Gun", -this.gunPitch, 0.0F, 0.0F);
        this.hierMesh().chunkSetAngles("Body", 0.0F, 0.0F, 0.0F);
        ((MyDrawer) this.draw).pushAngles(this.headYaw, -this.gunPitch);
        this.hierMesh().setCurChunk("Ray_ON");
        this.hierMesh().getChunkLocObj(locLloc);
        locLloc.add(this.pos.getAbs());
        this.L.set(1.0D, 0.0D, 0.0D);
        locLloc.transform(this.L);
        this.L.normalize();
        locLloc.get(this.B);
    }

    public void destroy() {
        if (this.isDestroyed()) {
            return;
        }
        if (this.aime != null) {
            this.aime.forgetAll();
            this.aime = null;
        }
        super.destroy();
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public boolean isStaticPos() {
        return false;
    }

    private void setDefaultLivePose() {
        Matrix4d matrix4d = new Matrix4d();
        this.heightAboveLandSurface = 0.0F;
        int i = this.hierMesh().hookFind("Ground_Level");
        if (i != -1) {
            this.hierMesh().hookMatrix(i, matrix4d);
            this.heightAboveLandSurface = (float) (-matrix4d.m23);
        }
        Point3d point3d = new Point3d();
        this.hierMesh().hookMatrix(this.hierMesh().hookFind("ConeNear"), matrix4d);
        point3d.x = matrix4d.m03;
        point3d.y = matrix4d.m13;
        point3d.z = matrix4d.m23;
        Point3d point3d1 = new Point3d();
        this.hierMesh().hookMatrix(this.hierMesh().hookFind("ConeFar"), matrix4d);
        point3d1.x = matrix4d.m03;
        point3d1.y = matrix4d.m13;
        point3d1.z = matrix4d.m23;
        this.hierMesh().setCurChunk("Ray_ON");
        this.hierMesh().getChunkLocObj(locLloc);
        locLloc.transformInv(point3d);
        locLloc.transformInv(point3d1);
        if (Math.abs(point3d.x) > 0.10000000000000001D) {
            System.out.println("**** Wrong position or orientation of ConeNear");
        }
        this.prop.R0 = Math.sqrt((point3d.y * point3d.y) + (point3d.z * point3d.z));
        this.prop.R1 = Math.sqrt((point3d1.y * point3d1.y) + (point3d1.z * point3d1.z));
        if (((point3d1.x - point3d.x) < 0.10000000000000001D) || (this.prop.R1 < this.prop.R0)) {
            System.out.println("**** Wrong position or orientation of ConeFar");
        }
        this.prop.TANGA = (this.prop.R1 - this.prop.R0) / (point3d1.x - point3d.x);
        float f = 1.0F;
        int j = Mission.curCloudsType();
        switch (j) {
            case 1: // '\001'
                f = 1.0F;
                break;

            case 2: // '\002'
                f = 0.6F;
                break;

            case 3: // '\003'
                f = 0.3F;
                break;

            case 4: // '\004'
                f = 0.12F;
                break;

            case 5: // '\005'
                f = 0.12F;
                break;

            case 6: // '\006'
                f = 0.12F;
                break;
        }
        this.prop.H = this.prop.Hclear * f;
        this.prop.R1 = this.prop.R0 + (this.prop.H * this.prop.TANGA);
        this.setGunAngles(0.0F, this.prop.GUN_STD_PITCH);
        ((MyDrawer) this.draw).interpolateAngles = false;
        this.Align();
    }

    protected SearchlightGeneric() {
        this(constr_arg1, constr_arg2);
    }

    private SearchlightGeneric(SearchlightProperties searchlightproperties, ActorSpawnArg actorspawnarg) {
        super(searchlightproperties.meshName);
        this.prop = null;
        this.cloudLight = new LightPoint();
        this.dying = 0;
        this.respawnDelay = 0L;
        this.B = new Point3d();
        this.L = new Vector3d();
        this.outCommand = new NetMsgFiltered();
        this.prop = searchlightproperties;
        actorspawnarg.setStationary(this);
        this.collide(true);
        this.drawing(true);
        this.draw = new MyDrawer();
        this.lastTimeWhenFound = 0L;
        this.smoothMove = false;
        this.createNetObject(actorspawnarg.netChannel, actorspawnarg.netIdRemote);
        this.startDelay = 0L;
        if (actorspawnarg.timeLenExist) {
            this.startDelay = (long) ((actorspawnarg.timeLen * 60F * 1000F) + 0.5F);
            if (this.startDelay < 0L) {
                this.startDelay = 0L;
            }
        }
        this.headYaw = 0.0F;
        this.gunPitch = 0.0F;
        this.activateMesh(true, false);
        this.setDefaultLivePose();
        if (Time.isRealOnly()) {
            this.flags |= 0x2000;
            if (!this.interpEnd("tick_builder")) {
                this.aime = null;
                this.interpPut(new TickBuilder(), "tick_builder", Time.currentReal(), null);
            }
        } else {
            this.startMove();
        }
    }

    private void Align() {
        this.pos.getAbs(p3d);
        p3d.z = Engine.land().HQ(p3d.x, p3d.y) + this.heightAboveLandSurface;
        o.setYPR(this.pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
        this.pos.setAbs(p3d, o);
    }

    public void align() {
        this.Align();
    }

    public void startMove() {
        if (!this.interpEnd("tick_game")) {
            if (this.aime != null) {
                this.aime.forgetAll();
                this.aime = null;
            }
            this.aime = new Aim(this, this.isNetMirror());
            this.interpPut(new TickGame(), "tick_game", Time.current(), null);
        }
    }

    public int WeaponsMask() {
        return -1;
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
        if (abulletproperties[0].powerType == 1) {
            return 0;
        }
        if (abulletproperties[1].powerType == 1) {
            return 1;
        }
        return abulletproperties[0].powerType != 2 ? 0 : 1;
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
        return (float) this.prop.H;
    }

    public boolean unmovableInFuture() {
        return true;
    }

    public void collisionDeath() {
        if (this.isNet()) {
            return;
        } else {
            this.ShowExplode(-1F);
            this.destroy();
            return;
        }
    }

    public float futurePosition(float f, Point3d point3d) {
        this.pos.getAbs(point3d);
        return f > 0.0F ? f : 0.0F;
    }

    private void send_DeathCommand(Actor actor) {
        if (!this.isNetMaster()) {
            return;
        }
        if (Mission.isDeathmatch()) {
            float f = Mission.respawnTime("Searchlight");
            this.respawnDelay = SecsToTicks(Rnd(f, f * 1.2F));
        } else {
            this.respawnDelay = 0L;
        }
        NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
        try {
            netmsgguaranted.writeByte(68);
            netmsgguaranted.writeShort(this.deathSeed);
            netmsgguaranted.writeFloat(this.headYaw);
            netmsgguaranted.writeFloat(this.gunPitch);
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

    private void send_FireCommand(Actor actor, float f) {
        if (!this.isNetMaster()) {
            return;
        }
        if (!this.net.isMirrored()) {
            return;
        }
        if (!Actor.isValid(actor) || !actor.isNet()) {
            return;
        }
        if (f < 0.0F) {
            try {
                this.outCommand.unLockAndClear();
                this.outCommand.writeByte(84);
                this.outCommand.writeNetObj(actor.net);
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
                this.outCommand.writeFloat(f);
                this.outCommand.writeNetObj(actor.net);
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
            netmsgguaranted.writeShort(0);
        } else {
            netmsgguaranted.writeShort(this.deathSeed);
        }
        netmsgguaranted.writeFloat(this.headYaw);
        netmsgguaranted.writeFloat(this.gunPitch);
        this.net.postTo(netchannel, netmsgguaranted);
    }

    public float getReloadingTime(Aim aim) {
        return 0.0F;
    }

    public float chainFireTime(Aim aim) {
        return 3F;
    }

    public float probabKeepSameEnemy(Actor actor) {
        return (Time.current() - this.lastTimeWhenFound) > TM_TO_REMEMBER_BAD_TARGET ? 0.0F : 1.0F;
    }

    public float minTimeRelaxAfterFight() {
        return 0.0F;
    }

    public void gunStartParking(Aim aim) {
        this.lightWantedState = false;
        aim.setRotationForParking(this.headYaw, this.gunPitch, 0.0F, this.prop.GUN_STD_PITCH, this.prop.HEAD_YAW_RANGE, this.prop.HEAD_MAX_YAW_SPEED, this.prop.GUN_MAX_PITCH_SPEED);
    }

    public void gunInMove(boolean flag, Aim aim) {
        float f = aim.t();
        if (!flag && this.smoothMove) {
            f = 0.5F * (1.0F + Geom.sinDeg((f * 180F) - 90F));
        }
        this.lightWantedState = !flag;
        float f1 = aim.anglesYaw.getDeg(f);
        float f2 = aim.anglesPitch.getDeg(f);
        this.setGunAngles(f1, f2);
        this.pos.inValidate(false);
        Actor actor = aim.getEnemy();
        if (Actor.isValid(actor) && actor.isAlive() && (actor.getArmy() != 0)) {
            Point3d point3d = new Point3d();
            actor.pos.getAbs(point3d);
            Point3d point3d1 = new Point3d();
            this.pos.getAbs(point3d1);
            Vector3d vector3d = new Vector3d();
            vector3d.sub(point3d, point3d1);
            Orient orient = new Orient();
            orient.setYPR(this.pos.getAbsOrient().getYaw() + f1, f2, 0.0F);
            Vector3d vector3d1 = new Vector3d(1.0D, 0.0D, 0.0D);
            orient.transform(vector3d1);
            double d = vector3d.length();
            double d1 = vector3d1.dot(vector3d);
            if (d1 > 0.0D) {
                double d2 = Math.sqrt((d * d) - (d1 * d1));
                float f3 = (float) (this.prop.R0 + (this.prop.TANGA * d));
                float f4 = actor.collisionR();
                float f5 = (float) (d2 - f4 - f3);
                if (f5 <= 0.0F) {
                    this.lastTimeWhenFound = Time.current();
                    ((Aircraft) actor).tmSearchlighted = this.lastTimeWhenFound;
                }
            }
        }
    }

    public Actor findEnemy(Aim aim) {
        if (!this.nightTime || (this.dying == 1)) {
            return null;
        }
        if (this.isNetMirror()) {
            return null;
        }
        if (Time.current() < this.startDelay) {
            return null;
        } else {
            this.lastTimeWhenFound = Time.current();
            Actor actor = null;
            NearestEnemies.set(this.WeaponsMask());
            actor = NearestEnemies.getAFoundFlyingPlane(this.pos.getAbsPoint(), this.AttackMaxDistance(), this.getArmy(), 250F);
            return actor;
        }
    }

    public boolean enterToFireMode(int i, Actor actor, float f, Aim aim) {
        if (!this.isNetMirror()) {
            this.send_FireCommand(actor, i != 0 ? f : -1F);
        }
        this.lightWantedState = true;
        return true;
    }

    private void Track_Mirror(Actor actor) {
        if (this.dying == 1) {
            return;
        }
        if (actor == null) {
            return;
        }
        if (this.aime == null) {
            return;
        } else {
            this.lastTimeWhenFound = Time.current();
            this.lightWantedState = true;
            this.aime.passive_StartFiring(0, actor, 0, 0.0F);
            return;
        }
    }

    private void Fire_Mirror(Actor actor, float f) {
        if (this.dying == 1) {
            return;
        }
        if (actor == null) {
            return;
        }
        if (this.aime == null) {
            return;
        }
        if (f <= 0.2F) {
            f = 0.2F;
        }
        if (f >= 15F) {
            f = 15F;
        }
        this.lastTimeWhenFound = Time.current();
        this.lightWantedState = true;
        this.aime.passive_StartFiring(1, actor, 0, f);
    }

    public int targetGun(Aim aim, Actor actor, float f, boolean flag) {
        flag = false;
        this.smoothMove = false;
        if (!this.nightTime || (this.dying == 1)) {
            return 0;
        }
        if (!Actor.isValid(actor) || !actor.isAlive() || (actor.getArmy() == 0)) {
            return 0;
        }
        this.lightWantedState = true;
        float f1 = f * Rnd(0.8F, 1.2F);
        Point3d point3d = new Point3d();
        actor.futurePosition(f1, point3d);
        Point3d point3d1 = new Point3d();
        this.pos.getAbs(point3d1);
        point3d1.z += 2D;
        Vector3d vector3d = new Vector3d();
        vector3d.sub(point3d, point3d1);
        vector3d.normalize();
        Orient orient = new Orient();
        orient.setAT0(vector3d);
        float f3 = ((Aircraft) actor).tmSearchlighted;
        float f2;
        if (f3 == 0L) {
            f2 = this.prop.SEARCH_MAX_CONE_ANGLE / 2.0F;
            this.smoothMove = true;
        } else if ((Time.current() - f3) > TM_LOOSE_IN_DARK) {
            float f5 = (Time.current() - f3) / 1000F;
            if (f5 >= 2.0F) {
                f5 = 2.0F;
                this.smoothMove = true;
            }
            f2 = (this.prop.SEARCH_MAX_CONE_ANGLE / 4F) * f5;
        } else {
            f2 = this.prop.FOUND_MAX_CONE_ANGLE / 2.0F;
        }
        orient.increment(Rnd(-1F, 1.0F) * f2, Rnd(-1F, 1.0F) * f2, 0.0F);
        vector3d.set(1.0D, 0.0D, 0.0D);
        orient.transform(vector3d);
        float f4;
        if (flag) {
            f3 = 99999F;
            f4 = 99999F;
        } else {
            f3 = this.prop.HEAD_MAX_YAW_SPEED;
            f4 = this.prop.GUN_MAX_PITCH_SPEED;
        }
        int i = aim.setRotationForTargeting(this, this.pos.getAbs().getOrient(), point3d1, this.headYaw, this.gunPitch, vector3d, 0.0F, f1, this.prop.HEAD_YAW_RANGE, this.prop.GUN_MIN_PITCH, this.prop.GUN_MAX_PITCH, f3, f4, 0.0F);
        if ((i == 2) && ((Time.current() - this.lastTimeWhenFound) > TM_TO_REMEMBER_BAD_TARGET)) {
            aim.anglesYaw.setDeg(this.headYaw);
            aim.anglesPitch.setDeg(this.gunPitch);
            return 0;
        } else {
            return i;
        }
    }

    public void singleShot(Aim aim) {
    }

    public void startFire(Aim aim) {
    }

    public void continueFire(Aim aim) {
    }

    public void stopFire(Aim aim) {
    }

    private static final int             TM_LOOSE_IN_DARK                   = 1000;
    private static final int             TM_TO_REMEMBER_BAD_TARGET          = 22000;
    private static final float           SUNZ_MIN_TO_WORK                   = -0.22F;
    private SearchlightProperties        prop;
    private float                        heightAboveLandSurface;
    private Aim                          aime;
    private float                        headYaw;
    private float                        gunPitch;
    private long                         startDelay;
    private long                         lastTimeWhenFound;
    private boolean                      smoothMove;
    private boolean                      lightWantedState;
    private boolean                      lightIsOn;
    private boolean                      nightTime;
    private LightPointActor              landLight;
    private LightPoint                   cloudLight;
    private int                          dying;
    static final int                     DYING_NONE                         = 0;
    static final int                     DYING_DEAD                         = 1;
    private short                        deathSeed;
    private long                         respawnDelay;
    private Point3d                      B;
    private Vector3d                     L;
    private static SearchlightProperties constr_arg1                        = null;
    private static ActorSpawnArg         constr_arg2                        = null;
    private static Point3d               p3d                                = new Point3d();
    private static Orient                o                                  = new Orient();
//    private static Vector3f n = new Vector3f();
    private static Vector3d              tmpv                               = new Vector3d();
    private static Loc                   locLloc                            = new Loc();
    private static Point3d               locLpos                            = new Point3d();
    private static Matrix4d              locLmatr                           = new Matrix4d();
    private static boolean               someObjectWasLightedInPreviousCall = true;
    private static HashMapExt            hashmap_ON                         = new HashMapExt();
    private static HashMapExt            hashmap_ALL                        = new HashMapExt();
    private static boolean               hashmap_ALL_is_changed             = true;
    private static java.util.Map.Entry   next_entry_get                     = null;
    private static Point3f               accumColor                         = new Point3f();
    private static Vector3d              accumDir                           = new Vector3d();
    private static Point3d               planeP                             = new Point3d();
    public static Point3d                nosePos                            = new Point3d();
    private static Vector3d              noseDir                            = new Vector3d();
    private static Vector3d              targRayDir                         = new Vector3d();
//    private static Point3d targetPos = new Point3d();
    private static Loc                   Cam2WorldLoc                       = new Loc();
    private NetMsgFiltered               outCommand;
}
