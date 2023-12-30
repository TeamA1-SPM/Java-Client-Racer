package main.menuhelper;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.List;

import static main.menuhelper.FontManager.*;

public class UserInputManager {

    public static final List<Character> UsernameList = new ArrayList<>();
    public static final List<Character> PasswordList = new ArrayList<>();

    public static final JTextField UsernameField = new JTextField();;
    public static final JPasswordField PasswordField = new JPasswordField();;


    // Sets up a username or password field and checks the maximum length
    public static void setupInputField(JComponent component, int maxLength) {
        PlainDocument document = new PlainDocument() {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                // Checks if the maximum length is exceeded
                if ((getLength() + str.length()) <= maxLength) {
                    super.insertString(offs, str, a);
                    for (char c : str.toCharArray()) {
                        if (component instanceof JPasswordField) {
                            PasswordList.add(c);
                        } else if (component instanceof JTextField) {
                            UsernameList.add(c);
                        }
                    }
                }
            }
            @Override
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                // Remove characters from the list when the user deletes them
                for (int i = 0; i < len; i++) {
                    if (component instanceof JPasswordField) {
                        PasswordList.remove(offs);
                    } else if (component instanceof JTextField) {
                        UsernameList.remove(offs);
                    }
                }
            }
        };
        // Assign document to the field
        if (component instanceof JPasswordField) {
            PasswordField.setDocument(document);
            PasswordField.setFont(FontSize30);
        } else if (component instanceof JTextField) {
            UsernameField.setDocument(document);
            UsernameField.setFont(FontSize30);
        }
    }

    // Extracts the content of the ArrayList into a String
    public static String listToString(List<Character> charList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : charList) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

}
