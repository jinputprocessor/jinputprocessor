package io.github.jinputprocessor.core.builder.base.types;

import io.github.jinputprocessor.InputProcessor;
import io.github.jinputprocessor.core.builder.InputProcessorBuilder;
import io.github.jinputprocessor.core.builder.base.AbstractInputProcessorBuilder;
import io.github.jinputprocessor.core.builder.base.types.number.IntegerInputProcessorBuilder;
import io.github.jinputprocessor.core.builder.base.types.number.LongInputProcessorBuilder;

public class StringInputProcessorBuilder<IN> extends AbstractInputProcessorBuilder<IN, String, StringInputProcessorBuilder<IN>> {

	public StringInputProcessorBuilder(InputProcessor<IN, String> previous) {
		super(previous);
	}

	public static StringInputProcessorBuilder<String> newInstance() {
		return new StringInputProcessorBuilder<>(InputProcessorBuilder.noOpProcessor());
	}

	@Override
	protected StringInputProcessorBuilder<IN> cast() {
		return this;
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
