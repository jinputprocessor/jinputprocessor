package io.github.jinputprocessor.core.builder.base.types.number;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.core.builder.InputProcessorBuilder;

public class LongInputProcessorBuilder<IN> extends AbstractNumberInputProcessorBuilder<IN, Long, LongInputProcessorBuilder<IN>> {

	public LongInputProcessorBuilder(InputProcessor<IN, Long> previous) {
		super(previous);
	}

	public static LongInputProcessorBuilder<Long> newInstance() {
		return new LongInputProcessorBuilder<>(InputProcessorBuilder.noOpProcessor());
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
