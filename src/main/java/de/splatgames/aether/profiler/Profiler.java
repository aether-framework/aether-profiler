package de.splatgames.aether.profiler;

import org.jetbrains.annotations.NotNull;

/**
 * Defines the core contract for a performance profiler capable of measuring
 * execution times for named code sections ("spans") and frames.
 * <p>
 * A {@code Profiler} allows developers to:
 * <ul>
 *     <li>Create and manage named spans via {@link #span(String)}</li>
 *     <li>Execute code blocks within timed spans via {@link #inSpan(String, CheckedSupplier)}</li>
 *     <li>Measure frame durations via {@link #frameBegin()}</li>
 *     <li>Capture and optionally reset profiling data via {@link #snapshot(boolean)}</li>
 * </ul>
 * <p>
 * Implementations are expected to be thread-safe if used in concurrent environments.
 * </p>
 *
 * <h2>Usage Example</h2>
 * <blockquote><pre>
 * Profiler profiler = new SimpleProfiler("Example", new ProfilerConfig());
 *
 * // Manually opening and closing a span
 * try (Span span = profiler.open("databaseQuery")) {
 *     runDatabaseQuery();
 * }
 *
 * // Executing code directly in a timed span
 * String result = profiler.inSpan("processing", () -> processData(input));
 *
 * // Measuring a frame
 * try (Frame frame = profiler.frameBegin()) {
 *     renderFrame();
 * }
 * System.out.println("Frame duration: " + frame.elapsedNs() + " ns");
 *
 * // Taking a snapshot and clearing data
 * ProfilerSnapshot snapshot = profiler.snapshot(true);
 * </pre></blockquote>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public interface Profiler {

    /**
     * Creates a new {@link SpanBuilder} for the given section name.
     * <p>
     * The builder allows further configuration before the span is opened.
     * </p>
     *
     * @param sectionName the logical name of the code section to profile
     * @return a new {@link SpanBuilder} instance
     * @throws NullPointerException if {@code sectionName} is {@code null}
     */
    @NotNull
    SpanBuilder span(@NotNull final String sectionName);

    /**
     * Executes a {@link CheckedSupplier} within a timed span.
     * <p>
     * This method automatically starts and closes a span around the provided code block.
     * If the code block throws an exception, the span will still be closed properly.
     * </p>
     *
     * @param sectionName the logical name of the code section to profile
     * @param body        the code to execute
     * @param <T>         the return type of the executed code
     * @return the result of executing {@code body}
     * @throws NullPointerException if any argument is {@code null}
     */
    @NotNull
    <T> T inSpan(@NotNull final String sectionName, @NotNull final CheckedSupplier<T> body);

    /**
     * Starts measuring a new frame.
     * <p>
     * Intended for profiling frame-based systems such as game loops or real-time rendering.
     * </p>
     *
     * @return a {@link Frame} instance representing the started frame
     */
    @NotNull
    Frame frameBegin();

    /**
     * Returns a {@link ProfilerSnapshot} of the current profiling data.
     *
     * @param clear whether to clear the profiler's collected data after snapshot creation
     * @return a snapshot of the current profiler state
     */
    @NotNull
    ProfilerSnapshot snapshot(final boolean clear);

    /**
     * Opens a span immediately for the given section name.
     * <p>
     * This is a convenience method equivalent to:
     * <pre>
     * profiler.span(name).open();
     * </pre>
     *
     * @param sectionName the logical name of the code section to profile
     * @return an opened {@link Span} instance
     */
    @NotNull
    default Span open(@NotNull final String sectionName) {
        return span(sectionName).open();
    }
}
