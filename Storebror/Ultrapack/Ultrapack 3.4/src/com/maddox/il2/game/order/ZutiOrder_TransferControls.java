package com.maddox.il2.game.order;

import com.maddox.il2.ai.World;
import com.maddox.il2.fm.RealFlightModel;
import com.maddox.il2.game.HUD;
import com.maddox.il2.game.Mission;
import com.maddox.il2.net.ZutiSupportMethods_NetSend;

public class ZutiOrder_TransferControls extends com.maddox.il2.game.order.Order {
    private boolean isRetakeCommand = false;

    public ZutiOrder_TransferControls(String name) {
        super(name);
    }

    public void setName(String name) {
        super.name = name;
    }

    public void setNameDE(String name) {
        super.name = this.nameDE;
    }

    public void setIsRetakeCommand(boolean value) {
        this.isRetakeCommand = value;
    }

    public void run() {
        try {
            if (Mission.MDS_VARIABLES().zutiMisc_EnableInstructor && World.getPlayerFM() instanceof RealFlightModel && !World.isPlayerGunner()) {
                ZutiSupportMethods_NetSend.transferControls_ToServer(this.name);

                if (!this.isRetakeCommand) // RTSConf.cur.joy.setEnable(false);
                    // Controls are no longer in our domain...
                    HUD.log("mds.instructor.disabled");
                else // RTSConf.cur.joy.setEnable(true);
                     // We have re-taken control over aircraft.
                    HUD.log("mds.instructor.enabled");
            } else HUD.log("mds.instructorNotEnabled");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}