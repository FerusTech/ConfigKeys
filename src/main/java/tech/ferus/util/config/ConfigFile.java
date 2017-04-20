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
import java.nio.file.Path;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A wrapper for Configurate's {@link ConfigurationLoader}. Allows you to easily get and save data.
 *
 * @param <T> the type of {@link ConfigurationNode}
 */
public class ConfigFile<T extends ConfigurationNode> {

    /**
     * The default {@link ConfigFile}.
     */
    @Nullable private static ConfigFile<? extends ConfigurationNode> defaultConfig = null;

    /**
     * The location of the configuration file on disk.
     */
    @Nonnull private final Path file;

    /**
     * The active loader for the configuration file on disk.
     */
    @Nonnull private final ConfigurationLoader<T> loader;

    /**
     * The loaded {@link ConfigurationNode} at the root level.
     */
    @Nonnull private final T root;

    /**
     * Constructs a new {@link ConfigFile}.
     *
     * @param file the location of the configuration file on disk
     * @param loader the active loaded for the configuration file on disk
     * @param root the loaded {@link ConfigurationNode} at the root level
     */
    public ConfigFile(@Nonnull final Path file,
                      @Nonnull final ConfigurationLoader<T> loader,
                      @Nonnull final T root) {
        this.file = file;
        this.loader = loader;
        this.root = root;
    }

    /**
     * Gets the location of the configuration file on disk.
     *
     * @return the location of the configuration file on disk
     */
    @Nonnull public Path getFile() {
        return this.file;
    }

    /**
     * Gets the location of the configuration file's parent directory on disk.
     *
     * @return the location of the configuration file's parent directory on disk
     */
    @Nonnull public Path getFileDirectory() {
        return this.file.getParent();
    }

    /**
     * Gets the active loader for the configuration file on disk.
     *
     * @return the active loader for the configuration file on disk
     */
    @Nonnull public ConfigurationLoader<T> getLoader() {
        return this.loader;
    }

    /**
     * Gets the root {@link ConfigurationNode}.
     *
     * @return the root node
     */
    @Nonnull public T getNode() {
        return this.root;
    }

    /**
     * Gets a {@link ConfigurationNode} on the first level.
     *
     * @param path the key for the node
     * @return a {@link ConfigurationNode} on the first level
     */
    public ConfigurationNode getNode(final Object path) {
        return this.root.getNode(path);
    }

    /**
     * Gets a {@link ConfigurationNode} on any level.
     *
     * @param path the key path for the node
     * @return a {@link ConfigurationNode} on any level
     */
    public ConfigurationNode getNode(final Object... path) {
        return this.root.getNode((Object[]) path);
    }

    /**
     * Saves the root {@link ConfigurationNode} with the active {@link ConfigurationLoader}.
     *
     * @throws IOException if the {@link ConfigurationNode} cannot be saved
     */
    public void save() throws IOException {
        this.loader.save(this.root);
    }

    /**
     * Sets the default {@link ConfigFile}
     *
     * @param defaultConfig the {@link ConfigFile} to be set
     * @param <T> the type of {@link ConfigurationNode}
     */
    public static <T extends ConfigurationNode> void setDefaultConfig(@Nonnull final ConfigFile<T> defaultConfig) {
        ConfigFile.defaultConfig = defaultConfig;
    }

    /**
     * Gets the default {@link ConfigFile}.
     *
     * @param <T> the type of {@link ConfigurationNode}
     * @return the default {@link ConfigFile}
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends ConfigurationNode> ConfigFile<T> getDefaultConfig() {
        return (ConfigFile<T>) ConfigFile.defaultConfig;
    }

    /**
     * Determines whether or not a value has been set for the default {@link ConfigFile}.
     *
     * @return true if the default {@link ConfigFile} has been set
     */
    public static boolean isDefaultConfigSet() {
        return ConfigFile.defaultConfig != null;
    }
}
