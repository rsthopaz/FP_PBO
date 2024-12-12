import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class Login {
    String username = "thopaz";
    char[] password = "autis".toCharArray();
    JTextField inputUsername;
    JPasswordField inputPassword;

    public void setupPanel() {
        JFrame frame = new JFrame("Login System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelUsername = new JLabel("Username: ");
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(labelUsername, gbc);

        inputUsername = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(inputUsername, gbc);

        JLabel labelPassword = new JLabel("Password:");
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(labelPassword, gbc);

        inputPassword = new JPasswordField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(inputPassword, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginListener(frame));
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(loginButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new CancelListener());
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(cancelButton, gbc);

        frame.getContentPane().add(panel);
        frame.setSize(400, 200);
        frame.setVisible(true);
    }

    public class LoginListener implements ActionListener {
        JFrame loginFrame;

        public LoginListener(JFrame frame) {
            this.loginFrame = frame;
        }

        public void actionPerformed(ActionEvent event) {
            if (username.equals(inputUsername.getText()) && Arrays.equals(password, inputPassword.getPassword())) {
                JOptionPane.showMessageDialog(null, "Login successful!");
                loginFrame.dispose(); // Close the login window
                openBookDatabase();  // Open the new page
            } else {
                JOptionPane.showMessageDialog(null, "Login failed!");
            }
        }

        private void openBookDatabase() {
            JFrame bookFrame = new JFrame("Library Database");
            bookFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Sample book data
            String[][] bookData = {
                {"1", "To Kill a Mockingbird", "Harper Lee", "1960"},
                {"2", "1984", "George Orwell", "1949"},
                {"3", "Moby Dick", "Herman Melville", "1851"},
                {"4", "The Great Gatsby", "F. Scott Fitzgerald", "1925"}
            };

            // Column headers
            String[] columnNames = {"ID", "Title", "Author", "Year"};

            // Create table
            JTable bookTable = new JTable(bookData, columnNames);
            JScrollPane scrollPane = new JScrollPane(bookTable);

            bookFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
            bookFrame.setSize(500, 300);
            bookFrame.setVisible(true);
        }
    }

    public class CancelListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            inputUsername.setText("");
            inputPassword.setText("");
            inputUsername.requestFocus();
        }
    }

    public static void main(String[] args) {
        Login login = new Login();
        login.setupPanel();
    }
}
