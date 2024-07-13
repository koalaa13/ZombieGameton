package org.example.visual;


import org.example.attack.Utils;
import org.example.model.MyBaseBlock;
import org.example.model.Point;
import org.example.model.Spot;
import org.example.model.Zombie;
import org.example.model.response.UnitsResponse;
import org.example.model.response.ZpotsResponse;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Visualizer extends JFrame {
    private enum Obj {
        EMPTY(new Color(220, 220, 220)),
        BLOCK(new Color(0, 0, 250)),
        FUTURE_BLOCK(new Color(200, 200, 250)),
        BASE(new Color(150, 50, 150)),
        FUTURE_BASE(new Color(250, 150, 250)),
        ENEMY_BLOCK(new Color(250, 0, 0)),
        ENEMY_BASE(new Color(150,50, 50)),
        WALL(new Color(0, 0, 0)),
        ZPOT(new Color(0, 100, 0));

        public final Color c;

        Obj(Color c) {
            this.c = c;
        }
    }

    private final int W = 1200;
    private final int W2 = 350;
    private final int H = 1000;
    private int observeSpace = 10;
    private int cellS;

    private UnitsResponse game;
    private final ZpotsResponse world;

    private JPanel canvas;
    private JLabel status;

    private Obj[][] field = null;

    private int shiftX;
    private int shiftY;
    private boolean freeze = false;
    private boolean limitGold = false;

    public Visualizer(ZpotsResponse world) {
        super("canvas");

        this.world = world;

        setSize(W + W2, H);

        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent e) {
                if (freeze) return;
                int x = e.getX();
                int y = e.getY() - getInsets().top;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    buildBlock(x, y);
                    repaint();
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    moveBase(x, y);
                    repaint();
                }
            }
        });

        canvas = new JPanel() {
            public void paint(Graphics g) {
                paintImpl(g);
            }
        };

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel legend = new JPanel(new GridLayout(10, 1));
        legend.add(makeJLabel("Empty", Obj.EMPTY.c));
        legend.add(makeJLabel("Block", Obj.BLOCK.c));
        legend.add(makeJLabel("New block", Obj.FUTURE_BLOCK.c));
        legend.add(makeJLabel("Base", Obj.BASE.c));
        legend.add(makeJLabel("New base", Obj.FUTURE_BASE.c));
        legend.add(makeJLabel("Enemy block", Obj.ENEMY_BLOCK.c));
        legend.add(makeJLabel("Enemy base", Obj.ENEMY_BASE.c));
        legend.add(makeJLabel("Wall", Obj.WALL.c));
        legend.add(makeJLabel("Zpot", Obj.ZPOT.c));
        legend.add(makeJLabel("Zombie", Color.ORANGE));
        legend.setMaximumSize(new Dimension(W2, H / 3));
        legend.setBorder(BorderFactory.createEtchedBorder());
        legend.setBackground(Color.WHITE);
        sidePanel.add(legend);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));

        status = new JLabel();
        sidePanel.add(status);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JSlider slider = new JSlider(5, 26, observeSpace);
        slider.addChangeListener(e -> setObserveSpace(slider.getValue()));
        slider.setMajorTickSpacing(3);
        slider.setMinorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        sidePanel.add(slider);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JCheckBox cb = new JCheckBox("Limit gold (5)");
        cb.addItemListener(e -> setLimitGold(cb.isSelected()));
        sidePanel.add(cb);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton button = new JButton("Build random");
        button.addActionListener(e -> setRandomFutureBlocks());
        sidePanel.add(button);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton button2 = new JButton("Build far away");
        button2.addActionListener(e -> setFarFutureBlocks(1));
        sidePanel.add(button2);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton buttonNear = new JButton("Build near");
        buttonNear.addActionListener(e -> setFarFutureBlocks(-1));
        sidePanel.add(buttonNear);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton buttonLeft = new JButton("Build left");
        buttonLeft.addActionListener(e -> setDirFutureBlocks(-1, 0));
        sidePanel.add(buttonLeft);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton buttonRight = new JButton("Build right");
        buttonRight.addActionListener(e -> setDirFutureBlocks(1, 0));
        sidePanel.add(buttonRight);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton buttonUp = new JButton("Build up");
        buttonUp.addActionListener(e -> setDirFutureBlocks(0, -1));
        sidePanel.add(buttonUp);

        sidePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        JButton buttonDown = new JButton("Build down");
        buttonDown.addActionListener(e -> setDirFutureBlocks(0, 1));
        sidePanel.add(buttonDown);

        canvas.setMaximumSize(new Dimension(W, H));
        sidePanel.setMaximumSize(new Dimension(W2, H));

        setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
        add(canvas);
        add(sidePanel);
        show();
    }

    private JLabel makeJLabel(String text, Color c) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(c);
        return lbl;
    }

    private void tryFillField(int x, int y, Obj cell) {
        int realX = x - shiftX;
        int realY = y - shiftY;
        if (checkPoint(realX, realY)) {
            field[realX][realY] = cell;
        }
    }

    private synchronized void initField() {
        int minX = game.base.stream().map(b -> b.x).min(Comparator.naturalOrder()).get().intValue() - observeSpace;
        int minY = game.base.stream().map(b -> b.y).min(Comparator.naturalOrder()).get().intValue() - observeSpace;
        int maxX = game.base.stream().map(b -> b.x).max(Comparator.naturalOrder()).get().intValue() + observeSpace;
        int maxY = game.base.stream().map(b -> b.y).max(Comparator.naturalOrder()).get().intValue() + observeSpace;
        shiftX = minX;
        shiftY = minY;
        int sizeX = maxX - minX + 1;
        int sizeY = maxY - minY + 1;
        cellS = Math.min((W + sizeX - 1) / sizeX, (H + sizeY - 1) / sizeY);
        field = new Obj[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                field[i][j] = Obj.EMPTY;
            }
        }
        for (var zp : world.zpots) {
            tryFillField((int) zp.x, (int) zp.y, zp.type == Spot.Type.DEFAULT ? Obj.ZPOT : Obj.WALL);
        }
        for (var block : game.base) {
            Obj o = block.isHead() ? Obj.BASE : Obj.BLOCK;
            tryFillField((int) block.x, (int) block.y, o);
        }
        if (game.enemyBlocks != null) {
            for (var block : game.enemyBlocks) {
                Obj o = block.isHead() ? Obj.ENEMY_BASE : Obj.ENEMY_BLOCK;
                tryFillField((int) block.x, (int) block.y, o);
            }
        }
    }

    private synchronized void paintImpl(Graphics g) {
        if (game == null) {
            System.err.println("Cannot draw. No game info");
            return;
        }
        MyBaseBlock base = game.base.stream().filter(b -> b.isHead).findFirst().get();
        status.setText("Turn=" + game.turn + " Gold=" + game.player.gold + " HP=" + base.health + (freeze ? " F" : ""));
        int rows = field.length;
        int cols = field[0].length;
        g.setColor(new Color(50, 50, 50));
        for (int i = 0; i < rows + 1; i++) {
            g.drawLine(i * cellS, 0, i * cellS, cols * cellS);
        }
        for (int i = 0; i < cols + 1; i++) {
            g.drawLine(0, i * cellS, rows * cellS, i * cellS);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.setColor(field[i][j].c);
                g.fillRect(i * cellS + 1, j * cellS + 1, cellS - 2, cellS - 2);
            }
        }
        for (var z : game.zombies) {
            int realX = (int) z.x - shiftX;
            int realY = (int) z.y - shiftY;
            int centerX = realX * cellS + 1 + cellS / 2;
            int centerY = realY * cellS + 1 + cellS / 2;
            g.setColor(Color.BLACK);
            g.drawLine(centerX, centerY,
                    centerX + z.direction.deltaX() * (cellS / 2), centerY + z.direction.deltaY() * (cellS / 2));
            g.setColor(Color.ORANGE);
            if (cellS > 10) {
                g.fillRect(centerX - 5, centerY - 5, 10, 10);
            }
            if (cellS > 10 && z.type == Zombie.Type.juggernaut) {
                g.setColor(Color.MAGENTA);
                g.fillRect(centerX - 3, centerY - 3, 6, 6);
            }
        }
    }

    private boolean checkPoint(int x, int y) {
        return x >= 0 && y >= 0 && x < field.length && y < field[0].length;
    }

    private int getSpentGold() {
        int spentGold = 0;
        for (Obj[] objs : field) {
            for (Obj obj : objs) {
                if (obj == Obj.FUTURE_BLOCK) {
                    spentGold++;
                }
            }
        }
        return spentGold;
    }

    private boolean checkBuild(int realX, int realY, boolean checkGold) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!checkPoint(realX + i, realY + j)) {
                    return false;
                }
                if (field[realX + i][realY + j] == Obj.ENEMY_BASE || field[realX + i][realY + j] == Obj.ENEMY_BLOCK) {
                    return false;
                }
            }
        }
        for (int i = -1; i <= 1; i++) {
            if (field[realX + i][realY] == Obj.ZPOT || field[realX + i][realY] == Obj.WALL) return false;
            if (field[realX][realY + i] == Obj.ZPOT || field[realX][realY + i] == Obj.WALL) return false;
        }
        boolean good = false;
        for (int i = -1; i <= 1; i++) {
            if (field[realX + i][realY] == Obj.BASE || field[realX + i][realY] == Obj.BLOCK ||
                    field[realX + i][realY] == Obj.FUTURE_BASE) good = true;
            if (field[realX][realY + i] == Obj.BASE || field[realX][realY + i] == Obj.BLOCK ||
                    field[realX][realY + i] == Obj.FUTURE_BASE) good = true;
        }
        if (checkGold) {
            good &= getSpentGold() < game.player.gold;
        }
        return good;
    }

    private void buildBlock(int x, int y) {
        int realX = x / cellS;
        int realY = y / cellS;
        if (!checkPoint(realX, realY)) {
            return;
        }
        if (field[realX][realY] == Obj.EMPTY) {
            if (checkBuild(realX, realY, true)) {
                field[realX][realY] = Obj.FUTURE_BLOCK;
            } else {
                System.err.println("Check buildBlock failed");
            }
        } else {
            System.err.println("Invalid buildBlock request");
        }
    }

    private boolean checkNoMoves() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == Obj.FUTURE_BASE) {
                    return false;
                }
            }
        }
        return true;
    }

    private void moveBase(int x, int y) {
        int realX = x / cellS;
        int realY = y / cellS;
        if (!checkPoint(realX, realY)) {
            return;
        }
        if (field[realX][realY] == Obj.BLOCK) {
            if (checkNoMoves()) {
                field[realX][realY] = Obj.FUTURE_BASE;
            } else {
                System.err.println("Check moveBase failed");
            }
        } else {
            System.err.println("Invalid moveBase request");
        }
    }

    public synchronized void setGame(UnitsResponse game) {
        this.game = game;
        initField();
        repaint();
    }

    public synchronized void setFreeze(boolean freeze) {
        this.freeze = freeze;
        repaint();
    }

    private synchronized void setObserveSpace(int observeSpace) {
        this.observeSpace = observeSpace;
        repaint();
    }

    private synchronized void setFutureBlocks(BiConsumer<Point, List<Point>> consumer) {
        int remainGold = (int) game.player.gold - getSpentGold();
        if (remainGold == 0) {
            System.err.println("No gold left");
            return;
        }
        List<Point> blocks = new ArrayList<>();
        Point basePoint = null;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == Obj.EMPTY && checkBuild(i, j, false)) {
                    blocks.add(new Point(i, j));
                }
                if (field[i][j] == Obj.BASE) {
                    basePoint = new Point(i, j);
                }
            }
        }
        if (basePoint == null) {
            System.err.println("No base found");
            return;
        }
        consumer.accept(basePoint, blocks);
        int lim = Math.min(remainGold, blocks.size());
        if (limitGold) {
            lim = Math.min(lim, 5);
        }
        for (int i = 0; i < lim; i++) {
            field[(int) blocks.get(i).x][(int) blocks.get(i).y] = Obj.FUTURE_BLOCK;
        }
        repaint();
    }

    private void setRandomFutureBlocks() {
        setFutureBlocks((p, blocks) -> Collections.shuffle(blocks));
    }

    private void setFarFutureBlocks(int mp) {
        setFutureBlocks((p, blocks) ->
                blocks.sort((o1, o2) -> (int) (mp * (Utils.dist(o2, p) - Utils.dist(o1, p)))));
    }

    private void setDirFutureBlocks(int dX, int dY) {
        setFutureBlocks((p, blocks) ->
                blocks.sort((o1, o2) -> (int) (dX * (o2.x - o1.x) + dY * (o2.y - o1.y))));
    }
    private synchronized void setLimitGold(boolean value) {
        this.limitGold = value;
    }

    public Point getFutureBase() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == Obj.FUTURE_BASE) {
                    return new Point(i + shiftX, j + shiftY);
                }
            }
        }
        return null;
    }

    public List<Point> getFutureBlocks() {
        List<Point> blocks = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == Obj.FUTURE_BLOCK) {
                    blocks.add(new Point(i + shiftX, j + shiftY));
                }
            }
        }
        return blocks;
    }
}
