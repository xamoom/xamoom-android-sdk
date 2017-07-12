/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Helper;

import android.annotation.SuppressLint;
import android.support.v4.app.FragmentActivity;

import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.Resource.Content;

@SuppressLint("Registered")
public class ContentFragmentActivity extends FragmentActivity implements XamoomContentFragment.OnXamoomContentFragmentInteractionListener {
  @Override
  public void clickedContentBlock(Content content) {

  }

  @Override
  public void clickedSpotMapContentLink(String contentId) {

  }
}
