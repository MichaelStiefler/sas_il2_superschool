/*4.10.1 class*/
package com.maddox.il2.game.order;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Way;
import com.maddox.il2.ai.WayPoint;
import com.maddox.il2.ai.World;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Main;
import com.maddox.il2.game.Mission;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;
import com.maddox.il2.objects.air.Aircraft;
import com.maddox.il2.objects.sounds.Voice;
import com.maddox.il2.objects.vehicles.radios.BeaconGeneric;
import java.util.Timer;
import java.util.TimerTask;

public class OrderVector_To_Home_Base extends Order
{
	private static Point3d V1 = new Point3d();
	private static Point3d V2 = new Point3d();
	
	public OrderVector_To_Home_Base()
	{
		super("Vector_To_Home_Base");
	}
	
	public void run()
	{
		try
		{
			if( Mission.MDS_VARIABLES().zutiMisc_DisableVectoring )
			{
				HUD.log("mds.vectoringNotPossible");
				return;
			}
			
			Player().FM.moveCarrier();
			Point3d localPoint3d = Player().FM.actor.pos.getAbsPoint();
			Object localObject;
			if ((Main.cur().netServerParams != null) && (Main.cur().netServerParams.isDogfight()))
			{
				localObject = ZutiSupportMethods.getPlayerBornPlace(localPoint3d, Player().getArmy());
				if (localObject != null)
					V2 = new Point3d(((BornPlace)localObject).place.x, ((BornPlace)localObject).place.y, 0.0D);
				else
				{
					return;
				}
			}
			else
			{
				localObject = Player().FM.AP.way;
				WayPoint localWayPoint = ((Way)localObject).get(((Way)localObject).size() - 1);
				localWayPoint.getP(V2);
			}
			
			Player().FM.actor.pos.getAbs(V1);
			V1.sub(V2);
			
			double d1 = Math.sqrt(V1.x * V1.x + V1.y * V1.y);
			double d2 = BeaconGeneric.lineOfSightDelta(Player().FM.getAltitude(), V2.z, d1);
			if (((d2 < 0.0D) || (d1 > 90000.0D)) && (World.cur().diffCur.RealisticNavigationInstruments))
			{
				return;
			}
			if (isEnableVoice())
			{
				if (World.cur().diffCur.RealisticNavigationInstruments)
					new DelayedOrder(Player(), d1);
				else
					Voice.speakHeadingToHome(Player(), V1);
			}
		}
		catch (Exception localException)
		{
			System.out.println("OrderVector_To_Home_Base error, ID_01: " + localException.toString());
		}
	}
	
	private class DelayedOrder extends TimerTask
	{
		private Timer timer = null;
		private Aircraft ac = null;
		
		public DelayedOrder(Aircraft paramDouble, double arg3)
		{
			this.ac = paramDouble;
			this.timer = new Timer();
			long l = (long)Math.random() * (long)(arg3 / 7D);
			this.timer.schedule(this, 10000L + l);
		}
		public void run()
		{
			Voice.speakHeadingToHome(this.ac, OrderVector_To_Home_Base.V1);
		}
	}
}