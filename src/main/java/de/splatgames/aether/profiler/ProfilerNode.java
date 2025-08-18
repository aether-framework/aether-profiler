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

import java.util.Map;

/**
 * Represents a single node in a profiler's hierarchical execution tree.
 * <p>
 * A {@code ProfilerNode} stores aggregated timing data for a specific named
 * code section (span) and maintains references to its child nodes, representing
 * nested spans within the profiled execution.
 * </p>
 *
 * <h2>Structure</h2>
 * Each node contains:
 * <ul>
 *     <li>{@link #name()} — the logical name of the profiled section</li>
 *     <li>{@link #totalNs()} — the total accumulated time in nanoseconds spent in this section</li>
 *     <li>{@link #count()} — the number of times this section was entered</li>
 *     <li>{@link #children()} — child sections (nested spans) keyed by name</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <blockquote><pre>
 * ProfilerNode root = profiler.snapshot(false).root();
 *
 * System.out.println("Root Section: " + root.name());
 * System.out.println("Total Time (ns): " + root.totalNs());
 * System.out.println("Average Time (ns): " + root.avgNs());
 *
 * for (ProfilerNode child : root.children().values()) {
 *     System.out.println("  Child: " + child.name() + " Avg: " + child.avgNs());
 * }
 * </pre></blockquote>
 *
 * @param name     the logical name of this profiler node, never {@code null}
 * @param totalNs  the total accumulated execution time for this node, in nanoseconds
 * @param count    the number of times this section was executed
 * @param children a mapping of child section names to their profiler nodes, never {@code null}
 * @author Erik Pförtner
 * @since 1.0.0
 */
public record ProfilerNode(
        @NotNull String name,
        long totalNs,
        long count,
        @NotNull Map<String, ProfilerNode> children
) {

    /**
     * Returns the average execution time in nanoseconds per invocation
     * of this profiler node.
     * <p>
     * If {@link #count()} is {@code 0}, this method returns {@code 0}.
     * </p>
     *
     * @return the average execution time in nanoseconds, or {@code 0} if this node has not been executed
     */
    public long avgNs() {
        return this.count == 0 ? 0 : this.totalNs / this.count;
    }
}
