package school.thoughtworks.twice;

import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import school.thoughtworks.twice.bean.Quiz;
import school.thoughtworks.twice.bean.Section;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wjlin on 1/17/17.
 */
public class SessionHelper {
    private static Session session;

    public static Session getSession() throws RepositoryException {
//        if(session == null) {
            Repository repository = JcrUtils.getRepository();
            session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));
//        }

        return session;
    }

    public static ObjectContentManager createObjectContentManager(Session session) {
        List classes = new ArrayList<Class>();
        classes.add(Quiz.class);
        classes.add(Section.class);

        Mapper mapper = new AnnotationMapperImpl(classes);
        ObjectContentManager ocm = new ObjectContentManagerImpl(session, mapper);
        return ocm;
    }
}
