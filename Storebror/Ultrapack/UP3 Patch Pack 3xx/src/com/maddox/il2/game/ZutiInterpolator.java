package com.maddox.il2.game;

import com.maddox.il2.engine.Interpolate;
import com.maddox.il2.objects.air.ZutiSupportMethods_Air;
import com.maddox.rts.Time;

public class ZutiInterpolator extends Interpolate
{
	public static boolean ZUTI_DESTROY_PLAYER_AC = false;
	public static long ZUTI_DELAYED_DESTROY_PLAYER_AC = -1;
	
	public boolean tick()
	{
		if (ZUTI_DESTROY_PLAYER_AC)
		{
			ZutiSupportMethods_Air.destroyPlayerAircraft(false);
			ZUTI_DESTROY_PLAYER_AC = false;
		}
		
		if( ZUTI_DELAYED_DESTROY_PLAYER_AC > 0 && ZUTI_DELAYED_DESTROY_PLAYER_AC < Time.current() )
		{
			ZutiSupportMethods_Air.destroyPlayerAircraft(false);
			ZUTI_DELAYED_DESTROY_PLAYER_AC = -1;
		}
		
		return true;
	}
}