package io.github.jinputprocessor;

import java.util.Collection;
import java.util.Map;

public sealed interface ProcessFailure {

	/**
	 * 
	 * @param value The value of the input, at the time the exception was thrown
	 * @param exception		The exception
	 *
	 */
	record UnexpectedException(Object value, Throwable exception) implements ProcessFailure {
	}

	/**
	 * 
	 * 
	 *
	 */
	record IndexedFailure(int index, ProcessFailure failure) implements ProcessFailure {
	}

	/**
	 * 
	 * 
	 *
	 */
	record MultiFailure(Collection<? extends ProcessFailure> failures) implements ProcessFailure {
	}

	/**
	 * 
	 * 
	 *
	 */
	sealed interface ValidationError extends ProcessFailure {

		/*
		 * ===========================================================================
		 * CUSTOM
		 * ===========================================================================
		 */

		record CustomError(Object errorKey, Map<String, Object> args) implements ValidationError {

			public CustomError(Object errorKey) {
				this(errorKey, Map.of());
			}

			public CustomError(Object errorKey, Map<String, Object> args) {
				this.errorKey = errorKey;
				this.args = Map.copyOf(args);
			}

		}

		/*
		 * ===========================================================================
		 * OBJECT
		 * ===========================================================================
		 */

		record ObjectIsNull() implements ValidationError {
		}

		/*
		 * ===========================================================================
		 * STRING
		 * ===========================================================================
		 */

		record StringIsEmpty() implements ValidationError {
		}

		record StringNotParseableToInteger() implements ValidationError {
		}

		/*
		 * ===========================================================================
		 * NUMBER
		 * ===========================================================================
		 */

		record NumberMustBeGreaterThan(Number ref) implements ValidationError {
		}

		record NumberMustBeGreaterOrEqualTo(Number ref) implements ValidationError {
		}

	}

}
