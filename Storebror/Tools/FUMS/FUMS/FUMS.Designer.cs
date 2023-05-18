namespace FUMS
{
    partial class FUMS
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
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FUMS));
            this.notifyIcon = new System.Windows.Forms.NotifyIcon(this.components);
            this.timerUpdate = new System.Windows.Forms.Timer(this.components);
            this.listBoxProto = new System.Windows.Forms.ListBox();
            this.checkBoxStartup = new System.Windows.Forms.CheckBox();
            this.SuspendLayout();
            // 
            // notifyIcon
            // 
            this.notifyIcon.Icon = ((System.Drawing.Icon)(resources.GetObject("notifyIcon.Icon")));
            this.notifyIcon.Text = "         FUMS\r\n    F*ck Up My System\r\nA Process Management Tool";
            this.notifyIcon.Visible = true;
            this.notifyIcon.DoubleClick += new System.EventHandler(this.notifyIcon_DoubleClick);
            // 
            // timerUpdate
            // 
            this.timerUpdate.Tick += new System.EventHandler(this.timerUpdate_Tick);
            // 
            // listBoxProto
            // 
            this.listBoxProto.FormattingEnabled = true;
            this.listBoxProto.Location = new System.Drawing.Point(12, 12);
            this.listBoxProto.Name = "listBoxProto";
            this.listBoxProto.ScrollAlwaysVisible = true;
            this.listBoxProto.Size = new System.Drawing.Size(417, 342);
            this.listBoxProto.TabIndex = 0;
            // 
            // checkBoxStartup
            // 
            this.checkBoxStartup.AutoSize = true;
            this.checkBoxStartup.Checked = true;
            this.checkBoxStartup.CheckState = System.Windows.Forms.CheckState.Indeterminate;
            this.checkBoxStartup.Location = new System.Drawing.Point(12, 360);
            this.checkBoxStartup.Name = "checkBoxStartup";
            this.checkBoxStartup.Size = new System.Drawing.Size(168, 17);
            this.checkBoxStartup.TabIndex = 1;
            this.checkBoxStartup.Text = "Start with Windows (Autostart)";
            this.checkBoxStartup.UseVisualStyleBackColor = true;
            this.checkBoxStartup.CheckStateChanged += new System.EventHandler(this.checkBoxStartup_CheckStateChanged);
            // 
            // FUMS
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(441, 384);
            this.Controls.Add(this.checkBoxStartup);
            this.Controls.Add(this.listBoxProto);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "FUMS";
            this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
            this.Text = "FUMS Process Management Tool";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.FUMS_FormClosing);
            this.Load += new System.EventHandler(this.FUMS_Load);
            this.Resize += new System.EventHandler(this.FUMS_Resize);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.NotifyIcon notifyIcon;
        private System.Windows.Forms.Timer timerUpdate;
        private System.Windows.Forms.ListBox listBoxProto;
        private System.Windows.Forms.CheckBox checkBoxStartup;
    }
}

