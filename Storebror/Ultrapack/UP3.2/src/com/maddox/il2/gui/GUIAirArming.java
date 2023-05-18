/*4.10.1 class*/
package com.maddox.il2.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.maddox.JGP.Color4f;
import com.maddox.JGP.Point3d;
import com.maddox.JGP.Vector3f;
import com.maddox.gwindow.GBevel;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowEditControl;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.Regiment;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorHMesh;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.BmpUtils;
import com.maddox.il2.engine.Camera3D;
import com.maddox.il2.engine.GUIRenders;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.LightEnvXY;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.engine.Render;
import com.maddox.il2.engine.Renders;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.NetUserRegiment;
import com.maddox.il2.net.ZutiSupportMethods_Net;
import com.maddox.il2.objects.ActorSimpleHMesh;
import com.maddox.il2.objects.ActorSimpleMesh;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.air.PaintScheme;
import com.maddox.rts.HomePath;
import com.maddox.rts.LDRres;
import com.maddox.rts.NetEnv;
import com.maddox.rts.ObjIO;
import com.maddox.rts.Property;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SFSInputStream;
import com.maddox.rts.SectFile;
import com.maddox.util.HashMapExt;

public class GUIAirArming extends GameState
{
	public GUIClient client;
	public DialogClient dialogClient;
	public GUIInfoMenu infoMenu;
	public GUIInfoName infoName;
	public GWindowEditControl wMashineGun;
	public GWindowEditControl wCannon;
	public GWindowEditControl wRocket;
	public GWindowEditControl wRocketDelay;
	public GWindowEditControl wBombDelay;
	// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
    public GWindowEditControl wBombFuze;
    // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
	public GWindowComboControl cFuel;
	public GWindowComboControl cAircraft;
	public GWindowComboControl cWeapon;
	public GWindowComboControl cCountry;
	public GWindowComboControl cRegiment;
	public GWindowComboControl cSkin;
	public GWindowComboControl cNoseart;
	public GWindowComboControl cPilot;
	public GWindowEditControl wNumber;
	public GWindowComboControl cSquadron;
	public GUISwitchBox3 sNumberOn;
	public GWindowComboControl cPlane;
	public GUIButton bBack;
	// MW Modified BEGIN: New Button declaration
	public GUIButton bJoy;
	// MW Modified END
	public static final int SINGLE = 0;
	public static final int CAMPAIGN = 1;
	public static final int DFIGHT = 2;
	public static final int COOP = 3;
	public static final int QUIK = 4;
	public static int stateId = 2;
	public ArrayList airNames;
	public ArrayList weaponNames;
	public HashMapExt regList;
	public HashMapExt regHash;
	public ResourceBundle resCountry;
	public ArrayList countryLst;
	protected boolean quikPlayer;
	protected int quikArmy;
	protected int quikPlanes;
	protected String quikPlane;
	protected String quikWeapon;
	protected int quikCurPlane;
	protected String quikRegiment;
	protected int quikSquadron;
	protected int quikWing;
	protected String quikSkin[] = {null, null, null, null};
	protected String quikNoseart[] = {null, null, null, null};
	protected String quikPilot[] = {null, null, null, null};
	protected boolean quikNumberOn[] = {true, true, true, true};
	protected int quikFuel;
	protected ArrayList quikListPlane;
	
	//TODO: Modified by |ZUTI|: from private to protected
	//--------------------------
	protected String planeName;
	protected boolean bEnableWeaponChange;
	//--------------------------
	
	private int playerNum;
	public GUIRenders renders;
	public Camera3D camera3D;
	public _Render3D render3D;
	public ActorHMesh actorMesh;
	public ArrayList weaponMeshs;
	public float animateMeshA;
	public float animateMeshT;
	private Orient _orient;
	
	private boolean isNet()
	{
		return stateId == 2 || stateId == 3;
	}
	private String airName()
	{
		return (String)airNames.get(cAircraft.getSelected());
	}
	
	static class UserRegiment
	{
		
		protected String country;
		protected String branch;
		protected String fileName;
		protected char id[];
		protected String shortInfo;
		protected int gruppeNumber;
		
		public UserRegiment(String s) throws Exception
		{
			id = new char[2];
			gruppeNumber = 1;
			fileName = s;
			String s1 = Main.cur().netFileServerReg.primaryPath();
			PropertyResourceBundle propertyresourcebundle = new PropertyResourceBundle(new SFSInputStream(s1 + "/" + s));
			country = propertyresourcebundle.getString("country");
			country = country.toLowerCase().intern();
			branch = country;
			country = Regiment.getCountryFromBranch(branch);
			String s2 = propertyresourcebundle.getString("id");
			id[0] = s2.charAt(0);
			id[1] = s2.charAt(1);
			if ((id[0] < '0' || id[0] > '9') && (id[0] < 'A' || id[0] > 'Z'))
				throw new RuntimeException("Bad regiment id[0]");
			if ((id[1] < '0' || id[1] > '9') && (id[1] < 'A' || id[1] > 'Z'))
				throw new RuntimeException("Bad regiment id[1]");
			try
			{
				String s3 = propertyresourcebundle.getString("short");
				if (s3 == null || s3.length() == 0)
					s3 = s;
				shortInfo = s3;
			}
			catch (Exception exception)
			{
				shortInfo = s;
			}
			try
			{
				String s4 = propertyresourcebundle.getString("gruppeNumber");
				if (s4 != null)
				{
					try
					{
						gruppeNumber = Integer.parseInt(s4);
					}
					catch (Exception exception2)
					{}
					if (gruppeNumber < 1)
						gruppeNumber = 1;
					if (gruppeNumber > 5)
						gruppeNumber = 5;
				}
			}
			catch (Exception exception1)
			{}
		}
	}
	
