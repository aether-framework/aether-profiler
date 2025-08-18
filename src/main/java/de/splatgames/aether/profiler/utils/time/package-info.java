/**
 * Contains time measurement and timing-related utilities for the Aether Profiler.
 * <p>
 * This package provides abstractions over system time sources and related helpers
 * to ensure consistent and accurate time measurements across different profiler
 * implementations.
 * </p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Providing high-precision timing sources (e.g., based on {@code System.nanoTime()}).</li>
 *   <li>Normalizing time values for reporting and snapshot generation.</li>
 *   <li>Abstracting platform-dependent differences in time measurement APIs.</li>
 * </ul>
 *
 * <h2>Design Notes</h2>
 * <ul>
 *   <li>Designed for minimal overhead in high-frequency measurement contexts.</li>
 *   <li>Time utilities should be deterministic and free of side effects.</li>
 *   <li>Used internally by profiler implementations in {@code de.splatgames.aether.profiler.impl}.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <blockquote><pre>
 * long start = TimeSource.nanoTime();
 * // ... perform measured operations ...
 * long elapsed = TimeSource.nanoTime() - start;
 * </pre></blockquote>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
package de.splatgames.aether.profiler.utils.time;
