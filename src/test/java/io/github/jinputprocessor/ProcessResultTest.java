package io.github.jinputprocessor;

import io.github.jinputprocessor.ProcessFailure.ValidationFailure;
import io.github.jinputprocessor.result.InputProcessorFailureException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProcessResultTest {

	private static final ProcessResult<Object> SUCCESS_WITH_NULL = ProcessResult.success(null);
	private static final Object OBJECT = new Object();
	private static final ProcessResult<Object> SUCCESS_WITH_ANY = ProcessResult.success(OBJECT);

	private static final ProcessFailure ERROR = new ValidationFailure.ObjectIsNull();
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
			var error = new ValidationFailure.ObjectIsNull();
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
			Assertions.assertThatExceptionOfType(InputProcessorFailureException.class)
				.isThrownBy(() -> FAILURE_WITH_ANY.getOrThrow())
				.extracting(InputProcessorFailureException::getFailure).isEqualTo(ERROR);
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
		void test_equals_and_hash_code_onNull() {
			Assertions.assertThat(SUCCESS_WITH_NULL).isNotEqualTo(null);
			Assertions.assertThat(SUCCESS_WITH_ANY).isNotEqualTo(null);
			Assertions.assertThat(FAILURE_WITH_ANY).isNotEqualTo(null);
		}

		@Test
		void test_equals_and_hash_code_onOtherObjectType() {
			Assertions.assertThat(SUCCESS_WITH_NULL).isNotEqualTo(OBJECT);
			Assertions.assertThat(SUCCESS_WITH_ANY).isNotEqualTo(OBJECT);
			Assertions.assertThat(FAILURE_WITH_ANY).isNotEqualTo(OBJECT);
		}

		@Test
		void test_equals_and_hash_code_onSameObject() {
			Assertions.assertThat(SUCCESS_WITH_NULL).hasSameHashCodeAs(SUCCESS_WITH_NULL);
			Assertions.assertThat(SUCCESS_WITH_NULL).isEqualTo(SUCCESS_WITH_NULL);

			Assertions.assertThat(SUCCESS_WITH_ANY).hasSameHashCodeAs(SUCCESS_WITH_ANY);
			Assertions.assertThat(SUCCESS_WITH_ANY).isEqualTo(SUCCESS_WITH_ANY);

			Assertions.assertThat(FAILURE_WITH_ANY).hasSameHashCodeAs(FAILURE_WITH_ANY);
			Assertions.assertThat(FAILURE_WITH_ANY).isEqualTo(FAILURE_WITH_ANY);
		}

		@Test
		void test_equals_and_hash_code_onIdenticalObject() {
			var otherSuccessNull = ProcessResult.success(null);
			Assertions.assertThat(SUCCESS_WITH_NULL).hasSameHashCodeAs(otherSuccessNull);
			Assertions.assertThat(SUCCESS_WITH_NULL).isEqualTo(otherSuccessNull);

			var otherSuccessAnyObject = ProcessResult.success(OBJECT);
			Assertions.assertThat(SUCCESS_WITH_ANY).hasSameHashCodeAs(otherSuccessAnyObject);
			Assertions.assertThat(SUCCESS_WITH_ANY).isEqualTo(otherSuccessAnyObject);

			var otherFailure = ProcessResult.failure(ERROR);
			Assertions.assertThat(FAILURE_WITH_ANY).hasSameHashCodeAs(otherFailure);
			Assertions.assertThat(FAILURE_WITH_ANY).isEqualTo(otherFailure);

		}

		@Test
		void test_equals_and_hash_code_onNotSameSuccessValue() {
			Assertions.assertThat(SUCCESS_WITH_ANY).isNotEqualTo(SUCCESS_WITH_NULL);
		}

		@Test
		void test_equals_and_hash_code_onNotSameStatus() {
			Assertions.assertThat(SUCCESS_WITH_ANY).isNotEqualTo(FAILURE_WITH_ANY);
		}

		@Test
		void test_equals_and_hash_code_onNotSameFailure_() {
			var otherDifferent = ProcessResult.failure(new ValidationFailure.StringIsEmpty());
			Assertions.assertThat(FAILURE_WITH_ANY).isNotEqualTo(otherDifferent);
		}

	}

}
