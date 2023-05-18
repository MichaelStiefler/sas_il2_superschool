using System;
using System.Collections.Generic;
using System.Collections;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Net.Sockets;
using System.Net;
using Org.Mentalis.Files;

namespace com.sas1946.il2.directx.dinput.Il2SwitchBox
{
    public partial class Il2SwitchBox : Form
    {
        #region Window Messages
        public enum WindowsMessage
        {
            WM_NULL = 0x0000,
            WM_CREATE = 0x0001,
            WM_DESTROY = 0x0002,
            WM_MOVE = 0x0003,
            WM_SIZE = 0x0005,
            WM_ACTIVATE = 0x0006,
            WM_SETFOCUS = 0x0007,
            WM_KILLFOCUS = 0x0008,
            WM_ENABLE = 0x000A,
            WM_SETREDRAW = 0x000B,
            WM_SETTEXT = 0x000C,
            WM_GETTEXT = 0x000D,
            WM_GETTEXTLENGTH = 0x000E,
            WM_PAINT = 0x000F,
            WM_CLOSE = 0x0010,
            WM_QUERYENDSESSION = 0x0011,
            WM_QUERYOPEN = 0x0013,
            WM_ENDSESSION = 0x0016,
            WM_QUIT = 0x0012,
            WM_ERASEBKGND = 0x0014,
            WM_SYSCOLORCHANGE = 0x0015,
            WM_SHOWWINDOW = 0x0018,
            WM_WININICHANGE = 0x001A,
            WM_SETTINGCHANGE = WM_WININICHANGE,
            WM_DEVMODECHANGE = 0x001B,
            WM_ACTIVATEAPP = 0x001C,
            WM_FONTCHANGE = 0x001D,
            WM_TIMECHANGE = 0x001E,
            WM_CANCELMODE = 0x001F,
            WM_SETCURSOR = 0x0020,
            WM_MOUSEACTIVATE = 0x0021,
            WM_CHILDACTIVATE = 0x0022,
            WM_QUEUESYNC = 0x0023,
            WM_GETMINMAXINFO = 0x0024,
            WM_PAINTICON = 0x0026,
            WM_ICONERASEBKGND = 0x0027,
            WM_NEXTDLGCTL = 0x0028,
            WM_SPOOLERSTATUS = 0x002A,
            WM_DRAWITEM = 0x002B,
            WM_MEASUREITEM = 0x002C,
            WM_DELETEITEM = 0x002D,
            WM_VKEYTOITEM = 0x002E,
            WM_CHARTOITEM = 0x002F,
            WM_SETFONT = 0x0030,
            WM_GETFONT = 0x0031,
            WM_SETHOTKEY = 0x0032,
            WM_GETHOTKEY = 0x0033,
            WM_QUERYDRAGICON = 0x0037,
            WM_COMPAREITEM = 0x0039,
            WM_GETOBJECT = 0x003D,
            WM_COMPACTING = 0x0041,
            WM_COMMNOTIFY = 0x0044,
            WM_WINDOWPOSCHANGING = 0x0046,
            WM_WINDOWPOSCHANGED = 0x0047,
            WM_POWER = 0x0048,
            WM_COPYDATA = 0x004A,
            WM_CANCELJOURNAL = 0x004B,
            WM_NOTIFY = 0x004E,
            WM_INPUTLANGCHANGEREQUEST = 0x0050,
            WM_INPUTLANGCHANGE = 0x0051,
            WM_TCARD = 0x0052,
            WM_HELP = 0x0053,
            WM_USERCHANGED = 0x0054,
            WM_NOTIFYFORMAT = 0x0055,
            WM_CONTEXTMENU = 0x007B,
            WM_STYLECHANGING = 0x007C,
            WM_STYLECHANGED = 0x007D,
            WM_DISPLAYCHANGE = 0x007E,
            WM_GETICON = 0x007F,
            WM_SETICON = 0x0080,
            WM_NCCREATE = 0x0081,
            WM_NCDESTROY = 0x0082,
            WM_NCCALCSIZE = 0x0083,
            WM_NCHITTEST = 0x0084,
            WM_NCPAINT = 0x0085,
            WM_NCACTIVATE = 0x0086,
            WM_GETDLGCODE = 0x0087,
            WM_SYNCPAINT = 0x0088,


