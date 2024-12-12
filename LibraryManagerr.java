import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.*;
import java.util.List;

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
        
        // Sample books for testing
        // books.add(new Book("Tes1", "Tes", "2005", Arrays.asList("Adventure", "Comedy"), "blulock.jpg"));
        // books.add(new Book("Tes2", "Tes", "2005", Arrays.asList("Adventure", "Romance"), "aot.jpg"));
        // books.add(new Book("Tes3", "Tes", "2005", Arrays.asList("Sci-Fi"), "download.jpg"));

        // Reading books from CSV file
        String filePath = "animek.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length == 6) {  // Menyesuaikan agar sesuai dengan 6 kolom
                    List<String> genres = Arrays.asList(data[3].split(","));
                    books.add(new Book(data[0], data[1], data[2], genres, data[4], data[5]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel homePanel = createHomePanel();
        tabbedPane.addTab("Home", homePanel);

        JPanel genrePanel = createGenrePanel();
        tabbedPane.addTab("Sort by Genre", genrePanel);

        JPanel allBooksPanel = createAllBooksPanel();
        tabbedPane.addTab("All Books", allBooksPanel);
    
        frame.add(tabbedPane);
        frame.setVisible(true);
    }


private JPanel createAllBooksPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    DefaultListModel<String> bookListModel = new DefaultListModel<>();
    JList<String> bookList = new JList<>(bookListModel);
    bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // Mengisi daftar buku
    updateBookListModel(bookListModel);

    JScrollPane scrollPane = new JScrollPane(bookList);
    panel.add(scrollPane, BorderLayout.CENTER);

    JButton sortButton = new JButton("Sort A-Z");
    panel.add(sortButton, BorderLayout.SOUTH);

    sortButton.addActionListener(e -> {
        books.sort(Comparator.comparing(Book::getTitle));
        updateBookListModel(bookListModel);
    });

    bookList.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {  // Klik ganda untuk detail
                int index = bookList.getSelectedIndex();
                if (index >= 0) {
                    Book selectedBook = books.get(index);
                    showBookDetails(selectedBook);
                }
            }
        }
    });

    return panel;
}