	public class DialogClient extends GUIDialogClient
	{
		public boolean notify(GWindow gwindow, int i, int j)
		{
			if (i != 2)
				return super.notify(gwindow, i, j);
			
			UserCfg usercfg = World.cur().userCfg;
			
			//TODO: Added by |ZUTI|: save sZutiMultiCrew status to usercfg
			//-------------------------------------------------------------------
			usercfg.bZutiMultiCrew = sZutiMultiCrew.isChecked();
			usercfg.bZutiMultiCrewAnytime = sZutiMultiCrewAnytime.isChecked();
			if (gwindow == sZutiMultiCrew)
			{
				if( !sZutiMultiCrew.isChecked() )
					sZutiMultiCrewAnytime.setChecked(false, false);
				
				sZutiMultiCrewAnytime.setEnable(sZutiMultiCrew.isChecked());
				return true;
			}
			//-------------------------------------------------------------------
			
			// MW Modified BEGIN: New button behaviour
			if (gwindow == bJoy)
			{
				Main.stateStack().push(53);
				return true;
			}
			// MW Modified END
			if (gwindow == cAircraft)
			{
				if (GUIAirArming.stateId != 2 && GUIAirArming.stateId != 4)
					return true;
				if (GUIAirArming.stateId == 2)
					usercfg.netAirName = airName();
				else if (GUIAirArming.stateId == 4)
					quikPlane = airName();
				
				//TODO: Added by |ZUTI|
				//-----------------------------------------------------------------------------------------------------
				if( zutiSelectedBornPlace != null && zutiSelectedBornPlace.zutiEnablePlaneLimits )
					ZutiSupportMethods_GUI.fillWeaponsListBasedOnBornPlace(GUIAirArming.this, zutiSelectedBornPlace);
				else
					fillWeapons();
				//System.out.println("GUIAirArming - Loading weapons 2");
				
				ZutiSupportMethods_GUI.setFuelSelectionForAircraft(GUIAirArming.this, zutiSelectedBornPlace, cFuel.getSelected());
				
				if (bEnableWeaponChange)
				{
					if (isNet())
						usercfg.fuel = (cFuel.getSelected() + 1) * 10;
					else if (GUIAirArming.stateId == 4)
						quikFuel = (cFuel.getSelected() + 1) * 10;
					else
						Main.cur().currentMissionFile.set(planeName, "Fuel", (cFuel.getSelected() + 1) * 10);
				}
				//-----------------------------------------------------------------------------------------------------
				
				selectWeapon();
				fillSkins();
				selectSkin();
				selectNoseart();
				setMesh();
				prepareMesh();
				prepareWeapons();
				prepareSkin();
				preparePilot();
				prepareNoseart();
				return true;
			}
			if (gwindow == cCountry)
			{
				if (GUIAirArming.stateId != 2 && GUIAirArming.stateId != 4)
					return true;
				fillRegiments();
				String s = (String)countryLst.get(cCountry.getSelected());
				ArrayList arraylist = (ArrayList)regList.get(s);
				Object obj = arraylist.get(cRegiment.getSelected());
				if (GUIAirArming.stateId == 2)
				{
					if (obj instanceof Regiment)
						usercfg.netRegiment = ((Regiment)obj).name();
					else
						usercfg.netRegiment = ((UserRegiment)obj).fileName;
				}
				else if (GUIAirArming.stateId == 4)
					quikRegiment = ((Regiment)obj).name();
				selectNoseart();
				setMesh();
				prepareMesh();
				prepareWeapons();
				prepareSkin();
				preparePilot();
				prepareNoseart();
				return true;
			}
			if (gwindow == cRegiment)
			{
				if (GUIAirArming.stateId != 2 && GUIAirArming.stateId != 4)
					return true;
				String s1 = (String)countryLst.get(cCountry.getSelected());
				ArrayList arraylist1 = (ArrayList)regList.get(s1);
				Object obj1 = arraylist1.get(cRegiment.getSelected());
				if (GUIAirArming.stateId == 2)
				{
					if (obj1 instanceof Regiment)
						usercfg.netRegiment = ((Regiment)obj1).name();
					else
						usercfg.netRegiment = ((UserRegiment)obj1).fileName;
				}
				else if (GUIAirArming.stateId == 4)
					quikRegiment = ((Regiment)obj1).name();
				prepareMesh();
				return true;
			}
			if (gwindow == cWeapon)
			{
				if (!bEnableWeaponChange)
					return true;
				if (isNet())
					usercfg.setWeapon(airName(), (String)weaponNames.get(cWeapon.getSelected()));
				else if (GUIAirArming.stateId == 4)
					quikWeapon = (String)weaponNames.get(cWeapon.getSelected());
				else
					Main.cur().currentMissionFile.set(planeName, "weapons", (String)weaponNames.get(cWeapon.getSelected()));
				prepareWeapons();
				return true;
			}
			if (gwindow == cSkin)
			{
				int k = cSkin.getSelected();
				if (GUIAirArming.stateId == 4)
				{
					if (k == 0)
						quikSkin[quikCurPlane] = null;
					else
						quikSkin[quikCurPlane] = cSkin.get(k);
				}
				else if (k == 0)
					usercfg.setSkin(airName(), null);
				else
					usercfg.setSkin(airName(), cSkin.get(k));
				prepareSkin();
				return true;
			}
			if (gwindow == cNoseart)
			{
				int l = cNoseart.getSelected();
				if (GUIAirArming.stateId == 4)
				{
					if (l == 0)
						quikNoseart[quikCurPlane] = null;
					else
						quikNoseart[quikCurPlane] = cNoseart.get(l);
				}
				else if (l == 0)
					usercfg.setNoseart(airName(), null);
				else
					usercfg.setNoseart(airName(), cNoseart.get(l));
				if (l == 0)
				{
					setMesh();
					prepareMesh();
					prepareWeapons();
					prepareSkin();
					preparePilot();
				}
				prepareNoseart();
				return true;
			}
			if (gwindow == cPilot)
			{
				if (GUIAirArming.stateId == 4)
				{
					if (cPilot.getSelected() == 0)
						quikPilot[quikCurPlane] = null;
					else
						quikPilot[quikCurPlane] = cPilot.getValue();
				}
				else if (cPilot.getSelected() == 0)
					usercfg.netPilot = null;
				else
					usercfg.netPilot = cPilot.getValue();
				preparePilot();
				return true;
			}
			if (gwindow == wMashineGun)
			{
				usercfg.coverMashineGun = clampValue(wMashineGun, usercfg.coverMashineGun, 100F, 1000F);
				return true;
			}
			if (gwindow == wCannon)
			{
				usercfg.coverCannon = clampValue(wCannon, usercfg.coverCannon, 100F, 1000F);
				return true;
			}
			if (gwindow == wRocket)
			{
				usercfg.coverRocket = clampValue(wRocket, usercfg.coverRocket, 100F, 1000F);
				return true;
			}
			if (gwindow == wRocketDelay)
			{
				usercfg.rocketDelay = clampValue(wRocketDelay, usercfg.rocketDelay, UserCfg.ROCKET_DELAY_MIN, UserCfg.ROCKET_DELAY_MAX);
				return true;
			}
			if (gwindow == wBombDelay)
			{
				usercfg.bombDelay = clampValue(wBombDelay, usercfg.bombDelay, UserCfg.BOMB_DELAY_MIN, UserCfg.BOMB_DELAY_MAX);
				return true;
			}
			// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
            if (gwindow == wBombFuze)
            {
                usercfg.bombFuze = clampValue(wBombFuze, usercfg.bombFuze, UserCfg.BOMB_FUZE_MIN, UserCfg.BOMB_FUZE_MAX);
                return true;
            }
            // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
			if (gwindow == cFuel)
			{
				if (bEnableWeaponChange)
					if (isNet())
						usercfg.fuel = (cFuel.getSelected() + 1) * 10;
					else if (GUIAirArming.stateId == 4)
						quikFuel = (cFuel.getSelected() + 1) * 10;
					else
						Main.cur().currentMissionFile.set(planeName, "Fuel", (cFuel.getSelected() + 1) * 10);
			}
			else
			{
				if (gwindow == cSquadron)
					if (GUIAirArming.stateId != 2)
					{
						return true;
					}
					else
					{
						usercfg.netSquadron = cSquadron.getSelected();
						prepareMesh();
						return true;
					}
				if (gwindow == wNumber)
				{
					if (GUIAirArming.stateId != 2)
						return true;
					String s2 = wNumber.getValue();
					int i1 = usercfg.netTacticalNumber;
					try
					{
						i1 = Integer.parseInt(s2);
					}
					catch (Exception exception)
					{}
					if (i1 < 1)
						i1 = 1;
					if (i1 > 99)
						i1 = 99;
					wNumber.setValue("" + i1, false);
					usercfg.netTacticalNumber = i1;
					prepareMesh();
					return true;
				}
				if (gwindow == sNumberOn)
				{
					if (GUIAirArming.stateId == 4)
						quikNumberOn[quikCurPlane] = sNumberOn.isChecked();
					else
						usercfg.netNumberOn = sNumberOn.isChecked();
					prepareMesh();
				}
				else
				{
					if (gwindow == cPlane)
					{
						if (GUIAirArming.stateId != 4)
							return true;
						quikCurPlane = cPlane.getSelected();
						if (quikPlayer && quikCurPlane == 0)
						{
							wMashineGun.showWindow();
							wCannon.showWindow();
							wRocket.showWindow();
							wRocketDelay.showWindow();
							wBombDelay.showWindow();
							// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
                            wBombFuze.showWindow();
							// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
						}
						else
						{
							wMashineGun.hideWindow();
							wCannon.hideWindow();
							wRocket.hideWindow();
							wRocketDelay.hideWindow();
							wBombDelay.hideWindow();
                            // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
                            wBombFuze.hideWindow();
                            // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
						}
						sNumberOn.setChecked(quikNumberOn[quikCurPlane], false);
						fillSkins();
						selectSkin();
						selectNoseart();
						fillPilots();
						selectPilot();
						setMesh();
						prepareMesh();
						prepareWeapons();
						prepareSkin();
						preparePilot();
						prepareNoseart();
						return true;
					}
					if (gwindow == bBack)
						switch (GUIAirArming.stateId)
						{
							case 2 : // '\002'
								((NetUser)NetEnv.host()).replicateNetUserRegiment();
								// fall through
								
							case 3 : // '\003'
								((NetUser)NetEnv.host()).replicateSkin();
								((NetUser)NetEnv.host()).replicateNoseart();
								((NetUser)NetEnv.host()).replicatePilot();
								// fall through
								
							case 4 : // '\004'
							default :
								usercfg.saveConf();
								destroyMesh();
								airNames.clear();
								cAircraft.clear(false);
								regList.clear();
								regHash.clear();
								cRegiment.clear(false);
								countryLst.clear();
								cCountry.clear(false);
								cWeapon.clear();
								weaponNames.clear();
								cSkin.clear(false);
								cPilot.clear(false);
								cNoseart.clear(false);
								Main.stateStack().pop();
								return true;
						}
				}
			}
			return super.notify(gwindow, i, j);
		}
		
		public void render()
		{
			super.render();
            // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
//			GUISeparate.draw(this, GColor.Gray, x1024(628F), y1024(176F), x1024(364F), 2.0F);
//			GUISeparate.draw(this, GColor.Gray, x1024(628F), y1024(320F), x1024(364F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(624F), y1024(140F), x1024(372F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(624F), y1024(264F), x1024(372F), 2.0F);
            GUISeparate.draw(this, GColor.Gray, x1024(624F), y1024(444F), x1024(372F), 2.0F);
            // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
			GUISeparate.draw(this, GColor.Gray, x1024(32F), y1024(640F), x1024(962F), 2.0F);
			setCanvasColor(GColor.Gray);
			setCanvasFont(0);
            // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
//			draw(x1024(644F), y1024(32F), x1024(332F), y1024(32F), 1, i18n("neta.Aircraft"));
//			draw(x1024(644F), y1024(96F), x1024(332F), y1024(32F), 1, i18n("neta.WeaponLoadout"));
//			draw(x1024(644F), y1024(176F), x1024(332F), y1024(32F), 1, i18n("neta.Country"));
//			draw(x1024(644F), y1024(240F), x1024(332F), y1024(32F), 1, i18n("neta.Regiment"));
//			draw(x1024(644F), y1024(320F), x1024(332F), y1024(32F), 1, i18n("neta.Skin"));
//			draw(x1024(644F), y1024(384F), x1024(332F), y1024(32F), 1, i18n("neta.Pilot"));
			if (GUIAirArming.stateId == 2)
			{
				draw(x1024(628F), y1024(448F), x1024(220F), y1024(32F), 0, i18n("neta.Number"));
				draw(x1024(864F), y1024(448F), x1024(128F), y1024(32F), 2, i18n("neta.Squadron"));
			}
			else
			{
				draw(x1024(628F), y1024(480F), x1024(220F), y1024(32F), 0, i18n("neta.NumberOn"));
			}
            draw(x1024(644F), y1024(20F), x1024(332F), y1024(32F), 1, i18n("neta.Aircraft"));
            draw(x1024(644F), y1024(76F), x1024(332F), y1024(32F), 1, i18n("neta.WeaponLoadout"));
            draw(x1024(644F), y1024(144F), x1024(332F), y1024(32F), 1, i18n("neta.Country"));
            draw(x1024(644F), y1024(200F), x1024(332F), y1024(32F), 1, i18n("neta.Regiment"));
            draw(x1024(644F), y1024(268F), x1024(332F), y1024(32F), 1, i18n("neta.Skin"));
            draw(x1024(644F), y1024(324F), x1024(332F), y1024(32F), 1, i18n("neta.Pilot"));
            // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
			GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
			if (GUIAirArming.stateId != 4 || quikPlayer && quikCurPlane == 0)
			{
				draw(x1024(80F), y1024(505F), x1024(576F), y1024(32F), 1, i18n("neta.WeaponConver"));
				draw(x1024(32F), y1024(544F), x1024(160F), y1024(32F), 2, i18n("neta.MachineGuns") + " ");
				draw(x1024(32F), y1024(592F), x1024(160F), y1024(32F), 2, i18n("neta.Cannons") + " ");
				draw(x1024(272F), y1024(544F), x1024(48F), y1024(32F), 0, " " + i18n("neta.m."));
				draw(x1024(272F), y1024(592F), x1024(48F), y1024(32F), 0, " " + i18n("neta.m."));
				draw(x1024(320F), y1024(544F), x1024(160F), y1024(32F), 2, i18n("neta.Rockets") + " ");
				draw(x1024(320F), y1024(592F), x1024(160F), y1024(32F), 2, i18n("neta.RocketDelay") + " ");
				draw(x1024(560F), y1024(544F), x1024(48F), y1024(32F), 0, " " + i18n("neta.m."));
				draw(x1024(560F), y1024(592F), x1024(48F), y1024(32F), 0, " " + i18n("neta.sec."));
				draw(x1024(608F), y1024(544F), x1024(224F), y1024(32F), 2, i18n("neta.BombDelay") + " ");
				draw(x1024(928F) - guilookandfeel.getVScrollBarW(), y1024(544F), x1024(48F), y1024(32F), 0, " " + i18n("neta.sec."));
				// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
	            draw(x1024(608F), y1024(592F), x1024(224F), y1024(32F), 2, bombFuzeCaption + ": ");
	            draw(x1024(928F) - guilookandfeel.getVScrollBarW(), y1024(592F), x1024(48F), y1024(32F), 0, " " + i18n("neta.sec."));
				// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
			}
			// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
//			draw(x1024(608F), y1024(592F), x1024(224F), y1024(32F), 2, i18n("neta.FuelQuantity") + " ");
//			draw(x1024(928F), y1024(592F), x1024(48F), y1024(32F), 0, " %");
            draw(x1024(608F), y1024(656F), x1024(224F), y1024(32F), 2, i18n("neta.FuelQuantity") + " ");
            draw(x1024(928F), y1024(656F), x1024(48F), y1024(32F), 0, " %");
            // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
			draw(x1024(96F), y1024(656F), x1024(320F), y1024(48F), 0, i18n("neta.Apply"));
			// MW Modified BEGIN: New button text position
			draw(x1024(396F), y1024(656F), x1024(620F), y1024(48F), 0, i18n("neta.Joystick"));
			// MW Modified END
			
			//TODO: Added by |ZUTI|: multicrew
			//-------------------------------------------------
			draw(x1024(40F), y1024(460F), x1024(220F), y1024(48F), 0, i18n("arming.multiCrew"));
			//draw(x1024(460F), y1024(656F), x1024(220F), y1024(48F), 0, i18n("Allow MultiCrew:"));
			draw(x1024(320F), y1024(460F), x1024(220F), y1024(48F), 0, i18n("arming.multiCrewAnytime"));
			//draw(x1024(700F), y1024(656F), x1024(220F), y1024(48F), 0, i18n("Crew Can Join Anytime:"));
			//-------------------------------------------------

			// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
//			if (cNoseart.isVisible())
            if (cNoseart.isEnable())
				//draw(x1024(292F), y1024(656F), x1024(320F), y1024(48F), 2, i18n("neta.Noseart"));
                draw(x1024(644F), y1024(380F), x1024(332F), y1024(32F), 1, i18n("neta.Noseart"));
            else
                draw(x1024(644F), y1024(380F), x1024(332F), y1024(32F), 1, i18n("neta.Noseart") + " (" + i18n("neta.None") + ")");
            // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
			setCanvasColorWHITE();
			guilookandfeel.drawBevel(this, x1024(32F), y1024(32F), x1024(564F), y1024(432F), guilookandfeel.bevelComboDown, guilookandfeel.basicelements);
		}
		
