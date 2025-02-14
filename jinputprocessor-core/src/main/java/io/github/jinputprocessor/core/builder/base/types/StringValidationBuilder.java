package io.github.jinputprocessor.core.builder.base.types;

import io.github.jinputprocessor.ProcessFailure.ValidationError;
import io.github.jinputprocessor.core.builder.base.AbstractValidationBuilder;

public class StringValidationBuilder<IN> extends AbstractValidationBuilder<IN, String, StringInputProcessorBuilder<IN>, StringValidationBuilder<IN>> {

	public StringValidationBuilder(StringInputProcessorBuilder<IN> builder) {
		super(builder);
	}

	@Override
	protected StringValidationBuilder<IN> cast() {
		return this;
	}

	public StringValidationBuilder<IN> canBeParsedToInteger() {
		builder = builder.validate(
			value -> {
				try {
					var i = Integer.parseInt(value);
					return null;
				} catch (NumberFormatException e) {
					return new ValidationError.StringNotParseableToInteger();
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

}
