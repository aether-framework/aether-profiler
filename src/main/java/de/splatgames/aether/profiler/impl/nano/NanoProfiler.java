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

import de.splatgames.aether.profiler.impl.AbstractProfiler;
import de.splatgames.aether.profiler.utils.ProfilerConfig;
import org.jetbrains.annotations.NotNull;

import java.util.function.LongSupplier;

/**
 * A concrete {@link AbstractProfiler} implementation that measures time using a caller‑provided
 * {@link LongSupplier} nano‑clock.
 * <p>
 * This variant is designed for environments where time must be sourced from an injectable,
 * deterministic or system‑specific provider (e.g. a high‑precision monotonic clock, a virtual
 * clock in tests, or a host API exposed by an engine). In contrast to the simple variant,
 * {@link de.splatgames.aether.profiler.impl.simple.SimpleProfiler SimpleProfiler}, this class never calls
 * {@link System#nanoTime()} directly. All time reads go through the supplied clock. </p>
 *
 * <p><strong>Thread Safety:</strong> Instances are intended to be used concurrently across multiple
 * threads. Span hierarchies are maintained per thread; aggregation occurs on frame end (if frames
 * are required) or on span root close (if frames are not required), depending on
 * {@link ProfilerConfig#requireFrames()}. </p>
 *
 * <p><strong>Typical usage:</strong></p>
 * <blockquote><pre>{@code
 * // Provide a nano-clock (e.g. your TimeUtils.timeSource)
 * LongSupplier clock = de.splatgames.software.shared.core.utils.TimeUtils.timeSource;
 *
 * NanoProfiler profiler = NanoProfiler.builder("XINI-Parse", clock)
 *     .requireFrames(false)
 *     .build();
 *
 * try (var parse = profiler.open("parse")) {
 *     try (var lex = profiler.open("lexer")) {
 *         // tokenize();
 *     }
 *     try (var ast = profiler.open("astBuild")) {
 *         // buildAst();
 *     }
 * }
 *
 * var snapshot = profiler.snapshot(true);
 * // Export via your exporter...
 * }</pre></blockquote>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class NanoProfiler extends AbstractProfiler {

    /**
     * Creates a new {@code NanoProfiler} that reads time exclusively from the provided nano‑clock.
     * <p>
     * The {@code config} instance is copied internally; subsequent external changes to the provided
     * {@link ProfilerConfig} will not affect this profiler. </p>
     *
     * @param name   the profiler name used in snapshots and diagnostics; must not be {@code null}
     * @param config the configuration; must not be {@code null}
     * @param clock  the nano‑time supplier used for all timing; must not be {@code null}
     */
    public NanoProfiler(@NotNull final String name,
                        @NotNull final ProfilerConfig config,
                        @NotNull final LongSupplier clock) {
        super(name, config, clock);
    }

    /**
     * Returns a builder for {@link NanoProfiler} using the given nano‑clock.
     * <p>
     * The builder exposes toggles such as {@link ProfilerConfig#requireFrames()} and
     * child limits. Call {@code build()} to create an immutable profiler instance. </p>
     *
     * @param name  the profiler name; must not be {@code null}
     * @param clock the nano‑time supplier; must not be {@code null}
     * @return a new {@code NanoProfilerBuilder} preconfigured with the given name and clock
     */
    @NotNull
    public static NanoProfilerBuilder builder(@NotNull final String name,
                                              @NotNull final LongSupplier clock) {
        return new NanoProfilerBuilder(name, clock);
    }
}
