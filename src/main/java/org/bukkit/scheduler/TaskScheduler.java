package org.bukkit.scheduler;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public interface TaskScheduler {

    public int scheduleSyncDelayedTask(@NotNull Runnable task, long delay);

    @Deprecated
    public int scheduleSyncDelayedTask(@NotNull KaiheilaRunnable task, long delay);

    public int scheduleSyncDelayedTask(@NotNull Runnable task);

    @Deprecated
    public int scheduleSyncDelayedTask(@NotNull KaiheilaRunnable task);

    public int scheduleSyncRepeatingTask(@NotNull Runnable task, long delay, long period);

    @Deprecated
    public int scheduleSyncRepeatingTask(@NotNull KaiheilaRunnable task, long delay, long period);

    @Deprecated
    public int scheduleAsyncDelayedTask(@NotNull Runnable task, long delay);

    @Deprecated
    public int scheduleAsyncDelayedTask(@NotNull Runnable task);

    @Deprecated
    public int scheduleAsyncRepeatingTask(@NotNull Runnable task, long delay, long period);

    @NotNull
    public <T> Future<T> callSyncMethod(@NotNull Callable<T> task);

    public void cancelTask(int taskId);

    public void cancelTasks();

    public boolean isCurrentlyRunning(int taskId);

    public boolean isQueued(int taskId);

    @NotNull
    public List<Worker> getActiveWorkers();

    @NotNull
    public List<Task> getPendingTasks();

    @NotNull
    public Task runTask(@NotNull Runnable task) throws IllegalArgumentException;

    public void runTask(@NotNull Consumer<Task> task) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    public Task runTask(@NotNull KaiheilaRunnable task) throws IllegalArgumentException;

    @NotNull
    public Task runTaskAsynchronously(@NotNull Runnable task) throws IllegalArgumentException;

    public void runTaskAsynchronously(@NotNull Consumer<Task> task) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    public Task runTaskAsynchronously(@NotNull KaiheilaRunnable task) throws IllegalArgumentException;

    @NotNull
    public Task runTaskLater(@NotNull Runnable task, long delay) throws IllegalArgumentException;

    public void runTaskLater(@NotNull Consumer<Task> task, long delay) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    public Task runTaskLater(@NotNull KaiheilaRunnable task, long delay) throws IllegalArgumentException;

    @NotNull
    public Task runTaskLaterAsynchronously(@NotNull Runnable task, long delay) throws IllegalArgumentException;

    public void runTaskLaterAsynchronously(@NotNull Consumer<Task> task, long delay) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    public Task runTaskLaterAsynchronously(@NotNull KaiheilaRunnable task, long delay) throws IllegalArgumentException;

    @NotNull
    public Task runTaskTimer(@NotNull Runnable task, long delay, long period) throws IllegalArgumentException;

    public void runTaskTimer(@NotNull Consumer<Task> task, long delay, long period) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    public Task runTaskTimer(@NotNull KaiheilaRunnable task, long delay, long period) throws IllegalArgumentException;

    @NotNull
    public Task runTaskTimerAsynchronously(@NotNull Runnable task, long delay, long period) throws IllegalArgumentException;

    public void runTaskTimerAsynchronously(@NotNull Consumer<Task> task, long delay, long period) throws IllegalArgumentException;

    @Deprecated
    @NotNull
    public Task runTaskTimerAsynchronously(@NotNull KaiheilaRunnable task, long delay, long period) throws IllegalArgumentException;
}
