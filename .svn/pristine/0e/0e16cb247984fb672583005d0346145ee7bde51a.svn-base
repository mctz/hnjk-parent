package com.hnjk.core.rao.dao.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.event.EventSource;
import org.hibernate.event.def.DefaultFlushEventListener;

/**
 * 
 * <code>PatchedDefaultFlushEventListener</code><p>
 * 如果使用默认的flushEventListener，在onPostUpdate()中遍历event.getOldState()或event.getState()会出现错误：<p>
	ERROR AssertionFailure:22 - an assertion failure occured (this may indicate a bug in Hibernate, but is more likely due to unsafe use of the session)<p>
	这是一个hibernate的bug，主要是因为在获取对象属性（一对一或一对多），当前session已经flush了，而无法lazy,详情请参见:https://hibernate.onjira.com/browse/HHH-2763<p>
	这里，参见了https://svn.intuitive-collaboration.com/RiskAnalytics/trunk/riskanalytics-grails/src/java/org/codehaus/groovy/grails/orm/hibernate/events/PatchedDefaultFlushEventListener.java
	grails的做法，创建一个补丁的listener，然后加入到监听器中。
 * @author： 广东学苑教育发展有限公司.
 * @since： 2011-11-18 下午03:09:01
 * @see 
 * @version 1.0
 */
public class PatchedDefaultFlushEventListener extends DefaultFlushEventListener {
	private static final long serialVersionUID = -7413770767669684078L;
    private static final Log LOG = LogFactory.getLog(PatchedDefaultFlushEventListener.class);

    @Override
    protected void performExecutions(EventSource session) throws HibernateException {
        session.getPersistenceContext().setFlushing(true);
        try {
            session.getJDBCContext().getConnectionManager().flushBeginning();
            // we need to lock the collection caches before
            // executing entity inserts/updates in order to
            // account for bidi associations
            session.getActionQueue().prepareActions();
            session.getActionQueue().executeActions();
        }
        catch (HibernateException he) {
            LOG.error("Could not synchronize database state with session", he);
            throw he;
        }
        finally {
            session.getPersistenceContext().setFlushing(false);
            session.getJDBCContext().getConnectionManager().flushEnding();
        }
    }
}
