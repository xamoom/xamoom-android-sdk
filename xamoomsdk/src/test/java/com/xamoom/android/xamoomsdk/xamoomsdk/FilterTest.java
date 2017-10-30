package com.xamoom.android.xamoomsdk.xamoomsdk;

import com.xamoom.android.xamoomsdk.Filter;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class FilterTest {

  @Test
  public void testBuilder() {
    String name = "name";
    ArrayList<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");
    Date date = new Date();
    String relatedSpotId = "1234";

    Filter filter = new Filter.FilterBuilder()
        .name(name)
        .tags(tags)
        .fromDate(date)
        .toDate(date)
        .relatedSpotId(relatedSpotId)
        .build();

    assertEquals(name, filter.getName());
    assertEquals(tags, filter.getTags());
    assertEquals(date, filter.getFromDate());
    assertEquals(date, filter.getToDate());
    assertEquals(relatedSpotId, filter.getRelatedSpotId());
  }

  @Test
  public void testBuilderAddTags() {
    ArrayList<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    Filter filter = new Filter.FilterBuilder()
        .addTag("tag1")
        .addTag("tag2")
        .build();

    assertEquals(tags, filter.getTags());
  }
}
