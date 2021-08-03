package com.maddox.il2.engine;

import java.util.AbstractCollection;
import java.util.List;

import com.maddox.JGP.Point2d;
import com.maddox.JGP.Point3d;

public abstract class CollideEnv {
    public class ResultTrigger {

        public boolean result;
        public double  altiSea;
        public double  altiGround;

        public ResultTrigger(boolean flag, double d1) {
            this.result = flag;
            this.altiSea = d1;
        }

        public ResultTrigger(boolean flag) {
            this.result = flag;
            this.altiSea = 0.0D;
        }
    }

    public boolean isDoCollision() {
        return false;
    }

    protected void doCollision(List list1) {
    }

    protected void doBulletMoveAndCollision() {
    }

    public void getSphere(AbstractCollection abstractcollection1, Point3d point3d1, double d1) {
    }

    public Actor getLine(Point3d point3d, Point3d point3d1, boolean flag, Actor actor1, Point3d point3d3) {
        return null;
    }

    public Actor getLine(Point3d point3d, Point3d point3d1, boolean flag, ActorFilter actorfilter1, Point3d point3d3) {
        return null;
    }

    public void getFiltered(AbstractCollection abstractcollection1, Point3d point3d1, double d1, ActorFilter actorfilter1) {
    }

    public void getNearestEnemies(Point3d point3d1, double d1, int j, Accumulator accumulator1) {
    }

    public void getNearestEnemiesCyl(Point3d point3d1, double d3, double d4, double d5, int j, Accumulator accumulator1) {
    }

    public ResultTrigger getEnemiesInCyl(Point2d point2d, double d, double d1, double d2, int i, int iaHumans, int avionMin) {
        return new ResultTrigger(false);
    }

    protected void changedPos(Actor actor1, Point3d point3d2, Point3d point3d3) {
    }

    protected void add(Actor actor1) {
    }

    protected void remove(Actor actor1) {
    }

    protected void changedPosStatic(Actor actor1, Point3d point3d2, Point3d point3d3) {
    }

    protected void addStatic(Actor actor1) {
    }

    protected void removeStatic(Actor actor1) {
    }

    protected void clear() {
    }

    protected void resetGameClear() {
    }

    protected void resetGameCreate() {
    }

    protected CollideEnv() {
    }
}
