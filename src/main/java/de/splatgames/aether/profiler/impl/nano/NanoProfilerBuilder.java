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

package de.splatgames.aether.profiler.impl.nano;

import de.splatgames.aether.profiler.utils.ProfilerConfig;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongSupplier;

/**
 * Builder for {@link NanoProfiler} instances using a caller-provided {@link LongSupplier}
 * as nano-time source.
 * <p>
 * This builder configures behavioral aspects such as frame aggregation and maximum
 * children per node via a fluent API and creates an immutable {@link NanoProfiler}
 * on {@link #build() build()}. The supplied {@link ProfilerConfig} is copied at
 * build time, so later external changes to the builder's config do not affect
 * already created profilers. </p>
 *
 * <p><strong>Typical usage:</strong></p>
 * <blockquote><pre>{@code
 * LongSupplier clock = de.splatgames.software.shared.core.utils.TimeUtils.timeSource;
 *
 * NanoProfiler profiler = new NanoProfilerBuilder("XINI-Parse", clock)
 *     .requireFrames(false)
 *     .maxChildrenPerNode(256)
 *     .build();
 * }</pre></blockquote>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class NanoProfilerBuilder {
    private final String name;
    private final LongSupplier clock;
    private final ProfilerConfig cfg = new ProfilerConfig();

    /**
     * Creates a new builder for {@link NanoProfiler} using the provided nano-time supplier.
     * <p>
     * The {@code clock} will be used by the resulting profiler for all timing operations. </p>
     *
     * @param name  the profiler name; must not be {@code null}
     * @param clock the nano-time supplier; must not be {@code null}
     */
    public NanoProfilerBuilder(@NotNull final String name, @NotNull final LongSupplier clock) {
        this.name = name;
        this.clock = clock;
    }

    /**
     * Enables or disables frame-based aggregation.
     * <p>
     * If {@code true}, finished root spans are aggregated on frame end; otherwise immediately
     * when the root span closes. </p>
     *
     * @param v {@code true} to require frames; {@code false} to aggregate immediately
     * @return this builder
     */
    @NotNull
    public NanoProfilerBuilder requireFrames(final boolean v) {
        cfg.requireFrames(v);
        return this;
    }

    /**
     * Toggles CPU time collection (reserved for future use).
     *
     * @param v {@code true} to enable; {@code false} to disable
     * @return this builder
     */
    @NotNull
    public NanoProfilerBuilder enableCpuTime(final boolean v) {
        cfg.enableCpuTime(v);
        return this;
    }

    /**
     * Sets the maximum number of children kept per node in aggregation.
     *
     * @param n the maximum number of children
     * @return this builder
     */
    @NotNull
    public NanoProfilerBuilder maxChildrenPerNode(final int n) {
        cfg.maxChildren(n);
        return this;
    }

    /**
     * Builds a new {@link NanoProfiler} with the configured settings.
     * <p>
     * The internal {@link ProfilerConfig} is copied; subsequent builder changes do not affect
     * the returned profiler. </p>
     *
     * @return the constructed {@code NanoProfiler}
     */
    @NotNull
    public NanoProfiler build() {
        return new NanoProfiler(name, cfg.copy(), clock);
    }
}
