/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.loader;

import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.InvalidWorkflowDescriptorException;

import org.w3c.dom.*;

import java.io.*;

import java.net.URL;

import java.util.*;

import javax.xml.parsers.*;


/**
 * XML配置解析工厂，用来初始化、配置流程配置和流程定义.<p>
 * @author Hani Suleiman
 * Date: May 10, 2002
 * Time: 11:30:41 AM
 * @author Marco.hu 
 * 
 */
public class XMLWorkflowFactory extends AbstractWorkflowFactory implements Serializable {
    //~ Static fields/initializers /////////////////////////////////////////////

    private static final long serialVersionUID = 452755218478437087L;

    //~ Instance fields ////////////////////////////////////////////////////////

    protected Map<String,WorkflowConfig> workflows;//流程定义
    
    protected boolean reload;//是否允许热加载

    //~ Methods ////////////////////////////////////////////////////////////////

    @Override
	public void setLayout(String workflowName, Object layout) {
    }

    @Override
	public Object getLayout(String workflowName) {
        return null;
    }

    @Override
	public boolean isModifiable(String name) {
        return true;
    }

    @Override
	public String getName() {
        return "";
    }

    /**
     * 根据工作流name获取工作流定义
     */
    @Override
	public WorkflowDescriptor getWorkflow(String name, boolean validate) throws FactoryException {
        WorkflowConfig c = (WorkflowConfig) workflows.get(name);

        if (c == null) {
            throw new FactoryException("Unknown workflow name \"" + name + '\"');
        }

        if (c.descriptor != null) {
            if (reload) {
                File file = new File(c.url.getFile());

                if (file.exists() && (file.lastModified() > c.lastModified)) {
                    c.lastModified = file.lastModified();
                    loadWorkflow(c, validate);
                }
            }
        } else {
            loadWorkflow(c, validate);
        }

        c.descriptor.setName(name);

        return c.descriptor;
    }

    /**
     * 获取工作流定义的name集合
     */
    @Override
	public String[] getWorkflowNames() {
        int i = 0;
        String[] res = new String[workflows.keySet().size()];
        Iterator it = workflows.keySet().iterator();

        while (it.hasNext()) {
            res[i++] = (String) it.next();
        }

        return res;
    }
    
    
    /* (non-Javadoc)
	 * @see com.opensymphony.workflow.loader.WorkflowFactory#getWorkflows()
	 */
	@Override
	public Map<String, WorkflowConfig> getWorkflows()	throws FactoryException {		
		return workflows;
	}

	@Override
	public void createWorkflow(String name) {
    }

