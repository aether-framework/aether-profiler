/**
 * Provides the high-resolution {@link de.splatgames.aether.profiler.Profiler Profiler} implementation
 * based on {@code System.nanoTime()} for maximum timing precision.
 * <p>
 * This package contains a low-overhead profiler suitable for fine-grained performance measurements,
 * where nanosecond-level accuracy is important. It is ideal for short-lived spans or operations
 * that require precise latency tracking without significant overhead.
 * </p>
 *
 * <h2>Key Characteristics</h2>
 * <ul>
 *   <li><strong>Precision:</strong> Uses {@code System.nanoTime()} for high-resolution timestamps.</li>
 *   <li><strong>Overhead:</strong> Minimal allocation and minimal runtime cost.</li>
 *   <li><strong>Thread Safety:</strong> Implementations are generally thread-safe unless otherwise documented.</li>
 *   <li><strong>Use Cases:</strong> Micro-benchmarking, frame-by-frame timing, tight-loop profiling.</li>
 * </ul>
 *
 * <h2>Usage Example</h2>
 * <blockquote><pre>
 * Profiler profiler = new NanoProfiler();
 * try (Span span = profiler.open("PhysicsStep")) {
 *     updatePhysics();
 * }
 * System.out.println(profiler.snapshot(true));
 * </pre></blockquote>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
package de.splatgames.aether.profiler.impl.nano;
