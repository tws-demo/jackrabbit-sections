package school.thoughtworks.twice.bean;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(jcrType="ocm:quiz", discriminator=false)
public class Quiz {

    @Field(path = true)
    private String path;
    @Field(jcrName="ocm:title")
    private String title;
    @Field(jcrName="ocm:definition")
    private String definition;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}
