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

package de.splatgames.aether.profiler;

/**
 * Represents a timed frame within the profiler's measurement scope.
 * <p>
 * A {@code Frame} typically measures the duration between its creation
 * and its {@link #close()} invocation. This interface is designed to be
 * used with the try-with-resources statement, ensuring proper cleanup
 * and timing finalization.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <blockquote><pre>
 * try (Frame frame = profiler.startFrame()) {
 *     // Code to measure
 *     performComplexCalculation();
 * }
 * long durationNs = frame.elapsedNs();
 * System.out.println("Frame duration: " + durationNs + " ns");
 * </pre></blockquote>
 *
 * <p>
 * Implementations are expected to be lightweight and thread-safe if used
 * in concurrent profiling scenarios.
 * </p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public interface Frame extends AutoCloseable {

    /**
     * Returns the elapsed time in nanoseconds between the frame's start and the current moment,
     * or between the start and the {@link #close()} call if already closed.
     *
     * @return the elapsed duration in nanoseconds
     */
    long elapsedNs();

    /**
     * Closes this frame, marking its end time in the profiler.
     * <p>
     * Implementations should ensure that this method is idempotent
     * and can be safely called multiple times without adverse effects.
     * </p>
     */
    @Override
    void close();
}
