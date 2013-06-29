package freemind.clouds;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ListIterator;
import java.util.Vector;
import freemind.main.FreeMind;
import freemind.main.FreeMindMain;
import freemind.main.Tools;
import freemind.main.XMLElement;
import freemind.modes.ControllerAdapter;
import freemind.modes.MindMapNode;
import freemind.modes.NodeAdapter;
import freemind.modes.XMLElementAdapter;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.MindMapLayout;
import freemind.view.mindmapview.NodeView;
import freemind.modes.mindmapmode.MindMapXMLElement;
import freemind.modes.browsemode.BrowseXMLElement;
import freemind.modes.actions.MindMapActions;
import freemind.preferences.layout.OptionPanel;
import freemind.preferences.layout.OptionPanel.ColorProperty;
import freemind.preferences.layout.OptionPanel.KeyProperty;
import freemind.controller.actions.generated.instance.ObjectFactory;

public privileged aspect AbstractCloudAspect {

	pointcut instanceofCloudAdapter(XMLElement child)
    : execution(boolean XMLElementAdapter.instanceofCloudAdapter(..)) && args(child);

	pointcut getAdditionalCloudHeight(NodeView node)
	 : execution(int MindMapLayout.getAdditionalCloudHeight(..)) && args(node);

	pointcut inicializarCloudAction(ControllerAdapter cthis) 
	 : execution(* ControllerAdapter.inicializarCloudAction(..)) && this(cthis);

	pointcut enableCloud(ControllerAdapter cthis, boolean enabled) 
	 : execution(* ControllerAdapter.enableCloud(..)) 
		&& this(cthis) && args(enabled);

	pointcut createCloudAdapter(XMLElementAdapter cthis) 
	 : execution(* XMLElementAdapter.createCloudAdapterHook(..)) && this(cthis);

	pointcut getCloudHook(XMLElementAdapter cthis, XMLElement child,
			NodeAdapter node) 
		: execution(* XMLElementAdapter.getCloudHook(..)) && this(cthis) && args(child, node);

	pointcut setCloudPropertiesHook(XMLElementAdapter cthis, String name,
			String sValue) 
		: execution(* XMLElementAdapter.setCloudPropertiesHook(..)) && this(cthis) 
		&& args(name, sValue);

	pointcut saveCloudHook(NodeAdapter cthis, XMLElement node) 
	 : execution(* NodeAdapter.saveCloudHook(..)) && args(node) && 
	 this(cthis);
	
	pointcut paintChildren(Graphics graphics, MapView cthis)
	 : execution(* MapView.paintChildren(..))
		&& args(graphics) && this(cthis);
	
	pointcut addHeightOfTheCloud(NodeView source, Rectangle innerBounds)
	 : execution(* MapView.addHeightOfTheCloud(..))
	 && args(source, innerBounds);
	
	pointcut coordinatesCloudHook(NodeView cthis,
			int additionalDistanceForConvexHull, boolean byChildren)
		: execution(* NodeView.coordinatesCloudHook(..)) && this(cthis)
		&& args(additionalDistanceForConvexHull, byChildren);
	
	pointcut cloudPanelHook(OptionPanel cthis, Vector controls) 
	 : execution(* OptionPanel.cloudPanelHook(..)) && this(cthis) && args(controls);
	
	pointcut colorPropertyHook(OptionPanel cthis, Vector controls) 
	 : execution(* OptionPanel.colorPropertyHook(..)) && this(cthis) && args(controls);

	int around(NodeView node) : getAdditionalCloudHeight(node) {
		return node.getAdditionalCloudHeigth();
	}

	boolean around(XMLElement child) : instanceofCloudAdapter(child) {
		return child.getUserObject() instanceof CloudAdapter;
	}

	before(ControllerAdapter cthis) : inicializarCloudAction(cthis) {
		cthis.cloud = new CloudAction(cthis);
		cthis.cloudColor = new CloudColorAction(cthis);
	}

	before(ControllerAdapter cthis, boolean enabled) : enableCloud(cthis, enabled) {
		cthis.cloudColor.setEnabled(enabled);
	}

	before(XMLElementAdapter cthis) : createCloudAdapter(cthis) {
		cthis.userObject = cthis.createCloudAdapter(null, cthis.frame);
	}

	before(XMLElementAdapter cthis, XMLElement child, NodeAdapter node) 
	: getCloudHook(cthis, child, node) {

		CloudAdapter cloud = (CloudAdapter) child.getUserObject();
		cloud.setTarget(node);
		node.setCloud(cloud);
	}

	before(XMLElementAdapter cthis, String name, String sValue) 
	: setCloudPropertiesHook(cthis, name, sValue) {
		if (cthis.userObject instanceof CloudAdapter) {
			CloudAdapter cloud = (CloudAdapter) cthis.userObject;
			if (name.equals("STYLE")) {
				cloud.setStyle(sValue);
			} else if (name.equals("COLOR")) {
				cloud.setColor(Tools.xmlToColor(sValue));
			} else if (name.equals("WIDTH")) {
				cloud.setWidth(Integer.parseInt(sValue));
			}
			return;
		}
	}

	before(NodeAdapter cthis, XMLElement node) : saveCloudHook(cthis, node) {
		if (cthis.getCloud() != null) {
			XMLElement cloud = (cthis.getCloud()).save();
			node.addChild(cloud);
		}
	}

	before(Graphics graphics, MapView cthis) 
	: paintChildren(graphics, cthis) {
		cthis.paintClouds(cthis.rootView, graphics);
	}

	before(NodeView source, Rectangle innerBounds) :
		addHeightOfTheCloud(source, innerBounds) {
		int additionalCloudHeigth = (source.getAdditionalCloudHeigth() + 1) / 2;
		if (additionalCloudHeigth != 0) {
			innerBounds.grow(additionalCloudHeigth, additionalCloudHeigth);
		}
	}

	before(NodeView cthis, int additionalDistanceForConvexHull,
			boolean byChildren) :
				coordinatesCloudHook(cthis, additionalDistanceForConvexHull, byChildren) {
		MindMapCloud cloud = cthis.getModel().getCloud();

		// consider existing clouds of children
		if (byChildren && cloud != null) {
			additionalDistanceForConvexHull += CloudView.getAdditionalHeigth(
					cloud, cthis) / 2;
		}
	}

	before(OptionPanel cthis, Vector controls) : cloudPanelHook(cthis, controls) {
		controls.add(new KeyProperty(cthis.frame, null,
				"keystroke_node_toggle_cloud"));
	}

	before(OptionPanel cthis, Vector controls) : colorPropertyHook(cthis, controls) {
		controls.add(new ColorProperty("standardcloudcolor.tooltip",
				FreeMind.RESOURCES_CLOUD_COLOR, "#f0f0f0")); // #f0f0f0
	}

	public abstract CloudAdapter XMLElementAdapter.createCloudAdapter(
			NodeAdapter node, FreeMindMain frame);

	public CloudAdapter MindMapXMLElement.createCloudAdapter(NodeAdapter node,
			FreeMindMain frame) {
		return new MindMapCloudModel(node, frame);
	}

	public CloudAdapter BrowseXMLElement.createCloudAdapter(NodeAdapter node,
			FreeMindMain frame) {
		if (name.equals("cloud")) {
			return new BrowseCloudModel(node, frame);
		}
		return null;
	}

	private void NodeAdapter.changeChildCloudIterativeLevels(int deltaLevel) {
		for (ListIterator e = childrenUnfolded(); e.hasNext();) {
			NodeAdapter childNode = (NodeAdapter) e.next();
			MindMapCloud childCloud = childNode.getCloud();
			if (childCloud != null) {
				childCloud.changeIterativeLevel(deltaLevel);
			}
			childNode.changeChildCloudIterativeLevels(deltaLevel);
		}
	}

	public void NodeAdapter.setCloud(MindMapCloud cloud) {
		// Take care to keep the calculated iterative levels consistent
		if (cloud != null && this.cloud == null) {
			changeChildCloudIterativeLevels(1);
		} else if (cloud == null && this.cloud != null) {
			changeChildCloudIterativeLevels(-1);
		}
		this.cloud = cloud;
	}

	public MindMapCloud NodeAdapter.getCloud() {
		return this.cloud;
	}

	public CloudAction ControllerAdapter.cloud = null;
	public CloudColorAction ControllerAdapter.cloudColor = null;

	declare parents : MindMapActions extends CloudsInterface;
//	
	public interface CloudsInterface {
		void setCloud(MindMapNode node, boolean enable);
//
		void setCloudColor(MindMapNode node, Color color);
	}

	public void ControllerAdapter.setCloud(MindMapNode node, boolean enable) {
		cloud.setCloud(node, enable);
	}

	public void ControllerAdapter.setCloudColor(MindMapNode node, Color color) {
		cloudColor.setCloudColor(node, color);
	}

	interface CloudMapNodeInterface {
	}

	// alterado de protected pra public
	public MindMapCloud CloudMapNodeInterface.cloud;

	// public List CloudMapNodeInterface.children;

	// public ListIterator CloudMapNodeInterface.childrenUnfolded() {
	// return children != null ? children.listIterator()
	// : Collections.EMPTY_LIST.listIterator();
	// }

	public MindMapCloud CloudMapNodeInterface.getCloud() {
		return cloud;
	}

	public void MindMapNode.setCloud(MindMapCloud cloud) {
		// Take care to keep the calculated iterative levels consistent
		if (cloud != null && this.cloud == null) {
			changeChildCloudIterativeLevels(1);
		} else if (cloud == null && this.cloud != null) {
			changeChildCloudIterativeLevels(-1);
		}
		this.cloud = cloud;
	}

	// o ideal era que esse metodo estivesse declarado apenas no aspecto, nao eh
	// necessario
	// coloca-lo na interface
	private void MindMapNode.changeChildCloudIterativeLevels(int deltaLevel) {
		// objeto criado para chamar método definido em NodeAdapter
		for (ListIterator e = childrenUnfolded(); e.hasNext();) {
			NodeAdapter childNode = (NodeAdapter) e.next();
			MindMapCloud childCloud = getCloud();
			if (childCloud != null) {
				childCloud.changeIterativeLevel(deltaLevel);
			}
			changeChildCloudIterativeLevels(deltaLevel);
		}
	}

	declare parents : MindMapNode extends CloudMapNodeInterface;

	public void MapView.paintClouds(NodeView source, Graphics graphics) {
		for (ListIterator e = source.getChildrenViews().listIterator(); e
				.hasNext();) {
			NodeView target = (NodeView) e.next();
			if (target.getModel().getCloud() != null) {
				CloudView cloud = new CloudView(target.getModel().getCloud(),
						target);
				cloud.paint(graphics);
			}
			paintClouds(target, graphics);
		}
	}

	public int NodeView.getAdditionalCloudHeigth() {
		MindMapCloud cloud = getModel().getCloud();
		if (cloud != null) {
			return CloudView.getAdditionalHeigth(cloud, this);
		} else {
			return 0;
		}
	}

	public AddCloudXmlAction ObjectFactory.createAddCloudXmlAction()
			throws javax.xml.bind.JAXBException {
		return new AddCloudXmlActionImpl();
	}

	public CloudColorXmlAction ObjectFactory.createCloudColorXmlAction()
			throws javax.xml.bind.JAXBException {
		return new CloudColorXmlActionImpl();
	}

	public static final String FreeMind.RESOURCES_CLOUD_COLOR = "standardcloudcolor";
	
}