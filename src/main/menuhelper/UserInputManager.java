package main.menuhelper;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.ArrayList;
import java.util.List;

public class UserInputManager {

    private static final List<Character> usernameList = new ArrayList<>();
    private static final List<Character> passwordList = new ArrayList<>();

    public static List<Character> getUsernameList() { return usernameList; }
    public static List<Character> getPasswordList() { return passwordList; }

    private static final JTextField usernameField = new JTextField();;
    private static final JPasswordField passwordField = new JPasswordField();;

    public static JTextField getUsernameField() { return usernameField; }
    public static JPasswordField getPasswordField() { return passwordField; }

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
                            passwordList.add(c);
                        } else if (component instanceof JTextField) {
                            usernameList.add(c);
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
                        passwordList.remove(offs);
                    } else if (component instanceof JTextField) {
                        usernameList.remove(offs);
                    }
                }
            }
        };
        // Assign document to the field
        if (component instanceof JPasswordField) {
            passwordField.setDocument(document);
            passwordField.setFont(FontManager.getSize30());
        } else if (component instanceof JTextField) {
            usernameField.setDocument(document);
            usernameField.setFont(FontManager.getSize30());
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
