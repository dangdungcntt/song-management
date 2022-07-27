package ui;

import model.Song;
import repository.SongRepository;
import repository.SongRepositoryImpl;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class FrameSongManagement extends JFrame {

    JButton btnAdd, btnSearch, btnDelete;
    JTextField txtSearch;
    JTable tblResult;
    DefaultTableModel tableModel;
    JPanel panel;
    SongRepository songRepository;
    FrameAddSong frameAddSong;
    List<Song> showingSongs;

    public FrameSongManagement(String title) {
        super(title);
        this.setSize(500, 540);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.initComponents();
        this.initData();
        this.addEventListeners();

        this.setVisible(true);
        this.showingSongs = new ArrayList<>();
        this.onPressSearch(false);
    }

    private void initData() {
        songRepository = new SongRepositoryImpl();
    }

    private void addEventListeners() {
        btnSearch.addActionListener(e -> onPressSearch(false));
        btnAdd.addActionListener(e -> onPressAdd());
        btnDelete.addActionListener(e -> onPressDelete());
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onPressSearch(false);
                }
            }
        });
    }

    private void onPressDelete() {
        int selectedRow = tblResult.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        String selectedCode = tblResult.getValueAt(selectedRow, 0).toString();

        if (!songRepository.removeSong(selectedCode)) {
            JOptionPane.showMessageDialog(null, "Unknown error");
            return;
        }

        this.onPressSearch(true);
    }

    private void showMainFrame() {
        this.setVisible(true);
    }

    private void onPressAdd() {
        this.setVisible(false);
        frameAddSong = new FrameAddSong("Add new song");
        frameAddSong.btnAdd.addActionListener(e -> handleAddNewSong());
        frameAddSong.btnCancel.addActionListener(e -> {
            frameAddSong.dispose();
            showMainFrame();
        });
        frameAddSong.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                showMainFrame();
            }
        });

    }

    private void handleAddNewSong() {
        String code = frameAddSong.txtCode.getText().trim();
        String title = frameAddSong.txtTitle.getText().trim();
        String singer = frameAddSong.txtSinger.getText().trim();
        if (code.length() == 0 || title.length() == 0 || singer.length() == 0) {
            JOptionPane.showMessageDialog(null, "Missing info");
            return;
        }

        Song song = new Song();
        song.setCode(code);
        song.setTitle(title);
        song.setSinger(singer);
        if (songRepository.addSong(song)) {
            JOptionPane.showMessageDialog(null, "Success");
            this.setVisible(true);
            frameAddSong.dispose();
            this.onPressSearch(true);
            return;
        }
        JOptionPane.showMessageDialog(null, "Unknown error");
    }

    private void onPressSearch(boolean silent) {
        tableModel.setRowCount(0);
        String keyword = txtSearch.getText().trim();
        this.showingSongs = new ArrayList<>();
        if (keyword.length() == 0) {
            this.showingSongs.addAll(songRepository.getAll());
        } else {
            List<Song> results = songRepository.search(keyword);
            if (results.size() == 0 && !silent) {
                JOptionPane.showMessageDialog(
                        null,
                        "Not found any result for keyword \"" + keyword + "\"");
                return;
            }

            this.showingSongs.addAll(results);
        }

        for (Song song : this.showingSongs) {
            String[] row = {
                    song.getCode(),
                    song.getTitle(),
                    song.getSinger()
            };
            tableModel.addRow(row);
        }
    }

    private void initComponents() {
        panel = new JPanel();
        panel.setLayout(new FlowLayout());

        btnAdd = new JButton("Add");
        btnSearch = new JButton("Search");
        btnDelete = new JButton("Delete");
        txtSearch = new JTextField();
        tblResult = createTable();
        JScrollPane scrollPane = new JScrollPane(tblResult);

        btnAdd.setPreferredSize(new Dimension(70, 30));
        btnSearch.setPreferredSize(new Dimension(100, 30));
        txtSearch.setPreferredSize(new Dimension(350, 30));

        panel.add(txtSearch);
        panel.add(btnSearch);
        panel.add(btnAdd);
        panel.add(btnDelete);
        panel.add(scrollPane);

        this.setContentPane(panel);
    }

    private JTable createTable() {
        String[] columns = {"Code", "Title", "Singer"};
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columns);
        tableModel.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                onUpdatedRow(e.getFirstRow());
            }
        });
        JTable tb = new JTable();
        tb.getTableHeader().setReorderingAllowed(false);
        tb.setModel(tableModel);
        return tb;
    }

    private void onUpdatedRow(int row) {
        String[] item = new String[3];

        for (int i = 0; i < 3; i++) {
            item[i] = tableModel.getValueAt(row, i).toString();
        }

        Song newSongData = Song.fromRowData(item);

        for (int i = 0; i < this.showingSongs.size(); i++) {
            if (i != row) {
                continue;
            }

            if (!songRepository.updateSong(this.showingSongs.get(i).getCode(), newSongData)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Update error");
                return;
            }

            this.onPressSearch(true);
            return;
        }

        JOptionPane.showMessageDialog(
                null,
                "Not found editing song");
    }

    public static void main(String[] args) {
        new FrameSongManagement("Song Management");
    }
}
