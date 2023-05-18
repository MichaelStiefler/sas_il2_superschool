package com.maddox.il2.gui;

import com.maddox.gwindow.*;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.*;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;

public class GUIQuickStats extends GameState
{
    public static class Table extends GWindowTable
    {

        public int countRows()
        {
            return 24;
        }

        public void renderCell(int i, int j, boolean flag, float f, float f1)
        {
            String s = tableElements[i][j];
            int k = 0;
            if(j > 0)
                k = 1;
            if(i % 2 == 0)
                setCanvasColor(GWindowLookAndFeel.getUIBackgroundGColor(0));
            else
                setCanvasColor(GWindowLookAndFeel.getUIBackgroundGColor(20));
            draw(0.0F, 0.0F, f, f1, lookAndFeel().regionWhite);
            setCanvasColorBLACK();
            draw(0.0F, 0.0F, f, f1, k, s);
        }

        public void afterCreated()
        {
            super.afterCreated();
            addColumn("", null);
            addColumn(I18N.gui("q.LastMission"), null);
            addColumn(I18N.gui("q.CurrentSession"), null);
            addColumn(I18N.gui("q.Career"), null);
            getColumn(0).setRelativeDx(34F);
            for(int i = 1; i < 4; i++)
                getColumn(i).setRelativeDx(22F);

            alignColumns();
            vSB.scroll = rowHeight(0);
            resized();
        }

        public void resolutionChanged()
        {
            vSB.scroll = rowHeight(0);
            super.resolutionChanged();
        }

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(super.notify(gwindow, i, j))
            {
                return true;
            } else
            {
                notify(i, j);
                return false;
            }
        }

        public String tableElements[][];

