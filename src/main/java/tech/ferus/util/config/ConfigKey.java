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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An object designed to allow easy data-grabbing from a {@link ConfigFile}.
 *
 * @param <T> the type of object that this {@link ConfigKey} should reference
 */
public class ConfigKey<T> {

    /**
     * The ConfigKeys logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigKey.class);

    /**
     * The path from root to node.
     */
    @Nonnull private final Object[] key;

    @Nullable private final T def;

    /**
     * Constructs a new {@link ConfigKey}.
     *
     * <p>Note: Default value will be null.</p>
     *
     * @param key the path to the node on any level
     */
    private ConfigKey(@Nonnull final Object[] key) {
        this(key, null);
    }

    /**
     * Constructs a new {@link ConfigKey}.
     *
     * @param key the path to the node on any level
     * @param def the default value to be returned if an existing value cannot be obtained
     */
    private ConfigKey(@Nonnull final Object[] key, @Nullable final T def) {
        this.key = key;
        this.def = def;
    }

    /**
     * Returns the value stored in configuration for this key.
     *
     * <p>If the path doesn't exist, there is no value, or an
     * exception occurs, the default value for this {@link ConfigKey}
     * will be returned.</p>
     *
     * @param config the {@link ConfigFile} to look through
     * @return the value stored in configuration for this key
     */
    @Nullable public T get(@Nonnull final ConfigFile config) {
        return this.get(config, this.def);
    }

    /**
     * Returns the value stored in configuration this key.
     *
     * <p>Note: If this ConfigKey has a default value set,
     * this method will ignore it and use the provided default value.</p>
     *
     * @param config the {@link ConfigFile} to look through
     * @param def the default value to be returned if an existing value cannot be obtained
     * @return the value stored in configuration for this key, or default
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public T get(@Nonnull final ConfigFile config, @Nullable final T def) {
        try {
            return (T) config.getNode((Object[]) this.key).getValue(def);
        } catch (final ClassCastException e) {
            LOGGER.error("Improper value type for \"{}\"!", this.key, e);
            return def;
        }
    }

    /**
     * Sets and saves a value for the node located at the path for this key.
     *
     * @param config the {@link ConfigFile} to save to
     * @param value the value to change the node to
     * @throws IOException if the configuration file cannot be saved to
     */
    public void setWithException(@Nullable final ConfigFile config, @Nullable final T value) throws IOException {
        if (config == null) {
            throw new IllegalStateException("Attempted to set \"" + String.join(".", Arrays.toString(this.key)) + "\" to a null ConfigFile.");
        }

        config.getNode(this.key).setValue(value);
        config.saveWithException();
    }

    /**
     * Sets and saves a value for the node located at the path for this key.
     *
     * @param config the {@link ConfigFile} to save to
     * @param value the value to change the node to
     * @return true if the configuration was saved; false otherwise
     */
    public boolean set(@Nullable final ConfigFile config, @Nullable final T value) {
        if (config == null) {
            LOGGER.error("Failed to set \"{}={}\" to a NULL config file.",
                    String.join(".", Arrays.toString(this.key)),
                    (value != null ? value.toString() : "null"));
            return false;
        }

        try {
            this.setWithException(config, value);
            return true;
        } catch (final IOException e) {
            LOGGER.error("Failed to set \"{}\" to \"{}\".",
                    (value != null ? value.toString() : "null"),
                    config.getFile().toString(), e);
            return false;
        }
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

    /**
     * Convenience method to construct a new {@link ConfigKey}.
     *
     * <p>The purpose of this method is purely to reduce
     * <code>new ConfigKey(new Object[]{"string"});</code> to <code>ConfigKey.of("string");</code>.</p>
     *
     * @param def the default value for this key
     * @param key the path to the node on the top level
     * @param <T> the type of object that the {@link ConfigKey} should reference
     * @return a new {@link ConfigKey} with the specified path
     */
    public static <T> ConfigKey<T> of(final T def, final Object key) {
        return new ConfigKey<>(new Object[]{key}, def);
    }

    /**
     * Convenience method to construct a new {@link ConfigKey}.
     *
     * <p>The purpose of this method is purely to reduce
     * <code>new ConfigKey(new Object[]{"string"});</code> to <code>ConfigKey.of("string");</code>.</p>
     *
     * @param def the default value for this key
     * @param key the path to the node on any level
     * @param <T> the type of object that the {@link ConfigKey} should reference
     * @return a new {@link ConfigKey} with the specified path
     */
    public static <T> ConfigKey<T> of(final T def, final Object... key) {
        return new ConfigKey<>(key, def);
    }
}