            WM_NCMOUSEMOVE = 0x00A0,
            WM_NCLBUTTONDOWN = 0x00A1,
            WM_NCLBUTTONUP = 0x00A2,
            WM_NCLBUTTONDBLCLK = 0x00A3,
            WM_NCRBUTTONDOWN = 0x00A4,
            WM_NCRBUTTONUP = 0x00A5,
            WM_NCRBUTTONDBLCLK = 0x00A6,
            WM_NCMBUTTONDOWN = 0x00A7,
            WM_NCMBUTTONUP = 0x00A8,
            WM_NCMBUTTONDBLCLK = 0x00A9,
            WM_NCXBUTTONDOWN = 0x00AB,
            WM_NCXBUTTONUP = 0x00AC,
            WM_NCXBUTTONDBLCLK = 0x00AD,

            WM_INPUT_DEVICE_CHANGE = 0x00FE,
            WM_INPUT = 0x00FF,

            WM_KEYFIRST = 0x0100,
            WM_KEYDOWN = 0x0100,
            WM_KEYUP = 0x0101,
            WM_CHAR = 0x0102,
            WM_DEADCHAR = 0x0103,
            WM_SYSKEYDOWN = 0x0104,
            WM_SYSKEYUP = 0x0105,
            WM_SYSCHAR = 0x0106,
            WM_SYSDEADCHAR = 0x0107,
            WM_UNICHAR = 0x0109,
            WM_KEYLAST = 0x0109,

            WM_IME_STARTCOMPOSITION = 0x010D,
            WM_IME_ENDCOMPOSITION = 0x010E,
            WM_IME_COMPOSITION = 0x010F,
            WM_IME_KEYLAST = 0x010F,

            WM_INITDIALOG = 0x0110,
            WM_COMMAND = 0x0111,
            WM_SYSCOMMAND = 0x0112,
            WM_TIMER = 0x0113,
            WM_HSCROLL = 0x0114,
            WM_VSCROLL = 0x0115,
            WM_INITMENU = 0x0116,
            WM_INITMENUPOPUP = 0x0117,
            WM_MENUSELECT = 0x011F,
            WM_MENUCHAR = 0x0120,
            WM_ENTERIDLE = 0x0121,
            WM_MENURBUTTONUP = 0x0122,
            WM_MENUDRAG = 0x0123,
            WM_MENUGETOBJECT = 0x0124,
            WM_UNINITMENUPOPUP = 0x0125,
            WM_MENUCOMMAND = 0x0126,

            WM_CHANGEUISTATE = 0x0127,
            WM_UPDATEUISTATE = 0x0128,
            WM_QUERYUISTATE = 0x0129,

            WM_CTLCOLORMSGBOX = 0x0132,
            WM_CTLCOLOREDIT = 0x0133,
            WM_CTLCOLORLISTBOX = 0x0134,
            WM_CTLCOLORBTN = 0x0135,
            WM_CTLCOLORDLG = 0x0136,
            WM_CTLCOLORSCROLLBAR = 0x0137,
            WM_CTLCOLORSTATIC = 0x0138,
            MN_GETHMENU = 0x01E1,

            WM_MOUSEFIRST = 0x0200,
            WM_MOUSEMOVE = 0x0200,
            WM_LBUTTONDOWN = 0x0201,
            WM_LBUTTONUP = 0x0202,
            WM_LBUTTONDBLCLK = 0x0203,
            WM_RBUTTONDOWN = 0x0204,
            WM_RBUTTONUP = 0x0205,
            WM_RBUTTONDBLCLK = 0x0206,
            WM_MBUTTONDOWN = 0x0207,
            WM_MBUTTONUP = 0x0208,
            WM_MBUTTONDBLCLK = 0x0209,
            WM_MOUSEWHEEL = 0x020A,
            WM_XBUTTONDOWN = 0x020B,
            WM_XBUTTONUP = 0x020C,
            WM_XBUTTONDBLCLK = 0x020D,
            WM_MOUSEHWHEEL = 0x020E,

            WM_PARENTNOTIFY = 0x0210,
            WM_ENTERMENULOOP = 0x0211,
            WM_EXITMENULOOP = 0x0212,

            WM_NEXTMENU = 0x0213,
            WM_SIZING = 0x0214,
            WM_CAPTURECHANGED = 0x0215,
            WM_MOVING = 0x0216,

            WM_POWERBROADCAST = 0x0218,

