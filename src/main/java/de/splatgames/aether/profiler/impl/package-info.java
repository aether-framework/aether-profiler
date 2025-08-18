/**
 * Contains concrete implementations of the {@link de.splatgames.aether.profiler.Profiler Profiler} API.
 * <p>
 * This package provides multiple profiler variants with different trade-offs in terms of precision,
 * overhead, and intended use cases. Implementations in this package are typically selected based on
 * the desired balance between measurement accuracy and runtime performance.
 * </p>
 *
 * <h2>Available Implementations</h2>
 * <ul>
 *   <li>
 *     {@link de.splatgames.aether.profiler.impl.nano} –
 *     High-precision profilers using {@code System.nanoTime()} for nanosecond-level timing.
 *     Suitable for micro-benchmarks and short-lived span measurements.
 *   </li>
 *   <li>
 *     {@link de.splatgames.aether.profiler.impl.simple} –
 *     Lightweight profilers using millisecond-level timing.
 *     Ideal for general-purpose profiling with minimal complexity.
 *   </li>
 * </ul>
 *
 * <h2>Design Notes</h2>
 * <ul>
 *   <li>All implementations adhere to the {@link de.splatgames.aether.profiler.Profiler Profiler} contract.</li>
 *   <li>Factory methods or direct instantiation can be used to obtain a profiler instance.</li>
 *   <li>Thread safety may vary between implementations; consult the specific class documentation.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <blockquote><pre>
 * // Using a high-precision profiler
 * Profiler profiler = new NanoProfiler();
 * try (Span span = profiler.open("UpdateStep")) {
 *     updateGameLogic();
 * }
 * ProfilerSnapshot snapshot = profiler.snapshot(true);
 * </pre></blockquote>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
package de.splatgames.aether.profiler.impl;
