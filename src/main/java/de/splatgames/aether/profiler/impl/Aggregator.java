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

import de.splatgames.aether.profiler.ProfilerSnapshot;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Aggregates completed span trees into a hierarchical snapshot model.
 * <p>
 * The {@code Aggregator} maintains a single root {@link AggNode} that represents
 * a synthetic top-level container named {@code "root"}. Each added root span is merged
 * into this aggregation tree, accumulating inclusive durations and counts per section
 * name while preserving insertion order of children for stable output. </p>
 *
 * <p>This class is an internal implementation detail. Consumers should use
 * {@link de.splatgames.aether.profiler.Profiler Profiler} to collect data and
 * {@link #snapshot(boolean, String)} to obtain a public {@link ProfilerSnapshot}. </p>
 *
 * @author Erik Pförtнер
 * @since 1.0.0
 */
public final class Aggregator {

    private final AggNode root = new AggNode("root");

    /**
     * Constructs a new {@code Aggregator} with an empty root node.
     * <p>
     * The root node is initialized with the name {@code "root"} and serves as the
     * top-level aggregation point for all added spans. </p>
     */
    public Aggregator() {
    }

    /**
     * Adds a completed root {@link SpanImpl} to the aggregation tree.
     * <p>
     * The synthetic {@code root} node is updated (count and time) and the full span
     * subtree is merged recursively under it. </p>
     *
     * @param rootSpan the completed root span to aggregate; must not be {@code null}
     */
    void add(@NotNull final SpanImpl rootSpan) {
        root.countAdd(1);
        root.timeAdd(rootSpan.durationNs());
        merge(root, rootSpan);
    }

    /**
     * Recursively merges a span into the given aggregation node.
     * <p>
     * For each span, a child node keyed by the span's {@link SpanImpl#name() name}
     * is obtained or created, then its count and total time are updated, followed by
     * merging all of its children. </p>
     *
     * @param into the parent aggregation node; must not be {@code null}
     * @param s    the span to merge; must not be {@code null}
     */
    private void merge(@NotNull final AggNode into, @NotNull final SpanImpl s) {
        final AggNode here = into.child(s.name());
        here.countAdd(1);
        here.timeAdd(s.durationNs());
        for (final var child : s.children()) {
            merge(here, child);
        }
    }

    /**
     * Produces a point-in-time {@link ProfilerSnapshot} of the aggregated data.
     * <p>
     * If {@code clear} is {@code true}, the aggregation tree is reset after the snapshot
     * is created. The snapshot uses the current {@link Instant} as its timestamp. </p>
     *
     * @param clear        whether to clear accumulated data after snapshot creation
     * @param profilerName the profiler name to embed in the snapshot; must not be {@code null}
     * @return a new immutable {@code ProfilerSnapshot} representing the current aggregates
     */
    @NotNull
    ProfilerSnapshot snapshot(final boolean clear, @NotNull final String profilerName) {
        final ProfilerSnapshot snap = new ProfilerSnapshot(profilerName, Instant.now(), this.root.toSnapshot());
        if (clear) {
            this.root.clear();
        }
        return snap;
    }
}
