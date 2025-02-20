package io.github.jinputprocessor.builder.base.types.number;

import io.github.jinputprocessor.builder.InputProcessorBuilder;
import io.github.jinputprocessor.builder.base.AbstractSanitizationBuilder;

/**
 * 
 * 
 *
 * @param <IN>
 * @param <T>
 * @param <B>
 */
public abstract class AbstractNumberSanitizationBuilder<IN, T extends Number, B extends InputProcessorBuilder<IN, T, B>, SELF extends AbstractNumberSanitizationBuilder<IN, T, B, SELF>>
	extends AbstractSanitizationBuilder<IN, T, B, SELF> {

	protected AbstractNumberSanitizationBuilder(B builder) {
		super(builder);
	}

	protected abstract boolean isEqualTo(T value, T ref);

	protected abstract boolean isLowerThan(T value, T ref);

	protected abstract boolean isGreaterThan(T value, T ref);

	protected boolean isLowerOrEqualTo(T value, T ref) {
		return isLowerThan(value, ref) || isEqualTo(value, ref);
	}

	protected boolean isGreaterOrEqualTo(T value, T ref) {
		return isGreaterThan(value, ref) || isEqualTo(value, ref);
	}

	// --------------------------------------------------------------------------------------------------------------------

	public SELF clamp(T minInclusive, T maxInclusive) {
		builder = builder.sanitize(value -> isLowerThan(value, minInclusive) ? minInclusive : isGreaterThan(value, maxInclusive) ? maxInclusive : value);
		return cast();
	}

}
