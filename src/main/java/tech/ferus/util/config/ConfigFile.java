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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A wrapper for Configurate's {@link ConfigurationLoader}. Allows you to easily get and save data.
 *
 * @param <T> the type of {@link ConfigurationNode}
 */
public class ConfigFile<T extends ConfigurationNode> {

    /**
     * The ConfigFile logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFile.class);

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
    @Nonnull private T root;

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
     * <p>Does not give exceptions upon failure.</p>
     *
     * @return true if save was successful; false otherwise
     */
    public boolean save() {
        try {
            this.loader.save(this.root);
            return true;
        } catch (final IOException e) {
            LOGGER.error("Failed to save configuration file to \"{}\".", this.file.toString(), e);
            return false;
        }
    }

    /**
     * Saves the root {@link ConfigurationNode} with the active {@link ConfigurationLoader}.
     *
     * @throws IOException if {@link ConfigurationLoader#save(ConfigurationNode)} throws an exception
     */
    public void saveWithException() throws IOException {
        this.loader.save(this.root);
    }

    /**
     * Reloads the configuration from the {@link ConfigurationLoader}.
     *
     * <p>Does not give exceptions upon failure.</p>
     *
     * @return true if reload was successful; false otherwise
     */
    public boolean reload() {
        try {
            this.reloadWithException();
            return true;
        } catch (final IOException e) {
            LOGGER.error("Failed to reload configuration for \"{}\".", this.file.toString(), e);
            return false;
        }
    }

    /**
     * Reloads the configuration from the {@link ConfigurationLoader}.
     *
     * @throws IOException if {@link ConfigurationLoader#load()} throws an exception
     */
    public void reloadWithException() throws IOException {
        this.root = this.loader.load();
    }
}
