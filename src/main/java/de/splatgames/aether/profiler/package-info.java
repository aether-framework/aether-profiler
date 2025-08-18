/**
 * Core API of the Aether Profiler.
 * <p>
 * This package defines the public interfaces and immutable records used to profile hierarchical
 * code sections (“spans”) and frame-based workloads. It is implementation-agnostic: choose a
 * concrete implementation from {@link de.splatgames.aether.profiler.impl} (e.g.
 * {@link de.splatgames.aether.profiler.impl.nano Nano} or
 * {@link de.splatgames.aether.profiler.impl.simple Simple}) and use the types defined here to
 * instrument your code. </p>
 *
 * <h2>Key Concepts</h2>
 * <ul>
 *   <li><b>{@link de.splatgames.aether.profiler.Profiler Profiler}:</b>
 *       Root service to open spans, measure frames, and capture {@link de.splatgames.aether.profiler.ProfilerSnapshot snapshots}.</li>
 *   <li><b>{@link de.splatgames.aether.profiler.Span Span}:</b>
 *       A timed, named scope that can contain child spans; forms a tree per thread.</li>
 *   <li><b>{@link de.splatgames.aether.profiler.SpanBuilder SpanBuilder}:</b>
 *       Fluent builder to set optional metadata (category, attributes) before opening a span.</li>
 *   <li><b>{@link de.splatgames.aether.profiler.Frame Frame}:</b>
 *       A higher-level measurement unit (e.g., a render tick or request cycle).</li>
 *   <li><b>{@link de.splatgames.aether.profiler.ProfilerSnapshot ProfilerSnapshot}:</b>
 *       Immutable snapshot of the current aggregates.</li>
 *   <li><b>{@link de.splatgames.aether.profiler.ProfilerNode ProfilerNode}:</b>
 *       Node in the aggregated call tree with inclusive time and invocation count.</li>
 * </ul>
 *
 * <h2>Lifecycle &amp; Usage</h2>
 * <ol>
 *   <li>Create a concrete profiler (see {@code impl.*} packages).</li>
 *   <li>Open spans via {@link de.splatgames.aether.profiler.Profiler#open(String)} or
 *       {@link de.splatgames.aether.profiler.Profiler#span(String)} + {@code open()}.</li>
 *   <li>(Optional) Bound work to frames with {@link de.splatgames.aether.profiler.Profiler#frameBegin()}.</li>
 *   <li>Capture a {@link de.splatgames.aether.profiler.ProfilerSnapshot} via
 *       {@link de.splatgames.aether.profiler.Profiler#snapshot(boolean)}.</li>
 * </ol>
 *
 * <h3>Example</h3>
 * <blockquote><pre>{@code
 * import de.splatgames.aether.profiler.*;
 * import de.splatgames.aether.profiler.impl.nano.NanoProfiler;
 * import de.splatgames.aether.profiler.utils.ProfilerConfig;
 *
 * Profiler profiler = new NanoProfiler("XINI-Parse", new ProfilerConfig());
 *
 * // Timed span (try-with-resources)
 * try (Span parse = profiler.open("parse")) {
 *     try (Span lex = profiler.open("lexer")) {
 *         // tokenize();
 *     }
 *     try (Span ast = profiler.open("astBuild")) {
 *         // buildAst();
 *     }
 * }
 *
 * // Optional frame boundary
 * try (Frame frame = profiler.frameBegin()) {
 *     // per-frame work...
 * }
 *
 * ProfilerSnapshot snapshot = profiler.snapshot(true);
 * // Export / inspect snapshot (see utils)
 * }</pre></blockquote>
 *
 * <h2>Threading Model</h2>
 * <p>
 * Spans are tracked per thread using a {@code ThreadLocal} stack. Each thread builds its own
 * span tree; aggregation merges completed roots into a global structure. Implementations aim
 * to be thread-safe for concurrent use; consult class-level docs of concrete implementations. </p>
 *
 * <h2>Timing Semantics</h2>
 * <ul>
 *   <li>Times are <em>inclusive</em> per node (a parent’s total includes its children).</li>
 *   <li>Average time ({@code avgNs}) is computed as {@code totalNs / count} with integer division.</li>
 *   <li>Self time can be derived as {@code totalNs - sum(children.totalNs)} if needed.</li>
 * </ul>
 *
 * <h2>Configuration &amp; Utilities</h2>
 * <p>
 * Common configuration and helpers live in {@link de.splatgames.aether.profiler.utils}, including
 * {@link de.splatgames.aether.profiler.utils.ProfilerConfig} and exporter utilities.
 * Time-source abstractions and related helpers reside in
 * {@link de.splatgames.aether.profiler.utils.time}. </p>
 *
 * <h2>Error Handling</h2>
 * <p>
 * {@link de.splatgames.aether.profiler.Profiler#inSpan(String, CheckedSupplier)} wraps checked exceptions
 * into {@code RuntimeException}. Prefer explicit try-with-resources for fine-grained control. </p>
 *
 * <h2>Annotations</h2>
 * <p>
 * Public APIs in this package use {@code @NotNull}/{@code @Nullable} for parameters and return values.
 * Follow these contracts when implementing custom profilers or integrating consumers. </p>
 *
 * @see de.splatgames.aether.profiler.impl
 * @see de.splatgames.aether.profiler.utils
 * @see de.splatgames.aether.profiler.utils.time
 * @since 1.0.0
 */
package de.splatgames.aether.profiler;
