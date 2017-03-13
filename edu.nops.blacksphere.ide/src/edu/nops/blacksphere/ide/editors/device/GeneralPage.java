package edu.nops.blacksphere.ide.editors.device;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.WorkbenchPart;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.ide.BSPMessages;

public class GeneralPage extends WorkbenchPart
		implements IFormPage, ModifyListener {

	private FormToolkit toolkit;
	
	private ScrolledForm form;

	private boolean isActive = false;

	private int index = 0;
	
	private boolean dirty;
	
	private Device device;
	
	private Section infoSection;
	private Section deviceSection;
	
	private Text authorText;
	private Text descrText;
	private Text ramText;
	private Text romText;
	private Text freqText;
	
	public static String PAGE_TITLE = BSPMessages.getString("GeneralPage.title"); //$NON-NLS-1$
	
	public static String PAGE_ID
		= "edu.nops.blacksphere.editors.device.GeneralPage"; //$NON-NLS-1$
	
	public boolean canLeaveThePage() {
		return true;
	}

	/** Создание секции "Общая информация" */
	private void createInfoSection(Composite parent) {
		infoSection = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TREE_NODE
				| ExpandableComposite.TITLE_BAR);
		infoSection.setText(BSPMessages.getString("GeneralPage.GeneralInformation")); //$NON-NLS-1$
		Composite container = toolkit.createComposite(infoSection);
		toolkit.paintBordersFor(container);
		infoSection.setClient(container);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 20;
		container.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// Автор
		toolkit.createLabel(container, BSPMessages.getString("GeneralPage.Autor")); //$NON-NLS-1$
		authorText = toolkit.createText(container, device.getAuthor());
		authorText.addModifyListener(this);
		authorText.setLayoutData(gd);
		// Описание
		toolkit.createLabel(container, BSPMessages.getString("GeneralPage.DeviceDescription")); //$NON-NLS-1$
		descrText = toolkit.createText(container, device.getDescr());
		descrText.addModifyListener(this);
		descrText.setLayoutData(gd);
	}
	
	/** Создание секции "Характеристики устройства" */
	private void createDeviceSection(Composite parent) {
		deviceSection = toolkit.createSection(parent,
				ExpandableComposite.EXPANDED | ExpandableComposite.TREE_NODE
				| ExpandableComposite.TITLE_BAR);
		deviceSection.setText(BSPMessages.getString("GeneralPage.DeviceInfo")); //$NON-NLS-1$
		Composite container = toolkit.createComposite(deviceSection);
		toolkit.paintBordersFor(container);
		deviceSection.setClient(container);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 20;
		container.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// Процессор
		toolkit.createLabel(container, BSPMessages.getString("GeneralPage.processorTypeLabel")); //$NON-NLS-1$
		Text text = toolkit.createText(container, BSPMessages.getString("GeneralPage.Intel8088/8086")); //$NON-NLS-1$
		text.setEnabled(false);
		text.setLayoutData(gd);
		// Частота
		toolkit.createLabel(container, BSPMessages.getString("GeneralPage.processorFreqLabel")); //$NON-NLS-1$
		freqText = toolkit.createText(container, String.valueOf(device.getFrequency()) ); //$NON-NLS-1$
		freqText.setLayoutData(gd);
		freqText.addVerifyListener(new IntegerVerifyListener());
		freqText.addModifyListener(this);
		// ОЗУ
		toolkit.createLabel(container, BSPMessages.getString("GeneralPage.ramLabel")); //$NON-NLS-1$
		ramText = toolkit.createText(container,
			String.valueOf(device.getRAM()));
		ramText.setLayoutData(gd);
		ramText.addVerifyListener(new IntegerVerifyListener());
		ramText.addModifyListener(this);
		//TODO: убрать, когда будет обработка размера памяти нормальная
		ramText.setEditable(false);
		// ПЗУ
		toolkit.createLabel(container, BSPMessages.getString("GeneralPage.romLabel")); //$NON-NLS-1$
		romText = toolkit.createText(container,
		 	String.valueOf(device.getROM()));
		romText.setLayoutData(gd);
		romText.addVerifyListener(new IntegerVerifyListener());
		romText.addModifyListener(this);
		//TODO: убрать, когда будет обработка размера памяти нормальная
		romText.setEditable(false);
	}

	public void createPartControl(Composite parent) {
		toolkit =  new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setText(PAGE_TITLE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		form.getBody().setLayout(layout);
		toolkit.paintBordersFor(form.getBody());
		createInfoSection(form.getBody());
		infoSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createDeviceSection(form.getBody());
		deviceSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	public void dispose() {
		  toolkit.dispose();
	}

	public void doSave(IProgressMonitor monitor) {
		// реальное сохранение происходит в DeviceEditor
		dirty = false;
	}

	public void doSaveAs() { doSave(null); }

	@SuppressWarnings("unchecked") //$NON-NLS-1$
	public Object getAdapter(Class adapter) {
		return null;
	}

	public FormEditor getEditor() {
		return null;
	}

	public IEditorInput getEditorInput() {
		return null;
	}

	public IEditorSite getEditorSite() {
		return null;
	}

	public String getId() {
		return PAGE_ID;
	}

	public int getIndex() {
		return index;
	}

	public IManagedForm getManagedForm() {
		return null;
	}

	public Control getPartControl() {
		return form;
	}

	public IWorkbenchPartSite getSite() {
		return null;
	}

	public String getTitle() {
		return PAGE_TITLE;
	}

	public Image getTitleImage() {
		return null;
	}

	public String getTitleToolTip() {
		return null;
	}

	public void init(IEditorSite site, IEditorInput input)
		throws PartInitException { }

	public void initialize(FormEditor editor) { }

	public boolean isActive() {
		return isActive;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		firePropertyChange(IEditorPart.PROP_DIRTY);
	}

	public boolean isEditor() {
		// TODO Автоматически созданная заглушка метода
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public boolean isSaveOnCloseNeeded() {
		return false;
	}

	public void removePropertyListener(IPropertyListener listener) { }

	public boolean selectReveal(Object object) {
		return false;
	}

	public void setActive(boolean active) {
		isActive = active;
	}

	public void setFocus() {
		form.setFocus();
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setDevice(Device device) {
		this.device = device;
	}

	@Override
	public void modifyText(ModifyEvent e) {
		if (e.getSource() == authorText) {
			device.setAuthor(authorText.getText());
			setDirty(true);
		} if (e.getSource() == descrText) {
			device.setDescr(descrText.getText());
			setDirty(true);
		} if (e.getSource() == ramText) {
			device.setRAM(Integer.parseInt(ramText.getText()));
			setDirty(true);
		} if (e.getSource() == romText) {
			device.setROM(Integer.parseInt(romText.getText()));
			setDirty(true);
		} if (e.getSource() == freqText) {
			device.setFrequency(Integer.parseInt(freqText.getText()));
			setDirty(true);
		}
	}

	/**
	 * Проверка вводимого текста на соответствие
	 * целому значению
	 * @author nops
	 */
	private class IntegerVerifyListener implements VerifyListener {

		public void verifyText(VerifyEvent event) {
			for (int i = 0; i < event.text.length(); i++)
				if (event.text.charAt(i) < '0' || event.text.charAt(i) > '9') { 
					event.doit = false;
					return;
				}
			event.doit = event.doit || event.text.equals(""); //$NON-NLS-1$
		}
		
	}

}
