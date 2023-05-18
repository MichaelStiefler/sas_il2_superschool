using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Security.Cryptography;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml;

namespace Manifest_Generator
{
    public partial class ManifestGenerator : Form
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="ManifestGenerator"/> class.
        /// </summary>
        public ManifestGenerator()
        {
            InitializeComponent();
            this.workingFolder = Directory.GetCurrentDirectory();
            this.ignoreFiles = new List<Regex>();
            this.gameFiles = new SynchronizedCollection<GameFile>();
            this.alternativeUrls = new List<AlternativeUrl>();
            this.updaterUrl = "";
            this.outputFile = "";
            this.version = "";
            this.maxDiff = 2000000000;
            this.pendingLogEntries = new List<string>();
            this.lastListBoxUpdate = Environment.TickCount;
        }

        /// <summary>
        /// Internal Structure to hold information about game files
        /// </summary>
        private class GameFile : IComparable<GameFile>
        {
            public string fileName;
            public long size;
            public string hash;
            public string url;
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

        private class AlternativeUrl // : IEquatable<string>
        {
            public string fileName;
            public string alternativeUrl;
            public AlternativeUrl(string fileName, string alternativeUrl)
            {
                this.fileName = fileName;
                this.alternativeUrl = alternativeUrl;
            }

            //public bool Equals(string other)
            //{
            //    return (other.Equals(this.fileName, StringComparison.InvariantCultureIgnoreCase));
            //}
        }

        private static readonly char[] escape = { ' ', '\r', '\n' };

        private List<Regex> ignoreFiles; // a list of Regular Expressions for files and folders to be ignored, taken from ManifestGeneratorProperties.xml
        private SynchronizedCollection<GameFile> gameFiles; // game files to be dealt with by the Updater
        private string updaterUrl; // base URL of the Updater, taken from ManifestGeneratorProperties.xml
        private string outputFile; // output filename, taken from ManifestGeneratorProperties.xml
        private string version; // version information, taken from ManifestGeneratorProperties.xml
        private long maxDiff; // maximum number of bytes being out of sync - if more data is out of sync, a game reinstallation is required - , taken from ManifestGeneratorProperties.xml
        private string workingFolder; // the current working folder of this tool
        private List<string> pendingLogEntries; // list of log List entries pending UI update
        private List<AlternativeUrl> alternativeUrls; // Files to be downloaded from elsewhere but the Update Server

        private int lastListBoxUpdate; // this is the tick count of the last log list UI update. Used to keep the UI responsive

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
        /// Adds a regular expression, representing one of the files to ignore, to the ignored files list.
        /// </summary>
        /// <param name="fileName">Name of the file.</param>
        private void AddIgnoreFile(string fileName)
        {
            // Turn "dir" style file/folder wildcards into valid regular expressions
            Regex mask = new Regex("^" + fileName.Replace("\\", "\\\\").Replace(".", "[.]").Replace("*", ".*").Replace("?", "."), RegexOptions.IgnoreCase);
            this.ignoreFiles.Add(mask);
        }

        /// <summary>
        /// Creates the manifest file.
        /// </summary>
        private void CreateManifest()
        {
            ignoreFiles.Clear();
            this.AddListBoxLine("Parsing Property File");
            XmlDocument properties = new XmlDocument();
            properties.Load(this.workingFolder + Path.DirectorySeparatorChar + Properties.Resources.PropertyFileName); // Load settings from ManifestGeneratorProperties.xml

            // Get common settings from ManifestGeneratorProperties.xml
            XmlNode commonNode = properties.SelectSingleNode("ManifestGenerator/Common");
            this.updaterUrl = commonNode.Attributes["UpdaterUrl"].Value;
            this.outputFile = commonNode.Attributes["OutputFile"].Value;
            this.version = commonNode.Attributes["Version"].Value;
            Int64.TryParse(commonNode.Attributes["MaxDiff"].Value, out this.maxDiff);


            // Parse files/folders to ignore
            XmlNodeList ignoreFilesList = properties.SelectNodes("/ManifestGenerator/IgnoreFiles/File");
            foreach (XmlNode ignoreFile in ignoreFilesList)
            {
                this.AddIgnoreFile(ignoreFile.Attributes["searchpattern"].Value);
            }
            this.AddIgnoreFile(Path.DirectorySeparatorChar + AppDomain.CurrentDomain.FriendlyName); // Add own App to "ignore" list
            this.AddIgnoreFile(Path.DirectorySeparatorChar + Properties.Resources.PropertyFileName); // Add settings file to "ignore" list
            this.AddIgnoreFile(Path.DirectorySeparatorChar + this.outputFile); // Add output file to "ignore" list

            // Parse files to download from alternative URL
            XmlNodeList alternativeUrlList = properties.SelectNodes("/ManifestGenerator/AlternativeUrls/File");
            foreach (XmlNode alternativeUrl in alternativeUrlList)
            {
                AlternativeUrl newAlternativeUrl = new AlternativeUrl(alternativeUrl.Attributes["name"].Value, alternativeUrl.Attributes["url"].Value);
                this.alternativeUrls.Add(newAlternativeUrl);
            }

            this.gameFiles.Clear();

            // Start the work. This takes place in a BackgroundWorker in order to keep the UI accessible
            this.backgroundHasher.RunWorkerAsync();
        }

