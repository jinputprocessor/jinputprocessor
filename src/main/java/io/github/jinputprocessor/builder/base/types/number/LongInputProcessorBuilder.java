package io.github.jinputprocessor.builder.base.types.number;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;

public class LongInputProcessorBuilder<IN> extends AbstractNumberInputProcessorBuilder<IN, Long, LongInputProcessorBuilder<IN>> {

	public LongInputProcessorBuilder(InputProcessor<IN, Long> previous) {
		super(previous);
	}

	@Override
	protected LongInputProcessorBuilder<IN> newInstance(InputProcessor<IN, Long> process) {
		return new LongInputProcessorBuilder<>(process);
	}

	public static LongInputProcessorBuilder<Long> newInstance() {
		return new LongInputProcessorBuilder<>(InputProcessors.noOpProcessor());
	}

	public LongSanitizationBuilder<IN> sanitize() {
		return new LongSanitizationBuilder<>(this);
	}

	public LongValidationBuilder<IN> validateThat() {
		return new LongValidationBuilder<>(this);
	}

	public IntegerInputProcessorBuilder<IN> mapToInt() {
		return map(value -> value.intValue(), IntegerInputProcessorBuilder::new);
	}

}
