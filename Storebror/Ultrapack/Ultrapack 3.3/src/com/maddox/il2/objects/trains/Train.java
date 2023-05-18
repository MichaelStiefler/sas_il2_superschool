/*4.10.1 class*/
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
import com.maddox.il2.game.ZutiSupportMethods_ResourcesManagement;
import com.maddox.rts.Message;
import com.maddox.rts.SectFile;
import com.maddox.rts.Spawn;
import com.maddox.rts.Time;

public class Train extends Chief {
    public static final float  TRAIN_SPEED       = 11.111112F;
    public static final double TRAIN_SPEEDUPDIST = 350.0;
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

    class Move extends Interpolate {
        public boolean tick() {
            if (Time.current() >= Train.this.startDelay) Train.this.moveTrain(Time.tickLenFs());
            return true;
        }
    }

    public static class TrainState {
        public int    _headSeg;
        public double _headAlong;
        public float  _curSpeed;
        public double _milestoneDist;
        public float  _requiredSpeed;
        public float  _maxAcceler;
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
        if (this.requiredSpeed < 11.111112F) return this.curSpeed / 11.111112F;
        return 1.0F;
    }

    protected final boolean stoppedForever() {
        return this.requiredSpeed < 0.0F;
    }

    private static final void ERR(String string) {
        String string_4_ = "INTERNAL ERROR IN Train: " + string;
        System.out.println(string_4_);
        throw new ActorException(string_4_);
    }

    private static final void ConstructorFailure() {
        System.out.println("Train: Creation error");
        throw new ActorException();
    }

    public Object getSwitchListener(Message message) {
        return this;
    }

    public Train(String string, int i, SectFile sectfile, String string_5_, SectFile sectfile_6_, String string_7_) {
        try {
            // TODO: Added by |ZUTI|
            // ----------------------------------------------------------
            this.zutiTrainName = string_5_.substring(string_5_.indexOf(".") + 1, string_5_.length());
            // ----------------------------------------------------------

            this.road = new RoadPath(sectfile_6_, string_7_);
            this.startDelay = this.road.get(0).waitTime;
            if (this.startDelay < 0L) this.startDelay = 0L;
            this.road.RegisterTravellerToBridges(this);
            this.setName(string);
            this.setArmy(i);
            this.headSeg = 0;
            this.headAlong = 0.0;
            this.pos = new ActorPosMove(this);
            this.pos.setAbs(this.road.get(0).start);
            this.pos.reset();
            int i_8_ = sectfile.sectionIndex(string_5_);
            int i_9_ = sectfile.vars(i_8_);
            if (i_9_ <= 0) throw new ActorException("Train: Missing wagons");
            this.wagons = new ArrayList();
            for (int i_10_ = 0; i_10_ < i_9_; i_10_++) {
                String string_11_ = sectfile.var(i_8_, i_10_);
                Object object = Spawn.get(string_11_);
                if (object == null) throw new ActorException("Train: Unknown class of wagon (" + string_11_ + ")");
                Wagon wagon = ((WagonSpawn) object).wagonSpawn(this);
                wagon.setName(string + i_10_);
                this.wagons.add(wagon);
            }
            this.recomputeTrainLength();
            this.curSpeed = 0.0F;
            this.requiredSpeed = 0.0F;
            this.placeTrain(true, false);
            this.recomputeSpeedRequirements(this.road.get(this.headSeg).length2Dallprev + this.headAlong - this.trainLength);
            if (!this.interpEnd("move")) this.interpPut(new Move(), "move", Time.current(), null);
        } catch (Exception exception) {
            System.out.println("Train creation failure:");
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            ConstructorFailure();
        }
    }

    public void BridgeSegmentDestroyed(int i, int i_12_, Actor actor) {
        boolean bool = this.road.MarkDestroyedSegments(i, i_12_);
        if (bool) {
            int i_13_;
            for (i_13_ = this.tailSeg; i_13_ <= this.headSeg; i_13_++)
                if (this.road.segIsWrongOrDamaged(i_13_)) break;
            if (i_13_ <= this.headSeg) {
                for (int i_14_ = 0; i_14_ < this.wagons.size(); i_14_++) {
                    Wagon wagon = (Wagon) this.wagons.get(i_14_);
                    wagon.absoluteDeath(actor);
                }
                this.road.UnregisterTravellerFromBridges(this);
                this.destroy();
            }
        }
    }

    public Actor GetNearestEnemy(Point3d point3d, double d, int i) {
        NearestEnemies.set(i);
        return NearestEnemies.getAFoundEnemy(point3d, d, this.getArmy());
    }

