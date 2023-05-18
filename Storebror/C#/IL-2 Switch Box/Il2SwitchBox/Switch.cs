using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;

namespace com.sas1946.il2.directx.dinput.Switch
{
    public partial class Switch : UserControl
    {
        public Switch()
        {
            InitializeComponent();
        }

        private int switchId;
        public int SwitchId
        {
            get { return switchId; }
            set 
            {
                switchId = value; 
                swtStatus.Text = "Switch " + value;
            }
        }

        public string SwitchText
        {
            get { return swtStatus.Text; }
            set
            {
                swtStatus.Text = value;
            }
        }

        private bool switchStatus;
        public bool SwitchStatus
        {
            get { return swtStatus.Checked; }
            set
            {
                switchStatus = value;
                swtStatus.Checked = value;
            }
        }

        public CheckBox SwtStatus
        {
            get { return swtStatus; }
            set
            {
                swtStatus = value;
            }
        }

    }
}
