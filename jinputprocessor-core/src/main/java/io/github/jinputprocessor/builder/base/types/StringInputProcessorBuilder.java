package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.InputProcessors;
import io.github.jinputprocessor.builder.base.AbstractInputProcessorBuilder;
import io.github.jinputprocessor.builder.base.types.number.IntegerInputProcessorBuilder;
import io.github.jinputprocessor.builder.base.types.number.LongInputProcessorBuilder;

public class StringInputProcessorBuilder<IN> extends AbstractInputProcessorBuilder<IN, String, StringInputProcessorBuilder<IN>> {

	public StringInputProcessorBuilder(InputProcessor<IN, String> previous) {
		super(previous);
	}

	public static StringInputProcessorBuilder<String> newInstance() {
		return new StringInputProcessorBuilder<>(InputProcessors.noOpProcessor());
	}

	public StringSanitizationBuilder<IN> sanitize() {
		return new StringSanitizationBuilder<>(this);
	}

	public StringValidationBuilder<IN> validateThat() {
		return new StringValidationBuilder<>(this);
	}

	public IntegerInputProcessorBuilder<IN> mapToInteger() {
		return mapTo(Integer::parseInt, IntegerInputProcessorBuilder::new);
	}

	public LongInputProcessorBuilder<IN> mapToLong() {
		return mapTo(Long::parseLong, LongInputProcessorBuilder::new);
	}

}
