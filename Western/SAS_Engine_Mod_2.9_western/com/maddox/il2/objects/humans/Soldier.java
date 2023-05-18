// Source File Name:   Soldier.java
// Last edited by western on 29th/Sep./2020
// By western: add "disappeartime set by the carrier" on 28th/Sep./2020
// By western: add "run return to the carrier" on 29th/Sep./2020

package com.maddox.il2.objects.humans;

import com.maddox.JGP.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.ai.ground.Prey;
import com.maddox.il2.ai.ground.UnitInterface;
import com.maddox.il2.engine.*;
import com.maddox.rts.Message;
import com.maddox.rts.Time;

public class Soldier extends ActorMesh
    implements MsgCollisionRequestListener, MsgCollisionListener, MsgExplosionListener, MsgShotListener, Prey
{
    class Move extends Interpolate
    {

        public boolean tick()
        {
            // +++ western: add ST_RETRUN
            if((st == ST_LIE || st == ST_LIEDEAD || bReturn && st == ST_RETRUN) && Time.current() >= disappearTime)
            {
                postDestroy();
                return false;
            }
            if(dying != 0)
            {
                switch(st)
                {
                case 1: // ST_RUN
                case 9: // ST_RETRUN    // +++ western add
                    st = ST_FALL;
                    animStartTime = Time.current();
                    break;

                case 3: // ST_LIE
                    st = ST_LIEDEAD;
                    idxOfDeadPose = World.Rnd().nextInt(0, 3);
                    break;
                }
                setAnimFrame(Time.tickNext());
            }
            long l = Time.tickNext() - animStartTime;
            switch(st)
            {
            default:
                break;

            case 0: // ST_FLY
                pos.getAbs(Soldier.p);
                Soldier.p.scaleAdd(Time.tickLenFs(), speed, Soldier.p);
                speed.z -= Time.tickLenFs() * World.g();
                Engine.land();
                float f = Landscape.HQ((float)Soldier.p.x, (float)Soldier.p.y);
                if((float)Soldier.p.z <= f)
                {
                    speed.z = 0.0D;
                    speed.normalize();
                    speed.scale(RUN_SPEEDD);
                    Soldier.p.z = f;
                    st = ST_RUN;
                    nRunCycles = World.Rnd().nextInt(9, 17);
                }
                pos.setAbs(Soldier.p);
                break;

            case 1: // ST_RUN
                pos.getAbs(Soldier.p);
                Soldier.p.scaleAdd(Time.tickLenFs(), speed, Soldier.p);
                Soldier.p.z = Engine.land().HQ(Soldier.p.x, Soldier.p.y);
                pos.setAbs(Soldier.p);
                if(l / (long)RUN_CYCLE_TIME >= (long)nRunCycles || World.land().isWater(Soldier.p.x, Soldier.p.y))
                {
                    st = ST_FALL;
                    runRequiredTime = Time.current() - animStartTime;  // +++ western: add return function
                    animStartTime = Time.current();
                }
                // +++ western: add return function
                if(bReturn && Time.current() - animStartTime > (long)((double)disappearPeriod * 0.52D))
                {
                    st = ST_RETRUN;
                    pos.getAbs(Soldier.p, Soldier.o);
                    Soldier.o.set(Soldier.o.azimut() + 180F, 0.0F, 0.0F);
                    pos.setAbs(Soldier.p, Soldier.o);
                    speed.set(-speed.x, -speed.y, -speed.z);
                    animStartTime = Time.current();
                }
                // --- western
                break;

            case 9: // ST_RETRUN        // +++ western: add ST_RETRUN
                pos.getAbs(Soldier.p);
                Soldier.p.scaleAdd(Time.tickLenFs(), speed, Soldier.p);
                Soldier.p.z = Engine.land().HQ(Soldier.p.x, Soldier.p.y);
                pos.setAbs(Soldier.p);
                if(World.land().isWater(Soldier.p.x, Soldier.p.y))
                {
                    st = ST_FALL;
                    animStartTime = Time.current();
                }
                break;
            // --- western

            case 2: // ST_FALL
                pos.getAbs(Soldier.p);
                Soldier.p.scaleAdd(Time.tickLenFs(), speed, Soldier.p);
                Soldier.p.z = Engine.land().HQ(Soldier.p.x, Soldier.p.y);
                if(World.land().isWater(Soldier.p.x, Soldier.p.y))
                    Soldier.p.z -= 0.5D;
                pos.setAbs(Soldier.p);
                if(l >= (long)FALL_CYCLE_TIME)
                {
                    st = ST_LIE;
                    animStartTime = Time.current();
                    // +++ By western: when disappear time is set, not to reset it
                    if(disappearTime < Time.current())
                        disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
                    // ---
                }
                break;

            case 3: // ST_LIE
                // +++ western: add return function
                if(bReturn && dying == 0 && Time.current() > disappearTime - runRequiredTime)
                {
                    st = ST_RETRUN;
                    pos.getAbs(Soldier.p, Soldier.o);
                    Soldier.o.set(Soldier.o.azimut() + 180F, 0.0F, 0.0F);
                    pos.setAbs(Soldier.p, Soldier.o);
                    speed.set(-speed.x, -speed.y, -speed.z);
                    animStartTime = Time.current();
                    break;
                }
                // --- western
                // fall through
            case 4: // ST_LIEDEAD
                pos.getAbs(Soldier.p);
                Soldier.p.z = Engine.land().HQ(Soldier.p.x, Soldier.p.y);
                if(World.land().isWater(Soldier.p.x, Soldier.p.y))
                    Soldier.p.z -= 3D;
                pos.setAbs(Soldier.p);
                break;
            }
            setAnimFrame(Time.tickNext());
            return true;
        }

        Move()
        {
        }
    }

    private class SoldDraw extends ActorMeshDraw
    {

        public int preRender(Actor actor)
        {
            setAnimFrame(Time.current());
            return super.preRender(actor);
        }

        private SoldDraw()
        {
        }

    }


    public static void resetGame()
    {
        preload1 = preload2 = null;
    }

    public static void PRELOAD()
    {
        preload1 = new Mesh(GetMeshName(1));
        preload2 = new Mesh(GetMeshName(2));
    }

    public void msgCollisionRequest(Actor actor, boolean aflag[])
    {
        if(actor == getOwner())
            aflag[0] = false;
        if(actor instanceof Soldier)
            aflag[0] = false;
        if(dying != 0)
            aflag[0] = false;
    }

    public void msgCollision(Actor actor, String s, String s1)
    {
        if(dying != 0)
            return;
        Point3d point3d = p;
        pos.getAbs(p);
        Point3d point3d1 = actor.pos.getAbsPoint();
        Vector3d vector3d = new Vector3d();
        vector3d.set(point3d.x - point3d1.x, point3d.y - point3d1.y, 0.0D);
        if(vector3d.length() < 0.001D)
        {
            float f = World.Rnd().nextFloat(0.0F, 359.99F);
            vector3d.set(Geom.sinDeg(f), Geom.cosDeg(f), 0.0D);
        }
        vector3d.normalize();
        float f1 = 0.2F;
        vector3d.add(World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1), World.Rnd().nextFloat(-f1, f1));
        vector3d.normalize();
        float f2 = 13.09091F * Time.tickLenFs();
        vector3d.scale(f2);
        point3d.add(vector3d);
        pos.setAbs(point3d);
        // +++ western: add ST_RETRUN
        if(st == ST_RUN || st == ST_RETRUN)
        {
            st = ST_FALL;
            animStartTime = Time.current();
        }
        if(st == ST_LIE && dying == 0 && (actor instanceof UnitInterface) && actor.getSpeed(null) > 0.5D)
            Die(actor);
    }

    public void msgShot(Shot shot)
    {
        shot.bodyMaterial = 3;
        if(dying != 0)
            return;
        if(shot.power <= 0.0F)
            return;
        if(shot.powerType == 1)
        {
            Die(shot.initiator);
            return;
        }
        if(shot.v.length() < 20D)
        {
            return;
        } else
        {
            Die(shot.initiator);
            return;
        }
    }

    public void msgExplosion(Explosion explosion)
    {
        if(dying != 0)
            return;
        float f = 0.005F;
        float f1 = 0.1F;
        Explosion _tmp = explosion;
        if(Explosion.killable(this, explosion.receivedTNT_1meter(this), f, f1, 0.0F))
            Die(explosion.initiator);
    }

    private void Die(Actor actor)
    {
        if(dying != 0)
        {
            return;
        } else
        {
            World.onActorDied(this, actor);
            dying = 1;
            return;
        }
    }

    public void destroy()
    {
        super.destroy();
    }

    public Object getSwitchListener(Message message)
    {
        return this;
    }

    private void setAnimFrame(double d)
    {
        int i;
        int j;
        float f;
        switch(st)
        {
        case 0: // ST_FLY
        case 1: // ST_RUN
        case 9: // ST_RETRUN            // +++ western: add ST_RETRUN
            i = RUN_START_FRAME;
            j = RUN_LAST_FRAME;
            int k = RUN_CYCLE_TIME;
            double d1 = d - (double)animStartTime;
            d1 %= k;
            if(d1 < 0.0D)
                d1 += k;
            f = (float)(d1 / (double)k);
            break;

        case 2: // ST_FALL
            i = FALL_START_FRAME;
            j = FALL_LAST_FRAME;
            int l = FALL_CYCLE_TIME;
            double d2 = d - (double)animStartTime;
            if(d2 <= 0.0D)
            {
                f = 0.0F;
                break;
            }
            if(d2 >= (double)l)
                f = 1.0F;
            else
                f = (float)(d2 / (double)l);
            break;

        case 3: // ST_LIE
            i = LIE_START_FRAME;
            j = LIE_LAST_FRAME;
            int i1 = LIE_CYCLE_TIME;
            double d3 = d - (double)animStartTime;
            if(d3 <= 0.0D)
            {
                f = 0.0F;
                break;
            }
            if(d3 >= (double)i1)
                f = 1.0F;
            else
                f = (float)(d3 / (double)i1);
            break;

        default: // ST_LIEDEAD
            i = j = LIEDEAD_START_FRAME + idxOfDeadPose;
            f = 0.0F;
            break;
        }
        mesh().setFrameFromRange(i, j, f);
    }

    public int HitbyMask()
    {
        return -25;
    }

    public int chooseBulletType(BulletProperties abulletproperties[])
    {
        if(dying != 0)
            return -1;
        if(abulletproperties.length == 1)
            return 0;
        if(abulletproperties.length <= 0)
            return -1;
        if(abulletproperties[0].power <= 0.0F)
            return 1;
        if(abulletproperties[0].powerType == 1)
            return 0;
        if(abulletproperties[1].powerType == 1)
            return 1;
        if(abulletproperties[0].cumulativePower > 0.0F)
            return 1;
        return abulletproperties[0].powerType != 2 ? 0 : 1;
    }

    public int chooseShotpoint(BulletProperties bulletproperties)
    {
        return dying == 0 ? 0 : -1;
    }

    public boolean getShotpointOffset(int i, Point3d point3d)
    {
        if(dying != 0)
            return false;
        if(i != 0)
            return false;
        if(point3d != null)
            point3d.set(0.0D, 0.0D, 0.0D);
        return true;
    }

    private static String GetMeshName(int i)
    {
        boolean flag = i == 2;
        boolean flag1 = World.cur().camouflage == 1;
        return "3do/humans/soldiers/" + (flag ? "Germany" : "Russia") + (flag1 ? "Winter" : "Summer") + "/mono.sim";
    }

    public Soldier(Actor actor, int i, Loc loc)
    {
        super(GetMeshName(i));
        st = 0;
        dying = 0;
        runRequiredTime = 0L;
        bReturn = false;
        setOwner(actor);
        setArmy(i);
        Point3d point3d = new Point3d();
        Orient orient = new Orient();
        loc.get(point3d, orient);
        Vector3d vector3d = new Vector3d();
        vector3d.set(1.0D, 0.0D, 0.0D);
        orient.transform(vector3d);
        speed = new Vector3d();
        speed.set(vector3d);
        if(speed.length() < 0.0099999997764825821D)
            speed.set(1.0D, 0.0D, 0.0D);
        speed.normalize();
        if(Math.abs(speed.z) > 0.90D)
        {
            speed.set(1.0D, 0.0D, 0.0D);
            speed.normalize();
        }
        orient.setAT0(speed);
        orient.set(orient.azimut(), 0.0F, 0.0F);
        pos.setAbs(point3d, orient);
        pos.reset();
        speed.scale(RUN_SPEEDD);
        animStartTime = Time.tick() + (long)World.Rnd().nextInt(0, 2300);
        disappearTime = -1L;
        disappearPeriod = -1L;
        collide(true);
        draw = new SoldDraw();
        drawing(true);
        if(!interpEnd("move"))
            interpPut(new Move(), "move", Time.current(), null);
    }

    // +++ By western: disappeartime and return flag set by the carrier
    public Soldier(Actor actor, int i, Loc loc, long l, boolean b)
    {
        this(actor, i, loc);
        if(l > Time.current())
        {
            disappearTime = l;
            disappearPeriod = disappearTime - Time.current();
            bReturn = b;
        }
    }

    public int getDying()
    {
        return dying;
    }

    // called when owner artillery or car is dead.
    public void noNeedReturn()
    {
        if(!bReturn) return;

        bReturn = false;
        if(st == ST_RETRUN)
        {
            st = ST_FALL;
            animStartTime = Time.current();
            disappearTime = 0L;
        }
        if(st == ST_LIE || st == ST_LIEDEAD)
            disappearTime = Time.tickNext() + (long)(1000 * World.Rnd().nextInt(25, 35));
    }
    // ---

    private static final int FPS = 30;
    private static final int RUN_START_FRAME = 0;
    private static final int RUN_LAST_FRAME = 22;
    private static final int RUN_N_FRAMES = 23;
    private static final int RUN_CYCLE_TIME = 733;
    private static final int FALL_START_FRAME = 22;
    private static final int FALL_LAST_FRAME = 54;
    private static final int FALL_N_FRAMES = 33;
    private static final int FALL_CYCLE_TIME = 1066;
    private static final int LIE_START_FRAME = 54;
    private static final int LIE_LAST_FRAME = 74;
    private static final int LIE_N_FRAMES = 21;
    private static final int LIE_CYCLE_TIME = 666;
    private static final int LIEDEAD_START_FRAME = 75;
    private static final int LIEDEAD_N_FRAMES = 4;
    private static final float RUN_SPEED = 6.545455F;
    private static final double RUN_SPEEDD = 6.5454545021057129D;
    private Vector3d speed;
    private static final int ST_FLY = 0;
    private static final int ST_RUN = 1;
    private static final int ST_FALL = 2;
    private static final int ST_LIE = 3;
    private static final int ST_LIEDEAD = 4;
    private int st;
    private int dying;
    static final int DYING_NONE = 0;
    static final int DYING_DEAD = 1;
    private int idxOfDeadPose;
    private long animStartTime;
    private long disappearTime;
    private int nRunCycles;
    private static Mesh preload1 = null;
    private static Mesh preload2 = null;
    private static Point3d p = new Point3d();
    private static Orient o = new Orient();
    private static Vector3f n = new Vector3f();

    // +++ western: return function
    private static final int ST_RETRUN = 9;
    private long runRequiredTime;
    private long disappearPeriod;
    private boolean bReturn;
    // --- western

}
