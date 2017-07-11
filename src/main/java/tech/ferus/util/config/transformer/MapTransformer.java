package tech.ferus.util.config.transformer;

import com.google.common.collect.Maps;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.Map;

/**
 * Intended to transform convert a {@link ConfigurationNode} map
 * into a map of whatever is specified.
 *
 * @param <K> the type of key to convert to
 * @param <V> the type of value to convert to
 */
public class MapTransformer<K, V> implements Transformer<Map<K, V>> {

    @SuppressWarnings("unchecked")
    @Override
    public Map<K, V> transform(final ConfigurationNode node) throws TransformerException {
        if (!node.hasMapChildren()) {
            throw new TransformerException("Node isn't in a map format and cannot be transformed.");
        }

        final Map<K, V> collect = Maps.newHashMap();
        try {
            for (final Map.Entry<Object, ? extends ConfigurationNode> entry : node.getChildrenMap().entrySet()) {
                collect.put((K) entry.getKey(), (V) entry.getValue().getValue());
            }
        } catch (final Exception e) {
            throw new TransformerException(e);
        }

        return collect;
    }
}
