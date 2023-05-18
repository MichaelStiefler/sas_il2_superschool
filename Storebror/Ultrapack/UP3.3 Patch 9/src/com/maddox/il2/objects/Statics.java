/*410 class*/
package com.maddox.il2.objects;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.AirportStatic;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.air.Airdrome;
import com.maddox.il2.ai.air.Point_Runaway;
import com.maddox.il2.ai.air.Point_Stay;
import com.maddox.il2.ai.air.Point_Taxi;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorLandMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorSpawn;
import com.maddox.il2.engine.ActorSpawnArg;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Loc;
import com.maddox.il2.engine.MsgDreamGlobalListener;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.il2.objects.bridges.LongBridge;
import com.maddox.il2.objects.buildings.House;
import com.maddox.il2.objects.buildings.Plate;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetEnv;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.util.AsciiBitSet;
import com.maddox.util.HashMapInt;
import com.maddox.util.HashMapIntEntry;

public class Statics extends Actor implements MsgDreamGlobalListener {
    private static SectFile ships       = null;
    private static SectFile technics    = null;
    private static SectFile buildings   = null;
    public ArrayList        bridges     = new ArrayList();
    private ActorSpawn[]    spawns;
    private boolean[]       spawnIsPlate;
    private float[]         uniformMaxDist;
    private ArrayList[]     cacheActors;
    private HashMapInt      allBlocks;
    private HashMapInt      allBridge0;
    private HashMapInt      allStates0;
    private int[]           _updateX    = new int[1];
    private int[]           _updateY    = new int[1];
    private Queue           wQueue      = new Queue();
    private Queue           sQueue      = new Queue();
    private boolean         bCheckPlate = true;
    Loc                     _loc        = new Loc();
    ActorSpawnArg           _spawnArg   = new ActorSpawnArg();

    static class Queue {
        int   ofs = 0;
        int   len = 0;
        int[] X   = new int[256];
        int[] Y   = new int[256];

        void clear() {
            this.ofs = this.len = 0;
        }

        void add(int i, int i_0_) {
            if (this.X.length <= this.len) {
                int[] is = new int[this.len * 2];
                int[] is_1_ = new int[this.len * 2];
                for (int i_2_ = this.ofs; i_2_ < this.len; i_2_++) {
                    is[i_2_] = this.X[i_2_];
                    is_1_[i_2_] = this.Y[i_2_];
                }
                this.X = is;
                this.Y = is_1_;
            }
            this.X[this.len] = i;
            this.Y[this.len] = i_0_;
            this.len++;
        }
    }

    public static class Block {
        boolean bExistPlate = false;
        int[]   code;

        // TODO: Edited by |ZUTI|: from private to public
        public Actor[] actor;

        public int amountObjects() {
            return this.code.length / 2;
        }

        public boolean isEquals(byte[] is) {
            int i = this.code.length / 2;
            if (this.actor != null && this.actor[0] != null) for (int i_3_ = 0; i_3_ < i; i_3_++) {
                int i_4_ = i_3_ >> 8;
                int i_5_ = 1 << (i_3_ & 0xff);
                if (!Actor.isAlive(this.actor[i_3_])) {
                    if ((is[i_4_] & i_5_) == 0) return false;
                    if ((is[i_4_] & i_5_) != 0) return false;
                }
            }
            else for (int i_6_ = 0; i_6_ < i; i_6_++) {
                int i_7_ = i_6_ >> 8;
                int i_8_ = 1 << (i_6_ & 0xff);
                if ((this.code[i_6_ * 2] & 0x8000) != 0) {
                    if ((is[i_7_] & i_8_) == 0) return false;
                    if ((is[i_7_] & i_8_) != 0) return false;
                }
            }
            return true;
        }

        public byte[] getDestruction(byte[] is) {
            int i = this.code.length / 2;
            int i_9_ = (i + 7) / 8;
            if (is == null || is.length < i_9_) is = new byte[i_9_];
            if (this.actor != null && this.actor[0] != null) for (int i_10_ = 0; i_10_ < i; i_10_++) {
                int i_11_ = i_10_ >> 8;
                int i_12_ = 1 << (i_10_ & 0xff);
                if (!Actor.isAlive(this.actor[i_10_])) is[i_11_] |= i_12_;
                else is[i_11_] &= i_12_ ^ 0xffffffff;
            }
            else for (int i_13_ = 0; i_13_ < i; i_13_++) {
                int i_14_ = i_13_ >> 8;
                int i_15_ = 1 << (i_13_ & 0xff);
                if ((this.code[i_13_ * 2] & 0x8000) != 0) is[i_14_] |= i_15_;
                else is[i_14_] &= i_15_ ^ 0xffffffff;
            }
            return is;
        }

        public void setDestruction(byte[] is) {
            int i = this.code.length / 2;
            if (this.actor != null && this.actor[0] != null) for (int i_16_ = 0; i_16_ < i; i_16_++) {
                int i_17_ = i_16_ >> 8;
                int i_18_ = 1 << (i_16_ & 0xff);
                this.actor[i_16_].setDiedFlag((is[i_17_] & i_18_) != 0);
            }
            else for (int i_19_ = 0; i_19_ < i; i_19_++) {
                int i_20_ = i_19_ >> 8;
                int i_21_ = 1 << (i_19_ & 0xff);
                if ((is[i_20_] & i_21_) != 0) this.code[i_19_ * 2] |= 0x8000;
                else this.code[i_19_ * 2] &= ~0x8000;
            }
        }

