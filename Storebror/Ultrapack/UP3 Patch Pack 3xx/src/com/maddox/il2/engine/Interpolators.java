/*4.10.1 class*/
package com.maddox.il2.engine;

public final class Interpolators
{
	private long timeSleep;
	private com.maddox.il2.engine.ArrayInterpolators interp;
	private int stepStamp;
	private boolean bForceRef;
	private boolean bDoTick;
	
	private void checkFlagForceRef()
	{
		bForceRef = false;
		int i = interp.size();
		for (int j = 0; j < i; j++)
		{
			if (!(interp.get(j) instanceof com.maddox.il2.engine.InterpolateRef))
				continue;
			bForceRef = true;
			break;
		}
	}

	public boolean isSleep()
	{
		return timeSleep != 0L;
	}

	public boolean sleep()
	{
		if (isSleep())
			return false;
		int i = interp.modCount();
		int j = interp.size();
		for (int k = 0; k < j; k++)
		{
			com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) interp.get(k);
			interpolate.sleep();
			if (i != interp.modCount())
				throw new ActorException("Interpolators changed in 'sleep'");
		}

		timeSleep = com.maddox.rts.Time.current();
		return true;
	}

	public boolean wakeup()
	{
		if (!isSleep())
			return false;
		int i = interp.modCount();
		int j = interp.size();
		for (int k = 0; k < j; k++)
		{
			com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) interp.get(k);
			interpolate.wakeup(timeSleep);
			if (i != interp.modCount())
				throw new ActorException("Interpolators changed in 'wakeup'");
		}

		timeSleep = 0L;
		return true;
	}

	public com.maddox.il2.engine.Interpolate get(java.lang.Object obj)
	{
		int i = interp.size();
		for (int j = 0; j < i; j++)
		{
			com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) interp.get(j);
			if (interpolate.id == obj || obj != null && obj.equals(interpolate.id))
				return interpolate;
		}

		return null;
	}

	public boolean end(java.lang.Object obj)
	{
		int i = interp.size();
		for (int j = 0; j < i; j++)
		{
			com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) interp.get(j);
			if (interpolate.id == obj || obj != null && obj.equals(interpolate.id))
			{
				interp.remove(j);
				if (interpolate.bExecuted)
					interpolate.end();
				interplateClean(interpolate);
				checkFlagForceRef();
				return true;
			}
		}

		return false;
	}

	public boolean end(com.maddox.il2.engine.Interpolate interpolate)
	{
		int i = interp.indexOf(interpolate);
		if (i >= 0)
		{
			interp.remove(i);
			if (interpolate.bExecuted)
				interpolate.end();
			interplateClean(interpolate);
			checkFlagForceRef();
			return true;
		}
		else
		{
			return false;
		}
	}

	public void endAll()
	{
		com.maddox.il2.engine.Interpolate interpolate;
		for (; interp.size() > 0; interplateClean(interpolate))
		{
			interpolate = (com.maddox.il2.engine.Interpolate) interp.get(0);
			interp.remove(0);
			if (interpolate.bExecuted)
				interpolate.end();
		}

		checkFlagForceRef();
	}

	public boolean cancel(java.lang.Object obj)
	{
		int i = interp.size();
		for (int j = 0; j < i; j++)
		{
			com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) interp.get(j);
			if (interpolate.id == obj || obj != null && obj.equals(interpolate.id))
			{
				interp.remove(j);
				if (interpolate.bExecuted)
					interpolate.cancel();
				interplateClean(interpolate);
				checkFlagForceRef();
				return true;
			}
		}

		return false;
	}

	public boolean cancel(com.maddox.il2.engine.Interpolate interpolate)
	{
		int i = interp.indexOf(interpolate);
		if (i >= 0)
		{
			interp.remove(i);
			if (interpolate.bExecuted)
				interpolate.cancel();
			interplateClean(interpolate);
			checkFlagForceRef();
			return true;
		}
		else
		{
			return false;
		}
	}

	public void cancelAll()
	{
		com.maddox.il2.engine.Interpolate interpolate;
		for (; interp.size() > 0; interplateClean(interpolate))
		{
			interpolate = (com.maddox.il2.engine.Interpolate) interp.get(0);
			interp.remove(0);
			if (interpolate.bExecuted)
				interpolate.cancel();
		}

		checkFlagForceRef();
	}

	public void put(com.maddox.il2.engine.Interpolate interpolate, java.lang.Object obj, long l, com.maddox.rts.Message message, com.maddox.il2.engine.Actor actor)
	{
		if (obj != null && get(obj) != null)
		{
			throw new ActorException("Interpolator: '" + obj + "' alredy exist");
		}
		else
		{
			interpolate.actor = actor;
			interpolate.timeBegin = l;
			interpolate.id = obj;
			interpolate.msgEnd = message;
			interpolate.bExecuted = false;
			interp.add(interpolate);
			checkFlagForceRef();
			return;
		}
	}

	public int size()
	{
		return interp.size();
	}

	public void tick(long l)
	{
		if (timeSleep == 0L)
		{
			if (stepStamp == com.maddox.il2.engine.InterpolateAdapter.step())
				return;
			bDoTick = true;
			int i = interp.size();
			if (bForceRef)
			{
				for (int j = 0; j < i; j++)
				{
					com.maddox.il2.engine.Interpolate interpolate = (com.maddox.il2.engine.Interpolate) interp.get(j);
					if (interpolate instanceof com.maddox.il2.engine.InterpolateRef)
						((com.maddox.il2.engine.InterpolateRef) interpolate).invokeRef();
				}

			}
			int k = interp.modCount();
			for (int i1 = 0; i1 < i; i1++)
			{
				com.maddox.il2.engine.Interpolate interpolate1 = (com.maddox.il2.engine.Interpolate) interp.get(i1);
				if (!interpolate1.bExecuted && interpolate1.timeBegin != -1L && interpolate1.timeBegin <= l)
				{
					interpolate1.bExecuted = true;
					interpolate1.begin();
					if (k != interp.modCount())
					{
						bDoTick = false;
						throw new ActorException("Interpolators changed in 'begin'");
					}
				}
				if (interpolate1.bExecuted)
					if (!interpolate1.tick())
					{
						if (k != interp.modCount())
						{
							bDoTick = false;
							throw new ActorException("Interpolators changed in 'tick'");
						}
						interp.remove(i1);
						k = interp.modCount();
						interpolate1.end();
						if (k != interp.modCount())
						{
							bDoTick = false;
							throw new ActorException("Interpolators changed in 'end'");
						}
						interplateClean(interpolate1);
						checkFlagForceRef();
						i1--;
						i--;
					}
					else if (k != interp.modCount())
					{
						bDoTick = false;
						
						//TODO: Disabled by |ZUTI|
						//throw new ActorException("Interpolators changed in 'tick'");
					}
			}

			stepStamp = com.maddox.il2.engine.InterpolateAdapter.step();
			bDoTick = false;
		}
	}

	private void interplateClean(com.maddox.il2.engine.Interpolate interpolate)
	{
		if (interpolate.actor == null)
		{
			return;
		}
		else
		{
			interpolate.doDestroy();
			interpolate.actor = null;
			interpolate.id = null;
			interpolate.msgEnd = null;
			return;
		}
	}

	public void destroy()
	{
		if (bDoTick)
		{
		    // TODO: Storebror:
		    // Disabled this log entry since it serves no purpose, gives useless "information" only.
		    // In return, it keeps the game from exiting due to a log flood caused in case of game lockups.
//			throw new ActorException("Interpolators destroying in invoked method 'tick' ");
		}
		else
		{
			cancelAll();
			interp = null;
			return;
		}
	}

	public Interpolators()
	{
		stepStamp = -1;
		bForceRef = false;
		bDoTick = false;
		timeSleep = 0L;
		interp = new ArrayInterpolators(2);
	}
}