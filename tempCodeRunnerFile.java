 // class AddBookListener implements ActionListener {
    //     public void actionPerformed(ActionEvent e) {
    //         JTextField titleField = new JTextField();
    //         JTextField authorField = new JTextField();
    //         JTextField yearField = new JTextField();
    //         JTextField genreField = new JTextField();
    //         JTextField imagePathField = new JTextField();

    //         Object[] inputFields = {
    //             "Title:", titleField, "Author:", authorField, "Year:", yearField, 
    //             "Genres (comma-separated):", genreField, "Image Path:", imagePathField
    //         };

    //         if (JOptionPane.showConfirmDialog(frame, inputFields, "Add Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
    //             List<String> genres = Arrays.asList(genreField.getText().split(","));
    //             // books.add(new Book(titleField.getText(), authorField.getText(), yearField.getText(), genres, imagePathField.getText()));
    //             updateGridPanel();
    //         }
    //     }
    // }