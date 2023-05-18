/*Using 4.09 class because TD changed theirs with refly button blocking here...*/
package com.maddox.il2.gui;
import com.maddox.gwindow.GWindowRoot;
import com.maddox.il2.ai.EventLog;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.NetUser;
import com.maddox.rts.NetEnv;
import com.maddox.sound.AudioDevice;

public class GUINetServerDMission extends GUINetMission
{
	protected void clientRender()
	{
		GUINetMission.DialogClient dialogclient = dialogClient;
		if (bEnableReFly)
		{
			GUINetMission.DialogClient dialogclient_0_ = dialogclient;
			float f = dialogclient.x1024(96.0F);
			float f_1_ = dialogclient.y1024(400.0F);
			float f_2_ = dialogclient.x1024(224.0F);
			float f_3_ = dialogclient.y1024(48.0F);
			if (dialogclient != null)
			{
				/* empty */
			}
			dialogclient_0_.draw(f, f_1_, f_2_, f_3_, 0, i18n("miss.ReFly"));
		}
		GUINetMission.DialogClient dialogclient_4_ = dialogclient;
		float f = dialogclient.x1024(96.0F);
		float f_5_ = dialogclient.y1024(464.0F);
		float f_6_ = dialogclient.x1024(224.0F);
		float f_7_ = dialogclient.y1024(48.0F);
		if (dialogclient != null)
		{
			/* empty */
		}
		dialogclient_4_.draw(f, f_5_, f_6_, f_7_, 0, i18n("miss.QuitMiss"));
	}
	
	protected void doReFly()
	{
		checkCaptured();
		destroyPlayerActor();
		((NetUser)NetEnv.host()).sendStatInc();
		EventLog.onRefly(((NetUser)NetEnv.host()).shortName());
		AudioDevice.soundsOff();
		Main.stateStack().change(39);
	}
	
	protected void doExit()
	{
		checkCaptured();
		destroyPlayerActor();
		((NetUser)NetEnv.host()).sendStatInc();
		Mission.cur().destroy();
		GUI.activate();
		Main.stateStack().change(38);
	}
	
	public GUINetServerDMission(GWindowRoot gwindowroot)
	{
		super(42);
		init(gwindowroot);
		infoMenu.info = i18n("miss.NetSDInfo");
	}
}
