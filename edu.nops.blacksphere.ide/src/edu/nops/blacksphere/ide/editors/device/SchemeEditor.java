package edu.nops.blacksphere.ide.editors.device;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.editor.FormEditor;

import edu.nops.blacksphere.core.device.Device;
import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPMessages;
import edu.nops.blacksphere.ide.editors.device.actions.GEFEditorAsImageAction;

/**
 * Редактор схемы устройства с палитрой.
 * Используеться как страница в редакторе устроуства. 
 * @author nops
 */
public class SchemeEditor extends GraphicalEditorWithFlyoutPalette {

	/** Имя страницы в редакторе устройства */
	public static String PAGE_TITLE = BSPMessages.getString("SchemeEditor.title"); //$NON-NLS-1$

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
	
	/**
	 * Создание нового редактора схем
	 * Установка имени страницы и объекта-состояния
	 */
	public SchemeEditor() {
		super();
		setPartName(PAGE_TITLE);
		setEditDomain(new DefaultEditDomain(this));
	}

	/** Корневой элемент палитры компонентов */
	private PaletteRoot paletteRoot;

	@Override
	protected PaletteRoot getPaletteRoot() {
		if (paletteRoot == null) {
			paletteRoot = SchemePaletteProvider.getPaletteRoot();
		}
		return paletteRoot;
	}


	/** Сипсок возможных значений масштаба */
	protected static double[] zoomLevels = {.25, .50, .75, 1.0, 1.5, 2.0};
	
	
	@Override
	protected void createActions() {
		super.createActions();
		IAction action = new DirectEditAction((IWorkbenchPart)this);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());
	}

	private KeyHandler sharedKeyHandler;

	/**
	 * Returns the KeyHandler with common bindings for both the Outline and Graphical Views.
	 * For example, delete is a common action.
	 */
	protected KeyHandler getCommonKeyHandler(){
		if (sharedKeyHandler == null){
			sharedKeyHandler = new KeyHandler();
			sharedKeyHandler.put(
				KeyStroke.getPressed(SWT.F2, 0),
				getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
		}
		return sharedKeyHandler;
	}
	
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
		viewer.setEditPartFactory(new SchemeEditPartFactory());
		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
		// настройка уровней масштабирования
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
		// установка ширины и высоты сетки
		Dimension gridDimension = new Dimension(BSP.GRID_SPACING, BSP.GRID_SPACING);
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, gridDimension);
		// действие включения отображения сетки
		IAction showGrid = new ToggleGridAction(viewer);
		getActionRegistry().registerAction(showGrid);
		// включить сетку 
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		// показать сетку
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
		// предоставление контекстного меню
		ContextMenuProvider cmProvider =
				new SchemeContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(cmProvider);
		getSite().registerContextMenu(cmProvider, getSite().getSelectionProvider());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer)
			.setParent(getCommonKeyHandler()));
		IAction saveAsImageAction = new GEFEditorAsImageAction(this.getGraphicalViewer());
		getActionRegistry().registerAction(saveAsImageAction);
	}
	
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		// реальное сохранение происходит в DeviceEditor
		// здесь нужно только запомнить позицию сохранения
		getCommandStack().markSaveLocation();
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(getDevice()); // set the contents of this editor
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

	/* Повышение области видимости метода getEditDomain */
	@Override
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}

	/* Повышение области видимости метода getSelectionSynchronizer */
	@Override
	public SelectionSynchronizer getSelectionSynchronizer() {
		return super.getSelectionSynchronizer();
	}
	
	
}
