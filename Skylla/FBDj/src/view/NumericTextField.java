package view;

import java.util.regex.Pattern;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class NumericTextField extends JTextField {
    private static final long serialVersionUID = 1L;

    @Override
    protected Document createDefaultModel() {
        return new NumericDocument();
    }

    private static class NumericDocument extends PlainDocument {
        private static final long    serialVersionUID = 1L;

        // The regular expression to match input against (zero or more digits)
        private final static Pattern DIGITS           = Pattern.compile("\\d*");

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            // Only insert the text if it matches the regular expression
            if (str != null && DIGITS.matcher(str).matches())
                super.insertString(offs, str, a);
        }
    }
}