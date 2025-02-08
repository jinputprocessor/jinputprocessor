package io.github.jinputprocessor.builder.base.types.number;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;

public class IntegerInputProcessorBuilder<IN> extends AbstractNumberInputProcessorBuilder<IN, Integer, IntegerInputProcessorBuilder<IN>> {

	public IntegerInputProcessorBuilder(InputProcessor<IN, Integer> previous) {
		super(previous);
	}

	@Override
	protected IntegerInputProcessorBuilder<IN> newInstance(InputProcessor<IN, Integer> process) {
		return new IntegerInputProcessorBuilder<>(process);
	}

	public static IntegerInputProcessorBuilder<Integer> newInstance() {
		return new IntegerInputProcessorBuilder<>(InputProcessors.noOpProcessor());
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
