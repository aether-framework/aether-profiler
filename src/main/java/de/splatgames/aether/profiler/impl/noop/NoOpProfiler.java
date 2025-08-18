package de.splatgames.aether.profiler.impl.noop;

import de.splatgames.aether.profiler.CheckedSupplier;
import de.splatgames.aether.profiler.Frame;
import de.splatgames.aether.profiler.Profiler;
import de.splatgames.aether.profiler.ProfilerNode;
import de.splatgames.aether.profiler.ProfilerSnapshot;
import de.splatgames.aether.profiler.SpanBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;

/**
 * A {@link Profiler} implementation that performs no operations.
 * <p>
 * This profiler is intended for cases where profiling is disabled
 * or not required. All methods return neutral or placeholder values
 * while still adhering to the {@link Profiler} contract.
 * </p>
 *
 * <p>Typical use cases include:</p>
 * <ul>
 *   <li>Providing a safe default instance when profiling is not enabled.</li>
 *   <li>Avoiding {@code null} checks while maintaining compatibility with code
 *       that expects a {@link Profiler} instance.</li>
 *   <li>Reducing overhead in performance-critical scenarios where profiling
 *       would add unnecessary costs.</li>
 * </ul>
 *
 * <p>The singleton instance is accessible via {@link #INSTANCE}.</p>
 *
 * @since 1.1.0
 * @author Erik Pförtner
 */
public final class NoOpProfiler implements Profiler {
    public static final NoOpProfiler INSTANCE = new NoOpProfiler();

    private static final NoOpSpanBuilder NOOP_SPAN_BUILDER = NoOpSpanBuilder.INSTANCE;
    private static final NoOpFrame NOOP_FRAME = NoOpFrame.INSTANCE;

    /**
     * Private constructor to enforce singleton usage via {@link #INSTANCE}.
     */
    private NoOpProfiler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Always returns a {@link NoOpSpanBuilder}, which itself performs no operations.
     *
     * @param sectionName ignored section name
     * @return a no-op {@link SpanBuilder}
     */
    @Override
    public @NotNull SpanBuilder span(@NotNull final String sectionName) {
        return NOOP_SPAN_BUILDER;
    }

    /**
     * Executes the given supplier body without profiling overhead.
     * <p>Any exception thrown by the supplier is wrapped in a {@link RuntimeException}.</p>
     *
     * @param sectionName ignored section name
     * @param body        code to execute
     * @param <T>         return type
     * @return the result of the supplier
     */
    @Override
    public <T> @NotNull T inSpan(@NotNull final String sectionName, @NotNull final CheckedSupplier<T> body) {
        try {
            return body.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Always returns a {@link NoOpFrame}, which reports zero elapsed time.
     *
     * @return a no-op frame
     */
    @Override
    public @NotNull Frame frameBegin() {
        return NOOP_FRAME;
    }

    /**
     * Returns a {@link ProfilerSnapshot} containing placeholder values.
     * <p>
     * The snapshot name is always {@code "NOOP"}, with zeroed timing values
     * and an empty node tree.
     * </p>
     *
     * @param clear ignored parameter
     * @return a placeholder snapshot
     */
    @Override
    public @NotNull ProfilerSnapshot snapshot(final boolean clear) {
        return new ProfilerSnapshot("NOOP", Instant.now(), new ProfilerNode("NOOP", 0, 0, Map.of()));
    }
}