        public void addDestruction(byte[] is) {
            int i = this.code.length / 2;
            if (this.actor != null && this.actor[0] != null) for (int i_22_ = 0; i_22_ < i; i_22_++) {
                int i_23_ = i_22_ >> 8;
                int i_24_ = 1 << (i_22_ & 0xff);
                if ((is[i_23_] & i_24_) != 0) this.actor[i_22_].setDiedFlag(true);
            }
            else for (int i_25_ = 0; i_25_ < i; i_25_++) {
                int i_26_ = i_25_ >> 8;
                int i_27_ = 1 << (i_25_ & 0xff);
                if ((is[i_26_] & i_27_) != 0) this.code[i_25_ * 2] |= 0x8000;
            }
        }

        public float getDestruction() {
            int i = this.code.length / 2;
            int i_28_ = 0;
            if (this.actor != null && this.actor[0] != null) {
                for (int i_29_ = 0; i_29_ < i; i_29_++)
                    if (!Actor.isAlive(this.actor[i_29_])) i_28_++;
            } else for (int i_30_ = 0; i_30_ < i; i_30_++)
                if ((this.code[i_30_ * 2] & 0x8000) != 0) i_28_++;
            return (float) i_28_ / (float) i;
        }

        public void setDestruction(float f) {
            int i = this.code.length / 2;
            int i_31_ = (int) (f * i + 0.5F);
            if (i_31_ > i) i_31_ = i;
            if (this.actor != null && this.actor[0] != null) {
                for (int i_32_ = 0; i_32_ < i; i_32_++)
                    if (i_31_ > 0) {
                        if (Actor.isAlive(this.actor[i_32_])) this.actor[i_32_].setDiedFlag(true);
                        i_31_--;
                    } else if (!Actor.isAlive(this.actor[i_32_])) this.actor[i_32_].setDiedFlag(false);
            } else for (int i_33_ = 0; i_33_ < i; i_33_++)
                if (i_31_ > 0) {
                    this.code[i_33_ * 2] |= 0x8000;
                    i_31_--;
                } else this.code[i_33_ * 2] &= ~0x8000;
        }

        public boolean isDestructed() {
            int i = this.code.length / 2;
            if (this.actor != null && this.actor[0] != null) {
                for (int i_34_ = 0; i_34_ < i; i_34_++)
                    if (!this.actor[i_34_].isAlive()) return true;
            } else for (int i_35_ = 0; i_35_ < i; i_35_++)
                if ((this.code[i_35_ * 2] & 0x8000) != 0) return true;
            return false;
        }

        public void restoreAll() {
            int i = this.code.length / 2;
            if (this.actor != null && this.actor[0] != null) {
                for (int i_36_ = 0; i_36_ < i; i_36_++)
                    if (!this.actor[i_36_].isAlive()) this.actor[i_36_].setDiedFlag(false);
            } else for (int i_37_ = 0; i_37_ < i; i_37_++)
                this.code[i_37_ * 2] &= ~0x8000;
        }

        public void restoreAll(float f, float f_38_, float f_39_, float f_40_, float f_41_) {
            int i = this.code.length / 2;
            if (this.actor != null && this.actor[0] != null) {
                for (int i_42_ = 0; i_42_ < i; i_42_++)
                    if (!this.actor[i_42_].isAlive()) {
                        Point3d point3d = this.actor[i_42_].pos.getAbsPoint();
                        if ((point3d.x - f_39_) * (point3d.x - f_39_) + (point3d.y - f_40_) * (point3d.y - f_40_) <= f_41_) this.actor[i_42_].setDiedFlag(false);
                    }
            } else for (int i_43_ = 0; i_43_ < i; i_43_++)
                if ((this.code[i_43_ * 2] & 0x8000) != 0) {
                    int i_45_ = this.code[i_43_ * 2 + 1];
                    short i_46_ = (short) (i_45_ & 0xffff);
                    short i_47_ = (short) (i_45_ >> 16 & 0xffff);
                    float f_48_ = i_46_ * 200.0F / 32000.0F + f;
                    float f_49_ = i_47_ * 200.0F / 32000.0F + f_38_;
                    if ((f_48_ - f_39_) * (f_48_ - f_39_) + (f_49_ - f_40_) * (f_49_ - f_40_) <= f_41_) this.code[i_43_ * 2] &= ~0x8000;
                }
        }
    }

    public static int[] readBridgesEndPoints(String string) {
        int[] is;
        try {
            DataInputStream datainputstream = new DataInputStream(new SFSInputStream(string));
            int i = datainputstream.readInt();
            if (i == -65535) i = datainputstream.readInt();
            is = new int[i * 4];
            for (int i_50_ = 0; i_50_ < i; i_50_++) {
                int i_51_ = datainputstream.readInt();
                int i_52_ = datainputstream.readInt();
                int i_53_ = datainputstream.readInt();
                int i_54_ = datainputstream.readInt();
                datainputstream.readInt();
                datainputstream.readFloat();
                is[i_50_ * 4 + 0] = i_51_;
                is[i_50_ * 4 + 1] = i_52_;
                is[i_50_ * 4 + 2] = i_53_;
                is[i_50_ * 4 + 3] = i_54_;
            }
            datainputstream.close();
        } catch (Exception exception) {
            is = null;
            String string_56_ = "Bridges data in '" + string + "' DAMAGED: " + exception.getMessage();
            System.out.println(string_56_);
        }
        return is;
    }

