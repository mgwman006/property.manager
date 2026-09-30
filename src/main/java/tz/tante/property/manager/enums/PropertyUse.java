package tz.tante.property.manager.enums;

public enum PropertyUse
{
  RESIDENTIAL("Residential"),
  COMMERCIAL("Commercial"),
  INDUSTRIAL("Industrial"),
  MIXED_USE("Mixed Use");

  private String description;

  PropertyUse(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }
}