		public void setPosSize()
		{
			set1024PosSize(0.0F, 32F, 1024F, 736F);
			wMashineGun.set1024PosSize(192F, 544F, 80F, 32F);
			wCannon.set1024PosSize(192F, 592F, 80F, 32F);
			wRocket.set1024PosSize(480F, 544F, 80F, 32F);
			wRocketDelay.set1024PosSize(480F, 592F, 80F, 32F);
			GUILookAndFeel guilookandfeel = (GUILookAndFeel)lookAndFeel();
			wBombDelay.setPosSize(x1024(832F), y1024(544F), x1024(96F) - guilookandfeel.getVScrollBarW(), y1024(32F));
			// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
            wBombFuze.setPosSize(x1024(832F), y1024(592F), x1024(96F) - guilookandfeel.getVScrollBarW(), y1024(32F));
//			cFuel.set1024PosSize(832F, 592F, 96F, 32F);
            cFuel.set1024PosSize(832F, 656F, 96F, 32F);
            // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
            // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
//			if (GUIAirArming.stateId == 4)
//				cAircraft.set1024PosSize(628F, 64F, 298F, 32F);
//			else
//				cAircraft.set1024PosSize(628F, 64F, 364F, 32F);
//			cPlane.set1024PosSize(932F, 64F, 60F, 32F);
//			cWeapon.set1024PosSize(628F, 128F, 364F, 32F);
//			cCountry.set1024PosSize(628F, 208F, 364F, 32F);
//			cRegiment.set1024PosSize(628F, 272F, 364F, 32F);
//			cSkin.set1024PosSize(628F, 352F, 364F, 32F);
//			cPilot.set1024PosSize(628F, 416F, 364F, 32F);
            if (GUIAirArming.stateId == 4)
                cAircraft.set1024PosSize(628F, 46F, 298F, 32F);
            else
                cAircraft.set1024PosSize(628F, 46F, 364F, 32F);
            cPlane.set1024PosSize(932F, 46F, 60F, 32F);
            cWeapon.set1024PosSize(628F, 102F, 364F, 32F);
            cCountry.set1024PosSize(628F, 170F, 364F, 32F);
            cRegiment.set1024PosSize(628F, 226F, 364F, 32F);
            cSkin.set1024PosSize(628F, 294F, 364F, 32F);
            cPilot.set1024PosSize(628F, 350F, 364F, 32F);
            cNoseart.set1024PosSize(628F, 406F, 364F, 32F);
            // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
			wNumber.set1024PosSize(628F, 480F, 112F, 32F);
			cSquadron.set1024PosSize(896F, 480F, 96F, 32F);
			sNumberOn.setPosC(x1024(944F), y1024(496F));
			GUILookAndFeel guilookandfeel1 = (GUILookAndFeel)lookAndFeel();
			GBevel gbevel = guilookandfeel1.bevelComboDown;
			renders.setPosSize(x1024(32F) + gbevel.L.dx, y1024(32F) + gbevel.T.dy, x1024(564F) - gbevel.L.dx - gbevel.R.dx, y1024(432F) - gbevel.T.dy - gbevel.B.dy);
			bBack.setPosC(x1024(56F), y1024(680F));
			// MW Modified BEGIN: New button position
			bJoy.setPosC(x1024(356F), y1024(680F));
			// MW Modified END
			
			//TODO: Added by |ZUTI|
			//-------------------------------------------------
			sZutiMultiCrew.setPosC(x1024(230F), y1024(485F));
			sZutiMultiCrewAnytime.setPosC(x1024(580F), y1024(485F));
			//-------------------------------------------------
		}
		
		public DialogClient()
		{}
	}
	
	class _Render3D extends Render
	{
		
		public void preRender()
		{
			if (Actor.isValid(actorMesh))
			{
				if (animateMeshA != 0.0F || animateMeshT != 0.0F)
				{
					actorMesh.pos.getAbs(_orient);
					_orient.set(_orient.azimut() + animateMeshA * client.root.deltaTimeSec, _orient.tangage() + animateMeshT * client.root.deltaTimeSec, 0.0F);
					// MW Modified BEGIN: Bug correction (Yaw forced to go back to [0:360] range)
					float f = _orient.getYaw();
					while (f > 360F)
					{
						f = f - 360F;
					}
					while (f < 0F)
					{
						f = f + 360F;
					}
					_orient.setYaw(f);
					// MW Modified END
					actorMesh.pos.setAbs(_orient);
					actorMesh.pos.reset();
				}
				actorMesh.draw.preRender(actorMesh);
				for (int i = 0; i < weaponMeshs.size(); i++)
				{
					ActorMesh actormesh = (ActorMesh)weaponMeshs.get(i);
					if (Actor.isValid(actormesh))
						actormesh.draw.preRender(actormesh);
				}
				
			}
		}
		
		public void render()
		{
			if (Actor.isValid(actorMesh))
			{
				Render.prepareStates();
				actorMesh.draw.render(actorMesh);
				for (int i = 0; i < weaponMeshs.size(); i++)
				{
					ActorMesh actormesh = (ActorMesh)weaponMeshs.get(i);
					if (Actor.isValid(actormesh))
						actormesh.draw.render(actormesh);
				}	
			}
		}
		
		public _Render3D(Renders renders1, float f)
		{
			super(renders1, f);
			setClearColor(new Color4f(0.5F, 0.78F, 0.92F, 1.0F));
			useClearStencil(true);
		}
	}
	
