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

	Path atPath(Path superPath);

	record RootPath() implements Path {

		@Override
		public String format() {
			return "";
		}

		@Override
		public Path atPath(Path superPath) {
			return superPath;
		}

	}

	record PropertyPath(Path parent, String property) implements Path {

		@Override
		public String format() {
			return parent.format() + "." + property;
		}

		@Override
		public Path atPath(Path superPath) {
			return new PropertyPath(parent.atPath(superPath), property);
		}

	}

	record IndexPath(Path parent, int index) implements Path {

		@Override
		public String format() {
			return parent.format() + "[" + index + "]";
		}

		@Override
		public Path atPath(Path superPath) {
			return new IndexPath(parent.atPath(superPath), index);
		}

	}

}
