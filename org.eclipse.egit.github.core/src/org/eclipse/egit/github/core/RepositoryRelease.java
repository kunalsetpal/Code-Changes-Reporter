/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/
package org.eclipse.egit.github.core;

import java.io.Serializable;

/**
 * Repository tag model class
 */
public class RepositoryRelease implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 1070566274663989459L;

	private String tagName;

	private int id;

	private String url;

	private String targetCommitish;

	//private Date createdAt

	/**
	 * @return tagName
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @param name
	 * @return this release
	 */
	public RepositoryRelease setTagName(String tagName) {
		this.tagName = tagName;
		return this;
	}

	/**
	 * @return id
	 */
	public int getid() {
		return id;
	}

	/**
	 * @param id
	 * @return this release
	 */
	public RepositoryRelease setId(int id) {
		this.id = id;
		return this;
	}

	/**
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 * @return this release
	 */
	public RepositoryRelease setUrl(String url) {
		this.url = url;
		return this;
	}

	/**
	 * @return commit
	 */
	public String gettargetCommitish() {
		return targetCommitish;
	}

	/**
	 * @param targetCommitish
	 * @return this release
	 */
	public RepositoryRelease setTargetCommitish(String targetCommitish) {
		this.targetCommitish = targetCommitish;
		return this;
	}
}
