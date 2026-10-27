package tz.tante.property.manager.enums;

public enum PropertyType
{
  LAND("Land"),
  STAND_ALONE_HOUSE("Stand Alone House"),
  APARTMENTS_BUILDING("Apartments Building"),
  COMPOUND("Compound"),
  COMPLEX("Complex");

  private String description;

  PropertyType(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }
}
