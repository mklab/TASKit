/**
 * 
 */
package org.mklab.taskit.client.activity;

import org.mklab.taskit.client.ClientFactory;
import org.mklab.taskit.client.LocalDatabase;
import org.mklab.taskit.client.model.StudentwiseRecordModel;
import org.mklab.taskit.client.model.StudentwiseRecordQuery;
import org.mklab.taskit.client.place.StudentList;
import org.mklab.taskit.client.ui.StudentListView;
import org.mklab.taskit.client.ui.TaskitView;
import org.mklab.taskit.shared.AccountProxy;
import org.mklab.taskit.shared.AttendanceProxy;
import org.mklab.taskit.shared.AttendanceRequest;
import org.mklab.taskit.shared.AttendanceType;
import org.mklab.taskit.shared.LectureProxy;
import org.mklab.taskit.shared.ReportProxy;
import org.mklab.taskit.shared.SubmissionProxy;
import org.mklab.taskit.shared.SubmissionRequest;
import org.mklab.taskit.shared.UserProxy;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;


/**
 * 学生別の成績を表示するアクティビティです。
 * 
 * @author Yuhi Ishikura
 * @version $Revision$, Jan 23, 2011
 */
public class StudentListActivity extends TaskitActivity implements StudentListView.Presenter {

  private static final boolean TOKEN_FOLLOWS_STUDENT_SELECTION = true;
  private StudentListView view;
  private StudentwiseRecordQuery query;

  /**
   * {@link StudentListActivity}オブジェクトを構築します。
   * 
   * @param clientFactory クライアントファクトリ
   */
  public StudentListActivity(ClientFactory clientFactory) {
    super(clientFactory);
    this.query = new StudentwiseRecordQuery(clientFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected TaskitView createTaskitView(ClientFactory clientFactory) {
    final StudentListView list = clientFactory.getStudentListView();
    this.view = list;
    list.setPresenter(this);
    return list;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onViewShown() {
    super.onViewShown();
    final StudentListView list = (StudentListView)getTaskitView();

    getClientFactory().getLocalDatabase().getCacheOrExecute(LocalDatabase.STUDENT_LIST, new Receiver<List<UserProxy>>() {

      @SuppressWarnings("synthetic-access")
      @Override
      public void onSuccess(List<UserProxy> arg0) {
        showInformationMessage(getClientFactory().getMessages().fetchedUserListMessage());
        list.setListData(arg0);

        final String initialSelection = getAccountIdInPlaceToken();
        if (initialSelection != null && initialSelection.length() > 0) {
          for (final UserProxy user : arg0) {
            if (initialSelection.equals(user.getAccount().getId())) {
              list.setSelectedListData(user);
              showStudentRecordAsync(user);
              break;
            }
          }
        }
      }

      private String getAccountIdInPlaceToken() {
        final Place place = getClientFactory().getPlaceController().getWhere();
        return ((StudentList)place).getStudentId();
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onStop() {
    checkOutAsync();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void submit(final ReportProxy report, final int point) {
    final UserProxy user = this.view.getSelectedUser();
    final SubmissionRequest submissionRequest = getClientFactory().getRequestFactory().submissionRequest();
    submissionRequest.submit(user.getAccount(), report, point).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        showInformationMessage(getClientFactory().getMessages().submittedReportMessage(user.getAccount().getId(), report.getTitle()));
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorDialog(error.getMessage());
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attend(final LectureProxy lecture, AttendanceType type) {
    final UserProxy user = this.view.getSelectedUser();
    final AttendanceRequest attendanceRequest = getClientFactory().getRequestFactory().attendanceRequest();
    attendanceRequest.attend(user.getAccount(), lecture, type).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        showInformationMessage(getClientFactory().getMessages().submittedAttendanceMessage(user.getAccount().getId(), lecture.getTitle()));
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorDialog(error.getMessage());
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void uncall(final AccountProxy user) {
    getClientFactory().getRequestFactory().helpCallRequest().cancelCall(user.getId()).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        showInformationDialog(getClientFactory().getMessages().uncalledMessage(user.getId()));
        getClientFactory().getHelpCallWatcher().updateHelpCallCount();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorDialog(getClientFactory().getMessages().uncallFailedMessage(user.getId()) + ":" + error.getMessage()); //$NON-NLS-1$
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void listSelectionChanged(UserProxy selectedUser) {
    this.view.clearUserPage();
    if (TOKEN_FOLLOWS_STUDENT_SELECTION) {
      getClientFactory().getPlaceController().goTo(new StudentList(selectedUser.getAccount().getId()));
    } else {
      showStudentRecordAsync(selectedUser);
    }
  }

  /**
   * 与えられたユーザーの成績を非同期で取得しビューに表示します。
   * 
   * @param student 表示するユーザー
   */
  private void showStudentRecordAsync(final UserProxy student) {
    showInformationMessage(getClientFactory().getMessages().fetchingUserRecordsMessage(student.getAccount().getId()));
    this.query.query(student.getAccount().getId(), new StudentwiseRecordQuery.Handler() {

      @SuppressWarnings({"unqualified-field-access", "synthetic-access"})
      @Override
      public void handleResult(StudentwiseRecordModel model) {
        showInformationMessage(getClientFactory().getMessages().fetchedUserRecordsMessage(student.getAccount().getId()));
        view.showUserPage(student, model);

        final StudentwiseRecordModel.LectureScore latestRow = Util.getLatestScore(model);
        if (latestRow != null) {
          view.highlightRow(latestRow);
        }
        checkInAsync(student);
      }
    });
  }

  /**
   * 与えられたユーザーにチェックインしたこと(閲覧中であること)をサーバーに非同期で送信します。
   * 
   * @param user 閲覧中のユーザー
   */
  private void checkInAsync(UserProxy user) {
    getClientFactory().getRequestFactory().checkMapRequest().checkIn(user.getAccount()).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        // do nothing
      }
    });
  }

  /**
   * 現在閲覧中のユーザーからチェックアウトしたこと(閲覧終了したこと)をサーバーに非同期で送信します。
   * <p>
   * 閲覧中でない場合でも実行できます。
   */
  private void checkOutAsync() {
    getClientFactory().getRequestFactory().checkMapRequest().checkOut().fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        // do nothing
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void reloadUserPage() {
    this.view.clearUserPage();
    final UserProxy selectedUser = this.view.getSelectedUser();
    showStudentRecordAsync(selectedUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final SubmissionProxy submission) {
    getClientFactory().getRequestFactory().submissionRequest().delete().using(submission).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        showInformationMessage(getClientFactory().getMessages().deletedSubmissionMessage(submission.getReport().getTitle()));
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        reloadUserPage();
        showErrorDialog(getClientFactory().getMessages().deletedSubmissionFailMessage(submission.getReport().getTitle()) + ":" + error.getMessage()); //$NON-NLS-1$
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final AttendanceProxy attendance) {
    final AttendanceRequest req = getClientFactory().getRequestFactory().attendanceRequest();
    req.delete().using(attendance).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        showInformationMessage(getClientFactory().getMessages().deletedAttendanceMessage(attendance.getLecture().getTitle()));
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        reloadUserPage();
        showErrorDialog(getClientFactory().getMessages().deletedAttendanceFailMessage(attendance.getLecture().getTitle()) + ":" + error.getMessage()); //$NON-NLS-1$
      }

    });

  }
}
