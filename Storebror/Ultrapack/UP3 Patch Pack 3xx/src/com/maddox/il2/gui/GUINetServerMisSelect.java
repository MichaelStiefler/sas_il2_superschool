/*4.10.1 Class*/
package com.maddox.il2.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.TreeMap;

import com.maddox.gwindow.GColor;
import com.maddox.gwindow.GTexture;
import com.maddox.gwindow.GWindow;
import com.maddox.gwindow.GWindowComboControl;
import com.maddox.gwindow.GWindowMessageBox;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.gwindow.GWindowTable;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.GameState;
import com.maddox.il2.game.I18N;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Main3D;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetUser;
import com.maddox.il2.net.USGS;
import com.maddox.rts.BackgroundTask;
import com.maddox.rts.CmdEnv;
import com.maddox.rts.HomePath;
import com.maddox.rts.MsgAction;
import com.maddox.rts.MsgBackgroundTaskListener;
import com.maddox.rts.NetEnv;
import com.maddox.rts.RTSConf;
import com.maddox.rts.SectFile;

public class GUINetServerMisSelect extends GameState
{
	public static final String HOME_DIR = "missions/net";
	public static final String HOME_DIR_DOGFIGHT = "missions/net/dogfight";
	public static final String HOME_DIR_COOP = "missions/net/coop";
	public GUIClient client;
	public DialogClient dialogClient;
	public GUIInfoMenu infoMenu;
	public GUIInfoName infoName;
	public GUIButton wPrev;
	public GUIButton wDifficulty;
	public GUIButton wNext;
	public GWindowComboControl wDirs;
	public Table wTable;
	public WDescript wDescript;
	private GWindowMessageBox loadMessageBox;
	public TreeMap _scanMap = new TreeMap();

	public class DialogClient extends GUIDialogClient
	{
		public boolean notify(GWindow gwindow, int i, int i_0_)
		{
			if (i != 2 || loadMessageBox != null)
				return super.notify(gwindow, i, i_0_);
			if (gwindow == wPrev)
			{
				GUINetServer.exitServer(true);
				return true;
			}
			if (gwindow == wDirs)
			{
				fillFiles();
				return true;
			}
			if (gwindow == wDifficulty)
			{
				Main.stateStack().push(41);
				return true;
			}
			if (gwindow == wNext)
			{
				if (wDirs.getValue() == null)
					return true;
				int i_1_ = wTable.selectRow;
				if (i_1_ < 0 || i_1_ >= wTable.files.size())
					return true;
				FileMission filemission = (FileMission) wTable.files.get(i_1_);
				Main.cur().currentMissionFile = new SectFile((HOME_DIR() + "/" + wDirs.getValue() + "/" + filemission.fileName), 0);
				GUINetServerMisSelect.this.doLoadMission();
				return true;
			}
			return super.notify(gwindow, i, i_0_);
		}

		public void render()
		{
			super.render();
			GUISeparate.draw(this, GColor.Gray, x1024(432.0F), y1024(546.0F), x1024(384.0F), 2.0F);
			GUISeparate.draw(this, GColor.Gray, x1024(416.0F), y1024(32.0F), 2.0F, y1024(608.0F));
			setCanvasColor(GColor.Gray);
			setCanvasFont(0);
			draw(x1024(64.0F), y1024(156.0F), x1024(240.0F), y1024(32.0F), 0, GUINetServerMisSelect.this.i18n("netsms.MissionType"));
			draw(x1024(64.0F), y1024(264.0F), x1024(240.0F), y1024(32.0F), 0, GUINetServerMisSelect.this.i18n("netsms.Missions"));
			draw(x1024(464.0F), y1024(264.0F), x1024(248.0F), y1024(32.0F), 0, GUINetServerMisSelect.this.i18n("netsms.Description"));
			draw(x1024(104.0F), y1024(592.0F), x1024(192.0F), y1024(48.0F), 0, (USGS.isUsed() || Main.cur().netGameSpy != null ? GUINetServerMisSelect.this.i18n("main.Quit") : GUINetServerMisSelect.this.i18n("netsms.MainMenu")));
			draw(x1024(496.0F), y1024(592.0F), x1024(128.0F), y1024(48.0F), 0, GUINetServerMisSelect.this.i18n("brief.Difficulty"));
			draw(x1024(528.0F), y1024(592.0F), x1024(216.0F), y1024(48.0F), 2, GUINetServerMisSelect.this.i18n("netsms.Load"));
		}

