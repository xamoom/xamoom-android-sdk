package com.xamoom.android.xamoomsdk.xamoomsdk;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.CallHandler;
import com.xamoom.android.xamoomsdk.EnduserApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import at.rags.morpheus.Morpheus;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

  @Test(expected = NullPointerException.class)
  public void testEnqueListCallWithNull() {
    mCallHandler.enqueListCall(null, null);
  }

}
