import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.shape.*; 
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.util.Random;

public class SudokuGrid extends Application
{
    // grid[Subgrid][Row inside subGrid][Column inside subGrid]
    Cell[][][] grid = new Cell[9][3][3]; // Holds all cells in the sudoku grid, used for calculations.
    Label[][][] labels = new Label[9][3][3]; // Holds what's to be shown on the visual sudoku board.

    GridPane board = new GridPane(); // This will represent the visual Sudoku board
    Button solveBtn = new Button("Solve!");
    Random gen = new Random();

    public void start(Stage primary) throws BoundaryViolationException
    {
        // Fill the grid matrix with Cells as if we were reading the sudoku puzzle left to right, row by row.
        // Initialize the grid matrix and labels matrix.
        for (int subGrid = 0; subGrid < 9; subGrid++)
        {
            for (int row = 0; row < 3; row++)
            {
                for (int col = 0; col < 3; col++)
                {
                    grid[subGrid][row][col] = new Cell(0, subGrid, row, col);
                    labels[subGrid][row][col] = new Label("0");
                }
            }
        }

        solve(0,0,0);
        setHints(40);
        removeNonHints();

        // Create 9 sub-grid GridPanes and place them in correct order into the board GridPane.
        for (int subGrid = 0; subGrid < 9; subGrid++)
        {
            GridPane subGridPane = new GridPane();
            for (int row = 0; row < 3; row++)
            {
                for (int col = 0; col < 3; col++)
                {
                    Cell currentCell = grid[subGrid][row][col]; // Retrieve the current cell from the grid matrix 
                    Label currentLabel = labels[subGrid][row][col];
                    String number = Integer.toString(currentCell.getData());

                    // Create and stylize each number on the sudoku board.
                    currentLabel.setText(number);
                    if (number.equals("0")) // If the value of the cell is 0, then make the cell look empty.
                    {
                        currentLabel.setTextFill(Color.WHITE);
                    }
                    currentLabel.setStyle("-fx-border-color: rgb(170, 170, 170); -fx-padding: 10 24 10 24;");
                    currentLabel.setFont(new Font(20));

                    // Place the number into the correct cell on the board.
                    GridPane.setConstraints(currentLabel, col, row);
                    subGridPane.getChildren().add(currentLabel); // Add the label to the Regional Box
                }
            }
            // Sub-grid GridPane styling.
            subGridPane.setHgap(0);
            subGridPane.setVgap(0);
            subGridPane.setStyle("-fx-background-color: white;");

            // Place the sub-grid into the correct spot.
            GridPane.setConstraints(subGridPane, subGrid % 3, subGrid / 3);
            board.getChildren().add(subGridPane);
        }

        // Sudoku board styling
        board.setHgap(4);
        board.setVgap(3);
        board.setTranslateX(95);
        board.setTranslateY(50);
        board.setStyle("-fx-background-color: black; -fx-padding: 1;");

        // Sudoku solve button
        solveBtn.setTranslateX(324);
        solveBtn.setTranslateY(540);
        solveBtn.setPrefWidth(100);
        solveBtn.setOnAction(this::solveAction);

        // Add the sudoku board to the scene and show it.
        Group root = new Group(board, solveBtn);
        Scene scene = new Scene(root, 800, 600, Color.rgb(240,240,240));
        primary.setTitle("Sudoku");
        primary.setScene(scene);
        primary.show();
    }

    // We are recursively solving each cell on the sudoku board.
    public boolean solve(int subGrid, int row, int col) throws BoundaryViolationException
    {
        //System.out.println("CALL: solve(subGrid = " + subGrid + ", row = " + row + ", col = " + col + ")");
        int s = subGrid;
        int r = row;
        int c = col + 1;

        // Base case
        if (c > 2) // Move to next column
        {
            if (r < 2) // Move to next row
            {
                r = row + 1;
                c = 0;
            }
            else // Move to next subGrid
            {
                if (s < 8)
                {
                    s = s + 1;
                    r = 0;
                    c = 0;
                }
                else if (col > 2)
                {
                    return true;
                }
            }
        }

        Cell current = grid[subGrid][row][col]; // Current cell
        if (current.isHint())
        {
            if (solve(s, r, c)) // If the recursive solve is successful, then return true.
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        int rank; // Rank to use within the ranked sequence of valid numbers.
        int num; // Number to add to the empty cell.
        RankSeq validNumbers = current.getValidNumbers(); // Current cell's valid number choices.
        //boolean mustBackTrack = true; // If we run out of number choices, this will remain true.

        int maxAttempts = validNumbers.size();

        for (int attempts = 0; attempts < maxAttempts; attempts++)
        {
            rank = gen.nextInt(validNumbers.size())+1; // Generate a random rank.
            num = (validNumbers.nodeAtRank(rank)).getData(); // Use that rank to get a valid number from the validNumbers ranked sequence.
            validNumbers.deleteItem(rank); // In case we must backtrack, remove this number from valid numbers.
            current.setValidNumbers(validNumbers); // Replace the cell's old valid numbers with an updated list of valid numbers.
            validNumbers = current.getValidNumbers(); // Store the cell's new list of valid number choices.

            if (Check.valid(grid, num, current)) // Check if placing the random number there is valid or not.
            {
                current.setData(num); // Set the cell to the random number.

                if (solve(s, r, c)) // If the recursive solve is successful, then return true.
                {
                    return true;
                }
            }
            else // Set the cell back to 0 and try another number.
            {
                current.setData(0);
            }
        }

        // If the for loop completes without returning, then we must backtrack.
        current.setData(0); // Reset this current cell's data to 0.
        current.initializeValidNumbers(); // Refill the ranked sequence of valid numbers.

        return false;
    }

    public void setHints(int numberOfHints)
    {
        for (int i = 0; i < numberOfHints; i++)
        {
            int subgrid = gen.nextInt(9);
            int row = gen.nextInt(3);
            int col = gen.nextInt(3);

            if (!grid[subgrid][row][col].makeHint())
            {
                i--;
            }
        }
    }

    public void removeNonHints() throws BoundaryViolationException {
        for (int subGrid = 0; subGrid < 9; subGrid++)
        {
            for (int row = 0; row < 3; row++)
            {
                for (int col = 0; col < 3; col++)
                {
                    // If the cell is not a hint, remove integer from cell.
                    if (!grid[subGrid][row][col].isHint())
                    {
                        grid[subGrid][row][col].setData(0);
                        grid[subGrid][row][col].initializeValidNumbers();
                    }
                }
            }
        }
    }

    public void solveAction(ActionEvent event) //throws BoundaryViolationException
    {
        try 
        {
            solve(0,0,0);
        }
        catch (BoundaryViolationException x) 
        {
            System.out.println("Invalid rank");
        }

        // Create 9 sub-grid GridPanes and place them in correct order into the board GridPane.
        for (int subGrid = 0; subGrid < 9; subGrid++)
        {
            for (int row = 0; row < 3; row++)
            {
                for (int col = 0; col < 3; col++)
                {
                    Cell currentCell = grid[subGrid][row][col]; // Retrieve the current cell from the grid matrix
                    String number = Integer.toString(currentCell.getData());

                    // Fill in each blank spot on the sudoku board.
                    labels[subGrid][row][col].setText(number);
                    if (number.equals("0")) // If the value of the cell is 0, then make the cell look empty.
                    {
                        labels[subGrid][row][col].setTextFill(Color.WHITE);
                    }
                    else
                    {
                        labels[subGrid][row][col].setTextFill(Color.BLACK);
                    }
                }
            }
        }

        solveBtn.setDisable(true);
    }
}
