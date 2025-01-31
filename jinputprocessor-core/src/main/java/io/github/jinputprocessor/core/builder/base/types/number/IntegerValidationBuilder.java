package io.github.jinputprocessor.core.builder.base.types.number;

public class IntegerValidationBuilder<IN> extends AbstractNumberValidationBuilder<IN, Integer, IntegerInputProcessorBuilder<IN>, IntegerValidationBuilder<IN>> {

	public IntegerValidationBuilder(IntegerInputProcessorBuilder<IN> builder) {
		super(builder);
	}

	@Override
	protected IntegerValidationBuilder<IN> cast() {
		return this;
	}

	@Override
	protected Integer getZero() {
		return 0;
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
