package com.maddox.il2.net;

import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.rts.*;
import java.io.IOException;
import java.util.ArrayList;

public class NetUserStat
{

    public NetUserStat()
    {
        score = 0.0D;
        enemyKill = new int[10];
        friendKill = new int[10];
        // TODO: Cheater Protection
        gotHitBullets = 0L;
        gotHitMassa = 0F;
        gotHitPower = 0F;
        gotHitCumulativePower = 0F;
        // ---
    }

    public void read(NetMsgInput netmsginput)
        throws IOException
    {
        int i = getNumOfScoreItems();
        nMissions = netmsginput.readInt();
        nSorties = netmsginput.readInt();
        nTakeoffs = netmsginput.readInt();
        nLandings = netmsginput.readInt();
        nDitches = netmsginput.readInt();
        nBails = netmsginput.readInt();
        nDeaths = netmsginput.readInt();
        nCaptures = netmsginput.readInt();
        tTotal = netmsginput.readFloat();
        tSingle = netmsginput.readFloat();
        tMulti = netmsginput.readFloat();
        tGunner = netmsginput.readFloat();
        tNight = netmsginput.readFloat();
        tIns = netmsginput.readFloat();
        tCCountry = netmsginput.readFloat();
        rating = netmsginput.readFloat();
        score = netmsginput.readFloat();
        for(int j = 0; j < i; j++)
            enemyKill[j] = netmsginput.readInt();

        for(int k = 0; k < i; k++)
            friendKill[k] = netmsginput.readInt();

        bulletsFire = netmsginput.readInt();
        bulletsHit = netmsginput.readInt();
        bulletsHitAir = netmsginput.readInt();
        rocketsFire = netmsginput.readInt();
        rocketsHit = netmsginput.readInt();
        bombFire = netmsginput.readInt();
        bombHit = netmsginput.readInt();
        curPlayerState = netmsginput.readByte();
    }

    public void write(NetMsgOutput netmsgoutput)
        throws IOException
    {
        netmsgoutput.writeInt(nMissions);
        netmsgoutput.writeInt(nSorties);
        netmsgoutput.writeInt(nTakeoffs);
        netmsgoutput.writeInt(nLandings);
        netmsgoutput.writeInt(nDitches);
        netmsgoutput.writeInt(nBails);
        netmsgoutput.writeInt(nDeaths);
        netmsgoutput.writeInt(nCaptures);
        netmsgoutput.writeFloat(tTotal);
        netmsgoutput.writeFloat(tSingle);
        netmsgoutput.writeFloat(tMulti);
        netmsgoutput.writeFloat(tGunner);
        netmsgoutput.writeFloat(tNight);
        netmsgoutput.writeFloat(tIns);
        netmsgoutput.writeFloat(tCCountry);
        netmsgoutput.writeFloat(rating);
        netmsgoutput.writeFloat((float)score);
        for(int i = 0; i < 10; i++)
            netmsgoutput.writeInt(enemyKill[i]);

        for(int j = 0; j < 10; j++)
            netmsgoutput.writeInt(friendKill[j]);

        netmsgoutput.writeInt(bulletsFire);
        netmsgoutput.writeInt(bulletsHit);
        netmsgoutput.writeInt(bulletsHitAir);
        netmsgoutput.writeInt(rocketsFire);
        netmsgoutput.writeInt(rocketsHit);
        netmsgoutput.writeInt(bombFire);
        netmsgoutput.writeInt(bombHit);
        netmsgoutput.writeByte(curPlayerState);
    }

    public boolean isEmpty()
    {
        if(nMissions != 0)
            return false;
        if(nSorties != 0)
            return false;
        if(nTakeoffs != 0)
            return false;
        if(nLandings != 0)
            return false;
        if(nDitches != 0)
            return false;
        if(nBails != 0)
            return false;
        if(nDeaths != 0)
            return false;
        if(nCaptures != 0)
            return false;
        if(tTotal != 0.0F)
            return false;
        if(tSingle != 0.0F)
            return false;
        if(tMulti != 0.0F)
            return false;
        if(tGunner != 0.0F)
            return false;
        if(tNight != 0.0F)
            return false;
        if(tIns != 0.0F)
            return false;
        if(tCCountry != 0.0F)
            return false;
        if(rating != 0.0F)
            return false;
        if(score != 0.0D)
            return false;
        for(int i = 0; i < 10; i++)
            if(enemyKill[i] != 0)
                return false;

        for(int j = 0; j < 10; j++)
            if(friendKill[j] != 0)
                return false;

        if(bulletsFire != 0)
            return false;
        if(bulletsHit != 0)
            return false;
        if(bulletsHitAir != 0)
            return false;
        if(rocketsFire != 0)
            return false;
        if(rocketsHit != 0)
            return false;
        if(bombFire != 0)
            return false;
        return bombHit == 0;
    }

