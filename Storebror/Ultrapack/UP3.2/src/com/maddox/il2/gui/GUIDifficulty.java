/*4.10.1 class*/
package com.maddox.il2.gui;
import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.DifficultySettings;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.Main;

public class GUIDifficulty extends GameState
{
	public GUIClient client;
	public DialogClient dialogClient;
	public GUIInfoMenu infoMenu;
	public GUIInfoName infoName;
	public GUISwitchBox3 sWind_N_Turbulence;
	public GUISwitchBox3 sFlutter_Effect;
	public GUISwitchBox3 sStalls_N_Spins;
	public GUISwitchBox3 sBlackouts_N_Redouts;
	public GUISwitchBox3 sEngine_Overheat;
	public GUISwitchBox3 sTorque_N_Gyro_Effects;
	public GUISwitchBox3 sRealistic_Landings;
	public GUISwitchBox3 sTakeoff_N_Landing;
	public GUISwitchBox3 sCockpit_Always_On;
	public GUISwitchBox3 sNo_Outside_Views;
	public GUISwitchBox3 sHead_Shake;
	public GUISwitchBox3 sNo_Icons;
	public GUISwitchBox3 sNo_Map_Icons;
	public GUISwitchBox3 sRealistic_Gunnery;
	public GUISwitchBox3 sLimited_Ammo;
	public GUISwitchBox3 sLimited_Fuel;
	public GUISwitchBox3 sVulnerability;
	public GUISwitchBox3 sNo_Padlock;
	public GUISwitchBox3 sClouds;
	public GUISwitchBox3 sSeparateEStart;
	public GUISwitchBox3 sNoInstantSuccess;
	public GUISwitchBox3 sNoMinimapPath;
	public GUISwitchBox3 sNoSpeedBar;
	public GUISwitchBox3 sComplexEManagement;
	public GUISwitchBox3 sReliability;
	public GUISwitchBox3 sG_Limits;
	public GUISwitchBox3 sRealisticPilotVulnerability;
	public GUISwitchBox3 sRealisticNavigationInstruments;
	
	//TODO: Disabled by |ZUTI|
	//-------------------------------------
	//public GUISwitchBox3 sNo_Player_Icon;
	//public GUISwitchBox3 sNo_Fog_Of_War_Icons;
	//-------------------------------------
	
	public GUIButton bExit;
	public GUIButton bEasy;
	public GUIButton bNormal;
	public GUIButton bHard;
	public GUIButton bNext;
	public boolean bEnable = true;
	public boolean bFirst = true;
	public boolean bSecond = false;
	public boolean bThird = false;
	
	public class DialogClient extends GUIDialogClient
	{
		public boolean notify(GWindow gwindow, int i, int i_0_)
		{
			if (i != 2)
				return super.notify(gwindow, i, i_0_);
			if (gwindow == bExit)
			{
				if (bFirst)
					Main.stateStack().pop();
				else if (bSecond)
				{
					bFirst = true;
					bSecond = false;
					bThird = false;
					GUIDifficulty.this.showHide();
				}
				else
				{
					bSecond = true;
					bFirst = false;
					bThird = false;
					GUIDifficulty.this.showHide();
				}
				return true;
			}
			if (gwindow == bNext)
			{
				if (bFirst)
				{
					bFirst = false;
					bSecond = true;
					bThird = false;
					GUIDifficulty.this.showHide();
				}
				else if (bSecond)
				{
					bFirst = false;
					bSecond = false;
					bThird = true;
					GUIDifficulty.this.showHide();
				}
				return true;
			}
			if (gwindow == bEasy)
			{
				settings().setEasy();
				GUIDifficulty.this.reset();
				return true;
			}
			if (gwindow == bNormal)
			{
				settings().setNormal();
				GUIDifficulty.this.reset();
				return true;
			}
			if (gwindow == bHard)
			{
				settings().setRealistic();
				GUIDifficulty.this.reset();
				return true;
			}
			
			//TODO: Disabled by |ZUTI|
			//---------------------------------------
			/*
			if (gwindow == sNo_Map_Icons)
			{
				sNo_Fog_Of_War_Icons.setEnable(sNo_Map_Icons.isChecked());
				if (!sNo_Map_Icons.isChecked())
					sNo_Fog_Of_War_Icons.setChecked(true, false);
			}
			*/
			//---------------------------------------
			return super.notify(gwindow, i, i_0_);
		}
		