	public void _enter()
	{
		//TODO: Added by |ZUTI|
		zutiSelectedBornPlace = null;
		
		try
		{
			Object localObject1;
			Object localObject2;
			int k;
			Object localObject3;
			Object localObject4;
			Object localObject5;
			int i2;
			Object localObject6;
			Object localObject8;
			Object localObject9;
			Object localObject10;
			Object localObject12;
			if (this.resCountry == null)
				this.resCountry = ResourceBundle.getBundle("i18n/country", RTSConf.cur.locale, LDRres.loader());
			this.bEnableWeaponChange = true;
			this.cFuel.setEnable(true);
			UserCfg localUserCfg = World.cur().userCfg;
			this.wMashineGun.setValue("" + localUserCfg.coverMashineGun, false);
			this.wCannon.setValue("" + localUserCfg.coverCannon, false);
			this.wRocket.setValue("" + localUserCfg.coverRocket, false);
			this.wRocketDelay.setValue("" + localUserCfg.rocketDelay, false);
			this.wBombDelay.setValue("" + localUserCfg.bombDelay, false);
			// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
            this.wBombFuze.setValue("" + localUserCfg.bombFuze, false);
            if("ru".equalsIgnoreCase(RTSConf.cur.locale.getLanguage())) {
                bombFuzeCaption = "\\u041F\\u0440\\u0435\\u0434\\u043E\\u0445\\u0440\\u0430\\u043D\\u0438\\u0442\\u0435\\u043B\\u044C \\u0431\\u043E\\u043C\\u0431\\u044B";
            } else if("cs".equalsIgnoreCase(RTSConf.cur.locale.getLanguage())) {
                bombFuzeCaption = "Bombov\\u00E1 pojistka";
            } else if("de".equalsIgnoreCase(RTSConf.cur.locale.getLanguage())) {
                bombFuzeCaption = "Bombensicherung";
            } else if("fr".equalsIgnoreCase(RTSConf.cur.locale.getLanguage())) {
                bombFuzeCaption = "Fus\\u00E9e de Bombe";
            } else if("hu".equalsIgnoreCase(RTSConf.cur.locale.getLanguage())) {
                bombFuzeCaption = "Bomba biztos\\u00EDt\\u00E9k";
            } else if("pl".equalsIgnoreCase(RTSConf.cur.locale.getLanguage())) {
                bombFuzeCaption = "Bezpiecznik Bomba";
            } else {
                bombFuzeCaption = "Bomb Fuze";
            }
			// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
			
			SectFile localSectFile = Main.cur().currentMissionFile;
			if (localSectFile == null)
			{
				World.cur().setWeaponsConstant(false);
			}
			else
			{
				int j = localSectFile.get("MAIN", "WEAPONSCONSTANT", 0, 0, 1);
				World.cur().setWeaponsConstant(j == 1);
			}
			
			//TODO: Added by |ZUTI|: set sZutiMultiCrew from prew usercmd
			//------------------------------------------------------------------------------
			sZutiMultiCrew.setChecked(localUserCfg.bZutiMultiCrew, false);
			sZutiMultiCrewAnytime.setChecked(localUserCfg.bZutiMultiCrewAnytime, false);
			if( !sZutiMultiCrew.isChecked() )
				sZutiMultiCrewAnytime.setChecked(false, false);
			
			sZutiMultiCrewAnytime.setEnable(sZutiMultiCrew.isChecked());
			//------------------------------------------------------------------------------			
			
			//System.out.println("State id: " + stateId);
			switch (stateId)
			{
				case 0 :
				case 1 :
					this.wMashineGun.showWindow();
					this.wCannon.showWindow();
					this.wRocket.showWindow();
					this.wRocketDelay.showWindow();
					this.wBombDelay.showWindow();
					// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
                    this.wBombFuze.showWindow();
					// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
					this.cPlane.hideWindow();
					this.wNumber.hideWindow();
					this.cSquadron.hideWindow();
					this.sNumberOn.showWindow();
					this.sNumberOn.setChecked(localUserCfg.netNumberOn, false);
					localSectFile = Main.cur().currentMissionFile;
					localObject2 = localSectFile.get("MAIN", "player", (String)null);
					this.planeName = ((String)localObject2);
					localObject3 = ((String)localObject2).substring(0, ((String)localObject2).length() - 1);
					localObject4 = ((String)localObject3).substring(0, ((String)localObject3).length() - 1);
					Regiment localRegiment = (Regiment)Actor.getByName((String)localObject4);
					localObject5 = localSectFile.get(this.planeName, "Class", (String)null);
					localObject6 = ObjIO.classForName((String)localObject5);
					localObject8 = Property.stringValue((Class)localObject6, "keyName", null);
					this.airNames.add(localObject8);
					this.cAircraft.add(I18N.plane((String)localObject8));
					this.cAircraft.setSelected(0, true, false);
					this.countryLst.add(localRegiment.branch());
					this.cCountry.add(this.resCountry.getString(localRegiment.branch()));
					this.cCountry.setSelected(0, true, false);
					localObject10 = new ArrayList();
					((ArrayList)localObject10).add(localRegiment);
					this.regList.put(localRegiment.branch(), localObject10);
					this.cRegiment.add(localRegiment.shortInfo());
					this.cRegiment.setSelected(0, true, false);
					int i7 = localSectFile.get(this.planeName, "Fuel", 100, 0, 100);
					//TODO: Edited by |ZUTI|: smaller fuel selections
					//-----------------------------------------------------------------------------------------
					int selection = (i7/10)-1;
					if( selection < 0 ) 
						selection = 0;

					ZutiSupportMethods_GUI.setFuelSelectionForAircraft(GUIAirArming.this, zutiSelectedBornPlace, selection);
					//-----------------------------------------------------------------------------------------
					this.playerNum = localSectFile.get("Main", "playerNum", 0);
					if (stateId == 1)
						this.bEnableWeaponChange = ((this.playerNum == 0) && (!(World.cur().isWeaponsConstant())));
					else
						this.bEnableWeaponChange = (!(World.cur().isWeaponsConstant()));
					this.cFuel.setEnable(this.bEnableWeaponChange);
					
					break;
				case 3 :
					this.playerNum = -1;
					this.wMashineGun.showWindow();
					this.wCannon.showWindow();
					this.wRocket.showWindow();
					this.wRocketDelay.showWindow();
					this.wBombDelay.showWindow();
					// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
                    this.wBombFuze.showWindow();
					// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
					this.cPlane.hideWindow();
					this.wNumber.hideWindow();
					this.cSquadron.hideWindow();
					this.sNumberOn.showWindow();
					this.sNumberOn.setChecked(localUserCfg.netNumberOn, false);
					this.planeName = GUINetAircraft.selectedWingName();
					this.bEnableWeaponChange = (!(World.cur().isWeaponsConstant()));
					int i = (int)localUserCfg.fuel;
					if (!(this.bEnableWeaponChange))
					{
						localObject2 = Main.cur().currentMissionFile;
						i = ((SectFile)localObject2).get(this.planeName, "Fuel", 100, 0, 100);
					}
					//TODO: Edited by |ZUTI|: smaller fuel selections
					//-----------------------------------------------------------------------------------------
					selection = (i/10)-1;
					if( selection < 0 ) 
						selection = 0;
					
					ZutiSupportMethods_GUI.setFuelSelectionForAircraft(GUIAirArming.this, zutiSelectedBornPlace, selection);
					//-----------------------------------------------------------------------------------------
					this.cFuel.setEnable(this.bEnableWeaponChange);
					this.airNames.add(GUINetAircraft.selectedAircraftKeyName());
					this.cAircraft.add(GUINetAircraft.selectedAircraftName());
					this.cAircraft.setSelected(0, true, false);
					this.countryLst.add(GUINetAircraft.selectedRegiment().branch());
					this.cCountry.add(this.resCountry.getString(GUINetAircraft.selectedRegiment().branch()));
					this.cCountry.setSelected(0, true, false);
					localObject2 = new ArrayList();
					((ArrayList)localObject2).add(GUINetAircraft.selectedRegiment());
					this.regList.put(GUINetAircraft.selectedRegiment().branch(), localObject2);
					this.cRegiment.add(GUINetAircraft.selectedRegiment().shortInfo());
					this.cRegiment.setSelected(0, true, false);
					
					break;
				case 2 :
					dfight :
					{
						int l;
						int i9;
						ArrayList localArrayList2;
						this.playerNum = -1;
						this.wMashineGun.showWindow();
						this.wCannon.showWindow();
						this.wRocket.showWindow();
						this.wRocketDelay.showWindow();
						this.wBombDelay.showWindow();
						// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
                        this.wBombFuze.showWindow();
						// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
						this.cPlane.hideWindow();
						this.wNumber.showWindow();
						this.cSquadron.showWindow();
						this.sNumberOn.hideWindow();
						
						//TODO: Edited by |ZUTI|: smaller fuel selections
						//-----------------------------------------------------------------------------------------
						selection = ((int)localUserCfg.fuel/10)-1;
						if( selection < 0 ) 
							selection = 0;
						
						ZutiSupportMethods_GUI.setFuelSelectionForAircraft(GUIAirArming.this, zutiSelectedBornPlace, selection);
						//-----------------------------------------------------------------------------------------
						
						localObject1 = (NetUser)NetEnv.host();
						k = ((NetUser)localObject1).getBornPlace();
						localObject3 = (BornPlace)World.cur().bornPlaces.get(k);
						
						//TODO: Added by |ZUTI|
						//----------------------------------------------------------------------------------------------------------
						zutiSelectedBornPlace = (BornPlace)localObject3;
						ArrayList zutiLimitedCountriesList = zutiSelectedBornPlace.zutiHomeBaseCountries;
						//----------------------------------------------------------------------------------------------------------
						
						//TODO: Edited by |ZUTI|
						//if (((BornPlace)localObject3).airNames != null)	//Original line
						ArrayList availablePlanesList = ZutiSupportMethods_Net.getBornPlaceAvailableAircraftList(zutiSelectedBornPlace);
						if( availablePlanesList != null )
						{
							//localObject4 = ((BornPlace)localObject3).airNames;
							for (l = 0; l < availablePlanesList.size(); ++l)
							{
								localObject5 = (String)availablePlanesList.get(l);
								localObject6 = (Class)Property.value(localObject5, "airClass", null);
								if (localObject6 == null)
									continue;
								this.airNames.add(localObject5);
								this.cAircraft.add(I18N.plane((String)localObject5));
							}
						}
						if (this.airNames.size() == 0 && !zutiSelectedBornPlace.zutiEnablePlaneLimits)
						{
							localObject4 = Main.cur().airClasses;
							for (l = 0; l < ((ArrayList)localObject4).size(); ++l)
							{
								localObject5 = (Class)((ArrayList)localObject4).get(l);
								localObject6 = Property.stringValue((Class)localObject5, "keyName");
								if (!(Property.containsValue((Class)localObject5, "cockpitClass")))
									continue;
								this.airNames.add(localObject6);
								this.cAircraft.add(I18N.plane((String)localObject6));
							}
						}
						
						localObject4 = Regiment.getAll();
						TreeMap localTreeMap = new TreeMap();
						i2 = ((List)localObject4).size();
						for (int i3 = 0; i3 < i2; ++i3)
						{
							localObject8 = (Regiment)((List)localObject4).get(i3);
							localObject10 = ((Regiment)localObject8).name();
							
							if (!(this.regHash.containsKey(localObject10)))
							{
								this.regHash.put(localObject10, localObject8);
								localObject12 = (ArrayList)this.regList.get(((Regiment)localObject8).branch());
								if (localObject12 == null)
								{
									String str1 = null;
									try
									{
										str1 = this.resCountry.getString(((Regiment)localObject8).branch());
									}
									catch (Exception localException6)
									{
										break dfight;
									}
									localObject12 = new ArrayList();
									this.regList.put(((Regiment)localObject8).branch(), localObject12);
									localTreeMap.put(str1, ((Regiment)localObject8).branch());
								}
								((ArrayList)localObject12).add(localObject8);
							}
						}
						try
						{
							localObject8 = Main.cur().netFileServerReg.primaryPath();
							localObject10 = new File(HomePath.toFileSystemName((String)localObject8, 0));
							File[] afile = ((File)localObject10).listFiles();
							if (afile != null)
								for (i9 = 0; i9 < afile.length; ++i9)
								{
									File file = afile[i9];
									if (!(file.isFile()))
										continue;
									String str2 = file.getName();
									if (this.regHash.containsKey(str2))
										continue;
									String str3 = str2.toLowerCase();
									if (!(str3.endsWith(".bmp")))
									{
										if (str3.endsWith(".tga"))
											continue;
										if (str3.length() > 123)
											continue;
										int i10 = BmpUtils.squareSizeBMP8Pal(((String)localObject8) + "/" + str3 + ".bmp");
										if ((i10 != 64) && (i10 != 128))
											System.out.println("File " + ((String)localObject8) + "/" + str3 + ".bmp NOT loaded");
										else
											try
											{
												UserRegiment localUserRegiment = new UserRegiment(str2);
												
												this.regHash.put(str2, localUserRegiment);
												ArrayList localArrayList3 = (ArrayList)this.regList.get(localUserRegiment.branch);
												if (localArrayList3 == null)
												{
													String str4 = null;
													try
													{
														str4 = this.resCountry.getString(localUserRegiment.branch);
													}
													catch (Exception localException8)
													{
														break dfight;
													}
													localArrayList3 = new ArrayList();
													this.regList.put(localUserRegiment.branch, localArrayList3);
													localTreeMap.put(str4, localUserRegiment.branch);
												}
												localArrayList3.add(localUserRegiment);
											}
											catch (Exception localException7)
											{
												System.out.println(localException7.getMessage());
												System.out.println("Regiment " + str2 + " NOT loaded");
											}
									}
								}
						}
						catch (Exception localException2)
						{
							System.out.println(localException2.getMessage());
							localException2.printStackTrace();
						}
						
						localObject9 = localTreeMap.keySet().iterator();
						
						while (((Iterator)localObject9).hasNext())
						{
							localObject10 = (String)((Iterator)localObject9).next();
							//Modified by |ZUTI
							//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
							if( zutiLimitedCountriesList == null || zutiLimitedCountriesList.size() <= 0 )
							{
								this.countryLst.add(localTreeMap.get(localObject10));
								this.cCountry.add((String)localObject10);
							}
							else if( zutiLimitedCountriesList.contains( localObject10 ) )
							{
								this.countryLst.add(localTreeMap.get(localObject10));
								this.cCountry.add((String)localObject10);
							}
							//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
						}
						//TODO: Modified by |ZUTI|
						//---------------------------------------------------------
						//Additional check: in unlucky event that there are NO valid countries available (it can happen that some mission
						//has saved countries that are not available in specific installation), add "None" as country option.
						if( this.countryLst.size() == 0 && this.cCountry.size() == 0 )
						{
							zutiSelectedBornPlace.zutiHomeBaseCountries.clear();
							zutiSelectedBornPlace.zutiHomeBaseCountries.add("None");
							this.countryLst.add(localTreeMap.get("None"));
							this.cCountry.add("None");
						}
						//---------------------------------------------------------
						localTreeMap.clear();
						
						this.cCountry.setSelected(0, true, false);
						fillRegiments();
						this.wNumber.setValue("" + localUserCfg.netTacticalNumber, false);
						this.cSquadron.setSelected(localUserCfg.netSquadron, true, false);
						if (localUserCfg.netRegiment != null)
						{
							localObject10 = this.regHash.get(localUserCfg.netRegiment);
							if (localObject10 != null)
							{
								localObject12 = null;
								if (localObject10 instanceof Regiment)
									localObject12 = ((Regiment)localObject10).branch();
								else
									localObject12 = ((UserRegiment)localObject10).branch;
								i9 = 0;
								for (; i9 < this.countryLst.size(); ++i9)
									if (((String)localObject12).equals(this.countryLst.get(i9)))
										break;
								
								if (i9 < this.countryLst.size())
								{
									this.cCountry.setSelected(i9, true, false);
									fillRegiments();
									localArrayList2 = (ArrayList)this.regList.get(this.countryLst.get(i9));
									if (localArrayList2 != null)
										for (i9 = 0; i9 < localArrayList2.size(); ++i9)
											if (localObject10.equals(localArrayList2.get(i9)))
											{
												this.cRegiment.setSelected(i9, true, false);
												break;
											}
								}
							}
							
						}
						
						this.cAircraft.setSelected(-1, false, false);
						try
						{
							for (int i6 = 0; i6 < this.airNames.size(); ++i6)
								if (localUserCfg.netAirName.equals(this.airNames.get(i6)))
								{
									this.cAircraft.setSelected(i6, true, false);
									break;
								}
						}
						catch (Exception localException4)
						{}
						//TODO: Added by |ZUTI|: added this.cAircraft.size() > 0 condition
						if (this.cAircraft.getSelected() < 0 && this.cAircraft.size() > 0)
						{
							this.cAircraft.setSelected(0, true, false);
							localUserCfg.netAirName = ((String)this.airNames.get(0));
						}
						if ((localUserCfg.netRegiment == null) && (this.cRegiment.size() > 0))
						{
							this.cRegiment.setSelected(-1, false, false);
							this.cRegiment.setSelected(0, true, true);
						}
						
						//TODO: Added by |ZUTI|
						//----------------------------------------------------------------
						String s = (String)countryLst.get(cCountry.getSelected());
						ArrayList arraylist = (ArrayList)regList.get(s);
						Object obj = arraylist.get(cRegiment.getSelected());

						try
						{
							//In some cases regiments can fail (user defined ones, for one). Catch that class cast exception...
							localUserCfg.netRegiment = ((Regiment)obj).name();
						}
						catch(ClassCastException ex)
						{
							//... and load None as an valid option!
							if( arraylist.size() <= 0 )
							{
								localUserCfg.netRegiment = "NoNe";
							}
							else
							{
								obj = arraylist.get(0);
								localUserCfg.netRegiment = ((Regiment)obj).name();
							}
						}
						
						int currentFuelSelectionIndex = cFuel.getSelected();
						
						ZutiSupportMethods_GUI.setFuelSelectionForAircraft(GUIAirArming.this, zutiSelectedBornPlace, currentFuelSelectionIndex);
						
						if (bEnableWeaponChange)
						{
							if (isNet())
								localUserCfg.fuel = (cFuel.getSelected() + 1) * 10;
							else if (GUIAirArming.stateId == 4)
								quikFuel = (cFuel.getSelected() + 1) * 10;
							else
								Main.cur().currentMissionFile.set(planeName, "Fuel", (cFuel.getSelected() + 1) * 10);
						}
						//----------------------------------------------------------------
						break;
					}
				case 4 :
					Object localObject11;
					if (this.quikPlayer)
					{
						this.wMashineGun.showWindow();
						this.wCannon.showWindow();
						this.wRocket.showWindow();
						this.wRocketDelay.showWindow();
						this.wBombDelay.showWindow();
						// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
                        this.wBombFuze.showWindow();
						// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
					}
					else
					{
						this.wMashineGun.hideWindow();
						this.wCannon.hideWindow();
						this.wRocket.hideWindow();
						this.wRocketDelay.hideWindow();
						this.wBombDelay.hideWindow();
                        // TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
                        this.wBombFuze.hideWindow();
                        // TODO: --- Bomb Fuze Setting by SAS~Storebror ---
					}
					this.cPlane.showWindow();
					this.wNumber.hideWindow();
					this.cSquadron.hideWindow();
					this.sNumberOn.showWindow();
					this.quikCurPlane = 0;
					this.sNumberOn.setChecked(this.quikNumberOn[this.quikCurPlane], false);
					
					//TODO: Edited by |ZUTI|: smaller fuel selections
					//-----------------------------------------------------------------------------------------
					selection = ((int)this.quikFuel/10)-1;
					if( selection < 0 ) 
						selection = 0;
					
					ZutiSupportMethods_GUI.setFuelSelectionForAircraft(GUIAirArming.this, zutiSelectedBornPlace, selection);
					//-----------------------------------------------------------------------------------------
					
					localObject1 = this.quikListPlane;
					for (k = 0; k < ((ArrayList)localObject1).size(); ++k)
					{
						localObject3 = (Class)((ArrayList)localObject1).get(k);
						localObject4 = Property.stringValue((Class)localObject3, "keyName");
						if ((this.quikPlayer) && (!(Property.containsValue((Class)localObject3, "cockpitClass"))))
							continue;
						this.airNames.add(localObject4);
						this.cAircraft.add(I18N.plane((String)localObject4));
					}
					
					localObject3 = Regiment.getAll();
					localObject4 = new TreeMap();
					int i1 = ((List)localObject3).size();
					for (i2 = 0; i2 < i1; ++i2)
					{
						Regiment regimentMW = (Regiment)((List)localObject3).get(i2);
						if (regimentMW.getArmy() != this.quikArmy)
							continue;
						localObject9 = regimentMW.name();
						
						if (!(this.regHash.containsKey(localObject9)))
						{
							this.regHash.put(localObject9, regimentMW);
							localObject11 = (ArrayList)this.regList.get(regimentMW.branch());
							unknownLabel :
							{
								if (localObject11 == null)
								{
									localObject12 = null;
									try
									{
										localObject12 = this.resCountry.getString(regimentMW.branch());
									}
									catch (Exception localException5)
									{
										break unknownLabel;
									}
									localObject11 = new ArrayList();
									this.regList.put(regimentMW.branch(), localObject11);
									((TreeMap)localObject4).put(localObject12, regimentMW.branch());
								}
							}
							((ArrayList)localObject11).add(regimentMW);
						}
					}
					
					Object localObject7 = ((TreeMap)localObject4).keySet().iterator();
					while (((Iterator)localObject7).hasNext())
					{
						localObject9 = (String)((Iterator)localObject7).next();
						this.countryLst.add(((TreeMap)localObject4).get(localObject9));
						this.cCountry.add((String)localObject9);
					}
					((TreeMap)localObject4).clear();
					
					this.cCountry.setSelected(0, true, false);
					fillRegiments();
					
					if (this.quikRegiment != null)
					{
						localObject9 = this.regHash.get(this.quikRegiment);
						if (localObject9 != null)
						{
							localObject11 = ((Regiment)localObject9).branch();
							int i8 = 0;
							for (; i8 < this.countryLst.size(); ++i8)
								if (((String)localObject11).equals(this.countryLst.get(i8)))
									break;
							
							if (i8 < this.countryLst.size())
							{
								this.cCountry.setSelected(i8, true, false);
								fillRegiments();
								ArrayList localArrayList1 = (ArrayList)this.regList.get(this.countryLst.get(i8));
								if (localArrayList1 != null)
									for (i8 = 0; i8 < localArrayList1.size(); ++i8)
										if (localObject9.equals(localArrayList1.get(i8)))
										{
											this.cRegiment.setSelected(i8, true, false);
											break;
										}
							}
						}
						
					}
					
					this.cAircraft.setSelected(-1, false, false);
					try
					{
						for (int i4 = 0; i4 < this.airNames.size(); ++i4)
							if (this.quikPlane.equals(this.airNames.get(i4)))
							{
								this.cAircraft.setSelected(i4, true, false);
								break;
							}
					}
					catch (Exception localException3)
					{}
					if (this.cAircraft.getSelected() < 0)
					{
						this.cAircraft.setSelected(0, true, false);
						this.quikPlane = ((String)this.airNames.get(0));
					}
					if ((this.quikRegiment == null) && (this.cRegiment.size() > 0))
					{
						this.cRegiment.setSelected(-1, false, false);
						this.cRegiment.setSelected(0, true, true);
					}
					this.cPlane.clear(false);
					for (int i5 = 0; i5 < this.quikPlanes; ++i5)
						this.cPlane.add(" " + (i5 + 1));
					this.cPlane.setSelected(this.quikCurPlane, true, false);
			}
			
			//TODO: Added by |ZUTI|
			//-----------------------------------------------------------------------------------------------------
			if( zutiSelectedBornPlace != null && zutiSelectedBornPlace.zutiEnablePlaneLimits )
				ZutiSupportMethods_GUI.fillWeaponsListBasedOnBornPlace(this, zutiSelectedBornPlace);
			else
				fillWeapons();
			//System.out.println("GUIAirArming - Loading weapons 2");
			//-----------------------------------------------------------------------------------------------------
			selectWeapon();
			
			fillSkins();
			selectSkin();
			
			fillPilots();
			selectPilot();
			
			fillNoseart();
			selectNoseart();
			
			setMesh();
			prepareMesh();
			prepareWeapons();
			prepareSkin();
			preparePilot();
			prepareNoseart();
		}
		catch (Exception localException1)
		{
			System.out.println(localException1.getMessage());
			localException1.printStackTrace();
			Main.stateStack().pop();
			return;
		}
		
		this.dialogClient.setPosSize();
		this.client.activateWindow();
	}
	
