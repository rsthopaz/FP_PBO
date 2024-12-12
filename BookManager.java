import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;

public class BookManager {
    JFrame frame;
    JTable bookTable;
    DefaultTableModel tableModel;

    public BookManager() {
        // Setup Frame
        frame = new JFrame("Library Book Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Table Model
        String[] columnNames = {"ID", "Title", "Author", "Year"};
        tableModel = new DefaultTableModel(columnNames, 0);
        bookTable = new JTable(tableModel);

        // Sample Data
        tableModel.addRow(new Object[]{"1", "To Kill a Mockingbird", "Harper Lee", "1960"});
        tableModel.addRow(new Object[]{"2", "1984", "George Orwell", "1949"});
        tableModel.addRow(new Object[]{"3", "Moby Dick", "Herman Melville", "1851"});
        tableModel.addRow(new Object[]{"4", "The Great Gatsby", "F. Scott Fitzgerald", "1925"});

        // Add Table to ScrollPane
        JScrollPane scrollPane = new JScrollPane(bookTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        // CRUD Buttons
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        addButton.addActionListener(new AddBookListener());
        editButton.addActionListener(new EditBookListener());
        deleteButton.addActionListener(new DeleteBookListener());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    // Listener for Add Book
    class AddBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JTextField idField = new JTextField();
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField yearField = new JTextField();

            Object[] inputFields = {
                "ID:", idField,
                "Title:", titleField,
                "Author:", authorField,
                "Year:", yearField
            };

            int option = JOptionPane.showConfirmDialog(frame, inputFields, "Add Book", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String id = idField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                String year = yearField.getText();

                if (!id.isEmpty() && !title.isEmpty() && !author.isEmpty() && !year.isEmpty()) {
                    tableModel.addRow(new Object[]{id, title, author, year});
                } else {
                    JOptionPane.showMessageDialog(frame, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Listener for Edit Book
    class EditBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                JTextField idField = new JTextField(tableModel.getValueAt(selectedRow, 0).toString());
                JTextField titleField = new JTextField(tableModel.getValueAt(selectedRow, 1).toString());
                JTextField authorField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());
                JTextField yearField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());

                Object[] inputFields = {
                    "ID:", idField,
                    "Title:", titleField,
                    "Author:", authorField,
                    "Year:", yearField
                };

                int option = JOptionPane.showConfirmDialog(frame, inputFields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    tableModel.setValueAt(idField.getText(), selectedRow, 0);
                    tableModel.setValueAt(titleField.getText(), selectedRow, 1);
                    tableModel.setValueAt(authorField.getText(), selectedRow, 2);
                    tableModel.setValueAt(yearField.getText(), selectedRow, 3);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a row to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener for Delete Book
    class DeleteBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this book?", "Delete Book", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a row to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookManager::new);
    }
}
