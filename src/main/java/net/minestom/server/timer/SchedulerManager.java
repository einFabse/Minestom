package net.minestom.server.timer;

import org.jctools.queues.MpmcUnboundedXaddArrayQueue;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public final class SchedulerManager implements Scheduler {
    private final Scheduler scheduler = Scheduler.newScheduler();
    private final List<Runnable> parallelTasks = new ArrayList<>();
    private final MpmcUnboundedXaddArrayQueue<Runnable> shutdownTasks = new MpmcUnboundedXaddArrayQueue<>(1024);

    @Override
    public void process() {
        this.scheduler.process();
    }

    @Override
    public void processTick() {
        this.scheduler.processTick();
    }

    @Override
    public @NotNull Task submit(@NotNull Supplier<TaskSchedule> task,
                                @NotNull ExecutionType executionType) {
        return scheduler.submit(task, executionType);
    }

    @Override
    public @NotNull Task submitAfter(@NotNull TaskSchedule schedule,
                                     @NotNull Supplier<TaskSchedule> task,
                                     @NotNull ExecutionType executionType) {
        return scheduler.submitAfter(schedule, task, executionType);
    }

    @Override
    public @NotNull Collection<@NotNull Task> scheduledTasks() {
        return scheduler.scheduledTasks();
    }

    @ApiStatus.Experimental
    public synchronized void prepareParallelTask(@NotNull Runnable task) {
        this.parallelTasks.add(task);
    }

    @ApiStatus.Experimental
    public synchronized void processParallelTasks() {
        this.parallelTasks.parallelStream().forEach(Runnable::run);
        this.parallelTasks.clear();
    }

    public void shutdown() {
        this.shutdownTasks.drain(Runnable::run);
    }

    public void buildShutdownTask(@NotNull Runnable runnable) {
        this.shutdownTasks.relaxedOffer(runnable);
    }
}
