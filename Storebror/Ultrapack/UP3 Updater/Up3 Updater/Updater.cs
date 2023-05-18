using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Reflection;
using System.Security.Cryptography;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml;

namespace Up3_Updater
{
    public partial class Updater : Form
    {
        public Updater()
        {
            InitializeComponent();
            this.workingFolder = Directory.GetCurrentDirectory();
            this.updaterUrl = "";
            this.outputFile = "";
            this.version = "";
            this.maxDiff = 2000000000;
            this.gameFiles = new SynchronizedCollection<GameFile>();
            this.webClients = new SynchronizedCollection<WebClient>();
            this.totalBytes = 0;
            this.syncedBytes = 0;
            this.unsyncedBytes = 0;
        }

        private string updaterUrl; // base URL of the Updater, taken from ManifestGeneratorProperties.xml
        private string outputFile; // output filename, taken from ManifestGeneratorProperties.xml
        private string version; // version information, taken from ManifestGeneratorProperties.xml
        private long maxDiff; // maximum number of bytes being out of sync - if more data is out of sync, a game reinstallation is required - , taken from ManifestGeneratorProperties.xml
        private string workingFolder; // the current working folder of this tool
        private SynchronizedCollection<GameFile> gameFiles; // game files to be dealt with by the Updater
        private SynchronizedCollection<WebClient> webClients; // game files to be dealt with by the Updater
        private long totalBytes;
        private long syncedBytes;
        private long unsyncedBytes;

        private static readonly char[] escape = { ' ', '\r', '\n' };

        public long TotalBytes
        {
            get => Interlocked.Read(ref this.totalBytes);
            set => Interlocked.Exchange(ref this.totalBytes, value);
        }
        public long SyncedBytes
        {
            get => Interlocked.Read(ref this.syncedBytes);
            set => Interlocked.Exchange(ref this.syncedBytes, value);
        }

        public long UnsyncedBytes
        {
            get => Interlocked.Read(ref this.unsyncedBytes);
            set => Interlocked.Exchange(ref this.unsyncedBytes, value);
        }


        private class UserState
        {
            public UserState(string message)
            {
                this.message = message;
            }
            public UserState(int percentage)
            {
                this.percentage = percentage;
            }
            public UserState(long total, long synced, long unsynced)
            {
                this.total = total;
                this.synced = synced;
                this.unsynced = unsynced;
            }
            public UserState(string message, long total, long synced, long unsynced, int percentage)
            {
                this.message = message;
                this.total = total;
                this.synced = synced;
                this.unsynced = unsynced;
                this.percentage = percentage;
            }

            public string message = "";
            public long total = 0;
            public long synced = 0;
            public long unsynced = 0;
            public int percentage = 0;
        }

        /// <summary>
        /// Internal Structure to hold information about game files
        /// </summary>
        private class GameFile : IComparable<GameFile>
        {
            public string fileName = "";
            public long size = 0;
            public string hash = "";
            public string url = "";
            public int CompareTo(GameFile other)
            {
                int retVal = Path.GetDirectoryName(fileName.ToLowerInvariant()).CompareTo(Path.GetDirectoryName(other.fileName.ToLowerInvariant()));
                if (retVal == 0)
                {
                    retVal = Path.GetFileName(fileName.ToLowerInvariant()).CompareTo(Path.GetFileName(other.fileName.ToLowerInvariant()));
                }
                return retVal;
            }
        }


        /// <summary>
        /// Delegate to add a log list entry from other threads than the UI thread
        /// </summary>
        /// <param name="theLine">The line.</param>
        private delegate void AddListBoxLineDelegate(string theLine);
        /// <summary>
        /// Adds a line to the log List.
        /// </summary>
        /// <param name="theLine">The line.</param>
        private void AddListBoxLine(string theLine)
        {
            if (this.InvokeRequired) // Check if method has been called from UI thread
            {
                // Not UI Thread --> Delegate method call to UI thread
                this.BeginInvoke(new AddListBoxLineDelegate(AddListBoxLine), new object[] { theLine });
            }
            else
            {
                // We're on the UI thread now. Make sure the Form is not about to be closed...
                if (this.IsDisposed || this.listBoxLog.IsDisposed) return;

                this.listBoxLog.BeginUpdate(); // Lock ListBox UI from updating
                this.listBoxLog.TopIndex = this.listBoxLog.Items.Add(theLine); // Add list item and make sure it's visible (auto-scroll)
                this.listBoxLog.EndUpdate(); // Unlock ListBox, let UI update contents
            }
        }

