package tz.tante.property.manager.enums;

public enum PropertyRegister
{
  AGENT("Agent"),
  OWNER("Owner"),
  RENTAL_PROFILE("Rental Profile"),
  CONTRACTOR("Contractor");

  private String description;

  PropertyRegister(String description)
  {
    this.description = description;
  }

  public String getDescription()
  {
    return description;
  }
}
