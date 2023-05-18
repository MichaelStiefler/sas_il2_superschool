/*4.10.1 class*/
package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.util.NumberTokenizer;

public class Target extends Actor {
    public static final int           PRIMARY            = 0;
    public static final int           SECONDARY          = 1;
    public static final int           SECRET             = 2;
    public static final int           DESTROY            = 0;
    public static final int           DESTROY_GROUND     = 1;
    public static final int           DESTROY_BRIDGE     = 2;
    public static final int           INSPECT            = 3;
    public static final int           ESCORT             = 4;
    public static final int           DEFENCE            = 5;
    public static final int           DEFENCE_GROUND     = 6;
    public static final int           DEFENCE_BRIDGE     = 7;
    protected int                     importance;
    protected long                    timeout;
    private static int                _counterStaticActors;
    private static StaticActorsFilter staticActorsFilter = new StaticActorsFilter();

    static class StaticActorsFilter implements ActorFilter {
        public boolean isUse(Actor actor, double d) {
            if (!actor.isAlive()) return true;
            if (!isStaticActor(actor)) return true;
            access$008();
            return true;
        }
    }

    public int importance() {
        return this.importance;
    }

    protected boolean checkActorDied(Actor actor) {
        return false;
    }

    protected boolean checkTaskComplete(Actor actor) {
        return false;
    }

    protected boolean checkPeriodic() {
        return false;
    }

    protected boolean checkTimeoutOff() {
        return false;
    }

    protected Target(int i, int i_0_) {
        this.importance = i;
        if (i_0_ >= 0) this.timeout = i_0_ * 60L * 1000L;
        else this.timeout = -1L;

        // TODO: Added by |ZUTI|
        // ------------------------------------
        this.zutiNameTarget();
        // ------------------------------------

        World.cur().targetsGuard.addTarget(this);
    }

    public static final void create(String string) {
        if (string != null && string.length() != 0) {
            NumberTokenizer numbertokenizer = new NumberTokenizer(string);
            int i = numbertokenizer.next(0, 0, 7);
            int i_1_ = numbertokenizer.next(0, 0, 2);
            boolean bool = numbertokenizer.next(0) == 1;
            int i_2_ = numbertokenizer.next(0, 0, 720);
            if (!bool) i_2_ = -1;
            int i_3_ = numbertokenizer.next(0);
            boolean bool_4_ = (i_3_ & 0x1) == 1;
            i_3_ /= 10;
            if (i_3_ < 0) i_3_ = 0;
            if (i_3_ > 100) i_3_ = 100;
            int i_5_ = numbertokenizer.next(0);
            int i_6_ = numbertokenizer.next(0);
            int i_7_ = numbertokenizer.next(1000, 2, 3000);
            int i_8_ = numbertokenizer.next(0);
            String string_9_ = numbertokenizer.next((String) null);
            if (string_9_ != null && string_9_.startsWith("Bridge")) string_9_ = " " + string_9_;
            switch (i) {
                case 0:
                    new TDestroy(i_1_, i_2_, string_9_, i_3_);
                    break;
                case 1:
                    // TODO: Edited by |ZUTI|: added i_8_ at the end, indicating if this
                    // target allows counting of static houses or not
                    new TDestroyGround(i_1_, i_2_, i_5_, i_6_, i_7_, i_3_, i_8_);
                    break;
                case 2:
                    new TDestroyBridge(i_1_, i_2_, string_9_);
                    break;
                case 3:
                    new TInspect(i_1_, i_2_, i_5_, i_6_, i_7_, bool_4_, string_9_);
                    break;
                case 4:
                case 5:
                    new TEscort(i_1_, i_2_, string_9_, i_3_);
                    break;
                case 6:
                    // TODO: Edited by |ZUTI|: added i_8_ at the end, indicating if this
                    // target allows counting of static houses or not
                    new TDefenceGround(i_1_, i_2_, i_5_, i_6_, i_7_, i_3_, i_8_);
                    break;
                case 7:
                    new TDefenceBridge(i_1_, i_2_, string_9_);
                    break;
            }
        }
    }

    // TODO: Edited by |ZUTI|: changed from protected to public
    public static int countStaticActors(Point3d point3d, double d) {
        _counterStaticActors = 0;
        Engine.cur.collideEnv.getFiltered(null, point3d, d, staticActorsFilter);
        return _counterStaticActors;
    }

    protected static boolean isStaticActor(Actor actor) {
        if (actor.getArmy() == 0) return false;
        if (actor instanceof Aircraft) return false;
        if (actor instanceof Chief) return false;
        if (actor instanceof Wing) return false;
        if (Actor.isValid(actor.getOwner()) && actor.getOwner() instanceof Chief) return false;
        return true;
    }

    static int access$008() {
        return _counterStaticActors++;
    }

    // TODO: |ZUTI| methods and variables
    // -------------------------------------------------------
    public static final String ZUTI_TARGET_DESIGNATION_PREFIX = "ZutiTarget_";
    private static int         ZUTI_TARGETS_COUNTER           = 0;
    private String             zutiTargetDesignation          = null;

    /**
     * Call this method whenever mission loads and starts loading targets
     */
    public static void zutiResetTargetsCount() {
        ZUTI_TARGETS_COUNTER = 0;
    }

    /**
     * Call this method to give target unique name in mission scope.
     */
    private void zutiNameTarget() {
        this.zutiTargetDesignation = ZUTI_TARGET_DESIGNATION_PREFIX + ZUTI_TARGETS_COUNTER;
        ZUTI_TARGETS_COUNTER++;
    }

    /**
     * Call this method to get unique target designation.
     */
    public String zutiGetTargetDesignation() {
        return this.zutiTargetDesignation;
    }
    // -------------------------------------------------------
}