        /// <summary>
        /// Handles the DoWork event of the CreateManifest control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="DoWorkEventArgs"/> instance containing the event data.</param>
        private void CreateManifest_DoWork(object sender, DoWorkEventArgs e)
        {
            BackgroundWorker worker = sender as BackgroundWorker;
            this.HashFolder(this.workingFolder, worker);
            worker.ReportProgress(100); // Ensure that pending log list entries are being shown and updated
            worker.ReportProgress(99,"List Size = " + this.gameFiles.Count);
            worker.ReportProgress(99, "Writing manifest file...");
            this.WriteManifestFile(this.workingFolder, worker);
            worker.ReportProgress(99, "Finished.");
        }

        /// <summary>
        /// Handles the ProgressChanged event of the CreateManifest control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="ProgressChangedEventArgs"/> instance containing the event data.</param>
        private void CreateManifest_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            if (this.IsDisposed || this.buttonCancel.IsDisposed) return;
            string relativeFilePath = e.UserState as String;
            if (e.ProgressPercentage == 0)
                this.pendingLogEntries.Add("Checking File " + relativeFilePath);
            else if (e.ProgressPercentage == 1)
                if (this.pendingLogEntries.Contains("Checking File " + relativeFilePath))
                    this.pendingLogEntries[this.pendingLogEntries.IndexOf("Checking File " + relativeFilePath)] = "Checking File " + relativeFilePath + " - Checked!";
                else
                    this.pendingLogEntries.Add("Checking File " + relativeFilePath + " - Checked!");
            else if (e.ProgressPercentage == 99)
            {
                this.AddListBoxLine(relativeFilePath);
            }
            if (e.ProgressPercentage < 100 && Environment.TickCount - this.lastListBoxUpdate < 10)
                return;
            this.UpdatePendingListboxEntries();
        }

