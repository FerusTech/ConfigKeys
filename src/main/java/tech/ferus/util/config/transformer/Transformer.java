package tech.ferus.util.config.transformer;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * This interface can be implemented to create any number of transformers.
 *
 * @param <T> the type of value to give back after converting
 */
public interface Transformer<T> {

    /**
     * Transforms a given node into a specified format.
     *
     * @param node the given node to transform
     * @return the transformed values from the node
     * @throws TransformerException if an exception occurs during transformation.
     */
    T transform(final ConfigurationNode node) throws TransformerException;
}
