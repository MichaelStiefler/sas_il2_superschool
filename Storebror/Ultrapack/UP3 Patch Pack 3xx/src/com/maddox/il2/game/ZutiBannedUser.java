package com.maddox.il2.game;

import java.lang.String;
import com.maddox.rts.Time;

public class ZutiBannedUser
{
	private String name;
	private String IP;
	private long duration;
	
	public ZutiBannedUser(){}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String value)
	{
		name = value;
	}
	
	public String getIP()
	{
		return IP;
	}
	
	public void setIP(String value)
	{
		IP = value;
	}
		
	public void setDuration(long value)
	{
		duration = value;
	}
	
	public long getDuration()
	{
		return duration;
	}
	
	public boolean isMatch(String inName, String inIP)
	{
		if( name.trim().equalsIgnoreCase(inName.trim()) && IP.trim().equalsIgnoreCase(inIP.trim()) )
			return true;
		
		return false;
	}
	
	public boolean isBanned()
	{		
		if( Time.current() < duration )
			return true;
		
		return false;
	}
}