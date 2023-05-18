package com.maddox.il2.builder;

import com.maddox.TexImage.TexImage;
import com.maddox.gwindow.*;
import com.maddox.il2.ai.*;
import com.maddox.il2.engine.*;
import com.maddox.il2.game.*;
import com.maddox.il2.gui.GUI;
import com.maddox.il2.gui.GUIPad;
import com.maddox.il2.objects.Statics;
import com.maddox.il2.objects.effects.SpritesFog;
import com.maddox.rts.*;
import java.io.File;
import java.io.PrintStream;
import java.util.*;

public class PlMission extends Plugin
{
    static class GWindowMenuItemArmy extends GWindowMenuItem
    {

        int army;

        public GWindowMenuItemArmy(GWindowMenu gwindowmenu, String s, String s1, int i)
        {
            super(gwindowmenu, s, s1);
            army = i;
        }
    }

    class DlgFileConfirmSave extends GWindowFileBoxExec
    {

        public boolean isCloseBox()
        {
            return bClose;
        }

        public void exec(GWindowFileBox gwindowfilebox, String s)
        {
            box = gwindowfilebox;
            bClose = true;
            if(s == null || box.files.size() == 0)
            {
                box.endExec();
                return;
            }
            int i = s.lastIndexOf("/");
            if(i >= 0)
                s = s.substring(i + 1);
            for(int j = 0; j < box.files.size(); j++)
            {
                String s1 = ((File)box.files.get(j)).getName();
                if(s.compareToIgnoreCase(s1) == 0)
                {
                    new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 20F, true, I18N.gui("warning.Warning"), I18N.gui("warning.ReplaceFile"), 1, 0.0F) {

                        public void result(int k)
                        {
                            if(k != 3)
                                bClose = false;
                            box.endExec();
                        }

                    }
;
                    return;
                }
            }

            box.endExec();
        }

        GWindowFileBox box;
        boolean bClose;

