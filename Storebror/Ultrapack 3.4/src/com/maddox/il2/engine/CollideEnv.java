package com.maddox.il2.engine;

import java.util.AbstractCollection;
import java.util.List;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;

public abstract class CollideEnv
{

    // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
    public class ResultTrigger
    {

        public boolean result;
        public double altiSea;
        public double altiGround;

        public ResultTrigger(boolean flag, double d1)
        {
            result = flag;
            altiSea = d1;
        }

        public ResultTrigger(boolean flag)
        {
            result = flag;
            altiSea = 0.0D;
        }
    }
    // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---

    public boolean isDoCollision()
    {
        return false;
    }

    protected void doCollision(List list)
    {
    }

    protected void doBulletMoveAndCollision()
    {
    }

    public void getSphere(AbstractCollection abstractcollection, Point3d point3d, double d)
    {
    }

    public Actor getLine(Point3d point3d, Point3d point3d1, boolean flag, Actor actor, Point3d point3d2)
    {
        return null;
    }

    public Actor getLine(Point3d point3d, Point3d point3d1, boolean flag, ActorFilter actorfilter, Point3d point3d2)
    {
        return null;
    }

    public void getFiltered(AbstractCollection abstractcollection, Point3d point3d, double d, ActorFilter actorfilter)
    {
    }

    public void getNearestEnemies(Point3d point3d, double d, int i, Accumulator accumulator)
    {
    }

    public void getNearestEnemiesCyl(Point3d point3d, double d, double d1, double d2, 
            int i, Accumulator accumulator)
    {
    }
    
    // TODO: +++ Trigger backport from HSFX 7.0.3 by SAS~Storebror +++
    public ResultTrigger getEnemiesInCyl(Point2d point2d, double d, double d1, double d2, 
            int i, int iaHumans, int avionMin)
    {
        return new ResultTrigger(false);
    }
    // TODO: --- Trigger backport from HSFX 7.0.3 by SAS~Storebror ---

    protected void changedPos(Actor actor, Point3d point3d, Point3d point3d1)
    {
    }

    protected void add(Actor actor)
    {
    }

    protected void remove(Actor actor)
    {
    }

    protected void changedPosStatic(Actor actor, Point3d point3d, Point3d point3d1)
    {
    }

    protected void addStatic(Actor actor)
    {
    }

    protected void removeStatic(Actor actor)
    {
    }

    protected void clear()
    {
    }

    protected void resetGameClear()
    {
    }

    protected void resetGameCreate()
    {
    }

    protected CollideEnv()
    {
    }
}
