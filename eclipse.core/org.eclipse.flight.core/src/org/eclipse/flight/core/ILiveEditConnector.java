/*******************************************************************************
 *  Copyright (c) 2013 GoPivotal, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *      GoPivotal, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.flight.core;

/**
 * @author Martin Lippert
 */
public interface ILiveEditConnector {

	String getConnectorID();
	
	void liveEditingStarted(String resourcePath);
	void liveEditingEvent(String resourcePath, int offset, int removeCount, String newText);

}
