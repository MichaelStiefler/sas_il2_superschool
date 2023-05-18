namespace com.sas1946.il2.directx.dinput.Il2SwitchBox
{
    partial class Il2SwitchBox
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
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Il2SwitchBox));
            this.panelSwitches = new System.Windows.Forms.FlowLayoutPanel();
            this.listBoxSend = new System.Windows.Forms.ListBox();
            this.listBoxReceive = new System.Windows.Forms.ListBox();
            this.labelSend = new System.Windows.Forms.Label();
            this.labelReceive = new System.Windows.Forms.Label();
            this.notifyIcon = new System.Windows.Forms.NotifyIcon(this.components);
            this.SuspendLayout();
            // 
            // panelSwitches
            // 
            this.panelSwitches.Location = new System.Drawing.Point(12, 12);
            this.panelSwitches.Name = "panelSwitches";
            this.panelSwitches.Size = new System.Drawing.Size(459, 125);
            this.panelSwitches.TabIndex = 1;
            // 
            // listBoxSend
            // 
            this.listBoxSend.FormattingEnabled = true;
            this.listBoxSend.Location = new System.Drawing.Point(12, 169);
            this.listBoxSend.Name = "listBoxSend";
            this.listBoxSend.Size = new System.Drawing.Size(219, 199);
            this.listBoxSend.TabIndex = 2;
            // 
            // listBoxReceive
            // 
            this.listBoxReceive.FormattingEnabled = true;
            this.listBoxReceive.Location = new System.Drawing.Point(252, 169);
            this.listBoxReceive.Name = "listBoxReceive";
            this.listBoxReceive.Size = new System.Drawing.Size(219, 199);
            this.listBoxReceive.TabIndex = 3;
            // 
            // labelSend
            // 
            this.labelSend.AutoSize = true;
            this.labelSend.Location = new System.Drawing.Point(113, 153);
            this.labelSend.Name = "labelSend";
            this.labelSend.Size = new System.Drawing.Size(32, 13);
            this.labelSend.TabIndex = 4;
            this.labelSend.Text = "Send";
            // 
            // labelReceive
            // 
            this.labelReceive.AutoSize = true;
            this.labelReceive.Location = new System.Drawing.Point(352, 153);
            this.labelReceive.Name = "labelReceive";
            this.labelReceive.Size = new System.Drawing.Size(47, 13);
            this.labelReceive.TabIndex = 5;
            this.labelReceive.Text = "Receive";
            // 
            // notifyIcon
            // 
            this.notifyIcon.Icon = ((System.Drawing.Icon)(resources.GetObject("notifyIcon.Icon")));
            this.notifyIcon.Text = "IL-2 Switch Box";
            this.notifyIcon.Visible = true;
            // 
            // Il2SwitchBox
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(485, 372);
            this.Controls.Add(this.labelReceive);
            this.Controls.Add(this.labelSend);
            this.Controls.Add(this.listBoxReceive);
            this.Controls.Add(this.listBoxSend);
            this.Controls.Add(this.panelSwitches);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "Il2SwitchBox";
            this.Text = "Il-2 Switch Box";
            this.WindowState = System.Windows.Forms.FormWindowState.Minimized;
            this.Load += new System.EventHandler(this.Il2SwitchBox_Load);
            this.Shown += new System.EventHandler(this.Il2SwitchBox_Shown);
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Il2SwitchBox_FormClosing);
            this.Resize += new System.EventHandler(this.Il2SwitchBox_Resize);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.FlowLayoutPanel panelSwitches;
        private System.Windows.Forms.ListBox listBoxSend;
        private System.Windows.Forms.ListBox listBoxReceive;
        private System.Windows.Forms.Label labelSend;
        private System.Windows.Forms.Label labelReceive;
        private System.Windows.Forms.NotifyIcon notifyIcon;

    }
}

