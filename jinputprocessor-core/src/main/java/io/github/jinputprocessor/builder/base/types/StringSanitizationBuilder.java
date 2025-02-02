package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.builder.base.AbstractSanitizationBuilder;

public class StringSanitizationBuilder<IN> extends AbstractSanitizationBuilder<IN, String, StringInputProcessorBuilder<IN>, StringSanitizationBuilder<IN>> {

	public StringSanitizationBuilder(StringInputProcessorBuilder<IN> builder) {
		super(builder);
	}

	public StringSanitizationBuilder<IN> strip() {
		builder = builder.sanitize(String::strip);
		return cast();
	}

	public StringSanitizationBuilder<IN> toUpperCase() {
		builder = builder.sanitize(String::toUpperCase);
		return cast();
	}

	public StringSanitizationBuilder<IN> toLowerCase() {
		builder = builder.sanitize(String::toLowerCase);
		return cast();
	}

}
