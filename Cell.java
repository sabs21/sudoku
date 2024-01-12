public class Cell
{
    private int data;
    private int row;
    private int col;
    private int subGrid; // Holds which sub-grid this cell belongs to.
    private boolean isHint = false;
    private RankSeq validNumbers= new RankSeq(); // Create and fill a ranked sequence of potential valid numbers for this cell in the sudoku grid.
    
    public Cell(int d, int s, int r, int c) throws BoundaryViolationException
    {
        data = d;
        subGrid = s;
        row = (s/3)*3 + r; // converts r parameter into row position on the board.
        col = (s%3)*3 + c; // converts c parameter into column position on the board.
        initializeValidNumbers(); // Fills the valid number ranked sequence.
    }
    
    // These methods aid in keeping track of which numbers have been tried for each cell.
    public void initializeValidNumbers() throws BoundaryViolationException
    {
        RankSeq newValidNumbers = new RankSeq();
        for (int i = 1; i < 10; i++)
        {
            newValidNumbers.insertItem(i, i);
        }
        validNumbers = newValidNumbers;
    }
    public RankSeq getValidNumbers()
    {
        return validNumbers;
    }
    public void setValidNumbers(RankSeq newValidNumbers)
    {
        validNumbers = newValidNumbers;
    }
    
    // Getter and setter for the integer within the cell.
    public void setData(int d)
    {
        data = d;
    }
    public int getData()
    {
        return data;
    }
    
    // Check if the cell is empty or not.
    public boolean isEmpty()
    {
        if (data <= 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    // This is for handling hints within the board.
    public boolean makeHint() {
        // If isHint is already set to true, then return false.
        if (isHint)
        {
            return false;
        }
        
        // Otherwise, set isHint to true and return true.
        isHint = true;
        return true;
    }
    public boolean isHint() {
        return isHint;
    }
    
    // Getters and setters within the number range [0 - 8]
    public void setRow(int r)
    {
        row = r;
    }
    public int getRow()
    {
        return row;
    }
    public void setCol(int c)
    {
        col = c;
    }
    public int getCol()
    {
        return col;
    }
    public int getSubGrid()
    {
        return subGrid;
    }
    
    public String toString()
    {
        // Add 1 to row, col, and subGrid to budge the 0th index to index 1.
        return "Cell Data:\nSub-grid = " + (subGrid + 1) + ", Row = " + (row + 1) + ", Column = " + (col + 1) + ", Data = " + data + ".\n";
    }
}
