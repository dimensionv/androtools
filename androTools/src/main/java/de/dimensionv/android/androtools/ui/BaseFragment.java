/**
 *
 */
package de.dimensionv.android.androtools.ui;

import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseIntArray;

import com.actionbarsherlock.app.SherlockFragment;

/**
 * @author mjoellnir
 * 
 */
public class BaseFragment extends SherlockFragment {

  private SparseIntArray requestCodes = null;

  /**
   * Registers request code (used in
   * {@link #startActivityForResult(Intent, int)}).
   * 
   * @param requestCode
   *          the request code.
   * @param id
   *          the fragment ID (can be {@link Fragment#getId()} of
   *          {@link Fragment#hashCode()}).
   */
  public void registerRequestCode(int requestCode, int id) {
    getRequestCodes().put(requestCode, id);
  }

  @Override
  public void startActivityForResult(Intent intent, int requestCode) {
    Fragment parent = getParentFragment();
    if(parent instanceof BaseFragment) {
      ((BaseFragment) parent).registerRequestCode(requestCode, hashCode());
      parent.startActivityForResult(intent, requestCode);
    } else {
      super.startActivityForResult(intent, requestCode);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(!checkNestedFragmentsForResult(requestCode, resultCode, data)) {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  /**
   * Checks to see whether there is any children fragments which has been
   * registered with {@code requestCode} before. If so, let it handle the
   * {@code requestCode}.
   * 
   * @param requestCode
   *          the code from {@link #onActivityResult(int, int, Intent)}.
   * @param resultCode
   *          the code from {@link #onActivityResult(int, int, Intent)}.
   * @param data
   *          the data from {@link #onActivityResult(int, int, Intent)}.
   * @return {@code true} if the results have been handed over to some child
   *         fragment. {@code false} otherwise.
   */
  protected boolean checkNestedFragmentsForResult(int requestCode,
      int resultCode, Intent data) {
    SparseIntArray reqCodes = getRequestCodes();
    final int id = reqCodes.get(requestCode);
    if(id == 0) {
      return false;
    }

    reqCodes.delete(requestCode);

    List<Fragment> fragments = getChildFragmentManager().getFragments();
    if(fragments == null) {
      return false;
    }

    for(Fragment fragment : fragments) {
      if(fragment.hashCode() == id) {
        fragment.onActivityResult(requestCode, resultCode, data);
        return true;
      }
    }

    return false;
  }

  /**
   * @return the requestCodes
   */
  public SparseIntArray getRequestCodes() {
    if(requestCodes == null) {
      requestCodes = new SparseIntArray();
    }
    return requestCodes;
  }

}
