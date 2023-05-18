package com.maddox.il2.ai;

import com.maddox.il2.ai.ground.ChiefGround;
import com.maddox.il2.engine.Actor;

class TDestroy extends Target {

    public void destroy() {
        super.destroy();
        this.actor = null;
    }

    boolean checkActor() {
        if ((this.actor != null) && (this.alive > 0)) {
            return false;
        }
        if (World.cur().triggersGuard.listTriggerAvionAppar.contains(this.nameTarget) || World.cur().triggersGuard.listTriggerChiefAppar.contains(this.nameTarget) || World.cur().triggersGuard.listTriggerStaticAppar.contains(this.nameTarget)) {
            return true;
        }
        this.actor = Actor.getByName(this.nameTarget);
        if ((this.actor != null) && (this.alive == -1)) {
            if ((this.actor instanceof ChiefGround) && ((ChiefGround) this.actor).isPacked() && this.actor.isAlive()) {
                return false;
            }
            int i = this.actor.getOwnerAttachedCount();
            if (i > 0) {
                this.alive = i - Math.round(i * this.destructLevel / 100F);
                if (this.alive >= i) {
                    this.alive = i - 1;
                }
            }
        }
        return this.actor != null;
    }

    protected boolean checkPeriodic() {
        if (!this.checkActor()) {
            return false;
        }
        if (!Actor.isValid(this.actor)) {
            this.setTaskCompleteFlag(true);
            this.setDiedFlag(true);
            return true;
        }
        int i = this.actor.getOwnerAttachedCount();
        if (((i == 0) && !(this.actor instanceof Wing)) || (this.alive == -1)) {
            return false;
        }
        int j = 0;
        int k = 0;
        for (int l = 0; l < i; l++) {
            Actor actor1 = (Actor) this.actor.getOwnerAttached(l);
            if (Actor.isAlive(actor1)) {
                j++;
                if (actor1.isTaskComplete()) {
                    k++;
                }
            }
        }

        if (j <= this.alive) {
            this.setTaskCompleteFlag(true);
            this.setDiedFlag(true);
            return true;
        }
        if (k > this.alive) {
            this.setDiedFlag(true);
            return true;
        } else {
            return false;
        }
    }

    protected boolean checkActorDied(Actor actor1) {
        if (!this.checkActor()) {
            return false;
        }
        if (this.actor == actor1) {
            this.setTaskCompleteFlag(true);
            this.setDiedFlag(true);
            return true;
        } else {
            return this.checkPeriodic();
        }
    }

    protected boolean checkTaskComplete(Actor actor1) {
        if (!this.checkActor()) {
            return false;
        }
        if (actor1 == this.actor) {
            this.setDiedFlag(true);
            return true;
        } else {
            return this.checkPeriodic();
        }
    }

    protected boolean checkTimeoutOff() {
        this.setDiedFlag(true);
        return true;
    }

    public TDestroy(int i, int j, String s, int k) {
        super(i, j);
        this.alive = -1;
        this.nameTarget = s;
        this.destructLevel = k;
    }

    String nameTarget;
    Actor  actor;
    int    destructLevel;
    int    alive;
}
