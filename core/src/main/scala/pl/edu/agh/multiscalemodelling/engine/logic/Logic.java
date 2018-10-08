package pl.edu.agh.multiscalemodelling.engine.logic;

public abstract class Logic {

    protected Board board;
    protected boolean isPaused;

    public Logic() {
        isPaused = true;
    }


    public void pause() {

        isPaused = !isPaused;

    }

    public void iterate() {

        //long startTime = System.currentTimeMillis();
        int threadsNum = Runtime.getRuntime().availableProcessors() * Runtime.getRuntime().availableProcessors();
        //threadsNum = 1;
        int start = 0;
        int end = 0;
        Thread[] threads = new Thread[threadsNum];

        //board.setNeighbourhood(new PentagonalRandomNeighbourhood(), new PeriodicBoudaryCondition());

        for (int i = 0; i < threadsNum; i++) {

            if (board.cells.size() % threadsNum == 0) {

                end += board.cells.size() / threadsNum;

            } else {

                end += (board.cells.size() / threadsNum) + 1;

            }

            int warunek = (end - start) * (i + 1);

            if (warunek > board.cells.size()) {
                end -= (warunek - board.cells.size());
            }

            threads[i] = createThread(board, start, end);
            threads[i].setName(Integer.toString(i));
            threads[i].start();

            start = end;

        }

        for (int i = 0; i < threadsNum; i++) {

            try {
                threads[i].join();
            } catch (InterruptedException e) {
            }

        }

        long endTime = System.currentTimeMillis();

        //System.out.println((endTime - startTime));


    }

    public Thread createThread(Board board, int start, int end) {

        return new LogicThread(board.cells.subList(start, end));

    }

    public boolean isPaused() {

        return isPaused;

    }

    public abstract void click(int x, int y);

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
