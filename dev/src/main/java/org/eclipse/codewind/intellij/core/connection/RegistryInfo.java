/*******************************************************************************
 * Copyright (c) 2019, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.intellij.core.connection;

import org.json.JSONObject;

public class RegistryInfo extends JSONObjectResult {
	
	public static final String ADDRESS_KEY = "address";
	public static final String USERNAME_KEY = "username";
	
	public RegistryInfo(JSONObject obj) {
		super(obj, "registry");
	}

	public String getAddress() {
		return getString(ADDRESS_KEY);
	}
	
	public String getUsername() {
		return getString(USERNAME_KEY);
	}
}
