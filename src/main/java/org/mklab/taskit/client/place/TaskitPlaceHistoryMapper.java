/**
 * 
 */
package org.mklab.taskit.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;


/**
 * @author Yuhi Ishikura
 * @version $Revision$, Jan 22, 2011
 */
@WithTokenizers({Login.Tokenizer.class, StudentList.Tokenizer.class, Admin.Tokenizer.class, AttendanceList.Tokenizer.class, Student.Tokenizer.class, Profile.Tokenizer.class})
public interface TaskitPlaceHistoryMapper extends PlaceHistoryMapper {
  // empty
}
