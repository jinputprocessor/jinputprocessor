package io.github.jinputprocessor.builder.base.types.number;

public class IntegerSanitizationBuilder<IN> extends AbstractNumberSanitizationBuilder<IN, Integer, IntegerInputProcessorBuilder<IN>, IntegerSanitizationBuilder<IN>> {

	public IntegerSanitizationBuilder(IntegerInputProcessorBuilder<IN> builder) {
		super(builder);
	}

	@Override
	protected IntegerSanitizationBuilder<IN> cast() {
		return this;
	}

	@Override
	protected boolean isEqualTo(Integer value, Integer ref) {
		return value.intValue() == ref.intValue();
	}

	@Override
	protected boolean isLowerThan(Integer value, Integer ref) {
		return value.intValue() < ref.intValue();
	}

	@Override
	protected boolean isGreaterThan(Integer value, Integer ref) {
		return value.intValue() > ref.intValue();
	}

}
