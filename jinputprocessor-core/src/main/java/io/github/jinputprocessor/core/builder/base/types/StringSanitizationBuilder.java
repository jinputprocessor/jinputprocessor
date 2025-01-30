package io.github.jinputprocessor.core.builder.base.types;

import io.github.jinputprocessor.core.builder.base.AbstractSanitizationBuilder;

public class StringSanitizationBuilder<IN> extends AbstractSanitizationBuilder<IN, String, StringInputProcessorBuilder<IN>, StringSanitizationBuilder<IN>> {

	public StringSanitizationBuilder(StringInputProcessorBuilder<IN> builder) {
		super(builder);
	}

	@Override
	protected StringSanitizationBuilder<IN> cast() {
		return this;
	}

	public StringSanitizationBuilder<IN> strip() {
		builder = builder.sanitize(String::strip);
		return this;
	}

}