    private static float solveSquareEq(float f, float f_15_, float f_16_) {
        float f_17_ = f_15_ * f_15_ - 4.0F * f * f_16_;
        if (f_17_ < 0.0F) return -1.0F;
        f_17_ = (float) Math.sqrt(f_17_);
        float f_18_ = (-f_15_ + f_17_) / (2.0F * f);
        float f_19_ = (-f_15_ - f_17_) / (2.0F * f);
        if (f_19_ > f_18_) f_18_ = f_19_;
        return f_18_ >= 0.0F ? f_18_ : -1.0F;
    }

    private static float findSideBOfTriangle(float f, float f_20_, float f_21_) {
        return solveSquareEq(1.0F, 2.0F * f * f_21_, f * f - f_20_ * f_20_);
    }

    private void recomputeTrainLength() {
        this.trainLength = 0.0F;
        for (int i = 0; i < this.wagons.size(); i++) {
            Wagon wagon = (Wagon) this.wagons.get(i);
            this.trainLength += wagon.getLength();
        }
    }

    private void placeTrain(boolean bool, boolean bool_22_) {
        if (bool_22_) for (int i = 0; i < this.wagons.size(); i++) {
            Wagon wagon = (Wagon) this.wagons.get(i);
            wagon.place(null, null, false, true);
        }
        else {
            if (bool) {
                float f = this.trainLength * 1.02F;
                RoadPart roadpart = new RoadPart();
                if (!this.road.FindFreeSpace(f, this.headSeg, this.headAlong, roadpart)) {
                    System.out.println("Train: Not enough room for wagons");
                    throw new ActorException();
                }
                this.headSeg = roadpart.begseg;
                this.headAlong = roadpart.begt;
            }
            double d = 0.0;
            int i = this.headSeg;
            double d_24_ = this.headAlong;
            Point3d point3d = this.road.get(i).computePos_Fit(d_24_, 0.0, 0.0F);
            for (int i_25_ = 0; i_25_ < this.wagons.size(); i_25_++) {
                Wagon wagon = (Wagon) this.wagons.get(i_25_);
                float f = wagon.getLength();
                int i_26_;
                if (d_24_ >= f) {
                    i_26_ = i;
                    d = d_24_ - f;
                } else {
                    if (i <= 0) {
                        System.out.println("Train: No room for wagons (curly station road?)");
                        throw new ActorException();
                    }
                    i_26_ = i - 1;
                    d = this.road.get(i_26_).length2D;
                    float f_27_ = this.road.computeCosToNextSegment(i_26_);
                    float f_28_ = findSideBOfTriangle((float) d_24_, f, f_27_);
                    if (f_28_ < 0.0F || f_28_ > d) {
                        System.out.println("Train: internal error in computings (len=" + d + " B=" + f_28_ + ")");
                        throw new ActorException();
                    }
                    d -= f_28_;
                    if (d <= 0.0) d = 0.0;
                }
                Point3d point3d_29_ = this.road.get(i_26_).computePos_Fit(d, 0.0, 0.0F);
                wagon.place(point3d, point3d_29_, bool, false);
                i = i_26_;
                d_24_ = d;
                point3d.set(point3d_29_);
                this.tailSeg = i_26_;
            }
        }
    }

    private void computeSpeedsWhenCrush(float f) {
        this.curSpeed = f * 0.9F;
        this.milestoneDist = 9.9999E7;
        this.requiredSpeed = 0.0F;
        this.maxAcceler = 3.5F;
    }

    private void LocoSound(int i) {
        if (this.wagons != null && this.wagons.size() > 0) {
            Wagon wagon = (Wagon) this.wagons.get(0);
            if (wagon != null && wagon instanceof LocomotiveVerm) switch (i) {
                case 0:
                    wagon.newSound("models.train", true);
                    break;
                case 1:
                    wagon.newSound("objects.train_signal", true);
                    break;
                default:
                    wagon.breakSounds();
            }
        }
    }

    private void recomputeSpeedRequirements(double d) {
        double d_35_ = this.road.get(this.road.nsegments() - 1).length2Dallprev;
        d_35_ -= this.trainLength;
        this.maxAcceler = 1.5F;
        double d_36_;
        if (d_35_ <= 350.0) {
            if (d < d_35_ * 0.5) {
                this.LocoSound(0);
                this.milestoneDist = d_35_ * 0.5;
                d_36_ = this.milestoneDist - d;
                this.requiredSpeed = 11.111112F;
            } else {
                this.LocoSound(1);
                this.milestoneDist = 9.99999E7;
                d_36_ = d_35_ - d;
                this.requiredSpeed = 0.7222222F;
            }
        } else if (d < 350.0) {
            this.LocoSound(0);
            this.milestoneDist = 350.0;
            d_36_ = this.milestoneDist - d;
            this.requiredSpeed = 11.111112F;
        } else if (d < d_35_ - 350.0) {
            this.milestoneDist = d_35_ - 350.0;
            d_36_ = 175.0;
            this.requiredSpeed = 11.111112F;
        } else {
            this.LocoSound(1);
            this.milestoneDist = 9.99999E7;
            d_36_ = d_35_ - d;
            this.requiredSpeed = 0.7222222F;
        }
        if (d_36_ > 0.05) {
            float f = (float) ((this.requiredSpeed * this.requiredSpeed - this.curSpeed * this.curSpeed) / (2.0 * d_36_));
            f = Math.abs(f);
            if (f <= this.maxAcceler) this.maxAcceler = f;
        }
        if (this.maxAcceler < 0.01F) this.maxAcceler = 0.01F;
    }

