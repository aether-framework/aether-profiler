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

import de.splatgames.aether.profiler.ProfilerNode;
import de.splatgames.aether.profiler.ProfilerSnapshot;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;

/**
 * Utility class for exporting {@link ProfilerSnapshot} instances into a compact JSON representation.
 * <p>
 * The JSON output is produced manually using a {@link StringBuilder} to avoid
 * the overhead of external serialization libraries and to maintain full control
 * over the structure and formatting.
 * </p>
 *
 * <p>
 * The generated JSON format has the following structure:
 * </p>
 * <pre>{@code
 * {
 *   "profiler": "ProfilerName",
 *   "at": "2025-08-11T17:52:28.559912400Z",
 *   "root": {
 *     "name": "root",
 *     "totalNs": 204200,
 *     "count": 1,
 *     "avgNs": 204200,
 *     "children": {
 *       "childName": {
 *         ...
 *       }
 *     }
 *   }
 * }
 * }</pre>
 *
 * <p>
 * This class is immutable, thread-safe, and cannot be instantiated.
 * </p>
 *
 * @author Erik Pförtner
 * @since 1.0.0
 */
public final class JsonExporter {

    /**
     * Private constructor to prevent instantiation.
     */
    private JsonExporter() {
    }

    /**
     * Serializes a {@link ProfilerSnapshot} to its JSON representation.
     * <p>
     * This method recursively serializes the root {@link ProfilerNode} and all its children,
     * producing a valid JSON object as a single string without extra whitespace or formatting.
     * </p>
     *
     * @param s the profiler snapshot to serialize; must not be {@code null}
     * @return the JSON representation of the profiler snapshot; never {@code null}
     */
    @NotNull
    public static String toJson(@NotNull final ProfilerSnapshot s) {
        final StringBuilder sb = new StringBuilder(256);
        sb.append('{');
        kv(sb, "profiler").append('"').append(s.profiler()).append('"').append(',');
        kv(sb, "at").append('"').append(s.at()).append('"').append(',');
        sb.append("\"root\":");
        node(sb, s.root());
        sb.append('}');
        return sb.toString();
    }

    /**
     * Recursively serializes a {@link ProfilerNode} into JSON format.
     *
     * @param sb the string builder to append to; must not be {@code null}
     * @param n  the profiler node to serialize; must not be {@code null}
     */
    private static void node(@NotNull final StringBuilder sb, @NotNull final ProfilerNode n) {
        sb.append('{');
        kv(sb, "name").append('"').append(n.name()).append('"').append(',');
        kv(sb, "totalNs").append(n.totalNs()).append(',');
        kv(sb, "count").append(n.count()).append(',');
        kv(sb, "avgNs").append(n.count() == 0 ? 0 : (n.totalNs() / n.count())).append(',');
        sb.append("\"children\":{");
        final Iterator<Map.Entry<String, ProfilerNode>> it = n.children().entrySet().iterator();
        while (it.hasNext()) {
            final var e = it.next();
            sb.append('"').append(e.getKey()).append('"').append(':');
            node(sb, e.getValue());
            if (it.hasNext()) sb.append(',');
        }
        sb.append('}');
        sb.append('}');
    }

    /**
     * Appends a JSON key (enclosed in quotes and followed by a colon) to the given StringBuilder.
     *
     * @param sb the string builder to append to; must not be {@code null}
     * @param k  the key to append; must not be {@code null}
     * @return the same StringBuilder instance for chaining
     */
    private static StringBuilder kv(@NotNull final StringBuilder sb, @NotNull final String k) {
        return sb.append('"').append(k).append('"').append(':');
    }
}
