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

import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.ferus.util.config.transformer.Transformer;

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
     * The path from root to node. May be relative.
     */
    @Nonnull private final String[] key;

    /**
     * The default value to be returned if all else fails.
     */
    @Nullable private final T def;

    /**
     * The transformer to convert non-simple values.
     */
    @Nullable private final Transformer<T> transformer;

    /**
     * Constructs a new {@link ConfigKey}.
     *
     * @param key the path to the node on any level
     * @param def the default value to be returned if an existing value cannot be obtained
     */
    private ConfigKey(@Nonnull final String[] key, @Nullable final T def) {
        this(key, def, null);
    }

    /**
     * Constructs a new {@link ConfigKey}.
     *
     * @param key the path to the node on any level
     * @param def the default value to be returned if an existing value cannot be obtained
     * @param transformer the transformer to convert non-simple values.
     */
    private ConfigKey(@Nonnull final String[] key, @Nullable final T def, @Nullable final Transformer<T> transformer) {
        this.key = key;
        this.def = def;
        this.transformer = transformer;
    }

    /**
     * Gets the path from root to node.
     *
     * <p>Note that keys can be used to obtain relative
     * paths and aren't always intended to lead directly
     * back to the root node.</p>
     *
     * @return the path from root to node
     */
    @Nonnull
    public String[] getKey() {
        return this.key;
    }

    /**
     * Gets the value that's returned if a value cannot be retrieved.
     *
     * @return the default value to be returned as a fallback
     */
    @Nullable
    public T getDef() {
        return this.def;
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
        return this.get(config.getNode(), this.def);
    }

    /**
     * Returns the value stored in configuration for this key.
     *
     * <p>If the path doesn't exist, there is no value, or an
     * exception occurs, the default value for this {@link ConfigKey}
     * will be returned.</p>
     *
     * @param node the {@link ConfigurationNode} to look through
     * @return the value stored in configuration for this key
     */
    @Nullable public T get(@Nonnull final ConfigurationNode node) {
        return this.get(node, this.def);
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
    @Nullable public T get(@Nonnull final ConfigFile config, @Nullable final T def) {
        return this.get(config.getNode(), def);
    }

    /**
     * Returns the value stored in configuration this key.
     *
     * <p>Note: If this ConfigKey has a default value set,
     * this method will ignore it and use the provided default value.</p>
     *
     * @param node the {@link ConfigurationNode} to look through
     * @param def the default value to be returned if an existing value cannot be obtained
     * @return the value stored in configuration for this key, or default
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public T get(@Nonnull final ConfigurationNode node, @Nullable final T def) {
        try {
            final ConfigurationNode get = node.getNode((Object[]) this.key);
            if (transformer != null) {
                try {
                    return transformer.transform(get);
                } catch (final IllegalArgumentException e) {
                    LOGGER.error("Failed to transform node \"{}\"!", this.key, e);
                    return def;
                }
            }

            return (T) get.getValue();
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
            this.set(config.getNode(), value);
            config.save();
            return true;
        } catch (final IOException e) {
            LOGGER.error("Failed to set \"{}\" to \"{}\".",
                    (value != null ? value.toString() : "null"),
                    config.getFile().toString(), e);
            return false;
        }
    }

    /**
     * Sets a value for the node located at the path for this key.
     *
     * <p>Does NOT save.</p>
     *
     * @param node the {@link ConfigurationNode} to set.
     * @param value the value to change the node to
     */
    public void set(@Nonnull final ConfigurationNode node, @Nullable final T value) {
        node.getNode((Object[]) this.key).setValue(value);
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
    public static <T> ConfigKey<T> of(final T def, final String key) {
        return new ConfigKey<>(new String[] { key }, def);
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
    public static <T> ConfigKey<T> of(final T def, final String... key) {
        return new ConfigKey<>(key, def);
    }

    /**
     * Convenience method to construct a new {@link ConfigKey}.
     *
     * <p>The purpose of this method is purely to reduce
     * <code>new ConfigKey(new Object[]{"string"});</code> to <code>ConfigKey.of("string");</code>.</p>
     *
     * @param def the default value for this key
     * @param transformer the transformer to convert non-simple values
     * @param key the path to the node on any level
     * @param <T> the type of object that the {@link ConfigKey} should reference
     * @return a new {@link ConfigKey} with the specified path
     */
    public static <T> ConfigKey<T> of(final T def, final Transformer<T> transformer, String... key) {
        return new ConfigKey<>(key, def, transformer);
    }
}