        /// <summary>
        /// Handles the RunWorkerCompleted event of the CreateManifest control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="RunWorkerCompletedEventArgs"/> instance containing the event data.</param>
        private void CreateManifest_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            this.buttonCancel.Text = "Exit";
        }

        /// <summary>
        /// Updates the log List from pending entries.
        /// </summary>
        private void UpdatePendingListboxEntries()
        {
            this.listBoxLog.BeginUpdate();
            foreach (string logEntry in this.pendingLogEntries)
            {
                if (logEntry.EndsWith(" - Checked!"))
                {
                    string searchElement = logEntry.Substring(0, logEntry.Length - 11);
                    if (this.listBoxLog.Items.Contains(searchElement))
                        this.listBoxLog.Items[this.listBoxLog.Items.IndexOf(searchElement)] = logEntry;
                    else
                        this.listBoxLog.Items.Add(logEntry);
                }
                else
                    this.listBoxLog.Items.Add(logEntry);
            }
            this.pendingLogEntries.Clear();
            this.listBoxLog.TopIndex = this.listBoxLog.Items.Count - 1;
            this.listBoxLog.EndUpdate();
            this.lastListBoxUpdate = Environment.TickCount;
        }

        /// <summary>
        /// Hashes the game files in parallel (multi threaded) execution.
        /// </summary>
        /// <param name="folderName">Name of the folder.</param>
        /// <param name="worker">The worker.</param>
        private void HashFilesParallel(string folderName, BackgroundWorker worker)
        {
            String[] files = Directory.GetFiles(folderName);
            Parallel.ForEach(
            files,
            (currentFile) =>
            {
                if (currentFile.StartsWith(this.workingFolder))
                {
                    string relativeFilePath = currentFile.Remove(0, this.workingFolder.Length);
                    bool ignore = false;
                    foreach (Regex ignoreFile in this.ignoreFiles)
                    {
                        if (ignoreFile.IsMatch(relativeFilePath))
                        {
                            ignore = true;
                            break;
                        }
                    }
                    if (!ignore)
                    {
                        relativeFilePath = relativeFilePath.Remove(0, 1);
                        worker.ReportProgress(0, relativeFilePath);
                        GameFile gameFile = new GameFile();
#if DEBUG
                        if (relativeFilePath.StartsWith("Ultrapack 3\\")) relativeFilePath = relativeFilePath.Remove(0, 12); // JUST FOR TESTING!
#endif
                        int alternativeIndex = this.alternativeUrls.FindIndex(alternativeUrl => alternativeUrl.fileName.Equals(relativeFilePath, StringComparison.InvariantCultureIgnoreCase));
                        gameFile.fileName = relativeFilePath;
                        gameFile.size = new System.IO.FileInfo(currentFile).Length;
                        gameFile.hash = CalculateMD5(currentFile, gameFile.size);
                        if (alternativeIndex == -1)
                            gameFile.url = this.updaterUrl + Uri.EscapeUriString(relativeFilePath.Replace(Path.DirectorySeparatorChar, '/'));
                        else
                            gameFile.url = this.alternativeUrls[alternativeIndex].alternativeUrl;
                        this.gameFiles.Add(gameFile);
                        worker.ReportProgress(1, relativeFilePath);
                    }
                }
                else
                {
                    worker.ReportProgress(99, "File " + currentFile + " doesn't seem to belong to working folder");
                }

            });
        }

        /// <summary>
        /// Checks whether the folder in question is on the ignored list.
        /// </summary>
        /// <param name="folderName">Name of the folder.</param>
        /// <returns></returns>
        private bool CheckIgnoreFolder(string folderName)
        {
            if (!folderName.StartsWith(this.workingFolder)) return false;
            string relativeFolderPath = folderName.Remove(0, this.workingFolder.Length) + Path.DirectorySeparatorChar;
#if DEBUG
            if (relativeFolderPath.StartsWith("\\Ultrapack 3")) relativeFolderPath = relativeFolderPath.Remove(0, 12); // JUST FOR TESTING!
#endif
            bool ignore = false;
            foreach (Regex ignoreFile in this.ignoreFiles)
            {
                if (ignoreFile.ToString().EndsWith(".*"))
                {
                    if (ignoreFile.IsMatch(relativeFolderPath))
                    {
                        ignore = true;
                        break;
                    }
                }
            }
            return ignore;
        }

        /// <summary>
        /// Hashes the folder's files.
        /// </summary>
        /// <param name="folderName">Name of the folder.</param>
        /// <param name="worker">The worker.</param>
        private void HashFolder(string folderName, BackgroundWorker worker)
        {
            try
            {
                HashFilesParallel(folderName, worker);
                foreach (string currentFolder in Directory.GetDirectories(folderName))
                {
                    if (CheckIgnoreFolder(currentFolder))
                    {
                        worker.ReportProgress(99, "Skipping folder " + currentFolder.Remove(0, this.workingFolder.Length + 1));
                        continue;
                    }
                    HashFolder(currentFolder, worker);
                }
            }
            catch (System.Exception excpt)
            {
                worker.ReportProgress(99, "Error Hashing Folder " + folderName + ":" + excpt.Message);
            }
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

        /// <summary>
        /// Writes the manifest file.
        /// </summary>
        /// <param name="folderName">Name of the folder where the manifest file will be stored.</param>
        private void WriteManifestFile(string folderName, BackgroundWorker worker)
        {
            try
            {
                XmlDocument doc = new XmlDocument();
                XmlNode docNode = doc.CreateXmlDeclaration("1.0", "UTF-8", "yes");
                doc.AppendChild(docNode);
                XmlNode updateNode = doc.CreateElement("update");
                XmlAttribute versionAttribute = doc.CreateAttribute("version");
                versionAttribute.Value = this.version;
                updateNode.Attributes.Append(versionAttribute);
                XmlAttribute maxdiffAttribute = doc.CreateAttribute("maxdiff");
                maxdiffAttribute.Value = this.maxDiff.ToString();
                updateNode.Attributes.Append(maxdiffAttribute);
                doc.AppendChild(updateNode);

                List<GameFile> theGameFiles = new List<GameFile>(this.gameFiles);
                theGameFiles.Sort();

                //foreach (GameFile gameFile in this.gameFiles)
                foreach (GameFile gameFile in theGameFiles)
                {
                    XmlNode fileNode = doc.CreateElement("file");
                    XmlAttribute pathAttribute = doc.CreateAttribute("path");
                    pathAttribute.Value = gameFile.fileName.Replace(Path.DirectorySeparatorChar, '/');
                    fileNode.Attributes.Append(pathAttribute);
                    XmlAttribute sizeAttribute = doc.CreateAttribute("size");
                    sizeAttribute.Value = gameFile.size.ToString();
                    fileNode.Attributes.Append(sizeAttribute);
                    XmlAttribute md5Attribute = doc.CreateAttribute("md5");
                    md5Attribute.Value = gameFile.hash;
                    fileNode.Attributes.Append(md5Attribute);
                    XmlAttribute srcAttribute = doc.CreateAttribute("src");
                    srcAttribute.Value = gameFile.url;
                    fileNode.Attributes.Append(srcAttribute);
                    updateNode.AppendChild(fileNode);
                }
                doc.Save(folderName + Path.DirectorySeparatorChar + this.outputFile);
            }
            catch (System.Exception excpt)
            {
                worker.ReportProgress(99, "Error writing Manifest File " + this.outputFile + ":" + excpt.Message);
            }
        }

        /// <summary>
        /// Handles the Shown event of the ManifestGenerator control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="EventArgs"/> instance containing the event data.</param>
        private void ManifestGenerator_Shown(object sender, EventArgs e)
        {
            this.AddListBoxLine("App Start");
            this.CreateManifest();
        }

        /// <summary>
        /// Handles the Click event of the buttonCancel control.
        /// </summary>
        /// <param name="sender">The source of the event.</param>
        /// <param name="e">The <see cref="EventArgs"/> instance containing the event data.</param>
        private void buttonCancel_Click(object sender, EventArgs e)
        {
            this.AddListBoxLine("App End");
            this.Close();
        }
    }

    /// <summary>
    /// A flicker free ListBox derivative
    /// </summary>
    /// <seealso cref="System.Windows.Forms.ListBox" />
    internal class FlickerFreeListBox : System.Windows.Forms.ListBox
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="FlickerFreeListBox"/> class.
        /// </summary>
        public FlickerFreeListBox()
        {
            this.SetStyle(
                ControlStyles.OptimizedDoubleBuffer |
                ControlStyles.ResizeRedraw |
                ControlStyles.UserPaint,
                true);
            this.DrawMode = DrawMode.OwnerDrawFixed;
        }


        /// <summary>
        /// Raises the <see cref="E:System.Windows.Forms.ListBox.DrawItem" />-Event.
        /// </summary>
        /// <param name="e">A <see cref="T:System.Windows.Forms.DrawItemEventArgs" />, object holding additional Event Data.</param>
        protected override void OnDrawItem(DrawItemEventArgs e)
        {
            if (this.Items.Count > 0)
            {
                e.DrawBackground();
                e.Graphics.DrawString(this.Items[e.Index].ToString(), e.Font, new SolidBrush(this.ForeColor), new PointF(e.Bounds.X, e.Bounds.Y));
            }
            base.OnDrawItem(e);
        }
        /// <summary>
        /// Raises the <see cref="E:System.Windows.Forms.Control.Paint" />-Event.
        /// </summary>
        /// <param name="e">A <see cref="T:System.Windows.Forms.PaintEventArgs" />, object holding additional Event Data.</param>
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
