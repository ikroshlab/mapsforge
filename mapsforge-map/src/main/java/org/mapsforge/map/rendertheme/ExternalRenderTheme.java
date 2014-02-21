/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.map.rendertheme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * An ExternalRenderTheme allows for customizing the rendering style of the map via an XML file.
 */
public class ExternalRenderTheme implements XmlRenderTheme {
	private static final long serialVersionUID = 1L;

	private final List<String> categories;
	private final long lastModifiedTime;
	private final File renderThemeFile;

	/**
	 * @param renderThemeFile
	 *            the XML render theme file.
	 * @throws FileNotFoundException
	 *             if the file does not exist or cannot be read.
	 */
	public ExternalRenderTheme(File renderThemeFile) throws FileNotFoundException {
		this(renderThemeFile, null);
	}

	/**
	 * @param renderThemeFile
	 *            the XML render theme file.
	 * @param categories the categories to render with this render theme.
	 * @throws FileNotFoundException
	 *             if the file does not exist or cannot be read.
	 */
	public ExternalRenderTheme(File renderThemeFile, List<String> categories) throws FileNotFoundException {
		if (!renderThemeFile.exists()) {
			throw new FileNotFoundException("file does not exist: " + renderThemeFile.getAbsolutePath());
		} else if (!renderThemeFile.isFile()) {
			throw new FileNotFoundException("not a file: " + renderThemeFile.getAbsolutePath());
		} else if (!renderThemeFile.canRead()) {
			throw new FileNotFoundException("cannot read file: " + renderThemeFile.getAbsolutePath());
		}

		this.categories = categories;
		if (this.categories != null) {
			Collections.sort(this.categories);
		}

		this.lastModifiedTime = renderThemeFile.lastModified();
		if (this.lastModifiedTime == 0L) {
			throw new FileNotFoundException("cannot read last modified time: " + renderThemeFile.getAbsolutePath());
		}
		this.renderThemeFile = renderThemeFile;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (!(obj instanceof ExternalRenderTheme)) {
			return false;
		}
		ExternalRenderTheme other = (ExternalRenderTheme) obj;
		if (this.lastModifiedTime != other.lastModifiedTime) {
			return false;
		}
		if (this.renderThemeFile == null) {
			if (other.renderThemeFile != null) {
				return false;
			}
		} else if (!this.renderThemeFile.equals(other.renderThemeFile)) {
			return false;
		}
		if ((this.categories == null && other.categories != null) ||
				(this.categories != null && (other.categories == null || !this.categories.equals(other.categories)))) {
			return false;
		}
		return true;
	}

	@Override
	public List<String> getCategories() {
		return this.categories;
	}

	@Override
	public String getRelativePathPrefix() {
		return this.renderThemeFile.getParent();
	}

	@Override
	public InputStream getRenderThemeAsStream() throws FileNotFoundException {
		return new FileInputStream(this.renderThemeFile);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.lastModifiedTime ^ (this.lastModifiedTime >>> 32));
		result = prime * result + ((this.renderThemeFile == null) ? 0 : this.renderThemeFile.hashCode());
		if (this.categories != null) {
			result = prime * result + (this.categories.hashCode());
		}
		return result;
	}
}
