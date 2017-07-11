package tech.ferus.util.config.transformer;

import com.google.common.collect.Lists;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.List;

/**
 * Intended to transform convert a {@link ConfigurationNode} list
 * into a list of whatever is specified.
 *
 * @param <T> the type of list to convert to
 */
public class ListTransformer<T> implements Transformer<List<T>> {

    @SuppressWarnings("unchecked")
    @Override
    public List<T> transform(final ConfigurationNode node) throws TransformerException {
        if (!node.hasListChildren()) {
            throw new TransformerException("Node isn't a list format and cannot be transformed.");
        }

        final List<T> collect = Lists.newArrayList();
        try {
            for (final ConfigurationNode element : node.getChildrenList()) {
                collect.add((T) element.getValue());
            }
        } catch (final Exception e) {
            throw new TransformerException(e);
        }

        return collect;
    }
}