    public static void load(String string, List list) {
        World.cur().statics._load(string, list);
    }

    private void _load(String string, List list) {
        try {
            DataInputStream datainputstream = new DataInputStream(new SFSInputStream(string));
            int i = datainputstream.readInt();
            if (i != -65535) throw new Exception("Not supported sersion");
            System.out.println("Load bridges");
            int spawnPointsNumber = datainputstream.readInt();
            for (int index = 0; index < spawnPointsNumber; index++) {
                int i_59_ = datainputstream.readInt();
                int i_60_ = datainputstream.readInt();
                int i_61_ = datainputstream.readInt();
                int i_62_ = datainputstream.readInt();
                int i_63_ = datainputstream.readInt();
                float f = datainputstream.readFloat();
                Bridge bridge = new Bridge(index, i_63_, i_59_, i_60_, i_61_, i_62_, f);
                if (list != null) list.add(bridge);
            }
            System.out.println("Load static objects");
            ArrayList arraylist = new ArrayList();
            spawnPointsNumber = datainputstream.readInt();
            this.spawns = null;
            if (spawnPointsNumber > 0) {
                int i_64_ = -1;
                ActorSpawnArg actorspawnarg = new ActorSpawnArg();
                this.spawns = new ActorSpawn[spawnPointsNumber];
                for (int index = 0; index < spawnPointsNumber; index++) {
                    String string_66_ = datainputstream.readUTF();
                    if ("com.maddox.il2.objects.air.Runaway".equals(string_66_)) i_64_ = index;
                    this.spawns[index] = (ActorSpawn) Spawn.get_WithSoftClass(string_66_);
                }
                spawnPointsNumber = datainputstream.readInt();
                while (spawnPointsNumber-- > 0) {
                    int i_67_ = datainputstream.readInt();
                    float f = datainputstream.readFloat();
                    float f_68_ = datainputstream.readFloat();
                    float f_69_ = datainputstream.readFloat();
                    this._loc.set(f, f_68_, 0.0, f_69_, 0.0F, 0.0F);
                    if (i_64_ == i_67_) {
                        Loc loc = new Loc(this._loc);
                        arraylist.add(loc);
                    }
                    if (i_67_ < this.spawns.length && this.spawns[i_67_] != null) {
                        actorspawnarg.clear();
                        actorspawnarg.point = this._loc.getPoint();
                        actorspawnarg.orient = this._loc.getOrient();
                        try {
                            Actor actor = this.spawns[i_67_].actorSpawn(actorspawnarg);
                            if (actor instanceof ActorLandMesh) {
                                ActorLandMesh actorlandmesh = (ActorLandMesh) actor;
                                actorlandmesh.mesh().setPos(actorlandmesh.pos.getAbs());
                                Landscape.meshAdd(actorlandmesh);
                            }
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                }
            }
            spawnPointsNumber = datainputstream.readInt();
            this.spawns = null;
            int i_70_ = -1;
            if (spawnPointsNumber > 0) {
                this.spawns = new ActorSpawn[spawnPointsNumber];
                this.spawnIsPlate = new boolean[spawnPointsNumber];
                this.uniformMaxDist = new float[spawnPointsNumber];
                this.cacheActors = new ArrayList[spawnPointsNumber];
                this._loc.set(0.0, 0.0, 0.0, 0.0F, 0.0F, 0.0F);
                for (int i_71_ = 0; i_71_ < spawnPointsNumber; i_71_++) {
                    String string_72_ = datainputstream.readUTF();
                    if (string_72_.indexOf("TreeLine") >= 0) i_70_ = i_71_;
                    this.spawns[i_71_] = (ActorSpawn) Spawn.get_WithSoftClass(string_72_);
                    this.cacheActors[i_71_] = new ArrayList();
                    try {
                        this._spawnArg.point = this._loc.getPoint();
                        this._spawnArg.orient = this._loc.getOrient();
                        Actor actor = this.spawns[i_71_].actorSpawn(this._spawnArg);
                        this.cachePut(i_71_, actor);
                        this.spawnIsPlate[i_71_] = actor instanceof Plate;
                        if (actor instanceof ActorMesh) {
                            this.uniformMaxDist[i_71_] = ((ActorMesh) actor).mesh().getUniformMaxDist();
                            actor.draw.uniformMaxDist = this.uniformMaxDist[i_71_];
                        } else this.uniformMaxDist[i_71_] = 0.0F;
                    } catch (Exception exception) {
                        System.out.println(exception.getMessage());
                        exception.printStackTrace();
                    }
                }
                int i_73_ = datainputstream.readInt();
                this.allBlocks = new HashMapInt((int) (i_73_ / 0.75F));
                while (i_73_-- > 0) {
                    int i_74_ = datainputstream.readInt();
                    int i_75_ = i_74_ & 0xffff;
                    int i_76_ = i_74_ >> 16 & 0xffff;
                    int i_77_ = datainputstream.readInt();
                    Block block = new Block();
                    block.code = new int[i_77_ * 2];
                    float f = 0.0F;
                    for (int i_78_ = 0; i_78_ < i_77_; i_78_++) {
                        int i_79_ = datainputstream.readInt();
                        if ((i_79_ & 0x7fff) >= this.spawns.length) i_79_ = 0;
                        block.code[i_78_ * 2 + 0] = i_79_;
                        block.code[i_78_ * 2 + 1] = datainputstream.readInt();
                        if (this.spawnIsPlate[i_79_ & 0x7fff]) block.bExistPlate = true;
                        float f_80_ = this.uniformMaxDist[i_79_ & 0x7fff];
                        if (f < f_80_) f = f_80_;
                    }
                    this.allBlocks.put(this.key(i_76_, i_75_), block);
                    Engine.drawEnv().setUniformMaxDist(i_75_, i_76_, f);
                    if (block.bExistPlate) {
                        this.bCheckPlate = false;
                        this._updateX[0] = i_75_;
                        this._updateY[0] = i_76_;
                        this._msgDreamGlobal(true, 1, 0, this._updateX, this._updateY);
                        this.bCheckPlate = true;
                    }
                }
                if (Config.isUSE_RENDER()) {
                    int i_81_ = Landscape.getSizeXpix();
                    int i_82_ = Landscape.getSizeYpix();
                    int[] is = new int[i_81_ + 31 >> 5];
                    for (int i_83_ = 0; i_83_ < i_82_; i_83_++) {
                        for (int i_84_ = 0; i_84_ < is.length; i_84_++)
                            is[i_84_] = 0;
                        int i_85_ = 0;
                        int i_86_ = 0;
                        while (i_86_ < i_81_) {
                            if (this.allBlocks.containsKey(this.key(i_83_, i_86_))) is[i_85_ >> 5] |= 1 << (i_85_ & 0x1f);
                            i_86_++;
                            i_85_++;
                        }
                        Landscape.MarkStaticActorsCells(0, i_83_, i_81_, 1, 200, is);
                    }
                    i_81_ = Landscape.getSizeXpix();
                    i_82_ = Landscape.getSizeYpix();
//					int i_87_ = 0;
                    for (int i_88_ = 0; i_88_ < i_82_; i_88_++)
                        for (int i_89_ = 0; i_89_ < i_81_; i_89_++)
                            if (this.allBlocks.containsKey(this.key(i_88_, i_89_))) {
                                Block block = (Block) this.allBlocks.get(this.key(i_88_, i_89_));
                                Landscape.MarkActorCellWithTrees(i_70_, i_89_, i_88_, block.code, block.code.length);
                                for (int i_90_ = 0; i_90_ < block.code.length; i_90_ += 2) {
//									if ((block.code[i_90_] & 0x7fff) == i_70_) i_87_++;
                                }
                            }
                }
            }
            Airdrome airdrome = new Airdrome();
            if (datainputstream.available() > 0) {
                spawnPointsNumber = datainputstream.readInt();
                airdrome.runw = new Point_Runaway[spawnPointsNumber][];
                for (int i_91_ = 0; i_91_ < spawnPointsNumber; i_91_++) {
                    int i_92_ = datainputstream.readInt();
                    airdrome.runw[i_91_] = new Point_Runaway[i_92_];
                    for (int i_93_ = 0; i_93_ < i_92_; i_93_++)
                        airdrome.runw[i_91_][i_93_] = new Point_Runaway(datainputstream.readFloat(), datainputstream.readFloat());
                }
                spawnPointsNumber = datainputstream.readInt();
                airdrome.taxi = new Point_Taxi[spawnPointsNumber][];
                for (int i_94_ = 0; i_94_ < spawnPointsNumber; i_94_++) {
                    int i_95_ = datainputstream.readInt();
                    airdrome.taxi[i_94_] = new Point_Taxi[i_95_];
                    for (int i_96_ = 0; i_96_ < i_95_; i_96_++)
                        airdrome.taxi[i_94_][i_96_] = new Point_Taxi(datainputstream.readFloat(), datainputstream.readFloat());
                }
                spawnPointsNumber = datainputstream.readInt();
                airdrome.stay = new Point_Stay[spawnPointsNumber][];
                for (int airdromeIndex = 0; airdromeIndex < spawnPointsNumber; airdromeIndex++) {
                    int numberOfPoints = datainputstream.readInt();
                    airdrome.stay[airdromeIndex] = new Point_Stay[numberOfPoints];
                    for (int pointsCounter = 0; pointsCounter < numberOfPoints; pointsCounter++)
                        airdrome.stay[airdromeIndex][pointsCounter] = new Point_Stay(datainputstream.readFloat(), datainputstream.readFloat());
                }
                airdrome.stayHold = new boolean[airdrome.stay.length];
            }
            World.cur().airdrome = airdrome;
            AirportStatic.make(arraylist, airdrome.runw, airdrome.taxi, airdrome.stay);
            datainputstream.close();
        } catch (Exception exception) {
            String string_100_ = "Actors load from '" + string + "' FAILED: " + exception.getMessage();
            System.out.println(string_100_);
            exception.printStackTrace();
        }
    }

    public void restoreAllBridges() {
        int i = 0;
        for (;;) {
            LongBridge longbridge = LongBridge.getByIdx(i);
            if (longbridge == null) break;
            if (!longbridge.isAlive()) longbridge.BeLive();
            i++;
        }
    }

    public void saveStateBridges(SectFile sectfile, int i) {
        int i_101_ = 0;
        for (;;) {
            LongBridge longbridge = LongBridge.getByIdx(i_101_);
            if (longbridge == null) break;
            if (!longbridge.isAlive()) {
                int i_102_ = longbridge.NumStateBits();
                BitSet bitset = longbridge.GetStateOfSegments();
                sectfile.lineAdd(i, AsciiBitSet.save(i_101_), AsciiBitSet.save(bitset, i_102_));
            }
            i_101_++;
        }
    }

    public void loadStateBridges(SectFile sectfile, boolean bool) {
        int i = sectfile.sectionIndex(bool ? "AddBridge" : "Bridge");
        if (i >= 0) this.loadStateBridges(sectfile, i, bool);
    }

    public void loadStateBridges(SectFile sectfile, int i, boolean bool) {
        boolean bool_103_ = false;
        if (!bool && Mission.isDogfight()) {
            bool_103_ = true;
            this.allBridge0 = new HashMapInt();
        }
        int i_104_ = sectfile.vars(i);
        BitSet bitset = new BitSet();
        for (int i_105_ = 0; i_105_ < i_104_; i_105_++) {
            String string = sectfile.var(i, i_105_);
            String string_106_ = sectfile.value(i, i_105_);
            int i_107_ = AsciiBitSet.load(string);
            LongBridge longbridge = LongBridge.getByIdx(i_107_);
            if (longbridge != null) {
                AsciiBitSet.load(string_106_, bitset, longbridge.NumStateBits());
                if (bool) bitset.or(longbridge.GetStateOfSegments());
                longbridge.SetStateOfSegments(bitset);
                if (bool_103_) this.allBridge0.put(i_107_, bitset.clone());
            }
        }
    }

    public void restoreAllHouses() {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        for (HashMapIntEntry hashmapintentry = this.allBlocks.nextEntry(null); hashmapintentry != null; hashmapintentry = this.allBlocks.nextEntry(hashmapintentry)) {
            Block block = (Block) hashmapintentry.getValue();
            if (block.isDestructed()) block.restoreAll();
        }
    }

    public void saveStateHouses(SectFile sectfile, int i) {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        for (HashMapIntEntry hashmapintentry = this.allBlocks.nextEntry(null); hashmapintentry != null; hashmapintentry = this.allBlocks.nextEntry(hashmapintentry)) {
            Block block = (Block) hashmapintentry.getValue();
            if (block.isDestructed()) {
                int i_108_ = hashmapintentry.getKey();
                sectfile.lineAdd(i, AsciiBitSet.save(i_108_), AsciiBitSet.save(block.getDestruction(null), block.amountObjects()));
            }
        }
    }

    public void loadStateHouses(SectFile sectfile, boolean bool) {
        int i = sectfile.sectionIndex(bool ? "AddHouse" : "House");
        if (i >= 0) this.loadStateHouses(sectfile, i, bool);
    }

    public void loadStateHouses(SectFile sectfile, int i, boolean bool) {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        boolean bool_109_ = false;
        if (!bool && Mission.isDogfight()) {
            bool_109_ = true;
            this.allStates0 = new HashMapInt();
        }
        int i_110_ = sectfile.vars(i);
        byte[] is = new byte[32];
        for (int i_111_ = 0; i_111_ < i_110_; i_111_++) {
            String string = sectfile.var(i, i_111_);
            String string_112_ = sectfile.value(i, i_111_);
            int i_113_ = AsciiBitSet.load(string);
            Block block = (Block) this.allBlocks.get(i_113_);
            if (block != null) {
                is = AsciiBitSet.load(string_112_, is, block.amountObjects());
                if (bool) block.addDestruction(is);
                else {
                    block.setDestruction(is);
                    if (bool_109_) {
                        int i_114_ = (block.amountObjects() + 7) / 8;
                        byte[] is_115_ = new byte[i_114_];
                        System.arraycopy(is, 0, is_115_, 0, i_114_);
                        this.allStates0.put(i_113_, is_115_);
                    }
                }
            }
        }
    }

    public void restoreAllHouses(float f, float f_116_, float f_117_) {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        int i = (int) ((f - f_117_) / 200.0F) - 1;
        int i_118_ = (int) ((f + f_117_) / 200.0F) + 2;
        int i_119_ = (int) ((f_116_ - f_117_) / 200.0F) - 1;
        int i_120_ = (int) ((f_116_ + f_117_) / 200.0F) + 2;
        HashMapIntEntry hashmapintentry = this.allBlocks.nextEntry(null);
        float f_121_ = f_117_ * f_117_;
        for (/**/; hashmapintentry != null; hashmapintentry = this.allBlocks.nextEntry(hashmapintentry)) {
            Block block = (Block) hashmapintentry.getValue();
            int i_122_ = hashmapintentry.getKey();
            int i_123_ = this.key2x(i_122_);
            int i_124_ = this.key2y(i_122_);
            if (i_123_ >= i && i_123_ <= i_118_ && i_124_ >= i_119_ && i_124_ <= i_120_) block.restoreAll(i_123_ * 200.0F, i_124_ * 200.0F, f, f_116_, f_121_);
        }
    }

    public void netBridgeSync(NetChannel netchannel) {
        int i = 0;
        for (;;) {
            LongBridge longbridge = LongBridge.getByIdx(i);
            if (longbridge == null) break;
            if (!longbridge.isAlive()) {
                int i_125_ = longbridge.NumStateBits();
                BitSet bitset = longbridge.GetStateOfSegments();
                BitSet bitset_126_ = null;
                if (this.allBridge0 != null) bitset_126_ = (BitSet) this.allBridge0.get(i);
                if (bitset_126_ == null || !bitset_126_.equals(bitset)) try {
                    NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                    netmsgguaranted.writeByte(28);
                    netmsgguaranted.writeShort(i);
                    int i_127_ = (i_125_ + 7) / 8;
                    byte[] is = new byte[i_127_];
                    for (int i_128_ = 0; i_128_ < i_125_; i_128_++)
                        if (bitset.get(i_128_)) {
                            int i_129_ = i_128_ / 8;
                            int i_130_ = i_128_ % 8;
                            is[i_129_] |= 1 << i_130_;
                        }
                    netmsgguaranted.write(is);
                    NetEnv.host().postTo(netchannel, netmsgguaranted);
                } catch (Exception exception) {
                    /* empty */
                }
            }
            i++;
        }
    }

    public void netMsgBridgeSync(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedShort();
        LongBridge longbridge = LongBridge.getByIdx(i);
        if (longbridge != null) {
            byte[] is = new byte[netmsginput.available()];
            netmsginput.read(is);
            int i_131_ = longbridge.NumStateBits();
            BitSet bitset = longbridge.GetStateOfSegments();
            for (int i_132_ = 0; i_132_ < i_131_; i_132_++) {
                int i_133_ = i_132_ / 8;
                int i_134_ = i_132_ % 8;
                if ((is[i_133_] & 1 << i_134_) != 0) bitset.set(i_132_);
                else bitset.clear(i_132_);
            }
            longbridge.SetStateOfSegments(bitset);
        }
    }

    public boolean onBridgeDied(int i, int i_135_, int i_136_, Actor actor) {
        if (Mission.isServer()) {
            try {
                NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
                netmsgguaranted.writeByte(27);
                netmsgguaranted.writeShort(i);
                netmsgguaranted.writeShort(i_135_);
                netmsgguaranted.writeByte(i_136_);
                com.maddox.il2.engine.ActorNet actornet = null;
                if (Actor.isValid(actor)) actornet = actor.net;
                netmsgguaranted.writeNetObj(actornet);
                NetEnv.host().post(netmsgguaranted);
            } catch (Exception exception) {
                /* empty */
            }
            return true;
        }
        try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(26);
            netmsgguaranted.writeShort(i);
            netmsgguaranted.writeShort(i_135_);
            netmsgguaranted.writeByte(i_136_);
            com.maddox.il2.engine.ActorNet actornet = null;
            if (Actor.isValid(actor)) actornet = actor.net;
            netmsgguaranted.writeNetObj(actornet);
            NetEnv.host().postTo(Main.cur().netServerParams.masterChannel(), netmsgguaranted);
        } catch (Exception exception) {
            /* empty */
        }
        return false;
    }

    public void netMsgBridgeRDie(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedShort();
        int i_137_ = netmsginput.readUnsignedShort();
        byte i_138_ = netmsginput.readByte();
        Actor actor = null;
        NetObj netobj = netmsginput.readNetObj();
        if (netobj != null) actor = (Actor) netobj.superObj();
        if (Mission.isServer()) {
            BridgeSegment bridgesegment = BridgeSegment.getByIdx(i, i_137_);
            bridgesegment.netForcePartDestroing(i_138_, actor);
        } else this.onBridgeDied(i, i_137_, i_138_, actor);
    }

    public void netMsgBridgeDie(NetObj netobj, NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readUnsignedShort();
        int i_139_ = netmsginput.readUnsignedShort();
        byte i_140_ = netmsginput.readByte();
        Actor actor = null;
        NetObj netobj_141_ = netmsginput.readNetObj();
        if (netobj_141_ != null) actor = (Actor) netobj_141_.superObj();
        try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(27);
            netmsgguaranted.writeShort(i);
            netmsgguaranted.writeShort(i_139_);
            netmsgguaranted.writeByte(i_140_);
            com.maddox.il2.engine.ActorNet actornet = null;
            if (Actor.isValid(actor)) actornet = actor.net;
            netmsgguaranted.writeNetObj(actornet);
            netobj.post(netmsgguaranted);
        } catch (Exception exception) {
            /* empty */
        }
        BridgeSegment bridgesegment = BridgeSegment.getByIdx(i, i_139_);
        bridgesegment.netForcePartDestroing(i_140_, actor);
    }

