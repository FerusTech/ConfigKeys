/*
 * Copyright 2017 FerusTech LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package tech.ferus.util.config;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static tech.ferus.util.config.ConfigFile.getDefaultConfig;

/**
 * An object designed to allow easy data-grabbing from a {@link ConfigFile}.
 *
 * @param <T> the type of object that this {@link ConfigKey} should reference
 */
public class ConfigKey<T> {

    /**
     * The path from root to node.
     */
    @Nonnull private final Object[] key;

    /**
     * Constructs a new {@link ConfigKey}.
     *
     * @param key the path to the node on any level
     */
    private ConfigKey(@Nonnull final Object[] key) {
        this.key = key;
    }

    /**
     * Returns the value stored in configuration for this key.
     *
     * <p>Note: The method attempts to get the data from the default {@link ConfigFile}.</p>
     *
     * @return the value stored in configuration for this key
     */
    @Nullable public T get() {
        return this.get(getDefaultConfig(), null);
    }

    /**
     * Returns the value stored in configuration for this key.
     *
     * @param config the {@link ConfigFile} to look through
     * @return the value stored in configuration for this key
     */
    @Nullable public T get(@Nonnull final ConfigFile config) {
        return this.get(config, null);
    }

    /**
     * Returns the value stored in configuration this key.
     *
     * <p>Note: The method attempts to get the data from the default {@link ConfigFile}.</p>
     *
     * <p>Additionally, this method will return a default value if none can be found for this key.</p>
     *
     * @param def the default value to be returned if an existing value cannot be obtained
     * @return the value stored in configuration for this key, or default
     */
    @Nullable public T get(@Nullable final T def) {
        return this.get(getDefaultConfig(), def);
    }

    /**
     * Returns the value stored in configuration this key.
     *
     * <p>Additionally, this method will return a default value if none can be found for this key.</p>
     *
     * @param def the default value to be returned if an existing value cannot be obtained
     * @return the value stored in configuration for this key, or default
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public T get(@Nullable final ConfigFile config, @Nullable final T def) {
        if (config == null) {
            throw new IllegalStateException("Attempted to get \"" + String.join(".", this.key) + "\" from null ConfigFile.");
        }

        try {
            return (T) config.getNode((Object[]) this.key).getValue(def);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException("Improper value type for \"{}\"!", e);
        }
    }

    /**
     * Sets and saves a value for the node located at the path for this key.
     *
     * <p>Note: This method will attempt to save data to the default {@link ConfigFile}.</p>
     *
     * @param value the value to change the node to
     * @throws IOException if the configuration file cannot be saved to
     */
    public void set(@Nullable final T value) throws IOException {
        this.set(getDefaultConfig(), value);
    }

    /**
     * Sets and saves a value for the node located at the path for this key.
     *
     * @param config the {@link ConfigFile} to save to
     * @param value the value to change the node to
     * @throws IOException if the configuration file cannot be saved to
     */
    public void set(@Nullable final ConfigFile config, @Nullable final T value) throws IOException {
        if (config == null) {
            throw new IllegalStateException("Attempted to set \"" + String.join(".", this.key) + "\" to a null ConfigFile.");
        }

        config.getNode(this.key).setValue(value);
        config.save();
    }

    /**
     * Convenience method to construct a new {@link ConfigKey}.
     *
     * <p>The purpose of this method is purely to reduce
     * <code>new ConfigKey(new Object[]{"string"});</code> to <code>ConfigKey.of("string");</code>.</p>
     *
     * @param key the path to the node on the top level
     * @param <T> the type of object that the {@link ConfigKey} should reference
     * @return a new {@link ConfigKey} with the specified path
     */
    public static <T> ConfigKey<T> of(final Object key) {
        return new ConfigKey<>(new Object[]{key});
    }

    /**
     * Convenience method to construct a new {@link ConfigKey}.
     *
     * <p>The purpose of this method is purely to reduce
     * <code>new ConfigKey(new Object[]{"string"});</code> to <code>ConfigKey.of("string");</code>.</p>
     *
     * @param key the path to the node on any level
     * @param <T> the type of object that the {@link ConfigKey} should reference
     * @return a new {@link ConfigKey} with the specified path
     */
    public static <T> ConfigKey<T> of(final Object... key) {
        return new ConfigKey<>(key);
    }
}
