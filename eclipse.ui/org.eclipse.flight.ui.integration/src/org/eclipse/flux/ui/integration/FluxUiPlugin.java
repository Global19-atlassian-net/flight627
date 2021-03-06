/*******************************************************************************
 * Copyright (c) 2013, 2014 Pivotal Software, Inc. and others.
 * All rights reserved. This program and the accompanying materials are made 
 * available under the terms of the Eclipse Public License v1.0 
 * (https://www.eclipse.org/legal/epl-v10.html), and the Eclipse Distribution 
 * License v1.0 (https://www.eclipse.org/org/documents/edl-v10.html). 
 *
 * Contributors:
 *     Pivotal Software, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.flux.ui.integration;

import org.eclipse.core.resources.IProject;
import org.eclipse.flux.core.IRepositoryListener;
import org.eclipse.flux.core.LiveEditCoordinator;
import org.eclipse.flux.core.Repository;
import org.eclipse.flux.ui.integration.handlers.LiveEditConnector;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Martin Lippert
 */
public class FluxUiPlugin extends AbstractUIPlugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.flux.ui.integration"; //$NON-NLS-1$

	// The shared instance
	private static FluxUiPlugin plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		org.eclipse.flux.core.Activator.getDefault().getRepository()
				.addRepositoryListener(new IRepositoryListener() {
					@Override
					public void projectDisconnected(IProject project) {
						updateProjectLabel(project);
					}

					@Override
					public void projectConnected(IProject project) {
						updateProjectLabel(project);
					}
				});

		if (Boolean.getBoolean("flux-eclipse-editor-connect")) {
			Repository repository = org.eclipse.flux.core.Activator.getDefault().getRepository();
			LiveEditCoordinator liveEditCoordinator = org.eclipse.flux.core.Activator.getDefault().getLiveEditCoordinator();
			new LiveEditConnector(liveEditCoordinator, repository);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static FluxUiPlugin getDefault() {
		return plugin;
	}

	protected static void updateProjectLabel(final IProject project) {
		final CloudProjectDecorator projectDecorator = CloudProjectDecorator
				.getInstance();
		if (projectDecorator != null) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					projectDecorator
							.fireLabelProviderChanged(new LabelProviderChangedEvent(
									projectDecorator, project));
				}
			});
		}
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	@Override
	public void earlyStartup() {
	}
}
