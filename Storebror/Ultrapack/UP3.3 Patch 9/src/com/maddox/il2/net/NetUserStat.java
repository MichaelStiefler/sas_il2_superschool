package com.maddox.il2.net;

import java.io.IOException;
import java.util.ArrayList;

import com.maddox.il2.ai.ScoreCounter;
import com.maddox.il2.ai.ScoreItem;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetMsgOutput;
import com.maddox.rts.Time;

public class NetUserStat {

    public NetUserStat() {
        this.score = 0.0D;
        this.enemyKill = new int[10];
        this.friendKill = new int[10];
        // TODO: Cheater Protection
        this.gotHitBullets = 0L;
        this.gotHitMassa = 0F;
        this.gotHitPower = 0F;
        this.gotHitCumulativePower = 0F;
        // ---
    }

    public void read(NetMsgInput netmsginput) throws IOException {
        int i = this.getNumOfScoreItems();
        this.nMissions = netmsginput.readInt();
        this.nSorties = netmsginput.readInt();
        this.nTakeoffs = netmsginput.readInt();
        this.nLandings = netmsginput.readInt();
        this.nDitches = netmsginput.readInt();
        this.nBails = netmsginput.readInt();
        this.nDeaths = netmsginput.readInt();
        this.nCaptures = netmsginput.readInt();
        this.tTotal = netmsginput.readFloat();
        this.tSingle = netmsginput.readFloat();
        this.tMulti = netmsginput.readFloat();
        this.tGunner = netmsginput.readFloat();
        this.tNight = netmsginput.readFloat();
        this.tIns = netmsginput.readFloat();
        this.tCCountry = netmsginput.readFloat();
        this.rating = netmsginput.readFloat();
        this.score = netmsginput.readFloat();
        for (int j = 0; j < i; j++)
            this.enemyKill[j] = netmsginput.readInt();

        for (int k = 0; k < i; k++)
            this.friendKill[k] = netmsginput.readInt();

        this.bulletsFire = netmsginput.readInt();
        this.bulletsHit = netmsginput.readInt();
        this.bulletsHitAir = netmsginput.readInt();
        this.rocketsFire = netmsginput.readInt();
        this.rocketsHit = netmsginput.readInt();
        this.bombFire = netmsginput.readInt();
        this.bombHit = netmsginput.readInt();
        this.curPlayerState = netmsginput.readByte();
//        System.out.println("NetUserStat read score=" + this.score);
    }

    public void write(NetMsgOutput netmsgoutput) throws IOException {
//        System.out.println("NetUserStat write score=" + this.score);
        netmsgoutput.writeInt(this.nMissions);
        netmsgoutput.writeInt(this.nSorties);
        netmsgoutput.writeInt(this.nTakeoffs);
        netmsgoutput.writeInt(this.nLandings);
        netmsgoutput.writeInt(this.nDitches);
        netmsgoutput.writeInt(this.nBails);
        netmsgoutput.writeInt(this.nDeaths);
        netmsgoutput.writeInt(this.nCaptures);
        netmsgoutput.writeFloat(this.tTotal);
        netmsgoutput.writeFloat(this.tSingle);
        netmsgoutput.writeFloat(this.tMulti);
        netmsgoutput.writeFloat(this.tGunner);
        netmsgoutput.writeFloat(this.tNight);
        netmsgoutput.writeFloat(this.tIns);
        netmsgoutput.writeFloat(this.tCCountry);
        netmsgoutput.writeFloat(this.rating);
        netmsgoutput.writeFloat((float) this.score);
        for (int i = 0; i < 10; i++)
            netmsgoutput.writeInt(this.enemyKill[i]);

        for (int j = 0; j < 10; j++)
            netmsgoutput.writeInt(this.friendKill[j]);

        netmsgoutput.writeInt(this.bulletsFire);
        netmsgoutput.writeInt(this.bulletsHit);
        netmsgoutput.writeInt(this.bulletsHitAir);
        netmsgoutput.writeInt(this.rocketsFire);
        netmsgoutput.writeInt(this.rocketsHit);
        netmsgoutput.writeInt(this.bombFire);
        netmsgoutput.writeInt(this.bombHit);
        netmsgoutput.writeByte(this.curPlayerState);
    }