            WM_DEVICECHANGE = 0x0219,

            WM_MDICREATE = 0x0220,
            WM_MDIDESTROY = 0x0221,
            WM_MDIACTIVATE = 0x0222,
            WM_MDIRESTORE = 0x0223,
            WM_MDINEXT = 0x0224,
            WM_MDIMAXIMIZE = 0x0225,
            WM_MDITILE = 0x0226,
            WM_MDICASCADE = 0x0227,
            WM_MDIICONARRANGE = 0x0228,
            WM_MDIGETACTIVE = 0x0229,


            WM_MDISETMENU = 0x0230,
            WM_ENTERSIZEMOVE = 0x0231,
            WM_EXITSIZEMOVE = 0x0232,
            WM_DROPFILES = 0x0233,
            WM_MDIREFRESHMENU = 0x0234,

            WM_IME_SETCONTEXT = 0x0281,
            WM_IME_NOTIFY = 0x0282,
            WM_IME_CONTROL = 0x0283,
            WM_IME_COMPOSITIONFULL = 0x0284,
            WM_IME_SELECT = 0x0285,
            WM_IME_CHAR = 0x0286,
            WM_IME_REQUEST = 0x0288,
            WM_IME_KEYDOWN = 0x0290,
            WM_IME_KEYUP = 0x0291,

            WM_MOUSEHOVER = 0x02A1,
            WM_MOUSELEAVE = 0x02A3,
            WM_NCMOUSEHOVER = 0x02A0,
            WM_NCMOUSELEAVE = 0x02A2,

            WM_WTSSESSION_CHANGE = 0x02B1,

            WM_TABLET_FIRST = 0x02c0,
            WM_TABLET_LAST = 0x02df,

            WM_CUT = 0x0300,
            WM_COPY = 0x0301,
            WM_PASTE = 0x0302,
            WM_CLEAR = 0x0303,
            WM_UNDO = 0x0304,
            WM_RENDERFORMAT = 0x0305,
            WM_RENDERALLFORMATS = 0x0306,
            WM_DESTROYCLIPBOARD = 0x0307,
            WM_DRAWCLIPBOARD = 0x0308,
            WM_PAINTCLIPBOARD = 0x0309,
            WM_VSCROLLCLIPBOARD = 0x030A,
            WM_SIZECLIPBOARD = 0x030B,
            WM_ASKCBFORMATNAME = 0x030C,
            WM_CHANGECBCHAIN = 0x030D,
            WM_HSCROLLCLIPBOARD = 0x030E,
            WM_QUERYNEWPALETTE = 0x030F,
            WM_PALETTEISCHANGING = 0x0310,
            WM_PALETTECHANGED = 0x0311,
            WM_HOTKEY = 0x0312,

            WM_PRINT = 0x0317,
            WM_PRINTCLIENT = 0x0318,

            WM_APPCOMMAND = 0x0319,

            WM_THEMECHANGED = 0x031A,

            WM_CLIPBOARDUPDATE = 0x031D,

            WM_DWMCOMPOSITIONCHANGED = 0x031E,
            WM_DWMNCRENDERINGCHANGED = 0x031F,
            WM_DWMCOLORIZATIONCOLORCHANGED = 0x0320,
            WM_DWMWINDOWMAXIMIZEDCHANGE = 0x0321,

            WM_GETTITLEBARINFOEX = 0x033F,

            WM_HANDHELDFIRST = 0x0358,
            WM_HANDHELDLAST = 0x035F,

            WM_AFXFIRST = 0x0360,
            WM_AFXLAST = 0x037F,

            WM_PENWINFIRST = 0x0380,
            WM_PENWINLAST = 0x038F,

            WM_APP = 0x8000,

            WM_USER = 0x0400,

            WM_REFLECT = WM_USER + 0x1C00,
        }
        #endregion

