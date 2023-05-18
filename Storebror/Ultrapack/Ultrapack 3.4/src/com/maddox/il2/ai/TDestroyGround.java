/*4.10.1 class*/
package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosStatic;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.objects.buildings.House;

class TDestroyGround extends Target {
    public double          r;
    public int             countActors;
    public int             destructLevel;
    private static Point3d p = new Point3d();

    protected boolean checkActorDied(Actor actor) {
        if (actor.pos == null) return false;

        // TODO: Changed by |ZUTI|: added last part - || (zutiCountHouses && !(actor instanceof House))
        // if( !Target.isStaticActor(actor) )
        if (!Target.isStaticActor(actor) && (!this.zutiCountMapObjects || this.zutiCountMapObjects && !(actor instanceof House))) // System.out.println("TDestroyGround returning false while checking if actor is dead...");
            return false;

        actor.pos.getAbs(p);
        p.z = this.pos.getAbsPoint().z;
        if (this.pos.getAbsPoint().distance(p) <= this.r) {
            this.countActors--;

            // System.out.println(" Remaining actors count: " + countActors);

            if (this.countActors <= 0) {
                this.setTaskCompleteFlag(true);
                this.setDiedFlag(true);

                return true;
            }
        }
        return false;
    }

    protected boolean checkTimeoutOff() {
        this.setDiedFlag(true);
        return true;
    }

    /**
     *
     * @param i:  importance
     * @param j:  timeout
     * @param k:  x coordinate
     * @param l:  y coordinate
     * @param i1: radius
     * @param j1: destruction level
     */
    public TDestroyGround(int i, int j, int k, int l, int i1, int j1) {
        super(i, j);

        // TODO: Added by |ZUTI|
        // ----------------------
        this.zutiTimeout = j;
        // ----------------------

        this.countActors = 0;
        this.r = i1;
        this.destructLevel = j1;
        World.land();
        this.pos = new ActorPosStatic(this, new Point3d(k, l, Landscape.HQ(k, l)), new Orient());
        this.countActors = Target.countStaticActors(this.pos.getAbsPoint(), this.r);

        if (this.countActors == 0) {
            this.setTaskCompleteFlag(true);
            this.setDiedFlag(true);
        } else {
            this.countActors = Math.round(this.countActors * this.destructLevel / 100F);
            if (this.countActors == 0) this.countActors = 1;
        }
    }

    // TODO: |ZUTI| Methods and variables
    // ---------------------------------------------------------------------------------------------------------------------------------------
    public int      zutiTimeout         = -1;
    private boolean zutiCountMapObjects = true;

    /**
     * |ZUTI| defined constructor.
     *
     * @param i
     * @param j
     * @param k
     * @param l
     * @param i1
     * @param j1
     * @param zutiCountHouses
     */
    public TDestroyGround(int i, int j, int k, int l, int i1, int j1, int zutiCountHouses) {
        super(i, j);
        this.zutiTimeout = j;

        if (zutiCountHouses == 1) this.zutiCountMapObjects = true;
        else this.zutiCountMapObjects = false;

        this.countActors = 0;
        this.r = i1;
        this.destructLevel = j1;
        World.land();
        this.pos = new ActorPosStatic(this, new Point3d(k, l, Landscape.HQ(k, l)), new Orient());
        this.countActors = Target.countStaticActors(this.pos.getAbsPoint(), this.r);

        /*
         * System.out.println("TDestroyGround - found static actors: " + countActors); System.out.println("TDestroyGround - position: " + this.pos.getAbsPoint().toString() + ", r=" + r); System.out.println("TDestroyGround - found Houses actors: " +
         * Target.zutiCountHousesInArea(this.pos.getAbsPoint(), r) );
         */

        // Added by |ZUTI|
        // -----------------------------------------
        if (this.zutiCountMapObjects) this.countActors += ZutiSupportMethods.countHousesInArea(this.pos.getAbsPoint(), this.r);
        // System.out.println("TDestroyGround - found actors (including houses): " + countActors);

        if (this.countActors == 0) {
            this.setTaskCompleteFlag(true);
            this.setDiedFlag(true);
        } else {
            this.countActors = Math.round(this.countActors * this.destructLevel / 100F);

            System.out.println("TDestroyGround " + this.pos.getAbsPoint().toString() + " - Objects to destroy " + this.countActors);

            if (this.countActors == 0) this.countActors = 1;
        }
    }

    /**
     * Check specified coordinates and return true if they are inside target radius.
     *
     * @param x
     * @param y
     * @return
     */
    public boolean zutiIsOverTarged(double x, double y) {
        double max = this.r * this.r;
        double tmpDistance = (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y);
        if (tmpDistance < max) return true;

        return false;
    }

    /**
     * Method checks if target allows targeting of actors.static objects.
     *
     * @return
     */
    public boolean zutiIsTargetingOfMapObjectsAllowed() {
        return this.zutiCountMapObjects;
    }
    // ---------------------------------------------------------------------------------------------------------------------------------------
}