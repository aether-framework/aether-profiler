/*
 * Copyright (c) 2025 Splatgames.de Software and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.splatgames.aether.profiler.utils;

/**
 * Configuration options for the Aether Profiler.
 * <p>
 * This class is a mutable, fluent configuration holder for controlling profiler behavior.
 * All setters return {@code this} to allow method chaining.
 * </p>
 *
 * <h2>Supported Configuration Options</h2>
 * <ul>
 *     <li>{@link #requireFrames} – Whether to capture stack frames for each profiling span.</li>
 *     <li>{@link #enableCpuTime} – (Reserved) Whether to measure CPU time in addition to wall-clock time.</li>
 *     <li>{@link #maxChildren} – The maximum number of child nodes allowed per profiling node.</li>
 * </ul>
 *
 * <p>
 * Instances can be cloned using {@link #copy()} to create an independent configuration object.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * <blockquote><pre>
 * ProfilerConfig config = new ProfilerConfig()
 *     .requireFrames(true)
 *     .maxChildren(512);
 * </pre></blockquote>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class ProfilerConfig {
    private boolean requireFrames = false;
    private boolean enableCpuTime = false; // reserved for future use
    private int maxChildren = 256;

    /**
     * Default constructor initializes the configuration with default values.
     * <p>
     * - Stack frames are not collected by default.
     * - CPU time measurement is disabled by default.
     * - Maximum children per node is set to 256.
     * </p>
     */
    public ProfilerConfig() {
    }

    /**
     * Returns whether stack frame collection is required for each span.
     * <p>
     * If {@code true}, the profiler will capture the current stack trace when a span is opened.
     * This can provide useful diagnostic context, but may incur a performance cost.
     * </p>
     *
     * @return {@code true} if stack frames should be collected; {@code false} otherwise
     */
    public boolean requireFrames() {
        return this.requireFrames;
    }

    /**
     * Enables or disables stack frame collection for spans.
     *
     * @param v {@code true} to collect stack frames; {@code false} to disable
     * @return this configuration instance for chaining
     */
    public ProfilerConfig requireFrames(final boolean v) {
        this.requireFrames = v;
        return this;
    }

    /**
     * Returns whether CPU time measurement is enabled.
     * <p>
     * This feature is currently reserved for future implementation.
     * </p>
     *
     * @return {@code true} if CPU time measurement is enabled; {@code false} otherwise
     */
    public boolean enableCpuTime() {
        return this.enableCpuTime;
    }

    /**
     * Enables or disables CPU time measurement.
     * <p>
     * This is a reserved option and may not yet have any effect.
     * </p>
     *
     * @param v {@code true} to enable CPU time measurement; {@code false} to disable
     * @return this configuration instance for chaining
     */
    public ProfilerConfig enableCpuTime(final boolean v) {
        this.enableCpuTime = v;
        return this;
    }

    /**
     * Returns the maximum number of child nodes that a profiling node may hold.
     *
     * @return the maximum child count
     */
    public int maxChildren() {
        return maxChildren;
    }

    /**
     * Sets the maximum number of child nodes allowed per profiling node.
     *
     * @param n the maximum child count; must be positive
     * @return this configuration instance for chaining
     */
    public ProfilerConfig maxChildren(final int n) {
        this.maxChildren = n;
        return this;
    }

    /**
     * Creates a deep copy of this configuration instance.
     * <p>
     * The returned configuration is independent of the original, meaning
     * changes to one will not affect the other.
     * </p>
     *
     * @return a cloned configuration object with the same settings
     */
    public ProfilerConfig copy() {
        return new ProfilerConfig()
                .requireFrames(this.requireFrames)
                .enableCpuTime(this.enableCpuTime)
                .maxChildren(this.maxChildren);
    }
}
