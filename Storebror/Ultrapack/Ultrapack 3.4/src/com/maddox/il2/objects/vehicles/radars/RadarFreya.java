package com.maddox.il2.objects.vehicles.radars;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Interpolate;
import com.maddox.rts.Message;
import com.maddox.rts.Time;

public class RadarFreya extends RotatingRadarGeneric {
    class Move extends Interpolate {

        public boolean tick() {
            if (RadarFreya.this.isAlive()) {
                float f = RotatingRadarGeneric.cvt(Time.current() % 15000F, 0.0F, 15000F, 0.0F, 360F);
                RadarFreya.this.hierMesh().chunkSetAngles("Head_D0", -f - RadarFreya.this.pos.getAbsOrient().getYaw(), 0.0F, 0.0F);
            }
            return true;
        }

        Move() {
        }
    }

    public RadarFreya() {
        this.startRotate();
    }

    public void startRotate() {
        this.interpPut(new Move(), "move", Time.current(), (Message) null);
    }

    public void interpolateTick() {
        super.interpolateTick();
        if ((World.getPlayerAircraft() != null) && (this.dying == 0) && (Time.current() > (this.RefreshInterval + 30000F))) {
            this.PlayerAircraftSearch();
        }
    }
}
