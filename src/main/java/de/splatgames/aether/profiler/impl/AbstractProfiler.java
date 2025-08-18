/*
 * Copyright (c) 2025 Splatgames.de Software and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.splatgames.aether.profiler.impl;

import de.splatgames.aether.profiler.CheckedSupplier;
import de.splatgames.aether.profiler.Frame;
import de.splatgames.aether.profiler.Profiler;
import de.splatgames.aether.profiler.ProfilerSnapshot;
import de.splatgames.aether.profiler.Span;
import de.splatgames.aether.profiler.SpanBuilder;
import de.splatgames.aether.profiler.utils.ProfilerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

/**
 * Base implementation of the {@link Profiler} interface.
 * <p>
 * This class provides the common logic for span management, aggregation, and snapshot creation.
 * It maintains a per-thread {@link SpanImpl} stack, handles root span tracking,
 * and delegates snapshot generation to the {@link Aggregator}. </p>
 *
 * <p>Concrete profiler implementations, such as
 * {@code SimpleProfiler} or {@code NanoProfiler}, provide the specific clock source
 * and configuration to this abstract base class.</p>
 *
 * <p>This class is thread-safe for concurrent usage across multiple threads,
 * as it uses {@link ThreadLocal} for per-thread state and a {@link ConcurrentHashMap}
 * for storing root spans.</p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public abstract class AbstractProfiler implements Profiler {

    /**
     * The current span per thread.
     */
    public final ThreadLocal<SpanImpl> current = ThreadLocal.withInitial(() -> null);

    /**
     * The root span for each thread, mapped by thread ID.
     */
    public final Map<Long, SpanImpl> rootsPerThread = new ConcurrentHashMap<>();

    /**
     * Aggregates completed spans into snapshots.
     */
    public final Aggregator aggregator = new Aggregator();

    /**
     * Logger for internal debugging and diagnostic output.
     */
    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    /**
     * Profiler name.
     */
    protected final String name;

    /**
     * Profiler configuration.
     */
    protected final ProfilerConfig config;

    /**
     * Clock source for measuring time in nanoseconds or CPU time.
     */
    protected final LongSupplier clock;

    /**
     * Creates a new {@code AbstractProfiler} instance.
     *
     * @param name   the profiler's name; must not be {@code null}
     * @param config the profiler's configuration; must not be {@code null}
     * @param clock  the time source for measuring durations; must not be {@code null}
     */
    protected AbstractProfiler(
            @NotNull final String name,
            @NotNull final ProfilerConfig config,
            @NotNull final LongSupplier clock
    ) {
        this.name = name;
        this.config = config;
        this.clock = clock;
    }

    /**
     * Creates a new {@link SpanBuilder} for the given section name.
     *
     * @param sectionName the section name; must not be {@code null}
     * @return a new span builder for the given section name
     */
    @Override
    @NotNull
    public final SpanBuilder span(@NotNull final String sectionName) {
        return new SpanBuilderImpl(this, sectionName);
    }

    /**
     * Executes a given {@link CheckedSupplier} inside a newly opened span.
     * <p>
     * The span is automatically closed after execution, even if an exception occurs.
     * Any checked exceptions are wrapped into a {@link RuntimeException}. </p>
     *
     * @param sectionName the section name; must not be {@code null}
     * @param body        the operation to execute inside the span; must not be {@code null}
     * @param <T>         the result type
     * @return the result returned by {@code body}
     * @throws RuntimeException if {@code body} throws an exception
     */
    @Override
    @NotNull
    public final <T> T inSpan(
            @NotNull final String sectionName,
            @NotNull final CheckedSupplier<T> body
    ) {
        try (Span s = span(sectionName).open()) {
            return body.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Starts a new profiling frame.
     * <p>
     * A frame is a high-level container for multiple spans, typically representing
     * one iteration of a render loop or request handling cycle. </p>
     *
     * @return a new {@link Frame} instance
     */
    @Override
    @NotNull
    public final Frame frameBegin() {
        return new FrameImpl(this);
    }

    /**
     * Creates a snapshot of the profiler's collected data.
     *
     * @param clear if {@code true}, clears the collected data after snapshot creation
     * @return a profiler snapshot containing aggregated profiling data
     */
    @Override
    @NotNull
    public final ProfilerSnapshot snapshot(final boolean clear) {
        return this.aggregator.snapshot(clear, this.name);
    }

    /**
     * Returns the current active span for the calling thread, or {@code null} if none.
     *
     * @return the current span or {@code null}
     */
    @Nullable
    public SpanImpl current() {
        return this.current.get();
    }

    /**
     * Pushes a new span onto the current thread's stack.
     * <p>
     * If the span has no parent, it is recorded as the thread's root span. </p>
     *
     * @param s the span to push; must not be {@code null}
     */
    public void push(@NotNull final SpanImpl s) {
        final SpanImpl parent = current();
        if (parent == null) {
            this.rootsPerThread.put(Thread.currentThread().getId(), s);
        }
        this.current.set(s);
    }

    /**
     * Pops the given span from the current thread's stack.
     * <p>
     * If the popped span has no parent and frame collection is disabled,
     * the span is immediately aggregated. </p>
     *
     * @param s the span to pop; must not be {@code null}
     */
    public void pop(@NotNull final SpanImpl s) {
        final SpanImpl parent = s.parent();
        this.current.set(parent);
        if (parent == null && !this.config.requireFrames()) {
            aggregate(s);
        }
    }

    /**
     * Aggregates the given root span into the profiler's data set.
     *
     * @param root the root span; must not be {@code null}
     */
    public void aggregate(@NotNull final SpanImpl root) {
        this.aggregator.add(root);
    }
}