        private const string IPADDRESS = "127.0.0.1";
        private const int WM_JOYSTICK_UPDATE = (int)WindowsMessage.WM_APP + 1;
        private const int WM_DEVICELINK_UPDATE = (int)WindowsMessage.WM_APP + 2;
        private const int WM_DEVICELINK_CONNECTED = (int)WindowsMessage.WM_APP + 3;
        private const bool PROTOSEND = true;
        private const bool PROTORECEIVE = false;
        private const int MAX_PROTO_LINES = 15;
        private const int MAX_QUEUE_SIZE = 1024;
        private IniReader iniReader;
        private Joystick.Joystick jst;
        private Switch.Switch[] switches;
        private bool firstUpdate = true;
        //UdpClient clientSocket = new System.Net.Sockets.UdpClient();
        //IPEndPoint clientSocketEndPoint;
        public ThreadQueue.ThreadQueue<string> rcvPkts = new ThreadQueue.ThreadQueue<string>(MAX_QUEUE_SIZE);
        public ThreadQueue.ThreadQueue<string> sndPkts = new ThreadQueue.ThreadQueue<string>(MAX_QUEUE_SIZE);

        private DeviceLinkClient.DeviceLinkClient myDeviceLinkClient;
        private TypePendingCommand pendingCommand;

        private bool switchBoxActive;
        private ContextMenu contextMenu;
        private MenuItem menuItemShow;
        private MenuItem menuItemExit;
        private bool exitClicked = false;
        private int repeatCommandRemains = 0;
        //private string repeatCommand = "";
        private const int REPEAT_MANUAL_GEAR = 75;

        private struct TypePendingCommand
        {
            public string cont;
            public string stop;
            public string send;
        }

        public Il2SwitchBox()
        {
            this.menuItemShow = new System.Windows.Forms.MenuItem();
            this.menuItemExit = new System.Windows.Forms.MenuItem();
            this.contextMenu = new System.Windows.Forms.ContextMenu();
            // Initialize contextMenu 
            this.contextMenu.MenuItems.AddRange(
                        new System.Windows.Forms.MenuItem[] { this.menuItemShow, this.menuItemExit });
            // Initialize menuItemShow 
            this.menuItemShow.Index = 0;
            this.menuItemShow.Text = "&Show";
            this.menuItemShow.Click += new System.EventHandler(this.menuItemShow_Click);
            // Initialize menuItemExit 
            this.menuItemExit.Index = 1;
            this.menuItemExit.Text = "E&xit";
            this.menuItemExit.Click += new System.EventHandler(this.menuItemExit_Click);
            InitializeComponent();
            this.notifyIcon.ContextMenu = this.contextMenu;
            this.Visible = false;
        }

        [System.Security.Permissions.PermissionSet(System.Security.Permissions.SecurityAction.Demand, Name = "FullTrust")]
        protected override void WndProc(ref Message m)
        {
            switch (m.Msg)
            {
                case WM_JOYSTICK_UPDATE:
                    if (firstUpdate)
                    {
                        this.updateAllSwitches(jst.Buttons);
                        this.firstUpdate = false;
                    }
                    else
                    {
                        this.updateSwitch((int)m.WParam, (int)m.LParam == 1);
                    }
                    break;
                case WM_DEVICELINK_UPDATE:
                    switch ((int)m.WParam)
                    {
                        case DeviceLinkClient.DeviceLinkClient.PACKET_RECEIVED:
                            this.DeviceLinkPacketReceived();
                            break;
                        case DeviceLinkClient.DeviceLinkClient.PACKET_SENT:
                            this.DeviceLinkPacketSent();
                            break;
                    }
                    break;
                case WM_DEVICELINK_CONNECTED:
                    switch ((int)m.WParam)
                    {
                        case 0:
                            this.Proto("Disconnected", PROTORECEIVE);
                            break;
                        case 1:
                            this.Proto("Connected", PROTORECEIVE);
                            break;
                    }
                    break;
            }
            base.WndProc(ref m);
        }

