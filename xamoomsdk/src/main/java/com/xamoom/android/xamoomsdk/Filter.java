package com.xamoom.android.xamoomsdk;

import java.util.ArrayList;
import java.util.Date;

public class Filter {
  private String name;
  private ArrayList<String> tags;
  private Date fromDate;
  private Date toDate;
  private String relatedSpotId;

  private Filter(FilterBuilder builder) {
    this.name = builder.name;
    this.tags = builder.tags;
    this.fromDate = builder.fromDate;
    this.toDate = builder.toDate;
    this.relatedSpotId = builder.relatedSpotId;
  }

  public String getName() {
    return name;
  }

  public ArrayList<String> getTags() {
    return tags;
  }

  public Date getFromDate() {
    return fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public String getRelatedSpotId() {
    return relatedSpotId;
  }

  public static class FilterBuilder {
    private String name;
    private ArrayList<String> tags;
    private Date fromDate;
    private Date toDate;
    private String relatedSpotId;

    public FilterBuilder() {
    }

    public FilterBuilder name(String name) {
      this.name = name;
      return this;
    }

    public FilterBuilder tags(ArrayList<String> tags) {
      this.tags = tags;
      return this;
    }

    public FilterBuilder addTag(String tag) {
      if (tags == null) {
        tags = new ArrayList<>();
      }
      tags.add(tag);
      return this;
    }

    public FilterBuilder fromDate(Date fromDate) {
      this.fromDate = fromDate;
      return this;
    }

    public FilterBuilder toDate(Date toDate) {
      this.toDate = toDate;
      return this;
    }

    public FilterBuilder relatedSpotId(String relatedSpotId) {
      this.relatedSpotId = relatedSpotId;
      return this;
    }

    public Filter build() {
      return new Filter(this);
    }
  }
}
