package com.maddox.il2.objects.bridges;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;

import com.maddox.JGP.Matrix4d;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3d;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.ai.ground.RoadSegment;
import com.maddox.il2.ai.ground.UnitInterface;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorDraw;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.ActorPosStatic;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.LandConf;
import com.maddox.il2.engine.LandPlate;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.engine.Mesh;
import com.maddox.il2.engine.MeshShared;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetMissionTrack;
import com.maddox.il2.net.NetServerParams;
import com.maddox.il2.objects.effects.Explosions;
import com.maddox.il2.objects.trains.Train;
import com.maddox.rts.Message;
import com.maddox.rts.NetChannel;
import com.maddox.rts.NetChannelInStream;
import com.maddox.rts.NetMsgFiltered;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;

public class LongBridge extends Actor implements LandPlate {
    class Mirror extends ActorNet {

        public Mirror(Actor actor, NetChannel netchannel, int i) {
            super(actor, netchannel, i);
        }
    }

    class Master extends ActorNet {

        public boolean netInput(NetMsgInput netmsginput) throws IOException {
            if (netmsginput.isGuaranted()) return true;
            if (netmsginput.readUnsignedByte() != 80) return false;
            else {
                int i = netmsginput.readUnsignedByte();
                int j = netmsginput.readUnsignedByte();
                int k = netmsginput.readUnsignedByte();
                float f = netmsginput.readFloat();
                /* boolean flag = */netmsginput.readBoolean();
                NetObj netobj = netmsginput.readNetObj();

                // TODO: Patch Pack 107, solve bug where the index of a destroyed bridge is always module 256 when a map has more than 256 bridges
                if (netmsginput.available() > 3) {
                    i = netmsginput.readUnsignedShort();
                    j = netmsginput.readUnsignedShort();
                }
                // ---

                Actor actor = netobj != null ? ((ActorNet) netobj).actor() : null;
                BridgeSegment bridgesegment = BridgeSegment.getByIdx(i, j);
                bridgesegment.netLifeChanged(k, f, actor, true);
                return true;
            }
        }

        public Master(Actor actor) {
            super(actor);
        }
    }

    private class LongBridgeDraw extends ActorDraw {

        public int preRender(Actor actor) {
            Point3d point3d = new Point3d();
            LongBridge.this.pos.getRender(point3d);
            float f = (float) Math.sqrt((LongBridge.this.endX - LongBridge.this.begX) * (LongBridge.this.endX - LongBridge.this.begX) + (LongBridge.this.endY - LongBridge.this.begY) * (LongBridge.this.endY - LongBridge.this.begY));
            f *= 0.5F;
            f += 1.988F;
            f *= 200F;
            f += 500F;
            if (!Render.currentCamera().isSphereVisible(point3d, f)) return 0;
            else return LongBridge.this.mat.preRender();
        }

        public void render(Actor actor) {
            char c;
            switch (LongBridge.this.type) {
                case 0:
                    c = '\200';
                    break;

                case 1:
                    c = ' ';
                    break;

                default:
                    c = '@';
                    break;
            }
            Point3d point3d = LongBridge.this.ComputeSegmentPos3d(0);
            Point3d point3d1 = LongBridge.this.ComputeSegmentPos3d(1 + 2 * LongBridge.this.nMidCells);
            float f = 284F;
            float f1 = f * 0.25F + f * 1.4F;
            f1 += 500F;
            if (Render.currentCamera().isSphereVisible(point3d, f1)) {
                World.cur();
                World.land().renderBridgeRoad(LongBridge.this.mat, c, LongBridge.this.begX, LongBridge.this.begY, LongBridge.this.incX, LongBridge.this.incY, LongBridge.this.offsetKoef);
            }
            if (Render.currentCamera().isSphereVisible(point3d1, f1)) {
                World.cur();
                World.land().renderBridgeRoad(LongBridge.this.mat, c, LongBridge.this.endX, LongBridge.this.endY, -LongBridge.this.incX, -LongBridge.this.incY, -LongBridge.this.offsetKoef);
            }
        }

        private LongBridgeDraw() {
        }

    }

    public int type() {
        return this.type;
    }

    public boolean isStaticPos() {
        return true;
    }

    public int bridgeIdx() {
        return this.bridgeIdx;
    }

    private static String nameOfBridgeByIdx(int i) {
        return " Bridge" + i;
    }

    public static LongBridge getByIdx(int i) {
        return (LongBridge) Actor.getByName(nameOfBridgeByIdx(i));
    }

