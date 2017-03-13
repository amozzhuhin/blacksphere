package edu.nops.blacksphere.ide.wizards;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.forms.widgets.FormText;

import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPMessages;


/**
 * Страница мастера создания проекта BlackSphere
 * со списком шаблонов проекта 
 * @author nops
 */
public class ProjectTemplatePage extends WizardPage 
	implements ISelectionChangedListener {

	List templateList;
	FormText templateDescription;
	
	private class TemplateInfo {
		// Имя отображаемое на странице выбора шаблона
		public String folder;
		// Описание отображаемое на странице выбора шаблона
		public String description;
		// Порядок в списке
		public int order;
		public TemplateInfo(String folder, String description, int order) {
			this.folder = folder;
			this.description = description;
			this.order = order;
		}
	}

	Hashtable<String, TemplateInfo> templates;
	String defaultTemplate;
	
    public ProjectTemplatePage(String pageName) {
        super(pageName);
        setPageComplete(true);
        templates = new Hashtable<String, TemplateInfo>();
        fillTemplates();
    }
    
    protected void fillTemplates() {
    	defaultTemplate = BSPMessages.getString("ProjectTemplatePage.EmptyTemplate"); //$NON-NLS-1$
    	templates.put(defaultTemplate,
    			new TemplateInfo("empty", //$NON-NLS-1$
    			BSPMessages.getString("ProjectTemplatePage.EmptyTemplateDescription"), 0)); //$NON-NLS-1$
    	templates.put(BSPMessages.getString("ProjectTemplatePage.DiaodeAndButton"), new TemplateInfo("diode_and_button", //$NON-NLS-1$ //$NON-NLS-2$
    			BSPMessages.getString("ProjectTemplatePage.DiodeAndButtonDescription"), 1)); //$NON-NLS-1$
    	templates.put(BSPMessages.getString("ProjectTemplatePage.CounterTitle"), //$NON-NLS-1$
    			new TemplateInfo("counter", //$NON-NLS-1$
    					BSPMessages.getString("ProjectTemplatePage.CounterDescription"), 2)); //$NON-NLS-1$
    	templates.put(BSPMessages.getString("ProjectTemplatePage.ADCTitle"), //$NON-NLS-1$
    			new TemplateInfo("adc", //$NON-NLS-1$
    	    	BSPMessages.getString("ProjectTemplatePage.ADCDescription"), 3)); //$NON-NLS-1$
    }
    
    private class TemplateSorter extends ViewerSorter {
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			return templates.get(e1).order - templates.get(e2).order;
		}
    }
    
	public void createControl(Composite parent) {
		// форма с изменяемыми эолементами управления по горизонтали
		SashForm container = new SashForm(parent, SWT.HORIZONTAL);
		container.setLayout(new GridLayout());
		GridData gd = new GridData(GridData.FILL_BOTH);
		// текстовое поле по умолчанию масштабируется по самому длинному абзацу
		// попробуем отказаться от такого масштабирования
		gd.widthHint = 300;  
		container.setLayoutData(gd);
		// список с именами шаблонов
		templateList = new List(container, SWT.BORDER);
		ListViewer viewer = new ListViewer(templateList);
		viewer.setSorter(new TemplateSorter());
		Enumeration<String> e = templates.keys();
		while (e.hasMoreElements())
			viewer.add(e.nextElement());
		viewer.addSelectionChangedListener(this);
		// описание шаблона
		templateDescription = new FormText(container, SWT.BORDER);
		templateDescription.setBackground(ColorConstants.white);
		templateDescription.marginWidth = 5;
		setControl(container);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		String name;
		if (templateList.getFocusIndex() >= 0)
			name = templateList.getItem(templateList.getFocusIndex());
		else
			name = defaultTemplate;
		templateDescription.setText(templates.get(name).description, true, true);
	}
	
	public String getTemplateFolder() {
		String name;
		if (templateList.getFocusIndex() >= 0)
			name = templateList.getItem(templateList.getFocusIndex());
		else
			name = defaultTemplate;
		return templates.get(name).folder;
	}
	
	public InputStream getTemplateInputStream(String file) throws IOException {
		Path path =	new Path(BSP.getFilePathFromPlugin("templates/" //$NON-NLS-1$
				+ getTemplateFolder() + "/" + file)); //$NON-NLS-1$
		return new FileInputStream(path.toOSString());
	}
}