	public void _leave()
	{
		World.cur().setUserCovers();
		client.hideWindow();
	}
	
	private void fillRegiments()
	{
		if (stateId != 2 && stateId != 4)
			return;
		cRegiment.clear();
		int i = cCountry.getSelected();
		if (i < 0)
			return;
		String s = (String)countryLst.get(i);
		ArrayList arraylist = (ArrayList)regList.get(s);
		if (arraylist.size() > 0)
		{
			for (int j = 0; j < arraylist.size(); j++)
			{
				Object obj = arraylist.get(j);
				if (obj instanceof Regiment)
					cRegiment.add(((Regiment)obj).shortInfo());
				else
					cRegiment.add(((UserRegiment)obj).shortInfo);
			}
			
			cRegiment.setSelected(0, true, false);
		}
	}
	
	//TODO: Modified by |ZUTI|: from private to protected
	protected void fillWeapons()
	{
		cWeapon.clear();
		weaponNames.clear();
		int i = cAircraft.getSelected();
		if (i < 0)
			return;
		Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
		String as[] = Aircraft.getWeaponsRegistered(class1);
		if (as != null && as.length > 0)
		{
			String s = (String)airNames.get(i);
			for (int j = 0; j < as.length; j++)
			{
				String s1 = as[j];
				if (!bEnableWeaponChange)
				{
					String s2 = Main.cur().currentMissionFile.get(planeName, "weapons", (String)null);
					if (!s1.equals(s2))
						continue;
				}
				weaponNames.add(s1);
				//System.out.println("GUIAirArming - " + s1);
				cWeapon.add(I18N.weapons(s, s1));
			}
			
			if (weaponNames.size() == 0)
			{
				weaponNames.add(as[0]);
				cWeapon.add(I18N.weapons(s, as[0]));
			}
			cWeapon.setSelected(0, true, false);
		}
	}
	
