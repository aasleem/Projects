package minesweeper;

import static org.junit.Assert.*;

import minesweeper.Minesweeper;
import minesweeper.Minesweeper.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinesweeperTest {

    Minesweeper minesweeper;
    List<Integer> rowsAndColumns = new ArrayList<Integer>();

    class MinesweeperWithExposeCell extends Minesweeper {
        public void exposeCell(int row, int col) {
            rowsAndColumns.add(row);
            rowsAndColumns.add(col);
        }
    }

    @Before
    public void setUp() { minesweeper = new Minesweeper(false); }

    @Test
    public void canary() { assertTrue(true); }

    @Test
    public void exposeAnUnexposedCell(){
        minesweeper.exposeCell(2, 4);

        assertEquals(CellState.valueOf("EXPOSED"), minesweeper.getCellState(2, 4));
    }

    @Test
    public void exposeAnAlreadyExposedCell(){
        minesweeper.exposeCell(2, 4);
        minesweeper.exposeCell(2, 4);

        assertEquals(CellState.valueOf("EXPOSED"), minesweeper.getCellState(2, 4));
    }

    @Test
    public void exposeMutlipleUnexposedCells(){
        minesweeper.exposeCell(1, 4);
        minesweeper.exposeCell(2, 4);

        assertEquals(CellState.valueOf("EXPOSED"), minesweeper.getCellState(1, 4));
        assertEquals(CellState.valueOf("EXPOSED"), minesweeper.getCellState(2, 4));
    }

    @Test
    public void exposeNeighborOfACell() {
        final boolean[] called = new boolean[] { false };
        minesweeper = new Minesweeper(false) {
            protected void exposeNeighborsOf(int row, int column) { called[0] = true; }
        };

        minesweeper.exposeCell(3, 4);

        assertEquals(CellState.valueOf("EXPOSED"), minesweeper.getCellState(3, 4));
        assertTrue(called[0]);
    }

    @Test
    public void exposeAlreadyExposedCellAndDontExposeNeighborCellAgain(){
        final boolean[] called = new boolean[] { false };

        minesweeper = new Minesweeper(false) {
            protected void exposeNeighborsOf(int row, int column) { called[0] = true; }
        };

        minesweeper.exposeCell(2, 4);

        assertTrue(called[0]);

        called[0] = false;
        minesweeper.exposeCell(2, 4);
        assertEquals(CellState.valueOf("EXPOSED"), minesweeper.getCellState(2, 4));
        assertFalse(called[0]);
    }

    @Test
    public void exposeEmptyCellThenExposeAllPosibleNeighbors(){
        Minesweeper minesweeper = new MinesweeperWithExposeCell();

        minesweeper.exposeNeighborsOf(3, 4);

        assertEquals(Arrays.asList(2, 3, 2, 4, 2, 5, 3, 3, 3, 5, 4, 3, 4, 4, 4, 5), rowsAndColumns);
    }

    @Test
    public void exposeTopLeftCellThenExposeAllPossibleNeighbors(){
        Minesweeper minesweeper = new MinesweeperWithExposeCell();

        minesweeper.exposeNeighborsOf(0, 0);

        assertEquals(Arrays.asList(0, 1, 1, 0, 1, 1), rowsAndColumns);
    }

    @Test
    public void exposeBottomLeftCellThenExposeAllPossibleNeighbors(){
        Minesweeper minesweeper = new MinesweeperWithExposeCell();

        minesweeper.exposeNeighborsOf(9, 0);

        assertEquals(Arrays.asList(8, 0, 8, 1, 9, 1), rowsAndColumns);
    }

    @Test
    public void exposeTopRightCellThenExposeAllPossibleNeighbors(){
        Minesweeper minesweeper = new MinesweeperWithExposeCell();

        minesweeper.exposeNeighborsOf(0, 9);

        assertEquals(Arrays.asList(0, 8, 1, 8, 1, 9), rowsAndColumns);
    }

    @Test
    public void exposeBottomRighCellThenExposeAllPossibleNeighbors(){
        minesweeper = new MinesweeperWithExposeCell();

        minesweeper.exposeNeighborsOf(9, 9);

        assertEquals(Arrays.asList(8, 8, 8, 9, 9, 8), rowsAndColumns);
    }

    @Test
    public void sealCellThatIsUnexposed(){
        minesweeper.toggleSeal(2, 2);

        assertEquals(CellState.valueOf("SEALED"), minesweeper.getCellState(2, 2));
    }

    @Test
    public void unsealCellThatIsUnexposed(){
        minesweeper.toggleSeal(2, 2);
        minesweeper.toggleSeal(2, 2);

        assertEquals(CellState.valueOf("UNEXPOSED"), minesweeper.getCellState(2, 2));
    }

    @Test
    public void tryToExposeCellThatHasBeenSealed(){
        minesweeper.toggleSeal(2, 2);
        minesweeper.exposeCell(2, 2);

        assertEquals(CellState.valueOf("SEALED"), minesweeper.getCellState(2, 2));
    }

    @Test
    public void tryToSealCellThatHasBeenExposed(){
        minesweeper.exposeCell(2, 2);
        minesweeper.toggleSeal(2, 2);

        assertEquals(CellState.valueOf("EXPOSED"), minesweeper.getCellState(2, 2));
    }

    @Test
    public void exposeSealedCellAndDontExposeNeighbors(){
        final boolean[] called = new boolean[] { false };
        minesweeper = new Minesweeper(false) {
            protected void exposeNeighborsOf(int row, int column) { called[0] = true; }
        };

        minesweeper.toggleSeal(2, 2);
        minesweeper.exposeCell(2, 2);

        assertFalse(called[0]);
    }

    @Test
    public void exposeMinedCellTryToExposeAnotherEmptyCell(){

        minesweeper.mineGrid[4][7] = true;

        minesweeper.exposeCell(4, 7);
        minesweeper.exposeCell(1, 2);

        assertEquals(CellState.valueOf("UNEXPOSED"), minesweeper.getCellState(1, 2));
    }

    @Test
    public void exposeMinedCellTryToSealCell(){

        minesweeper.mineGrid[4][7] = true;

        minesweeper.exposeCell(4, 7);
        minesweeper.toggleSeal(2, 2);

        assertNotEquals(CellState.valueOf("SEALED"), minesweeper.getCellState(2, 2));
    }

    @Test
    public void exposeCellOnAdjacentDoesNotExposeNeighbors(){
        final boolean[] called = new boolean[] { false };
        minesweeper = new Minesweeper(false) {
            @Override
            public int isAnAdjacentCell(int row, int column) { return 1; }
            @Override
            protected void exposeNeighborsOf(int row, int column) { called[0] = true; }
        };

        minesweeper.exposeCell(3, 4);

        assertEquals(CellState.valueOf("EXPOSED"), minesweeper.getCellState(3, 4));
        assertFalse(called[0]);
    }

    @Test
    public void setMineAndCheckIfANeighborIsAdjacent(){
        minesweeper.mineGrid[3][3] = true;
        assertEquals(1, minesweeper.isAnAdjacentCell(2, 2));
    }

    @Test
    public void setMineAndCheckItselfIsNotAdjacent(){
        minesweeper.mineGrid[3][3] = true;
        assertEquals(0, minesweeper.isAnAdjacentCell(3, 3));
    }

    @Test
    public void setMineAndExposeRandomCellThenCheckStatus(){
        minesweeper.mineGrid[1][1] = true;

        minesweeper.exposeCell(0, 0);
        assertEquals(GameStatus.valueOf("INPROGRESS"), minesweeper.getGameStatus());
    }

    @Test
    public void setMineAndExposeItThenCheckStatus(){
        minesweeper.mineGrid[1][1] = true;

        minesweeper.exposeCell(1, 1);
        assertEquals(GameStatus.valueOf("LOST"), minesweeper.getGameStatus());
    }

    @Test
    public void setMineAndSealItAndExposeRandomCellThenCheckStatus(){
        minesweeper.mineGrid[0][0] = true;

        minesweeper.toggleSeal(0, 0);
        assertEquals(GameStatus.valueOf("INPROGRESS"), minesweeper.getGameStatus());

        minesweeper.exposeCell(9, 9);
        assertEquals(GameStatus.valueOf("WON"), minesweeper.getGameStatus());
    }

    @Test
    public void placeTenRandomMine(){
        Minesweeper minesweeperGame = new Minesweeper();
        int numOfMine = 0;

        for(int row = 0; row < 10; row++)
            for (int col = 0; col < 10; col++)
                if (minesweeperGame.isMineCell(row, col))
                    numOfMine++;

        assertEquals(10, numOfMine);
    }

    @Test
    public void placeRandomMinesInTwoGamesCheckIfDifferent(){
        Minesweeper minesweeperGame1 = new Minesweeper();
        Minesweeper minesweeperGame2 = new Minesweeper();
         boolean allSame = true;
        
        for(int row = 0; row < 10; row++)
            for (int col = 0; col < 10; col++)
                 if(minesweeperGame1.isMineCell(row, col) != minesweeperGame2.isMineCell(row, col))
                   allSame = false;

         assertFalse(allSame);
    }
}