package io.github.jinputprocessor.builder.base.types;

import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.builder.base.AbstractValidationBuilder;

public class StringValidationBuilder<IN> extends AbstractValidationBuilder<IN, String, StringInputProcessorBuilder<IN>, StringValidationBuilder<IN>> {

	public StringValidationBuilder(StringInputProcessorBuilder<IN> builder) {
		super(builder);
	}

	public StringValidationBuilder<IN> canBeParsedToInteger() {
		builder = builder.validate(
			value -> {
				try {
					var i = Integer.parseInt(value);
					return null;
				} catch (NumberFormatException e) {
					return new ValidationError.StringIsNotParseableToInteger();
				}
			}
		);
		return cast();
	}

	public StringValidationBuilder<IN> isNotEmpty() {
		builder = builder.validate(
			value -> value.isEmpty()
				? new ValidationError.StringIsEmpty()
				: null
		);
		return cast();
	}

	public StringValidationBuilder<IN> isMaxLength(int maxLength) {
		builder = builder.validate(
			value -> value.length() > maxLength
				? new ValidationError.StringIsTooLong(value.length(), maxLength)
				: null
		);
		return cast();
	}

}
