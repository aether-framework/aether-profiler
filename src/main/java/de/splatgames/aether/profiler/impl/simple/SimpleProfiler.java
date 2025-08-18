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

import de.splatgames.aether.profiler.impl.AbstractProfiler;
import de.splatgames.aether.profiler.utils.ProfilerConfig;
import org.jetbrains.annotations.NotNull;

/**
 * A lightweight implementation of {@link AbstractProfiler} using {@link System#nanoTime()}
 * as the time source.
 * <p>
 * This profiler is intended for scenarios where nanosecond precision is desired, but no custom
 * time source is required. It uses the default JVM-provided {@code nanoTime()} for measurement
 * and can be configured via a {@link ProfilerConfig}. </p>
 *
 * <p>Instances of this class are immutable after construction and should be created via
 * {@link #builder(String)} for a fluent configuration experience.</p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class SimpleProfiler extends AbstractProfiler {

    /**
     * Constructs a new {@code SimpleProfiler} instance.
     *
     * @param name   the profiler name; must not be {@code null}
     * @param config the profiler configuration; must not be {@code null}
     */
    public SimpleProfiler(@NotNull final String name, @NotNull final ProfilerConfig config) {
        super(name, config, System::nanoTime);
    }

    /**
     * Creates a new {@link SimpleProfilerBuilder} for fluent configuration.
     * <p>
     * The builder allows setting various parameters before constructing the final
     * {@code SimpleProfiler} instance.</p>
     *
     * @param name the profiler name; must not be {@code null}
     * @return a new {@code SimpleProfilerBuilder} bound to the given name
     */
    @NotNull
    public static SimpleProfilerBuilder builder(@NotNull final String name) {
        return new SimpleProfilerBuilder(name);
    }
}
