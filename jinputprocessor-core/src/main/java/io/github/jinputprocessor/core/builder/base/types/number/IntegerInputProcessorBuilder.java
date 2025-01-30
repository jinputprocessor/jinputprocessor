package io.github.jinputprocessor.core.builder.base.types.number;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.core.builder.InputProcessorBuilder;

public class IntegerInputProcessorBuilder<IN> extends AbstractNumberInputProcessorBuilder<IN, Integer, IntegerInputProcessorBuilder<IN>> {

	public IntegerInputProcessorBuilder(InputProcessor<IN, Integer> previous) {
		super(previous);
	}

	public static IntegerInputProcessorBuilder<Integer> newInstance() {
		return new IntegerInputProcessorBuilder<>(InputProcessorBuilder.noOpProcessor());
	}

	@Override
	protected IntegerInputProcessorBuilder<IN> cast() {
		return this;
	}

	public IntegerSanitizationBuilder<IN> sanitize() {
		return new IntegerSanitizationBuilder<>(this);
	}

	public IntegerValidationBuilder<IN> validateThat() {
		return new IntegerValidationBuilder<>(this);
	}

	public LongInputProcessorBuilder<IN> mapToLong() {
		return mapTo(value -> value.longValue(), LongInputProcessorBuilder::new);
	}

}