	private void selectWeapon()
	{
		if (bEnableWeaponChange)
		{
			UserCfg usercfg = World.cur().userCfg;
			String s = null;
			if (isNet())
				s = usercfg.getWeapon(airName());
			else if (stateId == 4)
				s = quikWeapon;
			else
				s = Main.cur().currentMissionFile.get(planeName, "weapons", (String)null);
			cWeapon.setSelected(-1, false, false);
			for (int i = 0; i < weaponNames.size(); i++)
			{
				String s1 = (String)weaponNames.get(i);
				if (!s1.equals(s))
					continue;
				cWeapon.setSelected(i, true, false);
				break;
			}
			
			if (cWeapon.getSelected() < 0)
			{
				cWeapon.setSelected(0, true, false);
				if (isNet())
					usercfg.setWeapon(airName(), (String)weaponNames.get(0));
				else if (stateId == 4)
					quikWeapon = (String)weaponNames.get(0);
				else
					Main.cur().currentMissionFile.set(planeName, "weapons", (String)weaponNames.get(0));
			}
		}
		else
		{
			cWeapon.setSelected(0, true, false);
		}
	}
	
	public static String validateFileName(String s)
	{
		if (s.indexOf('\\') >= 0)
			s = s.replace('\\', '_');
		if (s.indexOf('/') >= 0)
			s = s.replace('/', '_');
		if (s.indexOf('?') >= 0)
			s = s.replace('?', '_');
		return s;
	}
	
