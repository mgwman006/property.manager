package tz.tante.property.manager.utilities;

public class Utility
{
  public static String generateCode(String prefix, Long sequence)
  {
    return String.format("%s-%06d", prefix, sequence);
  }
}
