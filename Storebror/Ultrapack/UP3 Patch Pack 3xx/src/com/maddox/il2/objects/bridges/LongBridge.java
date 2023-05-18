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

public class LongBridge extends Actor
    implements LandPlate
{
    class Mirror extends ActorNet
    {

        public Mirror(Actor actor, NetChannel netchannel, int i)
        {
            super(actor, netchannel, i);
        }
    }

    class Master extends ActorNet
    {

        public boolean netInput(NetMsgInput netmsginput)
            throws IOException
        {
            if(netmsginput.isGuaranted())
                return true;
            if(netmsginput.readUnsignedByte() != 80)
            {
                return false;
            } else
            {
                int i = netmsginput.readUnsignedByte();
                int j = netmsginput.readUnsignedByte();
                int k = netmsginput.readUnsignedByte();
                float f = netmsginput.readFloat();
                /*boolean flag = */netmsginput.readBoolean();
                NetObj netobj = netmsginput.readNetObj();
                
                //TODO: Patch Pack 107, solve bug where the index of a destroyed bridge is always module 256 when a map has more than 256 bridges
                if (netmsginput.available() > 3) {
                    i = netmsginput.readUnsignedShort();
                    j = netmsginput.readUnsignedShort();
                }
                //---
                
                Actor actor = netobj != null ? ((ActorNet)netobj).actor() : null;
                BridgeSegment bridgesegment = BridgeSegment.getByIdx(i, j);
                bridgesegment.netLifeChanged(k, f, actor, true);
                return true;
            }
        }

        public Master(Actor actor)
        {
            super(actor);
        }
    }

    private class LongBridgeDraw extends ActorDraw
    {

        public int preRender(Actor actor)
        {
            Point3d point3d = new Point3d();
            pos.getRender(point3d);
            float f = (float)Math.sqrt((float)((endX - begX) * (endX - begX) + (endY - begY) * (endY - begY)));
            f *= 0.5F;
            f += 1.988F;
            f *= 200F;
            f += 500F;
            if(!Render.currentCamera().isSphereVisible(point3d, f))
                return 0;
            else
                return mat.preRender();
        }

        public void render(Actor actor)
        {
            char c;
            switch(type)
            {
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
            Point3d point3d = ComputeSegmentPos3d(0);
            Point3d point3d1 = ComputeSegmentPos3d(1 + 2 * nMidCells);
            float f = 284F;
            float f1 = f * 0.25F + f * 1.4F;
            f1 += 500F;
            if(Render.currentCamera().isSphereVisible(point3d, f1))
            {
                World.cur();
                World.land().renderBridgeRoad(mat, c, begX, begY, incX, incY, offsetKoef);
            }
            if(Render.currentCamera().isSphereVisible(point3d1, f1))
            {
                World.cur();
                World.land().renderBridgeRoad(mat, c, endX, endY, -incX, -incY, -offsetKoef);
            }
        }

        private LongBridgeDraw()
        {
        }

    }


    public int type()
    {
        return type;
    }

    public boolean isStaticPos()
    {
        return true;
    }

    public int bridgeIdx()
    {
        return bridgeIdx;
    }

    private static String nameOfBridgeByIdx(int i)
    {
        return " Bridge" + i;
    }

    public static LongBridge getByIdx(int i)
    {
        return (LongBridge)Actor.getByName(nameOfBridgeByIdx(i));
    }

    public boolean isUsableFor(Actor actor)
    {
        return supervisor == null || supervisor == actor;
    }

    public void setSupervisor(Actor actor)
    {
        supervisor = actor;
    }

    public void resetSupervisor(Actor actor)
    {
        if(supervisor == actor)
            supervisor = null;
    }

    public float getWidth()
    {
        return width;
    }

    private String SegmentMeshName(boolean flag, int i)
    {
        return "3do/bridges/" + (type != 0 ? type != 1 ? "rail" : "country" : "highway") + winterSuffix + "/" + ((dirOct & 1) != 0 ? "long/" : "short/") + (flag ? "mid/" : "end/") + (i != 0 ? i != 3 ? "mono2" : "mono3" : "mono1") + ".sim";
    }

    private static int ComputeOctDirection(int i, int j)
    {
        byte byte0;
        if(i > 0)
            byte0 = j <= 0 ? ((byte) (((byte)(j >= 0 ? 0 : 1)))) : 7;
        else
        if(i < 0)
            byte0 = j <= 0 ? ((byte)(j >= 0 ? 4 : 3)) : 5;
        else
            byte0 = ((byte)(j <= 0 ? 2 : 6));
        return byte0;
    }

    private Orient ComputeSegmentOrient(boolean flag)
    {
        float f = (float)dirOct * 45F;
        if(!flag)
        {
            f += 180F;
            if(f >= 360F)
                f -= 360F;
        }
        Orient orient = new Orient();
        orient.setYPR(f, 0.0F, 0.0F);
        return orient;
    }

    private Point3d ComputeSegmentPos3d(int i)
    {
        float f;
        float f1;
        boolean flag;
        if(i == 0)
        {
            f = begX;
            f1 = begY;
            flag = true;
        } else
        if(i == 1 + 2 * nMidCells)
        {
            f = begX + incX * (1 + nMidCells);
            f1 = begY + incY * (1 + nMidCells);
            flag = false;
        } else
        {
            f = begX + incX * (i + 1 >> 1);
            f1 = begY + incY * (i + 1 >> 1);
            flag = (i & 1) == 0;
        }
        float f2 = offsetKoef + (flag ? 0.25F : -0.25F);
        float f3 = f + 0.5F + (float)incX * f2;
        float f4 = f1 + 0.5F + (float)incY * f2;
        Point3d point3d = new Point3d();
        point3d.x = World.land().PIX2WORLDX(f3);
        point3d.y = World.land().PIX2WORLDY(f4);
        point3d.z = 0.0D;
        return point3d;
    }

    private float SegmentLength()
    {
        return 200F * ((dirOct & 1) != 0 ? 0.7071F : 0.5F);
    }

    void ComputeSegmentKeyPoints(int i, Point3d point3d, Point3d point3d1, Point3d point3d2, Point3d point3d3)
    {
        boolean flag = i == 0;
        boolean flag1 = i == 1 + 2 * nMidCells;
//        boolean flag2 = !flag && !flag1;
        point3d1.set(ComputeSegmentPos3d(0));
        point3d3.set(ComputeSegmentPos3d(1));
        float f = (float)(point3d3.x - point3d1.x);
        float f1 = (float)(point3d3.y - point3d1.y);
        float f2 = 1.0F / (float)Math.sqrt(f * f + f1 * f1);
        point3d.set(ComputeSegmentPos3d(i));
        point3d1.set(point3d);
        point3d3.set(point3d);
        point3d1.sub(0.5D * (double)f, 0.5D * (double)f1, 0.0D);
        point3d3.add(0.5D * (double)f, 0.5D * (double)f1, 0.0D);
        if(flag)
        {
            point3d2.set(point3d1);
            point3d2.add(f * f2 * lengthEnd, f1 * f2 * lengthEnd, 0.0D);
            point3d1.z = 0.0D;
            point3d2.z = heightEnd;
            point3d3.z = height;
        } else
        if(flag1)
        {
            point3d2.set(point3d3);
            point3d2.sub(f * f2 * lengthEnd, f1 * f2 * lengthEnd, 0.0D);
            point3d1.z = height;
            point3d2.z = heightEnd;
            point3d3.z = 0.0D;
        } else
        {
            point3d2.set(point3d);
            point3d1.z = height;
            point3d2.z = height;
            point3d3.z = height;
        }
    }

    void ShowSegmentExplosion(BridgeSegment bridgesegment, int i, int j)
    {
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Point3d point3d2 = new Point3d();
        Point3d point3d3 = new Point3d();
        ComputeSegmentKeyPoints(i, point3d, point3d1, point3d2, point3d3);
        Point3d point3d4;
        Point3d point3d5;
        if(j == 0)
        {
            point3d4 = point3d1;
            point3d5 = point3d2;
        } else
        {
            point3d4 = point3d2;
            point3d5 = point3d3;
        }
        Explosions.ExplodeBridge(point3d4, point3d5, width);
    }

    public int NumStateBits()
    {
        return 2 * (1 + nMidCells + 1);
    }

    public BitSet GetStateOfSegments()
    {
        Object bridgeSegment[] = getOwnerAttached();
        BitSet bitset = new BitSet(2 * bridgeSegment.length);
        for(int i = 0; i < bridgeSegment.length; i++)
        {
            boolean flag = ((BridgeSegment)bridgeSegment[i]).IsDead(0);
            boolean flag1 = ((BridgeSegment)bridgeSegment[i]).IsDead(1);
            if(flag)
                bitset.set(i * 2 + 0);
            if(flag1)
                bitset.set(i * 2 + 1);
        }

        return bitset;
    }

    public void SetStateOfSegments(BitSet bitset)
    {
        Object bridgeSegment[] = getOwnerAttached();
        for(int i = 0; i < bridgeSegment.length; i++)
        {
            boolean flag1 = bitset.get(i * 2 + 0);
            boolean flag2 = bitset.get(i * 2 + 1);
            if(flag1 && i > 0)
                bitset.set((i - 1) * 2 + 1);
            if(flag2 && i < bridgeSegment.length - 1)
                bitset.set((i + 1) * 2 + 0);
        }

        boolean flag = false;
        for(int j = 0; j < bridgeSegment.length; j++)
        {
            boolean flag3 = bitset.get(j * 2 + 0);
            boolean flag4 = bitset.get(j * 2 + 1);
            if(flag3 || flag4)
                flag = true;
            ((BridgeSegment)bridgeSegment[j]).ForcePartState(0, !flag3);
            ((BridgeSegment)bridgeSegment[j]).ForcePartState(1, !flag4);
        }

        setDiedFlag(flag);
    }

    public void BeLive()
    {
        BitSet bitset = new BitSet(NumStateBits());
        SetStateOfSegments(bitset);
    }

    void SetSegmentDamageState(boolean flag, BridgeSegment bridgesegment, int i, float f, float f1, Actor actor)
    {
        int j = (f1 > 0.0F ? 0 : 2) + (f > 0.0F ? 0 : 1);
        boolean flag1 = i == 0;
        boolean flag2 = i == 1 + 2 * nMidCells;
        boolean flag3 = !flag1 && !flag2;
        boolean flag4 = flag2;
        if(flag3 && j == 2)
            flag4 = true;
        Orient orient = ComputeSegmentOrient(flag4);
        bridgesegment.pos.setAbs(orient);
        bridgesegment.pos.reset();
        bridgesegment.setMesh(MeshShared.get(SegmentMeshName(flag3, j)));
        bridgesegment.collide(true);
        bridgesegment.drawing(true);
        if(j != 0 && isAlive())
            World.onActorDied(this, actor);
        if(j != 0 && travellers.size() > 0)
        {
            Object aobj[] = travellers.toArray();
            for(int k = 0; k < aobj.length; k++)
            {
                if(aobj[k] instanceof ChiefGround)
                {
                    ((ChiefGround)aobj[k]).BridgeSegmentDestroyed(bridgeIdx, i, actor);
                    continue;
                }
                if(aobj[k] instanceof Train)
                    ((Train)aobj[k]).BridgeSegmentDestroyed(bridgeIdx, i, actor);
                else
                    ((UnitInterface)aobj[k]).absoluteDeath(actor);
            }

        }
        if(!flag)
            return;
        if(j == 0)
            return;
        Object bridgeSegment[] = getOwnerAttached();
        int l = 1 + 2 * nMidCells + 1;
        Actor actor1 = actor;
        if((j & 1) != 0 && i > 0)
            ((BridgeSegment)bridgeSegment[i - 1]).ForcePartDestroing(1, actor1);
        if((j & 2) != 0 && i < l - 1)
            ((BridgeSegment)bridgeSegment[i + 1]).ForcePartDestroing(0, actor1);
        if(flag1 && (j & 1) != 0)
            ((BridgeSegment)bridgeSegment[i]).ForcePartDestroing(1, actor1);
        if(flag2 && (j & 2) != 0)
            ((BridgeSegment)bridgeSegment[i]).ForcePartDestroing(0, actor1);
    }

    public void destroy()
    {
        Object bridgeSegment[] = getOwnerAttached();
        for(int i = 0; i < bridgeSegment.length; i++)
            ((BridgeSegment)bridgeSegment[i]).destroy();

        for(int j = 0; j < 2; j++)
            if(Actor.isValid(bridgeRoad[j]))
            {
                bridgeRoad[j].destroy();
                bridgeRoad[j] = null;
            }

        super.destroy();
    }

    public static void AddTraveller(int i, Actor actor)
    {
        if(!(actor instanceof UnitInterface) && !(actor instanceof ChiefGround) && !(actor instanceof Train))
        {
            System.out.println("Error: traveller type");
            return;
        }
        LongBridge longbridge = getByIdx(i >> 16);
        if(longbridge == null)
            return;
        if(longbridge.travellers.indexOf(actor) < 0)
            longbridge.travellers.add(actor);
    }

    public static void DelTraveller(int i, Actor actor)
    {
        LongBridge longbridge = getByIdx(i >> 16);
        if(longbridge == null)
            return;
        int j = longbridge.travellers.indexOf(actor);
        if(j >= 0)
            longbridge.travellers.remove(j);
    }

    public static void AddSegmentsToRoadPath(int i, ArrayList arraylist, float f, float f1)
    {
        getByIdx(i).AddSegmentsToRoadPath(arraylist, f, f1);
    }

    private void AddSegmentsToRoadPath(ArrayList arraylist, float f, float f1)
    {
        Point3d point3d = new Point3d();
        Point3d point3d1 = new Point3d();
        Point3d point3d2 = new Point3d();
        Point3d point3d3 = new Point3d();
        int i = 1 + 2 * nMidCells;
        int j = arraylist.size();
        ComputeSegmentKeyPoints(0, point3d, point3d1, point3d2, point3d3);
        arraylist.add(new RoadSegment(point3d1.x, point3d1.y, point3d1.z, width * 0.5F, 0.0D, bridgeIdx, 0));
        arraylist.add(new RoadSegment(point3d2.x, point3d2.y, point3d2.z, width * 0.5F, 0.0D, bridgeIdx, 0));
        for(int k = 1; k < 1 + 2 * nMidCells; k++)
        {
            ComputeSegmentKeyPoints(k, point3d, point3d1, point3d2, point3d3);
            arraylist.add(new RoadSegment((float)point3d1.x, (float)point3d1.y, (float)point3d1.z, width * 0.5F, 0.0D, bridgeIdx, k));
        }

        ComputeSegmentKeyPoints(i, point3d, point3d1, point3d2, point3d3);
        arraylist.add(new RoadSegment(point3d1.x, point3d1.y, point3d1.z, width * 0.5F, 0.0D, bridgeIdx, i));
        arraylist.add(new RoadSegment(point3d2.x, point3d2.y, point3d2.z, width * 0.5F, 0.0D, bridgeIdx, i));
        arraylist.add(new RoadSegment(point3d3.x, point3d3.y, point3d3.z, width * 0.5F, 0.0D, -1, -1));
        Point3d point3d4 = new Point3d(f, f1, 0.0D);
        ComputeSegmentKeyPoints(i, point3d, point3d1, point3d2, point3d3);
        Point3d point3d5 = new Point3d(point3d3);
        point3d5.z = 0.0D;
        ComputeSegmentKeyPoints(0, point3d, point3d1, point3d2, point3d3);
        point3d1.z = 0.0D;
        boolean flag = point3d4.distanceSquared(point3d5) < point3d4.distanceSquared(point3d1);
        if(flag)
        {
            int l = 2 + nMidCells * 2 + 1 + 2;
            for(int i1 = 0; i1 < l / 2; i1++)
            {
                RoadSegment roadsegment = (RoadSegment)arraylist.get(j + i1);
                RoadSegment roadsegment1 = (RoadSegment)arraylist.get((j + l) - 1 - i1);
                arraylist.set(j + i1, roadsegment1);
                arraylist.set((j + l) - 1 - i1, roadsegment);
            }

            for(int j1 = 0; j1 < l - 1; j1++)
            {
                RoadSegment roadsegment = (RoadSegment)arraylist.get(j + j1);
                RoadSegment roadsegment1 = (RoadSegment)arraylist.get(j + j1 + 1);
                roadsegment.br = roadsegment1.br;
                roadsegment.brSg = roadsegment1.brSg;
                if(j1 == l - 2)
                {
                    roadsegment1.br = null;
                    roadsegment1.brSg = null;
                }
            }

        }
        RoadSegment roadSegment = (RoadSegment)arraylist.get(arraylist.size() - 1);
        roadSegment.setZ(-1D);
        arraylist.set(arraylist.size() - 1, roadSegment);
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    public LongBridge(int i, int j, int k, int l, int i1, int j1, float f)
    {
        bridgeRoad = new BridgeRoad[2];
        bridgeIdx = i;
        setName(nameOfBridgeByIdx(bridgeIdx));
        supervisor = null;
        if(Math.abs(f) > 0.4F)
        {
            System.out.println("LongBridge: too big offset");
            f = 0.0F;
        }
        offsetKoef = f;
        switch(j)
        {
        case 128: 
            type = BRIDGE_HIGHWAY;
            bodyMaterial = 0;
            break;

        case 32: // ' '
            type = BRIDGE_COUNTRY;
            bodyMaterial = 3;
            break;

        case 64: // '@'
            type = BRIDGE_RAIL;
            bodyMaterial = 2;
            break;

        default:
            System.out.println("LongBridge: wrong type " + type);
            break;
        }
        winterSuffix = "";
        if(Config.isUSE_RENDER())
        {
            World.cur();
            LandConf landconf = World.land().config;
            if(landconf.camouflage.equals("WINTER"))
                winterSuffix = "_W";
            mat = Mat.New("maps/_Tex/" + (type != 0 ? type != 1 ? landconf.rail : landconf.road : landconf.highway) + ".mat");
        }
        begX = k;
        begY = l;
        endX = i1;
        endY = j1;
        int k1 = endX - begX;
        int l1 = endY - begY;
        if(l1 == 0)
        {
            nMidCells = Math.abs(k1) - 1;
            incY = 0;
            incX = k1 <= 0 ? -1 : 1;
        } else
        if(k1 == 0)
        {
            nMidCells = Math.abs(l1) - 1;
            incX = 0;
            incY = l1 <= 0 ? -1 : 1;
        } else
        {
            if(Math.abs(k1) != Math.abs(l1))
                System.out.println("LongBridge: wrong direction");
            nMidCells = Math.abs(l1) - 1;
            incX = k1 <= 0 ? -1 : 1;
            incY = l1 <= 0 ? -1 : 1;
        }
        dirOct = ComputeOctDirection(incX, incY);
        if(nMidCells < 0)
            System.out.println("LongBridge: zero length");
        pos = new ActorPosStatic(this);
        Orient orient = ComputeSegmentOrient(false);
        Point3d point3d = ComputeSegmentPos3d(0);
        Point3d point3d1 = ComputeSegmentPos3d(1 + 2 * nMidCells);
        point3d.add(point3d1);
        point3d.scale(0.5D);
        pos.setAbs(point3d, orient);
        pos.reset();
        draw = new LongBridgeDraw();
        drawing(false);
        float f1;
        float f2;
        if(type != 1)
        {
            f1 = BRIDGE_SEGM_MAX_LIFE;
            f2 = BRIDGE_SEGM_IGN_TNT;
        } else
        {
            f1 = BRIDGE_WOODSEGM_MAX_LIFE;
            f2 = BRIDGE_WOODSEGM_IGN_TNT;
        }
        int i2 = 0;
        Point3d point3d2 = ComputeSegmentPos3d(i2);
        BridgeSegment bridgeSegment = new BridgeSegment(this, bridgeIdx, i2++, f1, f2, point3d2, dirOct);
        for(int j2 = 0; j2 < nMidCells; j2++)
        {
            point3d2 = ComputeSegmentPos3d(i2);
            new BridgeSegment(this, bridgeIdx, i2++, f1, f2, point3d2, dirOct);
            point3d2 = ComputeSegmentPos3d(i2);
            new BridgeSegment(this, bridgeIdx, i2++, f1, f2, point3d2, dirOct);
        }

        point3d2 = ComputeSegmentPos3d(i2);
        new BridgeSegment(this, bridgeIdx, i2++, f1, f2, point3d2, dirOct);
        Mesh mesh = bridgeSegment.mesh();
        Matrix4d matrix4d = new Matrix4d();
        int k2 = mesh.hookFind("Top");
        mesh.hookMatrix(k2, matrix4d);
        height = (float)matrix4d.m23;
        width = 2.0F * Math.abs((float)matrix4d.m13);
        k2 = mesh.hookFind("Mid");
        mesh.hookMatrix(k2, matrix4d);
        heightEnd = (float)matrix4d.m23;
        lengthEnd = -(float)matrix4d.m03;
        lengthEnd += 0.5F * SegmentLength();
        travellers = new ArrayList();
        Point3d point3d3 = ComputeSegmentPos3d(0);
        Point3d point3d4 = ComputeSegmentPos3d(1);
        Vector3d vector3d = new Vector3d(point3d4);
        vector3d.sub(point3d3);
        point3d4 = ComputeSegmentPos3d(1 + 2 * nMidCells);
        float f3 = 2.0F * (float)vector3d.length();
        float f4 = f3 * 0.25F;
        f3 *= 1.4F;
        f3 *= 0.5F;
        vector3d.normalize();
        vector3d.scale(f3);
        point3d3.sub(vector3d);
        point3d4.add(vector3d);
        float f5 = (float)Math.sqrt(f3 * f3 + f4 * f4);
        bridgeRoad[0] = new BridgeRoad(point3d3, f5, mat, type, begX, begY, incX, incY, offsetKoef);
        bridgeRoad[1] = new BridgeRoad(point3d4, f5, mat, type, endX, endY, -incX, -incY, -offsetKoef);
        net = null;
        if(Mission.cur() != null && (!NetMissionTrack.isPlaying() || NetMissionTrack.playingOriginalVersion() > 102))
        {
            int l2 = Mission.cur().getUnitNetIdRemote(this);
            NetChannel netchannel = Mission.cur().getNetMasterChannel();
            if(netchannel == null)
                net = new Master(this);
            else
            if(l2 != 0)
                net = new Mirror(this, (NetChannel)netchannel, l2);
        }
    }

    public void sendLifeChanged(int i, int j, int k, float f, Actor actor, boolean flag)
    {
        if(isNetMirror())
            sendLifeChanged_mirror(i, j, k, f, actor, flag);
    }

    private void sendLifeChanged_mirror(int i, int j, int k, float f, Actor actor, boolean flag)
    {
        if(!isNetMirror() || (net.masterChannel() instanceof NetChannelInStream))
            return;
        try
        {
            NetMsgFiltered netmsgfiltered = null;
            netmsgfiltered = new NetMsgFiltered();
            netmsgfiltered.writeByte(80);
            netmsgfiltered.writeByte(i);
            netmsgfiltered.writeByte(j);
            netmsgfiltered.writeByte(k);
            netmsgfiltered.writeFloat(f);
            netmsgfiltered.writeBoolean(flag);
            netmsgfiltered.writeNetObj(actor != null ? ((NetObj) (actor.net)) : null);
            
            //TODO: Patch Pack 107, solve bug where the index of a destroyed bridge is always module 256 when a map has more than 256 bridges
            netmsgfiltered.writeShort(i);
            netmsgfiltered.writeShort(j);
            //---
            
            netmsgfiltered.setIncludeTime(false);
            net.postTo(NetServerParams.getServerTime(), net.masterChannel(), netmsgfiltered);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static final int BRIDGE_HIGHWAY = 0;
    public static final int BRIDGE_COUNTRY = 1;
    public static final int BRIDGE_RAIL = 2;
    public static final int N_BRIDGE_TYPES = 3;
    private static final float BRIDGE_SEGM_MAX_LIFE = 240F;
    private static final float BRIDGE_WOODSEGM_MAX_LIFE = 120F;
    private static final float BRIDGE_SEGM_IGN_TNT = 100F;
    private static final float BRIDGE_WOODSEGM_IGN_TNT = 10F;
    private int type;
    public int bodyMaterial;
    private int bridgeIdx;
    private int begX;
    private int begY;
    private int endX;
    private int endY;
    private int nMidCells;
    private float offsetKoef;
    private int incX;
    private int incY;
    private int dirOct;
    private float width;
    private float height;
    private float heightEnd;
    private float lengthEnd;
    private Mat mat;
    private BridgeRoad bridgeRoad[];
    private ArrayList travellers;
    private Actor supervisor;
    private String winterSuffix;











}