    public void netHouseSync(NetChannel netchannel) {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        for (HashMapIntEntry hashmapintentry = this.allBlocks.nextEntry(null); hashmapintentry != null; hashmapintentry = this.allBlocks.nextEntry(hashmapintentry)) {
            Block block = (Block) hashmapintentry.getValue();
            int i = hashmapintentry.getKey();
            byte[] is = null;
            if (this.allStates0 != null) is = (byte[]) this.allStates0.get(i);
            if (is == null) {
                if (block.isDestructed()) this.putMsgHouseSync(netchannel, i, block);
            } else if (!block.isEquals(is)) this.putMsgHouseSync(netchannel, i, block);
        }
    }

    private void putMsgHouseSync(NetChannel netchannel, int i, Block block) {
        try {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(25);
            netmsgguaranted.writeInt(i);
            netmsgguaranted.write(block.getDestruction(null));
            NetEnv.host().postTo(netchannel, netmsgguaranted);
        } catch (Exception exception) {
            /* empty */
        }
    }

    public void netMsgHouseSync(NetMsgInput netmsginput) throws IOException {
        int i = netmsginput.readInt();
        byte[] is = new byte[netmsginput.available()];
        netmsginput.read(is);
        Block block = (Block) this.allBlocks.get(i);
        if (block != null) block.setDestruction(is);
    }

