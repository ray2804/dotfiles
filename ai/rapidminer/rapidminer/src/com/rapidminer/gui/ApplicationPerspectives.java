/*
 *  RapidMiner
 *
 *  Copyright (C) 2001-2013 by Rapid-I and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://rapid-i.com
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.rapidminer.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;

import com.rapidminer.gui.actions.WorkspaceAction;
import com.rapidminer.gui.docking.RapidDockingToolbar;
import com.rapidminer.gui.processeditor.ProcessLogTab;
import com.rapidminer.gui.processeditor.results.ResultTab;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.gui.tools.ResourceMenu;
import com.rapidminer.gui.tools.SwingTools;
import com.rapidminer.tools.FileSystemService;
import com.rapidminer.tools.I18N;
import com.rapidminer.tools.LogService;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableResolver;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingContext;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.ws.WSDockKey;

/** Collection of {@link Perspective}s that can be applied, saved, created.
 * 
 * @author Simon Fischer
 *
 */
public abstract class ApplicationPerspectives {

    public final Action RESTORE_DEFAULT_ACTION = new ResourceAction("restore_predefined_perspective_default") {
        private static final long serialVersionUID = 1L;
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!current.isUserDefined()) {
                restoreDefault(current.getName());
                current.apply(context);
            }
        }
    };

    private final ButtonGroup workspaceButtonGroup = new ButtonGroup();

    private final JMenu workspaceMenu = new ResourceMenu("perspectives");

    private final ButtonGroup workspaceMenuGroup = new ButtonGroup();

    private Perspective current;

    private final DockingContext context;

    private final Map<String,Perspective> perspectives = new LinkedHashMap<String,Perspective>();

    private RapidDockingToolbar workspaceToolBar;
    
    private LinkedList<PerspectiveChangeListener> perspectiveChangeListenerList;

    public ApplicationPerspectives(DockingContext context) {
        this.context = context;
        context.setDockableResolver(new DockableResolver() {
            @Override
            public Dockable resolveDockable(String key) {
                if (key.startsWith(ResultTab.DOCKKEY_PREFIX)) {
                    ResultTab tab = new ResultTab(key);
                    tab.showResult(null);
                    return tab;
                } else if (key.startsWith(ProcessLogTab.DOCKKEY_PREFIX)) {
                    ProcessLogTab tab = new ProcessLogTab(key);
                    tab.setDataTableViewer(null);
                    return tab;
                } else {
                    return null;
                }
            }
        });
        this.makePredefined();
    }

    public void showPerspective(Perspective perspective) {
        if (current == perspective) {
            return;
        }
        if (current != null) {
            current.store(context);
        }
        perspective.apply(context);
        current = perspective;
        RESTORE_DEFAULT_ACTION.setEnabled(!current.isUserDefined());
        
        
        //TODO: change to listener mechanism
        MainFrame mainFrame = RapidMinerGUI.getMainFrame();
        if(mainFrame != null) {
        	
        	// check all ConditionalActions on perspective switch
        	mainFrame.getActions().enableActions();
        	
        	// try to request focus for the process renderer so actions are enabled after perspective switch and
        	// ProcessRenderer is visible
        	if (mainFrame.getProcessPanel().getProcessRenderer().isShowing()) {
        		mainFrame.getProcessPanel().getProcessRenderer().requestFocusInWindow();
        	}
        	
        	// disable close action on the result dockable when in pre-defined results perspective
        	//TODO: commented out because a fast switch between perspectives during this call breaks the Dockable (turns blue)
        	/*
        	if (perspective.getName().contains("result")) {
        		mainFrame.getResultDisplay().getDockKey().setCloseEnabled(false);
        	} else {
        		mainFrame.getResultDisplay().getDockKey().setCloseEnabled(true);
        	}*/
        }
        this.notifyChangeListener();
    }

    public JMenu getWorkspaceMenu() {
        return workspaceMenu;
    }

    public RapidDockingToolbar getWorkspaceToolBar() {
        if (workspaceToolBar == null) {
            workspaceToolBar = new RapidDockingToolbar("perspectives");
            Enumeration<AbstractButton> e = workspaceButtonGroup.getElements();
            while (e.hasMoreElements()) {
                AbstractButton b = e.nextElement();
                workspaceToolBar.add(b);
            }
        }
        return workspaceToolBar;
    }

    /**
     * Checks if the given string is valid as name of a new perspective.
     * @param name
     * @return validity
     */
    public boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        if (name.equals("")) {
            return false;
        }
        for (Perspective perspective : perspectives.values()) {
            if (perspective.getName().toLowerCase().equals(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @throws IllegalArgumentException if name is already used
     */
    public Perspective addPerspective(String name, boolean userDefined) {
        final Perspective p = new Perspective(this, name);
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Duplicate perspective name: " + name);
        }
        p.setUserDefined(userDefined);
        perspectives.put(name, p);
        Action action = new WorkspaceAction(this, p, name);
        if (p.isUserDefined()) {
            action.putValue(Action.NAME, name);
            action.putValue(Action.LARGE_ICON_KEY, SwingTools.createIcon("24/" + I18N.getMessage(I18N.getGUIBundle(), "gui.action.workspace_user.icon")));
            action.putValue(Action.SMALL_ICON, SwingTools.createIcon("16/" + I18N.getMessage(I18N.getGUIBundle(), "gui.action.workspace_user.icon")));
            action.putValue(Action.SHORT_DESCRIPTION, I18N.getMessage(I18N.getGUIBundle(), "gui.action.workspace_user.tip", name));
        }
        final JToggleButton button = new JToggleButton(action);
        if (p.isUserDefined()) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    evaluatePopup(e);
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    evaluatePopup(e);
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    evaluatePopup(e);
                }
                private void evaluatePopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        JPopupMenu menu = new JPopupMenu();
                        menu.add(new ResourceAction("delete_perspective") {
                            private static final long serialVersionUID = -1068519938053845994L;
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                deletePerspective(p);
                            }
                        });
                        menu.show(button, e.getX(), e.getY());
                    }
                }
            });
        } else {
            button.setText("");
        }
        workspaceButtonGroup.add(button);
        JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(action);
        workspaceMenuGroup.add(menuItem);
        workspaceMenu.add(menuItem);
        if (workspaceToolBar != null) {
            workspaceToolBar.add(button);
        }
        return p;
    }

    /** Saves all perspectives to the users config directory. */
    public void saveAll() {
        //LogService.getRoot().log(Level.CONFIG, "Saving perspectives.");
        LogService.getRoot().log(Level.CONFIG, "com.rapidminer.gui.ApplicationPerspectives.saving_perspectives");
        if (current != null) {
            current.store(context);
        }
        for (Perspective perspective : perspectives.values()) {
            perspective.save();
        }
    }

    /** Loads all perspectives from the users config directory. */
    public void loadAll() {
        //LogService.getRoot().log(Level.CONFIG, "Loading perspectives.");
    	LogService.getRoot().log(Level.CONFIG, "com.rapidminer.gui.ApplicationPerspectives.loading_perspectives");
        for (Perspective perspective : perspectives.values()) {
            perspective.load();
        }
        File[] userPerspectiveFiles = FileSystemService.getUserRapidMinerDir().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith("vlperspective-user-");
            }
        });
        for (File file : userPerspectiveFiles) {
            String name = file.getName();
            name = name.substring("vlperspective-user-".length());
            name = name.substring(0, name.length() - ".xml".length());
            Perspective perspective = createUserPerspective(name, false);
            perspective.load();
        }
    }

    public Perspective getCurrentPerspective() {
        return current;
    }

    /** Switches to the given perspective, storing the current one. */
    public void showPerspective(String name) {
        Enumeration<AbstractButton> e = workspaceButtonGroup.getElements();
        Enumeration<AbstractButton> i = workspaceMenuGroup.getElements();
        for (Perspective perspective : perspectives.values()) {
            AbstractButton b = e.nextElement();
            AbstractButton m = i.nextElement();
            if (perspective.getName().equals(name)) {
                showPerspective(perspective);
                b.setSelected(true);
                m.setSelected(true);
            }
        }
    }

    /** Creates a user-defined perspectives, and possibly switches to this new perspective immediately.
     *  The new perspective will be a copy of the current one. */
    public Perspective createUserPerspective(String name, boolean show) {
        Perspective perspective = addPerspective(name, true);
        perspective.store(context);
        if (show) {
            showPerspective(name);
        }
        return perspective;
    }

    private void deletePerspective(Perspective p) {
        for (int i = 0; i < workspaceMenu.getMenuComponentCount(); i++) {
            Component c = workspaceMenu.getMenuComponent(i);
            if ((c instanceof JMenuItem) && ((JMenuItem)c).getText().equals(p.getName())) {
                workspaceMenu.remove((JMenuItem)c);
            }
        }
        Enumeration<AbstractButton> e = workspaceButtonGroup.getElements();
        AbstractButton found = null;
        while (e.hasMoreElements()) {
            AbstractButton b = e.nextElement();
            if (b.getText().equals(p.getName())) {
                found = b;
                break;
            }
        }
        if (found != null) {
            workspaceButtonGroup.remove(found);
            workspaceMenuGroup.remove(found);
            if (workspaceToolBar != null) {
                workspaceToolBar.remove(found);
                workspaceToolBar.revalidate();
            }
        }
        perspectives.remove(p.getName());
        p.delete();
        if ((current == p) && !perspectives.isEmpty()) {
            showPerspective(perspectives.values().iterator().next().getName());
        }
    }

    /** Shows the tab as a child of the given dockable in all perspectives. */
    public void showTabInAllPerspectives(Dockable dockable, Dockable parent) {
        DockableState dstate = context.getDockableState(dockable);
        if ((dstate != null) && (!dstate.isClosed())) {
            return;
        }

        DockingDesktop dockingDesktop = context.getDesktopList().get(0);
        context.registerDockable(dockable);
        //dockingDesktop.registerDockable(dockable);

        WSDockKey parentKey = new WSDockKey(parent.getDockKey().getKey());
        WSDockKey key = new WSDockKey(dockable.getDockKey().getKey());
        for (Perspective persp : perspectives.values()) {
            if (persp == current) {
                continue;
            }

            // We don't need to show it if
            // 1. We don't know the parent
            // 2. We already have the child
            boolean containsParent = persp.getWorkspace().getDesktop(0).containsNode(parentKey);
            boolean containsChild = persp.getWorkspace().getDesktop(0).containsNode(key);
            if (containsParent && !containsChild) {
                persp.getWorkspace().getDesktop(0).createTab(parentKey, key, 1);
            }
        }

        DockableState[] states = dockingDesktop.getDockables();
        for (DockableState state : states) {
            if ((state.getDockable() == parent) && !state.isClosed()) {
                dockingDesktop.createTab(state.getDockable(), dockable, 1, true);
                break;
            }
        }
    }

    public void removeFromAllPerspectives(Dockable dockable) {
        //DockingDesktop dockingDesktop = (DockingDesktop)context.getDesktopList().get(0);
        //dockingDesktop.getDockableState(dockable)
        //dockingDesktop.unregisterDockable(dockable);
        //dockingDesktop.close(dockable);
        //dockingDesktop.remove(dockable);
        context.unregisterDockable(dockable);

        // TODO: Remove from Workspaces
        WSDockKey key = new WSDockKey(dockable.getDockKey().getKey());
        for (Perspective persp : perspectives.values()) {
            if (persp == current) {
                continue;
            }
            persp.getWorkspace().getDesktop(0).removeNode(key);
        }
    }

    protected abstract void makePredefined();

    protected abstract void restoreDefault(String perspectiveName);

    protected Perspective getPerspective(String name) {
        Perspective result = perspectives.get(name);
        if (result != null) {
            return result;
        } else {
            throw new NoSuchElementException("No such perspective: "+name);
        }
    }
    
    public void addPerspectiveChangeListener(PerspectiveChangeListener listener) {
    	if(perspectiveChangeListenerList == null) {
    		perspectiveChangeListenerList = new LinkedList<PerspectiveChangeListener>();
    	}
    	perspectiveChangeListenerList.add(listener);
    }
    
    public boolean removePerspectiveChangeListener(PerspectiveChangeListener listener) {
    	if(perspectiveChangeListenerList == null) {
    		return false;
    	}
    	return perspectiveChangeListenerList.remove(listener);
    }
    
    public void notifyChangeListener() {
    	if(perspectiveChangeListenerList != null) {
    		LinkedList<PerspectiveChangeListener> list = (LinkedList<PerspectiveChangeListener>) perspectiveChangeListenerList.clone();
    		for(PerspectiveChangeListener listener : list) {
    			listener.perspectiveChangedTo(current);
    		}
    	}
    }
    
}
