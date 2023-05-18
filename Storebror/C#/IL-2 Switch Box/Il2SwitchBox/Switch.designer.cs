namespace com.sas1946.il2.directx.dinput.Switch
{
    partial class Switch
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.swtStatus = new System.Windows.Forms.CheckBox();
            this.SuspendLayout();
            // 
            // swtStatus
            // 
            this.swtStatus.AutoSize = true;
            this.swtStatus.Location = new System.Drawing.Point(3, 3);
            this.swtStatus.Name = "swtStatus";
            this.swtStatus.Size = new System.Drawing.Size(67, 17);
            this.swtStatus.TabIndex = 0;
            this.swtStatus.TabStop = true;
            this.swtStatus.Text = "Switch #";
            this.swtStatus.UseVisualStyleBackColor = true;
            // 
            // Switch
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.swtStatus);
            this.Name = "Switch";
            this.Size = new System.Drawing.Size(123, 27);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.CheckBox swtStatus;
    }
}
