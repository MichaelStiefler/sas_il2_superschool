package com.maddox.il2.objects.air;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.maddox.il2.ai.World;
import com.maddox.il2.engine.Config;
import com.maddox.il2.engine.Mat;
import com.maddox.il2.game.Main;
import com.maddox.il2.objects.weapons.MGunBK374Hs129;
import com.maddox.il2.objects.weapons.MGunMK101s_Hs129;
import com.maddox.il2.objects.weapons.MGunMK103s_Hs129;
import com.maddox.il2.objects.weapons.PylonHS129MG17S;
import com.maddox.rts.HomePath;
import com.maddox.rts.Property;

public class HS_129B2 extends HS_129 {

    public HS_129B2() {
    }

    public void missionStarting() {
        super.missionStarting();
        this.customization();
    }

    private void customization() {
        if (!Config.isUSE_RENDER()) {
            return;
        }
        int i = this.hierMesh().chunkFindCheck("cf_D0");
        int j = this.hierMesh().materialFindInChunk("Gloss1D0o", i);
        Mat mat = this.hierMesh().material(j);
        String s = mat.Name();
        if (s.startsWith("PaintSchemes/Cache")) {
            try {
                s = s.substring(19);
                s = s.substring(0, s.indexOf("/"));
                String s1 = Main.cur().netFileServerSkin.primaryPath();
                File file = new File(HomePath.toFileSystemName(s1 + "/Hs-129B-2/Customization.ini", 0));
                BufferedReader bufferedreader = new BufferedReader(new FileReader(file));
                boolean flag = false;
                boolean flag1 = false;
                boolean flag2 = false;
                do {
                    String s2;
                    if ((s2 = bufferedreader.readLine()) == null) {
                        break;
                    }
                    if (s2.equals("[SmoothNose]")) {
                        flag = true;
                        flag1 = false;
                        flag2 = false;
                    } else if (s2.equals("[NoMirror]")) {
                        flag = false;
                        flag1 = true;
                        flag2 = false;
                    } else if (s2.equals("[NoDFLoop]")) {
                        flag = false;
                        flag1 = false;
                        flag2 = true;
                    } else if (s2.equals(s)) {
                        if (flag) {
                            this.hierMesh().chunkVisible("NoseParts_D0", false);
                        }
                        if (flag1) {
                            this.hierMesh().chunkVisible("Mirror", false);
                        }
                        if (flag2) {
                            this.hierMesh().chunkVisible("DF_loop", false);
                        }
                    }
                } while (true);
                bufferedreader.close();
            } catch (Exception exception) {
                System.out.println(exception);
            }
        }
    }

    public void onAircraftLoaded() {
        super.onAircraftLoaded();
        Object aobj[] = this.pos.getBaseAttached();
        if (aobj != null) {
            for (int i = 0; i < aobj.length; i++) {
                if (aobj[i] instanceof MGunMK103s_Hs129) {
                    if (Config.isUSE_RENDER()) {
                        this.hierMesh().chunkVisible("cannonPod", true);
                        this.hierMesh().chunkVisible("Mk103_Barrel", true);
                    }
                    continue;
                }
                if (aobj[i] instanceof MGunMK101s_Hs129) {
                    if (Config.isUSE_RENDER()) {
                        this.hierMesh().chunkVisible("cannonPod", true);
                        this.hierMesh().chunkVisible("Mk101_Barrel", true);
                    }
                    continue;
                }
                if (aobj[i] instanceof MGunBK374Hs129) {
                    if (Config.isUSE_RENDER()) {
                        this.hierMesh().chunkVisible("BK37_dummy", true);
                    }
                    this.FM.Sq.liftKeel *= 1.5F;
                    continue;
                }
                if ((aobj[i] instanceof PylonHS129MG17S) && Config.isUSE_RENDER()) {
                    this.hierMesh().chunkVisible("MG17_dummy", true);
                }
            }

        }
        if (Config.isUSE_RENDER() && ((World.cur().camouflage == 2) || (World.cur().camouflage == 5))) {
            this.hierMesh().chunkVisible("NoseParts_D0", false);
        }
    }

    static {
        Class class1 = HS_129B2.class;
        new NetAircraft.SPAWN(class1);
        Property.set(class1, "iconFar_shortClassName", "Hs-129");
        Property.set(class1, "meshName", "3do/plane/Hs-129B-2/hier.him");
        Property.set(class1, "PaintScheme", new PaintSchemeFMPar02());
        Property.set(class1, "meshName_ro", "3do/plane/Hs-129B-2(ro)/hier.him");
        Property.set(class1, "PaintScheme_ro", new PaintSchemeFMPar02());
        Property.set(class1, "yearService", 1942F);
        Property.set(class1, "yearExpired", 1945F);
        Property.set(class1, "FlightModel", "FlightModels/Hs-129B-2.fmd");
        Property.set(class1, "cockpitClass", new Class[] { CockpitHS_129B2.class });
        Aircraft.weaponTriggersRegister(class1, new int[] { 0, 0, 0, 0, 1, 1, 1, 9, 1, 1, 1, 1, 3, 3, 3, 3, 3, 3, 3, 3, 9, 9 });
        Aircraft.weaponHooksRegister(class1, new String[] { "_MGUN01", "_MGUN02", "_CANNON01", "_CANNON02", "_CANNON03", "_CANNON04", "_HEAVYCANNON01", "_ExternalDev01", "_MGUN03", "_MGUN04", "_MGUN05", "_MGUN06", "_ExternalBomb01", "_ExternalBomb02", "_ExternalBomb03", "_ExternalBomb04", "_ExternalBomb05", "_ExternalBomb06", "_ExternalBomb07", "_ExternalBomb08", "_ExternalDev02", "_ExternalDev03" });
    }
}
