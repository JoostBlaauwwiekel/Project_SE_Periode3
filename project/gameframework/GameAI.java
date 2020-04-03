package project.gameframework;

public interface GameAI {

    /**
     * This method should evaluate the board passed as a parameter. The current state of the board could either be
     * a winning state, a losing state or a neutral state.
     *
     * @param board the board about to be checked.
     * @return a given value which indicates the state of the board.
     */
    public int evaluate(GameBoardLogic board);

    /**
     * This method should iterate the whole game board and it should determine which move currently is the best move
     * to make. This method should use the computeAlgorithm() method. Which is an implementation of the algorithm or AI
     * used. (E.g. Minimax, Neural Network, Tabular Q learning, etc).
     *
     * @return the best move.
     */
    public int getBestMove(GameBoardLogic board, int player);

    /**
     * This is method should contain the main logic of the algorithm/ AI. This method can implemented as the user wishes
     * to do so.
     *
     * @return a move, depending on the implementation of the algorithm/ AI.
     */
    public int computeAlgorithm(GameBoardLogic board, int depth);
}