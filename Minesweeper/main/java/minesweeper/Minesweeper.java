package minesweeper;

import java.util.Random;

public class Minesweeper {

    public enum CellState {UNEXPOSED, EXPOSED, SEALED}
    public enum GameStatus {INPROGRESS, WON, LOST}

    boolean [][] mineGrid = new boolean[10][10];
    CellState[][] stateGrid = new CellState[10][10];

    public Minesweeper() { this(true); }

    protected Minesweeper(boolean needRandomMines) {
        for(int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                stateGrid[i][j] = CellState.UNEXPOSED;
        if(needRandomMines)
            placeRandomMines();
    }

    public void exposeCell(int row, int col) {
        if(getGameStatus() == GameStatus.INPROGRESS && getCellState(row, col) == CellState.UNEXPOSED) {
            stateGrid[row][col] = CellState.EXPOSED;
            if(isAnAdjacentCell(row, col) == 0)
                exposeNeighborsOf(row, col);
        }
    }

    protected void exposeNeighborsOf(int row, int col) {
        for (int rowOffset = -1; rowOffset < 2; rowOffset++)
            for (int colOffset = -1; colOffset < 2; colOffset++)
                if (!(rowOffset == 0 && colOffset == 0) && isValidLocation(row + rowOffset, col + colOffset))
                    exposeCell(row + rowOffset, col + colOffset);
    }

    public void toggleSeal(int row, int col) {
        if(getGameStatus() == GameStatus.INPROGRESS)
            if (getCellState(row, col) == CellState.SEALED)
                stateGrid[row][col] = CellState.UNEXPOSED;
            else
                stateGrid[row][col] = CellState.SEALED;
    }

    public int isAnAdjacentCell(int row, int col) {
        int numOfAdjacentMines = 0;

        for (int rowOffset = -1; rowOffset < 2; rowOffset++)
            for (int colOffset = -1; colOffset < 2; colOffset++)
                if (!(rowOffset == 0 && colOffset == 0) && isValidLocation(row + rowOffset, col + colOffset)
                        && isMineCell(row + rowOffset, col + colOffset))
                        numOfAdjacentMines++;

        return numOfAdjacentMines;
    }

    public CellState getCellState(int row, int col) {
        return stateGrid[row][col];
    }

    public boolean isMineCell(int row, int col) { return mineGrid[row][col]; }

    protected boolean isValidLocation(int row, int col) { return ((row  >= 0 && row  <= 9) && (col  >= 0 && col <= 9)); }

    protected void placeRandomMines(){
        Random random = new Random();
        int numOfMinesPlaced = 0;

        while(numOfMinesPlaced < 10) {
            int rowRand = random.nextInt(10);
            int colRand = random.nextInt(10);

            if(!isMineCell(rowRand, colRand)) {
                mineGrid[rowRand][colRand] = true;
                numOfMinesPlaced++;
            }
        }
    }

    public GameStatus getGameStatus() {
        boolean inProgress = false;
        
        for(int row = 0; row < 10; row++) {
          for(int column = 0; column < 10; column++) {
            if(isMineCell(row, column) && getCellState(row, column) == CellState.EXPOSED)
              return GameStatus.LOST;

            if(getCellState(row, column) == CellState.UNEXPOSED ||
              !isMineCell(row, column) && getCellState(row, column) == CellState.SEALED)
              inProgress = true;
          }
        }
        return inProgress ? GameStatus.INPROGRESS : GameStatus.WON;
    }
}