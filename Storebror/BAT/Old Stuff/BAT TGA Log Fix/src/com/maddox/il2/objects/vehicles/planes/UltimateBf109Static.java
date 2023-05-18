package com.maddox.il2.objects.vehicles.planes;

import com.maddox.il2.ai.World;

public abstract class UltimateBf109Static {
    public static class BF_109B1 extends PlaneGeneric {
    }

    public static class BF_109B2 extends PlaneGeneric {
        public BF_109B2() {
            if (World.rnd().nextFloat() > 0.66D) {
                this.hierMesh().chunkVisible("NoseCannon1_D0", true);
            }
        }
    }

    public static class BF_109C1 extends PlaneGeneric {
        public BF_109C1() {
            if (World.rnd().nextFloat() > 0.66D) {
                this.hierMesh().chunkVisible("NoseCannon1_D0", true);
            }
        }
    }

    public static class BF_109D1 extends PlaneGeneric {
        public BF_109D1() {
            if (World.rnd().nextFloat() > 0.66D) {
                this.hierMesh().chunkVisible("NoseCannon1_D0", true);
            }
        }
    }

    public static class BF_109E1 extends PlaneGeneric {
    }

    public static class BF_109E3 extends PlaneGeneric {
        public BF_109E3() {
            if (World.rnd().nextFloat() > 0.66D) {
                this.hierMesh().chunkVisible("Armor1_D0", true);
            } else if (World.rnd().nextFloat() > 0.66D) {
                this.hierMesh().chunkVisible("NoseCannon1_D0", true);
            }
        }
    }

