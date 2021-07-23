package com.maddox.il2.objects.air;

import java.io.IOException;

import com.maddox.il2.game.AircraftHotKeys;
import com.maddox.il2.game.HUD;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;

public class P_47ModPackAceMakerGunsight extends P_47ModPack implements TypeFighterAceMaker {

    public int   k14Mode;
    public int   k14WingspanType;
    public float k14Distance;

    public P_47ModPackAceMakerGunsight() {
        this.k14Mode = 0;
        this.k14WingspanType = 0;
        this.k14Distance = 200.0F;
    }

    public boolean typeFighterAceMakerToggleAutomation() {
        this.k14Mode += 1;
        if (this.k14Mode > 2) {
            this.k14Mode = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerMode" + this.k14Mode);
        return true;
    }

    public void typeFighterAceMakerAdjDistanceReset() {
    }

    //-------------------------------------------------------------------------------------------------------
    /* TODO: skylla: gyro-gunsight distance HUD log:
     * Infos & links:
     *  more Information, maybe some on the max/min distance? (one must be logged in to access the important stuff :S)
     *      https://ww2aircraft.net/forum/threads/k-14-gunsight.3415/
     *      "Note: when engaging in aerobatics the gyro's needed to be caged. If the pilot forgot to do so the system would
     *       be damaged and useless. When the gyros were caged, the system acted as a simple reflector gunsight.
     *       So the pilot had to be well trained to cage and uncage the gyros during combat."
     *  range (K-14):                       range (Mk II gyro):                 range: (EZ 42):
     *      182,88m - 731,52m                   164,59m - 731,52m                   80m - 1000m (source: http://forums.eagle.ru/showthread.php?t=128295&page=4)
     *      (600ft - 2400ft ) (source: see above)
     *      (200yds - 800yds)                   (180yds - 800yds) (source: http://forum.axishistory.com/viewtopic.php?t=17850&start=15)
     *
     *
     */

    public void typeFighterAceMakerAdjDistancePlus() {
        this.adjustK14AceMakerDistance(true);
    }

    public void typeFighterAceMakerAdjDistanceMinus() {
        this.adjustK14AceMakerDistance(false);
    }

    private void adjustK14AceMakerDistance(boolean increaseDistance) {
        int units = HUD.drawSpeed() >= P_47ModPackAceMakerGunsight.unitsIndex.length ? 0 : P_47ModPackAceMakerGunsight.unitsIndex[HUD.drawSpeed()];
        float tempDistance = this.k14Distance / P_47ModPackAceMakerGunsight.k14Const[units][0];
        tempDistance = Math.max(P_47ModPackAceMakerGunsight.k14Const[units][1], Math.min(Math.round((tempDistance + (P_47ModPackAceMakerGunsight.k14Const[units][2] * (increaseDistance ? 1F : -1F))) / P_47ModPackAceMakerGunsight.k14Const[units][2]) * P_47ModPackAceMakerGunsight.k14Const[units][2], P_47ModPackAceMakerGunsight.k14Const[units][3]));
        this.k14Distance = tempDistance * P_47ModPackAceMakerGunsight.k14Const[units][0];
        HUD.log(AircraftHotKeys.hudLogWeaponId, P_47ModPackAceMakerGunsight.k14Log[units], new Object[] { new Integer((int) (tempDistance)) });
    }

    private static final float[][] k14Const   = { { 1F, 180F, 10F, 730F }, { 0.9144F, 200F, 10F, 800F }, { 0.3048F, 600F, 30F, 2400F } };
    private static final String[]  k14Log     = { "K14AceMakerDistSI", "K14AceMakerDistGB", "K14AceMakerDistUS" };
    private static final int[]     unitsIndex = { 0, 0, 1, 2, 0, 1, 2 };

    //-------------------------------------------------------------------------------------------------------

    public void typeFighterAceMakerAdjSideslipReset() {
    }

    public void typeFighterAceMakerAdjSideslipPlus() {
        this.k14WingspanType -= 1;
        if (this.k14WingspanType < 0) {
            this.k14WingspanType = 0;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerAdjSideslipMinus() {
        this.k14WingspanType += 1;
        if (this.k14WingspanType > 9) {
            this.k14WingspanType = 9;
        }
        HUD.log(AircraftHotKeys.hudLogWeaponId, "K14AceMakerWing" + this.k14WingspanType);
    }

    public void typeFighterAceMakerReplicateToNet(NetMsgGuaranted paramNetMsgGuaranted) throws IOException {
        paramNetMsgGuaranted.writeByte(this.k14Mode);
        paramNetMsgGuaranted.writeByte(this.k14WingspanType);
        paramNetMsgGuaranted.writeFloat(this.k14Distance);
    }

    public void typeFighterAceMakerReplicateFromNet(NetMsgInput paramNetMsgInput) throws IOException {
        this.k14Mode = paramNetMsgInput.readByte();
        this.k14WingspanType = paramNetMsgInput.readByte();
        this.k14Distance = paramNetMsgInput.readFloat();
    }
}
