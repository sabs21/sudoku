public class Check
{
    // Checks for any duplicates of the value of grid[column][row] within its respective sub-grid.
    public static boolean subGrid (Cell[][][] grid, int num, int subGrid)
    {
        boolean noDupe = true;

        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 3; col++)
            {
                int cellData = grid[subGrid][row][col].getData();
                if (cellData == num)
                {
                    noDupe = false;
                    row = 3;
                    col = 3;
                }
            }
        }
        return noDupe;
    }

    // Column relies on a number range of [0 - 8] for col(umn) parameter
    public static boolean column (Cell[][][] grid, int num, int col)
    {
        boolean noDupe = true;

        // We skip unnecessary subGrid searches by starting at col / 3 and incrementing subGrid by 3.
        for (int subGrid = col / 3; subGrid < 9; subGrid += 3)
        {
            for (int row = 0; row < 3; row++)
            {
                int cellData = grid[subGrid][row][col%3].getData();
                if (cellData == num)
                {
                    noDupe = false;
                    row = 3;
                    subGrid = 9;
                }
            }
        }
        return noDupe;
    }

    // Row relies on a number range of [0 - 8] for row parameter
    public static boolean row (Cell[][][] grid, int num, int row)
    {
        boolean noDupe = true;
        int initialsubGrid = (row/3)*3; // Can either be 0, 3, or 6.

        for (int subGrid = initialsubGrid; subGrid < initialsubGrid + 3; subGrid++)
        {
            for (int col = 0; col < 3; col++)
            {
                int cellData = grid[subGrid][row%3][col].getData();
                if (cellData == num)
                {
                    noDupe = false;
                    col = 3;
                    subGrid = 9;
                }
            }
        }
        return noDupe;
    }

    // Uses all of the methods above to check if the cell is in a valid place.
    public static boolean valid (Cell[][][] grid, int num, Cell cell)
    {
        boolean subGridIsFine = Check.subGrid(grid, num, cell.getSubGrid());
        boolean rowIsFine = Check.row(grid, num, cell.getRow());
        boolean columnIsFine = Check.column(grid, num, cell.getCol());
        
        if (subGridIsFine && rowIsFine && columnIsFine)
        {
            return true;
        }
        return false;
    }
}
