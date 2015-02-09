// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// Helpful tools regarding fragments.
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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import de.dimensionv.java.libraries.common.utilities.strings.StringUtils;

/**
 * Helpful tools for handling {@code Fragments}.
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 *
 * @version 1.0
 * @since API 1.0.0
 */
@SuppressWarnings("UnusedDeclaration")
public class FragmentTools {

  /**
   * Creates and embeds a new {@link Fragment} (given by {@code fragmentClass}) into the
   * container with the ID {@code containerID} of the {@link FragmentActivity} parent,
   * with given {@code tag} and {@code arguments}.
   * 
   * @param fragmentClass
   *          The class of the new fragment that shall be created and embedded.
   * @param containerID
   *          The ID of the container that shall receive the new fragment.
   * @param parent
   *          The Activity containing the container.
   * @param tag
   *          The tag that's assigned to the fragment when attaching it to the
   *          parent. May be null or empty.
   * @param arguments
   *          Possible arguments to be passed on to the fragment, may be null.
   *
   * @throws IllegalAccessException
   * @throws InstantiationException
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static Fragment embedFragment(Class<? extends Fragment> fragmentClass, int containerID, FragmentActivity parent, String tag, Bundle arguments) throws InstantiationException,
      IllegalAccessException {
    return FragmentTools.embedFragment(fragmentClass, containerID, parent.getSupportFragmentManager(), tag, arguments);
  }

  /**
   * Creates and embeds a new {@link Fragment} (given by {@code fragmentClass}) into the
   * container with the ID {@code containerID} of the {@link Fragment} parent,
   * with given {@code tag} and {@code arguments}.
   * 
   * @param fragmentClass
   *          The class of the new fragment that shall be created and embedded.
   * @param containerID
   *          The ID of the container that shall receive the new fragment.
   * @param parent
   *          The Fragment containing the container.
   * @param tag
   *          The tag that's assigned to the fragment when attaching it to the
   *          parent. May be null or empty.
   * @param arguments
   *          Possible arguments to be passed on to the fragment, may be null.
   *
   * @throws IllegalAccessException
   * @throws InstantiationException
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static Fragment embedFragment(Class<? extends Fragment> fragmentClass, int containerID, Fragment parent, String tag, Bundle arguments) throws InstantiationException, IllegalAccessException {
    return FragmentTools.embedFragment(fragmentClass, containerID, parent.getChildFragmentManager(), tag, arguments);
  }

  /**
   * Creates and embeds a new {@link Fragment} (given by {@code fragmentClass}) into the
   * container with the ID {@code containerID} of the {@link FragmentManager} parent,
   * with given {@code tag} and {@code arguments}.
   * 
   * @param fragmentClass
   *          The class of the new fragment that shall be created and embedded.
   * @param containerID
   *          The ID of the container that shall receive the new fragment.
   * @param fragmentManager
   *          The FragmentManager that shall be used to receive and manage the
   *          fragment.
   * @param tag
   *          The tag that's assigned to the fragment when attaching it to the
   *          parent. May be null or empty.
   * @param arguments
   *          Possible arguments to be passed on to the fragment, may be null.
   * @throws IllegalAccessException
   * @throws InstantiationException
   *
   * @since Class 1.0
   * @since API 1.0.0
   */
  public static Fragment embedFragment(Class<? extends Fragment> fragmentClass, int containerID, FragmentManager fragmentManager, String tag,
      Bundle arguments) throws InstantiationException,
      IllegalAccessException {
    Fragment fragment = fragmentClass.newInstance();
    if(arguments != null) {
      fragment.setArguments(arguments);
    }
    FragmentTransaction ft = fragmentManager.beginTransaction();
    if(StringUtils.isEmpty(tag)) {
      ft.replace(containerID, fragment);
    } else {
      ft.replace(containerID, fragment, tag);
    }
    ft.commit();
    return fragment;
  }

  /**
   * Removes the {@link Fragment} specified by the {@code tag} from view.
   *
   * @param fragmentManager
   *          The FragmentManager that shall be used to receive and manage the
   *          fragment.
   * @param tag
   *          The tag that was assigned to the fragment when it was attaching to the
   *          parent.
   * @throws InstantiationException
   * @throws IllegalAccessException
   *
   * @since Class 2.0
   * @since API 1.1.0
   */
  public static void removeFragment(FragmentManager fragmentManager, String tag) throws InstantiationException, IllegalAccessException {
    removeFragment(fragmentManager, fragmentManager.findFragmentByTag(tag));
  }

  /**
   * Removes the specified {@link Fragment} from view.
   *
   * @param fragmentManager
   *          The FragmentManager that shall be used to receive and manage the
   *          fragment.
   * @param fragment
   *          The {@code Fragment} to be removed.
   * @throws InstantiationException
   * @throws IllegalAccessException
   *
   * @since Class 2.0
   * @since API 1.1.0
   */
  public static void removeFragment(FragmentManager fragmentManager, Fragment fragment) throws InstantiationException, IllegalAccessException {
    FragmentTransaction ft = fragmentManager.beginTransaction();
    ft.remove(fragment);
    ft.commit();
  }
}
