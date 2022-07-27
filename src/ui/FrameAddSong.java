package ui;

import javax.swing.*;
import java.awt.*;

public class FrameAddSong extends JFrame {

    GridBagLayout gb;
    GridBagConstraints gbc;
    JTextField txtCode, txtTitle, txtSinger;
    JButton btnAdd, btnCancel;
    JPanel panel;
    public FrameAddSong(String title) {
        super(title);
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();

        this.setVisible(true);
    }

    private void initComponents() {
        gb = new GridBagLayout();
        gbc = new GridBagConstraints();
        panel = new JPanel();
        panel.setLayout(gb);

        btnAdd = new JButton("Add");
        btnCancel = new JButton("Cancel");
        txtCode = new JTextField();
        txtTitle = new JTextField();
        txtSinger = new JTextField();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridwidth = 2;
        addComponent(panel, new JLabel("Add new song"), 0, 0, 0, 20);
        gbc.gridwidth = 1;
        addComponent(panel, new JLabel("Code"), 0, 1, 10, 10);
        addComponent(panel, new JLabel("Title"), 0, 2, 10, 10);
        addComponent(panel, new JLabel("Singer"), 0, 3, 10, 10);
        addComponent(panel, txtCode, 1, 1, 200, 10);
        addComponent(panel, txtTitle, 1, 2, 200, 10);
        addComponent(panel, txtSinger, 1, 3, 200, 10);
        addComponent(panel, btnAdd, 0, 4, 10, 10);
        addComponent(panel, btnCancel, 1, 4, 10, 10);

        this.setContentPane(panel);
    }

    private void addComponent(Container ct, Component c, int col, int row, int ncol, int nrow) {
        gbc.gridx = col;
        gbc.gridy = row;

        gbc.ipadx = ncol;
        gbc.ipady = nrow;

        gb.setConstraints(c, gbc);

        ct.add(c);
    }
}