    public boolean isEqualsCurrent(NetUserStat netuserstat)
    {
        if(score != netuserstat.score)
            return false;
        if(curPlayerState != netuserstat.curPlayerState)
            return false;
        for(int i = 0; i < 10; i++)
            if(enemyKill[i] != netuserstat.enemyKill[i])
                return false;

        for(int j = 0; j < 10; j++)
            if(friendKill[j] != netuserstat.friendKill[j])
                return false;

        if(bulletsFire != netuserstat.bulletsFire)
            return false;
        if(bulletsHit != netuserstat.bulletsHit)
            return false;
        if(bulletsHitAir != netuserstat.bulletsHitAir)
            return false;
        if(rocketsFire != netuserstat.rocketsFire)
            return false;
        if(rocketsHit != netuserstat.rocketsHit)
            return false;
        if(bombFire != netuserstat.bombFire)
            return false;
        return bombHit == netuserstat.bombHit;
    }

    public void clear()
    {
        nMissions = 0;
        nSorties = 0;
        nTakeoffs = 0;
        nLandings = 0;
        nDitches = 0;
        nBails = 0;
        nDeaths = 0;
        nCaptures = 0;
        tTotal = 0.0F;
        tSingle = 0.0F;
        tMulti = 0.0F;
        tGunner = 0.0F;
        tNight = 0.0F;
        tIns = 0.0F;
        tCCountry = 0.0F;
        rating = 0.0F;
        score = 0.0D;
        for(int i = 0; i < 10; i++)
            enemyKill[i] = 0;

        for(int j = 0; j < 10; j++)
            friendKill[j] = 0;

        bulletsFire = 0;
        bulletsHit = 0;
        bulletsHitAir = 0;
        rocketsFire = 0;
        rocketsHit = 0;
        bombFire = 0;
        bombHit = 0;
        curPlayerState = 4;
        
        // TODO: Cheater Protection
        gotHitBullets = 0L;
        gotHitMassa = 0F;
        gotHitPower = 0F;
        gotHitCumulativePower = 0F;
        // ---
        
    }

    public void set(NetUserStat netuserstat)
    {
        nMissions = netuserstat.nMissions;
        nSorties = netuserstat.nSorties;
        nTakeoffs = netuserstat.nTakeoffs;
        nLandings = netuserstat.nLandings;
        nDitches = netuserstat.nDitches;
        nBails = netuserstat.nBails;
        nDeaths = netuserstat.nDeaths;
        nCaptures = netuserstat.nCaptures;
        tTotal = netuserstat.tTotal;
        tSingle = netuserstat.tSingle;
        tMulti = netuserstat.tMulti;
        tGunner = netuserstat.tGunner;
        tNight = netuserstat.tNight;
        tIns = netuserstat.tIns;
        tCCountry = netuserstat.tCCountry;
        rating = netuserstat.rating;
        score = netuserstat.score;
        for(int i = 0; i < 10; i++)
            enemyKill[i] = netuserstat.enemyKill[i];

        for(int j = 0; j < 10; j++)
            friendKill[j] = netuserstat.friendKill[j];

        bulletsFire = netuserstat.bulletsFire;
        bulletsHit = netuserstat.bulletsHit;
        bulletsHitAir = netuserstat.bulletsHitAir;
        rocketsFire = netuserstat.rocketsFire;
        rocketsHit = netuserstat.rocketsHit;
        bombFire = netuserstat.bombFire;
        bombHit = netuserstat.bombHit;
        curPlayerState = netuserstat.curPlayerState;
    }

    public void inc(NetUserStat netuserstat)
    {
        nMissions += netuserstat.nMissions;
        nSorties += netuserstat.nSorties;
        nTakeoffs += netuserstat.nTakeoffs;
        nLandings += netuserstat.nLandings;
        nDitches += netuserstat.nDitches;
        nBails += netuserstat.nBails;
        nDeaths += netuserstat.nDeaths;
        nCaptures += netuserstat.nCaptures;
        tTotal += netuserstat.tTotal;
        tSingle += netuserstat.tSingle;
        tMulti += netuserstat.tMulti;
        tGunner += netuserstat.tGunner;
        tNight += netuserstat.tNight;
        tIns += netuserstat.tIns;
        tCCountry += netuserstat.tCCountry;
        rating += netuserstat.rating;
        score += netuserstat.score;
        for(int i = 0; i < 10; i++)
            enemyKill[i] += netuserstat.enemyKill[i];

        for(int j = 0; j < 10; j++)
            friendKill[j] += netuserstat.friendKill[j];

        bulletsFire += netuserstat.bulletsFire;
        bulletsHit += netuserstat.bulletsHit;
        bulletsHitAir += netuserstat.bulletsHitAir;
        rocketsFire += netuserstat.rocketsFire;
        rocketsHit += netuserstat.rocketsHit;
        bombFire += netuserstat.bombFire;
        bombHit += netuserstat.bombHit;
        curPlayerState = netuserstat.curPlayerState;
    }