        DlgFileConfirmSave()
        {
            bClose = true;
        }
    }

    class WMisc extends GWindowFramed
    {

        public void windowShown()
        {
            mMisc.bChecked = true;
            super.windowShown();
        }

        public void windowHidden()
        {
            mMisc.bChecked = false;
            super.windowHidden();
        }

        public void created()
        {
            super.bAlwaysOnTop = true;
            super.created();
            super.title = Plugin.i18n("mds.tabMisc");
            super.clientWindow = create(new GWindowDialogClient());
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)super.clientWindow;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 30F, 1.3F, Plugin.i18n("mds.misc.towerComms"), null));
            gwindowdialogclient.addControl(wZutiRadar_EnableTowerCommunications = new GWindowCheckBox(gwindowdialogclient, 32F, 1.0F, null) {

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiRadar_EnableTowerCommunications = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 30F, 1.3F, Plugin.i18n("mds.misc.disableAI"), null));
            gwindowdialogclient.addControl(wZutiMisc_DisableAIRadioChatter = new GWindowCheckBox(gwindowdialogclient, 32F, 3F, null) {

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiMisc_DisableAIRadioChatter = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 30F, 1.3F, Plugin.i18n("mds.misc.despawn"), null));
            gwindowdialogclient.addControl(wZutiMisc_DespawnAIPlanesAfterLanding = new GWindowCheckBox(gwindowdialogclient, 32F, 5F, null) {

                public void preRender()
                {
                    super.preRender();
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiMisc_DespawnAIPlanesAfterLanding = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 30F, 1.3F, Plugin.i18n("mds.misc.hideAirfields"), null));
            gwindowdialogclient.addControl(wZutiRadar_HideUnpopulatedAirstripsFromMinimap = new GWindowCheckBox(gwindowdialogclient, 32F, 7F, null) {

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiRadar_HideUnpopulatedAirstripsFromMinimap = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 30F, 1.3F, Plugin.i18n("mds.misc.hideHBNumbers"), null));
            gwindowdialogclient.addControl(wZutiMisc_HidePlayersCountOnHomeBase = new GWindowCheckBox(gwindowdialogclient, 32F, 9F, null) {

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiMisc_HidePlayersCountOnHomeBase = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11F, 30F, 1.3F, Plugin.i18n("mds.misc.disableVectoring"), null));
            gwindowdialogclient.addControl(wZutiRadar_DisableVectoring = new GWindowCheckBox(gwindowdialogclient, 32F, 11F, null) {

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiRadar_DisableVectoring = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
        }

        public void update()
        {
            wZutiRadar_HideUnpopulatedAirstripsFromMinimap.setChecked(zutiRadar_HideUnpopulatedAirstripsFromMinimap, false);
            wZutiRadar_DisableVectoring.setChecked(zutiRadar_DisableVectoring, false);
            wZutiRadar_EnableTowerCommunications.setChecked(zutiRadar_EnableTowerCommunications, false);
            wZutiMisc_DisableAIRadioChatter.setChecked(zutiMisc_DisableAIRadioChatter, false);
            wZutiMisc_DespawnAIPlanesAfterLanding.setChecked(zutiMisc_DespawnAIPlanesAfterLanding, false);
            wZutiMisc_HidePlayersCountOnHomeBase.setChecked(zutiMisc_HidePlayersCountOnHomeBase, false);
        }

        public void afterCreated()
        {
            super.afterCreated();
            resized();
            close(false);
        }

        GWindowLabel lSeparate_Misc;
        GWindowBoxSeparate bSeparate_Misc;
        GWindowCheckBox wZutiRadar_EnableTowerCommunications;
        GWindowCheckBox wZutiMisc_DisableAIRadioChatter;
        GWindowCheckBox wZutiMisc_DespawnAIPlanesAfterLanding;
        GWindowCheckBox wZutiMisc_HidePlayersCountOnHomeBase;
        GWindowCheckBox wZutiRadar_HideUnpopulatedAirstripsFromMinimap;
        GWindowCheckBox wZutiRadar_DisableVectoring;


        public WMisc()
        {
            doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 36F, 15F, true);
            super.bSizable = true;
        }
    }

    class WRespawnTime extends GWindowFramed
    {

        public void windowShown()
        {
            mRespawnTime.bChecked = true;
            super.windowShown();
        }

        public void windowHidden()
        {
            mRespawnTime.bChecked = false;
            super.windowHidden();
        }

        public void created()
        {
            super.bAlwaysOnTop = true;
            super.created();
            super.title = Plugin.i18n("mds.tabRespawn");
            super.clientWindow = create(new GWindowDialogClient());
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)super.clientWindow;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 18F, 1.3F, Plugin.i18n("mds.respawn.bigship"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24F, 1.0F, 18F, 1.3F, Plugin.i18n("mds.respawn.seconds"), null));
            gwindowdialogclient.addControl(wRespawnTime_Bigship = new GWindowEditControl(gwindowdialogclient, 18F, 1.0F, 5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 0, 0x124f80));
                        respawnTime_Bigship = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 18F, 1.3F, Plugin.i18n("mds.respawn.ship"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24F, 3F, 18F, 1.3F, Plugin.i18n("mds.respawn.seconds"), null));
            gwindowdialogclient.addControl(wRespawnTime_Ship = new GWindowEditControl(gwindowdialogclient, 18F, 3F, 5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 0, 0x124f80));
                        respawnTime_Ship = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 18F, 1.3F, Plugin.i18n("mds.respawn.aeroanchored"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24F, 5F, 18F, 1.3F, Plugin.i18n("mds.respawn.seconds"), null));
            gwindowdialogclient.addControl(wRespawnTime_Aeroanchored = new GWindowEditControl(gwindowdialogclient, 18F, 5F, 5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 0, 0x124f80));
                        respawnTime_Aeroanchored = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, 18F, 1.3F, Plugin.i18n("mds.respawn.artillery"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24F, 7F, 18F, 1.3F, Plugin.i18n("mds.respawn.seconds"), null));
            gwindowdialogclient.addControl(wRespawnTime_Artillery = new GWindowEditControl(gwindowdialogclient, 18F, 7F, 5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 0, 0x124f80));
                        respawnTime_Artillery = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, 18F, 1.3F, Plugin.i18n("mds.respawn.searchlight"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 24F, 9F, 18F, 1.3F, Plugin.i18n("mds.respawn.seconds"), null));
            gwindowdialogclient.addControl(wRespawnTime_Searchlight = new GWindowEditControl(gwindowdialogclient, 18F, 9F, 5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 0, 0x124f80));
                        respawnTime_Searchlight = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
        }

        public void update()
        {
            wRespawnTime_Bigship.setValue(Integer.toString(respawnTime_Bigship), false);
            wRespawnTime_Ship.setValue(Integer.toString(respawnTime_Ship), false);
            wRespawnTime_Aeroanchored.setValue(Integer.toString(respawnTime_Aeroanchored), false);
            wRespawnTime_Artillery.setValue(Integer.toString(respawnTime_Artillery), false);
            wRespawnTime_Searchlight.setValue(Integer.toString(respawnTime_Searchlight), false);
        }

        public void afterCreated()
        {
            super.afterCreated();
            resized();
            close(false);
        }

        GWindowEditControl wRespawnTime_Bigship;
        GWindowEditControl wRespawnTime_Ship;
        GWindowEditControl wRespawnTime_Aeroanchored;
        GWindowEditControl wRespawnTime_Artillery;
        GWindowEditControl wRespawnTime_Searchlight;


        public WRespawnTime()
        {
            doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 28F, 13F, true);
            super.bSizable = true;
        }
    }

    class WCraters extends GWindowFramed
    {

        public void windowShown()
        {
            mCraters.bChecked = true;
            super.windowShown();
        }

        public void windowHidden()
        {
            mCraters.bChecked = false;
            super.windowHidden();
        }

        public void created()
        {
            super.bAlwaysOnTop = true;
            super.created();
            super.title = Plugin.i18n("mds.tabCraters");
            super.clientWindow = create(new GWindowDialogClient());
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)super.clientWindow;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 28F, 1.3F, Plugin.i18n("mds.craters.cat11"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 35F, 1.0F, 5F, 1.3F, Plugin.i18n("mds.craters.cat12"), null));
            gwindowdialogclient.addControl(wZutiMisc_BombsCat1_CratersVisibilityMultiplier = new GWindowEditControl(gwindowdialogclient, 32F, 1.0F, 3F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = super.bNumericFloat = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Float.parseFloat(getValue()), 1.0F, 99999F));
                        zutiMisc_BombsCat1_CratersVisibilityMultiplier = Float.parseFloat(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 28F, 1.3F, Plugin.i18n("mds.craters.cat21"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 35F, 3F, 5F, 1.3F, Plugin.i18n("mds.craters.cat22"), null));
            gwindowdialogclient.addControl(wZutiMisc_BombsCat2_CratersVisibilityMultiplier = new GWindowEditControl(gwindowdialogclient, 32F, 3F, 3F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = super.bNumericFloat = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Float.parseFloat(getValue()), 1.0F, 99999F));
                        zutiMisc_BombsCat2_CratersVisibilityMultiplier = Float.parseFloat(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, 28F, 1.3F, Plugin.i18n("mds.craters.cat31"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 35F, 5F, 5F, 1.3F, Plugin.i18n("mds.craters.cat32"), null));
            gwindowdialogclient.addControl(wZutiMisc_BombsCat3_CratersVisibilityMultiplier = new GWindowEditControl(gwindowdialogclient, 32F, 5F, 3F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = super.bNumericFloat = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Float.parseFloat(getValue()), 1.0F, 99999F));
                        zutiMisc_BombsCat3_CratersVisibilityMultiplier = Float.parseFloat(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
        }

        public void update()
        {
            wZutiMisc_BombsCat1_CratersVisibilityMultiplier.setValue(Float.toString(zutiMisc_BombsCat1_CratersVisibilityMultiplier), false);
            wZutiMisc_BombsCat2_CratersVisibilityMultiplier.setValue(Float.toString(zutiMisc_BombsCat2_CratersVisibilityMultiplier), false);
            wZutiMisc_BombsCat3_CratersVisibilityMultiplier.setValue(Float.toString(zutiMisc_BombsCat3_CratersVisibilityMultiplier), false);
        }

        public void afterCreated()
        {
            super.afterCreated();
            resized();
            close(false);
        }

        GWindowEditControl wZutiMisc_BombsCat1_CratersVisibilityMultiplier;
        GWindowEditControl wZutiMisc_BombsCat2_CratersVisibilityMultiplier;
        GWindowEditControl wZutiMisc_BombsCat3_CratersVisibilityMultiplier;


        public WCraters()
        {
            doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 40F, 9F, true);
            super.bSizable = true;
        }
    }

    class WFoW extends GWindowFramed
    {

        public void windowShown()
        {
            mFoW.bChecked = true;
            super.windowShown();
        }

        public void windowHidden()
        {
            mFoW.bChecked = false;
            super.windowHidden();
        }

        public void created()
        {
            super.bAlwaysOnTop = true;
            super.created();
            super.title = Plugin.i18n("mds.tabRadar");
            super.clientWindow = create(new GWindowDialogClient());
            GWindowDialogClient gwindowdialogclient = (GWindowDialogClient)super.clientWindow;
            new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 1.0F, 52F, 23F);
            new GWindowHSeparate(gwindowdialogclient, 4F, 3.5F, 48F);
            new GWindowHSeparate(gwindowdialogclient, 4F, 11.5F, 48F);
            new GWindowHSeparate(gwindowdialogclient, 2.0F, 21.5F, 50F);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 4F, 2.0F, 20F, 1.3F, Plugin.i18n("mds.radar.advanced1"), null));
            gwindowdialogclient.addControl(wZutiRadar_IsRadarInAdvancedMode = new GWindowCheckBox(gwindowdialogclient, 2.0F, 2.0F, null) {

                public boolean notify(int i, int j)
                {
                    if(wZutiRadar_ShipsAsRadar.isChecked())
                    {
                        wZutiRadar_ShipRadar_MaxRange.setEnable(isChecked());
                        wZutiRadar_ShipRadar_MinHeight.setEnable(isChecked());
                        wZutiRadar_ShipRadar_MaxHeight.setEnable(isChecked());
                        wZutiRadar_ShipSmallRadar_MaxRange.setEnable(isChecked());
                        wZutiRadar_ShipSmallRadar_MinHeight.setEnable(isChecked());
                        wZutiRadar_ShipSmallRadar_MaxHeight.setEnable(isChecked());
                    }
                    if(wZutiRadar_ScoutsAsRadar.isChecked())
                    {
                        wZutiRadar_ScoutRadar_MaxRange.setEnable(isChecked());
                        wZutiRadar_ScoutRadar_DeltaHeight.setEnable(isChecked());
                        wZutiRadar_ScoutGroundObjects_Alpha.setEnable(isChecked());
                        wZutiRadar_ScoutCompleteRecon.setEnable(isChecked());
                    }
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        Mission.ZUTI_RADAR_IN_ADV_MODE = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 2.0F, 22F, 20F, 1.3F, Plugin.i18n("mds.radar.refresh"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.5F, 22F, 6F, 1.3F, Plugin.i18n("mds.radar.SEC"), null));
            gwindowdialogclient.addControl(wZutiRadar_RefreshInterval = new GWindowEditControl(gwindowdialogclient, 20F, 22F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 0, 0x1869f));
                        zutiRadar_RefreshInterval = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 4F, 4F, 20F, 1.3F, Plugin.i18n("mds.radar.shipsAsRadars"), null));
            gwindowdialogclient.addControl(wZutiRadar_ShipsAsRadar = new GWindowCheckBox(gwindowdialogclient, 2.0F, 4F, null) {

                public boolean notify(int i, int j)
                {
                    if(wZutiRadar_IsRadarInAdvancedMode.isChecked())
                    {
                        wZutiRadar_ShipRadar_MaxRange.setEnable(isChecked());
                        wZutiRadar_ShipRadar_MinHeight.setEnable(isChecked());
                        wZutiRadar_ShipRadar_MaxHeight.setEnable(isChecked());
                        wZutiRadar_ShipSmallRadar_MaxRange.setEnable(isChecked());
                        wZutiRadar_ShipSmallRadar_MinHeight.setEnable(isChecked());
                        wZutiRadar_ShipSmallRadar_MaxHeight.setEnable(isChecked());
                    }
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiRadar_ShipsAsRadar = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 5F, 8F, 16F, 1.3F, Plugin.i18n("mds.radar.BigShips"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 5F, 10F, 16F, 1.3F, Plugin.i18n("mds.radar.SmallShips"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.5F, 8F, 20F, 1.3F, Plugin.i18n("mds.radar.KM"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 35.5F, 8F, 20F, 1.3F, Plugin.i18n("mds.radar.M"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 47.5F, 8F, 20F, 1.3F, Plugin.i18n("mds.radar.M"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 23.5F, 10F, 20F, 1.3F, Plugin.i18n("mds.radar.KM"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 35.5F, 10F, 20F, 1.3F, Plugin.i18n("mds.radar.M"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 47.5F, 10F, 20F, 1.3F, Plugin.i18n("mds.radar.M"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 13.75F, 6F, 16F, 1.3F, Plugin.i18n("mds.radar.bigShipMax"), null, 1));
            gwindowdialogclient.addControl(wZutiRadar_ShipRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 20F, 8F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 1, 0x1869f));
                        zutiRadar_ShipRadar_MaxRange = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 25.75F, 6F, 16F, 1.3F, Plugin.i18n("mds.radar.bigShipMin"), null, 1));
            gwindowdialogclient.addControl(wZutiRadar_ShipRadar_MinHeight = new GWindowEditControl(gwindowdialogclient, 32F, 8F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 0, 0x1869f));
                        zutiRadar_ShipRadar_MinHeight = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 37.75F, 6F, 16F, 1.3F, Plugin.i18n("mds.radar.bigShipMaxH"), null, 1));
            gwindowdialogclient.addControl(wZutiRadar_ShipRadar_MaxHeight = new GWindowEditControl(gwindowdialogclient, 44F, 8F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 1000, 0x1869f));
                        zutiRadar_ShipRadar_MaxHeight = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addControl(wZutiRadar_ShipSmallRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 20F, 10F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 1, 0x1869f));
                        zutiRadar_ShipSmallRadar_MaxRange = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addControl(wZutiRadar_ShipSmallRadar_MinHeight = new GWindowEditControl(gwindowdialogclient, 32F, 10F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 0, 0x1869f));
                        zutiRadar_ShipSmallRadar_MinHeight = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addControl(wZutiRadar_ShipSmallRadar_MaxHeight = new GWindowEditControl(gwindowdialogclient, 44F, 10F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 1000, 0x1869f));
                        zutiRadar_ShipSmallRadar_MaxHeight = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 4F, 12F, 30F, 1.3F, Plugin.i18n("mds.radar.scoutsAsRadars"), null));
            gwindowdialogclient.addControl(wZutiRadar_ScoutsAsRadar = new GWindowCheckBox(gwindowdialogclient, 2.0F, 12F, null) {

                public boolean notify(int i, int j)
                {
                    if(wZutiRadar_IsRadarInAdvancedMode.isChecked())
                    {
                        wZutiRadar_ScoutRadar_MaxRange.setEnable(isChecked());
                        wZutiRadar_ScoutRadar_DeltaHeight.setEnable(isChecked());
                        wZutiRadar_ScoutGroundObjects_Alpha.setEnable(isChecked());
                        wZutiRadar_ScoutCompleteRecon.setEnable(isChecked());
                    }
                    bZutiRadar_ScoutRadarType_Red.setEnable(isChecked());
                    bZutiRadar_ScoutRadarType_Blue.setEnable(isChecked());
                    wZutiRadar_ScoutCompleteRecon.setEnable(isChecked());
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiRadar_ScoutsAsRadar = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 5F, 14F, 30F, 1.3F, Plugin.i18n("mds.radar.scoutAcScanMax"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 29.5F, 14F, 6F, 1.3F, Plugin.i18n("mds.radar.KM"), null));
            gwindowdialogclient.addControl(wZutiRadar_ScoutRadar_MaxRange = new GWindowEditControl(gwindowdialogclient, 26F, 14F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 1, 0x1869f));
                        zutiRadar_ScoutRadar_MaxRange = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 5F, 16F, 30F, 1.3F, Plugin.i18n("mds.radar.scoutAcDelta"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 29.5F, 16F, 6F, 1.3F, Plugin.i18n("mds.radar.M"), null));
            gwindowdialogclient.addControl(wZutiRadar_ScoutRadar_DeltaHeight = new GWindowEditControl(gwindowdialogclient, 26F, 16F, 3.5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        setValue(checkValidRange(Integer.parseInt(getValue()), 100, 0x1869f));
                        zutiRadar_ScoutRadar_DeltaHeight = Integer.parseInt(getValue());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 5F, 18F, 30F, 1.3F, Plugin.i18n("mds.radar.scoutGroundAlpha"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 29.5F, 18F, 6F, 1.3F, Plugin.i18n("mds.radar.DEG"), null));
            gwindowdialogclient.addControl(wZutiRadar_ScoutGroundObjects_Alpha = new GWindowComboControl(gwindowdialogclient, 26F, 18F, 3.5F) {

                public void afterCreated()
                {
                    super.afterCreated();
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiRadar_ScoutGroundObjects_Alpha = getSelected() + 1;
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            wZutiRadar_ScoutGroundObjects_Alpha.setEditable(false);
            wZutiRadar_ScoutGroundObjects_Alpha.add("30");
            wZutiRadar_ScoutGroundObjects_Alpha.add("35");
            wZutiRadar_ScoutGroundObjects_Alpha.add("40");
            wZutiRadar_ScoutGroundObjects_Alpha.add("45");
            wZutiRadar_ScoutGroundObjects_Alpha.add("50");
            wZutiRadar_ScoutGroundObjects_Alpha.add("55");
            wZutiRadar_ScoutGroundObjects_Alpha.add("60");
            wZutiRadar_ScoutGroundObjects_Alpha.add("65");
            wZutiRadar_ScoutGroundObjects_Alpha.add("70");
            wZutiRadar_ScoutGroundObjects_Alpha.add("75");
            wZutiRadar_ScoutGroundObjects_Alpha.add("80");
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 5F, 20F, 30F, 1.3F, Plugin.i18n("mds.radar.scoutCmplRecon"), null));
            gwindowdialogclient.addControl(wZutiRadar_ScoutCompleteRecon = new GWindowCheckBox(gwindowdialogclient, 2.0F, 20F, null) {

                public void afterCreated()
                {
                    super.afterCreated();
                    setEnable(false);
                }

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                    {
                        return false;
                    } else
                    {
                        zutiRadar_ScoutCompleteRecon = isChecked();
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addControl(bZutiRadar_ScoutRadarType_Red = new GWindowButton(gwindowdialogclient, 32.5F, 14F, 20F, 2.0F, Plugin.i18n("mds.radar.scoutRed"), null) {

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                        return false;
                    if(!zuti_manageAircraftsRed.isVisible())
                        zuti_manageAircraftsRed.showWindow();
                    zuti_manageAircraftsRed.setArmy(1);
                    zutiRadar_ScoutRadarType_Red = zuti_manageAircraftsRed.airNames;
                    return true;
                }

            }
);
            gwindowdialogclient.addControl(bZutiRadar_ScoutRadarType_Blue = new GWindowButton(gwindowdialogclient, 32.5F, 17F, 20F, 2.0F, Plugin.i18n("mds.radar.scoutBlue"), null) {

                public boolean notify(int i, int j)
                {
                    if(i != 2)
                        return false;
                    if(!zuti_manageAircraftsBlue.isVisible())
                        zuti_manageAircraftsBlue.showWindow();
                    zuti_manageAircraftsBlue.setArmy(2);
                    zutiRadar_ScoutRadarType_Blue = zuti_manageAircraftsBlue.airNames;
                    return true;
                }

            }
);
        }

        public void update()
        {
            wZutiRadar_IsRadarInAdvancedMode.setChecked(Mission.ZUTI_RADAR_IN_ADV_MODE, false);
            wZutiRadar_RefreshInterval.setValue(Integer.toString(zutiRadar_RefreshInterval), false);
            wZutiRadar_ShipsAsRadar.setChecked(zutiRadar_ShipsAsRadar, false);
            wZutiRadar_ShipRadar_MaxRange.setValue(Integer.toString(zutiRadar_ShipRadar_MaxRange), false);
            wZutiRadar_ShipRadar_MinHeight.setValue(Integer.toString(zutiRadar_ShipRadar_MinHeight), false);
            wZutiRadar_ShipRadar_MaxHeight.setValue(Integer.toString(zutiRadar_ShipRadar_MaxHeight), false);
            wZutiRadar_ShipSmallRadar_MaxRange.setValue(Integer.toString(zutiRadar_ShipSmallRadar_MaxRange), false);
            wZutiRadar_ShipSmallRadar_MinHeight.setValue(Integer.toString(zutiRadar_ShipSmallRadar_MinHeight), false);
            wZutiRadar_ShipSmallRadar_MaxHeight.setValue(Integer.toString(zutiRadar_ShipSmallRadar_MaxHeight), false);
            wZutiRadar_ScoutsAsRadar.setChecked(zutiRadar_ScoutsAsRadar, false);
            wZutiRadar_ScoutRadar_MaxRange.setValue(Integer.toString(zutiRadar_ScoutRadar_MaxRange), false);
            wZutiRadar_ScoutRadar_DeltaHeight.setValue(Integer.toString(zutiRadar_ScoutRadar_DeltaHeight), false);
            wZutiRadar_ScoutGroundObjects_Alpha.setSelected(zutiRadar_ScoutGroundObjects_Alpha - 1, true, false);
            wZutiRadar_ScoutCompleteRecon.setChecked(zutiRadar_ScoutCompleteRecon, false);
        }

        public void afterCreated()
        {
            super.afterCreated();
            resized();
            close(false);
        }

        GWindowCheckBox wZutiRadar_IsRadarInAdvancedMode;
        GWindowCheckBox wZutiRadar_ShipsAsRadar;
        GWindowCheckBox wZutiRadar_ScoutsAsRadar;
        GWindowCheckBox wZutiRadar_ScoutCompleteRecon;
        GWindowEditControl wZutiRadar_ShipRadar_MaxRange;
        GWindowEditControl wZutiRadar_ShipRadar_MinHeight;
        GWindowEditControl wZutiRadar_ShipRadar_MaxHeight;
        GWindowEditControl wZutiRadar_ShipSmallRadar_MaxRange;
        GWindowEditControl wZutiRadar_ShipSmallRadar_MinHeight;
        GWindowEditControl wZutiRadar_ShipSmallRadar_MaxHeight;
        GWindowEditControl wZutiRadar_ScoutRadar_MaxRange;
        GWindowEditControl wZutiRadar_ScoutRadar_DeltaHeight;
        GWindowEditControl wZutiRadar_RefreshInterval;
        GWindowComboControl wZutiRadar_ScoutGroundObjects_Alpha;
        GWindowButton bZutiRadar_ScoutRadarType_Red;
        GWindowButton bZutiRadar_ScoutRadarType_Blue;
        static final float separateWidth = 52F;
        static final float fowStartOf2ndCol = 21F;
        static final float fowTextBoxWidth = 3.5F;
        static final float fowStartOf3rdCol = 26F;
        static final float fowStartOf4thCol = 46.5F;
        static final float fowTitleTextW = 16F;
        static final float fowTextW = 30F;
        static final float fowStartCol1 = 20F;
        static final float fowStartCol2 = 32F;
        static final float fowStartCol3 = 44F;


        public WFoW()
        {
            doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 55F, 27F, true);
            super.bSizable = false;
        }
    }

    class WConditions extends GWindowFramed
    {

        public void windowShown()
        {
            mConditions.bChecked = true;
            super.windowShown();
        }

        public void windowHidden()
        {
            mConditions.bChecked = false;
            super.windowHidden();
        }

        public void created()
        {
            super.bAlwaysOnTop = true;
            super.created();
            super.title = Plugin.i18n("MissionConditions");
            float f = 13F;
            super.clientWindow = create(new GWindowTabDialogClient());
            GWindowTabDialogClient gwindowtabdialogclient = (GWindowTabDialogClient)super.clientWindow;
            GWindowDialogClient gwindowdialogclient = null;
            gwindowtabdialogclient.addTab(Plugin.i18n("weather"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, f - 1.0F, 1.3F, Plugin.i18n("Weather"), null));
            gwindowdialogclient.addControl(wCloudType = new GWindowComboControl(gwindowdialogclient, f, 1.0F, 8F) {

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getCloudType();
                    return super.notify(l, i1);
                }

            }
);
            wCloudType.setEditable(false);
            wCloudType.add(Plugin.i18n("Clear"));
            wCloudType.add(Plugin.i18n("Good"));
            wCloudType.add(Plugin.i18n("Hazy"));
            wCloudType.add(Plugin.i18n("Poor"));
            wCloudType.add(Plugin.i18n("Blind"));
            wCloudType.add(Plugin.i18n("Rain/Snow"));
            wCloudType.add(Plugin.i18n("Thunder"));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, f - 1.0F, 1.3F, Plugin.i18n("CloudHeight"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 3F, 2.0F, 1.3F, Plugin.i18n("[m]"), null));
            gwindowdialogclient.addControl(wCloudHeight = new GWindowEditControl(gwindowdialogclient, f, 3F, 5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getCloudHeight();
                    return super.notify(l, i1);
                }

            }
);
            wCloudHeight.setEditable(true);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 13F, 11F, 1.6F, Plugin.i18n("WindTable"), null));
            boxWindTable = new GWindowBoxSeparate(gwindowdialogclient, 1.0F, 15F, 20F, 25F);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 3F, 16F, 9F, 1.3F, Plugin.i18n("Altitude[m]"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 10F, 16F, 9F, 1.3F, Plugin.i18n("WindSpeed[m/s]"), null));
            gwindowdialogclient.addLabel(wLabel0 = new GWindowLabel(gwindowdialogclient, 1.0F, 18F, 5F, 1.3F, "10", null));
            wLabel0.align = 2;
            gwindowdialogclient.addLabel(wLabel1 = new GWindowLabel(gwindowdialogclient, 1.0F, 20F, 5F, 1.3F, "1000", null));
            wLabel1.align = 2;
            gwindowdialogclient.addLabel(wLabel2 = new GWindowLabel(gwindowdialogclient, 1.0F, 22F, 5F, 1.3F, "2000", null));
            wLabel2.align = 2;
            gwindowdialogclient.addLabel(wLabel3 = new GWindowLabel(gwindowdialogclient, 1.0F, 24F, 5F, 1.3F, "3000", null));
            wLabel3.align = 2;
            gwindowdialogclient.addLabel(wLabel4 = new GWindowLabel(gwindowdialogclient, 1.0F, 26F, 5F, 1.3F, "4000", null));
            wLabel4.align = 2;
            gwindowdialogclient.addLabel(wLabel5 = new GWindowLabel(gwindowdialogclient, 1.0F, 28F, 5F, 1.3F, "5000", null));
            wLabel5.align = 2;
            gwindowdialogclient.addLabel(wLabel6 = new GWindowLabel(gwindowdialogclient, 1.0F, 30F, 5F, 1.3F, "6000", null));
            wLabel6.align = 2;
            gwindowdialogclient.addLabel(wLabel7 = new GWindowLabel(gwindowdialogclient, 1.0F, 32F, 5F, 1.3F, "7000", null));
            wLabel7.align = 2;
            gwindowdialogclient.addLabel(wLabel8 = new GWindowLabel(gwindowdialogclient, 1.0F, 34F, 5F, 1.3F, "8000", null));
            wLabel8.align = 2;
            gwindowdialogclient.addLabel(wLabel9 = new GWindowLabel(gwindowdialogclient, 1.0F, 36F, 5F, 1.3F, "9000", null));
            wLabel9.align = 2;
            gwindowdialogclient.addLabel(wLabel10 = new GWindowLabel(gwindowdialogclient, 1.0F, 38F, 5F, 1.3F, "10000", null));
            wLabel10.align = 2;
            gwindowdialogclient.addLabel(wLabel00 = new GWindowLabel(gwindowdialogclient, 10F, 18F, 5F, 1.3F, "", null));
            wLabel00.align = 2;
            gwindowdialogclient.addLabel(wLabel11 = new GWindowLabel(gwindowdialogclient, 10F, 20F, 5F, 1.3F, "", null));
            wLabel11.align = 2;
            gwindowdialogclient.addLabel(wLabel22 = new GWindowLabel(gwindowdialogclient, 10F, 22F, 5F, 1.3F, "", null));
            wLabel22.align = 2;
            gwindowdialogclient.addLabel(wLabel33 = new GWindowLabel(gwindowdialogclient, 10F, 24F, 5F, 1.3F, "", null));
            wLabel33.align = 2;
            gwindowdialogclient.addLabel(wLabel44 = new GWindowLabel(gwindowdialogclient, 10F, 26F, 5F, 1.3F, "", null));
            wLabel44.align = 2;
            gwindowdialogclient.addLabel(wLabel55 = new GWindowLabel(gwindowdialogclient, 10F, 28F, 5F, 1.3F, "", null));
            wLabel55.align = 2;
            gwindowdialogclient.addLabel(wLabel66 = new GWindowLabel(gwindowdialogclient, 10F, 30F, 5F, 1.3F, "", null));
            wLabel66.align = 2;
            gwindowdialogclient.addLabel(wLabel77 = new GWindowLabel(gwindowdialogclient, 10F, 32F, 5F, 1.3F, "", null));
            wLabel77.align = 2;
            gwindowdialogclient.addLabel(wLabel88 = new GWindowLabel(gwindowdialogclient, 10F, 34F, 5F, 1.3F, "", null));
            wLabel88.align = 2;
            gwindowdialogclient.addLabel(wLabel99 = new GWindowLabel(gwindowdialogclient, 10F, 36F, 5F, 1.3F, "", null));
            wLabel99.align = 2;
            gwindowdialogclient.addLabel(wLabel1010 = new GWindowLabel(gwindowdialogclient, 10F, 38F, 5F, 1.3F, "", null));
            wLabel1010.align = 2;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, f - 1.0F, 1.3F, Plugin.i18n("WindDirection"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 5F, 7F, 1.3F, Plugin.i18n("[deg]"), null));
            gwindowdialogclient.addControl(wWindDirection = new GWindowEditControl(gwindowdialogclient, f, 5F, 5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getWindDirection();
                    return super.notify(l, i1);
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, f - 1.0F, 1.3F, Plugin.i18n("WindVelocity"), null));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 5.5F, 7F, 7F, 1.3F, Plugin.i18n("[m/s]"), null));
            gwindowdialogclient.addControl(wWindVelocity = new GWindowEditControl(gwindowdialogclient, f, 7F, 5F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getWindVelocity();
                    return super.notify(l, i1);
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 9F, f - 1.0F, 1.3F, Plugin.i18n("Gust"), null));
            gwindowdialogclient.addControl(wGust = new GWindowComboControl(gwindowdialogclient, f, 9F, 8F) {

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getGust();
                    return super.notify(l, i1);
                }

            }
);
            wGust.setEditable(false);
            wGust.add(Plugin.i18n("None"));
            wGust.add(Plugin.i18n("Low"));
            wGust.add(Plugin.i18n("Moderate"));
            wGust.add(Plugin.i18n("Strong"));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 11F, f - 1.0F, 1.3F, Plugin.i18n("Turbulence"), null));
            gwindowdialogclient.addControl(wTurbulence = new GWindowComboControl(gwindowdialogclient, f, 11F, 8F) {

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getTurbulence();
                    return super.notify(l, i1);
                }

            }
);
            wTurbulence.setEditable(false);
            wTurbulence.add(Plugin.i18n("None"));
            wTurbulence.add(Plugin.i18n("Low"));
            wTurbulence.add(Plugin.i18n("Moderate"));
            wTurbulence.add(Plugin.i18n("Strong"));
            wTurbulence.add(Plugin.i18n("VeryStrong"));
            gwindowtabdialogclient.addTab(Plugin.i18n("Season"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            f = 10F;
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, f - 1.0F, 1.3F, Plugin.i18n("Time"), null));
            gwindowdialogclient.addControl(wTimeH = new GWindowEditControl(gwindowdialogclient, f, 1.0F, 2.0F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int l, int i1)
                {
                    if(l != 2)
                    {
                        return false;
                    } else
                    {
                        getTime();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, f + 2.15F, 1.0F, 1.0F, 1.3F, ":", null));
            gwindowdialogclient.addControl(wTimeM = new GWindowEditControl(gwindowdialogclient, f + 2.5F, 1.0F, 2.0F, 1.3F, "") {

                public void afterCreated()
                {
                    super.afterCreated();
                    super.bNumericOnly = true;
                    super.bDelayedNotify = true;
                }

                public boolean notify(int l, int i1)
                {
                    if(l != 2)
                    {
                        return false;
                    } else
                    {
                        getTime();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, f - 1.0F, 1.3F, Plugin.i18n("Day"), null));
            gwindowdialogclient.addControl(wDay = new GWindowComboControl(gwindowdialogclient, f, 3F, 5F) {

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getDay();
                    return super.notify(l, i1);
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 5F, f - 1.0F, 1.3F, Plugin.i18n("Month"), null));
            gwindowdialogclient.addControl(wMonth = new GWindowComboControl(gwindowdialogclient, f, 5F, 5F) {

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getMonth();
                    return super.notify(l, i1);
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 7F, f - 1.0F, 1.3F, Plugin.i18n("Year"), null));
            gwindowdialogclient.addControl(wYear = new GWindowComboControl(gwindowdialogclient, f, 7F, 5F) {

                public boolean notify(int l, int i1)
                {
                    if(l == 2)
                        getYear();
                    return super.notify(l, i1);
                }

            }
);

//            wDay.setEditable(false);
//            wDay.hideList();
//            wDay.list = new ArrayList(Arrays.asList(_dayKey));
//            wMonth.setEditable(false);
//            wMonth.hideList();
//            wMonth.list = new ArrayList(Arrays.asList(_monthKey));
//            wYear.setEditable(false);
//            wYear.hideList();
//            wYear.list = new ArrayList(Arrays.asList(_yearKey));

            wDay.setEditable(false);
            for(int _dayKey = 0; _dayKey < 31; _dayKey++)
                wDay.add(Integer.toString(_dayKey + 1));

            wMonth.setEditable(false);
            for(int _monthKey = 0; _monthKey < 12; _monthKey++)
                wMonth.add(Integer.toString(_monthKey + 1));

            wYear.setEditable(false);
            for(int _yearKey = 1900; _yearKey <= Calendar.getInstance().get(Calendar.YEAR); _yearKey++)
                wYear.add(Integer.toString(_yearKey));

            gwindowtabdialogclient.addTab(Plugin.i18n("misc"), gwindowtabdialogclient.create(gwindowdialogclient = new GWindowDialogClient()));
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 1.0F, 12F, 1.3F, Plugin.i18n("timeLocked"), null));
            gwindowdialogclient.addControl(wTimeFix = new GWindowCheckBox(gwindowdialogclient, 14F, 1.0F, null) {

                public void preRender()
                {
                    super.preRender();
                    setChecked(World.cur().isTimeOfDayConstant(), false);
                }

                public boolean notify(int l, int i1)
                {
                    if(l != 2)
                    {
                        return false;
                    } else
                    {
                        World.cur().setTimeOfDayConstant(isChecked());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
            gwindowdialogclient.addLabel(new GWindowLabel(gwindowdialogclient, 1.0F, 3F, 12F, 1.3F, Plugin.i18n("weaponsLocked"), null));
            gwindowdialogclient.addControl(wWeaponFix = new GWindowCheckBox(gwindowdialogclient, 14F, 3F, null) {

                public void preRender()
                {
                    super.preRender();
                    setChecked(World.cur().isWeaponsConstant(), false);
                }

                public boolean notify(int l, int i1)
                {
                    if(l != 2)
                    {
                        return false;
                    } else
                    {
                        World.cur().setWeaponsConstant(isChecked());
                        PlMission.setChanged();
                        return false;
                    }
                }

            }
);
        }

        public void update()
        {
            float f = World.getTimeofDay();
            int i = (int)f % 24;
            int j = (int)(60F * (f - (float)(int)f));
            wTimeH.setValue("" + i, false);
            wTimeM.setValue("" + j, false);
            wCloudType.setSelected(cloudType, true, false);
            int k = (int)cloudHeight;
            wCloudHeight.setValue("" + k, false);
            wTimeFix.setChecked(World.cur().isTimeOfDayConstant(), false);
            wWeaponFix.setChecked(World.cur().isWeaponsConstant(), false);
            try
            {
                wDay.setSelected(day - 1, true, false);
                wMonth.setSelected(month - 1, true, false);
                wYear.setSelected(year - 1900, true, false);
            }
            catch(Exception exception)
            {
                System.out.println("Invalid date!");
            }
            wWindDirection.setValue("" + windDirection, false);
            wWindVelocity.setValue("" + windVelocity, false);
            int l = gust;
            int i1 = turbulence;
            if(gust > 0)
                l = (gust - 6) / 2;
            if(l >= 0 && l < wGust.size())
                wGust.setSelected(l, true, false);
            if(turbulence > 0)
                i1 = turbulence - 2;
            if(i1 >= 0 && i1 < wTurbulence.size())
                wTurbulence.setSelected(i1, true, false);
            calcWindTable(cloudType, cloudHeight, windVelocity);
        }

        public void calcWindTable(int i, float f, float f1)
        {
            float f2 = f1;
            if(f2 == 0.0F)
                f2 = 0.25F + (float)(wCloudType.getSelected() * wCloudType.getSelected()) * 0.12F;
            float f3 = f + 300F;
            float f4 = f3 / 2.0F;
            float f5 = (f2 * f4) / 3000F + f2;
            float f6 = (f2 * (f3 - f4)) / 9000F + f5;
            int ai[] = new int[11];
            for(int j = 0; j <= 10; j++)
            {
                int k = j * 1000;
                if((float)k > f3)
                    f2 = f6 + (((float)k - f3) * f2) / 18000F;
                else
                if((float)k > f4)
                    f2 = f5 + (((float)k - f4) * f2) / 9000F;
                else
                if((float)k > 10F)
                    f2 += (f2 * (float)k) / 3000F;
                if((float)k <= 10F);
                ai[j] = (int)f2;
            }

            ((GWindowDialogControl) (wLabel00)).cap.set("" + ai[0]);
            ((GWindowDialogControl) (wLabel11)).cap.set("" + ai[1]);
            ((GWindowDialogControl) (wLabel22)).cap.set("" + ai[2]);
            ((GWindowDialogControl) (wLabel33)).cap.set("" + ai[3]);
            ((GWindowDialogControl) (wLabel44)).cap.set("" + ai[4]);
            ((GWindowDialogControl) (wLabel55)).cap.set("" + ai[5]);
            ((GWindowDialogControl) (wLabel66)).cap.set("" + ai[6]);
            ((GWindowDialogControl) (wLabel77)).cap.set("" + ai[7]);
            ((GWindowDialogControl) (wLabel88)).cap.set("" + ai[8]);
            ((GWindowDialogControl) (wLabel99)).cap.set("" + ai[9]);
            ((GWindowDialogControl) (wLabel1010)).cap.set("" + ai[10]);
        }

        public void getTime()
        {
            String s = wTimeH.getValue();
            double d = 0.0D;
            try
            {
                d = Double.parseDouble(s);
            }
            catch(Exception exception) { }
            if(d < 0.0D)
                d = 0.0D;
            if(d > 23D)
                d = 23D;
            s = wTimeM.getValue();
            double d1 = 0.0D;
            try
            {
                d1 = Double.parseDouble(s);
            }
            catch(Exception exception1) { }
            if(d1 < 0.0D)
                d1 = 0.0D;
            if(d1 >= 60D)
                d1 = 59D;
            float f = (float)(d + d1 / 60D);
            if((int)(f * 60F) != (int)(World.getTimeofDay() * 60F))
            {
                World.setTimeofDay(f);
                if(Plugin.builder.isLoadedLandscape())
                    World.land().cubeFullUpdate();
            }
            PlMission.setChanged();
            update();
        }

        public void getCloudType()
        {
            cloudType = wCloudType.getSelected();
            Mission.setCloudsType(cloudType);
            PlMission.setChanged();
            update();
        }

        public void getCloudHeight()
        {
            try
            {
                cloudHeight = Float.parseFloat(wCloudHeight.getValue());
            }
            catch(Exception exception) { }
            if(cloudHeight < 300F)
                cloudHeight = 300F;
            if(cloudHeight > 5000F)
                cloudHeight = 5000F;
            Mission.setCloudsHeight(cloudHeight);
            PlMission.setChanged();
            update();
        }

        public void getYear()
        {
            year = Integer.parseInt(wYear.getValue());
            if(year != Mission.curYear())
            {
                Mission.setYear(year);
                Plugin.doDateChanged();
                if(Plugin.builder.isLoadedLandscape())
                    World.land().cubeFullUpdate();
            }
            PlMission.setChanged();
            update();
        }

        public void getDay()
        {
            day = Integer.parseInt(wDay.getValue());
            if(day != Mission.curDay())
            {
                Mission.setDay(day);
                World.land().setDay(day);
                Plugin.doDateChanged();
                if(Plugin.builder.isLoadedLandscape())
                    World.land().cubeFullUpdate();
            }
            PlMission.setChanged();
            update();
        }

        public void getMonth()
        {
            month = Integer.parseInt(wMonth.getValue());
            if(month != Mission.curMonth())
            {
                Mission.setMonth(month);
                World.land().setMonth(month);
                Plugin.doDateChanged();
                if(Plugin.builder.isLoadedLandscape())
                    World.land().cubeFullUpdate();
            }
            PlMission.setChanged();
            update();
        }

        public void getWindDirection()
        {
            try
            {
                windDirection = Float.parseFloat(wWindDirection.getValue());
            }
            catch(Exception exception) { }
            if(windDirection < 0.0F)
                windDirection = 0.0F;
            if(windDirection >= 360F)
                windDirection = 0.0F;
            Mission.setWindDirection(windDirection);
            PlMission.setChanged();
            update();
        }

        public void getWindVelocity()
        {
            try
            {
                windVelocity = Float.parseFloat(wWindVelocity.getValue());
            }
            catch(Exception exception) { }
            if(windVelocity > 15F)
                windVelocity = 15F;
            if(windVelocity < 0.0F)
                windVelocity = 0.0F;
            Mission.setWindVelocity(windVelocity);
            PlMission.setChanged();
            update();
        }

        public void getGust()
        {
            gust = wGust.getSelected();
            if(gust > 0)
                gust = gust * 2 + 6;
            float f = (float)gust * 1.0F;
            Mission.setGust(f);
            PlMission.setChanged();
        }

        public void getTurbulence()
        {
            turbulence = wTurbulence.getSelected();
            if(turbulence > 0)
                turbulence += 2;
            float f = (float)turbulence * 1.0F;
            Mission.setTurbulence(f);
            PlMission.setChanged();
        }

        public void afterCreated()
        {
            super.afterCreated();
            resized();
            close(false);
        }

        GWindowEditControl wTimeH;
        GWindowEditControl wTimeM;
        GWindowComboControl wCloudType;
        GWindowEditControl wCloudHeight;
        GWindowCheckBox wTimeFix;
        GWindowCheckBox wWeaponFix;
        GWindowComboControl wYear;
        GWindowComboControl wDay;
        GWindowComboControl wMonth;
        GWindowEditControl wWindDirection;
        GWindowEditControl wWindVelocity;
        GWindowComboControl wGust;
        GWindowComboControl wTurbulence;
        GWindowBoxSeparate boxWindTable;
        GWindowLabel wLabel0;
        GWindowLabel wLabel1;
        GWindowLabel wLabel2;
        GWindowLabel wLabel3;
        GWindowLabel wLabel4;
        GWindowLabel wLabel5;
        GWindowLabel wLabel6;
        GWindowLabel wLabel7;
        GWindowLabel wLabel8;
        GWindowLabel wLabel9;
        GWindowLabel wLabel10;
        GWindowLabel wLabel00;
        GWindowLabel wLabel11;
        GWindowLabel wLabel22;
        GWindowLabel wLabel33;
        GWindowLabel wLabel44;
        GWindowLabel wLabel55;
        GWindowLabel wLabel66;
        GWindowLabel wLabel77;
        GWindowLabel wLabel88;
        GWindowLabel wLabel99;
        GWindowLabel wLabel1010;

        public WConditions()
        {
            doNew(Plugin.builder.clientWindow, 2.0F, 2.0F, 23F, 45F, true);
            super.bSizable = true;
        }
    }

    class MenuItem extends GWindowMenuItem
    {

        public void execute()
        {
            recentMissionFileName = cap.caption;
            if(!isChanged())
            {
                missionFileName = lastOpenFile = recentMissionFileName;
                ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                load("missions/" + missionFileName);
                return;
            } else
            {
                new GWindowMessageBox(builder.clientWindow.root, 20F, true, i18n("LoadMission"), i18n("ConfirmExitMsg"), 1, 0.0F) {

                    public void result(int i)
                    {
                        if(i == 3)
                        {
                            if(missionFileName != null)
                            {
                                save("missions/" + missionFileName);
                                missionFileName = lastOpenFile = recentMissionFileName;
                                ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                                load("missions/" + missionFileName);
                            } else
                            {
                                GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] {
                                    new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
                                        "*.mis"
                                    })
                                }) {

                                    public void result(String s)
                                    {
                                        if(s != null)
                                        {
                                            s = checkMisExtension(s);
                                            missionFileName = lastOpenFile = s;
                                            ((GWindowRootMenu)Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                                            save("missions/" + s);
                                        }
                                        missionFileName = lastOpenFile = recentMissionFileName;
                                        ((GWindowRootMenu)Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                                        load("missions/" + missionFileName);
                                    }

                                }
;
                                gwindowfilesaveas.exec = new DlgFileConfirmSave();
                                if(lastOpenFile != null)
                                    gwindowfilesaveas.setSelectFile(lastOpenFile);
                            }
                        } else
                        {
                            missionFileName = lastOpenFile = recentMissionFileName;
                            ((GWindowRootMenu)Plugin.builder.clientWindow.root).statusBar.setDefaultHelp(missionFileName);
                            load("missions/" + missionFileName);
                        }
                    }
                }
;
                return;
            }
        }

        int indx;

        public MenuItem(GWindowMenu gwindowmenu, String s, String s1, int i)
        {
            super(gwindowmenu, s, s1);
            indx = i;
        }
    }


    public static void setChanged()
    {
        if(cur != null)
            cur.bChanged = true;
    }

    public static boolean isChanged()
    {
        if(cur != null)
            return cur.bChanged;
        else
            return false;
    }

    public static String missionFileName()
    {
        if(cur == null)
            return null;
        else
            return cur.missionFileName;
    }

    public static void doSetReload(boolean flag)
    {
        cur.bReload = flag;
        return;
    }

    public static void doMissionReload()
    {
        if(cur == null)
            return;
        if(!cur.bReload)
        {
            return;
        } else
        {
            cur.bReload = false;
            cur.doLoadMission("missions/" + cur.missionFileName);
            return;
        }
    }

    public static boolean doLoadMissionFile(String s, String sOld)
    {
        if(s == null)
            return false;
        int l = "missions/".length();
        if(s.toLowerCase().startsWith("missions/"))
            s = s.substring(l, s.length());
        if(sOld.toLowerCase().startsWith("missions/"))
            sOld = sOld.substring(l, sOld.length());
        ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(sOld);
        cur.lastOpenFile = null;
        cur.missionFileName = s;
        cur.doLoadMission("missions/" + s);
        cur.addToRecentMissions("missions/" + sOld);
        cur.missionFileName = sOld;
        cur.lastOpenFile = sOld;
        return true;
    }

    public boolean load(String s)
    {
        Plugin.builder.deleteAll();
        SectFile sectfile = new SectFile(s, 0);
        int i = sectfile.sectionIndex("MAIN");
        if(i < 0)
        {
            Plugin.builder.tipErr("MissionLoad: '" + s + "' - section MAIN not found");
            return false;
        }
        int j = sectfile.varIndex(i, "MAP");
        if(j < 0)
        {
            Plugin.builder.tipErr("MissionLoad: '" + s + "' - in section MAIN line MAP not found");
            return false;
        }
        String s1 = sectfile.value(i, j);
        PlMapLoad.Land land = PlMapLoad.getLandForFileName(s1);
        if(land == PlMapLoad.getLandLoaded())
        {
            World.cur().statics.restoreAllBridges();
            World.cur().statics.restoreAllHouses();
        } else
        if(!pluginMapLoad.mapLoad(land))
        {
            Plugin.builder.tipErr("MissionLoad: '" + s + "' - tirrain '" + s1 + "' not loaded");
            return false;
        }
        int k = sectfile.get("MAIN", "TIMECONSTANT", 0, 0, 1);
        World.cur().setTimeOfDayConstant(k == 1);
        World.setTimeofDay(sectfile.get("MAIN", "TIME", 12F, 0.0F, 23.99F));
        int l = sectfile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
        World.cur().setWeaponsConstant(l == 1);
        String s2 = sectfile.get("MAIN", "player", (String)null);
        Path.playerNum = sectfile.get("MAIN", "playerNum", 0, 0, 3);
        missionArmy = sectfile.get("MAIN", "army", 1, 1, 2);
        year = sectfile.get("SEASON", "Year", 1940, 1900, Calendar.getInstance().get(Calendar.YEAR));
        month = sectfile.get("SEASON", "Month", World.land().config.month, 1, 12);
        day = sectfile.get("SEASON", "Day", 15, 1, 31);
        Mission.setDate(year, month, day);
        World.land().setMonth(month);
        World.land().setDay(day);
        windDirection = sectfile.get("WEATHER", "WindDirection", 0.0F, 0.0F, 359.99F);
        windVelocity = sectfile.get("WEATHER", "WindSpeed", 0.0F, 0.0F, 15F);
        gust = sectfile.get("WEATHER", "Gust", 0, 0, 12);
        turbulence = sectfile.get("WEATHER", "Turbulence", 0, 0, 6);
        cloudType = sectfile.get("MAIN", "CloudType", 0, 0, 6);
        cloudHeight = sectfile.get("MAIN", "CloudHeight", 1000F, 300F, 5000F);
        Mission.createClouds(cloudType, cloudHeight);
        if(((Main) (Main3D.cur3D())).clouds != null)
            ((Main) (Main3D.cur3D())).clouds.setShow(false);
        Main3D.cur3D().spritesFog.setShow(false);
        wConditions.update();
        zutiInitMDSVariables();
        zutiLoadMDSVariables(sectfile);
        wFoW.update();
        wCraters.update();
        wRespawnTime.update();
        wMisc.update();
        Plugin.doLoad(sectfile);
        loadMissionMods(sectfile);
        if(s2 != null)
        {
            Object aobj[] = Plugin.builder.pathes.getOwnerAttached();
            int i1 = 0;
            do
            {
                if(i1 >= aobj.length)
                    break;
                Path path = (Path)aobj[i1];
                if(s2.equals(path.name()))
                {
                    if(!((PathAir)path).bOnlyAI)
                    {
                        Path.player = path;
                        missionArmy = path.getArmy();
                    }
                    break;
                }
                i1++;
            } while(true);
        }
        Plugin.builder.repaint();
        bChanged = false;
        addToRecentMissions(s);
        return true;
    }

    public boolean save(String s)
    {
        if(PlMapLoad.getLandLoaded() == null)
        {
            Plugin.builder.tipErr("MissionSave: tirrain not selected");
            return false;
        }
        SectFile sectfile = new SectFile();
        sectfile.setFileName(s);
        int i = sectfile.sectionAdd("MAIN");
        sectfile.lineAdd(i, "MAP", PlMapLoad.mapFileName());
        sectfile.lineAdd(i, "TIME", "" + World.getTimeofDay());
        if(World.cur().isTimeOfDayConstant())
            sectfile.lineAdd(i, "TIMECONSTANT", "1");
        if(World.cur().isWeaponsConstant())
            sectfile.lineAdd(i, "WEAPONSCONSTANT", "1");
        sectfile.lineAdd(i, "CloudType", "" + cloudType);
        sectfile.lineAdd(i, "CloudHeight", "" + cloudHeight);
        if(Actor.isValid(Path.player))
        {
            sectfile.lineAdd(i, "player", Path.player.name());
            if(Path.playerNum >= ((PathAir)Path.player).planes)
                Path.playerNum = 0;
        } else
        {
            Path.playerNum = 0;
        }
        sectfile.lineAdd(i, "army", "" + missionArmy);
        sectfile.lineAdd(i, "playerNum", "" + Path.playerNum);
        int j = sectfile.sectionAdd("SEASON");
        sectfile.lineAdd(j, "Year", "" + year);
        sectfile.lineAdd(j, "Month", "" + month);
        sectfile.lineAdd(j, "Day", "" + day);
        int k = sectfile.sectionAdd("WEATHER");
        sectfile.lineAdd(k, "WindDirection", "" + windDirection);
        sectfile.lineAdd(k, "WindSpeed", "" + windVelocity);
        sectfile.lineAdd(k, "Gust", "" + gust);
        sectfile.lineAdd(k, "Turbulence", "" + turbulence);
        zutiSaveMDSVariables(sectfile);
        if(!Plugin.doSave(sectfile))
        {
            return false;
        } else
        {
            saveMissionMods(sectfile);
            sectfile.saveFile(s);
            bChanged = false;
            addToRecentMissions(s);
            return true;
        }
    }

    public void mapLoaded()
    {
        zutiInitMDSVariables();
        if(!Plugin.builder.isLoadedLandscape())
            return;
        String s = "maps/" + PlMapLoad.mapFileName();
        SectFile sectfile = new SectFile(s);
        int i = sectfile.sectionIndex("static");
        if(i >= 0 && sectfile.vars(i) > 0)
        {
            String s1 = sectfile.var(i, 0);
            Statics.load(HomePath.concatNames(s, s1), PlMapLoad.bridgeActors);
        }
        int j = sectfile.sectionIndex("text");
        if(j >= 0 && sectfile.vars(j) > 0)
        {
            String s2 = sectfile.var(j, 0);
            if(Main3D.cur3D().land2DText == null)
                Main3D.cur3D().land2DText = new Land2DText();
            else
                Main3D.cur3D().land2DText.clear();
            Main3D.cur3D().land2DText.load(HomePath.concatNames(s, s2));
        }
        Statics.trim();
        if(Landscape.isExistMeshs())
        {
            for(int k = 0; k < PathFind.tShip.sy; k++)
            {
                for(int l = 0; l < PathFind.tShip.sx; l++)
                    if(Landscape.isExistMesh(l, PathFind.tShip.sy - k - 1))
                    {
                        PathFind.tShip.I(l, k, PathFind.tShip.intI(l, k) | 8);
                        PathFind.tNoShip.I(l, k, PathFind.tNoShip.intI(l, k) | 8);
                    }

            }

        }
        Mission.createClouds(cloudType, cloudHeight);
        if(((Main) (Main3D.cur3D())).clouds != null)
            ((Main) (Main3D.cur3D())).clouds.setShow(false);
        Main3D.cur3D().spritesFog.setShow(false);
        wConditions.update();
        wFoW.update();
        wCraters.update();
        wRespawnTime.update();
        wMisc.update();
    }

    public void configure()
    {
        Plugin.builder.bMultiSelect = false;
        if(Plugin.getPlugin("MapLoad") == null)
        {
            throw new RuntimeException("PlMission: plugin 'MapLoad' not found");
        } else
        {
            pluginMapLoad = (PlMapLoad)Plugin.getPlugin("MapLoad");
            return;
        }
    }

    void _viewTypeAll(boolean flag)
    {
        Plugin.doViewTypeAll(flag);
        viewBridge(flag);
        viewRunaway(flag);
        viewSpawn(flag);
        viewName.bChecked = Plugin.builder.conf.bShowName = flag;
        viewTime.bChecked = Plugin.builder.conf.bShowTime = flag;
        for(int i = 0; i < Plugin.builder.conf.bShowArmy.length; i++)
            viewArmy[i].bChecked = Plugin.builder.conf.bShowArmy[i] = flag;

        if(!flag)
            Plugin.builder.setSelected(null);
    }

    void viewBridge(boolean flag)
    {
        Plugin.builder.conf.bViewBridge = flag;
        viewBridge.bChecked = Plugin.builder.conf.bViewBridge;
    }

    void viewBridge()
    {
        viewBridge(!Plugin.builder.conf.bViewBridge);
    }

    void viewRunaway(boolean flag)
    {
        Plugin.builder.conf.bViewRunaway = flag;
        viewRunaway.bChecked = Plugin.builder.conf.bViewRunaway;
    }

    void viewSpawn(boolean flag)
    {
        Plugin.builder.conf.bViewSpawn = flag;
        viewSpawn.bChecked = Plugin.builder.conf.bViewSpawn;
    }

    void viewSpawn()
    {
        viewSpawn(!Plugin.builder.conf.bViewSpawn);
    }

    void viewRunaway()
    {
        viewRunaway(!Plugin.builder.conf.bViewRunaway);
    }

    public static void checkShowCurrentArmy()
    {
        Object obj = Plugin.builder.selectedPath();
        if(obj == null)
            obj = Plugin.builder.selectedActor();
        if(obj == null)
            return;
        int i = ((Actor)obj).getArmy();
        if(!Plugin.builder.conf.bShowArmy[i])
            Plugin.builder.setSelected(null);
    }

    private String checkMisExtension(String s)
    {
        if(!s.toLowerCase().endsWith(".mis"))
            return s.trim() + ".mis";
        else
            return s.trim();
    }

    public void createGUI()
    {
        zutiInitMDSVariables();
        Plugin.builder.mDisplayFilter.subMenu.addItem("-", null);
        viewBridge = Plugin.builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showBridge"), Plugin.i18n("TIPshowBridge")) {

            public void execute()
            {
                viewBridge();
            }

        }
);
        viewBridge.bChecked = Plugin.builder.conf.bViewBridge;
        viewRunaway = Plugin.builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showRunway"), Plugin.i18n("TIPshowRunway")) {

            public void execute()
            {
                viewRunaway();
            }

        }
);
        viewRunaway.bChecked = Plugin.builder.conf.bViewRunaway;
        viewSpawn = Plugin.builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showSpawnPoints"), Plugin.i18n("TIPshowSpawnPoints")) {

            public void execute()
            {
                viewSpawn();
            }

        }
);
        viewSpawn.bChecked = Plugin.builder.conf.bViewSpawn;
        Plugin.builder.mDisplayFilter.subMenu.addItem("-", null);
        viewName = Plugin.builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showName"), Plugin.i18n("TIPshowName")) {

            public void execute()
            {
                super.bChecked = Plugin.builder.conf.bShowName = !Plugin.builder.conf.bShowName;
            }

        }
);
        viewName.bChecked = Plugin.builder.conf.bShowName;
        viewTime = Plugin.builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showTime"), Plugin.i18n("TIPshowTime")) {

            public void execute()
            {
                super.bChecked = Plugin.builder.conf.bShowTime = !Plugin.builder.conf.bShowTime;
            }

        }
);
        viewTime.bChecked = Plugin.builder.conf.bShowTime;
        viewArmy = new GWindowMenuItemArmy[Builder.armyAmount()];
        for(int i = 0; i < Builder.armyAmount(); i++)
        {
            viewArmy[i] = Plugin.builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItemArmy(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("showArmy") + " " + I18N.army(Army.name(i)), Plugin.i18n("TIPshowArmy") + " " + I18N.army(Army.name(i)), i) {

                public void execute()
                {
                    super.bChecked = Plugin.builder.conf.bShowArmy[super.army] = !Plugin.builder.conf.bShowArmy[super.army];
                    PlMission.checkShowCurrentArmy();
                }

            }
);
            viewArmy[i].bChecked = Plugin.builder.conf.bShowArmy[i];
        }

        Plugin.builder.mDisplayFilter.subMenu.addItem("-", null);
        Plugin.builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("&ShowAll"), Plugin.i18n("TIPShowAll")) {

            public void execute()
            {
                _viewTypeAll(true);
            }

        }
);
        Plugin.builder.mDisplayFilter.subMenu.addItem(new GWindowMenuItem(Plugin.builder.mDisplayFilter.subMenu, Plugin.i18n("&HideAll"), Plugin.i18n("TIPHideAll")) {

            public void execute()
            {
                _viewTypeAll(false);
            }

        }
);
        Plugin.builder.mFile.subMenu.addItem(0, new GWindowMenuItem(Plugin.builder.mFile.subMenu, Plugin.i18n("Load"), Plugin.i18n("TIPLoad")) {

            public void execute()
            {
                doDlgLoadMission();
            }

        }
);
        itemRecentMissions = Plugin.builder.mFile.subMenu.addItem(1, new GWindowMenuItem(Plugin.builder.mFile.subMenu, Plugin.i18n("RecentMissions"), Plugin.i18n("TIPRecentMissions")));
        itemRecentMissions.subMenu = (GWindowMenu)itemRecentMissions.create(new GWindowMenu());
        itemRecentMissions.subMenu.close(false);
        SectFile sectfile = new SectFile("users/" + World.cur().userCfg.sId + "/settings.ini");
        int j = sectfile.sectionIndex("builder");
        if(j > -1)
        {
            int k = sectfile.vars(j);
            if(k > 12)
                k = 12;
            for(int l = 0; l < k; l++)
                itemRecentMissions.subMenu.addItem(new MenuItem(itemRecentMissions.subMenu, sectfile.line(j, l), null, l));
        }
        Plugin.builder.mFile.subMenu.addItem(2, new GWindowMenuItem(Plugin.builder.mFile.subMenu, Plugin.i18n("Clear Recent"), "Clear recent missions") {

            public void execute()
            {
                new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 20F, true, "Recent Missions", "Clear recent missions?", 1, 0.0F) {

                    public void result(int i)
                    {
                        if(i == 3)
                        {
                            itemRecentMissions.subMenu.clearItems();
                            itemRecentMissions.subMenu.resolutionChanged();
                        }
                    }
                }
;
            }
        }
);
        Plugin.builder.mFile.subMenu.addItem(3, new GWindowMenuItem(Plugin.builder.mFile.subMenu, "-", null));

        Plugin.builder.mFile.subMenu.addItem(4, new GWindowMenuItem(Plugin.builder.mFile.subMenu, Plugin.i18n("Save"), Plugin.i18n("TIPSaveAs")) {

            public void execute()
            {
                if(missionFileName != null)
                {
                    save("missions/" + missionFileName);
                } else
                {
                    GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(super.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] {
                        new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
                            "*.mis"
                        })
                    }) {

                        public void result(String s)
                        {
                            if(s != null)
                            {
                                s = checkMisExtension(s);
                                missionFileName = lastOpenFile = s;
                                ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                                save("missions/" + s);
                            }
                        }

                    }
;
                    gwindowfilesaveas.exec = new DlgFileConfirmSave();
                    if(lastOpenFile != null)
                        gwindowfilesaveas.setSelectFile(lastOpenFile);
                }
            }


        }
);
        Plugin.builder.mFile.subMenu.addItem(5, new GWindowMenuItem(Plugin.builder.mFile.subMenu, Plugin.i18n("SaveAs"), Plugin.i18n("TIPSaveAs")) {

            public void execute()
            {
                GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(super.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] {
                    new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
                        "*.mis"
                    })
                }) {

                    public void result(String s)
                    {
                        if(s != null)
                        {
                            s = checkMisExtension(s);
                            missionFileName = lastOpenFile = s;
                            ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                            save("missions/" + s);
                        }
                    }

                }
