package edu.nops.blacksphere.ide.editors.device;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.sun.imageio.spi.OutputStreamImageOutputStreamSpi;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.core.device.DeviceTransform;
import edu.nops.blacksphere.core.resources.IFileStoreEditorInput;
import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPMessages;

/**
 * Класс редактора устройства со станицами: главная страница,
 * редактор схемы, редактор лицевой панели (и исходный текст).
 * @author nops
 */
public class DeviceEditor extends FormEditor implements IPropertyListener,
		IResourceChangeListener {

	public static String EDITOR_TITLE = "device.bsx"; //$NON-NLS-1$

	private GeneralPage generalPage; 
	
	private SchemeEditor schemeEditor;
	
	private FaceEditor faceEditor;
	
	private TextEditor sourceEditor; 
	
	/** Описание устройства */
	private Device device;
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);
		setPartName(EDITOR_TITLE);
		// Добавление себя в слушатели изменения ресурсов
		// для отслеживания операций с проектом
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
			      IResourceChangeEvent.PRE_CLOSE
			      | IResourceChangeEvent.PRE_DELETE);

	}

	@Override
	protected void addPages() {
		try {
			// добавление редактора общих свойств проекта
			generalPage = new GeneralPage(); 
			generalPage.setDevice(device);
			addPage(generalPage.getIndex(), generalPage);
			generalPage.addPropertyListener(this);
			// добавление редактора схемы 
			schemeEditor = new SchemeEditor();
			schemeEditor.setDevice(device);
			int index = addPage(schemeEditor, getEditorInput());
			setPageText(index, schemeEditor.getTitle());
			schemeEditor.addPropertyListener(this);
			// добавление редактора лицевой панели
			faceEditor = new FaceEditor(); 
			faceEditor.setDevice(device);
			index = addPage(faceEditor, getEditorInput());
			setPageText(index, faceEditor.getTitle());
			faceEditor.addPropertyListener(this);
			// добавление текстового редактора
			// для ручного изменения devise.bsx
			sourceEditor = new TextEditor();
			if (getEditorInput() instanceof IStorageEditorInput) {
				index = addPage(sourceEditor, getEditorInput());
				setPageText(index, sourceEditor.getTitle());
				sourceEditor.addPropertyListener(this);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			IStorage storage = ((IStorageEditorInput) getEditorInput()).getStorage();
			if (storage instanceof IFile) {
				IFile file = (IFile) storage;
				OutputStreamWriter writer = new OutputStreamWriter(out);
				DeviceTransform.saveDevice(device, writer);
				file.setContents(
					new ByteArrayInputStream(out.toByteArray()), 
					true, false, monitor);
				// dirty для всех
				generalPage.doSave(monitor);
				schemeEditor.doSave(monitor);
				faceEditor.doSave(monitor);
				if (BSP.DEBUG) {
					sourceEditor.setInput(getEditorInput());
				}
				firePropertyChange(PROP_DIRTY);
			}
		} catch (CoreException ce) { 
			ce.printStackTrace();
		}
	}

	@Override
	public void doSaveAs() { }

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		try {
			// TODO нужно обработать если input не IStorageEditorInput
			// или IFileStoreEditorInput
			if (input instanceof IStorageEditorInput) {
				IStorage file = ((IStorageEditorInput) input).getStorage();
				InputStreamReader reader = new InputStreamReader(file.getContents(), "UTF-8"); //$NON-NLS-1$
				device = DeviceTransform.loadDevice(reader);
			} else {
				InputStream contents = ((IFileStoreEditorInput) input).openInputStream(0, null);
				InputStreamReader reader = new InputStreamReader(contents, "UTF-8"); //$NON-NLS-1$
				device = DeviceTransform.loadDevice(reader);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(getClass().getName() + BSPMessages.getString("DeviceEditor.loadFailedUsingDefaultModel")); //$NON-NLS-1$
			device = new Device();
		}
	}

	
	public void propertyChanged(Object source, int propId) {
		if (propId == PROP_DIRTY) {
			firePropertyChange(PROP_DIRTY);
		}
	}

	@Override
	public boolean isDirty() {
		return generalPage.isDirty() ||
			schemeEditor.isDirty() ||
			faceEditor.isDirty() ||
			sourceEditor.isDirty();
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (getActiveEditor() != null) {
			Object result = null;
			result = getActiveEditor().getAdapter(adapter);
			if (result != null)
				return result;
		}
		if (adapter == IContentOutlinePage.class) 
			return new DeviceOutlinePage(new TreeViewer());
		return super.getAdapter(adapter);
	}

	public Device getDevice() {
		return device;
	}
	
	/** Страница "Схема" со списком элементов схемы */
	public class DeviceOutlinePage extends ContentOutlinePage {	

		public DeviceOutlinePage(EditPartViewer viewer) {
			super(viewer);
		}

		public void createControl(Composite parent) {
			// создание страинцы просмотра
			getViewer().createControl(parent);
			// настройка просмотрщика
			getViewer().setEditDomain(schemeEditor.getEditDomain());
			getViewer().setEditPartFactory(new DeviceOutlineEditPartFactory());
			// настройка констекстного меню
			/*ContextMenuProvider cmProvider = new ShapesEditorContextMenuProvider(
					getViewer(), getActionRegistry()); 
			getViewer().setContextMenu(cmProvider);
			getSite().registerContextMenu(
					"org.eclipse.gef.examples.shapes.outline.contextmenu",
					cmProvider, getSite().getSelectionProvider());*/		
			// связывание выбора на схеме и на странице "Схема"
			schemeEditor.getSelectionSynchronizer().addViewer(getViewer());
			// initialize outline viewer with model
			getViewer().setContents(getDevice());
		}
		
		public void dispose() {
			// освобождаем синхронизацию выбора
			schemeEditor.getSelectionSynchronizer().removeViewer(getViewer());
			super.dispose();
		}

		public Control getControl() {
			return getViewer().getControl();
		}
		
		public void init(IPageSite pageSite) {
			super.init(pageSite);
			// Настройка команд
			/*ActionRegistry registry = getActionRegistry();
			IActionBars bars = pageSite.getActionBars();
			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.DELETE.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));*/
		}
	}

	public void resourceChanged(IResourceChangeEvent event) {
		// Если удаляют или закрывают проект - закрываем редактор
		if ((event.getType() & (IResourceChangeEvent.PRE_CLOSE
				| IResourceChangeEvent.PRE_DELETE)) != 0) {
			IEditorInput input = getEditorInput();
			if (input instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) input).getFile();
				if (file.getProject().equals(event.getResource()))
					close(false);
			}
		}
		
	}
	
	
}
