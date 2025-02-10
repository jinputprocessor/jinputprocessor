package io.github.jinputprocessor;

public sealed interface Path {

	String format();

	static Path atRoot() {
		return new RootPath();
	}

	static Path createPropertyPath(String property) {
		return atRoot().atProperty(property);
	}

	static Path createIndexPath(int index) {
		return atRoot().atIndex(index);
	}

	default Path atProperty(String property) {
		return new PropertyPath(this, property);
	}

	default Path atIndex(int index) {
		return new IndexPath(this, index);
	}

	record RootPath() implements Path {

		@Override
		public String format() {
			return "";
		}

	}

	record PropertyPath(Path parent, String property) implements Path {

		@Override
		public String format() {
			return parent.format() + "." + property;
		}

	}

	record IndexPath(Path parent, int index) implements Path {

		@Override
		public String format() {
			return parent.format() + "[" + index + "]";
		}

	}

}
