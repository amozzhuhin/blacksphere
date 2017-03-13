package edu.nops.blacksphere.ide.editors.device;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.palette.*;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;

import edu.nops.blacksphere.core.device.elements.*;
import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.BSPImages;
import edu.nops.blacksphere.ide.BSPMessages;


/**
 * Класс создаёт и предоставляет палитру компонентов
 * схемы устройства
 * @author nops
 */
public class SchemePaletteProvider {

	/**
	 * Получение корневого элемента палитры конмонентов
	 * @see GraphicalEditorWithFlyoutPalette#getPaletteRoot()
	 */
	public static PaletteRoot getPaletteRoot() {
		PaletteRoot paletteRoot = new PaletteRoot();
		paletteRoot.add(createControlGroup(paletteRoot));
		paletteRoot.add(createPortsGroup(paletteRoot));
		paletteRoot.add(createPowerGroup(paletteRoot));
		paletteRoot.add(createLogicGroup(paletteRoot));
		paletteRoot.add(createLEDGroup(paletteRoot));
		paletteRoot.add(createButtonGroup(paletteRoot));
		paletteRoot.add(createConverterGroup(paletteRoot));
		return paletteRoot;
	}

	private static PaletteContainer createControlGroup(PaletteRoot root){
		PaletteGroup group = new PaletteGroup(BSPMessages.getString("SchemePaletteProvider.ControlGroup")); //$NON-NLS-1$

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();

		ToolEntry tool = new PanningSelectionToolEntry();
		tool.setLabel(BSPMessages.getString("SchemePaletteProvider.SelectionTool")); //$NON-NLS-1$
		entries.add(tool);
		root.setDefaultEntry(tool);

		PaletteStack marqueeStack = new PaletteStack("Выделение", "", null); //$NON-NLS-1$ //$NON-NLS-2$
		
		MarqueeToolEntry marquee = new MarqueeToolEntry();
		marquee.setLabel(BSPMessages.getString("SchemePaletteProvider.ElementsSelection")); //$NON-NLS-1$
		marqueeStack.add(marquee);
		marquee = new MarqueeToolEntry();
		marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
				new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED));
		marquee.setLabel(BSPMessages.getString("SchemePaletteProvider.ConnectionSelection")); //$NON-NLS-1$
		marqueeStack.add(marquee);
		marquee = new MarqueeToolEntry();
		marquee.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR, 
				new Integer(MarqueeSelectionTool.BEHAVIOR_CONNECTIONS_TOUCHED 
				| MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED));
		marquee.setLabel(BSPMessages.getString("SchemePaletteProvider.AllSelection")); //$NON-NLS-1$
		marqueeStack.add(marquee);
		marqueeStack.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		entries.add(marqueeStack);
		
		tool = new ConnectionCreationToolEntry(
				BSPMessages.getString("SchemePaletteProvider.Connection"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.ConectionTooltip"), //$NON-NLS-1$
				new SimpleFactory(WireElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_CONNECTION_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_CONNECTION_24)
			);
		entries.add(tool);
		// Метка
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.Label"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.LabelTooltip"), //$NON-NLS-1$
				new SimpleFactory(LabelElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_LABEL_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_LABEL_24)
		));
		
		group.addAll(entries);
		return group;
	}

	private static PaletteContainer createPortsGroup(PaletteRoot root){
		PaletteDrawer group = new PaletteDrawer(
				BSPMessages.getString("SchemePaletteProvider.Ports"), //$NON-NLS-1$
				null);//$NON-NLS-1$

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();
		// Порт ввода
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.InPort"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.InPortTooltip"), //$NON-NLS-1$
				new SimpleFactory(InPortElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_INPORT_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_INPORT_24)
		));
		// Порт вывода
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.OutPort"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.OutPortTooltip"), //$NON-NLS-1$
				new SimpleFactory(OutPortElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_OUTPORT_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_OUTPORT_24)
		));
		group.addAll(entries);
		
		return group;
	}

	private static PaletteContainer createPowerGroup(PaletteRoot root) {
		PaletteDrawer group = new PaletteDrawer(
				BSPMessages.getString("SchemePaletteProvider.PowerGroup"), //$NON-NLS-1$
				null);//$NON-NLS-1$

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();
		// +5
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.Power"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.PowerTooltip"), //$NON-NLS-1$
				new SimpleFactory(PowerElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_POWER_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_POWER_24)
		));
		// Земля
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.Ground"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.GroundTooltip"), //$NON-NLS-1$
				new SimpleFactory(GroundElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_GROUND_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_GROUND_24)
		));
		group.addAll(entries);
		
		return group;
	}

	private static PaletteContainer createLEDGroup(PaletteRoot root) {
		PaletteDrawer group = new PaletteDrawer(
				BSPMessages.getString("SchemePaletteProvider.LedGroup"), //$NON-NLS-1$
				null);//$NON-NLS-1$

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();
		// LED indicator
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.LedIndicator"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.LedIndicatorTooltip"), //$NON-NLS-1$
				new SimpleFactory(LedElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_LED_DIODE_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_LED_DIODE_24)
		));
		// 7-LED indicator
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.Led7Indicator"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.Led7IndicatorTooltip"), //$NON-NLS-1$
				new SimpleFactory(Led7SegElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_LED_7_SEG_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_LED_7_SEG_24)
		));
		// LEDMatrix indicator
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.LedMatrix"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.LedMatrixTooltip"), //$NON-NLS-1$
				new SimpleFactory(LedMatrixElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_LED_MATRIX_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_LED_MATRIX_24)
		));
		group.addAll(entries);
		
		return group;
	}

	private static PaletteContainer createLogicGroup(PaletteRoot root) {
		PaletteDrawer group = new PaletteDrawer(
				BSPMessages.getString("SchemePaletteProvider.LogicGroup"), //$NON-NLS-1$
				null);//$NON-NLS-1$

		List<PaletteEntry> entries = new ArrayList<PaletteEntry>();
		// NOT
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.Not"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.NotTooltip"), //$NON-NLS-1$
				new SimpleFactory(NotElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_NOT_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_NOT_24)
		));
		// 3-AND-NOT
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.3AND-NOT"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.3AND-NOTTooltip"), //$NON-NLS-1$
				new SimpleFactory(And3NotElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_AND_3_NOT_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_AND_3_NOT_24)
		));
		group.addAll(entries);
		
		return group;
	}
	
	private static PaletteContainer createButtonGroup(PaletteRoot root) {
		PaletteDrawer group = new PaletteDrawer(
				BSPMessages.getString("SchemePaletteProvider.ButtonsGroup"), //$NON-NLS-1$
				null);//$NON-NLS-1$

		ArrayList<PaletteEntry> entries = new ArrayList<PaletteEntry>();
		// Button
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.Button"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.ButtonTooltip"), //$NON-NLS-1$
				new SimpleFactory(ButtonElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_BUTTON_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_BUTTON_24)
		));
		// Keyboard
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.Keyboard"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.KeyboardTooltip"), //$NON-NLS-1$
				new SimpleFactory(KeyboardElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_KEYBOARD_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_KEYBOARD_24)
		));
		group.addAll(entries);
		
		return group;
	}
	
	private static PaletteContainer createConverterGroup(PaletteRoot root) {
		PaletteDrawer group = new PaletteDrawer(
				BSPMessages.getString("SchemePaletteProvider.ConvertersGroup"), //$NON-NLS-1$
				null);

		ArrayList<PaletteEntry> entries = new ArrayList<PaletteEntry>();
		// ADC
		entries.add(new CombinedTemplateCreationEntry(
				BSPMessages.getString("SchemePaletteProvider.ADC"), //$NON-NLS-1$
				BSPMessages.getString("SchemePaletteProvider.ADCDescription"), //$NON-NLS-1$
				new SimpleFactory(ADCElement.class),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_ADC_16),
				BSP.getImages().getImageDescriptor(BSPImages.IMG_ADC_24)
		));
		group.addAll(entries);
	
		return group;
	}

}
