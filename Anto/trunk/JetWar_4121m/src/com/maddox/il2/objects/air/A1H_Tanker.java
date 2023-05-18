// Source File Name: A1H_Tanker.java
// Author:           
// Last Modified by: Storebror 2011-06-01
package com.maddox.il2.objects.air;

import com.maddox.il2.engine.Actor;
import com.maddox.il2.engine.ActorMesh;
import com.maddox.il2.engine.ActorNet;
import com.maddox.il2.engine.HookNamed;
import com.maddox.il2.engine.Loc;
import com.maddox.rts.NetMsgGuaranted;
import com.maddox.rts.NetMsgInput;
import com.maddox.rts.NetObj;

import java.io.IOException;

public class A1H_Tanker extends AD_Tanker
        implements TypeDockable {

  public A1H_Tanker() {
  }

  public boolean typeDockableIsDocked() {
    return true;
  }

  public void typeDockableAttemptAttach() {
  }

  public void typeDockableAttemptDetach() {
    if (FM.AS.isMaster()) {
      for (int i = 0; i < drones.length; i++) {
        if (Actor.isValid(drones[i])) {
          typeDockableRequestDetach(drones[i], i, true);
        }
      }

    }
  }

  public void typeDockableRequestAttach(Actor actor) {
    if (actor instanceof Aircraft) {
      Aircraft aircraft = (Aircraft) actor;
      if (aircraft.FM.AS.isMaster() && aircraft.FM.getSpeedKMH() > 10F && FM.getSpeedKMH() > 10F && isDrogueExtended()) {
        for (int i = 0; i < drones.length; i++) {
          if (Actor.isValid(drones[i])) {
            continue;
          }
          HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
          Loc loc = new Loc();
          Loc loc1 = new Loc();
          pos.getAbs(loc1);
          hooknamed.computePos(this, loc1, loc);
          actor.pos.getAbs(loc1);
          if (loc.getPoint().distance(loc1.getPoint()) >= 7.5D) {
            continue;
          }
          if (FM.AS.isMaster()) {
            typeDockableRequestAttach(actor, i, true);
          } else {
            FM.AS.netToMaster(32, i, 0, actor);
          }
          break;
        }

      }
    }
  }

  public void typeDockableRequestDetach(Actor actor) {
    for (int i = 0; i < drones.length; i++) {
      if (actor != drones[i]) {
        continue;
      }
      Aircraft aircraft = (Aircraft) actor;
      if (!aircraft.FM.AS.isMaster()) {
        continue;
      }
      if (FM.AS.isMaster()) {
        typeDockableRequestDetach(actor, i, true);
      } else {
        FM.AS.netToMaster(33, i, 1, actor);
      }
    }

  }

  public void typeDockableRequestAttach(Actor actor, int i, boolean flag) {
    if (i >= 0 && i <= 1) {
      if (flag) {
        if (FM.AS.isMaster()) {
          FM.AS.netToMirrors(34, i, 1, actor);
          typeDockableDoAttachToDrone(actor, i);
        } else {
          FM.AS.netToMaster(34, i, 1, actor);
        }
      } else if (FM.AS.isMaster()) {
        if (!Actor.isValid(drones[i])) {
          FM.AS.netToMirrors(34, i, 1, actor);
          typeDockableDoAttachToDrone(actor, i);
        }
      } else {
        FM.AS.netToMaster(34, i, 0, actor);
      }
    }
  }

  public void typeDockableRequestDetach(Actor actor, int i, boolean flag) {
    if (flag) {
      if (FM.AS.isMaster()) {
        FM.AS.netToMirrors(35, i, 1, actor);
        typeDockableDoDetachFromDrone(i);
      } else {
        FM.AS.netToMaster(35, i, 1, actor);
      }
    }
  }

  public void typeDockableDoAttachToDrone(Actor actor, int i) {
    if (!Actor.isValid(drones[i])) {
      HookNamed hooknamed = new HookNamed(this, "_Dockport" + i);
      Loc loc = new Loc();
      Loc loc1 = new Loc();
      pos.getAbs(loc1);
      hooknamed.computePos(this, loc1, loc);
      HookNamed probe = new HookNamed((ActorMesh) actor, "_Probe");
      Loc loc2 = new Loc(); //Probe location
      probe.computePos(this, loc, loc2);
      actor.pos.setAbs(loc2);
      actor.pos.setBase(this, null, true);
      actor.pos.resetAsBase();
      drones[i] = actor;
      ((TypeDockable) drones[i]).typeDockableDoAttachToQueen(this, i);
    }
  }

  public void typeDockableDoDetachFromDrone(int i) {
    if (Actor.isValid(drones[i])) {
      drones[i].pos.setBase(null, null, true);
      ((TypeDockable) drones[i]).typeDockableDoDetachFromQueen(i);
      drones[i] = null;
    }
  }

  public void typeDockableDoAttachToQueen(Actor actor, int i) {
  }

  public void typeDockableDoDetachFromQueen(int i) {
  }

  public void typeDockableReplicateToNet(NetMsgGuaranted netmsgguaranted)
          throws IOException {
    for (int i = 0; i < drones.length; i++) {
      if (Actor.isValid(drones[i])) {
        netmsgguaranted.writeByte(1);
        ActorNet actornet = drones[i].net;
        if (actornet.countNoMirrors() == 0) {
          netmsgguaranted.writeNetObj(actornet);
        } else {
          netmsgguaranted.writeNetObj(null);
        }
      } else {
        netmsgguaranted.writeByte(0);
      }
    }

  }

  public void typeDockableReplicateFromNet(NetMsgInput netmsginput)
          throws IOException {
    for (int i = 0; i < drones.length; i++) {
      if (netmsginput.readByte() != 1) {
        continue;
      }
      NetObj netobj = netmsginput.readNetObj();
      if (netobj != null) {
        typeDockableDoAttachToDrone((Actor) netobj.superObj(), i);
      }
    }

  }

  protected boolean cutFM(int i, int j, Actor actor) {
    if (FM.AS.isMaster()) {
      switch (i) {
        case 33: // '!'
        case 34: // '"'
        case 35: // '#'
          typeDockableRequestDetach(drones[0], 0, true);
          break;

        case 36: // '$'
        case 37: // '%'
        case 38: // '&'
          typeDockableRequestDetach(drones[1], 1, true);
          break;
      }
    }
    return super.cutFM(i, j, actor);
  }
  private Actor drones[] = {
    null, null
  };

  static {
    java.lang.Class class1 = com.maddox.il2.objects.air.A1H_Tanker.class;
    new NetAircraft.SPAWN(class1);
    com.maddox.rts.Property.set(class1, "iconFar_shortClassName", "A1H");
    com.maddox.rts.Property.set(class1, "meshName", "3DO/Plane/A1H_Tanker(multi1)/hier.him");
    com.maddox.rts.Property.set(class1, "PaintScheme", new PaintSchemeFMPar06());
    com.maddox.rts.Property.set(class1, "yearService", 1945F);
    com.maddox.rts.Property.set(class1, "yearExpired", 1948.5F);
    com.maddox.rts.Property.set(class1, "FlightModel", "FlightModels/A1H.fmd");
    com.maddox.rts.Property.set(class1, "LOSElevation", 1.0585F);
    com.maddox.il2.objects.air.Aircraft.weaponTriggersRegister(class1, new int[]{
              1, 1, 1, 1, 9, 9
            });
    com.maddox.il2.objects.air.Aircraft.weaponHooksRegister(class1, new java.lang.String[]{
              "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_ExternalDev17", "_ExternalDev18"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "default", new java.lang.String[]{
              "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", null, null
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "2x DT", new java.lang.String[]{
              "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", "MGunHispanoMkIk 200", "FuelTankGun_TankAD4 1", "FuelTankGun_TankAD4 1"
            });
    com.maddox.il2.objects.air.Aircraft.weaponsRegister(class1, "none", new java.lang.String[]{
              null, null, null, null, null, null
            });
  }
}