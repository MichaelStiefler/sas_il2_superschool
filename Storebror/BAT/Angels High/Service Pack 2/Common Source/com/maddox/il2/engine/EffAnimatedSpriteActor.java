package com.maddox.il2.engine;

import com.maddox.rts.State;
import com.maddox.rts.States;

class EffAnimatedSpriteActor extends Eff3DActor implements VisibilityLong {
    class _Finish extends State {

        public void begin(int i) {
            EffAnimatedSpriteActor.this.destroy();
        }

        public _Finish(Object obj) {
            super(obj);
        }
    }

    public void _setIntesity(float f) {
        if (this.states.getState() == 0) {
            ((Eff3D) this.draw).setIntesity(f);
            if (this.bUseIntensityAsSwitchDraw) {
                this.drawing(f != 0.0F);
            }
        }
    }

    public EffAnimatedSpriteActor(Eff3D eff3d, Loc loc) {
        this.isVisibilityLong = EffAnimatedSprite.staticVisibilityLong;
        this.draw = eff3d;
        if (_isStaticPos) {
            this.pos = new ActorPosStaticEff3D(this, loc);
        } else {
            this.pos = new ActorPosMove(this, loc);
        }
        this.states = new States(new Object[] { new Eff3DActor.Ready(this), new _Finish(this) });
        this.states.setState(0);
        this.drawing(true);
    }

    public EffAnimatedSpriteActor(Eff3D eff3d, ActorPos actorpos) {
        this.isVisibilityLong = EffAnimatedSprite.staticVisibilityLong;
        this.draw = eff3d;
        this.pos = actorpos;
        this.states = new States(new Object[] { new Eff3DActor.Ready(this), new _Finish(this) });
        this.states.setState(0);
        this.flags |= 3;
        actorpos.base().pos.addChildren(this);
    }

    public boolean isVisibilityLong() {
        return this.isVisibilityLong;
    }

    private boolean isVisibilityLong = false;
}