    public void netMsgHouseDie(NetObj netobj, NetMsgInput netmsginput) throws IOException {
        Actor actor = null;
        NetObj netobj_142_ = netmsginput.readNetObj();
        if (netobj_142_ != null) actor = (Actor) netobj_142_.superObj();
        int i = netmsginput.readInt();
        int i_143_ = netmsginput.readUnsignedShort();
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        Block block = (Block) this.allBlocks.get(i);
        if (block != null && i_143_ < block.code.length / 2) if (block.actor != null && block.actor[0] != null) {
            if (Actor.isAlive(block.actor[i_143_]) && block.actor[i_143_] instanceof House) {
                ((House) block.actor[i_143_]).doDieShow();
                World.onActorDied(block.actor[i_143_], actor);
                this.replicateHouseDie(netobj, netmsginput.channel(), netobj_142_, i, i_143_);
            }
        } else if ((block.code[i_143_ * 2] & 0x8000) == 0) {
            block.code[i_143_ * 2] |= 0x8000;
            this.replicateHouseDie(netobj, netmsginput.channel(), netobj_142_, i, i_143_);
        }
    }

    public void onHouseDied(House house, Actor actor) {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        Point3d point3d = house.pos.getAbsPoint();
        int i = (int) (point3d.y / 200.0);
        int i_144_ = (int) (point3d.x / 200.0);
        int i_145_ = this.key(i, i_144_);
        Block block = (Block) this.allBlocks.get(i_145_);
        if (block != null && block.actor != null) {
            int i_146_ = 0;
            int i_147_;
            for (i_147_ = block.actor.length; i_146_ < i_147_; i_146_++)
                if (block.actor[i_146_] == house) break;
            if (i_146_ < i_147_) {
                com.maddox.il2.engine.ActorNet actornet = null;
                if (Actor.isValid(actor)) actornet = actor.net;
                try {
                    this.replicateHouseDie(NetEnv.host(), null, actornet, i_145_, i_146_);
                } catch (Exception exception) {
                    /* empty */
                }
            }
        }
    }

