package edu.nops.blacksphere.ide.editors.device;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.editor.FormEditor;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.ide.BSPMessages;
import edu.nops.blacksphere.ide.editors.device.actions.GEFEditorAsImageAction;

/**
 * Страница редактора лицевой панели устройства 
 * @author nops
 */
public class FaceEditor extends GraphicalEditor {

	public FaceEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}
	
	/** Модель устройства */
	private Device device;
	
	public void setDevice(Device device) {
		this.device = device;
	}
	
	public Device getDevice() {
		if (device == null)
			device = new Device();
		return device;
	}

	public static String PAGE_TITLE = BSPMessages.getString("FaceEditor.title"); //$NON-NLS-1$

	public String getTitle() {
		return PAGE_TITLE;
	}
	
	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(getDevice()); // set the contents of this editor
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// реальное сохранение происходит в DeviceEditor
		// здесь нужно только запомнить позицию сохранения
		getCommandStack().markSaveLocation();
	}

	/** Сипсок возможных значений масштаба */
	protected static double[] zoomLevels = {.25, .50, .75, 1.0, 1.5, 2.0};

	/**
	 * Настройка просмотрщика редактора
	 * Установка фабрики элементов
	 * Установка корневого элемента редактирования
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		viewer.setEditPartFactory(new FaceEditPartFactory());
		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		List<String> zoomLevelContributions = new ArrayList<String>(3);
		zoomLevelContributions.add(ZoomManager.FIT_ALL);
		zoomLevelContributions.add(ZoomManager.FIT_WIDTH);
		zoomLevelContributions.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevelContributions);
		root.getZoomManager().setZoomLevels(zoomLevels);
		// добавление действий масштабирования
		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		getSite().getKeyBindingService().registerAction(zoomIn);
		getSite().getKeyBindingService().registerAction(zoomOut);
		// включение масштабирования по скролу мышки
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), 
				MouseWheelZoomHandler.SINGLETON);
		viewer.setRootEditPart(root);
		// Сохранить как картинку
		IAction saveAsImageAction = new GEFEditorAsImageAction(this.getGraphicalViewer());
		getActionRegistry().registerAction(saveAsImageAction);
		// Пока пойдёт контекстное меню одинаковое
		ContextMenuProvider cmProvider =
			new SchemeContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(cmProvider);
		getSite().registerContextMenu(cmProvider, getSite().getSelectionProvider());
	}
	
	@SuppressWarnings("unchecked") //$NON-NLS-1$
	@Override
	public Object getAdapter(Class type) {
		if (type == ZoomManager.class)
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());
		/* TODO доделать Content Outline
		  if (type == IContentOutlinePage.class) {
			DeviceOutlinePage outline = new DeviceOutlinePage(new TreeViewer(), device);
			getSelectionSynchronizer().addViewer(outline.getViewer());
			return outline;
		}*/
		return super.getAdapter(type);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IEditorPart activeEditor = getSite().getPage().getActiveEditor();
		if (activeEditor instanceof FormEditor
				&& this.equals(((FormEditor) activeEditor).getActiveEditor()))
			updateActions(getSelectionActions());
	}

	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

}
