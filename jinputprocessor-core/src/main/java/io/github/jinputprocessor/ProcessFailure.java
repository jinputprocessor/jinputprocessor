package io.github.jinputprocessor;

import java.util.Collection;

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

		non-sealed interface CustomValidationError extends ValidationError {

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

		record StringTooLong(int currentLength, int maxLength) implements ValidationError {

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
