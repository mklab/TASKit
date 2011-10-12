/**
 * 
 */
package org.mklab.taskit.client.activity;

import org.mklab.taskit.client.ClientFactory;
import org.mklab.taskit.client.LocalDatabase;
import org.mklab.taskit.client.Messages;
import org.mklab.taskit.client.ui.ProfileView;
import org.mklab.taskit.client.ui.TaskitView;

import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.ServerFailure;


/**
 * @author ishikura
 */
public class ProfileActivity extends TaskitActivity implements ProfileView.Presenter {

  /**
   * {@link ProfileActivity}オブジェクトを構築します。
   * 
   * @param clientFactory クライアントファクトリ
   */
  public ProfileActivity(ClientFactory clientFactory) {
    super(clientFactory);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected TaskitView createTaskitView(ClientFactory clientFactory) {
    final ProfileView profileView = clientFactory.getProfileView();
    profileView.setPresenter(this);

    return profileView;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onViewShown() {
    super.onViewShown();
    ((ProfileView)getTaskitView()).setUser(getLoginUser());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changePassword(String currentPassword, String password) {
    if (currentPassword.equals(password)) {
      showInformationDialog("Password not changed."); //$NON-NLS-1$
      return;
    }
    final Messages messages = getClientFactory().getMessages();
    getClientFactory().getRequestFactory().accountRequest().changeMyPassword(currentPassword, password).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        showInformationMessage(messages.changedPasswordMessage());
        clearLoginUserCache();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorMessage(messages.changedPasswordFailMessage() + ":" + error.getMessage()); //$NON-NLS-1$
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void changeUserName(String name) {
    final Messages messages = getClientFactory().getMessages();
    getClientFactory().getRequestFactory().userRequest().changeUserName(name).fire(new Receiver<Void>() {

      @Override
      public void onSuccess(@SuppressWarnings("unused") Void response) {
        showInformationMessage(messages.changedUserNameMessage());
        clearLoginUserCache();
      }

      /**
       * {@inheritDoc}
       */
      @Override
      public void onFailure(ServerFailure error) {
        showErrorMessage(messages.changedUserNameFailMessage() + ":" + error.getMessage()); //$NON-NLS-1$
      }
    });
  }

  void clearLoginUserCache() {
    getClientFactory().getLocalDatabase().clearCache(LocalDatabase.LOGIN_USER);
  }

}