        /// <summary>
        /// Creates the manifest file.
        /// </summary>
        private void ReadProperties()
        {
            Stream s = Assembly.GetExecutingAssembly().GetManifestResourceStream("Up3_Updater." + Properties.Resources.PropertyFileName);
            XmlDocument properties = new XmlDocument();
            properties.Load(s); // Load settings from Updater.xml
            s.Close();

            // Get other settings from ManifestGeneratorProperties.xml
            this.updaterUrl = properties.SelectSingleNode("Updater/UpdaterUrl").InnerText.Trim(escape);
            this.outputFile = properties.SelectSingleNode("Updater/OutputFile").InnerText.Trim(escape);
            this.version = properties.SelectSingleNode("Updater/Version").InnerText.Trim(escape);
            Int64.TryParse(properties.SelectSingleNode("Updater/MaxDiff").InnerText.Trim(escape), out this.maxDiff);

            // Start the work. This takes place in a BackgroundWorker in order to keep the UI accessible
            this.backgroundWorker.RunWorkerAsync();
        }

        private void buttonCancel_Click(object sender, EventArgs e)
        {
            if (this.backgroundWorker.IsBusy)
            {
                this.backgroundWorker.CancelAsync();
                List<WebClient> theWebClients = new List<WebClient>(this.webClients);
                foreach (WebClient webClient in theWebClients)
                {
                    if (webClient != null) webClient.CancelAsync();
                }
                while (this.backgroundWorker.IsBusy)
                {
                    Thread.Sleep(0);
                    Application.DoEvents();
                }
                this.webClients.Clear();
                this.buttonCancel.Text = "Exit";
                return;
            }
            if (this.buttonCancel.Text.Equals("Exit")) this.Close();
            else this.buttonCancel.Text = "Exit";
        }

        private void Updater_Shown(object sender, EventArgs e)
        {
            this.ReadProperties();
        }

