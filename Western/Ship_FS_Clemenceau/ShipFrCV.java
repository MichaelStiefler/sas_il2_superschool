package com.maddox.il2.objects.ships;

import com.maddox.il2.ai.ground.TgtShip;
import com.maddox.il2.engine.HierMesh;
import com.maddox.il2.objects.vehicles.radios.TypeHasILSBlindLanding;
import com.maddox.rts.SectFile;

public abstract class ShipFrCV extends Ship
{
    static
    {
        new BigshipGeneric.SPAWN(com.maddox.il2.objects.ships.ShipFrCV.FSClemenceauR98CV_1962.class);
        new BigshipGeneric.SPAWN(com.maddox.il2.objects.ships.ShipFrCV.FSClemenceauR98CV_1986.class);
        new BigshipGeneric.SPAWN(com.maddox.il2.objects.ships.ShipFrCV.FSFochR99CV_1964.class);
        new BigshipGeneric.SPAWN(com.maddox.il2.objects.ships.ShipFrCV.FSFochR99CV_1988.class);
        new BigshipGeneric.SPAWN(com.maddox.il2.objects.ships.ShipFrCV.NAeSaoPaulo_A12_Br_2001.class);
        new BigshipGeneric.SPAWN(com.maddox.il2.objects.ships.ShipFrCV.NAeSaoPaulo_A12_Br_2011.class);
    }

    public ShipFrCV()
    {
    }

    public static class FSClemenceauR98CV_1962 extends CarrierShipILSGeneric
    implements TgtShip, TypeBlastDeflector
    {

        public FSClemenceauR98CV_1962()
        {
        }

        public FSClemenceauR98CV_1962(String paramString1, int paramInt, SectFile paramSectFile1, String paramString2, SectFile paramSectFile2, String paramString3)
        {
            super(paramString1, paramInt, paramSectFile1, paramString2, paramSectFile2, paramString3);
            super.bHasBlastDeflectorControl = true;
        }

        public void setBlastDeflector(int num, int z)
        {
            super.setBlastDeflector(num, z);
        }

        public void moveBlastDeflector(int num, float f)
        {
 if( bLogDetailBD )
   System.out.println("ShipFrCV: 45 - moveBlastDeflector(int num=" + Integer.toString(num) + ", flost f="  + Float.toString(f) + ")");

            float f1 = f * 1.3F;
            if( f1 > 1F )
                f1 = 1F;
            float f2 = f * 1.3F - 0.2F;
            if( f2 < 0F )
                f2 = 0F;
            if( f2 > 1F )
                f2 = 1F;

            switch(num)
            {
                case 0:
                    hierMesh().chunkSetAngles("BlastDef01", 0.0F, -60F * f1, 0.0F);
                    hierMesh().chunkSetAngles("BlastDef02", 0.0F, -60F * f2, 0.0F);
                    break;
                case 1:
                    hierMesh().chunkSetAngles("BlastDef11", 0.0F, -60F * f1, 0.0F);
                    hierMesh().chunkSetAngles("BlastDef12", 0.0F, -60F * f2, 0.0F);
                    break;
                default:
                break;
            }
        }

    }

    public static class FSClemenceauR98CV_1986 extends FSClemenceauR98CV_1962
    {

        public FSClemenceauR98CV_1986()
        {
        }

        public FSClemenceauR98CV_1986(String paramString1, int paramInt, SectFile paramSectFile1, String paramString2, SectFile paramSectFile2, String paramString3)
        {
            super(paramString1, paramInt, paramSectFile1, paramString2, paramSectFile2, paramString3);
        }
    }

    public static class FSFochR99CV_1964 extends FSClemenceauR98CV_1962
    {

        public FSFochR99CV_1964()
        {
        }

        public FSFochR99CV_1964(String paramString1, int paramInt, SectFile paramSectFile1, String paramString2, SectFile paramSectFile2, String paramString3)
        {
            super(paramString1, paramInt, paramSectFile1, paramString2, paramSectFile2, paramString3);
        }
    }

    public static class FSFochR99CV_1988 extends FSClemenceauR98CV_1962
    {

        public FSFochR99CV_1988()
        {
        }

        public FSFochR99CV_1988(String paramString1, int paramInt, SectFile paramSectFile1, String paramString2, SectFile paramSectFile2, String paramString3)
        {
            super(paramString1, paramInt, paramSectFile1, paramString2, paramSectFile2, paramString3);
        }
    }

    public static class NAeSaoPaulo_A12_Br_2001 extends FSClemenceauR98CV_1962
    {

        public NAeSaoPaulo_A12_Br_2001()
        {
        }

        public NAeSaoPaulo_A12_Br_2001(String paramString1, int paramInt, SectFile paramSectFile1, String paramString2, SectFile paramSectFile2, String paramString3)
        {
            super(paramString1, paramInt, paramSectFile1, paramString2, paramSectFile2, paramString3);
        }
    }

    public static class NAeSaoPaulo_A12_Br_2011 extends FSClemenceauR98CV_1962
    {

        public NAeSaoPaulo_A12_Br_2011()
        {
        }

        public NAeSaoPaulo_A12_Br_2011(String paramString1, int paramInt, SectFile paramSectFile1, String paramString2, SectFile paramSectFile2, String paramString3)
        {
            super(paramString1, paramInt, paramSectFile1, paramString2, paramSectFile2, paramString3);
        }
    }

    private static boolean bLogDetailBD = false;

}
