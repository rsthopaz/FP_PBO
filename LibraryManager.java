import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class LibraryManager {
    JFrame frame;
    JTabbedPane tabbedPane;

    JTable bookTable;
    DefaultTableModel tableModel;

    public LibraryManager() {
        frame = new JFrame("Library Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);

        tabbedPane = new JTabbedPane();

        // Tab 1: Home (CRUD for books)
        JPanel homePanel = createHomePanel();
        tabbedPane.addTab("Home", homePanel);

        // Tab 2: Sort by Genre
        JPanel genrePanel = createGenrePanel();
        tabbedPane.addTab("Sort by Genre", genrePanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Top Panel with Search Bar
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Search: ");
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        topPanel.add(searchLabel, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        // Table Model
        String[] columnNames = {"ID", "Cover", "Title", "Author", "Year", "Genre"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 1) ? Icon.class : Object.class; // Use Icon for the Cover column
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(250); // Set the row height to 50 (or any size you prefer)
bookTable.getColumnModel().getColumn(1).setPreferredWidth(60); // Set column width for the image column


        // Sample Data
        tableModel.addRow(new Object[]{"1", createImageIcon("download.jpg"), "To Kill a Mockingbird", "Harper Lee", "1960", "Fiction"});
        tableModel.addRow(new Object[]{"2", createImageIcon("sample2.jpg"), "1984", "George Orwell", "1949", "Dystopian"});
        tableModel.addRow(new Object[]{"3", createImageIcon("sample3.jpg"), "Moby Dick", "Herman Melville", "1851", "Adventure"});
        tableModel.addRow(new Object[]{"4", createImageIcon("sample4.jpg"), "The Great Gatsby", "F. Scott Fitzgerald", "1925", "Fiction"});

        // Add Table to ScrollPane
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

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

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Search Functionality
        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            DefaultTableModel searchModel = new DefaultTableModel(columnNames, 0) {
                @Override
                public Class<?> getColumnClass(int column) {
                    return (column == 1) ? Icon.class : Object.class;
                }
            };

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String title = tableModel.getValueAt(i, 2).toString().toLowerCase();
                String author = tableModel.getValueAt(i, 3).toString().toLowerCase();

                if (title.contains(query) || author.contains(query)) {
                    searchModel.addRow(new Object[]{
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        tableModel.getValueAt(i, 3),
                        tableModel.getValueAt(i, 4),
                        tableModel.getValueAt(i, 5)
                    });
                }
            }

            bookTable.setModel(searchModel);

            // Reset table when search field is empty
            searchField.addActionListener(evt -> {
                if (searchField.getText().isEmpty()) {
                    bookTable.setModel(tableModel);
                }
            });
        });

        return panel;
    }

    private JPanel createGenrePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Dropdown for Genre
        JComboBox<String> genreDropdown = new JComboBox<>(new String[]{"All", "Fiction", "Dystopian", "Adventure"});
        panel.add(genreDropdown, BorderLayout.NORTH);

        // Table for Sorted Data
        DefaultTableModel genreTableModel = new DefaultTableModel(new String[]{"ID", "Cover", "Title", "Author", "Year", "Genre"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 1) ? Icon.class : Object.class;
            }
        };
        JTable genreTable = new JTable(genreTableModel);
        JScrollPane scrollPane = new JScrollPane(genreTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action for Genre Selection
        genreDropdown.addActionListener(e -> {
            String selectedGenre = genreDropdown.getSelectedItem().toString();
            genreTableModel.setRowCount(0); // Clear the table

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String genre = tableModel.getValueAt(i, 5).toString();
                if (selectedGenre.equals("All") || genre.equalsIgnoreCase(selectedGenre)) {
                    genreTableModel.addRow(new Object[]{
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        tableModel.getValueAt(i, 3),
                        tableModel.getValueAt(i, 4),
                        tableModel.getValueAt(i, 5)
                    });
                }
            }
        });

        return panel;
    }

    // Helper to create ImageIcon
    private ImageIcon createImageIcon(String path) {
        return new ImageIcon(path); 
    }

    // Listener for Add Book
    class AddBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JTextField idField = new JTextField();
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField yearField = new JTextField();
            JTextField genreField = new JTextField();

            Object[] inputFields = {"ID:", idField, "Title:", titleField, "Author:", authorField, "Year:", yearField, "Genre:", genreField};

            int option = JOptionPane.showConfirmDialog(frame, inputFields, "Add Book", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                tableModel.addRow(new Object[]{idField.getText(), null, titleField.getText(), authorField.getText(), yearField.getText(), genreField.getText()});
            }
        }
    }

    // Listener for Edit Book
    class EditBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow >= 0) {
                JTextField idField = new JTextField(tableModel.getValueAt(selectedRow, 0).toString());
                JTextField titleField = new JTextField(tableModel.getValueAt(selectedRow, 2).toString());
                JTextField authorField = new JTextField(tableModel.getValueAt(selectedRow, 3).toString());
                JTextField yearField = new JTextField(tableModel.getValueAt(selectedRow, 4).toString());
                JTextField genreField = new JTextField(tableModel.getValueAt(selectedRow, 5).toString());

                Object[] inputFields = {"ID:", idField, "Title:", titleField, "Author:", authorField, "Year:", yearField, "Genre:", genreField};

                int option = JOptionPane.showConfirmDialog(frame, inputFields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    tableModel.setValueAt(idField.getText(), selectedRow, 0);
                    tableModel.setValueAt(titleField.getText(), selectedRow, 2);
                    tableModel.setValueAt(authorField.getText(), selectedRow, 3);
                    tableModel.setValueAt(yearField.getText(), selectedRow, 4);
                    tableModel.setValueAt(genreField.getText(), selectedRow, 5);
                }
            }
        }
    }

    // Listener for Delete Book
        // Listener for Delete Book
        class DeleteBookListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = bookTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Confirm before deletion
                    int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this book?", 
                                                               "Delete Book", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a book to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        
        public static void main(String[] args) {
            // Start the application
            SwingUtilities.invokeLater(() -> new LibraryManager());
        }
    }
    