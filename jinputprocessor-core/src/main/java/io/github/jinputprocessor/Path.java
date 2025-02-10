package io.github.jinputprocessor;

import java.util.Objects;

public sealed interface Path {

	/**
	 * Format this path into a human readable form: "lastName", "myList[0]", etc.
	 * 
	 * @return
	 */
	String format();

	/**
	 * Create a new path at root (no property, no index).
	 * 
	 * @return
	 */
	static Path atRoot() {
		return new RootPath();
	}

	static Path createPropertyPath(String property) {
		return new PropertyPath(atRoot(), property);
	}

	static Path createIndexPath(int index) {
		return new IndexPath(atRoot(), index);
	}

	default Path atProperty(String property) {
		return atPath(createPropertyPath(property));
	}

	default Path atIndex(int index) {
		return atPath(createIndexPath(index));
	}

	Path atPath(Path superPath);

	record RootPath() implements Path {

		@Override
		public String format() {
			return "value";
		}

		@Override
		public Path atPath(Path superPath) {
			return superPath;
		}

	}

	record PropertyPath(Path parent, String property) implements Path {

		public PropertyPath(Path parent, String property) {
			this.parent = Objects.requireNonNull(parent, "parent path cannot be null");
			this.property = Objects.requireNonNull(property, "property path cannot be null");
		}

		@Override
		public String format() {
			if (parent instanceof RootPath) {
				return property;
			}
			return parent.format() + "." + property;
		}

		@Override
		public Path atPath(Path superPath) {
			return new PropertyPath(parent.atPath(superPath), property);
		}

	}

	record IndexPath(Path parent, int index) implements Path {

		public IndexPath(Path parent, int index) {
			this.parent = Objects.requireNonNull(parent, "parent path cannot be null");
			if (index < 0) {
				throw new IllegalArgumentException("index path cannot be negative: " + index);
			}
			this.index = index;
		}

		@Override
		public String format() {
			if (parent instanceof RootPath) {
				return "index [" + index + "]";
			}
			return parent.format() + "[" + index + "]";
		}

		@Override
		public Path atPath(Path superPath) {
			return new IndexPath(parent.atPath(superPath), index);
		}

	}

}
