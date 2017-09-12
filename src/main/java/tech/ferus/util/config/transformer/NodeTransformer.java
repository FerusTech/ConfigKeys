package tech.ferus.util.config.transformer;

import ninja.leaping.configurate.ConfigurationNode;

/**
 * Intended to provide a simple pass-through. ConfigurationNodes
 * are returned simply as they're sent. This is required due to how
 * ConfigKey deals with transformers and object retrieval.
 */
public class NodeTransformer implements Transformer<ConfigurationNode> {

    @Override
    public ConfigurationNode transform(final ConfigurationNode node) throws TransformerException {
        return node;
    }
}
