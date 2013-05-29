package spell.dialogs;


import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import spell.model.MagicItem;
import spell.tools.LocaleManager;


public class MagicItemMergeDialog extends TitleAreaDialog {

	private Combo itemCombo;
	private MagicItem sourceItem;
	private int value;
	private MagicItem[] items;
	private MagicItem targetItem;
	
	public final static int CREATE = 0;
	public final static int REPLACE = 1;
	public final static int IGNORE = 2;
	
	
	public MagicItemMergeDialog(Shell parentShell, MagicItem[] items, MagicItem sourceItem) {
		super(parentShell);
		this.sourceItem = sourceItem;
		this.items = items;
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(390,250);
	}

	@Override
	public void create() {
		super.create();
		// Set the title
		setTitle(LocaleManager.instance().getMessage("conflictSolve"));
		// Set the message
		
		setMessage(	LocaleManager.instance().getMessage("magicItemNotFound") + "\n" +
					LocaleManager.instance().getMessage("magicItem") + " : " + sourceItem.getTitle(), IMessageProvider.WARNING);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(LocaleManager.instance().getMessage("actionChoice"));
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		group.setLayout(layout);
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.CENTER;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		group.setLayoutData(gridData);
		
		Button radio = new Button(group, SWT.RADIO | SWT.LEFT);
		radio.setText(LocaleManager.instance().getMessage("magicItemCreate"));
		radio.setData("CREATE");
		radio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				value = CREATE;
				itemCombo.setEnabled(false);
			}
		  });
		radio = new Button(group, SWT.RADIO | SWT.LEFT);
		radio.setText(LocaleManager.instance().getMessage("magicItemReplace"));
		radio.setData("REPLACE");
		radio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				value = REPLACE;
				itemCombo.setEnabled(true);
			}
		  });
		itemCombo = new Combo(group, SWT.LEFT | SWT.READ_ONLY);
		int selectedIndex = 0;
		for(int idx = 0; idx < items.length; idx++) {
			MagicItem item = items[idx];
			itemCombo.add(item.getTitle());
			if(item.getOriginalName().equals(sourceItem.getOriginalName()))
				selectedIndex = idx; 
		}
		itemCombo.select(selectedIndex);
		itemCombo.setEnabled(false);
		
		
		radio = new Button(group, SWT.RADIO | SWT.LEFT);
		radio.setText(LocaleManager.instance().getMessage("magicItemIgnore"));
		radio.setData("IGNORE");
		radio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				value = IGNORE;
				itemCombo.setEnabled(false);
			}
		  });

		return parent;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Add button
		// Own method as we need to overview the SelectionAdapter
		createOkButton(parent, OK, LocaleManager.instance().getMessage("validate"), true);
		// Add a SelectionListener

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, LocaleManager.instance().getMessage("cancel"), false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;
		return valid;
	}

	// We allow the user to resize this dialog
	@Override
	protected boolean isResizable() {
		return true;
	}

	// We need to have the textFields into Strings because the UI gets disposed
	// and the Text Fields are not accessible any more.
	private void saveInput() {
		if(itemCombo.getSelectionIndex() > -1) {
			targetItem = items[itemCombo.getSelectionIndex()];
		}
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	public int getValue() {
		return value;
	}

	public MagicItem getTargetItem() {
		return targetItem;
	}
	
}


