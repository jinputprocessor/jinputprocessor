package io.github.jinputprocessor.builder.base.types.number;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;

public class LongInputProcessorBuilder<IN> extends AbstractNumberInputProcessorBuilder<IN, Long, LongInputProcessorBuilder<IN>> {

	public LongInputProcessorBuilder(InputProcessor<IN, Long> previous) {
		super(previous);
	}

	public static LongInputProcessorBuilder<Long> newInstance() {
		return new LongInputProcessorBuilder<>(InputProcessors.noOpProcessor());
	}

	@Override
	protected LongInputProcessorBuilder<IN> cast() {
		return this;
	}

	public LongSanitizationBuilder<IN> sanitize() {
		return new LongSanitizationBuilder<>(this);
	}

	public LongValidationBuilder<IN> validateThat() {
		return new LongValidationBuilder<>(this);
	}

	public IntegerInputProcessorBuilder<IN> mapToInt() {
		return mapTo(value -> value.intValue(), IntegerInputProcessorBuilder::new);
	}

}
