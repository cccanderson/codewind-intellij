/*******************************************************************************
 * Copyright (c) 2018, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.codewind.intellij.ui.prefs;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.ui.ComboBox;
import org.eclipse.codewind.intellij.core.cli.InstallUtil;
import org.eclipse.codewind.intellij.ui.messages.CodewindUIBundle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.internal.browser.BrowserManager;
import org.eclipse.ui.internal.browser.IBrowserDescriptor;

import javax.swing.*;

/**
 * Top level Codewind preference page
 */
@SuppressWarnings("restriction")
public class CodewindPrefsParentPage extends PreferencePage implements IWorkbenchPreferencePage {

	public static final String ID = "CodewindParentPage"; //$NON-NLS-1$
	
	private static PropertiesComponent prefs;

	private NumberFormat timeoutFormat;

	private JFormattedTextField installTimeoutText;
	private JFormattedTextField startTimeoutText;
	private JFormattedTextField stopTimeoutText;
	private JFormattedTextField uninstallTimeoutText;
	private JFormattedTextField debugTimeoutText;
	private ComboBox webBrowserCombo;
	private JRadioButton[] stopAppsButtons = new JRadioButton[3];
		
	private String browserName = null;

	@Override
	public void init(/*IWorkbench arg0*/) {
		// setDescription("Expand this preferences category to set specific Codewind preferences.");
		// setImageDescriptor(CodewindUIPlugin.getDefaultIcon());

		prefs = PropertiesComponent.getInstance();
	}

