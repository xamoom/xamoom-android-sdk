/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

package com.xamoom.android.xamoomsdk.Enums;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.EnduserApi;

import java.util.EnumSet;

/**
 * ContentFlags are options you can use when getting content.
 *
 * @see EnduserApi#getContent(String, EnumSet, APICallback)
 */
public enum ContentFlags {
  /**
   * Use preview when you don't want to have statistics recorded.
   */
  PREVIEW,
  /**
   * Use private if you only want to receive {@link com.xamoom.android.xamoomsdk.Resource.ContentBlock}
   * with an *unchecked* "mobile exclusive" in the xamoom-backend.
   */
  PRIVATE
}