    public boolean isEmpty() {
        if (this.nMissions != 0) return false;
        if (this.nSorties != 0) return false;
        if (this.nTakeoffs != 0) return false;
        if (this.nLandings != 0) return false;
        if (this.nDitches != 0) return false;
        if (this.nBails != 0) return false;
        if (this.nDeaths != 0) return false;
        if (this.nCaptures != 0) return false;
        if (this.tTotal != 0.0F) return false;
        if (this.tSingle != 0.0F) return false;
        if (this.tMulti != 0.0F) return false;
        if (this.tGunner != 0.0F) return false;
        if (this.tNight != 0.0F) return false;
        if (this.tIns != 0.0F) return false;
        if (this.tCCountry != 0.0F) return false;
        if (this.rating != 0.0F) return false;
        if (this.score != 0.0D) return false;
        for (int i = 0; i < 10; i++)
            if (this.enemyKill[i] != 0) return false;

        for (int j = 0; j < 10; j++)
            if (this.friendKill[j] != 0) return false;

        if (this.bulletsFire != 0) return false;
        if (this.bulletsHit != 0) return false;
        if (this.bulletsHitAir != 0) return false;
        if (this.rocketsFire != 0) return false;
        if (this.rocketsHit != 0) return false;
        if (this.bombFire != 0) return false;
        return this.bombHit == 0;
    }

    public boolean isEqualsCurrent(NetUserStat netuserstat) {
//        System.out.println("NetUserStat isEqualsCurrent score=" + this.score + ", netuserstat.score=" + netuserstat.score);
        if (this.score != netuserstat.score) return false;
        if (this.curPlayerState != netuserstat.curPlayerState) return false;
        for (int i = 0; i < 10; i++)
            if (this.enemyKill[i] != netuserstat.enemyKill[i]) return false;

        for (int j = 0; j < 10; j++)
            if (this.friendKill[j] != netuserstat.friendKill[j]) return false;

        if (this.bulletsFire != netuserstat.bulletsFire) return false;
        if (this.bulletsHit != netuserstat.bulletsHit) return false;
        if (this.bulletsHitAir != netuserstat.bulletsHitAir) return false;
        if (this.rocketsFire != netuserstat.rocketsFire) return false;
        if (this.rocketsHit != netuserstat.rocketsHit) return false;
        if (this.bombFire != netuserstat.bombFire) return false;
        return this.bombHit == netuserstat.bombHit;
    }

    public void clear() {
//        System.out.println("NetUserStat clear");
        this.nMissions = 0;
        this.nSorties = 0;
        this.nTakeoffs = 0;
        this.nLandings = 0;
        this.nDitches = 0;
        this.nBails = 0;
        this.nDeaths = 0;
        this.nCaptures = 0;
        this.tTotal = 0.0F;
        this.tSingle = 0.0F;
        this.tMulti = 0.0F;
        this.tGunner = 0.0F;
        this.tNight = 0.0F;
        this.tIns = 0.0F;
        this.tCCountry = 0.0F;
        this.rating = 0.0F;
        this.score = 0.0D;
        for (int i = 0; i < 10; i++)
            this.enemyKill[i] = 0;

        for (int j = 0; j < 10; j++)
            this.friendKill[j] = 0;

        this.bulletsFire = 0;
        this.bulletsHit = 0;
        this.bulletsHitAir = 0;
        this.rocketsFire = 0;
        this.rocketsHit = 0;
        this.bombFire = 0;
        this.bombHit = 0;
        this.curPlayerState = 4;

        // TODO: Cheater Protection
        this.gotHitBullets = 0L;
        this.gotHitMassa = 0F;
        this.gotHitPower = 0F;
        this.gotHitCumulativePower = 0F;
        // ---

    }