    /**
     * 初始化配置，获取workflows.xml定义的流程
     */
    @Override
	public void initDone() throws FactoryException {
        reload = "true".equals(getProperties().getProperty("reload", "false"));

        String name = getProperties().getProperty("resource", "workflows.xml");
        InputStream is = getInputStream(name);

        if (is == null) {
            throw new FactoryException("Unable to find workflows file '" + name + "' in classpath");
        }

        try {        	
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            DocumentBuilder db;

            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new FactoryException("Error creating document builder", e);
            }

            Document doc = db.parse(is);

            Element root = (Element) doc.getElementsByTagName("workflows").item(0);
            workflows = new HashMap<String,WorkflowConfig>();

            String basedir = getBaseDir(root);

            List<Element> list = XMLUtil.getChildElements(root, "workflow");

            for(Element element : list){
            	 WorkflowConfig config = new WorkflowConfig(basedir, 
            			 element.getAttribute("type"),
            			 element.getAttribute("location"),
            			 element.getAttribute("cnname"));
                 workflows.put(element.getAttribute("name"), config);
            }            
          
        } catch (Exception e) {
            throw new InvalidWorkflowDescriptorException("Error in workflow config", e);
        }
    }

    @Override
	public boolean removeWorkflow(String name) throws FactoryException {
        throw new FactoryException("remove workflow not supported");

        //WorkflowConfig workflow = (WorkflowConfig)workflows.remove(name);
        //if(workflow == null) return false;
        //if(workflow.descriptor != null)
        //{
        //
        //}
        //return true;
    }

    @Override
	public void renameWorkflow(String oldName, String newName) {
    }

    @Override
	public void save() {
    }

    @Override
	public boolean saveWorkflow(String name, WorkflowDescriptor descriptor, boolean replace) throws FactoryException {
        WorkflowConfig c = (WorkflowConfig) workflows.get(name);

        if ((c != null) && !replace) {
            return false;
        }

        if (c == null) {
            throw new UnsupportedOperationException("Saving of new workflow is not currently supported");
        }

        Writer out;

        // [KAP] comment this line to disable all the validation while saving a workflow
        //descriptor.validate();
        try {
            out = new OutputStreamWriter(new FileOutputStream(c.url.getFile() + ".new"), "utf-8");
        } catch (FileNotFoundException ex) {
            throw new FactoryException("Could not create new file to save workflow " + c.url.getFile());
        } catch (UnsupportedEncodingException ex) {
            throw new FactoryException("utf-8 encoding not supported, contact your JVM vendor!");
        }

        writeXML(descriptor, out);

        //write it out to a new file, to ensure we don't end up with a messed up file if we're interrupted halfway for some reason
        //now lets rename
        File original = new File(c.url.getFile());
        File backup = new File(c.url.getFile() + ".bak");
        File updated = new File(c.url.getFile() + ".new");
        boolean isOK = !original.exists() || original.renameTo(backup);

        if (!isOK) {
            throw new FactoryException("Unable to backup original workflow file " + original + " to " + backup + ", aborting save");
        }

        isOK = updated.renameTo(original);

        if (!isOK) {
            throw new FactoryException("Unable to rename new  workflow file " + updated + " to " + original + ", aborting save");
        }

        backup.delete();

        return true;
    }

    /**
     * Get where to find workflow XML files.
     * @param root The root element of the XML file.
     * @return The absolute base dir used for finding these files or null.
     */
    protected String getBaseDir(Element root) {
        String basedir = root.getAttribute("basedir");

        if (basedir.length() == 0) {
            // No base dir defined
            return null;
        }

        if (new File(basedir).isAbsolute()) {
            // An absolute base dir defined
            return basedir;
        } else {
            // Append the current working directory to the relative base dir
            return new File(System.getProperty("user.dir"), basedir).getAbsolutePath();
        }
    }

    /**
     * Load the workflow config file from the current context classloader.
     * The search order is:
     * <li>Specified URL</li>
     * <li>&lt;name&gt;</li>
     * <li>/&lt;name&gt;</li>
     * <li>META-INF/&lt;name&gt;</li>
     * <li>/META-INF/&lt;name&gt;</li>
     */
    protected InputStream getInputStream(String name) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = null;

        if ((name != null) && (name.indexOf(":/") > -1)) {
            try {
                is = new URL(name).openStream();
            } catch (Exception e) {
            }
        }

        if (is == null) {
            try {
                is = classLoader.getResourceAsStream(name);
            } catch (Exception e) {
            }
        }

        if (is == null) {
            try {
                is = classLoader.getResourceAsStream('/' + name);
            } catch (Exception e) {
            }
        }

        if (is == null) {
            try {
                is = classLoader.getResourceAsStream("META-INF/" + name);
            } catch (Exception e) {
            }
        }

        if (is == null) {
            try {
                is = classLoader.getResourceAsStream("/META-INF/" + name);
            } catch (Exception e) {
            }
        }

        return is;
    }

    protected void writeXML(WorkflowDescriptor descriptor, Writer out) {
        PrintWriter writer = new PrintWriter(new BufferedWriter(out));
        writer.println(WorkflowDescriptor.XML_HEADER);
        writer.println(WorkflowDescriptor.DOCTYPE_DECL);
        descriptor.writeXML(writer, 0);
        writer.flush();
        writer.close();
    }

    private void loadWorkflow(WorkflowConfig c, boolean validate) throws FactoryException {
        try {
            c.descriptor = WorkflowLoader.load(c.url, validate);
        } catch (Exception e) {
            throw new FactoryException("Error in workflow descriptor: " + c.url, e);
        }
    }

   
    
    /**
     * workflows.xml配置对象.
     * @see workflow.xml
     */
    public static class WorkflowConfig implements Serializable {
        private static final long serialVersionUID = 4939957922893602958L;
        public String location;//流程定义文件位置
        public String cnname;//工作流中文名称
        public String type; //  file,  URL, service
        public URL url;//url
        public  WorkflowDescriptor descriptor;//工作流描述对象
        public long lastModified;//最后修改时间

        /**
         * 构造流程配置
         * @param basedir 流程定义文件根路径
         * @param type	类型
         * @param location 位置
         */
        public WorkflowConfig(String basedir, String type, String location,String cnname) {
            if ("URL".equals(type)) {
                try {
                    url = new URL(location);

                    File file = new File(url.getFile());

                    if (file.exists()) {
                        lastModified = file.lastModified();
                    }
                } catch (Exception ex) {
                }
            } else if ("file".equals(type)) {
                try {
                    File file = new File(basedir, location);
                    url = file.toURL();
                    lastModified = file.lastModified();
                } catch (Exception ex) {
                }
            } else {
                url = Thread.currentThread().getContextClassLoader().getResource(location);
            }

            this.type = type;
            this.location = location;
            this.cnname = cnname;
        }
    }
}
