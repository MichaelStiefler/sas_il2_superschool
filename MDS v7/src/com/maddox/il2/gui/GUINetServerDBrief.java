/*
 * GUINetServerDBrief - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package com.maddox.il2.gui;

import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.Army;
import com.maddox.il2.ai.UserCfg;
import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.ZutiSupportMethods_Multicrew;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.USGS;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.il2.objects.effects.ForceFeedback;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HotKeyCmd;
import com.maddox.rts.NetEnv;
import com.maddox.rts.Property;
import com.maddox.sound.AudioDevice;

public class GUINetServerDBrief extends GUIBriefing
{
	public void enter(GameState gamestate)
	{
		super.enter(gamestate);
		if (gamestate != null && (gamestate.id() == 42 || gamestate.id() == 38) && briefSound != null)
		{
			String string = Main.cur().currentMissionFile.get("MAIN", ("briefSound" + ((NetUser) NetEnv.host()).getArmy()));
			if (string != null)
				briefSound = string;
			CmdEnv.top().exec("music PUSH");
			CmdEnv.top().exec("music LIST " + briefSound);
			CmdEnv.top().exec("music PLAY");
		}
	}

	public void leave(GameState gamestate)
	{
		if (gamestate != null && gamestate.id() == 42 && briefSound != null)
		{
			CmdEnv.top().exec("music POP");
			CmdEnv.top().exec("music STOP");
			briefSound = null;
		}
		super.leave(gamestate);
	}

	public void leavePop(GameState gamestate)
	{
		if (gamestate != null && gamestate.id() == 2 && briefSound != null)
		{
			CmdEnv.top().exec("music POP");
			CmdEnv.top().exec("music PLAY");
		}
		super.leavePop(gamestate);
	}

	protected void fillTextDescription()
	{
		super.fillTextDescription();
		prepareTextDescription(Army.amountNet());
	}

	protected String textDescription()
	{
		if (textArmyDescription == null)
			return null;
		NetUser netuser = (NetUser) NetEnv.host();
		int i = netuser.getBornPlace();
		if (i < 0)
			return textArmyDescription[0];
		BornPlace bornplace = (BornPlace) World.cur().bornPlaces.get(i);
		return textArmyDescription[bornplace.army];
	}

	private boolean isValidBornPlace()
	{
		NetUser netuser = (NetUser) NetEnv.host();
		int i = netuser.getBornPlace();
		if (i < 0 || i >= World.cur().bornPlaces.size())
		{
			new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("brief.BornPlace"), i18n("brief.BornPlaceSelect"), 3, 0.0F);
			return false;
		}
		int i_0_ = netuser.getAirdromeStay();
		if (i_0_ < 0)
		{
			new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("brief.StayPlace"), i18n("brief.StayPlaceWait"), 3, 0.0F);
			return false;
		}
		return true;
	}

	private boolean isValidAircraftForServer()
	{
		if (Main.cur().netServerParams == null)
			return true;
		if (!Main.cur().netServerParams.isMaster())
			return true;
		NetUser netuser = (NetUser) NetEnv.host();
		if (netuser == null)
			return true;
		do
		{
			boolean bool;
			try
			{
				UserCfg usercfg = World.cur().userCfg;
				netuser.checkReplicateSkin(usercfg.netAirName);				
				bool = true;
			}
			catch (Exception exception)
			{
				System.out.println("isValidAircraftForServer exception: " + exception.toString());
				break;
			}
			return bool;
		}
		while (false);
		return false;
	}

	protected void doNext()
	{
		//TODO: Modified by |ZUTI|: check if aircraft is available
		int result = ZutiSupportMethods_GUI.isValidArming();
		if (result != -1)
		{
			String strReason = "";
			switch (result)
			{
				case 0:
					strReason = i18n("mds.info.various");
				break;
				case 1:
					strReason = i18n("mds.info.fuel");
				break;
				case 2:
					strReason = i18n("mds.info.weapons");
				break;
				case 3:
					strReason = i18n("mds.info.country");
				break;
				case 4:
					strReason = i18n("mds.info.homeBase");
				break;
			}
			if( result != 4 )
				Main.stateStack().push(55);
			new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("mds.info.bornPlace"), i18n("mds.info.selectValidOptions") + " - " + strReason, 3, 0.0F);
			return;
		}		
		//Request AC
		ZutiSupportMethods_GUI.SERVER_BRIEFING_SCREEN = this;
		String acName = ZutiSupportMethods_Air.getAircraftI18NName((Class) Property.value(World.cur().userCfg.netAirName, "airClass", null));
		ZutiSupportMethods_NetSend.requestAircraft(acName);
	}
	
	protected void doNext_original()
	{
		//Reset that object
		ZutiSupportMethods_GUI.SERVER_BRIEFING_SCREEN = null;
		
		if (isValidBornPlace())
		{
			//TODO: Added by |ZUTI|: if the deck is full but we have airspawn enabled in that case, airstart
			//----------------------------------------------------------------------------------------------
			BornPlace bornPlace = null;
			NetUser netuser = (NetUser) NetEnv.host();
			if( netuser != null )
			{
				int i = netuser.getBornPlace();
				if (i >= 0 && i < World.cur().bornPlaces.size())
					bornPlace = (BornPlace)World.cur().bornPlaces.get(i);
			}
			boolean isCarrierDeckFree = isCarrierDeckFree(netuser);
			//----------------------------------------------------------------------------------------------
			
			if (!isValidAircraftForServer())
			{
				GUIAirArming.stateId = 2;
				Main.stateStack().push(55);
			}
			//TODO: Modified by |ZUTI|: also checking for airspawn flag in case the deck if full.
			else if (!isCarrierDeckFree && bornPlace != null && !bornPlace.zutiAirspawnIfQueueFull )
				new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("brief.CarrierDeckFull"), i18n("brief.CarrierDeckFullWait"), 3, 0.0F);
			
			else
			{
				//TODO: Added by |ZUTI|: for airspawns
				boolean airstart = false;
				if( !isCarrierDeckFree && bornPlace != null && bornPlace.zutiAirspawnIfQueueFull )
					airstart = true;
				
				Main.cur().resetUser();
				int i = netuser.getBornPlace();
				int i_1_ = netuser.getAirdromeStay();
				UserCfg usercfg = World.cur().userCfg;
				netuser.checkReplicateSkin(usercfg.netAirName);
				netuser.checkReplicateNoseart(usercfg.netAirName);
				netuser.checkReplicatePilot();
				String string;
				if (usercfg.netTacticalNumber < 10)
					string = "0" + usercfg.netTacticalNumber;
				else
					string = "" + usercfg.netTacticalNumber;
				//---------------------------------------------------------------------------
				//TODO: Original
				/*
				CmdEnv.top().exec(
						"spawn " + ((Class) Property.value(usercfg.netAirName, "airClass", null)).getName() + " PLAYER NAME "
								+ (((NetUser) NetEnv.host()).netUserRegiment.isEmpty() ? usercfg.netRegiment : "") + usercfg.netSquadron + "0" + string + " WEAPONS "
								+ usercfg.getWeapon(usercfg.netAirName) + " BORNPLACE " + i + " STAYPLACE " + i_1_ + " FUEL " + usercfg.fuel + " OVR");
				*/
				//TODO: |ZUTI| call
				CmdEnv.top().exec(
						"spawn " + ((Class)Property.value(usercfg.netAirName, "airClass", null)).getName() + " PLAYER NAME " + (((NetUser)NetEnv.host()).netUserRegiment.isEmpty() ? usercfg.netRegiment : "") + usercfg.netSquadron + "0" + string
								+ " WEAPONS " + usercfg.getWeapon(usercfg.netAirName) + " BORNPLACE " + i + " STAYPLACE " + i_1_ + " FUEL " + usercfg.fuel + (airstart ? " Z_AIRSTART " : " ") + (usercfg.bZutiMultiCrew ? " Z_MULTICREW " : " ") + (usercfg.bZutiMultiCrewAnytime ? "Z_MULTICREW_ANYTIME" : "") + " OVR");
				//---------------------------------------------------------------------------
				
				com.maddox.il2.objects.air.Aircraft aircraft = World.getPlayerAircraft();
				if (Actor.isValid(aircraft))
				{
					GUI.unActivate();
					HotKeyCmd.exec("aircraftView", "CockpitView");
					ForceFeedback.startMission();
					AudioDevice.soundsOn();
					Main.stateStack().change(42);
					
					//TODO: Added by |ZUTI|: sync your position with other crew members
					//---------------------------------------------------------------------------------------
					//Enable joystick!
					ZutiSupportMethods_Multicrew.setJojstickState();
					//---------------------------------------------------------------------
				}
			}
		}
	}

	protected void doDiff()
	{
		Main.stateStack().push(41);
	}

	protected void doLoodout()
	{
		if (isValidBornPlace())
		{
			GUIAirArming.stateId = 2;
			Main.stateStack().push(55);
		}
	}

	protected void doBack()
	{
		GUINetServer.exitServer(true);
	}

	protected void clientRender()
	{
		GUIBriefingGeneric.DialogClient dialogclient = dialogClient;
		GUIBriefingGeneric.DialogClient dialogclient_2_ = dialogclient;
		float f = dialogclient.x1024(15.0F);
		float f_3_ = dialogclient.y1024(633.0F);
		float f_4_ = dialogclient.x1024(140.0F);
		float f_5_ = dialogclient.y1024(48.0F);
		if (dialogclient != null)
		{
			/* empty */
		}
		dialogclient_2_.draw(f, f_3_, f_4_, f_5_, 1, (USGS.isUsed() || Main.cur().netGameSpy != null ? i18n("main.Quit") : i18n("brief.MainMenu")));
		GUIBriefingGeneric.DialogClient dialogclient_6_ = dialogclient;
		float f_7_ = dialogclient.x1024(194.0F);
		float f_8_ = dialogclient.y1024(633.0F);
		float f_9_ = dialogclient.x1024(208.0F);
		float f_10_ = dialogclient.y1024(48.0F);
		if (dialogclient != null)
		{
			/* empty */
		}
		dialogclient_6_.draw(f_7_, f_8_, f_9_, f_10_, 1, i18n("brief.Difficulty"));
		GUIBriefingGeneric.DialogClient dialogclient_11_ = dialogclient;
		float f_12_ = dialogclient.x1024(680.0F);
		float f_13_ = dialogclient.y1024(633.0F);
		float f_14_ = dialogclient.x1024(176.0F);
		float f_15_ = dialogclient.y1024(48.0F);
		if (dialogclient != null)
		{
			/* empty */
		}
		dialogclient_11_.draw(f_12_, f_13_, f_14_, f_15_, 1, i18n("brief.Arming"));
		super.clientRender();
	}

	protected void clientSetPosSize()
	{
		GUIBriefingGeneric.DialogClient dialogclient = dialogClient;
		bLoodout.setPosC(dialogclient.x1024(768.0F), dialogclient.y1024(689.0F));
	}

	public GUINetServerDBrief(GWindowRoot gwindowroot)
	{
		super(39);
		init(gwindowroot);
	}
}
