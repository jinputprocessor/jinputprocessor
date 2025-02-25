package io.github.jinputprocessor.builder.base.types.number;

import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.builder.InputProcessorBuilder;
import io.github.jinputprocessor.builder.base.AbstractValidationBuilder;

/**
 * 
 * 
 *
 * @param <IN>
 * @param <T>
 * @param <B>
 */
public abstract class AbstractNumberValidationBuilder<IN, T extends Number, B extends InputProcessorBuilder<IN, T, B>, SELF extends AbstractNumberValidationBuilder<IN, T, B, SELF>>
	extends AbstractValidationBuilder<IN, T, B, SELF> {

	protected AbstractNumberValidationBuilder(B builder) {
		super(builder);
	}

	protected abstract T getZero();

	protected abstract boolean isEqualTo(T value, T ref);

	protected abstract boolean isLowerThan(T value, T ref);

	protected abstract boolean isGreaterThan(T value, T ref);

	protected boolean isLowerOrEqualTo(T value, T ref) {
		return isLowerThan(value, ref) || isEqualTo(value, ref);
	}

	protected boolean isGreaterOrEqualTo(T value, T ref) {
		return isGreaterThan(value, ref) || isEqualTo(value, ref);
	}

	public boolean isPositive(T value) {
		return isGreaterThan(value, getZero());
	}

	// --------------------------------------------------------------------------------------------------------------------

	public SELF isGreaterThan(T ref) {
		builder = builder.validate(
			value -> !isGreaterThan(value, ref)
				? new ValidationFailure.NumberIsLowerOrEqualTo<>(ref)
				: null
		);
		return cast();
	}

	public SELF isGreaterOrEqualTo(T ref) {
		builder = builder.validate(
			value -> !isGreaterOrEqualTo(value, ref)
				? new ValidationFailure.NumberIsLowerThan<>(ref)
				: null
		);
		return cast();
	}

}
