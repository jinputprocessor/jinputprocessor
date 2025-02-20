package io.github.jinputprocessor.builder.base.types.number;

public class LongValidationBuilder<IN> extends AbstractNumberValidationBuilder<IN, Long, LongInputProcessorBuilder<IN>, LongValidationBuilder<IN>> {

	public LongValidationBuilder(LongInputProcessorBuilder<IN> builder) {
		super(builder);
	}

	@Override
	protected Long getZero() {
		return 0L;
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
