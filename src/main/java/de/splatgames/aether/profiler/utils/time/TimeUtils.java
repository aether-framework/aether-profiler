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

package de.splatgames.aether.profiler.utils.time;


/**
 * The TimeUtils class provides utility methods for obtaining the current time in milliseconds and nanoseconds.
 * It also includes a static instance of the NanoTimeSource class for precise timing operations.
 */
public class TimeUtils {

    /**
     * Represents a static instance of {@link TimeSource.NanoTimeSource} used for obtaining the current system time in nanoseconds.
     * This variable provides a convenient way to access the system time source for precise timing operations.
     *
     * <p>
     * Usage:
     * <pre>{@code
     * long currentTime = timeSource.currentTime();
     * }</pre>
     *
     * @see TimeSource.NanoTimeSource
     */
    public static TimeSource.NanoTimeSource timeSource;

    static {
        timeSource = System::nanoTime;
    }

    /**
     * Private constructor to prevent instantiation of the utility class.
     * This class is designed to be used statically, and instantiation is not required.
     */
    private TimeUtils() {
        // Prevent instantiation
    }

    /**
     * Returns the current time in milliseconds.
     *
     * @return the current time in milliseconds
     */
    public static long getMillis() {
        return getNanos() / 1000000L;
    }

    /**
     * Returns the current time in nanoseconds.
     *
     * @return the current time in nanoseconds
     */
    public static long getNanos() {
        return timeSource.getAsLong();
    }
}