		public void setPosSize()
		{
			set1024PosSize(80.0F, 64.0F, 848.0F, 672.0F);
			wPrev.setPosC(x1024(56.0F), y1024(616.0F));
			wDifficulty.setPosC(x1024(456.0F), y1024(616.0F));
			wNext.setPosC(x1024(792.0F), y1024(616.0F));
			wDirs.setPosSize(x1024(48.0F), y1024(192.0F), x1024(336.0F), M(2.0F));
			wTable.setPosSize(x1024(48.0F), y1024(304.0F), x1024(336.0F), y1024(256.0F));
			wDescript.setPosSize(x1024(448.0F), y1024(312.0F), x1024(354.0F), y1024(212.0F));
		}
	}

	public class WDescript extends GWindow
	{
		public void render()
		{
			String string = null;
			if (wTable.selectRow >= 0)
			{
				string = (((FileMission) wTable.files.get(wTable.selectRow)).description);
				if (string != null && string.length() == 0)
					string = null;
			}
			if (string != null)
			{
				setCanvasFont(0);
				setCanvasColorBLACK();
				drawLines(0.0F, -root.C.font.descender, string, 0, string.length(), win.dx, root.C.font.height);
			}
		}
	}

	public class Table extends GWindowTable
	{
		public ArrayList files = new ArrayList();

		public int countRows()
		{
			return files != null ? files.size() : 0;
		}

		public void renderCell(int i, int i_2_, boolean bool, float f, float f_3_)
		{
			setCanvasFont(0);
			String string = ((FileMission) files.get(i)).name;
			if (bool)
			{
				setCanvasColorBLACK();
				draw(0.0F, 0.0F, f, f_3_, lookAndFeel().regionWhite);
				setCanvasColorWHITE();
				draw(0.0F, 0.0F, f, f_3_, 0, string);
			}
			else
			{
				setCanvasColorBLACK();
				draw(0.0F, 0.0F, f, f_3_, 0, string);
			}
		}

		public void afterCreated()
		{
			super.afterCreated();
			bColumnsSizable = false;
			addColumn(I18N.gui("netsms.Mission_files"), null);
			vSB.scroll = rowHeight(0);
			resized();
		}

		public void resolutionChanged()
		{
			vSB.scroll = rowHeight(0);
			super.resolutionChanged();
		}

		public boolean notify(GWindow gwindow, int i, int i_4_)
		{
			if (super.notify(gwindow, i, i_4_))
				return true;
			this.notify(i, i_4_);
			return false;
		}

		public Table(GWindow gwindow)
		{
			super(gwindow, 2.0F, 4.0F, 20.0F, 16.0F);
			bNotify = true;
			wClient.bNotify = true;
		}
	}

	static class FileMission
	{
		public String fileName;
		public String name;
		public String description;

		public FileMission(String string, String string_5_)
		{
			fileName = string_5_;
			try
			{
				String string_6_ = string_5_;
				int i = string_6_.lastIndexOf(".");
				if (i >= 0)
					string_6_ = string_6_.substring(0, i);
				ResourceBundle resourcebundle = ResourceBundle.getBundle(string + "/" + string_6_, RTSConf.cur.locale);
				name = resourcebundle.getString("Name");
				description = resourcebundle.getString("Short");
			}
			catch (Exception exception)
			{
				name = string_5_;
				description = null;
			}
		}
	}

	class MissionListener implements MsgBackgroundTaskListener
	{
		public void msgBackgroundTaskStarted(BackgroundTask backgroundtask)
		{
		/* empty */
		}

		public void msgBackgroundTaskStep(BackgroundTask backgroundtask)
		{
			loadMessageBox.message = ((int) backgroundtask.percentComplete() + "% " + I18N.gui(backgroundtask.messageComplete()));
		}

