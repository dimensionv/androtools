// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// Helpful tools regarding views.
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
import android.app.Dialog;
import android.view.View;

/**
 * Helpful tools for handling views.
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 *
 * @version 1.0
 * @since API 1.0.0
 */
public class ViewTools {

  /**
   * <p>Returns the {@code View} object with the given {@code id} from the
   * {@code View} object provided by {@code parent}.</p>
   * <p>This method is a convenience-method to keep the actual code from being overly cluttered
   * with castings, as casting into the actual {@code View} object is done via Templates.</p>
   *
   * @param parent
   *     The parent {@code View} object containing the requested child {@code View}
   *     object.
   * @param id
   *     The resource-ID of the requested child {@code View} object.
   * @param <T>
   *     This is the left-hand side of the assignent, and can be any class extending
   *     {@code View}.
   *
   * @return The appropriately cast {@code View} object requested by the given {@code id}
   * or null if it could not be found within the parent {@code View} object.
   *
   * @since Class 1.0, API 1.0.0
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T findView(View parent, int id) {
    return (T) parent.findViewById(id);
  }

  /**
   * <p>Returns the {@code View} object with the given {@code id} from the
   * {@code Activity} object provided by {@code parent}.</p>
   * <p>This method is a convenience-method to keep the actual code from being overly cluttered
   * with castings, as casting into the actual {@code View} object is done via Templates.</p>
   *
   * @param parent
   *     The {@code Activity} object containing the requested child {@code View}
   *     object.
   * @param id
   *     The resource-ID of the requested child {@code View} object.
   * @param <T>
   *     This is the left-hand side of the assignent, and can be any class extending
   *     {@code View}.
   *
   * @return The appropriately cast {@code View} object requested by the given {@code id}
   * or null if it could not be found within the {@code Activity} object.
   *
   * @since Class 1.0, API 1.0.0
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T findView(Activity parent, int id) {
    return (T) parent.findViewById(id);
  }

  /**
   * <p>Returns the {@code View} object with the given {@code id} from the
   * {@code Dialog} object provided by {@code parent}.</p>
   * <p>This method is a convenience-method to keep the actual code from being overly cluttered
   * with castings, as casting into the actual {@code View} object is done via Templates.</p>
   *
   * @param parent
   *     The {@code Dialog} object containing the requested child {@code View}
   *     object.
   * @param id
   *     The resource-ID of the requested child {@code View} object.
   * @param <T>
   *     This is the left-hand side of the assignent, and can be any class extending
   *     {@code View}.
   *
   * @return The appropriately cast {@code View} object requested by the given {@code id}
   * or null if it could not be found within the {@code Dialog} object.
   *
   * @since Class 1.0, API 1.0.0
   */
  @SuppressWarnings("unchecked")
  public static final <T extends View> T findView(Dialog parent, int id) {
    return (T) parent.findViewById(id);
  }
}