    public boolean isUsableFor(Actor actor) {
        return this.supervisor == null || this.supervisor == actor;
    }

    public void setSupervisor(Actor actor) {
        this.supervisor = actor;
    }

    public void resetSupervisor(Actor actor) {
        if (this.supervisor == actor) this.supervisor = null;
    }

    public float getWidth() {
        return this.width;
    }

    private String SegmentMeshName(boolean flag, int i) {
        return "3do/bridges/" + (this.type != 0 ? this.type != 1 ? "rail" : "country" : "highway") + this.winterSuffix + "/" + ((this.dirOct & 1) != 0 ? "long/" : "short/") + (flag ? "mid/" : "end/") + (i != 0 ? i != 3 ? "mono2" : "mono3" : "mono1")
                + ".sim";
    }

    private static int ComputeOctDirection(int i, int j) {
        byte byte0;
        if (i > 0) byte0 = j <= 0 ? (byte) (byte) (j >= 0 ? 0 : 1) : 7;
        else if (i < 0) byte0 = j <= 0 ? (byte) (j >= 0 ? 4 : 3) : 5;
        else byte0 = (byte) (j <= 0 ? 2 : 6);
        return byte0;
    }

    private Orient ComputeSegmentOrient(boolean flag) {
        float f = this.dirOct * 45F;
        if (!flag) {
            f += 180F;
            if (f >= 360F) f -= 360F;
        }
        Orient orient = new Orient();
        orient.setYPR(f, 0.0F, 0.0F);
        return orient;
    }

    private Point3d ComputeSegmentPos3d(int i) {
        float f;
        float f1;
        boolean flag;
        if (i == 0) {
            f = this.begX;
            f1 = this.begY;
            flag = true;
        } else if (i == 1 + 2 * this.nMidCells) {
            f = this.begX + this.incX * (1 + this.nMidCells);
            f1 = this.begY + this.incY * (1 + this.nMidCells);
            flag = false;
        } else {
            f = this.begX + this.incX * (i + 1 >> 1);
            f1 = this.begY + this.incY * (i + 1 >> 1);
            flag = (i & 1) == 0;
        }
        float f2 = this.offsetKoef + (flag ? 0.25F : -0.25F);
        float f3 = f + 0.5F + this.incX * f2;
        float f4 = f1 + 0.5F + this.incY * f2;
        Point3d point3d = new Point3d();
        point3d.x = World.land().PIX2WORLDX(f3);
        point3d.y = World.land().PIX2WORLDY(f4);
        point3d.z = 0.0D;
        return point3d;
    }

    private float SegmentLength() {
        return 200F * ((this.dirOct & 1) != 0 ? 0.7071F : 0.5F);
    }

    void ComputeSegmentKeyPoints(int i, Point3d point3d, Point3d point3d1, Point3d point3d2, Point3d point3d3) {
        boolean flag = i == 0;
        boolean flag1 = i == 1 + 2 * this.nMidCells;
//        boolean flag2 = !flag && !flag1;
        point3d1.set(this.ComputeSegmentPos3d(0));
        point3d3.set(this.ComputeSegmentPos3d(1));
        float f = (float) (point3d3.x - point3d1.x);
        float f1 = (float) (point3d3.y - point3d1.y);
        float f2 = 1.0F / (float) Math.sqrt(f * f + f1 * f1);
        point3d.set(this.ComputeSegmentPos3d(i));
        point3d1.set(point3d);
        point3d3.set(point3d);
        point3d1.sub(0.5D * f, 0.5D * f1, 0.0D);
        point3d3.add(0.5D * f, 0.5D * f1, 0.0D);
        if (flag) {
            point3d2.set(point3d1);
            point3d2.add(f * f2 * this.lengthEnd, f1 * f2 * this.lengthEnd, 0.0D);
            point3d1.z = 0.0D;
            point3d2.z = this.heightEnd;
            point3d3.z = this.height;
        } else if (flag1) {
            point3d2.set(point3d3);
            point3d2.sub(f * f2 * this.lengthEnd, f1 * f2 * this.lengthEnd, 0.0D);
            point3d1.z = this.height;
            point3d2.z = this.heightEnd;
            point3d3.z = 0.0D;
        } else {
            point3d2.set(point3d);
            point3d1.z = this.height;
            point3d2.z = this.height;
            point3d3.z = this.height;
        }
    }

