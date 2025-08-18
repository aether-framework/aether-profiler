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
 * A functional interface similar to {@link java.util.function.Supplier Supplier} that allows for checked exceptions.
 * <p>
 * This is useful in contexts where the supplied value may be computed by code that throws a checked exception.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <blockquote><pre>
 * CheckedSupplier&lt;String&gt; supplier = () -&gt; {
 *     if (Math.random() &lt; 0.5) {
 *         throw new IOException("Random failure");
 *     }
 *     return "Hello, World!";
 * };
 *
 * try {
 *     String result = supplier.get();
 *     System.out.println("Result: " + result);
 * } catch (Exception e) {
 *     e.printStackTrace();
 * }
 * </pre></blockquote>
 *
 * @param <T> the type of results supplied by this supplier
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
@FunctionalInterface
public interface CheckedSupplier<T> {

    /**
     * Gets a result, potentially throwing a checked exception.
     *
     * @return the computed result
     * @throws Exception if unable to compute the result
     */
    T get() throws Exception;
}
