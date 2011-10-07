/**
 * 
 */
package org.mklab.taskit.client.activity;

import org.mklab.taskit.client.ClientFactory;
import org.mklab.taskit.client.ui.AdminView;
import org.mklab.taskit.client.ui.LectureEditor;
import org.mklab.taskit.client.ui.TaskitView;
import org.mklab.taskit.shared.LectureProxy;
import org.mklab.taskit.shared.LectureRequest;

import java.util.ArrayList;
import java.util.List;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;


/**
 * @author yuhi
 */
public class AdminActivity extends TaskitActivity implements AdminView.Presenter {

  private LectureRequest lectureRequest;
  private LectureEditor lectureEditor;

  /**
   * {@link AdminActivity}オブジェクトを構築します。
   * 
   * @param clientFactory クライアントファクトリ
   */
  public AdminActivity(ClientFactory clientFactory) {
    super(clientFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected TaskitView createTaskitView(final ClientFactory clientFactory) {
    final AdminView view = new AdminView(clientFactory);
    view.setPresenter(this);
    this.lectureEditor = view.getLectureEditor();
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onViewShown() {
    super.onViewShown();
    updateLectureListData();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save(LectureProxy lecture) {
    this.lectureRequest.updateOrCreate().using(lecture).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        updateLectureListData();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        updateLectureListData();
        showErrorMessage(error.getMessage());
      }
    });
    this.lectureRequest = null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LectureProxy edit(LectureProxy lecture) {
    this.lectureRequest = getClientFactory().getRequestFactory().lectureRequest();
    if (lecture == null) return this.lectureRequest.create(LectureProxy.class);

    return this.lectureRequest.edit(lecture);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public LectureProxy newLecture() {
    return edit(null);
  }

  void updateLectureListData() {
    final LectureRequest req = getClientFactory().getRequestFactory().lectureRequest();
    req.getAllLectures().fire(new Receiver<List<LectureProxy>>() {

      @SuppressWarnings("synthetic-access")
      @Override
      public void onSuccess(List<LectureProxy> response) {
        final List<LectureProxy> editableList = new ArrayList<LectureProxy>();
        for (LectureProxy lecture : response) {
          editableList.add(lecture);
        }
        AdminActivity.this.lectureEditor.setLectures(editableList);
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(LectureProxy lecture) {
    this.lectureRequest.delete().using(lecture).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        updateLectureListData();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        updateLectureListData();
        showErrorMessage(error.getMessage());
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void selectedTabChanged() {
    // TODO Auto-generated method stub

  }
}
