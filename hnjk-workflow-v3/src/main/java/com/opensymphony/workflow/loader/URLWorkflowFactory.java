/*
 * Copyright (c) 2002-2003 by OpenSymphony
 * All rights reserved.
 */
package com.opensymphony.workflow.loader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.workflow.FactoryException;
import com.opensymphony.workflow.loader.XMLWorkflowFactory.WorkflowConfig;


/**
 * @author Hani Suleiman
 * Date: May 10, 2002
 * Time: 11:59:47 AM
 */

@SuppressWarnings({"unchecked"})
public class URLWorkflowFactory extends AbstractWorkflowFactory implements Serializable {
    //~ Instance fields ////////////////////////////////////////////////////////

    /**
	 * 
	 */
	private static final long serialVersionUID = 4218402254193587895L;
	private transient Map<String,WorkflowDescriptor> cache = new HashMap();

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
        return false;
    }

    @Override
	public String getName() {
        return "";
    }

    @Override
	public WorkflowDescriptor getWorkflow(String name, boolean validate) throws FactoryException {
        boolean useCache = "true".equals(getProperties().getProperty("cache", "false"));

        if (useCache) {
            WorkflowDescriptor descriptor = (WorkflowDescriptor) cache.get(name);

            if (descriptor != null) {
                return descriptor;
            }
        }

        try {
            URL url = new URL(name);
            WorkflowDescriptor descriptor = WorkflowLoader.load(url, validate);

            if (useCache) {
                cache.put(name, descriptor);
            }

            return descriptor;
        } catch (Exception e) {
            throw new FactoryException("Unable to find workflow " + name, e);
        }
    }

    @Override
	public String[] getWorkflowNames() throws FactoryException {
        throw new FactoryException("URLWorkflowFactory does not contain a list of workflow names");
    }

    @Override
	public void createWorkflow(String name) {
    }

    @Override
	public boolean removeWorkflow(String name) throws FactoryException {
        throw new FactoryException("remove workflow not supported");
    }

    @Override
	public void renameWorkflow(String oldName, String newName) {
    }

    @Override
	public void save() {
    }

    
    /* (non-Javadoc)
	 * @see com.opensymphony.workflow.loader.WorkflowFactory#getWorkflows()
	 */
	@Override
	public Map<String, WorkflowConfig> getWorkflows()throws FactoryException {
	
		throw new FactoryException("URLWorkflowFactory does not support get all workflows!");
	}

	@Override
	public boolean saveWorkflow(String name, WorkflowDescriptor descriptor, boolean replace) throws FactoryException {
        WorkflowDescriptor c = (WorkflowDescriptor) cache.get(name);
        URL url;

        try {
            url = new URL(name);
        } catch (MalformedURLException ex) {
            throw new FactoryException("workflow '" + name + "' is an invalid url:" + ex);
        }

        boolean useCache = "true".equals(getProperties().getProperty("cache", "false"));

        if (useCache && (c != null) && !replace) {
            return false;
        }

        if (new File(url.getFile()).exists() && !replace) {
            return false;
        }

        Writer out;

        try {
            out = new OutputStreamWriter(new FileOutputStream(url.getFile() + ".new"), "utf-8");
        } catch (FileNotFoundException ex) {
            throw new FactoryException("Could not create new file to save workflow " + url.getFile());
        } catch (UnsupportedEncodingException ex) {
            throw new FactoryException("utf-8 encoding not supported, contact your JVM vendor!");
        }

        //write it out to a new file, to ensure we don't end up with a messed up file if we're interrupted halfway for some reason
        PrintWriter writer = new PrintWriter(new BufferedWriter(out));
        writer.println(WorkflowDescriptor.XML_HEADER);
        writer.println(WorkflowDescriptor.DOCTYPE_DECL);
        descriptor.writeXML(writer, 0);
        writer.flush();
        writer.close();

        //now lets rename
        File original = new File(url.getFile());
        File backup = new File(url.getFile() + ".bak");
        File updated = new File(url.getFile() + ".new");
        boolean isOK = original.renameTo(backup);

        if (!isOK) {
            throw new FactoryException("Unable to backup original workflow file " + original + " to " + backup + ", aborting save");
        }

        isOK = updated.renameTo(original);

        if (!isOK) {
            throw new FactoryException("Unable to rename new  workflow file " + updated + " to " + original + ", aborting save");
        }

        backup.delete();

        if (useCache) {
            cache.put(name, descriptor);
        }

        return true;
    }
}
