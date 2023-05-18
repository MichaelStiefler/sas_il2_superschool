/*4.10.1 class*/
package com.maddox.il2.ai;

import com.maddox.JGP.Point3d;
import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorPosStatic;
import com.maddox.il2.engine.Landscape;
import com.maddox.il2.engine.Orient;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.objects.buildings.House;

class TDefenceGround extends Target
{
    public double r;
    public int countActors;
    public int destructLevel;
    private static Point3d p = new Point3d();

    protected boolean checkActorDied(Actor actor)
    {
        if(actor.pos == null)
            return false;
        
        //TODO: Changed by |ZUTI|: added last part - || (zutiCountHouses && !(actor instanceof House))
		//if( !Target.isStaticActor(actor) )
		if( !Target.isStaticActor(actor) && ( !zutiCountMapObjects || (zutiCountMapObjects && !(actor instanceof House)) ) )
		{
			//System.out.println("TDefenceGround returning false while checking if actor is dead...");
			return false;
		}
        
        actor.pos.getAbs(p);
        p.z = pos.getAbsPoint().z;
        if(pos.getAbsPoint().distance(p) <= r)
        {
        	countActors--;
            if(countActors <= 0)
            {
                setDiedFlag(true);
                
                return true;
            }
        }
        return false;
    }

    protected boolean checkTimeoutOff()
    {
        setTaskCompleteFlag(true);
        setDiedFlag(true);
        return true;
    }

    /**
	 * 
	 * @param i: importance
	 * @param j: timeout
	 * @param k: x coordinate
	 * @param l: y coordinate
	 * @param i1: radius
	 * @param j1: destruction level
	 */
    public TDefenceGround(int i, int j, int k, int l, int i1, int j1)
    {
        super(i, j);
        
		//TODO: Added by |ZUTI|
        //---------------------
		zutiTimeout = j;
        //---------------------
		
        countActors = 0;
        r = i1;
        destructLevel = j1;
        com.maddox.il2.ai.World.land();
        pos = new ActorPosStatic(this, new Point3d(k, l, Landscape.HQ(k, l)), new Orient());
        countActors = Target.countStaticActors(pos.getAbsPoint(), r);
        if(countActors == 0)
        {
            setTaskCompleteFlag(true);
        }
		else
        {
            countActors = Math.round((float)(countActors * destructLevel) / 100F);
            if(countActors == 0)
                countActors = 1;
        }
    }
    
    //TODO: |ZUTI| Methods and variables
	//---------------------------------------------------------------------------------------------------------------------------------------
	public int zutiTimeout = -1;
	private boolean zutiCountMapObjects = true;
	
	/**
	 * |ZUTI| defined constructor.
	 * @param i
	 * @param j
	 * @param k
	 * @param l
	 * @param i1
	 * @param j1
	 * @param zutiCountHouses
	 */
	public TDefenceGround(int i, int j, int k, int l, int i1, int j1, int zutiCountHouses)
    {
        super(i, j);
		zutiTimeout = j;
        countActors = 0;
        r = i1;
        destructLevel = j1;
        com.maddox.il2.ai.World.land();
        pos = new ActorPosStatic(this, new Point3d(k, l, Landscape.HQ(k, l)), new Orient());
        countActors = Target.countStaticActors(pos.getAbsPoint(), r);
        
        //Added by |ZUTI|
		//-----------------------------------------
		if( this.zutiCountMapObjects )
		{
			countActors += ZutiSupportMethods.countHousesInArea(this.pos.getAbsPoint(), r);
			//System.out.println("TDefenceGround - found actors (including houses): " + countActors);
		}
		//-----------------------------------------
        
        if(countActors == 0)
        {
            setTaskCompleteFlag(true);
            setDiedFlag(true);
        }
		else
        {
            countActors = Math.round((float)(countActors * destructLevel) / 100F);
            
            System.out.println("TDefenceGround " + this.pos.getAbsPoint().toString() + " - Objects to defend " + countActors);
            
            if(countActors == 0)
                countActors = 1;
        }
    }
	
	/**
	 * Check specified coordinates and return true if they are inside target radius.
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean zutiIsOverTarged(double x, double y)
	{
		double max = r * r;
		double tmpDistance = (((x - p.x) * (x - p.x)) + ((y - p.y) * (y - p.y)));
		if( tmpDistance < max )
			return true;
		
		return false;
	}
	
	/**
	 * Method checks if target allows targeting of actors.static objects.
	 * @return
	 */
	public boolean zutiIsTargetingOfMapObjectsAllowed()
	{
		return zutiCountMapObjects;
	}
	//---------------------------------------------------------------------------------------------------------------------------------------
}