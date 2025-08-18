package de.splatgames.aether.profiler.impl.noop;

import de.splatgames.aether.profiler.Frame;

/**
 * A {@link Frame} implementation that performs no operations.
 * <p>
 * This class acts as a placeholder or default {@code Frame} when profiling
 * is disabled or not required. All calls return neutral values and have
 * no side effects.
 * </p>
 *
 * <p>Usage of this class avoids {@code null} checks while maintaining a
 * lightweight and safe no-op implementation.</p>
 *
 * @author Erik Pförtner
 * @since 1.1.0
 */
final class NoOpFrame implements Frame {
    static final NoOpFrame INSTANCE = new NoOpFrame();

    /**
     * Private constructor to enforce singleton instance via {@link #INSTANCE}.
     */
    private NoOpFrame() {
        // Private constructor to prevent instantiation
    }

    /**
     * Always returns {@code 0}, since no time is measured in this no-op implementation.
     *
     * @return {@code 0}
     */
    @Override
    public long elapsedNs() {
        return 0;
    }

    /**
     * Does nothing. This method is intentionally left blank to ensure
     * compatibility with the {@link Frame} contract while avoiding
     * any side effects.
     */
    @Override
    public void close() {
        // no-op
    }
}
