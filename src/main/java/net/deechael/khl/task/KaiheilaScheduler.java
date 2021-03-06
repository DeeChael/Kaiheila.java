package net.deechael.khl.task;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.deechael.khl.core.KaiheilaObject;
import net.deechael.khl.gate.Gateway;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;

public class KaiheilaScheduler extends KaiheilaObject implements TaskScheduler {

    protected static final Logger Log = LoggerFactory.getLogger(KaiheilaScheduler.class);

    private static final int START_ID = 1;

    private static final IntUnaryOperator INCREMENT_IDS = previous -> {
        // We reached the end, go back to the start!
        if (previous == Integer.MAX_VALUE) {
            return START_ID;
        }
        return previous + 1;
    };
    private static final int RECENT_TICKS;

    static {
        RECENT_TICKS = 30;
    }

    private final AtomicInteger ids = new AtomicInteger(START_ID);
    private final AtomicReference<KaiheilaTask> tail;
    private final PriorityQueue<KaiheilaTask> pending = new PriorityQueue<KaiheilaTask>(10,
            new Comparator<KaiheilaTask>() {
                @Override
                public int compare(final KaiheilaTask o1, final KaiheilaTask o2) {
                    int value = Long.compare(o1.getNextRun(), o2.getNextRun());

                    // If the tasks should run on the same tick they should be run FIFO
                    return value != 0 ? value : Long.compare(o1.getCreatedAt(), o2.getCreatedAt());
                }
            });
    private final List<KaiheilaTask> temp = new ArrayList<KaiheilaTask>();
    private final ConcurrentHashMap<Integer, KaiheilaTask> runners = new ConcurrentHashMap<Integer, KaiheilaTask>();
    private final Executor executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("Craft Scheduler Thread - %d").build());
    private volatile KaiheilaTask head;
    private volatile KaiheilaTask currentTask = null;
    private volatile int currentTick = -1;
    private KaiheilaAsyncDebugger debugHead = new KaiheilaAsyncDebugger(-1, null) {
        @Override
        StringBuilder debugTo(StringBuilder string) {
            return string;
        }
    };
    private KaiheilaAsyncDebugger debugTail = debugHead;

    public KaiheilaScheduler(Gateway gateway) {
        super(gateway);
        this.head = new KaiheilaTask(getGateway());
        this.tail = new AtomicReference<>(head);
    }

    private static void validate(final Object task) {
        Validate.notNull(task, "Task cannot be null");
        Validate.isTrue(task instanceof Runnable || task instanceof Consumer || task instanceof Callable, "Task must be Runnable, Consumer, or Callable");
    }

    @Override
    public int scheduleSyncDelayedTask(final Runnable task) {
        return this.scheduleSyncDelayedTask(task, 0L);
    }

    @Override
    public Task runTask(Runnable runnable) {
        return runTaskLater(runnable, 0L);
    }

    @Override
    public void runTask(Consumer<Task> task) throws IllegalArgumentException {
        runTaskLater(task, 0L);
    }

    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(final Runnable task) {
        return this.scheduleAsyncDelayedTask(task, 0L);
    }

    @Override
    public Task runTaskAsynchronously(Runnable runnable) {
        return runTaskLaterAsynchronously(runnable, 0L);
    }

    @Override
    public void runTaskAsynchronously(Consumer<Task> task) throws IllegalArgumentException {
        runTaskLaterAsynchronously(task, 0L);
    }

    @Override
    public int scheduleSyncDelayedTask(final Runnable task, final long delay) {
        return this.scheduleSyncRepeatingTask(task, delay, KaiheilaTask.NO_REPEATING);
    }

    @Override
    public Task runTaskLater(Runnable runnable, long delay) {
        return runTaskTimer(runnable, delay, KaiheilaTask.NO_REPEATING);
    }

    @Override
    public void runTaskLater(Consumer<Task> task, long delay) throws IllegalArgumentException {
        runTaskTimer(task, delay, KaiheilaTask.NO_REPEATING);
    }

    @Deprecated
    @Override
    public int scheduleAsyncDelayedTask(final Runnable task, final long delay) {
        return this.scheduleAsyncRepeatingTask(task, delay, KaiheilaTask.NO_REPEATING);
    }

    @Override
    public Task runTaskLaterAsynchronously(Runnable runnable, long delay) {
        return runTaskTimerAsynchronously(runnable, delay, KaiheilaTask.NO_REPEATING);
    }

    @Override
    public void runTaskLaterAsynchronously(Consumer<Task> task, long delay) throws IllegalArgumentException {
        runTaskTimerAsynchronously(task, delay, KaiheilaTask.NO_REPEATING);
    }

    @Override
    public void runTaskTimerAsynchronously(Consumer<Task> task, long delay, long period) throws IllegalArgumentException {
        runTaskTimerAsynchronously((Object) task, delay, KaiheilaTask.NO_REPEATING);
    }

    @Override
    public int scheduleSyncRepeatingTask(final Runnable runnable, long delay, long period) {
        return runTaskTimer(runnable, delay, period).getTaskId();
    }

    @Override
    public Task runTaskTimer(Runnable runnable, long delay, long period) {
        return runTaskTimer((Object) runnable, delay, period);
    }

    @Override
    public void runTaskTimer(Consumer<Task> task, long delay, long period) throws IllegalArgumentException {
        runTaskTimer((Object) task, delay, period);
    }

    public Task runTaskTimer(Object runnable, long delay, long period) {
        validate(runnable);
        if (delay < 0L) {
            delay = 0;
        }
        if (period == KaiheilaTask.ERROR) {
            period = 1L;
        } else if (period < KaiheilaTask.NO_REPEATING) {
            period = KaiheilaTask.NO_REPEATING;
        }
        return handle(new KaiheilaTask(getGateway(), runnable, nextId(), period), delay);
    }

    @Deprecated
    @Override
    public int scheduleAsyncRepeatingTask(final Runnable runnable, long delay, long period) {
        return runTaskTimerAsynchronously(runnable, delay, period).getTaskId();
    }

    @Override
    public Task runTaskTimerAsynchronously(Runnable runnable, long delay, long period) {
        return runTaskTimerAsynchronously((Object) runnable, delay, period);
    }

    public Task runTaskTimerAsynchronously(Object runnable, long delay, long period) {
        validate(runnable);
        if (delay < 0L) {
            delay = 0;
        }
        if (period == KaiheilaTask.ERROR) {
            period = 1L;
        } else if (period < KaiheilaTask.NO_REPEATING) {
            period = KaiheilaTask.NO_REPEATING;
        }
        return handle(new KaiheilaAsyncTask(runners, getGateway(), runnable, nextId(), period), delay);
    }

    @Override
    public <T> Future<T> callSyncMethod(final Callable<T> task) {
        validate(task);
        final KaiheilaFuture<T> future = new KaiheilaFuture<T>(task, getGateway(), nextId());
        handle(future, 0L);
        return future;
    }

    @Override
    public void cancelTask(final int taskId) {
        if (taskId <= 0) {
            return;
        }
        KaiheilaTask task = runners.get(taskId);
        if (task != null) {
            task.cancel0();
        }
        task = new KaiheilaTask(getGateway(),
                new Runnable() {
                    @Override
                    public void run() {
                        if (!check(KaiheilaScheduler.this.temp)) {
                            check(KaiheilaScheduler.this.pending);
                        }
                    }

                    private boolean check(final Iterable<KaiheilaTask> collection) {
                        final Iterator<KaiheilaTask> tasks = collection.iterator();
                        while (tasks.hasNext()) {
                            final KaiheilaTask task = tasks.next();
                            if (task.getTaskId() == taskId) {
                                task.cancel0();
                                tasks.remove();
                                if (task.isSync()) {
                                    runners.remove(taskId);
                                }
                                return true;
                            }
                        }
                        return false;
                    }
                });
        handle(task, 0L);
        for (KaiheilaTask taskPending = head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                return;
            }
            if (taskPending.getTaskId() == taskId) {
                taskPending.cancel0();
            }
        }
    }

    @Override
    public void cancelTasks() {
        final KaiheilaTask task = new KaiheilaTask(getGateway(),
                new Runnable() {
                    @Override
                    public void run() {
                        check(KaiheilaScheduler.this.pending);
                        check(KaiheilaScheduler.this.temp);
                    }

                    void check(final Iterable<KaiheilaTask> collection) {
                        final Iterator<KaiheilaTask> tasks = collection.iterator();
                        while (tasks.hasNext()) {
                            final KaiheilaTask task = tasks.next();
                            task.cancel0();
                            tasks.remove();
                            if (task.isSync()) {
                                runners.remove(task.getTaskId());
                            }
                        }
                    }
                });
        handle(task, 0L);
        for (KaiheilaTask taskPending = head.getNext(); taskPending != null; taskPending = taskPending.getNext()) {
            if (taskPending == task) {
                break;
            }
            if (taskPending.getTaskId() != -1) {
                taskPending.cancel0();
            }
        }
        for (KaiheilaTask runner : runners.values()) {
            runner.cancel0();
        }
    }

    @Override
    public boolean isCurrentlyRunning(final int taskId) {
        final KaiheilaTask task = runners.get(taskId);
        if (task == null) {
            return false;
        }
        if (task.isSync()) {
            return (task == currentTask);
        }
        final KaiheilaAsyncTask asyncTask = (KaiheilaAsyncTask) task;
        synchronized (asyncTask.getWorkers()) {
            return !asyncTask.getWorkers().isEmpty();
        }
    }

    @Override
    public boolean isQueued(final int taskId) {
        if (taskId <= 0) {
            return false;
        }
        for (KaiheilaTask task = head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() == taskId) {
                return task.getPeriod() >= KaiheilaTask.NO_REPEATING; // The task will run
            }
        }
        KaiheilaTask task = runners.get(taskId);
        return task != null && task.getPeriod() >= KaiheilaTask.NO_REPEATING;
    }

    @Override
    public List<Worker> getActiveWorkers() {
        final ArrayList<Worker> workers = new ArrayList<Worker>();
        for (final KaiheilaTask taskObj : runners.values()) {
            // Iterator will be a best-effort (may fail to grab very new values) if called from an async thread
            if (taskObj.isSync()) {
                continue;
            }
            final KaiheilaAsyncTask task = (KaiheilaAsyncTask) taskObj;
            synchronized (task.getWorkers()) {
                // This will never have an issue with stale threads; it's state-safe
                workers.addAll(task.getWorkers());
            }
        }
        return workers;
    }

    @Override
    public List<Task> getPendingTasks() {
        final ArrayList<KaiheilaTask> truePending = new ArrayList<KaiheilaTask>();
        for (KaiheilaTask task = head.getNext(); task != null; task = task.getNext()) {
            if (task.getTaskId() != -1) {
                // -1 is special code
                truePending.add(task);
            }
        }

        final ArrayList<Task> pending = new ArrayList<Task>();
        for (KaiheilaTask task : runners.values()) {
            if (task.getPeriod() >= KaiheilaTask.NO_REPEATING) {
                pending.add(task);
            }
        }

        for (final KaiheilaTask task : truePending) {
            if (task.getPeriod() >= KaiheilaTask.NO_REPEATING && !pending.contains(task)) {
                pending.add(task);
            }
        }
        return pending;
    }

    /**
     * This method is designed to never block or wait for locks; an immediate execution of all current tasks.
     */
    public void mainThreadHeartbeat(final int currentTick) {
        this.currentTick = currentTick;
        final List<KaiheilaTask> temp = this.temp;
        parsePending();
        while (isReady(currentTick)) {
            final KaiheilaTask task = pending.remove();
            if (task.getPeriod() < KaiheilaTask.NO_REPEATING) {
                if (task.isSync()) {
                    runners.remove(task.getTaskId(), task);
                }
                parsePending();
                continue;
            }
            if (task.isSync()) {
                currentTask = task;
                try {
                    task.run();
                } catch (final Throwable throwable) {
                    Log.warn(
                            String.format(
                                    "Task #%s generated an exception",
                                    task.getTaskId()),
                            throwable);
                } finally {
                    currentTask = null;
                }
                parsePending();
            } else {
                debugTail = debugTail.setNext(new KaiheilaAsyncDebugger(currentTick + RECENT_TICKS, task.getTaskClass()));
                executor.execute(task);
                // We don't need to parse pending
                // (async tasks must live with race-conditions if they attempt to cancel between these few lines of code)
            }
            final long period = task.getPeriod(); // State consistency
            if (period > 0) {
                task.setNextRun(currentTick + period);
                temp.add(task);
            } else if (task.isSync()) {
                runners.remove(task.getTaskId());
            }
        }
        pending.addAll(temp);
        temp.clear();
        debugHead = debugHead.getNextHead(currentTick);
    }

    private void addTask(final KaiheilaTask task) {
        final AtomicReference<KaiheilaTask> tail = this.tail;
        KaiheilaTask tailTask = tail.get();
        while (!tail.compareAndSet(tailTask, task)) {
            tailTask = tail.get();
        }
        tailTask.setNext(task);
    }

    private KaiheilaTask handle(final KaiheilaTask task, final long delay) {
        task.setNextRun(currentTick + delay);
        addTask(task);
        return task;
    }

    private int nextId() {
        Validate.isTrue(runners.size() < Integer.MAX_VALUE, "There are already " + Integer.MAX_VALUE + " tasks scheduled! Cannot schedule more.");
        int id;
        do {
            id = ids.updateAndGet(INCREMENT_IDS);
        } while (runners.containsKey(id)); // Avoid generating duplicate IDs
        return id;
    }

    private void parsePending() {
        KaiheilaTask head = this.head;
        KaiheilaTask task = head.getNext();
        KaiheilaTask lastTask = head;
        for (; task != null; task = (lastTask = task).getNext()) {
            if (task.getTaskId() == -1) {
                task.run();
            } else if (task.getPeriod() >= KaiheilaTask.NO_REPEATING) {
                pending.add(task);
                runners.put(task.getTaskId(), task);
            }
        }
        // We split this because of the way things are ordered for all of the async calls in CraftScheduler
        // (it prevents race-conditions)
        for (task = head; task != lastTask; task = head) {
            head = task.getNext();
            task.setNext(null);
        }
        this.head = lastTask;
    }

    private boolean isReady(final int currentTick) {
        return !pending.isEmpty() && pending.peek().getNextRun() <= currentTick;
    }

    @Override
    public String toString() {
        int debugTick = currentTick;
        StringBuilder string = new StringBuilder("Recent tasks from ").append(debugTick - RECENT_TICKS).append('-').append(debugTick).append('{');
        debugHead.debugTo(string);
        return string.append('}').toString();
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(KaiheilaRunnable task, long delay) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLater(long)");
    }

    @Deprecated
    @Override
    public int scheduleSyncDelayedTask(KaiheilaRunnable task) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTask(Plugin)");
    }

    @Deprecated
    @Override
    public int scheduleSyncRepeatingTask(KaiheilaRunnable task, long delay, long period) {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimer(long, long)");
    }

    @Deprecated
    @Override
    public Task runTask(KaiheilaRunnable task) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTask(Plugin)");
    }

    @Deprecated
    @Override
    public Task runTaskAsynchronously(KaiheilaRunnable task) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskAsynchronously(Plugin)");
    }

    @Deprecated
    @Override
    public Task runTaskLater(KaiheilaRunnable task, long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLater(long)");
    }

    @Deprecated
    @Override
    public Task runTaskLaterAsynchronously(KaiheilaRunnable task, long delay) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskLaterAsynchronously(long)");
    }

    @Deprecated
    @Override
    public Task runTaskTimer(KaiheilaRunnable task, long delay, long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimer(long, long)");
    }

    @Deprecated
    @Override
    public Task runTaskTimerAsynchronously(KaiheilaRunnable task, long delay, long period) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Use BukkitRunnable#runTaskTimerAsynchronously(long, long)");
    }
}