    void ShowSegmentExplosion(BridgeSegment bridgesegment, int i, int j) {
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Point3d point3d2 = new Point3d();
        Point3d point3d3 = new Point3d();
        this.ComputeSegmentKeyPoints(i, point3d, point3d1, point3d2, point3d3);
        Point3d point3d4;
        Point3d point3d5;
        if (j == 0) {
            point3d4 = point3d1;
            point3d5 = point3d2;
        } else {
            point3d4 = point3d2;
            point3d5 = point3d3;
        }
        Explosions.ExplodeBridge(point3d4, point3d5, this.width);
    }

    public int NumStateBits() {
        return 2 * (1 + this.nMidCells + 1);
    }

    public BitSet GetStateOfSegments() {
        Object bridgeSegment[] = this.getOwnerAttached();
        BitSet bitset = new BitSet(2 * bridgeSegment.length);
        for (int i = 0; i < bridgeSegment.length; i++) {
            boolean flag = ((BridgeSegment) bridgeSegment[i]).IsDead(0);
            boolean flag1 = ((BridgeSegment) bridgeSegment[i]).IsDead(1);
            if (flag) bitset.set(i * 2 + 0);
            if (flag1) bitset.set(i * 2 + 1);
        }

        return bitset;
    }

    public void SetStateOfSegments(BitSet bitset) {
        Object bridgeSegment[] = this.getOwnerAttached();
        for (int i = 0; i < bridgeSegment.length; i++) {
            boolean flag1 = bitset.get(i * 2 + 0);
            boolean flag2 = bitset.get(i * 2 + 1);
            if (flag1 && i > 0) bitset.set((i - 1) * 2 + 1);
            if (flag2 && i < bridgeSegment.length - 1) bitset.set((i + 1) * 2 + 0);
        }

        boolean flag = false;
        for (int j = 0; j < bridgeSegment.length; j++) {
            boolean flag3 = bitset.get(j * 2 + 0);
            boolean flag4 = bitset.get(j * 2 + 1);
            if (flag3 || flag4) flag = true;
            ((BridgeSegment) bridgeSegment[j]).ForcePartState(0, !flag3);
            ((BridgeSegment) bridgeSegment[j]).ForcePartState(1, !flag4);
        }

        this.setDiedFlag(flag);
    }

    public void BeLive() {
        BitSet bitset = new BitSet(this.NumStateBits());
        this.SetStateOfSegments(bitset);
    }

    void SetSegmentDamageState(boolean flag, BridgeSegment bridgesegment, int i, float f, float f1, Actor actor) {
        int j = (f1 > 0.0F ? 0 : 2) + (f > 0.0F ? 0 : 1);
        boolean flag1 = i == 0;
        boolean flag2 = i == 1 + 2 * this.nMidCells;
        boolean flag3 = !flag1 && !flag2;
        boolean flag4 = flag2;
        if (flag3 && j == 2) flag4 = true;
        Orient orient = this.ComputeSegmentOrient(flag4);
        bridgesegment.pos.setAbs(orient);
        bridgesegment.pos.reset();
        bridgesegment.setMesh(MeshShared.get(this.SegmentMeshName(flag3, j)));
        bridgesegment.collide(true);
        bridgesegment.drawing(true);
        if (j != 0 && this.isAlive()) World.onActorDied(this, actor);
        if (j != 0 && this.travellers.size() > 0) {
            Object aobj[] = this.travellers.toArray();
            for (int k = 0; k < aobj.length; k++) {
                if (aobj[k] instanceof ChiefGround) {
                    ((ChiefGround) aobj[k]).BridgeSegmentDestroyed(this.bridgeIdx, i, actor);
                    continue;
                }
                if (aobj[k] instanceof Train) ((Train) aobj[k]).BridgeSegmentDestroyed(this.bridgeIdx, i, actor);
                else((UnitInterface) aobj[k]).absoluteDeath(actor);
            }

        }
        if (!flag) return;
        if (j == 0) return;
        Object bridgeSegment[] = this.getOwnerAttached();
        int l = 1 + 2 * this.nMidCells + 1;
        Actor actor1 = actor;
        if ((j & 1) != 0 && i > 0) ((BridgeSegment) bridgeSegment[i - 1]).ForcePartDestroing(1, actor1);
        if ((j & 2) != 0 && i < l - 1) ((BridgeSegment) bridgeSegment[i + 1]).ForcePartDestroing(0, actor1);
        if (flag1 && (j & 1) != 0) ((BridgeSegment) bridgeSegment[i]).ForcePartDestroing(1, actor1);
        if (flag2 && (j & 2) != 0) ((BridgeSegment) bridgeSegment[i]).ForcePartDestroing(0, actor1);
    }

