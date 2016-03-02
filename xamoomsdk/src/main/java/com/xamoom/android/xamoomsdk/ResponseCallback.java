package com.xamoom.android.xamoomsdk;


import android.util.Log;

import com.xamoom.android.xamoomsdk.Resource.Error.ErrorMessage;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Internal callbacks used in {@link EnduserApiInterface}.
 */
public abstract class ResponseCallback<T> implements Callback<T> {
  public abstract void failure(ErrorMessage error);
  private static final String TAG = ResponseCallback.class.getSimpleName();

  @Override
  public void failure(RetrofitError error) {
    Log.e(TAG, error.toString());

    ErrorMessage errorMessage = (ErrorMessage) error.getBodyAs(ErrorMessage.class);
    if (errorMessage != null) {
      failure(errorMessage);
    } else {
      failure(new ErrorMessage());
    }
  }
}
