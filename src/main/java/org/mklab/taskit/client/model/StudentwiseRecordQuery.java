/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mklab.taskit.client.model;

import org.mklab.taskit.client.ClientFactory;
import org.mklab.taskit.client.LocalDatabase;
import org.mklab.taskit.shared.AttendanceProxy;
import org.mklab.taskit.shared.LectureProxy;
import org.mklab.taskit.shared.SubmissionProxy;

import java.util.List;

import com.google.web.bindery.requestfactory.shared.Receiver;


/**
 * 成績データの非同期取得を行うクラスです。
 * 
 * @author Yuhi Ishikura
 */
public class StudentwiseRecordQuery {

  private ClientFactory clientFactory;

  /**
   * {@link StudentwiseRecordQuery}オブジェクトを構築します。
   * 
   * @param clientFactory クライアントファクトリ
   */
  public StudentwiseRecordQuery(ClientFactory clientFactory) {
    if (clientFactory == null) throw new NullPointerException();
    this.clientFactory = clientFactory;
  }

  ClientFactory getClientFactory() {
    return this.clientFactory;
  }

  /**
   * 成績データを取得します。
   * 
   * @param accountId 生徒アカウントID
   * @param handler 取得結果を処理するハンドラ
   */
  public void query(final String accountId, final Handler handler) {
    this.clientFactory.getLocalDatabase().execute(LocalDatabase.LECTURE_LIST, new Receiver<List<LectureProxy>>() {

      @SuppressWarnings("nls")
      @Override
      public void onSuccess(List<LectureProxy> response) {
        final StudentwiseRecordModel model = new StudentwiseRecordModel(response);
        handler.handleResult(model);

        getClientFactory().getRequestFactory().submissionRequest().getSubmissionsByAccountId(accountId).with("submitter", "report.lecture").fire(new Receiver<List<SubmissionProxy>>() {

          @Override
          public void onSuccess(@SuppressWarnings("hiding") List<SubmissionProxy> response) {
            model.setSubmissions(response);
            handler.handleResult(model);
          }

        });
        getClientFactory().getRequestFactory().attendanceRequest().getAttendancesByAccountId(accountId).with("lecture").fire(new Receiver<List<AttendanceProxy>>() {

          @Override
          public void onSuccess(@SuppressWarnings("hiding") List<AttendanceProxy> response) {
            model.setAttendances(response);
            handler.handleResult(model);
          }
        });
      }
    });
  }

  /**
   * 成績データを取得します。
   * 
   * @param handler 取得結果を処理するハンドラ
   */
  public void query(final Handler handler) {
    this.clientFactory.getLocalDatabase().execute(LocalDatabase.LECTURE_LIST, new Receiver<List<LectureProxy>>() {

      @SuppressWarnings("nls")
      @Override
      public void onSuccess(List<LectureProxy> response) {
        final StudentwiseRecordModel model = new StudentwiseRecordModel(response);
        handler.handleResult(model);

        getClientFactory().getRequestFactory().submissionRequest().getMySubmissions().with("submitter", "report.lecture").fire(new Receiver<List<SubmissionProxy>>() {

          @Override
          public void onSuccess(@SuppressWarnings("hiding") List<SubmissionProxy> response) {
            model.setSubmissions(response);
            handler.handleResult(model);
          }

        });
        getClientFactory().getRequestFactory().attendanceRequest().getMyAttendances().with("lecture").fire(new Receiver<List<AttendanceProxy>>() {

          @Override
          public void onSuccess(@SuppressWarnings("hiding") List<AttendanceProxy> response) {
            model.setAttendances(response);
            handler.handleResult(model);
          }
        });
      }
    });
  }

  /**
   * 取得した成績データを処理するインターフェースです。
   * 
   * @author ishikura
   */
  public interface Handler {

    /**
     * 取得結果を処理します。
     * 
     * @param model 取得した成績データ
     */
    void handleResult(StudentwiseRecordModel model);
  }

}