;
                gwindowfilesaveas.exec = new DlgFileConfirmSave();
                if(lastOpenFile != null)
                    gwindowfilesaveas.setSelectFile(lastOpenFile);
            }


        }
);
        Plugin.builder.mFile.subMenu.addItem(6, new GWindowMenuItem(Plugin.builder.mFile.subMenu, Plugin.i18n("Play"), Plugin.i18n("TIPPlay")) {

            public void execute()
            {
                if(!Plugin.builder.isLoadedLandscape())
                    return;
                if(PlMission.isChanged() || missionFileName == null)
                {
                    if(missionFileName != null)
                    {
                        if(save("missions/" + missionFileName))
                            playMission();
                    } else
                    {
                        GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(super.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] {
                            new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
                                "*.mis"
                            })
                        }) {

                            public void result(String s)
                            {
                                if(s != null)
                                {
                                    s = checkMisExtension(s);
                                    missionFileName = lastOpenFile = s;
                                    ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                                    if(save("missions/" + s))
                                        playMission();
                                }
                            }

                        }
;
                        gwindowfilesaveas.exec = new DlgFileConfirmSave();
                        if(lastOpenFile != null)
                            gwindowfilesaveas.setSelectFile(lastOpenFile);
                    }
                } else
                {
                    playMission();
                }
            }


        }
);
        Plugin.builder.mFile.subMenu.bNotify = true;
        Plugin.builder.mFile.subMenu.addNotifyListener(new GNotifyListener() {

            public boolean notify(GWindow gwindow, int j, int k)
            {
                if(j != 13)
                {
                    return false;
                } else
                {
                    Plugin.builder.mFile.subMenu.getItem(1).bEnable = itemRecentMissions.subMenu.size() > 0;
                    Plugin.builder.mFile.subMenu.getItem(2).bEnable = itemRecentMissions.subMenu.size() > 0;
                    Plugin.builder.mFile.subMenu.getItem(3).bEnable = false;
                    Plugin.builder.mFile.subMenu.getItem(4).bEnable = Plugin.builder.isLoadedLandscape();
                    Plugin.builder.mFile.subMenu.getItem(5).bEnable = Plugin.builder.isLoadedLandscape();
                    Plugin.builder.mFile.subMenu.getItem(6).bEnable = Plugin.builder.isLoadedLandscape();

                    return false;
                }
            }

        }
);
        mConditions = Plugin.builder.mConfigure.subMenu.addItem(0, new GWindowMenuItem(Plugin.builder.mConfigure.subMenu, Plugin.i18n("&Conditions"), Plugin.i18n("TIPConditions")) {

            public void execute()
            {
                if(wConditions.isVisible())
                    wConditions.hideWindow();
                else
                    wConditions.showWindow();
            }

        }
);
        mFoW = Plugin.builder.mConfigure.subMenu.addItem(1, new GWindowMenuItem(Plugin.builder.mConfigure.subMenu, Plugin.i18n("&FogOfWar"), Plugin.i18n("TIPFoW")) {

            public void execute()
            {
                if(wFoW.isVisible())
                    wFoW.hideWindow();
                else
                    wFoW.showWindow();
            }

        }
);
        Plugin.builder.mConfigure.subMenu.addItem(2, "-", null);
        mCraters = Plugin.builder.mConfigure.subMenu.addItem(3, new GWindowMenuItem(Plugin.builder.mConfigure.subMenu, Plugin.i18n("Cra&ters"), Plugin.i18n("TIPCraters")) {

            public void execute()
            {
                if(wCraters.isVisible())
                    wCraters.hideWindow();
                else
                    wCraters.showWindow();
            }

        }
);
        mRespawnTime = Plugin.builder.mConfigure.subMenu.addItem(4, new GWindowMenuItem(Plugin.builder.mConfigure.subMenu, Plugin.i18n("&Respawn"), Plugin.i18n("TIPRespawnTime")) {

            public void execute()
            {
                if(wRespawnTime.isVisible())
                    wRespawnTime.hideWindow();
                else
                    wRespawnTime.showWindow();
            }

        }
);
        mMisc = Plugin.builder.mConfigure.subMenu.addItem(5, new GWindowMenuItem(Plugin.builder.mConfigure.subMenu, Plugin.i18n("&Misc"), Plugin.i18n("TIPMisc")) {

            public void execute()
            {
                if(wMisc.isVisible())
                    wMisc.hideWindow();
                else
                    wMisc.showWindow();
            }

        }
);
        Plugin.builder.mEdit.subMenu.addItem(0, "-", null);
        wConditions = new WConditions();
        wConditions.update();
        wFoW = new WFoW();
        wFoW.update();
        wCraters = new WCraters();
        wCraters.update();
        wRespawnTime = new WRespawnTime();
        wRespawnTime.update();
        wMisc = new WMisc();
        wMisc.update();
        zuti_manageAircraftsRed = new Zuti_WManageAircrafts();
        zuti_manageAircraftsRed.hideWindow();
        zuti_manageAircraftsBlue = new Zuti_WManageAircrafts();
        zuti_manageAircraftsBlue.hideWindow();
    }

    private void doLoadMission(String s)
    {
        load(s);
//        _loadFileName = s;
//        _loadMessageBox = new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 20F, true, Plugin.i18n("StandBy"), Plugin.i18n("LoadingMission"), 4, 0.0F);
//        new MsgAction(72, 0.0D) {
//
//            public void doAction()
//            {
//                load(_loadFileName);
//                _loadMessageBox.close(false);
//            }
//
//        }
//;
    }

    private void playMission()
    {
        if(missionFileName.substring(0, 3).equalsIgnoreCase("Net"))
        {
            new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 25F, true, I18N.gui("warning.Warning"), "Net missions cannot be played from FMB.", 3, 0.0F);

        } else if(missionFileName.substring(0, 5).equalsIgnoreCase("Quick") || Actor.isValid(Path.player))
        {
            playMissionGo();
        } else
        {
            new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 25F, true, I18N.gui("warning.Warning"), "Player aircraft not set! Continue?", 1, 0.0F) {

                public void result(int i)
                {
                    if(i == 3)
                        playMissionGo();
                }
            }
