import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Dodajemy Material Design dla lepszego wyglądu
import com.formdev.flatlaf.FlatDarkLaf;

public class NDNodeApp {

    private JFrame mainFrame;
    private JFrame loadingFrame;
    private JTextArea notesTextArea;
    private JList<String> notesList;
    private DefaultListModel<String> notesListModel;
    private List<String> savedNotes;
    private Timer autoSaveTimer;

    public static void main(String[] args) {
        // Ustawiamy FlatLaf Dark Theme dla estetycznego wyglądu
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        new NDNodeApp().start();
    }

    public void start() {
        // Wyświetlenie okna ładowania
        showLoadingScreen();

        // Symulacja ładowania (2 sekundy)
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(this::showMainScreen);
        }).start();
    }

    private void showLoadingScreen() {
        loadingFrame = new JFrame("ND node");
        loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadingFrame.setSize(400, 200);
        loadingFrame.setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("<html><span style='color:red; font-size:24px;'>ND</span><span style='color:red; font-size:18px;'> node</span></html>");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        titlePanel.add(titleLabel);

        JPanel loadingPanel = new JPanel();
        loadingPanel.setLayout(new BorderLayout());
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(50);
        progressBar.setString("Launching...");
        progressBar.setStringPainted(true);
        loadingPanel.add(progressBar, BorderLayout.CENTER);

        loadingFrame.add(titlePanel, BorderLayout.NORTH);
        loadingFrame.add(loadingPanel, BorderLayout.CENTER);
        loadingFrame.setVisible(true);
        loadingFrame.setLocationRelativeTo(null);
    }

    private void showMainScreen() {
        loadingFrame.dispose();

        mainFrame = new JFrame("ND node");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLayout(new BorderLayout());

        // Menu główne
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu setupMenu = new JMenu("Setup");
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenu pubMenu = new JMenu("Pub");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(setupMenu);
        menuBar.add(preferencesMenu);
        menuBar.add(pubMenu);

        mainFrame.setJMenuBar(menuBar);

        // Panel na widgety i przyciski bazy danych
        JPanel widgetsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton dbButton1 = new JButton("Database 1");
        JButton dbButton2 = new JButton("Database 2");
        JButton dbButton3 = new JButton("Database 3");

        widgetsPanel.add(dbButton1);
        widgetsPanel.add(dbButton2);
        widgetsPanel.add(dbButton3);

        // Przyciski New Board, New Chat, New Node
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3));
        JButton newBoardButton = new JButton("New Board +");
        JButton newChatButton = new JButton("New Chat +");
        JButton newNodeButton = new JButton("New Node +");

        buttonsPanel.add(newBoardButton);
        buttonsPanel.add(newChatButton);
        buttonsPanel.add(newNodeButton);

        // Spis notatek po lewej stronie
        notesListModel = new DefaultListModel<>();
        notesList = new JList<>(notesListModel);
        JScrollPane notesScrollPane = new JScrollPane(notesList);
        notesScrollPane.setPreferredSize(new Dimension(200, 0));

        // Pole tekstowe na notatki
        notesTextArea = new JTextArea();
        JScrollPane textScrollPane = new JScrollPane(notesTextArea);

        // Dolny panel z wersją programu i automatycznym zapisem
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel versionLabel = new JLabel("Version 1.0 | Auto-saving every 1 minute", SwingConstants.CENTER);
        statusPanel.add(versionLabel, BorderLayout.CENTER);

        // Automatyczne zapisywanie notatek co minutę
        savedNotes = loadSavedNotes();
        updateNotesList(savedNotes);

        autoSaveTimer = new Timer(60000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveNotes();
            }
        });
        autoSaveTimer.start();

        //Dodanie wszystkich elementów do ramki głównej
        mainFrame.add(notesScrollPane, BorderLayout.WEST);
        mainFrame.add(textScrollPane, BorderLayout.CENTER);
        mainFrame.add(widgetsPanel, BorderLayout.NORTH);
        mainFrame.add(buttonsPanel, BorderLayout.SOUTH);
        mainFrame.add(statusPanel, BorderLayout.PAGE_END);

        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
    }

    private void saveNotes() {
        String currentNote = notesTextArea.getText();
        if (!currentNote.isEmpty()) {
            savedNotes.add(currentNote);
            updateNotesList(savedNotes);
            saveNotesToFile(savedNotes);
        }
    }

    private void updateNotesList(List<String> notes) {
        notesListModel.clear();
        for (String note : notes) {
            notesListModel.addElement(note);
        }
    }

    private List<String> loadSavedNotes() {
        List<String> notes = new ArrayList<>();
        try {
            notes = Files.readAllLines(Paths.get("notes.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return notes;
    }

    private void saveNotesToFile(List<String> notes) {
        try (FileWriter writer = new FileWriter("notes.txt")) {
            for (String note : notes) {
                writer.write(note + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