		public void msgBackgroundTaskStoped(BackgroundTask backgroundtask)
		{
			BackgroundTask.removeListener(this);
			if (backgroundtask.isComplete())
				missionLoaded();
			else
				GUINetServerMisSelect.this.missionBad(I18N.gui("miss.LoadBad") + " " + backgroundtask.messageCancel());
		}

		public MissionListener()
		{
			BackgroundTask.addListener(this);
		}
	}

	private void doLoadMission()
	{
		loadMessageBox = new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("netsms.StandBy"), (i18n("netsms.Loading_simulation")), 5, 0.0F)
		{
			public void result(int i)
			{
				if (i == 1)
					BackgroundTask.cancel(I18N.gui("miss.UserCancel"));
			}
		};
		new MsgAction(72, 0.0)
		{
			public void doAction()
			{
				if (Mission.cur() != null)
					Mission.cur().destroy();
				try
				{
					new MissionListener();
					Mission.loadFromSect(Main.cur().currentMissionFile, true);
				}
				catch (Exception exception)
				{
					System.out.println(exception.getMessage());
					exception.printStackTrace();
					GUINetServerMisSelect.this.missionBad(I18N.gui("miss.LoadBad"));
				}
			}
		};
	}

	public void missionLoaded()
	{
		new MsgAction(72, 0.0)
		{
			public void doAction()
			{
				//com.maddox.il2.engine.GUIWindowManager guiwindowmanager = Main3D.cur3D().guiManager;
				if (loadMessageBox != null)
				{
					loadMessageBox.close(false);
					loadMessageBox = null;
				}
				if (Main.cur().netServerParams.isDogfight())
				{
					((NetUser) NetEnv.host()).setBornPlace(-1);
					CmdEnv.top().exec("mission BEGIN");
					Main.stateStack().change(39);
				}
				else if (Main.cur().netServerParams.isCoop())
				{
					((NetUser) NetEnv.host()).resetAllPlaces();
					CmdEnv.top().exec("mission BEGIN");
					int i = GUINetAircraft.serverPlace();
					if (i != -1)
						((NetUser) NetEnv.host()).requestPlace(i);
					Main.stateStack().change(45);
				}
			}
		};
	}

	private void missionBad(String string)
	{
		loadMessageBox.close(false);
		loadMessageBox = null;
		new GWindowMessageBox(Main3D.cur3D().guiManager.root, 20.0F, true, i18n("netsms.Error"), string, 3, 0.0F)
		{
			public void result(int i)
			{
			/* empty */
			}
		};
	}

	public String HOME_DIR()
	{
		if (Main.cur().netServerParams.isCoop())
			return "missions/net/coop";
		if (Main.cur().netServerParams.isDogfight())
			return "missions/net/dogfight";
		return "missions/net";
	}

	public void enter(GameState gamestate)
	{
		World.cur().diffCur.set(World.cur().userCfg.netDifficulty);
		Main.cur().netServerParams.setDifficulty(World.cur().diffCur.get());
		if (gamestate.id() == 35)
			_enter();
		else
		{
			if (Main.cur().netServerParams.isCoop())
			{
				NetEnv.cur().connect.bindEnable(true);
				Main.cur().netServerParams.USGSupdate();
			}
			client.activateWindow();
		}
	}

	public void enterPop(GameState gamestate)
	{
		if (gamestate.id() == 41)
		{
			World.cur().userCfg.netDifficulty = World.cur().diffCur.get();
			World.cur().userCfg.saveConf();
			Main.cur().netServerParams.setDifficulty(World.cur().diffCur.get());
		}
		client.activateWindow();
	}

	public void _enter()
	{
		fillDirs();
		if (Main.cur().netServerParams.isCoop())
		{
			infoMenu.info = i18n("netsms.infoC");
			NetEnv.cur().connect.bindEnable(true);
			Main.cur().netServerParams.USGSupdate();
		}
		else if (Main.cur().netServerParams.isDogfight())
			infoMenu.info = i18n("netsms.infoD");
		else
			infoMenu.info = i18n("netsms.infoM");
		client.activateWindow();
	}

	public void _leave()
	{
		client.hideWindow();
	}

	public void fillDirs()
	{
		File file = new File(HomePath.get(0), HOME_DIR());
		File[] files = file.listFiles();
		wDirs.clear(false);
		if (files == null || files.length == 0)
		{
			wTable.files.clear();
			wTable.setSelect(-1, 0);
		}
		else
		{
			for (int i = 0; i < files.length; i++)
			{
				if (files[i].isDirectory() && !files[i].isHidden() && !".".equals(files[i].getName()) && !"..".equals(files[i].getName()))
					_scanMap.put(files[i].getName(), null);
			}
			Iterator iterator = _scanMap.keySet().iterator();
			while (iterator.hasNext())
				wDirs.add((String) iterator.next());
			if (_scanMap.size() > 0)
				wDirs.setSelected(0, true, false);
			_scanMap.clear();
			fillFiles();
		}
	}

	public void fillFiles()
	{
		wTable.files.clear();
		String string = wDirs.getValue();
		if (string != null)
		{
			String string_16_ = HOME_DIR() + "/" + string;
			File file = new File(HomePath.get(0), string_16_);
			File[] files = file.listFiles();
			if (files != null && files.length > 0)
			{
				for (int i = 0; i < files.length; i++)
				{
					//TODO: Added by |ZUTI|: show only files that end with .mis
					//if (!files[i].isDirectory() && !files[i].isHidden() && files[i].getName().toLowerCase().lastIndexOf(".properties") < 0)
					if (!files[i].isDirectory() && !files[i].isHidden() && files[i].getName().toLowerCase().lastIndexOf(".mis") >= 0 && files[i].getName().toLowerCase().lastIndexOf(".mismds") < 0)
					{
						FileMission filemission = new FileMission(string_16_, files[i].getName());
						_scanMap.put(filemission.fileName, filemission);
					}
				}
				Iterator iterator = _scanMap.keySet().iterator();
				while (iterator.hasNext())
					wTable.files.add(_scanMap.get(iterator.next()));
				
				//TODO: Added by |ZUTI|: order table content ignoring upper/downer cases in mission names
				//---------------------------------------------------
				Collections.sort(wTable.files, new ZutiSupportMethods_GUI.Missions_CompareByFileName());
				//---------------------------------------------------
				
				if (_scanMap.size() > 0)
					wTable.setSelect(0, 0);
				else
					wTable.setSelect(-1, 0);
				_scanMap.clear();
			}
			else
				wTable.setSelect(-1, 0);
		}
		else
			wTable.setSelect(-1, 0);
		wTable.resized();
	}

	public GUINetServerMisSelect(GWindowRoot gwindowroot)
	{
		super(38);
		client = (GUIClient) gwindowroot.create(new GUIClient());
		dialogClient = (DialogClient) client.create(new DialogClient());
		infoMenu = (GUIInfoMenu) client.create(new GUIInfoMenu());
		infoMenu.info = i18n("netsms.infoM");
		infoName = (GUIInfoName) client.create(new GUIInfoName());
		wDirs = ((GWindowComboControl) (dialogClient.addControl(new GWindowComboControl(dialogClient, 2.0F, 2.0F, 20.0F + (gwindowroot.lookAndFeel().getHScrollBarW() / gwindowroot.lookAndFeel().metric())))));
		wDirs.setEditable(false);
		wTable = new Table(dialogClient);
		dialogClient.create(wDescript = new WDescript());
		wDescript.bNotify = true;
		GTexture gtexture = ((GUILookAndFeel) gwindowroot.lookAndFeel()).buttons2;
		wPrev = (GUIButton) dialogClient.addEscape(new GUIButton(dialogClient, gtexture, 0.0F, 96.0F, 48.0F, 48.0F));
		wDifficulty = (GUIButton) dialogClient.addControl(new GUIButton(dialogClient, gtexture, 0.0F, 48.0F, 48.0F, 48.0F));
		wNext = (GUIButton) dialogClient.addDefault(new GUIButton(dialogClient, gtexture, 0.0F, 192.0F, 48.0F, 48.0F));
		dialogClient.activateWindow();
		client.hideWindow();
	}
}