    private void replicateHouseDie(NetObj netobj, NetChannel netchannel, NetObj netobj_148_, int i, int i_149_) throws IOException {
        int i_150_ = netobj.countMirrors();
        if (netobj.isMirror()) i_150_++;
        if (netchannel != null) i_150_--;
        if (i_150_ > 0) {
            NetMsgGuaranted netmsgguaranted = new NetMsgGuaranted();
            netmsgguaranted.writeByte(24);
            netmsgguaranted.writeNetObj(netobj_148_);
            netmsgguaranted.writeInt(i);
            netmsgguaranted.writeShort(i_149_);
            netobj.postExclude(netchannel, netmsgguaranted);
        }
    }

    public HashMapInt allBlocks() {
        return this.allBlocks;
    }

    public int key(int i, int i_151_) {
        return i_151_ & 0xffff | i << 16;
    }

    public int key2x(int i) {
        return i & 0xffff;
    }

    public int key2y(int i) {
        return i >> 16 & 0xffff;
    }

    public void updateBlock(int i, int i_152_) {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        Block block = (Block) this.allBlocks.get(this.key(i_152_, i));
        if (block != null && block.actor != null) {
            this._updateX[0] = i;
            this._updateY[0] = i_152_;
            this._msgDreamGlobal(false, 1, 0, this._updateX, this._updateY);
            this._msgDreamGlobal(true, 1, 0, this._updateX, this._updateY);
        }
    }

