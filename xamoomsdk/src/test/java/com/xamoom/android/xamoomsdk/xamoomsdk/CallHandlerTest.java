package com.xamoom.android.xamoomsdk.xamoomsdk;

import com.xamoom.android.xamoomsdk.CallHandler;
import com.xamoom.android.xamoomsdk.EnduserApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import at.rags.morpheus.Morpheus;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class CallHandlerTest {

  private Morpheus mMockMorpheus;
  private CallHandler mCallHandler;

  @Before
  public void setup() {
    mMockMorpheus = mock(Morpheus.class);
    mCallHandler = new CallHandler(mMockMorpheus);
  }

  @Test
  public void testConstructor() {
    CallHandler callHandler = new CallHandler(mMockMorpheus);

    assertNotNull(callHandler);
  }

  @Test(expected = NullPointerException.class)
  public void testEnqueCallWithNull() {
    mCallHandler.enqueCall(null, null);
  }

}
