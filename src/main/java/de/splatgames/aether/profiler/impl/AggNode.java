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

import de.splatgames.aether.profiler.ProfilerNode;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

/**
 * Internal aggregation node used by the profiler to accumulate timing statistics in a tree.
 * <p>
 * Each {@code AggNode} represents a section name in the hierarchical profile. It stores the
 * inclusive total duration in nanoseconds and the number of observations (spans), and maintains
 * a map of child nodes keyed by child section name. Insertion order is preserved to keep
 * snapshot output stable. </p>
 *
 * <p>This type is an internal detail of the implementation layer and is not part of the public API.
 * Use {@link de.splatgames.aether.profiler.ProfilerSnapshot ProfilerSnapshot} and {@link ProfilerNode} to consume
 * aggregated results.</p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
final class AggNode {

    private final String name;
    private final LongAdder totalNs = new LongAdder();
    private final LongAdder count = new LongAdder();
    private final Map<String, AggNode> children = new LinkedHashMap<>();

    /**
     * Creates a new aggregation node with the given name.
     *
     * @param name the node name; must not be {@code null}
     */
    AggNode(@NotNull final String name) {
        this.name = name;
    }

    /**
     * Returns the child node with the given name, creating it if necessary.
     *
     * @param n the child node name; must not be {@code null}
     * @return the existing or newly created child node
     */
    @NotNull
    AggNode child(@NotNull final String n) {
        return children.computeIfAbsent(n, AggNode::new);
    }

    /**
     * Resets all accumulated values on this node and its descendants.
     * <p>
     * After calling this method, {@code totalNs} and {@code count} are zeroed, and the same
     * is applied recursively to all children. </p>
     */
    void clear() {
        this.totalNs.reset();
        this.count.reset();
        this.children.values().forEach(AggNode::clear);
    }

    /**
     * Adds the given duration in nanoseconds to this node's total time.
     *
     * @param ns the duration to add, in nanoseconds
     */
    void timeAdd(final long ns) {
        this.totalNs.add(ns);
    }

    /**
     * Increments this node's observation count by the specified value.
     *
     * @param c the increment amount (typically {@code 1})
     */
    void countAdd(final long c) {
        this.count.add(c);
    }

    /**
     * Converts this aggregation node (and its subtree) into a public {@link ProfilerNode}.
     * <p>
     * The returned node contains inclusive totals and counts; consumers may derive self-time
     * by subtracting the sum of child totals from the node's total if desired. </p>
     *
     * @return an immutable snapshot view of this node and its children
     */
    @NotNull
    ProfilerNode toSnapshot() {
        final Map<String, ProfilerNode> kids = new LinkedHashMap<>();
        for (final var e : this.children.entrySet()) {
            kids.put(e.getKey(), e.getValue().toSnapshot());
        }
        return new ProfilerNode(this.name, this.totalNs.sum(), this.count.sum(), kids);
    }
}
