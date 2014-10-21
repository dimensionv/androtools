/**
 *
 */
package de.dimensionv.android.androtools.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import de.dimensionv.common.utilities.StringUtils;

/**
 * Helpful tools for handling fragments.
 * 
 * @author mjoellnir
 * @version 1.0
 * @since 2.0
 */
public class FragmentTools {

  /**
   * Creates and embeds a new Fragment (given by fragmentClass) into the
   * container with the ID containerID of the <b>FragmentActivity</b> parent,
   * with given tag and arguments.
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
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @since Class 1.0
   * @since API 2.0
   */
  public static Fragment embedFragment(Class<? extends Fragment> fragmentClass, int containerID, FragmentActivity parent, String tag, Bundle arguments) throws InstantiationException,
      IllegalAccessException {
    return FragmentTools.embedFragment(fragmentClass, containerID, parent.getSupportFragmentManager(), tag, arguments);
  }

  /**
   * Creates and embeds a new Fragment (given by fragmentClass) into the
   * container with the ID containerID of the <b>Fragment</b> parent,
   * with given tag and arguments.
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
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @since Class 1.0
   * @since API 2.0
   */
  public static Fragment embedFragment(Class<? extends Fragment> fragmentClass, int containerID, Fragment parent, String tag, Bundle arguments) throws InstantiationException,
      IllegalAccessException {
    return FragmentTools.embedFragment(fragmentClass, containerID, parent.getChildFragmentManager(), tag, arguments);
  }

  /**
   * Creates and embeds a new Fragment (given by fragmentClass) into the
   * container with the ID containerID, using the <b>FragmentManager</b>
   * fragmentManager, with given tag and arguments.
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
   * @since Class 1.0
   * @since API 2.0
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

  @SuppressWarnings("unchecked")
  @Deprecated
  public static final <T extends View> T findView(View parent, int id) {
    return (T) parent.findViewById(id);
  }
}