        private void Il2SwitchBox_Load(object sender, EventArgs e)
        {
            // grab the joystick
            this.iniReader = new IniReader(System.IO.Path.GetDirectoryName(System.Reflection.Assembly.GetEntryAssembly().Location) + "/Il2SwitchBox.ini");
            jst = new Joystick.Joystick(this.Handle, WM_JOYSTICK_UPDATE);
            string[] sticks = jst.FindJoysticks();
            if (sticks == null)
            {
                int numSwitches = this.iniReader.ReadInteger("Common", "switches", 0);
                this.switches = new Switch.Switch[numSwitches];

                // add the button controls to the button container
                for (int i = 0; i < numSwitches; i++)
                {
                    this.switches[i] = new Switch.Switch();
                    this.switches[i].SwitchId = i + 1;
                    string sectionName = String.Format("Switch{0:D2}", i + 1);
                    if (this.iniReader.GetSectionNames().Contains(sectionName))
                    {
                        string switchName = this.iniReader.ReadString(sectionName, "name", "");
                        if (switchName.Length > 0) this.switches[i].SwitchText = switchName;
                    }
                    this.switches[i].SwitchStatus = false;
                    this.switches[i].SwtStatus.CheckedChanged += this.switch_CheckedChanged;
                    panelSwitches.Controls.Add(this.switches[i]);
                }
            }
            else
            {
                string joystickName = this.iniReader.ReadString("Common", "name", "");
                if (joystickName.Length == 0)
                    joystickName = sticks[0];
                jst.AcquireJoystick(joystickName);
                this.switches = new Switch.Switch[jst.Buttons.Length];

                // add the button controls to the button container
                for (int i = 0; i < jst.Buttons.Length; i++)
                {
                    this.switches[i] = new Switch.Switch();
                    this.switches[i].SwitchId = i + 1;
                    string sectionName = String.Format("Switch{0:D2}", i + 1);
                    if (this.iniReader.GetSectionNames().Contains(sectionName))
                    {
                        string switchName = this.iniReader.ReadString(sectionName, "name", "");
                        if (switchName.Length > 0) this.switches[i].SwitchText = switchName;
                    }
                    this.switches[i].SwitchStatus = jst.Buttons[i];
                    this.switches[i].SwtStatus.CheckedChanged += this.switch_CheckedChanged;
                    panelSwitches.Controls.Add(this.switches[i]);
                }
            }
            this.switchBoxActive = false;

            this.myDeviceLinkClient = new DeviceLinkClient.DeviceLinkClient(IPADDRESS, 10001, this.rcvPkts, this.sndPkts, this.Handle, WM_DEVICELINK_UPDATE, WM_DEVICELINK_CONNECTED);
            this.myDeviceLinkClient.Open();

            //this.clientSocketEndPoint = new IPEndPoint(IPAddress.Parse("127.0.0.1"), 10001); // endpoint where server is listening (testing localy)
            //clientSocket.Connect(clientSocketEndPoint);
            this.pendingCommand = new TypePendingCommand();

            //this.pendingCommand.cont = "A/2\\1.00";
            //this.pendingCommand.stop = "none";
            //this.pendingCommand.send = "R/2";

            //this.deviceLinkCommand("R/2");

            // start updating positions
            //tmrUpdateStick.Enabled = true;
        }

        private void deviceLinkCommand(string theCommand)
        {
            this.myDeviceLinkClient.Send(theCommand);
            //// send data
            //clientSocket.Send(Encoding.ASCII.GetBytes(theCommand), theCommand.Length);
            //this.Proto(theCommand, PROTOSEND);

            //// then receive data
            //byte[] receivedData = clientSocket.Receive(ref this.clientSocketEndPoint);  // Exception: An existing connection was forcibly closed by the remote host
            //string receiveData = Encoding.Default.GetString(receivedData);
            //this.Proto(receiveData, PROTORECEIVE);
            //return receiveData;
        }

        private void updateAllSwitches(bool[] activatedSwitches)
        {
            for (int i = 0; i < this.switches.Length; i++)
                this.switches[i].SwitchStatus = (i < activatedSwitches.Length && activatedSwitches[i]);
        }

        private void updateSwitch(int switchIndex, bool switchActivated)
        {
            if (this.switches[switchIndex].SwitchStatus != switchActivated)
            {
                this.switches[switchIndex].SwitchStatus = switchActivated;
            }
            //this.switches[switchIndex].SwitchStatus = switchActivated;
        }

        private void Il2SwitchBox_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (e.CloseReason == CloseReason.UserClosing && !this.exitClicked)
            {
                e.Cancel = true;
                this.WindowState = FormWindowState.Minimized;
                return;
            }
            try
            {
                if (jst != null)
                    jst.ReleaseJoystick();
            } catch (Exception){};
            if (this.myDeviceLinkClient != null)
                this.myDeviceLinkClient.Close();
        }

        private void setGear(bool gearDown)
        {
            if (gearDown)
            {
                this.pendingCommand.cont = "A/164\\";
                this.pendingCommand.stop = "A/164\\1.00";
                this.pendingCommand.send = "R/165";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/164");
            }
            else
            {
                this.pendingCommand.cont = "A/164\\";
                this.pendingCommand.stop = "A/164\\0.00";
                this.pendingCommand.send = "R/165";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/164");
            }
        }

