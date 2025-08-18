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

import java.util.List;

/**
 * Represents a measured time span in a profiling session.
 * <p>
 * A {@code Span} corresponds to a named section of code whose execution time is measured.
 * Spans can be nested, forming a hierarchical tree of profiling data.
 * </p>
 *
 * <h2>Lifecycle</h2>
 * <ul>
 *     <li>A {@code Span} is typically obtained via {@link Profiler#span(String)} or
 *     {@link Profiler#open(String)}.</li>
 *     <li>It starts timing when opened and stops when {@link #close()} is called.</li>
 *     <li>Spans can contain zero or more child spans, accessible via {@link #children()}.</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <blockquote><pre>
 * try (Span span = profiler.open("DatabaseQuery")) {
 *     executeDatabaseQuery();
 * }
 * System.out.println("Duration (ns): " + span.durationNs());
 * </pre></blockquote>
 *
 * <h2>Thread Safety</h2>
 * Implementations should ensure thread safety if spans can be accessed from multiple threads.
 *
 * @see Profiler
 * @see SpanBuilder
 * @see ProfilerNode
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public interface Span extends AutoCloseable {

    /**
     * Returns the duration of this span in nanoseconds.
     *
     * @return the measured duration in nanoseconds
     */
    long durationNs();

    /**
     * Returns the name of this span.
     *
     * @return the non-null name of the span
     */
    @NotNull
    String name();

    /**
     * Returns the child spans of this span.
     * <p>
     * The returned list may be empty if this span has no children.
     *
     * @return a non-null, possibly empty list of child spans
     */
    @NotNull
    List<? extends Span> children();

    /**
     * Closes this span and records its end time.
     * <p>
     * This method must be called to ensure accurate measurement.
     * Implementations should be idempotent — calling this method multiple times
     * should have no further effect after the first call.
     * </p>
     */
    @Override
    void close();
}
