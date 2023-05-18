/*4.10.1 class*/
package com.maddox.il2.builder;

import java.util.AbstractCollection;
import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorFilter;
import com.maddox.il2.engine.ActorPosMove;
import com.maddox.il2.engine.Engine;
import com.maddox.il2.engine.IconDraw;
import com.maddox.il2.objects.ActorAlign;
import com.maddox.il2.objects.bridges.Bridge;
import com.maddox.il2.objects.bridges.BridgeSegment;
import com.maddox.rts.Message;

public class ActorTarget extends Actor implements ActorAlign
{
	public int importance;
	public int type;
	public Actor target = null;
	public int timeout;
	public boolean bTimeout;
	public boolean bLanding;
	public int destructLevel = 50;
	public int r;
	private static Actor _Actor = null;
	private static double _Len2;
	private static double _maxLen2;
	private static SelectBridge _selectBridge = new SelectBridge();
	private static SelectAir _selectAir = new SelectAir();
	private static SelectChief _selectChief = new SelectChief();
	private static SelectMoved _selectMoved = new SelectMoved();
	
	static class SelectMoved implements ActorFilter
	{
		public boolean isUse(Actor actor, double d)
		{
			if (d <= ActorTarget._maxLen2)
			{
				if (!(actor instanceof PPoint))
					return true;
				if (ActorTarget._Actor == null || d < ActorTarget._Len2)
				{
					ActorTarget._Actor = actor;
					ActorTarget._Len2 = d;
				}
			}
			return true;
		}
	}
	
	static class SelectChief implements ActorFilter
	{
		public boolean isUse(Actor actor, double d)
		{
			if (d <= ActorTarget._maxLen2)
			{
				if (!(actor instanceof PNodes))
					return true;
				if (ActorTarget._Actor == null || d < ActorTarget._Len2)
				{
					ActorTarget._Actor = actor;
					ActorTarget._Len2 = d;
				}
			}
			return true;
		}
	}
	
	static class SelectAir implements ActorFilter
	{
		public boolean isUse(Actor actor, double d)
		{
			if (d <= ActorTarget._maxLen2)
			{
				if (!(actor instanceof PAir))
					return true;
				if (ActorTarget._Actor == null || d < ActorTarget._Len2)
				{
					ActorTarget._Actor = actor;
					ActorTarget._Len2 = d;
				}
			}
			return true;
		}
	}
	
	static class SelectBridge implements ActorFilter
	{
		public boolean isUse(Actor actor, double d)
		{
			if (d <= ActorTarget._maxLen2)
			{
				if (actor instanceof BridgeSegment)
					actor = actor.getOwner();
				else
					return true;
				if (ActorTarget._Actor == null || d < ActorTarget._Len2)
				{
					ActorTarget._Actor = actor;
					ActorTarget._Len2 = d;
				}
			}
			return true;
		}
	}
	
	public Actor getTarget()
	{
		return target;
	}
	
	public void align()
	{
		alignPosToLand(0.0, true);
	}
	
	public Object getSwitchListener(Message message)
	{
		return this;
	}
	
