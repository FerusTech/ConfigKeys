package tech.ferus.util.config.transformer;

import com.google.common.collect.Sets;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.Set;

/**
 * Intended to transform convert a {@link ConfigurationNode} list
 * into a set of whatever is specified.
 *
 * @param <T> the type of set to convert to
 */
public class SetTransformer<T> implements Transformer<Set<T>> {

    @SuppressWarnings("unchecked")
    @Override
    public Set<T> transform(final ConfigurationNode node) throws TransformerException {
        if (!node.hasListChildren()) {
            throw new TransformerException("Node isn't a list format and cannot be transformed.");
        }

        final Set<T> collect = Sets.newHashSet();
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
