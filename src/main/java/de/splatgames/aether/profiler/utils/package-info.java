/**
 * Provides utility classes and helper functions for the Aether Profiler framework.
 * <p>
 * The utilities in this package contain shared logic and support components
 * that are used by multiple profiler implementations. These helpers are designed
 * to be reusable, lightweight, and implementation-agnostic.
 * </p>
 *
 * <h2>Responsibilities</h2>
 * <ul>
 *   <li>Shared configuration models (e.g., {@link de.splatgames.aether.profiler.utils.ProfilerConfig ProfilerConfig}).</li>
 *   <li>Common internal utilities for data processing, validation, and conversions.</li>
 *   <li>Non-time-specific support classes that improve profiler usability.</li>
 * </ul>
 *
 * <h2>Design Notes</h2>
 * <ul>
 *   <li>All utilities in this package are stateless or encapsulate their state in dedicated model classes.</li>
 *   <li>Utilities are designed to be safe for multi-threaded environments unless otherwise documented.</li>
 *   <li>The package serves as a foundation for both core and implementation-specific profiler logic.</li>
 * </ul>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
package de.splatgames.aether.profiler.utils;
