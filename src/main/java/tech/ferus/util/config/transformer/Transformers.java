package tech.ferus.util.config.transformer;

/**
 * Contains a bunch of basic transformers.
 */
public final class Transformers {

    // Basic List Transformers
    public static final ListTransformer<String> STRING_LIST = new ListTransformer<>();
    public static final ListTransformer<Boolean> BOOLEAN_LIST = new ListTransformer<>();
    public static final ListTransformer<Integer> INTEGER_LIST = new ListTransformer<>();
    public static final ListTransformer<Float> FLOAT_LIST = new ListTransformer<>();
    public static final ListTransformer<Double> DOUBLE_LIST = new ListTransformer<>();
    public static final ListTransformer<Long> LONG_LIST = new ListTransformer<>();

    // Basic Set Transformers
    public static final SetTransformer<String> STRING_SET = new SetTransformer<>();
    public static final SetTransformer<Boolean> BOOLEAN_SET = new SetTransformer<>();
    public static final SetTransformer<Integer> INTEGER_SET = new SetTransformer<>();
    public static final SetTransformer<Float> FLOAT_SET = new SetTransformer<>();
    public static final SetTransformer<Double> DOUBLE_SET = new SetTransformer<>();
    public static final SetTransformer<Long> LONG_SET = new SetTransformer<>();

    // Basic Map Transformers
    public static final MapTransformer<String, String> STRING_MAP = new MapTransformer<>();
    public static final MapTransformer<String, Boolean> BOOLEAN_MAP = new MapTransformer<>();
    public static final MapTransformer<String, Integer> INTEGER_MAP = new MapTransformer<>();
    public static final MapTransformer<String, Float> FLOAT_MAP = new MapTransformer<>();
    public static final MapTransformer<String, Double> DOUBLE_MAP = new MapTransformer<>();
    public static final MapTransformer<String, Long> LONG_MAP = new MapTransformer<>();
}