    public static class BF_109E4N extends PlaneGeneric {
        public BF_109E4N() {
            if ((World.cur().camouflage == 2) || (World.cur().camouflage == 5)) {
                this.hierMesh().chunkVisible("FilterE_d0", true);
                this.hierMesh().chunkVisible("FilterEFlap_D0", true);
            }
        }
    }

// public static class BF_109E7N extends PlaneGeneric
// {
// public BF_109E7N()
// {
// if(World.cur().camouflage == 2 || World.cur().camouflage == 5)
// {
// hierMesh().chunkVisible("FilterE_d0", true);
// hierMesh().chunkVisible("FilterEFlap_D0", true);
// }
// }
// }
// public static class BF_109F0 extends PlaneGeneric
// {
// }
// public static class BF_109F2U extends PlaneGeneric
// {
// public BF_109F2U()
// {
// if((double)World.rnd().nextFloat() > 0.5D)
// {
// hierMesh().chunkVisible("Mg131_D0", true);
// hierMesh().chunkVisible("MgFFL_D0", false);
// hierMesh().chunkVisible("MgFFR_D0", false);
// } else
// {
// hierMesh().chunkVisible("Mg131_D0", false);
// hierMesh().chunkVisible("MgFFL_D0", true);
// hierMesh().chunkVisible("MgFFR_D0", true);
// }
// }
// }
// public static class BF_109F4Z extends PlaneGeneric
// {
// public BF_109F4Z()
// {
// if(World.cur().camouflage == 2 || World.cur().camouflage == 5)
// {
// hierMesh().chunkVisible("Filter_d0", true);
// hierMesh().chunkVisible("FilterFlap1_D0", true);
// hierMesh().chunkVisible("FilterFlap2_D0", true);
// }
// }
// }
// public static class BF_109G1 extends PlaneGeneric
// {
// public BF_109G1()
// {
// if(World.cur().camouflage == 2 || World.cur().camouflage == 5)
// {
// hierMesh().chunkVisible("Filter_d0", true);
// hierMesh().chunkVisible("FilterFlap1_D0", true);
// hierMesh().chunkVisible("FilterFlap2_D0", true);
// }
// }
// }
// public static class BF_109G10C3 extends PlaneGeneric
// {
// }
// public static class BF_109G10Erla extends PlaneGeneric
// {
// }
// public static class BF_109G14AS extends PlaneGeneric
// {
// }
// public static class BF_109G3 extends PlaneGeneric
// {
// public BF_109G3()
// {
// if(World.cur().camouflage == 2 || World.cur().camouflage == 5)
// {
// hierMesh().chunkVisible("Filter_d0", true);
// hierMesh().chunkVisible("FilterFlap1_D0", true);
// hierMesh().chunkVisible("FilterFlap2_D0", true);
// }
// }
// }
// public static class BF_109G5 extends PlaneGeneric
// {
// public BF_109G5()
// {
// if(World.cur().camouflage == 2 || World.cur().camouflage == 5)
// {
// hierMesh().chunkVisible("Filter_d0", true);
// hierMesh().chunkVisible("FilterFlap1_D0", true);
// hierMesh().chunkVisible("FilterFlap2_D0", true);
// }
// }
// }
// public static class BF_109G5AS extends PlaneGeneric
// {
// }
// public static class BF_109G6Erla extends PlaneGeneric
// {
// public BF_109G6Erla()
// {
// if(World.cur().camouflage == 2 || World.cur().camouflage == 5)
// {
// hierMesh().chunkVisible("Filter_d0", true);
// hierMesh().chunkVisible("FilterFlap1_D0", true);
// hierMesh().chunkVisible("FilterFlap2_D0", true);
// }
// }
// }
// public static class BF_109G6Mid extends PlaneGeneric
// {
// public BF_109G6Mid()
// {
// if(World.cur().camouflage == 2 || World.cur().camouflage == 5)
// {
// hierMesh().chunkVisible("Filter_d0", true);
// hierMesh().chunkVisible("FilterFlap1_D0", true);
// hierMesh().chunkVisible("FilterFlap2_D0", true);
// }
// }
// }
// public static class BF_109K14 extends PlaneGeneric
// {
// }
    public static class BF_109K4C3 extends PlaneGeneric {
        public BF_109K4C3() {
            if (World.rnd().nextFloat() > 0.6D) {
                this.hierMesh().chunkVisible("GunL_D0", true);
            }
            this.hierMesh().chunkVisible("GunR_D0", true);
        }
    }

// public static class BF_109T_1 extends PlaneGeneric
// {
// }
// public static class BF_109T_2 extends PlaneGeneric
// {
// }
// public static class S_199 extends PlaneGeneric
// {
// }
    static {
        new PlaneGeneric.SPAWN(BF_109B1.class);
        new PlaneGeneric.SPAWN(BF_109B2.class);
        new PlaneGeneric.SPAWN(BF_109C1.class);
        new PlaneGeneric.SPAWN(BF_109D1.class);
        new PlaneGeneric.SPAWN(BF_109E1.class);
        new PlaneGeneric.SPAWN(BF_109E3.class);
        new PlaneGeneric.SPAWN(BF_109E4N.class);
// new PlaneGeneric.SPAWN(BF_109E7N.class);
// new PlaneGeneric.SPAWN(BF_109F0.class);
// new PlaneGeneric.SPAWN(BF_109F2U.class);
// new PlaneGeneric.SPAWN(BF_109F4Z.class);
// new PlaneGeneric.SPAWN(BF_109G1.class);
// new PlaneGeneric.SPAWN(BF_109G3.class);
// new PlaneGeneric.SPAWN(BF_109G5.class);
// new PlaneGeneric.SPAWN(BF_109G5AS.class);
// new PlaneGeneric.SPAWN(BF_109G6Mid.class);
// new PlaneGeneric.SPAWN(BF_109G6Erla.class);
// new PlaneGeneric.SPAWN(BF_109G10C3.class);
// new PlaneGeneric.SPAWN(BF_109G10Erla.class);
// new PlaneGeneric.SPAWN(BF_109G14AS.class);
        new PlaneGeneric.SPAWN(BF_109K4C3.class);
// new PlaneGeneric.SPAWN(BF_109K14.class);
// new PlaneGeneric.SPAWN(BF_109T_1.class);
// new PlaneGeneric.SPAWN(BF_109T_2.class);
// new PlaneGeneric.SPAWN(S_199.class);
    }
}
