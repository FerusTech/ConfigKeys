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
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An implementation of {@link ConfigFile} for the HOCON specification.
 */
public class HoconConfigFile extends ConfigFile<CommentedConfigurationNode> {

    /**
     * The HoconConfigFile logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HoconConfigFile.class);

    /**
     * Constructs a new {@link HoconConfigFile}.
     *
     * <p>Additionally, passes a {@link CommentedConfigurationNode}
     * for the loader and the root node.</p>
     *
     * @param file the location of the configuration file on disk
     * @param loader the active loaded for the configuration file on disk
     * @param root the loaded {@link CommentedConfigurationNode} at the root level
     */
    public HoconConfigFile(@Nonnull final Path file,
                           @Nonnull final ConfigurationLoader<CommentedConfigurationNode> loader,
                           @Nonnull final CommentedConfigurationNode root) {
        super(file, loader, root);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Overrides {@link ConfigFile#getNode(Object)} to return commented nodes.</p>
     */
    @Override public CommentedConfigurationNode getNode(final Object path) {
        return this.getNode().getNode(path);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Overrides {@link ConfigFile#getNode(Object...)} to return commented nodes.</p>
     */
    @Override public CommentedConfigurationNode getNode(final Object... path) {
        return this.getNode().getNode((Object[]) path);
    }

    /**
     * Creates a {@link HoconConfigFile}.
     *
     * @param file the location of the config file on disk
     * @return a new {@link HoconConfigFile}
     * @throws IOException if files cannot be properly deleted/created
     */
    public static HoconConfigFile load(final Path file) throws IOException {
        return load(file, null, false);
    }

    /**
     * Creates a {@link HoconConfigFile}.
     *
     * <p>Also merges a configuration file from classpath into the file on disk.</p>
     *
     * @param file the location of the config file on disk
     * @param resource the location of the resource in the classpath
     * @return a new {@link HoconConfigFile}
     * @throws IOException if files cannot be properly deleted/created
     */
    public static HoconConfigFile load(@Nonnull final Path file,
                                       @Nullable final String resource) throws IOException {
        return load(file, resource, false);
    }

    /**
     * Creates a {@link HoconConfigFile}.
     *
     * <p>Also merges a configuration file from the classpath into the file on disk.</p>
     *
     * <p>May delete file on disk before copying resource.</p>
     *
     * @param file the location of the config file on disk
     * @param resource the location of the resource in the classpath
     * @param overwrite whether or not to delete the file on disk before copying resource
     * @return a new {@link HoconConfigFile}
     * @throws IOException if files cannot be properly deleted/created
     */
    public static HoconConfigFile load(@Nonnull final Path file,
                                       @Nullable final String resource,
                                       final boolean overwrite) throws IOException {
        if (overwrite) {
            try {
                Files.deleteIfExists(file);
            } catch (final IOException e) {
                LOGGER.error("Failed to delete file: {}", file.toString(), e);
            }
        }

        if (!Files.exists(file)) {
            try {
                Files.createDirectories(file.getParent());
            } catch (final IOException e) {
                LOGGER.error("Failed to create parent directories of: {}", file.toString(), e);
            }

            if (resource != null && !resource.isEmpty()) {
                try {
                    Files.copy(HoconConfigFile.class.getResourceAsStream(resource), file);
                } catch (final IOException e) {
                    LOGGER.error("Failed to copy resource to: {}", file.toString(), e);
                }
            }
        }

        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(file).build();

        try {
            return new HoconConfigFile(file, loader, loader.load());
        } catch (final IOException e) {
            LOGGER.error("Failed to load configuration file at: {}", file.toString(), e);
        }

        return null;
    }

    /**
     * Creates a {@link HoconConfigFile}.
     *
     * <p>May merge a configuration file from the classpath into the file on disk.</p>
     *
     * <p>May delete the file on disk before copying resource.</p>
     *
     * <p>If accessing this method directly, keep in mind that passing <code>true</code>
     * for <code>overwrite</code> but <code>false</code> for <code>merge</code> is going
     * to result in a non-existent file on disk.</p>
     *
     * @param file the location of the config file on disk
     * @param resource the location of the resource in the classpath
     * @param overwrite whether or not to delete the file on disk before copying the resource
     * @param merge whether or not to merge the resource into the file on disk
     * @return a new {@link HoconConfigFile}
     * @throws IOException if files cannot be properly deleted/created
     */
    @Deprecated
    public static HoconConfigFile load(@Nonnull final Path file,
                                       @Nullable final String resource,
                                       final boolean overwrite,
                                       final boolean merge) throws IOException {
        if (overwrite) {
            Files.deleteIfExists(file);
        }

        if (!Files.exists(file)) {
            Files.createDirectories(file.getParent());
            Files.createFile(file);
        }

        final HoconConfigurationLoader fileLoader = HoconConfigurationLoader.builder().setPath(file).build();

        CommentedConfigurationNode root;
        root = fileLoader.load();

        if (merge) {
            final HoconConfigurationLoader resourceLoader = HoconConfigurationLoader.builder().setURL(ConfigFile.class.getResource(resource)).build();
            root.mergeValuesFrom(resourceLoader.load());
            fileLoader.save(root);
        }

        return new HoconConfigFile(file, fileLoader, root);
    }
}
