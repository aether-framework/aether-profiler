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

import de.splatgames.aether.profiler.Frame;
import org.jetbrains.annotations.NotNull;

/**
 * Default {@link Frame} implementation for the Aether Profiler.
 * <p>
 * A frame represents a profiling period that can encapsulate multiple root-level spans.
 * When the frame is closed, any accumulated root span for the current thread
 * is aggregated into the {@link AbstractProfiler}'s statistics.
 * </p>
 *
 * <p>Instances of this class are created via
 * {@link AbstractProfiler#frameBegin()} and are meant to be used in a
 * try-with-resources block to ensure proper closing.</p>
 *
 * <pre>{@code
 * try (Frame frame = profiler.frameBegin()) {
 *     // Execute profiled code here
 * }
 * }</pre>
 *
 * <p>This class is thread-bound — each thread may have its own active frame.</p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class FrameImpl implements Frame {

    /**
     * The owning profiler instance.
     */
    private final @NotNull AbstractProfiler p;

    /**
     * Start time of the frame in nanoseconds, captured using {@link AbstractProfiler#clock}.
     */
    private final long startNs;

    /**
     * Indicates whether the frame has already been closed.
     */
    private boolean closed;

    /**
     * Creates a new {@code FrameImpl} bound to the given profiler.
     *
     * @param p the owning profiler; must not be {@code null}
     */
    public FrameImpl(@NotNull final AbstractProfiler p) {
        this.p = p;
        this.startNs = p.clock.getAsLong();
    }

    /**
     * Closes this frame, finalizing the profiling period.
     * <p>
     * If a root-level {@link SpanImpl} exists for the current thread, it is removed
     * from {@link AbstractProfiler#rootsPerThread} and aggregated into the profiler's
     * statistics. This method is idempotent — calling it more than once has no effect.
     * </p>
     */
    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        final long tid = Thread.currentThread().getId();
        final SpanImpl root = this.p.rootsPerThread.remove(tid);
        if (root != null) {
            this.p.aggregate(root);
        }
    }

    /**
     * Returns the elapsed time since this frame was started, in nanoseconds.
     * <p>The elapsed time is calculated relative to the profiler's clock.</p>
     *
     * @return the elapsed time in nanoseconds
     */
    @Override
    public long elapsedNs() {
        return this.p.clock.getAsLong() - this.startNs;
    }
}
