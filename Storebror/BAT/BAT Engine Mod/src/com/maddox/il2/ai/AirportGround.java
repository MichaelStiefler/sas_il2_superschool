// Last edited 09th/Apr./2018 By western: Add custom Runway Lights keep seconds function
package com.maddox.il2.ai;

import java.util.*;
import com.maddox.il2.objects.air.*;
import com.maddox.il2.game.*;
import com.maddox.il2.objects.vehicles.stationary.*;
import java.io.*;
import com.maddox.il2.net.*;
import com.maddox.il2.engine.*;
import com.maddox.rts.*;

public class AirportGround extends AirportStatic
{
    private ArrayList runwayLights;
    private boolean lightsOn;
    private Aircraft acThatRequestedLights;
    private boolean aircraftIsTakingOff;
    private boolean canTurnOffLights;
    private boolean canTurnOnLights;
    private int randomDelay;
    private long timeOfLightsOn;
    private static long MAX_LIGHTS_ON_TIME_MS = 180000L;  // default 90000;

    public AirportGround() {
        super();
        this.runwayLights = null;
        this.lightsOn = false;
        this.acThatRequestedLights = null;
        this.aircraftIsTakingOff = false;
        this.canTurnOffLights = false;
        this.canTurnOnLights = false;
        this.randomDelay = 0;
        this.timeOfLightsOn = 0L;
        this.net = null;
        if (Mission.cur() != null && (!NetMissionTrack.isPlaying() || NetMissionTrack.playingOriginalVersion() > 102)) {
            final int unitNetIdRemote = Mission.cur().getUnitNetIdRemote(this);
            final NetChannel netMasterChannel = Mission.cur().getNetMasterChannel();
            if (netMasterChannel == null) {
                this.net = new Master(this);
            }
            else if (unitNetIdRemote != 0) {
                this.net = new Mirror(this, netMasterChannel, unitNetIdRemote);
            }
        }
        // By western: custom Runway Lights keep seconds
        int sec = Config.cur.ini.get("Mods", "RunwayLightsKeepOnSeconds", -1);
        if (sec > 1800)
            sec = 1800;
        if (sec >= 60)
            MAX_LIGHTS_ON_TIME_MS = (long) sec * 1000L;
    }

    public void destroy() {
        if (this.runwayLights != null) {
            this.runwayLights.clear();
        }
        super.destroy();
    }

    protected void update() {
        super.update();
        if (Mission.cur() == null) {
            return;
        }
        if (NetMissionTrack.isPlaying() && NetMissionTrack.playingOriginalVersion() <= 102) {
            return;
        }
        if (this.net.isMirror()) {
            return;
        }
        if (this.lightsOn) {
            if (this.canTurnOffLights) {
                if (this.randomDelay < 0) {
                    this.turnOnLights(false);
                }
                else {
                    --this.randomDelay;
                }
            }
            else if (this.acThatRequestedLights != null && this.aircraftIsTakingOff) {
                if (!this.acThatRequestedLights.FM.Gears.onGround() || !this.acThatRequestedLights.isAlive()) {
                    this.randomDelay = 300 + World.Rnd().nextInt(800);
                    this.canTurnOffLights = true;
                }
                else if (Time.current() > this.timeOfLightsOn + MAX_LIGHTS_ON_TIME_MS) {
                    this.canTurnOffLights = true;
                    this.turnOnLights(false);
                }
            }
            else if (this.acThatRequestedLights != null && !this.aircraftIsTakingOff) {
                if (this.acThatRequestedLights.FM.Gears.onGround() || !this.acThatRequestedLights.isAlive()) {
                    this.randomDelay = 300 + World.Rnd().nextInt(800);
                    this.canTurnOffLights = true;
                }
                else if (Time.current() > this.timeOfLightsOn + MAX_LIGHTS_ON_TIME_MS) {
                    this.canTurnOffLights = true;
                    this.turnOnLights(false);
                }
            }
            else if (Time.current() > this.timeOfLightsOn + MAX_LIGHTS_ON_TIME_MS) {
                this.canTurnOffLights = true;
                this.turnOnLights(false);
            }
        }
        else if (this.canTurnOnLights) {
            if (this.randomDelay < 0) {
                this.canTurnOnLights = false;
                this.turnOnLights(true);
                this.timeOfLightsOn = Time.current();
            }
            else {
                --this.randomDelay;
            }
        }
    }

