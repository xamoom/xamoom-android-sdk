package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentBlockDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.Database.MenuDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.internal.runtime.RuntimeAdapter;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MenuDatabaseAdapterTest {
  private MenuDatabaseAdapter mMenuDatabaseAdapter;
  private ContentDatabaseAdapter mMockedContentDatabaseAdapter;
  private DatabaseHelper mMockedDatabaseHelper;
  private SQLiteDatabase mMockedDatabase;
  private Cursor mMockedCursor;

  @Before
  public void setup() {
    mMenuDatabaseAdapter = MenuDatabaseAdapter.getInstance(RuntimeEnvironment.application);
    mMockedContentDatabaseAdapter = Mockito.mock(ContentDatabaseAdapter.class);
    mMockedDatabaseHelper = mock(DatabaseHelper.class);
    mMockedDatabase = mock(SQLiteDatabase.class);
    mMockedCursor = mock(Cursor.class);
    mMenuDatabaseAdapter.setDatabaseHelper(mMockedDatabaseHelper);
    mMenuDatabaseAdapter.setContentDatabaseAdapter(mMockedContentDatabaseAdapter);

    Mockito.stub(mMockedDatabaseHelper.getWritableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabaseHelper.getReadableDatabase())
        .toReturn(mMockedDatabase);
  }

  @Test
  public void getMenu() {
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);

    Menu savedMenu = mMenuDatabaseAdapter.getMenu("1");

    Mockito.verify(mMockedDatabase).query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString());

    Assert.assertNotNull(savedMenu);
  }

  @Test
  public void getMenuWithRow() {
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);

    Menu savedMenu = mMenuDatabaseAdapter.getMenu(1);

    Mockito.verify(mMockedDatabase).query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString());

    Assert.assertNotNull(savedMenu);
  }

  @Test
  public void testInsertOrUpdateNewEntity() {
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);
    Mockito.stub(mMockedCursor.getInt(anyInt())).toReturn(-1);

    Content content1 = new Content();
    content1.setId("2");

    Content content2 = new Content();
    content2.setId("3");

    ArrayList<Content> contents = new ArrayList<>();
    contents.add(content1);
    contents.add(content2);

    Menu menu = new Menu();
    menu.setId("1");
    menu.setItems(contents);

    long row = mMenuDatabaseAdapter.insertOrUpdate(menu, 0);

    Mockito.verify(mMockedDatabase).insert(anyString(), anyString(), any(ContentValues.class));
    Mockito.verify(mMockedContentDatabaseAdapter).insertOrUpdateContent(eq(content1), eq(true), anyInt());
    Mockito.verify(mMockedContentDatabaseAdapter).insertOrUpdateContent(eq(content2), eq(true), anyInt());
  }

  @Test
  public void testInsertOrUpdateExisiting() {
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);
    Mockito.stub(mMockedCursor.getInt(anyInt())).toReturn(1);

    Menu menu = new Menu();
    menu.setId("1");

    long row = mMenuDatabaseAdapter.insertOrUpdate(menu, 0);

    Mockito.verify(mMockedDatabase).update(eq(OfflineEnduserContract.MenuEntry.TABLE_NAME),
        any(ContentValues.class), anyString(), any(String[].class));
  }

  @Test
  public void testDelete() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(1);

    boolean deleted = mMenuDatabaseAdapter.deleteMenu("1");

    junit.framework.Assert.assertTrue(deleted);
  }

  @Test
  public void testDeleteFail() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(0);

    boolean deleted = mMenuDatabaseAdapter.deleteMenu("1");

    junit.framework.Assert.assertFalse(deleted);
  }
}