		public void render()
		{
			super.render();
			GUISeparate.draw(this, GColor.Gray, x1024(32.0F), y1024(464.0F), x1024(768.0F), 2.0F);
			GUISeparate.draw(this, GColor.Gray, x1024(32.0F), y1024(544.0F), x1024(768.0F), 2.0F);
			setCanvasColor(GColor.Gray);
			setCanvasFont(0);
			draw(x1024(96.0F), y1024(577.0F), x1024(224.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Back"));
			if (bEnable)
			{
				draw(x1024(224.0F), y1024(464.0F), x1024(128.0F), y1024(48.0F), 2, GUIDifficulty.this.i18n("diff.Easy"));
				draw(x1024(416.0F), y1024(464.0F), x1024(128.0F), y1024(48.0F), 2, GUIDifficulty.this.i18n("diff.Normal"));
				draw(x1024(608.0F), y1024(464.0F), x1024(128.0F), y1024(48.0F), 2, GUIDifficulty.this.i18n("diff.Hard"));
			}
			
			if (bFirst || bSecond)
				draw(x1024(512.0F), y1024(577.0F), x1024(224.0F), y1024(48.0F), 2, GUIDifficulty.this.i18n("diff.Next"));
			if (bFirst)
			{
				draw(x1024(128.0F), y1024(32.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.SeparateEStart"));
				draw(x1024(128.0F), y1024(96.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.ComplexEManagement"));
				draw(x1024(128.0F), y1024(160.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Engine"));
				draw(x1024(128.0F), y1024(224.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Torque"));
				draw(x1024(128.0F), y1024(288.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Flutter"));
				draw(x1024(128.0F), y1024(352.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Wind"));
				draw(x1024(128.0F), y1024(416.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Reliability"));
				draw(x1024(528.0F), y1024(32.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Stalls"));
				draw(x1024(528.0F), y1024(96.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Vulnerability"));
				draw(x1024(528.0F), y1024(160.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Blackouts"));
				draw(x1024(528.0F), y1024(224.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.RealisticGun"));
				draw(x1024(528.0F), y1024(288.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.LimitedAmmo"));
				draw(x1024(528.0F), y1024(352.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.LimitedFuel"));
				draw(x1024(528.0F), y1024(416.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.G_Limits"));
			}
			else if (bSecond)
			{
				draw(x1024(128.0F), y1024(32.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Cockpit"));
				draw(x1024(128.0F), y1024(96.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoOutside"));
				draw(x1024(128.0F), y1024(160.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Head"));
				draw(x1024(128.0F), y1024(224.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoIcons"));
				draw(x1024(128.0F), y1024(288.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoPadlock"));
				draw(x1024(128.0F), y1024(352.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Clouds"));
				draw(x1024(528.0F), y1024(32.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoInstantSuccess"));
				draw(x1024(528.0F), y1024(96.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.Takeoff"));
				draw(x1024(528.0F), y1024(160.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.RealisticLand"));
				draw(x1024(528.0F), y1024(224.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoSpeedBar"));
				draw(x1024(528.0F), y1024(288.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.RealisticNavInstr"));
				draw(x1024(528.0F), y1024(352.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.RealisticPilotVulnerability"));
			}
			else if (bThird)
			{
				//TODO: Changed by |ZUTI|: disabled two options!
				/*
				draw(x1024(128.0F), y1024(32.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoMapIcons"));
				draw(x1024(128.0F), y1024(96.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoPlayerIcon"));
				draw(x1024(128.0F), y1024(160.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoFogOfWarIcons"));
				draw(x1024(128.0F), y1024(224.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoMinimapPath"));
				*/
				
				draw(x1024(128.0F), y1024(32.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoMapIcons"));
				draw(x1024(128.0F), y1024(96.0F), x1024(272.0F), y1024(48.0F), 0, GUIDifficulty.this.i18n("diff.NoMinimapPath"));
			}
		}
		
		public void setPosSize()
		{
			set1024PosSize(92.0F, 72.0F, 832.0F, 656.0F);
			sSeparateEStart.setPosC(x1024(88.0F), y1024(56.0F));
			sComplexEManagement.setPosC(x1024(88.0F), y1024(120.0F));
			sEngine_Overheat.setPosC(x1024(88.0F), y1024(184.0F));
			sTorque_N_Gyro_Effects.setPosC(x1024(88.0F), y1024(248.0F));
			sFlutter_Effect.setPosC(x1024(88.0F), y1024(312.0F));
			sWind_N_Turbulence.setPosC(x1024(88.0F), y1024(376.0F));
			sReliability.setPosC(x1024(88.0F), y1024(440.0F));
			sStalls_N_Spins.setPosC(x1024(488.0F), y1024(56.0F));
			sVulnerability.setPosC(x1024(488.0F), y1024(120.0F));
			sBlackouts_N_Redouts.setPosC(x1024(488.0F), y1024(184.0F));
			sRealistic_Gunnery.setPosC(x1024(488.0F), y1024(248.0F));
			sLimited_Ammo.setPosC(x1024(488.0F), y1024(312.0F));
			sLimited_Fuel.setPosC(x1024(488.0F), y1024(376.0F));
			sG_Limits.setPosC(x1024(488.0F), y1024(440.0F));
			sCockpit_Always_On.setPosC(x1024(88.0F), y1024(56.0F));
			sNo_Outside_Views.setPosC(x1024(88.0F), y1024(120.0F));
			sHead_Shake.setPosC(x1024(88.0F), y1024(184.0F));
			sNo_Icons.setPosC(x1024(88.0F), y1024(248.0F));
			sNo_Padlock.setPosC(x1024(88.0F), y1024(312.0F));
			sClouds.setPosC(x1024(88.0F), y1024(376.0F));
			sNoInstantSuccess.setPosC(x1024(488.0F), y1024(56.0F));
			sTakeoff_N_Landing.setPosC(x1024(488.0F), y1024(120.0F));
			sRealistic_Landings.setPosC(x1024(488.0F), y1024(184.0F));
			sNoSpeedBar.setPosC(x1024(488.0F), y1024(248.0F));
			sRealisticNavigationInstruments.setPosC(x1024(488.0F), y1024(312.0F));
			sRealisticPilotVulnerability.setPosC(x1024(488.0F), y1024(376.0F));
			
			//TODO: Disabled two buttons by |ZUTI|
			//--------------------------------------------------
			/*
			sNo_Map_Icons.setPosC(x1024(88.0F), y1024(56.0F));
			sNo_Player_Icon.setPosC(x1024(88.0F), y1024(120.0F));
			sNo_Fog_Of_War_Icons.setPosC(x1024(88.0F), y1024(184.0F));
			sNoMinimapPath.setPosC(x1024(88.0F), y1024(248.0F));
			*/
			
			sNo_Map_Icons.setPosC(x1024(88.0F), y1024(56.0F));
			sNoMinimapPath.setPosC(x1024(88.0F), y1024(120.0F));
			//--------------------------------------------------
			
			bExit.setPosC(x1024(56.0F), y1024(602.0F));
			bEasy.setPosC(x1024(392.0F), y1024(488.0F));
			bNormal.setPosC(x1024(584.0F), y1024(488.0F));
			bHard.setPosC(x1024(776.0F), y1024(488.0F));
			bNext.setPosC(x1024(776.0F), y1024(602.0F));
		}
	}
	
	public void enterPush(GameState gamestate)
	{
		if (gamestate.id() == 27)
			bEnable = false;
		else
			bEnable = true;
		_enter();
	}
	
	protected DifficultySettings settings()
	{
		return World.cur().diffUser;
	}
	
	public void _enter()
	{
		reset();
		sReliability.setEnable(bEnable);
		sG_Limits.setEnable(bEnable);
		sRealisticPilotVulnerability.setEnable(bEnable);
		sRealisticNavigationInstruments.setEnable(bEnable);
		//TODO: Disabled by |ZUTI|
		//---------------------------------------
		//sNo_Player_Icon.setEnable(bEnable);
		//---------------------------------------
		sWind_N_Turbulence.setEnable(bEnable);
		sFlutter_Effect.setEnable(bEnable);
		sStalls_N_Spins.setEnable(bEnable);
		sBlackouts_N_Redouts.setEnable(bEnable);
		sEngine_Overheat.setEnable(bEnable);
		sTorque_N_Gyro_Effects.setEnable(bEnable);
		sRealistic_Landings.setEnable(bEnable);
		sTakeoff_N_Landing.setEnable(bEnable);
		sCockpit_Always_On.setEnable(bEnable);
		sNo_Outside_Views.setEnable(bEnable);
		sHead_Shake.setEnable(bEnable);
		sNo_Icons.setEnable(bEnable);
		sNo_Map_Icons.setEnable(bEnable);
		sRealistic_Gunnery.setEnable(bEnable);
		sLimited_Ammo.setEnable(bEnable);
		sLimited_Fuel.setEnable(bEnable);
		sVulnerability.setEnable(bEnable);
		sNo_Padlock.setEnable(bEnable);
		sClouds.setEnable(bEnable);
		sSeparateEStart.setEnable(bEnable);
		sNoInstantSuccess.setEnable(bEnable);
		sNoMinimapPath.setEnable(bEnable);
		sNoSpeedBar.setEnable(bEnable);
		sComplexEManagement.setEnable(bEnable);
		setShow(bEnable, bEasy);
		setShow(bEnable, bNormal);
		setShow(bEnable, bHard);
		showHide();
		client.activateWindow();
	}
	
	private void setShow(boolean bool, GWindow gwindow)
	{
		if (bool)
			gwindow.showWindow();
		else
			gwindow.hideWindow();
	}
	
	private void showHide()
	{
		setShow(bFirst, sSeparateEStart);
		setShow(bFirst, sComplexEManagement);
		setShow(bFirst, sEngine_Overheat);
		setShow(bFirst, sTorque_N_Gyro_Effects);
		setShow(bFirst, sFlutter_Effect);
		setShow(bFirst, sWind_N_Turbulence);
		setShow(bFirst, sStalls_N_Spins);
		setShow(bFirst, sVulnerability);
		setShow(bFirst, sBlackouts_N_Redouts);
		setShow(bFirst, sRealistic_Gunnery);
		setShow(bFirst, sLimited_Ammo);
		setShow(bFirst, sLimited_Fuel);
		setShow(bFirst || bSecond, bNext);
		setShow(bFirst, sReliability);
		setShow(bFirst, sG_Limits);
		setShow(bSecond, sRealisticPilotVulnerability);
		setShow(bSecond, sRealisticNavigationInstruments);
		setShow(bSecond, sCockpit_Always_On);
		setShow(bSecond, sNo_Outside_Views);
		setShow(bSecond, sHead_Shake);
		setShow(bSecond, sNo_Icons);
		setShow(bSecond, sNo_Padlock);
		setShow(bSecond, sClouds);
		setShow(bSecond, sNoInstantSuccess);
		setShow(bSecond, sTakeoff_N_Landing);
		setShow(bSecond, sRealistic_Landings);
		setShow(bSecond, sNoSpeedBar);
		setShow(bThird, sNo_Map_Icons);
		//TODO: Disabled by |ZUTI|
		//---------------------------------------
		/*
		setShow(bThird, sNo_Player_Icon);
		setShow(bThird, sNo_Fog_Of_War_Icons);
		*/
		//---------------------------------------
		setShow(bThird, sNoMinimapPath);
		dialogClient.doResolutionChanged();
		dialogClient.setPosSize();
	}
	
	private void reset()
	{
		DifficultySettings difficultysettings = settings();
		sReliability.setChecked(difficultysettings.Reliability, false);
		sG_Limits.setChecked(difficultysettings.G_Limits, false);
		sRealisticPilotVulnerability.setChecked(difficultysettings.RealisticPilotVulnerability, false);
		sRealisticNavigationInstruments.setChecked(difficultysettings.RealisticNavigationInstruments, false);
		
		//TODO: Disabled by |ZUTI|
		//---------------------------------------
		/*
		sNo_Player_Icon.setChecked(difficultysettings.No_Player_Icon, false);
		sNo_Fog_Of_War_Icons.setChecked(difficultysettings.No_Fog_Of_War_Icons, false);
		sNo_Fog_Of_War_Icons.setEnable(sNo_Map_Icons.isChecked() & bEnable);
		*/
		//---------------------------------------
		
		sWind_N_Turbulence.setChecked(difficultysettings.Wind_N_Turbulence, false);
		sFlutter_Effect.setChecked(difficultysettings.Flutter_Effect, false);
		sStalls_N_Spins.setChecked(difficultysettings.Stalls_N_Spins, false);
		sBlackouts_N_Redouts.setChecked(difficultysettings.Blackouts_N_Redouts, false);
		sEngine_Overheat.setChecked(difficultysettings.Engine_Overheat, false);
		sTorque_N_Gyro_Effects.setChecked(difficultysettings.Torque_N_Gyro_Effects, false);
		sRealistic_Landings.setChecked(difficultysettings.Realistic_Landings, false);
		sTakeoff_N_Landing.setChecked(difficultysettings.Takeoff_N_Landing, false);
		sCockpit_Always_On.setChecked(difficultysettings.Cockpit_Always_On, false);
		sNo_Outside_Views.setChecked(difficultysettings.No_Outside_Views, false);
		sHead_Shake.setChecked(difficultysettings.Head_Shake, false);
		sNo_Icons.setChecked(difficultysettings.No_Icons, false);
		sNo_Map_Icons.setChecked(difficultysettings.No_Map_Icons, false);
		sRealistic_Gunnery.setChecked(difficultysettings.Realistic_Gunnery, false);
		sLimited_Ammo.setChecked(difficultysettings.Limited_Ammo, false);
		sLimited_Fuel.setChecked(difficultysettings.Limited_Fuel, false);
		sVulnerability.setChecked(difficultysettings.Vulnerability, false);
		sNo_Padlock.setChecked(difficultysettings.No_Padlock, false);
		sClouds.setChecked(difficultysettings.Clouds, false);
		sSeparateEStart.setChecked(difficultysettings.SeparateEStart, false);
		sNoInstantSuccess.setChecked(difficultysettings.NoInstantSuccess, false);
		sNoMinimapPath.setChecked(difficultysettings.NoMinimapPath, false);
		sNoSpeedBar.setChecked(difficultysettings.NoSpeedBar, false);
		sComplexEManagement.setChecked(difficultysettings.ComplexEManagement, false);
	}
	
	public void _leave()
	{
		if (bEnable)
		{
			DifficultySettings difficultysettings = settings();
			difficultysettings.Reliability = sReliability.isChecked();
			difficultysettings.RealisticNavigationInstruments = sRealisticNavigationInstruments.isChecked();
			difficultysettings.G_Limits = sG_Limits.isChecked();
			difficultysettings.RealisticPilotVulnerability = sRealisticPilotVulnerability.isChecked();
			//TODO: Disabled by |ZUTI|
			//---------------------------------------
			/*
			difficultysettings.No_Player_Icon = sNo_Player_Icon.isChecked();
			difficultysettings.No_Fog_Of_War_Icons = sNo_Fog_Of_War_Icons.isChecked();
			*/
			//---------------------------------------
			difficultysettings.Wind_N_Turbulence = sWind_N_Turbulence.isChecked();
			difficultysettings.Flutter_Effect = sFlutter_Effect.isChecked();
			difficultysettings.Stalls_N_Spins = sStalls_N_Spins.isChecked();
			difficultysettings.Blackouts_N_Redouts = sBlackouts_N_Redouts.isChecked();
			difficultysettings.Engine_Overheat = sEngine_Overheat.isChecked();
			difficultysettings.Torque_N_Gyro_Effects = sTorque_N_Gyro_Effects.isChecked();
			difficultysettings.Realistic_Landings = sRealistic_Landings.isChecked();
			difficultysettings.Takeoff_N_Landing = sTakeoff_N_Landing.isChecked();
			difficultysettings.Cockpit_Always_On = sCockpit_Always_On.isChecked();
			difficultysettings.No_Outside_Views = sNo_Outside_Views.isChecked();
			difficultysettings.Head_Shake = sHead_Shake.isChecked();
			difficultysettings.No_Icons = sNo_Icons.isChecked();
			difficultysettings.No_Map_Icons = sNo_Map_Icons.isChecked();
			difficultysettings.Realistic_Gunnery = sRealistic_Gunnery.isChecked();
			difficultysettings.Limited_Ammo = sLimited_Ammo.isChecked();
			difficultysettings.Limited_Fuel = sLimited_Fuel.isChecked();
			difficultysettings.Vulnerability = sVulnerability.isChecked();
			difficultysettings.No_Padlock = sNo_Padlock.isChecked();
			difficultysettings.Clouds = sClouds.isChecked();
			difficultysettings.SeparateEStart = sSeparateEStart.isChecked();
			difficultysettings.NoInstantSuccess = sNoInstantSuccess.isChecked();
			difficultysettings.NoMinimapPath = sNoMinimapPath.isChecked();
			difficultysettings.NoSpeedBar = sNoSpeedBar.isChecked();
			difficultysettings.ComplexEManagement = sComplexEManagement.isChecked();
		}
		client.hideWindow();
	}
	
	protected void clientInit(GWindowRoot gwindowroot)
	{
	/* empty */
	}
	
	public GUIDifficulty(GWindowRoot gwindowroot)
	{
		this(gwindowroot, 17);
	}
	
	protected GUIDifficulty(GWindowRoot gwindowroot, int i)
	{
		super(i);
		client = (GUIClient)gwindowroot.create(new GUIClient());
		dialogClient = (DialogClient)client.create(new DialogClient());
		infoMenu = (GUIInfoMenu)client.create(new GUIInfoMenu());
		infoMenu.info = i18n("diff.info");
		infoName = (GUIInfoName)client.create(new GUIInfoName());
		GTexture gtexture = ((GUILookAndFeel)gwindowroot.lookAndFeel()).buttons2;
		bExit = (GUIButton)dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96.0F, 48.0F, 48.0F));
		bEasy = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
		bNormal = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
		bHard = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
		bNext = (GUIButton)dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
		sWind_N_Turbulence = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sFlutter_Effect = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sStalls_N_Spins = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sBlackouts_N_Redouts = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sEngine_Overheat = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sTorque_N_Gyro_Effects = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sRealistic_Landings = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sTakeoff_N_Landing = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sCockpit_Always_On = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sNo_Outside_Views = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sHead_Shake = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sNo_Icons = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sNo_Map_Icons = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sRealistic_Gunnery = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sLimited_Ammo = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sLimited_Fuel = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sVulnerability = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sNo_Padlock = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sClouds = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sSeparateEStart = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sNoInstantSuccess = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sNoMinimapPath = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sNoSpeedBar = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sComplexEManagement = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sReliability = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sRealisticNavigationInstruments = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sG_Limits = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sRealisticPilotVulnerability = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		//TODO: Disabled by |ZUTI|
		//---------------------------------------
		/*
		sNo_Player_Icon = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		sNo_Fog_Of_War_Icons = ((GUISwitchBox3)dialogClient.addControl(new GUISwitchBox3(dialogClient)));
		*/
		//---------------------------------------
		clientInit(gwindowroot);
		dialogClient.activateWindow();
		client.hideWindow();
	}
}