private void updateBookListModel(DefaultListModel<String> bookListModel) {
    bookListModel.clear();
    for (Book book : books) {
        bookListModel.addElement(book.getTitle());
    }
}
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Search: ");
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");

        topPanel.add(searchLabel, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        updateGridPanel();

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

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

        searchButton.addActionListener(e -> {
            String query = searchField.getText().toLowerCase();
            gridPanel.removeAll();
            for (Book book : books) {
                if (book.getTitle().toLowerCase().contains(query) || book.getAuthor().toLowerCase().contains(query)) {
                    gridPanel.add(createBookPanel(book));
                }
            }
            gridPanel.revalidate();
            gridPanel.repaint();
        });

        return panel;
    }

    private JPanel createGenrePanel() {
        JPanel genrePanel = new JPanel(new BorderLayout());
        JPanel genreOptionsPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        String[] genres = {
            "Action",
            "Adventure",
            "Comedy",
            "Demons",
            "Drama",
            "Ecchi",
            "Fantasy",
            "Game",
            "Harem",
            "Historical",
            "Horror",
            "Josei",
            "Magic",
            "Martial Arts",
            "Mecha",
            "Military",
            "Music",
            "Mystery",
            "Psychological",
            "Parody",
            "Police",
            "Romance",
            "Samurai",
            "School",
            "Sci-Fi",
            "Seinen",
            "Shoujo",
            "Shoujo Ai",
            "Shounen",
            "Slice of Life",
            "Sports",
            "Space",
            "Super Power",
            "Supernatural",
            "Thriller",
            "Vampire"
        };
        
        JPanel genreBooksPanel = new JPanel(new GridLayout(0, 4, 10, 10));

        for (String genre : genres) {
            JLabel genreLabel = new JLabel(genre);
            genreLabel.setForeground(Color.BLUE);
            genreLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            genreLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    genreBooksPanel.removeAll();
                    for (Book book : books) {
                        if (book.getGenres().contains(genre)) {
                            genreBooksPanel.add(createBookPanel(book));
                        }
                    }
                    genreBooksPanel.revalidate();
                    genreBooksPanel.repaint();
                }
            });
            genreOptionsPanel.add(genreLabel);
        }

        genrePanel.add(genreOptionsPanel, BorderLayout.NORTH);
        genrePanel.add(new JScrollPane(genreBooksPanel), BorderLayout.CENTER);
        return genrePanel;
    }

    private void updateGridPanel() {
        gridPanel.removeAll();
        for (Book book : books) {
            gridPanel.add(createBookPanel(book));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createBookPanel(Book book) {
        JPanel bookPanel = new JPanel(new BorderLayout());
        JLabel coverLabel = new JLabel(createImageIcon(book.getImagePath()));
        bookPanel.add(coverLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.add(new JLabel(" "));
        infoPanel.add(new JLabel("Title: " + book.getTitle()));
        infoPanel.add(new JLabel("Author: " + book.getAuthor()));
        infoPanel.add(new JLabel("Year: " + book.getYear()));
        infoPanel.add(new JLabel("Genres: " + String.join(", ", book.getGenres())));

        bookPanel.add(infoPanel, BorderLayout.CENTER);
        bookPanel.setPreferredSize(new Dimension(200, 300));

        bookPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showBookDetails(book);
            }
        });
    
        return bookPanel;
    }

    // private void showBookDetails(Book book) {
    //     JPanel detailsPanel = new JPanel(new BorderLayout(20, 20));
    //     JLabel coverLabel = new JLabel(createImageIcon(book.getImagePath()));
    // coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
    // detailsPanel.add(coverLabel, BorderLayout.NORTH);
    //     JTextArea textArea = new JTextArea(
    //         "Title: " + book.getTitle() + "\n" +
    //         "Author: " + book.getAuthor() + "\n" +
    //         "Year: " + book.getYear() + "\n" +
    //         "Genres: " + String.join(", ", book.getGenres()) + "\n\n" +
    //         "Description: This is description example\n"
    //     );
    //     textArea.setLineWrap(true);
    //     textArea.setWrapStyleWord(true);
    //     textArea.setEditable(false);
    //     textArea.setPreferredSize(new Dimension(300, 200));
        
    //     detailsPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);

    //     JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Book Details", JOptionPane.INFORMATION_MESSAGE);
    // }
    
    private void showBookDetails(Book book) {
        new BookDetailsPage(book);
    }

    class BookDetailsPage extends JFrame {
        public BookDetailsPage(Book book) {
            setTitle("Book Details - " + book.getTitle());
            setSize(600, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout(10, 10));
    
            // Panel Gambar Cover
            JLabel coverLabel = new JLabel(createImageIcon(book.getImagePath()));
            coverLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(coverLabel, BorderLayout.NORTH);
            
            String description = book.getDescription();
            String formattedDescription = description.replace("\n", "<br>"); 
            // Panel Informasi Buku
            JEditorPane bookInfoPane = new JEditorPane("text/html", 
    "<html>" +
    "<b>Title:</b> " + book.getTitle() + "<br>" +
    "<b>Author:</b> " + book.getAuthor() + "<br>" +
    "<b>Year:</b> " + book.getYear() + "<br>" +
    "<b>Genres:</b> " + String.join(", ", book.getGenres()) + "<br><br>" +
    "<b>Description:</b><br>" + formattedDescription +
    "</html>"
);
bookInfoPane.setEditable(false);

            // bookInfoArea.setLineWrap(true);
            // bookInfoArea.setWrapStyleWord(true);
            // bookInfoArea.setEditable(false);
            // bookInfoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
            JScrollPane scrollPane = new JScrollPane(bookInfoPane);
            scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Add the JScrollPane to your frame
            add(scrollPane, BorderLayout.CENTER);
            
    
            // Tombol Tutup
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> dispose());
            add(closeButton, BorderLayout.SOUTH);
    
            setVisible(true);
        }
    }
    
    

    private ImageIcon createImageIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image image = icon.getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    public class Book {
        private String title, author, year, imagePath, description;
        private List<String> genres;
    
        public Book(String title, String author, String year, List<String> genres, String imagePath, String description) {
            this.title = title;
            this.author = author;
            this.year = year;
            this.genres = genres;
            this.imagePath = imagePath;
            this.description = description;
        }
    
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getYear() { return year; }
        public List<String> getGenres() { return genres; }
        public String getImagePath() { return imagePath; }
        public String getDescription() { return description; }
        public void setTitle(String title) {
            this.title = title;
        }
    
        public void setAuthor(String author) {
            this.author = author;
        }
    
        public void setYear(String year) {
            this.year = year;
        }
    
        public void setGenres(List<String> genres) {
            this.genres = genres;
        }
    
        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    
        public void setDescription(String description) {
            this.description = description;
        }
    }

    class AddBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField yearField = new JTextField();
            JTextField genreField = new JTextField();
            JTextField imagePathField = new JTextField();
            JTextArea description = new JTextArea(5, 20);
            description.setLineWrap(true);  // Agar teks terbungkus jika panjang
            description.setWrapStyleWord(true);  // Agar teks terbungkus di kata, bukan di tengah
            JScrollPane descriptionScroll = new JScrollPane(description); 

            Object[] inputFields = {
                "Title:", titleField, "Author:", authorField, "Year:", yearField, 
                "Genres (comma-separated):", genreField, "Image Path:", imagePathField, "Descripton:", description
            };

            if (JOptionPane.showConfirmDialog(frame, inputFields, "Add Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                List<String> genres = Arrays.asList(genreField.getText().split(","));
                books.add(new Book(titleField.getText(), authorField.getText(), yearField.getText(), genres, imagePathField.getText(), description.getText()));
                updateGridPanel();
            }
        }
    }
