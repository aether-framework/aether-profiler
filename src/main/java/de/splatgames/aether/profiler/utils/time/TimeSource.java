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

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

/**
 * The TimeSource interface is a functional interface that represents a source of time values.
 * It provides a method to retrieve the value of the current object in the specified time unit.
 *
 * @author Erik Pförtner
 * @see TimeUnit
 * @since 1.0.0
 */
@FunctionalInterface
public interface TimeSource {
    /**
     * Retrieves the value of the current object in the specified time unit.
     *
     * @param timeUnit the time unit in which to retrieve the value
     * @return the value of the current object in the specified time unit
     */
    long get(@NotNull final TimeUnit timeUnit);

    /**
     * A specialized TimeSource that provides time values in nanoseconds.
     * It extends the TimeSource interface and implements LongSupplier to provide
     * the current time in nanoseconds.
     */
    interface NanoTimeSource extends TimeSource, LongSupplier {
        /**
         * Returns the value of the object converted to the specified TimeUnit.
         *
         * @param timeUnit the TimeUnit to convert the value to
         * @return the value converted to the specified TimeUnit
         */
        default long get(@NotNull final TimeUnit timeUnit) {
            return timeUnit.convert(this.getAsLong(), TimeUnit.NANOSECONDS);
        }
    }
}