    public void destroy() {
        Object bridgeSegment[] = this.getOwnerAttached();
        for (int i = 0; i < bridgeSegment.length; i++)
            ((BridgeSegment) bridgeSegment[i]).destroy();

        for (int j = 0; j < 2; j++)
            if (Actor.isValid(this.bridgeRoad[j])) {
                this.bridgeRoad[j].destroy();
                this.bridgeRoad[j] = null;
            }

        super.destroy();
    }

    public static void AddTraveller(int i, Actor actor) {
        if (!(actor instanceof UnitInterface) && !(actor instanceof ChiefGround) && !(actor instanceof Train)) {
            System.out.println("Error: traveller type");
            return;
        }
        LongBridge longbridge = getByIdx(i >> 16);
        if (longbridge == null) return;
        if (longbridge.travellers.indexOf(actor) < 0) longbridge.travellers.add(actor);
    }

    public static void DelTraveller(int i, Actor actor) {
        LongBridge longbridge = getByIdx(i >> 16);
        if (longbridge == null) return;
        int j = longbridge.travellers.indexOf(actor);
        if (j >= 0) longbridge.travellers.remove(j);
    }

    public static void AddSegmentsToRoadPath(int i, ArrayList arraylist, float f, float f1) {
        getByIdx(i).AddSegmentsToRoadPath(arraylist, f, f1);
    }