    private boolean cantEnterIntoSegment(int i) {
        return i >= this.road.nsegments() - 1 || !this.road.segIsPassableBy(i, this);
    }

    private void moveTrain(float f) {
        if (this.requiredSpeed < 0.0F) {
            this.placeTrain(false, true);

            // TODO: Added by |ZUTI|: train has stopped
            // ----------------------------------------
            if (this.zutiReportFinalDestination) {
                System.out.println(this.zutiTrainName + " has reached final destination!");
                this.zutiReportFinalDestination = false;

                ZutiSupportMethods_ResourcesManagement.addResourcesFromMovingRRRObjects(this.zutiTrainName, this.pos.getAbsPoint(), this.getArmy(), 1.0F, true);
            }
            // ----------------------------------------
        } else {
            RoadSegment roadsegment = this.road.get(this.headSeg);
            double d = roadsegment.length2Dallprev + this.headAlong - this.trainLength;
            if (d >= this.milestoneDist) this.recomputeSpeedRequirements(d);
            float f_37_ = this.requiredSpeed - this.curSpeed;
            double d_38_;
            if (f_37_ != 0.0F) {
                f_37_ /= f;
                float f_39_;
                if (Math.abs(f_37_) > this.maxAcceler) {
                    f_37_ = f_37_ >= 0.0F ? this.maxAcceler : -this.maxAcceler;
                    f_39_ = this.curSpeed + f * f_37_;
                    if (f_39_ < 0.0F) f_39_ = 0.0F;
                } else f_39_ = this.requiredSpeed;
                d_38_ = this.curSpeed * f + f_37_ * f * f * 0.5F;
                this.curSpeed = f_39_;
                if (d_38_ <= 0.0) d_38_ = 0.0;
            } else {
                d_38_ = this.curSpeed * f;
                if (this.requiredSpeed == 0.0F) this.requiredSpeed = -1.0F;
            }
            for (/**/; this.headAlong + d_38_ >= roadsegment.length2D; roadsegment = this.road.get(this.headSeg)) {
                if (this.cantEnterIntoSegment(this.headSeg + 1)) {
                    this.headAlong = roadsegment.length2D;
                    d_38_ = 0.0;
                    this.curSpeed = 0.0F;
                    this.requiredSpeed = -1.0F;
                    if (this.headSeg + 1 >= this.road.nsegments() - 1) World.onTaskComplete(this);
                    break;
                }
                this.headAlong = this.headAlong + d_38_ - roadsegment.length2D;
                this.headSeg++;
            }
            this.headAlong += d_38_;
            this.pos.setAbs(roadsegment.computePos_Fit(this.headAlong, 0.0, 0.0F));
            this.placeTrain(false, false);
        }
    }

    void wagonDied(Wagon wagon, Actor actor) {
        int i;
        for (i = 0; i < this.wagons.size(); i++)
            if (wagon == (Wagon) this.wagons.get(i)) break;
        if (i >= this.wagons.size()) ERR("Unknown wagon");
        if (this.requiredSpeed >= 0.0F) if (i == 0) {
            this.computeSpeedsWhenCrush(this.curSpeed);
            if (wagon instanceof LocomotiveVerm) World.onActorDied(this, actor);
        } else {
            this.computeSpeedsWhenCrush(this.curSpeed);
            if (wagon instanceof LocomotiveVerm) World.onActorDied(this, actor);
        }
    }

    boolean isAnybodyDead() {
        for (int i = 0; i < this.wagons.size(); i++) {
            Wagon wagon = (Wagon) this.wagons.get(i);
            if (wagon.IsDeadOrDying()) return true;
        }
        return false;
    }

    void setStateDataMirror(TrainState trainstate, boolean bool) {
        this.headSeg = trainstate._headSeg;
        this.headAlong = trainstate._headAlong;
        this.curSpeed = trainstate._curSpeed;
        this.milestoneDist = trainstate._milestoneDist;
        this.requiredSpeed = trainstate._requiredSpeed;
        this.maxAcceler = trainstate._maxAcceler;
        this.placeTrain(false, false);
        if (this.requiredSpeed >= 0.0F && bool) this.computeSpeedsWhenCrush(this.curSpeed);
    }

    // TODO: |ZUTI| variables
    // --------------------------------------------------------------
    private boolean zutiReportFinalDestination = true;
    private String  zutiTrainName              = null;
    // --------------------------------------------------------------
}
