package interfaces;

/**
 * Interface for assignable area
 * @author muzahid@mpower-social.com
 * */
import java.util.List;

import models.User;

public interface Assignable {
	public void assignTo(User user);
	public void removeFrom(User user);
	public List<Assignable> getAssignable(User user);
	public boolean isAssignable(User user);
	public boolean isRemovable(User user);
}
