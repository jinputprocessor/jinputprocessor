package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.builder.base.AbstractSanitizationBuilder;
import java.util.Objects;

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

	public StringSanitizationBuilder<IN> prefix(String prefix) {
		Objects.requireNonNull(prefix, "prefix cannot be null");
		builder = builder.sanitize(value -> value.startsWith(prefix) ? value : prefix + value);
		return cast();
	}

	public StringSanitizationBuilder<IN> suffix(String suffix) {
		Objects.requireNonNull(suffix, "suffix cannot be null");
		builder = builder.sanitize(value -> value.endsWith(suffix) ? value : value + suffix);
		return cast();
	}

}
