/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: HookFactory.java,v 1.1.4.8 2005/04/17 08:29:06 christianfoltin Exp $*/
package freemind.extensions;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.xml.bind.Unmarshaller;

import freemind.common.JaxbTools;
import freemind.controller.actions.generated.instance.Plugin;
import freemind.controller.actions.generated.instance.PluginActionType;
import freemind.controller.actions.generated.instance.PluginClasspathType;
import freemind.controller.actions.generated.instance.PluginModeType;
import freemind.controller.actions.generated.instance.PluginRegistrationType;
import freemind.main.FreeMindMain;
import freemind.modes.MindMapNode;

/**
 * @author christianfoltin
 * 
 * @file HookFactory.java
 * @package freemind.modes
 */
/**
 * @author foltin
 *
 */
public class HookFactory {
	/**
	 * Match xml files in the accessories/plugin directory and not in its
	 * subdirectories.
	 */
	private static final String pluginPrefixRegEx = "(accessories\\.|)plugins\\.[^.]*";

	private FreeMindMain frame;

	// Logging:
	private java.util.logging.Logger logger;

	private HashMap pluginInfo;

	private Vector allPlugins;

	/** Contains PluginRegistrationType -> PluginType relations. */
	private HashMap allRegistrations;

	/** Contains PluginType -> Object (baseClass) relations. */
	private HashMap allRegistrationInstances;

	private ImportWizard importWizard;

	/**
	 *  
	 */
	public HookFactory(FreeMindMain frame) {
		this.frame = frame;
		logger = frame.getLogger(this.getClass().getName());
		allRegistrationInstances = new HashMap();
	}

	/**
	 * @return a string vector with representatives for plugins.
	 */
	public Vector getPossibleNodeHooks(Class mode) {
		return searchFor(NodeHook.class, mode);
	}

	/**
	 * @return a string vector with representatives for plugins.
	 */
	public Vector getPossibleModeControllerHooks(Class mode) {
		return searchFor(ModeControllerHook.class, mode);
	}

