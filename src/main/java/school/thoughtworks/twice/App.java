package school.thoughtworks.twice;

import org.apache.jackrabbit.core.nodetype.InvalidNodeTypeDefException;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.xml.NodeTypeReader;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.version.Version;
import org.apache.jackrabbit.ocm.version.VersionIterator;
import org.apache.jackrabbit.spi.QNodeTypeDefinition;
import school.thoughtworks.twice.bean.Quiz;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.version.VersionException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class App {

    public static final String OCM_NAMESPACE = "http://jackrabbit.apache.org/ocm";

    public static void main(String[] args) throws RepositoryException, IOException, InvalidNodeTypeDefException {
        Session session = SessionHelper.getSession();


        String[] jcrNamespaces = session.getWorkspace().getNamespaceRegistry().getPrefixes();
        Boolean existNS = Arrays.stream(jcrNamespaces).anyMatch(e-> e.equals("ocm"));
        if(!existNS) {
            session.getWorkspace().getNamespaceRegistry().registerNamespace("ocm", OCM_NAMESPACE);
        }

        InputStream xml = new FileInputStream("./src/main/resources/custom.xml");
        QNodeTypeDefinition[] types = NodeTypeReader.read(xml);

        Workspace workspace = session.getWorkspace();
        NodeTypeManager ntMgr = workspace.getNodeTypeManager();
        NodeTypeRegistry ntReg = ((NodeTypeManagerImpl) ntMgr).getNodeTypeRegistry();

        for(QNodeTypeDefinition def: types) {
            try {
                ntReg.getNodeTypeDef(def.getName());
            } catch (NoSuchNodeTypeException e) {
                ntReg.registerNodeType(def);
            }
        }

        session.save();
//        session.logout();
//
//        session = SessionHelper.getSession();

        ObjectContentManager ocm = SessionHelper.createObjectContentManager(session);

        Quiz quiz = new Quiz();
        quiz.setPath("/quizes");
        quiz.setTitle("quiz01");
        quiz.setDefinition("我去");

        ocm.insert(quiz);
        ocm.save();

        quiz.setTitle("quiz02");
        ocm.checkout("/quizes");
        quiz.setTitle("quiz02");
        ocm.update(quiz);
        ocm.save();
        ocm.checkin("/quizes");
//        quiz = (Quiz) ocm.getObject("/quiz");
        System.out.println(quiz);

        VersionIterator versionIterator = ocm.getAllVersions("/quizes");
        while (versionIterator.hasNext())
        {
            Version version = (Version) versionIterator.next();
            System.out.println("version found : "+ version.getName() + " - " +
                    version.getPath() + " - " + version.getCreated().getTime());
        }

        ocm.checkout("/quizes");
        ocm.remove("/quizes");
        ocm.checkin("/quizes");

//

//
//        quiz = (Quiz) ocm.getObject("/quiz");
//
//        quiz.setDefinition("不能骂脏话");
//

    }

    public static void printVersion(ObjectContentManager ocm) throws VersionException {
        VersionIterator versionIterator = ocm.getAllVersions("/quiz");
        while (versionIterator.hasNext())
        {
            Version version = (Version) versionIterator.next();
            System.out.println("version found : "+ version.getName() + " - " +
                    version.getPath() + " - " + version.getCreated().getTime());
        }
    }

    public static <T>  void printNode (ObjectContentManager ocm, String path) {
        T obj = (T)ocm.getObject(path);
        System.out.println(obj);
    }
}
