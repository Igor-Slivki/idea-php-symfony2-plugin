package fr.adrienbrault.idea.symfony2plugin.util;

import com.intellij.openapi.project.Project;
import fr.adrienbrault.idea.symfony2plugin.Settings;
import fr.adrienbrault.idea.symfony2plugin.util.dict.PhpTypeCache;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Espendiller <daniel@espendiller.net>
 */
public class PhpTypeCacheIndex {

    protected static Map<Project, Map<Object, PhpTypeCache>> instance = new HashMap<Project, Map<Object, PhpTypeCache>>();

    public static PhpTypeCache getInstance(Project project, Object object){

        Map<Object, PhpTypeCache> projectInstance = instance.get(project);

        if(projectInstance == null) {
            projectInstance = new HashMap<Object, PhpTypeCache>();
            instance.put(project, projectInstance);
        }

        PhpTypeCache projectPhpTypeInstance = projectInstance.get(object);
        if(projectPhpTypeInstance == null) {
            projectPhpTypeInstance = new PhpTypeCache();
            projectInstance.put(object, projectPhpTypeInstance);
        }

        if(projectPhpTypeInstance.getLastCacheClear() < System.currentTimeMillis() - 1000 * Settings.getInstance(project).phpTypesLifetimeSec) {
            projectPhpTypeInstance.clear();
        }

        return projectPhpTypeInstance;

    }

}
