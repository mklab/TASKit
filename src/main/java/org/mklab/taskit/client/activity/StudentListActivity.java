/**
 * 
 */
package org.mklab.taskit.client.activity;

import org.mklab.taskit.client.ClientFactory;
import org.mklab.taskit.client.model.StudentScoreModel;
import org.mklab.taskit.client.model.StudentScoreQuery;
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
 * @author Yuhi Ishikura
 * @version $Revision$, Jan 23, 2011
 */
public class StudentListActivity extends TaskitActivity implements StudentListView.Presenter {

  private StudentListView view;
  private StudentScoreQuery query;

  /**
   * {@link StudentListActivity}オブジェクトを構築します。
   * 
   * @param clientFactory クライアントファクトリ
   */
  public StudentListActivity(ClientFactory clientFactory) {
    super(clientFactory);
    this.query = new StudentScoreQuery(clientFactory.getRequestFactory());
  }

  /**
   * @see org.mklab.taskit.client.activity.TaskitActivity#createTaskitView(org.mklab.taskit.client.ClientFactory)
   */
  @Override
  protected TaskitView createTaskitView(ClientFactory clientFactory) {
    final StudentListView list = clientFactory.getStudentListView();
    this.view = list;
    list.setPresenter(this);

    getClientFactory().getRequestFactory().userRequest().getAllStudents().with("account").fire(new Receiver<List<UserProxy>>() { //$NON-NLS-1$

          @Override
          public void onSuccess(List<UserProxy> arg0) {
            list.setListData(arg0);

            final String initialSelection = getAccountIdInPlaceToken();
            if (initialSelection != null && initialSelection.length() > 0) {
              for (final UserProxy user : arg0) {
                if (initialSelection.equals(user.getAccount().getId())) {
                  list.setSelectedListData(user);
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
    return list;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void submit(ReportProxy report, int point) {
    final UserProxy user = this.view.getSelectedUser();
    final SubmissionRequest submissionRequest = getClientFactory().getRequestFactory().submissionRequest();
    submissionRequest.submit(user.getAccount(), report, point).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        // do nothing
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorMessage(error.getMessage());
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attend(LectureProxy lecture, AttendanceType type) {
    final UserProxy user = this.view.getSelectedUser();
    final AttendanceRequest attendanceRequest = getClientFactory().getRequestFactory().attendanceRequest();
    attendanceRequest.attend(user.getAccount(), lecture, type).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        // do nothing
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorMessage(error.getMessage());
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void uncall(AccountProxy user) {
    getClientFactory().getRequestFactory().helpCallRequest().cancelCall(user.getId()).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        showInformationMessage("Successfully canceled the call."); //$NON-NLS-1$
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorMessage("Failed to cancel the call. : " + error.getMessage()); //$NON-NLS-1$
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void listSelectionChanged(UserProxy selectedUser) {
    this.view.clearUserPage();
    fetchAndShowAsync(selectedUser);
  }

  private void fetchAndShowAsync(final UserProxy selectedUser) {
    this.query.query(selectedUser.getAccount().getId(), new StudentScoreQuery.Handler() {

      @SuppressWarnings({"unqualified-field-access", "synthetic-access"})
      @Override
      public void handleResult(StudentScoreModel model) {
        view.showUserPage(selectedUser, model);

        final StudentScoreModel.LectureScore latestRow = Util.getLatestScore(model);
        view.highlightRow(latestRow);
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
    fetchAndShowAsync(selectedUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(SubmissionProxy submission) {
    getClientFactory().getRequestFactory().submissionRequest().delete().using(submission).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        // do nothing
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorMessage("Failed to delete the submission. : " + error.getMessage()); //$NON-NLS-1$
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(AttendanceProxy attendance) {
    getClientFactory().getRequestFactory().attendanceRequest().delete().using(attendance).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        // do nothing
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorMessage("Failed to delete the attendance. : " + error.getMessage()); //$NON-NLS-1$
      }

    });

  }
}
