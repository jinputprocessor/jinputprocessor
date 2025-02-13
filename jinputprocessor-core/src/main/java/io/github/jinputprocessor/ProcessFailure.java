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
	 * A failure because of a validation issue. 
	 * It is again a sealed interface, allowing switch pattern matching for a complete failure handling.
	 * 
	 * @see ValidationProcessor
	 */
	sealed interface ValidationFailure extends ProcessFailure {

		// ===========================================================================
		// CUSTOM

		/**
		 * Extend this interface to declare your own custom validation failures.
		 * See online documentation and examples for more.
		 */
		non-sealed interface CustomValidationFailure extends ValidationFailure {

		}

		// ===========================================================================
		// OBJECT

		record ObjectIsNull() implements ValidationFailure {
		}

		record ObjectIsNotInstanceOf(Class<?> clazz) implements ValidationFailure {
		}

		// ===========================================================================
		// STRING

		record StringIsEmpty() implements ValidationFailure {
		}

		record StringIsTooLong(int currentLength, int maxLength) implements ValidationFailure {
		}

		record StringIsNotParseableToInteger() implements ValidationFailure {
		}

		// ===========================================================================
		// NUMBER

		record NumberIsNotGreaterThan<T extends Number>(T ref) implements ValidationFailure {
		}

		record NumberIsNotGreaterOrEqualTo<T extends Number>(T ref) implements ValidationFailure {
		}

		// ===========================================================================
		// COLLECTION

		record CollectionIsEmpty() implements ValidationFailure {

		}

	}

}
