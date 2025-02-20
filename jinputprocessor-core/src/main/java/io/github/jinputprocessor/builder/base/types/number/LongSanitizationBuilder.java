package io.github.jinputprocessor.builder.base.types.number;

public class LongSanitizationBuilder<IN> extends AbstractNumberSanitizationBuilder<IN, Long, LongInputProcessorBuilder<IN>, LongSanitizationBuilder<IN>> {

	public LongSanitizationBuilder(LongInputProcessorBuilder<IN> builder) {
		super(builder);
	}

	@Override
	protected boolean isEqualTo(Long value, Long ref) {
		return value.longValue() == ref.longValue();
	}

	@Override
	protected boolean isLowerThan(Long value, Long ref) {
		return value.longValue() < ref.longValue();
	}

	@Override
	protected boolean isGreaterThan(Long value, Long ref) {
		return value.longValue() > ref.longValue();
	}

}