        private void backgroundWorker_DoWork(object sender, DoWorkEventArgs e)
        {
            BackgroundWorker worker = sender as BackgroundWorker;
            worker.ReportProgress(0, new UserState("Downloading Manifest File..."));
            if (!this.Download_Manifest(this.updaterUrl + this.outputFile, this.workingFolder + Path.DirectorySeparatorChar + this.outputFile, worker))
            {
                if (worker.CancellationPending)
                    worker.ReportProgress(0, new UserState("Cancellation requested, terminating."));
                else
                    worker.ReportProgress(0, new UserState("Manifest File Download failed, terminating."));
                return;
            }
            worker.ReportProgress(0, new UserState("Manifest File Download finished, checking local data..."));
            if (!this.GameFilesFromManifest(this.workingFolder + Path.DirectorySeparatorChar + this.outputFile, worker))
            {
                if (worker.CancellationPending)
                    worker.ReportProgress(0, new UserState("Cancellation requested, terminating."));
                else
                    worker.ReportProgress(0, new UserState("Parsing Manifest File failed, terminating."));
                return;
            }
            if (!CheckSyncedLocalFiles(worker))
            {
                if (worker.CancellationPending)
                    worker.ReportProgress(0, new UserState("Cancellation requested, terminating."));
                else
                    worker.ReportProgress(0, new UserState("Checking Local Files failed, terminating."));
                return;
            }
            if (this.UnsyncedBytes == 0)
            {
                worker.ReportProgress(0, new UserState("Game is up to date already, nothing left to sync."));
                Task.Run(() => { MessageBox.Show("Game is up to date!", "IL-2 1946 Updater", MessageBoxButtons.OK, MessageBoxIcon.Information); });
                return;
            }
            if (this.maxDiff > 0 && this.UnsyncedBytes > this.maxDiff)
            {
                worker.ReportProgress(0, new UserState("Game differs too much from Master Server, please reinstall."));
                Task.Run(() => { MessageBox.Show("Your game differs too much from the Master Server contents.\r\nPlease reinstall from scratch!", "IL-2 1946 Updater", MessageBoxButtons.OK, MessageBoxIcon.Information); });
                return;
            }

            //// TEST!!!
            //this.gameFiles.Clear();
            //GameFile gameFile = new GameFile();
            //gameFile.size = 154030389;
            //this.UnsyncedBytes += gameFile.size;
            //gameFile.hash = "20b8e6336365aa815dbf571e6deed01c";
            //gameFile.fileName = "fb_3do.SFS";
            //gameFile.url = "https://onedrive.live.com/download?cid=7F11E7867133791A&resid=7F11E7867133791A%21481&authkey=ACPmLjdrS7_CUUI";
            //this.gameFiles.Add(gameFile);
            //gameFile = new GameFile();
            //gameFile.size = 475859205;
            //this.UnsyncedBytes += gameFile.size;
            //gameFile.hash = "45d015969e67f3aaccb7670df95294a6";
            //gameFile.fileName = "fb_3do01.SFS";
            //gameFile.url = "https://onedrive.live.com/download?cid=7F11E7867133791A&resid=7F11E7867133791A%21491&authkey=AGdEVLPfK8y1a_I";
            //this.gameFiles.Add(gameFile);
            //gameFile = new GameFile();
            //gameFile.size = 23594482;
            //this.UnsyncedBytes += gameFile.size;
            //gameFile.hash = "1edd6718d8c9ceaf5eac0efd42edb087";
            //gameFile.fileName = "fb_3do02.SFS";
            //gameFile.url = "https://onedrive.live.com/download?cid=7F11E7867133791A&resid=7F11E7867133791A%21477&authkey=AEBoK7iBXU3kdt4";
            //this.gameFiles.Add(gameFile);
            //gameFile = new GameFile();
            //gameFile.size = 402718;
            //this.UnsyncedBytes += gameFile.size;
            //gameFile.hash = "03d69de4e5837fc26bfb8ef1a4948455";
            //gameFile.fileName = "fb_3do03.SFS";
            //gameFile.url = "https://onedrive.live.com/download?cid=7F11E7867133791A&resid=7F11E7867133791A%21478&authkey=AMegNOJ7aUp3ckg";
            //this.gameFiles.Add(gameFile);
            //gameFile = new GameFile();
            //gameFile.size = 46454030;
            //this.UnsyncedBytes += gameFile.size;
            //gameFile.hash = "62e21522c9495d97ca86bb89773ff193";
            //gameFile.fileName = "fb_3do04.SFS";
            //gameFile.url = "https://onedrive.live.com/download?cid=7F11E7867133791A&resid=7F11E7867133791A%21479&authkey=AOpluY-gUcexYRU";
            //this.gameFiles.Add(gameFile);
            //gameFile = new GameFile();
            //gameFile.size = 357751;
            //this.UnsyncedBytes += gameFile.size;
            //gameFile.hash = "1bc4e87e8544ca6a83bf23acd91bf27d";
            //gameFile.fileName = "fb_3do05.SFS";
            //gameFile.url = "https://onedrive.live.com/download?cid=7F11E7867133791A&resid=7F11E7867133791A%21480&authkey=AK-fZ79tApgCCRc";
            //this.gameFiles.Add(gameFile);

            //this.TotalBytes = this.UnsyncedBytes;

            if (!DownloadUnsyncedFiles(worker))
            {
                if (worker.CancellationPending)
                    worker.ReportProgress(0, new UserState("Cancellation requested, terminating."));
                else
                    worker.ReportProgress(0, new UserState("Downloading Files failed, terminating."));
                return;
            }
        }