    private Actor cacheGet(int i) {
        ArrayList arraylist = this.cacheActors[i];
        int i_153_ = arraylist.size();
        if (i_153_ == 0) return null;
        Actor actor = (Actor) arraylist.get(i_153_ - 1);
        arraylist.remove(i_153_ - 1);
        return actor;
    }

    private void cachePut(int i, Actor actor) {
        ArrayList arraylist = this.cacheActors[i];
        arraylist.add(actor);
        actor.drawing(false);
        actor.collide(false);
    }

    public static void trim() {
        /* empty */
    }

    public void msgDreamGlobalTick(int i, int i_154_) {
        this._msgDreamGlobalTick(false, i, i_154_ - 1);
        this._msgDreamGlobalTick(true, i, i_154_ - 1);
    }

    public void _msgDreamGlobalTick(boolean bool, int i, int i_155_) {
        Queue queue = bool ? this.wQueue : this.sQueue;
        if (queue.ofs < queue.len) {
            int i_156_ = i - i_155_;
            int i_157_ = (queue.len * i_156_ + i / 2) / i;
            if (i_157_ > queue.len) i_157_ = queue.len;
            int i_158_ = i_157_ - queue.ofs;
            if (i_158_ > 0) {
                this._msgDreamGlobal(bool, i_158_, queue.ofs, queue.X, queue.Y);
                queue.ofs = i_157_;
                if (queue.ofs == queue.len) queue.clear();
            }
        }
    }

