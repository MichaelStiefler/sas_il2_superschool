package com.maddox.gwindow;

import java.util.ArrayList;

import com.maddox.rts.VK;

public class GWindowEditBox extends GWindowDialogControl
    implements GWindowCellEdit
{

    public GWindowEditBox()
    {
        this.value = new StringBuffer();
        this.maxLength = 255;
        this.bCanEdit = true;
        this.bCanEditTab = true;
        this.bNumericOnly = false;
        this.bNumericFloat = false;
        this.bPassword = false;
        this.bSelectOnFocus = true;
        this.bDelayedNotify = false;
        this.offsetX = 0.0F;
        this.bAllSelected = false;
        this.caretOffset = 0;
        this.bShowCaret = false;
        this.caretTimeout = 0.0F;
        this.bControlDown = false;
        this.bShiftDown = false;
        this.bChangePending = false;
        this.bHistory = false;
        this.historyCur = 0;
        // TODO: +++ Enable Right Alt key by SAS~Storebror +++
        this.bAltDown = false;
        // TODO: --- Enable Right Alt key by SAS~Storebror ---
    }

    public void setHistory(boolean flag)
    {
        this.bHistory = flag;
        if(this.bHistory && this.historyList == null)
            this.historyList = new ArrayList();
        else
        if(!this.bHistory && this.historyList != null)
            this.historyList = null;
    }

    public void setEnable(boolean flag)
    {
        super.setEnable(flag);
        if(!this.bEnable)
        {
            this.bControlDown = false;
            this.bShiftDown = false;
            this.bShowCaret = false;
            this.bAllSelected = false;
            this.bChangePending = false;
            this.caretOffset = 0;
            // TODO: +++ Enable Right Alt key by SAS~Storebror +++
            this.bAltDown = false;
            // TODO: --- Enable Right Alt key by SAS~Storebror ---
        }
    }

    public void setEditable(boolean flag)
    {
        this.bCanEdit = flag;
    }

    public void setValue(String s)
    {
        this.setValue(s, true);
    }

    public void setValue(String s, boolean flag)
    {
        this.value.delete(0, this.value.length());
        this.value.append(s);
        if(this.caretOffset > this.value.length())
            this.caretOffset = this.value.length();
        if(flag)
            if(this.bDelayedNotify)
                this.bChangePending = true;
            else
                this.notify(2, 0);
    }

    public void setCellEditValue(Object obj)
    {
        this.setValue(obj.toString(), false);
    }

    public Object getCellEditValue()
    {
        return this.getValue();
    }

    public void clear()
    {
        this.clear(true);
    }

    public void clear(boolean flag)
    {
        this.caretOffset = 0;
        this.value.delete(0, this.value.length());
        this.bAllSelected = false;
        if(flag)
            if(this.bDelayedNotify)
                this.bChangePending = true;
            else
                this.notify(2, 0);
    }

    public void selectAll()
    {
        if(this.bCanEdit && this.value.length() > 0)
        {
            this.caretOffset = this.value.length();
            this.bAllSelected = true;
        }
    }

    public int getFirstChar()
    {
        if(this.value.length() > 0)
            return this.value.charAt(0);
        else
            return -1;
    }

    public String getValue()
    {
        return this.value.toString();
    }

    public void insert(String s)
    {
        for(int i = 0; i < s.length(); i++)
            this.insert(s.charAt(i));

    }

    public boolean insert(char c)
    {
        if(this.value.length() >= this.maxLength)
            return false;
        this.value.insert(this.caretOffset, c);
        this.caretOffset++;
        if(this.bDelayedNotify)
            this.bChangePending = true;
        else
            this.notify(2, 0);
        return true;
    }

    protected void startShowCaret()
    {
        this.caretTimeout = 0.3F;
        this.bShowCaret = true;
    }

    public boolean backspace()
    {
        if(this.caretOffset == 0)
            return false;
        this.value.delete(this.caretOffset - 1, this.caretOffset);
        this.caretOffset--;
        if(this.bDelayedNotify)
            this.bChangePending = true;
        else
            this.notify(2, 0);
        return true;
    }

    public boolean delete()
    {
        if(this.caretOffset == this.value.length())
            return false;
        this.value.delete(this.caretOffset, this.caretOffset + 1);
        if(this.bDelayedNotify)
            this.bChangePending = true;
        else
            this.notify(2, 0);
        return true;
    }

    public boolean wordLeft()
    {
        do
        {
            if(this.caretOffset <= 0)
                break;
            char c = this.value.charAt(this.caretOffset - 1);
            if(c != ' ' && c != '\t')
                break;
            this.caretOffset--;
        } while(true);
        do
        {
            if(this.caretOffset <= 0)
                break;
            char c1 = this.value.charAt(this.caretOffset - 1);
            if(c1 == ' ' || c1 == '\t')
                break;
            this.caretOffset--;
        } while(true);
        this.startShowCaret();
        return true;
    }

    public boolean moveLeft()
    {
        if(this.caretOffset == 0)
        {
            return false;
        } else
        {
            this.caretOffset--;
            this.startShowCaret();
            return true;
        }
    }

    public boolean moveRight()
    {
        if(this.caretOffset == this.value.length())
        {
            return false;
        } else
        {
            this.caretOffset++;
            this.startShowCaret();
            return true;
        }
    }

    public boolean wordRight()
    {
        do
        {
            if(this.caretOffset >= this.value.length())
                break;
            char c = this.value.charAt(this.caretOffset);
            if(c != ' ' && c != '\t')
                break;
            this.caretOffset++;
        } while(true);
        do
        {
            if(this.caretOffset >= this.value.length())
                break;
            char c1 = this.value.charAt(this.caretOffset);
            if(c1 == ' ' || c1 == '\t')
                break;
            this.caretOffset++;
        } while(true);
        this.startShowCaret();
        return true;
    }

    public boolean moveHome()
    {
        this.caretOffset = 0;
        this.startShowCaret();
        return true;
    }

    public boolean moveEnd()
    {
        this.caretOffset = this.value.length();
        this.startShowCaret();
        return true;
    }

    public void editCopy()
    {
        if(this.bAllSelected || !this.bCanEdit)
            this.root.C.copyToClipboard(this.value.toString());
    }

    public void editPaste()
    {
        if(this.bCanEdit)
        {
            if(this.bAllSelected)
                this.clear();
            this.insert(this.root.C.pasteFromClipboard());
        }
    }

    public void editCut()
    {
        if(this.bCanEdit)
        {
            if(this.bAllSelected)
            {
                this.root.C.copyToClipboard(this.value.toString());
                this.bAllSelected = false;
                this.clear();
            }
        } else
        {
            this.editCopy();
        }
    }

    public void keyboardChar(char c)
    {
        // TODO: +++ Enable Right Alt key by SAS~Storebror +++
//        if(bEnable && bCanEdit && !bControlDown)
        if(this.bEnable && this.bCanEdit && (!this.bControlDown || this.bControlDown && this.bAltDown))
        // TODO: --- Enable Right Alt key by SAS~Storebror ---
        {
            if(c == '\t' && !this.bCanEditTab)
                return;
            if(this.bAllSelected)
                this.clear();
            this.bAllSelected = false;
            if(this.bNumericOnly)
            {
                if(Character.isDigit(c))
                    this.insert(c);
                else
                if(c == '-' && this.value.length() == 0)
                    this.insert(c);
            } else
                // TODO: +++ Enable Right Alt key by SAS~Storebror +++
//            if(Character.isLetterOrDigit(c) || c >= ' ' && c < '\200' || c == '\t')
              if(Character.isLetterOrDigit(c) || c >= ' ' || c == '\t')
                // TODO: --- Enable Right Alt key by SAS~Storebror ---
                this.insert(c);
        }
    }

    public void keyboardKey(int i, boolean flag)
    {
        if(!this.bEnable)
        {
            super.keyboardKey(i, flag);
            return;
        }
        if(!flag)
        {
            switch(i)
            {
            default:
                break;

            case 9:
                if(!this.bCanEditTab)
                    break;
                // fall through

            case 8:
            case 35:
            case 36:
            case 37:
            case 39:
            case 127:
                if(this.bCanEdit)
                    return;
                break;

            case 17:
                this.bControlDown = false;
                break;
                
            // TODO: +++ Enable Right Alt key by SAS~Storebror +++
            case VK.ALT:
                this.bAltDown = false;
            // TODO: --- Enable Right Alt key by SAS~Storebror ---

            case 16:
                this.bShiftDown = false;
                break;

            case 38:
            case 40:
                if(this.bCanEdit && this.bHistory)
                    return;
                break;

            case 46:
                if(this.bCanEdit && this.bNumericFloat)
                    return;
                break;
            }
            super.keyboardKey(i, flag);
            return;
        }
        switch(i)
        {
        case 9:
            if(this.bCanEdit && !this.bCanEditTab)
                return;
            break;

        case 17:
            this.bControlDown = true;
            break;

            // TODO: +++ Enable Right Alt key by SAS~Storebror +++
            case VK.ALT:
                this.bAltDown = true;
            // TODO: --- Enable Right Alt key by SAS~Storebror ---

        case 16:
            this.bShiftDown = true;
            break;

        case 10:
            if(this.bCanEdit && this.bHistory && this.value.length() > 0)
                this.historyList.add(0, this.value.toString());
            break;

        case 39:
            this.bAllSelected = false;
            if(!this.bCanEdit)
                break;
            if(this.bControlDown)
                this.wordRight();
            else
                this.moveRight();
            return;

        case 37:
            this.bAllSelected = false;
            if(!this.bCanEdit)
                break;
            if(this.bControlDown)
                this.wordLeft();
            else
                this.moveLeft();
            return;

        case 38:
            if(!this.bCanEdit || !this.bHistory)
                break;
            this.bAllSelected = false;
            if(!this.historyList.isEmpty())
            {
                this.setValue((String)this.historyList.get(this.historyCur));
                this.moveEnd();
                int j = this.historyList.size();
                this.historyCur = (this.historyCur + 1) % j;
            }
            return;

        case 40:
            if(!this.bCanEdit || !this.bHistory)
                break;
            this.bAllSelected = false;
            if(!this.historyList.isEmpty())
            {
                int k = this.historyList.size();
                this.historyCur = ((this.historyCur - 1) + k) % k;
                this.setValue((String)this.historyList.get(this.historyCur));
                this.moveEnd();
            }
            return;

        case 36:
            this.bAllSelected = false;
            if(this.bCanEdit)
            {
                this.moveHome();
                return;
            }
            break;

        case 35:
            this.bAllSelected = false;
            if(this.bCanEdit)
            {
                this.moveEnd();
                return;
            }
            break;

        case 8:
            if(this.bCanEdit)
            {
                if(this.bAllSelected)
                    this.clear();
                else
                    this.backspace();
                this.bAllSelected = false;
                return;
            }
            this.bAllSelected = false;
            break;

        case 127:
            if(this.bCanEdit)
            {
                if(this.bAllSelected)
                    this.clear();
                else
                    this.delete();
                this.bAllSelected = false;
                return;
            }
            this.bAllSelected = false;
            break;

        case 46:
            if(this.bCanEdit && this.bNumericFloat)
            {
                this.insert('.');
                return;
            }
            break;

        default:
            if(!this.bControlDown)
                break;
            // TODO: +++ Enable Right Alt key by SAS~Storebror +++
            if(this.bAltDown)
                break;
            // TODO: --- Enable Right Alt key by SAS~Storebror ---
            if(i == 67)
                this.editCopy();
            if(i == 86)
                this.editPaste();
            if(i == 88)
                this.editCut();
            break;
        }
        super.keyboardKey(i, flag);
    }

    public void close(boolean flag)
    {
        if(this.bEnable && this.bChangePending)
        {
            this.bChangePending = false;
            this.notify(2, 0);
        }
        super.close(flag);
    }

    public void keyFocusEnter()
    {
        if(this.bEnable && this.bSelectOnFocus)
            this.selectAll();
        super.keyFocusEnter();
    }

    public void keyFocusExit()
    {
        if(this.bEnable)
        {
            this.bAllSelected = false;
            if(this.bChangePending)
            {
                this.bChangePending = false;
                this.notify(2, 0);
            }
        }
        this.bControlDown = false;
        this.bShiftDown = false;
        // TODO: +++ Enable Right Alt key by SAS~Storebror +++
        this.bAltDown = false;
        // TODO: --- Enable Right Alt key by SAS~Storebror ---
        super.keyFocusExit();
    }

    public void mouseButton(int i, boolean flag, float f, float f1)
    {
        super.mouseButton(i, flag, f, f1);
        if(!this.bEnable || !this.bCanEdit || i != 0 || !flag)
        {
            return;
        } else
        {
            f -= this.offsetX;
            GFont gfont = this.root.textFonts[this.font];
            this.caretOffset = gfont.len(this.getValue(), f, true, false);
            this.startShowCaret();
            this.bAllSelected = false;
            return;
        }
    }

    public void mouseDoubleClick(int i, float f, float f1)
    {
        super.mouseDoubleClick(i, f, f1);
        if(this.bEnable && i == 0)
            this.selectAll();
    }

    public boolean _notify(int i, int j)
    {
        if(i == 2)
        {
            if(!this.bChangePending)
                return true;
            this.bChangePending = false;
        }
        return this.notify(i, j);
    }

    public void checkCaretTimeout()
    {
        if(!this.isKeyFocus() || !this.bCanEdit || !this.isActivated())
        {
            this.bShowCaret = false;
            this.bAllSelected = false;
            return;
        }
        float f = this.root.deltaTimeSec;
        this.caretTimeout -= f;
        if(this.caretTimeout <= 0.0F)
        {
            this.bShowCaret = !this.bShowCaret;
            this.caretTimeout = 0.3F;
        }
    }

    public void render()
    {
        this.lookAndFeel().render(this, this.offsetX);
        this.checkCaretTimeout();
    }

    public void created()
    {
        this.bEnableDoubleClick[0] = true;
        super.created();
    }

    public StringBuffer value;
    public int maxLength;
    public boolean bCanEdit;
    public boolean bCanEditTab;
    public boolean bNumericOnly;
    public boolean bNumericFloat;
    public boolean bPassword;
    public boolean bSelectOnFocus;
    public boolean bDelayedNotify;
    public float offsetX;
    public boolean bAllSelected;
    public int caretOffset;
    public boolean bShowCaret;
    public float caretTimeout;
    public boolean bControlDown;
    public boolean bShiftDown;
    public boolean bChangePending;
    public boolean bHistory;
    public ArrayList historyList;
    public int historyCur;
    // TODO: +++ Enable Right Alt key by SAS~Storebror +++
    public boolean bAltDown;
    // TODO: --- Enable Right Alt key by SAS~Storebror ---
}