	public ActorTarget(Point3d point3d, int i, String string, int i_0_)
	{
		flags |= 0x2000;
		pos = new ActorPosMove(this);
		pos.setAbs(point3d);
		align();
		drawing(true);
		type = i;
		if (i == 3 || i == 6 || i == 7)
		{
			bTimeout = true;
			timeout = 30;
		}
		
		//TODO: Added by |ZUTI|
		//-------------------------------------------
		if( i == 1 || i == 6 )
		{
			if( i_0_ == 1 )
				this.zutiAllowHousesTargeting = true;
			else
				this.zutiAllowHousesTargeting = false;
		}
		//-------------------------------------------
		
		if (i != 1 && i != 6)
		{
			if (string == null)
			{
				if (i == 2 || i == 7)
					target = selectBridge(10000.0);
				else if (i == 4)
					target = selectAir(10000.0);
				else if (i == 5)
					target = selectChief(10000.0);
				else if (i == 3)
					target = selectChief(1000.0);
				else
					target = selectMoved(10000.0);
				if (target == null && i != 3)
					throw new RuntimeException("target NOT found");
			}
			else
			{
				target = Actor.getByName(string);
				if (target == null)
					throw new RuntimeException("target NOT found");
				if (i == 2 || i == 7)
				{
					if (!(target instanceof Bridge))
						throw new RuntimeException("target NOT found");
				}
				else if (i == 4)
				{
					if (!(target instanceof PathAir))
						throw new RuntimeException("target NOT found");
					target = ((Path)target).point(i_0_);
				}
				else if (i == 5)
				{
					if (!(target instanceof PathChief))
						throw new RuntimeException("target NOT found");
					target = ((Path)target).point(i_0_);
				}
				else if (i == 3)
				{
					if (!(target instanceof PathChief))
						throw new RuntimeException("target NOT found");
					target = ((Path)target).point(i_0_);
				}
				else
				{
					if (!(target instanceof Path))
						throw new RuntimeException("target NOT found");
					target = ((Path)target).point(i_0_);
				}
			}
		}
		switch (i)
		{
			case 0 :
				if (target instanceof PAir)
					icon = IconDraw.get("icons/tdestroyair.mat");
				else
					icon = IconDraw.get("icons/tdestroychief.mat");
				break;
			case 1 :
				icon = IconDraw.get("icons/tdestroyground.mat");
				r = 500;
				break;
			case 2 :
				icon = IconDraw.get("icons/tdestroybridge.mat");
				break;
			case 3 :
				icon = IconDraw.get("icons/tinspect.mat");
				r = 500;
				break;
			case 4 :
				icon = IconDraw.get("icons/tescort.mat");
				break;
			case 5 :
				icon = IconDraw.get("icons/tdefence.mat");
				break;
			case 6 :
				icon = IconDraw.get("icons/tdefenceground.mat");
				r = 500;
				break;
			case 7 :
				icon = IconDraw.get("icons/tdefencebridge.mat");
				break;
		}
	}
	
	protected void createActorHashCode()
	{
		makeActorRealHashCode();
	}
	
	private Actor selectBridge(double d)
	{
		_Actor = null;
		_maxLen2 = d * d;
		Point3d point3d = pos.getAbsPoint();
		Engine.drawEnv().getFiltered((AbstractCollection)null, point3d.x - d, point3d.y - d, point3d.x + d, point3d.y + d, 2, _selectBridge);
		Actor actor = _Actor;
		_Actor = null;
		return actor;
	}
	
	private Actor selectAir(double d)
	{
		_Actor = null;
		_maxLen2 = d * d;
		Point3d point3d = pos.getAbsPoint();
		Engine.drawEnv().getFiltered((AbstractCollection)null, point3d.x - d, point3d.y - d, point3d.x + d, point3d.y + d, 12, _selectAir);
		Actor actor = _Actor;
		_Actor = null;
		return actor;
	}
	
	private Actor selectChief(double d)
	{
		_Actor = null;
		_maxLen2 = d * d;
		Point3d point3d = pos.getAbsPoint();
		Engine.drawEnv().getFiltered((AbstractCollection)null, point3d.x - d, point3d.y - d, point3d.x + d, point3d.y + d, 12, _selectChief);
		Actor actor = _Actor;
		_Actor = null;
		return actor;
	}
	
	private Actor selectMoved(double d)
	{
		_Actor = null;
		_maxLen2 = d * d;
		Point3d point3d = pos.getAbsPoint();
		Engine.drawEnv().getFiltered((AbstractCollection)null, point3d.x - d, point3d.y - d, point3d.x + d, point3d.y + d, 12, _selectMoved);
		Actor actor = _Actor;
		_Actor = null;
		return actor;
	}
	
	//TODO: |ZUTI| methods and variables
	//----------------------------------------------------------------------
	public boolean zutiAllowHousesTargeting = false;
	//----------------------------------------------------------------------
}