    public boolean hasLights() {
        return this.runwayLights != null && this.runwayLights.size() > 0;
    }

    public void addLights(final SmokeGeneric smokeGeneric) {
        if (runwayLights == null) {
            runwayLights = new ArrayList();
        }
        runwayLights.add(smokeGeneric);
        smokeGeneric.setArmy(0);
    }

    public void addLights(VisualLandingAidGeneric vlageneric)
    {
        if(runwayLights == null)
            runwayLights = new ArrayList();
        runwayLights.add(vlageneric);
        vlageneric.setArmy(0);
    }

    private void turnOnLights(final boolean flag) {
        this.lightsOn = flag;
        if (!this.net.isMirror()) {
            this.master_sendLights(this.lightsOn);
        }
        if (this.runwayLights != null) {
            for (int i = 0; i < this.runwayLights.size(); ++i) {
                Actor actor = (Actor) runwayLights.get(i);
                if(actor instanceof SmokeGeneric){
                    SmokeGeneric smokegeneric = (SmokeGeneric) actor;
                    smokegeneric.setVisible(flag);
                }
                else if(actor instanceof VisualLandingAidGeneric){
                    VisualLandingAidGeneric vlageneric = (VisualLandingAidGeneric) actor;
                    vlageneric.setVisible(flag);
                }
            }
        }
    }

    public void turnOnLights(final Aircraft acThatRequestedLights) {
        if (this.net.isMirror()) {
            this.mirror_sendLights(acThatRequestedLights);
            return;
        }
        this.canTurnOnLights = true;
        this.canTurnOffLights = false;
        this.acThatRequestedLights = acThatRequestedLights;
        if (this.acThatRequestedLights != null && this.acThatRequestedLights.FM.Gears.onGround()) {
            this.aircraftIsTakingOff = true;
        }
        else {
            this.aircraftIsTakingOff = false;
        }
        this.randomDelay = 200 + World.Rnd().nextInt(200);
    }

    public void netFirstUpdate(final NetChannel netChannel) throws IOException {
        try {
            final NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(80);
            netMsgGuaranted.writeBoolean(this.lightsOn);
            this.net.postTo(netChannel, netMsgGuaranted);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Airport lights: NetFirstUpdate failed");
        }
    }

    private boolean master_sendLights(final boolean b) {
        if (this.net.isMirror()) {
            return false;
        }
        try {
            final NetMsgGuaranted netMsgGuaranted = new NetMsgGuaranted();
            netMsgGuaranted.writeByte(80);
            netMsgGuaranted.writeBoolean(b);
            this.net.post(netMsgGuaranted);
            return true;
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    private boolean mirror_sendLights(final Aircraft aircraft) {
        if (!this.net.isMirror() || this.net.masterChannel() instanceof NetChannelInStream) {
            return false;
        }
        try {
            final NetMsgFiltered netMsgFiltered = new NetMsgFiltered();
            netMsgFiltered.writeByte(81);
            netMsgFiltered.writeNetObj((aircraft == null) ? null : aircraft.net);
            netMsgFiltered.setIncludeTime(false);
            this.net.postTo(NetServerParams.getServerTime(), this.net.masterChannel(), netMsgFiltered);
            return true;
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    class Master extends ActorNet
    {
        public Master(final Actor actor) {
            super(actor);
        }

        public boolean netInput(final NetMsgInput netMsgInput) throws IOException {
            if (netMsgInput.isGuaranted()) {
                return true;
            }
            if (netMsgInput.readUnsignedByte() == 81) {
                final NetObj netObj = netMsgInput.readNetObj();
                AirportGround.this.acThatRequestedLights = ((netObj == null) ? null : ((Aircraft)((ActorNet)netObj).actor()));
                AirportGround.this.turnOnLights(AirportGround.this.acThatRequestedLights);
                return true;
            }
            return false;
        }
    }

    class Mirror extends ActorNet
    {
        NetMsgFiltered out;

        public Mirror(final Actor actor, final NetChannel netChannel, final int n) {
            super(actor, netChannel, n);
            this.out = new NetMsgFiltered();
        }

        public boolean netInput(final NetMsgInput netMsgInput) throws IOException {
            if (netMsgInput.isGuaranted()) {
                if (netMsgInput.readUnsignedByte() == 80) {
                    AirportGround.this.turnOnLights(netMsgInput.readBoolean());
                }
                return true;
            }
            return true;
        }
    }
}
