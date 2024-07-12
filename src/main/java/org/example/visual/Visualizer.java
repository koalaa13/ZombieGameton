package org.example.visual;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class Visualizer extends JFrame {
    private enum Obj {
        EMPTY(220, 220, 220),
        BLOCK(250, 150, 150),
        FUTURE_BLOCK(150, 50, 50),
        BASE(150, 150, 250),
        FUTURE_BASE(50, 50, 150),
        ENEMY_BLOCK(50, 0, 0),
        ENEMY_BASE(0, 0, 50),
        ZPOT(50, 150, 50);

        int r;
        int g;
        int b;

        Obj(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    private final int W = 1000;
    private final int W2 = 200;
    private final int H = 800;
    private int cellS;

    private Game game = null;
    private World world;

    private JPanel canvas;

    private Obj[][] field = null;

    private int shiftX;
    private int shiftY;

    public Visualizer(World world) {
        super("canvas");

        this.world = world;

        setSize(W + W2, H);

        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    buildBlock(e.getX(), e.getY());
                } else if (e.getButton() == MouseEvent.BUTTON2) {
                    moveBase(e.getX(), e.getY());
                }
                repaint();
            }

            public void mouseEntered(MouseEvent arg0) {
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mousePressed(MouseEvent arg0) {
            }

            public void mouseReleased(MouseEvent arg0) {
            }
        });

        canvas = new JPanel() {
            // paint the canvas
            public void paint(Graphics g) {
                paintImpl(g);
            }
        };

        add(canvas);
        show();
    }

    private void tryFillField(int x, int y, Obj cell) {
        int realX = x - shiftX;
        int realY = y - shiftY;
        if (realX < field.length && realY < field[0].length) {
            field[realX][realY] = cell;
        }
    }

    private void initField() {
        int minX = game.base.stream().map(getX).min() - 12;
        int minY = game.base.stream().map(getY).min() - 12;
        int maxX = game.base.stream().map(getX).max() + 12;
        int maxY = game.base.stream().map(getY).max() + 12;
        shiftX = minX;
        shiftY = minY;
        int sizeX = maxX - minX + 1;
        int sizeY = maxY - minY + 1;
        cellS = Math.min((W + sizeX - 1) / sizeX, (H + sizeY - 1) / sizeY);
        field = new Obj[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeX; j++) {
                field[i][j] = Obj.EMPTY;
            }
        }
        for (var zp : world.zpots) {
            tryFillField(zp.x, zp.y, Obj.ZPOT);
        }
        for (var block : game.base) {
            Obj o = block.isHead ? Obj.BASE : Obj.BLOCK;
            tryFillField(block.x, block.y, o);
        }
        for (var block : game.enemyBlocks) {
            Obj o = block.isHead ? Obj.ENEMY_BASE : Obj.ENEMY_BLOCK;
            tryFillField(block.x, block.y, o);
        }
    }

    private synchronized void paintImpl(Graphics g) {
        g.setColor(new Color(50, 50, 50));
        for (int i = 0; i < W; i += cellS) {
            g.drawRect(i, 0, 1, H);
        }
        for (int i = 0; i < H; i += cellS) {
            g.drawRect(0, i, W, 1);
        }
        initField();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                Obj o = field[i][j];
                g.setColor(new Color(o.r, o.g, o.b));
                g.drawRect(i * cellS + 1, j * cellS + 1, cellS - 2, cellS - 2);
            }
        }
    }

    private void buildBlock(int x, int y) {
        int realX = x / cellS;
        int realY = y / cellS;
        field[realX][realY] = Obj.FUTURE_BLOCK;
    }

    private void moveBase(int x, int y) {
        int realX = x / cellS;
        int realY = y / cellS;
        field[realX][realY] = Obj.FUTURE_BASE;
    }

    public int getFutureBaseX() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == Obj.FUTURE_BASE) return i + shiftX;
            }
        }
        return -1;
    }

    public List<Integer> getFutureBlocksX() {
        List<Integer> blocks = new ArrayList<>();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == Obj.FUTURE_BASE) {
                    blocks.add(i + shiftX);
                }
            }
        }
        return blocks;
    }
}