;
        }
    }

    private void playMissionGo()
    {
        saveRecentMissionsList();
        Main.cur().currentMissionFile = new SectFile("missions/" + missionFileName, 0);
//        bReload = true;
//        if(Main.stateStack().size() == 2)
//            Main.stateStack().push(4);
//        else
//            Main.stateStack().pop();
        if(missionFileName.substring(0,5).equalsIgnoreCase("Quick"))
        {
            bReload = false;
            Main.stateStack().push(14);
        } else
        {
            bReload = true;
            Main.stateStack().push(4);
        }
    }

    private void doDlgLoadMission()
    {
        if(!isChanged())
        {
            _doDlgLoadMission();
            return;
        } else
        {
            new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 20F, true, Plugin.i18n("LoadMission"), Plugin.i18n("ConfirmExitMsg"), 1, 0.0F) {

                public void result(int i)
                {
                    if(i == 3)
                    {
                        if(missionFileName != null)
                        {
                            save("missions/" + missionFileName);
                            _doDlgLoadMission();
                        } else
                        {
                            GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(super.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] {
                                new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
                                    "*.mis"
                                })
                            }) {

                                public void result(String s)
                                {
                                    if(s != null)
                                    {
                                        s = checkMisExtension(s);
                                        missionFileName = lastOpenFile = s;
                                        ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                                        save("missions/" + s);
                                    }
                                    _doDlgLoadMission();
                                }

                            }
;
                            gwindowfilesaveas.exec = new DlgFileConfirmSave();
                            if(lastOpenFile != null)
                                gwindowfilesaveas.setSelectFile(lastOpenFile);
                        }
                    } else
                    {
                        _doDlgLoadMission();
                    }
                }


            }
