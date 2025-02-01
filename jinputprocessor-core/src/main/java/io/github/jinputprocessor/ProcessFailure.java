package io.github.jinputprocessor;

import io.github.jinputprocessor.processor.ValidationProcessor;
import java.util.Collection;

/**
 * An Interface regrouping all possible failures of a {@link ProcessResult}.
 * 
 * As this interface is sealed, it allows to use switch pattern matching feature with all case covered at compilation time.
 */
public sealed interface ProcessFailure {

	/**
	 * An unexpected exception that occured within an processor, like a {@link NullPointerException}.
	 * 
	 * @param value 		The value of the input, at the time the exception was thrown 
	 * 						(not necessiraly the initial input value, it may have been transformed/sanitized in between)
	 * @param exception		The exception
	 */
	record UnexpectedException(Object value, Throwable exception) implements ProcessFailure {
	}

	/**
	 * A failure that occured on an named variable/attribute.
	 */
	record NamedFailure(String name, ProcessFailure failure) implements ProcessFailure {
	}

	/**
	 * A failure that occurred at a particular index (array, sequenced collection, etc.).
	 */
	record IndexedFailure(int index, ProcessFailure failure) implements ProcessFailure {
	}

	/**
	 * A failure that regroups multiple other failures (collection, etc.).
	 */
	record MultiFailure(Collection<? extends ProcessFailure> failures) implements ProcessFailure {
	}

	/**
	 * A failure representing a validation error. 
	 * It is again a sealed interface, allowing switch pattern matching for a complete error handling.
	 * 
	 * @see ValidationProcessor
	 */
	sealed interface ValidationError extends ProcessFailure {

		// ===========================================================================
		// CUSTOM

		/**
		 * Extend this interface to declare your own custom validation errors.
		 * See documentation and examples for more.
		 */
		non-sealed interface CustomValidationError extends ValidationError {

		}

		// ===========================================================================
		// OBJECT

		record ObjectIsNull() implements ValidationError {
		}

		// ===========================================================================
		// STRING

		record StringIsEmpty() implements ValidationError {
		}

		record StringIsTooLong(int currentLength, int maxLength) implements ValidationError {
		}

		record StringIsNotParseableToInteger() implements ValidationError {
		}

		// ===========================================================================
		// NUMBER

		record NumberIsNotGreaterThan<T extends Number>(T ref) implements ValidationError {
		}

		record NumberIsNotGreaterOrEqualTo<T extends Number>(T ref) implements ValidationError {
		}

	}

}
