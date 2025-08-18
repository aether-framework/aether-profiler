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

package de.splatgames.aether.profiler;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Immutable snapshot of a profiler's current state at a specific point in time.
 * <p>
 * A {@code ProfilerSnapshot} captures the profiling data from a {@link Profiler}
 * instance, including its name, the timestamp of when the snapshot was taken,
 * and the root {@link ProfilerNode} containing the hierarchical profiling data.
 * </p>
 *
 * <h2>Structure</h2>
 * Each snapshot contains:
 * <ul>
 *     <li>{@link #profiler()} — the name or identifier of the profiler instance</li>
 *     <li>{@link #at()} — the exact time when the snapshot was recorded</li>
 *     <li>{@link #root()} — the root node of the profiler's execution tree</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <blockquote><pre>
 * ProfilerSnapshot snapshot = profiler.snapshot(false);
 *
 * System.out.println("Profiler: " + snapshot.profiler());
 * System.out.println("Snapshot Time: " + snapshot.at());
 * System.out.println("Root Avg Time (ns): " + snapshot.root().avgNs());
 * </pre></blockquote>
 *
 * <h2>Thread Safety</h2>
 * This record is immutable and therefore inherently thread-safe.
 *
 * @param profiler the name or identifier of the profiler instance, never {@code null}
 * @param at       the timestamp when the snapshot was created, never {@code null}
 * @param root     the root {@link ProfilerNode} representing the profiling data, never {@code null}
 * @author Erik Pförtner
 * @see Profiler
 * @see ProfilerNode
 * @since 1.0.0
 */
public record ProfilerSnapshot(
        @NotNull String profiler,
        @NotNull Instant at,
        @NotNull ProfilerNode root
) {
}
