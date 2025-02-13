package io.github.jinputprocessor;

import io.github.jinputprocessor.processor.ValidationProcessor;
import java.util.List;

/**
 * An Interface regrouping all possible failures of a {@link ProcessResult}.
 * 
 * As this interface is sealed, it allows to use switch pattern matching feature with all case covered at compilation time.
 */
public sealed interface ProcessFailure {

	default ProcessFailure atPath(Path path) {
		return new PathFailure(path, this);
	}

	default Path getPath() {
		return Path.root();
	}

	/**
	 * An unexpected exception that occured within an processor, like a {@link NullPointerException}.
	 * 
	 * @param value 		The value of the input, at the time the exception was thrown 
	 * 						(not necessiraly the initial input value, it may have been transformed/sanitized in between)
	 * @param exception		The exception
	 */
	record UnexpectedException(Object value, Throwable exception) implements ProcessFailure {

//		@Override
//		public final String toString() {
//			if (exception != null) {
//				exception.printStackTrace();
//			}
//			return "UnexpectedException / value=" + value + " / exception=" + exception;
//		}

	}

	/**
	 * A failure that occured within a given path.
	 */
	record PathFailure(Path path, ProcessFailure failure) implements ProcessFailure {

		@Override
		public ProcessFailure atPath(Path superPath) {
			return new PathFailure(path.atPath(superPath), failure);
		}

		@Override
		public Path getPath() {
			return path;
		}

	}

	/**
	 * A failure that regroups multiple other failures (collection, etc.).
	 */
	record MultiFailure(List<? extends ProcessFailure> failures) implements ProcessFailure {

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

		record ObjectIsNotInstanceOf(Class<?> clazz) implements ValidationError {
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

		// ===========================================================================
		// COLLECTION

		record CollectionIsEmpty() implements ValidationError {

		}

	}

}
