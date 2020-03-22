package org.jdesktop.lg3d.scenemanager.utils.plugin;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;

import org.jdesktop.lg3d.displayserver.AppConnectorPrivate;
import org.jdesktop.lg3d.scenemanager.config.ConfigData;

public class SceneManagerPluginConfig extends ConfigData {
    
    private String pluginClass;
    
    private boolean singletonPlugin;
    
    private String classPathJars;
    
    public SceneManagerPlugin createPlugin() {
        SceneManagerPlugin plugin= null;
        try {
            if(getClassPathJars()!= null) {
                URLClassLoader loader = (URLClassLoader)getClass().getClassLoader();
                Class sysclass = URLClassLoader.class;
                
                Method method = sysclass.getDeclaredMethod("addURL",new Class[]{URL.class});
                method.setAccessible(true);
                URL[] newUrls = parseClassPathJars(getClassPathJars());
                for(URL newUrl : newUrls)
                    method.invoke(loader,new Object[]{ newUrl });
            }
            Class clazz= Class.forName(pluginClass);
            plugin= (SceneManagerPlugin)clazz.newInstance();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error instanitating plugin class " + pluginClass);
            e.printStackTrace();
        }
        return plugin;
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.pluginClass = pluginClass;
    }
    
    public boolean isSingletonPlugin() {
        return singletonPlugin;
    }
    
    public void setSingletonPlugin(boolean singletonPlugin) {
        this.singletonPlugin = singletonPlugin;
    }
    
    @Override
    public void doConfig() {
        AppConnectorPrivate.getAppConnector().postEvent(this, null);
    }

    public String getClassPathJars() {
        return classPathJars;
    }

    public void setClassPathJars(String classPathJars) {
        this.classPathJars = classPathJars;
    }
    
}