;
            return;
        }
    }

    private void _doDlgLoadMission()
    {
        GWindowFileOpen gwindowfileopen = new GWindowFileOpen(((GWindow) (Plugin.builder.clientWindow)).root, true, Plugin.i18n("LoadMission"), "missions", new GFileFilter[] {
            new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
                "*.mis"
            })
        }) {

            public void result(String s)
            {
                if(s != null)
                {
                    missionFileName = lastOpenFile = s;
                    ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                    doLoadMission("missions/" + s);
                }
            }

        }
;
        if(lastOpenFile != null)
            gwindowfileopen.setSelectFile(lastOpenFile);
    }

    public boolean exitBuilder()
    {
        saveRecentMissionsList();
        if(!isChanged())
        {
            clearMissionMods();
            missionFileName = lastOpenFile = null;
            ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
            return true;
        } else
        {
            new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 20F, true, Plugin.i18n("ConfirmExit"), Plugin.i18n("ConfirmExitMsg"), 1, 0.0F) {

                public void result(int i)
                {
                    if(i == 3)
                    {
                        if(missionFileName != null)
                        {
                            save("missions/" + missionFileName);
                            clearMissionMods();
                            missionFileName = lastOpenFile = null;
                            ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                            Plugin.builder.doMenu_FileExit();
                        } else
                        {
                            GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(super.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] {
                                new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
                                    "*.mis"
                                })
                            }) {

                                public void result(String s)
                                {
                                    if(s != null)
                                    {
                                        s = checkMisExtension(s);
                                        missionFileName = lastOpenFile = s;
                                        ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                                        save("missions/" + s);
                                    }
                                    bChanged = false;
                                    clearMissionMods();
                                    missionFileName = lastOpenFile = null;
                                    ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                                    Plugin.builder.doMenu_FileExit();
                                }

                            }
;
                            gwindowfilesaveas.exec = new DlgFileConfirmSave();
                            if(lastOpenFile != null)
                                gwindowfilesaveas.setSelectFile(lastOpenFile);
                        }
                    } else
                    {
                        bChanged = false;
                        clearMissionMods();
                        missionFileName = lastOpenFile = null;
                        ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                        Plugin.builder.doMenu_FileExit();
                    }
                }
            }