    public void set(NetUserStat netuserstat) {
//        System.out.println("NetUserStat set netuserstat.score=" + netuserstat.score);
        this.nMissions = netuserstat.nMissions;
        this.nSorties = netuserstat.nSorties;
        this.nTakeoffs = netuserstat.nTakeoffs;
        this.nLandings = netuserstat.nLandings;
        this.nDitches = netuserstat.nDitches;
        this.nBails = netuserstat.nBails;
        this.nDeaths = netuserstat.nDeaths;
        this.nCaptures = netuserstat.nCaptures;
        this.tTotal = netuserstat.tTotal;
        this.tSingle = netuserstat.tSingle;
        this.tMulti = netuserstat.tMulti;
        this.tGunner = netuserstat.tGunner;
        this.tNight = netuserstat.tNight;
        this.tIns = netuserstat.tIns;
        this.tCCountry = netuserstat.tCCountry;
        this.rating = netuserstat.rating;
        this.score = netuserstat.score;
        for (int i = 0; i < 10; i++)
            this.enemyKill[i] = netuserstat.enemyKill[i];

        for (int j = 0; j < 10; j++)
            this.friendKill[j] = netuserstat.friendKill[j];

        this.bulletsFire = netuserstat.bulletsFire;
        this.bulletsHit = netuserstat.bulletsHit;
        this.bulletsHitAir = netuserstat.bulletsHitAir;
        this.rocketsFire = netuserstat.rocketsFire;
        this.rocketsHit = netuserstat.rocketsHit;
        this.bombFire = netuserstat.bombFire;
        this.bombHit = netuserstat.bombHit;
        this.curPlayerState = netuserstat.curPlayerState;
    }

    public void inc(NetUserStat netuserstat) {
//        System.out.println("NetUserStat inc score=" + this.score + ", netuserstat.score=" + netuserstat.score);
        this.nMissions += netuserstat.nMissions;
        this.nSorties += netuserstat.nSorties;
        this.nTakeoffs += netuserstat.nTakeoffs;
        this.nLandings += netuserstat.nLandings;
        this.nDitches += netuserstat.nDitches;
        this.nBails += netuserstat.nBails;
        this.nDeaths += netuserstat.nDeaths;
        this.nCaptures += netuserstat.nCaptures;
        this.tTotal += netuserstat.tTotal;
        this.tSingle += netuserstat.tSingle;
        this.tMulti += netuserstat.tMulti;
        this.tGunner += netuserstat.tGunner;
        this.tNight += netuserstat.tNight;
        this.tIns += netuserstat.tIns;
        this.tCCountry += netuserstat.tCCountry;
        this.rating += netuserstat.rating;
        this.score += netuserstat.score;
        for (int i = 0; i < 10; i++)
            this.enemyKill[i] += netuserstat.enemyKill[i];

        for (int j = 0; j < 10; j++)
            this.friendKill[j] += netuserstat.friendKill[j];

        this.bulletsFire += netuserstat.bulletsFire;
        this.bulletsHit += netuserstat.bulletsHit;
        this.bulletsHitAir += netuserstat.bulletsHitAir;
        this.rocketsFire += netuserstat.rocketsFire;
        this.rocketsHit += netuserstat.rocketsHit;
        this.bombFire += netuserstat.bombFire;
        this.bombHit += netuserstat.bombHit;
        this.curPlayerState = netuserstat.curPlayerState;
    }

