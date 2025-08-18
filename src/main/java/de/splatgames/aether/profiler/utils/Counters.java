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

package de.splatgames.aether.profiler.utils;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

/**
 * Thread-safe counter utility that aggregates named numeric values.
 * <p>
 * This class is designed for low-contention, high-throughput counting operations
 * using {@link LongAdder} instances stored in a concurrent map. Counters are
 * created lazily on first use and can be incremented or added to by name.
 * </p>
 *
 * <p>Typical usage:</p>
 * <blockquote><pre>
 * Counters counters = new Counters();
 * counters.inc("requests");
 * counters.add("bytes", 1024);
 * Map&lt;String, Long&gt; snapshot = counters.snapshot();
 * </pre></blockquote>
 *
 * <p>
 * The {@link #snapshot()} method returns a consistent mapping of counter values
 * at the time of invocation, but is not atomic with respect to concurrent updates.
 * Values in the snapshot will not change after creation.
 * </p>
 *
 * <p>This class is safe for concurrent use by multiple threads.</p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class Counters {

    /**
     * Internal storage mapping counter names to their {@link LongAdder} accumulators.
     */
    private final Map<String, LongAdder> map = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@code Counters} instance with an empty counter map.
     * <p>
     * This initializes the internal storage for counters, ready for use.
     * </p>
     */
    public Counters() {
    }

    /**
     * Increments the counter associated with the given name by {@code 1}.
     * <p>
     * If the counter does not yet exist, it will be created automatically.
     * </p>
     *
     * @param name the counter name; must not be {@code null}
     */
    public void inc(@NotNull final String name) {
        this.map.computeIfAbsent(name, k -> new LongAdder()).increment();
    }

    /**
     * Adds the specified value to the counter associated with the given name.
     * <p>
     * If the counter does not yet exist, it will be created automatically.
     * </p>
     *
     * @param name the counter name; must not be {@code null}
     * @param v    the value to add (may be negative to decrement)
     */
    public void add(@NotNull final String name, final long v) {
        this.map.computeIfAbsent(name, k -> new LongAdder()).add(v);
    }

    /**
     * Returns a snapshot of all counters and their current summed values.
     * <p>
     * The returned map is a new {@link LinkedHashMap} containing a copy
     * of the counter values at the time of invocation. This snapshot will
     * not reflect future changes to the counters.
     * </p>
     *
     * @return an immutable snapshot of counter names to their values; never {@code null}
     */
    @NotNull
    public Map<String, Long> snapshot() {
        final Map<String, Long> out = new LinkedHashMap<>();
        this.map.forEach((k, v) -> out.put(k, v.sum()));
        return out;
    }
}