//     class AddBookListener implements ActionListener {
//     public void actionPerformed(ActionEvent e) {
//         JTextField titleField = new JTextField();
//         JTextField authorField = new JTextField();
//         JTextField yearField = new JTextField();
//         JTextField genreField = new JTextField();
//         JTextField imagePathField = new JTextField();

//         Object[] inputFields = {
//             "Title:", titleField, 
//             "Author:", authorField, 
//             "Year:", yearField, 
//             "Genres (comma-separated):", genreField, 
//             "Image Path:", imagePathField
//         };

//         if (JOptionPane.showConfirmDialog(frame, inputFields, "Add Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
//             // Validasi input tahun untuk memastikan itu adalah angka
//             String yearText = yearField.getText();
//             if (!yearText.matches("\\d{4}")) {
//                 JOptionPane.showMessageDialog(frame, "Year must be a 4-digit number.");
//                 return;
//             }

//             // Ambil data dari input
//             String title = titleField.getText().trim();
//             String author = authorField.getText().trim();
//             String imagePath = imagePathField.getText().trim();

//             // Validasi input wajib
//             if (title.isEmpty() || author.isEmpty()) {
//                 JOptionPane.showMessageDialog(frame, "Title and Author are required.");
//                 return;
//             }

//             // Memisahkan genre yang dipisahkan dengan koma
//             List<String> genres = Arrays.asList(genreField.getText().split(","));
//             genres.replaceAll(String::trim);  // Menghapus spasi berlebih di setiap genre

//             // Menambahkan buku ke daftar (misalnya, daftar buku adalah 'books')
//             Book newBook = new Book(title, author, yearText, genres, imagePath);
//             books.add(newBook); // Pastikan Anda sudah mendeklarasikan 'books' sebagai list buku.

//             updateGridPanel();  // Memperbarui tampilan setelah menambahkan buku
//         }
//     }
// }


    class EditBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String title = JOptionPane.showInputDialog(frame, "Enter the title of the book to edit:");
            if (title == null || title.isEmpty()) return;
            for (Book book : books) {
                if (book.getTitle().equalsIgnoreCase(title)) {
                    JTextField titleField = new JTextField(book.getTitle());
                    JTextField authorField = new JTextField(book.getAuthor());
                    JTextField yearField = new JTextField(book.getYear());
                    JTextField genreField = new JTextField(String.join(",", book.getGenres()));
                    JTextField imagePathField = new JTextField(book.getImagePath());
                    JTextArea description = new JTextArea(book.getDescription());
                    description.setPreferredSize(new Dimension(200, 100));

                    Object[] inputFields = {
                        "Title:", titleField, "Author:", authorField, "Year:", yearField,
                        "Genres (comma-separated):", genreField, "Image Path:", imagePathField, 
                        "Descriptoion:",  new JScrollPane(description)
                    };

                    if (JOptionPane.showConfirmDialog(frame, inputFields, "Edit Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                        book.genres = Arrays.asList(genreField.getText().split(","));
                        // books.set(books.indexOf(book), new Book(titleField.getText(), authorField.getText(), yearField.getText(), book.genres, imagePathField.getText()));
                        book.setTitle(titleField.getText());
                        book.setAuthor(authorField.getText());
                        book.setYear(yearField.getText());
                        book.setGenres(Arrays.asList(genreField.getText().split(",")));
                        book.setImagePath(imagePathField.getText());
                        book.setDescription(description.getText());

                        updateGridPanel();
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame, "Book not found!");
        }
    }

    class DeleteBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String title = JOptionPane.showInputDialog(frame, "Enter the title of the book to delete:");
            if (title == null || title.isEmpty()) return;
            books.removeIf(book -> book.getTitle().equalsIgnoreCase(title));
            updateGridPanel();
        }
    }

    public static void main(String[] args) {
        new LibraryManagerr();
    }
}