        private void setGearManual(bool gearDown)
        {
            if (gearDown)
            {
                //this.pendingCommand.cont = "A/56\\";
                //this.pendingCommand.stop = "A/56\\1.00";
                //this.pendingCommand.send = "R/56/169";
                //this.myDeviceLinkClient.Send("R/56");

                //this.pendingCommand.cont = "A/20\\";
                //this.pendingCommand.stop = "none";
                //this.pendingCommand.send = "R/20/169";
                //this.repeatCommandRemains = REPEAT_MANUAL_GEAR;
                //this.myDeviceLinkClient.Send("R/20/169");

                this.pendingCommand.cont = "A/164\\";
                this.pendingCommand.stop = "A/164\\1.00";
                this.pendingCommand.send = "R/164/169";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/164");
            }
            else
            {
                //this.pendingCommand.cont = "A/56\\";
                //this.pendingCommand.stop = "A/56\\0.00";
                //this.pendingCommand.send = "R/56/167";
                //this.myDeviceLinkClient.Send("R/56");

                //this.pendingCommand.cont = "A/20\\";
                //this.pendingCommand.stop = "none";
                //this.pendingCommand.send = "R/20/167";
                //this.repeatCommandRemains = REPEAT_MANUAL_GEAR;
                //this.myDeviceLinkClient.Send("R/20/167");

                this.pendingCommand.cont = "A/164\\";
                this.pendingCommand.stop = "A/164\\0.00";
                this.pendingCommand.send = "R/164/167";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/164");
            }
        }

        private void setCanopy(bool openCanopy)
        {
            if (openCanopy)
            {
                this.pendingCommand.cont = "A/212\\";
                this.pendingCommand.stop = "A/212\\1";
                this.pendingCommand.send = "R/213";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/212");
            }
            else
            {
                this.pendingCommand.cont = "A/212\\";
                this.pendingCommand.stop = "A/212\\0";
                this.pendingCommand.send = "R/213";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/212");
            }
        }

        private void setNavLights(bool turnOnLights)
        {
            this.pendingCommand.cont = "";
            this.pendingCommand.stop = "";
            this.pendingCommand.send = "";
            if (turnOnLights)
            {
                this.myDeviceLinkClient.Send("R/411");
            } else {
                this.myDeviceLinkClient.Send("R/411");
            }
        }

        private void setLandingLights(bool turnOnLight)
        {
            this.pendingCommand.cont = "";
            this.pendingCommand.stop = "";
            this.pendingCommand.send = "";
            if (turnOnLight)
            {
                this.myDeviceLinkClient.Send("R/413");
            }
            else
            {
                this.myDeviceLinkClient.Send("R/413");
            }
        }

        private void setWingtipSmoke(bool turnOnSmoke)
        {
            this.pendingCommand.cont = "";
            this.pendingCommand.stop = "";
            this.pendingCommand.send = "";
            if (turnOnSmoke)
            {
                this.myDeviceLinkClient.Send("R/415");
            }
            else
            {
                this.myDeviceLinkClient.Send("R/415");
            }
        }

        private void setBombBay(bool openBombBay)
        {
            if (openBombBay)
            {
                
            } else {

            }
       }

        private void setTailHook(bool lowerHook)
        {
            if (lowerHook)
            {
                this.pendingCommand.cont = "A/214\\";
                this.pendingCommand.stop = "A/214\\1";
                this.pendingCommand.send = "R/215";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/214");
            }
            else
            {
                this.pendingCommand.cont = "A/214\\";
                this.pendingCommand.stop = "A/214\\0";
                this.pendingCommand.send = "R/215";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/214");
            }
        }

        private void setTailWheelLock(bool lockWheel)
        {
            if (lockWheel)
            {
                this.pendingCommand.cont = "A/174\\";
                this.pendingCommand.stop = "A/174\\1";
                this.pendingCommand.send = "R/175";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/174");
            }
            else
            {
                this.pendingCommand.cont = "A/174\\";
                this.pendingCommand.stop = "A/174\\0";
                this.pendingCommand.send = "R/175";
                this.repeatCommandRemains = 10;
                this.myDeviceLinkClient.Send("R/174");
            }
        }