	@Override
	protected JComponent createContents(JComponent parent) {
		final JPanel composite = new JPanel(); //(parent, SWT.NONE);
	    /*GridLayout layout = new GridLayout();
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(4);
		layout.verticalSpacing = convertVerticalDLUsToPixels(3);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
	    layout.numColumns = 1;
	    composite.setLayout(layout);
	    composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));*/

	    if (CodewindInstall.ENABLE_STOP_APPS_OPTION) {
		    JTextField stopAppContainersLabel = new JTextField(); //(composite, SWT.READ_ONLY | SWT.SINGLE);
		    stopAppContainersLabel.setText(CodewindUIBundle.message("PrefsParentPage_StopAppsLabel"));
		    //stopAppContainersLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false, 2, 1));
		    stopAppContainersLabel.setBackground(composite.getBackground());
		    stopAppContainersLabel.setForeground(composite.getForeground());
	
		    JPanel stopAppsComposite = new JPanel(); //composite, SWT.NONE);
		    /*layout = new GridLayout();
		    layout.horizontalSpacing = convertHorizontalDLUsToPixels(8);
		    layout.verticalSpacing = convertVerticalDLUsToPixels(3);
		    layout.marginWidth = 20;
		    layout.marginHeight = 2;
		    layout.numColumns = 3;
		    stopAppsComposite.setLayout(layout);
		    stopAppsComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));*/
	
		    stopAppsButtons[0] = new JRadioButton(); //stopAppsComposite, SWT.RADIO);
		    stopAppsButtons[0].setText(CodewindUIBundle.message("PrefsParentPage_StopAppsAlways"));
		    stopAppsButtons[0].setActionCommand(InstallUtil.STOP_APP_CONTAINERS_ALWAYS);
		    stopAppsComposite.add(stopAppsButtons[0]);
	
		    stopAppsButtons[1] = new JRadioButton(); //stopAppsComposite, SWT.RADIO);
		    stopAppsButtons[1].setText(CodewindUIBundle.message("PrefsParentPage_StopAppsNever"));
		    stopAppsButtons[1].setActionCommand(InstallUtil.STOP_APP_CONTAINERS_NEVER);
			stopAppsComposite.add(stopAppsButtons[1]);
	
		    stopAppsButtons[2] = new JRadioButton(); //stopAppsComposite, SWT.RADIO);
		    stopAppsButtons[2].setText(CodewindUIBundle.message("PrefsParentPage_StopAppsPrompt"));
		    stopAppsButtons[2].setActionCommand(InstallUtil.STOP_APP_CONTAINERS_PROMPT);
			stopAppsComposite.add(stopAppsButtons[2]);
	
		    setStopAppsSelection(prefs.getValue(InstallUtil.STOP_APP_CONTAINERS_PREFSKEY));
	
		    stopAppsComposite.add(new JLabel()); //(composite, SWT.HORIZONTAL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL, GridData.CENTER, true, false, 2, 1));
			composite.add(stopAppsComposite);
	    }
	    // ButtonGroup is just for interbutton communication - need a JPanel or somesuch for the grouping
	    ButtonGroup generalGroup = new ButtonGroup(); //composite, SWT.NONE);
	    generalGroup.setText(CodewindUIBundle.message("PrefsParentPage_GeneralGroup"));
	    /*layout = new GridLayout();
	    layout.horizontalSpacing = convertHorizontalDLUsToPixels(7);
	    layout.verticalSpacing = convertVerticalDLUsToPixels(5);
	    layout.marginWidth = 3;
	    layout.marginHeight = 10;
	    layout.numColumns = 1;
	    generalGroup.setLayout(layout);
	    generalGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));*/
	    
	    JCheckBox autoOpenOverviewButton = new JCheckBox();// generalGroup, SWT.CHECK);
	    autoOpenOverviewButton.setText(CodewindUIBundle.message("PrefsParentPage_AutoOpenOverviewButton"));
	    //autoOpenOverviewButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
	    autoOpenOverviewButton.setSelected(prefs.getBoolean(CodewindCorePlugin.AUTO_OPEN_OVERVIEW_PAGE));
	    
	    autoOpenOverviewButton.addActionListener(new ActionListener() {
			@Override
			public void widgetSelected(ActionEvent e) {
				prefs.setValue(CodewindCorePlugin.AUTO_OPEN_OVERVIEW_PAGE, autoOpenOverviewButton.isSelected());
			}
		});
	    generalGroup.add(autoOpenOverviewButton);
	    
	    JCheckBox supportFeaturesButton = new JCheckBox();//generalGroup, SWT.CHECK);
	    supportFeaturesButton.setText(CodewindUIBundle.message("PrefsParentPage_EnableSupportFeatures"));
	    //supportFeaturesButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
	    supportFeaturesButton.setSelected(prefs.getBoolean(CodewindCorePlugin.ENABLE_SUPPORT_FEATURES));
	    
	    supportFeaturesButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prefs.setValue(CodewindCorePlugin.ENABLE_SUPPORT_FEATURES, supportFeaturesButton.isSelected());
			}
		});
	    generalGroup.add(supportFeaturesButton);
	    
	    JPanel installGroup = new JPanel(); //composite, SWT.NONE);
	    installGroup.setText(CodewindUIBundle.message("PrefsParentPage_StartupShutdownGroup"));
	    /* layout = new GridLayout();
	    layout.horizontalSpacing = convertHorizontalDLUsToPixels(7);
	    layout.verticalSpacing = convertVerticalDLUsToPixels(5);
	    layout.marginWidth = 3;
	    layout.marginHeight = 10;
	    layout.numColumns = 4;
	    installGroup.setLayout(layout);
	    installGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));*/

	    timeoutFormat = NumberFormat.getNumberInstance();
		timeoutFormat.setParseIntegerOnly(true);

	    installTimeoutText = createCWTimeoutEntry(installGroup, CodewindUIBundle.message("PrefsParentPage_InstallTimeout"), CodewindCorePlugin.CW_INSTALL_TIMEOUT, InstallUtil);
	    uninstallTimeoutText = createCWTimeoutEntry(installGroup, CodewindUIBundle.message("PrefsParentPage_UninstallTimeout"), CodewindCorePlugin.CW_UNINSTALL_TIMEOUT);
	    startTimeoutText = createCWTimeoutEntry(installGroup, CodewindUIBundle.message("PrefsParentPage_StartTimeout"), CodewindCorePlugin.CW_START_TIMEOUT);
	    stopTimeoutText = createCWTimeoutEntry(installGroup, CodewindUIBundle.message("PrefsParentPage_StopTimeout"), CodewindCorePlugin.CW_STOP_TIMEOUT);
	    composite.add(installGroup);

	    JPanel debugGroup = new JPanel(); //composite, SWT.NONE);
	    debugGroup.setText(CodewindUIBundle.message("PrefsParentPage_DebugGroup"));
	    /*layout = new GridLayout();
	    layout.horizontalSpacing = convertHorizontalDLUsToPixels(7);
	    layout.verticalSpacing = convertVerticalDLUsToPixels(5);
	    layout.marginWidth = 3;
	    layout.marginHeight = 10;
	    layout.numColumns = 2;
	    debugGroup.setLayout(layout);
	    debugGroup.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));*/

		JLabel debugTimeoutLabel = new JLabel(); //(debugGroup, SWT.READ_ONLY);
		debugTimeoutLabel.setText(" " + CodewindUIBundle.message("PrefsParentPage_DebugTimeoutLabel")); //$NON-NLS-1$
		//debugTimeoutLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
		debugGroup.add(debugTimeoutLabel);

		NumberFormat timeoutFormat2 = NumberFormat.getNumberInstance();
		timeoutFormat2.setParseIntegerOnly(true);
		timeoutFormat2.setMaximumIntegerDigits(3);
		debugTimeoutText = new JFormattedTextField(timeoutFormat2); //(debugGroup, SWT.BORDER);
		debugTimeoutText.setText("" + 	//$NON-NLS-1$
				prefs.getInt(CodewindCorePlugin.DEBUG_CONNECT_TIMEOUT_PREFSKEY));

		/*GridData debugTextData = new GridData(GridData.BEGINNING, GridData.FILL, false, false);
		debugTextData.widthHint = 50;
		debugTimeoutText.setLayoutData(debugTextData);*/

		debugTimeoutText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				validate();
			}
		});
		debugGroup.add(debugTimeoutText);
		composite.add(debugGroup);
		 	    
	    final JPanel selectWebBrowserComposite = new JPanel(); //debugGroup, SWT.NONE);
	    /*layout = new GridLayout();
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(4);
		layout.verticalSpacing = convertVerticalDLUsToPixels(3);
		layout.marginWidth = 3;
		layout.marginHeight = 2;
	    layout.numColumns = 2;
	    selectWebBrowserComposite.setLayout(layout);
	    selectWebBrowserComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 2, 1));*/
	    
	    JLabel browserSelectionLabel = new JLabel(); //selectWebBrowserComposite, SWT.NONE);
	    browserSelectionLabel.setText(CodewindUIBundle.message("BrowserSelectionLabel"));
	    // browserSelectionLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false, 2, 1));
	    selectWebBrowserComposite.add(browserSelectionLabel);
        
        webBrowserCombo = new ComboBox(); //(selectWebBrowserComposite, SWT.BORDER | SWT.READ_ONLY);
        selectWebBrowserComposite.add(webBrowserCombo);
	    refreshPreferencesPage();
	    	    
		Link addBrowserButton = new Link(selectWebBrowserComposite, SWT.NONE);
		addBrowserButton.setText("<a href=\"org.eclipse.ui.browser.preferencePage\">"+CodewindUIBundle.message("BrowserSelectionManageButtonText")+"</a>"); //$NON-NLS-1$ //$NON-NLS-2$
		addBrowserButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {				
				PreferencesUtil.createPreferenceDialogOn(composite
						.getShell(), e.text, null, null);
				composite.layout();				
			}
		});
		debugGroup.add(selectWebBrowserComposite);
		/*Label endSpacer = new Label(composite, SWT.NONE);
		endSpacer.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
		// this last label just moves the Default and Apply buttons over
		new Label(composite, SWT.NONE);*/

		return composite;
	}
	
	private JFormattedTextField createCWTimeoutEntry(Composite comp, String label, String prefKey, int defaultValue) {
	    JLabel timeoutLabel = new JLabel(comp, SWT.NONE);
	    timeoutLabel.setText(label);
	    //timeoutLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, false));
	    
	    JFormattedTextField text = new JFormattedTextField(timeoutFormat); //(comp, SWT.BORDER);
	    text.setText(Integer.toString(prefs.getInt(prefKey,defaultValue));
	    /*GridData data = new GridData(GridData.BEGINNING, GridData.FILL, false, false);
		data.widthHint = 50;
		text.setLayoutData(data);*/
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				validate();
			}
		});
		
	    return text;
	}
	
	private void setStopAppsSelection(String selection) {
		for (JButton button : stopAppsButtons) {
			if (button.getActionCommand().equals(selection)) {
				button.setSelected(true);
				break;
			}
		}
	}

	private void validate() {
		if (!validateTimeout(installTimeoutText.getText().trim()) ||
				!validateTimeout(uninstallTimeoutText.getText().trim()) ||
				!validateTimeout(startTimeoutText.getText().trim()) ||
				!validateTimeout(stopTimeoutText.getText().trim()) ||
				!validateTimeout(debugTimeoutText.getText().trim())) {
			return;
		}
		
		setErrorMessage(null);
		setValid(true);
	}
	
	private boolean validateTimeout(String timeoutStr) {
		boolean isValid = true;
		try {
			int timeout = Integer.parseInt(timeoutStr);
			if (timeout <= 0) {
				isValid = false;
			}
		} catch(NumberFormatException e) {
			isValid = false;
		}
		if (!isValid) {
			setErrorMessage(NLS.bind(CodewindUIBundle.message("PrefsParentPage_ErrInvalidTimeout"), timeoutStr));
			setValid(false);
		}
		return isValid;
	}

	@Override
	public boolean performOk() {
		if (!isValid()) {
			return false;
		}
		
		if (CodewindInstall.ENABLE_STOP_APPS_OPTION) {
			for (JButton button : stopAppsButtons) {
				if (button.isSelected()) {
					prefs.setValue(InstallUtil.STOP_APP_CONTAINERS_PREFSKEY, (String)button.getActionCommand());
					break;
				}
			}
		}
		
		prefs.setValue(CodewindCorePlugin.CW_INSTALL_TIMEOUT, Integer.parseInt(installTimeoutText.getText().trim()));
		prefs.setValue(CodewindCorePlugin.CW_UNINSTALL_TIMEOUT, Integer.parseInt(uninstallTimeoutText.getText().trim()));
		prefs.setValue(CodewindCorePlugin.CW_START_TIMEOUT, Integer.parseInt(startTimeoutText.getText().trim()));
		prefs.setValue(CodewindCorePlugin.CW_STOP_TIMEOUT, Integer.parseInt(stopTimeoutText.getText().trim()));
		
		// validate in validate() that this is a good integer
		int debugTimeout = Integer.parseInt(debugTimeoutText.getText().trim());
		prefs.setValue(CodewindCorePlugin.DEBUG_CONNECT_TIMEOUT_PREFSKEY, debugTimeout);

		// removes any trimmed space
		debugTimeoutText.setText("" + debugTimeout); //$NON-NLS-1$
		
		if (this.webBrowserCombo != null) {
			// The first option in the webBrowserCombo is to not use the default browser.
			// As a result, if the first option is selected, then remove the preference
			if (webBrowserCombo.getSelectedIndex() > 0) {
				// If it is selected, then save the preference. Do not add if it's the first item, since the option
				// for the first entry is "No browser selected"
				if (browserName != null) {
					prefs.setValue(CodewindCorePlugin.NODEJS_DEBUG_BROWSER_PREFSKEY, browserName);
				}
			} else {
				prefs.unsetValue(CodewindCorePlugin.NODEJS_DEBUG_BROWSER_PREFSKEY);
			}
		}

		return true;
	}
	
	@Override
	public void performDefaults() {
		if (CodewindInstall.ENABLE_STOP_APPS_OPTION) {
			setStopAppsSelection(InstallUtil.STOP_APP_CONTAINERS_DEFAULT);
		}
		debugTimeoutText.setText("" + 	//$NON-NLS-1$
				prefs.getDefaultInt(CodewindCorePlugin.DEBUG_CONNECT_TIMEOUT_PREFSKEY));
		webBrowserCombo.setSelectedIndex(0);
	}
	
	// Refreshes all changes
	protected void refreshPreferencesPage() {
		if (webBrowserCombo == null)
			return;
		
		// Remove all the browsers, since a browser may have been removed through
		// the Web Browser preference page
		webBrowserCombo.removeAll();
		webBrowserCombo.add(CodewindUIBundle.message("BrowserSelectionNoBrowserSelected"));
		webBrowserCombo.setSelectedIndex(0);
        
        BrowserManager bm = BrowserManager.getInstance();
    	List<IBrowserDescriptor> validBrowsers;
        
        if (bm != null) {
	        List<IBrowserDescriptor> browserList = bm.getWebBrowsers();
	        if (browserList != null) {
		        validBrowsers = new ArrayList<IBrowserDescriptor>();		        
		        int len = browserList.size();
		        
		        // Search for valid browers only. If the location is null, that means
		        // it is the default system browser. Since the browser will be launched
		        // as an external browser, a location must exist or there will be an error
		        for (int i=0; i<len; i++) {
		        	IBrowserDescriptor tempBrowser = browserList.get(i);
		        	if (tempBrowser != null && tempBrowser.getLocation() != null && 
		        			!tempBrowser.getLocation().trim().equals("")) { //$NON-NLS-1$
		        		validBrowsers.add(tempBrowser);
		        	}
		        }
		
		        len = validBrowsers.size();		        
				Logger.log("Refresh preference page valid browser length: " + len);  //$NON-NLS-1$
		        		        		        
				// When it loads, check if preference exists
				String foundDefaultBrowser = prefs.getValue(CodewindCorePlugin.NODEJS_DEBUG_BROWSER_PREFSKEY);
		        boolean foundDefaultBrowserName = false;
		        
		        for (int i=0; i<len; i++) {
		        	IBrowserDescriptor tempBrowser = validBrowsers.get(i);
		        	if (tempBrowser != null) {
		        		String browserName = tempBrowser.getName();
			    		webBrowserCombo.addItem(browserName);

		    		   if (browserName != null && foundDefaultBrowser != null && browserName.equalsIgnoreCase(foundDefaultBrowser)) {
		    			   // Need to add 1 because of the first option of no browser selected
		    			   webBrowserCombo.setSelectedIndex(i + 1);
		    			   foundDefaultBrowserName = true;
		    		   }
		        	}
		        }
		        
				Logger.log("Refresh preference page found default browser: " + foundDefaultBrowser);  //$NON-NLS-1$
		        
				if (foundDefaultBrowser == null || !foundDefaultBrowserName){
					// Could not find browser from preference or browser got removed
					Logger.log("Refresh preference page: could not find preferences or browser was removed");  //$NON-NLS-1$
					prefs.unsetValue(CodewindCorePlugin.NODEJS_DEBUG_BROWSER_PREFSKEY);
				}
				
				webBrowserCombo.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						if (webBrowserCombo.getSelectedIndex() >= 0){
							browserName = webBrowserCombo.getSelectedItem().toString();
						}
					}
				});
	        }
        }
	
		//webBrowserCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	}

	@Override
	public void setVisible(boolean visible){
		// Override the setVisible for when the user updates the web browser page
		// and then returns back to this preference page. The preference page will
		// not get redrawn, so update the combo box in this method
		if (webBrowserCombo != null){
			refreshPreferencesPage();
		}
		super.setVisible(visible);
	}
}
