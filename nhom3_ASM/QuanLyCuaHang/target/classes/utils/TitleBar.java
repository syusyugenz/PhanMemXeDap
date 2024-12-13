/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author bangp
 */
public class TitleBar extends JComponent{
    
     private JFrame frame;
    private JPanel panel;
    private int x;
    private int y;

    public TitleBar() {
        init();
    }

    public void initJFram(JFrame frame, JPanel panel) {
        this.frame = frame;

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (frame.getExtendedState() == JFrame.NORMAL && SwingUtilities.isLeftMouseButton(e)) {
                    x = e.getXOnScreen();
                    y = e.getYOnScreen();
                }
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - x;
                int deltaY = e.getYOnScreen() - y;
                int newX = frame.getLocation().x + deltaX;
                int newY = frame.getLocation().y + deltaY;
                frame.setLocation(newX, newY);
                x = e.getXOnScreen();
                y = e.getYOnScreen();
            }
        });
    }

    private void init() {
        setLayout(new MigLayout("insets 3, fill", "[fill][right]", "[fill]"));

        panel = new JPanel(new MigLayout("insets 0"));
        panel.setOpaque(false);
        add(panel, "cell 0 0, growx");

        panel.setLayout(new MigLayout("inset 3"));
        
        Item close = new Item();
        close.setBackground(new Color(235, 47, 47));
       
        panel.add(close);

        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
      
    }

    private class Item extends JButton {

        public Item() {
            init();
        }

        private void init() {
            setContentAreaFilled(false);
            setBorder(null);
            setPreferredSize(new Dimension(17, 17));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int with = getWidth();
            int height = getHeight();
            g2.setColor(getBackground());
            g2.fillOval(0, 0, with, height);
            g2.dispose();
        }
    }
}
