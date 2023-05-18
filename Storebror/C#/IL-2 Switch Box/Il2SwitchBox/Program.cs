using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

namespace com.sas1946.il2.directx.dinput
{
    static class Program
    {
        /// <summary>
        /// Der Haupteinstiegspunkt für die Anwendung.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new Il2SwitchBox.Il2SwitchBox());
        }
    }
}
