package net.deechael.khl.task;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public interface TaskScheduler {

    int scheduleSyncDelayedTask(@NotNull Runnable task, long delay);

    @Deprecated
    int scheduleSyncDelayedTask(@NotNull KaiheilaRunnable task, long delay);

    int scheduleSyncDelayedTask(@NotNull Runnable task);

    @Deprecated
    int scheduleSyncDelayedTask(@NotNull KaiheilaRunnable task);

    int scheduleSyncRepeatingTask(@NotNull Runnable task, long delay, long period);

    @Deprecated
    int scheduleSyncRepeatingTask(@NotNull KaiheilaRunnable task, long delay, long period);

    @Deprecated
    int scheduleAsyncDelayedTask(@NotNull Runnable task, long delay);

    @Deprecated
    int scheduleAsyncDelayedTask(@NotNull Runnable task);

    @Deprecated
    int scheduleAsyncRepeatingTask(@NotNull Runnable task, long delay, long period);

    @NotNull
    <T> Future<T> callSyncMethod(@NotNull Callable<T> task);

    void cancelTask(int taskId);

    void cancelTasks();

    boolean isCurrentlyRunning(int taskId);

    boolean isQueued(int taskId);

    @NotNull
    List<Worker> getActiveWorkers();

    @NotNull
    List<Task> getPendingTasks();

    @NotNull
    Task runTask(@NotNull Runnable task) throws IllegalArgumentException;

    void runTask(@NotNull Consumer<Task> task) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    Task runTask(@NotNull KaiheilaRunnable task) throws IllegalArgumentException;

    @NotNull
    Task runTaskAsynchronously(@NotNull Runnable task) throws IllegalArgumentException;

    void runTaskAsynchronously(@NotNull Consumer<Task> task) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    Task runTaskAsynchronously(@NotNull KaiheilaRunnable task) throws IllegalArgumentException;

    @NotNull
    Task runTaskLater(@NotNull Runnable task, long delay) throws IllegalArgumentException;

    void runTaskLater(@NotNull Consumer<Task> task, long delay) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    Task runTaskLater(@NotNull KaiheilaRunnable task, long delay) throws IllegalArgumentException;

    @NotNull
    Task runTaskLaterAsynchronously(@NotNull Runnable task, long delay) throws IllegalArgumentException;

    void runTaskLaterAsynchronously(@NotNull Consumer<Task> task, long delay) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    Task runTaskLaterAsynchronously(@NotNull KaiheilaRunnable task, long delay) throws IllegalArgumentException;

    @NotNull
    Task runTaskTimer(@NotNull Runnable task, long delay, long period) throws IllegalArgumentException;

    void runTaskTimer(@NotNull Consumer<Task> task, long delay, long period) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    Task runTaskTimer(@NotNull KaiheilaRunnable task, long delay, long period) throws IllegalArgumentException;

    @NotNull
    Task runTaskTimerAsynchronously(@NotNull Runnable task, long delay, long period) throws IllegalArgumentException;

    void runTaskTimerAsynchronously(@NotNull Consumer<Task> task, long delay, long period) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    Task runTaskTimerAsynchronously(@NotNull KaiheilaRunnable task, long delay, long period) throws IllegalArgumentException;
}
