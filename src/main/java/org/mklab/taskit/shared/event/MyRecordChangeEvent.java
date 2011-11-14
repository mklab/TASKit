/**
 * 
 */
package org.mklab.taskit.shared.event;

import org.mklab.taskit.server.auth.Invoker;
import org.mklab.taskit.shared.UserType;

import de.novanic.eventservice.client.event.Event;


/**
 * 学生の成績に変更があった場合に発生するイベントです。
 * 
 * @author Yuhi Ishikura
 */
@Invoker({UserType.STUDENT})
public class MyRecordChangeEvent implements Event {

  /** for serialization. */
  private static final long serialVersionUID = -5258139335369899430L;

  /**
   * {@link MyRecordChangeEvent}オブジェクトを構築します。
   */
  public MyRecordChangeEvent() {
    // for serialization.
  }

}
