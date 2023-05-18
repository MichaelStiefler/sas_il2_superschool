package com.maddox.il2.ai.ground;

import com.maddox.il2.engine.Actor;

// Referenced classes of package com.maddox.il2.ai.ground:
//            ShipAim

public interface ShipHunterInterface
{

    public abstract float getReloadingTime(ShipAim aim);

    public abstract float chainFireTime(ShipAim aim);

    public abstract float probabKeepSameEnemy(Actor actor);

    public abstract float minTimeRelaxAfterFight();

    public abstract boolean enterToFireMode(int i, Actor actor, float f, ShipAim aim);

    public abstract void gunStartParking(ShipAim aim);

    public abstract void gunInMove(boolean flag, ShipAim aim);

    public abstract Actor findEnemy(ShipAim aim);

    public abstract int targetGun(ShipAim aim, Actor actor, float f, boolean flag);

    public abstract int targetMissile(ShipAim aim, Actor actor, float f, boolean flag);

    public abstract boolean isMissile(ShipAim aim);

    public abstract void singleShot(ShipAim aim);

    public abstract void startFire(ShipAim aim);

    public abstract void continueFire(ShipAim aim);

    public abstract void stopFire(ShipAim aim);
}