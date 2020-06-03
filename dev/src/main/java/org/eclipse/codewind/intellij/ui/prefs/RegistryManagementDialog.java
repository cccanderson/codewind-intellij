/*******************************************************************************
 * Copyright (c) 2019 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.ui.internal.prefs;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.codewind.intellij.core.connection.CodewindConnection;
import org.eclipse.codewind.intellij.core.connection.ImagePushRegistryInfo;
import org.eclipse.codewind.intellij.core.connection.RegistryInfo;
import org.eclipse.codewind.intellij.ui.messages.CodewindUIBundle;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class RegistryManagementDialog extends TitleAreaDialog {
	
	private final CodewindConnection connection;
	private final List<RegistryInfo> regList;
	private final ImagePushRegistryInfo pushReg;
	private RegistryManagementComposite regComposite;
	
	public RegistryManagementDialog(Shell parentShell, CodewindConnection connection, List<RegistryInfo> regList, ImagePushRegistryInfo pushReg) {
		super(parentShell);
		this.connection = connection;
		this.regList = regList;
		this.pushReg = pushReg;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CodewindUIBundle.message("RegMgmtDialogTitle"));
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		return super.createButtonBar(parent);
	}

	protected Control createDialogArea(Composite parent) {
		setTitleImage(CodewindUIPlugin.getImage(CodewindUIPlugin.CODEWIND_BANNER));
		setTitle(CodewindUIBundle.message("RegMgmtDialogTitle"));
		if (connection.isLocal()) {
			setMessage(CodewindUIBundle.message("RegMgmtDialogLocalMessage"));
		} else {
			setMessage(CodewindUIBundle.message("RegMgmtDialogMessage"));
		}
		
		Composite content = (Composite) super.createDialogArea(parent);
		content.setLayout(new GridLayout());
		content.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		regComposite = new RegistryManagementComposite(content, connection, regList, pushReg);
		GridData data = new GridData(GridData.FILL, GridData.FILL, true, true);
		data.widthHint = 250;
		regComposite.setLayoutData(data);

		return parent; 
	}

	public boolean hasChanges() {
		return regComposite.hasChanges();
	}
	
	public IStatus updateRegistries(IProgressMonitor monitor) {
		return regComposite.updateRegistries(monitor);
	}
	
	@Override
	protected Point getInitialSize() {
		Point point = super.getInitialSize();
		return new Point(700, point.y);
	}
	
	public static void open(Shell shell, CodewindConnection connection, IProgressMonitor monitor) {
		try {
			List<RegistryInfo> regList = connection.requestRegistryList();
			ImagePushRegistryInfo pushReg = connection.requestGetPushRegistry();
			RegistryManagementDialog regDialog = new RegistryManagementDialog(shell, connection, regList, pushReg);
			if (regDialog.open() == Window.OK && regDialog.hasChanges()) {
				SubMonitor mon = SubMonitor.convert(monitor, CodewindUIBundle.message("RegUpdateTask"), 100);
				IStatus status = regDialog.updateRegistries(mon.split(100));
				if (!status.isOK()) {
					throw new InvocationTargetException(status.getException(), status.getMessage());
				}
				if (mon.isCanceled()) {
					return;
				}
			}
		} catch (Exception e) {
			MessageDialog.openError(shell, CodewindUIBundle.message("RegListErrorTitle"), NLS.bind(CodewindUIBundle.message("RegListErrorMsg"), e));
		}
	}
}
