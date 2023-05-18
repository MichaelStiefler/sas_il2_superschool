namespace Manifest_Generator
{
    partial class ManifestGenerator
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ManifestGenerator));
            this.listBoxLog = new Manifest_Generator.FlickerFreeListBox();
            this.buttonCancel = new System.Windows.Forms.Button();
            this.backgroundHasher = new System.ComponentModel.BackgroundWorker();
            this.SuspendLayout();
            // 
            // listBoxLog
            // 
            this.listBoxLog.DrawMode = System.Windows.Forms.DrawMode.OwnerDrawFixed;
            this.listBoxLog.FormattingEnabled = true;
            this.listBoxLog.Location = new System.Drawing.Point(12, 12);
            this.listBoxLog.Name = "listBoxLog";
            this.listBoxLog.SelectionMode = System.Windows.Forms.SelectionMode.None;
            this.listBoxLog.Size = new System.Drawing.Size(600, 381);
            this.listBoxLog.TabIndex = 0;
            // 
            // buttonCancel
            // 
            this.buttonCancel.Font = new System.Drawing.Font("Microsoft Sans Serif", 18F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.buttonCancel.Location = new System.Drawing.Point(214, 399);
            this.buttonCancel.Name = "buttonCancel";
            this.buttonCancel.Size = new System.Drawing.Size(197, 36);
            this.buttonCancel.TabIndex = 1;
            this.buttonCancel.Text = "Cancel";
            this.buttonCancel.UseVisualStyleBackColor = true;
            this.buttonCancel.Click += new System.EventHandler(this.buttonCancel_Click);
            // 
            // backgroundHasher
            // 
            this.backgroundHasher.WorkerReportsProgress = true;
            this.backgroundHasher.DoWork += new System.ComponentModel.DoWorkEventHandler(this.CreateManifest_DoWork);
            this.backgroundHasher.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.CreateManifest_ProgressChanged);
            this.backgroundHasher.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.CreateManifest_RunWorkerCompleted);
            // 
            // ManifestGenerator
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(624, 441);
            this.Controls.Add(this.buttonCancel);
            this.Controls.Add(this.listBoxLog);
            this.DoubleBuffered = true;
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.MinimizeBox = false;
            this.Name = "ManifestGenerator";
            this.Opacity = 0.95D;
            this.Text = "Manifest Generator";
            this.Shown += new System.EventHandler(this.ManifestGenerator_Shown);
            this.ResumeLayout(false);

        }

        #endregion

        FlickerFreeListBox listBoxLog;
        private System.Windows.Forms.Button buttonCancel;
        private System.ComponentModel.BackgroundWorker backgroundHasher;
    }
}