        private void backgroundWorker_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            if (this.IsDisposed) return;
            UserState userState = e.UserState as UserState;
            switch (e.ProgressPercentage)
            {
                case 0:
                    this.AddListBoxLine(userState.message);
                    break;
                case 1:
                    this.toolStripStatusLabelTotal.Text = "Total: " + string.Format("{0:n0}", userState.total >> 10) + " kB";
                    this.toolStripStatusLabelSynced.Text = "Synced: " + string.Format("{0:n0}", userState.synced >> 10) + " kB";
                    this.toolStripStatusLabelUnsynced.Text = "Unsynced: " + string.Format("{0:n0}", userState.unsynced >> 10) + " kB";
                    break;
                case 2:
                    this.toolStripProgressBar.Visible = true;
                    if (userState.percentage < this.toolStripProgressBar.Minimum) userState.percentage = this.toolStripProgressBar.Minimum;
                    if (userState.percentage > this.toolStripProgressBar.Maximum) userState.percentage = this.toolStripProgressBar.Maximum;
                    this.toolStripProgressBar.Value = userState.percentage;
                    break;
                case 3:
                    if (userState.message.Length > 0) this.AddListBoxLine(userState.message);
                    if (userState.total > -1) this.toolStripStatusLabelTotal.Text = "Total: " + string.Format("{0:n0}", userState.total >> 10) + " kB";
                    if (userState.synced > -1) this.toolStripStatusLabelSynced.Text = "Synced: " + string.Format("{0:n0}", userState.synced >> 10) + " kB";
                    if (userState.unsynced > -1) this.toolStripStatusLabelUnsynced.Text = "Unsynced: " + string.Format("{0:n0}", userState.unsynced >> 10) + " kB";
                    if (userState.percentage > -1)
                    {
                        if (userState.percentage < this.toolStripProgressBar.Minimum) userState.percentage = this.toolStripProgressBar.Minimum;
                        if (userState.percentage > this.toolStripProgressBar.Maximum) userState.percentage = this.toolStripProgressBar.Maximum;
                        this.toolStripProgressBar.Visible = true;
                        this.toolStripProgressBar.Value = userState.percentage;
                    }
                    break;
                default:
                    break;
            }
        }

