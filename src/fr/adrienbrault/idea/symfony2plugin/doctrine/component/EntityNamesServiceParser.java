package fr.adrienbrault.idea.symfony2plugin.doctrine.component;

import fr.adrienbrault.idea.symfony2plugin.util.service.AbstractServiceParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityNamesServiceParser extends AbstractServiceParser {


    protected Map<String, String> entityNameMap = new ConcurrentHashMap<String, String>();

    @Override
    public String getXPathFilter() {
        return "/container/services/service[@id[starts-with(.,'doctrine.orm.')]]//call[@method='setEntityNamespaces']//argument[@key]";
    }

    public void parser(InputStream file) {
        NodeList nodeList = this.parserer(file);

        if(nodeList == null) {
            return;
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element node = (Element) nodeList.item(i);
            this.entityNameMap.put(node.getAttribute("key"), "\\" + node.getTextContent());
        }

    }

    public Map<String, String> getEntityNameMap() {
        return entityNameMap;
    }

}