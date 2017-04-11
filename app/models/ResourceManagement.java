package models;

import play.data.validation.Required;
import play.data.validation.Unique;
import play.db.jpa.Blob;
import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by real on 4/2/17.
 */
@Entity
public class ResourceManagement extends Model {
    @Unique
    @Required
    public String contentTitle;

    public String categoryList;

    @Unique
    @Required
    public String resourceVersion;

    public String updated_at;

    //@ManyToOne
    public String update_by;

    public String filePath;


    public int status;

    public String comment;
}
