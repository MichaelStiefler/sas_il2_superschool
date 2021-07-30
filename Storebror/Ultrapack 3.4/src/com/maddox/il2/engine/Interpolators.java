/*4.10.1 class*/
package com.maddox.il2.engine;

public final class Interpolators {
    private long                                     timeSleep;
    private com.maddox.il2.engine.ArrayInterpolators interp;
    private int                                      stepStamp;
    private boolean                                  bForceRef;
    private boolean                                  bDoTick;

    private void checkFlagForceRef() {
        this.bForceRef = false;
        int i = this.interp.size();
        for (int j = 0; j < i; j++) {
            if (!(this.interp.get(j) instanceof com.maddox.il2.engine.InterpolateRef)) continue;
            this.bForceRef = true;
            break;
        }
    }

    public boolean isSleep() {
        return this.timeSleep != 0L;
    }

    public boolean sleep() {
        if (this.isSleep()) return false;
        int i = this.interp.modCount();
        int j = this.interp.size();
        for (int k = 0; k < j; k++) {
            com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) this.interp.get(k);
            interpolate.sleep();
            if (i != this.interp.modCount()) throw new ActorException("Interpolators changed in 'sleep'");
        }

        this.timeSleep = com.maddox.rts.Time.current();
        return true;
    }

    public boolean wakeup() {
        if (!this.isSleep()) return false;
        int i = this.interp.modCount();
        int j = this.interp.size();
        for (int k = 0; k < j; k++) {
            com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) this.interp.get(k);
            interpolate.wakeup(this.timeSleep);
            if (i != this.interp.modCount()) throw new ActorException("Interpolators changed in 'wakeup'");
        }

        this.timeSleep = 0L;
        return true;
    }

    public com.maddox.il2.engine.Interpolate get(java.lang.Object obj) {
        int i = this.interp.size();
        for (int j = 0; j < i; j++) {
            com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) this.interp.get(j);
            if (interpolate.id == obj || obj != null && obj.equals(interpolate.id)) return interpolate;
        }

        return null;
    }

    public boolean end(java.lang.Object obj) {
        int i = this.interp.size();
        for (int j = 0; j < i; j++) {
            com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) this.interp.get(j);
            if (interpolate.id == obj || obj != null && obj.equals(interpolate.id)) {
                this.interp.remove(j);
                if (interpolate.bExecuted) interpolate.end();
                this.interplateClean(interpolate);
                this.checkFlagForceRef();
                return true;
            }
        }

        return false;
    }

    public boolean end(com.maddox.il2.engine.Interpolate interpolate) {
        int i = this.interp.indexOf(interpolate);
        if (i >= 0) {
            this.interp.remove(i);
            if (interpolate.bExecuted) interpolate.end();
            this.interplateClean(interpolate);
            this.checkFlagForceRef();
            return true;
        } else return false;
    }

    public void endAll() {
        com.maddox.il2.engine.Interpolate interpolate;
        for (; this.interp.size() > 0; this.interplateClean(interpolate)) {
            interpolate = (com.maddox.il2.engine.Interpolate) this.interp.get(0);
            this.interp.remove(0);
            if (interpolate.bExecuted) interpolate.end();
        }

        this.checkFlagForceRef();
    }

    public boolean cancel(java.lang.Object obj) {
        int i = this.interp.size();
        for (int j = 0; j < i; j++) {
            com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) this.interp.get(j);
            if (interpolate.id == obj || obj != null && obj.equals(interpolate.id)) {
                this.interp.remove(j);
                if (interpolate.bExecuted) interpolate.cancel();
                this.interplateClean(interpolate);
                this.checkFlagForceRef();
                return true;
            }
        }

        return false;
    }

    public boolean cancel(com.maddox.il2.engine.Interpolate interpolate) {
        int i = this.interp.indexOf(interpolate);
        if (i >= 0) {
            this.interp.remove(i);
            if (interpolate.bExecuted) interpolate.cancel();
            this.interplateClean(interpolate);
            this.checkFlagForceRef();
            return true;
        } else return false;
    }

    public void cancelAll() {
        com.maddox.il2.engine.Interpolate interpolate;
        for (; this.interp.size() > 0; this.interplateClean(interpolate)) {
            interpolate = (com.maddox.il2.engine.Interpolate) this.interp.get(0);
            this.interp.remove(0);
            if (interpolate.bExecuted) interpolate.cancel();
        }

        this.checkFlagForceRef();
    }

    public void put(com.maddox.il2.engine.Interpolate interpolate, java.lang.Object obj, long l, com.maddox.rts.Message message, com.maddox.il2.engine.Actor actor) {
        if (obj != null && this.get(obj) != null) throw new ActorException("Interpolator: '" + obj + "' alredy exist");
        else {
            interpolate.actor = actor;
            interpolate.timeBegin = l;
            interpolate.id = obj;
            interpolate.msgEnd = message;
            interpolate.bExecuted = false;
            this.interp.add(interpolate);
            this.checkFlagForceRef();
            return;
        }
    }

    public int size() {
        return this.interp.size();
    }

    public void tick(long l) {
        if (this.timeSleep == 0L) {
            if (this.stepStamp == com.maddox.il2.engine.InterpolateAdapter.step()) return;
            this.bDoTick = true;
            int i = this.interp.size();
            if (this.bForceRef) for (int j = 0; j < i; j++) {
                com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) this.interp.get(j);
                if (interpolate instanceof com.maddox.il2.engine.InterpolateRef) ((com.maddox.il2.engine.InterpolateRef) interpolate).invokeRef();
            }
            int k = this.interp.modCount();
            for (int i1 = 0; i1 < i; i1++) {
                com.maddox.il2.engine.Interpolate interpolate1 = (com.maddox.il2.engine.Interpolate) this.interp.get(i1);
                if (!interpolate1.bExecuted && interpolate1.timeBegin != -1L && interpolate1.timeBegin <= l) {
                    interpolate1.bExecuted = true;
                    interpolate1.begin();
                    if (k != this.interp.modCount()) {
                        this.bDoTick = false;
                        throw new ActorException("Interpolators changed in 'begin'");
                    }
                }
                if (interpolate1.bExecuted) if (!interpolate1.tick()) {
                    if (k != this.interp.modCount()) {
                        this.bDoTick = false;
                        throw new ActorException("Interpolators changed in 'tick'");
                    }
                    this.interp.remove(i1);
                    k = this.interp.modCount();
                    interpolate1.end();
                    if (k != this.interp.modCount()) {
                        this.bDoTick = false;
                        throw new ActorException("Interpolators changed in 'end'");
                    }
                    this.interplateClean(interpolate1);
                    this.checkFlagForceRef();
                    i1--;
                    i--;
                } else if (k != this.interp.modCount()) this.bDoTick = false;
            }

            this.stepStamp = com.maddox.il2.engine.InterpolateAdapter.step();
            this.bDoTick = false;
        }
    }

    private void interplateClean(com.maddox.il2.engine.Interpolate interpolate) {
        if (interpolate.actor == null) return;
        else {
            interpolate.doDestroy();
            interpolate.actor = null;
            interpolate.id = null;
            interpolate.msgEnd = null;
            return;
        }
    }

    public void destroy() {
        if (this.bDoTick) {
            // TODO: Storebror:
            // Disabled this log entry since it serves no purpose, gives useless "information" only.
            // In return, it keeps the game from exiting due to a log flood caused in case of game lockups.
//			throw new ActorException("Interpolators destroying in invoked method 'tick' ");
        } else {
            this.cancelAll();
            this.interp = null;
            return;
        }
    }

    public Interpolators() {
        this.stepStamp = -1;
        this.bForceRef = false;
        this.bDoTick = false;
        this.timeSleep = 0L;
        this.interp = new ArrayInterpolators(2);
    }
}