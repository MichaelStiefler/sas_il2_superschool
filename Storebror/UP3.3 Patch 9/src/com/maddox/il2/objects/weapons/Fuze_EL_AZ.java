package com.maddox.il2.objects.weapons;

import com.maddox.JGP.Vector3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPos;
import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.il2.objects.air.AircraftLH;
import com.maddox.rts.Time;

public abstract class Fuze_EL_AZ extends Fuze {

    public Fuze_EL_AZ() {
        this.mirrorFuzeMode = -1;
        this.releasedBombMode = 1;
        this.chargeGone = false;
    }

    public static void reset() {
        Fuze_EL_AZ.selectedFuzeMode = 1;
        AircraftLH.hasFuzeModeSelector = false;
    }

    public static int getFuzeMode() {
        return Fuze_EL_AZ.selectedFuzeMode;
    }

    public void setMirrorFuzeMode(int i) {
        this.mirrorFuzeMode = i;
    }

    public void setOwnerBomb(Bomb bomb) {
        super.setOwnerBomb(bomb);
        this.releasedBombMode = Fuze_EL_AZ.selectedFuzeMode;
        if (this.mirrorFuzeMode != -1) {
            this.releasedBombMode = this.mirrorFuzeMode;
        }
    }

    public static void toggleELAZFuzeMode() {
        if (Fuze_EL_AZ.selectedFuzeMode == 1) {
            Fuze_EL_AZ.selectedFuzeMode = 2;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "EL_AZ_Mode_Short");
        } else if (Fuze_EL_AZ.selectedFuzeMode == 2) {
            Fuze_EL_AZ.selectedFuzeMode = 3;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "EL_AZ_Mode_Long");
        } else if (Fuze_EL_AZ.selectedFuzeMode == 3) {
            Fuze_EL_AZ.selectedFuzeMode = 1;
            HUD.log(AircraftHotKeys.hudLogWeaponId, "EL_AZ_Mode_Instant");
        }
    }

    protected abstract int getProbableArmingMin();

    protected abstract int getProbableArmingMax();

    protected boolean switchToBackupMode() {
        if (this.releasedBombMode == 1) {
            this.releasedBombMode = 3;
            this.setDetonationDelay(this.getDetonationDelay());
            return true;
        }
        if (this.releasedBombMode == 2) {
            this.releasedBombMode = 3;
            this.setDetonationDelay(this.getDetonationDelay());
            return true;
        }
        return this.releasedBombMode != 3 ? false : false;
    }

    public boolean isFuzeArmed(ActorPos actorpos, Vector3d vector3d, Actor actor) {
        if (this.chargeGone && !this.isArmed) {
            return false;
        }
        if (this.chargeGone && this.isArmed) {
            return true;
        } else {
            this.isArmed = this.isArmedRecursive();
            this.chargeGone = true;
            return this.isArmed;
        }
    }

    public boolean isArmedRecursive() {
        long l = Time.current() - this.startingTime;
        int i = this.getProbableArmingMin();
        int j = this.getProbableArmingMax();
        if (l < i) {
            if (this.switchToBackupMode()) {
                return this.isArmedRecursive();
            } else {
                return false;
            }
        }
        if (l > j) {
            return true;
        }
        int k = j - i;
        float f = ((float) (l - k) / (float) k) + 0.1F;
        if (this.getOwnerBomb().getRnd().nextFloat() < f) {
            return true;
        }
        if (this.switchToBackupMode()) {
            return this.isArmedRecursive();
        } else {
            return false;
        }
    }

    protected static final int GERMAN_EL_MODE_INSTANT     = 1;
    protected static final int GERMAN_EL_MODE_SHORT_DELAY = 2;
    protected static final int GERMAN_EL_MODE_LONG_DELAY  = 3;
    protected static int       selectedFuzeMode           = 1;
    protected int              mirrorFuzeMode;
    protected int              releasedBombMode;
    private boolean            chargeGone;

}
