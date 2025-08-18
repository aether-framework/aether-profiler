package de.splatgames.aether.profiler.impl.noop;

import de.splatgames.aether.profiler.Span;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@link Span} implementation that performs no operations.
 * <p>
 * This span always reports a duration of {@code 0}, a constant name,
 * no children, and performs no action when closed.
 * </p>
 *
 * @author Erik Pförtner
 * @since 1.1.0
 */
final class NoOpSpan implements Span {

    /**
     * Always returns {@code 0}, since no duration is measured.
     *
     * @return {@code 0}
     */
    @Override
    public long durationNs() {
        return 0;
    }

    /**
     * Returns the constant name {@code "NOOP"}.
     *
     * @return {@code "NOOP"}
     */
    @Override
    public @NotNull String name() {
        return "NOOP";
    }

    /**
     * Always returns an empty list, as this span has no children.
     *
     * @return an empty list
     */
    @Override
    public @NotNull List<? extends Span> children() {
        return List.of();
    }

    /**
     * Does nothing.
     */
    @Override
    public void close() {
        // no-op
    }
}