    public void msgDreamGlobal(boolean bool, int i, int[] is, int[] is_159_) {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
//        if (this.allBlocks != null) {
        this._msgDreamGlobalTick(bool, 1, 0);
        Queue queue = bool ? this.wQueue : this.sQueue;
        for (int i_160_ = 0; i_160_ < i; i_160_++) {
            int i_161_ = is[i_160_];
            int i_162_ = is_159_[i_160_];
            Block block = (Block) this.allBlocks.get(this.key(i_162_, i_161_));
            if (block != null) queue.add(i_161_, i_162_);
        }
//        }
    }

    private void _msgDreamGlobal(boolean bool, int i, int i_163_, int[] is, int[] is_164_) {
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
        if (bool) for (int i_165_ = 0; i_165_ < i; i_165_++) {
            int i_166_ = is[i_165_ + i_163_];
            int i_167_ = is_164_[i_165_ + i_163_];
            Block block = (Block) this.allBlocks.get(this.key(i_167_, i_166_));
            if (block != null && (!this.bCheckPlate || !block.bExistPlate)) {
                float f = i_166_ * 200.0F;
                float f_168_ = i_167_ * 200.0F;
                int i_169_ = block.code.length / 2;
                block.actor = new Actor[i_169_];
                for (int i_170_ = 0; i_170_ < i_169_; i_170_++) {
                    int i_171_ = block.code[i_170_ * 2 + 0];
                    int i_172_ = block.code[i_170_ * 2 + 1];
                    int i_173_ = i_171_ & 0x7fff;
                    short i_174_ = (short) (i_171_ >> 16);
                    short i_175_ = (short) (i_172_ & 0xffff);
                    short i_176_ = (short) (i_172_ >> 16 & 0xffff);
                    float f_177_ = i_174_ * 360.0F / 32000.0F;
                    float f_178_ = i_175_ * 200.0F / 32000.0F + f;
                    float f_179_ = i_176_ * 200.0F / 32000.0F + f_168_;
                    this._loc.set(f_178_, f_179_, 0.0, -f_177_, 0.0F, 0.0F);
                    Actor actor = this.cacheGet(i_173_);
                    if (actor != null) {
                        actor.pos.setAbs(this._loc);
                        actor.setDiedFlag((i_171_ & 0x8000) != 0);
                    } else {
                        this._spawnArg.point = this._loc.getPoint();
                        this._spawnArg.orient = this._loc.getOrient();
                        try {
                            actor = this.spawns[i_173_].actorSpawn(this._spawnArg);
                            if ((i_171_ & 0x8000) != 0) actor.setDiedFlag(true);
                            if (actor instanceof ActorMesh) actor.draw.uniformMaxDist = this.uniformMaxDist[i_173_];
                        } catch (Exception exception) {
                            System.out.println(exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                    block.actor[i_170_] = actor;
                }
            }
        }
        else for (int i_180_ = 0; i_180_ < i; i_180_++) {
            int i_181_ = is[i_180_ + i_163_];
            int i_182_ = is_164_[i_180_ + i_163_];
            Block block = (Block) this.allBlocks.get(this.key(i_182_, i_181_));
            if (block != null && block.actor != null && (!this.bCheckPlate || !block.bExistPlate)) {
                int i_184_ = block.code.length / 2;
                for (int i_185_ = 0; i_185_ < i_184_; i_185_++) {
                    Actor actor = block.actor[i_185_];
                    block.actor[i_185_] = null;
                    if (actor != null) {
                        if (actor.getDiedFlag()) block.code[i_185_ * 2 + 0] |= 0x8000;
                        this.cachePut(block.code[i_185_ * 2 + 0] & 0x7fff, actor);
                    }
                }
                block.actor = null;
            }
        }
    }

    public void resetGame() {
        this.wQueue.clear();
        this.sQueue.clear();
        if (this.allBlocks == null) return; // TODO: Null check added by SAS~Storebror
//        if (this.allBlocks != null) {
        ArrayList arraylist = new ArrayList();
        for (HashMapIntEntry hashmapintentry = this.allBlocks.nextEntry(null); hashmapintentry != null; hashmapintentry = this.allBlocks.nextEntry(hashmapintentry)) {
            Block block = (Block) hashmapintentry.getValue();
            if (block.actor != null) for (int i = 0; i < block.actor.length; i++) {
                arraylist.add(block.actor[i]);
                block.actor[i] = null;
            }
        }
        Engine.destroyListGameActors(arraylist);
        for (int i = 0; i < this.cacheActors.length; i++) {
            ArrayList arraylist_186_ = this.cacheActors[i];
            Engine.destroyListGameActors(arraylist_186_);
        }
        this.allBlocks = null;
        this.allStates0 = null;
        this.allBridge0 = null;
        this.cacheActors = null;
        this.spawns = null;
        this.bridges.clear();
//        }
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public Statics() {
        this.flags |= 0x4000;
    }

    protected void createActorHashCode() {
        this.makeActorRealHashCode();
    }

    public synchronized static SectFile getShipsFile() {
        if (ships == null) {
            ships = new SectFile("com/maddox/il2/objects/ships.ini");
            ships.createIndexes();
        }
        return ships;
    }

    public synchronized static SectFile getTechnicsFile() {
        if (technics == null) {
            technics = new SectFile("com/maddox/il2/objects/technics.ini");
            technics.createIndexes();
        }
        return technics;
    }

    public synchronized static SectFile getBuildingsFile() {
        if (buildings == null) {
            buildings = new SectFile("com/maddox/il2/objects/static.ini");
            buildings.createIndexes();
        }
        return buildings;
    }
}
