import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;

public class LibraryManagerr {
    JFrame frame;
    JTabbedPane tabbedPane;

    JPanel gridPanel;
    ArrayList<Book> books = new ArrayList<>();

    public LibraryManagerr() {
        frame = new JFrame("Library Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 500);

        tabbedPane = new JTabbedPane();

        
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "blulock.jpg"));
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "aot.jpg"));
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "download.jpg"));
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "blulock.jpg"));
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "aot.jpg"));
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "download.jpg"));
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "blulock.jpg"));
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "aot.jpg"));
        books.add(new Book("Tes", "Tes", "2005", "Adventure", "download.jpg"));
        
        // Tab 1: Home (CRUD for books)
        JPanel homePanel = createHomePanel();
        tabbedPane.addTab("Home", homePanel);

        // Tab 2: Sort by Genre (you can implement later)
        JPanel genrePanel = createGenrePanel();
        tabbedPane.addTab("Sort by Genre", genrePanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
    private JPanel createGenrePanel() {
        JPanel genrePanel = new JPanel(new BorderLayout());
    
        // Create a combo box for genre selection
        String[] genres = {"Fiction", "Dystopian", "Adventure", "Sci-Fi", "Romance"};
        JComboBox<String> genreComboBox = new JComboBox<>(genres);
    
        // Panel to show books based on selected genre
        JPanel genreBooksPanel = new JPanel();
        genreBooksPanel.setLayout(new GridLayout(0, 4, 10, 10));  // 4 books per row, 10px spacing
    
        // Action listener for genre selection
        genreComboBox.addActionListener(e -> {
            String selectedGenre = (String) genreComboBox.getSelectedItem();
            genreBooksPanel.removeAll();  // Clear the existing books
    
            // Loop over books and display only the ones with the selected genre
            for (Book book : books) {
                if (book.getGenre().equalsIgnoreCase(selectedGenre)) {
                    genreBooksPanel.add(createBookPanel(book));
                }
            }
    
            genreBooksPanel.revalidate();  // Refresh the grid
            genreBooksPanel.repaint();
        });
    
        genrePanel.add(genreComboBox, BorderLayout.NORTH);
        genrePanel.add(new JScrollPane(genreBooksPanel), BorderLayout.CENTER);
    
        return genrePanel;
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

        // Panel for books in grid layout
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(0, 4, 10, 10));  // 4 books per row, 10px spacing

        
        updateGridPanel();  // Initially load books into the grid

        // Scrollable panel for books
        JScrollPane scrollPane = new JScrollPane(gridPanel);
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

        // Search functionality
        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            gridPanel.removeAll();  // Clear the grid before searching

            // Loop over books and check if title or author contains the search query
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query)) {
                    gridPanel.add(createBookPanel(book));
                }
            }

            gridPanel.revalidate(); // Refresh the grid
            gridPanel.repaint();
        });

        return panel;
    }

    // Update the grid with all books
    private void updateGridPanel() {
        gridPanel.removeAll();  // Clear existing books
        for (Book book : books) {
            gridPanel.add(createBookPanel(book));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // Helper method to create a book panel for each book
    private JPanel createBookPanel(Book book) {
        JPanel bookPanel = new JPanel();
        bookPanel.setLayout(new BorderLayout());

        // Set up the cover image
        ImageIcon bookCover = createImageIcon(book.getImagePath());
        JLabel coverLabel = new JLabel(bookCover);
        bookPanel.add(coverLabel, BorderLayout.NORTH);

        // Add book information below the image
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        infoPanel.add(new JLabel("Title: " + book.getTitle()));
        infoPanel.add(new JLabel("Author: " + book.getAuthor()));
        infoPanel.add(new JLabel("Year: " + book.getYear()));
        infoPanel.add(new JLabel("Genre: " + book.getGenre()));

        bookPanel.add(infoPanel, BorderLayout.CENTER);

        // Make the panel clickable for editing or deleting
        bookPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        bookPanel.setPreferredSize(new Dimension(200, 300));  // Set preferred size for each book panel

        return bookPanel;
    }

    // Helper to create ImageIcon
    private ImageIcon createImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(200, 300, Image.SCALE_SMOOTH);  // Resize image
        return new ImageIcon(resizedImage);
    }

    // Book data class
    static class Book {
        private String title;
        private String author;
        private String year;
        private String genre;
        private String imagePath;

        public Book(String title, String author, String year, String genre, String imagePath) {
            this.title = title;
            this.author = author;
            this.year = year;
            this.genre = genre;
            this.imagePath = imagePath;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public String getYear() {
            return year;
        }

        public String getGenre() {
            return genre;
        }

        public String getImagePath() {
            return imagePath;
        }
    }

    // Listener for Add Book
    class AddBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField yearField = new JTextField();
            JTextField genreField = new JTextField();
            JTextField imagePathField = new JTextField();

            Object[] inputFields = {"Title:", titleField, "Author:", authorField, "Year:", yearField, "Genre:", genreField, "Image Path:", imagePathField};

            int option = JOptionPane.showConfirmDialog(frame, inputFields, "Add Book", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                Book newBook = new Book(titleField.getText(), authorField.getText(), yearField.getText(), genreField.getText(), imagePathField.getText());
                books.add(newBook);
                updateGridPanel();  // Refresh the grid display
            }
        }
    }
    // Listener for Edit Book
    class EditBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = gridPanel.getComponentZOrder((Component) e.getSource());
            if (selectedIndex >= 0) {
                Book selectedBook = books.get(selectedIndex);

                JTextField titleField = new JTextField(selectedBook.getTitle());
                JTextField authorField = new JTextField(selectedBook.getAuthor());
                JTextField yearField = new JTextField(selectedBook.getYear());
                JTextField genreField = new JTextField(selectedBook.getGenre());
                JTextField imagePathField = new JTextField(selectedBook.getImagePath());

                Object[] inputFields = {"Title:", titleField, "Author:", authorField, "Year:", yearField, "Genre:", genreField, "Image Path:", imagePathField};

                int option = JOptionPane.showConfirmDialog(frame, inputFields, "Edit Book", JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    selectedBook = new Book(titleField.getText(), authorField.getText(), yearField.getText(), genreField.getText(), imagePathField.getText());
                    books.set(selectedIndex, selectedBook);
                    updateGridPanel();  // Refresh the grid display
                }
            }
        }
    }

    // Listener for Delete Book
    class DeleteBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = gridPanel.getComponentZOrder((Component) e.getSource());
            if (selectedIndex >= 0) {
                books.remove(selectedIndex);
                updateGridPanel();  // Refresh the grid display
            }
        }
    }

    public static void main(String[] args) {
        // Start the application
        SwingUtilities.invokeLater(() -> new LibraryManagerr());
    }
}
