// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// A base class for Fragments that is capable of handling nested Fragments
// Activity-starts from those fragments with proper handing over of possible
// result-codes from the activities to the appropriate fragment, which
// initially started the Activity.
//
// ////////////////////////////////////////////////////////////////////////////
// License:
// // // // // // // // // // // // // // // // // // // //
// Copyright 2014 Volkmar Seifert <vs@dimensionv.de>.
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
// notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY VOLKMAR SEIFERT AND CONTRIBUTORS ``AS IS''
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE FOUNDATION OR CONTRIBUTORS BE LIABLE
// FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
// DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
// SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
// CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
// LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
// OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
// DAMAGE.
//
// The views and conclusions contained in the software and documentation are
// those of the authors and should not be interpreted as representing official
// policies, either expressed or implied, of Volkmar Seifert <vs@dimensionv.de>.
// ////////////////////////////////////////////////////////////////////////////
package de.dimensionv.android.androtools.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A base class for {@code Fragments} that is capable of handling nested {@code Fragments}
 * and {@code Activity}-starts from those fragments with proper handing over of possible
 * result-codes from the activities to the appropriate fragment, which initially started the
 * {@code Activity}.</p>
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 *
 * @version 1.0
 * @since API 1.0.0
 */
public class BaseFragment extends Fragment {

  private HashMap<Integer, Fragment> requestCodes = null;

  /**
   * Registers request code (used in
   * {@link #startActivityForResult(Intent, int)}).
   * 
   * @param requestCode
   *          the request code.
   * @param fragment
   *          the fragment ID (can be {@link Fragment#getId()} of
   *          {@link Fragment#hashCode()}
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public void registerRequestCode(int requestCode, Fragment fragment) {
    getRequestCodesMap().put(requestCode, fragment);
  }

  /**
   * <p>Call {@link Activity#startActivityForResult(Intent, int)} on the fragment's
   * containing {@link Activity}.</p>
   * <p>In addition to that, prior to making that call to the activity's startActivityForResult method,
   * this method checks whether it is the child of another {@code Fragment}. If that is the case and the
   * parent {@code Fragment} is also an instance of {@code BaseFragment}, it will register
   * the request code with the parent fragment and call its startActivityForResult-method, in order
   * to ensure that when the started {@code Activity} will return, the result-processing is
   * handed down to the correct fragment.</p>
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  @Override
  public void startActivityForResult(Intent intent, int requestCode) {
    Fragment parent = getParentFragment();
    if((parent != null) && (parent instanceof BaseFragment)) {
      ((BaseFragment) parent).registerRequestCode(requestCode, this);
      parent.startActivityForResult(intent, requestCode);
    } else {
      super.startActivityForResult(intent, requestCode);
    }
  }

  /**
   * <p>Receive the result from a previous call to
   * {@link #startActivityForResult(Intent, int)}.  This follows the
   * related Activity API as described there in
   * {@link Activity#onActivityResult(int, int, Intent)}.</p>
   *
   * <p>If there are nested fragments, this method will check if one of them has registered the result
   * code and hands processing over to the appropriate fragment.</p>
   *
   * @param requestCode The integer request code originally supplied to
   *                    startActivityForResult(), allowing you to identify who this
   *                    result came from.
   * @param resultCode The integer result code returned by the child activity
   *                   through its setResult().
   * @param data An Intent, which can return result data to the caller
   *               (various data can be attached to Intent "extras").
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(!checkNestedFragmentsForResult(requestCode, resultCode, data)) {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  /**
   * Checks to see whether there are any nested child fragments which have been
   * registered with a {@code requestCode} before. If so, let it handle the
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
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  protected boolean checkNestedFragmentsForResult(int requestCode, int resultCode, Intent data) {
    Map<Integer, Fragment> reqCodes = getRequestCodesMap();
    final Fragment fragment = reqCodes.remove(requestCode);
    if(fragment == null) {
      return false;
    }

    fragment.onActivityResult(requestCode, resultCode, data);
    return true;
  }

  /**
   * Returns an unmodifiable {@link Map} of the registered request codes.
   *
   * @return the requestCodes
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public Map<Integer, Fragment> getRequestCodesM() {
    return Collections.unmodifiableMap(getRequestCodesMap());
  }

  /**
   * <p>Returns a {@link Map} of the registered request codes.</p>
   * <p>If the map is not instantiated, yet,
   * this method will take care of that, too.</p>
   *
   * @return A {@link Map} with the registered request codes.
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  private Map<Integer, Fragment> getRequestCodesMap() {
    if(requestCodes == null) {
      requestCodes = new HashMap<Integer, Fragment>();
    }
    return requestCodes;
  }

}
