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

public class ConfigKey<T> {

    @Nonnull private final String[] key;

    private ConfigKey(@Nonnull final String key) {
        this.key = new String[] {key};
    }

    private ConfigKey(@Nonnull final String[] key) {
        this.key = key;
    }

    @Nullable public T get() {
        return this.get(getDefaultConfig(), null);
    }

    @Nullable public T get(@Nonnull final ConfigFile config) {
        return this.get(config, null);
    }

    @Nullable public T get(@Nullable final T def) {
        return this.get(getDefaultConfig(), def);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public T get(@Nullable final ConfigFile config, @Nullable final T def) {
        if (config == null) {
            throw new IllegalStateException("Attempted to get \"" + String.join(".", this.key) + "\" from null ConfigFile.");
        }

        try {
            return (T) config.getNode(this.key).getValue(def);
        } catch (final ClassCastException e) {
            throw new IllegalArgumentException("Improper value type for \"{}\"!", e);
        }
    }

    public void set(@Nullable final T value) throws IOException {
        this.set(getDefaultConfig(), value);
    }

    public void set(@Nullable final ConfigFile config, @Nullable final T value) throws IOException {
        if (config == null) {
            throw new IllegalStateException("Attempted to set \"" + String.join(".", this.key) + "\" to a null ConfigFile.");
        }

        config.getNode(this.key).setValue(value);
        config.save();
    }

    public static <T> ConfigKey<T> of(final String key) {
        return new ConfigKey<>(key);
    }

    public static <T> ConfigKey<T> of(final String... key) {
        return new ConfigKey<>(key);
    }
}
