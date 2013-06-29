package freemind.icons;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import freemind.controller.Controller;

public class ToggleLeftToolbarAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private Controller controller;

	private boolean leftToolbarVisible = true;

	public ToggleLeftToolbarAction(Controller controller) {
		super(controller.getResourceString("toggle_left_toolbar"));
		this.controller = controller;
		setEnabled(true);
	}

	public void actionPerformed(ActionEvent event) {
		leftToolbarVisible = !leftToolbarVisible;
		setLeftToolbarVisible(leftToolbarVisible);
	}

	public void setLeftToolbarVisible(boolean visible) {
		if (controller.getMode() != null
				&& controller.getMode().getLeftToolBar() != null) {
			leftToolbarVisible = visible;
			controller.getMode().getLeftToolBar()
					.setVisible(leftToolbarVisible);
		}
	}

	// Esse método não existia... Ele foi criado pra ser acessado pelo aspecto!
	public boolean getLeftToolbarVisible() {
		return leftToolbarVisible;
	}

}