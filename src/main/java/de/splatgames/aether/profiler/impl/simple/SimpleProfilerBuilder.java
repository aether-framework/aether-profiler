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

package de.splatgames.aether.profiler.impl.simple;

import de.splatgames.aether.profiler.utils.ProfilerConfig;
import org.jetbrains.annotations.NotNull;

/**
 * A builder for creating instances of {@link SimpleProfiler}.
 * <p>
 * This builder provides a fluent API to configure and construct a {@code SimpleProfiler}.
 * Configuration options include frame recording, CPU time measurement, and limiting
 * the number of child nodes stored per profiler node. </p>
 *
 * <p>Instances of this builder are not thread-safe and should be used from a single thread
 * during configuration. Once {@link #build()} is invoked, the produced
 * {@link SimpleProfiler} instance is immutable.</p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class SimpleProfilerBuilder {

    /**
     * The profiler's name.
     */
    private final String name;

    /**
     * The configuration object used to customize the profiler.
     */
    private final ProfilerConfig cfg = new ProfilerConfig();

    /**
     * Creates a new {@code SimpleProfilerBuilder} bound to a given name.
     *
     * @param name the profiler's name; must not be {@code null}
     */
    public SimpleProfilerBuilder(@NotNull final String name) {
        this.name = name;
    }

    /**
     * Enables or disables the collection of stack frames for spans.
     * <p>
     * When enabled, the profiler will record the stack trace for each opened span.
     * This can be useful for debugging but may introduce performance overhead. </p>
     *
     * @param v {@code true} to require frame collection; {@code false} otherwise
     * @return this builder instance for method chaining
     */
    @NotNull
    public SimpleProfilerBuilder requireFrames(final boolean v) {
        cfg.requireFrames(v);
        return this;
    }

    /**
     * Enables or disables CPU time measurement for spans.
     * <p>
     * This option uses thread-specific CPU time measurement when supported by the JVM.
     * It may have a small performance cost, but it provides more accurate CPU-bound metrics. </p>
     *
     * @param v {@code true} to enable CPU time measurement; {@code false} otherwise
     * @return this builder instance for method chaining
     */
    @NotNull
    public SimpleProfilerBuilder enableCpuTime(final boolean v) {
        cfg.enableCpuTime(v);
        return this;
    }

    /**
     * Sets the maximum number of children per profiler node.
     * <p>
     * Once the limit is reached, additional children will not be recorded to reduce
     * memory consumption. </p>
     *
     * @param n the maximum number of children per node
     * @return this builder instance for method chaining
     */
    @NotNull
    public SimpleProfilerBuilder maxChildrenPerNode(final int n) {
        cfg.maxChildren(n);
        return this;
    }

    /**
     * Builds a new {@link SimpleProfiler} instance with the current configuration.
     * <p>
     * The returned instance is immutable and can be used across threads for profiling. </p>
     *
     * @return a new configured {@code SimpleProfiler}
     */
    @NotNull
    public SimpleProfiler build() {
        return new SimpleProfiler(name, cfg.copy());
    }
}
