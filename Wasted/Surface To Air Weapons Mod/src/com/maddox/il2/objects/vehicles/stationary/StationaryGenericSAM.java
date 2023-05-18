package com.maddox.il2.objects.vehicles.stationary;


import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Time;

public abstract class StationaryGenericSAM extends StationaryGeneric
{
    class Move extends Interpolate {

        public boolean tick() {
        	StationaryGenericSAM.this.Target();
            return true;
        }

        Move() {
        }
    }
    
    public void Target() {
    }
    
    public StationaryGenericSAM()
    {
    	 if (!this.interpEnd("move")) {
             this.interpPut(new Move(), "move", Time.current(), null);
         }
    }
}