	/**
	 * @param class1
	 * @param mode
	 * @return a string vector with representatives for plugins.
	 */
	private Vector searchFor(Class baseClass, Class mode) {
		actualizePlugins();
		Vector returnValue = new Vector();
		for (Iterator i = allPlugins.iterator(); i.hasNext();) {
			String label = (String) i.next();
			HookDescriptor descriptor = getHookDescriptor(label);
			//Properties prop = descriptor.properties;
			try {
				logger.finest("Loading: " + label);
				if (baseClass.isAssignableFrom(Class.forName(descriptor
						.getBaseClass()))) {
					// the plugin inherits from the baseClass, we carry on to
					// look for the mode
					for (Iterator j = descriptor.getModes().iterator(); j
							.hasNext();) {
						String pmode = (String) j.next();
						if (pmode.equals(mode.getPackage().getName())) {
							// add the class:
							returnValue.add(label);
						}

					}
				}
			} catch (ClassNotFoundException e) {
				logger.severe("Class not found.");
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	/**
	 *  
	 */
	private void actualizePlugins() {
		if (importWizard == null) {
			importWizard = new ImportWizard();
			importWizard.CLASS_LIST.clear();
			importWizard.buildClassList();
		}
		pluginInfo = new HashMap();
		allPlugins = new Vector();
		allRegistrations = new HashMap();
		// the unmarshaller:
		Unmarshaller unmarshaller = JaxbTools.getInstance()
				.createUnmarshaller();
		// the loop
		for (Iterator i = importWizard.CLASS_LIST.iterator(); i.hasNext();) {
			String xmlPluginFile = (String) i.next();
			if (xmlPluginFile.matches(pluginPrefixRegEx)) {
				// make file name:
				xmlPluginFile = xmlPluginFile.replace('.', '/') /*
																 * Here, this is
																 * not the
																 * File.separatorChar!!!
																 */
						+ importWizard.lookFor;
				// this is one of our plugins:
				URL pluginURL = getClassLoader(Collections.EMPTY_LIST)
						.getResource(xmlPluginFile);
				// unmarshal xml:
				Plugin plugin = null;
				try {
					logger.finest("Reading: " + xmlPluginFile + " from "
							+ pluginURL);
					InputStream in = pluginURL.openStream();
					unmarshaller.setValidating(true);
					plugin = (Plugin) unmarshaller.unmarshal(in);
				} catch (Exception e) {
					// error case
					logger.severe(e.getLocalizedMessage());
					e.printStackTrace();
					continue;
				}
				// plugin is loaded.
				for (Iterator j = plugin.getPluginAction().iterator(); j
						.hasNext();) {
					PluginActionType action = (PluginActionType) j.next();
					pluginInfo.put(action.getLabel(), new HookDescriptor(frame,
							action, plugin));
					allPlugins.add(action.getLabel());
				}
				for (Iterator k = plugin.getPluginRegistration().iterator(); k
						.hasNext();) {
					PluginRegistrationType registration = (PluginRegistrationType) k
							.next();
					allRegistrations.put(registration, plugin);

				}
			}
		}
	}

	public ModeControllerHook createModeControllerHook(String hookName) {
		HookDescriptor descriptor = getHookDescriptor(hookName);
		return (ModeControllerHook) createJavaHook(hookName, descriptor);
	}

	private MindMapHook createJavaHook(String hookName,
			HookDescriptor descriptor) {
		try {
			// construct class loader:
			List pluginClasspathList = descriptor.getPluginBase()
					.getPluginClasspath();
			ClassLoader loader = getClassLoader(pluginClasspathList);
			// constructed.
			Class hookClass = Class.forName(descriptor.getClassName(), true,
					loader);
			MindMapHook hook = (MindMapHook) hookClass.newInstance();
			decorateHook(hookName, descriptor, hook);
			return hook;
		} catch (Exception e) {
			logger
					.severe("Error occurred loading hook: "
							+ descriptor.getClassName() + "\nException:"
							+ e.toString());
			return null;
		}
	}

	private HashMap classLoaderCache = new HashMap();

	/**
	 * @param pluginClasspathList
	 * @return
	 * @throws MalformedURLException
	 */
	private ClassLoader getClassLoader(List pluginClasspathList) {
		String key = createPluginClasspathString(pluginClasspathList);
		if (classLoaderCache.containsKey(key))
			return (ClassLoader) classLoaderCache.get(key);
		try {
			URL[] urls = new URL[pluginClasspathList.size() + 1];
			int j = 0;
			//logger.info("freemind.base.dir is " + getFreemindBaseDir());
			urls[j++] = new File(getFreemindBaseDir()).toURL();
			for (Iterator i = pluginClasspathList.iterator(); i.hasNext();) {
				PluginClasspathType classPath = (PluginClasspathType) i.next();
				// new version of classpath resolution suggested by ewl under 
				// patch [ 1154510 ] Be able to give absolute classpath entries in plugin.xml
				File file = new File(classPath.getJar());
				if (! file.isAbsolute()) {
					file = new File(getFreemindBaseDir(),
							classPath.getJar());
				}
				// end new version by ewl.
//				File file = new File(getFreemindBaseDir() + File.separator
//						+ classPath.getJar());
				logger.info("file " + file.toString() + " exists = "
						+ file.exists());
				urls[j++] = file.toURL();
			}
			ClassLoader loader = new URLClassLoader(urls, this.getClass()
					.getClassLoader());
			classLoaderCache.put(key, loader);
			return loader;
		} catch (MalformedURLException e) {
			logger.severe(e.getMessage());
			return this.getClass().getClassLoader();
		}
	}

	/** This string is used to identify known classloaders as they
	 *  are cached.
	 * 
	 * @param pluginClasspathList
	 * @return
	 */
	private String createPluginClasspathString(List pluginClasspathList) {
		String result = "";
		for (Iterator i = pluginClasspathList.iterator(); i.hasNext();) {
			PluginClasspathType type = (PluginClasspathType) i.next();
			result += type.getJar() + ",";
		}
		return result;
	}

	/**
	 * @return
	 */
	public static String getFreemindBaseDir() {
		return System.getProperty("freemind.base.dir", ".");
	}

	/**
	 * Do not call this method directly. Call ModeController.createNodeHook
	 * instead.
	 */
	public NodeHook createNodeHook(String hookName) {
		logger.finest("CreateNodeHook: " + hookName);
		HookDescriptor descriptor = getHookDescriptor(hookName);
		return (NodeHook) createJavaHook(hookName, descriptor);
	}

	/**
	 * @param node
	 * @param hookName
	 * @return null if not present, the hook otherwise.
	 */
	public PermanentNodeHook getHookInNode(MindMapNode node, String hookName) {
		// search for already instanciated hooks of this type:
		for (Iterator i = node.getActivatedHooks().iterator(); i.hasNext();) {
			PermanentNodeHook otherHook = (PermanentNodeHook) i.next();
			if (otherHook.getName().equals(hookName)) {
				// there is already one instance.
				return otherHook;
			}
		}
		return null;
	}

	private void decorateHook(String hookName, HookDescriptor descriptor,
			MindMapHook hook) {
		hook.setProperties(descriptor.getProperties());
		hook.setName(hookName);
		Object baseClass = getPluginBaseClass(descriptor); 
		hook.setPluginBaseClass(baseClass);
	}

	/**
	 * @param descriptor
	 * @return
	 */
	private Object getPluginBaseClass(HookDescriptor descriptor) {
		// test if registration is present:
		Object baseClass = null;
		if(allRegistrationInstances.containsKey(descriptor.getPluginBase())){
			baseClass = allRegistrationInstances.get(descriptor.getPluginBase());
		}
		return baseClass;
	}

	/**
	 * @return
	 */
	protected FreeMindMain getFrame() {
		return frame;
	}

	public String getProperty(String hookName, String prop) {
		HookDescriptor descriptor = getHookDescriptor(hookName);
		return descriptor.getProperties().getProperty(prop);
	}

	/**
	 * @param action
	 */
	public void decorateAction(String hookName, AbstractAction action) {
		HookDescriptor descriptor = getHookDescriptor(hookName);
		String name = descriptor.getName();
		if (name != null) {
			action.putValue(AbstractAction.NAME, name);
		} else {
			action.putValue(AbstractAction.NAME, descriptor.getClassName());
		}
		String docu = descriptor.getDocumentation();
		if (docu != null) {
			action.putValue(AbstractAction.SHORT_DESCRIPTION, docu);
			action.putValue(AbstractAction.LONG_DESCRIPTION, docu);
		}
		String icon = descriptor.getIconPath();
		if (icon != null) {
			ImageIcon imageIcon = new ImageIcon(getClassLoader(
					Collections.EMPTY_LIST).getResource(icon));
			action.putValue(AbstractAction.SMALL_ICON, imageIcon);
		}
		String key = descriptor.getKeyStroke();
		if (key != null)
			action.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke
					.getKeyStroke(key));

	}

	/**
	 * @return returns a list of menu position strings for the
	 *         StructuredMenuHolder.
	 */
	public List getHookMenuPositions(String hookName) {
		HookDescriptor descriptor = getHookDescriptor(hookName);
		return descriptor.menuPositions;
	}

	/**
	 * This method takes into account, that there are on off hooks that need a
	 * special treatment for their menues like adjusting the checkbox.
	 * 
	 * @param hookAction
	 * @return returns a new JMenuItem for the given hookAction.
	 */
	public JMenuItem getHookMenuItem(String hookName, AbstractAction hookAction) {
		return new JMenuItem(hookAction);
	}

	/**
	 * @param permHook
	 * @return
	 */
	public HookInstanciationMethod getInstanciationMethod(String hookName) {
		HookDescriptor descriptor = getHookDescriptor(hookName);
		return descriptor.getInstanciationMethod();
	}

	public static class RegistrationContainer {
		public Class hookRegistrationClass;

		public boolean isPluginBase;

		public Plugin correspondingPlugin;

		RegistrationContainer() {
			// package protected constructor.
		}
	}

	/**
	 * Each Plugin can have a list of HookRegistrations that are called after
	 * the corresponding mode is enabled. (Like singletons.) One of these can
	 * operate as the pluginBase that is accessible to every normal
	 * plugin_action via the getPluginBaseClass method.
	 * 
	 * @return A list of RegistrationContainer elements. The field
	 *         hookRegistrationClass of RegistrationContainer is a class that is
	 *         (probably) of HookRegistration type. You have to register every
	 *         registration via the registerRegistrationContainer method when
	 *         instanciated (this is typically done in the ModeController).
	 */
	public List getRegistrations(Class mode) {
		actualizePlugins();
		Vector returnValue = new Vector();
		for (Iterator i = allRegistrations.keySet().iterator(); i.hasNext();) {
			PluginRegistrationType registration = (PluginRegistrationType) i
					.next();
			boolean modeFound = false;
			for (Iterator j = (registration.getPluginMode()).iterator(); j
					.hasNext();) {
				PluginModeType possibleMode = (PluginModeType) j.next();
				if (mode.getPackage().getName().equals(
						possibleMode.getClassName())) {
					modeFound = true;
				}
			}
			if (!modeFound)
				continue;
			try {
				Plugin plugin = (Plugin) allRegistrations.get(registration);
				ClassLoader loader = getClassLoader(plugin.getPluginClasspath());
				RegistrationContainer container = new RegistrationContainer();
				Class hookRegistrationClass = Class.forName(registration
						.getClassName(), true, loader);
				container.hookRegistrationClass = hookRegistrationClass;
				container.correspondingPlugin = plugin;
				container.isPluginBase = registration.isIsPluginBase();
				returnValue.add(container);
			} catch (ClassNotFoundException e) { 
				e.printStackTrace();
			}
		}
		return returnValue;
	}

	/** See getRegistrations.
	 * @param container
	 * @param instanciatedRegistrationObject
	 */
	public void registerRegistrationContainer(RegistrationContainer container,
			HookRegistration instanciatedRegistrationObject) {
		// registration only for pluginBases.
		if (container.isPluginBase) {
			allRegistrationInstances.put(container.correspondingPlugin,
					instanciatedRegistrationObject);
		}
	}
	
	/** 
	 * @param hookName
	 * @return the base class if declared and successfully instanciated
	 * or NULL.
	 */
	public Object getPluginBaseClass(String hookName) {
		logger.finest("getPluginBaseClass: " + hookName);
		HookDescriptor descriptor = getHookDescriptor(hookName);
		return getPluginBaseClass(descriptor);
	}

	/**
	 * @param hookName
	 */
	private HookDescriptor getHookDescriptor(String hookName) {
		HookDescriptor descriptor = (HookDescriptor) pluginInfo.get(hookName);
		if (hookName == null || descriptor == null)
			throw new IllegalArgumentException("Unknown hook name " + hookName);
		return descriptor;
	}
}