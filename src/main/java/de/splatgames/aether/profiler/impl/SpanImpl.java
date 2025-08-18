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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Default {@link Span} implementation used internally by the Aether Profiler.
 * <p>
 * A {@code SpanImpl} represents a single timed section of code with an optional
 * category and arbitrary attributes. Spans can be nested to form a hierarchical
 * call tree, where each span may have a parent span and zero or more children.
 * </p>
 *
 * <p>Instances are thread-confined to the thread that created them and are not
 * intended to be shared across threads. Closing a span marks its end timestamp
 * and pops it from the current profiler stack.</p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class SpanImpl implements Span {

    /**
     * Atomic handle for the {@link #closed} field to avoid double-closing spans.
     */
    private static final VarHandle CLOSED;

    static {
        try {
            CLOSED = MethodHandles.lookup().findVarHandle(SpanImpl.class, "closed", boolean.class);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final @NotNull AbstractProfiler p;
    private final @NotNull String name;
    private final @Nullable String category;
    private final @NotNull Map<String, String> attrs;
    private final @Nullable SpanImpl parent;
    private final @NotNull List<SpanImpl> children = new ArrayList<>(4);
    private final long startNs;
    private long endNs;
    private volatile boolean closed;

    /**
     * Creates a new {@code SpanImpl} instance.
     *
     * @param p        the owning profiler; must not be {@code null}
     * @param name     the span name; must not be {@code null}
     * @param category optional category; may be {@code null}
     * @param attrs    immutable map of attributes; must not be {@code null}
     * @param parent   optional parent span; may be {@code null}
     */
    private SpanImpl(
            @NotNull final AbstractProfiler p,
            @NotNull final String name,
            @Nullable final String category,
            @NotNull final Map<String, String> attrs,
            @Nullable final SpanImpl parent
    ) {
        this.p = p;
        this.name = name;
        this.category = category;
        this.attrs = attrs;
        this.parent = parent;
        this.startNs = p.clock.getAsLong();
    }

    /**
     * Starts and registers a new {@code SpanImpl} in the given profiler.
     *
     * @param p        the profiler; must not be {@code null}
     * @param name     the span name; must not be {@code null}
     * @param category optional category; may be {@code null}
     * @param attrs    optional map of attributes; may be {@code null}
     * @return the newly created span
     */
    @NotNull
    public static SpanImpl start(
            @NotNull final AbstractProfiler p,
            @NotNull final String name,
            @Nullable final String category,
            @Nullable final Map<String, String> attrs
    ) {
        final SpanImpl parent = p.current();
        final Map<String, String> safeAttrs = (attrs == null) ? Map.of() : Map.copyOf(attrs);
        final SpanImpl s = new SpanImpl(p, name, category, safeAttrs, parent);
        if (parent != null) {
            parent.children.add(s);
        }
        p.push(s);
        return s;
    }

    /**
     * Returns the parent span, if any.
     *
     * @return the parent span or {@code null} if this is a root span
     */
    @Nullable
    public SpanImpl parent() {
        return this.parent;
    }

    /**
     * Marks this span as closed, recording the end timestamp and
     * removing it from the profiler's stack. If already closed, does nothing.
     */
    @Override
    public void close() {
        if ((boolean) CLOSED.getAndSet(this, true)) {
            return;
        }
        this.endNs = this.p.clock.getAsLong();
        this.p.pop(this);
    }

    /**
     * Returns the span duration in nanoseconds. If the span is still open,
     * the duration is measured up to the current time.
     *
     * @return duration in nanoseconds
     */
    @Override
    public long durationNs() {
        return ((this.endNs == 0) ? this.p.clock.getAsLong() : this.endNs) - this.startNs;
    }

    /**
     * Returns the span name.
     *
     * @return the span name; never {@code null}
     */
    @Override
    @NotNull
    public String name() {
        return this.name;
    }

    /**
     * Returns an unmodifiable view of this span's children.
     *
     * @return immutable list of children; never {@code null}
     */
    @Override
    @NotNull
    public List<SpanImpl> children() {
        return Collections.unmodifiableList(this.children);
    }
}
