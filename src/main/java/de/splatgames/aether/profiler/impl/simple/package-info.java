/**
 * Provides a lightweight and straightforward {@link de.splatgames.aether.profiler.Profiler Profiler} implementation
 * with millisecond-level precision.
 * <p>
 * This package contains an easy-to-use profiler for general performance tracking,
 * optimized for simplicity and low complexity rather than ultra-high precision.
 * It is intended for applications where approximate timing data is sufficient.
 * </p>
 *
 * <h2>Key Characteristics</h2>
 * <ul>
 *   <li><strong>Precision:</strong> Uses {@code System.currentTimeMillis()} or equivalent for coarse timing.</li>
 *   <li><strong>Overhead:</strong> Very low, with a focus on minimal API complexity.</li>
 *   <li><strong>Thread Safety:</strong> Implementations may be thread-safe; check individual class documentation.</li>
 *   <li><strong>Use Cases:</strong> General application profiling, long-running tasks, periodic performance logging.</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <blockquote><pre>
 * Profiler profiler = new SimpleProfiler();
 * try (Span span = profiler.open("DataLoad")) {
 *     loadDataFromDisk();
 * }
 * log.info("Profiling result: {}", profiler.snapshot(true));
 * </pre></blockquote>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
package de.splatgames.aether.profiler.impl.simple;
