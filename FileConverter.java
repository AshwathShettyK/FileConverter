import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.events.MouseEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class FileConverter extends JFrame {
    // UI Components
    JTabbedPane tabbedPane;
    JTextField outputFolderField; // Only output folder field remains

    // Components for Image
    JTextField imagePathField;
    JComboBox<String> imageFormatBox;

    // Components for Audio
    JTextField audioPathField;
    JComboBox<String> audioFormatBox;

    // Components for Video
    JTextField videoPathField;
    JComboBox<String> videoFormatBox;

    // Components for Document
    JTextField docPathField;
    JComboBox<String> docFormatBox;

    String defaultOutputFolder = "C:\\Users\\Ashwath Shetty K\\Documents\\Converted Documents";
    long maxFileSizeImageAudioVideo = 50 * 1024 * 1024; // 50MB for images, audio, and video
    long maxFileSizeDocument = 100 * 1024 * 1024; // 100MB for documents

    public FileConverter() {
        setTitle("File Converter");
        setSize(680, 400); // Increased layout size to ensure all components are visible
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Dark theme background
        getContentPane().setBackground(Color.DARK_GRAY);

        // Main Title
        JLabel mainTitle = new JLabel("File Converter", SwingConstants.CENTER);
        mainTitle.setFont(new Font("Arial", Font.BOLD, 24));
        mainTitle.setForeground(Color.CYAN);
        add(mainTitle, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.DARK_GRAY);
        tabbedPane.setForeground(Color.WHITE); // Set tab text color to white
        tabbedPane.setFocusable(false);

        // Add a custom UI to change the selected tab's text color to black
        UIManager.put("TabbedPane.selected", Color.BLACK);
        tabbedPane.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI() {
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // Do nothing to remove the border
            }
        });

        initImageTab();
        initAudioTab();
        initVideoTab();
        initDocumentTab();

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // Utility methods for styled components
    private JLabel createStyledLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.CYAN);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBounds(x, y, 200, 25);
        return label;
    }

    private JTextField createStyledTextField(String text, int x, int y, int w, int h) {
        JTextField field = new JTextField(text);
        field.setBounds(x, y, w, h);
        field.setBackground(Color.BLACK);
        field.setForeground(Color.CYAN);
        field.setCaretColor(Color.CYAN);
        field.setBorder(new LineBorder(Color.CYAN));
        return field;
    }

    private JButton createStyledButton(String text, int x, int y, int w, int h) {
        JButton button = new JButton(text);
        button.setBounds(x, y, w, h);
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.CYAN);
        button.setBorder(new LineBorder(Color.CYAN));
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(Color.CYAN);
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(Color.DARK_GRAY);
                button.setForeground(Color.CYAN);
            }
        });
        return button;
    }

    // -------- IMAGE TAB ---------
    private void initImageTab() {
        JPanel panel = createTabPanel();

        // Title (Centered)
        JLabel titleLabel = new JLabel("Image Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBounds(27, 10, 600, 30); // Centered
        panel.add(titleLabel);

        // File size restriction message (Centered)
        JLabel sizeLabel = new JLabel("Maximum file size: 50MB", SwingConstants.CENTER);
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sizeLabel.setForeground(Color.CYAN);
        sizeLabel.setBounds(28, 40, 600, 20); // Centered
        panel.add(sizeLabel);

        // Select File components in a single line
        JLabel label = createStyledLabel("Select Image File:", 50, 70);
        panel.add(label);

        imagePathField = createStyledTextField("", 200, 70, 300, 25);
        imagePathField.setEditable(false); // Restrict user input
        panel.add(imagePathField);

        JButton chooseBtn = createStyledButton("Choose File", 520, 70, 120, 25);
        panel.add(chooseBtn);

        JLabel formatLabel = createStyledLabel("Choose output format:", 50, 120);
        panel.add(formatLabel);

        String[] imageFormats = {"jpg", "png", "tiff", "webp", "bmp"};
        imageFormatBox = new JComboBox<>(imageFormats);
        styleComboBox(imageFormatBox, 250, 120, 150); // Increased ComboBox length
        panel.add(imageFormatBox);

        // Output Folder
        JLabel folderLabel = createStyledLabel("Output Folder:", 50, 170);
        panel.add(folderLabel);

        outputFolderField = createStyledTextField(defaultOutputFolder, 180, 170, 340, 25); // Decreased length
        panel.add(outputFolderField);

        JButton browseBtn = createStyledButton("Browse", 535, 170, 90, 25);
        panel.add(browseBtn);

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                outputFolderField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JButton convertBtn = createStyledButton("Convert", 250, 220, 100, 25);
        panel.add(convertBtn);

        JButton closeBtn = createStyledButton("Close", 370, 220, 100, 25);
        closeBtn.addActionListener(e -> System.exit(0));
        panel.add(closeBtn);

        chooseBtn.addActionListener(e -> openFileChooser(imagePathField, "Image Files", imageFormats));

        convertBtn.addActionListener(e -> {
            String inputPath = imagePathField.getText();
            String format = (String) imageFormatBox.getSelectedItem();
            if (!inputPath.isEmpty()) {
                if (new File(inputPath).length() > maxFileSizeImageAudioVideo) {
                    JOptionPane.showMessageDialog(this, "File size exceeds the limit of 50MB!", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    convertFile(inputPath, format, "image");
                }
            } else {
                // Display a warning message if no file is selected
                JOptionPane.showMessageDialog(this, "Please select an image file!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        tabbedPane.addTab("Image", panel);
    }

    // -------- AUDIO TAB ---------
    private void initAudioTab() {
        JPanel panel = createTabPanel();

        // Title (Centered)
        JLabel titleLabel = new JLabel("Audio Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBounds(27, 10, 600, 30); // Centered
        panel.add(titleLabel);

        // File size restriction message (Centered)
        JLabel sizeLabel = new JLabel("Maximum file size: 50MB", SwingConstants.CENTER);
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sizeLabel.setForeground(Color.CYAN);
        sizeLabel.setBounds(28, 40, 600, 20); // Centered
        panel.add(sizeLabel);

        // Select File components in a single line
        JLabel label = createStyledLabel("Select Audio File:", 50, 70);
        panel.add(label);

        audioPathField = createStyledTextField("", 200, 70, 300, 25);
        audioPathField.setEditable(false); // Restrict user input
        panel.add(audioPathField);

        JButton chooseBtn = createStyledButton("Choose File", 520, 70, 120, 25);
        panel.add(chooseBtn);

        JLabel formatLabel = createStyledLabel("Choose output format:", 50, 120);
        panel.add(formatLabel);

        String[] audioFormats = {"mp3", "wav", "aac", "flac", "ogg"};
        audioFormatBox = new JComboBox<>(audioFormats);
        styleComboBox(audioFormatBox, 250, 120, 150); // Increased ComboBox length
        panel.add(audioFormatBox);

        // Output Folder
        JLabel folderLabel = createStyledLabel("Output Folder:", 50, 170);
        panel.add(folderLabel);

        outputFolderField = createStyledTextField(defaultOutputFolder, 180, 170, 340, 25); // Decreased length
        panel.add(outputFolderField);

        JButton browseBtn = createStyledButton("Browse", 535, 170, 90, 25);
        panel.add(browseBtn);

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                outputFolderField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JButton convertBtn = createStyledButton("Convert", 250, 220, 100, 25);
        panel.add(convertBtn);

        JButton closeBtn = createStyledButton("Close", 370, 220, 100, 25);
        closeBtn.addActionListener(e -> System.exit(0));
        panel.add(closeBtn);

        chooseBtn.addActionListener(e -> openFileChooser(audioPathField, "Audio Files", audioFormats));

        convertBtn.addActionListener(e -> {
            String inputPath = audioPathField.getText();
            String format = (String) audioFormatBox.getSelectedItem();
            if (!inputPath.isEmpty()) {
                if (new File(inputPath).length() > maxFileSizeImageAudioVideo) {
                    JOptionPane.showMessageDialog(this, "File size exceeds the limit of 50MB!", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    convertFile(inputPath, format, "audio");
                }
            } else {
                // Display a warning message if no file is selected
                JOptionPane.showMessageDialog(this, "Please select an audio file!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        tabbedPane.addTab("Audio", panel);
    }

    // -------- VIDEO TAB ---------
    private void initVideoTab() {
        JPanel panel = createTabPanel();

        // Title (Centered)
        JLabel titleLabel = new JLabel("Video Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBounds(27, 10, 600, 30); // Centered
        panel.add(titleLabel);

        // File size restriction message (Centered)
        JLabel sizeLabel = new JLabel("Maximum file size: 50MB", SwingConstants.CENTER);
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sizeLabel.setForeground(Color.CYAN);
        sizeLabel.setBounds(28, 40, 600, 20); // Centered
        panel.add(sizeLabel);

        // Select File components
        JLabel label = createStyledLabel("Select Video File:", 50, 70);
        panel.add(label);

        videoPathField = createStyledTextField("", 200, 70, 300, 25);
        videoPathField.setEditable(false); // Restrict user input
        panel.add(videoPathField);

        JButton chooseBtn = createStyledButton("Choose File", 520, 70, 120, 25);
        panel.add(chooseBtn);

        JLabel formatLabel = createStyledLabel("Choose output format:", 50, 120);
        panel.add(formatLabel);

        String[] videoFormats = {"mp4", "mov", "avi", "webm"};
        videoFormatBox = new JComboBox<>(videoFormats);
        styleComboBox(videoFormatBox, 250, 120, 150); // Increased ComboBox length
        panel.add(videoFormatBox);

        // Output Folder
        JLabel folderLabel = createStyledLabel("Output Folder:", 50, 170);
        panel.add(folderLabel);

        outputFolderField = createStyledTextField(defaultOutputFolder, 180, 170, 340, 25); // Decreased length
        panel.add(outputFolderField);

        JButton browseBtn = createStyledButton("Browse", 535, 170, 90, 25);
        panel.add(browseBtn);

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                outputFolderField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JButton convertBtn = createStyledButton("Convert", 250, 220, 100, 25);
        panel.add(convertBtn);

        JButton closeBtn = createStyledButton("Close", 370, 220, 100, 25);
        closeBtn.addActionListener(e -> System.exit(0));
        panel.add(closeBtn);

        chooseBtn.addActionListener(e -> openFileChooser(videoPathField, "Video Files", videoFormats));

        convertBtn.addActionListener(e -> {
            String inputPath = videoPathField.getText();
            String format = (String) videoFormatBox.getSelectedItem();
            if (!inputPath.isEmpty()) {
                if (new File(inputPath).length() > maxFileSizeImageAudioVideo) {
                    JOptionPane.showMessageDialog(this, "File size exceeds the limit of 50MB!", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    convertFile(inputPath, format, "video");
                }
            } else {
                // Display a warning message if no file is selected
                JOptionPane.showMessageDialog(this, "Please select an video file!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        tabbedPane.addTab("Video", panel);
    }

    // -------- DOCUMENT TAB ---------
    private void initDocumentTab() {
        JPanel panel = createTabPanel();

        // Title (Centered)
        JLabel titleLabel = new JLabel("Document Converter", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.CYAN);
        titleLabel.setBounds(27, 10, 600, 30); // Centered
        panel.add(titleLabel);

        // File size restriction message (Centered)
        JLabel sizeLabel = new JLabel("Maximum file size: 100MB", SwingConstants.CENTER);
        sizeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        sizeLabel.setForeground(Color.CYAN);
        sizeLabel.setBounds(28, 40, 600, 20); // Centered
        panel.add(sizeLabel);

        // Select File components in a single line
        JLabel label = createStyledLabel("Select Document File:", 50, 70);
        panel.add(label);

        docPathField = createStyledTextField("", 200, 70, 300, 25);
        docPathField.setEditable(false); // Restrict user input
        panel.add(docPathField);

        JButton chooseBtn = createStyledButton("Choose File", 520, 70, 120, 25);
        panel.add(chooseBtn);

        JLabel formatLabel = createStyledLabel("Choose output format:", 50, 120);
        panel.add(formatLabel);

        String[] docFormats = {"pdf", "docx", "odt"};
        docFormatBox = new JComboBox<>(docFormats);
        styleComboBox(docFormatBox, 250, 120, 150); // Increased ComboBox length
        panel.add(docFormatBox);

        // Output Folder
        JLabel folderLabel = createStyledLabel("Output Folder:", 50, 170);
        panel.add(folderLabel);

        outputFolderField = createStyledTextField(defaultOutputFolder, 180, 170, 340, 25); // Decreased length
        panel.add(outputFolderField);

        JButton browseBtn = createStyledButton("Browse", 535, 170, 90, 25);
        panel.add(browseBtn);

        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = chooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                outputFolderField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        });

        JButton convertBtn = createStyledButton("Convert", 250, 220, 100, 25);
        panel.add(convertBtn);

        JButton closeBtn = createStyledButton("Close", 370, 220, 100, 25);
        closeBtn.addActionListener(e -> System.exit(0));
        panel.add(closeBtn);

        chooseBtn.addActionListener(e -> openFileChooser(docPathField, "Documents", docFormats));

        convertBtn.addActionListener(e -> {
            String inputPath = docPathField.getText();
            String format = (String) docFormatBox.getSelectedItem();
            if (!inputPath.isEmpty()) {
                if (new File(inputPath).length() > maxFileSizeDocument) {
                    JOptionPane.showMessageDialog(this, "File size exceeds the limit of 100MB!", "Warning", JOptionPane.WARNING_MESSAGE);
                } else {
                    convertFile(inputPath, format, "document");
                }
            } else {
                // Display a warning message if no file is selected
                JOptionPane.showMessageDialog(this, "Please select a document file!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        tabbedPane.addTab("Document", panel);
    }

    // Shared methods
    private JPanel createTabPanel() {
        JPanel panel = new JPanel(null);
        panel.setBackground(Color.DARK_GRAY);
        return panel;
    }

    private void styleComboBox(JComboBox<String> box, int x, int y, int width) {
        box.setBounds(x, y, width, 25); // Increased ComboBox length
        box.setBackground(Color.BLACK);
        box.setForeground(Color.CYAN);
        box.setBorder(new LineBorder(Color.CYAN));
    }

    private void openFileChooser(JTextField pathField, String description, String[] extensions) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(description, extensions));
        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            pathField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }
private void convertFile(String inputPath, String format, String type) {
    try {
        String outputDir = outputFolderField.getText().isEmpty() ? defaultOutputFolder : outputFolderField.getText();
        new File(outputDir).mkdirs();

        // Use the input file's name with the new format
        String baseName = new File(inputPath).getName().replaceAll("\\..*$", "");
        String outputName = baseName + "_converted." + format;
        String outputPath = outputDir + File.separator + outputName;

        // Handle file existence by appending a number
        int counter = 1;
        while (new File(outputPath).exists()) {
            outputName = baseName + "_converted" + counter + "." + format;
            outputPath = outputDir + File.separator + outputName;
            counter++;
        }

        ProcessBuilder builder;
        if (type.equals("document")) {
            builder = new ProcessBuilder(
                "soffice", "--headless", "--convert-to", format,
                inputPath, "--outdir", outputDir
            );
        } else {
            builder = new ProcessBuilder(
                "ffmpeg", "-i", inputPath, outputPath
            );
        }

        builder.redirectErrorStream(true); // Combine stderr with stdout (optional)
        Process process = builder.start();
        process.waitFor();

        JOptionPane.showMessageDialog(this, "File converted successfully! File saved to: " + outputPath);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error during conversion: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileConverter::new);
    }
}