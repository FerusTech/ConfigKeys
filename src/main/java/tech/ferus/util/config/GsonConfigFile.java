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
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An implementation of {@link ConfigFile} for the GSON specification.
 */
public class GsonConfigFile extends ConfigFile<ConfigurationNode> {

    /**
     * Constructs a new {@link GsonConfigFile}.
     *
     * @param file the location of the configuration file on disk
     * @param loader the active loaded for the configuration file on disk
     * @param root the loaded {@link ConfigurationNode} at the root level
     */
    public GsonConfigFile(@Nonnull final Path file,
                          @Nonnull final ConfigurationLoader<ConfigurationNode> loader,
                          @Nonnull final ConfigurationNode root) {
        super(file, loader, root);
    }

    /**
     * Creates a {@link GsonConfigFile}.
     *
     * @param file the location of the config file on disk
     * @return a new {@link GsonConfigFile}
     * @throws IOException if files cannot be properly deleted/created
     */
    public static GsonConfigFile load(final Path file) throws IOException {
        return load(file, null, false, false);
    }

    /**
     * Creates a {@link GsonConfigFile}.
     *
     * <p>Also merges a configuration file from classpath into the file on disk.</p>
     *
     * @param file the location of the config file on disk
     * @param resource the location of the resource in the classpath
     * @return a new {@link GsonConfigFile}
     * @throws IOException if files cannot be properly deleted/created
     */
    public static GsonConfigFile load(@Nonnull final Path file,
                                       @Nonnull final String resource) throws IOException {
        return load(file, resource, false, true);
    }

    /**
     * Creates a {@link GsonConfigFile}.
     *
     * <p>Also merges a configuration file from the classpath into the file on disk.</p>
     *
     * <p>May delete file on disk before copying resource.</p>
     *
     * @param file the location of the config file on disk
     * @param resource the location of the resource in the classpath
     * @param overwrite whether or not to delete the file on disk before copying resource
     * @return a new {@link GsonConfigFile}
     * @throws IOException if files cannot be properly deleted/created
     */
    public static GsonConfigFile load(@Nonnull final Path file,
                                       @Nonnull final String resource,
                                       final boolean overwrite) throws IOException {
        return load(file, resource, overwrite, true);
    }

    /**
     * Creates a {@link GsonConfigFile}.
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
     * @return a new {@link GsonConfigFile}
     * @throws IOException if files cannot be properly deleted/created
     */
    public static GsonConfigFile load(@Nonnull final Path file,
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

        final GsonConfigurationLoader fileLoader = GsonConfigurationLoader.builder().setPath(file).build();

        ConfigurationNode root;
        root = fileLoader.load();

        if (merge) {
            final GsonConfigurationLoader resourceLoader = GsonConfigurationLoader.builder().setURL(ConfigFile.class.getResource(resource)).build();
            root.mergeValuesFrom(resourceLoader.load());
            fileLoader.save(root);
        }

        return new GsonConfigFile(file, fileLoader, root);
    }
}