    public void fillFromScoreCounter(boolean flag) {
        ScoreCounter scorecounter = World.cur().scoreCounter;
        double d = 0.0D;
        ArrayList arraylist = scorecounter.enemyItems;
        for (int i = 0; i < arraylist.size(); i++) {
            ScoreItem scoreitem = (ScoreItem) arraylist.get(i);
            this.enemyKill[scoreitem.type]++;
            d += scoreitem.score;
        }

        double d1 = 0.0D;
        arraylist = scorecounter.friendItems;
        for (int j = 0; j < arraylist.size(); j++) {
            ScoreItem scoreitem1 = (ScoreItem) arraylist.get(j);
            this.friendKill[scoreitem1.type]++;
            d1 += scoreitem1.score;
        }

        if (scorecounter.bPlayerDead) d /= 10D;
        else if (scorecounter.bPlayerCaptured) d = d * 2D / 10D;
        else if (scorecounter.bLandedFarAirdrome) d = d * 7D / 10D;
        else if (scorecounter.bPlayerParatrooper) d /= 2D;
//        System.out.println("NetUserStat fillFromScoreCounter score=" + this.score + ", enemyKill=" + d + ", friendKill=" + d1);
        this.score += d - d1;
        this.bulletsFire = scorecounter.bulletsFire;
        this.bulletsHit = scorecounter.bulletsHit;
        this.bulletsHitAir = scorecounter.bulletsHitAir;
        this.rocketsFire = scorecounter.rocketsFire;
        this.rocketsHit = scorecounter.rocketsHit;
        this.bombFire = scorecounter.bombFire;
        this.bombHit = scorecounter.bombHit;
        if (Mission.cur() != null) {
            Mission.cur();
            if (!Mission.isPlaying()) this.nMissions = 1;
        }
        this.nSorties = 1;
        this.nTakeoffs = scorecounter.nPlayerTakeoffs;
        this.nLandings = scorecounter.nPlayerLandings;
        this.nDitches = scorecounter.nPlayerDitches;
        if (World.isPlayerParatrooper()) this.nBails = 1;
        if (scorecounter.bPlayerDead) this.nDeaths = 1;
        if (scorecounter.bPlayerCaptured) this.nCaptures = 1;
        if (scorecounter.timeStart != -1L) {
            this.tTotal = (Time.currentReal() - scorecounter.timeStart) * 0.001F;
            switch (scorecounter.player_is) {
                case 0: // '\0'
                    this.tSingle = this.tTotal;
                    break;

                case 1: // '\001'
                    this.tMulti = this.tTotal;
                    break;

                case 2: // '\002'
                    this.tGunner = this.tTotal;
                    break;
            }
            if (World.land() != null) this.tNight = World.land().nightTime(scorecounter.todStart, (int) this.tTotal);
            if (Config.isUSE_RENDER()) {
                Main3D main3d = Main3D.cur3D();
                if (main3d.clouds != null && main3d.clouds.type() > 2) this.tIns = this.tTotal;
            }
            if (scorecounter.bCrossCountry) this.tCCountry = this.tTotal;
        }
        scorecounter.playerUpdateState();
        this.curPlayerState = 0;
        if (scorecounter.bPlayerDead) this.curPlayerState |= 1;
        if (scorecounter.bPlayerParatrooper) this.curPlayerState |= 2;
        if (scorecounter.bLanded) this.curPlayerState |= 4;
        if (scorecounter.bLandedFarAirdrome) this.curPlayerState |= 8;
        if (scorecounter.bPlayerCaptured) this.curPlayerState |= 0x10;
        if (scorecounter.bPlayerStateUnknown) this.curPlayerState |= 0x20;
        if (flag) scorecounter.resetGame();
    }

    private int getNumOfScoreItems() {
        if (!NetMissionTrack.isPlaying()) return 10;
        int i = NetMissionTrack.playingVersion();
        return i >= 103 ? 10 : 9;
    }

    public int              nMissions;
    public int              nSorties;
    public int              nTakeoffs;
    public int              nLandings;
    public int              nDitches;
    public int              nBails;
    public int              nDeaths;
    public int              nCaptures;
    public float            tTotal;
    public float            tSingle;
    public float            tMulti;
    public float            tGunner;
    public float            tNight;
    public float            tIns;
    public float            tCCountry;
    public float            rating;
    public double           score;
    public int              enemyKill[];
    public int              friendKill[];
    public int              bulletsFire;
    public int              bulletsHit;
    public int              bulletsHitAir;
    public int              rocketsFire;
    public int              rocketsHit;
    public int              bombFire;
    public int              bombHit;
    public int              curPlayerState;
    // TODO: Cheater Protection
    public long             gotHitBullets;
    public float            gotHitMassa;
    public float            gotHitPower;
    public float            gotHitCumulativePower;
    // ---
    public static final int PLAYER_STATE_DEAD              = 1;
    public static final int PLAYER_STATE_PARATROOPER       = 2;
    public static final int PLAYER_STATE_LANDED            = 4;
    public static final int PLAYER_STATE_LANDEDFARAIRDROME = 8;
    public static final int PLAYER_STATE_CAPTURED          = 16;
    public static final int PLAYER_STATE_UNKNOWN           = 32;
}
