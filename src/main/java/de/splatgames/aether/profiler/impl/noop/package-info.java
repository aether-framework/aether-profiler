/**
 * Provides no-operation (NOOP) implementations of the
 * {@link de.splatgames.aether.profiler.Profiler} API.
 *
 * <p>
 * These implementations are lightweight stand-ins that
 * allow applications to keep instrumentation code in place
 * while effectively disabling profiling. All methods return
 * inert objects or constant values without recording any
 * metrics or consuming significant resources.
 * </p>
 *
 * <p>
 * Typical usage is to supply a {@link de.splatgames.aether.profiler.impl.noop.NoOpProfiler}
 * when profiling is not required, ensuring consistent API
 * interaction without runtime overhead.
 * </p>
 *
 * @author Erik Pförtner
 * @since 1.1.0
 */
package de.splatgames.aether.profiler.impl.noop;
