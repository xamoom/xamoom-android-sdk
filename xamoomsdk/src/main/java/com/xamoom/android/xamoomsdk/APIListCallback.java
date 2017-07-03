/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk;

/**
 * Generic callback for lists.
 */
public interface APIListCallback<T, U> {
  void finished(T result, String cursor, boolean hasMore);
  void error(U error);
}