;
            return false;
        }
    }

    public void loadNewMap()
    {
        if(!bChanged)
        {
            clearMissionMods();
            missionFileName = lastOpenFile = null;
            ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
            ((PlMapLoad)Plugin.getPlugin("MapLoad")).guiMapLoad();
            return;
        } else
        {
            new GWindowMessageBox(((GWindow) (Plugin.builder.clientWindow)).root, 20F, true, Plugin.i18n("SaveMission"), Plugin.i18n("ConfirmExitMsg"), 1, 0.0F) {

                public void result(int i)
                {
                    if(i == 3)
                    {
                        if(missionFileName != null)
                        {
                            save("missions/" + missionFileName);
                            clearMissionMods();
                            missionFileName = lastOpenFile = null;
                            ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                            ((PlMapLoad)Plugin.getPlugin("MapLoad")).guiMapLoad();
                        } else
                        {
                            GWindowFileSaveAs gwindowfilesaveas = new GWindowFileSaveAs(super.root, true, Plugin.i18n("SaveMission"), "missions", new GFileFilter[] {
                                new GFileFilterName(Plugin.i18n("MissionFiles"), new String[] {
                                    "*.mis"
                                })
                            }) {

                                public void result(String s)
                                {
                                    if(s != null)
                                    {
                                        s = checkMisExtension(s);
                                        missionFileName = lastOpenFile = s;
                                        ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                                        save("missions/" + s);
                                    }
                                    bChanged = false;
                                    clearMissionMods();
                                    missionFileName = lastOpenFile = null;
                                    ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                                    ((PlMapLoad)Plugin.getPlugin("MapLoad")).guiMapLoad();
                                }

                            }
;
                            gwindowfilesaveas.exec = new DlgFileConfirmSave();
                            if(lastOpenFile != null)
                                gwindowfilesaveas.setSelectFile(lastOpenFile);
                        }
                    } else
                    {
                        bChanged = false;
                        clearMissionMods();
                        missionFileName = lastOpenFile = null;
                        ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                        ((PlMapLoad)Plugin.getPlugin("MapLoad")).guiMapLoad();
                    }
                    missionFileName = null;
                    ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
                }
            }
;
            return;
        }
    }

    public void freeResources()
    {
        if(wConditions.isVisible())
            wConditions.hideWindow();
        if(wFoW.isVisible())
            wFoW.hideWindow();
        if(wCraters.isVisible())
            wCraters.hideWindow();
        if(wRespawnTime.isVisible())
            wRespawnTime.hideWindow();
        if(wMisc.isVisible())
            wMisc.hideWindow();
        if(!bReload)
        {
            missionFileName = null;
            ((GWindowRootMenu)((GWindow) (Plugin.builder.clientWindow)).root).statusBar.setDefaultHelp(missionFileName);
        }
    }

    public PlMission()
    {
        missionArmy = 1;
        cloudType = 0;
        cloudHeight = 1000F;
        windDirection = 0.0F;
        windVelocity = 0.0F;
        gust = 0;
        turbulence = 0;
        day = 15;
        month = World.land().config.month;
        year = 1940;
        bChanged = false;
        bReload = false;
        cur = this;
    }

    private String checkValidRange(int i, int j, int k)
    {
        if(i < j)
            i = j;
        else
        if(i > k)
            i = k;
        return Integer.toString(i);
    }

    private String checkValidRange(float f, float f1, float f2)
    {
        if(f < f1)
            f = f1;
        else
        if(f > f2)
            f = f2;
        return Float.toString(f);
    }

    private void zutiLoadMDSVariables(SectFile sectfile)
    {
        try
        {
            Mission.ZUTI_RADAR_IN_ADV_MODE = false;
            if(sectfile.get("MDS", "MDS_Radar_SetRadarToAdvanceMode", 0, 0, 1) == 1)
                Mission.ZUTI_RADAR_IN_ADV_MODE = true;
            zutiRadar_ShipsAsRadar = false;
            if(sectfile.get("MDS", "MDS_Radar_ShipsAsRadar", 0, 0, 1) == 1)
                zutiRadar_ShipsAsRadar = true;
            zutiRadar_ShipRadar_MaxRange = sectfile.get("MDS", "MDS_Radar_ShipRadar_MaxRange", 100, 1, 0x1869f);
            zutiRadar_ShipRadar_MinHeight = sectfile.get("MDS", "MDS_Radar_ShipRadar_MinHeight", 100, 0, 0x1869f);
            zutiRadar_ShipRadar_MaxHeight = sectfile.get("MDS", "MDS_Radar_ShipRadar_MaxHeight", 5000, 1000, 0x1869f);
            zutiRadar_ShipSmallRadar_MaxRange = sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MaxRange", 25, 1, 0x1869f);
            zutiRadar_ShipSmallRadar_MinHeight = sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MinHeight", 0, 0, 0x1869f);
            zutiRadar_ShipSmallRadar_MaxHeight = sectfile.get("MDS", "MDS_Radar_ShipSmallRadar_MaxHeight", 2000, 1000, 0x1869f);
            zutiRadar_ScoutsAsRadar = false;
            if(sectfile.get("MDS", "MDS_Radar_ScoutsAsRadar", 0, 0, 1) == 1)
                zutiRadar_ScoutsAsRadar = true;
            zutiRadar_ScoutRadar_MaxRange = sectfile.get("MDS", "MDS_Radar_ScoutRadar_MaxRange", 2, 1, 0x1869f);
            zutiRadar_ScoutRadar_DeltaHeight = sectfile.get("MDS", "MDS_Radar_ScoutRadar_DeltaHeight", 1500, 100, 0x1869f);
            zutiRadar_ScoutCompleteRecon = false;
            if(sectfile.get("MDS", "MDS_Radar_ScoutCompleteRecon", 0, 0, 1) == 1)
                zutiRadar_ScoutCompleteRecon = true;
            zutiLoadScouts_Red(sectfile);
            zutiLoadScouts_Blue(sectfile);
            zutiRadar_ScoutGroundObjects_Alpha = sectfile.get("MDS", "MDS_Radar_ScoutGroundObjects_Alpha", 5, 1, 11);
            zutiRadar_RefreshInterval = sectfile.get("MDS", "MDS_Radar_RefreshInterval", 0, 0, 0x1869f);
            zutiRadar_DisableVectoring = false;
            if(sectfile.get("MDS", "MDS_Radar_DisableVectoring", 0, 0, 1) == 1)
                zutiRadar_DisableVectoring = true;
            zutiRadar_EnableTowerCommunications = true;
            if(sectfile.get("MDS", "MDS_Radar_EnableTowerCommunications", 1, 0, 1) == 0)
                zutiRadar_EnableTowerCommunications = false;
            zutiRadar_HideUnpopulatedAirstripsFromMinimap = false;
            if(sectfile.get("MDS", "MDS_Radar_HideUnpopulatedAirstripsFromMinimap", 0, 0, 1) == 1)
                zutiRadar_HideUnpopulatedAirstripsFromMinimap = true;
            zutiMisc_DisableAIRadioChatter = false;
            if(sectfile.get("MDS", "MDS_Misc_DisableAIRadioChatter", 0, 0, 1) == 1)
                zutiMisc_DisableAIRadioChatter = true;
            zutiMisc_DespawnAIPlanesAfterLanding = true;
            if(sectfile.get("MDS", "MDS_Misc_DespawnAIPlanesAfterLanding", 1, 0, 1) == 0)
                zutiMisc_DespawnAIPlanesAfterLanding = false;
            zutiMisc_HidePlayersCountOnHomeBase = false;
            if(sectfile.get("MDS", "MDS_Misc_HidePlayersCountOnHomeBase", 0, 0, 1) == 1)
                zutiMisc_HidePlayersCountOnHomeBase = true;
            zutiMisc_BombsCat1_CratersVisibilityMultiplier = sectfile.get("MDS", "MDS_Misc_BombsCat1_CratersVisibilityMultiplier", 1.0F, 1.0F, 99999F);
            zutiMisc_BombsCat2_CratersVisibilityMultiplier = sectfile.get("MDS", "MDS_Misc_BombsCat2_CratersVisibilityMultiplier", 1.0F, 1.0F, 99999F);
            zutiMisc_BombsCat3_CratersVisibilityMultiplier = sectfile.get("MDS", "MDS_Misc_BombsCat3_CratersVisibilityMultiplier", 1.0F, 1.0F, 99999F);
            respawnTime_Bigship = sectfile.get("RespawnTime", "Bigship", 1800, 0, 0x124f80);
            respawnTime_Ship = sectfile.get("RespawnTime", "Ship", 1800, 0, 0x124f80);
            respawnTime_Aeroanchored = sectfile.get("RespawnTime", "Aeroanchored", 1800, 0, 0x124f80);
            respawnTime_Artillery = sectfile.get("RespawnTime", "Artillery", 1800, 0, 0x124f80);
            respawnTime_Searchlight = sectfile.get("RespawnTime", "Searchlight", 1800, 0, 0x124f80);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void zutiSaveMDSVariables(SectFile sectfile)
    {
        try
        {
            int i = sectfile.sectionIndex("MDS");
            if(i < 0)
                i = sectfile.sectionAdd("MDS");
            sectfile.lineAdd(i, "MDS_Radar_SetRadarToAdvanceMode", BoolToInt(Mission.ZUTI_RADAR_IN_ADV_MODE));
            sectfile.lineAdd(i, "MDS_Radar_RefreshInterval", Integer.toString(zutiRadar_RefreshInterval));
            sectfile.lineAdd(i, "MDS_Radar_DisableVectoring", BoolToInt(zutiRadar_DisableVectoring));
            sectfile.lineAdd(i, "MDS_Radar_EnableTowerCommunications", BoolToInt(zutiRadar_EnableTowerCommunications));
            sectfile.lineAdd(i, "MDS_Radar_ShipsAsRadar", BoolToInt(zutiRadar_ShipsAsRadar));
            sectfile.lineAdd(i, "MDS_Radar_ShipRadar_MaxRange", Integer.toString(zutiRadar_ShipRadar_MaxRange));
            sectfile.lineAdd(i, "MDS_Radar_ShipRadar_MinHeight", Integer.toString(zutiRadar_ShipRadar_MinHeight));
            sectfile.lineAdd(i, "MDS_Radar_ShipRadar_MaxHeight", Integer.toString(zutiRadar_ShipRadar_MaxHeight));
            sectfile.lineAdd(i, "MDS_Radar_ShipSmallRadar_MaxRange", Integer.toString(zutiRadar_ShipSmallRadar_MaxRange));
            sectfile.lineAdd(i, "MDS_Radar_ShipSmallRadar_MinHeight", Integer.toString(zutiRadar_ShipSmallRadar_MinHeight));
            sectfile.lineAdd(i, "MDS_Radar_ShipSmallRadar_MaxHeight", Integer.toString(zutiRadar_ShipSmallRadar_MaxHeight));
            sectfile.lineAdd(i, "MDS_Radar_ScoutsAsRadar", BoolToInt(zutiRadar_ScoutsAsRadar));
            sectfile.lineAdd(i, "MDS_Radar_ScoutRadar_MaxRange", Integer.toString(zutiRadar_ScoutRadar_MaxRange));
            sectfile.lineAdd(i, "MDS_Radar_ScoutRadar_DeltaHeight", Integer.toString(zutiRadar_ScoutRadar_DeltaHeight));
            sectfile.lineAdd(i, "MDS_Radar_HideUnpopulatedAirstripsFromMinimap", BoolToInt(zutiRadar_HideUnpopulatedAirstripsFromMinimap));
            sectfile.lineAdd(i, "MDS_Radar_ScoutGroundObjects_Alpha", Integer.toString(zutiRadar_ScoutGroundObjects_Alpha));
            sectfile.lineAdd(i, "MDS_Radar_ScoutCompleteRecon", BoolToInt(zutiRadar_ScoutCompleteRecon));
            zutiSaveScouts_Red(sectfile);
            zutiSaveScouts_Blue(sectfile);
            sectfile.lineAdd(i, "MDS_Misc_DisableAIRadioChatter", BoolToInt(zutiMisc_DisableAIRadioChatter));
            sectfile.lineAdd(i, "MDS_Misc_DespawnAIPlanesAfterLanding", BoolToInt(zutiMisc_DespawnAIPlanesAfterLanding));
            sectfile.lineAdd(i, "MDS_Misc_HidePlayersCountOnHomeBase", BoolToInt(zutiMisc_HidePlayersCountOnHomeBase));
            sectfile.lineAdd(i, "MDS_Misc_BombsCat1_CratersVisibilityMultiplier", Float.toString(zutiMisc_BombsCat1_CratersVisibilityMultiplier));
            sectfile.lineAdd(i, "MDS_Misc_BombsCat2_CratersVisibilityMultiplier", Float.toString(zutiMisc_BombsCat2_CratersVisibilityMultiplier));
            sectfile.lineAdd(i, "MDS_Misc_BombsCat3_CratersVisibilityMultiplier", Float.toString(zutiMisc_BombsCat3_CratersVisibilityMultiplier));
            i = sectfile.sectionAdd("RespawnTime");
            sectfile.lineAdd(i, "Bigship", Integer.toString(respawnTime_Bigship));
            sectfile.lineAdd(i, "Ship", Integer.toString(respawnTime_Ship));
            sectfile.lineAdd(i, "Aeroanchored", Integer.toString(respawnTime_Aeroanchored));
            sectfile.lineAdd(i, "Artillery", Integer.toString(respawnTime_Artillery));
            sectfile.lineAdd(i, "Searchlight", Integer.toString(respawnTime_Searchlight));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void zutiInitMDSVariables()
    {
        if(GUI.pad != null)
            GUI.pad.zutiColorAirfields = true;
        zutiRadar_ShipsAsRadar = false;
        zutiRadar_ShipRadar_MaxRange = 100;
        zutiRadar_ShipRadar_MinHeight = 100;
        zutiRadar_ShipRadar_MaxHeight = 5000;
        zutiRadar_ShipSmallRadar_MaxRange = 25;
        zutiRadar_ShipSmallRadar_MinHeight = 0;
        zutiRadar_ShipSmallRadar_MaxHeight = 2000;
        zutiRadar_ScoutsAsRadar = false;
        zutiRadar_ScoutRadar_MaxRange = 2;
        zutiRadar_ScoutRadar_DeltaHeight = 1500;
        zutiRadar_ScoutRadarType_Red = "";
        zutiRadar_ScoutRadarType_Blue = "";
        zutiRadar_RefreshInterval = 0;
        zutiRadar_DisableVectoring = false;
        zutiRadar_EnableTowerCommunications = true;
        zutiRadar_HideUnpopulatedAirstripsFromMinimap = false;
        Mission.ZUTI_RADAR_IN_ADV_MODE = false;
        zutiRadar_ScoutGroundObjects_Alpha = 5;
        zutiRadar_ScoutCompleteRecon = false;
        zutiMisc_DisableAIRadioChatter = false;
        zutiMisc_DespawnAIPlanesAfterLanding = true;
        zutiMisc_HidePlayersCountOnHomeBase = false;
        zutiMisc_BombsCat1_CratersVisibilityMultiplier = 1.0F;
        zutiMisc_BombsCat2_CratersVisibilityMultiplier = 1.0F;
        zutiMisc_BombsCat3_CratersVisibilityMultiplier = 1.0F;
        respawnTime_Bigship = 1800;
        respawnTime_Ship = 1800;
        respawnTime_Aeroanchored = 1800;
        respawnTime_Artillery = 1800;
        respawnTime_Searchlight = 1800;
    }

    private String BoolToInt(boolean flag)
    {
        if(flag)
            return "1";
        else
            return "0";
    }

    private void zutiSaveScouts_Red(SectFile sectfile)
    {
        if(zutiRadar_ScoutRadarType_Red != null && zutiRadar_ScoutRadarType_Red.trim().length() > 0)
        {
            int i = sectfile.sectionAdd("MDS_Scouts_Red");
            for(StringTokenizer stringtokenizer = new StringTokenizer(zutiRadar_ScoutRadarType_Red); stringtokenizer.hasMoreTokens(); sectfile.lineAdd(i, stringtokenizer.nextToken()));
        }
    }

    private void zutiSaveScouts_Blue(SectFile sectfile)
    {
        if(zutiRadar_ScoutRadarType_Blue != null && zutiRadar_ScoutRadarType_Blue.trim().length() > 0)
        {
            int i = sectfile.sectionAdd("MDS_Scouts_Blue");
            for(StringTokenizer stringtokenizer = new StringTokenizer(zutiRadar_ScoutRadarType_Blue); stringtokenizer.hasMoreTokens(); sectfile.lineAdd(i, stringtokenizer.nextToken()));
        }
    }

    private void zutiLoadScouts_Red(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("MDS_Scouts_Red");
        if(i > -1)
        {
            zutiRadar_ScoutRadarType_Red = "";
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                String s = sectfile.line(i, k);
                StringTokenizer stringtokenizer = new StringTokenizer(s);
                String s1 = null;
                if(stringtokenizer.hasMoreTokens())
                    s1 = stringtokenizer.nextToken();
                if(s1 != null)
                {
                    s1 = s1.intern();
                    Class class1 = (Class)Property.value(s1, "airClass", null);
                    if(class1 != null)
                        zutiRadar_ScoutRadarType_Red += s1 + " ";
                }
            }

        }
    }

    private void zutiLoadScouts_Blue(SectFile sectfile)
    {
        int i = sectfile.sectionIndex("MDS_Scouts_Blue");
        if(i > -1)
        {
            zutiRadar_ScoutRadarType_Blue = "";
            int j = sectfile.vars(i);
            for(int k = 0; k < j; k++)
            {
                String s = sectfile.line(i, k);
                StringTokenizer stringtokenizer = new StringTokenizer(s);
                String s1 = null;
                if(stringtokenizer.hasMoreTokens())
                    s1 = stringtokenizer.nextToken();
                if(s1 != null)
                {
                    s1 = s1.intern();
                    Class class1 = (Class)Property.value(s1, "airClass", null);
                    if(class1 != null)
                        zutiRadar_ScoutRadarType_Blue += s1 + " ";
                }
            }

        }
    }

    private void clearMissionMods()
    {
        sectModsMap.clear();
    }

    private void loadMissionMods(SectFile sectfile)
    {
        clearMissionMods();
        try
        {
            int modsSectionIndex = sectfile.sectionIndex("Mods");
            if(modsSectionIndex < 0)
                return;
            for(int varIndex = 0; varIndex < sectfile.vars(modsSectionIndex); varIndex++)
                sectModsMap.put("" + sectfile.var(modsSectionIndex, varIndex), "" + sectfile.value(modsSectionIndex, varIndex));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void saveMissionMods(SectFile sectfile)
    {
        try
        {
            if(!sectModsMap.isEmpty())
            {
                int modsSectionIndex = sectfile.sectionIndex("Mods");
                if(modsSectionIndex < 0)
                    modsSectionIndex = sectfile.sectionAdd("Mods");
                else
                    sectfile.sectionClear(modsSectionIndex);
                Iterator sectModsMapIterator = sectModsMap.entrySet().iterator();
                while (sectModsMapIterator.hasNext()) {
                    Map.Entry sectModsMapEntry = (Map.Entry)sectModsMapIterator.next();
                    sectfile.lineAdd(modsSectionIndex, "" + sectModsMapEntry.getKey(), "" + sectModsMapEntry.getValue());
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void saveRecentMissionsList()
    {
        SectFile sectfile = new SectFile("users/" + World.cur().userCfg.sId + "/settings.ini", 1);
        int j = itemRecentMissions.subMenu.size();
        if(j > 0)
        {
            if(sectfile.sectionIndex("builder") < 0)
                sectfile.sectionAdd("builder");
            int i = sectfile.sectionIndex("builder");
            sectfile.sectionClear(i);
            for(int k = 0; k < j; k++)
                if(itemRecentMissions.subMenu.getItem(k).cap.caption.indexOf('#') == -1 && itemRecentMissions.subMenu.getItem(k).cap.caption.indexOf('&') == -1)
                    sectfile.varAdd(i, itemRecentMissions.subMenu.getItem(k).cap.caption);
        } else
        if(sectfile.sectionIndex("builder") > -1)
            sectfile.sectionRemove(sectfile.sectionIndex("builder"));
        sectfile.saveFile();
    }

    private boolean addToRecentMissions(String s)
    {
        s = s.substring(9, s.length());
        if(s.substring(0, 5).equalsIgnoreCase("Quick") || s.equalsIgnoreCase("Single/LastMission.mis"))
            return false;
        int i = itemRecentMissions.subMenu.size();
        if(i > 0 && s.equalsIgnoreCase(itemRecentMissions.subMenu.getItem(0).cap.caption))
            return false;
        for(int j = 1; j < i; j++)
            if(s.equalsIgnoreCase(itemRecentMissions.subMenu.getItem(j).cap.caption))
            {
                for(int l = j - 1; l > -1; l--)
                    itemRecentMissions.subMenu.getItem(l + 1).cap.set(itemRecentMissions.subMenu.getItem(l).cap.caption);

                itemRecentMissions.subMenu.getItem(0).cap.set(s);
                return true;
            }

        if(i < 12)
            itemRecentMissions.subMenu.addItem(new MenuItem(itemRecentMissions.subMenu, "", null, i));
        for(int k = i - 1; k > -1; k--)
            if(k != 11)
                itemRecentMissions.subMenu.getItem(k + 1).cap.set(itemRecentMissions.subMenu.getItem(k).cap.caption);

        itemRecentMissions.subMenu.getItem(0).cap.set(s);
        itemRecentMissions.subMenu.resolutionChanged();
        return true;
    }

    protected static PlMission cur;
    protected int missionArmy;
    private int cloudType;
    private float cloudHeight;
    private float windDirection;
    private float windVelocity;
    private int gust;
    private int turbulence;
    private int day;
    private int month;
    private int year;
//    private String _yearKey[] = {
//        "1930", "1931", "1932", "1933", "1934", "1935", "1936", "1937", "1938", "1939",
//        "1940", "1941", "1942", "1943", "1944", "1945", "1946", "1947", "1948", "1949",
//        "1950", "1951", "1952", "1953", "1954", "1955", "1956", "1957", "1958", "1959",
//        "1960"
//    };
//    private String _dayKey[] = {
//        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
//        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
//        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
//        "31"
//    };
//    private String _monthKey[] = {
//        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
//        "11", "12"
//    };
    private static final int RECENT_MISSIONS_LIMIT = 12;
    private static final int RADAR_SHIPRADAR_MAXRANGE_MIN = 1;
    private static final int RADAR_SHIPRADAR_MAXRANGE_MAX = 0x1869f;
    private static final int RADAR_SHIPRADAR_MINHEIGHT_MIN = 0;
    private static final int RADAR_SHIPRADAR_MINHEIGHT_MAX = 0x1869f;
    private static final int RADAR_SHIPRADAR_MAXHEIGHT_MIN = 1000;
    private static final int RADAR_SHIPRADAR_MAXHEIGHT_MAX = 0x1869f;
    private static final int RADAR_SHIPSMALLRADAR_MAXRANGE_MIN = 1;
    private static final int RADAR_SHIPSMALLRADAR_MAXRANGE_MAX = 0x1869f;
    private static final int RADAR_SHIPSMALLRADAR_MINHEIGHT_MIN = 0;
    private static final int RADAR_SHIPSMALLRADAR_MINHEIGHT_MAX = 0x1869f;
    private static final int RADAR_SHIPSMALLRADAR_MAXHEIGHT_MIN = 1000;
    private static final int RADAR_SHIPSMALLRADAR_MAXHEIGHT_MAX = 0x1869f;
    private static final int RADAR_SCOUTRADAR_MAXRANGE_MIN = 1;
    private static final int RADAR_SCOUTRADAR_MAXRANGE_MAX = 0x1869f;
    private static final int RADAR_SCOUTRADAR_DELTAHEIGHT_MIN = 100;
    private static final int RADAR_SCOUTRADAR_DELTAHEIGHT_MAX = 0x1869f;
    private static final int RADAR_SCOUTGROUNDOBJECT_ALPHA_MIN = 1;
    private static final int RADAR_SCOUTGROUNDOBJECT_ALPHA_MAX = 11;
    private static final int RADAR_REFRESHINTERVAL_MIN = 0;
    private static final int RADAR_REFRESHINTERVAL_MAX = 0x1869f;
    private static final float MISC_BOMBSCAT1_CRATERSVISIBILITYMULTIPLIER_MIN = 1F;
    private static final float MISC_BOMBSCAT1_CRATERSVISIBILITYMULTIPLIER_MAX = 99999F;
    private static final float MISC_BOMBSCAT2_CRATERSVISIBILITYMULTIPLIER_MIN = 1F;
    private static final float MISC_BOMBSCAT2_CRATERSVISIBILITYMULTIPLIER_MAX = 99999F;
    private static final float MISC_BOMBSCAT3_CRATERSVISIBILITYMULTIPLIER_MIN = 1F;
    private static final float MISC_BOMBSCAT3_CRATERSVISIBILITYMULTIPLIER_MAX = 99999F;
    private static final int RT_BIGSHIP_MIN = 0;
    private static final int RT_BIGSHIP_MAX = 0x124f80;
    private static final int RT_SHIP_MIN = 0;
    private static final int RT_SHIP_MAX = 0x124f80;
    private static final int RT_AEROANCHORED_MIN = 0;
    private static final int RT_AEROANCHORED_MAX = 0x124f80;
    private static final int RT_ARTILLERY_MIN = 0;
    private static final int RT_ARTILLERY_MAX = 0x124f80;
    private static final int RT_SEARCHLIGHT_MIN = 0;
    private static final int RT_SEARCHLIGHT_MAX = 0x124f80;
    private boolean bChanged;
    private boolean bReload;
    private String missionFileName;
    private String recentMissionFileName;
    PlMapLoad pluginMapLoad;
    WConditions wConditions;
    GWindowMenuItem mConditions;
    WFoW wFoW;
    GWindowMenuItem mFoW;
    WCraters wCraters;
    GWindowMenuItem mCraters;
    WRespawnTime wRespawnTime;
    GWindowMenuItem mRespawnTime;
    WMisc wMisc;
    GWindowMenuItem mMisc;
    GWindowMenuItem viewBridge;
    GWindowMenuItem viewRunaway;
    GWindowMenuItem viewSpawn;
    GWindowMenuItem viewName;
    GWindowMenuItem viewTime;
    GWindowMenuItem viewArmy[];
    private GWindowMenuItem itemRecentMissions;
    private String lastOpenFile;
//    private GWindowMessageBox _loadMessageBox;
    private String _loadFileName;
    private int zutiRadar_RefreshInterval;
    private boolean zutiRadar_DisableVectoring;
    private boolean zutiRadar_EnableTowerCommunications;
    private boolean zutiRadar_HideUnpopulatedAirstripsFromMinimap;
    private boolean zutiRadar_ShipsAsRadar;
    private int zutiRadar_ShipRadar_MaxRange;
    private int zutiRadar_ShipRadar_MinHeight;
    private int zutiRadar_ShipRadar_MaxHeight;
    private int zutiRadar_ShipSmallRadar_MaxRange;
    private int zutiRadar_ShipSmallRadar_MinHeight;
    private int zutiRadar_ShipSmallRadar_MaxHeight;
    private boolean zutiRadar_ScoutsAsRadar;
    private int zutiRadar_ScoutRadar_MaxRange;
    private int zutiRadar_ScoutRadar_DeltaHeight;
    String zutiRadar_ScoutRadarType_Red;
    String zutiRadar_ScoutRadarType_Blue;
    private int zutiRadar_ScoutGroundObjects_Alpha;
    private boolean zutiRadar_ScoutCompleteRecon;
    private boolean zutiMisc_DisableAIRadioChatter;
    private boolean zutiMisc_DespawnAIPlanesAfterLanding;
    private boolean zutiMisc_HidePlayersCountOnHomeBase;
    private float zutiMisc_BombsCat1_CratersVisibilityMultiplier;
    private float zutiMisc_BombsCat2_CratersVisibilityMultiplier;
    private float zutiMisc_BombsCat3_CratersVisibilityMultiplier;
    private int respawnTime_Bigship;
    private int respawnTime_Ship;
    private int respawnTime_Aeroanchored;
    private int respawnTime_Artillery;
    private int respawnTime_Searchlight;
    private Zuti_WManageAircrafts zuti_manageAircraftsRed;
    private Zuti_WManageAircrafts zuti_manageAircraftsBlue;
    private HashMap sectModsMap = new HashMap();

    static 
    {
        Property.set(com.maddox.il2.builder.PlMission.class, "name", "Mission");
    }
}