        private void Proto(string theLine, bool sendReceive)
        {
            ListBox lb = sendReceive ? this.listBoxSend : this.listBoxReceive;
            lb.BeginUpdate();
            while (lb.Items.Count >= MAX_PROTO_LINES) lb.Items.RemoveAt(0);
            lb.TopIndex = lb.Items.Add(theLine);
            lb.EndUpdate();
        }

        private void switch_CheckedChanged(object sender, EventArgs e)
        {
            for (int i=0; i<this.switches.Length; i++)
                if (sender.Equals(this.switches[i].SwtStatus))
                    this.doSwitchCheckedChanged(i);
                    
        }

        private void doSwitchCheckedChanged(int switchIndex)
        {
            bool switchActive = this.switches[switchIndex].SwitchStatus;
            //this.Proto("SWT " + switchIndex + " " + switchActive, PROTOSEND);
            string sectionName = String.Format("Switch{0:D2}", switchIndex + 1);
            if (this.iniReader.GetSectionNames().Contains(sectionName))
            {
                if (this.iniReader.ReadInteger(sectionName, "inverted", 0) != 0)
                    switchActive = !switchActive;
            }
            if (this.switches[switchIndex].SwitchText.ToLower() == "activate")
                this.switchBoxActive = switchActive;
            else if (!this.switchBoxActive) return;
            switch (this.switches[switchIndex].SwitchText.ToLower())
            {
                case "gear":
                    this.setGear(switchActive);
                    break;
                case "manual gear":
                    this.setGearManual(switchActive);
                    break;
                case "canopy":
                    this.setCanopy(switchActive);
                    break;
                case "nav lights":
                    this.setNavLights(switchActive);
                    break;
                case "landing light":
                    this.setLandingLights(switchActive);
                    break;
                case "wingtip smoke":
                    this.setWingtipSmoke(switchActive);
                    break;
                case "bomb bay":
                    this.setBombBay(switchActive);
                    break;
                case "tail wheel lock":
                    this.setTailWheelLock(switchActive);
                    break;
                case "tail hook":
                    this.setTailHook(switchActive);
                    break;
            }
        }

        private void DeviceLinkPacketReceived()
        {
            string deviceLinkPacket = this.rcvPkts.Dequeue();
            //if (deviceLinkPacket.StartsWith(DeviceLinkClient.DeviceLinkClient.SYNC_REPLY)) return;
            this.Proto(deviceLinkPacket, PROTORECEIVE);
            if (this.pendingCommand.cont != null && this.pendingCommand.stop != null && this.pendingCommand.send != null)
            {
                if (this.pendingCommand.cont.Length > 0 && this.pendingCommand.stop.Length > 0 && this.pendingCommand.send.Length > 0)
                {
                    if (deviceLinkPacket.StartsWith(this.pendingCommand.cont) && !deviceLinkPacket.StartsWith(this.pendingCommand.stop)/* && --this.repeatCommandRemains>0*/)
                    {
                        this.myDeviceLinkClient.Send(this.pendingCommand.send);
                    }
                }
            }
        }

        private void DeviceLinkPacketSent()
        {
            string deviceLinkPacket = this.sndPkts.Dequeue();
            if (deviceLinkPacket.StartsWith(DeviceLinkClient.DeviceLinkClient.SYNC_REQUEST)) return;
            this.Proto(deviceLinkPacket, PROTOSEND);
        }

        private void Il2SwitchBox_Resize(object sender, EventArgs e)
        {
            if (this.WindowState == FormWindowState.Minimized)
            {
                this.minimizeToTray(true);
            }
        }

        private void minimizeToTray(bool minimize)
        {
            //if (minimize) this.Hide(); else this.Show();
            //this.ShowInTaskbar = !minimize;
            //this.WindowState = FormWindowState.Normal;
            this.Visible = !minimize;
            this.WindowState = minimize ? FormWindowState.Minimized : FormWindowState.Normal;
            this.notifyIcon.Visible = true;
        }

        private void menuItemShow_Click(object Sender, EventArgs e)
        {
            this.minimizeToTray(false);
        }

        private void menuItemExit_Click(object Sender, EventArgs e)
        {
            // Close the form, which closes the application.
            this.exitClicked = true;
            this.Close();
        }

        private void Il2SwitchBox_Shown(object sender, EventArgs e)
        {
            this.minimizeToTray(true);
        }
    }
}
