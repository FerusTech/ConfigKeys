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
import java.nio.file.Files;
import java.nio.file.Path;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.json.JSONConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A wrapper for Configurate's {@link ConfigurationLoader}. Allows you to easily get and save data.
 *
 * @param <T> the type of {@link ConfigurationNode}
 */
public class ConfigFile<T extends ConfigurationNode> {

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
    public T getNode(final Object path) {
        return (T) this.root.getNode(path);
    }

    /**
     * Gets a {@link ConfigurationNode} on any level.
     *
     * @param path the key path for the node
     * @return a {@link ConfigurationNode} on any level
     */
    public T getNode(final Object... path) {
        return (T) this.root.getNode((Object[]) path);
    }

    /**
     * Saves the root {@link ConfigurationNode} with the active {@link ConfigurationLoader}.
     *
     * @throws IOException if {@link ConfigurationLoader#save(ConfigurationNode)} throws an exception
     */
    public void save() throws IOException {
        this.loader.save(this.root);
    }

    /**
     * Reloads the configuration from the {@link ConfigurationLoader}.
     *
     * @throws IOException if {@link ConfigurationLoader#load()} throws an exception
     */
    public void reload() throws IOException {
        this.root = this.loader.load();
    }

    /**
     * Loads a ConfigFile with the HOCON format.
     *
     * <p>Will be in {@link CommentedConfigurationNode} format.</p>
     *
     * @param destination the local file to write to and read from
     * @param resource the jar file to read from
     * @param overwrite whether or not to overwrite the local file
     * @param merge whether or not to merge the resource with the local file
     * @return a ConfigFile with the HOCON format
     * @throws IOException if {@link ConfigFile#load(ConfigType, Path, String, boolean, boolean)} throws an exception
     */
    public static ConfigFile<CommentedConfigurationNode> loadHocon(@Nonnull final Path destination,
                                       @Nullable final String resource,
                                       final boolean overwrite,
                                       final boolean merge) throws IOException {
        return load(ConfigType.HOCON, destination, resource, overwrite, merge);
    }


    /**
     * Loads a ConfigFile with the GSON format.
     *
     * <p>Will be in {@link ConfigurationNode} format.</p>
     *
     * @param destination the local file to write to and read from
     * @param resource the jar file to read from
     * @param overwrite whether or not to overwrite the local file
     * @param merge whether or not to merge the resource with the local file
     * @return a ConfigFile with the GSON format
     * @throws IOException if {@link ConfigFile#load(ConfigType, Path, String, boolean, boolean)} throws an exception
     */
    public static ConfigFile<ConfigurationNode> loadGson(@Nonnull final Path destination,
                                                         @Nullable final String resource,
                                                         final boolean overwrite,
                                                         final boolean merge) throws IOException {
        return load(ConfigType.GSON, destination, resource, overwrite, merge);
    }


    /**
     * Loads a ConfigFile with the JSON format.
     *
     * <p>Will be in {@link ConfigurationNode} format.</p>
     *
     * @param destination the local file to write to and read from
     * @param resource the jar file to read from
     * @param overwrite whether or not to overwrite the local file
     * @param merge whether or not to merge the resource with the local file
     * @return a ConfigFile with the JSON format
     * @throws IOException if {@link ConfigFile#load(ConfigType, Path, String, boolean, boolean)} throws an exception
     */
    public static ConfigFile<ConfigurationNode> loadJson(@Nonnull final Path destination,
                                                         @Nullable final String resource,
                                                         final boolean overwrite,
                                                         final boolean merge) throws IOException {
        return load(ConfigType.JSON, destination, resource, overwrite, merge);
    }


    /**
     * Loads a ConfigFile with the YAML format.
     *
     * <p>Will be in {@link ConfigurationNode} format.</p>
     *
     * @param destination the local file to write to and read from
     * @param resource the jar file to read from
     * @param overwrite whether or not to overwrite the local file
     * @param merge whether or not to merge the resource with the local file
     * @return a ConfigFile with the YAML format
     * @throws IOException if {@link ConfigFile#load(ConfigType, Path, String, boolean, boolean)} throws an exception
     */
    public static ConfigFile<ConfigurationNode> loadYaml(@Nonnull final Path destination,
                                                         @Nullable final String resource,
                                                         final boolean overwrite,
                                                         final boolean merge) throws IOException {
        return load(ConfigType.YAML, destination, resource, overwrite, merge);
    }

    private static <T extends ConfigurationNode> ConfigFile<T> load(@Nonnull final ConfigType type,
                                   @Nonnull final Path destination,
                                   @Nullable final String resource,
                                   final boolean overwrite,
                                   final boolean merge) throws IOException {
        if (overwrite) {
            Files.deleteIfExists(destination);
        }

        final boolean fileIsFresh;
        if (!Files.exists(destination)) {
            Files.createDirectories(destination.getParent());
            if (resource != null) {
                Files.copy(ConfigFile.class.getResourceAsStream(resource), destination);
            } else {
                Files.createFile(destination);
            }
            fileIsFresh = true;
        } else {
            fileIsFresh = false;
        }

        final ConfigurationLoader<T> loader = (ConfigurationLoader<T>) getDestinationLoader(type, destination);
        final T root = loader.load();

        if (!fileIsFresh && merge && resource != null) {
            final ConfigurationLoader<T> resourceLoader = (ConfigurationLoader<T>) getResourceLoader(type, resource);
            root.mergeValuesFrom(resourceLoader.load());
            loader.save(root);
        }

        return new ConfigFile<>(destination, loader, root);
    }

    private static ConfigurationLoader<? extends ConfigurationNode> getDestinationLoader(@Nonnull final ConfigType type,
                                                                                         @Nonnull final Path destination) {
        switch (type) {
            case HOCON:
                return HoconConfigurationLoader.builder().setPath(destination).build();
            case GSON:
                return GsonConfigurationLoader.builder().setPath(destination).build();
            case JSON:
                return JSONConfigurationLoader.builder().setPath(destination).build();
            default: // YAML
                return YAMLConfigurationLoader.builder().setPath(destination).build();
        }
    }

    private static ConfigurationLoader<? extends ConfigurationNode> getResourceLoader(@Nonnull final ConfigType type,
                                                                                      @Nonnull final String resource) {
        switch (type) {
            case HOCON:
                return HoconConfigurationLoader.builder().setURL(ConfigFile.class.getResource(resource)).build();
            case GSON:
                return GsonConfigurationLoader.builder().setURL(ConfigFile.class.getResource(resource)).build();
            case JSON:
                return JSONConfigurationLoader.builder().setURL(ConfigFile.class.getResource(resource)).build();
            default: // YAML
                return YAMLConfigurationLoader.builder().setURL(ConfigFile.class.getResource(resource)).build();
        }
    }

    private enum ConfigType {
        HOCON,
        GSON,
        JSON,
        YAML
    }
}
