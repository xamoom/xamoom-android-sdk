package com.xamoom.android.xamoomsdk;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.Helper.ContentFragmentActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class XamoomContentFragmentTest {

  @Test
  public void testNewInstance() {
    XamoomContentFragment fragment = XamoomContentFragment.newInstance("0FF", "api");

    ContentFragmentActivity activity = Robolectric.buildActivity( ContentFragmentActivity.class )
        .create()
        .start()
        .resume()
        .get();

    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add( fragment, null );
    fragmentTransaction.commit();

    assertNotNull(fragment);
  }
}