        private void backgroundWorker_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            this.buttonCancel.Text = "Exit";
        }

        private bool Download_Manifest(string url, string localFile, BackgroundWorker worker)
        {
            try
            {
                using (var sr = new StreamReader(HttpWebRequest.Create(url).GetResponse().GetResponseStream()))
                using (var sw = new StreamWriter(localFile))
                {
                    sw.Write(sr.ReadToEnd());
                }
                if (worker.CancellationPending) return false;
                return true;
            }
            catch
            {
                return false;
            }
        }

        private bool GameFilesFromManifest(string manifestPath, BackgroundWorker worker)
        {
            try
            {
                this.gameFiles.Clear();
                this.TotalBytes = 0;
                this.SyncedBytes = 0;
                this.UnsyncedBytes = 0;
                XmlDocument manifest = new XmlDocument();
                manifest.Load(manifestPath);
                XmlNode updateNode = manifest.SelectSingleNode("update");
                this.version = updateNode.Attributes["version"].Value;
                Int64.TryParse(updateNode.Attributes["maxdiff"].Value, out this.maxDiff);
                XmlNodeList files = manifest.SelectNodes("/update/file");
                foreach (XmlNode file in files)
                {
                    GameFile gameFile = new GameFile();
                    gameFile.fileName = file.Attributes["path"].Value;
                    Int64.TryParse(file.Attributes["size"].Value, out gameFile.size);
                    gameFile.hash = file.Attributes["md5"].Value;
                    gameFile.url = file.Attributes["src"].Value;
                    this.gameFiles.Add(gameFile);
                    if (worker.CancellationPending) return false;
                }
                return true;
            }
            catch
            {
                return false;
            }
        }

        private bool CheckSyncedLocalFiles(BackgroundWorker worker)
        {
            try
            {
                long totalBytes = 0;
                long syncedBytes = 0;
                long unsyncedBytes = 0;
                foreach (GameFile gameFile in this.gameFiles)
                {
                    if (worker.CancellationPending) return false;
                    totalBytes += gameFile.size;
                }
                this.TotalBytes = totalBytes;
                worker.ReportProgress(3, new UserState("", this.TotalBytes, this.SyncedBytes, this.UnsyncedBytes, 0));
                SynchronizedCollection<GameFile> temp = new SynchronizedCollection<GameFile>();
                Parallel.ForEach(
                this.gameFiles,
                new ParallelOptions { MaxDegreeOfParallelism = Environment.ProcessorCount - 1 },
                (gameFile, state) =>
                {
                    if (worker.CancellationPending) state.Stop();
                    string filename = this.workingFolder + Path.DirectorySeparatorChar + gameFile.fileName;
#if DEBUG
                    filename = this.workingFolder + Path.DirectorySeparatorChar + "Ultrapack 3" + Path.DirectorySeparatorChar + gameFile.fileName; // ONLY FOR TESTING!!!
#endif
                    bool isSynced = false;
                    if (File.Exists(filename))
                    {
                        long size = new System.IO.FileInfo(filename).Length;
                        if (size == gameFile.size)
                        {
                            string md5 = CalculateMD5(filename, size);
                            if (md5.Equals(gameFile.hash))
                            {
                                Interlocked.Add(ref syncedBytes, gameFile.size);
                                isSynced = true;
                                //this.gameFiles.Remove(gameFile);
                                temp.Add(gameFile);

                            }
                        }
                    }
                    if (!isSynced)
                    {
                        Interlocked.Add(ref unsyncedBytes, gameFile.size);
                    }
                    int percentage = Convert.ToInt32(Math.Ceiling((double)Interlocked.Read(ref syncedBytes) + (double)Interlocked.Read(ref unsyncedBytes)) / (double)Interlocked.Read(ref totalBytes) * 100.0);
                    worker.ReportProgress(3, new UserState("", Interlocked.Read(ref totalBytes), Interlocked.Read(ref syncedBytes), Interlocked.Read(ref unsyncedBytes), percentage));
                });
                if (worker.CancellationPending) return false;
                syncedBytes = 0;
                foreach (GameFile gameFile in temp)
                {
                    if (worker.CancellationPending) return false;
                    syncedBytes += gameFile.size;
                    this.gameFiles.Remove(gameFile);
                }
                this.TotalBytes = totalBytes;
                this.SyncedBytes = totalBytes - unsyncedBytes;
                this.UnsyncedBytes = unsyncedBytes;
                worker.ReportProgress(3, new UserState("", this.TotalBytes, this.SyncedBytes, this.UnsyncedBytes, 100));
                if (worker.CancellationPending) return false;
                return true;
            }
            catch (Exception excpt)
            {
                worker.ReportProgress(0, "Error in CheckSyncedLocalFiles: " + excpt.Message);
                return false;
            }
        }

        private bool DownloadUnsyncedFiles(BackgroundWorker worker)
        {
            try
            {
                long totalBytes = this.TotalBytes;
                long syncedBytes = this.SyncedBytes;
                long unsyncedBytes = this.UnsyncedBytes;
                long transferBytesTotal = unsyncedBytes;
                long transferredBytes = 0;
                int retVal = 1;
                this.webClients.Clear();
                Parallel.ForEach(
                this.gameFiles,
                new ParallelOptions { MaxDegreeOfParallelism = Math.Min(Environment.ProcessorCount - 1, 4) },
                (gameFile, state) =>
                {
                    if (worker.CancellationPending) state.Stop();
                    int maxRepeat = 10;
                    bool downloadFinished = false;
                    do
                    {
                        try
                        {
                            using (WebClient myWebClient = new WebClient())
                            {
                                if (worker.CancellationPending) break;
                                myWebClient.Credentials = CredentialCache.DefaultNetworkCredentials;
                                worker.ReportProgress(0, new UserState("Downloading " + gameFile.fileName + " ..."));
                                string localFileName = this.workingFolder + Path.DirectorySeparatorChar + gameFile.fileName;
                                this.webClients.Add(myWebClient);
                                Debug.WriteLine("Downloading " + gameFile.fileName + " to " + localFileName + ".updater");
                                myWebClient.DownloadFile(gameFile.url, localFileName + ".updater");
                                if (worker.CancellationPending) break;
                                if (File.Exists(localFileName))
                                {
                                    Debug.WriteLine("Deleting " + localFileName);
                                    File.Delete(localFileName);
                                }
                                Debug.WriteLine("Renaming " + localFileName + ".updater to ", localFileName);
                                File.Move(localFileName + ".updater", localFileName);
                                this.webClients.Remove(myWebClient);
                                downloadFinished = true;
                                Debug.WriteLine(gameFile.fileName + " unsyncedBytes before = " + Interlocked.Read(ref unsyncedBytes));
                                Interlocked.Add(ref unsyncedBytes, -1 * gameFile.size);
                                Debug.WriteLine(gameFile.fileName + " unsyncedBytes after = " + Interlocked.Read(ref unsyncedBytes));
                                Interlocked.Add(ref syncedBytes, gameFile.size);
                                Interlocked.Add(ref transferredBytes, gameFile.size);
                                int percentage = Convert.ToInt32(Math.Ceiling((double)Interlocked.Read(ref transferredBytes) / (double)Interlocked.Read(ref transferBytesTotal) * 100.0));
                                worker.ReportProgress(3, new UserState("Download of " + gameFile.fileName + " finished.", Interlocked.Read(ref totalBytes), Interlocked.Read(ref syncedBytes), Interlocked.Read(ref unsyncedBytes), percentage));
                                break;
                            }
                        }
                        catch (Exception excpt)
                        {
                            worker.ReportProgress(0, new UserState("Exception while D/L " + gameFile.fileName + " :"));
                            worker.ReportProgress(0, new UserState(excpt.Message));
                        }
                    } while (maxRepeat-- > 0);
                    if (worker.CancellationPending) state.Stop();
                    if (!downloadFinished) {
                        Interlocked.Exchange(ref retVal, 0);
                        state.Stop();
                    }
                });
                this.TotalBytes = totalBytes;
                this.SyncedBytes = syncedBytes;
                this.UnsyncedBytes = unsyncedBytes;
                int percentage2 = Convert.ToInt32(Math.Ceiling((double)syncedBytes / (double)totalBytes * 100.0));
                worker.ReportProgress(3, new UserState("", this.TotalBytes, this.SyncedBytes, this.UnsyncedBytes, percentage2));
                return (retVal != 0);
            }
            catch (Exception excpt)
            {
                worker.ReportProgress(0, new UserState("Error in DownloadUnsyncedFiles: " + excpt.Message));
                return false;
            }
        }

        public async Task DownloadFilesAsync(BackgroundWorker worker)
        {
            var allTasks = new List<Task>();
            var throttler = new SemaphoreSlim(initialCount: 4);
            long totalBytes = this.TotalBytes;
            long syncedBytes = this.SyncedBytes;
            long unsyncedBytes = this.UnsyncedBytes;
            long transferBytesTotal = unsyncedBytes;
            long transferredBytes = 0;
            foreach (GameFile gameFile in this.gameFiles)
            {
                // do an async wait until we can schedule again
                await throttler.WaitAsync();

                // using Task.Run(...) to run the lambda in its own parallel
                // flow on the threadpool
                allTasks.Add(
                    Task.Run(async () =>
                    {
                        bool downloadComplete = false;
                        int maxRepeat = 10;
                        do
                        {
                            try
                            {
                                using (WebClient webClient = new WebClient())
                                {
                                    webClient.Credentials = System.Net.CredentialCache.DefaultNetworkCredentials;
                                    worker.ReportProgress(0, "Downloading " + gameFile.fileName + " ...");
                                    await webClient.DownloadFileTaskAsync(gameFile.url, this.workingFolder + Path.DirectorySeparatorChar + gameFile.fileName);
                                    Interlocked.Add(ref unsyncedBytes, -gameFile.size);
                                    Interlocked.Add(ref syncedBytes, gameFile.size);
                                    Interlocked.Add(ref transferredBytes, gameFile.size);
                                    int percentage = Convert.ToInt32(Math.Ceiling((double)Interlocked.Read(ref transferredBytes) / (double)Interlocked.Read(ref transferBytesTotal) * 100.0));
                                    worker.ReportProgress(3, new UserState("Download of " + gameFile.fileName + " finished.", Interlocked.Read(ref totalBytes), Interlocked.Read(ref syncedBytes), Interlocked.Read(ref unsyncedBytes), percentage));
                                    downloadComplete = true;
                                    break;
                                }
                            }
                            catch (Exception)
                            {
                                await Task.Delay(30000);
                            }
                        } while (maxRepeat-- > 0);
                        if (!downloadComplete) worker.ReportProgress(0, "Error downloading " + gameFile.fileName);
                    }));
            }

            // won't get here until all urls have been put into tasks
            await Task.WhenAll(allTasks);
        }

        private async Task<bool> DownloadFileAsync(GameFile gameFile)
        {
            bool retVal = false;
            int maxRepeat = 10;
            do
            {
                try
                {
                    using (WebClient webClient = new WebClient())
                    {
                        webClient.Credentials = System.Net.CredentialCache.DefaultNetworkCredentials;
                        await webClient.DownloadFileTaskAsync(gameFile.url, this.workingFolder + Path.DirectorySeparatorChar + gameFile.fileName);
                        retVal = true;
                        break;
                    }
                }
                catch (Exception)
                {
                    await Task.Delay(30000);
                }
            } while (maxRepeat-- > 0);
            if (!retVal) AddListBoxLine("Error downloading " + gameFile.fileName);
            return retVal;
        }


        /// <summary>
        /// Calculates the md5 checksum for a given file.
        /// </summary>
        /// <param name="filename">The filename.</param>
        /// <param name="size">The file size.</param>
        /// <returns></returns>
        private string CalculateMD5(string filename, long size)
        {
            using (var md5 = MD5.Create())
            {
                using (var stream = new FileStream(filename, FileMode.Open, FileAccess.Read, FileShare.ReadWrite, Convert.ToInt32(Math.Min(size + 100.0, 1024 * 1024))))
                {
                    var hash = md5.ComputeHash(stream);
                    return BitConverter.ToString(hash).Replace("-", "").ToLowerInvariant();
                }
            }
        }

        private void Updater_FormClosing(object sender, FormClosingEventArgs e)
        {
            this.backgroundWorker.CancelAsync();
            foreach (WebClient webClient in this.webClients)
            {
                if (webClient != null && webClient.IsBusy) webClient.CancelAsync();
            }
            while (this.backgroundWorker.IsBusy)
            {
                Thread.Sleep(0);
                Application.DoEvents();
            }
        }
    }

    internal class FlickerFreeListBox : System.Windows.Forms.ListBox
    {
        public FlickerFreeListBox()
        {
            this.SetStyle(
                ControlStyles.OptimizedDoubleBuffer |
                ControlStyles.ResizeRedraw |
                ControlStyles.UserPaint,
                true);
            this.DrawMode = DrawMode.OwnerDrawFixed;
        }
        protected override void OnDrawItem(DrawItemEventArgs e)
        {
            if (this.Items.Count > 0)
            {
                e.DrawBackground();
                e.Graphics.DrawString(this.Items[e.Index].ToString(), e.Font, new SolidBrush(this.ForeColor), new PointF(e.Bounds.X, e.Bounds.Y));
            }
            base.OnDrawItem(e);
        }
        protected override void OnPaint(PaintEventArgs e)
        {
            Region iRegion = new Region(e.ClipRectangle);
            e.Graphics.FillRegion(new SolidBrush(this.BackColor), iRegion);
            if (this.Items.Count > 0)
            {
                for (int i = 0; i < this.Items.Count; ++i)
                {
                    System.Drawing.Rectangle irect = this.GetItemRectangle(i);
                    if (e.ClipRectangle.IntersectsWith(irect))
                    {
                        if ((this.SelectionMode == SelectionMode.One && this.SelectedIndex == i)
                        || (this.SelectionMode == SelectionMode.MultiSimple && this.SelectedIndices.Contains(i))
                        || (this.SelectionMode == SelectionMode.MultiExtended && this.SelectedIndices.Contains(i)))
                        {
                            OnDrawItem(new DrawItemEventArgs(e.Graphics, this.Font,
                                irect, i,
                                DrawItemState.Selected, this.ForeColor,
                                this.BackColor));
                        }
                        else
                        {
                            OnDrawItem(new DrawItemEventArgs(e.Graphics, this.Font,
                                irect, i,
                                DrawItemState.Default, this.ForeColor,
                                this.BackColor));
                        }
                        iRegion.Complement(irect);
                    }
                }
            }
            base.OnPaint(e);
        }
    }
}
