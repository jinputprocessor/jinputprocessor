package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessFailure.ValidationError;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProcessResultTest {

	private static final ProcessResult<Object> SUCCESS_WITH_NULL = ProcessResult.success(null);
	private static final Object OBJECT = new Object();
	private static final ProcessResult<Object> SUCCESS_WITH_ANY = ProcessResult.success(OBJECT);

	private static final ProcessFailure ERROR = new ValidationError.ObjectIsNull();
	private static final ProcessResult<Object> FAILURE_WITH_ANY = ProcessResult.failure(ERROR);

	@Nested
	class StaticFactory {

		@Test
		void sucess() {
			ProcessResultAssert.assertThat(ProcessResult.success(""))
				.isSuccessWithValue("");
		}

		@Test
		void failure() {
			var error = new ValidationError.ObjectIsNull();
			ProcessResultAssert.assertThat(ProcessResult.failure(error))
				.isFailure(error);
		}

	}

	@Nested
	class IsSuccess {

		@Test
		void when_success_with_null_then_true() {
			Assertions.assertThat(SUCCESS_WITH_NULL.isSuccess()).isTrue();
		}

		@Test
		void when_success_with_anyObject_then_true() {
			Assertions.assertThat(SUCCESS_WITH_ANY.isSuccess()).isTrue();
		}

		@Test
		void when_failure_with_anyReason_then_false() {
			Assertions.assertThat(FAILURE_WITH_ANY.isSuccess()).isFalse();
		}

	}

	@Nested
	class IsFailure {

		@Test
		void when_success_with_null_then_false() {
			Assertions.assertThat(SUCCESS_WITH_NULL.isFailure()).isFalse();
		}

		@Test
		void when_success_with_anyObject_then_false() {
			Assertions.assertThat(SUCCESS_WITH_ANY.isFailure()).isFalse();
		}

		@Test
		void when_failure_with_anyReason_then_true() {
			Assertions.assertThat(FAILURE_WITH_ANY.isFailure()).isTrue();
		}

	}

	@Nested
	class Get {

		@Test
		void when_success_with_null_then_null() {
			Assertions.assertThat(SUCCESS_WITH_NULL.get()).isNull();
		}

		@Test
		void when_success_with_anyObject_then_object() {
			Assertions.assertThat(SUCCESS_WITH_ANY.get()).isSameAs(OBJECT);
		}

		@Test
		void when_failure_with_anyReason_then_exception() {
			Assertions.assertThatIllegalStateException()
				.isThrownBy(() -> FAILURE_WITH_ANY.get());
		}

	}

	@Nested
	class GetOrThrow {

		@Test
		void when_success_with_null_then_null() {
			Assertions.assertThat(SUCCESS_WITH_NULL.getOrThrow()).isNull();
		}

		@Test
		void when_success_with_anyObject_then_object() {
			Assertions.assertThat(SUCCESS_WITH_ANY.getOrThrow()).isSameAs(OBJECT);
		}

		@Test
		void when_failure_with_anyReason_then_exception() {
			Assertions.assertThatIllegalArgumentException()
				.isThrownBy(() -> FAILURE_WITH_ANY.getOrThrow());
		}

	}

	@Nested
	class GetFailure {

		@Test
		void when_success_with_null_then_null() {
			Assertions.assertThatIllegalStateException()
				.isThrownBy(() -> SUCCESS_WITH_NULL.getFailure());
		}

		@Test
		void when_success_with_anyObject_then_object() {
			Assertions.assertThatIllegalStateException()
				.isThrownBy(() -> SUCCESS_WITH_ANY.getFailure());
		}

		@Test
		void when_failure_with_anyReason_then_exception() {
			Assertions.assertThat(FAILURE_WITH_ANY.getFailure()).isSameAs(ERROR);
		}

	}

	@Nested
	class AtProperty {

	}

	@Nested
	class ToString {

		@Test
		void when_success_with_null() {
			Assertions.assertThat(SUCCESS_WITH_NULL.toString())
				.isEqualTo("ProcessResult<Success>: null");
		}

		@Test
		void when_success_with_anyObject() {
			Assertions.assertThat(SUCCESS_WITH_ANY.toString())
				.isEqualTo("ProcessResult<Success>: " + OBJECT.toString());
		}

		@Test
		void when_failure_with_anyReason_() {
			Assertions.assertThat(FAILURE_WITH_ANY.toString())
				.isEqualTo("ProcessResult<Failure>: " + ERROR.toString());
		}

	}

	@Nested
	class Equality {

		@Test
		void when_onSameNull_then_false() {
			Assertions.assertThat(SUCCESS_WITH_NULL).isNotEqualTo(null);
			Assertions.assertThat(SUCCESS_WITH_ANY).isNotEqualTo(null);
			Assertions.assertThat(FAILURE_WITH_ANY).isNotEqualTo(null);
		}

		@Test
		void when_onOtherObjectType_then_false() {
			Assertions.assertThat(SUCCESS_WITH_NULL).isNotEqualTo(OBJECT);
			Assertions.assertThat(SUCCESS_WITH_ANY).isNotEqualTo(OBJECT);
			Assertions.assertThat(FAILURE_WITH_ANY).isNotEqualTo(OBJECT);
		}

		@Test
		void when_onSameObject_then_true() {
			Assertions.assertThat(SUCCESS_WITH_NULL.hashCode()).isEqualTo(SUCCESS_WITH_NULL.hashCode());
			Assertions.assertThat(SUCCESS_WITH_NULL).isEqualTo(SUCCESS_WITH_NULL);

			Assertions.assertThat(SUCCESS_WITH_ANY.hashCode()).isEqualTo(SUCCESS_WITH_ANY.hashCode());
			Assertions.assertThat(SUCCESS_WITH_ANY).isEqualTo(SUCCESS_WITH_ANY);

			Assertions.assertThat(FAILURE_WITH_ANY.hashCode()).isEqualTo(FAILURE_WITH_ANY.hashCode());
			Assertions.assertThat(FAILURE_WITH_ANY).isEqualTo(FAILURE_WITH_ANY);
		}

		@Test
		void when_onIdenticalObject_then_true() {
			var otherSuccessNull = ProcessResult.success(null);
			Assertions.assertThat(SUCCESS_WITH_NULL.hashCode()).isEqualTo(otherSuccessNull.hashCode());
			Assertions.assertThat(SUCCESS_WITH_NULL).isEqualTo(otherSuccessNull);

			var otherSuccessAnyObject = ProcessResult.success(OBJECT);
			Assertions.assertThat(SUCCESS_WITH_ANY.hashCode()).isEqualTo(otherSuccessAnyObject.hashCode());
			Assertions.assertThat(SUCCESS_WITH_ANY).isEqualTo(otherSuccessAnyObject);

			var otherFailure = ProcessResult.failure(ERROR);
			Assertions.assertThat(FAILURE_WITH_ANY.hashCode()).isEqualTo(otherFailure.hashCode());
			Assertions.assertThat(FAILURE_WITH_ANY).isEqualTo(otherFailure);

		}

		@Test
		void when_notSameSuccessValue_then_false() {
			Assertions.assertThat(SUCCESS_WITH_ANY).isNotEqualTo(SUCCESS_WITH_NULL);
		}

		@Test
		void when_notSameStatus_then_false() {
			Assertions.assertThat(SUCCESS_WITH_ANY).isNotEqualTo(FAILURE_WITH_ANY);
		}

		@Test
		void when_notSameFailure_then_false() {
			var otherDifferent = ProcessResult.failure(new ValidationError.StringIsEmpty());
			Assertions.assertThat(FAILURE_WITH_ANY).isNotEqualTo(otherDifferent);
		}

	}

}
