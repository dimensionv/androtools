// ////////////////////////////////////////////////////////////////////////////
//
// Author: Volkmar Seifert
// Description:
// Provides static pre-initialized fixed empty (zero-sized) arrays for the
// primitive data types.
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
package de.dimensionv.android.androtools.datastructures.arrays;

/**
 * Provides static pre-initialized fixed empty (zero-sized) arrays for the primitive data types.
 *
 * @author Volkmar Seifert &lt;vs@DimensionV.de&gt;
 * @version 1.0
 * @since API 2.0.0
 */
public final class EmptyArray {
  /**
   * Pre-initialized empty (zero-sized) array of type {@code boolean}.
   */
  public static final boolean[] BOOLEAN = new boolean[0];
  /**
   * Pre-initialized empty (zero-sized) array of type {@code byte}.
   */
  public static byte[] BYTE = new byte[0];
  /**
   * Pre-initialized empty (zero-sized) array of type {@code int}.
   */
  public static int[] INT = new int[0];
  /**
   * Pre-initialized empty (zero-sized) array of type {@code long}.
   */
  public static long[] LONG = new long[0];
  /**
   * Pre-initialized empty (zero-sized) array of type {@code float}.
   */
  public static float[] FLOAT = new float[0];
  /**
   * Pre-initialized empty (zero-sized) array of type {@code double}.
   */
  public static double[] DOUBLE = new double[0];
  /**
   * Pre-initialized empty (zero-sized) array of type {@code char}.
   */
  public static char[] CHAR = new char[0];
}