	private void fillSkins()
	{
		cSkin.clear();
		cSkin.add(i18n("neta.Default"));
		try
		{
			int i = cAircraft.getSelected();
			String s = Main.cur().netFileServerSkin.primaryPath();
			String s1 = validateFileName((String)airNames.get(i));
			File file = new File(HomePath.toFileSystemName(s + "/" + s1, 0));
			File afile[] = file.listFiles();
			if (afile != null)
			{
				for (int j = 0; j < afile.length; j++)
				{
					File file1 = afile[j];
					if (file1.isFile())
					{
						String s2 = file1.getName();
						String s3 = s2.toLowerCase();
						if (s3.endsWith(".bmp") && s3.length() + s1.length() <= 122)
						{
							int k = BmpUtils.squareSizeBMP8Pal(s + "/" + s1 + "/" + s2);
							if (k == 512 || k == 1024)
								cSkin.add(s2);
							else
								System.out.println("Skin " + s + "/" + s1 + "/" + s2 + " NOT loaded");
						}
					}
				}
				
			}
		}
		catch (Exception exception)
		{
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		cSkin.setSelected(0, true, false);
	}
	
	private void selectSkin()
	{
		UserCfg usercfg = World.cur().userCfg;
		cSkin.setSelected(-1, false, false);
		String s = usercfg.getSkin(airName());
		if (stateId == 4)
			s = quikSkin[quikCurPlane];
		for (int i = 1; i < cSkin.size(); i++)
		{
			String s1 = cSkin.get(i);
			if (!s1.equals(s))
				continue;
			cSkin.setSelected(i, true, false);
			break;
		}
		
		if (cSkin.getSelected() < 0)
		{
			cSkin.setSelected(0, true, false);
			if (stateId == 4)
				quikSkin[quikCurPlane] = null;
			else
				usercfg.setSkin(airName(), null);
		}
	}
	
	private void fillPilots()
	{
		cPilot.clear();
		cPilot.add(i18n("neta.Default"));
		try
		{
			String s = Main.cur().netFileServerPilot.primaryPath();
			File file = new File(HomePath.toFileSystemName(s, 0));
			File afile[] = file.listFiles();
			if (afile != null)
			{
				for (int i = 0; i < afile.length; i++)
				{
					File file1 = afile[i];
					if (file1.isFile())
					{
						String s1 = file1.getName();
						String s2 = s1.toLowerCase();
						if (s2.endsWith(".bmp") && s2.length() <= 122)
							if (BmpUtils.checkBMP8Pal(s + "/" + s1, 256, 256))
								cPilot.add(s1);
							else
								System.out.println("Pilot " + s + "/" + s1 + " NOT loaded");
					}
				}
				
			}
		}
		catch (Exception exception)
		{
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		cPilot.setSelected(0, true, false);
	}
	
	private void selectPilot()
	{
		UserCfg usercfg = World.cur().userCfg;
		cPilot.setSelected(-1, false, false);
		String s = usercfg.netPilot;
		if (stateId == 4)
			s = quikPilot[quikCurPlane];
		for (int i = 1; i < cPilot.size(); i++)
		{
			String s1 = cPilot.get(i);
			if (!s1.equals(s))
				continue;
			cPilot.setSelected(i, true, false);
			break;
		}
		
		if (cPilot.getSelected() < 0)
		{
			cPilot.setSelected(0, true, false);
			if (stateId == 4)
				quikPilot[quikCurPlane] = null;
			else
				usercfg.netPilot = null;
		}
	}
	
	private void fillNoseart()
	{
		cNoseart.clear();
		cNoseart.add(i18n("neta.None"));
		try
		{
			String s = Main.cur().netFileServerNoseart.primaryPath();
			File file = new File(HomePath.toFileSystemName(s, 0));
			File afile[] = file.listFiles();
			if (afile != null)
			{
				for (int i = 0; i < afile.length; i++)
				{
					File file1 = afile[i];
					if (file1.isFile())
					{
						String s1 = file1.getName();
						String s2 = s1.toLowerCase();
						if (s2.endsWith(".bmp") && s2.length() <= 122)
							if (BmpUtils.checkBMP8Pal(s + "/" + s1, 256, 512))
								cNoseart.add(s1);
							else
								System.out.println("Noseart " + s + "/" + s1 + " NOT loaded");
					}
				}
				
			}
		}
		catch (Exception exception)
		{
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
		cNoseart.setSelected(0, true, false);
	}
	
	private void selectNoseart()
	{
		UserCfg usercfg = World.cur().userCfg;
		cNoseart.setSelected(-1, false, false);
		boolean flag = true;
		int i = cAircraft.getSelected();
		if (i < 0)
		{
			flag = false;
		}
		else
		{
			Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
			flag = Property.intValue(class1, "noseart", 0) == 1;
			if (flag)
			{
				int j = cCountry.getSelected();
				if (j < 0)
				{
					flag = false;
				}
				else
				{
					String s1 = (String)countryLst.get(j);
					String s3 = Regiment.getCountryFromBranch(s1);
					flag = "us".equals(s3);
				}
			}
		}
		if (flag)
		{
			String s = usercfg.getNoseart(airName());
			if (stateId == 4)
				s = quikNoseart[quikCurPlane];
			for (int k = 1; k < cNoseart.size(); k++)
			{
				String s2 = cNoseart.get(k);
				if (!s2.equals(s))
					continue;
				cNoseart.setSelected(k, true, false);
				break;
			}
			
//			cNoseart.showWindow();
            cNoseart.setEnable(true);
		}
		else
		{
//			cNoseart.hideWindow();
            cNoseart.setEnable(false);
		}
		if (cNoseart.getSelected() < 0)
		{
			cNoseart.setSelected(0, true, false);
			if (stateId == 4)
				quikNoseart[quikCurPlane] = null;
			else
				usercfg.setNoseart(airName(), null);
		}
	}
	
	private void createRender()
	{
		renders = new GUIRenders(dialogClient)
		{
			
			public void mouseButton(int i, boolean flag, float f, float f1)
			{
				super.mouseButton(i, flag, f, f1);
				if (!flag)
					return;
				if (i == 1)
				{
					animateMeshA = animateMeshT = 0.0F;
					if (Actor.isValid(actorMesh))
						actorMesh.pos.setAbs(new Orient(90F, 0.0F, 0.0F));
				}
				else if (i == 0)
				{
					f -= win.dx / 2.0F;
					if (Math.abs(f) < win.dx / 16F)
						animateMeshA = 0.0F;
					else
						animateMeshA = (-128F * f) / win.dx;
					f1 -= win.dy / 2.0F;
					if (Math.abs(f1) < win.dy / 16F)
						animateMeshT = 0.0F;
					else
						animateMeshT = (-128F * f1) / win.dy;
				}
			}
			
		};
		camera3D = new Camera3D();
		camera3D.set(50F, 1.0F, 100F);
		render3D = new _Render3D(renders.renders, 1.0F);
		render3D.setCamera(camera3D);
		LightEnvXY lightenvxy = new LightEnvXY();
		render3D.setLightEnv(lightenvxy);
		lightenvxy.sun().setLight(0.5F, 0.5F, 1.0F, 1.0F, 1.0F, 0.8F);
		Vector3f vector3f = new Vector3f(-2F, 1.0F, -1F);
		vector3f.normalize();
		lightenvxy.sun().set(vector3f);
	}
	
	private void setMesh()
	{
		destroyMesh();
		int i = cAircraft.getSelected();
		if (i < 0)
			return;
		try
		{
			Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
			String s = (String)countryLst.get(cCountry.getSelected());
			String s1 = Regiment.getCountryFromBranch(s);
			String s2 = Aircraft.getPropertyMesh(class1, s1);
			actorMesh = new ActorSimpleHMesh(s2);
			double d = actorMesh.hierMesh().visibilityR();
			Aircraft.prepareMeshCamouflage(s2, actorMesh.hierMesh());
			actorMesh.pos.setAbs(new Orient(90F, 0.0F, 0.0F));
			actorMesh.pos.reset();
			d *= Math.cos(0.26179938779914941D) / Math.sin(((double)camera3D.FOV() * 3.1415926535897931D) / 180D / 2D);
			camera3D.pos.setAbs(new Point3d(d, 0.0D, 0.0D), new Orient(180F, 0.0F, 0.0F));
			camera3D.pos.reset();
		}
		catch (Exception exception)
		{
			System.out.println(exception.getMessage());
			exception.printStackTrace();
		}
	}
	
	private void destroyMesh()
	{
		if (Actor.isValid(actorMesh))
			actorMesh.destroy();
		actorMesh = null;
		destroyWeaponMeshs();
	}
	
	private void destroyWeaponMeshs()
	{
		for (int i = 0; i < weaponMeshs.size(); i++)
		{
			ActorMesh actormesh = (ActorMesh)weaponMeshs.get(i);
			if (Actor.isValid(actormesh))
				actormesh.destroy();
		}
		
		weaponMeshs.clear();
	}
	
	private void prepareMesh()
	{
		if (!Actor.isValid(actorMesh))
			return;
		int i = cAircraft.getSelected();
		if (i < 0)
			return;
		switch (stateId)
		{
			default :
				break;
			
			case 3 : // '\003'
				Class class1 = GUINetAircraft.selectedAircraftClass();
				Regiment regiment = GUINetAircraft.selectedRegiment();
				String s = regiment.country();
				String s2 = GUINetAircraft.selectedWingName();
				PaintScheme paintscheme1 = Aircraft.getPropertyPaintScheme(class1, s);
				if (paintscheme1 == null)
					return;
				int l = s2.charAt(s2.length() - 2) - 48;
				int i1 = s2.charAt(s2.length() - 1) - 48;
				int j1 = GUINetAircraft.selectedAircraftNumInWing();
				UserCfg usercfg1 = World.cur().userCfg;
				paintscheme1.prepare(class1, actorMesh.hierMesh(), regiment, l, i1, j1, usercfg1.netNumberOn);
				break;
			
			case 0 : // '\0'
			case 1 : // '\001'
			case 2 : // '\002'
				Class class2 = (Class)Property.value(airNames.get(i), "airClass", null);
				PaintScheme paintscheme = Aircraft.getPropertyPaintScheme(class2, (String)countryLst.get(cCountry.getSelected()));
				if (paintscheme == null)
					return;
				int k = cCountry.getSelected();
				if (k < 0)
					return;
				String s3 = (String)countryLst.get(k);
				ArrayList arraylist1 = (ArrayList)regList.get(s3);
				if (arraylist1 == null)
					return;
				Object obj = arraylist1.get(cRegiment.getSelected());
				Object obj1 = null;
				if (obj instanceof Regiment)
				{
					obj1 = (Regiment)obj;
					NetUserRegiment netuserregiment = ((NetUser)NetEnv.host()).netUserRegiment;
					((NetUser)NetEnv.host()).setUserRegiment(netuserregiment.branch(), "", netuserregiment.aid(), netuserregiment.gruppeNumber());
				}
				else
				{
					UserRegiment userregiment = (UserRegiment)obj;
					((NetUser)NetEnv.host()).setUserRegiment(userregiment.country, userregiment.fileName + ".bmp", userregiment.id, userregiment.gruppeNumber);
					obj1 = ((NetUser)NetEnv.host()).netUserRegiment;
				}
				if (obj1 == null)
					return;
				if (isNet())
				{
					if (cSquadron.getSelected() < 0)
						return;
					UserCfg usercfg = World.cur().userCfg;
					boolean flag = usercfg.netNumberOn;
					if (stateId == 2)
						flag = true;
					paintscheme.prepareNum(class2, actorMesh.hierMesh(), ((Regiment)(obj1)), cSquadron.getSelected(), 0, usercfg.netTacticalNumber, flag);
				}
				else
				{
					int k1 = planeName.charAt(planeName.length() - 2) - 48;
					int l1 = planeName.charAt(planeName.length() - 1) - 48;
					int i2 = Main.cur().currentMissionFile.get("Main", "playerNum", 0);
					UserCfg usercfg2 = World.cur().userCfg;
					paintscheme.prepare(class2, actorMesh.hierMesh(), ((Regiment)(obj1)), k1, l1, i2, usercfg2.netNumberOn);
				}
				break;
			
			case 4 : // '\004'
				Class class3 = (Class)Property.value(airNames.get(i), "airClass", null);
				int j = cCountry.getSelected();
				if (j < 0)
					return;
				String s1 = (String)countryLst.get(j);
				ArrayList arraylist = (ArrayList)regList.get(s1);
				if (arraylist == null)
					return;
				Regiment regiment1 = (Regiment)arraylist.get(cRegiment.getSelected());
				if (regiment1 == null)
					return;
				PaintScheme paintscheme2 = Aircraft.getPropertyPaintScheme(class3, regiment1.country());
				if (paintscheme2 == null)
					return;
				paintscheme2.prepare(class3, actorMesh.hierMesh(), regiment1, quikSquadron, quikWing, quikCurPlane, quikNumberOn[quikCurPlane]);
				break;
		}
	}
	
	private void prepareWeapons()
	{
		destroyWeaponMeshs();
		if (!Actor.isValid(actorMesh))
			return;
		int i = cAircraft.getSelected();
		if (i < 0)
			return;
		Class class1 = (Class)Property.value(airNames.get(i), "airClass", null);
		
		//TODO: Added by |ZUTI|
		//-----------------------------------------------------------------------------
		String as[] = ZutiSupportMethods_GUI.syncWeaponsLists(this, Aircraft.getWeaponsRegistered(class1) );
		//-----------------------------------------------------------------------------
		
		//String as[] = Aircraft.getWeaponsRegistered(class1);
		if (as == null || as.length == 0)
			return;
		i = cWeapon.getSelected();
		if (i < 0 || i >= as.length)
			return;
		String as1[] = Aircraft.getWeaponHooksRegistered(class1);
		com.maddox.il2.objects.air.Aircraft._WeaponSlot a_lweaponslot[] = Aircraft.getWeaponSlotsRegistered(class1, as[i]);
		if (as1 == null || a_lweaponslot == null)
			return;
		for (int j = 0; j < as1.length; j++)
			if (as1[j] != null && a_lweaponslot[j] != null)
			{
				Class class2 = a_lweaponslot[j].clazz;
				if (!(com.maddox.il2.objects.weapons.BombGun.class).isAssignableFrom(class2) || Property.containsValue(class2, "external"))
				{
					String s = Property.stringValue(class2, "mesh", null);
					if (s == null)
					{
						Class class3 = (Class)Property.value(class2, "bulletClass", null);
						if (class3 != null)
							s = Property.stringValue(class3, "mesh", null);
					}
					if (s != null)
						try
						{
							ActorSimpleMesh actorsimplemesh = new ActorSimpleMesh(s);
							actorsimplemesh.pos.setBase(actorMesh, new HookNamed(actorMesh, as1[j]), false);
							actorsimplemesh.pos.changeHookToRel();
							actorsimplemesh.pos.resetAsBase();
							weaponMeshs.add(actorsimplemesh);
						}
						catch (Exception exception)
						{
							System.out.println(exception.getMessage());
							exception.printStackTrace();
						}
				}
			}
		
	}
	
	private void prepareSkin()
	{
		int i = cSkin.getSelected();
		if (i < 0)
			return;
		Class class1 = (Class)Property.value(airNames.get(cAircraft.getSelected()), "airClass", null);
		String s = (String)countryLst.get(cCountry.getSelected());
		String s1 = Regiment.getCountryFromBranch(s);
		String s2 = Aircraft.getPropertyMesh(class1, s1);
		if (i == 0)
		{
			((NetUser)NetEnv.host()).setSkin(null);
			Aircraft.prepareMeshCamouflage(s2, actorMesh.hierMesh());
		}
		else
		{
			String s3 = validateFileName(airName());
			String s4 = s3 + "/" + cSkin.get(i);
			String s5 = s2;
			int j = s5.lastIndexOf('/');
			if (j >= 0)
				s5 = s5.substring(0, j + 1) + "summer";
			else
				s5 = s5 + "summer";
			String s6 = "PaintSchemes/Cache/" + s3;
			try
			{
				File file = new File(HomePath.toFileSystemName(s6, 0));
				if (!file.isDirectory())
				{
					file.mkdir();
				}
				else
				{
					File afile[] = file.listFiles();
					if (afile != null)
					{
						for (int k = 0; k < afile.length; k++)
							if (afile[k] != null)
							{
								String s8 = afile[k].getName();
								if (s8.regionMatches(true, s8.length() - 4, ".tg", 0, 3))
									afile[k].delete();
							}
						
					}
				}
			}
			catch (Exception exception)
			{
				return;
			}
			String s7 = Main.cur().netFileServerSkin.primaryPath();
			if (!BmpUtils.bmp8PalTo4TGA4(s7 + "/" + s4, s5, s6))
				return;
			Aircraft.prepareMeshCamouflage(s2, actorMesh.hierMesh(), s6);
			((NetUser)NetEnv.host()).setSkin(s4);
		}
	}
	
	private void preparePilot()
	{
		int i = cPilot.getSelected();
		if (i < 0)
			return;
		if (i == 0)
		{
			Class class1 = (Class)Property.value(airNames.get(cAircraft.getSelected()), "airClass", null);
			String s1 = (String)countryLst.get(cCountry.getSelected());
			String s3 = Regiment.getCountryFromBranch(s1);
			String s5 = Aircraft.getPropertyMesh(class1, s3);
			String s7 = HomePath.concatNames(s5, "pilot1.mat");
			Aircraft.prepareMeshPilot(actorMesh.hierMesh(), 0, s7, "3do/plane/textures/pilot1.tga");
			((NetUser)NetEnv.host()).setPilot(null);
		}
		else
		{
			String s = Main.cur().netFileServerPilot.primaryPath();
			String s2 = cPilot.get(i);
			String s4 = s2.substring(0, s2.length() - 4);
			String s6 = "PaintSchemes/Cache/Pilot" + s4 + ".mat";
			String s8 = "PaintSchemes/Cache/Pilot" + s4 + ".tga";
			if (!BmpUtils.bmp8PalToTGA3(s + "/" + s2, s8))
				return;
			Aircraft.prepareMeshPilot(actorMesh.hierMesh(), 0, s6, s8);
			((NetUser)NetEnv.host()).setPilot(s2);
		}
	}
	
	private void prepareNoseart()
	{
		int i = cNoseart.getSelected();
		if (i > 0)
		{
			String s = Main.cur().netFileServerNoseart.primaryPath();
			String s1 = cNoseart.get(i);
			String s2 = s1.substring(0, s1.length() - 4);
			String s3 = "PaintSchemes/Cache/Noseart0" + s2 + ".mat";
			String s4 = "PaintSchemes/Cache/Noseart0" + s2 + ".tga";
			String s5 = "PaintSchemes/Cache/Noseart1" + s2 + ".mat";
			String s6 = "PaintSchemes/Cache/Noseart1" + s2 + ".tga";
			if (!BmpUtils.bmp8PalTo2TGA4(s + "/" + s1, s4, s6))
				return;
			Aircraft.prepareMeshNoseart(actorMesh.hierMesh(), s3, s5, s4, s6);
			((NetUser)NetEnv.host()).setNoseart(s1);
		}
	}
	
	private float clampValue(GWindowEditControl gwindoweditcontrol, float f, float f1, float f2)
	{
		String s = gwindoweditcontrol.getValue();
		try
		{
			f = Float.parseFloat(s);
		}
		catch (Exception exception)
		{}
		if (f < f1)
			f = f1;
		if (f > f2)
			f = f2;
		gwindoweditcontrol.setValue("" + f, false);
		return f;
	}
	
	public GUIAirArming(GWindowRoot gwindowroot)
	{
		super(55);
		airNames = new ArrayList();
		weaponNames = new ArrayList();
		regList = new HashMapExt();
		regHash = new HashMapExt();
		countryLst = new ArrayList();
		bEnableWeaponChange = true;
		quikPlayer = true;
		quikArmy = 1;
		quikPlanes = 4;
		quikPlane = "Il-2_M3";
		quikWeapon = "default";
		quikCurPlane = 0;
		quikRegiment = "r01";
		quikSquadron = 0;
		quikWing = 0;
		quikFuel = 100;
		quikListPlane = new ArrayList();
		playerNum = -1;
		weaponMeshs = new ArrayList();
		animateMeshA = 0.0F;
		animateMeshT = 0.0F;
		_orient = new Orient();
		client = (GUIClient)gwindowroot.create(new GUIClient());
		dialogClient = (DialogClient)client.create(new DialogClient());
		infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
		infoMenu.info = i18n("neta.info");
		infoName = (GUIInfoName)client.create(new GUIInfoName());
		com.maddox.gwindow.GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
		wMashineGun = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
		wMashineGun.bNumericOnly = wMashineGun.bNumericFloat = true;
		wMashineGun.bDelayedNotify = true;
		wCannon = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
		wCannon.bNumericOnly = wCannon.bNumericFloat = true;
		wCannon.bDelayedNotify = true;
		wRocket = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
		wRocket.bNumericOnly = wRocket.bNumericFloat = true;
		wRocket.bDelayedNotify = true;
		wRocketDelay = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
		wRocketDelay.bNumericOnly = wRocketDelay.bNumericFloat = true;
		wRocketDelay.bDelayedNotify = true;
		wBombDelay = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
		wBombDelay.bNumericOnly = wBombDelay.bNumericFloat = true;
		wBombDelay.bDelayedNotify = true;
		// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
        wBombFuze = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null));
        wBombFuze.bNumericOnly = wBombFuze.bNumericFloat = true;
        wBombFuze.bDelayedNotify = true;
		// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
		wNumber = (GWindowEditControl)dialogClient.addControl(new GWindowEditControl(dialogClient, 0.0F, 0.0F, 1.0F, 2.0F, null)
		{
			
			public void keyboardKey(int i, boolean flag)
			{
				super.keyboardKey(i, flag);
				if (i == 10 && flag)
					notify(2, 0);
			}
			
		});
		wNumber.bNumericOnly = true;
		wNumber.bDelayedNotify = true;
		wNumber.align = 1;
		sNumberOn = (GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
		cFuel = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cFuel.setEditable(false);
		cFuel.add("10");
		cFuel.add("20");
		cFuel.add("30");
		cFuel.add("40");
		cFuel.add("50");
		cFuel.add("60");
		cFuel.add("70");
		cFuel.add("80");
		cFuel.add("90");
		cFuel.add("100");
		cAircraft = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cAircraft.setEditable(false);
		cAircraft.listVisibleLines = 24;// MW Increased the size of the drop down list
		cWeapon = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cWeapon.setEditable(false);
		cCountry = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cCountry.setEditable(false);
		cRegiment = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cRegiment.setEditable(false);
		cSkin = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cSkin.setEditable(false);
		cPilot = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cPilot.setEditable(false);
		cSquadron = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cSquadron.setEditable(false);
		cSquadron.editBox.align = cSquadron.align = 1;
		cSquadron.add("1");
		cSquadron.add("2");
		cSquadron.add("3");
		cSquadron.add("4");
		cPlane = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cPlane.setEditable(false);
		cNoseart = (GWindowComboControl)dialogClient.addControl(new GWindowComboControl(dialogClient, 0.0F, 0.0F, 1.0F));
		cNoseart.setEditable(false);
		bBack = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96F, 48F, 48F));
		// MW Modified BEGIN: New button init
		bJoy = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient,
		gtexture, 0.0F, 48F, 48F, 48F));
		// MW Modified END
		createRender();
		dialogClient.activateWindow();
		client.hideWindow();
		
		//TODO: Added by |ZUTI|
		sZutiMultiCrew = (com.maddox.il2.gui.GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
		sZutiMultiCrewAnytime = (com.maddox.il2.gui.GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient));
	}
	
	//TODO: |ZUTI| variables and methods
	//------------------------------------------------------------------------
	private BornPlace zutiSelectedBornPlace = null;
	public GUISwitchBox3 sZutiMultiCrew;
	public GUISwitchBox3 sZutiMultiCrewAnytime;
	//------------------------------------------------------------------------
	// TODO: +++ Bomb Fuze Setting by SAS~Storebror +++
	private static String bombFuzeCaption = "Bomb Fuze";
	// TODO: --- Bomb Fuze Setting by SAS~Storebror ---
}