    public void fillFromScoreCounter(boolean flag)
    {
        ScoreCounter scorecounter = World.cur().scoreCounter;
        double d = 0.0D;
        ArrayList arraylist = scorecounter.enemyItems;
        for(int i = 0; i < arraylist.size(); i++)
        {
            ScoreItem scoreitem = (ScoreItem)arraylist.get(i);
            enemyKill[scoreitem.type]++;
            d += scoreitem.score;
        }

        double d1 = 0.0D;
        arraylist = scorecounter.friendItems;
        for(int j = 0; j < arraylist.size(); j++)
        {
            ScoreItem scoreitem1 = (ScoreItem)arraylist.get(j);
            friendKill[scoreitem1.type]++;
            d1 += scoreitem1.score;
        }

        if(scorecounter.bPlayerDead)
            d /= 10D;
        else
        if(scorecounter.bPlayerCaptured)
            d = (d * 2D) / 10D;
        else
        if(scorecounter.bLandedFarAirdrome)
            d = (d * 7D) / 10D;
        else
        if(scorecounter.bPlayerParatrooper)
            d /= 2D;
        score += d - d1;
        bulletsFire = scorecounter.bulletsFire;
        bulletsHit = scorecounter.bulletsHit;
        bulletsHitAir = scorecounter.bulletsHitAir;
        rocketsFire = scorecounter.rocketsFire;
        rocketsHit = scorecounter.rocketsHit;
        bombFire = scorecounter.bombFire;
        bombHit = scorecounter.bombHit;
        if(Mission.cur() != null)
        {
            Mission.cur();
            if(!Mission.isPlaying())
                nMissions = 1;
        }
        nSorties = 1;
        nTakeoffs = scorecounter.nPlayerTakeoffs;
        nLandings = scorecounter.nPlayerLandings;
        nDitches = scorecounter.nPlayerDitches;
        if(World.isPlayerParatrooper())
            nBails = 1;
        if(scorecounter.bPlayerDead)
            nDeaths = 1;
        if(scorecounter.bPlayerCaptured)
            nCaptures = 1;
        if(scorecounter.timeStart != -1L)
        {
            tTotal = (float)(Time.currentReal() - scorecounter.timeStart) * 0.001F;
            switch(scorecounter.player_is)
            {
            case 0: // '\0'
                tSingle = tTotal;
                break;

            case 1: // '\001'
                tMulti = tTotal;
                break;

            case 2: // '\002'
                tGunner = tTotal;
                break;
            }
            if(World.land() != null)
                tNight = World.land().nightTime(scorecounter.todStart, (int)tTotal);
            if(Config.isUSE_RENDER())
            {
                Main3D main3d = Main3D.cur3D();
                if(main3d.clouds != null && main3d.clouds.type() > 2)
                    tIns = tTotal;
            }
            if(scorecounter.bCrossCountry)
                tCCountry = tTotal;
        }
        scorecounter.playerUpdateState();
        curPlayerState = 0;
        if(scorecounter.bPlayerDead)
            curPlayerState |= 1;
        if(scorecounter.bPlayerParatrooper)
            curPlayerState |= 2;
        if(scorecounter.bLanded)
            curPlayerState |= 4;
        if(scorecounter.bLandedFarAirdrome)
            curPlayerState |= 8;
        if(scorecounter.bPlayerCaptured)
            curPlayerState |= 0x10;
        if(scorecounter.bPlayerStateUnknown)
            curPlayerState |= 0x20;
        if(flag)
            scorecounter.resetGame();
    }

    private int getNumOfScoreItems()
    {
        if(!NetMissionTrack.isPlaying())
            return 10;
        int i = NetMissionTrack.playingVersion();
        return i >= 103 ? 10 : 9;
    }

    public int nMissions;
    public int nSorties;
    public int nTakeoffs;
    public int nLandings;
    public int nDitches;
    public int nBails;
    public int nDeaths;
    public int nCaptures;
    public float tTotal;
    public float tSingle;
    public float tMulti;
    public float tGunner;
    public float tNight;
    public float tIns;
    public float tCCountry;
    public float rating;
    public double score;
    public int enemyKill[];
    public int friendKill[];
    public int bulletsFire;
    public int bulletsHit;
    public int bulletsHitAir;
    public int rocketsFire;
    public int rocketsHit;
    public int bombFire;
    public int bombHit;
    public int curPlayerState;
    // TODO: Cheater Protection
    public long gotHitBullets;
    public float gotHitMassa;
    public float gotHitPower;
    public float gotHitCumulativePower;
    // ---
    public static final int PLAYER_STATE_DEAD = 1;
    public static final int PLAYER_STATE_PARATROOPER = 2;
    public static final int PLAYER_STATE_LANDED = 4;
    public static final int PLAYER_STATE_LANDEDFARAIRDROME = 8;
    public static final int PLAYER_STATE_CAPTURED = 16;
    public static final int PLAYER_STATE_UNKNOWN = 32;
}
