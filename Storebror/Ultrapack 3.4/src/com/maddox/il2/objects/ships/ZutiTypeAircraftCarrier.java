package com.maddox.il2.objects.ships;

import com.maddox.il2.builder.ZutiTypeFrontCarrier;
import com.maddox.il2.game.ZutiTypeRRR;

public interface ZutiTypeAircraftCarrier extends ZutiTypeRRR, ZutiTypeWarShip, ZutiTypeFrontCarrier {
    int getSpawnPoints();

    /**
     * 1 = USS generic deck/CV2, CV3, 2 = USS intrepid/essex, CV9, CV10..., 3 = CVE, 4 = CVL, 5 = HMS, 6 = Akagi, 7 = IJN Generic, remaining ones
     *
     * @return
     */
    int getDeckTypeId();
}
