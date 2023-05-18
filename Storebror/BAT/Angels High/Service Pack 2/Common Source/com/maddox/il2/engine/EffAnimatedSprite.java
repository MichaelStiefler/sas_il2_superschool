package com.maddox.il2.engine;

public class EffAnimatedSprite extends Eff3D implements VisibilityLong {

    protected Eff3DActor NewActor(Loc loc) {
        this.isVisibilityLong = staticVisibilityLong;
        return new EffAnimatedSpriteActor(this, loc);
    }

    protected Eff3DActor NewActor(ActorPos actorpos) {
        this.isVisibilityLong = staticVisibilityLong;
        return new EffAnimatedSpriteActor(this, actorpos);
    }

    protected EffAnimatedSprite() {
        this.isVisibilityLong = staticVisibilityLong;
        this.cppObj = this.cNew();
    }

    private native int cNew();

    public EffAnimatedSprite(int i) {
        this.isVisibilityLong = staticVisibilityLong;
        this.cppObj = i;
    }

    public boolean isVisibilityLong() {
        return this.isVisibilityLong;
    }

    private boolean       isVisibilityLong     = false;
    public static boolean staticVisibilityLong = false;

    static {
        GObj.loadNative();
    }
}
