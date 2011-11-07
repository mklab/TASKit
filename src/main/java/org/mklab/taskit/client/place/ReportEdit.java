/**
 * 
 */
package org.mklab.taskit.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;


/**
 * 課題データ編集アクティビティの場所を表すクラスです。
 * 
 * @author Yuhi Ishikura
 */
public class ReportEdit extends Place {

  /** このアクティビティの場所を表すオブジェクトです。 */
  public static final Place INSTANCE = new ReportEdit();

  /**
   * @author Yuhi Ishikura
   */
  public static class Tokenizer implements PlaceTokenizer<ReportEdit> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportEdit getPlace(@SuppressWarnings("unused") String token) {
      return new ReportEdit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getToken(@SuppressWarnings("unused") ReportEdit place) {
      return ""; //$NON-NLS-1$
    }

  }
}