        public Table(GWindow gwindow)
        {
            super(gwindow);
            tableElements = new String[24][4];
            bNotify = true;
            wClient.bNotify = true;
        }
    }

    public class DialogClient extends GUIDialogClient
    {

        public boolean notify(GWindow gwindow, int i, int j)
        {
            if(i != 2)
                return super.notify(gwindow, i, j);
            if(gwindow == bPrev)
            {
                Main.stateStack().pop();
                return true;
            }
            if(gwindow == bRes)
            {
                GUIQuickStats.resetQMBStat();
                fillTableElements();
                return true;
            } else
            {
                return super.notify(gwindow, i, j);
            }
        }

        public void render()
        {
            super.render();
            GUISeparate.draw(this, GColor.Gray, x1024(48F), y1024(628F), x1024(924F), 2.0F);
            setCanvasColor(GColor.Gray);
            setCanvasFont(0);
            draw(x1024(0.0F), y1024(636F), x1024(170F), M(2.0F), 1, i18n("q.BAC"));
            draw(x1024(285F), y1024(636F), x1024(170F), M(2.0F), 1, i18n("q.RES"));
        }

        public void setPosSize()
        {
            set1024PosSize(0.0F, 32F, 1024F, 736F);
            bPrev.setPosC(x1024(85F), y1024(689F));
            bRes.setPosC(x1024(370F), y1024(689F));
            wScrollStats.set1024PosSize(80F, 32F, 880F, 540F);
        }

        public DialogClient()
        {
        }
    }


    public void _enter()
    {
        loadQMBStat();
        fillTableElements();
        wScrollStats.resized();
        client.activateWindow();
    }

    private void fillTableElements()
    {
        wScrollStats.tableElements[0][0] = "  " + i18n("q.BFire");
        wScrollStats.tableElements[1][0] = "  " + i18n("q.AHit");
        wScrollStats.tableElements[2][0] = "  " + i18n("q.Air%");
        wScrollStats.tableElements[3][0] = "  " + i18n("q.GHit");
        wScrollStats.tableElements[4][0] = "  " + i18n("q.Ground%");
        wScrollStats.tableElements[5][0] = "  " + i18n("q.BombFire");
        wScrollStats.tableElements[6][0] = "  " + i18n("q.BombHit");
        wScrollStats.tableElements[7][0] = "  " + i18n("q.BombPct");
        wScrollStats.tableElements[8][0] = "  " + i18n("q.RockFire");
        wScrollStats.tableElements[9][0] = "  " + i18n("q.RockHit");
        wScrollStats.tableElements[10][0] = "  " + i18n("q.RockPct");
        wScrollStats.tableElements[11][0] = "  " + i18n("q.AirKill");
        wScrollStats.tableElements[12][0] = "  " + i18n("q.GroundKill");
        wScrollStats.tableElements[13][0] = "  " + i18n("q.TankKill");
        wScrollStats.tableElements[14][0] = "  " + i18n("q.CarKill");
        wScrollStats.tableElements[15][0] = "  " + i18n("q.ArtKill");
        wScrollStats.tableElements[16][0] = "  " + i18n("q.AAAKill");
        wScrollStats.tableElements[17][0] = "  " + i18n("q.TrainKill");
        wScrollStats.tableElements[18][0] = "  " + i18n("q.AirStaticKill");
        wScrollStats.tableElements[19][0] = "  " + i18n("q.ShipKill");
        wScrollStats.tableElements[20][0] = "  " + i18n("q.BridgeKill");
        wScrollStats.tableElements[21][0] = "  " + i18n("q.Bailout");
        wScrollStats.tableElements[22][0] = "  " + i18n("q.Dead");
        wScrollStats.tableElements[23][0] = "  " + i18n("q.Score");
        wScrollStats.tableElements[0][1] = "" + qmbBulletsFired;
        wScrollStats.tableElements[1][1] = "" + qmbBulletsHitAir;
        wScrollStats.tableElements[2][1] = "" + qmbPercentageAir;
        wScrollStats.tableElements[3][1] = "" + qmbBulletsFiredHitGround;
        wScrollStats.tableElements[4][1] = "" + qmbPercentageGround;
        wScrollStats.tableElements[5][1] = "" + qmbMissionBombsFired;
        wScrollStats.tableElements[6][1] = "" + qmbMissionBombsHit;
        wScrollStats.tableElements[7][1] = "" + qmbMissionPctBomb;
        wScrollStats.tableElements[8][1] = "" + qmbMissionRocketsFired;
        wScrollStats.tableElements[9][1] = "" + qmbMissionRocketsHit;
        wScrollStats.tableElements[10][1] = "" + qmbMissionPctRocket;
        wScrollStats.tableElements[11][1] = "" + qmbMissionAirKill;
        wScrollStats.tableElements[12][1] = "" + qmbMissionGroundKill;
        wScrollStats.tableElements[13][1] = "" + qmbMissionTankKill;
        wScrollStats.tableElements[14][1] = "" + qmbMissionCarKill;
        wScrollStats.tableElements[15][1] = "" + qmbMissionArtilleryKill;
        wScrollStats.tableElements[16][1] = "" + qmbMissionAAAKill;
        wScrollStats.tableElements[17][1] = "" + qmbMissionTrainKill;
        wScrollStats.tableElements[18][1] = "" + qmbMissionAirStaticKill;
        wScrollStats.tableElements[19][1] = "" + qmbMissionShipKill;
        wScrollStats.tableElements[20][1] = "" + qmbMissionBridgeKill;
        wScrollStats.tableElements[21][1] = "" + qmbPara;
        wScrollStats.tableElements[22][1] = "" + qmbDead;
        wScrollStats.tableElements[23][1] = "" + qmbScore;
        wScrollStats.tableElements[0][2] = "" + qmbSessionBulletsFired;
        wScrollStats.tableElements[1][2] = "" + qmbSessionBulletsHitAir;
        wScrollStats.tableElements[2][2] = "" + qmbSessionPctAir;
        wScrollStats.tableElements[3][2] = "" + qmbSessionBulletsFiredHitGround;
        wScrollStats.tableElements[4][2] = "" + qmbSessionPctGround;
        wScrollStats.tableElements[5][2] = "" + qmbSessionBombsFired;
        wScrollStats.tableElements[6][2] = "" + qmbSessionBombsHit;
        wScrollStats.tableElements[7][2] = "" + qmbSessionPctBomb;
        wScrollStats.tableElements[8][2] = "" + qmbSessionRocketsFired;
        wScrollStats.tableElements[9][2] = "" + qmbSessionRocketsHit;
        wScrollStats.tableElements[10][2] = "" + qmbSessionPctRocket;
        wScrollStats.tableElements[11][2] = "" + qmbSessionAirKill;
        wScrollStats.tableElements[12][2] = "" + qmbSessionGroundKill;
        wScrollStats.tableElements[13][2] = "" + qmbSessionTankKill;
        wScrollStats.tableElements[14][2] = "" + qmbSessionCarKill;
        wScrollStats.tableElements[15][2] = "" + qmbSessionArtilleryKill;
        wScrollStats.tableElements[16][2] = "" + qmbSessionAAAKill;
        wScrollStats.tableElements[17][2] = "" + qmbSessionTrainKill;
        wScrollStats.tableElements[18][2] = "" + qmbSessionAirStaticKill;
        wScrollStats.tableElements[19][2] = "" + qmbSessionShipKill;
        wScrollStats.tableElements[20][2] = "" + qmbSessionBridgeKill;
        wScrollStats.tableElements[21][2] = "" + qmbSessionPara;
        wScrollStats.tableElements[22][2] = "" + qmbSessionDead;
        wScrollStats.tableElements[23][2] = "" + qmbSessionScore;
        wScrollStats.tableElements[0][3] = "" + qmbTotalBulletsFired;
        wScrollStats.tableElements[1][3] = "" + qmbTotalBulletsHitAir;
        wScrollStats.tableElements[2][3] = "" + qmbTotalPctAir;
        wScrollStats.tableElements[3][3] = "" + qmbTotalBulletsFiredHitGround;
        wScrollStats.tableElements[4][3] = "" + qmbTotalPctGround;
        wScrollStats.tableElements[5][3] = "" + qmbTotalBombsFired;
        wScrollStats.tableElements[6][3] = "" + qmbTotalBombsHit;
        wScrollStats.tableElements[7][3] = "" + qmbTotalPctBomb;
        wScrollStats.tableElements[8][3] = "" + qmbTotalRocketsFired;
        wScrollStats.tableElements[9][3] = "" + qmbTotalRocketsHit;
        wScrollStats.tableElements[10][3] = "" + qmbTotalPctRocket;
        wScrollStats.tableElements[11][3] = "" + qmbTotalAirKill;
        wScrollStats.tableElements[12][3] = "" + qmbTotalGroundKill;
        wScrollStats.tableElements[13][3] = "" + qmbTotalTankKill;
        wScrollStats.tableElements[14][3] = "" + qmbTotalCarKill;
        wScrollStats.tableElements[15][3] = "" + qmbTotalArtilleryKill;
        wScrollStats.tableElements[16][3] = "" + qmbTotalAAAKill;
        wScrollStats.tableElements[17][3] = "" + qmbTotalTrainKill;
        wScrollStats.tableElements[18][3] = "" + qmbTotalAirStaticKill;
        wScrollStats.tableElements[19][3] = "" + qmbTotalShipKill;
        wScrollStats.tableElements[20][3] = "" + qmbTotalBridgeKill;
        wScrollStats.tableElements[21][3] = "" + qmbTotalPara;
        wScrollStats.tableElements[22][3] = "" + qmbTotalDead;
        wScrollStats.tableElements[23][3] = "" + qmbTotalScore;
    }

    public void _leave()
    {
        saveStat();
        client.hideWindow();
    }

    public static void saveStat()
    {
        String s = "users/" + World.cur().userCfg.sId + "/QMB.ini";
        SectFile sectfile = new SectFile(s, 1, false, World.cur().userCfg.krypto());
        String s1 = "MAIN";
        int i = sectfile.sectionIndex(s1);
        sectfile.sectionClear(i);
        sectfile.lineAdd(i, "qmbTotalScore", "" + qmbTotalScore);
        sectfile.lineAdd(i, "qmbTotalAirKill", "" + qmbTotalAirKill);
        sectfile.lineAdd(i, "qmbTotalGroundKill", "" + qmbTotalGroundKill);
        sectfile.lineAdd(i, "qmbTotalBulletsFired", "" + qmbTotalBulletsFired);
        sectfile.lineAdd(i, "qmbTotalBulletsHitAir", "" + qmbTotalBulletsHitAir);
        sectfile.lineAdd(i, "qmbTotalBulletsFiredHitGround", "" + qmbTotalBulletsFiredHitGround);
        sectfile.lineAdd(i, "qmbTotalPctAir", "" + qmbTotalPctAir);
        sectfile.lineAdd(i, "qmbTotalPctGround", "" + qmbTotalPctGround);
        sectfile.lineAdd(i, "qmbTotalTankKill", "" + qmbTotalTankKill);
        sectfile.lineAdd(i, "qmbTotalCarKill", "" + qmbTotalCarKill);
        sectfile.lineAdd(i, "qmbTotalArtilleryKill", "" + qmbTotalArtilleryKill);
        sectfile.lineAdd(i, "qmbTotalAAAKill", "" + qmbTotalAAAKill);
        sectfile.lineAdd(i, "qmbTotalTrainKill", "" + qmbTotalTrainKill);
        sectfile.lineAdd(i, "qmbTotalShipKill", "" + qmbTotalShipKill);
        sectfile.lineAdd(i, "qmbTotalAirStaticKill", "" + qmbTotalAirStaticKill);
        sectfile.lineAdd(i, "qmbTotalBridgeKill", "" + qmbTotalBridgeKill);
        sectfile.lineAdd(i, "qmbTotalPara", "" + qmbTotalPara);
        sectfile.lineAdd(i, "qmbTotalDead", "" + qmbTotalDead);
        sectfile.lineAdd(i, "qmbTotalBombsFired", "" + qmbTotalBombsFired);
        sectfile.lineAdd(i, "qmbTotalBombsHit", "" + qmbTotalBombsHit);
        sectfile.lineAdd(i, "qmbTotalRocketsFired", "" + qmbTotalRocketsFired);
        sectfile.lineAdd(i, "qmbTotalRocketsHit", "" + qmbTotalRocketsHit);
        sectfile.lineAdd(i, "qmbTotalPctBomb", "" + qmbTotalPctBomb);
        sectfile.lineAdd(i, "qmbTotalPctRocket", "" + qmbTotalPctRocket);
        sectfile.saveFile();
    }

    public static void loadQMBStat()
    {
        String s = "users/" + World.cur().userCfg.sId + "/QMB.ini";
        if(exestFile(s))
        {
            SectFile sectfile = new SectFile(s, 1, false, World.cur().userCfg.krypto());
            qmbTotalScore = sectfile.get("MAIN", "qmbTotalScore", 0);
            qmbTotalAirKill = sectfile.get("MAIN", "qmbTotalAirKill", 0.0F);
            qmbTotalGroundKill = sectfile.get("MAIN", "qmbTotalGroundKill", 0);
            qmbTotalBulletsFired = sectfile.get("MAIN", "qmbTotalBulletsFired", 0);
            qmbTotalBulletsHitAir = sectfile.get("MAIN", "qmbTotalBulletsHitAir", 0);
            qmbTotalBulletsFiredHitGround = sectfile.get("MAIN", "qmbTotalBulletsFiredHitGround", 0);
            qmbTotalPctAir = sectfile.get("MAIN", "qmbTotalPctAir", 0.0F);
            qmbTotalPctGround = sectfile.get("MAIN", "qmbTotalPctGround", 0.0F);
            qmbTotalTankKill = sectfile.get("MAIN", "qmbTotalTankKill", 0);
            qmbTotalCarKill = sectfile.get("MAIN", "qmbTotalCarKill", 0);
            qmbTotalArtilleryKill = sectfile.get("MAIN", "qmbTotalArtilleryKill", 0);
            qmbTotalAAAKill = sectfile.get("MAIN", "qmbTotalAAAKill", 0);
            qmbTotalTrainKill = sectfile.get("MAIN", "qmbTotalTrainKill", 0);
            qmbTotalShipKill = sectfile.get("MAIN", "qmbTotalShipKill", 0);
            qmbTotalAirStaticKill = sectfile.get("MAIN", "qmbTotalAirStaticKill", 0);
            qmbTotalBridgeKill = sectfile.get("MAIN", "qmbTotalBridgeKill", 0);
            qmbTotalPara = sectfile.get("MAIN", "qmbTotalPara", 0);
            qmbTotalDead = sectfile.get("MAIN", "qmbTotalDead", 0);
            qmbTotalBombsFired = sectfile.get("MAIN", "qmbTotalBombsFired", 0);
            qmbTotalBombsHit = sectfile.get("MAIN", "qmbTotalBombsHit", 0);
            qmbTotalRocketsFired = sectfile.get("MAIN", "qmbTotalRocketsFired", 0);
            qmbTotalRocketsHit = sectfile.get("MAIN", "qmbTotalRocketsHit", 0);
            qmbTotalPctBomb = sectfile.get("MAIN", "qmbTotalPctBomb", 0.0F);
            qmbTotalPctRocket = sectfile.get("MAIN", "qmbTotalPctRocket", 0.0F);
        }
    }

    private static boolean exestFile(String s)
    {
        try
        {
            SFSInputStream sfsinputstream = new SFSInputStream(s);
            sfsinputstream.close();
        }
        catch(Exception exception)
        {
            return false;
        }
        return true;
    }

    public static void qmbMissionComplete(int i, int j, int k, int l, float f, int i1, int ai[], boolean flag, 
            boolean flag1, int j1, int k1, int l1, int i2)
    {
        loadQMBStat();
        qmbBulletsFired = i;
        qmbBulletsHitAir = j;
        qmbBulletsHit = k;
        qmbBulletsFiredHitGround = qmbBulletsHit - qmbBulletsHitAir;
        qmbMissionBombsFired = j1;
        qmbMissionBombsHit = i2;
        qmbMissionRocketsFired = k1;
        qmbMissionRocketsHit = l1;
        qmbSessionBombsFired += j1;
        qmbSessionBombsHit += i2;
        qmbSessionRocketsFired += k1;
        qmbSessionRocketsHit += l1;
        qmbTotalBombsFired += j1;
        qmbTotalBombsHit += i2;
        qmbTotalRocketsFired += k1;
        qmbTotalRocketsHit += l1;
        qmbSessionBulletsFired += i;
        qmbSessionBulletsHitAir += j;
        qmbSessionBulletsHit += k;
        qmbSessionBulletsFiredHitGround = qmbSessionBulletsHit - qmbSessionBulletsHitAir;
        qmbTotalBulletsFired += i;
        qmbTotalBulletsHitAir += j;
        qmbTotalBulletsHit += k;
        qmbTotalBulletsFiredHitGround += qmbBulletsFiredHitGround;
        if(qmbBulletsFired > 0)
        {
            qmbPercentageAir = ((float)qmbBulletsHitAir * 100F) / (float)qmbBulletsFired;
            qmbPercentageGround = ((float)qmbBulletsFiredHitGround * 100F) / (float)qmbBulletsFired;
            qmbPercentageAir = (float)(int)(qmbPercentageAir * 100F) / 100F;
            qmbPercentageGround = (float)(int)(qmbPercentageGround * 100F) / 100F;
        } else
        {
            qmbPercentageAir = qmbPercentageGround = 0.0F;
        }
        if(qmbSessionBulletsFired > 0)
        {
            qmbSessionPctAir = ((float)qmbSessionBulletsHitAir * 100F) / (float)qmbSessionBulletsFired;
            qmbSessionPctGround = ((float)qmbSessionBulletsFiredHitGround * 100F) / (float)qmbSessionBulletsFired;
            qmbSessionPctAir = (float)(int)(qmbSessionPctAir * 100F) / 100F;
            qmbSessionPctGround = (float)(int)(qmbSessionPctGround * 100F) / 100F;
        } else
        {
            qmbSessionPctAir = qmbSessionPctGround = 0.0F;
        }
        if(qmbTotalBulletsFired > 0)
        {
            qmbTotalPctAir = ((float)qmbTotalBulletsHitAir * 100F) / (float)qmbTotalBulletsFired;
            qmbTotalPctGround = ((float)qmbTotalBulletsFiredHitGround * 100F) / (float)qmbTotalBulletsFired;
            qmbTotalPctAir = (float)(int)(qmbTotalPctAir * 100F) / 100F;
            qmbTotalPctGround = (float)(int)(qmbTotalPctGround * 100F) / 100F;
        } else
        {
            qmbTotalPctAir = qmbTotalPctGround = 0.0F;
        }
        if(qmbMissionBombsFired > 0)
        {
            qmbMissionPctBomb = ((float)qmbMissionBombsHit * 100F) / (float)qmbMissionBombsFired;
            qmbMissionPctBomb = (float)(int)(qmbMissionPctBomb * 100F) / 100F;
        } else
        {
            qmbMissionPctBomb = 0.0F;
        }
        if((float)qmbSessionBombsFired > 0.0F)
        {
            qmbSessionPctBomb = ((float)qmbSessionBombsHit * 100F) / (float)qmbSessionBombsFired;
            qmbSessionPctBomb = (float)(int)(qmbSessionPctBomb * 100F) / 100F;
        } else
        {
            qmbSessionPctBomb = 0.0F;
        }
        if(qmbTotalBombsFired > 0)
        {
            qmbTotalPctBomb = ((float)qmbTotalBombsHit * 100F) / (float)qmbTotalBombsFired;
            qmbTotalPctBomb = (float)(int)(qmbTotalPctBomb * 100F) / 100F;
        } else
        {
            qmbTotalPctBomb = 0.0F;
        }
        if(qmbMissionRocketsFired > 0)
        {
            qmbMissionPctRocket = ((float)qmbMissionRocketsHit * 100F) / (float)qmbMissionRocketsFired;
            qmbMissionPctRocket = (float)(int)(qmbMissionPctRocket * 100F) / 100F;
        } else
        {
            qmbMissionPctRocket = 0.0F;
        }
        if(qmbSessionRocketsFired > 0)
        {
            qmbSessionPctRocket = ((float)qmbSessionRocketsHit * 100F) / (float)qmbSessionRocketsFired;
            qmbSessionPctRocket = (float)(int)(qmbSessionPctRocket * 100F) / 100F;
        } else
        {
            qmbSessionPctRocket = 0.0F;
        }
        if(qmbTotalRocketsFired > 0)
        {
            qmbTotalPctRocket = ((float)qmbTotalRocketsHit * 100F) / (float)qmbTotalRocketsFired;
            qmbTotalPctRocket = (float)(int)(qmbTotalPctRocket * 100F) / 100F;
        } else
        {
            qmbTotalPctRocket = 0.0F;
        }
        qmbMissionAirKill = f;
        qmbSessionAirKill += qmbMissionAirKill;
        qmbTotalAirKill += qmbMissionAirKill;
        qmbMissionGroundKill = i1;
        qmbSessionGroundKill += qmbMissionGroundKill;
        qmbTotalGroundKill += qmbMissionGroundKill;
        if(ai != null)
        {
            for(int j2 = 0; j2 < ai.length; j2++)
                switch(ai[j2])
                {
                case 1: // '\001'
                    qmbMissionTankKill++;
                    qmbSessionTankKill++;
                    qmbTotalTankKill++;
                    break;

                case 2: // '\002'
                    qmbMissionCarKill++;
                    qmbSessionCarKill++;
                    qmbTotalCarKill++;
                    break;

                case 3: // '\003'
                    qmbMissionArtilleryKill++;
                    qmbSessionArtilleryKill++;
                    qmbTotalArtilleryKill++;
                    break;

                case 4: // '\004'
                    qmbMissionAAAKill++;
                    qmbSessionAAAKill++;
                    qmbTotalAAAKill++;
                    break;

                case 6: // '\006'
                    qmbMissionTrainKill++;
                    qmbSessionTrainKill++;
                    qmbTotalTrainKill++;
                    break;

                case 7: // '\007'
                    qmbMissionShipKill++;
                    qmbSessionShipKill++;
                    qmbTotalShipKill++;
                    break;

                case 5: // '\005'
                    qmbMissionBridgeKill++;
                    qmbSessionBridgeKill++;
                    qmbTotalBridgeKill++;
                    break;

                case 8: // '\b'
                    qmbMissionAirStaticKill++;
                    qmbSessionAirStaticKill++;
                    qmbTotalAirStaticKill++;
                    break;
                }

        }
        if(flag1)
        {
            qmbPara++;
            qmbSessionPara++;
            qmbTotalPara++;
        }
        if(flag)
        {
            qmbDead++;
            qmbSessionDead++;
            qmbTotalDead++;
        }
        qmbScore = l;
        qmbSessionScore += qmbScore;
        qmbTotalScore += qmbScore;
        saveStat();
    }

    public static void resetMissionStat()
    {
        qmbBulletsFired = 0;
        qmbBulletsHitAir = 0;
        qmbPercentageAir = 0.0F;
        qmbBulletsFiredHitGround = 0;
        qmbPercentageGround = 0.0F;
        qmbMissionBombsFired = 0;
        qmbMissionBombsHit = 0;
        qmbMissionPctBomb = 0.0F;
        qmbMissionRocketsFired = 0;
        qmbMissionRocketsHit = 0;
        qmbMissionPctRocket = 0.0F;
        qmbMissionAirKill = 0.0F;
        qmbMissionGroundKill = 0;
        qmbMissionTankKill = 0;
        qmbMissionCarKill = 0;
        qmbMissionArtilleryKill = 0;
        qmbMissionAAAKill = 0;
        qmbMissionTrainKill = 0;
        qmbMissionAirStaticKill = 0;
        qmbMissionShipKill = 0;
        qmbMissionBridgeKill = 0;
        qmbDead = 0;
        qmbPara = 0;
        qmbScore = 0;
    }

    public static void resetSessionStat()
    {
        qmbSessionBulletsHit = 0;
        qmbSessionBulletsFired = 0;
        qmbSessionBulletsHitAir = 0;
        qmbSessionPctAir = 0.0F;
        qmbSessionBulletsFiredHitGround = 0;
        qmbSessionPctGround = 0.0F;
        qmbSessionBombsFired = 0;
        qmbSessionBombsHit = 0;
        qmbSessionPctBomb = 0.0F;
        qmbSessionRocketsFired = 0;
        qmbSessionRocketsHit = 0;
        qmbSessionPctRocket = 0.0F;
        qmbSessionAirKill = 0.0F;
        qmbSessionGroundKill = 0;
        qmbSessionTankKill = 0;
        qmbSessionCarKill = 0;
        qmbSessionArtilleryKill = 0;
        qmbSessionAAAKill = 0;
        qmbSessionTrainKill = 0;
        qmbSessionAirStaticKill = 0;
        qmbSessionShipKill = 0;
        qmbSessionBridgeKill = 0;
        qmbSessionPara = 0;
        qmbSessionDead = 0;
        qmbSessionScore = 0;
    }

    public static void resetQMBStat()
    {
        qmbBulletsHit = 0;
        qmbTotalBulletsFired = 0;
        qmbTotalBulletsHitAir = 0;
        qmbTotalPctAir = 0.0F;
        qmbTotalBulletsFiredHitGround = 0;
        qmbTotalPctGround = 0.0F;
        qmbTotalBombsFired = 0;
        qmbTotalBombsHit = 0;
        qmbTotalPctBomb = 0.0F;
        qmbTotalRocketsFired = 0;
        qmbTotalRocketsHit = 0;
        qmbTotalPctRocket = 0.0F;
        qmbTotalAirKill = 0.0F;
        qmbTotalGroundKill = 0;
        qmbTotalTankKill = 0;
        qmbTotalCarKill = 0;
        qmbTotalArtilleryKill = 0;
        qmbTotalAAAKill = 0;
        qmbTotalTrainKill = 0;
        qmbTotalAirStaticKill = 0;
        qmbTotalShipKill = 0;
        qmbTotalBridgeKill = 0;
        qmbTotalPara = 0;
        qmbTotalDead = 0;
        qmbTotalScore = 0;
        resetMissionStat();
        resetSessionStat();
        saveStat();
    }

    public GUIQuickStats(GWindowRoot gwindowroot)
    {
        super(71);
        resetSessionStat();
        resetMissionStat();
        client = (GUIClient)gwindowroot.create(new GUIClient());
        dialogClient = (DialogClient)client.create(new DialogClient());
        infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
        infoMenu.info = i18n("qmb.stat");
        infoName = (GUIInfoName)client.create(new GUIInfoName());
        com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
        bPrev = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
        bRes = (GUIButton)dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192F, 48F, 48F));
        wScrollStats = new Table(dialogClient);
        dialogClient.activateWindow();
        client.hideWindow();
    }

    public GUIClient client;
    public DialogClient dialogClient;
    public GUIInfoMenu infoMenu;
    public GUIInfoName infoName;
    public GUIButton bPrev;
    public GUIButton bRes;
    static int qmbBulletsFired;
    static int qmbBulletsHitAir;
    static int qmbBulletsHit;
    static int qmbBulletsFiredHitGround;
    static int qmbMissionBombsFired;
    static int qmbMissionBombsHit;
    static int qmbMissionRocketsFired;
    static int qmbMissionRocketsHit;
    static int qmbSessionBombsFired;
    static int qmbSessionBombsHit;
    static int qmbSessionRocketsFired;
    static int qmbSessionRocketsHit;
    static int qmbTotalBombsFired;
    static int qmbTotalBombsHit;
    static int qmbTotalRocketsFired;
    static int qmbTotalRocketsHit;
    static int qmbSessionBulletsFired;
    static int qmbSessionBulletsHitAir;
    static int qmbSessionBulletsHit;
    static int qmbSessionBulletsFiredHitGround;
    static int qmbTotalBulletsFired;
    static int qmbTotalBulletsHitAir;
    static int qmbTotalBulletsHit;
    static int qmbTotalBulletsFiredHitGround;
    static float qmbPercentageAir;
    static float qmbSessionPctAir;
    static float qmbTotalPctAir;
    static float qmbMissionPctBomb;
    static float qmbSessionPctBomb;
    static float qmbTotalPctBomb;
    static float qmbMissionPctRocket;
    static float qmbSessionPctRocket;
    static float qmbTotalPctRocket;
    static float qmbPercentageGround;
    static float qmbSessionPctGround;
    static float qmbTotalPctGround;
    static int qmbDead;
    static int qmbSessionDead;
    static int qmbTotalDead;
    static int qmbPara;
    static int qmbSessionPara;
    static int qmbTotalPara;
    static int qmbScore;
    static int qmbSessionScore;
    static int qmbTotalScore;
    static float qmbMissionAirKill;
    static float qmbSessionAirKill;
    static float qmbTotalAirKill;
    static int qmbMissionGroundKill;
    static int qmbSessionGroundKill;
    static int qmbTotalGroundKill;
    static int qmbMissionTankKill;
    static int qmbSessionTankKill;
    static int qmbTotalTankKill;
    static int qmbMissionCarKill;
    static int qmbSessionCarKill;
    static int qmbTotalCarKill;
    static int qmbMissionArtilleryKill;
    static int qmbSessionArtilleryKill;
    static int qmbTotalArtilleryKill;
    static int qmbMissionAAAKill;
    static int qmbSessionAAAKill;
    static int qmbTotalAAAKill;
    static int qmbMissionTrainKill;
    static int qmbSessionTrainKill;
    static int qmbTotalTrainKill;
    static int qmbMissionShipKill;
    static int qmbSessionShipKill;
    static int qmbTotalShipKill;
    static int qmbMissionBridgeKill;
    static int qmbSessionBridgeKill;
    static int qmbTotalBridgeKill;
    static int qmbMissionAirStaticKill;
    static int qmbSessionAirStaticKill;
    static int qmbTotalAirStaticKill;
    public Table wScrollStats;
    private static final int nOfRows = 24;

}
