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

import de.splatgames.aether.profiler.Span;
import de.splatgames.aether.profiler.SpanBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Default {@link SpanBuilder} implementation used by the Aether Profiler.
 * <p>
 * This builder collects optional span metadata such as a category and arbitrary
 * attributes (key/value pairs) and opens a new {@link Span} on demand via
 * {@link #open()}. Attributes preserve insertion order. </p>
 *
 * <p>Instances are not thread-safe and are intended for single-threaded use during
 * span construction.</p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class SpanBuilderImpl implements SpanBuilder {

    private final @NotNull AbstractProfiler p;
    private final @NotNull String name;
    private @Nullable String category;
    private @Nullable Map<String, String> attrs;

    /**
     * Creates a new span builder bound to the given profiler and section name.
     *
     * @param p    the owning profiler; must not be {@code null}
     * @param name the span/section name; must not be {@code null}
     */
    public SpanBuilderImpl(@NotNull final AbstractProfiler p, @NotNull final String name) {
        this.p = p;
        this.name = name;
    }

    /**
     * Sets an optional category for the span.
     *
     * @param category the category or {@code null}
     * @return this builder
     */
    @NotNull
    @Override
    public SpanBuilder category(@Nullable final String category) {
        this.category = category;
        return this;
    }

    /**
     * Adds an attribute to the span.
     * <p>
     * Values are stored via {@link String#valueOf(Object)} to avoid null handling
     * downstream and to minimize allocations. </p>
     *
     * @param k the attribute key; must not be {@code null}
     * @param v the attribute value (converted to String), may be {@code null}
     * @return this builder
     */
    @NotNull
    @Override
    public SpanBuilder attr(@NotNull final String k, @Nullable final Object v) {
        if (this.attrs == null) {
            this.attrs = new LinkedHashMap<>();
        }
        this.attrs.put(k, String.valueOf(v));
        return this;
    }

    /**
     * Opens a new {@link Span} using the configured properties.
     *
     * @return the opened span
     */
    @NotNull
    @Override
    public Span open() {
        return SpanImpl.start(this.p, this.name, this.category, this.attrs);
    }
}
