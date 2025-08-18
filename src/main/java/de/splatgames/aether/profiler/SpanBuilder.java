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
import org.jetbrains.annotations.Nullable;

/**
 * A builder for constructing and configuring {@link Span} instances before starting them.
 * <p>
 * A {@code SpanBuilder} is typically obtained via {@link Profiler#span(String)}.
 * It allows setting optional metadata such as a category or custom attributes
 * before the span is opened with {@link #open()}.
 * </p>
 *
 * <h2>Typical Usage</h2>
 * <blockquote><pre>
 * try (Span span = profiler.span("DatabaseQuery")
 *         .category("Database")
 *         .attr("QueryType", "SELECT")
 *         .open()) {
 *     executeQuery();
 * }
 * </pre></blockquote>
 *
 * <h2>Thread Safety</h2>
 * Implementations are not required to be thread-safe. Builders are intended for
 * single-threaded use during span construction.
 *
 * @see Profiler
 * @see Span
 * @see ProfilerNode
 * @see ProfilerSnapshot
 *
 * author Erik Pförtner
 * @since 1.0.0
 */
public interface SpanBuilder {

    /**
     * Sets the category of the span.
     * <p>
     * Categories are optional labels used to group spans logically, e.g. {@code "Database"} or {@code "Rendering"}.
     * A {@code null} value indicates that no category is assigned.
     * </p>
     *
     * @param category the category name, or {@code null} if none
     * @return this builder instance for method chaining
     */
    @NotNull
    SpanBuilder category(@Nullable final String category);

    /**
     * Adds a custom attribute to the span.
     * <p>
     * Attributes are key-value pairs that store additional metadata about a span.
     * Implementations may ignore {@code null} values or store them explicitly, depending on their design.
     * </p>
     *
     * @param key   the non-null attribute key
     * @param value the attribute value, may be {@code null}
     * @return this builder instance for method chaining
     */
    @NotNull
    SpanBuilder attr(@NotNull final String key, @Nullable final Object value);

    /**
     * Opens the span for timing.
     * <p>
     * This method starts the measurement for the span and returns a {@link Span} instance.
     * The caller is responsible for closing the span via {@link Span#close()} to ensure
     * accurate duration tracking.
     * </p>
     *
     * @return the opened span, never {@code null}
     */
    @NotNull
    Span open();
}