    private void AddSegmentsToRoadPath(ArrayList arraylist, float f, float f1) {
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Point3d point3d2 = new Point3d();
        Point3d point3d3 = new Point3d();
        int i = 1 + 2 * this.nMidCells;
        int j = arraylist.size();
        this.ComputeSegmentKeyPoints(0, point3d, point3d1, point3d2, point3d3);
        arraylist.add(new RoadSegment(point3d1.x, point3d1.y, point3d1.z, this.width * 0.5F, 0.0D, this.bridgeIdx, 0));
        arraylist.add(new RoadSegment(point3d2.x, point3d2.y, point3d2.z, this.width * 0.5F, 0.0D, this.bridgeIdx, 0));
        for (int k = 1; k < 1 + 2 * this.nMidCells; k++) {
            this.ComputeSegmentKeyPoints(k, point3d, point3d1, point3d2, point3d3);
            arraylist.add(new RoadSegment((float) point3d1.x, (float) point3d1.y, (float) point3d1.z, this.width * 0.5F, 0.0D, this.bridgeIdx, k));
        }

        this.ComputeSegmentKeyPoints(i, point3d, point3d1, point3d2, point3d3);
        arraylist.add(new RoadSegment(point3d1.x, point3d1.y, point3d1.z, this.width * 0.5F, 0.0D, this.bridgeIdx, i));
        arraylist.add(new RoadSegment(point3d2.x, point3d2.y, point3d2.z, this.width * 0.5F, 0.0D, this.bridgeIdx, i));
        arraylist.add(new RoadSegment(point3d3.x, point3d3.y, point3d3.z, this.width * 0.5F, 0.0D, -1, -1));
        Point3d point3d4 = new Point3d(f, f1, 0.0D);
        this.ComputeSegmentKeyPoints(i, point3d, point3d1, point3d2, point3d3);
        Point3d point3d5 = new Point3d(point3d3);
        point3d5.z = 0.0D;
        this.ComputeSegmentKeyPoints(0, point3d, point3d1, point3d2, point3d3);
        point3d1.z = 0.0D;
        boolean flag = point3d4.distanceSquared(point3d5) < point3d4.distanceSquared(point3d1);
        if (flag) {
            int l = 2 + this.nMidCells * 2 + 1 + 2;
            for (int i1 = 0; i1 < l / 2; i1++) {
                RoadSegment roadsegment = (RoadSegment) arraylist.get(j + i1);
                RoadSegment roadsegment1 = (RoadSegment) arraylist.get(j + l - 1 - i1);
                arraylist.set(j + i1, roadsegment1);
                arraylist.set(j + l - 1 - i1, roadsegment);
            }

            for (int j1 = 0; j1 < l - 1; j1++) {
                RoadSegment roadsegment = (RoadSegment) arraylist.get(j + j1);
                RoadSegment roadsegment1 = (RoadSegment) arraylist.get(j + j1 + 1);
                roadsegment.br = roadsegment1.br;
                roadsegment.brSg = roadsegment1.brSg;
                if (j1 == l - 2) {
                    roadsegment1.br = null;
                    roadsegment1.brSg = null;
                }
            }

        }
        RoadSegment roadSegment = (RoadSegment) arraylist.get(arraylist.size() - 1);
        roadSegment.setZ(-1D);
        arraylist.set(arraylist.size() - 1, roadSegment);
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public LongBridge(int i, int j, int k, int l, int i1, int j1, float f) {
        this.bridgeRoad = new BridgeRoad[2];
        this.bridgeIdx = i;
        this.setName(nameOfBridgeByIdx(this.bridgeIdx));
        this.supervisor = null;
        if (Math.abs(f) > 0.41F) { // TODO: Changed by SAS~Storebror, give some margin!
            System.out.println("LongBridge: too big offset (" + Math.abs(f) + ", max=0.4)");
            f = 0.0F;
        }
        this.offsetKoef = f;
        switch (j) {
            case 128:
                this.type = BRIDGE_HIGHWAY;
                this.bodyMaterial = 0;
                break;

            case 32:
                this.type = BRIDGE_COUNTRY;
                this.bodyMaterial = 3;
                break;

            case 64:
                this.type = BRIDGE_RAIL;
                this.bodyMaterial = 2;
                break;

            default:
                System.out.println("LongBridge: wrong type " + this.type);
                break;
        }
        this.winterSuffix = "";
        if (Config.isUSE_RENDER()) {
            World.cur();
            LandConf landconf = World.land().config;
            if (landconf.camouflage.equals("WINTER")) this.winterSuffix = "_W";
            this.mat = Mat.New("maps/_Tex/" + (this.type != 0 ? this.type != 1 ? landconf.rail : landconf.road : landconf.highway) + ".mat");
        }
        this.begX = k;
        this.begY = l;
        this.endX = i1;
        this.endY = j1;
        int k1 = this.endX - this.begX;
        int l1 = this.endY - this.begY;
        if (l1 == 0) {
            this.nMidCells = Math.abs(k1) - 1;
            this.incY = 0;
            this.incX = k1 <= 0 ? -1 : 1;
        } else if (k1 == 0) {
            this.nMidCells = Math.abs(l1) - 1;
            this.incX = 0;
            this.incY = l1 <= 0 ? -1 : 1;
        } else {
            if (Math.abs(k1) != Math.abs(l1)) System.out.println("LongBridge: wrong direction");
            this.nMidCells = Math.abs(l1) - 1;
            this.incX = k1 <= 0 ? -1 : 1;
            this.incY = l1 <= 0 ? -1 : 1;
        }
        this.dirOct = ComputeOctDirection(this.incX, this.incY);
        if (this.nMidCells < 0) System.out.println("LongBridge: zero length");
        this.pos = new ActorPosStatic(this);
        Orient orient = this.ComputeSegmentOrient(false);
        Point3d point3d = this.ComputeSegmentPos3d(0);
        Point3d point3d1 = this.ComputeSegmentPos3d(1 + 2 * this.nMidCells);
        point3d.add(point3d1);
        point3d.scale(0.5D);
        this.pos.setAbs(point3d, orient);
        this.pos.reset();
        this.draw = new LongBridgeDraw();
        this.drawing(false);
        float f1;
        float f2;
        if (this.type != 1) {
            f1 = BRIDGE_SEGM_MAX_LIFE;
            f2 = BRIDGE_SEGM_IGN_TNT;
        } else {
            f1 = BRIDGE_WOODSEGM_MAX_LIFE;
            f2 = BRIDGE_WOODSEGM_IGN_TNT;
        }
        int i2 = 0;
        Point3d point3d2 = this.ComputeSegmentPos3d(i2);
        BridgeSegment bridgeSegment = new BridgeSegment(this, this.bridgeIdx, i2++, f1, f2, point3d2, this.dirOct);
        for (int j2 = 0; j2 < this.nMidCells; j2++) {
            point3d2 = this.ComputeSegmentPos3d(i2);
            new BridgeSegment(this, this.bridgeIdx, i2++, f1, f2, point3d2, this.dirOct);
            point3d2 = this.ComputeSegmentPos3d(i2);
            new BridgeSegment(this, this.bridgeIdx, i2++, f1, f2, point3d2, this.dirOct);
        }

        point3d2 = this.ComputeSegmentPos3d(i2);
        new BridgeSegment(this, this.bridgeIdx, i2++, f1, f2, point3d2, this.dirOct);
        Mesh mesh = bridgeSegment.mesh();
        Matrix4d matrix4d = new Matrix4d();
        int k2 = mesh.hookFind("Top");
        mesh.hookMatrix(k2, matrix4d);
        this.height = (float) matrix4d.m23;
        this.width = 2.0F * Math.abs((float) matrix4d.m13);
        k2 = mesh.hookFind("Mid");
        mesh.hookMatrix(k2, matrix4d);
        this.heightEnd = (float) matrix4d.m23;
        this.lengthEnd = -(float) matrix4d.m03;
        this.lengthEnd += 0.5F * this.SegmentLength();
        this.travellers = new ArrayList();
        Point3d point3d3 = this.ComputeSegmentPos3d(0);
        Point3d point3d4 = this.ComputeSegmentPos3d(1);
        Vector3d vector3d = new Vector3d(point3d4);
        vector3d.sub(point3d3);
        point3d4 = this.ComputeSegmentPos3d(1 + 2 * this.nMidCells);
        float f3 = 2.0F * (float) vector3d.length();
        float f4 = f3 * 0.25F;
        f3 *= 1.4F;
        f3 *= 0.5F;
        vector3d.normalize();
        vector3d.scale(f3);
        point3d3.sub(vector3d);
        point3d4.add(vector3d);
        float f5 = (float) Math.sqrt(f3 * f3 + f4 * f4);
        this.bridgeRoad[0] = new BridgeRoad(point3d3, f5, this.mat, this.type, this.begX, this.begY, this.incX, this.incY, this.offsetKoef);
        this.bridgeRoad[1] = new BridgeRoad(point3d4, f5, this.mat, this.type, this.endX, this.endY, -this.incX, -this.incY, -this.offsetKoef);
        this.net = null;
        if (Mission.cur() != null && (!NetMissionTrack.isPlaying() || NetMissionTrack.playingOriginalVersion() > 102)) {
            int l2 = Mission.cur().getUnitNetIdRemote(this);
            NetChannel netchannel = Mission.cur().getNetMasterChannel();
            if (netchannel == null) this.net = new Master(this);
            else if (l2 != 0) this.net = new Mirror(this, netchannel, l2);
        }
    }

    public void sendLifeChanged(int i, int j, int k, float f, Actor actor, boolean flag) {
        if (this.isNetMirror()) this.sendLifeChanged_mirror(i, j, k, f, actor, flag);
    }

    private void sendLifeChanged_mirror(int i, int j, int k, float f, Actor actor, boolean flag) {
        if (!this.isNetMirror() || this.net.masterChannel() instanceof NetChannelInStream) return;
        try {
            NetMsgFiltered netmsgfiltered = null;
            netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(80);
            netmsgfiltered.writeByte(i);
            netmsgfiltered.writeByte(j);
            netmsgfiltered.writeByte(k);
            netmsgfiltered.writeFloat(f);
            netmsgfiltered.writeBoolean(flag);
            netmsgfiltered.writeNetObj(actor != null ? (NetObj) actor.net : null);

            // TODO: Patch Pack 107, solve bug where the index of a destroyed bridge is always module 256 when a map has more than 256 bridges
            netmsgfiltered.writeShort(i);
            netmsgfiltered.writeShort(j);
            // ---

            netmsgfiltered.setIncludeTime(false);
            this.net.postTo(NetServerParams.getServerTime(), this.net.masterChannel(), netmsgfiltered);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static final int    BRIDGE_HIGHWAY           = 0;
    public static final int    BRIDGE_COUNTRY           = 1;
    public static final int    BRIDGE_RAIL              = 2;
    public static final int    N_BRIDGE_TYPES           = 3;
    private static final float BRIDGE_SEGM_MAX_LIFE     = 240F;
    private static final float BRIDGE_WOODSEGM_MAX_LIFE = 120F;
    private static final float BRIDGE_SEGM_IGN_TNT      = 100F;
    private static final float BRIDGE_WOODSEGM_IGN_TNT  = 10F;
    private int                type;
    public int                 bodyMaterial;
    private int                bridgeIdx;
    private int                begX;
    private int                begY;
    private int                endX;
    private int                endY;
    private int                nMidCells;
    private float              offsetKoef;
    private int                incX;
    private int                incY;
    private int                dirOct;
    private float              width;
    private float              height;
    private float              heightEnd;
    private float              lengthEnd;
    private Mat                mat;
    private BridgeRoad         bridgeRoad[];
    private ArrayList          travellers;
    private Actor              supervisor;
    private String             winterSuffix;

}
