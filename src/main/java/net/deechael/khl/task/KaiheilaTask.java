package net.deechael.khl.task;

import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;

import java.util.function.Consumer;

class KaiheilaTask extends KaiheilaObject implements Task, Runnable {

    private volatile KaiheilaTask next = null;
    public static final int ERROR = 0;
    public static final int NO_REPEATING = -1;
    public static final int CANCEL = -2;
    public static final int PROCESS_FOR_FUTURE = -3;
    public static final int DONE_FOR_FUTURE = -4;
    /**
     * -1 means no repeating <br>
     * -2 means cancel <br>
     * -3 means processing for Future <br>
     * -4 means done for Future <br>
     * Never 0 <br>
     * >0 means number of ticks to wait between each execution
     */
    private volatile long period;
    private long nextRun;
    private final Runnable rTask;
    private final Consumer<Task> cTask;
    private final int id;
    private final long createdAt = System.nanoTime();

    KaiheilaTask(Gateway gateway) {
        this(gateway, null, KaiheilaTask.NO_REPEATING, KaiheilaTask.NO_REPEATING);
    }

    KaiheilaTask(Gateway gateway, final Object task) {
        this(gateway, task, KaiheilaTask.NO_REPEATING, KaiheilaTask.NO_REPEATING);
    }

    KaiheilaTask(Gateway gateway, final Object task, final int id, final long period) {
        super(gateway);
        if (task instanceof Runnable) {
            this.rTask = (Runnable) task;
            this.cTask = null;
        } else if (task instanceof Consumer) {
            this.cTask = (Consumer<Task>) task;
            this.rTask = null;
        } else if (task == null) {
            // Head or Future task
            this.rTask = null;
            this.cTask = null;
        } else {
            throw new AssertionError("Illegal task class " + task);
        }
        this.id = id;
        this.period = period;
    }

    @Override
    public final int getTaskId() {
        return id;
    }

    @Override
    public boolean isSync() {
        return true;
    }

    @Override
    public void run() {
        if (rTask != null) {
            rTask.run();
        } else {
            cTask.accept(this);
        }
    }

    long getCreatedAt() {
        return createdAt;
    }

    long getPeriod() {
        return period;
    }

    void setPeriod(long period) {
        this.period = period;
    }

    long getNextRun() {
        return nextRun;
    }

    void setNextRun(long nextRun) {
        this.nextRun = nextRun;
    }

    KaiheilaTask getNext() {
        return next;
    }

    void setNext(KaiheilaTask next) {
        this.next = next;
    }

    Class<?> getTaskClass() {
        return (rTask != null) ? rTask.getClass() : ((cTask != null) ? cTask.getClass() : null);
    }

    @Override
    public boolean isCancelled() {
        return (period == KaiheilaTask.CANCEL);
    }

    @Override
    public void cancel() {
        getKaiheilaBot().getScheduler().cancelTask(id);
    }

    /**
     * This method properly sets the status to cancelled, synchronizing when required.
     *
     * @return false if it is a craft future task that has already begun execution, true otherwise
     */
    boolean cancel0() {
        setPeriod(KaiheilaTask.CANCEL);
        return true;
    }
}
