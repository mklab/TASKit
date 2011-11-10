/**
 * 
 */
package org.mklab.taskit.client.ui;

import org.mklab.taskit.client.model.StudentwiseRecordModel;
import org.mklab.taskit.shared.UserProxy;


/**
 * 学生が成績データ閲覧に利用するビューです。
 * 
 * @author ishikura
 */
public interface StudentView extends TaskitView {

  /**
   * 学生の成績データを設定します。
   * 
   * @param model 成績データ
   */
  void setModel(StudentwiseRecordModel model);

  /**
   * プレゼンターを設定します。
   * 
   * @param presenter プレゼンター
   */
  void setPresenter(Presenter presenter);

  /**
   * 与えられた行をハイライトします。
   * 
   * @param rowData ハイライトする行
   */
  void highlightRow(StudentwiseRecordModel.LectureScore rowData);

  /**
   * 呼び出し中かどうか設定します。
   * 
   * @param calling 呼び出し中かどうか
   */
  void setCalling(boolean calling);

  /**
   * 呼び出し順序を取得します。
   * 
   * @param position 自分が何番目の呼び出しかどうか
   */
  void setHelpCallPosition(int position);

  /**
   * ログインユーザー情報を設定します。
   * 
   * @param loginUser ログインユーザー情報
   */
  void setLoginUser(UserProxy loginUser);

  /**
   * 得点率を設定します。
   * 
   * @param percentage 得点率
   */
  void setScore(double percentage);

  /**
   * {@link StudentView}で利用するプレゼンターです。
   * 
   * @author ishikura
   */
  public static interface Presenter {

    /**
     * 先生やTAの呼び出しを希望します。
     * 
     * @param message メッセージ
     */
    void call(String message);

    /**
     * 呼び出しをキャンセルします。
     */
    void uncall();
  }

}
