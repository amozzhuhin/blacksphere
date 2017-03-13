package edu.nops.blacksphere.ide.launch;

import edu.nops.blacksphere.ide.BSP;
import edu.nops.blacksphere.ide.launch.eclipse.EclipseController;

/**
 * Отдельный эмулятор устройства. Управляет запуском
 * отдельного процесса Eclipse.
 */
public class StandaloneEmulator extends EclipseController {
	/**
	 * Создать отдельный эмулятор устройства
	 * @param args массив опций запуска
	 *     <code>-eclipseHome dir</code> определяет директорию установки Eclipse.
	 *     Он должен быть приведён, если текущий каталог не является
	 *     каталогом установки Eklipse. Другие опции соответствуют
	 *     опция принимаемым приложением Eclipse.
	 */
	public StandaloneEmulator(String[] args) {
		super(BSP.EMULATOR_APPLICATION_ID, args);
	}

}
