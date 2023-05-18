using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;
using System.Threading;
using System.Collections;
using System.IO;
using System.Security.Permissions;
using IniParser;
using IniParser.Model;
using PlanSwitcher;
using Microsoft.Win32.TaskScheduler;

namespace FUMS
{
    public partial class FUMS : Form
    {
        private ContextMenu contextMenu;
        private MenuItem menuItemShow;
        private MenuItem menuItemExit;
        private FileIniDataParser iniParser;
        private bool exitClicked = false;

        private Dictionary<string, int> processPriorities;
        private Dictionary<string, int> processAffinities;
        private Dictionary<string, int> powerPlans;
        private Object processPrioritiesSyncObject = new Object();
        private Object processAffinitiesSyncObject = new Object();
        private Object powerPlanSyncObject = new Object();

        private IPowerManager powerManager;
        private List<PowerPlan> plans;
        private PowerPlan startupPlan;
        private const string INI_NAME = "FUMS.ini";
        private const int MAX_LOG_LINES = 1000;
        private int numCores;
        private int maxAffinity;
        private bool isHT;
        private bool powerPlanSet;
        private FileSystemWatcher iniWatcher;
        private bool skipStartupCheckStateChange;

        public FUMS()
        {
            this.menuItemShow = new System.Windows.Forms.MenuItem();
            this.menuItemExit = new System.Windows.Forms.MenuItem();
            this.contextMenu = new System.Windows.Forms.ContextMenu();
            // Initialize contextMenu 
            this.contextMenu.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] { this.menuItemShow, this.menuItemExit });
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
        }

        private void FUMS_Resize(object sender, EventArgs e)
        {
            if (this.WindowState == FormWindowState.Minimized)
            {
                this.MinimizeToTray(true);
            }
        }

        private void MinimizeToTray(bool minimize)
        {
            if (minimize) this.Hide(); else this.Show();
            this.ShowInTaskbar = !minimize;
            this.WindowState = minimize ? FormWindowState.Minimized : FormWindowState.Normal;
        }

        private void FUMS_Load(object sender, EventArgs e)
        {
            this.MinimizeToTray(true);
            this.iniParser = new FileIniDataParser();
            this.processPriorities = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);
            this.processAffinities = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);
            this.powerPlans = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);
            this.powerManager = PowerManagerProvider.CreatePowerManager();
            this.plans = powerManager.GetPlans();
            this.startupPlan = this.powerManager.GetCurrentPlan();
            this.numCores = 0;
            foreach (var item in new System.Management.ManagementObjectSearcher("Select * from Win32_Processor").Get())
                this.numCores += int.Parse(item["NumberOfCores"].ToString());
            if (Environment.ProcessorCount > this.numCores) this.isHT = true;
            int shiftNum = this.isHT ? (this.numCores << 1) : this.numCores;
            this.maxAffinity = (1 << shiftNum) - 1;
            this.powerPlanSet = false;
            this.skipStartupCheckStateChange = true;
            this.checkBoxStartup.CheckState = this.isStartupEnabled()?CheckState.Checked:CheckState.Unchecked;
            this.ReadSettings();
        }

        private void notifyIcon_DoubleClick(object sender, EventArgs e)
        {
            this.MinimizeToTray(false);
        }

        private void menuItemShow_Click(object Sender, EventArgs e)
        {
            this.MinimizeToTray(false);
        }
        private void menuItemExit_Click(object Sender, EventArgs e)
        {
            this.exitClicked = true;
            this.Close();
        }

        private void timerUpdate_Tick(object sender, EventArgs e)
        {
            this.CheckProcesses();
        }

        [PermissionSet(SecurityAction.Demand, Name = "FullTrust")]
        private void WatchFileForChanges(string theFile)
        {
            this.iniWatcher = new FileSystemWatcher();
            this.iniWatcher.Path = Path.GetDirectoryName(theFile);
            /* Watch for changes in LastAccess and LastWrite times, and
               the renaming of files or directories. */
            this.iniWatcher.NotifyFilter = NotifyFilters.LastAccess | NotifyFilters.LastWrite
               | NotifyFilters.FileName | NotifyFilters.DirectoryName;
            // Only watch our ini file.
            this.iniWatcher.Filter = Path.GetFileName(theFile);

            // Add event handlers.
            this.iniWatcher.Changed += new FileSystemEventHandler(OnChanged);
            this.iniWatcher.Created += new FileSystemEventHandler(OnChanged);
            this.iniWatcher.Deleted += new FileSystemEventHandler(OnChanged);
            this.iniWatcher.Renamed += new RenamedEventHandler(OnRenamed);

            // Begin watching.
            this.iniWatcher.EnableRaisingEvents = true;
        }

        private void OnChanged(object source, FileSystemEventArgs e)
        {
            try
            {
                this.iniWatcher.EnableRaisingEvents = false;
                this.ReadProcessSettings();
            }

            finally
            {
                this.iniWatcher.EnableRaisingEvents = true;
            }
        }

        private void OnRenamed(object source, RenamedEventArgs e)
        {
            this.ReadProcessSettings();
        }

        delegate void DoProto(string theLine);
        private void Proto(string theLine)
        {
            if (this.listBoxProto.InvokeRequired)
            {
                this.BeginInvoke(new DoProto(this.Proto), new object[] { theLine });
            }
            else
            {
                this.listBoxProto.BeginUpdate();
                while (this.listBoxProto.Items.Count > MAX_LOG_LINES)
                    this.listBoxProto.Items.RemoveAt(0);
                this.listBoxProto.TopIndex = this.listBoxProto.Items.Add(theLine);
                this.listBoxProto.EndUpdate();
            }
        }

        private void ReadSettings()
        {
            String iniFileName = Path.GetDirectoryName(System.Reflection.Assembly.GetEntryAssembly().Location) + Path.DirectorySeparatorChar + INI_NAME;
            IniData iniData = this.iniParser.ReadFile(iniFileName);
            int updateInterval;
            if (!Int32.TryParse(iniData["Common"]["UpdateInterval"].Trim(), out updateInterval)) updateInterval = 1000;
            this.timerUpdate.Interval = updateInterval;

            if (iniData["Affinity"].Count() > 0)
            {
                this.Proto("Cores found: " + this.numCores);
                this.Proto("Is Hyperthreading: " + this.isHT);
            }

            this.ReadProcessSettings();
            this.WatchFileForChanges(iniFileName);
            this.timerUpdate.Enabled = true;
        }

        private ProcessPriorityClass IntToPrio(int thePrio)
        {
            ProcessPriorityClass retVal = ProcessPriorityClass.Normal;
            switch (thePrio)
            {
                case -2:
                    retVal = ProcessPriorityClass.Idle;
                    break;
                case -1:
                    retVal = ProcessPriorityClass.BelowNormal;
                    break;
                case 0:
                    retVal = ProcessPriorityClass.Normal;
                    break;
                case 1:
                    retVal = ProcessPriorityClass.AboveNormal;
                    break;
                case 2:
                    retVal = ProcessPriorityClass.High;
                    break;
                case 3:
                    retVal = ProcessPriorityClass.RealTime;
                    break;
                default:
                    retVal = ProcessPriorityClass.Normal;
                    break;
            }
            return retVal;
        }

        private void CheckProcesses()
        {
            int newPowerPlan = -1;
            List<Process> pendingProcessList;
            List<Process> processList = Process.GetProcesses().ToList();

            Monitor.Enter(this.processPrioritiesSyncObject);
            try
            {
                pendingProcessList = processList.Where(process => this.processPriorities.ContainsKey(process.ProcessName) && process.PriorityClass != this.IntToPrio(this.processPriorities[process.ProcessName])).ToList();
                foreach (Process pendingProcess in pendingProcessList)
                {
                    try
                    {
                        ProcessPriorityClass processPriorityClass = this.IntToPrio(this.processPriorities[pendingProcess.ProcessName]);
                        pendingProcess.PriorityClass = processPriorityClass;
                        this.Proto("Priority for " + pendingProcess.ProcessName + " set to " + processPriorityClass.ToString());
                    }
                    catch (Exception ex) { Debug.WriteLine("Exception caught:", ex.Message); }
                }
            }
            catch (Exception ex) { Debug.WriteLine("Exception caught:", ex.Message); }
            Monitor.Exit(this.processPrioritiesSyncObject);
            Thread.Sleep(0);

            Monitor.Enter(this.processAffinitiesSyncObject);
            try
            {
                pendingProcessList = processList.Where(process => this.processAffinities.ContainsKey(process.ProcessName) && process.ProcessorAffinity != (IntPtr)this.processAffinities[process.ProcessName]).ToList();
                foreach (Process pendingProcess in pendingProcessList)
                {
                    try
                    {
                        pendingProcess.ProcessorAffinity = (IntPtr)this.processAffinities[pendingProcess.ProcessName];
                        this.Proto("Affinity for " + pendingProcess.ProcessName + " set to " + this.AffinityToCores(this.processAffinities[pendingProcess.ProcessName]));
                    }
                    catch (Exception ex) { Debug.WriteLine("Exception caught:", ex.Message); }
                }
            }
            catch (Exception ex) { Debug.WriteLine("Exception caught:", ex.Message); }
            Monitor.Exit(this.processAffinitiesSyncObject);
            Thread.Sleep(0);

            Monitor.Enter(this.powerPlanSyncObject);
            PowerPlan currentPowerPlan = this.powerManager.GetCurrentPlan();
            try
            {
                pendingProcessList = processList.Where(process => this.powerPlans.ContainsKey(process.ProcessName)).ToList();
                foreach (Process pendingProcess in pendingProcessList)
                {
                    try
                    {
                        if (this.powerPlans[pendingProcess.ProcessName] > newPowerPlan) newPowerPlan = this.powerPlans[pendingProcess.ProcessName];
                    }
                    catch (Exception ex) { Debug.WriteLine("Exception caught:", ex.Message); }
                }
            }
            catch (Exception ex) { Debug.WriteLine("Exception caught:", ex.Message); }
            Monitor.Exit(this.powerPlanSyncObject);
            if (newPowerPlan >= 0)
            {
                if (!this.plans[newPowerPlan].Equals(this.powerManager.GetCurrentPlan()))
                {
                    this.powerManager.SetActive(this.plans[newPowerPlan]);
                    this.Proto("Powerplan set to " + this.plans[newPowerPlan].name);
                    this.powerPlanSet = true;
                }
            }
            else if (this.powerPlanSet)
            {
                if (!this.startupPlan.Equals(this.powerManager.GetCurrentPlan()))
                {
                    this.powerManager.SetActive(this.startupPlan);
                    this.Proto("Powerplan reset to " + this.startupPlan.name);
                    this.powerPlanSet = false;
                }
            }
            else if (!this.startupPlan.Equals(this.powerManager.GetCurrentPlan()))
            {
                this.startupPlan = this.powerManager.GetCurrentPlan();
                this.Proto("Default Powerplan changed to " + this.startupPlan.name);
            }
        }

        private void ReadProcessSettings()
        {
            String iniFileName = Path.GetDirectoryName(System.Reflection.Assembly.GetEntryAssembly().Location) + Path.DirectorySeparatorChar + INI_NAME;
            IniData iniData = this.iniParser.ReadFile(iniFileName);
            Dictionary<string, int> newProcessPriorities = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);
            Dictionary<string, int> newProcessAffinities = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);
            Dictionary<string, int> newPowerPlans = new Dictionary<string, int>(StringComparer.InvariantCultureIgnoreCase);
            foreach (KeyData keyData in iniData["Priority"])
            {
                try
                {
                    string theKey = keyData.KeyName;
                    int intValue;
                    if (!Int32.TryParse(keyData.Value, out intValue)) continue;
                    if (theKey.EndsWith(".exe", StringComparison.InvariantCultureIgnoreCase))
                        theKey = theKey.Substring(0, theKey.Length - 4);

                    newProcessPriorities[theKey] = intValue;

                    this.Proto("Priority Setting: " + theKey + " = " + IntToPrio(intValue).ToString());
                }
                catch { }
            }
            foreach (KeyData keyData in iniData["Affinity"])
            {
                try
                {
                    string theKey = keyData.KeyName;
                    int intValue;
                    if (!Int32.TryParse(keyData.Value, out intValue)) continue;
                    if (intValue < -1 || intValue > this.maxAffinity) continue;
                    if (intValue == -1) intValue = this.maxAffinity;

                    if (theKey.EndsWith(".exe", StringComparison.InvariantCultureIgnoreCase))
                        theKey = theKey.Substring(0, theKey.Length - 4);
                    newProcessAffinities[theKey] = intValue;

                    this.Proto("Affinity Setting: " + theKey + " = " + AffinityToCores(intValue));
                }
                catch { }
            }
            foreach (KeyData keyData in iniData["PowerPlan"])
            {
                try
                {
                    string theKey = keyData.KeyName;
                    int intValue;
                    if (!Int32.TryParse(keyData.Value, out intValue)) continue;
                    if (intValue < 0 || intValue >= this.plans.Count) continue;
                    intValue = this.plans.Count - 1 - intValue;
                    if (theKey.EndsWith(".exe", StringComparison.InvariantCultureIgnoreCase))
                        theKey = theKey.Substring(0, theKey.Length - 4);
                    newPowerPlans[theKey] = intValue;
                    this.Proto("PowerPlan Setting: " + theKey + " = " + this.plans[intValue].name);
                }
                catch { }
            }

            Monitor.Enter(this.processPrioritiesSyncObject);
            this.processPriorities.Clear();
            this.processPriorities = newProcessPriorities;
            Monitor.Exit(this.processPrioritiesSyncObject);
            Monitor.Enter(this.processAffinitiesSyncObject);
            this.processAffinities.Clear();
            this.processAffinities = newProcessAffinities;
            Monitor.Exit(this.processAffinitiesSyncObject);
            Monitor.Enter(this.powerPlanSyncObject);
            this.powerPlans.Clear();
            this.powerPlans = newPowerPlans;
            Monitor.Exit(this.powerPlanSyncObject);
        }

        private String AffinityToCores(int affinity)
        {
            StringBuilder affinityCores = new StringBuilder();
            for (int i = 0; i < (int)Math.Log(this.maxAffinity + 1, 2); i++)
            {
                int affinityMask = 1 << i;
                if ((affinity & affinityMask) != 0)
                {
                    if (affinityCores.Length != 0) affinityCores.Append("+");
                    int coreNum = i;
                    if (this.isHT) coreNum /= 2;
                    affinityCores.Append(coreNum + 1);
                    if (this.isHT && i % 2 != 0) affinityCores.Append("(HT)");
                }
            }
            return affinityCores.ToString();
        }

        private bool isStartupEnabled()
        {
            using (TaskService ts = new TaskService())
            {
                Task task = ts.FindTask("FUMS");
                return (task != null && task.Enabled);
            }
        }

        private bool setStartup(bool enableStartup)
        {
            using (TaskService ts = new TaskService())
            {
                Task task = ts.FindTask("FUMS");
                if (task != null) {
                    try
                    {
                        task.Folder.DeleteTask("FUMS");
                        if (!enableStartup) return true;
                    }
                    catch
                    {
                        if (!enableStartup) return false;
                    }
                } else if (!enableStartup) return true;

                TaskDefinition td = ts.NewTask();
                td.RegistrationInfo.Description = "Start FUMS with Windows";
                LogonTrigger logonTrigger = new LogonTrigger();
                logonTrigger.Delay = TimeSpan.FromSeconds(30);
                td.Triggers.Add(logonTrigger);
                td.Actions.Add(new ExecAction(System.Reflection.Assembly.GetEntryAssembly().Location));
                td.Principal.RunLevel = TaskRunLevel.Highest;
                td.Settings.StopIfGoingOnBatteries = false;
                td.Settings.DisallowStartIfOnBatteries = false;
                td.Settings.ExecutionTimeLimit = TimeSpan.Zero;
                td.Settings.IdleSettings.StopOnIdleEnd = false;
                return (ts.RootFolder.RegisterTaskDefinition("FUMS", td) != null);
            }
        }

        private void FUMS_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (e.CloseReason == CloseReason.UserClosing && !this.exitClicked)
            {
                e.Cancel = true;
                this.WindowState = FormWindowState.Minimized;
            }
        }

        private void checkBoxStartup_CheckStateChanged(object sender, EventArgs e)
        {
            if (this.skipStartupCheckStateChange)
            {
                this.skipStartupCheckStateChange = false;
                return;
            }

            this.setStartup(this.checkBoxStartup.CheckState == CheckState.Checked);
            if (this.isStartupEnabled() != (this.checkBoxStartup.CheckState == CheckState.Checked))
            {
                this.skipStartupCheckStateChange = true;
                if (this.checkBoxStartup.CheckState == CheckState.Checked)
                    this.checkBoxStartup.CheckState = CheckState.Unchecked;
                else
                    this.checkBoxStartup.CheckState = CheckState.Unchecked;
            }
        }
    }
}
