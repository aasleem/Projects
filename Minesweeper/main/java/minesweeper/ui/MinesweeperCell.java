package minesweeper.ui;

import javax.swing.*;

public class MinesweeperCell extends JButton {
    public final int row;
    public final int column;  
    
    public MinesweeperCell(int theRow, int theCol) {
        row = theRow;
        column = theCol;
        setSize(20, 20);
    }

    public int getRow() { return row; }
    
    public int getColumn() { return column; }
}