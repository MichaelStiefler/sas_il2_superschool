/*4.10.1 class*/
package com.maddox.il2.game.order;

import com.maddox.JGP.Point3d;
import com.maddox.il2.ai.Airport;
import com.maddox.il2.game.ZutiSupportMethods;
import com.maddox.il2.net.BornPlace;

class OrderRequest_For_Landing extends com.maddox.il2.game.order.Order {
    private static com.maddox.JGP.Point3d WP = new Point3d();

    public OrderRequest_For_Landing() {
        super("Request_For_Landing");
    }

    public void run() {
        try {
            com.maddox.il2.fm.FlightModel flightmodel = this.Player().FM;

            com.maddox.il2.ai.Way way = null;

            // TODO: Added by |ZUTI|
            // -------------------------------------------------
            Airport zutiAirport = null;

            if (com.maddox.il2.game.Main.cur().netServerParams.isDogfight()) {
                // Dogfight pilots don't have waypoints
                WP = this.Player().FM.actor.pos.getAbsPoint();

                // Get user born place
                BornPlace bp = ZutiSupportMethods.getPlayerBornPlace(WP, this.Player().getArmy());
                if (bp != null) zutiAirport = ZutiSupportMethods.getAirport(bp.place.x, bp.place.y);
            }
            // -------------------------------------------------
            else {
                way = flightmodel.AP.way;
                way.get(way.size() - 1).getP(WP);
            }

            flightmodel.BarometerZ = (float) com.maddox.il2.ai.World.land().HQ(WP.x, WP.y);

            int i = 0;
            // TODO: Added by |ZUTI|
            // -------------------------------------------------
            if (com.maddox.il2.game.Main.cur().netServerParams.isDogfight() && zutiAirport != null) i = zutiAirport.landingFeedback(WP, (com.maddox.il2.objects.air.Aircraft) flightmodel.actor);
            // -------------------------------------------------
            // Original
            else if (com.maddox.il2.engine.Actor.isAlive(way.landingAirport)) i = way.landingAirport.landingFeedback(WP, (com.maddox.il2.objects.air.Aircraft) flightmodel.actor);

            if (i == 0) com.maddox.il2.objects.sounds.Voice.speakLandingPermited((com.maddox.il2.objects.air.Aircraft) flightmodel.actor);
            if (i == 1) com.maddox.il2.objects.sounds.Voice.speakLandingDenied((com.maddox.il2.objects.air.Aircraft) flightmodel.actor);
            if (i == 2) com.maddox.il2.objects.sounds.Voice.speakWaveOff((com.maddox.il2.objects.air.Aircraft) flightmodel.actor);
        } catch (Exception ex) {
            System.out.println("OrderRequest_For_Landing error, ID_01: " + ex.toString());
        }
    }
}