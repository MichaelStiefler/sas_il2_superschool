package com.maddox.il2.objects.trains;

import java.util.ArrayList;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Chief;
import com.maddox.il2.ai.World;
import com.maddox.il2.ai.ground.NearestEnemies;
import com.maddox.il2.ai.ground.RoadPart;
import com.maddox.il2.ai.ground.RoadPath;
import com.maddox.il2.ai.ground.RoadSegment;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorException;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Message;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class Train extends Chief {
    class Move extends Interpolate {

        public boolean tick() {
            if (Time.current() >= Train.this.startDelay) {
                Train.this.moveTrain(Time.tickLenFs());
            }
            return true;
        }

        Move() {
        }
    }

    public static class TrainState {

        public int    _headSeg;
        public double _headAlong;
        public float  _curSpeed;
        public double _milestoneDist;
        public float  _requiredSpeed;
        public float  _maxAcceler;

        public TrainState() {
            this._headSeg = 0;
            this._headAlong = 0.0D;
            this._curSpeed = 0.0F;
            this._milestoneDist = 0.0D;
            this._requiredSpeed = 0.0F;
            this._maxAcceler = 0.0F;
        }
    }

    void getStateData(TrainState trainstate) {
        trainstate._headSeg = this.headSeg;
        trainstate._headAlong = this.headAlong;
        trainstate._curSpeed = this.curSpeed;
        trainstate._milestoneDist = this.milestoneDist;
        trainstate._requiredSpeed = this.requiredSpeed;
        trainstate._maxAcceler = this.maxAcceler;
    }

    protected final float getEngineSmokeKoef() {
        if (this.requiredSpeed < 11.11111F) {
            return this.curSpeed / 11.11111F;
        } else {
            return 1.0F;
        }
    }

    protected final boolean stoppedForever() {
        return this.requiredSpeed < 0.0F;
    }

    private static final void ERR(String s) {
        String s1 = "INTERNAL ERROR IN Train: " + s;
        System.out.println(s1);
        throw new ActorException(s1);
    }

    private static final void ConstructorFailure() {
        System.out.println("Train: Creation error");
        throw new ActorException();
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public Train(String s, int i, SectFile sectfile, String s1, SectFile sectfile1, String s2) {
        this.wagons = null;
        this.trainLength = 0.0F;
        this.road = null;
        this.startDelay = 0L;
        this.headSeg = 0;
        this.headAlong = 0.0D;
        this.tailSeg = 0;
        this.curSpeed = 0.0F;
        this.milestoneDist = 0.0D;
        this.requiredSpeed = 0.0F;
        this.maxAcceler = 0.0F;
        try {
            this.road = new RoadPath(sectfile1, s2);
            this.startDelay = this.road.get(0).waitTime;
            if (this.startDelay < 0L) {
                this.startDelay = 0L;
            }
            this.road.RegisterTravellerToBridges(this);
            this.setName(s);
            this.setArmy(i);
            this.headSeg = 0;
            this.headAlong = 0.0D;
            super.pos = new ActorPosMove(this);
            super.pos.setAbs(this.road.get(0).start);
            super.pos.reset();
            int j = sectfile.sectionIndex(s1);
            int k = sectfile.vars(j);
            if (k <= 0) {
                throw new ActorException("Train: Missing wagons");
            }
            this.wagons = new ArrayList();
            for (int l = 0; l < k; l++) {
                String s3 = sectfile.var(j, l);
                Object obj = Spawn.get(s3);
                if (obj == null) {
                    throw new ActorException("Train: Unknown class of wagon (" + s3 + ")");
                }
                Wagon wagon = ((WagonSpawn) obj).wagonSpawn(this);
                wagon.setName(s + l);
                this.wagons.add(wagon);
            }

            this.recomputeTrainLength();
            this.curSpeed = 0.0F;
            this.requiredSpeed = 0.0F;
            this.placeTrain(true, false);
            this.recomputeSpeedRequirements((this.road.get(this.headSeg).length2Dallprev + this.headAlong) - this.trainLength);
            if (!this.interpEnd("move") && !World.cur().triggersGuard.listTriggerChiefSol.contains(s)) {
                this.interpPut(new Move(), "move", Time.current(), null);
            }
        } catch (Exception exception) {
            System.out.println("Train creation failure:");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            Train.ConstructorFailure();
        }
    }

    public void startMove() {
        if (!this.interpEnd("move")) {
            this.interpPut(new Move(), "move", Time.current(), null);
        }
    }

    public void BridgeSegmentDestroyed(int i, int j, Actor actor) {
        boolean flag = this.road.MarkDestroyedSegments(i, j);
        if (!flag) {
            return;
        }
        int k;
        for (k = this.tailSeg; (k <= this.headSeg) && !this.road.segIsWrongOrDamaged(k); k++) {
            ;
        }
        if (k > this.headSeg) {
            return;
        }
        for (int l = 0; l < this.wagons.size(); l++) {
            Wagon wagon = (Wagon) this.wagons.get(l);
            wagon.absoluteDeath(actor);
        }

        this.road.UnregisterTravellerFromBridges(this);
        this.destroy();
    }

    public Actor GetNearestEnemy(Point3d point3d, double d, int i) {
        NearestEnemies.set(i);
        return NearestEnemies.getAFoundEnemy(point3d, d, this.getArmy());
    }

    private static float solveSquareEq(float f, float f1, float f2) {
        float f3 = (f1 * f1) - (4F * f * f2);
        if (f3 < 0.0F) {
            return -1F;
        }
        f3 = (float) Math.sqrt(f3);
        float f4 = (-f1 + f3) / (2.0F * f);
        float f5 = (-f1 - f3) / (2.0F * f);
        if (f5 > f4) {
            f4 = f5;
        }
        return f4 >= 0.0F ? f4 : -1F;
    }

    private static float findSideBOfTriangle(float f, float f1, float f2) {
        return Train.solveSquareEq(1.0F, 2.0F * f * f2, (f * f) - (f1 * f1));
    }

    private void recomputeTrainLength() {
        this.trainLength = 0.0F;
        for (int i = 0; i < this.wagons.size(); i++) {
            Wagon wagon = (Wagon) this.wagons.get(i);
            this.trainLength += wagon.getLength();
        }

    }

    private void placeTrain(boolean flag, boolean flag1) {
        if (flag1) {
            for (int i = 0; i < this.wagons.size(); i++) {
                Wagon wagon = (Wagon) this.wagons.get(i);
                wagon.place(null, null, false, true);
            }

            return;
        }
        if (flag) {
            float f = this.trainLength * 1.02F;
            RoadPart roadpart = new RoadPart();
            if (!this.road.FindFreeSpace(f, this.headSeg, this.headAlong, roadpart)) {
                System.out.println("Train: Not enough room for wagons");
                throw new ActorException();
            }
            this.headSeg = roadpart.begseg;
            this.headAlong = roadpart.begt;
        }
        int j = this.headSeg;
        double d = this.headAlong;
        Point3d point3d = this.road.get(j).computePos_Fit(d, 0.0D, 0.0F);
        for (int l = 0; l < this.wagons.size(); l++) {
            Wagon wagon1 = (Wagon) this.wagons.get(l);
            float f1 = wagon1.getLength();
            int k;
            double d2;
            if (d >= f1) {
                k = j;
                d2 = d - f1;
            } else {
                if (j <= 0) {
                    System.out.println("Train: No room for wagons (curly station road?)");
                    throw new ActorException();
                }
                k = j - 1;
                d2 = this.road.get(k).length2D;
                float f2 = this.road.computeCosToNextSegment(k);
                float f3 = Train.findSideBOfTriangle((float) d, f1, f2);
                if ((f3 < 0.0F) || (f3 > d2)) {
                    System.out.println("Train: internal error in computings (len=" + d2 + " B=" + f3 + ")");
                    throw new ActorException();
                }
                d2 -= f3;
                if (d2 <= 0.0D) {
                    d2 = 0.0D;
                }
            }
            Point3d point3d1 = this.road.get(k).computePos_Fit(d2, 0.0D, 0.0F);
            wagon1.place(point3d, point3d1, flag, false);
            j = k;
            d = d2;
            point3d.set(point3d1);
            this.tailSeg = k;
        }

    }

    private void computeSpeedsWhenCrush(float f) {
        this.curSpeed = f * 0.9F;
        this.milestoneDist = 99999000D;
        this.requiredSpeed = 0.0F;
        this.maxAcceler = Train.TRAIN_CRUSHACCEL;
    }

    private void LocoSound(int i) {
        if ((this.wagons == null) || (this.wagons.size() <= 0)) {
            return;
        }
        Wagon wagon = (Wagon) this.wagons.get(0);
        if ((wagon == null) || !(wagon instanceof LocomotiveVerm)) {
            return;
        }
        switch (i) {
            case 0:
                wagon.newSound("models.train", true);
                break;

            case 1:
                wagon.newSound("objects.train_signal", true);
                break;

            default:
                wagon.breakSounds();
                break;
        }
    }

    private void recomputeSpeedRequirements(double d) {
        double d1 = this.road.get(this.road.nsegments() - 1).length2Dallprev;
        d1 -= this.trainLength;
        this.maxAcceler = Train.TRAIN_ACCEL;
        double d2;
        if (d1 <= 350D) {
            if (d < (d1 * 0.5D)) {
                this.LocoSound(0);
                this.milestoneDist = d1 * 0.5D;
                d2 = this.milestoneDist - d;
                this.requiredSpeed = 11.11111F;
            } else {
                this.LocoSound(1);
                this.milestoneDist = 99999900D;
                d2 = d1 - d;
                this.requiredSpeed = Train.TRAIN_SLOWSPEED;
            }
        } else if (d < 350D) {
            this.LocoSound(0);
            this.milestoneDist = 350D;
            d2 = this.milestoneDist - d;
            this.requiredSpeed = 11.11111F;
        } else if (d < (d1 - 350D)) {
            this.milestoneDist = d1 - 350D;
            d2 = 175D;
            this.requiredSpeed = 11.11111F;
        } else {
            this.LocoSound(1);
            this.milestoneDist = 99999900D;
            d2 = d1 - d;
            this.requiredSpeed = Train.TRAIN_SLOWSPEED;
        }
        if (d2 > 0.05D) {
            float f = (float) (((this.requiredSpeed * this.requiredSpeed) - (this.curSpeed * this.curSpeed)) / (2D * d2));
            f = Math.abs(f);
            if (f <= this.maxAcceler) {
                this.maxAcceler = f;
            }
        }
        if (this.maxAcceler < 0.01F) {
            this.maxAcceler = 0.01F;
        }
    }

    private boolean cantEnterIntoSegment(int i) {
        return (i >= (this.road.nsegments() - 1)) || !this.road.segIsPassableBy(i, this);
    }

    private void moveTrain(float f) {
        if (this.requiredSpeed < 0.0F) {
            this.placeTrain(false, true);
            return;
        }
        RoadSegment roadsegment = this.road.get(this.headSeg);
        double d = (roadsegment.length2Dallprev + this.headAlong) - this.trainLength;
        if (d >= this.milestoneDist) {
            this.recomputeSpeedRequirements(d);
        }
        float f1 = this.requiredSpeed - this.curSpeed;
        double d1;
        if (f1 != 0.0F) {
            f1 /= f;
            float f2;
            if (Math.abs(f1) > this.maxAcceler) {
                f1 = f1 >= 0.0F ? this.maxAcceler : -this.maxAcceler;
                f2 = this.curSpeed + (f * f1);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }
            } else {
                f2 = this.requiredSpeed;
            }
            d1 = (this.curSpeed * f) + (f1 * f * f * 0.5F);
            this.curSpeed = f2;
            if (d1 <= 0.0D) {
                d1 = 0.0D;
            }
        } else {
            d1 = this.curSpeed * f;
            if (this.requiredSpeed == 0.0F) {
                this.requiredSpeed = -1F;
            }
        }
        do {
            if ((this.headAlong + d1) < roadsegment.length2D) {
                break;
            }
            if (this.cantEnterIntoSegment(this.headSeg + 1)) {
                this.headAlong = roadsegment.length2D;
                d1 = 0.0D;
                this.curSpeed = 0.0F;
                this.requiredSpeed = -1F;
                if ((this.headSeg + 1) >= (this.road.nsegments() - 1)) {
                    World.onTaskComplete(this);
                }
                break;
            }
            this.headAlong = (this.headAlong + d1) - roadsegment.length2D;
            this.headSeg++;
            roadsegment = this.road.get(this.headSeg);
        } while (true);
        this.headAlong += d1;
        super.pos.setAbs(roadsegment.computePos_Fit(this.headAlong, 0.0D, 0.0F));
        this.placeTrain(false, false);
    }

    void wagonDied(Wagon wagon, Actor actor) {
        int i;
        for (i = 0; (i < this.wagons.size()) && (wagon != (Wagon) this.wagons.get(i)); i++) {
            ;
        }
        if (i >= this.wagons.size()) {
            Train.ERR("Unknown wagon");
        }
        if (this.requiredSpeed >= 0.0F) {
            if (i == 0) {
                this.computeSpeedsWhenCrush(this.curSpeed);
                if (wagon instanceof LocomotiveVerm) {
                    World.onActorDied(this, actor);
                }
            } else {
                this.computeSpeedsWhenCrush(this.curSpeed);
                if (wagon instanceof LocomotiveVerm) {
                    World.onActorDied(this, actor);
                }
            }
        } else if (wagon instanceof LocomotiveVerm) {
            World.onActorDied(this, actor);
        }
    }

    boolean isAnybodyDead() {
        for (int i = 0; i < this.wagons.size(); i++) {
            Wagon wagon = (Wagon) this.wagons.get(i);
            if (wagon.IsDeadOrDying()) {
                return true;
            }
        }

        return false;
    }

    void setStateDataMirror(TrainState trainstate, boolean flag) {
        this.headSeg = trainstate._headSeg;
        this.headAlong = trainstate._headAlong;
        this.curSpeed = trainstate._curSpeed;
        this.milestoneDist = trainstate._milestoneDist;
        this.requiredSpeed = trainstate._requiredSpeed;
        this.maxAcceler = trainstate._maxAcceler;
        this.placeTrain(false, false);
        if ((this.requiredSpeed >= 0.0F) && flag) {
            this.computeSpeedsWhenCrush(this.curSpeed);
        }
    }

    public static final float  TRAIN_SPEED       = 11.11111F;
    private static final float TRAIN_SLOWSPEED   = 0.7222222F;
    private static final float TRAIN_ACCEL       = 1.5F;
    private static final float TRAIN_CRUSHACCEL  = 3.5F;
    public static final double TRAIN_SPEEDUPDIST = 350D;
    private ArrayList          wagons;
    private float              trainLength;
    private RoadPath           road;
    private long               startDelay;
    private int                headSeg;
    private double             headAlong;
    private int                tailSeg;
    private float              curSpeed;
    private double             milestoneDist;
    private float              requiredSpeed;
    private float              maxAcceler;
}
