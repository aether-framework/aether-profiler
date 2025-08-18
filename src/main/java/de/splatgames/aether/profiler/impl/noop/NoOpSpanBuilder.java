package de.splatgames.aether.profiler.impl.noop;

import de.splatgames.aether.profiler.Span;
import de.splatgames.aether.profiler.SpanBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link SpanBuilder} implementation that performs no operations.
 * <p>
 * All configuration methods return this builder without recording any state.
 * {@link #open()} returns an inert {@link Span} that reports a zero duration,
 * a constant name, no children, and performs no action on {@link Span#close()}.
 * </p>
 *
 * <p>This builder is used when profiling is disabled, allowing client code to
 * keep instrumentation calls without incurring runtime overhead.</p>
 *
 * @author Erik Pförtner
 * @since 1.1.0
 */
final class NoOpSpanBuilder implements SpanBuilder {
    static final NoOpSpanBuilder INSTANCE = new NoOpSpanBuilder();

    private static final NoOpSpan NOOP_SPAN = new NoOpSpan();

    /**
     * Singleton constructor.
     */
    private NoOpSpanBuilder() { }

    /**
     * Ignores the provided category and returns this builder.
     *
     * @param category the (ignored) category, may be {@code null}
     * @return this builder
     */
    @Override
    public @NotNull SpanBuilder category(@Nullable final String category) {
        return this;
    }

    /**
     * Ignores the provided attribute and returns this builder.
     *
     * @param key   the (ignored) attribute key; must not be {@code null}
     * @param value the (ignored) attribute value; may be {@code null}
     * @return this builder
     */
    @Override
    public @NotNull SpanBuilder attr(@NotNull final String key, @Nullable final Object value) {
        return this;
    }

    /**
     * Returns a no-op {@link Span}.
     * <p>
     * The returned span measures no time, has no children, and its
     * {@link Span#close()} method does nothing.
     * </p>
     *
     * @return an inert span instance
     */
    @Override
    public @NotNull Span open() {
        return NOOP_SPAN;
    }
}
