package minesweeper.ui;

import minesweeper.*;
import minesweeper.Minesweeper.CellState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MinesweeperFrame extends JFrame {
    Minesweeper minesweeper;
    MinesweeperCell cellGrid[][];

    @Override
    protected void frameInit(){
        super.frameInit();

        cellGrid = new MinesweeperCell[10][10];
        minesweeper = new Minesweeper();

        setLayout(new GridLayout(10, 10));

        for (int row = 0; row < 10; row++)
            for(int col = 0; col < 10; col++) {
                cellGrid[row][col] = new MinesweeperCell(row, col);

                getContentPane().add(cellGrid[row][col] );
                cellGrid[row][col] .addMouseListener(new CellMouseListener());
            }
    }

    public static void main(String[] args){
        JFrame frame = new MinesweeperFrame();
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    private class CellMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent mouseEvent) {
            MinesweeperCell cell = (MinesweeperCell) mouseEvent.getSource();
            if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                minesweeper.toggleSeal(cell.getRow(), cell.getColumn());
                updateDisplay();
            } 
            
            else if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                minesweeper.exposeCell(cell.getRow(), cell.getColumn());
                updateDisplay();
            }

            Minesweeper.GameStatus status = minesweeper.getGameStatus();
            if(status == Minesweeper.GameStatus.WON) {
                JOptionPane.showMessageDialog(cell, "Congrats, won!!");
            }

            else if(status == Minesweeper.GameStatus.LOST) {
                showMines();
                JOptionPane.showMessageDialog(cell, "Sorry, you lost :(");
            }
        }

        public void updateDisplay() {
            for (int row = 0; row < 10; row++)
                for (int col = 0; col < 10; col++) {
                    CellState state = minesweeper.getCellState(row, col);
                    switch (state) {
                        case SEALED:
                            if (cellGrid[row][col].getText() == "")
                                cellGrid[row][col].setText("S");
                            break;
                        case UNEXPOSED:
                            if (cellGrid[row][col].getText() == "S")
                                cellGrid[row][col].setText("");
                            break;
                        case EXPOSED:
                            int numOfMines = minesweeper.isAnAdjacentCell(cellGrid[row][col].getRow(), cellGrid[row][col].getColumn());

                            if (cellGrid[row][col].getText() == "") {
                                if (numOfMines == 0 && minesweeper.isMineCell(cellGrid[row][col].getRow(), cellGrid[row][col].getColumn()))
                                    cellGrid[row][col].setText("M");
                                else if (numOfMines > 0)
                                    cellGrid[row][col].setText(Integer.toString(numOfMines));
                                else
                                    cellGrid[row][col].setText(".");
                            }
                            break;
                    }
                }
        }

        public void showMines(){
            for(int row = 0; row < 10; row++)
                for(int col = 0; col < 10; col++) {
                    if( minesweeper.isMineCell(cellGrid[row][col].getRow(),  cellGrid[row][col].getColumn()))
                        cellGrid[row][col].setText("M");
                }
        }

    }

}
